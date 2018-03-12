package com.iscreate.op.action.home;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.pojo.home.HomeSettings;
import com.iscreate.op.service.home.HomeSettingsService;

public class HomeSettingsAction {
	private HomeSettingsService homeSettingsService;

	public HomeSettingsService getHomeSettingsService() {
		return homeSettingsService;
	}

	public void setHomeSettingsService(HomeSettingsService homeSettingsService) {
		this.homeSettingsService = homeSettingsService;
	}
	
	public long orgUserId;
	
	public long homeItemId;
	
	public long roleId;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	
	public long getOrgUserId() {
		return orgUserId;
	}

	public void setOrgUserId(long orgUserId) {
		this.orgUserId = orgUserId;
	}

	public long getHomeItemId() {
		return homeItemId;
	}

	public void setHomeItemId(long homeItemId) {
		this.homeItemId = homeItemId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * 关闭用户门户控件并且记录到个人设置中
	* @author ou.jh
	* @date Jun 6, 2013 3:57:18 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void closeUserHomeItemAction(){
		log.info("进入closeUserHomeItemAction方法");
		//1、成功 0、失败
		int flag = 0;
		//根据用户ID，角色ID，组件ID获取用户个人设置 
		List<HomeSettings> homeSettingsList = this.homeSettingsService.getHomeSettingsByOrgUserIdAndRoleIdAndhomeItemId(orgUserId, homeItemId, roleId);
		if(homeSettingsList != null && homeSettingsList.size() > 0){
			//存在对应的用户个人设置
			HomeSettings homeSettings = homeSettingsList.get(0);
			homeSettings.setStatus(0);
			homeSettings.setUpdatetime(new Date());
			try {
				this.homeSettingsService.updateHomeSettings(homeSettings);
				//更新成功
				flag = 1;
			} catch (Exception e) {
				//更新失败
				flag = 0;
			}
		}else{
			//不存在对应的用户个人设置
			HomeSettings homeSettings = new HomeSettings();
			homeSettings.setCreatetime(new Date());
			homeSettings.setHomeItemId(homeItemId);
			homeSettings.setOrgUserId(orgUserId);
			homeSettings.setRoleId(roleId);
			homeSettings.setStatus(0);
			Serializable saveHomeSettings = this.homeSettingsService.saveHomeSettings(homeSettings);
			if(saveHomeSettings != null){
				//保存成功
				flag = 1;
			}else{
				//保存失败
				flag = 0;
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(flag);
		log.info("执行closeUserHomeItemAction方法成功，实现了”关闭用户门户控件并且记录到个人设置中");
		log.info("退出closeUserHomeItemAction方法,返回void");
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
