package com.activiti.core.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.activiti.core.service.RoleService;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.GatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.NoneStartEventActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activiti.core.bean.BizInfo;
import com.activiti.core.common.exception.ServiceException;
import com.activiti.core.common.utils.LoginUser;
import com.activiti.core.dao.IProcessModelDao;
import com.activiti.core.service.IBizInfoService;
import com.activiti.core.service.IProcessDefinitionService;
import com.activiti.core.util.Constants;
import com.activiti.core.util.WebUtil;

@Service
@Transactional(readOnly = true)
public class ProcessServiceImpl implements IProcessDefinitionService {

	private static Logger logger = Logger.getLogger(ProcessServiceImpl.class);
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private ProcessEngineFactoryBean processEngine;

	@Autowired
	private IBizInfoService bizInfoService;
	
	@Autowired
	private RoleService roleService;

	@Autowired
	private IProcessModelDao processDao;
	
	public Map<String, Object> getActivityTask(BizInfo bean, LoginUser user){
		
		if (bean == null || Constants.BIZ_END.equals(bean.getStatus())) {
			return null;
		}
		Map<String, Object> result = null;
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(bean.getProcessInstanceId()).taskAssignee(user.getUsername()).listPage(0, 1);
		String curreOp = null;
		Task task = null;
		if (taskList != null && taskList.size() > 0) {
			if (taskList.size() > 1) {
				StringBuffer temp = new StringBuffer();
				temp.append("(HANDLE)listSize:" + taskList.size() + "==>");
				for (Task t : taskList) {
					temp.append(t.getId() + "::" + t.getTaskDefinitionKey() + " || ");
				}
				logger.debug("=========" + temp.toString());
			}
			task = taskList.get(0);
			curreOp = Constants.HANDLE;
		} else {
			Set<String> roles = roleService.getUserRolesByUserName(user.getUsername());
			if (roles == null || roles.size() <= 0) {
				return null;
			}
			List<String> roles2 = new ArrayList<String>();
			for (String s : roles)
				roles2.add(s);
			taskList = taskService.createTaskQuery().taskCandidateGroupIn(roles2).listPage(0, 1);
			if (taskList != null && taskList.size() > 0) {
				if (taskList.size() > 1) {
					StringBuffer temp = new StringBuffer();
					temp.append("(SIGN)listSize:" + taskList.size() + "==>");
					for (Task t : taskList) {
						temp.append(t.getId() + "::" + t.getTaskDefinitionKey() + " || ");
					}
					logger.debug("=========" + temp.toString());
				}
				task = taskList.get(0);
				curreOp = Constants.SIGN;
			}
		}
		if (curreOp != null && task != null) {
			result = new HashMap<String, Object>();
			result.put("taskID", task.getId());
			result.put("curreOp", curreOp);
		}
		return result;
	}

	public Map<String, String> loadStartButtons(String tempId){
		
		Map<String, String> result = null;
		ActivityImpl activity = getStartActivityImpl(tempId);
		if (activity != null) {
			ProcessDefinitionEntity pde = (ProcessDefinitionEntity) activity.getProcessDefinition();
			result = findOutGoingTransNames(pde, activity);
		}
		result = result == null ? new HashMap<String, String>() : result;
		if (result.size() == 0) {
			result.put("submit", "提交");
		}
		return result;
	}

	private ActivityImpl getStartActivityImpl(String tempId) {
		
		ProcessDefinition pd = (ProcessDefinition) repositoryService.createProcessDefinitionQuery().processDefinitionId(tempId).singleResult();
		ActivityImpl activity = null;
		ProcessDefinitionEntity pde = null;
		if (pd != null) {
			pde = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(pd.getId());
			List<ActivityImpl> list = pde.getActivities();
			for (ActivityImpl bean : list) {
				if (bean.getActivityBehavior() instanceof NoneStartEventActivityBehavior) {
					activity = bean;
					break;
				}
			}
		}
		return activity;
	}

	public Map<String, String> findOutGoingTransNames(String taskID, boolean def){
		
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		ProcessDefinitionEntity pd = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
		String activitiId = pi.getActivityId();
		ActivityImpl activity = pd.findActivity(activitiId);
		Map<String, String> result = findOutGoingTransNames(pd, activity);
		result = result == null ? new HashMap<String, String>() : result;
		if (def && result.size() == 0) {
			result.put("submit", "提交");
		}
		return result;
	}

