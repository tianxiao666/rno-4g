package com.iscreate.op.service.rno.job.request;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.WritableUtils;

import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.Writable;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;

public class NodeHeartbeatRequest implements Writable {

	private HeartbeatEventType heartbeatType = HeartbeatEventType.Normal;
	private JobWorkerStatus workerData;

	private List<JobStatus> runningJobs;// 正在运行的job。包括正在killing的也算
	private List<JobStatus> finishedJobs;// 已经完成的job:killed,finished,failed
//	private List<JobStatus> killedJobs; // 已经kill完成的job

	public HeartbeatEventType getHeartbeatType() {
		return heartbeatType;
	}

	public void setHeartbeatType(HeartbeatEventType heartbeatType) {
		this.heartbeatType = heartbeatType;
	}

	public JobWorkerStatus getWorkerData() {
		return workerData;
	}

	public void setWorkerData(JobWorkerStatus workerData) {
		this.workerData = workerData;
	}

	public List<JobStatus> getRunningJobs() {
		return runningJobs;
	}

	public void setRunningJobs(List<JobStatus> runningJobs) {
		this.runningJobs = runningJobs;
	}

	public List<JobStatus> getFinishedJobs() {
		return finishedJobs;
	}

	public void setFinishedJobs(List<JobStatus> finishedJobs) {
		this.finishedJobs = finishedJobs;
	}


	@Override
	public void readFields(DataInput arg0) throws IOException {
		heartbeatType = HeartbeatEventType.getByCode(WritableUtils.readString(arg0));
		workerData = new JobWorkerStatus();
		workerData.readFields(arg0);

		// list
		JobStatus js = null;

		// running jobs
		int cnt = arg0.readInt();
		if (cnt > 0) {
			runningJobs = new ArrayList<JobStatus>();
			for (int i = 0; i < cnt; i++) {
				js = new JobStatus();
				js.readFields(arg0);
				runningJobs.add(js);
			}
		}

		// finished jobs
		cnt = arg0.readInt();
		if (cnt > 0) {
			finishedJobs = new ArrayList<JobStatus>();
			for (int i = 0; i < cnt; i++) {
				js = new JobStatus();
				js.readFields(arg0);
				finishedJobs.add(js);
			}
		}

	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		WritableUtils.writeString(arg0, heartbeatType.getCode());
		workerData.write(arg0);

		// running jobs
		if (runningJobs != null && runningJobs.size() > 0) {
			arg0.writeInt(runningJobs.size());
			for (JobStatus js : runningJobs) {
				js.write(arg0);
			}
		} else {
			arg0.writeInt(0);
		}

		// finished jobs
		if (finishedJobs != null && finishedJobs.size() > 0) {
			arg0.writeInt(finishedJobs.size());
			for (JobStatus js : finishedJobs) {
				js.write(arg0);
			}
		} else {
			arg0.writeInt(0);
		}


	}

	@Override
	public String toString() {
		return "NodeHeartbeatRequest [heartbeatType=" + heartbeatType
				+ ", workerData=" + workerData + ", runningJobs=" + runningJobs
				+ ", finishedJobs=" + finishedJobs ;
	}

}
