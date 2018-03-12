package com.iscreate.op.action.urgentrepair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.constant.OrganizationConstant;
import com.iscreate.op.dao.publicinterface.WorkOrderAssnetResourceDao;
import com.iscreate.op.pojo.publicinterface.Tasktracerecord;
import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairTechsupporttaskorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.urgentrepair.UrgentRepairTechSupportTaskOrderService;
import com.iscreate.op.service.urgentrepair.UrgentRepairWorkOrderService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.TimeFormatHelper;

public class UrgentRepairTechSupportTaskOrderAction {
	private String WOID;
	private String TOID;
	private String ORGID;
	private String recipient;
	private String actor;
	private Map<String,String> tech_workOrderInfo;
	private Map<String,String> taskOrderInfo;
	private Map<String,String> tech_taskOrderInfo;
	private String stepReply_processingProgress;
	private String stepReply_faultType;
	private String stepReply_faultResult;
	private String stepReply_desc;
	private String pageType;//跳转页面类型
	private String customerWoType;
	private String search_value;
	//参数类
	private WorkmanageTaskorder workmanageTaskorder = new WorkmanageTaskorder();
	private UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder = new UrgentrepairTechsupporttaskorder();
	private Tasktracerecord tasktracerecord;
	//注入service
	private UrgentRepairTechSupportTaskOrderService  urgentRepairTechSupportTaskOrderService;
	private UrgentRepairWorkOrderService urgentRepairWorkOrderService;
	private WorkManageService workManageService;
	private WorkOrderAssnetResourceDao workOrderAssnetResourceDao;
	private BizMessageService bizMessageService;
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	private  static final  Log log = LogFactory.getLog(UrgentRepairTechSupportTaskOrderAction.class);
	
	/**
	 * 受理技术任务单
	 * */
	public String acceptUrgentRepairTechSupportTaskOrderAction() {
		log.info("进入 acceptUrgentRepairTechSupportTaskOrderAction()");
		log.info("acceptUrgentRepairTechSupportTaskOrderAction() 受理技术任务单");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",workmanageTaskorder = "+workmanageTaskorder+"]");
		//参数转换
		String accept = (String) SessionService.getInstance().getValueByKey(
		"userId");
		urgentRepairTechSupportTaskOrderService.txAcceptUrgentRepairTechSupportTaskOrder(accept,WOID, TOID,workmanageTaskorder);
		
		log.info("退出 acceptUrgentRepairTechSupportTaskOrderAction()");
		
