package com.activiti.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.activiti.core.bean.Dict;
import com.activiti.core.common.exception.ServiceException;
import com.activiti.core.common.utils.PageHelper;
import com.activiti.core.dao.IDictDao;
import com.activiti.core.service.IDictService;
import com.activiti.core.util.DataBaseAgentUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly=true)
public class DictServiceImpl implements IDictService {
	
	@Autowired
	private IDictDao dictDao;

	@Override
	public PageHelper<Map<String, Object>> findDictConfig(PageHelper<Map<String, Object>> page, Map<String, Object> map) {
		
		return dictDao.findDictConfig(page, map);
	}
	
	@Override
	public PageHelper<Map<String, Object>> findDictData(String dictId,PageHelper<Map<String, Object>> page, Map<String, Object> map){
		
		if(StringUtils.isEmpty(dictId)||dictId.trim().length()<32){
			throw new ServiceException("参数为空");
		}
		Dict dict = dictDao.getById(dictId);
		if(dict == null){
			throw new ServiceException("字典配置信息不存在");
		}
		map.put("tblName", DataBaseAgentUtil.getDictTblName(dict));
		page = dictDao.findDictData(page, map);
		return page;
	}

	@Override
	@Transactional(readOnly=false)
	public void addDictConfig(Dict dict){
		if(StringUtils.isEmpty(dict.getId())){
			if(StringUtils.isEmpty(dict.getDictName())){
				throw new ServiceException("字典名称为空");
			}
			if(dictDao.checkDictName(dict.getDictName())){
				throw new ServiceException("字典名称已存在");
			}
			dict.setTblName(DataBaseAgentUtil.builTableName());
			//保存配置信息
			dictDao.save(dict);
			//创建数据表
			dictDao.createDictDataTbl(dict);
		}else{
			//保存配置信息
			dictDao.update(dict);
		}
	}

	@Override
	@Transactional(readOnly=false)
	public void delDictConfig(List<String> ids) {
		if(null == ids || ids.isEmpty()){
			throw new ServiceException("参数为空");
		}
		Dict dict = new Dict();
		for(int i=0;i<ids.size();i++){
			dict = dictDao.getById(ids.get(i));
			dictDao.delDictDataTbl(DataBaseAgentUtil.getDictTblName(dict));
			dictDao.delete(dict);
		}
	}

	@Override
	public Dict getDict(String dictId) {
		return dictDao.getById(dictId);
	}

	@Override
	public List<Dict> findDictEnable() {
		return dictDao.findDictEnable();
	}

	
	public List<Map<String,Object>> loadDictDataByParam(Map<String,Object> param) throws ServiceException{
		if(param == null || param.size()<1){
			throw new ServiceException("参数为空!");
		}
		
		if(param.get("dictName")==null && param.get("dictId")==null){
			throw new ServiceException("参数错误!");
		}
		//查询字典配置
		Dict dict = dictDao.findDictByParam(param);
		if(dict == null){
			//throw new ServiceException("字典配置不存在!");
			return null;
		}
		param.remove("dictId");
		param.remove("dictName");
		//查询表数据
		return dictDao.findDictData(DataBaseAgentUtil.getDictTblName(dict), param);
	}

	@Override
	@Transactional(readOnly=false)
	public void importDictData(String dictId,String userName,MultipartFile file) {
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		String value = null;
		Map<String,Object> param = null;
		
		if(StringUtils.isEmpty(dictId)||file == null){
			throw new ServiceException("导入参数错误！");
		}
		
		Dict dict = dictDao.getById(dictId);
		if(dict == null){
			throw new ServiceException("没有对应字典配置信息");
		}
		
		try {
			Workbook workBook = WorkbookFactory.create(file.getInputStream());
			Integer sheetNum = workBook.getNumberOfSheets();
			if(sheetNum == null || sheetNum<1){
				throw new ServiceException("文档格式错误！");
			}
			for(int i=0;i<sheetNum;i++){
				sheet = workBook.getSheetAt(i);
				if(sheet == null)
					continue;
				for(int j=0;j<=sheet.getLastRowNum();j++){
					row = sheet.getRow(j);
					if (row == null)
						continue;
					for(int m=0;m<=row.getLastCellNum();m++){
						cell = row.getCell(m);
						if(cell == null)	
							continue;
						param = new HashMap<String,Object>();
						value = cell.getStringCellValue();
						param.put("id", UUID.randomUUID());
						param.put("name", value.trim());
						param.put("userName", userName);
						dictDao.importDictData(DataBaseAgentUtil.getDictTblName(dict), param);
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException("文件读取异常!"+e.getLocalizedMessage());
		}
		
	}

	@Override
	@Transactional(readOnly=false)
	public void addDictData(String dictId, String userName, String name)
			{
		if(StringUtils.isEmpty(dictId)||StringUtils.isEmpty(name)){
			throw new ServiceException("参数为空");
		}
		Dict dict = dictDao.getById(dictId);
		if(dict == null){
			throw new ServiceException("字典配置信息不存在");
		}
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("id", UUID.randomUUID());
		data.put("name", name);
		data.put("userName", userName);
		dictDao.importDictData(DataBaseAgentUtil.getDictTblName(dict), data);
	}

	@Override
	@Transactional(readOnly=false)
	public void editDictData(String dictId, String dataId, String userName,
			String name) {
		if(StringUtils.isEmpty(dictId)||StringUtils.isEmpty(dataId)||StringUtils.isEmpty(name)){
			throw new ServiceException("参数为空");
		}
		Dict dict = dictDao.getById(dictId);
		if(dict == null){
			throw new ServiceException("字典配置信息不存在");
		}
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("id", dataId);
		data.put("name", name);
		data.put("userName", userName);
		dictDao.editDictData(DataBaseAgentUtil.getDictTblName(dict), data);
	}

	@Override
	@Transactional(readOnly=false)
	public void delDictData(String dictId, List<String> ids)
			{
		if(StringUtils.isEmpty(dictId)||ids == null || ids.isEmpty()){
			throw new ServiceException("参数为空");
		}
		Dict dict = dictDao.getById(dictId);
		if(dict == null){
			throw new ServiceException("字典配置信息不存在");
		}
		dictDao.delDictData(DataBaseAgentUtil.getDictTblName(dict), ids);
	}

	@Override
	@Transactional(readOnly=false)
	public void delDictDataAll(String dictId) {
		if(StringUtils.isEmpty(dictId)){
			throw new ServiceException("参数为空");
		}
		Dict dict = dictDao.getById(dictId);
		if(dict == null){
			throw new ServiceException("字典配置信息不存在");
		}
		dictDao.delDictDataAll(DataBaseAgentUtil.getDictTblName(dict));
	}

	@Override
	public Map<String, Object> findDictData(String dictId, String dataId)
			{
		if(StringUtils.isEmpty(dictId)||StringUtils.isEmpty(dataId)){
			throw new ServiceException("参数为空");
		}
		Dict dict = dictDao.getById(dictId);
		if(dict == null){
			throw new ServiceException("字典配置信息不存在");
		}
		return dictDao.findDictData(DataBaseAgentUtil.getDictTblName(dict), dataId);
	}

}
