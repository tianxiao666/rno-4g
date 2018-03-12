package com.iscreate.op.service.rno.job.request;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableUtils;

import com.iscreate.op.service.rno.job.Writable;

public class RegisterWorkerNodeResponse implements Writable {

	private boolean flag = true;
	private String credential;
	private long registerTime=0;
	private long suggestHeartbeatTime = 30000;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public long getSuggestHeartbeatTime() {
		return suggestHeartbeatTime;
	}

	public void setSuggestHeartbeatTime(long suggestHeartbeatTime) {
		this.suggestHeartbeatTime = suggestHeartbeatTime;
	}

	public long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(long registerTime) {
		this.registerTime = registerTime;
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		flag=arg0.readBoolean();
		credential=WritableUtils.readString(arg0);
		suggestHeartbeatTime=arg0.readLong();
		registerTime=arg0.readLong();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeBoolean(flag);
		WritableUtils.writeString(arg0, credential);
		arg0.writeLong(suggestHeartbeatTime);
		arg0.writeLong(registerTime);
	}

}
