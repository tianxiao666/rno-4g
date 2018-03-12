package com.iscreate.op.action.urgentrepair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.iscreate.op.pojo.urgentrepair.UrgentrepairCustomerworkorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairSencetaskorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.urgentrepair.UrgentRepairSenceTaskOrderService;
import com.iscreate.op.service.urgentrepair.UrgentRepairWorkOrderService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.TimeFormatHelper;

public class UrgentRepairWorkOrderAction {

	private String WOID;
	private String ORGID;
	private String accountForGIS;//GIS传来的派发对象
	private String baseStationId;
	private String baseStationType;
	private Map<String,String> stationOfGIS;
	private String entryType;//入口类型，判断是否从GIS传来
	private long providerOrgId;
	private String woTemplateId;
	private String toTemplateId;
	private Map<String,String> workOrderInfo;
	private Map<String,String> com_workOrderInfo;
	private Map<String,String> scene_taskOrderInfo;
	private String recipient;
	private String actor;
	private String stepReply_processingProgress;
	private String stepReply_faultType;
	private String stepReply_faultResult;
	private String stepReply_desc;
	private String moudleId;
	private String content;
	private String customerWoType;
	
	//参数类
	private WorkmanageWorkorder workmanageWorkorder = new WorkmanageWorkorder();
	private WorkmanageTaskorder workmanageTaskorder = new WorkmanageTaskorder();
	private UrgentrepairWorkorder urgentrepairWorkorder = new UrgentrepairWorkorder();
	private UrgentrepairCustomerworkorder urgentrepairCustomerworkorder = new UrgentrepairCustomerworkorder();
	private Workorderassnetresource workorderassnetresource = new Workorderassnetresource();
	private UrgentrepairSencetaskorder urgentrepairSencetaskorder = new UrgentrepairSencetaskorder();
	private Tasktracerecord tasktracerecord;
	//注入service
	private UrgentRepairWorkOrderService urgentRepairWorkOrderService;
	private UrgentRepairSenceTaskOrderService urgentRepairSenceTaskOrderService;
	private WorkManageService workManageService;
	private NetworkResourceInterfaceService networkResourceService;
	private WorkOrderAssnetResourceDao workOrderAssnetResourceDao;
	private BizMessageService bizMessageService;
	
