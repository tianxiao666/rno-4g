package com.iscreate.op.service.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iscreate.op.constant.BizAuthorityConstant;
import com.iscreate.op.constant.CompareGisDispatchReport;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.cardispatch.CardispatchCarDao;
import com.iscreate.op.dao.cardispatch.CardispatchDriverDao;
import com.iscreate.op.dao.cardispatch.CardispatchTerminalDao;
import com.iscreate.op.dao.informationmanage.InformationManageNetworkResourceDao;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.networkresource.common.dao.NetworkResourceManageDao;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.tools.TimeFormatHelper;

public class GisDispatchReportServiceImpl implements GisDispatchReportService {
	
	private CardispatchCarDao cardispatchCarDao;
	
	private NetworkResourceInterfaceService networkResourceInterfaceService;
	
	private WorkManageService workManageService;
	
	private CardispatchDriverDao cardispatchDriverDao;
	
	private CardispatchTerminalDao cardispatchTerminalDao;
	
	
	private SysOrgUserService sysOrgUserService;

	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	
	private InformationManageNetworkResourceDao informationManageNetworkResourceDao;
	
	private NetworkResourceManageDao networkResourceManageDao;
	
	

	public NetworkResourceManageDao getNetworkResourceManageDao() {
		return networkResourceManageDao;
	}

	public void setNetworkResourceManageDao(
			NetworkResourceManageDao networkResourceManageDao) {
		this.networkResourceManageDao = networkResourceManageDao;
	}

	public InformationManageNetworkResourceDao getInformationManageNetworkResourceDao() {
		return informationManageNetworkResourceDao;
	}

