package com.eastcom.esflow.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eastcom.esflow.common.dao.BaseDaoImpl;
import com.eastcom.esflow.common.utils.PageHelper;
import com.eastcom.esflow.dao.BizReportDao;

@Repository
public class BizReportDaoImpl extends BaseDaoImpl<Map<String,Object>> implements BizReportDao{

	@Override
	public PageHelper<Map<String, Object>> bizSatisfaction(PageHelper<Map<String, Object>> page, Map<String, String> params) {
		
		List<Object> list = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT BIZ.SYSTEM_NAME AS SYSTEM,SUM(CASE WHEN biz.SATISFACTION='好' THEN 1 ELSE 0 END )AS GOOD,");
		sql.append(" SUM(CASE WHEN biz.SATISFACTION='中' THEN 1 ELSE 0 END ) AS MIDDLE,SUM(CASE WHEN biz.SATISFACTION='差' THEN 1 ELSE 0 END ) AS BAD");
		sql.append(" FROM esflow.report_event_biz biz where biz.task_def_key ='已完成'");
		
		if(StringUtils.isNotBlank(params.get("systemName"))){
			sql.append(" and biz.SYSTEM_NAME = ? ");
			list.add(params.get("systemName"));
		}
		if(StringUtils.isNotBlank(params.get("createTime")) && StringUtils.isNotBlank(params.get("createTime2"))){
			
			sql.append(" AND BIZ.CREATE_TIME BETWEEN ? AND ? ");
			list.add(params.get("createTime"));
			list.add(params.get("createTime2"));
		}
		sql.append("GROUP BY SYSTEM_NAME ORDER BY SYSTEM_NAME");
		return this.findBySql(page,sql.toString(),list.toArray(),Map.class);
	}
	
	@Override
	public PageHelper<Map<String, Object>> bizSystemStatus(PageHelper<Map<String, Object>> page, Map<String, String> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("SELECT biz.SYSTEM_NAME AS SYSTEM,SUM(CASE WHEN BIZ.TASK_DEF_KEY='新建' THEN 1 ELSE 0 END)AS NEWBIZ,");
		sql.append(" SUM(CASE WHEN BIZ.TASK_DEF_KEY='草稿' THEN 1 ELSE 0 END)AS TEMP,SUM(CASE WHEN BIZ.TASK_DEF_KEY='服务台处理' THEN 1 ELSE 0 END)AS SERVICE,");
		sql.append(" SUM(CASE WHEN BIZ.TASK_DEF_KEY='厂商处理' THEN 1 ELSE 0 END)AS VENDOR,SUM(CASE WHEN BIZ.TASK_DEF_KEY='用户反馈' THEN 1 ELSE 0 END)AS USER,");
		sql.append(" SUM(CASE WHEN BIZ.TASK_DEF_KEY='服务台关闭' THEN 1 ELSE 0 END)AS CLOSE,SUM(CASE WHEN BIZ.TASK_DEF_KEY='重新提交' THEN 1 ELSE 0 END)AS REBUILD,");
		sql.append(" SUM(CASE WHEN BIZ.TASK_DEF_KEY='延期申请确认' THEN 1 ELSE 0 END)AS DELAYTASK,SUM(CASE WHEN BIZ.TASK_DEF_KEY='已完成' THEN 1 ELSE 0 END)AS BIZEND,");
		sql.append(" SUM(CASE WHEN BIZ.TASK_DEF_KEY='服务台拦截' THEN 1 ELSE 0 END)AS INTE,COUNT(1) AS TOTAL FROM ESFLOW.report_event_biz biz WHERE BIZ.TASK_DEF_KEY<>'已删除'");
		
		if(StringUtils.isNotBlank(params.get("systemName"))){
			sql.append(" and biz.SYSTEM_NAME = ? ");
			list.add(params.get("systemName"));
		}
		if(StringUtils.isNotBlank(params.get("createTime")) && StringUtils.isNotBlank(params.get("createTime2"))){
			
			sql.append(" AND BIZ.CREATE_TIME BETWEEN ? AND ? ");
			list.add(params.get("createTime"));
			list.add(params.get("createTime2"));
		}
		sql.append(" GROUP BY SYSTEM_NAME ORDER BY SYSTEM_NAME");
		return this.findBySql(page,sql.toString(),list.toArray(),Map.class);
	}
	@Override
	public PageHelper<Map<String, Object>> bizSystemReportSource(PageHelper<Map<String, Object>> page, Map<String, String> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(" SELECT BIZ.SYSTEM_NAME AS SYSTEM, COUNT(1) AS TOTAL , SUM(CASE WHEN BIZ.REPORT_SOURCE='QQ' THEN 1 ELSE 0 END) AS QQ,");
		sql.append(" SUM(CASE WHEN BIZ.REPORT_SOURCE='工单' THEN 1 ELSE 0 END) AS BIZ, SUM(CASE WHEN BIZ.REPORT_SOURCE='电话' THEN 1 ELSE 0 END) AS MOBILE,");
		sql.append(" SUM(CASE WHEN BIZ.REPORT_SOURCE='自监控' THEN 1 ELSE 0 END) AS ONESELF, SUM(CASE WHEN BIZ.REPORT_SOURCE='邮件' THEN 1 ELSE 0 END) AS EMAIL");
		sql.append(" FROM REPORT_EVENT_BIZ BIZ WHERE 1=1 AND BIZ.TASK_DEF_KEY='已完成'");
		
		if(StringUtils.isNotBlank(params.get("systemName"))){
			sql.append(" and BIZ.SYSTEM_NAME = ? ");
			list.add(params.get("systemName"));
		}
		if(StringUtils.isNotBlank(params.get("createTime")) && StringUtils.isNotBlank(params.get("createTime2"))){
			
			sql.append(" AND BIZ.CREATE_TIME BETWEEN ? AND ? ");
			list.add(params.get("createTime"));
			list.add(params.get("createTime2"));
		}
		sql.append("  GROUP BY SYSTEM_NAME ORDER BY TOTAL DESC ");
		return this.findBySql(page,sql.toString(),list.toArray(),Map.class);
	}
	
