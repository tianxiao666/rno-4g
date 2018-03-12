package com.iscreate.op.service.rno.parser.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MrrTransmitPowerLevRec extends MrrRecord{

	private String cellName;
	private int subCell;
	private int channelGroupNumber;
	private Map<String, Integer> msPowers = new HashMap<String, Integer>();
	private Map<String, Integer> bsPowers = new HashMap<String, Integer>();
	
	public MrrTransmitPowerLevRec() {
		super.setRecType(33);
		super.setRecTypeName("Record BTS AND MS TRANSMIT POWER LEVEL CELL DATA");
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
	public Map<String, Integer> getMsPowers() {
		return msPowers;
	}
	public void setMsPowers(Map<String, Integer> msPowers) {
		this.msPowers = msPowers;
	}
	public Map<String, Integer> getBsPowers() {
		return bsPowers;
	}
	public void setBsPowers(Map<String, Integer> bsPowers) {
		this.bsPowers = bsPowers;
	}
	
	public void addMsPowers(String key,int val){
		msPowers.put(key, val);
	}
	
	public void addBsPowers(String key,int val){
		bsPowers.put(key, val);
	}
	
	@Override
	public String toString() {
		return "MrrTransmitPowerLevRec:[cellName="+cellName
						+",subCell="+subCell+",channelGroupNumber="+channelGroupNumber
						+",msPowers="+msPowers+",bsPowers="+bsPowers+"]";
	}
	
	@Override
	public List<String> getFields() {
		
		List<String> result = new ArrayList<String>();

		result.add("cellName");
		result.add("subCell");
		result.add("channelGroupNumber");
		result.add("msPowers");
		result.add("bsPowers");
		return result;
	}
	
	@Override
	public String getFieldValues() {
		String result = "";

		result += this.getCellName() + "\t";
		result += this.getSubCell() + "\t";
		result += this.getMsPowers() + "\t";
		result += this.getBsPowers() + "\t";
		result += "\n";
		return result;
	}
}
