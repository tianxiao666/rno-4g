package com.iscreate.op.service.rno.parser.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EriFasCellData extends EriFasRecord {

	private String cellName;
	private int freqNum;
	private List<Integer> arfcnList = new ArrayList<Integer>();
	private List<Integer> avmedianList = new ArrayList<Integer>();
	private List<Integer> avpercentileList = new ArrayList<Integer>();
	private List<Integer> noofmeasList = new ArrayList<Integer>();
	
	public EriFasCellData(){
		super.setRecType(41);
	}
	
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public int getFreqNum() {
		return freqNum;
	}
	public void setFreqNum(int freqNum) {
		this.freqNum = freqNum;
	}
	public List<Integer> getArfcnList() {
		return arfcnList;
	}
	public void setArfcnList(List<Integer> arfcnList) {
		this.arfcnList = arfcnList;
	}
	public List<Integer> getAvmedianList() {
		return avmedianList;
	}
	public void setAvmedianList(List<Integer> avmedianList) {
		this.avmedianList = avmedianList;
	}
	public List<Integer> getAvpercentileList() {
		return avpercentileList;
	}
	public void setAvpercentileList(List<Integer> avpercentileList) {
		this.avpercentileList = avpercentileList;
	}
	public List<Integer> getNoofmeasList() {
		return noofmeasList;
	}
	public void setNoofmeasList(List<Integer> noofmeasList) {
		this.noofmeasList = noofmeasList;
	}
	
	public String getArfcn1To150() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arfcnList.size(); i++) {
			if(i == (arfcnList.size()-1)) {
				sb.append(arfcnList.get(i));
				break;
			}
			sb.append(arfcnList.get(i) + ",");
		}
		return sb.toString();
	}
	
	public String getAvmedian1To150() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < avmedianList.size(); i++) {
			if(i == (avmedianList.size()-1)) {
				sb.append(avmedianList.get(i));
				break;
			}
			sb.append(avmedianList.get(i) + ",");
		}
		return sb.toString();
	}	
	
	public String getAvpercentile1To150() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < avpercentileList.size(); i++) {
			if(i == (avpercentileList.size()-1)) {
				sb.append(avpercentileList.get(i));
				break;
			}
			sb.append(avpercentileList.get(i) + ",");
		}
		return sb.toString();
	}	
	
	public String getNoofmeas1To150() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < noofmeasList.size(); i++) {
			if(i == (noofmeasList.size()-1)) {
				sb.append(noofmeasList.get(i));
				break;
			}
			sb.append(noofmeasList.get(i) + ",");
		}
		return sb.toString();
	}	
	
	@Override
	public String toString() {
		return "FasCellData [cellName=" + cellName + ", freqNum=" + freqNum
				+ ", arfcnList=" + arfcnList + "]";
	}
	
}
