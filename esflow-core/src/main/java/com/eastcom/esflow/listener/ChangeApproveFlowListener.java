package com.eastcom.esflow.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;


/**
 * 变更 主管审核结束流程走向
 * Title: esflow <br>
 * Description: <br>
 * Copyright: eastcom Copyright (C) 2009 <br>
 * @author <a href="mailto:lukw@eastcom-sw.com">lukw</a><br>
 * @email:lukw@eastcom-sw.com	<br>
 * @version 1.0 <br>
 * @creatdate 2016年4月23日 上午11:44:42 <br>
 *
 */
@Component("changeApproveFlowListener")
public class ChangeApproveFlowListener implements ExecutionListener {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		String approve = (String)execution.getVariable("needApprove");
		if("是".equalsIgnoreCase(approve))
			execution.setVariable("SYS_BUTTON_VALUE", "yesFlow");
		else
			execution.setVariable("SYS_BUTTON_VALUE", "noFlow");
	}
		
}
