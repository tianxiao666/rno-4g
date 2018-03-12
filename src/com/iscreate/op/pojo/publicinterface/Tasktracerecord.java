package com.iscreate.op.pojo.publicinterface;

import java.io.Serializable;
import java.util.Date;

/**
 * Workorderassnetresource entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Tasktracerecord{

	// Fields
	private Long id;
	private String woId;
	private String woType;
	private String toId;
	private String handler;
	private String handlerName;
	private String handleWay;
	private Date handleTime;
	private String handleResultDescription;

	// Constructors

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWoId() {
		return woId;
	}

	public void setWoId(String woId) {
		this.woId = woId;
	}

	public String getWoType() {
		return woType;
	}

	public void setWoType(String woType) {
		this.woType = woType;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getHandleWay() {
		return handleWay;
	}

	public void setHandleWay(String handleWay) {
		this.handleWay = handleWay;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public String getHandleResultDescription() {
		return handleResultDescription;
	}

	public void setHandleResultDescription(String handleResultDescription) {
		this.handleResultDescription = handleResultDescription;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	

	
}