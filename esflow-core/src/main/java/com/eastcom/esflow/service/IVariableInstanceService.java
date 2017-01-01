package com.eastcom.esflow.service;

import java.util.List;
import java.util.Map;

import com.eastcom.esflow.bean.AbstractVariableInstance;
import com.eastcom.esflow.bean.BizInfo;
import com.eastcom.esflow.bean.BizInfoConf;
import com.eastcom.esflow.bean.BizLog;
import com.eastcom.esflow.bean.TaskVariableInstance;

/**
 * 流程实例、流程任务实例数据处理
 */
public interface IVariableInstanceService {

	/**
	 * 添加
	 * 
	 * @param beans
	 * @
	 */
	public void addProcessInstance(AbstractVariableInstance... beans) ;

	public void updateProcessInstance(AbstractVariableInstance... beans) ;

	/**
	 * 加载某个工单所填写的所有数据
	 * 
	 * @param bean
	 * @return
	 * @
	 */
	public List<AbstractVariableInstance> loadInstances(BizInfo bean) ;

	public Map<String, AbstractVariableInstance> getVarMap(BizInfo bizInfo, BizInfoConf bizInfoConf, VariableLoadType type);

	/**
	 * 根据LOG记录加载对应的数据
	 * 
	 * @param logBean
	 * @return
	 * @
	 */
	public List<AbstractVariableInstance> loadValueByLog(BizLog logBean) ;
	
	public List<TaskVariableInstance> findTaskVariableInstance(Map<String, String> params);

	enum VariableLoadType {
		ALL, UPDATABLE
	}

	/**
	 * 获取指定参数 最新实例
	 * @param params
	 * @return
	 */
	public TaskVariableInstance getTaskVarInstanceByVarName(Map<String, String> params);

	/**
	 * 工时修改
	 * @param list
	 */
	public void reSetWorkTime(List<Map<String, String>> list);
	
}
