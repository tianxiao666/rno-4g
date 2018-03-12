package com.iscreate.op.service.rno.parser.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MrrFrameErasureRateRec extends MrrRecord {

	private String cellName;
	private int subCell;
	private int channelGroupNumber;
	private Map<String, Integer> ferUls = new HashMap<String, Integer>();
	private Map<String, Integer> ferDls = new HashMap<String, Integer>();
	
	public MrrFrameErasureRateRec() {
		super.setRecType(38);
		super.setRecTypeName("Record UPLINK AND DOWNLINK FRAME ERASURE RATE CELL DATA");
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

	public Map<String, Integer> getFerUls() {
		return ferUls;
	}

	public void setFerUls(Map<String, Integer> ferUls) {
		this.ferUls = ferUls;
	}

	public Map<String, Integer> getFerDls() {
		return ferDls;
	}

	public void setFerDls(Map<String, Integer> ferDls) {
		this.ferDls = ferDls;
	}
	
	public void addFerUls(String key,int val){
		ferUls.put(key, val);
	}
	public void addFerDls(String key,int val){
		ferDls.put(key, val);
	}
	
	@Override
	public String toString() {
		return "MrrFrameErasureRateRec:[cellName="+cellName
						+",subCell="+subCell+",channelGroupNumber="+channelGroupNumber
						+",ferUls="+ferUls+",ferDls="+ferDls+"]";
	}
	

	@Override
	public List<String> getFields() {
		List<String> result = new ArrayList<String>();

		result.add("cellName");
		result.add("subCell");
		result.add("channelGroupNumber");
		result.add("ferUls");
		result.add("ferDls");

		return result;
	}
	
	@Override
	public String getFieldValues() {
		String result = "";

		result += this.getCellName() + "\t";
		result += this.getSubCell() + "\t";
		result += this.getChannelGroupNumber() + "\t";
		result += this.getFerUls() + "\t";
		result += this.getFerDls() + "\t";
		result += "\n";
		return result;
	}
}
