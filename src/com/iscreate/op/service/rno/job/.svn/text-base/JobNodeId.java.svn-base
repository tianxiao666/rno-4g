package com.iscreate.op.service.rno.job;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

import com.iscreate.op.service.rno.job.common.NodeType;

public class JobNodeId implements Writable {

	private String nodeRandomStr = UUID.randomUUID().toString()
			.replaceAll("-", "");
	private String name = "";
	private String nodeHost = "";
	private NodeType nodeType = NodeType.WorkerNode;

	public JobNodeId() {

	}

	public JobNodeId(JobNodeId another) {
		nodeRandomStr=another.getNodeRandomStr();
		name=another.getName();
		nodeHost=another.getNodeHost();
		nodeType=another.getNodeType();
	}

	public JobNodeId(String name, String nodeHost, NodeType nodeType) {
		this.name = name;
		this.nodeHost = nodeHost;
		this.nodeType = nodeType;
	}

	public String getNodeRandomStr() {
		return nodeRandomStr;
	}

	public void setNodeRandomStr(String nodeRandomStr) {
		this.nodeRandomStr = nodeRandomStr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNodeHost() {
		return nodeHost;
	}

	public void setNodeHost(String nodeHost) {
		this.nodeHost = nodeHost;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		nodeRandomStr = arg0.readUTF();
		name = arg0.readUTF();
		nodeHost = arg0.readUTF();
		nodeType = NodeType.getByCode(arg0.readUTF());
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeUTF(nodeRandomStr);
		arg0.writeUTF(name);
		arg0.writeUTF(nodeHost);
		arg0.writeUTF(nodeType.getCode());
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nodeRandomStr == null) ? 0 : nodeRandomStr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobNodeId other = (JobNodeId) obj;
		if (nodeRandomStr == null) {
			if (other.nodeRandomStr != null)
				return false;
		} else if (!nodeRandomStr.equals(other.nodeRandomStr))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JobNodeId [nodeRandomStr=" + nodeRandomStr + ", name=" + name
				+ ", nodeHost=" + nodeHost + ", nodeType=" + nodeType + "]";
	}

}
