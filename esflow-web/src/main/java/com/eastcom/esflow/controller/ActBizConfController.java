package com.eastcom.esflow.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eastcom.esflow.common.utils.DataGrid;
import com.eastcom.esflow.common.utils.Json;
import com.eastcom.esflow.common.utils.PageHelper;
import com.eastcom.esflow.service.BizInfoConfService;
import com.eastcom.esflow.service.BizSystemUserConfService;
import com.eastcom.esflow.service.IBizFileService;
import com.eastcom.esflow.service.IProcessDefinitionService;
import com.eastcom.esflow.service.act.ActProcessService;

@Controller
@RequestMapping("/actBizConf")
public class ActBizConfController{

	@Autowired
	private ActProcessService actProcessService;
	
	@Autowired
	private BizSystemUserConfService bizSystemUserConfService;
	
	@Autowired
	private IProcessDefinitionService processDefinitionService ;
	
	@Autowired
	private BizInfoConfService bizInfoConfService;
	
	@Autowired
	private IBizFileService bizFileService;
	
	private Logger logger = Logger.getLogger(ActBizConfController.class);
	
	@ResponseBody
	@RequestMapping(value = "/saveBizSystemUserConf")
	public Json saveBizSystemUserConf(@RequestParam String[] list,@RequestParam Map<String,String> params){
		
		Json json = new Json();
		try {
			this.bizSystemUserConfService.saveBizSystemUserConf(Arrays.asList(list), params);
		} catch (Exception e) {
			json.setMsg("配置项保存异常："+(e.getCause() == null ? e.getLocalizedMessage() : e.getCause()));
			json.setSuccess(false);
			return json;
		}
		json.setSuccess(true);
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "/findBizSystemUserConf")
	public DataGrid findBizSystemUserConf(PageHelper<Map<String,String>> page,@RequestParam Map<String,String> params){
		
		logger.info("params : "+ params);
		String bizType = params.get("bizType");
		if(StringUtils.isNotBlank(bizType)&&"english".equalsIgnoreCase(params.get("nameType"))){
			ProcessDefinition processDefinition = processDefinitionService.getLatestProcDefByKey(bizType);
			logger.info("bizType : "+ processDefinition.getName());
			params.put("bizType",  processDefinition.getName());
		}
		PageHelper<Map<String,String>> result = this.bizSystemUserConfService.findBizSystemUserConf(page, params);
		DataGrid dg = new DataGrid();
		dg.setRows(result.getList());
		dg.setTotal(result.getTotal());
		return dg;
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteChangeConf")
	public Json deleteChangeConf(String[] list){
		
		logger.info("list : " + list);
		Json json = new Json();
		try {
			this.bizSystemUserConfService.deleteByIds(Arrays.asList(list));
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("删除失败");
			json.setSuccess(false);
			return json;
		}
		json.setMsg("删除成功");
		json.setSuccess(true);
		return json;
	}
	
	/**
	 * 所属流程查询
	 * @param params
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value= "/findProcess")
	public Map<String, Object> findProcess(@RequestParam Map<String,String> params){
			
		List<ProcessDefinition> list = actProcessService.getList();
		Map<String, Object> item = new HashMap<String, Object>();
			for(ProcessDefinition process:list){
				item.put(process.getKey(), process.getName());
			}
			return item;
	}
	
	@ResponseBody
	@RequestMapping(value = "/turnTask")
	public Json turnTask(@RequestParam Map<String,Object> params){
		
		Json json = new Json();
		try {
			this.bizInfoConfService.turnTask(params);
		} catch (Exception e) {
			e.printStackTrace();
			json.setMsg("转派失败");
			json.setSuccess(false);
			return json;
		}
		json.setMsg("转派成功");
		json.setSuccess(true);
		return json;
	}
	
	@RequestMapping("deleteFile")
	public void deleteFile(String id){
		bizFileService.deleteBizFile(new String[]{id});
	}
	
}
