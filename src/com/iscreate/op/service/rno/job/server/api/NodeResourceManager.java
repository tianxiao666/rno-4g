package com.iscreate.op.service.rno.job.server.api;

import java.util.List;

import com.iscreate.op.service.rno.job.JobNodeId;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;
import com.iscreate.op.service.rno.job.common.NodeResource;

public interface NodeResourceManager {

	/**
	 * 添加一个节点
	 * @param workerNode
	 * @return
	 */
	public boolean addNode(JobWorkerStatus workerNode);
	
	/**
	 * 移除一个节点
	 * 如果该节点有未完成的任务，则移除失败
	 * @param jobNodeId
	 * @return
	 */
	public boolean removeNode(JobNodeId jobNodeId);
	
	/**
	 * 更新一个节点的信息
	 * @param workerNode
	 */
	public void touchNode(JobWorkerStatus workerNode);
	
	/**
	 * 获取一个node的信息
	 * @param jobNodeId
	 * @return
	 */
	public NodeResource getNode(JobNodeId jobNodeId);
	
	/**
	 * 获取所有的节点的信息
	 * @return
	 */
	public List<NodeResource> getAllNodes();
	
	/**
	 * 获取能完成指定job的节点
	 * @param jobType
	 * @return
	 */
	public NodeResource popCapabilityNode(String jobType);
	
}
