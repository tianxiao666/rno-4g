package com.iscreate.op.pojo.routineinspection;

/**
 * RoutineinspectionExample entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RoutineinspectionExample implements java.io.Serializable {

	// Fields

	private Long id;
	private String reId;
	private String reType;
	private String woId;
	private String toId;

	// Constructors

	/** default constructor */
	public RoutineinspectionExample() {
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getWoId() {
		return this.woId;
	}

	public void setWoId(String woId) {
		this.woId = woId;
	}

	public String getToId() {
		return this.toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

}