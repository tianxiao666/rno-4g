package com.iscreate.op.service.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iscreate.op.dao.informationmanage.InformationManageNetworkResourceDao;
import com.iscreate.op.dao.report.BaseStationOutDao;
import com.iscreate.op.dao.report.UrgentRepairReportDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.system.SysOrganizationService;


public class BaseStationOutServiceImpl implements BaseStationOutService {
	public BaseStationOutDao baseStationOutDao;
	
	public InformationManageNetworkResourceDao informationManageNetworkResourceDao;
	
	public UrgentRepairReportDao urgentRepairReportDao;
	
	public NetworkResourceInterfaceService networkResourceInterfaceService;
	
	
	
	
	
	private SysOrganizationDao sysOrganizationDao;//du.hw添加
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationDao getSysOrganizationDao() {
		return sysOrganizationDao;
	}

	public void setSysOrganizationDao(SysOrganizationDao sysOrganizationDao) {
		this.sysOrganizationDao = sysOrganizationDao;
	}

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	

	

	

	public BaseStationOutDao getBaseStationOutDao() {
		return baseStationOutDao;
	}

	public void setBaseStationOutDao(BaseStationOutDao baseStationOutDao) {
		this.baseStationOutDao = baseStationOutDao;
	}
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.baseStationOutService#getbaseStationOutByOrg(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Map<String, Object>> getbaseStationOutByOrg(String userId,String beginTime,String endTime){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		//List<ProviderOrganization> topLevelOrgByAccount = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		if(topLevelOrgByAccount != null && !topLevelOrgByAccount.isEmpty()){
			
