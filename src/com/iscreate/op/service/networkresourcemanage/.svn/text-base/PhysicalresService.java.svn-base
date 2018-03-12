package com.iscreate.op.service.networkresourcemanage;

import java.util.List;
import java.util.Map;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;

public interface PhysicalresService {

	/**
	 * 根据类型和id，查询一个物理资源
	 * @param type
	 * @param id
	 * @return
	 */
	public abstract BasicEntity getPhysicalresById(String type, long id);
	/**
	 * 
	 * @description: 根据类型和label，查询一个物理资源
	 * @author：
	 * @param type
	 * @param label
	 * @return     
	 * @return BasicEntity     
	 * @date：2012-5-28 下午03:24:33
	 */
	public abstract BasicEntity getPhysicalresByLabel(String type, String label);
	/**
	 * 更新一个物理资源
	 * @param appEntity
	 * @param type
	 */
	public int updatePhysicalres(ApplicationEntity appEntity, String type);
	
	/**
	 * 添加一个物理资源
	 * @param appEntity
	 * @param type
	 */
	public int addPhysicalres(ApplicationEntity appEntity);
	
	/**
	 * 根据特定条件，查询物理资源
	 * @param type
	 * @param id
	 * @param key
	 * @param condition
	 * @return
	 */
	public List<BasicEntity> getPhysicalresByCondition(String type, String attr, Object condition, String searchCondition);
	
	/**
	 * 根据不同条件查询资源
	 * @return
	 */
	public List<BasicEntity> searchResourceByCondition(ApplicationEntity searchedAe);
	
	/**
	 * 根据不同条件查询资源个数(分页后的个数)
	 * @return
	 */
	public int searchResourceCountByConditionAndPage(ApplicationEntity searchedAe, int pageIndex, int pageSize);
	
	/**
	 * 根据不同条件查询资源个数
	 * @return
	 */
	public int searchResourceCountByCondition(ApplicationEntity searchedAe);
	
	/**
	 * 根据不同条件查询资源(分页)
	 * @return
	 */
	public List<BasicEntity> searchResourceByConditionAndPage(ApplicationEntity searchedAe, int pageIndex, int pageSize);
	/**
	 * 
	 * @description: 取得某个资源面板布局
	 * @author：
	 * @return     
	 * @return List<BasicEntity>     
	 * @date：Jul 23, 2012 4:09:26 PM
	 */
	public List<BasicEntity> getResourceEntityList(String type,String resourceId,String resourceType);
	/**
	 * 根据不同条件查询资源(属性为日期类型做特殊处理 （dateMap）)
	 * @return
	 */
	public List<BasicEntity> searchResourceByCondition(ApplicationEntity searchedAe,Map<String,String> dateMap);
	/**
	 * 根据子资源递归查询资源所属区域
	 * @return
	 */
	public ApplicationEntity searchParentAreaResourceForSrc(ApplicationEntity searchedAe,String areaName,String linkType);
	/**
	 * 
	 * @description: 获得未绑关系的资源
	 * @author：
	 * @param type
	 * @return     
	 * @return List<BasicEntity>     
	 * @date：Sep 13, 2012 6:25:23 PM
	 */
	public List<BasicEntity> getNoAssociateResource(String type);
	/**
	 * 根据子资源递归查询父资源List
	 * @return
	 */
	public List<Map<String,Object>> searchParentResourceForSrc(
			ApplicationEntity searchedAe,String linkType); 
	/**
	 *取得某个资源类型是否有上级资源类型（GeneralBaseStation基站五大类，区域，站址，机房）中一个
	 * @return
	 */
	public String getDirectestParentResourceType(
			String type); 
	/**
	 * 
	 * @description: 某个资源的某个类型的上级资源信息;
	 * @author：     
	 * @return void     
	 * @date：Nov 22, 2012 9:16:40 AM
	 */
	public Map<String,Object>  getDirectestParentResourceInfo(ApplicationEntity searchedAe,String entityType,String linkType);
	/**
	 * 更新一个表记录
	 *
	 * 
	 */
	public int updatePhysicalres(ApplicationEntity appEntity, String type,String attr);
	/**
	 * 
	 * @description: 获取id最大值
	 * @author：
	 * @param type
	 * @return     
	 * @return int     
	 * @date：Dec 11, 2012 9:52:33 AM
	 */
	public long getMaxIdFromTable(String type);
	/**
	 * 
	 * @description: 判断是否是未绑关系的资源
	 * @author：
	 * @param type entityId
	 * @return     
	 * @return List<BasicEntity>     
	 * @date：Sep 13, 2012 6:25:23 PM
	 */
	public List<BasicEntity> getNoAssociateResource(String type,String entityId);
	/**
	 * 
	 * @description: 获取相关资源某种父资源的figurenode Id
	 * @author：
	 * @param ids
	 * @param map
	 * @param parentType
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jan 21, 2013 9:08:23 AM
	 */
	public Map<String,Object> getSomeParentTypeNodesId(String ids,Map<String,Object> map,String parentType);
	/**
	 * 
	 * @description:获取相关资源某种父资源的entity Map
	 * @author：
	 * @param ids
	 * @param parentType
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jan 21, 2013 10:14:44 AM
	 */
	public Map<String,Object> getSomeParentTypeApplicationEntitysMapByIds(String ids,String currentType,String parentType);
	
