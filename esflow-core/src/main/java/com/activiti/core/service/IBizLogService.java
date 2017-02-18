package com.activiti.core.service;

import java.util.List;

import com.activiti.core.bean.BizLog;

public interface IBizLogService {

	public void addBizLog(BizLog... beans) ;

	public List<BizLog> loadBizLogs(String bizId) ;

	public BizLog getBizLogById(String id) ;
}
