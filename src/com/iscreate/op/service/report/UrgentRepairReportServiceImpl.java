package com.iscreate.op.service.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.iscreate.op.constant.TreeConstant;
import com.iscreate.op.dao.informationmanage.InformationManageNetworkResourceDao;
import com.iscreate.op.dao.report.UrgentRepairReportDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.plat.datadictionary.DataDictionaryService;
import com.iscreate.plat.tree.TreeNode;

public class UrgentRepairReportServiceImpl implements UrgentRepairReportService{
	
	private UrgentRepairReportDao urgentRepairReportDao;
	
	
	private DataDictionaryService dataDictionaryService;
	
	
	
	
	private InformationManageNetworkResourceDao informationManageNetworkResourceDao;
	
	private NetworkResourceInterfaceService networkResourceInterfaceService;
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	private SysOrganizationDao sysOrganizationDao;//组织dao du.hw
	

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

	public NetworkResourceInterfaceService getNetworkResourceInterfaceService() {
		return networkResourceInterfaceService;
	}


	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}


	public InformationManageNetworkResourceDao getInformationManageNetworkResourceDao() {
		return informationManageNetworkResourceDao;
	}


	public void setInformationManageNetworkResourceDao(
			InformationManageNetworkResourceDao informationManageNetworkResourceDao) {
		this.informationManageNetworkResourceDao = informationManageNetworkResourceDao;
	}


	


	public DataDictionaryService getDataDictionaryService() {
		return dataDictionaryService;
	}


	public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
		this.dataDictionaryService = dataDictionaryService;
	}


	public UrgentRepairReportDao getUrgentRepairReportDao() {
		return urgentRepairReportDao;
	}


	public void setUrgentRepairReportDao(UrgentRepairReportDao urgentRepairReportDao) {
		this.urgentRepairReportDao = urgentRepairReportDao;
	}


	


	/**
	 * 获取故障抢修指定时间段的报表数据
	* @date Nov 2, 20124:54:44 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairBylatestAllowedTimeAndJudge( long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<String> rowNames = null;
		List<String> judges = null;
		List<String> rowValues = null;
		if(rowName != null && !rowName.equals("")){
			rowNames = new ArrayList<String>();
			String[] split = rowName.split(",");
			if(split != null){
				for(String s:split){
					rowNames.add(s);
				}
			}
		}
		if(judge != null && !judge.equals("")){
			judges = new ArrayList<String>();
				String[] j = judge.split(",");
				if(j != null){
					for(String s:j){
						if(s.equals("等于")){
							judges.add("=");
						}if(s.equals("不等于")){
							judges.add("<>");	
						}
					}
				}
		}if(rowValue != null && !rowValue.equals("")){
			rowValues = new ArrayList<String>();
				rowValue = new String(rowValue) ;
			String[] split = rowValue.split(",");
			if(split != null){
				for(String s:split){
					rowValues.add(s);
				}
			}
		}
	//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getProviderOrgDownwardByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
	List<Map<String,Object>> orgListDownwardByOrg = this.sysOrganizationService.getProviderOrgByOrgIdAndOrgAttrByLevelService(orgId,"BusinessOrganization","downward");

	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<String> orgIds = new ArrayList<String>();
		for (Map<String,Object> b : orgListDownwardByOrg) {
			if(b != null){
				orgIds.add(b.get("orgId")+"");
			}
		}
		if(orgIds != null){
			List<Map<String,Object>> urgentRepairBylatestAllowedTimeAndJudge = urgentRepairReportDao.getUrgentRepairBylatestAllowedTimeAndJudge(orgIds, beginTime, endTime, rowNames, judges, rowValues);
			if(urgentRepairBylatestAllowedTimeAndJudge != null && urgentRepairBylatestAllowedTimeAndJudge.size() > 0){
				list.addAll(urgentRepairBylatestAllowedTimeAndJudge);
			}
		}
		return list;
	}
	
	
	/**
	 *  获取工单数(按组织)
	* @date Nov 2, 20124:54:37 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairByOrgCount(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
	//	List<ProviderOrganization> nextProviderOrgByOrgIdService = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		//yuan.yw
		List<Map<String,Object>> nextProviderOrgByOrgIdService=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
	
		if(nextProviderOrgByOrgIdService != null && nextProviderOrgByOrgIdService.size() > 0){
			for(Map<String,Object> pr:nextProviderOrgByOrgIdService){
				Map<String, Object> returnMap = new HashMap<String, Object>();
				long oId = Long.valueOf(pr.get("orgId")+"");
				String bName = pr.get("name")+"";
				List<Map<String, Object>> list = getUrgentRepairBylatestAllowedTimeAndJudge(oId, beginTime, endTime,rowName,judge,rowValue);
				int workorderCount = 0;
				int ProcessTimeCount = 0;
				if(list != null){
					workorderCount = list.size();
					for(Map<String, Object> map:list){
						if(map.get("ProcessTimeRate") != null && !map.get("ProcessTimeRate").equals("")){
							String de  =map.get("ProcessTimeRate").toString();
							float ProcessTimeRate = 0;
							if(de != null){
								ProcessTimeRate = Float.valueOf(de);
							}
							if(ProcessTimeRate == 0){
								ProcessTimeCount++;
							}
						}
					}
				}
				returnMap.put("orgId", oId);
				returnMap.put("orgName", bName);
				returnMap.put("pOrgId", orgId);
				returnMap.put("workorderCount", workorderCount);
				returnMap.put("ProcessTimeCount", ProcessTimeCount);
				returnList.add(returnMap);
			}
		}
		return returnList;
		//getUrgentRepairBylatestAllowedTimeAndJudge();
	}
	
	
	/**
	 * 获取故障工单处理及时率(按组织)
	* @date Nov 2, 20123:58:28 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		Map<String,Object> orgByOrgIdService = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		String pName = "";
		if(orgByOrgIdService != null){
			pName = (String) orgByOrgIdService.get("name");
		}
		//List<ProviderOrganization> nextProviderOrgByOrgIdService = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		//yuan.yw
		List<Map<String,Object>> nextProviderOrgByOrgIdService=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
	
		if(nextProviderOrgByOrgIdService != null && nextProviderOrgByOrgIdService.size() > 0){
			for(Map<String,Object> pr:nextProviderOrgByOrgIdService){
				float  troudleshootingTimeCountA = 0;
				int wCount = 0;
				Map<String, Object> returnMap = new HashMap<String, Object>();
				long oId = Long.valueOf(pr.get("orgId")+"");
				String bName = pr.get("name")+"";
				List<Map<String, Object>> list = getUrgentRepairBylatestAllowedTimeAndJudge(oId, beginTime, endTime,rowName,judge,rowValue);
				float workorderCount = 0;
				float ProcessTimeCount = 0;
				float troudleshootingTimeCount = 0;
				float acceptedTimeRateCount = 0;
				if(list != null){
					//System.out.println(list.size()+"===");
					for(Map<String, Object> map:list){
						if(map.get("ProcessTimeRate") != null && !map.get("ProcessTimeRate").equals("")){
							String de  =map.get("ProcessTimeRate").toString();
							float ProcessTimeRate = 0;
							if(de != null){
								ProcessTimeRate = Float.valueOf(de);
							}
							ProcessTimeCount = ProcessTimeCount + ProcessTimeRate;
							String troudleshootingTimeString = null;
							if(map.get("troudleshootingTime") != null && !map.get("troudleshootingTime").equals("")){
								troudleshootingTimeString = map.get("troudleshootingTime").toString();
							}
							float troudleshootingTimeInt = 0;
							if(troudleshootingTimeString != null){
								troudleshootingTimeInt = Float.valueOf(troudleshootingTimeString);
							}
							troudleshootingTimeCount = troudleshootingTimeCount + troudleshootingTimeInt;
							
							wCount = list.size();
						}
						if(map.get("ProcessTimeRate") != null && !map.get("ProcessTimeRate").equals("")){
							String acceptedTimeRateString  =map.get("ProcessTimeRate").toString();
							float acceptedTimeRate = 0;
							if(acceptedTimeRateString != null){
								acceptedTimeRate = Float.valueOf(acceptedTimeRateString);
							}
							if(acceptedTimeRate == 0){
								acceptedTimeRateCount++;
							}
						}
						workorderCount++;
					}
					troudleshootingTimeCountA = troudleshootingTimeCount;
					if(list.size() > 0){
						troudleshootingTimeCount = troudleshootingTimeCount/list.size();
					}
					//System.out.println(troudleshootingTimeCount);
				}
				float processTimeRateCount = (float)(Math.round((ProcessTimeCount / workorderCount)*100));
				returnMap.put("orgId", oId);
				returnMap.put("orgName", bName);
				returnMap.put("pOrgId", orgId);
				//System.out.println(workorderCount);
				returnMap.put("wCount", workorderCount);
				//System.out.println(workorderCount);
				returnMap.put("ProcessTimeCount", ProcessTimeCount);
				returnMap.put("processTimeRateCount", processTimeRateCount+"");
				//System.out.println(acceptedTimeRateCount);
				returnMap.put("AcceptedTimeRateCount", acceptedTimeRateCount+"");
				returnMap.put("troudleshootingTimeAverage", troudleshootingTimeCount+"");
				returnMap.put("troudleshootingTimeCountA", troudleshootingTimeCountA+"");
				returnMap.put("pOrgName", pName);
				//System.out.println(troudleshootingTimeCount);
				returnList.add(returnMap);
			}
		}
		//System.out.println(returnList.size());
		return returnList;
	}
	
	
	/**
	 * 获取故障工单处理及时率(按基站类型)
	* @date Nov 2, 20124:51:43 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByBaseStationLevel(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> rlist = new ArrayList<Map<String,Object>>();
		Map<String,Object> orgByOrgIdService = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		String pName = "";
		if(orgByOrgIdService != null){
			pName = (String) orgByOrgIdService.get("name");
		}
		//List<ProviderOrganization> nextProviderOrgByOrgIdService = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		//yuan.yw
		List<Map<String,Object>> nextProviderOrgByOrgIdService=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
	
		if(nextProviderOrgByOrgIdService != null && nextProviderOrgByOrgIdService.size() > 0){
			for(Map<String,Object> pr:nextProviderOrgByOrgIdService){
				long oId = Long.valueOf(pr.get("orgId")+"");
				String bName = pr.get("name")+"";
				List<Map<String, Object>> list = getUrgentRepairBylatestAllowedTimeAndJudge(oId, beginTime, endTime,rowName,judge,rowValue);
				rlist.addAll(list);
			}
			
			//获取基站类型的数据字典
			List<TreeNode> baseStationTypeDictionary = dataDictionaryService.getDictionaryByTreeIdService(TreeConstant.BASESTATIONTYPE);
			Set<String> baseStationTypeDictionarySet = new HashSet<String>();
			if(baseStationTypeDictionary != null && baseStationTypeDictionary.size() > 0){
				for(TreeNode tr:baseStationTypeDictionary){
					baseStationTypeDictionarySet.add(tr.getTreeNodeName());
				}
			}
			classificationStatistic(orgId,pName,returnList, rlist,
					baseStationTypeDictionarySet,"baseStationLevel");
		}
		return returnList;
	}
	
	
	/**
	 * 获取故障工单处理及时率(按故障类型)
	* @date Nov 2, 20124:51:51 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByFaultType(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> rlist = new ArrayList<Map<String,Object>>();
		Map<String,Object> orgByOrgIdService = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		String pName = "";
		if(orgByOrgIdService != null){
			pName = (String) orgByOrgIdService.get("name");
		}
		//List<ProviderOrganization> nextProviderOrgByOrgIdService = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		//yuan.yw
		List<Map<String,Object>> nextProviderOrgByOrgIdService=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
	
		if(nextProviderOrgByOrgIdService != null && nextProviderOrgByOrgIdService.size() > 0){
			for(Map<String,Object> pr:nextProviderOrgByOrgIdService){
				long oId = Long.valueOf(pr.get("orgId")+"");
				String bName = pr.get("name")+"";
				List<Map<String, Object>> list = getUrgentRepairBylatestAllowedTimeAndJudge(oId, beginTime, endTime,rowName,judge,rowValue);
				rlist.addAll(list);
			}
			
			//获取故障类型的数据字典
			List<TreeNode> faultTypeDictionary = dataDictionaryService.getDictionaryByTreeIdService(TreeConstant.FAULTTYPE);
			Set<String> faultTypeDictionarySet = new HashSet<String>();
			if(faultTypeDictionary != null && faultTypeDictionary.size() > 0){
				for(TreeNode tr:faultTypeDictionary){
					faultTypeDictionarySet.add(tr.getTreeNodeName());
				}
			}
			classificationStatistic(orgId,pName,returnList, rlist,
					faultTypeDictionarySet,"faultType");
		}
		return returnList;
	}

	/**
	 * 获取故障工单处理及时率(按故障级别)
	* @date Nov 2, 20124:53:53 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByFaultLevel(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> rlist = new ArrayList<Map<String,Object>>();
		Map<String,Object> orgByOrgIdService = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		String pName = "";
		if(orgByOrgIdService != null){
			pName = (String) orgByOrgIdService.get("name");
		}
		//List<ProviderOrganization> nextProviderOrgByOrgIdService = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		//yuan.yw
		List<Map<String,Object>> nextProviderOrgByOrgIdService=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
	
		if(nextProviderOrgByOrgIdService != null && nextProviderOrgByOrgIdService.size() > 0){
			for(Map<String,Object> pr:nextProviderOrgByOrgIdService){
				long oId = Long.valueOf(pr.get("orgId")+"");
				String bName = pr.get("name")+"";
				List<Map<String, Object>> list = getUrgentRepairBylatestAllowedTimeAndJudge(oId, beginTime, endTime,rowName,judge,rowValue);
				rlist.addAll(list);
			}
			
			//获取告警级别的数据字典
			List<TreeNode> faultLevelDictionary = dataDictionaryService.getDictionaryByTreeIdService(TreeConstant.FAULTLEVEL);
			Set<String> faultLevelDictionarySet = new HashSet<String>();
			if(faultLevelDictionary != null && faultLevelDictionary.size() > 0){
				for(TreeNode tr:faultLevelDictionary){
					faultLevelDictionarySet.add(tr.getTreeNodeName());
				}
			}
			classificationStatistic(orgId,pName,returnList, rlist,
					faultLevelDictionarySet,"faultLevel");
		}
		return returnList;
	}
	
	/**
	 * 获取故障工单处理及时率(按受理专业)
	* @date Nov 2, 20124:54:28 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByAcceptProfessional(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> rlist = new ArrayList<Map<String,Object>>();
		Map<String,Object> orgByOrgIdService = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		String pName = "";
		if(orgByOrgIdService != null){
			pName = (String) orgByOrgIdService.get("name");
		}
		//List<ProviderOrganization> nextProviderOrgByOrgIdService = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		//yuan.yw
		List<Map<String,Object>> nextProviderOrgByOrgIdService=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
	
		if(nextProviderOrgByOrgIdService != null && nextProviderOrgByOrgIdService.size() > 0){
			for(Map<String,Object> pr:nextProviderOrgByOrgIdService){
				Map<String, Object> returnMap = new HashMap<String, Object>();
				long oId = Long.valueOf(pr.get("orgId")+"");
				String bName = pr.get("name")+"";
				List<Map<String, Object>> list = getUrgentRepairBylatestAllowedTimeAndJudge(oId, beginTime, endTime,rowName,judge,rowValue);
				rlist.addAll(list);
			}
			
			//获取受理专业的数据字典
			List<TreeNode> acceptProfessionalDictionary = dataDictionaryService.getDictionaryByTreeIdService(TreeConstant.ACCEPTPROFESSIONAL);
			Set<String> acceptProfessionalDictionarySet = new HashSet<String>();
			if(acceptProfessionalDictionary != null && acceptProfessionalDictionary.size() > 0){
				for(TreeNode tr:acceptProfessionalDictionary){
					acceptProfessionalDictionarySet.add(tr.getTreeNodeName());
				}
			}
			classificationStatistic(orgId,pName,returnList, rlist,
					acceptProfessionalDictionarySet,"acceptProfessional");
		}
		return returnList;
	}
	
	/**
	 * 获取故障工单处理及时率(按故障大类)
	* @date Nov 2, 20125:27:13 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByFaultGenera(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> rlist = new ArrayList<Map<String,Object>>();
		Map<String,Object> orgByOrgIdService = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		String pName = "";
		if(orgByOrgIdService != null){
			pName = (String) orgByOrgIdService.get("name");
		}
		//List<ProviderOrganization> nextProviderOrgByOrgIdService = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
		//yuan.yw
		List<Map<String,Object>> nextProviderOrgByOrgIdService=this.sysOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
	
		if(nextProviderOrgByOrgIdService != null && nextProviderOrgByOrgIdService.size() > 0){
			for(Map<String,Object> pr:nextProviderOrgByOrgIdService){
				Map<String, Object> returnMap = new HashMap<String, Object>();
				long oId = Long.valueOf(pr.get("orgId")+"");
				String bName = pr.get("name")+"";
				List<Map<String, Object>> list = getUrgentRepairBylatestAllowedTimeAndJudge(oId, beginTime, endTime,rowName,judge,rowValue);
				rlist.addAll(list);
			}
			Set<String> FaultGeneraSet = new HashSet<String>();
			if(rlist != null && rlist.size() >= 0){
				for(Map m:rlist){
					if(m.get("faultGenera") != null && !m.get("faultGenera").equals("")){
						FaultGeneraSet.add(m.get("faultGenera").toString());
					}
				}
				
			}
			classificationStatistic(orgId,pName,returnList, rlist,
					FaultGeneraSet,"faultGenera");
			if(returnList != null && !returnList.isEmpty()){
				List<Map<String, Object>> listM = new ArrayList<Map<String,Object>>();
				for(Map<String, Object> m:returnList){
					if(m.get("wCount") != null && !m.get("wCount").equals("") && m.get("wCount").equals(0)){
						listM.add(m);
					}
				}
				returnList.removeAll(listM);
			}
			
		}
		return returnList;
	}
	/**
	 * 分类统计
	* @date Nov 2, 20124:51:36 PM
	* @Description: TODO 
	* @param @param returnList
	* @param @param rlist
	* @param @param baseStationTypeDictionary        
	* @throws
	 */
	private void classificationStatistic(long orgId,String orgName,List<Map<String, Object>> returnList,
			List<Map<String, Object>> rlist,
			Set<String> set,String selectType) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(rlist != null && set != null){
			
			for(String s:set){
				String treeNodeName = s;
				
				Map<String, Object> returnMap = new HashMap<String, Object>();
				float workorderCount = 0;
				float processTimeCount = 0;
				float troudleshootingTimeCount = 0;
				float acceptedTimeRateCount = 0;
				for(Map map:rlist){
					if(map.get(selectType) != null && map.get(selectType).equals(treeNodeName)){
						if(map.get("ProcessTimeRate") != null && !map.get("ProcessTimeRate").equals("")){
							String de  =map.get("ProcessTimeRate").toString();
							float processTimeRate = 0;
							if(de != null){
								processTimeRate = Float.valueOf(de);
							}
							processTimeCount = processTimeCount + processTimeRate;
							
							String troudleshootingTimeString = null;
							if(map.get("troudleshootingTime") != null && !map.get("troudleshootingTime").equals("")){
								troudleshootingTimeString = map.get("troudleshootingTime").toString();
								
							}
							float troudleshootingTimeInt = 0;
							if(troudleshootingTimeString != null){
								troudleshootingTimeInt = Float.valueOf(troudleshootingTimeString);
							}
							troudleshootingTimeCount = troudleshootingTimeCount + troudleshootingTimeInt;
							//troudleshootingTimeCount = troudleshootingTimeCount/list.size();
						}
						if(map.get("ProcessTimeRate") != null && !map.get("ProcessTimeRate").equals("")){
							String acceptedTimeRateString  =map.get("ProcessTimeRate").toString();
							float acceptedTimeRate = 0;
							if(acceptedTimeRateString != null){
								acceptedTimeRate = Float.valueOf(acceptedTimeRateString);
							}
							if(acceptedTimeRate == 0){
								acceptedTimeRateCount++;
							}
						}
						workorderCount++;
						//System.out.println(workorderCount);
						list.add(map);
					}
				}
				float troudleshootingTimeCountA = troudleshootingTimeCount;
				if(workorderCount != 0){
					troudleshootingTimeCount = troudleshootingTimeCount/workorderCount;
				}
				float processTimeRateCount = (float)(Math.round((processTimeCount / workorderCount)*100));
				returnMap.put("statisticsType", treeNodeName);
				returnMap.put("wCount", workorderCount);
				returnMap.put("processTimeRateCount", processTimeRateCount+"");
				returnMap.put("processTimeCount", processTimeCount+"");
				//System.out.println(acceptedTimeRateCount);
				returnMap.put("AcceptedTimeRateCount", acceptedTimeRateCount+"");
				returnMap.put("troudleshootingTimeAverage", troudleshootingTimeCount+"");
				returnMap.put("troudleshootingTimeCountA", troudleshootingTimeCountA+"");
				//System.out.println(treeNodeName+"====="+workorderCount +"==processTimeRateCount"+processTimeRateCount+"");
				returnMap.put("pOrgId", orgId);
				returnMap.put("pOrgName", orgName);
				returnList.add(returnMap);
			}
			//统计空值
			float workorderCount = 0;
			float processTimeCount = 0;
			float troudleshootingTimeCount = 0;
			float acceptedTimeRateCount = 0;
			rlist.removeAll(list);
			for(Map map:rlist){
				if(map.get(selectType) == null || map.get(selectType).equals("")){
					if(map.get("ProcessTimeRate") != null && !map.get("ProcessTimeRate").equals("")){
						String de  =map.get("ProcessTimeRate").toString();
						float processTimeRate = 0;
						if(de != null){
							processTimeRate = Float.valueOf(de);
						}
						processTimeCount = processTimeCount + processTimeRate;
						String troudleshootingTimeString = null;
						if(map.get("troudleshootingTime") != null && !map.get("troudleshootingTime").equals("")){
							troudleshootingTimeString = map.get("troudleshootingTime").toString();
							
						}
						float troudleshootingTimeInt = 0;
						if(troudleshootingTimeString != null){
							troudleshootingTimeInt = Float.valueOf(troudleshootingTimeString);
						}
						troudleshootingTimeCount = troudleshootingTimeCount + troudleshootingTimeInt;
						//troudleshootingTimeCount = troudleshootingTimeCount/list.size();
					}
					if(map.get("ProcessTimeRate") != null && !map.get("ProcessTimeRate").equals("")){
						String acceptedTimeRateString  =map.get("ProcessTimeRate").toString();
						float acceptedTimeRate = 0;
						if(acceptedTimeRateString != null){
							acceptedTimeRate = Float.valueOf(acceptedTimeRateString);
						}
						if(acceptedTimeRate == 0){
							acceptedTimeRateCount++;
						}
					}
					workorderCount++;
					//System.out.println(workorderCount);
				}
			}
			float  troudleshootingTimeCountA = troudleshootingTimeCount;
			Map<String, Object> returnMap = new HashMap<String, Object>();
			if(workorderCount != 0){
				troudleshootingTimeCount = troudleshootingTimeCount/workorderCount;
			}
			float processTimeRateCount = (float)(Math.round((processTimeCount / workorderCount)*100));
			//System.out.println(processTimeCount+"============"+workorderCount);
			returnMap.put("statisticsType", "空值");
			returnMap.put("wCount", workorderCount);
			returnMap.put("processTimeRateCount", processTimeRateCount+"");
			returnMap.put("processTimeCount", processTimeCount+"");
			//System.out.println(acceptedTimeRateCount);
			returnMap.put("AcceptedTimeRateCount", acceptedTimeRateCount+"");
			returnMap.put("troudleshootingTimeAverage", troudleshootingTimeCount+"");
			returnMap.put("troudleshootingTimeCountA", troudleshootingTimeCountA+"");
			returnMap.put("pOrgId", orgId);
			returnMap.put("pOrgName", orgName);
			returnList.add(returnMap);
		}
	}
	
	
	
	
		
	
	/**
	 * 根据不同条件查询工单详细信息
	* @date Nov 6, 20129:32:53 AM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairWorkorderManage(long orgId,String beginTime,String endTime,String rowName,String judge, String rowValue){
		List<Map<String, Object>> list = getUrgentRepairBylatestAllowedTimeAndJudge(orgId, beginTime, endTime,rowName,judge,rowValue);
		if(list != null){
			for(Map<String, Object> m:list){
				long oId = Long.parseLong(m.get("organizationId")+"");
				SysOrg org = sysOrganizationDao.getOrgByOrgId(oId);
				if(org != null){
					m.put("orgName", org.getName());
				}
				if(m.get("troudleshootingTime") != null && !"".equals(m.get("troudleshootingTime"))){
					String string = m.get("troudleshootingTime").toString();
					float parseFloat = Float.valueOf(string);
					parseFloat = parseFloat/60;
					String string2 = Float.toString(parseFloat);
					if(string2.indexOf(".") > 0){
						try {
							string2 = string2.substring(0,string2.indexOf(".")+3);
						} catch (Exception e) {
							string2 = string2;
						}
					}
					m.put("troudleshootingTimeparseFloat", string2);
				}
			}
		}
		
		//System.out.println(list.size());
		return list;
	}
	
	
	/**
	 * 根据不同条件查询工单详细信息
	* @date Nov 6, 20129:32:53 AM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairWorkorderManageProject(long orgId,String beginTime,String endTime,String rowName,String judge, String rowValue){
		List<Map<String, Object>> list = getUrgentRepairBylatestAllowedTimeAndJudgeProject(orgId, beginTime, endTime,rowName,judge,rowValue);
		if(list != null){
			for(Map<String, Object> m:list){
				long oId = Long.parseLong(m.get("organizationId")+"");
				SysOrg org = sysOrganizationDao.getOrgByOrgId(oId);
				if(org != null){
					m.put("orgName", org.getName());
				}
				if(m.get("troudleshootingTime") != null && !"".equals(m.get("troudleshootingTime"))){
					String string = m.get("troudleshootingTime").toString();
					float parseFloat = Float.valueOf(string);
					parseFloat = parseFloat/60;
					String string2 = Float.toString(parseFloat);
					if(string2.indexOf(".") > 0){
						try {
							string2 = string2.substring(0,string2.indexOf(".")+3);
						} catch (Exception e) {
							string2 = string2;
						}
					}
					m.put("troudleshootingTimeparseFloat", string2);
				}
			}
		}
		
		//System.out.println(list.size());
		return list;
	}
	
	public Map<String, Object> getTreeNode(){
		//获取基站类型的数据字典
		List<TreeNode> baseStationTypeDictionary = dataDictionaryService.getDictionaryByTreeIdService(TreeConstant.BASESTATIONTYPE);
		List<TreeNode> acceptProfessionalDictionary = dataDictionaryService.getDictionaryByTreeIdService(TreeConstant.ACCEPTPROFESSIONAL);
		List<TreeNode> faultLevelDictionary = dataDictionaryService.getDictionaryByTreeIdService(TreeConstant.FAULTLEVEL);
		List<TreeNode> faultTypeDictionary = dataDictionaryService.getDictionaryByTreeIdService(TreeConstant.FAULTTYPE);
		String baseStationType = "";
		String acceptProfessional = "";
		String alarmLevel = "";
		String faultTypet = "";
		if(baseStationTypeDictionary != null && !baseStationTypeDictionary.isEmpty()){
			for(TreeNode tr:baseStationTypeDictionary){
				baseStationType =  baseStationType + "," + tr.getTreeNodeName();
			}
		}
		if(acceptProfessionalDictionary != null && !acceptProfessionalDictionary.isEmpty()){
			for(TreeNode tr:acceptProfessionalDictionary){
				acceptProfessional =  acceptProfessional + "," + tr.getTreeNodeName();
			}
		}
		if(faultLevelDictionary != null && !faultLevelDictionary.isEmpty()){
			for(TreeNode tr:faultLevelDictionary){
				alarmLevel =  alarmLevel + "," + tr.getTreeNodeName();
			}
		}
		if(faultTypeDictionary != null && !faultTypeDictionary.isEmpty()){
			for(TreeNode tr:faultTypeDictionary){
				faultTypet =  faultTypet + "," + tr.getTreeNodeName();
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("baseStationType", baseStationType);
		map.put("acceptProfessional", acceptProfessional);
		map.put("faultLevel", alarmLevel);
		map.put("faultType", faultTypet);
		return map;
	}
	
	
	/**
	 * 获取故障工单处理及时率(按组织)向上
	* @date Nov 2, 20123:58:28 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByTopOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//List<ProviderOrganization> upProviderOrgByOrgIdService = this.providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> upProviderOrgByOrgIdService = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		long uoId = 0;
		if(upProviderOrgByOrgIdService != null && !upProviderOrgByOrgIdService.isEmpty()){
			uoId = upProviderOrgByOrgIdService.get(0).getOrgId();
		}
		if(uoId != 0){
			list = getUrgentRepairProcessTimeRateByOrg(uoId, beginTime, endTime, rowName, judge, rowValue);
		}
		return list;
	}
	
	/**
	 * 获取故障工单处理及时率(按基站类型)向上
	* @date Nov 2, 20124:51:43 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByBaseStationLevelAndTopOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//List<ProviderOrganization> upProviderOrgByOrgIdService = this.providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> upProviderOrgByOrgIdService = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		long uoId = 0;
		if(upProviderOrgByOrgIdService != null && !upProviderOrgByOrgIdService.isEmpty()){
			uoId = upProviderOrgByOrgIdService.get(0).getOrgId();
		}
		if(uoId != 0){
			list = getUrgentRepairProcessTimeRateByBaseStationLevel(uoId, beginTime, endTime, rowName, judge, rowValue);
		}
		return list;
	}
	
	
	/**
	 * 获取故障工单处理及时率(按故障类型)向上
	* @date Nov 2, 20124:51:51 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByFaultTypeAndTopOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//List<ProviderOrganization> upProviderOrgByOrgIdService = this.providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> upProviderOrgByOrgIdService = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		long uoId = 0;
		if(upProviderOrgByOrgIdService != null && !upProviderOrgByOrgIdService.isEmpty()){
			uoId = upProviderOrgByOrgIdService.get(0).getOrgId();
		}
		if(uoId != 0){
			list = getUrgentRepairProcessTimeRateByFaultType(uoId, beginTime, endTime, rowName, judge, rowValue);
		}
		return list;
	}
	
	
	/**
	 * 获取故障工单处理及时率(按故障级别)向上
	* @date Nov 2, 20124:53:53 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByFaultLevelAndTopOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//List<ProviderOrganization> upProviderOrgByOrgIdService = this.providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> upProviderOrgByOrgIdService = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		long uoId = 0;
		if(upProviderOrgByOrgIdService != null && !upProviderOrgByOrgIdService.isEmpty()){
			uoId = upProviderOrgByOrgIdService.get(0).getOrgId();
		}
		if(uoId != 0){
			list = getUrgentRepairProcessTimeRateByFaultLevel(uoId, beginTime, endTime, rowName, judge, rowValue);
		}
		return list;
	}
	
	/**
	 * 获取故障工单处理及时率(按受理专业)向上
	* @date Nov 2, 20124:54:28 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByAcceptProfessionalAndTopOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//List<ProviderOrganization> upProviderOrgByOrgIdService = this.providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> upProviderOrgByOrgIdService = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		long uoId = 0;
		if(upProviderOrgByOrgIdService != null && !upProviderOrgByOrgIdService.isEmpty()){
			uoId = upProviderOrgByOrgIdService.get(0).getOrgId();
		}
		if(uoId != 0){
			list = getUrgentRepairProcessTimeRateByAcceptProfessional(uoId, beginTime, endTime, rowName, judge, rowValue);
		}
		return list;
	}
	
	/**
	 * 获取故障工单处理及时率(按故障大类)向上
	* @date Nov 2, 20125:27:13 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairProcessTimeRateByFaultGeneraAndTopOrg(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//List<ProviderOrganization> upProviderOrgByOrgIdService = this.providerOrganizationService.getUpProviderOrgByOrgIdService(orgId);
		//yuan.yw
		List<SysOrg> upProviderOrgByOrgIdService = this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(orgId,"parent");

		long uoId = 0;
		if(upProviderOrgByOrgIdService != null && !upProviderOrgByOrgIdService.isEmpty()){
			uoId = upProviderOrgByOrgIdService.get(0).getOrgId();
		}
		if(uoId != 0){
			list = getUrgentRepairProcessTimeRateByFaultGenera(uoId, beginTime, endTime, rowName, judge, rowValue);
		}
		return list;
	}
	
	
	/**
	 *  获取工单数(按项目)
	* @date Nov 2, 20124:54:37 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairByOrgCountProject(long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){
		List<Map<String, Object>> returnList = new ArrayList<Map<String,Object>>();
		//List<ProviderOrganization> nextProviderOrgByOrgIdService = new ArrayList<ProviderOrganization>();
		//List<ProviderOrganization> nextProviderOrgByOrgIdService2 = providerOrganizationService.getOrgListDownwardByOrg(orgId);
		//List<Map<String, String>> findProjectByOrgId = organizationService.getProjectToDownwardOrgByOrgIdService(orgId);
		//yuan.yw
		List<Map<String, String>> findProjectByOrgId = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(orgId);

		Map<String,Object> orgByOrgIdService = sysOrganizationDao.getOrganizationMessageByOrgId(orgId);
		String pName = "";
		if(orgByOrgIdService != null){
			pName = (String) orgByOrgIdService.get("name");
		}
		if(findProjectByOrgId != null && findProjectByOrgId.size() > 0){
			for(Map<String,String> pr:findProjectByOrgId){
				float  troudleshootingTimeCountA = 0;
				int wCount = 0;
				Map<String, Object> returnMap = new HashMap<String, Object>();
				long oId = Long.valueOf(pr.get("id"));
				String bName = pr.get("NAME");
				List<Map<String, Object>> list = getUrgentRepairBylatestAllowedTimeAndJudgeProject(oId, beginTime, endTime,rowName,judge,rowValue);
				float workorderCount = 0;
				float ProcessTimeCount = 0;
				float troudleshootingTimeCount = 0;
				float acceptedTimeRateCount = 0;
				if(list != null){
					//System.out.println(list.size()+"===");
					for(Map<String, Object> map:list){
						if(map.get("ProcessTimeRate") != null && !map.get("ProcessTimeRate").equals("")){
							String de  =map.get("ProcessTimeRate").toString();
							float ProcessTimeRate = 0;
							if(de != null){
								ProcessTimeRate = Float.valueOf(de);
							}
							ProcessTimeCount = ProcessTimeCount + ProcessTimeRate;
							String troudleshootingTimeString = null;
							if(map.get("troudleshootingTime") != null && !map.get("troudleshootingTime").equals("")){
								troudleshootingTimeString = map.get("troudleshootingTime").toString();
							}
							float troudleshootingTimeInt = 0;
							if(troudleshootingTimeString != null){
								troudleshootingTimeInt = Float.valueOf(troudleshootingTimeString);
							}
							troudleshootingTimeCount = troudleshootingTimeCount + troudleshootingTimeInt;
							
							wCount = list.size();
						}
						if(map.get("ProcessTimeRate") != null && !map.get("ProcessTimeRate").equals("")){
							String acceptedTimeRateString  =map.get("ProcessTimeRate").toString();
							float acceptedTimeRate = 0;
							if(acceptedTimeRateString != null){
								acceptedTimeRate = Float.valueOf(acceptedTimeRateString);
							}
							if(acceptedTimeRate == 0){
								acceptedTimeRateCount++;
							}
						}
						workorderCount++;
					}
					troudleshootingTimeCountA = troudleshootingTimeCount;
					if(list.size() > 0){
						troudleshootingTimeCount = troudleshootingTimeCount/list.size();
					}
					//System.out.println(troudleshootingTimeCount);
				}
				float processTimeRateCount = (float)(Math.round((ProcessTimeCount / workorderCount)*100));
				returnMap.put("orgId", oId);
				returnMap.put("pOrgId", orgId);
				//System.out.println(workorderCount);
				returnMap.put("wCount", workorderCount);
				//System.out.println(workorderCount);
				returnMap.put("ProcessTimeCount", ProcessTimeCount);
				returnMap.put("processTimeRateCount", processTimeRateCount+"");
				//System.out.println(acceptedTimeRateCount);
				returnMap.put("AcceptedTimeRateCount", acceptedTimeRateCount+"");
				returnMap.put("troudleshootingTimeAverage", troudleshootingTimeCount+"");
				returnMap.put("troudleshootingTimeCountA", troudleshootingTimeCountA+"");
				returnMap.put("orgName", bName);
				returnMap.put("pOrgName", pName);
				//System.out.println(troudleshootingTimeCount);
				returnList.add(returnMap);
			}
		}
		//System.out.println(returnList.size());
		return returnList;
	}
	
	
	/**
	 * 获取故障抢修指定时间段的报表数据(项目)
	* @date Nov 2, 20124:54:44 PM
	* @Description: TODO 
	* @param @param orgId
	* @param @param beginTime
	* @param @param endTime
	* @param @param rowName
	* @param @param judge
	* @param @param rowValue
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> getUrgentRepairBylatestAllowedTimeAndJudgeProject( long orgId, String beginTime, String endTime,String rowName , String judge , String rowValue ){		
		List<String> rowNames = null;
		List<String> judges = null;
		List<String> rowValues = null;
		if(rowName != null && !rowName.equals("")){
			rowNames = new ArrayList<String>();
			String[] split = rowName.split(",");
			if(split != null){
				for(String s:split){
					rowNames.add(s);
				}
			}
		}
		if(judge != null && !judge.equals("")){
			judges = new ArrayList<String>();
				String[] j = judge.split(",");
				if(j != null){
					for(String s:j){
						if(s.equals("等于")){
							judges.add("=");
						}if(s.equals("不等于")){
							judges.add("<>");	
						}
					}
				}
		}if(rowValue != null && !rowValue.equals("")){
			rowValues = new ArrayList<String>();
				rowValue = new String(rowValue) ;
			String[] split = rowValue.split(",");
			if(split != null){
				for(String s:split){
					rowValues.add(s);
				}
			}
		}
	//List<Map<String, String>> orgListDownwardByOrg = organizationService.getProjectToDownwardOrgByOrgIdService(orgId);
	//yuan.yw	
	List<Map<String, String>> orgListDownwardByOrg = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(orgId);
	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	if(orgListDownwardByOrg != null && !orgListDownwardByOrg.isEmpty()){
		
		for (Map<String, String> map : orgListDownwardByOrg) {
			String string = map.get("id");
//			List<String> list2 = new ArrayList<String>();
//			List<Map<String, String>> orgByProjectIdService = this.organizationService.getOrgByProjectIdService(string);
//			if(orgByProjectIdService != null ){
//				for(Map<String, String> ma: orgByProjectIdService){
//					String string2 = ma.get("id");
//					Long valueOf = Long.valueOf(string2);
//					List<ProviderOrganization> orgListDownwardByOrg2 = this.providerOrganizationService.getOrgListDownwardByOrg(valueOf);
//					if(orgListDownwardByOrg2 != null){
//						for(ProviderOrganization p:orgListDownwardByOrg2){
//							List<Map<String,Object>> urgentRepairBylatestAllowedTimeAndJudge = this.urgentRepairReportDao.getUrgentRepairBylatestAllowedTimeAndJudge(p.getId(), beginTime, endTime, rowNames, judges, rowValues);
//							list.addAll(urgentRepairBylatestAllowedTimeAndJudge);
//						}
//					}
//				}
//			}
			//项目获取资源
			List<Map<String,String>> findProjectResourceByProjectId = this.informationManageNetworkResourceDao.findAreaIdAndResourceTypeByProjectIdWithoutSame(string);
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
						//System.out.println(reIds.size());
//						int size = 0;
//						if(reIds.size() > 500){
//							//System.out.println("====="+reIds.size()%500);
//							if(reIds.size()%500 > 0){
//								size = reIds.size()/500 + 1;
//							}else{
//								size = reIds.size()/500;
//							}
//							int ci = 0;
//							int bi = 0;
//							for(int i = 0;i < size;i++){
//								ci = 499 + i;
//								if(ci > reIds.size()){
//									ci = reIds.size();
//								}else{
//									
//								}
//								List<String> re = new ArrayList<String>();
//								for(;bi <= ci;bi++){
//									re.add(reIds.get(bi));
//								}
//								
//								List<Map<String,Object>> urgentRepairBylatestAllowedTimeAndJudge = urgentRepairReportDao.getUrgentRepairByreIdsAndreType(re, key, beginTime, endTime, rowNames, judges, rowValues);
//								if(urgentRepairBylatestAllowedTimeAndJudge != null && urgentRepairBylatestAllowedTimeAndJudge.size() > 0){
//									list.addAll(urgentRepairBylatestAllowedTimeAndJudge);
//								}
//							}
//						}else{
							List<Map<String,Object>> urgentRepairBylatestAllowedTimeAndJudge = urgentRepairReportDao.getUrgentRepairByreIdsAndreType(reIds, key, beginTime, endTime, rowNames, judges, rowValues);
							if(urgentRepairBylatestAllowedTimeAndJudge != null && urgentRepairBylatestAllowedTimeAndJudge.size() > 0){
								list.addAll(urgentRepairBylatestAllowedTimeAndJudge);
							}
//						}
					}
				}
			}
			//long id = Long.valueOf(string);
		}
	}
		return list;
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
////						if(orgId == 16){
////							List<ProviderOrganization> nextProviderOrg = providerOrganizationService.getProviderOrgNextByOrgIdAndOrgAttrService(orgId,"BusinessOrganization");
////							if(nextProviderOrg != null && nextProviderOrg.size() > 0){
////								for(ProviderOrganization p:nextProviderOrg){
////									Map<String, Object> map = new HashMap<String, Object>();
////									map.put("type", "Org");
////									long id = p.getId();
////									String name = p.getName();
////									map.put("id", id);
////									map.put("name", name);
////									nextProviderOrgByOrgId.add(map);
////								}
////							}else{
////							}
////						}else{
//							Map<String, Object> map = new HashMap<String, Object>();
//							map.put("type", "Org");
//							long id = providerOrganization.getOrgId();
//							String name = providerOrganization.getName();
//							map.put("id", id);
//							map.put("name", name);
//							nextProviderOrgByOrgId.add(map);
////						}
//					}
//				}
//			}else{
//				//Map<String, String> topOrgService = organizationService.getTopOrgService();
//				Map<String, String> topOrgService = this.sysOrganizationService.getTopOrgService();
//				if(topOrgService != null && !topOrgService.isEmpty()){
//					String orgId = topOrgService.get("id");
//					long orgIdl = Long.valueOf(orgId);
//					//List<Map<String, String>> findProjectByOrgId = organizationService.getProjectToDownwardOrgByOrgIdService(orgIdl);
//					//yuan.yw
//					List<Map<String, String>> findProjectByOrgId = this.sysOrganizationService.getProjectToDownwardOrgByOrgId(orgIdl);
//						if(findProjectByOrgId != null && findProjectByOrgId.size() > 0){
//							for(Map<String, String> m:findProjectByOrgId){
//								Map<String, Object> map = new HashMap<String, Object>();
//								map.put("type", "Project");
//								String id = m.get("id");
//								String name = m.get("NAME");
//								map.put("id", id);
//								map.put("name", name);
//								nextProviderOrgByOrgId.add(map);
//							}
//						}else{
//						}
//				}
//			}
//		}else{
//		}
		return nextProviderOrgByOrgId;
	}
}
