package com.iscreate.op.service.rno.job.client.api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.AbstractService;

import com.iscreate.op.service.rno.job.request.NodeHeartbeatRequest;
import com.iscreate.op.service.rno.job.request.NodeHeartbeatResponse;
import com.iscreate.op.service.rno.job.request.RegisterWorkerNodeRequest;
import com.iscreate.op.service.rno.job.request.RegisterWorkerNodeResponse;

public abstract class WorkerToManagerNodeClient extends AbstractService {

	public WorkerToManagerNodeClient(String name) {
		super(name);
	}

	public static WorkerToManagerNodeClient createWorkerToManagerNodeClient(
			Configuration conf) {
		WorkerToManagerNodeClientImpl client = new WorkerToManagerNodeClientImpl();
		client.init(conf);
		return client;
	}

	/**
	 * 向管理节点注册自己
	 * 
	 * @param request
	 * @return
	 */
	public abstract RegisterWorkerNodeResponse registerWorkerNode(
			RegisterWorkerNodeRequest request);

	/**
	 * 心跳信息
	 * 
	 * @param request
	 * @return
	 */
	public abstract NodeHeartbeatResponse nodeHeartbeat(
			NodeHeartbeatRequest request);

}
