package com.iscreate.op.service.networkresourcemanage;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;

public class ShowResOnMapServiceImpl implements ShowResOnMapService {

	private StructureCommonService structureCommonService;
	// 光缆段两端的设施类型
	private String fiberSectionFacilityTypes[] = new String[] { "ODF", "Joint",
			"FiberCrossCabinet", "FiberDistributionCabinet",
			"FiberTerminalCase" };

	// 纤芯两端的设施类型
	private String fiberCoreFacilityTypes[] = new String[] { "Terminal",
			"Joint" };
	// 局向纤芯两端的设施类型
	private String OpticalRouteP2PFacilityTypes[] = new String[] { "Terminal" , "Joint" };

	// 传输段两端的设施类型
	private String transmissionSectionFacilityTypes[] = new String[] {
			"NetworkElement", "TransmissionPort" };

	GsonBuilder builder = new GsonBuilder();
	Gson gson = builder.create();
	
	/**
	 * 获取每条光缆段的详细信息（包括所属光缆及光缆段两端的设施，有坐标的上级设施)
	 * 
	 * @param fiberSectionEntityArray
	 * @return
	 */
	public Map<String, Object> getFiberSectionFacility(
			ApplicationEntity fiberSectionEntity) {

			Map<String, Object> fiberSectionMap = new HashMap<String, Object>();
			// 每条光缆段所属的光缆
			ApplicationEntity fiberEntity=structureCommonService.getStrutureSelationsEntity(fiberSectionEntity,"Fiber",AssociatedType.LINK,"networkresourcemanage");
			fiberSectionMap.put("belong", fiberEntity.toMap());
			// 每条光缆段两端的设施
			List<Map<String, Object>> connfacilityList = this.structureCommonService
					.getStrutureSelationsReturnList(fiberSectionEntity,
							fiberSectionFacilityTypes, AssociatedType.LINK,
							"networkresourcemanage");
			fiberSectionMap.put("connFacility", connfacilityList);
			//System.out.println("光缆段两端Facility=="+gson.toJson(connfacilityList));
			List<Map<String, Object>> facilityList = new ArrayList<Map<String, Object>>();
			if (connfacilityList != null) {
				for (int i = 0; i < connfacilityList.size(); i++) {
					Map<String, Object> map = connfacilityList.get(i);
					if (map.get("_entityType")
							.equals("ODF")) {
						String id = connfacilityList.get(i).get("id")
								.toString();
						ApplicationEntity odfEntity = structureCommonService
								.getSectionEntity("ODF", id);
						ApplicationEntity parentEntity = this
								.getOdfParent(odfEntity);
						
						Map<String, Object> map2 = parentEntity.toMap();
						
						
						if ( parentEntity != null ) {
							Map<String, List<Map<String, Object>>> strutureSelationsApplicationMapList = this.structureCommonService.getStrutureSelationsApplicationMapList(parentEntity, "Station", AssociatedType.PARENT, "networkresourcemanage");
							List<Map<String, Object>> station_list = strutureSelationsApplicationMapList.get("Station");
							if ( station_list != null && station_list.size() > 0 ) {
								Map<String, Object> station_map = station_list.get(0);
								String lat = station_map.get("latitude")+"";
								String lng = station_map.get("longitude")+"";
								map.put("latitude", lat);
								map.put("longitude", lng);
								map2.put("latitude", lat);
								map2.put("longitude", lng);
							}
						}
						
						facilityList.add(map2);
					} else if (map.get("_entityType")
							.equals("Joint")) {
						String id = connfacilityList.get(i).get("id")
								.toString();
						ApplicationEntity jointEntity = structureCommonService
								.getSectionEntity("Joint", id);
						ApplicationEntity parentEntity = this
								.getJointParent(jointEntity);
						if ( parentEntity != null ) {
							String lat = parentEntity.getValue("latitude")+"";
							String lng = parentEntity.getValue("longitude")+"";
							map.put("latitude", lat);
							map.put("longitude", lng);
							Map<String, Object> map2 = parentEntity.toMap();
							map2.put("latitude", lat);
							map2.put("longitude", lng);
							facilityList.add(map2);
						}
					} else {
						facilityList.add(connfacilityList.get(i));
					}
				}
			}
			fiberSectionMap.put("parentFacility", facilityList);
			//System.out.println("光缆段parentFacility=="+gson.toJson(facilityList));
		return fiberSectionMap; 
	}

