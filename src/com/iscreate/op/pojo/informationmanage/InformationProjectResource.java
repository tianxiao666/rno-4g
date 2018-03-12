package com.iscreate.op.pojo.informationmanage;

import com.google.gson.annotations.Expose;


/**
 * OrgAreaResource entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class InformationProjectResource implements java.io.Serializable {

	// Fields
	
	@Expose
	private Long id;
	@Expose
	private Long projectId;
	private InformationProject project;
	@Expose
	private Long areaId;
	private Area area;
	@Expose
	private Long orgId;
	@Expose
	private String orgType;
	@Expose
	private String resourceType;
	@Expose
	private Integer status;

	
	
	
	// Constructors

	/** default constructor */
	public InformationProjectResource() {
	}


	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgType() {
		return this.orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public InformationProject getProject() {
		return project;
	}

	public void setProject(InformationProject project) {
		this.project = project;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	
}


