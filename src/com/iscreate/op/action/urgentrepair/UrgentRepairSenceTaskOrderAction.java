package com.iscreate.op.action.urgentrepair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.iscreate.op.pojo.urgentrepair.UrgentrepairSencetaskorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairTechsupporttaskorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.urgentrepair.UrgentRepairSenceTaskOrderService;
import com.iscreate.op.service.urgentrepair.UrgentRepairWorkOrderService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.TimeFormatHelper;

public class UrgentRepairSenceTaskOrderAction {
	private String TOID;
	private String WOID;
	private String ORGID;
	private Map<String,String> scene_workOrderInfo;
	private Map<String,String> taskOrderInfo;
	private Map<String,String> scene_taskOrderInfo;
	private String recipient;
	private String actor;
	private String stepReply_processingProgress;
	private String stepReply_faultType;
	private String stepReply_faultResult;
	private String stepReply_desc;
	private String pageType;//跳转页面类型
	private String customerWoType;
	private String search_value;
	
	//参数类
	private UrgentrepairSencetaskorder urgentrepairSencetaskorder = new UrgentrepairSencetaskorder();
	private UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder = new UrgentrepairTechsupporttaskorder();
	private WorkmanageTaskorder workmanageTaskorder = new WorkmanageTaskorder();
	private Tasktracerecord tasktracerecord;
	//注入service
	private UrgentRepairSenceTaskOrderService urgentRepairSenceTaskOrderService;
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

	private  static final  Log log = LogFactory.getLog(UrgentRepairSenceTaskOrderAction.class);
	
