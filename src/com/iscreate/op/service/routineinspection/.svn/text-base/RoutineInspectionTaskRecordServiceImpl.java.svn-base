package com.iscreate.op.service.routineinspection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.dao.routineinspection.RoutineInspectionPlanDao;
import com.iscreate.op.dao.routineinspection.RoutineInspectionTaskDao;
import com.iscreate.op.dao.routineinspection.RoutineInspectionTaskRecordDao;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionExample;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionPlanworkorder;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionRecordExample;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionRecordTemplate;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorder;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;

public class RoutineInspectionTaskRecordServiceImpl implements RoutineInspectionTaskRecordService{

	private Log log = LogFactory.getLog(this.getClass());
	private RoutineInspectionTaskRecordDao routineInspectionTaskRecordDao;
	private RoutineInspectionTaskDao routineInspectionTaskDao;
	private RoutineInspectionPlanDao routineInspectionPlanDao;
	private NetworkResourceInterfaceService networkResourceService;
	private String className = "com.iscreate.op.service.routineinspection.RoutineInspectionTaskRecordServiceImpl";
	
	/**
	 * 根据专业和任务单ID获取巡检资源
	 * 巡检任务详细信息里的巡检指标信息
	 * @param profession
	 * @return
	 */
	public Map<String, List<Map<String,String>>> getRoutineInspectionResourceByProfessionAndToIdService(String profession,String toId,String terminalType){
		log.info("进入getRoutineInspectionResourceByProfessionAndToIdService方法");
		log.info("参数profession="+profession+",toId="+toId);
		Map<String, List<Map<String,String>>> fullMap = new HashMap<String, List<Map<String,String>>>();
		//根据任务单Id获取任务
		RoutineinspectionTaskorder routineInspectionTaskByToId = this.routineInspectionTaskDao.getRoutineInspectionTaskByToId(toId);
		if(routineInspectionTaskByToId!=null){
			String woId = routineInspectionTaskByToId.getRoutineinspectionWoId();
			RoutineinspectionPlanworkorder planWorkorder = this.routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId(woId);
			if(planWorkorder==null){
				log.error(this.className+"类里的getRoutineInspectionResourceByProfessionAndToIdService方法获取工单失败，工单号为："+woId);
				return fullMap;
			}
			long templateId = planWorkorder.getTemplateId();
			String reId = routineInspectionTaskByToId.getResourceId();
			String reType = routineInspectionTaskByToId.getResourceType();
			profession = routineInspectionTaskByToId.getRoutineInspectionProfession();
			if(profession==null || "".equals(profession)){
				//环境监控
				profession = "FlatNavigation_4_Room_4_EvironmentAndMonitoring";
			}else{
				profession += ",FlatNavigation_4_Room_4_EvironmentAndMonitoring";
			}
			
			//根据专业、资源Id和资源类型获取设备
			Map<String, List<Map<String,String>>> map = this.networkResourceService.getEquipmentByProfessionAndReIdAndReTypeService(profession, reId, reType);
			if(map!=null && map.size()>0){
				for (String professionId : map.keySet()) {
					String professionName = "";
					if("ResGroup_4_Power_Flattening".equals(professionId)){
						professionName = "动力";
					}else if("ResGroup_4_Wireless_Flattening".equals(professionId)){
						professionName = "无线";
					}else if("FlatNavigation_4_Room_4_Transmission".equals(professionId)){
						professionName = "传输";
					}else if("FlatNavigation_4_Room_4_EvironmentAndMonitoring".equals(professionId)){
						professionName = "环境监控";
					}
					List<Map<String,String>> list = new ArrayList<Map<String,String>>();
					if(map.get(professionId)!=null && map.get(professionId).size()>0){
						Map<String,String> map3 = new HashMap<String, String>();
						for (Map<String, String> map2 : map.get(professionId)) {
							if(map3.containsKey(map2.get("_entityType"))){
								continue;
							}
							map3.put(map2.get("_entityType"), map2.get("_entityType"));
							Map<String,String> subMap = new HashMap<String, String>();
							subMap.put("id", reId);
							subMap.put("type", map2.get("_entityType"));
							subMap.put("name", map2.get("chineseTypeName"));
							subMap.put("eId", map2.get("id"));
							List<RoutineinspectionRecordTemplate> tList = this.routineInspectionTaskRecordDao.getRoutineInspectionTemplateByReIdAndReTypeAndTemplateIdAndTerminalType(reId, map2.get("_entityType"), templateId, terminalType);
							if(tList==null || tList.size()==0){
								continue;
							}
							RoutineinspectionExample example = this.routineInspectionTaskRecordDao.getRoutineInspectionExampleByReIdAndReTypeAndToId(reId, map2.get("_entityType"), toId);
							if(example==null){
								subMap.put("isComplete", "false");
							}else{
								subMap.put("isComplete", "true");
							}
							list.add(subMap);
						}
					}
					fullMap.put(professionName, list);
					if(list==null || list.size()==0){
						fullMap.remove(professionName);
					}
				}
			}
			if(fullMap.containsKey("环境监控")){
				List<Map<String, String>> list = fullMap.get("环境监控");
				Map<String,String> subMap = new HashMap<String, String>();
				subMap.put("id", reId);
				subMap.put("type", reType);
				subMap.put("name", "机房");
				subMap.put("eId", reId);
//					List<RoutineinspectionRecordTemplate> tList = this.routineInspectionTaskRecordDao.getRoutineInspectionTemplateByReIdAndReTypeAndTemplateIdAndTerminalType(reId, reType, templateId, "mobile");
//					if(tList==null || tList.size()==0){
//						continue;
//					}
				RoutineinspectionExample example = this.routineInspectionTaskRecordDao.getRoutineInspectionExampleByReIdAndReTypeAndToId(reId, reType, toId);
				if(example==null){
					subMap.put("isComplete", "false");
				}else{
					subMap.put("isComplete", "true");
				}
				list.add(subMap);
				fullMap.put("环境监控", list);
			}else{
				List<Map<String, String>> list = new ArrayList<Map<String,String>>();
				Map<String,String> subMap = new HashMap<String, String>();
				subMap.put("id", reId);
				subMap.put("type", reType);
				subMap.put("name", "机房");
				subMap.put("eId", reId);
//					List<RoutineinspectionRecordTemplate> tList = this.routineInspectionTaskRecordDao.getRoutineInspectionTemplateByReIdAndReTypeAndTemplateIdAndTerminalType(reId, reType, templateId, "mobile");
//					if(tList==null || tList.size()==0){
//						continue;
//					}
				RoutineinspectionExample example = this.routineInspectionTaskRecordDao.getRoutineInspectionExampleByReIdAndReTypeAndToId(reId, reType, toId);
				if(example==null){
					subMap.put("isComplete", "false");
				}else{
					subMap.put("isComplete", "true");
				}
				list.add(subMap);
				fullMap.put("环境监控", list);
			}
		}
		log.info("执行getRoutineInspectionResourceByProfessionAndToIdService方法成功，实现了”根据专业和任务单ID获取巡检资源“的功能");
		log.info("退出getRoutineInspectionResourceByProfessionAndToIdService方法,返回Map<String, List<Map<String, String>>>");
		return fullMap;
	}

