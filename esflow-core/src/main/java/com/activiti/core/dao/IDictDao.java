package com.activiti.core.dao;

import java.util.List;
import java.util.Map;

import com.activiti.core.bean.Dict;
import com.activiti.core.common.dao.IBaseDao;
import com.activiti.core.common.utils.PageHelper;

/**
 * 字典
 * @author zhongty
 *
 */
public interface IDictDao  extends IBaseDao<Dict>{

	/**
	 * 字典配置列表
	 * @param page
	 * @param map
	 * @return
	 */
	public PageHelper<Map<String, Object>> findDictConfig(PageHelper<Map<String, Object>> page,Map<String,Object> map);
	/**
	 * 字典数据列表
	 * @param page
	 * @param map
	 * @return
	 */
	public PageHelper<Map<String, Object>> findDictData(PageHelper<Map<String, Object>> page,Map<String,Object> map);
	
	/**
	 * 已启用字典配置列表
	 * @return
	 */
	public List<Dict> findDictEnable();
	/**
	 * 创建数据表
	 * @param dict
	 */
	public void createDictDataTbl(Dict dict);
	
	/**
	 * 删除数据表
	 * @param tableName
	 */
	public void delDictDataTbl(String tableName);
	
	/**
	 * 通过字典名称、id获取字典配置
	 * @param param
	 * @return
	 */
	public Dict findDictByParam(Map<String,Object> param);
	
	
	/**
	 * 获取字典表数据
	 * @param tblName
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findDictData(String tblName,Map<String,Object> param);
	
	/**
	 * 字典名称检测
	 * @param dictName
	 * @return
	 */
	public Boolean checkDictName(String dictName);
	
	/**
	 * 导入字典数据
	 * @param tblName
	 */
	public void importDictData(String tblName,Map<String, Object> data);
	
	/**
	 * 编辑字典数据
	 * @param tblName
	 * @param data
	 */
	public void editDictData(String tblName,Map<String,Object> data);
	
	/**
	 * 批量删除字典数据
	 * @param tblName
	 * @param ids
	 */
	public void delDictData(String tblName,List<String> ids);
	
	/**
	 * 删除字典表中所有数据
	 * @param tblName
	 */
	public void delDictDataAll(String tblName);
	
	/**
	 * 获取字典数据
	 * @param tblName
	 * @param dataId
	 * @return
	 */
	public Map<String,Object> findDictData(String tblName,String dataId);
}
