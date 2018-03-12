package com.iscreate.op.service.rno.job.common;

public enum JobState {

	Initiate("Initiate"),
	Queue("Queue"),
	Pending("Pending"),
	Launched("Launched"),
	Running("Running"),
	Killing("Stopping"),
	Killed("Stopped"),
	Failed("Fail"),
	Finished("Succeded");
	
	String code;
	private JobState(String code){
		this.code=code;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public static JobState getByCode(String code){
		JobState[] all=JobState.values();
		for(JobState one:all){
			if(one.code.equals(code)){
				return one;
			}
		}
		return null;
	}
	@Override
	public String toString() {
		return code;
	}
	public static boolean isInEndState(String code){
		if(Failed.code.equals(code) ||
		   Finished.code.equals(code)
		||Killed.code.equals(code)
		){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isInEndState(JobState st){
		if(Failed==st||Finished==st||Killed==st){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * 是否成功结束
	 * @param st
	 * @return
	 * @author brightming
	 * 2014-8-18 上午11:14:18
	 */
	public static boolean isEndWithSucceded(JobState st){
		if(Finished==st){
			return true;
		}
		return false;
	}
}
