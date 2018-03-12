package com.iscreate.plat.networkresource.common.dao;

import java.util.List;
import java.util.Map;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;

public interface NetworkResourceManageDao {
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
	public List<BasicEntity> getResourceByResourceType(long parentId,String parentType,String resourceType);
	
	/**
	 * 根据父级资源获取其所属指定类型子级资源集合
	* @author ou.jh
	* @date Jun 21, 2013 1:55:08 PM
	* @Description: TODO 
	* @param @param parentId
	* @param @param parentType
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getChildrenResourceByResourceType(long parentId,String parentType,String resourceType);
	
//	/**
//	 * 根据指定区域获取其所属指定类型资源集合
//	* @author ou.jh
//	* @date Jun 21, 2013 2:04:20 PM
//	* @Description: TODO 
//	* @param @param areaId
//	* @param @param resourceType
//	* @param @return        
//	* @throws
//	 */
//	public List<BasicEntity> getResourceByAreaAndResourceType(long areaId,String resourceType);
	
	/**
	 * 根据指定多个区域获取其所属指定类型资源集合
	* @author ou.jh
	* @date Jun 21, 2013 2:04:20 PM
	* @Description: TODO 
	* @param @param areaIds
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getResourceByAreasAndResourceType(List<Long> areaIds,String resourceType);
	
	/**
	 * 根据子级资源获取父级资源
	* @author ou.jh
	* @date Jun 21, 2013 4:29:49 PM
	* @Description: TODO 
	* @param @param childId
	* @param @param childType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getParentResourceByChildIdAndChildType(long childId ,String childType);
	
	/**
	 * 根据资源ID与资源类型获取资源实例
	* @author ou.jh
	* @date Jun 21, 2013 4:45:21 PM
	* @Description: TODO 
	* @param @param id
	* @param @param reType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getResourceByIdAndReType(long reId , String reType);

	/**
	 * 根据资源类型获取资源实例
	* @author ou.jh
	* @date Jun 21, 2013 4:45:21 PM
	* @Description: TODO 
	* @param @param id
	* @param @param reType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getResourceByReType(String reType);
	
	/**
	 * 根据资源ID与资源类型获取指定类型的关联关系资源（大部分为逻辑网资源）
	* @author ou.jh
	* @date Jun 25, 2013 10:55:01 AM
	* @Description: TODO 
	* @param @param reId 资源ID
	* @param @param reType 资源类型
	* @param @param selectType 关联关系资源类型
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getLINKResourceByReIdAndReTypeAndSelectType(long reId,String reType,String selectType);
	
	/**
	 * 根据子级资源获取父级资源
	* @author ou.jh
	* @date Jun 21, 2013 4:29:49 PM
	* @Description: TODO 
	* @param @param childId
	* @param @param childType
	* @param @param ParentType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getParentResourceByChildIdAndChildTypeAndParentType(long childId ,String childType,String parentType);
	
	
	/**
	 * 根据指定区域获取其所属指定类型子级资源集合
	* @author ou.jh
	* @date Jun 21, 2013 2:04:20 PM
	* @Description: TODO 
	* @param @param areaId
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getChildrenResourceByAreaAndResourceType(long areaId,String resourceType);
	
	/**
	 * 根据父级资源集合获取其所属指定类型资源集合
	* @author ou.jh
	* @date Jun 21, 2013 1:55:08 PM
	* @Description: TODO 
	* @param @param parentIds
	* @param @param parentType
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getResourceByResourceType(List<Long> parentIds,String parentType,String resourceType);
	
	/**
	 * 根据父级资源获取其所属指定类型子级资源集合
	* @author ou.jh
	* @date Jun 21, 2013 1:55:08 PM
	* @Description: TODO 
	* @param @param parentId
	* @param @param parentType
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getChildrenResourceByResourceType(List<Long> parentIds,String parentType,String resourceType);
	
	/**
	 * 基站统计监控
	* @author ou.jh
	* @date Jul 4, 2013 5:31:07 PM
	* @Description: TODO 
	* @param @param areaIds
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getGPSBaseStationReportByResourceType(List<Long> areaIds);
	
	/**
	 * 全资源统计
	* @author ou.jh
	* @date Jul 4, 2013 5:31:07 PM
	* @Description: TODO 
	* @param @param areaIds
	* @param @return        
	* @throws
	 */
	public List<BasicEntity> getGPSReportReportByResourceType(List<Long> areaIds);
	
//	/**
//	 * 根据子级区域获取父级区域
//	* @author ou.jh
//	* @date Jun 21, 2013 4:29:49 PM
//	* @Description: TODO 
//	* @param @param childId
//	* @param @param childType
//	* @param @return        
//	* @throws
//	 */
//	public List<BasicEntity> getParentAreaByChildIdAndChildType(long childId ,String childType);
}
