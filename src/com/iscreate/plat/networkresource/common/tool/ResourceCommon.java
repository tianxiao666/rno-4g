package com.iscreate.plat.networkresource.common.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
public class ResourceCommon {
	
	private  static final  Log log = LogFactory.getLog(ResourceCommon.class);
	/**
	 * 把NULL，TRUE，FALSE转换成“”，“是”，“否”
	 * @param infoMap
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> mapRecombinationToString(Map<String, Object> infoMap){
		log.info("进入===mapRecombinationToString方法");
		log.info("开始循环infoMap");
		for(String key:infoMap.keySet()){
			//System.out.println(infoMap.get(key));
			log.info("infoMap==key:"+key);
			if(infoMap.get(key) != null){
				if(infoMap.get(key).equals(true)){
					infoMap.put(key, "是");
				}else if(infoMap.get(key).equals(false)){
					infoMap.put(key, "否");
				}else if(infoMap.get(key).equals("null")){
					infoMap.put(key, "");
				}
			}
		}
		log.info("退出===mapRecombinationToString方法");
		return infoMap;
	}
	
	/**
	 * 把NULL，TRUE，FALSE转换成“”，“是”，“否”
	 * @param infoMap
	 * @return Map<String, Object>
	 */
	public static List<Map<String,Object>> listMapRecombinationToString(List<Map<String,Object>> infoList){
		log.info("进入===listMapRecombinationToString方法");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		log.info("开始循环infoMap");
		for(Map<String,Object> infoMap:infoList){
			for(String key:infoMap.keySet()){
				log.info("infoMap==key:"+key);
				//System.out.println(infoMap.get(key));
				if(infoMap.get(key) != null){
					if(infoMap.get(key).equals(true)){
						infoMap.put(key, "是");
					}else if(infoMap.get(key).equals(false)){
						infoMap.put(key, "否");
					}else if(infoMap.get(key).equals("null")){
						infoMap.put(key, "");
					}
				}
			}
			//log.info("退出===listMapRecombinationToString");
			list.add(infoMap);
		}
		log.info("退出===listMapRecombinationToString方法");
		return list;
	}
	
	/**
	 * 把“”，“是”，“否”转换成NULL，TRUE，FALSE
	 * @param infoMap
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> mapRecombinationToObject(Map<String, Object> infoMap){
		log.info("进入===mapRecombinationToObject方法");
		log.info("开始循环infoMap");
		for(String key:infoMap.keySet()){
			//System.out.println(infoMap.get(key));
			log.info("infoMap==key:"+key);
			if(infoMap.get(key) != null){
				if(infoMap.get(key).equals("是")){
					infoMap.put(key, true);
				}else if(infoMap.get(key).equals("否")){
					infoMap.put(key, false);
				}else if(infoMap.get(key).equals("")){
					infoMap.put(key, null);
				}
			}
		}
		log.info("退出===mapRecombinationToObject方法");
		return infoMap;
	}
	
	/**
	 * 把1，0转换成TRUE，FALSE
	 * @param intger
	 * @return boolean
	 */
	public static boolean intgerRecombinationBoolean(int intger){
		log.info("进入===intgerRecombinationBoolean方法");
		if(intger == 1){
			log.info("退出===intgerRecombinationBoolean方法 返回值为:true");
			return true;
		}else{
			log.info("退出===intgerRecombinationBoolean方法 返回值为:false");
			return false;
		}
	}
	
	/**
	 * 把TRUE，FALSE转换成1，0
	 * @param bool
	 * @return int
	 */
	public static int intgerRecombinationBoolean(boolean bool){
		log.info("进入===intgerRecombinationBoolean方法");
		if(bool){
			log.info("退出===intgerRecombinationBoolean方法 返回值为:1");
			return 1;
		}else{
			log.info("退出===intgerRecombinationBoolean方法 返回值为:0");
			return 0;
		}
	}
	

