package com.activiti.core.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.activiti.core.common.dao.IBaseDao;
import com.activiti.core.common.utils.PageHelper;

public interface RoleDao extends IBaseDao<Map<String,Object>>{

	public List<Map<String,Object>> getRoleRootNodes(Map<String,String> params);

	public List<Map<String,Object>> getRootRole();

	/**
	 * 
	 * @param roles
	 * @return
	 */
	public PageHelper<Map<String, Object>> loadUsersByRole(PageHelper<Map<String, Object>> page,List<String> roles);

	/**
	 * 
	 * @param userName
	 * @return
	 */
	public Map<String, String> loadUsersByUserName(String userName);

	public List<Map<String, String>> findUsersByFullname(String userName);

	public Map<String, String> getThreeLevalManager(String username);

	public Map<String, String> findUserByUserName(String userName);

	public Map<String, String> findThreeLevalManager(Map<String, String> params);

	/**
	 * 获取部门信息
	 * @param deptmentId
	 * @return
	 */
	public Map<String, String> getDeptmet(String deptmentId);

	/**
	 * 根据params获取人员
	 * @param page
	 * @param params
	 * @return
	 */
	public void loadUserByParams( PageHelper<Map<String, Object>> page, Map<String, String> params);

	public Set<String> getUserRolesByUserName(String username);

	/**
	 * 人员查询  用户名  角色
	 * @param page
	 * @param params
	 * @return
	 */
	public PageHelper<Map<String, Object>> findUsers(PageHelper<Map<String, Object>> page, Map<String, String> params);
}