	/**
	 * 根据资源Id资源类型工单Id获取巡检记录模板
	 * @param reId
	 * @param reType
	 * @param woId
	 * @return
	 */
	public List<Map<String,String>> getRoutineInspectionRecordTemplateByReIdAndReTypeAndWoIdService(String reId,String reType,String woId,String terminalType){
		log.info("进入getRoutineInspectionResourceByProfessionAndToIdService方法");
		log.info("参数reId="+reId+",reType="+reType+",woId="+woId);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		//根据工单Id获取工单信息
		RoutineinspectionPlanworkorder planWorkOrder = this.routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId(woId);
		if(planWorkOrder!=null){
			//long templateId = 1;  //测试HardCode
			long templateId = planWorkOrder.getTemplateId();     
			List<RoutineinspectionRecordTemplate> rouRecordTempList = this.routineInspectionTaskRecordDao.getRoutineInspectionTemplateByReIdAndReTypeAndTemplateIdAndTerminalType(reId, reType, templateId, terminalType);
			if(rouRecordTempList!=null && rouRecordTempList.size()>0){
				for (RoutineinspectionRecordTemplate rouRecordTemp : rouRecordTempList) {
					Map<String,String> map = new HashMap<String, String>();
					map.put("id", rouRecordTemp.getId()+"");
					map.put("tag", rouRecordTemp.getTag());
					list.add(map);
				}
			}
		}else{
			log.info("没有该工单="+woId);
		}
		log.info("执行getRoutineInspectionResourceByProfessionAndToIdService方法成功，实现了”根据专业和任务单ID获取巡检资源“的功能");
		log.info("退出getRoutineInspectionResourceByProfessionAndToIdService方法,返回Map<String, List<Map<String, String>>>");
		return list;
	}
	
