package com.eastcom.esflow.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eastcom.esflow.bean.AbstractVariable;
import com.eastcom.esflow.bean.AbstractVariableInstance;
import com.eastcom.esflow.bean.BizFile;
import com.eastcom.esflow.bean.BizInfo;
import com.eastcom.esflow.bean.BizInfoConf;
import com.eastcom.esflow.bean.BizLog;
import com.eastcom.esflow.bean.ProcessVariable;
import com.eastcom.esflow.bean.ProcessVariableInstance;
import com.eastcom.esflow.bean.TaskVariable;
import com.eastcom.esflow.bean.TaskVariableInstance;
import com.eastcom.esflow.common.exception.ServiceException;
import com.eastcom.esflow.common.utils.DateUtils;
import com.eastcom.esflow.common.utils.PageHelper;
import com.eastcom.esflow.service.BizInfoConfService;
import com.eastcom.esflow.service.BizTimedTaskService;
import com.eastcom.esflow.service.IBizFileService;
import com.eastcom.esflow.service.IBizInfoService;
import com.eastcom.esflow.service.IBizLogService;
import com.eastcom.esflow.service.IProcessDefinitionService;
import com.eastcom.esflow.service.IProcessExecuteService;
import com.eastcom.esflow.service.IProcessVariableService;
import com.eastcom.esflow.service.IVariableInstanceService;
import com.eastcom.esflow.service.IVariableInstanceService.VariableLoadType;
import com.eastcom.esflow.service.RoleService;
import com.eastcom.esflow.util.Constants;
import com.eastcom.esflow.util.UploadFileUtil;
import com.eastcom.esflow.util.WebUtil;
import com.eastcom.esflow.util.WorkOrderUtil;

@Service
@Transactional(readOnly = true)
public class ProcessExecuteServiceImpl implements IProcessExecuteService {

	
	private Logger log = Logger.getLogger(IProcessExecuteService.class);
	
	@Autowired
	private IProcessVariableService valService;

	@Autowired
	private IVariableInstanceService instService;
	
	@Autowired
	private IBizInfoService workService;
	
	@Autowired
	private IBizLogService logService;

	@Autowired
	private IBizFileService bizFileService;
	
	@Autowired
	private IProcessDefinitionService processService;
	
	@Autowired 
	private  RoleService roleService;
	
	@Autowired 
	private BizInfoConfService bizInfoConfService;
	
	@Autowired
	private BizTimedTaskService bizTimedTaskService;
	
	public Map<String, Object> loadBizLogInput(String logId) {
		
		BizLog logBean = logService.getBizLogById(logId);
		if (logBean == null) {
			return null;
		}
		List<AbstractVariableInstance> values = instService.loadValueByLog(logBean);
		Map<String, Object> results = new HashMap<String, Object>();
		if (values == null || values.size() <= 0) {
			return null;
		}
		for (AbstractVariableInstance pia : values) {
			String name = pia.getVariable().getName();
			Object value = pia.getValue();
			results.put(name, value);
		}
		return results;
	}

	public List<Map<String, Object>> loadUserGroup(String parentID) {
		return workService.loadUserGroup(parentID);
	}

	/**
	 * 加载所有的流程
	 */
	public Map<String, Object> loadProcessList() {
		return processService.loadProcessList();
	}

	/**
	 * 根据流程定义ID获取流程名
	 */
	public String getProcessDefinitionName(String procDefId) {
		return processService.getProcDefById(procDefId).getName();
	}

	@Override
	public PageHelper<BizInfo> queryMyBizInfos(String targe, Map<String, Object> params, PageHelper<BizInfo> page){
		
		
		log.info(" queryMyBizInfos begin ------");
		// 转换查询时间
		String ct1 = (String) params.get("createTime");
		String ct2 = (String) params.get("createTime2");
		Date dt1 = DateUtils.parseDate(ct1);
		Date dt2 = DateUtils.parseDate(ct2);
		if (dt1 == null) {
			params.remove("createTime");
		} else {
			params.put("createTime", dt1);
		}
		if (dt2 == null) {
			params.remove("createTime2");
		} else {
			params.put("createTime2", dt2);
		}
		String pt1 = (String) params.get("processTime");
		if (!StringUtils.isEmpty(pt1)) {
			String pt2 = (String) params.get("processTime2");
			Date dtp1 = DateUtils.parseDate(pt1);
			Date dtp2 = DateUtils.parseDate(pt2);
			if (dtp1 == null) {
				params.remove("processTime");
			} else {
				params.put("processTime", dtp1);
			}
			if (dtp2 == null) {
				params.remove("processTime2");
			} else {
				params.put("processTime2", dtp2);
			}
		}
		if ("myCreate".equalsIgnoreCase(targe)) {
			params.remove("createUser");
		}else if ("myClose".equalsIgnoreCase(targe)) {
			params.put("status", Constants.BIZ_END);
		}else if("myWork".equals(targe)){
			params.put("checkAssignee", "checkAssignee");
		}else if("myTemp".equalsIgnoreCase(targe)){
			params.put("status", "草稿");
			params.remove("createUser");
		}
		return workService.getBizInfoList(params, page);
	}

