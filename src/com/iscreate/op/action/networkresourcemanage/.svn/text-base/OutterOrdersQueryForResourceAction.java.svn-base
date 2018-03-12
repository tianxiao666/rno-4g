package com.iscreate.op.action.networkresourcemanage;



import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.service.outlinking.OutLinkingService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.tools.GisDispatchJSONTools;
import com.iscreate.plat.tools.InterfaceUtil;

public class OutterOrdersQueryForResourceAction {
	private static final Log log = LogFactory.getLog(OutterOrdersQueryForResourceAction.class);
	private String status; //工单 任务单状态
	
	private String resourceId;//资源id
	
	private String resourceType;//资源类型
	
	private String  orderType;//获取工单（workorder）还是任务单（taskorder）
	
	private String searchCondition;//查询条件
	
	private WorkManageService workManageService;
	
	
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}
	
	/**
	 * 
	 * @description: 向外部项目获取资源关联的特定条件工单或任务单
	 * @author：     
	 * @return void     
	 * @date：Dec 3, 2012 11:25:06 AM
	 */
	public void getWorkOrdersByResourceIdByStatusAction(){
		log.info("进入getWorkOrdersByResourceIdByStatusAction，向外部项目获取资源关联的特定条件工单或任务单。");
		//获取环境代码
		String code = this.getNetworkResourceUrl();	
//		if(this.searchCondition!=null && !"".equals(this.searchCondition)){
//			try {
//				this.searchCondition=URLEncoder.encode(this.searchCondition, "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				log.error("进入getWorkOrdersByResourceIdByStatusAction，this.searchCondition="+this.searchCondition+"转码(UTF-8)错误");
//				e.printStackTrace();
//			}
//			
//		}
//		String interfaceURL = code + "/workmanage/getWorkOrdersByResourceIdByStatusAction?resourceId="+resourceId+"&resourceType="+resourceType+"&orderType="+orderType+"&searchCondition="+searchCondition+"&status="+status;
//		String res = "";
//		HttpServletResponse response = ServletActionContext.getResponse();
//		response.setCharacterEncoding("utf-8");
//		response.setContentType("text/html");
//		log.info("进入getWorkOrdersByResourceIdByStatusAction，路径interfaceURL="+interfaceURL);
		try {
//			res = InterfaceUtil.httpPostClientReuqest(interfaceURL); 
//			log.info("根据路径interfaceURL="+interfaceURL+"获取到信息res="+res);
			StringBuilder json = new StringBuilder();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/plain");
			List<Map> resultList = null;
			json.append("[");
			String bizTypeCode = "all";
			String taskOrderType="all";
			String sqlString ="";
			
			
			
			if(searchCondition!=null && !searchCondition.isEmpty()){
				try {
					this.searchCondition=new String(searchCondition.getBytes("ISO8859-1"),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			
			if ("workOrder".equals(orderType)) {
				if ("current".equals(status)) {
					sqlString += " and t2.\"status\" <> "+WorkManageConstant.WORKORDER_END;
				} else if ("history".equals(status)) {
					sqlString += " and t2.\"status\" = "+WorkManageConstant.WORKORDER_END;	
				}
				if (!"null".equals(searchCondition) && searchCondition != null && !"".equals(searchCondition)) {
					String conditionSqlString = " and (t2.\"woId\" like '%"+ searchCondition+ "%' or t2.\"woTitle\" like '%"+ searchCondition+ "%')";
					sqlString += conditionSqlString;
				}
				resultList=this.workManageService.getWorkOrderListByResourceTypeAndResourceId(resourceType, resourceId, bizTypeCode, taskOrderType, sqlString);
			} else {
				if ("current".equals(status)) {
					sqlString += " and t1.\"status\" <> "+WorkManageConstant.WORKORDER_END; 
				} else if ("history".equals(status)) {
					sqlString += " and t1.\"status\" = "+WorkManageConstant.WORKORDER_END;	
				}
				if (!"null".equals(searchCondition) && searchCondition != null && !"".equals(searchCondition)) {
					String conditionSqlString = " and (t1.\"toId\" like '%"+ searchCondition+ "%' or t1.\"toTitle\" like '%"+ searchCondition+ "%')";
					sqlString += conditionSqlString;
				}
				resultList=this.workManageService.getTaskOrderListByResourceTypeAndResourceId(resourceType, resourceId, bizTypeCode, taskOrderType, sqlString);
			}
			if (resultList != null && resultList.size() > 0) {
				if("workOrder".equals(orderType)){
					for (Map<String,Object> wo : resultList) {
						json.append(GisDispatchJSONTools.getWorkOrderJson(wo));
						json.append(",");
					}
				}else{
					for (Map<String,Object> wo : resultList) {
						json.append(GisDispatchJSONTools.getTaskOrderJson(wo));
						json.append(",");
					}
				}
				json.append("{'totalCount':"+resultList.size()+"}");
			}
			json.append("]");
			Gson  gson = new GsonBuilder().create();
			String res = json.toString();
			List<Map<String,String>> resultList2 = gson.fromJson(res,new TypeToken<List<Map<String,String>>>(){}.getType());
			if(resultList2 != null && !resultList2.isEmpty()){
				for(Map<String,String> m:resultList2){
					m.put("opUrlApp", code);
				}
				res = gson.toJson(resultList2);
			}
			log.info("退出getWorkOrdersByResourceIdByStatusAction，返回结果res="+res);
			response.getWriter().write(res);
		} catch (Exception e) {
//			log.error("退出getWorkOrdersByResourceIdByStatusAction，发生异常，路径interfaceURL="+interfaceURL);
			e.printStackTrace();
		} 
	}
	
	/**
	 * 根据环境代码获取对应的网络资源外部链接
	 * @return
	 */
	public String getNetworkResourceUrl(){
		String code = "";
		OutLinkingService outLinking = new OutLinkingService();
		code = outLinking.getUrlByProjService("ops");
		return code;
	}

	public WorkManageService getWorkManageService() {
		return workManageService;
	}

	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}
}
