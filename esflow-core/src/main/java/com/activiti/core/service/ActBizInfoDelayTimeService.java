package com.activiti.core.service;

import java.util.List;

import com.activiti.core.bean.ActBizInfoDelayTime;
import com.activiti.core.common.service.IBaseService;

public interface ActBizInfoDelayTimeService extends IBaseService<ActBizInfoDelayTime> {

	/**
	 * 根据工单id查询最新记录
	 * @param bizId
	 * @return
	 */
	public ActBizInfoDelayTime findActBizInfoByBizId(String bizId, String taskName);
	
	public void saveOrUpdate(ActBizInfoDelayTime actBizInfo);

	public List<ActBizInfoDelayTime> findActBizInfoDelayTime(ActBizInfoDelayTime delayTime);
}
