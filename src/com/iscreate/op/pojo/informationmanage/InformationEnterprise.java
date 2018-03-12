package com.iscreate.op.pojo.informationmanage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

/**
 * InformationEnterprise entity.
 * 
 * @author MyEclipse Persistence Tools
 */


public class InformationEnterprise implements java.io.Serializable {

	// Fields
	@Expose
	private Long id;
	@Expose
	private String fullName;
	@Expose
	private String registerNumber;
	@Expose
	private String ownership;
	@Expose
	private String legalRepresentative;
	@Expose
	private String businessSphere;
	@Expose
	private String registerAddress;
	@Expose
	private String businessAddress;
	@Expose
	private String taxBearer;
	@Expose
	private String taxRegistrationNumber;
	@Expose
	private String zipCode;
	@Expose
	private String registerMoney;
	@Expose
	private String industryType;
	@Expose
	private String state;
	@Expose
	private String phone;
	@Expose
	private String telautogram;
	@Expose
	private String mailbox;
	@Expose
	private String internetUrl;
	@Expose
	private String cooperative;
	@Expose
	private Date createDate;
	@Expose
	private String loginAuthority;
	@Expose
	private String enterpriseSuffix;
	
	
	
	//private List<InformationProject> clientProjectsList;
	//private List<InformationProject> serverProjectsList;
	//private Map<String,InformationProject> clientProjectsMap;
	
	// Constructors

	/** default constructor */
	public InformationEnterprise() {
	}


	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getRegisterNumber() {
		return this.registerNumber;
	}

	public void setRegisterNumber(String registerNumber) {
		this.registerNumber = registerNumber;
	}

	public String getOwnership() {
		return this.ownership;
	}

	public void setOwnership(String ownership) {
		this.ownership = ownership;
	}

	public String getLegalRepresentative() {
		return this.legalRepresentative;
	}

	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}

	public String getBusinessSphere() {
		return this.businessSphere;
	}

	public void setBusinessSphere(String businessSphere) {
		this.businessSphere = businessSphere;
	}

	public String getRegisterAddress() {
		return this.registerAddress;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}

	public String getBusinessAddress() {
		return this.businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getTaxBearer() {
		return this.taxBearer;
	}

	public void setTaxBearer(String taxBearer) {
		this.taxBearer = taxBearer;
	}

	public String getTaxRegistrationNumber() {
		return this.taxRegistrationNumber;
	}

	public void setTaxRegistrationNumber(String taxRegistrationNumber) {
		this.taxRegistrationNumber = taxRegistrationNumber;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getRegisterMoney() {
		return this.registerMoney;
	}

	public void setRegisterMoney(String registerMoney) {
		this.registerMoney = registerMoney;
	}

	public String getIndustryType() {
		return this.industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTelautogram() {
		return this.telautogram;
	}

	public void setTelautogram(String telautogram) {
		this.telautogram = telautogram;
	}

	public String getMailbox() {
		return this.mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getInternetUrl() {
		return this.internetUrl;
	}

	public void setInternetUrl(String internetUrl) {
		this.internetUrl = internetUrl;
	}

	public String getCooperative() {
		return this.cooperative;
	}

	public void setCooperative(String cooperative) {
		this.cooperative = cooperative;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getLoginAuthority() {
		return this.loginAuthority;
	}

	public void setLoginAuthority(String loginAuthority) {
		this.loginAuthority = loginAuthority;
	}
	public String getEnterpriseSuffix() {
		return enterpriseSuffix;
	}

	public void setEnterpriseSuffix(String enterpriseSuffix) {
		this.enterpriseSuffix = enterpriseSuffix;
	}

	

	
}