	/**
	 * 保存巡检实例
	 * @param routineinspectionExample
	 * @return
	 */
	public boolean txSaveRoutineInspectionExampleService(RoutineinspectionExample routineinspectionExample){
		boolean isSuccess = this.routineInspectionTaskRecordDao.saveRoutineInspectionExample(routineinspectionExample);
		if(!isSuccess){
			throw new UserDefinedException(this.className+"类里的txSaveRoutineInspectionRecordService方法保存巡检实例失败");
		}
		return isSuccess;
	}
	
	/**
	 * 保存巡检内容
	 * @param routineinspectionExample
	 * @return
	 */
	public boolean txSaveRoutineInspectionRecordService(RoutineinspectionRecordExample routineinspectionRecordExample){
		boolean isSuccess = this.routineInspectionTaskRecordDao.saveRoutineInspectionRecordExample(routineinspectionRecordExample);
		if(!isSuccess){
			throw new UserDefinedException(this.className+"类里的txSaveRoutineInspectionRecordService方法保存巡检实例失败");
		}
		return isSuccess;
	}
	
	/**
	 * 根据资源Id、资源类型、任务单Id和巡检内容模板Id获取巡检内容实例
	 * @param reId
	 * @param reType
	 * @param toId
	 * @param templateId
	 * @return
	 */
	public RoutineinspectionRecordExample getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdAndTemplateIdService(String reId,String reType,String toId,long templateId){
		RoutineinspectionRecordExample example = this.routineInspectionTaskRecordDao.getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdAndTemplateId(reId, reType, toId, templateId);
		if(example==null){
			log.info(this.className+"类中的getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdAndTemplateIdService获取巡检内容实例为空");
		}
		return example;
	}
	
	/**
	 * 修改巡检内容
	 * @param re
	 */
	public boolean txUpdateRoutineInspectionRecordExampleService(RoutineinspectionRecordExample routineinspectionRecordExample){
		boolean isSuccess = this.routineInspectionTaskRecordDao.updateRoutineInspectionRecordExample(routineinspectionRecordExample);
		if(!isSuccess){
			throw new UserDefinedException(this.className+"类里的txUpdateRoutineInspectionRecordExampleService方法修改巡检实例失败");
		}
		return isSuccess;
	}
	
	/**
	 * 根据资源Id、资源类型、任务单Id获取巡检实例
	 * @param reId
	 * @param reType
	 * @param toId
	 * @return
	 */
	public RoutineinspectionExample getRoutineInspectionExampleByReIdAndReTypeAndToIdService(String reId,String reType,String toId){
		RoutineinspectionExample re = this.routineInspectionTaskRecordDao.getRoutineInspectionExampleByReIdAndReTypeAndToId(reId, reType, toId);
		if(re==null){
			log.info(this.className+"类中的getRoutineInspectionExampleByReIdAndReTypeAndToIdService获取巡检实例为空");
		}
		return re;
	}
	
	/**
	 * 根据资源Id、资源类型、任务单Id获取巡检模板实例
	 * @param reId
	 * @param reType
	 * @param toId
	 * @return
	 */
	public List<Map<String,String>> getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdService(String reId,String reType,String toId){
		List<Map<String,String>> rreList = new ArrayList<Map<String,String>>();
		List<RoutineinspectionRecordExample> list = this.routineInspectionTaskRecordDao.getRoutineinspectionRecordExampleByReIdAndReTypeAndToId(reId, reType, toId);
		if(list==null || list.size()==0){
			log.info(this.className+"类中的getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdService获取巡检模板实例为空");
		}else{
			for (RoutineinspectionRecordExample rre : list) {
				Map<String,String> map = new HashMap<String, String>();
				map.put("templateId", rre.getRecord_temp_id()+"");
				map.put("value", rre.getValue());
				map.put("remark", rre.getRemark());
				rreList.add(map);
			}
		}
		return rreList;
	}
	
