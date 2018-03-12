package com.iscreate.op.action.routineinspection;

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
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.dao.publicinterface.WorkOrderAssnetResourceDao;
import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionExample;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionRecordExample;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.routineinspection.RoutineInspectionTaskRecordService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageCommunicationHelper;
import com.iscreate.plat.mobile.util.MobilePackageUtil;

public class RoutineInspectionTaskRecordActionForMobile {

	private Log log = LogFactory.getLog(this.getClass());
	private RoutineInspectionTaskRecordService routineInspectionTaskRecordService;
	private NetworkResourceInterfaceService networkResourceService;
	private String className = "com.iscreate.op.action.routineinspection.RoutineInspectionTaskRecordActionForMobile";
	private WorkOrderAssnetResourceDao workOrderAssnetResourceDao;
	/**
	 * 根据专业获取巡检资源
	 */
	public void getRoutineInspectionResourceByProfessionActionForMobile(){
		log.info("终端：进入getRoutineInspectionResourceByProfessionActionForMobile方法");
		Gson gson = null;
		HttpServletResponse response = null;
		try {
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				log.info("getRoutineInspectionTaskByToIdActionForMobile方法中的mobilePackage为空，返回错误信息");
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			if(formJsonMap!=null){
				Map<String, String> professionInfo = new HashMap<String, String>();
				String toId = formJsonMap.get("TOID");
				String profession = formJsonMap.get("profession");
				Map<String, List<Map<String, String>>> equipmentInfo = this.routineInspectionTaskRecordService.getRoutineInspectionResourceByProfessionAndToIdService(profession, toId , "mobile");
				if(equipmentInfo!=null && equipmentInfo.size()>0){
					for (String key : equipmentInfo.keySet()) {
						List<Map<String, String>> list = equipmentInfo.get(key);
						if(list!=null && list.size()>0){
							String listString = gson.toJson(list);
							professionInfo.put(key, listString);
						}
					}
				}
				mch.addGroup("professionInfo", professionInfo);
				mobilePackage.setContent(mch.mapToJson());
				MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			}
		} catch (Exception e) {
			log.error("getRoutineInspectionTaskByToIdActionForMobile方法运行时出错");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：成功执行了getRoutineInspectionResourceByProfessionActionForMobile方法，该方法实现了“根据专业获取巡检资源”");
		log.info("退出loadBizMessageForMobileAction方法，返回void");
	}
	
	/**
	 * 获取巡检记录模板
	 */
	public void getRoutineInspectionRecordTemplateActionForMobile(){
		Gson gson = null;
		HttpServletResponse response = null;
		try {
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				log.info(this.className+"类里的getRoutineInspectionRecordTemplateActionForMobile方法中的mobilePackage为空，返回错误信息");
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			if(formJsonMap!=null){
				String woId = formJsonMap.get("WOID");
				String reId = formJsonMap.get("reId");
				String reType = formJsonMap.get("reType");
				List<Map<String,String>> list = this.routineInspectionTaskRecordService.getRoutineInspectionRecordTemplateByReIdAndReTypeAndWoIdService(reId, reType, woId , "mobile");
				String listString = gson.toJson(list);
				Map<String,String> recordTemplateMap = new HashMap<String, String>();
				recordTemplateMap.put("recordTemplate", listString);
				mch.addGroup("recordTemplateArea", recordTemplateMap);
				mobilePackage.setContent(mch.mapToJson());
				MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			}
		} catch (Exception e) {
			log.error(this.className+"类里的getRoutineInspectionRecordTemplateActionForMobile方法运行时出错");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
	}
	
	/**
	 * 获取巡检内容实例
	 */
	public void getRoutineInspectionRecordExampleActionForMobile(){
		Gson gson = null;
		HttpServletResponse response = null;
		try {
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				log.info(this.className+"类里的getRoutineInspectionRecordExampleActionForMobile方法中的mobilePackage为空，返回错误信息");
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			if(formJsonMap!=null){
				String toId = formJsonMap.get("TOID");
				String reId = formJsonMap.get("reId");
				String reType = formJsonMap.get("reType");
				List<Map<String,String>> list = this.routineInspectionTaskRecordService.getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdService(reId, reType, toId);
				String listString = gson.toJson(list);
				Map<String,String> recordTemplateMap = new HashMap<String, String>();
				recordTemplateMap.put("recordExample", listString);
				mch.addGroup("recordExampleArea", recordTemplateMap);
				mobilePackage.setContent(mch.mapToJson());
				MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			}
		} catch (Exception e) {
			log.error(this.className+"类里的getRoutineInspectionRecordExampleActionForMobile方法运行时出错");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
	}
	
	/**
	 * 获取设备
	 */
	public void getEquipmentActionForMobile(){
		log.info("终端：进入getEquipmentActionForMobile方法");
		Gson gson = null;
		HttpServletResponse response = null;
		try {
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				log.info("getEquipmentActionForMobile方法中的mobilePackage为空，返回错误信息");
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			if(formJsonMap!=null){
				String reId = formJsonMap.get("reId");
				String reType = formJsonMap.get("reType");
				String toId = formJsonMap.get("TOID");
				String profession = formJsonMap.get("profession");
				if(toId!=null&&!"".equals(toId)){
					Map<String, String> professionInfo = new HashMap<String, String>();
					Map<String, List<Map<String, String>>> equipmentInfo = this.routineInspectionTaskRecordService.getRoutineInspectionResourceByProfessionAndToIdService(profession, toId , "mobile");
					if(equipmentInfo!=null && equipmentInfo.size()>0){
						for (String key : equipmentInfo.keySet()) {
							List<Map<String, String>> list = equipmentInfo.get(key);
							if(list!=null && list.size()>0){
								String listString = gson.toJson(list);
								professionInfo.put(key, listString);
							}
						}
					}
					mch.addGroup("professionInfo", professionInfo);
				}else if(reId!=null&&!"".equals(reId)){
					String reName = "";
					Map<String,Map<String, String>> resourceByReIdAndReTypeService = this.networkResourceService.getBaseFacilityToMapService(reId,reType);
					if(resourceByReIdAndReTypeService!=null && resourceByReIdAndReTypeService.size()>0){
						reName = resourceByReIdAndReTypeService.get("chineseTypeNameMap").get("chineseTypeName");
					}
					Map<String, String> map = new HashMap<String, String>();
					map.put("reId", reId);
					map.put("reType", reType);
					map.put("reName", reName);
					mch.addGroup("equipment", map);
				}
				mobilePackage.setContent(mch.mapToJson());
				MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			}
		} catch (Exception e) {
			log.error("getEquipmentActionForMobile方法运行时出错");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：成功执行了getEquipmentActionForMobile方法，该方法实现了“获取设备”");
		log.info("退出getEquipmentActionForMobile方法，返回void");
	}
	
	/**
	 * 保存巡检内容
	 */
	public void saveRoutineInspectionRecordActionForMobile(){
		log.info("终端：进入saveRoutineInspectionRecordActionForMobile方法");
		Gson gson = null;
		HttpServletResponse response = null;
		try {
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				log.info(this.className+"类里的saveRoutineInspectionRecordActionForMobile方法中的mobilePackage为空，返回错误信息");
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			if(formJsonMap!=null){
				String templateIdJsonStr = formJsonMap.get("templateId");
				//templateIdJsonStr = "[{\"a\":\"1\"},{\"b\":\"2\"}]";
				String valueJsonStr = formJsonMap.get("value");
				String remarkJsonStr = formJsonMap.get("remark");
				String woId = formJsonMap.get("WOID");
				String toId = formJsonMap.get("TOID");
				String reId = formJsonMap.get("reId");
				String reType = formJsonMap.get("reType");
				
				Gson gson2 = new Gson();
				List<Map<String,String>> templateIdList = gson2.fromJson(templateIdJsonStr, new TypeToken<List<Map<String,String>>>(){}.getType());
				List<Map<String,String>> valueList = gson2.fromJson(valueJsonStr, new TypeToken<List<Map<String,String>>>(){}.getType());
				List<Map<String,String>> remarkList = gson2.fromJson(remarkJsonStr, new TypeToken<List<Map<String,String>>>(){}.getType());
				
				if(templateIdList!=null && templateIdList.size()>0){
					for(int i=0; i<templateIdList.size(); i++){
						String templateId = templateIdList.get(i).get(i+"");
						String remark = remarkList.get(i).get(i+"");
						String value = valueList.get(i).get(i+"");
						//if(value!=null && !"".equals(value)){
							boolean isSuccess = false;
							RoutineinspectionRecordExample example = this.routineInspectionTaskRecordService.getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdAndTemplateIdService(reId, reType, toId, Long.parseLong(templateId));
							if(example==null){
								//保存巡检内容实例
								RoutineinspectionRecordExample routineinspectionRecordExample = new RoutineinspectionRecordExample();
								routineinspectionRecordExample.setReId(reId);
								routineinspectionRecordExample.setRemark(remark);
								routineinspectionRecordExample.setReType(reType);
								routineinspectionRecordExample.setToId(toId);
								routineinspectionRecordExample.setValue(value);
								routineinspectionRecordExample.setReId(reId);
								routineinspectionRecordExample.setRecord_temp_id(Long.parseLong(templateId));
								isSuccess = this.routineInspectionTaskRecordService.txSaveRoutineInspectionRecordService(routineinspectionRecordExample);
								if(!isSuccess){
									throw new UserDefinedException(this.className+"类里的saveRoutineInspectionRecordActionForMobile方法保存巡检内容失败");
								}
							}else{
								//修改巡检内容实例
								if(remark!=null && !"".equals(remark)){
									example.setRemark(remark);
								}
								example.setValue(value);
								isSuccess = this.routineInspectionTaskRecordService.txUpdateRoutineInspectionRecordExampleService(example);
								if(!isSuccess){
									throw new UserDefinedException(this.className+"类里的saveRoutineInspectionRecordActionForMobile方法修改巡检内容失败");
								}
							}
							Map<String,String> map = new HashMap<String, String>();
							map.put("result", isSuccess+"");
							mch.addGroup("isSuccess", map);
						//}
					}
					
					//保存巡检实例
					RoutineinspectionExample re = this.routineInspectionTaskRecordService.getRoutineInspectionExampleByReIdAndReTypeAndToIdService(reId, reType, toId);
					if(re==null){
						RoutineinspectionExample example = new RoutineinspectionExample();
						example.setWoId(woId);
						example.setToId(toId);
						example.setReId(reId);
						example.setReType(reType);
						boolean isSuccess = this.routineInspectionTaskRecordService.txSaveRoutineInspectionExampleService(example);
						if(!isSuccess){
							throw new UserDefinedException(this.className+"类里的saveRoutineInspectionRecordActionForMobile方法修改巡检实例失败");
						}
					}
				}
				
				mobilePackage.setContent(mch.mapToJson());
				MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(this.className+"类里的getEquipmentActionForMobile方法运行时出错");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：成功执行了getEquipmentActionForMobile方法，该方法实现了“获取设备”");
		log.info("退出getEquipmentActionForMobile方法，返回void");
	}
	/**
	 * 
	 * @description:根据工单加载网络资源
	 * @author：yuan.yw     
	 * @return void     
	 * @date：Jun 6, 2013 10:15:26 AM
	 */
	public void loadNetworResourceByRepairWoIdActionForMobile() {
		log.info("进入 loadNetworResourceByRepairWoIdActionForMobile()");
		log.info("loadNetworResourceByRepairWoIdActionForMobile() 根据工单加载网络资源");
		try {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();

			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);

			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			List<Workorderassnetresource> workorderassnetresources =  workOrderAssnetResourceDao.getWorkOrderAssnetResourceRecordDao("woId", woId);
			if(workorderassnetresources!=null&&!workorderassnetresources.isEmpty()){
				//基站信息
				Map associatedAlarmResourceMap = new HashMap();
				List<Map> associatedAlarmResourceList = new ArrayList<Map>();
				for(Workorderassnetresource workorderassnetresource : workorderassnetresources){
					Map<String,String> resourceMap = this.networkResourceService.getResourceByReIdAndReTypeService(workorderassnetresource.getNetworkResourceId()+"", workorderassnetresource.getNetworkResourceType());
					if(resourceMap!=null && !resourceMap.isEmpty()){
						associatedAlarmResourceList.add(resourceMap);
					}
				}
				associatedAlarmResourceMap.put("associatedResource", gson.toJson(associatedAlarmResourceList));
				mch.addGroup("associatedResourceArea",associatedAlarmResourceMap); // 关联的网络资源
				
			}
			mobilePackage.setContent(mch.mapToJson());

			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 loadNetworResourceByRepairWoIdActionForMobile()");
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadNetworResourceByRepairWoIdActionForMobile()");
		} 
	}
	public RoutineInspectionTaskRecordService getRoutineInspectionTaskRecordService() {
		return routineInspectionTaskRecordService;
	}
	public void setRoutineInspectionTaskRecordService(
			RoutineInspectionTaskRecordService routineInspectionTaskRecordService) {
		this.routineInspectionTaskRecordService = routineInspectionTaskRecordService;
	}

	public NetworkResourceInterfaceService getNetworkResourceService() {
		return networkResourceService;
	}

	public void setNetworkResourceService(
			NetworkResourceInterfaceService networkResourceService) {
		this.networkResourceService = networkResourceService;
	}

	public WorkOrderAssnetResourceDao getWorkOrderAssnetResourceDao() {
		return workOrderAssnetResourceDao;
	}

	public void setWorkOrderAssnetResourceDao(
			WorkOrderAssnetResourceDao workOrderAssnetResourceDao) {
		this.workOrderAssnetResourceDao = workOrderAssnetResourceDao;
	}
	
}
