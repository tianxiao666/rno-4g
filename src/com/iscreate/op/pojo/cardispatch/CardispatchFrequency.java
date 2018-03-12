package com.iscreate.op.pojo.cardispatch;

import java.util.Date;

/**
 * CardispatchFrequency entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class CardispatchFrequency implements java.io.Serializable {

	// Fields

	private Long id;
	private Long shiftsid;
	private String frequencyname;
	private String starttime;
	private String endtime;
	private Integer ordernumber;
	private Date lastedittime;
	private Long entityId;
	private String entityType;

	// Constructors

	/** default constructor */
	public CardispatchFrequency() {
	}

	/** minimal constructor */
	public CardispatchFrequency(Date lastedittime) {
		this.lastedittime = lastedittime;
	}

	/** full constructor */
	public CardispatchFrequency(Long shiftsid, String frequencyname,
			String starttime, String endtime, Integer ordernumber,
			Date lastedittime, Long entityId, String entityType) {
		this.shiftsid = shiftsid;
		this.frequencyname = frequencyname;
		this.starttime = starttime;
		this.endtime = endtime;
		this.ordernumber = ordernumber;
		this.lastedittime = lastedittime;
		this.entityId = entityId;
		this.entityType = entityType;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShiftsid() {
		return this.shiftsid;
	}

	public void setShiftsid(Long shiftsid) {
		this.shiftsid = shiftsid;
	}

	public String getFrequencyname() {
		return this.frequencyname;
	}

	public void setFrequencyname(String frequencyname) {
		this.frequencyname = frequencyname;
	}

	public String getStarttime() {
		return this.starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return this.endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public Integer getOrdernumber() {
		return this.ordernumber;
	}

	public void setOrdernumber(Integer ordernumber) {
		this.ordernumber = ordernumber;
	}

	public Date getLastedittime() {
		return this.lastedittime;
	}

	public void setLastedittime(Date lastedittime) {
		this.lastedittime = lastedittime;
	}

	public Long getEntityId() {
		return this.entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return this.entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

}