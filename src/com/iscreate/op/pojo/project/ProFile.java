package com.iscreate.op.pojo.project;


import java.util.Date;

/**
 * ProFile entity. @author MyEclipse Persistence Tools
 */

public class ProFile implements java.io.Serializable {

	// Fields

	private Long fileId;
	private String relaServType;
	private Long relaServId;
	private String status;
	private String fileName;
	private String path;
	private Long createUserId;
	private Date createTime;
	private Date modTime;

	// Constructors

	/** default constructor */
	public ProFile() {
	}

	/** minimal constructor */
	public ProFile(String relaServType, Long relaServId) {
		this.relaServType = relaServType;
		this.relaServId = relaServId;
	}

	/** full constructor */
	public ProFile(String relaServType, Long relaServId, String status,
			String fileName, String path, Long createUserId,
			Date createTime, Date modTime) {
		this.relaServType = relaServType;
		this.relaServId = relaServId;
		this.status = status;
		this.fileName = fileName;
		this.path = path;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.modTime = modTime;
	}

	// Property accessors

	
	public String getRelaServType() {
		return this.relaServType;
	}

	public void setRelaServType(String relaServType) {
		this.relaServType = relaServType;
	}

	

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Long getRelaServId() {
		return relaServId;
	}

	public void setRelaServId(Long relaServId) {
		this.relaServId = relaServId;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
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