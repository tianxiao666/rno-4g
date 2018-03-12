package com.iscreate.op.service.informationmanage;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.dao.informationmanage.ProjectInformationDao;
import com.iscreate.op.pojo.informationmanage.Area;
import com.iscreate.op.pojo.informationmanage.InformationEnterprise;
import com.iscreate.op.pojo.informationmanage.InformationProject;
import com.iscreate.op.pojo.informationmanage.Staff;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.system.SysOrganizationService;

@SuppressWarnings("finally")
public class ProjectInformationServiceImpl extends
		BaseServiceImpl<InformationProject> implements
		ProjectInformationService {

	/** ********** 依赖注入 ************* */
	private ProjectInformationDao projectInformationDao;
	private NetworkResourceInterfaceService networkResourceInterfaceService;

	private SysOrganizationService sysOrganizationService;

	/** ********** 属性 ************* */

	private Log log = LogFactory.getLog(this.getClass());

	/** *************** service ****************** */

	/**
	 * 验证项目编号是否唯一
	 * 
	 * @param proId -
	 *            项目编号
	 * @return true : 已存在 , false : 不存在
	 */
	public boolean checkProjectNumberExists(String projectNumber, String id) {
		boolean exists = false;
		if (StringUtil.isNullOrEmpty(id)) {
			id = "0";
		}
		exists = projectInformationDao.checkProjectNumberExists(projectNumber,
				id);
		return exists;
	}

	/**
	 * 验证项目存在
	 * 
	 * @param param_map -
	 *            参数集合
	 * @return true : 已存在 , false : 不存在
	 */
	public boolean checkProjectExists(Map<String, String> param_map) {
		boolean exists = false;
		List<InformationProject> list = findByParam(param_map);
		exists = list != null && list.size() > 0;
		return exists;
	}

	public Map<String, Object> findSingleProjectInfo(
			Map<String, String> param_Map) {
		InformationProject project = findSingleByParam(param_Map);
		Map<String, Object> project_map = null;
		try {
			project_map = ObjectUtil.object2Map(project, false);
			project_map = formatObject(project_map);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			return project_map;
		}
	}

	/**
	 * 根据组织id,获取相应项目信息
	 * 
	 * @param orgId -
	 *            组织id
	 * @return
	 */
	public List<Map<String, String>> findProjectByOrgId(String orgId) {
		List<Map<String, String>> list = projectInformationDao
				.findProjectByOrgId(orgId);
		return list;
	}

	/**
	 * 保存项目信息
	 * 
	 * @param param_map -
	 *            项目信息
	 * @return 是否操作成功
	 */
	public boolean saveProjectInfo(Map<String, String> param_map) {

		return false;
	}

	/** ********** 对象格式化 ************** */

	/**
	 * 项目类格式化
	 * 
	 * @param project -
	 *            项目集合
	 */
	public Map<String, Object> formatObject(Map<String, Object> project) {
		Map<String, Object> obj_map = new LinkedHashMap<String, Object>();
		for (Iterator<String> it = project.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			Object value = project.get(key);
			obj_map.put(key, value);
			if (value == null) {
				continue;
			}
			if (key.equals("cityId")) {
				String cityId = value + "";
				Map<String, String> city = this.networkResourceInterfaceService
						.getResourceByReIdAndReTypeService(cityId, "Sys_Area");
				if (city != null) {
					String cityName = city.get("name");
					obj_map.put("cityName", cityName);
				}
			} else if (value instanceof Date) {
				Date date = (Date) value;
				String formatDate = DateUtil.formatDate(date, "yyyy-MM-dd");
				obj_map.put(key, formatDate);
			} else if (key.equals("projectManager")
					|| key.equals("salesManager")) {
				Staff staff = (Staff) value;
				String staffName = staff.getStaffName();
				obj_map.put(key + "Name", staffName);
			} else if (key.equals("clientEnterprise")
					|| key.equals("serverEnterprise")) {
				InformationEnterprise enterprise = (InformationEnterprise) value;
				Long id = enterprise.getId();
				obj_map.put(key + "Id", id);
			} else if (key.contains("OrgId")) {
				String orgId = value + "";

				// TODO need to change the interface
				// List<CustomerOrganization> orgListUpwardByOrg =
				// customerOrganizationService.getCustomerOrgUpwardByOrgIdService(Long.valueOf(orgId));
				List<Map<String, Object>> orgListUpwardByOrg = sysOrganizationService
						.getAllTheTopOrgByOrgIdService(Long.valueOf(orgId));

				String orgString = "";
				if (orgListUpwardByOrg != null && orgListUpwardByOrg.size() > 0) {
					for (int i = 0; i < orgListUpwardByOrg.size(); i++) {
						Map<String, Object> map = orgListUpwardByOrg.get(i);
						String name = (map.get("name") == null ? "" : map.get(
								"name").toString());
						if (i == 0) {
							orgString += name;
						} else {
							orgString = orgString + "/" + name;
						}
					}
				}
//				for (int i = 0; i < orgListUpwardByOrg.size(); i++) {
//					String orgName = orgListUpwardByOrg.get(i).getName();
//					if (i == 0) {
//						orgString = orgName + orgString;
//					} else {
//						orgString = orgName + "/" + orgString;
//					}
//				}
				key = key.replace("Id", "");
				obj_map.put(key + "FullName", orgString);
			} 
//			else if (key.equals("serverOrgId")) {
//				String orgId = value + "";
//				List<ProviderOrganization> orgListUpwardByOrg = providerOrganizationService
//						.getOrgListUpwardByOrg(Long.valueOf(orgId));
//				String orgString = "";
//				for (int i = 0; i < orgListUpwardByOrg.size(); i++) {
//					String orgName = orgListUpwardByOrg.get(i).getName();
//					if (i == 0) {
//						orgString = orgName + orgString;
//					} else {
//						orgString = orgName + "/" + orgString;
//					}
//				}
//				key = key.replace("Id", "");
//				obj_map.put(key + "FullName", orgString);
//			}
		}
		return obj_map;
	}

	/**
	 * 根据项目id , 更新项目信息
	 */
	public int updateProjectInfoById(Long id, Map setMap) {
		int num = 0;
		Map<String, Object> param_map = new HashMap<String, Object>();
		param_map.put("id", id);
		num = projectInformationDao.txupdateMapObj(param_map, setMap);
		return num;
	}

	/** ************* getter setter *************** */
	public ProjectInformationDao getProjectInformationDao() {
		return projectInformationDao;
	}

	public void setProjectInformationDao(
			ProjectInformationDao projectInformationDao) {
		this.projectInformationDao = projectInformationDao;
	}

	

	public NetworkResourceInterfaceService getNetworkResourceInterfaceService() {
		return networkResourceInterfaceService;
	}

	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}

}
