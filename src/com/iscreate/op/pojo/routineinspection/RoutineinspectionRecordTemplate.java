package com.iscreate.op.pojo.routineinspection;

/**
 * RoutineinspectionTableTemplate entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RoutineinspectionRecordTemplate implements java.io.Serializable {

	// Fields

	private Long id;
	private String tag;
	private Integer sortOrder;
	private String terminalType;
	private String reId;
	private String reType;
	private Long rou_temp_id;

	// Constructors

	/** default constructor */
	public RoutineinspectionRecordTemplate() {
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getTerminalType() {
		return this.terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getReId() {
		return this.reId;
	}

	public void setReId(String reId) {
		this.reId = reId;
	}

	public String getReType() {
		return this.reType;
	}

	public void setReType(String reType) {
		this.reType = reType;
	}

	public Long getRou_temp_id() {
		return rou_temp_id;
	}

	public void setRou_temp_id(Long rou_temp_id) {
		this.rou_temp_id = rou_temp_id;
	}

}