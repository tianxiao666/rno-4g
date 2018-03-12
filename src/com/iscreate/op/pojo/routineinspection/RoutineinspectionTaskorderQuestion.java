package com.iscreate.op.pojo.routineinspection;

/**
 * RoutineinspectionTaskorderQuestion entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RoutineinspectionTaskorderQuestion implements java.io.Serializable {

	// Fields

	private Long id;
	private String toId;
	private String questionId;

	// Constructors

	/** default constructor */
	public RoutineinspectionTaskorderQuestion() {
	}

	/** minimal constructor */
	public RoutineinspectionTaskorderQuestion(Long id) {
		this.id = id;
	}

	/** full constructor */
	public RoutineinspectionTaskorderQuestion(Long id, String toId,
			String questionId) {
		this.id = id;
		this.toId = toId;
		this.questionId = questionId;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToId() {
		return this.toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

}