package com.activiti.core.dao;

import java.util.List;
import java.util.Map;

import com.activiti.core.common.dao.IBaseDao;
import com.activiti.core.bean.BizFile;

public interface IBizFileDao extends IBaseDao<BizFile> {

	public List<BizFile> loadBizFileByBizId(String bizId, String taskId);
	
	public List<BizFile> findBizFile(Map<String,String> params);
}
