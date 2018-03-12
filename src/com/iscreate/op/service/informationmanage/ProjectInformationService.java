package com.iscreate.op.service.informationmanage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.pojo.informationmanage.InformationProject;

public interface ProjectInformationService extends BaseService<InformationProject> {

	
	public Map<String, Object> findSingleProjectInfo(Map<String,String> param_Map);

	/**
	 *  验证项目编号是否唯一
	 *  @param proId - 项目编号
	 *  @return true : 已存在 , false : 不存在
	 */
	public boolean checkProjectNumberExists(String projectNumber , String id);

	/**
	 *  验证项目存在
	 *  @param param_map - 参数集合
	 *  @return true : 已存在 , false : 不存在
	 */
	public boolean checkProjectExists(Map<String,String> param_map);

	/**
	 * 根据组织id,获取相应项目信息
	 * @param orgId - 组织id
	 * @return
	 */
	public abstract List<Map<String,String>> findProjectByOrgId(String orgId);

	public abstract int updateProjectInfoById(Long id, Map setMap);


}
