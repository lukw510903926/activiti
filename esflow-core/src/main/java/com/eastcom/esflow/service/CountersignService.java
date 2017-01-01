package com.eastcom.esflow.service;

import java.util.List;

import com.eastcom.esflow.bean.Countersign;
import com.eastcom.esflow.common.service.IBaseService;

public interface CountersignService extends IBaseService<Countersign>{

	public List<Countersign> findCountersign(Countersign countersign);
}
