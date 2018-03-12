package com.iscreate.op.pojo.system;

import java.math.BigDecimal;
import java.util.Date;

/**
 * SysOrg entity. @author MyEclipse Persistence Tools
 */

public class SysOrg implements java.io.Serializable {

	// Fields

	private Long orgId;
	private String name;
	private String tel;
	private String mobile;
	private String email;
	private String address;
	private String description;
	private Long parentId;
	private String path;
	private Long orgTypeId;
	private Long businessTypeId;
	private Integer status;
	private Long orgUserId;
	private String orgUserTel;
	private Double longitude;
	private Double latitude;
	private Date createtime;
	private Date updatetime;
	private Long enterpriseId;
	private String orgType;//组织类型

	// Constructors

	/** default constructor */
	public SysOrg() {
	}

	/** minimal constructor */
	public SysOrg(String name, Long orgTypeId, Long businessTypeId,
			Integer status) {
		this.name = name;
		this.orgTypeId = orgTypeId;
		this.businessTypeId = businessTypeId;
		this.status = status;
	}

	/** full constructor */
	public SysOrg(String name, String tel, String mobile, String email,
			String address, String description, Long parentId, String path,
			Long orgTypeId, Long businessTypeId, Integer status,
			Long orgUserId, String orgUserTel, Double longitude,
			Double latitude, Date createtime, Date updatetime, Long enterpriseId) {
		this.name = name;
		this.tel = tel;
		this.mobile = mobile;
		this.email = email;
		this.address = address;
		this.description = description;
		this.parentId = parentId;
		this.path = path;
		this.orgTypeId = orgTypeId;
		this.businessTypeId = businessTypeId;
		this.status = status;
		this.orgUserId = orgUserId;
		this.orgUserTel = orgUserTel;
		this.longitude = longitude;
		this.latitude = latitude;
		this.createtime = createtime;
		this.updatetime = updatetime;
		this.enterpriseId = enterpriseId;
	}

	// Property accessors

	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getOrgTypeId() {
		return this.orgTypeId;
	}

	public void setOrgTypeId(Long orgTypeId) {
		this.orgTypeId = orgTypeId;
	}

	public Long getBusinessTypeId() {
		return this.businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getOrgUserId() {
		return this.orgUserId;
	}

	public void setOrgUserId(Long orgUserId) {
		this.orgUserId = orgUserId;
	}

	public String getOrgUserTel() {
		return this.orgUserTel;
	}

	public void setOrgUserTel(String orgUserTel) {
		this.orgUserTel = orgUserTel;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
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

	public Long getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	
}