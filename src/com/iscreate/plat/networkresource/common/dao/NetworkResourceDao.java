package com.iscreate.plat.networkresource.common.dao;

import java.util.List;
import java.util.Map;

public interface NetworkResourceDao {
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
	* @date Jun 21, 2013 2:04:20 PM
	* @Description: TODO 
	* @param @param areaId
	* @param @param resourceType
	* @param @return        
	* @throws
	 */
	public List<Map<String,Object>> getResourceByAreaAndResourceType(long areaId,String resourceType);
}
