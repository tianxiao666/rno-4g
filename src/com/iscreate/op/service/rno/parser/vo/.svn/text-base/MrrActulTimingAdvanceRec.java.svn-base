package com.iscreate.op.service.rno.parser.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MrrActulTimingAdvanceRec extends MrrRecord{
	
	private String cellName;
	private int subCell;
	private int channelGroupNumber;
	private Map<String, Integer> tavals = new HashMap<String, Integer>();
	
	public MrrActulTimingAdvanceRec() {
		super.setRecType(34);
		super.setRecTypeName("Record ACTUAL TIMING ADVANCE CELL DATA");
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

	public Map<String, Integer> getTavals() {
		return tavals;
	}

	public void setTavals(Map<String, Integer> tavals) {
		this.tavals = tavals;
	}
	
	public void addTavals(String key,int val){
		tavals.put(key, val);
	}
	
	@Override
	public String toString() {
		return "MrrActulTimingAdvanceRec:[cellName="+cellName
						+",subCell="+subCell+",channelGroupNumber="+channelGroupNumber
						+",tavals="+tavals+"]";
	}
	
	@Override
	public List<String> getFields() {
		List<String> result = new ArrayList<String>();
		result.add("cellName");
		result.add("subCell");
		result.add("channelGroupNumber");
		result.add("tavals");
		return result;
	}
	
	@Override
	public String getFieldValues() {
		String result = "";

		result += this.getCellName() + "\t";
		result += this.getSubCell() + "\t";
		result += this.getChannelGroupNumber() + "\t";
		result += this.getTavals() + "\t";
		result += "\n";
		return result;
	}

}
