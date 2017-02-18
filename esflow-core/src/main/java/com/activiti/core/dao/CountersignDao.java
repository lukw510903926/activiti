package com.activiti.core.dao;

import java.util.List;

import com.activiti.core.common.dao.IBaseDao;
import com.activiti.core.bean.Countersign;

public interface CountersignDao extends IBaseDao<Countersign> {

	public List<Countersign> findCountersign(Countersign countersign);
}
