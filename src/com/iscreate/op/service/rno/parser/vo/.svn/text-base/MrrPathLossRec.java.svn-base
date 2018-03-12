package com.iscreate.op.service.rno.parser.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MrrPathLossRec extends MrrRecord{
	
	private String cellName;
	private int subCell;
	private int channelGroupNumber;
	private Map<String, Integer> pLossUls = new HashMap<String, Integer>();
	private Map<String, Integer> pLossDls = new HashMap<String, Integer>();
	
	public MrrPathLossRec() {
		super.setRecType(35);
		super.setRecTypeName("Record UPLINK AND DOWNLINK PATH LOSS CELL DATA");
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

	public Map<String, Integer> getPLossUls() {
		return pLossUls;
	}

	public void setPLossUls(Map<String, Integer> lossUls) {
		pLossUls = lossUls;
	}
	
	public Map<String, Integer> getPLossDls() {
		return pLossDls;
	}

	public void setPLossDls(Map<String, Integer> lossDls) {
		pLossDls = lossDls;
	}
	
	public void addPLossUls(String key,int val){
		pLossUls.put(key, val);
	}
	
	public void addPLossDls(String key,int val){
		pLossDls.put(key, val);
	}

	@Override
	public String toString() {
		return "MrrPathLossRec:[cellName="+cellName
						+",subCell="+subCell+",channelGroupNumber="+channelGroupNumber
						+",pLossUls="+pLossUls+",pLossDls="+pLossDls+"]";
	}
	
	
	@Override
	public List<String> getFields() {
		List<String> result = new ArrayList<String>();

		result.add("cellName");
		result.add("subCell");
		result.add("channelGroupNumber");
		result.add("pLossUls");
		result.add("pLossDls");
		return result;
	}
	
	@Override
	public String getFieldValues() {
		String result = "";

		result += this.getCellName() + "\t";
		result += this.getSubCell() + "\t";
		result += this.getChannelGroupNumber() + "\t";
		result += this.getPLossUls() + "\t";
		result += this.getPLossDls() + "\t";
		result += "\n";
		return result;
	}
}
