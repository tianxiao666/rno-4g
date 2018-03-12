package com.iscreate.op.service.urgentrepair;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.BizAuthorityConstant;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.urgentrepair.UrgentRepairSenceTaskOrderDao;
import com.iscreate.op.dao.urgentrepair.UrgentRepairTechSupportTaskOrderDao;
import com.iscreate.op.dao.urgentrepair.UrgentRepairWorkOrderDao;
import com.iscreate.op.pojo.bizmsg.BizMessage;
import com.iscreate.op.pojo.publicinterface.Tasktracerecord;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairSencetaskorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairTechsupporttaskorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairWorkorder;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.pojo.workorderinterface.wangyou.WorkorderinterfaceWangyouWorkorderRelation;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.op.service.publicinterface.CommonService;
import com.iscreate.op.service.publicinterface.TaskOrderCommonService;
import com.iscreate.op.service.publicinterface.WorkOrderCommonService;
import com.iscreate.op.service.staffduty.StaffDutyService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.op.service.workorderinterface.wangyou.WorkorderinterfaceWangyouWorkorderRelationService;
import com.iscreate.plat.datadictionary.DataDictionaryService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.tree.TreeNode;

public class UrgentRepairTechSupportTaskOrderServiceImpl implements UrgentRepairTechSupportTaskOrderService{
	private  static final  Log log = LogFactory.getLog(UrgentRepairTechSupportTaskOrderServiceImpl.class);
	private UrgentRepairWorkOrderDao urgentRepairWorkOrderDao;
	private UrgentRepairTechSupportTaskOrderDao urgentRepairTechSupportTaskOrderDao;
	private UrgentRepairSenceTaskOrderDao urgentRepairSenceTaskOrderDao;
	private UrgentRepairSenceTaskOrderService urgentRepairSenceTaskOrderService;
	private WorkManageService workManageService;
	private TaskOrderCommonService taskOrderCommonService;
	private WorkOrderCommonService workOrderCommonService;
	private StaffDutyService staffDutyService;
	private CommonService commonService;
	private DataDictionaryService dataDictionaryService;
	private WorkorderinterfaceWangyouWorkorderRelationService workorderinterfaceWangyouWorkorderRelationService;
	private BizMessageService bizMessageService;
	private String WOID;
	private String TOID;
	private Map<String,String> workOrderInfo;
	private Map<String,String> taskOrderInfo;
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
	/**
	 * 受理专家任务
	 * @param accept
	 * @param toId
	 */
	public boolean txAcceptUrgentRepairTechSupportTaskOrder(String accept,String woId,String toId,WorkmanageTaskorder workmanageTaskorder){
		
		// 判断传入任务单号是否为空
		if (toId == null || "".equals(toId)) {
			throw new UserDefinedException("参数 toId 为空。");
		}
		if (woId == null || "".equals(woId)) {
			throw new UserDefinedException("参数 woId 为空。");
		}
		//获取当前处理人
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(accept);
//		Staff staff = providerOrganizationService.getStaffByAccount(accept);
		if(sysOrgUserByAccount==null){
			throw new UserDefinedException("找不到账号=="+accept+"的员工。");
		}
		//服务跟踪记录
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
		tasktracerecord.setHandler(accept);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("受理专家任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setToId(toId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【受理意见】"+workmanageTaskorder.getAcceptComment());
		boolean flag = false;
		log.info("启动受理专家任务流程。");
		workmanageTaskorder.setAcceptTime(new Date());
		workmanageTaskorder.setToId(toId);
		flag = this.workManageService.acceptTaskOrder(workmanageTaskorder, accept,tasktracerecord);
		return flag;
		
	}
	
