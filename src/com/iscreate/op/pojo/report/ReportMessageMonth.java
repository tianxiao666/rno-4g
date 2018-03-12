package com.iscreate.op.pojo.report;

import java.io.Serializable;
import java.util.Date;

/**
 * Workorderassnetresource entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ReportMessageMonth{

	// Fields
	private Long id;
	private String indicators;
	private String dimension;
	private Long organizationId;
	private Date statisticaltime;
	public Long getId() {
		return id;
	}
	public String getIndicators() {
		return indicators;
	}
	public void setIndicators(String indicators) {
		this.indicators = indicators;
	}
	public String getDimension() {
		return dimension;
	}
	public void setDimension(String dimension) {
		this.dimension = dimension;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Date getStatisticaltime() {
		return statisticaltime;
	}
	public void setStatisticaltime(Date statisticaltime) {
		this.statisticaltime = statisticaltime;
	}
	public void setId(Long id) {
		this.id = id;
	}

	// Constructors


	

	
}