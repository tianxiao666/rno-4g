package com.iscreate.op.pojo.rno;

import java.util.Date;

/**
 * RnoDtDescriptor entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RnoDtDescriptor implements java.io.Serializable {

	// Fields

	private Long dtDescId;
	private String name;
	private Date testDate;
	private Date createTime;
	private Date modTime;
	private String status;
	private Long areaId;
	private String dtlogVersion;
	private String vendor;
	private String device;
	private String version;
	private String type;
	private String netMode;

	// Constructors

	/** default constructor */
	public RnoDtDescriptor() {
	}

	/** full constructor */
	public RnoDtDescriptor(String name, Date testDate, Date createTime,
			Date modTime, String status, Long areaId, String dtlogVersion,
			String vendor, String device, String version, String type,
			String netMode) {
		this.name = name;
		this.testDate = testDate;
		this.createTime = createTime;
		this.modTime = modTime;
		this.status = status;
		this.areaId = areaId;
		this.dtlogVersion = dtlogVersion;
		this.vendor = vendor;
		this.device = device;
		this.version = version;
		this.type = type;
		this.netMode = netMode;
	}

	// Property accessors

	public Long getDtDescId() {
		return this.dtDescId;
	}

	public void setDtDescId(Long dtDescId) {
		this.dtDescId = dtDescId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTestDate() {
		return this.testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModTime() {
		return this.modTime;
	}

	public void setModTime(Date modTime) {
		this.modTime = modTime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getDtlogVersion() {
		return this.dtlogVersion;
	}

	public void setDtlogVersion(String dtlogVersion) {
		this.dtlogVersion = dtlogVersion;
	}

	public String getVendor() {
		return this.vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDevice() {
		return this.device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNetMode() {
		return this.netMode;
	}

	public void setNetMode(String netMode) {
		this.netMode = netMode;
	}

	@Override
	public String toString() {
		return "RnoDtDescriptor [dtDescId=" + dtDescId + ", name=" + name
				+ ", testDate=" + testDate + ", createTime=" + createTime
				+ ", modTime=" + modTime + ", status=" + status + ", areaId="
				+ areaId + ", dtlogVersion=" + dtlogVersion + ", vendor="
				+ vendor + ", device=" + device + ", version=" + version
				+ ", type=" + type + ", netMode=" + netMode + "]";
	}

}