package com.iscreate.op.action.maintain;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.pojo.maintain.ResourceMaintenance;
import com.iscreate.op.service.maintain.NetworkResourceMaintenanceService;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.plat.networkresource.dictionary.SearchScope;

public class NetworkResourceMaintenanceAction {
	
	//资源ID
	public String resourceId;

	//资源类型
	public String resourceType;
	
	//开始时间
	public String sTime = "";
	
	//结束时间
	public String eTime = "";
	
	//资源维护记录ID
	public String mId;
	
	
	//业务模块名称
	public String bizModule;
	
	//业务信息唯一标识
	public String bizProcessCode;
	
	//业务信息ID
	public long bizProcessId;
	
	
	public Dictionary dictionary;
	
	private NetworkResourceMaintenanceService networkResourceMaintenanceService;
	
	private String searchCondition;
	
	private  static final  Log log = LogFactory.getLog(NetworkResourceMaintenanceAction.class);

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public NetworkResourceMaintenanceService getNetworkResourceMaintenanceService() {
		return networkResourceMaintenanceService;
	}

	public void setNetworkResourceMaintenanceService(
			NetworkResourceMaintenanceService networkResourceMaintenanceService) {
		this.networkResourceMaintenanceService = networkResourceMaintenanceService;
	}
	
	

	
	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String getBizModule() {
		return bizModule;
	}

	public void setBizModule(String bizModule) {
		this.bizModule = bizModule;
	}

	public String getBizProcessCode() {
		return bizProcessCode;
	}

	public void setBizProcessCode(String bizProcessCode) {
		this.bizProcessCode = bizProcessCode;
	}

	public long getBizProcessId() {
		return bizProcessId;
	}

