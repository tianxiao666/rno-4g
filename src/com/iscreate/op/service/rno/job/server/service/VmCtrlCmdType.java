package com.iscreate.op.service.rno.job.server.service;


public enum VmCtrlCmdType {

	Create("Create"), 
	Destroy("Destroy"), 
	Start("Start");

	String code;

	private VmCtrlCmdType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static VmCtrlCmdType getByCode(String code) {
		VmCtrlCmdType[] all = VmCtrlCmdType.values();
		for (VmCtrlCmdType one : all) {
			if (one.code.equals(code)) {
				return one;
			}
		}
		return null;
	}
}
