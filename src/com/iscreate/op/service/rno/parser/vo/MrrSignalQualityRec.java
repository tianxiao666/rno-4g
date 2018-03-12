package com.iscreate.op.service.rno.parser.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MrrSignalQualityRec extends MrrRecord {
	
	private String cellName;
	private int subCell;
	private int channelGroupNumber;
	private Map<String, Integer> rxQualUls = new HashMap<String, Integer>();
	private Map<String, Integer> rxQualDls = new HashMap<String, Integer>();

	public MrrSignalQualityRec() {
		super.setRecType(32);
		super.setRecTypeName("Record UPLINK AND DOWNLINK SIGNAL QUALITY CELL DATA");
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

	public Map<String, Integer> getRxQualUls() {
		return rxQualUls;
	}

	public void setRxQualUls(Map<String, Integer> rxQualUls) {
		this.rxQualUls = rxQualUls;
	}

	public Map<String, Integer> getRxQualDls() {
		return rxQualDls;
	}

	public void setRxQualDls(Map<String, Integer> rxQualDls) {
		this.rxQualDls = rxQualDls;
	}
	
	public void addRxQualUl(String key,int val){
		rxQualUls.put(key, val);
	}
	
	public void addRxQualDl(String key,int val){
		rxQualDls.put(key, val);
	}
	
	@Override
	public String toString() {
		return "MrrSignalQualityRec:[cellName="+cellName
						+",subCell="+subCell+",channelGroupNumber="+channelGroupNumber
						+",rxQualUls="+rxQualUls+",rxQualDls="+rxQualDls+"]";
	}
	
	@Override
	public List<String> getFields() {
		
		List<String> result = new ArrayList<String>();

		result.add("cellName");
		result.add("subCell");
		result.add("channelGroupNumber");
		result.add("rxQualUls");
		result.add("rxQualDls");
		return result;
	}
	
	@Override
	public String getFieldValues() {
		String result = "";

		result += this.getCellName() + "\t";
		result += this.getSubCell() + "\t";
		result += this.getChannelGroupNumber() + "\t";
		result += this.getRxQualUls() + "\t";
		result += this.getRxQualDls() + "\t";
		result += "\n";
		return result;
	}
}
