package com.iscreate.op.pojo.gisdispatch;

import java.util.List;

/**
 * 图元类型
 * 
 * @author gmh
 * 
 *         2012-3-21下午04:13:20
 */
public class PicUnitType {

	private long id;// 主键
	private String name;// 类型名称
	private String code;//类型编码
	private String icon;// 默认图标
	private int position;// 在图层中的排列顺序
	private PicLayer picLayer;// 关联的图层
	
	private boolean isGonglishuVisible;//是否在可见公里数范围内

	private boolean needShow;// 用户设置的是否显示
	private PicUnitTypeMile picUnitTypeMile;// 关联的公里数设置
	private List<LittleIcon> littleIcons;// 关联的脚标

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public PicLayer getPicLayer() {
		return picLayer;
	}

	public void setPicLayer(PicLayer picLayer) {
		this.picLayer = picLayer;
	}

	public PicUnitTypeMile getPicUnitTypeMile() {
		return picUnitTypeMile;
	}

	public void setPicUnitTypeMile(PicUnitTypeMile picUnitTypeMile) {
		this.picUnitTypeMile = picUnitTypeMile;
	}

	public List<LittleIcon> getLittleIcons() {
		return littleIcons;
	}

	public void setLittleIcons(List<LittleIcon> littleIcons) {
		this.littleIcons = littleIcons;
	}

	public boolean isNeedShow() {
		return needShow;
	}

	public void setNeedShow(boolean needShow) {
		this.needShow = needShow;
	}
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public String getKey(){
		return id+"";
	}
	
	
	public boolean isGonglishuVisible() {
		return isGonglishuVisible;
	}

	public void setGonglishuVisible(boolean isGonglishuVisible) {
		this.isGonglishuVisible = isGonglishuVisible;
	}

	/**
	 * 当前可见公里数，是否可见
	 * 
	 * @param currentMileKm
	 * @return
	 * 
	 *         $Author gmh
	 * 
	 *         2012-3-23 下午03:22:40
	 */
	public boolean inShowRange(double currentMileKm) {
		if (this.getPicUnitTypeMile().getMinVisibleMile() <=currentMileKm  
				&& this.getPicUnitTypeMile().getMaxVisibleMile() >= currentMileKm) { 
			return true;
		} else {
			return false;
		}
	}

}
