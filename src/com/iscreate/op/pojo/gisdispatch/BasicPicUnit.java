package com.iscreate.op.pojo.gisdispatch;

import com.iscreate.op.constant.GisDispatchGraphConstant;


/**
 * 基本图元
 * 
 * @author brightming
 * 
 */
public class BasicPicUnit {

	// 图元唯一标识
	private String id;
	// 图元类型
	private String type;
	// 图元名称
	private String name;

	// 经纬度
	private LatLng latlng;
	// 图标
	private String icon;

	private boolean isVisible;// 是否可见
	
	private String isOnDuty;//是否上班

	private String address;	//当前地址
	
	// 关联的任务信息
	private TaskInfo taskInfo;

	private PicUnitType picUnitType;// 关联某种图元类型
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LatLng getLatlng() {
		return latlng;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setLatlng(LatLng latlng) {
		this.latlng = latlng;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TaskInfo getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}

	public PicUnitType getPicUnitType() {
		return picUnitType;
	}

	public void setPicUnitType(PicUnitType picUnitType) {
		this.picUnitType = picUnitType;
	}

	public String getIsOnDuty() {
		return isOnDuty;
	}

	public void setIsOnDuty(String isOnDuty) {
		this.isOnDuty = isOnDuty;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BasicPicUnit) {
			BasicPicUnit bpu = (BasicPicUnit) obj;
			if (bpu.getType().equals(getType()) && bpu.getId().equals(getId())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 是否包含在指定经纬度范围内
	 * 
	 * @param latLngBounds
	 * @return
	 * 
	 * $Author gmh
	 * 
	 * 2012-3-23 上午11:15:48
	 */
	public boolean containedIn(LatLngBounds latLngBounds) {
		if (latlng == null) {
			return false;
		}
		if (this.latlng.containedIn(latLngBounds)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 获取bpu的key
	 */
	public String getKey() {
		return type + "_" + id;
	}

	/**
	 * 获取信息窗口的内容
	 * 
	 * Mar 28, 2012 11:41:05 AM gmh
	 */
	public void getInfoWindowMsg() {

	}

	/**
	 * 转为json格式
	 * 
	 * @return Author gmh 2012-3-22 下午03:45:53
	 */
	public String toJson() {
		String result = "{";

		result += "name:'" + name + "',type:'" + type + "',key:'" + getKey()
				+ "',icon:'" + icon + "',isVisible:"+isVisible+"," + "latitude:"
				+ latlng.getLatitude() + ",longitude:" + latlng.getLongitude();
		result += ",title:'" + name + "',isOnDuty:'"+getIsOnDuty()+"',address:'"+getAddress()+"'";
		if (taskInfo != null) {
			result += ",taskInfo:" + taskInfo.toJson();
		}else {
			result += ",taskInfo:{totalTaskCount:0}";
		}

		result += "}";

		return result;
	}

	/**
	 * 
	 * @return Author gmh 2012-3-23 上午10:18:23
	 */
	public String toGetTaskInfoJson() {
		String result = "{";

		result += "key:'" + getKey() + "'";
		if (taskInfo != null) {
			result += ",taskInfo:" + taskInfo.toJson();
		} else {
			result += ",taskInfo:{totalTaskCount:0}";
		}
		if(GisDispatchGraphConstant.RESOURCE_HUMAN.equals(this.type) || GisDispatchGraphConstant.RESOURCE_CAR.equals(this.type)){
			result+=",longitude:"+latlng.getLongitude()+",latitude:"+latlng.getLatitude();
		}
		
		result += "}";

		return result;
	}

	/**
	 * 返回最简单的信息
	 * 
	 * @return
	 * 
	 * $Author gmh
	 * 
	 * 2012-3-23 下午03:03:16
	 */
	public String toSimpleJson() {
		String result = "{key:'" + getKey() + "'" + ",latitude:"
				+ latlng.getLatitude() + ",longitude:" + latlng.getLongitude()
				+ "}";
		return result;
	}

}
