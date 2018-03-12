package com.iscreate.op.pojo.workmanage;

import java.util.Date;

/**
 * WorkmanageWorkorder entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WorkmanageWorkorder implements java.io.Serializable {

	// Fields

	private Long id;
	private Long draftId;
	private String processDefineId;
	private String processInstId;
	private String woId;
	private String woType;
	private String woTitle;
	private Integer status;
	private String currentTaskId;
	private String currentTaskName;
	private String creator;
	private String creatorName;
	private Long creatorOrgId;
	private Date createTime;
	private Integer isOverTime;
	private Date requireCompleteTime;
	private String acceptComment;
	private Date finalCompleteTime;
	private String currentHandler;
	private String currentHandlerName;
	private Long currentHandlerOrgId;
	private String acceptPeople;
	private String acceptPeopleName;
	private String lastReplyPeople;
	private String lastReplyPeopleName;
	private Integer isRead;
	private Integer isSendWillOverTime;
	private Integer isSendOverTime;
	
	private String orderOwner;
	private Long orderOwnerOrgId;
	

	// Constructors

	/** default constructor */
	public WorkmanageWorkorder() {
	}

	/** full constructor */
	public WorkmanageWorkorder(Long draftId, String processDefineId,
			String processInstId, String woId, String woType, String woTitle,
			Integer status, String currentTaskId, String currentTaskName,
			String creator, String creatorName, Long creatorOrgId,
			Date createTime, Integer isOverTime, Date requireCompleteTime,
			String acceptComment, Date finalCompleteTime,
			String currentHandler, String currentHandlerName,
			Long currentHandlerOrgId, String acceptPeople,
			String acceptPeopleName, String lastReplyPeople,
			String lastReplyPeopleName, Integer isRead) {
		this.draftId = draftId;
		this.processDefineId = processDefineId;
		this.processInstId = processInstId;
		this.woId = woId;
		this.woType = woType;
		this.woTitle = woTitle;
		this.status = status;
		this.currentTaskId = currentTaskId;
		this.currentTaskName = currentTaskName;
		this.creator = creator;
		this.creatorName = creatorName;
		this.creatorOrgId = creatorOrgId;
		this.createTime = createTime;
		this.isOverTime = isOverTime;
		this.requireCompleteTime = requireCompleteTime;
		this.acceptComment = acceptComment;
		this.finalCompleteTime = finalCompleteTime;
		this.currentHandler = currentHandler;
		this.currentHandlerName = currentHandlerName;
		this.currentHandlerOrgId = currentHandlerOrgId;
		this.acceptPeople = acceptPeople;
		this.acceptPeopleName = acceptPeopleName;
		this.lastReplyPeople = lastReplyPeople;
		this.lastReplyPeopleName = lastReplyPeopleName;
		this.isRead = isRead;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDraftId() {
		return this.draftId;
	}

	public void setDraftId(Long draftId) {
		this.draftId = draftId;
	}

	public String getProcessDefineId() {
		return this.processDefineId;
	}

	public void setProcessDefineId(String processDefineId) {
		this.processDefineId = processDefineId;
	}

	public String getProcessInstId() {
		return this.processInstId;
	}

	public void setProcessInstId(String processInstId) {
		this.processInstId = processInstId;
	}

	public String getWoId() {
		return this.woId;
	}

	public void setWoId(String woId) {
		this.woId = woId;
	}

	public String getWoType() {
		return this.woType;
	}

	public void setWoType(String woType) {
		this.woType = woType;
	}

	public String getWoTitle() {
		return this.woTitle;
	}

	public void setWoTitle(String woTitle) {
		this.woTitle = woTitle;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCurrentTaskId() {
		return this.currentTaskId;
	}

	public void setCurrentTaskId(String currentTaskId) {
		this.currentTaskId = currentTaskId;
	}

	public String getCurrentTaskName() {
		return this.currentTaskName;
	}

	public void setCurrentTaskName(String currentTaskName) {
		this.currentTaskName = currentTaskName;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return this.creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Long getCreatorOrgId() {
		return this.creatorOrgId;
	}

	public void setCreatorOrgId(Long creatorOrgId) {
		this.creatorOrgId = creatorOrgId;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getIsOverTime() {
		return this.isOverTime;
	}

	public void setIsOverTime(Integer isOverTime) {
		this.isOverTime = isOverTime;
	}

	public Date getRequireCompleteTime() {
		return this.requireCompleteTime;
	}

	public void setRequireCompleteTime(Date requireCompleteTime) {
		this.requireCompleteTime = requireCompleteTime;
	}

	public String getAcceptComment() {
		return this.acceptComment;
	}

	public void setAcceptComment(String acceptComment) {
		this.acceptComment = acceptComment;
	}

	public Date getFinalCompleteTime() {
		return this.finalCompleteTime;
	}

	public void setFinalCompleteTime(Date finalCompleteTime) {
		this.finalCompleteTime = finalCompleteTime;
	}

	public String getCurrentHandler() {
		return this.currentHandler;
	}

	public void setCurrentHandler(String currentHandler) {
		this.currentHandler = currentHandler;
	}

	public String getCurrentHandlerName() {
		return this.currentHandlerName;
	}

	public void setCurrentHandlerName(String currentHandlerName) {
		this.currentHandlerName = currentHandlerName;
	}

	public Long getCurrentHandlerOrgId() {
		return this.currentHandlerOrgId;
	}

	public void setCurrentHandlerOrgId(Long currentHandlerOrgId) {
		this.currentHandlerOrgId = currentHandlerOrgId;
	}

	public String getAcceptPeople() {
		return this.acceptPeople;
	}

	public void setAcceptPeople(String acceptPeople) {
		this.acceptPeople = acceptPeople;
	}

	public String getAcceptPeopleName() {
		return this.acceptPeopleName;
	}

	public void setAcceptPeopleName(String acceptPeopleName) {
		this.acceptPeopleName = acceptPeopleName;
	}

	public String getLastReplyPeople() {
		return this.lastReplyPeople;
	}

	public void setLastReplyPeople(String lastReplyPeople) {
		this.lastReplyPeople = lastReplyPeople;
	}

	public String getLastReplyPeopleName() {
		return this.lastReplyPeopleName;
	}

	public void setLastReplyPeopleName(String lastReplyPeopleName) {
		this.lastReplyPeopleName = lastReplyPeopleName;
	}

	public Integer getIsRead() {
		return this.isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public Integer getIsSendWillOverTime() {
		return isSendWillOverTime;
	}

	public void setIsSendWillOverTime(Integer isSendWillOverTime) {
		this.isSendWillOverTime = isSendWillOverTime;
	}
	
	

	public Integer getIsSendOverTime() {
		return isSendOverTime;
	}

	public void setIsSendOverTime(Integer isSendOverTime) {
		this.isSendOverTime = isSendOverTime;
	}

	public String getOrderOwner() {
		return orderOwner;
	}

	public void setOrderOwner(String orderOwner) {
		this.orderOwner = orderOwner;
	}

	public Long getOrderOwnerOrgId() {
		return orderOwnerOrgId;
	}

	public void setOrderOwnerOrgId(Long orderOwnerOrgId) {
		this.orderOwnerOrgId = orderOwnerOrgId;
	}

	
	
	

}