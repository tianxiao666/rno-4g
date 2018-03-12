package com.iscreate.op.action.publicinterface;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpRequest;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;


public class NetworkResourceInterfaceAction {
 
	private NetworkResourceInterfaceService networkResourceService;
	private long orgId;
	List<Map<String,String>> baseStationList;
	private String pinyin;
	private String fuzzy;
	private String resourceId;
	private String resourceType;
	private String WOID;
	private String baseStationName;
	private Log log = LogFactory.getLog(this.getClass());
	
	

	//通用基站页面Action
	/**
	 * 获取基站列表（抢修）
	 */
	public void getListBaseStationAction(){
		log.info("进入getListBaseStationAction方法");
		log.info("传入的参数orgId="+this.orgId);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		this.baseStationList = this.networkResourceService.getListBaseStationService(this.orgId, "GeneralBaseStation");
		String result = gson.toJson(baseStationList);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("执行getListBaseStationAction方法成功，实现了”根据组织Id获取该组织所管辖的基站列表“的功能");
		log.info("退出getListBaseStationAction方法,返回String为success");
	}
	
	/**
	 * 根据拼音获取基站列表（抢修）
	 * @return
	 */
	public void getListPinyinBaseStationAction(){
		log.info("进入getListPinyinBaseStationAction方法");
		log.info("传入的参数orgId="+this.orgId+",pinyin="+this.pinyin);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		this.baseStationList = this.networkResourceService.getListPinyinBaseStationService(this.orgId, "GeneralBaseStation", this.pinyin);
		String result = gson.toJson(baseStationList);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("执行getListPinyinBaseStationAction方法成功，实现了”根据组织Id和获取该组织所管辖的基站列表(拼音首字母查找)“的功能");
		log.info("退出getListPinyinBaseStationAction方法,返回String为success");
	}
	
	/**
	 * 根据模糊查找基站列表（抢修）
	 * @return
	 */
	public void getListFuzzyBaseStationAction(){
		log.info("进入getListFuzzyBaseStationAction方法");
		log.info("传入的参数orgId="+this.orgId+",fuzzy="+this.fuzzy);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			this.fuzzy = URLDecoder.decode(this.fuzzy,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("参数fuzzy转码失败");
			throw new UserDefinedException("参数fuzzy转码失败");
		}
		this.baseStationList = this.networkResourceService.getListFuzzyBaseStationService(this.orgId, "GeneralBaseStation", this.fuzzy);
		String result = gson.toJson(baseStationList);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("执行getListFuzzyBaseStationAction方法成功，实现了”根据组织Id获取该组织所管辖的基站列表(模糊查找)“的功能");
		log.info("退出getListFuzzyBaseStationAction方法,返回String为success");
	}
	
	//通用设施基础信息
	/**
	 * 获取基础设置信息（抢修）
	 */
	public void getBaseFacilityAction(){
		log.info("进入getListFuzzyBaseStationAction方法");
		log.info("传入的参数resourceId="+this.resourceId+",resourceType="+this.resourceType);
		this.networkResourceService.getBaseFacilityService(this.resourceId, this.resourceType);
		log.info("执行getListFuzzyBaseStationAction方法成功，实现了”根据资源Id和资源类型获取基础设置信息“的功能");
		log.info("退出getListFuzzyBaseStationAction方法,返回void");
	}
	
	/**
	 * 获取资源维护记录（抢修）
	 */
	public void getResourceMaintainRecordAction(){
		log.info("进入getListFuzzyBaseStationAction方法");
		log.info("传入的参数WOID="+this.WOID+",resourceId="+this.resourceId+",resourceType="+this.resourceType);
		this.networkResourceService.getResourceMaintainRecordAjaxService(this.WOID,this.resourceId,this.resourceType);
		log.info("执行getListFuzzyBaseStationAction方法成功，实现了”根据工单Id、资源Id和资源类型获取资源维护记录“的功能");
		log.info("退出getListFuzzyBaseStationAction方法,返回void");
	}
	
	/**
	 * 根据基站Id和基站类型获取站址
	 */
	public void getStationByIdAndTypeAction(){
		log.info("进入getStationByIdAndTypeAction方法");
		log.info("传入的参数resourceId="+this.resourceId+",resourceType="+this.resourceType);
		this.networkResourceService.getStationByBaseStationIdAndBaseStationTypeService(this.resourceId, this.resourceType);
		log.info("执行getStationByIdAndTypeAction方法成功，实现了”根据基站Id和基站类型获取站址“的功能");
		log.info("退出getStationByIdAndTypeAction方法,返回void");
	}
	