	/**
	 * 获取按钮，如果出口只有一条并且为网关时取网关的出口，否则去出口线
	 */
	private Map<String, String> findOutGoingTransNames(ProcessDefinitionEntity pd, ActivityImpl activity) {
		
		if (activity == null) {
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		// 5.通过活动对象找当前活动的所有出口
		List<PvmTransition> transitions = activity.getOutgoingTransitions();
		if (transitions == null) {
			return null;
		}
		List<PvmTransition> lines = null;
		if (transitions.size() == 1) {
			ActivityImpl object = (ActivityImpl) transitions.get(0).getDestination();
			if (object.getActivityBehavior() instanceof GatewayActivityBehavior) {
				lines = object.getOutgoingTransitions();
			}
		}
		if (lines != null) {
			for (PvmTransition trans : lines) {
				String transName = (String) trans.getProperty("name");
				if (StringUtils.isNotEmpty(transName)) {
					result.put(trans.getId(), transName);
				}
			}
		}

		return result;
	}

	@Transactional
	public ProcessInstance newProcessInstance(LoginUser user, String id, Map<String, Object> variables){
		
		ProcessInstance bean = runtimeService.startProcessInstanceById(id, variables);
		try {
			String buttonValue = variables == null ? null : (String) variables.get("SYS_BUTTON_VALUE");
			ActivityImpl activityImpl = getStartActivityImpl(id);
			String transfer_type = (String) variables.get("SYS_transfer_type");
			String transfer_value = (String) variables.get("SYS_transfer_value");
			executeCommand(null, user, buttonValue, bean.getId(), activityImpl, transfer_type, transfer_value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 获取当前任务的前一个任务<br>
	 * 如果当前任务的前置任务有多个，可以指定parentTaskDefinitionKey 来确定获取，否则将获取最后进入当前任务的结点作为前置结点
	 * 
	 * @param taskID
	 *            当前任务ID
	 * @param parentTaskDefinitionKey
	 *            前置结点定义
	 * @return
	 * @
	 */
	public String getParentTask(String taskID){
		
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		ProcessDefinitionEntity pd = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
		PvmTransition pvmTransition = findLastInputTransition(processInstanceId, pd, task.getTaskDefinitionKey(), true);
		if (pvmTransition != null && pvmTransition.getSource() != null)
			return pvmTransition.getSource().getId();
		return null;
	}

	/**
	 * 获取流程运行PATH,以逗号开头，使用逗号分隔，根据历史任务的结束时间倒序排序
	 * 
	 * @return
	 * @
	 */
	public String getProcessPath(BizInfo bean){
		
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(bean.getProcessInstanceId()).orderByHistoricTaskInstanceEndTime().desc().list();
		if (list == null || list.size() <= 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (HistoricTaskInstance hti : list) {
			if (hti.getEndTime() == null) {
				continue;
			}
			String tid = hti.getTaskDefinitionKey();
			if (sb.toString().endsWith("," + tid)) {
				continue;
			}
			sb.append("," + hti.getTaskDefinitionKey());
		}
		return sb.toString();
	}

	/**
	 * 获取最后进入的线实例
	 * 
	 * @param processInstance
	 * @param processDefinitionEntity
	 * @param activity
	 * @return
	 */
	private PvmTransition findLastInputTransition(String processInstance,ProcessDefinitionEntity processDefinitionEntity, String activityID, boolean isRecursive) {
		// 找到活动定义
		ActivityImpl activity = processDefinitionEntity.findActivity(activityID);
		// 获取该定义的入口流
		List<PvmTransition> list = activity.getIncomingTransitions();
		HistoricActivityInstance lastBean = null;
		PvmTransition pt2 = null;
		// 循环判断哪个流入为最晚流入
		for (PvmTransition pt : list) {
			HistoricActivityInstance bean = null;
			// 查询当前流程实例审批结束的历史节点
			List<HistoricActivityInstance> historicActivityInstances = historyService
					.createHistoricActivityInstanceQuery().processInstanceId(processInstance).activityId(pt.getSource().getId())
					.finished().orderByHistoricActivityInstanceEndTime().desc().list();

			if (historicActivityInstances.size() > 0) {
				bean = historicActivityInstances.get(0);
			}
			if (bean == null || bean.getEndTime() == null) {
				continue;
			}
			if (lastBean != null) {
				if (bean.getEndTime().getTime() > lastBean.getEndTime().getTime()) {
					lastBean = bean;
					pt2 = pt;
				}
			} else {
				lastBean = bean;
				pt2 = pt;
			}
		}
		if (isRecursive)
			if (pt2 != null && pt2.getSource() != null) {
				PvmActivity pvm = pt2.getSource();
				String type = (String) pvm.getProperty("type");
				if (!"userTask".equals(type)) {
					pt2 = findLastInputTransition(processInstance, processDefinitionEntity, pvm.getId(), isRecursive);
				}
			}
		return pt2;
	}

	/**
	 * 执行任务
	 * 
	 * @param bean
	 * @param taskID
	 * @param variables
	 * @return
	 * @
	 */
	@Transactional
	public boolean completeTask(BizInfo bean2, String taskID, LoginUser user, Map<String, Object> variables) {
		
		variables.put("SYS_CURRENT_PID", bean2.getProcessInstanceId());
		variables.put("SYS_CURRENT_WORKNUMBER", bean2.getBizId());
		variables.put("SYS_CURRENT_WORKID", bean2.getId());
		variables.put("SYS_CURRENT_TASKID", taskID);
		String transfer_type = (String) variables.get("SYS_transfer_type");
		String transfer_value = (String) variables.get("SYS_transfer_value");

		Task task = getTaskBean(taskID);
		variables.put("SYS_CURRENT_TASKKEY", task == null ? null : task.getTaskDefinitionKey());
		String processInstanceID = task.getProcessInstanceId();
		taskService.complete(task.getId(), variables);
		String buttonValue = variables == null ? null : (String) variables.get("SYS_BUTTON_VALUE");
		ProcessDefinitionEntity pd = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(task.getProcessDefinitionId());
		ActivityImpl activityImpl = pd.findActivity(task.getTaskDefinitionKey());
		executeCommand(bean2, user, buttonValue, processInstanceID, activityImpl, transfer_type, transfer_value);
		autoClaim(task.getProcessInstanceId());
		return true;
	}

	/**
	 * 自动签收//如果当前任务只有一个用户则设定此用户为自动签收
	 * 
	 * @param processInstanceID
	 * @return
	 */
	@Override
	public boolean autoClaim(String processInstanceID) {
		
		List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceID).list();
		for (Task task : list) {
			if (!StringUtils.isEmpty(task.getAssignee())) {
				continue;
			}
			List<String> groups = getTaskCandidateGroup(task);
			if (groups == null || groups.size() <= 0) {
				continue;
			}
			String username = bizInfoService.loadOnlyUser(groups);
			if (StringUtils.isEmpty(username)) {
				continue;
			}
			taskService.claim(task.getId(), username);
		}
		return true;
	}

	private boolean executeCommand(BizInfo bean2, LoginUser user, String buttonValue, String processInstanceID,
			ActivityImpl activityImpl, String transfer_type, String transfer_value) {

		if (!StringUtils.isEmpty(buttonValue)) {
			// 获取当前任务的流出，并判断是否为当前活动任务
			ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceID).singleResult();
			if (pi == null) {
				return true;
			} // 流程已结束
			String activityID = pi.getActivityId();
			Task task2 = taskService.createTaskQuery().taskDefinitionKey(activityID).processInstanceId(processInstanceID).list().get(0);
			List<PvmTransition> transitions = activityImpl.getOutgoingTransitions();
			// 此处获取出口线的算法必须与findOutGoingTransNames中对应的算法一致
			if (transitions.size() == 1) {
				ActivityImpl object = (ActivityImpl) transitions.get(0).getDestination();
				if (object.getActivityBehavior() instanceof GatewayActivityBehavior) {
					transitions = object.getOutgoingTransitions();
				}
			}
			for (PvmTransition pvmTransition : transitions) {
				if (pvmTransition.getId().equals(buttonValue)||"submit".equalsIgnoreCase(buttonValue)) {
					String docv = (String) pvmTransition.getProperty("documentation");
					if (StringUtils.isEmpty(docv)) {
						continue;
					}
					
					// 如果该线为驳回先，则从历史里面去当前任务已经结束的最近一个任务，然后将单分配给此人
					if (docv.startsWith("command:fallback")) {
						List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
								.processInstanceId(processInstanceID).finished().orderByHistoricTaskInstanceEndTime()
								.desc().taskDefinitionKey(activityID).list();
						if (list != null && list.size() > 0) {
							HistoricTaskInstance hti = list.get(0);
							if (hti != null && hti.getAssignee() != null) {
								taskService.claim(task2.getId(), hti.getAssignee());
							}
						}
						// 如果该线为循环，则将新的任务转派给当前用户
					} else if (docv.startsWith("command:repeat")) {
						taskService.claim(task2.getId(), user.getUsername());
						// 如果该线为转派，则将新的任务转派给制定的用户或组
					} else if (docv.startsWith("command:transfer")) {
						try {
							assignmentTask(bean2, task2, user, transfer_value, transfer_type);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 签收任务
	 * 
	 * @param bean
	 * @param taskID
	 * @param user
	 * @return
	 * @
	 */
	public boolean claimTask(BizInfo bean, String taskID, String username){
		
		// 签收进行权限判断
		Task task = getTaskBean(taskID);
		if (task == null) {
			throw new ServiceException("无效任务");
		}
		if (!StringUtils.isEmpty(task.getAssignee())) {
			throw new ServiceException("任务已被签收");
		}
		boolean flag = claimRole(task, username);
		if (!flag) {
			List<String> group = getTaskCandidateGroup(task); 
			StringBuilder roles = new StringBuilder();
			if(group != null && !group.isEmpty()){
				for(String role : group)
					roles.append(role).append(" ");
			}
			throw new ServiceException("没有权限签收该任务,当前任务代办角色为 :" + roles.toString());
		}
		taskService.claim(taskID, username);
		return true;
	}
	
	private boolean claimRole(Task task, String username) {
		List<String> group = getTaskCandidateGroup(task); //
		logger.info("group :"+group);
		boolean flag = false ;
		if (group != null && !group.isEmpty()) {
			Set<String> roles = roleService.getUserRolesByUserName(username);
			logger.info("user roles "+ roles);
			if(roles != null)
				for (String group2 : group) {
					if (roles.contains(group2)) {
						flag = true;
						break;
					}
				}
		} else {
			flag = true; // 环节没设置签收角色
		}
		return flag;
	}

	public Map<String, Object> loadProcessList(){
		
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		Map<String, Object> result = new HashMap<String, Object>();
		for (ProcessDefinition pd : list) {
			if (pd.getResourceName() != null && pd.getResourceName().startsWith("org/activiti/explorer/demo/process/")) {
				continue;
			}
			result.put(pd.getKey(), pd.getName());
		}
		return result;
	}
	/**
	 * 转派任务
	 * 
	 * @param bean
	 * @param taskID
	 * @param user
	 * @param toUser
	 * @return
	 * @
	 */
	public boolean assignmentTask(BizInfo bean, String taskID, LoginUser user, String toAssignment,String assignmentType){
	
		if (!("group".equalsIgnoreCase(assignmentType) || "user".equalsIgnoreCase(assignmentType))) {
			throw new ServiceException("参数错误");
		}
		Task task = getTaskBean(taskID);
		if (!user.getUsername().equals(task.getAssignee())) {
			throw new ServiceException("没有权限处理该任务");
		}
		return assignmentTask(bean, task, user, toAssignment, assignmentType);
	}

	@Override
	public List<String> getTaskCandidateGroup(Task task) {
		
		List<IdentityLink> links = taskService.getIdentityLinksForTask(task.getId());
		List<String> result = new ArrayList<String>();
		if(links != null && !links.isEmpty()) {
			for (IdentityLink il : links) {
				if("candidate".equals(il.getType())) {
					String groupName = il.getGroupId();
					if(StringUtils.isNotEmpty(groupName))
						result.add(groupName);
				}
			}
		}
		return result;
	}
	
	@Override
	public void interceptTask(String taskId){
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		List<String> groups = this.getTaskCandidateGroup(task);
		if (groups != null) {
			for (String group : groups) {
				if (group == null) {
					continue;
				}
				taskService.deleteCandidateGroup(task.getId(), group);
			}
		}
		taskService.addCandidateUser(task.getId(), WebUtil.getLoginUser().getUsername());
	}

	private boolean assignmentTask(BizInfo bean, Task task, LoginUser user,String toAssignment, String assignmentType){

		if (StringUtils.isEmpty(toAssignment)) {
			return false;
		}
		// 先删除定义的组，然后添加新的组
		List<String> groups = getTaskCandidateGroup(task);
		if (groups != null) {
			try {
				for (String group : groups) {
					if (group == null) {
						continue;
					}
					taskService.deleteCandidateGroup(task.getId(), group);
				}
				taskService.unclaim(task.getId());
			} catch (Exception e) {
			}
		}
		String[] temps = toAssignment.split(",");
		for (String t : temps) {
			if (StringUtils.isEmpty(t)) {
				continue;
			}
			taskService.addCandidateGroup(task.getId(), t);
		}
		return true;
	}

	@Override
	public List<String[]> getNextTaskInfo(BizInfo bean){
		
		List<String[]> taskList = new ArrayList<String[]>();
		// 由于逻辑问题，当前先不处理下一步任务，只处理该任务是否已经结束
		ProcessInstance bean2 = runtimeService.createProcessInstanceQuery().processInstanceId(bean.getProcessInstanceId()).singleResult();
		if (bean2 == null) {// 已经结束
			return taskList;
		} else {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(bean.getProcessInstanceId()).list();
			if (tasks != null && tasks.size() > 0) {
				StringBuffer sb = null;
				for(Task task : tasks){
					sb = new StringBuffer();
					if (StringUtils.isEmpty(task.getAssignee())) {
						List<String> list = getTaskCandidateGroup(task);
						for (String s : list) {
							sb.append(s + ",");
						}
					}
					taskList.add(new String[] { task.getId(), task.getTaskDefinitionKey(), task.getName(), task.getAssignee(),sb.toString() });
				}
				return taskList;
			}
			return taskList;
		}
	}
	
	@Override
	public String[] getNextTaskInfo(String nextTaskId,Map<String,Object> params){
		
		// 由于逻辑问题，当前先不处理下一步任务，只处理该任务是否已经结束
		Task task = taskService.createTaskQuery().taskId(nextTaskId).singleResult();
		taskService.complete(nextTaskId,params);
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
		if (tasks != null && !tasks.isEmpty()) {
			StringBuffer sb = new StringBuffer();
			task = tasks.get(0);//TODO 分派子单流程任务代办人设置为当前登录人员
			List<String> groups = getTaskCandidateGroup(task);
			if (groups != null) {
				for (String group : groups) {
					if (group == null) {
						continue;
					}
					taskService.deleteCandidateGroup(task.getId(), group);
				}
			}
			taskService.addCandidateUser(task.getId(), WebUtil.getLoginUser().getUsername());
			if (StringUtils.isEmpty(task.getAssignee())) {
				List<String> list = getTaskCandidateGroup(task);
				for (String s : list) {
					sb.append(s + ",");
				}
			}
			return new String[] { task.getId(), task.getTaskDefinitionKey(),task.getName(), task.getAssignee(), sb.toString() };
		}
		return new String[] {};
	}

	@Override
	public Task getTaskBean(String taskID){
		
		if (StringUtils.isEmpty(taskID)) {
			return null;
		}
		return taskService.createTaskQuery().taskId(taskID).singleResult();
	}

	/**
	 * 获取当前用户对工单有权限处理的任务，并返回操作权限 返回HANDLE，表示可以进行处理，SIGN表示可以进行签收，其他无权限<br>
	 * 返回格式：任务ID:权限
	 * 
	 * @param bean
	 *            工单对象
	 * @param user
	 *            用户
	 * @return
	 * @
	 */
	public String getWorkAccessTask(BizInfo bean, String username){
		
		// 先判断当前工单的所有活动是否可以进行处理
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(bean.getProcessInstanceId()).list();
		for (Task task : tasks) {
			if (username.equals(task.getAssignee())) {
				return task.getId() + ":HANDLE";
			}
		}
		return null;
	}

	@Override
	public String getWorkAccessTask(String taskID, String username){
		
		String result = null;
		Task task = getTaskBean(taskID);
		if (task == null) {
			return null;
		}
		// 判断指定处理人
		if(StringUtils.isNotEmpty(task.getAssignee())) {
			if (username.equals(task.getAssignee())) {
				result = Constants.HANDLE;
			}
		} else {
			// 无指定处理人，判断任务角色
			boolean flag = claimRole(task, username);
			if (flag) {
				result = Constants.SIGN;
			}
		}
		return result;
	}

	@Override
	public int getWorkOrderVersion(BizInfo bean){
		
		String processDefinitionId = bean.getProcessDefinitionId();
		ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		if (definition == null) {
			throw new ServiceException("找不到流程定义:" + processDefinitionId);
		}
		return definition.getVersion();
	}

	@Override
	public String[] getWorkOrderHistoryTask(String processInstanceId){
		
		List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
		List<String> temps = new ArrayList<String>();
		for (HistoricTaskInstance task : tasks) {
			if (task.getEndTime() == null) {
				continue;
			}
			if (!temps.contains(task.getTaskDefinitionKey()))
				temps.add(task.getTaskDefinitionKey());
		}
		return (String[]) temps.toArray(new String[temps.size()]);
	}

	public List[] getHighLightedElement(ProcessDefinitionEntity processDefinitionEntity,List<HistoricActivityInstance> historicActivityInstances) {
	
		List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
		List<String> activitys = new ArrayList<String>();// 用以保存高亮的流程
		for (int i = 0; i < historicActivityInstances.size(); i++) {// 对历史流程节点进行遍历
			HistoricActivityInstance hai = historicActivityInstances.get(i);
			activitys.add(hai.getActivityId());
			if (i == historicActivityInstances.size() - 1) {
				continue;
			}
			ActivityImpl activityImpl = processDefinitionEntity.findActivity(hai.getActivityId());// 得到节点定义的详细信息
			if (activityImpl == null) {
				continue;
			}
			List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
			ActivityImpl sameActivityImpl1 = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i + 1).getActivityId());// 将后面第一个节点放在时间相同节点的集合里
			sameStartTimeNodes.add(sameActivityImpl1);
			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
				HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
				HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
				if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {// 如果第一个节点和第二个节点开始时间相同保存
					ActivityImpl sameActivityImpl2 = processDefinitionEntity
							.findActivity(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {// 有不相同跳出循环
					break;
				}
			}
			List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
			for (PvmTransition pvmTransition : pvmTransitions) {// 对所有的线进行遍历
				ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					highFlows.add(pvmTransition.getId());
				}
			}

		}
		return new List[] { activitys, highFlows };
	}

	/**
	 * 显示流程实例图片
	 * 
	 * @param id
	 * @return
	 * @
	 */
	public InputStream viewProcessImage(BizInfo bean){
		
		ProcessInstance bean2 = runtimeService.createProcessInstanceQuery().processInstanceId(bean.getProcessInstanceId()).singleResult();
		String pdid = null;
		if (bean2 == null) {
			HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceId(bean.getProcessInstanceId()).singleResult();
			if (hpi != null)
				pdid = hpi.getProcessDefinitionId();
		} else {
			pdid = bean2.getProcessDefinitionId();
		}
		List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(bean.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().asc().list();
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(pdid);
		BpmnModel bm = repositoryService.getBpmnModel(pdid);
		List[] hiLists = getHighLightedElement(processDefinition, list);
		try {
			InputStream imageStream = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator()
					.generateDiagram(bm, "PNG", hiLists[0], hiLists[1],
							processEngine.getProcessEngineConfiguration().getActivityFontName(),
							processEngine.getProcessEngineConfiguration().getLabelFontName(),
							processEngine.getProcessEngineConfiguration().getClassLoader(), 1.0);
			return imageStream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ProcessDefinition getProcDefById(String id) {
		return repositoryService.getProcessDefinition(id);
	}

	@Override
	public ProcessDefinition getLatestProcDefByKey(String key) {
		return repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
	}
	
	@Override
	@Transactional
	public boolean copyVariables(ProcessDefinition processDefinition) throws Exception {
		
		boolean flag = false;
		if(processDefinition != null) {
			ProcessDefinition newProcessDefinition = getLatestProcDefByKey(processDefinition.getKey());
			processDao.copyVariables(processDefinition, newProcessDefinition);
			flag = true;
		}
		return flag;
	}
}
