package com.iscreate.op.pojo.rno;

import java.util.Date;

/**
 * RnoCellDescriptor entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RnoCellDescriptor implements java.io.Serializable {

	// Fields

	private Long cellDescriptorId;
	private String name;
	private String tempStorage;
	private String defaultDescriptor;
	private Date createTime;
	private Date modTime;
	private String status;
	private Long areaId;

	// Constructors

	/** default constructor */
	public RnoCellDescriptor() {
	}

	/** minimal constructor */
	public RnoCellDescriptor(String name, String tempStorage,
			String defaultDescriptor, Date createTime, String status,Long areaId) {
		this.name = name;
		this.tempStorage = tempStorage;
		this.defaultDescriptor = defaultDescriptor;
		this.createTime = createTime;
		this.status = status;
		this.areaId=areaId;
	}

	/** full constructor */
	public RnoCellDescriptor(String name, String tempStorage,
			String defaultDescriptor, Date createTime, Date modTime,
			String status,Long areaId) {
		this.name = name;
		this.tempStorage = tempStorage;
		this.defaultDescriptor = defaultDescriptor;
		this.createTime = createTime;
		this.modTime = modTime;
		this.status = status;
		this.areaId=areaId;
	}

	// Property accessors

	public Long getCellDescriptorId() {
		return this.cellDescriptorId;
	}

	public void setCellDescriptorId(Long cellDescriptorId) {
		this.cellDescriptorId = cellDescriptorId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTempStorage() {
		return this.tempStorage;
	}

	public void setTempStorage(String tempStorage) {
		this.tempStorage = tempStorage;
	}

	public String getDefaultDescriptor() {
		return this.defaultDescriptor;
	}

	public void setDefaultDescriptor(String defaultDescriptor) {
		this.defaultDescriptor = defaultDescriptor;
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
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

}