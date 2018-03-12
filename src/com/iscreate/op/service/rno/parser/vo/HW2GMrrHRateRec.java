package com.iscreate.op.service.rno.parser.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HW2GMrrHRateRec extends HW2GMrrRecord {


	private Date meaTime;
	private String bscName;
	private String cellName;
	private int cellIdx;
	private int lac;
	private int ci;
	private int trxIdx;
	private String integrity;
	private Map<String, Integer> hRateUls=new HashMap<String, Integer>();
	private Map<String, Integer> hRateDls=new HashMap<String, Integer>();
	
	public HW2GMrrHRateRec() {
		super.setRecType("S4100C");
		super.setRecTypeName("RNO_2G_HW_MRR_HRATE");
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


	public Map<String, Integer> gethRateUls() {
		return hRateUls;
	}


	public void sethRateUls(Map<String, Integer> hRateUls) {
		this.hRateUls = hRateUls;
	}


	public Map<String, Integer> gethRateDls() {
		return hRateDls;
	}


	public void sethRateDls(Map<String, Integer> hRateDls) {
		this.hRateDls = hRateDls;
	}

	public void addHRateDls(String key,int val){
		hRateDls.put(key, val);
	}
	public void addHRateUls(String key,int val){
		hRateUls.put(key, val);
	}
	@Override
	public List<String> getFields() {
		List<String> result = new ArrayList<String>();

		result.add("cellName");
		result.add("trx");
		result.add("hRateUls");
		result.add("hRateDls");

		return result;
	}
	@Override
	public String getFieldValues() {
		String result = "";

		result += this.getCellName() + "\t";
		result += this.getTrxIdx() + "\t";
		result += this.gethRateUls() + "\t";
		result += this.gethRateDls() + "\t";
		result += "\n";
		return result;
	}
	@Override
	public String toString() {
		return "HW2GMrrHRateRec:[cellName="+cellName
		+",trxIdx="+trxIdx+",hRateDls="+hRateDls+",hRateUls="+hRateUls+"]";
	}
	
}
