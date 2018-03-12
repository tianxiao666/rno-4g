package com.iscreate.op.service.rno.job.request;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.Writable;
import com.iscreate.op.service.rno.job.common.NodeResource;

public class GetJobDetailResponse implements Writable {

	private JobProfile job;
	private NodeResource node;
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		int j=arg0.readInt();
		if(j>0){
			job=new JobProfile();
			job.readFields(arg0);
		}
		j=arg0.readInt();
		if(j>0){
			node=new NodeResource();
			node.readFields(arg0);
		}
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		if(job!=null){
			arg0.writeInt(1);
			job.write(arg0);
		}else{
			arg0.writeInt(0);
		}
		if(node!=null){
			arg0.writeInt(1);
			node.write(arg0);
		}else{
			arg0.writeInt(0);
		}

	}

	public JobProfile getJob() {
		return job;
	}

	public void setJob(JobProfile job) {
		this.job = job;
	}

	public NodeResource getNode() {
		return node;
	}

	public void setNode(NodeResource node) {
		this.node = node;
	}

	@Override
	public String toString() {
		return "GetJobDetailResponse [job=" + job + ", node=" + node + "]";
	}

}
