package com.activiti.core.service;

import java.util.List;
import java.util.Map;

import com.activiti.core.common.service.IBaseService;
import com.activiti.core.common.utils.PageHelper;
import com.activiti.core.bean.BizInfo;

/**
 * 工单信息业务<br>
 */
public interface IBizInfoService extends IBaseService<BizInfo> {

	/**
	 * 返回唯一的用户，如果角色对应的用户不止一个，则返回null
	 * @param roles
	 * @return
	 */
	public String loadOnlyUser(List<String> roles);
	
	/**
	 * 查询角色下所有用户
	 * @param roles
	 * @return
	 * 
	 */
	public List<String> loadAllUsers(List<String> roles);

	/**
	 * 加载用户及用户组<br>
	 * 如果parentID为空，则加载父及为空的组，否则加载该组下面的所有组和用户
	 * @return
	 * 
	 */
	public List<Map<String, Object>> loadUserGroup(String parentID);

	/**
	 * 添加工单信息
	 * @param beans
	 * 
	 */
	public void addBizInfo(BizInfo... beans);

	/**
	 * 更新工单系信息
	 * @param beans
	 * 
	 */
	public void updateBizInfo(BizInfo... beans);
	
	/**
	 * 复制工单
	 * @param bizId
	 * @param processInstanceId
	 * @param variables
	 * @return
	 * 
	 */
	public BizInfo copyBizInfo(String bizId, String processInstanceId, Map<String, Object> variables);
	/**
	 * 删除工单信息
	 * @param beans
	 * 
	 */
	public void deleteBizInfo(BizInfo... beans);

	/**
	 * 删除工单信息
	 * @param ids
	 * 
	 */
	public void deleteBizInfo(String... ids);

	/**
	 * 包含配置信息 taskId 
	 * @param id
	 * @return
	 */
	public BizInfo getByBizId(String id);
	
	/**
	 * 不包含配置信息 taskId 
	 * @param id
	 * @return
	 */
	public BizInfo getBizInfo(String id,String loginUser);

	/**
	 * 分页查询指定用户创建的工单
	 * @return
	 */
	public PageHelper<BizInfo> getBizInfoList(Map<String, Object> params, PageHelper<BizInfo> page);

	/**
	 * 工单催办
	 * @param bizIds
	 */
	public void sendMessage(List<String> bizIds, String message);;

	/**
	 * 
	 * @param structId
	 * @return
	 */
	public String loadStructCity(String structId);
	
	/**
	 * 
	 * @param list
	 */
	public void updateBizByIds(List<String> list);
	
	/**
	 * 批量查询工单
	 * @param list
	 * @return
	 */
	public List<BizInfo> getBizInfos(List<String> list);

	public List<String> loadBizInfoStatus(String processId);


	public List<BizInfo> getBizByParentId(String parentId);

	public void sendEmail(List<String> bizIds);

	public void removeLeft(List<String> bizIds);

}
