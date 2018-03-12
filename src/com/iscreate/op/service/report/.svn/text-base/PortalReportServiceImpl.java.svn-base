package com.iscreate.op.service.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.publicinterface.WorkOrderCommonService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.plat.tools.TimeFormatHelper;

public class PortalReportServiceImpl implements PortalReportService{
	
	
	
	private WorkOrderCommonService workOrderCommonService;

	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	
	

	public WorkOrderCommonService getWorkOrderCommonService() {
		return workOrderCommonService;
	}

	public void setWorkOrderCommonService(
			WorkOrderCommonService workOrderCommonService) {
		this.workOrderCommonService = workOrderCommonService;
	}
	
	
	
	
	

	/**
	 * 根据受理专业获取故障抢修单
	* @date Nov 2, 20125:01:05 PM
	* @Description: TODO 
	* @param @param userId
	* @param @param startTime
	* @param @param endTime
	* @param @return        
	* @throws
	 */
	public Map<String,Object> getUrgentRepairReportOrderCountByAcceptProfessional(String userId,String startTime ,String endTime){
		List<Map> mapList = new ArrayList<Map>();
		String bizName = "";
		Map<String,Object> returnMap = new HashMap<String, Object>();
		Map<String,Integer> rMap = new HashMap<String, Integer>();
		//List<ProviderOrganization> topLevelOrgByAccount = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

		SysOrg providerOrganization = null;
		if(topLevelOrgByAccount != null && topLevelOrgByAccount.size() > 0){
			providerOrganization = topLevelOrgByAccount.get(0);
			bizName = providerOrganization.getName();
		}
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(providerOrganization.getOrgId(),"BusinessOrganization");
		List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(providerOrganization.getOrgId(),"BusinessOrganization","downward");

		List<String> listString = null;
		if(orgListDownwardByOrg != null && orgListDownwardByOrg.size() > 0){
			listString = new ArrayList<String>();
			for(Map<String,Object> pr:orgListDownwardByOrg){
				listString.add(pr.get("orgId")+"");
				//接口不完整测试使用数据
//				List<Map> workOrderListByAccountAndAcceptProfessional = new ArrayList<Map>();
//				Map<String, Object> m1 = new HashMap<String, Object>();
//				m1.put("acceptProfessional", "a");
//				m1.put("acceptProfessional", "b");
//				m1.put("acceptProfessional", "c");
//				m1.put("acceptProfessional", "d");
//				m1.put("acceptProfessional", "");
//				workOrderListByAccountAndAcceptProfessional.add(m1);
//				
			}
			List<Map> workOrderListByAccountAndAcceptProfessional = workOrderCommonService.getWorkOrderListByOrgIdsService(listString,  startTime, endTime);
			
			mapList.addAll(workOrderListByAccountAndAcceptProfessional);
		}
		if(mapList != null && mapList.size() > 0){
			for(Map map:mapList){
				if(map.get("acceptProfessional") != null && !map.get("acceptProfessional").equals("")){
					if(rMap.get(map.get("acceptProfessional").toString()) == null){
						rMap.put(map.get("acceptProfessional").toString(), 1);
					}else{
						Integer integer = rMap.get(map.get("acceptProfessional").toString());
						integer++;
						rMap.put(map.get("acceptProfessional").toString(), integer);
					}
				}else{
					if(rMap.get("空值") == null){
						rMap.put("空值", 1);
					}else{
						Integer integer = rMap.get("空值");
						integer++;
						rMap.put("空值", integer);
					}
				}
			}
		}
		returnMap.put("value", rMap);
		returnMap.put("orgName", bizName);
		return returnMap;
	}
	
	
	/**
	 * 根据时间与组织统计
	* @date Nov 2, 20125:01:12 PM
	* @Description: TODO 
	* @param @param userId
	* @param @param startTime
	* @param @param endTime
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getOrderCountByWotemplate(String userId,String startTime ,String endTime){
		String[] startTimes = null;
		if(startTime != null){
			startTimes = startTime.split(",");
		}
		String[] endTimes = null;
		if(endTime != null){
			endTimes = endTime.split(",");
		}
		Map<String, Object> reutrnMap = new HashMap<String, Object>();
		List<Map> rList = new ArrayList<Map>();
		String bizName = "";
		//List<ProviderOrganization> topLevelOrgByAccount = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

		SysOrg providerOrganization = null;
		long userorgId = 0;
		if(topLevelOrgByAccount != null && topLevelOrgByAccount.size() > 0){
			providerOrganization = topLevelOrgByAccount.get(0);
			bizName = providerOrganization.getName();
			userorgId = providerOrganization.getOrgId();
		}
		if(startTimes != null && startTimes.length > 0 && endTimes != null && endTimes.length > 0){
			for(int i = 0;i < startTimes.length;i++){
				List<Map> mapList = new ArrayList<Map>();
				//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(userorgId,"BusinessOrganization");
				List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(userorgId,"BusinessOrganization","downward");

				List<String> listString = null;
				if(orgListDownwardByOrg != null && orgListDownwardByOrg.size() > 0){
					listString = new ArrayList<String>();
					for(Map<String,Object> pr:orgListDownwardByOrg){
						listString.add(pr.get("orgId")+"");
							//List<Map> commonQueryList = workManageService.commonQuery("workOrder", "and 1=1 createTime >= "+startTime+" and createTime <="+endTime+" and creatorOrgId="+pr.getId());
							//测试数据
//							List<Map> commonQueryList = new ArrayList<Map>();
//							Map<String, Object> m1 = new HashMap<String, Object>();
//							m1.put("faultOccuredTime", "2012-07-10 00:00:00");
//							m1.put("alarmClearTime", "2012-07-10 12:00:00");
//							commonQueryList.add(m1);
//							Map<String, Object> m2 = new HashMap<String, Object>();
//							m2.put("faultOccuredTime", "2012-07-15 00:00:00");
//							m2.put("alarmClearTime", "2012-07-16 12:00:00");
//							commonQueryList.add(m2);
//							mapList.addAll(commonQueryList);
					}
					List<Map> workOrderListByAccountAndAcceptProfessional = workOrderCommonService.getWorkOrderListByOrgIdsService(listString, startTimes[i], endTimes[i]);
					mapList.addAll(workOrderListByAccountAndAcceptProfessional);
				}
				long count = 0;
				int workOrderCount = 0;
				Map map = new HashMap();
				if(mapList != null && mapList.size() > 0){
					workOrderCount = mapList.size();
					for(Map m:mapList){
						if(m.get("alarmClearTime") != null && m.get("faultOccuredTime") != null){
							try
							{
								//System.out.println(m.get("alarmClearTime")+"=="+m.get("faultOccuredTime"));
								Date d1 = TimeFormatHelper.setTimeFormat(m.get("faultOccuredTime").toString());
								Date d2 = TimeFormatHelper.setTimeFormat(m.get("alarmClearTime").toString());
//								System.out.println("faultOccuredTime=="+m.get("faultOccuredTime").toString());
//								System.out.println("alarmClearTime=="+m.get("alarmClearTime").toString());
//								Date d1 = df.parse(be.getValue("alarmClearTime").toString());
//								Date d2 = df.parse(be.getValue("faultOccuredTime").toString());
								//System.out.println("d1=="+d1+"d2=="+d2);
								long diff = d2.getTime() - d1.getTime();
								long hour = diff / (1000 * 60 * 60);
								//System.out.println(hour);
								count = count + hour;
							}
							catch (Exception e)
							{
							}
						}
					}
					count = count/workOrderCount;
				}
				map.put("time", startTimes[i]);
				map.put("count", count);
				map.put("workOrderCount", workOrderCount);
				rList.add(map);
			}
		}
		reutrnMap.put("bizName", bizName);
		reutrnMap.put("value", rList);
		return reutrnMap;
	}
	

	public Map<String, Object> getReportByOrgId(long orgId,String startTime,String endTime){
		String[] startTimes = null;
		if(startTime != null){
			startTimes = startTime.split(",");
		}
		String[] endTimes = null;
		if(endTime != null){
			endTimes = endTime.split(",");
		}
		Map<String, Object> reutrnMap = new HashMap<String, Object>();
		List<Map> rList = new ArrayList<Map>();
		if(startTimes != null && startTimes.length > 0 && endTimes != null && endTimes.length > 0){
			for(int i = 0;i < startTimes.length;i++){
				List<Map> mapList = new ArrayList<Map>();
				//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
				List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(orgId,"BusinessOrganization","downward");

				List<String> listString = null;
				if(orgListDownwardByOrg != null && orgListDownwardByOrg.size() > 0){
					listString = new ArrayList<String>();
					for(Map<String,Object> pr:orgListDownwardByOrg){
						listString.add(pr.get("orgId")+"");
							//List<Map> commonQueryList = workManageService.commonQuery("workOrder", "and 1=1 createTime >= "+startTime+" and createTime <="+endTime+" and creatorOrgId="+pr.getId());
							//测试数据
//							List<Map> commonQueryList = new ArrayList<Map>();
//							Map<String, Object> m1 = new HashMap<String, Object>();
//							m1.put("faultOccuredTime", "2012-07-10 00:00:00");
//							m1.put("alarmClearTime", "2012-07-10 12:00:00");
//							commonQueryList.add(m1);
//							Map<String, Object> m2 = new HashMap<String, Object>();
//							m2.put("faultOccuredTime", "2012-07-15 00:00:00");
//							m2.put("alarmClearTime", "2012-07-16 12:00:00");
//							commonQueryList.add(m2);
//							mapList.addAll(commonQueryList);
					}
					List<Map> workOrderListByAccountAndAcceptProfessional = workOrderCommonService.getWorkOrderListByOrgIdsService(listString, startTimes[i], endTimes[i]);
					mapList.addAll(workOrderListByAccountAndAcceptProfessional);
				}
				long count = 0;
				int workOrderCount = 0;
				Map map = new HashMap();
				if(mapList != null && mapList.size() > 0){
					workOrderCount = mapList.size();
					for(Map m:mapList){
						if(m.get("alarmClearTime") != null && m.get("faultOccuredTime") != null){
							try
							{
								//System.out.println(m.get("alarmClearTime")+"=="+m.get("faultOccuredTime"));
								Date d1 = TimeFormatHelper.setTimeFormat(m.get("faultOccuredTime").toString());
								Date d2 = TimeFormatHelper.setTimeFormat(m.get("alarmClearTime").toString());
//								System.out.println("faultOccuredTime=="+m.get("faultOccuredTime").toString());
//								System.out.println("alarmClearTime=="+m.get("alarmClearTime").toString());
//								Date d1 = df.parse(be.getValue("alarmClearTime").toString());
//								Date d2 = df.parse(be.getValue("faultOccuredTime").toString());
								//System.out.println("d1=="+d1+"d2=="+d2);
								long diff = d2.getTime() - d1.getTime();
								long hour = diff / (1000 * 60 * 60);
								//System.out.println(hour);
								count = count + hour;
							}
							catch (Exception e)
							{
							}
						}
					}
					count = count/workOrderCount;
				}
				map.put("time", startTimes[i]);
				map.put("count", count);
				map.put("workOrderCount", workOrderCount);
				rList.add(map);
			}
		}
		reutrnMap.put("value", rList);
		return reutrnMap;
	}
	
	public Map<String, Object> getProjectByOrgId(String projectId,String startTime,String endTime){
		String[] startTimes = null;
		if(startTime != null){
			startTimes = startTime.split(",");
		}
		String[] endTimes = null;
		if(endTime != null){
			endTimes = endTime.split(",");
		}
		Map<String, Object> reutrnMap = new HashMap<String, Object>();
		List<Map> rList = new ArrayList<Map>();
		if(startTimes != null && startTimes.length > 0 && endTimes != null && endTimes.length > 0){
			for(int i = 0;i < startTimes.length;i++){
				List<Map> mapList = new ArrayList<Map>();
				//List<Map<String,String>> orgByProjectIdService = this.organizationService.getOrgByProjectIdService(projectId+"");
				//yuan.yw
				List<Map<String,String>> orgByProjectIdService = this.sysOrganizationService.getOrgByProjectIdService(projectId+"");
				List<String> listString = null;
				if(orgByProjectIdService != null && orgByProjectIdService.size() > 0){
					listString = new ArrayList<String>();
					for(Map<String,String> pr:orgByProjectIdService){
						listString.add(pr.get("id")+"");
							//List<Map> commonQueryList = workManageService.commonQuery("workOrder", "and 1=1 createTime >= "+startTime+" and createTime <="+endTime+" and creatorOrgId="+pr.getId());
							//测试数据
//							List<Map> commonQueryList = new ArrayList<Map>();
//							Map<String, Object> m1 = new HashMap<String, Object>();
//							m1.put("faultOccuredTime", "2012-07-10 00:00:00");
//							m1.put("alarmClearTime", "2012-07-10 12:00:00");
//							commonQueryList.add(m1);
//							Map<String, Object> m2 = new HashMap<String, Object>();
//							m2.put("faultOccuredTime", "2012-07-15 00:00:00");
//							m2.put("alarmClearTime", "2012-07-16 12:00:00");
//							commonQueryList.add(m2);
//							mapList.addAll(commonQueryList);
					}
					List<Map> workOrderListByAccountAndAcceptProfessional = workOrderCommonService.getWorkOrderListByOrgIdsService(listString, startTimes[i], endTimes[i]);
					mapList.addAll(workOrderListByAccountAndAcceptProfessional);
				}
				long count = 0;
				int workOrderCount = 0;
				Map map = new HashMap();
				if(mapList != null && mapList.size() > 0){
					workOrderCount = mapList.size();
					for(Map m:mapList){
						if(m.get("alarmClearTime") != null && m.get("faultOccuredTime") != null){
							try
							{
								//System.out.println(m.get("alarmClearTime")+"=="+m.get("faultOccuredTime"));
								Date d1 = TimeFormatHelper.setTimeFormat(m.get("faultOccuredTime").toString());
								Date d2 = TimeFormatHelper.setTimeFormat(m.get("alarmClearTime").toString());
//								System.out.println("faultOccuredTime=="+m.get("faultOccuredTime").toString());
//								System.out.println("alarmClearTime=="+m.get("alarmClearTime").toString());
//								Date d1 = df.parse(be.getValue("alarmClearTime").toString());
//								Date d2 = df.parse(be.getValue("faultOccuredTime").toString());
								//System.out.println("d1=="+d1+"d2=="+d2);
								long diff = d2.getTime() - d1.getTime();
								long hour = diff / (1000 * 60 * 60);
								//System.out.println(hour);
								count = count + hour;
							}
							catch (Exception e)
							{
							}
						}
					}
					count = count/workOrderCount;
				}
				map.put("time", startTimes[i]);
				map.put("count", count);
				map.put("workOrderCount", workOrderCount);
				rList.add(map);
			}
		}
		reutrnMap.put("value", rList);
		return reutrnMap;
	}
	
}
