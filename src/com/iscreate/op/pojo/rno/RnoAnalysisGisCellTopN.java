package com.iscreate.op.pojo.rno;

import java.io.Serializable;

public class RnoAnalysisGisCellTopN implements Serializable{

	private String cell;
	private String chineseName;
	private Double lng;// 经度
	private Double lat;// 纬度
	private String freqType;// gsm900，gsm1800，td
	private Float azimuth;// 方向角
	private String allLngLats;//显示所需要的所有经纬度坐标:经度+","+纬度+","+下一个
	
	private String site;
	private Long lac;
	private Long ci;
	private Long bcch;
	private String tch;
	private Float sumcica;
	private Long rank;

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public Float getSumcica() {
		return sumcica;
	}

	public void setSumcica(Float sumcica) {
		this.sumcica = sumcica;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public String getFreqType() {
		return freqType;
	}

	public void setFreqType(String freqType) {
		this.freqType = freqType;
	}

	public Float getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(Float azimuth) {
		this.azimuth = azimuth;
	}

	public String getAllLngLats() {
		return allLngLats;
	}

	public void setAllLngLats(String allLngLats) {
		this.allLngLats = allLngLats;
	}
	
	

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Long getLac() {
		return lac;
	}

	public void setLac(Long lac) {
		this.lac = lac;
	}

	public Long getCi() {
		return ci;
	}

	public void setCi(Long ci) {
		this.ci = ci;
	}

	public Long getBcch() {
		return bcch;
	}

	public void setBcch(Long bcch) {
		this.bcch = bcch;
	}

	public String getTch() {
		return tch;
	}

	public void setTch(String tch) {
		this.tch = tch;
	}

	@Override
	public String toString() {
		return "RnoGisCell [cell=" + cell + ", chineseName=" + chineseName
				+ ", lng=" + lng + ", lat=" + lat + ", freqType=" + freqType
				+ ", azimuth=" + azimuth + ", allLngLats=" + allLngLats
				+ ", site=" + site + ", lac=" + lac + ", ci=" + ci + ", bcch="
				+ bcch + ", tch=" + tch + "]";
	}




	

}
