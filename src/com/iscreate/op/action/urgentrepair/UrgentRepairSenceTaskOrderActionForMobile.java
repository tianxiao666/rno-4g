package com.iscreate.op.action.urgentrepair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.constant.BizAuthorityConstant;
import com.iscreate.op.constant.OrganizationConstant;
import com.iscreate.op.constant.TreeConstant;
import com.iscreate.op.dao.publicinterface.WorkOrderAssnetResourceDao;
import com.iscreate.op.pojo.bizmsg.BizMessage;
import com.iscreate.op.pojo.publicinterface.Tasktracerecord;
import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairCustomerworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairSencetaskorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairTechsupporttaskorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.publicinterface.TaskTracingRecordService;
import com.iscreate.op.service.publicinterface.WorkOrderCommonService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.urgentrepair.BizResourceReader;
import com.iscreate.op.service.urgentrepair.UrgentRepairCustomerWorkOrderService;
import com.iscreate.op.service.urgentrepair.UrgentRepairSenceTaskOrderService;
import com.iscreate.op.service.urgentrepair.UrgentRepairTechSupportTaskOrderService;
import com.iscreate.op.service.urgentrepair.UrgentRepairWorkOrderService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.datadictionary.DataDictionaryService;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageCommunicationHelper;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.tree.TreeNode;

public class UrgentRepairSenceTaskOrderActionForMobile {
	private String toId;
	private String woId;
	private Map<String,String> workOrderInfo;
	private Map<String,String> taskOrderInfo;
	//注入service
	private UrgentRepairWorkOrderService urgentRepairWorkOrderService;
	private UrgentRepairSenceTaskOrderService urgentRepairSenceTaskOrderService;
	private WorkManageService workManageService;
	private WorkOrderCommonService workOrderCommonService;
	private TaskTracingRecordService taskTracingRecordService;
	private UrgentRepairTechSupportTaskOrderService urgentRepairTechSupportTaskOrderService;
	private NetworkResourceInterfaceService networkResourceService;
	private WorkOrderAssnetResourceDao workOrderAssnetResourceDao;
	private BizMessageService bizMessageService;
	private DataDictionaryService dataDictionaryService;
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

	
	
	private  static final  Log log = LogFactory.getLog(UrgentRepairSenceTaskOrderActionForMobile.class);


