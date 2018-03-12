package com.iscreate.op.pojo.cardispatch;

/**
 * CardispathTerminal entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CardispathTerminal implements java.io.Serializable {

	// Fields

	private Long terminalId;
	private String terminalName;
	private String clientversion;
	private String terminalComment;
	private Integer terminalState;
	private String clientimei;
	private String telphoneNo;
	private String launchedTime;
	private String expiredTime;
	private String terminalPic;
	private String mobileType;
	private String stateLastTime;
	private String terminalBizId;
	private Double monthlyRent;
	
	
	// Constructors

	/** default constructor */
	public CardispathTerminal() {
	}

	// Property accessors

	

	public String getClientversion() {
		return this.clientversion;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	public void setClientversion(String clientversion) {
		this.clientversion = clientversion;
	}

	public Integer getTerminalState() {
		return this.terminalState;
	}

	public void setTerminalState(Integer terminalState) {
		this.terminalState = terminalState;
	}

	public String getClientimei() {
		return this.clientimei;
	}

	public void setClientimei(String clientimei) {
		this.clientimei = clientimei;
	}

	public String getTelphoneNo() {
		return this.telphoneNo;
	}

	public void setTelphoneNo(String telphoneNo) {
		this.telphoneNo = telphoneNo;
	}

	public String getLaunchedTime() {
		return this.launchedTime;
	}

	public void setLaunchedTime(String launchedTime) {
		this.launchedTime = launchedTime;
	}

	public String getExpiredTime() {
		return this.expiredTime;
	}

	public void setExpiredTime(String expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getTerminalPic() {
		return this.terminalPic;
	}

	public void setTerminalPic(String terminalPic) {
		this.terminalPic = terminalPic;
	}

	public String getMobileType() {
		return this.mobileType;
	}

	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}

	public String getStateLastTime() {
		return this.stateLastTime;
	}

	public void setStateLastTime(String stateLastTime) {
		this.stateLastTime = stateLastTime;
	}

	public String getTerminalBizId() {
		return terminalBizId;
	}

	public void setTerminalBizId(String terminalBizId) {
		this.terminalBizId = terminalBizId;
	}

	public Double getMonthlyRent() {
		return monthlyRent;
	}

	public void setMonthlyRent(Double monthlyRent) {
		this.monthlyRent = monthlyRent;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public String getTerminalComment() {
		return terminalComment;
	}

	public void setTerminalComment(String terminalComment) {
		this.terminalComment = terminalComment;
	}

	
}