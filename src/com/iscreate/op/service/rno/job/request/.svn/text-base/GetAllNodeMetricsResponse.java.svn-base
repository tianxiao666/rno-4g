package com.iscreate.op.service.rno.job.request;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.iscreate.op.service.rno.job.Writable;
import com.iscreate.op.service.rno.job.common.NodeResource;

public class GetAllNodeMetricsResponse implements Writable {

	private List<NodeResource> nodes; 
	@Override
	public void readFields(DataInput arg0) throws IOException {
		nodes=new ArrayList<NodeResource>();
		int cnt=arg0.readInt();
		if(cnt>0){
			NodeResource node;
			for(int i=0;i<cnt;i++){
				node=new NodeResource();
				node.readFields(arg0);
				nodes.add(node);
			}
		}
	}

	@Override
	public void write(DataOutput arg0) throws IOException {

		if(nodes==null || nodes.size()==0){
			arg0.writeInt(0);
			return;
		}
		arg0.writeInt(nodes.size());
		for(NodeResource n:nodes){
			n.write(arg0);
		}
	}

	public List<NodeResource> getNodes() {
		return nodes;
	}

	public void setNodes(List<NodeResource> nodes) {
		this.nodes = nodes;
	}

}
