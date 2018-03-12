package com.iscreate.op.service.publicinterface;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.publicinterface.Tasktracerecord;

public interface TaskTracingRecordService {
	
	
	/**
	 * 保存服务跟踪记录
	 * */
	public String txSaveTaskTracingRecordService(Tasktracerecord tasktracerecord);
	
	/**
	 * 获取服务跟踪记录列表
	 * @param key
	 * @param value
	 * @return
	 */
	public List<Tasktracerecord> getTaskTracingRecordByKeyService(String key,Object value);
	
	/**
	 * 根据工单Id或任务单Id获取服务追踪记录
	 * @param woId
	 */
	public void getTaskTraceRecordService(String key,String woId);
	
	/**
	 * 获取服务跟踪记录列表
	 * @param key
	 * @param value
	 * @return
	 */
	public List<Tasktracerecord> getTaskTracingRecordByKeyService(String key,Object value,String handleWay);
	
}
