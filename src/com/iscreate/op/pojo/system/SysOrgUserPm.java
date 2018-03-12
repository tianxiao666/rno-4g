package com.iscreate.op.pojo.system;


import java.util.Date;

/**
 * SysOrgUserPm entity. @author MyEclipse Persistence Tools
 */

public class SysOrgUserPm implements java.io.Serializable {

	// Fields

	private Long orgUserId;
	private String userNumber;
	private String name;
	private String email;
	private String backupEmail;
	private Long status;
	private String insuranceType;
	private String insuranceAddress;
	private String bankAddress;
	private String bankAccount;
	private String contract;
	private Long contractNum;
	private Date contractStartTime;
	private Date contractEndTime;
	private String gender;
	private Long originProv;
	private Long originCity;
	private String nation;
	private String marriage;
	private String mobile;
	private String idcard;
	private String highestEducationCode;
	private Long educationRank;
	private String graduateSchool;
	private String schoolProfessional;
	private Date graduateDate;
	private String homeAddress;
	private String relaPhone;
	private String note;
	private Date entryDate;
	private Date dimissionDate;
	private String serviceType;
	private String employmentType;
	private Date birthday;
	private String bloodType;
	private Long height;
	private Long weight;
	private String politicalAffiliation;
	private String accountPropeties;
	private String accountAddress;
	private String archivesAddress;
	private String address;
	private String cardAddress;
	private String originalIncome;
	private String originalBasicWage;
	private String originalReward;
	private String originalOtherSubsidies;
	private String exceptIncome;
	private String exceptOtherSubsidies;
	private String trainExperience;
	private String workExperience;
	private String socialRelations;
	private String careerOrientation;
	private String emergentNotice;
	private String employNature;
	private Date createTime;
	private Date modTime;
	private Integer degree;
	private String relaPeople;
	private Integer provident;
	private String dutyNote;

	// Constructors


	

	public SysOrgUserPm(Long orgUserId, String userNumber, String name,
			String email, String backupEmail, Long status,
			String insuranceType, String insuranceAddress, String bankAddress,
			String bankAccount, String contract, Long contractNum,
			Date contractStartTime, Date contractEndTime, String gender,
			Long originProv, Long originCity, String nation, String marriage,
			String mobile, String idcard, String highestEducationCode,
			Long educationRank, String graduateSchool,
			String schoolProfessional, Date graduateDate, String homeAddress,
			String relaPhone, String note, Date entryDate, Date dimissionDate,
			String serviceType, String employmentType, Date birthday,
			String bloodType, Long height, Long weight,
			String politicalAffiliation, String accountPropeties,
			String accountAddress, String archivesAddress, String address,
			String cardAddress, String originalIncome,
			String originalBasicWage, String originalReward,
			String originalOtherSubsidies, String exceptIncome,
			String exceptOtherSubsidies, String trainExperience,
			String workExperience, String socialRelations,
			String careerOrientation, String emergentNotice,
			String employNature, Date createTime, Date modTime, Integer degree,
			String relaPeople, Integer provident, String dutyNote) {
		super();
		this.orgUserId = orgUserId;
		this.userNumber = userNumber;
		this.name = name;
		this.email = email;
		this.backupEmail = backupEmail;
		this.status = status;
		this.insuranceType = insuranceType;
		this.insuranceAddress = insuranceAddress;
		this.bankAddress = bankAddress;
		this.bankAccount = bankAccount;
		this.contract = contract;
		this.contractNum = contractNum;
		this.contractStartTime = contractStartTime;
		this.contractEndTime = contractEndTime;
		this.gender = gender;
		this.originProv = originProv;
		this.originCity = originCity;
		this.nation = nation;
		this.marriage = marriage;
		this.mobile = mobile;
		this.idcard = idcard;
		this.highestEducationCode = highestEducationCode;
		this.educationRank = educationRank;
		this.graduateSchool = graduateSchool;
		this.schoolProfessional = schoolProfessional;
		this.graduateDate = graduateDate;
		this.homeAddress = homeAddress;
		this.relaPhone = relaPhone;
		this.note = note;
		this.entryDate = entryDate;
		this.dimissionDate = dimissionDate;
		this.serviceType = serviceType;
		this.employmentType = employmentType;
		this.birthday = birthday;
		this.bloodType = bloodType;
		this.height = height;
		this.weight = weight;
		this.politicalAffiliation = politicalAffiliation;
		this.accountPropeties = accountPropeties;
		this.accountAddress = accountAddress;
		this.archivesAddress = archivesAddress;
		this.address = address;
		this.cardAddress = cardAddress;
		this.originalIncome = originalIncome;
		this.originalBasicWage = originalBasicWage;
		this.originalReward = originalReward;
		this.originalOtherSubsidies = originalOtherSubsidies;
		this.exceptIncome = exceptIncome;
		this.exceptOtherSubsidies = exceptOtherSubsidies;
		this.trainExperience = trainExperience;
		this.workExperience = workExperience;
		this.socialRelations = socialRelations;
		this.careerOrientation = careerOrientation;
		this.emergentNotice = emergentNotice;
		this.employNature = employNature;
		this.createTime = createTime;
		this.modTime = modTime;
		this.degree = degree;
		this.relaPeople = relaPeople;
		this.provident = provident;
		this.dutyNote = dutyNote;
	}

