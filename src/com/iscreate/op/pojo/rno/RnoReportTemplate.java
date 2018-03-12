package com.iscreate.op.pojo.rno;

import java.util.Date;

public class RnoReportTemplate {

	private Long id;
	private String reportName;
	private String tableName;
	private String creator;
	private String status;
	private Date createTime;
	private Date modTime;
	private String applyScope;
	private String scopeValue;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getApplyScope() {
		return applyScope;
	}
	public void setApplyScope(String applyScope) {
		this.applyScope = applyScope;
	}
	public String getScopeValue() {
		return scopeValue;
	}
	public void setScopeValue(String scopeValue) {
		this.scopeValue = scopeValue;
	}
	public RnoReportTemplate(Long id, String reportName, String tableName,
			String creator, String status, Date createTime, Date modTime,
			String applyScope, String scopeValue) {
		super();
		this.id = id;
		this.reportName = reportName;
		this.tableName = tableName;
		this.creator = creator;
		this.status = status;
		this.createTime = createTime;
		this.modTime = modTime;
		this.applyScope = applyScope;
		this.scopeValue = scopeValue;
	}
	public RnoReportTemplate() {
		super();
	}
	@Override
	public String toString() {
		return "RnoReportTemplate [id=" + id + ", reportName=" + reportName
				+ ", tableName=" + tableName + ", creator=" + creator
				+ ", status=" + status + ", createTime=" + createTime
				+ ", modTime=" + modTime + ", applyScope=" + applyScope
				+ ", scopeValue=" + scopeValue + "]";
	}
	
	

}
