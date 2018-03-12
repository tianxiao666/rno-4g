package com.iscreate.op.service.rno.parser.vo;

import java.util.Date;

import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;

public class NcsAdmRecord extends NcsRecord{

	
	private int recType;
	private int length;
	private int fileFormat;
	private String year;
	private String month;
	private String day;
	private String hour;
	private String minute;
	private String second;
	private int recordInfo;
	private String rid;
	private String startDateYear;
	private String startMonth;
	private String startDay;
	private String startHour;
	private String startMinute;
	private String startSecond;
	
	private int abss;
	private int relssSign;//0:positive 1:negative
	private int relss;
	private int relss2Sign;
	private int relss2;
	private int relss3Sign;
	private int relss3;
	private int relss4Sign;
	private int relss4;
	private int relss5Sign;
	private int relss5;
	
	private int ncellType;
	private int numFreq;
	private int segTime;//The length of each recording segment in minutes
	
	private int termReason;
	private int recTime;
	private int ecnoAbss;
	private int nUcellType;
	
	private int tfddMrr;
	private int numUmfi;
	private int tnccpermValIndi;
	private int tnccpermBitmap;
	private int tmbcr;
	
	//------获取开始时间----------//
	public Date getStartTime(DateUtil dateUtil){
		return dateUtil.to_yyyyMMddHHmmssDate("20"+startDateYear+"-"+startMonth+"-"+startDay+" "+startHour+":"+startMinute+":"+startSecond);
	}
	
	//---获取定义的门限---//
	public String getRelssValueName(int val){
		int[] signs=new int[]{relssSign,relss2Sign,relss3Sign,relss4Sign,relss5Sign};
		int[] vals=new int[]{relss,relss2,relss3,relss4,relss5};
		String[] names=new String[]{"relss","relss2","relss3","relss4","relss5"};
		int needSign=0;
		if(val<0){
			needSign=1;
		}
		for(int i=0;i<signs.length;i++){
			if(signs[i]==needSign){
				return names[i];
			}
		}
		return "";
	}
	