	/**
	 * 获取纤芯的详细信息（包括纤芯所属的光缆段，及两端的设施，有坐标的上级设施）
	 * 
	 * @param fiberSectionEntityArray
	 * @return
	 */
	public List<Map<String, Object>> getFiberCoreFacility(
			ApplicationEntity[] fiberCoreEntityArray) {
		List<Map<String, Object>> fiberCoreList = new ArrayList<Map<String, Object>>();
		for (ApplicationEntity fiberCoreEntity : fiberCoreEntityArray) {
			Map<String, Object> fiberCoreMap = new HashMap<String, Object>();
			fiberCoreMap.put("line", fiberCoreEntity.toMap());
			// 每条纤芯所属的光缆段
			ApplicationEntity fiberSectionEntity= structureCommonService
					.getStrutureSelationsEntity(fiberCoreEntity,
							"FiberSection", AssociatedType.LINK,
							"networkresourcemanage");
			fiberCoreMap.put("belong", fiberSectionEntity.toMap());
			// 每条纤芯两端的设施
			List<Map<String, Object>> connfacilityList = this.structureCommonService
					.getStrutureSelationsReturnList(fiberCoreEntity,
							fiberCoreFacilityTypes, AssociatedType.LINK,
							"networkresourcemanage");
			fiberCoreMap.put("connfacility", connfacilityList);
			if(connfacilityList.size()>0){
				//System.out.println(fiberCoreEntity.toMap()+"纤芯两端设施个数为："+connfacilityList.size()+",Facility=="+gson.toJson(connfacilityList));
			}
			// 查找无坐标的设施的上级
			List<Map<String, Object>> facilityList = new ArrayList<Map<String, Object>>();
			if (connfacilityList != null) {
				for (int i = 0; i < connfacilityList.size(); i++) {
					// Terminal的父(FiberCrossCabinet,FiberDistributionCabinet,FiberTerminalCase,ODF,ODM)
					if (connfacilityList.get(i).get("_entityType").equals(
							"Terminal")) {
						String id = connfacilityList.get(i).get("id")
								.toString();
						ApplicationEntity terminalEntity = structureCommonService
								.getSectionEntity("Terminal", id);
						ApplicationEntity[] parent = structureCommonService
								.getStrutureSelationsApplicationEntity(
										terminalEntity, AssociatedType.PARENT,
										"networkresourcemanage");
						if (parent.length>0) {
							// ODM 的父(FiberCrossCabinet,ODF)
							if (parent[0].getValue("_entityType").equals("ODM")) {
								String tempid = parent[0].getValue("id").toString();
						ApplicationEntity ODMEntity = structureCommonService
								.getSectionEntity("ODM", tempid);
								ApplicationEntity[] parentEntity = structureCommonService
										.getStrutureSelationsApplicationEntity(
												ODMEntity,
												AssociatedType.PARENT,
												"networkresourcemanage");
								if (parentEntity.length>0) {
									if (parentEntity[0].getValue("_entityType")
											.equals("ODF")) {
										String entityid =parentEntity[0].getValue("id").toString();
										ApplicationEntity odfEntity = structureCommonService
												.getSectionEntity("ODF",
														entityid);
										ApplicationEntity parentAppEntity = this
												.getOdfParent(odfEntity);
										facilityList.add(parentAppEntity
												.toMap());
									} else {
										facilityList.add(parentEntity[0]
												.toMap());
									}
								}
							}else if(parent[0].getValue("_entityType").equals("ODF")){
								String entityid = parent[0].getValue("id").toString();
								ApplicationEntity odfEntity = structureCommonService
										.getSectionEntity("ODF", entityid);
								ApplicationEntity parentAppEntity = this
										.getOdfParent(odfEntity);
								facilityList.add(parentAppEntity.toMap());
							} else {

								facilityList.add(parent[0].toMap());
							}
						}
					} else if (connfacilityList.get(i).get("_entityType")
							.equals("Joint")) {
						String id = connfacilityList.get(i).get("id")
								.toString();
						ApplicationEntity jointEntity = structureCommonService
								.getSectionEntity("Joint", id);
						ApplicationEntity parentEntity = this
								.getJointParent(jointEntity);
						facilityList.add(parentEntity.toMap());

					} else {
						facilityList.add(connfacilityList.get(i));
					}
				}
			}
			fiberCoreMap.put("parentFacility", facilityList);
			if(facilityList.size()>0)
			//System.out.println("纤芯parentFacility=="+gson.toJson(facilityList));
			fiberCoreList.add(fiberCoreMap);
		}
		return fiberCoreList;
	}

