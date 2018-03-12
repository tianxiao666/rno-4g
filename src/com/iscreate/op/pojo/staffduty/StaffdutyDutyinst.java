package com.iscreate.op.pojo.staffduty;
// default package

import java.util.Date;

/**
 * StaffdutyDutyinst entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class StaffdutyDutyinst implements java.io.Serializable {

	// Fields

	private Long id;
	private Long dutyTemplateId;
	private Date dutyBeginTime;
	private Date dutyEndTime;
	private Date relieveBeginTime;
	private Date relieveEndTime;
	private Date takeShiftsBeginTime;
	private Date takeShiftsEndTime;
	private String userId;
	private String onDutyAddress;
	private String onDutyLatLng;
	private String offDutyAddress;
	private String offDutyLatLng;

	// Constructors

	/** default constructor */
	public StaffdutyDutyinst() {
	}

	/** full constructor */
	public StaffdutyDutyinst(Long dutyTemplateId, Date dutyBeginTime,
			Date dutyEndTime, Date relieveBeginTime, Date relieveEndTime,
			Date takeShiftsBeginTime, Date takeShiftsEndTime, String userId,
			String onDutyAddress,String onDutyLatLng,String offDutyAddress,String offDutyLatLng) {
		this.dutyTemplateId = dutyTemplateId;
		this.dutyBeginTime = dutyBeginTime;
		this.dutyEndTime = dutyEndTime;
		this.relieveBeginTime = relieveBeginTime;
		this.relieveEndTime = relieveEndTime;
		this.takeShiftsBeginTime = takeShiftsBeginTime;
		this.takeShiftsEndTime = takeShiftsEndTime;
		this.userId = userId;
		this.onDutyAddress = onDutyAddress;
		this.onDutyLatLng = onDutyLatLng;
		this.offDutyAddress = offDutyAddress;
		this.offDutyLatLng = offDutyLatLng;
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

	public Date getDutyBeginTime() {
		return this.dutyBeginTime;
	}

	public void setDutyBeginTime(Date dutyBeginTime) {
		this.dutyBeginTime = dutyBeginTime;
	}

	public Date getDutyEndTime() {
		return this.dutyEndTime;
	}

	public void setDutyEndTime(Date dutyEndTime) {
		this.dutyEndTime = dutyEndTime;
	}

	public Date getRelieveBeginTime() {
		return this.relieveBeginTime;
	}

	public void setRelieveBeginTime(Date relieveBeginTime) {
		this.relieveBeginTime = relieveBeginTime;
	}

	public Date getRelieveEndTime() {
		return this.relieveEndTime;
	}

	public void setRelieveEndTime(Date relieveEndTime) {
		this.relieveEndTime = relieveEndTime;
	}

	public Date getTakeShiftsBeginTime() {
		return this.takeShiftsBeginTime;
	}

	public void setTakeShiftsBeginTime(Date takeShiftsBeginTime) {
		this.takeShiftsBeginTime = takeShiftsBeginTime;
	}

	public Date getTakeShiftsEndTime() {
		return this.takeShiftsEndTime;
	}

	public void setTakeShiftsEndTime(Date takeShiftsEndTime) {
		this.takeShiftsEndTime = takeShiftsEndTime;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOnDutyAddress() {
		return onDutyAddress;
	}

	public void setOnDutyAddress(String onDutyAddress) {
		this.onDutyAddress = onDutyAddress;
	}

	public String getOnDutyLatLng() {
		return onDutyLatLng;
	}

	public void setOnDutyLatLng(String onDutyLatLng) {
		this.onDutyLatLng = onDutyLatLng;
	}

	public String getOffDutyAddress() {
		return offDutyAddress;
	}

	public void setOffDutyAddress(String offDutyAddress) {
		this.offDutyAddress = offDutyAddress;
	}

	public String getOffDutyLatLng() {
		return offDutyLatLng;
	}

	public void setOffDutyLatLng(String offDutyLatLng) {
		this.offDutyLatLng = offDutyLatLng;
	}
	
}