package com.iscreate.op.service.networkresourcemanage;



import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;


public class GisDispatchAreaResourceServiceImpl implements GisDispatchAreaResourceService {

	/** 依赖注入 ************************/
	private  StaffOrganizationService staffOrganizationService;
	private StructureCommonService structureCommonService;
	/** 属性 ************************/
	private Log log = LogFactory.getLog(this.getClass());


	/** service *******************************************/

	
	/**
	 * 根据用户账号,获取区域列表(省->...->街)
	 * @param accountId
	 * @return
	 */
	public List<Map<String,Object>> gisfindAreaListByUserId ( String accountId ) {
		
		List<Map<String, Object>> user_area_list = new ArrayList<Map<String,Object>>();
		List<BasicEntity> areaByUserId = this.staffOrganizationService.getAreaByUserId(accountId);
		for (int i = 0; areaByUserId != null && i < areaByUserId.size(); i++) {
			BasicEntity basicEntity = areaByUserId.get(i);
			Map<String, Object> map = basicEntity.toMap();
			user_area_list.add(map);
		}
		if ( user_area_list == null || user_area_list.size() == 0 ) {
			return null;
		}
		List<Map<String, Object>> result_user_area_list = new ArrayList<Map<String,Object>>();
		Map<String, Object> user_area = user_area_list.get(0);
		result_user_area_list.add(user_area);
		do {
			ApplicationEntity areaEntity = this.structureCommonService.getSectionEntity("Sys_Area", user_area.get("id")+"");
			List<Map<String, Object>> strutureSelationsApplicationMap = this.structureCommonService.getStrutureSelationsApplicationMap(areaEntity, "Sys_Area", AssociatedType.PARENT, "networkresourcemanage");
			if ( strutureSelationsApplicationMap == null || strutureSelationsApplicationMap.size() == 0 ) {
				break;
			}
			user_area = strutureSelationsApplicationMap.get(0);
			if ( user_area != null && user_area.size() > 0 ) {
				result_user_area_list.add(0,user_area);
			}
		} while ( user_area != null ) ;
		return result_user_area_list;
	}
	
	
	
	
	public static void main(String[] args) {
//		String a = "[{\"a\":\"1\"},{\"b\":\"2\"}]";
//		Gson gson = new GsonBuilder().create();
//		List<Map<String,String>> list = gson.fromJson(a, new TypeToken<List<Map<String,String>>>(){}.getType());
//		for (int i = 0; i < list.size(); i++) {
//			Map<String, String> map = list.get(i);
//			System.out.println(map);
//		}
		
		
//		Comparator<? super String> comparator = new Comparator<? super Map.Entry>(){
//
//			public int compare(Entry<String, String> o1,
//					Entry<String, String> o2) {
//				System.out.println(o1);
//				return 0;  
//			}  
//		};
//		
//		
//		TreeMap<String,String> a_map = new TreeMap<String, String>(comparator);
//		a_map.put("2", "中东");
//		a_map.put("3", "广州");
//		a_map.put("5", "北京");
//		a_map.put("4", "a");
//		a_map.put("6", "2");
//		
//		
//		System.out.println(a_map);
		
//		List<String> list = new ArrayList<String>();
//		list.add(0, "a");
//		list.add(0, "d");
//		list.add(0, "c");
//		System.out.println(list);
	}
	
	
	/**
	 * 城市列表(按省份)
	 * @param p_name - 区域名
	 */
	@SuppressWarnings("unused")
	public Map<String, Map<String, Object>> cityListByProvince( String p_name ) {
		Comparator cmp = Collator.getInstance(Locale.CHINA);
		Map<String, Map<String, Object>> result_map = new TreeMap<String, Map<String, Object>>(cmp);
		List<Map<String, Object>> provinces_list = new ArrayList<Map<String,Object>>();
		if ( p_name != null && !p_name.isEmpty() ) {
			//获取区域信息
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", p_name);
			List<List<ApplicationEntity>> entityListByEntityTypeRoAetg = this.structureCommonService.getEntityListByEntityTypeRoAetg("Sys_Area", map, "networkresourcemanage");
			Map<String, Object> area = null;
			if ( entityListByEntityTypeRoAetg != null && entityListByEntityTypeRoAetg.size() > 0 ) {
				List<ApplicationEntity> list = entityListByEntityTypeRoAetg.get(0);
				if( list != null && list.size() > 0 ) {
					ApplicationEntity applicationEntity = list.get(0);
					area = applicationEntity.toMap();
				}
			}
			if ( area == null || area.size() == 0 ) {
				return result_map;
			}
			String areaId = area.get("id")+"";
			ApplicationEntity areaEntity = this.structureCommonService.getSectionEntity("Sys_Area", areaId);
			List<Map<String, Object>> parent_area_list = this.structureCommonService.getStrutureSelationsApplicationMap(areaEntity, "Sys_Area", AssociatedType.PARENT, "networkresourcemanage");
			Map<String,Object> parent_areaEntity = null;
			if ( parent_area_list != null && parent_area_list.size() > 0 ) {
				parent_areaEntity = parent_area_list.get(0);
			}
			
			if( parent_areaEntity == null ) {
				return result_map;
			}
			Map<String, Object> parent_area = parent_areaEntity;
			if ( parent_area == null || parent_area.size() == 0 ) {
				//省
				provinces_list.add(area);
			} else {
				String p_areaId = parent_area.get("id")+"";
				ApplicationEntity g_areaEntity = this.structureCommonService.getSectionEntity("Sys_Area", p_areaId);
				List<Map<String, Object>> g_parent_area_list = this.structureCommonService.getStrutureSelationsApplicationMap(g_areaEntity, "Sys_Area", AssociatedType.PARENT, "networkresourcemanage");
				Map<String, Object> g_parent_area = null;
				if ( g_parent_area_list != null && g_parent_area_list.size() > 0 ) {
					g_parent_area = g_parent_area_list.get(0);
				}
				if ( g_parent_area == null || g_parent_area.size() == 0 )  {
					//市
					provinces_list.add(parent_area);
				} else {
					//区街
					return result_map;
				}
			}
		} else {
			// 查询所有省份
			List<BasicEntity> topLevelAreaList = this.staffOrganizationService.getTopLevelAreaList();
			provinces_list = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < topLevelAreaList.size(); i++) {
				BasicEntity basicEntity = topLevelAreaList.get(i);
				Map<String, Object> map = basicEntity.toMap();
				provinces_list.add(map);
			}
		}
		