	public void setBizProcessId(long bizProcessId) {
		this.bizProcessId = bizProcessId;
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

	public String getSTime() {
		return sTime;
	}

	public void setSTime(String time) {
		sTime = time;
	}

	public String getETime() {
		return eTime;
	}

	public void setETime(String time) {
		eTime = time;
	}
	
	public String getMId() {
		return mId;
	}

	public void setMId(String id) {
		mId = id;
	}

	
	
	//根据资源获取资源维护记录
	public void loadNetworkResourceMaintenanceAction(){
		log.info("进入===loadNetworkResourceMaintenanceAction方法");
		String res_type = this.resourceType;
		long res_id = Long.parseLong(this.resourceId);
		List<ResourceMaintenance> resourceMaintenanceRecordsByResTypeAndResId = null;
		if(this.sTime == null || this.sTime.equals("")){
			resourceMaintenanceRecordsByResTypeAndResId = networkResourceMaintenanceService.getResourceMaintenanceRecordsByResTypeAndResId(res_type, res_id);
		}else if(this.sTime != null && !this.sTime.equals("") && this.eTime != null && !this.eTime.equals("")){
			resourceMaintenanceRecordsByResTypeAndResId = networkResourceMaintenanceService.getResourceMaintenanceRecordsByResTypeAndResIdAndTime(res_type, res_id,sTime,eTime);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		if(resourceMaintenanceRecordsByResTypeAndResId != null && resourceMaintenanceRecordsByResTypeAndResId.size() > 0){
			log.info("开始循环resourceMaintenanceRecordsByResTypeAndResId");
			for(ResourceMaintenance re:resourceMaintenanceRecordsByResTypeAndResId){
				Map<String, Object> map = re.getApplicationEntity().toMap();
				String format = sdf.format(map.get("op_time"));
				map.put("op_time", format);
				resultList.add(map);
			}
			log.info("结束循环resourceMaintenanceRecordsByResTypeAndResId");
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(resultList);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换出错");
			e.printStackTrace();
		}
		log.info("退出===loadNetworkResourceMaintenanceAction方法");
	}

	
	//根据资源维护记录ID获取资源维护记录
	public void loadNetworkResourceMaintenanceByIdAction(){
		log.info("进入===loadNetworkResourceMaintenanceByIdAction方法");
		long parseLong = 0;
		if(this.mId != null && !this.mId.equals("")){
			parseLong = Long.parseLong(this.mId);
		}
		ResourceMaintenance resourceMaintenanceRecordsById = networkResourceMaintenanceService.getResourceMaintenanceRecordsById(parseLong);
		Map<String, Object> map = null;
		if(resourceMaintenanceRecordsById != null){
			ApplicationEntity applicationEntity = resourceMaintenanceRecordsById.getApplicationEntity();
			if(applicationEntity != null){
				map = resourceMaintenanceRecordsById.getApplicationEntity().toMap();
			}
		}
		if(map != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String format = sdf.format(map.get("op_time"));
			map.put("op_time", format);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(map);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换出错");
			e.printStackTrace();
		}
		log.info("退出===loadNetworkResourceMaintenanceByIdAction方法");
	}
	
	
	public void loadNetworkResourceMaintenanceBybizModuleAndbizProcessCodeAction(){
		log.info("进入===loadNetworkResourceMaintenanceBybizModuleAndbizProcessCodeAction方法");
		Map<String, List<Map<String, Object>>> remap = new HashMap<String, List<Map<String,Object>>>();
		if(this.bizModule != null && !this.bizModule.equals("") && this.bizProcessCode != null && !this.bizProcessCode.equals("")){
			try {
				this.bizModule = new String(this.bizModule.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<ResourceMaintenance> resourceMaintenanceRecordsByBizModuleAndBizProcessCode = networkResourceMaintenanceService.getResourceMaintenanceRecordsByBizModuleAndBizProcessCode(this.bizModule, this.bizProcessCode);
			if(resourceMaintenanceRecordsByBizModuleAndBizProcessCode != null && resourceMaintenanceRecordsByBizModuleAndBizProcessCode.size() > 0){
				log.info("开始循环resourceMaintenanceRecordsByBizModuleAndBizProcessCode");
				for(ResourceMaintenance re:resourceMaintenanceRecordsByBizModuleAndBizProcessCode){
					String key = re.getRes_id() +","+re.getRes_type();
					Map<String, Object> map = re.getApplicationEntity().toMap();
					List<BasicEntity> entry;
					try {
						entry = dictionary.getEntry(re.getRes_type() + ",networkResourceDefination", SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							map.put("divtionaryReType", entry.get(0).getValue("display"));
							
						}
					} catch (EntryOperationException e) {
						log.error("获取数据字典失败 key:"+re.getRes_type());
						map.put("divtionaryReType", re.getRes_type());
					}
					if(remap.get(key) != null){
						List<Map<String, Object>> list = remap.get(key);
						list.add(map);
					}else{
						List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
						list.add(map);
						remap.put(key, list);
					}
				}
				log.info("结束循环resourceMaintenanceRecordsByBizModuleAndBizProcessCode");
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(remap);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换出错");
			e.printStackTrace();
		}
		log.info("退出===loadNetworkResourceMaintenanceBybizModuleAndbizProcessCodeAction方法");
	}
	
	public void loadNetworkResourceMaintenanceBybizModuleAndbizProcessCodeAndRIdAndRtypeAction(){
		log.info("进入===loadNetworkResourceMaintenanceBybizModuleAndbizProcessCodeAndRIdAndRtypeAction方法");
		List<Map<String, Object>> relist = new ArrayList<Map<String, Object>>();
		if(this.bizModule != null && !this.bizModule.equals("") && this.bizProcessCode != null && !this.bizProcessCode.equals("") && this.resourceType != null && !this.resourceType.equals("") && this.resourceId != null && !this.resourceId.equals("")){
			try {
				this.bizModule = new String(this.bizModule.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<ResourceMaintenance> list = networkResourceMaintenanceService.getResourceMaintenanceRecordsByBizModuleAndBizRocessIdAndBizProcessCodeAndRIdAndRtype(bizModule, bizProcessCode, resourceId, resourceType);
			if(list != null && list.size() > 0){
				log.info("开始循环list");
				for(ResourceMaintenance re:list){
					Map<String, Object> map = re.getApplicationEntity().toMap();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String format = sdf.format(map.get("op_time"));
					map.put("op_time", format);
					List<BasicEntity> entry;
					try {
						entry = dictionary.getEntry(re.getRes_type() + ",networkResourceDefination", SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							map.put("divtionaryReType", entry.get(0).getValue("display"));
							
						}
					} catch (EntryOperationException e) {
						log.error("获取数据字典失败 key"+re.getRes_type());
						map.put("divtionaryReType", re.getRes_type());
					}
					relist.add(map);
				}
				log.info("结束循环list");
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(relist);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换出错");
			e.printStackTrace();
		}
		log.info("退出===loadNetworkResourceMaintenanceBybizModuleAndbizProcessCodeAndRIdAndRtypeAction方法");
	}
	
	public void loadNetworkResourceMaintenanceBybizModuleAndbizReIdAndReTypeAction(){
		log.info("进入===loadNetworkResourceMaintenanceBybizModuleAndbizReIdAndReTypeAction方法");
		List<Map<String, Object>> relist = new ArrayList<Map<String, Object>>();
		Long valueOf = Long.valueOf(resourceId);
			List<ResourceMaintenance> list = networkResourceMaintenanceService.getResourceMaintenanceRecordsByResTypeAndResId(resourceType,valueOf);
			if(list != null && list.size() > 0){
				log.info("开始循环list");
				for(ResourceMaintenance re:list){
					Map<String, Object> map = re.getApplicationEntity().toMap();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String format = sdf.format(map.get("op_time"));
					map.put("op_time", format);
					List<BasicEntity> entry;
					try {
						entry = dictionary.getEntry(re.getRes_type() + ",networkResourceDefination", SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							map.put("divtionaryReType", entry.get(0).getValue("display"));
							
						}
					} catch (EntryOperationException e) {
						log.error("获取数据字典失败 key"+re.getRes_type());
						map.put("divtionaryReType", re.getRes_type());
					}
					relist.add(map);
				}
				log.info("结束循环list");
			}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(relist);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换出错");
			e.printStackTrace();
		}
		log.info("退出===loadNetworkResourceMaintenanceBybizModuleAndbizReIdAndReTypeAction方法");
	}
	/**
	 * 资源浏览页面查询资源某条件维护记录
	 */
	public void searchNetworkResourceMaintenanceByCondition(){
		log.info("进入===searchNetworkResourceMaintenanceByCondition方法");
		String res_type = this.resourceType;
		long res_id = Long.parseLong(this.resourceId);
		List<ResourceMaintenance> resourceMaintenanceRecordsByResTypeAndResId = networkResourceMaintenanceService.getResourceMaintenanceRecordsByResTypeAndResId(res_type, res_id);	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		if(resourceMaintenanceRecordsByResTypeAndResId != null && resourceMaintenanceRecordsByResTypeAndResId.size() > 0){
			log.info("开始循环resourceMaintenanceRecordsByResTypeAndResId");
			for(ResourceMaintenance re:resourceMaintenanceRecordsByResTypeAndResId){
				if(re.getBiz_module()!=null){
					if(re.getBiz_module().contains(this.searchCondition)){
						Map<String, Object> map = re.getApplicationEntity().toMap();
						String format = sdf.format(map.get("op_time"));
						map.put("op_time", format);
						resultList.add(map);
						continue;
					}
				}
				if(re.getOp_scene()!=null){
					if(re.getOp_scene().contains(this.searchCondition)){
						Map<String, Object> map = re.getApplicationEntity().toMap();
						String format = sdf.format(map.get("op_time"));
						map.put("op_time", format);
						resultList.add(map);
						continue;
					}
				}
				if(re.getOp_cause()!=null){
					if(re.getOp_cause().contains(this.searchCondition)){
						Map<String, Object> map = re.getApplicationEntity().toMap();
						String format = sdf.format(map.get("op_time"));
						map.put("op_time", format);
						resultList.add(map);
						continue;
					}
				}
			}	
			log.info("结束循环resourceMaintenanceRecordsByResTypeAndResId");
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(resultList);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换出错");
			e.printStackTrace();
		}
		log.info("退出===searchNetworkResourceMaintenanceByCondition方法");
	}
	
}
