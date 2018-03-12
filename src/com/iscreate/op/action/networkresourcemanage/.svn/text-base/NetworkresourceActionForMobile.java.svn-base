package com.iscreate.op.action.networkresourcemanage;


import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.constant.OperationType;
import com.iscreate.op.pojo.maintain.ResourceMaintenance;
import com.iscreate.op.pojo.maintain.ServiceMaintenance;
import com.iscreate.op.service.maintain.NetworkResourceMaintenanceService;
import com.iscreate.op.service.networkresourcemanage.StaffOrganizationService;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageUtil;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
import com.iscreate.plat.networkresource.application.tool.XMLAEMLibrary;
import com.iscreate.plat.networkresource.common.action.ActionHelper;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.QuickSort;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.plat.networkresource.dictionary.SearchScope;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.networkresource.util.MobileUtil;

public class NetworkresourceActionForMobile {
	
	private StructureCommonService structureCommonService;
	
	private  static final  Log log = LogFactory.getLog(NetworkresourceActionForMobile.class);
	
    private StaffOrganizationService staffOrganizationService;//人员组织接口 yuan.yw add
	
	
	
	public StaffOrganizationService getStaffOrganizationService() {
		return staffOrganizationService;
	}

	public void setStaffOrganizationService(
			StaffOrganizationService staffOrganizationService) {
		this.staffOrganizationService = staffOrganizationService;
	}
	
	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}


	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}

	//数据字典
	private Dictionary dictionary;
	
	
	private QuickSort<Map<String,Object>> quickSort;//排序
	

	private XMLAEMLibrary moduleLibrary;
	
	private NetworkResourceMaintenanceService networkResourceMaintenanceService;
	

	public QuickSort<Map<String, Object>> getQuickSort() {
		return quickSort;
	}


	public void setQuickSort(QuickSort<Map<String, Object>> quickSort) {
		this.quickSort = quickSort;
	}


	public Dictionary getDictionary() {
		return dictionary;
	}


	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String loginNetWorkActionForMobile(){
		log.info("进入===loginNetWorkActionForMobile方法");
		String result = "success";
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId = (String)session.getAttribute("userId");
//		String userName = (String)session.getAttribute("userName");
		if(userId==null || "".equals(userId)){
			result = "failed";
		}
		log.info("退出===loginNetWorkActionForMobile方法 返回值为:"+result);
		return result;
	}
	
	/**
	 * 登陆页面的action
	 */
	public void loginPageActionForMobile(){
		log.info("进入===loginPageActionForMobile方法");
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		String userId = (String)session.getAttribute("userId");
		String userName = (String)session.getAttribute("userName");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
		if(mobilePackage == null) {
			mobilePackage = new MobilePackage();
		}
		MobileContentHelper mch = new MobileContentHelper();
		if(userId==null || "".equals(userId) || userName==null || "".equals(userName)){
			mobilePackage.setResult("error");
			mobilePackage.setContent(mch.mapToJson ());
			//返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(mobilePackage);
			try {
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e) {
				log.error("json转换出错");
				log.info("退出===loginPageActionForMobile方法");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			Map<String, String> loginContentMap = new HashMap<String, String>();
			loginContentMap.put("userId", userId);
			loginContentMap.put("username", userName);
			
			mch.addGroup("header", loginContentMap);
			mobilePackage.setContent(mch.mapToJson());
			//返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(mobilePackage);
			try {
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e) {
				log.error("json转换出错");
				log.info("退出===loginPageActionForMobile方法");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.info("退出===loginPageActionForMobile方法");
	}
	
	/**
	 * 
	 * @description: 检索查询资源
	 * @author：
	 * @return void
	 * @date：2012-3-5 上午10:19:45
	 */
	public void searchResourceByLableActionForMobile() {
		log.info("进入===searchResourceByLableActionForMobile方法");
		//System.out.println("omomom");
		HttpSession session = ServletActionContext.getRequest().getSession();
		
//		System.out.println("sessionId: " + session.getId());
//		System.out.println("userId: " + session.getAttribute("userId"));
//		System.out.println("userName: " + session.getAttribute("userName"));
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
		String content = mobilePackage.getContent();
		MobileContentHelper mch = new MobileContentHelper();
		mch.setContent(content);
		Map<String, String> formJsonMap = mch.getGroupByKey("request");
		//数据字典
		List<String> allEntityTypes = structureCommonService.getAllEntityTypes("networkresourcemanage");
		String[] entityTypes = null;
		if(allEntityTypes != null){
			entityTypes = allEntityTypes.toArray(new String[allEntityTypes.size()]);      
		}
		Map infoMapChinese =  new HashMap<String, Object>();
		for(String st:entityTypes){
			List<BasicEntity> entry;
			try {
				entry = dictionary.getEntry(st + ",networkResourceDefination", SearchScope.OBJECT, "");
				
				if(entry != null && !entry.isEmpty()) {
					infoMapChinese.put(st, entry.get(0).getValue("display"));
					
				}
			} catch (EntryOperationException e) {
				log.error("获取数据字典失败");
				//e.printStackTrace();
				infoMapChinese.put(st, st);
			}
		}
		
		
		
		String findType = formJsonMap.get("findType");
		String allType = formJsonMap.get("allType");
		String label = formJsonMap.get("label");
		String reType= formJsonMap.get("reType");
		//判断是否带经纬度
//		String[] aetNameOfAetg = this.structureCommonService.getAetNameOfAetg(reType, "networkresourcemanage");
//		String[] search_4_ResWithLatLon = this.structureCommonService.getAetNameOfAetg("Search_4_ResWithLatLon", "networkresourcemanage");
//		boolean isLatLon = false;
//		if(search_4_ResWithLatLon != null && search_4_ResWithLatLon.length > 0){
//			for(String st:search_4_ResWithLatLon){
//				if(st.equals(reType)){
//					isLatLon = true;
//				}else{
//					if(aetNameOfAetg != null && aetNameOfAetg.length > 0){
//						int count = 0;
//						for(String s:search_4_ResWithLatLon){
//							if(s.equals(st)){
//								count++;
//							}
//						}
//						if(count == aetNameOfAetg.length ){
//							isLatLon = true;
//						}
//					}
//				}
//			}
//		}
		List<Map> results = new ArrayList<Map>();
		Map map = new HashMap();
		if(findType != null){
			if(findType.equals("GPS")){
				int index = label.indexOf("物理位置#:");
				if(index >=0){
					int indexOf = label.indexOf("经度:")+3;
					int indexOf2 = label.indexOf(",纬度:");
					String longitudeString = label.substring(indexOf,indexOf2);
					double longitude = Double.valueOf(longitudeString);
					//System.out.println(longitude);
					int indexOf3 = label.indexOf("纬度:")+3;
					int indexOf4 = label.indexOf("):半径");
					String latitudeString = label.substring(indexOf3,indexOf4);
					double latitude = Double.valueOf(latitudeString);
					//System.out.println(latitude);
					int indexOf5 = label.indexOf(":半径(")+4;
					int indexOf6 = label.indexOf(")米");
					String distanceString = label.substring(indexOf5,indexOf6);
					double distance = Double.valueOf(distanceString);
					//System.out.println(distance);
					List<ApplicationEntity> entityListByAetgAndGPSDistance = structureCommonService.getEntityListByAetgAndGPSDistance(reType, distance, longitude, latitude, null, "networkresourcemanage");
					for(ApplicationEntity ae:entityListByAetgAndGPSDistance){
						Map<String, Object> aemap = ae.toMap();
						
						//System.out.println(aemap.get("_entityType")+"========="+ae.getValue("_entityType"));
						String chineseString = infoMapChinese.get(aemap.get("_entityType"))+"";
						aemap.put("chineseString", chineseString);
						results.add(aemap);
					}
				}

			}
			if(findType.equals("name")){
				int index = label.indexOf("设备名称#:");
				if(index >=0){
					int indexOf = label.indexOf("设备名称#:(")+7;
					int indexOf2 = label.indexOf(")");
					String labelName = label.substring(indexOf,indexOf2);
					if(label.indexOf(":半径(") >= 0){
						int indexOf5 = label.indexOf(":半径(")+4;
						int indexOf6 = label.indexOf(")米");
						String distanceString = label.substring(indexOf5,indexOf6);
						double distance = Double.valueOf(distanceString);
						int indexOf7 = label.indexOf("经度:")+3;
						int indexOf8 = label.indexOf(",纬度:");
						String longitudeString = label.substring(indexOf7,indexOf8);
						double longitude = Double.valueOf(longitudeString);
						//System.out.println(longitude);
						int indexOf3 = label.indexOf("纬度:")+3;
						int indexOf4 = label.lastIndexOf(")");
						String latitudeString = label.substring(indexOf3,indexOf4);
						double latitude = Double.valueOf(latitudeString);
						//System.out.println(distance);
						Map<String, Object> queryMap = new HashMap<String, Object>();
						queryMap.put("name", "%"+labelName+"%");
						List<ApplicationEntity> entityListByAetgAndGPSDistance = new ArrayList<ApplicationEntity>();
						entityListByAetgAndGPSDistance = structureCommonService.getEntityListByAetgAndGPSDistance(reType, distance, longitude, latitude, queryMap, "networkresourcemanage");	
						if(entityListByAetgAndGPSDistance != null){	
							for(ApplicationEntity ae:entityListByAetgAndGPSDistance){
								Map<String, Object> aemap = ae.toMap();
								String chineseString = infoMapChinese.get(aemap.get("_entityType"))+"";
								aemap.put("chineseString", chineseString);
								results.add(aemap);
							}
						}
					}else{
						//System.out.println(distance);
						Map<String, Object> queryMap = new HashMap<String, Object>();
						queryMap.put("name", "%"+labelName+"%");
						List<List<ApplicationEntity>> entityListByAetgAndGPSDistance = new ArrayList<List<ApplicationEntity>>();
						entityListByAetgAndGPSDistance = structureCommonService.getEntityListByEntityTypeRoAetg(reType, queryMap, "networkresourcemanage");	
						if(entityListByAetgAndGPSDistance != null){
							for(List<ApplicationEntity> lists:entityListByAetgAndGPSDistance){
								if(lists != null){
									for(ApplicationEntity ae:lists){
										Map<String, Object> aemap = ae.toMap();
										String chineseString = infoMapChinese.get(aemap.get("_entityType"))+"";
										aemap.put("chineseString", chineseString);
										results.add(aemap);
									}
								}
							}
						}
					}
					if(allType != null){
						if(allType.equals("allType")){
							Map<String, Object> queryMap = new HashMap<String, Object>();
							queryMap.put("name", "%"+labelName+"%");
							List<List<ApplicationEntity>> entityListByEntityTypeRoAetg = structureCommonService.getEntityListByEntityTypeRoAetg("Search_4_ResMore",queryMap, "networkresourcemanage");
							if(entityListByEntityTypeRoAetg != null){
								for(List<ApplicationEntity> lists:entityListByEntityTypeRoAetg){
									if(lists != null){
										for(ApplicationEntity ae:lists){
											Map<String, Object> aemap = ae.toMap();
											String chineseString = infoMapChinese.get(aemap.get("_entityType"))+"";
											aemap.put("chineseString", chineseString);
											results.add(aemap);
										}
									}
								}
							}
						}
					}
			}
		}
			
			if(findType.equals("label")){
				int indexOf = label.indexOf("设备编码#:(")+7;
				int indexOf2 = label.indexOf(")");
			String labelString  = label.substring(indexOf, indexOf2);
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("label", labelString);
			//String aetgName = "Search_4_ResWithLatLon";
			List<List<ApplicationEntity>> entityListByEntityTypeRoAetg = structureCommonService.getEntityListByEntityTypeRoAetgAccurate(reType,queryMap, "networkresourcemanage");
			for(List<ApplicationEntity> lists:entityListByEntityTypeRoAetg){
				for(ApplicationEntity ae:lists){
					Map<String, Object> aemap = ae.toMap();
					String chineseString = infoMapChinese.get(aemap.get("_entityType"))+"";
					aemap.put("chineseString", chineseString);
					results.add(aemap);
				}
			}
			}
	}
//		//System.out.println("1111111111111111111111111");
		//System.out.println(results.size());
		//List<Map<String,Object>> entityListRingByAetgAndGPSDistanceAndPaging = structureCommonService.getEntityListRingByAetgAndGPSDistanceAndPaging("Search_4_ResWithLatLon", 10,100000, 113.32, 23.05, null, 0, 10, "networkresourcemanage");
		//System.out.println(entityListRingByAetgAndGPSDistanceAndPaging.size());
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Map<String, String> allTypeMap = new HashMap<String, String>();
		//allTypeMap.put("allType", allType);
		Map<String, String> resourceMsgResultMap = new HashMap<String, String>();
		//mch1.addGroup("allType", allTypeMap);
		resourceMsgResultMap.put("resourceMsgDiv", gson.toJson(results));
		resourceMsgResultMap.put("allType", allType);
		mch.addGroup("resourceMsgDivArea", resourceMsgResultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		//返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("json转换失败");
			log.info("退出===searchResourceByLableActionForMobile方法");
			e.printStackTrace();
		}
		log.info("退出===searchResourceByLableActionForMobile方法");
	}
	
	
	//获取资源基本信息
	public void loadNetWorkResourceInfoActionForMobile(){
		log.info("进入===loadNetWorkResourceInfoActionForMobile方法");
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
		String content = mobilePackage.getContent();
		MobileContentHelper mch = new MobileContentHelper();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		mch.setContent(content);
		Map<String, String> formJsonMap = mch.getGroupByKey("request");
		String id = formJsonMap.get("id");
		String entityType = formJsonMap.get("entityType");
		ApplicationEntity buriedlineSectionEntity = structureCommonService.getSectionEntity(entityType, id);
		if(buriedlineSectionEntity == null){
			buriedlineSectionEntity = ActionHelper.getApplicationEntity(entityType);
		}
	//	this.infoMap = buriedlineSectionEntity.toMap();
		//ApplicationEntity转换成Map
		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap = ResourceCommon.applicationEntityConvertMap(buriedlineSectionEntity);
		infoMap = ResourceCommon.mapRecombinationToString(infoMap);
//		this.infoMap = ResourceCommon.mapRecombinationToString(infoMap);
		Map<String, Object> infoMapChineseMap =  new HashMap<String, Object>();
		Map<String,Object> orderIdMap = new HashMap<String,Object>();
		for(String key : infoMap.keySet()) {
			try {
				List<BasicEntity> entry = null;
				if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
					entry = dictionary.getEntry(key + "," + entityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					
				}
				if(entry != null && !entry.isEmpty()) {
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						infoMapChineseMap.put(key, entry.get(0).getValue("display"));
						orderIdMap.put(entry.get(0).getValue("orderID").toString(),key);
					}
				}
			} catch (EntryOperationException e) {
				log.error("获取数据字典出错");
				//e.printStackTrace();
				infoMapChineseMap.put(key, key);
				orderIdMap.put(key,key);
			}
		}
		Map<String,Object> sortedMap = quickSort.sortMap(infoMap,orderIdMap);
		if(sortedMap!=null){
			infoMap=sortedMap;
		}
		ApplicationEntity entity = structureCommonService.getSectionEntity(entityType,id);
		String[] selationsArray = structureCommonService.getStrutureSelationsArray(entity,AssociatedType.CHILD,"networkresourcemanage");
		String[] LinkArray = structureCommonService.getStrutureSelationsArray(entity,AssociatedType.LINK,"networkresourcemanage");
		//获取父子关系的数据
		Map<String, List<Map<String, Object>>> childAssociatedResourcMap = new HashMap<String, List<Map<String, Object>>>(); 
		Map<String, Object> infoChildMapChinese =  new HashMap<String, Object>();
		Map<String, Object> childAssociatedResourcCount =  new HashMap<String, Object>();
		log.info("开始循环selationsArray");
		for(String st:selationsArray){
			ApplicationEntity[] childRecursion = structureCommonService.getAppArrsByRecursion(
					entity, new String[]{st}, AssociatedType.CHILD, "networkresourcemanage");
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			if(childRecursion != null){
				log.info("开始循环childRecursion");
				for(ApplicationEntity ap:childRecursion){
					list.add(ap.toMap());
				}
				log.info("结束循环childRecursion");
				quickSort.sort(list,"name");
				childAssociatedResourcMap.put(st, list);
				childAssociatedResourcCount.put(st, list.size());
			}else{
				childAssociatedResourcMap.put(st, list);
				childAssociatedResourcCount.put(st, 0);
			}
			//获取父子关系的数据字典
			List<BasicEntity> entry;
			try {
				entry = dictionary.getEntry(st + ",networkResourceDefination", SearchScope.OBJECT, "");
				if(entry != null && !entry.isEmpty()) {
					infoChildMapChinese.put(st, entry.get(0).getValue("display"));
					
				}
			} catch (EntryOperationException e) {
				log.error("获取数据字典出错");
				//e.printStackTrace();
				infoChildMapChinese.put(st, st);
			}
		}
		log.info("结束循环selationsArray");
		//获取关联关系的数据
		Map<String, List<Map<String, Object>>> linkAssociatedResourcMap = new HashMap<String, List<Map<String, Object>>>(); 
		Map<String, Object> infoLinkMapChinese =  new HashMap<String, Object>();
		Map<String, Object> linkAssociatedResourcCount =  new HashMap<String, Object>();
		for(String st:LinkArray){
			ApplicationEntity[] linkRecursion = structureCommonService.getAppArrsByRecursion(
					entity, new String[]{st}, AssociatedType.LINK, "networkresourcemanage");
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			if(linkRecursion != null){
				log.info("开始循环linkRecursion");
				for(ApplicationEntity ap:linkRecursion){
					list.add(ap.toMap());
				}
				log.info("结束循环linkRecursion");
				quickSort.sort(list,"name");
				linkAssociatedResourcMap.put(st, list);
				linkAssociatedResourcCount.put(st, list.size());
				
			}else{
				linkAssociatedResourcMap.put(st, list);
				linkAssociatedResourcCount.put(st, 0);
			}
			//获取关联关系的数据字典
			List<BasicEntity> entry;
			try {
				entry = dictionary.getEntry(st + ",networkResourceDefination", SearchScope.OBJECT, "");
				if(entry != null && !entry.isEmpty()) {
					infoLinkMapChinese.put(st, entry.get(0).getValue("display"));
					
				}
			} catch (EntryOperationException e) {
				log.error("获取数据字典出错");
				//e.printStackTrace();
				infoLinkMapChinese.put(st, st);
			}
		}
		Map<String, String> resourceMsgResultMap = new HashMap<String, String>();
		resourceMsgResultMap.put("resourceMsgChinese", gson.toJson(infoMapChineseMap));
		resourceMsgResultMap.put("resourceMsg", gson.toJson(infoMap));
		resourceMsgResultMap.put("childAssociatedResourcMap", gson.toJson(childAssociatedResourcMap));
		resourceMsgResultMap.put("infoChildMapChinese", gson.toJson(infoChildMapChinese));
		resourceMsgResultMap.put("childAssociatedResourcCount", gson.toJson(childAssociatedResourcCount));
		resourceMsgResultMap.put("linkAssociatedResourcMap", gson.toJson(linkAssociatedResourcMap));
		resourceMsgResultMap.put("infoLinkMapChinese", gson.toJson(infoLinkMapChinese));
		resourceMsgResultMap.put("linkAssociatedResourcCount", gson.toJson(linkAssociatedResourcCount));
		mch.addGroup("resourceMsgs", resourceMsgResultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		//返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			log.error("json转换出错");
			log.info("退出===loadNetWorkResourceInfoActionForMobile方法");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("退出===loadNetWorkResourceInfoActionForMobile方法");
	}
	
	//获取资源基本信息(维护)
	public void loadResourcePreserveActionForMobile(){
		log.info("进入===loadResourcePreserveActionForMobile方法");
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
		String content = mobilePackage.getContent();
		MobileContentHelper mch = new MobileContentHelper();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		mch.setContent(content);
		Map<String, String> formJsonMap = mch.getGroupByKey("request");
		String id = formJsonMap.get("id");
		//String id = "";
		String entityType = formJsonMap.get("entityType");
		String addRoUpdate = formJsonMap.get("addRoUpdate");
		String pId = formJsonMap.get("pId");
		String pEntityType = formJsonMap.get("pEntityType");
//		String pId = "1";
//		String pEntityType = "Sys_Area";
		Map<String, Object> infoMap = new HashMap<String, Object>();
		ApplicationEntity entity = structureCommonService.getSectionEntity(entityType,id);
		if(entity == null){
			ApplicationModule module = ModuleProvider.getModule(entityType);
			ApplicationEntity applicationEntity = ActionHelper.getApplicationEntity(entityType);
			String _entityId = applicationEntity.getValue("_entityId")+"";
			String _entityType = applicationEntity.getValue("_entityType")+"";
			infoMap = module.toMap();
			infoMap.put("_entityType", _entityType);
			infoMap.put("_entityId", _entityId);
			infoMap.remove("_moduleName");
			//infoMap = ResourceCommon.applicationEntityConvertMapToNull(infoMap);
		}
		else{
//			this.infoMap = entity.toMap();
//			this.infoMap = ResourceCommon.mapRecombinationToString(infoMap);
			infoMap = ResourceCommon.applicationEntityConvertMap(entity);
		}
		ApplicationModule module = ModuleProvider.getModule(entityType);
		Map<String, Object> infoMapChineseMap =  new HashMap<String, Object>();
		Map<String, Object> nullableMap = new HashMap<String, Object>();
		Map<String, Object> attrTypeMap = new HashMap<String, Object>();
		Map<String,Object> orderIdMap = new HashMap<String,Object>();
		Map<String,Object> currentEntityChineseMap = new HashMap<String,Object>();
		Map<String, List<String>> dropdownListMap = new HashMap<String, List<String>>();
		log.info("开始循环module");
		for(String key: module.keyset()){
			//判断是否需要增加非空判断
			Object nullVal = module.getAttribute(key).getValue("nullable");
			if(nullVal != null && !"".equals(nullVal)) {
				nullableMap.put(key, nullVal.toString());
			}
			//用map保存属性的类型,例如java.lang.String
			Object attrTypeVal = module.getAttribute(key).getValue("type");
			if(attrTypeVal != null && !"".equals(attrTypeVal)) {
				attrTypeMap.put(key, attrTypeVal.toString());
			}
			
			try {
				List<BasicEntity> entry = null;
				if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
					//数据字典，中英文转换
					entry = dictionary.getEntry(key + "," + entityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					
				}
				if(entry != null && !entry.isEmpty()) {
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						currentEntityChineseMap.put(key, entry.get(0).getValue("display"));
						orderIdMap.put(entry.get(0).getValue("orderID").toString(),key);
					}
				}
				
				if(!"_entityId".equals(key)&&!"_entityType".equals(key)) {
					//根据数据字典中，属性为entity类型或者general类型，获取属性对应的下拉框的值，提供前台进行下拉框选择操作
					//拿出来的值，例如:其他-其他,置换-置换,局用-局用
					if("entity".equals(entry.get(0).getValue("dictionaryType"))) {
						//entity类型下拉框数据组装
						List<BasicEntity> dropDownEntry = dictionary.getEntry(key + "," + entityType + ",networkResourceValueEmun", SearchScope.OBJECT, "");
						if(dropDownEntry != null && !dropDownEntry.isEmpty()) {
							String dropdownMixVal = dropDownEntry.get(0).getValue("mixValue");
							//先用逗号进行分隔
							String[] firstSplitArrs = dropdownMixVal.split(",");
							if(firstSplitArrs != null && firstSplitArrs.length > 0) {
								List<String> attrDropdownList = new ArrayList<String>();
								for(String secondVal : firstSplitArrs) {
									//再通过"-"获取key，value，获取value值
									String[] secondSplitArrs = secondVal.split(":");
									if(secondSplitArrs != null && secondSplitArrs.length == 2) {
										attrDropdownList.add(secondSplitArrs[1]);
										
									}
								}
								dropdownListMap.put(key, attrDropdownList);
							}
						}
						
					} else if("general".equals(entry.get(0).getValue("dictionaryType"))) {
						String generalEntryName = entry.get(0).getValue("generalEntryName");
						//general类型下拉框数据组装
						List<BasicEntity> dropDownEntry = dictionary.getEntry(generalEntryName + ",networkResourceValueEmun", SearchScope.OBJECT, "");
						if(dropDownEntry != null && !dropDownEntry.isEmpty()) {
							String dropdownMixVal = dropDownEntry.get(0).getValue("mixValue");
							//先用逗号进行分隔
							String[] firstSplitArrs = dropdownMixVal.split(",");
							if(firstSplitArrs != null && firstSplitArrs.length > 0) {
								List<String> attrDropdownList = new ArrayList<String>();
								for(String secondVal : firstSplitArrs) {
									//再通过"-"获取key，value，获取value值
									String[] secondSplitArrs = secondVal.split(":");
									if(secondSplitArrs != null && secondSplitArrs.length == 2) {
										attrDropdownList.add(secondSplitArrs[1]);
										
									}
								}
								dropdownListMap.put(key, attrDropdownList);
							}
						}
					}
				}
			} catch (EntryOperationException e) {
				log.error("获取数据字典出错");
				//e.printStackTrace();
			}
			
		}
		log.info("结束循环module");
		log.info("开始循环infoMap");
		for(String key : infoMap.keySet()) {
			try {
				List<BasicEntity> entry = null;
				if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
					entry = dictionary.getEntry(key + "," + entityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					
				}
				if(entry != null && !entry.isEmpty()) {
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						infoMapChineseMap.put(key, entry.get(0).getValue("display"));
					}
				}
			} catch (EntryOperationException e) {
				log.error("获取数据字典出错");
				//e.printStackTrace();
				infoMapChineseMap.put(key, key);
			}
		}
		log.info("结束循环infoMap");
		Map<String,Object> sortedMap = quickSort.sortMap(infoMap,orderIdMap);
		if(sortedMap!=null){
			infoMap=sortedMap;
		}
		Map<String, String> resourceMsgResultMap = new HashMap<String, String>();
		resourceMsgResultMap.put("resourceMsgChinese", gson.toJson(infoMapChineseMap));
		resourceMsgResultMap.put("resourceMsg", gson.toJson(infoMap));
		resourceMsgResultMap.put("nullableMap", gson.toJson(nullableMap));
		resourceMsgResultMap.put("attrTypeMap", gson.toJson(attrTypeMap));
		resourceMsgResultMap.put("orderIdMap", gson.toJson(orderIdMap));
		resourceMsgResultMap.put("currentEntityChineseMap", gson.toJson(currentEntityChineseMap));
		resourceMsgResultMap.put("dropdownListMap", gson.toJson(dropdownListMap));
		resourceMsgResultMap.put("addRoUpdate", addRoUpdate);
		resourceMsgResultMap.put("pId", pId);
		resourceMsgResultMap.put("pEntityType", pEntityType);
		mch.addGroup("resourceMsgs", resourceMsgResultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		//返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("json转换失败");
			log.info("退出===loadResourcePreserveActionForMobile方法");
			e.printStackTrace();
		}
		log.info("退出===loadResourcePreserveActionForMobile方法");
	}
	
	//保存或者更新一个资源
	public void addResourcePreserveActionForMobile(){
		log.info("进入===addResourcePreserveActionForMobile方法");
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
		String content = mobilePackage.getContent();
		MobileContentHelper mch = new MobileContentHelper();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		mch.setContent(content);
		Map<String, String> formJsonMap = mch.getGroupByKey("request");
		String id = formJsonMap.get("id");
		String pId = formJsonMap.get("pId");
		String pEntityType = formJsonMap.get("pEntityType");
		String entityType = formJsonMap.get("_entityType");
		String addRoUpdate = formJsonMap.get("addRoUpdate");
		String bizModule = formJsonMap.get("bizModule");
		String bizProcessCode = formJsonMap.get("bizProcessCode");
		String bizProcessId = formJsonMap.get("bizProcessId");
		ApplicationEntity applicationEntity = MobileUtil.getAeFromMap(entityType, formJsonMap);
		long saveId = 0;
		int section = 0;
		String message = null;
		if("add".equals(addRoUpdate)){
			if(pId != null && pEntityType != null){
				String lowerinfoName = entityType.toLowerCase();
				//System.out.println(lowerinfoName);
				saveId = this.structureCommonService.getEntityPrimaryKey(lowerinfoName);//Unique.nextLong(lowerinfoName+"_id");
				//id = saveId + "";
				if(lowerinfoName.indexOf("Area")>=0){
					applicationEntity.setValue("area_id", saveId);
				}else{
					applicationEntity.setValue("id", saveId);
				}
				
				//Unique.nextLong(lowerinfoName+"_id");
				section = this.structureCommonService.saveInfoEntity(applicationEntity, "networkresourcemanage");
				ApplicationEntity areaEntity = structureCommonService.getSectionEntity(pEntityType,pId);
				int createAssociatedRelation = this.structureCommonService.createAssociatedRelation(areaEntity,applicationEntity,AssociatedType.CLAN, "networkresourcemanage");
				if(createAssociatedRelation>0){
					String resName = "";
					if(applicationEntity.getValue("name") != null){
						resName = applicationEntity.getValue("name");
					}else{
						resName = applicationEntity.getValue("label");
					}
					ResourceMaintenance maintenance = new ResourceMaintenance();
					maintenance.setBiz_module(OperationType.NETWORK);
					maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
					maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
					maintenance.setOp_category(OperationType.RESOURCEINSERT);
					maintenance.setRes_id((Long)applicationEntity.getValue("id"));
					maintenance.setRes_type(applicationEntity.getValue("_entityType").toString());
					maintenance.setRes_keyinfo(resName);
					int insertResourceMaintenanceRecordsBySystemForces = networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
					if(bizModule != null && bizModule.equals("")){
						ServiceMaintenance serviceMaintenance = new ServiceMaintenance();
						serviceMaintenance.setBiz_processcode(bizProcessCode);
						if(bizProcessId != null && !bizProcessId.equals("")){
							long parseLong = Long.parseLong(bizProcessId);
							serviceMaintenance.setBiz_processId(parseLong);
						}
						if(bizModule != null && bizModule.equals("")){
							serviceMaintenance.setBiz_module(bizModule);
						}
						serviceMaintenance.setMaintenance_id(insertResourceMaintenanceRecordsBySystemForces);
						networkResourceMaintenanceService.insertServiceMaintenance(serviceMaintenance);
					}
				}
			}
			
		}
		else{
			ApplicationEntity upddateAe = structureCommonService.getSectionEntity(entityType, id);
			section = this.structureCommonService.updateInfoEntity(applicationEntity);
			//updateApp
			String updateValue = getUpdateValue(applicationEntity,upddateAe);
			String resName = "";
			if(upddateAe.getValue("name") != null){
				resName = upddateAe.getValue("name");
			}else{
				resName = upddateAe.getValue("label");
			}
			ResourceMaintenance maintenance = new ResourceMaintenance();
			maintenance.setBiz_module(OperationType.NETWORK);
			maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
			maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
			maintenance.setOp_category(OperationType.RESOURCEUPDATE);
			maintenance.setRes_id((Long)upddateAe.getValue("id"));
			maintenance.setRes_type(upddateAe.getValue("_entityType").toString());
			maintenance.setRes_keyinfo(resName);
			maintenance.setContent(updateValue);
			int insertResourceMaintenanceRecordsBySystemForces = networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
			if(bizModule != null && bizModule.equals("")){
				ServiceMaintenance serviceMaintenance = new ServiceMaintenance();
				serviceMaintenance.setBiz_processcode(bizProcessCode);
				if(bizProcessId != null && !bizProcessId.equals("")){
					long parseLong = Long.parseLong(bizProcessId);
					serviceMaintenance.setBiz_processId(parseLong);
				}
				if(bizModule != null && bizModule.equals("")){
					serviceMaintenance.setBiz_module(bizModule);
				}
				serviceMaintenance.setMaintenance_id(insertResourceMaintenanceRecordsBySystemForces);
				networkResourceMaintenanceService.insertServiceMaintenance(serviceMaintenance);
			}
		}
		if(section > 0){
			message = "保存成功";
		}else{
			message = "保存失败";
		}
		Map<String, String> resourceMsgResultMap = new HashMap<String, String>();
		resourceMsgResultMap.put("id", id);
		resourceMsgResultMap.put("message", message);
		resourceMsgResultMap.put("entityType", entityType);
		mch.addGroup("resourceMsgs", resourceMsgResultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		//返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("退出===addResourcePreserveActionForMobile方法");
	}
	
	/**
	 * 根据资源类型资源
	* @date Nov 28, 201210:51:07 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void searchResourceByResourceTypeActionForMobile(){
		//System.out.println("omomom");
		HttpSession session = ServletActionContext.getRequest().getSession();
		
//		System.out.println("sessionId: " + session.getId());
//		System.out.println("userId: " + session.getAttribute("userId"));
//		System.out.println("userName: " + session.getAttribute("userName"));
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
		String content = mobilePackage.getContent();
		MobileContentHelper mch = new MobileContentHelper();
		mch.setContent(content);
		Map<String, String> formJsonMap = mch.getGroupByKey("request");
		//数据字典
		List<String> allEntityTypes = structureCommonService.getAllEntityTypes("networkresourcemanage");
		String[] entityTypes = null;
		if(allEntityTypes != null){
			entityTypes = allEntityTypes.toArray(new String[allEntityTypes.size()]);      
		}
		Map infoMapChinese =  new HashMap<String, Object>();
		for(String st:entityTypes){
			List<BasicEntity> entry;
			try {
				entry = dictionary.getEntry(st + ",networkResourceDefination", SearchScope.OBJECT, "");
				
				if(entry != null && !entry.isEmpty()) {
					infoMapChinese.put(st, entry.get(0).getValue("display"));
					
				}
			} catch (EntryOperationException e) {
				//e.printStackTrace();
				infoMapChinese.put(st, st);
			}
		}
		
		
		
		String resourceType = formJsonMap.get("resourceType");
		String label = formJsonMap.get("label");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "%"+label+"%");
		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		List<List<ApplicationEntity>> entityListByAetg = this.structureCommonService.getEntityListByEntityTypeRoAetg(resourceType, map, "networkresourcemanage");
		if(entityListByAetg != null && !entityListByAetg.isEmpty()){
			for(List<ApplicationEntity> aeList:entityListByAetg){
				if(aeList != null && !aeList.isEmpty()){
					for(ApplicationEntity ae:aeList){
						if(ae != null){
							Map<String, Object> aemap = ae.toMap();
							String chineseString = infoMapChinese.get(aemap.get("_entityType"))+"";
							aemap.put("chineseString", chineseString);
							results.add(aemap);
						}
					}
				}
			}
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Map<String, String> allTypeMap = new HashMap<String, String>();
		//allTypeMap.put("allType", allType);
		Map<String, String> resourceMsgResultMap = new HashMap<String, String>();
		//mch1.addGroup("allType", allTypeMap);
		resourceMsgResultMap.put("resourceMsgDiv", gson.toJson(results));
		mch.addGroup("resourceMsgDivArea", resourceMsgResultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		//返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据分组获取对应资源类型
	* @date Nov 28, 20122:38:09 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void getResourceTypesByEntitygroupActionForMobile(){
		//System.out.println("omomom");
		HttpSession session = ServletActionContext.getRequest().getSession();
		
//		System.out.println("sessionId: " + session.getId());
//		System.out.println("userId: " + session.getAttribute("userId"));
//		System.out.println("userName: " + session.getAttribute("userName"));
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
		String content = mobilePackage.getContent();
		MobileContentHelper mch = new MobileContentHelper();
		mch.setContent(content);
		Map<String, String> formJsonMap = mch.getGroupByKey("request");
		String aetg = formJsonMap.get("aetg");
		
		String[] aetNameOfAetg = this.structureCommonService.getAetNameOfAetg(aetg, "networkresourcemanage");
		List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
		for(String st:aetNameOfAetg){
			Map<String, Object> map =  new HashMap<String, Object>();
			List<BasicEntity> entry;
			try {
				entry = dictionary.getEntry(st + ",networkResourceDefination", SearchScope.OBJECT, "");
				
				if(entry != null && !entry.isEmpty()) {
					map.put("chineseDisplay", entry.get(0).getValue("display"));
					map.put("resourceType", st);
					
				}
			} catch (EntryOperationException e) {
				//e.printStackTrace();
				map.put("chineseDisplay", st);
				map.put("resourceType", st);
			}
			results.add(map);
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Map<String, String> allTypeMap = new HashMap<String, String>();
		//allTypeMap.put("allType", allType);
		Map<String, String> resourceMsgResultMap = new HashMap<String, String>();
		//mch1.addGroup("allType", allTypeMap);
		resourceMsgResultMap.put("resourceType", gson.toJson(results));
		mch.addGroup("resourceMsgs", resourceMsgResultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		//返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取修改前后对象属性修改信息
	public String getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe){
		String reString = "";
		ApplicationModule module = moduleLibrary.getModule(originalAe.getType());
		for(String key:module.keyset()){
			//System.out.println(originalAe.getValue(key)+ "     " +updateAe.getValue(key));
			if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
				if(originalAe.getValue(key) == null && updateAe.getValue(key) != null || ("null".equals(originalAe.getValue(key)) && !"null".equals(updateAe.getValue(key)) ) || ("".equals(originalAe.getValue(key)) && !"".equals(updateAe.getValue(key)))){
					String keyString = "";
					try {
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							entry = dictionary.getEntry(key + "," + originalAe.getType() + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								keyString = "<"+ entry.get(0).getValue("display").toString().trim() +">";
							}
						}
					} catch (EntryOperationException e) {
						keyString =  "<"+key+">" ;
					}
					reString = reString + keyString + "，修改前：" + " 空值 " + ";修改后： " + updateAe.getValue(key) + " $_$" + (char)10;
				}else if((originalAe.getValue(key) != null && updateAe.getValue(key) == null ) || (!"null".equals(originalAe.getValue(key)) && "null".equals(updateAe.getValue(key)) ) || (!"".equals(originalAe.getValue(key)) && "".equals(updateAe.getValue(key)))){
					String keyString = "";
					try {
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							entry = dictionary.getEntry(key + "," + originalAe.getType() + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								keyString =  "<"+ entry.get(0).getValue("display").toString().trim() +">";
							}
						}
					} catch (EntryOperationException e) {
						keyString =  "<"+key+">";
					}
					reString = reString + keyString + "，修改前： " + originalAe.getValue(key) + " ;修改后：" + " 空值 " + " $_$" + (char)10;
				}else if(originalAe.getValue(key) != null && updateAe.getValue(key) != null  && !"null".equals(originalAe.getValue(key)) && !"null".equals(updateAe.getValue(key)) && !"".equals(originalAe.getValue(key)) && !"".equals(updateAe.getValue(key))){
					if(!originalAe.getValue(key).equals(updateAe.getValue(key))){
						String keyString = "";
						try {
							List<BasicEntity> entry = null;
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								entry = dictionary.getEntry(key + "," + originalAe.getType() + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								
							}
							if(entry != null && !entry.isEmpty()) {
								if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
									keyString =  "<"+ entry.get(0).getValue("display").toString().trim() +">";
								}
							}
						} catch (EntryOperationException e) {
							keyString =  "<"+key+">" ;
						}
						reString = reString + keyString + "，修改前： " + originalAe.getValue(key) + " ;修改后： " + updateAe.getValue(key) +" $_$" + (char)10;
					}
				}
			}
		}
		return reString;
	}

	
	//获取资源基本信息(维护)(网络资源3.0)
	public void loadResPreserveActionForMobile(){
		log.info("进入===loadResPreserveActionForMobile方法");
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
		String content = mobilePackage.getContent();
		MobileContentHelper mch = new MobileContentHelper();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		mch.setContent(content);
		Map<String, String> formJsonMap = mch.getGroupByKey("request");
		String id = formJsonMap.get("id");
		//String id = "";
		String entityType = formJsonMap.get("entityType");
		String addRoUpdate = formJsonMap.get("addRoUpdate");
		String pId = formJsonMap.get("pId");
		String pEntityType = formJsonMap.get("pEntityType");
//		String pId = "1";
//		String pEntityType = "Sys_Area";
		Map<String, Object> infoMap = new HashMap<String, Object>();
		ApplicationEntity entity = structureCommonService.getSectionEntity(entityType,id);
		if(entity == null){
			ApplicationModule module = ModuleProvider.getModule(entityType);
			ApplicationEntity applicationEntity = ActionHelper.getApplicationEntity(entityType);
			String _entityId = applicationEntity.getValue("_entityId")+"";
			String _entityType = applicationEntity.getValue("_entityType")+"";
			infoMap = module.toMap();
			infoMap.put("_entityType", _entityType);
			infoMap.put("_entityId", _entityId);
			infoMap.remove("_moduleName");
			//infoMap = ResourceCommon.applicationEntityConvertMapToNull(infoMap);
		}
		else{
//			this.infoMap = entity.toMap();
//			this.infoMap = ResourceCommon.mapRecombinationToString(infoMap);
			infoMap = ResourceCommon.applicationEntityConvertMap(entity);
		}
		ApplicationModule module = ModuleProvider.getModule(entityType);
		Map<String, Object> infoMapChineseMap =  new HashMap<String, Object>();
		Map<String, Object> nullableMap = new HashMap<String, Object>();
		Map<String, Object> attrTypeMap = new HashMap<String, Object>();
		Map<String,Object> orderIdMap = new HashMap<String,Object>();
		Map<String,Object> currentEntityChineseMap = new HashMap<String,Object>();
		Map<String, List<String>> dropdownListMap = new HashMap<String, List<String>>();
		log.info("开始循环module");
		for(String key: module.keyset()){
			//判断是否需要增加非空判断
			Object nullVal = module.getAttribute(key).getValue("nullable");
			if(nullVal != null && !"".equals(nullVal)) {
				nullableMap.put(key, nullVal.toString());
			}
			//用map保存属性的类型,例如java.lang.String
			Object attrTypeVal = module.getAttribute(key).getValue("type");
			if(attrTypeVal != null && !"".equals(attrTypeVal)) {
				attrTypeMap.put(key, attrTypeVal.toString());
			}
			
			try {
				List<BasicEntity> entry = null;
				if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
					//数据字典，中英文转换
					entry = dictionary.getEntry(key + "," + entityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					
				}
				if(entry != null && !entry.isEmpty()) {
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						currentEntityChineseMap.put(key, entry.get(0).getValue("display"));
						orderIdMap.put(entry.get(0).getValue("orderID").toString(),key);
					}
				}
				
				if(!"_entityId".equals(key)&&!"_entityType".equals(key)) {
					//根据数据字典中，属性为entity类型或者general类型，获取属性对应的下拉框的值，提供前台进行下拉框选择操作
					//拿出来的值，例如:其他-其他,置换-置换,局用-局用
					if("entity".equals(entry.get(0).getValue("dictionaryType"))) {
						//entity类型下拉框数据组装
						List<BasicEntity> dropDownEntry = dictionary.getEntry(key + "," + entityType + ",networkResourceValueEmun", SearchScope.OBJECT, "");
						if(dropDownEntry != null && !dropDownEntry.isEmpty()) {
							String dropdownMixVal = dropDownEntry.get(0).getValue("mixValue");
							//先用逗号进行分隔
							String[] firstSplitArrs = dropdownMixVal.split(",");
							if(firstSplitArrs != null && firstSplitArrs.length > 0) {
								List<String> attrDropdownList = new ArrayList<String>();
								for(String secondVal : firstSplitArrs) {
									//再通过"-"获取key，value，获取value值
									String[] secondSplitArrs = secondVal.split(":");
									if(secondSplitArrs != null && secondSplitArrs.length == 2) {
										attrDropdownList.add(secondSplitArrs[1]);
										
									}
								}
								dropdownListMap.put(key, attrDropdownList);
							}
						}
						
					} else if("general".equals(entry.get(0).getValue("dictionaryType"))) {
						String generalEntryName = entry.get(0).getValue("generalEntryName");
						//general类型下拉框数据组装
						List<BasicEntity> dropDownEntry = dictionary.getEntry(generalEntryName + ",networkResourceValueEmun", SearchScope.OBJECT, "");
						if(dropDownEntry != null && !dropDownEntry.isEmpty()) {
							String dropdownMixVal = dropDownEntry.get(0).getValue("mixValue");
							//先用逗号进行分隔
							String[] firstSplitArrs = dropdownMixVal.split(",");
							if(firstSplitArrs != null && firstSplitArrs.length > 0) {
								List<String> attrDropdownList = new ArrayList<String>();
								for(String secondVal : firstSplitArrs) {
									//再通过"-"获取key，value，获取value值
									String[] secondSplitArrs = secondVal.split(":");
									if(secondSplitArrs != null && secondSplitArrs.length == 2) {
										attrDropdownList.add(secondSplitArrs[1]);
										
									}
								}
								dropdownListMap.put(key, attrDropdownList);
							}
						}
					}
				}
			} catch (EntryOperationException e) {
				log.error("获取数据字典出错");
				//e.printStackTrace();
			}
			
		}
		log.info("结束循环module");
		log.info("开始循环infoMap");
		for(String key : infoMap.keySet()) {
			try {
				List<BasicEntity> entry = null;
				if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
					entry = dictionary.getEntry(key + "," + entityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					
				}
				if(entry != null && !entry.isEmpty()) {
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						infoMapChineseMap.put(key, entry.get(0).getValue("display"));
					}
				}
			} catch (EntryOperationException e) {
				log.error("获取数据字典出错");
				//e.printStackTrace();
				infoMapChineseMap.put(key, key);
			}
		}
		try {
			List<BasicEntity> entry = null;
			if(!"_entityType".equals(entityType) && !"_entityId".equals(entityType)) {
				entry = dictionary.getEntry(entityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
				
			}
			if(entry != null && !entry.isEmpty()) {
					infoMapChineseMap.put("entityTypeChinese", entry.get(0).getValue("display"));
			}
		} catch (EntryOperationException e) {
			log.error("获取数据字典出错");
			//e.printStackTrace();
			infoMapChineseMap.put("entityTypeChinese", entityType);
		}
		log.info("结束循环infoMap");
		Map<String,Object> sortedMap = quickSort.sortMap(infoMap,orderIdMap);
		if(sortedMap!=null){
			infoMap=sortedMap;
		}
		Map<String, String> resourceMsgResultMap = new HashMap<String, String>();
		resourceMsgResultMap.put("resourceMsgChinese", gson.toJson(infoMapChineseMap));
		resourceMsgResultMap.put("resourceMsg", gson.toJson(infoMap));
		resourceMsgResultMap.put("nullableMap", gson.toJson(nullableMap));
		resourceMsgResultMap.put("attrTypeMap", gson.toJson(attrTypeMap));
		resourceMsgResultMap.put("orderIdMap", gson.toJson(orderIdMap));
		resourceMsgResultMap.put("currentEntityChineseMap", gson.toJson(currentEntityChineseMap));
		resourceMsgResultMap.put("dropdownListMap", gson.toJson(dropdownListMap));
		resourceMsgResultMap.put("addRoUpdate", addRoUpdate);
		resourceMsgResultMap.put("pId", pId);
		resourceMsgResultMap.put("pEntityType", pEntityType);
		mch.addGroup("resourceMsgs", resourceMsgResultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		//返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("json转换失败");
			log.info("退出===loadResPreserveActionForMobile方法");
			e.printStackTrace();
		}
		log.info("退出===loadResPreserveActionForMobile方法");
	}
	
	
	//根据资源类型获取子类型
	public void loadStrutureSelationsArrayByResourceTypeActionForMobile(){
		log.info("进入===loadStrutureSelationsArrayByResourceTypeActionForMobile方法");
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
		String content = mobilePackage.getContent();
		MobileContentHelper mch = new MobileContentHelper();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		mch.setContent(content);
		Map<String, String> formJsonMap = mch.getGroupByKey("request");
		String pId = formJsonMap.get("pId");
		String pEntityType = formJsonMap.get("pEntityType");
		Map<String, String> pMap = new HashMap<String, String>();
		//List aetgList = new ArrayList<Map<String,Object>>();
		Map<String,Object> aetgMap = new HashMap<String,Object>();
		if("Room".equals(pEntityType)){
				aetgMap.put("ResGroup_4_Wireless_Flattening","无线");
				aetgMap.put("ResGroup_4_Power_Flattening","动力");
				aetgMap.put("FlatNavigation_4_Room_4_EvironmentAndMonitoring","环境监控");
				aetgMap.put("FlatNavigation_4_Room_4_Transmission","传输");
				aetgMap.put("FlatNavigation_4_Room_4_IndoorCover","室分");
				aetgMap.put("FlatNavigation_4_Room_4_WLAN","WLAN");
		}
		if(pId != null && pEntityType != null){
			ApplicationEntity entity = this.structureCommonService.getSectionEntity(pEntityType, pId);
			if(entity != null){
				String pName = entity.getValue("name");
				pMap.put("pName", pName);
				List<BasicEntity> entry;
				try {
					entry = dictionary.getEntry(pEntityType + ",networkResourceDefination", SearchScope.OBJECT, "");
					
					if(entry != null && !entry.isEmpty()) {
						pMap.put("pChineseDisplay", entry.get(0).getValue("display").toString());
						
					}
				} catch (EntryOperationException e) {
					//e.printStackTrace();
					pMap.put("pChineseDisplay", pEntityType);
				}
			}
			List<Map<String, Object>> results = new ArrayList<Map<String,Object>>();
			if(aetgMap != null && aetgMap.size() > 0){
				for(String s:aetgMap.keySet()){
					String[] aetNameOfAetg = this.structureCommonService.getAetNameOfAetg(s, "networkresourcemanage");
					Collator instance = Collator.getInstance(Locale.CHINA);
					Map<String, Object> map =  new TreeMap<String, Object>(instance);
					for(String st:aetNameOfAetg){
						if("Photo".equals(st)){
							continue;
						}
						List<BasicEntity> entry;
						try {
							entry = dictionary.getEntry(st + ",networkResourceDefination", SearchScope.OBJECT, "");
							
							if(entry != null && !entry.isEmpty()) {
								map.put(entry.get(0).getValue("display").toString(),st);
								
							}
						} catch (EntryOperationException e) {
							//e.printStackTrace();
							map.put(st, st);
						}
					}
					Map<String, Object> mapType = new HashMap<String, Object>();
					mapType.put("agtChinses", aetgMap.get(s));
					mapType.put("chineseMap", map);
					results.add(mapType);
				}
			}else{
				String[] strutureSelationsArray = this.structureCommonService.getStrutureSelationsArray(entity, AssociatedType.CHILD, "networkresourcemanage");
				Collator instance = Collator.getInstance(Locale.CHINA);
				Map<String, Object> map = null;
				if(strutureSelationsArray != null && strutureSelationsArray.length > 0){
					map = new TreeMap<String, Object>(instance);
					for(String st:strutureSelationsArray){
						if("Photo".equals(st)){
							continue;
						}
						List<BasicEntity> entry;
						try {
							entry = dictionary.getEntry(st + ",networkResourceDefination", SearchScope.OBJECT, "");
							
							if(entry != null && !entry.isEmpty()) {
								map.put(entry.get(0).getValue("display").toString(),st);
								
							}
						} catch (EntryOperationException e) {
							//e.printStackTrace();
							map.put(st, st);
						}
					}
				}else{
					map = null;
				}
				Map<String, Object> mapType = new HashMap<String, Object>();
				mapType.put("agtChinses", "下属资源");
				mapType.put("chineseMap", map);
				results.add(mapType);
			}
			Map<String, String> allTypeMap = new HashMap<String, String>();
			//allTypeMap.put("allType", allType);
			Map<String, String> resourceMsgResultMap = new HashMap<String, String>();
			//mch1.addGroup("allType", allTypeMap);
			
			pMap.put("pId", pId);
			pMap.put("pEntityType", pEntityType);
			resourceMsgResultMap.put("resourceType", gson.toJson(results));
			resourceMsgResultMap.put("pMap", gson.toJson(pMap));
			mch.addGroup("resourceMsgs", resourceMsgResultMap);
			mobilePackage.setResult("success");
			mobilePackage.setContent(mch.mapToJson ());
			//返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(mobilePackage);
			try {
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e) {
				log.error("json转换出错");
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.info("退出===loadResPreserveActionForMobile方法");
			}
		}
		log.info("退出===loadStrutureSelationsArrayByResourceTypeActionForMobile方法");
	}
	
	
	public void loadNetworkResourceMaintenanceActionForMobile(){
		log.info("进入===loadNetworkResourceMaintenanceActionForMobile");
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
		String content = mobilePackage.getContent();
		MobileContentHelper mch = new MobileContentHelper();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		mch.setContent(content);
		Map<String, String> formJsonMap = mch.getGroupByKey("request");
		String id = formJsonMap.get("id");
		String entityType = formJsonMap.get("entityType");
		String start = formJsonMap.get("start");
		String pageNum = formJsonMap.get("pageNum");
		List<ResourceMaintenance> resourceMaintenanceRecordsByResTypeAndResIdLIMIT = this.networkResourceMaintenanceService.getResourceMaintenanceRecordsByResTypeAndResIdLIMIT(entityType, id, start, pageNum);
		List<Map<String, Object>>  resourceMaintenanceList = new ArrayList<Map<String,Object>>();
		String chineseTypeName = entityType;
		try {
			List<BasicEntity> entry = null;
			entry = dictionary.getEntry(entityType+ ",networkResourceDefination" ,SearchScope.OBJECT, "");
			chineseTypeName = entry.get(0).getValue("display").toString();
		} catch (EntryOperationException e) {
			log.error("获取数据字典失败");
			e.printStackTrace();
		}
		if(resourceMaintenanceRecordsByResTypeAndResIdLIMIT != null && resourceMaintenanceRecordsByResTypeAndResIdLIMIT.size() > 0){
			for(ResourceMaintenance Re:resourceMaintenanceRecordsByResTypeAndResIdLIMIT){
				String reContent = Re.getContent();
				//System.out.println(reContent);
				if(reContent != null){
					reContent = reContent.replaceAll("\\$_\\$", " ");
				}else{
					reContent = "";
				}
				//System.out.println(reContent +"==");
				Re.setContent(reContent);
				ApplicationEntity applicationEntity = Re.getApplicationEntity();
				Map<String, Object> map = applicationEntity.toMap();
				map.put("chineseTypeName", chineseTypeName);
				resourceMaintenanceList.add(map);
			}
		}else{
			log.info("resourceMaintenanceRecordsByResTypeAndResIdLIMIT为空");
		}
		Map<String, String> resourceMsgResultMap = new HashMap<String, String>();
		resourceMsgResultMap.put("resourceType", gson.toJson(resourceMaintenanceList));
		mch.addGroup("resourceMsgs", resourceMsgResultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		//返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			log.error("json转换出错");
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("退出===loadNetworkResourceMaintenanceActionForMobile");
		}
		log.info("退出===loadNetworkResourceMaintenanceActionForMobile");
	}
	
	
	/**
	 * 根据坐标，指定半径查询指定查询条件不指定资源或指定资源组类型数据(圆环)分页
	 * @param AetgName 资源组类型(可以不指定)
	 * @param innerDistance 内半径
	 * @param outerDistance 外半径
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param map 需要的查询条件
	 * @param start 开始下标
	 * @param end 结束下标
	 * @param myStructure structure名字
	 * @return List<Map<String,Object>>
	 */
	public void getEntityListRingByAetgAndGPSDistanceAndPagingActionForMobile(){
		log.info("进入===getEntityListRingByAetgAndGPSDistanceAndPagingActionForMobile");
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
		String content = mobilePackage.getContent();
		MobileContentHelper mch = new MobileContentHelper();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		mch.setContent(content);
		Map<String, String> formJsonMap = mch.getGroupByKey("request");
		String AetgName = formJsonMap.get("AetgName");
		String innerDistanceString = formJsonMap.get("innerDistance");
		String outerDistanceString = formJsonMap.get("outerDistance");
		String longitudeString = formJsonMap.get("longitude");
		String latitudeString = formJsonMap.get("latitude");
		String condition = formJsonMap.get("condition");
		String conditionValue = formJsonMap.get("conditionValue");
		String startString = formJsonMap.get("start");
		String endString = formJsonMap.get("end");
		String[] splitcondition = null;
		Map<String, Object> map = null;
		//yuan.yw add 对用户区域进行筛选
		String userId =(String)session.getAttribute("userId");
		/*List<BasicEntity> userAreaIds = this.staffOrganizationService.getAreaByUserId(userId);
		List<String> areaIdsList = null;
		if(userAreaIds!=null && userAreaIds.size()>0){
			areaIdsList = new ArrayList<String>();
			for(BasicEntity be:userAreaIds){
				areaIdsList.add(be.getValue("id")+"");
				ApplicationEntity[] apList = this.structureCommonService.getAppArrsByRecursionForSrcSameType(ApplicationEntity.changeFromEntity(be), "Sys_Area", "networkresourcemanage");
				if(apList!=null && apList.length>0){
					for(ApplicationEntity ap:apList){
						areaIdsList.add(ap.getValue("id")+"");
					}
				}
			}
		}*/
		if(condition != null&&!"".equals(condition)){
			
			splitcondition = condition.split(",");
		}
		
		String[] splitconditionValue = null;
		if(conditionValue != null&&!"".equals(conditionValue)){

			splitconditionValue = conditionValue.split(",");
		}
		if(splitcondition != null && splitcondition.length > 0 && splitconditionValue != null && splitconditionValue.length > 0 ){
			map = new HashMap<String, Object>();
			for(int i = 0;splitcondition.length > i;i++){
				map.put(splitcondition[i], "%"+splitconditionValue[i]+"%");
			}
		}
		
		double innerDistance = 0;
		if(innerDistanceString != null && !"".equals(innerDistanceString)){
			innerDistance = Double.parseDouble(innerDistanceString);
		}else{
			innerDistance = 0;
		}
		double outerDistance = 0;
		if(outerDistanceString != null && !"".equals(outerDistanceString)){
			outerDistance = Double.parseDouble(outerDistanceString);
		}else{
			outerDistance = 0;
		}
		double longitude = 0;
		if(longitudeString != null && !"".equals(longitudeString)){
			longitude = Double.parseDouble(longitudeString);
		}else{
			longitude = 0;
		}
		double latitude = 0;
		if(latitudeString != null && !"".equals(latitudeString)){
			latitude = Double.parseDouble(latitudeString);
		}else{
			latitude = 0;
		}
		int start = 0;
		if(startString != null && !"".equals(startString)){
			start = Integer.parseInt(startString);
		}else{
			start = 0;
		}
		int end = 0;
		if(endString != null && !"".equals(startString)){
			end = Integer.parseInt(endString);
		}else{
			end = 0;
		}
		//List<Map<String,Object>> entityListRingByAetgAndGPSDistanceAndPaging = this.structureCommonService.getEntityListRingByAetgAndGPSDistanceAndPaging(AetgName, innerDistance, outerDistance, longitude, latitude, map, start, end,areaIdsList,"networkresourcemanage");
		
		Map<String,Object> entityListRingByAetgAndGPSDistanceAndPaging = this.structureCommonService.getResourceWithPagingByAetNameAndGPSDistanceAndAccount(AetgName, longitude, latitude, map, start, end, userId);
		//		Map<String, String> resourceMsgResultMap = new HashMap<String, String>();
//		resourceMsgResultMap.put("resourceType", gson.toJson(entityListRingByAetgAndGPSDistanceAndPaging));
//		mch.addGroup("resourceMsgs", resourceMsgResultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(gson.toJson(entityListRingByAetgAndGPSDistanceAndPaging));
		//返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			log.error("json转换出错");
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("退出===loadNetworkResourceMaintenanceActionForMobile");
		}
		log.info("退出===getEntityListRingByAetgAndGPSDistanceAndPagingAction方法");
	}

	public XMLAEMLibrary getModuleLibrary() {
		return moduleLibrary;
	}


	public void setModuleLibrary(XMLAEMLibrary moduleLibrary) {
		this.moduleLibrary = moduleLibrary;
	}


	public NetworkResourceMaintenanceService getNetworkResourceMaintenanceService() {
		return networkResourceMaintenanceService;
	}


	public void setNetworkResourceMaintenanceService(
			NetworkResourceMaintenanceService networkResourceMaintenanceService) {
		this.networkResourceMaintenanceService = networkResourceMaintenanceService;
	}
}
