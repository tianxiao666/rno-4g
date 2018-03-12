package com.iscreate.op.service.rno.parser.vo;

import java.util.ArrayList;
import java.util.List;

public class MrrNumOfMeaResultsRec extends MrrRecord {
	
	private String cellName;
	private int subCell;
	private int channelGroupNumber;
	private int rep;
	private int repferUl;
	private int repferDl;
	private int repferBl;
	private int repferTHL;
	
	public MrrNumOfMeaResultsRec() {
		super.setRecType(37);
		super.setRecTypeName("Record NUMBER OF MEASUREMENT RESULTS CELL DATA");
	}
	
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public int getSubCell() {
		return subCell;
	}
	public void setSubCell(int subCell) {
		this.subCell = subCell;
	}
	public int getChannelGroupNumber() {
		return channelGroupNumber;
	}
	public void setChannelGroupNumber(int channelGroupNumber) {
		this.channelGroupNumber = channelGroupNumber;
	}
	public int getRep() {
		return rep;
	}
	public void setRep(int rep) {
		this.rep = rep;
	}
	public int getRepferUl() {
		return repferUl;
	}
	public void setRepferUl(int repferUl) {
		this.repferUl = repferUl;
	}
	public int getRepferDl() {
		return repferDl;
	}
	public void setRepferDl(int repferDl) {
		this.repferDl = repferDl;
	}
	public int getRepferBl() {
		return repferBl;
	}
	public void setRepferBl(int repferBl) {
		this.repferBl = repferBl;
	}
	public int getRepferTHL() {
		return repferTHL;
	}
	public void setRepferTHL(int repferTHL) {
		this.repferTHL = repferTHL;
	}
	
	@Override
	public String toString() {
		return "MrrNumOfMeaResultsRec:[cellName="+cellName
						+",subCell="+subCell+",channelGroupNumber="+channelGroupNumber
						+",rep="+rep+",repferUl="+repferUl+",repferDl="+repferDl
						+",repferBl="+repferBl+",repferTHL="+repferTHL+"]";
	}
	
	@Override
	public List<String> getFields() {

		List<String> result = new ArrayList<String>();

		result.add("cellName");
		result.add("subCell");
		result.add("channelGroupNumber");
		result.add("rep");
		result.add("repferUl");
		result.add("repferDl");
		result.add("repferBl");
		result.add("repferTHL");
		return result;
	}
	
	@Override
	public String getFieldValues() {
		String result = "";

		result += this.getCellName() + "\t";
		result += this.getSubCell() + "\t";
		result += this.getChannelGroupNumber() + "\t";
		result += this.getRep() + "\t";
		result += this.getRepferUl() + "\t";
		result += this.getRepferDl() + "\t";
		result += this.getRepferBl() + "\t";
		result += this.getRepferTHL() + "\t";
		result += "\n";
		return result;
	}
}