	/**
	 * 获取当前需要填写的属性列表<br>
	 * 如果没有工单号则获取模板的公共属性<br>
	 * 如果有工单号，则获取到工单当前需要处理的流程，然后再加载属性
	 * 
	 * @param tempID
	 * @param workNumber
	 * @return
	 * @throws ServiceException
	 */
	public List<AbstractVariable> loadHandleProcessVariables(String tempID) {
		
		List<AbstractVariable> result = new ArrayList<AbstractVariable>();
		List<ProcessVariable> list = valService.loadVariables(tempID, -1);
		if (list != null) {
			result.addAll(list);
		}
		return result;
	}

	public List<AbstractVariable> loadHandleProcessValBean(BizInfo bean, String taskID) {
		
		List<AbstractVariable> result = new ArrayList<AbstractVariable>();
		Task task = processService.getTaskBean(taskID);
		// 获取到当前工单的处理环节(任务)
		if (task != null) {
			List<TaskVariable> taskVariabels = valService.loadTaskVariables(bean.getProcessDefinitionId(),processService.getWorkOrderVersion(bean), task.getTaskDefinitionKey());
			if (taskVariabels != null)
				result.addAll(taskVariabels);
		} else {
			return loadHandleProcessVariables(bean.getProcessDefinitionId());
		}
		return result;
	}

	public List<AbstractVariable> loadProcessValBean(BizInfo bean) {
		
		List<AbstractVariable> result = new ArrayList<AbstractVariable>();
		String processDefinitionId = bean.getProcessDefinitionId();
		Integer vesion = 0;
		if(StringUtils.isNotBlank(processDefinitionId)){
			String[] definition = processDefinitionId.split(":");
			vesion = Integer.valueOf(definition[1]);
		}
		List<ProcessVariable> list = valService.loadVariables(bean.getProcessDefinitionId(),vesion);
		if (list != null) {
			result.addAll(list);
		}
		return result;
	}

	/**
	 * 签收工单
	 * 
	 * @param params
	 * @return
	 */
	private BizInfo sign(BizInfo bizInfo,String loginUser) {
		
		BizInfoConf bizInfoConf = this.bizInfoConfService.getBizInfoConfByBizId(bizInfo.getId());
		String taskId = bizInfoConf.getTaskId();
		if (StringUtils.isEmpty(taskId)) {
			throw new ServiceException("找不到任务ID");
		}
		String username = WebUtil.getLoginUser() == null ? loginUser : WebUtil.getLoginUser().getUsername();
		processService.claimTask(bizInfo, taskId, username);
		bizInfoConf.setTaskAssignee(username);
		workService.updateBizInfo(bizInfo);
		this.bizInfoConfService.saveOrUpdate(bizInfoConf);
		return bizInfo;
	}
	