	public static String[] getEntityGroupString(String entityGroup){
		log.info("进入===getEntityGroupString方法");
		if(entityGroup.equals("Conn_4_PipelineSection")){
			log.info("退出===getEntityGroupString方法 返回值为：'ManWell','Pole','HangWall','MarkPost','Station','FiberCrossCabinet'");
			return new String[]{"ManWell","Pole","HangWall","MarkPost","Station","FiberCrossCabinet"};
		}
		else if(entityGroup.equals("Passthrough_4_PipelineSection")){
			log.info("退出===getEntityGroupString方法 返回值为：'PipeHole','VerticalPipe','CableEntranceHole'");
			return new String[]{"PipeHole","VerticalPipe","CableEntranceHole"};
		}
		else if(entityGroup.equals("Conn_4_PolelineSection")){
			log.info("退出===getEntityGroupString方法 返回值为：'Pole','HangWall'");
			return new String[]{"Pole","HangWall"};
		}
		else if(entityGroup.equals("Conn_4_BuriedlineSection")){
			log.info("退出===getEntityGroupString方法 返回值为：'MarkPost','Pole'");
			return new String[]{"MarkPost","Pole"};
		}
		else if(entityGroup.equals("Conn_4_FiberSection")){
			log.info("退出===getEntityGroupString方法 返回值为：'ODF','Joint','FiberCrossCabinet','FiberDistributionCabinet','FiberTerminalCase'");
			return new String[]{"ODF","Joint","FiberCrossCabinet","FiberDistributionCabinet","FiberTerminalCase"};
		}
		else if(entityGroup.equals("Passthrough_4_FiberSection")){
			log.info("退出===getEntityGroupString方法 返回值为：'PolelineSection','BuriedlineSection','PipeHole','CableEntranceHole','MarkPost'");
			return new String[]{"PolelineSection","BuriedlineSection","PipeHole","CableEntranceHole","MarkPost"};
		}
		else if(entityGroup.equals("Conn_4_FiberCore")){
			log.info("退出===getEntityGroupString方法 返回值为：'Terminal','Pole','ManWell'");
			return new String[]{"Terminal","Pole","ManWell"};
		}
		else if(entityGroup.equals("Passthrough_4_WirelessChannel")){
			log.info("退出===getEntityGroupString方法 返回值为：'ElectricalTerminal','OpticalTerminal'");
			return new String[]{"ElectricalTerminal","OpticalTerminal"};
		}
		else if(entityGroup.equals("Conn_4_OpticalRoute")){
			log.info("退出===getEntityGroupString方法 返回值为：'Station','FiberCrossCabinet','FiberDistributionCabinet','FiberTerminalCase'");
			return new String[]{"Station","FiberCrossCabinet","FiberDistributionCabinet","FiberTerminalCase"};
		}
		else{
			log.info("退出===getEntityGroupString方法 返回值为：'"+entityGroup+"'");
			return new  String[]{entityGroup};
		}
	}
	
	
	
	public static String[] getlogicalresEntityClan(String logicalresEntityString){
		log.info("进入===getlogicalresEntityClan方法");
		if(logicalresEntityString.equals("PipeRoute")){
			log.info("退出===getEntityGroupString方法 返回值为：'PipelineSection','BuriedlineSection'");
			return new String[]{"PipelineSection","BuriedlineSection"};
		}
		else if(logicalresEntityString.equals("PoleRoute")){
			log.info("退出===getEntityGroupString方法 返回值为：'PolelineSection'");
			return new String[]{"PolelineSection"};
		}
		else if(logicalresEntityString.equals("Fiber")){
			log.info("退出===getEntityGroupString方法 返回值为：'FiberSection'");
			return new String[]{"FiberSection"};
		}
		else if(logicalresEntityString.equals("FiberSection")){
			log.info("退出===getEntityGroupString方法 返回值为：'FiberCore'");
			return new String[]{"FiberCore"};
		}
		else if(logicalresEntityString.equals("OpticalRoute")){
			log.info("退出===getEntityGroupString方法 返回值为：'OpticalRouteP2P'");
			return new String[]{"OpticalRouteP2P"};
		}
		else if(logicalresEntityString.equals("OpticalRouteP2P")){
			log.info("退出===getEntityGroupString方法 返回值为：'OpticalRouteSP2SP'");
			return new String[]{"OpticalRouteSP2SP"};
		}
		else if(logicalresEntityString.equals("TransmissionNetwork")){
			log.info("退出===getEntityGroupString方法 返回值为：'TransmissionSection'");
			return new String[]{"TransmissionSection"};
		}
		else{
			return null;
		}
	}
	
