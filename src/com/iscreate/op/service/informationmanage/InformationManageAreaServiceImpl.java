package com.iscreate.op.service.informationmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.dao.informationmanage.InformationManageAreaDao;
import com.iscreate.op.pojo.informationmanage.Area;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;


@SuppressWarnings("unchecked")
public class InformationManageAreaServiceImpl extends BaseServiceImpl<Area> implements InformationManageAreaService {
	
	InformationManageAreaDao informationManageAreaDao; 
	NetworkResourceInterfaceService networkResourceInterfaceService;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * 获取所有省份信息
	 * @return 省份信息集合
	 */
	public List<Map<String,String>> getAllProvince () {
		List<Map<String,String>> list_map = this.networkResourceInterfaceService.getTopAreaService();
		return list_map;
	}
	
	public void getSubAreaRecursion ( List<Map<String,String>> list , List<Map<String,Object>> list_map , int lv , int maxLv ) {
		if ( lv > maxLv ) {
			return;
		}
		for (int i = 0; list != null && i < list.size(); i++) {
			Map<String,Object> data_map = new HashMap<String, Object>();
			Map<String, String> map = list.get(i);
			if ( map == null ) {
				return;
			}
			data_map.put("data",map);
			String areaId = map.get("id");
			List<Map<String, String>> child_list = networkResourceInterfaceService.getResourceService( areaId , "Sys_Area" , "Sys_Area" , "CHILD" );
			if ( child_list != null && child_list.size() > 0 ) {
				List<Map<String,Object>> new_list = new ArrayList<Map<String,Object>>();
				int nextLv = lv + 1;
				this.getSubAreaRecursion(child_list,new_list,nextLv,maxLv);
				data_map.put("child",new_list);
			}
			list_map.add(data_map);
		}
	}
	
	
	public List<Map<String,String>> getSubArea( String areaId ) {
		List<Map<String, String>> child_list = networkResourceInterfaceService.getResourceService( areaId , "Sys_Area" , "Sys_Area" , "CHILD" );
		return child_list;
	}
	
	
	public List<Map<String, Object>> getAreaTreeView ( int max , String provinceId ) {
		List<Map<String,String>> pros = null;
		if ( StringUtil.isNullOrEmpty(provinceId) || provinceId.equals("0") ) {
			pros = this.networkResourceInterfaceService.getTopAreaService();
		} else {
			Map<String, String> area = this.networkResourceInterfaceService.getResourceByReIdAndReTypeService(provinceId, "Sys_Area");
			pros = new ArrayList<Map<String,String>>();
			pros.add(area);
		}
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		this.getSubAreaRecursion(pros,list,1,max);
		return list;
	}
	

	public InformationManageAreaDao getInformationManageAreaDao() {
		return informationManageAreaDao;
	}
	public void setInformationManageAreaDao(
			InformationManageAreaDao informationManageAreaDao) {
		this.informationManageAreaDao = informationManageAreaDao;
	}
	public NetworkResourceInterfaceService getNetworkResourceInterfaceService() {
		return networkResourceInterfaceService;
	}
	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}

	
	
	
}
