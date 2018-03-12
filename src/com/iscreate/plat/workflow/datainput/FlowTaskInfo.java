package com.iscreate.plat.workflow.datainput;

import java.util.Date;

/**
 * FlowDoTaskList entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FlowTaskInfo implements java.io.Serializable {

	// Fields

	private String taskId;
	private String taskStatus;
	private String nodeId;
	private String sendUserId;
	private Date sendDate;
	private String acceptorType;
	private String acceptorUserId;
	private String currentUserId;
	private Date acceptDate;
	private Date completeDate;
	private String instanceId;
	private String toNodeId;
	private String flowId;
	private String taskName;
	// Constructors

	/** default constructor */
	public FlowTaskInfo() {
	}

	/** minimal constructor */
	public FlowTaskInfo(String taskId, Date sendDate, Date acceptDate,
			Date completeDate) {
		this.taskId = taskId;
		this.sendDate = sendDate;
		this.acceptDate = acceptDate;
		this.completeDate = completeDate;
	}

	/** full constructor */
	public FlowTaskInfo(String taskId, String taskStatus, String nodeId,
			String sendUserId, Date sendDate, String acceptorType,
			String acceptorUserId, String currentUserId, Date acceptDate,
			Date completeDate, String instanceId, String toNodeId, String flowId,String taskName) {
		this.taskId = taskId;
		this.taskStatus = taskStatus;
		this.nodeId = nodeId;
		this.sendUserId = sendUserId;
		this.sendDate = sendDate;
		this.acceptorType = acceptorType;
		this.acceptorUserId = acceptorUserId;
		this.currentUserId = currentUserId;
		this.acceptDate = acceptDate;
		this.completeDate = completeDate;
		this.instanceId = instanceId;
		this.toNodeId = toNodeId;
		this.flowId = flowId;
		this.taskName=taskName;
	}

	// Property accessors

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskStatus() {
		return this.taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getSendUserId() {
		return this.sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getAcceptorType() {
		return this.acceptorType;
	}

	public void setAcceptorType(String acceptorType) {
		this.acceptorType = acceptorType;
	}

	public String getAcceptorUserId() {
		return this.acceptorUserId;
	}

	public void setAcceptorUserId(String acceptorUserId) {
		this.acceptorUserId = acceptorUserId;
	}

	public String getCurrentUserId() {
		return this.currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	public Date getAcceptDate() {
		return this.acceptDate;
	}

	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}

	public Date getCompleteDate() {
		return this.completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public String getInstanceId() {
		return this.instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getToNodeId() {
		return this.toNodeId;
	}

	public void setToNodeId(String toNodeId) {
		this.toNodeId = toNodeId;
	}

	public String getFlowId() {
		return this.flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}