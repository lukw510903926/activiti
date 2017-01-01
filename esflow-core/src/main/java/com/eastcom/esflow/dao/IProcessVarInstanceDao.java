package com.eastcom.esflow.dao;

import java.util.List;
import java.util.Map;

import com.eastcom.esflow.bean.ProcessVariableInstance;
import com.eastcom.esflow.common.dao.IBaseDao;

/**
 * 流程实例DAO
 */
public interface IProcessVarInstanceDao extends IBaseDao<ProcessVariableInstance> {

	public List<ProcessVariableInstance> findVariablesOfBizInfo(String bizInfoId);

	public List<ProcessVariableInstance> loadProcessInstances(String processInstanceId);

	public List<ProcessVariableInstance> loadProcessInstancesByBizId(String bizId) ;

	/**
	 * 获取工单配置信息
	 * @param bizId
	 * @return
	 */
	public List<Map<String, String>> getBizInfo(String bizId);
	
	public List<ProcessVariableInstance> getProcessVariableInstances(Map<String, String> params);

	public void deleteByVarId(String variableId);
}
