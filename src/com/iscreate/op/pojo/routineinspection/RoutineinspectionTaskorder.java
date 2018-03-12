package com.iscreate.op.pojo.routineinspection;

import java.util.Date;

/**
 * RoutineinspectionTaskorder entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RoutineinspectionTaskorder implements java.io.Serializable {

	// Fields

	private Long id;
	private String routineinspectionToId;
	private String taskTitle;
	private Date taskPlanBeginTime;
	private Date taskPlanEndTime;
	private Long orgId;
	private String routineinspectionWoId;
	private String resourceType;
	private String resourceId;
	private String routineInspectionProfession;
	private String orgName;
	private String resourceName;
	private Double longitude;
	private Double latitude;
	private Date signInTime;
	private Date signOutTime;
	private Float deviate;

	// Constructors

	/** default constructor */
	public RoutineinspectionTaskorder() {
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoutineinspectionToId() {
		return this.routineinspectionToId;
	}

	public void setRoutineinspectionToId(String routineinspectionToId) {
		this.routineinspectionToId = routineinspectionToId;
	}

	public String getTaskTitle() {
		return this.taskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}

	public Date getTaskPlanBeginTime() {
		return this.taskPlanBeginTime;
	}

	public void setTaskPlanBeginTime(Date taskPlanBeginTime) {
		this.taskPlanBeginTime = taskPlanBeginTime;
	}

	public Date getTaskPlanEndTime() {
		return this.taskPlanEndTime;
	}

	public void setTaskPlanEndTime(Date taskPlanEndTime) {
		this.taskPlanEndTime = taskPlanEndTime;
	}

	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getRoutineinspectionWoId() {
		return this.routineinspectionWoId;
	}

	public void setRoutineinspectionWoId(String routineinspectionWoId) {
		this.routineinspectionWoId = routineinspectionWoId;
	}

	public String getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getRoutineInspectionProfession() {
		return this.routineInspectionProfession;
	}

	public void setRoutineInspectionProfession(
			String routineInspectionProfession) {
		this.routineInspectionProfession = routineInspectionProfession;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Date getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(Date signInTime) {
		this.signInTime = signInTime;
	}

	public Date getSignOutTime() {
		return signOutTime;
	}

	public void setSignOutTime(Date signOutTime) {
		this.signOutTime = signOutTime;
	}

	public Float getDeviate() {
		return deviate;
	}

	public void setDeviate(Float deviate) {
		this.deviate = deviate;
	}

}