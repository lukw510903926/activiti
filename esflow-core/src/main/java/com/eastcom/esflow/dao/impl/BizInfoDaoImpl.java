package com.eastcom.esflow.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eastcom.esflow.bean.BizInfo;
import com.eastcom.esflow.common.dao.BaseDaoImpl;
import com.eastcom.esflow.common.utils.LoginUser;
import com.eastcom.esflow.common.utils.PageHelper;
import com.eastcom.esflow.dao.BizInfoConfDao;
import com.eastcom.esflow.dao.IBizInfoDao;
import com.eastcom.esflow.dao.RoleDao;
import com.eastcom.esflow.util.Constants;
import com.eastcom.esflow.util.WebUtil;

@Repository
public class BizInfoDaoImpl extends BaseDaoImpl<BizInfo> implements IBizInfoDao {
	
	@Autowired
	private BizInfoConfDao bizInfoConfDao;
	
	@Autowired
	private RoleDao roleDao;
	
	private Logger logger = Logger.getLogger("bizInfoDaoImpl");
	
	private static Map<String,String> orderMap = new HashMap<String, String>();
	
	@Override
	public List<BizInfo> getBizByParentId(String parentId){
		
		if(StringUtils.isBlank(parentId))
			return null;
		StringBuilder hql = new StringBuilder(" from BizInfo a where a.parentId = ? ");
		return this.find(hql.toString(), new Object[]{parentId});
	}
	
