package com.eastcom.esflow.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.eastcom.esflow.bean.BizInfo;
import com.eastcom.esflow.common.utils.PageHelper;
import com.eastcom.esflow.service.IBizInfoService;
import com.eastcom.esflow.service.impl.BizInfoServiceImpl;
import com.eastcom.esflow.util.Constants;
import com.eastcom.esflow.util.context.ContextFactory;

/**
 *  完成有子工单的工单的处理
 * Title: esflow <br>
 * Description: <br>
 * Copyright: eastcom Copyright (C) 2009 <br>
 * @author <a href="mailto:lukw@eastcom-sw.com">lukw</a><br>
 * @email:lukw@eastcom-sw.com	<br>
 * @version 1.0 <br>
 * @creatdate 2016年4月8日 下午10:51:23 <br>
 *
 */
@Component("assignTaskListener")
public class AssignTaskListener implements ExecutionListener {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		
		IBizInfoService bizInfoService = (IBizInfoService) ContextFactory.getBeanByType(BizInfoServiceImpl.class);
		Map<String, Object> variables = execution.getVariables();
		String bizId = (String) variables.get(Constants.SYS_BIZ_ID);
		if (StringUtils.isNotEmpty(bizId)) {
			BizInfo bizInfo = bizInfoService.get(bizId);
			if(bizInfo == null)
				throw new Exception("工单ID： " + bizId + " 不存在");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("parentId", bizId);
			params.put("parentTaskName", bizInfo.getTaskName());
			PageHelper<BizInfo> page = new PageHelper<BizInfo>();
			page.setPage(-1);
			page.setRows(-1);
			List<BizInfo> list = bizInfoService.getBizInfoList(params,page).getList();
			if(list != null && !list.isEmpty())
				throw new Exception("工单： " + bizInfo.getBizId() + " 已分配子单,不可分派,请给出解决方案");
		} else {
			throw new Exception("工单ID： " + bizId + " 不存在");
		}
	}
}
