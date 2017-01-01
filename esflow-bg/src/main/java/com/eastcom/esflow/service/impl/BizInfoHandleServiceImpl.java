package com.eastcom.esflow.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.esflow.bean.AbstractVariable;
import com.eastcom.esflow.bean.BizInfo;
import com.eastcom.esflow.service.IBizInfoHandleServcice;
import com.eastcom.esflow.service.IBizInfoService;
import com.eastcom.esflow.service.IProcessDefinitionService;
import com.eastcom.esflow.service.IProcessExecuteService;
import com.eastcom.esflow.service.RoleService;
import com.eastcom.esflow.util.Constants;
import com.eastcom.esflow.util.WebUtil;

@Service
@Transactional(readOnly = true)
public class BizInfoHandleServiceImpl implements IBizInfoHandleServcice {

	@Autowired
	private RoleService roleService;

	@Autowired
	private IProcessDefinitionService processDefinitionService;

	@Autowired
	private IBizInfoService bizInfoService;
	
	@Autowired
	private IProcessExecuteService processService;
	
	@Override
	public Map<String, Object> getCurrentTask(String bizId, String loginUser) {

		Map<String, Object> result = new HashMap<String, Object>();
		BizInfo workBean = bizInfoService.getBizInfo(bizId,loginUser);
		String taskId = workBean.getTaskId();
		Task task = processDefinitionService.getTaskBean(taskId);
		String curreOp = processDefinitionService.getWorkAccessTask(taskId, loginUser);
		if (task != null)
			result.put("$currentTaskName", task.getName());
		List<AbstractVariable> list = this.processService.loadHandleProcessValBean(workBean, taskId);
		if (Constants.HANDLE.equalsIgnoreCase(curreOp)) {
			result.put("ProcessTaskValBeans", list);
			result.put("handleUser", WebUtil.userToMap(roleService.loadUsersByUserName(loginUser)));
			Map<String, String> buttons = processDefinitionService.findOutGoingTransNames(taskId, false);
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
		return result;
	}
	
	@Override
	@Transactional(readOnly = false)
	public BizInfo submit(Map<String, Object> params){
		
		return  this.processService.submit(params, null);
	}
	
}
