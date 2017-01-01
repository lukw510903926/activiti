package com.eastcom.esflow.dao;

import java.util.List;

import com.eastcom.esflow.bean.Countersign;
import com.eastcom.esflow.common.dao.IBaseDao;

public interface CountersignDao extends IBaseDao<Countersign>{

	public List<Countersign> findCountersign(Countersign countersign);
}
