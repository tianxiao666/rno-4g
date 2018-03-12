package com.iscreate.op.pojo.routineinspection;

import java.util.Date;

/**
 * RoutineinspectionQuestion entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RoutineinspectionQuestion implements java.io.Serializable {

	// Fields

	private Long id;
	private String seriousLevel;
	private String questionType;
	private String description;
	private String questionPicture;
	private String resourceType;
	private String resourceId;
	private String resourceName;
	private String creator;
	private String creatorName;
	private Long creatorOrgId;
	private String creatorOrgName;
	private Date createTime;
	private String handler;
	private String handlerName;
	private Date handleTime;
	private String handleResult;
	private String handlePicture;
	private Integer isOver;

	// Constructors

	/** default constructor */
	public RoutineinspectionQuestion() {
	}

	/** full constructor */
	public RoutineinspectionQuestion(String seriousLevel,String resourceName,String creatorOrgName,
			String questionType, String description, String questionPicture,
			String resourceType, String resourceId, String creator,
			String creatorName, Long creatorOrgId, Date createTime,
			String handler, String handlerName, Date handleTime,
			String handleResult, String handlePicture, Integer isOver) {
		this.seriousLevel = seriousLevel;
		this.questionType = questionType;
		this.description = description;
		this.questionPicture = questionPicture;
		this.resourceType = resourceType;
		this.resourceId = resourceId;
		this.creator = creator;
		this.creatorName = creatorName;
		this.creatorOrgId = creatorOrgId;
		this.createTime = createTime;
		this.handler = handler;
		this.handlerName = handlerName;
		this.handleTime = handleTime;
		this.handleResult = handleResult;
		this.handlePicture = handlePicture;
		this.isOver = isOver;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public String getSeriousLevel() {
		return seriousLevel;
	}

	public void setSeriousLevel(String seriousLevel) {
		this.seriousLevel = seriousLevel;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuestionPicture() {
		return this.questionPicture;
	}

	public void setQuestionPicture(String questionPicture) {
		this.questionPicture = questionPicture;
	}

	public String getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
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

	public String getHandler() {
		return this.handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getHandlerName() {
		return this.handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public Date getHandleTime() {
		return this.handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public String getHandleResult() {
		return this.handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}

	public String getHandlePicture() {
		return this.handlePicture;
	}

	public void setHandlePicture(String handlePicture) {
		this.handlePicture = handlePicture;
	}

	public Integer getIsOver() {
		return this.isOver;
	}

	public void setIsOver(Integer isOver) {
		this.isOver = isOver;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getCreatorOrgName() {
		return creatorOrgName;
	}

	public void setCreatorOrgName(String creatorOrgName) {
		this.creatorOrgName = creatorOrgName;
	}
	
}