package com.iscreate.op.service.rno.job.request;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.iscreate.op.service.rno.job.Writable;
import com.iscreate.op.service.rno.job.common.JobWorkerStatus;

public class RegisterWorkerNodeRequest implements Writable {

	private JobWorkerStatus workerNodeData;

	public JobWorkerStatus getWorkerNodeData() {
		return workerNodeData;
	}

	public void setWorkerNodeData(JobWorkerStatus workerNodeData) {
		this.workerNodeData = workerNodeData;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		workerNodeData=new JobWorkerStatus();
		workerNodeData.readFields(arg0);

	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		workerNodeData.write(arg0);
	}

}
