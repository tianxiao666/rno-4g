package com.iscreate.op.service.rno.job;

public interface LifeCycle {

	public void start() throws LifeCycleException;
	
	public void stop()  throws LifeCycleException;
}
