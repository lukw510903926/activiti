package com.eastcom.esflow.dao;

import java.util.Map;

import com.eastcom.esflow.bean.BizSystemUserConf;
import com.eastcom.esflow.common.dao.IBaseDao;
import com.eastcom.esflow.common.utils.PageHelper;

public interface BizSystemUserConfDao extends IBaseDao<BizSystemUserConf>{

	public PageHelper<Map<String,String>> findBizSystemUserConf(PageHelper<Map<String,String>>  page,Map<String,String> params);
}
