package com.iscreate.op.service.rno.job.request;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.Writable;

public class SubmitJobRequest implements Writable {

	private JobProfile jobProfile;
	
	//用户信息？
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		jobProfile=new JobProfile();
		jobProfile.readFields(arg0);
	}

	public JobProfile getJobProfile() {
		return jobProfile;
	}

	public void setJobProfile(JobProfile jobProfile) {
		this.jobProfile = jobProfile;
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		jobProfile.write(arg0);
	}

	@Override
	public String toString() {
		return "SubmitJobRequest [jobProfile=" + jobProfile + "]";
	}

}
