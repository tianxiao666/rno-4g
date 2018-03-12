package com.iscreate.op.service.informationmanage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.iscreate.op.action.informationmanage.common.ArrayUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.dao.informationmanage.InformationManageNetworkResourceDao;
import com.iscreate.op.dao.informationmanage.ProjectInformationDao;
import com.iscreate.op.pojo.informationmanage.InformationProject;
import com.iscreate.op.pojo.informationmanage.InformationProjectResource;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.system.SysOrganizationService;

@SuppressWarnings({"finally","unused"})
public class InformationManageNetworkResourceServiceImpl extends BaseServiceImpl<InformationProjectResource> implements InformationManageNetworkResourceService {
	
	/************* 依赖注入 *************/
	private InformationManageNetworkResourceDao informationManageNetworkResourceDao;
	private ProjectInformationDao projectInformationDao;
	private InformationNetworkService informationNetworkService; 
	/************* 属性 *************/
	private Log log = LogFactory.getLog(this.getClass());
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	/************* service *************/
	
	/**
	 * 根据区域、组织,获取项目的所有授权资源类型
	 */
	public Map<String,Map<String,Object>> checkProjectResourceIsexists ( Map<String,String> requestParamMap ) {
		String orgId = requestParamMap.get("orgId");
		String projectId = requestParamMap.get("projectId");
		String areaId = requestParamMap.get("areaId");
  		List<Map<String,Object>> list = this.informationManageNetworkResourceDao.findResourceByOrgProjectAreaIdWithOutStatus(orgId , projectId , areaId );
		
		Map<String,Map<String,Object>> list_mapList = new LinkedHashMap<String,Map<String,Object>>();
		for (int i = 0; list != null && i < list.size(); i++) {
			Map<String,Object> oar = list.get(i);
			String keyString = oar.get("resourceType")+"";
			if ( StringUtil.isNullOrEmpty(keyString) ) {
				continue;
			}
			try {
				Map<String, Object> formatObject = formatObject(oar);
				list_mapList.put(keyString,formatObject);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return list_mapList;
	}
	
	
	
	/**
	 * 根据区域、组织,获取项目的所有授权资源类型(包括上级区域)
	 */
	public Map<String,Map<String,String>> checkProjectResourceStateIsexists ( Map<String,String> requestParamMap ) {
		String areaId = requestParamMap.get("areaId");
		String orgId = requestParamMap.get("orgId");
		String projectId = requestParamMap.get("projectId");
		//递归获取上级区域
		List<Map<String, String>> parent_areas = this.informationNetworkService.getRecursionParentAreaWithSelfByAreaIdService( areaId , "Sys_Area" );
		//根据区域获取资源授权信息
		List<String> areaIds = new ArrayList<String>();
		if ( parent_areas != null ) {
			for (int i = 0; i < parent_areas.size(); i++) {
				Map<String, String> area_map = parent_areas.get(i);
				areaIds.add(area_map.get("id"));
			}
		} else {
			areaIds.add(areaId);
		}
		Map<String,Map<String,String>> list_mapList = new LinkedHashMap<String,Map<String,String>>();
		List<Map<String, String>> list = informationManageNetworkResourceDao.findProjectResourceByProjectAndOrgAndAreaListGroupResourceType ( areaIds , projectId , orgId ) ;
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> res_map = list.get(i);
			String keyString = res_map.get("resourceType");
			res_map.put("areaId", areaId);
			res_map.put("orgId", orgId);
			List<Map<String, String>> state_list = this.informationManageNetworkResourceDao.findProjectResourceByParam(areaId, orgId, projectId, keyString);
			//获取state值
			if ( state_list != null && state_list.size() > 0 ) {
				Map<String,String> ipr = state_list.get(0);
				if ( !StringUtil.isNullOrEmpty(ipr.get("status")) ) {
					Integer status = Integer.valueOf(ipr.get("status"));
					res_map.put("status", status+"");
				}
			} else {
				res_map.put("status", "0");
			}
			try {
				list_mapList.put(keyString,res_map);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return list_mapList;
	}
	
	
	
	
	public void txSaveProjectResourceStatus ( Map<String,String> requestParamMap ) {
		String nocheckedres = requestParamMap.get("nocheckedres");
		String checkedres = requestParamMap.get("checkedres");
		String[] chres = checkedres.split(",");
		String[] nores = nocheckedres.split(",");
		
		requestParamMap.remove("nocheckedres");
		requestParamMap.remove("checkedres");
		
		String orgId = requestParamMap.get("orgId");
		Long areaId = Long.valueOf(requestParamMap.get("areaId"));
		Long projectId = Long.valueOf(requestParamMap.get("projectId"));
		//List<ProviderOrganization> org_list = this.providerOrganizationService.getOrgListDownwardByOrg( Long.valueOf(orgId) );
		//yuan.yw
		List<SysOrg> org_list = this.sysOrganizationService.getOrgListDownwardByOrg( Long.valueOf(orgId) );
		List<Long> org_id_list = new ArrayList<Long>();
		for (int i = 0; i < org_list.size(); i++) {
			SysOrg pro = org_list.get(i);
			if ( pro != null ) {
				org_id_list.add(pro.getOrgId());
			}
		}
		
		//把该区域、组织里的相关项目授权数据清除
		List<Long> areaIdList = new ArrayList<Long>();
		areaIdList.add(areaId);
		List<Long> projectIdList = new ArrayList<Long>();
		projectIdList.add(projectId);
		this.informationManageNetworkResourceDao.deleteProjectResourceByParamWithBetch(areaIdList, org_id_list, projectIdList);
		
		//状态1
		for (int i = 0; chres != null && i < chres.length; i++) {
			if ( StringUtil.isNullOrEmpty(chres[i]) ) continue;
			for (int j = 0; j < org_id_list.size(); j++) {
				Long sub_orgId = org_id_list.get(j);
				try {
					InformationProjectResource oa = new InformationProjectResource();
					oa.setAreaId(Long.valueOf(requestParamMap.get("areaId")));
					oa.setOrgId(sub_orgId);
					oa.setProjectId(Long.valueOf(requestParamMap.get("projectId")));
					oa.setResourceType(chres[i]);
					oa.setOrgType("serviceProvider");
					oa.setStatus(1);
					super.txinsert(oa);
				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		//状态0
		for (int i = 0;  nores != null &&  i < nores.length; i++) {
			if ( StringUtil.isNullOrEmpty(nores[i]) ) continue;
			for (int j = 0; j < org_id_list.size(); j++) {
				Long sub_orgId = org_id_list.get(j);
				try {
					InformationProjectResource oa = new InformationProjectResource();
					oa.setAreaId(Long.valueOf(requestParamMap.get("areaId")));
					oa.setOrgId(sub_orgId);
					oa.setProjectId(Long.valueOf(requestParamMap.get("projectId")));
					oa.setResourceType(nores[i]);
					oa.setOrgType("serviceProvider");
					oa.setStatus(0);
					super.txremove(oa);
				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 保存项目中授权的相关资源
	 */
	public void txSaveProjectResource ( Map<String,String> requestParamMap ) {
		String nocheckedres = requestParamMap.get("nocheckedres");
		String checkedres = requestParamMap.get("checkedres");
		String[] chres = checkedres.split(",");
		String[] nores = nocheckedres.split(",");
		
		requestParamMap.remove("nocheckedres");
		requestParamMap.remove("checkedres");
		
		String orgId = requestParamMap.get("orgId");
		Long areaId = Long.valueOf(requestParamMap.get("areaId"));
		Long projectId = Long.valueOf(requestParamMap.get("projectId"));
		
		//List<ProviderOrganization> org_list = this.providerOrganizationService.getOrgListDownwardByOrg( Long.valueOf(orgId) );
		//yuan.yw
		List<SysOrg> org_list = this.sysOrganizationService.getOrgListDownwardByOrg( Long.valueOf(orgId) );
		List<Long> org_id_list = new ArrayList<Long>();
		for (int i = 0; i < org_list.size(); i++) {
			SysOrg pro = org_list.get(i);
			if ( pro != null ) {
				org_id_list.add(pro.getOrgId());
			}
		}
		
		//把该区域、组织里的相关项目授权数据清除
		List<Long> areaIdList = new ArrayList<Long>();
		areaIdList.add(areaId);
		List<Long> projectIdList = new ArrayList<Long>();
		projectIdList.add(projectId);
		this.informationManageNetworkResourceDao.deleteProjectResourceByParamWithBetch(areaIdList, org_id_list, projectIdList);
		
		//重新插入项目授权数据
		//添加(添加数据库中没有的数据)
		for (int i = 0; chres != null && i < chres.length; i++) {
			if ( StringUtil.isNullOrEmpty(chres[i]) ) continue;
			for (int j = 0; j < org_id_list.size(); j++) {
				Long sub_orgId = org_id_list.get(j);
				try {
					Map<String, Object> map = null;
					InformationProjectResource oam = new InformationProjectResource();
					oam.setAreaId(Long.valueOf(requestParamMap.get("areaId")));
					oam.setOrgId(sub_orgId);
					oam.setProjectId(Long.valueOf(requestParamMap.get("projectId")));
					oam.setResourceType(chres[i]);
					oam.setOrgType("serviceProvider");
					oam.setStatus(1);
					map = ObjectUtil.object2Map(oam, false);
					List<InformationProjectResource> list = findByParam(map);
					Long num = 1l;
					if ( list == null || list.size() == 0 ) {
						num = txinsert(oam);
					}
				} catch (NumberFormatException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * 根据项目id、组织id、资源类型,获取项目资源信息
	 * @param projectId - 项目id
	 * @param orgId - 组织Id
	 * @param resourceType - 资源类型
	 * @return
	 */
	public List<Map<String, String>> findAreaIdByProjectOrgIdResType ( String orgId , String resourceType ) {
		List<Map<String, String>> list = this.informationManageNetworkResourceDao.findProjectResourceByProjectOrgIdResourceType( null , orgId, resourceType);
		return list;
	}
	
	/**
	 * 根据项目id、组织id、资源类型,获取项目资源信息
	 * @param projectId - 项目id
	 * @param orgId - 组织Id
	 * @param resourceType - 资源类型
	 * @return
	 */
	public List<Map<String, String>> findAreaIdByProjectOrgIdResType ( String projectId , String orgId , String resourceType ) {
		List<Map<String, String>> list = this.informationManageNetworkResourceDao.findProjectResourceByProjectOrgIdResourceType( projectId , orgId, resourceType);
		return list;
	}
	
	/**
	 * 根据区域id,获取区域下的所有组织id
	 * @param areaId - 区域id
	 * @return
	 */
	public List<String> findOrgIdByAreaId ( String areaId ) {
		List<String> project_list = this.findProjectIdByAreaId(areaId);
		Set<String> result_set = new HashSet<String>();
		if ( project_list == null || project_list.size() == 0 ) {
			return new ArrayList<String>();
		}
		for (int i = 0; i < project_list.size() ; i++) {
			String projectId = project_list.get(i);
			List<String> orgId_list = this.findOrgIdByProjectId(projectId);
			result_set.addAll(orgId_list);
		}
		List<String> result_list = ArrayUtil.set2List(result_set);
		return result_list;
	}
	
	
	
	/**
	 * 根据项目id,获取项目下的所有组织id
	 * @param projectId - 组织id
	 * @return 
	 */
	public List<String> findOrgIdByProjectId ( String projectId ) {
		List<Map<String, String>> list = this.informationManageNetworkResourceDao.findCarryOutOrgIdByProjectId(projectId);
		Set<String> org_set = new HashSet<String>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = list.get(i);
			String orgId = map.get("orgId");
			org_set.add(orgId);
		}
		List<String> org_list = ArrayUtil.set2List(org_set);
		return org_list;
	}
	
	
	/**
	 * 根据区域id,获取区域下的所有项目id
	 * @param areaId - 区域id
	 * @return 
	 */
	public List<String> findProjectIdByAreaId ( String areaId ) {
		List<Map<String, String>> list = this.informationManageNetworkResourceDao.findProjectIdByAreaId(areaId);
		Set<String> project_set = new HashSet<String>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = list.get(i);
			String projectId = map.get("projectId");
			project_set.add(projectId);
		}
		List<String> project_list = ArrayUtil.set2List(project_set);
		return project_list;
	}
	
	
	/**
	 * 根据实施组织id , 获取项目实例
	 * @param orgId - 实施组织id
	 * @return
	 */
	public List<Map<String,String>> findProjectByCarryOutOrgId ( String orgId ) {
		List<Map<String, String>> presource = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(orgId);
		List<Map<String,String>> project_list = new ArrayList<Map<String,String>>();
		Set<String> project_id_set = new HashSet<String>();
		for (int i = 0; i < presource.size() ; i++) {
			Map<String, String> pr_map = presource.get(i);
			if ( pr_map != null && pr_map.containsKey("projectId") ) {
				project_id_set.add(pr_map.get("projectId"));
			}
		}
		
		for ( Iterator<String> it = project_id_set.iterator(); it.hasNext() ; ) {
			String projectId = it.next();
			Map<String, String> project = this.projectInformationDao.findProjectByProjectId(projectId);
			if ( project != null ) {
				project_list.add(project);
			}
		}
		return project_list;
	}
	
	
	/**
	 * 根据资源类型、组织id、授权状态为条件 , 获取有该权限的区域Id
	 * @param orgIds - 组织id
	 * @param resourceType - 资源类型
	 * @param status - 授权状态(0,1)
	 * 					0 ： 没授权
	 * 					1 ： 已授权
	 * @return 区域id
	 */
	public List<String> findAreaIdsByOrgIdResourceTypeWithStatus ( List<String> orgIds , String resourceType , int status ) {
		List<String> area_list = this.informationManageNetworkResourceDao.findAreaIdByOrgIdResourceTypeWithStatus(orgIds, resourceType, status);
		return area_list;
	}
	
	/**
	 * 根据资源类型、区域Id、授权状态为条件 , 获取有该权限的组织id
	 * @param areaIds - 区域Id
	 * @param resourceType - 资源类型
	 * @param status - 授权状态(0,1)
	 * 					0 ： 没授权
	 * 					1 ： 已授权
	 * @return 组织id
	 */
	public List<Map<String,String>> findOrgIdsByAreaIdResourceTypeWithStatus ( List<String> areaIds , String resourceType , int status ) {
		List<Map<String,String>> area_list =  this.informationManageNetworkResourceDao.findOrgIdByAreaIdResourceTypeWithStatus(areaIds, resourceType, status);
		return area_list;
	}
	
	
	
	
	/******************* 格式化 **********************/
	public Map<String,Object> formatObject ( Map<String,Object> oar_map ) {
		Map<String,Object> obj_map = new LinkedHashMap<String, Object>();
		for (Iterator<String> it = oar_map.keySet().iterator();it.hasNext();) {
			String key = it.next();
			Object value = oar_map.get(key);
			obj_map.put(key, value);
			if ( value == null || (value+"").isEmpty() ) {
				continue;
			}
			if ( key.equals("project") ) {
				InformationProject project = (InformationProject) obj_map.get(key);
				String projectName = project.getName();
				obj_map.put("projectName", projectName);
			}  else {
				obj_map.put(key, value);
			}
		}
		return obj_map;
	}
	
	
	
	/*********** getter setter ***********/
	public InformationManageNetworkResourceDao getInformationManageNetworkResourceDao() {
		return informationManageNetworkResourceDao;
	}
	public void setInformationManageNetworkResourceDao(
			InformationManageNetworkResourceDao informationManageNetworkResourceDao) {
		this.informationManageNetworkResourceDao = informationManageNetworkResourceDao;
	}
	
	public ProjectInformationDao getProjectInformationDao() {
		return projectInformationDao;
	}
	public void setProjectInformationDao(ProjectInformationDao projectInformationDao) {
		this.projectInformationDao = projectInformationDao;
	}
	public InformationNetworkService getInformationNetworkService() {
		return informationNetworkService;
	}
	public void setInformationNetworkService(
			InformationNetworkService informationNetworkService) {
		this.informationNetworkService = informationNetworkService;
	}
	
	 
}
