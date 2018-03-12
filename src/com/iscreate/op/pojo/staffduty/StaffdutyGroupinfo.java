package com.iscreate.op.pojo.staffduty;
// default package

import java.util.Date;

/**
 * StaffdutyGroupinfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class StaffdutyGroupinfo implements java.io.Serializable {

	// Fields

	private Long id;
	private String groupName;
	private String orgId;
	private String telePhone;
	private String mobilePhone;
	private Integer isValid;
	private Date lastEditTime;

	// Constructors

	/** default constructor */
	public StaffdutyGroupinfo() {
	}

	/** minimal constructor */
	public StaffdutyGroupinfo(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	/** full constructor */
	public StaffdutyGroupinfo(String groupName, String orgId, String telePhone,
			String mobilePhone, Integer isValid, Date lastEditTime) {
		this.groupName = groupName;
		this.orgId = orgId;
		this.telePhone = telePhone;
		this.mobilePhone = mobilePhone;
		this.isValid = isValid;
		this.lastEditTime = lastEditTime;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTelePhone() {
		return this.telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Integer getIsValid() {
		return this.isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	public Date getLastEditTime() {
		return this.lastEditTime;
	}

	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

}