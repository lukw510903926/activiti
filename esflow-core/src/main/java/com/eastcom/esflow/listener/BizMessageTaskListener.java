package com.eastcom.esflow.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * 任务短信接口
 * @author 2622
 * @time 2016年7月1日
 * @email lukw@eastcom-sw.com
 */
@Component("bizMessageTaskListener")
public class BizMessageTaskListener implements TaskListener {
	
	private static final long serialVersionUID = 1L;

//	private Logger logger = Logger.getLogger("bizMessageTaskListener");

	@Override
	public void notify(DelegateTask delegateTask) {

//		String sendMessage = Config.getConfig("sendMessage");
//		if(!"sendMessage".equalsIgnoreCase(sendMessage))
//			return;
//		IBizInfoService bizInfoService = (IBizInfoService) ContextFactory.getBeanByType(BizInfoServiceImpl.class);
//		Map<String, Object> variables = delegateTask.getVariables();
//		String bizId = (String) variables.get(Constants.SYS_BIZ_ID);
//		String isSendMessage = (String)variables.get("isSendMessage");
//		if (StringUtils.isNotEmpty(bizId) && !"否".equalsIgnoreCase(isSendMessage)) {
//			List<String> list = new ArrayList<String>();
//			list.add(bizId);
//			bizInfoService.sendMessage(list, null);
//		}
//		BizInfo bizInfo = bizInfoService.get(bizId);
	}
}
