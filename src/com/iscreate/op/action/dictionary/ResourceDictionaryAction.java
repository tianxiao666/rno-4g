package com.iscreate.op.action.dictionary;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.dictionary.ResourceDictionaryService;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.Entity;
import com.iscreate.plat.networkresource.common.tool.QuickSort;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.plat.networkresource.dictionary.SearchScope;


public class ResourceDictionaryAction {
	private Dictionary dictionary;//字典
	private ResourceDictionaryService resourceDictionaryService;//字典service
	private QuickSort<Map<String,Object>> quickSort;//排序
	private StructureCommonService structureCommonService;//公共接口
	
	private String entityType;//资源类型或属性名
	private String attributeType;//资源属性
	
	private String entityChineseType;//资源中文
	private String operateType;//操作字典类型
	
	private List<Map<String,Object>> resultList;//操作结果
	
	//属性中英文显示字典属性
	private String display;
	private String orderID;
	private String isDictionaryType;
	private String dictionaryType;
	private String generalEntryName;
	
	//属性字典属性
	private String mixName;
	private String mixValue;
	private String originalMixName;//原下拉值名称

	
	private static final Log log = LogFactory.getLog(ResourceDictionaryAction.class);
	
	public String getOriginalMixName() {
		return originalMixName;
	}
	public void setOriginalMixName(String originalMixName) {
		this.originalMixName = originalMixName;
	}

