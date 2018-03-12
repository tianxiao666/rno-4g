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
import com.iscreate.op.dao.publicinterface.WorkOrderAssnetResourceDao;
import com.iscreate.op.dao.urgentrepair.UrgentRepairCustomerWorkOrderDao;
import com.iscreate.op.dao.urgentrepair.UrgentRepairSenceTaskOrderDao;
import com.iscreate.op.dao.urgentrepair.UrgentRepairTechSupportTaskOrderDao;
import com.iscreate.op.dao.urgentrepair.UrgentRepairWorkOrderDao;
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
import com.iscreate.op.pojo.workorderinterface.wangyou.WorkorderinterfaceWangyouWorkorderRelation;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.op.service.publicinterface.CommonService;
import com.iscreate.op.service.publicinterface.TaskTracingRecordService;
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

public class UrgentRepairWorkOrderServiceImpl implements
		UrgentRepairWorkOrderService {
	// 注入Dao层
	private UrgentRepairWorkOrderDao urgentRepairWorkOrderDao;
	private UrgentRepairCustomerWorkOrderDao urgentRepairCustomerWorkOrderDao;
	private UrgentRepairSenceTaskOrderDao urgentRepairSenceTaskOrderDao;
	private UrgentRepairTechSupportTaskOrderDao urgentRepairTechSupportTaskOrderDao;
	private WorkOrderAssnetResourceDao workOrderAssnetResourceDao;
	private WorkManageService workManageService;
	private WorkOrderCommonService workOrderCommonService;
	private StaffDutyService staffDutyService;
	private CommonService commonService;
	private DataDictionaryService dataDictionaryService;
	private WorkorderinterfaceWangyouWorkorderRelationService workorderinterfaceWangyouWorkorderRelationService;
	private BizMessageService bizMessageService;
	private TaskTracingRecordService taskTracingRecordService;
	private SysOrgUserService sysOrgUserService;
	
	
	private  static final  Log log = LogFactory.getLog(UrgentRepairWorkOrderServiceImpl.class);

	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	/**
	 * 创建故障抢修工单&客户工单
	 * 
	 * @param Creator
	 * @param woId
	 * @param urgentrepairWorkorder
	 */
	public String txCreateUrgentRepairWorkOrder(String creator, UrgentrepairWorkorder urgentrepairWorkorder,
		UrgentrepairCustomerworkorder urgentrepairCustomerworkorder,Workorderassnetresource workorderassnetresource,WorkmanageWorkorder workmanageWorkorder) {
		if (creator == null) {
			log.error("传入参数 creator 为空");
			throw new UserDefinedException("参数 creator 为空。");
		}
		// 创建工单个性表
		if (urgentrepairWorkorder == null) {
			log.error("传入参数 urgentrepairWorkorder 为空");
			throw new UserDefinedException("参数 类urgentrepairWorkorder 为空。");
		}
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(creator);
//		Staff staff = providerOrganizationService.getStaffByAccount(creator);
		if(sysOrgUserByAccount==null){
			log.error("找不到 账号为"+creator+"的员工");
			throw new UserDefinedException("找不到账号=="+creator+"的员工。");
		}
		//字符串转换Date
		//故障发生时间
		if(urgentrepairWorkorder.getFaultOccuredTime()!=null){
			Date faultOccuredTime =  TimeFormatHelper.setTimeFormat(urgentrepairWorkorder.getFaultOccuredTime());
			urgentrepairWorkorder.setFaultOccuredTime(faultOccuredTime);
		}
		//截止时间
		if(urgentrepairWorkorder.getLatestAllowedTime()!=null){
			Date latestAllowedTime =  TimeFormatHelper.setTimeFormat(urgentrepairWorkorder.getLatestAllowedTime());
			urgentrepairWorkorder.setLatestAllowedTime(latestAllowedTime);
			workmanageWorkorder.setRequireCompleteTime(latestAllowedTime);
		}
		
	
		// 保存业务工单数据
		Serializable workorderid = urgentRepairWorkOrderDao
				.saveUrgentRepairWorkOrder(urgentrepairWorkorder);
		log.debug("成功保存抢修工单业务数据");
		 //保存客户工单数据
		Serializable customerworkorderid = null;
		if (urgentrepairCustomerworkorder != null) {
			customerworkorderid = urgentRepairCustomerWorkOrderDao
					.saveUrgentRepairCustomerWorkOrder(urgentrepairCustomerworkorder);
			log.debug("成功保存抢修客户工单数据");
		}else{
			log.info("创建抢修工单，客户工单的对象为空");
		}		

		//获取创建人的所在组织
		//List<ProviderOrganization> providerOrganizations = providerOrganizationService.getOrgByAccountAndRoleCode(creator,BizAuthorityConstant.ROLE_TASKDISPATCHER );
		//yuan.yw
		List<Map<String,Object>> providerOrganizations = this.sysOrganizationService.getOrgByAccountAndRoleCode(creator,BizAuthorityConstant.ROLE_TASKDISPATCHER );
		
		long providerOrgId = 0;
		if(providerOrganizations!=null&&providerOrganizations.size()>0){
			providerOrgId = Long.parseLong(providerOrganizations.get(0).get("orgId")+"");
			if(providerOrganizations.size()>1){
				log.warn("账号=="+creator+"担任角色任务调度员所在的组织有多个。sroviderOrganizations.size()>1");
				
			}
			log.debug("获取账号为"+creator+"的人员所属的组织，组织ID为"+providerOrgId+"");
		}else {
			log.error("找不到账号=="+creator+"担任角色任务调度员所在的组织");
			throw new UserDefinedException("找不到账号=="+creator+"担任角色任务调度员所在的组织");
		}
		
		//公共工单属性
		workmanageWorkorder.setWoType("抢修");
		workmanageWorkorder.setCreator(creator);
		workmanageWorkorder.setCreatorName(sysOrgUserByAccount.getName());
		workmanageWorkorder.setCreatorOrgId(providerOrgId);
		
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
		tasktracerecord.setHandler(creator);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("创建工单");
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【故障描述】"+urgentrepairWorkorder.getFaultDescription());
		//派发对象
		List<String> participantList = new ArrayList<String>();
		participantList.add(creator);
		// 创建工单号
		String woId = workManageService.createWorkOrder(WorkManageConstant.PROCESS_URGENTREPAIR_WORKORDER_CODE, workmanageWorkorder, participantList, workorderassnetresource,tasktracerecord);
		log.debug("启动创建工单流程。");
		//更新工单个性表
		long lwoId = Long.parseLong(workorderid+"");
		UrgentrepairWorkorder updatewo = urgentRepairWorkOrderDao.getUrgentrepairWorkorderById(lwoId);
		if(updatewo==null){
			log.error("txCreateUrgentRepairWorkOrder 调用 urgentRepairWorkOrderDao.getUrgentrepairWorkorderById()，找不到主键=="+lwoId+"的工单个性表。");
			throw new UserDefinedException("找不到主键=="+lwoId+"的工单个性表。");
		}
		updatewo.setWoId(woId);
		urgentRepairWorkOrderDao.updateUrgentRepairWorkOrder(updatewo);
		//更新客户工单
		if(customerworkorderid!=null){
			long lcwoId = Long.parseLong(customerworkorderid+"");
			UrgentrepairCustomerworkorder updatecwo = urgentRepairCustomerWorkOrderDao.getUrgentrepairCustomerWorkorderById(lcwoId);
			if(updatecwo==null){
				log.error("txCreateUrgentRepairWorkOrder 调用 urgentRepairCustomerWorkOrderDao.getUrgentrepairCustomerWorkorderById()，找不到主键=="+lcwoId+"的客户工单个性表。");
				throw new UserDefinedException("找不到主键=="+lcwoId+"的客户工单个性表。");
			}
			updatecwo.setWoId(woId);
			urgentRepairCustomerWorkOrderDao.updateUrgentRepairCustomerWorkOrder(updatecwo);
			//保存IOSM工单与Eoms工单关系
			WorkorderinterfaceWangyouWorkorderRelation wr = new WorkorderinterfaceWangyouWorkorderRelation();
			wr.setIosmWoId(woId);
			if(urgentrepairCustomerworkorder.getCustomerWoId()!=null){
				wr.setCustomerWoId(urgentrepairCustomerworkorder.getCustomerWoId());
				workorderinterfaceWangyouWorkorderRelationService.saveWorkorderinterfaceWangyouWorkorderRelation(wr);
				log.debug("保存IOSM工单与Eoms工单（客户工单）的关系");
			}else{
				log.error("客户工单没单号，保存IOSM工单与Eoms工单（客户工单）的关系失败。");
				throw new UserDefinedException("客户工单没单号，保存IOSM工单与Eoms工单（客户工单）的关系失败。");
			}
			
		}
		return woId;

	}
	
	/**
	 * 更改工单信息
	 * 一般都是从网优之家对接回来的工单，发现若信息不全，则使用更改工单功能将信息补全
	 * @param woId
	 * @param urgentrepairWorkorder
	 * @param workorderassnetresource
	 */
	public String txModifytUrgentRepairWorkOrder(String woId,UrgentrepairWorkorder urgentrepairWorkorder,Workorderassnetresource workorderassnetresource){
		List<UrgentrepairWorkorder> woList =  urgentRepairWorkOrderDao.getUrgentrepairWorkorderByWoId(woId);
		if(woList == null || woList.isEmpty()){
			log.error("更新工单个性表，调用 urgentRepairWorkOrderDao.getUrgentrepairWorkorderByWoId()，找不到主键=="+woId+"的工单个性表。");
			throw new UserDefinedException("找不到主键=="+woId+"的工单个性表。");
		}
		//工单实例信息
		UrgentrepairWorkorder  wo = woList.get(0);
		wo.setBaseStationLevel(urgentrepairWorkorder.getBaseStationLevel());
		wo.setFaultArea(urgentrepairWorkorder.getFaultArea());
		wo.setFaultLevel(urgentrepairWorkorder.getFaultLevel());
		wo.setFaultType(urgentrepairWorkorder.getFaultType());
		wo.setAcceptProfessional(urgentrepairWorkorder.getAcceptProfessional());
		wo.setNetElementName(urgentrepairWorkorder.getNetElementName());
		wo.setStationName(urgentrepairWorkorder.getStationName());
		wo.setFaultStationAddress(urgentrepairWorkorder.getFaultStationAddress());
		wo.setFaultStationName(urgentrepairWorkorder.getFaultStationName());
		wo.setFaultDescription(urgentrepairWorkorder.getFaultDescription());
		wo.setLatestAllowedTime(urgentrepairWorkorder.getLatestAllowedTime());
		wo.setFaultOccuredTime(urgentrepairWorkorder.getFaultOccuredTime());
		wo.setWoId(woId);
		
		//更新工单信息
		urgentRepairWorkOrderDao.updateUrgentRepairWorkOrder(wo);
		
		List<Workorderassnetresource> waList = workOrderAssnetResourceDao.getWorkOrderAssnetResourceRecordDao("woId", woId);
		if(waList == null || waList.isEmpty()){
			log.error("更新工单关联资源表，调用 workOrderAssnetResourceDao.getWorkOrderAssnetResourceRecordDao，找不到主键=="+woId+"的工单关联资源表数据。");
			throw new UserDefinedException("找不到woId=="+woId+"的工单关联资源表。");
		}
		
		//网络资源关联工单表
		Workorderassnetresource wa = waList.get(0);
		wa.setNetworkResourceId(workorderassnetresource.getNetworkResourceId());
		wa.setNetworkResourceType(workorderassnetresource.getNetworkResourceType());
		wa.setStationId(workorderassnetresource.getStationId());
		wa.setWoId(woId);
		workorderassnetresource.setId(wa.getId());
		workorderassnetresource.setWoId(woId);
		//更新网络资源关联工单表
		workOrderAssnetResourceDao.updateWorkOrderAssnetResourceDao(wa);
		
		return "success";
		
	}

	/**
	 * 受理工单
	 * 创建故障抢修工单后，必须先对其受理才能进行后续业务功能
	 * 若果是网优之家对接过来的工单，受理工单后需要调用网优之家接口更新网优之家工单的状态信息
	 * @param accept
	 *            受理人
	 * @param woId
	 *            工单号
	 * @param urgentrepairWorkorder
	 *            工单实例
	 */
	public String txAcceptUrgentRepairWorkOrder(String acceptor, String woId,
			UrgentrepairWorkorder urgentrepairWorkorder) {
		String result = "success";
		// 判断传入任务单号是否为空
		if (woId == null || "".equals(woId)) {
			throw new UserDefinedException("参数 woId 为空。");
		}
		//获取当前处理人
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(acceptor);
//		Staff staff = providerOrganizationService.getStaffByAccount(acceptor);
		if(sysOrgUserByAccount==null){
			throw new UserDefinedException("找不到账号=="+acceptor+"的员工。");
		}
		
		// 根据工单号获取工单实例
		List<UrgentrepairWorkorder> wos = urgentRepairWorkOrderDao
				.getUrgentrepairWorkorderByWoId(woId);
		UrgentrepairWorkorder wo = new UrgentrepairWorkorder();
		if (wos != null && !wos.isEmpty()) {
			wo = wos.get(0);
		}else{
			throw new UserDefinedException("找不到对应单号=="+woId+"的工单实例。");
		}
		if (urgentrepairWorkorder == null) {
			throw new UserDefinedException("参数 类urgentrepairWorkorder 为空。");			
		}
		String workOrderAcceptedComment = "";
		if (urgentrepairWorkorder.getWorkOrderAcceptedComment() != null) {
			workOrderAcceptedComment = urgentrepairWorkorder
					.getWorkOrderAcceptedComment();
		}
		wo.setWorkOrderAcceptedComment(workOrderAcceptedComment);
		wo.setWorkOrderAcceptedPeople(acceptor);
		wo.setWorkOrderAcceptedTime(new Date());
		// 调用DAO更新工单方法
		urgentRepairWorkOrderDao.updateUrgentRepairWorkOrder(wo);
		log.info("更新工单号=="+woId+"的工单");
		//服务跟踪记录
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
		tasktracerecord.setHandler(acceptor);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("受理工单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【受理意见】"+urgentrepairWorkorder.getWorkOrderAcceptedComment());
		
		//获取当前者的所在组织
		long providerOrgId = 0;
		log.info("获取账号=="+acceptor+"担任任务调度员角色所在组织。");
		//List<ProviderOrganization> providerOrganizations = providerOrganizationService.getOrgByAccountAndRoleCode(acceptor, BizAuthorityConstant.ROLE_TASKDISPATCHER);
		//yuan.yw
		List<Map<String,Object>> providerOrganizations = this.sysOrganizationService.getOrgByAccountAndRoleCode(acceptor,BizAuthorityConstant.ROLE_TASKDISPATCHER );
		
		if(providerOrganizations!=null&&providerOrganizations.size()>0){
			providerOrgId = Long.parseLong(providerOrganizations.get(0).get("orgId")+"");
			if(providerOrganizations.size()>1){
				log.warn("获取账号=="+acceptor+"担任任务调度员角色所在组织有多个。sroviderOrganizations.size()>1");
				
			}
		}else {
			throw new UserDefinedException("找不到账号=="+acceptor+"担任任务调度员角色所在组织");
		}
		
		//保存工单当前处理人以及其所属组织
		WorkmanageWorkorder workmanageWorkorder = workManageService.getWorkOrderEntity(woId);
		workmanageWorkorder.setOrderOwner(acceptor);
		workmanageWorkorder.setOrderOwnerOrgId(providerOrgId);
		workManageService.updateWorkmanageWorkorder(workmanageWorkorder);
		
		boolean flag = false;
		flag = this.workManageService.acceptWorkOrder(woId, acceptor,tasktracerecord);
		log.info("启动受理工单流程。");
		if(flag){
			//调用网优之家的受理接口，对工单进行受理
			if(wo.getCustomerWoType()!=null){
				if("home2g".equals(wo.getCustomerWoType())||"homeTd".equals(wo.getCustomerWoType())){
					if(!workOrderCommonService.callWebServerAcceptWorkOrder(woId, wo.getCustomerWoType())){
						//业务定义，若果调用失败，则系统告知用户，需要手动去网优之家受理工单
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
	 * 工单阶段回复
	 * 记录反馈工单受理后至结束前的一些信息
	 * 若果是网优之家对接过来的工单，阶段回复工单后需要调用网优之家接口更新网优之家工单的状态信息
	 * @param actor
	 * @param woId
	 * @param stepReply_desc
	 * @param stepReply_processingProgress
	 * @param stepReply_faultType
	 * @param stepReply_faultResult
	 * @return
	 */
	public String txStepReplyUrgentRepairWorkOrder(String actor,String woId,String stepReply_desc,String stepReply_processingProgress,String stepReply_faultType,String stepReply_faultResult,String customerWoType){
		String result = "success";
		//获取阶段回复人的实例
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(actor);
//		Staff staff = providerOrganizationService.getStaffByAccount(actor);
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		if(sysOrgUserByAccount!=null){
			if(sysOrgUserByAccount.getName()!=null){
				tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
			}
		}
		//阶段回复的故障大类
		if(stepReply_faultType!=null&&!"".equals(stepReply_faultType)){
			TreeNode tn1 = dataDictionaryService.getDictionaryByTreeNodeIdService(Long.parseLong(stepReply_faultType));
			if(tn1!=null){
				stepReply_faultType = tn1.getReferenceValue();
			}
		}
		//阶段回复的故障原因
		if(stepReply_faultResult!=null&&!"".equals(stepReply_faultResult)){
			TreeNode tn2 = dataDictionaryService.getDictionaryByTreeNodeIdService(Long.parseLong(stepReply_faultResult));
			if(tn2!=null){
				stepReply_faultResult = tn2.getReferenceValue();
			}
		}
		
		tasktracerecord.setHandler(actor);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("阶段回复工单");
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setWoId(woId);
		if(stepReply_desc==null||"".equals(stepReply_desc)){
			stepReply_desc = "无";
		}
		
		String contentStr = "";
		//由于网友之家与其他工单的阶段回复处理意见内容格式不一样，需要判断是否为网优之家的工单
		if(customerWoType!=null){
			if("home2g".equals(customerWoType)||"homeTd".equals(customerWoType)){
				//网优之家工单
				contentStr = "【处理进度】"+stepReply_processingProgress+"；【故障大类】"+stepReply_faultType+"；【故障原因】"+stepReply_faultType+"；【处理意见】"+stepReply_desc;
			}else{
				contentStr = "【处理意见】"+stepReply_desc;
			}
		}else{
			contentStr = "【处理意见】"+stepReply_desc;
		}
		tasktracerecord.setHandleResultDescription(contentStr);
		//保存阶段回复信息
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
			//调用网优之家阶段回复接口
			if(wo.getCustomerWoType()!=null){
				if("home2g".equals(wo.getCustomerWoType())||"homeTd".equals(wo.getCustomerWoType())){
					if(!workOrderCommonService.callWebServerStepReplyWorkOrder(woId, wo.getCustomerWoType(), stepReply_processingProgress, stepReply_faultType, stepReply_faultResult)){
						//业务定义，若果调用失败，则系统告知用户，需要手动去网优之家进行阶段性的记录工单进度信息
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
	 * 工单最终回复
	 * 若果是网优之家对接过来的工单，回复工单后需要调用网优之家接口更新网优之家工单的状态信息
	 * @param reply
	 * @param woId
	 * @param urgentrepairWorkorder
	 */
	public String txFinalReplyUrgentRepairWorkOrder(String reply, String woId,
			UrgentrepairWorkorder urgentrepairWorkorder) {
		
		String result= "success";
		
		// 判断传入工单号是否为空
		if (woId == null || "".equals(woId)) {
			throw new UserDefinedException("参数 woId 为空。");
		}
		if (urgentrepairWorkorder == null) {
			throw new UserDefinedException("参数 类urgentrepairWorkorder 为空。");
		}
		//通过数据字典故障原因
		String faultCause =  urgentrepairWorkorder.getFaultCause();
		if(faultCause!=null&&!"".equals(faultCause)){
			TreeNode tn1 = dataDictionaryService.getDictionaryByTreeNodeIdService(Long.parseLong(faultCause));
			if(tn1!=null){
				faultCause = tn1.getTreeNodeName();
				urgentrepairWorkorder.setFaultCause(faultCause);
			}
		}
		//通过数据字典故障大类
		String faultGenera =  urgentrepairWorkorder.getFaultGenera();
		if(faultGenera!=null&&!"".equals(faultGenera)){
			TreeNode tn2 = dataDictionaryService.getDictionaryByTreeNodeIdService(Long.parseLong(faultGenera));
			if(tn2!=null){
				faultGenera = tn2.getTreeNodeName();
				urgentrepairWorkorder.setFaultGenera(faultGenera);
			}
		}
		
		
		
		//判断子任务是否结束,若有未结束的子任务，系统自动回复并结束该任务，子任务包括现场任务以及技术支援任务
		if(!workOrderCommonService.hasAllSubTasksFinishedByWoId(woId)){
			//先获取工单下的所有任务单，包括现场任务以及技术支援任务
			List<Map> orderList = workManageService.getTaskOrderListByWoId(woId);
			if(orderList!=null&&!orderList.isEmpty()){
				for(Map toMap : orderList){
					if(toMap.get("status") != null&&"11".equals(toMap.get("status"))){
						continue;
					}
					if(toMap.get("toId") == null){
						throw new UserDefinedException("工单的子任务没任务单号。");
					}
					String toId = toMap.get("toId").toString();
					
					if(toMap.get("bizProcessName")==null){
						throw new UserDefinedException("无法判断toId=="+toId+"的任务单类型。");
					}
					if(toMap.get("currentHandlerName")==null|| toMap.get("currentHandler")==null){
						throw new UserDefinedException("toId=="+toId+"的任务单没保存当前处理人。");
					}
					
					String statusName = toMap.get("statusName").toString();
					String bizProcessName = toMap.get("bizProcessName").toString();
					String currentHandler = toMap.get("currentHandler").toString();
					String currentHandlerName = toMap.get("currentHandlerName").toString();
					//关闭现场任务
					if("现场任务单".equals(bizProcessName)){
						//只针对状态为非“已撤销”和非“已结束”的现场任务做处理
						if(!"已撤销".equals(statusName)&&!"已结束".equals(statusName)){
							//若现场任务下的子任务，即技术支援任务还没全部结束，则先关闭未结束的技术支援任务
							if(!workOrderCommonService.hasAllSubTasksFinishedByToId(toId)){
								List<Map> subOrderList = workManageService.getChildTaskOrderListByToId(toId);
								if(subOrderList!=null && !subOrderList.isEmpty()){
									for(Map subOrderMap : subOrderList){
										if(subOrderMap.get("status") != null&&"11".equals(subOrderMap.get("status"))){
											continue;
										}
										if(subOrderMap.get("toId") == null){
											throw new UserDefinedException("现场任务的子任务没任务单号。");
										}
										String subToId = subOrderMap.get("toId").toString();
										
										if(subOrderMap.get("bizProcessName")==null){
											throw new UserDefinedException("无法判断toId=="+toId+"的任务单类型。");
										}
										if(subOrderMap.get("currentHandlerName")==null|| toMap.get("currentHandler")==null){
											throw new UserDefinedException("toId=="+toId+"的任务单没保存当前处理人。");
										}
										
										String subCurrentHandler = subOrderMap.get("currentHandler").toString();
										String subCurrentHandlerName = subOrderMap.get("currentHandlerName").toString();
										//关闭未结束或未撤销的技术支援任务
										finishTechSupportTask(woId,subToId ,subCurrentHandler, subCurrentHandlerName ,urgentrepairWorkorder,statusName);
									}
								}
							}
							//当现场任务的子任务全部结束了，关闭所有未结束或未撤销的现场任务
							finishSceneTask(woId,toId ,currentHandler, currentHandlerName ,urgentrepairWorkorder,statusName);
						}
						
						
					}else 
						//关闭技术支援任务
						if("专家任务单".equals(bizProcessName)){
						if(!"已撤销".equals(statusName)&&!"已结束".equals(statusName)){
							//关闭未结束或未撤销的技术支援任务
							finishTechSupportTask(woId,toId ,currentHandler, currentHandlerName ,urgentrepairWorkorder,statusName);
						}
					}
					
				}
			}
		}

		// 根据工单号获取工单实例
		List<UrgentrepairWorkorder> wos = urgentRepairWorkOrderDao
				.getUrgentrepairWorkorderByWoId(woId);
		UrgentrepairWorkorder wo = new UrgentrepairWorkorder();
		if (wos != null && !wos.isEmpty()) {
			wo = wos.get(0);
		}else{
			throw new UserDefinedException("找不到工单号=="+woId+"的工单。");
		}
		wo.setFaultCause(urgentrepairWorkorder.getFaultCause());
		wo.setFaultGenera(urgentrepairWorkorder.getFaultGenera());
		wo.setHowToDeal(urgentrepairWorkorder.getHowToDeal());
		wo.setResonForDelayApply(urgentrepairWorkorder.getResonForDelayApply());
		wo.setIsAlarmClear(urgentrepairWorkorder.getIsAlarmClear());
		wo.setAffectedServiceName(urgentrepairWorkorder.getAffectedServiceName());
		wo.setSideeffectService(urgentrepairWorkorder.getSideeffectService());
		wo.setFaultDealResult(urgentrepairWorkorder.getFaultDealResult());
		//告警消除时间
		if("0".equals(urgentrepairWorkorder.getIsAlarmClear()+"")){
			wo.setAlarmClearTime(null);
		}else{
			if(urgentrepairWorkorder.getAlarmClearTime()!=null){
				Date alarmClearTime =  TimeFormatHelper.setTimeFormat(urgentrepairWorkorder.getAlarmClearTime());
				wo.setAlarmClearTime(alarmClearTime);
			}else{
				wo.setAlarmClearTime(new Date());
			}
		}
		
		
		if("1".equals(wo.getFaultDealResult())){
			List<Map> commonwo = workManageService.getChildTaskOrderListByWoId(woId);
			//故障解决时间
			if(commonwo == null || commonwo.isEmpty()){
				wo.setFaultSolveTime(new Date());
			}else{
				if(commonwo.get(0).get("faultSolveTime")!=null){
					Date faultSolveTime =  TimeFormatHelper.setTimeFormat(commonwo.get(0).get("faultSolveTime"));
					wo.setFaultSolveTime(faultSolveTime);
				}else{
					wo.setFaultSolveTime(new Date());
				}
			}
			
		}else{
			//预计解决时间
			if(urgentrepairWorkorder.getForeseeResolveTime()!=null){
				Date foreseeSolveTime =  TimeFormatHelper.setTimeFormat(urgentrepairWorkorder.getForeseeResolveTime());
				wo.setForeseeResolveTime(foreseeSolveTime);
			}
		}
		
		
		// 调用DAO更新工单方法
		log.info("更新工单号=="+woId+"的工单。");
		urgentRepairWorkOrderDao.updateUrgentRepairWorkOrder(wo);

		//服务跟踪记录
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(reply);
//		Staff staff = providerOrganizationService.getStaffByAccount(reply);
		if(sysOrgUserByAccount==null){
			throw new UserDefinedException("找不到账号=="+reply+"的员工。");
		}
		//服务跟踪记录
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setWoId(woId);
		tasktracerecord.setWoType("抢修工单");
		String faultHandleResult="无";
		String howToDeale = "无";
		if("1".equals(urgentrepairWorkorder.getFaultDealResult())){
			faultHandleResult ="已解决";
			if(urgentrepairWorkorder.getHowToDeal()!=null){
				howToDeale = urgentrepairWorkorder.getHowToDeal();
			}
		}else if("0".equals(urgentrepairWorkorder.getFaultDealResult())){
			faultHandleResult ="未解决";
		}
		tasktracerecord.setHandleResultDescription("【最终回复】处理措施："+howToDeale+"；处理结果："+faultHandleResult);
		tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
		tasktracerecord.setHandleWay("最终回复工单");
		boolean flag = false;
		log.info("启动最终回复工单流程。");
		flag = this.workManageService.finishWorkOrder(woId,reply,tasktracerecord);
		
		if(flag){
			//调用网优之家最终回复工单接口
			if(wo.getCustomerWoType()!=null){
				if("home2g".equals(wo.getCustomerWoType())||"homeTd".equals(wo.getCustomerWoType())){
					if(!workOrderCommonService.callWebServerReplyWorkOrder(woId,wo,reply)){
						//业务定义，若果调用接口失败，则系统告知用户，需要手动去网优之家进行工单的最终回复
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
	 * 结束现场任务（最终回复工单调用）
	 * @param woId
	 * @param toId
	 * @param currentHandler
	 * @param currentHandlerName
	 * @param urgentrepairWorkorder
	 */
	private void finishSceneTask(String woId,String toId ,String currentHandler, String currentHandlerName ,UrgentrepairWorkorder urgentrepairWorkorder,String statusName){
		// 根据现场任务单号获取任务单实例
		List<UrgentrepairSencetaskorder> stos = urgentRepairSenceTaskOrderDao.getUrgentRepairSenceTaskOrderByToId(toId);
		UrgentrepairSencetaskorder sto = new UrgentrepairSencetaskorder();
		if(stos != null && ! stos.isEmpty()){
			sto = stos.get(0);
		}else{
			throw new UserDefinedException("工单最终回复时，找不到现场任务单号=="+toId+"的任务单。");
		}
		sto.setFaultReason(urgentrepairWorkorder.getFaultCause());
		sto.setFaultGenera(urgentrepairWorkorder.getFaultGenera());
		sto.setFaultHandleDetail("【调度员代填任务】"+urgentrepairWorkorder.getHowToDeal());
		sto.setReasonForDelayApply(urgentrepairWorkorder.getResonForDelayApply());
		sto.setIsAlarmClear(urgentrepairWorkorder.getIsAlarmClear());
		sto.setAffectedServiceName(urgentrepairWorkorder.getAffectedServiceName());
		sto.setSideeffectService(urgentrepairWorkorder.getSideeffectService());
		sto.setFaultHandleResult(urgentrepairWorkorder.getFaultDealResult());
		//告警消除时间
		if("0".equals(urgentrepairWorkorder.getIsAlarmClear()+"")){
			sto.setAlarmClearTime(null);
		}else{
			if(urgentrepairWorkorder.getAlarmClearTime()!=null){
				Date alarmClearTime =  TimeFormatHelper.setTimeFormat(urgentrepairWorkorder.getAlarmClearTime());
				sto.setAlarmClearTime(alarmClearTime);
			}else{
				sto.setAlarmClearTime(new Date());
			}
		}
		
		
		if("1".equals(urgentrepairWorkorder.getFaultDealResult())){
			List<Map> commonwo = workManageService.getChildTaskOrderListByWoId(woId);
			//故障解决时间
			if(commonwo == null || commonwo.isEmpty()){
				sto.setFaultSolveTime(new Date());
			}else{
				if(commonwo.get(0).get("faultSolveTime")!=null){
					Date faultSolveTime =  TimeFormatHelper.setTimeFormat(commonwo.get(0).get("faultSolveTime"));
					sto.setFaultSolveTime(faultSolveTime);
				}else{
					sto.setFaultSolveTime(new Date());
				}
			}
			
		}else{
			//预计解决时间
			if(urgentrepairWorkorder.getForeseeResolveTime()!=null){
				Date foreseeSolveTime =  TimeFormatHelper.setTimeFormat(urgentrepairWorkorder.getForeseeResolveTime());
				sto.setForeseeSolveTime(foreseeSolveTime);
			}
		}
		
		
		// 调用DAO更新工单方法
		log.info("更新任务单号=="+toId+"的现场任务单。");
		urgentRepairSenceTaskOrderDao.updateUrgentRepairSenceTaskOrder(sto);
		
		//现场任务服务跟踪记录
		//服务跟踪记录
		Tasktracerecord aTasktracerecord = new Tasktracerecord();
		aTasktracerecord.setHandleTime(new Date());
		aTasktracerecord.setWoId(woId);
		aTasktracerecord.setWoType("抢修工单");
		aTasktracerecord.setHandlerName(currentHandlerName);
		aTasktracerecord.setHandler(currentHandler);
		aTasktracerecord.setToId(toId);
		aTasktracerecord.setHandleResultDescription("【调度员代填任务】【受理意见】已受理");
		aTasktracerecord.setHandleWay("受理现场任务单");
		WorkmanageTaskorder taskOrder = new WorkmanageTaskorder();
		taskOrder.setAcceptComment("已受理");
		taskOrder.setAcceptPeople(currentHandler);
		taskOrder.setAcceptPeopleName(currentHandlerName);
		taskOrder.setAcceptTime(new Date());
		taskOrder.setToId(toId);
		if(statusName.equals("待受理")){
			if(!workManageService.acceptTaskOrder(taskOrder, currentHandler, aTasktracerecord)){
				return;
			}
		}
		Tasktracerecord sTasktracerecord = new Tasktracerecord();
		sTasktracerecord.setHandleTime(new Date());
		sTasktracerecord.setWoId(woId);
		sTasktracerecord.setWoType("抢修工单");
		String s_faultHandleResult="无";
		String s_howToDeale = "无";
		if("1".equals(urgentrepairWorkorder.getFaultDealResult())){
			s_faultHandleResult ="已解决";
			if(urgentrepairWorkorder.getHowToDeal()!=null){
				s_howToDeale = urgentrepairWorkorder.getHowToDeal();
			}
		}else if("0".equals(urgentrepairWorkorder.getFaultDealResult())){
			s_faultHandleResult ="未解决";
		}
		sTasktracerecord.setHandleResultDescription("【调度员代填任务】【最终回复】处理措施："+s_howToDeale+"；处理结果："+s_faultHandleResult);
		sTasktracerecord.setHandlerName(currentHandlerName);
		sTasktracerecord.setHandler(currentHandler);
		sTasktracerecord.setToId(toId);
		sTasktracerecord.setHandleWay("最终回复现场任务单");
		//结束现场任务工作流，最终回复 
		workManageService.finishTaskOrder(toId, currentHandler, sTasktracerecord);
		
	}
	
	/**
	 * 结束专家单（最终回复工单调用）
	 * @param woId
	 * @param toId
	 * @param currentHandler
	 * @param currentHandlerName
	 * @param urgentrepairWorkorder
	 */
	private void finishTechSupportTask(String woId,String toId ,String currentHandler, String currentHandlerName ,UrgentrepairWorkorder urgentrepairWorkorder,String statusName){
		// 根据现专家务单号获取任务单实例
		List<UrgentrepairTechsupporttaskorder> ttos = urgentRepairTechSupportTaskOrderDao.getUrgentRepairTechSupportTaskOrderByToId(toId);
		UrgentrepairTechsupporttaskorder tto = new UrgentrepairTechsupporttaskorder();
		if(ttos != null && ! ttos.isEmpty()){
			tto = ttos.get(0);
		}else{
			throw new UserDefinedException("工单最终回复时，找不到现场任务单号=="+toId+"的任务单。");
		}
		tto.setFaultReason(urgentrepairWorkorder.getFaultCause());
		tto.setFaultGenera(urgentrepairWorkorder.getFaultGenera());
		tto.setFaultHandleDetail("【调度员代填任务】"+urgentrepairWorkorder.getHowToDeal());
		tto.setReasonForDelayApply(urgentrepairWorkorder.getResonForDelayApply());
		tto.setIsAlarmClear(urgentrepairWorkorder.getIsAlarmClear());
		tto.setAffectedServiceName(urgentrepairWorkorder.getAffectedServiceName());
		tto.setSideeffectService(urgentrepairWorkorder.getSideeffectService());
		tto.setFaultHandleResult(urgentrepairWorkorder.getFaultDealResult());
		//告警消除时间
		if("0".equals(urgentrepairWorkorder.getIsAlarmClear()+"")){
			tto.setAlarmClearTime(null);
		}else{
			if(urgentrepairWorkorder.getAlarmClearTime()!=null){
				Date alarmClearTime =  TimeFormatHelper.setTimeFormat(urgentrepairWorkorder.getAlarmClearTime());
				tto.setAlarmClearTime(alarmClearTime);
			}else{
				tto.setAlarmClearTime(new Date());
			}
		}
		
		
		if("1".equals(urgentrepairWorkorder.getFaultDealResult())){
			List<Map> commonwo = workManageService.getChildTaskOrderListByWoId(woId);
			//故障解决时间
			if(commonwo == null || commonwo.isEmpty()){
				tto.setFaultSolveTime(new Date());
			}else{
				if(commonwo.get(0).get("faultSolveTime")!=null){
					Date faultSolveTime =  TimeFormatHelper.setTimeFormat(commonwo.get(0).get("faultSolveTime"));
					tto.setFaultSolveTime(faultSolveTime);
				}else{
					tto.setFaultSolveTime(new Date());
				}
			}
			
		}else{
			//预计解决时间
			if(urgentrepairWorkorder.getForeseeResolveTime()!=null){
				Date foreseeSolveTime =  TimeFormatHelper.setTimeFormat(urgentrepairWorkorder.getForeseeResolveTime());
				tto.setForeseeSolveTime(foreseeSolveTime);
			}
		}
		
		
		// 调用DAO更新工单方法
		log.info("更新任务单号=="+toId+"的专家任务单。");
		urgentRepairTechSupportTaskOrderDao.updateUrgentRepairTechSupportTaskOrder(tto);
		//专家任务服务跟踪记录
		Tasktracerecord aTasktracerecord = new Tasktracerecord();
		aTasktracerecord.setHandleTime(new Date());
		aTasktracerecord.setWoId(woId);
		aTasktracerecord.setWoType("抢修工单");
		aTasktracerecord.setHandlerName(currentHandlerName);
		aTasktracerecord.setHandler(currentHandler);
		aTasktracerecord.setToId(toId);
		aTasktracerecord.setHandleResultDescription("【调度员代填任务】【受理意见】已受理");
		aTasktracerecord.setHandleWay("受理专家任务单");
		WorkmanageTaskorder taskOrder = new WorkmanageTaskorder();
		taskOrder.setAcceptComment("已受理");
		taskOrder.setAcceptPeople(currentHandler);
		taskOrder.setAcceptPeopleName(currentHandlerName);
		taskOrder.setAcceptTime(new Date());
		taskOrder.setToId(toId);
		if(statusName.equals("待受理")){
			if(workManageService.acceptTaskOrder(taskOrder, currentHandler, aTasktracerecord)){
				return;
			}
		}
		Tasktracerecord sTasktracerecord = new Tasktracerecord();
		sTasktracerecord.setHandleTime(new Date());
		sTasktracerecord.setWoId(woId);
		sTasktracerecord.setWoType("抢修工单");
		String s_faultHandleResult="无";
		String s_howToDeale = "无";
		if("1".equals(urgentrepairWorkorder.getFaultDealResult())){
			s_faultHandleResult ="已解决";
			if(urgentrepairWorkorder.getHowToDeal()!=null){
				s_howToDeale = urgentrepairWorkorder.getHowToDeal();
			}
		}else if("0".equals(urgentrepairWorkorder.getFaultDealResult())){
			s_faultHandleResult ="未解决";
		}
		sTasktracerecord.setHandleResultDescription("【调度员代填任务】【最终回复】处理措施："+s_howToDeale+"；处理结果："+s_faultHandleResult);
		sTasktracerecord.setHandlerName(currentHandlerName);
		sTasktracerecord.setHandler(currentHandler);
		sTasktracerecord.setToId(toId);
		sTasktracerecord.setHandleWay("最终回复专家任务单");
		workManageService.finishTaskOrder(toId, currentHandler, sTasktracerecord);
	}
	

	/**
	 * 派发现场任务单
	 * 
	 * @param sendor
	 * @param recipient
	 * @param woId
	 * @param toId
	 * @param urgentrepairSencetaskorder
	 */
	public void txCreateUrgentRepairSceneTaskOrder(String sendor,
			String recipient,String woId,WorkmanageTaskorder workmanageTaskorder) {
		// 判断传入任务单号是否为空
		if (woId == null || "".equals(woId)) {
			throw new UserDefinedException("参数 woId 为空。");
		}
		//获取当前处理人
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(sendor);
//		Staff staff = providerOrganizationService.getStaffByAccount(sendor);
		if(sysOrgUserByAccount==null){
			throw new UserDefinedException("找不到账号=="+sendor+"的员工。");
		}
		
		//获取接收对象
		//ou.jh
		SysOrgUser sysOrgUserByAccount2 = this.sysOrgUserService.getSysOrgUserByAccount(recipient);
//		Staff rStaff = providerOrganizationService.getStaffByAccount(recipient);
		if(sysOrgUserByAccount2==null){
			throw new UserDefinedException("找不到账号=="+recipient+"的员工。");
		}
		// 判断工单实例是否为空
		UrgentrepairSencetaskorder urgentrepairSencetaskorder = new UrgentrepairSencetaskorder();
		log.info("保存现场任务单个性表。");
		Serializable stoid = urgentRepairSenceTaskOrderDao
				.saveUrgentRepairSenceTaskOrder(urgentrepairSencetaskorder);
		
		
		//指定对象
		List<String> participantList = new ArrayList<String>();
		participantList.add(recipient);
		//获取创建者的所在组织
		long providerOrgId = 0;
		log.info("获取账号=="+sendor+"担任任务调度员角色所在组织。");
		//List<ProviderOrganization> providerOrganizations = providerOrganizationService.getOrgByAccountAndRoleCode(sendor, BizAuthorityConstant.ROLE_TASKDISPATCHER);
		//yuan.yw
		List<Map<String,Object>> providerOrganizations = this.sysOrganizationService.getOrgByAccountAndRoleCode(sendor,BizAuthorityConstant.ROLE_TASKDISPATCHER );
		
		if(providerOrganizations!=null&&providerOrganizations.size()>0){
			providerOrgId = Long.parseLong(providerOrganizations.get(0).get("orgId")+"");
			if(providerOrganizations.size()>1){
				log.warn("获取账号=="+sendor+"担任任务调度员角色所在组织有多个。sroviderOrganizations.size()>1");
				
			}
		}else {
			throw new UserDefinedException("找不到账号=="+sendor+"担任任务调度员角色所在组织");
		}
		
		//工单当前处理人所属组织ID
		long currentHandlerOrgId = 0;
		//List<ProviderOrganization> currentHandlerOrgs = providerOrganizationService.getOrgByAccountAndOrgType(recipient, OrganizationConstant.ORGANIZATION_MAINTENANCETEAM);
		List<SysOrg> currentHandlerOrgs = this.sysOrganizationService.getOrgByAccountAndOrgType(recipient, OrganizationConstant.ORGANIZATION_MAINTENANCETEAM);
		if(currentHandlerOrgs!=null&&currentHandlerOrgs.size()>0){
			currentHandlerOrgId = currentHandlerOrgs.get(0).getOrgId();
			if(currentHandlerOrgs.size()>1){
				log.warn("获取账号=="+recipient+"所在维护队组织有多个。currentHandlerOrgs.size()>1");
				
			}
		}else {
			throw new UserDefinedException("找不到账号=="+recipient+"担任任务调度员角色所在组织");
		}
		
		
		//公共任务单属性
		workmanageTaskorder.setToType("抢修");
		workmanageTaskorder.setAssigner(sendor);
		workmanageTaskorder.setAssignerName(sysOrgUserByAccount.getName());
		workmanageTaskorder.setAssignerOrgId(providerOrgId);
		workmanageTaskorder.setCurrentHandlerOrgId(currentHandlerOrgId);
		
		//服务跟踪记录
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		tasktracerecord.setHandlerName(sysOrgUserByAccount.getName());
		tasktracerecord.setHandler(sendor);
		tasktracerecord.setHandleTime(new Date());
		tasktracerecord.setHandleWay("派发现场任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【派发意见】"+workmanageTaskorder.getAssignComment());
		log.info("派发现场任务单，启动工作流，生成现场任务单号。");
		String toId = workManageService.assignTaskOrder(WorkManageConstant.PROCESS_URGENTREPAIR_SCENE_CODE, woId, woId, workmanageTaskorder, participantList,tasktracerecord);
		//更新任务单表
		long lstoid = Long.parseLong(stoid+"");
		UrgentrepairSencetaskorder sto = urgentRepairSenceTaskOrderDao.getUrgentRepairSenceTaskOrderById(lstoid);
		if(sto == null){
			throw new UserDefinedException("找不到主键=="+lstoid+"的现场任务单。");
		}
		
		sto.setToId(toId);
		log.info("将生成的现场任务单号=="+toId+" 更新至现场任务单个性表中。");
		urgentRepairSenceTaskOrderDao.updateUrgentRepairSenceTaskOrder(sto);
		
		//保存工单当前处理人以及其所属组织
		WorkmanageWorkorder workmanageWorkorder = workManageService.getWorkOrderEntity(woId);
		workmanageWorkorder.setOrderOwner(recipient);
		workmanageWorkorder.setOrderOwnerOrgId(currentHandlerOrgId);
		workManageService.updateWorkmanageWorkorder(workmanageWorkorder);
		
		log.info("派发现场任务后，更改工单状态为‘派发中’。");
		boolean flag = workManageService.updateWorkOrderStatus(woId, WorkManageConstant.WORKORDER_ASSIGNED);
		if(flag){
			//成功派发现场任务后，需邮件通知接收者
			Map woTemp = workManageService.getWorkOrderForShow(woId);
			String woTitle = "";
			if(woTemp!=null){
				woTitle = woTemp.get("woTitle").toString();
			}
			Map emailInfoMap = new HashMap();
			String remaininTime = "";
			//判断任务是否已超时
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
				emailInfoMap.put("cellphone", sysOrgUserByAccount2.getMobile());
				emailInfoMap.put("content", content);
				//发送邮件
				commonService.sendCMCCEmail(emailInfoMap);
			}
			
		}
		
	}
	

	/**
	 * 撤销任务
	 * 在派发任务的下级任务管理列表里，可以对工单下状态不为“已撤销”或“已结束”的子任务作撤销操作，
	 * @param actor
	 * @param woId
	 * @param toId
	 * @param workmanageTaskorder
	 */
	public boolean txCancelUrgentRepairSceneTaskOrder(String actor,String woId,String toId,
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
		tasktracerecord.setHandleWay("撤销现场任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【撤销意见】"+workmanageTaskorder.getCancelComment());
		boolean flag = false;
		try {
			log.info("启动撤销现场任务流程。");
			workmanageTaskorder.setCancelTime(new Date());
			workmanageTaskorder.setToId(toId);
			flag = this.workManageService.cancelTaskOrder(workmanageTaskorder,actor,tasktracerecord);
			if(flag){
				//判断工单的子任务是否全部结束或撤销
				boolean woSubIsFinish = workOrderCommonService.hasAllSubTasksFinishedByWoId(woId, toId);
				if(woSubIsFinish){
					//更改工单状态为“处理中”
					if(workManageService.updateWorkOrderStatus(woId, WorkManageConstant.WORKORDER_HANDLING)){
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
	 * 在派发任务的下级任务管理列表里，可以对工单下的状态为“待撤销”的子任务作重派操作
	 * @param actor
	 * @param woId
	 * @param toId
	 * @param workmanageTaskorder
	 */
	public boolean txReAssignUrgentRepairSceneTaskOrder(String actor,String woId,String toId,
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
		tasktracerecord.setHandleWay("重派现场任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【重派意见】"+workmanageTaskorder.getReassignComment());
		boolean flag = false;
		try {
			log.info("启动重派现场任务流程。");
			workmanageTaskorder.setReassignTime(new Date());
			workmanageTaskorder.setToId(toId);
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
	 * @param woId
	 * @param toId
	 * @param workmanageTaskorder
	 */
	public boolean txHastenUrgentRepairSceneTaskOrder(String actor,String woId,String toId,
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
		tasktracerecord.setHandleWay("催办现场任务单");
		tasktracerecord.setWoId(woId);
		tasktracerecord.setWoType("抢修工单");
		tasktracerecord.setHandleResultDescription("【催办任务】"+toId+"【催办意见】"+workmanageTaskorder.getUrgeWorkComment());
		boolean flag = workManageService.stepReply(tasktracerecord);
		if(flag){
			//消息盒子
			BizMessage bizMsg = new BizMessage();
			bizMsg.setContent(workmanageTaskorder.getUrgeWorkComment());
			bizMsg.setSendPerson(actor);
			bizMsg.setReceivePerson(receivePerson);
			bizMsg.setLink("op/urgentrepair/loadUrgentRepairSenceTaskOrderPageAction?WOID="+woId+"&TOID="+toId);
			bizMsg.setLinkForMobile("loadUrgentRepairSceneTaskOrderPageActionForMobile?WOID="+woId+"&TOID="+toId);
			bizMsg.setFunctionType("UrgentRepairSenceTaskOrder");
			bizMsg.setWoId(woId);
			bizMsg.setToId(toId);
			bizMsg.setSendTime(new Date());
			bizMsg.setTitle(toId);
			bizMsg.setType("抢修");
			//发送催办消息
			bizMessageService.txAddBizMessageService(bizMsg,"hasten");
		}
		
		return flag;
	}

	

	/**
	 * 根据工单号获取工单
	 * @param woId
	 */
	public UrgentrepairWorkorder getUrgentRepairWorkOrder(String woId) {
		List<UrgentrepairWorkorder> urgentrepairWorkorder = urgentRepairWorkOrderDao.getUrgentrepairWorkorderByWoId(woId);
		if(urgentrepairWorkorder==null||urgentrepairWorkorder.isEmpty()){
			return null;
		}
		return urgentrepairWorkorder.get(0);
	}
	
	/**
	 * 获取维护队长
	 * 派发现场任务单，需要根据登录人所在组织获取其下的维护队，维护队队长是派发对象
	 * @param providerOrgId
	 */
	public List<Map>  loadTeamLeadersInfoService(String providerOrgId){
		List<Map> teamLeaderLists = new ArrayList<Map>();
		if(providerOrgId == null || "".equals(providerOrgId)){
			log.debug("参数 providerOrgId 为空。");
			return null;
		}
		long orgId = Long.parseLong(providerOrgId);
		log.info("获取组织ID=="+providerOrgId+"的下级组织中所有维护队类型的组织。");
		//获取维护队列表
		//List<ProviderOrganization> providerOrganizations = providerOrganizationService.getOrgListDownwardByOrgTypeAndOrg(orgId, OrganizationConstant.ORGANIZATION_MAINTENANCETEAM);
		//yuan.yw
		List<Map<String,Object>> providerOrganizations = this.sysOrganizationService.getOrgListDownwardOrUpwardByOrgTypeAndOrgIdService(orgId, OrganizationConstant.ORGANIZATION_MAINTENANCETEAM,"child");
		// 获取队长List	
		for (int i = 0; i < providerOrganizations.size(); i++) {
			Map map = new HashMap();
			Map busyStatus = new HashMap();
			long teamId = Long.valueOf(providerOrganizations.get(i).get("orgId")+"");
			String team = providerOrganizations.get(i).get("name")+"";
			
//			busyStatus = staffBusyQueryService.queryBizUnitTaskCountByBizUnitId(teamId+"");
			
			//int totalCount=commonWfFrameService.getTeamPendingTaskOrderCountByBizunitId(teamId+"");
			
			//List<Staff> accountList = providerOrganizationService.getStaffListDownwardByRoleAndOrg(teamId, BizAuthorityConstant.ROLE_TRAMLEADER);
			//yuan.yw
			List<Map<String,Object>> accountList =this.sysOrganizationService.getProviderStaffByOrgIdAndRoleCode(teamId, BizAuthorityConstant.ROLE_TRAMLEADER, "downward");
		
			if(accountList!=null&&!accountList.isEmpty()){
				String accountId = accountList.get(0).get("ACCOUNT")+"";
				boolean flag = staffDutyService.checkIsDutyToday(accountId);
//				String teamleader = accountList.get(0).getCnName();
////				int sum = Integer.parseInt(busyStatus.get("totalTask").toString());
//				String dutyStatus = "是";
//				int ds = dutyForMobileService.getPersonDutyStatus(accountId);
//				if(ds==1){
//					dutyStatus = "是";
//				}else{
//					dutyStatus = "否" ;
//				}
//				//获取维护队任务数
				long count = workManageService.getTaskOrderCountByObjectTypeAndObjectId("people", accountId);
				if(flag){
					map.put("dutyStatus", "是");
				}else{
					map.put("dutyStatus", "否");
				}
				
				map.put("teamName", team);
				map.put("teamId", teamId);
				map.put("accountId", accountId);
				map.put("teamleader", accountList.get(0).get("NAME"));
				String phone = "";
				if(accountList.get(0).get("CELLPHONENUMBER")==null||"".equals(accountList.get(0).get("CELLPHONENUMBER"))||"null".equals(accountList.get(0).get("CELLPHONENUMBER"))){
					phone = "";
				}else{
					phone = accountList.get(0).get("CELLPHONENUMBER")+"";
				}
				map.put("phone", phone);
				map.put("sum", count);
				teamLeaderLists.add(map);
			}
			
		}
		
		return teamLeaderLists;
	}
	
//	/**
//	 * 获取调度员
//	 */
//	public List<Map>  loadDispatcherInfoService(String providerOrgId){
//		List<Map> teamerLists = new ArrayList<Map>();
//		if(providerOrgId == null || "".equals(providerOrgId)){
//			log.debug("参数 providerOrgId 为空。");
//			return null;
//		}
//		long orgId = Long.parseLong(providerOrgId);
//		log.info("获取组织ID=="+providerOrgId+"的上级组织中所有项目组类型的组织。");
//		List<ProviderOrganization> providerOrganizations = new ArrayList<ProviderOrganization>();
//		List<ProviderOrganization> temp1 = providerOrganizationService.getOrgListUpwardByOrgTypeAndOrg(orgId, OrganizationConstant.ORGANIZATION_PROJECTTEAM);
//		List<ProviderOrganization> temp2 = providerOrganizationService.getOrgListDownwardByOrgTypeAndOrg(orgId, OrganizationConstant.ORGANIZATION_PROJECTTEAM);
//		if(temp1!=null&&!temp1.isEmpty()){
//			providerOrganizations.addAll(temp1);
//		}
//		if(temp2!=null&&!temp2.isEmpty()){
//			providerOrganizations.addAll(temp2);
//		}
//		// 调度员List	
//		for (int i = 0; i < providerOrganizations.size(); i++) {
//			Map busyStatus = new HashMap();
//			long teamId = providerOrganizations.get(i).getId();
//			String team = providerOrganizations.get(i).getName();
////			//获取调度员任务数
////			busyStatus = staffBusyQueryService.queryBizUnitTaskCountByBizUnitId(teamId+"");
//			
//			//int totalCount=commonWfFrameService.getTeamPendingTaskOrderCountByBizunitId(teamId+"");
//			log.info("获取组织ID=="+teamId+"项目组下的任务调度员。");
//			List<Staff> accountList = providerOrganizationService.getStaffListDownwardByRoleAndOrg(teamId, BizAuthorityConstant.ROLE_TASKDISPATCHER);
//			if(accountList!=null&&!accountList.isEmpty()){
//				for(Staff staff : accountList){
//					Map map = new HashMap();
//					String accountId = staff.getAccount();
//					boolean flag = staffDutyService.checkIsDutyToday(accountId);
//					if(flag){
//						map.put("dutyStatus", "是");
//					}else{
//						map.put("dutyStatus", "否");
//					}
//					long count = workManageService.getTaskOrderCountByObjectTypeAndObjectId("people", accountId);
//					map.put("teamId", teamId);
//					map.put("accountId", accountId);
//					map.put("teamer",staff.getName());
//					String phone = "";
//					if(staff.getCellPhoneNumber()==null||"".equals(staff.getCellPhoneNumber())||"null".equals(staff.getCellPhoneNumber())){
//						phone = "";
//					}else{
//						phone = staff.getCellPhoneNumber();
//					}
//					map.put("phone", phone);
//					map.put("sum", count);
//					teamerLists.add(map);
//				}
//				
//			}
//			
//		}
//		
//		return teamerLists;
//	}
	
	
	
	
	public UrgentRepairWorkOrderDao getUrgentRepairWorkOrderDao() {
		return urgentRepairWorkOrderDao;
	}

	public void setUrgentRepairWorkOrderDao(
			UrgentRepairWorkOrderDao urgentRepairWorkOrderDao) {
		this.urgentRepairWorkOrderDao = urgentRepairWorkOrderDao;
	}

	public UrgentRepairCustomerWorkOrderDao getUrgentRepairCustomerWorkOrderDao() {
		return urgentRepairCustomerWorkOrderDao;
	}

	public void setUrgentRepairCustomerWorkOrderDao(
			UrgentRepairCustomerWorkOrderDao urgentRepairCustomerWorkOrderDao) {
		this.urgentRepairCustomerWorkOrderDao = urgentRepairCustomerWorkOrderDao;
	}

	public UrgentRepairSenceTaskOrderDao getUrgentRepairSenceTaskOrderDao() {
		return urgentRepairSenceTaskOrderDao;
	}

	public void setUrgentRepairSenceTaskOrderDao(
			UrgentRepairSenceTaskOrderDao urgentRepairSenceTaskOrderDao) {
		this.urgentRepairSenceTaskOrderDao = urgentRepairSenceTaskOrderDao;
	}


	public WorkOrderAssnetResourceDao getWorkOrderAssnetResourceDao() {
		return workOrderAssnetResourceDao;
	}

	public void setWorkOrderAssnetResourceDao(
			WorkOrderAssnetResourceDao workOrderAssnetResourceDao) {
		this.workOrderAssnetResourceDao = workOrderAssnetResourceDao;
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

	public StaffDutyService getStaffDutyService() {
		return staffDutyService;
	}

	public void setStaffDutyService(StaffDutyService staffDutyService) {
		this.staffDutyService = staffDutyService;
	}

	public UrgentRepairTechSupportTaskOrderDao getUrgentRepairTechSupportTaskOrderDao() {
		return urgentRepairTechSupportTaskOrderDao;
	}

	public void setUrgentRepairTechSupportTaskOrderDao(
			UrgentRepairTechSupportTaskOrderDao urgentRepairTechSupportTaskOrderDao) {
		this.urgentRepairTechSupportTaskOrderDao = urgentRepairTechSupportTaskOrderDao;
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

	public TaskTracingRecordService getTaskTracingRecordService() {
		return taskTracingRecordService;
	}

	public void setTaskTracingRecordService(
			TaskTracingRecordService taskTracingRecordService) {
		this.taskTracingRecordService = taskTracingRecordService;
	}

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}
	
	
	
	
	
}
