package com.iscreate.op.action.networkresourcemanage;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.outlinking.OutLinkingService;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageUtil;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
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

public class NewNetworkresourceOperaActionForMobile {

	private  static final  Log log = LogFactory.getLog(NewNetworkresourceOperaActionForMobile.class);
	private StructureCommonService structureCommonService;
	//数据字典
	private Dictionary dictionary;
	
	
	private QuickSort<Map<String,Object>> quickSort;//排序
	
	private XMLAEMLibrary moduleLibrary;
	
	//获取资源基本信息
	public void loadNewNetWorkResourceInfoActionForMobile(){
		log.info("进入loadNetWorkResourceInfoActionForMobile方法，获取资源基本信息");
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
		String pId = "";
		String pType = "";
		
		if(buriedlineSectionEntity == null){
			buriedlineSectionEntity = ActionHelper.getApplicationEntity(entityType);
		}else{
			ApplicationEntity[] parentApps = this.structureCommonService.getStrutureSelationsApplicationEntity(buriedlineSectionEntity, AssociatedType.PARENT, "networkresourcemanage");
			if(parentApps!=null&&parentApps.length>0){
				pId = parentApps[0].getValue("id")+"";
				pType = parentApps[0].getValue("_entityType")+"";
			}
		}
		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap = ResourceCommon.applicationEntityConvertMap(buriedlineSectionEntity);
		infoMap = ResourceCommon.mapRecombinationToString(infoMap);
		List<BasicEntity> entry1 = null;
		String chineseName="";
		try {
			entry1 = dictionary.getEntry(entityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
			if(entry1 != null && !entry1.isEmpty()) {
				chineseName = entry1.get(0).getValue("display").toString();
			}
		} catch (EntryOperationException e) {
			log.error("loadNewNetWorkResourceInfoActionForMobile,获取"+entityType+"中文字典失败，可能该字典不存在");
			e.printStackTrace();
			chineseName = entityType;
		}
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
				infoMapChineseMap.put(key, key);
				orderIdMap.put(key,key);
			}
		}
		Map<String,Object> sortedMap = quickSort.sortMap(infoMap,orderIdMap);
		if(sortedMap!=null){
			infoMap=sortedMap;
		}
		Map<String,Object> aetMap=null;
		Map<String,Object> childMap=null;
		String code = getNetworkResourceUrl();
		if("Room".equals(entityType)){
			String[] aetgs = {"ResGroup_4_Wireless_Flattening","ResGroup_4_Power_Flattening","ResGroup_4_EvironmentAndMonitoring","ResGroup_4_Transmission","ResGroup_4_IndoorCover","ResGroup_4_WLAN"};
			aetMap = new LinkedHashMap<String,Object>();
			aetMap.put("ResGroup_4_Wireless_Flattening","无线");
			aetMap.put("ResGroup_4_Power_Flattening","动力");
			aetMap.put("ResGroup_4_EvironmentAndMonitoring","环境监控");
			aetMap.put("ResGroup_4_Transmission","传输");
			aetMap.put("ResGroup_4_IndoorCover","室分");
			aetMap.put("ResGroup_4_WLAN","WLAN");
			childMap = new HashMap<String,Object>();
			for(String aetg:aetgs){
				String[] aets = this.structureCommonService.getAetNameOfAetg(aetg,"networkresourcemanage");
				if(aets!=null&&aets.length>0){
					List<Map<String,Object>>  rList = new ArrayList<Map<String,Object>>();
					for(String aet:aets){
						if(aet.equals("Room")){
							continue;
						}
						ApplicationEntity[] apps = this.structureCommonService.getAppArrsByRecursionForSrcSameType(buriedlineSectionEntity, aet,"networkresourcemanage");
						if(apps!=null && apps.length>0){
							for(ApplicationEntity ap:apps){
								rList.add(ap.toMap());
							}
						}
					}
					if(rList!=null && rList.size()>0){
						quickSort.sort(rList, "name");
						childMap.put(aetg,rList);
					}else{
						childMap.put(aetg,null);
					}
				}
				
			}	
			ApplicationEntity[] apps = this.structureCommonService.getAppArrsByRecursionForSrcSameType(buriedlineSectionEntity, "Photo","networkresourcemanage");
			if(apps!=null && apps.length>0){
				List<Map<String,Object>>  rList = new ArrayList<Map<String,Object>>();
				String  savePath = ServletActionContext.getServletContext().getRealPath("");
				for(ApplicationEntity ap:apps){
					Map<String,Object> map = ap.toMap();
					String path =  savePath+"/upload/"+map.get("uuidname");
					map.put("uuidname", code+"upload/"+map.get("uuidname"));
					map.put("size", getFileSize(path));
					rList.add(map);
				}
				if(rList!=null && rList.size()>0){
					quickSort.sort(rList, "name");
					childMap.put("Photo",rList);
				}else{
					childMap.put("Photo",null);
				}
			}
		}else{
			String[] aets = this.structureCommonService.getAssociatedAetName(entityType, AssociatedType.CHILD, "networkresourcemanage");
			if(aets!=null&&aets.length>0){
				aetMap = new HashMap<String,Object>();
				childMap = new HashMap<String,Object>();
				for(String aet:aets){
					/*if("Photo".equals(aet)){
						continue;
					}*/
					List<BasicEntity> entr = null;
					String aetName = "";
					try {
						entr = dictionary.getEntry(aet + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						if(entr != null && !entr.isEmpty()) {
							aetName = entr.get(0).getValue("display").toString();
							aetMap.put(aet, aetName);
						}else{
							aetMap.put(aet, aet);
						}
					} catch (EntryOperationException e) {
						log.error("loadNewNetWorkResourceInfoActionForMobile,获取"+aet+"中文字典失败，可能该字典不存在");
						e.printStackTrace();
						aetMap.put(aet, aet);
					}
					//ApplicationEntity[] apps = this.structureCommonService.getAppArrsByRecursionForSrcSameType(buriedlineSectionEntity, aet,"networkresourcemanage");
					ApplicationEntity[] apps = this.structureCommonService.getStrutureSelationsApplicationEntity(buriedlineSectionEntity, aet, AssociatedType.CHILD, "networkresourcemanage");
					if(apps!=null && apps.length>0){
						List<Map<String,Object>>  rList = new ArrayList<Map<String,Object>>();
						String  savePath = ServletActionContext.getServletContext().getRealPath("");
						for(ApplicationEntity ap:apps){
							if("Photo".equals(aet)){
								Map<String,Object> map = ap.toMap();
								String path =  savePath+"/upload/"+map.get("uuidname");
								map.put("uuidname", code+"upload/"+map.get("uuidname"));
								map.put("size", getFileSize(path));
								rList.add(map);
							}else{
								rList.add(ap.toMap());
							}
							
						}
						if(rList!=null && rList.size()>0){
							quickSort.sort(rList, "name");
							childMap.put(aet,rList);
						}else{
							childMap.put(aet,null);
						}
					}
					
				}
			}
		}
		
		Map<String, String> resourceMsgResultMap = new HashMap<String, String>();
		resourceMsgResultMap.put("resourceMsgChinese", gson.toJson(infoMapChineseMap));
		resourceMsgResultMap.put("resourceMsg", gson.toJson(infoMap));
		resourceMsgResultMap.put("chineseName", gson.toJson(chineseName));
		resourceMsgResultMap.put("aetMap", gson.toJson(aetMap));
		resourceMsgResultMap.put("childMap", gson.toJson(childMap));
		resourceMsgResultMap.put("pId", gson.toJson(pId));
		resourceMsgResultMap.put("pType", gson.toJson(pType));
		mch.addGroup("resourceMsgs", resourceMsgResultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		//返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			log.error("退出loadNewNetWorkResourceInfoActionForMobile方法，返回结果"+resultPackageJsonStr+"失败");
			e.printStackTrace();
		}
		log.info("退出loadNewNetWorkResourceInfoActionForMobile方法,返回结果"+resultPackageJsonStr);
	}
	/**
	 * 
	 * @description: 获取下级资源
	 * @author：     
	 * @return void     
	 * @date：Feb 28, 2013 11:10:21 AM
	 */
	public  void getChildResourcesForActionForMobile(){
		log.info("进入getChildResourcesForActionForMobile方法，获取下级资源");
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
		ApplicationEntity appEntity = structureCommonService.getSectionEntity(entityType, id);	
		Map<String,Object> aetMap = null;
		Map<String,Object> childMap = null;
		Map<String,Object> infoMap = null;
		if(appEntity!=null){
			infoMap = appEntity.toMap();
			List<BasicEntity> entry1 = null;
			String chineseName="";
			try {
				entry1 = dictionary.getEntry(entityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
				if(entry1 != null && !entry1.isEmpty()) {
					chineseName = entry1.get(0).getValue("display").toString();
				}
			} catch (EntryOperationException e) {
				log.error("loadNewNetWorkResourceInfoActionForMobile,获取"+entityType+"中文字典失败，可能该字典不存在");
				e.printStackTrace();
				chineseName = entityType;
			}
			infoMap.put("chineseName", chineseName);
			String[] aets = this.structureCommonService.getAssociatedAetName(entityType, AssociatedType.CHILD, "networkresourcemanage");
			if(aets!=null&&aets.length>0){
				aetMap = new HashMap<String,Object>();
				childMap = new HashMap<String,Object>();
				for(String aet:aets){
					if("Photo".equals(aet)){
						continue;
					}
					List<BasicEntity> entr = null;
					String aetName = "";
					try {
						entr = dictionary.getEntry(aet + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						if(entr != null && !entr.isEmpty()) {
							aetName = entr.get(0).getValue("display").toString();
							aetMap.put(aet, aetName);
						}else{
							aetMap.put(aet, aet);
						}
					} catch (EntryOperationException e) {
						log.error("loadNewNetWorkResourceInfoActionForMobile,获取"+aet+"中文字典失败，可能该字典不存在");
						e.printStackTrace();
						aetMap.put(aet, aet);
					}
					//ApplicationEntity[] apps = this.structureCommonService.getAppArrsByRecursionForSrcSameType(appEntity, aet,"networkresourcemanage");
					ApplicationEntity[] apps = this.structureCommonService.getStrutureSelationsApplicationEntity(appEntity, aet, AssociatedType.CHILD, "networkresourcemanage");
					if(apps!=null && apps.length>0){
						List<Map<String,Object>>  rList = new ArrayList<Map<String,Object>>();
						for(ApplicationEntity ap:apps){
							rList.add(ap.toMap());
						}
						if(rList!=null && rList.size()>0){
							quickSort.sort(rList, "name");
							childMap.put(aet,rList);
						}else{
							childMap.put(aet,null);
						}
					}
					
				}
			}
		}
		
		Map<String, String> resourceMsgResultMap = new HashMap<String, String>();
		resourceMsgResultMap.put("resourceMsg", gson.toJson(infoMap));
		resourceMsgResultMap.put("aetMap", gson.toJson(aetMap));
		resourceMsgResultMap.put("childMap", gson.toJson(childMap));
		mch.addGroup("resourceMsgs", resourceMsgResultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		//返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			log.error("退出getChildResourcesForActionForMobile方法，返回结果"+resultPackageJsonStr+"失败");
			e.printStackTrace();
		}
		log.info("退出getChildResourcesForActionForMobile方法,返回结果"+resultPackageJsonStr);
	}
	
	/**
	 * 
	 * @description: 删除资源
	 * @author：     
	 * @return void     
	 * @date：Jun 28, 2012 9:17:13 PM
	 */
	public void deleteResourceNewActionForMobile(){
		log.info("进入deleteResourceNewActionForMobile()，删除资源");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
				
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
				
		MobilePackage  mobilePackage = MobilePackageUtil.getMobilePackage ();
				
		// mobilePackage为空，返回错误信息
		if(mobilePackage == null) {
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage.setResult("error");
			// 返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(newMobilePackage);
			try {
				log.info("退出deleteResourceNewActionForMobile()，返回结果"+resultPackageJsonStr);
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e) {
				log.error("退出deleteResourceNewActionForMobile()，发生异常，返回结果"+resultPackageJsonStr+"失败");
				e.printStackTrace();
			}
		}		
		String content = mobilePackage.getContent();
				
		MobileContentHelper mch = new MobileContentHelper();
		mch.setContent(content);
		// 获取表单提交的JSON信息的Map
		Map<String, String> contentMap = mch.getGroupByKey ("request");
		String currentEntityId="";
		String currentEntityType="";
		Map<String,String> resultMap = new HashMap<String,String>();
		if(contentMap != null) {
			String currentIdsAndTypes = contentMap.get("currentIdsAndTypes");
			if(currentIdsAndTypes!=null && !"".equals(currentIdsAndTypes)){
				String[] idsAndTypesArray = currentIdsAndTypes.split(",");
				int status=0;
				if(idsAndTypesArray!=null && idsAndTypesArray.length>0){
					for(String idAndType:idsAndTypesArray){	
						currentEntityType = idAndType.substring(idAndType.indexOf("#")+1);
						currentEntityId = idAndType.substring(0,idAndType.indexOf("#"));
						BasicEntity chooseResEntity = structureCommonService.getSectionEntity(currentEntityType,currentEntityId);
						ApplicationEntity chooseResAppEntity = ApplicationEntity
						.changeFromEntity(chooseResEntity);
						if (chooseResAppEntity != null) {
							status=structureCommonService.delEntityByRecursion(
							chooseResAppEntity, "networkresourcemanage");
						}else{
							status=0;
						}
					}
				}
				if(status>0){
					resultMap.put("msg","success");
				}else{
					resultMap.put("msg","error");
				}
			}else{
				resultMap.put("msg","error");
			}
		}else{
			resultMap.put("msg","error");
		}
		mch = new MobileContentHelper();	
		mch.addGroup ("title", resultMap);
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		// 返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			log.info("退出deleteResourceNewActionForMobile()，返回结果"+resultPackageJsonStr);
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			log.error("退出deleteResourceNewActionForMobile()，发生异常，返回结果"+resultPackageJsonStr+"失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据环境代码获取对应的网络资源链接
	 * @return
	 */
	public String getNetworkResourceUrl(){
		String code = "";
		OutLinkingService outLinking = new OutLinkingService();
		code = outLinking.getUrlByProjService("ops");
		return code;
	}
	/**
	 * 
	 * @description: 取得文件大小
	 * @author：
	 * @return     
	 * @return String     
	 * @date：Mar 1, 2013 4:16:15 PM
	 */
	public String getFileSize(String path){
		File f = new File(path);   
        try{   
          FileInputStream fis = new FileInputStream(f) ;   
          try{    
        	  String result = String.valueOf(fis.available()/1000)+"k";
        	  return result;
          }catch(IOException e1){   
        	  log.error("获取"+path+"文件大小失败，IO流Exception,返回结果文件大小：0k");
        	  e1.printStackTrace();   
        	  return "0k";
          }   
      }catch(FileNotFoundException e2){   
          log.error("获取"+path+"文件大小失败，文件找不到，返回结果文件大小：0k");
          e2.printStackTrace();
          return "0k";
      }   

	}
	//上传照片并保存
	public void addPhotoResourceActionForMoblie(){
		log.info("进入 addPhotoResourceActionForMoblie,上传照片并保存");
		try {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			
			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				// 返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				try {
					log.info("退出 addPhotoResourceActionForMoblie，返回结果"+resultPackageJsonStr);
					response.getWriter().write(resultPackageJsonStr);
				} catch (IOException e) {
					log.error("退出 addPhotoResourceActionForMoblie，发生异常，返回结果"+resultPackageJsonStr+"失败");
					e.printStackTrace();
				}
			}
			
			String fileStr = mobilePackage.getFileData();
			String picture = "";
			if(fileStr!=null && !fileStr.trim().equals(""))
			{
				String prefixPath = ServletActionContext.getServletContext().getRealPath("");
				String suffixPath = "/upload";
				picture = MobilePackageUtil.copyPhoto(prefixPath,suffixPath, fileStr.trim());
				picture = picture.replace("/upload/", "");
			}
			Map<String,String> resultMap = new HashMap<String,String>();
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String currentEntityId = formJsonMap.get("currentEntityId");
			String currentEntityType = formJsonMap.get("currentEntityType");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(currentEntityId!=null && !"".equals(currentEntityId)){
				//获取图片的父级entity
				BasicEntity imgParentEntity = this.structureCommonService.getSectionEntity(currentEntityType, currentEntityId);
				ApplicationEntity photoParentAppEntity = ApplicationEntity.changeFromEntity(imgParentEntity);
				
				ApplicationEntity photoAppEntity = moduleLibrary.getModule("Photo").createApplicationEntity();
				photoAppEntity.setValue("id", structureCommonService.getEntityPrimaryKey("Photo"));
				photoAppEntity.setValue("name",currentEntityType+"-"+sdf.format(new Date()) );
				photoAppEntity.setValue("uuidname", picture);
				/*用structure保存ae*/
				structureCommonService.saveInfoEntity(photoAppEntity, "networkresourcemanage");
				int status = structureCommonService.createAssociatedRelation(photoParentAppEntity, photoAppEntity, AssociatedType.CLAN, "networkresourcemanage");
				if(status>0){
					resultMap = new HashMap<String,String>();
					resultMap.put("result","success");
					resultMap.put("photoUrl", this.getNetworkResourceUrl()+"upload/"+picture);
				}else{
					resultMap = new HashMap<String,String>();
					resultMap.put("result","error");
					resultMap.put("photoUrl", "");
				}
			}else{
				resultMap = new HashMap<String,String>();
				resultMap.put("result","error");
				resultMap.put("photoUrl", "");
				
			}
			mch = new MobileContentHelper();
			mch.addGroup ("title", resultMap);
			mobilePackage.setResult("success");
			mobilePackage.setContent(mch.mapToJson ());
			// 返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(mobilePackage);
			try {
				log.info("退出addPhotoResourceActionForMoblie，返回结果"+resultPackageJsonStr);
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e) {
				log.error("退出addPhotoResourceActionForMoblie，发生异常，返回结果"+resultPackageJsonStr+"失败");
				e.printStackTrace();
			}
		}catch(Exception e){
			log.error("退出addPhotoResourceActionForMoblie，发生异常");
			e.printStackTrace();
		}
			
	}
	
	
	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}
	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
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
	public XMLAEMLibrary getModuleLibrary() {
		return moduleLibrary;
	}
	public void setModuleLibrary(XMLAEMLibrary moduleLibrary) {
		this.moduleLibrary = moduleLibrary;
	}
	
}
