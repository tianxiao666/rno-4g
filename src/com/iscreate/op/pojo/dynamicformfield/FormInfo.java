package com.iscreate.op.pojo.dynamicformfield;

/**
 * FormInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class FormInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private String formCode;
	private String formName;
	private String areacode;
	private String areaName;

	// Constructors

	/** default constructor */
	public FormInfo() {
	}

	/** minimal constructor */
	public FormInfo(Long id) {
		this.id = id;
	}

	/** full constructor */
	public FormInfo(Long id, String formCode, String formName, String areacode,
			String areaName) {
		this.id = id;
		this.formCode = formCode;
		this.formName = formName;
		this.areacode = areacode;
		this.areaName = areaName;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFormCode() {
		return this.formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	public String getFormName() {
		return this.formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getAreacode() {
		return this.areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}