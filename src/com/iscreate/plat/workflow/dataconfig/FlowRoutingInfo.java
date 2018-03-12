package com.iscreate.plat.workflow.dataconfig;

/*******************************************************************************
 * 
 * 
 * 流程流转信息，路由信息
 * 
 * @author iscreate
 * 
 ******************************************************************************/

public class FlowRoutingInfo {
	public String flow_Id;
	public String start_node_id;
	public String end_node_id;
	public String node_des;
	
	public String routingId;

	public String getRoutingId() {
		return routingId;
	}

	public void setRoutingId(String routingId) {
		this.routingId = routingId;
	}

	public String getFlow_Id() {
		return flow_Id;
	}

	public void setFlow_Id(String flow_Id) {
		this.flow_Id = flow_Id;
	}

	public String getStart_node_id() {
		return start_node_id;
	}

	public void setStart_node_id(String start_node_id) {
		this.start_node_id = start_node_id;
	}

	public String getEnd_node_id() {
		return end_node_id;
	}

	public void setEnd_node_id(String end_node_id) {
		this.end_node_id = end_node_id;
	}

	public String getNode_des() {
		return node_des;
	}

	public void setNode_des(String node_des) {
		this.node_des = node_des;
	}

}
