package com.iscreate.op.service.rno.job.event;

import org.apache.hadoop.yarn.event.AbstractEvent;

import com.iscreate.op.service.rno.job.common.NodeResource;

public class JobNodeEvent extends AbstractEvent<JobNodeEventType> {

	private final NodeResource node;
    private Object attachData;
    
	public JobNodeEvent(NodeResource node, JobNodeEventType jobNodeEventType) {
		super(jobNodeEventType);
		this.node = node;
	}

	public NodeResource getNodeResource(){
		return this.node;
	}

	public Object getAttachData() {
		return attachData;
	}

	public void setAttachData(Object attachData) {
		this.attachData = attachData;
	}
	
	
}
