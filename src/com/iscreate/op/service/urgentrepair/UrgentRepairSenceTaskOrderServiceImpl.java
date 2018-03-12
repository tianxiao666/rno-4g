package com.iscreate.op.service.urgentrepair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.BizAuthorityConstant;
import com.iscreate.op.constant.OrganizationConstant;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.urgentrepair.UrgentRepairSenceTaskOrderDao;
import com.iscreate.op.dao.urgentrepair.UrgentRepairTechSupportTaskOrderDao;
import com.iscreate.op.dao.urgentrepair.UrgentRepairWorkOrderDao;
import com.iscreate.op.pojo.bizmsg.BizMessage;
import com.iscreate.op.pojo.publicinterface.Tasktracerecord;
import com.iscreate.op.pojo.system.SysOrg;
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
import com.iscreate.op.service.workmanage.WorkManageException;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.op.service.workorderinterface.wangyou.WorkorderinterfaceWangyouWorkorderRelationService;
import com.iscreate.plat.datadictionary.DataDictionaryService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.tree.TreeNode;

public class UrgentRepairSenceTaskOrderServiceImpl implements
		UrgentRepairSenceTaskOrderService {
	private  static final  Log log = LogFactory.getLog(UrgentRepairSenceTaskOrderServiceImpl.class);
	private UrgentRepairWorkOrderDao urgentRepairWorkOrderDao;
	private UrgentRepairSenceTaskOrderDao urgentRepairSenceTaskOrderDao;
	private UrgentRepairTechSupportTaskOrderDao urgentRepairTechSupportTaskOrderDao;
	private WorkManageService workManageService;
	private WorkOrderCommonService workOrderCommonService;
	private TaskOrderCommonService taskOrderCommonService;
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
	 * 受理现场任务
	 * 必须对现场任务单进行受理后才能对其进行后续业务操作
	 * @param accept 受理人
	 * @param woId 工单号
	 * @param toId 任务单号
	 */
	public boolean txAcceptUrgentRepairSenceTaskOrder(String accept,String woId,String toId,
			WorkmanageTaskorder workmanageTaskorder) {
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
		tasktracerecord.setHandleWay("受理现场任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setToId(toId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【受理意见】"+workmanageTaskorder.getAcceptComment());
		boolean flag = false;
		log.info("启动受理现场任务流程。");
		workmanageTaskorder.setAcceptTime(new Date());
		workmanageTaskorder.setToId(toId);
		//受理现场任务，不涉及工作流
		flag = this.workManageService.acceptTaskOrder(workmanageTaskorder, accept,tasktracerecord);
		return flag;

	}
	
	/**
	 * 现场任务阶段回复
	 * 若现场任务单所属工单树网优之家工单，则阶段回复现场任务后需要调用网优之家阶段回复接口更新网优之家工单信息
	 * @return
	 */
	public String txStepReplyUrgentRepairSenceTaskOrder(String actor,String woId,String toId,String stepReply_desc,String stepReply_processingProgress,String stepReply_faultType,String stepReply_faultResult,String customerWoType){
		String result = "success";
		//阶段回复处理人实例
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(actor);
//		Staff staff = providerOrganizationService.getStaffByAccount(actor);
		//阶段回复故障处理大类，通过数据字典获取
		if(stepReply_faultType!=null&&!"".equals(stepReply_faultType)){
			TreeNode tn1 = dataDictionaryService.getDictionaryByTreeNodeIdService(Long.parseLong(stepReply_faultType));
			if(tn1!=null){
				stepReply_faultType = tn1.getReferenceValue();
			}
		}
		//阶段回复故障处理原因，通过数据字典获取
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
		tasktracerecord.setHandleWay("阶段回复现场任务");
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
	 * 撤销任务
	 * 在派发任务的下级任务管理列表里，可以对现场任务单下的状态不为“已撤销”或“已结束”的子任务作撤销操作
	 * @param actor 撤销任务者
	 */
	public boolean txCancelUrgentRepairTechSupportTaskOrder(String actor,String woId,String toId,String subToId,
			WorkmanageTaskorder workmanageTaskorder) {
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
		tasktracerecord.setHandler(actor);
		tasktracerecord.setHandlerName(sysOrgUserByAccount.getName()+"");
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("撤销专家任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setToId(toId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【撤销意见】"+workmanageTaskorder.getCancelComment());
		boolean flag = false;
		try {
			log.info("启动重派专家任务流程。");
			workmanageTaskorder.setCancelTime(new Date());
			workmanageTaskorder.setToId(subToId);
			flag = this.workManageService.cancelTaskOrder(workmanageTaskorder, actor, tasktracerecord);
			if(flag){
				boolean woSubIsFinish = taskOrderCommonService.hasAllSubTasksFinishedByToId(toId, subToId);
				if(woSubIsFinish){
					if(workManageService.updateTaskOrderStatus(toId, WorkManageConstant.TASKORDER_HANDLING)){
						return true;
					}
				}
			}
		} catch (WorkManageException e) {
			return false;
		}
		return flag;

	}
	
	/**
	 * 重派任务
	 *在派发任务的下级任务管理列表里，可以对工单下的状态为“待撤销”的子任务作重派操作
	 * @param actor
	 * @param toId
	 */
	public boolean txReAssignlUrgentRepairTechSupportTaskOrder(String actor,String woId,String toId,String subToId,
			WorkmanageTaskorder workmanageTaskorder) {
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
		tasktracerecord.setHandler(subToId);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("重派专家任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setToId(toId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【重派意见】"+workmanageTaskorder.getReassignComment());
		boolean flag = false;
		try {
			log.info("启动重派专家任务流程。");
			workmanageTaskorder.setReassignTime(new Date());
			workmanageTaskorder.setToId(subToId);
			flag = this.workManageService.reAssignTaskOrder(workmanageTaskorder,tasktracerecord);
		} catch (WorkManageException e) {
			return false;
		}
		return flag;

	}
	
	/**
	 * 催办任务
	 * 在派发任务的下级任务管理列表里，可以对工单下的子任务作催办操作，催办后会对催办对象任务发送一条催办消息
	 * @param actor
	 * @param toId
	 */
	public boolean txHastenUrgentRepairTechSupportTaskOrder(String actor,String woId,String toId,String subToId,
			WorkmanageTaskorder workmanageTaskorder) {
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
		
		//获取接收催办信息对象
		WorkmanageTaskorder to =  workManageService.getTaskOrderEntity(toId);
		if(to == null){
			throw new UserDefinedException("找不到toId=="+toId+"的任务单。");
		}
		String receivePerson = to.getCurrentHandler();
		
		//服务跟踪记录
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
		tasktracerecord.setHandler(actor);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("催办专家任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setToId(toId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【催办任务】"+toId+"【催办意见】"+workmanageTaskorder.getUrgeWorkComment());
		boolean flag = workManageService.stepReply(tasktracerecord);
		if(flag){
			//消息盒子
			BizMessage bizMsg = new BizMessage();
			bizMsg.setContent(workmanageTaskorder.getUrgeWorkComment());
			bizMsg.setSendPerson(actor);
			bizMsg.setReceivePerson(receivePerson);
			bizMsg.setLink("op/urgentrepair/loadUrgentRepairTechSupportTaskOrderPageAction?WOID="+woId+"&TOID="+toId);
			bizMsg.setLinkForMobile("loadUrgentRepairTechSupportTaskOrderPageActionForMobile?WOID="+woId+"&TOID="+toId);
			bizMsg.setFunctionType("UrgentRepairTechSupportTaskOrder");
			bizMsg.setWoId(woId);
			bizMsg.setToId(toId);
			bizMsg.setSendTime(new Date());
			bizMsg.setTitle(toId);
			bizMsg.setType("抢修");
			//发送催办消息
			bizMessageService.txAddBizMessageService(bizMsg,"hasten");
		}
		
		return true;

	}
	
	
	/**
	 * 驳回现场任务
	 * 在未受理现场任务之前可驳回任务，驳回后该任务单不能编辑，状态变为“待撤销”
	 * @param actor
	 * @param toId
	 */
	public boolean txRejectUrgentRepairSenceTaskOrder(String actor,String woId,String toId,
			WorkmanageTaskorder workmanageTaskorder) {
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
		tasktracerecord.setHandleWay("驳回现场任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setToId(toId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【驳回意见】"+workmanageTaskorder.getRejectComment());
		boolean flag = false;
		log.info("启动工作驳回现场任务流程。");
		workmanageTaskorder.setRejectTime(new Date());
		workmanageTaskorder.setToId(toId);
		//驳回现场任务
		flag = this.workManageService.rejectTaskOrder(workmanageTaskorder, tasktracerecord);
		return flag;

	}
	
	
	/**
	 * 转派现场任务
	 * 将任务的责任人转移到转派对象身上，转派不涉及工作流，只是更改任务的当前责任人
	 * 转派后会邮件通知转派对象
	 * @param actor
	 * @param woId
	 * @param toId
	 * @param recipient
	 * @param stepReply_desc
	 * @return
	 */
	public boolean txToSendUrgentRepairSenceTaskOrder(String actor,String woId,String toId,String recipient,
			String stepReply_desc) {
		// 判断传入任务单号是否为空
		if (toId == null || "".equals(toId)) {
			throw new UserDefinedException("参数 toId 为空。");
		}
		if (woId == null || "".equals(woId)) {
			throw new UserDefinedException("参数 woId 为空。");
		}
		if (recipient == null || "".equals(recipient)) {
			throw new UserDefinedException("参数 recipient 为空。");
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
		// 更新任务单共性表，推动工作流
		// 指定待办人
		List<String> participantList = new ArrayList<String>();
		participantList.add(actor);
		//服务跟踪记录
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
		tasktracerecord.setHandler(actor);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("转派现场任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setToId(toId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【转派原因】"+stepReply_desc);
		boolean flag = false;
		log.info("启动转派现场任务流程。");
		
		//工单当前处理人所属组织ID
		long currentHandlerOrgId = 0;
		//List<ProviderOrganization> currentHandlerOrgs = providerOrganizationService.getOrgByAccountAndOrgType(recipient, OrganizationConstant.ORGANIZATION_MAINTENANCETEAM);
		//yuan.yw
		List<SysOrg> currentHandlerOrgs = this.sysOrganizationService.getOrgByAccountAndOrgType(recipient, OrganizationConstant.ORGANIZATION_MAINTENANCETEAM);
		if(currentHandlerOrgs!=null&&currentHandlerOrgs.size()>0){
			currentHandlerOrgId = currentHandlerOrgs.get(0).getOrgId();
			if(currentHandlerOrgs.size()>1){
				log.warn("获取账号=="+recipient+"所在维护队有多个。currentHandlerOrgs.size()>1");
				
			}
		}else {
			throw new UserDefinedException("找不到账号=="+recipient+"担任任务调度员角色所在组织");
		}
		WorkmanageWorkorder updateWorkorder =  new WorkmanageWorkorder();
		updateWorkorder.setOrderOwner(recipient);
		updateWorkorder.setOrderOwnerOrgId(currentHandlerOrgId);
		WorkmanageTaskorder updateTaskorder =  new WorkmanageTaskorder();
		updateTaskorder.setCurrentHandlerOrgId(currentHandlerOrgId);
		updateTaskorder.setCurrentHandler(recipient);
		updateTaskorder.setOrderOwner(recipient);
		updateTaskorder.setOrderOwnerOrgId(currentHandlerOrgId);
		if(sysOrgUserByAccount2.getName()!=null){
			updateTaskorder.setCurrentHandlerName(sysOrgUserByAccount2.getName());
		}else{
			updateTaskorder.setCurrentHandlerName("");
		}
		//转派现场任务单
		flag = this.workManageService.handoverTaskOrder(woId, toId,updateWorkorder, updateTaskorder, tasktracerecord);
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
					
					emailInfoMap.put("title", "现场任务单，截止时间："+TimeFormatHelper.getTimeFormatBySecond(workmanageTaskorder.getRequireCompleteTime()).replace("_", " ")+remaininTime);
				}else{
					emailInfoMap.put("title", "现场任务单");
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
	 * 最终回复现场任务
	 * 若工单的子任务全部结束或撤销，则更改工单状态为“处理中”
	 * @param reply
	 * @param toId
	 * @param urgentrepairSencetaskorder
	 */
	public boolean txFinalReplyUrgentRepairSenceTaskOrder(String reply,String woId,
			String toId, UrgentrepairSencetaskorder urgentrepairSencetaskorder) {

		// 判断传入任务单号是否为空
		if (toId == null || "".equals(toId)) {
			throw new UserDefinedException("参数 toId 为空。");
		}
		if (woId == null || "".equals(woId)) {
			throw new UserDefinedException("参数 woId 为空。");
		}
		// 根据工单号获取工单实例
		List<UrgentrepairSencetaskorder> tos = urgentRepairSenceTaskOrderDao
				.getUrgentRepairSenceTaskOrderByToId(toId);
		if (tos == null||tos.isEmpty()) {
			throw new UserDefinedException("找不到任务单号=="+toId+"的现场任务单。");
		}
		if (urgentrepairSencetaskorder == null) {
			throw new UserDefinedException("参数 类urgentrepairSencetaskorder 为空。");
		}
		
		//获取当前处理人
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(reply);
//		Staff staff = providerOrganizationService.getStaffByAccount(reply);
		if(sysOrgUserByAccount==null){
			throw new UserDefinedException("找不到账号=="+reply+"的员工。");
		}
		UrgentrepairSencetaskorder to = tos.get(0);
		//根据数据字典获取故障大类
		String faultGenera = urgentrepairSencetaskorder.getFaultGenera();
		if(faultGenera!=null&&!"".equals(faultGenera)){
			TreeNode tn1 = dataDictionaryService.getDictionaryByTreeNodeIdService(Long.parseLong(faultGenera));
			if(tn1!=null){
				faultGenera = tn1.getTreeNodeName();
			}
		}
		//根据数据字典获取故障原因
		String faultReason = urgentrepairSencetaskorder.getFaultReason();
		if(faultReason!=null&&!"".equals(faultReason)){
			TreeNode tn2 = dataDictionaryService.getDictionaryByTreeNodeIdService(Long.parseLong(faultReason));
			if(tn2!=null){
				faultReason = tn2.getTreeNodeName();
			}
		}
		
		
		to.setFaultGenera(faultGenera);
		to.setFaultHandleDetail(urgentrepairSencetaskorder.getFaultHandleDetail());
		to.setSideeffectService(urgentrepairSencetaskorder.getSideeffectService());
		to.setFaultHandleResult(urgentrepairSencetaskorder.getFaultHandleResult());
		to.setFaultReason(faultReason);
		to.setReasonForDelayApply(urgentrepairSencetaskorder.getReasonForDelayApply());
		to.setIsAlarmClear(urgentrepairSencetaskorder.getIsAlarmClear());
		to.setAffectedServiceName(urgentrepairSencetaskorder.getAffectedServiceName());
		//告警消除时间
		if("0".equals(urgentrepairSencetaskorder.getIsAlarmClear()+"")){
			to.setAlarmClearTime(null);
		}else{
			if(urgentrepairSencetaskorder.getAlarmClearTime()!=null){
				Date alarmClearTime =  TimeFormatHelper.setTimeFormat(urgentrepairSencetaskorder.getAlarmClearTime());
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
			if(urgentrepairSencetaskorder.getForeseeSolveTime()!=null){
				Date foreseeSolveTime =  TimeFormatHelper.setTimeFormat(urgentrepairSencetaskorder.getForeseeSolveTime());
				to.setForeseeSolveTime(foreseeSolveTime);
			}
		}
		
		
		// 调用DAO更新工单方法
		log.info("更新任务单号=="+toId+"的现场任务单。");
		urgentRepairSenceTaskOrderDao.updateUrgentRepairSenceTaskOrder(to);

		//服务跟踪记录
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
		tasktracerecord.setHandler(reply);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("最终回复现场任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setToId(toId);
		tasktracerecord.setWoType("抢修工单");
		String faultHandleResult="无";
		String faultHandleDetail="无";
		if("1".equals(urgentrepairSencetaskorder.getFaultHandleResult())){
			faultHandleResult ="已解决";
			if(urgentrepairSencetaskorder.getFaultHandleDetail()!=null){
				faultHandleDetail = urgentrepairSencetaskorder.getFaultHandleDetail();
			}
		}else if("0".equals(urgentrepairSencetaskorder.getFaultHandleResult())){
			faultHandleResult ="未解决";
		}
		tasktracerecord.setHandleResultDescription("【最终回复】处理措施："+faultHandleDetail+"；处理结果："+faultHandleResult);
		boolean flag = false;
		log.info("启动最终回复流程。");
		//最终回复工单
		flag = workManageService.finishTaskOrder(toId, reply,tasktracerecord);
		if(flag){
			boolean woSubIsFinish = workOrderCommonService.hasAllSubTasksFinishedByWoId(woId, toId);
			if(woSubIsFinish){
				//判断工单的子任务是否全部结束或撤销，若果全部结束或撤销则更改工单状态为处理中并发送一条消息通知工单责任人
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
						//发送新任务消息给工单责任人
						bizMessageService.txAddBizMessageService(bizMsg,"newTask");
					}
					return true;
				}
			}
		}else{
			return false;
		}
		
		return true;
	}

	
	/**
	 * 升级技术支援任务
	 * 现场任务负责人无法处理故障是，将任务升级为技术支援任务，由专家去解决，现场任务和技术支援任务是1对1关系
	 * @param sendor 派发人
	 * @param recipient 接收人
	 * @param woId 工单号
	 * @param toId 任务单号
	 * @param urgentrepairTechsupporttaskorder 技术支援单信息
	 */
	public void txCreateUrgentRepairTechSupportTaskOrder(String sender,
			String recipient,  String woId, String senceToId,
			UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder,
			WorkmanageTaskorder workmanageTaskorder) {
		if (woId == null) {
			throw new UserDefinedException("参数 woId 为空。");
		}
		if(senceToId == null){
			throw new UserDefinedException("参数 toId 为空。");
		}
		//获取当前处理人
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(sender);
//		Staff staff = providerOrganizationService.getStaffByAccount(sender);
		if(sysOrgUserByAccount==null){
			throw new UserDefinedException("参数 sender 为空。");
		}
		//获取当前处理人
		//ou.jh
		SysOrgUser sysOrgUserByAccount2 = this.sysOrgUserService.getSysOrgUserByAccount(recipient);
//		Staff rStaff = providerOrganizationService.getStaffByAccount(recipient);
		if(sysOrgUserByAccount2==null){
			throw new UserDefinedException("参数 recipient 为空。");
		}
		// 保存专家任务单个性表
		log.info("保存专家任务单个性表。");
		Serializable tstoid =  urgentRepairTechSupportTaskOrderDao.saveUrgentRepairTechSupportTaskOrder(urgentrepairTechsupporttaskorder);
		log.info("生成专家任务单个性表的主键=="+tstoid);
		//派发对象
		List<String> participantList = new ArrayList<String>();
		participantList.add(recipient);
		//获取创建者的所在组织
		log.info("获取账号=="+sender+"所属的，维护队组织。");
		//List<ProviderOrganization> providerOrganizations = providerOrganizationService.getOrgByAccountAndOrgType(sender, OrganizationConstant.ORGANIZATION_MAINTENANCETEAM);
		//yuan.yw
		List<SysOrg> providerOrganizations = this.sysOrganizationService.getOrgByAccountAndOrgType(sender, OrganizationConstant.ORGANIZATION_MAINTENANCETEAM);
		long providerOrgId = 0;
		if(providerOrganizations!=null&&providerOrganizations.size()>0){
			providerOrgId = providerOrganizations.get(0).getOrgId();
			if(providerOrganizations.size()>1){
				log.warn("账号=="+sender+"所在维护队组织有多个。sroviderOrganizations.size()>1");
				
			}
		}else {
			throw new UserDefinedException("找不到账号=="+sender+"所属的维护队组织。");
		}
		
		//工单当前处理人所属组织ID
		long currentHandlerOrgId = 0;
		//List<ProviderOrganization> currentHandlerOrgs = providerOrganizationService.getOrgByAccountAndRoleCode(recipient, BizAuthorityConstant.ROLE_TECHNICALSPECIALIST);
		//yuan.yw
		List<Map<String,Object>> currentHandlerOrgs = this.sysOrganizationService.getOrgByAccountAndRoleCode(recipient, BizAuthorityConstant.ROLE_TECHNICALSPECIALIST);
		if(currentHandlerOrgs!=null&&currentHandlerOrgs.size()>0){
			currentHandlerOrgId = Long.parseLong(currentHandlerOrgs.get(0).get("orgId")+"");
			if(currentHandlerOrgs.size()>1){
				log.warn("获取账号=="+recipient+"担任任务调度员角色所在组织有多个。currentHandlerOrgs.size()>1");
				
			}
		}else {
			throw new UserDefinedException("找不到账号=="+recipient+"担任任务调度员角色所在组织");
		}
		
		//公共任务单属性
		workmanageTaskorder.setToType("抢修");
		workmanageTaskorder.setAssigner(sender);
		workmanageTaskorder.setAssignerName(sysOrgUserByAccount.getName());
		workmanageTaskorder.setAssignerOrgId(providerOrgId);
		workmanageTaskorder.setCurrentHandlerOrgId(currentHandlerOrgId);
		//服务跟踪记录
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
		tasktracerecord.setHandler(sender);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("升级专家任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【升级原因】"+workmanageTaskorder.getAssignComment());
		//派发专家，生成单号
		log.info("升级专家任务单，启动工作流，生成专家任务单号。");
		String techToId = workManageService.assignTaskOrder(WorkManageConstant.PROCESS_URGENTREPAIR_EXPERT_CODE, woId, senceToId, workmanageTaskorder, participantList,tasktracerecord);
		long ltstoid  = Long.parseLong(tstoid+"");
		UrgentrepairTechsupporttaskorder tsto = urgentRepairTechSupportTaskOrderDao.getUrgentRepairTechSupportTaskOrderById(ltstoid);
		if(tsto == null){
			throw new UserDefinedException("找不到主键=="+ltstoid+"的专家任务单个性表。");
		}
		//更新专家表
		tsto.setToId(techToId);
		log.info("将生成的专家任务单号=="+techToId+" 更新至专家任务单个性表中。");
		urgentRepairTechSupportTaskOrderDao.updateUrgentRepairTechSupportTaskOrder(tsto);
		
		//更改任务单状态为升级中
		log.info("升级专家任务后，更改现场任务单状态为‘升级中’。");
		boolean flag =workManageService.updateTaskOrderStatus(senceToId, WorkManageConstant.TASKORDER_UPGRADING);
		if(flag){
			//升级技术支援任务成功，发送邮件通知派发对象
			Map woTemp = workManageService.getWorkOrderForShow(woId);
			String woTitle = "";
			if(woTemp!=null){
				woTitle = woTemp.get("woTitle").toString();
			}
//			Map toTemp = workManageService.getTaskOrderForShow(toId);
//			String toType = "";
//			if(toTemp!=null&&toTemp.get("bizProcessName")!=null){
//				toType = toTemp.get("bizProcessName").toString();
//			}
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
			
//			List<UrgentrepairWorkorder> woList = urgentRepairWorkOrderDao.getUrgentrepairWorkorderByWoId(woId);
//			String part1 = "";
//			if(woList!=null&&!woList.isEmpty()){
//				part1 = woList.get(0).getFaultType()+"/"+woList.get(0).getFaultLevel()+"/"+woList.get(0).getAcceptProfessional();
//				
//			}
			if(sysOrgUserByAccount2.getMobile()!=null&&!"null".equals(sysOrgUserByAccount2.getMobile())&&!"".equals(sysOrgUserByAccount2.getMobile())){
				Map params =new HashMap();
				params.put("iosmWoId", woId);
				List<WorkorderinterfaceWangyouWorkorderRelation> wrs = workorderinterfaceWangyouWorkorderRelationService.getWorkorderinterfaceWangyouWorkorderRelationList(params);
				String eomsWoId = "无";
				if(wrs!=null&&!wrs.isEmpty()){
					eomsWoId = wrs.get(0).getCustomerWoId();
				}
				String content = "【工单标题】"+woTitle;
				content += " ；【Eoms单号】"+eomsWoId;
				content += " ；【故障描述】"+workmanageTaskorder.getAssignComment();
				emailInfoMap.put("content", content);
				emailInfoMap.put("cellphone", sysOrgUserByAccount2.getMobile());
				//发送邮件
				commonService.sendCMCCEmail(emailInfoMap);
			}
			
		}
	}

	/**
	 * 获取现场任务单根据任务单号
	 *
	 * @param toId
	 */
	public UrgentrepairSencetaskorder getUrgentRepairSenceTaskOrder(String toId) {
		//获取现场任务单根据任务单号
		List<UrgentrepairSencetaskorder> senceTaskOrders = urgentRepairSenceTaskOrderDao.getUrgentRepairSenceTaskOrderByToId(toId);
		if(senceTaskOrders==null||senceTaskOrders.isEmpty()){
			return null;
		}
		return senceTaskOrders.get(0);
	}
	
	/**
	 * 获取专家列表
	 * 派发技术支援任务，选择专家
	 * 通过登录人查找其所属组织的上级或下级组织中有任务调度员的组织，再通过递归从该组织开始往上寻找有专家角色的人员的组织，从而找出专家列表
	 * @param account
	 * @return
	 */
	public List<Map> loadSpecialistInfoService(String account){
		// 派发对象
		if(account==null||"".equals(account)){
			log.debug("参数 account 为空。");
			return null;
		}
		log.info("获取账号=="+account+"的上级组织或下级组织中有任务调度员角色的组织。");
		//获取登录人所属组织的上级组织或下级组织中有任务调度员角色的组织。
		//List<ProviderOrganization> providerOrganizations = providerOrganizationService.getOrgInRoleCodeByAccountService(account, BizAuthorityConstant.ROLE_TASKDISPATCHER);
		List<Map<String,Object>> providerOrganizations = this.sysOrganizationService.getOrgByAccountAndRoleCode(account, BizAuthorityConstant.ROLE_TASKDISPATCHER);
		
		List<Map> staffList =  new ArrayList<Map>();
		List<Map<String,Object>> accounts = new ArrayList<Map<String,Object>>();
		List<Map> result = new ArrayList<Map>();
		if(providerOrganizations==null||providerOrganizations.isEmpty()){
			log.info("获取账号=="+account+"的上级组织或下级组织中有任务调度员角色的组织。");
			return null;
		}
		//往上递归寻找技术专家角色的账号
		for(Map<String,Object> org:providerOrganizations){
			log.info("获取账号=="+org.get("orgId")+"的上级组织的技术专家角色的账号。");
			//accounts = providerOrganizationService.getStaffListUpwardByRoleAndOrg(org.getId(), BizAuthorityConstant.ROLE_TECHNICALSPECIALIST);
			//yuan.yw
			accounts = this.sysOrganizationService.getProviderStaffByOrgIdAndRoleCode(Long.valueOf(org.get("orgId")+""), BizAuthorityConstant.ROLE_TECHNICALSPECIALIST, "upward");
			if(accounts==null||accounts.isEmpty()){
				log.info("账号=="+org.get("orgId")+"的上级组织没有的技术专家角色的账号。");
				continue;
			}		
			//过滤重复人员
			log.info("过滤重复的专家。");
			for (Map<String,Object> acc : accounts) {
				boolean flag = false;
				if(result!=null&&result.size()>0){
					for(Map map:result){
						if((acc.get("ACCOUNT")+"").equals(map.get("account"))){
							flag = true;
						}
					}
				}
				if(flag){
					continue;
				}
				Map te = new HashMap();
				Map<String,String> busyStatus = new HashMap<String,String>();
				boolean isOk = staffDutyService.checkIsDutyToday(account);
				long count = workManageService.getTaskOrderCountByObjectTypeAndObjectId("people", acc.get("ACCOUNT")+"");
				
				te.put("account", acc.get("ACCOUNT"));// q.h
				te.put("name", acc.get("NAME"));
				te.put("sum", count);
				boolean isSb = staffDutyService.checkIsDutyToday(account);
				if(isSb){
					te.put("dutyStatus", "是");
				}else{
					te.put("dutyStatus", "否");
				}
				String phone = "";
				if(acc.get("CELLPHONE")==null||"".equals(acc.get("CELLPHONE"))||"null".equals(acc.get("CELLPHONE"))){
					phone = "";
				}else{
					phone = acc.get("CELLPHONE")+"";
				}
				te.put("phone", phone);
				result.add(te);
			}

		}
		//专家列表
		staffList.addAll(result);
		
		return staffList;
	}
	
	/**
	 * 获取维护人员
	 * 现场任务转派对象的列表，获取登录人所属组织的同级组织下的所有维护人员
	 * 可根据条件查找
	 * @param providerOrgId 登录人所属组织ID
	 * @param search_value 查询条件（姓名模糊查找）
	 */
	public List<Map>  loadTeamersInfoService(String providerOrgId,String search_value){
		List<Map> teamerLists = new ArrayList<Map>();
		if(providerOrgId == null || "".equals(providerOrgId)){
			log.debug("参数 providerOrgId 为空。");
			return null;
		}
		long orgId = Long.parseLong(providerOrgId);
		log.info("获取组织ID=="+providerOrgId+"的下级组织中所有维护队类型的组织。");
		//List<ProviderOrganization> providerOrganizations = providerOrganizationService.getOrgListDownwardByOrgTypeAndOrg(orgId, OrganizationConstant.ORGANIZATION_MAINTENANCETEAM);
		//yuan.yw
		List<Map<String,Object>> providerOrganizations = this.sysOrganizationService.getOrgListDownwardOrUpwardByOrgTypeAndOrgIdService(orgId, OrganizationConstant.ORGANIZATION_MAINTENANCETEAM,"child");
		// 队长List	
		for (int i = 0; i < providerOrganizations.size(); i++) {
			Map busyStatus = new HashMap();
			long teamId = Long.valueOf(providerOrganizations.get(i).get("orgId")+"");
			String team = providerOrganizations.get(i).get("name")+"";
//			//获取维护队任务数
//			busyStatus = staffBusyQueryService.queryBizUnitTaskCountByBizUnitId(teamId+"");
			
			//int totalCount=commonWfFrameService.getTeamPendingTaskOrderCountByBizunitId(teamId+"");
			log.info("获取组织ID=="+teamId+"维护队的维护队员。");
			//List<Staff> accountList = providerOrganizationService.getStaffListDownwardByRoleAndOrg(teamId, BizAuthorityConstant.ROLE_MAINTENANCEWORKER);
			//yuan.yw
			List<Map<String,Object>> accountList =this.sysOrganizationService.getProviderStaffByOrgIdAndRoleCode(teamId, BizAuthorityConstant.ROLE_TEAMMEMBER, "downward");
		
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
					long count = workManageService.getTaskOrderCountByObjectTypeAndObjectId("people", accountId);
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
					map.put("sum", count);
					teamerLists.add(map);
				}
				
			}
			
		}
		
		return teamerLists;
	}
	

	public UrgentRepairSenceTaskOrderDao getUrgentRepairSenceTaskOrderDao() {
		return urgentRepairSenceTaskOrderDao;
	}

	public void setUrgentRepairSenceTaskOrderDao(
			UrgentRepairSenceTaskOrderDao urgentRepairSenceTaskOrderDao) {
		this.urgentRepairSenceTaskOrderDao = urgentRepairSenceTaskOrderDao;
	}

	public UrgentRepairTechSupportTaskOrderDao getUrgentRepairTechSupportTaskOrderDao() {
		return urgentRepairTechSupportTaskOrderDao;
	}

	public void setUrgentRepairTechSupportTaskOrderDao(
			UrgentRepairTechSupportTaskOrderDao urgentRepairTechSupportTaskOrderDao) {
		this.urgentRepairTechSupportTaskOrderDao = urgentRepairTechSupportTaskOrderDao;
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

	

	public WorkManageService getWorkManageService() {
		return workManageService;
	}

	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}


	public WorkOrderCommonService getWorkOrderCommonService() {
		return workOrderCommonService;
	}


	public void setWorkOrderCommonService(
			WorkOrderCommonService workOrderCommonService) {
		this.workOrderCommonService = workOrderCommonService;
	}


	public TaskOrderCommonService getTaskOrderCommonService() {
		return taskOrderCommonService;
	}


	public void setTaskOrderCommonService(
			TaskOrderCommonService taskOrderCommonService) {
		this.taskOrderCommonService = taskOrderCommonService;
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
