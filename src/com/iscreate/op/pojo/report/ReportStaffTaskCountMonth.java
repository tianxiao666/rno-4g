package com.iscreate.op.pojo.report;

import java.util.Date;

public class ReportStaffTaskCountMonth {
	private Long id;
	private String staffAccount;
	private Long taskCount;
	private Long orgId;
	private Date statisticalTime;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStaffAccount() {
		return staffAccount;
	}
	public void setStaffAccount(String staffAccount) {
		this.staffAccount = staffAccount;
	}
	public Long getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(Long taskCount) {
		this.taskCount = taskCount;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Date getStatisticalTime() {
		return statisticalTime;
	}
	public void setStatisticalTime(Date statisticalTime) {
		this.statisticalTime = statisticalTime;
	}
}
