package com.iscreate.op.action.rno;

import java.util.Date;
/**
 * 
 * @filename: StsCodition.java
 * @classpath: com.iscreate.op.action.rno
 * @description: 话务性能查询条件 vo
 * @author：
 * @date：Oct 10, 2013 12:20:03 PM
 * @version：
 */
public class StsCondition {
	private Date stsDate;
	private String stsPeriod;
	private Long areaId;
	private String engName;
	private String cell;
	private String cellChineseName;
	private Date staticTime;
	private String grade;
	
	private Long cityId;
	private Date beginTime;
	private Date latestAllowedTime;
	private String stsDateStr;
	private String stsType;
	
	public String getStsType() {
		return stsType;
	}

	public void setStsType(String stsType) {
		this.stsType = stsType;
	}

	public String toString() {
		return "areaId=" + areaId + ",stsDate=" + stsDate + ",stsPeriod=" + stsPeriod
				+ ",areaId=" + areaId + ",engName=" + engName + ",cell="
				+ cell + ",cellChineseName=" + cellChineseName+ ",grade=" + grade+ ",staticTime=" + staticTime+ ",cityId=" + cityId+ ",beginTime=" + beginTime+ ",latestAllowedTime=" + latestAllowedTime;
	}
	
	//getter and setter
	public Date getStsDate() {
		return stsDate;
	}
	public void setStsDate(Date stsDate) {
		this.stsDate = stsDate;
	}
	public String getStsPeriod() {
		return stsPeriod;
	}
	public void setStsPeriod(String stsPeriod) {
		this.stsPeriod = stsPeriod;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public String getEngName() {
		return engName;
	}
	public void setEngName(String engName) {
		this.engName = engName;
	}
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	public String getCellChineseName() {
		return cellChineseName;
	}
	public void setCellChineseName(String cellChineseName) {
		this.cellChineseName = cellChineseName;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Date getStaticTime() {
		return staticTime;
	}
	public void setStaticTime(Date staticTime) {
		this.staticTime = staticTime;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getLatestAllowedTime() {
		return latestAllowedTime;
	}

	public void setLatestAllowedTime(Date latestAllowedTime) {
		this.latestAllowedTime = latestAllowedTime;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getStsDateStr() {
		return stsDateStr;
	}

	public void setStsDateStr(String stsDateStr) {
		this.stsDateStr = stsDateStr;
	}
	
	
}
