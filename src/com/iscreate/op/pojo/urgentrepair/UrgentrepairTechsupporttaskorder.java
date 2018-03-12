package com.iscreate.op.pojo.urgentrepair;

import java.io.Serializable;
import java.util.Date;

/**
 * UrgentrepairTechsupporttaskorder entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class UrgentrepairTechsupporttaskorder implements Serializable{

	// Fields
	private static final long serialVersionUID = 1L;

	private Long id;
	private String toId;
	private Date foreseeSolveTime;
	private String reasonForDelayApply;
	private Date faultSolveTime;
	private String faultHandleResult;
	private Date alarmClearTime;
	private Integer isAlarmClear;
	private String faultHandleDetail;
	private String faultArea;
	private String faultReason;
	private Integer sideeffectService;
	private String affectedServiceName;
	private String faultGenera;

	// Constructors

	/** default constructor */
	public UrgentrepairTechsupporttaskorder() {
	}

	/** minimal constructor */
	public UrgentrepairTechsupporttaskorder(String toId) {
		this.toId = toId;
	}

	/** full constructor */
	public UrgentrepairTechsupporttaskorder(String toId, Date foreseeSolveTime,
			String reasonForDelayApply, Date faultSolveTime,
			String faultHandleResult, Date alarmClearTime, Integer isAlarmClear,
			String faultHandleDetail, String faultArea, String faultReason,
			Integer sideeffectService, String affectedServiceName,
			String faultGenera) {
		this.toId = toId;
		this.foreseeSolveTime = foreseeSolveTime;
		this.reasonForDelayApply = reasonForDelayApply;
		this.faultSolveTime = faultSolveTime;
		this.faultHandleResult = faultHandleResult;
		this.alarmClearTime = alarmClearTime;
		this.isAlarmClear = isAlarmClear;
		this.faultHandleDetail = faultHandleDetail;
		this.faultArea = faultArea;
		this.faultReason = faultReason;
		this.sideeffectService = sideeffectService;
		this.affectedServiceName = affectedServiceName;
		this.faultGenera = faultGenera;
	}

	// Property accessors

	public String getToId() {
		return this.toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public Date getForeseeSolveTime() {
		return this.foreseeSolveTime;
	}

	public void setForeseeSolveTime(Date foreseeSolveTime) {
		this.foreseeSolveTime = foreseeSolveTime;
	}

	public String getReasonForDelayApply() {
		return this.reasonForDelayApply;
	}

	public void setReasonForDelayApply(String reasonForDelayApply) {
		this.reasonForDelayApply = reasonForDelayApply;
	}

	public Date getFaultSolveTime() {
		return this.faultSolveTime;
	}

	public void setFaultSolveTime(Date faultSolveTime) {
		this.faultSolveTime = faultSolveTime;
	}

	public String getFaultHandleResult() {
		return this.faultHandleResult;
	}

	public void setFaultHandleResult(String faultHandleResult) {
		this.faultHandleResult = faultHandleResult;
	}

	public Date getAlarmClearTime() {
		return this.alarmClearTime;
	}

	public void setAlarmClearTime(Date alarmClearTime) {
		this.alarmClearTime = alarmClearTime;
	}

	
	public Integer getIsAlarmClear() {
		return isAlarmClear;
	}

	public void setIsAlarmClear(Integer isAlarmClear) {
		this.isAlarmClear = isAlarmClear;
	}

	public String getFaultHandleDetail() {
		return this.faultHandleDetail;
	}

	public void setFaultHandleDetail(String faultHandleDetail) {
		this.faultHandleDetail = faultHandleDetail;
	}

	public String getFaultArea() {
		return this.faultArea;
	}

	public void setFaultArea(String faultArea) {
		this.faultArea = faultArea;
	}

	public String getFaultReason() {
		return this.faultReason;
	}

	public void setFaultReason(String faultReason) {
		this.faultReason = faultReason;
	}

	public Integer getSideeffectService() {
		return this.sideeffectService;
	}

	public void setSideeffectService(Integer sideeffectService) {
		this.sideeffectService = sideeffectService;
	}

	public String getAffectedServiceName() {
		return this.affectedServiceName;
	}

	public void setAffectedServiceName(String affectedServiceName) {
		this.affectedServiceName = affectedServiceName;
	}

	public String getFaultGenera() {
		return this.faultGenera;
	}

	public void setFaultGenera(String faultGenera) {
		this.faultGenera = faultGenera;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}