	public static boolean isLogicalresEntity(String entityType){
		log.info("进入===isLogicalresEntity方法");
		boolean result = false;
		if(entityType != null){
			log.info("entityType:值不为空");
			if(entityType.equals("PipeRoute")||entityType.equals("FiberSection")||entityType.equals("OpticalRouteP2P")
					||entityType.equals("Fiber")||entityType.equals("OpticalRoute")||entityType.equals("PoleRoute")){
				result = true;
			}else{
				result = false;
			}
		}
		log.info("退出===getEntityGroupString方法 返回值为："+result);
		return result;
	}
	
	/**
	 * ApplicationEntity转换Map
	 * @param applicationEntity
	 * @return
	 */
	public static Map<String,Object> applicationEntityConvertMap(ApplicationEntity applicationEntity){
		log.info("进入===isLogicalresEntity方法");
		Map<String, Object> moduleMap = ModuleProvider.getModule(applicationEntity.getType()).toMap();
		Map<String, Object> appMap = applicationEntity.toMap();
		for(String key:moduleMap.keySet()){
			moduleMap.put(key, "");		
		}
		
		for(String key:appMap.keySet()){
			if(!"_entityId".equals(key)) {
				moduleMap.put(key, appMap.get(key));
			}
		}
		log.info("退出===isLogicalresEntity方法 返回值为："+moduleMap);
		return moduleMap;
	}
	
	
	/**
	 * ApplicationEntity转换Map
	 * @param applicationEntity
	 * @return
	 */
	public static Map<String,Object> applicationEntityConvertMapToNull(Map<String, Object> appMap){
		log.info("进入===applicationEntityConvertMapToNull方法");
		for(String key:appMap.keySet()){
			if(!"_entityId".equals(key)&&!"_entityType".equals(key)) {
				appMap.put(key, "");
			}
		}
		log.info("退出===applicationEntityConvertMapToNull方法 返回值为："+appMap);
		return appMap;
	}
	