	/**
	 * 根据工单号修改工单属性，只修改工单的业务属性
	 */
	@Override
	@Transactional
	public BizInfo update(Map<String, Object> params) {
		
		String workNumber = (String) params.get("base.workNumber");
		if (StringUtils.isEmpty(workNumber)) {
			throw new ServiceException("工单号为空");
		}
		BizInfo bean = workService.get(workNumber);
		if (bean == null) {
			throw new ServiceException("找不到工单");
		}
		String createUser = bean.getCreateUser();
		if (!createUser.equals(WebUtil.getLoginUser().getUsername())) {
			throw new ServiceException("只有当前创单用户才能修改");
		}
		List<AbstractVariable> processValList = loadHandleProcessValBean(bean, null);
		List<AbstractVariableInstance> list4 = instService.loadInstances(bean);
		// 设置流程参数

		for (AbstractVariable proAbs : processValList) {
			// 如果数据为空则不更新
			String value = (String) params.get(proAbs.getName());
			if (StringUtils.isEmpty(value)) {
				continue;
			}
			AbstractVariableInstance valueBean = null;
			for (AbstractVariableInstance instAbs : list4) {
				if (instAbs.getVariable().getId().equals(proAbs.getId())) {
					valueBean = instAbs;
					break;
				}
			}
			if (valueBean == null) {
				valueBean = proAbs instanceof ProcessVariable ? new ProcessVariableInstance() : new TaskVariableInstance();
				valueBean.setProcessInstanceId(bean.getProcessInstanceId());
				valueBean.setValue(value);
				valueBean.setVariable(proAbs);
				instService.addProcessInstance(valueBean);
			} else {
				valueBean.setValue(value);
				instService.updateProcessInstance(valueBean);
			}
		}
		return bean;
	}

	@Override
	@Transactional
	public BizInfo createBizDraft(Map<String, Object> params,  MultiValueMap<String, MultipartFile> multiValueMap, boolean startProc, String[] deleFileId) {
		
		String source = (String) params.get("$source");
		source = StringUtils.isBlank(source) ? "人工发起" : source;
		String procDefId = (String) params.get("base.tempID");
		String dt1 = (String)params.get("base.limitTime");
		String createUser = (String)params.get("base.createUser");
		String tempBizId = (String)params.get("tempBizId");
		Date limitTime = DateUtils.parseDate(dt1);
		Date now = new Date();
		BizInfo bizInfo = null;
		BizInfoConf bizInfoConf = null;
		if(StringUtils.isNotBlank(tempBizId)){
			bizInfo = workService.get(tempBizId);
			bizInfoConf = this.bizInfoConfService.get(bizInfo.getId());
		}else{
			bizInfo = new BizInfo();
			bizInfo.setBizId(WorkOrderUtil.builWorkNumber(procDefId));
		}
		if(bizInfoConf == null){
			bizInfoConf = new BizInfoConf();
			bizInfoConf.setBizInfo(bizInfo);
		}
		bizInfo.setSource(source);
		bizInfo.setLimitTime(limitTime);
		bizInfo.setProcessDefinitionId(procDefId);
		bizInfo.setBizType(getProcessDefinitionName(procDefId));
		bizInfo.setStatus(Constants.BIZ_TEMP); // TODO 临时填值
		bizInfo.setCreateTime(now);
		if(StringUtils.isNotBlank(createUser)){
			bizInfo.setCreateUser(createUser);
			bizInfoConf.setTaskAssignee(createUser);
		}else{
			bizInfo.setCreateUser(WebUtil.getLoginUser().getUsername());
			bizInfoConf.setTaskAssignee(WebUtil.getLoginUser().getUsername());
		}
		bizInfo.setTitle((String) params.get("base.workTitle"));
		workService.addBizInfo(bizInfo);
		bizInfoConf.setBizInfo(bizInfo);
		this.bizInfoConfService.saveOrUpdate(bizInfoConf);
		if (startProc)
			startProc(bizInfo,bizInfoConf, params, now);
		else {
			List<AbstractVariable> processValList = loadHandleProcessVariables(procDefId);
			saveOrUpdateVars(bizInfo,bizInfoConf, processValList, params, now);
		}
		/* 处理附件 */
		if(multiValueMap!=null && !multiValueMap.isEmpty()){
			for(String fileCatalog : multiValueMap.keySet()){
				List<MultipartFile> filesLists = (List<MultipartFile>) multiValueMap.get(fileCatalog);
				CommonsMultipartFile[] files = new  CommonsMultipartFile[filesLists.size()];
				filesLists.toArray(files);
				saveBizFiles(files, now, bizInfo, null,fileCatalog);
			}
		}
		this.deleBizFiles(deleFileId);
		return bizInfo;
	}
	
