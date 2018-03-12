package com.iscreate.op.pojo.system;

import java.math.BigDecimal;
import java.util.Date;

/**
 * SysOrgType entity. @author MyEclipse Persistence Tools
 */

public class SysOrgType implements java.io.Serializable {

	// Fields

	private Long orgTypeId;
	private String code;
	private String name;
	private Long parentId;
	private Date createtime;
	private Date updatetime;
	private Integer status;

	// Constructors

	/** default constructor */
	public SysOrgType() {
	}

	/** minimal constructor */
	public SysOrgType(String code) {
		this.code = code;
	}

	/** full constructor */
	public SysOrgType(String code, String name, Long parentId, Date createtime,
			Date updatetime, Integer status) {
		this.code = code;
		this.name = name;
		this.parentId = parentId;
		this.createtime = createtime;
		this.updatetime = updatetime;
		this.status = status;
	}

	// Property accessors

	public Long getOrgTypeId() {
		return this.orgTypeId;
	}

	public void setOrgTypeId(Long orgTypeId) {
		this.orgTypeId = orgTypeId;
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

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
}