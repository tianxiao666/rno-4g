package com.iscreate.op.pojo.rno;

import java.io.Serializable;

import javax.servlet.ServletRequest;

import com.sun.xml.internal.bind.v2.model.core.ID;

public class RnoXlsCell implements Serializable{

	private String siteno;
	private String cellid;
	private String bsc;
	private String btsname;
	private String btslng;
	private String btslat;
	private String antbearing;
	private String system;
	private String colortype;
	private String description;
	public String getSiteno() {
		return siteno;
	}
	public void setSiteno(String siteno) {
		this.siteno = siteno;
	}
	public String getCellid() {
		return cellid;
	}
	public void setCellid(String cellid) {
		this.cellid = cellid;
	}
	public String getBsc() {
		return bsc;
	}
	public void setBsc(String bsc) {
		this.bsc = bsc;
	}
	public String getBtsname() {
		return btsname;
	}
	public void setBtsname(String btsname) {
		this.btsname = btsname;
	}
	public String getBtslng() {
		return btslng;
	}
	public void setBtslng(String btslng) {
		this.btslng = btslng;
	}
	public String getBtslat() {
		return btslat;
	}
	public void setBtslat(String btslat) {
		this.btslat = btslat;
	}
	public String getAntbearing() {
		return antbearing;
	}
	public void setAntbearing(String antbearing) {
		this.antbearing = antbearing;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getColortype() {
		return colortype;
	}
	public void setColortype(String colortype) {
		this.colortype = colortype;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
