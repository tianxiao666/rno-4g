package com.iscreate.op.service.rno.job.request;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.iscreate.op.service.rno.job.Writable;

public class GetAllJobsResponse implements Writable {

	private List<GetJobDetailResponse> jobs;
	@Override
	public void readFields(DataInput arg0) throws IOException {
		jobs=new ArrayList<GetJobDetailResponse>();
		int cnt=arg0.readInt();
		if(cnt>0){
			GetJobDetailResponse job;
			for(int i=0;i<cnt;i++){
				job=new GetJobDetailResponse();
				job.readFields(arg0);
				jobs.add(job);
			}
		}

	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		if(jobs==null||jobs.size()==0){
			arg0.writeInt(0);
			return;
		}
		arg0.writeInt(jobs.size());
		for(GetJobDetailResponse job:jobs){
			job.write(arg0);
		}

	}

	public List<GetJobDetailResponse> getJobs() {
		return jobs;
	}

	public void setJobs(List<GetJobDetailResponse> jobs) {
		this.jobs = jobs;
	}

	@Override
	public String toString() {
		return "GetAllJobsResponse [jobs=" + jobs + "]";
	}

}
