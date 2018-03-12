package com.iscreate.plat.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.constant.GisDispatchGraphConstant;
import com.iscreate.op.pojo.gisdispatch.LittleIcon;
import com.iscreate.op.pojo.gisdispatch.PicLayer;
import com.iscreate.op.pojo.gisdispatch.PicLayerManager;
import com.iscreate.op.pojo.gisdispatch.PicUnitType;
import com.iscreate.op.pojo.gisdispatch.UserLayerSetting;
import com.iscreate.op.pojo.gisdispatch.UserLayerTypeSetting;
import com.iscreate.op.pojo.gisdispatch.UserLittleIconSetting;

/**
 * 转换对象为json的工具
 * @author gmh
 *
 * 2012-3-23上午10:38:11
 */
public class GisDispatchJSONTools {

	/**
	 * 转换工单为json对象
	 * @param wo
	 * @return
	 * Mar 26, 2012 2:36:17 PM
	 * gmh
	 */
	public static String getWorkOrderJson(Map wo){
		if(wo==null || wo.isEmpty()){
			return "{}";
		}
		StringBuilder json=new StringBuilder();
		json.append("{");
		json.append("'type':'workorder'");//类型为工单
		json.append(",");
		json.append("'WOTYPE':'"+wo.get("woType")+"'");//工单类型
		json.append(",");
		json.append("'WOID':'"+wo.get("woId")+"'");//工单id
		json.append(",");
		json.append("'WOTITLE':'"+wo.get("woTitle")+"'");//工单标题
		json.append(",");
		json.append("'CREATEPERSON':'"+wo.get("creatorName")+"'");//创建人
		json.append(",");
		json.append("'CREATETIME':'"+wo.get("createTime")+"'");//创建时间
		json.append(",");
		json.append("'BIZ_OVERTIME':'"+wo.get("requireCompleteTime")+"'");//要求完成时间
		json.append(",");
		json.append("'OPERATORID':'"+wo.get("currentHandlerName")+"'");//责任人
		json.append(",");
		json.append("'ISREADED':'"+wo.get("isRead")+"'");//0:未读 1:已读 2：未读，状态改变 3：已读，状态改变
		json.append(",");
		json.append("'STATUSNAME':'"+wo.get("statusName")+"'");//状态
		json.append(",");
		json.append("'resourceType':'"+wo.get("resourceType")+"'");//关联资源类型
		json.append(",");
		json.append("'resourceId':'"+wo.get("resourceId")+"'");//关联的基站id
		
		String overTime = (String)wo.get("requireCompleteTime");
		if(overTime!=null && !"".equals(overTime)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date overTime2 = sdf.parse(overTime);
				 Date curDate = new Date();
				int res = curDate.compareTo(overTime2);	//如果小于的话就是-1
				if(res==1){
					//已经超时
					json.append(",'isOver':'true'");
				}else{
					//判断是否快超时
					boolean quickRes = isQuickOverTime(overTime2);
					json.append(",'isQuick':"+quickRes);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//che.yd----------begin-----
		json.append(",");
		json.append("'locationHref':'"+wo.get("formUrl")+"'");
		
		//che.yd----------end-----
		json.append("}");
		
		return json.toString();
	}
	//判断工单是否快超时
	private static boolean isQuickOverTime(Date overTime){
		Date curDate = new Date();
		if(curDate.getYear()==overTime.getYear()&&curDate.getMonth()==overTime.getMonth()&&curDate.getDate()==overTime.getDate()&&curDate.getHours()==overTime.getHours()){
			int overM = overTime.getMinutes();
			int curM = curDate.getMinutes();
			//判断工单结束时间-当前时间是否小于等于30分钟，是则快超时
			if(overM - curM <= 30){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取任务单的json数据
	 * @param to
	 * @return
	 * Mar 26, 2012 2:42:56 PM
	 * gmh
	 */
	public static String getTaskOrderJson(Map to){
		if(to==null || to.isEmpty()){
			return "{}";
		}
		StringBuilder json=new StringBuilder();
		json.append("{");
		json.append("type:'taskorder'");//类型为工单
		json.append(",");
		json.append("TOTYPE:'"+to.get("toType")+"'");//任务单id
		json.append(",");
		json.append("TOID:'"+to.get("toId")+"'");//任务单id
		json.append(",");
		json.append("TASKNAME:'"+to.get("toTitle")+"'");//工单标题
		json.append(",");
		json.append("ASSIGNEDPERSONID:'"+to.get("assignerName")+"'");//创建人
		json.append(",");
		json.append("CREATETIME:'"+to.get("assignTime")+"'");//创建时间，分配时间
		json.append(",");
		json.append("OVERTIME:'"+to.get("requireCompleteTime")+"'");//要求完成时间
		json.append(",");
		json.append("OPERATORID:'"+to.get("currentHandlerName")+"'");//责任人
		json.append(",");
		json.append("STATUSNAME:'"+to.get("statusName")+"'");//状态

		//che.yd----------begin-----
		json.append(",");
		json.append("'ISREADED':'"+to.get("isRead")+"'");//0:未读 1:已读 2：未读，状态改变 3：已读，状态改变
		json.append(",");
		json.append("resourceId:'"+to.get("resourceId")+"'");//关联的基站id
		json.append(",");
		json.append("WOID:'"+to.get("woId")+"'");//工单id
		String overTime = (String)to.get("requireCompleteTime");
		if(overTime!=null && !"".equals(overTime)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date overTime2 = sdf.parse(overTime);
				Date curDate = new Date();
				int res = curDate.compareTo(overTime2);	//如果小于的话就是-1
				if(res==1){
					//已经超时
					json.append(",'isOver':'true'");
				}else{
					//判断是否快超时
					boolean quickRes = isQuickOverTime(overTime2);
					json.append(",'isQuick':"+quickRes);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		json.append(",");
		json.append("locationHref:'"+to.get("formUrl")+"'");
		
		//che.yd--------------end-----
		json.append("}");
		
		return json.toString();
	}
	
	/**
	 * 返回左边树图层管理器json
	 * 
	 * @param plm
	 * @return
	 */
	public static String getLayerTreeOfPicLayerManagerJson(PicLayerManager plm){
		/**
		 * { 
		 *  name:'',
		 *  layerList: 
		 *  [ 
		 *    { 
		 *      name:'', 
		 *      key:'', 
		 *      selected: true/false,
		 *      geTypes: 
		 *      [
		 *        {
		 *          key:'', 
		 *          code:'', name:'', 
		 *          selected:true/false,
		 *          minMile:xxx,
		 *          maxMile:xxx,
		 *          littleIcons: [ { key:'' name:'', selected:true/false } ]
		 *        } 
		 *      ] 
		 *    }
		 *  ] 
		 *}
		 */

		// 准备
		StringBuilder sb = new StringBuilder();
		sb.append("{name:'" + plm.getName() + "',");// layermanager开始
		sb.append("layerList:[");// layerList开始

		// 用户的图层设置信息，关联的图层id为key
		Map<Long, UserLayerSetting> userLayerSettings = plm.getUserLayerSettings();
		
		// 用户的图元类型设置信息，关联的图元类型的id为key
		Map<Long, UserLayerTypeSetting> userLayerTypeSettings =plm.getUserLayerTypeSettings();
		
		// 用户的脚标设置信息，关联的脚标id为key
		Map<Long, UserLittleIconSetting> userLittleIconSettings = plm.getUserLittleIconSettings();
		
		
		//遍历构造用户设置的图层
		if(userLayerSettings!=null && !userLayerSettings.isEmpty()){
			
			
			for(Long layerId:userLayerSettings.keySet()){
				UserLayerSetting uls=userLayerSettings.get(layerId);
				PicLayer pl=uls.getPicLayer();
				sb.append("{");
				sb.append("name:'"+pl.getName()+"',");
				sb.append("key:'"+pl.getKey()+"',");
				sb.append("selected:"+uls.isShowOrNot()+",");
				sb.append("geTypes:");
				sb.append("[");		//图元类型开始
				
				//构造用户设置的图元类型
				List<PicUnitType> puTypeList=pl.getPicUnitTypes();
				if(puTypeList!=null && !puTypeList.isEmpty()){
					for(PicUnitType tempType:puTypeList){
						Long typeId=tempType.getId();
						if(userLayerTypeSettings!=null && !userLayerTypeSettings.isEmpty()){
							UserLayerTypeSetting ults=userLayerTypeSettings.get(typeId);
							PicUnitType put=ults.getPicUnitType();
							sb.append("{");//图元类型开始
							sb.append("key:'"+put.getKey()+"',");
							sb.append("code:'"+put.getCode()+"',");
							sb.append("name:'"+put.getName()+"',");
							sb.append("selected:"+ults.isVisible()+",");
							sb.append("minMile:"+put.getPicUnitTypeMile().getMinVisibleMile()+",");
							sb.append("maxMile:"+put.getPicUnitTypeMile().getMaxVisibleMile()+",");
							sb.append("littleIcons:");
							sb.append("[");		//脚标开始
							
							
							//构造用户设置的图元类型
							List<LittleIcon> littleIconList=put.getLittleIcons();
							if(littleIconList!=null && !littleIconList.isEmpty()){
								for(LittleIcon tempIcon:littleIconList){
									Long iconId=tempIcon.getId();
									if(userLittleIconSettings!=null && !userLittleIconSettings.isEmpty()){
										UserLittleIconSetting userLiSetting=userLittleIconSettings.get(iconId);
										LittleIcon icon=userLiSetting.getLittleIcon();
										sb.append("{");
										sb.append("key:'"+icon.getKey()+"',");
										sb.append("name:'"+icon.getName()+"',");
										sb.append("selected:"+userLiSetting.isShowOrNot());
										sb.append("}");// 脚标结束
										sb.append(",");
//										isExists=true;
									}
								}
								
								sb = sb.delete(sb.length() - 1, sb.length());// 去除最后一个元素的逗号
							}
						}
						sb.append("]");// 脚标结束
						sb.append("}");// 图元类型结束
						sb.append(",");
					}
					sb = sb.delete(sb.length() - 1, sb.length());// 去除最后一个元素的逗号
				}
				sb.append("]");// 图元类型结束
				sb.append("}");// 图层结束
				sb.append(",");// 图层之间的间隔
			}
			sb = sb.delete(sb.length() - 1, sb.length());// 去除最后一个元素的逗号
		}
		
		/*
		// 处理可见图层
		Map<String, PicLayer> visiblePicLayers = plm.getVisiblePicLayers();
		if (visiblePicLayers != null && !visiblePicLayers.isEmpty()) {
			for (String plKey : visiblePicLayers.keySet()) {
				PicLayer pl = visiblePicLayers.get(plKey);
				sb.append(getLeftTreeOfLayerJson(pl));// 获取左边树图层json
				sb.append(",");// 图层之间的间隔
			}

			sb = sb.delete(sb.length() - 1, sb.length());// 去除最后一个逗号
		}
		
		
		Map<String, PicLayer> hiddenPicLayers = plm.getHiddenPicLayers();
		
		//---------------che.yd------begin---------
		if(hiddenPicLayers!=null && !hiddenPicLayers.isEmpty()){
			System.out.println("进来了不可见图层");
			sb.append(",");
			for (String plKey : hiddenPicLayers.keySet()) {
				PicLayer pl = hiddenPicLayers.get(plKey);
				sb.append(getLeftTreeOfLayerJson(pl));// 获取左边树图层json
				sb.append(",");// 图层之间的间隔
			}
			sb = sb.delete(sb.length() - 1, sb.length());// 去除最后一个逗号
		}

		//---------------che.yd------end---------
		 */
		sb.append("]");// layerList结束
		sb.append("}");// 图层管理器结束
		return sb.toString();
	}
	
	
	/**
	 * 获取图层左边树json
	 * 
	 * @return
	 */
	public static String getLeftTreeOfLayerJson(PicLayer pl) {
		StringBuilder sb = new StringBuilder();
		if (pl == null) {
			return "";
		}

		sb.append("{");// 图层开始
		sb.append("name:'" + pl.getName() + "',key:'" + pl.getKey() + "'");
		sb.append(",");
		sb.append("selected:" + pl.isShow());
		sb.append(",");
		sb.append("geTypes:");
		sb.append("[");// 图元类型开始
		// 开始处理图元类型
		List<PicUnitType> putypes = pl.getPicUnitTypes();
		if (putypes != null && !putypes.isEmpty()) {
			for (PicUnitType put : putypes) {
				sb.append("{");// 一个图元类型开始
				sb.append("key:'" + put.getKey() + "'");
				sb.append(",");
				sb.append("code:'" + put.getCode() + "'");
				sb.append(",");
				sb.append("name:'" + put.getName() + "'");
				sb.append(",");
				sb.append("selected:" + put.isNeedShow());
				sb.append(",");
				sb.append("littleIcons:");
				sb.append("[");// 脚标开始
				List<LittleIcon> lis = put.getLittleIcons();
				if (lis != null && lis.size() > 0) {
					for (LittleIcon li : lis) {
						sb.append("{");// 脚标开始
						sb.append("key:'" + li.getIcon() + "'");
						sb.append(",");
						sb.append("name:'" + li.getName() + "'");
						sb.append(",");
						sb.append("selected:" + li.isNeedShow());
						sb.append("}");// 脚标结束
						sb.append(",");

					}

					sb = sb.delete(sb.length() - 1, sb.length());// 去除最后一个逗号
				}
				sb.append("]");// 脚标结束
				sb.append("}");// 一个图元类型结束
				sb.append(",");
			}

			sb = sb.delete(sb.length() - 1, sb.length());// 去除最后一个逗号
		}

		sb.append("]");// 图元类型结束
		sb.append("}");// 图层结束

		return sb.toString();

	}
	/**
	 * 
	 * author:che.yd 把资源信息与资源关联的任务信息，转换成json格式
	 * @return
	 */
	public static String transferResourceAndTaskInfoToJson(Map resourceMap,Map<String, List<Map<String,Object>>> totalMap,Map busyOrNotMap,Map<String,Map> workListMap){

	    
//		String accessUrl=InterfaceUtil.getConfigValueFromProperties("gisShowStationURL");
		String accessUrl = "";
		
		//转换图元基本的信息
		StringBuffer sb=new StringBuffer("");
		if(resourceMap==null || resourceMap.size()==0){
			return sb.toString();
		}
		
		try {
			sb.append("{");
			sb.append("basicInfo:{");
			if(GisDispatchGraphConstant.RESOURCE_STATION.equals(resourceMap.get("resourceType").toString())){	//站址
				sb.append("name:'"+resourceMap.get("objectName")+"',");
				sb.append("address:'"+resourceMap.get("location")+"',");
				sb.append("key:'"+GisDispatchGraphConstant.RESOURCE_STATION+"_"+resourceMap.get("objectIdentity")+"',");
				sb.append("stationAccessUrl:'"+accessUrl+resourceMap.get("objectIdentity")+"',");
				sb.append("rank:'"+resourceMap.get("rank")+"',");
				
				//获取及基站信息
				List<Map<String,String>> baseStationListMap=(List<Map<String,String>>)resourceMap.get("baseStationList");
				
				
				sb.append("baseStationList:[");
				if(baseStationListMap!=null && !baseStationListMap.isEmpty()){
					for(Map<String,String> tempMap:baseStationListMap){
						
						
						String baseStationId=tempMap.get("id");
						String baseStationName=tempMap.get("name");
						String baseStationGrade=tempMap.get("importancegrade");
						String baseStationType=tempMap.get("baseStationType");
						sb.append("{baseStationId:'"+baseStationId+"',");
						sb.append("baseStationName:'"+baseStationName+"',");
						sb.append("baseStationType:'"+baseStationType+"',");
						sb.append("baseStationGrade:'"+baseStationGrade+"'},");
					}
					sb.delete(sb.length()-1, sb.length());
				}
				sb.append("]");
				sb.append("}");
			}else if(GisDispatchGraphConstant.RESOURCE_MANHOLE.equals(resourceMap.get("resourceType").toString())){	//人手紧
				
			}else if(GisDispatchGraphConstant.RESOURCE_HUMAN.equals(resourceMap.get("resourceType").toString())){	//人员
				sb.append("staffName:'"+resourceMap.get("staffName")+"',");
				sb.append("staffId:'"+GisDispatchGraphConstant.RESOURCE_HUMAN+"_"+resourceMap.get("staffId")+"',");
				sb.append("sex:'"+resourceMap.get("sex")+"',");
				sb.append("contactPhone:'"+resourceMap.get("phone")+"'");
				sb.append("}");
				
				if(resourceMap.get("skillList")!=null){
					List<Map> skillList=(List<Map>)resourceMap.get("skillList");
					sb.append(",");
					sb.append("skillList:[");
					if(skillList!=null && !skillList.isEmpty()){
						for(Map tempMap:skillList){
							sb.append("{");
							sb.append("skillType:'"+tempMap.get("skillType")+"',");
							sb.append("skillGrade:'"+tempMap.get("skillGrade")+"',");
							sb.append("experienceYear:'"+tempMap.get("experienceYear")+"'");
							sb.append("}");
							sb.append(",");
						}
						sb.delete(sb.length()-1, sb.length());
					}
					sb.append("]");
				}
				
				if(resourceMap.get("materialList")!=null){
					List<Map<String,Object>> materialList=(List<Map<String,Object>>)resourceMap.get("materialList");
					sb.append(",");
					sb.append("materialList:[");
					if(materialList!=null && !materialList.isEmpty()){
						for(Map<String,Object> tempMap:materialList){
							sb.append("{");
							sb.append("materialstoreId:'"+tempMap.get("storeId")+"',");
							sb.append("materialType:'"+tempMap.get("materialType")+"',");
							sb.append("materialName:'"+tempMap.get("meClassificationName")+"',");
							sb.append("materialCount:'"+tempMap.get("count")+"'");
							sb.append("}");
							sb.append(",");
						}
						sb.delete(sb.length()-1, sb.length());
					}
					sb.append("]");
				}
				
				//转换人员忙闲状态任务数信息
				if(busyOrNotMap!=null && !busyOrNotMap.isEmpty()){
					sb.append(",");
					sb.append("busyOrNot:{");
					sb.append("totalTask:'"+busyOrNotMap.get("totalTask")+"',");
					sb.append("h2:'"+busyOrNotMap.get("h2")+"',");
					sb.append("h4:'"+busyOrNotMap.get("h4")+"',");
					sb.append("h8:'"+busyOrNotMap.get("h8")+"',");
					sb.append("h8Up:'"+busyOrNotMap.get("h8Up")+"'");
					sb.append("}");
				}
				
			}else if(GisDispatchGraphConstant.RESOURCE_CAR.equals(resourceMap.get("resourceType").toString())){	//车辆
				sb.append("key:'"+resourceMap.get("id")+"',");
				sb.append("name:'"+resourceMap.get("carNumber")+"',");
				sb.append("carType:'"+resourceMap.get("type")+"',");
				sb.append("driverName:'"+resourceMap.get("driverName")+"',");
				sb.append("driverPhone:'"+resourceMap.get("driverPhone")+"',");
				sb.append("driverCarId:'"+resourceMap.get("driverCarId")+"',");
				sb.append("driverAccount:'"+resourceMap.get("driverAccount")+"',");
				sb.append("carPic:'"+resourceMap.get("carPic")+"',");
				sb.append("passengerNumber:'"+resourceMap.get("passengerNumber")+"',");
				sb.append("carState:'"+resourceMap.get("carState")+"'");
				sb.append("}");
			}else if((GisDispatchGraphConstant.RESOURCE_MAINTAINGROUP.equals(resourceMap.get("resourceType").toString()))){	//维护组
				
				sb.append("teamKey:'"+GisDispatchGraphConstant.RESOURCE_MAINTAINGROUP+"_"+resourceMap.get("teamId")+"',");
				sb.append("teamName:'"+resourceMap.get("teamName")+"',");
				sb.append("teamLeaderKey:'"+GisDispatchGraphConstant.RESOURCE_HUMAN+"_"+resourceMap.get("teamLeaderId")+"',");
				sb.append("teamLeaderName:'"+resourceMap.get("teamLeaderName")+"',");
				sb.append("teamLeaderPhone:'"+resourceMap.get("teamLeaderPhone")+"'");
				sb.append("}");
				
				if(resourceMap.get("teamMemberList")!=null){
					List<Map> teamMemberList=(List<Map>)resourceMap.get("teamMemberList");
					sb.append(",");
					sb.append("teamMemberList:[");
					for(Map tempMap:teamMemberList){
						sb.append("{");
						sb.append("staffId:'"+tempMap.get("staffId")+"',");
						sb.append("staffName:'"+tempMap.get("staffName")+"',");
						sb.append("phone:'"+tempMap.get("phone")+"',");
						sb.append("sex:'"+tempMap.get("sex")+"'");
						sb.append("}");
						sb.append(",");
					}
					sb.delete(sb.length()-1, sb.length());
					sb.append("]");
				}
			}
			else if((GisDispatchGraphConstant.RESOURCE_SHIYEBU.equals(resourceMap.get("resourceType").toString())) || 		//事业部
					(GisDispatchGraphConstant.RESOURCE_MAINTAINTEAMAAREADDRESS.equals(resourceMap.get("resourceType").toString())) ||	//维护片区
					(GisDispatchGraphConstant.RESOURCE_HEADERQUARTER.equals(resourceMap.get("resourceType").toString())) || 		//公司
					(GisDispatchGraphConstant.RESOURCE_PROJECTGROUP.equals(resourceMap.get("resourceType").toString()))  		){//项目组
				sb.append("orgId:'"+resourceMap.get("orgId")+"',");
				sb.append("orgName:'"+resourceMap.get("orgName")+"',");
				sb.append("address:'"+resourceMap.get("address")+"',");
				sb.append("contactPhone:'"+resourceMap.get("contactPhone")+"',");
				sb.append("orgStaffCount:'"+resourceMap.get("orgStaffCount")+"',");
				sb.append("dutyPerson:'"+resourceMap.get("dutyPerson")+"'");
				sb.append("}");
			}
			else if((GisDispatchGraphConstant.RESOURCE_QUESTIONSPOT.equals(resourceMap.get("resourceType").toString())) || //问题点
					(GisDispatchGraphConstant.RESOURCE_HIDDENTROUBLESPOT.equals(resourceMap.get("resourceType").toString())) || 		//隐患点
					(GisDispatchGraphConstant.RESOURCE_FAULTSPOT.equals(resourceMap.get("resourceType").toString())) ||	//故障点
					(GisDispatchGraphConstant.RESOURCE_WATCHSPOT.equals(resourceMap.get("resourceType").toString())) ){ 		//盯防点

				sb.append("hotspotId:'"+resourceMap.get("id")+"',");
				sb.append("markName:'"+resourceMap.get("markName")+"',");
				sb.append("markType:'"+resourceMap.get("markType")+"',");
				sb.append("address:'"+resourceMap.get("address")+"',");
				sb.append("remark:'"+resourceMap.get("remark")+"'");
				sb.append("}");
			}
			else if((GisDispatchGraphConstant.RESOURCE_STOREHOUSE.equals(resourceMap.get("resourceType").toString())) ){ 		//仓库

				sb.append("warehouseId:'"+resourceMap.get("storeId")+"',");
				sb.append("warehouseName:'"+resourceMap.get("warehouseName")+"',");
				sb.append("warehousePhone:'"+resourceMap.get("warehousePhone")+"',");
				sb.append("warehouseAddress:'"+resourceMap.get("warehousePosition")+"',");
				sb.append("remark:'"+resourceMap.get("remark")+"'");
				sb.append("}");
				
				if(resourceMap.get("materialList")!=null){
					List<Map> materialList=(List<Map>)resourceMap.get("materialList");
					sb.append(",");
					sb.append("materialList:[");
					if(materialList!=null && !materialList.isEmpty()){
						for(Map<String,Object> tempMap:materialList){
							sb.append("{");
							sb.append("materialstoreId:'"+tempMap.get("storeId")+"',");
							sb.append("materialType:'"+tempMap.get("materialType")+"',");
							sb.append("materialName:'"+tempMap.get("meClassificationName")+"',");
							sb.append("materialCount:'"+tempMap.get("count")+"'");
							sb.append("}");
							sb.append(",");
						}
						sb.delete(sb.length()-1, sb.length());
					}
					sb.append("]");
				}
				
				//组装出库单
				if(resourceMap.get("outboundList")!=null){
					List<Map> outboundList=(List<Map>)resourceMap.get("outboundList");
					sb.append(",");
					sb.append("outboundList:[");
					if(outboundList!=null && !outboundList.isEmpty()){
						for(Map<String,Object> tempMap:outboundList){
							sb.append("{");
							sb.append("formId:'"+tempMap.get("formId")+"',");
							sb.append("outboundTheApplicant:'"+tempMap.get("outboundTheApplicant")+"',");
							sb.append("status:'"+tempMap.get("status")+"'");
							sb.append("}");
							sb.append(",");
						}
						sb.delete(sb.length()-1, sb.length());
					}
					sb.append("]");
				}
				
				//组装入库单
				if(resourceMap.get("incomingList")!=null){
					List<Map> incomingList=(List<Map>)resourceMap.get("incomingList");
					sb.append(",");
					sb.append("incomingList:[");
					if(incomingList!=null && !incomingList.isEmpty()){
						for(Map<String,Object> tempMap:incomingList){
							sb.append("{");
							sb.append("formId:'"+tempMap.get("formId")+"',");
							sb.append("incomingTheApplicant:'"+tempMap.get("incomingTheApplicant")+"',");
							sb.append("status:'"+tempMap.get("status")+"'");
							sb.append("}");
							sb.append(",");
						}
						sb.delete(sb.length()-1, sb.length());
					}
					sb.append("]");
				}
			}
			
			
			
			//转换工单/任务单信息
			if(totalMap!=null && !totalMap.isEmpty()){
				List<Map<String,Object>> ownWorkOrderList = totalMap.get("ownWorkOrderList");
				List<Map<String,Object>> notOwnWorkOrderList = totalMap.get("notOwnWorkOrderList");
				List<Map<String,Object>> ownTaskOrderList = totalMap.get("ownTaskOrderList");
				List<Map<String,Object>> notOwnTaskOrderList = totalMap.get("notOwnTaskOrderList");
				
				//待办工单
				if((ownWorkOrderList!=null && !ownWorkOrderList.isEmpty())){
					sb.append(",");
					sb.append("ownWorkOrderList:[");
					for(Map<String,Object> tempMap:ownWorkOrderList){
						sb.append("{");
						sb.append("orderNum:'"+tempMap.get("woId")+"',");
						sb.append("taskName:'"+tempMap.get("woTitle")+"',");
						sb.append("statuName:'"+tempMap.get("statusName")+"',");
						sb.append("orderFlag:'wo',");
						sb.append("locationHref:'"+tempMap.get("formUrl")+"'");
						sb.append("}");
						sb.append(",");
					}
					sb.delete(sb.length()-1, sb.length());
					sb.append("]");
				}
				//站址工单
				if((notOwnWorkOrderList!=null && !notOwnWorkOrderList.isEmpty())){
					sb.append(",");
					sb.append("notOwnWorkOrderList:[");
					for(Map<String,Object> tempMap:notOwnWorkOrderList){
						sb.append("{");
						sb.append("orderNum:'"+tempMap.get("woId")+"',");
						sb.append("taskName:'"+tempMap.get("woTitle")+"',");
						sb.append("statuName:'"+tempMap.get("statusName")+"',");
						sb.append("orderFlag:'wo',");
						sb.append("locationHref:'"+tempMap.get("formUrl")+"'");
						sb.append("}");
						sb.append(",");
					}
					sb.delete(sb.length()-1, sb.length());
					sb.append("]");
				}
				
				//待办任务单
				if((ownTaskOrderList!=null && !ownTaskOrderList.isEmpty())){
					sb.append(",");
					sb.append("ownTaskOrderList:[");
					for(Map<String,Object> tempMap:ownTaskOrderList){
						sb.append("{");
						sb.append("orderNum:'"+tempMap.get("toId")+"',");
						sb.append("parentWoId:'"+tempMap.get("woId")+"',");
						sb.append("taskName:'"+tempMap.get("toTitle")+"',");
						sb.append("statuName:'"+tempMap.get("statusName")+"',");
						sb.append("locationHref:'"+tempMap.get("formUrl")+"',");
						
						//图元类型是车辆
						if(GisDispatchGraphConstant.RESOURCE_CAR.equals(resourceMap.get("resourceType").toString())){
							sb.append("takeCarAddress:'"+tempMap.get("planUseCarAddress")+"',");
							sb.append("realTakeCarTime:'"+tempMap.get("planUseCarTime")+"',");
							sb.append("locationHref:'"+tempMap.get("formUrl")+"',");
						}
						sb.append("orderFlag:'to'");
						sb.append("}");
						sb.append(",");
					}
					sb.delete(sb.length()-1, sb.length());
					sb.append("]");
				}
				
				//站址任务单
				if((notOwnTaskOrderList!=null && !notOwnTaskOrderList.isEmpty())){
					sb.append(",");
					sb.append("notOwnTaskOrderList:[");
					for(Map<String,Object> tempMap:notOwnTaskOrderList){
						sb.append("{");
						sb.append("orderNum:'"+tempMap.get("toId")+"',");
						sb.append("parentWoId:'"+tempMap.get("woId")+"',");
						sb.append("taskName:'"+tempMap.get("toTitle")+"',");
						sb.append("statuName:'"+tempMap.get("statusName")+"',");
						sb.append("orderFlag:'to',");
						sb.append("locationHref:'"+tempMap.get("formUrl")+"'");
						sb.append("}");
						sb.append(",");
					}
					sb.delete(sb.length()-1, sb.length());
					sb.append("]");
				}

			}
			
			//工作列表
			if(workListMap!=null && !workListMap.isEmpty()){;
				sb.append(",");
				sb.append("workList:[");
				for(Map.Entry<String, Map> entry:workListMap.entrySet()){
					Map tempMap=entry.getValue();
					sb.append("{");
					sb.append("id:'"+tempMap.get("id")+"',");
					sb.append("workName:'"+tempMap.get("workName")+"',");
					sb.append("workLocation:'"+tempMap.get("workLocation")+"',");
					sb.append("workFlag:'"+tempMap.get("workFlag")+"'");
					sb.append("}");
					sb.append(",");
				}
				sb.delete(sb.length()-1, sb.length());
				sb.append("]");
			}
			
			sb.append("}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @author :che.yd 把资源信息与资源关联的任务信息，转换成json格式，（注意：该action主要是针对于连线派发工单树的情况）
	 * @return
	 */
	public static String transferResourceAndTaskInfoToJsonOfWorkOrderTree(Map resourceMap,Map<String, List<Map<String,Object>>> totalMap,Map busyOrNotMap,Map<String,Map> workListMap){
		
//		String accessUrl=InterfaceUtil.getConfigValueFromProperties("gisShowStationURL");
	    
		String accessUrl = "";
		//转换图元基本的信息
		StringBuffer sb=new StringBuffer("");
		if(resourceMap==null || resourceMap.size()==0){
			return sb.toString();
		}
		
		try {
			sb.append("{");
			sb.append("basicInfo:{");
			if(GisDispatchGraphConstant.RESOURCE_STATION.equals(resourceMap.get("resourceType").toString())){	//站址
				sb.append("name:'"+resourceMap.get("objectName")+"',");
				sb.append("address:'"+resourceMap.get("location")+"',");
				sb.append("key:'"+GisDispatchGraphConstant.RESOURCE_STATION+"_"+resourceMap.get("objectIdentity")+"',");
				sb.append("stationAccessUrl:'"+accessUrl+resourceMap.get("objectIdentity")+"',");
				sb.append("rank:'"+resourceMap.get("rank")+"'");
				sb.append("}");
			}else if(GisDispatchGraphConstant.RESOURCE_MANHOLE.equals(resourceMap.get("resourceType").toString())){	//人手紧
				
			}else if(GisDispatchGraphConstant.RESOURCE_HUMAN.equals(resourceMap.get("resourceType").toString())){	//人员
				sb.append("staffName:'"+resourceMap.get("staffName")+"',");
				sb.append("staffId:'"+GisDispatchGraphConstant.RESOURCE_HUMAN+"_"+resourceMap.get("staffId")+"',");
				sb.append("sex:'"+resourceMap.get("sex")+"',");
				sb.append("contactPhone:'"+resourceMap.get("phone")+"'");
				sb.append("}");
				
				if(resourceMap.get("skillList")!=null){
					List<Map> skillList=(List<Map>)resourceMap.get("skillList");
					if(skillList!=null && !skillList.isEmpty()){
						sb.append(",");
						sb.append("skillList:[");
						for(Map tempMap:skillList){
							sb.append("{");
							sb.append("skillType:'"+tempMap.get("skillType")+"',");
							sb.append("skillGrade:'"+tempMap.get("skillGrade")+"',");
							sb.append("experienceYear:'"+tempMap.get("experienceYear")+"'");
							sb.append("}");
							sb.append(",");
						}
						sb.delete(sb.length()-1, sb.length());
						sb.append("]");
					}
				}
			}else if(GisDispatchGraphConstant.RESOURCE_CAR.equals(resourceMap.get("resourceType").toString())){	//车辆
				sb.append("key:'"+resourceMap.get("carId")+"',");
				sb.append("name:'"+resourceMap.get("carNumber")+"',");
				sb.append("carType:'"+resourceMap.get("type")+"',");
				sb.append("driverName:'"+resourceMap.get("driverName")+"',");
				sb.append("driverPhone:'"+resourceMap.get("driverPhone")+"',");
				sb.append("driverCarId:'"+resourceMap.get("driverCarId")+"',");
				sb.append("driverAccount:'"+resourceMap.get("driverAccount")+"',");
				sb.append("carPic:'"+resourceMap.get("carPic")+"',");
				sb.append("passengerNumber:'"+resourceMap.get("passengerNumber")+"',");
				sb.append("carState:'"+resourceMap.get("carState")+"'");
				sb.append("}");
			}else if((GisDispatchGraphConstant.RESOURCE_MAINTAINGROUP.equals(resourceMap.get("resourceType").toString()))){	//维护组
				
				sb.append("teamKey:'"+GisDispatchGraphConstant.RESOURCE_MAINTAINGROUP+"_"+resourceMap.get("teamId")+"',");
				sb.append("teamName:'"+resourceMap.get("teamName")+"',");
				sb.append("teamLeaderKey:'"+GisDispatchGraphConstant.RESOURCE_HUMAN+"_"+resourceMap.get("teamLeaderId")+"',");
				sb.append("teamLeaderName:'"+resourceMap.get("teamLeaderName")+"',");
				sb.append("teamLeaderPhone:'"+resourceMap.get("teamLeaderPhone")+"'");
				sb.append("}");
				
				if(resourceMap.get("teamMemberList")!=null){
					List<Map> teamMemberList=(List<Map>)resourceMap.get("teamMemberList");
					sb.append(",");
					sb.append("teamMemberList:[");
					for(Map tempMap:teamMemberList){
						sb.append("{");
						sb.append("staffId:'"+tempMap.get("staffId")+"',");
						sb.append("staffName:'"+tempMap.get("staffName")+"',");
						sb.append("phone:'"+tempMap.get("phone")+"',");
						sb.append("sex:'"+tempMap.get("sex")+"'");
						sb.append("}");
						sb.append(",");
					}
					sb.delete(sb.length()-1, sb.length());
					sb.append("]");
				}
			}
			else if((GisDispatchGraphConstant.RESOURCE_SHIYEBU.equals(resourceMap.get("resourceType").toString())) || 		//事业部
					(GisDispatchGraphConstant.RESOURCE_MAINTAINTEAMAAREADDRESS.equals(resourceMap.get("resourceType").toString())) ||	//维护片区
					(GisDispatchGraphConstant.RESOURCE_HEADERQUARTER.equals(resourceMap.get("resourceType").toString())) || 		//公司
					(GisDispatchGraphConstant.RESOURCE_PROJECTGROUP.equals(resourceMap.get("resourceType").toString()))  		){//项目组
				sb.append("orgId:'"+resourceMap.get("orgId")+"',");
				sb.append("orgName:'"+resourceMap.get("orgName")+"',");
				sb.append("address:'"+resourceMap.get("address")+"',");
				sb.append("contactPhone:'"+resourceMap.get("contactPhone")+"',");
				sb.append("orgStaffCount:'"+resourceMap.get("orgStaffCount")+"',");
				sb.append("dutyPerson:'"+resourceMap.get("dutyPerson")+"'");
				sb.append("}");
			}
			else if((GisDispatchGraphConstant.RESOURCE_QUESTIONSPOT.equals(resourceMap.get("resourceType").toString())) || //问题点
					(GisDispatchGraphConstant.RESOURCE_HIDDENTROUBLESPOT.equals(resourceMap.get("resourceType").toString())) || 		//隐患点
					(GisDispatchGraphConstant.RESOURCE_FAULTSPOT.equals(resourceMap.get("resourceType").toString())) ||	//故障点
					(GisDispatchGraphConstant.RESOURCE_WATCHSPOT.equals(resourceMap.get("resourceType").toString())) ){ 		//盯防点

				sb.append("hotspotId:'"+resourceMap.get("id")+"',");
				sb.append("markName:'"+resourceMap.get("markName")+"',");
				sb.append("markType:'"+resourceMap.get("markType")+"',");
				sb.append("address:'"+resourceMap.get("address")+"',");
				sb.append("remark:'"+resourceMap.get("remark")+"'");
				sb.append("}");
			}
			else if((GisDispatchGraphConstant.RESOURCE_STOREHOUSE.equals(resourceMap.get("resourceType").toString())) ){ 		//仓库

				sb.append("warehouseId:'"+resourceMap.get("id")+"',");
				sb.append("warehouseName:'"+resourceMap.get("warehouseName")+"',");
				sb.append("warehousePhone:'"+resourceMap.get("warehousePhone")+"',");
				sb.append("warehouseAddress:'"+resourceMap.get("warehouseAddress")+"',");
				sb.append("remark:'"+resourceMap.get("remark")+"'");
				sb.append("}");
			}
			
			
			
			//转换工单/任务单信息
			if(totalMap!=null && !totalMap.isEmpty()){
				List<Map<String,Object>> ownWorkOrderList = totalMap.get("ownWorkOrderList");
				List<Map<String,Object>> notOwnWorkOrderList = totalMap.get("notOwnWorkOrderList");
				List<Map<String,Object>> ownTaskOrderList = totalMap.get("ownTaskOrderList");
				List<Map<String,Object>> notOwnTaskOrderList = totalMap.get("notOwnTaskOrderList");
				
				
				//合并待办与不待办的工单列表
				List<Map<String,Object>> allWorkOrderList=new ArrayList<Map<String,Object>>();
				allWorkOrderList.addAll(ownWorkOrderList);
				allWorkOrderList.addAll(notOwnWorkOrderList);
				
				//合并待办与不待办的任务单列表
				List<Map<String,Object>> allTaskOrderList=new ArrayList<Map<String,Object>>();
				allTaskOrderList.addAll(ownTaskOrderList);
				allTaskOrderList.addAll(notOwnTaskOrderList);
				
				//用于记录存在父工单的子任务单
				Map<String,String> isExistsParentWoIdMap=new HashMap<String,String>();
				
				//如果工单不为空
				if(allWorkOrderList!=null && !allWorkOrderList.isEmpty()){
					sb.append(",");
					sb.append("orderTreeList:[");
					//遍历工单列表，构造前台展示需要的数据格式
					for(Map<String,Object> tempWorkOrderMap:allWorkOrderList){
						String woId=tempWorkOrderMap.get("woId")==null?"":tempWorkOrderMap.get("woId").toString();
						sb.append("{");
						sb.append("orderNum:'"+tempWorkOrderMap.get("woId")+"',");
						sb.append("taskName:'"+tempWorkOrderMap.get("woTitle")+"',");
						sb.append("statuName:'"+tempWorkOrderMap.get("statusName")+"',");
						sb.append("orderFlag:'wo',");
						sb.append("locationHref:'"+tempWorkOrderMap.get("formUrl")+"',");
						
						//先构造工单的的子任务列表
						sb.append("childTaskList:[");
						if(allTaskOrderList!=null && !allTaskOrderList.isEmpty()){
							for(Map<String,Object> tempTaskOrderMap:allTaskOrderList){
								if(woId.equals(tempTaskOrderMap.get("woId")==null?"":tempTaskOrderMap.get("woId").toString())){
									
									//记录存在父工单的子任务单
									isExistsParentWoIdMap.put(tempTaskOrderMap.get("toId").toString(), tempTaskOrderMap.get("toId").toString());
									sb.append("{");
									sb.append("orderNum:'"+tempTaskOrderMap.get("toId")+"',");
									sb.append("parentWoId:'"+tempTaskOrderMap.get("woId")+"',");
									sb.append("taskName:'"+tempTaskOrderMap.get("toTitle")+"',");
									sb.append("statuName:'"+tempTaskOrderMap.get("statusName")+"',");
									sb.append("locationHref:'"+tempTaskOrderMap.get("formUrl")+"',");
									
									//图元类型是车辆
									if(GisDispatchGraphConstant.RESOURCE_CAR.equals(resourceMap.get("resourceType").toString())){
										sb.append("takeCarAddress:'"+tempTaskOrderMap.get("takeCarAddress")+"',");
										sb.append("realTakeCarTime:'"+tempTaskOrderMap.get("realTakeCarTime")+"',");
										sb.append("locationHref:'"+tempTaskOrderMap.get("formUrl")+"',");
									}
									sb.append("orderFlag:'to'");
									sb.append("}");
									sb.append(",");
								}
							}

							if(isExistsParentWoIdMap!=null && !isExistsParentWoIdMap.isEmpty()){
								sb.delete(sb.length()-1, sb.length());
							}
						}
						sb.append("]");
						sb.append("}");
						sb.append(",");
					}
					
					if(allTaskOrderList!=null && !allTaskOrderList.isEmpty()){
						//遍历任务单列表，构造前台展示需要的数据格式
						for(Map<String,Object> tempTaskOrderMap:allTaskOrderList){
							String tempToId=tempTaskOrderMap.get("toId")==null?"":tempTaskOrderMap.get("toId").toString();
							if(isExistsParentWoIdMap.get(tempToId)==null){
								sb.append("{");
								sb.append("orderNum:'"+tempTaskOrderMap.get("toId")+"',");
								sb.append("parentWoId:'"+tempTaskOrderMap.get("woId")+"',");
								sb.append("taskName:'"+tempTaskOrderMap.get("toTitle")+"',");
								sb.append("statuName:'"+tempTaskOrderMap.get("statusName")+"',");
								sb.append("locationHref:'"+tempTaskOrderMap.get("formUrl")+"',");
								//图元类型是车辆
								if(GisDispatchGraphConstant.RESOURCE_CAR.equals(resourceMap.get("resourceType").toString())){
									sb.append("takeCarAddress:'"+tempTaskOrderMap.get("takeCarAddress")+"',");
									sb.append("realTakeCarTime:'"+tempTaskOrderMap.get("realTakeCarTime")+"',");
									sb.append("locationHref:'"+tempTaskOrderMap.get("formUrl")+"',");
								}
								sb.append("orderFlag:'to'");
								sb.append("}");
								sb.append(",");
							}
						}
					}
					sb.delete(sb.length()-1, sb.length());
					sb.append("]");
				}else{	//工单列表为空
					if(allTaskOrderList!=null && !allTaskOrderList.isEmpty()){
						sb.append(",");
						sb.append("orderTreeList:[");
						//遍历任务单列表，构造前台展示需要的数据格式
						for(Map<String,Object> tempTaskOrderMap:allTaskOrderList){
							sb.append("{");
							sb.append("orderNum:'"+tempTaskOrderMap.get("toId")+"',");
							sb.append("parentWoId:'"+tempTaskOrderMap.get("woId")+"',");
							sb.append("taskName:'"+tempTaskOrderMap.get("toTitle")+"',");
							sb.append("statuName:'"+tempTaskOrderMap.get("statusName")+"',");
							sb.append("locationHref:'"+tempTaskOrderMap.get("formUrl")+"',");
							//图元类型是车辆
							if(GisDispatchGraphConstant.RESOURCE_CAR.equals(resourceMap.get("resourceType").toString())){
								sb.append("takeCarAddress:'"+tempTaskOrderMap.get("takeCarAddress")+"',");
								sb.append("realTakeCarTime:'"+tempTaskOrderMap.get("realTakeCarTime")+"',");
								sb.append("locationHref:'"+tempTaskOrderMap.get("formUrl")+"',");
							}
							sb.append("orderFlag:'to'");
							sb.append("}");
							sb.append(",");
						}
						sb.delete(sb.length()-1, sb.length());
						sb.append("]");
					}
					
				}
			}
			
			//转换人员忙闲状态任务数信息
			if(busyOrNotMap!=null && !busyOrNotMap.isEmpty()){
				sb.append(",");
				sb.append("busyOrNot:{");
				sb.append("totalTask:'"+busyOrNotMap.get("totalTask")+"',");
				sb.append("h2:'"+busyOrNotMap.get("h2")+"',");
				sb.append("h4:'"+busyOrNotMap.get("h4")+"',");
				sb.append("h8:'"+busyOrNotMap.get("h8")+"',");
				sb.append("h8Up:'"+busyOrNotMap.get("h8Up")+"'");
				sb.append("}");
			}
			
			
			//工作列表
			if(workListMap!=null && !workListMap.isEmpty()){;
				sb.append(",");
				sb.append("workList:[");
				for(Map.Entry<String, Map> entry:workListMap.entrySet()){
					Map tempMap=entry.getValue();
					sb.append("{");
					sb.append("id:'"+tempMap.get("id")+"',");
					sb.append("workName:'"+tempMap.get("workName")+"',");
					sb.append("workLocation:'"+tempMap.get("workLocation")+"',");
					sb.append("workFlag:'"+tempMap.get("workFlag")+"'");
					sb.append("}");
					sb.append(",");
				}
				sb.delete(sb.length()-1, sb.length());
				sb.append("]");
			}
			
			sb.append("}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}

	
}
