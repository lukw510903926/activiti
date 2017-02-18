package com.activiti.core.dao;

import java.util.List;
import java.util.Map;

import com.activiti.core.common.dao.IBaseDao;
import com.activiti.core.common.utils.PageHelper;
import com.activiti.core.bean.BizInfo;

public interface IBizInfoDao extends IBaseDao<BizInfo> {
	/**
	 * 返回唯一的用户，如果角色对应的用户不止一个，则返回null
	 * @param roles
	 * @return
	 * @
	 */
	public String loadOnlyUser(List<String> roles);

	/**
	 * 加载用户及用户组<br>
	 * 如果parentID为空，则加载父及为空的组，否则加载该组下面的所有组和用户
	 * @return
	 */
	public List<Map<String, Object>> loadUserGroup(String parentID) ;

	/**
	 * 包含配置信息 taskId 
	 * @param id
	 * @return
	 */
	public BizInfo getByBizId(String id);
	
	public PageHelper<BizInfo> queryWorkOrder(Map<String, Object> params, PageHelper<BizInfo> page);

	/**
	 * 查询多个角色下的所有用户
	 * @param roles
	 * @return
	 * @
	 */
	public List<String> loadAllUsers(List<String> roles) ;

	/**
	 * 
	 * @param structId
	 * @return
	 * @
	 */
	public String loadStructCity(String structId);

	/**
	 * 批量查询工单
	 * @param list
	 * @return
	 */
	public List<BizInfo> getBizInfos(List<String> list);

	/**
	 * 根据父单查询子单
	 * @param parentId
	 * @return
	 */
	public List<BizInfo> getBizByParentId(String parentId);

	public BizInfo getBizInfo(String id,String loginUser);

}
