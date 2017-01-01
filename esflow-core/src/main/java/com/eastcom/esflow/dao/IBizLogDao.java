package com.eastcom.esflow.dao;

import java.util.List;
import java.util.Map;

import com.eastcom.esflow.bean.BizLog;
import com.eastcom.esflow.common.dao.IBaseDao;

public interface IBizLogDao extends IBaseDao<BizLog> {

	public List<BizLog> loadLogByBizId(String bizId) ;

	public List<Map<String,Object>> findBizInfoIds(String handleUser);

	/**
	 * 获取解决工单的任务ID
	 * @param bizId
	 * @param handleResult
	 * @param handleName
	 * @return
	 */
	public String findCompleteBizlogs(String bizId, String handleResult);

	/**
	 * 获取工作量评估 列表工单id
	 * @return
	 */
	public List<Map<String, String>> findBizInfoIds();

	/**
	 * 获取指定处理结果的 日志
	 * @param params
	 * @return
	 */
	public List<BizLog> findBizLogs(BizLog bizLog);
}
