package com.iscreate.op.pojo.staffduty;
// default package

import java.util.Date;

/**
 * StaffdutyDutylog entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class StaffdutyDutylog implements java.io.Serializable {

	// Fields

	private Long id;
	private Long dutyTemplateId;
	private String userId;
	private String userName;
	private Date beginTime;
	private Date endTime;
	private Integer costTime;
	private String workLogType;
	private String workLog;
	private Date lastEditTime;

	// Constructors

	/** default constructor */
	public StaffdutyDutylog() {
	}

	/** minimal constructor */
	public StaffdutyDutylog(Long dutyTemplateId, Date beginTime, Date endTime) {
		this.dutyTemplateId = dutyTemplateId;
		this.beginTime = beginTime;
		this.endTime = endTime;
	}

	/** full constructor */
	public StaffdutyDutylog(Long dutyTemplateId, String userId,
			String userName, Date beginTime, Date endTime, Integer costTime,
			String workLogType,String workLog, Date lastEditTime) {
		this.dutyTemplateId = dutyTemplateId;
		this.userId = userId;
		this.userName = userName;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.costTime = costTime;
		this.workLogType = workLogType;
		this.workLog = workLog;
		this.lastEditTime = lastEditTime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDutyTemplateId() {
		return this.dutyTemplateId;
	}

	public void setDutyTemplateId(Long dutyTemplateId) {
		this.dutyTemplateId = dutyTemplateId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getCostTime() {
		return this.costTime;
	}

	public void setCostTime(Integer costTime) {
		this.costTime = costTime;
	}

	public String getWorkLogType() {
		return workLogType;
	}

	public void setWorkLogType(String workLogType) {
		this.workLogType = workLogType;
	}

	public String getWorkLog() {
		return this.workLog;
	}

	public void setWorkLog(String workLog) {
		this.workLog = workLog;
	}

	public Date getLastEditTime() {
		return this.lastEditTime;
	}

	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

}