	/**
	 * 技术支援任务阶回复
	 * 若技术支援任务单所属工单树网优之家工单，则阶段回复技术支援任务后需要调用网优之家阶段回复接口更新网优之家工单信息
	 * @param actor
	 * @param woId
	 * @param toId
	 * @param stepReply_desc
	 * @param stepReply_processingProgress
	 * @param stepReply_faultType
	 * @param stepReply_faultResult
	 * @return
	 */
	public String txStepReplyUrgentRepairTechSupportTaskOrder(String actor,String woId,String toId,String stepReply_desc,String stepReply_processingProgress,String stepReply_faultType,String stepReply_faultResult,String customerWoType){
		String result = "success";
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(actor);
//		Staff staff = providerOrganizationService.getStaffByAccount(actor);
		
		if(stepReply_faultType!=null&&!"".equals(stepReply_faultType)){
			TreeNode tn1 = dataDictionaryService.getDictionaryByTreeNodeIdService(Long.parseLong(stepReply_faultType));
			if(tn1!=null){
				stepReply_faultType = tn1.getReferenceValue();
			}
		}
		if(stepReply_faultResult!=null&&!"".equals(stepReply_faultResult)){
			TreeNode tn2 = dataDictionaryService.getDictionaryByTreeNodeIdService(Long.parseLong(stepReply_faultResult));
			if(tn2!=null){
				stepReply_faultResult = tn2.getReferenceValue();
			}
		}
		
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		if(sysOrgUserByAccount!=null){
			if(sysOrgUserByAccount.getName()!=null){
				tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
			}
		}
		tasktracerecord.setHandler(actor);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("阶段回复专家任务");
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setToId(toId);
		if(stepReply_desc==null||"".equals(stepReply_desc)){
			stepReply_desc = "无";
		}
		String contentStr = "";
		if(customerWoType!=null){
			if("home2g".equals(customerWoType)||"homeTd".equals(customerWoType)){
				contentStr = "【处理进度】"+stepReply_processingProgress+"；【故障大类】"+stepReply_faultType+"；【故障原因】"+stepReply_faultType+"；【处理意见】"+stepReply_desc;
			}else{
				contentStr = "【处理意见】"+stepReply_desc;
			}
		}else{
			contentStr = "【处理意见】"+stepReply_desc;
		}
		tasktracerecord.setHandleResultDescription(contentStr);
		
		boolean flag = workManageService.stepReply(tasktracerecord);
		
		// 根据工单号获取工单实例
		List<UrgentrepairWorkorder> wos = urgentRepairWorkOrderDao
				.getUrgentrepairWorkorderByWoId(woId);
		UrgentrepairWorkorder wo = new UrgentrepairWorkorder();
		if (wos != null && !wos.isEmpty()) {
			wo = wos.get(0);
		}else{
			throw new UserDefinedException("找不到对应单号=="+woId+"的工单实例。");
		}
		
		if(flag){
			//调用网优之家阶段回复接口，更新网优之家工单信息
			if(wo.getCustomerWoType()!=null){
				if("home2g".equals(wo.getCustomerWoType())||"homeTd".equals(wo.getCustomerWoType())){
					if(!workOrderCommonService.callWebServerStepReplyWorkOrder(woId, wo.getCustomerWoType(), stepReply_processingProgress, stepReply_faultType, stepReply_faultResult)){
						//业务定义，若果调用接口失败，则系统告知用户，需要手动去网优之家进行工单的信息更新
						result = "VPNFail";
					}
				}
			}
		}else{
			result = "fail";
		}
		
		
		return result;
	}
	
	/**
	 * 驳回技术支援任务
	 * 在未受理技术支援任务之前可驳回任务，驳回后该任务单不能编辑，状态变为“待撤销”
	 * @param actor
	 * @param toId
	 */
	public boolean txRejectUrgentRepairTechSupportTaskOrder(String actor,String woId,String toId,WorkmanageTaskorder workmanageTaskorder){
		// 判断传入任务单号是否为空
		if (toId == null || "".equals(toId)) {
			throw new UserDefinedException("参数 toId 为空。");
		}
		if (woId == null || "".equals(woId)) {
			throw new UserDefinedException("参数 woId 为空。");
		}
		//获取当前处理人
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(actor);
//		Staff staff = providerOrganizationService.getStaffByAccount(actor);
		if(sysOrgUserByAccount==null){
			throw new UserDefinedException("找不到账号=="+actor+"的员工。");
		}
		//服务跟踪记录
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
		tasktracerecord.setHandler(actor);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("驳回专家任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setToId(toId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【驳回意见】"+workmanageTaskorder.getRejectComment());
		boolean flag = false;
		log.info("启动驳回专家任务流程。");
		workmanageTaskorder.setRejectTime(new Date());
		workmanageTaskorder.setToId(toId);
		flag = this.workManageService.rejectTaskOrder(workmanageTaskorder, tasktracerecord);
		return flag;
	}
	
