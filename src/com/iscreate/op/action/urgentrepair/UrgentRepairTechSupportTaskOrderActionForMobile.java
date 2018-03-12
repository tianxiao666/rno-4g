package com.iscreate.op.action.urgentrepair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.constant.OrganizationConstant;
import com.iscreate.op.constant.TreeConstant;
import com.iscreate.op.dao.publicinterface.WorkOrderAssnetResourceDao;
import com.iscreate.op.pojo.publicinterface.Tasktracerecord;
import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairCustomerworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairTechsupporttaskorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.publicinterface.TaskTracingRecordService;
import com.iscreate.op.service.publicinterface.WorkOrderCommonService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.urgentrepair.BizResourceReader;
import com.iscreate.op.service.urgentrepair.UrgentRepairCustomerWorkOrderService;
import com.iscreate.op.service.urgentrepair.UrgentRepairTechSupportTaskOrderService;
import com.iscreate.op.service.urgentrepair.UrgentRepairWorkOrderService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.datadictionary.DataDictionaryService;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageCommunicationHelper;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.tree.TreeNode;

public class UrgentRepairTechSupportTaskOrderActionForMobile {
	//注入service
	private UrgentRepairWorkOrderService urgentRepairWorkOrderService;
	private WorkManageService workManageService;
	private TaskTracingRecordService taskTracingRecordService;
	private NetworkResourceInterfaceService networkResourceService;
	private WorkOrderAssnetResourceDao workOrderAssnetResourceDao;
	private UrgentRepairTechSupportTaskOrderService urgentRepairTechSupportTaskOrderService;
	private BizMessageService bizMessageService;
	private DataDictionaryService dataDictionaryService;
	private WorkOrderCommonService workOrderCommonService;
	private UrgentRepairCustomerWorkOrderService urgentRepairCustomerWorkOrderService;
	private SysOrgUserService sysOrgUserService;
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	
	
	private  static final  Log log = LogFactory.getLog(UrgentRepairTechSupportTaskOrderActionForMobile.class);
	
