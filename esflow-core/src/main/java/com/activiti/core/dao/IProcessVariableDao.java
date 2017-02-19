package com.activiti.core.dao;

import java.util.List;
import java.util.Map;

import com.activiti.core.bean.ProcessVariable;
import com.activiti.core.common.dao.IBaseDao;
import com.activiti.core.common.utils.PageHelper;

/**
 * 流程全局配置DAO
 */
public interface IProcessVariableDao extends IBaseDao<ProcessVariable> {

	public List<ProcessVariable> loadProcessVariables(String processDefinitionId, int version) ;

	public List<ProcessVariable> getRefList(Map<String,Object> params);
	
	public void deleteById(String id);

	/**
	 * 流程参数分页
	 * @param processDefinitionId
	 * @param version
	 * @param page
	 * @return
	 * @
	 */
	public PageHelper<ProcessVariable> loadProcessVariables(String processDefinitionId, int version,PageHelper<ProcessVariable> page) ;

	public int getProcessOrder(ProcessVariable bean);
}
