package com.iscreate.op.pojo.staffduty;
// default package

import java.util.Date;

/**
 * StaffdutyDutytemplate entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class StaffdutyDutytemplate implements java.io.Serializable {

	// Fields

	private Long id;
	private Long frequencyId;
	private Date dutyDate;
	private String userId;

	// Constructors

	/** default constructor */
	public StaffdutyDutytemplate() {
	}

	/** full constructor */
	public StaffdutyDutytemplate(Long frequencyId, Date dutyDate, String userId) {
		this.frequencyId = frequencyId;
		this.dutyDate = dutyDate;
		this.userId = userId;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFrequencyId() {
		return this.frequencyId;
	}

	public void setFrequencyId(Long frequencyId) {
		this.frequencyId = frequencyId;
	}

	public Date getDutyDate() {
		return this.dutyDate;
	}

	public void setDutyDate(Date dutyDate) {
		this.dutyDate = dutyDate;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}