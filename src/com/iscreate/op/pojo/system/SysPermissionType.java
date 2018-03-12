package com.iscreate.op.pojo.system;

import java.util.Date;
import java.util.Set;

/**
 * SysPermissionType entity. @author MyEclipse Persistence Tools
 */

public class SysPermissionType implements java.io.Serializable {

	// Fields

	private Long perTypeId;
	private String code;
	private String name;
	private Long parentId;
	private Date createtime;
	private Date updatetime;

	// Constructors

	/** default constructor */
	public SysPermissionType() {
	}

	/** minimal constructor */
	public SysPermissionType(String code) {
		this.code = code;
	}

	/** full constructor */
	public SysPermissionType(String code, String name, Long parentId,
			Date createtime, Date updatetime) {
		this.code = code;
		this.name = name;
		this.parentId = parentId;
		this.createtime = createtime;
		this.updatetime = updatetime;
	}

	// Property accessors

	public Long getPerTypeId() {
		return this.perTypeId;
	}

	public void setPerTypeId(Long perTypeId) {
		this.perTypeId = perTypeId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

}