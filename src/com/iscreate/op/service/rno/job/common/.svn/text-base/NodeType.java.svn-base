package com.iscreate.op.service.rno.job.common;


public enum NodeType{
	ManagerNode("ManagerNode"),
	WorkerNode("WorkerNode"),
	HadoopNodeMgrNode("HadoopNodeMgrNode");
	
	String code;
	private NodeType(String code){
		this.code=code;
	}
	public String getCode() {
		return code;
	}
	public static NodeType getByCode(String code){
		NodeType[] all=NodeType.values();
		for(NodeType jnt:all){
			if(jnt.code.equals(code)){
				return jnt;
			}
		}
		return null;
	}
}
