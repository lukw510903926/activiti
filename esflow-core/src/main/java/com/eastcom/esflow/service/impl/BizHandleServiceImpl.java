package com.eastcom.esflow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.esflow.bean.ProcessVariableInstance;
import com.eastcom.esflow.common.utils.PageHelper;
import com.eastcom.esflow.dao.IBizHandleDao;
import com.eastcom.esflow.service.IBizHandleService;
import com.eastcom.esflow.service.IProcessVariableService;

@Service
@Transactional(readOnly=true)
public class BizHandleServiceImpl implements IBizHandleService{

	@Autowired
	private IBizHandleDao bizHandlDao;
	
	private Logger logger = Logger.getLogger("bizHandleService");
	
	@Autowired
	private IProcessVariableService processService;
	
	@Override
	public PageHelper<Map<String, Object>> loadMembers(PageHelper<Map<String, Object>> page, Map<String, Object> params){
		
		logger.info("params: " + params);
		if(params.containsKey("headQuarter")){
			Map<String,String> query = new HashMap<String, String>();
			query.put("name", "invovelDepartments");
			query.put("bizId", (String)params.get("bizId"));
			List<ProcessVariableInstance> list = this.processService.getProcessVariableInstances(query);
			if(list !=null && !list.isEmpty()){
				logger.info("params: " + list.get(0).getValue());
				params.put("deptment", list.get(0).getValue());
				params.put("headType", "headType");
			}
		}
		PageHelper<Map<String, Object>> result = bizHandlDao.findMember(page, params);
		List<Map<String, Object>> list = result.getList();
		List<Map<String, Object>> resultList= new ArrayList<Map<String,Object>>();
		Map<String, Object> param = null;
		if(list != null && !list.isEmpty()){
			for(Map<String, Object> map : list){
				param = new HashMap<String, Object>();
				for(String key : map.keySet()){
					param.put(key.toLowerCase(), map.get(key));
				}
				resultList.add(param);
			}
		}
		result.setList(resultList);
		return result;
	}

	@Override
	public List<Map<String, Object>> loadSectors(Map<String, Object> params) {
		
		List<Map<String, Object>> list = this.bizHandlDao.findSector(params);
		List<Map<String, Object>> resultList= new ArrayList<Map<String,Object>>();
		Map<String, Object> param = null;
		if(list != null && !list.isEmpty()){
			for(Map<String, Object> map : list){
				param = new HashMap<String, Object>();
				param.put("id", map.get("ID"));
				param.put("pId", map.get("PID"));
				param.put("name", map.get("NAME"));
				resultList.add(param);
			}
		} 
		return resultList;
	}
}
