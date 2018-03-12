package com.iscreate.op.action.publicinterface;


import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.publicinterface.Tasktracerecord;
import com.iscreate.op.service.publicinterface.TaskTracingRecordService;

public class TaskTracingRecordAction {
	private String WOID;
	private String TOID;
	private String key;
	private String value;
	private List<Tasktracerecord> tasktracerecordList;
	
	private Tasktracerecord tasktracerecord;
	
	private TaskTracingRecordService taskTracingRecordService;
	
	/**
	 * 根据工单Id获取服务跟踪记录
	 */
	public String getWorkTraceRecordAction(){
		this.tasktracerecordList = this.taskTracingRecordService.getTaskTracingRecordByKeyService("woId", this.WOID);
		return "success";
	}
	
	/**
	 * 根据任务单Id获取服务跟踪记录
	 */
	public String getTaskTraceRecordAction(){
		this.tasktracerecordList = this.taskTracingRecordService.getTaskTracingRecordByKeyService("toId", this.TOID);
		return "success";
	}
	
	public String getWOID() {
		return WOID;
	}

	public void setWOID(String woid) {
		WOID = woid;
	}

	public String getTOID() {
		return TOID;
	}

	public void setTOID(String toid) {
		TOID = toid;
	}

	public Tasktracerecord getTasktracerecord() {
		return tasktracerecord;
	}

	public void setTasktracerecord(Tasktracerecord tasktracerecord) {
		this.tasktracerecord = tasktracerecord;
	}

	public TaskTracingRecordService getTaskTracingRecordService() {
		return taskTracingRecordService;
	}

	public void setTaskTracingRecordService(
			TaskTracingRecordService taskTracingRecordService) {
		this.taskTracingRecordService = taskTracingRecordService;
	}
	
	
	public List<Tasktracerecord> getTasktracerecordList() {
		return tasktracerecordList;
	}

	public void setTasktracerecordList(List<Tasktracerecord> tasktracerecordList) {
		this.tasktracerecordList = tasktracerecordList;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
