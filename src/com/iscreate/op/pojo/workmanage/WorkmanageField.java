package com.iscreate.op.pojo.workmanage;

/**
 * WorkmanageField entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WorkmanageField implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String categoryFlag;
	private String fieldLabel;
	private String fieldName;
	private String fieldType;
	
	private String fieldNameShow;
	private String fieldNameValue;
	
	
	private String fieldLoadUrl;
	private Integer displayType;
	private Integer displayOrder;
	private String operator;
	private Integer nullable;
	private Integer width;
	private String rendererJs;

	// Constructors

	/** default constructor */
	public WorkmanageField() {
	}

	/** full constructor */
	public WorkmanageField(String categoryFlag, String fieldLabel,
			String fieldName, String fieldType, String fieldLoadUrl,
			Integer displayType, Integer displayOrder, String operator,
			Integer nullable, Integer width, String rendererJs) {
		this.categoryFlag = categoryFlag;
		this.fieldLabel = fieldLabel;
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.fieldLoadUrl = fieldLoadUrl;
		this.displayType = displayType;
		this.displayOrder = displayOrder;
		this.operator = operator;
		this.nullable = nullable;
		this.width = width;
		this.rendererJs = rendererJs;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoryFlag() {
		return this.categoryFlag;
	}

	public void setCategoryFlag(String categoryFlag) {
		this.categoryFlag = categoryFlag;
	}

	public String getFieldLabel() {
		return this.fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return this.fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldLoadUrl() {
		return this.fieldLoadUrl;
	}

	public void setFieldLoadUrl(String fieldLoadUrl) {
		this.fieldLoadUrl = fieldLoadUrl;
	}

	public Integer getDisplayType() {
		return this.displayType;
	}

	public void setDisplayType(Integer displayType) {
		this.displayType = displayType;
	}

	public Integer getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getNullable() {
		return this.nullable;
	}

	public void setNullable(Integer nullable) {
		this.nullable = nullable;
	}

	public Integer getWidth() {
		return this.width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getRendererJs() {
		return this.rendererJs;
	}

	public void setRendererJs(String rendererJs) {
		this.rendererJs = rendererJs;
	}

	public String getFieldNameShow() {
		return fieldNameShow;
	}

	public void setFieldNameShow(String fieldNameShow) {
		this.fieldNameShow = fieldNameShow;
	}

	public String getFieldNameValue() {
		return fieldNameValue;
	}

	public void setFieldNameValue(String fieldNameValue) {
		this.fieldNameValue = fieldNameValue;
	}
	
	

}