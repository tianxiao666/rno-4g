package com.iscreate.op.service.rno.job.common;

public enum VmState {

	Stop("Stop"), 
	Running("Running"), 
	Shutdowning("Shutdowning");

	String code;

	private VmState(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static VmState getByCode(String code) {
		VmState[] all = VmState.values();
		for (VmState jnt : all) {
			if (jnt.code.equals(code)) {
				return jnt;
			}
		}
		return null;
	}
}
