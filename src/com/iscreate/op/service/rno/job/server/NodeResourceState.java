package com.iscreate.op.service.rno.job.server;


/**
 * 管理的节点的可用状态
 * @author brightming
 *
 */
public enum NodeResourceState {
    Alive("Alive"),
    LoseContact("LoseContact"),
    Shutdown("Shutdown");//发出了shutdown请求，还未收到shutdown_ack的
	String code;
	private NodeResourceState(String code){
		this.code=code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public static NodeResourceState getByCode(String code){
		NodeResourceState[] all=NodeResourceState.values();
		for(NodeResourceState jnt:all){
			if(jnt.code.equals(code)){
				return jnt;
			}
		}
		return null;
	}
	
}
