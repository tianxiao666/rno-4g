package com.iscreate.op.pojo.cardispatch;

/**
 * CardispatchGpsmileage entity. @author MyEclipse Persistence Tools
 */

public class CardispatchGpsmileage implements java.io.Serializable {

	// Fields

	private CardispatchGpsmileageId id;
	private String carNumber;
	private Long terminalId;
	private String terminalImei;
	private Double gpsMileage;

	// Constructors

	/** default constructor */
	public CardispatchGpsmileage() {
	}

	/** minimal constructor */
	public CardispatchGpsmileage(CardispatchGpsmileageId id) {
		this.id = id;
	}

	/** full constructor */
	public CardispatchGpsmileage(CardispatchGpsmileageId id, String carNumber,
			Long terminalId, String terminalImei, Double gpsMileage) {
		this.id = id;
		this.carNumber = carNumber;
		this.terminalId = terminalId;
		this.terminalImei = terminalImei;
		this.gpsMileage = gpsMileage;
	}

	// Property accessors

	public CardispatchGpsmileageId getId() {
		return this.id;
	}

	public void setId(CardispatchGpsmileageId id) {
		this.id = id;
	}

	public String getCarNumber() {
		return this.carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public Long getTerminalId() {
		return this.terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	public String getTerminalImei() {
		return this.terminalImei;
	}

	public void setTerminalImei(String terminalImei) {
		this.terminalImei = terminalImei;
	}

	public Double getGpsMileage() {
		return this.gpsMileage;
	}

	public void setGpsMileage(Double gpsMileage) {
		this.gpsMileage = gpsMileage;
	}

}