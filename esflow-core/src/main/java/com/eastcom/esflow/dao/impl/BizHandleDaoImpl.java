package com.eastcom.esflow.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eastcom.esflow.common.dao.BaseDaoImpl;
import com.eastcom.esflow.common.utils.PageHelper;
import com.eastcom.esflow.dao.IBizHandleDao;

@Repository
public class BizHandleDaoImpl extends BaseDaoImpl<Map<String,Object>> implements IBizHandleDao{

	@Override
	public PageHelper<Map<String, Object>> findMember(PageHelper<Map<String, Object>> page, Map<String, Object> params) {
		
		StringBuffer sql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		sql.append(" SELECT T.ID,T.USERNAME,T.FULLNAME,T.MOBILE,T.EMAIL,T2.STRUCTURE_NAME FROM COMMON.SYS_SUPERME_STRUCTURE T2");
		sql.append(" JOIN COMMON.SYS_SUPERME_USER T ON (T.STRUCTURE_ID = T2.ID) WHERE 1 = 1 ");
		
		String deptment = (String)params.get("deptment");
		if(StringUtils.isNotBlank((String)params.get("sector"))){
			sql.append(" AND T2.ID = ?");
			list.add((String)params.get("sector"));
		}
		if(StringUtils.isNotBlank((String)params.get("portalname"))){
			sql.append(" AND T.USERNAME LIKE ? ");
			list.add("%"+(String)params.get("portalname")+"%");
		}
		if(StringUtils.isNotBlank((String)params.get("cnname"))){
			sql.append(" AND T.FULLNAME LIKE ? ");
			list.add("%"+(String)params.get("cnname")+"%");
		}
		if(StringUtils.isNotBlank(deptment)){
			String[] depts = deptment.split(",");
			if(depts.length==1){
				sql.append(" AND T2.STRUCTURE_NAME = ? ");
				list.add(deptment);
			}else{
				sql.append(" AND t2.STRUCTURE_NAME in ( ");
				for (String dept : depts) {
					sql.append(" ? ,");
					list.add(dept);
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(" ) ");
			}
		}
		sql.append(" UNION SELECT T.ID,T.USERNAME,T.FULLNAME,T.MOBILE,T.EMAIL,T2.STRUCTURE_NAME FROM COMMON.SYS_SUPERME_STRUCTURE");
		sql.append(" T2 JOIN COMMON.SYS_USER T ON (T.STRUCT_ID = T2.ID) WHERE 1 = 1");
		
		if(StringUtils.isNotBlank((String)params.get("sector"))){
			sql.append(" AND T2.ID = ?");
			list.add((String)params.get("sector"));
		}
		if(StringUtils.isNotBlank((String)params.get("portalname"))){
			sql.append(" AND T.USERNAME LIKE ? ");
			list.add("%"+(String)params.get("portalname")+"%");
		}
		if(StringUtils.isNotBlank((String)params.get("cnname"))){
			sql.append(" AND T.FULLNAME LIKE ?  ");
			list.add("%"+(String)params.get("cnname")+"%");
		}
		if(StringUtils.isNotBlank(deptment)){
			String[] depts = deptment.split(",");
			if(depts.length==1){
				sql.append(" AND T2.STRUCTURE_NAME = ? ");
				list.add(deptment);
			}else{
				sql.append(" AND T2.STRUCTURE_NAME in ( ");
				for (String dept : depts) {
					sql.append(" ? ,");
					list.add(dept);
				}
				sql.deleteCharAt(sql.length()-1);
				sql.append(" ) ");
			}
		}
		sql.append(" ORDER BY USERNAME ");
		return this.findBySql(page,sql.toString(),list.toArray(), Map.class);
	}
	
	public PageHelper<Map<String, Object>> findUsers(PageHelper<Map<String, Object>> page, Map<String, Object> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(" SELECT A.ID AS ID,A.USERNAME AS USERNAME,U.MOBILE AS MOBILE,U.NAME_CN AS FULLNAME,");
		sql.append(" S.NAME AS STRUCTURE_NAME, U.EMAIL AS EMAIL FROM COMMON.SYS_BS_USER U JOIN COMMON.SYS_BS_ACCOUNT ");
		sql.append(" A ON U.ACCOUNT_ID=A.ID JOIN COMMON.SYS_BS_DEPARTMENT S ON U.DEPARTMENT_ID = S.ID WHERE 1=1");
		if(StringUtils.isNotBlank((String)params.get("sector"))){
			sql.append(" AND S.ID = ?");
			list.add((String)params.get("sector"));
		}
		if(params.containsKey("headType")){//TODO  只查询总部下员工
			sql.append(" and S.NAME_CN NOT LIKE '%分公司%'");
		}
		if(StringUtils.isNotBlank((String)params.get("cnname"))){
			sql.append(" AND ( U.NAME_CN LIKE ?  or a.USERNAME like ? ) ");
			list.add("%"+(String)params.get("cnname")+"%");
			list.add("%"+(String)params.get("cnname")+"%");
		}
		String deptment = (String)params.get("deptment");
		if(StringUtils.isNotBlank(deptment)){
			sql.append(" AND S.NAME = ? ");
			list.add(deptment);
		}
		sql.append(" ORDER BY USERNAME ");
		return this.findBySql(page,sql.toString(),list.toArray(), Map.class);
	}

	@Override
	public List<Map<String, Object>> findSector(Map<String, Object> params) {
		
		List<Object> list = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.ID,T.PARENT_ID PID,T.STRUCTURE_NAME NAME FROM COMMON.SYS_SUPERME_STRUCTURE T WHERE 1=1 AND FLAG=1 AND ISDELETED = 0 AND SYSFLAG !=0 ORDER BY DEPTLEVEL ");
		if(params != null){
			for(String key:params.keySet()){
				sql.append(" and "+key+" = ?");
				list.add(params.get(key));
			}
		}
		return this.findBySql(sql.toString(),list.toArray(),Map.class);
	}
	
	public List<Map<String, Object>> findDeptments(Map<String, Object> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT NAME AS NAME ,ID AS ID ,PARENT_ID AS PID FROM COMMON.SYS_BS_DEPARTMENT WHERE ENABLED ='1' ");
		if(params != null){
			for(String key:params.keySet()){
				if(key.equals("company")){
					sql.append(" and NOT NAME_CN LIKE ?");
					list.add("%"+params.get(key)+"%");
				}else{
					sql.append(" and "+key+" = ?");
					list.add(params.get(key));
				}
			}
		}
		sql.append(" ORDER BY LEVEL_");
		return this.findBySql(sql.toString(),list.toArray(),Map.class);
	}
}
