package com.iscreate.op.dao.informationmanage;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.informationmanage.Area;
import com.iscreate.op.pojo.informationmanage.InformationProjectResource;
import com.iscreate.op.service.informationmanage.InformationManageNetworkResourceService;

public interface InformationManageNetworkResourceDao extends BaseDao<InformationProjectResource> {
	
	
	/**
	 * 根据项目id、组织id、资源类型,获取项目资源信息
	 * @param projectId - 项目id
	 * @param orgId - 组织Id
	 * @param resourceType - 资源类型
	 * @return
	 */
	public List<Map<String, String>> findProjectResourceByProjectOrgIdResourceType ( String projectId , String orgId , String resourceType );

	/**
	 * 根据项目id,获取项目下的所有组织id
	 * @param projectId - 项目id
	 */
	public abstract List<Map<String,String>> findCarryOutOrgIdByProjectId(String projectId);

	/**
	 * 根据区域id,获取区域下的所有项目id
	 * @param areaId - 区域id
	 */
	public abstract List<Map<String,String>> findProjectIdByAreaId(String areaId);

	
	/**
	 * 根据区域、组织、项目批量删除授权数据
	 * @param areaIds - 区域集合
	 * @param orgIds - 组织集合
	 * @param projectIds - 项目集合
	 * @return 是否操作成功
	 */
	public abstract boolean deleteProjectResourceByParamWithBetch(List<Long> areaIds,
			List<Long> orgIds, List<Long> projectIds);

	/**
	 * 根据组织Id获取组织区域的关系
	 * @param orgId
	 * @return
	 */
	public abstract List<Map<String,String>> findProjectResourceByOrgId(String orgId);

	/**
	 * 根据区域Id和组织类型和资源类型
	 * @param areaId
	 * @param resourceType
	 * @return
	 */
	public abstract List<Map<String,String>> findProjectResourceByAreaIdAndResourceType(
			String areaId, String resourceType);

	/**
	 * 根据组织Id获取组织区域关系
	 * @param orgId
	 * @param resourceType
	 * @return
	 */
	public abstract List<Map<String,String>> findProjectResourceByOrgIdAndResourceType(
			String orgId, String resourceType);

	/**
	 * 根据区域Id获取组织区域的关系
	 * @param orgId
	 * @return
	 */
	public abstract List<Map<String,String>> findProjectResourceByAreaId(String areaId);

	/**
	 * 根据项目Id , 获取该项目的 (区域 、 资源)授权信息 - 根据区域 、 资源剔除重复项
	 * @param projectId - 项目id
	 * @return
	 */
	public abstract List<Map<String,String>> findAreaIdAndResourceTypeByProjectIdWithoutSame(String projectId);

	
	/**
	 * 查询区域下 , 项目对组织的资源分配
	 * @param areaIds - 区域id集合
	 * @param projectId - 项目id
	 * @param orgId - 组织id
	 * @return
	 */
	public abstract List<Map<String,String>> findProjectResourceByProjectAndOrgAndAreaListGroupResourceType(
			List<String> areaIds, String projectId, String orgId);

	/**
	 * 根据资源类型、组织id、授权状态为条件 , 获取有该权限的区域Id
	 * @param orgIds - 组织id集合
	 * @param resourceType - 资源类型
	 * @param status - 授权状态(0,1)
	 * 					0 ： 没授权
	 * 					1 ： 已授权
	 * @return 区域id
	 */
	public abstract List<String> findAreaIdByOrgIdResourceTypeWithStatus(
			List<String> orgId, String resourceType, int status);

	/**
	 * 根据资源类型、区域Id、授权状态为条件 , 获取有该权限的组织id
	 * @param areaIds - 区域Id集合
	 * @param resourceType - 资源类型
	 * @param status - 授权状态(0,1)
	 * 					0 ： 没授权
	 * 					1 ： 已授权
	 * @return 组织id
	 */
	public abstract List<Map<String,String>> findOrgIdByAreaIdResourceTypeWithStatus(
			List<String> areaId, String resourceType, int status);

	
	/**
	 * 根据参数,获取项目资源授权信息
	 * @param areaId - 区域Id
	 * @param orgId - 组织Id
	 * @param projectId - 项目Id
	 * @param resourceType - 资源类型
	 * @return
	 */
	public abstract List<Map<String,String>> findProjectResourceByParam(String areaId, String orgId,
			String projectId, String resourceType);

	
	/**
	 * 根据组织项目id,获取项目资源授权信息(不带status条件)
	 * @param orgId - 组织ID
	 * @param projectId - 项目ID
	 * @return
	 */
	public abstract List<Map<String,Object>> findResourceByOrgProjectAreaIdWithOutStatus(
			String orgId, String projectId , String areaId);  
	
	
	
	
	
	
}
