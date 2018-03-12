package com.iscreate.op.pojo.system;

import java.math.BigDecimal;
import java.util.Date;

/**
 * SysOrgUser entity. @author MyEclipse Persistence Tools
 */

public class SysOrgUser implements java.io.Serializable {

	// Fields

	private Long orgUserId;
	private String name;
	private String tel;
	private String mobile;
	private String email;
	private String address;
	private String description;
	private String gender;
	private Date birthday;
	private String idcard;
	private Integer status;
	private Date createtime;
	private Date updatetime;
	private Long enterpriseId;
	private String mobileemail;
	private String backupemail;

	// Constructors

	/** default constructor */
	public SysOrgUser() {
	}

	/** minimal constructor */
	public SysOrgUser(String name, Integer status) {
		this.name = name;
		this.status = status;
	}

	/** full constructor */
	public SysOrgUser(String name, String tel, String mobile, String email,
			String address, String description, String gender, Date birthday,
			String idcard, Integer status, Date createtime, Date updatetime,
			Long enterpriseId, String mobileemail, String backupemail) {
		this.name = name;
		this.tel = tel;
		this.mobile = mobile;
		this.email = email;
		this.address = address;
		this.description = description;
		this.gender = gender;
		this.birthday = birthday;
		this.idcard = idcard;
		this.status = status;
		this.createtime = createtime;
		this.updatetime = updatetime;
		this.enterpriseId = enterpriseId;
		this.mobileemail = mobileemail;
		this.backupemail = backupemail;
	}

	// Property accessors

	public Long getOrgUserId() {
		return this.orgUserId;
	}

	public void setOrgUserId(Long orgUserId) {
		this.orgUserId = orgUserId;
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

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdcard() {
		return this.idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getMobileemail() {
		return this.mobileemail;
	}

	public void setMobileemail(String mobileemail) {
		this.mobileemail = mobileemail;
	}

	public String getBackupemail() {
		return this.backupemail;
	}

	public void setBackupemail(String backupemail) {
		this.backupemail = backupemail;
	}

}