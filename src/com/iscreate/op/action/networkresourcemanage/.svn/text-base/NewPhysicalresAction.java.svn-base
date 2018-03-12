package com.iscreate.op.action.networkresourcemanage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.iscreate.op.constant.OperationType;
import com.iscreate.op.pojo.maintain.ResourceMaintenance;
import com.iscreate.op.pojo.maintain.ServiceMaintenance;
import com.iscreate.op.service.maintain.NetworkResourceMaintenanceService;
import com.iscreate.op.service.networkresourcemanage.PhysicalresService;
import com.iscreate.op.service.networkresourcemanage.StaffOrganizationService;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
import com.iscreate.plat.networkresource.application.tool.XMLAEMLibrary;
import com.iscreate.plat.networkresource.common.action.ActionHelper;
import com.iscreate.plat.networkresource.common.dao.NetworkResourceManageDao;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.QuickSort;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.plat.networkresource.dictionary.SearchScope;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;

public class NewPhysicalresAction {
	private static final Log log = LogFactory.getLog(NewPhysicalresAction.class);
	private PhysicalresService physicalresService;
	private StructureCommonService structureCommonService;
	private XMLAEMLibrary moduleLibrary;
	private Dictionary dictionary;
	private StaffOrganizationService staffOrganizationService;
	private Map<String,Object> searchMap;
	private Map<String,Object> currentEntityChineseMap;
	private Map<String,Object> dropdownListMap;
	private Map<String,Object> attrTypeMap;
	private String selectResType;
	private QuickSort<Map<String,Object>> quickSort;//排序
	private String dateString;
	private String name;
	private String label;
	private String sType;
	private String areaId;
	private String areaType;
	private String assName;
	private List<Map<String,Object>> searchResourceMapList;
	
	private String orderType;
	private String currentEntityId;
	private String currentEntityType;
	private String chineseName;
	/*分页处理属性*/
	private String pageIndex;
	private String conditionString;
	private String pageSize;
	
	
	//导出文件的输入流
	private InputStream resultInputStream;
	//文件名
	private String chaxunFileName;
	
	//资源添加
	private Map<String,Object> addedResMap;
	private Map<String,Object> nullableMap;
	private Map<String,Object> attrLengthMap;
	private String parentEntityTypeGrp;
	private String addedResEntityType;
	private String addedResParentEntityType;
	private long addedResParentEntityId;
	
	private NetworkResourceMaintenanceService networkResourceMaintenanceService;
	
	
	private String areaName;
	
	private String bizModule;

	private String currentEntityLabel;
	private Map<String,Object> currentEntityMap;
	private String parentEntityType;
	private String parentEntityId;
	private Map<String,Object> parentEntityMap;
	
	
	private String updatedEntityType;
	private String bizProcessCode;
	private String bizProcessId;
	private String newParentResEntityId;
	private String newParentResEntityType;
	private String oldParentResEntityId;
	private String oldParentResEntityType;
	private List<Map<String,Object>> parentMapList;
	
	private String forwardType;
	
	private String cityAreaId;//市区域id
	private String districtAreaId;//区/县区域id
	private String streetAreaId;//街道区域id
	
