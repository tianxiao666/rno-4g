package com.iscreate.op.service.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.dao.informationmanage.InformationManageNetworkResourceDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.system.SysOrganizationService;

public class NetReportServiceImpl implements NetReportService {
	
	private NetworkResourceInterfaceService networkResourceInterfaceService;
	
	
	
	
	public InformationManageNetworkResourceDao informationManageNetworkResourceDao;
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	
	private SysOrganizationDao sysOrganizationDao;//du.hw添加
	
	

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
	
	public InformationManageNetworkResourceDao getInformationManageNetworkResourceDao() {
		return informationManageNetworkResourceDao;
	}




	public void setInformationManageNetworkResourceDao(
			InformationManageNetworkResourceDao informationManageNetworkResourceDao) {
		this.informationManageNetworkResourceDao = informationManageNetworkResourceDao;
	}




	public NetworkResourceInterfaceService getNetworkResourceInterfaceService() {
		return networkResourceInterfaceService;
	}




	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}

	

	




	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getStationByArea(java.lang.String)
	 */
	public List<Map<String,String>> getStationByArea(String userId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		List<Map<String,String>> areaByAccountService = this.networkResourceInterfaceService.getAreaByAccountService(userId);
		Set<Map<String, String>> set = new HashSet<Map<String,String>>();
		if(areaByAccountService != null && !areaByAccountService.isEmpty()){
			for(Map<String,String> map : areaByAccountService){
				set.add(map);
			}
			for(Map<String, String> map:set){
				String areaId = map.get("id");
				String areaName = map.get("name");
				Map<String, String> reportResourceListService = this.networkResourceInterfaceService.getReportResourceListService(areaId, "Station,BaseStation_GSM,BaseStation_TD");
				reportResourceListService.put("areaId", areaId);
				reportResourceListService.put("areaName", areaName);
				reportResourceListService.put("pName", "");
				reportResourceListService.put("pId", "0");
				list.add(reportResourceListService);
			}
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getWANByArea(java.lang.String)
	 */
	public List<Map<String,String>> getWANByArea(String userId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		List<Map<String,String>> areaByAccountService = this.networkResourceInterfaceService.getAreaByAccountService(userId);
		Set<Map<String, String>> set = new HashSet<Map<String,String>>();
		if(areaByAccountService != null && !areaByAccountService.isEmpty()){
			for(Map<String,String> map : areaByAccountService){
				set.add(map);
			}
			for(Map<String, String> map:set){
				String areaId = map.get("id");
				String areaName = map.get("name");
				Map<String, String> reportResourceListService = this.networkResourceInterfaceService.getReportResourceListService(areaId, "Cell,BaseStation_GSM,BaseStation_TD");
				reportResourceListService.put("areaId", areaId);
				reportResourceListService.put("areaName", areaName);
				reportResourceListService.put("pName", "");
				reportResourceListService.put("pId", "0");
				list.add(reportResourceListService);
			}
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getStationByOrg(java.lang.String)
	 */
	public List<Map<String,String>> getStationByOrg(String userId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		//List<ProviderOrganization> topLevelOrgByAccount = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

		if(topLevelOrgByAccount != null && !topLevelOrgByAccount.isEmpty()){
			
			SysOrg providerOrganization = topLevelOrgByAccount.get(0);
			if(providerOrganization !=null ){
				long orgId = providerOrganization.getOrgId();
				String pName = providerOrganization.getName();
				//List<ProviderOrganization> osrList = this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
				//yuan.yw
				List<Map<String,Object>> osrList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
			
				if(osrList != null && !osrList.isEmpty()){
					for(Map<String,Object> p:osrList){
						String name = p.get("name")+"";
						long id = Long.valueOf(p.get("orgId")+"");
						Map<String, String> map = new HashMap<String, String>();
						long parseLong = 0;
						long baseStation_GSM = 0;
						long baseStation_TD = 0;
					//	List<ProviderOrganization> providerOrgDownwardByOrgIdAndOrgAttrService = this.providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(p.getId(),"BusinessOrganization");
						List<Map<String,Object>> providerOrgDownwardByOrgIdAndOrgAttrService = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(id,"BusinessOrganization","downward");

						List<Map<String, String>> org_Area_ResourceByOrgId = new ArrayList<Map<String,String>>();
						if(providerOrgDownwardByOrgIdAndOrgAttrService != null){
							for(Map<String,Object> pr:providerOrgDownwardByOrgIdAndOrgAttrService){
								long id2 = Long.valueOf(pr.get("orgId")+"");
								List<Map<String,String>> findProjectResourceByOrgId = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(id2+"");
								org_Area_ResourceByOrgId.addAll(findProjectResourceByOrgId);
							}
						}
						//List<Org_Area_Resource> org_Area_ResourceByOrgId = this.providerOrganizationDao.getOrg_Area_ResourceByOrgId(p.getId()+"");
						Set<String> set = new HashSet<String>();
						if(org_Area_ResourceByOrgId != null && !org_Area_ResourceByOrgId.isEmpty()){
							for(Map<String, String> oa:org_Area_ResourceByOrgId){
								set.add(oa.get("areaId"));
							}
							for(String oa:set){
								String areaId = oa;
								Map<String, String> reportResourceListService = this.networkResourceInterfaceService.getReportResourceListService(areaId, "Station,BaseStation_GSM,BaseStation_TD");
								if(reportResourceListService != null){
									String string = reportResourceListService.get("Station");
									parseLong = parseLong + Long.parseLong(string);
									String string2 = reportResourceListService.get("BaseStation_GSM");
									baseStation_GSM = baseStation_GSM + Long.parseLong(string2);
									String string3 = reportResourceListService.get("BaseStation_TD");
									baseStation_TD = baseStation_TD + Long.parseLong(string3);
									
								}
							}
						}
						map.put("orgName", name);
						map.put("orgId", id+"");
						map.put("Station", parseLong+"");
						map.put("BaseStation_GSM", baseStation_GSM+"");
						map.put("BaseStation_TD", baseStation_TD+"");
						map.put("pName", pName);
						list.add(map);
					}
				}
			}
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getWANByOrgAction(java.lang.String)
	 */
	public List<Map<String,String>> getWANByOrg(String userId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		//List<ProviderOrganization> topLevelOrgByAccount = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

		if(topLevelOrgByAccount != null && !topLevelOrgByAccount.isEmpty()){
			
			SysOrg providerOrganization = topLevelOrgByAccount.get(0);
			if(providerOrganization !=null ){
				long orgId = providerOrganization.getOrgId();
				String pName = providerOrganization.getName();
			//	List<ProviderOrganization> osrList = this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
				//yuan.yw
				List<Map<String,Object>> osrList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
			
				if(osrList != null && !osrList.isEmpty()){
					for(Map<String,Object> p:osrList){
						String name = p.get("name")+"";
						long id = Long.valueOf(p.get("orgId")+"");
						Map<String, String> map = new HashMap<String, String>();
						long baseStation_GSM = 0;
						long baseStation_TD = 0;
						long cell = 0;
//						List<ProviderOrganization> providerOrgDownwardByOrgIdAndOrgAttrService = this.providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(p.getId(),"BusinessOrganization");
						List<Map<String,Object>> providerOrgDownwardByOrgIdAndOrgAttrService = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(id,"BusinessOrganization","downward");

						List<Map<String, String>> org_Area_ResourceByOrgId = new ArrayList<Map<String,String>>();
						if(providerOrgDownwardByOrgIdAndOrgAttrService != null){
							for(Map<String,Object> pr:providerOrgDownwardByOrgIdAndOrgAttrService){
								long id2 = Long.valueOf(pr.get("orgId")+"");
								List<Map<String,String>> findProjectResourceByOrgId = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(id2+"");
								org_Area_ResourceByOrgId.addAll(findProjectResourceByOrgId);
							}
						}
						//List<Map<String, String>> org_Area_ResourceByOrgId = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(p.getId()+"");
						//List<Org_Area_Resource> org_Area_ResourceByOrgId = this.providerOrganizationDao.getOrg_Area_ResourceByOrgId(p.getId()+"");
						Set<String> set = new HashSet<String>();
						if(org_Area_ResourceByOrgId != null && !org_Area_ResourceByOrgId.isEmpty()){
							for(Map<String, String> oa:org_Area_ResourceByOrgId){
								set.add(oa.get("areaId"));
							}
							for(String oa:set){
								String areaId = oa;
								Map<String, String> reportResourceListService = this.networkResourceInterfaceService.getReportResourceListService(areaId, "BaseStation_GSM,BaseStation_TD,Cell");
								if(reportResourceListService != null){
									String string2 = reportResourceListService.get("BaseStation_GSM");
									baseStation_GSM = baseStation_GSM + Long.parseLong(string2);
									String string3 = reportResourceListService.get("BaseStation_TD");
									baseStation_TD = baseStation_TD + Long.parseLong(string3);
									String string4 = reportResourceListService.get("Cell");
									cell = cell + Long.parseLong(string4);
								}
							}
						}
						map.put("orgName", name);
						map.put("orgId", id+"");
						map.put("BaseStation_GSM", baseStation_GSM+"");
						map.put("BaseStation_TD", baseStation_TD+"");
						map.put("Cell", cell+"");
						map.put("pName", pName);
						list.add(map);
					}
				}
			}
		}
		return list;
	}
	
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getStationByAreaId(java.lang.String)
	 */
	public List<Map<String,String>> getStationByAreaId(String areaId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> resourceByReIdAndReTypeService = this.networkResourceInterfaceService.getResourceByReIdAndReTypeService(areaId, "Sys_Area");
		String pName = resourceByReIdAndReTypeService.get("name");
		List<Map<String,String>> areaByAccountService = this.networkResourceInterfaceService.getResourceService(areaId, "Sys_Area", "Sys_Area", "CHILD");
		Set<Map<String, String>> set = new HashSet<Map<String,String>>();
		if(areaByAccountService != null && !areaByAccountService.isEmpty()){
			for(Map<String,String> map : areaByAccountService){
				set.add(map);
			}
			for(Map<String, String> map:set){
				String aId = map.get("id");
				String areaName = map.get("name");
				Map<String, String> reportResourceListService = this.networkResourceInterfaceService.getReportResourceListService(aId, "Station,BaseStation_GSM,BaseStation_TD");
				reportResourceListService.put("areaId", aId);
				reportResourceListService.put("areaName", areaName);
				reportResourceListService.put("pName", pName);
				reportResourceListService.put("pId", areaId);
				list.add(reportResourceListService);
			}
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getStationByAreaId(java.lang.String)
	 */
	public List<Map<String,String>> getStationByAreaIdAndTopOrg(String userId,String areaId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		//Map<String, String> resourceByReIdAndReTypeService = this.networkResourceInterfaceService.getResourceByReIdAndReTypeService(areaId, "Sys_Area");
		List<Map<String,String>> areaByAccountService2 = this.networkResourceInterfaceService.getResourceService(areaId, "Sys_Area", "Sys_Area", "PARENT");
		Map<String, String> resourceByReIdAndReTypeService = null;
		if(areaByAccountService2 != null && !areaByAccountService2.isEmpty()){
			resourceByReIdAndReTypeService = areaByAccountService2.get(0);
		}
		if(resourceByReIdAndReTypeService != null){
			String aId = resourceByReIdAndReTypeService.get("id");
			list = getStationByAreaId(aId);
		}else{
			list = getStationByArea(userId);
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getWANByAreaId(java.lang.String)
	 */
	public List<Map<String,String>> getWANByAreaId(String areaId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> resourceByReIdAndReTypeService = this.networkResourceInterfaceService.getResourceByReIdAndReTypeService(areaId, "Sys_Area");
		String pName = resourceByReIdAndReTypeService.get("name");
		List<Map<String,String>> areaByAccountService = this.networkResourceInterfaceService.getResourceService(areaId, "Sys_Area", "Sys_Area", "CHILD");
		Set<Map<String, String>> set = new HashSet<Map<String,String>>();
		if(areaByAccountService != null && !areaByAccountService.isEmpty()){
			for(Map<String,String> map : areaByAccountService){
				set.add(map);
			}
			for(Map<String, String> map:set){
				String aId = map.get("id");
				String areaName = map.get("name");
				Map<String, String> reportResourceListService = this.networkResourceInterfaceService.getReportResourceListService(aId, "BaseStation_GSM,BaseStation_TD,Cell");
				reportResourceListService.put("areaId", aId);
				reportResourceListService.put("areaName", areaName);
				reportResourceListService.put("pName", pName);
				reportResourceListService.put("pId", areaId);
				list.add(reportResourceListService);
			}
		}
		return list;
	}
	
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getWANByAreaId(java.lang.String)
	 */
	public List<Map<String,String>> getWANByAreaIdAndTopOrg(String userId,String areaId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		//Map<String, String> resourceByReIdAndReTypeService = this.networkResourceInterfaceService.getResourceByReIdAndReTypeService(areaId, "Sys_Area");
		List<Map<String,String>> areaByAccountService2 = this.networkResourceInterfaceService.getResourceService(areaId, "Sys_Area", "Sys_Area", "PARENT");
		Map<String, String> resourceByReIdAndReTypeService = null;
		if(areaByAccountService2 != null && !areaByAccountService2.isEmpty()){
			resourceByReIdAndReTypeService = areaByAccountService2.get(0);
		}
		if(resourceByReIdAndReTypeService != null){
			String aId = resourceByReIdAndReTypeService.get("id");
			list = getWANByAreaId(aId);
		}else{
			list = getWANByArea(userId);
		}
		return list;
	}
	
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getStationByOrgId(long)
	 */
	public List<Map<String,String>> getStationByOrgId(long orgId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,Object> providerOrganization = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		if(providerOrganization !=null ){
			long oId = Long.parseLong((String) providerOrganization.get("id"));
			String pName = (String) providerOrganization.get("name");
			//List<ProviderOrganization> osrList = this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(oId,"BusinessOrganization");
			//yuan.yw
			List<Map<String,Object>> osrList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		
			if(osrList != null && !osrList.isEmpty()){
				for(Map<String,Object> p:osrList){
					String name = p.get("name")+"";
					long id = Long.valueOf(p.get("orgId")+"");
					Map<String, String> map = new HashMap<String, String>();
					long parseLong = 0;
					long baseStation_GSM = 0;
					long baseStation_TD = 0;
					//List<ProviderOrganization> providerOrgDownwardByOrgIdAndOrgAttrService = this.providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(p.getId(),"BusinessOrganization");
					List<Map<String,Object>> providerOrgDownwardByOrgIdAndOrgAttrService = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(id,"BusinessOrganization","downward");

					List<Map<String, String>> org_Area_ResourceByOrgId = new ArrayList<Map<String,String>>();
					if(providerOrgDownwardByOrgIdAndOrgAttrService != null){
						for(Map<String,Object> pr:providerOrgDownwardByOrgIdAndOrgAttrService){
							long id2 = Long.valueOf(pr.get("orgId")+"");
							List<Map<String,String>> findProjectResourceByOrgId = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(id2+"");
							org_Area_ResourceByOrgId.addAll(findProjectResourceByOrgId);
						}
					}
					//List<Map<String, String>> org_Area_ResourceByOrgId = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(p.getId()+"");
					//List<Org_Area_Resource> org_Area_ResourceByOrgId = this.providerOrganizationDao.getOrg_Area_ResourceByOrgId(p.getId()+"");
					Set<String> set = new HashSet<String>();
					if(org_Area_ResourceByOrgId != null && !org_Area_ResourceByOrgId.isEmpty()){
						for(Map<String, String> oa:org_Area_ResourceByOrgId){
							set.add(oa.get("areaId"));
						}
						for(String oa:set){
							String areaId = oa;
							//System.out.println(areaId);
							Map<String, String> reportResourceListService = this.networkResourceInterfaceService.getReportResourceListService(areaId, "Station,BaseStation_GSM,BaseStation_TD");
							if(reportResourceListService != null){
								String string = reportResourceListService.get("Station");
								if(parseLong != Long.parseLong(string)){
									parseLong = parseLong + Long.parseLong(string);
								}
								String string2 = reportResourceListService.get("BaseStation_GSM");
								 if(baseStation_GSM != Long.parseLong(string2)){
									 baseStation_GSM = baseStation_GSM + Long.parseLong(string2);
									}
								 String string3 = reportResourceListService.get("BaseStation_TD");
								 if(baseStation_TD != Long.parseLong(string3)){
									 baseStation_TD = baseStation_TD + Long.parseLong(string3);
									}
							}
						}
					}
					map.put("orgName", name);
					map.put("orgId", id+"");
					map.put("Station", parseLong+"");
					map.put("BaseStation_GSM", baseStation_GSM+"");
					map.put("BaseStation_TD", baseStation_TD+"");
					map.put("pName", pName);
					map.put("pId", orgId+"");
					list.add(map);
				}
			}
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getStationByOrgId(long)
	 */
	public List<Map<String,String>> getStationByOrgIdAndTopOrg(long orgId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		SysOrg providerOrganization = null;
		//List<ProviderOrganization> upProviderOrgByOrgIdService = this.providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> upProviderOrgByOrgIdService = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		if(upProviderOrgByOrgIdService != null && !upProviderOrgByOrgIdService.isEmpty()){
			providerOrganization = upProviderOrgByOrgIdService.get(0);
		}
		if(providerOrganization !=null ){
			long oId = providerOrganization.getOrgId();
			list = getStationByOrgId(oId);
		}
		return list;
	}
	
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getWANByOrgId(long)
	 */
	public List<Map<String,String>> getWANByOrgId(long orgId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,Object> providerOrganization = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		if(providerOrganization !=null ){
			long oId = Long.parseLong((String) providerOrganization.get("id"));
			String pName = (String) providerOrganization.get("name");
			//List<ProviderOrganization> osrList = this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(oId,"BusinessOrganization");
			//yuan.yw
			List<Map<String,Object>> osrList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		
			if(osrList != null && !osrList.isEmpty()){
				for(Map<String,Object> p:osrList){
					String name = p.get("name")+"";
					long id = Long.valueOf(p.get("orgId")+"");
					Map<String, String> map = new HashMap<String, String>();
					long baseStation_GSM = 0;
					long baseStation_TD = 0;
					long cell = 0;
					//List<ProviderOrganization> providerOrgDownwardByOrgIdAndOrgAttrService = this.providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(p.getId(),"BusinessOrganization");
					List<Map<String,Object>> providerOrgDownwardByOrgIdAndOrgAttrService = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(id,"BusinessOrganization","downward");

					List<Map<String, String>> org_Area_ResourceByOrgId = new ArrayList<Map<String,String>>();
					if(providerOrgDownwardByOrgIdAndOrgAttrService != null){
						for(Map<String,Object> pr:providerOrgDownwardByOrgIdAndOrgAttrService){
							long id2 = Long.valueOf(pr.get("orgId")+"");
							List<Map<String,String>> findProjectResourceByOrgId = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(id2+"");
							org_Area_ResourceByOrgId.addAll(findProjectResourceByOrgId);
						}
					}
					//List<Map<String, String>> org_Area_ResourceByOrgId = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(p.getId()+"");
					//List<Org_Area_Resource> org_Area_ResourceByOrgId = this.providerOrganizationDao.getOrg_Area_ResourceByOrgId(p.getId()+"");
					Set<String> set = new HashSet<String>();
					if(org_Area_ResourceByOrgId != null && !org_Area_ResourceByOrgId.isEmpty()){
						for(Map<String, String> oa:org_Area_ResourceByOrgId){
							set.add(oa.get("areaId"));
						}
						for(String oa:set){
							String areaId = oa;
							Map<String, String> reportResourceListService = this.networkResourceInterfaceService.getReportResourceListService(areaId, "BaseStation_GSM,BaseStation_TD,Cell");
							if(reportResourceListService != null){
								String string2 = reportResourceListService.get("BaseStation_GSM");
								 if(baseStation_GSM != Long.parseLong(string2)){
									 baseStation_GSM = baseStation_GSM + Long.parseLong(string2);
									}
								 String string3 = reportResourceListService.get("BaseStation_TD");
								 if(baseStation_TD != Long.parseLong(string3)){
									 baseStation_TD = baseStation_TD + Long.parseLong(string3);
									}
								 String string4 = reportResourceListService.get("Cell");
								 if(cell != Long.parseLong(string4)){
									 cell = cell + Long.parseLong(string4);
									}							 
							}
						}
					}
					map.put("orgName", name);
					map.put("orgId", id+"");
					map.put("BaseStation_GSM", baseStation_GSM+"");
					map.put("BaseStation_TD", baseStation_TD+"");
					map.put("Cell", cell+"");
					map.put("pName", pName);
					map.put("pId", orgId+"");
					list.add(map);
				}
			}
		}
		return list;
	}
	
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getWANByOrgId(long)
	 */
	public List<Map<String,String>> getWANByOrgIdAndTopOrg(long orgId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		SysOrg providerOrganization = null;
		//List<ProviderOrganization> upProviderOrgByOrgIdService = this.providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> upProviderOrgByOrgIdService = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");
		if(upProviderOrgByOrgIdService != null && !upProviderOrgByOrgIdService.isEmpty()){
			providerOrganization = upProviderOrgByOrgIdService.get(0);
		}
		if(providerOrganization !=null ){
			long oId = providerOrganization.getOrgId();
			list = getWANByOrgId(oId);
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getWANByOrgId(long)
	 */
	public List<Map<String,String>> getStationByOrgIdProject(long orgId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			list = getStationByOrgIdAndProject(orgId);
		return list;
	}





	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getStationByOrgId(long)
	 */
	public List<Map<String,String>> getStationByOrgIdAndProject(long orgId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,Object> providerOrganization = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		if(providerOrganization !=null ){
//			if(providerOrganization.get("id") != null && !providerOrganization.get("id").equals("") && !providerOrganization.get("id").equals("null")){
//				long oId = Long.parseLong(providerOrganization.get("id")+"");
//			}
			String pName = (String) providerOrganization.get("name");
			//List<Map<String, String>> osrList = organizationService.getProjectToDownwardOrgByOrgIdService(orgId);
			//yuan.yw
			List<Map<String, String>> osrList = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(orgId);
			if(osrList != null && !osrList.isEmpty()){
				for(Map<String, String> p:osrList){
					String name = p.get("NAME");
					String id = p.get("id");
					Map<String, String> map = new HashMap<String, String>();
					long parseLong = 0;
					long baseStation_GSM = 0;
					long baseStation_TD = 0;
					//List<Map<String, String>> providerOrgDownwardByOrgIdAndOrgAttrService = organizationService.getOrgByProjectIdService(id);
					//yuan.yw
					List<Map<String, String>> providerOrgDownwardByOrgIdAndOrgAttrService = this.sysOrganizationService.getOrgByProjectIdService(id);
					List<Map<String, String>> org_Area_ResourceByOrgId = new ArrayList<Map<String, String>>();
					if(providerOrgDownwardByOrgIdAndOrgAttrService != null && !providerOrgDownwardByOrgIdAndOrgAttrService.isEmpty()){
						for(Map<String, String> m:providerOrgDownwardByOrgIdAndOrgAttrService){

//							Long valueOf = Long.valueOf(m.get("id"));
//							List<ProviderOrganization> providerOrgDownwardByOrgIdAndOrgAttrService1 = this.providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(valueOf,"BusinessOrganization");
//							if(providerOrgDownwardByOrgIdAndOrgAttrService != null){
//								for(ProviderOrganization pr:providerOrgDownwardByOrgIdAndOrgAttrService1){
//									long id2 = pr.getId();
//									List<Map<String,String>> findProjectResourceByOrgId = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(id2+"");
//									org_Area_ResourceByOrgId.addAll(findProjectResourceByOrgId);
//								}
//							}
							List<Map<String, String>> org_Area_ResourceByOrgId1 = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(m.get("id"));
							if(org_Area_ResourceByOrgId1 != null && !org_Area_ResourceByOrgId1.isEmpty()){
								org_Area_ResourceByOrgId.addAll(org_Area_ResourceByOrgId1);
							}
						}
					}
					Set<String> set = new HashSet<String>();
					if(org_Area_ResourceByOrgId != null && !org_Area_ResourceByOrgId.isEmpty()){
						for(Map<String, String> oa:org_Area_ResourceByOrgId){
							set.add(oa.get("areaId"));
						}
						for(String oa:set){
							String areaId = oa;
							//System.out.println(areaId);
							Map<String, String> reportResourceListService = this.networkResourceInterfaceService.getReportResourceListService(areaId, "Station,BaseStation_GSM,BaseStation_TD");
							if(reportResourceListService != null){
								String string = reportResourceListService.get("Station");
								if(parseLong != Long.parseLong(string)){
									parseLong = parseLong + Long.parseLong(string);
								}
								String string2 = reportResourceListService.get("BaseStation_GSM");
								 if(baseStation_GSM != Long.parseLong(string2)){
									 baseStation_GSM = baseStation_GSM + Long.parseLong(string2);
									}
								 String string3 = reportResourceListService.get("BaseStation_TD");
								 if(baseStation_TD != Long.parseLong(string3)){
									 baseStation_TD = baseStation_TD + Long.parseLong(string3);
									}
							}
						}
					}
					map.put("orgName", name);
					map.put("orgId", id+"");
					map.put("Station", parseLong+"");
					map.put("BaseStation_GSM", baseStation_GSM+"");
					map.put("BaseStation_TD", baseStation_TD+"");
					map.put("pName", pName);
					map.put("pId", orgId+"");
					list.add(map);
				}
			}
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.iscreate.op.service.report.NetReportService#getWANByOrgId(long)
	 */
	public List<Map<String,String>> getWANByOrgIdProject(long orgId){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			list = getWANByOrgIdAndProject(orgId);
		return list;
	}
	
	
	
	
	
	
	/* (non-Javadoc)
	* @see com.iscreate.op.service.report.NetReportService#getStationByOrgId(long)
	*/
	public List<Map<String,String>> getWANByOrgIdAndProject(long orgId){
	List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	Map<String,Object> providerOrganization = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
	
	if(providerOrganization !=null ){
		long oId = Long.parseLong((String) providerOrganization.get("id"));
		String pName = (String) providerOrganization.get("name");
		//List<Map<String, String>> osrList = organizationService.getProjectToDownwardOrgByOrgIdService(orgId);
		//yuan.yw
		List<Map<String, String>> osrList = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(orgId);
		if(osrList != null && !osrList.isEmpty()){
			for(Map<String, String> p:osrList){
				String name = p.get("NAME");
				String id = p.get("id");
				Map<String, String> map = new HashMap<String, String>();
				long parseLong = 0;
				long baseStation_GSM = 0;
				long baseStation_TD = 0;
				long cell = 0;
				//List<Map<String, String>> providerOrgDownwardByOrgIdAndOrgAttrService = organizationService.getOrgByProjectIdService(id);
				//yuan.yw
				List<Map<String, String>> providerOrgDownwardByOrgIdAndOrgAttrService = this.sysOrganizationService.getOrgByProjectIdService(id);
				List<Map<String, String>> org_Area_ResourceByOrgId = new ArrayList<Map<String, String>>();
				if(providerOrgDownwardByOrgIdAndOrgAttrService != null && !providerOrgDownwardByOrgIdAndOrgAttrService.isEmpty()){
					for(Map<String, String> m:providerOrgDownwardByOrgIdAndOrgAttrService){
//						Long valueOf = Long.valueOf(m.get("id"));
//						List<ProviderOrganization> providerOrgDownwardByOrgIdAndOrgAttrService1 = this.providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(valueOf,"BusinessOrganization");
//						if(providerOrgDownwardByOrgIdAndOrgAttrService != null){
//							for(ProviderOrganization pr:providerOrgDownwardByOrgIdAndOrgAttrService1){
//								long id2 = pr.getId();
//								List<Map<String,String>> findProjectResourceByOrgId = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(id2+"");
//								org_Area_ResourceByOrgId.addAll(findProjectResourceByOrgId);
//							}
//						}
						List<Map<String, String>> org_Area_ResourceByOrgId1 = this.informationManageNetworkResourceDao.findProjectResourceByOrgId(m.get("id"));
						if(org_Area_ResourceByOrgId1 != null && !org_Area_ResourceByOrgId1.isEmpty()){
							org_Area_ResourceByOrgId.addAll(org_Area_ResourceByOrgId1);
						}
					}
				}
				Set<String> set = new HashSet<String>();
				if(org_Area_ResourceByOrgId != null && !org_Area_ResourceByOrgId.isEmpty()){
					for(Map<String, String> oa:org_Area_ResourceByOrgId){
						set.add(oa.get("areaId"));
					}
					for(String oa:set){
						String areaId = oa;
						Map<String, String> reportResourceListService = this.networkResourceInterfaceService.getReportResourceListService(areaId, "BaseStation_GSM,BaseStation_TD,Cell");
						if(reportResourceListService != null){
							String string2 = reportResourceListService.get("BaseStation_GSM");
							 if(baseStation_GSM != Long.parseLong(string2)){
								 baseStation_GSM = baseStation_GSM + Long.parseLong(string2);
								}
							 String string3 = reportResourceListService.get("BaseStation_TD");
							 if(baseStation_TD != Long.parseLong(string3)){
								 baseStation_TD = baseStation_TD + Long.parseLong(string3);
								}
							 String string4 = reportResourceListService.get("Cell");
							 if(cell != Long.parseLong(string4)){
								 cell = cell + Long.parseLong(string4);
								}							 
						}
					}
				}
							map.put("orgName", name);
							map.put("orgId", id+"");
							map.put("BaseStation_GSM", baseStation_GSM+"");
							map.put("BaseStation_TD", baseStation_TD+"");
							map.put("Cell", cell+"");
							map.put("pName", pName);
							map.put("pId", orgId+"");
							list.add(map);
						}
					}
				}
				return list;
	}
}
