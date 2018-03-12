package com.iscreate.op.pojo.routineinspection;

import java.io.Serializable;
import java.util.Date;

/**
 * RoutineinspectionPlanworkorder entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RoutineinspectionPlanworkorder implements Serializable {

	// Fields

	private Long id;
	private String routineinspectionWoId;
	private String planTitle;
	private Date planStartTime;
	private Date planEndTime;
	private String remark;
	private Long orgId;
	
	private Long templateId;
	private Integer type;
	private String routineInspectionProfession;
	private Integer vipBaseStationRoutineInspectionCount;

	// Constructors

	/** default constructor */
	public RoutineinspectionPlanworkorder() {
	}

	/** full constructor */
	public RoutineinspectionPlanworkorder(String routineinspectionWoId,
			String planTitle, Date planStartTime, Date planEndTime,
			String remark, Long orgId, Integer type,
			String routineInspectionProfession,
			Integer vipBaseStationRoutineInspectionCount) {
		this.routineinspectionWoId = routineinspectionWoId;
		this.planTitle = planTitle;
		this.planStartTime = planStartTime;
		this.planEndTime = planEndTime;
		this.remark = remark;
		this.orgId = orgId;
		this.type = type;
		this.routineInspectionProfession = routineInspectionProfession;
		this.vipBaseStationRoutineInspectionCount = vipBaseStationRoutineInspectionCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoutineinspectionWoId() {
		return routineinspectionWoId;
	}

	public void setRoutineinspectionWoId(String routineinspectionWoId) {
		this.routineinspectionWoId = routineinspectionWoId;
	}

	public String getPlanTitle() {
		return planTitle;
	}

	public void setPlanTitle(String planTitle) {
		this.planTitle = planTitle;
	}

	public Date getPlanStartTime() {
		return planStartTime;
	}

	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}

	public Date getPlanEndTime() {
		return planEndTime;
	}

	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRoutineInspectionProfession() {
		return routineInspectionProfession;
	}

	public void setRoutineInspectionProfession(String routineInspectionProfession) {
		this.routineInspectionProfession = routineInspectionProfession;
	}

	public Integer getVipBaseStationRoutineInspectionCount() {
		return vipBaseStationRoutineInspectionCount;
	}

	public void setVipBaseStationRoutineInspectionCount(
			Integer vipBaseStationRoutineInspectionCount) {
		this.vipBaseStationRoutineInspectionCount = vipBaseStationRoutineInspectionCount;
	}

	// Property accessors

	

}