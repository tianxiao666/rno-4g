package com.iscreate.op.pojo.workmanage;

/**
 * WorkmanageCountTaskorderObject entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WorkmanageCountTaskorderObject implements java.io.Serializable {

	// Fields

	private Long id;
	private String objectId;
	private String objectType;
	private Long taskCount;

	// Constructors

	/** default constructor */
	public WorkmanageCountTaskorderObject() {
	}

	/** full constructor */
	public WorkmanageCountTaskorderObject(String objectId, String objectType,
			Long taskCount) {
		this.objectId = objectId;
		this.objectType = objectType;
		this.taskCount = taskCount;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObjectId() {
		return this.objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Long getTaskCount() {
		return this.taskCount;
	}

	public void setTaskCount(Long taskCount) {
		this.taskCount = taskCount;
	}

}