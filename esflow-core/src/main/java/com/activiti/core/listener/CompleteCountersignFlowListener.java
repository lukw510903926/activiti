package com.activiti.core.listener;

import java.util.List;

import com.activiti.core.service.IBizInfoService;
import com.activiti.core.util.context.ContextFactory;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.activiti.core.bean.BizInfo;
import com.activiti.core.bean.Countersign;
import com.activiti.core.service.CountersignService;
import com.activiti.core.service.impl.BizInfoServiceImpl;
import com.activiti.core.service.impl.CountersignServiceImpl;
import com.activiti.core.util.Constants;


/**
 * 会签结束 流程走向判断
 * Title: esflow <br>
 * Description: <br>
 * Copyright: eastcom Copyright (C) 2009 <br>
 * @author <a href="mailto:lukw@eastcom-sw.com">lukw</a><br>
 * @email:lukw@eastcom-sw.com	<br>
 * @version 1.0 <br>
 * @creatdate 2016年4月23日 上午11:44:42 <br>
 *
 */
@Component("completeCountersignFlowListener")
public class CompleteCountersignFlowListener implements ExecutionListener {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		IBizInfoService bizInfoService = (IBizInfoService) ContextFactory.getBeanByType(BizInfoServiceImpl.class);
		CountersignService countersignService = (CountersignServiceImpl) ContextFactory.getBeanByType(CountersignServiceImpl.class);
		String bizId = (String) execution.getVariable(Constants.SYS_BIZ_ID);
		if (StringUtils.isNotEmpty(bizId)) {
			BizInfo bizInfo = bizInfoService.get(bizId);
			if(bizInfo != null){
				Countersign countersign = new Countersign();
				countersign.setProcessDefinitionId(bizInfo.getProcessDefinitionId());
				countersign.setProcessInstanceId(bizInfo.getProcessInstanceId());
				countersign.setResultType(Constants.MEET_NO);
				countersign.setIsComplete(0);
				List<Countersign> list = countersignService.findCountersign(countersign);
				if(list != null && !list.isEmpty())
					execution.setVariable("SYS_BUTTON_VALUE", "rejectFlow");
				else
					execution.setVariable("SYS_BUTTON_VALUE", "adoptFlow");
				countersign.setIsComplete(0);
				countersign.setResultType(null);
				list = countersignService.findCountersign(countersign);
				//会签完成,更新本次会签完成状态
				for(Countersign counter : list){
					counter.setIsComplete(1);
					countersignService.update(counter);
				}
			}else{
				throw new Exception("工单ID： " + bizId + "会签完成失败");
			}
		} else {
			throw new Exception("工单ID： " + bizId + "会签完成失败");
		}
	}
}
