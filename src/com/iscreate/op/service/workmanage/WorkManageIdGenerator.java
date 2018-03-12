package com.iscreate.op.service.workmanage;

import com.iscreate.op.service.workmanage.util.OrderIdUtil;



public class WorkManageIdGenerator {

	private static WorkManageIdGenerator instance;

	private WorkManageIdGenerator() {
	}

	// 单例模式中获取唯一的MyApplication实例
	public static WorkManageIdGenerator getInstance() {
		if (null == instance) {
			instance = new WorkManageIdGenerator();
		}
		return instance;
	}

	/**
	 * 生成全局的工单Id
	 * */
	public String generateWOID(String bizFlag) {
		String woId=OrderIdUtil.generateWorkOrderId(bizFlag);
		return woId;
	}

	/**
	 * 生成全局的任务单Id
	 * */
	public String generateTOID(String workOrderId) {
		String toId=OrderIdUtil.generateTaskOrderId(workOrderId);
		return toId;
	}

}
