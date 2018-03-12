package com.iscreate.op.pojo.rno;

import java.util.Date;

/**
 * RnoStsDescriptor entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RnoStsDescriptor implements java.io.Serializable {

	// Fields

	private Long stsDescId;
	private String netType;
	private String specType;
	private Date stsDate;
	private String stsPeriod;
	private Date createTime;
	private Date modTime;
	private String status;
	private Long areaId;

	// Constructors

	/** default constructor */
	public RnoStsDescriptor() {
	}

	/** minimal constructor */
	public RnoStsDescriptor(String netType, String specType, Date stsDate,
			String stsPeriod, String status) {
		this.netType = netType;
		this.specType = specType;
		this.stsDate = stsDate;
		this.stsPeriod = stsPeriod;
		this.status = status;
	}

	/** full constructor */
	public RnoStsDescriptor(Long stsDescId, String netType, String specType,
			Date stsDate, String stsPeriod, Date createTime, Date modTime,
			String status, Long areaId) {
		super();
		this.stsDescId = stsDescId;
		this.netType = netType;
		this.specType = specType;
		this.stsDate = stsDate;
		this.stsPeriod = stsPeriod;
		this.createTime = createTime;
		this.modTime = modTime;
		this.status = status;
		this.areaId = areaId;
	}

	public Long getStsDescId() {
		return stsDescId;
	}

	public void setStsDescId(Long stsDescId) {
		this.stsDescId = stsDescId;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getSpecType() {
		return specType;
	}

	public void setSpecType(String specType) {
		this.specType = specType;
	}

	public Date getStsDate() {
		return stsDate;
	}

	public void setStsDate(Date stsDate) {
		this.stsDate = stsDate;
	}

	public String getStsPeriod() {
		return stsPeriod;
	}

	public void setStsPeriod(String stsPeriod) {
		this.stsPeriod = stsPeriod;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModTime() {
		return modTime;
	}

	public void setModTime(Date modTime) {
		this.modTime = modTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	
	

}