package com.eastcom.esflow.dao;

import java.util.Map;

import com.eastcom.esflow.common.dao.IBaseDao;
import com.eastcom.esflow.common.utils.PageHelper;

public interface BizReportDao extends IBaseDao<Map<String,Object>>{
	
	/**
	 * 用户满意度统计
	 * @param page
	 * @param params
	 * @return
	 */
	public PageHelper<Map<String, Object>> bizSatisfaction(PageHelper<Map<String, Object>> page, Map<String, String> params);
	
	/**
	 * 各系统工单状态统计
	 * @param page
	 * @param params
	 * @return
	 */
	public PageHelper<Map<String, Object>> bizSystemStatus(PageHelper<Map<String, Object>> page, Map<String, String> params);

	/**
	 * 工单超时统计
	 * @param page
	 * @param params
	 * @return
	 */
	public PageHelper<Map<String, Object>> bizTimeout( PageHelper<Map<String, Object>> page, Map<String, String> params);

	/**
	 * 各系统报账来源统计
	 * @param page
	 * @param params
	 * @return
	 */
	public PageHelper<Map<String, Object>> bizSystemReportSource(PageHelper<Map<String, Object>> page, Map<String, String> params);

	/**
	 * 驳回
	 * @param page
	 * @param params
	 * @return
	 */
	public PageHelper<Map<String, Object>> bizSystemReject(PageHelper<Map<String, Object>> page, Map<String, String> params);

	/**
	 * 各系统效能统计
	 * @param page
	 * @param params
	 * @return
	 */
	public PageHelper<Map<String, Object>> bizSystemEfficiency(PageHelper<Map<String, Object>> page, Map<String, String> params);

	/**
	 * 各系统延期工单统计
	 * @param page
	 * @param params
	 * @return
	 */
	public PageHelper<Map<String, Object>> bizDelayCount(PageHelper<Map<String, Object>> page, Map<String, String> params);

	/**
	 * 工单支持方式统计
	 * @param page
	 * @param params
	 * @return
	 */
	public PageHelper<Map<String, Object>> bizSupportModeCount(PageHelper<Map<String, Object>> page, Map<String, String> params);

	/**
	 * 工单拦截统计 (最后提供解决方案)
	 * @param page
	 * @param params
	 * @return
	 */
	public PageHelper<Map<String, Object>> bizInterceptCount(PageHelper<Map<String, Object>> page, Map<String, String> params);

	/**
	 * 处理时长统计
	 * @param page
	 * @param params
	 * @return
	 */
	public PageHelper<Map<String, Object>> bizHandleTimeCount(PageHelper<Map<String, Object>> page,Map<String, String> params);

}