	public String getMixName() {
		return mixName;
	}
	public void setMixName(String mixName) {
		this.mixName = mixName;
	}
	public String getMixValue() {
		return mixValue;
	}
	public void setMixValue(String mixValue) {
		this.mixValue = mixValue;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getIsDictionaryType() {
		return isDictionaryType;
	}
	public void setIsDictionaryType(String isDictionaryType) {
		this.isDictionaryType = isDictionaryType;
	}
	public String getDictionaryType() {
		return dictionaryType;
	}
	public void setDictionaryType(String dictionaryType) {
		this.dictionaryType = dictionaryType;
	}
	public String getGeneralEntryName() {
		return generalEntryName;
	}
	public void setGeneralEntryName(String generalEntryName) {
		this.generalEntryName = generalEntryName;
	}
	
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getEntityChineseType() {
		return entityChineseType;
	}
	public void setEntityChineseType(String entityChineseType) {
		this.entityChineseType = entityChineseType;
	}
	
	public String getAttributeType() {
		return attributeType;
	}
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}
	public List<Map<String, Object>> getResultList() {
		return resultList;
	}
	public void setResultList(List<Map<String, Object>> resultList) {
		this.resultList = resultList;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}
	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}
	public QuickSort<Map<String, Object>> getQuickSort() {
		return quickSort;
	}
	public void setQuickSort(QuickSort<Map<String, Object>> quickSort) {
		this.quickSort = quickSort;
	}
	public ResourceDictionaryService getResourceDictionaryService() {
		return resourceDictionaryService;
	}
	public void setResourceDictionaryService(
			ResourceDictionaryService resourceDictionaryService) {
		this.resourceDictionaryService = resourceDictionaryService;
	}
	public Dictionary getDictionary() {
		return dictionary;
	}
	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
		
	/**
	 * 
	 * @description: 取得资源类型列表
	 * @author：     
	 * @return void     
	 * @date：Jun 20, 2012 9:33:49 AM
	 */
	public void getResourceTypeAction(){
		log.info("进入getResourceTypeAction，取得资源类型列表");
		List<String> allTypeList = structureCommonService.getAllEntityTypes("networkresourcemanage");
		this.resultList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> resultList1 = new ArrayList<Map<String,Object>>();
		for (String type : allTypeList) {
			log.info("执行getResourceTypeAction,获取type="+type+"中文字典");
			Map<String, Object> typeMap = new HashMap<String, Object>();
			typeMap.put("typeName",type);
			try {
				List<BasicEntity> entry = null;
				entry = dictionary.getEntry( type+ ",networkResourceDefination" ,SearchScope.OBJECT, "");
				if(entry != null && !entry.isEmpty()) {
					typeMap.put("chineseName",entry.get(0).getValue("display"));
				}
			} catch (EntryOperationException e) {
				log.error("执行getResourceTypeAction,获取"+type+"中文字典失败,字典可能不存在.");
				e.printStackTrace();
				typeMap.put("chineseName","中文未定义");
			}
			resultList1.add(typeMap);
		}
		if(resultList1!=null&&resultList1.size()>0){
			Object[] a = new Object[resultList1.size()];
			int index=0;
			for(Map<String,Object> mp:resultList1){
				a[index] = mp.get("typeName");
				index++;
			}
			Arrays.sort(a);
			for(Object o:a){
				for(Map<String,Object> mp:resultList1){
					if(o.equals(mp.get("typeName"))){
						resultList.add(mp);
					}
				}
			}
		//	quickSort.sort(resultList,"typeName");
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(resultList);
		try {
			log.info("退出getResourceTypeAction,返回结果result："+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.info("退出getResourceTypeAction,返回结果result："+result+"失败");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description: 取得某一资源类型属性
	 * @author：     
	 * @return void     
	 * @date：Jun 20, 2012 9:38:57 AM
	 */
	public void getResourcAttributeAction(){
		log.info("进入getResourcAttributeAction,取得某一资源类型属性");
		ApplicationModule module = ModuleProvider.getModule(this.entityType);
		Map<String,Object> attrMap = module.toMap();
		this.resultList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> resultList1 = new ArrayList<Map<String,Object>>();
		for(String key:attrMap.keySet()){
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap.put("attrName",key);
			resultList1.add(resultMap);
		}
		if(resultList1!=null&&resultList1.size()>0){
			Object[] a = new Object[resultList1.size()];
			int index=0;
			for(Map<String,Object> mp:resultList1){
				a[index] = mp.get("attrName");
				index++;
			}
			Arrays.sort(a);
			for(Object o:a){
				for(Map<String,Object> mp:resultList1){
					if(o.equals(mp.get("attrName"))){
						resultList.add(mp);
					}
				}
			}
			//quickSort.sort(resultList,"attrName");
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(resultList);
		try {
			log.info("退出getResourcAttributeAction,返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getResourcAttributeAction,返回结果result="+result+"失败");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description: 取得通用字典属性名称
	 * @author：     
	 * @return void     
	 * @date：Jun 20, 2012 10:06:34 AM
	 */
	public void getNormalDictionaryNamesAction(){
		log.info("进入getNormalDictionaryNamesAction，取得通用字典属性名称");
		this.resultList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> resultList1 = new ArrayList<Map<String,Object>>();
		List<BasicEntity> entry = resourceDictionaryService.getResourceDictionary("dn", "%,networkResourceValueEmun", "attrDefine", "{''mixValue'':''%''}");
		if(entry!=null){
			for(BasicEntity b: entry){
				String[] dns= b.getValue("dn").toString().split(",");
				if(dns.length>=3){
					continue;
				}
				Map<String,Object> resultMap = new HashMap<String,Object>(); 
				resultMap.put("attrName",b.getValue("rdn"));
				resultList1.add(resultMap);
			}	
		}
		if(resultList1!=null&&resultList1.size()>0){
			Object[] a = new Object[resultList1.size()];
			int index=0;
			for(Map<String,Object> mp:resultList1){
				a[index] = mp.get("attrName");
				index++;
			}
			Arrays.sort(a);
			for(Object o:a){
				for(Map<String,Object> mp:resultList1){
					if(o.equals(mp.get("attrName"))){
						resultList.add(mp);
					}
				}
			}
			//quickSort.sort(resultList,"attrName");
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(resultList);
		try {
			log.info("退出getNormalDictionaryNamesAction，返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getNormalDictionaryNamesAction，返回结果result="+result+"失败");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description: 取得资源Entity中文字典
	 * @author：     
	 * @return void     
	 * @date：Jun 19, 2012 11:39:45 AM
	 */
	public String getResourceEntityDictionaryAction(){
		log.info("进入getResourceEntityDictionaryAction,取得资源Entity中文字典");
		List<BasicEntity> beList = resourceDictionaryService.getResourceDictionary("dn","%,networkResourceDefination","attrDefine","{''display'':''%''}");
		this.resultList = new ArrayList<Map<String,Object>>();
		String entityName="";
		String entityChineseName="";
		if(beList!=null){
			for (BasicEntity b : beList) {
				Map<String, Object> typeMap = new HashMap<String, Object>();
				entityName=b.getValue("rdn");
				entityChineseName=b.getValue("attrDefine");
				entityChineseName=entityChineseName.substring(entityChineseName.indexOf(":")+2,entityChineseName.length()-2);
				typeMap.put("entityName",entityName);
				typeMap.put("entityChineseName",entityChineseName);
				resultList.add(typeMap);
			
			}
		}
		
		if(resultList!=null){
			quickSort.sort(resultList, "entityName");
		}
		log.info("退出getResourceEntityDictionaryAction,返回结果result=success");
		return "success";
	}
	/**
	 * 
	 * @description: 取得资源属性中英文字典
	 * @author：     
	 * @return void     
	 * @date：Jun 19, 2012 11:42:36 AM
	 */
	public String getResourceAttributesDictionaryAction(){
		log.info("进入getResourceAttributesDictionaryAction,取得资源属性中英文字典");
		String entityName = "";
		String attrDefine ="";
		String attributeName ="";
		this.resultList = new ArrayList<Map<String,Object>>();
		List<BasicEntity> beList = resourceDictionaryService.getResourceDictionary("dn","%,networkResourceDefination","attrDefine","{''display'':''%''}");
		if(beList!=null&&beList.size()>0){
			for(BasicEntity be:beList){
				entityName=be.getValue("rdn");
				List<BasicEntity> searchList=resourceDictionaryService.getResourceDictionary("dn","%,"+entityName+",networkResourceDefination","attrDefine","{%''orderID'':''%''}");
				if(searchList!=null&&searchList.size()>0){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("entityName",entityName);
					List<Map<String,Object>> attrList = new ArrayList<Map<String,Object>>();
					for(BasicEntity b:searchList){
						Map<String,Object> attrMap = new HashMap<String,Object>();
						attributeName=b.getValue("rdn");
						attrDefine = b.getValue("attrDefine");
						String[] attrs= attrDefine.substring(1,attrDefine.length()-1).split(",");
						attrMap.put("attributeName",attributeName);
						for(String s:attrs){
							if(s.indexOf("display")>=0){
								attrMap.put("display",s.substring(s.indexOf(":")+2,s.length()-1));
							}else if(s.indexOf("orderID")>=0){
								attrMap.put("orderID",s.substring(s.indexOf(":")+2,s.length()-1));
							}else if(s.indexOf("isDictionaryType")>=0){
								attrMap.put("isDictionaryType",s.substring(s.indexOf(":")+2,s.length()-1));
							}else if(s.indexOf("dictionaryType")>=0){
								attrMap.put("dictionaryType",s.substring(s.indexOf(":")+2,s.length()-1));
							}else if(s.indexOf("generalEntryName")>=0){
								attrMap.put("generalEntryName",s.substring(s.indexOf(":")+2,s.length()-1));
							}
						}
						attrList.add(attrMap);	
					}
					quickSort.sort(attrList,"orderID");
					map.put("attrList",attrList);
					resultList.add(map);
				}	
			}
			quickSort.sort(resultList,"entityName");
		}
		log.info("退出getResourceAttributesDictionaryAction，返回结果result=success");
		return "success";
	}
	/**
	 * 
	 * @description: 取得属性下拉列表Entity级别字典
	 * @author：     
	 * @return void     
	 * @date：Jun 19, 2012 11:45:01 AM
	 */
	public String getResourceEntityDropdownDictionaryAction(){
		log.info("进入getResourceEntityDropdownDictionaryAction,取得属性下拉列表Entity级别字典");
		List<BasicEntity> beList = resourceDictionaryService.getResourceDictionary("dn","%,networkResourceDefination","attrDefine","{''display'':''%''}");
		String entityName = "";
		String typeNames = "";
		this.resultList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> resultList1 = new ArrayList<Map<String,Object>>();
		if(beList!=null){
			for(BasicEntity be:beList){
				entityName= be.getValue("rdn");
				List<BasicEntity> searchList = resourceDictionaryService.getResourceDictionary("dn",entityName+",networkResourceValueEmun","attrDefine","{''attribute'':''%''}");
				if(searchList!=null){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("entityName",entityName);

					List<Map<String,Object>>  attrList = new ArrayList<Map<String,Object>>();
					for(BasicEntity b:searchList){			
						typeNames = b.getValue("attrDefine");
						typeNames = typeNames.substring(typeNames.indexOf(":")+2,typeNames.length()-2);	
						if(typeNames!=null){
							String[] types=typeNames.split(",");
							for(String typeName:types){
								List<BasicEntity> valueList = resourceDictionaryService.getResourceDictionary("dn",typeName+","+entityName+",networkResourceValueEmun","attrDefine","{''mixValue'':''%''}");
								if(valueList!=null&&valueList.size()>0){
									String mixValue=valueList.get(0).getValue("attrDefine");
									if(mixValue!=null){
										String[] mixValueArray = mixValue.substring(mixValue.indexOf(":")+2,mixValue.length()-2).split(",");
										for(String s:mixValueArray){
											Map<String,Object> attrValueMap= new HashMap<String,Object>();
											attrValueMap.put("attributeName",typeName);
											attrValueMap.put("attrValue", s.substring(0,s.indexOf(":")));
											attrValueMap.put("attrName",s.substring(s.indexOf(":")+1,s.length()));
											attrList.add(attrValueMap);
										}
									}
									
								}
							}
						}
						
						
						
					}
					quickSort.sort(attrList,"attributeName");
					map.put("attrList", attrList);
					resultList1.add(map);
				}
			}
			if(resultList1!=null&&resultList1.size()>0){
				Object[] a = new Object[resultList1.size()];
				int index=0;
				for(Map<String,Object> mp:resultList1){
					a[index] = mp.get("entityName");
					index++;
				}
				Arrays.sort(a);
				for(Object o:a){
					for(Map<String,Object> mp:resultList1){
						if(o.equals(mp.get("entityName"))){
							resultList.add(mp);
						}
					}
				}
				//quickSort.sort(resultList,"attrName");
			}
			//quickSort.sort(resultList,"entityName");
		}
		log.info("退出 getResourceEntityDropdownDictionaryAction,返回结果result=success");
		return "success";                                                       
	}
	/**
	 * 
	 * @description: 取得属性下拉列表通用字典
	 * @author：     
	 * @return void     
	 * @date：Jun 19, 2012 11:47:24 AM
	 */
	public String getResourceNormalDropdownDictionaryAction(){	
		log.info("进入getResourceNormalDropdownDictionaryAction,取得属性下拉列表通用字典");
		this.resultList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> resultList1 = new ArrayList<Map<String,Object>>();
		List<BasicEntity> entry = resourceDictionaryService.getResourceDictionary("dn", "%,networkResourceValueEmun", "attrDefine", "{''mixValue'':''%''}");
		if(entry!=null){
			for(BasicEntity b:entry){
				String[] dns = b.getValue("dn").toString().split(",");
				if(dns.length>=3){
					continue;
				}
				String mixValue = b.getValue("attrDefine");
				mixValue = mixValue.substring(mixValue.indexOf(":")+2,mixValue.length()-2);
				String[] mixValueArray = mixValue.split(",");
				if(mixValueArray!=null&&mixValueArray.length>0){
					for(String s:mixValueArray){
						String[] secondSplitArrs = s.split(":");
						if(secondSplitArrs != null && secondSplitArrs.length == 2) {
							Map<String,Object> resultMap = new HashMap<String,Object>(); 
							//System.out.println(secondSplitArrs[1]);
							resultMap.put("attrName", secondSplitArrs[1]);
							resultMap.put("attrValue", secondSplitArrs[0]);
							resultMap.put("attributeName", b.getValue("rdn"));
							resultList1.add(resultMap);
						}
					}
				}
			}
			
		}	
		if(resultList1!=null&&resultList1.size()>0){
			List<Object> cList = new ArrayList<Object>();
			int index=0;
			for(Map<String,Object> mp:resultList1){
				boolean hasFlag = false;
				for(Object o:cList){
					if(mp.get("attributeName").equals(o)){
						hasFlag=true;
					}
				}
				if(!hasFlag){
					cList.add(mp.get("attributeName"));
					index++;
				}
				
			}
			Object[] a = cList.toArray();
			Arrays.sort(a);
			for(Object o:a){
				for(Map<String,Object> mp:resultList1){
					if(o.equals(mp.get("attributeName"))){
						resultList.add(mp);
					}
				}
			}
			//quickSort.sort(resultList,"attrName");
		}
		/*if(resultList!=null){
			quickSort.sort(resultList,"attributeName");
		}*/
		log.info("退出getResourceNormalDropdownDictionaryAction,返回结果result=success");
		return "success";
	}
	/**
	 * 
	 * @description: 更新某个资源字典
	 * @author：     
	 * @return void     
	 * @date：Jun 20, 2012 5:07:09 PM
	 */
	public void updateResourceDictionaryAction(){
		log.info("进入updateResourceDictionaryAction,更新某个资源字典");
		String result="error";
		String dn="";
		if(this.operateType.equals("entityChinese")){//更新entity中英文字典
			dn = this.entityType+",networkResourceDefination";
			Entity entry = new Entity();
			entry.setValue("display", this.entityChineseType);
			try {
				dictionary.replaceEntry(dn,entry);
				result="success";
			} catch (EntryOperationException e) {
				log.error("更新字典"+dn+"失败");
				e.printStackTrace();
			}
		}else if(this.operateType.equals("attributeChinese")){//更新资源属性字典
			dn = this.attributeType+","+this.entityType+",networkResourceDefination";
			Entity entry = new Entity();
			entry.setValue("display", this.display);
			entry.setValue("orderID", this.orderID);
			entry.setValue("isDictionaryType",this.isDictionaryType);
			entry.setValue("dictionaryType", this.dictionaryType);
			entry.setValue("generalEntryName", this.generalEntryName);
			try {
				dictionary.replaceEntry(dn, entry);
				result="success";
			} catch (EntryOperationException e) {
				log.error("更新字典"+dn+"失败");
				e.printStackTrace();
			}
		}else if(this.operateType.equals("attributeEntityDictionary")){//更新属性entity级别字典
			dn=this.attributeType+","+this.entityType+",networkResourceValueEmun";
			try {
				if(dictionary.hasEntry(dn)){
					String mixValues="";
					List<BasicEntity> listEntry=dictionary.getEntry(dn, SearchScope.OBJECT,"");
					if(listEntry!=null&&!listEntry.isEmpty()){
						String dropdownMixVal = listEntry.get(0).getValue("mixValue");
						String[] firstSplitArrs = dropdownMixVal.split(",");
						if(firstSplitArrs != null && firstSplitArrs.length > 0) {
							for(int i=0;i<firstSplitArrs.length;i++) {
								String[] secondSplitArrs = firstSplitArrs[i].split(":");
								if(secondSplitArrs != null && secondSplitArrs.length == 2) {
									if(secondSplitArrs[1].equals(this.originalMixName)){
										mixValues += ","+this.mixValue+":"+this.mixName; 
										if(i<firstSplitArrs.length-1){
											for(int j=i+1;j<firstSplitArrs.length;j++){
												mixValues +=","+firstSplitArrs[j];
											}
										}
										break;
									}
									mixValues +=","+secondSplitArrs[0]+":"+secondSplitArrs[1];
								}
							}
						}
					}
					if(!"".equals(mixValues)){
						mixValues = mixValues.substring(1,mixValues.length());	
						Entity entry = new Entity();
						entry.setValue("mixValue", mixValues);
						dictionary.replaceEntry(dn,entry);
						result="success";
					}
					
				}
			} catch (EntryOperationException e) {
				log.error("更新字典"+dn+"失败");
				e.printStackTrace();
			}
		}else{//更新属性通用字典
			dn=this.attributeType+",networkResourceValueEmun";
			try {
				if(dictionary.hasEntry(dn)){
					String mixValues="";
					List<BasicEntity> listEntry=dictionary.getEntry(dn, SearchScope.OBJECT,"");
					if(listEntry!=null&&!listEntry.isEmpty()){
						String dropdownMixVal = listEntry.get(0).getValue("mixValue");
						String[] firstSplitArrs = dropdownMixVal.split(",");
						if(firstSplitArrs != null && firstSplitArrs.length > 0) {
							for(int i=0;i<firstSplitArrs.length;i++) {
								String[] secondSplitArrs = firstSplitArrs[i].split(":");
								if(secondSplitArrs != null && secondSplitArrs.length == 2) {
									if(secondSplitArrs[1].equals(this.originalMixName)){
										mixValues += ","+this.mixValue+":"+this.mixName; 
										if(i<firstSplitArrs.length-1){
											for(int j=i+1;j<firstSplitArrs.length;j++){
												mixValues +=","+firstSplitArrs[j];
											}
										}
										break;
									}
									mixValues +=","+secondSplitArrs[0]+":"+secondSplitArrs[1];
								}
							}
						}
					}
					if(!"".equals(mixValues)){
						mixValues = mixValues.substring(1,mixValues.length());
						Entity entry = new Entity();
						entry.setValue("mixValue", mixValues);
						dictionary.replaceEntry(dn,entry);
						result="success";
					}
				}
			} catch (EntryOperationException e) {
				log.error("更新字典"+dn+"失败");
				e.printStackTrace();
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		result = gson.toJson(result);
		try {
			log.info("退出updateResourceDictionaryAction,返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出updateResourceDictionaryAction,返回结果result="+result+"失败");
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * @description: 删除资源字典
	 * @author：     
	 * @return void     
	 * @date：Jun 20, 2012 6:03:23 PM
	 */
	public void deleteResourceDictionaryAction(){
		log.info("进入deleteResourceDictionaryAction，删除资源字典");
		String result="error";
		String dn="";
		if(this.operateType.equals("entityChinese")){//entity 中英文字典删除
			dn = this.entityType+",networkResourceDefination";
			try {
				dictionary.removeEntry(dn);
				result="success";
			} catch (EntryOperationException e) {
				log.error("删除字典"+dn+"失败");
				e.printStackTrace();
			}
		}else if(this.operateType.equals("attributeChinese")){//资源属性 字典删除
			dn = this.attributeType+","+this.entityType+",networkResourceDefination";
			try {
				dictionary.removeEntry(dn);
				result="success";
			} catch (EntryOperationException e) {
				log.error("删除字典"+dn+"失败");
				e.printStackTrace();
			}
		}else if(this.operateType.equals("attributeEntityDictionary")){//属性entity级别字典删除
			dn= this.attributeType+","+this.entityType+",networkResourceValueEmun";
			try {
				if(dictionary.hasEntry(dn)){
					String mixValues="";
					List<BasicEntity> listEntry=dictionary.getEntry(dn, SearchScope.OBJECT,"");
					if(listEntry!=null&&!listEntry.isEmpty()){
						String dropdownMixVal = listEntry.get(0).getValue("mixValue");
						String[] firstSplitArrs = dropdownMixVal.split(",");
						if(firstSplitArrs != null && firstSplitArrs.length >= 2) {
							for(int i=0;i<firstSplitArrs.length;i++) {
								String[] secondSplitArrs = firstSplitArrs[i].split(":");
								if(secondSplitArrs != null && secondSplitArrs.length == 2) {
									if(secondSplitArrs[1].equals(this.originalMixName)){
										if(i<firstSplitArrs.length-1){
											for(int j=i+1;j<firstSplitArrs.length;j++){
												mixValues +=","+firstSplitArrs[j];
											}
										}
										break;
									}
									mixValues +=","+secondSplitArrs[0]+":"+secondSplitArrs[1];
								}
							}
							if(!"".equals(mixValues)){
								mixValues = mixValues.substring(1,mixValues.length());
							}
							Entity entry = new Entity();
							entry.setValue("mixValue", mixValues);
							dictionary.replaceEntry(dn,entry);
							result="success";
						}else if(firstSplitArrs != null && firstSplitArrs.length==1){
							dictionary.removeEntry(dn);
							dn=this.entityType+",networkResourceValueEmun";
							List<BasicEntity> be = dictionary.getEntry(dn, SearchScope.OBJECT,"");
							BasicEntity entityA = be.get(0);
							String attribute  =  entityA.getValue("attribute").toString();
							if(attribute.contains(",")){
								if(attribute.contains(this.attributeType)){
									String[] attributes = attribute.split(",");
									String attrs="";
									for(String attr:attributes){
										if(!attr.equals(this.attributeType)){
											attrs += attr+",";
										}
									}
									attrs = attrs.substring(0,attrs.lastIndexOf(","));
									Entity entry = new Entity();
									entry.setValue("attribute", attrs);
									dictionary.replaceEntry(dn,entry);
								}
							}else{
								if(attribute.equals(this.attributeType)){
									dictionary.removeEntry(dn);
								}
							}
							result="success";
						}
					}
					
				}
			} catch (EntryOperationException e) {
				log.error("删除字典"+dn+"失败");
				e.printStackTrace();
			}
		}else{//属性通用字典删除
			dn= this.attributeType+",networkResourceValueEmun";
			try {
				if(dictionary.hasEntry(dn)){
					String mixValues="";
					List<BasicEntity> listEntry=dictionary.getEntry(dn, SearchScope.OBJECT,"");
					if(listEntry!=null&&!listEntry.isEmpty()){
						String dropdownMixVal = listEntry.get(0).getValue("mixValue");
						String[] firstSplitArrs = dropdownMixVal.split(",");
						if(firstSplitArrs != null && firstSplitArrs.length >= 2) {
							for(int i=0;i<firstSplitArrs.length;i++) {
								String[] secondSplitArrs = firstSplitArrs[i].split(":");
								if(secondSplitArrs != null && secondSplitArrs.length == 2) {
									if(secondSplitArrs[1].equals(this.originalMixName)){
										if(i<firstSplitArrs.length-1){
											for(int j=i+1;j<firstSplitArrs.length;j++){
												mixValues +=","+firstSplitArrs[j];
											}
										}
										break;
									}
									mixValues +=","+secondSplitArrs[0]+":"+secondSplitArrs[1];
								}
							}
							if(!"".equals(mixValues)){
								mixValues = mixValues.substring(1,mixValues.length());
							}
							Entity entry = new Entity();
							entry.setValue("mixValue", mixValues);
							dictionary.replaceEntry(dn,entry);
							result="success";
						}else if(firstSplitArrs != null && firstSplitArrs.length==1){
							dictionary.removeEntry(dn);
							result="success";
						}
					}
					
				}
			} catch (EntryOperationException e) {
				log.error("删除字典"+dn+"失败");
				e.printStackTrace();
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		result = gson.toJson(result);
		try {
			log.info("退出deleteResourceDictionaryAction,返回结果result:"+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出deleteResourceDictionaryAction,返回结果result:"+result+"失败");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description: 增加资源字典
	 * @author：     
	 * @return void     
	 * @date：Jun 20, 2012 6:05:52 PM
	 */
	public void addResourceDictionaryAction(){
		log.info("进入addResourceDictionaryAction,增加资源字典");
		String result = "error";
		String dn="";
		if(this.operateType.equals("entityChinese")){//增加entity中英文字典
			dn=this.entityType+",networkResourceDefination";
			try {
				if(!dictionary.hasEntry(dn)){
					Entity entry = new Entity();
					entry.setValue("display",this.entityChineseType);
					dictionary.addEntry(dn,entry);
					result="success";
				}
			} catch (EntryOperationException e) {
				log.error("增加字典"+dn+"失败");
				e.printStackTrace();
			}
		}else if(this.operateType.equals("attributeChinese")){//属性字典增加
			dn=this.attributeType+","+this.entityType+",networkResourceDefination";
			try {
				if(!dictionary.hasEntry(dn)){
					Entity entry = new Entity();
					entry.setValue("display", this.display);
					entry.setValue("orderID", this.orderID);
					entry.setValue("isDictionaryType",this.isDictionaryType);
					entry.setValue("dictionaryType", this.dictionaryType);
					entry.setValue("generalEntryName", this.generalEntryName);
					dictionary.addEntry(dn, entry);
					result ="success";
				}
			} catch (EntryOperationException e) {
				log.error("增加字典"+dn+"失败");
				e.printStackTrace();
			}
		}else if(this.operateType.equals("attributeEntityDictionary")){//属性entity级别字典增加
			dn=this.entityType+",networkResourceValueEmun";
			List<BasicEntity> list = resourceDictionaryService.getResourceDictionary("dn",dn,"attrDefine","{''attribute'':''%''}");
			if(list==null){
				Entity entry = new Entity();
				entry.setValue("attribute",this.attributeType);
				try {
					dictionary.addEntry(dn, entry);
				} catch (EntryOperationException e) {
					//System.out.println("sssss");
					log.error("增加字典"+dn+"失败");
					e.printStackTrace();
				}
			}else{
				try {
					List<BasicEntity> be = dictionary.getEntry(dn, SearchScope.OBJECT,"");
					BasicEntity entityA = be.get(0);
					String attribute  =  entityA.getValue("attribute").toString();
					List<BasicEntity> beList = resourceDictionaryService.getResourceDictionary("dn",dn,"attrDefine","{''attribute'':''%"+this.attributeType+"%''}");
					if(beList==null){
						Entity entry = new Entity();
						entry.setValue("attribute",attribute+","+this.attributeType);
						dictionary.replaceEntry(dn, entry);
					}
				} catch (EntryOperationException e1) {
					log.error("增加字典"+dn+"失败");
					e1.printStackTrace();
				}
				
			}
			
			dn=this.attributeType+","+this.entityType+",networkResourceValueEmun";
			//System.out.println(this.mixName+"----------");
			//System.out.println(this.mixValue+"-------------");
			try {
				if(!dictionary.hasEntry(dn)){
					Entity entry = new Entity();
					entry.setValue("mixValue",this.mixValue+":"+this.mixName);
					dictionary.addEntry(dn, entry);
					result="success";
				}else{
					List<BasicEntity> listEntry=dictionary.getEntry(dn, SearchScope.OBJECT,"");
					if(listEntry!=null&&!listEntry.isEmpty()){
						String dropdownMixVal = listEntry.get(0).getValue("mixValue");
						dropdownMixVal=dropdownMixVal+","+this.mixValue+":"+this.mixName;
						Entity entry = new Entity();
						//System.out.println(dropdownMixVal);
						entry.setValue("mixValue", dropdownMixVal);
						dictionary.replaceEntry(dn,entry);
						result="success";			
					}
				}
			} catch (EntryOperationException e) {
				log.error("增加字典"+dn+"失败");
				e.printStackTrace();
			}
		}else{//属性通用字典增加
			dn=this.attributeType+",networkResourceValueEmun";
			try {
				if(!dictionary.hasEntry(dn)){
					Entity entry = new Entity();
					entry.setValue("mixValue",this.mixValue+":"+this.mixName);
					dictionary.addEntry(dn, entry);
					result="success";
				}else{
					List<BasicEntity> listEntry=dictionary.getEntry(dn, SearchScope.OBJECT,"");
					if(listEntry!=null&&!listEntry.isEmpty()){
						String dropdownMixVal = listEntry.get(0).getValue("mixValue");
						dropdownMixVal=dropdownMixVal+","+this.mixValue+":"+this.mixName;
						Entity entry = new Entity();
						entry.setValue("mixValue", dropdownMixVal);
						dictionary.replaceEntry(dn,entry);
						result="success";			
					}
				}
			} catch (EntryOperationException e) {
				log.error("增加字典"+dn+"失败");
				e.printStackTrace();
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		result = gson.toJson(result);
		try {
			log.info("退出addResourceDictionaryAction,返回结果result："+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出addResourceDictionaryAction,返回结果result："+result+"失败");
			e.printStackTrace();
		}
	}
}
