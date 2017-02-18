package com.activiti.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.activiti.core.bean.BizInfo;
import com.activiti.core.bean.BizSystemUserConf;
import com.activiti.core.bean.ProcessVariableInstance;
import com.activiti.core.common.service.BaseServiceImpl;
import com.activiti.core.common.utils.PageHelper;
import com.activiti.core.dao.BizSystemUserConfDao;
import com.activiti.core.service.BizSystemUserConfService;
import com.activiti.core.service.IBizInfoService;
import com.activiti.core.service.IProcessVariableService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BizSystemUserConfServiceImpl extends BaseServiceImpl<BizSystemUserConf> implements BizSystemUserConfService {

	@Autowired
	private BizSystemUserConfDao bizSystemUserConfDao;
	
	@Autowired
	private IProcessVariableService processValService;
	
	@Autowired
	private IBizInfoService bizInfoService;
	
	private Logger logger = Logger.getLogger("bizSystemUserConfServiceImpl");
	
	@Override
	@Transactional(readOnly = false)
	public void saveOrUpdate(BizSystemUserConf conf){
		
		if(this.check(conf))
			this.bizSystemUserConfDao.saveOrUpdate(conf);
		
	}
	
	@Override
	@Transactional(readOnly = false)
	public void saveBizSystemUserConf(List<String> userNames,Map<String,String> params){
		
		BizSystemUserConf conf = null;
		String userType = params.get("userType");
		String systemName = params.get("systemName");
		String bizType = params.get("bizType");
		if(StringUtils.isBlank(userType)||StringUtils.isBlank(systemName))
			return;
		for(String userName : userNames){
			if(StringUtils.isNotBlank(userName)){
				conf = new BizSystemUserConf();
				conf.setSystemName(systemName);
				conf.setUserName(userName);
				conf.setUserType(userType);
				conf.setBizType(bizType);
				conf.setCreateTime(new Date());
				this.saveOrUpdate(conf);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteByIds(List<String> list){
		
		if(list != null && !list.isEmpty()){
			for (String id : list) {
				if(StringUtils.isNotBlank(id))
					this.bizSystemUserConfDao.deleteById(id);
			}
		}
	}
	
	@Override
	public PageHelper<Map<String,String>> findBizSystemUserConf(PageHelper<Map<String,String>>  page, Map<String,String> params){

		List<ProcessVariableInstance> list = null;
		BizInfo bizInfo = null;
		if(StringUtils.isNotBlank(params.get("bizId"))){
			Map<String,String> query = new HashMap<String, String>();
			query.put("bizId", params.get("bizId"));
			if(StringUtils.isNotBlank(params.get("systemType")))
				query.put("name", params.get("systemType"));
			else
				query.put("name", "systemName");
			list = this.processValService.getProcessVariableInstances(query);
			if(list != null && !list.isEmpty())
				params.put("systemName", list.get(0).getValue());
			bizInfo = this.bizInfoService.get(params.get("bizId"));
			params.put("bizType", bizInfo.getBizType());
		}
		PageHelper<Map<String,String>> result = this.bizSystemUserConfDao.findBizSystemUserConf(page, params);
		logger.info("result: " + result);
		return result;
	}

	private boolean check(BizSystemUserConf conf){
		
		PageHelper<Map<String, String>> page = new PageHelper<Map<String,String>>();
		page.setPage(-1);
		page.setRows(-1);
		Map<String,String> params = new HashMap<String, String>();
		params.put("userType", conf.getUserType());
		params.put("systemName", conf.getSystemName());
		params.put("userName", conf.getUserName());
		params.put("bizType", conf.getBizType());
		List<Map<String, String>> result = this.findBizSystemUserConf(page, params).getList();
		if(result != null && !result.isEmpty())
			return false;
		return true;
	}
}