	/**
	 * 显示现场处理单的页面内容
	 * @return
	 */
	public void loadUrgentRepairSceneTaskOrderPageActionForMobile(){
		log.info("进入 loadUrgentRepairSceneTaskOrderPageActionForMobile()");
		log.info("loadUrgentRepairSceneTaskOrderPageActionForMobile() 显示现场处理单的页面内容");
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			mobilePackage = getTaskOrderMobilePackageByToId(toId,mobilePackage,mch,formJsonMap);
			
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 loadUrgentRepairSceneTaskOrderPageActionForMobile()");
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadUrgentRepairSceneTaskOrderPageActionForMobile()");
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
		UrgentrepairSencetaskorder  urgentrepairSencetaskorder = urgentRepairSenceTaskOrderService.getUrgentRepairSenceTaskOrder(toId);
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
			
			if(urgentrepairSencetaskorder==null){
				return null;
			}
			
			
			String affectedServiceName ="";
			if(urgentrepairSencetaskorder.getAffectedServiceName()!=null){
				affectedServiceName = urgentrepairSencetaskorder.getAffectedServiceName();
			}
			commonTaskInfo.put("affectedServiceName", affectedServiceName);
			String faultGenera ="";
			if(urgentrepairSencetaskorder.getFaultGenera()!=null){
				faultGenera = urgentrepairSencetaskorder.getFaultGenera();
			}
			commonTaskInfo.put("faultGenera", faultGenera);
			String faultReason ="";
			if(urgentrepairSencetaskorder.getFaultReason()!=null){
				faultReason = urgentrepairSencetaskorder.getFaultReason();
			}
			commonTaskInfo.put("faultReason", faultReason);
			String faultHandleDetail ="";
			if(urgentrepairSencetaskorder.getFaultHandleDetail()!=null){
				faultHandleDetail = urgentrepairSencetaskorder.getFaultHandleDetail();
			}
			commonTaskInfo.put("faultHandleDetail", faultHandleDetail);
			String faultHandleResult ="";
			if("1".equals(urgentrepairSencetaskorder.getFaultHandleResult())){
				faultHandleResult = "解决";
			}else{
				faultHandleResult = "延迟解决";
			}
			commonTaskInfo.put("faultHandleResult",faultHandleResult);
			String reasonForDelayApply ="";
			if(urgentrepairSencetaskorder.getReasonForDelayApply()!=null){
				reasonForDelayApply = urgentrepairSencetaskorder.getReasonForDelayApply();
			}
			commonTaskInfo.put("reasonForDelayApply", reasonForDelayApply);
			//告警清除时间
			commonTaskInfo.put("alarmClearTime", TimeFormatHelper.getTimeFormatByMin(urgentrepairSencetaskorder.getAlarmClearTime()));
			//故障解决时间
			commonTaskInfo.put("faultSolveTime", TimeFormatHelper.getTimeFormatByMin(urgentrepairSencetaskorder.getFaultSolveTime()));
			//延迟解决时间
			commonTaskInfo.put("foreseeSolveTime", TimeFormatHelper.getTimeFormatByMin(urgentrepairSencetaskorder.getForeseeSolveTime()));
			String sideeffectService ="";
			if(urgentrepairSencetaskorder.getSideeffectService()!=null){
				sideeffectService = urgentrepairSencetaskorder.getSideeffectService()+"";
			}
			commonTaskInfo.put("sideeffectService", sideeffectService);
			
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
			commonTaskInfo.put("TOID", commonTaskInfo.get("toId"));
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
	 * 加载现场处理工单
	 */
	public void loadUrgentRepairSceneWorkOrderPageActionForMobile() {
		log.info("进入 loadUrgentRepairSceneWorkOrderPageActionForMobile()");
		log.info("loadUrgentRepairSceneTaskOrderPageActionForMobile() 加载现场处理工单");
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			// 获取工单信息
			workOrderInfo = new HashMap<String,String>();
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
					if ("一级告警".equals(urgentrepairWorkorder.getFaultLevel()
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
			
			if (this.workOrderInfo != null) {
				workOrderInfo.put("WOID", workOrderInfo.get("woId"));
				mch.addGroup("job_menu_lev1_1", this.workOrderInfo); // 工单信息
			}
			
			//其它类型工单
			mobilePackage.setContent(mch.mapToJson());

			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 loadUrgentRepairSceneWorkOrderPageActionForMobile()");
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadUrgentRepairSceneWorkOrderPageActionForMobile()");
		} 
	}
	
	/**
	 * 加载现场处理相关资源
	 */
	public void loadUrgentRepairSceneAssResPageActionForMobile() {
		log.info("进入 loadUrgentRepairSceneAssResPageActionForMobile()");
		log.info("loadUrgentRepairSceneAssResPageActionForMobile() 加载现场处理相关资源");
		try {
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			List<Workorderassnetresource> workorderassnetresources =  workOrderAssnetResourceDao.getWorkOrderAssnetResourceRecordDao("woId", woId);
			if(workorderassnetresources!=null&&!workorderassnetresources.isEmpty()){
				//基站信息
				Map<String,String> associatedAlarmResourceMap = new HashMap<String,String>();
				List<Map<String,String>> associatedAlarmResourceList = new ArrayList<Map<String,String>>();
				List<Map<String,String>> tempList = new ArrayList<Map<String,String>>();
				//业务设计，只拿一个基站，所以只需把第一个基站的ID 和类型保存即可
				String networkResourceType = "";
				long networkResourceId = 0;
				
				if(workorderassnetresources.get(0).getNetworkResourceType()!=null){
					networkResourceType = workorderassnetresources.get(0).getNetworkResourceType();
				}
				
				
				if(workorderassnetresources.get(0).getNetworkResourceId()!=null){
					networkResourceId = workorderassnetresources.get(0).getNetworkResourceId();
				}
				
				for(Workorderassnetresource workorderassnetresource : workorderassnetresources){
					Map<String,Map<String,String>> baseFacilityMap = networkResourceService.getBaseFacilityToMapService(workorderassnetresource.getNetworkResourceId()+"", workorderassnetresource.getNetworkResourceType());
					if(baseFacilityMap!=null){
						Map<String,String > dictionaries = baseFacilityMap.get("dictionary");
						Map<String,String > entities = baseFacilityMap.get("entity");
						for(String key : dictionaries.keySet()){
							Map<String,String> aarMap = new HashMap<String,String>();
							aarMap.put("cnName",dictionaries.get(key));
							if(entities.get(key)==null||"null".equals(entities.get(key))){
								aarMap.put("value", "");
							}else{
								aarMap.put("value", entities.get(key));
							}
							tempList.add(aarMap);
						}
						Map<String,String> map = new HashMap<String,String>();
						map.put("networkResourceList", gson.toJson(tempList));
						if(workorderassnetresource.getNetworkResourceType()!=null){
							map.put("networkResourceType",networkResourceType);
							map.put("networkResourceId", networkResourceId+"");
							map.put("stationId", workorderassnetresource.getStationId()+"");
						}
						associatedAlarmResourceList.add(map);
					}
				}
				associatedAlarmResourceMap.put("associatedAlarmResource", gson.toJson(associatedAlarmResourceList));
				mch.addGroup("associatedAlarmResourceArea",associatedAlarmResourceMap); // 关联的网络资源
				
				//维护记录
				List<Map<String,String>> maintainRecords = networkResourceService.getHandleResourceMaintainRecordService(woId, networkResourceId+"", networkResourceType);
				if(maintainRecords!=null&&!maintainRecords.isEmpty()){
					Map<String,String> maintainRecordMap =  new HashMap<String,String>();
					maintainRecordMap.put("maintainRecordDiv", gson.toJson(maintainRecords));
					mch.addGroup("maintainRecordArea",maintainRecordMap);
				}
				
			}
			mobilePackage.setContent(mch.mapToJson());

			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 loadUrgentRepairSceneAssResPageActionForMobile()");
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadUrgentRepairSceneAssResPageActionForMobile()");
		} 
	}
	
	/**
	 * 加载现场处理服务跟踪记录
	 */
	public void loadUrgentRepairSceneTraceRecordPageActionForMobile() {
		log.info("进入 loadUrgentRepairSceneTraceRecordPageActionForMobile()");
		log.info("loadUrgentRepairSceneTraceRecordPageActionForMobile() 加载现场处理服务跟踪记录");
		try {
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
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
			log.info("退出 loadUrgentRepairSceneTraceRecordPageActionForMobile()");
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadUrgentRepairSceneTraceRecordPageActionForMobile()");
		} 
	}
	
	/**
	 * 受理现场处理单
	 */
	public void acceptUrgentRepairSceneTaskOrderActionForMobile(){
		log.info("进入 acceptUrgentRepairSceneTaskOrderActionForMobile()");
		log.info("acceptUrgentRepairSceneTaskOrderActionForMobile()受理现场处理单");
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			
			// 更新任务单
			WorkmanageTaskorder workmanageTaskorder = workManageService.getTaskOrderEntity(toId);
			workmanageTaskorder.setAcceptComment("已受理");
			workmanageTaskorder.setAcceptPeople(accept);
			workmanageTaskorder.setAcceptTime(new Date());
			//更改受理状态为受理中，状态号为6
			boolean isOk = urgentRepairSenceTaskOrderService.txAcceptUrgentRepairSenceTaskOrder(accept,woId,toId, workmanageTaskorder);
			if (!isOk) {
				// 返回content的JSON字符串信息
				MobilePackageCommunicationHelper.responseMobileError("现场任务受理失败！");
				log.info("退出 acceptUrgentRepairSceneTaskOrderActionForMobile()");
				return;
			}else{
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
				//受理完毕后，重新加载
				MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
				log.info("退出 acceptUrgentRepairSceneTaskOrderActionForMobile()");
			}		
			
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 acceptUrgentRepairSceneTaskOrderActionForMobile()");
		} 
	}
	
	/**
	 * 加载驳回页面
	 */
	public void loadRejectSceneTaskOrderActionForMobile() {
		log.info("进入 loadRejectSceneTaskOrderActionForMobile()");
		log.info("loadRejectSceneTaskOrderActionForMobile() 加载驳回页面");
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
			log.info("退出 loadRejectSceneTaskOrderActionForMobile()");
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadRejectSceneTaskOrderActionForMobile()");
		} 
	}
	
