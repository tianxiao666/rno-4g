package com.iscreate.op.pojo.cardispatch;

import java.util.Date;

/**
 * CardispatchCarterminalpair entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CardispatchCarterminalpair implements java.io.Serializable {

	// Fields

	private Long carterminalpairId;
	private Long carId;
	private Long terminalId;
	private Date allocateTime;
	private Long entityId;
	private String entityType;

	// Constructors

	/** default constructor */
	public CardispatchCarterminalpair() {
	}

	/** full constructor */
	public CardispatchCarterminalpair(Long carId, Long terminalId,
			Date allocateTime, Long entityId, String entityType) {
		this.carId = carId;
		this.terminalId = terminalId;
		this.allocateTime = allocateTime;
		this.entityId = entityId;
		this.entityType = entityType;
	}

	// Property accessors

	
	
	public Long getCarId() {
		return this.carId;
	}

	public Long getCarterminalpairId() {
		return carterminalpairId;
	}

	public void setCarterminalpairId(Long carterminalpairId) {
		this.carterminalpairId = carterminalpairId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public Long getTerminalId() {
		return this.terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	public Date getAllocateTime() {
		return this.allocateTime;
	}

	public void setAllocateTime(Date allocateTime) {
		this.allocateTime = allocateTime;
	}

	public Long getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return this.entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

}