package com.iscreate.op.pojo.rno;

import java.util.Date;

/**
 * RnoTile entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RnoTile implements java.io.Serializable {

	// Fields

	private Long tileId;
	private String name;
	private Long isSystemTile;
	private Long canDelete;
	private Long accountId;
	private Date createTime;
	private Date modTime;

	// Constructors

	/** default constructor */
	public RnoTile() {
	}

	/** full constructor */
	public RnoTile(String name, Long isSystemTile, Long canDelete,
			Long accountId, Date createTime, Date modTime) {
		this.name = name;
		this.isSystemTile = isSystemTile;
		this.canDelete = canDelete;
		this.accountId = accountId;
		this.createTime = createTime;
		this.modTime = modTime;
	}

	// Property accessors

	public Long getTileId() {
		return this.tileId;
	}

	public void setTileId(Long tileId) {
		this.tileId = tileId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getIsSystemTile() {
		return this.isSystemTile;
	}

	public void setIsSystemTile(Long isSystemTile) {
		this.isSystemTile = isSystemTile;
	}

	public Long getCanDelete() {
		return this.canDelete;
	}

	public void setCanDelete(Long canDelete) {
		this.canDelete = canDelete;
	}

	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
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

}