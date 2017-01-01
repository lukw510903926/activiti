package com.eastcom.esflow.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eastcom.esflow.bean.AbstractVariable;
import com.eastcom.esflow.bean.AbstractVariableInstance;
import com.eastcom.esflow.bean.ActBizInfoDelayTime;
import com.eastcom.esflow.bean.BizInfo;
import com.eastcom.esflow.bean.ProcessVariableInstance;
import com.eastcom.esflow.common.exception.ServiceException;
import com.eastcom.esflow.common.utils.DataGrid;
import com.eastcom.esflow.common.utils.Json;
import com.eastcom.esflow.common.utils.LoginUser;
import com.eastcom.esflow.common.utils.PageHelper;
import com.eastcom.esflow.service.ActBizInfoDelayTimeService;
import com.eastcom.esflow.service.IBizInfoService;
import com.eastcom.esflow.service.IDictService;
import com.eastcom.esflow.service.IProcessDefinitionService;
import com.eastcom.esflow.service.IProcessExecuteService;
import com.eastcom.esflow.service.IVariableInstanceService;
import com.eastcom.esflow.service.RoleService;
import com.eastcom.esflow.util.WebUtil;

@Controller
@RequestMapping("/workflow")
public class ProcessExecuteController {
	
	@Autowired
	private IProcessExecuteService processExecuteService;
	
	@Autowired
	private IProcessDefinitionService processDefinitionService ;
	
	@Autowired
	private IBizInfoService bizInfoService ;
	
	@Autowired
	private IDictService dictService;
	
	@Autowired
	private ActBizInfoDelayTimeService actBizInfoDelayTimeService; 
	
	@Autowired
	private IVariableInstanceService taskInstanceService; 
	
	@Autowired
	private RoleService roleService;
	
	@Value("${web.maxUploadSize}")
	private long maxUpload;
	
	private Logger logger = Logger.getLogger("processExecuteController");
	
	@RequestMapping(value = "/loadWorkLogInput" )
	@ResponseBody
	public Map<String, Object> loadWorkLogInput(String logId) {
		Map<String, Object> map = processExecuteService.loadBizLogInput(logId);
		return map == null ? new HashMap<String, Object>(0) : map;
	}

	@RequestMapping(value = "")
	public String index(Model model) {
		
		Map<String, Object> list = processExecuteService.loadProcessList();
		model.addAttribute("ProcessMapJson", list == null ? "" : JSONObject.toJSON(list).toString());
		List<Map<String, Object>> notices = new ArrayList<Map<String, Object>>();
		model.addAttribute("notices", notices);
		return "work/index";
	}

