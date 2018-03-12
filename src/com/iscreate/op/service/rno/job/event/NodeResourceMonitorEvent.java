package com.iscreate.op.service.rno.job.event;

import org.apache.hadoop.yarn.event.AbstractEvent;

public class NodeResourceMonitorEvent extends AbstractEvent<NodeResourceMonitorEventType> {

	public NodeResourceMonitorEvent(NodeResourceMonitorEventType type) {
		super(type);
	}

}
