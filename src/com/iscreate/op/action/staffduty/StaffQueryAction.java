package com.iscreate.op.action.staffduty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.service.staffduty.StaffService;

public class StaffQueryAction {
	
	private StaffService staffService;

	
	/**
	 * 通过帐号获取人员基本信息Action
	 * @author chen
	 */
	public void getStaffBaseInfoAction(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String accountId = request.getParameter("accountId");
		//获取人员基本信息
		Map staffMap = staffService.getStaffBaseInfoService(accountId);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(staffMap);		
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 通过帐号获取人员任务信息Action
	 * @author chen
	 */
	public void getStaffTaskInfoAction(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String accountId = request.getParameter("accountId");
		String taskStatus = request.getParameter("taskStatus");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String taskType = request.getParameter("taskType");
		String toTitle = request.getParameter("toTitle");
		Map conditions = new HashMap();
		conditions.put("taskStatus", taskStatus);
		conditions.put("beginTime", beginTime);
		conditions.put("endTime", endTime);
		conditions.put("taskType", taskType);
		conditions.put("toTitle", toTitle);
		
		//获取人员任务信息
		List<Map> taskList = staffService.getStaffTaskInfoService(accountId, conditions);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(taskList);		
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 按条件获取人员列表信息Action 
	 * @author chen
	 */
	public void getStaffListByConditionsAction(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String searchType = request.getParameter("searchType");
		String orgId = request.getParameter("orgId");
		String staffName = request.getParameter("staffName");
		String skillId = request.getParameter("skillId");
		String experienceAge = request.getParameter("experienceAge");
		String sex = request.getParameter("sex");
		String startDutyTime = request.getParameter("startDutyTime");
		String endDutyTime = request.getParameter("endDutyTime");
		//封装参数
		Map conditions = new HashMap();
		conditions.put("searchType", searchType);
		conditions.put("orgId", orgId);
		conditions.put("staffName", staffName);
		conditions.put("skillId", skillId);
		conditions.put("experienceAge", experienceAge);
		conditions.put("sex", sex);
		conditions.put("startDutyTime", startDutyTime);
		conditions.put("endDutyTime", endDutyTime);
		//按条件获取人员列表
		List<Map> staffList = staffService.getStaffListByConditionsService(conditions);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(staffList, new TypeToken<List<Map>>(){}.getType());		
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	public StaffService getStaffService() {
		return staffService;
	}

	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}
}
