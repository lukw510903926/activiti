package com.eastcom.esflow.dao;

import java.util.List;
import java.util.Map;

import com.eastcom.esflow.bean.BizFile;
import com.eastcom.esflow.common.dao.IBaseDao;

public interface IBizFileDao extends IBaseDao<BizFile> {

	public List<BizFile> loadBizFileByBizId(String bizId, String taskId);
	
	public List<BizFile> findBizFile(Map<String,String> params);
}
