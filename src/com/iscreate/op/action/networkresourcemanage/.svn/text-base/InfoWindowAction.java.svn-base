package com.iscreate.op.action.networkresourcemanage;



import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.service.maintain.NetworkResourceMaintenanceService;
import com.iscreate.op.service.networkresourcemanage.ShowResOnMapService;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;

public class InfoWindowAction {
	// Structure公共类
	private StructureCommonService structureCommonService;
	private ShowResOnMapService showResOnMapService;
	Map<String, List<Map<String, Object>>> resMap = null;
	private NetworkResourceMaintenanceService networkResourceMaintenanceService;
	
	private final static Map<String , String[][]> res2Area_Map;					//根据资源查询区域

	static {
		res2Area_Map = new LinkedHashMap<String, String[][]>();
		init();
	}
	


	/**
	 * 根据资源id、类型,获取区域
	 */
	@SuppressWarnings({"unchecked","unused"})
	public void getResourceArea() {
		String[] param_arr = new String[]{"resourceType","resourceId"};
		Map<String,Object> param_map = 	ActionUtil.getRequestParaMap(param_arr);
		String resourceType = param_map.get("resourceType")+"";
		String resourceId = param_map.get("resourceId")+"";
		String[][] res_map = res2Area_Map.get(resourceType);
		ApplicationEntity area_app = null;
		if ( 	resourceType.equals("BaseStation") || 
				resourceType.equals("BaseStation_GSM") || 
				resourceType.equals("BaseStation_repeater") || 
				resourceType.equals("BaseStation_TD") || 
				resourceType.equals("BaseStation_WLAN") || 
				resourceType.equals("GeneralBaseStation") ) {
			BasicEntity areaByStation = null;
			areaByStation = structureCommonService.getAreaByStation(resourceId, resourceType);
			area_app = ApplicationEntity.changeFromEntity(areaByStation);
		} else {
			for (int i = 0; res_map != null && i < res_map.length; i++) {
				ApplicationEntity areaEntity = structureCommonService.getSectionEntity(resourceType, resourceId);
				String[] structure = res_map[i];
				ApplicationEntity[] areaAE = structureCommonService.getAppArrsByRecursion(areaEntity, structure , AssociatedType.PARENT, "networkresourcemanage");
				if ( areaAE != null && areaAE.length > 0 ) {
					area_app = areaAE[0];
					break;
				}
			}
		}
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if ( area_app != null  ) {
			map = area_app.toMap();
		}
		try {
			ActionUtil.responseWrite(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取资源图片
	 */
	public void getResourceImg () {
		String[] param_arr = new String[]{"resourceType","resourceId"};
		Map<String,Object> param_map = 	ActionUtil.getRequestParaMap(param_arr);
		String resourceType = param_map.get("resourceType")+"";
		String resourceId = param_map.get("resourceId")+"";
		ApplicationEntity areaEntity = structureCommonService.getSectionEntity(resourceType, resourceId);
		ApplicationEntity[] areaAE = structureCommonService.getAppArrsByRecursion(areaEntity, new String[]{"Photo"} , AssociatedType.CHILD, "networkresourcemanage");
		List<Map<String,Object>> ae_map  = new ArrayList<Map<String,Object>>();
		for (int i = 0; areaAE!= null && i < areaAE.length; i++) {
			ae_map.add(areaAE[i].toMap());
		}
		try {
			ActionUtil.responseWrite(ae_map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化操作
	 */      
	public static void init () {
		res2Area_Map.put("Station", new String[][]{{"Sys_Area"}});
		res2Area_Map.put("ManWell", new String[][]{{"Sys_Area"}});
		res2Area_Map.put("Pole", new String[][]{{"Sys_Area"}});
		res2Area_Map.put("HangWall", new String[][]{{"Sys_Area"}});
		res2Area_Map.put("MarkPost", new String[][]{{"Sys_Area"}});
		res2Area_Map.put("FiberCrossCabinet", new String[][]{{"Sys_Area"}});
		res2Area_Map.put("FiberDistributionCabinet", new String[][]{{"Sys_Area"}});
		res2Area_Map.put("FiberTerminalCase", new String[][]{{"Sys_Area"}});
		res2Area_Map.put("BaseStation", new String[][]{
								{"Room","Station","Sys_Area"},
								{"Stairs","Station","Sys_Area"},
								{"Corridor","Station","Sys_Area"},
								{"Passage","Station","Sys_Area"}});
		res2Area_Map.put("BaseStation_GSM", new String[][]{
								{"Room","Station","Sys_Area"},
								{"Stairs","Station","Sys_Area"},
								{"Corridor","Station","Sys_Area"},
								{"Passage","Station","Sys_Area"}});
		res2Area_Map.put("BaseStation_repeater", new String[][]{
								{"Room","Station","Sys_Area"},
								{"Stairs","Station","Sys_Area"},
								{"Corridor","Station","Sys_Area"},
								{"Passage","Station","Sys_Area"}});
		res2Area_Map.put("BaseStation_TD", new String[][]{
								{"Room","Station","Sys_Area"},
								{"Stairs","Station","Sys_Area"},
								{"Corridor","Station","Sys_Area"},
								{"Passage","Station","Sys_Area"}});
		res2Area_Map.put("BaseStation_WLAN", new String[][]{
								{"Room","Station","Sys_Area"},
								{"Stairs","Station","Sys_Area"},
								{"Corridor","Station","Sys_Area"},
								{"Passage","Station","Sys_Area"}});
	}
	
	
	
	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}
	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}
	public ShowResOnMapService getShowResOnMapService() {
		return showResOnMapService;
	}
	public void setShowResOnMapService(ShowResOnMapService showResOnMapService) {
		this.showResOnMapService = showResOnMapService;
	}

	public Map<String, List<Map<String, Object>>> getResMap() {
		return resMap;
	}
	public void setResMap(Map<String, List<Map<String, Object>>> resMap) {
		this.resMap = resMap;
	}
	public NetworkResourceMaintenanceService getNetworkResourceMaintenanceService() {
		return networkResourceMaintenanceService;
	}
	public void setNetworkResourceMaintenanceService(
			NetworkResourceMaintenanceService networkResourceMaintenanceService) {
		this.networkResourceMaintenanceService = networkResourceMaintenanceService;
	}
}
