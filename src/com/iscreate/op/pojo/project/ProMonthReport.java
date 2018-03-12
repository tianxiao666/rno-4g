package com.iscreate.op.pojo.project;

import java.util.Date;

public class ProMonthReport {
	private Long proMonthReportId;
	private Long proId;		
	private String status;	
	private Date month;	
	private String totalWeekValueMoney;		
	private String totalWeekCostMoney;	
	private String initialMoney;
	private String finalMoney;
	private String finishMoney;
	private String totalValueMoney;
	private Long createUserId;
	private Date createTime;
	private Date modTime;
	
	
	public Long getProMonthReportId() {
		return proMonthReportId;
	}
	public void setProMonthReportId(Long proMonthReportId) {
		this.proMonthReportId = proMonthReportId;
	}
	public Long getProId() {
		return proId;
	}
	public void setProId(Long proId) {
		this.proId = proId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getMonth() {
		return month;
	}
	public void setMonth(Date month) {
		this.month = month;
	}
	public String getTotalWeekValueMoney() {
		return totalWeekValueMoney;
	}
	public void setTotalWeekValueMoney(String totalWeekValueMoney) {
		this.totalWeekValueMoney = totalWeekValueMoney;
	}
	public String getTotalWeekCostMoney() {
		return totalWeekCostMoney;
	}
	public void setTotalWeekCostMoney(String totalWeekCostMoney) {
		this.totalWeekCostMoney = totalWeekCostMoney;
	}
	public String getInitialMoney() {
		return initialMoney;
	}
	public void setInitialMoney(String initialMoney) {
		this.initialMoney = initialMoney;
	}
	public String getFinalMoney() {
		return finalMoney;
	}
	public void setFinalMoney(String finalMoney) {
		this.finalMoney = finalMoney;
	}
	public String getFinishMoney() {
		return finishMoney;
	}
	public void setFinishMoney(String finishMoney) {
		this.finishMoney = finishMoney;
	}
	public String getTotalValueMoney() {
		return totalValueMoney;
	}
	public void setTotalValueMoney(String totalValueMoney) {
		this.totalValueMoney = totalValueMoney;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
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
	
	
	
}
