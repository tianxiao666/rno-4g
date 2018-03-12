package com.iscreate.op.service.rno.job.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.iscreate.op.service.rno.job.Writable;
import com.iscreate.op.service.rno.job.server.NodeResourceState;

public class NodeResource implements Writable{

	private NodeResourceState state=NodeResourceState.Shutdown;
	private JobWorkerStatus workerData=new JobWorkerStatus();
	private long lastTouchTime=0;
	
	public NodeResourceState getState() {
		return state;
	}
	public void setState(NodeResourceState state) {
		this.state = state;
	}
	public JobWorkerStatus getWorkerData() {
		return workerData;
	}
	public void setWorkerData(JobWorkerStatus workerData) {
		this.workerData = workerData;
	}
	public long getLastTouchTime() {
		return lastTouchTime;
	}
	public void setLastTouchTime(long lastTouchTime) {
		this.lastTouchTime = lastTouchTime;
	}
	@Override
	public void readFields(DataInput arg0) throws IOException {
		state=NodeResourceState.getByCode(arg0.readUTF());
		workerData=new JobWorkerStatus();
		workerData.readFields(arg0);
		lastTouchTime=arg0.readLong();
	}
	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeUTF(state.getCode());
		workerData.write(arg0);
		arg0.writeLong(lastTouchTime);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((workerData == null) ? 0 : workerData.hashCode());
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
		NodeResource other = (NodeResource) obj;
		if (workerData == null) {
			if (other.workerData != null)
				return false;
		} else if (!workerData.equals(other.workerData))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "NodeResource [state=" + state + ", workerData=" + workerData
				+ ", lastTouchTime=" + lastTouchTime + "]";
	}
	
	
}
