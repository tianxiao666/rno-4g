package com.iscreate.op.service.rno.job;


public enum ActionType {

	Launch("Launch"), Stop("Stop");

	String code;

	private ActionType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public static ActionType getByCode(String code) {
		ActionType[] all = ActionType.values();
		for (ActionType one : all) {
			if (one.code.equals(code)) {
				return one;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return code;
	}
}
