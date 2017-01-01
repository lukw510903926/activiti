package com.eastcom.esflow.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.eastcom.esflow.bean.BizTimedTask;
import com.eastcom.esflow.common.dao.BaseDaoImpl;
import com.eastcom.esflow.dao.BizTimedTaskDao;

@Repository
public class BizTimedTaskDaoImpl extends BaseDaoImpl<BizTimedTask> implements BizTimedTaskDao{

	@Override
	@SuppressWarnings("unchecked")
	public List<BizTimedTask> findBizTimedTask(BizTimedTask bizTask) {

		List<Object> list = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(" from BizTimedTask t where 1=1 ");
		if(bizTask.getEndTime()!=null){
			hql.append(" and t.endTime = ? ");
			list.add(bizTask.getEndTime());
		}
		if(StringUtils.isNotBlank(bizTask.getBizId())){
			hql.append(" and t.bizId = ? ");
			list.add(bizTask.getBizId());
		}
		if(StringUtils.isNotBlank(bizTask.getTaskId())){
			hql.append(" and t.taskId = ? ");
			list.add(bizTask.getTaskId());
		}
		Query  query = this.getSessionFactory().openSession().createQuery(hql.toString());
		for (int i = 0; i < list.size(); i++) {
			query.setParameter(i, list.get(i));
		}
		return query.list();
	}
	
	@Override
	public void deleteTimedTask(String id){
		
		if(StringUtils.isNotBlank(id)){
			String sql =" DELETE FROM ESFLOW.BIZ_TIMED_TASK WHERE ID = ? ";
			this.executeBySql(sql,new Object[]{id});
		}
	}
}
