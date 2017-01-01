package com.eastcom.esflow.dao;

import java.util.List;
import java.util.Map;

import com.eastcom.esflow.bean.ProcessVariable;
import com.eastcom.esflow.common.dao.IBaseDao;
import com.eastcom.esflow.common.utils.PageHelper;

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
