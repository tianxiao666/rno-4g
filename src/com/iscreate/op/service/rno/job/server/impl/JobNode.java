package com.iscreate.op.service.rno.job.server.impl;

import org.apache.hadoop.service.CompositeService;
import org.apache.hadoop.yarn.state.StateMachine;

import com.iscreate.op.service.rno.job.IsConfiguration;
import com.iscreate.op.service.rno.job.JobNodeId;
import com.iscreate.op.service.rno.job.common.JobNodeState;
import com.iscreate.op.service.rno.job.event.JobNodeEvent;
import com.iscreate.op.service.rno.job.event.JobNodeEventType;

public abstract class JobNode extends CompositeService {

	
	
	//默认状态
	JobNodeState defaultInitState=JobNodeState.OutOfService;
	//当前状态
	JobNodeState curState=JobNodeState.OutOfService;
	
	JobNodeId jobNodeId;
	protected StateMachine<JobNodeState,JobNodeEventType,JobNodeEvent> statemachine; 
	
	public JobNode(String name) {
		super(name);
	}

	
	public static JobManagerNode createJobManagerNode(IsConfiguration conf,JobNodeId jobNodeId){
		JobManagerNode node=new JobManagerNode();
		node.jobNodeId=jobNodeId;
		node.init(conf);
		
		return node;
		
	}
	
	public static JobWorkerNode createJobWorkerNode(IsConfiguration conf,JobNodeId jobNodeId){
		JobWorkerNode node=new JobWorkerNode();
		node.jobNodeId=jobNodeId;
		node.init(conf);
		
		return node;
	}


	public JobNodeId getJobNodeId() {
		return jobNodeId;
	}
	
	
}
