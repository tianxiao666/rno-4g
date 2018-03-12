package com.iscreate.op.service.rno.job.request;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.iscreate.op.service.rno.job.JobAction;
import com.iscreate.op.service.rno.job.Writable;

public class NodeHeartbeatResponse implements Writable {

	private List<JobAction> jobActions;
	
	public List<JobAction> getJobActions() {
		return jobActions;
	}

	public void setJobActions(List<JobAction> jobActions) {
		this.jobActions = jobActions;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		int cnt=arg0.readInt();
		if(cnt>0){
			jobActions=new ArrayList<JobAction>();
			JobAction ja;
			for(int i=0;i<cnt;i++){
				ja=new JobAction();
				ja.readFields(arg0);
				jobActions.add(ja);
			}
		}
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		if(jobActions!=null && jobActions.size()>0){
			arg0.writeInt(jobActions.size());
			for(JobAction ja:jobActions){
				ja.write(arg0);
			}
		}else{
			arg0.writeInt(0);
		}
	}

	@Override
	public String toString() {
		return "NodeHeartbeatResponse [jobActions=" + jobActions + "]";
	}

}