	@Override
	public PageHelper<Map<String, Object>> bizTimeout(PageHelper<Map<String, Object>> page, Map<String, String> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT BIZ.SYSTEM_NAME AS SYSTEM,SUM(CASE WHEN BIZ.IS_SHUFFLE=1 THEN 1 ELSE 0 END) AS SHUFFLE,");
		sql.append(" SUM(CASE WHEN BIZ.LIMIT_TIME<BIZ.SERVICE_HANDLE THEN 1 ELSE 0 END )AS FIRSTCOUNT,");
		sql.append(" SUM(CASE WHEN (BIZ.LIMIT_TIME<BIZ.SECOND_VENDER_HANDLE AND (BIZ.LIMIT_TIME > BIZ.SERVICE_HANDLE OR ");
		sql.append(" BIZ.SERVICE_HANDLE IS NULL)) THEN 1 ELSE 0 END )AS SECONDCOUNT,SUM(CASE WHEN (BIZ.LIMIT_TIME <BIZ.THREE_VENDER_HANDLE");
		sql.append(" and (BIZ.LIMIT_TIME > BIZ.SERVICE_HANDLE or BIZ.SERVICE_HANDLE IS NULL) and (BIZ.LIMIT_TIME >");
		sql.append(" BIZ.SECOND_VENDER_HANDLE OR BIZ.SECOND_VENDER_HANDLE IS NULL))THEN 1 ELSE 0 END )AS THREECOUNT, ");
		sql.append(" CONCAT(FORMAT(SUM(CASE WHEN BIZ.LIMIT_TIME <BIZ.THREE_VENDER_HANDLE OR BIZ.LIMIT_TIME <BIZ.SECOND_VENDER_HANDLE ");
		sql.append(" OR BIZ.LIMIT_TIME <BIZ.SERVICE_HANDLE THEN 1 ELSE 0 END )/COUNT(1)*100,2),'%') AS TIMECOUNT,");
		sql.append(" SUM(CASE WHEN BIZ.LIMIT_TIME < BIZ.THREE_VENDER_HANDLE OR BIZ.LIMIT_TIME < BIZ.SECOND_VENDER_HANDLE");
		sql.append(" OR BIZ.LIMIT_TIME < BIZ.SERVICE_HANDLE THEN 1 ELSE 0 END ) OUTCOUNT,");
		
		sql.append(" COUNT(1) AS TOTAL FROM ESFLOW.REPORT_EVENT_BIZ BIZ WHERE 1=1");
		if(StringUtils.isNotBlank(params.get("systemName"))){
			sql.append(" and biz.SYSTEM_NAME = ? ");
			list.add(params.get("systemName"));
		}
		if(StringUtils.isNotBlank(params.get("createTime")) && StringUtils.isNotBlank(params.get("createTime2"))){
			
			sql.append(" AND BIZ.CREATE_TIME BETWEEN ? AND ? ");
			list.add(params.get("createTime"));
			list.add(params.get("createTime2"));
		}
		sql.append(" GROUP BY SYSTEM_NAME ORDER BY TOTAL");
		return this.findBySql(page,sql.toString(),list.toArray(),Map.class);
	}
	
