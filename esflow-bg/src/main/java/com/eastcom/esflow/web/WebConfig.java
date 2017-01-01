/*
 * Created on 2015年12月28日
 * WebConfig.java V1.0
 *
 * Copyright Notice =========================================================
 * This file contains proprietary information of Eastcom Technologies Co. Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2012 =======================================================
 */
package com.eastcom.esflow.web;

import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.stereotype.Component;


/**
 * 
 * Title: cmdb-bg <br>
 * Description: <br>
 * Copyright: eastcom Copyright (C) 2009 <br>
 * 
 * @author <a href="mailto:zhangzq@eastcom-sw.com">张泽钦</a><br>
 * @e-mail: zhangzq@eastcom-sw.com <br>
 * @version 1.0 <br>
 * @creatdate 2015年12月28日 上午11:29:19 <br>
 * 
 */
@Component
public class WebConfig {
	
	@Bean
	public OpenSessionInViewFilter openSessionInViewFilter() {
		return new OpenSessionInViewFilter();
	}
}