	/**
	 * 根据基站Id获取上一级区域
	 */
	public void getAllAreaForUpByIdAction(){
		log.info("进入getAllAreaForUpByIdAction方法");
		log.info("传入的参数resourceId="+this.resourceId+",resourceType="+this.resourceType);
		this.networkResourceService.getAllAreaByBaseStationIdService(this.resourceId, this.resourceType);
		log.info("执行getAllAreaForUpByIdAction方法成功，实现了”根据基站Id获取上一级区域“的功能");
		log.info("退出getAllAreaForUpByIdAction方法,返回void");
	}
	
	/**
	 * 根据工单ID获取基础设置信息（抢修）
	 */
	public void getBaseFacilityByWoIdAction(){
		log.info("进入getBaseFacilityByWoIdAction方法");
		log.info("传入的参数WOID="+this.WOID);
		this.networkResourceService.getBaseFacilityByWoIdService(this.WOID);
		log.info("执行getBaseFacilityByWoIdAction方法成功，实现了”根据基站Id获取上一级区域“的功能");
		log.info("退出getBaseFacilityByWoIdAction方法,返回void");
	}

	/**
	 * 获取网络资源的url链接
	 */
	public void getNetUrlAction(){
		log.info("进入getNetUrlAction方法");
		this.networkResourceService.getNetworkResourceUrlService();
		log.info("执行getNetUrlAction方法成功，实现了”获取网络资源的url链接“的功能");
		log.info("退出getNetUrlAction方法,返回void");
	}
	
	
	
	
	
	/**
	 * 递归获取指定entity下特定类型的子应用数据对象
	 */
	public void getChildTypeByEntityAction(){
		log.info("进入getChildTypeByEntityAction方法");
		HttpServletRequest request = ServletActionContext.getRequest();
		String currentEntityType = request.getParameter("currentEntityType");
		String parentEntityId = request.getParameter("parentEntityId");
		String parentEntityType = request.getParameter("parentEntityType");
		log.info("request里的currentEntityType="+currentEntityType+",parentEntityId="+parentEntityId+",parentEntityType="+parentEntityType);
		List<Map<String, String>> list = this.networkResourceService.getResourceService(parentEntityId, parentEntityType, currentEntityType, "CHILD");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("返回jsp页面时出错");
			throw new UserDefinedException("返回jsp页面时出错");
		}
		log.info("执行getChildTypeByEntityAction方法成功，实现了”递归获取指定entity下特定类型的子应用数据对象“的功能");
		log.info("退出getChildTypeByEntityAction方法,返回void");
	}
	
	/**
	 * ajax根据基站名获取基站信息
	 */
	public void getBaseStationByBaseStationNameAjaxAction(){
		log.info("进入getBaseStationByBaseStationNameAjaxAction方法");
		log.info("参数baseStationName="+this.baseStationName);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Map<String, String> map = this.networkResourceService.getResourceByReNameAndReTypeService(this.baseStationName,"GeneralBaseStation");
		String result = gson.toJson(map);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("返回jsp页面时出错");
			throw new UserDefinedException("返回jsp页面时出错");
		}
		log.info("执行getBaseStationByBaseStationNameAjaxAction方法成功，实现了”ajax根据基站名获取基站信息“的功能");
		log.info("退出getBaseStationByBaseStationNameAjaxAction方法,返回void");
	}
	
	public NetworkResourceInterfaceService getNetworkResourceService() {
		return networkResourceService;
	}

	public void setNetworkResourceService(
			NetworkResourceInterfaceService networkResourceService) {
		this.networkResourceService = networkResourceService;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public List<Map<String, String>> getBaseStationList() {
		return baseStationList;
	}

	public void setBaseStationList(List<Map<String, String>> baseStationList) {
		this.baseStationList = baseStationList;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getFuzzy() {
		return fuzzy;
	}

	public void setFuzzy(String fuzzy) {
		this.fuzzy = fuzzy;
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

	public String getWOID() {
		return WOID;
	}

	public void setWOID(String woid) {
		WOID = woid;
	}

	public String getBaseStationName() {
		return baseStationName;
	}

	public void setBaseStationName(String baseStationName) {
		this.baseStationName = baseStationName;
	}

	
}
