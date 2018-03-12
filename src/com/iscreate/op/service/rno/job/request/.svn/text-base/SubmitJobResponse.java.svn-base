package com.iscreate.op.service.rno.job.request;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.Writable;

public class SubmitJobResponse implements Writable {

	private boolean isAccepted = false;// 是否接纳
	private JobProfile jobProfile;
    private String msg="";
	
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isAccepted() {
		return isAccepted;
	}

	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public JobProfile getJobProfile() {
		return jobProfile;
	}

	public void setJobProfile(JobProfile jobProfile) {
		this.jobProfile = jobProfile;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		isAccepted=arg0.readBoolean();
		jobProfile=new JobProfile();
		jobProfile.readFields(arg0);
		msg=arg0.readUTF();

	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeBoolean(isAccepted);
		jobProfile.write(arg0);
		arg0.writeUTF(msg);
	}

	@Override
	public String toString() {
		return "SubmitJobResponse [isAccepted=" + isAccepted + ", jobProfile="
				+ jobProfile + ", msg=" + msg + "]";
	}

}
