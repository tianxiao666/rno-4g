package com.iscreate.op.service.rno.job.request;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableUtils;

import com.iscreate.op.service.rno.job.JobStatus;
import com.iscreate.op.service.rno.job.Writable;
import com.iscreate.op.service.rno.job.common.KillResponseType;

public class KillJobResponse implements Writable {

	private KillResponseType responseType=KillResponseType.Rejected;
	private JobStatus jobStatus;
	

	public KillResponseType getResponseType() {
		return responseType;
	}

	public void setResponseType(KillResponseType responseType) {
		this.responseType = responseType;
	}

	public JobStatus getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(JobStatus jobStatus) {
		this.jobStatus = jobStatus;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		responseType=KillResponseType.getByCode(WritableUtils.readString(arg0));
		jobStatus=new JobStatus();
		jobStatus.readFields(arg0);
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		WritableUtils.writeString(arg0, responseType.getCode());
		jobStatus.write(arg0);

	}

	@Override
	public String toString() {
		return "KillJobResponse [responseType=" + responseType + ", jobStatus="
				+ jobStatus + "]";
	}

}
