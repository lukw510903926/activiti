package com.eastcom.esflow.service;

import java.util.List;

import com.eastcom.esflow.bean.BizInfo;
import com.eastcom.esflow.bean.BizInfoConf;
import com.eastcom.esflow.bean.BizTimedTask;
import com.eastcom.esflow.common.service.IBaseService;

public interface BizTimedTaskService extends IBaseService<BizTimedTask>{

	public List<BizTimedTask> findBizTimedTask(BizTimedTask bizTask);

	public void saveTimedTask(BizInfo bizInfo,BizInfoConf bizConf);

	public void sumitBizTimedTask();
}
