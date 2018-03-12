package com.iscreate.op.pojo.urgentrepair;

import java.util.Date;

/**
 * UrgentrepairDevice2gworkorder entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UrgentrepairDevice2gworkorder implements java.io.Serializable {

	// Fields

	private Long id;
	private String customerWoId;
	private Date assginOrderTime;
	private String woStatus;
	private Date acceptTime;
	private String isOverTime;
	private Date firstAlarmTime;
	private Date latestAlarmTime;
	private String vipLevel;
	private String smallAreaNumber;
	private String smallAreaName;
	private String acceptDomain;
	private String alarmType;
	private String alarmLevel;
	private String alarmContent;
	private String faultType;
	private String faultCause;
	private Date recoverTime;
	private String handleProgress;
	private String acceptMaintain;
	private String woRemark;
	private String woId;

	// Constructors

	/** default constructor */
	public UrgentrepairDevice2gworkorder() {
	}

	/** full constructor */
	public UrgentrepairDevice2gworkorder(String customerWoId,
			Date assginOrderTime, String woStatus, Date acceptTime,
			String isOverTime, Date firstAlarmTime, Date latestAlarmTime,
			String vipLevel, String smallAreaNumber, String smallAreaName,
			String acceptDomain, String alarmType, String alarmLevel,
			String alarmContent, String faultType, String faultCause,
			Date recoverTime, String handleProgress, String acceptMaintain,
			String woRemark, String woId) {
		this.customerWoId = customerWoId;
		this.assginOrderTime = assginOrderTime;
		this.woStatus = woStatus;
		this.acceptTime = acceptTime;
		this.isOverTime = isOverTime;
		this.firstAlarmTime = firstAlarmTime;
		this.latestAlarmTime = latestAlarmTime;
		this.vipLevel = vipLevel;
		this.smallAreaNumber = smallAreaNumber;
		this.smallAreaName = smallAreaName;
		this.acceptDomain = acceptDomain;
		this.alarmType = alarmType;
		this.alarmLevel = alarmLevel;
		this.alarmContent = alarmContent;
		this.faultType = faultType;
		this.faultCause = faultCause;
		this.recoverTime = recoverTime;
		this.handleProgress = handleProgress;
		this.acceptMaintain = acceptMaintain;
		this.woRemark = woRemark;
		this.woId = woId;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerWoId() {
		return this.customerWoId;
	}

	public void setCustomerWoId(String customerWoId) {
		this.customerWoId = customerWoId;
	}

	public Date getAssginOrderTime() {
		return this.assginOrderTime;
	}

	public void setAssginOrderTime(Date assginOrderTime) {
		this.assginOrderTime = assginOrderTime;
	}

	public String getWoStatus() {
		return this.woStatus;
	}

	public void setWoStatus(String woStatus) {
		this.woStatus = woStatus;
	}

	public Date getAcceptTime() {
		return this.acceptTime;
	}

	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}

	public String getIsOverTime() {
		return this.isOverTime;
	}

	public void setIsOverTime(String isOverTime) {
		this.isOverTime = isOverTime;
	}

	public Date getFirstAlarmTime() {
		return this.firstAlarmTime;
	}

	public void setFirstAlarmTime(Date firstAlarmTime) {
		this.firstAlarmTime = firstAlarmTime;
	}

	public Date getLatestAlarmTime() {
		return this.latestAlarmTime;
	}

	public void setLatestAlarmTime(Date latestAlarmTime) {
		this.latestAlarmTime = latestAlarmTime;
	}

	public String getVipLevel() {
		return this.vipLevel;
	}

	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getSmallAreaNumber() {
		return this.smallAreaNumber;
	}

	public void setSmallAreaNumber(String smallAreaNumber) {
		this.smallAreaNumber = smallAreaNumber;
	}

	public String getSmallAreaName() {
		return this.smallAreaName;
	}

	public void setSmallAreaName(String smallAreaName) {
		this.smallAreaName = smallAreaName;
	}

	public String getAcceptDomain() {
		return this.acceptDomain;
	}

	public void setAcceptDomain(String acceptDomain) {
		this.acceptDomain = acceptDomain;
	}

	public String getAlarmType() {
		return this.alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public String getAlarmLevel() {
		return this.alarmLevel;
	}

	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getAlarmContent() {
		return this.alarmContent;
	}

	public void setAlarmContent(String alarmContent) {
		this.alarmContent = alarmContent;
	}

	public String getFaultType() {
		return this.faultType;
	}

	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}

	public String getFaultCause() {
		return this.faultCause;
	}

	public void setFaultCause(String faultCause) {
		this.faultCause = faultCause;
	}

	public Date getRecoverTime() {
		return this.recoverTime;
	}

	public void setRecoverTime(Date recoverTime) {
		this.recoverTime = recoverTime;
	}

	public String getHandleProgress() {
		return this.handleProgress;
	}

	public void setHandleProgress(String handleProgress) {
		this.handleProgress = handleProgress;
	}

	public String getAcceptMaintain() {
		return this.acceptMaintain;
	}

	public void setAcceptMaintain(String acceptMaintain) {
		this.acceptMaintain = acceptMaintain;
	}

	public String getWoRemark() {
		return this.woRemark;
	}

	public void setWoRemark(String woRemark) {
		this.woRemark = woRemark;
	}

	public String getWoId() {
		return this.woId;
	}

	public void setWoId(String woId) {
		this.woId = woId;
	}

}