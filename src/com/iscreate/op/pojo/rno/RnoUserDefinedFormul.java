package com.iscreate.op.pojo.rno;

import java.util.Date;

/**
 * RnoUserDefinedFormul entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RnoUserDefinedFormul {

	// Fields

	private Long formulaId;
	private String name;
	private String condition;
	private String style;
	private String applyScope;
	private String scopeValue;
	private String moduleType;
	private String creater;
	private Date createTime;
	private Date modTime;
	private String status;

	// Constructors

	/** default constructor */
	public RnoUserDefinedFormul() {
	}

	/** full constructor */
	public RnoUserDefinedFormul(String name, String condition, String style,
			String applyScope, String scopeValue, String moduleType,
			String creater, Date createTime, Date modTime, String status) {
		this.name = name;
		this.condition = condition;
		this.style = style;
		this.applyScope = applyScope;
		this.scopeValue = scopeValue;
		this.moduleType = moduleType;
		this.creater = creater;
		this.createTime = createTime;
		this.modTime = modTime;
		this.status = status;
	}

	// Property accessors

	public Long getFormulaId() {
		return this.formulaId;
	}

	public void setFormulaId(Long formulaId) {
		this.formulaId = formulaId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCondition() {
		return this.condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getApplyScope() {
		return this.applyScope;
	}

	public void setApplyScope(String applyScope) {
		this.applyScope = applyScope;
	}
	public String getModuleType() {
		return this.moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getCreater() {
		return this.creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModTime() {
		return this.modTime;
	}

	public void setModTime(Date modTime) {
		this.modTime = modTime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getScopeValue() {
		return scopeValue;
	}

	public void setScopeValue(String scopeValue) {
		this.scopeValue = scopeValue;
	}

}