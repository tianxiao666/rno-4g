package com.iscreate.op.service.rno.job.common;

public enum ListenPortType {
	ClientPort("ClientPort"),
	WorkerNodeCtrlPort("WorkerNodeCtrlPort"),
	ManagerWorkerPort("ManagerWorkerPort");
	
	String code;
	private ListenPortType(String code){
		this.code=code;
	}
	public String getCode() {
		return code;
	}
	public static ListenPortType getByCode(String code){
		ListenPortType[] all=ListenPortType.values();
		for(ListenPortType one:all){
			if(one.code.equals(code)){
				return one;
			}
		}
		return null;
	}
}
