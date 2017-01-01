package com.eastcom.esflow.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.esflow.bean.ActBizInfoDelayTime;
import com.eastcom.esflow.common.service.BaseServiceImpl;
import com.eastcom.esflow.dao.ActBizInfoDelayTimeDao;
import com.eastcom.esflow.service.ActBizInfoDelayTimeService;

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
