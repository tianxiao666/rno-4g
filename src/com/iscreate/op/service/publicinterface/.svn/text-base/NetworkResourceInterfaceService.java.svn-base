package com.iscreate.op.service.publicinterface;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iscreate.op.pojo.system.SysOrg;

public interface NetworkResourceInterfaceService {

	/**
	 * 根据组织Id和资源类型获取基站信息
	 * @param orgId
	 * @param resourceType
	 * @return
	 */
	public List<Map<String,Object>> getNetResourceByOrgIdAndResourceType(long orgId,String resourceType);
	
	/**
	 * 根据区域Id和类型获取基站
	 * @param areaId
	 * @param reType
	 * @return
	 */
	public List<Map<String,String>> getBaseStationByAreaIdAndReType(String areaId,String reType);
	
	/**
	 * 根据区域Id和类型获取资源
	 * @param areaId
	 * @param reType
	 * @return
	 */
	public List<Map<String,String>> getResourceByAreaIdAndReType(String areaId,String reType);
	
	/**
	 * 根据基站Id和和资源类型获取组织架构
	 * @param baseStationId
	 * @param orgType
	 * @param resourceType
	 * @return
	 */
	public List<SysOrg> getOrgByBaseStationIdAndOrgTypeAndResourceType(long baseStationId,String orgType,String resourceType);
	
	/**
	 * 根据组织Id和资源类型获取其父区域
	 * @param orgId
	 * @param orgType
	 * @param resourceType
	 * @return
	 */
	public Map<String,String> getParentAreaByOrgIdAndReType(long orgId,String orgType,String resourceType);
	
	/**
	 * 根据组织Id和资源类型获取其子区域
	 * @param orgId
	 * @param orgType
	 * @param resourceType
	 * @return
	 */
	public List<Map<String,String>> getChildrenAreaByOrgIdAndReType(long orgId,String orgType,String resourceType);
	
	/**
	 * 根据基站Id和基站类型获取站址
	 */
	public Map<String,String> getStationByBaseStationIdAndBaseStationType(String baseStationId,String stationType,String selectReType);
	
	/**
	 * 根据站址Id获取区域
	 * @param stationId
	 * @return
	 */
	public Map<String,String> getAreaByStationId(String stationId,String stationType);
	
	/**
	 * 根据区域Id获取父级区域
	 * @param areaId
	 * @param areaType
	 * @return
	 */
	public Map<String,String> getParentAreaByAreaId(String areaId,String areaType);
	
	/**
	 * 递归获取父区域（包含传入的Id）
	 * @param areaId
	 * @param areaType
	 * @return
	 */
	public List<Map<String,String>> getRecursionParentAreaWithSelfByAreaIdService(String areaId,String areaType);
	
	/**
	 * 递归获取父区域（不包含传入的Id）
	 * @return
	 */
	public List<Map<String,String>> getRecursionParentAreaByAreaIdService(String areaId,String areaType);
	
	/**
	 * 获取资源
	 * @param reId
	 * @param reType
	 * @param selectReType
	 * @param associatedType
	 * @return
	 */
	public List<Map<String,String>> getResourceService(String reId,String reType,String selectReType,String associatedType);
	
	/**
	 * 根据资源Id和资源类型获取基础设施的信息
	 * @param resourceId
	 * @param resourceType
	 * @return Map<String,Map<String,String>>
	 */
	public Map<String,Map<String,String>> getBaseFacilityToMapService(String resourceId,String resourceType);
	
	/**
	 * 根据资源Id和资源类型获取资源
	 * @param resourceId
	 * @param resourceType
	 * @return
	 */
	public Map<String,String> getResourceByReIdAndReTypeService(String resourceId,String resourceType);
	
	/**
	 * 获取基站列表
	 * @param orgId
	 * @param stationType
	 * @return
	 */
	public List<Map<String,String>> getListBaseStationService(long orgId,String stationType);
	
	/**
	 * 根据拼音获取基站列表
	 * @param orgId
	 * @param stationType
	 * @param pinyin
	 * @return
	 */
	public List<Map<String,String>> getListPinyinBaseStationService(long orgId,String stationType,String pinyin);
	
	/**
	 * 根据模糊查找基站列表
	 * @param orgId
	 * @param stationType
	 * @param fuzzy
	 * @return
	 */
	public List<Map<String,String>> getListFuzzyBaseStationService(long orgId,String stationType,String fuzzy);
	
	/**
	 * 根据资源Id和资源类型获取基础设施的信息
	 * @param resourceId
	 * @param resourceType
	 */
	public void getBaseFacilityService(String resourceId,String resourceType);
	
	/**
	 * 根据工单Id，资源Id，资源类型获取资源维护信息
	 * @param woId
	 * @param reId
	 * @param reType
	 * @return
	 */
	public List<Map<String,String>> getResourceMaintainRecordByWoIdService(String woId,String reId,String reType);
	
	/**
	 * 根据资源Id，资源类型获取资源维护信息
	 * @param reId
	 * @param reType
	 * @return
	 */
	public List<Map<String,String>> getResourceMaintainRecordService(String reId,String reType);
	
	/**
	 * 根据资源Id，资源类型获取处理过的资源维护信息（Map里的isBelong标记了是否属于该工单的资源）
	 * @param woId
	 * @param reId
	 * @param reType
	 * @return
	 */
	public List<Map<String,String>> getHandleResourceMaintainRecordService(String woId,String reId,String reType);
	
