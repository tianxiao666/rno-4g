package com.iscreate.op.action.networkresourcemanage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.networkresourcemanage.NetworkReportService;
import com.iscreate.op.service.networkresourcemanage.StaffOrganizationService;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.GetChineseFirstLetter;
import com.iscreate.plat.networkresource.common.tool.QuickSort;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.plat.networkresource.dictionary.SearchScope;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;

public class NetworkReportAction {
	
	public StructureCommonService structureCommonService;
	
	
	private List<Map<String, Object>> allTypeMapList; //所有类型
	
	private String areaId;
	
	private String areaType;
	
	private String areaName;
	
	private Dictionary dictionary;
	
	private String selectResType; //需要查询的资源的类型
	
	
	private String allLink;
	
	private QuickSort<Map<String,Object>> quickSort; //对entity属性及对象排序
	
	
	private String entityId;
	
	private String resourceType;
	
	
	private String currentType;
	
	private Map<String, List<String>> allTypeMap;
	
	//导出excel
	//导出文件的输入流
	private InputStream resultInputStream;
	//文件名
	private String chaxunFileName;
	
	//查询条件
	private String conditions;
	
	//查询列
	private String column;
	
	//查询值
	private String columnValue;
	
	private NetworkReportService networkReportService;
	
	
	private StaffOrganizationService staffOrganizationService;
	
	
	private String repertType;
	
	
	private String tableTitle;
	
	private  static final  Log log = LogFactory.getLog(NetworkReportAction.class);
	
