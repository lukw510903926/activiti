package com.activiti.core.service;

import java.util.List;
import java.util.Map;

import com.activiti.core.bean.BizSystemUserConf;
import com.activiti.core.common.utils.PageHelper;
import com.activiti.core.common.service.IBaseService;

public interface BizSystemUserConfService extends IBaseService<BizSystemUserConf>{

	public PageHelper<Map<String,String>> findBizSystemUserConf(PageHelper<Map<String,String>>  page, Map<String,String> params);

	public void saveOrUpdate(BizSystemUserConf conf);
	
	public void deleteByIds(List<String> list);

	public void saveBizSystemUserConf(List<String> userIds, Map<String, String> params);

}