	@Override
	public PageHelper<Map<String, Object>> bizSystemReject(PageHelper<Map<String, Object>> page, Map<String, String> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(" SELECT SUM(CASE WHEN VENDER_REJECT = 1 THEN 1 ELSE 0 END ) VERDER,");
		sql.append(" CONCAT(FORMAT(SUM(CASE WHEN USER_REJECT = 1 THEN 1 ELSE 0 END)/COUNT(1)*100,2),'%') as REJECTPERCENT,");
		sql.append(" CONCAT(FORMAT(SUM(CASE WHEN VENDER_REJECT = 1 THEN 1 ELSE 0 END)/COUNT(1)*100,2),'%') as VENDERREJECT,");
		sql.append(" SUM(CASE WHEN USER_REJECT = 1 THEN 1 ELSE 0 END) AS USER, COUNT(1) AS TOTAL, BIZ.SYSTEM_NAME  AS SYSTEM");
		sql.append(" FROM REPORT_EVENT_BIZ BIZ WHERE 1=1 ");
		if(StringUtils.isNotBlank(params.get("systemName"))){
			sql.append(" and BIZ.SYSTEM_NAME = ? ");
			list.add(params.get("systemName"));
		}
		if(StringUtils.isNotBlank(params.get("createTime")) && StringUtils.isNotBlank(params.get("createTime2"))){
			
			sql.append(" AND BIZ.CREATE_TIME BETWEEN ? AND ? ");
			list.add(params.get("createTime"));
			list.add(params.get("createTime2"));
		}
		sql.append(" GROUP BY SYSTEM_NAME ORDER BY TOTAL");
		return this.findBySql(page,sql.toString(),list.toArray(),Map.class);
	}
	