	/**
	 * 保存巡检内容
	 * @param reId
	 * @param reType
	 * @param woId
	 * @param toId
	 * @param tempIdJsonStr
	 * @param valueJsonStr
	 * @param remarkJsonStr
	 * @return
	 */
	public boolean txSaveRoutineInspectionRecordService(String reId,String reType,String woId,String toId,String tempIdJsonStr,String valueJsonStr,String remarkJsonStr){
		boolean result = true;
		Gson gson=new Gson();
		List<Map<String,String>> templateIdList = gson.fromJson(tempIdJsonStr, new TypeToken<List<Map<String,String>>>(){}.getType());
		List<Map<String,String>> valueList = gson.fromJson(valueJsonStr, new TypeToken<List<Map<String,String>>>(){}.getType());
		List<Map<String,String>> remarkList = gson.fromJson(remarkJsonStr, new TypeToken<List<Map<String,String>>>(){}.getType());
		if(templateIdList!=null && templateIdList.size()>0){
			for(int i=0; i<templateIdList.size(); i++){
				String templateId = templateIdList.get(i).get(i+"");
				String remark = remarkList.get(i).get(i+"");
				String value = valueList.get(i).get(i+"");
				//if(value!=null && !"".equals(value)){
					boolean isSuccess = false;
					RoutineinspectionRecordExample example = this.getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdAndTemplateIdService(reId, reType, toId, Long.parseLong(templateId));
					if(example==null){
						//保存巡检内容实例
						RoutineinspectionRecordExample routineinspectionRecordExample = new RoutineinspectionRecordExample();
						routineinspectionRecordExample.setReId(reId);
						routineinspectionRecordExample.setRemark(remark);
						routineinspectionRecordExample.setReType(reType);
						routineinspectionRecordExample.setToId(toId);
						routineinspectionRecordExample.setValue(value);
						routineinspectionRecordExample.setRecord_temp_id(Long.parseLong(templateId));
						isSuccess = this.txSaveRoutineInspectionRecordService(routineinspectionRecordExample);
						if(!isSuccess){
							result = isSuccess;
							throw new UserDefinedException(this.className+"类里的saveRoutineInspectionRecordActionForMobile方法保存巡检内容失败");
						}
					}else{
						//修改巡检内容实例
						if(remark!=null && !"".equals(remark)){
							example.setRemark(remark);
						}
						example.setValue(value);
						isSuccess = this.txUpdateRoutineInspectionRecordExampleService(example);
						if(!isSuccess){
							result = isSuccess;
							throw new UserDefinedException(this.className+"类里的saveRoutineInspectionRecordActionForMobile方法修改巡检内容失败");
						}
					}
				//}
			}
			
			//保存巡检实例
			RoutineinspectionExample re = this.getRoutineInspectionExampleByReIdAndReTypeAndToIdService(reId, reType, toId);
			if(re==null){
				RoutineinspectionExample example = new RoutineinspectionExample();
				example.setWoId(woId);
				example.setToId(toId);
				example.setReId(reId);
				example.setReType(reType);
				boolean isSuccess = this.txSaveRoutineInspectionExampleService(example);
				if(!isSuccess){
					result = isSuccess;
					throw new UserDefinedException(this.className+"类里的saveRoutineInspectionRecordActionForMobile方法修改巡检实例失败");
				}
			}
		}
		return result;
	}
	
	public RoutineInspectionTaskRecordDao getRoutineInspectionTaskRecordDao() {
		return routineInspectionTaskRecordDao;
	}
	public void setRoutineInspectionTaskRecordDao(
			RoutineInspectionTaskRecordDao routineInspectionTaskRecordDao) {
		this.routineInspectionTaskRecordDao = routineInspectionTaskRecordDao;
	}
	public NetworkResourceInterfaceService getNetworkResourceService() {
		return networkResourceService;
	}
	public void setNetworkResourceService(
			NetworkResourceInterfaceService networkResourceService) {
		this.networkResourceService = networkResourceService;
	}
	public RoutineInspectionTaskDao getRoutineInspectionTaskDao() {
		return routineInspectionTaskDao;
	}
	public void setRoutineInspectionTaskDao(
			RoutineInspectionTaskDao routineInspectionTaskDao) {
		this.routineInspectionTaskDao = routineInspectionTaskDao;
	}

	public RoutineInspectionPlanDao getRoutineInspectionPlanDao() {
		return routineInspectionPlanDao;
	}

	public void setRoutineInspectionPlanDao(
			RoutineInspectionPlanDao routineInspectionPlanDao) {
		this.routineInspectionPlanDao = routineInspectionPlanDao;
	}
}
