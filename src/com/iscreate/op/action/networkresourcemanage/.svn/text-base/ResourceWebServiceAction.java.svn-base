package com.iscreate.op.action.networkresourcemanage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.networkresourcemanage.StaffOrganizationService;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.QuickSort;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.plat.networkresource.dictionary.SearchScope;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;


public class ResourceWebServiceAction {
	private String areaId;
	
	private String resourceType;
	
	private String baseStationId;
	
	private String baseStationType;
	
	private String reId;
	
	private String reType;
	
	private String selectReType;
	
	private String associatedType;
	
	private String baseStationName;
	
	public Dictionary dictionary;
	
	private String cellName;
	
	private QuickSort<Map<String,Object>> quickSort;//排序
	
	private StructureCommonService structureCommonService;
	
	private String condition;
	
	
	
	private String AetgName ;
	private double innerDistance;
	private double outerDistance;
	private double longitude;
	private double latitude;
	private int start;
	private int end;

	private  static final  Log log = LogFactory.getLog(ResourceWebServiceAction.class);
	private String aetgs;//专业组字符串
	
	private String conditionValue;
	
	private StaffOrganizationService staffOrganizationService;//人员组织接口
	
	
	
	public StaffOrganizationService getStaffOrganizationService() {
		return staffOrganizationService;
	}

	public void setStaffOrganizationService(
			StaffOrganizationService staffOrganizationService) {
		this.staffOrganizationService = staffOrganizationService;
	}

