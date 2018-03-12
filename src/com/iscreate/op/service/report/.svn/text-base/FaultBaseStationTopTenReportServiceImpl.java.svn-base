package com.iscreate.op.service.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.iscreate.op.dao.informationmanage.ProjectInformationDao;
import com.iscreate.op.dao.report.FaultBaseStationTopTenReportDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.system.SysOrganizationService;

public class FaultBaseStationTopTenReportServiceImpl implements
		FaultBaseStationTopTenReportService {

	
	private FaultBaseStationTopTenReportDao faultBaseStationTopTenReportDao;
	private ProjectInformationDao projectInformationDao;
	
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
	
	
	

	public void setFaultBaseStationTopTenReportDao(
			FaultBaseStationTopTenReportDao faultBaseStationTopTenReportDao) {
		this.faultBaseStationTopTenReportDao = faultBaseStationTopTenReportDao;
	}

	
	
	
	public ProjectInformationDao getProjectInformationDao() {
		return projectInformationDao;
	}

	public void setProjectInformationDao(ProjectInformationDao projectInformationDao) {
		this.projectInformationDao = projectInformationDao;
	}

	/**
	 * 根据用户当前所在组织，获取前10基站故障数的数据
	 * @param userId
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getFaultBaseStationTopTenData(String userId,String beginTime,String endTime) {
		List<Map<String, Object>> dataList=null;
		Map<String,Object> resultMap=new HashMap<String,Object>();
		SysOrg topOrg=null;
		String orgName="";
		String json="";
		try {
			//List<ProviderOrganization> orgList=this.providerOrganizationService.getTopLevelOrgByAccount(userId);
			List<SysOrg> orgList=this.sysOrganizationService.getTopLevelOrgByAccount(userId);
			if(orgList!=null && !orgList.isEmpty()){
				topOrg=orgList.get(0);
//				orgName=topOrg.getName();
				//获取子组织
				//List<ProviderOrganization> orgListDownwardByOrg = this.providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(topOrg.getOrgId(),"BusinessOrganization");
				List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(topOrg.getOrgId(),"BusinessOrganization","downward");

				dataList=this.faultBaseStationTopTenReportDao.getFaultBaseStationTopTenData(orgListDownwardByOrg,beginTime,endTime);
			}
			List<String> baseStationName_xAxis=new ArrayList<String>();
			List<Long> baseStationFaultCount_yAxis=new ArrayList<Long>();
			
			
			//组装前台展示格式数据
			if(dataList!=null && !dataList.isEmpty()){
				for(Map<String,Object> tempMap:dataList){
					String baseStationName=tempMap.get("baseStationName")==null?"":tempMap.get("baseStationName").toString();
					Long workOrderCount=Long.valueOf(tempMap.get("workOrderCount")==null?"0":tempMap.get("workOrderCount").toString());
					baseStationName_xAxis.add(baseStationName);
					baseStationFaultCount_yAxis.add(workOrderCount);
				}
			}
			
			/*
			var arr=[{
            name: 'Tokyo',
            data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]

        	}];*/
			List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
			Map<String,Object> yAxis_map=new HashMap<String,Object>();
			yAxis_map.put("name", "故障数");
			yAxis_map.put("data", baseStationFaultCount_yAxis);
			yAxis.add(yAxis_map);
			
			if(topOrg!=null){
				resultMap.put("orgName", topOrg.getName());
				resultMap.put("topOrgId", topOrg.getOrgId());
			}
			resultMap.put("xAxis", baseStationName_xAxis);	//基站名称
			resultMap.put("yAxis", yAxis);	//基站故障数
			json=gson.toJson(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	
	
	
	/**
	 * 根据用户所选组织的，获取前10基站故障数的数据
	 * @param orgId
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getFaultBaseStationTopTenDataByOrg(long orgId,String beginTime,String endTime){
		List<Map<String, Object>> dataList=null;
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String json="";
		try {
			Map<String,Object> providerOrganization = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
			if(providerOrganization!=null){
				//获取子组织

				//List<ProviderOrganization> orgListDownwardByOrg = this.providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(providerOrganization.getId(),"BusinessOrganization");
				List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(Long.parseLong((String) providerOrganization.get("id")),"BusinessOrganization","downward");

				dataList=this.faultBaseStationTopTenReportDao.getFaultBaseStationTopTenData(orgListDownwardByOrg,beginTime,endTime);
			}
			List<String> baseStationName_xAxis=new ArrayList<String>();
			List<Long> baseStationFaultCount_yAxis=new ArrayList<Long>();
			
			
			//组装前台展示格式数据
			if(dataList!=null && !dataList.isEmpty()){
				for(Map<String,Object> tempMap:dataList){
					String baseStationName=tempMap.get("baseStationName")==null?"":tempMap.get("baseStationName").toString();
					Long workOrderCount=Long.valueOf(tempMap.get("workOrderCount")==null?"0":tempMap.get("workOrderCount").toString());
					baseStationName_xAxis.add(baseStationName);
					baseStationFaultCount_yAxis.add(workOrderCount);
				}
			}
			
			/*
			var arr=[{
            name: 'Tokyo',
            data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]

        	}];*/
			List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
			Map<String,Object> yAxis_map=new HashMap<String,Object>();
			yAxis_map.put("name", "故障数");
			yAxis_map.put("data", baseStationFaultCount_yAxis);
			yAxis.add(yAxis_map);
			
			
			if(providerOrganization!=null){
				resultMap.put("orgId", providerOrganization.get("id"));
				resultMap.put("orgName", providerOrganization.get("name"));
			}
			
			resultMap.put("xAxis", baseStationName_xAxis);	//基站名称
			resultMap.put("yAxis", yAxis);	//基站故障数
			json=gson.toJson(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	
	/**
	 * 按项目获取前10基站故障数的数据
	 * @param userId
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getFaultBaseStationTopTenDataForProject(String userId,String beginTime,String endTime){
		List<Map<String, Object>> dataList=null;
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String json="";
		
		SysOrg topOrg=null;
		
		List<String> baseStationName_xAxis=new ArrayList<String>();
		List<Long> baseStationFaultCount_yAxis=new ArrayList<Long>();
		try {
			
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
				}
				
				if(projectIdList!=null && !projectIdList.isEmpty()){
					
					//获取数据
					dataList=this.faultBaseStationTopTenReportDao.getFaultBaseStationTopTenDataByProjectIdList(projectIdList, beginTime, endTime);
					
					if(dataList!=null && !dataList.isEmpty()){
						for(Map<String,Object> tempMap:dataList){
							String baseStationName=tempMap.get("baseStationName")==null?"":tempMap.get("baseStationName").toString();
							Long workOrderCount=Long.valueOf(tempMap.get("workOrderCount")==null?"0":tempMap.get("workOrderCount").toString());
							baseStationName_xAxis.add(baseStationName);
							baseStationFaultCount_yAxis.add(workOrderCount);
						}
					}
				}
				
				List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
				Map<String,Object> yAxis_map=new HashMap<String,Object>();
				yAxis_map.put("name", "故障数");
				yAxis_map.put("data", baseStationFaultCount_yAxis);
				yAxis.add(yAxis_map);
				
				if(topOrg!=null){
					resultMap.put("topOrgName", topOrg.getName());	//组织名称
					resultMap.put("topOrgId", topOrg.getOrgId());	//组织id
				}

				resultMap.put("xAxis", baseStationName_xAxis);	//基站名称
				resultMap.put("yAxis", yAxis);	//基站故障数
				json=gson.toJson(resultMap);
			}
			json=gson.toJson(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	/**
	 * 根据用户所选项目的，获取前10基站故障数的数据
	 * @param orgId
	 * @param beginTime 月周期开始时间
	 * @param endTime 月周期结束时间
	 * @return
	 */
	public String getFaultBaseStationTopTenDataByProject(long projectId,String beginTime,String endTime){
		List<Map<String, Object>> dataList=null;
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String json="";
		
		//ProviderOrganization topOrg=null;
		
		List<String> baseStationName_xAxis=new ArrayList<String>();
		List<Long> baseStationFaultCount_yAxis=new ArrayList<Long>();
		try {
			
			
//			Map<String,String> project=this.projectInformationDao.findProjectByProjectId (String.valueOf(projectId));
			
			List<String> projectIdList=new ArrayList<String>();
			projectIdList.add(String.valueOf(projectId));
			
			if(projectIdList!=null && !projectIdList.isEmpty()){
				//获取数据
				dataList=this.faultBaseStationTopTenReportDao.getFaultBaseStationTopTenDataByProjectIdList(projectIdList, beginTime, endTime);
				
				if(dataList!=null && !dataList.isEmpty()){
					for(Map<String,Object> tempMap:dataList){
						String baseStationName=tempMap.get("baseStationName")==null?"":tempMap.get("baseStationName").toString();
						Long workOrderCount=Long.valueOf(tempMap.get("workOrderCount")==null?"0":tempMap.get("workOrderCount").toString());
						baseStationName_xAxis.add(baseStationName);
						baseStationFaultCount_yAxis.add(workOrderCount);
					}
				}
			}
			
			List<Map<String,Object>> yAxis=new ArrayList<Map<String,Object>>();
			Map<String,Object> yAxis_map=new HashMap<String,Object>();
			yAxis_map.put("name", "故障数");
			yAxis_map.put("data", baseStationFaultCount_yAxis);
			yAxis.add(yAxis_map);
			
//			if(project!=null){
//				resultMap.put("projectName",project.get("id"));	//项目名称
//				resultMap.put("projectId", project.get("NAME"));	//项目id
//			}

			resultMap.put("xAxis", baseStationName_xAxis);	//基站名称
			resultMap.put("yAxis", yAxis);	//基站故障数
			json=gson.toJson(resultMap);
			json=gson.toJson(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	
	
	/**
	 * 获取项目列表
	 * @param userId
	 * @return
	 */
	public String getProjectTreeData(String userId){
		String json="";
		
		SysOrg topOrg=null;
		//获取用户最高级组织
		//List<ProviderOrganization> orgList=this.providerOrganizationService.getTopLevelOrgByAccount(userId);
		List<SysOrg> orgList=this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		
		List<Map<String,String>> resultList=new ArrayList<Map<String,String>>();
		
		if(orgList!=null && !orgList.isEmpty()){
			topOrg=orgList.get(0);
			if(topOrg!=null){
				//根据组织id，获取对应的项目
				//List<Map<String, String>> osrList = this.organizationService.getProjectToDownwardOrgByOrgIdService(topOrg.getOrgId());
				//yuan.yw
				List<Map<String, String>> osrList = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(topOrg.getOrgId());
			
				if(osrList!=null && !osrList.isEmpty()){
					for(Map<String,String> tempMap:osrList){
						
						Map<String,String> mapEntity=new HashMap<String,String>();
						
						String proId=tempMap.get("proId");
						String projectName=tempMap.get("NAME");
						mapEntity.put("orgId", proId);
						mapEntity.put("orgName", projectName);
						resultList.add(mapEntity);
					}
				}
				
				json=gson.toJson(resultList);
			}
		}
		return json;
	}
	
	
}
