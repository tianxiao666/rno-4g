package com.iscreate.op.action.staffduty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.pojo.staffduty.Skill;
import com.iscreate.op.pojo.staffduty.Staffskill;
import com.iscreate.op.service.publicinterface.StationQueryTimerService;
import com.iscreate.op.service.staffduty.StaffSkillService;

public class StaffSkillAction {
	private StaffSkillService staffSkillService;

	public StaffSkillService getStaffSkillService() {
		return staffSkillService;
	}

	public void setStaffSkillService(StaffSkillService staffSkillService) {
		this.staffSkillService = staffSkillService;
	}
	
	/**
	 * 条件获取人员列表（包含技能信息）
	 */
	public void getStaffListByConditionsActionForStaffSkill(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String orgId = request.getParameter("orgId");
		String staffName = request.getParameter("staffName");
		String staffSex = request.getParameter("staffSex");
		String skillId = request.getParameter("skillId");
		String skillGrade = request.getParameter("skillGrade");
		String skillYear = request.getParameter("skillYear");
		String contactPhone = request.getParameter("contactPhone");
		
		Map conditions = new HashMap();
		conditions.put("orgId", orgId);
		conditions.put("staffName", staffName);
		conditions.put("staffSex", staffSex);
		conditions.put("skillId", skillId);
		conditions.put("skillGrade", skillGrade);
		conditions.put("skillYear", skillYear);
		conditions.put("contactPhone", contactPhone);
		
		//条件获取人员列表
		List<Map> staffList = staffSkillService.getStaffListByConditionsServiceForStaffSkill(conditions);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(staffList);		
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
	 * 根据人员帐号获取人员技能信息
	 */
	public void getStaffSkillInfoByStaffIdAction(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String account = request.getParameter("staffId");
		//获取人员技能信息
		List<Map> staffSkillList = staffSkillService.getStaffSkillListByAccount(account);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(staffSkillList);		
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
	 * 获取所有技能信息
	 */
	public void getAllSkillInfoAction(){
		//获取人员技能信息
		List<Skill> staffSkillList = staffSkillService.getAllSkillInfo();
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(staffSkillList);		
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
	 * 添加人员技能
	 */
	public void addStaffSkillAction(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String account = request.getParameter("staffId");
		String skillId = request.getParameter("skillId");
		String skillGrade = request.getParameter("skillGrade");
		String experienceYear = request.getParameter("experienceYear");
		//封装数据
		Staffskill sk = new Staffskill();
		sk.setStaffAccount(account);
		sk.setSkillId(Long.valueOf(skillId));
		sk.setSkillGrade(skillGrade);
		sk.setExperienceYear(Integer.valueOf(experienceYear));
		//添加人员技能
		boolean addRes = staffSkillService.addStaffSkill(sk);
		if(addRes){
			//获取人员技能
			List<Map> staffSkillList = staffSkillService.getStaffSkillListByAccount(account);
			// json转换
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(staffSkillList);	
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
	}
	
	/**
	 * 根据id删除人员技能
	 */
	public void delStaffSkillByIdAction(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String account = request.getParameter("staffId");
		String staffSkillId = request.getParameter("staffSkillId");
		//删除人员技能
		staffSkillService.deleteStaffSkill(Long.valueOf(staffSkillId));
		//获取人员技能
		List<Map> staffSkillList = staffSkillService.getStaffSkillListByAccount(account);
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(staffSkillList);	
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
}
