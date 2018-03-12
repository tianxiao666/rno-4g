package com.iscreate.op.service.rno.job.event;

import org.apache.hadoop.yarn.event.AbstractEvent;

import com.iscreate.op.service.rno.job.common.JobProgressEventType;
import com.iscreate.op.service.rno.job.server.JobProgressData;

public class JobProgressEvent extends AbstractEvent<JobProgressEventType> {

	private JobProgressData progressData;

	public JobProgressEvent(JobProgressEventType type,
			JobProgressData progressData) {
		super(type);
		this.progressData = progressData;
	}

	public JobProgressData getProgressData() {
		return progressData;
	}

	public void setProgressData(JobProgressData progressData) {
		this.progressData = progressData;
	}

	
}