	/**
	 * 获取局向光纤的详细信息（包括局向光纤所属的光路纤芯，及两端的设施，有坐标的上级设施）
	 * 
	 * @param opticalRouteSP2SPEntityArray
	 * @return
	 */
	public Map<String, Object> getOpticalRouteSP2SPFacility(
			ApplicationEntity opticalRouteSP2SPEntity,String areaId) {
			Map<String, Object> opticalRouteSP2SPMap = new HashMap<String, Object>();
			
			List<Map<String, Object>> connfacilityList = new ArrayList<Map<String,Object>>();
			// 局向纤芯两端的设施
			for (int i = 0; i < OpticalRouteP2PFacilityTypes.length; i++) {
				String type = OpticalRouteP2PFacilityTypes[i];
				List<Map<String, Object>> f_list = this.structureCommonService.getStrutureSelationsApplicationMap(opticalRouteSP2SPEntity, type, AssociatedType.LINK, "networkresourcemanage");
				connfacilityList.addAll(f_list);
			}
			
			opticalRouteSP2SPMap.put("connFacility", connfacilityList);
			//System.out.println("局向纤芯两端Facility==="+gson.toJson(connfacilityList));
			
			
			List<Map<String, Object>> facilityList = new ArrayList<Map<String, Object>>();
			List<ApplicationEntity> parentEntityList=new ArrayList<ApplicationEntity>();
			if (connfacilityList != null) {
				for (int i = 0; i < connfacilityList.size(); i++) {
					//判断设施是否属于当前区域
					Map<String, Object> resource_map = connfacilityList.get(i);
					if ( resource_map.get("_entityType").equals("Terminal") ) {
						//端子
						String id = connfacilityList.get(i).get("id").toString();
						ApplicationEntity terminalEntity = structureCommonService.getSectionEntity("Terminal", id);
						
						//查询terminal->odf->room->station
						String[] typeArrs = new String[]{"ODF","Room","Station"};
						ApplicationEntity[] station_apps = this.structureCommonService.getAppArrsByRecursion(terminalEntity, typeArrs, AssociatedType.PARENT , "networkresourcemanage" );
						if ( station_apps != null && station_apps.length > 0 ) {
							Map<String, Object> station_map = station_apps[0].toMap();
							String lat = station_map.get("latitude")+"";
							String lng = station_map.get("longitude")+"";
							resource_map.put("latitude", lat);
							resource_map.put("longitude", lng);
							facilityList.add(station_map);
							parentEntityList.add(station_apps[0]);
							continue;
						}
						//查询terminal->odm->odf->room->station
						typeArrs = new String[]{"ODM","ODF","Room","Station"};
						station_apps = this.structureCommonService.getAppArrsByRecursion(terminalEntity, typeArrs, AssociatedType.PARENT , "networkresourcemanage" );
						if ( station_apps != null && station_apps.length > 0 ) {
							Map<String, Object> station_map = station_apps[0].toMap();
							String lat = station_map.get("latitude")+"";
							String lng = station_map.get("longitude")+"";
							resource_map.put("latitude", lat);
							resource_map.put("longitude", lng);
							facilityList.add(station_map);
							parentEntityList.add(station_apps[0]);
							continue;
						}
						//查询terminal->odm->->fiberCrossCabinet
						typeArrs = new String[]{"ODM","FiberCrossCabinet"};
						station_apps = this.structureCommonService.getAppArrsByRecursion(terminalEntity, typeArrs, AssociatedType.PARENT , "networkresourcemanage" );
						if ( station_apps != null && station_apps.length > 0 ) {
							Map<String, Object> station_map = station_apps[0].toMap();
							String lat = station_map.get("latitude")+"";
							String lng = station_map.get("longitude")+"";
							resource_map.put("latitude", lat);
							resource_map.put("longitude", lng);
							facilityList.add(station_map);
							parentEntityList.add(station_apps[0]);
							continue;
						}
					} else if ( resource_map.get("_entityType").equals("Joint") ) {
						
					}
				}
			}
			opticalRouteSP2SPMap.put("parentFacility", facilityList);

			opticalRouteSP2SPMap.putAll(opticalRouteSP2SPEntity.toMap());
			// 局向纤芯所属的光路纤芯  
			ApplicationEntity opticalRouteP2P = structureCommonService
			.getStrutureSelationsEntity(
					opticalRouteSP2SPEntity, "OpticalRouteP2P",
					AssociatedType.LINK, "networkresourcemanage");
			opticalRouteSP2SPMap.put("belong", opticalRouteP2P.toMap());
			
			// 光路纤芯所属的光路
			ApplicationEntity opticalRoute = structureCommonService
			.getStrutureSelationsEntity(
					opticalRouteP2P, "OpticalRoute",
					AssociatedType.LINK, "networkresourcemanage");
			opticalRouteSP2SPMap.put("grandBelong", opticalRoute.toMap());

			//System.out.println("局向纤芯parentFacility==="+gson.toJson(facilityList));

		return opticalRouteSP2SPMap;
	}

	
	/**
	 * 获取传输段的详细信息（包括传输段所属的传输系统,关联的光路纤芯,两端的设施，有坐标的上级设施）
	 * 
	 * @param transmissionSectionEntityArray
	 * @return
	 */
	public List<Map<String, Object>> getTransmissionSectionFacility(
			ApplicationEntity[] transmissionSectionEntityArray,String areaId) {
		List<Map<String, Object>> transmissionSectionList = new ArrayList<Map<String, Object>>();
		for (ApplicationEntity transmissionSectionEntity : transmissionSectionEntityArray) {
			Map<String, Object> transmissionSectionMap = transmissionSectionEntity.toMap();
			// 传输段所属的传输系统
			ApplicationEntity networkEntity = structureCommonService
					.getStrutureSelationsEntity(transmissionSectionEntity,
							"TransmissionNetwork", AssociatedType.LINK,
							"networkresourcemanage");
			transmissionSectionMap.put("belong", networkEntity
					.toMap());

			// 传输段所关联的光路纤芯
			ApplicationEntity opticalRouteP2PEntity = structureCommonService
					.getStrutureSelationsEntity(transmissionSectionEntity,
							"OpticalRouteP2P", AssociatedType.LINK,
							"networkresourcemanage");
			Map<String, Object> map2 = opticalRouteP2PEntity.toMap();
			transmissionSectionMap.put("opticalRouteP2P", map2 );
			//System.out.println("传输段所关联的光路纤芯==="+gson.toJson(opticalRouteP2PEntity.toMap()));
			// 传输段所关联的光路纤芯包含的所有局向纤芯
			List<Map<String,Object>> opticalRouteSP2SPList=new ArrayList<Map<String,Object>>();
			if(opticalRouteP2PEntity!=null){
				ApplicationEntity[] opticalRouteSP2SPArray = structureCommonService
				.getStrutureSelationsApplicationEntity(opticalRouteP2PEntity, "OpticalRouteSP2SP",
						AssociatedType.LINK, "networkresourcemanage");
				if(opticalRouteSP2SPArray!=null){
					if(opticalRouteSP2SPArray.length>0){
						for(ApplicationEntity opticalRouteSP2SP:opticalRouteSP2SPArray){
							Map<String,Object> map=this.getOpticalRouteSP2SPFacility(opticalRouteSP2SP,areaId);
							opticalRouteSP2SPList.add(map);
						}
					}
				}
			}
			transmissionSectionMap.put("opticalRouteSP2SP", opticalRouteSP2SPList);
			//TODO
			// 传输段两端的设施
			List<Map<String, Object>> connfacilityList = this.structureCommonService
					.getStrutureSelationsReturnList(transmissionSectionEntity,
							transmissionSectionFacilityTypes,
							AssociatedType.LINK, "networkresourcemanage");
			transmissionSectionMap.put("connFacility", connfacilityList);
			//System.out.println("传输段两端Facility==="+gson.toJson(connfacilityList));
			// 传输段有坐标的上级设施(站址)
			List<Map<String, Object>> facilityList = new ArrayList<Map<String, Object>>();
			if (connfacilityList != null) {
				for (int i = 0; i < connfacilityList.size(); i++) {
					if (connfacilityList.get(i).get("_entityType").equals(
							"NetworkElement")) {
						String id = connfacilityList.get(i).get("id")
								.toString();
						ApplicationEntity networkElementEntity = structureCommonService
								.getSectionEntity("NetworkElement", id);
						ApplicationEntity[] parent = structureCommonService
								.getStrutureSelationsApplicationEntity(
										networkElementEntity,
										AssociatedType.PARENT,
										"networkresourcemanage");
						if (parent.length>0) {
							facilityList.add(parent[0].toMap());
						}
					} else {
						String id = connfacilityList.get(i).get("id")
								.toString();
						ApplicationEntity transmissionPortEntity=structureCommonService
						.getSectionEntity("TransmissionPort", id);
						
						ApplicationEntity[] parentArray = structureCommonService
								.getStrutureSelationsApplicationEntity(
										transmissionPortEntity,
										AssociatedType.PARENT,
										"networkresourcemanage");
						if (parentArray!=null){
							if(parentArray.length>0){
								ApplicationEntity[] parent = structureCommonService
								.getStrutureSelationsApplicationEntity(
										parentArray[0],
										AssociatedType.PARENT,
										"networkresourcemanage");
								if (parent.length > 0)
									facilityList.add(parent[0].toMap());
							}
						}
					}
				}
			}
			transmissionSectionMap.put("parentFacility", facilityList);
			//System.out.println("传输段parentFacility==="+gson.toJson(facilityList));
			transmissionSectionList.add(transmissionSectionMap);
		}
		return transmissionSectionList;
	}