		//拼装操作
		for (int i = 0; i < provinces_list.size(); i++) {
			Map<String, Object> province_info_map = provinces_list.get(i);
			String province_name = province_info_map.get("name")+"";// 省名
			String province_id = province_info_map.get("id")+"";// 省id
			// 获取省份拼音首字母
			String province_name_firstLetter = FirstLetterServiceImpl.getChinesePinYinFirstLetter(province_name);
			if (province_name.equals("北京市") || province_name.equals("香港")
					|| province_name.equals("天津") || province_name.equals("上海")
					|| province_name.equals("台湾") || province_name.equals("澳门")
					|| province_name.equals("钓鱼台")
					|| province_name.equals("重庆市")) {
				province_name = "其他";
				province_name_firstLetter = "其他";
			}
			Map<String, Object> province_result_map = null;// 省份数据及下级数据
			if (!result_map.containsKey(province_name_firstLetter)
					|| result_map.get(province_name_firstLetter) == null) {
				province_result_map = new HashMap<String, Object>();
			} else {
				province_result_map = result_map.get(province_name_firstLetter);
			}
			Map<String, Object> province_data_map = new HashMap<String, Object>();

			// 查询市级信息
			List<Map<String, Object>> city_list = null;

			if (province_name.equals("其他")) {
				Object object = province_result_map.get(province_name);
				if (object != null) {
					province_data_map = (Map<String, Object>) object;
					if (province_data_map.containsKey("child")
							&& province_data_map.get("child") != null) {
						city_list = (List<Map<String, Object>>) province_data_map.get("child");
					} else {
						city_list = new ArrayList<Map<String, Object>>();
					}
				} else {
					city_list = new ArrayList<Map<String, Object>>();
				}
				city_list.add(province_info_map);
			} else {
				ApplicationEntity province_Entity = this.structureCommonService.getSectionEntity("Sys_Area", province_id);
				city_list = this.structureCommonService.getStrutureSelationsApplicationMap(province_Entity, "Sys_Area", AssociatedType.CHILD, "networkresourcemanage");
			}

			province_data_map.put("child", city_list);
			// 保存省信息
			province_data_map.put("info", province_info_map);

			province_result_map.put(province_name, province_data_map);
			result_map.put(province_name_firstLetter, province_result_map);
		}
		
