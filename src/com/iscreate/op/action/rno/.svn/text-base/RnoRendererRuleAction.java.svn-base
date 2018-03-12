package com.iscreate.op.action.rno;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.pojo.rno.RnoTrafficRendererConfig;
import com.iscreate.op.service.rno.RnoRendererRuleService;

public class RnoRendererRuleAction {

	// -----------类静态-------------//
	private static Log log = LogFactory.getLog(RnoRendererRuleAction.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全

	// ----------注入----------------------//
	private RnoRendererRuleService rnoRendererRuleService;

	// ----------------页面变量---------------------------//
	private String ruleCode;// 渲染规则名称
	private long areaId;

	public RnoRendererRuleService getRnoRendererRuleService() {
		return rnoRendererRuleService;
	}

	public void setRnoRendererRuleService(
			RnoRendererRuleService rnoRendererRuleService) {
		this.rnoRendererRuleService = rnoRendererRuleService;
	}

	
	
	
	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public String getRuleCode() {
		return ruleCode;
	}

	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}

	private void writeToClient(String result) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("writeToClient 出错！");
		}
	}

	/**
	 * 根据名称获取相应的渲染规则
	 * 
	 * @author brightming 2013-10-10 下午3:24:22
	 */
	public void getRendererRuleForAjaxAction() {
		log.info("进入方法： getRendererRuleForAjaxAction。ruleCode="+ruleCode);
		List<RnoTrafficRendererConfig> settings=rnoRendererRuleService.getRendererRuleByRuleCode(ruleCode,areaId);
		String result=gson.toJson(settings);
		log.info("退出getRendererRuleForAjaxAction。result="+result);
		//System.out.println("result="+result);
		writeToClient(result);
	}
}
