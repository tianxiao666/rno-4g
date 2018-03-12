package com.iscreate.op.action.report;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.report.GisDispatchReportService;
import com.iscreate.sso.session.UserInfo;

public class GisDispatchReportAction {
	private GisDispatchReportService gisDispatchReportService;

	public GisDispatchReportService getGisDispatchReportService() {
		return gisDispatchReportService;
	}

	public void setGisDispatchReportService(
			GisDispatchReportService gisDispatchReportService) {
		this.gisDispatchReportService = gisDispatchReportService;
	}
	/**
	 * 统计人员，车辆，基站，站址
	* @date Nov 8, 201210:01:23 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getReportCharAction(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		String userId = "";
		try {
			request=ServletActionContext.getRequest();
			response=ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			userId=(String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			
			List<Map<String,String>> resourceMap=this.gisDispatchReportService.getResouceCountList(userId);
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(resourceMap);
//			System.out.println(result);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取人员任务统计监控
	* @date Nov 8, 201211:09:48 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getPeakStatisticsByStaffAction(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		String userId = "";
		try {
			request=ServletActionContext.getRequest();
			response=ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			userId=(String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			
			List<Map<String,Object>> resourceMap=this.gisDispatchReportService.getPeakStatisticsByStaff(userId);
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(resourceMap);
//			System.out.println(result);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取车辆任务统计监控
	* @date Nov 8, 201211:09:48 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getPeakStatisticsByCarAction(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		String userId = "";
		try {
			request=ServletActionContext.getRequest();
			response=ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			userId=(String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			
			List<Map<String,Object>> resourceMap=this.gisDispatchReportService.getPeakStatisticsByCar(userId);
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(resourceMap);
//			System.out.println(result);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 获取基站任务统计监控
	* @date Nov 8, 201211:09:48 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getPeakStatisticsByStationAction(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		String userId = "";
		try {
			request=ServletActionContext.getRequest();
			response=ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			userId=(String) request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			
			List<Map<String,Object>> resourceMap=this.gisDispatchReportService.getPeakStatisticsByBaseStation(userId);
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(resourceMap);
//			System.out.println(result);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