	/**
	 * 受理现场任务单
	 * */
	public String acceptUrgentRepairSenceTaskOrderAction() {
		log.info("进入 acceptUrgentRepairSenceTaskOrderAction()");
		log.info("acceptUrgentRepairSenceTaskOrderAction() 受理现场任务单");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",workmanageTaskorder = "+workmanageTaskorder+"]");
		//参数转换
		String accept = (String) SessionService.getInstance().getValueByKey("userId");
		urgentRepairSenceTaskOrderService.txAcceptUrgentRepairSenceTaskOrder(accept,WOID,TOID, workmanageTaskorder);
		log.info("退出 acceptUrgentRepairSenceTaskOrderAction()");
		return "success";
	}
	
	/**
	 * 驳回现场任务单
	 * */
	public String rejectUrgentRepairSenceTaskOrderAction() {
		log.info("进入 rejectUrgentRepairSenceTaskOrderAction()");
		log.info("rejectUrgentRepairSenceTaskOrderAction() 驳回现场任务单");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",workmanageTaskorder = "+workmanageTaskorder+"]");
		//参数转换
		String accept = (String) SessionService.getInstance().getValueByKey(
		"userId");
		urgentRepairSenceTaskOrderService.txRejectUrgentRepairSenceTaskOrder(accept,WOID,TOID, workmanageTaskorder);
		log.info("退出 rejectUrgentRepairSenceTaskOrderAction()");
		return "success";
		
	}
	

	/**
	 * 重派任务
	 * */
	public void reAssignUrgentRepairTechSupportTaskOrderAction() {
		// 参数转换
		log.info("进入 reAssignUrgentRepairTechSupportTaskOrderAction()");
		log.info("reAssignUrgentRepairTechSupportTaskOrderAction() 驳回现场任务单");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",workmanageTaskorder = "+workmanageTaskorder+"]");
		HttpServletResponse response = ServletActionContext.getResponse();
		String actor = (String) SessionService.getInstance().getValueByKey(
				"userId");
		boolean falg = urgentRepairSenceTaskOrderService.txReAssignlUrgentRepairTechSupportTaskOrder(actor, WOID,TOID, workmanageTaskorder.getToId(), workmanageTaskorder);
		if(!falg){
			try {
				log.error("reAssignUrgentRepairTechSupportTaskOrderAction() 调用 urgentRepairSenceTaskOrderService.txReAssignlUrgentRepairTechSupportTaskOrder 失败");
				response.getWriter().write("error");
			} catch (Exception e) {
				log.error("执行 reAssignUrgentRepairTechSupportTaskOrderAction() 发送数据到页面失败");
			}
		}else{
			try {
				log.info("退出 reAssignUrgentRepairTechSupportTaskOrderAction()");
				response.getWriter().write("success");
			} catch (Exception e) {
				log.error("执行 reAssignUrgentRepairTechSupportTaskOrderAction() 发送数据到页面失败");
			}
		}
	}
	
	/**
	 * 撤销任务
	 * */
	public void cancelUrgentRepairTechSupportTaskOrderAction() {
		log.info("进入 cancelUrgentRepairTechSupportTaskOrderAction()");
		log.info("cancelUrgentRepairTechSupportTaskOrderAction() 驳回现场任务单");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",workmanageTaskorder = "+workmanageTaskorder+"]");
		// 参数转换
		HttpServletResponse response = ServletActionContext.getResponse();
		String actor = (String) SessionService.getInstance().getValueByKey(
				"userId");
		boolean falg = urgentRepairSenceTaskOrderService.txCancelUrgentRepairTechSupportTaskOrder(actor, WOID,TOID,workmanageTaskorder.getToId(), workmanageTaskorder);
		if(!falg){
			try {
				log.error("cancelUrgentRepairTechSupportTaskOrderAction() 调用 urgentRepairSenceTaskOrderService.txCancelUrgentRepairTechSupportTaskOrder 失败");
				response.getWriter().write("error");
			} catch (Exception e) {
				log.error("执行 cancelUrgentRepairTechSupportTaskOrderAction() 发送数据到页面失败");
			}
		}else{
			try {
				log.info("退出 cancelUrgentRepairTechSupportTaskOrderAction()");
				response.getWriter().write("success");
			} catch (Exception e) {
				log.error("执行 cancelUrgentRepairTechSupportTaskOrderAction() 发送数据到页面失败");
			}
		}
	}
	
	/**
	 * 催办任务
	 * */
	public void hastenUrgentRepairTechSupportTaskOrderAction() {
		log.info("进入 hastenUrgentRepairTechSupportTaskOrderAction()");
		log.info("hastenUrgentRepairTechSupportTaskOrderAction() 驳回现场任务单");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",workmanageTaskorder = "+workmanageTaskorder+"]");
		// 参数转换
		HttpServletResponse response = ServletActionContext.getResponse();
		String actor = (String) SessionService.getInstance().getValueByKey(
				"userId");
		boolean falg = urgentRepairSenceTaskOrderService.txHastenUrgentRepairTechSupportTaskOrder(actor, WOID,TOID, workmanageTaskorder.getToId(), workmanageTaskorder);
		if(!falg){
			try {
				log.error("hastenUrgentRepairTechSupportTaskOrderAction() 调用 urgentRepairSenceTaskOrderService.txHastenUrgentRepairTechSupportTaskOrder 失败");
				response.getWriter().write("error");
			} catch (Exception e) {
				log.error("执行 hastenUrgentRepairTechSupportTaskOrderAction() 发送数据到页面失败");
			}
		}else{
			try {
				log.info("退出 hastenUrgentRepairTechSupportTaskOrderAction()");
				response.getWriter().write("success");
			} catch (Exception e) {
				log.error("执行 hastenUrgentRepairTechSupportTaskOrderAction() 发送数据到页面失败");
			}
		}
	}

	/**
	 * 升级为(派发)技术支援任务单
	 * */
	public void createUrgentRepairTechSupportTaskOrderAction() {
		log.info("进入 createUrgentRepairTechSupportTaskOrderAction()");
		log.info("createUrgentRepairTechSupportTaskOrderAction() 升级为(派发)技术支援任务单");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",recipient = "+recipient+",workmanageTaskorder = "+workmanageTaskorder+"]");
		//参数转换
		String sendor = (String) SessionService.getInstance().getValueByKey(
		"userId");
		String result = "success";
		if(recipient==null||"".equals(recipient)){
			result = "error";
		}
		UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder = new UrgentrepairTechsupporttaskorder();
		urgentRepairSenceTaskOrderService.txCreateUrgentRepairTechSupportTaskOrder(sendor, recipient, WOID,TOID, urgentrepairTechsupporttaskorder,workmanageTaskorder);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			log.info("退出 createUrgentRepairTechSupportTaskOrderAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 createUrgentRepairTechSupportTaskOrderAction() 发送数据到页面失败");
		}
	}
	
	/**
	 * 最终回复现场任务单
	 * */
	public String finalReplyUrgentRepairSenceTaskOrderAction() {
		log.info("进入 finalReplyUrgentRepairSenceTaskOrderAction()");
		log.info("finalReplyUrgentRepairSenceTaskOrderAction() 最终回复现场任务单");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",recipient = "+recipient+",urgentrepairSencetaskorder = "+urgentrepairSencetaskorder+"]");
		//参数转换
		String reply = (String) SessionService.getInstance().getValueByKey(
		"userId");
		urgentRepairSenceTaskOrderService.txFinalReplyUrgentRepairSenceTaskOrder(reply,WOID, TOID, urgentrepairSencetaskorder);
		log.info("退出 finalReplyUrgentRepairSenceTaskOrderAction()");
		return "success";
	}
	

	
	/**
	 * 阶段回复现场任务
	 * @return
	 */
	public void stepReplyUrgentRepairSenceTaskOrderAction(){
		log.info("进入 stepReplyUrgentRepairSenceTaskOrderAction()");
		log.info("stepReplyUrgentRepairSenceTaskOrderAction() 阶段回复现场任务");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",stepReply_desc = "+stepReply_desc+",stepReply_processingProgress = "+stepReply_processingProgress+",stepReply_faultType = "+stepReply_faultType+",stepReply_faultResult = "+stepReply_faultResult+",customerWoType = "+customerWoType+"]");
		
		String actor = (String) SessionService.getInstance().getValueByKey(
		"userId");
		
		String flag = urgentRepairSenceTaskOrderService.txStepReplyUrgentRepairSenceTaskOrder(actor, WOID, TOID, stepReply_desc, stepReply_processingProgress, stepReply_faultType, stepReply_faultResult,customerWoType);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			log.info("退出 stepReplyUrgentRepairSenceTaskOrderAction()");
			response.getWriter().write(flag);
		} catch (Exception e) {
			log.error("执行 stepReplyUrgentRepairSenceTaskOrderAction() 发送数据到页面失败");
		}
	}
	
	/**
	 * 生成转派现场任务组织树
	 */
	public void createToSendSenceTaskOrgTreeAction(){
		log.info("进入 createToSendSenceTaskOrgTreeAction()");
		log.info("createToSendSenceTaskOrgTreeAction() 生成转派现场任务组织树");
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
			
		}else{
			log.error("createToSendSenceTaskOrgTreeAction 调用 providerOrganizationService.getOrgFromOrgTreeByAccountAndOrgType 查询 该账号所属的组织树的项目组节点及其子树 为空。");
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
			log.info("退出 createToSendSenceTaskOrgTreeAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 createToSendSenceTaskOrgTreeAction() 发送数据到页面失败");
		}
	}
	
	/**
	 * 转派现场任务单
	 */
	public void toSendUrgentRepairSenceTaskOrderAction(){
		log.info("进入 toSendUrgentRepairSenceTaskOrderAction()");
		log.info("toSendUrgentRepairSenceTaskOrderAction() 转派现场任务单");
		log.info("参数：[WOID = "+WOID+",TOID = "+TOID+",recipient = "+recipient+",recipient = "+recipient+",stepReply_desc = "+stepReply_desc+"]");
		String actor = (String) SessionService.getInstance().getValueByKey(
		"userId");
		boolean flag = urgentRepairSenceTaskOrderService.txToSendUrgentRepairSenceTaskOrder(actor, WOID, TOID, recipient, stepReply_desc);
		String result = "success";
		if(!flag){
			log.error("toSendUrgentRepairSenceTaskOrderAction 调用 urgentRepairSenceTaskOrderService.txToSendUrgentRepairSenceTaskOrder 失败");
			result="error";
		}
		
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			log.info("退出 toSendUrgentRepairSenceTaskOrderAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 createToSendSenceTaskOrgTreeAction() 发送数据到页面失败");
		}
	}
	
	/**
	 * 加载现场任务单页面
	 * @return
	 */
	public String loadUrgentRepairSenceTaskOrderPageAction(){
		log.info("进入 loadUrgentRepairSenceTaskOrderPageAction()");
		log.info("loadUrgentRepairSenceTaskOrderPageAction() 加载现场任务单页面");
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
		scene_workOrderInfo = workManageService.getWorkOrderForShow(WOID);
		UrgentrepairWorkorder urgentrepairWorkorder = urgentRepairWorkOrderService.getUrgentRepairWorkOrder(WOID);
		if(scene_workOrderInfo!=null){
			if(urgentrepairWorkorder!=null){
				scene_workOrderInfo.put("acceptProfessional", urgentrepairWorkorder.getAcceptProfessional());
				String createTime = "";
				if(scene_workOrderInfo.get("createTime")!=null){
					createTime = TimeFormatHelper.getTimeFormatBySecond(scene_workOrderInfo.get("createTime"));
				}
				scene_workOrderInfo.put("createTime",createTime);
				if(urgentrepairWorkorder.getCustomerWoType()!=null){
					scene_workOrderInfo.put("customerWoType", urgentrepairWorkorder.getCustomerWoType());
				}
				
				//截取工单所属城市
				String faultArea ="";
				if(urgentrepairWorkorder.getFaultArea()!=null){
					faultArea = urgentrepairWorkorder.getFaultArea()+"";
					String[] areas = faultArea.split("-");
					scene_workOrderInfo.put("city", areas[1]);
				}
				
			}
		}else{
			log.debug("loadUrgentRepairSenceTaskOrderPageAction() 调用 urgentRepairWorkOrderService.getUrgentRepairWorkOrder 获取工单公共信息为空");
			throw new UserDefinedException("找不到该任务单");
		}
		
		//获取关联网络资源
		List<Workorderassnetresource> workOrderAssnetResources = workOrderAssnetResourceDao.getWorkOrderAssnetResourceRecordDao("woId", WOID);
		if(workOrderAssnetResources!=null&&!workOrderAssnetResources.isEmpty()){
			Workorderassnetresource workOrderAssnetResource = workOrderAssnetResources.get(0);
			if(workOrderAssnetResource.getNetworkResourceId()!=null){
				scene_workOrderInfo.put("baseStationId", workOrderAssnetResource.getNetworkResourceId()+"");
			}else{
				scene_workOrderInfo.put("baseStationId", "");
			}
			
			if(workOrderAssnetResource.getNetworkResourceType()!=null){
				scene_workOrderInfo.put("baseStationType", workOrderAssnetResource.getNetworkResourceType()+"");
			}else{
				scene_workOrderInfo.put("baseStationType", "");
			}
			
		}else{
			log.debug("loadUrgentRepairSenceTaskOrderPageAction() 调用 workOrderAssnetResourceDao.getWorkOrderAssnetResourceRecordDao('woId', WOID) 获取关联网络资源为空");
			throw new UserDefinedException("找不到该任务单");
		}
		
		//任务单公共信息
		scene_taskOrderInfo = workManageService.getTaskOrderForShow(TOID);
		UrgentrepairSencetaskorder urgentrepairSencetaskorder = urgentRepairSenceTaskOrderService.getUrgentRepairSenceTaskOrder(TOID);
		if(scene_taskOrderInfo!=null){
			String taskName = "";
			if(scene_workOrderInfo.get("woTitle")!=null){
				taskName = scene_workOrderInfo.get("woTitle");
			}
			scene_taskOrderInfo.put("taskName", "专家处理任务："+taskName);
			
			String assignTime = "";
			if(scene_taskOrderInfo.get("assignTime")!=null){
				assignTime = TimeFormatHelper.getTimeFormatBySecond(scene_taskOrderInfo.get("assignTime"));
			}
			scene_taskOrderInfo.put("assignTime",assignTime);
			
			String finalCompleteTime = "";
			if(scene_taskOrderInfo.get("finalCompleteTime")!=null){
				finalCompleteTime = TimeFormatHelper.getTimeFormatBySecond(scene_taskOrderInfo.get("finalCompleteTime"));
			}
			scene_taskOrderInfo.put("finalCompleteTime",finalCompleteTime);
			if(urgentrepairSencetaskorder!=null){
				String affectedServiceName ="";
				if(urgentrepairSencetaskorder.getAffectedServiceName()!=null){
					affectedServiceName = urgentrepairSencetaskorder.getAffectedServiceName();
				}
				scene_taskOrderInfo.put("affectedServiceName", affectedServiceName);
				String faultGenera ="";
				if(urgentrepairSencetaskorder.getFaultGenera()!=null){
					faultGenera = urgentrepairSencetaskorder.getFaultGenera();
				}
				scene_taskOrderInfo.put("faultGenera", faultGenera);
				String faultReason ="";
				if(urgentrepairSencetaskorder.getFaultReason()!=null){
					faultReason = urgentrepairSencetaskorder.getFaultReason();
				}
				scene_taskOrderInfo.put("faultReason", faultReason);
				String faultHandleDetail ="";
				if(urgentrepairSencetaskorder.getFaultHandleDetail()!=null){
					faultHandleDetail = urgentrepairSencetaskorder.getFaultHandleDetail();
				}
				scene_taskOrderInfo.put("faultHandleDetail", faultHandleDetail);
				String faultHandleResult ="";
				if(urgentrepairSencetaskorder.getFaultHandleResult()!=null){
					faultHandleResult = urgentrepairSencetaskorder.getFaultHandleResult();
				}
				scene_taskOrderInfo.put("faultHandleResult",faultHandleResult);
				String reasonForDelayApply ="";
				if(urgentrepairSencetaskorder.getReasonForDelayApply()!=null){
					reasonForDelayApply = urgentrepairSencetaskorder.getReasonForDelayApply();
				}
				scene_taskOrderInfo.put("reasonForDelayApply", reasonForDelayApply);
				//告警清除时间
				scene_taskOrderInfo.put("alarmClearTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairSencetaskorder.getAlarmClearTime()));
				//故障解决时间
				scene_taskOrderInfo.put("faultSolveTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairSencetaskorder.getFaultSolveTime()));
				//延迟解决时间
				scene_taskOrderInfo.put("foreseeSolveTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairSencetaskorder.getForeseeSolveTime()));
				String isAlarmClear ="";
				if(urgentrepairSencetaskorder.getIsAlarmClear()!=null){
					isAlarmClear = urgentrepairSencetaskorder.getIsAlarmClear()+"";
				}
				scene_taskOrderInfo.put("isAlarmClear", isAlarmClear);
				String sideeffectService ="";
				if(urgentrepairSencetaskorder.getSideeffectService()!=null){
					sideeffectService = urgentrepairSencetaskorder.getSideeffectService()+"";
				}
				scene_taskOrderInfo.put("sideeffectService", sideeffectService);
				
				//判断当前操作者权限
				if(scene_taskOrderInfo.get("currentHandler")!=null&&!"".equals(scene_taskOrderInfo.get("currentHandler"))){
					if(actor.trim().equals(scene_taskOrderInfo.get("currentHandler").trim())){
						scene_taskOrderInfo.put("hasPower", "1");//拥有权限
						//更新消息盒子
						bizMessageService.updateBizMsgToHasReadByReceivePersonAndOrderIdService(actor,TOID); 
					}else{
						scene_taskOrderInfo.put("hasPower", "0");//没有权限
					}
				}else{
					scene_taskOrderInfo.put("hasPower", "0");//没有权限
				}
			}
			
			//查看是否派发过现场任务或专家任务
			scene_workOrderInfo.put("hasSceneTask", "has");
			List<Map> tos =  workManageService.getChildTaskOrderListByToId(TOID);
			if(tos!=null&&!tos.isEmpty()){
				scene_workOrderInfo.put("hasTechTask", "has");
			}else{
				
				scene_workOrderInfo.put("hasTechTask", "not");
			}
		}else{
			log.debug("loadUrgentRepairSenceTaskOrderPageAction() 调用 urgentRepairSenceTaskOrderService.getUrgentRepairSenceTaskOrder(TOID) 获取任务单信息为空");
			throw new UserDefinedException("找不到该任务单");
		}
		log.info("退出 toSendUrgentRepairSenceTaskOrderAction()");
		return "success";
	}
	
	/**
	 * 加载现场任务流转过程
	 * @return
	 */
	public String loadSenceTaskOrderProcedureAction(){
		log.info("进入 loadSenceTaskOrderProcedureAction()");
		log.info("loadSenceTaskOrderProcedureAction() 加载现场任务流转过程");
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
			UrgentrepairSencetaskorder urgentrepairSencetaskorder = urgentRepairSenceTaskOrderService.getUrgentRepairSenceTaskOrder(TOID);
			if(urgentrepairSencetaskorder!=null){
				String affectedServiceName ="";
				if(urgentrepairSencetaskorder.getAffectedServiceName()!=null){
					affectedServiceName = urgentrepairSencetaskorder.getAffectedServiceName();
				}
				taskOrderInfo.put("affectedServiceName", affectedServiceName);
				String faultGenera ="";
				if(urgentrepairSencetaskorder.getFaultGenera()!=null){
					faultGenera = urgentrepairSencetaskorder.getFaultGenera();
				}
				taskOrderInfo.put("faultGenera", faultGenera);
				String faultReason ="";
				if(urgentrepairSencetaskorder.getFaultReason()!=null){
					faultReason = urgentrepairSencetaskorder.getFaultReason();
				}
				taskOrderInfo.put("faultReason", faultReason);
				String faultHandleDetail ="";
				if(urgentrepairSencetaskorder.getFaultHandleDetail()!=null){
					faultHandleDetail = urgentrepairSencetaskorder.getFaultHandleDetail();
				}
				taskOrderInfo.put("faultHandleDetail", faultHandleDetail);
				String faultHandleResult ="";
				if(urgentrepairSencetaskorder.getFaultHandleResult()!=null){
					faultHandleResult = urgentrepairSencetaskorder.getFaultHandleResult();
				}
				taskOrderInfo.put("faultHandleResult",faultHandleResult);
				String reasonForDelayApply ="";
				if(urgentrepairSencetaskorder.getReasonForDelayApply()!=null){
					reasonForDelayApply = urgentrepairSencetaskorder.getReasonForDelayApply();
				}
				taskOrderInfo.put("reasonForDelayApply", reasonForDelayApply);
				//告警清除时间
				taskOrderInfo.put("alarmClearTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairSencetaskorder.getAlarmClearTime()));
				//故障解决时间
				taskOrderInfo.put("faultSolveTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairSencetaskorder.getFaultSolveTime()));
				//延迟解决时间
				taskOrderInfo.put("foreseeSolveTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairSencetaskorder.getForeseeSolveTime()));
				String isAlarmClear ="";
				if(urgentrepairSencetaskorder.getIsAlarmClear()!=null){
					isAlarmClear = urgentrepairSencetaskorder.getIsAlarmClear()+"";
				}
				taskOrderInfo.put("isAlarmClear", isAlarmClear);
				String sideeffectService ="";
				if(urgentrepairSencetaskorder.getSideeffectService()!=null){
					sideeffectService = urgentrepairSencetaskorder.getSideeffectService()+"";
				}
				taskOrderInfo.put("sideeffectService", sideeffectService);
				
				//获取服务跟踪记录，判断是否存在驳回，重派
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
		}else{
			log.debug("loadSenceTaskOrderProcedureAction 调用 workManageService.getTaskOrderForShow(TOID) 获取任务单信息失败");
		}
		log.info("退出 loadSenceTaskOrderProcedureAction()");
		return pageType;
	}
	
	/**
	 * 获取专家
	 */
	public void  loadSpecialistInfoAction(){
		log.info("进入 loadSpecialistInfoAction()");
		log.info("loadSpecialistInfoAction() 获取专家");
		//获取当前责任人
		String account = (String) SessionService.getInstance().getValueByKey(
		"userId");
		List<Map> specialistLists = urgentRepairSenceTaskOrderService.loadSpecialistInfoService(account);
	
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(specialistLists, new TypeToken<List<Map>>(){}.getType());		
		try {
			log.info("退出 loadSpecialistInfoAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 loadSpecialistInfoAction() 发送数据到页面失败");
		}
	}
	
	/**
	 * 获取下级任务单
	 * @return
	 */
	public void getSceneTaskOrderChildrenTaskOrderAction(){
		log.info("进入 getSceneTaskOrderChildrenTaskOrderAction()");
		log.info("getSceneTaskOrderChildrenTaskOrderAction() 获取下级任务单");
		log.info("参数：[TOID = "+TOID+"]");
		
		if(TOID==null||"".equals(TOID)){
			log.error("参数 TOID 为空。");
			throw new UserDefinedException("获取下级任务单 获取参数 TOID 为空。");
		}
		
		List<Map> taskorderMap =  workManageService.getChildTaskOrderListByToId(TOID);
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(taskorderMap, new TypeToken<List<Map>>(){}.getType());		
		try {
			log.info("退出 getSceneTaskOrderChildrenTaskOrderAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getSceneTaskOrderChildrenTaskOrderAction() 发送数据到页面失败");
		}
	}
	
	/**
	 * 获取维护人员
	 */
	public void loadToSendTeamerInfoListAction(){
		log.info("进入 loadToSendTeamerInfoListAction()");
		log.info("getSceneTaskOrderChildrenTaskOrderAction() 获取维护人员");
		log.info("参数：[ORGID = "+ORGID+"]");
		
		if(ORGID==null||"".equals(ORGID)){
			log.error("参数 ORGID 为空。");
			throw new UserDefinedException("获取维护人员 获取参数 ORGID 为空。");
		}
		
		List<Map> teamersLists = urgentRepairSenceTaskOrderService.loadTeamersInfoService(ORGID,search_value);
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(teamersLists, new TypeToken<List<Map>>(){}.getType());		
		try {
			log.info("退出 loadToSendTeamerInfoListAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 loadToSendTeamerInfoListAction() 发送数据到页面失败");
		}
	}

	public String getTOID() {
		return TOID;
	}

	public void setTOID(String toid) {
		TOID = toid;
	}

	public String getWOID() {
		return WOID;
	}

	public void setWOID(String woid) {
		WOID = woid;
	}

	public UrgentrepairSencetaskorder getUrgentrepairSencetaskorder(){
		return urgentrepairSencetaskorder;
	}

	public void setUrgentrepairSencetaskorder(
			UrgentrepairSencetaskorder urgentrepairSencetaskorder) {
		this.urgentrepairSencetaskorder = urgentrepairSencetaskorder;
	}

	public UrgentrepairTechsupporttaskorder getUrgentrepairTechsupporttaskorder() {
		return urgentrepairTechsupporttaskorder;
	}

	public void setUrgentrepairTechsupporttaskorder(
			UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder) {
		this.urgentrepairTechsupporttaskorder = urgentrepairTechsupporttaskorder;
	}

	public UrgentRepairSenceTaskOrderService getUrgentRepairSenceTaskOrderService() {
		return urgentRepairSenceTaskOrderService;
	}

	public void setUrgentRepairSenceTaskOrderService(
			UrgentRepairSenceTaskOrderService urgentRepairSenceTaskOrderService) {
		this.urgentRepairSenceTaskOrderService = urgentRepairSenceTaskOrderService;
	}

	
	public Map<String, String> getScene_workOrderInfo() {
		return scene_workOrderInfo;
	}

	public void setScene_workOrderInfo(Map<String, String> scene_workOrderInfo) {
		this.scene_workOrderInfo = scene_workOrderInfo;
	}

	public Map<String, String> getScene_taskOrderInfo() {
		return scene_taskOrderInfo;
	}

	public void setScene_taskOrderInfo(Map<String, String> scene_taskOrderInfo) {
		this.scene_taskOrderInfo = scene_taskOrderInfo;
	}

	public Map<String, String> getTaskOrderInfo() {
		return taskOrderInfo;
	}

	public void setTaskOrderInfo(Map<String, String> taskOrderInfo) {
		this.taskOrderInfo = taskOrderInfo;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
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

	public Tasktracerecord getTasktracerecord() {
		return tasktracerecord;
	}

	public void setTasktracerecord(Tasktracerecord tasktracerecord) {
		this.tasktracerecord = tasktracerecord;
	}

	public String getStepReply_desc() {
		return stepReply_desc;
	}

	public void setStepReply_desc(String stepReply_desc) {
		this.stepReply_desc = stepReply_desc;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}


	public String getORGID() {
		return ORGID;
	}

	public void setORGID(String orgid) {
		ORGID = orgid;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
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
