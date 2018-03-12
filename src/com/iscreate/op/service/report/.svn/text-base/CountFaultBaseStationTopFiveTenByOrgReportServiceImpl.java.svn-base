package com.iscreate.op.service.report;

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
import com.iscreate.op.dao.report.CountFaultBaseStationTopFiveTenByOrgReportDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.system.SysOrganizationService;

public class CountFaultBaseStationTopFiveTenByOrgReportServiceImpl implements
		CountFaultBaseStationTopFiveTenByOrgReportService {

	private CountFaultBaseStationTopFiveTenByOrgReportDao countFaultBaseStationTopFiveTenByOrgReportDao;
	
	private NetworkResourceInterfaceService networkResourceInterfaceService;
	
	private SysOrganizationDao sysOrganizationDao;//du.hw添加
	private static Gson gson=new Gson();
	
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
	
	public void setCountFaultBaseStationTopFiveTenByOrgReportDao(
			CountFaultBaseStationTopFiveTenByOrgReportDao countFaultBaseStationTopFiveTenByOrgReportDao) {
		this.countFaultBaseStationTopFiveTenByOrgReportDao = countFaultBaseStationTopFiveTenByOrgReportDao;
	}

	

	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}

	


	/**
	 * 获取基站故障数最差分布的数据
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenDataByUserId(String userId,String beginTime,String endTime){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		SysOrg topOrg;
		String json="";
		
//		aaaa
		try {
			//List<ProviderOrganization> orgList=this.providerOrganizationService.getTopLevelOrgByAccount(userId);
			List<SysOrg> orgList = this.sysOrganizationService.getTopLevelOrgByAccount(userId);
			if(orgList!=null && !orgList.isEmpty()){
				topOrg=orgList.get(0);
				if(topOrg!=null){
					List<Long> orgId_xAxis=new ArrayList<Long>();
					List<String> orgName_xAxis=new ArrayList<String>();
					List<Long> baseStationCount_yAxis=new ArrayList<Long>();	//故障基站数
					
					//获取组织下所有（递归）子组织
					//List<ProviderOrganization> subProviderOrgByOrgIdService = providerOrganizationService.getOrgListDownwardByOrg(topOrg.getId());
					//yuan.yw
					List<SysOrg> subProviderOrgByOrgIdService = this.sysOrganizationService.getOrgListDownwardByOrg(topOrg.getOrgId());
					
					//获取子组织集合
//					List<ProviderOrganization> subOrgList=this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(topOrg.getId(),"BusinessOrganization");
					//List<ProviderOrganization> subOrgList=this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(topOrg.getOrgId(),OrganizationConstant.BUSINESS_ORGANIZATION);
					//yuan.yw
					List<Map<String,Object>> subOrgList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(topOrg.getOrgId(),"BusinessOrganization");
				
					long baseStationTotalCount=0;
					if(subOrgList!=null && !subOrgList.isEmpty()){
						for(Map<String,Object> organization:subOrgList){
							if(!OrganizationConstant.ORGANIZATION_FUNCTIONDIVISION.equals(organization.get("type")+"")){
								String orgName=organization.get("name")+"";
								orgId_xAxis.add(Long.valueOf(organization.get("orgId")+""));
								orgName_xAxis.add(orgName);
								
								//获取组织对应的数据
								List<Map<String,Object>> orgResList=this.getCountFaultBaseStationTopFiveTenDataByOrg(subProviderOrgByOrgIdService, organization, beginTime, endTime);
								if(orgResList!=null && !orgResList.isEmpty()){
									for(Map<String,Object> tempMap:orgResList){
										if(tempMap!=null){
											long baseStationCount=Long.valueOf(tempMap.get("baseStationCount")==null?"0":tempMap.get("baseStationCount").toString());
											
											baseStationTotalCount=baseStationTotalCount+baseStationCount;
											baseStationCount_yAxis.add(baseStationCount);
										}
									}
								}
							}
							List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
							Map<String,Object> yAxis_workOrderCountMap=new HashMap<String,Object>();
							yAxis_workOrderCountMap.put("name", "基站数");
							yAxis_workOrderCountMap.put("data", baseStationCount_yAxis);
							yAxis.add(yAxis_workOrderCountMap);
							
							if(topOrg!=null){
								resultMap.put("topOrgName", topOrg.getName());	//组织名称
								resultMap.put("topOrgId", topOrg.getOrgId());	//组织id
							}
							resultMap.put("xAxis", orgName_xAxis);	//xAxis数据
							resultMap.put("yAxis", yAxis);	//yAxis数据
							
							resultMap.put("orgId_xAxis", orgId_xAxis);
							resultMap.put("baseStationTotalCount", baseStationTotalCount);
							
							json=gson.toJson(resultMap);
							
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	/**
	 * 获取下级组织对应的基站故障数最差分布的数据
	 * @param orgId 组织id
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenDataBySubOrgId(Long orgId,String beginTime,String endTime){
		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		Map<String,Object> topOrg;
		String json="";
		try {
			if(orgId!=null && orgId.intValue()>0){
				topOrg = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
					
				if(topOrg!=null){
					//获取组织下所有（递归）子组织
					//List<ProviderOrganization> subProviderOrgByOrgIdService = providerOrganizationService.getOrgListDownwardByOrg(topOrg.getId());
					//yuan.yw
					List<SysOrg> subProviderOrgByOrgIdService = this.sysOrganizationService.getOrgListDownwardByOrg(Long.parseLong((String) topOrg.get("id")));
					//获取子组织集合

					//List<ProviderOrganization> subOrgList=this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(Long.parseLong((String)topOrg.get("id")),OrganizationConstant.BUSINESS_ORGANIZATION);
					//List<ProviderOrganization> subOrgList=this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(topOrg.getId(),OrganizationConstant.BUSINESS_ORGANIZATION);
					//yuan.yw
					List<Map<String,Object>> subOrgList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(Long.parseLong((String)topOrg.get("id")),"BusinessOrganization");
				

					List<Long> orgId_xAxis=new ArrayList<Long>();
					List<String> orgName_xAxis=new ArrayList<String>();
					List<Long> baseStationCount_yAxis=new ArrayList<Long>();	//故障基站数
					
					long baseStationTotalCount=0;
					
					if(subOrgList!=null && !subOrgList.isEmpty()){
						for(Map<String,Object> organization:subOrgList){
							
							String orgName=organization.get("name")+"";
							orgId_xAxis.add(Long.valueOf(organization.get("orgId")+""));
							orgName_xAxis.add(orgName);
							
							//获取组织对应的数据
							List<Map<String,Object>> orgResList=this.getCountFaultBaseStationTopFiveTenDataByOrg(subProviderOrgByOrgIdService,organization,beginTime,endTime);
							if(orgResList!=null && !orgResList.isEmpty()){
								for(Map<String,Object> tempMap:orgResList){
									if(tempMap!=null){
										long baseStationCount=Long.valueOf(tempMap.get("baseStationCount")==null?"0":tempMap.get("baseStationCount").toString());
										
										baseStationTotalCount=baseStationTotalCount+baseStationCount;
										baseStationCount_yAxis.add(baseStationCount);
									}
								}
							}
						}
					}
					
					List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
					
					Map<String,Object> yAxis_workOrderCountMap=new HashMap<String,Object>();
					yAxis_workOrderCountMap.put("name", "基站数");
					yAxis_workOrderCountMap.put("data", baseStationCount_yAxis);
					yAxis.add(yAxis_workOrderCountMap);
					
					if(topOrg!=null){
						resultMap.put("topOrgName", topOrg.get("name"));	//组织名称
						resultMap.put("topOrgId", topOrg.get("id"));	//组织id
					}
					resultMap.put("xAxis", orgName_xAxis);	//xAxis数据
					resultMap.put("yAxis", yAxis);	//yAxis数据
					
					resultMap.put("orgId_xAxis", orgId_xAxis);
					resultMap.put("baseStationTotalCount", baseStationTotalCount);
					
					json=gson.toJson(resultMap);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	/**
	 * 获取组织对应的基站故障数最差分布的环比数据
	 * @param userId
	 * @param yearMonthList 年月List
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenReportLoopCompareDataByUserId(String userId,List<String> yearMonthList){
		String json="";
		SysOrg topOrg;
		int dateIndex=0;
		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		
		List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();	//集合里面的每个map对应每1条线
		List<String> dateName_xAxis=new ArrayList<String>();	//xAxis轴
		
		//获取人员最高级组织架构
		//List<ProviderOrganization> topOrgList = providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topOrgList = this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		if(topOrgList!=null && !topOrgList.isEmpty()){
			topOrg=topOrgList.get(0);
			
			if(topOrg!=null){

//				Map<String,List<Long>> map_orgResList=new LinkedHashMap<String, List<Long>>();
				
				//获取组织下所有（递归）子组织
				//List<ProviderOrganization> subProviderOrgByOrgIdService = providerOrganizationService.getOrgListDownwardByOrg(topOrg.getId());
				//yuan.yw
				List<SysOrg> subProviderOrgByOrgIdService = this.sysOrganizationService.getOrgListDownwardByOrg(topOrg.getOrgId());
				//获取子组织集合
				//List<ProviderOrganization> subOrgList=this.providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(topOrg.getOrgId(),OrganizationConstant.BUSINESS_ORGANIZATION);
				//yuan.yw
				List<Map<String,Object>> subOrgList=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(topOrg.getOrgId(),"BusinessOrganization");
			
				if(subOrgList!=null && !subOrgList.isEmpty()){
					for(Map<String,Object> org:subOrgList){
						if(!OrganizationConstant.ORGANIZATION_FUNCTIONDIVISION.equals(org.get("type")+"")){
//							long tempOrgId=org.getId();
							String tempOrgName=org.get("name")+"";
							
							Map<String,Object> tempCountMap=new HashMap<String,Object>();
							tempCountMap.put("name", tempOrgName);
							
							List<Long> baseStationCountList=new ArrayList<Long>();
							
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
									
									List<Map<String, Object>> dataList = this.getCountFaultBaseStationTopFiveTenLoopCompareDataByOrgIdAndTime(subProviderOrgByOrgIdService,org,beginTime, endTime);
									//System.out.println(org.getId()+"   "+dataList + " " + beginTime + " " +endTime);
									if(dataList!=null && !dataList.isEmpty()){
										if(dataList!=null && !dataList.isEmpty()){
											for(Map<String,Object> tempMap:dataList){
												if(tempMap!=null){
													long baseStationCount=Long.valueOf(tempMap.get("baseStationCount")==null?"0":tempMap.get("baseStationCount").toString());
													baseStationCountList.add(baseStationCount);
												}
											}
										}
									}
									dateIndex++;
								}
								tempCountMap.put("data",baseStationCountList);
							}
							yAxis.add(tempCountMap);
						}
					}
					
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
							dateName_xAxis.add(str);
						}
					}
					
					if(topOrg!=null){
						resultMap.put("topOrgName", topOrg.getName());	//组织名称
						resultMap.put("topOrgId", topOrg.getOrgId());	//组织id
					}
					resultMap.put("xAxis", dateName_xAxis);	//xAxis数据
					resultMap.put("yAxis", yAxis);	//yAxis数据
					
					json=gson.toJson(resultMap);
				}
			}
		}
		return json;
	}

	
	/**
	 * 按地域获取基站故障数最差分布的数据
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenDataByUserIdForArea(String userId,String beginTime,String endTime){
		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String json="";
		
		//获取当前登录人所在组织对应的区域
		List<Map<String,String>> areaListByAccount = this.networkResourceInterfaceService.getAreaByAccountService(userId);
		
		List<Long> areaId_xAxis=new ArrayList<Long>();
		List<String> areaName_xAxis=new ArrayList<String>();
		List<Long> baseStationCount_yAxis=new ArrayList<Long>();	//故障基站数
		long baseStationTotalCount=0;
		
		if(areaListByAccount!=null && !areaListByAccount.isEmpty()){
			
			//递归获取当前登录人所在组织对应的区域的其子区域
			List<Map<String,String>> allAreaList=new ArrayList<Map<String,String>>();
			List<String> allAreaIdList=new ArrayList<String>();
			if(areaListByAccount!=null && !areaListByAccount.isEmpty()){
				for(Map<String,String> map:areaListByAccount){
					String areaId = map.get("id")==null?"0":map.get("id").toString();
					List<Map<String, String>> allSubAreaList=this.networkResourceInterfaceService.getDownwardOnSelfResourceService(areaId, "Sys_Area", "Sys_Area");
					if(allSubAreaList!=null && !allSubAreaList.isEmpty()){
						for(Map<String,String> tempMap:allSubAreaList){
							allAreaList.add(tempMap);
						}
					}
				}
				
				for(Map<String,String> map:allAreaList){
					String areaId = map.get("id")==null?"0":map.get("id").toString();
					allAreaIdList.add(areaId);
				}
				
//				System.out.println("区域大小==="+allAreaIdList.size());
				
				for(Map<String,String> map:areaListByAccount){
					String areaId = map.get("id")==null?"0":map.get("id").toString();
					String areaName = map.get("name")==null?"0":map.get("name").toString();
					
					areaId_xAxis.add(Long.valueOf(areaId));
					areaName_xAxis.add(areaName);
					
					//获取区域对应的数据
					List<Map<String,Object>> areaResList=this.getCountFaultBaseStationTopFiveTenDataByOrgIdAndTimeForArea(allAreaIdList, areaId, beginTime, endTime);
					if(areaResList!=null && !areaResList.isEmpty()){
						for(Map<String,Object> tempMap:areaResList){
							if(tempMap!=null){
								long baseStationCount=Long.valueOf(tempMap.get("baseStationCount")==null?"0":tempMap.get("baseStationCount").toString());
								baseStationTotalCount=baseStationTotalCount+baseStationCount;
								baseStationCount_yAxis.add(baseStationCount);
							}
						}
					}
				}
				
				List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
				Map<String,Object> yAxis_workOrderCountMap=new HashMap<String,Object>();
				yAxis_workOrderCountMap.put("name", "基站数");
				yAxis_workOrderCountMap.put("data", baseStationCount_yAxis);
				yAxis.add(yAxis_workOrderCountMap);
				
				resultMap.put("xAxis", areaName_xAxis);	//xAxis数据
				resultMap.put("yAxis", yAxis);	//yAxis数据
				
				resultMap.put("areaId_xAxis", areaId_xAxis);
				resultMap.put("baseStationTotalCount", baseStationTotalCount);
				json=gson.toJson(resultMap);
			}

		}
		return json;
	}
	
	
	
	/**
	 * 按地域获取下级区域对应的基站故障数最差分布的数据
	 * @param orgId 组织id
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenDataBySubAreaIdForArea(Long areaId,String beginTime,String endTime){
		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String json="";
		try {
			if(areaId!=null && areaId.intValue()>0){
				
				//获取区域对应的子区域
				List<Map<String, String>> subArealist = this.networkResourceInterfaceService.getResourceService(String.valueOf(areaId), "Sys_Area", "Sys_Area", "CHILD");
				
				List<Long> areaId_xAxis=new ArrayList<Long>();
				List<String> areaName_xAxis=new ArrayList<String>();
				List<Long> baseStationCount_yAxis=new ArrayList<Long>();	//故障基站数
				long baseStationTotalCount=0;
				
				if(subArealist!=null && !subArealist.isEmpty()){
					
					//递归获取区域对应的其子区域
					List<Map<String,String>> allAreaList=new ArrayList<Map<String,String>>();
					List<String> allAreaIdList=new ArrayList<String>();
					
					for(Map<String, String> subArea:subArealist){
						String subAreaId=subArea.get("id")==null?"0":subArea.get("id").toString();
						List<Map<String, String>> allSubAreaList=this.networkResourceInterfaceService.getDownwardOnSelfResourceService(subAreaId, "Sys_Area", "Sys_Area");
						if(allSubAreaList!=null && !allSubAreaList.isEmpty()){
							for(Map<String,String> tempMap:allSubAreaList){
								allAreaList.add(tempMap);
							}
						}
					}
					
					for(Map<String,String> map:allAreaList){
						String id = map.get("id")==null?"0":map.get("id").toString();
						allAreaIdList.add(id);
					}
					
					for(Map<String,String> map:subArealist){
						String tempAreaId = map.get("id")==null?"0":map.get("id").toString();
						String areaName = map.get("name")==null?"0":map.get("name").toString();
						
						areaId_xAxis.add(Long.valueOf(tempAreaId));
						areaName_xAxis.add(areaName);
						
						//获取区域对应的数据
						List<Map<String,Object>> areaResList=this.getCountFaultBaseStationTopFiveTenDataByOrgIdAndTimeForArea(allAreaIdList, tempAreaId, beginTime, endTime);
						if(areaResList!=null && !areaResList.isEmpty()){
							for(Map<String,Object> tempMap:areaResList){
								if(tempMap!=null){
									long baseStationCount=Long.valueOf(tempMap.get("baseStationCount")==null?"0":tempMap.get("baseStationCount").toString());
									baseStationTotalCount=baseStationTotalCount+baseStationCount;
									baseStationCount_yAxis.add(baseStationCount);
								}
							}
						}
					}
					
					List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
					Map<String,Object> yAxis_workOrderCountMap=new HashMap<String,Object>();
					yAxis_workOrderCountMap.put("name", "基站数");
					yAxis_workOrderCountMap.put("data", baseStationCount_yAxis);
					yAxis.add(yAxis_workOrderCountMap);
					
					resultMap.put("xAxis", areaName_xAxis);	//xAxis数据
					resultMap.put("yAxis", yAxis);	//yAxis数据
					
					resultMap.put("areaId_xAxis", areaId_xAxis);
					resultMap.put("baseStationTotalCount", baseStationTotalCount);
					resultMap.put("isExistSubArea", true);
				}else{
					resultMap.put("isExistSubArea", false);
				}
				json=gson.toJson(resultMap);
				
				//旧的--------begin
//				//获取子区域集合
//				List<Map<String, String>> subArealist = this.networkResourceInterfaceService.getResourceService(String.valueOf(areaId), "Sys_Area", "Sys_Area", "CHILD");
//				
//				List<Long> areaId_xAxis=new ArrayList<Long>();
//				List<String> areaName_xAxis=new ArrayList<String>();
//				List<Long> baseStationCount_yAxis=new ArrayList<Long>();	//故障基站数
//				
//				long baseStationTotalCount=0;
//				
//				if(subArealist!=null && !subArealist.isEmpty()){
//					for(Map<String,String> map:subArealist){
//						String id = map.get("id")==null?"0":map.get("id").toString();
//						String areaName = map.get("name")==null?"0":map.get("name").toString();
//						
//						areaId_xAxis.add(Long.valueOf(id));
//						areaName_xAxis.add(areaName);
//						
//						//获取区域对应的数据
//						List<Map<String,Object>> areaResList=this.getCountFaultBaseStationTopFiveTenDataByOrgIdAndTimeForArea(id, beginTime, endTime);
//						if(areaResList!=null && !areaResList.isEmpty()){
//							for(Map<String,Object> tempMap:areaResList){
//								if(tempMap!=null){
//									long baseStationCount=Long.valueOf(tempMap.get("baseStationCount")==null?"0":tempMap.get("baseStationCount").toString());
//									
////									baseStationCount=1l;	//hardcode
//									baseStationTotalCount=baseStationTotalCount+baseStationCount;
//									baseStationCount_yAxis.add(baseStationCount);
//								}
//							}
//						}
//					}
//					
//					List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
//					Map<String,Object> yAxis_workOrderCountMap=new HashMap<String,Object>();
//					yAxis_workOrderCountMap.put("name", "基站数");
//					yAxis_workOrderCountMap.put("data", baseStationCount_yAxis);
//					yAxis.add(yAxis_workOrderCountMap);
//					
//					resultMap.put("xAxis", areaName_xAxis);	//xAxis数据
//					resultMap.put("yAxis", yAxis);	//yAxis数据
//					
//					resultMap.put("areaId_xAxis", areaId_xAxis);
//					resultMap.put("baseStationTotalCount", baseStationTotalCount);
//					json=gson.toJson(resultMap);
//				}
				//旧的-----end
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	
	
	
	/**
	 * 按地域获取区域对应的基站故障数最差分布的环比数据
	 * @param userId
	 * @param yearMonthList 年月List
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenReportLoopCompareDataByUserIdForArea(String userId,List<String> yearMonthList){
		
		String json="";
		int dateIndex=0;
		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		
		List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();	//集合里面的每个map对应每1条线
		List<String> dateName_xAxis=new ArrayList<String>();	//xAxis轴
		
		//获取当前登录人所在组织对应的区域
		List<Map<String,String>> areaListByAccount = this.networkResourceInterfaceService.getAreaByAccountService(userId);
		
		if(areaListByAccount!=null && !areaListByAccount.isEmpty()){
			//递归获取当前登录人所在组织对应的区域的其子区域
			List<Map<String,String>> allAreaList=new ArrayList<Map<String,String>>();
			List<String> allAreaIdList=new ArrayList<String>();
			if(areaListByAccount!=null && !areaListByAccount.isEmpty()){
				for(Map<String,String> map:areaListByAccount){
					String areaId = map.get("id")==null?"0":map.get("id").toString();
					List<Map<String, String>> allSubAreaList=this.networkResourceInterfaceService.getDownwardOnSelfResourceService(areaId, "Sys_Area", "Sys_Area");
					if(allSubAreaList!=null && !allSubAreaList.isEmpty()){
						for(Map<String,String> tempMap:allSubAreaList){
							allAreaList.add(tempMap);
						}
					}
				}
				
				for(Map<String,String> map:allAreaList){
					String areaId = map.get("id")==null?"0":map.get("id").toString();
					allAreaIdList.add(areaId);
				}
				
				for(Map<String,String> map:areaListByAccount){
					String areaId = map.get("id")==null?"0":map.get("id").toString();
					String areaName = map.get("name")==null?"0":map.get("name").toString();
					
					Map<String,Object> tempCountMap=new HashMap<String,Object>();
					tempCountMap.put("name", areaName);
					
					List<Long> baseStationCountList=new ArrayList<Long>();
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
//							if(dateIndex<6){
//								dateName_xAxis.add(str);
//							}
							List<Map<String, Object>> dataList = this.getCountFaultBaseStationTopFiveTenDataByOrgIdAndTimeForArea(allAreaIdList, areaId, beginTime, endTime);
							if(dataList!=null && !dataList.isEmpty()){
								if(dataList!=null && !dataList.isEmpty()){
									for(Map<String,Object> tempMap:dataList){
										if(tempMap!=null){
											long baseStationCount=Long.valueOf(tempMap.get("baseStationCount")==null?"0":tempMap.get("baseStationCount").toString());
											baseStationCountList.add(baseStationCount);
										}
									}
								}
							}
							dateIndex++;
						}
						tempCountMap.put("data",baseStationCountList);
					}
					yAxis.add(tempCountMap);
				}
			}
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
					dateName_xAxis.add(str);
				}
			}
			
			resultMap.put("xAxis", dateName_xAxis);	//xAxis数据
			resultMap.put("yAxis", yAxis);	//yAxis数据
			json=gson.toJson(resultMap);
		}
		return json;
	}
	
	
	/**
	 * 按项目获取基站故障数最差分布的数据
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenDataByUserIdForProject(String userId,String beginTime,String endTime){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String json="";
		
		//List<ProviderOrganization> topOrgList=this.providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topOrgList=this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		
		SysOrg topOrg;
		if(topOrgList!=null && !topOrgList.isEmpty()){
			topOrg=topOrgList.get(0);
			
			if(topOrg!=null){
				
				List<String> projectId_xAxis=new ArrayList<String>();
				List<String> projectName_xAxis=new ArrayList<String>();
				List<Long> baseStationCount_yAxis=new ArrayList<Long>();	//故障基站数
				long baseStationTotalCount=0;
				
				//获取人员相关的组织列表
				//List<ProviderOrganization> orgList=this.providerOrganizationService.getOrgByAccountService(userId);
				//yuan.yw
				List<SysOrg> orgList=this.sysOrganizationService.getOrgByAccountService(userId);
				
				if(orgList!=null && !orgList.isEmpty()){
					List<Map<String, String>> projectList=new ArrayList<Map<String, String>>();
					for(SysOrg org:orgList){
						//根据组织获取项目
						//List<Map<String, String>> osrList = this.organizationService.getProjectToDownwardOrgByOrgIdService(org.getId());
						//yuan.yw
						List<Map<String, String>> osrList = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(org.getOrgId());
						if(osrList!=null && !osrList.isEmpty()){
							projectList.addAll(osrList);
						}
					}
					
					List<String> projectIdList=new ArrayList<String>();
					if(projectList!=null && !projectList.isEmpty()){
						for(Map<String, String> p:projectList){
							String projectId = p.get("id")==null?"":p.get("id").toString();
							if(projectId!=null && !projectId.isEmpty()){
								projectIdList.add(projectId);
							}
						}
						
						for(Map<String, String> projectMap:projectList){
							String projectId=projectMap.get("id")==null?"":projectMap.get("id").toString();
							String projectName=projectMap.get("NAME")==null?"":projectMap.get("NAME").toString();
							
							projectId_xAxis.add(projectId);
							projectName_xAxis.add(projectName);
							
							//获取数据
							List<Map<String,Object>> resList=this.getCountFaultBaseStationTopFiveTenDataByProject(projectIdList, projectId, beginTime, endTime);
							if(resList!=null && !resList.isEmpty()){
								for(Map<String,Object> tempMap:resList){
									if(tempMap!=null){
										long baseStationCount=Long.valueOf(tempMap.get("baseStationCount")==null?"0":tempMap.get("baseStationCount").toString());
										baseStationTotalCount=baseStationTotalCount+baseStationCount;
										baseStationCount_yAxis.add(baseStationCount);
									}
								}
							}else{
								baseStationCount_yAxis.add(0l);
							}
						}
					}
					
					List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
					Map<String,Object> yAxis_map=new HashMap<String,Object>();
					yAxis_map.put("name", "故障数");
					yAxis_map.put("data", baseStationCount_yAxis);
					yAxis.add(yAxis_map);
					
					if(topOrg!=null){
						resultMap.put("topOrgName", topOrg.getName());	//组织名称
						resultMap.put("topOrgId", topOrg.getOrgId());	//组织id
					}

					resultMap.put("xAxis", projectName_xAxis);	//项目名称
					resultMap.put("yAxis", yAxis);	//基站故障数
					json=gson.toJson(resultMap);
				}
			}
		}
		
//		xxx
		return json;
		
	}
	
	
	
	/**
	 * 按项目获取基站故障数最差分布的环比数据
	 * @param userId
	 * @param yearMonthList
	 * @return
	 */
	public String getCountFaultBaseStationTopFiveTenReportLoopCompareDataByOrgIdForProject(String userId,List<String> yearMonthList){
		
		String json="";
		int dateIndex=0;
		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		
		List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();	//集合里面的每个map对应每1条线
		List<String> dateName_xAxis=new ArrayList<String>();	//xAxis轴
		
		//List<ProviderOrganization> topOrgList=this.providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> topOrgList=this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		
		SysOrg topOrg=null;
		if(topOrgList!=null && !topOrgList.isEmpty()){
			topOrg=topOrgList.get(0);
		}
		
		
		//获取人员相关的组织列表
		//List<ProviderOrganization> orgList=this.providerOrganizationService.getOrgByAccountService(userId);
		//yuan.yw
		List<SysOrg> orgList=this.sysOrganizationService.getOrgByAccountService(userId);
		
		if(orgList!=null && !orgList.isEmpty()){
			List<Map<String, String>> projectList=new ArrayList<Map<String, String>>();
			for(SysOrg org:orgList){
				//根据组织获取项目
				//List<Map<String, String>> osrList = this.organizationService.getProjectToDownwardOrgByOrgIdService(org.getId());
				//yuan.yw
				List<Map<String, String>> osrList = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(org.getOrgId());
				if(osrList!=null && !osrList.isEmpty()){
					projectList.addAll(osrList);
				}
			}
			
			List<String> projectIdList=new ArrayList<String>();
			if(projectList!=null && !projectList.isEmpty()){
				for(Map<String, String> p:projectList){
					String projectId = p.get("id")==null?"":p.get("id").toString();
					if(projectId!=null && !projectId.isEmpty()){
						projectIdList.add(projectId);
					}
				}
				for(Map<String, String> projectMap:projectList){
					
					
					String projectId=projectMap.get("id")==null?"":projectMap.get("id").toString();
					String projectName=projectMap.get("NAME")==null?"":projectMap.get("NAME").toString();
					
					Map<String,Object> tempCountMap=new HashMap<String,Object>();
					tempCountMap.put("name", projectName);
					
					List<Long> baseStationCountList=new ArrayList<Long>();
					
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
							
							List<Map<String, Object>> dataList = this.getCountFaultBaseStationTopFiveTenDataByProject(projectIdList, projectId, beginTime, endTime);
							if(dataList!=null && !dataList.isEmpty()){
								if(dataList!=null && !dataList.isEmpty()){
									for(Map<String,Object> tempMap:dataList){
										if(tempMap!=null){
											long baseStationCount=Long.valueOf(tempMap.get("baseStationCount")==null?"0":tempMap.get("baseStationCount").toString());
											baseStationCountList.add(baseStationCount);
										}
									}
								}
							}
							dateIndex++;
						}
						tempCountMap.put("data",baseStationCountList);
					}
					yAxis.add(tempCountMap);
					
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
							dateName_xAxis.add(str);
						}
					}
					if(topOrg!=null){
						resultMap.put("topOrgName", topOrg.getName());	//组织名称
						resultMap.put("topOrgId", topOrg.getOrgId());	//组织id
					}
					resultMap.put("xAxis", dateName_xAxis);	//xAxis数据
					resultMap.put("yAxis", yAxis);	//yAxis数据
					
					json=gson.toJson(resultMap);
				}
			}
		}
		
		
		
		
		
		
		
		return json;
	}	
	
	
	
	/**
	 * 获取区域对象
	 * @param areaId
	 * @return
	 */
	public String getAreaInfoByAreaId(long areaId){
		String json="";
		Map<String,Map<String,String>> areaMap = this.networkResourceInterfaceService.getBaseFacilityToMapService(String.valueOf(areaId), "Sys_Area");
		
		Map<String,String> entityMap=areaMap.get("entity");
		json=gson.toJson(entityMap);
		
//		for(Map.Entry<String, Map<String,String>> entry:areaMap.entrySet()){
//			String key=entry.getKey();
//			Map<String,String> map=entry
//		}
		
		return json;
	}
	
	
	
	
	
	/**
	 * 获取父级区域
	 * @param orgId
	 * @return
	 */
	public String getParentArea(long areaId){
		String json="";
		List<Map<String, String>> parentArealist = this.networkResourceInterfaceService.getResourceService(String.valueOf(areaId), "Sys_Area", "Sys_Area", "PARENT");
		if(parentArealist!=null && !parentArealist.isEmpty()){
			Map<String,String> parentMap=parentArealist.get(0);
			json=gson.toJson(parentMap);
		}
		return json;
	}
	
	
	
	/**
	 * 按组织获取
	 * @param allSubOrgWithChildList
	 * @param parentNextChildOrg
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<Map<String,Object>> getCountFaultBaseStationTopFiveTenDataByOrg(List<SysOrg> allSubOrgWithChildList,Map<String,Object> parentNextChildOrg,String beginTime,String endTime){
		
		List<Map<String,Object>> resList=null;
//		获取下级组织集合
		//List<ProviderOrganization> subProviderOrgByOrgIdService =providerOrganizationService.getOrgListDownwardByOrg(Long.valueOf(parentNextChildOrg.get("orgId")+""));
		List<Map<String,Object>> subProviderOrgByOrgIdService =this.sysOrganizationService.getOrgListMapDownwardByOrg(Long.valueOf(parentNextChildOrg.get("orgId")+""));
		
		subProviderOrgByOrgIdService.add(parentNextChildOrg);
		if(subProviderOrgByOrgIdService!=null && !subProviderOrgByOrgIdService.isEmpty()){
			resList=this.countFaultBaseStationTopFiveTenByOrgReportDao.getCountFaultBaseStationTopFiveTenData(allSubOrgWithChildList, subProviderOrgByOrgIdService, beginTime, endTime);
		}
		return resList;
	}
	
	
	/**
	 * 递归获取子组织的环比数据
	 * @param allSubOrgWithChildList
	 * @param parentNextChildOrg
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<Map<String,Object>> getCountFaultBaseStationTopFiveTenLoopCompareDataByOrgIdAndTime(List<SysOrg> allSubOrgWithChildList,Map<String,Object> parentNextChildOrg,String beginTime,String endTime){
		List<Map<String,Object>> resList=null;
//		获取下级组织集合
		//List<ProviderOrganization> subProviderOrgByOrgIdService =providerOrganizationService.getOrgListDownwardByOrg(Long.valueOf(parentNextChildOrg.get("orgId")+""));
		List<Map<String,Object>> subProviderOrgByOrgIdService =this.sysOrganizationService.getOrgListMapDownwardByOrg(Long.valueOf(parentNextChildOrg.get("orgId")+""));
		subProviderOrgByOrgIdService.add(parentNextChildOrg);
		if(subProviderOrgByOrgIdService!=null && !subProviderOrgByOrgIdService.isEmpty()){
			resList=this.countFaultBaseStationTopFiveTenByOrgReportDao.getCountFaultBaseStationTopFiveTenData(allSubOrgWithChildList, subProviderOrgByOrgIdService, beginTime, endTime);
		}
		return resList;
	}
	
	
	
	/**
	 * 递归获取子区域的数据
	 * @param org
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<Map<String,Object>> getCountFaultBaseStationTopFiveTenDataByOrgIdAndTimeForArea(List<String> allSubAreaWithChildList,String targetAreaId,String beginTime,String endTime){
		List<Map<String,Object>> resList=null;
		
		//递归获取下级区域集合
		List<Map<String, String>> targetAllSubAreaList=this.networkResourceInterfaceService.getDownwardOnSelfResourceService(targetAreaId, "Sys_Area", "Sys_Area");
		List<String> targetAllSubAreaIdList=new ArrayList<String>();
		
		if(targetAllSubAreaList!=null && !targetAllSubAreaList.isEmpty()){
			for(Map<String, String> map:targetAllSubAreaList){
				String id = map.get("id")==null?"0":map.get("id").toString();
				targetAllSubAreaIdList.add(id);
			}
		}
		
		if(allSubAreaWithChildList!=null && !allSubAreaWithChildList.isEmpty()){
			resList=this.countFaultBaseStationTopFiveTenByOrgReportDao.getCountFaultBaseStationTopFiveTenDataForArea(allSubAreaWithChildList, targetAllSubAreaIdList, beginTime, endTime);
		}
		return resList;
	}
	
	
	/**
	 * 按项目获取
	 * @param allSubOrgWithChildList
	 * @param targetOrgList
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<Map<String,Object>> getCountFaultBaseStationTopFiveTenDataByProject(List<String> allProjectIdList,String targetProjectId,String beginTime,String endTime){
		List<Map<String,Object>> resList=null;
		resList=this.countFaultBaseStationTopFiveTenByOrgReportDao.getCountFaultBaseStationTopFiveTenDataForProject(allProjectIdList, targetProjectId, beginTime, endTime);
		return resList;
	}
	
	
	/**
	 * 按项目获取环比数据
	 * @param allSubOrgWithChildList
	 * @param targetOrgList
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<Map<String,Object>> getCountFaultBaseStationTopFiveTenLoopCompareDataForProject(String beginTime,String endTime){
		List<Map<String,Object>> resList=null;
//		resList=this.countFaultBaseStationTopFiveTenByOrgReportDao.getCountFaultBaseStationTopFiveTenDataForProject(allSubOrgWithChildList, targetOrgList, beginTime, endTime);
		return resList;
	}
	
	
	
	
	
	
	
	
}
