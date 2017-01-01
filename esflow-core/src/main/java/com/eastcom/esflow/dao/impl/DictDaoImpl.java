package com.eastcom.esflow.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.eastcom.esflow.bean.Dict;
import com.eastcom.esflow.common.dao.BaseDaoImpl;
import com.eastcom.esflow.common.utils.PageHelper;
import com.eastcom.esflow.dao.IDictDao;

@Repository
public class DictDaoImpl extends BaseDaoImpl<Dict> implements IDictDao {

	@Value("${dictData.catalog}")
	private String catalog;
	
	@Value("${jdbc.type}")
	private String dbType;
	
	@Override
	public PageHelper<Map<String, Object>> findDictConfig(PageHelper<Map<String, Object>> page, Map<String, Object> param) {
		StringBuffer sql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		sql.append(" SELECT DICT.ID ID,DICT.DICT_NAME DICTNAME,DICT.TBL_NAME TBLNAME,DICT.STATUS STATUS,DICT.UPDATE_USER ");
		sql.append(" UPDATEUSER, DICT.UPDATE_TIME UPDATETIME FROM ESFLOW.CONF_DATA_DICTIONARY DICT WHERE 1=1 ");
		
		if(StringUtils.isNotBlank((String)param.get("dictName"))){
			sql.append(" AND DICT.DICT_NAME = ?");
			list.add((String)param.get("dictName"));
		}
		if(StringUtils.isNotBlank((String)param.get("status"))){
			sql.append(" AND DICT.STATUS= ? ");
			list.add((String)param.get("status"));
		}
		sql.append(" ORDER BY DICT.DICT_NAME, DICT.UPDATE_TIME");
		return this.findBySql(page,sql.toString(),list.toArray(), Map.class);
	}
	
	@Override
	public PageHelper<Map<String, Object>> findDictData(
			PageHelper<Map<String, Object>> page, Map<String, Object> param) {
		StringBuffer sql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		sql.append("SELECT T.ID ID,T.NAME NAME,T.UPDATE_USER UPDATEUSER,T.UPDATE_TIME UPDATETIME FROM "+param.get("tblName").toString()+" T");
		
		if(StringUtils.isNotBlank((String)param.get("name"))){
			sql.append(" AND T.NAME = ?");
			list.add((String)param.get("name"));
		}
		sql.append(" ORDER BY T.NAME, T.UPDATE_TIME");
		return this.findBySql(page,sql.toString(),list.toArray(), Map.class);
	}


	@Override
	public void createDictDataTbl(Dict dict) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("create table " + catalog + "." + dict.getTblName() + "(");
		sql.append("ID VARCHAR(32) PRIMARY KEY,");
		sql.append("NAME VARCHAR(64) NOT NULL,");
		sql.append("UPDATE_USER VARCHAR(32) NOT NULL,");
		if("mysql".equalsIgnoreCase(dbType))
			sql.append("UPDATE_TIME TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");
		else
			sql.append(" UPDATE_TIME TIMESTAMP(6) DEFAULT SYSDATE NOT NULL ");
		sql.append(")");

		this.executeBySql(sql.toString());
	}

	@Override
	public void delDictDataTbl(String tableName) {
		this.executeBySql("drop table " + tableName);
	}

	@Override
	public List<Dict> findDictEnable() {
		String hql = "from Dict d where d.status = ?";
		return this.find(hql, new Integer[]{0});
	}

	@Override
	public Dict findDictByParam(Map<String, Object> param) {
		String dictId = (String) param.get("dictId");
		String dictName = (String) param.get("dictName");
		
		List<String> params = new ArrayList<String>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from Dict d where 1=1 ");
		if(StringUtils.isNotEmpty(dictId)){
			hql.append(" and d.id = ? ");
			params.add(dictId);
		}if(StringUtils.isNotEmpty(dictName)){
			hql.append(" and d.dictName = ? ");
			params.add(dictName);
		}
		List<Dict> list = this.find(hql.toString(),params.toArray());
		if(null == list || list.isEmpty()){
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<Map<String, Object>> findDictData(String tblName,
			Map<String, Object> param) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from "+tblName+" where 1=1 ");
		if(param != null){
			for(String key:param.keySet()){
				sql.append(" and "+key+" = ?");
				params.add(param.get(key));
			}
		}
		sql.append(" order by id desc ");
		return this.findBySql(sql.toString(),params.toArray(),Map.class);
	}

	@Override
	public Boolean checkDictName(String dictName) {
		String hql = "select count(0) from Dict d where d.dictName = '"+dictName+"'";
		return this.count(hql)>0;
	}

	@Override
	public void importDictData(String tblName, Map<String, Object> data) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into ");
		sql.append(tblName);
		sql.append(" (id,name,update_user) values(?,?,?)");
		List<Object> param = new ArrayList<Object>();
		param.add(data.get("id"));
		param.add(data.get("name"));
		param.add(data.get("userName"));
		this.executeBySql(sql.toString(), param.toArray());
	}

	@Override
	public void editDictData(String tblName, Map<String, Object> data) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update ");
		sql.append(tblName);
		sql.append(" set name = ?, update_user = ? ");
		sql.append(" where id = ?");
		List<Object> param = new ArrayList<Object>();
		param.add(data.get("name"));
		param.add(data.get("userName"));
		param.add(data.get("id"));
		this.executeBySql(sql.toString(), param.toArray());
	}

	@Override
	public void delDictData(String tblName, List<String> ids) {
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from ");
		sql.append(tblName);
		sql.append(" where id in (");
		for(int i=0;i<ids.size();i++){
			sql.append(" ? ");
			if(i!=(ids.size()-1))
				sql.append(",");
		}
		sql.append(" )");
		this.executeBySql(sql.toString(), ids.toArray());
	}

	@Override
	public void delDictDataAll(String tblName) {
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from ");
		sql.append(tblName);
		 
		this.executeBySql(sql.toString());
	}

	@Override
	public Map<String, Object> findDictData(String tblName, String dataId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT T.ID,T.NAME,T.UPDATE_USER UPDATEUSER,T.UPDATE_TIME UPDATETIME FROM ");
		sql.append(tblName);
		sql.append(" T WHERE T.ID = ?");
		List<Map<String, Object>> list = this.findBySql(sql.toString(),new Object[]{dataId},Map.class);
		if(list == null || list.isEmpty()){
			return null;
		}
		return list.get(0);
	}


}
