package com.iscreate.op.service.rno.job.protocol;

import org.apache.hadoop.ipc.VersionedProtocol;

import com.iscreate.op.service.rno.job.request.NodeHeartbeatRequest;
import com.iscreate.op.service.rno.job.request.NodeHeartbeatResponse;
import com.iscreate.op.service.rno.job.request.RegisterWorkerNodeRequest;
import com.iscreate.op.service.rno.job.request.RegisterWorkerNodeResponse;

/**
 * 工作节点与管理节点的通信协议
 * @author brightming
 *
 */
public interface WorkerToManagerNodeProtocol extends VersionedProtocol{

	public static final long versionID=1L;
	
	/**
	 * 向管理节点注册自己
	 * @param request
	 * @return
	 */
	public RegisterWorkerNodeResponse registerWorkerNode(RegisterWorkerNodeRequest request);
	
	/**
	 * 心跳信息
	 * @param request
	 * @return
	 */
	public NodeHeartbeatResponse nodeHeartbeat(NodeHeartbeatRequest request);

	
	
}
