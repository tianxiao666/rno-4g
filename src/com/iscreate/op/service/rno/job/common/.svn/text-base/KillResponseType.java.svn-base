package com.iscreate.op.service.rno.job.common;


public enum KillResponseType{
	Killing("Killing"),
	Killed("Killed"),
	Rejected("Rejected");
	String code;
	private KillResponseType(String code){
		this.code=code;
	}
	public String getCode() {
		return code;
	}
	public static KillResponseType getByCode(String code){
		KillResponseType[] all=KillResponseType.values();
		for(KillResponseType jnt:all){
			if(jnt.code.equals(code)){
				return jnt;
			}
		}
		return null;
	}
}
