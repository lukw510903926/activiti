package com.activiti.core.service;

import java.util.List;

import com.activiti.core.bean.BizInfoConf;
import com.activiti.core.bean.BizTimedTask;
import com.activiti.core.common.service.IBaseService;
import com.activiti.core.bean.BizInfo;

public interface BizTimedTaskService extends IBaseService<BizTimedTask> {

	public List<BizTimedTask> findBizTimedTask(BizTimedTask bizTask);

	public void saveTimedTask(BizInfo bizInfo,BizInfoConf bizConf);

	public void sumitBizTimedTask();
}
