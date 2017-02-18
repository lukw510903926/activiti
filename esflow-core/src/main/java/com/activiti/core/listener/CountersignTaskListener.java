package com.activiti.core.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;


/**
 * 会签
 * Title: esflow <br>
 * Description: <br>
 * Copyright: eastcom Copyright (C) 2009 <br>
 * @author <a href="mailto:lukw@eastcom-sw.com">lukw</a><br>
 * @email:lukw@eastcom-sw.com	<br>
 * @version 1.0 <br>
 * @creatdate 2016年4月23日 上午11:44:42 <br>
 *
 */
@Component("countersignTaskListener")
public class CountersignTaskListener implements ExecutionListener ,TaskListener{
	
	private static final long serialVersionUID = 1L;
	
	public static Map<String,ArrayList<String>> membersCache = new HashMap<String,ArrayList<String>>();

	@Override
	public void notify(DelegateExecution delegateTask) {
		
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		
		@SuppressWarnings("unchecked")
		ArrayList<String> members = (ArrayList<String>)delegateTask.getVariable("members");;
		String key  = (String)delegateTask.getProcessDefinitionId()+":"+(String)delegateTask.getProcessInstanceId();
		
		if(!membersCache.containsKey(key)){
			membersCache.put(key, members);
		}
		delegateTask.setVariable("members", members);
	}
}
