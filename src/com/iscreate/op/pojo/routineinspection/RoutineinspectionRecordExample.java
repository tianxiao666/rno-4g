package com.iscreate.op.pojo.routineinspection;

/**
 * RoutineinspectionTableExample entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RoutineinspectionRecordExample implements java.io.Serializable {

	// Fields

	private Long id;
	private String value;
	private String reId;
	private String reType;
	private String toId;
	private Long record_temp_id;
	private String remark;

	// Constructors

	/** default constructor */
	public RoutineinspectionRecordExample() {
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public String getToId() {
		return this.toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public Long getRecord_temp_id() {
		return record_temp_id;
	}

	public void setRecord_temp_id(Long record_temp_id) {
		this.record_temp_id = record_temp_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}