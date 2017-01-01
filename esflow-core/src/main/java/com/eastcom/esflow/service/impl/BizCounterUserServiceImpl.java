package com.eastcom.esflow.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.esflow.bean.BizCounterUser;
import com.eastcom.esflow.common.service.BaseServiceImpl;
import com.eastcom.esflow.common.utils.PageHelper;
import com.eastcom.esflow.dao.BizCounterUserDao;
import com.eastcom.esflow.service.BizCounterUserService;

/**
 * 2016年8月23日
 * @author lukw 
 * 下午8:16:03
 * com.eastcom.esflow.service.impl
 * @email lukw@eastcom-sw.com
 */

@Service
@Transactional(readOnly = true)
public class BizCounterUserServiceImpl extends BaseServiceImpl<BizCounterUser> implements BizCounterUserService{

	@Autowired
	private BizCounterUserDao bizCounterUserDao;
	
	@Override
	public PageHelper<BizCounterUser> findBizCounterUser(PageHelper<BizCounterUser> page, BizCounterUser user) {
		
		return this.bizCounterUserDao.findBizCounterUser(page, user);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteUser(BizCounterUser user){
		
		PageHelper<BizCounterUser> page = new PageHelper<BizCounterUser>();
		page.setPage(-1);
		page.setRows(-1);
		List<BizCounterUser> list = this.findBizCounterUser(page, user).getList();
		if(list !=null && !list.isEmpty()){
			this.delete(list);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void saveUser(List<Map<String,String>> list,String bizId,String taskId){
		
		BizCounterUser user = null;
		if(list != null && !list.isEmpty()){
			for (Map<String, String> map : list) {
				user = new BizCounterUser();
				user.setBizId(bizId);
				user.setTaskId(StringUtils.isBlank(taskId)?"START":taskId);
				user.setName(map.get("name"));
				user.setUsername(map.get("username"));
				user.setDeptmentName(map.get("deptmentName"));
				this.bizCounterUserDao.save(user);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void updateUser(List<Map<String,String>> list,String bizId,String taskId){
		
		BizCounterUser user = new BizCounterUser();
		user.setBizId(bizId);
		user.setTaskId(StringUtils.isBlank(taskId)?"START":taskId);
		this.deleteUser(user);
		this.saveUser(list, bizId, taskId);
	}
}