	public String getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}

	public String getAetgs() {
		return aetgs;
	}

	public void setAetgs(String aetgs) {
		this.aetgs = aetgs;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}

	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}
	
	public String getBaseStationId() {
		return baseStationId;
	}

	public void setBaseStationId(String baseStationId) {
		this.baseStationId = baseStationId;
	}

	public String getBaseStationType() {
		return baseStationType;
	}

	public void setBaseStationType(String baseStationType) {
		this.baseStationType = baseStationType;
	}
	
	public void getBaseStationByAreaAction(){
		log.info("进入===getBaseStationByAreaAction方法");
		if(areaId != null && !areaId.equals("")){
			String[] split = areaId.split(",");
			List<String> areaIds = new ArrayList<String>();
			if(split != null && split.length > 0){
				for(String s : split){
					areaIds.add(s);
				}
			}
			List<Map<String, Object>> reList  = structureCommonService.getBaseStationByAreasByHibernate(areaIds, resourceType);
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(reList);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				log.error("json转换失败");
				e.printStackTrace();
			}
		}
		log.info("退出===getBaseStationByAreaAction方法");
	}
	
	public void getAreaByBaseStationAction(){
		log.info("进入===getAreaByBaseStationAction方法");
		BasicEntity areaByStation = structureCommonService.getAreaByStation(baseStationId, baseStationType);
		Map<String, Object> map = null;
		if(areaByStation != null){
			map = areaByStation.toMap();
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
			log.error("json转换失败");
			e.printStackTrace();
		}
		log.info("退出===getAreaByBaseStationAction方法");
	}

	public void getResourceByAssociatedTypeAction(){
		log.info("进入===getResourceByAssociatedTypeAction方法");
		ApplicationEntity sectionEntity = structureCommonService.getSectionEntity(reType, reId);
		AssociatedType type = null;
		if("CHILD".equals(associatedType)){
			type = AssociatedType.CHILD;
		}else if("PARENT".equals(associatedType)){
			type = AssociatedType.PARENT;
		}else if("LINK".equals(associatedType)){
			type = AssociatedType.LINK;
		}
		ApplicationEntity[] strutureSelationsApplicationEntity = structureCommonService.getStrutureSelationsApplicationEntity(sectionEntity,selectReType, type, "networkresourcemanage");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(strutureSelationsApplicationEntity != null && strutureSelationsApplicationEntity.length > 0){
			log.info("开始循环strutureSelationsApplicationEntity");
			for(ApplicationEntity ae:strutureSelationsApplicationEntity){
				list.add(ae.toMap());
			}
			log.info("结束循环strutureSelationsApplicationEntity");
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换失败");
			e.printStackTrace();
		}
		log.info("退出===getResourceByAssociatedTypeAction方法");
	}

	public void getResourceByreIdAndreTypeAction(){
		log.info("进入===getResourceByreIdAndreTypeAction方法");
		ApplicationModule module = ModuleProvider.getModule(reType);
		ApplicationEntity sectionEntity = structureCommonService.getSectionEntity(reType, reId);
		Map<String,Object> orderIdMap = new HashMap<String,Object>();
		Map<String, Map<String, Object>> maps = new HashMap<String, Map<String,Object>>();
		if(sectionEntity !=null){
			Map<String, Object> map = new HashMap<String, Object>();
			map = module.toMap();
			Map<String, Object> sectionMap = sectionEntity.toMap();
			Map<String, Object> infoMapChineseMap = new HashMap<String, Object>();
			String infoName = reType;
			log.info("开始循环map");
			for(String key : map.keySet()) {
				try {
					List<BasicEntity> entry = null;
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						entry = dictionary.getEntry(key + "," + infoName + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						
					}
					if(entry != null && !entry.isEmpty()) {
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							infoMapChineseMap.put(key, entry.get(0).getValue("display"));
							orderIdMap.put(entry.get(0).getValue("orderID").toString(),key);
						}
					}
					
				} catch (EntryOperationException e) {
					log.error("获取数据字典失败");
					e.printStackTrace();
				}
			}
			log.info("结束循环map");
			Map<String,Object> sortedMap = quickSort.sortMap(infoMapChineseMap,orderIdMap);
			log.info("开始循环sectionMap");
			for(String k :sectionMap.keySet()){
				if(sectionMap.get(k) != null && sectionMap.get(k).getClass().toString().equals("class java.util.Date")){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss");
						String format = sdf.format(sectionMap.get(k));
						sectionMap.put(k, format);
				}
			}
			log.info("结束循环sectionMap");
			Map<String, Object> chineseTypeNameMap = new HashMap<String, Object>();
			try {
				List<BasicEntity> entry = null;
				entry = dictionary.getEntry(infoName + ",networkResourceDefination" ,SearchScope.OBJECT, "");
				chineseTypeNameMap.put("chineseTypeName", entry.get(0).getValue("display"));		
			} catch (EntryOperationException e) {
				log.error("获取数据字典失败");
				e.printStackTrace();
			}
			maps.put("entity", sectionMap);
			maps.put("dictionary", sortedMap);
			maps.put("chineseTypeNameMap", chineseTypeNameMap);
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(maps);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换失败");
			e.printStackTrace();
		}
		log.info("退出===getResourceByreIdAndreTypeAction方法");
	}
	
	//根据基站ID获取站址
	public void getStationByBaseStationAction(){
		log.info("进入===getStationByBaseStationAction方法");
		List<BasicEntity> stationByBaseStation = structureCommonService.getStationByBaseStation(this.baseStationId, this.baseStationType, this.selectReType);
		Map<String, Object> map = new HashMap<String, Object>();
		if(stationByBaseStation != null && stationByBaseStation.size() > 0){
			map = stationByBaseStation.get(0).toMap();
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
			log.error("json转换失败");
			e.printStackTrace();
		}
		log.info("退出===getStationByBaseStationAction方法");
	}
	
	//根据站址ID获取基站
	public void getBaseStationByStationAction(){
		log.info("进入===getBaseStationByStationAction方法");
		List<Map<String, Object>> list = structureCommonService.getBaseStationByStationByHibernate(this.reId, this.selectReType);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.info("json转换失败");
			e.printStackTrace();
		}
		log.info("退出===getBaseStationByStationAction方法");
	}
	
	
	public void getStationCountByAreaAction(){
		log.info("进入===getStationCountByAreaAction方法");
		int count = 0;
		if(areaId != null && !areaId.equals("")){
			String[] split = areaId.split(",");
			List<String> areaIds = new ArrayList<String>();
			if(split != null && split.length > 0){
				for(String s : split){
					areaIds.add(s);
				}
			}
			count = this.structureCommonService.getStationCountByArea(areaIds);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(count);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换失败");
			e.printStackTrace();
		}
		log.info("退出===getStationCountByAreaAction方法");
	}
	
	public void getBaseStationCountByAreaAction(){
		log.info("进入===getBaseStationCountByAreaAction方法");
		int count = 0;
		if(areaId != null && !areaId.equals("")){
			String[] split = areaId.split(",");
			List<String> areaIds = new ArrayList<String>();
			if(split != null && split.length > 0){
				for(String s : split){
					areaIds.add(s);
				}
			}
			count = this.structureCommonService.getBaseStationCountByArea(areaIds);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(count);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换失败");
			e.printStackTrace();
		}
		log.info("退出===getBaseStationCountByAreaAction方法");
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
	public void getEntityListRingByAetgAndGPSDistanceAndPagingAction(){
		//yuan.yw add 对用户区域进行筛选
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId =(String)session.getAttribute("userId");
		List<BasicEntity> userAreaIds = this.staffOrganizationService.getAreaByUserId(userId);
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
		}
		String[] splitcondition = null;
		Map<String, Object> map = null;
		if(this.condition != null){
			
			splitcondition = condition.split(",");
		}
		if(conditionValue != null && !conditionValue.equals("")){
			try {
				this.conditionValue = new String(this.conditionValue.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				log.error("字符串转换失败");
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		String[] splitconditionValue = null;
		if(this.conditionValue != null){

			splitconditionValue = conditionValue.split(",");
		}
		if(splitcondition != null && splitcondition.length > 0 && splitconditionValue != null && splitconditionValue.length > 0 ){
			map = new HashMap<String, Object>();
			for(int i = 0;splitcondition.length > i;i++){
				map.put(splitcondition[i], "%"+splitconditionValue[i]+"%");
			}
		}
		log.info("进入===getEntityListRingByAetgAndGPSDistanceAndPagingAction方法");
		List<Map<String,Object>> entityListRingByAetgAndGPSDistanceAndPaging = this.structureCommonService.getEntityListRingByAetgAndGPSDistanceAndPaging(AetgName, innerDistance, outerDistance, longitude, latitude, map, start, end,areaIdsList, "networkresourcemanage");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(entityListRingByAetgAndGPSDistanceAndPaging);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换失败");
			e.printStackTrace();
		}
		log.info("退出===getEntityListRingByAetgAndGPSDistanceAndPagingAction方法");
	}
	
	public String reTypes;
	
	public String getReTypes() {
		return reTypes;
	}

	public void setReTypes(String reTypes) {
		this.reTypes = reTypes;
	}

	public void getReportResourceListAction(){
		log.info("进入===getReportResourceListAction方法");
		Map<String, String> reportResourceList = new HashMap<String,String>();
		List<String> a = new ArrayList<String>();
		if(areaId != null && !areaId.equals("")){
			ApplicationEntity sectionEntity = this.structureCommonService.getSectionEntity("Sys_Area", areaId);
			ApplicationEntity[] appArrsByRecursionForSrcSameType = this.structureCommonService.getAppArrsByRecursionForSrcSameType(sectionEntity, "Sys_Area", "networkresourcemanage");
			if(appArrsByRecursionForSrcSameType != null && appArrsByRecursionForSrcSameType.length > 0){
				log.info("开始循环appArrsByRecursionForSrcSameType");
				for(ApplicationEntity ae:appArrsByRecursionForSrcSameType){
					String aeid = ae.getValue("id")+"";
					a.add(aeid);
				}
				log.info("结束循环appArrsByRecursionForSrcSameType");
			}
				a.add(areaId);
		}
		if(reTypes != null && !reTypes.equals("")){
			String[] split = reTypes.split(",");
			String res = "";
			log.info("开始循环split");
			for(String s:split){
				if(s.equals("Station")){
					int count =  this.structureCommonService.getStationCountByArea(a);
					//System.out.println(count);
					reportResourceList.put(s, count+"");
				}else if(s.equals("Cell")){
					int cellCountByArea = this.structureCommonService.getCellCountByArea(a);
					reportResourceList.put(s, cellCountByArea+"");
				}else{
					List<Map<String,Object>> baseStationByAreasByHibernate = this.structureCommonService.getBaseStationByAreasByHibernate(a, s);
					int count = 0;
					if(baseStationByAreasByHibernate != null && !baseStationByAreasByHibernate.isEmpty()){
						count = baseStationByAreasByHibernate.size();
					}
					reportResourceList.put(s, count+"");
				}
				
			}
			log.info("结束循环split");
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(reportResourceList);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换失败");
			e.printStackTrace();
		}
		log.info("退出===getReportResourceListAction方法");
	}
	
	
	public void getCellByNameAction(){
		log.info("进入===getCellByNameAction方法");
		if(cellName != null && !cellName.equals("")){
			try {
				this.cellName = new String(this.cellName.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				log.error("字符串转换失败");
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		Map<String, Object> cellByName = this.structureCommonService.getCellByName(this.cellName);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(cellByName);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换失败");
			e.printStackTrace();
		}
		log.info("退出===getCellByNameAction方法");
	}
	
	public void getBaseStationByNameAction(){
		log.info("进入===getBaseStationByNameAction方法");
		if(baseStationName != null && !baseStationName.equals("")){
			try {
				this.baseStationName = new String(this.baseStationName.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				log.error("字符串转换失败");
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("name", baseStationName);
		List<List<ApplicationEntity>> entityListByAetg = this.structureCommonService.getEntityListByEntityTypeRoAetg(this.baseStationType,map, "networkresourcemanage");
		Map<String,Object> baseStationByName = new HashMap<String, Object>();
		if(entityListByAetg != null && !entityListByAetg.isEmpty()){
			List<ApplicationEntity> list = entityListByAetg.get(0);
			if(list != null && !list.isEmpty()){
				ApplicationEntity applicationEntity = list.get(0);
				if(applicationEntity != null){
					baseStationByName = applicationEntity.toMap();
				}
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(baseStationByName);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换失败");
			e.printStackTrace();
		}
		log.info("退出===getBaseStationByNameAction方法");
	}
	
	
	public void getAppArrsByRecursionForSrcAction(){
		log.info("进入===getAppArrsByRecursionForSrcAction方法");
		ApplicationEntity sectionEntity = this.structureCommonService.getSectionEntity(this.reType, this.reId);
		List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		if(reType.equals(selectReType)){
			ApplicationEntity[] appArrsByRecursionForSrcSameType = this.structureCommonService.getAppArrsByRecursionForSrcSameType(sectionEntity,selectReType,"networkresourcemanage");
			if(appArrsByRecursionForSrcSameType != null){
				log.info("开始循环appArrsByRecursionForSrcSameType");
				for(ApplicationEntity app:appArrsByRecursionForSrcSameType){
					if(app != null){
						Map<String, Object> map = app.toMap();
						listMap.add(map);
					}
				}
				log.info("结束循环appArrsByRecursionForSrcSameType");
			}
		}else{
			ApplicationEntity[] appArrsByRecursionForSrc = this.structureCommonService.getAppArrsByRecursionForSrc(sectionEntity, this.selectReType, "networkresourcemanage");
			if(appArrsByRecursionForSrc != null){
				log.info("开始循环appArrsByRecursionForSrc");
				for(ApplicationEntity app:appArrsByRecursionForSrc){
					if(app != null){
						Map<String, Object> map = app.toMap();
						listMap.add(map);
					}
				}
				log.info("结束循环appArrsByRecursionForSrc");
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(listMap);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换失败");
			e.printStackTrace();
		}
		log.info("进入===getAppArrsByRecursionForSrcAction方法");
	}
	
	//根据站址ID获取基站
	public void getRoomByAreasAction(){
		log.info("进入===getBaseStationByStationAction方法");
		if(areaId != null && !areaId.equals("")){
			String[] split = areaId.split(",");
			List<String> areaIds = new ArrayList<String>();
			if(split != null && split.length > 0){
				for(String s : split){
					areaIds.add(s);
				}
			}
			List<Map<String, Object>> list = structureCommonService.getRoomByAreasByHibernate(areaIds, this.selectReType);
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(list);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				log.info("json转换失败");
				e.printStackTrace();
			}
		}
		log.info("退出===getBaseStationByStationAction方法");
	}
	// 获取机房专业分组下的资源实例list
	public void getResourceEntitysByAetgsAction(){
		log.info("进入getResourceEntitysByAetgsAction(),reType="+reType+",reId="+reId+",aetgs="+aetgs+",获取机房专业分组下的资源实例list");
		if(aetgs!=null && !"".equals(aetgs)){
			Map<String,Object> resultMap = new HashMap<String,Object>();
			ApplicationEntity sectionEntity = this.structureCommonService.getSectionEntity(this.reType, this.reId);
			String[] split = aetgs.split(",");
			if(split != null && split.length > 0){
				for(String s : split){
					List<Map<String,Object>> rList = new ArrayList<Map<String,Object>>();
					String[] aetNames=null;
					String chineseName="";
					
					aetNames=structureCommonService.getAetNameOfAetg(s,"networkresourcemanage");
					if(aetNames!=null&&!"".equals(aetNames)){
						for(String aetName:aetNames){
							List<BasicEntity> entry1 = null;
							try {
								entry1 = dictionary.getEntry(aetName + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								if(entry1 != null && !entry1.isEmpty()) {
									chineseName = entry1.get(0).getValue("display").toString();
								}
							} catch (EntryOperationException e) {
								log.error("getResourceEntitysByAetgsAction,获取"+aetName+"中文字典失败，可能该字典不存在");
								e.printStackTrace();
								chineseName = aetName;
							}
							ApplicationEntity[] apps = this.structureCommonService.getAppArrsByRecursionForSrcSameType(sectionEntity, aetName, "networkresourcemanage");
							if(apps!=null){
								for(ApplicationEntity ap:apps){
									Map<String,Object> rMap = ap.toMap();
									rMap.put("chineseTypeName", chineseName);
									rList.add(rMap);
								}
							}
						}
					}
					if(rList!=null && !rList.isEmpty()){
						quickSort.sort(rList, "name");
					}
					resultMap.put(s, rList);
				}
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(resultMap);
			try {
				log.info("退出getResourceEntitysByAetgsAction(),返回结果result="+result);
				response.getWriter().write(result);
			} catch (IOException e) {
				log.error("退出getResourceEntitysByAetgsAction(),json转换失败");
				e.printStackTrace();
			}
		}
	}
	
	
	//根据站址ID获取
	public void getResByAreasAction11(){
		log.info("进入===getResByAreasAction方法");
		if(areaId != null && !areaId.equals("")){
			String[] split = areaId.split(",");
			List<String> areaIds = new ArrayList<String>();
			if(split != null && split.length > 0){
				for(String s : split){
					areaIds.add(s);
				}
			}
			String[] splitcondition = null;
			Map<String, String> map = null;
			if(this.condition != null){
				
				splitcondition = condition.split(",");
			}
			if(conditionValue != null && !conditionValue.equals("")){
				try {
					this.conditionValue = new String(this.conditionValue.getBytes("iso-8859-1"),"UTF-8") ;
				} catch (UnsupportedEncodingException e1) {
					log.error("字符串转换失败");
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			String[] splitconditionValue = null;
			if(this.conditionValue != null){
				splitconditionValue = conditionValue.split(",");
			}
			if(splitcondition != null && splitcondition.length > 0 && splitconditionValue != null && splitconditionValue.length > 0 ){
				map = new HashMap<String, String>();
				for(int i = 0;splitcondition.length > i;i++){
					map.put(splitcondition[i], "%"+splitconditionValue[i]+"%");
				}
			}
			List<Map<String, Object>> list = structureCommonService.getResByAreasByHibernate(areaIds, this.selectReType,map);
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(list);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				log.info("json转换失败");
				e.printStackTrace();
			}
		}
		log.info("退出===getResByAreasAction方法");
	}
	
	
	//根据站址ID获取
	public void getResByAreasRecursionAction(){
		log.info("进入===getResByAreasAction方法");
		if(areaId != null && !areaId.equals("")){
			String[] split = areaId.split(",");
			List<String> areaIds = new ArrayList<String>();
			if(split != null && split.length > 0){
				for(String s : split){
					areaIds.add(s);
				}
			}
			String[] splitcondition = null;
			Map<String, String> map = null;
			if(this.condition != null){
				
				splitcondition = condition.split(",");
			}
			if(conditionValue != null && !conditionValue.equals("")){
				try {
					this.conditionValue = new String(this.conditionValue.getBytes("iso-8859-1"),"UTF-8") ;
				} catch (UnsupportedEncodingException e1) {
					log.error("字符串转换失败");
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			String[] splitconditionValue = null;
			if(this.conditionValue != null){
				splitconditionValue = conditionValue.split(",");
			}
			if(splitcondition != null && splitcondition.length > 0 && splitconditionValue != null && splitconditionValue.length > 0 ){
				map = new HashMap<String, String>();
				for(int i = 0;splitcondition.length > i;i++){
					map.put(splitcondition[i], "%"+splitconditionValue[i]+"%");
				}
			}
			List<String> arIds = new ArrayList<String>();
			if(areaIds != null && areaIds.size() > 0){
				for(String ar:areaIds){
					arIds.add(ar);
					ApplicationEntity sectionEntity = this.structureCommonService.getSectionEntity("Sys_Area", ar);
					ApplicationEntity[] appArrsByRecursionForSrc = this.structureCommonService.getAppArrsByRecursionForSrcSameType(sectionEntity, "Sys_Area", "networkresourcemanage");
					if(appArrsByRecursionForSrc != null){
						for(ApplicationEntity ae:appArrsByRecursionForSrc){
							String string = ae.getValue("id").toString();
							arIds.add(string);
						}
					}
				}
			}
			List<Map<String, Object>> list = structureCommonService.getResByAreasHibernate(arIds, this.selectReType,map);
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(list);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				log.info("json转换失败");
				e.printStackTrace();
			}
		}
		log.info("退出===getResByAreasAction方法");
	}
	
	public String getReId() {
		return reId;
	}

	public void setReId(String reId) {
		this.reId = reId;
	}

	public String getReType() {
		return reType;
	}

	public void setReType(String reType) {
		this.reType = reType;
	}

	public String getSelectReType() {
		return selectReType;
	}

	public void setSelectReType(String selectReType) {
		this.selectReType = selectReType;
	}

	public String getAssociatedType() {
		return associatedType;
	}

	public void setAssociatedType(String associatedType) {
		this.associatedType = associatedType;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public QuickSort<Map<String, Object>> getQuickSort() {
		return quickSort;
	}

	public void setQuickSort(QuickSort<Map<String, Object>> quickSort) {
		this.quickSort = quickSort;
	}

	public String getAetgName() {
		return AetgName;
	}

	public void setAetgName(String aetgName) {
		AetgName = aetgName;
	}

	public double getInnerDistance() {
		return innerDistance;
	}

	public void setInnerDistance(double innerDistance) {
		this.innerDistance = innerDistance;
	}

	public double getOuterDistance() {
		return outerDistance;
	}

	public void setOuterDistance(double outerDistance) {
		this.outerDistance = outerDistance;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getBaseStationName() {
		return baseStationName;
	}

	public void setBaseStationName(String baseStationName) {
		this.baseStationName = baseStationName;
	}



	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}
