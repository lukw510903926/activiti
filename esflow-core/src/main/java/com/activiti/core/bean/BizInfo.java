package com.activiti.core.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.activiti.core.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 工单对象
 */
@Entity
@Table(name = "ACT_BIZ_INFO")
@DynamicInsert
@DynamicUpdate
public class BizInfo implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = -9003521142344551524L;

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Column(unique = true, nullable = false, length = 64, name = "ID")
	private String id;

	/**
	 * 工单号
	 */
	@Column(length = 64, name = "BIZ_ID")
	private String bizId;

	@Column(nullable = false, length = 512, name = "TITLE")
	private String title;

	@Column(nullable = false, length = 256, name = "BIZ_TYPE")
	private String bizType;

	@Column(nullable = false, length = 64, name = "PROCESS_DEFINITION_ID")
	private String processDefinitionId;

	@Column(length = 64, name = "PROCESS_INSTANCE_ID")
	private String processInstanceId;

	@Column(nullable = true, length = 256, name = "TASK_ID")
	private String taskId;

	@Column(nullable = true, length = 64, name = "TASK_DEF_KEY")
	private String taskDefKey;

	@Column(nullable = true, length = 256, name = "TASK_NAME")
	private String taskName;

	/**
	 * 当前任务处理人
	 */
	@Column(nullable = true, length = 256, name = "TASK_ASSIGNEE")
	private String taskAssignee;

	@Column(nullable = false, length = 256, name = "CREATE_USER")
	private String createUser;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LIMIT_TIME")
	private Date limitTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;

	@Column(name = "ISSHUFFLE")
	private Integer isShuffle = 0;
	
	@Column(nullable = false, length = 32, name = "BIZ_STATUS")
	private String status = Constants.BIZ_TEMP;

	@Column(nullable = false, length = 128, name = "SOURCE")
	private String source;
	
	@Column(nullable = true, length = 64, name = "PARENT_ID")
	private String parentId;
	
	@Column(nullable = true, length = 256, name = "PARENT_TASKNAME")
	private String parentTaskName;

	/**
	 * 工单催办次数
	 */
	@Column(nullable = false, name = "PRESS_COUNT")
	private Integer pressCount = 0;
	
	public BizInfo() {
	}

	public BizInfo(String id, String bizId, String title, String bizType,
			String processDefinitionId, String processInstanceId,String taskId, String taskDefKey, String taskName,
			String taskAssignee, String createUser, Date limitTime,	Date createTime, Date operateTime, String status, String source,
			String parentId, String parentTaskName, Integer pressCount, Integer isShuffle) {
		super();
		this.id = id;
		this.bizId = bizId;
		this.title = title;
		this.bizType = bizType;
		this.processDefinitionId = processDefinitionId;
		this.processInstanceId = processInstanceId;
		this.taskId = taskId;
		this.taskDefKey = taskDefKey;
		this.taskName = taskName;
		this.taskAssignee = taskAssignee;
		this.createUser = createUser;
		this.limitTime = limitTime;
		this.createTime = createTime;
		this.isShuffle = isShuffle;
		this.status = status;
		this.source = source;
		this.parentId = parentId;
		this.parentTaskName = parentTaskName;
		this.pressCount = pressCount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskAssignee() {
		return taskAssignee;
	}

	public void setTaskAssignee(String taskAssignee) {
		this.taskAssignee = taskAssignee;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Integer getIsShuffle() {
		return isShuffle;
	}
	
	public void setIsShuffle(Integer isShuffle) {
		this.isShuffle = isShuffle;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(Date limitTime) {
		this.limitTime = limitTime;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public String getParentTaskName() {
		return parentTaskName;
	}

	public void setParentTaskName(String parentTaskName) {
		this.parentTaskName = parentTaskName;
	}
	
	public Integer getPressCount() {
		return pressCount;
	}

	public void setPressCount(Integer pressCount) {
		this.pressCount = pressCount;
	}
	
	public BizInfo clone() {
		BizInfo bizInfo = null;
		try {
			bizInfo = (BizInfo) super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bizInfo;
	}

}
