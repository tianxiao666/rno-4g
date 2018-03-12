package com.iscreate.op.pojo.rno;

import java.util.Date;

/**
 * RnoHandoverDescriptor entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RnoHandoverDescriptor implements java.io.Serializable {

	// Fields

	private Long rnoHandoverDescId;
	private Date staticsTime;
	private Long areaId;
	private Date createTime;
	private Date modTime;
	private String status;
	private String name;

	// Constructors

	/** default constructor */
	public RnoHandoverDescriptor() {
	}

	/** full constructor */
	public RnoHandoverDescriptor(Date staticsTime, Long areaId,
			Date createTime, Date modTime, String status) {
		this.staticsTime = staticsTime;
		this.areaId = areaId;
		this.createTime = createTime;
		this.modTime = modTime;
		this.status = status;
	}

	// Property accessors

	public Long getRnoHandoverDescId() {
		return this.rnoHandoverDescId;
	}

	public void setRnoHandoverDescId(Long rnoHandoverDescId) {
		this.rnoHandoverDescId = rnoHandoverDescId;
	}

	public Date getStaticsTime() {
		return this.staticsTime;
	}

	public void setStaticsTime(Date staticsTime) {
		this.staticsTime = staticsTime;
	}

	public Long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((rnoHandoverDescId == null) ? 0 : rnoHandoverDescId
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final RnoHandoverDescriptor other = (RnoHandoverDescriptor) obj;
		if (rnoHandoverDescId == null) {
			if (other.rnoHandoverDescId != null)
				return false;
		} else if (!rnoHandoverDescId.equals(other.rnoHandoverDescId))
			return false;
		return true;
	}

}