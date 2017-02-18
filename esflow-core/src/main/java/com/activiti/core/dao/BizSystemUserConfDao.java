package com.activiti.core.dao;

import java.util.Map;

import com.activiti.core.common.dao.IBaseDao;
import com.activiti.core.common.utils.PageHelper;
import com.activiti.core.bean.BizSystemUserConf;

public interface BizSystemUserConfDao extends IBaseDao<BizSystemUserConf> {

	public PageHelper<Map<String,String>> findBizSystemUserConf(PageHelper<Map<String,String>>  page, Map<String,String> params);
}
