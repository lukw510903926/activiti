package com.activiti.web.controller.view;

import com.activiti.core.bean.AbstractVariable;
import com.activiti.core.bean.AbstractVariableInstance;
import com.activiti.core.bean.BizFile;
import com.activiti.core.bean.ProcessVariableInstance;
import com.activiti.core.common.utils.Json;
import com.activiti.core.common.utils.LoginUser;
import com.activiti.core.service.IBizFileService;
import com.activiti.core.service.IBizInfoService;
import com.activiti.core.service.IProcessExecuteService;
import com.activiti.core.service.RoleService;
import com.activiti.core.service.act.ActProcessService;
import com.activiti.core.util.WebUtil;
import com.alibaba.fastjson.JSONObject;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class BizController{

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private ActProcessService actProcessService;

	@Autowired
	private IProcessExecuteService processExecuteService;
	
	@Autowired
	private IBizInfoService workService;
	
	@Autowired
	private IBizFileService bizFileService;
	
	@Autowired
	private RoleService roleService;
	
	@ResponseBody
	@RequestMapping("/biz/getProcessStatus")
	public List<String> getProcessStatus(@RequestParam Map<String,String> params){
		
		String processName = params.get("processName[]")==null?params.get("processName"):params.get("processName[]");
		List<ProcessDefinition> list = actProcessService.getList();
		if(StringUtils.isBlank(processName))
			return this.workService.loadBizInfoStatus(null);
		for(ProcessDefinition process:list){
			if(processName.equals(process.getName())){
				return this.workService.loadBizInfoStatus(process.getId());
			}
		}
		return new ArrayList<String>();
	}
	
	/**
	 * 工单管理视图
	 * 
	 * @return
	 */
	@RequestMapping(value = "biz")
	public String queryView(@RequestParam Map<String,Object> params, Model model) {
		
		for (String key : params.keySet()) {
			if(params.get(key)!=null){
				model.addAttribute(key, params.get(key));
			}
		}
		List<String> processList = new ArrayList<String>();
		List<ProcessDefinition> list = actProcessService.getList();
		if(list != null){
			for (ProcessDefinition processDefinition : list) {
				processList.add(processDefinition.getName());
			}
		}
		List<String> statusList = workService.loadBizInfoStatus(null);
		model.addAttribute("statusList", JSONObject.toJSON(statusList));
		model.addAttribute("processList", JSONObject.toJSON(processList));
		return "biz/biz_list";
	}

	/**
	 * 工单管理视图
	 * 
	 * @return
	 */
	@RequestMapping(value = "test")
	public String testView() {
		return "biz/biz_query_form";
	}

	@SuppressWarnings("unchecked")
	public String display(@PathVariable String id, @MatrixVariable(pathVar = "id", required = false) String bizId,
			@MatrixVariable(pathVar = "id", required = false) String taskId, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		WebUtil.getLogoUser(request, response);
		Map<String, Object> result = processExecuteService.queryWorkOrder(id);

		Map<String, String> serviceInfo = (Map<String, String>) result.get("serviceInfo");
		String sysFormType = null;
		if (serviceInfo != null) {
			sysFormType = serviceInfo.get(IProcessExecuteService.systemFormType);
			if (serviceInfo.containsKey(IProcessExecuteService.systemFormType)) {
				serviceInfo.remove(IProcessExecuteService.systemFormType);
			}
			model.addAttribute(IProcessExecuteService.systemFormType, sysFormType);
		}
		model.addAttribute("CURRE_OP", result.get("CURRE_OP"));
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
				model.addAttribute(IProcessExecuteService.systemFormType, pia);
			}
		}
		model.addAttribute("ProcessValBeanMap", map);
		result.remove("ProcessValBeanMap");

		model.addAttribute("ProcessTaskValBeans", result.get("ProcessTaskValBeans"));
		result.remove("ProcessTaskValBeans");

		model.addAttribute("SYS_BUTTON", result.get("SYS_BUTTON"));
		result.remove("SYS_BUTTON");

		model.addAttribute("workBean", result);

		return "work/workInfo";
	}

	/**
	 * 工单管理视图
	 * 
	 * @return
	 */
	@RequestMapping(value = "biz/create/{key}")
	public String createView(@PathVariable String key, String bizId, String cartId,Model model, HttpServletRequest request, HttpServletResponse response) {
		
		model.addAttribute("key", key);
		LoginUser createUser = WebUtil.getLogoUser(request,response);
		Map<String, String> userMap = roleService.loadUsersByUserName(createUser.getUsername());
		model.addAttribute("createUser",JSONObject.toJSON(userMap));
		model.addAttribute("bizId",bizId);
		model.addAttribute("cartId",cartId);
		return "biz/biz_create";
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
		StringBuffer sb = new StringBuffer();
		AbstractVariable sysSubType = null;
		for (AbstractVariable bean : list) {
			if (IProcessExecuteService.systemFormType.equalsIgnoreCase(bean.getName())) {
				sysSubType = bean;
				continue;
			}
			if (sb.indexOf(bean.getName() + ",") != -1) {
				continue;
			}
			sb.append(bean.getName() + ",");
			String groupName = bean.getGroupName();
			groupName = StringUtils.isEmpty(groupName) ? "其它信息" : groupName;
			List<AbstractVariable> temp = (List<AbstractVariable>) map.get(groupName);
			if (temp == null) {
				temp = new ArrayList<AbstractVariable>();
				map.put(groupName, temp);
			}
			temp.add(bean);
		}
		if (sysSubType != null) {
			map.put(IProcessExecuteService.systemFormType, sysSubType);
			if (!StringUtils.isEmpty(subTypeValue)) {
				String component = sysSubType.getViewComponent();
				if (component.indexOf("|") != -1) {
					String compArgs = component.substring(component.indexOf("|") + 1);
					String[] temps = compArgs.split(",");
					for (String temp : temps) {
						if (subTypeValue.equals(temp)) {
							continue;
						}
						map.remove(temp);
					}
				}
			}
		}
		return map;
	}

	@RequestMapping(value = "biz/{id}", method = RequestMethod.GET)
	public String detailView(@PathVariable String id, Model model, HttpServletRequest request,HttpServletResponse response) {
		
		model.addAttribute("id", id);
		LoginUser createUser = WebUtil.getLogoUser(request,response);
		Map<String, String> userMap = roleService.loadUsersByUserName(createUser.getUsername());
		model.addAttribute("currentUser",JSONObject.toJSON(WebUtil.userToMap(userMap)));
		return "biz/biz_detail";
	}
	
	@ResponseBody
	@RequestMapping(value = "biz/pressBizInfo")
	public Json pressBizInfo(@RequestParam List<String> bizIds,String message){
		
		Json json = new Json();
		try {
			this.workService.sendMessage(bizIds, message);
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("操作失败: " + (e.getCause() == null ? e.getLocalizedMessage() : e.getCause()));
			json.setSuccess(false);
			return json;
		}
		json.setMsg("操作成功 ");
		json.setSuccess(true);
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "biz/sendMessage")
	public Json sendMessage(@RequestParam List<String> list){
		
		Json json = new Json();
		try {
			this.workService.sendMessage(list, null);
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("操作失败: " + (e.getCause() == null ? e.getLocalizedMessage() : e.getCause()));
			json.setSuccess(false);
			return json;
		}
		json.setMsg("操作成功 ");
		json.setSuccess(true);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("biz/download")
	public void download(String id, HttpServletResponse response){
		try {
			BizFile bizFile = bizFileService.getBizFileById(id);
			File file = new File("/home/ipnet/esflowFilePath/"+bizFile.getPath());
			if(file.exists()){
				FileInputStream fis = new FileInputStream(file);
				response.reset();
				response.setContentType("application/octet-stream;charset=UTF-8");
				response.setHeader("Content-Disposition", "attachment;filename="+new String(bizFile.getName().getBytes("gb2312"),"ISO-8859-1"));
				ServletOutputStream outputStream = response.getOutputStream();
				byte[] buff = new byte[512];
				while(fis.read(buff)!=-1){
					outputStream.write(buff);
				}
				fis.close();
				outputStream.flush();
				outputStream.close();
			}else{
				response.reset();
				response.setContentType("application/octet-stream;charset=UTF-8");
				response.setHeader("Content-Disposition", "attachment;filename="+new String("文件不存在".getBytes("gb2312"),"ISO-8859-1"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ResponseBody
	@RequestMapping("/biz/getBizFile")
	public List<BizFile> getBizFile(@RequestParam Map<String,String> params){
		
		logger.info("params : "+ params);
		return this.bizFileService.findBizFile(params);
	}
	
	@ResponseBody
	@RequestMapping(value = "biz/getDraftBiz")
	public Map<String, Object> getDraftBiz(String id,HttpServletRequest request, HttpServletResponse response) {
		WebUtil.getLogoUser(request,response);
		Map<String, Object> workOrder = processExecuteService.queryWorkOrder(id);
		workOrder.remove("workLoad");
		return workOrder;
	}
	
}