	/**
	 * 转派专家任务
	 * 将任务的责任人转移到转派对象身上，转派不涉及工作流，只是更改任务的当前责任人
	 * 转派后会邮件通知转派对象
	 * @param actor
	 * @param woId
	 * @param toId
	 * @param recipient
	 * @param stepReply_desc
	 * @return
	 */
	public boolean txToSendUrgentRepairTechSupportTaskOrder(String actor,String woId,String toId,String recipient,
			String stepReply_desc) {
		// 判断传入任务单号是否为空
		if (toId == null || "".equals(toId)) {
			throw new UserDefinedException("参数 toId 为空。");
		}
		if (woId == null || "".equals(woId)) {
			throw new UserDefinedException("参数 woId 为空。");
		}
		//获取当前处理人
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(actor);
//		Staff staff = providerOrganizationService.getStaffByAccount(actor);
		if(sysOrgUserByAccount==null){
			throw new UserDefinedException("找不到账号=="+actor+"的员工。");
		}
		
		//获取派发对象
		//ou.jh
		SysOrgUser sysOrgUserByAccount2 = this.sysOrgUserService.getSysOrgUserByAccount(recipient);
//		Staff rStaff = providerOrganizationService.getStaffByAccount(recipient);
		if(sysOrgUserByAccount2==null){
			throw new UserDefinedException("找不到账号=="+recipient+"的员工。");
		}
		
		//服务跟踪记录
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
		tasktracerecord.setHandler(actor);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("转派专家任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setToId(toId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【转派原因】"+stepReply_desc);
		boolean flag = false;
		log.info("启动转派专家任务流程。");
		
		//工单当前处理人所属组织ID
		long currentHandlerOrgId = 0;
//		List<ProviderOrganization> currentHandlerOrgs = providerOrganizationService.getOrgByAccountAndRoleCode(recipient, BizAuthorityConstant.ROLE_TECHNICALSPECIALIST);
		//yuan.yw
		List<Map<String,Object>> currentHandlerOrgs = this.sysOrganizationService.getOrgByAccountAndRoleCode(recipient, BizAuthorityConstant.ROLE_TECHNICALSPECIALIST);

		if(currentHandlerOrgs!=null&&currentHandlerOrgs.size()>0){
			currentHandlerOrgId = Long.parseLong(currentHandlerOrgs.get(0).get("orgId")+"");
			if(currentHandlerOrgs.size()>1){
				log.warn("获取账号=="+recipient+"担任专家角色所在组织有多个。currentHandlerOrgs.size()>1");
				
			}
		}else {
			throw new UserDefinedException("找不到账号=="+recipient+"担任任务调度员角色所在组织");
		}
		WorkmanageWorkorder updateWorkorder =  new WorkmanageWorkorder();
		updateWorkorder.setOrderOwner(recipient);
		updateWorkorder.setOrderOwnerOrgId(currentHandlerOrgId);
		WorkmanageTaskorder updateTaskorder =  new WorkmanageTaskorder();
		updateTaskorder.setCurrentHandler(recipient);
		if(sysOrgUserByAccount2.getName()!=null){
			updateTaskorder.setCurrentHandlerName(sysOrgUserByAccount2.getName());
		}else{
			updateTaskorder.setCurrentHandlerName("");
		}
		//转派技术支援任务单
		flag = this.workManageService.handoverTaskOrder(woId, toId,updateWorkorder,updateTaskorder, tasktracerecord);
		if(flag){
			//转派成功后，发送通知邮件给转派对象
			WorkmanageTaskorder workmanageTaskorder =workManageService.getTaskOrderEntity(toId);
			if(workmanageTaskorder!=null){
				Map woTemp = workManageService.getWorkOrderForShow(woId);
				String woTitle = "";
				if(woTemp!=null){
					woTitle = woTemp.get("woTitle").toString();
				}
//				Map toTemp = workManageService.getTaskOrderForShow(toId);
//				String toType = "";
//				if(toTemp!=null&&toTemp.get("bizProcessName")!=null){
//					toType = toTemp.get("bizProcessName").toString();
//				}
				Map emailInfoMap = new HashMap();
				String remaininTime = "";
				if(workmanageTaskorder.getRequireCompleteTime()!=null){
					Date nowDate = new Date();
					long time = workmanageTaskorder.getRequireCompleteTime().getTime() - nowDate.getTime();
					long hours = time/1000/3600;
					long min = time/1000/60%60;
					String hh = hours+"";
					String mm = min+"";
					if(time<0){
						remaininTime = "（已超时）";
					}else{
						if(hh=="0"){
							remaininTime = "（剩余"+mm+"分）";
						}else{
							remaininTime = "（剩余"+hh+"小时"+mm+"分）";
						}
					}
					
					emailInfoMap.put("title", "专家任务单，截止时间："+TimeFormatHelper.getTimeFormatBySecond(workmanageTaskorder.getRequireCompleteTime()).replace("_", " ")+remaininTime);
				}else{
					emailInfoMap.put("title", "专家任务单");
				}
				List<UrgentrepairWorkorder> woList = urgentRepairWorkOrderDao.getUrgentrepairWorkorderByWoId(woId);
//				String part1 = "";
//				if(woList!=null&&!woList.isEmpty()){
//					part1 = woList.get(0).getFaultType()+"/"+woList.get(0).getFaultLevel()+"/"+woList.get(0).getAcceptProfessional();
//					
//				}
				if(sysOrgUserByAccount2.getMobile()!=null&&!"null".equals(sysOrgUserByAccount2.getMobile())&&!"".equals(sysOrgUserByAccount2.getMobile())){
					Map params =new HashMap();
					params.put("iosmWoId", woId);
					List<WorkorderinterfaceWangyouWorkorderRelation> wrs = workorderinterfaceWangyouWorkorderRelationService.getWorkorderinterfaceWangyouWorkorderRelationList(params);
					String eomsWoId = "无";
					if(wrs!=null&&!wrs.isEmpty()){
						eomsWoId = wrs.get(0).getCustomerWoId();
					}
					String content = "【工单标题】"+woTitle;
					content += "；【Eoms单号】"+eomsWoId;
					if(woList!=null&&!woList.isEmpty()){
						content += "；【故障描述】"+woList.get(0).getFaultDescription();
					}
					emailInfoMap.put("cellphone", sysOrgUserByAccount2.getMobile());
					emailInfoMap.put("content", content);
					//发送邮件
					commonService.sendCMCCEmail(emailInfoMap);	
				}
				
			}
		}
		return flag;

	}

