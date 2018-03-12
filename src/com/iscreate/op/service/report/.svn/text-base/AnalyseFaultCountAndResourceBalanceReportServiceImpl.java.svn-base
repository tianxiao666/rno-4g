package com.iscreate.op.service.report;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.iscreate.op.constant.OrganizationConstant;
import com.iscreate.op.dao.informationmanage.InformationManageNetworkResourceDao;
import com.iscreate.op.dao.report.AnalyseFaultCountAndResourceBalanceReportDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.informationmanage.InformationManageNetworkResourceService;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.system.SysOrganizationService;

public class AnalyseFaultCountAndResourceBalanceReportServiceImpl implements
		AnalyseFaultCountAndResourceBalanceReportService {
	
	private AnalyseFaultCountAndResourceBalanceReportDao analyseFaultCountAndResourceBalanceReportDao;
	
	private InformationManageNetworkResourceService informationManageNetworkResourceService;
	private UrgentRepairReportService urgentRepairReportService;
	
	private InformationManageNetworkResourceDao informationManageNetworkResourceDao;
	private NetworkResourceInterfaceService networkResourceInterfaceService;
	
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
	
	private static Gson gson=new Gson();
	
	public static NumberFormat nf=NumberFormat.getInstance();
	
	
	public void setAnalyseFaultCountAndResourceBalanceReportDao(
			AnalyseFaultCountAndResourceBalanceReportDao analyseFaultCountAndResourceBalanceReportDao) {
		this.analyseFaultCountAndResourceBalanceReportDao = analyseFaultCountAndResourceBalanceReportDao;
	}

	
	
	
	public void setUrgentRepairReportService(
			UrgentRepairReportService urgentRepairReportService) {
		this.urgentRepairReportService = urgentRepairReportService;
	}
	

	public void setInformationManageNetworkResourceService(
			InformationManageNetworkResourceService informationManageNetworkResourceService) {
		this.informationManageNetworkResourceService = informationManageNetworkResourceService;
	}

	public void setInformationManageNetworkResourceDao(
			InformationManageNetworkResourceDao informationManageNetworkResourceDao) {
		this.informationManageNetworkResourceDao = informationManageNetworkResourceDao;
	}

	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}

	/**
	 * 根据用户账号获取组织对应的每百个基站的人/车/任务量统计分析的数据
	 * @param userId 用户账号
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getAnalyseFaultCountAndResourceBalanceDataByUserId(String userId,String beginTime,String endTime){
		
		Map<String,Object> returnMap=new HashMap<String,Object>();
		SysOrg topOrg;
		String json="";
		
		nf.setMaximumFractionDigits(1);
		try {
			//List<ProviderOrganization> orgList=this.providerOrganizationService.getTopLevelOrgByAccount(userId);
			List<SysOrg> orgList=this.sysOrganizationService.getTopLevelOrgByAccount(userId);
			if(orgList!=null && !orgList.isEmpty()){
				topOrg=orgList.get(0);
				
				if(topOrg!=null){
					//获取子组织集合
					//List<ProviderOrganization> subOrgList=this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(topOrg.getId(),"BusinessOrganization");
					//yuan.yw
					List<Map<String,Object>> subOrgList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(topOrg.getOrgId(),"BusinessOrganization");
					List<Long> orgId_xAxis=new ArrayList<Long>();
					List<String> orgName_xAxis=new ArrayList<String>(); //组织名称（x轴）
					List<Double> workOrderCount_yAxis=new ArrayList<Double>();	//工单数
					List<Double> peopleCount_yAxis=new ArrayList<Double>();	//人员数
					List<Double> carCount_yAxis=new ArrayList<Double>();	//车辆数
					
					Double per_workOrderTotalCount=0d;
					Double per_peopleTotalCount=0d;
					Double per_carTotalCount=0d;
					
					if(subOrgList!=null && !subOrgList.isEmpty()){
						for(Map<String,Object> organization:subOrgList){
							if(!OrganizationConstant.ORGANIZATION_FUNCTIONDIVISION.equals(organization.get("type")+"")){
								String orgName=organization.get("name")+"";
								orgId_xAxis.add(Long.valueOf(organization.get("orgId")+""));
								orgName_xAxis.add(orgName);
								
								//获取组织对应的数据
								List<Map<String,Object>> orgResList=this.getAnalyseFaultCountAndResourceBalanceDataByOrgIdAndTime(organization,beginTime,endTime);
								if(orgResList!=null && !orgResList.isEmpty()){
									for(Map<String,Object> tempMap:orgResList){
										if(tempMap!=null){
											double peopleCount=Long.valueOf(tempMap.get("peopleCount")==null?"0":tempMap.get("peopleCount").toString());
											double carCount=Long.valueOf(tempMap.get("carCount")==null?"0":tempMap.get("carCount").toString());
											double workOrderCount=Long.valueOf(tempMap.get("workOrderCount")==null?"0":tempMap.get("workOrderCount").toString());
											double baseStationCount=Double.valueOf(tempMap.get("baseStationCount")==null?"0":tempMap.get("baseStationCount").toString());
											
//											peopleCount_yAxis.add(Double.valueOf(nf.format(((peopleCount/68))*100)));
//											carCount_yAxis.add(Double.valueOf(nf.format((carCount/68)*100)));
//											workOrderCount_yAxis.add(Double.valueOf(nf.format((workOrderCount/68)*100)));
											
//											//统计每百基站数据
											if(baseStationCount!=0){
												Double res=Double.valueOf(nf.format(((peopleCount/baseStationCount))*100));
												peopleCount_yAxis.add(res);
												per_peopleTotalCount=per_peopleTotalCount+res;
												
												
												res=Double.valueOf(nf.format((carCount/baseStationCount)*100));
												carCount_yAxis.add(res);
												per_carTotalCount=per_carTotalCount+res;
												
												res=Double.valueOf(nf.format((workOrderCount/baseStationCount)*100));
												workOrderCount_yAxis.add(res);
												per_workOrderTotalCount=per_workOrderTotalCount+res;
											}else{
												peopleCount_yAxis.add(null);
												carCount_yAxis.add(null);
												workOrderCount_yAxis.add(null);
											}
										}
									}
								}
							}
						}
					}
					
					List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
					
					Map<String,Object> yAxis_workOrderCountMap=new HashMap<String,Object>();
					yAxis_workOrderCountMap.put("name", "工单数");
					yAxis_workOrderCountMap.put("type", "line");
					//yAxis_workOrderCountMap.put("yAxis", 1);
					yAxis_workOrderCountMap.put("data", workOrderCount_yAxis);
					yAxis.add(yAxis_workOrderCountMap);
					
					Map<String,Object> yAxis_peopleCountMap=new HashMap<String,Object>();
					yAxis_peopleCountMap.put("name", "人员");
					yAxis_peopleCountMap.put("type", "column");
					yAxis_peopleCountMap.put("yAxis", 1);
					yAxis_peopleCountMap.put("data", peopleCount_yAxis);
					yAxis.add(yAxis_peopleCountMap);
					
					Map<String,Object> yAxis_carCountMap=new HashMap<String,Object>();
					yAxis_carCountMap.put("name", "车辆");
					yAxis_carCountMap.put("type", "column");
					yAxis_carCountMap.put("yAxis", 1);
					yAxis_carCountMap.put("data", carCount_yAxis);
					yAxis.add(yAxis_carCountMap);
					
					
					returnMap.put("topOrgName", topOrg.getName());	//组织名称
					returnMap.put("topOrgId", topOrg.getOrgId());	//组织id
					returnMap.put("xAxis", orgName_xAxis);	//xAxis数据
					returnMap.put("yAxis", yAxis);	//yAxis数据
					returnMap.put("orgId_xAxis", orgId_xAxis);
					
					//获取统计数据
					returnMap.put("per_workOrderTotalCount", nf.format(per_workOrderTotalCount));
					returnMap.put("per_peopleTotalCount", nf.format(per_peopleTotalCount));
					returnMap.put("per_carTotalCount", nf.format(per_carTotalCount));
					
					json=gson.toJson(returnMap);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	/**
	 * 获取下级组织对应的每百个基站的人/车/任务量统计分析的数据
	 * @param orgId 组织id
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getSubOrgAnalyseFaultCountAndResourceBalanceDataBySubOrgId(Long orgId,String beginTime,String endTime){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		Map<String,Object> topOrg;
		String json="";
		
		nf.setMaximumFractionDigits(1);
		try {
			if(orgId!=null && orgId.intValue()>0){
				 topOrg = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
				
				if(topOrg!=null){
					//获取子组织集合
					//List<ProviderOrganization> subOrgList=this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(topOrg.getId(),"BusinessOrganization");
					//yuan.yw
					List<Map<String,Object>> subOrgList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(Long.parseLong((String) topOrg.get("id")),"BusinessOrganization");
					List<Long> orgId_xAxis=new ArrayList<Long>();
					List<String> orgName_xAxis=new ArrayList<String>(); //组织名称（x轴）
					List<Double> peopleCount_yAxis=new ArrayList<Double>();	//人员数
					List<Double> carCount_yAxis=new ArrayList<Double>();	//车辆数
					List<Double> workOrderCount_yAxis=new ArrayList<Double>();	//工单数
					
					Double per_workOrderTotalCount=0d;
					Double per_peopleTotalCount=0d;
					Double per_carTotalCount=0d;
					
					if(subOrgList!=null && !subOrgList.isEmpty()){
						for(Map<String,Object> organization:subOrgList){
							
							String orgName=organization.get("name")+"";
							orgId_xAxis.add(Long.valueOf(organization.get("orgId")+""));
							orgName_xAxis.add(orgName);
							
							//获取组织对应的数据
							List<Map<String,Object>> orgResList=this.getAnalyseFaultCountAndResourceBalanceDataByOrgIdAndTime(organization,beginTime,endTime);
							if(orgResList!=null && !orgResList.isEmpty()){
								for(Map<String,Object> tempMap:orgResList){
									if(tempMap!=null){
										double peopleCount=Double.valueOf(tempMap.get("peopleCount")==null?"0":tempMap.get("peopleCount").toString());
										double carCount=Double.valueOf(tempMap.get("carCount")==null?"0":tempMap.get("carCount").toString());
										double workOrderCount=Double.valueOf(tempMap.get("workOrderCount")==null?"0":tempMap.get("workOrderCount").toString());
										double baseStationCount=Double.valueOf(tempMap.get("baseStationCount")==null?"0":tempMap.get("baseStationCount").toString());
										
//										//统计每百基站数据
										if(baseStationCount!=0){
//											peopleCount_yAxis.add(Double.valueOf(nf.format(((peopleCount/baseStationCount))*100)));
//											carCount_yAxis.add(Double.valueOf(nf.format((carCount/baseStationCount)*100)));
//											workOrderCount_yAxis.add(Double.valueOf(nf.format((workOrderCount/baseStationCount)*100)));
											
											Double res=Double.valueOf(nf.format(((peopleCount/baseStationCount))*100));
											peopleCount_yAxis.add(res);
											per_peopleTotalCount=per_peopleTotalCount+res;
											
											res=Double.valueOf(nf.format((carCount/baseStationCount)*100));
											carCount_yAxis.add(res);
											per_carTotalCount=per_carTotalCount+res;
											
											res=Double.valueOf(nf.format((workOrderCount/baseStationCount)*100));
											workOrderCount_yAxis.add(res);
											per_workOrderTotalCount=per_workOrderTotalCount+res;
											
										}else{
											peopleCount_yAxis.add(null);
											carCount_yAxis.add(null);
											workOrderCount_yAxis.add(null);
										}
									}
								}
							}
						}
					}
					
					List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
					
					Map<String,Object> yAxis_workOrderCountMap=new HashMap<String,Object>();
					yAxis_workOrderCountMap.put("name", "工单数");
					yAxis_workOrderCountMap.put("type", "line");
					yAxis_workOrderCountMap.put("data", workOrderCount_yAxis);
					yAxis.add(yAxis_workOrderCountMap);
					
					Map<String,Object> yAxis_peopleCountMap=new HashMap<String,Object>();
					yAxis_peopleCountMap.put("name", "人员");
					yAxis_peopleCountMap.put("type", "column");
					yAxis_peopleCountMap.put("data", peopleCount_yAxis);
					yAxis_peopleCountMap.put("yAxis", 1);
					yAxis.add(yAxis_peopleCountMap); 
					
					Map<String,Object> yAxis_carCountMap=new HashMap<String,Object>();
					yAxis_carCountMap.put("name", "车辆");
					yAxis_carCountMap.put("type", "column");
					yAxis_carCountMap.put("data", carCount_yAxis);
					yAxis_carCountMap.put("yAxis", 1);
					yAxis.add(yAxis_carCountMap);
					
					resultMap.put("topOrgName", topOrg.get("name"));	//组织名称
					resultMap.put("topOrgId", topOrg.get("id"));	//组织id
					resultMap.put("xAxis", orgName_xAxis);	//xAxis数据
					resultMap.put("yAxis", yAxis);	//yAxis数据
					
					resultMap.put("orgId_xAxis", orgId_xAxis);
					
					//获取统计数据
					resultMap.put("per_workOrderTotalCount", nf.format(per_workOrderTotalCount));
					resultMap.put("per_peopleTotalCount", nf.format(per_peopleTotalCount));
					resultMap.put("per_carTotalCount", nf.format(per_carTotalCount));
					
					json=gson.toJson(resultMap);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	/**
	 * 根据用户账号获取组织对应的每百个基站的人/车/任务量统计分析的环比数据
	 * @param userId
	 * @param yearMonthList 年月List
	 * @return
	 */
	public String getAnalyseFaultCountAndResourceBalanceReportLoopCompareDataByUserId(String userId,List<String> yearMonthList){
		
		String json="";
		List<Map<String, Object>> dataList=null;
		
		SysOrg topOrg;
		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
		Map<String,Object> yAxis_peopleCountMap=new HashMap<String,Object>();
		Map<String,Object> yAxis_carCountMap=new HashMap<String,Object>();
		Map<String,Object> yAxis_workOrderCountMap=new HashMap<String,Object>();
		
		List<Double> peopleCount_yAxis=new ArrayList<Double>();
		List<Double> carCount_yAxis=new ArrayList<Double>();
		List<Double> workOrderCount_yAxis=new ArrayList<Double>();
		
		//获取人员最高级组织架构
		//List<ProviderOrganization> topOrgList = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topOrgList=this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		
		List<String> dateName_xAxis=new ArrayList<String>();
		
		if(topOrgList!=null && !topOrgList.isEmpty()){
			long orgId = 0;
			topOrg=topOrgList.get(0);
			orgId=topOrg.getOrgId();
			
			//获取子组织
			//List<ProviderOrganization> subOrgList=this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
			//yuan.yw
			List<Map<String,Object>> subOrgList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
			if(subOrgList!=null && !subOrgList.isEmpty()){
				
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
						
						
						dataList = this.getAnalyseFaultCountAndResourceBalanceReportLoopCompareDataByCondition(beginTime, endTime);
						
						//组装前台展示格式数据
						if(dataList!=null && !dataList.isEmpty()){
							for(Map<String,Object> tempMap:dataList){
								Double peopleCount=Double.valueOf(tempMap.get("peopleCount")==null?"0":tempMap.get("peopleCount").toString());
								Double carCount=Double.valueOf(tempMap.get("carCount")==null?"0":tempMap.get("carCount").toString());
								Double workOrderCount=Double.valueOf(tempMap.get("workOrderCount")==null?"0":tempMap.get("workOrderCount").toString());
								Long baseStationCount=Long.valueOf(tempMap.get("baseStationCount")==null?"0":tempMap.get("baseStationCount").toString());
								 
//								peopleCount_yAxis.add(peopleCount);
//								carCount_yAxis.add(carCount);
//								workOrderCount_yAxis.add(workOrderCount);
								
								//统计每百基站数据
								if(baseStationCount.intValue()!=0){
									peopleCount_yAxis.add(Double.valueOf(nf.format(((peopleCount/baseStationCount))*100)));
									carCount_yAxis.add(Double.valueOf(nf.format((carCount/baseStationCount)*100)));
									workOrderCount_yAxis.add(Double.valueOf(nf.format((workOrderCount/baseStationCount)*100)));
								}else{
									peopleCount_yAxis.add(0d);
									carCount_yAxis.add(0d);
									workOrderCount_yAxis.add(0d);
								}
							}
						}
						dateName_xAxis.add(str);
					}
					yAxis_workOrderCountMap.put("name", "工单数");
					yAxis_workOrderCountMap.put("data",workOrderCount_yAxis);
					yAxis.add(yAxis_workOrderCountMap);
					
					yAxis_peopleCountMap.put("name", "人员");
					yAxis_peopleCountMap.put("data", peopleCount_yAxis);
					yAxis_peopleCountMap.put("type","line");
					yAxis_peopleCountMap.put("yAxis", 1);
					yAxis.add(yAxis_peopleCountMap);
					
					yAxis_carCountMap.put("name", "车辆");
					yAxis_carCountMap.put("data", carCount_yAxis);
					yAxis_carCountMap.put("type","line");
					yAxis_carCountMap.put("yAxis", 1);
					yAxis.add(yAxis_carCountMap);
				}
			}
			if(topOrg!=null){
				resultMap.put("topOrgName", topOrg.getName());	//组织名称
				resultMap.put("topOrgId", topOrg.getOrgId());	//组织id
			}
		}
		
		resultMap.put("xAxis", dateName_xAxis);	//xAxis数据
		resultMap.put("yAxis", yAxis);	//yAxis数据
		
		json=gson.toJson(resultMap);
		//yAxis_peopleCountMap.put("type", "line");
		
		return json;
	}
	
	
	
	/**
	 * 按项目获取对应的每百个基站的人/车/任务量统计分析的数据
	 * @param userId 用户账号
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getAnalyseFaultCountAndResourceBalanceDataByUserIdForProject(String userId,String beginTime,String endTime){
		Map<String,Object> returnMap=new HashMap<String,Object>();
		SysOrg topOrg=null;
		String json="";
		nf.setMaximumFractionDigits(1);
		try {
			List<String> projectId_xAxis=new ArrayList<String>();
			List<String> projectName_xAxis=new ArrayList<String>(); //项目名称（x轴）
			List<Double> workOrderCount_yAxis=new ArrayList<Double>();	//工单数
			List<Double> peopleCount_yAxis=new ArrayList<Double>();	//人员数
			List<Double> carCount_yAxis=new ArrayList<Double>();	//车辆数
			
			Double per_workOrderTotalCount=0d;
			Double per_peopleTotalCount=0d;
			Double per_carTotalCount=0d;
			
			//List<ProviderOrganization> topOrgList=this.providerOrganizationService.getTopLevelOrgByAccount(userId);
			
			List<SysOrg> topOrgList=this.sysOrganizationService.getTopLevelOrgByAccount(userId);
			
			if(topOrgList!=null && !topOrgList.isEmpty()){
				topOrg=topOrgList.get(0);
			}
			
			//获取人员相关的组织列表
			//List<ProviderOrganization> orgList=this.providerOrganizationService.getOrgByAccountService(userId);
			//yuan.yw
			List<SysOrg> orgList=this.sysOrganizationService.getOrgByAccountService(userId);
			
			if(orgList!=null && !orgList.isEmpty()){
				
				List<Map<String, String>> projectList=new ArrayList<Map<String, String>>();	//项目列表
				for(SysOrg org:orgList){
					//根据组织获取项目
					//List<Map<String, String>> osrList = this.organizationService.getProjectToDownwardOrgByOrgIdService(org.getId());
					//yuan.yw
					List<Map<String, String>> osrList = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(org.getOrgId());
					if(osrList!=null && !osrList.isEmpty()){
						projectList.addAll(osrList);
					}
				}
				
				//遍历项目列表
				if(projectList!=null && !projectList.isEmpty()){
					for(Map<String, String> p:projectList){
						if(p!=null && !p.isEmpty()){
							String projectId = p.get("id")==null?"":p.get("id").toString();
							String projectName=p.get("NAME")==null?"":p.get("NAME").toString();
							
							projectName_xAxis.add(projectName);
							
							//根据项目获取实施组织
							List<String> operationOrgList=this.informationManageNetworkResourceService.findOrgIdByProjectId(projectId);
							
							//根据组织，获取相关的人员和车辆
							List<Map<String,Object>> dataList=this.getAnalyseFaultCountAndResourceBalanceDataByTimeForProject(operationOrgList, beginTime, endTime);
							
							//根据项目获取工单数
							double workOrderCount=0d;
							List<Map<String, Object>> workOrderList=this.urgentRepairReportService.getUrgentRepairBylatestAllowedTimeAndJudgeProject(Long.valueOf(projectId), beginTime, endTime, null, null, null);
							if(workOrderList!=null && !workOrderList.isEmpty()){
								workOrderCount=workOrderList.size();
							}else{
								workOrderCount=0d;
							}
							
							//根据项目获取基站数
							double baseStationCount=0d;
							
							List<Map<String,String>> findProjectResourceByProjectId = this.informationManageNetworkResourceDao.findAreaIdAndResourceTypeByProjectIdWithoutSame(projectId);
							if(findProjectResourceByProjectId!=null && !findProjectResourceByProjectId.isEmpty()){
								Map<String, List<Map<String,String>>> reMap = new HashMap<String, List<Map<String,String>>>();
								for(Map<String,String> m:findProjectResourceByProjectId){
									String areaId = m.get("areaId"); 
									String resourceType = m.get("resourceType");
									if("GeneralBaseStation".equals(resourceType)){
										List<Map<String,String>> baseStationByAreaIdAndReType = this.networkResourceInterfaceService.getBaseStationByAreaIdAndReType(areaId, resourceType);
										if(baseStationByAreaIdAndReType!=null && !baseStationByAreaIdAndReType.isEmpty()){
											baseStationCount=baseStationCount+baseStationByAreaIdAndReType.size();
										}
									}
								}
							}
							
							if(dataList!=null && !dataList.isEmpty()){
								for(Map<String,Object> tempMap:dataList){
									if(tempMap!=null){
										double peopleCount=Long.valueOf(tempMap.get("peopleCount")==null?"0":tempMap.get("peopleCount").toString());
										double carCount=Long.valueOf(tempMap.get("carCount")==null?"0":tempMap.get("carCount").toString());
										//double workOrderCount=Long.valueOf(tempMap.get("workOrderCount")==null?"0":tempMap.get("workOrderCount").toString());
										
										//统计每百基站数据
										if(baseStationCount!=0){
											Double res=Double.valueOf(nf.format(((peopleCount/baseStationCount))*100));
											peopleCount_yAxis.add(res);
											per_peopleTotalCount=per_peopleTotalCount+res;
											
											res=Double.valueOf(nf.format((carCount/baseStationCount)*100));
											carCount_yAxis.add(res);
											per_carTotalCount=per_carTotalCount+res;
											
											res=Double.valueOf(nf.format((workOrderCount/baseStationCount)*100));
											workOrderCount_yAxis.add(res);
											per_workOrderTotalCount=per_workOrderTotalCount+res;
										}else{
											peopleCount_yAxis.add(null);
											carCount_yAxis.add(null);
											workOrderCount_yAxis.add(null);
										}
									}
								}
							}
						}
					}
				}
				List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
				
				Map<String,Object> yAxis_workOrderCountMap=new HashMap<String,Object>();
				yAxis_workOrderCountMap.put("name", "工单数");
				yAxis_workOrderCountMap.put("type", "line");
				//yAxis_workOrderCountMap.put("yAxis", 1);
				yAxis_workOrderCountMap.put("data", workOrderCount_yAxis);
				yAxis.add(yAxis_workOrderCountMap);
				
				Map<String,Object> yAxis_peopleCountMap=new HashMap<String,Object>();
				yAxis_peopleCountMap.put("name", "人员");
				yAxis_peopleCountMap.put("type", "column");
				yAxis_peopleCountMap.put("yAxis", 1);
				yAxis_peopleCountMap.put("data", peopleCount_yAxis);
				yAxis.add(yAxis_peopleCountMap);
				
				Map<String,Object> yAxis_carCountMap=new HashMap<String,Object>();
				yAxis_carCountMap.put("name", "车辆");
				yAxis_carCountMap.put("type", "column");
				yAxis_carCountMap.put("yAxis", 1);
				yAxis_carCountMap.put("data", carCount_yAxis);
				yAxis.add(yAxis_carCountMap);
				
				if(topOrg!=null){
					returnMap.put("topOrgName", topOrg.getName());	//组织名称
					returnMap.put("topOrgId", topOrg.getOrgId());	//组织id
				}
				
				returnMap.put("xAxis", projectName_xAxis);	//xAxis数据
				returnMap.put("yAxis", yAxis);	//yAxis数据
				returnMap.put("orgId_xAxis", projectId_xAxis);
				
				//获取统计数据
				returnMap.put("per_workOrderTotalCount", nf.format(per_workOrderTotalCount));
				returnMap.put("per_peopleTotalCount", nf.format(per_peopleTotalCount));
				returnMap.put("per_carTotalCount", nf.format(per_carTotalCount));
				
				json=gson.toJson(returnMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		xxx
		
		return json;
	}
	
	
	
	/**
	 * 按项目获取对应的每百个基站的人/车/任务量统计分析的环比数据
	 * @param userId
	 * @param yearMonthList
	 * @return
	 */
	public String getAnalyseFaultCountAndResourceBalanceReportLoopCompareDataByOrgIdForProject(String userId,List<String> yearMonthList){
		
		String json="";
		List<Map<String, Object>> dataList=null;
		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
		Map<String,Object> yAxis_peopleCountMap=new HashMap<String,Object>();
		Map<String,Object> yAxis_carCountMap=new HashMap<String,Object>();
		Map<String,Object> yAxis_workOrderCountMap=new HashMap<String,Object>();
		
		List<Double> peopleCount_yAxis=new ArrayList<Double>();
		List<Double> carCount_yAxis=new ArrayList<Double>();
		List<Double> workOrderCount_yAxis=new ArrayList<Double>();
		
		List<String> dateName_xAxis=new ArrayList<String>();
		
		SysOrg topOrg=null;
		//List<ProviderOrganization> topOrgList=this.providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topOrgList=this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		
		if(topOrgList!=null && !topOrgList.isEmpty()){
			topOrg=topOrgList.get(0);
		}
		
		//获取人员相关的组织列表
		//List<ProviderOrganization> orgList=this.providerOrganizationService.getOrgByAccountService(userId);
		//yuan.yw
		List<SysOrg> orgList=this.sysOrganizationService.getOrgByAccountService(userId);
		
		List<Map<String, String>> projectList=new ArrayList<Map<String, String>>();	//项目列表
		for(SysOrg org:orgList){
			//根据组织获取项目
			//List<Map<String, String>> osrList = this.organizationService.getProjectToDownwardOrgByOrgIdService(org.getId());
			//yuan.yw
			List<Map<String, String>> osrList = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(org.getOrgId());

			if(osrList!=null && !osrList.isEmpty()){
				projectList.addAll(osrList);
			}
		}
		
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
			
			
			
			//根据项目获取基站数
			double baseStationCount=0d;
			double workOrderCount=0d;
			double peopleCount=0d;
			double carCount=0d;
			
			//获取当月人员数、车辆数
			dataList = this.getAnalyseFaultCountAndResourceBalanceReportLoopCompareDataByCondition(beginTime, endTime);
			
			if(dataList!=null && !dataList.isEmpty()){
				for(Map<String,Object> tempMap:dataList){
					peopleCount=Double.valueOf(tempMap.get("peopleCount")==null?"0":tempMap.get("peopleCount").toString());
					carCount=Double.valueOf(tempMap.get("carCount")==null?"0":tempMap.get("carCount").toString());
				}
			}
			
			//遍历项目，获取当月所有项目的基站数、工单数
			if(projectList!=null && !projectList.isEmpty()){
				for(Map<String, String> p:projectList){
					if(p!=null && !p.isEmpty()){
						String projectId = p.get("id")==null?"":p.get("id").toString();
						String projectName=p.get("NAME")==null?"":p.get("NAME").toString();
						
						
						//根据项目获取实施组织
						List<String> operationOrgList=this.informationManageNetworkResourceService.findOrgIdByProjectId(projectId);
						
						//根据项目获取工单数
						List<Map<String, Object>> workOrderList=this.urgentRepairReportService.getUrgentRepairBylatestAllowedTimeAndJudgeProject(Long.valueOf(projectId), beginTime, endTime, null, null, null);
						if(workOrderList!=null && !workOrderList.isEmpty()){
							workOrderCount=workOrderCount+workOrderList.size();
						}
						
						//获取基站数
						List<Map<String,String>> findProjectResourceByProjectId = this.informationManageNetworkResourceDao.findAreaIdAndResourceTypeByProjectIdWithoutSame(projectId);
						if(findProjectResourceByProjectId!=null && !findProjectResourceByProjectId.isEmpty()){
							Map<String, List<Map<String,String>>> reMap = new HashMap<String, List<Map<String,String>>>();
							for(Map<String,String> m:findProjectResourceByProjectId){
								String areaId = m.get("areaId"); 
								String resourceType = m.get("resourceType");
								if("GeneralBaseStation".equals(resourceType)){
									List<Map<String,String>> baseStationByAreaIdAndReType = this.networkResourceInterfaceService.getBaseStationByAreaIdAndReType(areaId, resourceType);
									if(baseStationByAreaIdAndReType!=null && !baseStationByAreaIdAndReType.isEmpty()){
										baseStationCount=baseStationCount+baseStationByAreaIdAndReType.size();
									}
								}
							}
						}
					}
				}
			}
			
			//统计每百基站数据
			if(baseStationCount!=0){
				peopleCount_yAxis.add(Double.valueOf(nf.format(((peopleCount/baseStationCount))*100)));
				carCount_yAxis.add(Double.valueOf(nf.format((carCount/baseStationCount)*100)));
				workOrderCount_yAxis.add(Double.valueOf(nf.format((workOrderCount/baseStationCount)*100)));
			}else{
				peopleCount_yAxis.add(0d);
				carCount_yAxis.add(0d);
				workOrderCount_yAxis.add(0d);
			}
			
			dateName_xAxis.add(str);
		}
		
		yAxis_workOrderCountMap.put("name", "工单数");
		yAxis_workOrderCountMap.put("data",workOrderCount_yAxis);
		yAxis.add(yAxis_workOrderCountMap);
		
		yAxis_peopleCountMap.put("name", "人员");
		yAxis_peopleCountMap.put("data", peopleCount_yAxis);
		yAxis_peopleCountMap.put("type","line");
		yAxis_peopleCountMap.put("yAxis", 1);
		yAxis.add(yAxis_peopleCountMap);
		
		yAxis_carCountMap.put("name", "车辆");
		yAxis_carCountMap.put("data", carCount_yAxis);
		yAxis_carCountMap.put("type","line");
		yAxis_carCountMap.put("yAxis", 1);
		yAxis.add(yAxis_carCountMap);
		
		resultMap.put("topOrgName", topOrg.getName());	//组织名称
		resultMap.put("topOrgId", topOrg.getOrgId());	//组织id
		resultMap.put("xAxis", dateName_xAxis);	//xAxis数据
		resultMap.put("yAxis", yAxis);	//yAxis数据
		
		json=gson.toJson(resultMap);
		return json;
	}
	
	
	
	/**
	 * 判断组织是否具有下级组织
	 * @param orgId
	 * @return
	 */
	public String judgeOrgIsExistSubOrg(Long orgId){
		String json="{'isExist':false}";
		try {
			if(orgId!=null && orgId.intValue()>0){
				Map<String, Object> org =  org = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
				//获取子组织
				//List<ProviderOrganization> subOrgList=this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(org.getId(),"BusinessOrganization");
				//yuan.yw
				List<Map<String,Object>> subOrgList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(Long.parseLong((String)org.get("id")),"BusinessOrganization");
				if(subOrgList!=null && !subOrgList.isEmpty()){
					json="{'isExistSubOrg':true}";
				}else{
					json="{'isExistSubOrg':false}";
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	/**
	 * 递归获取子组织的数据
	 * @param orgId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<Map<String,Object>> getAnalyseFaultCountAndResourceBalanceDataByOrgIdAndTime(Map<String,Object> org,String beginTime,String endTime){
		List<Map<String,Object>> resList=null;
		
		Map<String,Object> self=org;
		
		//递归获取下级组织集合
		//List<ProviderOrganization> subProviderOrgByOrgIdService = providerOrganizationService.getOrgListDownwardByOrg(org.getId());
		//yuan.yw
		List<Map<String,Object>> subProviderOrgByOrgIdService = this.sysOrganizationService.getOrgListMapDownwardByOrg(Long.valueOf(org.get("orgId")+""));
		if(subProviderOrgByOrgIdService!=null && !subProviderOrgByOrgIdService.isEmpty()){
			subProviderOrgByOrgIdService.add(self);
			resList=this.analyseFaultCountAndResourceBalanceReportDao.getAnalyseFaultCountAndResourceBalanceData(subProviderOrgByOrgIdService, beginTime, endTime);
		}
		return resList;
	}
	
	
	private List<Map<String,Object>> getAnalyseFaultCountAndResourceBalanceReportLoopCompareDataByCondition(String beginTime,String endTime){
		return this.analyseFaultCountAndResourceBalanceReportDao.getAnalyseFaultCountAndResourceBalanceLoopCompareData(beginTime, endTime);
	}
	
	
	
	/**
	 * 按项目获取
	 * @param targetOrgIdList
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String,Object>> getAnalyseFaultCountAndResourceBalanceDataByTimeForProject(List<String> targetOrgIdList ,String beginTime,String endTime){
		List<Map<String,Object>> resList=null;
		resList=this.analyseFaultCountAndResourceBalanceReportDao.getAnalyseFaultCountAndResourceBalanceDataForProject(targetOrgIdList, beginTime, endTime);
		return resList;
	}
	
	public List<Map<String,Object>> getAnalyseFaultCountAndResourceBalanceLoopCompareDataDataByTimeForProject(String beginTime,String endTime){
		List<Map<String,Object>> resList=null;
		resList=this.analyseFaultCountAndResourceBalanceReportDao.getAnalyseFaultCountAndResourceBalanceLoopCompareDataForProject(beginTime, endTime);
		return resList;
	}
	
	
	
	
	/**
	 * 获取用户最高级组织
	 * @param userId
	 * @return
	 */
	public String getUserTopOrg(String userId){
		String json="";
		//List<ProviderOrganization> list = this.providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> list = this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		
		if(list!=null){
			SysOrg org=list.get(0);
			if(org!=null){
				json=gson.toJson(org);
			}
		}
		return json;
	}
	
	/**
	 * 获取父级组织
	 * @param orgId
	 * @return
	 */
	public String getParentOrg(long orgId){
		String json="";
		//List<ProviderOrganization> list = this.providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		List<SysOrg> list = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");
		if(list!=null){
			SysOrg org=list.get(0);
			if(org!=null){
				json=gson.toJson(org);
			}
		}
		return json;
	}
	
	

}
