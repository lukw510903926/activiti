package com.eastcom.esflow.dao;

import java.util.List;

import com.eastcom.esflow.bean.ActBizInfoDelayTime;
import com.eastcom.esflow.common.dao.IBaseDao;

public interface ActBizInfoDelayTimeDao extends IBaseDao<ActBizInfoDelayTime>{

	public ActBizInfoDelayTime findActBizInfoByBizId(String bizId,String taskName);

	public List<ActBizInfoDelayTime> findActBizInfoDelayTime(ActBizInfoDelayTime delayTime);

}
