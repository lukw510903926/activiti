package com.eastcom.esflow.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.eastcom.esflow.util.context.ContextFactory;

/**
 * 转派处理
 * 		人/角色
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
@Component("turnTaskListener")
public class TurnTaskListener implements TaskListener {

	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger("turnTaskListener");
	@Override
	public void notify(DelegateTask delegateTask) {
	
		String serviceVendor = (String)delegateTask.getVariable("serviceVendor");
		
		String handleUser = (String)delegateTask.getVariable("handleUser");
		TaskService taskService = (TaskService) ContextFactory.getBeanByType(TaskServiceImpl.class);
		log.info(handleUser);
		if(StringUtils.isNotBlank(handleUser)){
		
			List<IdentityLink> links = taskService.getIdentityLinksForTask(delegateTask.getId());
			List<String> result = new ArrayList<String>();
			if(links != null && !links.isEmpty()) {
				for (IdentityLink il : links) {
					if("candidate".equals(il.getType())) {
						String groupName = il.getGroupId();
						if(StringUtils.isNotEmpty(groupName))
							result.add(groupName);
					}
				}
			}
			
			for (String group : result) {
				if (StringUtils.isNotBlank(group))
					taskService.deleteCandidateGroup(delegateTask.getId(), group);
			}
			if(handleUser.startsWith("GROUP:")){
				String[] group = handleUser.split("\\:");
				if(group.length>1&&StringUtils.isNotBlank(group[1])){
					delegateTask.addCandidateGroup(group[1]);
					log.info("group handle.....................................");
				}
			}else{
				delegateTask.setAssignee(handleUser);
				log.info("user handle.....................................");
			}
		}
		if(StringUtils.isNotBlank(serviceVendor)){
			delegateTask.addCandidateGroup(serviceVendor);
		}
	}
}
