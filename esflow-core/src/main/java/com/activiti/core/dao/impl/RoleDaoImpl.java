package com.activiti.core.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.activiti.core.dao.RoleDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.activiti.core.common.dao.BaseDaoImpl;
import com.activiti.core.common.utils.PageHelper;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<Map<String,Object>> implements RoleDao {

	public List<Map<String,Object>> getRoleRootNodes(Map<String,String> params) {
		
		StringBuilder sql = new StringBuilder(" SELECT ID AS ID ,PARENT_ID ");
		sql.append("AS PARENTID ,ROLECNNAME ,ROLENAME  FROM COMMON.SYS_ROLE ROLE WHERE 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if(StringUtils.isNotBlank(params.get("id"))){
			sql.append(" AND ROLE.ID = ?");
			list.add(params.get("id"));
		}
		if(StringUtils.isNotBlank(params.get("parentId"))){
			sql.append(" AND ROLE.PARENT_ID = ? ");
			list.add(params.get("parentId"));
		}
		if(StringUtils.isNotBlank(params.get("roleName"))){
			sql.append(" AND (ROLE.ROLECNNAME = ? OR ROLE.ROLENAME = ? )");
			list.add(params.get("roleName"));
		}
		return this.findBySql(sql.toString(),list.toArray(),Map.class);
	}
	
	public List<Map<String,Object>> findRoleRootNodes(Map<String,String> params) {
		
		StringBuilder sql = new StringBuilder(" SELECT ID AS ID ,PARENT_ID ");
		sql.append("AS PARENTID ,NAME_CN AS ROLECNNAME,NAME AS ROLENAME  FROM COMMON.SYS_AU_ROLE ROLE WHERE 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if(StringUtils.isNotBlank(params.get("id"))){
			sql.append(" AND ROLE.ID = ?");
			list.add(params.get("id"));
		}
		if(StringUtils.isNotBlank(params.get("parentId"))){
			sql.append(" AND ROLE.PARENT_ID = ? ");
			list.add(params.get("parentId"));
		}
		if(StringUtils.isNotBlank(params.get("roleName"))){
			sql.append(" AND (ROLE.NAME_CN = ? OR ROLE.NAME = ? )");
			list.add(params.get("roleName"));
		}
		return this.findBySql(sql.toString(),list.toArray(),Map.class);
	}

	public List<Map<String,Object>> getRootRole() {
	
		String sql = " SELECT ID AS ID ,PARENT_ID AS PARENTID ,ROLECNNAME ,ROLENAME  FROM COMMON.SYS_ROLE ROLE WHERE PARENT_ID IS NULL ";
		return this.findBySql(sql.toString(),Map.class);
	}
	
	@Override
	public PageHelper<Map<String,Object>> loadUsersByRole(PageHelper<Map<String, Object>> page,List<String> roles) {
		
		if (roles == null || roles.size() <= 0) {
			return null;
		}
		List<Object> list = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("SELECT U.USERNAME AS USERNAME,U.MOBILE AS MOBILE,U.FULLNAME AS FULLNAME, S.DESCRIPTION AS DESCRIPTION,");
		sb.append(" U.EMAIL AS EMAIL FROM COMMON.SYS_USER U LEFT JOIN COMMON.SYS_SUPERME_STRUCTURE S ON U.STRUCT_ID = S.ID WHERE ");
		sb.append(" U.ID IN (SELECT USER_ID FROM COMMON.SYS_USER_ROLE WHERE ROLE_ID IN (SELECT ID FROM COMMON.SYS_ROLE WHERE ROLECNNAME IN (");
		for (int i = 0; i < roles.size(); i++) {
			sb.append("?,");
			list.add(roles.get(i));
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append("))) ");
		sb.append(" UNION SELECT U.USERNAME AS USERNAME,U.MOBILE AS MOBILE,U.FULLNAME AS FULLNAME, S.DESCRIPTION AS DESCRIPTION,");
		sb.append(" U.EMAIL AS EMAIL FROM COMMON.SYS_SUPERME_USER U LEFT JOIN COMMON.SYS_SUPERME_STRUCTURE S ON U.STRUCTURE_ID = S.ID WHERE ");
		sb.append(" U.ID IN (SELECT USER_ID FROM COMMON.SYS_USER_ROLE WHERE ROLE_ID IN (SELECT ID FROM COMMON.SYS_ROLE WHERE ROLECNNAME IN (");
		for (int i = 0; i < roles.size(); i++) {
			sb.append("?,");
			list.add(roles.get(i));
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append("))) ORDER BY USERNAME ");
		return this.findBySql(page,sb.toString(), list.toArray(), Map.class);
	}
	
	
	@Override
	public PageHelper<Map<String,Object>> findUsers(PageHelper<Map<String, Object>> page,Map<String,String> params) {
		
		List<Object> list = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("SELECT U.USERNAME AS USERNAME,U.MOBILE AS MOBILE,U.FULLNAME AS FULLNAME, S.DESCRIPTION AS DESCRIPTION,");
		sb.append(" U.EMAIL AS EMAIL FROM COMMON.SYS_USER U LEFT JOIN COMMON.SYS_SUPERME_STRUCTURE S ON U.STRUCT_ID = S.ID WHERE ");
		sb.append(" U.ID IN (SELECT USER_ID FROM COMMON.SYS_USER_ROLE WHERE ROLE_ID IN (SELECT ID FROM COMMON.SYS_ROLE WHERE 1=1 ");
		if(StringUtils.isNotBlank(params.get("role"))){
			sb.append(" AND ROLECNNAME = ? ");
			list.add(params.get("role"));
		}
		sb.append(" ))");
		if(StringUtils.isNotBlank(params.get("fullname"))){
			sb.append(" AND U.FULLNAME LIKE ? or U.USERNAME LIKE ? ");
			list.add("%"+params.get("fullname")+"%");
			list.add("%"+params.get("fullname")+"%");
		}
		sb.append(" UNION SELECT U.USERNAME AS USERNAME,U.MOBILE AS MOBILE,U.FULLNAME AS FULLNAME, S.DESCRIPTION AS DESCRIPTION,");
		sb.append(" U.EMAIL AS EMAIL FROM COMMON.SYS_SUPERME_USER U LEFT JOIN COMMON.SYS_SUPERME_STRUCTURE S ON U.STRUCTURE_ID = S.ID WHERE ");
		sb.append(" U.ID IN (SELECT USER_ID FROM COMMON.SYS_USER_ROLE WHERE ROLE_ID IN (SELECT ID FROM COMMON.SYS_ROLE WHERE 1=1");
		if(StringUtils.isNotBlank(params.get("role"))){
			sb.append(" AND ROLECNNAME = ? ");
			list.add(params.get("role"));
		}
		sb.append(" ))");
		if(StringUtils.isNotBlank(params.get("fullname"))){
			sb.append(" AND U.FULLNAME LIKE ? or U.USERNAME LIKE ? ");
			list.add("%"+params.get("fullname")+"%");
			list.add("%"+params.get("fullname")+"%");
		}
		sb.append(" ORDER BY USERNAME ");
		return this.findBySql(page,sb.toString(), list.toArray(), Map.class);
	}
	
	public PageHelper<Map<String,Object>> findUsersByRole(PageHelper<Map<String, Object>> page,List<String> roles){
		
		List<Object> list = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer("SELECT A.USERNAME AS USERNAME,U.MOBILE AS MOBILE,U.NAME_CN AS FULLNAME,");
		sb.append(" S.NAME AS DESCRIPTION, U.EMAIL AS EMAIL FROM COMMON.SYS_BS_USER U JOIN COMMON.SYS_BS_ACCOUNT A ON U.ACCOUNT_ID=A.ID ");
		sb.append(" JOIN COMMON.SYS_BS_DEPARTMENT S ON U.DEPARTMENT_ID = S.ID WHERE A.ID IN (SELECT ACCOUNT_ID FROM COMMON.SYS_AU_ROLE_ACCOUNT_R ");
		sb.append("  WHERE ROLE_ID IN (SELECT ID FROM COMMON.SYS_AU_ROLE WHERE NAME_CN IN (");
		for (int i = 0; i < roles.size(); i++) {
			sb.append("?,");
			list.add(roles.get(i));
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append("))) ");
		return this.findBySql(page,sb.toString(), list.toArray(), Map.class);
	}
	
	@Override
	public Map<String,String> loadUsersByUserName(String userName){
		
		if (StringUtils.isBlank(userName)) 
			return new HashMap<String, String>();
		StringBuffer sb = new StringBuffer("SELECT U.USERNAME AS USERNAME,U.FULLNAME AS FULLNAME,S.STRUCTURE_NAME AS DEPT,S.DESCRIPTION,");
		sb.append(" U.MOBILE,EMAIL FROM COMMON.SYS_USER U LEFT JOIN COMMON.SYS_SUPERME_STRUCTURE S ON U.STRUCT_ID = S.ID WHERE U.USERNAME = ? ");
		sb.append(" UNION SELECT U.USERNAME AS USERNAME,U.FULLNAME AS FULLNAME,S.STRUCTURE_NAME AS DEPT,S.DESCRIPTION,U.MOBILE,	");
		sb.append(" EMAIL FROM	COMMON.SYS_SUPERME_USER U LEFT JOIN COMMON.SYS_SUPERME_STRUCTURE S ON U.STRUCTURE_ID = S.ID WHERE U.USERNAME = ?");
		List<Map<String,String>> list = this.findBySql(sb.toString(),new Object[]{userName,userName}, Map.class);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return new HashMap<String, String>();
	}
	
	@Override
	public Map<String,String> findUserByUserName(String userName){
		
		StringBuilder sql = new StringBuilder(" SELECT A.USERNAME AS USERNAME,U.MOBILE AS MOBILE,U.NAME_CN AS FULLNAME,s.PARENT_IDS as DEPTMENTIDS,");
		sql.append(" S.NAME AS DESCRIPTION,S.ID AS DEPTMENTID, U.EMAIL AS EMAIL FROM COMMON.SYS_BS_USER U JOIN COMMON.SYS_BS_ACCOUNT A ON");
		sql.append(" U.ACCOUNT_ID=A.ID JOIN COMMON.SYS_BS_DEPARTMENT S ON U.DEPARTMENT_ID = S.ID WHERE A.USERNAME = ? ");
		List<Map<String,String>> list = this.findBySql(sql.toString(),new Object[]{userName}, Map.class);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return new HashMap<String, String>();
	}
	
	@Override
	public List<Map<String,String>> findUsersByFullname(String fullname){
		
		StringBuffer sb = new StringBuffer("SELECT U.USERNAME AS USERNAME,U.FULLNAME AS FULLNAME,S.STRUCTURE_NAME AS DEPT,S.DESCRIPTION,");
		sb.append(" U.MOBILE,EMAIL FROM COMMON.SYS_USER U LEFT JOIN COMMON.SYS_SUPERME_STRUCTURE S ON U.STRUCT_ID = S.ID WHERE U.FULLNAME LIKE ? ");
		List<Map<String,String>> list = this.findBySql(sb.toString(),new Object[]{"%"+fullname+"%"}, Map.class);
		return list;
	}
	
	public List<Map<String,String>> findUsersByName(String fullname){
		
		StringBuilder sql = new StringBuilder(" SELECT A.USERNAME AS USERNAME,U.MOBILE AS MOBILE,U.NAME_CN AS FULLNAME,");
		sql.append(" S.NAME AS DESCRIPTION, U.EMAIL AS EMAIL FROM COMMON.SYS_BS_USER U JOIN COMMON.SYS_BS_ACCOUNT A ON");
		sql.append(" U.ACCOUNT_ID=A.ID JOIN COMMON.SYS_BS_DEPARTMENT S ON U.DEPARTMENT_ID = S.ID WHERE U.NAME_CN = ? ");
		return this.findBySql(sql.toString(),new Object[]{fullname}, Map.class);
	}
	
	@Override
	public Map<String,String> getThreeLevalManager(String username){
		
		StringBuilder sql = new StringBuilder(" SELECT USERNAME,FULLNAME FROM COMMON.SYS_USER U WHERE ");
		sql.append(" U.STRUCT_ID=(SELECT STRUCT_ID FROM COMMON.SYS_USER WHERE USERNAME = ? )");
		List<Map<String,String>> list = this.findBySql(sql.toString(),new Object[]{username}, Map.class);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return new HashMap<String, String>();
	}
	
	@Override
	public Map<String,String> findThreeLevalManager(Map<String,String> params){
		
		List<Object> args = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(" SELECT A.USERNAME AS USERNAME,U.MOBILE AS MOBILE,U.NAME_CN AS FULLNAME,U.USER_CODE, ");
		sql.append(" U.EMAIL AS EMAIL FROM COMMON.SYS_BS_USER U JOIN COMMON.SYS_BS_ACCOUNT A ON U.ACCOUNT_ID=A.ID  ");
		sql.append(" JOIN COMMON.SYS_BS_DEPARTMENT S ON U.DEPARTMENT_ID = S.ID WHERE (U.USER_CODE  like '3%' or U.USER_CODE like '2%') ");
		if(StringUtils.isNotBlank(params.get("deptmentId"))){
			sql.append(" AND U.DEPARTMENT_ID = ?");
			args.add(params.get("deptmentId"));
		}
		if(StringUtils.isNotBlank(params.get("parentId"))){
			sql.append(" AND S.PARENT_ID = ?");
			args.add(params.get("parentId"));
		}
		sql.append(" order by U.USER_CODE desc ");
		List<Map<String,String>> list = this.findBySql(sql.toString(),args.toArray(), Map.class);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return new HashMap<String, String>();
	}
	
	@Override
	public Map<String,String> getDeptmet(String deptmentId){
		
		String sql = " SELECT NAME,ID ,PARENT_ID AS PARENTID FROM COMMON.SYS_BS_DEPARTMENT WHERE ID = ? ";
		List<Map<String,String>> list = this.findBySql(sql,new Object[]{deptmentId},Map.class);
		if(list != null && !list.isEmpty())
			return list.get(0);
		return new HashMap<String, String>();
	}

	@Override
	public void loadUserByParams(
			PageHelper<Map<String, Object>> page, Map<String, String> params) {
		StringBuffer sqlbuff = new StringBuffer(" SELECT U.ID,U.EMAIL,U.MOBILE,U.NAME_CN,A.USERNAME FROM COMMON.SYS_BS_USER U  ");
		sqlbuff.append(" INNER JOIN COMMON.SYS_BS_ACCOUNT A ON A.ID = U.ACCOUNT_ID ");
		List<Object> parameterList = new ArrayList<Object>();
		if(StringUtils.isNotBlank(params.get("departmentName"))){
			sqlbuff.append(" INNER JOIN ( SELECT * FROM COMMON.SYS_BS_DEPARTMENT WHERE NAME_CN NOT LIKE '%分公司%' AND NAME = ? ");
			sqlbuff.append("UNION SELECT * FROM COMMON.SYS_BS_DEPARTMENT WHERE PARENT_ID IN (SELECT ID FROM COMMON.SYS_BS_DEPARTMENT WHERE NAME_CN NOT LIKE '%分公司%' AND NAME = ?)) D ON D.ID = U.DEPARTMENT_ID ");
			parameterList.add(params.get("departmentName"));
			parameterList.add(params.get("departmentName"));
		}
		if(StringUtils.isNotBlank(params.get("departmentId"))){
			sqlbuff.append(" INNER JOIN ( SELECT * FROM COMMON.SYS_BS_DEPARTMENT WHERE ID = ? ");
			sqlbuff.append("UNION SELECT * FROM COMMON.SYS_BS_DEPARTMENT WHERE PARENT_ID IN (SELECT ID FROM COMMON.SYS_BS_DEPARTMENT WHERE ID = ?)) D ON D.ID = U.DEPARTMENT_ID ");
			parameterList.add(params.get("departmentId"));
			parameterList.add(params.get("departmentId"));
		}
		if(StringUtils.isNotBlank(params.get("roleName"))){
			sqlbuff.append(" INNER JOIN COMMON.SYS_AU_ROLE_ACCOUNT_R RA ON RA.ACCOUNT_ID = U.ACCOUNT_ID");
			sqlbuff.append(" INNER JOIN COMMON.SYS_AU_ROLE R ON RA.ROLE_ID = R.ID AND R.NAME_CN = ? ");
			parameterList.add(params.get("roleName"));
		}
		if(StringUtils.isNotBlank(params.get("systemName"))){
			sqlbuff.append(" INNER JOIN ESFLOW.BIZ_SYSTEM_USER_CONF S ON S.USER_NAME = A.USERNAME AND S.USER_TYPE='needInterfaceMan' AND S.SYSTEM_NAME = ?");
			parameterList.add(params.get("systemName"));
		}
		this.findBySql(page, sqlbuff.toString(), parameterList.toArray(),Map.class);
	}
	
	@Override
	public Set<String> getUserRolesByUserName(String username){
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if(StringUtils.isNotBlank(username)){
			StringBuffer sql = new StringBuffer(" SELECT R.ROLECNNAME AS NAME FROM COMMON.SYS_ROLE R WHERE");
			sql.append(" ID IN ( SELECT UR.ROLE_ID FROM COMMON.SYS_USER U ");
			sql.append(" JOIN COMMON.SYS_USER_ROLE UR ON U.ID = UR.USER_ID WHERE USERNAME = ? )");
			list =  this.findBySql(sql.toString(),new Object[]{username},Map.class);
		}
		return this.listToSet(list);
	}
	
	private Set<String> listToSet (List<Map<String,String>> list){
		
		Set<String> set = new HashSet<String>();
		if(list != null && !list.isEmpty()){
			for (Map<String,String> map : list) {
				set.add(map.get("NAME"));
			}
		}
		return set;
	}
}
