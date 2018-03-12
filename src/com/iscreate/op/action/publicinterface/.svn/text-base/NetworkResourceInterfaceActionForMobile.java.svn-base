package com.iscreate.op.action.publicinterface;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.pojo.informationmanage.Area;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageUtil;


public class NetworkResourceInterfaceActionForMobile {
 
	private NetworkResourceInterfaceService networkResourceService;
	
	
	/**
	 * 获取所有省份
	 */
	public void getAllProviceForMobile () {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
				
		MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
				
		//mobilePackage为空，返回错误信息
		if(mobilePackage == null) {
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage.setResult("error");
			//返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(newMobilePackage);
			try {
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		MobileContentHelper mch = new MobileContentHelper();
		mch.setContent(mobilePackage.getContent());
		
		//获取参数
		Map<String, String> paramMap = mch.getGroupByKey ("request");
		List<Map<String, String>> list = networkResourceService.getTopAreaService();
		
		//返回信息
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("pageDataList", gson.toJson(list));
		mch.addGroup("pageData", resultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据地区id获取下级地区集合
	 * (请求参数) id - 上级区域id集合
	 * (响应) (List<Map<String,String>>) 区域集合
	 */
	public void getSubAreaForMobile () {
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
				
		MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
				
		//mobilePackage为空，返回错误信息
		if(mobilePackage == null) {
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage.setResult("error");
			//返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(newMobilePackage);
			try {
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		MobileContentHelper mch = new MobileContentHelper();
		mch.setContent(mobilePackage.getContent());
		
		//获取参数
		Map<String, String> paramMap = mch.getGroupByKey ("request");
		String id = paramMap.get("id");
		List<Map<String, String>> list = networkResourceService.getResourceService( id , "Sys_Area" , "Sys_Area" , "CHILD" );
		
		//返回信息
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("pageDataList", gson.toJson(list));
		mch.addGroup("pageData", resultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	
	
	

	/***************** getter setter *******************/
	public NetworkResourceInterfaceService getNetworkResourceService() {
		return networkResourceService;
	}

	public void setNetworkResourceService(
			NetworkResourceInterfaceService networkResourceService) {
		this.networkResourceService = networkResourceService;
	}
	
	
	
	
}
