package com.activiti.core.dao;

import java.util.List;
import java.util.Map;

import com.activiti.core.bean.AbstractVariableInstance;
import com.activiti.core.bean.BizLog;
import com.activiti.core.bean.TaskVariableInstance;
import com.activiti.core.common.dao.IBaseDao;

/**
 * 流程任务实例DAO
 */
public interface ITaskVarInstanceDao extends IBaseDao<TaskVariableInstance> {

	public List<TaskVariableInstance> findByProcInstId(String processInstanceId) ;

	public List<AbstractVariableInstance> loadValueByLog(BizLog logBean) ;

	public List<TaskVariableInstance> findByTaskId(String taskId) ;

	/**
	 * 
	 * @param params
	 * @return
	 */
	public List<TaskVariableInstance> findTaskVariableInstance(Map<String, String> params);

	public TaskVariableInstance getTaskVarInstanceByVarName(Map<String, String> params);

	/**
	 * 
	 * @param variableId
	 */
	public void deleteByVarId(String variableId);
}