	@Override
	@Transactional
	public BizInfo updateBiz(String id, Map<String, Object> params, MultiValueMap<String, MultipartFile> fileMap, boolean startProc){
	
		BizInfo bizInfo = workService.get(id);
		BizInfoConf bizInfoConf = this.bizInfoConfService.getBizInfoConfByBizId(id);
		Date now = new Date();
		if(fileMap != null && !fileMap.isEmpty()){
			for(String fileCatalog : fileMap.keySet()){
				List<MultipartFile> filesLists = (List<MultipartFile>) fileMap.get(fileCatalog);
				CommonsMultipartFile[] files = new  CommonsMultipartFile[filesLists.size()];
				filesLists.toArray(files);
				saveBizFiles(files, now, bizInfo, null,fileCatalog);
			}
		}
		if(StringUtils.isNotBlank(bizInfo.getProcessInstanceId()) && StringUtils.isNotBlank(bizInfoConf.getTaskId())){
			reSubmit(params, bizInfo,bizInfoConf);
		}else if (startProc)
			startProc(bizInfo,bizInfoConf, params, now);
		else
			saveOrUpdateVars(bizInfo,bizInfoConf, loadHandleProcessValBean(bizInfo, bizInfoConf.getTaskId()), params, now);
		return bizInfo;
	}

	public BizInfo startProc(BizInfo bizInfo,BizInfoConf bizInfoConf, Map<String, Object> params, Date now) {

		String procDefId = bizInfo.getProcessDefinitionId();
		Map<String, Object> variables = new HashMap<String, Object>();
		List<AbstractVariable> processValList = loadHandleProcessVariables(procDefId);
		// 设置流程参数
		for (AbstractVariable abc : processValList) {
			if (abc.isProcessVariable()!=null && abc.isProcessVariable()) {
				Object value = WorkOrderUtil.convObject((String) params.get(abc.getName()), abc.getViewComponent());
				variables.put(abc.getName(), value);
			}
		}
		variables.put("SYS_FORMTYPE", params.get(IProcessExecuteService.systemFormType));
		variables.put("SYS_BUTTON_VALUE", params.get("base.buttonId"));
		variables.put("SYS_BIZ_CREATEUSER", bizInfo.getCreateUser());
		variables.put(Constants.SYS_BIZ_ID, bizInfo.getId());
		variables.put("systemName",params.get("systemName"));
		ProcessInstance instance = processService.newProcessInstance(WebUtil.getLoginUser(), procDefId, variables);
		bizInfo.setProcessInstanceId(instance.getId());
		this.processService.autoClaim(instance.getId());//TODO任务创建时的自动签收

		TaskEntity task = new TaskEntity(); // 开始节点没有任务对象
		task.setId("START");
		task.setName((String) params.get("base.handleName"));
		writeBizLog(bizInfo, task, now, (String) params.get("base.handleResult"), params);
		updateBizTaskInfo(bizInfo,bizInfoConf);
		this.bizInfoConfService.saveOrUpdate(bizInfoConf);
		// 保存流程字段
		saveOrUpdateVars(bizInfo,bizInfoConf, processValList, params, now);
		return bizInfo;
	}
	
	private void reSubmit(Map<String, Object> params,BizInfo bizInfo,BizInfoConf bizInfoConf){
		
 		String result = (String) params.get("base.handleResult");
		String buttonId = (String) params.get("base.buttonId");
		Date now = new Date();
		Task task = processService.getTaskBean(bizInfoConf.getTaskId());
		Map<String, Object> variables = new HashMap<String, Object>();
		//从新提交时 参数有流程变量 和 任务变量
		List<AbstractVariable> processValList = loadHandleProcessValBean(bizInfo, task.getId());
		processValList.addAll(loadHandleProcessVariables(bizInfo.getProcessDefinitionId()));
		// 设置流程参数
		for (AbstractVariable abc : processValList) {
			if (abc.isProcessVariable()!=null && abc.isProcessVariable()) {
				Object value = WorkOrderUtil.convObject((String) params.get(abc.getName()), abc.getViewComponent());
				variables.put(abc.getName(), value);
			}
		}
		variables.put("SYS_FORMTYPE", params.get(IProcessExecuteService.systemFormType));
		variables.put("SYS_BUTTON_VALUE", buttonId);
		variables.put("SYS_BIZ_ID", bizInfo.getId());
		variables.put("handleUser", params.get("handleUser"));
		variables.put("SYS_BIZ_CREATEUSER", bizInfo.getCreateUser());
		// 增加处理结果
		processService.completeTask(bizInfo, bizInfoConf.getTaskId(), WebUtil.getLoginUser(), variables);
		// 保存业务字段
		saveOrUpdateVars(bizInfo,bizInfoConf, processValList, params, now);
		this.bizInfoConfService.saveOrUpdate(bizInfoConf);
		updateBizTaskInfo(bizInfo,bizInfoConf);
		// 保存工单信息
		workService.updateBizInfo(bizInfo);
		/* 保存日志 */
		writeBizLog(bizInfo, task, now, result, params);
	}
	
