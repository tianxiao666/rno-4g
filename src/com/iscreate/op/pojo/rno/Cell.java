package com.iscreate.op.pojo.rno;

import java.util.Date;

/**
 * Cell entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Cell{

	// Fields

	private Long id;
	private String name;
	private String label;
	private Long ifsharetg;
	private String covertype;
	private String coverarea;
	private String importancegrade;
	private Date takeoverdate;
	private Long ifpasschecking;
	private Date passcheckingdate;
	private Long iftranssingleline;
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
	private Long antHeigh;
	private String basetype;
	private Long downtilt;
	private Long bearing;
	private Long maxTxBs;
	private Long maxTxMs;
	private Long entityId;
	private String entityType;
	private String antModel;
	private Long electricaldowntilt;
	private Long mechanicaldowntilt;
	private Double longitude;
	private Double latitude;
	private String site;
	private Long bscId;
	private String btstype;
	private Long cellDescId;
	private Long areaId;
    private String lngLats;
	// Constructors

	/** default constructor */
	public Cell() {
	}

	/** full constructor */
	public Cell(String name, String label, Long ifsharetg, String covertype,
			String coverarea, String importancegrade, Date takeoverdate,
			Long ifpasschecking, Date passcheckingdate, Long iftranssingleline,
			String gsmfrequencesection, String address, Long lac, Long ci,
			Long bsic, Long bcch, String tch, String antManufactory,
			String antType, Long antGain, Long antHeigh, String basetype,
			Long downtilt, Long bearing, Long maxTxBs, Long maxTxMs,
			Long entityId, String entityType, String antModel,
			Long electricaldowntilt, Long mechanicaldowntilt, Double longitude,
			Double latitude, String site, Long bscId, String btstype,
			Long cellDescId, Long areaId,String lngLats) {
		this.name = name;
		this.label = label;
		this.ifsharetg = ifsharetg;
		this.covertype = covertype;
		this.coverarea = coverarea;
		this.importancegrade = importancegrade;
		this.takeoverdate = takeoverdate;
		this.ifpasschecking = ifpasschecking;
		this.passcheckingdate = passcheckingdate;
		this.iftranssingleline = iftranssingleline;
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
		this.antHeigh = antHeigh;
		this.basetype = basetype;
		this.downtilt = downtilt;
		this.bearing = bearing;
		this.maxTxBs = maxTxBs;
		this.maxTxMs = maxTxMs;
		this.entityId = entityId;
		this.entityType = entityType;
		this.antModel = antModel;
		this.electricaldowntilt = electricaldowntilt;
		this.mechanicaldowntilt = mechanicaldowntilt;
		this.longitude = longitude;
		this.latitude = latitude;
		this.site = site;
		this.bscId = bscId;
		this.btstype = btstype;
		this.cellDescId = cellDescId;
		this.areaId = areaId;
		this.lngLats=lngLats;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Long getIfsharetg() {
		return this.ifsharetg;
	}

	public void setIfsharetg(Long ifsharetg) {
		this.ifsharetg = ifsharetg;
	}

	public String getCovertype() {
		return this.covertype;
	}

	public void setCovertype(String covertype) {
		this.covertype = covertype;
	}

	public String getCoverarea() {
		return this.coverarea;
	}

	public void setCoverarea(String coverarea) {
		this.coverarea = coverarea;
	}

	public String getImportancegrade() {
		return this.importancegrade;
	}

	public void setImportancegrade(String importancegrade) {
		this.importancegrade = importancegrade;
	}

	public Date getTakeoverdate() {
		return this.takeoverdate;
	}

	public void setTakeoverdate(Date takeoverdate) {
		this.takeoverdate = takeoverdate;
	}

	public Long getIfpasschecking() {
		return this.ifpasschecking;
	}

	public void setIfpasschecking(Long ifpasschecking) {
		this.ifpasschecking = ifpasschecking;
	}

	public Date getPasscheckingdate() {
		return this.passcheckingdate;
	}

	public void setPasscheckingdate(Date passcheckingdate) {
		this.passcheckingdate = passcheckingdate;
	}

	public Long getIftranssingleline() {
		return this.iftranssingleline;
	}

	public void setIftranssingleline(Long iftranssingleline) {
		this.iftranssingleline = iftranssingleline;
	}

	public String getGsmfrequencesection() {
		return this.gsmfrequencesection;
	}

	public void setGsmfrequencesection(String gsmfrequencesection) {
		this.gsmfrequencesection = gsmfrequencesection;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getLac() {
		return this.lac;
	}

	public void setLac(Long lac) {
		this.lac = lac;
	}

	public Long getCi() {
		return this.ci;
	}

	public void setCi(Long ci) {
		this.ci = ci;
	}

	public Long getBsic() {
		return this.bsic;
	}

	public void setBsic(Long bsic) {
		this.bsic = bsic;
	}

	public Long getBcch() {
		return this.bcch;
	}

	public void setBcch(Long bcch) {
		this.bcch = bcch;
	}

	public String getTch() {
		return this.tch;
	}

	public void setTch(String tch) {
		this.tch = tch;
	}

	public String getAntManufactory() {
		return this.antManufactory;
	}

	public void setAntManufactory(String antManufactory) {
		this.antManufactory = antManufactory;
	}

	public String getAntType() {
		return this.antType;
	}

	public void setAntType(String antType) {
		this.antType = antType;
	}

	public Long getAntGain() {
		return this.antGain;
	}

	public void setAntGain(Long antGain) {
		this.antGain = antGain;
	}

	public Long getAntHeigh() {
		return this.antHeigh;
	}

	public void setAntHeigh(Long antHeigh) {
		this.antHeigh = antHeigh;
	}

	public String getBasetype() {
		return this.basetype;
	}

	public void setBasetype(String basetype) {
		this.basetype = basetype;
	}

	public Long getDowntilt() {
		return this.downtilt;
	}

	public void setDowntilt(Long downtilt) {
		this.downtilt = downtilt;
	}

	public Long getBearing() {
		return this.bearing;
	}

	public void setBearing(Long bearing) {
		this.bearing = bearing;
	}

	public Long getMaxTxBs() {
		return this.maxTxBs;
	}

	public void setMaxTxBs(Long maxTxBs) {
		this.maxTxBs = maxTxBs;
	}

	public Long getMaxTxMs() {
		return this.maxTxMs;
	}

	public void setMaxTxMs(Long maxTxMs) {
		this.maxTxMs = maxTxMs;
	}

	public Long getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return this.entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getAntModel() {
		return this.antModel;
	}

	public void setAntModel(String antModel) {
		this.antModel = antModel;
	}

	public Long getElectricaldowntilt() {
		return this.electricaldowntilt;
	}

	public void setElectricaldowntilt(Long electricaldowntilt) {
		this.electricaldowntilt = electricaldowntilt;
	}

	public Long getMechanicaldowntilt() {
		return this.mechanicaldowntilt;
	}

	public void setMechanicaldowntilt(Long mechanicaldowntilt) {
		this.mechanicaldowntilt = mechanicaldowntilt;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getSite() {
		return this.site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Long getBscId() {
		return this.bscId;
	}

	public void setBscId(Long bscId) {
		this.bscId = bscId;
	}

	public String getBtstype() {
		return this.btstype;
	}

	public void setBtstype(String btstype) {
		this.btstype = btstype;
	}

	public Long getCellDescId() {
		return this.cellDescId;
	}

	public void setCellDescId(Long cellDescId) {
		this.cellDescId = cellDescId;
	}

	public Long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getLngLats() {
		return lngLats;
	}

	public void setLngLats(String lngLats) {
		this.lngLats = lngLats;
	}

}