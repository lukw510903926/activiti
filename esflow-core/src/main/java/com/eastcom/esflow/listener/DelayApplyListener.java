package com.eastcom.esflow.listener;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.eastcom.esflow.bean.ActBizInfoDelayTime;
import com.eastcom.esflow.bean.BizInfo;
import com.eastcom.esflow.bean.BizInfoConf;
import com.eastcom.esflow.common.utils.DateUtils;
import com.eastcom.esflow.service.ActBizInfoDelayTimeService;
import com.eastcom.esflow.service.BizInfoConfService;
import com.eastcom.esflow.service.IBizInfoService;
import com.eastcom.esflow.service.impl.ActBizInfoDelayTimeServiceImpl;
import com.eastcom.esflow.service.impl.BizInfoConfServiceImpl;
import com.eastcom.esflow.service.impl.BizInfoServiceImpl;
import com.eastcom.esflow.util.Constants;
import com.eastcom.esflow.util.context.ContextFactory;

/**
 * 延期申请处理
 * 
 * Title: esflow <br>
 * Description: <br>
 * Copyright: eastcom Copyright (C) 2009 <br>
 * 
 * @author <a href="mailto:zhangzq@eastcom-sw.com">张泽钦</a><br>
 * @e-mail: zhangzq@eastcom-sw.com <br>
 * @version 1.0 <br>
 * @creatdate 2016年1月29日 下午2:55:43 <br>
 *
 */
@Component("delayApplyListener")
public class DelayApplyListener implements ExecutionListener {
	
	private static final long serialVersionUID = 1L;


	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		IBizInfoService bizInfoService = (IBizInfoService) ContextFactory.getBeanByType(BizInfoServiceImpl.class);
		BizInfoConfService bizInfoConfService = (BizInfoConfService) ContextFactory.getBeanByType(BizInfoConfServiceImpl.class);
		ActBizInfoDelayTimeService delayTimeService = (ActBizInfoDelayTimeService) ContextFactory.getBeanByType(ActBizInfoDelayTimeServiceImpl.class);
		TaskService taskService = execution.getEngineServices().getTaskService();
		Map<String, Object> variables = execution.getVariables();
		String bizId = (String) variables.get(Constants.SYS_BIZ_ID);
		Date delaytime = DateUtils.parseDate((String)variables.get("delayTime"));
		
		if (StringUtils.isNotEmpty(bizId)) {
			BizInfo bizInfo = bizInfoService.get(bizId);
			BizInfoConf bizInfoConf = bizInfoConfService.getBizInfoConfByBizId(bizId);
			Task task = taskService.createTaskQuery().taskId(bizInfoConf.getTaskId()).singleResult();
			ActBizInfoDelayTime delayTime = new ActBizInfoDelayTime();
			delayTime.setBizId(bizId);
			delayTime.setTaskName(task.getName());
			List<ActBizInfoDelayTime> list = delayTimeService.findActBizInfoDelayTime(delayTime);
			if(list != null && list.size()>2)
				throw new Exception("延期失败, 工单： " + bizInfo.getBizId() + " 延期不可以超过3次!");
			if (bizInfo != null && bizInfoConf != null) {
				ActBizInfoDelayTime delayTimeInfo = new ActBizInfoDelayTime();
				delayTimeInfo.setDelayTime(delaytime);
				delayTimeInfo.setBizId(bizId);
				delayTimeInfo.setApplyStatus(0);
				delayTimeInfo.setCreateTime(new Date());
				delayTimeInfo.setTaskId(bizInfoConf.getTaskId());
				delayTimeInfo.setTaskName(task.getName());
				delayTimeService.save(delayTimeInfo);
			}
		} else {
			throw new Exception("延期失败, 工单ID： " + bizId + "不存在");
		}
	}

}