	public void saveOrUpdateVars(BizInfo bizInfo,BizInfoConf bizInfoConf, List<AbstractVariable> processValList, Map<String, Object> params,Date now) {
		
		String procInstId = bizInfo.getProcessInstanceId();
		String taskId = bizInfoConf.getTaskId();
		Map<String, ? extends AbstractVariableInstance> currentVars = instService.getVarMap(bizInfo,bizInfoConf,VariableLoadType.UPDATABLE);
		for (AbstractVariable proAbs : processValList) {
			String proName = proAbs.getName().trim();
			String component = proAbs.getViewComponent();
			String value = null;
			if("REQUIREDFILE".equalsIgnoreCase(component)){
				value = "file";// TODO 附件特殊处理
			}else{
				value = (String) params.get(proName);
			}
			if (StringUtils.isEmpty(value)) 
				continue;
			AbstractVariableInstance valueBean = currentVars.get(proAbs.getName());
			if (null != valueBean) {
				valueBean.setValue(value);
				valueBean.setCreateTime(new Date());
				instService.updateProcessInstance(valueBean);
			} else {
				valueBean = proAbs instanceof ProcessVariable ? new ProcessVariableInstance() : new TaskVariableInstance();
				valueBean.setProcessInstanceId(procInstId);
				valueBean.setValue(value);
				valueBean.setCreateTime(now);
				valueBean.setVariable(proAbs);
				if (valueBean instanceof ProcessVariableInstance)
					((ProcessVariableInstance) valueBean).setBizId(bizInfo.getId());
				else if (valueBean instanceof TaskVariableInstance) {
					((TaskVariableInstance) valueBean).setTaskId(taskId);
				}
				instService.addProcessInstance(valueBean);
			}
		}
	}

	private void saveBizFiles(MultipartFile[] files,Date now, BizInfo bizInfo, Task task,String fileCatalog) {
	
		if (files != null)
			for (MultipartFile file : files) {
				BizFile bean = UploadFileUtil.saveFile(file);
				if (bean == null) {
					continue;
				}
				bean.setCreateDate(now);
				bean.setFileCatalog(fileCatalog);
				bean.setCreateUser(WebUtil.getLoginUser().getUsername());
				if (null != task) {
					bean.setTaskInstanceId(task.getId());
					bean.setTaskName(task.getName());
					bean.setTaskId(task.getId());
				}
				bean.setBizInfo(bizInfo);
				bizFileService.addBizFile(bean);
			}
	}

	private void deleBizFiles(String[] ids) {
		List<String> filePathList = new ArrayList<String>();
		if (ids != null) {
			for (String id : ids) {
				BizFile bizFile = bizFileService.getBizFileById(id);
				filePathList.add(bizFile.getPath());
			}
			bizFileService.deleteBizFile(ids);
			for (String filePath : filePathList) {
				File file = new File(filePath);
				file.deleteOnExit();
			}
		}
	}
	
	@Override
	public void updateBizTaskInfo(BizInfo bizInfo,BizInfoConf bizInfoConf) {
		
		List<String[]> taskList = processService.getNextTaskInfo(bizInfo);
		// 如果nextTaskInfo返回null，标示流程已结束
		if (taskList==null || taskList.isEmpty()) {
			bizInfoConf = this.bizInfoConfService.getBizInfoConfByBizId(bizInfo.getId());
			bizInfoConf.setTaskId("END");
			bizInfo.setTaskName("已结束");
			bizInfo.setStatus(Constants.BIZ_END);
			bizInfo.setTaskDefKey(Constants.BIZ_END);
			bizInfoConf.setTaskAssignee("-");
		} else {
			String[] nextTaskInfo = taskList.get(0);
			bizInfo.setStatus(nextTaskInfo[2]);
			bizInfoConf.setTaskId(nextTaskInfo[0]);
			bizInfo.setTaskName(nextTaskInfo[1]);
			bizInfo.setTaskDefKey(nextTaskInfo[2]);
			bizInfoConf.setTaskAssignee(nextTaskInfo[3]);
			this.bizTimedTaskService.saveTimedTask(bizInfo, bizInfoConf);
			if (StringUtils.isEmpty(bizInfoConf.getTaskAssignee()) && StringUtils.isNotBlank(nextTaskInfo[4]))
				bizInfoConf.setTaskAssignee(Constants.BIZ_TASKASSIGNEE + nextTaskInfo[4]);
			BizInfoConf bizConf = null;
			if (taskList.size() > 1) {
				for(int i=1;i<taskList.size();i++){
					bizConf = new BizInfoConf();
					nextTaskInfo = taskList.get(i); 
					bizConf.setTaskId(nextTaskInfo[0]);
					bizConf.setTaskAssignee(nextTaskInfo[3]);
					if (StringUtils.isEmpty(bizConf.getTaskAssignee())&&StringUtils.isNotBlank(nextTaskInfo[4])) {
						bizConf.setTaskAssignee(Constants.BIZ_TASKASSIGNEE + nextTaskInfo[4]);
					}
					bizConf.setBizInfo(bizInfo);
					this.bizInfoConfService.saveOrUpdate(bizConf);
				}
			}
		}
	}
	
