package com.iscreate.op.pojo.system;

import java.util.Date;

/**
 * SysAccount entity. @author MyEclipse Persistence Tools
 */

public class SysAccount implements java.io.Serializable {

	// Fields

	private Long accountId;
	private String account;
	private String password;
	private Long orgUserId;
	private Date lastLoginTime;
	private String description;
	private Date createtime;
	private Date updatetime;

	// Constructors

	/** default constructor */
	public SysAccount() {
	}

	/** minimal constructor */
	public SysAccount(String account, String password, Long orgUserId) {
		this.account = account;
		this.password = password;
		this.orgUserId = orgUserId;
	}

	/** full constructor */
	public SysAccount(String account, String password, Long orgUserId,
			 Date lastLoginTime, String description,
			Date createtime, Date updatetime) {
		this.account = account;
		this.password = password;
		this.orgUserId = orgUserId;
		this.lastLoginTime = lastLoginTime;
		this.description = description;
		this.createtime = createtime;
		this.updatetime = updatetime;
	}

	// Property accessors

	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getOrgUserId() {
		return this.orgUserId;
	}

	public void setOrgUserId(Long orgUserId) {
		this.orgUserId = orgUserId;
	}


	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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