package com.iscreate.op.service.rno.parser.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MrrSignalStrengthRec extends MrrRecord {

	public MrrSignalStrengthRec(){
		super.setRecType(31);
		super.setRecTypeName("Record UPLINK AND DOWNLINK SIGNAL STRENGTH CELL DATA");
	}
	
	private String cellName;
	private int subCell;
	private int chgr;
	private Map<String,Integer> rxLevUls=new HashMap<String,Integer>();
	private Map<String,Integer> rxLevDls=new HashMap<String,Integer>();;
	
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
	public int getChgr() {
		return chgr;
	}
	public void setChgr(int chgr) {
		this.chgr = chgr;
	}
	public Map<String, Integer> getRxLevUls() {
		return rxLevUls;
	}
	public void setRxLevUls(Map<String, Integer> rxLevUls) {
		this.rxLevUls = rxLevUls;
	}
	public Map<String, Integer> getRxLevDls() {
		return rxLevDls;
	}
	public void setRxLevDls(Map<String, Integer> rxLevDls) {
		this.rxLevDls = rxLevDls;
	}
	
	public void addRxLevUl(String key,int val){
		rxLevUls.put(key, val);
	}
	
	public void addRxLevDl(String key,int val){
		rxLevDls.put(key, val);
	}
	@Override
	public String toString() {
		return "MrrSignalStrengthRec [cellName=" + cellName + ", subCell="
				+ subCell + ", chgr=" + chgr + ", rxLevUls=" + rxLevUls
				+ ", rxLevDls=" + rxLevDls + "]";
	}
	
	@Override
	public List<String> getFields() {
		
		List<String> result = new ArrayList<String>();

		result.add("cellName");
		result.add("subCell");
		result.add("chgr");
		result.add("rxLevUls");
		result.add("rxLevDls");
		return result;
	}
	
	@Override
	public String getFieldValues() {
		String result = "";

		result += this.getCellName() + "\t";
		result += this.getSubCell() + "\t";
		result += this.getChgr() + "\t";
		result += this.getRxLevUls() + "\t";
		result += this.getRxLevDls() + "\t";
		result += "\n";
		return result;
	}
}