	public int getRecType() {
		return recType;
	}
	public void setRecType(int recType) {
		this.recType = recType;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getFileFormat() {
		return fileFormat;
	}
	public void setFileFormat(int fileFormat) {
		this.fileFormat = fileFormat;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	public String getSecond() {
		return second;
	}
	public void setSecond(String second) {
		this.second = second;
	}
	public int getRecordInfo() {
		return recordInfo;
	}
	public void setRecordInfo(int recordInfo) {
		this.recordInfo = recordInfo;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getStartDateYear() {
		return startDateYear;
	}
	public void setStartDateYear(String startDateYear) {
		this.startDateYear = startDateYear;
	}
	public String getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}
	public String getStartHour() {
		return startHour;
	}
	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}
	public String getStartMinute() {
		return startMinute;
	}
	public void setStartMinute(String startMinute) {
		this.startMinute = startMinute;
	}
	public String getStartSecond() {
		return startSecond;
	}
	public void setStartSecond(String startSecond) {
		this.startSecond = startSecond;
	}
	public int getAbss() {
		return abss;
	}
	public void setAbss(int abss) {
		this.abss = abss;
	}
	public int getRelssSign() {
		return relssSign;
	}
	public void setRelssSign(int relssSign) {
		this.relssSign = relssSign;
	}
	public int getRelss() {
		return relss;
	}
	public void setRelss(int relss) {
		this.relss = relss;
	}
	public int getRelss2Sign() {
		return relss2Sign;
	}
	public void setRelss2Sign(int relss2Sign) {
		this.relss2Sign = relss2Sign;
	}
	public int getRelss2() {
		return relss2;
	}
	public void setRelss2(int relss2) {
		this.relss2 = relss2;
	}
	public int getRelss3Sign() {
		return relss3Sign;
	}
	public void setRelss3Sign(int relss3Sign) {
		this.relss3Sign = relss3Sign;
	}
	public int getRelss3() {
		return relss3;
	}
	public void setRelss3(int relss3) {
		this.relss3 = relss3;
	}
	public int getRelss4Sign() {
		return relss4Sign;
	}
	public void setRelss4Sign(int relss4Sign) {
		this.relss4Sign = relss4Sign;
	}
	public int getRelss4() {
		return relss4;
	}
	public void setRelss4(int relss4) {
		this.relss4 = relss4;
	}
	public int getRelss5Sign() {
		return relss5Sign;
	}
	public void setRelss5Sign(int relss5Sign) {
		this.relss5Sign = relss5Sign;
	}
	public int getRelss5() {
		return relss5;
	}
	public void setRelss5(int relss5) {
		this.relss5 = relss5;
	}
	public int getNcellType() {
		return ncellType;
	}
	public void setNcellType(int ncellType) {
		this.ncellType = ncellType;
	}
	public int getNumFreq() {
		return numFreq;
	}
	public void setNumFreq(int numFreq) {
		this.numFreq = numFreq;
	}
	public int getSegTime() {
		return segTime;
	}
	public void setSegTime(int segTime) {
		this.segTime = segTime;
	}
	public int getTermReason() {
		return termReason;
	}
	public void setTermReason(int termReason) {
		this.termReason = termReason;
	}
	public int getRecTime() {
		return recTime;
	}
	public void setRecTime(int recTime) {
		this.recTime = recTime;
	}
	public int getEcnoAbss() {
		return ecnoAbss;
	}
	public void setEcnoAbss(int ecnoAbss) {
		this.ecnoAbss = ecnoAbss;
	}
	public int getNUcellType() {
		return nUcellType;
	}
	public void setNUcellType(int ucellType) {
		nUcellType = ucellType;
	}
	public int getTfddMrr() {
		return tfddMrr;
	}
	public void setTfddMrr(int tfddMrr) {
		this.tfddMrr = tfddMrr;
	}
	public int getNumUmfi() {
		return numUmfi;
	}
	public void setNumUmfi(int numUmfi) {
		this.numUmfi = numUmfi;
	}
	public int getTnccpermValIndi() {
		return tnccpermValIndi;
	}
	public void setTnccpermValIndi(int tnccpermValIndi) {
		this.tnccpermValIndi = tnccpermValIndi;
	}
	public int getTnccpermBitmap() {
		return tnccpermBitmap;
	}
	public void setTnccpermBitmap(int tnccpermBitmap) {
		this.tnccpermBitmap = tnccpermBitmap;
	}
	public int getTmbcr() {
		return tmbcr;
	}
	public void setTmbcr(int tmbcr) {
		this.tmbcr = tmbcr;
	}
	@Override
	public String toString() {
		return "NcsAdminRecord [recType=" + recType + ", length=" + length
				+ ", fileFormat=" + fileFormat + ", year=" + year + ", month="
				+ month + ", day=" + day + ", hour=" + hour + ", minute="
				+ minute + ", second=" + second + ", recordInfo=" + recordInfo
				+ ", rid=" + rid + ", startDateYear=" + startDateYear + ", startMonth="
				+ startMonth + ", startDay=" + startDay + ", startHour="
				+ startHour + ", startMinute=" + startMinute + ", startSecond="
				+ startSecond + ", abss=" + abss + ", relssSign=" + relssSign
				+ ", relss=" + relss + ", relss2Sign=" + relss2Sign
				+ ", relss2=" + relss2 + ", relss3Sign=" + relss3Sign
				+ ", relss3=" + relss3 + ", relss4Sign=" + relss4Sign
				+ ", relss4=" + relss4 + ", relss5Sign=" + relss5Sign
				+ ", relss5=" + relss5 + ", ncellType=" + ncellType
				+ ", numFreq=" + numFreq + ", segTime=" + segTime
				+ ", termReason=" + termReason + ", recTime=" + recTime
				+ ", ecnoAbss=" + ecnoAbss + ", nUcellType=" + nUcellType
				+ ", tfddMrr=" + tfddMrr + ", numUmfi=" + numUmfi
				+ ", tnccpermValIndi=" + tnccpermValIndi + ", tnccpermBitmap="
				+ tnccpermBitmap + ", tmbcr=" + tmbcr + "]";
	}
	
	
	
	
}
