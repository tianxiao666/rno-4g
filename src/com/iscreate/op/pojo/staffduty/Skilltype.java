package com.iscreate.op.pojo.staffduty;
// default package

/**
 * Skilltype entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Skilltype implements java.io.Serializable {

	// Fields

	private Long id;
	private String skillTypeName;

	// Constructors

	/** default constructor */
	public Skilltype() {
	}

	/** full constructor */
	public Skilltype(String skillTypeName) {
		this.skillTypeName = skillTypeName;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSkillTypeName() {
		return this.skillTypeName;
	}

	public void setSkillTypeName(String skillTypeName) {
		this.skillTypeName = skillTypeName;
	}

}