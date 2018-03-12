package com.iscreate.op.service.gisdispatch;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.dao.gisdispatch.GisDispatchAreaDao;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.plat.tools.firstLetter.FirstLetterServiceImpl;

public class GisDispatchAreaServiceImpl implements GisDispatchAreaService {

	/** 依赖注入 ************************/
	private GisDispatchAreaDao gisDispatchAreaDao;
	private NetworkResourceInterfaceService networkResourceInterfaceService;
	/** 属性 ************************/
	private Log log = LogFactory.getLog(this.getClass());


	/** service *******************************************/

	
	/**
	 * 根据用户账号,获取区域列表(省->...->街)
	 * @param accountId
	 * @return
	 */
	public List<Map<String,String>> gisfindAreaListByUserId ( String accountId ) {
		List<Map<String, String>> user_area_list = this.networkResourceInterfaceService.getAreaByAccountService(accountId);
		if ( user_area_list == null || user_area_list.size() == 0 ) {
			return null;
		}
		List<Map<String, String>> result_user_area_list = new ArrayList<Map<String,String>>();
		Map<String, String> user_area = user_area_list.get(0);
		result_user_area_list.add(user_area);
		do {
			user_area = networkResourceInterfaceService.getParentAreaByAreaId(user_area.get("id"), "Sys_Area");
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
		List<Map<String, String>> provinces_list = new ArrayList<Map<String,String>>();
		if ( !StringUtil.isNullOrEmpty(p_name) ) {
			//获取区域信息
			Map<String, String> area = this.networkResourceInterfaceService.getResourceByReNameAndReTypeService(p_name,"Sys_Area");
			if ( area == null || area.size() == 0 ) {
				return result_map;
			}
			String areaId = area.get("id");
			Map<String, String> parent_area = this.networkResourceInterfaceService.getParentAreaByAreaId(areaId, "Sys_Area");
			if ( parent_area == null || parent_area.size() == 0 ) {
				//省
				provinces_list.add(area);
			} else {
				String p_areaId = parent_area.get("id");
				Map<String, String> g_parent_area = this.networkResourceInterfaceService.getParentAreaByAreaId(p_areaId, "Sys_Area");
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
			provinces_list = this.networkResourceInterfaceService.getTopAreaService();
		}
		
		//拼装操作
		for (int i = 0; i < provinces_list.size(); i++) {
			Map<String, String> province_info_map = provinces_list.get(i);
			String province_name = province_info_map.get("name");// 省名
			String province_id = province_info_map.get("id");// 省id
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
			List<Map<String, String>> city_list = null;

			if (province_name.equals("其他")) {
				Object object = province_result_map.get(province_name);
				if (object != null) {
					province_data_map = (Map<String, Object>) object;
					if (province_data_map.containsKey("child")
							&& province_data_map.get("child") != null) {
						city_list = (List<Map<String, String>>) province_data_map
								.get("child");
					} else {
						city_list = new ArrayList<Map<String, String>>();
					}
				} else {
					city_list = new ArrayList<Map<String, String>>();
				}
				city_list.add(province_info_map);
			} else {
				city_list = this.networkResourceInterfaceService
						.getResourceService(province_id, "Sys_Area", "Sys_Area",
								"CHILD");
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
		List<Map<String, String>> provinces_list = new ArrayList<Map<String,String>>();
		if ( !StringUtil.isNullOrEmpty(p_name) ) {
			//获取区域信息
			Map<String, String> area = this.networkResourceInterfaceService.getResourceByReNameAndReTypeService(p_name,"Sys_Area");
			if ( area == null || area.size() == 0 ) {
				return result_map;
			}
			String areaId = area.get("id");
			Map<String, String> parent_area = this.networkResourceInterfaceService.getParentAreaByAreaId(areaId, "Sys_Area");
			if ( parent_area == null || parent_area.size() == 0 ) {
				//省
				provinces_list.add(area);
			} else {
				String p_areaId = parent_area.get("id");
				Map<String, String> g_parent_area = this.networkResourceInterfaceService.getParentAreaByAreaId(p_areaId, "Sys_Area");
				if ( g_parent_area == null || g_parent_area.size() == 0 )  {
					//市
					String area_name = area.get("name");
					// 获取省份拼音首字母
					String city_name_firstLetter = FirstLetterServiceImpl.getChinesePinYinFirstLetter(area_name);
					List<Map<String,String>> city_list = new  ArrayList<Map<String,String>>();
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
			provinces_list = this.networkResourceInterfaceService.getTopAreaService();
		}
		
		if ( provinces_list == null ) {
			return result_map;
		}
		
		//拼装操作
		for (int i = 0; i < provinces_list.size(); i++) {
			Map<String, String> province_info_map = provinces_list.get(i);
			String province_name = province_info_map.get("name");// 省名
			String province_id = province_info_map.get("id");// 省id
			
			//查询下级资源
			List<Map<String, String>> down_child_list = this.networkResourceInterfaceService.getResourceService(province_id, "Sys_Area", "Sys_Area",
					"CHILD");
			
			for (int j = 0; j < down_child_list.size(); j++) {
				Map<String, String> city_map = down_child_list.get(j);
				String city_name = city_map.get("name");// 市名
				// 获取省份拼音首字母
				String province_name_firstLetter = FirstLetterServiceImpl.getChinesePinYinFirstLetter(city_name);
				Map<String, Object> first_letter_map = result_map.get(province_name_firstLetter);
				if ( first_letter_map == null ) {
					first_letter_map = new HashMap<String, Object>();
				}
				
				List<Map<String,String>> city_list = null;
				if ( !first_letter_map.containsKey("child") || first_letter_map.get("child") == null ) {
					city_list = new ArrayList<Map<String,String>>();
				} else {
					city_list = (List<Map<String, String>>) first_letter_map.get("child");
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
	public List<Map<String, String>> gisSubAreaList ( String areaId ) {
		List<Map<String, String>> child_liust = this.networkResourceInterfaceService.getResourceService(areaId, "Sys_Area", "Sys_Area","CHILD");
		return child_liust;
	}
	
	
	

	/** getter setter ************************/
	public GisDispatchAreaDao getGisDispatchAreaDao() {
		return gisDispatchAreaDao;
	}

	public void setGisDispatchAreaDao(GisDispatchAreaDao gisDispatchAreaDao) {
		this.gisDispatchAreaDao = gisDispatchAreaDao;
	}

	public NetworkResourceInterfaceService getNetworkResourceInterfaceService() {
		return networkResourceInterfaceService;
	}

	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}

}
