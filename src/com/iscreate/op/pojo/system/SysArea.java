package com.iscreate.op.pojo.system;

import java.util.Date;
import java.util.TreeMap;

/**
 * SysArea entity. @author MyEclipse Persistence Tools
 */

public class SysArea implements java.io.Serializable {

	// Fields

	private Long areaId;
	private String name;
	private String areaLevel;
	private String entityType;
	private Long entityId;
	private Double longitude;
	private Double latitude;
	private Long parentId;
	private String path;
	private Date createtime;
	private Date updatetime;
	private TreeMap<String,SysArea> children;

	// Constructors

	/** default constructor */
	public SysArea() {
	}

	/** minimal constructor */
	public SysArea(String name, String areaLevel, String path) {
		this.name = name;
		this.areaLevel = areaLevel;
		this.path = path;
	}

	/** full constructor */
	public SysArea(String name, String areaLevel, String entityType,
			Long entityId, Double longitude, Double latitude, Long parentId,
			String path, Date createtime, Date updatetime) {
		this.name = name;
		this.areaLevel = areaLevel;
		this.entityType = entityType;
		this.entityId = entityId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.parentId = parentId;
		this.path = path;
		this.createtime = createtime;
		this.updatetime = updatetime;
	}

	// Property accessors

	public Long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAreaLevel() {
		return this.areaLevel;
	}

	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
	}

	public String getEntityType() {
		return this.entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Long getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public TreeMap<String, SysArea> getChildren() {
		return children;
	}

	public void setChildren(TreeMap<String, SysArea> children) {
		this.children = children;
	}

}