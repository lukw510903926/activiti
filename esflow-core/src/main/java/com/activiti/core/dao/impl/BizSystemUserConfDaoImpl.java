package com.activiti.core.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.activiti.core.bean.BizSystemUserConf;
import com.activiti.core.common.dao.BaseDaoImpl;
import com.activiti.core.common.utils.PageHelper;
import com.activiti.core.dao.BizSystemUserConfDao;

@Repository
public class BizSystemUserConfDaoImpl extends BaseDaoImpl<BizSystemUserConf> implements BizSystemUserConfDao{

	
	public PageHelper<Map<String,String>> findBizSystemUserConf(PageHelper<Map<String,String>>  page,Map<String,String> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(" SELECT CONF.USER_NAME USERNAME,CONF.USER_TYPE AS USERTYPE,CONF.CREATE_TIME AS CREATETIME, ");
		sql.append(" CONF.ID AS ID,CONF.SYSTEM_NAME AS SYSTEMNAME, U.NAME_CN AS FULLNAME,");
		sql.append(" CONF.BIZ_TYPE AS BIZTYPE FROM ESFLOW.BIZ_SYSTEM_USER_CONF CONF INNER JOIN COMMON.SYS_BS_ACCOUNT A ON CONF.USER_NAME = A.USERNAME ");
		sql.append(" INNER JOIN COMMON.SYS_BS_USER U ON U.ACCOUNT_ID = A.ID");
		if(StringUtils.isNotBlank(params.get("userType"))){
			sql.append(" AND CONF.USER_TYPE = ? ");
			list.add(params.get("userType"));
		}
		if(StringUtils.isNotBlank(params.get("systemName"))){
			sql.append(" AND CONF.SYSTEM_NAME = ? ");
			list.add(params.get("systemName"));
		}
		if(StringUtils.isNotBlank(params.get("userName"))){
			sql.append(" AND CONF.USER_NAME = ? ");
			list.add(params.get("userName"));
		}
		if(StringUtils.isNotBlank(params.get("bizType"))){
			sql.append(" AND CONF.BIZ_TYPE = ? ");
			list.add(params.get("bizType"));
		}
		if(StringUtils.isNotBlank(params.get("username"))){
			sql.append(" and (u.NAME_CN like ? ) ");
			list.add("%"+params.get("username")+"%");
		}
		sql.append(" ORDER BY  CONF.SYSTEM_NAME DESC, CONF.CREATE_TIME DESC");
		return this.findBySql(page,sql.toString(),list.toArray(),Map.class);
	}
}
