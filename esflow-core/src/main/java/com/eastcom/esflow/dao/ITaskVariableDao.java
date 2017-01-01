package com.eastcom.esflow.dao;

import java.util.List;
import java.util.Map;

import com.eastcom.esflow.bean.TaskVariable;
import com.eastcom.esflow.common.dao.IBaseDao;
import com.eastcom.esflow.common.utils.PageHelper;

/**
 * 流程任务模板DAO
 */
public interface ITaskVariableDao extends IBaseDao<TaskVariable> {

	public List<TaskVariable> loadTaskVariables(String processDefinitionId, int version, String... taskIdS);

	public int getProcessOrder(TaskVariable bean);
	
	public List<TaskVariable> getRefList(Map<String,Object> params);

	public List<TaskVariable> findTaskVariable(TaskVariable taskVariable);

	public TaskVariable getTaskVariableByName(String name, String taskId,String processDefinedId);

	/**
	 * 参数分页查询
	 * @param processDefinitionId
	 * @param version
	 * @param taskIdS
	 * @param page
	 * @return
	 * @throws DaoException
	 */
	public PageHelper<TaskVariable> loadTaskVariables(String processDefinitionId,int version, PageHelper<TaskVariable> page, String taskIdS);
}
