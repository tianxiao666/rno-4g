package com.iscreate.op.pojo.dynamicformfield;

/**
 * FormControlAttributeDefine entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FormControlAttributeDefine implements java.io.Serializable {

	// Fields

	private String attributeId;
	private String attributeType;
	private String attributeName;
	private String controlTag;

	// Constructors

	/** default constructor */
	public FormControlAttributeDefine() {
	}

	/** minimal constructor */
	public FormControlAttributeDefine(String attributeId) {
		this.attributeId = attributeId;
	}

	/** full constructor */
	public FormControlAttributeDefine(String attributeId, String attributeType,
			String attributeName, String controlTag) {
		this.attributeId = attributeId;
		this.attributeType = attributeType;
		this.attributeName = attributeName;
		this.controlTag = controlTag;
	}

	// Property accessors

	public String getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}

	public String getAttributeType() {
		return this.attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getControlTag() {
		return this.controlTag;
	}

	public void setControlTag(String controlTag) {
		this.controlTag = controlTag;
	}

}