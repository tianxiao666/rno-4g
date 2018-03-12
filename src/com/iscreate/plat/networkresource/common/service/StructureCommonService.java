package com.iscreate.plat.networkresource.common.service;

import java.util.List;
import java.util.Map;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;

public interface StructureCommonService {



	/**
	 * 根据不同对象查询其不同关系资源实例数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure Structure实例名称
	 * @return Map<String, ApplicationEntity[]>
	 */
	public abstract Map<String, ApplicationEntity[]> getStrutureSelationsMap(
			ApplicationEntity entity, AssociatedType associatedType,
			String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源实例数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure Structure实例名称
	 * @return Map<String, List<Map<String, Object>>>
	 */
	public abstract Map<String, List<Map<String, Object>>> getStrutureSelationsReturnMap(
			ApplicationEntity entity, AssociatedType associatedType,
			String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源实例数组的集合
	 * * @param resourcesTypes 资源的类型数组
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure Structure实例名称
	 * @return Map<String, List<Map<String, Object>>>
	 */
	public abstract Map<String, List<Map<String, Object>>> getStrutureSelationsReturnMap(
			ApplicationEntity entity, String[] resourcesTypes,
			AssociatedType associatedType, String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源实例数组的集合
	 * * @param resourcesTypes 资源的类型数组
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>> 
	 */
	public abstract List<Map<String, Object>> getStrutureSelationsReturnList(
			ApplicationEntity entity, String[] resourcesTypes,
			AssociatedType associatedType, String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源实例集合
	 * @param resourcesType 资源的类型 
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure  Structure实例名称
	 * @return Map<String, List<Map<String, Object>>>
	 */
	public abstract Map<String, List<Map<String, Object>>> getStrutureSelationsApplicationMapList(
			ApplicationEntity entity, String resourcesType,
			AssociatedType associatedType, String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源实例数组
	 * @param resourcesType 资源的类型 
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure  Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public abstract List<Map<String, Object>> getStrutureSelationsApplicationMap(
			ApplicationEntity entity, String resourcesType,
			AssociatedType associatedType, String myStructure);

	/**
	 * 根据不同对象查询其不同关系指定类型资源实例数组
	 * @param resourcesType 资源的类型 
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure  Structure实例名称
	 * @return ApplicationEntity[];
	 */
	public abstract ApplicationEntity[] getStrutureSelationsApplicationEntity(
			ApplicationEntity entity, String resourcesType,
			AssociatedType associatedType, String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源
	 * @param resourcesType 资源的类型 
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure  Structure实例名称
	 * @return ApplicationEntity[];
	 */
	public abstract ApplicationEntity getStrutureSelationsEntity(
			ApplicationEntity entity, String resourcesType,
			AssociatedType associatedType, String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure  Structure实例名称
	 * @return ApplicationEntity[];
	 */
	public abstract ApplicationEntity[] getStrutureSelationsApplicationEntity(
			ApplicationEntity entity, AssociatedType associatedType,
			String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源类型数组
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure  Structure实例名称
	 * @return String[];
	 */
	public abstract String[] getStrutureSelationsArray(
			ApplicationEntity entity, AssociatedType associatedType,
			String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源类型数组(指定视图)
	 * @param entity需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param viewName 视图名称
	 * @param myStructure Structure实例名称
	 * @return
	 */
	public abstract String[] getStrutureSelationsArray(
			ApplicationEntity entity, AssociatedType associatedType,
			String viewName, String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源(类型,总数量)数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param addLink 控制是否需要同时获取child和link类型的关联类型变量标识
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public abstract List<Map<String, Object>> getStrutureSelationsTypeMap(
			ApplicationEntity entity, AssociatedType associatedType,
			String addLink, String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源(类型,总数量)数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param addLink 控制是否需要同时获取child和link类型的关联类型变量标识
	 * @viewName 视图名称
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public abstract List<Map<String, Object>> getStrutureSelationsTypeMap(
			ApplicationEntity entity, AssociatedType associatedType,
			String addLink, String viewName, String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源(类型,总数量)数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param addLink 控制是否需要同时获取child和link类型的关联类型变量标识
	 * @viewName 视图名称
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public abstract List<Map<String, Object>> getStrutureSelationsTypeMap(
			ApplicationEntity entity, String[] searchTypes,
			AssociatedType associatedType, String addLink, String viewName,
			String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源(类型,总数量)数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public abstract List<Map<String, Object>> getStrutureSelationsTypeMap(
			ApplicationEntity entity, AssociatedType associatedType,
			String myStructure);

	/**
	 * 建立两个对象之间的关系
	 * @param leftAe 关联资源对象
	 * @param rightAe 被关联资源对象
	 * @param associatedType 需要创建的关系类型
	 * @param myStructure Structure实例名称
	 * @return int 1为成功 0为失败
	 */
	public abstract int createAssociatedRelation(ApplicationEntity leftAe,
			ApplicationEntity rightAe, AssociatedType associatedType,
			String myStructure);

	/**
	 * 根据ID与类型查询对象属性
	 * @param entityType 需要查询的资源类型
	 * @param id 需要查询的资源ID
	 * @return ApplicationEntity
	 */
	public abstract ApplicationEntity getSectionEntity(String entityType,
			String id);

	/**
	 * 删除两个对象间的关系
	 * @param leftAe 左资源
	 * @param rightAe 右资源
	 * @param associatedType 需要删除的关系类型
	 * @return int 1为成功 0为失败
	 */
	public abstract int delStrutureAssociation(ApplicationEntity leftAe,
			ApplicationEntity rightAe, AssociatedType associatedType,
			String myStructure);

	/**
	 * 删除对象的关系(递归删除)
	 * @param appEntity 需要删除关系的资源对象
	 * @param myStructure  Structure实例名称
	 * @return int 1为成功 0为失败
	 */
	public abstract int delEntityByRecursion(ApplicationEntity appEntity,
			String myStructure);

	/**
	 * 删除对象的关系(递归删除)
	 * @param appEntity 需要删除关系的资源对象
	 * @param myStructure Structure实例名称
	 * @return int 1为成功 0为失败
	 */
	public abstract int delEntityByRecursionOnly(ApplicationEntity appEntity,
			String myStructure);

	/**
	 * 根据不同类型返回不同关联类型数组
	 * @param infoType 输入类型
	 * @param associatedType 关联关系 
	 * @param myStructure
	 * @return
	 */
	public abstract String[] getAssociatedAetName(String infoType,
			AssociatedType associatedType, String myStructure);

	/**
	 * 保存对象信息
	 * @param entity 需要保存的资源对象
	 * @return int 1为成功 0为失败
	 */
	public abstract int saveInfoEntity(ApplicationEntity entity,
			String myStructure);

	/**
	 * 更新对象信息
	 * @param entity 需要更新的资源对象
	 * @return int 1为成功 0为失败
	 */
	public abstract int updateInfoEntity(ApplicationEntity entity);

	/**
	 * 删除对象信息
	 * @param infoName 需要更新的资源对象的类型
	 * @param id 需要删除的资源ID
	 * @return int 1为成功 0为失败
	 */

	public abstract int deleteFiberCoreEntity(String infoName, String id);

	/**
	 * 递归获取子级entity(同一关系)
	 * @param app 根节点entity
	 * @param typeArrs 类型数组
	 * @param associatedType 关系
	 * @param myStructure structure名字
	 * @return
	 */
	public abstract ApplicationEntity[] getAppArrsByRecursion(
			ApplicationEntity app, String[] typeArrs,
			AssociatedType associatedType, String myStructure);

	/**
	 * 递归获取子级entity(同一关系)
	 * @param app 根节点entity
	 * @param typeArrs 类型数组
	 * @param associatedTypes 关系数组
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public abstract ApplicationEntity[] getAppArrsByRecursion(
			ApplicationEntity app, String[] typeArrs,
			AssociatedType[] associatedTypeArrs, String myStructure);

	/**
	 * 递归获取子级entity(同一关系)
	 * @param appArrs entity数组
	 * @param typeArrs 类型数组
	 * @param associatedType 关系
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public abstract ApplicationEntity[] getAppArrsByRecursion(
			ApplicationEntity[] appArrs, String[] typeArrs,
			AssociatedType associatedType, String myStructure);

	/**
	 * 递归获取子级entity(不同关系)
	 * @param appArrs entity数组
	 * @param typeArrs 类型数组
	 * @param associatedTypes 关系数组
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public abstract ApplicationEntity[] getAppArrsByRecursion(
			ApplicationEntity[] appArrs, String[] typeArrs,
			AssociatedType[] associatedTypeArrs, String myStructure);

	/**
	 * 递归获取指定entity下特定类型的子应用数据对象
	 * @param app 根节点entity
	 * @param type 需要制定的子类型
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public abstract ApplicationEntity[] getAppArrsByRecursionForSrc(
			ApplicationEntity app, String type, String myStructure);

	/**
	 * 获取structure下的所有entity的类型
	 * @param myStructure structure名字
	 * @return List<String> 
	 */
	public abstract List<String> getAllEntityTypes(String myStructure);

	/**
	 * AE set值时进行类型转换
	 * @param entity
	 * @param type
	 * @param key
	 * @param value
	 */
	public abstract void addValueTo(ApplicationEntity entity, String type,
			String key, String value);

	/**
	 * 根据不同的类型获取不同对象查询
	 * @param modelString
	 * @param id
	 * @return
	 */
	public abstract List<Map<String, Object>> getResVenifyTaskEntityList(
			String modelString, String id);

	/**
	 * 根据不同的类型获取不同对象查询
	 * @param modelString
	 * @param id
	 * @return
	 */
	public abstract List<Map<String, Object>> getResVenifyTaskEntityListByStatus(
			String modelString, String task_status);

	/**
	 * 递归获取子级entity，并进行关系绑定
	 * @param taskId 任务id
	 * @param user 操作人 
	 * @param aeArrs ae数组
	 * @param associatedType 关联类型
	 * @param myStructure structure名字
	 */
	public abstract void bindTaskAndEntityAssByRecursion(long taskId,
			String user, ApplicationEntity[] aeArrs,
			AssociatedType associatedType, String myStructure);

	/**
	 * 递归获取子级entity，并进行关系绑定
	 * @param taskId 任务id
	 * @param user 操作人
	 * @param ae ae实例
	 * @param associatedType 关联类型
	 * @param myStructure structure名字
	 */
	public abstract void bindTaskAndEntityAssByRecursion(long taskId,
			String user, ApplicationEntity ae, AssociatedType associatedType,
			String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源(类型,总数量)数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param addLink 控制是否需要同时获取child和link类型的关联类型变量标识
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public abstract List<Map<String, Object>> getStrutureSelationsTypeMapToMobile(
			ApplicationEntity entity, AssociatedType associatedType,
			String addLink, String myStructure);

	/**
	 * 根据不同对象查询其不同关系资源(类型,总数量)数组的集合
	 * @param entity 需要查询对象
	 * @param associatedType 需要查询的关系类型
	 * @param myStructure Structure实例名称
	 * @return List<Map<String, Object>>
	 */
	public abstract List<Map<String, Object>> getStrutureSelationsTypeMapToMobile(
			ApplicationEntity entity, AssociatedType associatedType,
			String myStructure);

	/**
	 * 根据entityGroupName递归获取子级entity(不同关系)
	 * @param app 根节点entity
	 * @param entityGroupName entityGroup名字
	 * @param associatedType 关系数组
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public abstract ApplicationEntity[] getAppArrsByEntityGroup(
			ApplicationEntity app, String entityGroupName,
			AssociatedType[] associatedTypeArrs, String myStructure);

	/**
	 * 对象检索,不指定类型或指定类型
	 * @param entityType 资源类型
	 * @param map 需要的查询条件
	 * @param myStructure structure名字
	 * @return List<List<ApplicationEntity>> 
	 */
	public abstract List<List<ApplicationEntity>> getEntityListByEntity(
			String entityType, Map<String, Object> map, String myStructure);

	/**
	 * 对象检索,指定EntityGroup
	 * @param AetgName EntityGroup名字
	 * @param map 需要的查询条件
	 * @param myStructure structure名字
	 * @return List<List<ApplicationEntity>>
	 */
	public abstract List<List<ApplicationEntity>> getEntityListByAetg(
			String AetgName, Map<String, Object> map, String myStructure);

	/**
	 * 对象检索,不指定是否是EntityGroup或Entity类型
	 * @param AetgName EntityGroup名字
	 * @param map 需要的查询条件
	 * @param myStructure structure名字
	 * @return List<List<ApplicationEntity>>
	 */
	public abstract List<List<ApplicationEntity>> getEntityListByEntityTypeRoAetg(
			String AetgName, Map<String, Object> map, String myStructure);

	/**
	 * 根据坐标，指定半径查询指定查询条件指定资源类型数据
	 * @param AetgName Entity类型
	 * @param distance 半径
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param map 需要的查询条件
	 * @param myStructure structure名字
	 * @return List<ApplicationEntity>
	 */
	public abstract List<ApplicationEntity> getEntityListByGPSDistance(
			String AetgName, double distance, double longitude,
			double latitude, Map<String, Object> map, String myStructure);

	/**
	 * 根据坐标，指定半径查询指定查询条件不指定资源或指定资源组类型数据
	 * @param AetgName 资源组类型(可以不指定)
	 * @param distance 半径
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param map 需要的查询条件
	 * @param myStructure structure名字
	 * @return List<ApplicationEntity>
	 */
	public abstract List<ApplicationEntity> getEntityListByAetgAndGPSDistance(
			String AetgName, double distance, double longitude,
			double latitude, Map<String, Object> map, String myStructure);

	public abstract boolean isChildTypeOfEntity(ApplicationEntity entity,
			String type, String myStructure);

	public abstract boolean isLinkTypeOfEntity(ApplicationEntity entity,
			String type, String myStructure);

	/**
	 * 增加一个任务和多个资源的绑定(绑定任务和资源的同时，记录多个资源之间的关系)
	 * @param entity
	 * @return
	 */
	public abstract int addTaskAeRelAss(ApplicationEntity entity);

	/**
	 * 根据类型和label，查询一个物理资源
	 * @param type
	 * @param label
	 * @return
	 */
	public abstract BasicEntity getPhysicalresByLabel(String type, String label);

	/**
	 * 对象检索,不指定是否是EntityGroup或Entity类型(GPS)
	 * @param AetgName EntityGroup名字
	 * @param map 需要的查询条件
	 * @param mapByGPS
	 * @param myStructure structure名字
	 * @return List<List<ApplicationEntity>>
	 */
	public abstract List<List<ApplicationEntity>> getEntityListGPSByEntityTypeRoAetg(
			String AetgName, Map<String, Object> map,
			Map<String, Object> mapByGP, String myStructure);

	/**
	 * 对象检索,不指定是否是EntityGroup或Entity类型(GPS圆环)
	 * @param AetgName EntityGroup名字
	 * @param map 需要的查询条件
	 * @param mapByGPS
	 * @param areaIdsList
	 * @param myStructure structure名字
	 * @return List<List<ApplicationEntity>>
	 */
	public abstract List<List<ApplicationEntity>> getEntityListGPSRingByEntityTypeRoAetg(
			String AetgName, Map<String, Object> map,
			Map<String, Object> mapByGP,List<String> areaIdsList, String myStructure);

	/**
	 * 根据坐标，指定半径查询指定查询条件不指定资源或指定资源组类型数据(分页)
	 * @param AetgName 资源组类型(可以不指定)
	 * @param distance 半径
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param map 需要的查询条件
	 * @param start 开始下标
	 * @param end 结束下标
	 * @param myStructure structure名字
	 * @return List<ApplicationEntity>
	 */
	public abstract List<ApplicationEntity> getEntityListByAetgAndGPSDistanceAndPaging(
			String AetgName, double distance, double longitude,
			double latitude, Map<String, Object> map, int start, int end,
			String myStructure);

	/**
	 * 根据坐标，指定半径查询指定查询条件不指定资源或指定资源组类型数据(圆环)
	 * @param AetgName 资源组类型(可以不指定)
	 * @param innerDistance 内半径
	 * @param outerDistance 外半径
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param map 需要的查询条件
	 * @param areaIdsList
	 * @param myStructure structure名字
	 * @return List<ApplicationEntity>
	 */
	public abstract List<ApplicationEntity> getEntityListRingByAetgAndGPSDistance(
			String AetgName, double innerDistance, double outerDistance,
			double longitude, double latitude, Map<String, Object> map,List<String> areaIdsList,
			String myStructure);

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
	 * @param areaIdsList
	 * @param myStructure structure名字
	 * @return List<Map<String,Object>>
	 */
	public abstract List<Map<String,Object>> getEntityListRingByAetgAndGPSDistanceAndPaging(
			String AetgName, double innerDistance, double outerDistance,
			double longitude, double latitude, Map<String, Object> map,
			int start, int end, List<String> areaIdsList,String myStructure);

	/**
	 * 对象检索,不指定是否是EntityGroup或Entity类型(精确)
	 * @param AetgName EntityGroup名字
	 * @param map 需要的查询条件
	 * @param myStructure structure名字
	 * @return List<List<ApplicationEntity>>
	 */
	public List<List<ApplicationEntity>> getEntityListByEntityTypeRoAetgAccurate(String AetgName,Map<String,Object> map, String myStructure);
	
	
	/**
	 * 获取主键
	 * @param EntityType entity类型
	 * @return long
	 */
	public long getEntityPrimaryKey(String EntityType);
	
	/**
	 * 获取资源分组数组
	 * @param entityGroupName
	 * @param myStructure
	 * @return
	 */
	public String[] getAetNameOfAetg(String entityGroupName, String myStructure);


	/**
	 * 递归获取指定entity下特定类型的子应用数据对象
	 * @param app 根节点entity
	 * @param type 需要制定的子类型
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public List<ApplicationEntity>  getAppArrsByRecursionForSrcByAtg(ApplicationEntity app, String type, String myStructure);
	
	
	/**
	 * 根据区域ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<BasicEntity> getBaseStationByAreas(List<String> areaId,String resourceType);
	
	
	/**
	 * 根据区域ID获取站址
	 * @param areaId
	 * @return
	 */
	public List<BasicEntity> getStationByAreas(List<String> areaId,String resourceType);
	

	
	
	/**
	 * 根据区域ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<BasicEntity> getBaseStationByArea(String areaId,String resourceType);
	
	/**
	 * 根据区域ID获取站址
	 * @param areaId
	 * @return
	 */
	public List<BasicEntity> getStationByArea(String areaId,String resourceType);
	
	
	/**
	 * 根据区域ID与定义小区条件查询小区
	 * @param areaId 区域ID
	 * @param columnName 查询列名
	 * @param columnValue 查询条件
	 * @param myStructure 
	 * @return ApplicationEntity
	 */
	public ApplicationEntity getCellOfChildAreaByAreaId(String areaId,String columnName,String columnValue,String myStructure);
	
	
	
	//获取是否位于区域
	public boolean getLocatedArea(String startAreaId , String EndAreaId );
	
	
	/**
	 * 根据区域ID获取站址
	 * @param areaId
	 * @return
	 */
	public BasicEntity getAreaByStation(String resourceId,String resourceType);
	
	/**
	 * 根据基站ID获取站址
	 * @param areaId
	 * @return
	 */
	public List<BasicEntity> getStationByBaseStation(String baseStationId,String baseStationType,String resourceType);
	/**
	 * 
	 * @description: 利用sql语句更新entity数据
	 * @author：
	 * @return     
	 * @return int     
	 * @date：Sep 26, 2012 5:18:34 PM
	 */
	public int updateApplicationEntityBySql(ApplicationEntity app,ApplicationModule module);
	
	
	/**
	 * 递归获取指定entity下特定类型的子应用数据对象(相同类型)
	 * @param app 根节点entity
	 * @param type 需要制定的子类型
	 * @param myStructure structure名字
	 * @return ApplicationEntity[]
	 */
	public ApplicationEntity[] getAppArrsByRecursionForSrcSameType(ApplicationEntity app, String type, String myStructure);
	
	/**
	 * 根据站址ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<BasicEntity> getBaseStationByStation(String stationId,String resourceType);
	
	public int getStationCountByArea(List<String> areaIdList);
	
	public int getBaseStationCountByArea(List<String> areaIdList);
	
	/**
	 * 根据区域ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<Map<String, Object>> getBaseStationByAreasByHibernate(List<String> areaId,String resourceType);
	
	public List<Map<String, Object>> getBaseStationByStationByHibernate(String stationId,String resourceType);
	
	public int getCellCountByArea(List<String> areaIdList);
	/**
	 * 根据站址ID获取基站
	 * @param areaId
	 * @return
	 */
	public Map<String, Object> getBaseStationByName(String name);
	
	/**
	 * 根据站址ID获取基站
	 * @param areaId
	 * @return
	 */
	public Map<String, Object> getCellByName(String name);
	
	/**
	 * 根据区域ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<Map<String, Object>> getRoomByAreasByHibernate(List<String> areaId,String resourceType);
	
	/**
	 * 根据区域ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<Map<String, Object>> getResByAreasByHibernate(List<String> areaId,String resourceType,Map<String, String> cmap);
	
	/**
	 * 根据区域ID获取基站
	 * @param areaId
	 * @return
	 */
	public List<Map<String, Object>> getResByAreasHibernate(List<String> areaId,String resourceType,Map<String, String> cmap);
	
	/**
	 * 根据父级资源获取其所属指定类型资源集合
	* @author ou.jh
	* @date Jun 21, 2013 1:55:08 PM
	* @Description: TODO 
	* @param @param parentId
	* @param @param parentType
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<Map<String,Object>> getResourceByResourceType(long parentId,String parentType,String resourceType);
	
	
	/**
	 * 根据指定区域获取其所属指定类型资源集合 
	* @author ou.jh
	* @date Jun 21, 2013 2:06:29 PM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public List<Map<String,Object>> getResourceByAreaAndResourceType(long areaId,String resourceType);
	
	/**
	 * 
	 * @description: 根据坐标，资源类型，条件，账户，分页获取资源记录相关信息（按距当前位置距离排序）（适用 站址 机房 基站）
	 * @author：yuan.yw
	 * @param AetName 资源类型
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param map 条件map
	 * @param start 开始记录
	 * @param end  结束记录
	 * @param account 账户
	 * @return     
	 * @return Map<String,Object>    
	 * @date：Jun 25, 2013 12:08:38 PM
	 */
	public abstract Map<String,Object> getResourceWithPagingByAetNameAndGPSDistanceAndAccount(
			String AetName, double longitude, double latitude, Map<String, Object> map,
			int start, int end, String account);
}