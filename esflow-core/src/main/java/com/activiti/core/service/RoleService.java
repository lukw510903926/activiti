package com.activiti.core.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.activiti.core.common.service.IBaseService;
import com.activiti.core.common.utils.PageHelper;

public interface RoleService extends IBaseService<Map<String,Object>>{

	public List<Map<String,Object>> getRoleRootNodes(Map<String,String> params) ;

	public List<Map<String,Object>> getRootRole()  ;
	
	public PageHelper<Map<String, Object>> loadUsersByRole(PageHelper<Map<String, Object>> page,List<String> roles);
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public Map<String, String> loadUsersByUserName(String userName);

	public List<Map<String, String>> findUsersByFullname(String fullname);

	/**
	 * 获取指定人员的三级经理
	 * @param username
	 * @return
	 */
	public Map<String, String> getThreeLevalManager(String username);

	/**
	 * 根据params获取人员
	 * @param page
	 * @param params
	 */
	public void loadUserByParams(PageHelper<Map<String, Object>> page,Map<String, String> params);
	
	public Set<String> getUserRolesByUserName(String username);
	
	/**
	 * 带缓存 根据用户名查询用户账号查询中文名
	 * @param taskAssignee
	 * @param userCache
	 * @return
	 */
	public String getFullNameByUserName(String taskAssignee,Map<String, Map<String, String>> userCache);
	
	
	/**
	 * 人员查询  用户名  角色
	 * @param page
	 * @param params
	 * @return
	 */
	public PageHelper<Map<String, Object>> findUsers(PageHelper<Map<String, Object>> page, Map<String, String> params);
}
