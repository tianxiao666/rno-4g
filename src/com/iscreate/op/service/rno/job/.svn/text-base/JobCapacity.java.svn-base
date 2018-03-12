package com.iscreate.op.service.rno.job;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

public class JobCapacity implements Writable, Serializable {

	private static final long serialVersionUID = -4426974441969971466L;

	private String jobType="";
	private int maxSlots=0;
	private int curSlots = 0;//当前被占用的slot

	public JobCapacity(String jobType, int maxSlots, int curSlots) {
		super();
		this.jobType = jobType;
		this.maxSlots = maxSlots;
		this.curSlots = curSlots;
	}

	public JobCapacity() {
		super();
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public int getMaxSlots() {
		return maxSlots;
	}

	public void setMaxSlots(int maxSlots) {
		this.maxSlots = maxSlots;
	}

	public int getCurSlots() {
		return curSlots;
	}

	public void setCurSlots(int curSlots) {
		this.curSlots = curSlots;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public synchronized void incCurSlot(){
		curSlots++;
	}
	
	public synchronized void decCurSlot(){
		if(curSlots>0){
			curSlots--;
		}
	}

	//是否还有空闲任务槽
	public boolean hasFreeSlot(){
		return maxSlots>curSlots;
	}
	
	@Override
	public String toString() {
		return "JobCapacity [jobType=" + jobType + ", maxSlots=" + maxSlots
				+ ", curSlots=" + curSlots + "]";
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		this.jobType=arg0.readUTF();
		this.maxSlots=arg0.readInt();
		this.curSlots=arg0.readInt();
		
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeUTF(jobType);
		arg0.writeInt(maxSlots);
		arg0.writeInt(curSlots);
	}

	/**
	 * 判断所有的slot是否空闲
	 * @return
	 */
	public synchronized boolean idle() {
		return curSlots==0;
	}
	
	
}
