package com.iscreate.op.service.rno.parser.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.iscreate.op.service.rno.tool.DateUtil;

public class MrrAdmRecord extends MrrRecord {

	public MrrAdmRecord(){
		super.setRecType(30);
		super.setRecTypeName("Record ADMINISTRATIVE");
	}
	
	private int fileFormat;
	
	private int startYear;
	private int startMonth;
	private int startDay;
	private int startHour;
	private int startMinute;
	private int startSecond;
	
	private int recInfo;
	private String rid;
	
	private int totalTime;
	private int measureLimit;
	private int measureSign;
	
	private int measureInterval;
	private int measureType;
	
	private int measureLink;
	private int measureLimit2;
	private int measureLimit3;
	private int measureLimit4;
	
	private int connectionType;
	private int dtmFilter;
	
	//------获取开始时间----------//
	public Date getStartTime(DateUtil dateUtil){
		return dateUtil.to_yyyyMMddHHmmssDate("20"+startYear+"-"+startMonth+"-"+startDay+" "+startHour+":"+startMinute+":"+startSecond);
	}
	
	public int getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(int fileFormat) {
		this.fileFormat = fileFormat;
	}
	public int getStartYear() {
		return startYear;
	}
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}
	public int getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}
	public int getStartDay() {
		return startDay;
	}
	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}
	public int getStartHour() {
		return startHour;
	}
	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}
	public int getStartMinute() {
		return startMinute;
	}
	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}
	public int getStartSecond() {
		return startSecond;
	}
	public void setStartSecond(int startSecond) {
		this.startSecond = startSecond;
	}
	public int getRecInfo() {
		return recInfo;
	}
	public void setRecInfo(int recInfo) {
		this.recInfo = recInfo;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public int getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	public int getMeasureLimit() {
		return measureLimit;
	}
	public void setMeasureLimit(int measureLimit) {
		this.measureLimit = measureLimit;
	}
	public int getMeasureSign() {
		return measureSign;
	}
	public void setMeasureSign(int measureSign) {
		this.measureSign = measureSign;
	}
	public int getMeasureInterval() {
		return measureInterval;
	}
	public void setMeasureInterval(int measureInterval) {
		this.measureInterval = measureInterval;
	}
	public int getMeasureType() {
		return measureType;
	}
	public void setMeasureType(int measureType) {
		this.measureType = measureType;
	}
	public int getMeasureLink() {
		return measureLink;
	}
	public void setMeasureLink(int measureLink) {
		this.measureLink = measureLink;
	}
	public int getMeasureLimit2() {
		return measureLimit2;
	}
	public void setMeasureLimit2(int measureLimit2) {
		this.measureLimit2 = measureLimit2;
	}
	public int getMeasureLimit3() {
		return measureLimit3;
	}
	public void setMeasureLimit3(int measureLimit3) {
		this.measureLimit3 = measureLimit3;
	}
	public int getMeasureLimit4() {
		return measureLimit4;
	}
	public void setMeasureLimit4(int measureLimit4) {
		this.measureLimit4 = measureLimit4;
	}
	public int getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(int connectionType) {
		this.connectionType = connectionType;
	}
	public int getDtmFilter() {
		return dtmFilter;
	}
	public void setDtmFilter(int dtmFilter) {
		this.dtmFilter = dtmFilter;
	}
	@Override
	public String toString() {
		return "MrrAdmRecord [fileFormat=" + fileFormat + ", startYear="
				+ startYear + ", startMonth=" + startMonth + ", startDay="
				+ startDay + ", startHour=" + startHour + ", startMinute="
				+ startMinute + ", startSecond=" + startSecond + ", recInfo="
				+ recInfo + ", rid=" + rid + ", totalTime=" + totalTime
				+ ", measureLimit=" + measureLimit + ", measureSign="
				+ measureSign + ", measureInterval=" + measureInterval
				+ ", measureType=" + measureType + ", measureLink="
				+ measureLink + ", measureLimit2=" + measureLimit2
				+ ", measureLimit3=" + measureLimit3 + ", measureLimit4="
				+ measureLimit4 + ", connectionType=" + connectionType
				+ ", dtmFilter=" + dtmFilter + "]";
	}
	
	@Override
	public List<String> getFields() {
		List<String> result = new ArrayList<String>();

		result.add("fileFormat");
		result.add("startYear");
		result.add("startMonth");
		result.add("startDay");
		result.add("startHour");
		result.add("startMinute");
		result.add("startSecond");
		result.add("recInfo");
		result.add("rid");
		result.add("totalTime");
		result.add("measureLimit");
		result.add("measureSign");	
		result.add("measureInterval");
		result.add("measureType");
		result.add("measureLink");
		result.add("measureLimit2");
		result.add("measureLimit3");
		result.add("measureLimit4");
		result.add("connectionType");
		result.add("dtmFilter");
		return result;
	}
	
	@Override
	public String getFieldValues() {
		String result = "";

		result += this.getFileFormat() + "\t";
		result += this.getStartYear() + "\t";
		result += this.getStartMonth() + "\t";
		result += this.getStartDay() + "\t";
		result += this.getStartHour() + "\t";
		result += this.getStartMinute() + "\t";
		result += this.getStartSecond()+ "\t";
		result += this.getRecInfo() + "\t";
		result += this.getRid() + "\t";
		result += this.getTotalTime() + "\t";
		result += this.getMeasureLimit() + "\t";
		result += this.getMeasureSign() + "\t";
		result += this.getMeasureInterval() + "\t";
		result += this.getMeasureType() + "\t";
		result += this.getMeasureLimit2() + "\t";
		result += this.getMeasureLimit3() + "\t";
		result += this.getMeasureLimit4() + "\t";
		result += this.getConnectionType() + "\t";
		result += this.getDtmFilter() + "\t";
		result += "\n";
		return result;
	}
}