	public String getTableTitle() {
		return tableTitle;
	}



	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}



	public String getRepertType() {
		return repertType;
	}



	public void setRepertType(String repertType) {
		this.repertType = repertType;
	}



	public NetworkReportService getNetworkReportService() {
		return networkReportService;
	}



	public void setNetworkReportService(NetworkReportService networkReportService) {
		this.networkReportService = networkReportService;
	}



	public String getConditions() {
		return conditions;
	}



	public void setConditions(String conditions) {
		this.conditions = conditions;
	}



	public String getColumn() {
		return column;
	}



	public void setColumn(String column) {
		this.column = column;
	}



	public String getColumnValue() {
		return columnValue;
	}



	public void setColumnValue(String columnValue) {
		this.columnValue = columnValue;
	}



	public InputStream getResultInputStream() {
		return resultInputStream;
	}



	public void setResultInputStream(InputStream resultInputStream) {
		this.resultInputStream = resultInputStream;
	}



	public String getChaxunFileName() {
		return chaxunFileName;
	}



	public void setChaxunFileName(String chaxunFileName) {
		this.chaxunFileName = chaxunFileName;
	}



	public Map<String, List<String>> getAllTypeMap() {
		return allTypeMap;
	}



	public void setAllTypeMap(Map<String, List<String>> allTypeMap) {
		this.allTypeMap = allTypeMap;
	}



	public String getEntityId() {
		return entityId;
	}



	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}



	public String getResourceType() {
		return resourceType;
	}



	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}



	public String getCurrentType() {
		return currentType;
	}



	public void setCurrentType(String currentType) {
		this.currentType = currentType;
	}



	public QuickSort<Map<String, Object>> getQuickSort() {
		return quickSort;
	}



	public void setQuickSort(QuickSort<Map<String, Object>> quickSort) {
		this.quickSort = quickSort;
	}



	public String getAllLink() {
		return allLink;
	}



	public void setAllLink(String allLink) {
		this.allLink = allLink;
	}



	public String getAreaType() {
		return areaType;
	}



	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}



	public String getSelectResType() {
		return selectResType;
	}



	public void setSelectResType(String selectResType) {
		this.selectResType = selectResType;
	}



	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}



	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}



	public List<Map<String, Object>> getAllTypeMapList() {
		return allTypeMapList;
	}



	public void setAllTypeMapList(List<Map<String, Object>> allTypeMapList) {
		this.allTypeMapList = allTypeMapList;
	}



	public String getAreaId() {
		return areaId;
	}



	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}



	public String getAreaName() {
		return areaName;
	}



	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}



	public Dictionary getDictionary() {
		return dictionary;
	}



	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}


	//加载报表Action 
	public String loadNetworkReportAction(){
		log.info("进入===loadNetworkReportAction");
		try {
			this.areaType = "Sys_Area";
			if(areaId!=null&&!"".equals(areaId)){
				ApplicationEntity areaEntity =structureCommonService.getSectionEntity(areaType, areaId);
				if(areaEntity!=null){
					this.areaName = areaEntity.getValue("name");
					this.repertType = "";
					this.areaId = areaEntity.getValue("id").toString();
				}
			}else{
				//获取登录人ID
				HttpSession session = ServletActionContext.getRequest().getSession();
				String userId = "";
				if(session.getAttribute("userId") != null && !"".equals(session.getAttribute("userId"))){	
					userId = session.getAttribute("userId").toString();
				}
				List<BasicEntity> areaByUserId = null;
				if(userId != null && !userId.equals("")){
					areaByUserId = staffOrganizationService.getAreaByUserId(userId);
				}
				if(areaByUserId != null && areaByUserId.size() > 0){
						this.areaName = areaByUserId.get(0).getValue("name");
						this.areaId = areaByUserId.get(0).getValue("id");
				}
			}
			//List<String> allTypeList = structureCommonService.getAllEntityTypes("networkresourcemanage");
			List<String> allTypeList = new ArrayList<String>();
			allTypeList.add("Sys_Area");
			allTypeList.add("Station");
			allTypeList.add("GeneralBaseStation");
			allTypeList.add("GeneralRoomContainer");
			this.allTypeMap = new HashMap<String, List<String>>();
			String[] aetNameOfAetg = structureCommonService.getAetNameOfAetg("GeneralRoomContainer", "networkresourcemanage");
			String[] aetNameOfAetg2 = structureCommonService.getAetNameOfAetg("GeneralBaseStation", "networkresourcemanage");
			if(aetNameOfAetg != null && aetNameOfAetg.length >0){
				List<String> asList = new ArrayList<String>();
				log.info("开始循环error");
				for (String type : aetNameOfAetg) {
					List<BasicEntity> entry = null;
					Map<String, Object> typeMap = new HashMap<String, Object>();
					try {
						entry = dictionary.getEntry(type + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							typeMap.put("type", type);
							String display = entry.get(0).getValue("display").toString();
							String chinese= GetChineseFirstLetter.getFirstLetter(display);
							typeMap.put("chineseType", display);
							asList.add(type+"$_$"+display);
						}
					} catch (EntryOperationException e) {
						if(entry == null || entry.isEmpty()) {
								typeMap.put("type", type);
								typeMap.put("chineseType", type);
								asList.add(type+"$_$"+type);
						}
					}
				}
				log.info("结束循环error");
				allTypeMap.put("GeneralRoomContainer", asList);
			}
			if(aetNameOfAetg2 != null && aetNameOfAetg2.length >0){
				List<String> asList = new ArrayList<String>();
				log.info("开始循环aetNameOfAetg2");
				for (String type : aetNameOfAetg2) {
					List<BasicEntity> entry = null;
					Map<String, Object> typeMap = new HashMap<String, Object>();
					try {
						entry = dictionary.getEntry(type + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							typeMap.put("type", type);
							String display = entry.get(0).getValue("display").toString();
							String chinese= GetChineseFirstLetter.getFirstLetter(display);
							typeMap.put("chineseType", display);
							asList.add(type+"$_$"+display);
						}
					} catch (EntryOperationException e) {
						if(entry == null || entry.isEmpty()) {
								typeMap.put("type", type);
								typeMap.put("chineseType", type);
								asList.add(type+"$_$"+type);
						}
					}
				}
				log.info("结束循环aetNameOfAetg2");
				allTypeMap.put("GeneralBaseStation", asList);
			}
			this.allTypeMapList = new ArrayList<Map<String,Object>>();
			log.info("开始循环allTypeList");
			for (String type : allTypeList) {
				List<BasicEntity> entry = null;
				Map<String, Object> typeMap = new HashMap<String, Object>();
				try {
					entry = dictionary.getEntry(type + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					if(entry != null && !entry.isEmpty()) {
						typeMap.put("type", type);
						String display = entry.get(0).getValue("display").toString();
						String chinese= GetChineseFirstLetter.getFirstLetter(display);
						typeMap.put("chineseType", display);
						this.allTypeMapList.add(typeMap);
					}
				} catch (EntryOperationException e) {
					if(entry == null || entry.isEmpty()) {
						
						if(type.equals("GeneralRoomContainer")){
							typeMap.put("type", type);
							typeMap.put("chineseType", "机房");
						}else if(type.equals("GeneralBaseStation")){
							typeMap.put("type", type);
							typeMap.put("chineseType", "基站");
						}else{
							typeMap.put("type", type);
							typeMap.put("chineseType", type);
						}
						this.allTypeMapList.add(typeMap);
					}
				}
				quickSort.sort(allTypeMapList, "chineseType");
			}
			log.info("结束循环allTypeList");
			log.info("退出===loadNetworkReportAction 返回值为：success");
			return "success";
		} catch (RuntimeException e) {
			log.error("获取数据出错");
			e.printStackTrace();
			log.info("退出===loadNetworkReportAction 返回值为：error");
			return "error";
		}
	}
	
	//加载指定类型下的数据
	public void loadResourceEntityByAreaAction(){
		log.info("进入===loadResourceEntityByAreaAction");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		ApplicationEntity[] appArrsByRecursionForSrc  = null;
			if(areaId!=null&&!"".equals(areaId)){
				ApplicationEntity areaEntity =structureCommonService.getSectionEntity(this.areaType, areaId);
				ApplicationEntity[] applicationChildEntities = null;
				ApplicationEntity[] applicationLinkEntities = null;
					String[] cTypes = new String[]{this.selectResType};
					applicationLinkEntities = structureCommonService.getAppArrsByRecursion(areaEntity,cTypes,AssociatedType.LINK,"networkresourcemanage");
					applicationChildEntities = structureCommonService.getAppArrsByRecursionForSrc(areaEntity,this.selectResType,"networkresourcemanage");	
				if(applicationChildEntities != null){
					log.info("开始循环applicationChildEntities");
					for(ApplicationEntity entity:applicationChildEntities){
						Map<String,Object> map = new HashMap<String, Object>();
						map = entity.toMap();
						list.add(map);
					}
					log.info("结束循环applicationChildEntities");
				}
				if(applicationLinkEntities != null){
					log.info("开始循环applicationLinkEntities");
					for(ApplicationEntity entity:applicationLinkEntities){
						Map<String,Object> map = new HashMap<String, Object>();
						map = entity.toMap();
						list.add(map);
					}
					log.info("结束循环applicationLinkEntities");
				}
				
			}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		quickSort.sort(list, "name");
		String result = gson.toJson(list);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换出错");
			e.printStackTrace();
		}
		log.info("退出===loadResourceEntityByAreaAction");
	}
	
	//获取指定的类型下的子类型或关联类型或全部
	public void loadChildRoLinkTypeBySelectResTypeAction(){
		log.info("进入===loadChildRoLinkTypeBySelectResTypeAction");
		List<String> list = new ArrayList<String>();
		List<String> childName = new ArrayList<String>();
		List<String> linkName = new ArrayList<String>();
		if("child".equals(this.allLink)){
			String[] associatedAetName = structureCommonService.getAssociatedAetName(selectResType, AssociatedType.CHILD, "networkresourcemanage");
			List<String> asList = new ArrayList<String>();
			if(associatedAetName != null && associatedAetName.length >0){
				log.info("开始循环associatedAetName");
				for(String ser:associatedAetName)
				{
					if(!asList.contains(ser)){
						asList.add(ser);
					}
				}
				log.info("结束循环associatedAetName");
				childName.addAll(asList);
			}
		}else if("link".equals(this.allLink)){
			String[] associatedAetName = structureCommonService.getAssociatedAetName(selectResType, AssociatedType.LINK, "networkresourcemanage");
			List<String> asList = new ArrayList<String>();
			if(associatedAetName != null && associatedAetName.length >0){
				log.info("开始循环associatedAetName");
				for(String ser:associatedAetName)
				{
					if(!asList.contains(ser)){
						asList.add(ser);
					}
				}
				log.info("结束循环associatedAetName");
				linkName.addAll(asList);
			}
		}else{
			String[] childNames = structureCommonService.getAetNameOfAetg("StatisScope_4_"+selectResType, "networkresourcemanage");
			if(childNames != null && childNames.length > 0){
			List<String> asList1 = Arrays.asList(childNames);
			childName.addAll(asList1);
			}
//			String[] childNames = structureCommonService.getAssociatedAetName(selectResType, AssociatedType.CHILD, "networkresourcemanage");
//			if(childNames != null && childNames.length > 0){
//				List<String> asList1 = Arrays.asList(childNames);
//				childName.addAll(asList1);
//				for(String se:childNames){
//					String[] associatedAetName = structureCommonService.getAssociatedAetName(se, AssociatedType.CHILD, "networkresourcemanage");
//					if(associatedAetName != null && associatedAetName.length >0){
//						List<String> asList = Arrays.asList(associatedAetName);
//						childName.addAll(asList);
//					}
//				}
//			}
		}
		this.allTypeMapList = new ArrayList<Map<String,Object>>();
		if(childName != null){
			log.info("开始循环childName");
			for(String st:childName){
				List<BasicEntity> entry = null;
				Map<String, Object> typeMap = new HashMap<String, Object>();
				try {
					entry = dictionary.getEntry(st + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					if(entry != null && !entry.isEmpty()) {
						typeMap.put("type", st);
						typeMap.put("associatedType", "child");
						typeMap.put("chineseType", entry.get(0).getValue("display"));
						this.allTypeMapList.add(typeMap);
					}
				} catch (EntryOperationException e) {
					log.error("获取数据字典出错");
					e.printStackTrace();
					if(entry != null && !entry.isEmpty()) {
						typeMap.put("type", st);
						typeMap.put("associatedType", "child");
						typeMap.put("chineseType", st);
						this.allTypeMapList.add(typeMap);
					}
				}
			}
			log.info("结束循环childName");
		}
		if(linkName != null){
			log.info("开始循环linkName");
			for(String st:linkName){
				List<BasicEntity> entry = null;
				Map<String, Object> typeMap = new HashMap<String, Object>();
				try {
					entry = dictionary.getEntry(st + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					if(entry != null && !entry.isEmpty()) {
						typeMap.put("type", st);
						typeMap.put("associatedType", "link");
						typeMap.put("chineseType", entry.get(0).getValue("display"));
						this.allTypeMapList.add(typeMap);
					}
				} catch (EntryOperationException e) {
					log.error("获取数据字典出错");
					e.printStackTrace();
					if(entry != null && !entry.isEmpty()) {
						typeMap.put("type", st);
						typeMap.put("associatedType", "link");
						typeMap.put("chineseType", st);
						this.allTypeMapList.add(typeMap);
					}
				}
			}
			log.info("结束循环linkName");
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(allTypeMapList);
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("json转换出错");
			e.printStackTrace();
		}
		log.info("退出===loadChildRoLinkTypeBySelectResTypeAction");
	}
	
	//定类型数量Action
	public void statisticsResourceTotalAction(){
		log.info("进入===statisticsResourceTotalAction");
		if(this.repertType !=null && !this.repertType.equals("")){
			String[] Ids = this.entityId.split(",");
			String[] selectResTypes = null;
			if(this.repertType.equals("Outdoor")){
				selectResTypes = new String[]{"Station","ManWell","Pole","HangWall","FiberCrossCabinet"};
			}else if(this.repertType.equals("BaseStation")){
				selectResTypes = new String[]{"BaseStation","BaseStation_GSM","BaseStation_repeater","BaseStation_TD","BaseStation_WLAN"};
			}else if(this.repertType.equals("Cell")){
				selectResTypes = new String[]{"Tower","Cell"};
			}else if(this.repertType.equals("Power")){
				selectResTypes = new String[]{"DCBattery","DCRectifyFrame","OilEngine","AirConditioning"};
			}
			String[] currentTypes =  this.currentType.split(",");
			Map<String, Object> map = new HashMap<String, Object>();
			for(String selectType:selectResTypes){
				String countString = "";
				String rType = selectType;
					String[] cTypes = new String[]{rType};
					int i = 0;
					for(String sId:Ids){
						int count = 0;
						ApplicationEntity app = structureCommonService.getSectionEntity(currentTypes[i], sId);
						ApplicationEntity[] appArrsByRecursion = null;
						ApplicationEntity[] appArrsByRecursions = null;
						appArrsByRecursion = structureCommonService.getAppArrsByRecursionForSrcSameType(app,rType,"networkresourcemanage");
						appArrsByRecursions = structureCommonService.getAppArrsByRecursion(app,cTypes,AssociatedType.LINK,"networkresourcemanage");
						if(appArrsByRecursion != null){
							count = appArrsByRecursion.length + count;
						}if(appArrsByRecursions != null){
							count = appArrsByRecursions.length + count;
						}
						countString = countString+ count +",";
						
					}
					i++;
				
				
				
				List<BasicEntity> entry = null;
				String mapKey = "";
				try {
					entry = dictionary.getEntry(rType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					if(entry != null && !entry.isEmpty()) {
						mapKey = entry.get(0).getValue("display").toString();
					}
				} catch (EntryOperationException e) {
					e.printStackTrace();
					if(entry != null && !entry.isEmpty()) {
						mapKey = rType;
					}
				}
				map.put(mapKey, countString);
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
				e.printStackTrace();
			}

		}else{
			
			String[] Ids = this.entityId.split(",");
			String[] selectResTypes = this.selectResType.split("&_&");
			String[] currentTypes =  this.currentType.split(",");
			Map<String, Object> map = new HashMap<String, Object>();
			for(String selectType:selectResTypes){
				String countString = "";
				String[] split = selectType.split(",");
				String rType = split[0].toString();
				String rAss = split[1].toString();
				AssociatedType ass = null;
				
				if(!"allLink".equals(this.allLink)){
					String[] cTypes = new String[]{rType};
					int i = 0;
					for(String sId:Ids){
						int count = 0;
						ApplicationEntity app = structureCommonService.getSectionEntity(currentTypes[i], sId);
						ApplicationEntity[] appArrsByRecursion = null;
						if("child".equals(rAss)){
							ass = AssociatedType.CHILD;
						}else if("link".equals(rAss)){
							ass = AssociatedType.LINK;
						}
						appArrsByRecursion = structureCommonService.getAppArrsByRecursion(app,cTypes,ass,"networkresourcemanage");
						if(appArrsByRecursion != null){
							count = appArrsByRecursion.length;
						}else{
							count = 0;
						}
						countString = countString+ count +",";
						i++;
					}
				}else{
					String[] cTypes = new String[]{rType};
					int i = 0;
					for(String sId:Ids){
						int count = 0;
						ApplicationEntity app = structureCommonService.getSectionEntity(currentTypes[i], sId);
						ApplicationEntity[] appArrsByRecursion = null;
						ApplicationEntity[] appArrsByRecursions = null;
						appArrsByRecursion = structureCommonService.getAppArrsByRecursionForSrcSameType(app,rType,"networkresourcemanage");
						appArrsByRecursions = structureCommonService.getAppArrsByRecursion(app,cTypes,AssociatedType.LINK,"networkresourcemanage");
						if(appArrsByRecursion != null){
							count = appArrsByRecursion.length + count;
						}if(appArrsByRecursions != null){
							count = appArrsByRecursions.length + count;
						}
						countString = countString+ count +",";
						
					}
					i++;
				}
				
				
				
				List<BasicEntity> entry = null;
				String mapKey = "";
				try {
					entry = dictionary.getEntry(rType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					if(entry != null && !entry.isEmpty()) {
						mapKey = entry.get(0).getValue("display").toString();
					}
				} catch (EntryOperationException e) {
					e.printStackTrace();
					if(entry != null && !entry.isEmpty()) {
						mapKey = rType;
					}
				}
				map.put(mapKey, countString);
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
				e.printStackTrace();
			}
		}
	}
	
	//下载报表
	public String downLoadResourceTotalAction(){
		log.info("进入===downLoadResourceTotalAction");
		String[] Ids = this.entityId.split(",");
		String[] selectResTypes = this.selectResType.split("&_&");
		String[] currentTypes =  this.currentType.split(",");
		Map<String,Object> linkedHashMap = new LinkedHashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> titleList = new ArrayList<String>();
		List<List> dataList = new ArrayList<List>();
		String title = " ";
		if(this.tableTitle != null && !this.tableTitle.equals("")){
			try {
				title = new String(this.tableTitle.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		titleList.add(title);
		log.info("开始循环selectResTypes");
		for(String selectType:selectResTypes){
			List<String> countString = new ArrayList<String>();
			String[] split = selectType.split(",");
			String rType = split[0].toString();
			String rAss = split[1].toString();
			List<BasicEntity> entry = null;
			String mapKey = "";
			try {
				entry = dictionary.getEntry(rType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
				if(entry != null && !entry.isEmpty()) {
					mapKey = entry.get(0).getValue("display").toString();
				}
			} catch (EntryOperationException e) {
				e.printStackTrace();
				if(entry != null && !entry.isEmpty()) {
					mapKey = rType;
				}
			}
			countString.add(mapKey);
			
			AssociatedType ass = null;
			
			if(!"allLink".equals(this.allLink)){
				String[] cTypes = new String[]{rType};
				int i = 0;
				for(String sId:Ids){
					int count = 0;
					ApplicationEntity app = structureCommonService.getSectionEntity(currentTypes[i], sId);
					if(app.getValue("name") != null){
						linkedHashMap.put(app.getValue("name").toString(), null);
					}else{
						linkedHashMap.put(app.getValue("label").toString(), null);
					}
					ApplicationEntity[] appArrsByRecursion = null;
					if("child".equals(rAss)){
						ass = AssociatedType.CHILD;
					}else if("link".equals(rAss)){
						ass = AssociatedType.LINK;
					}
					//111
					ApplicationEntity[] appArrsByRecursions = null;
					appArrsByRecursion = structureCommonService.getAppArrsByRecursionForSrcSameType(app,rType,"networkresourcemanage");
					appArrsByRecursions = structureCommonService.getAppArrsByRecursion(app,cTypes,AssociatedType.LINK,"networkresourcemanage");
				if(appArrsByRecursion != null){
					count = appArrsByRecursion.length + count;
				}if(appArrsByRecursions != null){
					count = appArrsByRecursions.length + count;
				}
					countString.add(count+"");
					i++;
				
				}
			}else{
				String[] cTypes = new String[]{rType};
				int i = 0;
				for(String sId:Ids){
					int count = 0;
					ApplicationEntity app = structureCommonService.getSectionEntity(currentTypes[i], sId);
					if(app.getValue("name") != null){
						linkedHashMap.put(app.getValue("name").toString(), null);
					}else{
						linkedHashMap.put(app.getValue("label").toString(), null);
					}
					ApplicationEntity[] appArrsByRecursion = null;
					ApplicationEntity[] appArrsByRecursions = null;
					appArrsByRecursion = structureCommonService.getAppArrsByRecursionForSrcSameType(app,rType,"networkresourcemanage");
					appArrsByRecursions = structureCommonService.getAppArrsByRecursion(app,cTypes,AssociatedType.LINK,"networkresourcemanage");
				if(appArrsByRecursion != null){
					count = appArrsByRecursion.length + count;
				}if(appArrsByRecursions != null){
					count = appArrsByRecursions.length + count;
				}
					countString.add(count+"");
				
				}
				i++;
				}
			dataList.add(countString);
		}
		log.info("结束循环selectResTypes");
		log.info("开始循环linkedHashMap");
		for(String ke:linkedHashMap.keySet()){
			titleList.add(ke);
		}
		log.info("结束循环linkedHashMap");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
					//导出excel数据
					resultInputStream = creatExcel2003InputStream(titleList, dataList);
					try {
						chaxunFileName = sdf.format(new Date()) + " "
								+ new String("资源统计导出表".getBytes("GBK"), "ISO-8859-1");
					} catch (UnsupportedEncodingException e) {
						log.error("导出excel数据出错");
						chaxunFileName = "resource";
						e.printStackTrace();
					}
					log.info("退出===downLoadResourceTotalAction 返回值为：success");
					return "success";
	}
	
	//获取指定资源下指定类型的资源Action
	public void getResourceEntityListAction(){
		log.info("进入===getResourceEntityListAction方法");
		ApplicationEntity app = structureCommonService.getSectionEntity(this.currentType, this.entityId);
		ApplicationModule application = ModuleProvider.getModule(this.selectResType);
		AssociatedType ass = null;
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> chineseList = new ArrayList<Map<String,Object>>();
		Map<String,Object> orderIdMap = new HashMap<String,Object>();
		String[] cTypes = new String[]{this.selectResType};
		if(application != null){
			Map<String, Object> infoMap = application.toMap();
			//获取中文标题(不能正常获取的继续显示英文)(这里用linkedHashMap，使其与模板顺序一致，确保跟内容相对应)
			Map<String, Object> titleMap = new LinkedHashMap<String, Object>();
			log.info("开始循环infoMap");
			for(String key : infoMap.keySet()) {
				try {
					List<BasicEntity> entry = null;
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						//数据字典，中英文转换
						entry = dictionary.getEntry(key + "," + this.selectResType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					}
					if(entry != null && !entry.isEmpty()) {
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							titleMap.put(key, entry.get(0).getValue("display"));
							orderIdMap.put( entry.get(0).getValue("orderID").toString(), key);
						}
					}
				} catch (EntryOperationException e) {
					log.error("获取数据字典出错");
					e.printStackTrace();
					titleMap.put(key, key);
				}
			}
			log.info("结束循环infoMap");
			ApplicationEntity[] appArrsByRecursion = null;
			ApplicationEntity[] appArrsByRecursions = null;
			if("child".equals(allLink)){
				ass = AssociatedType.CHILD;
				appArrsByRecursion = structureCommonService.getAppArrsByRecursionForSrcSameType(app,selectResType,"networkresourcemanage");
				appArrsByRecursions = structureCommonService.getAppArrsByRecursion(app,cTypes,AssociatedType.LINK,"networkresourcemanage");
			}else if("link".equals(allLink)){
				ass = AssociatedType.LINK;
				appArrsByRecursion = structureCommonService.getAppArrsByRecursion(app,cTypes,ass,"networkresourcemanage");
			}
			if(appArrsByRecursion != null){
				log.info("开始循环appArrsByRecursion");
				for(ApplicationEntity ac:appArrsByRecursion){
					//将null值转换为空串值
					Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(ac);
					resMap = ResourceCommon.mapRecombinationToString(resMap);
					//生成被查询资源的map(已进行关联资源的过滤)
					Map<String,Object> sortedMap = quickSort.sortMap(resMap,orderIdMap);//排序
					if(sortedMap!=null){
						resMap=sortedMap;
					}
					log.info("开始循环resMap");
					for(String key:resMap.keySet()){
						Object object = resMap.get(key);
						String encode = "";
						if(object.getClass().toString().indexOf("java.util.Date") >= 0){
							if(object != null && !object.equals("")){
								encode = object.toString();
								SimpleDateFormat spdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",java.util.Locale.ENGLISH);
								SimpleDateFormat spdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								Date parse = null;
								try {
									parse = spdf1.parse(encode);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if(parse != null){
									encode = spdf.format(parse);		
								}
							}
						}else{
							encode = encode(object.toString());
						}
						if(key.equals("maintainAttention")){
							//System.out.println(encode+"==========");
						}
//						if("address".equals(key)){
//							if(resMap.get(key).toString().indexOf("海珠区滨江东路935号汇美景台A栋负一") >= 0){		
////								System.out.println("======"+resMap.get(key));
////								System.out.println("======"+encode);
//								encodetext(encode);
//								System.out.println("======"+encode);
//								encode = "";
//							}
//						}
						resMap.put(key, encode);
					}
					log.info("结束循环resMap");
					list.add(resMap);
				}
				log.info("结束循环appArrsByRecursion");
			}
			if(appArrsByRecursions != null){
				log.info("开始循环appArrsByRecursions");
				for(ApplicationEntity ac:appArrsByRecursions){
					//将null值转换为空串值
					Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(ac);
					resMap = ResourceCommon.mapRecombinationToString(resMap);
					//生成被查询资源的map(已进行关联资源的过滤)
					Map<String,Object> sortedMap = quickSort.sortMap(resMap,orderIdMap);//排序
					if(sortedMap!=null){
						resMap=sortedMap;
					}
					for(String key:resMap.keySet()){
						Object object = resMap.get(key);
						String encode = encode(object.toString());
						resMap.put(key, encode);
					}
					list.add(resMap);
				}
				log.info("结束循环appArrsByRecursions");
			}
			Map<String, Object> infoMapChineseMap = quickSort.sortMap(titleMap,orderIdMap);//中文字典排序
			chineseList.add(infoMapChineseMap);
		}
		Map<String, List<Map<String,Object>>> map = new HashMap<String, List<Map<String,Object>>>();
		map.put("entityList", list);
		map.put("chineseList", chineseList);
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
		log.info("退出===getResourceEntityListAction方法");
	}
	
	public static String encode(String s) {
		log.info("进入===encode方法");
		  if (s == null)
		   return null;
		  StringBuffer sb = new StringBuffer();
		  int n = s.length();
		  for (int i = 0; i < n; i++) {
		   char c = s.charAt(i);
		   switch (c) {
		   case ' ':
			   sb.append("");
		    break;
		   case '“':
		    sb.append("");
		    break;
		   case '<':
			   sb.append("");
		    break; // 为防止script攻击，转为全角
		   case '>':
			   sb.append("");
		    break;
		   case '&':
			   sb.append("");
		    break;
		   case '"':
			   sb.append("“");
		    break;
		   case '(':
			   sb.append("（");
		    break;
		   case ')':
			   sb.append("）");
			    break;
		   case '[':
			   sb.append("【");
			    break;
		   case ']':
			   sb.append("】");
			    break;
		   case '（':
			   sb.append("（");
			    break;
		   case '）':
			   sb.append("）");
			    break;
		   case ',':
			   sb.append("");
			    break;
		   case '。':
			   sb.append("");
			    break;
		   case '，':
			   sb.append("");
			    break;
		   case ':':
			   sb.append("：");
			    break;
		   case ';':
			   sb.append("；");
			    break;
		   // 下面处理其它特殊字符，可省略
		    case 'à': sb.append("");break;
		    //.....
		   default:
		    sb.append(c);
		   }
		  }
		  String sbS = sb.toString();
		  char[] chars = new char[]{10,13,32};
		  for(int i = 0;chars.length > i;i++){
			  sbS = sbS.replaceAll(chars[i]+"", "");
		  }
//		  if(sbS != null && sbS.indexOf("广汕路任命武装警察学校内") > -1){
//			  for(int i = 0;sbS.length() > i;i++){
//				  char charAt = sbS.charAt(i);
//				  Integer valueOf = Integer.valueOf(charAt);
//				  System.out.println(charAt+"===="+valueOf);
//			  }
//		  }
		  log.info("退出===encode方法 返回值为："+sbS);
		  return sbS;
		 }
	
	
	/**
	 * 
	 * @param biaotou
	 *            表头数据
	 * @param data
	 *            内容
	 * @return ByteArrayInputStream 可以处理的数据类型有int
	 *         ,Integer,Date，double，Double，Float，float，Calendar，RichTextString
	 */
	public static ByteArrayInputStream creatExcel2003InputStream(List biaotou,
			List<List> data) {
		log.info("进入===creatExcel2003InputStream方法");
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;

		ByteArrayOutputStream out = null;
		try {
			HSSFRow row = null;
			HSSFCell cell = null;
			wb = new HSSFWorkbook();
			//wb.setSheetName(1, "table1");
			sheet = (HSSFSheet) wb.createSheet();
			
			row = sheet.createRow(0);
			for (int i = 0; i < biaotou.size(); i++) {
				// 建立新cell（单元格）对象
				cell = row.createCell(i);
				String value = "";
				if(biaotou.get(i) !=null){
					value = (String) biaotou.get(i);
				}
				cell.setCellValue(value);
			}
			List object = null;

			for (int i = 0; i < data.size(); i++) {
				// 建立新row（行）对象
				row = sheet.createRow(i + 1);
				object = data.get(i);
				for (int j = 0; j < object.size(); j++) {
					// 一行的内容
					cell = row.createCell(j);
					Object cellValue = object.get(j);
					//类型转换
					if(cellValue == null || "null".equals(cellValue) || "".equals(cellValue)) {
						//数据中get出来是空值的情况，excel中导出空串值
						cell.setCellValue("");
					} else {
						//有正常数据的情况
						if(cellValue.getClass().getName().indexOf("Integer") > -1) {
							int value = Integer.parseInt(cellValue.toString());
							cell.setCellValue(value);
						} else if(cellValue.getClass().getName().indexOf("Double") > -1) {
							double value = Double.parseDouble(cellValue.toString());
							cell.setCellValue(value);
						} else if(cellValue.getClass().getName().indexOf("Float") > -1) {
							float value = Float.parseFloat(cellValue.toString());
							cell.setCellValue(value);
						} else if(cellValue.getClass().getName().indexOf("Boolean") > -1) {
							boolean booleanValue = Boolean.parseBoolean(cellValue.toString());
							String value = booleanValue ? "是" : "否";
							cell.setCellValue(value);
						} else if(cellValue.getClass().getName().indexOf("Date") > -1) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
							String value = sdf.format(cellValue);
							cell.setCellValue(value);
						} else {
							String value = cellValue + "";
							cell.setCellValue(value);
						}
					}
				}
			}

			out = new ByteArrayOutputStream();
			wb.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
		log.info("退出===creatExcel2003InputStream方法 返回值为：byteArrayInputStream");
		return byteArrayInputStream;
	}

	
	//下载详细报表
	public String downLoadResourceTotalInformationReportAction(){
		log.info("进入===downLoadResourceTotalInformationReportAction方法");
		ApplicationEntity app = structureCommonService.getSectionEntity(this.currentType, this.entityId);
		ApplicationModule application = ModuleProvider.getModule(this.selectResType);
		AssociatedType ass = null;
		List<List> eList = new ArrayList<List>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> chineseList = new ArrayList<Map<String,Object>>();
		List turnMapTo = new ArrayList();
		Map<String,Object> orderIdMap = new HashMap<String,Object>();
		String[] cTypes = new String[]{this.selectResType};
		if(application != null){
			Map<String, Object> infoMap = application.toMap();
			//获取中文标题(不能正常获取的继续显示英文)(这里用linkedHashMap，使其与模板顺序一致，确保跟内容相对应)
			Map<String, Object> titleMap = new LinkedHashMap<String, Object>();
			log.info("开始循环infoMap");
			for(String key : infoMap.keySet()) {
				try {
					List<BasicEntity> entry = null;
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						//数据字典，中英文转换
						entry = dictionary.getEntry(key + "," + this.selectResType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					}
					if(entry != null && !entry.isEmpty()) {
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							titleMap.put(key, entry.get(0).getValue("display"));
							orderIdMap.put( entry.get(0).getValue("orderID").toString(), key);
						}
					}
				} catch (EntryOperationException e) {
					log.error("获取数据字典出错 key:"+key);
					e.printStackTrace();
					titleMap.put(key, key);
				}
			}
			log.info("结束循环infoMap");
			ApplicationEntity[] appArrsByRecursion = null;
			ApplicationEntity[] appArrsByRecursions = null;
			if("child".equals(allLink)){
				ass = AssociatedType.CHILD;
				appArrsByRecursion = structureCommonService.getAppArrsByRecursionForSrcSameType(app,selectResType,"networkresourcemanage");
				
				appArrsByRecursions = structureCommonService.getAppArrsByRecursion(app,cTypes,AssociatedType.LINK,"networkresourcemanage");
			}else if("link".equals(allLink)){
				ass = AssociatedType.LINK;
				appArrsByRecursion = structureCommonService.getAppArrsByRecursion(app,cTypes,ass,"networkresourcemanage");
			}
			if(appArrsByRecursion != null){
				log.info("开始循环appArrsByRecursion");
				for(ApplicationEntity ac:appArrsByRecursion){
					//将null值转换为空串值
					Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(ac);
					resMap = ResourceCommon.mapRecombinationToString(resMap);
					//生成被查询资源的map(已进行关联资源的过滤)
					Map<String,Object> sortedMap = quickSort.sortMap(resMap,orderIdMap);//排序
					if(sortedMap!=null){
						resMap=sortedMap;
					}
					log.info("开始循环resMap");
					for(String key:resMap.keySet()){
						Object object = resMap.get(key);
						String encode = encode(object.toString());
//						if("address".equals(key)){
//							if(resMap.get(key).toString().indexOf("海珠区滨江东路935号汇美景台A栋负一") >= 0){		
////								System.out.println("======"+resMap.get(key));
////								System.out.println("======"+encode);
//								encodetext(encode);
//								System.out.println("======"+encode);
//								encode = "";
//							}
//						}
						resMap.put(key, encode);
					}
					log.info("结束循环resMap");
					list.add(resMap);
				}
				log.info("结束循环appArrsByRecursion");
			}
			if(appArrsByRecursions != null){
				log.info("开始循环appArrsByRecursions");
				for(ApplicationEntity ac:appArrsByRecursions){
					//将null值转换为空串值
					Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(ac);
					resMap = ResourceCommon.mapRecombinationToString(resMap);
					//生成被查询资源的map(已进行关联资源的过滤)
					Map<String,Object> sortedMap = quickSort.sortMap(resMap,orderIdMap);//排序
					if(sortedMap!=null){
						resMap=sortedMap;
					}
					for(String key:resMap.keySet()){
						Object object = resMap.get(key);
						String encode = encode(object.toString());
						resMap.put(key, encode);
					}
					list.add(resMap);
				}
				log.info("结束循环appArrsByRecursions");
			}
			
			if(list != null && list.size() > 0){
				log.info("开始循环list");
				for(Map m:list){
					if("equal".equals(this.conditions)){
						if(this.columnValue.equals(m.get(this.column))){
							eList.add(turnMapToList(m));
						}
					}else if("notequal".equals(this.conditions)){
						if(!this.columnValue.equals(m.get(this.column))){
							eList.add(turnMapToList(m));
						}
					}else if("LessThan".equals(this.conditions)){
						try {
							int parseInt = Integer.parseInt(this.columnValue);
							if(m.get(this.column) != null && !"".equals(m.get(this.column))){
								int parseInt1 = Integer.parseInt(m.get(this.column).toString());
								if(parseInt1 < parseInt){
									eList.add(turnMapToList(m));
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}else if("greaterThan".equals(this.conditions)){
						try {
							int parseInt = Integer.parseInt(this.columnValue);
							if(m.get(this.column) != null && !"".equals(m.get(this.column))){
								int parseInt1 = Integer.parseInt(m.get(this.column).toString());
								if(parseInt1 > parseInt){
									eList.add(turnMapToList(m));
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}else if("Contain".equals(this.conditions)){
						if(m.get(this.column) != null && !"".equals(m.get(this.column))){
							if(m.get(this.column).toString().indexOf(this.columnValue) >= 0){
								eList.add(turnMapToList(m));
							}
						}
					}else if("NoContain".equals(this.conditions)){
						if(m.get(this.column) != null && !"".equals(m.get(this.column))){
							if(m.get(this.column).toString().indexOf(this.columnValue) < 0){
								eList.add(turnMapToList(m));
							}
						}
					}else{
						eList.add(turnMapToList(m));
					}
				}
				log.info("结束循环list");
			}
			Map<String, Object> infoMapChineseMap = quickSort.sortMap(titleMap,orderIdMap);//中文字典排序
			Map<String,Object> titleMap1 = quickSort.sortMap(titleMap,orderIdMap);
			turnMapTo = turnMapToList(titleMap1);
		}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			//导出excel数据
			resultInputStream = creatExcel2003InputStream(turnMapTo, eList);
			try {
				chaxunFileName = sdf.format(new Date()) + " "
						+ new String("资源统计导出表".getBytes("GBK"), "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				chaxunFileName = "resource";
				e.printStackTrace();
			}
			log.info("退出===downLoadResourceTotalInformationReportAction方法 返回值为：success");
			return "success";
	}
	
	private List turnMapToList(Map<String,Object> map){
		log.info("进入===turnMapToList方法");
		List<String> list = new ArrayList<String>();
		if(map != null){
			log.info("开始循环map");
			for(String o:map.keySet()){
				if(!"_entityType".equals(o)){
					list.add(map.get(o).toString());
				}
			}
			log.info("结束循环map");
		}
		log.info("进入===turnMapToList方法 返回值为："+list);
		return list;
	}



	public StaffOrganizationService getStaffOrganizationService() {
		return staffOrganizationService;
	}



	public void setStaffOrganizationService(
			StaffOrganizationService staffOrganizationService) {
		this.staffOrganizationService = staffOrganizationService;
	}
}