		return "success";
	}


	/**
	 * 驳回现场任务单
	 * */
	public String rejectUrgentRepairTechSupportTaskOrderAction() {
		log.info("进入 rejectUrgentRepairTechSupportTaskOrderAction()");
		log.info("rejectUrgentRepairTechSupportTaskOrderAction() 驳回现场任务单");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",workmanageTaskorder = "+workmanageTaskorder+"]");
		//参数转换
		String accept = (String) SessionService.getInstance().getValueByKey(
		"userId");
		urgentRepairTechSupportTaskOrderService.txRejectUrgentRepairTechSupportTaskOrder(accept,WOID,TOID, workmanageTaskorder);
		
		log.info("退出 rejectUrgentRepairTechSupportTaskOrderAction()");
		
		return "success";
		
	}
	
	/**
	 * 最终回复技术任务单
	 * */
	public String finalReplyUrgentRepairTechSupportTaskOrderAction() {
		log.info("进入 finalReplyUrgentRepairTechSupportTaskOrderAction()");
		log.info("finalReplyUrgentRepairTechSupportTaskOrderAction() 最终回复技术任务单");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",urgentrepairTechsupporttaskorder = "+urgentrepairTechsupporttaskorder+"]");
		//参数转换
		String reply = (String) SessionService.getInstance().getValueByKey(
		"userId");
		urgentRepairTechSupportTaskOrderService.txReplyUrgentRepairTechSupportTaskOrder(reply, WOID,TOID, urgentrepairTechsupporttaskorder);
		
		log.info("退出 finalReplyUrgentRepairTechSupportTaskOrderAction()");
		
		return "success";
	}
	
	/**
	 * 阶段回复专家任务
	 * @return
	 */
	public void stepReplyUrgentRepairTechSupportTaskOrderAction(){
		log.info("进入 stepReplyUrgentRepairTechSupportTaskOrderAction()");
		log.info("stepReplyUrgentRepairTechSupportTaskOrderAction() 阶段回复专家任务");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",stepReply_desc = "+stepReply_desc+",stepReply_processingProgress = "+stepReply_processingProgress+",stepReply_faultType = "+stepReply_faultType+",stepReply_faultResult = "+stepReply_faultResult+",customerWoType = "+customerWoType+"]");
		
		String actor = (String) SessionService.getInstance().getValueByKey(
		"userId");
		
		String flag = urgentRepairTechSupportTaskOrderService.txStepReplyUrgentRepairTechSupportTaskOrder(actor, WOID, TOID, stepReply_desc, stepReply_processingProgress, stepReply_faultType, stepReply_faultResult,customerWoType);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			log.info("退出 stepReplyUrgentRepairTechSupportTaskOrderAction()");
			response.getWriter().write(flag);
		} catch (Exception e) {
			log.error("执行 stepReplyUrgentRepairTechSupportTaskOrderAction() 发送数据到页面失败");
		}
	}

	/**
	 * 加载专家任务单页面
	 * @return
	 */
	public String loadUrgentRepairTechSupportTaskOrderPageAction(){
		log.info("进入 loadUrgentRepairTechSupportTaskOrderPageAction()");
		log.info("loadUrgentRepairTechSupportTaskOrderPageAction() 加载专家任务单页面");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+"]");
		
		if(WOID==null||"".equals(WOID)){
			log.error("参数 WOID 为空。");
			throw new UserDefinedException("找不到该任务单");
		}
		
		if(TOID==null||"".equals(TOID)){
			log.error("参数 TOID 为空。");
			throw new UserDefinedException("找不到该任务单");
		}
		
		String actor = (String) SessionService.getInstance().getValueByKey(
		"userId");
		//工单公共信息
		tech_workOrderInfo = workManageService.getWorkOrderForShow(WOID);
		UrgentrepairWorkorder urgentrepairWorkorder = urgentRepairWorkOrderService.getUrgentRepairWorkOrder(WOID);
		if(tech_workOrderInfo!=null){
			if(urgentrepairWorkorder!=null){
				tech_workOrderInfo.put("acceptProfessional", urgentrepairWorkorder.getAcceptProfessional());
				String createTime = "";
				if(tech_workOrderInfo.get("createTime")!=null){
					createTime = TimeFormatHelper.getTimeFormatBySecond(tech_workOrderInfo.get("createTime"));
				}
				tech_workOrderInfo.put("createTime", createTime);
				
				//截取工单所属城市
				String faultArea ="";
				if(urgentrepairWorkorder.getFaultArea()!=null){
					faultArea = urgentrepairWorkorder.getFaultArea()+"";
					String[] areas = faultArea.split("-");
					tech_workOrderInfo.put("city", areas[1]);
				}
			}
		}else{
			throw new UserDefinedException("找不到该任务单");
		}
		
		List<Workorderassnetresource> workOrderAssnetResources = workOrderAssnetResourceDao.getWorkOrderAssnetResourceRecordDao("woId", WOID);
		if(workOrderAssnetResources!=null&&!workOrderAssnetResources.isEmpty()){
			Workorderassnetresource workOrderAssnetResource = workOrderAssnetResources.get(0);
			if(workOrderAssnetResource.getNetworkResourceId()!=null){
				tech_workOrderInfo.put("baseStationId", workOrderAssnetResource.getNetworkResourceId()+"");
			}else{
				tech_workOrderInfo.put("baseStationId", "");
			}
			
			if(workOrderAssnetResource.getNetworkResourceType()!=null){
				tech_workOrderInfo.put("baseStationType", workOrderAssnetResource.getNetworkResourceType()+"");
			}else{
				tech_workOrderInfo.put("baseStationType", "");
			}
			
		}
		//任务单公共信息
		tech_taskOrderInfo = workManageService.getTaskOrderForShow(TOID);
		if(tech_taskOrderInfo!=null){
			String finalCompleteTime = "";
			if(tech_taskOrderInfo.get("finalCompleteTime")!=null){
				finalCompleteTime = TimeFormatHelper.getTimeFormatBySecond(tech_taskOrderInfo.get("finalCompleteTime"));
			}
			tech_taskOrderInfo.put("finalCompleteTime",finalCompleteTime);
			
			String assignTime = "";
			if(tech_taskOrderInfo.get("assignTime")!=null){
				assignTime = TimeFormatHelper.getTimeFormatBySecond(tech_taskOrderInfo.get("assignTime"));
			}
			tech_taskOrderInfo.put("assignTime", assignTime);
			UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder = urgentRepairTechSupportTaskOrderService.getUrgentRepairTechSupportTaskOrder(TOID);
			if(urgentrepairTechsupporttaskorder!=null){
				String affectedServiceName ="";
				if(urgentrepairTechsupporttaskorder.getAffectedServiceName()!=null){
					affectedServiceName = urgentrepairTechsupporttaskorder.getAffectedServiceName();
				}
				tech_taskOrderInfo.put("affectedServiceName", affectedServiceName);
				String faultGenera ="";
				if(urgentrepairTechsupporttaskorder.getFaultGenera()!=null){
					faultGenera = urgentrepairTechsupporttaskorder.getFaultGenera();
				}
				tech_taskOrderInfo.put("faultGenera", faultGenera);
				String faultReason ="";
				if(urgentrepairTechsupporttaskorder.getFaultReason()!=null){
					faultReason = urgentrepairTechsupporttaskorder.getFaultReason();
				}
				tech_taskOrderInfo.put("faultReason", faultReason);
				String faultHandleDetail ="";
				if(urgentrepairTechsupporttaskorder.getFaultHandleDetail()!=null){
					faultHandleDetail = urgentrepairTechsupporttaskorder.getFaultHandleDetail();
				}
				tech_taskOrderInfo.put("faultHandleDetail", faultHandleDetail);
				String faultHandleResult ="";
				if(urgentrepairTechsupporttaskorder.getFaultHandleResult()!=null){
					faultHandleResult = urgentrepairTechsupporttaskorder.getFaultHandleResult();
				}
				tech_taskOrderInfo.put("faultHandleResult",faultHandleResult);
				String reasonForDelayApply ="";
				if(urgentrepairTechsupporttaskorder.getReasonForDelayApply()!=null){
					reasonForDelayApply = urgentrepairTechsupporttaskorder.getReasonForDelayApply();
				}
				tech_taskOrderInfo.put("reasonForDelayApply", reasonForDelayApply);
				//告警清除时间
				tech_taskOrderInfo.put("alarmClearTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairTechsupporttaskorder.getAlarmClearTime()));
				//故障解决时间
				tech_taskOrderInfo.put("faultSolveTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairTechsupporttaskorder.getFaultSolveTime()));
				//延迟解决时间
				tech_taskOrderInfo.put("foreseeSolveTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairTechsupporttaskorder.getForeseeSolveTime()));
				String isAlarmClear ="";
				if(urgentrepairTechsupporttaskorder.getIsAlarmClear()!=null){
					isAlarmClear = urgentrepairTechsupporttaskorder.getIsAlarmClear()+"";
				}
				tech_taskOrderInfo.put("isAlarmClear", isAlarmClear);
				String sideeffectService ="";
				if(urgentrepairTechsupporttaskorder.getSideeffectService()!=null){
					sideeffectService = urgentrepairTechsupporttaskorder.getSideeffectService()+"";
				}
				tech_taskOrderInfo.put("sideeffectService", sideeffectService);
				//判断当前操作者权限
				if(tech_taskOrderInfo.get("currentHandler")!=null&&!"".equals(tech_taskOrderInfo.get("currentHandler"))){
					if(actor.trim().equals(tech_taskOrderInfo.get("currentHandler").trim())){
						tech_taskOrderInfo.put("hasPower", "1");//拥有权限
						//更新消息盒子
						bizMessageService.updateBizMsgToHasReadByReceivePersonAndOrderIdService(actor,TOID); 
					}else{
						tech_taskOrderInfo.put("hasPower", "0");//没有权限
					}
				}else{
					tech_taskOrderInfo.put("hasPower", "0");//没有权限
				}
			}
			
			//查看是否派发过现场任务或专家任务
			tech_workOrderInfo.put("hasSceneTask", "has");
			tech_workOrderInfo.put("hasTechTask", "has");
		}else{
			throw new UserDefinedException("找不到该任务单");
		}
		log.info("退出 loadUrgentRepairTechSupportTaskOrderPageAction()");
		return "success";
	}
	
	/**
	 * 加载现场任务流转过程
	 * @return
	 */
	public String loadTechSupportTaskOrderProcedureAction(){
		log.info("进入 loadTechSupportTaskOrderProcedureAction()");
		log.info("loadTechSupportTaskOrderProcedureAction() 加载现场任务流转过程");
		log.info("参数：[TOID = "+TOID+"]");
		
		if(TOID==null||"".equals(TOID)){
			log.error("参数 TOID 为空。");
			throw new UserDefinedException("加载现场任务流转过程 获取参数 TOID 为空。");
		}
		
		//任务单公共信息
		taskOrderInfo = workManageService.getTaskOrderForShow(TOID);
		if(taskOrderInfo!=null){
			String assignTime = "";
			if(taskOrderInfo.get("assignTime")!=null){
				assignTime = TimeFormatHelper.getTimeFormatBySecond(taskOrderInfo.get("assignTime"));
			}
			taskOrderInfo.put("assignTime",assignTime);
			
			String requireCompleteTime = "";
			if(taskOrderInfo.get("requireCompleteTime")!=null){
				requireCompleteTime = TimeFormatHelper.getTimeFormatBySecond(taskOrderInfo.get("requireCompleteTime"));
			}
			taskOrderInfo.put("requireCompleteTime",requireCompleteTime);
			
			String acceptTime = "";
			if(taskOrderInfo.get("acceptTime")!=null){
				acceptTime = TimeFormatHelper.getTimeFormatBySecond(taskOrderInfo.get("acceptTime"));
			}
			taskOrderInfo.put("acceptTime",acceptTime);
			
			String rejectTime = "";
			if(taskOrderInfo.get("rejectTime")!=null){
				rejectTime = TimeFormatHelper.getTimeFormatBySecond(taskOrderInfo.get("rejectTime"));
			}
			taskOrderInfo.put("rejectTime",rejectTime);
			
			String finalCompleteTime = "";
			if(taskOrderInfo.get("finalCompleteTime")!=null){
				finalCompleteTime = TimeFormatHelper.getTimeFormatBySecond(taskOrderInfo.get("finalCompleteTime"));
			}
			taskOrderInfo.put("finalCompleteTime",finalCompleteTime);
			
			String cancelTime = "";
			if(taskOrderInfo.get("cancelTime")!=null){
				cancelTime = TimeFormatHelper.getTimeFormatBySecond(taskOrderInfo.get("cancelTime"));
			}
			taskOrderInfo.put("cancelTime",cancelTime);
			
			String reassignTime = "";
			if(taskOrderInfo.get("reassignTime")!=null){
				reassignTime = TimeFormatHelper.getTimeFormatBySecond(taskOrderInfo.get("reassignTime"));
			}
			taskOrderInfo.put("reassignTime",reassignTime);
			//任务单个性信息
			UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder = urgentRepairTechSupportTaskOrderService.getUrgentRepairTechSupportTaskOrder(TOID);
			if(urgentrepairTechsupporttaskorder!=null){
				String affectedServiceName ="";
				if(urgentrepairTechsupporttaskorder.getAffectedServiceName()!=null){
					affectedServiceName = urgentrepairTechsupporttaskorder.getAffectedServiceName();
				}
				taskOrderInfo.put("affectedServiceName", affectedServiceName);
				String faultGenera ="";
				if(urgentrepairTechsupporttaskorder.getFaultGenera()!=null){
					faultGenera = urgentrepairTechsupporttaskorder.getFaultGenera();
				}
				taskOrderInfo.put("faultGenera", faultGenera);
				String faultReason ="";
				if(urgentrepairTechsupporttaskorder.getFaultReason()!=null){
					faultReason = urgentrepairTechsupporttaskorder.getFaultReason();
				}
				taskOrderInfo.put("faultReason", faultReason);
				String faultHandleDetail ="";
				if(urgentrepairTechsupporttaskorder.getFaultHandleDetail()!=null){
					faultHandleDetail = urgentrepairTechsupporttaskorder.getFaultHandleDetail();
				}
				taskOrderInfo.put("faultHandleDetail", faultHandleDetail);
				String faultHandleResult ="";
				if(urgentrepairTechsupporttaskorder.getFaultHandleResult()!=null){
					faultHandleResult = urgentrepairTechsupporttaskorder.getFaultHandleResult();
				}
				taskOrderInfo.put("faultHandleResult",faultHandleResult);
				String reasonForDelayApply ="";
				if(urgentrepairTechsupporttaskorder.getReasonForDelayApply()!=null){
					reasonForDelayApply = urgentrepairTechsupporttaskorder.getReasonForDelayApply();
				}
				taskOrderInfo.put("reasonForDelayApply", reasonForDelayApply);
				//告警清除时间
				taskOrderInfo.put("alarmClearTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairTechsupporttaskorder.getAlarmClearTime()));
				//故障解决时间
				taskOrderInfo.put("faultSolveTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairTechsupporttaskorder.getFaultSolveTime()));
				//延迟解决时间
				taskOrderInfo.put("foreseeSolveTime",  TimeFormatHelper.getTimeFormatBySecond(urgentrepairTechsupporttaskorder.getForeseeSolveTime()));
				String isAlarmClear ="";
				if(urgentrepairTechsupporttaskorder.getIsAlarmClear()!=null){
					isAlarmClear = urgentrepairTechsupporttaskorder.getIsAlarmClear()+"";
				}
				taskOrderInfo.put("isAlarmClear", isAlarmClear);
				String sideeffectService ="";
				if(urgentrepairTechsupporttaskorder.getSideeffectService()!=null){
					sideeffectService = urgentrepairTechsupporttaskorder.getSideeffectService()+"";
				}
				taskOrderInfo.put("sideeffectService", sideeffectService);
				
				//获取服务跟踪记录，判断是否存在撤销，重派
				if(taskOrderInfo.get("rejectComment")!=null&&!"".equals(taskOrderInfo.get("rejectComment"))){
					taskOrderInfo.put("hasReject", "yes");
				}else{
					taskOrderInfo.put("hasReject", "no");
				}
				if(taskOrderInfo.get("reassignComment")!=null&&!"".equals(taskOrderInfo.get("reassignComment"))){
					taskOrderInfo.put("hasReAssign", "yes");
				}else{
					taskOrderInfo.put("hasReAssign", "no");
				}
			}
		}
		
		log.info("退出 loadTechSupportTaskOrderProcedureAction()");
		
		return pageType;
	}
	
	/**
	 * 生成转派现场任务组织树
	 */
	public void createToSendTechSupportTaskOrgTreeAction(){
		log.info("进入 createToSendTechSupportTaskOrgTreeAction()");
		log.info("createToSendTechSupportTaskOrgTreeAction() 生成转派现场任务组织树");
		log.info("参数：[actor = "+actor+"]");
		
		if(actor==null||"".equals(actor)){
			log.error("参数 actor 为空。");
			throw new UserDefinedException("生成转派现场任务组织树 获取参数 actor 为空。");
		}
		
		List<Map<String, Object>> providerOrganizationList = new ArrayList<Map<String, Object>>();
		//List<ProviderOrganization> pos = providerOrganizationService.getOrgFromOrgTreeByAccountAndOrgType(actor, OrganizationConstant.ORGANIZATION_PROJECTTEAM);
		//yuan.yw
		List<SysOrg> pos = this.sysOrganizationService.getOrgByAccountAndOrgType(actor, OrganizationConstant.ORGANIZATION_PROJECTTEAM);

		if(pos!=null&&!pos.isEmpty()){
			String orgIds = "";
			for(SysOrg po : pos){
				orgIds = ","+po.getOrgId();
				//Map temp = providerOrganizationService.getOrgTreeDownwardByOrgId(po.getOrgId());
				//providerOrganizationList.add(temp);
			}
			providerOrganizationList = this.sysOrganizationService.getOrgTreeDownwardByorgIds(orgIds.substring(1));
			
		}
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(providerOrganizationList);		
		try {
			log.info("退出 createToSendTechSupportTaskOrgTreeAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 createToSendTechSupportTaskOrgTreeAction() 发送数据到页面失败");
		}
	}
	
	/**
	 * 转派专家任务单
	 */
	public void toSendUrgentRepairTechSupportTaskOrderAction(){
		log.info("进入 toSendUrgentRepairTechSupportTaskOrderAction()");
		log.info("toSendUrgentRepairTechSupportTaskOrderAction() 转派专家任务单");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",recipient = "+recipient+",stepReply_desc = "+stepReply_desc+"]");
		String actor = (String) SessionService.getInstance().getValueByKey(
		"userId");
		boolean flag = urgentRepairTechSupportTaskOrderService.txToSendUrgentRepairTechSupportTaskOrder(actor, WOID, TOID, recipient, stepReply_desc);
		String result = "success";
		if(!flag){
			result="error";
		}
		
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			log.info("退出 toSendUrgentRepairTechSupportTaskOrderAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 toSendUrgentRepairTechSupportTaskOrderAction() 发送数据到页面失败");
		}
	}
	
	/**
	 * 获取转派专家
	 */
	public void loadToSendSpecialistInfoListAction(){
		log.info("进入 loadToSendSpecialistInfoListAction()");
		log.info("loadToSendSpecialistInfoListAction() 获取转派专家");
		log.info("参数：[ORGID = "+ORGID+"]");
		
		if(ORGID==null||"".equals(ORGID)){
			log.error("参数 ORGID 为空。");
			throw new UserDefinedException("获取转派专家 获取参数 ORGID 为空。");
		}
		
		Set<Map> teamersLists =urgentRepairTechSupportTaskOrderService.loadToSendSpecialistInfoService(ORGID,search_value);
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(teamersLists, new TypeToken<Set<Map>>(){}.getType());		
		try {
			log.info("退出 loadToSendSpecialistInfoListAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 loadToSendSpecialistInfoListAction() 发送数据到页面失败");
		}
	}

	public String getTOID() {
		return TOID;
	}


	public void setTOID(String toid) {
		TOID = toid;
	}


	public UrgentrepairTechsupporttaskorder getUrgentrepairTechsupporttaskorder() {
		return urgentrepairTechsupporttaskorder;
	}


	public void setUrgentrepairTechsupporttaskorder(
			UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder) {
		this.urgentrepairTechsupporttaskorder = urgentrepairTechsupporttaskorder;
	}


	public UrgentRepairTechSupportTaskOrderService getUrgentRepairTechSupportTaskOrderService() {
		return urgentRepairTechSupportTaskOrderService;
	}


	public void setUrgentRepairTechSupportTaskOrderService(
			UrgentRepairTechSupportTaskOrderService urgentRepairTechSupportTaskOrderService) {
		this.urgentRepairTechSupportTaskOrderService = urgentRepairTechSupportTaskOrderService;
	}


	public String getWOID() {
		return WOID;
	}


	public void setWOID(String wOID) {
		WOID = wOID;
	}

	

	public Map<String, String> getTech_workOrderInfo() {
		return tech_workOrderInfo;
	}


	public void setTech_workOrderInfo(Map<String, String> tech_workOrderInfo) {
		this.tech_workOrderInfo = tech_workOrderInfo;
	}


	public Map<String, String> getTaskOrderInfo() {
		return taskOrderInfo;
	}


	public void setTaskOrderInfo(Map<String, String> taskOrderInfo) {
		this.taskOrderInfo = taskOrderInfo;
	}


	public WorkmanageTaskorder getWorkmanageTaskorder() {
		return workmanageTaskorder;
	}


	public void setWorkmanageTaskorder(WorkmanageTaskorder workmanageTaskorder) {
		this.workmanageTaskorder = workmanageTaskorder;
	}


	public WorkManageService getWorkManageService() {
		return workManageService;
	}


	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}


	public String getStepReply_processingProgress() {
		return stepReply_processingProgress;
	}


	public void setStepReply_processingProgress(String stepReply_processingProgress) {
		this.stepReply_processingProgress = stepReply_processingProgress;
	}


	public String getStepReply_faultType() {
		return stepReply_faultType;
	}


	public void setStepReply_faultType(String stepReply_faultType) {
		this.stepReply_faultType = stepReply_faultType;
	}


	public String getStepReply_faultResult() {
		return stepReply_faultResult;
	}


	public void setStepReply_faultResult(String stepReply_faultResult) {
		this.stepReply_faultResult = stepReply_faultResult;
	}


	public String getStepReply_desc() {
		return stepReply_desc;
	}


	public void setStepReply_desc(String stepReply_desc) {
		this.stepReply_desc = stepReply_desc;
	}


	public Tasktracerecord getTasktracerecord() {
		return tasktracerecord;
	}


	public void setTasktracerecord(Tasktracerecord tasktracerecord) {
		this.tasktracerecord = tasktracerecord;
	}





	public String getORGID() {
		return ORGID;
	}


	public void setORGID(String orgid) {
		ORGID = orgid;
	}


	public String getRecipient() {
		return recipient;
	}


	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}


	public String getActor() {
		return actor;
	}


	public void setActor(String actor) {
		this.actor = actor;
	}


	public String getPageType() {
		return pageType;
	}


	public void setPageType(String pageType) {
		this.pageType = pageType;
	}


	public Map<String, String> getTech_taskOrderInfo() {
		return tech_taskOrderInfo;
	}


	public void setTech_taskOrderInfo(Map<String, String> tech_taskOrderInfo) {
		this.tech_taskOrderInfo = tech_taskOrderInfo;
	}


	public UrgentRepairWorkOrderService getUrgentRepairWorkOrderService() {
		return urgentRepairWorkOrderService;
	}


	public void setUrgentRepairWorkOrderService(
			UrgentRepairWorkOrderService urgentRepairWorkOrderService) {
		this.urgentRepairWorkOrderService = urgentRepairWorkOrderService;
	}


	public WorkOrderAssnetResourceDao getWorkOrderAssnetResourceDao() {
		return workOrderAssnetResourceDao;
	}


	public void setWorkOrderAssnetResourceDao(
			WorkOrderAssnetResourceDao workOrderAssnetResourceDao) {
		this.workOrderAssnetResourceDao = workOrderAssnetResourceDao;
	}


	public BizMessageService getBizMessageService() {
		return bizMessageService;
	}


	public void setBizMessageService(BizMessageService bizMessageService) {
		this.bizMessageService = bizMessageService;
	}


	public String getCustomerWoType() {
		return customerWoType;
	}


	public void setCustomerWoType(String customerWoType) {
		this.customerWoType = customerWoType;
	}


	public String getSearch_value() {
		return search_value;
	}


	public void setSearch_value(String search_value) {
		this.search_value = search_value;
	}
	
	
}
