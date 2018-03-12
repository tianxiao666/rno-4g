package com.iscreate.op.action.staffduty;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.service.staffduty.StaffPlanDutyService;


@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class StaffPlanDutyAction {

	/************ 依赖注入 ***********/
	private StaffPlanDutyService staffPlanDutyService;
	
	/***************** 属性 *******************/

	/****************** action *******************/
	
	
	/**
	 * 新排班管理信息
	 */
	public void staffNewDutyCalendarAction () {
		//获取参数
		HttpServletRequest request = ServletActionContext.getRequest();
		String date_string = request.getParameter("date");
		String bizId = request.getParameter("bizId");
		String name = request.getParameter("name");
		String staffId = request.getParameter("staffId");//判断是否查询登录用户排班即可
		Map<String, Map<String, List<Map<String, Object>>>> result_map = this.staffPlanDutyService.staffNewDutyCalendar(staffId,bizId, name, date_string);
		//相应到页面
		Gson gson = new Gson();
		String json = gson.toJson(result_map);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType ("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = ServletActionContext.getResponse().getWriter();
			out.print(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if ( out != null ) {
				out.flush();
				out.close();
			}
		}
	}
	
	/**
	 * 查询排班列表
	 */
	public void staffNewDutyListAction() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String date_string = request.getParameter("date");
		String bizId = request.getParameter("bizId");
		String name = request.getParameter("name");
		String isUser = request.getParameter("isUser");//判断是否查询登录用户排班即可
		Map<String, Map<String, Map<String, Map<String, Object>>>> result_map = this.staffPlanDutyService.staffNewDutyList(isUser,bizId, name, date_string);
		//相应到页面
		Gson gson = new Gson();
		String json = gson.toJson(result_map);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType ("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = ServletActionContext.getResponse().getWriter();
			out.print(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if ( out != null ) {
				out.flush();
				out.close();
			}
		}
	}

	
	public void loadStaffDutyFreq () {
		List<Map<String, String>> list = this.staffPlanDutyService.loadStaffDutyFreq();
		//相应到页面
		Gson gson = new Gson();
		String json = gson.toJson(list);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType ("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = ServletActionContext.getResponse().getWriter();
			out.print(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if ( out != null ) {
				out.flush();
				out.close();
			}
		}
	}
	
	/**
	 * 根据人员姓名,查询人员信息集合
	 * @param staffName - 人员姓名
	 * @param bizId - 组织id
	 */
	public void findStaffListByStaffName () {
		HttpServletRequest request = ServletActionContext.getRequest();
		String staffName = request.getParameter("staffName");
		String bizId = request.getParameter("bizId");
		List<Map<String, String>> list = this.staffPlanDutyService.findStaffListByStaffName(staffName,bizId);
		//相应到页面
		Gson gson = new Gson();
		String json = gson.toJson(list);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType ("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = ServletActionContext.getResponse().getWriter();
			out.print(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if ( out != null ) {
				out.flush();
				out.close();
			}
		}
	}
	
	/**
	 * 更新值班信息
	 * (请求参数) 	dutyDate 日期
	 * 				freId 班次
	 * 				delIds 删除人员Id数组
	 * 				addIds 增加人员Id数组
	 */
	public void staffDutyUpdateAction () {
		HttpServletRequest request = ServletActionContext.getRequest();
		String dutyDate = request.getParameter("dutyDate");
		String freId = request.getParameter("freId");
		String delIds = request.getParameter("delIds");
		String addIds = request.getParameter("addIds");
		Gson gson = new Gson();
		List<Map<String, String>> delIds_list = gson.fromJson(delIds, new TypeToken<List<Map<String, String>>>() {}.getType());
		List<Map<String, String>> addIds_list = gson.fromJson(addIds, new TypeToken<List<Map<String, String>>>() {}.getType());
		boolean flag = this.staffPlanDutyService.staffDutyUpdate(dutyDate, freId, delIds_list, addIds_list);
		
	}
	
	/**
	 * 根据日期删除排班信息
	 */
	public void deleteStaffDutyTemplateByDate () {
		HttpServletRequest request = ServletActionContext.getRequest();
		String dutyDate = request.getParameter("dutyDate");
		String freId = request.getParameter("freId");
		boolean flag = this.staffPlanDutyService.deleteStaffDutyTemplateByDate(dutyDate , freId );
	}
	
	
	/***************** getter setter *******************/
	public StaffPlanDutyService getStaffPlanDutyService() {
		return staffPlanDutyService;
	}

	public void setStaffPlanDutyService(StaffPlanDutyService staffPlanDutyService) {
		this.staffPlanDutyService = staffPlanDutyService;
	}
	
	
	
}
