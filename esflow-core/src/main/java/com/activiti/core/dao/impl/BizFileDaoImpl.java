package com.activiti.core.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.activiti.core.bean.BizFile;
import com.activiti.core.common.dao.BaseDaoImpl;
import com.activiti.core.dao.IBizFileDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class BizFileDaoImpl extends BaseDaoImpl<BizFile> implements IBizFileDao {

	@Override
	public List<BizFile> loadBizFileByBizId(String bizId, String taskId){
		
		List<Object> list = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("FROM BizFile bean WHERE bean.bizInfo.id = ? ");
		list.add(bizId);
		if(StringUtils.isBlank(taskId)){
			hql.append(" and bean.taskId = null ");
		}else{
			hql.append(" and bean.taskId = ?  ORDER BY bean.createDate ASC");
			list.add(taskId);
		}
		return this.find(hql.toString(), list.toArray());
	}
	
	public List<BizFile> findBizFile(Map<String,String> params){
		
		List<Object> list = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(" from BizFile file join fetch file.bizInfo biz where 1=1 ");
		if(StringUtils.isNotBlank(params.get("taskId"))){
			hql.append(" and file.taskId = ? ");
			list.add(params.get("taskId"));
		}
		if(StringUtils.isNotBlank(params.get("bizId"))){
			hql.append(" and biz.id = ? ");
			list.add(params.get("bizId"));
		}
		if(StringUtils.isNotBlank(params.get("name"))){
			hql.append(" and file.fileCatalog = ? ");
			list.add(params.get("name"));
		}
		return this.find(hql.toString(),list.toArray());
	}

}