	@Override
	public PageHelper<Map<String, Object>> bizSystemEfficiency(PageHelper<Map<String, Object>> page, Map<String, String> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT BIZ.SYSTEM_NAME AS SYSTEM,SUM(CASE WHEN biz.SATISFACTION='差'THEN 1 ELSE ");
		sql.append(" 0 END) AS BAD,SUM(case when biz.DELAY>0 then 1 else 0 end ) AS DELAY,SUM(CASE WHEN (BIZ.LIMIT_TIME ");
		sql.append(" < BIZ.SERVICE_HANDLE or BIZ.LIMIT_TIME < BIZ.SECOND_VENDER_HANDLE or BIZ.THREE_VENDER_HANDLE>biz.LIMIT_TIME)");
		sql.append(" THEN 1 ELSE 0 END )AS TIMEOUT, SUM(CASE WHEN biz.USER_REJECT =1 THEN 1 ELSE 0 END ) AS REJECT,");
		sql.append(" COUNT(1) AS TOTAL FROM ESFLOW.REPORT_EVENT_BIZ BIZ WHERE BIZ.TASK_DEF_KEY ='已完成'");
		if(StringUtils.isNotBlank(params.get("systemName"))){
			sql.append(" and BIZ.SYSTEM_NAME = ? ");
			list.add(params.get("systemName"));
		}
		if(StringUtils.isNotBlank(params.get("createTime")) && StringUtils.isNotBlank(params.get("createTime2"))){
			
			sql.append(" AND BIZ.CREATE_TIME BETWEEN ? AND ? ");
			list.add(params.get("createTime"));
			list.add(params.get("createTime2"));
		}
		sql.append("GROUP BY SYSTEM ORDER BY TOTAL");
		return this.findBySql(page,sql.toString(),list.toArray(),Map.class);
	}
	
	@Override
	public PageHelper<Map<String, Object>> bizDelayCount(PageHelper<Map<String, Object>> page, Map<String, String> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT BIZ.SYSTEM_NAME AS SYSTEM,COUNT(1) AS TOTAL ,");
		sql.append(" SUM(CASE WHEN BIZ.DELAY<>0 THEN 1 ELSE 0 END) AS DELAY, SUM(CASE WHEN BIZ.DELAY>1 THEN 1 ELSE 0 END) AS DELAYS");
		sql.append(" FROM REPORT_EVENT_BIZ BIZ WHERE 1=1");
		if(StringUtils.isNotBlank(params.get("systemName"))){
			sql.append(" and BIZ.SYSTEM_NAME = ? ");
			list.add(params.get("systemName"));
		}
		if(StringUtils.isNotBlank(params.get("createTime")) && StringUtils.isNotBlank(params.get("createTime2"))){
			
			sql.append(" AND BIZ.CREATE_TIME BETWEEN ? AND ? ");
			list.add(params.get("createTime"));
			list.add(params.get("createTime2"));
		}
		sql.append(" GROUP BY SYSTEM_NAME  ORDER BY TOTAL DESC ");
		return this.findBySql(page,sql.toString(),list.toArray(),Map.class);
	}
	
	@Override
	public PageHelper<Map<String, Object>> bizSupportModeCount(PageHelper<Map<String, Object>> page, Map<String, String> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT BIZ.SYSTEM_NAME AS SYSTEM,COUNT(1) AS TOTAL ,");
		sql.append(" SUM(CASE WHEN BIZ.SUPPORT_MODE='现场' THEN 1 ELSE 0 END) AS LIVE,");
		sql.append(" SUM(CASE WHEN BIZ.SUPPORT_MODE='远程' THEN 1 ELSE 0 END) AS REMOTE,");
		sql.append(" SUM(CASE WHEN (BIZ.SUPPORT_MODE<>'远程' AND BIZ.SUPPORT_MODE<>'现场') THEN 1 ELSE 0 END) AS OTHER");
		sql.append(" FROM REPORT_EVENT_BIZ BIZ WHERE 1=1 and BIZ.TASK_DEF_KEY='已完成'");
		if(StringUtils.isNotBlank(params.get("systemName"))){
			sql.append(" and BIZ.SYSTEM_NAME = ? ");
			list.add(params.get("systemName"));
		}
		if(StringUtils.isNotBlank(params.get("createTime")) && StringUtils.isNotBlank(params.get("createTime2"))){
			
			sql.append(" AND BIZ.CREATE_TIME BETWEEN ? AND ? ");
			list.add(params.get("createTime"));
			list.add(params.get("createTime2"));
		}
		sql.append(" GROUP BY SYSTEM_NAME  ORDER BY TOTAL DESC ");
		return this.findBySql(page,sql.toString(),list.toArray(),Map.class);
	}
	
	@Override
	public PageHelper<Map<String, Object>> bizInterceptCount(PageHelper<Map<String, Object>> page, Map<String, String> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT BIZ.SYSTEM_NAME AS SYSTEM,COUNT(1) AS TOTAL ,");
		sql.append(" CONCAT(FORMAT(SUM(CASE WHEN BIZ.HANDLE_LINE='一线' THEN 1 ELSE 0 END)/COUNT(1)*100,2),'%') as FIRST,");
		sql.append(" CONCAT(FORMAT(SUM(CASE WHEN BIZ.HANDLE_LINE='二线' THEN 1 ELSE 0 END)/COUNT(1)*100,2),'%') as SECOND,");
		sql.append(" CONCAT(FORMAT(SUM(CASE WHEN BIZ.HANDLE_LINE = '三线' THEN 1 ELSE 0 END)/COUNT(1)*100,2),'%') as THREE,");
		sql.append(" SUM(CASE WHEN BIZ.HANDLE_LINE='一线' THEN 1 ELSE 0 END) AS FIRSTCOUNT,");
		sql.append(" SUM(CASE WHEN BIZ.HANDLE_LINE='二线' THEN 1 ELSE 0 END) AS SECONDCOUNT,");
		sql.append(" SUM(CASE WHEN BIZ.HANDLE_LINE = '三线' THEN 1 ELSE 0 END) AS THREECOUNT");
		sql.append(" FROM REPORT_EVENT_BIZ BIZ WHERE 1=1 and BIZ.TASK_DEF_KEY='已完成'");
		if(StringUtils.isNotBlank(params.get("systemName"))){
			sql.append(" and BIZ.SYSTEM_NAME = ? ");
			list.add(params.get("systemName"));
		}
		if(StringUtils.isNotBlank(params.get("createTime")) && StringUtils.isNotBlank(params.get("createTime2"))){
			
			sql.append(" AND BIZ.CREATE_TIME BETWEEN ? AND ? ");
			list.add(params.get("createTime"));
			list.add(params.get("createTime2"));
		}
		sql.append(" GROUP BY SYSTEM_NAME  ORDER BY TOTAL DESC ");
		return this.findBySql(page,sql.toString(),list.toArray(),Map.class);
	}
	
	@Override
	public PageHelper<Map<String, Object>> bizHandleTimeCount(PageHelper<Map<String, Object>> page, Map<String, String> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT B.BIZ_ID AS BIZID,B.WORK_NUM AS WORKNUM,B.TASK_DEF_KEY AS STATUS,");
		sql.append(" CREATE_TIME CREATETIME,b.PROCESS_DEFINITION_ID as PROCESSDEFINITIONID,");
		sql.append(" SERVICE_SIGN SERVICESIGN,SUBSTRING(TIMEDIFF(SERVICE_SIGN, CREATE_TIME),1, 5) SIGN,");
		sql.append(" B.SERVICE_ASSIGN SERVICEASSIGN,SUBSTRING(TIMEDIFF(SERVICE_ASSIGN, SERVICE_SIGN),1, 5) ASSIGN,");
		sql.append(" B.SERVICE_HANDLE SERVICEHANDLE,SUBSTRING(TIMEDIFF(SERVICE_HANDLE, SERVICE_ASSIGN),1, 5) SHANDLE,");
		sql.append(" B.VENDER_SIGN VENDERSIGN,SUBSTRING(TIMEDIFF(VENDER_SIGN, SERVICE_ASSIGN),1, 5) VSIGN,");
		sql.append(" B.SECOND_VENDER_HANDLE SECONDHANLE,SUBSTRING(TIMEDIFF(SECOND_VENDER_HANDLE, VENDER_SIGN),1, 5) SEHANDLE,");
		sql.append(" B.THREE_VENDER_HANDLE THREEHANDLE,SUBSTRING(TIMEDIFF(THREE_VENDER_HANDLE, VENDER_SIGN),1, 5) THANDLE");
		sql.append(" FROM ESFLOW.REPORT_EVENT_BIZ B WHERE 1=1 ");
	
		if(StringUtils.isNotBlank(params.get("systemName"))){
			sql.append(" and B.SYSTEM_NAME = ? ");
			list.add(params.get("systemName"));
		}
		if(StringUtils.isNotBlank(params.get("createTime")) && StringUtils.isNotBlank(params.get("createTime2"))){
			
			sql.append(" AND B.CREATE_TIME BETWEEN ? AND ? ");
			list.add(params.get("createTime"));
			list.add(params.get("createTime2"));
		}
		sql.append("ORDER BY b.LIMIT_TIME DESC ");
		return this.findBySql(page,sql.toString(),list.toArray(),Map.class);
	}
	
	
}
