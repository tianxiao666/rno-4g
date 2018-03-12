package com.iscreate.op.service.report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.constant.OrganizationConstant;
import com.iscreate.op.dao.report.OrgWorkOrderCountMonthReportDao;
import com.iscreate.op.dao.report.StaffTaskCountMonthReportDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.report.ReportStaffTaskCountMonth;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;


public class OrgWorkOrderCountMonthReportServiceImpl implements OrgWorkOrderCountMonthReportService{
	
	public OrgWorkOrderCountMonthReportDao orgWorkOrderCountMonthReportDao;
	
	public StaffTaskCountMonthReportDao staffTaskCountMonthReportDao;

	
	private OrgStaffCountMonthReportService orgStaffCountMonthReportService;
	
	private UrgentRepairReportService urgentRepairReportService;
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	
	private SysOrgUserService sysOrgUserService;//ou.jh
	
	private SysOrganizationDao sysOrganizationDao;//du.hw

	
	public SysOrganizationDao getSysOrganizationDao() {
		return sysOrganizationDao;
	}

	public void setSysOrganizationDao(SysOrganizationDao sysOrganizationDao) {
		this.sysOrganizationDao = sysOrganizationDao;
	}

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	
	public OrgWorkOrderCountMonthReportDao getOrgWorkOrderCountMonthReportDao() {
		return orgWorkOrderCountMonthReportDao;
	}

	public void setOrgWorkOrderCountMonthReportDao(
			OrgWorkOrderCountMonthReportDao orgWorkOrderCountMonthReportDao) {
		this.orgWorkOrderCountMonthReportDao = orgWorkOrderCountMonthReportDao;
	}
	
	
	public StaffTaskCountMonthReportDao getStaffTaskCountMonthReportDao() {
		return staffTaskCountMonthReportDao;
	}

	public void setStaffTaskCountMonthReportDao(
			StaffTaskCountMonthReportDao staffTaskCountMonthReportDao) {
		this.staffTaskCountMonthReportDao = staffTaskCountMonthReportDao;
	}
	public OrgStaffCountMonthReportService getOrgStaffCountMonthReportService() {
		return orgStaffCountMonthReportService;
	}

	public void setOrgStaffCountMonthReportService(
			OrgStaffCountMonthReportService orgStaffCountMonthReportService) {
		this.orgStaffCountMonthReportService = orgStaffCountMonthReportService;
	}
	
	public UrgentRepairReportService getUrgentRepairReportService() {
		return urgentRepairReportService;
	}

