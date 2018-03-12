package com.iscreate.op.service.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.report.RoutineinspectionReportDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.system.SysOrganizationService;

public class RoutineinspectionReportServiceImpl implements RoutineinspectionReportService {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private RoutineinspectionReportDao routineinspectionReportDao;
	

	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}


	

	
	public RoutineinspectionReportDao getRoutineinspectionReportDao() {
		return routineinspectionReportDao;
	}
	
	public void setRoutineinspectionReportDao(
			RoutineinspectionReportDao routineinspectionReportDao) {
		this.routineinspectionReportDao = routineinspectionReportDao;
	}
	
	/**
	 * 获取组织ID获取该组织以下组织的巡检报表数据TOP6
	* @date Mar 6, 20133:02:13 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getRoutineinspectionReportTOPSixByOrgId(long orgId){
		log.info("进入getRoutineinspectionReportTOPFourByOrgId方法");
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(orgId,"BusinessOrganization","downward");

		String orgIds = "";
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(orgListDownwardByOrg != null && orgListDownwardByOrg.size() > 0){
			log.info("开始循环orgListDownwardByOrg");
			for (Map<String,Object> b : orgListDownwardByOrg) {
				orgIds = orgIds + b.get("orgId") + ",";
			}
			log.info("结束循环orgListDownwardByOrg");
			orgIds = orgIds.substring(0,orgIds.length() - 1);
		}else{
			log.info("orgListDownwardByOrg为空");
		}
		List<Map<String,Object>> routineinspectionReportTOPFour = null;
		if(orgIds != null && !"".equals(orgIds)){	
			routineinspectionReportTOPFour = this.routineinspectionReportDao.getRoutineinspectionReportTOPSix(orgIds);
		}else{
			log.info("orgIds为空");
		}
		log.info("退出getRoutineinspectionReportTOPFourByOrgId方法 返回值为:"+routineinspectionReportTOPFour);
		return routineinspectionReportTOPFour;
	}

	/**
	 * 获取项目ID获取该项目关联组织的巡检报表数据TOP4
	* @date Mar 6, 20134:53:00 PM
	* @Description: TODO 
	* @param @param projectId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getRoutineinspectionReportTOPFourByProjectId(String projectId){
		log.info("进入getRoutineinspectionReportTOPFourByProjectId方法");
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		//List<Map<String,String>> orgByProjectIdService = this.organizationService.getOrgByProjectIdService(projectId);
		//yuan.yw
		List<Map<String,String>> orgByProjectIdService = this.sysOrganizationService.getOrgByProjectIdService(projectId);
		String orgIds = "";
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(orgByProjectIdService != null && orgByProjectIdService.size() > 0){
			log.info("开始循环orgByProjectIdService");
			for (Map<String,String> b : orgByProjectIdService) {
				orgIds = orgIds + b.get("id") + ",";
			}
			log.info("结束循环orgByProjectIdService");
			orgIds = orgIds.substring(0,orgIds.length() - 1);
		}else{
			log.info("orgListDownwardByOrg为空");
		}
		List<Map<String,Object>> routineinspectionReportTOPFour = null;
		if(orgIds != null && !"".equals(orgIds)){			
			routineinspectionReportTOPFour = this.routineinspectionReportDao.getRoutineinspectionReportTOPFourByClosed(orgIds);
		}else{
			log.info("routineinspectionReportTOPFour为空");
		}
		log.info("退出getRoutineinspectionReportTOPFourByProjectId方法 返回值为:"+routineinspectionReportTOPFour);
		return routineinspectionReportTOPFour;
	}

	
	/**
	 * 获取userId获取用户身份
	* @date Mar 6, 20134:07:54 PM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getLoginIdBelongEnterpriseType(String userId){
//		log.info("进入getLoginIdBelongEnterpriseType方法");
		List<Map<String, Object>> nextProviderOrgByOrgId = new ArrayList<Map<String, Object>>();
//		Map<String, String> loginIdBelongEnterpriseTypeService = this.sysOrganizationService.getLoginIdBelongEnterpriseTypeService();
//		if(loginIdBelongEnterpriseTypeService != null ){
//			String isCoo = loginIdBelongEnterpriseTypeService.get("isCoo");
//			if("true".equals(isCoo)){
//				//List<ProviderOrganization> topLevelOrgByAccount = providerOrganizationService.getTopLevelOrgByAccount(userId);
//				List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);
//
//				if(topLevelOrgByAccount != null && !topLevelOrgByAccount.isEmpty()){
//					SysOrg providerOrganization = topLevelOrgByAccount.get(0);
//					if(providerOrganization != null){
//						long orgId = providerOrganization.getOrgId();
//						if(orgId == 16){
//							//List<ProviderOrganization> nextProviderOrg = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
//							//yuan.yw
//							List<Map<String,Object>> nextProviderOrg=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
//						
//							if(nextProviderOrg != null && nextProviderOrg.size() > 0){
//								log.info("开始循环nextProviderOrg");
//								for(Map<String,Object> p:nextProviderOrg){
//									Map<String, Object> map = new HashMap<String, Object>();
//									map.put("type", "Org");
//									long id = Long.valueOf(p.get("orgId")+"");
//									String name = p.get("name")+"";
//									map.put("id", id);
//									map.put("name", name);
//									nextProviderOrgByOrgId.add(map);
//								}
//								log.info("结束循环nextProviderOrg");
//							}else{
//								log.info("nextProviderOrg为空");
//							}
//						}else{
//							Map<String, Object> map = new HashMap<String, Object>();
//							map.put("type", "Org");
//							long id = providerOrganization.getOrgId();
//							String name = providerOrganization.getName();
//							map.put("id", id);
//							map.put("name", name);
//							nextProviderOrgByOrgId.add(map);
//						}
//					}
//				}
//			}else{
//				//Map<String, String> topOrgService = organizationService.getTopOrgService();
//				Map<String, String> topOrgService = this.sysOrganizationService.getTopOrgService();
//				if(topOrgService != null && !topOrgService.isEmpty()){
//					String orgId = topOrgService.get("id");
//					long orgIdl = Long.valueOf(orgId);
//						//List<Map<String, String>> findProjectByOrgId = organizationService.getProjectToDownwardOrgByOrgIdService(orgIdl);
//						//yuan.yw
//						List<Map<String, String>> findProjectByOrgId = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(orgIdl);
//						if(findProjectByOrgId != null && findProjectByOrgId.size() > 0){
//							log.info("开始循环findProjectByOrgId");
//							for(Map<String, String> m:findProjectByOrgId){
//								Map<String, Object> map = new HashMap<String, Object>();
//								map.put("type", "Project");
//								String id = m.get("id");
//								String name = m.get("NAME");
//								map.put("id", id);
//								map.put("name", name);
//								nextProviderOrgByOrgId.add(map);
//							}
//							log.info("结束循环findProjectByOrgId");
//						}else{
//							log.info("nextProviderOrg为空");
//						}
//				}
//			}
//		}else{
//			log.info("loginIdBelongEnterpriseTypeService为空");
//		}
//		log.info("退出getLoginIdBelongEnterpriseType方法 返回值为:"+nextProviderOrgByOrgId);
		return nextProviderOrgByOrgId;
	}
}
