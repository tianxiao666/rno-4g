package com.iscreate.op.action.login;

import com.iscreate.op.service.publicinterface.SessionService;

public class UserAuthorityAction {

	/** 登录 */
	private String userId;

	private String password;

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String validateUser() {				
		SessionService.getInstance().setValueByKey("userId",this.getUserId());
		System.out.println("ssid"+SessionService.getInstance());
		System.out.println("uid"+SessionService.getInstance().getValueByKey("userId"));
		System.out.println("ssnnnid"+SessionService.getInstance());
		return "success";

	}

}