	public void setUrgentRepairReportService(
			UrgentRepairReportService urgentRepairReportService) {
		this.urgentRepairReportService = urgentRepairReportService;
	}

	
	/**
	 * 根据组织Id获取该组织某段时间内的报表数据
	 * @param orgId 组织Id
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public List<Map> getOrgWorkOrderCountReportService(long orgId,String beginTime, String endTime) {
		//TODO 组织工单数 应该在数据库统计好，在Service循环调用下级会消耗性能
		List<Map> orgReportList = new ArrayList<Map>();
		int workOrderCount = 0;
		Map map = new HashMap();
		map.put("orgId",orgId);
		Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		String orgName = "";
		if(orgInfo!=null){
			orgName = (String) orgInfo.get("name");
		}
		map.put("orgName", orgName);
		//List<ProviderOrganization> subOrgList = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		List<Map<String,Object>> subOrgList = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(orgId,"BusinessOrganization","downward");

		if(subOrgList!=null && !subOrgList.isEmpty()){
			for (Map<String,Object> subOrg : subOrgList) {
				//获取数据
				List<Map<String, Object>> orgWorkOrderCountList = orgWorkOrderCountMonthReportDao.getOrgWorkOrderCountReportByOrgId(Long.valueOf(subOrg.get("orgId")+""), beginTime, endTime);
				if(orgWorkOrderCountList!=null && !orgWorkOrderCountList.isEmpty()){
					for (Map<String, Object> org : orgWorkOrderCountList) {
						if(org != null){
							if(org.get("workOrderCount") != null){
								
								Long count = Long.valueOf(org.get("workOrderCount").toString());
								workOrderCount+=count;
							}
						}
					}
				}
			}
		}else{
			if(orgInfo!=null && orgInfo.get("type").equals(OrganizationConstant.ORGANIZATION_MAINTENANCETEAM)){
				List<ReportStaffTaskCountMonth> taskCountReportList = staffTaskCountMonthReportDao.getStaffTaskCountReportByOrgId(orgId, beginTime, endTime);
				if(taskCountReportList!=null && !taskCountReportList.isEmpty()){
					for (ReportStaffTaskCountMonth rs : taskCountReportList) {
						workOrderCount+=rs.getTaskCount();
					}
				}
				map.put("isTeam",true);
			}
		}
		map.put("workOrderCount", workOrderCount);
		orgReportList.add(map);
		return orgReportList;
	}
	/**
	 * 根据组织Id获取下级组织某段时间内的报表数据
	 * @param orgId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map> getNextOrgWorkOrderCountReportService(long orgId,String beginTime, String endTime) {
		List<Map> resultList = new ArrayList<Map>();
		//List<ProviderOrganization> subOrgList = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		//yuan.yw
		List<Map<String,Object>> subOrgList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
	
		if(subOrgList!=null && !subOrgList.isEmpty()){
			for (Map<String,Object> subOrg : subOrgList) {
				List<Map> subReportList = this.getOrgWorkOrderCountReportService(Long.valueOf(subOrg.get("orgId")+""), beginTime, endTime);
				if(subReportList!=null && !subReportList.isEmpty()){
					resultList.addAll(subReportList);
				}
			}
		}
		return resultList;
	}

	/**
	 * 获取当前用户组织架构下工单数统计
	 * @param userId
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public Map getUserOrgWorkOrderCountReportService(String userId,String beginTime,String endTime) {
		Map resMap = new HashMap();
		List<String> orgNameList = new ArrayList<String>();
		List<Integer> dataList = new ArrayList<Integer>();
		List<String> orgIdList = new ArrayList<String>();
		List<String> staffNameList = new ArrayList<String>();
		String name = "工单数";
		String reportType = "org";
		String orgName = "";
		//获取用户顶级组织架构
		//List<ProviderOrganization> topOrgList = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topOrgList = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

		if(topOrgList!=null && !topOrgList.isEmpty()){
			for (SysOrg org : topOrgList) {
				orgName = org.getName();
				List<Map> nextOrgReportList = this.getNextOrgWorkOrderCountReportService(org.getOrgId(), beginTime, endTime);
				if(nextOrgReportList!=null && !nextOrgReportList.isEmpty()){
					for (Map map : nextOrgReportList) {
						dataList.add(Integer.valueOf(map.get("workOrderCount")+""));
						orgNameList.add(map.get("orgName")+"");
						orgIdList.add(map.get("orgId")+"");
					}
				}else{
					//判断当前组织架构，若组织架构为维护队，则获取下属人员任务信息
					Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(org.getOrgId());
					
					if(orgInfo!=null && orgInfo.get("type").equals(OrganizationConstant.ORGANIZATION_MAINTENANCETEAM)){
						orgName = (String) orgInfo.get("name");
						name = "任务数";
						reportType = "human";
						//ou.jh
						List<Map<String,Object>> userByOrgId = this.sysOrgUserService.getUserByOrgId(org.getOrgId());
//						List<Staff> accountList = providerOrganizationService.getAccountListByOrgIdService(org.getOrgId());
						if(userByOrgId!=null && !userByOrgId.isEmpty()){
							for (Map<String,Object> staff : userByOrgId) {
								List<ReportStaffTaskCountMonth> staffTaskCountList = staffTaskCountMonthReportDao.getStaffTaskCountReportByOrgId(staff.get("account")+"", beginTime, endTime);
								if(staffTaskCountList!=null && !staffTaskCountList.isEmpty()){
									ReportStaffTaskCountMonth staffReport = staffTaskCountList.get(0);
									dataList.add(Integer.valueOf(staffReport.getTaskCount()+""));
								}else{
									dataList.add(0);
								}
								staffNameList.add(staff.get("name")+"");
							}
						}
					}
				}
				resMap.put("userOrgId", org.getOrgId());
			}
		}
		//报表数据
		Map reportMap = new HashMap();
		reportMap.put("name", name);
		reportMap.put("data", dataList);
		//放到返回结果
		resMap.put("reportMap", reportMap);
		resMap.put("orgNameList", orgNameList);
		resMap.put("orgIdList", orgIdList);
		resMap.put("staffNameList", staffNameList);
		resMap.put("reportType", reportType);
		resMap.put("orgName", orgName);
		return resMap;
	}

	/**
	 * 根据组织Id获取下级组织报表信息
	 * @param orgId 组织Id
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 */
	public Map getNextOrgReportInfoService(long orgId, String beginTime,String endTime) {
		Map resMap = new HashMap();
		List<String> orgNameList = new ArrayList<String>();
		List<Integer> dataList = new ArrayList<Integer>();
		List<String> orgIdList = new ArrayList<String>();
		List<String> staffNameList = new ArrayList<String>();
		String name = "工单数";
		String reportType = "org";
		//默认获取当前组织架构工单数
		List<Map> nextOrgReportList = this.getNextOrgWorkOrderCountReportService(orgId, beginTime, endTime);
		if(nextOrgReportList!=null && !nextOrgReportList.isEmpty()){
			for (Map map : nextOrgReportList) {
				dataList.add(Integer.valueOf(map.get("workOrderCount")+""));
				orgNameList.add(map.get("orgName")+"");
				orgIdList.add(map.get("orgId")+"");
			}
		}else{
			//判断当前组织架构，若组织架构为维护队，则获取下属人员任务信息
			Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
			
			if(orgInfo!=null && orgInfo.get("type").equals(OrganizationConstant.ORGANIZATION_MAINTENANCETEAM)){
				name = "任务数";
				reportType = "human";
				//ou.jh
				List<Map<String,Object>> userByOrgId = this.sysOrgUserService.getUserByOrgId(orgId);
//				List<Staff> accountList = providerOrganizationService.getAccountListByOrgIdService(orgId);
				if(userByOrgId!=null && !userByOrgId.isEmpty()){
					for (Map<String,Object> staff : userByOrgId) {
						List<ReportStaffTaskCountMonth> staffTaskCountList = staffTaskCountMonthReportDao.getStaffTaskCountReportByOrgId(staff.get("account")+"", beginTime, endTime);
						if(staffTaskCountList!=null && !staffTaskCountList.isEmpty()){
							ReportStaffTaskCountMonth staffReport = staffTaskCountList.get(0);
							dataList.add(Integer.valueOf(staffReport.getTaskCount()+""));
						}else{
							dataList.add(0);
						}
						staffNameList.add(staff.get("name")+"");
					}
				}
			}
		}
		//报表数据
		Map reportMap = new HashMap();
		reportMap.put("name", name);
		reportMap.put("data", dataList);
		//放到返回结果
		resMap.put("reportMap", reportMap);
		resMap.put("orgNameList", orgNameList);
		resMap.put("orgIdList", orgIdList);
		resMap.put("staffNameList", staffNameList);
		resMap.put("reportType", reportType);
		resMap.put("userOrgId", orgId);
		String orgName = "";
		Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		if(orgInfo!=null){
			orgName = (String) orgInfo.get("name");
		}
		resMap.put("orgName", orgName);
		return resMap;
	}

