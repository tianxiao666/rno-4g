package com.iscreate.op.service.rno.job.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.WritableUtils;

import com.iscreate.op.service.rno.job.JobCapacity;
import com.iscreate.op.service.rno.job.JobNodeId;
import com.iscreate.op.service.rno.job.Readable;
import com.iscreate.op.service.rno.job.Writable;

public class JobWorkerStatus implements Writable, Readable {

	// private String name = "";
	// private String host = "";// 主机端口
	// private int port = 0;

	private JobNodeId jobNodeId;
	private Map<ListenPortType, Integer> listenPorts;
	private Map<String, JobCapacity> jobCapacities = new HashMap<String, JobCapacity>();

	public JobWorkerStatus(JobWorkerStatus workerNode) {
		jobNodeId = new JobNodeId(workerNode.getJobNodeId());
		listenPorts = new HashMap<ListenPortType, Integer>(workerNode.listenPorts);
		jobCapacities = new HashMap<String, JobCapacity>(workerNode.jobCapacities);

	}

	public JobWorkerStatus() {
	}

	public JobNodeId getJobNodeId() {
		return jobNodeId;
	}

	public void setJobNodeId(JobNodeId jobNodeId) {
		this.jobNodeId = jobNodeId;
	}

	public Map<ListenPortType, Integer> getListenPorts() {
		return listenPorts;
	}

	public void setListenPorts(Map<ListenPortType, Integer> listenPorts) {
		this.listenPorts = listenPorts;
	}

	public Map<String, JobCapacity> getJobCapacities() {
		return jobCapacities;
	}

	public void setJobCapacities(Map<String, JobCapacity> jobCapacities) {
		this.jobCapacities = jobCapacities;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		jobNodeId = new JobNodeId();
		jobNodeId.readFields(arg0);

		int cnt = arg0.readInt();
		if (cnt > 0) {
			listenPorts = new HashMap<ListenPortType, Integer>();
			for (int i = 0; i < cnt; i++) {
				listenPorts.put(ListenPortType.getByCode(WritableUtils.readString(arg0)),
						new Integer(arg0.readInt()));
			}
		}

		int size = arg0.readInt();
		jobCapacities = new HashMap<String, JobCapacity>();
		String key;
		JobCapacity jc;
		for (int i = 0; i < size; i++) {
			key=WritableUtils.readString(arg0);
			jc = new JobCapacity();
			jc.readFields(arg0);
			jobCapacities.put(key, jc);
		}

	}

	@Override
	public void write(DataOutput arg0) throws IOException {

		jobNodeId.write(arg0);

		if (listenPorts == null) {
			arg0.writeInt(0);
		} else {
			arg0.writeInt(listenPorts.size());
			for (ListenPortType k : listenPorts.keySet()) {
				WritableUtils.writeString(arg0, k.getCode());
				arg0.writeInt(listenPorts.get(k));
			}
		}

		arg0.writeInt(jobCapacities == null ? 0 : jobCapacities.size());
		if (jobCapacities != null) {
			for (String key : jobCapacities.keySet()) {
				WritableUtils.writeString(arg0, key);
				jobCapacities.get(key).write(arg0);
			}
		}
	}

	@Override
	public String toString() {
		return "JobWorkerStatus [jobNodeId=" + jobNodeId + ", listenPorts="
				+ listenPorts + ", jobCapacities=" + jobCapacities + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((jobNodeId == null) ? 0 : jobNodeId.hashCode());
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
		JobWorkerStatus other = (JobWorkerStatus) obj;
		if (jobNodeId == null) {
			if (other.jobNodeId != null)
				return false;
		} else if (!jobNodeId.equals(other.jobNodeId))
			return false;
		return true;
	}

	/**
	 * 是否能处理该指定任务
	 * @param jobType
	 * @return
	 */
	public boolean canHandleJob(String jobType) {
		JobCapacity jc = this.getJobCapacities().get(jobType);
		if (jc == null) {
			return false;
		}
		return jc.hasFreeSlot();
	}

	public synchronized int getAllJobsCnt(){
		int cnt=0;
		for(JobCapacity one:jobCapacities.values()){
			cnt+=one.getMaxSlots()-one.getCurSlots();
		}
		return cnt;
	}
	
	public synchronized void occupyJobSlot(String jobType){
		JobCapacity jc = this.getJobCapacities().get(jobType);
		if (jc == null) {
			return;
		}
		jc.incCurSlot();
	}
	
	public synchronized void releaseJobSlot(String jobType){
		JobCapacity jc = this.getJobCapacities().get(jobType);
		if (jc == null) {
			return;
		}
		jc.decCurSlot();
	}

	public synchronized boolean isIdle() {
		for(JobCapacity jc:jobCapacities.values()){
			if(!jc.idle()){
				return false;
			}
		}
		return true;
	}
}
