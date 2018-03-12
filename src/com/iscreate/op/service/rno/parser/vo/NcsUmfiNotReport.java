package com.iscreate.op.service.rno.parser.vo;

public class NcsUmfiNotReport extends NcsRecord{

	private int recType=55;
	private int length=22;
	private String cellName;
	private int mfddArfcn;
	private int mscrCode;
	private int diversity;
	private int recTimeUmfi;
	private int repUmfi;
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
	@Override
	public String toString() {
		return "NcsUmfiNotReport [recType=" + recType + ", length=" + length
				+ ", cellName=" + cellName + ", mfddArfcn=" + mfddArfcn
				+ ", mscrCode=" + mscrCode + ", diversity=" + diversity
				+ ", recTimeUmfi=" + recTimeUmfi + ", repUmfi=" + repUmfi + "]";
	}
	
}
