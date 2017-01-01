package com.eastcom.esflow.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.eastcom.esflow.bean.Dict;
import com.eastcom.esflow.common.utils.PageHelper;

/**
 * 字典业务
 * @author zhongty
 *
 */
public interface IDictService {

	/**
	 * 字典配置列表
	 * @param page
	 * @param map
	 * @return
	 */
	public PageHelper<Map<String, Object>> findDictConfig(PageHelper<Map<String, Object>> page,Map<String,Object> map) ;
	
	/**
	 * 字典数据列表
	 * @param page
	 * @param map
	 * @return
	 * @
	 */
	public PageHelper<Map<String, Object>> findDictData(String dictId,PageHelper<Map<String, Object>> page,Map<String,Object> map) ;
	
	/**
	 * 字典配置列表
	 * @param page
	 * @param map
	 * @return
	 */
	public List<Dict> findDictEnable() ;
	
	/**
	 * 获取字段配置
	 * @param dictId
	 * @return
	 * @
	 */
	public Dict getDict(String dictId);
	
	/**
	 * 增加、编辑字典配置
	 * @param dict
	 * @
	 */
	public void addDictConfig(Dict dic);
	
	/**
	 * 批量删除
	 * @param ids
	 * @
	 */
	public void delDictConfig(List<String> ids);
	
	/**
	 * 导入字典数据
	 * @param dictId
	 * @param userName
	 * @param file
	 * @
	 */
	public void importDictData(String dictId,String userName,MultipartFile file);
	
	/**
	 * 通过字典名称、id获取表数据
	 * @param param dictName、dictId
	 * @return
	 * @
	 */
	public List<Map<String,Object>> loadDictDataByParam(Map<String,Object> param) ;
	
	/**
	 * 添加字典数据
	 * @param dictId
	 * @param userName
	 * @param name
	 * @
	 */
	public void addDictData(String dictId,String userName,String name);
	
	/**
	 * 编辑字典数据
	 * @param dictId
	 * @param userName
	 * @param name
	 * @
	 */
	public void editDictData(String dictId,String dataId,String userName,String name);
	
	/**
	 * 批量删除字典数据
	 * @param dictId
	 * @param ids
	 * @
	 */
	public void delDictData(String dictId,List<String> ids);
	
	/**
	 * 删除字典数据表中所有
	 * @param dictId
	 * @
	 */
	public void delDictDataAll(String dictId);
	
	/**
	 * 获取字典数据
	 * @param dictId
	 * @param dataId
	 * @return
	 * @
	 */
	public Map<String,Object> findDictData(String dictId,String dataId);
}