		return result_map;
	}
	
	
	/**
	 * 城市列表(按城市)
	 * @param p_name - 区域名
	 */
	public Map<String,Map<String,Object>> cityListByCity ( String p_name ) {
		Comparator cmp = Collator.getInstance(Locale.CHINA);
		Map<String, Map<String, Object>> result_map = new TreeMap<String, Map<String, Object>>(cmp);
		List<Map<String, Object>> provinces_list = new ArrayList<Map<String,Object>>();
		if ( p_name != null && !p_name.isEmpty() ) {
			
			//获取区域信息
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", p_name);
			List<List<ApplicationEntity>> entityListByEntityTypeRoAetg = this.structureCommonService.getEntityListByEntityTypeRoAetg("Sys_Area", map, "networkresourcemanage");
			Map<String, Object> area = null;
			if ( entityListByEntityTypeRoAetg != null && entityListByEntityTypeRoAetg.size() > 0 ) {
				List<ApplicationEntity> list = entityListByEntityTypeRoAetg.get(0);
				if( list != null && list.size() > 0 ) {
					ApplicationEntity applicationEntity = list.get(0);
					area = applicationEntity.toMap();
				}
			}
			if ( area == null || area.size() == 0 ) {
				return result_map;
			}
			String areaId = area.get("id")+"";
			ApplicationEntity areaEntity = this.structureCommonService.getSectionEntity("Sys_Area", areaId);
			List<Map<String, Object>> parent_area_list = this.structureCommonService.getStrutureSelationsApplicationMap(areaEntity, "Sys_Area", AssociatedType.PARENT, "networkresourcemanage");
			Map<String,Object> parent_areaEntity = null;
			if ( parent_area_list != null && parent_area_list.size() > 0 ) {
				parent_areaEntity = parent_area_list.get(0);
			}
			if( parent_areaEntity == null ) {
				return result_map;
			}
			Map<String, Object> parent_area = parent_areaEntity;
			if ( parent_area == null || parent_area.size() == 0 ) {
				//省
				provinces_list.add(area);
			} else {
				String p_areaId = parent_area.get("id")+"";
				ApplicationEntity g_parentEntity = this.structureCommonService.getSectionEntity("Sys_Area", p_areaId);
				List<Map<String, Object>> g_parent_area_list = this.structureCommonService.getStrutureSelationsApplicationMap(g_parentEntity, "Sys_Area", AssociatedType.PARENT, "networkresourcemanage");
				Map<String, Object> g_parent_area = null;
				
				if ( g_parent_area_list != null && g_parent_area_list.size() > 0 ) {
					g_parent_area = g_parent_area_list.get(0);
				}
				
				if ( g_parent_area == null || g_parent_area.size() == 0 )  {
					//市
					String area_name = area.get("name")+"";
					// 获取省份拼音首字母
					String city_name_firstLetter = FirstLetterServiceImpl.getChinesePinYinFirstLetter(area_name);
					List<Map<String,Object>> city_list = new  ArrayList<Map<String,Object>>();
					city_list.add(area);
					Map<String,Object> city_map = new HashMap<String, Object>();
					city_map.put("child", city_list);
					result_map.put(city_name_firstLetter, city_map);
					return result_map;
				} else {
					//区街
					return result_map;
				}
			}
		} else {
			// 查询所有省份
			List<BasicEntity> topLevelAreaList = this.staffOrganizationService.getTopLevelAreaList();
			provinces_list = new ArrayList<Map<String,Object>>();
			for (int i = 0; i < topLevelAreaList.size(); i++) {
				BasicEntity basicEntity = topLevelAreaList.get(i);
				Map<String, Object> map = basicEntity.toMap();
				provinces_list.add(map);
			}
		}
		
		if ( provinces_list == null ) {
			return result_map;
		}
		
		//拼装操作
		for (int i = 0; i < provinces_list.size(); i++) {
			Map<String, Object> province_info_map = provinces_list.get(i);
			String province_name = province_info_map.get("name")+"";// 省名
			String province_id = province_info_map.get("id")+"";// 省id
			ApplicationEntity provinceEntity = this.structureCommonService.getSectionEntity("Sys_Area", province_id);
			//查询下级资源
			List<Map<String, Object>> down_child_list = this.structureCommonService.getStrutureSelationsApplicationMap(provinceEntity, "Sys_Area", AssociatedType.CHILD, "networkresourcemanage");
			
			for (int j = 0; j < down_child_list.size(); j++) {
				Map<String, Object> city_map = down_child_list.get(j);
				String city_name = city_map.get("name")+"";// 市名
				// 获取省份拼音首字母
				String province_name_firstLetter = FirstLetterServiceImpl.getChinesePinYinFirstLetter(city_name);
				Map<String, Object> first_letter_map = result_map.get(province_name_firstLetter);
				if ( first_letter_map == null ) {
					first_letter_map = new HashMap<String, Object>();
				}
				
				List<Map<String,Object>> city_list = null;
				if ( !first_letter_map.containsKey("child") || first_letter_map.get("child") == null ) {
					city_list = new ArrayList<Map<String,Object>>();
				} else {
					city_list = (List<Map<String, Object>>) first_letter_map.get("child");
				}
				city_list.add(city_map);
				first_letter_map.put("child", city_list);
				result_map.put(province_name_firstLetter, first_letter_map);
			}
		}
		return result_map;
	}
	
	
	/**
	 * 根据区域id,获取下级区域id
	 * @param areaId - 区域id
	 * @return
	 */
	public List<Map<String, Object>> gisSubAreaList ( String areaId ) {
		ApplicationEntity areaEntity = this.structureCommonService.getSectionEntity("Sys_Area", areaId);
		List<Map<String, Object>> child_list = this.structureCommonService.getStrutureSelationsApplicationMap(areaEntity, "Sys_Area", AssociatedType.CHILD, "networkresourcemanage");
		return child_list;
	}

	/**
	 * 
	 * @description: 获取用户所属区域及其父区域id String List
	 * @author：yuan.yw
	 * @param userId
	 * @return     
	 * @return List<String>     
	 * @date：Mar 6, 2013 10:01:12 AM
	 */
	public List<String> getUserParentAreaAndAreaIdsList(String userId) {
		List<BasicEntity> areaByUserId = this.staffOrganizationService.getAreaByUserId(userId);
		List<String> user_AreaIds_List = new ArrayList<String>();
		StringBuffer user_AreaIds = new StringBuffer();
		StringBuffer user_parentAreaIds = new StringBuffer();
		if(areaByUserId!=null && areaByUserId.size()>0){
			for (BasicEntity be:areaByUserId) {
				user_AreaIds.append(","+be.getValue("id"));
				ApplicationEntity app = ApplicationEntity.changeFromEntity(be);
				//根据区域获取其子级区域
				ApplicationEntity[] childApps = structureCommonService.getAppArrsByRecursionForSrcSameType(app,"Sys_Area", "networkresourcemanage");
				if(childApps!=null && childApps.length>0){
					for(ApplicationEntity ae:childApps){
						String id = ae.getValue("id")+"";
						if(user_AreaIds.indexOf(","+id)<0){
							user_AreaIds.append(","+id);
						}
					}
				}
				List<ApplicationEntity> parentApps = getParentArea(app);
				if(parentApps!=null && parentApps.size()>0){
					for(ApplicationEntity ae:parentApps){
						String id = ae.getValue("id")+"";
						if(user_parentAreaIds.indexOf(","+id)<0){
							user_parentAreaIds.append(","+id);
						}
					}
				}
			}
			if(user_AreaIds!=null && !"".equals(user_AreaIds) ){
				user_AreaIds_List.add(user_AreaIds.substring(1)+"");
				user_AreaIds_List.add(user_parentAreaIds.substring(1)+"");
			}else{
				user_AreaIds_List.add("");
				user_AreaIds_List.add("");
			}
			
		}else{
			user_AreaIds_List.add(user_AreaIds+"");
			user_AreaIds_List.add(user_parentAreaIds+"");
		}
		return user_AreaIds_List;
		
	}

	//递归获取父级区域
	public List<ApplicationEntity> getParentArea(ApplicationEntity changeFromEntity){
		List<ApplicationEntity> resultList = new ArrayList<ApplicationEntity>();
		ApplicationEntity[] appArrsByRecursion = structureCommonService.getAppArrsByRecursion(changeFromEntity,new String[]{"Sys_Area"},AssociatedType.PARENT,"networkresourcemanage");
		if(appArrsByRecursion != null && appArrsByRecursion.length > 0){
			for(ApplicationEntity ap:appArrsByRecursion){
				resultList.add(ap);
				List<ApplicationEntity> listApp = getParentArea(ap);
				if(listApp!=null &&listApp.size()>0){
					resultList.addAll(listApp);
				}
			}	
			return resultList;
		}else{
			return null;
		}
	}


	/** getter setter ************************/
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
	
	

}