	/**
	 * 获取资源维护记录
	 */
	public void getResourceMaintainRecordAjaxService(String woId,String reId,String reType);
	
	/**
	 * 根据基站Id和基站类型获取站址
	 */
	public void getStationByBaseStationIdAndBaseStationTypeService(String baseStationId,String stationType);
	
	/**
	 * 根据站址Id和类型获取所有的父级的区域
	 * @param stationId
	 * @param stationType
	 */
	public void getAllAreaByBaseStationIdService(String stationId,String stationType);
	
	/**
	 * 根据站址Id和类型获取所有的父级的区域
	 * @param stationId
	 * @param stationType
	 * @return
	 */
	public List<Map<String,String>> getAllAreaByStationIdAndStationTypeService(String stationId,String stationType);
	
	/**
	 * 根据工单ID获取基础设置信息
	 * @param woId
	 */
	public void getBaseFacilityByWoIdService(String woId);
	
	/**
	 * 根据人员获取其所属的区域信息
	 * @param account
	 * @return
	 */
	public List<Map<String,String>> getAreaByAccountService(String account);
	
	/**
	 * 根据组织Id获取站址
	 * @param orgId
	 * @return
	 */
	public List<Map<String,String>> getStationByOrgIdService(long orgId);
	
	/**
	 * 根据站址Id和基站类型获取基站信息
	 * @param stationId
	 * @param stationType
	 * @return
	 */
	public List<Map<String,String>> getStationByStationIdAndBaseStationTypeService(String stationId,String baseStationType);
	
	/**
	 * 根据区域Id获取站址数量
	 * @param areaIds
	 * @return
	 */
	public String getStationCountByAreaService(String areaIds);
	
	/**
	 * 根据区域Id获取基站数量
	 * @param areaIds
	 * @return
	 */
	public String getBaseStationCountByAreaService(String areaIds);
	
	/**
	 * 根据坐标，指定半径查询指定查询条件不指定资源或指定资源组类型数据(圆环)分页
	 * @param AetgName 资源组类型(可以不指定)
	 * @param innerDistance 内半径
	 * @param outerDistance 外半径
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param map 需要的查询条件
	 * @param start 开始下标
	 * @param end 结束下标
	 * @param myStructure structure名字
	 * @return List<Map<String,String>>
	 */
	public List<Map<String, String>> getEntityListRingByAetgAndGPSDistanceAndPagingService(String AetgName,double innerDistance,double outerDistance,double longitude, double latitude,Map<String,Object> map,int start,int end, String myStructure);
	
	/**
	 * 获取顶级区域
	 * @return
	 */
	public List<Map<String,String>> getTopAreaService();
	
	/**
	 * 根据区域Id和资源类型获取资源（报表）
	 * @param areaId
	 * @param reTypes
	 * @return
	 */
	public Map<String,String> getReportResourceListService(String areaId,String reTypes);
	
	/**
	 * 根据资源名和资源类型获取资源信息
	 * @param baseStationName
	 * @return
	 */
	public Map<String,String> getResourceByReNameAndReTypeService(String reName,String reType);
	
	/**
	 * 根据条件获取其向下的资源(包括本身资源)
	 * @param reId(当前资源Id)
	 * @param reType(当前资源类型)
	 * @param selectReType(需要获取的资源)
	 * @return
	 */
	public List<Map<String,String>> getDownwardOnSelfResourceService(String reId,String reType,String selectReType);
	
	/**
	 * 根据条件获取其向下的资源(不包括本身资源)
	 * @param reId(当前资源Id)
	 * @param reType(当前资源类型)
	 * @param selectReType(需要获取的资源)
	 * @return
	 */
	public List<Map<String,String>> getDownwardResourceService(String reId,String reType,String selectReType);
	
	/**
	 * 根据专业获取设备
	 * @param profession
	 * @return
	 */
	public Map<String,List<Map<String,String>>> getEquipmentByProfessionAndReIdAndReTypeService(String profession,String reId,String reType);
	
	/**
	 * 根据组织Id获取巡检机房(不是直接挂到一个维护队)
	 * @param orgId
	 * @return
	 */
	public List<Map<String,String>> getRoutineInspectionRoomByOrgIdService(long orgId);
	
	/**
	 * 根据组织Id获取巡检机房(直接挂到一个维护队)
	 * @param orgId
	 * @return
	 */
	public List<Map<String,Object>> getRoutineInspectionRoomByOrgIdToMaintenanceTeamService(long orgId);
	
	/**
	 * 根据区域获取机房
	 * @param areaIds
	 * @return
	 */
	public List<Map<String,String>> getRoomByAreaIdsService(String areaIds);
	
	/**
	 * 获取外部链接
	 */
	public void getNetworkResourceUrlService();
	
	/**
	 * 根据环境代码获取对应的网络资源外部链接
	 * @return
	 */
	public String getNetworkResourceUrl();
	
	
	//树==============================================================================
	/**
	 * ajax获取区域树（从顶级开始查找）
	 */
	public void getTopAreaTreeAjaxService();
	
	
	/**
	 * 获取用户物理区域
	 * @param account 用户帐号
	 * @return
	 */
	public Map getUserAreaService(String account);
	
	/**
	 * 根据区域集合获取的基站集合
	* @author ou.jh
	* @date Jul 4, 2013 4:00:38 PM
	* @Description: TODO 
	* @param @param areaIds
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getBaseStationByAreas(List<String> areaIds,String resourceType);
	
}
