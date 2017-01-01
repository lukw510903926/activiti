package com.eastcom.esflow.service;

import java.util.List;
import java.util.Map;

import com.eastcom.esflow.bean.BizSystemUserConf;
import com.eastcom.esflow.common.service.IBaseService;
import com.eastcom.esflow.common.utils.PageHelper;

public interface BizSystemUserConfService extends IBaseService<BizSystemUserConf>{

	public PageHelper<Map<String,String>> findBizSystemUserConf(PageHelper<Map<String,String>>  page,Map<String,String> params);

	public void saveOrUpdate(BizSystemUserConf conf);
	
	public void deleteByIds(List<String> list);

	public void saveBizSystemUserConf(List<String> userIds, Map<String, String> params);

}
