package com.iscreate.op.pojo.report;

import java.util.Date;

public class ReportComment {
	private Long id;
	private Long reportmessageid;
	private String criticsid;
	private String criticsname;
	private Date criticstime;
	private String content;
	public Long getId() {
		return id;
	}

	public Long getReportmessageid() {
		return reportmessageid;
	}
	public void setReportmessageid(Long reportmessageid) {
		this.reportmessageid = reportmessageid;
	}
	public String getCriticsid() {
		return criticsid;
	}
	public void setCriticsid(String criticsid) {
		this.criticsid = criticsid;
	}
	public String getCriticsname() {
		return criticsname;
	}
	public void setCriticsname(String criticsname) {
		this.criticsname = criticsname;
	}
	public Date getCriticstime() {
		return criticstime;
	}
	public void setCriticstime(Date criticstime) {
		this.criticstime = criticstime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
