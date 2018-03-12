package com.iscreate.op.pojo.rno;

import java.math.BigDecimal;

import com.iscreate.op.service.rno.tool.DbValInject;

/**
 * RnoThreshold entity. @author chao.xj
 */

public class RnoThreshold implements java.io.Serializable {

	// Fields
	@DbValInject(dbField="ID",type="BigDecimal")
	private BigDecimal id;
	@DbValInject(dbField="ORDER_NUM",type="long")
	private long orderNum;
	@DbValInject(dbField="MODULE_TYPE",type="String")
	private String moduleType;
	@DbValInject(dbField="CODE",type="String")
	private String code;
	@DbValInject(dbField="DESC_INFO",type="String")
	private String descInfo;
	@DbValInject(dbField="DEFAULT_VAL",type="String")
	private String defaultVal;
	@DbValInject(dbField="SCOPE_DESC",type="String")
	private String scopeDesc;
	@DbValInject(dbField="CONDITION_GROUP",type="String")
	private String conditionGroup;
	// Constructors
	/** default constructor */
	public RnoThreshold() {
	}

	/** full constructor */
	public RnoThreshold(long orderNum, String moduleType, String code,
			String descInfo, String defaultVal, String scopeDesc) {
		this.orderNum = orderNum;
		this.moduleType = moduleType;
		this.code = code;
		this.descInfo = descInfo;
		this.defaultVal = defaultVal;
		this.scopeDesc = scopeDesc;
	}

	// Property accessors

	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public long getOrderNum() {
		return this.orderNum;
	}

	public void setOrderNum(long orderNum) {
		this.orderNum = orderNum;
	}

	public String getModuleType() {
		return this.moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescInfo() {
		return this.descInfo;
	}

	public void setDescInfo(String descInfo) {
		this.descInfo = descInfo;
	}

	public String getDefaultVal() {
		return this.defaultVal;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	public String getScopeDesc() {
		return this.scopeDesc;
	}

	public void setScopeDesc(String scopeDesc) {
		this.scopeDesc = scopeDesc;
	}
	public String getConditionGroup() {
		return conditionGroup;
	}

	public void setConditionGroup(String conditionGroup) {
		this.conditionGroup = conditionGroup;
	}
	@Override
	public String toString() {
		return "RnoThreshold [id=" + id + ", orderNum=" + orderNum
				+ ", moduleType=" + moduleType + ", code=" + code
				+ ", descInfo=" + descInfo + ", defaultVal=" + defaultVal
				+ ", scopeDesc=" + scopeDesc + "]";
	}

}