	/**
	 * 提交工单，实现流转
	 * 
	 * @param params
	 * @param files
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public BizInfo submit(Map<String, Object> params, MultiValueMap<String, MultipartFile> fileMap) {
		
		log.info("params :"+params);
		Date now = new Date();
		BizInfo bizInfo = null;
		BizInfoConf bizInfoConf = null;
 		String result = (String) params.get("base.handleResult");
		String buttonId = (String) params.get("base.buttonId");
		String bizId = (String) params.get("base.workNumber");
		if (StringUtils.isNotBlank(bizId))
			bizInfo = workService.getByBizId(bizId);
		if (null == bizInfo)
			throw new ServiceException("工单不存在");
		bizInfoConf = this.bizInfoConfService.getBizInfoConfByBizId(bizInfo.getId()); 
		if(bizInfoConf == null){
			throw new ServiceException("请确认是否有提交工单权限");
		}
		String taskId = bizInfoConf.getTaskId();
		Task task = processService.getTaskBean(taskId);
		if (Constants.SIGN.equalsIgnoreCase(buttonId)) {
			sign(bizInfo,(String)params.get("loginUser"));
		}else {										
			Map<String, Object> variables = new HashMap<String, Object>();
			List<AbstractVariable> processValList = loadHandleProcessValBean(bizInfo, taskId);
			// 设置流程参数
			for (AbstractVariable abc : processValList) {
				if (abc.isProcessVariable()!=null && abc.isProcessVariable()) {
					Object value = WorkOrderUtil.convObject((String) params.get(abc.getName()), abc.getViewComponent());
					variables.put(abc.getName(), value);
				}
			}
			variables.put("SYS_FORMTYPE", params.get(IProcessExecuteService.systemFormType));
			variables.put("SYS_BUTTON_VALUE", buttonId);
			variables.put("SYS_BIZ_ID", bizInfo.getId());
			ArrayList<String> members = this.getMembers((String)params.get("handleUser"));
			variables.put("members",members);//会签
			variables.put("SYS_BIZ_CREATEUSER", bizInfo.getCreateUser());
			// 增加处理结果
			processService.completeTask(bizInfo, taskId, WebUtil.getLoginUser(), variables);
			// 保存业务字段
			saveOrUpdateVars(bizInfo,bizInfoConf, processValList, params, now);
			updateBizTaskInfo(bizInfo,bizInfoConf);
			workService.updateBizInfo(bizInfo);
			this.bizInfoConfService.saveOrUpdate(bizInfoConf);
		}
		if(fileMap!=null && !fileMap.isEmpty()){
			for(String fileCatalog : fileMap.keySet()){
				List<MultipartFile> filesLists = (List<MultipartFile>) fileMap.get(fileCatalog);
				CommonsMultipartFile[] files = new  CommonsMultipartFile[filesLists.size()];
				filesLists.toArray(files);
				saveBizFiles(files, now, bizInfo, task,fileCatalog);
			}
		}
		writeBizLog(bizInfo, task, now, result, params);
		return bizInfo;
	}

	private ArrayList<String> getMembers(String handleUser){
		
		PageHelper<Map<String,Object>> page = new PageHelper<Map<String,Object>>();
		page.setRows(-1);
		page.setPage(-1);
		ArrayList<String> roles = new ArrayList<String>();
		ArrayList<String> members = new ArrayList<String>();
		if(StringUtils.isNotBlank(handleUser)){
			String[] group = null;
			if(handleUser.startsWith("GROUP:")){
				group = handleUser.split("\\:");
				if(group.length>1&&StringUtils.isNotBlank(group[1])){
					roles.add(group[1]);
					List<Map<String,Object>> users = roleService.loadUsersByRole(page, roles).getList();
					if(users != null && !users.isEmpty()){
						for(Map<String,Object> map : users){
							String username = (String)map.get("username");
							if(StringUtils.isNotBlank(username))
								members.add(username);
						}
					}
				}
			}else{
				group = handleUser.split("\\,");
				for(String member : group){
					if(StringUtils.isNotBlank(member))
						members.add(member);
				}
			}
		}
		return members;
	}
	
	public void writeBizLog(BizInfo bizInfo, Task task, Date now, String result, Map<String, Object> params) {
		
		BizLog logBean = new BizLog();
		logBean.setCreateTime(now);
		logBean.setTaskID(task.getId());
		logBean.setTaskName(task.getName());
		logBean.setBizInfo(bizInfo);
		logBean.setHandleDescription((String) params.get("base.handleMessage"));
		logBean.setHandleResult(result);
		if(WebUtil.getLoginUser() != null){
			logBean.setHandleUser(WebUtil.getLoginUser().getUsername());
		}else
			logBean.setHandleUser((String)params.get("loginUser"));
		logBean.setHandleName((String) params.get("base.handleName"));
		logService.addBizLog(logBean);
	}

	@Override
	public BizInfo getBizInfo(String id, String workNumber) {
		
		BizInfo bean = null;
		if (StringUtils.isNotBlank(id)) {
			bean = workService.getBizInfo(id,WebUtil.getLoginUser().getUsername());
		} else if (StringUtils.isNotBlank(workNumber)) {
			bean = workService.getBizInfo(workNumber,WebUtil.getLoginUser().getUsername());
		}
		if (bean == null) {
			throw new ServiceException("找不到工单");
		}
		return bean;
	}

	/**
	 * 获取某个流程的开始按钮
	 * 
	 * @param tempId
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, String> loadStartButtons(String tempId) {
	
		Map<String, String> buttons = processService.loadStartButtons(tempId);
		if (buttons == null || buttons.size() <= 0) {
			buttons = buttons == null ? new HashMap<String, String>() : buttons;
			buttons.put("submit", "提交");
		}
		return buttons;
	}

	/**
	 * 根据工单号查询工单信息，并且处理工单的处理权限,KEY列表如下<br>
	 * ---ID跟taskID配套，如果传了taskID,则会判断当前是否可编辑，否则工单只呈现 workInfo： 工单对象信息<br>
	 * CURRE_OP: 当前用户操作权限<br>
	 * ProcessValBeanMap :需要呈现的业务字段<br>
	 * ProcessTaskValBeans:当前编辑的业务字段<br>
	 * extInfo :扩展信息<br>
	 * extInfo.createUser:创建人信息<br>
	 * serviceInfo:业务字段信息内容<br>
	 * annexs:附件列表<br>
	 * workLogs:日志
	 * 
	 * @param bizId
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> queryWorkOrder(String id) {

		String loginUser = WebUtil.getLoginUser().getUsername();
		Map<String, Object> result = new HashMap<String, Object>();
		// 加载工单对象
		BizInfo workBean =  workService.getBizInfo(id,loginUser);
		if (workBean == null) {
			throw new ServiceException("找不到工单:" + id);
		}
		result.put("workInfo", workBean);
		String taskId = workBean.getTaskId();
		// 加载工单详情字段
		List<AbstractVariable> list = loadProcessValBean(workBean);
		result.put("ProcessValBeanMap", list);

		// 处理扩展信息
		Map<String, Object> extInfo = new HashMap<String, Object>();
		result.put("extInfo", extInfo);
		Map<String, String> userMap = WebUtil.userToMap(roleService.loadUsersByUserName(workBean.getCreateUser()));
		
		extInfo.put("createUser", userMap);
		extInfo.put("base_taskID",taskId);
		
		// 子工单信息
		Map<String, Object> queryParam = new HashMap<String, Object>(1);
		queryParam.put("parentId", workBean.getId());
		PageHelper<BizInfo>  page = new PageHelper<BizInfo>();
		page.setPage(-1);
		page.setRows(-1);
		PageHelper<BizInfo> subResult = workService.getBizInfoList(queryParam, page);
		String curreOp = null;
		if(!subResult.getList().isEmpty()) 
			result.put("subBizInfo", subResult.getList());
		if (StringUtils.isNotEmpty(taskId)) 
			curreOp = processService.getWorkAccessTask(taskId, loginUser);
		result.put("CURRE_OP", curreOp);
		Task task = processService.getTaskBean(workBean.getTaskId());
		if (task != null)
			result.put("$currentTaskName", task.getName());
		list = loadHandleProcessValBean(workBean, taskId);
		// 加载当前编辑的业务字段,只有当前操作为HANDLE的时候才加载
		if (Constants.HANDLE.equalsIgnoreCase(curreOp)) {
			result.put("ProcessTaskValBeans", list);
			extInfo.put("handleUser", WebUtil.userToMap(roleService.loadUsersByUserName(loginUser)));
			Map<String, String> buttons = processService.findOutGoingTransNames(taskId, false);
			if (buttons == null || buttons.size() <= 0) {
				buttons = buttons == null ? new HashMap<String, String>() : buttons;
				buttons.put("submit", "提交");
			}
			result.put("SYS_BUTTON", buttons);
		} else if (Constants.SIGN.equalsIgnoreCase(curreOp)) {
			Map<String, String> buttons = new HashMap<String, String>(1);
			buttons.put(Constants.SIGN, "签收");
			result.put("SYS_BUTTON", buttons);
		}
		// 加载工单参数
		List<AbstractVariableInstance> list4 = instService.loadInstances(workBean);
		if (list4 != null) {
			result.put("serviceInfo", list4);
		}
		// 加载附件列表
		List<BizFile> list2 = bizFileService.loadBizFilesByBizId(workBean.getId(),null);
		result.put("annexs", list2 == null || list2.size() <= 0 ? null : list2);
		// 加载日志
		List<BizLog> list3 = logService.loadBizLogs(workBean.getId());
		Map<String, List<AbstractVariableInstance>> logVars = new HashMap<String, List<AbstractVariableInstance>>(0);
		Map<String,Object> fileMap = new HashMap<String, Object>();
		if(list3 != null && !list3.isEmpty()) {
			for (BizLog bizLog : list3) {
				List<BizFile> fileList = bizFileService.loadBizFilesByBizId(bizLog.getBizInfo().getId(),bizLog.getTaskID());
				fileMap.put(bizLog.getId(), fileList);
				List<AbstractVariableInstance> values = instService.loadValueByLog(bizLog);
				if(values != null && !values.isEmpty()) {
					logVars.put(bizLog.getId(), values);
				}
			}
		}
		result.put("files", fileMap);
		result.put("workLogs", list3 == null || list3.size() <= 0 ? null : list3);
		result.put("logVars", logVars);
		//工作量评估
		String processInstanceId = workBean.getProcessInstanceId();
		if(StringUtils.isNotBlank(processInstanceId)&&StringUtils.isNotBlank(taskId)){
			Map<String,String> queryParams = new HashMap<String, String>();
			queryParams.put("processInstanceId", processInstanceId);
			queryParams.put("taskId", taskId);
			List<TaskVariableInstance> workLoads = this.instService.findTaskVariableInstance(queryParams);
			result.put("workLoad", workLoads);
		}
		return result;
	}

	/**
	 * 下载或查看文件
	 * 
	 * @param action
	 * @param id
	 * @return [文件类型,InputStream]
	 * @throws ServiceException
	 */
	public Object[] downloadFile(String action, String id) {
	
		Object[] result = new Object[4];
		if ("work".equalsIgnoreCase(action)) {
			BizInfo bean = workService.get(id);
			if (bean == null) {
				throw new ServiceException("找不到工单");
			}
			result[0] = "IMAGE";
			result[1] = processService.viewProcessImage(bean);
		} else {
			BizFile bean = bizFileService.getBizFileById(id);
			if (bean == null) {
				throw new ServiceException("找不到附件");
			}
			File file = UploadFileUtil.getUploadFile(bean);
			if (!file.exists()) {
				throw new ServiceException("找不到附件");
			}
			InputStream is = null;
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			result[0] = bean.getFileType();
			result[1] = is;
			result[2] = file.length();
			result[3] = bean.getName();
		}
		return result;
	}
}
