package com.iscreate.op.service.rno.job.event;

import org.apache.hadoop.yarn.event.AbstractEvent;

import com.iscreate.op.service.rno.job.JobProfile;

public class JobEvent extends AbstractEvent<JobEventType> {

	private final JobProfile jobProfile;
	
	public JobEvent(JobEventType type,JobProfile jobProfile){
		super(type);
		this.jobProfile=jobProfile;
	}

	public JobProfile getJobProfile() {
		return jobProfile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		JobEvent other = (JobEvent) obj;
		if (jobProfile == null) {
			if (other.jobProfile != null)
				return false;
		} else if (!jobProfile.equals(other.jobProfile))
			return false;
		return true;
	}
	
}
