package com.iscreate.op.pojo.staffduty;
// default package

/**
 * Skill entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Skill implements java.io.Serializable {

	// Fields

	private Long id;
	private String skillType;
	private String skillDescription;
	private Long skillTypeId;

	// Constructors

	/** default constructor */
	public Skill() {
	}

	/** full constructor */
	public Skill(String skillType, String skillDescription, Long skillTypeId) {
		this.skillType = skillType;
		this.skillDescription = skillDescription;
		this.skillTypeId = skillTypeId;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSkillType() {
		return this.skillType;
	}

	public void setSkillType(String skillType) {
		this.skillType = skillType;
	}

	public String getSkillDescription() {
		return this.skillDescription;
	}

	public void setSkillDescription(String skillDescription) {
		this.skillDescription = skillDescription;
	}

	public Long getSkillTypeId() {
		return this.skillTypeId;
	}

	public void setSkillTypeId(Long skillTypeId) {
		this.skillTypeId = skillTypeId;
	}

}