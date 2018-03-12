package com.iscreate.op.pojo.bizmsg;

import java.util.Date;

public class BizMessage {

	private String id;
	private String content;
	private Date sendTime;
	private String receivePerson;
	private String sendPerson;
	private Integer state;
	private Date readTime;
	private Long message_type_id;
	private String type;
	private String link;
	private String linkForMobile;
	private String title;
	
	
	private String woId;
	private String toId;
	private String orgId;
	private String typeName;
	private BizmsgMessageType bisMsgType;
	private String functionType;
	
	public BizMessage(){
		
	}
	
	public String getFunctionType() {
		return functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getReceivePerson() {
		return receivePerson;
	}

	public void setReceivePerson(String receivePerson) {
		this.receivePerson = receivePerson;
	}

	public String getSendPerson() {
		return sendPerson;
	}

	public void setSendPerson(String sendPerson) {
		this.sendPerson = sendPerson;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public Long getMessage_type_id() {
		return message_type_id;
	}

	public void setMessage_type_id(Long message_type_id) {
		this.message_type_id = message_type_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLinkForMobile() {
		return linkForMobile;
	}

	public void setLinkForMobile(String linkForMobile) {
		this.linkForMobile = linkForMobile;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWoId() {
		return woId;
	}

	public void setWoId(String woId) {
		this.woId = woId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public BizmsgMessageType getBisMsgType() {
		return bisMsgType;
	}

	public void setBisMsgType(BizmsgMessageType bisMsgType) {
		this.bisMsgType = bisMsgType;
	}
	
	
}
