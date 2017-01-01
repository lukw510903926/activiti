package com.eastcom.esflow.bean;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="conf_data_dictionary",catalog="esflow")
public class Dict implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String dictName;
	private String tblName;
	private Integer status;
	private String updateUser;
	private Calendar updateTime;
	
	public Dict(){}
	public Dict(String id,String dictName,String tblName,Integer status,String updateUser,Calendar updateTime){
		this.id = id;
		this.dictName = dictName;
		this.tblName = tblName;
		this.status = status;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
	}
	
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 64)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "DICT_NAME",unique=true, nullable = false, length = 64)
	public String getDictName() {
		return dictName;
	}
	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
	
	@Column(name = "TBL_NAME",updatable=false,unique=true, nullable = false, length = 32)
	public String getTblName() {
		return tblName;
	}
	public void setTblName(String tblName) {
		this.tblName = tblName;
	}
	
	@Column(name = "STATUS", precision = 1, scale = 0)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "UPDATE_USER", nullable = false, length = 64)
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", nullable = false,updatable = true)
	public Calendar getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Calendar updateTime) {
		this.updateTime = updateTime;
	}
	
}
