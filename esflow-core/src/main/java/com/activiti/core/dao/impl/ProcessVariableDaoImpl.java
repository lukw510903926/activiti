package com.activiti.core.dao.impl;

import java.util.List;
import java.util.Map;

import com.activiti.core.bean.ProcessVariable;
import com.activiti.core.common.dao.BaseDaoImpl;
import com.activiti.core.common.exception.ServiceException;
import com.activiti.core.common.utils.PageHelper;
import com.activiti.core.common.utils.ReflectionUtils;
import com.activiti.core.dao.IProcessVariableDao;
import org.springframework.stereotype.Repository;

@Repository
public class ProcessVariableDaoImpl extends BaseDaoImpl<ProcessVariable> implements IProcessVariableDao {

	@Override
	public List<ProcessVariable> loadProcessVariables(String processDefinitionId, int version) {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM ProcessVariable BEAN WHERE BEAN.processDefinitionId=? ");
		hql.append(" AND BEAN.version = ");
		Object[] args = null;
		if (version < 0) {
			args = new Object[] { processDefinitionId, processDefinitionId };
			hql.append("(SELECT max(version) FROM ProcessVariable WHERE processDefinitionId=?)");
		} else {
			hql.append("?");
			args = new Object[] { processDefinitionId, version };
		}
		hql.append(" ORDER BY BEAN.groupOrder ASC,BEAN.order ASC");
		return this.find(hql.toString(), args);
	}
	
	@Override
	public PageHelper<ProcessVariable> loadProcessVariables(String processDefinitionId, int version, PageHelper<ProcessVariable> page) {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM ProcessVariable BEAN WHERE BEAN.processDefinitionId=? ");
		hql.append(" AND BEAN.version = ");
		Object[] args = null;
		if (version < 0) {
			args = new Object[] { processDefinitionId, processDefinitionId };
			hql.append("(SELECT max(version) FROM ProcessVariable WHERE processDefinitionId=?)");
		} else {
			hql.append("?");
			args = new Object[] { processDefinitionId, version };
		}
		hql.append(" ORDER BY BEAN.groupOrder ASC,BEAN.order ASC");
		return this.find(page,hql.toString(), args);
	}

	public List<ProcessVariable> getRefList(Map<String,Object> params){
		StringBuffer hql = new StringBuffer(" from ProcessVariable where processDefinitionId=? and version=? and refVariable is not null and refVariable<>''");
		List<ProcessVariable> list = this.find(hql.toString(),new Object[]{params.get("processId"),params.get("version")});
		return list;
	}

	@Override
	public void deleteById(String id) {
		
		String hql = "delete from ProcessVariable where id = ? ";
		this.execute(hql,new Object[]{id});
	}

	@Override
	public int getProcessOrder(ProcessVariable bean) throws ServiceException {
		
		String sql = "SELECT MAX(NAME_ORDER) FROM RM.ACT_B_PROC_VAL WHERE " + (bean.getGroupName() == null ? "1=1" : "GROUP_NAME=?");
		sql += " AND TEMP_ID='" + bean.getProcessDefinitionId() + "' AND VERSION=" + bean.getVersion();
		List<Object> list = this.findBySql(sql, bean.getGroupName() == null ? null : new Object[] { bean.getGroupName() });
		if (list == null || list.size() <= 0 || list.get(0) == null) {
			return 0;
		}
		return (Integer) ReflectionUtils.convert(list.get(0), Integer.class);
	}
}
