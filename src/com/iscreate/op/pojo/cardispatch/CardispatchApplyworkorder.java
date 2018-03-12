package com.iscreate.op.pojo.cardispatch;

/**
 * CardispatchApplyworkorder entity. @author MyEclipse Persistence Tools
 */

public class CardispatchApplyworkorder implements java.io.Serializable {

	// Fields

	private Long id;
	private String carWoId;
	private String associateWorkType;
	private String associateToId;
	private String associateWoId;
	
	// Constructors
	
	/** default constructor */
	public CardispatchApplyworkorder() {
	}

	/** full constructor */
	public CardispatchApplyworkorder(String carWoId, String associateWorkType,
			String associateToId, String associateWoId) {
		this.carWoId = carWoId;
		this.associateWorkType = associateWorkType;
		this.associateToId = associateToId;
		this.associateWoId = associateWoId;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCarWoId() {
		return this.carWoId;
	}

	public void setCarWoId(String carWoId) {
		this.carWoId = carWoId;
	}

	public String getAssociateWorkType() {
		return this.associateWorkType;
	}

	public void setAssociateWorkType(String associateWorkType) {
		this.associateWorkType = associateWorkType;
	}

	public String getAssociateToId() {
		return this.associateToId;
	}

	public void setAssociateToId(String associateToId) {
		this.associateToId = associateToId;
	}

	public String getAssociateWoId() {
		return this.associateWoId;
	}

	public void setAssociateWoId(String associateWoId) {
		this.associateWoId = associateWoId;
	}

}