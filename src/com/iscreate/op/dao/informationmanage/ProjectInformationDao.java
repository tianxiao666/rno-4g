package com.iscreate.op.dao.informationmanage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.InformationConstant;
import com.iscreate.op.pojo.informationmanage.InformationProject;

public interface ProjectInformationDao extends BaseDao<InformationProject> {

	/************ 属性 **************/
	
	/**
	 * 验证项目号是否已经存在
	 * @param proId 项目号
	 * @param id 主键(null)
	 * @return 是否存在 (true:存在)
	 */
	public boolean checkProjectNumberExists(String projectNumber, String id);

	
	
	
	/**
	 * 根据组织id,获取相应项目信息
	 * @param orgId - 组织id
	 * @return 项目信息
	 */
	public List<Map<String,String>> findProjectByOrgId ( String orgId );



	/**
	 * 根据项目id获取项目信息
	 * @param projectId - 项目id
	 * @return
	 */
	public abstract Map<String,String> findProjectByProjectId(String projectId);



	/**
	 * 根据服务商企业名,查询项目信息
	 * @param serverEnterPriseName - 服务商企业名
	 * @return 
	 */
	public abstract List<Map<String, String>> findProjectByServerEnterPriseName(String serverEnterPriseName);
	
	/**
	 * 根据组织id 字符串,获取相应项目信息
	 * @author yuan.yw
	 * @param orgIds - 组织id 字符串
	 * @return 项目信息
	 */
	public List<Map<String,String>> findProjectListByOrgIds ( String orgIds );
	
}
