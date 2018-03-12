package com.iscreate.op.service.rno.parser.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HW2GMrrFRateRec extends HW2GMrrRecord {



	private Date meaTime;
	private String bscName;
	private String cellName;
	private int cellIdx;
	private int lac;
	private int ci;
	private int trxIdx;
	private String integrity;
	private Map<String, Integer> fRateUls=new HashMap<String, Integer>();
	private Map<String, Integer> fRateDls=new HashMap<String, Integer>();
	
	
	
	public HW2GMrrFRateRec() {
		super.setRecType("S4100A");
		super.setRecTypeName("RNO_2G_HW_MRR_FRATE");
	}

	public Date getMeaTime() {
		return meaTime;
	}


	public void setMeaTime(Date meaTime) {
		this.meaTime = meaTime;
	}


	public String getBscName() {
		return bscName;
	}


	public void setBscName(String bscName) {
		this.bscName = bscName;
	}


	public String getCellName() {
		return cellName;
	}


	public void setCellName(String cellName) {
		this.cellName = cellName;
	}


	public int getCellIdx() {
		return cellIdx;
	}


	public void setCellIdx(int cellIdx) {
		this.cellIdx = cellIdx;
	}


	public int getLac() {
		return lac;
	}


	public void setLac(int lac) {
		this.lac = lac;
	}


	public int getCi() {
		return ci;
	}


	public void setCi(int ci) {
		this.ci = ci;
	}


	public int getTrxIdx() {
		return trxIdx;
	}


	public void setTrxIdx(int trxIdx) {
		this.trxIdx = trxIdx;
	}


	public String getIntegrity() {
		return integrity;
	}


	public void setIntegrity(String integrity) {
		this.integrity = integrity;
	}


	public Map<String, Integer> getfRateUls() {
		return fRateUls;
	}


	public void setfRateUls(Map<String, Integer> fRateUls) {
		this.fRateUls = fRateUls;
	}


	public Map<String, Integer> getfRateDls() {
		return fRateDls;
	}


	public void setfRateDls(Map<String, Integer> fRateDls) {
		this.fRateDls = fRateDls;
	}
	public void addFRateDls(String key,int val){
		fRateDls.put(key, val);
	}
	public void addFRateUls(String key,int val){
		fRateUls.put(key, val);
	}
	@Override
	public List<String> getFields() {
		List<String> result = new ArrayList<String>();

		result.add("cellName");
		result.add("trx");
		result.add("fRateUls");
		result.add("fRateDls");

		return result;
	}
	@Override
	public String getFieldValues() {
		String result = "";

		result += this.getCellName() + "\t";
		result += this.getTrxIdx() + "\t";
		result += this.getfRateUls() + "\t";
		result += this.getfRateDls() + "\t";
		result += "\n";
		return result;
	}
	@Override
	public String toString() {
		return "HW2GMrrHRateRec:[cellName="+cellName
		+",trxIdx="+trxIdx+",fRateDls="+fRateDls+",fRateUls="+fRateUls+"]";
	}
}
