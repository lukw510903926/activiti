package com.eastcom.esflow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.esflow.bean.Countersign;
import com.eastcom.esflow.common.service.BaseServiceImpl;
import com.eastcom.esflow.dao.CountersignDao;
import com.eastcom.esflow.service.CountersignService;

@Service
@Transactional
public class CountersignServiceImpl extends BaseServiceImpl<Countersign> implements CountersignService{

	@Autowired
	private CountersignDao countersignDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<Countersign> findCountersign(Countersign countersign) {
		return this.countersignDao.findCountersign(countersign);
	}

}