	/**
	 * 获取接头的父设施
	 * 
	 * @param jointEntity
	 * @return
	 */
	public ApplicationEntity getJointParent(ApplicationEntity jointEntity) {
		ApplicationEntity parentEntity = new ApplicationEntity();
		ApplicationEntity[] parentArray = structureCommonService
				.getStrutureSelationsApplicationEntity(jointEntity,
						AssociatedType.PARENT, "networkresourcemanage");
		if (parentArray.length>0) {
			parentEntity = parentArray[0];
		}
		return parentEntity;
	}

	/**
	 * 获取接头
	 * 
	 * @param applicationEntity
	 * @return
	 */
	public List<Map<String, Object>> getJoint(ApplicationEntity[] entityArray) {
		List<Map<String, Object>> jointList = new ArrayList<Map<String, Object>>();
		for (ApplicationEntity entity : entityArray) {
			Map<String, Object> jointMap = new HashMap<String, Object>();
			ApplicationEntity[] jointEntityArray = structureCommonService
					.getStrutureSelationsApplicationEntity(entity, "Joint",
							AssociatedType.CHILD, "networkresourcemanage");
			if (jointEntityArray.length>0) {
				for (ApplicationEntity jointEntity : jointEntityArray) {
					// jointMap.put("joint", jointEntity.toMap());
					jointMap.putAll(jointEntity.toMap());
					ApplicationEntity parent = this.getJointParent(jointEntity);
					jointMap.put("parent", parent.toMap());
					jointList.add(jointMap);
				}
			}
		}
		return jointList;
	}

