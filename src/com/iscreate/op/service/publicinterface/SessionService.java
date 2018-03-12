package com.iscreate.op.service.publicinterface;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.mortbay.log.Log;

import com.opensymphony.xwork2.ActionContext;

public class SessionService {

	private static SessionService instance;
	private static HashMap<String, Object> attachParams=new HashMap<String,Object>();
	private SessionService() {
	}

	// 单例模式中获取唯一的MyApplication实例
	public static SessionService getInstance() {
		if (null == instance) {
			instance = new SessionService();
		}
		return instance;
	}

	
	/**
	 * set session的value
	 * 
	 */
	public void setValueByKey(String key,String value) {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		HttpSession session = request.getSession();
		session.setAttribute(key, value);
	}
	
	/**
	 * 根据key获取session的value
	 * 
	 */
	public Object getValueByKey(String key) {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		HttpSession session = request.getSession();
		return session.getAttribute(key);
	}
	/**
	 * 删除session的key
	 * @title 
	 * @param key
	 * @author chao.xj
	 * @date 2014-3-10上午10:58:10
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void rmvValueByKey(String key) {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		HttpSession session = request.getSession();
		session.removeAttribute(key);
	}
	/**
	 * 
	 * @title set session的value
	 * @param key
	 * @param value
	 * @author chao.xj
	 * @date 2014-7-15上午11:20:35
	 * @company 怡创科技
	 * @version 1.2
	 */
	public void setValueByKey(String key,Object value) {
		ActionContext ctx = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) ctx
				.get(ServletActionContext.HTTP_REQUEST);
		HttpSession session = request.getSession();
		session.setAttribute(key, value);
	}
	public static void saveSession(String key,Object value) {
		
		attachParams.put(key,value);
	}
	public static HttpSession getSession() {
		return (HttpSession)attachParams.get("session");
	}
}
