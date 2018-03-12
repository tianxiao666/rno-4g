package com.iscreate.op.pojo.dynamicformfield;

/**
 * FormControl entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FormControl implements java.io.Serializable {

	// Fields

	private Long controlId;
	private String formCode;
	private String name;
	private String defaultStr;
	private String controlTag;
	private String orderStr;
	private String cnName ;
	

	// Constructors

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	/** default constructor */
	public FormControl() {
	}

	/** minimal constructor */
	public FormControl(Long controlId) {
		this.controlId = controlId;
	}

	/** full constructor */
	public FormControl(Long controlId, String formCode, String name,
			String defaultStr, String controlTag, String orderStr) {
		this.controlId = controlId;
		this.formCode = formCode;
		this.name = name;
		this.defaultStr = defaultStr;
		this.controlTag = controlTag;
		this.orderStr = orderStr;
	}

	// Property accessors

	public Long getControlId() {
		return this.controlId;
	}

	public void setControlId(Long controlId) {
		this.controlId = controlId;
	}

	public String getFormCode() {
		return this.formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultStr() {
		return this.defaultStr;
	}

	public void setDefaultStr(String defaultStr) {
		this.defaultStr = defaultStr;
	}

	public String getControlTag() {
		return this.controlTag;
	}

	public void setControlTag(String controlTag) {
		this.controlTag = controlTag;
	}

	public String getOrderStr() {
		return this.orderStr;
	}

	public void setOrderStr(String orderStr) {
		this.orderStr = orderStr;
	}

}