package com.iscreate.plat.workflow.datainput;

import java.util.Date;

/**
 * TaskActionRecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FlowTaskActionRecord implements java.io.Serializable {

	// Fields

	private Long id;
	private String taskId;
	private String actionName;
	private Integer actionType;
	private String actionContent;
	private Date createTime;
	private String createUserId;
	private String type;
	private String instanceId;

	// Constructors

	/** default constructor */
	public FlowTaskActionRecord() {
	}

	/** minimal constructor */
	public FlowTaskActionRecord(String taskId, String actionName, Date createTime) {
		this.taskId = taskId;
		this.actionName = actionName;
		this.createTime = createTime;
	}

	/** full constructor */
	public FlowTaskActionRecord(String taskId, String actionName,
			Integer actionType, String actionContent, Date createTime,
			String createUserId) {
		this.taskId = taskId;
		this.actionName = actionName;
		this.actionType = actionType;
		this.actionContent = actionContent;
		this.createTime = createTime;
		this.createUserId = createUserId;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public Integer getActionType() {
		return this.actionType;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public String getActionContent() {
		return this.actionContent;
	}

	public void setActionContent(String actionContent) {
		this.actionContent = actionContent;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

}