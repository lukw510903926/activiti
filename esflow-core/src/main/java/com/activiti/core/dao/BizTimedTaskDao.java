package com.activiti.core.dao;

import java.util.List;

import com.activiti.core.bean.BizTimedTask;
import com.activiti.core.common.dao.IBaseDao;

public interface BizTimedTaskDao extends IBaseDao<BizTimedTask>{

	public List<BizTimedTask> findBizTimedTask(BizTimedTask bizTask);

	void deleteTimedTask(String id);
}
