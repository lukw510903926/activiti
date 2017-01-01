package com.eastcom.esflow.listener;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import com.eastcom.esflow.bean.ActBizInfoDelayTime;
import com.eastcom.esflow.bean.BizInfo;
import com.eastcom.esflow.common.utils.DateUtils;
import com.eastcom.esflow.service.ActBizInfoDelayTimeService;
import com.eastcom.esflow.service.IBizInfoService;
import com.eastcom.esflow.service.impl.ActBizInfoDelayTimeServiceImpl;
import com.eastcom.esflow.service.impl.BizInfoServiceImpl;
import com.eastcom.esflow.util.Constants;
import com.eastcom.esflow.util.context.ContextFactory;

/**
 * 延期申请确认
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
@Component("delayConfirmListener")
public class DelayConfirmListener implements ExecutionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		IBizInfoService bizInfoService = (IBizInfoService) ContextFactory.getBeanByType(BizInfoServiceImpl.class);
		ActBizInfoDelayTimeService delayTimeService = (ActBizInfoDelayTimeService) ContextFactory.getBeanByType(ActBizInfoDelayTimeServiceImpl.class);
		Map<String, Object> variables = execution.getVariables();
		String bizId = (String) variables.get(Constants.SYS_BIZ_ID);
		Date delaytime = DateUtils.parseDate((String)variables.get("delayTime"));
		String result = (String)variables.get("confirmResult");
		BizInfo bizInfo = bizInfoService.get(bizId);
		if("同意".equals(result)&& delaytime != null) {
			bizInfo.setLimitTime(delaytime);
			bizInfoService.updateBizInfo(bizInfo);
			ActBizInfoDelayTime temp = new ActBizInfoDelayTime();
			temp.setBizId(bizId);
			List<ActBizInfoDelayTime> list = delayTimeService.findActBizInfoDelayTime(temp);
			if(list != null && !list.isEmpty()) {
				ActBizInfoDelayTime delayTime = list.get(0);
				delayTime.setApplyStatus(1);
				delayTimeService.update(delayTime);
			}
		}
	}
}
