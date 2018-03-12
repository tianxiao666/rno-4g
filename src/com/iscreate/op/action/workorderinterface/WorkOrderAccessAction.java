package com.iscreate.op.action.workorderinterface;

import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.constant.BizAuthorityConstant;
import com.iscreate.op.constant.Constant;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.urgentrepair.UrgentRepairCustomerWorkOrderDao;
import com.iscreate.op.dao.urgentrepair.UrgentRepairWorkOrderDao;
import com.iscreate.op.pojo.publicinterface.Tasktracerecord;
import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairCustomerworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;

import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.urgentrepair.UrgentRepairWorkOrderService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.op.service.workorderinterface.wangyou.WorkorderinterfaceWangyouWorkorderRelationService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.TimeFormatHelper;


public class WorkOrderAccessAction {
	
	
	Logger logger = Logger.getLogger(this.getClass());
	
	
	private UrgentRepairWorkOrderDao urgentRepairWorkOrderDao;
	private UrgentRepairWorkOrderService urgentRepairWorkOrderService;
	private UrgentRepairCustomerWorkOrderDao urgentRepairCustomerWorkOrderDao;

	private WorkManageService workManageService;
	private NetworkResourceInterfaceService networkResourceInterfaceService;
	
	private String customerWorkOrderParam;
	private String acceptWorkOrderParam;
	
	private static Gson gson=new Gson();
	
	
	private String customerWoId;	//客户工单号
	private String customWorkOrderType;	//网优客户工单类型,如2g、td
	private String smallAreaName;	//获取小区名称
	private String smallAreaNumber;	//获取小区号
	private String faultStationName;	//获取基站名称
	private String vipLevel;	//vip等级
	private String alarmContent;	//告警内容
	private String faultType;	//故障大类
	private String acceptDomain;	//受理专业
	private String alarmType;	//告警类型
	private String alarmLevel;	//告警级别
	private String firstAlarmTime;	//首次告警时间
	private String assginOrderTime;	//派单时间
	
	private String defaultOrgIdOfTaskDispatcher;	//默认任务调度员所在组织
	private String defaultTaskDispatcher;	//默认任务调度员
	
	
	private String creator;
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	
	private SysOrgUserService sysOrgUserService;
	
	

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
	
	public void setUrgentRepairWorkOrderDao(
			UrgentRepairWorkOrderDao urgentRepairWorkOrderDao) {
		this.urgentRepairWorkOrderDao = urgentRepairWorkOrderDao;
	}

