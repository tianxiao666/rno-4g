package com.iscreate.op.action.networkresourcemanage;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.informationmanage.common.ArrayUtil;
import com.iscreate.op.service.maintain.NetworkResourceMaintenanceService;
import com.iscreate.op.service.networkresourcemanage.ShowResOnMapService;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;


public class ShowResOnMapAction {
	// Structure公共类
	private StructureCommonService structureCommonService;
	private ShowResOnMapService showResOnMapService;
	Map<String, List<Map<String, Object>>> resMap = null;
	private NetworkResourceMaintenanceService networkResourceMaintenanceService;
	
	
	private String areaId = "1";
	
	private Double swLat;
	private Double swLng;
	private Double neLat;
	private Double neLng;
	
	/**
	 * 获取管道的相关设施信息
	 */
	public void getPipeRouteInfoAction() {
		ApplicationEntity areaEntity = structureCommonService.getSectionEntity(
				"Sys_Area", areaId);
		
		Map<String,Object> result_map = new HashMap<String, Object>();

		// 获取当前区域的站址.人井.电杆.挂墙点.标桩
		String[] resTypes = new String[] { "Station", "ManWell", "Pole",
				"HangWall", "MarkPost" };
		resMap = new HashMap<String, List<Map<String,Object>>>();
		for (int i = 0; i < resTypes.length; i++) {
			resMap.put(resTypes[i], new ArrayList<Map<String,Object>>());
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
//		String result = gson.toJson(resMap);
		String result = "";
		/*
		 * 获取当前区域下的所有管道段
		 */
		ApplicationEntity[] pipelineSectionEntityArray = structureCommonService
				.getStrutureSelationsApplicationEntity(areaEntity,
						"PipelineSection", AssociatedType.LINK,
						"networkresourcemanage");
		List<Map<String, Object>> pipelineList = new ArrayList<Map<String, Object>>();

		String[] connPipelineSectionStrings = new String[] { "ManWell", "Pole",
				"HangWall", "MarkPost", "Station", "FiberCrossCabinet" };

		if (pipelineSectionEntityArray != null) {
			for (ApplicationEntity pipelineSectionEntity : pipelineSectionEntityArray) {
				Map<String, Object> pipelineMap =pipelineSectionEntity.toMap();
				// 获取每条管道段的两端设施
				List<Map<String, Object>> connPipelineSectionList = this.structureCommonService
						.getStrutureSelationsReturnList(pipelineSectionEntity,
								connPipelineSectionStrings,
								AssociatedType.LINK, "networkresourcemanage");
				//System.out.println("管道段Facility===" + gson.toJson(connPipelineSectionList));
				pipelineMap.put("connFacility", connPipelineSectionList);
				
				for (int i = 0; i < connPipelineSectionList.size(); i++) {
					Map<String, Object> line_resource = connPipelineSectionList.get(i);
					String type = line_resource.get("_entityType")+"";
					if ( !resMap.containsKey(type) || resMap.get(type) == null ) {
						continue;
					} 
					List<Map<String, Object>> list = resMap.get(type);
					list.add(line_resource);
					resMap.put(type, list);
				}
				
				// 管道段所属管道
				ApplicationEntity pipeRouteEntity = structureCommonService
				.getStrutureSelationsEntity(pipelineSectionEntity,
						"PipeRoute", AssociatedType.LINK,
						"networkresourcemanage");
				pipelineMap.put("belong", pipeRouteEntity.toMap());
				pipelineList.add(pipelineMap);
			}
		}
		result_map.put("PipeLine", pipelineList);

		/*
		 * 获取当前区域下的所有直埋段
		 */
		ApplicationEntity[] buriedlineSectionEntityArray = structureCommonService
				.getStrutureSelationsApplicationEntity(areaEntity,
						"BuriedlineSection", AssociatedType.LINK,
						"networkresourcemanage");
		List<Map<String, Object>> buriedlineList = new ArrayList<Map<String, Object>>();

		String[] connBuriedlineSectionStrings = new String[] { "MarkPost",
				"Pole" };
		if (buriedlineSectionEntityArray != null) {
			// 获取每条直埋段的两端设施
			for (ApplicationEntity buriedlineSectionEntity : buriedlineSectionEntityArray) {
				Map<String, Object> buriedlineMap =buriedlineSectionEntity.toMap();
				List<Map<String, Object>> connBuriedlineSectionList = this.structureCommonService
						.getStrutureSelationsReturnList(
								buriedlineSectionEntity,
								connBuriedlineSectionStrings,
								AssociatedType.LINK, "networkresourcemanage");
				//System.out.println("直埋段Facility===" + gson.toJson(connBuriedlineSectionList));
				buriedlineMap.put("connFacility", connBuriedlineSectionList);
				
				
				for (int i = 0; i < connBuriedlineSectionList.size(); i++) {
					Map<String, Object> line_resource = connBuriedlineSectionList.get(i);
					String type = line_resource.get("_entityType")+"";
					if ( !resMap.containsKey(type) || resMap.get(type) == null ) {
						List<Map<String, Object>> list = resMap.get(type);
						list.add(line_resource);
						resMap.put(type, list);
					}
				}
				
				
				// 直埋段所属管道
				ApplicationEntity pipeRouteEntity = structureCommonService
				.getStrutureSelationsEntity(buriedlineSectionEntity,
						"PipeRoute", AssociatedType.LINK,
						"networkresourcemanage");
				buriedlineMap.put("belong", pipeRouteEntity.toMap());
				buriedlineList.add(buriedlineMap);
			}
		}
		result_map.put("BuriedLine", buriedlineList);

		/*
		 * 获取当前区域下的所有吊线段
		 */
		ApplicationEntity[] polelineSectionEntityArray = structureCommonService
				.getStrutureSelationsApplicationEntity(areaEntity,
						"PolelineSection", AssociatedType.LINK,
						"networkresourcemanage");
		List<Map<String, Object>> polelineList = new ArrayList<Map<String, Object>>();

		String[] connPolelineSectionStrings = new String[] { "HangWall", "Pole" };
		if (polelineSectionEntityArray != null) {
			// 获取每条吊线段的两端设施
			for (ApplicationEntity polelineSectionEntity : polelineSectionEntityArray) {
				Map<String, Object> polelineMap = polelineSectionEntity.toMap();
				List<Map<String, Object>> connPolelineSectionList = this.structureCommonService
						.getStrutureSelationsReturnList(polelineSectionEntity,
								connPolelineSectionStrings,
								AssociatedType.LINK, "networkresourcemanage");
				//System.out.println("吊线段Facility===" + gson.toJson(connPolelineSectionList));
				polelineMap.put("connFacility", connPolelineSectionList);

				for (int i = 0; i < connPolelineSectionList.size(); i++) {
					Map<String, Object> line_resource = connPolelineSectionList.get(i);
					String type = line_resource.get("_entityType")+"";
					if ( !resMap.containsKey(type) || resMap.get(type) == null ) {
						List<Map<String, Object>> list = resMap.get(type);
						list.add(line_resource);
						resMap.put(type, list);
					}
				}
				
				
				// 吊线段所属杆路
				ApplicationEntity poleRouteEntity = structureCommonService
						.getStrutureSelationsEntity(polelineSectionEntity,
								"PoleRoute", AssociatedType.LINK,
								"networkresourcemanage");
				polelineMap.put("belong", poleRouteEntity.toMap());
				polelineList.add(polelineMap);
			}
		}
		result_map.put("PoleLine", polelineList);
		result_map.putAll(resMap);
		result = gson.toJson(result_map);
		//System.out.println("finalResult===" + result);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取光缆的相关设施信息
	 */
	public void getFiberInfoAction() {
		ApplicationEntity areaEntity = structureCommonService.getSectionEntity(
				"Sys_Area", areaId);

		String result = "";
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();

		Map<String,Object> result_map = new HashMap<String, Object>();
		
		String[] resTypes = new String[] { "FiberCrossCabinet",
				"FiberDistributionCabinet", "FiberTerminalCase" , "ODF" };
		resMap = new HashMap<String, List<Map<String,Object>>>();
		for (int i = 0; i < resTypes.length; i++) {
			resMap.put(resTypes[i], new ArrayList<Map<String,Object>>());
		}
		result = "";
		//System.out.println("===="+resMap.get("FiberCrossCabinet").toString());
		
		/*
		// 查询当前区域下的站址
		List<Map<String, Object>> stationList = new ArrayList<Map<String, Object>>();
		ApplicationEntity[] stationEntityArray = structureCommonService
				.getStrutureSelationsApplicationEntity(areaEntity, "Station",
						AssociatedType.CHILD, "networkresourcemanage");
		if (stationEntityArray != null) {
			for (ApplicationEntity stationEntity : stationEntityArray) {
				stationList.add(stationEntity.toMap());
			}
		}
		String stationString = gson.toJson(stationList);
		result = result.substring(0, result.length() - 1) + ",\"Station\":"
				+ stationString;

		
		// 查询当前区域下站址相关的ODF
		List<Map<String, Object>> odfList = new ArrayList<Map<String, Object>>();
		if(stationEntityArray!=null)
		odfList = showResOnMapService.getOdf(stationEntityArray);
		String odfString = gson.toJson(odfList);
		result = result + ",\"ODF\":" + odfString;

		// 查询当前区域下的人井
		List<Map<String, Object>> manWellList = new ArrayList<Map<String, Object>>();
		ApplicationEntity[] manWellEntityArray = structureCommonService
				.getStrutureSelationsApplicationEntity(areaEntity, "ManWell",
						AssociatedType.CHILD, "networkresourcemanage");
		if (manWellEntityArray != null) {
			for (ApplicationEntity manWellEntity : manWellEntityArray) {
				manWellList.add(manWellEntity.toMap());
			}
		}
		String manWellString = gson.toJson(manWellList);
		result = result + ",\"ManWell\":" + manWellString;

		// 人井下的接头
		List<Map<String, Object>> jointList = showResOnMapService
				.getJoint(manWellEntityArray);

		// 查询当前区域下的电杆
		List<Map<String, Object>> poleList = new ArrayList<Map<String, Object>>();
		ApplicationEntity[] poleEntityArray = structureCommonService
				.getStrutureSelationsApplicationEntity(areaEntity, "Pole",
						AssociatedType.CHILD, "networkresourcemanage");
		if (poleEntityArray != null) {
			for (ApplicationEntity poleEntity : poleEntityArray) {
				poleList.add(poleEntity.toMap());
			}
		}
		String poleString = gson.toJson(poleList);
		result = result + ",\"Pole\":" + poleString;
		//System.out.println("poleString=="+poleString);
		
		// 电杆下的接头
		List<Map<String, Object>> poleJoint = showResOnMapService
				.getJoint(manWellEntityArray);
		if (poleJoint != null)
			jointList.addAll(poleJoint);
		String jointString = gson.toJson(jointList);
		result = result + ",\"Joint\":" + jointString;

		*/


		/*
		 * 查询当前区域下的所有光缆段
		 */

		ApplicationEntity[] fiberSectionEntityArray = structureCommonService
				.getStrutureSelationsApplicationEntity(areaEntity,
						"FiberSection", AssociatedType.LINK,
						"networkresourcemanage");
		// 获取每条光缆段的详细信息（包括所属光缆及光缆段两端的设施，有坐标的上级设施)
		List<Map<String, Object>> fiberSectionList = new ArrayList<Map<String, Object>>();
		if (fiberSectionEntityArray != null) {
			for(int i=0;i<fiberSectionEntityArray.length;i++){
				Map<String,Object> fiberSectionMap=fiberSectionEntityArray[i].toMap();
				Map<String, Object> fiberSectionFacility = showResOnMapService.getFiberSectionFacility(fiberSectionEntityArray[i]);
				fiberSectionMap.putAll(fiberSectionFacility);
				fiberSectionList.add(fiberSectionMap);
				if ( !fiberSectionFacility.containsKey("connFacility") || fiberSectionFacility.get("connFacility") == null ) {
					continue;
				}
				List<Map<String, Object>> section_list = (List<Map<String, Object>>) fiberSectionFacility.get("connFacility");
				for (int j = 0; j < section_list.size(); j++) {
					Map<String, Object> line_resource = section_list.get(j);
					String type = line_resource.get("_entityType")+"";
					if ( !resMap.containsKey(type) || resMap.get(type) == null ) {
						continue;
					} 
					List<Map<String, Object>> list = resMap.get(type);
					list.add(line_resource);
					resMap.put(type, list);
				}
			}
		}
		result_map.put("FiberSection", fiberSectionList);
		result_map.putAll(resMap);
		
		result = gson.toJson(result_map);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取纤芯的相关设施信息
	 */
	public void getFiberCoreInfoAction() {
		ApplicationEntity areaEntity = structureCommonService.getSectionEntity(
				"Sys_Area", areaId);
		String result = "";

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		// 查询当前区域下的光交接箱.光分纤箱.终端盒
		String[] resTypes = new String[] { "FiberCrossCabinet",
				"FiberDistributionCabinet", "FiberTerminalCase", };
		resMap = structureCommonService.getStrutureSelationsReturnMap(
				areaEntity, resTypes, AssociatedType.CHILD,
				"networkresourcemanage");
		result = gson.toJson(resMap);

		// 查询当前区域下的站址
		List<Map<String, Object>> stationList = new ArrayList<Map<String, Object>>();
		ApplicationEntity[] stationEntityArray = structureCommonService
				.getStrutureSelationsApplicationEntity(areaEntity, "Station",
						AssociatedType.CHILD, "networkresourcemanage");
		if (stationEntityArray != null) {
			for (ApplicationEntity stationEntity : stationEntityArray) {
				stationList.add(stationEntity.toMap());
			}
		}
		String stationString = gson.toJson(stationList);
		result = result.substring(0, result.length() - 1) + ",\"Station\":"
				+ stationString;

		// 查询当前区域下站址相关的ODF
		List<Map<String, Object>> odfList = new ArrayList<Map<String, Object>>();
		odfList = showResOnMapService.getOdf(stationEntityArray);
		String odfString = gson.toJson(odfList);
		result = result + ",\"ODF\":" + odfString;

		// 查询当前区域下的人井
		List<Map<String, Object>> manWellList = new ArrayList<Map<String, Object>>();
		ApplicationEntity[] manWellEntityArray = structureCommonService
				.getStrutureSelationsApplicationEntity(areaEntity, "ManWell",
						AssociatedType.CHILD, "networkresourcemanage");
		if (manWellEntityArray != null) {
			for (ApplicationEntity manWellEntity : manWellEntityArray) {
				manWellList.add(manWellEntity.toMap());
			}
		}
		String manWellString = gson.toJson(manWellList);
		result = result + ",\"ManWell\":" + manWellString;

		// 人井下的接头
		List<Map<String, Object>> jointList = showResOnMapService
				.getJoint(manWellEntityArray);

		// 查询当前区域下的电杆
		List<Map<String, Object>> poleList = new ArrayList<Map<String, Object>>();
		ApplicationEntity[] poleEntityArray = structureCommonService
				.getStrutureSelationsApplicationEntity(areaEntity, "Pole",
						AssociatedType.CHILD, "networkresourcemanage");
		if (poleEntityArray != null) {
			for (ApplicationEntity poleEntity : poleEntityArray) {
				poleList.add(poleEntity.toMap());
			}
		}
		String poleString = gson.toJson(poleList);
		result = result + ",\"Pole\":" + poleString;
		// 电杆下的接头
		List<Map<String, Object>> poleJoint = showResOnMapService
				.getJoint(manWellEntityArray);
		if (poleJoint != null)
			jointList.addAll(poleJoint);
		String jointString = gson.toJson(jointList);
		result = result + ",\"Joint\":" + jointString;

		//当前区域下的所有光缆段
		ApplicationEntity[] fiberSectionEntityArray = structureCommonService
		.getStrutureSelationsApplicationEntity(areaEntity,
				"FiberSection", AssociatedType.LINK,
				"networkresourcemanage");
		List<Map<String, Object>> fiberCoreList = new ArrayList<Map<String, Object>>();
		if(fiberSectionEntityArray!=null){
			for(ApplicationEntity fiberSectionEntity:fiberSectionEntityArray){
				//光缆段的详细信息（包括所属光缆及光缆段两端的设施，有坐标的上级设施)
				Map<String,Object> facilityMap =showResOnMapService.getFiberSectionFacility(fiberSectionEntity);
				//光缆段关联的纤芯
				ApplicationEntity[] fiberCoreArray= structureCommonService
				.getStrutureSelationsApplicationEntity(fiberSectionEntity, "FiberCore",
						AssociatedType.LINK, "networkresourcemanage");
				if(fiberCoreArray!=null){
					for(int i=0;i<fiberCoreArray.length;i++){
						Map<String,Object> fiberCoreMap=fiberCoreArray[i].toMap();
						fiberCoreMap.put("parent", fiberSectionEntity.toMap());
						fiberCoreMap.putAll(facilityMap);
						fiberCoreList.add(fiberCoreMap);
					}
				}
			}
		}
		//System.out.println("总纤芯数："+fiberCoreList.size());
		String fiberCoreString = gson.toJson(fiberCoreList);
		//System.out.println("fiberCoreList==" + fiberCoreString);
		result = result + ",\"FiberCore\":" + fiberCoreString+"}";
		
		//System.out.println("finalResult==="+result);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取光路的相关设施信息
	 */
	public void getOpticalRouteInfoAction() {
		ApplicationEntity areaEntity = structureCommonService.getSectionEntity(
				"Sys_Area", areaId);
		String result = "";

		Map<String,Object> result_map = new HashMap<String, Object>();
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();

		// 查询当前区域下的光交接箱.光分纤箱.终端盒
		String[] resTypes = new String[] { "FiberCrossCabinet",
				"FiberDistributionCabinet", "FiberTerminalCase" , "Terminal" };
		
		resMap = new HashMap<String, List<Map<String,Object>>>();
		for (int i = 0; i < resTypes.length; i++) {
			resMap.put(resTypes[i], new ArrayList<Map<String,Object>>());
		}
		result = "";
		/*
		resMap = structureCommonService.getStrutureSelationsReturnMap(
				areaEntity, resTypes, AssociatedType.CHILD,
				"networkresourcemanage");
		result = gson.toJson(resMap);

		// 查询当前区域下的站址
		List<Map<String, Object>> stationList = new ArrayList<Map<String, Object>>();
		ApplicationEntity[] stationEntityArray = structureCommonService
				.getStrutureSelationsApplicationEntity(areaEntity, "Station",
						AssociatedType.CHILD, "networkresourcemanage");
		if (stationEntityArray != null) {
			for (ApplicationEntity stationEntity : stationEntityArray) {
				stationList.add(stationEntity.toMap());
			}
		}
		String stationString = gson.toJson(stationList);
		result = result.substring(0, result.length() - 1) + ",\"Station\":"
				+ stationString;

		// 查询当前区域下站址相关的ODF
		List<Map<String, Object>> odfList = new ArrayList<Map<String, Object>>();
		odfList = showResOnMapService.getOdf(stationEntityArray);
		String odfString = gson.toJson(odfList);
		result = result + ",\"ODF\":" + odfString;

		*/
		
		// 查询所有局向光纤
		List<BasicEntity> opticalRouteSP2SPEntityList= null;//opticalRouteSP2SPService.getAllOpticalRouteEntity();
		//System.out.println(opticalRouteSP2SPEntityList.size());
		// 获取局向光纤的详细信息（包括局向光纤所属的光路纤芯，及两端的设施，有坐标的上级设施）
		List<Map<String, Object>> opticalRouteSP2SPList = new ArrayList<Map<String, Object>>();
		if(opticalRouteSP2SPEntityList!=null){
			for(int i=0;i<opticalRouteSP2SPEntityList.size();i++){
				ApplicationEntity app=ApplicationEntity.changeFromEntity(opticalRouteSP2SPEntityList.get(i));
				Map<String,Object> map=showResOnMapService.getOpticalRouteSP2SPFacility(app,areaId);
				if ( map == null ) {
					return;
				}
				opticalRouteSP2SPList.add(map);
				//TODO
				if ( !map.containsKey("connFacility") || map.get("connFacility") == null ) {
					continue;
				}
				List<Map<String, Object>> section_list = (List<Map<String, Object>>) map.get("connFacility");
				for (int j = 0; j < section_list.size(); j++) {
					Map<String, Object> line_resource = section_list.get(j);
					String type = line_resource.get("_entityType")+"";
					if ( !resMap.containsKey(type) || resMap.get(type) == null ) {
						continue;
					} 
					List<Map<String, Object>> list = resMap.get(type);
					list.add(line_resource);
					resMap.put(type, list);
				}
			}
		}
//		
//		ApplicationEntity[] opticalRouteSP2SPEntityArray = structureCommonService
//				.getStrutureSelationsApplicationEntity(areaEntity,
//						"OpticalRouteSP2SP", AssociatedType.LINK,
//						"networkresourcemanage");
		
		//System.out.println("opticalRouteSP2SPList==" + opticalRouteSP2SPString);
		result_map.put("OpticalRouteSP2SP", opticalRouteSP2SPList);
		result_map.putAll(resMap);
		result = gson.toJson(result_map);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取传输拓扑的相关设施信息
	 */
	public void getTransmissionInfoAction() {
		ApplicationEntity areaEntity = structureCommonService.getSectionEntity(
				"Sys_Area", areaId);
		String result = "";
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();

		// 查询当前区域下的站址
		String[] resTypes = new String[] { "Station" };
		resMap = structureCommonService.getStrutureSelationsReturnMap(
				areaEntity, resTypes, AssociatedType.CHILD,
				"networkresourcemanage");
		result = gson.toJson(resMap);

		// 当前区域下的所有传输段
		ApplicationEntity[] transmissionSectionEntityArray = structureCommonService
				.getStrutureSelationsApplicationEntity(areaEntity,
						"TransmissionSection", AssociatedType.LINK,
						"networkresourcemanage");
		// 获取传输段的详细信息（包括传输段所属的传输系统,关联的光路纤芯,两端的设施，有坐标的上级设施）
		List<Map<String, Object>> transmissionSectionList = new ArrayList<Map<String, Object>>();
		if (transmissionSectionEntityArray != null) {
			transmissionSectionList = showResOnMapService
					.getTransmissionSectionFacility(transmissionSectionEntityArray, areaId);
		}
		String transmissionSectionString = gson.toJson(transmissionSectionList);
		//System.out.println("transmissionSectionString=="
		//		+ transmissionSectionString);
		result = result.substring(0, result.length() - 1)
				+ ",\"TransmissionSection\":" + transmissionSectionString+"}";
	
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 查询当前区域下的八种主要设施
	 */
	public void getMarkersAction(){
		resMap = new LinkedHashMap<String, List<Map<String,Object>>>();
		String result = "";
		HttpServletRequest request=ServletActionContext.getRequest();
		String type=request.getParameter("resType");
		String resTypes[]=type.split(",");
		
		//LatLngBounds bounds = new LatLngBounds(swLat,swLng,neLat,neLng);
		
		List<String> resourceTypes = new ArrayList<String>();
		List<String> baseStationTypes = new ArrayList<String>();
		List<String> stationTypes = new ArrayList<String>();
		
		boolean needToFindBaseStation = false;
		for ( int i = 0 ; i < resTypes.length ; i++ ) {
			String resString = resTypes[i];
			resourceTypes.add(resString);
			if ( 	resString.equalsIgnoreCase("GeneralBaseStation") || 
					resString.equalsIgnoreCase("BaseStation_GSM") || 
					resString.equalsIgnoreCase("BaseStation_repeater") ||
					resString.equalsIgnoreCase("BaseStation_TD") ||
					resString.equalsIgnoreCase("BaseStation_WLAN") || 
					resString.equalsIgnoreCase("BaseStation") ) {
				needToFindBaseStation = true;
			}
		}
		
		resTypes = new String[resourceTypes.size()];
		resourceTypes.toArray(resTypes);
		
		List<Map<String, Object>> subArea = getSubArea(areaId);
		
		List<String> areaIds = new ArrayList<String>();
		areaIds.add(areaId);
		//查询该区域下的所有子区域
		for ( int i = 0 ; i < subArea.size() ; i++ ) {
			areaIds.add(subArea.get(i).get("id").toString());
		}
		//查询当前区域下的八种主要设施
		for (  int i = 0 ; i < areaIds.size() ; i++ ) {
			//获取区域实例
			ApplicationEntity areaEntity = structureCommonService.getSectionEntity(
					"Sys_Area", areaIds.get(i));
			//根据区域查询八大资源
			Map<String, List<Map<String, Object>>> strutureSelationsReturnMap = structureCommonService.getStrutureSelationsReturnMap(
					areaEntity, resTypes, AssociatedType.CHILD,"networkresourcemanage");
			if ( needToFindBaseStation ) {
				//根据区域查询基站
				List<BasicEntity> baseStationByArea = structureCommonService.getBaseStationByArea( areaIds.get(i) , "GeneralBaseStation" );
				if ( !ArrayUtil.isNullOrEmpty(baseStationByArea) ) {
					List<Map<String, Object>> baseStation_list = new ArrayList<Map<String,Object>>();
					for (int j = 0; j < baseStationByArea.size() ; j++) {
						BasicEntity basicEntity = baseStationByArea.get(j);
						baseStation_list.add(basicEntity.toMap());
					}
					strutureSelationsReturnMap.put("BaseStation", baseStation_list );
				}
			}
			//组合返回集合
			for ( Iterator<String> it = strutureSelationsReturnMap.keySet().iterator() ; it.hasNext() ; ) {
				String key = it.next();
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				if ( resMap.containsKey(key) ) {
					list = resMap.get(key);
				}
				List<Map<String, Object>> strutureList = strutureSelationsReturnMap.get(key);
				/*
				List<Map<String, Object>> struture_new_list = new ArrayList<Map<String,Object>>();
				//根据坐标,筛选
				for (int j = 0; j < strutureList.size(); j++) {
					Map<String, Object> map = strutureList.get(j);
					if ( !map.containsKey("latitude") || !map.containsKey("longitude") || map.get("latitude") == null || map.get("longitude") == null ) {
						continue;
					}
					String lat = map.get("latitude")+"";
					String lng = map.get("longitude")+"";
					if ( lat.isEmpty() || lng.isEmpty() ) {
						continue;
					}
					Double lattitude = Double.valueOf(lat);
					Double longitude = Double.valueOf(lng);
					LatLng point = new LatLng(lattitude,longitude);
					boolean flag = point.containedIn(bounds);
					if ( !flag ) {
						System.out.println(map.get("stationName"));
						continue;
					} else {
						struture_new_list.add(map);
					}
				}*/
				list.addAll(strutureList);
				if ( list == null || list.size() == 0 ) {
					it.remove();
					resMap.remove(key);
					continue;
				}
				resMap.put(key,list);
			}
		}
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		result = gson.toJson(resMap); 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	/**
	 * 根据区域实例，获取最高级区域
	 * @param area 区域实例
	 * @return
	 */
	public BasicEntity getTopArea ( BasicEntity area ) {
		ApplicationEntity app = ApplicationEntity.changeFromEntity(area);
		List<Map<String, Object>> parentList = structureCommonService.getStrutureSelationsApplicationMap(app, "Sys_Area", AssociatedType.PARENT , "networkresourcemanage");
		if ( parentList == null || parentList.size() <= 0 || parentList.get(0) == null ) {
			return area;
		} else {
			Map<String, Object> map = parentList.get(0);
			BasicEntity ae = ApplicationEntity.fromMap(map);
			return getTopArea(ae);
		}
	}
	
	
	/**
	 * 根据区域实例，获取下级区域
	 */
	public List<Map<String, Object>> getSubArea( BasicEntity area ) {
		ApplicationEntity app = ApplicationEntity.changeFromEntity(area);
		List<Map<String, Object>> childList = structureCommonService.getStrutureSelationsApplicationMap(app, "Sys_Area", AssociatedType.CHILD , "networkresourcemanage");
		return childList;
	}
	
	
	/**
	 * 根据区域实例，获取下级区域
	 */
	public List<Map<String, Object>> getSubArea( String areaId ) {
		ApplicationEntity app = structureCommonService.getSectionEntity("Sys_Area",areaId);
		ApplicationEntity[] arr = structureCommonService.getAppArrsByRecursionForSrcSameType(app, "Sys_Area", "networkresourcemanage");
		List<Map<String, Object>> childList = new ArrayList<Map<String,Object>>();
		for (int i = 0; arr != null && i < arr.length; i++) {
			ApplicationEntity ae = arr[i];
			childList.add(ae.toMap());
		}
		return childList;
	}
	
	
	/**
	 * 根据父区域，获取以下所有子区域(递归)
	 * @param area 父区域
	 * @return
	 */
	public List<Map<String, Object>> getChildArea ( BasicEntity area ) {
		List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
		
		ApplicationEntity app = ApplicationEntity.changeFromEntity(area);
		List<Map<String, Object>> childList = structureCommonService.getStrutureSelationsApplicationMap(app, "Sys_Area", AssociatedType.CHILD , "networkresourcemanage");
		if ( childList != null && childList.size()>0 ) {
			for ( Map<String,Object> lmap : childList) {
				Map<String, Object> objMap = new HashMap<String,Object>();
				Map<String, Object> dataMap = new HashMap<String,Object>();
				BasicEntity fromMap = ApplicationEntity.fromMap(lmap);
				dataMap.put("entity", lmap);
				List<Map<String, Object>> childArea = getChildArea(fromMap);
				dataMap.put("child", childArea);
				objMap.put(lmap.get("name").toString(), dataMap);
				listmap.add(objMap);
			}
		} 
		return listmap;
	}
	
	
	
	
	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}

	

	public void setShowResOnMapService(ShowResOnMapService showResOnMapService) {
		this.showResOnMapService = showResOnMapService;
	}



	public NetworkResourceMaintenanceService getNetworkResourceMaintenanceService() {
		return networkResourceMaintenanceService;
	}

	public void setNetworkResourceMaintenanceService(
			NetworkResourceMaintenanceService networkResourceMaintenanceService) {
		this.networkResourceMaintenanceService = networkResourceMaintenanceService;
	}

	public Double getSwLat() {
		return swLat;
	}

	public void setSwLat(Double swLat) {
		this.swLat = swLat;
	}

	public Double getSwLng() {
		return swLng;
	}

	public void setSwLng(Double swLng) {
		this.swLng = swLng;
	}

	public Double getNeLat() {
		return neLat;
	}

	public void setNeLat(Double neLat) {
		this.neLat = neLat;
	}

	public Double getNeLng() {
		return neLng;
	}

	public void setNeLng(Double neLng) {
		this.neLng = neLng;
	}
	
	
	
}
