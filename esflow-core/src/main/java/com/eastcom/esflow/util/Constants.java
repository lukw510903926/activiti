package com.eastcom.esflow.util;

public class Constants {
	
	/**
	 * 权限前缀标识
	 */
	public final static String BIZ_TASKASSIGNEE = "GROUP:,";
	
	/**
	 * 签收标识
	 */
	public final static String SIGN = "SIGN";
	/**
	 * 延期标识
	 */
	public final static String DELAY_TIME = "DELAYTIME";
	
	/**
	 * 是否延期
	 */
	public final static String IS_ASSIGNSUBBIZ = "IS_ASSIGNSUBBIZ";
	/**
	 * 延期不通过
	 */
	public final static String DELAY_NOT_PASS = "DELAYNOTPASS";
	
	/**
	 * 处理标识
	 */
	public final static String HANDLE = "HANDLE";
	/**
	 * 转派
	 */
	public final static String TURNSEND = "转派";
	
	
	//============== 流程参数相关 =============
	/**
	 * 工单创建人
	 */
	public final static String SYS_BIZ_CREATEUSER = "SYS_BIZ_CREATEUSER";
	/**
	 * 工单ID
	 */
	public final static String SYS_BIZ_ID = "SYS_BIZ_ID";
	
	//=====================变更流程操作类型===========================
	
	public final static String  OPERATE_ADD= "新增";
	
	public final static String OPERATE_UPDATE = "更新";
	
	public final static String OPERATE_DELETE = "删除";
	
	public final static String OPERATE_TEMP = "tempType";
	
	//=====================工单状态===========================
	
	//草稿
	public final static String BIZ_TEMP = "草稿";
	//新建
	public final static String BIZ_NEW = "新建";
	
	public final static String BIZ_HANDLE = "处理中";
	//结束
	public final static String BIZ_END = "已完成";
	
	//申请人处理
	public final static String BIZ_CREATEUSER = "申请人处理";
	//删除
	public final static String BIZ_DELETE = "已删除";
	//拦截
	public final static String BIZ_INTERCEPT ="服务台拦截";
	
	//=====================会签结果类型===========================

	//通过
	public final static int MEET_YES = 1;
	//驳回
	public final static int MEET_NO = 0;
	
//	==================工单来源===============================
	// 新平台
	public final static String NEW_PLATFORM= "运维管理";
	//旧平台
	public final static String OLD_PLATFORM = "12583";
}
