package com.iscreate.op.pojo.informationmanage;

import java.util.Date;

import com.google.gson.annotations.Expose;

/**
 * Staff entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Staff implements java.io.Serializable {

	// Fields
	@Expose
	private String id;
	@Expose
	private String staffId;
	@Expose
	private String staffName;
	@Expose
	private String sex;
	@Expose
	private Integer age;
	@Expose
	private String contactPhone;
	@Expose
	private String cellPhoneNumber;
	@Expose
	private String email;
	@Expose
	private String qqNumber;
	@Expose
	private String description;
	@Expose
	private Long entityId;
	@Expose
	private String entityType;
	@Expose
	private Short staffTypeId;
	@Expose
	private Date birthday;
	@Expose
	private String degree;
	@Expose
	private Date graduateDate;
	@Expose
	private String idCardNum;
	@Expose
	private Short state;
	@Expose
	private String picture;

	// Constructors

	/** default constructor */
	public Staff() {
	}

	/** full constructor */
	public Staff(String staffId, String staffName, String sex, Integer age,
			String contactPhone, String cellPhoneNumber, String email,
			String qqNumber, String description, Long entityId,
			String entityType, Short staffTypeId, Date birthday, String degree,
			Date graduateDate, String idCardNum, Short state, String picture) {
		this.staffId = staffId;
		this.staffName = staffName;
		this.sex = sex;
		this.age = age;
		this.contactPhone = contactPhone;
		this.cellPhoneNumber = cellPhoneNumber;
		this.email = email;
		this.qqNumber = qqNumber;
		this.description = description;
		this.entityId = entityId;
		this.entityType = entityType;
		this.staffTypeId = staffTypeId;
		this.birthday = birthday;
		this.degree = degree;
		this.graduateDate = graduateDate;
		this.idCardNum = idCardNum;
		this.state = state;
		this.picture = picture;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStaffId() {
		return this.staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return this.staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getCellPhoneNumber() {
		return this.cellPhoneNumber;
	}

	public void setCellPhoneNumber(String cellPhoneNumber) {
		this.cellPhoneNumber = cellPhoneNumber;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQqNumber() {
		return this.qqNumber;
	}

	public void setQqNumber(String qqNumber) {
		this.qqNumber = qqNumber;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Short getStaffTypeId() {
		return this.staffTypeId;
	}

	public void setStaffTypeId(Short staffTypeId) {
		this.staffTypeId = staffTypeId;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public Date getGraduateDate() {
		return this.graduateDate;
	}

	public void setGraduateDate(Date graduateDate) {
		this.graduateDate = graduateDate;
	}

	public String getIdCardNum() {
		return this.idCardNum;
	}

	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}

	public Short getState() {
		return this.state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}