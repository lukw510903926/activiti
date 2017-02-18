package com.activiti.core.service;

import java.util.List;

import com.activiti.core.common.service.IBaseService;
import com.activiti.core.bean.Countersign;

public interface CountersignService extends IBaseService<Countersign> {

	public List<Countersign> findCountersign(Countersign countersign);
}