	/**
	 * 驳回现场处理单
	 */
	public void rejectUrgentRepairSceneTaskOrderActionForMobile(){
		log.info("进入 rejectUrgentRepairSceneTaskOrderActionForMobile()");
		log.info("rejectUrgentRepairSceneTaskOrderActionForMobile() 驳回现场处理单");
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			
			// 更新任务单
			WorkmanageTaskorder workmanageTaskorder = workManageService.getTaskOrderEntity(toId);
			workmanageTaskorder.setRejectComment(formJsonMap.get("rejectComment"));
			workmanageTaskorder.setRejectTime(new Date());
			//更改受理状态为受理中，状态号为6
			boolean isOk = urgentRepairSenceTaskOrderService.txRejectUrgentRepairSenceTaskOrder(accept,woId,toId, workmanageTaskorder);
			if (!isOk) {
				// 返回content的JSON字符串信息
				MobilePackageCommunicationHelper.responseMobileError("现场任务驳回失败！");
				log.info("退出 rejectUrgentRepairSceneTaskOrderActionForMobile()");
				return;
			}else{
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
				//受理完毕后，重新加载
				MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
				log.info("退出 rejectUrgentRepairSceneTaskOrderActionForMobile()");
			}		
			
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 rejectUrgentRepairSceneTaskOrderActionForMobile()");
		} 
	}	
	
	/**
	 * 跳转到派发技术支援单页面(预填任务信息)
	 * @return
	 */
	public void loadUrgentRepairTechSupportTaskActionForMobile() {
		log.info("进入 loadUrgentRepairTechSupportTaskActionForMobile()");
		log.info("loadUrgentRepairTechSupportTaskActionForMobile() 跳转到派发技术支援单页面(预填任务信息)");
		try {
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			// 预填派发任务的任务主题和任务描述
			//获得工单
			WorkmanageWorkorder workmanageWorkorder = workManageService.getWorkOrderEntity(woId);
			//重新设置任务名称
			String taskName = "专家任务单：" + workmanageWorkorder.getWoTitle();
			//获得任务单
			taskOrderInfo = workManageService.getTaskOrderForShow(toId);
			if(taskOrderInfo == null) {
				// 返回content的JSON字符串信息
				log.error("loadUrgentRepairTechSupportTaskActionForMobile 调用 workManageService.getTaskOrderForShow 获取不到任务单信息");
				MobilePackageCommunicationHelper.responseMobileError("loadUrgentRepairTechSupportTaskActionForMobile 调用 workManageService.getTaskOrderForShow 获取不到任务单信息");
				log.info("退出 loadUrgentRepairTechSupportTaskActionForMobile()");
				return;
			}
			taskOrderInfo.put("taskName", taskName);
			taskOrderInfo.put("TOID", toId);
			taskOrderInfo.put("WOID", woId);
			//转换任务截止时间
			if(taskOrderInfo.get("requireCompleteTime")!=null&&!"".equals(taskOrderInfo.get("requireCompleteTime"))){
				taskOrderInfo.put("requireCompleteTime",TimeFormatHelper.getTimeFormatBySecond(TimeFormatHelper.setTimeFormat(taskOrderInfo.get("requireCompleteTime"))));
			}
			
			mch.addGroup("createTaskForm", this.taskOrderInfo);
			
			// 显示派选对象列表
			List<Map> specialistLists = urgentRepairSenceTaskOrderService.loadSpecialistInfoService(account);
			if(specialistLists==null||specialistLists.isEmpty()){
				// 返回content的JSON字符串信息
				log.error("loadUrgentRepairTechSupportTaskActionForMobile 调用 urgentRepairSenceTaskOrderService.loadSpecialistInfoService 获取不到专家列表");
				MobilePackageCommunicationHelper.responseMobileError("loadUrgentRepairTechSupportTaskActionForMobile 调用 urgentRepairSenceTaskOrderService.loadSpecialistInfoService 获取不到专家列表！");
				return;
			}
			Map accountMap = new HashMap();
			accountMap.put("accountDiv", gson.toJson(specialistLists));
			mch.addGroup("accountDivArea", accountMap);

			mobilePackage.setContent(mch.mapToJson());

			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 loadUrgentRepairTechSupportTaskActionForMobile()");
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadUrgentRepairTechSupportTaskActionForMobile()");
		} 
	}
	

	/**
	 * 派发支援任务单
	 * @return
	 */
	public void createUrgentRepairTechSupportTaskOrderActionForMobile() {
		log.info("进入 createUrgentRepairTechSupportTaskOrderActionForMobile()");
		log.info("createUrgentRepairTechSupportTaskOrderActionForMobile() 派发支援任务单");
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			String selectTeamer = formJsonMap.get("selectTeamer");
			UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder = new UrgentrepairTechsupporttaskorder();
			WorkmanageTaskorder workmanageTaskorder = new WorkmanageTaskorder();
			workmanageTaskorder.setToTitle(formJsonMap.get("taskName"));
			workmanageTaskorder.setAssignTime(new Date());
			workmanageTaskorder.setAssignComment(formJsonMap.get("assignComment"));
			workmanageTaskorder.setRequireCompleteTime(TimeFormatHelper.setTimeFormat(formJsonMap.get("requireCompleteTime")));
			urgentRepairSenceTaskOrderService.txCreateUrgentRepairTechSupportTaskOrder(sendor, selectTeamer, woId,toId, urgentrepairTechsupporttaskorder,workmanageTaskorder);
			//组装mobilepackage
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
//			//通过TOID显示任务单信息
//			Map<String,String> commonTaskInfo = workManageService.getTaskOrderForShow(toId);
//			if(commonTaskInfo == null) {
//				MobilePackage errMobilePackage = new MobilePackage();
//				errMobilePackage.setResult("error");
//				// 返回content的JSON字符串信息
//				MobilePackageCommunicationHelper.responseMobilePacageAndResult(errMobilePackage);
//				return;
//			}else{
//				commonTaskInfo.put("TOID", commonTaskInfo.get("toId"));
//				commonTaskInfo.put("WOID", commonTaskInfo.get("woId"));
//				mch.addGroup("job_menu_lev1_0", commonTaskInfo); // 任务单信息
//			}
//			mch.addGroup("header", formJsonMap);
//			newMobilePackage.setContent(mch.mapToJson());
			//受理完毕后，重新加载
			MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
			log.info("退出 createUrgentRepairTechSupportTaskOrderActionForMobile()");
			//升级后跳转页面
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 createUrgentRepairTechSupportTaskOrderActionForMobile()");
		}
	}
	
	
	/**
	 * 跳转到转派现场任务页面(预填任务信息)
	 * @return
	 */
	public void loadUrgentRepairToSendSceneTaskActionForMobile() {
		log.info("进入 loadUrgentRepairToSendSceneTaskActionForMobile()");
		log.info("loadUrgentRepairToSendSceneTaskActionForMobile() 跳转到转派现场任务页面(预填任务信息)");
		try {
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			//获得任务单
			taskOrderInfo = workManageService.getTaskOrderForShow(toId);
			if(taskOrderInfo == null) {
				// 返回content的JSON字符串信息
				MobilePackageCommunicationHelper.responseMobileError("转派现场任务失败");
				return;
			}
			taskOrderInfo.put("TOID", toId);
			taskOrderInfo.put("WOID", woId);
			mch.addGroup("createTaskForm", this.taskOrderInfo);
			
			// 显示派选对象列表
			List<Map> specialistLists = urgentRepairSenceTaskOrderService.loadSpecialistInfoService(account);
			if(specialistLists==null||specialistLists.isEmpty()){
				// 返回content的JSON字符串信息
				MobilePackageCommunicationHelper.responseMobileError("获取不到专家列表！");
			}
			Map accountMap = new HashMap();
			accountMap.put("accountDiv", gson.toJson(specialistLists));
			mch.addGroup("accountDivArea", accountMap);

			mobilePackage.setContent(mch.mapToJson());

			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 loadUrgentRepairToSendSceneTaskActionForMobile()");
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadUrgentRepairToSendSceneTaskActionForMobile()");
		} 
	}
	
	
	/**
	 * 预填现场处理子任务的最终回复信息
	 * @return
	 */
	public void preFillUrgentRepairSceneLastReplyContentActionForMobile() {
		log.info("进入 preFillUrgentRepairSceneLastReplyContentActionForMobile()");
		log.info("preFillUrgentRepairSceneLastReplyContentActionForMobile() 预填现场处理子任务的最终回复信息)");
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
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
										Map<String,String> strMap =  new HashMap<String,String>();
										strMap.put("id", subTreeNode.getId()+"");
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
										Map<String,String> strMap =  new HashMap<String,String>();
										strMap.put("id", subTreeNode.getId()+"");
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
								Map<String,String> strMap =  new HashMap<String,String>();
								strMap.put("id", treeNode.getId()+"");
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
							Map<String,String> strMap =  new HashMap<String,String>();
							strMap.put("id", treeNode.getId()+"");
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
			log.info("退出 preFillUrgentRepairSceneLastReplyContentActionForMobile()");
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 preFillUrgentRepairSceneLastReplyContentActionForMobile()");
		} 
	}
	
	/**
	 * 最终回复现场处理
	 */
	public void finalReplyUrgentRepairSceneTaskOrderActionForMobile(){
		log.info("进入 finalReplyUrgentRepairSceneTaskOrderActionForMobile()");
		log.info("finalReplyUrgentRepairSceneTaskOrderActionForMobile() 最终回复现场处理");
		try {
			
			// session获取账号id
			String actor = (String) SessionService.getInstance().getValueByKey("userId");

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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			UrgentrepairSencetaskorder urgentrepairSencetaskorder = new UrgentrepairSencetaskorder();
			urgentrepairSencetaskorder.setAffectedServiceName(formJsonMap.get("affectedServiceName"));
			int sideeffectService = -1;
			if("1".equals(formJsonMap.get("sideeffectService"))){
				sideeffectService = 1;
			}else{
				sideeffectService = 0;
			}
			if("1".equals(formJsonMap.get("faultHandleResult"))){
				urgentrepairSencetaskorder.setIsAlarmClear(1);
				urgentrepairSencetaskorder.setAlarmClearTime(TimeFormatHelper.setTimeFormat(formJsonMap.get("alarmClearTime")));
			}else{
				urgentrepairSencetaskorder.setIsAlarmClear(0);
				urgentrepairSencetaskorder.setAlarmClearTime(null);
			}
			urgentrepairSencetaskorder.setSideeffectService(sideeffectService);
			urgentrepairSencetaskorder.setReasonForDelayApply(formJsonMap.get("reasonForDelayApply"));
			urgentrepairSencetaskorder.setFaultHandleDetail(formJsonMap.get("faultHandleDetail"));
			urgentrepairSencetaskorder.setFaultHandleResult(formJsonMap.get("faultHandleResult"));
			urgentrepairSencetaskorder.setForeseeSolveTime(TimeFormatHelper.setTimeFormat(formJsonMap.get("foreseeSolveTime")));
			urgentrepairSencetaskorder.setFaultGenera(formJsonMap.get("faultGenera"));
			urgentrepairSencetaskorder.setFaultReason(formJsonMap.get("faultReason"));
			//最终回复
			if(urgentRepairSenceTaskOrderService.txFinalReplyUrgentRepairSenceTaskOrder(actor, woId, toId, urgentrepairSencetaskorder)){
				MobilePackage newMobilePackage = new MobilePackage();
				mobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
				//最终回复，重新加载
				MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
				log.info("退出 finalReplyUrgentRepairSceneTaskOrderActionForMobile()");
			}else{
				// 返回content的JSON字符串信息
				MobilePackageCommunicationHelper.responseMobileError("现场任务回复失败！");
				log.info("退出 finalReplyUrgentRepairSceneTaskOrderActionForMobile()");
				return;
			}
			
			
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 finalReplyUrgentRepairSceneTaskOrderActionForMobile()");
		} 
	}
	
	/**
	 * 加载阶段回复页面
	 */
	public void loadSceneTaskOrderStepReplyActionForMobile() {
		log.info("进入 loadSceneTaskOrderStepReplyActionForMobile()");
		log.info("loadSceneTaskOrderStepReplyActionForMobile() 加载阶段回复页面");
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
										Map<String,String> strMap =  new HashMap<String,String>();
										strMap.put("id", subTreeNode.getId()+"");
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
										Map<String,String> strMap =  new HashMap<String,String>();
										strMap.put("id", subTreeNode.getId()+"");
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
							Map<String,String> strMap =  new HashMap<String,String>();
							strMap.put("id", treeNode.getId()+"");
							strMap.put("value", treeNode.getReferenceValue());
							strList.add(strMap);
						}
					}
				}
			}else{
				List<TreeNode> treeNodes = dataDictionaryService.getTheTopDictionaryByTreeIdService(TreeConstant.ACCEPTPROFESSIONAL_FAULTGENERA_FAULTCAUSE);
				if(treeNodes!=null&&!treeNodes.isEmpty()){
					for(TreeNode treeNode : treeNodes){
						Map<String,String> strMap =  new HashMap<String,String>();
						strMap.put("id", treeNode.getId()+"");
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
			MobilePackage newMobilePackage = new MobilePackage();
			mch.addGroup("responseForm", formJsonMap);
			mch.addGroup("header", newFormJsonMap);
			newMobilePackage.setContent(mch.mapToJson());
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
			log.info("退出 loadSceneTaskOrderStepReplyActionForMobile()");
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadSceneTaskOrderStepReplyActionForMobile()");
		} 
	}
	
	/**
	 * 阶段回复
	 */
	public void stepReplyUrgentRepairSceneTaskOrderActionForMobile() {
		log.info("进入 stepReplyUrgentRepairSceneTaskOrderActionForMobile()");
		log.info("stepReplyUrgentRepairSceneTaskOrderActionForMobile() 阶段回复");
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
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
			String flag = urgentRepairSenceTaskOrderService.txStepReplyUrgentRepairSenceTaskOrder(actor, woId, toId, handleResultDescription, processingProgress, faultGenera, faultReason,customerWoType);
			if("success".equals(flag)){
				//阶段回复完技术支持单后重新加载任务单
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
				MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
			}else if("fail".equals(flag)){
				// 返回content的JSON字符串信息
				log.error("in stepReplyUrgentRepairSceneTaskOrderActionForMobile 调用 urgentRepairSenceTaskOrderService.txStepReplyUrgentRepairSenceTaskOrder 现场任务阶段回复失败");
				MobilePackageCommunicationHelper.responseMobileError("现场任务阶段回复失败！");
				log.info("退出 stepReplyUrgentRepairSceneTaskOrderActionForMobile()");
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
				log.info("退出 stepReplyUrgentRepairSceneTaskOrderActionForMobile()");
			}
			
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 stepReplyUrgentRepairSceneTaskOrderActionForMobile()");
		} 
	}
	
	/**
	 * 获取一个现场处理的子任务
	 * 
	 * @description TODO
	 * @author sunny
	 * @date 2011-11-3 下午05:34:12
	 */
	public void getSubTaskOfSceneActionForMobile() {
		log.info("进入 getSubTaskOfSceneActionForMobile()");
		log.info("getSubTaskOfSceneActionForMobile()  获取一个现场处理的子任务");
		try {
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			// 获取子任务&ownTable=&propertyName=
			List<Map> subTasks = workManageService.getChildTaskOrderListByToId(toId);
			if(subTasks!=null && subTasks.size()>0){//yuan.yw add 非空判断
				for(Map map : subTasks){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
					/*时间处理*/
					//派发时间
					if(map.get("assignTime") != null){
						String assignedTime;
						assignedTime = sdf2.format(sdf.parse(map.get("assignTime").toString()));
						map.put("assignTime", assignedTime);
					}
					//截止时间
					if(map.get("requireCompleteTime") != null){
						String requireCompleteTime = sdf2.format(sdf.parse(map.get("requireCompleteTime").toString()));
						map.put("requireCompleteTime", requireCompleteTime);
					}
					
				}
				//记录子任务数，用作在前台进行判断
				formJsonMap.put("subTaskSize", subTasks.size()+"");
			}else{
				//记录子任务数，用作在前台进行判断
				formJsonMap.put("subTaskSize", "0");
			}

			//基本信息，WOID、TOID
			mch.addGroup("header", formJsonMap);
			//子任务列表
			Map<String, String> subTaskMap = new HashMap<String, String>();
			subTaskMap.put("subTask", gson.toJson(subTasks));
			mch.addGroup("subTaskArea", subTaskMap);
			mobilePackage.setContent(mch.mapToJson());
			
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 getSubTaskOfSceneActionForMobile()");
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 getSubTaskOfSceneActionForMobile()");
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 getSubTaskOfSceneActionForMobile()");
		}
	}
	
	/**
	 * 加载转派现场任务页面
	 */
	public void loadToSendUrgentRepairSceneTaskActionForMobile(){
		log.info("进入 loadToSendUrgentRepairSceneTaskActionForMobile()");
		log.info("loadToSendUrgentRepairSceneTaskActionForMobile()  加载转派现场任务页面");
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
				log.info("退出 loadToSendUrgentRepairSceneTaskActionForMobile()");
				return;
			}
	
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			//获得任务单
			if(toId==null||"".equals(toId)){
				MobilePackageCommunicationHelper.responseMobileError("toId为空！");
				log.info("退出 loadToSendUrgentRepairSceneTaskActionForMobile()");
				return;
			}
			
			//获取转派维护队
			log.info("获取账号=="+account+"的所属维护队。");
			//List<ProviderOrganization> providerOrganizations = providerOrganizationService.getOrgByAccountAndOrgType(account, OrganizationConstant.ORGANIZATION_MAINTENANCETEAM);
			//yuan.yw
			List<SysOrg> providerOrganizations = this.sysOrganizationService.getOrgByAccountAndOrgType(account, OrganizationConstant.ORGANIZATION_MAINTENANCETEAM);
			if(providerOrganizations==null||providerOrganizations.isEmpty()){
				log.error("loadToSendUrgentRepairSceneTaskActionForMobile 调用 providerOrganizationService.getOrgByAccountAndOrgType 获取账号=="+account+"没有所属维护队。");
				MobilePackageCommunicationHelper.responseMobileError("loadToSendUrgentRepairSceneTaskActionForMobile 调用 providerOrganizationService.getOrgByAccountAndOrgType 获取账号=="+account+"没有所属维护队。");
				log.info("退出 loadToSendUrgentRepairSceneTaskActionForMobile()");
				return;
			}
			List<Map<String,String>> teamList = new ArrayList<Map<String,String>>();
			for(SysOrg org:providerOrganizations){
				log.info("获取组织=="+org.getOrgId()+"的父级组织。");
				//List<ProviderOrganization> parentOrgs = providerOrganizationService.getUpProviderOrgByOrgIdService(org.getId());
				//yuan.yw
				List<SysOrg> parentOrgs =this.sysOrganizationService.getUpOrNextLevelOrgByOrgIdService(org.getOrgId(),"parent");
				if(parentOrgs!=null&&!parentOrgs.isEmpty()){
					for(SysOrg parentOrg : parentOrgs){
						//List<ProviderOrganization> teams  = providerOrganizationService.getOrgListDownwardByOrgTypeAndOrg(parentOrg.getOrgId(), OrganizationConstant.ORGANIZATION_MAINTENANCETEAM);
						List<Map<String,Object>> teams  = this.sysOrganizationService.getOrgListDownwardOrUpwardByOrgTypeAndOrgIdService(parentOrg.getOrgId(), OrganizationConstant.ORGANIZATION_MAINTENANCETEAM,"child");
						if(teams==null||teams.isEmpty()){
							log.info("组织=="+org.getOrgId()+"的下级组织没有维护队。");
							continue;
						}
						List<Map<String,String>> teamInfo = new ArrayList<Map<String,String>>();
						for(Map<String,Object> team : teams){
							long orgId = Long.valueOf(team.get("orgId")+"");
							//List<Staff> accountList = providerOrganizationService.getStaffListDownwardByRoleAndOrg(orgId, BizAuthorityConstant.ROLE_TRAMLEADER);
							//yuan.yw
							List<Map<String,Object>> accountList =this.sysOrganizationService.getProviderStaffByOrgIdAndRoleCode(orgId, BizAuthorityConstant.ROLE_TRAMLEADER, "downward");
							String teamLeader = "";
							if(accountList!=null&&!accountList.isEmpty()){
								teamLeader = accountList.get(0).get("NAME")+"";
							}
							//team.setDutyPerson(teamLeader);
							Map<String,String> mp = new HashMap<String,String>();
							mp.put("dutyPerson", teamLeader);
							mp.put("orgId",team.get("orgId")+"");
							mp.put("name",team.get("name")+"");
							teamInfo.add(mp);
						}
						
						teamList.addAll(teamInfo);
					}
				}
				

			}
			Map<String,String> newFormJsonMap =  new HashMap<String,String>();
			String json = gson.toJson(teamList);
			newFormJsonMap.put("hiddenTeamList", json);
			newFormJsonMap.put("WOID", woId);
			newFormJsonMap.put("TOID", toId);
			mch.addGroup("toSendTaskForm", newFormJsonMap);
			
			mobilePackage.setContent(mch.mapToJson());
	
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 loadToSendUrgentRepairSceneTaskActionForMobile()");
		} catch (RuntimeException e) {
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadToSendUrgentRepairSceneTaskActionForMobile()");
		} 
	}
	
	/**
	 * 获取维护队员列表
	 */
	public void getToSendTeamerInfoListActionForMobile(){
		log.info("进入 getToSendTeamerInfoListActionForMobile()");
		log.info("getToSendTeamerInfoListActionForMobile() 获取维护队员列表");
		try{
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
			String ORGID = formJsonMap.get("ORGID");
			log.info("参数 [GRGID="+ORGID+"]");
			Map newFormJsonMap =  new HashMap();
			if(ORGID!=null&&!"".equals(ORGID)){
				//获取维护队员列表
				List<Map> teamersLists = urgentRepairSenceTaskOrderService.loadTeamersInfoService(ORGID,"");
				String json = gson.toJson(teamersLists);
				newFormJsonMap.put("hiddenTeamerList", json);
				mch.addGroup("toSendTaskForm", newFormJsonMap);
			}
			mobilePackage.setContent(mch.mapToJson());
			
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			log.info("退出 getToSendTeamerInfoListActionForMobile()");
		}catch (RuntimeException e){
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 getToSendTeamerInfoListActionForMobile()");
		}
	}
	
	/**
	 * 转派现场任务单
	 * @return
	 */
	public void toSendUrgentRepairSceneTaskOrderActionForMobile() {
		log.info("进入 toSendUrgentRepairSceneTaskOrderActionForMobile()");
		log.info("toSendUrgentRepairSceneTaskOrderActionForMobile() 转派现场任务单");
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			String selectTeamer = formJsonMap.get("selectTeamer");
			String comment = formJsonMap.get("comment");
			urgentRepairSenceTaskOrderService.txToSendUrgentRepairSenceTaskOrder(sendor, woId, toId, selectTeamer, comment);
			//组装mobilepackage
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
			//转派完毕后，重新加载
			MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
			//升级后跳转页面
			log.info("退出 toSendUrgentRepairSceneTaskOrderActionForMobile()");
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 toSendUrgentRepairSceneTaskOrderActionForMobile()");
		}
	}
	
	/**
	 * 加载催办专家任务单
	 * @return
	 */
	public void loadHastenUrgentRepairTechSupportTaskOrderPageActionForMobile() {
		log.info("进入 loadHastenUrgentRepairTechSupportTaskOrderPageActionForMobile()");
		log.info("loadHastenUrgentRepairTechSupportTaskOrderPageActionForMobile() 加载催办专家任务单");
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			String comment = formJsonMap.get("comment");
			
			Map newFormJsonMap =  new HashMap();
			newFormJsonMap.put("WOID", woId);
			newFormJsonMap.put("TOID", toId);
			mch.addGroup("hastenTaskForm", newFormJsonMap);
			//组装mobilepackage
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
			//转派完毕后，重新加载
			MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
			//升级后跳转页面
			log.info("退出 loadHastenUrgentRepairTechSupportTaskOrderPageActionForMobile()");
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 loadHastenUrgentRepairTechSupportTaskOrderPageActionForMobile()");
		}
	}
	
	/**
	 * 催办专家任务单
	 * @return
	 */
	public void hastenUrgentRepairTechSupportTaskOrderActionForMobile() {
		log.info("进入 hastenUrgentRepairTechSupportTaskOrderActionForMobile()");
		log.info("hastenUrgentRepairTechSupportTaskOrderActionForMobile() 催办专家任务单");
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
			this.toId = formJsonMap.get("TOID");
			this.woId = formJsonMap.get("WOID");
			log.info("参数 [woId="+woId+",toId="+toId+"]");
			String comment = formJsonMap.get("comment");
			String urgeWorkComment = formJsonMap.get("urgeWorkComment");
			//获取当前处理人
			//ou.jh
			SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(sendor);
//			Staff staff = providerOrganizationService.getStaffByAccount(sendor);
			if(sysOrgUserByAccount==null){
				log.error("hastenUrgentRepairTechSupportTaskOrderActionForMobile 调用 providerOrganizationService.getStaffByAccount 获取不到账号=="+sendor+"的个人信息");
				MobilePackageCommunicationHelper.responseMobileError("hastenUrgentRepairTechSupportTaskOrderActionForMobile 调用 providerOrganizationService.getStaffByAccount 获取不到账号=="+sendor+"的个人信息");
				log.info("退出 hastenUrgentRepairTechSupportTaskOrderActionForMobile()");
			}
			
			//获取接收催办信息对象
			WorkmanageTaskorder to =  workManageService.getTaskOrderEntity(toId);
			if(to == null){
				MobilePackageCommunicationHelper.responseMobileError("找不到催办任务单，toId=="+toId);
			}
			String receivePerson = to.getCurrentHandler();
			
			//服务跟踪记录
			Tasktracerecord tasktracerecord = new Tasktracerecord();
			tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
			tasktracerecord.setHandler(sendor);
			tasktracerecord.setHandleTime(new Date());
			tasktracerecord.setHandleWay("催办专家任务单");
			tasktracerecord.setWoId(woId);
			tasktracerecord.setToId(toId);
			tasktracerecord.setWoType("抢修工单");
			tasktracerecord.setHandleResultDescription("【催办任务】"+toId+"【催办意见】"+urgeWorkComment);
			boolean flag = workManageService.stepReply(tasktracerecord);
			if(flag){
				//消息盒子
				BizMessage bizMsg = new BizMessage();
				bizMsg.setContent(urgeWorkComment);
				bizMsg.setSendPerson(sendor);
				bizMsg.setReceivePerson(receivePerson);
				bizMsg.setLink("op/urgentrepair/loadUrgentRepairTechSupportTaskOrderPageAction?WOID="+woId+"&TOID="+toId);
				bizMsg.setLinkForMobile("loadUrgentRepairTechSupportTaskOrderPageActionForMobile?WOID="+woId+"&TOID="+toId);
				bizMsg.setSendTime(new Date());
				bizMsg.setFunctionType("UrgentRepairTechSupportTaskOrder");
				bizMsg.setWoId(woId);
				bizMsg.setToId(toId);
				bizMsg.setTitle(toId);
				bizMsg.setType("抢修");
				bizMessageService.txAddBizMessageService(bizMsg,"hasten");
			}
			//组装mobilepackage
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage = getTaskOrderMobilePackageByToId(toId,newMobilePackage,mch,formJsonMap);
			//转派完毕后，重新加载
			MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
			log.info("退出 hastenUrgentRepairTechSupportTaskOrderActionForMobile()");
			//升级后跳转页面
		} catch (RuntimeException e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
			log.info("退出 hastenUrgentRepairTechSupportTaskOrderActionForMobile()");
		}
	}

	public UrgentRepairWorkOrderService getUrgentRepairWorkOrderService() {
		return urgentRepairWorkOrderService;
	}

	public void setUrgentRepairWorkOrderService(
			UrgentRepairWorkOrderService urgentRepairWorkOrderService) {
		this.urgentRepairWorkOrderService = urgentRepairWorkOrderService;
	}

	public UrgentRepairSenceTaskOrderService getUrgentRepairSenceTaskOrderService() {
		return urgentRepairSenceTaskOrderService;
	}

	public void setUrgentRepairSenceTaskOrderService(
			UrgentRepairSenceTaskOrderService urgentRepairSenceTaskOrderService) {
		this.urgentRepairSenceTaskOrderService = urgentRepairSenceTaskOrderService;
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
