package com.iscreate.op.service.rno.job.client.api;

import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.net.NetUtils;
import org.apache.log4j.Logger;

import com.iscreate.op.service.rno.job.IsConfiguration;
import com.iscreate.op.service.rno.job.protocol.WorkerToManagerNodeProtocol;
import com.iscreate.op.service.rno.job.request.NodeHeartbeatRequest;
import com.iscreate.op.service.rno.job.request.NodeHeartbeatResponse;
import com.iscreate.op.service.rno.job.request.RegisterWorkerNodeRequest;
import com.iscreate.op.service.rno.job.request.RegisterWorkerNodeResponse;

public class WorkerToManagerNodeClientImpl extends WorkerToManagerNodeClient {

	private static Logger log=Logger.getLogger(WorkerToManagerNodeClientImpl.class);
	
	InetSocketAddress serverAddr;
	private WorkerToManagerNodeProtocol proxy;
	
	public WorkerToManagerNodeClientImpl() {
		super("WorkerToManagerNodeClientImpl");
	}
	
	public WorkerToManagerNodeClientImpl(String name) {
		super(name);
	}

    @Override
    protected void serviceInit(Configuration conf) throws Exception {
    	serverAddr=NetUtils.createSocketAddrForHost(conf.get(IsConfiguration.JobNodeManagerHost,IsConfiguration.JobNodeManagerHost_default),
    			conf.getInt(IsConfiguration.JobNodeManagerWorkerPort,IsConfiguration.JobNodeManagerWorkerPort_default));
    	super.serviceInit(conf);
    }
	
	@Override
	protected void serviceStart() throws Exception {
		proxy=RPC.waitForProxy(WorkerToManagerNodeProtocol.class, WorkerToManagerNodeProtocol.versionID, serverAddr, getConfig());
		
		super.serviceStart();
	}
	
	@Override
	protected void serviceStop() throws Exception {
		RPC.stopProxy(proxy);
		super.serviceStop();
	}
	
	@Override
	public RegisterWorkerNodeResponse registerWorkerNode(
			RegisterWorkerNodeRequest request) {
		log.debug("registerWorkerNode...request="+request);
		RegisterWorkerNodeResponse response=proxy.registerWorkerNode(request);
		log.debug("registerWorkerNode...response="+response);
		return  response;
	}

	@Override
	public NodeHeartbeatResponse nodeHeartbeat(NodeHeartbeatRequest request) {
		log.debug("nodeHeartbeat...request="+request);
		NodeHeartbeatResponse response=proxy.nodeHeartbeat(request);
		log.debug("nodeHeartbeat...response="+response);
		return  response;
	}

	
}