	/**
	 * 
	 * @description: 获取查询的资源属性字段信息
	 * @author：     
	 * @return void     
	 * @date：Feb 20, 2013 11:47:01 AM
	 */
	public String getSearchAttributesAction(){
		log.info("进入getSearchAttributesAction()，获取查询的资源属性字段信息");
		try {
			ApplicationModule module = ModuleProvider.getModule(this.selectResType);
			Map<String,Object> keyMap = module.toMap();
			this.currentEntityChineseMap = new HashMap<String, Object>();
			//生成下拉框的map
			this.dropdownListMap = new HashMap<String, Object>();
			this.attrTypeMap = new HashMap<String,Object>();
			Map<String,Object> orderIdMap =new HashMap<String,Object>();//entity属性排序
			this.searchMap = new HashMap<String,Object>();
			for(String key : keyMap.keySet()) {
				try {
					List<BasicEntity> entry = null;
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						//数据字典，中英文转换
						entry = dictionary.getEntry(key + "," + this.selectResType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					}
					if(entry != null && !entry.isEmpty()) {
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							String type = module.getAttribute(key).getValue("type")+"";
							String orderId = entry.get(0).getValue("orderID")+"";
							long oId=0L;
							if(orderId!=null &&!"".equals(orderId)&&!"null".equals(orderId)){
								oId=Long.valueOf(orderId);
							}
							if(type.indexOf("java.util.Date")>=0 || (oId<=7&&oId>=4) ){
								if(this.searchMap.size()>=8){
									break;
								}
								this.searchMap.put(key, keyMap.get(key));
								this.currentEntityChineseMap.put(key, entry.get(0).getValue("display"));
								orderIdMap.put(entry.get(0).getValue("orderID")+"",key);
								this.attrTypeMap.put(key,module.getAttribute(key).getValue("type"));
								//根据数据字典中，属性为entity类型或者general类型，获取属性对应的下拉框的值，提供前台进行下拉框选择操作
								//拿出来的值，例如:其他-其他,置换-置换,局用-局用
								if("entity".equals(entry.get(0).getValue("dictionaryType"))) {
									//entity类型下拉框数据组装
									List<BasicEntity> dropDownEntry = dictionary.getEntry(key + "," + this.selectResType + ",networkResourceValueEmun", SearchScope.OBJECT, "");
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
											this.dropdownListMap.put(key, attrDropdownList);
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
											this.dropdownListMap.put(key, attrDropdownList);
										}
									}
								}
							}
						}
					}
					
				} catch (EntryOperationException e) {
					log.error("获取"+selectResType+"属性字段"+key+"的中文字典失败，可能该字典不存在。");
					e.printStackTrace();
				}
				
			}
			//addedResMap排序
			Map<String,Object> sortedMap = quickSort.sortMap(this.searchMap, orderIdMap);
			if(sortedMap!=null){
				this.searchMap = sortedMap;
				//System.out.println(this.addedResMap+"*****");
			}
			log.info("退出getSearchAttributesAction()，返回结果result=success");
			return "success";
		} catch (RuntimeException e) {
			log.error("退出getSearchAttributesAction()，发生RuntimeException异常，返回结果result=error");
			e.printStackTrace();
			return "error";
		}
	}
	/**
	 * 
	 * @description: 获取查询资源类型
	 * @author：     
	 * @return void     
	 * @date：Feb 20, 2013 3:37:08 PM
	 */
	public void getSearchTypeAction(){
		log.info("进入getSearchTypeAction()，获取查询资源类型");
		String result="";
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Map<String,Object> aetgMap = new LinkedHashMap<String,Object>();
		aetgMap.put("Search_Facilities", "点设施资源");
		aetgMap.put("Search_Wireless", "无线资源");
		aetgMap.put("Search_Transmission", "传输资源");
		aetgMap.put("Search_Power", "动力资源");
		aetgMap.put("Search_Wlan", "Wlan资源");
		aetgMap.put("Search_EvironmentAndMonitoring", "环境监控资源");
		aetgMap.put("Search_GroupUserSite", "集客类资源");
		String[] aetgs = {"Search_Facilities","Search_Wireless","Search_Transmission","Search_Power","Search_Wlan","Search_EvironmentAndMonitoring","Search_GroupUserSite"};
		for(String aetgName:aetgs){
			String[] aetNames=structureCommonService.getAetNameOfAetg(aetgName,"networkresourcemanage");
			Map<String,Object> rMap = new HashMap<String,Object>();
			if(aetNames!=null&&!"".equals(aetNames)){
				for(String aetName:aetNames){
					List<BasicEntity> entry = null;
					try {
						entry = dictionary.getEntry(aetName + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							String display = entry.get(0).getValue("display").toString();
					        rMap.put(aetName,display);
						}
					} catch (EntryOperationException e) {
						log.error("获取"+aetName+"中文字典失败，可能该字典不存在");
						e.printStackTrace();
						rMap.put(aetName,aetName);
					}
				}
			}
			resultMap.put(aetgName, rMap);
		}
		Map<String,Object> rMap = new HashMap<String,Object>();
		rMap.put("aetgs", aetgMap);
		rMap.put("aets", resultMap);
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		
	    result = gson.toJson(rMap);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出getSearchTypeAction(),返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getSearchTypeAction(),返回结果result="+result+"失败");
			e.printStackTrace();
		}

	}
	/**
	 * 
	 * @description: 资源查询方法
	 * @author：yuan.yw     
	 * @return void     
	 * @date：Jul 2, 2013 4:44:56 PM
	 * @updatedate 2013-7-2
	 * @updatereason 整改网络资源
	 */
	public void searchResourceByPageNewAction(){
		log.info("进入searchResourceByPageNewAction()，查询资源分页");
		try{
			int totalSize = 0;
			int totalPage = 0;
			//获取登录人ID
			HttpSession session = ServletActionContext.getRequest().getSession();
			String userId = "";
			List<BasicEntity> areaByUserId = null;//用户所属区域
			String areaConditionSql = "";//区域查询条件sql
			if(session.getAttribute("userId") != null && !"".equals(session.getAttribute("userId"))){	
				userId = session.getAttribute("userId").toString();
				areaByUserId = this.staffOrganizationService.getAreaByUserId(userId);//查询用户所属区域
				if(areaByUserId==null || areaByUserId.isEmpty()){//用户所属区域id为空 不搜索
					Map<String,Object> searchBigMap = new HashMap<String, Object>();
					searchBigMap.put("totalPage", totalPage);
					searchBigMap.put("totalSize", totalSize);
					HttpServletResponse response = ServletActionContext.getResponse();
					response.setCharacterEncoding("utf-8");
					response.setContentType("text/html");
					
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					String result = gson.toJson(searchBigMap);
					
					try {
						log.info("退出searchResourceByPageNewAction(),返回结果result="+result);
						response.getWriter().write(result);
					} catch (IOException e) {
						log.error("退出searchResourceByPageNewAction(),返回结果result="+result+"失败");
						e.printStackTrace();
					}
					return;
				}else{
					boolean flag = false;
					List<Long> userAreaIds = new ArrayList<Long>();//用户区域id List
					for(BasicEntity be:areaByUserId){
						String aId = be.getValue("area_id")+"";
						userAreaIds.add(Long.valueOf(aId));
						if(aId.equals(this.areaId)){//区域条件
							areaConditionSql +=" and  (fn.path like '%/Sys_Area/"+be.getValue("area_id")+"/%' or fn.parent_figurenode_id=0 or fn.parent_figurenode_id is null)";
							flag = true;
							break;
						}						
					}
					if(!flag){//选择区域不在用户区域 判断是否在子区域
						List<Long> curAreaIds = new ArrayList<Long>();//当前选择区域id List
						if(this.areaId != null && !this.areaId.equals("")){
							curAreaIds.add(Long.valueOf(this.areaId));
						}else{
							return;
						}
						List<BasicEntity> curAreaList = this.physicalresService.getResourceByResourceType(curAreaIds, "Sys_Area", "Sys_Area");
						List<BasicEntity> userAreaList = this.physicalresService.getResourceByResourceType(userAreaIds, "Sys_Area", "Sys_Area");
						List<String> curArea = new ArrayList<String>();
						List<String> userArea = new ArrayList<String>();
						for(BasicEntity be:curAreaList){
							curArea.add(be.getValue("area_id")+"");
						}
						for(BasicEntity be:userAreaList){
							userArea.add(be.getValue("area_id")+"");
						}
						curArea.retainAll(userArea);//求交集
						if(curArea!=null && !curArea.isEmpty()){//获取区域条件
							for(String id:curArea){
								if("".equals(areaConditionSql)){
									areaConditionSql +=" and ( fn.path like '%/Sys_Area/"+id+"/%'";
								}else{
									areaConditionSql +=" or fn.path like '%/Sys_Area/"+id+"/%'";
								}
							}
							areaConditionSql +=" or fn.parent_figurenode_id=0 or fn.parent_figurenode_id is null)";
						}
						
					}
				}
			}
			this.searchResourceMapList = new ArrayList<Map<String,Object>>();
			ApplicationModule module = ModuleProvider.getModule(this.sType);
			Map<String, Object> searchedResModuleMap = module.toMap();
			Map<String,Object> orderIdMap = new HashMap<String,Object>();
			if(searchedResModuleMap != null) {//获取资源字典
				//获取中文标题(不能正常获取的继续显示英文)
				Map<String, Object> titleMap = new LinkedHashMap<String, Object>();
				Map<String, Object> attrTypeMap = new LinkedHashMap<String, Object>();
				for(String key : searchedResModuleMap.keySet()) {
					try {
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							//数据字典，中英文转换
							entry = dictionary.getEntry(key + "," + this.sType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								attrTypeMap.put(key,module.getAttribute(key).getValue("type"));
								titleMap.put(key, entry.get(0).getValue("display"));
								orderIdMap.put( entry.get(0).getValue("orderID").toString(), key);
							}
						}
					} catch (EntryOperationException e) {
						log.error("获取"+this.sType+"属性字段"+key+"的中文字典失败，可能该字典不存在");
						e.printStackTrace();
						titleMap.put(key, key);
						orderIdMap.put( "0", key);
					}
				}
				Map<String,Object> sortedMap = quickSort.sortMap(titleMap,orderIdMap);//中文字典排序
				if(sortedMap!=null){
					titleMap = sortedMap;
				}
				String sqlStr = "";
				if(this.conditionString!=null && !"".equals(this.conditionString)){
					sqlStr = this.conditionString;
				}else{
					ApplicationEntity appEntity = ActionHelper.getApplicationEntity(this.sType);//条件组合

					Map<String,String> dateMap = null;
					if(this.dateString!=null&&!"".equals(this.dateString)){//属性类型为日期特殊处理 
						dateMap = new HashMap<String,String>();
						String[] dates=this.dateString.split(",");
						for(String s:dates){
							String key=s.substring(0,s.indexOf(":"));
							String date=s.substring(s.indexOf(":")+1,s.length());
							dateMap.put(key,date);
						}
					}
					
					sqlStr = this.physicalresService.getSearchSqlString(appEntity, dateMap);
					
					if(name!=null && !"".equals(name)){
						//sqlStr +=" and instr(stype.name,'"+name+"')>0stype.name like '%"+name+"%'";
						name=name.trim();
						if(name!=null && !"".equals(name)){
							sqlStr +=" and instr(stype.name,'"+name+"')>0";
						}
						
					}
					if(label!=null && !"".equals(label)){
						//sqlStr +=" and stype.label like '%"+label+"%'";
						label = label.trim();
						if(label!=null && !"".equals(label)){
							sqlStr +=" and instr(stype.label,'"+label+"')>0";
						}
						
					}
					this.conditionString = sqlStr;
				}
				
				String sql = "";
				String countSql = "";
				if(sqlStr!=null && !"".equals(sqlStr)){
					sql = " select "+ResourceCommon.getSelectSqlAttributsString(sType, "stype")+" ,rownum rc from "+this.sType+"  stype,figurenode  fn where stype.ENTITY_ID=fn.entityId  "+areaConditionSql+sqlStr+"";
					countSql = " select count(*)  \"count\" from "+this.sType+"  stype,figurenode  fn where stype.ENTITY_ID=fn.entityId  "+areaConditionSql+sqlStr+"";
				}else{
					sql = "select  "+ResourceCommon.getSelectSqlAttributsString(sType, "stype")+" ,rownum rc from "+this.sType+"  stype,figurenode  fn where stype.ENTITY_ID=fn.entityId "+areaConditionSql+"";					
					countSql = "select  count(*)  \"count\" from "+this.sType+"  stype,figurenode  fn where stype.ENTITY_ID=fn.entityId "+areaConditionSql+"";
				}
				List<Map<String,Object>> list = this.physicalresService.searchResourceBySql(this.sType, sql, countSql,this.pageSize, this.pageIndex,this.orderType);
				List<BasicEntity> rList=null;
				if(list!=null && list.size()>0){
					Map<String,Object> map = list.get(0);
					totalSize = Integer.parseInt(map.get("count")+"");
					rList = (List<BasicEntity>)map.get("entityList");
					if(this.pageSize!=null && !"".equals(this.pageSize )&& !"0".equals(this.pageSize)){
						if(totalSize%Integer.parseInt(this.pageSize)==0){
							totalPage = totalSize/Integer.parseInt(this.pageSize);
						}else{
							totalPage = totalSize/Integer.parseInt(this.pageSize)+1;
						} 
					}
					
				}
				//rList = this.physicalresService.searchResourceBySql(this.sType, sql, this.pageSize, this.pageIndex,this.orderType);
				
				List<String> assNameList = new ArrayList<String>();
				if(rList!=null && rList.size()>0){
					for(BasicEntity be : rList){
						long childId=0;
						String enId = be.getValue("_entityId")+"";
						if(this.sType.indexOf("Area")>=0){
							childId=Long.valueOf(be.getValue("area_id")+"");
						}else{
							childId=Long.valueOf(be.getValue("id")+"");
						}
						List<BasicEntity> parEn = this.physicalresService.getParentResourceByChildIdAndChildType(childId, this.sType);
						if(parEn!=null && !parEn.isEmpty()){
							BasicEntity b = parEn.get(0);
							String parName = b.getValue("name");
							String parEnId = b.getValue("_entityId")+"";
							if(parName!=null && !(parEnId).equals(enId)){
								assNameList.add(parName);
							}else{
								assNameList.add("");
							}
						}else{
							assNameList.add("");
						}
						Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(ApplicationEntity.changeFromEntity(be));
						//将null值转换为空串值
						resMap = ResourceCommon.mapRecombinationToString(resMap);
						for(String key:resMap.keySet()){
							resMap.put(key, ResourceCommon.dateTimeFormat(resMap.get(key)));
						}
						//生成被查询资源的map(已进行关联资源的过滤)
						sortedMap = quickSort.sortMap(resMap,orderIdMap);//排序
						if(sortedMap!=null){
							resMap=sortedMap;
						}
						this.searchResourceMapList.add(resMap);
					}	

				}
				
				Map<String,Object> searchBigMap = new HashMap<String, Object>();
				searchBigMap.put("titleMap", titleMap);
				searchBigMap.put("contentMapList", this.searchResourceMapList);
				searchBigMap.put("assNameList", assNameList);
				searchBigMap.put("totalPage", totalPage);
				searchBigMap.put("totalSize", totalSize);
				searchBigMap.put("conditionString", conditionString);
				searchBigMap.put("attrTypeMap", attrTypeMap);
				searchBigMap.put("pageIndex", pageIndex);
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/html");
				
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				String result = gson.toJson(searchBigMap);
				
				try {
					log.info("退出searchResourceByPageNewAction(),返回结果result="+result);
					response.getWriter().write(result);
				} catch (IOException e) {
					log.error("退出searchResourceByPageNewAction(),返回结果result="+result+"失败");
					e.printStackTrace();
				}
				
			}
		}catch(Exception e){
			log.error("退出searchResourceByPageNewAction(),发生异常");
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	/**
	 * 
	 * @description: 跳转列表呈现/gis地图页面
	 * @author：
	 * @return     
	 * @return String     
	 * @date：Feb 22, 2013 9:17:35 AM
	 */
	public String forwardSearchResourceGisPageAction(){
		log.info("进入forwardSearchResourceGisPageAction(),跳转列表呈现/gis地图页面");
		if(this.sType!=null &&!"".equals(this.sType)){
			List<BasicEntity> entry = null;
			try {
				entry = dictionary.getEntry(this.sType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
				if(entry != null && !entry.isEmpty()) {
					this.chineseName = entry.get(0).getValue("display").toString();
				}
			} catch (EntryOperationException e) {
				log.error("forwardSearchResourceGisPageAction(),获取"+this.sType+"中文字典失败，可能该字典不存在");
				e.printStackTrace();
				if(entry != null && !entry.isEmpty()) {
					 this.chineseName = this.sType;
				}
			}
		}
		
		if("listPage".equals(this.forwardType)){
			log.info("退出forwardSearchResourceGisPageAction(),返回结果listPage");
			return "listpage";
		}else{
			log.info("退出forwardSearchResourceGisPageAction(),返回结果success");
			return "success";
		}
		
	}
	//导出资源数据
	public String exportSearchResourceDataAction(){
		log.info("进入exportSearchResourceDataAction(),导出资源数据");
		try {
			Map<String, Object> searchedResModuleMap = ModuleProvider.getModule(this.sType).toMap();
			Map<String,Object> orderIdMap = new HashMap<String,Object>();
			Map<String,Object> sortedMap = null;
			this.searchResourceMapList = new ArrayList<Map<String,Object>>();
			int totalSize = 0;
			int totalPage = 0;
			if(searchedResModuleMap != null) {
				//获取中文标题(不能正常获取的继续显示英文)(这里用linkedHashMap，使其与模板顺序一致，确保跟内容相对应)
				Map<String,Object> titleMap = new LinkedHashMap<String, Object>();
				
				for(String key : searchedResModuleMap.keySet()) {
					try {
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							//数据字典，中英文转换
							entry = dictionary.getEntry(key + "," + this.sType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								titleMap.put(key, entry.get(0).getValue("display"));
								orderIdMap.put( entry.get(0).getValue("orderID").toString(), key);
							}
						}
					} catch (EntryOperationException e) {
						log.error("获取"+this.sType+"属性字段"+key+"中文字典失败，可能该字典不存在");
						e.printStackTrace();
						titleMap.put(key, key);
						orderIdMap.put("0", key);
					}
				}	
				sortedMap = quickSort.sortMap(titleMap,orderIdMap);//中文字典排序
				if(sortedMap!=null){
					titleMap = sortedMap;
				}
				
				//获取登录人ID
				HttpSession session = ServletActionContext.getRequest().getSession();
				String userId = "";
				String areaConditionSql = "";
				List<BasicEntity> areaByUserId = null;//用户所属区域
				if(session.getAttribute("userId") != null && !"".equals(session.getAttribute("userId"))){	
					userId = session.getAttribute("userId").toString();
					areaByUserId = this.staffOrganizationService.getAreaByUserId(userId);//查询用户所属区域
					if(areaByUserId==null || areaByUserId.isEmpty()){//用户所属区域id为空 不搜索
						return "error";
					}else{
						boolean flag = false;
						List<Long> userAreaIds = new ArrayList<Long>();//用户区域id List
						for(BasicEntity be:areaByUserId){
							String aId = be.getValue("area_id")+"";
							userAreaIds.add(Long.valueOf(aId));
							if(aId.equals(this.areaId)){//区域条件
								areaConditionSql +=" and  (fn.path like '%/Sys_Area/"+be.getValue("area_id")+"/%' or fn.parent_figurenode_id=0 or fn.parent_figurenode_id is null)";
								flag = true;
								break;
							}						
						}
						if(!flag){//选择区域不在用户区域 判断是否在子区域
							List<Long> curAreaIds = new ArrayList<Long>();//当前选择区域id List
							curAreaIds.add(Long.valueOf(this.areaId));
							List<BasicEntity> curAreaList = this.physicalresService.getResourceByResourceType(curAreaIds, "Sys_Area", "Sys_Area");
							List<BasicEntity> userAreaList = this.physicalresService.getResourceByResourceType(userAreaIds, "Sys_Area", "Sys_Area");
							List<String> curArea = new ArrayList<String>();
							List<String> userArea = new ArrayList<String>();
							for(BasicEntity be:curAreaList){
								curArea.add(be.getValue("area_id")+"");
							}
							for(BasicEntity be:userAreaList){
								userArea.add(be.getValue("area_id")+"");
							}
							curArea.retainAll(userArea);//求交集
							if(curArea!=null && !curArea.isEmpty()){//获取区域条件
								for(String id:curArea){
									if("".equals(areaConditionSql)){
										areaConditionSql +=" and ( fn.path like '%/Sys_Area/"+id+"/%'";
									}else{
										areaConditionSql +=" or fn.path like '%/Sys_Area/"+id+"/%'";
									}
								}
								areaConditionSql +=" or fn.parent_figurenode_id=0 or fn.parent_figurenode_id is null)";
							}
							
						}
					}
				}
				String sqlStr = this.conditionString;
				String sql = "";
				String countSql = "";
				if(sqlStr!=null && !"".equals(sqlStr)){
					sql = " select "+ResourceCommon.getSelectSqlAttributsString(sType, "stype")+" ,rownum rc from "+this.sType+"  stype,figurenode  fn where stype.ENTITY_ID=fn.entityId  "+areaConditionSql+sqlStr+"";
					countSql = " select count(*)  \"count\" from "+this.sType+"  stype,figurenode  fn where stype.ENTITY_ID=fn.entityId  "+areaConditionSql+sqlStr+"";
				}else{
					sql = "select  "+ResourceCommon.getSelectSqlAttributsString(sType, "stype")+" ,rownum rc from "+this.sType+"  stype,figurenode  fn where stype.ENTITY_ID=fn.entityId "+areaConditionSql+"";					
					countSql = "select  count(*)  \"count\" from "+this.sType+"  stype,figurenode  fn where stype.ENTITY_ID=fn.entityId "+areaConditionSql+"";
				}
				List<Map<String,Object>> list = this.physicalresService.searchResourceBySql(this.sType, sql, countSql,"0", this.pageIndex,this.orderType);
				List<BasicEntity> rList=null;
				if(list!=null && list.size()>0){
					Map<String,Object> map = list.get(0);
					totalSize = Integer.parseInt(map.get("count")+"");
					rList = (List<BasicEntity>)map.get("entityList");
					if(this.pageSize!=null && !"".equals(this.pageSize )&& !"0".equals(this.pageSize)){
						if(totalSize%Integer.parseInt(this.pageSize)==0){
							totalPage = totalSize/Integer.parseInt(this.pageSize);
						}else{
							totalPage = totalSize/Integer.parseInt(this.pageSize)+1;
						} 
					}
					
				}				
				List<String> assNameList = new ArrayList<String>();
				if(rList!=null && rList.size()>0){
					for(BasicEntity be : rList){
						long childId=0;
						String enId = be.getValue("_entityId")+"";
						if(this.sType.indexOf("Area")>=0){
							childId=Long.valueOf(be.getValue("area_id")+"");
						}else{
							childId=Long.valueOf(be.getValue("id")+"");
						}
						List<BasicEntity> parEn = this.physicalresService.getParentResourceByChildIdAndChildType(childId, this.sType);
						
						if(parEn!=null && !parEn.isEmpty()){
							BasicEntity b = parEn.get(0);
							String parName = b.getValue("name");
							String parEnId = b.getValue("_entityId")+"";
							if(parName!=null && !(parEnId).equals(enId)){
								assNameList.add(parName);
							}else{
								assNameList.add("");
							}
						}else{
							assNameList.add("");
						}
						Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(ApplicationEntity.changeFromEntity(be));
						//将null值转换为空串值
						resMap = ResourceCommon.mapRecombinationToString(resMap);
						for(String key:resMap.keySet()){
							resMap.put(key, ResourceCommon.dateTimeFormat(resMap.get(key)));
						}
						//生成被查询资源的map(已进行关联资源的过滤)
						sortedMap = quickSort.sortMap(resMap,orderIdMap);//排序
						if(sortedMap!=null){
							resMap=sortedMap;
						}
						this.searchResourceMapList.add(resMap);
					}	

				}
				//获取标题行
				List titleList = new ArrayList();
				titleList.add("所属资源名称");
					
				for (String key : titleMap.keySet()) {
					titleList.add(titleMap.get(key));
				}
				//内容数据行
				List<List> dataList = new ArrayList<List>();
				int index = 0;
				for(Map<String, Object> map : this.searchResourceMapList) {
					List contentList = new ArrayList();
					contentList.add(assNameList.get(index));
					index++;
					if(map != null) {
						for(String key : map.keySet()) {
							if( !("_entityId".equals(key) || "_entityType".equals(key))) {
									contentList.add(map.get(key));	
							}
						}
					}
					dataList.add(contentList);
				}
				if (!titleList.isEmpty() && !dataList.isEmpty()) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
					//导出excel数据
					resultInputStream = creatExcel2003InputStream(titleList, dataList);
					try {
						chaxunFileName = sdf.format(new Date()) + " "
								+ new String("资源数据导出表".getBytes("GBK"), "ISO-8859-1");
					} catch (UnsupportedEncodingException e) {
						chaxunFileName = "resource";
						log.error("发生UnsupportedEncodingException异常,设置chaxunFileName="+chaxunFileName);
						e.printStackTrace();
					}
					log.info("退出exportSearchResourceDataAction,返回结果result=success");
					return "success";
				} else {
					log.info("退出exportSearchResourceDataAction,返回结果result=error");
					return "error";
				}
			}
			log.info("退出exportSearchResourceDataAction,返回结果result=error");
			return "error";
		} catch (NumberFormatException e) {
			log.error("退出exportSearchResourceDataAction,发生NumberFormatException异常,返回结果result=error");
			e.printStackTrace();
			return "error";
		}
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
		log.info("进入creatExcel2003InputStream(List biaotou,List<List> data),数据导出到Excel表");
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
				cell.setCellValue((String) biaotou.get(i));
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
			log.info("退出creatExcel2003InputStream(List biaotou,List<List> data),数据导出成功");
		} catch (Exception e) {
			log.error("退出creatExcel2003InputStream(List biaotou,List<List> data),发生异常，返回null");
			e.printStackTrace();
			return null;
		}
		return new ByteArrayInputStream(out.toByteArray());
	}
	//加载增加资源页面
	public String loadAddResourcePageAction(){
		log.info("进入loadAddResourcePageAction，加载添加物理资源页面");
		try {
			if(areaId!=null&&!"".equals(areaId)){
				BasicEntity areaEntity =physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
				if(areaEntity!=null){
					this.areaName = areaEntity.getValue("name");
				}
			}
			List<BasicEntity> entry1 = null;
			try {
				entry1 = dictionary.getEntry(this.sType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
				if(entry1 != null && !entry1.isEmpty()) {
					this.chineseName = entry1.get(0).getValue("display").toString();
				}
			} catch (EntryOperationException e) {
				log.error("forwardSearchResourceGisPageAction(),获取"+this.sType+"中文字典失败，可能该字典不存在");
				e.printStackTrace();
				if(entry1 != null && !entry1.isEmpty()) {
					 this.chineseName = this.sType;
				}
			}
			ApplicationModule module = ModuleProvider.getModule(this.sType);
			this.addedResMap = module.toMap();
			//中英文转换map
			this.currentEntityChineseMap = new HashMap<String, Object>();
			//非空属性判断map
			this.nullableMap = new HashMap<String, Object>();
			//属性类型判断map
			this.attrTypeMap = new HashMap<String, Object>();
			//生成下拉框的map
			this.dropdownListMap = new HashMap<String, Object>();
			//属性长度判断map
			this.attrLengthMap = new HashMap<String,Object>();
			Map<String,Object> orderIdMap =new HashMap<String,Object>();//entity属性排序
			
			for(String key : this.addedResMap.keySet()) {
				try {
					List<BasicEntity> entry = null;
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						//数据字典，中英文转换
						entry = dictionary.getEntry(key + "," + sType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					}
					if(entry != null && !entry.isEmpty()) {
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							this.currentEntityChineseMap.put(key, entry.get(0).getValue("display"));
						//	System.out.println(entry.get(0).getValue("orderID").toString()+"--");
							orderIdMap.put(entry.get(0).getValue("orderID").toString(),key);
						}
					}
					
					//根据数据字典中，属性为entity类型或者general类型，获取属性对应的下拉框的值，提供前台进行下拉框选择操作
					//拿出来的值，例如:其他-其他,置换-置换,局用-局用
					if("entity".equals(entry.get(0).getValue("dictionaryType"))) {
						//entity类型下拉框数据组装
						List<BasicEntity> dropDownEntry = dictionary.getEntry(key + "," + this.sType + ",networkResourceValueEmun", SearchScope.OBJECT, "");
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
								this.dropdownListMap.put(key, attrDropdownList);
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
								this.dropdownListMap.put(key, attrDropdownList);
							}
						}
					}
				} catch (EntryOperationException e) {
					log.error("获取"+sType+"属性字段"+key+"的中文字典失败，可能该字典不存在。");
					e.printStackTrace();
				}
				
				//判断是否需要增加非空判断
				Object nullVal = module.getAttribute(key).getValue("nullable");
				if(nullVal != null && !"".equals(nullVal)) {
					this.nullableMap.put(key, nullVal.toString());
				}
				//用map保存属性的类型,例如java.lang.String
				Object attrTypeVal = module.getAttribute(key).getValue("type");
				if(attrTypeVal != null && !"".equals(attrTypeVal)) {
					this.attrTypeMap.put(key, attrTypeVal.toString());
					if(attrTypeVal.toString().equals("java.lang.String")){
						//用map保存String类型属性的长度
						Object attrLengthVal = module.getAttribute(key).getValue("length");
						if(attrLengthVal!=null && !"".equals(attrLengthVal)){
							this.attrLengthMap.put(key,Integer.valueOf(attrLengthVal.toString()));
						}
					}
				}
				
				//生成属性的下拉框，前台中提供选择操作
				
			}
			
			//获取当前资源类型关联的父类型，用来控制隶属资源树单选按钮的生成
			String[] parentTypeArrs = structureCommonService.getAssociatedAetName(this.sType, AssociatedType.PARENT, "networkresourcemanage");
			if(parentTypeArrs != null && parentTypeArrs.length > 0) {
				this.parentEntityTypeGrp = "";
				for (String type : parentTypeArrs) {
					this.parentEntityTypeGrp += type + ",";
				}
				this.parentEntityTypeGrp = this.parentEntityTypeGrp.substring(0, this.parentEntityTypeGrp.length() - 1);	
				if(this.currentEntityId!=null && !"".equals(this.currentEntityId)&&this.currentEntityType!=null&&!"".equals(this.currentEntityType)){
					this.parentMapList = new ArrayList<Map<String,Object>>();
					ApplicationEntity curAp = this.structureCommonService.getSectionEntity(this.currentEntityType, this.currentEntityId);
					for (String type : parentTypeArrs) {
						List<BasicEntity> entry = null;
						String name = "";
						try {
							entry = dictionary.getEntry(type + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							if(entry != null && !entry.isEmpty()) {
								name = entry.get(0).getValue("display").toString();
							}
						} catch (EntryOperationException e) {
							log.error("forwardSearchResourceGisPageAction(),获取"+type+"中文字典失败，可能该字典不存在");
							e.printStackTrace();
							if(entry != null && !entry.isEmpty()) {
								 name = type;
							}
						}
						if(this.currentEntityType.equals(type)){
							Map<String,Object> rMap = curAp.toMap();
							rMap.put("chineseName",name);
							this.parentMapList.add(rMap);	
						}else{
							ApplicationEntity[] apps = this.structureCommonService.getAppArrsByRecursionForSrcSameType(curAp, type,"networkresourcemanage");
							if(apps!=null && apps.length>0){
								for(ApplicationEntity ap:apps){
									Map<String,Object> rMap = ap.toMap();
									rMap.put("chineseName",name);
									this.parentMapList.add(rMap);	
								}
							}
						}
						
					}
					if(parentMapList!=null){
						quickSort.sort(this.parentMapList, "name");
					}
				}else{
					
					this.parentEntityTypeGrp = "";
					for (String type : parentTypeArrs) {
						this.parentEntityTypeGrp += type + ",";
					}
					this.parentEntityTypeGrp = this.parentEntityTypeGrp.substring(0, this.parentEntityTypeGrp.length() - 1);
				
				}	
			}
			
			//addedResMap排序
			Map<String,Object> sortedMap = quickSort.sortMap(this.addedResMap, orderIdMap);
			if(sortedMap!=null){
				this.addedResMap = sortedMap;
			}
			
			log.info("退出loadAddResourcePageAction()，返回结果result=success");
			return "success"; //页面跳转
		} catch (RuntimeException e) {
			log.info("退出loadAddResourcePageAction()，发生RuntimeException异常，返回结果result=error");
			e.printStackTrace();
			return "error";
		}
	}
	/*增加资源*/
	public void addResourceNewAction(){
		log.info("进入addResourceNewAction()，添加一个物理资源");
		try {
			
			ApplicationEntity appEntity = ActionHelper.getApplicationEntity(this.addedResEntityType);
			if(this.addedResEntityType.indexOf("Area")>=0){
				appEntity.setValue("area_id",structureCommonService.getEntityPrimaryKey(this.addedResEntityType));
			}else{
				appEntity.setValue("id",structureCommonService.getEntityPrimaryKey(this.addedResEntityType));
			}
			
			int resultCount = structureCommonService.saveInfoEntity(appEntity, "networkresourcemanage");
			//添加资源维护记录
			if(resultCount == 1){
				String resName = "";
				if(appEntity.getValue("name") != null){
					resName = appEntity.getValue("name");
				}else{
					resName = appEntity.getValue("label");
				}
				ResourceMaintenance maintenance = new ResourceMaintenance();
				maintenance.setBiz_module(OperationType.NETWORK);
				maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
				maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
				maintenance.setOp_category(OperationType.RESOURCEINSERT);
				maintenance.setRes_id((Long)appEntity.getValue("id"));
				maintenance.setRes_type(appEntity.getValue("_entityType").toString());
				maintenance.setRes_keyinfo(resName);
				networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
				if(this.addedResParentEntityId!=0L && !"".equals(this.addedResParentEntityId)){
					log.info("参数父资源id addedResParentEntityId="+addedResParentEntityId+",类型addedResParentEntityType="+addedResParentEntityType);
					//获取当前被添加的entity
					BasicEntity addedEntity = physicalresService.getPhysicalresById(this.addedResEntityType, Long.parseLong(appEntity.getValue("id").toString()));
					ApplicationEntity addedAppEntity = ApplicationEntity.changeFromEntity(addedEntity);
				
					//获取当前被添加的entity的父entity
					BasicEntity addedParentEntity = physicalresService.getPhysicalresById(this.addedResParentEntityType, this.addedResParentEntityId);
					ApplicationEntity addedParentAppEntity = ApplicationEntity.changeFromEntity(addedParentEntity);

					//建立新资源与其父entity的父子关系
					resultCount = structureCommonService.createAssociatedRelation(addedParentAppEntity, addedAppEntity, AssociatedType.CLAN, "networkresourcemanage");
					if(resultCount == 1){
						String resNameChinese= "";
						if(addedParentEntity != null){
							try {
								List<BasicEntity> entry = null;
									entry = dictionary.getEntry(this.addedResParentEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
									resNameChinese = entry.get(0).getValue("display");
										//System.out.println(infoNameChinese);
							} catch (EntryOperationException e) {
								log.error("获取"+this.addedResParentEntityType+"中文字典失败，可能该中文字典不存在。");
								e.printStackTrace();
							}
						}
						maintenance = new ResourceMaintenance();
						maintenance.setBiz_module(OperationType.NETWORK);
						maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
						maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
						String lresName = "";
						if(addedParentAppEntity.getValue("name") != null){
							lresName = addedParentAppEntity.getValue("name");
						}else{
							lresName = addedParentAppEntity.getValue("label");
						}
						String rresName = "";
						if(addedAppEntity.getValue("name") != null){
							rresName = addedAppEntity.getValue("name");
						}else{
							rresName = addedAppEntity.getValue("label");
						}
						maintenance.setRes_keyinfo(rresName);
						String content = OperationType.INSERT+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
						maintenance.setOp_category(OperationType.INSERTLINK);
						maintenance.setContent(content);
						maintenance.setRes_id((Long)addedAppEntity.getValue("id"));
						maintenance.setRes_type(addedAppEntity.getValue("_entityType").toString());
						networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
					}else{
						log.error("创建新增"+this.addedResEntityType+"资源关联关系失败");
						resultCount=0;
					}
				}
			}else{
				log.error("添加保存"+this.addedResEntityType+"资源失败");
				resultCount=0;
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = null;
			if (resultCount > 0) {
				result = gson.toJson(appEntity.getValue("id")+"");
			} else {
				result = gson.toJson("error");
			}
			try {
				log.info("退出addResourceNewAction，返回结果result="+result);
				response.getWriter().write(result);
			} catch (IOException e) {
				log.error("退出addResourceNewAction，返回结果result="+result+"失败");
				e.printStackTrace();
			}
			
		} catch (NumberFormatException e) {
			log.error("退出addResourceNewAction，发生NumberFormatException异常");
			e.printStackTrace();
		}
	}
	//加载资源修改页面
	public String loadUpdateResourcePageAction(){
		log.info("进入loadUpdateResourcePageAction()，查询一个节点(用于对节点进行编辑操作)");
		if(bizModule != null && !bizModule.equals("")){
			try {
				this.bizModule = new String(this.bizModule.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				log.error(this.bizModule+"转码失败，发生UnsupportedEncodingException");
				e1.printStackTrace();
			}
			
		}
		List<BasicEntity> entry1=null;
		this.chineseName = this.currentEntityType;
		try {
			entry1 = dictionary.getEntry(this.currentEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
		} catch (EntryOperationException e) {
			log.error("获取"+currentEntityType+"的中文字典失败，可能该字典不存在。");
			e.printStackTrace();
		}
		if(entry1 != null && !entry1.isEmpty()) {
			this.chineseName = entry1.get(0).getValue("display");
		}
		try {
			if(areaId!=null&&!"".equals(areaId)){
				BasicEntity areaEntity =physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
				if(areaEntity!=null){
					this.areaName = areaEntity.getValue("name");
				}
			}
			
			ApplicationModule module = ModuleProvider.getModule(this.currentEntityType);

			BasicEntity currentEntity =null;
			if(this.currentEntityId!=null&&!"".equals(this.currentEntityId)){
				long currentEntityId = Long.parseLong(this.currentEntityId);
			    currentEntity = physicalresService.getPhysicalresById(this.currentEntityType, currentEntityId);
			}else{
				 currentEntity = physicalresService.getPhysicalresByLabel(this.currentEntityType,this.currentEntityLabel);
			}
			
			ApplicationEntity currentAppEntity = ApplicationEntity.changeFromEntity(currentEntity);
			this.currentEntityId=currentAppEntity.getValue("id").toString();
			Map<String,Object> orderIdMap = new HashMap<String,Object>();//排序map
			if(currentAppEntity != null) {
				//转换map，包含空属性
				this.currentEntityMap = ResourceCommon.applicationEntityConvertMap(currentAppEntity);
				
				//中英文转换map
				this.currentEntityChineseMap = new HashMap<String, Object>();
				//非空属性判断map
				this.nullableMap = new HashMap<String, Object>();
				//属性类型判断map
				this.attrTypeMap = new HashMap<String, Object>();
				//生成下拉框的map
				this.dropdownListMap = new HashMap<String, Object>();
				//属性长度判断map
				this.attrLengthMap =new HashMap<String,Object>();
				
				for(String key : this.currentEntityMap.keySet()) {
					try {
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							//数据字典，中英文转换
							entry = dictionary.getEntry(key + "," + this.currentEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								this.currentEntityChineseMap.put(key, entry.get(0).getValue("display"));
								orderIdMap.put(entry.get(0).getValue("orderID").toString(), key);
							}
						}
						
						if(!"_entityId".equals(key)&&!"_entityType".equals(key)) {
							
							
							//根据数据字典中，属性为entity类型或者general类型，获取属性对应的下拉框的值，提供前台进行下拉框选择操作
							//拿出来的值，例如:其他-其他,置换-置换,局用-局用
							if("entity".equals(entry.get(0).getValue("dictionaryType"))) {
								//entity类型下拉框数据组装
								List<BasicEntity> dropDownEntry = dictionary.getEntry(key + "," + this.currentEntityType + ",networkResourceValueEmun", SearchScope.OBJECT, "");
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
										this.dropdownListMap.put(key, attrDropdownList);
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
										this.dropdownListMap.put(key, attrDropdownList);
									}
								}
							}
						}
					} catch (EntryOperationException e) {
						log.error("获取"+currentEntityType+"属性字段"+key+"的中文字典失败，可能该字典不存在。");
						e.printStackTrace();
					}
					
					if(!"_entityId".equals(key)&&!"_entityType".equals(key)) {
						//判断是否需要增加非空判断
						Object nullVal = module.getAttribute(key).getValue("nullable");
						if(nullVal != null && !"".equals(nullVal)) {
							this.nullableMap.put(key, nullVal.toString());
						}
						//用map保存属性的类型,例如java.lang.String
						Object attrTypeVal = module.getAttribute(key).getValue("type");
						if(attrTypeVal != null && !"".equals(attrTypeVal)) {
							this.attrTypeMap.put(key, attrTypeVal.toString());
							
							if(attrTypeVal.toString().equals("java.lang.String")){
								//用map保存String类型属性的长度
								Object attrLengthVal = module.getAttribute(key).getValue("length");
								if(attrLengthVal!=null && !"".equals(attrLengthVal)){
									this.attrLengthMap.put(key,Integer.valueOf(attrLengthVal.toString()));
								}
							}
						}
						
					}
					
					//若类型为时间类型，进行格式化
					if(this.currentEntityMap.get(key) != null 
							&& this.currentEntityMap.get(key).getClass().getName().indexOf("Date") > -1) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
						this.currentEntityMap.put(key, sdf.format(this.currentEntityMap.get(key)));
					}
				}
			}
			
			//获取当前资源类型关联的父类型，用来控制隶属资源树单选按钮的生成
			String[] parentTypeArrs = structureCommonService.getAssociatedAetName(this.currentEntityType, AssociatedType.PARENT, "networkresourcemanage");
			if(parentTypeArrs != null && parentTypeArrs.length > 0) {
				this.parentEntityTypeGrp = "";
				for (String type : parentTypeArrs) {
					this.parentEntityTypeGrp += type + "_";
				}
				this.parentEntityTypeGrp = this.parentEntityTypeGrp.substring(0, this.parentEntityTypeGrp.length() - 1);
			}
			
			//数据处理(空值处理)
			ResourceCommon.mapRecombinationToString(this.currentEntityMap);
			
			ApplicationEntity parentAppEntity = null;
			if(this.parentEntityType != null && this.parentEntityId != null 
					&& !"undefined".equals(this.parentEntityType) && !"undefined".equals(this.parentEntityId) 
					&& !"null".equals(this.parentEntityType) && !"null".equals(this.parentEntityId) 
					&& !"".equals(this.parentEntityType) && !"".equals(this.parentEntityId) ) {
				//能够获取父级类型和id的情况，直接获取父级entity
				parentAppEntity = ApplicationEntity.changeFromEntity(
						physicalresService.getPhysicalresById(this.parentEntityType, Long.parseLong(this.parentEntityId)));
			} else {
				//未能获取父级类型和id的情况，通过查询获取
				ApplicationEntity[] parentAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(currentAppEntity, AssociatedType.PARENT, "networkresourcemanage");
				if(parentAppArrs != null && parentAppArrs.length > 0) {
					//获取父级entity
					parentAppEntity = parentAppArrs[0];
				}
			}
			
			String parentType = "";
			if(parentAppEntity != null && parentAppEntity.getType() != null) {	
				//求出父entity后，获取其id和类型，供属性编辑页面，隶属资源树控制使用
				//this.parentEntityId = parentAppEntity.getValue("id").toString();
				//this.parentEntityType = parentType;
				this.parentEntityMap = parentAppEntity.toMap();
				if(parentAppEntity.getValue("name")!=null && !"".equals(parentAppEntity.getValue("name"))){
					this.parentEntityMap.put("name", parentAppEntity.getValue("name"));
				}					
			}
			Map<String,Object> sortedMap = quickSort.sortMap(this.currentEntityMap, orderIdMap);
			if(sortedMap!=null){
				this.currentEntityMap = sortedMap;
			}
			log.info("退出loadUpdateResourcePageAction()，返回结果result=success");
			return "success";
		} catch (NumberFormatException e) {
			log.info("退出loadUpdateResourcePageAction()，发生NumberFormatException异常，返回结果result=error");
			e.printStackTrace();
			return "error";
		}
	}
	
	
	public void updateResourceNewAction(){
		log.info("进入updateResourceNewAction()，更新一个物理资源");
		try {
			//更新对象信息
			ApplicationEntity appEntity = ActionHelper.getApplicationEntity(this.updatedEntityType);
			ApplicationModule module = ModuleProvider.getModule(this.updatedEntityType);
			String uId = appEntity.getValue("id").toString();
			ApplicationEntity sectionEntity = structureCommonService.getSectionEntity(this.updatedEntityType, uId);
			//int updateResult = physicalresService.updatePhysicalres(appEntity, this.updatedEntityType);
			int updateResult =structureCommonService.updateApplicationEntityBySql(appEntity, module);
			if(updateResult>0){
				ApplicationEntity upddateAe = structureCommonService.getSectionEntity(this.updatedEntityType, uId);
					//updateApp
					
					String updateValue = getUpdateValue(sectionEntity,upddateAe);
					
					if(updateValue!=null && !"".equals(updateValue)){
						String resName = "";
						if(upddateAe.getValue("name") != null){
							resName = upddateAe.getValue("name");
						}else{
							resName = upddateAe.getValue("label");
						}
						ResourceMaintenance maintenance = new ResourceMaintenance();
						maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
						maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
						maintenance.setOp_category(OperationType.RESOURCEUPDATE);
						maintenance.setRes_id((Long)upddateAe.getValue("id"));
						maintenance.setRes_type(upddateAe.getValue("_entityType").toString());
						maintenance.setRes_keyinfo(resName);
						maintenance.setContent(updateValue);
						maintenance.setBiz_module(OperationType.NETWORK);
						int forces = networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
						if(forces > 0){
							if(this.bizModule != null && !this.bizModule.equals("")){
								ServiceMaintenance serviceMaintenance = new ServiceMaintenance();
								serviceMaintenance.setBiz_processcode(this.bizProcessCode);
								serviceMaintenance.setMaintenance_id(forces);
								if(this.bizProcessId != null && !this.bizProcessId.equals("")){
									long parseLong = Long.parseLong(bizProcessId);
									serviceMaintenance.setBiz_processId(parseLong);
								}
								if(this.bizModule != null && !this.bizModule.equals("")){
									serviceMaintenance.setBiz_module(this.bizModule);
								}
								networkResourceMaintenanceService.insertServiceMaintenance(serviceMaintenance);
							}
						}
				
					}
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = "";
			if(updateResult < 1) {
				result = gson.toJson("error");
				try {
					log.info("退出updateResourceNewAction()，更新失败，返回结果result="+result); 
					response.getWriter().write(result);
				} catch (IOException e) {
					log.error("退出updateResourceNewAction()，更新失败,返回结果result="+result+"失败"); 
					e.printStackTrace();
				}
				return;
			}
			if(this.newParentResEntityId != null && this.newParentResEntityType != null) {
				if(!"".equals(this.newParentResEntityId) && !"".equals(this.newParentResEntityType)) {
					if(!(this.oldParentResEntityId.equals(this.newParentResEntityId)&&this.oldParentResEntityType.equals(this.newParentResEntityType))) {
						//获取当前被更新的对象
						BasicEntity operatedEntity = physicalresService.getPhysicalresById(this.updatedEntityType, Long.parseLong(uId));
						ApplicationEntity operatedAppEntity = ApplicationEntity.changeFromEntity(operatedEntity);
						
						//获取当前被更新对象的旧的父对象
						//获取当前被更新对象的旧的父对象
						if(this.oldParentResEntityType!=null&&!"".equals(this.oldParentResEntityType)&&this.oldParentResEntityId!=null&&!"".equals(this.oldParentResEntityId)){
							BasicEntity oldParentEntity = physicalresService.getPhysicalresById(this.oldParentResEntityType, Long.parseLong(this.oldParentResEntityId));
							ApplicationEntity oldParentAppEntity = ApplicationEntity.changeFromEntity(oldParentEntity);
							//删除当前被更新对象的所有关系
							int delStrutureAssociation = structureCommonService.delStrutureAssociation(oldParentAppEntity, operatedAppEntity, AssociatedType.CHILD, "networkresourcemanage");
							//资源维护记录
							if(delStrutureAssociation == 1){
								String resNameChinese= "";
								if(oldParentResEntityType != null){
									try {
										List<BasicEntity> entry = null;
											entry = dictionary.getEntry(oldParentResEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
											resNameChinese = entry.get(0).getValue("display");
												//System.out.println(infoNameChinese);
									} catch (EntryOperationException e) {
										log.error("获取"+this.oldParentResEntityType+"中文字典失败,可能该中文字典不存在");
										e.printStackTrace();
									}
								}
								ResourceMaintenance maintenance = new ResourceMaintenance();
								maintenance.setBiz_module(OperationType.NETWORK);
								maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
								maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
								String lresName = "";
								if(oldParentAppEntity.getValue("name") != null){
									lresName = oldParentAppEntity.getValue("name");
								}else{
									lresName = oldParentAppEntity.getValue("label");
								}
								String rresName = "";
								if(operatedAppEntity.getValue("name") != null){
									rresName = operatedAppEntity.getValue("name");
								}else{
									rresName = operatedAppEntity.getValue("label");
								}
								maintenance.setRes_keyinfo(rresName);
								String content = OperationType.DELETE+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
								//String content = OperationType.INSERT +"与" + lresName + OperationType.getAssociatedTypeChinese(associatedType.toString());
								maintenance.setOp_category(OperationType.DELETELINK);
								maintenance.setContent(content);
								maintenance.setRes_id((Long)operatedAppEntity.getValue("id"));
								maintenance.setRes_type(operatedAppEntity.getValue("_entityType").toString());
								networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
							}
						}
						//获取当前被更新对象的新的父对象
						BasicEntity newParentEntity = physicalresService.getPhysicalresById(this.newParentResEntityType, Long.parseLong(this.newParentResEntityId));
						ApplicationEntity newParentAppEntity = ApplicationEntity.changeFromEntity(newParentEntity);
						
						
						//建立新父对象与当前被更新对象的父子关系
						int createAssociatedRelation = structureCommonService.createAssociatedRelation(newParentAppEntity, operatedAppEntity, AssociatedType.CLAN, "networkresourcemanage");
						//资源维护记录
						if(createAssociatedRelation == 1){
							String resNameChinese= "";
							if(newParentResEntityType != null){
								try {
									List<BasicEntity> entry = null;
										entry = dictionary.getEntry(newParentResEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
										resNameChinese = entry.get(0).getValue("display");
											//System.out.println(infoNameChinese);
								} catch (EntryOperationException e) {
									log.error("获取"+this.newParentResEntityType+"中文字典失败，可能该中文字典不存在");
									e.printStackTrace();
								}
							}
							ResourceMaintenance maintenance = new ResourceMaintenance();
							maintenance.setBiz_module(OperationType.NETWORK);
							maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
							maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
							String lresName = "";
							if(newParentAppEntity.getValue("name") != null){
								lresName = newParentAppEntity.getValue("name");
							}else{
								lresName = newParentAppEntity.getValue("label");
							}
							String rresName = "";
							if(operatedAppEntity.getValue("name") != null){
								rresName = operatedAppEntity.getValue("name");
							}else{
								rresName = operatedAppEntity.getValue("label");
							}
							maintenance.setRes_keyinfo(rresName);
							String content = OperationType.INSERT+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
							//String content = OperationType.INSERT +"与" + lresName + OperationType.getAssociatedTypeChinese(associatedType.toString());
							maintenance.setOp_category(OperationType.INSERTLINK);
							maintenance.setContent(content);
							maintenance.setRes_id((Long)operatedAppEntity.getValue("id"));
							maintenance.setRes_type(operatedAppEntity.getValue("_entityType").toString());
							networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
						}
					}
				}
			}
			
			result = gson.toJson("success");
			try {
				log.info("退出updateResourceNewAction()，返回结果result="+result); 
				response.getWriter().write(result);
			} catch (IOException e) {
				log.error("退出updateResourceNewAction()，返回结果result="+result+"失败"); 
				e.printStackTrace();
			}
			
		} catch (RuntimeException e) {
			log.error("退出updateResourceNewAction()，发生RuntimeException异常"); 
			e.printStackTrace();
		}
		
	}
	//获取修改前后对象属性修改信息
	public String getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe){
		log.info("进入getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe)，获取修改前后对象属性修改信息");
		log.info("进入getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe)，originalAe="+originalAe.toMap());
		log.info("进入getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe)，updateAe="+updateAe.toMap());

		String reString = "";
		ApplicationModule module = moduleLibrary.getModule(originalAe.getType());
		for(String key:module.keyset()){
			//System.out.println(originalAe.getValue("width")+ "     " +updateAe.getValue("width"));
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
		log.info("退出getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe),返回结果reString="+reString);
		return reString;
	}
	/**
	 * 
	 * @description: 根据id String 和类型 获取资源实例
	 * @author：     
	 * @return void     
	 * @date：Mar 7, 2013 5:26:14 PM
	 */
	public void  getResourceEntitysByIdsAndTypeAction(){
		try{
			log.info("进入getResourceEntitysByIdsAndTypeAction()，根据id String 和类型 获取资源实例"); 
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			if(this.currentEntityId!=null && !"".equals(this.currentEntityId)){
				String[] idArray=this.currentEntityId.split(",");
				if(idArray!=null && idArray.length>0){
					for(String id:idArray){
						ApplicationEntity ap=this.structureCommonService.getSectionEntity(this.currentEntityType, id);
						if(ap!=null){
							resultList.add(ap.toMap());
						}
					}
				}
			}
			Gson gson = new GsonBuilder().create();
			String result = gson.toJson(resultList);
			try {
				log.info("退出getResourceEntitysByIdsAndTypeAction()，返回结果result="+result); 
				response.getWriter().write(result);
			} catch (IOException e) {
				log.error("退出getResourceEntitysByIdsAndTypeAction()，返回结果result="+result+"失败"); 
				e.printStackTrace();
			}
		}catch(Exception e){
			log.error("退出getResourceEntitysByIdsAndTypeAction()，发生异常。");
		}
	}
	
	public PhysicalresService getPhysicalresService() {
		return physicalresService;
	}



	public void setPhysicalresService(PhysicalresService physicalresService) {
		this.physicalresService = physicalresService;
	}



	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}



	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}



	public XMLAEMLibrary getModuleLibrary() {
		return moduleLibrary;
	}



	public void setModuleLibrary(XMLAEMLibrary moduleLibrary) {
		this.moduleLibrary = moduleLibrary;
	}



	public Dictionary getDictionary() {
		return dictionary;
	}



	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}




	public Map<String, Object> getSearchMap() {
		return searchMap;
	}




	public void setSearchMap(Map<String, Object> searchMap) {
		this.searchMap = searchMap;
	}




	public Map<String, Object> getCurrentEntityChineseMap() {
		return currentEntityChineseMap;
	}




	public void setCurrentEntityChineseMap(
			Map<String, Object> currentEntityChineseMap) {
		this.currentEntityChineseMap = currentEntityChineseMap;
	}




	public Map<String, Object> getDropdownListMap() {
		return dropdownListMap;
	}




	public void setDropdownListMap(Map<String, Object> dropdownListMap) {
		this.dropdownListMap = dropdownListMap;
	}




	public Map<String, Object> getAttrTypeMap() {
		return attrTypeMap;
	}




	public void setAttrTypeMap(Map<String, Object> attrTypeMap) {
		this.attrTypeMap = attrTypeMap;
	}




	public String getSelectResType() {
		return selectResType;
	}




	public void setSelectResType(String selectResType) {
		this.selectResType = selectResType;
	}




	public QuickSort<Map<String, Object>> getQuickSort() {
		return quickSort;
	}




	public void setQuickSort(QuickSort<Map<String, Object>> quickSort) {
		this.quickSort = quickSort;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getSType() {
		return sType;
	}
	public void setSType(String type) {
		sType = type;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAreaType() {
		return areaType;
	}
	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}
	public String getAssName() {
		return assName;
	}
	public void setAssName(String assName) {
		this.assName = assName;
	}
	public List<Map<String, Object>> getSearchResourceMapList() {
		return searchResourceMapList;
	}
	public void setSearchResourceMapList(
			List<Map<String, Object>> searchResourceMapList) {
		this.searchResourceMapList = searchResourceMapList;
	}
	public String getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getConditionString() {
		return conditionString;
	}
	public void setConditionString(String conditionString) {
		this.conditionString = conditionString;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getCurrentEntityId() {
		return currentEntityId;
	}
	public void setCurrentEntityId(String currentEntityId) {
		this.currentEntityId = currentEntityId;
	}
	public String getCurrentEntityType() {
		return currentEntityType;
	}
	public void setCurrentEntityType(String currentEntityType) {
		this.currentEntityType = currentEntityType;
	}
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
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
	public Map<String, Object> getAddedResMap() {
		return addedResMap;
	}
	public void setAddedResMap(Map<String, Object> addedResMap) {
		this.addedResMap = addedResMap;
	}
	public Map<String, Object> getNullableMap() {
		return nullableMap;
	}
	public void setNullableMap(Map<String, Object> nullableMap) {
		this.nullableMap = nullableMap;
	}
	public Map<String, Object> getAttrLengthMap() {
		return attrLengthMap;
	}
	public void setAttrLengthMap(Map<String, Object> attrLengthMap) {
		this.attrLengthMap = attrLengthMap;
	}
	public String getParentEntityTypeGrp() {
		return parentEntityTypeGrp;
	}
	public void setParentEntityTypeGrp(String parentEntityTypeGrp) {
		this.parentEntityTypeGrp = parentEntityTypeGrp;
	}
	public String getAddedResEntityType() {
		return addedResEntityType;
	}
	public void setAddedResEntityType(String addedResEntityType) {
		this.addedResEntityType = addedResEntityType;
	}
	public String getAddedResParentEntityType() {
		return addedResParentEntityType;
	}
	public void setAddedResParentEntityType(String addedResParentEntityType) {
		this.addedResParentEntityType = addedResParentEntityType;
	}
	
	public long getAddedResParentEntityId() {
		return addedResParentEntityId;
	}
	public void setAddedResParentEntityId(long addedResParentEntityId) {
		this.addedResParentEntityId = addedResParentEntityId;
	}
	public NetworkResourceMaintenanceService getNetworkResourceMaintenanceService() {
		return networkResourceMaintenanceService;
	}
	public void setNetworkResourceMaintenanceService(
			NetworkResourceMaintenanceService networkResourceMaintenanceService) {
		this.networkResourceMaintenanceService = networkResourceMaintenanceService;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getBizModule() {
		return bizModule;
	}
	public void setBizModule(String bizModule) {
		this.bizModule = bizModule;
	}
	public String getCurrentEntityLabel() {
		return currentEntityLabel;
	}
	public void setCurrentEntityLabel(String currentEntityLabel) {
		this.currentEntityLabel = currentEntityLabel;
	}
	public Map<String, Object> getCurrentEntityMap() {
		return currentEntityMap;
	}
	public void setCurrentEntityMap(Map<String, Object> currentEntityMap) {
		this.currentEntityMap = currentEntityMap;
	}
	public String getParentEntityType() {
		return parentEntityType;
	}
	public void setParentEntityType(String parentEntityType) {
		this.parentEntityType = parentEntityType;
	}
	public String getParentEntityId() {
		return parentEntityId;
	}
	public void setParentEntityId(String parentEntityId) {
		this.parentEntityId = parentEntityId;
	}
	public Map<String, Object> getParentEntityMap() {
		return parentEntityMap;
	}
	public void setParentEntityMap(Map<String, Object> parentEntityMap) {
		this.parentEntityMap = parentEntityMap;
	}
	public String getUpdatedEntityType() {
		return updatedEntityType;
	}
	public void setUpdatedEntityType(String updatedEntityType) {
		this.updatedEntityType = updatedEntityType;
	}
	public String getBizProcessCode() {
		return bizProcessCode;
	}
	public void setBizProcessCode(String bizProcessCode) {
		this.bizProcessCode = bizProcessCode;
	}
	public String getBizProcessId() {
		return bizProcessId;
	}
	public void setBizProcessId(String bizProcessId) {
		this.bizProcessId = bizProcessId;
	}
	public String getNewParentResEntityId() {
		return newParentResEntityId;
	}
	public void setNewParentResEntityId(String newParentResEntityId) {
		this.newParentResEntityId = newParentResEntityId;
	}
	public String getNewParentResEntityType() {
		return newParentResEntityType;
	}
	public void setNewParentResEntityType(String newParentResEntityType) {
		this.newParentResEntityType = newParentResEntityType;
	}
	public String getOldParentResEntityId() {
		return oldParentResEntityId;
	}
	public void setOldParentResEntityId(String oldParentResEntityId) {
		this.oldParentResEntityId = oldParentResEntityId;
	}
	public String getOldParentResEntityType() {
		return oldParentResEntityType;
	}
	public void setOldParentResEntityType(String oldParentResEntityType) {
		this.oldParentResEntityType = oldParentResEntityType;
	}
	public List<Map<String, Object>> getParentMapList() {
		return parentMapList;
	}
	public void setParentMapList(List<Map<String, Object>> parentMapList) {
		this.parentMapList = parentMapList;
	}
	public String getForwardType() {
		return forwardType;
	}
	public void setForwardType(String forwardType) {
		this.forwardType = forwardType;
	}
	public StaffOrganizationService getStaffOrganizationService() {
		return staffOrganizationService;
	}
	public void setStaffOrganizationService(
			StaffOrganizationService staffOrganizationService) {
		this.staffOrganizationService = staffOrganizationService;
	}
	public String getCityAreaId() {
		return cityAreaId;
	}
	public void setCityAreaId(String cityAreaId) {
		this.cityAreaId = cityAreaId;
	}
	public String getDistrictAreaId() {
		return districtAreaId;
	}
	public void setDistrictAreaId(String districtAreaId) {
		this.districtAreaId = districtAreaId;
	}
	public String getStreetAreaId() {
		return streetAreaId;
	}
	public void setStreetAreaId(String streetAreaId) {
		this.streetAreaId = streetAreaId;
	}
	
	
	
	
	
}
