package com.iscreate.op.service.rno.parser.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MrrPathLossDifferenceRec extends MrrRecord {

	private String cellName;
	private int subCell;
	private int channelGroupNumber;
	private Map<String, Integer> pLDiffs = new HashMap<String, Integer>();
	
	public MrrPathLossDifferenceRec() {
		super.setRecType(36);
		super.setRecTypeName("Record PATH LOSS DIFFERENCE CELL DATA");
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

	public Map<String, Integer> getPLDiffs() {
		return pLDiffs;
	}

	public void setPLDiffs(Map<String, Integer> diffs) {
		pLDiffs = diffs;
	}
	
	public void addPLDiffs(String key,int val){
		pLDiffs.put(key, val);
	}
	
	@Override
	public String toString() {
		return "MrrPathLossDifferenceRec:[cellName="+cellName
						+",subCell="+subCell+",channelGroupNumber="+channelGroupNumber
						+",pLDiffs="+pLDiffs+"]";
	}
	
	@Override
	public List<String> getFields() {
		
		List<String> result = new ArrayList<String>();

		result.add("cellName");
		result.add("subCell");
		result.add("channelGroupNumber");
		result.add("pLDiffs");
		return result;
	}
	
	@Override
	public String getFieldValues() {
		String result = "";

		result += this.getCellName() + "\t";
		result += this.getSubCell() + "\t";
		result += this.getChannelGroupNumber() + "\t";
		result += this.getPLDiffs() + "\t";
		result += "\n";
		return result;
	}
}
