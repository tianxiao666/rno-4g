package com.iscreate.op.dao.informationmanage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.CardispatchConstant;
import com.iscreate.op.constant.InformationConstant;
import com.iscreate.op.pojo.informationmanage.InformationProjectResource;

public class InformationManageNetworkResourceDaoImpl extends BaseDaoImpl<InformationProjectResource> implements InformationManageNetworkResourceDao {

	private static final String project_resource_Table = " ( SELECT pr.* , id as \"proResId\" FROM " + DBUtil.getTableName(InformationProjectResource.class) + " pr ) proRes " ;
	
	/**
	 * 根据项目id、组织id、资源类型,获取项目资源信息
	 * @param projectId - 项目id
	 * @param orgId - 组织Id
	 * @param resourceType - 资源类型
	 * @return
	 */
	public List<Map<String, String>> findProjectResourceByProjectOrgIdResourceType ( String projectId , String orgId , String resourceType ) {
		String whereString = " WHERE 1=1 AND projectId IS NOT NULL AND STATUS = 1 ";
		if ( !StringUtil.isNullOrEmpty(projectId) ) {
			whereString += " AND projectId = " + projectId ;
		}
		if ( !StringUtil.isNullOrEmpty(orgId) ) {
			whereString += " AND orgId = " + orgId ;
		}
		if ( !StringUtil.isNullOrEmpty(resourceType) ) {
			whereString += " AND resourceType = '" + resourceType + "'" ;
		}
		String sql = 	" 	SELECT " +
						"		" + InformationConstant.PROJECT_RESOURCE_COLUMNTEXT + " " +
						"	FROM " + 
						"		" + project_resource_Table + 
						"	" + whereString  ;
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	/**
	 * 根据区域id,获取区域下的所有项目id(group by projectId)
	 * @param areaId - 区域id
	 */
	public List<Map<String,String>> findProjectIdByAreaId ( String areaId ) {
		String whereString = " WHERE 1=1 AND areaId = " + areaId ;
		String sql = 	" 	SELECT " +
						"		areaId \"areaId\" , projectId \"projectId\" " +
						"	FROM " + 
						"		" + project_resource_Table + 
						"	" + whereString + 
						" 	GROUP BY projectId , areaId " ;
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	/**
	 * 根据项目id,获取项目的所有实施组织id
	 * @param projectId - 项目id
	 */
	public List<Map<String,String>> findCarryOutOrgIdByProjectId ( String projectId ) {
		String whereString = " WHERE 1=1 AND projectId = " + projectId ;
		String sql = 	" 	SELECT " +
						"		orgId \"orgId\" , projectId \"projectId\"  " +
						"	FROM " + 
						"		" + project_resource_Table + 
						"	" + whereString + 
						" 	GROUP BY orgId , projectId " ;
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	/**
	 * 根据区域、组织、项目批量删除授权数据
	 * @param areaIds - 区域集合
	 * @param orgIds - 组织集合
	 * @param projectIds - 项目集合
	 * @return 是否操作成功
	 */
	public boolean deleteProjectResourceByParamWithBetch ( List<Long> areaIds , List<Long> orgIds , List<Long> projectIds ) {
		String whereString = " WHERE 1=1 ";
		String areaIdString = DBUtil.list2InString(areaIds, "areaId");
		String orgIdString = DBUtil.list2InString(orgIds, "orgId");
		String projectIdString = DBUtil.list2InString(projectIds, "projectId");
		whereString += areaIdString + orgIdString + projectIdString ;
		String sql = " DELETE FROM " + DBUtil.getTableName(InformationProjectResource.class) + " " + whereString;
		Boolean flag = super.executeSql(sql);
		return flag;
	}
	
	
	/**
	 * 根据组织Id获取组织区域关系
	 * @param orgId
	 * @param resourceType
	 * @return
	 */
	public List<Map<String,String>> findProjectResourceByOrgIdAndResourceType (String orgId , String resourceType) {
		String sql = " SELECT " + InformationConstant.PROJECT_RESOURCE_COLUMNTEXT + " FROM " + project_resource_Table + " WHERE resourceType IS NOT NULL AND orgId = " + orgId + " AND resourceType = '" + resourceType + "' ";
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	/**
	 * 根据区域Id和组织类型和资源类型
	 * @param areaId
	 * @param resourceType
	 * @return
	 */
	public List<Map<String,String>> findProjectResourceByAreaIdAndResourceType(String areaId,String resourceType){
		String sql = " SELECT  " + InformationConstant.PROJECT_RESOURCE_COLUMNTEXT + "  FROM " + project_resource_Table + " WHERE resourceType IS NOT NULL AND areaId = " + areaId + " AND resourceType = '" + resourceType + "' ";
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
		
	}
	
	
	/**
	 * 根据组织Id获取组织区域的关系
	 * @param orgId
	 * @return
	 */
	public List<Map<String,String>> findProjectResourceByOrgId(String orgId){
		String sql = " SELECT  " + InformationConstant.PROJECT_RESOURCE_COLUMNTEXT + "  FROM " + project_resource_Table + " WHERE STATUS = 1 AND resourceType IS NOT NULL AND orgId = " + orgId;
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	/**
	 * 根据区域Id获取组织区域的关系
	 * @param orgId
	 * @return
	 */
	public List<Map<String,String>> findProjectResourceByAreaId(String areaId){
		String sql = " SELECT  " + InformationConstant.PROJECT_RESOURCE_COLUMNTEXT + "  FROM " + project_resource_Table + " WHERE resourceType IS NOT NULL AND areaId = " + areaId;
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	
	/**
	 * 根据项目Id , 获取该项目的 (区域 、 资源)授权信息 - 根据区域 、 资源剔除重复项
	 * @param projectId - 项目id
	 * @return
	 */
	public List<Map<String,String>> findAreaIdAndResourceTypeByProjectIdWithoutSame ( String projectId ) {
		String whereString = " WHERE 1=1 AND projectId = " + projectId ;
		String sql = 	" 	SELECT " +
						"		areaId \"areaId\"  , resourceType \"resourceType\" " +
						"	FROM " + 
						"		" + project_resource_Table + 
						"	" + whereString + 
						"	GROUP BY areaId , resourceType";
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	/**
	 * 查询区域下 , 项目对组织的资源分配
	 * @param areaIds - 区域id集合
	 * @param projectId - 项目id
	 * @param orgId - 组织id
	 * @return
	 */
	public List<Map<String,String>> findProjectResourceByProjectAndOrgAndAreaListGroupResourceType ( List<String> areaIds , String projectId , String orgId ) {
		String whereString = " WHERE 1=1 ";
		String areaIdString = DBUtil.list2InString(areaIds, "areaId");
		String projectString = "";
		if ( StringUtil.isNullOrEmpty(projectId) ) {
			projectString = " AND projectId = " + projectId ;
		}
		String orgString = "";
		if ( StringUtil.isNullOrEmpty(orgId) ) {
			orgString = " AND orgId = " + orgId ;
		}
		whereString += areaIdString + projectString + orgString ;
		String sql = " SELECT resourceType \"resourceType\" , projectId \"projectId\"  FROM " + DBUtil.getTableName(InformationProjectResource.class) + whereString + " GROUP BY resourceType , projectId ";
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	/**
	 * 根据资源类型、组织id、授权状态为条件 , 获取有该权限的区域Id
	 * @param orgIds - 组织id集合
	 * @param resourceType - 资源类型
	 * @param status - 授权状态(0,1)
	 * 					0 ： 没授权
	 * 					1 ： 已授权
	 * @return 区域id
	 */
	public List<String> findAreaIdByOrgIdResourceTypeWithStatus ( List<String> orgIds , String resourceType , int status ) {
		String whereString = " WHERE 1=1 ";
		if ( orgIds != null && orgIds.size() > 0 ) {
			whereString += DBUtil.list2InLong(orgIds, "orgId") ;
		}
		if ( !StringUtil.isNullOrEmpty(resourceType) ) {
			whereString += " AND resourceType = '" + resourceType + "' " ;
		}
		if ( status == 1 || status == 0 ) {
			whereString += " AND status = " + status ;
		}
		String sql = 	"	SELECT distinct" +
						"		areaId \"areaId\" " +
						"	FROM " +
						"		" + DBUtil.getTableName(InformationProjectResource.class) + " " + whereString + 
						" 	GROUP BY areaId , orgId , resourceType ";
		
		List<Map<String, String>> list = super.executeFindList(sql);
		List<String> areaIds = new ArrayList<String>();
		if ( list != null && list.size() > 0 ) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				String areaId = map.get("areaId");
				areaIds.add(areaId);
			}
		}
		return areaIds;
	}
	
	
	/**
	 * 根据资源类型、区域Id、授权状态为条件 , 获取有该权限的组织id
	 * @param areaIds - 区域Id集合
	 * @param resourceType - 资源类型
	 * @param status - 授权状态(0,1)
	 * 					0 ： 没授权
	 * 					1 ： 已授权
	 * @return 组织id
	 */
	public List<Map<String,String>> findOrgIdByAreaIdResourceTypeWithStatus ( List<String> areaIds , String resourceType , int status ) {
		String whereString = " WHERE 1=1 ";
		if ( areaIds != null && areaIds.size() > 0 ) {
			whereString += DBUtil.list2InLong(areaIds, "areaId") ;
		}
		if ( !StringUtil.isNullOrEmpty(resourceType) ) {
			whereString += " AND resourceType = '" + resourceType + "' " ;
		}
		if ( status == 1 || status == 0 ) {
			whereString += " AND status = " + status ;
		}
		String sql = 	"	SELECT " +
						"		orgId \"orgId\" , areaId \"areaId\" " +
						"	FROM " +
						"		" + DBUtil.getTableName(InformationProjectResource.class) + " " + whereString + 
						" 	GROUP BY areaId , orgId , resourceType ";
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	
	
	/**
	 * 根据参数,获取项目资源授权信息
	 * @param areaId - 区域Id
	 * @param orgId - 组织Id
	 * @param projectId - 项目Id
	 * @param resourceType - 资源类型
	 * @return
	 */
	public List<Map<String,String>> findProjectResourceByParam ( String areaId , String orgId , String projectId , String resourceType ) {
		String whereString = " WHERE 1=1 ";
		if ( !StringUtil.isNullOrEmpty(areaId) ) {
			whereString += " AND areaId = " + areaId;
		}
		if ( !StringUtil.isNullOrEmpty(orgId) ) {
			whereString += " AND orgId = " + orgId;
		}
		if ( !StringUtil.isNullOrEmpty(projectId) ) {
			whereString += " AND projectId = " + projectId;
		}
		if ( !StringUtil.isNullOrEmpty(resourceType) ) {
			whereString += " AND resourceType = '" + resourceType + "'" ;
		}
		String sql = " SELECT " + CardispatchConstant.PRO_RESOURCE_COLUMNTEXT + " FROM " + DBUtil.getTableName(InformationProjectResource.class) + " res " + whereString;
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	/**
	 * 根据组织项目id,获取项目资源授权信息(不带status条件)
	 * @param orgId - 组织ID
	 * @param projectId - 项目ID
	 * @return
	 */
	public List<Map<String,Object>> findResourceByOrgProjectAreaIdWithOutStatus ( String orgId , String projectId , String areaId ) {
		String whereString = " WHERE 1=1 ";
		if ( !StringUtil.isNullOrEmpty(orgId) ) {
			whereString += " AND orgId = " + orgId;
		}
		if ( !StringUtil.isNullOrEmpty(projectId) ) {
			whereString += " AND projectId = " + projectId;
		}
		if ( !StringUtil.isNullOrEmpty(areaId) ) {
			whereString += " AND areaId = " + areaId;
		}
		String sql = " SELECT " + CardispatchConstant.PRO_RESOURCE_COLUMNTEXT + " FROM " + DBUtil.getTableName(InformationProjectResource.class) + " res " + whereString;
		List<Map<String, Object>> list = super.executeFindListMapObject(sql);
		return list;
	}
	
	
	
}