	public void setInformationManageNetworkResourceDao(
			InformationManageNetworkResourceDao informationManageNetworkResourceDao) {
		this.informationManageNetworkResourceDao = informationManageNetworkResourceDao;
	}

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}

	public CardispatchDriverDao getCardispatchDriverDao() {
		return cardispatchDriverDao;
	}


	public void setCardispatchDriverDao(CardispatchDriverDao cardispatchDriverDao) {
		this.cardispatchDriverDao = cardispatchDriverDao;
	}

	public CardispatchTerminalDao getCardispatchTerminalDao() {
		return cardispatchTerminalDao;
	}


	public void setCardispatchTerminalDao(
			CardispatchTerminalDao cardispatchTerminalDao) {
		this.cardispatchTerminalDao = cardispatchTerminalDao;
	}


	/**
	 * 根据userId，获取站址，车辆，人员的数量统计
	* @date Nov 8, 20129:57:39 AM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String,String>> getResouceCountList(String userId){
		
		
		List<Map<String,String>> resultMap=new ArrayList<Map<String,String>>();
		
		//获取公司所有的区域
		//List<ProviderOrganization> topLevelOrgByAccount = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		
		long orgId = 0;
		if(topLevelOrgByAccount != null && topLevelOrgByAccount.size() > 0){
			SysOrg providerOrganization = topLevelOrgByAccount.get(0);
			orgId = providerOrganization.getOrgId();
		}
		List<Map<String,Object>> areaList = null;
		//areaList = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		areaList = this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		
		if(areaList!=null && !areaList.isEmpty()){
			
			for(int i=0;i<areaList.size();i++){
				
				Map<String,Object> bizArea=areaList.get(i);
				Map<String,String> areaMap=new HashMap<String,String>();
				
				
				//---------获取人员----------------
				Map<String,String> allStaffs=new HashMap<String,String>();
				if(bizArea!=null ){
					//System.out.println(bizArea.getId());
					//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(bizArea.getId(),"BusinessOrganization");
					//List<ProviderOrganization> orgListDownwardByOrg = gisDaiFactory.getOrganizationService().getOrgListDownwardByOrg(bizArea.getId());
					List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(Long.valueOf(bizArea.get("orgId")+""),"BusinessOrganization","downward");

					if(orgListDownwardByOrg != null && orgListDownwardByOrg.size() > 0){
						for(Map<String,Object> bi:orgListDownwardByOrg){
							long oId = Long.valueOf(bi.get("orgId")+"");
						//ou.jh
						List<Map<String,Object>> userByOrgId = this.sysOrgUserService.getUserByOrgId(oId);
//						List<Staff> accounts = providerOrganizationService.getStaffListByOrgIdService(oId);
						if (userByOrgId != null && userByOrgId.size() > 0) {
							for (Map<String,Object> account : userByOrgId) {
								if (!allStaffs.containsKey(account
										.get("account"))) {
									allStaffs.put(account.get("account")+"", account.get("name")+"");
								}
							}
						}
					}
				}
				
				areaMap.put("human", String.valueOf(allStaffs.size()));
//				System.out.println("人员大小==="+allStaffs.size());
				
				
				
				//---------获取车辆----------------
				
				//获取车辆
				Map<String,String> allCars=new HashMap<String,String>();
				if(orgListDownwardByOrg != null && orgListDownwardByOrg.size() > 0){
					for(Map<String,Object> bi:orgListDownwardByOrg){
						String orgIdString = bi.get("orgId")+"";
						List<Map<String,Object>> resourceCarList = cardispatchCarDao.findCarListByBizId(orgIdString);
						if(resourceCarList!=null && !resourceCarList.isEmpty()){
							for(Map<String,Object> car:resourceCarList){
								if(car != null){
									String carId = car.get("carId").toString();
									if (!allCars.containsKey(carId)) {
										allCars.put(carId, carId);
									}
								}
							}
							}
						}
					}
				areaMap.put("car", String.valueOf(allCars.size()));
//				System.out.println("车辆大小==="+allCars.size());
				
				//-----------------获取站址------------------------
//				Set<Map<String,String>> set = new HashSet<Map<String,String>>();
//				System.out.println("基站======"+new Date());
//				if(orgListDownwardByOrg != null && orgListDownwardByOrg.size() > 0){
//					for(ProviderOrganization bi:orgListDownwardByOrg){
//						List<Map<String, String>> stationByOrgIdList = networkResourceInterfaceService.getStationByOrgIdService(bi.getId());
//						if(stationByOrgIdList!=null && !stationByOrgIdList.isEmpty()){
//							for(Map<String, String> ba:stationByOrgIdList){
//								set.add(ba);
//							}
//						}
//					}
//				}
//				areaMap.put("station", set.size()+"");
//				

				
				//-----------------获取基站------------------------

				Set<String> set = new HashSet<String>();
				Set<String> set1 = new HashSet<String>();
				if(orgListDownwardByOrg != null && orgListDownwardByOrg.size() > 0){
					for(Map<String,Object> bi:orgListDownwardByOrg){
						//List<Map<String, String>> stationByOrgIdList = networkResourceInterfaceService.getStationByOrgIdService(bi.getId());
						List<Long> areaIds = new ArrayList<Long>();
							List<Map<String,String>> oarList = this.informationManageNetworkResourceDao.findProjectResourceByProjectOrgIdResourceType(null,bi.get("orgId")+"","GeneralBaseStation");
							if(oarList != null && oarList.size() > 0){
								for(Map<String,String> m1:oarList){
									areaIds.add(Long.parseLong(m1.get("areaId")+""));
								}
							}
//						Set<Map<String, Object>> baseStationSet = new HashSet<Map<String,Object>>();
//						List<Map<String,Object>> baseStationSet = this.networkResourceInterfaceService.getBaseStationByAreas(areaIds, "GeneralBaseStation");
						List<BasicEntity> baseStationReportByResourceType = this.networkResourceManageDao.getGPSReportReportByResourceType(areaIds);
						if(baseStationReportByResourceType != null && !baseStationReportByResourceType.isEmpty()){
							for(BasicEntity b :baseStationReportByResourceType){
								set1.add(b.getValue("baseStationId")+""+b.getValue("baseStationType")+"");
								set.add(b.getValue("stationId")+"");
							}
						}
						
					}
				}
				areaMap.put("station", set.size()+"");
				areaMap.put("BaseStation", set1.size()+"");
				
				//---获取组织工单数
				Set<Map> setWork = new HashSet<Map>();
				if(orgListDownwardByOrg != null && orgListDownwardByOrg.size() > 0){
					String orgIds = "";
					for(Map<String,Object> bi:orgListDownwardByOrg){
						orgIds = orgIds + bi.get("orgId") + ",";
					}
					if(orgIds != null && !orgIds.equals("")){
						orgIds = orgIds.substring(0, orgIds.length()-1);
					}
						List<Map> commonQuery = workManageService.commonQuery("workOrder", " and 1 = 1 and \"creatorOrgId\" in ("+orgIds+")");
						//List<Map<String,String>> netResourceByOrgIdAndResourceType = networkResourceInterfaceService.getNetResourceByOrgIdAndResourceType(orgId, "GeneralBaseStation");
						if(commonQuery!=null && !commonQuery.isEmpty()){
								setWork.addAll(commonQuery);
						}
					areaMap.put("workorder", setWork.size()+"");
				}
				}
				String areaName=bizArea.get("name")+"";
				areaMap.put("areaName", areaName);
				
				String areaId=bizArea.get("orgId")+"";
				areaMap.put("areaId", areaId);
				
				areaMap.put("types", "3");
				
				resultMap.add(areaMap);
			}
			
		}
		return resultMap;
	}

	
	/**
	 * 根据组织ID获取组织下人员的任务数
	* @date Nov 8, 201210:43:28 AM
	* @Description: TODO 
	* @param @param bizunitInstanceId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getPeakStatisticsByStaff(String userId){
		//获取公司所有的区域
		//List<ProviderOrganization> topLevelOrgByAccount = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

		long orgId = 0;
		if(topLevelOrgByAccount != null && topLevelOrgByAccount.size() > 0){
			SysOrg providerOrganization = topLevelOrgByAccount.get(0);
			orgId = providerOrganization.getOrgId();
		}
		List<Map<String,Object>> areaList = null;
		List<Map<String, Object>> rList = new ArrayList<Map<String,Object>>();
		//areaList = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		areaList= this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(orgId,"BusinessOrganization","downward");

		if(areaList != null && areaList.size() > 0){
			for(Map<String,Object> pr:areaList){
				//ou.jh
				List<Map<String,Object>> userByOrgIdAndRoleCode = this.sysOrgUserService.getUserByOrgIdAndRoleCode(Long.valueOf(pr.get("orgId")+""), BizAuthorityConstant.ROLE_TEAMMEMBER);
//				List<Staff> staffByOrgIdAndRoleCodeService = providerOrganizationService.getStaffByOrgIdAndRoleCodeService(pr.getId(), BizAuthorityConstant.ROLE_TEAMMEMBER);
				if(userByOrgIdAndRoleCode != null && userByOrgIdAndRoleCode.size() > 0){
					for(Map<String,Object> staff:userByOrgIdAndRoleCode){
						String staffName = staff.get("name")+"";
						String account = staff.get("account")+"";
						long workOrderCountByResourceTypeAndResourceId = workManageService.getTaskOrderCountByObjectTypeAndObjectId("people", account);
						
						
						Date nowDate=new Date();
						String str_nowDate = TimeFormatHelper.getTimeFormatByFree(nowDate, "yyyy-MM-dd HH:mm:ss");
						
						String conditionString = " and \"currentHandler\"='"+account+"' " +
								" and \"requireCompleteTime\" < to_date('"+str_nowDate+"','yyyy-mm-dd hh24:mi:ss') " +
										" and \"status\" <> "+WorkManageConstant.TASKORDER_CLOSED;
						Map<String, Object> userTaskOrders = workManageService.getUserTaskOrders(account, "all", "all", null, null, conditionString);
						long hasTimedOutTasks = 0;
						if(userTaskOrders != null){
							hasTimedOutTasks = Long.valueOf(userTaskOrders.get("count")+"");
						}
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("staffName", staffName);
						map.put("staffId", account);
						map.put("unfinishedTasks", workOrderCountByResourceTypeAndResourceId);
						map.put("hasTimedOutTasks", hasTimedOutTasks);
						map.put("orgName", pr.get("name"));
						rList.add(map);
					}
				}
			}
		}
		if(rList != null){
			Collections.sort(rList, new CompareGisDispatchReport());
		}
		return rList;
	}
	
	
	/**
	 * 根据组织ID获取组织下车辆的任务数
	* @date Nov 8, 201210:43:28 AM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getPeakStatisticsByCar(String userId){
		//获取公司所有的区域
		//List<ProviderOrganization> topLevelOrgByAccount = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

		long orgId = 0;
		if(topLevelOrgByAccount != null && topLevelOrgByAccount.size() > 0){
			SysOrg providerOrganization = topLevelOrgByAccount.get(0);
			orgId = providerOrganization.getOrgId();
		}
		List<Map<String,Object>> areaList = null;
		List<Map<String, Object>> rList = new ArrayList<Map<String,Object>>();
		//areaList = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		areaList= this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(orgId,"BusinessOrganization","downward");

		if(areaList != null && areaList.size() > 0){
			for(Map<String,Object> pr:areaList){
				String orgIdString = pr.get("orgId")+"";
				List<Map<String,Object>> resourceCarList = cardispatchCarDao.findCarListByBizId(orgIdString);
				if(resourceCarList != null && resourceCarList.size() > 0){
					for(Map<String,Object> m:resourceCarList){
						if(m.get("carId") != null && !m.get("carId").equals("")){
							Map<String, Object> param_map = new HashMap<String, Object>();
							param_map.put("carId", m.get("carId"));
							List<Map<String,Object>> findDriverList = cardispatchDriverDao.findDriverList(param_map);
							String driverName = "";
							String driverPhone = "";
							if(findDriverList != null && findDriverList.size() > 0){
								if(findDriverList.get(0) != null && !findDriverList.get(0).isEmpty()){
									driverName = findDriverList.get(0).get("driverName")+"";
									driverPhone = findDriverList.get(0).get("driverPhone")+"";
								}
							}
							long workOrderCountByResourceTypeAndResourceId = workManageService.getWorkOrderCountByResourceTypeAndResourceId("car", m.get("carId").toString());
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("carNumber", m.get("carNumber"));
							map.put("driverName", driverName);
							map.put("id", m.get("carId"));
							map.put("driverPhone", driverPhone);
							map.put("unfinishedTasks", workOrderCountByResourceTypeAndResourceId);
							map.put("orgName", pr.get("name"));
							//------获取车辆终端状态
							List<Map<String, Object>> terminalList = cardispatchTerminalDao.findTerminalList(param_map);
							if(terminalList!=null && !terminalList.isEmpty()){
								String carState = null;
								Map<String, Object> tMap = terminalList.get(0);
								carState = tMap.get("terminalState")+"";
								map.put("carState", carState);
							}
							rList.add(map);
						}
					}
				}
			}
		}
		if(rList != null){
			Collections.sort(rList, new CompareGisDispatchReport());
		}
		return rList;
	}
	
	
	
	/**
	 * 根据组织ID获取组织下基站的任务数
	* @date Nov 8, 201210:43:28 AM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getPeakStatisticsByBaseStation(String userId){
		//获取公司所有的区域
		//List<ProviderOrganization> topLevelOrgByAccount = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

		long orgId = 0;
		if(topLevelOrgByAccount != null && topLevelOrgByAccount.size() > 0){
			SysOrg providerOrganization = topLevelOrgByAccount.get(0);
			orgId = providerOrganization.getOrgId();
		}
		List<Map<String,Object>> areaList = null;
		List<Map<String, Object>> rList = new ArrayList<Map<String,Object>>();
		//areaList = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		areaList= this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(orgId,"BusinessOrganization","downward");
		if(areaList != null && areaList.size() > 0){
			List<Long> areaIds = new ArrayList<Long>();
			for(Map<String, Object> m : areaList){
				List<Map<String,String>> oarList = this.informationManageNetworkResourceDao.findProjectResourceByProjectOrgIdResourceType(null,m.get("orgId")+"","GeneralBaseStation");
				if(oarList != null && oarList.size() > 0){
					for(Map<String,String> m1:oarList){
						areaIds.add(Long.parseLong(m1.get("areaId")+""));
					}
				}
			}
//			Set<Map<String, Object>> baseStationSet = new HashSet<Map<String,Object>>();
//			List<Map<String,Object>> baseStationSet = this.networkResourceInterfaceService.getBaseStationByAreas(areaIds, "GeneralBaseStation");
			List<BasicEntity> baseStationReportByResourceType = this.networkResourceManageDao.getGPSBaseStationReportByResourceType(areaIds);
			if(baseStationReportByResourceType != null && !baseStationReportByResourceType.isEmpty()){
				for(BasicEntity b :baseStationReportByResourceType){
					Map<String, Object> map = b.toMap();
					//基站没有超时任务时设置为0
					if(map.get("routineCount") == null){
						map.put("routineCount", 0);
					}
					//基站没有未完成任务时设置为0
					if(map.get("unfinishedTasks") == null){
						map.put("unfinishedTasks", 0);
					}
					rList.add(map);
				}
			}
		}
		if(rList != null){
			Collections.sort(rList, new CompareGisDispatchReport());
		}
		//System.out.println(rSet.size());
		return rList;
	}
	
	

	public CardispatchCarDao getCardispatchCarDao() {
		return cardispatchCarDao;
	}

	public void setCardispatchCarDao(CardispatchCarDao cardispatchCarDao) {
		this.cardispatchCarDao = cardispatchCarDao;
	}

	public NetworkResourceInterfaceService getNetworkResourceInterfaceService() {
		return networkResourceInterfaceService;
	}

	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}

	public WorkManageService getWorkManageService() {
		return workManageService;
	}

	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}
}
