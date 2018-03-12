package com.iscreate.op.action.rno;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.pojo.rno.RnoTrafficRendererConfig;
import com.iscreate.op.service.rno.RnoTrafficRendererService;

public class RnoTrafficRendererAction {
	
	private RnoTrafficRendererService rnoTrafficRendererService;
	
	private static Gson gson = new GsonBuilder().create();// 线程安全
	
	private String trafficCode;
	
	private long areaId;

	private List<RnoTrafficRendererConfig> rnoTrafficRendererList ;
	
	private List<RnoTrafficRendererConfig> rendererList = new  ArrayList<RnoTrafficRendererConfig>();

	public String getTrafficCode() {
		return trafficCode;
	}

	public void setTrafficCode(String trafficCode) {
		this.trafficCode = trafficCode;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	public RnoTrafficRendererService getRnoTrafficRendererService() {
		return rnoTrafficRendererService;
	}

	public void setRnoTrafficRendererService(
			RnoTrafficRendererService rnoTrafficRendererService) {
		this.rnoTrafficRendererService = rnoTrafficRendererService;
	}
	
	/**
	 * 根据区域ID与话务指标类型获取话务性能渲染配置 
	* @author ou.jh
	* @date Oct 28, 2013 2:50:00 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getRnoTrafficRendererAction(){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		//根据话务指标类型获取对应类型数据 
		Map<String, Object> trafficMap = this.rnoTrafficRendererService.getRnoTraffic(trafficCode);
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		//根据区域ID与话务指标类型获取话务性能渲染配置 
		List<Map<String, Object>> trafficList = this.rnoTrafficRendererService.getRnoTrafficRendererByTrafficCodeAndAreaId(trafficCode, areaId);
		if(trafficList == null || trafficList.size() == 0){
			//根据区域ID与话务指标类型获取默认话务性能渲染配置 
			List<Map<String, Object>> list = this.rnoTrafficRendererService.getDefaultRnoTrafficRenderer(trafficCode);
			returnList = list;
		}else{
			returnList = trafficList;
		}
		returnMap.put("trafficMap", trafficMap);
		returnMap.put("returnList", returnList);
		String result = gson.toJson(returnMap);

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新或新增话务性能渲染配置
	* @author ou.jh
	* @date Oct 29, 2013 10:04:03 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void updateOrAddrnoTrafficRendererAction(){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		boolean flag = false;
		String message = "";
		//根据区域ID与话务指标类型获取话务性能渲染配置 
		List<Map<String, Object>> configRulesFromDb = this.rnoTrafficRendererService.getRnoTrafficRendererByTrafficCodeAndAreaId(trafficCode, areaId);
		try {
			//处理话务性能渲染配置数据
			List<RnoTrafficRendererConfig> fromClient=copyRnoTrafficRendererList();
			//对应区域话务指标类型获取话务性能渲染配置为空
			if(configRulesFromDb == null || configRulesFromDb.size() == 0){
				//处理话务性能渲染配置数据ID为空 
				restRnoTrafficRendererIDToNull(fromClient);
				//批量添加话务性能渲染配置 
				this.rnoTrafficRendererService.insertRnoTrafficRendererList(fromClient);
			}else{
				//批量更新话务性能渲染配置
				System.out.println(rnoTrafficRendererList.get(0).getMaxValue());
				this.rnoTrafficRendererService.updateRnoTrafficRendererList(fromClient);
			}
			flag = true;
			message = "修改成功";
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			message = "修改失败";
		}
		returnMap.put("flag", flag);
		returnMap.put("message", message);
		String result = gson.toJson(returnMap);

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");

		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理话务性能渲染配置数据
	* @author ou.jh
	* @date Oct 28, 2013 4:53:23 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<RnoTrafficRendererConfig> copyRnoTrafficRendererList(){
		 List<RnoTrafficRendererConfig> tmp=new ArrayList<RnoTrafficRendererConfig>();
		if(this.rnoTrafficRendererList != null && this.rnoTrafficRendererList.size() > 0){
			for(RnoTrafficRendererConfig r:rnoTrafficRendererList){
				RnoTrafficRendererConfig rendererConfig = r.copyRnoTrafficRendererConfig();
				tmp.add(rendererConfig);
			}
		}
		return tmp;
	}
	
	/**
	 * 处理话务性能渲染配置数据ID为空
	* @author ou.jh
	* @date Oct 28, 2013 4:53:23 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void restRnoTrafficRendererIDToNull(List<RnoTrafficRendererConfig> rList){
		if(rList != null && rList.size() > 0){
			for(RnoTrafficRendererConfig r:rList){
				r.setTrafficRenId(0l);
			}
		}
	}


	
	public List<RnoTrafficRendererConfig> getRnoTrafficRendererList() {
		return rnoTrafficRendererList;
	}

	public void setRnoTrafficRendererList(
			List<RnoTrafficRendererConfig> rnoTrafficRendererList) {
		this.rnoTrafficRendererList = rnoTrafficRendererList;
	}
}