	/**
	 * 时间格式化
	 * @param object
	 * @return
	 */
	public static Object dateTimeFormat(Object object){
		log.info("进入===dateTimeFormat方法");
		Object encode = "";
		if(object.getClass().toString().indexOf("java.util.Date") >= 0){
			if(object != null && !object.equals("")){
				encode = object.toString();
				SimpleDateFormat spdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",java.util.Locale.ENGLISH);
				SimpleDateFormat spdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date parse = null;
				try {
					parse = spdf1.parse(encode.toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					log.error("时间转换错误 encode:"+encode);
					e.printStackTrace();
				}
				if(parse != null){
					encode = spdf.format(parse);		
				}
			}
		}else{
			encode = object;
		}
		log.info("退出===dateTimeFormat方法 返回值为："+encode);
		return encode;
	}
	/**
	 * 
	 * @description: 获取表字段属性查询输出字符串（oracle默认查出为大写）
	 * @author：
	 * @param moduleName
	 * @return     
	 * @return String     
	 * @date：Mar 19, 2013 5:23:06 PM
	 */
	public static String getSelectSqlAttributsString(String moduleName){
		ApplicationModule am1 = ModuleProvider.getModule("Sys_Area");
		log.info("进入getSelectSqlAttributsString(String moduleName)方法,获取表字段属性查询输出字符串（oracle默认查出为大写）,moduleName="+moduleName);
		String result="";
		if(moduleName!=null || !"".equals(moduleName)){
			ApplicationModule am = ModuleProvider.getModule(moduleName);
			StringBuffer sf = new StringBuffer();
			if(am!=null){
				for(String key:am.keyset()){
					if(key.equals("_entityId")){
						sf.append(","+moduleName+".ENTITY_ID  \"_entityId\"");
					}else if(key.equals("_entityType")){
						sf.append(","+moduleName+".ENTITY_TYPE  \"_entityType\"");
					}else if(key.equals("level")){
						sf.append(","+moduleName+".\"LEVEL\"  \""+key+"\"");
					}else if(key.equals("size")){
						sf.append(","+moduleName+".\"SIZE\"  \""+key+"\"");
					}else{
						sf.append(","+moduleName+"."+key+"  \""+key+"\"");
					}
					
				}
				if(!"Figurenode".equals(moduleName)&&!"Figureline".equals(moduleName)&&!"Figure".equals(moduleName)){
					sf.append(","+moduleName+".ENTITY_ID  \"_entityId\"");
					sf.append(","+moduleName+".ENTITY_TYPE  \"_entityType\"");
				}
				result = sf.substring(1);
			}
			if("Tree".equals(moduleName)){
				sf = new StringBuffer();
				sf.append(moduleName+".ENTITY_TYPE  \"_entityType\","+moduleName+".treeName \"treeName\","+moduleName+".treeId \"treeId\"");
				result = sf+"";
			}else if("Treenode".equals(moduleName)){
				sf = new StringBuffer();
				sf.append(moduleName+".ENTITY_TYPE  \"_entityType\","+moduleName+".aenType \"aenType\","+moduleName+".treeId \"treeId\","+moduleName+".nodeId \"nodeId\","+moduleName+".parentId \"parentId\","+moduleName+".aenId \"aenId\"");
				result = sf+"";
			}else if("Tbl_Unique_Uniquetable".equals(moduleName)){
				sf = new StringBuffer();
				sf.append(moduleName+".ENTITY_TYPE  \"_entityType\","+moduleName+".code \"code\","+moduleName+".name \"name\","+moduleName+".ENTITY_ID  \"_entityId\"");				
				result = sf+"";
			}/*else if("Net_Maintenance_Records".equals(moduleName)){
				sf = new StringBuffer();
				sf.append(moduleName+".id \"id\","+moduleName+".\"_ENTITYID\"  \"_entityId\","+moduleName+".\"_ENTITYTYPE\"  \"_entityType\","+moduleName+".biz_module \"biz_module\" ,"+moduleName+".op_cause \"op_cause\" ,"+moduleName+".linkurl \"linkurl\","+moduleName+".op_scene \"op_scene\","+moduleName+".op_category \"op_category\" ,"+moduleName+".res_type  \"res_type\","+moduleName+".res_id \"res_id\","+moduleName+".res_keyinfo  \"res_keyinfo\","+moduleName+".content \"content\","+moduleName+".user_name \"user_name\","+moduleName+".user_account  \"user_account\","+moduleName+".src_teminal \"src_teminal\","+moduleName+".op_time  \"op_time\", "+moduleName+".longitude \"longitude\","+moduleName+".latitude  \"latitude\","+moduleName+".record_type \"record_type\" ");
				result = sf+"";
			}else if("Service_Maintenance".equals(moduleName)){
				sf = new StringBuffer();
				sf.append(moduleName+".id \"id\","+moduleName+".\"_ENTITYID\"  \"_entityId\","+moduleName+".\"_ENTITYTYPE\"  \"_entityType\","+moduleName+".biz_module \"biz_module\" ,"+moduleName+".biz_processcode \"biz_processcode\" ,"+moduleName+".biz_processId \"biz_processId\","+moduleName+".maintenance_id \"maintenance_id\" ");
				result = sf+"";
			}*/
			
		}
		log.info("退出getSelectSqlAttributsString(String moduleName)方法 返回值为：result="+result);
		return result;
	}
	
	/**
	 * 
	 * @description: 获取插入数据 表的属性列字符串和值字符串 Map
	 * @author：
	 * @param ap
	 * @return     
	 * @return Map<String,String>     
	 * @date：Mar 19, 2013 5:39:18 PM
	 */
	public static Map<String,String> getInsertAttributesAndValuesStringMap(BasicEntity ap){
		log.info("进入getInsertAttributesAndValuesStringMap(ApplicationEntity ap)方法,获取插入数据 表的属性列字符串和值字符串 Map,ap="+ap);
		Map<String,String> result=null;
		if(ap!=null ){
			String moduleName = ap.getValue("_entityType")+"";
			ApplicationModule am = ModuleProvider.getModule(moduleName);
			StringBuffer attrStr = new StringBuffer();
			StringBuffer valueStr = new StringBuffer();
			if(am!=null){
				result = new HashMap<String,String>();
				for(String key:ap.keyset()){
					String type = "java.lang.String";
					if(am.containKey(key)){
						type = am.getAttribute(key).getValue("type")+"";
					}
					Object value = ap.getValue(key);
					if(value!=null &&!"".equals(value)&&!"null".equals(value)){
						if(key.equals("_entityId")){
							attrStr.append(",ENTITY_ID");
						}else if(key.equals("_entityType")){
							attrStr.append(",ENTITY_TYPE");
						}else if(key.equals("level")){
							attrStr.append(",\"LEVEL\"");
						}else if(key.equals("size")){
							valueStr.append(",\"SIZE\"='"+value+"'");
						}else{
							attrStr.append(","+key);
						}
						if("java.lang.String".indexOf(type)>=0){
							value = (value+"").replace("'","''");
						}
						if("java.util.Date".indexOf(type)>=0 ){
							Date d = ResourceCommon.setTimeFormat(value);
							if(d==null){
								valueStr.append(","+null);
							}else{
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								valueStr.append(",to_date('"+sdf.format(d)+"','yyyy-mm-dd hh24:mi:ss')");
							}
							
						}else{
							valueStr.append(",'"+value+"'");
						}
					}
				}
				if(attrStr!=null){
					result.put("attrStr",attrStr.substring(1));
					result.put("valueStr", valueStr.substring(1));
				}
			}else{
				result = new HashMap<String,String>();
				for(String key:ap.keyset()){
					Object value = ap.getValue(key);
					if(value!=null &&!"".equals(value)&&!"null".equals(value)){
						if(key.equals("_entityId")){
							attrStr.append(",ENTITY_ID");
						}else if(key.equals("_entityType")){
							attrStr.append(",ENTITY_TYPE");
						}else if(key.equals("level")){
							attrStr.append(",\"LEVEL\"");
						}else if(key.equals("size")){
							valueStr.append(",\"SIZE\"='"+value+"'");
						}else{
							attrStr.append(","+key);
						}
						valueStr.append(",'"+value+"'");
					}
				}
				if(attrStr!=null){
					result.put("attrStr",attrStr.substring(1));
					result.put("valueStr", valueStr.substring(1));
				}
			}
		}
		log.info("退出getInsertAttributesAndValuesStringMap(ApplicationEntity ap)方法 返回值为：result="+result);
		return result;
	}
	/**
	 * 
	 * @description: 获取表更新的值 key-value String
	 * @author：
	 * @param ap
	 * @return     
	 * @return String     
	 * @date：Mar 19, 2013 6:04:04 PM
	 */
	public static String getUpdateAttributesSqlString(BasicEntity ap){
		log.info("进入getUpdateAttributesSqlString(ApplicationEntity ap)方法,获取表更新的值 key-value String,ap="+ap);
		String result="";
		if(ap!=null ){
			String moduleName = ap.getValue("_entityType")+"";
			ApplicationModule am = ModuleProvider.getModule(moduleName);
			StringBuffer valueStr = new StringBuffer();
			if(am!=null){
				for(String key:ap.keyset()){
					String type = "java.lang.String";
					if(am.containKey(key)){
						type = am.getAttribute(key).getValue("type")+"";
					}
					Object value = ap.getValue(key);
					if(value!=null &&!"".equals(value)&&!"null".equals(value)){
						if("java.lang.String".indexOf(type)>=0){
							value = (value+"").replace("'","''");
						}
						if("java.util.Date".indexOf(type)>=0){
							Date d = ResourceCommon.setTimeFormat(value);
							if(d==null){
								valueStr.append(","+key+"="+null);
							}else{
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								valueStr.append(","+key+"=to_date('"+sdf.format(d)+"','yyyy-mm-dd hh24:mi:ss')");
							}
							
						}else{
							if(key.equals("_entityId")){
								valueStr.append(",ENTITY_ID='"+value+"'");
							}else if(key.equals("_entityType")){
								valueStr.append(",ENTITY_TYPE='"+value+"'");
							}else if(key.equals("level")){
								valueStr.append(",\"LEVEL\"='"+value+"'");
							}else if(key.equals("size")){
								valueStr.append(",\"SIZE\"='"+value+"'");
							}else{
								valueStr.append(","+key+"='"+value+"'");
							}
							
						}
					}
				}
				if(valueStr!=null){
					result=valueStr.substring(1);
				}
			}else{
				for(String key:ap.keyset()){
					
					Object value = ap.getValue(key);
					if(value!=null &&!"".equals(value)&&!"null".equals(value)){
						value = (value+"").replace("'","''");
						
						if(key.equals("_entityId")){
							valueStr.append(",ENTITY_ID='"+value+"'");
						}else if(key.equals("_entityType")){
							valueStr.append(",ENTITY_TYPE='"+value+"'");
						}else if(key.equals("level")){
							valueStr.append(",\"LEVEL\"='"+value+"'");
						}else if(key.equals("size")){
							valueStr.append(",\"SIZE\"='"+value+"'");
						}else{
							valueStr.append(","+key+"='"+value+"'");
						}

					}
				}
				if(valueStr!=null){
					result=valueStr.substring(1);
				}
			}
		}
		log.info("退出getUpdateAttributesSqlString(ApplicationEntity ap)方法 返回值为：result="+result);
		return result;
	}
	
	/**
	 * 保存时间格式为： yyyy-MM-dd HH:mm:ss
	 * @param beforeTime
	 * @return 假如返回出去的是null，表示传入的格式错误，或者传入的参数是null
	 */
	public static Date setTimeFormat(Object beforeTime){
		Date afterTime=null;
		try {
			if(beforeTime != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String strType=beforeTime.getClass().getName();
				if(strType.indexOf("Date")>-1){	//假如传入的参数是Date类型
					afterTime=sdf.parse(sdf.format(beforeTime));
				}else if(strType.indexOf("String")>-1 && !"".equals(beforeTime.toString())){ 	//假如传入的参数是String类型
					afterTime=sdf.parse(beforeTime.toString());
				}else{
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return afterTime;
	}
	/**
	 * 
	 * @description: 获取表字段属性查询输出字符串（oracle默认查出为大写） 别名
	 * @author：
	 * @param moduleName alterName
	 * @return     
	 * @return String     
	 * @date：Mar 19, 2013 5:23:06 PM
	 */
	public static String getSelectSqlAttributsString(String moduleName,String alterName){
		log.info("进入getSelectSqlAttributsString(String moduleName)方法,获取表字段属性查询输出字符串（oracle默认查出为大写）,moduleName="+moduleName);
		String result="";
		if(moduleName!=null || !"".equals(moduleName)){
			ApplicationModule am = ModuleProvider.getModule(moduleName);
			StringBuffer sf = new StringBuffer();
			if(am!=null){
				for(String key:am.keyset()){
					if(key.equals("_entityId")){
						sf.append(","+alterName+".ENTITY_ID  \"_entityId\"");
					}else if(key.equals("_entityType")){
						sf.append(","+alterName+".ENTITY_TYPE  \"_entityType\"");
					}else if(key.equals("level")){
						sf.append(","+alterName+".\"LEVEL\"  \""+key+"\"");
					}else if(key.equals("size")){
						sf.append(","+alterName+".\"SIZE\"  \""+key+"\"");
					}else if(key.equals("area_id")){
						sf.append(","+alterName+"."+key+"  \""+key+"\""+","+alterName+"."+key+"  \"id\"");
					}else if(key.equals("area_level")){
						sf.append(","+alterName+"."+key+"  \""+key+"\""+","+alterName+"."+key+"  \"level\"");
					}else{
						sf.append(","+alterName+"."+key+"  \""+key+"\"");
					}
					
				}
				if(!"Figurenode".equals(moduleName)&&!"Figureline".equals(moduleName)&&!"Figure".equals(moduleName)){
					sf.append(","+alterName+".ENTITY_ID  \"_entityId\"");
					sf.append(","+alterName+".ENTITY_TYPE  \"_entityType\"");
				}
				result = sf.substring(1);
			}
			if("Tree".equals(moduleName)){
				sf = new StringBuffer();
				sf.append(alterName+".ENTITY_TYPE  \"_entityType\","+moduleName+".treeName \"treeName\","+moduleName+".treeId \"treeId\"");
				result = sf+"";
			}else if("Treenode".equals(moduleName)){
				sf = new StringBuffer();
				sf.append(alterName+".ENTITY_TYPE  \"_entityType\","+moduleName+".aenType \"aenType\","+moduleName+".treeId \"treeId\","+moduleName+".nodeId \"nodeId\","+moduleName+".parentId \"parentId\","+moduleName+".aenId \"aenId\"");
				result = sf+"";
			}else if("Tbl_Unique_Uniquetable".equals(moduleName)){
				sf = new StringBuffer();
				sf.append(alterName+".ENTITY_TYPE  \"_entityType\","+moduleName+".code \"code\","+moduleName+".name \"name\","+moduleName+".ENTITY_ID  \"_entityId\"");				
				result = sf+"";
			}/*else if("Net_Maintenance_Records".equals(moduleName)){
				sf = new StringBuffer();
				sf.append(moduleName+".id \"id\","+moduleName+".\"_ENTITYID\"  \"_entityId\","+moduleName+".\"_ENTITYTYPE\"  \"_entityType\","+moduleName+".biz_module \"biz_module\" ,"+moduleName+".op_cause \"op_cause\" ,"+moduleName+".linkurl \"linkurl\","+moduleName+".op_scene \"op_scene\","+moduleName+".op_category \"op_category\" ,"+moduleName+".res_type  \"res_type\","+moduleName+".res_id \"res_id\","+moduleName+".res_keyinfo  \"res_keyinfo\","+moduleName+".content \"content\","+moduleName+".user_name \"user_name\","+moduleName+".user_account  \"user_account\","+moduleName+".src_teminal \"src_teminal\","+moduleName+".op_time  \"op_time\", "+moduleName+".longitude \"longitude\","+moduleName+".latitude  \"latitude\","+moduleName+".record_type \"record_type\" ");
				result = sf+"";
			}else if("Service_Maintenance".equals(moduleName)){
				sf = new StringBuffer();
				sf.append(moduleName+".id \"id\","+moduleName+".\"_ENTITYID\"  \"_entityId\","+moduleName+".\"_ENTITYTYPE\"  \"_entityType\","+moduleName+".biz_module \"biz_module\" ,"+moduleName+".biz_processcode \"biz_processcode\" ,"+moduleName+".biz_processId \"biz_processId\","+moduleName+".maintenance_id \"maintenance_id\" ");
				result = sf+"";
			}*/
			
		}
		log.info("退出getSelectSqlAttributsString(String moduleName)方法 返回值为：result="+result);
		return result;
	}
	
	
	/**
	 * 转换area_id与area_type(由于sys_area表与area表字段名不一致，现转换数据)
	* @author ou.jh
	* @date Jul 1, 2013 4:29:44 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public static void typeAreaIdAndAreaLevelMap(List<Map<String, Object>> list){
		if(list != null && list.size() > 0){
			for(Map<String, Object> m:list){
				if(m.get("area_id") != null && !m.get("area_id").equals("")){
					m.put("id", m.get("area_id"));
				}
				if(m.get("area_level") != null && !m.get("area_level").equals("")){
					m.put("level", m.get("area_level"));
				}
			}
		}
	}
	
	
	/**
	 * 转换area_id与area_type(由于sys_area表与area表字段名不一致，现转换数据)
	* @author ou.jh
	* @date Jul 1, 2013 4:29:44 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public static void typeAreaIdAndAreaLevelApp(List<ApplicationEntity> list){
		if(list != null && list.size() > 0){
			for(ApplicationEntity m:list){
				if(m.getValue("area_id") != null && !m.getValue("area_id").equals("")){
					m.set("id", m.getValue("area_id"));
				}
				if(m.getValue("area_level") != null && !m.getValue("area_level").equals("")){
					m.set("level", m.getValue("area_level"));
				}
			}
		}
	}
	
	
	/**
	 * 转换area_id与area_type(由于sys_area表与area表字段名不一致，现转换数据)
	* @author ou.jh
	* @date Jul 1, 2013 4:29:44 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public static BasicEntity typeAreaIdAndAreaLevelBas(BasicEntity app){
		BasicEntity de = app;
				if(app.getValue("area_id") != null && !app.getValue("area_id").equals("")){
					de.set("id", app.getValue("area_id"));
				}
		return de;
	}
	
	
	/**
	 * 转换area_id与area_type(由于sys_area表与area表字段名不一致，现转换数据)
	* @author ou.jh
	* @date Jul 1, 2013 4:29:44 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public static ApplicationEntity typeAreaIdAndAreaLevelApp(ApplicationEntity app){
		ApplicationEntity de = app;
				if(app.getValue("area_id") != null && !app.getValue("area_id").equals("")){
					de.set("id", app.getValue("area_id"));
				}
				if(app.getValue("area_level") != null && !app.getValue("area_level").equals("")){
					de.set("level", app.getValue("area_level"));
				}
		return de;
	}
	
	/**
	 * 资源类型是否为逻辑网元素
	* @author ou.jh
	* @date Jul 8, 2013 9:55:16 AM
	* @Description: TODO 
	* @param @param reType
	* @param @return        
	* @throws
	 */
	public static boolean isLinkType(String reType){
		String[] linkTypes = new String[]{
				"PipeRoute",
				"PoleRoute",
				"PipelineSection",
				"PolelineSection",
				"BuriedlineSection",
				"Fiber",
				"FiberSection",
				"FiberCore",
				"OpticalRoute",
				"OpticalRouteP2P",
				"OpticalRouteSP2SP",
				"TransmissionNetwork",
				"TransmissionSection",
				"WirelessChannel",
				"PigtailedFiber"
		};
		if(reType == null || reType.equals("")){
			return false;
		}else{
			boolean flag = false;
			for(String linkType:linkTypes){
				if(linkType.equals(reType)){
					flag = true;
				}else{
					continue;
				}
			}
			return flag;
		}
	}
}
