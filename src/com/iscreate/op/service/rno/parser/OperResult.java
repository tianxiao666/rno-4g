package com.iscreate.op.service.rno.parser;

public class OperResult {

	private boolean flag;
	private String message;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "OperResult [flag=" + flag + ", message=" + message + "]";
	}

}