	/**
	 * 最终回复专家任务
	 * 若工单的子任务全部结束或撤销，则更改工单状态为“处理中”
	 * @param reply
	 * @param toId
	 * @param urgentrepairTechsupporttaskorder
	 */
	public boolean txReplyUrgentRepairTechSupportTaskOrder(String reply,String woId,String toId,UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder){

		// 判断传入任务单号是否为空
		if (toId == null || "".equals(toId)) {
			throw new UserDefinedException("参数 toId 为空。");
		}
		if (woId == null || "".equals(woId)) {
			throw new UserDefinedException("参数 woId 为空。");
		}
		//获取当前处理人
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(reply);
//		Staff staff = providerOrganizationService.getStaffByAccount(reply);
		if(sysOrgUserByAccount==null){
			throw new UserDefinedException("找不到账号=="+reply+"的员工。");
		}
		//根据工单号获取工单实例
		List<UrgentrepairTechsupporttaskorder> tos = urgentRepairTechSupportTaskOrderDao.getUrgentRepairTechSupportTaskOrderByToId(toId);
		if(tos==null||tos.isEmpty()){
			throw new UserDefinedException("找不到任务单号=="+toId+"的专家任务单。");
		}
		
		if(urgentrepairTechsupporttaskorder==null){
			throw new UserDefinedException("参数 类urgentrepairTechsupporttaskorder 为空。");
		}
		UrgentrepairTechsupporttaskorder to = tos.get(0);
		
		//通过数据字典获取故障大类
		String faultGenera = urgentrepairTechsupporttaskorder.getFaultGenera();
		if(faultGenera!=null&&!"".equals(faultGenera)){
			TreeNode tn1 = dataDictionaryService.getDictionaryByTreeNodeIdService(Long.parseLong(faultGenera));
			if(tn1!=null){
				faultGenera = tn1.getTreeNodeName();
			}
		}
		
		//通过数据字典获取故障原因
		String faultReason = urgentrepairTechsupporttaskorder.getFaultReason();
		if(faultReason!=null&&!"".equals(faultReason)){
			TreeNode tn2 = dataDictionaryService.getDictionaryByTreeNodeIdService(Long.parseLong(faultReason));
			if(tn2!=null){
				faultReason = tn2.getTreeNodeName();
			}
		}
		
		to.setFaultGenera(faultGenera);
		to.setFaultHandleDetail(urgentrepairTechsupporttaskorder.getFaultHandleDetail());
		to.setSideeffectService(urgentrepairTechsupporttaskorder.getSideeffectService());
		to.setFaultHandleResult(urgentrepairTechsupporttaskorder.getFaultHandleResult());
		to.setFaultReason(faultReason);
		to.setReasonForDelayApply(urgentrepairTechsupporttaskorder.getReasonForDelayApply());
		to.setIsAlarmClear(urgentrepairTechsupporttaskorder.getIsAlarmClear());
		to.setAffectedServiceName(urgentrepairTechsupporttaskorder.getAffectedServiceName());
		//告警消除时间
		if("0".equals(urgentrepairTechsupporttaskorder.getIsAlarmClear()+"")){
			to.setAlarmClearTime(null);
		}else{
			if(urgentrepairTechsupporttaskorder.getAlarmClearTime()!=null){
				Date alarmClearTime =  TimeFormatHelper.setTimeFormat(urgentrepairTechsupporttaskorder.getAlarmClearTime());
				to.setAlarmClearTime(alarmClearTime);
			}else{
				to.setAlarmClearTime(new Date());
			}
		}
		
		if("1".equals(to.getFaultHandleResult())){
			//故障解决时间
			to.setFaultSolveTime(new Date());
		}else{
			//截止时间
			if(urgentrepairTechsupporttaskorder.getForeseeSolveTime()!=null){
				Date foreseeSolveTime =  TimeFormatHelper.setTimeFormat(urgentrepairTechsupporttaskorder.getForeseeSolveTime());
				to.setForeseeSolveTime(foreseeSolveTime);
			}
		}
		//调用DAO更新工单方法
		log.info("更新任务单号=="+toId+"专家任务单个性表。");
		urgentRepairTechSupportTaskOrderDao.updateUrgentRepairTechSupportTaskOrder(to);
		
		//服务跟踪记录
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
		tasktracerecord.setHandler(reply);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("最终回复专家任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setToId(toId);
		tasktracerecord.setWoType("抢修工单");
		String faultHandleResult="无";
		String faultHandleDetail="无";
		if("1".equals(urgentrepairTechsupporttaskorder.getFaultHandleResult())){
			faultHandleResult ="已解决";
			if(urgentrepairTechsupporttaskorder.getFaultHandleDetail()!=null){
				faultHandleDetail = urgentrepairTechsupporttaskorder.getFaultHandleDetail();
			}
		}else if("0".equals(urgentrepairTechsupporttaskorder.getFaultHandleResult())){
			faultHandleResult ="未解决";
		}
		tasktracerecord.setHandleResultDescription("【最终回复】处理措施："+faultHandleDetail+"；处理结果："+faultHandleResult);
		boolean techFinishFlag = false;
		log.info("启动专家任务单最终回复流程。");
		techFinishFlag = this.workManageService.finishTaskOrder(toId, reply,tasktracerecord);
		//当专家任务流程结束，开始穿透回复现场任务
		if(techFinishFlag){
			log.info("当专家任务流程结束，开始穿透回复现场任务。");
			WorkmanageTaskorder wto = workManageService.getTaskOrderEntity(toId);
			if(wto == null){
				throw new UserDefinedException("找不到任务单号=="+toId+"的专家任务单共性表。");
			}
			//判断上级任务的全部下级任务是否已结束
			String pareantToId = wto.getParentBizOrderId();
			log.info("判断任务单号=="+pareantToId+"的现场任务单的下级任务是否全部结束。");
			boolean isFinished = taskOrderCommonService.hasAllSubTasksFinishedByToId(pareantToId,toId);
			if(isFinished){
				log.info("当任务单号=="+pareantToId+"的现场任务单的下级任务已经全部结束，获取任务单号=="+pareantToId+"现场任务单个性表。");
				
				//获取技术支援任务的上级任务，现场任务单
				List<UrgentrepairSencetaskorder> stos = urgentRepairSenceTaskOrderDao
						.getUrgentRepairSenceTaskOrderByToId(pareantToId);
				if (stos == null||stos.isEmpty()) {
					throw new UserDefinedException("找不到任务单号=="+pareantToId+"现场任务单个性表。");
				}				
				UrgentrepairSencetaskorder sto = stos.get(0);
				sto.setFaultGenera(to.getFaultGenera());
				sto.setFaultHandleDetail(urgentrepairTechsupporttaskorder.getFaultHandleDetail());
				sto.setSideeffectService(urgentrepairTechsupporttaskorder.getSideeffectService());
				sto.setFaultHandleResult(urgentrepairTechsupporttaskorder.getFaultHandleResult());
				sto.setFaultReason(to.getFaultReason());
				sto.setReasonForDelayApply(urgentrepairTechsupporttaskorder.getReasonForDelayApply());
				sto.setIsAlarmClear(urgentrepairTechsupporttaskorder.getIsAlarmClear());
				sto.setAffectedServiceName(urgentrepairTechsupporttaskorder.getAffectedServiceName());
				//告警消除时间
				if(urgentrepairTechsupporttaskorder.getAlarmClearTime()!=null){
					Date alarmClearTime =  TimeFormatHelper.setTimeFormat(urgentrepairTechsupporttaskorder.getAlarmClearTime());
					sto.setAlarmClearTime(alarmClearTime);
				}else{
					sto.setAlarmClearTime(new Date());
				}
				//故障解决时间
				sto.setFaultSolveTime(new Date());
				//截止时间
				if(urgentrepairTechsupporttaskorder.getForeseeSolveTime()!=null){
					Date foreseeSolveTime =  TimeFormatHelper.setTimeFormat(urgentrepairTechsupporttaskorder.getForeseeSolveTime());
					sto.setForeseeSolveTime(foreseeSolveTime);
				}
				// 调用DAO更新工单方法
				log.info("更新任务单号=="+wto.getParentBizOrderId()+"现场任务单个性表。");
				urgentRepairSenceTaskOrderDao.updateUrgentRepairSenceTaskOrder(sto);
				// 更新任务单共性表，推动工作流
				boolean sceneFinishFlag = false;
				log.info("启动最终回复现场任务工作流。");
				//结束现场任务单
				sceneFinishFlag = this.workManageService.finishTaskOrder(pareantToId, wto.getAssigner(),null);
				if(sceneFinishFlag){
					//判断工单下的全部子任务是否全部结束或撤销
					boolean woSubIsFinish = workOrderCommonService.hasAllSubTasksFinishedByWoId(woId, pareantToId);
					if(woSubIsFinish){
						//更改工单状态为“处理中”
						if(workManageService.updateWorkOrderStatus(woId, WorkManageConstant.WORKORDER_HANDLING)){
							WorkmanageWorkorder wwo =  workManageService.getWorkOrderEntity(woId);
							if(wwo!=null){
								//消息盒子
								BizMessage bizMsg = new BizMessage();
								bizMsg.setContent("【处理中】"+wwo.getWoTitle());
								bizMsg.setSendPerson(reply);
								bizMsg.setReceivePerson(wwo.getCurrentHandler());
								bizMsg.setLink("op/urgentrepair/loadUrgentRepairWorkOrderPageAction?WOID="+woId);
								bizMsg.setSendTime(new Date());
								bizMsg.setFunctionType("UrgentRepairWorkOrder");
								bizMsg.setWoId(woId);
								bizMsg.setTitle(woId);
								bizMsg.setType("抢修");
								//发送消息给工单负责人
								bizMessageService.txAddBizMessageService(bizMsg,"newTask");
							}
							return true;
						}
					}
					
				}else{
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}
	
	
	
	/**
	 * 获取专家任务单
	 *
	 * @param toId
	 */
	public UrgentrepairTechsupporttaskorder getUrgentRepairTechSupportTaskOrder(String toId) {
		List<UrgentrepairTechsupporttaskorder> techTaskOrders = urgentRepairTechSupportTaskOrderDao.getUrgentRepairTechSupportTaskOrderByToId(toId);
		if(techTaskOrders==null||techTaskOrders.isEmpty()){
			return null;
		}
		return techTaskOrders.get(0);
	}
	
	/**
	 * 获取专家
	 * 技术支援任务的转派对象列表，根据登录人所属组织获取向下递归获取专家角色的人员列表
	 * 可根据条件查找
	 */
	public Set<Map>  loadToSendSpecialistInfoService(String providerOrgId,String search_value){
		Set<Map> teamerLists = new HashSet<Map>();
		if(providerOrgId==null||"".equals(providerOrgId)){
			throw new UserDefinedException("参数 providerOrgId 为空。");
		}
		long orgId = Long.parseLong(providerOrgId);
		Map busyStatus = new HashMap();
		long teamId = orgId;
		String team = "";
		//获取维护队任务数
//		busyStatus = staffBusyQueryService.queryBizUnitTaskCountByBizUnitId(teamId+"");		
//      int totalCount=commonWfFrameService.getTeamPendingTaskOrderCountByBizunitId(teamId+"");
		log.info("从组织ID=="+orgId+"递归向下获取技术专家。");
	//	List<Staff> accountList = providerOrganizationService.getStaffListDownwardByRoleAndOrg(orgId, BizAuthorityConstant.ROLE_TECHNICALSPECIALIST);
		//yuan.yw
		List<Map<String,Object>> accountList =this.sysOrganizationService.getProviderStaffByOrgIdAndRoleCode(orgId, BizAuthorityConstant.ROLE_TECHNICALSPECIALIST, "downward");
	
		if(accountList!=null&&!accountList.isEmpty()){
			for(Map<String,Object> staff : accountList){
				String name = staff.get("NAME")+"";
				//条件查询，匹配中文名
				if(search_value!=null&&!"".equals(search_value)){
					if(name.indexOf(search_value)<0){
						continue;
					}
				}
				Map map = new HashMap();
				String accountId = staff.get("ACCOUNT")+"";
				boolean flag = staffDutyService.checkIsDutyToday(accountId);
				if(flag){
					map.put("dutyStatus", "是");
				}else{
					map.put("dutyStatus", "否");
				}
				map.put("teamId", teamId);
				map.put("accountId", accountId);
				map.put("teamer",staff.get("NAME"));
				String phone = "";
				if(staff.get("CELLPHONENUMBER")==null||"".equals(staff.get("CELLPHONENUMBER"))||"null".equals(staff.get("CELLPHONENUMBER"))){
					phone = "";
				}else{
					phone = staff.get("CELLPHONENUMBER")+"";
				}
				map.put("phone", phone);
				map.put("sum", 0);
				teamerLists.add(map);
			}
			
		}
			
		return teamerLists;
	}

	public UrgentRepairTechSupportTaskOrderDao getUrgentRepairTechSupportTaskOrderDao() {
		return urgentRepairTechSupportTaskOrderDao;
	}

	public void setUrgentRepairTechSupportTaskOrderDao(
			UrgentRepairTechSupportTaskOrderDao urgentRepairTechSupportTaskOrderDao) {
		this.urgentRepairTechSupportTaskOrderDao = urgentRepairTechSupportTaskOrderDao;
	}


	public String getWOID() {
		return WOID;
	}

	public void setWOID(String woid) {
		WOID = woid;
	}

	public String getTOID() {
		return TOID;
	}

	public void setTOID(String toid) {
		TOID = toid;
	}

	public Map<String, String> getWorkOrderInfo() {
		return workOrderInfo;
	}

	public void setWorkOrderInfo(Map<String, String> workOrderInfo) {
		this.workOrderInfo = workOrderInfo;
	}

	public Map<String, String> getTaskOrderInfo() {
		return taskOrderInfo;
	}

	public void setTaskOrderInfo(Map<String, String> taskOrderInfo) {
		this.taskOrderInfo = taskOrderInfo;
	}

	public WorkManageService getWorkManageService() {
		return workManageService;
	}

	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}

	
	public UrgentRepairSenceTaskOrderDao getUrgentRepairSenceTaskOrderDao() {
		return urgentRepairSenceTaskOrderDao;
	}

	public void setUrgentRepairSenceTaskOrderDao(
			UrgentRepairSenceTaskOrderDao urgentRepairSenceTaskOrderDao) {
		this.urgentRepairSenceTaskOrderDao = urgentRepairSenceTaskOrderDao;
	}

	public UrgentRepairSenceTaskOrderService getUrgentRepairSenceTaskOrderService() {
		return urgentRepairSenceTaskOrderService;
	}

	public void setUrgentRepairSenceTaskOrderService(
			UrgentRepairSenceTaskOrderService urgentRepairSenceTaskOrderService) {
		this.urgentRepairSenceTaskOrderService = urgentRepairSenceTaskOrderService;
	}

	public TaskOrderCommonService getTaskOrderCommonService() {
		return taskOrderCommonService;
	}

	public void setTaskOrderCommonService(
			TaskOrderCommonService taskOrderCommonService) {
		this.taskOrderCommonService = taskOrderCommonService;
	}

	public WorkOrderCommonService getWorkOrderCommonService() {
		return workOrderCommonService;
	}

	public void setWorkOrderCommonService(
			WorkOrderCommonService workOrderCommonService) {
		this.workOrderCommonService = workOrderCommonService;
	}

	public StaffDutyService getStaffDutyService() {
		return staffDutyService;
	}

	public void setStaffDutyService(StaffDutyService staffDutyService) {
		this.staffDutyService = staffDutyService;
	}

	public UrgentRepairWorkOrderDao getUrgentRepairWorkOrderDao() {
		return urgentRepairWorkOrderDao;
	}

	public void setUrgentRepairWorkOrderDao(
			UrgentRepairWorkOrderDao urgentRepairWorkOrderDao) {
		this.urgentRepairWorkOrderDao = urgentRepairWorkOrderDao;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public DataDictionaryService getDataDictionaryService() {
		return dataDictionaryService;
	}

	public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
		this.dataDictionaryService = dataDictionaryService;
	}

	public WorkorderinterfaceWangyouWorkorderRelationService getWorkorderinterfaceWangyouWorkorderRelationService() {
		return workorderinterfaceWangyouWorkorderRelationService;
	}

	public void setWorkorderinterfaceWangyouWorkorderRelationService(
			WorkorderinterfaceWangyouWorkorderRelationService workorderinterfaceWangyouWorkorderRelationService) {
		this.workorderinterfaceWangyouWorkorderRelationService = workorderinterfaceWangyouWorkorderRelationService;
	}

	public BizMessageService getBizMessageService() {
		return bizMessageService;
	}

	public void setBizMessageService(BizMessageService bizMessageService) {
		this.bizMessageService = bizMessageService;
	}
	
	
	
	
}