	/**
	 * 获取ODF父设施
	 * 
	 * @param odfEntity
	 * @return
	 */
	public ApplicationEntity getOdfParent(ApplicationEntity odfEntity) {
		ApplicationEntity parentEntity = new ApplicationEntity();
//		System.out.println("==========getOdfParent(ApplicationEntity odfEntity)"+odfEntity.toMap());
		ApplicationEntity[] parentArray = structureCommonService
				.getStrutureSelationsApplicationEntity(odfEntity,
						AssociatedType.PARENT, "networkresourcemanage");
		if (parentArray.length>0) {
			parentEntity = parentArray[0];
		}
//		System.out.println(parentArray.length+"==========getOdfParent(ApplicationEntity odfEntity)"+parentEntity.toMap());
		return parentEntity;
	}

	/**
	 * 获取ODF
	 * 
	 * @param stationEntityArray
	 * @return
	 */
	public List<Map<String, Object>> getOdf(
			ApplicationEntity[] stationEntityArray) {
		List<Map<String, Object>> odfList = new ArrayList<Map<String, Object>>();
		for (ApplicationEntity stationEntity : stationEntityArray) {
			Map<String, Object> odfMap = new HashMap<String, Object>();
			ApplicationEntity[] odfEntityArray = structureCommonService
					.getStrutureSelationsApplicationEntity(stationEntity,
							"ODF", AssociatedType.CHILD,
							"networkresourcemanage");
			if (odfEntityArray.length>0) {
				for (ApplicationEntity odfEntity : odfEntityArray) {
					odfMap.putAll(odfEntity.toMap());
					ApplicationEntity parent = this.getOdfParent(odfEntity);
					odfMap.put("parent", parent.toMap());
					odfList.add(odfMap);
				}
			}
		}
		return odfList;

	}
	
	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}

	
}
