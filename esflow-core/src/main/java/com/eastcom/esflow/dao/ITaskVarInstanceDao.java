package com.eastcom.esflow.dao;

import java.util.List;
import java.util.Map;

import com.eastcom.esflow.bean.AbstractVariableInstance;
import com.eastcom.esflow.bean.BizLog;
import com.eastcom.esflow.bean.TaskVariableInstance;
import com.eastcom.esflow.common.dao.IBaseDao;

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
