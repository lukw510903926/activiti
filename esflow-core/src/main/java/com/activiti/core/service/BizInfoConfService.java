package com.activiti.core.service;

import java.util.List;
import java.util.Map;

import com.activiti.core.bean.BizInfoConf;
import com.activiti.core.common.service.IBaseService;

/**
 * @author 2622
 * @time 2016年5月30日
 * @email lukw@eastcom-sw.com
 */
public interface BizInfoConfService extends IBaseService<BizInfoConf> {

	public List<BizInfoConf> findBizInfoConf(BizInfoConf bizInfoConf);
	
	public BizInfoConf getBizInfoConfByBizId(String bizId);
	
	public void saveOrUpdate(BizInfoConf bizInfoConf);
	
	/**
	 * 工单转派 (不通过工作流)
	 * @param map
	 */
	public void turnTask(Map<String, Object> map);

}