	public void setUrgentRepairCustomerWorkOrderDao(
			UrgentRepairCustomerWorkOrderDao urgentRepairCustomerWorkOrderDao) {
		this.urgentRepairCustomerWorkOrderDao = urgentRepairCustomerWorkOrderDao;
	}



	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}

	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}


	public String getCustomerWorkOrderParam() {
		return customerWorkOrderParam;
	}

	public void setCustomerWorkOrderParam(String customerWorkOrderParam) {
		this.customerWorkOrderParam = customerWorkOrderParam;
	}
	
	


	public UrgentRepairWorkOrderService getUrgentRepairWorkOrderService() {
		return urgentRepairWorkOrderService;
	}

	public void setUrgentRepairWorkOrderService(
			UrgentRepairWorkOrderService urgentRepairWorkOrderService) {
		this.urgentRepairWorkOrderService = urgentRepairWorkOrderService;
	}

	public String getAcceptWorkOrderParam() {
		return acceptWorkOrderParam;
	}

	public void setAcceptWorkOrderParam(String acceptWorkOrderParam) {
		this.acceptWorkOrderParam = acceptWorkOrderParam;
	}

	/**
	 * 创建Iosm工单action
	 */
	public void createIosmWorkOrderAction(){
		
		HttpServletResponse response = null;
		PrintWriter pw=null;
		try {
			response = ServletActionContext.getResponse();
			pw=response.getWriter();
			
			String resourceId="";
			String baseStationType="";
			
			//获取客户工单参数
			String customerWorkOrderParam=new String(this.customerWorkOrderParam.getBytes("ISO8859-1"),"UTF-8");
			Map<String, String> customerWorkOrderParamMap=gson.fromJson(customerWorkOrderParam, new TypeToken<Map<String, String>>(){}.getType());
			
			
			defaultOrgIdOfTaskDispatcher=customerWorkOrderParamMap.get("defaultOrgIdOfTaskDispatcher")==null?"":customerWorkOrderParamMap.get("defaultOrgIdOfTaskDispatcher").toString();
			defaultTaskDispatcher=customerWorkOrderParamMap.get("defaultTaskDispatcher")==null?"":customerWorkOrderParamMap.get("defaultTaskDispatcher").toString();
			
			customerWoId=customerWorkOrderParamMap.get("customerWoId")==null?"":customerWorkOrderParamMap.get("customerWoId").toString();
			customWorkOrderType=customerWorkOrderParamMap.get("customWorkOrderType")==null?"":customerWorkOrderParamMap.get("customWorkOrderType").toString();
			smallAreaName=customerWorkOrderParamMap.get("smallAreaName")==null?"":customerWorkOrderParamMap.get("smallAreaName").toString();
			smallAreaNumber=customerWorkOrderParamMap.get("smallAreaNumber")==null?"":customerWorkOrderParamMap.get("smallAreaNumber").toString();
			faultStationName=customerWorkOrderParamMap.get("faultStationName")==null?"":customerWorkOrderParamMap.get("faultStationName").toString();
			vipLevel=customerWorkOrderParamMap.get("vipLevel")==null?"":customerWorkOrderParamMap.get("vipLevel").toString();
			alarmContent=customerWorkOrderParamMap.get("alarmContent")==null?"":customerWorkOrderParamMap.get("alarmContent").toString();
			faultType=customerWorkOrderParamMap.get("faultType")==null?"":customerWorkOrderParamMap.get("faultType").toString();
			acceptDomain=customerWorkOrderParamMap.get("acceptDomain")==null?"":customerWorkOrderParamMap.get("acceptDomain").toString();
			alarmType=customerWorkOrderParamMap.get("alarmType")==null?"":customerWorkOrderParamMap.get("alarmType").toString();
			alarmLevel=customerWorkOrderParamMap.get("alarmLevel")==null?"":customerWorkOrderParamMap.get("alarmLevel").toString();
			firstAlarmTime=customerWorkOrderParamMap.get("firstAlarmTime")==null?"":customerWorkOrderParamMap.get("firstAlarmTime").toString();
			assginOrderTime=customerWorkOrderParamMap.get("assginOrderTime")==null?"":customerWorkOrderParamMap.get("assginOrderTime").toString();
			
			Map<String,String> baseStationMap=null;	//基站
			
			//获取基站
			if("2g".equals(customWorkOrderType)){
				
				//根据小区名称获取小区
//				String encode_smallAreaName=URLEncoder.encode(this.smallAreaName, "UTF-8");
				Map<String,String> smallAreaMap=this.networkResourceInterfaceService.getResourceByReNameAndReTypeService(smallAreaName,Constant.NETWORKRESOURCE_CELL);
				String smallAreaId="";
				if(smallAreaMap!=null && !smallAreaMap.isEmpty()){
					smallAreaId=smallAreaMap.get("id")==null?"":smallAreaMap.get("id").toString();
				}
				
				
				//根据小区id，获取所属基站
				List<Map<String,String>> smallAreaOfBaseStationList=new ArrayList<Map<String,String>>();
				
				if(smallAreaId!=null && !"".equals(smallAreaId)){
					
					List<Map<String,String>> baseStationList=this.networkResourceInterfaceService.getResourceService(smallAreaId,Constant.NETWORKRESOURCE_CELL,Constant.NETWORKRESOURCE_BASESTATION,"PARENT");
					if(baseStationList!=null && !baseStationList.isEmpty()){
						smallAreaOfBaseStationList.addAll(baseStationList);
						
					}
					
					List<Map<String,String>> baseStation_gsmList=this.networkResourceInterfaceService.getResourceService(smallAreaId,Constant.NETWORKRESOURCE_CELL,"BaseStation_GSM","PARENT");
					if(baseStation_gsmList!=null && !baseStation_gsmList.isEmpty()){
						smallAreaOfBaseStationList.addAll(baseStation_gsmList);
					}
					
					if(smallAreaOfBaseStationList!=null && !smallAreaOfBaseStationList.isEmpty()){
						baseStationMap=smallAreaOfBaseStationList.get(0);
					}
				}
				
				//--------------new begin------------------
//				ApplicationEntity cellApplication=null;
//				
//				//根据组织获取区域
//				List<Org_Area_Resource> oars = this.organizationService.getOrg_Area_ResourceByOrg(this.bizunitInstanceId+"","serviceProvider");
//				if(oars!=null && !oars.isEmpty()){
//					for(Org_Area_Resource oar : oars){
//						String areaId=oar.getAreaId();
//						cellApplication=this.structureCommonService.getCellOfChildAreaByAreaId(areaId, "name", this.smallAreaName, "networkresourcemanage");
//						if(cellApplication!=null){
//							break;
//						}
//					}
//				}
//				ApplicationEntity[] parentEntityArray=this.structureCommonService.getStrutureSelationsApplicationEntity(cellApplication, AssociatedType.PARENT, "networkresourcemanage");
//				if(parentEntityArray!=null && parentEntityArray.length>0){
//					baseStationEntity=parentEntityArray[0];
//				}
				//--------------new end------------------
				
			}else if("td".equals(customWorkOrderType)){
				//根据基站名称获取基站对象
				baseStationMap=this.networkResourceInterfaceService.getResourceByReNameAndReTypeService(faultStationName,Constant.NETWORKRESOURCE_GENERAL_BASESTATION);
			}
			
			List<Map<String,String>> areaInfoList=null;
			
			Map<String,String> areaInfo=new HashMap<String,String>();
			
			Map<String,String> tempStationMap = new HashMap<String,String>();
			if("2g".equals(customWorkOrderType)){
				if(baseStationMap!=null && !baseStationMap.isEmpty()){
					resourceId=baseStationMap.get("id")==null?"":baseStationMap.get("id").toString();
					baseStationType=baseStationMap.get("_entityType")==null?"":baseStationMap.get("_entityType").toString();
					String baseStationName=baseStationMap.get("name")==null?"":baseStationMap.get("name").toString();
					
					//根据基站id和基站类型，获取站址
					tempStationMap=this.networkResourceInterfaceService.getStationByBaseStationIdAndBaseStationType(resourceId, baseStationType, Constant.NETWORKRESOURCE_STATION);
					String tempStationId=tempStationMap.get("id")==null?"":tempStationMap.get("id").toString();
					String tempStationType=tempStationMap.get("_entityType")==null?"":tempStationMap.get("_entityType").toString();
					
					//获取基站所属站址的区域信息
//					areaInfoList=this.networkResourceInterfaceService.getAllAreaByStationIdAndStationTypeService(tempStationId,tempStationType);
					areaInfo=this.transferAreaInfo(tempStationMap);
					
					
					//获取该基站所在的区域（如深圳龙岗或深圳葵涌）与组织的关联，可以通过该关联获取对应组织，也就是获取指定范围的组织
					List<SysOrg> orgList = this.networkResourceInterfaceService.getOrgByBaseStationIdAndOrgTypeAndResourceType(Long.valueOf(resourceId), "serviceProvider", baseStationType);
					if(orgList==null || orgList.isEmpty()){
						logger.info("CustomWoId【"+customerWoId+"】，get Area Of BaseStation【"+baseStationName+"】 ,and the Area has the taskDispatcher role，but The Area is null");
						return;
					}
					long orgId =orgList.get(0).getOrgId();//获取组织id
					
					//根据传入的组织为起点，向上或向下获取具有‘任务调度员’角色的组织列表
					/*List<ProviderOrganization> withTaskDispatcherRoleOrgList = this.providerOrganizationService.getProviderOrgByOrgIdAndRoleCode(orgId, BizAuthorityConstant.ROLE_TASKDISPATCHER);
					
					long withTaskDispatcherRoleOrgId = 0;
					if(withTaskDispatcherRoleOrgList!=null && !withTaskDispatcherRoleOrgList.isEmpty()){
						withTaskDispatcherRoleOrgId = withTaskDispatcherRoleOrgList.get(0).getId();
					}else{
						withTaskDispatcherRoleOrgId=Long.valueOf(this.defaultOrgIdOfTaskDispatcher);
					}*/
					
					//根据带有指定业务角色的组织id，获取具有任务调度员角色的用户列表
					//List<Staff> staffList=this.providerOrganizationService.getProviderStaffByOrgIdAndRoleCode(withTaskDispatcherRoleOrgId, BizAuthorityConstant.ROLE_TASKDISPATCHER);
					//yuan.yw
					List<Map<String,Object>> staffList = this.sysOrganizationService.getProviderStaffByOrgIdAndRoleCode(orgId, BizAuthorityConstant.ROLE_TASKDISPATCHER, "downOrUpward");
					if(staffList!=null && !staffList.isEmpty()){
						Map<String,Object> staff=staffList.get(0);
						String accountId=staff.get("ACCOUNT")+"";
						logger.info("任务调度员account=="+accountId);
						this.creator=accountId;
					}else{
						this.creator=this.defaultTaskDispatcher;
					}
					
				}else{
					logger.info("CustomWoId【"+this.customerWoId+"】，get BaseStation By SmallAreaName【"+this.smallAreaName+"】，The BaseStation is null");
					pw.write("fail");
					return;
				}
			}else if("td".equals(customWorkOrderType)){
				if(baseStationMap!=null && !baseStationMap.isEmpty()){
					resourceId=baseStationMap.get("id")==null?"":baseStationMap.get("id").toString();
					baseStationType=baseStationMap.get("_entityType")==null?"":baseStationMap.get("_entityType").toString();
					
					//根据基站id和基站类型，获取站址
					tempStationMap=this.networkResourceInterfaceService.getStationByBaseStationIdAndBaseStationType(resourceId, baseStationType,Constant.NETWORKRESOURCE_STATION);
					String tempStationId="";
					String tempStationType="";
					if(tempStationMap!=null && !tempStationMap.isEmpty()){
						tempStationId=tempStationMap.get("id")==null?"":tempStationMap.get("id").toString();
						tempStationType=tempStationMap.get("_entityType")==null?"":tempStationMap.get("_entityType").toString();
					}
					
					//获取基站所属站址的区域信息
//					areaInfoList=this.networkResourceInterfaceService.getAllAreaByStationIdAndStationTypeService(tempStationId,tempStationType);
					areaInfo=this.transferAreaInfo(tempStationMap);
					
					//获取该基站所在的区域（如深圳龙岗或深圳葵涌）与组织的关联，可以通过该关联获取对应组织，也就是获取指定范围的组织
					List<SysOrg> orgList = this.networkResourceInterfaceService.getOrgByBaseStationIdAndOrgTypeAndResourceType(Long.valueOf(resourceId), "serviceProvider", baseStationType);
					if(orgList==null || orgList.isEmpty()){
						logger.info("CustomWoId【"+this.customerWoId+"】，get Area Of BaseStation【"+this.faultStationName+"】 in it,and the Area has the taskDispatcher role，but The Area is null");
						return;
					}
					long orgId =orgList.get(0).getOrgId();//获取组织id
					
					//以传入的组织为起点，向上或向下获取具有‘任务调度员’角色的业务单元
					/*List<ProviderOrganization> withTaskDispatcherRoleOrgList = this.providerOrganizationService.getProviderOrgByOrgIdAndRoleCode(orgId, BizAuthorityConstant.ROLE_TASKDISPATCHER);

					long withTaskDispatcherRoleOrgId = 0;
					if(withTaskDispatcherRoleOrgList!=null && !withTaskDispatcherRoleOrgList.isEmpty()){
						withTaskDispatcherRoleOrgId = withTaskDispatcherRoleOrgList.get(0).getId();
					}else{
						withTaskDispatcherRoleOrgId=Long.valueOf(this.defaultOrgIdOfTaskDispatcher);
					}*/
					
					//根据带有指定业务角色的组织id，获取具有任务调度员角色的用户列表
					//List<Staff> staffList=this.providerOrganizationService.getProviderStaffByOrgIdAndRoleCode(withTaskDispatcherRoleOrgId, BizAuthorityConstant.ROLE_TASKDISPATCHER);
					//yuan.yw
					List<Map<String,Object>> staffList = this.sysOrganizationService.getProviderStaffByOrgIdAndRoleCode(orgId, BizAuthorityConstant.ROLE_TASKDISPATCHER, "downOrUpward");

					if(staffList!=null && !staffList.isEmpty()){
						Map<String,Object> staff=staffList.get(0);
						String accountId=staff.get("ACCOUNT")+"";
						logger.info("任务调度员account=="+accountId);
						this.creator=accountId;
					}else{
						this.creator=this.defaultTaskDispatcher;
					}
					
				}else{
					logger.info("CustomWoId【"+this.customerWoId+"】，get BaseStation By BaseStationName【"+this.faultStationName+"】，The BaseStation is null");
					pw.write("fail");
					return;
				}
			}
			
			//根据工单主题生成规则构造工单主题wotitle
			String woTitle="";
			String cityName=areaInfo.get("city");
			if(cityName==null){
				cityName="";
			}
			String tempGeneralStationName="";
			
			if(baseStationMap!=null && !baseStationMap.isEmpty()){
				tempGeneralStationName=(baseStationMap.get("name")==null)||("null".equals(baseStationMap.get("name")))?"":baseStationMap.get("name").toString();
			}
			
//			//如果是td设备工单，直接使用接口获取的数据来设置基站
//			if("td".equals(customWorkOrderType)){
//				tempGeneralStationName=this.faultStationName;
//			}
			
			String commonWorkOrderId="";
			UrgentrepairWorkorder urgentrepairWorkorder=new UrgentrepairWorkorder();
			if(baseStationMap!=null && !baseStationMap.isEmpty()){
				try {
					if("2g".equals(customWorkOrderType)){
						urgentrepairWorkorder.setFaultType(this.alarmType);
						String smallAreaAndNumber=(this.smallAreaName==null?"":this.smallAreaName)+"（"+(this.smallAreaNumber==null?"":this.smallAreaNumber)+")";
						if(baseStationMap!=null){
							urgentrepairWorkorder.setNetElementName(smallAreaAndNumber);	//IOSM的网元字段
						}
					}else if("td".equals(customWorkOrderType)){
						
					}
					
					urgentrepairWorkorder.setFaultLevel(this.alarmLevel);	//IOSM的故障级别
					urgentrepairWorkorder.setFaultOccuredTime(TimeFormatHelper.setTimeFormat(this.firstAlarmTime));	//IOSM的故障发生时间
					
					//计算故障处理截止时间
					Date temp_latestAllowedTime=this.calculateTime(vipLevel, alarmLevel,this.assginOrderTime );
					urgentrepairWorkorder.setLatestAllowedTime(temp_latestAllowedTime);	//IOSM的工单处理时限
					
					urgentrepairWorkorder.setWoId(commonWorkOrderId);
					
					if(baseStationMap!=null && !baseStationMap.isEmpty()){
						urgentrepairWorkorder.setStationName((baseStationMap.get("name")==null)||("null".equals(baseStationMap.get("name")))?"":baseStationMap.get("name").toString());
						urgentrepairWorkorder.setFaultStationName((baseStationMap.get("name")==null)||("null".equals(baseStationMap.get("name")))?"":baseStationMap.get("name").toString());
						urgentrepairWorkorder.setFaultStationAddress((baseStationMap.get("address")==null)||("null".equals(baseStationMap.get("address")))?"":baseStationMap.get("address").toString());
					}
					
					urgentrepairWorkorder.setFaultDescription(this.alarmContent==null?"":constructAlarmContent(this.alarmContent));	//IOSM的故障描述
					urgentrepairWorkorder.setBaseStationLevel(vipLevel==null?"":vipLevel);	//IOSM的基站等级
					urgentrepairWorkorder.setAcceptProfessional(acceptDomain==null?"":acceptDomain);		//IOSM的受理专业
					urgentrepairWorkorder.setFaultInWhichProvince(areaInfo.get("province")==null?"":areaInfo.get("province"));
					urgentrepairWorkorder.setFaultInWhichCity(areaInfo.get("city")==null?"":areaInfo.get("city"));
					urgentrepairWorkorder.setFaultInWhichCountry( areaInfo.get("district")==null?"":areaInfo.get("district"));
					urgentrepairWorkorder.setFaultInWhichTown(areaInfo.get("town")==null?"":areaInfo.get("town"));

					if("2g".equals(customWorkOrderType)){
						urgentrepairWorkorder.setCustomerWoType("home2g");
					}else if("td".equals(customWorkOrderType)){
						urgentrepairWorkorder.setCustomerWoType("homeTd");
					}
					
					//保存IOSM个性信息抢修工单
					Serializable workorderid = this.urgentRepairWorkOrderDao.saveUrgentRepairWorkOrder(urgentrepairWorkorder);
					
					
					String creatorName="";
					//ou.jh
					SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(creator);
//					Staff staff=this.providerOrganizationService.getStaffByAccount(creator);
					if(sysOrgUserByAccount!=null){
						creatorName=sysOrgUserByAccount.getName();
					}
					
					
					//获取创建人的所在组织
					//List<ProviderOrganization> providerOrganizations = providerOrganizationService.getOrgByAccountAndRoleCode(creator,BizAuthorityConstant.ROLE_TASKDISPATCHER );
					//yuan.yw
					List<Map<String,Object>> providerOrganizations = this.sysOrganizationService.getOrgByAccountAndRoleCode(creator,BizAuthorityConstant.ROLE_TASKDISPATCHER );
					long providerOrgId = 0;
					if(providerOrganizations!=null&&providerOrganizations.size()>0){
						providerOrgId = Long.parseLong(providerOrganizations.get(0).get("orgId")+"");
						if(providerOrganizations.size()>1){
							logger.warn("账号=="+this.creator+"担任角色任务调度员所在的组织有多个。sroviderOrganizations.size()>1");
							
						}
					}else {
						throw new UserDefinedException("找不到账号=="+this.creator+"担任角色任务调度员所在的组织");
					}
					
					//创建公共工单对象
					WorkmanageWorkorder workmanageWorkorder=new WorkmanageWorkorder();
					workmanageWorkorder.setWoType("抢修");
					workmanageWorkorder.setCreator(this.creator);
					workmanageWorkorder.setCreatorName(sysOrgUserByAccount.getName());
					workmanageWorkorder.setCreatorOrgId(providerOrgId);
					
					woTitle=this.constructWoTitle(cityName, tempGeneralStationName, this.vipLevel, this.faultType);
					workmanageWorkorder.setWoTitle(woTitle);
					
					//派发对象
					List<String> participantList = new ArrayList<String>();
					participantList.add(creator);
					
					
					//创建工单关联网络资源对象
					Workorderassnetresource workorderassnetresource=new Workorderassnetresource();
					workorderassnetresource.setNetworkResourceType(baseStationType==null?"":baseStationType);
					workorderassnetresource.setNetworkResourceId(((resourceId==null)||("".equals(resourceId)))?0l:Long.parseLong(resourceId));
					
					
					//根据基站id和基站类型，获取站址
//					Map<String,String> tempStationMap=this.networkResourceInterfaceService.getStationByBaseStationIdAndBaseStationType(resourceId, baseStationType,Constant.NETWORKRESOURCE_STATION);
					if(tempStationMap!=null){
						String tempStationId=tempStationMap.get("id")==null?"":tempStationMap.get("id").toString();
						workorderassnetresource.setStationId(Long.valueOf(tempStationId));
					}

					//创建服务过程跟踪记录对象
					Tasktracerecord tasktracerecord = new Tasktracerecord();
					tasktracerecord.setHandlerName(creatorName);
					tasktracerecord.setHandler(creator);
					tasktracerecord.setHandleTime(new Date());
					tasktracerecord.setHandleWay("创建工单");
					tasktracerecord.setWoType("抢修工单");
					tasktracerecord.setHandleResultDescription("【故障描述】"+urgentrepairWorkorder.getFaultDescription());
					
					//创建IOSM工单
					commonWorkOrderId=this.workManageService.createWorkOrder(WorkManageConstant.PROCESS_URGENTREPAIR_WORKORDER_CODE, workmanageWorkorder, 
							participantList, workorderassnetresource, tasktracerecord);
					
//					this.urgentRepairWorkOrderService.txCreateUrgentRepairWorkOrder(creator, urgentrepairWorkorder, null, workorderassnetresource, workmanageWorkorder);
				} catch (Exception e) {
					e.printStackTrace();
					pw.write("fail");
				}
			}
			
//			System.out.println("创建IOSM工单==="+commonWorkOrderId);
//			logger.info("create_IOSM工单==="+commonWorkOrderId);
			
			if(commonWorkOrderId!=null && !"".equals(commonWorkOrderId)){
				
				urgentrepairWorkorder.setWoId(commonWorkOrderId);
				this.urgentRepairWorkOrderDao.updateUrgentRepairWorkOrder(urgentrepairWorkorder);
				
				UrgentrepairCustomerworkorder customerWorkOrder=new UrgentrepairCustomerworkorder();
				customerWorkOrder.setCustomerWoTitle( woTitle==null?"":woTitle);	//设置iosm工单关联的客户工单信息
				customerWorkOrder.setCustomerWoId(customerWoId);	//设置客户工单id
				customerWorkOrder.setWoId(commonWorkOrderId);	//设置IOSM工单id
				this.urgentRepairCustomerWorkOrderDao.saveUrgentRepairCustomerWorkOrder(customerWorkOrder);
				
				
				pw.write(commonWorkOrderId+"&&"+creator);
			}else{
				pw.write("fail");
				return;
			}
		} catch (Exception e) {
			pw.write("fail");
			return;
		}finally{
			pw.close();
		}
	}
	
	/**
	 * 受理IOSM工单
	 */
	public void acceptIosmWorkOrderAction(){
		HttpServletResponse response = null;
		PrintWriter pw=null;
		try {
			response = ServletActionContext.getResponse();
			pw=response.getWriter();
			
			String acceptWorkOrderParam=new String(this.acceptWorkOrderParam.getBytes("ISO8859-1"),"UTF-8");
			Map<String, String> acceptWorkOrderParamMap=gson.fromJson(acceptWorkOrderParam, new TypeToken<Map<String, String>>(){}.getType());
			
			UrgentrepairWorkorder urgentrepairWorkorder=new UrgentrepairWorkorder();
			urgentrepairWorkorder.setWorkOrderAcceptedComment("已受理");
			if(acceptWorkOrderParamMap.get("handler")!=null&&!"".equals(acceptWorkOrderParamMap.get("handler"))&&acceptWorkOrderParamMap.get("woId")!=null&&!"".equals(acceptWorkOrderParamMap.get("woId"))){
				String woId = acceptWorkOrderParamMap.get("woId");
				String handler = acceptWorkOrderParamMap.get("handler");
				String flag = urgentRepairWorkOrderService.txAcceptUrgentRepairWorkOrder(handler, acceptWorkOrderParamMap.get("woId").toString(), urgentrepairWorkorder);
				pw.write(flag);
			}else{
				pw.write("fail");
			}
		} catch (Exception e) {
			pw.write("fail");
		}finally{
			pw.close();
		}
	}
	
	/**
	 * 获取站址所属的区域信息
	 * @param resourceType 资源类型
	 * @param resourceId 资源id标识
	 * @return
	 */
	private Map<String,String> transferAreaInfo(Map<String,String> tempStationMap){
		Map<String,String> areaMap=new HashMap<String,String>();
		try {
			
			if(tempStationMap!=null && !tempStationMap.isEmpty()){
				//获取站址所属镇级区域
				Map<String,String> resMap=this.networkResourceInterfaceService.getAreaByStationId(tempStationMap.get("id").toString(), tempStationMap.get("_entityType").toString());
				if(resMap!=null){
					areaMap.put("town", resMap.get("name").toString());
					//获取区级
					resMap=this.networkResourceInterfaceService.getParentAreaByAreaId(resMap.get("id").toString(), resMap.get("_entityType").toString());
					if(resMap!=null){
						areaMap.put("town", resMap.get("name").toString());
						//获取市级
						resMap=this.networkResourceInterfaceService.getParentAreaByAreaId(resMap.get("id").toString(), resMap.get("_entityType").toString());
						if(resMap!=null){
							areaMap.put("town", resMap.get("name").toString());
							//获取省级
							resMap=this.networkResourceInterfaceService.getParentAreaByAreaId(resMap.get("id").toString(), resMap.get("_entityType").toString());
							if(resMap!=null){
								areaMap.put("town", resMap.get("name").toString());
							}
						}
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return areaMap;
	}
	
	
	
	
	/**
	 * 故障处理截止时间
	 * @param baseStationLevel
	 * @param faultLevel
	 * @param faultOccuredTime
	 * @return
	 */
	private Date calculateTime(String baseStationLevel,String faultLevel,String faultOccuredTime){
		Date date=new Date();
		int val=0;
		try {
			if("普通".equals(baseStationLevel)){
				if("A1".equals(faultLevel)){
					val=6;
				}else if("A2".equals(faultLevel)){
					val=12;
				}else if("A3".equals(faultLevel)){
					val=24;
				}
			}else if("vip".equals(baseStationLevel)){
				if("A1".equals(faultLevel)){
					val=4;
				}else if("A2".equals(faultLevel)){
					val=8;
				}else if("A3".equals(faultLevel)){
					val=16;
				}
			}
			date=TimeFormatHelper.setTimeFormat(faultOccuredTime);
			date.setHours(date.getHours()+val);
			
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	
	/**
	 * 格式化告警内容的显示格式
	 * @param param
	 * @return
	 */
	private String constructAlarmContent(String param){
		logger.info("param : "+param);
		String res="";
		try {
			int flagIndex=param.indexOf(")(");
			if(flagIndex!=-1){	//如果有多个告警内容
				logger.info("--------------多个警告----------");
				param=param.substring(0, flagIndex+1)+"#"+param.substring(flagIndex+1);
				flagIndex=param.indexOf(")(");
				while(flagIndex!=-1){
					param=param.substring(0, flagIndex+1)+"#"+param.substring(flagIndex+1);
					flagIndex=param.indexOf(")(");
					logger.info("param ："+param);
					logger.info("flagIndex ："+flagIndex);
				}
				
				String[] alarmContentArray=param.split("#");
//				System.out.println("alarmContentArray.length=="+alarmContentArray.length);
				
				List<String> replaeAlarmContent=new ArrayList<String>();
				for(int i=0;i<alarmContentArray.length;i++){
					String temp=alarmContentArray[i];
					if(i!=0){
						//temp="\r\n"+temp;
						temp="<br/>"+temp;
					}
					temp=temp.replace("AlarmTime", "告警时间").replace("AlarmType", "告警类型")
					.replace("AlarmLevel", "告警级别").replace("AlarmObject", "告警对象").replace("AlarmContent", "告警内容");
					replaeAlarmContent.add(temp);
				}
				
				for(String str:replaeAlarmContent){
					res=res+str;
				}
				logger.info("res ："+res);
			}else{
				logger.info("--------------单个警告----------");
				res=param.replace("AlarmTime", "告警时间").replace("AlarmType", "告警类型")
				.replace("AlarmLevel", "告警级别").replace("AlarmObject", "告警对象").replace("AlarmContent", "告警内容");
				logger.info("res ："+res);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 构造工单主题
	 * @param cityName 
	 * @param stationName
	 * @param vipLevel
	 * @param faultType
	 * @return
	 */
	private String constructWoTitle(String cityName,String stationName,String vipLevel,String faultType){
		String res="";
		try {
			res=cityName+"【"+stationName+"（"+vipLevel+"）】"+faultType;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	
	/**
	 * 对参数进行解码
	 * @param param
	 * @return
	 */
	@Deprecated
	private String decodeParam(String param){
		try {
			String decodeParam=new String(param.getBytes("ISO8859-1"),"UTF-8");
			return decodeParam;
		} catch (Exception e) {
			
		}
		return "";
	}

	
	
}
