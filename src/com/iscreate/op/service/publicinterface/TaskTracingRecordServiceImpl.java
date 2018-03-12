package com.iscreate.op.service.publicinterface;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.dao.publicinterface.TaskTracingRecordDao;
import com.iscreate.op.pojo.publicinterface.Tasktracerecord;

public class TaskTracingRecordServiceImpl implements TaskTracingRecordService {
	private TaskTracingRecordDao taskTracingRecordDao;
	
	/**
	 * 保存服务跟踪记录
	 * */
	public String txSaveTaskTracingRecordService(Tasktracerecord tasktracerecord)
	{		
		if(tasktracerecord==null){
			
		}
		taskTracingRecordDao.saveTaskTracingRecordDao(tasktracerecord);
		return "success";
	}
	
	/**
	 * 获取服务跟踪记录列表
	 * @param key
	 * @param value
	 * @return
	 */
	public List<Tasktracerecord> getTaskTracingRecordByKeyService(String key,Object value){
		return taskTracingRecordDao.getTasktracerecordListDao(key, value);
	}
	
	/**
	 * 获取服务跟踪记录列表
	 * @param key
	 * @param value
	 * @return
	 */
	public List<Tasktracerecord> getTaskTracingRecordByKeyService(String key,Object value,String handleWay){
		return taskTracingRecordDao.getTasktracerecordListDao(key, value,handleWay);
	}

	/**
	 * 根据工单Id或任务单Id获取服务追踪记录
	 * @param woId
	 */
	public void getTaskTraceRecordService(String key,String woId){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<Tasktracerecord> tasktracerecordList = this.taskTracingRecordDao.getTasktracerecordListDao(key, woId);
//		List<Tasktracerecord> tasktracerecordList = new ArrayList<Tasktracerecord>();
//		for(int i=0;i<50;i++){
//			Tasktracerecord t = new Tasktracerecord();
//			int j = i+1;
//			t.setWoId("工单"+j);
//			t.setToId("任务单"+j);
//			t.setHandleWay("创建工单"+j);
//			t.setHandleResultDescription("内容"+j);
//			t.setHandler("处理人"+j);
//			t.setHandleTime(new Date());
//			tasktracerecordList.add(t);
//		}
		String result = gson.toJson(tasktracerecordList);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TaskTracingRecordDao getTaskTracingRecordDao() {
		return taskTracingRecordDao;
	}

	public void setTaskTracingRecordDao(TaskTracingRecordDao taskTracingRecordDao) {
		this.taskTracingRecordDao = taskTracingRecordDao;
	}
	
	
}