			SysOrg providerOrganization = topLevelOrgByAccount.get(0);
			if(providerOrganization !=null ){
				long orgId = providerOrganization.getOrgId();
				list = getbaseStationOutByOrgId(orgId, beginTime, endTime);
			}
		}
		return list;
	}
	
	

	public List<Map<String, Object>> getbaseStationOutByOrgId(long oId,String beginTime,String endTime){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> providerOrganization = sysOrganizationDao.getOrganizationMessageByOrgId(oId);
			if(providerOrganization !=null ){
//				long orgId = Long.parseLong(providerOrganization.get("id")+"");
				String pName = (String) providerOrganization.get("name");
			//	List<ProviderOrganization> osrList = this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
				//yuan.yw
				List<Map<String,Object>> osrList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(oId,"BusinessOrganization");
				if(osrList != null && !osrList.isEmpty()){
					for(Map<String,Object> p:osrList){
						Map<String, Object> map = new HashMap<String, Object>();
						String name = p.get("name")+"";
						long id = Long.valueOf(p.get("orgId")+"");
						long areaBaseStationCounts = 0;
						long baseStationOut = 0;
						//List<ProviderOrganization> providerOrgDownwardByOrgIdAndOrgAttrService = this.providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(id,"BusinessOrganization");
						List<Map<String,Object>> providerOrgDownwardByOrgIdAndOrgAttrService = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(id,"BusinessOrganization","downward");
						if(providerOrgDownwardByOrgIdAndOrgAttrService != null && !providerOrgDownwardByOrgIdAndOrgAttrService.isEmpty()){
							String prs = "";
							String areaIds = "";
							List<Map<String,Object>> areaBaseStationCount = null;
							for(Map<String,Object> pr:providerOrgDownwardByOrgIdAndOrgAttrService){
								prs = prs + pr.get("orgId") + ",";
								List<Map<String, String>> org_Area_ResourceByOrgId = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(pr.get("orgId")+"");
								//List<Org_Area_Resource> org_Area_ResourceByOrgId = this.providerOrganizationDao.getOrg_Area_ResourceByOrgId(pr.getId()+"");
								Set<String> set = new HashSet<String>();
								if(org_Area_ResourceByOrgId != null && !org_Area_ResourceByOrgId.isEmpty()){
									for(Map<String, String> oa:org_Area_ResourceByOrgId){
										set.add(oa.get("areaId"));
									}
									
									for(String oa:set){
										areaIds = areaIds + oa + ",";
									}
									
								}
							}
							if(areaIds != null && !areaIds.equals("")){
								areaIds = areaIds.substring(0, areaIds.length() - 1);
								areaBaseStationCount = this.baseStationOutDao.getAreaBaseStationCount(areaIds);
							}
							
							if(areaBaseStationCount != null && !areaBaseStationCount.isEmpty()){
								for(Map<String,Object> m:areaBaseStationCount){
									String string = m.get("baseStationCount").toString();
									Long valueOf = Long.valueOf(string);
									areaBaseStationCounts = areaBaseStationCounts + valueOf;
									//System.out.println(areaBaseStationCounts);
								}
							}
							if(prs != null && !prs.equals("")){
								prs = prs.substring(0, prs.length() - 1);
								List<Map<String,Object>> getbaseStationOut = this.baseStationOutDao.getbaseStationOut(prs, beginTime, endTime);
								if(getbaseStationOut != null && !getbaseStationOut.isEmpty()){
									baseStationOut = baseStationOut + getbaseStationOut.size();
								}
							}
						}
						map.put("baseStationCount", areaBaseStationCounts);
						map.put("baseStationOut", baseStationOut);
						map.put("orgName", name);
						map.put("orgId", id+"");
						map.put("pName", pName);
						map.put("pOrgId", oId);
						list.add(map);
					}
				}
			}
		return list;
	}
	
	
	public List<Map<String, Object>> getbaseStationOutByOrgIdTopOrg(long oId,String beginTime,String endTime){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		//List<ProviderOrganization> upProviderOrgByOrgIdService = this.providerOrganizationService.getUpProviderOrgByOrgIdService(oId);
		//yuan.yw
		List<SysOrg> upProviderOrgByOrgIdService = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(oId,"parent");
		
		long uoId = 0;
		if(upProviderOrgByOrgIdService != null && !upProviderOrgByOrgIdService.isEmpty()){
			uoId = upProviderOrgByOrgIdService.get(0).getOrgId();
		}
		if(uoId != 0){
			list = getbaseStationOutByOrgId(uoId, beginTime, endTime);
		}
		return list;
	}
	
	/**
	 * 按项目
	 */
	public List<Map<String, Object>> getbaseStationOutByOrgIdProject(long oId,String beginTime,String endTime){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		//List<ProviderOrganization> upProviderOrgByOrgIdService = this.providerOrganizationService.getUpProviderOrgByOrgIdService(oId);
			list = getbaseStationOutByOrgIdAndProject(oId, beginTime, endTime);
		return list;
	}
	
	private List<Map<String, Object>> getbaseStationOutByOrgIdAndProject(long oId,String beginTime,String endTime){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> providerOrganization = sysOrganizationDao.getOrganizationMessageByOrgId(oId);
		if(providerOrganization !=null ){
				long orgId = Long.parseLong((String) providerOrganization.get("id"));
				String pName = (String) providerOrganization.get("name");
				//List<Map<String, String>> orgListDownwardByOrg = organizationService.getProjectToDownwardOrgByOrgIdService(orgId);
				//yuan.yw
				List<Map<String, String>> orgListDownwardByOrg = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(orgId);
				if(orgListDownwardByOrg != null && !orgListDownwardByOrg.isEmpty()){
					
					for (Map<String, String> map : orgListDownwardByOrg) {
						Map<String, Object> mapB = new HashMap<String, Object>();
						String string = map.get("id");
						String name = map.get("NAME");
						long areaBaseStationCounts = 0;
						long baseStationOut = 0;
						List<Map<String,String>> findProjectResourceByProjectId = this.informationManageNetworkResourceDao.findAreaIdAndResourceTypeByProjectIdWithoutSame(string);
						//System.out.println(findProjectResourceByProjectId);
						if(findProjectResourceByProjectId != null && !findProjectResourceByProjectId.isEmpty()){
							Map<String, List<Map<String,String>>> reMap = new HashMap<String, List<Map<String,String>>>();
							for(Map<String,String> m:findProjectResourceByProjectId){
								String areaId = m.get("areaId"); 
								String resourceType = m.get("resourceType"); 
								List<Map<String,String>> baseStationByAreaIdAndReType = this.networkResourceInterfaceService.getBaseStationByAreaIdAndReType(areaId, resourceType);
								if(baseStationByAreaIdAndReType != null && !baseStationByAreaIdAndReType.isEmpty()){
									for(Map<String,String> m1:baseStationByAreaIdAndReType){
										String entityType = m1.get("_entityType");
										if(reMap.get(entityType) == null){
											List<Map<String,String>> list2 = new ArrayList<Map<String,String>>();
											list2.add(m1);
											reMap.put(entityType, list2);
										}else{
											List<Map<String, String>> list2 = reMap.get(entityType);
											list2.add(m1);
											reMap.put(entityType, list2);
										}
									}
								}
							}
							if(reMap != null){
								for(String key:reMap.keySet()){
									List<String> reIds = new ArrayList<String>();
									List<Map<String, String>> list2 = reMap.get(key);
									if(list2 !=null && !list2.isEmpty()){
										for(Map<String, String> m:list2){
											String id = m.get("id");
											reIds.add(id);
										}
									}
									int size = 0;
									if(reIds.size() > 500){
										//System.out.println("====="+reIds.size()%500);
										if(reIds.size()%500 > 0){
											size = reIds.size()/500 + 1;
										}else{
											size = reIds.size()/500;
										}
										int ci = 0;
										int bi = 0;
										for(int i = 0;i < size;i++){
											ci = 499 + i;
											if(ci > reIds.size()){
												ci = reIds.size();
											}else{
												
											}
											List<String> re = new ArrayList<String>();
											for(;bi <= ci;bi++){
												re.add(reIds.get(bi));
											}
											
											List<Map<String,Object>> urgentRepairBylatestAllowedTimeAndJudge = urgentRepairReportDao.getUrgentRepairByreIdsAndreType(re, key, beginTime, endTime, null, null, null);
											if(urgentRepairBylatestAllowedTimeAndJudge != null && urgentRepairBylatestAllowedTimeAndJudge.size() > 0){
												list.addAll(urgentRepairBylatestAllowedTimeAndJudge);
											}
										}
									}else{
										List<Map<String,Object>> urgentRepairBylatestAllowedTimeAndJudge = urgentRepairReportDao.getUrgentRepairByreIdsAndreType(reIds, key, beginTime, endTime, null, null, null);
										if(urgentRepairBylatestAllowedTimeAndJudge != null && urgentRepairBylatestAllowedTimeAndJudge.size() > 0){
											list.addAll(urgentRepairBylatestAllowedTimeAndJudge);
										}
									}
								}
							}
						}
						//long id = Long.valueOf(string);
						mapB.put("baseStationCount", areaBaseStationCounts);
						mapB.put("baseStationOut", baseStationOut);
						mapB.put("orgName", name);
						mapB.put("orgId", string+"");
						mapB.put("pName", pName);
						mapB.put("pOrgId", oId);
						list.add(mapB);
					}
				}
			}
//				List<Map<String, String>> osrList = organizationService.getProjectToDownwardOrgByOrgIdService(orgId);
//				//List<ProviderOrganization> osrList = this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
//				if(osrList != null && !osrList.isEmpty()){
//					for(Map<String, String> p:osrList){
//						Map<String, Object> map = new HashMap<String, Object>();
//						String name = p.get("NAME");
//						String id = p.get("id");
//						long areaBaseStationCounts = 0;
//						long baseStationOut = 0;
//						List<Map<String, String>> providerOrgDownwardByOrgIdAndOrgAttrService = organizationService.getOrgByProjectIdService(id);
//						//List<ProviderOrganization> providerOrgDownwardByOrgIdAndOrgAttrService = this.providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(id,"BusinessOrganization");
//						if(providerOrgDownwardByOrgIdAndOrgAttrService != null && !providerOrgDownwardByOrgIdAndOrgAttrService.isEmpty()){
//							String prs = "";
//							String areaIds = "";
//							List<Map<String,Object>> areaBaseStationCount = null;
//							for(Map<String, String> pr:providerOrgDownwardByOrgIdAndOrgAttrService){
//								prs = prs + pr.get("id") + ",";
//								List<Map<String, String>> org_Area_ResourceByOrgId = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(pr.get("id")+"");
//								//List<Org_Area_Resource> org_Area_ResourceByOrgId = this.providerOrganizationDao.getOrg_Area_ResourceByOrgId(pr.getId()+"");
//								Set<String> set = new HashSet<String>();
//								if(org_Area_ResourceByOrgId != null && !org_Area_ResourceByOrgId.isEmpty()){
//									for(Map<String, String> oa:org_Area_ResourceByOrgId){
//										set.add(oa.get("areaId"));
//									}
//									
//									for(String oa:set){
//										areaIds = areaIds + oa + ",";
//									}
//									
//								}
//							}
//							if(areaIds != null && !areaIds.equals("")){
//								areaIds = areaIds.substring(0, areaIds.length() - 1);
//								areaBaseStationCount = this.baseStationOutDao.getAreaBaseStationCount(areaIds);
//							}
//							
//							if(areaBaseStationCount != null && !areaBaseStationCount.isEmpty()){
//								for(Map<String,Object> m:areaBaseStationCount){
//									String string = m.get("baseStationCount").toString();
//									Long valueOf = Long.valueOf(string);
//									areaBaseStationCounts = areaBaseStationCounts + valueOf;
//									//System.out.println(areaBaseStationCounts);
//								}
//							}
//							if(prs != null && !prs.equals("")){
//								prs = prs.substring(0, prs.length() - 1);
//								List<Map<String,Object>> getbaseStationOut = this.baseStationOutDao.getbaseStationOut(prs, beginTime, endTime);
//								if(getbaseStationOut != null && !getbaseStationOut.isEmpty()){
//									baseStationOut = baseStationOut + getbaseStationOut.size();
//								}
//							}
//						}
//						map.put("baseStationCount", areaBaseStationCounts);
//						map.put("baseStationOut", baseStationOut);
//						map.put("orgName", name);
//						map.put("orgId", id+"");
//						map.put("pName", pName);
//						map.put("pOrgId", oId);
//						list.add(map);
//					}
//				}
//			}
		return list;
	}

	public InformationManageNetworkResourceDao getInformationManageNetworkResourceDao() {
		return informationManageNetworkResourceDao;
	}

	public void setInformationManageNetworkResourceDao(
			InformationManageNetworkResourceDao informationManageNetworkResourceDao) {
		this.informationManageNetworkResourceDao = informationManageNetworkResourceDao;
	}

	public UrgentRepairReportDao getUrgentRepairReportDao() {
		return urgentRepairReportDao;
	}

	public void setUrgentRepairReportDao(UrgentRepairReportDao urgentRepairReportDao) {
		this.urgentRepairReportDao = urgentRepairReportDao;
	}

	public NetworkResourceInterfaceService getNetworkResourceInterfaceService() {
		return networkResourceInterfaceService;
	}

	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}
}
