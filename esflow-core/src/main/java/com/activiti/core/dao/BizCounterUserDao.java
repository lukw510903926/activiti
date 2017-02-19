package com.activiti.core.dao;

import com.activiti.core.bean.BizCounterUser;
import com.activiti.core.common.dao.IBaseDao;
import com.activiti.core.common.utils.PageHelper;

/**
 * 
 * 2016年8月23日
 * @author lukw 
 * 下午8:01:21
 * com.eastcom.esflow.dao
 * @email lukw@eastcom-sw.com
 */
public interface BizCounterUserDao extends IBaseDao<BizCounterUser>{

	public PageHelper<BizCounterUser> findBizCounterUser(PageHelper<BizCounterUser> page, BizCounterUser user);

}