	@Override
	public String loadOnlyUser(List<String> roles) {
		
		if (roles == null || roles.size() <= 0) {
			return null;
		}
		List<Object> args = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT username AS USERNAME FROM COMMON.sys_user WHERE id IN (SELECT USER_ID FROM COMMON.sys_user_role WHERE ROLE_ID IN (SELECT ID FROM COMMON.sys_role WHERE rolecnname in (");
		for (int i = 0; i < roles.size(); i++) {
			sb.append("?,");
			args.add(roles.get(i));
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append("))) ");
		sb.append(" union SELECT username AS USERNAME FROM COMMON.sys_superme_user WHERE id IN (SELECT USER_ID FROM COMMON.sys_user_role WHERE ROLE_ID IN (SELECT ID FROM COMMON.sys_role WHERE rolecnname in (");
		for (int i = 0; i < roles.size(); i++) {
			sb.append("?,");
			args.add(roles.get(i));
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append("))) ");
		List<Map<String, Object>> list = super.findBySql(sb.toString(), args.toArray(), Map.class);
		if (list == null || list.size() != 1) {
			return null;
		}
		Map<String, Object> entity = list.get(0);
		return (String) entity.get("USERNAME");
	}
	
	@Override
	public String loadStructCity(String structId){
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if (StringUtils.isNotBlank(structId)) {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT DESCRIPTION FROM COMMON.SYS_SUPERME_STRUCTURE WHERE ID = ? ");
			list = this.findBySql(sb.toString(), new Object[]{structId}, Map.class);
		}
		String city  = null;
		if (list != null && list.size() == 1) {
			Map<String, Object> entity = list.get(0);
			String description = (String)entity.get("DESCRIPTION");
			if(StringUtils.isNotBlank(description)){
				String[] strings = description.split("\\.");
				city = strings[0];
			}
		}
		return  city;
	}
	
	@Override
	public List<String> loadAllUsers(List<String> roles) {
		
		if (roles == null || roles.size() <= 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		sb.append("SELECT username AS USERNAME FROM COMMON.sys_user WHERE id IN (SELECT USER_ID FROM COMMON.sys_user_role WHERE ROLE_ID IN (SELECT ID FROM COMMON.sys_role WHERE rolecnname in (");
		for (int i = 0; i < roles.size(); i++) {
			sb.append("?,");
			args.add(roles.get(i));
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append("))) ");
		sb.append("union SELECT username AS USERNAME FROM COMMON.sys_superme_user WHERE id IN (SELECT USER_ID FROM COMMON.sys_user_role WHERE ROLE_ID IN (SELECT ID FROM COMMON.sys_role WHERE rolecnname in (");
		for (int i = 0; i < roles.size(); i++) {
			sb.append("?,");
			args.add(roles.get(i));
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append("))) ");
		List<Map<String, Object>> list = super.findBySql(sb.toString(), args.toArray(), Map.class);
		if (list == null ) {
			return null;
		}
		List<String> userNameList = new ArrayList<String>();
		for(Map<String, Object> map : list){
			String userName = (String) map.get("USERNAME");
			if(StringUtils.isNotBlank(userName))
				userNameList.add(userName);
		}
		return userNameList;
	}
	
	@Override
	public List<Map<String, Object>> loadUserGroup(String parentID) {
		parentID = StringUtils.isEmpty(parentID) ? null : parentID;
		if (parentID == null) {
			// 查询所有的根角色
			String sql = "SELECT id,rolename as name,rolecnname AS nameCN,'role' as type,'closed' as state FROM common.sys_role WHERE parent_id IS NULL";
			return super.findBySql(sql, Map.class);
		} else {
			// 先查询该角色下的角色
			String sql = "SELECT id,rolename as name,rolecnname AS nameCN,'role' as type,'closed' as state FROM common.sys_role WHERE parent_id =?";
			List<Map<String, Object>> result = super.findBySql(sql, new Object[] { parentID }, Map.class);
			result = result == null ? new ArrayList<Map<String, Object>>() : result;
			// 再查询该角色小的用户
			sql = "SELECT id,username as name,fullname as nameCN,'user' as type FROM COMMON.sys_user WHERE id IN (SELECT USER_ID FROM COMMON.sys_user_role WHERE ROLE_ID=?)";
			List<Map<String, Object>> result2 = super.findBySql(sql, new Object[] { parentID }, Map.class);
			if (result2 != null)
				result.addAll(result2);
			return result;
		}
	}

	@Override
	public BizInfo getBizInfo(String id,String loginUser){
		
		StringBuilder sql = this.buildSql();
		List<Object> list = new ArrayList<Object>();
		String  taskAssignee = this.bizInfoConfDao.getTaskAssignee(id);
		sql.append(" left join (select c.biz_id as bizId,group_concat(c.task_assignee) as task_Assignee,");
		sql.append(" group_concat(c.task_id) as task_Id	from esflow.act_biz_info_conf c where c.biz_id = ? ");
		list.add(id);
		if(StringUtils.isNotBlank(taskAssignee) && StringUtils.isNotBlank(loginUser)){
			sql.append(" and (c.task_assignee = ? ");
			list.add(loginUser);
			Set<String> roles = roleDao.getUserRolesByUserName(loginUser);
			for (String s : roles) {
				sql.append("or c.task_assignee like ? ");
				list.add("%," + s + ",%");
			}
			sql.append(" )");
		}
		sql.append(" group by c.biz_id limit 0,1 ) conf on bean.id = conf.bizId where 1 = 1 and bean.id = ?");
		list.add(id);
		List<BizInfo> result = this.findBySql(sql.toString(),list.toArray(),BizInfo.class);
		return result.size() > 0 ? result.get(0) : null;
	}
	@Override
	public BizInfo getByBizId(String bizId){
		
		String hql = " from  BizInfo where id = ? or bizId = ? ";
		List<BizInfo> list = this.find(hql,new Object[]{bizId,bizId});
		if(list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}
	
	@Override
	public List<BizInfo> getBizInfos(List<String> list){
		
		StringBuilder hql  = new StringBuilder("FROM BizInfo bean WHERE bean.bizId in (");
		List<String> params = new ArrayList<String>();
		for(String id : list){
			hql.append(" ? ,");
			params.add(id);
		}
		hql = hql.deleteCharAt(hql.length() - 1);
		hql.append(")");
		return this.find(hql.toString(), params.toArray());
	}

	@Override
	public PageHelper<BizInfo> queryWorkOrder(Map<String, Object> params, PageHelper<BizInfo> page) {
		
		StringBuilder sql = this.buildSql();
		String loginUser = this.getLoginUser(params);
		List<Object> args = new ArrayList<Object>();
		sql.append(" join (select c.biz_id as bizId,group_concat(c.task_assignee) as task_Assignee,");
		sql.append(" group_concat(c.task_id) as task_Id from esflow.act_biz_info_conf c where 1=1 ");
		String taskAssignee = (String) params.get("taskAssignee");
		if (params.containsKey("checkAssignee")) {
			sql.append(" and ( c.task_assignee = ? ");
			args.add(loginUser);
			Set<String> roles = roleDao.getUserRolesByUserName(loginUser);
			for (String s : roles) {
				sql.append("or c.task_assignee like ? ");
				args.add("%," + s + ",%");
			}
			sql.append(")");
		}
		if(StringUtils.isNotEmpty(taskAssignee)){
			List<Map<String,String>> list = this.roleDao.findUsersByFullname(taskAssignee);
			if(list!=null && !list.isEmpty()){
				sql.append(" and  c.task_assignee in (");
				for (Map<String,String> map : list) {
					String username = (String)map.get("USERNAME");
					if(StringUtils.isNotBlank(username)){
						sql.append("?,");
						args.add(username);
					}
				}
				sql = sql.deleteCharAt(sql.length() - 1).append(")");
			}else{
				sql.append(" and c.task_assignee like ? ");
				args.add("%"+taskAssignee+"%");
			}
		}
		sql.append(" group by c.biz_id) conf on bean.id = conf.bizId left join (select i.biz_id, i.value");
	    sql.append(" from esflow.act_biz_process_instance i join esflow.act_biz_process_variable v ");
	    sql.append(" on i.process_variable_id = v.id where v. name = 'systemName') iu on bean.id = iu.biz_id ");
		sql.append(" where 1 = 1 and bean.biz_status <> ? ");
		args.add(Constants.BIZ_DELETE);
		String bizId = (String) params.get("bizId");
		if (StringUtils.isNotEmpty(bizId)) {
			sql.append(" and bean.biz_Id like ?");
			args.add("%"+bizId+"%");
		}
		String title = (String) params.get("title");
		if (StringUtils.isNotEmpty(title)) {
			sql.append(" and bean.title like ?");
			args.add("%" + title + "%");
		}
		String createUser = (String) params.get("createUser");
		if (StringUtils.isNotEmpty(createUser)) {
			List<Map<String,String>> list = this.roleDao.findUsersByFullname(createUser);
			if(list!=null && !list.isEmpty()){
				sql.append(" and bean.create_user in (");
				for (Map<String,String> map : list) {
					String username = (String)map.get("USERNAME");
					if(StringUtils.isNotBlank(username)){
						sql.append("?,");
						args.add(username);
					}
				}
				sql = sql.deleteCharAt(sql.length() - 1).append(")");
			}else{
				sql.append(" and bean.create_user like ? ");
				args.add("%"+createUser+"%");
			}
		}	
		String parentId = (String) params.get("parentId");
		if (StringUtils.isNotEmpty(parentId)) {
			sql.append(" and bean.parent_id = ?");
			args.add(parentId);
		}
		String isShuffle = (String) params.get("isShuffle");
		if (StringUtils.isNotEmpty(isShuffle)) {
			sql.append(" and bean.isShuffle = ?");
			args.add(isShuffle);
		}
		String parentTaskName = (String)params.get("parentTaskName");
		if (StringUtils.isNotEmpty(parentTaskName)) {
			sql.append(" and bean.parent_taskname = ?");
			args.add(parentTaskName);
		}
		String bizType = (String) params.get("bizType");
		if (StringUtils.isNotEmpty(bizType)) {
			sql.append(" and bean.biz_type = ?");
			args.add(bizType);
		}
		String status =  (String)params.get("status");
		if (StringUtils.isNotEmpty(status)) {
			sql.append(" and bean.biz_status = ? ");
			args.add(status);
		}
		String action = (String)params.get("action");
		if ("myHandle".equalsIgnoreCase(action)) {
			sql.append(" and bean.id in (");
			sql.append("select distinct bean.BIZ_ID FROM act_biz_log bean WHERE bean.handle_user =? )");
			args.add(loginUser);
		}
		if("myIntercept".equalsIgnoreCase(action)){
			sql.append(" and bean.biz_status =? ");
			args.add(Constants.BIZ_INTERCEPT);
		}
		if("itAction".equalsIgnoreCase(action)){
			sql.append(" and (bean.biz_type =?  or bean.biz_type = ?) ");
			args.add("IT超市申请管理");
			args.add("IT超市回收流程");
		}
		if("myWork".equalsIgnoreCase(action)){
			sql.append(" and (bean.biz_status <> ? and bean.biz_status <> ? )");
			args.add(Constants.BIZ_TEMP);
			args.add(Constants.BIZ_INTERCEPT);
		}
		if("myTemp".equalsIgnoreCase(action)||"myCreate".equalsIgnoreCase(action)){
			sql.append(" and bean.create_user = ? ");
			args.add(loginUser);
		}
		String taskDefKey = (String)params.get("taskDefKey");
		if (StringUtils.isNotEmpty(taskDefKey)) {
			sql.append(" and bean.task_def_key = ?");
			args.add(taskDefKey);
		}
		if("myEventService".equalsIgnoreCase(action)){
			sql.append(" and (bean.task_def_key = ? or bean.task_def_key = ?)");
			args.add("服务台处理");
			args.add("服务台关闭");
		}
		if (params.get("createTime")!=null && params.get("createTime2")!=null) {
			if(StringUtils.isNotBlank(params.get("createTime").toString())&&StringUtils.isNotBlank(params.get("createTime2").toString())){
				sql.append(" and bean.create_time between ? and ? ");
				args.add(params.get("createTime"));
				args.add(params.get("createTime2"));
			}
		}
		String systemName = (String)params.get("systemName");
		if(StringUtils.isNotBlank(systemName)){
			sql.append(" and iu.value = ? ");
			args.add(systemName);
		}
		String order = StringUtils.isBlank(page.getOrder()) ? "DESC" : page.getOrder();
		String sort = params.get("sort")==null ?  page.getSort() : (String)params.get("sort");
		if(StringUtils.isNotBlank(sort)){
			sql.append(" order by "+orderMap.get(page.getSort())+" ");
			if(StringUtils.isNotBlank(order))
				sql.append(order);
		}else{
			String event = "myEventQA,myEventHandle,myEventService,myEventService";
			if(StringUtils.isNotBlank(action) && event.indexOf(action)!=-1){// 
				sql.append(" order by bean.limit_time desc");
			}else{
				sql.append(" order by bean.create_time desc");
			}
		}
		logger.info("args : " + args);
		return this.findBySql(page, sql.toString(), args.toArray(),BizInfo.class);
	}

	private String getLoginUser(Map<String, Object> params) {
		
		LoginUser user = WebUtil.getLoginUser();
		String loginUser = user == null ? (String)params.get("loginUser") : user.getUsername();
		return loginUser;
	}
	
	private StringBuilder buildSql(){
		
		StringBuilder sql = new StringBuilder(" select distinct bean.id as id,bean.biz_Id ,bean.title as title,");
		sql.append(" bean.biz_Type,bean.process_Definition_Id,bean.process_Instance_Id,conf.task_Id,conf.task_Assignee,");
		sql.append(" bean.task_Name,bean.task_Def_Key,bean.press_Count,bean.create_User,bean.limit_Time,bean.create_Time,");
		sql.append(" bean.isshuffle,bean.biz_status,bean.source,bean.parent_Id,bean.parent_TaskName from esflow.act_biz_info bean ");
		return sql;
	}
	
}
