package com.iscreate.op.service.rno.job.request;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableUtils;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.Writable;

public class KillJobRequest implements Writable {

	private JobProfile job;
	private String account;
	private String reason;
	
	public JobProfile getJob() {
		return job;
	}

	public void setJob(JobProfile job) {
		this.job = job;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		job=new JobProfile();
		job.readFields(arg0);
		account=WritableUtils.readString(arg0);
		reason=WritableUtils.readString(arg0);
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		job.write(arg0);
		WritableUtils.writeString(arg0, account);
		WritableUtils.writeString(arg0, reason);
	}

}
