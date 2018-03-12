package com.iscreate.op.pojo.rno;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RnoLteCell implements Serializable {
	
	private Long id;  //id
	private String cell;// 小区名
	private Double lng;// 经度
	private Double lat;// 纬度
	private String chineseName; // 小区中文名
	private Float azimuth;// 方向角
	private String mapType; // 所用地图类型：‘B’：表示“百度”，‘E’：表示GOOGLE EARTH
	private String shapeType;// 小区形状 ‘A’：表示箭头 ‘T’：表示三角形
	private String allLngLats;// 小区形状的经纬度数据。经度与纬度之间用英文逗号分隔。多个经纬度以英文分号分隔

	private String bandType; // 频段类型
	private String coverType; // 覆盖类型
	private String band; // 下行带宽
	private String earfcn; // 下行频点
	private Long groundHeight; // 天线挂高
	private Long rrunum; // RRU数量
	private String rruver; // RRU型号
	private String antennaType; // 天线型号
	private String integrated; // 天线是否合路
	private Long rspower; // 参考信号功率dBm
	private String coverRange; // 覆盖范围
	private String pci; // pci

	public RnoLteCell() {
	}

	public RnoLteCell(String cell, Double lng, Double lat, String chineseName,
			Float azimuth, String mapType, String shapeType, String allLngLats,
			String bandType, String coverType, String band, String earfcn,
			Long groundHeight, Long rrunum, String rruver, String antennaType,
			String integrated, Long rspower, String coverRange, String pci,
			String busCellId) {
		super();
		this.cell = cell;
		this.lng = lng;
		this.lat = lat;
		this.chineseName = chineseName;
		this.azimuth = azimuth;
		this.mapType = mapType;
		this.shapeType = shapeType;
		this.allLngLats = allLngLats;
		this.bandType = bandType;
		this.coverType = coverType;
		this.band = band;
		this.earfcn = earfcn;
		this.groundHeight = groundHeight;
		this.rrunum = rrunum;
		this.rruver = rruver;
		this.antennaType = antennaType;
		this.integrated = integrated;
		this.rspower = rspower;
		this.coverRange = coverRange;
		this.pci = pci;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPci() {
		return pci;
	}

	public void setPci(String pci) {
		this.pci = pci;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
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

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public Float getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(Float azimuth) {
		this.azimuth = azimuth;
	}

	public String getMapType() {
		return mapType;
	}

	public void setMapType(String mapType) {
		this.mapType = mapType;
	}

	public String getShapeType() {
		return shapeType;
	}

	public void setShapeType(String shapeType) {
		this.shapeType = shapeType;
	}

	public String getAllLngLats() {
		return allLngLats;
	}

	public void setAllLngLats(String allLngLats) {
		this.allLngLats = allLngLats;
	}

	public String getBandType() {
		return bandType;
	}

	public void setBandType(String bandType) {
		this.bandType = bandType;
	}

	public String getCoverType() {
		return coverType;
	}

	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getEarfcn() {
		return earfcn;
	}

	public void setEarfcn(String earfcn) {
		this.earfcn = earfcn;
	}

	public Long getGroundHeight() {
		return groundHeight;
	}

	public void setGroundHeight(Long groundHeight) {
		this.groundHeight = groundHeight;
	}

	public Long getRrunum() {
		return rrunum;
	}

	public void setRrunum(Long rrunum) {
		this.rrunum = rrunum;
	}

	public String getRruver() {
		return rruver;
	}

	public void setRruver(String rruver) {
		this.rruver = rruver;
	}

	public String getAntennaType() {
		return antennaType;
	}

	public void setAntennaType(String antennaType) {
		this.antennaType = antennaType;
	}

	public String getIntegrated() {
		return integrated;
	}

	public void setIntegrated(String integrated) {
		this.integrated = integrated;
	}

	public Long getRspower() {
		return rspower;
	}

	public void setRspower(Long rspower) {
		this.rspower = rspower;
	}

	public String getCoverRange() {
		return coverRange;
	}

	public void setCoverRange(String coverRange) {
		this.coverRange = coverRange;
	}


	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("{cell:").append(cell).append(",lng:").append(lng)
				.append(",lat:").append(lat).append(",chineseName:")
				.append(chineseName);
		buf.append(",azimuth:").append(azimuth).append(",mapType:")
				.append(mapType).append(",shapeType:").append(shapeType)
				.append(",shapeData:").append(allLngLats).append("}");
		return buf.toString();
	}

}
