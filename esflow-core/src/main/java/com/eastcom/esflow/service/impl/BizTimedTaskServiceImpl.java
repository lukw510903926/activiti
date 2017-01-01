package com.eastcom.esflow.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eastcom.esflow.bean.BizInfo;
import com.eastcom.esflow.bean.BizInfoConf;
import com.eastcom.esflow.bean.BizTimedTask;
import com.eastcom.esflow.common.service.BaseServiceImpl;
import com.eastcom.esflow.common.utils.DateUtils;
import com.eastcom.esflow.dao.BizTimedTaskDao;
import com.eastcom.esflow.service.BizTimedTaskService;
import com.eastcom.esflow.service.IProcessExecuteService;

@Service
@Transactional(readOnly = true)
public class BizTimedTaskServiceImpl extends BaseServiceImpl<BizTimedTask> implements BizTimedTaskService{

	@Autowired
	private BizTimedTaskDao bizTimedTaskDao;
	
	@Autowired
	private IProcessExecuteService processExecuteService;
	
	private Logger logger = Logger.getLogger(BizTimedTaskServiceImpl.class);
	
	@Override
	@Transactional(readOnly = false)
	public void saveTimedTask(BizInfo bizInfo,BizInfoConf bizConf){
		
		BizTimedTask  bizTimedTask = this.buildTimeTask(bizInfo, bizConf);
		if(StringUtils.isNotBlank(bizTimedTask.getButtonId())){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, 3);
			bizTimedTask.setEndTime(DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd"));
			this.bizTimedTaskDao.save(bizTimedTask);
		}
	}
	
	@Override
	@Scheduled(cron="0 20 23 * * ? ")  
	@Transactional(readOnly = false)
	public void sumitBizTimedTask(){
		
		BizTimedTask  bizTimedTask = new BizTimedTask();
		Date date = new Date();
		bizTimedTask.setEndTime(DateUtils.formatDate(date, "yyyy-MM-dd"));
		List<BizTimedTask> list = this.bizTimedTaskDao.findBizTimedTask(bizTimedTask);
		try {
			if(list != null && !list.isEmpty()){
				for (BizTimedTask bizTask : list) {
					
					bizTask.setEndTime(null);
					List<BizTimedTask> result = this.bizTimedTaskDao.findBizTimedTask(bizTask);
					if(result != null && !result.isEmpty()){
						Map<String,Object> params = new HashMap<String,Object>();
						params.put("base.buttonId", bizTask.getButtonId());
						params.put("base.workNumber", bizTask.getBizId());
						params.put("treatment", "确认");
						params.put("result", "好");
						params.put("complain", "否");
						params.put("isShuffle", "否");
						params.put("handleMessage", "3天未处理,自动提交工单");
						params.put("base.handleName", "用户确认");
						params.put("base.handleResult","提交");
						processExecuteService.submit(params, null);
						this.deleteTimedTask(bizTask.getId());
					}
				}
			}
		} catch (Exception e) {
			logger.error("sumitBizTimedTask  ", e);
		}
	}
	
	@Transactional(readOnly = false)
	public void deleteTimedTask(String id){
		this.bizTimedTaskDao.deleteTimedTask(id);
	}
	
	private BizTimedTask buildTimeTask(BizInfo bizInfo,BizInfoConf bizConf){
		
		BizTimedTask  bizTimedTask = new BizTimedTask();
		String taskDefKey = bizInfo.getTaskName();
		bizTimedTask.setTaskName(bizInfo.getTaskDefKey());
		bizTimedTask.setTaskDefKey(taskDefKey);
		bizTimedTask.setTaskId(bizConf.getTaskId());
		bizTimedTask.setBizId(bizInfo.getId());
		String bizType = bizInfo.getBizType();
		String buttonId = null;
		if("事件管理".equalsIgnoreCase(bizType)){
			if("userConfirm".equalsIgnoreCase(taskDefKey)){
				buttonId = "flow11";
			}else if("userConfirm2".equalsIgnoreCase(taskDefKey)){
				buttonId = "flow31";
			}
		}
		bizTimedTask.setButtonId(buttonId);
		return bizTimedTask;
	}
	
	@Override
	public List<BizTimedTask> findBizTimedTask(BizTimedTask bizTask) {
		return this.bizTimedTaskDao.findBizTimedTask(bizTask);
	}
	
	public static void main(String[] args) {
		
		Date date = new Date();
		System.out.println(DateUtils.formatDate(date, "yyyy-MM-dd"));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 7);
		System.out.println(cal.getTime());
		System.out.println(DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd"));
	}
}
