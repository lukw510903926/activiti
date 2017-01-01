package com.eastcom.esflow.service;

import java.util.List;

import com.eastcom.esflow.bean.BizLog;

public interface IBizLogService {

	public void addBizLog(BizLog... beans) ;

	public List<BizLog> loadBizLogs(String bizId) ;

	public BizLog getBizLogById(String id) ;
}
