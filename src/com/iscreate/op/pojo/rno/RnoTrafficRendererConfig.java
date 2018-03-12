package com.iscreate.op.pojo.rno;

/**
 * RnoTrafficRendererConfig entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RnoTrafficRendererConfig {

	// Fields

	private Long trafficRenId;
	private Long trafficType;
	private String name;
	private Float minValue;
	private Float maxValue;
	private String style;
	private Long areaId;
   	private Integer configOrder=0;
   	private String disabledFields;
   	private String params;
	public RnoTrafficRendererConfig copyRnoTrafficRendererConfig(){
		RnoTrafficRendererConfig config = new RnoTrafficRendererConfig();
		config.setAreaId(areaId);
		config.setMaxValue(maxValue);
		config.setMinValue(minValue);
		config.setName(name);
		config.setStyle(style);
		config.setTrafficRenId(trafficRenId);
		config.setTrafficType(trafficType);
		config.setConfigOrder(configOrder);
		config.setParams(params);
		config.setDisabledFields(disabledFields);
		return config;
	}

	/** default constructor */
	public RnoTrafficRendererConfig() {
	}


	public RnoTrafficRendererConfig(Long trafficType, String name,
			Float minValue, Float maxValue, String style,Integer configOrder,String disabledFields,String params) {
		this.trafficType = trafficType;
		this.name = name;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.style = style;
		this.areaId = areaId;
		this.configOrder=configOrder;
		this.disabledFields=disabledFields;
		this.params=params;
	}

	// Property accessors

	public Long getTrafficRenId() {
		return this.trafficRenId;
	}

	public void setTrafficRenId(Long trafficRenId) {
		this.trafficRenId = trafficRenId;
	}

	public Long getTrafficType() {
		return this.trafficType;
	}

	public void setTrafficType(Long trafficType) {
		this.trafficType = trafficType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getMinValue() {
		return this.minValue;
	}

	public void setMinValue(Float minValue) {
		this.minValue = minValue;
	}

	public Float getMaxValue() {
		return this.maxValue;
	}

	public void setMaxValue(Float maxValue) {
		this.maxValue = maxValue;
	}

	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}


	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}


	public Integer getConfigOrder() {
		return configOrder;
	}

	public void setConfigOrder(Integer configOrder) {
		this.configOrder = configOrder;
	}

	public String getDisabledFields() {
		return disabledFields;
	}

	public void setDisabledFields(String disabledFields) {
		this.disabledFields = disabledFields;
	}

	@Override
	public String toString() {
		return "RnoTrafficRendererConfig [trafficRenId=" + trafficRenId
				+ ", trafficType=" + trafficType + ", name=" + name
				+ ", minValue=" + minValue + ", maxValue=" + maxValue
				+ ", style=" + style + ", areaId=" + areaId + ", configOrder="
				+ configOrder + ", disabledFields=" + disabledFields + ", params=" + params+"]";
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}



}