	/**
	 * 显示专家处理单的页面内容
	 * @return
	 */
	public void loadUrgentRepairTechSupportTaskOrderPageActionForMobile(){
		log.info("进入 loadUrgentRepairTechSupportTaskOrderPageActionForMobile()");
		log.info("loadUrgentRepairTechSupportTaskOrderPageActionForMobile() 显示现场处理单的页面内容");
		try {
			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();
			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);

			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			mobilePackage = getTaskOrderMobilePackageByToId(toId,mobilePackage,mch,formJsonMap);

			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 loadUrgentRepairTechSupportTaskOrderPageActionForMobile()");
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadUrgentRepairTechSupportTaskOrderPageActionForMobile()");
		} 
	}
	
	/**
	 * 根据任务单id获取其信息的终端包的content
	 * @param ToId
	 * @param mobilePackage
	 * @param mch
	 * @param formJsonMap
	 * @return
	 */
	public MobilePackage getTaskOrderMobilePackageByToId(String toId ,MobilePackage mobilePackage ,MobileContentHelper mch, Map<String, String> formJsonMap){
		log.info("进入 getTaskOrderMobilePackageByToId()");
		log.info("getTaskOrderMobilePackageByToId() 根据任务单id获取其信息的终端包的content");
		//通过TOID显示任务单信息
		Map commonTaskInfo = workManageService.getTaskOrderForShow(toId);
		UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder =  urgentRepairTechSupportTaskOrderService.getUrgentRepairTechSupportTaskOrder(toId);
		if(commonTaskInfo == null) {
			return null;
		}else{
			String assignTimeStr = "";
			if(commonTaskInfo.get("assignTime")!=null){
				assignTimeStr = TimeFormatHelper.getTimeFormatByMin(commonTaskInfo.get("assignTime").toString());
				commonTaskInfo.put("assignTime", assignTimeStr);
			}
			String requireCompleteTimeStr = "";
			if(commonTaskInfo.get("requireCompleteTime")!=null){
				requireCompleteTimeStr = TimeFormatHelper.getTimeFormatByMin(commonTaskInfo.get("requireCompleteTime").toString());
				commonTaskInfo.put("requireCompleteTime", requireCompleteTimeStr);
			}
			
			if(urgentrepairTechsupporttaskorder==null){
				return null;
			}
			
			String affectedServiceName ="";
			if(urgentrepairTechsupporttaskorder.getAffectedServiceName()!=null){
				affectedServiceName = urgentrepairTechsupporttaskorder.getAffectedServiceName();
			}
			commonTaskInfo.put("affectedServiceName", affectedServiceName);
			String faultGenera ="";
			if(urgentrepairTechsupporttaskorder.getFaultGenera()!=null){
				faultGenera = urgentrepairTechsupporttaskorder.getFaultGenera();
			}
			commonTaskInfo.put("faultGenera", faultGenera);
			String faultReason ="";
			if(urgentrepairTechsupporttaskorder.getFaultReason()!=null){
				faultReason = urgentrepairTechsupporttaskorder.getFaultReason();
			}
			commonTaskInfo.put("faultReason", faultReason);
			String faultHandleDetail ="";
			if(urgentrepairTechsupporttaskorder.getFaultHandleDetail()!=null){
				faultHandleDetail = urgentrepairTechsupporttaskorder.getFaultHandleDetail();
			}
			commonTaskInfo.put("faultHandleDetail", faultHandleDetail);
			String faultHandleResult ="";
			if("1".equals(urgentrepairTechsupporttaskorder.getFaultHandleResult())){
				faultHandleResult = "解决";
			}else{
				faultHandleResult = "延迟解决";
			}
			commonTaskInfo.put("faultHandleResult",faultHandleResult);
			String reasonForDelayApply ="";
			if(urgentrepairTechsupporttaskorder.getReasonForDelayApply()!=null){
				reasonForDelayApply = urgentrepairTechsupporttaskorder.getReasonForDelayApply();
			}
			commonTaskInfo.put("reasonForDelayApply", reasonForDelayApply);
			//告警清除时间
			commonTaskInfo.put("alarmClearTime", TimeFormatHelper.getTimeFormatByMin(urgentrepairTechsupporttaskorder.getAlarmClearTime()));
			//故障解决时间
			commonTaskInfo.put("faultSolveTime", TimeFormatHelper.getTimeFormatByMin(urgentrepairTechsupporttaskorder.getFaultSolveTime()));
			//延迟解决时间
			commonTaskInfo.put("foreseeSolveTime", TimeFormatHelper.getTimeFormatByMin(urgentrepairTechsupporttaskorder.getForeseeSolveTime()));
			String sideeffectService ="";
			if(urgentrepairTechsupporttaskorder.getSideeffectService()!=null){
				sideeffectService = urgentrepairTechsupporttaskorder.getSideeffectService()+"";
			}
			commonTaskInfo.put("sideeffectService", sideeffectService);
			
			commonTaskInfo.put("TOID", commonTaskInfo.get("toId"));
			if(commonTaskInfo.get("currentHandler")!=null){
				//ou.jh
				SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(commonTaskInfo.get("currentHandler")+"");
//				Account ca =providerOrganizationService.getAccountByAccountId(commonTaskInfo.get("currentHandler")+"");
				String phone = "";
				if(sysOrgUserByAccount.getMobile()!=null){
					phone = sysOrgUserByAccount.getMobile();
				}else{
					phone="无电话号码";
				}
				commonTaskInfo.put("operatorPhone", phone);
				
				//更新消息盒子
				bizMessageService.updateBizMsgToHasReadByReceivePersonAndOrderIdService(commonTaskInfo.get("currentHandler").toString(),toId); 
			}
			if(commonTaskInfo.get("assigner")!=null){
				//ou.jh
				SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(commonTaskInfo.get("assigner")+"");
//				Account aa =providerOrganizationService.getAccountByAccountId(commonTaskInfo.get("assigner")+"");
				String phone = "";
				if(sysOrgUserByAccount.getMobile()!=null){
					phone = sysOrgUserByAccount.getMobile();
				}else{
					phone="无电话号码";
				}
				commonTaskInfo.put("assignedPersonPhone", phone);
			}
			mch.addGroup("job_menu_lev1_0", commonTaskInfo); // 任务单信息
		}
		
		//判断是否拥有操作权限
		if(commonTaskInfo.get("currentHandler")!=null&&!"".equals(commonTaskInfo.get("currentHandler"))){
			String userId = (String) SessionService.getInstance().getValueByKey(
			"userId");
			if(userId.equals(commonTaskInfo.get("currentHandler"))){
				commonTaskInfo.put("hasPower", "yes");
			}else{
				commonTaskInfo.put("hasPower", "no");
			}
		}
		
		mch.addGroup("header", formJsonMap);
		mobilePackage.setContent(mch.mapToJson());
		log.info("退出 getTaskOrderMobilePackageByToId()");
		return mobilePackage;
	}
	
	/**
	 * 加载专家处理工单
	 */
	public void loadUrgentRepairTechSupportWorkOrderPageActionForMobile() {
		log.info("进入 loadUrgentRepairTechSupportWorkOrderPageActionForMobile()");
		log.info("getTaskOrderMobilePackageByToId() 加载专家处理工单");
		try {
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();

			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);

			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			// 获取工单信息
			Map<String,String> workOrderInfo = new HashMap<String,String>();
			UrgentrepairWorkorder urgentrepairWorkorder = urgentRepairWorkOrderService.getUrgentRepairWorkOrder(woId);
			if (urgentrepairWorkorder != null) {
				Map workOrderMap  = workManageService.getWorkOrderForShow(woId);
				if(workOrderMap!=null){
					if(workOrderMap.get("woTitle")!=null){
						workOrderInfo.put("woTitle", workOrderMap.get("woTitle")+"");
					}
				}
				if(urgentrepairWorkorder.getAcceptProfessional()!=null){
					workOrderInfo.put("acceptProfessional", urgentrepairWorkorder.getAcceptProfessional()+"");
				}
				if( urgentrepairWorkorder.getBaseStationLevel()!=null){
					workOrderInfo.put("baseStationLevel", urgentrepairWorkorder.getBaseStationLevel()+"");
				}
				if( urgentrepairWorkorder.getFaultArea()!=null){
					workOrderInfo.put("faultArea", urgentrepairWorkorder.getFaultArea()+"");
				}
				if( urgentrepairWorkorder.getStationName()!=null){
					workOrderInfo.put("stationName", urgentrepairWorkorder.getStationName()+"");
				}
				
				//故障发生时间
				String faultOccuredTime = "";
				if(urgentrepairWorkorder.getFaultOccuredTime() != null&&!"".equals(urgentrepairWorkorder.getFaultOccuredTime())) {
						faultOccuredTime = TimeFormatHelper.getTimeFormatByMin(urgentrepairWorkorder.getFaultOccuredTime());
				}
				workOrderInfo.put("faultOccuredTime", faultOccuredTime);
				//故障处理时限
				String latestAllowedTime = "";
				if(urgentrepairWorkorder.getLatestAllowedTime() != null&&!"".equals(urgentrepairWorkorder.getLatestAllowedTime())) {
					latestAllowedTime = TimeFormatHelper.getTimeFormatByMin(urgentrepairWorkorder.getLatestAllowedTime());
				}
				workOrderInfo.put("latestAllowedTime", latestAllowedTime);
				if(urgentrepairWorkorder.getFaultDescription()!=null){
					workOrderInfo.put("faultDescription", urgentrepairWorkorder.getFaultDescription()+"");
				}
				if(urgentrepairWorkorder.getFaultType()!=null){
					workOrderInfo.put("faultType", urgentrepairWorkorder.getFaultType()+"");
				}
				if( urgentrepairWorkorder.getFaultLevel()!=null){
					workOrderInfo.put("faultLevel", urgentrepairWorkorder.getFaultLevel()+"");
				}
				if(urgentrepairWorkorder.getNetElementName()!=null){
					workOrderInfo.put("netElementName", urgentrepairWorkorder.getNetElementName()+"");
				}
				if( urgentrepairWorkorder.getFaultStationName()!=null){
					workOrderInfo.put("faultStationName", urgentrepairWorkorder.getFaultStationName()+"");
				}
				if( urgentrepairWorkorder.getFaultStationAddress()!=null){
					workOrderInfo.put("faultStationAddress", urgentrepairWorkorder.getFaultStationAddress()+"");
				}
				
				if (urgentrepairWorkorder.getSideeffectService()!= null) {
					if ("1".equals(urgentrepairWorkorder.getSideeffectService().toString())) {
						workOrderInfo.put("sideeffectService", "是");
					} else if ("0".equals(urgentrepairWorkorder.getSideeffectService())) {
						workOrderInfo.put("sideeffectService", "否");
					} else if ("-1".equals(urgentrepairWorkorder.getSideeffectService())) {
						workOrderInfo.put("sideeffectService", "");
					}
				}

				if (urgentrepairWorkorder.getFaultLevel() != null) {
					if ("A1".equals(urgentrepairWorkorder.getFaultLevel()
							.toString())) {
						workOrderInfo.put("criticalClass", "extraurgent");
					} else {
						workOrderInfo.put("criticalClass", "urgent");
					}
				}
			
				//****** 创建工单信息 *********************
				if(urgentrepairWorkorder.getCustomerWoType()!=null){
					if("home2g".equals(urgentrepairWorkorder.getCustomerWoType())){
						Map cwoMap = workOrderCommonService.getUrgentRepairHome2GWorkOrderService(woId);
						if(cwoMap!=null){
							workOrderInfo.putAll(cwoMap);
							workOrderInfo.put("customerWoType", urgentrepairWorkorder.getCustomerWoType());
						}
					}else if("homeTd".equals(urgentrepairWorkorder.getCustomerWoType())){
						Map cwoMap = workOrderCommonService.getUrgentRepairHomeTdWorkOrderService(woId);
						if(cwoMap!=null){
							workOrderInfo.putAll(cwoMap);
							workOrderInfo.put("customerWoType", urgentrepairWorkorder.getCustomerWoType());
						}
					}else{
						List<UrgentrepairCustomerworkorder> ctos = urgentRepairCustomerWorkOrderService.getUrgentrepairCustomerWorkorderByWoId(woId);
						if(ctos!=null&&!ctos.isEmpty()){
							UrgentrepairCustomerworkorder cto = ctos.get(0);
							if(cto.getCustomerWoId()!=null){
								workOrderInfo.put("customerWoId", cto.getCustomerWoId()+"");
							}
							if(cto.getSubAlarmInfo()!=null){
								workOrderInfo.put("subAlarmInfo", cto.getSubAlarmInfo()+"");
							}
							if(cto.getCustomerWoTitle()!=null){
								workOrderInfo.put("customerWoTitle", cto.getCustomerWoTitle()+"");
							}
							if(cto.getCustomerSenderDepartment()!=null){
								workOrderInfo.put("customerSenderDepartment", cto.getCustomerSenderDepartment()+"");
							}
							if(cto.getCustomerWoSender()!=null){
								workOrderInfo.put("customerWoSender", cto.getCustomerWoSender()+"");
							}
							if(cto.getSendWoWay()!=null){
								workOrderInfo.put("sendWoWay", cto.getSendWoWay()+"");
							}
							if(cto.getCustomerWoCurrentDepartment()!=null){
								workOrderInfo.put("customerWoCurrentDepartment", cto.getCustomerWoCurrentDepartment()+"");
							}
							if(cto.getCustomerWoCurrentHandler()!=null){
								workOrderInfo.put("customerWoCurrentHandler", cto.getCustomerWoCurrentHandler()+"");
							}
							if(cto.getIsEmergencyFault()!=null){
								workOrderInfo.put("isEmergencyFault", cto.getIsEmergencyFault()+"");
							}
							if(cto.getSideeffectService()!=null){
								if(cto.getSideeffectService().equals("1")){
									workOrderInfo.put("sideeffectService","是");
								}else if(cto.getSideeffectService().equals("0")){
									workOrderInfo.put("sideeffectService", "否");
								}
								
							}
							if(cto.getAffectedServiceName()!=null){
								workOrderInfo.put("affectedServiceName", cto.getAffectedServiceName()+"");
							}
							if(cto.getAlarmFormalName()!=null){
								workOrderInfo.put("alarmFormalName", cto.getAlarmFormalName()+"");
							}
							if(cto.getAlarmNetManageSource()!=null){
								workOrderInfo.put("alarmNetManageSource", cto.getAlarmNetManageSource()+"");
							}
							if(cto.getAlarmLogicalClass()!=null){
								workOrderInfo.put("alarmLogicalClass", cto.getAlarmLogicalClass()+"");
							}
							if(cto.getAlarmLogicalSubClass()!=null){
								workOrderInfo.put("alarmLogicalSubClass", cto.getAlarmLogicalSubClass()+"");
							}
							if(cto.getAlarmClass()!=null){
								workOrderInfo.put("alarmClass", cto.getAlarmClass()+"");
							}
							if(cto.getNetmanageAlarmLevel()!=null){
								workOrderInfo.put("netmanageAlarmLevel", cto.getNetmanageAlarmLevel()+"");
							}
							if(cto.getAlarmAssociatedId()!=null){
								workOrderInfo.put("alarmAssociatedId", cto.getAlarmAssociatedId()+"");
							}
							if(cto.getSubAlarmNumber()!=null){
								workOrderInfo.put("subAlarmNumber", cto.getSubAlarmNumber()+"");
							}
						}
					}	
					
				}else{
					List<UrgentrepairCustomerworkorder> ctos = urgentRepairCustomerWorkOrderService.getUrgentrepairCustomerWorkorderByWoId(woId);
					if(ctos!=null&&!ctos.isEmpty()){
						UrgentrepairCustomerworkorder cto = ctos.get(0);
						if(cto.getCustomerWoId()!=null){
							workOrderInfo.put("customerWoId", cto.getCustomerWoId()+"");
						}
						if(cto.getSubAlarmInfo()!=null){
							workOrderInfo.put("subAlarmInfo", cto.getSubAlarmInfo()+"");
						}
						if(cto.getCustomerWoTitle()!=null){
							workOrderInfo.put("customerWoTitle", cto.getCustomerWoTitle()+"");
						}
						if(cto.getCustomerSenderDepartment()!=null){
							workOrderInfo.put("customerSenderDepartment", cto.getCustomerSenderDepartment()+"");
						}
						if(cto.getCustomerWoSender()!=null){
							workOrderInfo.put("customerWoSender", cto.getCustomerWoSender()+"");
						}
						if(cto.getSendWoWay()!=null){
							workOrderInfo.put("sendWoWay", cto.getSendWoWay()+"");
						}
						if(cto.getCustomerWoCurrentDepartment()!=null){
							workOrderInfo.put("customerWoCurrentDepartment", cto.getCustomerWoCurrentDepartment()+"");
						}
						if(cto.getCustomerWoCurrentHandler()!=null){
							workOrderInfo.put("customerWoCurrentHandler", cto.getCustomerWoCurrentHandler()+"");
						}
						if(cto.getIsEmergencyFault()!=null){
							workOrderInfo.put("isEmergencyFault", cto.getIsEmergencyFault()+"");
						}
						if(cto.getSideeffectService()!=null){
							if(cto.getSideeffectService().equals("1")){
								workOrderInfo.put("sideeffectService","是");
							}else if(cto.getSideeffectService().equals("0")){
								workOrderInfo.put("sideeffectService", "否");
							}
						}
						if(cto.getAffectedServiceName()!=null){
							workOrderInfo.put("affectedServiceName", cto.getAffectedServiceName()+"");
						}
						if(cto.getAlarmFormalName()!=null){
							workOrderInfo.put("alarmFormalName", cto.getAlarmFormalName()+"");
						}
						if(cto.getAlarmNetManageSource()!=null){
							workOrderInfo.put("alarmNetManageSource", cto.getAlarmNetManageSource()+"");
						}
						if(cto.getAlarmLogicalClass()!=null){
							workOrderInfo.put("alarmLogicalClass", cto.getAlarmLogicalClass()+"");
						}
						if(cto.getAlarmLogicalSubClass()!=null){
							workOrderInfo.put("alarmLogicalSubClass", cto.getAlarmLogicalSubClass()+"");
						}
						if(cto.getAlarmClass()!=null){
							workOrderInfo.put("alarmClass", cto.getAlarmClass()+"");
						}
						if(cto.getNetmanageAlarmLevel()!=null){
							workOrderInfo.put("netmanageAlarmLevel", cto.getNetmanageAlarmLevel()+"");
						}
						if(cto.getAlarmAssociatedId()!=null){
							workOrderInfo.put("alarmAssociatedId", cto.getAlarmAssociatedId()+"");
						}
						if(cto.getSubAlarmNumber()!=null){
							workOrderInfo.put("subAlarmNumber", cto.getSubAlarmNumber()+"");
						}
					}
				}
			}
			
			if (workOrderInfo != null) {
				workOrderInfo.put("WOID", workOrderInfo.get("woId"));
				mch.addGroup("job_menu_lev1_1", workOrderInfo); // 工单信息
			}
			
			//其它类型工单
			
			mobilePackage.setContent(mch.mapToJson());

			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 loadUrgentRepairTechSupportWorkOrderPageActionForMobile()");
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadUrgentRepairTechSupportWorkOrderPageActionForMobile()");
		}
	}
	
	/**
	 * 加载专家处理相关资源
	 */
	public void loadUrgentRepairTechSupportAssResPageActionForMobile() {
		log.info("进入 loadUrgentRepairTechSupportAssResPageActionForMobile()");
		log.info("loadUrgentRepairTechSupportAssResPageActionForMobile() 加载专家处理相关资源");
		try {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();

			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				// 返回content的JSON字符串信息
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);

			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			//业务设计，只拿一个基站，所以只需把第一个基站的ID 和类型保存即可
			String networkResourceType = "";
			long networkResourceId = 0;
			List<Workorderassnetresource> workorderassnetresources =  workOrderAssnetResourceDao.getWorkOrderAssnetResourceRecordDao("woId", woId);
			
			if(workorderassnetresources.get(0).getNetworkResourceType()!=null){
				networkResourceType = workorderassnetresources.get(0).getNetworkResourceType();
			}
			
			if(workorderassnetresources.get(0).getNetworkResourceId()!=null){
				networkResourceId = workorderassnetresources.get(0).getNetworkResourceId();
			}
			
			if(workorderassnetresources!=null&&!workorderassnetresources.isEmpty()){
				Map associatedAlarmResourceMap = new HashMap();
				List<Map> associatedAlarmResourceList = new ArrayList<Map>();
				List<Map> tempList = new ArrayList<Map>();
				for(Workorderassnetresource workorderassnetresource : workorderassnetresources){
					Map<String,Map<String,String>> baseFacilityMap = networkResourceService.getBaseFacilityToMapService(workorderassnetresource.getNetworkResourceId()+"", workorderassnetresource.getNetworkResourceType());
					if(baseFacilityMap!=null){
						Map<String,String > dictionaries = baseFacilityMap.get("dictionary");
						Map<String,String > entities = baseFacilityMap.get("entity");
						for(String key : dictionaries.keySet()){
							Map aarMap = new HashMap();
							aarMap.put("cnName",dictionaries.get(key));
							if(entities.get(key)==null||"null".equals(entities.get(key))){
								aarMap.put("value", "");
							}else{
								aarMap.put("value", entities.get(key));
							}
							tempList.add(aarMap);
						}
						Map map = new HashMap();
						map.put("networkResourceList", gson.toJson(tempList));
						if(workorderassnetresource.getNetworkResourceType()!=null){
							map.put("networkResourceType", workorderassnetresource.getNetworkResourceType());
							map.put("networkResourceId", workorderassnetresource.getNetworkResourceId());
							map.put("stationId", workorderassnetresource.getStationId());
						}
						associatedAlarmResourceList.add(map);
					}
				}
				associatedAlarmResourceMap.put("associatedAlarmResource", gson.toJson(associatedAlarmResourceList));
				mch.addGroup("associatedAlarmResourceArea",associatedAlarmResourceMap); // 关联的网络资源
				
				//维护记录
				List<Map<String,String>> maintainRecords = networkResourceService.getHandleResourceMaintainRecordService(woId, networkResourceId+"", networkResourceType);
				if(maintainRecords!=null&&!maintainRecords.isEmpty()){
					Map maintainRecordMap =  new HashMap();
					maintainRecordMap.put("maintainRecordDiv", gson.toJson(maintainRecords));
					mch.addGroup("maintainRecordArea",maintainRecordMap);
				}
			}
			
			mobilePackage.setContent(mch.mapToJson());

			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 loadUrgentRepairTechSupportAssResPageActionForMobile()");
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadUrgentRepairTechSupportAssResPageActionForMobile()");
		} 
	}
	
	/**
	 * 加载专家处理服务跟踪记录
	 */
	public void loadUrgentRepairTechSupportTraceRecordPageActionForMobile() {
		log.info("进入 loadUrgentRepairTechSupportTraceRecordPageActionForMobile()");
		log.info("loadUrgentRepairTechSupportTraceRecordPageActionForMobile() 加载专家处理服务跟踪记录");
		try {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();

			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				// 返回content的JSON字符串信息
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);

			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			// 服务跟踪记录
			List<Map> serviceTraceRecord = new ArrayList<Map>();
			List<Tasktracerecord> taskTracingRecords  = taskTracingRecordService.getTaskTracingRecordByKeyService("woId", woId);
			if(taskTracingRecords!=null&&!taskTracingRecords.isEmpty()){
				for(Tasktracerecord tasktracerecord : taskTracingRecords){
					Map map = new HashMap();
					if(tasktracerecord.getHandler()!=null){
						map.put("handler", tasktracerecord.getHandler());
					}
					if( tasktracerecord.getHandleResultDescription()!=null){
						map.put("handleResultDescription", tasktracerecord.getHandleResultDescription());
					}
					if( tasktracerecord.getHandlerName()!=null){
						map.put("handlerName", tasktracerecord.getHandlerName());
					}
					if( tasktracerecord.getHandleWay()!=null){
						map.put("handleWay", tasktracerecord.getHandleWay());
					}
					if(tasktracerecord.getToId()!=null){
						map.put("toId", tasktracerecord.getToId());
					}
					if(tasktracerecord.getWoId()!=null){
						map.put("woId", tasktracerecord.getWoId());
					}
					if( tasktracerecord.getWoType()!=null){
						map.put("woType", tasktracerecord.getWoType());
					}
					
					String handleTime = "";
					if(tasktracerecord.getHandleTime() != null&&!"".equals(tasktracerecord.getHandleTime())) {
						handleTime = TimeFormatHelper.getTimeFormatByMin(tasktracerecord.getHandleTime());
					}
					map.put("handleTime", handleTime);
					
					serviceTraceRecord.add(map);
				}
				if (serviceTraceRecord != null) {
					Map<String, String> traceRecordMap = new HashMap<String, String>();
					traceRecordMap.put("traceRecord", gson.toJson(serviceTraceRecord));
					mch.addGroup("traceRecordArea", traceRecordMap);
				}
			}
			mobilePackage.setContent(mch.mapToJson());

			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 loadUrgentRepairTechSupportTraceRecordPageActionForMobile()");
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadUrgentRepairTechSupportTraceRecordPageActionForMobile()");
		} 
	}
	
	/**
	 * 受理专家处理单
	 */
	public void acceptUrgentRepairTechSupportTaskOrderActionForMobile(){
		log.info("进入 acceptUrgentRepairTechSupportTaskOrderActionForMobile()");
		log.info("acceptUrgentRepairTechSupportTaskOrderActionForMobile() 加载专家处理服务跟踪记录");
		try {
			
			String accept = (String) SessionService.getInstance().getValueByKey(
			"userId");

			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				// 返回content的JSON字符串信息
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);

			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			
			// 更新任务单
			WorkmanageTaskorder workmanageTaskorder = workManageService.getTaskOrderEntity(toId);
			workmanageTaskorder.setAcceptComment("已受理");
			workmanageTaskorder.setAcceptPeople(accept);
			workmanageTaskorder.setAcceptTime(new Date());
			//更改受理状态为受理中，状态号为6
			boolean isOk = urgentRepairTechSupportTaskOrderService.txAcceptUrgentRepairTechSupportTaskOrder(accept, woId, toId, workmanageTaskorder);
			if (!isOk) {
				// 返回content的JSON字符串信息
				log.error("acceptUrgentRepairTechSupportTaskOrderActionForMobile 调用 urgentRepairTechSupportTaskOrderService.txAcceptUrgentRepairTechSupportTaskOrder 终端专家任务单受理失败");
				MobilePackageCommunicationHelper.responseMobileError("acceptUrgentRepairTechSupportTaskOrderActionForMobile 调用 urgentRepairTechSupportTaskOrderService.txAcceptUrgentRepairTechSupportTaskOrder 终端专家任务单受理失败");
				log.info("退出 acceptUrgentRepairTechSupportTaskOrderActionForMobile()");
			}else{
				//受理完毕后，重新加载
				//组装mobilepackage
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
//				//通过TOID显示任务单信息
//				Map<String,String> commonTaskInfo = workManageService.getTaskOrderForShow(toId);
//				if(commonTaskInfo == null) {
//					MobilePackage errMobilePackage = new MobilePackage();
//					errMobilePackage.setResult("error");
//					// 返回content的JSON字符串信息
//					MobilePackageCommunicationHelper.responseMobilePacageAndResult(errMobilePackage);
//					return;
//				}else{
//					commonTaskInfo.put("TOID", commonTaskInfo.get("toId"));
//					mch.addGroup("job_menu_lev1_0", commonTaskInfo); // 任务单信息
//				}
//				mch.addGroup("header", formJsonMap);
//				newMobilePackage.setContent(mch.mapToJson());
				MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
				log.info("退出 acceptUrgentRepairTechSupportTaskOrderActionForMobile()");
			}	
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 acceptUrgentRepairTechSupportTaskOrderActionForMobile()");
		} 
	}
	
	/**
	 * 加载驳回页面
	 */
	public void loadRejectTechSupportTaskOrderActionForMobile() {
		log.info("进入 loadRejectTechSupportTaskOrderActionForMobile()");
		log.info("loadRejectTechSupportTaskOrderActionForMobile() 加载驳回页面");
		try {

			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);

			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+"]");
			// 将WOID、toId、bizunitInstanceId带到阶段回复页面
			MobilePackage newMobilePackage = new MobilePackage();
			mch.addGroup("responseForm", formJsonMap);
			newMobilePackage.setContent(mch.mapToJson());
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
			log.info("退出 loadRejectTechSupportTaskOrderActionForMobile()");
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadRejectTechSupportTaskOrderActionForMobile()");
		} 
	}
	
	/**
	 * 驳回专家处理单
	 */
	public void rejectUrgentRepairTechSupportTaskOrderActionForMobile(){
		log.info("进入 rejectUrgentRepairTechSupportTaskOrderActionForMobile()");
		log.info("rejectUrgentRepairTechSupportTaskOrderActionForMobile() 驳回专家处理单");
		try {
			String accept = (String) SessionService.getInstance().getValueByKey(
			"userId");

			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);

			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			// 更新任务单
			WorkmanageTaskorder workmanageTaskorder = workManageService.getTaskOrderEntity(toId);
			workmanageTaskorder.setAcceptComment("已受理");
			workmanageTaskorder.setAcceptPeople(accept);
			workmanageTaskorder.setAcceptTime(new Date());
			//更改受理状态为受理中，状态号为6
			boolean isOk = urgentRepairTechSupportTaskOrderService.txRejectUrgentRepairTechSupportTaskOrder(accept, woId, toId, workmanageTaskorder);
			if (!isOk) {
				// 返回content的JSON字符串信息
				log.error("rejectUrgentRepairTechSupportTaskOrderActionForMobile 调用 urgentRepairTechSupportTaskOrderService.txRejectUrgentRepairTechSupportTaskOrder 专家任务单驳回失败");
				MobilePackageCommunicationHelper.responseMobileError("rejectUrgentRepairTechSupportTaskOrderActionForMobile 调用 urgentRepairTechSupportTaskOrderService.txRejectUrgentRepairTechSupportTaskOrder 专家任务单驳回失败");
				log.info("退出 rejectUrgentRepairTechSupportTaskOrderActionForMobile()");
			}else{
				//受理完毕后，重新加载
				//组装mobilepackage
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
//				//通过TOID显示任务单信息
//				Map<String,String> commonTaskInfo = workManageService.getTaskOrderForShow(toId);
//				if(commonTaskInfo == null) {
//					MobilePackage errMobilePackage = new MobilePackage();
//					errMobilePackage.setResult("error");
//					// 返回content的JSON字符串信息
//					MobilePackageCommunicationHelper.responseMobilePacageAndResult(errMobilePackage);
//					return;
//				}else{
//					commonTaskInfo.put("TOID", commonTaskInfo.get("toId"));
//					mch.addGroup("job_menu_lev1_0", commonTaskInfo); // 任务单信息
//				}
//				mch.addGroup("header", formJsonMap);
//				newMobilePackage.setContent(mch.mapToJson());
				MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
				log.info("退出 rejectUrgentRepairTechSupportTaskOrderActionForMobile()");
			}	
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 rejectUrgentRepairTechSupportTaskOrderActionForMobile()");
		} 
	}	
	
	
	
	/**
	 * 预填专家处理的最终回复信息
	 * @return
	 */
	public void preFillUrgentRepairTechSupportLastReplyContentActionForMobile() {
		log.info("进入 preFillUrgentRepairTechSupportLastReplyContentActionForMobile()");
		log.info("preFillUrgentRepairTechSupportLastReplyContentActionForMobile() 预填专家处理的最终回复信息");
		// 预填子任务最终回复
		try {

			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);

			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			HttpSession session = ServletActionContext.getRequest().getSession();
			String actor = (String) SessionService.getInstance().getValueByKey(
			"userId");
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			UrgentrepairWorkorder urgentrepairWorkorder = urgentRepairWorkOrderService.getUrgentRepairWorkOrder(woId);
			if(urgentrepairWorkorder!=null){
				if( urgentrepairWorkorder.getAcceptProfessional()!=null){
					formJsonMap.put("acceptProfessional", urgentrepairWorkorder.getAcceptProfessional());
				}
				if(urgentrepairWorkorder.getCustomerWoType()!=null){
					formJsonMap.put("customerWoType",urgentrepairWorkorder.getCustomerWoType());
				}
			}
			
			String acceptProfessinal = urgentrepairWorkorder.getAcceptProfessional();
			List<Map> strList = new  ArrayList<Map>();
			//获取工单所属区域
			String faultArea ="";
			String city = "";
			if(urgentrepairWorkorder.getFaultArea()!=null){
				faultArea = urgentrepairWorkorder.getFaultArea()+"";
				String[] areas = faultArea.split("-");
				city = areas[1];
			}
			if(urgentrepairWorkorder.getCustomerWoType()!=null){
				formJsonMap.put("customerWoType", urgentrepairWorkorder.getCustomerWoType());
				if(acceptProfessinal!=null&&!"".equals(acceptProfessinal)){
					if("home2g".equals(urgentrepairWorkorder.getCustomerWoType())){
						List<TreeNode> treeNodes = dataDictionaryService.getNextDictionaryByReferenceValueAndTreeIdService("2G", TreeConstant.SHENZHEN_ACCEPTPROFESSIONAL_FAULTGENERA_FAULTCAUSE);
						if(treeNodes!=null&&!treeNodes.isEmpty()){
							for(TreeNode treeNode : treeNodes){
								if(acceptProfessinal.equals(treeNode.getTreeNodeName())){
									long acceptProfessinalId = treeNode.getId();
									List<TreeNode> subTreeNodes = dataDictionaryService.getNextDictionaryByTreeNodeIdService(acceptProfessinalId);
									for(TreeNode subTreeNode : subTreeNodes){
										Map strMap =  new HashMap();
										strMap.put("id", subTreeNode.getId());
										strMap.put("value", subTreeNode.getReferenceValue());
										strList.add(strMap);
									}
								}
							}
						}
						
					}else if("homeTd".equals(urgentrepairWorkorder.getCustomerWoType())){
						List<TreeNode> treeNodes = dataDictionaryService.getNextDictionaryByReferenceValueAndTreeIdService("TD", TreeConstant.SHENZHEN_ACCEPTPROFESSIONAL_FAULTGENERA_FAULTCAUSE);
						if(treeNodes!=null&&!treeNodes.isEmpty()){
							for(TreeNode treeNode : treeNodes){
								if(acceptProfessinal.equals(treeNode.getTreeNodeName())){
									long acceptProfessinalId = treeNode.getId();
									List<TreeNode> subTreeNodes = dataDictionaryService.getNextDictionaryByTreeNodeIdService(acceptProfessinalId);
									for(TreeNode subTreeNode : subTreeNodes){
										Map strMap =  new HashMap();
										strMap.put("id", subTreeNode.getId());
										strMap.put("value", subTreeNode.getReferenceValue());
										strList.add(strMap);
									}
								}
							}
						}
					}
				}else{
					String treeId = BizResourceReader.getCityToTreeNameMappingInfo("faultGenera.mapping",city);
					if(treeId!=null&&!"".equals(treeId)){
						List<TreeNode> treeNodes = dataDictionaryService.getTheTopDictionaryByTreeIdService(Long.parseLong(treeId));
						if(treeNodes!=null&&!treeNodes.isEmpty()){
							for(TreeNode treeNode : treeNodes){
								Map strMap =  new HashMap();
								strMap.put("id", treeNode.getId());
								strMap.put("value", treeNode.getReferenceValue());
								strList.add(strMap);
							}
						}
					}
				}
			}else{
				String treeId = BizResourceReader.getCityToTreeNameMappingInfo("faultGenera.mapping",city);
				if(treeId!=null&&!"".equals(treeId)){
					List<TreeNode> treeNodes = dataDictionaryService.getTheTopDictionaryByTreeIdService(Long.parseLong(treeId));
					if(treeNodes!=null&&!treeNodes.isEmpty()){
						for(TreeNode treeNode : treeNodes){
							Map strMap =  new HashMap();
							strMap.put("id", treeNode.getId());
							strMap.put("value", treeNode.getReferenceValue());
							strList.add(strMap);
						}
					}
				}
			}
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String json = gson.toJson(strList);
			formJsonMap.put("selectFaultGenera", json);
			mch.addGroup("header", formJsonMap);
			mobilePackage.setContent(mch.mapToJson());
			
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 preFillUrgentRepairTechSupportLastReplyContentActionForMobile()");
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 preFillUrgentRepairTechSupportLastReplyContentActionForMobile()");
		}
	}
	
	/**
	 * 最终回复专家处理
	 */
	public void finalReplyUrgentRepairTechSupportTaskOrderActionForMobile(){
		log.info("进入 finalReplyUrgentRepairTechSupportTaskOrderActionForMobile()");
		log.info("finalReplyUrgentRepairTechSupportTaskOrderActionForMobile() 最终回复专家处理");
		try {
			// session获取账号id
			String actor = (String) SessionService.getInstance().getValueByKey("userId");

			// 更新专家处理任务单
			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);

			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder = new UrgentrepairTechsupporttaskorder();
			urgentrepairTechsupporttaskorder.setAffectedServiceName(formJsonMap.get("affectedServiceName"));
			int sideeffectService = -1;
			if("1".equals(formJsonMap.get("sideeffectService"))){
				sideeffectService = 1;
			}else{
				sideeffectService = 0;
			}
			if("1".equals(formJsonMap.get("faultHandleResult"))){
				urgentrepairTechsupporttaskorder.setIsAlarmClear(1);
				urgentrepairTechsupporttaskorder.setAlarmClearTime(TimeFormatHelper.setTimeFormat(formJsonMap.get("alarmClearTime")));
			}else{
				urgentrepairTechsupporttaskorder.setIsAlarmClear(0);
				urgentrepairTechsupporttaskorder.setAlarmClearTime(null);
			}
			urgentrepairTechsupporttaskorder.setSideeffectService(sideeffectService);
			urgentrepairTechsupporttaskorder.setReasonForDelayApply(formJsonMap.get("reasonForDelayApply"));
			urgentrepairTechsupporttaskorder.setFaultHandleDetail(formJsonMap.get("faultHandleDetail"));
			urgentrepairTechsupporttaskorder.setFaultHandleResult(formJsonMap.get("faultHandleResult"));
			urgentrepairTechsupporttaskorder.setForeseeSolveTime(TimeFormatHelper.setTimeFormat(formJsonMap.get("foreseeSolveTime")));
			urgentrepairTechsupporttaskorder.setFaultGenera(formJsonMap.get("faultGenera"));
			urgentrepairTechsupporttaskorder.setFaultReason(formJsonMap.get("faultReason"));
			
			//最终回复
			if(urgentRepairTechSupportTaskOrderService.txReplyUrgentRepairTechSupportTaskOrder(actor, woId, toId, urgentrepairTechsupporttaskorder)){
				// 最终回复完毕后，重新加载
				//组装mobilepackage
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
//				//通过TOID显示任务单信息
//				Map<String,String> commonTaskInfo = workManageService.getTaskOrderForShow(toId);
//				if(commonTaskInfo == null) {
//					MobilePackage errMobilePackage = new MobilePackage();
//					errMobilePackage.setResult("error");
//					// 返回content的JSON字符串信息
//					MobilePackageCommunicationHelper.responseMobilePacageAndResult(errMobilePackage);
//					return;
//				}else{
//					commonTaskInfo.put("TOID", commonTaskInfo.get("toId"));
//					mch.addGroup("job_menu_lev1_0", commonTaskInfo); // 任务单信息
//				}
//				mch.addGroup("header", formJsonMap);
//				newMobilePackage.setContent(mch.mapToJson());
				MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
				log.info("进入 finalReplyUrgentRepairTechSupportTaskOrderActionForMobile()");
			}else{
				log.error("finalReplyUrgentRepairTechSupportTaskOrderActionForMobile 调用 finalReplyUrgentRepairTechSupportTaskOrderActionForMobile 专家任务单恢复失败");
				// 返回content的JSON字符串信息
				MobilePackageCommunicationHelper.responseMobileError("finalReplyUrgentRepairTechSupportTaskOrderActionForMobile 调用 finalReplyUrgentRepairTechSupportTaskOrderActionForMobile 专家任务单恢复失败");
				log.info("进入 finalReplyUrgentRepairTechSupportTaskOrderActionForMobile()");
			}
			
			
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("进入 finalReplyUrgentRepairTechSupportTaskOrderActionForMobile()");
		}
	}
	
	

	/**
	 * 加载阶段回复页面
	 */
	public void loadTechSupportTaskOrderStepReplyActionForMobile() {
		log.info("进入 loadTechSupportTaskOrderStepReplyActionForMobile()");
		log.info("loadTechSupportTaskOrderStepReplyActionForMobile() 加载阶段回复页面");
		try {
			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map newFormJsonMap = new HashMap();
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+"]");
			UrgentrepairWorkorder urgentrepairWorkorder =  urgentRepairWorkOrderService.getUrgentRepairWorkOrder(woId);
			String acceptProfessinal = urgentrepairWorkorder.getAcceptProfessional();
			List<Map> strList = new  ArrayList<Map>();
			if(urgentrepairWorkorder.getCustomerWoType()!=null){
				newFormJsonMap.put("customerWoType", urgentrepairWorkorder.getCustomerWoType());
				if(acceptProfessinal!=null&&!"".equals(acceptProfessinal)){
					if("home2g".equals(urgentrepairWorkorder.getCustomerWoType())){
						List<TreeNode> treeNodes = dataDictionaryService.getNextDictionaryByReferenceValueAndTreeIdService("2G", TreeConstant.SHENZHEN_ACCEPTPROFESSIONAL_FAULTGENERA_FAULTCAUSE);
						if(treeNodes!=null&&!treeNodes.isEmpty()){
							for(TreeNode treeNode : treeNodes){
								if(acceptProfessinal.equals(treeNode.getTreeNodeName())){
									long acceptProfessinalId = treeNode.getId();
									List<TreeNode> subTreeNodes = dataDictionaryService.getNextDictionaryByTreeNodeIdService(acceptProfessinalId);
									for(TreeNode subTreeNode : subTreeNodes){
										Map strMap =  new HashMap();
										strMap.put("id", subTreeNode.getId());
										strMap.put("value", subTreeNode.getReferenceValue());
										strList.add(strMap);
									}
								}
							}
						}
						
					}else if("homeTd".equals(urgentrepairWorkorder.getCustomerWoType())){
						List<TreeNode> treeNodes = dataDictionaryService.getNextDictionaryByReferenceValueAndTreeIdService("TD", TreeConstant.SHENZHEN_ACCEPTPROFESSIONAL_FAULTGENERA_FAULTCAUSE);
						if(treeNodes!=null&&!treeNodes.isEmpty()){
							for(TreeNode treeNode : treeNodes){
								if(acceptProfessinal.equals(treeNode.getTreeNodeName())){
									long acceptProfessinalId = treeNode.getId();
									List<TreeNode> subTreeNodes = dataDictionaryService.getNextDictionaryByTreeNodeIdService(acceptProfessinalId);
									for(TreeNode subTreeNode : subTreeNodes){
										Map strMap =  new HashMap();
										strMap.put("id", subTreeNode.getId());
										strMap.put("value", subTreeNode.getReferenceValue());
										strList.add(strMap);
									}
								}
							}
						}
					}
				}else{
					List<TreeNode> treeNodes = dataDictionaryService.getTheTopDictionaryByTreeIdService(TreeConstant.ACCEPTPROFESSIONAL_FAULTGENERA_FAULTCAUSE);
					if(treeNodes!=null&&!treeNodes.isEmpty()){
						for(TreeNode treeNode : treeNodes){
							Map strMap =  new HashMap();
							strMap.put("id", treeNode.getId());
							strMap.put("value", treeNode.getReferenceValue());
							strList.add(strMap);
						}
					}
				}
			}else{
				List<TreeNode> treeNodes = dataDictionaryService.getTheTopDictionaryByTreeIdService(TreeConstant.ACCEPTPROFESSIONAL_FAULTGENERA_FAULTCAUSE);
				if(treeNodes!=null&&!treeNodes.isEmpty()){
					for(TreeNode treeNode : treeNodes){
						Map strMap =  new HashMap();
						strMap.put("id", treeNode.getId());
						strMap.put("value", treeNode.getReferenceValue());
						strList.add(strMap);
					}
				}
			}
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String json = gson.toJson(strList);
			newFormJsonMap.put("selectFaultGenera", json);
			// 将WOID、toId、bizunitInstanceId带到阶段回复页面
			mch.addGroup("responseForm", formJsonMap);
			mch.addGroup("header", newFormJsonMap);
			mobilePackage.setContent(mch.mapToJson());
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 loadTechSupportTaskOrderStepReplyActionForMobile()");
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadTechSupportTaskOrderStepReplyActionForMobile()");
		} 
	}
	
	/**
	 * 阶段回复
	 */
	public void stepReplyUrgentRepairTechSupportTaskOrderActionForMobile() {
		log.info("进入 stepReplyUrgentRepairTechSupportTaskOrderActionForMobile()");
		log.info("stepReplyUrgentRepairTechSupportTaskOrderActionForMobile()阶段回复");
		try {
			String actor = (String) SessionService.getInstance().getValueByKey("userId");

			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);

			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			String handleResultDescription = formJsonMap.get("handleResultDescription");
			String processingProgress = formJsonMap.get("processingProgress");
			String faultGenera = formJsonMap.get("faultGenera");
			String faultReason = formJsonMap.get("faultReason");
			String customerWoType = formJsonMap.get("customerWoType");
			
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = (String) request.getSession().getAttribute("userId");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			// 添加服务跟踪记录表
			Tasktracerecord tasktracerecord = new Tasktracerecord ();
			//ou.jh
			SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(actor);
//			Staff staff = providerOrganizationService.getStaffByAccount(actor);
			if(sysOrgUserByAccount!=null){
				if(sysOrgUserByAccount.getName()!=null){
					tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
				}
			}
			String flag = urgentRepairTechSupportTaskOrderService.txStepReplyUrgentRepairTechSupportTaskOrder(actor, woId, toId, handleResultDescription, processingProgress, faultGenera, faultReason,customerWoType);
			if("success".equals(flag)){
				//阶段回复完技术支持单后重新加载任务单
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
				MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
				log.info("退出 stepReplyUrgentRepairTechSupportTaskOrderActionForMobile()");
			}else if("fail".equals(flag)){
				// 返回content的JSON字符串信息
				MobilePackageCommunicationHelper.responseMobileError("现场任务阶段回复失败！");
				log.info("退出 stepReplyUrgentRepairTechSupportTaskOrderActionForMobile()");
				return;
			}else if("VPNFail".equals(flag)){
				//阶段回复完技术支持单后重新加载任务单
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
				String newContent = newMobilePackage.getContent();
				MobileContentHelper newMch = new MobileContentHelper();
				newMch.setContent(newContent);
				Map<String,String> newMap = newMch.getGroupByKey("job_menu_lev1_0");
				newMap.put("VPNFAIL", "VPNFAIL");
				newMch.addGroup("job_menu_lev1_0", newMap);
				newMobilePackage.setContent(newMch.mapToJson());
				MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
				log.info("退出 stepReplyUrgentRepairTechSupportTaskOrderActionForMobile()");
			}
			
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 stepReplyUrgentRepairTechSupportTaskOrderActionForMobile()");
		}
	}
	
	/**
	 * 加载专家转派任务页面
	 */
	public void loadToSendUrgentRepairTechSupportTaskActionForMobile(){
		log.info("进入 loadToSendUrgentRepairTechSupportTaskActionForMobile()");
		log.info("loadToSendUrgentRepairTechSupportTaskActionForMobile() 加载专家转派任务页面");
		try{
			// session获取账号id
			String account = (String) SessionService.getInstance().getValueByKey(
			"userId");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();

			// 更新现场处理任务单
			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			Map newFormJsonMap =  new HashMap();
			
			//List<ProviderOrganization> providerOrganizations = providerOrganizationService.getOrgFromOrgTreeByAccountAndOrgType(account, OrganizationConstant.ORGANIZATION_PROJECTTEAM);
			List<SysOrg> providerOrganizations =this.sysOrganizationService.getOrgByAccountAndOrgType(account, OrganizationConstant.ORGANIZATION_PROJECTTEAM);
			if(providerOrganizations!=null&&!providerOrganizations.isEmpty()){
				Set<Map> teamersLists = new HashSet<Map>();
				for(SysOrg org :providerOrganizations){
					String orgId = org.getOrgId()+"";
					//获取专家列表
					Set<Map> tempList = urgentRepairTechSupportTaskOrderService.loadToSendSpecialistInfoService(orgId,"");
					teamersLists.addAll(tempList);
				}
				
				String json = gson.toJson(teamersLists);
				newFormJsonMap.put("hiddenTeamerList", json);
				newFormJsonMap.put("WOID", woId);
				newFormJsonMap.put("TOID", toId);
				mch.addGroup("toSendTaskForm", newFormJsonMap);
			}
			mobilePackage.setContent(mch.mapToJson());
			
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 loadToSendUrgentRepairTechSupportTaskActionForMobile()");
		}catch (RuntimeException e){
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadToSendUrgentRepairTechSupportTaskActionForMobile()");
		}
	}
	
	/**
	 * 转派专家任务单
	 * @return
	 */
	public void toSendUrgentRepairTechSupportTaskOrderActionForMobile() {
		log.info("进入 toSendUrgentRepairTechSupportTaskOrderActionForMobile()");
		log.info("toSendUrgentRepairTechSupportTaskOrderActionForMobile() 转派专家任务单");
		try {
			// session获取账号id
			String sendor = (String) SessionService.getInstance().getValueByKey(
			"userId");

			// 更新现场处理任务单
			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();

			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);

			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			String selectTeamer = formJsonMap.get("selectTeamer");
			String comment = formJsonMap.get("comment");
			urgentRepairTechSupportTaskOrderService.txToSendUrgentRepairTechSupportTaskOrder(sendor, woId, toId, selectTeamer, comment);
			//组装mobilepackage
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
			//转派完毕后，重新加载
			MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
			log.info("退出 toSendUrgentRepairTechSupportTaskOrderActionForMobile()");
			//升级后跳转页面
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 toSendUrgentRepairTechSupportTaskOrderActionForMobile()");
		}
	}

	public UrgentRepairWorkOrderService getUrgentRepairWorkOrderService() {
		return urgentRepairWorkOrderService;
	}

	public void setUrgentRepairWorkOrderService(
			UrgentRepairWorkOrderService urgentRepairWorkOrderService) {
		this.urgentRepairWorkOrderService = urgentRepairWorkOrderService;
	}

	public WorkManageService getWorkManageService() {
		return workManageService;
	}

	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}


	public TaskTracingRecordService getTaskTracingRecordService() {
		return taskTracingRecordService;
	}

	public void setTaskTracingRecordService(
			TaskTracingRecordService taskTracingRecordService) {
		this.taskTracingRecordService = taskTracingRecordService;
	}

	public UrgentRepairTechSupportTaskOrderService getUrgentRepairTechSupportTaskOrderService() {
		return urgentRepairTechSupportTaskOrderService;
	}

	public void setUrgentRepairTechSupportTaskOrderService(
			UrgentRepairTechSupportTaskOrderService urgentRepairTechSupportTaskOrderService) {
		this.urgentRepairTechSupportTaskOrderService = urgentRepairTechSupportTaskOrderService;
	}

	public WorkOrderAssnetResourceDao getWorkOrderAssnetResourceDao() {
		return workOrderAssnetResourceDao;
	}

	public void setWorkOrderAssnetResourceDao(
			WorkOrderAssnetResourceDao workOrderAssnetResourceDao) {
		this.workOrderAssnetResourceDao = workOrderAssnetResourceDao;
	}

	public NetworkResourceInterfaceService getNetworkResourceService() {
		return networkResourceService;
	}

	public void setNetworkResourceService(
			NetworkResourceInterfaceService networkResourceService) {
		this.networkResourceService = networkResourceService;
	}

	public BizMessageService getBizMessageService() {
		return bizMessageService;
	}

	public void setBizMessageService(BizMessageService bizMessageService) {
		this.bizMessageService = bizMessageService;
	}

	public DataDictionaryService getDataDictionaryService() {
		return dataDictionaryService;
	}

	public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
		this.dataDictionaryService = dataDictionaryService;
	}

	public WorkOrderCommonService getWorkOrderCommonService() {
		return workOrderCommonService;
	}

	public void setWorkOrderCommonService(
			WorkOrderCommonService workOrderCommonService) {
		this.workOrderCommonService = workOrderCommonService;
	}

	public UrgentRepairCustomerWorkOrderService getUrgentRepairCustomerWorkOrderService() {
		return urgentRepairCustomerWorkOrderService;
	}

	public void setUrgentRepairCustomerWorkOrderService(
			UrgentRepairCustomerWorkOrderService urgentRepairCustomerWorkOrderService) {
		this.urgentRepairCustomerWorkOrderService = urgentRepairCustomerWorkOrderService;
	}

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}
	
	
}
