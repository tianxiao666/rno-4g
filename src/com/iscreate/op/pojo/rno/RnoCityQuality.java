package com.iscreate.op.pojo.rno;

import java.util.Date;

/**
 * RnoCityQuality entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class RnoCityQuality implements java.io.Serializable {

	// Fields

	private Long cityqulId;
	private String grade;
	private Long areaId;
	private Double score;
	private Date staticTime;
	private Date createTime;

	// Constructors

	/** default constructor */
	public RnoCityQuality() {
	}

	/** full constructor */
	public RnoCityQuality(String grade, Long areaId, Double score,
			Date staticTime, Date createTime) {
		this.grade = grade;
		this.areaId = areaId;
		this.score = score;
		this.staticTime = staticTime;
		this.createTime = createTime;
	}

	// Property accessors

	public Long getCityqulId() {
		return this.cityqulId;
	}

	public void setCityqulId(Long cityqulId) {
		this.cityqulId = cityqulId;
	}

	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Date getStaticTime() {
		return this.staticTime;
	}

	public void setStaticTime(Date staticTime) {
		this.staticTime = staticTime;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}