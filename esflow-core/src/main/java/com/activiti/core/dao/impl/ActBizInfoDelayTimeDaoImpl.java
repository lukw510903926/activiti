package com.activiti.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.activiti.core.bean.ActBizInfoDelayTime;
import com.activiti.core.common.dao.BaseDaoImpl;
import com.activiti.core.dao.ActBizInfoDelayTimeDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ActBizInfoDelayTimeDaoImpl extends BaseDaoImpl<ActBizInfoDelayTime> implements ActBizInfoDelayTimeDao {

	@Override
	public ActBizInfoDelayTime findActBizInfoByBizId(String bizId, String taskName){
		
		StringBuilder hql = new StringBuilder( "from ActBizInfoDelayTime a where a.bizId = ?  ");
		hql.append(" and applyStatus=0 and a.createTime = ");
		hql.append(" (select max(b.createTime) from ActBizInfoDelayTime b where b.bizId = ?)");
		List<ActBizInfoDelayTime> list = this.find(hql.toString(),new Object[]{bizId,bizId});
		if(list!=null && !list.isEmpty())
			return list.get(0);
		return null;
	}
	
	@Override
	public List<ActBizInfoDelayTime> findActBizInfoDelayTime(ActBizInfoDelayTime delayTime){
		
		StringBuilder hql = new StringBuilder(" from ActBizInfoDelayTime a where 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if(delayTime != null){
			if(StringUtils.isNotBlank(delayTime.getBizId())){
				hql.append(" and a.bizId = ? ");
				list.add(delayTime.getBizId());
			}
			if(StringUtils.isNotBlank(delayTime.getTaskName())){
				hql.append(" and a.taskName = ? ");
				list.add(delayTime.getTaskName());
			}
			if(StringUtils.isNotBlank(delayTime.getTaskId())){
				hql.append(" and a.taskId = ? ");
				list.add(delayTime.getTaskId());
			}
		}
		hql.append(" order by a.createTime desc");
		return this.find(hql.toString(),list.toArray());
	}
}
