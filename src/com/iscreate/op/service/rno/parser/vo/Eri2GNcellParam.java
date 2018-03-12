package com.iscreate.op.service.rno.parser.vo;

import java.util.HashMap;
import java.util.Map;

public class Eri2GNcellParam {

Map<String, Object> eri2GNcellParam=new HashMap<String, Object>();
	
	String msc             ;  
	String bsc             ;  
	String cell            ;  
	String n_bsc           ;  
	String n_cell          ;  
	String awoffset        ;  
	String bqoffset        ;  
	String bqoffsetafr     ;  
	String bqoffsetawb     ;  
	String cand            ;  
	String cs              ;  
	String gprsvalid       ;  
	String hihyst          ;  
	String khyst           ;  
	String koffset         ;  
	String lhyst           ;  
	String loffset         ;  
	String lohyst          ;  
	String offset          ;  
	String proffset        ;  
	String trhyst          ;  
	String troffset        ;  
	String userdata        ;    

	public void put(String key,String value) {
		this.eri2GNcellParam.put(key, value);
	}
	public Map<String, Object> getEri2GNcellParam() {
	return eri2GNcellParam;
	}

	public void setEri2GNcellParam(Map<String, Object> eri2gNcellParam) {
		eri2GNcellParam = eri2gNcellParam;
	}
	public String getMsc() {
		return msc;
	}
	public void setMsc(String msc) {
		this.msc = msc;
	}
	public String getBsc() {
		return bsc;
	}
	public void setBsc(String bsc) {
		this.bsc = bsc;
	}
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	public String getN_bsc() {
		return n_bsc;
	}
	public void setN_bsc(String n_bsc) {
		this.n_bsc = n_bsc;
	}
	public String getN_cell() {
		return n_cell;
	}
	public void setN_cell(String n_cell) {
		this.n_cell = n_cell;
	}
	public String getAwoffset() {
		return awoffset;
	}
	public void setAwoffset(String awoffset) {
		this.awoffset = awoffset;
	}
	public String getBqoffset() {
		return bqoffset;
	}
	public void setBqoffset(String bqoffset) {
		this.bqoffset = bqoffset;
	}
	public String getBqoffsetafr() {
		return bqoffsetafr;
	}
	public void setBqoffsetafr(String bqoffsetafr) {
		this.bqoffsetafr = bqoffsetafr;
	}
	public String getBqoffsetawb() {
		return bqoffsetawb;
	}
	public void setBqoffsetawb(String bqoffsetawb) {
		this.bqoffsetawb = bqoffsetawb;
	}
	public String getCand() {
		return cand;
	}
	public void setCand(String cand) {
		this.cand = cand;
	}
	public String getCs() {
		return cs;
	}
	public void setCs(String cs) {
		this.cs = cs;
	}
	public String getGprsvalid() {
		return gprsvalid;
	}
	public void setGprsvalid(String gprsvalid) {
		this.gprsvalid = gprsvalid;
	}
	public String getHihyst() {
		return hihyst;
	}
	public void setHihyst(String hihyst) {
		this.hihyst = hihyst;
	}
	public String getKhyst() {
		return khyst;
	}
	public void setKhyst(String khyst) {
		this.khyst = khyst;
	}
	public String getKoffset() {
		return koffset;
	}
	public void setKoffset(String koffset) {
		this.koffset = koffset;
	}
	public String getLhyst() {
		return lhyst;
	}
	public void setLhyst(String lhyst) {
		this.lhyst = lhyst;
	}
	public String getLoffset() {
		return loffset;
	}
	public void setLoffset(String loffset) {
		this.loffset = loffset;
	}
	public String getLohyst() {
		return lohyst;
	}
	public void setLohyst(String lohyst) {
		this.lohyst = lohyst;
	}
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	public String getProffset() {
		return proffset;
	}
	public void setProffset(String proffset) {
		this.proffset = proffset;
	}
	public String getTrhyst() {
		return trhyst;
	}
	public void setTrhyst(String trhyst) {
		this.trhyst = trhyst;
	}
	public String getTroffset() {
		return troffset;
	}
	public void setTroffset(String troffset) {
		this.troffset = troffset;
	}
	public String getUserdata() {
		return userdata;
	}
	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}

}
