package com.iscreate.op.service.informationmanage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.dao.informationmanage.InformationManageNetworkResourceDao;
import com.iscreate.op.pojo.informationmanage.InformationProjectResource;

public interface InformationManageNetworkResourceService extends BaseService<InformationProjectResource> {

	/************* service *************/
	public void txSaveProjectResource(Map<String,String> requestParamMap);

	/************* service *************/
	public Map<String,Map<String,Object>> checkProjectResourceIsexists(Map<String,String> requestParamMap);

	public void txSaveProjectResourceStatus(Map<String,String> requestParamMap);
	
	/**
	 * 根据项目id、组织id、资源类型,获取项目资源信息
	 * @param orgId - 组织Id
	 * @param resourceType - 资源类型
	 * @return
	 */
	public List<Map<String, String>> findAreaIdByProjectOrgIdResType ( String orgId , String resourceType );

	/**
	 * 根据区域id,获取区域下的所有项目id
	 * @param areaId - 区域id
	 * @return 
	 */
	public abstract List<String> findProjectIdByAreaId(String areaId);

	/**
	 * 根据项目id,获取项目下的所有组织id
	 * @param projectId - 组织id
	 * @return 
	 */
	public abstract List<String> findOrgIdByProjectId(String projectId);

	/**
	 * 根据区域id,获取区域下的所有组织id
	 * @param areaId - 区域id
	 * @return
	 */
	public abstract List<String> findOrgIdByAreaId(String areaId);

	/**
	 * 根据项目id、组织id、资源类型,获取项目资源信息
	 * @param projectId - 项目id
	 * @param orgId - 组织Id
	 * @param resourceType - 资源类型
	 * @return
	 */
	public abstract List<Map<String, String>> findAreaIdByProjectOrgIdResType(String projectId,
			String orgId, String resourceType);

	
	/**
	 * 根据实施组织id , 获取项目实例
	 * @param orgId - 实施组织id
	 * @return
	 */
	public abstract List<Map<String,String>> findProjectByCarryOutOrgId(String orgId);

	public abstract Map<String,Map<String,String>> checkProjectResourceStateIsexists(Map<String,String> requestParamMap);

	
	/**
	 * 根据资源类型、组织id、授权状态为条件 , 获取有该权限的区域Id
	 * @param orgId - 组织id
	 * @param resourceType - 资源类型
	 * @param status - 授权状态(0,1)
	 * 					0 ： 没授权
	 * 					1 ： 已授权
	 * @return 区域id
	 */
	public abstract List<String> findAreaIdsByOrgIdResourceTypeWithStatus(
			List<String> orgIds, String resourceType, int status);

	public abstract List<Map<String,String>> findOrgIdsByAreaIdResourceTypeWithStatus(
			List<String> areaIds, String resourceType, int status);
	
	
	
	
}