	private  static final  Log log = LogFactory.getLog(UrgentRepairWorkOrderAction.class);
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}

	/**
	 * 创建抢修工单(包括客户工单)
	 * */
	public void createUrgentRepairWorkOrderAction() {
		log.info("进入 createUrgentRepairWorkOrderAction()");
		log.info("createUrgentRepairWorkOrderAction() 创建抢修工单(包括客户工单)");
		log.info("参数：[WOID = "+WOID+",urgentrepairWorkorder = "+urgentrepairWorkorder+",urgentrepairCustomerworkorder = "+urgentrepairCustomerworkorder+",workorderassnetresource = "+workorderassnetresource+",workmanageWorkorder = "+workmanageWorkorder+"]");
		// 参数转换
		HttpServletResponse response = ServletActionContext.getResponse();
		String creator = (String) SessionService.getInstance().getValueByKey("userId");
		WOID = urgentRepairWorkOrderService.txCreateUrgentRepairWorkOrder(creator, urgentrepairWorkorder, urgentrepairCustomerworkorder,workorderassnetresource ,workmanageWorkorder );
		if(WOID==null||"".equals(WOID)){
			try {
				log.error("方法 createUrgentRepairWorkOrderAction 无法成功创建工单，没有生成工单号 WOID");
				response.getWriter().write("error");
			} catch (Exception e) {
				log.error("执行 createUrgentRepairWorkOrderAction() 失败");
				
			}
		}else{
			try {
				log.info("退出 createUrgentRepairWorkOrderAction()");
				response.getWriter().write(WOID);
			} catch (Exception e) {
				log.error("执行 createUrgentRepairWorkOrderAction() 失败");
			}
		}
	}
	
	 /**
	  * 修改工单
	  */
	public void modifyUrgentRepairWorkOrderAction() {
		log.info("进入 modifyUrgentRepairWorkOrderAction()");
		log.info("modifyUrgentRepairWorkOrderAction() 修改工单");
		log.info("参数：[WOID = "+WOID+",urgentrepairWorkorder = "+urgentrepairWorkorder+",workorderassnetresource = "+workorderassnetresource+"]");
		// 参数转换
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		String flag = urgentRepairWorkOrderService.txModifytUrgentRepairWorkOrder(WOID, urgentrepairWorkorder, workorderassnetresource);
		try {
			log.info("退出 modifyUrgentRepairWorkOrderAction()");
			response.getWriter().write(flag);
		} catch (Exception e) {
			log.error("方法 modifyUrgentRepairWorkOrderAction 修改woId="+WOID+"的工单失败");
		}
}

	/**
	 * 受理抢修工单
	 * */
	public void acceptUrgentRepairWorkOrderAction() {
		log.info("进入 acceptUrgentRepairWorkOrderAction()");
		log.info("acceptUrgentRepairWorkOrderAction() 受理抢修工单");
		log.info("参数：[WOID = "+WOID+",entryType = "+entryType+",accountForGIS = "+accountForGIS+",urgentrepairWorkorder = "+urgentrepairWorkorder+"]");
		
		// 参数转换
		String actor = (String) SessionService.getInstance().getValueByKey(
				"userId");
		String flag = urgentRepairWorkOrderService.txAcceptUrgentRepairWorkOrder(actor, WOID, urgentrepairWorkorder);
		
		Map map = new HashMap();
		
		map.put("result", flag);
		if("GIS".equals(entryType)){
			map.put("entryType", "GIS");
			map.put("accountForGIS", accountForGIS);
		}
		map.put("woId", WOID);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(map);
		
		try {
			log.info("退出 acceptUrgentRepairWorkOrderAction()");
			response.getWriter().write(json);
		} catch (Exception e) {
			log.error("执行 acceptUrgentRepairWorkOrderAction() 失败");
		}
		
	}
	
	/**
	 * 重派任务
	 * */
	public void reAssignUrgentRepairSceneTaskOrderAction() {
		log.info("进入 reAssignUrgentRepairSceneTaskOrderAction()");
		log.info("reAssignUrgentRepairSceneTaskOrderAction() 重派任务");
		log.info("参数：[WOID = "+WOID+",workmanageTaskorder = "+workmanageTaskorder+"]");
		// 参数转换
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String actor = (String) SessionService.getInstance().getValueByKey(
				"userId");
		boolean falg = urgentRepairWorkOrderService.txReAssignUrgentRepairSceneTaskOrder(actor, WOID, workmanageTaskorder.getToId(), workmanageTaskorder);
		if(!falg){
			log.error("reAssignUrgentRepairSceneTaskOrderAction 调用 urgentRepairWorkOrderService.txReAssignUrgentRepairSceneTaskOrder 失败");
			try {
				log.info("退出 reAssignUrgentRepairSceneTaskOrderAction()");
				response.getWriter().write("error");
			} catch (Exception e) {
				log.error("执行 createUrgentRepairWorkOrderAction() 失败");
			}
		}else{
			try {
				log.info("退出 reAssignUrgentRepairSceneTaskOrderAction()");
				response.getWriter().write("success");
			} catch (Exception e) {
				log.error("执行 reAssignUrgentRepairSceneTaskOrderAction() 失败");
			}
		}
	}
	
	/**
	 * 撤销任务
	 * */
	public void cancelUrgentRepairSceneTaskOrderAction() {
		log.info("进入 cancelUrgentRepairSceneTaskOrderAction()");
		log.info("cancelUrgentRepairSceneTaskOrderAction() 撤销任务");
		log.info("参数：[WOID = "+WOID+",workmanageTaskorder = "+workmanageTaskorder+"]");
		// 参数转换
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String actor = (String) SessionService.getInstance().getValueByKey(
				"userId");
		boolean falg = urgentRepairWorkOrderService.txCancelUrgentRepairSceneTaskOrder(actor, WOID,workmanageTaskorder.getToId(), workmanageTaskorder);
		if(!falg){
			log.error("cancelUrgentRepairSceneTaskOrderAction 调用 urgentRepairWorkOrderService.txCancelUrgentRepairSceneTaskOrder 失败");
			try {
				log.info("退出 cancelUrgentRepairSceneTaskOrderAction()");
				response.getWriter().write("error");
			} catch (Exception e) {
				log.error("执行 cancelUrgentRepairSceneTaskOrderAction() 失败");
			}
		}else{
			try {
				log.info("退出 cancelUrgentRepairSceneTaskOrderAction()");
				response.getWriter().write("success");
			} catch (Exception e) {
				log.error("执行 cancelUrgentRepairSceneTaskOrderAction() 失败");
			}
		}
	}

	/**
	 * 催办任务
	 * */
	public void hastenUrgentRepairSceneTaskOrderAction() {
		log.info("进入 hastenUrgentRepairSceneTaskOrderAction()");
		log.info("hastenUrgentRepairSceneTaskOrderAction() 催办任务");
		log.info("参数：[WOID = "+WOID+",workmanageTaskorder = "+workmanageTaskorder+"]");
		// 参数转换
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String actor = (String) SessionService.getInstance().getValueByKey(
				"userId");
		boolean falg = urgentRepairWorkOrderService.txHastenUrgentRepairSceneTaskOrder(actor, WOID, workmanageTaskorder.getToId(), workmanageTaskorder);
		if(!falg){
			log.error("hastenUrgentRepairSceneTaskOrderAction 调用 urgentRepairWorkOrderService.txHastenUrgentRepairSceneTaskOrder 失败");
			try {
				log.info("退出 hastenUrgentRepairSceneTaskOrderAction()");
				response.getWriter().write("error");
			} catch (Exception e) {
				log.error("执行 hastenUrgentRepairSceneTaskOrderAction() 失败");
			}
		}else{
			try {
				log.info("退出 hastenUrgentRepairSceneTaskOrderAction()");
				response.getWriter().write("success");
			} catch (Exception e) {
				log.error("执行 hastenUrgentRepairSceneTaskOrderAction() 失败");
			}
		}
	}

	/**
	 * 派发(创建)现场任务工单
	 * */
	public void createUrgentRepairSenceTaskOrderAction() {
		log.info("进入 createUrgentRepairSenceTaskOrderAction()");
		log.info("createUrgentRepairSenceTaskOrderAction() 派发(创建)现场任务工单");
		log.info("参数：[WOID = "+WOID+",workmanageTaskorder = "+workmanageTaskorder+",recipient = "+recipient+"]");
		//参数转换
		String result = "success";
		String sendor = (String) SessionService.getInstance().getValueByKey(
		"userId");
		if(recipient==null||"".equals(recipient)){
			result = "error";
		}
		String[] temp = recipient.split("-");
		urgentRepairWorkOrderService.txCreateUrgentRepairSceneTaskOrder(sendor,temp[1],WOID,workmanageTaskorder);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			log.info("退出 createUrgentRepairSenceTaskOrderAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 createUrgentRepairSenceTaskOrderAction() 失败");
		}
	}


	/**
	 * 最终回复抢修工单
	 * */
	public void finalReplyUrgentRepairWorkOrderAction() {
		log.info("进入 createUrgentRepairWorkOrderAction()");
		log.info("createUrgentRepairWorkOrderAction() 最终回复抢修工单");
		log.info("参数：[WOID = "+WOID+",urgentrepairWorkorder = "+urgentrepairWorkorder+"]");
		//参数转换
		String reply = (String) SessionService.getInstance().getValueByKey(
		"userId");
		String flag = urgentRepairWorkOrderService.txFinalReplyUrgentRepairWorkOrder(reply, WOID, urgentrepairWorkorder);
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			log.info("退出 createUrgentRepairWorkOrderAction()");
			response.getWriter().write(flag);
		} catch (Exception e) {
			log.error("执行 createUrgentRepairWorkOrderAction() 失败");
		}
	}
	
	
	
	/**
	 * 工单阶段回复
	 * @return
	 */
	public void stepReplyUrgentRepairWorkOrderAction(){
		log.info("进入 stepReplyUrgentRepairWorkOrderAction()");
		log.info("stepReplyUrgentRepairWorkOrderAction() 工单阶段回复");
		log.info("参数：[WOID = "+WOID+",stepReply_desc = "+stepReply_desc+",stepReply_processingProgress = "+stepReply_processingProgress+",stepReply_faultType = "+stepReply_faultType+",stepReply_faultResult = "+stepReply_faultResult+",customerWoType = "+customerWoType+"]");
		String actor = (String) SessionService.getInstance().getValueByKey(
		"userId");
		
		String flag = urgentRepairWorkOrderService.txStepReplyUrgentRepairWorkOrder(actor, WOID, stepReply_desc, stepReply_processingProgress, stepReply_faultType, stepReply_faultResult,customerWoType);
		
		
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			log.info("退出 stepReplyUrgentRepairWorkOrderAction()");
			response.getWriter().write(flag);
		} catch (Exception e) {
			log.error("执行 stepReplyUrgentRepairWorkOrderAction() 失败");
		}
	}
	
	/**
	 * 生成转工单组织树
	 */
	public void createToSendWorkOrderOrgTreeAction(){
		log.info("进入 createToSendWorkOrderOrgTreeAction()");
		log.info("createToSendWorkOrderOrgTreeAction() 生成转工单组织树");
		log.info("参数：[actor = "+actor+"]");
		
		if(actor==null||"".equals(actor)){
			log.error("参数 actor 为空。");
			throw new UserDefinedException("参数 actor 为空。");
		}
		
		List<Map<String, Object>> providerOrganizationList = new ArrayList<Map<String, Object>>();
		//List<ProviderOrganization> pos = providerOrganizationService.getOrgFromOrgTreeByAccountAndOrgType(actor, OrganizationConstant.ORGANIZATION_BUSINESSDIVISION);
		List<SysOrg> pos = this.sysOrganizationService.getOrgByAccountAndOrgType(actor, OrganizationConstant.ORGANIZATION_BUSINESSDIVISION);
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
			log.info("退出 createToSendWorkOrderOrgTreeAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 createToSendWorkOrderOrgTreeAction() 失败");
		}
	}
	
	
	
	/**
	 * 加载工单页面
	 * @return
	 */
	public String loadUrgentRepairWorkOrderPageAction(){
		log.info("进入 loadUrgentRepairWorkOrderPageAction()");
		log.info("loadUrgentRepairWorkOrderPageAction() 加载工单页面");
		log.info("参数：[WOID = "+WOID+"]");
		String actor = (String) SessionService.getInstance().getValueByKey(
		"userId");
		if(WOID==null||"".equals(WOID)){
			log.error("参数 WOID 为空。");
			throw new UserDefinedException("找不到该工单。");
		}
		//工单信息
		com_workOrderInfo = workManageService.getWorkOrderForShow(WOID);
		if(com_workOrderInfo!=null){
			String taskName = "";
			if(com_workOrderInfo.get("woTitle")!=null){
				taskName = com_workOrderInfo.get("woTitle");
			}
			com_workOrderInfo.put("taskName", "现场处理任务："+taskName);
			
			String createTime = "";
			if(com_workOrderInfo.get("createTime")!=null){
				createTime =  TimeFormatHelper.getTimeFormatBySecond(com_workOrderInfo.get("createTime"));
			}
			com_workOrderInfo.put("createTime",createTime);
			
			String finalCompleteTime = "";
			if(com_workOrderInfo.get("finalCompleteTime")!=null){
				finalCompleteTime = TimeFormatHelper.getTimeFormatBySecond(com_workOrderInfo.get("finalCompleteTime"));
			}
			com_workOrderInfo.put("finalCompleteTime",finalCompleteTime );
			UrgentrepairWorkorder urgentrepairWorkorder = urgentRepairWorkOrderService.getUrgentRepairWorkOrder(WOID);
			if(urgentrepairWorkorder!=null){
				List<Workorderassnetresource> waList = workOrderAssnetResourceDao.getWorkOrderAssnetResourceRecordDao("woId", WOID);
				if(waList!=null&&!waList.isEmpty()){
					Workorderassnetresource wa = waList.get(0);
					com_workOrderInfo.put("stationId", wa.getStationId()+"");
					com_workOrderInfo.put("networkResourceType", wa.getNetworkResourceType());
					com_workOrderInfo.put("networkResourceId", wa.getNetworkResourceId()+"");
				}else{
					log.debug("woid="+WOID+"的 工单关联资源记录 为空，调用workOrderAssnetResourceDao.getWorkOrderAssnetResourceRecordDao");
				}
				
				//截取工单所属城市
				String faultArea ="";
				if(urgentrepairWorkorder.getFaultArea()!=null){
					faultArea = urgentrepairWorkorder.getFaultArea()+"";
					String[] areas = faultArea.split("-");
					com_workOrderInfo.put("city", areas[1]);
				}
				
				String faultCause ="";
				if(urgentrepairWorkorder.getFaultCause()!=null){
					faultCause = urgentrepairWorkorder.getFaultCause();
				}
				
				com_workOrderInfo.put("faultCause", faultCause);
				
				String customerWoType ="";
				if(urgentrepairWorkorder.getCustomerWoType()!=null){
					customerWoType = urgentrepairWorkorder.getCustomerWoType();
				}
				com_workOrderInfo.put("customerWoType", customerWoType);
				
				String faultGenera ="";
				if(urgentrepairWorkorder.getFaultGenera()!=null){
					faultGenera = urgentrepairWorkorder.getFaultGenera();
				}
				com_workOrderInfo.put("faultGenera", faultGenera);
				
				String acceptProfessional ="";
				if(urgentrepairWorkorder.getAcceptProfessional()!=null){
					acceptProfessional = urgentrepairWorkorder.getAcceptProfessional();
				}
				com_workOrderInfo.put("acceptProfessional", acceptProfessional);
				
				//工单受理时间
				com_workOrderInfo.put("workOrderAcceptedTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getWorkOrderAcceptedTime()));
				
				//截止时间
				com_workOrderInfo.put("latestAllowedTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getLatestAllowedTime()));
				
				String howToDeal ="";
				if(urgentrepairWorkorder.getHowToDeal()!=null){
					howToDeal = urgentrepairWorkorder.getHowToDeal();
				}
				com_workOrderInfo.put("howToDeal", howToDeal);
				
				String resonForDelayApply ="";
				if(urgentrepairWorkorder.getResonForDelayApply()!=null){
					resonForDelayApply = urgentrepairWorkorder.getResonForDelayApply();
				}
				com_workOrderInfo.put("resonForDelayApply", resonForDelayApply);
				
				String workOrderAcceptedComment ="";
				if(urgentrepairWorkorder.getWorkOrderAcceptedComment()!=null){
					workOrderAcceptedComment = urgentrepairWorkorder.getWorkOrderAcceptedComment();
				}
				com_workOrderInfo.put("workOrderAcceptedComment", workOrderAcceptedComment);
				//告警清除时间
				com_workOrderInfo.put("alarmClearTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getAlarmClearTime()));
				//故障清除时间
				com_workOrderInfo.put("faultSolveTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getFaultSolveTime()));
				
				String isAlarmClear ="";
				if(urgentrepairWorkorder.getIsAlarmClear()!=null){
					isAlarmClear = urgentrepairWorkorder.getIsAlarmClear()+"";
				}
				com_workOrderInfo.put("isAlarmClear", isAlarmClear);
				
				String faultDealResult ="";
				if(urgentrepairWorkorder.getFaultDealResult()!=null){
					faultDealResult = urgentrepairWorkorder.getFaultDealResult()+"";
				}
				com_workOrderInfo.put("faultDealResult", faultDealResult);
				
				String sideeffectService ="";
				if(urgentrepairWorkorder.getSideeffectService()!=null){
					sideeffectService = urgentrepairWorkorder.getSideeffectService()+"";
				}
				
				com_workOrderInfo.put("sideeffectService", sideeffectService);
				
				String affectedServiceName ="";
				if(urgentrepairWorkorder.getAffectedServiceName()!=null){
					affectedServiceName = urgentrepairWorkorder.getAffectedServiceName()+"";
				}
				
				com_workOrderInfo.put("affectedServiceName", affectedServiceName);
				
				String faultDescription ="";
				if(urgentrepairWorkorder.getFaultDescription()!=null){
					faultDescription = urgentrepairWorkorder.getFaultDescription()+"";
				}
				
				com_workOrderInfo.put("faultDescription", faultDescription);
				
				//预计解决时间
				com_workOrderInfo.put("foreseeResolveTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getForeseeResolveTime()));
				//判断当前操作者权限
				if(com_workOrderInfo.get("currentHandler")!=null&&!"".equals(com_workOrderInfo.get("currentHandler"))){
					if(actor.trim().equals(com_workOrderInfo.get("currentHandler").trim())){
						com_workOrderInfo.put("hasPower", "1");//拥有权限
						//更新消息盒子
						bizMessageService.updateBizMsgToHasReadByReceivePersonAndOrderIdService(actor,WOID); 
					}else{
						com_workOrderInfo.put("hasPower", "0");//没有权限
					}
				}else{
					com_workOrderInfo.put("hasPower", "0");//没有权限
				}
				//获取派发人所属最高组织
				//List<ProviderOrganization> pos = providerOrganizationService.getTopLevelOrgByAccount(actor);
				//yuan.yw
				List<SysOrg> pos = this.sysOrganizationService.getTopLevelOrgByAccount(actor);
				if(pos!=null&&!pos.isEmpty()){
					com_workOrderInfo.put("ORGID", pos.get(0).getOrgId()+"");
				}
				
				//工单最终回复预填
				List<Map> childTaskOrderList =  workManageService.getChildTaskOrderListByWoId(WOID);
				if(childTaskOrderList!=null&&!childTaskOrderList.isEmpty()){
					Map childTaskOrder = childTaskOrderList.get(0);
					//第一个子任务单号
					String subToId = childTaskOrder.get("toId")+"";
					UrgentrepairSencetaskorder urgentrepairSencetaskorder =  urgentRepairSenceTaskOrderService.getUrgentRepairSenceTaskOrder(subToId);
					if(urgentrepairSencetaskorder!=null){
						scene_taskOrderInfo = new HashMap<String,String>();
						String t_affectedServiceName ="";
						if(urgentrepairSencetaskorder.getAffectedServiceName()!=null){
							t_affectedServiceName = urgentrepairSencetaskorder.getAffectedServiceName();
						}
						scene_taskOrderInfo.put("affectedServiceName", t_affectedServiceName);
						String s_faultGenera ="";
						if(urgentrepairSencetaskorder.getFaultGenera()!=null){
							s_faultGenera = urgentrepairSencetaskorder.getFaultGenera();
						}
						scene_taskOrderInfo.put("faultGenera", s_faultGenera);
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
						String s_isAlarmClear ="";
						if(urgentrepairSencetaskorder.getIsAlarmClear()!=null){
							s_isAlarmClear = urgentrepairSencetaskorder.getIsAlarmClear()+"";
						}
						scene_taskOrderInfo.put("isAlarmClear", s_isAlarmClear);
						String t_sideeffectService ="";
						if(urgentrepairSencetaskorder.getSideeffectService()!=null){
							t_sideeffectService = urgentrepairSencetaskorder.getSideeffectService()+"";
						}
						scene_taskOrderInfo.put("sideeffectService", t_sideeffectService);
					}
				}
				//查看是否派发过现场任务或专家任务
				List<Map> tos =  workManageService.getTaskOrderListByWoId(WOID);
				if(tos!=null&&!tos.isEmpty()){
					com_workOrderInfo.put("hasSceneTask", "has");
					String result = "not";
					for(Map to : tos){
						if(to.get("bizProcessName")!=null && to.get("bizProcessName").equals("专家任务单")){
							result = "has";
						}
					}
					com_workOrderInfo.put("hasTechTask", result);
				}else{
					com_workOrderInfo.put("hasSceneTask", "not");
					com_workOrderInfo.put("hasTechTask", "not");
				}
			}else{
				throw new UserDefinedException("找不到该工单。");
			}
		}else{
			throw new UserDefinedException("找不到该工单。");
		}
		log.info("退出 loadUrgentRepairWorkOrderPageAction()");
		return "success";
	}
	
	/**
	 * 加载抢修工单页面(集中调度)
	 * @return
	 */
	public String loadUrgentRepairWorkOrderPageForGISAction(){
		log.info("进入 loadUrgentRepairWorkOrderPageForGISAction()");
		log.info("loadUrgentRepairWorkOrderPageForGISAction() 加载抢修工单页面(集中调度)");
		log.info("参数：[WOID = "+WOID+"]");
		
		if(WOID==null||"".equals(WOID)){
			log.error("参数 WOID 为空。");
			throw new UserDefinedException("参数 WOID 为空。");
		}
		
		String actor = (String) SessionService.getInstance().getValueByKey(
		"userId");
		//工单信息
		com_workOrderInfo = workManageService.getWorkOrderForShow(WOID);
		if(com_workOrderInfo!=null){
			String taskName = "";
			if(com_workOrderInfo.get("woTitle")!=null){
				taskName = com_workOrderInfo.get("woTitle");
			}
			com_workOrderInfo.put("taskName", "现场处理任务："+taskName);
			
			String createTime = "";
			if(com_workOrderInfo.get("createTime")!=null){
				createTime =  TimeFormatHelper.getTimeFormatBySecond(com_workOrderInfo.get("createTime"));
			}
			com_workOrderInfo.put("createTime",createTime);
			
			String finalCompleteTime = "";
			if(com_workOrderInfo.get("finalCompleteTime")!=null){
				finalCompleteTime = TimeFormatHelper.getTimeFormatBySecond(com_workOrderInfo.get("finalCompleteTime"));
			}
			com_workOrderInfo.put("finalCompleteTime",finalCompleteTime );
			com_workOrderInfo.put("accountForGIS", accountForGIS);
			com_workOrderInfo.put("entryType", entryType);
			UrgentrepairWorkorder urgentrepairWorkorder = urgentRepairWorkOrderService.getUrgentRepairWorkOrder(WOID);
			if(urgentrepairWorkorder!=null){
				String faultCause ="";
				if(urgentrepairWorkorder.getFaultCause()!=null){
					faultCause = urgentrepairWorkorder.getFaultCause();
				}
				com_workOrderInfo.put("faultCause", faultCause);
				
				String faultGenera ="";
				if(urgentrepairWorkorder.getFaultGenera()!=null){
					faultGenera = urgentrepairWorkorder.getFaultGenera();
				}
				com_workOrderInfo.put("faultGenera", faultGenera);
				
				String acceptProfessional ="";
				if(urgentrepairWorkorder.getAcceptProfessional()!=null){
					acceptProfessional = urgentrepairWorkorder.getAcceptProfessional();
				}
				com_workOrderInfo.put("acceptProfessional", acceptProfessional);
				
				//工单受理时间
				com_workOrderInfo.put("workOrderAcceptedTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getWorkOrderAcceptedTime()));
				
				//截止时间
				com_workOrderInfo.put("latestAllowedTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getLatestAllowedTime()));
				
				String howToDeal ="";
				if(urgentrepairWorkorder.getHowToDeal()!=null){
					howToDeal = urgentrepairWorkorder.getHowToDeal();
				}
				com_workOrderInfo.put("howToDeal", howToDeal);
				
				String resonForDelayApply ="";
				if(urgentrepairWorkorder.getResonForDelayApply()!=null){
					resonForDelayApply = urgentrepairWorkorder.getResonForDelayApply();
				}
				com_workOrderInfo.put("resonForDelayApply", resonForDelayApply);
				
				String workOrderAcceptedComment ="";
				if(urgentrepairWorkorder.getWorkOrderAcceptedComment()!=null){
					workOrderAcceptedComment = urgentrepairWorkorder.getWorkOrderAcceptedComment();
				}
				com_workOrderInfo.put("workOrderAcceptedComment", workOrderAcceptedComment);
				//告警清除时间
				com_workOrderInfo.put("alarmClearTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getAlarmClearTime()));
				//故障清除时间
				com_workOrderInfo.put("faultSolveTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getFaultSolveTime()));
				
				String isAlarmClear ="";
				if(urgentrepairWorkorder.getIsAlarmClear()!=null){
					isAlarmClear = urgentrepairWorkorder.getIsAlarmClear()+"";
				}
				com_workOrderInfo.put("isAlarmClear", isAlarmClear);
				
				String faultDealResult ="";
				if(urgentrepairWorkorder.getFaultDealResult()!=null){
					faultDealResult = urgentrepairWorkorder.getFaultDealResult()+"";
				}
				com_workOrderInfo.put("faultDealResult", faultDealResult);
				
				String sideeffectService ="";
				if(urgentrepairWorkorder.getSideeffectService()!=null){
					sideeffectService = urgentrepairWorkorder.getSideeffectService()+"";
				}
				
				com_workOrderInfo.put("sideeffectService", sideeffectService);
				
				String faultDescription ="";
				if(urgentrepairWorkorder.getFaultDescription()!=null){
					faultDescription = urgentrepairWorkorder.getFaultDescription()+"";
				}
				
				com_workOrderInfo.put("faultDescription", faultDescription);
				
				String affectedServiceName ="";
				if(urgentrepairWorkorder.getAffectedServiceName()!=null){
					affectedServiceName = urgentrepairWorkorder.getAffectedServiceName()+"";
				}
				
				com_workOrderInfo.put("affectedServiceName", affectedServiceName);
				
				//预计解决时间
				com_workOrderInfo.put("foreseeResolveTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getForeseeResolveTime()));
				//判断当前操作者权限
				if(actor.trim().equals(com_workOrderInfo.get("currentHandler").trim())){
					com_workOrderInfo.put("hasPower", "1");//拥有权限
				}else{
					com_workOrderInfo.put("hasPower", "0");//没有权限
				}
				
			}
		}
		//获取派发人所属最高组织
		//List<ProviderOrganization> pos = providerOrganizationService.getTopLevelOrgByAccount(actor);
		//yuan.yw
		List<SysOrg> pos = this.sysOrganizationService.getTopLevelOrgByAccount(actor);
		if(pos!=null&&!pos.isEmpty()){
			com_workOrderInfo.put("ORGID", pos.get(0).getOrgId()+"");
		}
		
		log.info("退出 loadUrgentRepairWorkOrderPageForGISAction()");
		return "success";
	}
	
	/**
	 * 进入创建抢修工单页面（集中调度）
	 * @return
	 */
	public String loadCreateUrgentRepairWorkOrderPageForGISAction(){
		log.info("进入 loadCreateUrgentRepairWorkOrderPageForGISAction()");
		log.info("loadCreateUrgentRepairWorkOrderPageForGISAction() 进入创建抢修工单页面（集中调度）");
		log.info("参数：[baseStationId = "+baseStationId+",baseStationType = "+baseStationType+"]");
		
		if(baseStationId==null||"".equals(baseStationId)){
			log.error("参数 baseStationId 为空。");
			throw new UserDefinedException("参数 baseStationId 为空。");
		}
		
		if(baseStationType==null||"".equals(baseStationType)){
			log.error("参数 baseStationType 为空。");
			throw new UserDefinedException("参数 baseStationType 为空。");
		}
		
		Map<String,String> resourceMap = networkResourceService.getResourceByReIdAndReTypeService(baseStationId, baseStationType);
		if(resourceMap!=null){
			stationOfGIS = new HashMap();
			stationOfGIS.put("gis", "gis");
			if(resourceMap.get("name")!=null){
				stationOfGIS.put("gis_name", resourceMap.get("name"));
			}
			stationOfGIS.put("gis_id", baseStationId);
			stationOfGIS.put("gis_type", baseStationType);
			
		}
		log.info("退出 loadCreateUrgentRepairWorkOrderPageForGISAction()");
		return "success";
	}
	
	/**
	 * 进入创建抢修工单页面（快速创单）
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String loadCreateUrgentRepairWorkOrderPageForQuickAction() throws UnsupportedEncodingException{
		log.info("进入 loadCreateUrgentRepairWorkOrderPageForQuickAction()");
		log.info("loadCreateUrgentRepairWorkOrderPageForQuickAction() 创建抢修工单(包括客户工单)");
		log.info("参数：[content = "+content+"]");
		if(content!=null){
			content = content.replaceAll("\"", "\'");
		}
		log.info("退出 loadCreateUrgentRepairWorkOrderPageForQuickAction()");
		return "success";
	}
	
	/**
	 * 加载工单流转过程
	 * @return
	 */
	public String loadWorkOrderProcedureAction(){
		log.info("进入 loadWorkOrderProcedureAction()");
		log.info("loadWorkOrderProcedureAction() 加载工单流转过程");
		log.info("参数：[WOID = "+WOID+"]");
		
		if(WOID==null||"".equals(WOID)){
			log.error("参数 WOID 为空。");
			throw new UserDefinedException("加载工单流转过程 获取参数 WOID 为空。");
		}
		
		workOrderInfo = workManageService.getWorkOrderForShow(WOID);
		if(workOrderInfo!=null&&!workOrderInfo.isEmpty()){
			String finalCompleteTime = "";
			if(workOrderInfo.get("finalCompleteTime")!=null){
				finalCompleteTime = TimeFormatHelper.getTimeFormatBySecond(workOrderInfo.get("finalCompleteTime"));
			}
			workOrderInfo.put("finalCompleteTime",finalCompleteTime);
			UrgentrepairWorkorder urgentrepairWorkorder = urgentRepairWorkOrderService.getUrgentRepairWorkOrder(WOID);
			if(urgentrepairWorkorder!=null){
				String faultCause ="";
				if(urgentrepairWorkorder.getFaultCause()!=null){
					faultCause = urgentrepairWorkorder.getFaultCause();
				}
				workOrderInfo.put("faultCause", faultCause);
				
				String faultGenera ="";
				if(urgentrepairWorkorder.getFaultGenera()!=null){
					faultGenera = urgentrepairWorkorder.getFaultGenera();
				}
				workOrderInfo.put("faultGenera", faultGenera);
				//工单受理时间
				workOrderInfo.put("workOrderAcceptedTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getWorkOrderAcceptedTime()));
				
				String howToDeal ="";
				if(urgentrepairWorkorder.getHowToDeal()!=null){
					howToDeal = urgentrepairWorkorder.getHowToDeal();
				}
				workOrderInfo.put("howToDeal", howToDeal);
				
				String resonForDelayApply ="";
				if(urgentrepairWorkorder.getResonForDelayApply()!=null){
					resonForDelayApply = urgentrepairWorkorder.getResonForDelayApply();
				}
				workOrderInfo.put("resonForDelayApply", resonForDelayApply);
				
				String faultDealResult ="";
				if(urgentrepairWorkorder.getFaultDealResult()!=null){
					faultDealResult = urgentrepairWorkorder.getFaultDealResult();
				}
				workOrderInfo.put("faultDealResult", faultDealResult);
				
				String affectedServiceName ="";
				if(urgentrepairWorkorder.getAffectedServiceName()!=null){
					affectedServiceName = urgentrepairWorkorder.getAffectedServiceName();
				}
				workOrderInfo.put("affectedServiceName", affectedServiceName);
				
				String sideeffectService ="";
				if(urgentrepairWorkorder.getSideeffectService()!=null){
					sideeffectService = urgentrepairWorkorder.getSideeffectService()+"";
				}
				workOrderInfo.put("sideeffectService", sideeffectService);
				
				String workOrderAcceptedComment ="";
				if(urgentrepairWorkorder.getWorkOrderAcceptedComment()!=null){
					workOrderAcceptedComment = urgentrepairWorkorder.getWorkOrderAcceptedComment();
				}
				workOrderInfo.put("workOrderAcceptedComment", workOrderAcceptedComment);
				//告警清除时间
				workOrderInfo.put("alarmClearTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getAlarmClearTime()));
				//故障解决时间
				workOrderInfo.put("faultSolveTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getFaultSolveTime()));
				String isAlarmClear ="";
				if(urgentrepairWorkorder.getIsAlarmClear()!=null){
					isAlarmClear = urgentrepairWorkorder.getIsAlarmClear()+"";
				}
				workOrderInfo.put("isAlarmClear", isAlarmClear);
				//预计解决时间
				workOrderInfo.put("foreseeResolveTime", TimeFormatHelper.getTimeFormatBySecond(urgentrepairWorkorder.getForeseeResolveTime()));
				//预计解决时间
			}
		}
		log.info("退出 loadWorkOrderProcedureAction()");
		return "success";
	}
	
	/**
	 * 获取工单下级任务单
	 * @return
	 */
	public void getWorkOrderChildrenTaskOrderAction(){
		log.info("进入 getWorkOrderChildrenTaskOrderAction()");
		log.info("getWorkOrderChildrenTaskOrderAction() 获取工单下级任务单");
		log.info("参数：[WOID = "+WOID+"]");
		
		if(WOID==null||"".equals(WOID)){
			log.error("参数 WOID 为空。");
			throw new UserDefinedException("获取工单下级任务单 获取参数 WOID 为空。");
		}
		
		List<Map> taskorderMap =  workManageService.getChildTaskOrderListByWoId(WOID);
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(taskorderMap, new TypeToken<List<Map>>(){}.getType());		
		try {
			log.info("退出 getWorkOrderChildrenTaskOrderAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getWorkOrderChildrenTaskOrderAction() 失败");
		}
	}
	
	/**
	 * 获取维护队长
	 */
	public void  loadTeamLeadersInfoAction(){
		log.info("进入 loadTeamLeadersInfoAction()");
		log.info("loadTeamLeadersInfoAction() 获取维护队长");
		log.info("参数：[ORGID = "+ORGID+"]");
		
		if(ORGID==null||"".equals(ORGID)){
			log.error("参数 ORGID 为空。");
			throw new UserDefinedException("获取维护队长 获取参数 ORGID 为空。");
		}
		
		List<Map> teamLeaderLists = urgentRepairWorkOrderService.loadTeamLeadersInfoService(ORGID);
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(teamLeaderLists, new TypeToken<List<Map>>(){}.getType());		
		try {
			log.info("退出 loadTeamLeadersInfoAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 loadTeamLeadersInfoAction() 失败");
		}
	}
	
	
	
	public String getWOID() {
		return WOID;
	}

	public void setWOID(String woid) {
		WOID = woid;
	}

	public UrgentrepairWorkorder getUrgentrepairWorkorder() {
		return urgentrepairWorkorder;
	}

	public void setUrgentrepairWorkorder(UrgentrepairWorkorder urgentrepairWorkorder) {
		this.urgentrepairWorkorder = urgentrepairWorkorder;
	}

	public UrgentrepairCustomerworkorder getUrgentrepairCustomerworkorder() {
		return urgentrepairCustomerworkorder;
	}

	public void setUrgentrepairCustomerworkorder(
			UrgentrepairCustomerworkorder urgentrepairCustomerworkorder) {
		this.urgentrepairCustomerworkorder = urgentrepairCustomerworkorder;
	}

	public UrgentrepairSencetaskorder getUrgentrepairSencetaskorder() {
		return urgentrepairSencetaskorder;
	}

	public void setUrgentrepairSencetaskorder(
			UrgentrepairSencetaskorder urgentrepairSencetaskorder) {
		this.urgentrepairSencetaskorder = urgentrepairSencetaskorder;
	}

	public UrgentRepairWorkOrderService getUrgentRepairWorkOrderService() {
		return urgentRepairWorkOrderService;
	}

	public void setUrgentRepairWorkOrderService(
			UrgentRepairWorkOrderService urgentRepairWorkOrderService) {
		this.urgentRepairWorkOrderService = urgentRepairWorkOrderService;
	}

	public String getWoTemplateId() {
		return woTemplateId;
	}

	public void setWoTemplateId(String woTemplateId) {
		this.woTemplateId = woTemplateId;
	}

	public long getProviderOrgId() {
		return providerOrgId;
	}

	public void setProviderOrgId(long providerOrgId) {
		this.providerOrgId = providerOrgId;
	}

	public String getToTemplateId() {
		return toTemplateId;
	}

	public void setToTemplateId(String toTemplateId) {
		this.toTemplateId = toTemplateId;
	}

	public Workorderassnetresource getWorkorderassnetresource() {
		return workorderassnetresource;
	}

	public void setWorkorderassnetresource(
			Workorderassnetresource workorderassnetresource) {
		this.workorderassnetresource = workorderassnetresource;
	}

	public String getORGID() {
		return ORGID;
	}

	public void setORGID(String orgid) {
		ORGID = orgid;
	}

	public Map<String, String> getWorkOrderInfo() {
		return workOrderInfo;
	}

	public void setWorkOrderInfo(Map<String, String> workOrderInfo) {
		this.workOrderInfo = workOrderInfo;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public WorkmanageWorkorder getWorkmanageWorkorder() {
		return workmanageWorkorder;
	}

	public void setWorkmanageWorkorder(WorkmanageWorkorder workmanageWorkorder) {
		this.workmanageWorkorder = workmanageWorkorder;
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

	public Tasktracerecord getTasktracerecord() {
		return tasktracerecord;
	}

	public void setTasktracerecord(Tasktracerecord tasktracerecord) {
		this.tasktracerecord = tasktracerecord;
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



	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public Map<String, String> getCom_workOrderInfo() {
		return com_workOrderInfo;
	}

	public void setCom_workOrderInfo(Map<String, String> com_workOrderInfo) {
		this.com_workOrderInfo = com_workOrderInfo;
	}

	public Map<String, String> getScene_taskOrderInfo() {
		return scene_taskOrderInfo;
	}

	public void setScene_taskOrderInfo(Map<String, String> scene_taskOrderInfo) {
		this.scene_taskOrderInfo = scene_taskOrderInfo;
	}

	public UrgentRepairSenceTaskOrderService getUrgentRepairSenceTaskOrderService() {
		return urgentRepairSenceTaskOrderService;
	}

	public void setUrgentRepairSenceTaskOrderService(
			UrgentRepairSenceTaskOrderService urgentRepairSenceTaskOrderService) {
		this.urgentRepairSenceTaskOrderService = urgentRepairSenceTaskOrderService;
	}

	public String getAccountForGIS() {
		return accountForGIS;
	}

	public void setAccountForGIS(String accountForGIS) {
		this.accountForGIS = accountForGIS;
	}

	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}
	
	public NetworkResourceInterfaceService getNetworkResourceService() {
		return networkResourceService;
	}

	public void setNetworkResourceService(
			NetworkResourceInterfaceService networkResourceService) {
		this.networkResourceService = networkResourceService;
	}

	public Map<String, String> getStationOfGIS() {
		return stationOfGIS;
	}

	public void setStationOfGIS(Map<String, String> stationOfGIS) {
		this.stationOfGIS = stationOfGIS;
	}

	public String getBaseStationId() {
		return baseStationId;
	}

	public void setBaseStationId(String baseStationId) {
		this.baseStationId = baseStationId;
	}

	public String getBaseStationType() {
		return baseStationType;
	}

	public void setBaseStationType(String baseStationType) {
		this.baseStationType = baseStationType;
	}

	public String getMoudleId() {
		return moudleId;
	}

	public void setMoudleId(String moudleId) {
		this.moudleId = moudleId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public WorkOrderAssnetResourceDao getWorkOrderAssnetResourceDao() {
		return workOrderAssnetResourceDao;
	}

	public void setWorkOrderAssnetResourceDao(
			WorkOrderAssnetResourceDao workOrderAssnetResourceDao) {
		this.workOrderAssnetResourceDao = workOrderAssnetResourceDao;
	}

	
	
	
}
