package com.iscreate.op.service.rno.job;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableUtils;

public class JobAction implements Writable{

	private ActionType actionType;
	private JobProfile jobProfile;
	
	public JobAction(){
		
	}
	
	public JobAction(ActionType actionType,JobProfile jobProfile){
		this.actionType=actionType;
		this.jobProfile=jobProfile;
	}
	
	public ActionType getActionType() {
		return actionType;
	}
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}
	public JobProfile getJobProfile() {
		return jobProfile;
	}
	public void setJobProfile(JobProfile jobProfile) {
		this.jobProfile = jobProfile;
	}

	@Override
	public String toString() {
		return "JobAction [actionType=" + actionType + ", jobProfile="
				+ jobProfile + "]";
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.actionType=ActionType.getByCode(WritableUtils.readString(arg0));
		JobProfile jp=new JobProfile();
		jp.readFields(arg0);
		this.jobProfile=jp;
		
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		WritableUtils.writeString(arg0, actionType.getCode());
		jobProfile.write(arg0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actionType == null) ? 0 : actionType.hashCode());
		result = prime * result
				+ ((jobProfile == null) ? 0 : jobProfile.hashCode());
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
		JobAction other = (JobAction) obj;
		if (actionType != other.actionType)
			return false;
		if (jobProfile == null) {
			if (other.jobProfile != null)
				return false;
		} else if (!jobProfile.equals(other.jobProfile))
			return false;
		return true;
	}
	
	
	
}