	/**
	 * 
	 * @description:获取相关资源父资源的id Map
	 * @author：
	 * @param ids
	 * @param parentType
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jan 21, 2013 10:14:44 AM
	 */
	public Map<String,Object> getSomeParentTypeIdsMapByIds(String ids,String currentType,String parentType);
	/**
	 * 
	 * @description: 拼装sql 批量入库数据
	 * @author：
	 * @param entityType
	 * @param sql
	 * @return     
	 * @return int     
	 * @date：Jan 25, 2013 12:10:07 PM
	 */
	public int insertEntitysToDataTable(String entityType,String sql);
	/**
	 * 
	 * @description: 取得父子Map
	 * @author：
	 * @param mp
	 * @param parentType
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jan 25, 2013 1:41:14 PM
	 */
	public List<Map<String,Object>> getNodeIdsAndParentNodeIdsMap(Map<String,Object> mp,String parentType);
	/**
	 * 
	 * @description: sql语句更新entity String
	 * @author：
	 * @return     
	 * @return int     
	 * @date：Sep 26, 2012 5:18:34 PM
	 */
	public String updateApplicationSql(ApplicationEntity app,ApplicationModule module);
	/**
	 * 
	 * @description: 获取多个实例entity Map
	 * @author：
	 * @param updateIdMap
	 * @param currentType
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jan 30, 2013 2:45:14 PM
	 */
	public Map<String,Object> getApplicationEntityMap(Map<String,Object> updateIdMap,String currentType);
	/**
	 * 
	 * @description: 获取多个实例id对应figurenode Map
	 * @author：
	 * @param updateIdMap
	 * @param currentType
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jan 30, 2013 2:45:14 PM
	 */
	public Map<String,Object> getFigureNodeIdMap(Map<String,Object> updateIdMap,String currentType);
	/**
	 * 
	 * @description: 获取多个figurenode ids 对应父级name和type信息
	 * @author：
	 * @param updateIdMap
	 * @param currentType
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jan 30, 2013 2:45:14 PM
	 */
	public Map<String,Object> getFigureNodeIdParInfoMap(Map<String,Object> fnIdMap,String assType);
	/**
	 * 
	 * @description: 获取搜索条件Sql String
	 * @author：
	 * @param searchedAe
	 * @param dateMap
	 * @return     
	 * @return String     
	 * @date：Feb 20, 2013 7:12:09 PM
	 */
	public String getSearchSqlString(ApplicationEntity searchedAe, Map<String,String> dateMap);
	
	/**
	 * 
	 * @description: 获取资源实例 figurenode Id
	 * @author：
	 * @param id
	 * @param type
	 * @return     
	 * @return String     
	 * @date：Feb 20, 2013 7:12:09 PM
	 */
	public String getFigureNodeIdById(String id,String type);
	/**
	 * 
	 * @description: 据sql分页查询资源
	 * @author：
	 * @param type
	 * @param sql
	 * @param pageSize
	 * @param pageIndex
	 * @param orderType
	 * @return     
	 * @return List<Map<String,Object>>    
	 * @date：Feb 21, 2013 9:43:57 AM
	 */
	public List<Map<String,Object>> searchResourceBySql(String type,String sql,String countSql,String pageSize,String pageIndex,String orderType);
	/**
	 * 
	 * @description: 根据id及类型获取父资源
	 * @author：yuan.yw
	 * @param childId
	 * @param childType
	 * @return     
	 * @return List<BasicEntity>     
	 * @date：Jul 3, 2013 10:51:27 AM
	 */
	public List<BasicEntity> getParentResourceByChildIdAndChildType(long childId, String childType);
	/**
	 * 
	 * @description: 根据父级资源集合获取其所属指定类型资源集合
	 * @author：yuan.yw
	 * @param parentIds
	 * @param parentType
	 * @param resourceType
	 * @return     
	 * @return List<BasicEntity>     
	 * @date：Jul 3, 2013 10:52:41 AM
	 */
	public List<BasicEntity> getResourceByResourceType(List<Long> parentIds,String parentType,String resourceType);
	
}