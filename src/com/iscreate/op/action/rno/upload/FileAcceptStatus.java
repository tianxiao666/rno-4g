package com.iscreate.op.action.rno.upload;

public class FileAcceptStatus {

	private boolean flag;
	private String msg;
	private String token;
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public FileAcceptStatus(boolean flag, String msg) {
		super();
		this.flag = flag;
		this.msg = msg;
	}
	public FileAcceptStatus() {
		super();
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
}
