package com.iscreate.op.service.rno.parser.vo;

import java.util.Date;

import com.iscreate.op.service.rno.tool.DateUtil;

public class EriFasAdmRecord extends EriFasRecord{

	private int fileFormat;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	private int recInfo;
	private String rid;
	private int totalTime;
	private int percentVal;
	

	public EriFasAdmRecord(){
		super.setRecType(40);
	}
	
	public int getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(int fileFormat) {
		this.fileFormat = fileFormat;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
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
	public int getPercentVal() {
		return percentVal;
	}
	public void setPercentVal(int percentVal) {
		this.percentVal = percentVal;
	}
	
	@Override
	public String toString() {
		return "FasAdmRecord [fileFormat=" + fileFormat + ", year=" + year
				+ ", month=" + month + ", day=" + day + ", hour=" + hour
				+ ", minute=" + minute + ", second=" + second + ", recInfo="
				+ recInfo + ", rid=" + rid + ", totalTime=" + totalTime
				+ ", percentVal=" + percentVal + "]";
	}
	
	public Date getMeaTime(DateUtil dateUtil) {
		return dateUtil.to_yyyyMMddHHmmssDate("20"+year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second);
	}
	
}
