package com.iscreate.op.service.rno.job.server.impl;

import org.apache.hadoop.conf.Configuration;

import com.iscreate.op.service.rno.job.event.IsAsyncDispatcher;
import com.iscreate.op.service.rno.job.server.service.JobClientService;
import com.iscreate.op.service.rno.job.server.service.NodeResourceManagerService;

public class JMContext {

	private JobManagerNode managerNode;
    private IsAsyncDispatcher dispatcher;
    private Configuration conf;
    
	public JobManagerNode getManagerNode() {
		return managerNode;
	}

	public void setManagerNode(JobManagerNode managerNode) {
		this.managerNode = managerNode;
	}

	public IsAsyncDispatcher getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(IsAsyncDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public Configuration getConf() {
		return conf;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}
	
	
}