	/** default constructor */
	public SysOrgUserPm() {
	}

	/** minimal constructor */
	public SysOrgUserPm(String userNumber, String name, Long status) {
		this.userNumber = userNumber;
		this.name = name;
		this.status = status;
	}

	/** full constructor */
	
	

	// Property accessors
	
	
	

	public Long getOrgUserId() {
		return this.orgUserId;
	}

	public void setOrgUserId(Long orgUserId) {
		this.orgUserId = orgUserId;
	}

	public String getUserNumber() {
		return this.userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBackupEmail() {
		return this.backupEmail;
	}

	public void setBackupEmail(String backupEmail) {
		this.backupEmail = backupEmail;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getInsuranceType() {
		return this.insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getInsuranceAddress() {
		return this.insuranceAddress;
	}

	public void setInsuranceAddress(String insuranceAddress) {
		this.insuranceAddress = insuranceAddress;
	}

	public String getBankAddress() {
		return this.bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getBankAccount() {
		return this.bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getContract() {
		return this.contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public Long getContractNum() {
		return this.contractNum;
	}

	public void setContractNum(Long contractNum) {
		this.contractNum = contractNum;
	}

	public Date getContractStartTime() {
		return this.contractStartTime;
	}

	public void setContractStartTime(Date contractStartTime) {
		this.contractStartTime = contractStartTime;
	}

	public Date getContractEndTime() {
		return this.contractEndTime;
	}

	public void setContractEndTime(Date contractEndTime) {
		this.contractEndTime = contractEndTime;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Long getOriginProv() {
		return this.originProv;
	}

	public void setOriginProv(Long originProv) {
		this.originProv = originProv;
	}

	public Long getOriginCity() {
		return this.originCity;
	}

	public void setOriginCity(Long originCity) {
		this.originCity = originCity;
	}

	public String getNation() {
		return this.nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getMarriage() {
		return this.marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdcard() {
		return this.idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getHighestEducationCode() {
		return this.highestEducationCode;
	}

	public void setHighestEducationCode(String highestEducationCode) {
		this.highestEducationCode = highestEducationCode;
	}

	public Long getEducationRank() {
		return this.educationRank;
	}

	public void setEducationRank(Long educationRank) {
		this.educationRank = educationRank;
	}

	public String getGraduateSchool() {
		return this.graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public String getSchoolProfessional() {
		return this.schoolProfessional;
	}

	public void setSchoolProfessional(String schoolProfessional) {
		this.schoolProfessional = schoolProfessional;
	}

	public Date getGraduateDate() {
		return this.graduateDate;
	}

	public void setGraduateDate(Date graduateDate) {
		this.graduateDate = graduateDate;
	}

	public String getHomeAddress() {
		return this.homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}


	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getDimissionDate() {
		return this.dimissionDate;
	}

	public void setDimissionDate(Date dimissionDate) {
		this.dimissionDate = dimissionDate;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getEmploymentType() {
		return this.employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getBloodType() {
		return this.bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public Long getHeight() {
		return this.height;
	}

	public void setHeight(Long height) {
		this.height = height;
	}

	public Long getWeight() {
		return this.weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public String getPoliticalAffiliation() {
		return this.politicalAffiliation;
	}

	public void setPoliticalAffiliation(String politicalAffiliation) {
		this.politicalAffiliation = politicalAffiliation;
	}

	public String getAccountPropeties() {
		return this.accountPropeties;
	}

	public void setAccountPropeties(String accountPropeties) {
		this.accountPropeties = accountPropeties;
	}

	public String getAccountAddress() {
		return this.accountAddress;
	}

	public void setAccountAddress(String accountAddress) {
		this.accountAddress = accountAddress;
	}

	public String getArchivesAddress() {
		return this.archivesAddress;
	}

	public void setArchivesAddress(String archivesAddress) {
		this.archivesAddress = archivesAddress;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCardAddress() {
		return this.cardAddress;
	}

	public void setCardAddress(String cardAddress) {
		this.cardAddress = cardAddress;
	}

	public String getOriginalIncome() {
		return this.originalIncome;
	}

	public void setOriginalIncome(String originalIncome) {
		this.originalIncome = originalIncome;
	}

	public String getOriginalBasicWage() {
		return this.originalBasicWage;
	}

	public void setOriginalBasicWage(String originalBasicWage) {
		this.originalBasicWage = originalBasicWage;
	}

	public String getOriginalReward() {
		return this.originalReward;
	}

	public void setOriginalReward(String originalReward) {
		this.originalReward = originalReward;
	}

	public String getOriginalOtherSubsidies() {
		return this.originalOtherSubsidies;
	}

	public void setOriginalOtherSubsidies(String originalOtherSubsidies) {
		this.originalOtherSubsidies = originalOtherSubsidies;
	}

	public String getExceptIncome() {
		return this.exceptIncome;
	}

	public void setExceptIncome(String exceptIncome) {
		this.exceptIncome = exceptIncome;
	}

	public String getExceptOtherSubsidies() {
		return this.exceptOtherSubsidies;
	}

	public void setExceptOtherSubsidies(String exceptOtherSubsidies) {
		this.exceptOtherSubsidies = exceptOtherSubsidies;
	}

	public String getTrainExperience() {
		return this.trainExperience;
	}

	public void setTrainExperience(String trainExperience) {
		this.trainExperience = trainExperience;
	}

	public String getWorkExperience() {
		return this.workExperience;
	}

	public void setWorkExperience(String workExperience) {
		this.workExperience = workExperience;
	}

	public String getSocialRelations() {
		return this.socialRelations;
	}

	public void setSocialRelations(String socialRelations) {
		this.socialRelations = socialRelations;
	}

	public String getCareerOrientation() {
		return this.careerOrientation;
	}

	public void setCareerOrientation(String careerOrientation) {
		this.careerOrientation = careerOrientation;
	}

	public String getEmergentNotice() {
		return this.emergentNotice;
	}

	public void setEmergentNotice(String emergentNotice) {
		this.emergentNotice = emergentNotice;
	}

	public String getEmployNature() {
		return this.employNature;
	}

	public void setEmployNature(String employNature) {
		this.employNature = employNature;
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

	public Integer getDegree() {
		return this.degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	public String getRelaPhone() {
		return relaPhone;
	}

	public void setRelaPhone(String relaPhone) {
		this.relaPhone = relaPhone;
	}

	public String getRelaPeople() {
		return relaPeople;
	}

	public void setRelaPeople(String relaPeople) {
		this.relaPeople = relaPeople;
	}

	public Integer getProvident() {
		return provident;
	}

	public void setProvident(Integer provident) {
		this.provident = provident;
	}

	public String getDutyNote() {
		return dutyNote;
	}

	public void setDutyNote(String dutyNote) {
		this.dutyNote = dutyNote;
	}

}