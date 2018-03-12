package com.iscreate.op.pojo.report;

import java.util.Date;

public class ReportOrgStaffSkillMonth {
	private Long id;
	private Long orgId;
	private String skillName;
	private Long staffCount;
	private Date statisticalTime;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	public Long getStaffCount() {
		return staffCount;
	}
	public void setStaffCount(Long staffCount) {
		this.staffCount = staffCount;
	}
	public Date getStatisticalTime() {
		return statisticalTime;
	}
	public void setStatisticalTime(Date statisticalTime) {
		this.statisticalTime = statisticalTime;
	}
}
