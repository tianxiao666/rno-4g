package com.iscreate.op.dao.informationmanage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.DBUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.InformationConstant;
import com.iscreate.op.pojo.informationmanage.InformationProject;

public class ProjectInformationDaoImpl extends BaseDaoImpl<InformationProject> implements ProjectInformationDao {
	
	/************ 依赖注入 **************/
	
	/************ 属性 **************/
	private static final String project_table = " ( SELECT p.* , id as proId FROM " + DBUtil.getTableName(InformationProject.class) + " p ) project";
	
	
	/**
	 * 验证项目号是否已经存在
	 * @param proId 项目号
	 * @param id 主键(null)
	 * @return 是否存在 (true:存在)
	 */
	public boolean checkProjectNumberExists( String projectNumber , String id ){
		boolean exists = false;
		String sql = "SELECT " + InformationConstant.PROJECT_COLUMNTEXT + " FROM " + DBUtil.getTableName(InformationProject.class) + " project WHERE id != " + id + " AND projectNumber = '" + projectNumber + "' ";
		List<Map<String,String>> list = executeFindList(sql);
		exists = list != null && list.size() > 0;
		return exists;
	}
	
	
	/**
	 * 根据组织id,获取相应项目信息
	 * @param orgId - 组织id
	 * @return 项目信息
	 */
	public List<Map<String,String>> findProjectByOrgId ( String orgId ) {
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if ( StringUtil.isNullOrEmpty(orgId) ) {
			return list;
		}
		String selectString = InformationConstant.PROJECT_COLUMNTEXT + " , CASE WHEN clientOrgId = 73 THEN 'client' ELSE 'server' END AS orgType ";
		String whereString = " WHERE clientOrgId = {orgId} OR serverOrgId = {orgId} ";
		whereString = whereString.replace("{orgId}", orgId);
		String sql = 	"	SELECT " + 
								selectString + 
						"	FROM " +
						"	" + project_table + whereString ;
		list = super.executeFindList(sql);
		return list;
	}
	
	
	/**
	 * 根据项目id获取项目信息
	 * @param projectId - 项目id
	 * @return
	 */
	public Map<String,String> findProjectByProjectId ( String projectId ) {
		String sql = " SELECT " + InformationConstant.PROJECT_COLUMNTEXT + " FROM " + project_table + " WHERE proId = " + projectId;
		List<Map<String, String>> list = super.executeFindList(sql);
		Map<String,String> result_map = null;
		if ( list != null && list.size() > 0 ) {
			result_map = list.get(0);
		}
		return result_map;
	}
	
	/**
	 * 根据服务商企业名,查询项目信息
	 * @param serverEnterPriseName - 服务商企业名
	 * @return 
	 */
	public List<Map<String, String>> findProjectByServerEnterPriseName ( String serverEnterPriseName ) {
		String sql = " SELECT " + InformationConstant.PROJECT_COLUMNTEXT + " FROM " + project_table + " WHERE serverEnterpriseName = '" + serverEnterPriseName + "'";
		List<Map<String, String>> list = super.executeFindList(sql);
		return list;
	}
	
	/**
	 * 根据组织id 字符串,获取相应项目信息
	 * @author yuan.yw
	 * @param orgIds - 组织id 字符串
	 * @return 项目信息
	 * @date 2013-5-27
	 */
	public List<Map<String,String>> findProjectListByOrgIds ( String orgIds ){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if ( StringUtil.isNullOrEmpty(orgIds) ) {
			return list;
		}
		String selectString = InformationConstant.PROJECT_COLUMNTEXT + " , CASE WHEN clientOrgId = 73 THEN 'client' ELSE 'server' END AS orgType ";
		String whereString = " WHERE clientOrgId in {orgId} OR serverOrgId in {orgId} ";
		whereString = whereString.replace("{orgId}", "("+orgIds+")");
		String sql = 	"	SELECT " + 
								selectString + 
						"	FROM " +
						"	" + project_table + whereString ;
		list = super.executeFindList(sql);
		return list;
	}
	
	/*************** getter setter ****************/
	
}
