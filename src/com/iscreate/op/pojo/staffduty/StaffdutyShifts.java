package com.iscreate.op.pojo.staffduty;
// default package

/**
 * StaffdutyShifts entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class StaffdutyShifts implements java.io.Serializable {

	// Fields

	private Long id;
	private Long groupId;
	private Integer timeLimit;
	private String shiftsName;
	private Integer freNum;
	private Integer isValid;

	// Constructors

	/** default constructor */
	public StaffdutyShifts() {
	}

	/** full constructor */
	public StaffdutyShifts(Long groupId, Integer timeLimit, String shiftsName,
			Integer freNum, Integer isValid) {
		this.groupId = groupId;
		this.timeLimit = timeLimit;
		this.shiftsName = shiftsName;
		this.freNum = freNum;
		this.isValid = isValid;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Integer getTimeLimit() {
		return this.timeLimit;
	}

	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String getShiftsName() {
		return this.shiftsName;
	}

	public void setShiftsName(String shiftsName) {
		this.shiftsName = shiftsName;
	}

	public Integer getFreNum() {
		return this.freNum;
	}

	public void setFreNum(Integer freNum) {
		this.freNum = freNum;
	}

	public Integer getIsValid() {
		return this.isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

}