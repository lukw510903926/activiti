package com.eastcom.esflow.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eastcom.esflow.bean.AbstractVariableInstance;
import com.eastcom.esflow.bean.BizFile;
import com.eastcom.esflow.bean.BizInfo;
import com.eastcom.esflow.bean.BizLog;
import com.eastcom.esflow.bean.ProcessVariableInstance;
import com.eastcom.esflow.common.utils.Json;
import com.eastcom.esflow.common.utils.PageHelper;
import com.eastcom.esflow.service.IBizFileService;
import com.eastcom.esflow.service.IBizInfoHandleServcice;
import com.eastcom.esflow.service.IBizInfoService;
import com.eastcom.esflow.service.IBizLogService;
import com.eastcom.esflow.service.IProcessExecuteService;
import com.eastcom.esflow.service.IProcessVariableService;
import com.eastcom.esflow.service.IVariableInstanceService;

@Controller
@RequestMapping("/esflowData/bizInfo/")
public class BizInfoController {

	@Autowired
	private IProcessExecuteService processService;
	
	@Autowired
	private IBizInfoService bizInfoService;
	
	@Autowired
	private IBizLogService bizLogService;
	
	@Autowired
	private IBizFileService bizFileService;
	
	@Autowired
	private IProcessVariableService processVariableService;
	
	@Autowired
	private IVariableInstanceService variableInstanceService;
	
	@Autowired
	private IBizInfoHandleServcice bizHandleService;
	
	private Logger logger = Logger.getLogger(BizInfoController.class);
	
	@ResponseBody
	@RequestMapping("/get/{bizId}/{loginUser}")
	public String getBizInfo(@PathVariable("bizId") String bizId,@PathVariable("loginUser") String loginUser){
		
		if(StringUtils.isNotBlank(bizId)){
			BizInfo bizInfo = this.bizInfoService.getBizInfo(bizId, loginUser);
			return JSONObject.toJSONString(bizInfo);
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/list")
	public String findBizInfo(@RequestBody Map<String,Object> params){
		
		if(params.get("loginUser")==null){
			return null;
		}
		String target = params.get("action") ==null ? "itAction" : (String)params.get("action");
		params.put("action", target);
		PageHelper<BizInfo> page = new PageHelper<BizInfo>();
		page.setRows(-1);
		page.setPage(-1);
		if(params.get("pageSize") != null){
			page.setRows(Integer.valueOf(params.get("pageSize").toString()));
		}
		if(params.get("pageNum")!=null){
			page.setPage(Integer.valueOf(params.get("pageNum").toString()));
		}
		PageHelper<BizInfo> result = this.processService.queryMyBizInfos(target, params, page);
		return JSONArray.toJSONString(result);
	}
	
	@ResponseBody
	@RequestMapping("/logs/list/{bizId}")
	public String findBizLogs(@PathVariable("bizId")String bizId){
		
		if(StringUtils.isNotBlank(bizId)){
			List<BizLog> list = this.bizLogService.loadBizLogs(bizId);
			return JSONArray.toJSONString(list);
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("process/list/{bizId}")
	public String findProcessVariables(@PathVariable("bizId")String bizId){
		
		if(StringUtils.isNotBlank(bizId)){
			Map<String,String> params = new HashMap<String, String>();
			params.put("bizId", bizId);
			List<ProcessVariableInstance> list = this.processVariableService.getProcessVariableInstances(params);
			return JSONArray.toJSONString(list);
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("task/list/{logId}")
	public String findTaskVariables(@PathVariable("logId")String logId){
		
		if(StringUtils.isNotBlank(logId)){
			BizLog bizLog = this.bizLogService.getBizLogById(logId);
			if(bizLog != null){
				List<AbstractVariableInstance> list = this.variableInstanceService.loadValueByLog(bizLog);
				return JSONArray.toJSONString(list);
			}
		}
		return null;		
	}
	
	@ResponseBody
	@RequestMapping("file/list/{logId}")
	public String findBizFiles(@PathVariable("logId")String logId){
		if(StringUtils.isNotBlank(logId)){
			BizLog bizLog = this.bizLogService.getBizLogById(logId);
			if(bizLog != null){
				String taskId = "START".equals(bizLog.getTaskID()) ? null : bizLog.getTaskID();
				List<BizFile> list = bizFileService.loadBizFilesByBizId(bizLog.getBizInfo().getId(),taskId);
				return JSONArray.toJSONString(list);
			}
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/currentTask/get/{bizId}/{loginUser}")
	public String getCurrentTask(@PathVariable("bizId")String bizId,@PathVariable("loginUser") String loginUser){
		
		if(StringUtils.isNotBlank(loginUser)&&StringUtils.isNotBlank(bizId)){
			Map<String,Object> result = bizHandleService.getCurrentTask(bizId, loginUser);
			return JSONObject.toJSONString(result);
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping("/submit")
	public String submitBiz(@RequestBody Map<String, Object> params){
		
		Json json = new Json();
		String bizId = (String)params.get("workNumber");
		BizInfo bizInfo = this.bizInfoService.get(bizId);
		try {
			params.put("base.buttonId", params.get("buttonId"));
			params.put("base.handleResult", params.get("handleResult"));
			params.put("base.handleName", bizInfo.getTaskDefKey());
			params.put("base.workNumber", params.get("workNumber"));
			bizInfo = this.bizHandleService.submit(params);
		} catch (Exception e) {
			logger.error(" submit error :",e);
			json.setSuccess(false);
			json.setMsg("提交失败");
			return JSONObject.toJSONString(json);
		}
		json.setSuccess(true);
		json.setMsg(JSONObject.toJSONString(bizInfo));
		return JSONObject.toJSONString(json);
	}
}
