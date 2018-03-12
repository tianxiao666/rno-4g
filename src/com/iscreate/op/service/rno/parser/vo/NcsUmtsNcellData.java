package com.iscreate.op.service.rno.parser.vo;

public class NcsUmtsNcellData extends NcsRecord{

	private int recType=54;
	private int length=51;
	private String cellName;
	private int mfddArfcn;
	private int mscrCode;
	private int diversity;
	private int definedAsNcell;
	private int recTimeUmfi;
	private int repUmfi;
	private int uTimes;
	private int aveCno;
	private int uTimes1;
	private int aveCno1;
	private int uTmies2;
	private int aveCno2;
	private int uTimes3;
	private int aveCno3;
	private int UtimesEcnoAbss;
	private int uTmiesAlone;
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
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public int getMfddArfcn() {
		return mfddArfcn;
	}
	public void setMfddArfcn(int mfddArfcn) {
		this.mfddArfcn = mfddArfcn;
	}
	public int getMscrCode() {
		return mscrCode;
	}
	public void setMscrCode(int mscrCode) {
		this.mscrCode = mscrCode;
	}
	public int getDiversity() {
		return diversity;
	}
	public void setDiversity(int diversity) {
		this.diversity = diversity;
	}
	public int getDefinedAsNcell() {
		return definedAsNcell;
	}
	public void setDefinedAsNcell(int definedAsNcell) {
		this.definedAsNcell = definedAsNcell;
	}
	public int getRecTimeUmfi() {
		return recTimeUmfi;
	}
	public void setRecTimeUmfi(int recTimeUmfi) {
		this.recTimeUmfi = recTimeUmfi;
	}
	public int getRepUmfi() {
		return repUmfi;
	}
	public void setRepUmfi(int repUmfi) {
		this.repUmfi = repUmfi;
	}
	public int getuTimes() {
		return uTimes;
	}
	public void setuTimes(int uTimes) {
		this.uTimes = uTimes;
	}
	public int getAveCno() {
		return aveCno;
	}
	public void setAveCno(int aveCno) {
		this.aveCno = aveCno;
	}
	public int getuTimes1() {
		return uTimes1;
	}
	public void setuTimes1(int uTimes1) {
		this.uTimes1 = uTimes1;
	}
	public int getAveCno1() {
		return aveCno1;
	}
	public void setAveCno1(int aveCno1) {
		this.aveCno1 = aveCno1;
	}
	public int getuTmies2() {
		return uTmies2;
	}
	public void setuTmies2(int uTmies2) {
		this.uTmies2 = uTmies2;
	}
	public int getAveCno2() {
		return aveCno2;
	}
	public void setAveCno2(int aveCno2) {
		this.aveCno2 = aveCno2;
	}
	public int getuTimes3() {
		return uTimes3;
	}
	public void setuTimes3(int uTimes3) {
		this.uTimes3 = uTimes3;
	}
	public int getAveCno3() {
		return aveCno3;
	}
	public void setAveCno3(int aveCno3) {
		this.aveCno3 = aveCno3;
	}
	public int getUtimesEcnoAbss() {
		return UtimesEcnoAbss;
	}
	public void setUtimesEcnoAbss(int utimesEcnoAbss) {
		UtimesEcnoAbss = utimesEcnoAbss;
	}
	public int getuTmiesAlone() {
		return uTmiesAlone;
	}
	public void setuTmiesAlone(int uTmiesAlone) {
		this.uTmiesAlone = uTmiesAlone;
	}
	@Override
	public String toString() {
		return "NcsUmtsNcellData [recType=" + recType + ", length=" + length
				+ ", cellName=" + cellName + ", mfddArfcn=" + mfddArfcn
				+ ", mscrCode=" + mscrCode + ", diversity=" + diversity
				+ ", definedAsNcell=" + definedAsNcell + ", recTimeUmfi="
				+ recTimeUmfi + ", repUmfi=" + repUmfi + ", uTimes=" + uTimes
				+ ", aveCno=" + aveCno + ", uTimes1=" + uTimes1 + ", aveCno1="
				+ aveCno1 + ", uTmies2=" + uTmies2 + ", aveCno2=" + aveCno2
				+ ", uTimes3=" + uTimes3 + ", aveCno3=" + aveCno3
				+ ", UtimesEcnoAbss=" + UtimesEcnoAbss + ", uTmiesAlone="
				+ uTmiesAlone + "]";
	}
	
	
}
