package com.eastcom.esflow.service;

import java.util.Map;

import com.eastcom.esflow.bean.BizInfo;


public interface IBizInfoHandleServcice{

	public Map<String,Object> getCurrentTask(String bizId,String loginUser);

	public BizInfo submit(Map<String, Object> params);

}
