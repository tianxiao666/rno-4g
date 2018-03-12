package com.iscreate.plat.tools.map;

import java.util.Map;

/**
 * 
 * @filename: MapAddressResult.java
 * @classpath: com.iscreate.plat.tools.map
 * @description: 调用经纬度转换实际地址的百度地图api 【geocoder】转换json格式数据用到
 * @author：yuan.yw
 * @date：Jul 22, 2013 3:56:22 PM
 * @version：
 */
public class MapAddressResult {
	private Map<String,Object> location;
	private String formatted_address;
	private String business;
	private Map<String,Object> addressComponent;
	private String cityCode;
	public Map<String, Object> getLocation() {
		return location;
	}
	public void setLocation(Map<String, Object> location) {
		this.location = location;
	}
	public String getFormatted_address() {
		return formatted_address;
	}
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public Map<String, Object> getAddressComponent() {
		return addressComponent;
	}
	public void setAddressComponent(Map<String, Object> addressComponent) {
		this.addressComponent = addressComponent;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
	/*
	 * 数据格式
		"result":
		{
		"location":{"lng":116.322987,"lat":39.983424071404},
		"formatted_address":"北京市海淀区中关村大街27号1101-08室",
		"business":"人民大学,中关村,苏州街",
		"addressComponent":
		{"city":"北京市",
		"district":"海淀区",
		"province":"北京市",
		"street":"中关村大街",
		"street_number":"27号1101-08室"},
		"cityCode":131
		}
		-------------*/
}