	/**
	 * 获取用户组织架构工单数量环比报表信息
	 * @param orgId 组织Id 
	 * @param yearMonthList 年月List
	 * @return
	 */
	public Map getOrgWorkOrderCountChainReportService(long orgId,List<String> yearMonthList) {
		Map resMap = new HashMap();
		List<Integer> dataList = new ArrayList<Integer>();
		List<String> nameList = new ArrayList<String>();
		String orgName = "";
		Map<String,Object> org = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		if(org!=null){
			orgName = (String) org.get("name");
		}
		//根据年月List获取环比数据
		if(yearMonthList!=null && !yearMonthList.isEmpty()){
			int size = yearMonthList.size()-1;
			for(int i=size;i>=0;i--){
				String str = yearMonthList.get(i);
				String beginTime = str+"-01 00:00:00";
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date parse = null;
				try {
					parse = sf.parse(beginTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 Calendar calender = Calendar.getInstance();
		          calender.setTime(parse);
		          calender.add(Calendar.MONTH, 1);
				String endTime = sf.format(calender.getTime());
				int staffCount = 0;
				//获取数据
				List<Map> orgStaffCountList = this.getOrgWorkOrderCountReportService(orgId, beginTime, endTime);
				if(orgStaffCountList!=null && !orgStaffCountList.isEmpty()){
					Map map = orgStaffCountList.get(0);
					staffCount = Integer.valueOf(map.get("workOrderCount")+"");
					resMap.put("isTeam", map.get("isTeam"));
				}
				nameList.add(str);
				dataList.add(staffCount);
			}
		}
		//报表数据
		Map reportMap = new HashMap();
		reportMap.put("name", "工单数");
		reportMap.put("data", dataList);
		reportMap.put("type", "line");
		//保存数据
		resMap.put("orgId", orgId);
		resMap.put("orgName", orgName);
		resMap.put("reportMap", reportMap);
		resMap.put("nameList", nameList);
		return resMap;
	}

	/**
	 * 获取用户组织架构下人员及任务数统计
	 * @param userId 用户Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getUserOrgCompareReportService(String userId, String beginTime,String endTime) {
		Map resMap = new HashMap();
		List<String> nameList = new ArrayList<String>();
		List<String> idList = new ArrayList<String>();
		List<Integer> woDataList = new ArrayList<Integer>();
		List<Integer> staffDataList = new ArrayList<Integer>();
		String name = "工单数";
		String reportType = "org";
		String orgName = "";
		//获取用户顶级组织架构
		//List<ProviderOrganization> topOrgList = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topOrgList = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

		if(topOrgList!=null && !topOrgList.isEmpty()){
			for (SysOrg org : topOrgList) {
				orgName = org.getName();
				List<Map> nextOrgReportList = this.getNextOrgWorkOrderCountReportService(org.getOrgId(), beginTime, endTime);
				if(nextOrgReportList!=null && !nextOrgReportList.isEmpty()){
					for (Map map : nextOrgReportList) {
						woDataList.add(Integer.valueOf(map.get("workOrderCount")+""));
						nameList.add(map.get("orgName")+"");
						idList.add(map.get("orgId")+"");
					}
				}else{
					//判断当前组织架构，若组织架构为维护队，则获取下属人员任务信息
					Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(org.getOrgId());
					
					if(orgInfo!=null && orgInfo.get("type").equals(OrganizationConstant.ORGANIZATION_MAINTENANCETEAM)){
						name = "任务数";
						reportType = "human";
						//ou.jh
						List<Map<String,Object>> userByOrgId = this.sysOrgUserService.getUserByOrgId(org.getOrgId());
//						List<Staff> accountList = providerOrganizationService.getAccountListByOrgIdService(org.getOrgId());
						if(userByOrgId!=null && !userByOrgId.isEmpty()){
							for (Map<String,Object> staff : userByOrgId) {
								List<ReportStaffTaskCountMonth> staffTaskCountList = staffTaskCountMonthReportDao.getStaffTaskCountReportByOrgId(staff.get("account")+"", beginTime, endTime);
								if(staffTaskCountList!=null && !staffTaskCountList.isEmpty()){
									ReportStaffTaskCountMonth staffReport = staffTaskCountList.get(0);
									woDataList.add(Integer.valueOf(staffReport.getTaskCount()+""));
								}else{
									woDataList.add(0);
								}
								nameList.add(staff.get("name")+"");
							}
						}
					}
				}
				nextOrgReportList = orgStaffCountMonthReportService.getNextOrgStaffCountReportService(org.getOrgId(), beginTime, endTime);
				if(nextOrgReportList!=null && !nextOrgReportList.isEmpty()){
					for (Map map : nextOrgReportList) {
						staffDataList.add(Integer.valueOf(map.get("staffCount")+""));
					}
				}else{
					//判断当前组织架构，若组织架构为维护队，获取维护队人员数量，不可点击向下钻取
					Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(org.getOrgId());
					
					if(orgInfo!=null && orgInfo.get("type").equals(OrganizationConstant.ORGANIZATION_MAINTENANCETEAM)){
						List<Map> staffCountList = orgStaffCountMonthReportService.getOrgStaffCountByOrgIdService(org.getOrgId(), beginTime, endTime);
						if(staffCountList!=null && !staffCountList.isEmpty()){
							for (Map map : staffCountList) {
								staffDataList.add(Integer.valueOf(map.get("staffCount")+""));
							}
						}
					}
				}
				resMap.put("userOrgId", org.getOrgId());
			}
		}
		//报表数据
		Map sMap = new HashMap();
		sMap.put("name", "人员数");
		sMap.put("data", staffDataList);
		sMap.put("type", "column");
		sMap.put("yAxis", 1);
		Map wMap = new HashMap();
		wMap.put("name", name);
		wMap.put("data", woDataList);
		wMap.put("type", "line");
		//保存数据
		resMap.put("sMap", sMap);
		resMap.put("wMap", wMap);
		resMap.put("nameList", nameList);
		resMap.put("idList", idList);
		resMap.put("reportType", reportType);
		resMap.put("orgName", orgName);
		return resMap;
	}

	/**
	 * 根据组织Id获取人员及任务数统计
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getOrgCompareReportService(long orgId, String beginTime,String endTime) {
		Map resMap = new HashMap();
		List<String> nameList = new ArrayList<String>();
		List<String> idList = new ArrayList<String>();
		List<Integer> woDataList = new ArrayList<Integer>();
		List<Integer> staffDataList = new ArrayList<Integer>();
		String name = "工单数";
		String reportType = "org";
		List<Map> woList = this.getNextOrgWorkOrderCountReportService(orgId, beginTime, endTime);
		if(woList!=null && !woList.isEmpty()){
			for (Map map : woList) {
				woDataList.add(Integer.valueOf(map.get("workOrderCount")+""));
				nameList.add(map.get("orgName")+"");
				idList.add(map.get("orgId")+"");
			}
		}else{
			//判断当前组织架构，若组织架构为维护队，则获取下属人员任务信息
			Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
			
			if(orgInfo!=null && orgInfo.get("type").equals(OrganizationConstant.ORGANIZATION_MAINTENANCETEAM)){
				name = "任务数";
				reportType = "human";
				//ou.jh
				List<Map<String,Object>> userByOrgId = this.sysOrgUserService.getUserByOrgId(orgId);
//				List<Staff> accountList = providerOrganizationService.getAccountListByOrgIdService(orgId);
				if(userByOrgId!=null && !userByOrgId.isEmpty()){
					for (Map<String,Object> staff : userByOrgId) {
						List<ReportStaffTaskCountMonth> staffTaskCountList = staffTaskCountMonthReportDao.getStaffTaskCountReportByOrgId(staff.get("account")+"", beginTime, endTime);
						if(staffTaskCountList!=null && !staffTaskCountList.isEmpty()){
							ReportStaffTaskCountMonth staffReport = staffTaskCountList.get(0);
							woDataList.add(Integer.valueOf(staffReport.getTaskCount()+""));
						}else{
							woDataList.add(0);
						}
						nameList.add(staff.get("name")+"");
					}
				}
			}
		}
		List<Map> scList = orgStaffCountMonthReportService.getNextOrgStaffCountReportService(orgId, beginTime, endTime);
		if(scList!=null && !scList.isEmpty()){
			for (Map map : scList) {
				staffDataList.add(Integer.valueOf(map.get("staffCount")+""));
			}
		}
		//报表数据
		Map sMap = new HashMap();
		sMap.put("name", "人员数");
		sMap.put("data", staffDataList);
		sMap.put("type", "column");
		sMap.put("yAxis", 1);
		Map wMap = new HashMap();
		wMap.put("name", name);
		wMap.put("data", woDataList);
		wMap.put("type", "line");
		//保存数据
		resMap.put("sMap", sMap);
		resMap.put("wMap", wMap);
		resMap.put("nameList", nameList);
		resMap.put("idList", idList);
		resMap.put("reportType", reportType);
		Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		if(orgInfo!=null){
			resMap.put("orgName", orgInfo.get("name"));
		}
		resMap.put("userOrgId", orgId);
		return resMap;
	}

	/**
	 * 获取人员及任务数统计 环比数据
	 * @param orgId 组织Id
	 * @param yearMonthList 年月List
	 * @return
	 */
	public Map getOrgCompareChainReportService(long orgId,List<String> yearMonthList) {
		Map resMap = new HashMap();
		List<Integer> woDataList = new ArrayList<Integer>();	//任务数 数据
		List<Integer> scDataList = new ArrayList<Integer>();	//人员数 数据
		List<String> nameList = new ArrayList<String>();
		//根据年月List获取环比数据
		if(yearMonthList!=null && !yearMonthList.isEmpty()){
			int size = yearMonthList.size()-1;
			for(int i=size;i>=0;i--){
				String str = yearMonthList.get(i);
				String beginTime = str+"-01 00:00:00";
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date parse = null;
				try {
					parse = sf.parse(beginTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 Calendar calender = Calendar.getInstance();
		          calender.setTime(parse);
		          calender.add(Calendar.MONTH, 1);
				String endTime = sf.format(calender.getTime());
				int woCount = 0;
				int staffCount = 0;
				//获取数据
				List<Map> orgWorkOrderCountList = this.getOrgWorkOrderCountReportService(orgId, beginTime, endTime);
				if(orgWorkOrderCountList!=null && !orgWorkOrderCountList.isEmpty()){
					Map map = orgWorkOrderCountList.get(0);
					woCount = Integer.valueOf(map.get("workOrderCount")+"");
				}
				List<Map> orgStaffCountList = orgStaffCountMonthReportService.getOrgStaffCountByOrgIdService(orgId, beginTime, endTime);
				if(orgStaffCountList!=null && !orgStaffCountList.isEmpty()){
					Map map = orgStaffCountList.get(0);
					staffCount = Integer.valueOf(map.get("staffCount")+"");
				}
				nameList.add(str);
				woDataList.add(woCount);
				scDataList.add(staffCount);
			}
		}
		String orgName = "";
		Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		if(orgInfo!=null){
			orgName = (String) orgInfo.get("name");
		}
		//报表数据
		Map sMap = new HashMap();
		sMap.put("name", "人员数");
		sMap.put("data", scDataList);
		sMap.put("type", "line");
		Map wMap = new HashMap();
		wMap.put("name", "工单数");
		wMap.put("data", woDataList);
		wMap.put("type", "line");
		//保存数据
		resMap.put("userOrgId", orgId);
		resMap.put("orgName", orgName);
		resMap.put("sMap", sMap);
		resMap.put("wMap", wMap);
		resMap.put("nameList", nameList);
		return resMap;
	}

	/**
	 * 获取上级工单数报表信息
	 * @param orgId 组织架构Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getUpOrgReportInfoService(long orgId, String beginTime,String endTime) {
		Map resMap = null;
		//List<ProviderOrganization> upOrgList = providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> upOrgList = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		if(upOrgList!=null && !upOrgList.isEmpty()){
			SysOrg upOrg = upOrgList.get(0);
			orgId = upOrg.getOrgId();
			resMap = this.getNextOrgReportInfoService(orgId, beginTime, endTime);
		}
		return resMap;
	}

	/**
	 * 获取上级组织架构工单数量环比报表信息
	 * @param orgId 组织Id 
	 * @param yearMonthList 年月List
	 * @return
	 */
	public Map getUpOrgWorkOrderCountChainReportService(long orgId,List<String> yearMonthList) {
		Map resMap = null;
		//List<ProviderOrganization> upOrgList = providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> upOrgList = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		if(upOrgList!=null && !upOrgList.isEmpty()){
			SysOrg upOrg = upOrgList.get(0);
			orgId = upOrg.getOrgId();
			resMap = this.getOrgWorkOrderCountChainReportService(orgId, yearMonthList);
		}
		return resMap;
	}

	/**
	 * 获取上级组织的 人员及任务数统计 环比数据
	 * @param orgId 组织Id
	 * @param yearMonthList 年月List
	 * @return
	 */
	public Map getUpOrgCompareChainReportService(long orgId,List<String> yearMonthList) {
		Map resMap = null;
		//List<ProviderOrganization> upOrgList = providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> upOrgList = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		if(upOrgList!=null && !upOrgList.isEmpty()){
			SysOrg upOrg = upOrgList.get(0);
			orgId = upOrg.getOrgId();
			resMap = this.getOrgCompareChainReportService(orgId, yearMonthList);
		}
		return resMap;
	}

	/**
	 * 获取上级组织的 人员及任务数统计
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getUpOrgCompareReportService(long orgId, String beginTime,String endTime) {
		Map resMap = null;
		//List<ProviderOrganization> upOrgList = providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> upOrgList = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		if(upOrgList!=null && !upOrgList.isEmpty()){
			SysOrg upOrg = upOrgList.get(0);
			orgId = upOrg.getOrgId();
			resMap = this.getOrgCompareReportService(orgId, beginTime, endTime);
		}
		return resMap;
	}

	/**
	 * 获取组织架构下 项目工单数统计
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getUserOrgProjectWorkOrderCountReportService(long orgId,String beginTime, String endTime) {
		Map resMap = new HashMap();	//返回结果
		List<Integer> dataList = new ArrayList<Integer>();
		List<String> orgNameList = new ArrayList<String>();
		List<String> orgIdList =  new ArrayList<String>();
		String orgName = "";
		String reName = "工单数";
		Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		if(orgInfo!=null){
			if(orgInfo.get("type").equals(OrganizationConstant.ORGANIZATION_MAINTENANCETEAM)){
				reName = "任务数";
			}
			orgName = (String) orgInfo.get("name");
		}
		//获取当前登录用户组织架构下的 项目
		//List<Map<String, String>> projectList = organizationService.getProjectToDownwardOrgByOrgIdService(orgId);
		//yuan.yw
		List<Map<String, String>> projectList = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(orgId);
		if(projectList!=null && !projectList.isEmpty()){
			for (Map<String, String> map : projectList) {
				long poId = Long.valueOf(map.get("id"));
				String poName = map.get("NAME");
				int workOrderCount = 0;
				List<Map<String, Object>> woList = urgentRepairReportService.getUrgentRepairBylatestAllowedTimeAndJudgeProject(poId,beginTime,endTime,null,null,null);
				if(woList!=null && !woList.isEmpty()){
					workOrderCount = woList.size();
				}
				orgNameList.add(poName);
				dataList.add(workOrderCount);
				orgIdList.add(poId+"");
			}
		}
		//报表数据
		Map reportMap = new HashMap();
		reportMap.put("name", reName);
		reportMap.put("data", dataList);
		//放到返回结果
		resMap.put("reportMap", reportMap);
		resMap.put("orgIdList", orgIdList);
		resMap.put("orgNameList", orgNameList);
		resMap.put("orgName", orgName);
		resMap.put("userOrgId", orgId);
		resMap.put("reportType", "project");
		return resMap;
	}

	/**
	 * 获取组织下项目的人员及任务数统计
	 * @param orgId 组织Id
	 * @param beginTime 统计开始时间
	 * @param endTime 统计结束时间
	 * @return
	 */
	public Map getOrgProjectStaffAndWorkOrderCountReportService(long orgId,String beginTime, String endTime) {
		Map resMap = new HashMap();
		List<String> nameList = new ArrayList<String>();
		List<String> idList = new ArrayList<String>();
		List<Integer> woDataList = new ArrayList<Integer>();
		List<Integer> staffDataList = new ArrayList<Integer>();
		String name = "工单数";
		//List<Map<String, String>> projectList = organizationService.getProjectToDownwardOrgByOrgIdService(orgId);
		//yuan.yw
		List<Map<String, String>> projectList = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(orgId);
		if(projectList!=null && !projectList.isEmpty()){
			for (Map<String, String> map : projectList) {
				long poId = Long.valueOf(map.get("id")+"");
				String poName = map.get("NAME");
				int scCount = 0;
				int woCount = 0;
				//获取项目所属组织
				//List<Map<String, String>> orgList = organizationService.getOrgByProjectIdService(poId+"");
				//yuan.yw
				List<Map<String, String>> orgList = this.sysOrganizationService.getOrgByProjectIdService(poId+"");
				if(orgList!=null && !orgList.isEmpty()){
					for (Map<String, String> map2 : orgList) {
						long pOrgId = Long.valueOf(map2.get("id")+"");
						List<Map> scList = orgStaffCountMonthReportService.getOrgStaffCountByOrgIdService(pOrgId, beginTime, endTime);
						if(scList!=null && !scList.isEmpty()){
							for (Map m : scList) {
								scCount += Integer.valueOf(m.get("staffCount")+"");
							}
						}
					}
				}
				List<Map<String, Object>> woList = urgentRepairReportService.getUrgentRepairBylatestAllowedTimeAndJudgeProject(poId,beginTime,endTime,null,null,null);
				if(woList!=null && !woList.isEmpty()){
					woCount = woList.size();
				}
				nameList.add(poName);
				idList.add(poId+"");
				staffDataList.add(scCount);
				woDataList.add(woCount);
			}
		}
		//报表数据
		Map sMap = new HashMap();
		sMap.put("name", "人员数");
		sMap.put("data", staffDataList);
		sMap.put("type", "column");
		sMap.put("yAxis", 1);
		Map wMap = new HashMap();
		wMap.put("name", name);
		wMap.put("data", woDataList);
		wMap.put("type", "line");
		//保存数据
		resMap.put("sMap", sMap);
		resMap.put("wMap", wMap);
		resMap.put("nameList", nameList);
		resMap.put("idList", idList);
		resMap.put("reportType", "project");
		Map<String,Object> orgInfo = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		
		if(orgInfo!=null){
			resMap.put("orgName", orgInfo.get("name"));
		}
		resMap.put("userOrgId", orgId);
		return resMap;
	}
	
	
	
}
