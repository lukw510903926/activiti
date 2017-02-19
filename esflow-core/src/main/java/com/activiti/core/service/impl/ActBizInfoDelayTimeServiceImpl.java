package com.activiti.core.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.activiti.core.bean.ActBizInfoDelayTime;
import com.activiti.core.common.service.BaseServiceImpl;
import com.activiti.core.dao.ActBizInfoDelayTimeDao;
import com.activiti.core.service.ActBizInfoDelayTimeService;

@Service
public class ActBizInfoDelayTimeServiceImpl extends BaseServiceImpl<ActBizInfoDelayTime> implements ActBizInfoDelayTimeService{

	@Autowired
	private ActBizInfoDelayTimeDao actBizInfoDelayTimeDao;

	@Override
	public void saveOrUpdate(ActBizInfoDelayTime actBizInfo){
		
		if(StringUtils.isNotBlank(actBizInfo.getId())){
			this.actBizInfoDelayTimeDao.update(actBizInfo);
		}else{
			this.actBizInfoDelayTimeDao.save(actBizInfo);
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public ActBizInfoDelayTime findActBizInfoByBizId(String bizId,String taskId) {
		
		if(StringUtils.isBlank(bizId))
			return null;
		return actBizInfoDelayTimeDao.findActBizInfoByBizId(bizId,taskId);
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActBizInfoDelayTime> findActBizInfoDelayTime(ActBizInfoDelayTime delayTime){
		
		return this.actBizInfoDelayTimeDao.findActBizInfoDelayTime(delayTime);
	}
}