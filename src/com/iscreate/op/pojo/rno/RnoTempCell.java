package com.iscreate.op.pojo.rno;

import java.util.Date;

public class RnoTempCell {

	private Long id;
	private Long cellDescId;
	private String name;
	private String label;
	private String covertype;
	private String importancegrade;
	private String gsmfrequencesection;
	private String address;
	private Long lac;
	private Long ci;
	private Long bsic;
	private Long bcch;
	private String tch;
	private String antManufactory;
	private String antType;
	private Long antGain;
	private Long antHeight;
	private String basetype;
	private Long downtilt;
	private Long bearing;
	private Long maxTxBs;
	private Long maxTxMs;
	private String antModel;
	private Long electricaldowntilt;
	private Long mechanicaldowntilt;
	private Double longitude;
	private Double latitude;
	private String site;
	private Long bscId;
	private String btstype;
	private Long areaId;
	private String lnglats;
	private String coverarea;
	public RnoTempCell(){
		
	}
	
	public RnoTempCell(Long cellDescId, String name, String label,
			String covertype, String importancegrade,
			String gsmfrequencesection, String address, Long lac, Long ci,
			Long bsic, Long bcch, String tch, String antManufactory,
			String antType, Long antGain, Long antHeight, String basetype,
			Long downtilt, Long bearing, Long maxTxBs, Long maxTxMs,
			String antModel, Long electricaldowntilt, Long mechanicaldowntilt,
			Double longitude, Double latitude, String site, Long bscId,
			String btstype, Long areaId, String lngLats, String coverarea) {
		super();
		this.cellDescId = cellDescId;
		this.name = name;
		this.label = label;
		this.covertype = covertype;
		this.importancegrade = importancegrade;
		this.gsmfrequencesection = gsmfrequencesection;
		this.address = address;
		this.lac = lac;
		this.ci = ci;
		this.bsic = bsic;
		this.bcch = bcch;
		this.tch = tch;
		this.antManufactory = antManufactory;
		this.antType = antType;
		this.antGain = antGain;
		this.antHeight = antHeight;
		this.basetype = basetype;
		this.downtilt = downtilt;
		this.bearing = bearing;
		this.maxTxBs = maxTxBs;
		this.maxTxMs = maxTxMs;
		this.antModel = antModel;
		this.electricaldowntilt = electricaldowntilt;
		this.mechanicaldowntilt = mechanicaldowntilt;
		this.longitude = longitude;
		this.latitude = latitude;
		this.site = site;
		this.bscId = bscId;
		this.btstype = btstype;
		this.areaId = areaId;
		this.lnglats = lnglats;
		this.coverarea = coverarea;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCellDescId() {
		return cellDescId;
	}
	public void setCellDescId(Long cellDescId) {
		this.cellDescId = cellDescId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getCovertype() {
		return covertype;
	}
	public void setCovertype(String covertype) {
		this.covertype = covertype;
	}
	public String getImportancegrade() {
		return importancegrade;
	}
	public void setImportancegrade(String importancegrade) {
		this.importancegrade = importancegrade;
	}
	public String getGsmfrequencesection() {
		return gsmfrequencesection;
	}
	public void setGsmfrequencesection(String gsmfrequencesection) {
		this.gsmfrequencesection = gsmfrequencesection;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public Long getBsic() {
		return bsic;
	}
	public void setBsic(Long bsic) {
		this.bsic = bsic;
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
	public String getAntManufactory() {
		return antManufactory;
	}
	public void setAntManufactory(String antManufactory) {
		this.antManufactory = antManufactory;
	}
	public String getAntType() {
		return antType;
	}
	public void setAntType(String antType) {
		this.antType = antType;
	}
	public Long getAntGain() {
		return antGain;
	}
	public void setAntGain(Long antGain) {
		this.antGain = antGain;
	}
	public Long getAntHeight() {
		return antHeight;
	}
	public void setAntHeight(Long antHeight) {
		this.antHeight = antHeight;
	}
	public String getBasetype() {
		return basetype;
	}
	public void setBasetype(String basetype) {
		this.basetype = basetype;
	}
	public Long getDowntilt() {
		return downtilt;
	}
	public void setDowntilt(Long downtilt) {
		this.downtilt = downtilt;
	}
	public Long getBearing() {
		return bearing;
	}
	public void setBearing(Long bearing) {
		this.bearing = bearing;
	}
	public Long getMaxTxBs() {
		return maxTxBs;
	}
	public void setMaxTxBs(Long maxTxBs) {
		this.maxTxBs = maxTxBs;
	}
	public Long getMaxTxMs() {
		return maxTxMs;
	}
	public void setMaxTxMs(Long maxTxMs) {
		this.maxTxMs = maxTxMs;
	}
	public String getAntModel() {
		return antModel;
	}
	public void setAntModel(String antModel) {
		this.antModel = antModel;
	}
	public Long getElectricaldowntilt() {
		return electricaldowntilt;
	}
	public void setElectricaldowntilt(Long electricaldowntilt) {
		this.electricaldowntilt = electricaldowntilt;
	}
	public Long getMechanicaldowntilt() {
		return mechanicaldowntilt;
	}
	public void setMechanicaldowntilt(Long mechanicaldowntilt) {
		this.mechanicaldowntilt = mechanicaldowntilt;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public Long getBscId() {
		return bscId;
	}
	public void setBscId(Long bscId) {
		this.bscId = bscId;
	}
	public String getBtstype() {
		return btstype;
	}
	public void setBtstype(String btstype) {
		this.btstype = btstype;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getCoverarea() {
		return coverarea;
	}
	public void setCoverarea(String coverarea) {
		this.coverarea = coverarea;
	}

	public String getLnglats() {
		return lnglats;
	}

	public void setLnglats(String lnglats) {
		this.lnglats = lnglats;
	}

    
}
