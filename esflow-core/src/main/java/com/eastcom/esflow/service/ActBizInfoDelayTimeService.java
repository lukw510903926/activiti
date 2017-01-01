package com.eastcom.esflow.service;

import java.util.List;

import com.eastcom.esflow.bean.ActBizInfoDelayTime;
import com.eastcom.esflow.common.service.IBaseService;

public interface ActBizInfoDelayTimeService extends IBaseService<ActBizInfoDelayTime>{

	/**
	 * 根据工单id查询最新记录
	 * @param bizId
	 * @param taskDefKey
	 * @return
	 */
	public ActBizInfoDelayTime findActBizInfoByBizId(String bizId,String taskName);
	
	public void saveOrUpdate(ActBizInfoDelayTime actBizInfo);

	public List<ActBizInfoDelayTime> findActBizInfoDelayTime(ActBizInfoDelayTime delayTime);
}
