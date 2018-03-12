package com.iscreate.plat.gantt.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.plat.gantt.service.GanttService;

public class GanttAction {
	
	private GanttService ganttService;
	private String resourceId;
	private String resourceType;
	private String taskDate;
	private int isOverTime;
	
	
	
	public int getIsOverTime() {
		return isOverTime;
	}
	public void setIsOverTime(int isOverTime) {
		this.isOverTime = isOverTime;
	}
	/**
	 * 获取资源任务甘特信息
	 */
	public void getResourceTaskGanttAction(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			//获取数据
			List<Map> taskList = ganttService.getResourceTaskGanttService(resourceId,resourceType, taskDate ,isOverTime);
			// json转换
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(taskList, new TypeToken<List<Map>>(){}.getType());	
			//返回数据
			response.getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取资源某月任务甘特信息
	 */
	public void getResourceMonthTaskAction(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String endDate = request.getParameter("endDate");
			//获取数据
			Map resMap = ganttService.getResourceMonthTaskGanttService(resourceId, resourceType, taskDate, endDate ,isOverTime);
			// json转换
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(resMap, new TypeToken<Map>(){}.getType());	
			//返回数据
			response.getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	public GanttService getGanttService() {
		return ganttService;
	}
	public void setGanttService(GanttService ganttService) {
		this.ganttService = ganttService;
	}
	public String getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
}
