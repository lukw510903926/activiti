package com.eastcom.esflow.listener;

import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.eastcom.esflow.bean.BizInfo;
import com.eastcom.esflow.bean.BizInfoConf;
import com.eastcom.esflow.service.BizInfoConfService;
import com.eastcom.esflow.service.IBizInfoService;
import com.eastcom.esflow.service.impl.BizInfoConfServiceImpl;
import com.eastcom.esflow.service.impl.BizInfoServiceImpl;
import com.eastcom.esflow.util.Constants;
import com.eastcom.esflow.util.context.ContextFactory;

/**
 *  完成主工单时 手动完成子工单
 * Title: esflow <br>
 * Description: <br>
 * Copyright: eastcom Copyright (C) 2009 <br>
 * @author <a href="mailto:lukw@eastcom-sw.com">lukw</a><br>
 * @email:lukw@eastcom-sw.com	<br>
 * @version 1.0 <br>
 * @creatdate 2016年4月8日 下午10:51:23 <br>
 *
 */
@Component("completeSubBizInfoListener")
public class CompleteSubBizInfoListener implements ExecutionListener {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {

		IBizInfoService bizInfoService = (IBizInfoService) ContextFactory.getBeanByType(BizInfoServiceImpl.class);
		BizInfoConfService bizInfoConfService = (BizInfoConfService) ContextFactory.getBeanByType(BizInfoConfServiceImpl.class);
		Map<String, Object> variables = execution.getVariables();
		String bizId = (String) variables.get(Constants.SYS_BIZ_ID);
		BizInfo bizInfo = null;
		try {
			if (StringUtils.isNotEmpty(bizId)) {
				bizInfo = bizInfoService.get(bizId);
				List<BizInfo> list = bizInfoService.getBizByParentId(bizId);
				if (list != null && !list.isEmpty()) {
					for (BizInfo subBizInfo : list) {
						BizInfoConf conf = new BizInfoConf();
						conf.setBizInfo(subBizInfo);
						subBizInfo.setStatus(Constants.BIZ_END);
						bizInfoService.updateBizInfo(subBizInfo);
						List<BizInfoConf> bizConfList = bizInfoConfService.findBizInfoConf(conf);
						if (bizConfList != null && !bizConfList.isEmpty()) {
							for (BizInfoConf bizInfoConf : bizConfList) {
								bizInfoConf.setTaskAssignee("-");
								bizInfoConfService.update(bizInfoConf);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception(bizInfo.getBizType() + "工单完成失败,工单号:"+bizInfo.getBizId());
		}
	}
}
