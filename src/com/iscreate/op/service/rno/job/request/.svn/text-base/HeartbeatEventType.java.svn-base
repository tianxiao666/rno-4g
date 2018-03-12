package com.iscreate.op.service.rno.job.request;


public enum HeartbeatEventType{
	
	Normal("Normal"),
	Shutdown_req("Shutdown_req"),//本节点请求shutdown
	Shutdown_ack("Shutdown_ack");//本节点shutdown完成后，给管理节点的回应
	String code;
	
	private HeartbeatEventType(String code){
		this.code=code;
	}
	public String getCode() {
		return code;
	}
	public static HeartbeatEventType getByCode(String code){
		HeartbeatEventType[] all=HeartbeatEventType.values();
		for(HeartbeatEventType jnt:all){
			if(jnt.code.equals(code)){
				return jnt;
			}
		}
		return null;
	}
}