	/**
	 * target取值如下<br>
	 * myComplete : 待办工单<br>
	 * myClaim : 待签任务<br>
	 * query : 全局查询<br>
	 * myCreate : 我创建的单<br>
	 * myHandle : 我处理过的单<br>
	 * myClose : 我创建并关闭的单
	 * @param target
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/queryWorkOrder/")
	public DataGrid queryWorkOrder(@RequestParam Map<String, Object> params, PageHelper<BizInfo> page,HttpServletRequest request, HttpServletResponse response) {

		WebUtil.getLogoUser(request, response);
		String action = (String) params.get("action");
		PageHelper<BizInfo> helper = processExecuteService.queryMyBizInfos(action, params, page);
		DataGrid grid = new DataGrid();
		if (helper != null) {
			grid.setRows(helper.getList());
			grid.setTotal(helper.getTotal());
		}
		return grid;
	}
	
	@ResponseBody
	@RequestMapping(value = "/loadUserGroup")
	public List<Map<String, Object>> loadUserGroup(String parentID) {
		
		return processExecuteService.loadUserGroup(parentID);
	}

	/**
	 * 创建工单
	 * 
	 * @param tempID
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public Map<String, Object> create(String key, HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		WebUtil.getLogoUser(request, response);
		ProcessDefinition pd = processDefinitionService.getLatestProcDefByKey(key);
		if(pd != null) {
			String proDefId = pd.getId();
			data.put("base_tempID", proDefId);
			List<AbstractVariable> list = processExecuteService.loadHandleProcessVariables(proDefId);
			Map<String, String> buttons = processExecuteService.loadStartButtons(proDefId);
			data.put("SYS_BUTTON", buttons);
			Map<String, Object> map = groupProcessValBean(list, null);
			if (map.get(IProcessExecuteService.systemFormType) != null) {
				AbstractVariable pva = (AbstractVariable) map.get(IProcessExecuteService.systemFormType);
				data.put(IProcessExecuteService.systemFormType, pva);
			}
			if (map.containsKey(IProcessExecuteService.systemFormType)) {
				map.remove(IProcessExecuteService.systemFormType);
			}
			data.put("ProcessValBeanMap", map);
			data.put("result", true);
		} else {
			data.put("result", false);
			data.put("msg", "流程【"+ key +"】未找到!");
		}
		return data;
	}

	/**
	 * 显示某个工单信息
	 * 
	 * @param workNumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/display")
	public Map<String, Object> display(String id, String workNumber, HttpServletRequest request,HttpServletResponse response) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		LoginUser LoginUser = WebUtil.getLogoUser(request, response);
		Set<String> roles = roleService.getUserRolesByUserName(LoginUser.getUsername());
		Map<String, Object> result = processExecuteService.queryWorkOrder(id);
		String sysFormType = null;
		data.put("CURRE_OP", result.get("CURRE_OP"));
		result.remove("CURRE_OP");
		List<AbstractVariable> list = (List<AbstractVariable>) result.get("ProcessValBeanMap");
		Map<String, Object> map = groupProcessValBean(list, sysFormType);
		if (map.containsKey(IProcessExecuteService.systemFormType)) {
			AbstractVariable processVal = (AbstractVariable) map.get(IProcessExecuteService.systemFormType);
			map.remove(IProcessExecuteService.systemFormType);
			if (processVal != null) {
				AbstractVariableInstance pia = new ProcessVariableInstance();
				pia.setVariable(processVal);
				pia.setValue(sysFormType);
				data.put(IProcessExecuteService.systemFormType, pia);
			}
		}
		if(roles != null && roles.contains("工时修改")){
			data.put("reSetWorkTime", true);
		}else{
			data.put("reSetWorkTime", false);
		}
		data.put("ProcessValBeanMap", map);
		result.remove("ProcessValBeanMap");
		data.put("ProcessTaskValBeans", result.get("ProcessTaskValBeans"));
		result.remove("ProcessTaskValBeans");
		data.put("SYS_BUTTON", result.get("SYS_BUTTON"));
		result.remove("SYS_BUTTON");
		data.put("workLoad",result.get("workLoad"));
		data.put("workBean", result);
		Date delayTime = new Date();
		ActBizInfoDelayTime actBizInfoDelayTime = actBizInfoDelayTimeService.findActBizInfoByBizId(id, null);
		if(actBizInfoDelayTime!=null){
			delayTime = actBizInfoDelayTime.getDelayTime();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(delayTime != null)
				data.put("delayTime", dateFormat.format(delayTime));
		}
		return data;
	}
	
	@ResponseBody
	@SuppressWarnings("unchecked")
	@RequestMapping("/resetWorkTime")
	public Json resetWorkTime(@RequestParam String workTime){
		
		Json json = new Json();
		try {
			List<Map<String,String>> list = (List<Map<String, String>>) JSONArray.parse(workTime);
			this.taskInstanceService.reSetWorkTime(list);
		} catch (Exception e) {
			json.setMsg("工时修改失败");
			json.setSuccess(false);
			return json;
		}
		json.setMsg("工时修改成功");
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping(value = "bizInfo")
	public ResponseEntity<String> createBiz(@RequestParam Map<String, Object> params, boolean startProc,String[] deleFileId, MultipartHttpServletRequest request, HttpServletResponse response) {
	
		Json json = new Json();
		WebUtil.getLogoUser(request, response);
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
		BizInfo bean = null;
		try {
			json.setSuccess(false);
			if(validateFileSize(request)){
				json.setMsg("操作失败: " + "附件大小不能超过"+maxUpload/1024/1024+"M");
				ResponseEntity<String> entity = new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
				return entity;
			}
			bean = processExecuteService.createBizDraft(params, request.getMultiFileMap(), startProc, deleFileId);
		} catch (Exception e) {
			logger.info("createBiz error : ",e);
			String message = "" + (e.getCause() == null ? e.getLocalizedMessage() : e.getCause());
			message = message.replaceAll("[a-zA-Z]", "").replaceAll("\\.","");
			json.setMsg("操作失败: " + message);
			ResponseEntity<String> entity = new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
			return entity;
		}
		json.setSuccess(true);
		if(startProc){
			json.setMsg("/biz/" + bean.getId());
		}else
			json.setMsg("/biz?action=myWork");
		ResponseEntity<String> entity = new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
		return entity;
	}
	
	@RequestMapping(value = "bizInfo/{id}")
	public ResponseEntity<String> updateBiz(@PathVariable String id, @RequestParam Map<String, Object> params, boolean startProc, MultipartHttpServletRequest request, HttpServletResponse response) {
		
		Json json = new Json();
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
		try {			
			WebUtil.getLogoUser(request, response);
			json.setSuccess(false);
			if(validateFileSize(request)){
				json.setMsg("操作失败: " + "附件大小不能超过"+maxUpload/1024/1024+"M");
				ResponseEntity<String> entity = new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
				return entity;
			}
			processExecuteService.updateBiz(id, params, request.getMultiFileMap(), startProc);
		} catch (Exception e) {
			logger.info("error",e);
			String message = "" + (e.getCause() == null ? e.getLocalizedMessage() : e.getCause());
			message = message.replaceAll("[a-zA-Z]", "").replaceAll("\\.","");
			json.setMsg("操作失败: " + message);
			ResponseEntity<String> entity = new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
			return entity;
		}
		json.setSuccess(true);
		json.setMsg("操作成功");
		ResponseEntity<String> entity = new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
		return entity;
	}

	/**
	 * 工单处理，自动处理工单的各种状态的提交
	 * 
	 * @param params
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/submit")
	public ResponseEntity<String> submit(@RequestParam Map<String, Object> params, String[] uploadDesc, MultipartHttpServletRequest request, HttpServletResponse response) {
	
		Json json = new Json();
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
		boolean max = validateFileSize(request);
		try {
			params.put("_uploadDesc", uploadDesc);
			WebUtil.getLogoUser(request, response);
			json.setSuccess(false);
			if(validateFileSize(request)){
				json.setMsg("操作失败: " + "附件大小不能超过"+maxUpload/1024/1024+"M");
				ResponseEntity<String> entity = new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
				return entity;
			}
			processExecuteService.submit(params, request.getMultiFileMap());
		} catch (Exception e) {
			json.setSuccess(false);
			e.printStackTrace();
			String message = "" + (e.getCause() == null ? e.getLocalizedMessage() : e.getCause());
			message = message.replaceAll("[a-zA-Z]", "").replaceAll("\\.","");
			if(max){
				message = "附件大小不能超过"+maxUpload/1024/1024+"M";
			}
			json.setMsg("操作失败: " + message);
			ResponseEntity<String> entity = new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
			return entity;
		}

		json.setSuccess(true);
		json.setMsg("操作成功");
		ResponseEntity<String> entity = new ResponseEntity<String>(JSONObject.toJSONString(json), responseHeaders, HttpStatus.OK);
		return entity;
	}
	
	private boolean validateFileSize(MultipartHttpServletRequest request){
		
		long fileSize = 0L;
		MultiValueMap<String, MultipartFile> multiValueMap = request.getMultiFileMap();
		if(multiValueMap!=null && !multiValueMap.isEmpty()){
			for(String fileCatalog : multiValueMap.keySet()){
				List<MultipartFile> filesLists = (List<MultipartFile>) multiValueMap.get(fileCatalog);
				CommonsMultipartFile[] files = new  CommonsMultipartFile[filesLists.size()];
				filesLists.toArray(files);
				for (MultipartFile commonsMultipartFile : files) {
					fileSize = fileSize + commonsMultipartFile.getSize();
				}
			}
		}
		if(maxUpload<(fileSize)){
			return true;
		}
		return false;
	}
	
	@ResponseBody
	@RequestMapping(value = "/bizInfo/delete")
	public Json deleteBizInfo(@RequestParam List<String> ids){
		
		Json json = new Json();
		try {
			bizInfoService.updateBizByIds(ids);
		} catch (ServiceException e) {
			json.setSuccess(false);
			json.setMsg("操作失败: " + (e.getCause() == null ? e.getLocalizedMessage() : e.getCause()));
			return json;
		}
		json.setSuccess(true);
		json.setMsg("操作成功");
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "/download")
	public void downloadFile(String action, String id, HttpServletRequest request, HttpServletResponse response) {
		
		Object[] result = processExecuteService.downloadFile(action, id);
		if (result[1] == null) {
			return;
		}
		InputStream is = (InputStream) result[1];
		String fileType = (String) result[0];
		Long fileLong = (Long) result[2];
		String fileName = (String) result[3];
		response.reset();
		if ("IMAGE".equalsIgnoreCase(fileType)) {
			response.setContentType("image/PNG;charset=GB2312");
		} else {
			try {
				fileName = new String(fileName.getBytes("utf-8"), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setContentType("application/x-download");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			response.setHeader("Content-Length", fileLong == null ? "0" : String.valueOf(fileLong));
		}
		if (is != null) {
			OutputStream output = null;
			try {
				output = response.getOutputStream();// 得到输出流
				int size = 2048;
				byte[] b = new byte[size];
				int p = 0;
				while ((p = is.read(b)) > 0) {
					output.write(b, 0, p);
					if (p < size) {
						break;
					}
				}
			} catch (Exception e) {
			} finally {
				try {
					is.close();// 关闭文件流

				} catch (Exception e) {
				}
				try {
					output.flush();
					output.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 将属性进行分组
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> groupProcessValBean(List<AbstractVariable> list, String subTypeValue) {
	
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (AbstractVariable bean : list) {
			String groupName = bean.getGroupName();
			groupName = StringUtils.isEmpty(groupName) ? "其它信息" : groupName;
			List<AbstractVariable> temp = (List<AbstractVariable>) map.get(groupName);
			if (temp == null) {
				temp = new ArrayList<AbstractVariable>();
				map.put(groupName, temp);
			}
			temp.add(bean);
		}
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/interface/create")
	public String submit2(@RequestParam Map<String, Object> params, HttpServletRequest request,HttpServletResponse response, Model model) {
		
		String loginUser = (String) params.get("loginUser");
		LoginUser user = WebUtil.getLogoUser(request, response);
		if (user == null) {
			return "ERROR:找不到登录用户:" + loginUser;
		}
		params.put("$source", "alarm"); // 当前只有告警调用此接口创单
		// 根据接口获取登录用户
		BizInfo bean = null;
		try {
			bean = processExecuteService.submit(params, null);
		} catch (Exception e) {
			return "ERROR:处理失败-" + e.getMessage();
		}
		return bean.getBizId();
	}

	@ResponseBody
	@RequestMapping(value = "/interface/update")
	public String update(@RequestParam Map<String, Object> params, HttpServletRequest request,HttpServletResponse response, Model model) {
	
		String loginUser = (String) params.get("loginUser");
		LoginUser user = WebUtil.getLogoUser(request, response);
		if (user == null) {
			return "ERROR:找不到登录用户:" + loginUser;
		}
		// 根据接口获取登录用户
		try {
			processExecuteService.update(params);
		} catch (Exception e) {
			return "ERROR:处理失败-" + e.getMessage();
		}
		return "true";
	}

	@ResponseBody
	@RequestMapping(value = "/interface/getWorkOrderInfo")
	public String getWorkOrderInfo(String workNumber, HttpServletRequest request, HttpServletResponse response) {
		
		try {
			BizInfo mapInfo = processExecuteService.getBizInfo(null, workNumber);
			return JSONObject.toJSON(mapInfo).toString();
		} catch (Exception e) {
			return "ERROR:获取数据失败:" + e.getMessage();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/roleList")
	public JSONArray getRoleList(){
		
		List<Map<String, Object>> roles = roleService.getRoleRootNodes(new HashMap<String,String>());
		return JSONObject.parseArray(JSONObject.toJSONString(roles));
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/dictComboBoxList")
	public List<Map<String, Object>> dictComboBoxList(@RequestParam Map<String,Object> param) {
		
		return dictService.loadDictDataByParam(param);
	}
}
