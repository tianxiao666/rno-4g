package com.iscreate.op.pojo.gisdispatch;

public class UserConfigTree {

	private String targetType;//目标类型
	private String targetKey;//目标id
	private String picLayerKey;	//图层id
	private String picUnitTypeKey;	//图元类型id
	private String littleIconKey;	//脚标id
	private Boolean isShow;	//是否显示

	
	
	
	public String getTargetKey() {
		return targetKey;
	}
	public void setTargetKey(String targetKey) {
		this.targetKey = targetKey;
	}
	public String getPicLayerKey() {
		return picLayerKey;
	}
	public void setPicLayerKey(String picLayerKey) {
		this.picLayerKey = picLayerKey;
	}
	public String getPicUnitTypeKey() {
		return picUnitTypeKey;
	}
	public void setPicUnitTypeKey(String picUnitTypeKey) {
		this.picUnitTypeKey = picUnitTypeKey;
	}
	public String getLittleIconKey() {
		return littleIconKey;
	}
	public void setLittleIconKey(String littleIconKey) {
		this.littleIconKey = littleIconKey;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public Boolean getIsShow() {
		return isShow;
	}
	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}
	
	
	public String toJson(){
		StringBuilder buf=new StringBuilder();
		buf.append("{");
		buf.append("targetType:'"+targetType+"'");
		buf.append(",");
		buf.append("targetKey:'"+targetKey+"'");
		buf.append(",");
		buf.append("picLayerKey:'"+picLayerKey+"'");
		buf.append(",");
		buf.append("littleIconKey:'"+littleIconKey+"'");
		buf.append(",");
		buf.append("isShow:"+isShow+"");
		buf.append("");
		buf.append("}");
		
		return buf.toString();
	}
	
	
}
