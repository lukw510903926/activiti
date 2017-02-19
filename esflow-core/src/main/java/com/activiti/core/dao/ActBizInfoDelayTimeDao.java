package com.activiti.core.dao;

import java.util.List;

import com.activiti.core.bean.ActBizInfoDelayTime;
import com.activiti.core.common.dao.IBaseDao;

public interface ActBizInfoDelayTimeDao extends IBaseDao<ActBizInfoDelayTime>{

	public ActBizInfoDelayTime findActBizInfoByBizId(String bizId,String taskName);

	public List<ActBizInfoDelayTime> findActBizInfoDelayTime(ActBizInfoDelayTime delayTime);

}
