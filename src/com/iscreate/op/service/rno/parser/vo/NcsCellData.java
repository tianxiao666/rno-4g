package com.iscreate.op.service.rno.parser.vo;

import com.iscreate.op.service.rno.tool.DbValInject;

public class NcsCellData extends NcsRecord{

	private int recType;
	private int length;
	private String cellName;
	private int chgr;
	private long rep;
	private long repHr;
	private long repUndefGsm;
	private int avss;
	
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

	public int getChgr() {
		return chgr;
	}

	public void setChgr(int chgr) {
		this.chgr = chgr;
	}

	public long getRep() {
		return rep;
	}

	public void setRep(long rep) {
		this.rep = rep;
	}

	public long getRepHr() {
		return repHr;
	}

	public void setRepHr(long repHr) {
		this.repHr = repHr;
	}

	public long getRepUndefGsm() {
		return repUndefGsm;
	}

	public void setRepUndefGsm(long repUndefGsm) {
		this.repUndefGsm = repUndefGsm;
	}

	public int getAvss() {
		return avss;
	}

	public void setAvss(int avss) {
		this.avss = avss;
	}

	@Override
	public String toString() {
		return "NcsCellData [recType=" + recType + ", length=" + length
				+ ", cellName=" + cellName + ", chgr=" + chgr + ", rep=" + rep
				+ ", repHr=" + repHr + ", repUndefGsm=" + repUndefGsm
				+ ", avss=" + avss + "]";
	}

	
}
