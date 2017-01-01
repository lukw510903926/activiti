package com.eastcom.esflow.dao;

import java.util.List;

import com.eastcom.esflow.bean.BizTimedTask;
import com.eastcom.esflow.common.dao.IBaseDao;

public interface BizTimedTaskDao extends IBaseDao<BizTimedTask>{

	public List<BizTimedTask> findBizTimedTask(BizTimedTask bizTask);

	void deleteTimedTask(String id);
}
