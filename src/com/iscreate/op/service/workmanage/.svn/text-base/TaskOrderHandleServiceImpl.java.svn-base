package com.iscreate.op.service.workmanage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.publicinterface.WorkOrderAssnetResourceDao;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.workmanage.WorkmanageBizprocessTaskinfoConf;
import com.iscreate.op.pojo.workmanage.WorkmanageCountTaskorderObject;
import com.iscreate.op.pojo.workmanage.WorkmanageStatusreg;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.workmanage.util.CommonTools;
import com.iscreate.op.service.workmanage.util.DataSelectUtil;
import com.iscreate.plat.tools.LatLngHelper;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;
import com.iscreate.plat.workflow.serviceaccess.ServiceBean;

public class TaskOrderHandleServiceImpl implements TaskOrderHandleService {

	
	private HibernateTemplate hibernateTemplate;
	private ServiceBean workFlowService;
	private WorkOrderHandleService workOrderHandleService;
	private WorkFlowInfoService workFlowInfoService;
	private BizprocessTaskinfoConfService bizprocessTaskinfoConfService;
	private WorkmanageStatusregService workmanageStatusregService;
	private CommonQueryService commonQueryService;
	private NetworkResourceInterfaceService networkResourceInterfaceService;
	private WorkOrderAssnetResourceDao workOrderAssnetResourceDao;
	
	private DataSelectUtil dataSelectUtil;
	private SysOrgUserService sysOrgUserService;
	
	
	
	private static final String PROCESS_END_FLAG="ended";
	private static final String PROCESS_ENDTASK_NAME="end1";
	
	
	double terminal_longitude=113.3174d;
	double terminal_latitude=23.08315d;
	
	private static final Log logger = LogFactory.getLog(TaskOrderHandleServiceImpl.class);
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void setWorkFlowService(ServiceBean workFlowService) {
		this.workFlowService = workFlowService;
	}
	
	public void setWorkOrderHandleService(
			WorkOrderHandleService workOrderHandleService) {
		this.workOrderHandleService = workOrderHandleService;
	}
	
	public void setWorkFlowInfoService(WorkFlowInfoService workFlowInfoService) {
		this.workFlowInfoService = workFlowInfoService;
	}
	
	public void setBizprocessTaskinfoConfService(
			BizprocessTaskinfoConfService bizprocessTaskinfoConfService) {
		this.bizprocessTaskinfoConfService = bizprocessTaskinfoConfService;
	}
	
	public void setDataSelectUtil(DataSelectUtil dataSelectUtil) {
		this.dataSelectUtil = dataSelectUtil;
	}
	

	
	

	public void setWorkmanageStatusregService(
			WorkmanageStatusregService workmanageStatusregService) {
		this.workmanageStatusregService = workmanageStatusregService;
	}
	
	

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}
	



	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}
	

	public void setWorkOrderAssnetResourceDao(
			WorkOrderAssnetResourceDao workOrderAssnetResourceDao) {
		this.workOrderAssnetResourceDao = workOrderAssnetResourceDao;
	}

	/**
	 * 根任务单id，获取任务单对象
	 * @param toId
	 */
	public WorkmanageTaskorder getTaskOrderByToId(String toId){
		WorkmanageTaskorder taskOrder = null;
		String sql = "select o from WorkmanageTaskorder o where o.toId=?";
		List list = this.hibernateTemplate.find(sql, toId);
		if (list != null && !list.isEmpty()) {
			taskOrder = (WorkmanageTaskorder) list.get(0);
		}
		return taskOrder;
	}
	
	/**
	 * 根据任务单id获取用于显示的任务单信息
	 * @param toId
	 * @return
	 */
	public Map<String,String> getTaskOrderByToIdForShowProcess(String toId){
		Map<String,String> map=null;
		String sql="select * from V_WM_URGENTREPAIR_TASKORDER where \"toId\"=?";
		List<String> params=new ArrayList<String>();
		params.add(toId);
		List<Map> list=this.dataSelectUtil.selectDataWithCondition(sql, params);
		if(list!=null && !list.isEmpty()){
			map=list.get(0);
		}
		return map;
	}
	
	
	/**
	 * 根据任务单id获取用于显示的巡检任务单信息
	 * @param toId
	 * @return
	 */
	public Map<String,String> getRoutineInspectionTaskOrderByToIdForShowProcess(String toId){
		Map<String,String> map=null;
//		String sql="select * from view_workmanage_routineinspection_taskorder where toId=?";
//		List<String> params=new ArrayList<String>();
//		params.add(toId);
//		List<Map> list=this.dataSelectUtil.selectDataWithCondition(sql, params);
//		if(list!=null && !list.isEmpty()){
//			map=list.get(0);
//		}
		
		
		//调用通用查询接口
		String queryEntityName="V_WM_INSP_TASKORDER";
		String conditionString="and \"toId\"='"+toId+"'";
		
		List<Map> list  = null;
		Map<String,Object> returnMap=this.commonQueryService.commonQueryService(null,null, null, null, queryEntityName, null, conditionString);
		if(returnMap!=null){
			list = (List<Map>)returnMap.get("entityList");
		}
		
		if(list!=null && !list.isEmpty()){
			map = new HashMap<String, String>();
			Map<String,Object> map2 = list.get(0);
			for (String key : map2.keySet()) {
				map.put(key, map2.get(key)+"");
			}
		}
		
		return map;
	}
	
	
	
	/**
	 * 根据任务单id获取用于任务单InHibernate
	 * @param toId
	 * @return
	 */
	public Map<String,String> getTaskOrderByToIdInHibernate(String toId){
		Map resultMap=new HashMap();
		WorkmanageTaskorder wm_taskOrder=this.getTaskOrderByToId(toId);
		if(resultMap!=null){
			CommonTools.getValueOfObjectByPropertyName(wm_taskOrder,resultMap);
		}
		
		WorkmanageBizprocessTaskinfoConf taskNodeInfo=this.bizprocessTaskinfoConfService.getWorkmanageBizprocessTaskinfoConfByCondition(wm_taskOrder.getProcessDefineId(), wm_taskOrder.getCurrentTaskName());
		if(resultMap!=null){
			CommonTools.getValueOfObjectByPropertyName(taskNodeInfo,resultMap);
		}
		
		
		//获取任务单状态信息
		if(resultMap!=null){
			String statusId=resultMap.get("status")==null?"":resultMap.get("status").toString();
			if(statusId!=null && !statusId.isEmpty()){
				WorkmanageStatusreg statusReg=this.workmanageStatusregService.getWorkmanageStatusregById(Long.valueOf(statusId));
				CommonTools.getValueOfObjectByPropertyName(statusReg,resultMap);
			}
			
		}
		return resultMap;
	}
	
	
	/**
	 * 更新任务单对象
	 * @param workmanageTaskorder
	 */
	public void updateTaskOrder(WorkmanageTaskorder workmanageTaskorder){
		this.hibernateTemplate.update(workmanageTaskorder);
	}
	

	/**
	 * 创建任务单流程
	 * @param bizProcessCode 业务流程code
	 * @param processDefineId 流程定义id
	 * @param workOrder 任务单对象
	 * @param participantList 参与者列表
	 * @return
	 */
	public String createTaskOrderProcess(String bizProcessCode,String processDefineId,
			WorkmanageTaskorder taskOrder, List<String> participantList) {
		
		String parentProcessTaskId="";	//父流程任务id
		String processInstanceId = "";
		// 保存任务单信息对象
		this.hibernateTemplate.save(taskOrder);
		
		// 提供流程变量信息
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("TOID", taskOrder.getToId());	//将任务号存至流程变量,用于下个任务环节初始化表单时调用
		variables.put("WOID", taskOrder.getWoId());	//将工单号存至流程变量,用于下个任务环节初始化表单时调用
		
		StringBuffer sb_participant = new StringBuffer();
		if (participantList != null && !participantList.isEmpty()) {
			for (String participant : participantList) {
				sb_participant.append(participant).append(",");
			}
			sb_participant.delete(sb_participant.length() - 1,
					sb_participant.length());
		}

		// 指定接收者
		variables.put(ProcessConstants.VariableKey.ASSIGNEE_TYPE,ProcessConstants.AcceptorType.ACCEPTOR_PEOPLE);
		variables.put(ProcessConstants.VariableKey.ASSIGNEE_ID,sb_participant.toString());
		
		if(taskOrder.getWoId().equals(taskOrder.getParentBizOrderId())){	//父流程是工单类型
			WorkmanageWorkorder parentOrder=this.workOrderHandleService.getWorkOrderByWoId(taskOrder.getParentBizOrderId());
			parentProcessTaskId=parentOrder.getCurrentTaskId();
		}else{
			WorkmanageTaskorder parentOrder=this.getTaskOrderByToId(taskOrder.getParentBizOrderId());
			parentProcessTaskId=parentOrder.getCurrentTaskId();
		}
		
		
//		//如果是启动专家单流程，需要由工作流关闭父流程
//		if("抢修".equals(taskOrder.getToType()) && Constants.PROCESS_URGENTREPAIR_EXPERT_CODE.equals(bizProcessCode)){
//			
//			processInstanceId=this.workFlowService.startWorkflow(processDefineId, taskOrder.getAssigner(), ProcessConstants.StarterType.STARTER_TASK, 
//					parentProcessTaskId, taskOrder.getToId(), variables, true, null, null);
//		}else{
//			// 工作流程引擎启动新流程
//			processInstanceId = this.workFlowService.startWorkflow(
//					processDefineId, taskOrder.getAssigner(),
//					ProcessConstants.StarterType.STARTER_TASK, parentProcessTaskId,taskOrder.getToId(), variables);
//		}
		
		// 工作流程引擎启动新流程
		try {
			processInstanceId = this.workFlowService.startWorkflow(
					processDefineId, taskOrder.getAssigner(),
					ProcessConstants.StarterType.STARTER_TASK, parentProcessTaskId,taskOrder.getToId(), variables);
		} catch (WFException e) {
			e.printStackTrace();
		}
		
		
		if(processInstanceId!=null && !"".equals(processInstanceId)){
			//流程实例化成功后，更新工单信息
			taskOrder.setProcessDefineId(processDefineId);
			taskOrder.setProcessInstId(processInstanceId);
			this.hibernateTemplate.update(taskOrder);

			boolean initFlag = this.initTaskOrderInfo(processDefineId,
					processInstanceId,participantList, variables);

			logger.info("initFlag===" + initFlag);
		}

		return processInstanceId;
	}
	
	
	/**
	 * 创建任务单流程（带群组）
	 * @param bizProcessCode
	 * @param processDefineId
	 * @param workmanageTaskorder
	 * @param personalOrGroup 个人任务还是群组任务
	 * @param taskAcceptorId
	 * @return
	 */
	public String createTaskOrderProcess(String bizProcessCode,String processDefineId,
			WorkmanageTaskorder workmanageTaskorder, String personalOrGroup,String taskAcceptorId){
		
		String parentProcessTaskId="";	//父流程任务id
		String processInstanceId = "";
		// 保存任务单信息对象
		this.hibernateTemplate.save(workmanageTaskorder);
		
		// 提供流程变量信息
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("TOID", workmanageTaskorder.getToId());	//将任务号存至流程变量,用于下个任务环节初始化表单时调用
		variables.put("WOID", workmanageTaskorder.getWoId());	//将工单号存至流程变量,用于下个任务环节初始化表单时调用
		
		
		// 指定接收者类型和具体接收者
		variables.put(ProcessConstants.VariableKey.ASSIGNEE_TYPE,personalOrGroup);
		variables.put(ProcessConstants.VariableKey.ASSIGNEE_ID,taskAcceptorId);
		
		if(workmanageTaskorder.getWoId().equals(workmanageTaskorder.getParentBizOrderId())){	//父流程是工单类型
			WorkmanageWorkorder parentOrder=this.workOrderHandleService.getWorkOrderByWoId(workmanageTaskorder.getParentBizOrderId());
			parentProcessTaskId=parentOrder.getCurrentTaskId();
		}else{
			WorkmanageTaskorder parentOrder=this.getTaskOrderByToId(workmanageTaskorder.getParentBizOrderId());
			parentProcessTaskId=parentOrder.getCurrentTaskId();
		}
		
		
//		//如果是启动专家单流程，需要由工作流关闭父流程
//		if("抢修".equals(taskOrder.getToType()) && Constants.PROCESS_URGENTREPAIR_EXPERT_CODE.equals(bizProcessCode)){
//			
//			processInstanceId=this.workFlowService.startWorkflow(processDefineId, taskOrder.getAssigner(), ProcessConstants.StarterType.STARTER_TASK, 
//					parentProcessTaskId, taskOrder.getToId(), variables, true, null, null);
//		}else{
//			// 工作流程引擎启动新流程
//			processInstanceId = this.workFlowService.startWorkflow(
//					processDefineId, taskOrder.getAssigner(),
//					ProcessConstants.StarterType.STARTER_TASK, parentProcessTaskId,taskOrder.getToId(), variables);
//		}
		
		// 工作流程引擎启动新流程
		try {
			processInstanceId = this.workFlowService.startWorkflow(
					processDefineId, workmanageTaskorder.getAssigner(),
					ProcessConstants.StarterType.STARTER_TASK, parentProcessTaskId,workmanageTaskorder.getToId(), variables);
		} catch (WFException e) {
			e.printStackTrace();
		}
		
		
		if(processInstanceId!=null && !"".equals(processInstanceId)){
			//流程实例化成功后，更新工单信息
			workmanageTaskorder.setProcessDefineId(processDefineId);
			workmanageTaskorder.setProcessInstId(processInstanceId);
			this.hibernateTemplate.update(workmanageTaskorder);

			boolean initFlag = this.initTaskOrderInfo(processDefineId,
					processInstanceId, variables);

			logger.info("initFlag===" + initFlag);
		}

		return processInstanceId;
		
	}
	
	
	
	/**
	 * 初始化任务表信息
	 * @param processDefineId 流程定义id
	 * @param processInstId 流程实例id
	 * @param participantList 参与者列表
	 * @param variables 流程变量
	 * @return
	 */
	private boolean initTaskOrderInfo(String processDefineId,
			String processInstId,List<String> participantList,
			Map<String, Object> variables){
		
		boolean isSuccess = false;
		
		// 获取当前流程所在节点信息
		FlowTaskInfo flowTaskInfo = this.workFlowInfoService.getTaskInfoByProcessInstanceId(processInstId);
		
		if(flowTaskInfo!=null){
			String taskName = flowTaskInfo.getTaskName();
			String taskId=flowTaskInfo.getTaskId();

			// 根据流程定义id与当前所在节点信息，获取节点模板配置信息
			WorkmanageBizprocessTaskinfoConf taskInfoConf = this.bizprocessTaskinfoConfService
					.getWorkmanageBizprocessTaskinfoConfByCondition(
							processDefineId, taskName);
			
			// 更新任务单对象信息
			String toId = variables.get("TOID").toString();
			WorkmanageTaskorder update_taskOrder = this.getTaskOrderByToId(toId);
			if (participantList != null && !participantList.isEmpty()) {
				String userId=participantList.get(0);
				//ou.jh
				SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(userId);
//				Staff staff=this.providerOrganizationService.getStaffByAccount(userId);
				
				update_taskOrder.setCurrentHandler(userId);
				if(sysOrgUserByAccount!=null){
					update_taskOrder.setCurrentHandlerName(sysOrgUserByAccount.getName());
					
				}
				
				//获取用户所属最高级组织
				//List<ProviderOrganization> orgList=this.providerOrganizationService.getTopLevelOrgByAccount(userId);
				List<SysOrg> orgList=this.sysOrganizationService.getTopLevelOrgByAccount(userId);
				
				if(orgList!=null && !orgList.isEmpty()){
					SysOrg org=orgList.get(0);
					update_taskOrder.setCurrentHandlerOrgId(org.getOrgId());
				}else{
					logger.info("获取用户【"+userId+"】所属最高级组织为空");
				}
				
				update_taskOrder.setCurrentTaskId(taskId);
				update_taskOrder.setCurrentTaskName(taskName);
				if (taskInfoConf != null) {
					update_taskOrder.setStatus(taskInfoConf.getTaskInitStatus());
				}
				this.hibernateTemplate.update(update_taskOrder);
			}
		}else{
			
			//判断流程是否已经结束
			boolean isEnded=this.workFlowInfoService.judgeProcessIsEnd(processInstId);
			if(isEnded){	//如果是，更新任务单信息
				WorkmanageBizprocessTaskinfoConf taskInfoConf = this.bizprocessTaskinfoConfService
				.getWorkmanageBizprocessTaskinfoConfByCondition(
						processDefineId, PROCESS_ENDTASK_NAME);
		
				// 更新任务单对象信息
				String toId = variables.get("TOID").toString();
				WorkmanageTaskorder update_taskOrder = this.getTaskOrderByToId(toId);
				
				update_taskOrder.setCurrentTaskId(PROCESS_END_FLAG);
				update_taskOrder.setCurrentTaskName(PROCESS_ENDTASK_NAME);
				if (taskInfoConf != null) {
					update_taskOrder.setStatus(taskInfoConf.getTaskInitStatus());
				}
				this.hibernateTemplate.update(update_taskOrder);
			}
		}
		isSuccess = true;
		return isSuccess;
	}
	
	
	
	/**
	 * 初始化任务表信息
	 * @param processDefineId 流程定义id
	 * @param processInstId 流程实例id
	 * @param participantList 参与者列表
	 * @param variables 流程变量
	 * @return
	 */
	private boolean initTaskOrderInfo(String processDefineId,
			String processInstId,Map<String, Object> variables){
		
		boolean isSuccess = false;
		
		// 获取当前流程所在节点信息
		FlowTaskInfo flowTaskInfo = this.workFlowInfoService.getTaskInfoByProcessInstanceId(processInstId);
		
		if(flowTaskInfo!=null){
			String taskName = flowTaskInfo.getTaskName();
			String taskId=flowTaskInfo.getTaskId();

			// 根据流程定义id与当前所在节点信息，获取节点模板配置信息
			WorkmanageBizprocessTaskinfoConf taskInfoConf = this.bizprocessTaskinfoConfService
					.getWorkmanageBizprocessTaskinfoConfByCondition(
							processDefineId, taskName);
			
			// 更新任务单对象信息
			String toId = variables.get("TOID").toString();
			WorkmanageTaskorder update_taskOrder = this.getTaskOrderByToId(toId);
			update_taskOrder.setCurrentTaskId(taskId);
			update_taskOrder.setCurrentTaskName(taskName);
			if (taskInfoConf != null) {
				update_taskOrder.setStatus(taskInfoConf.getTaskInitStatus());
			}
			this.hibernateTemplate.update(update_taskOrder);
		}else{
			
			//判断流程是否已经结束
			boolean isEnded=this.workFlowInfoService.judgeProcessIsEnd(processInstId);
			if(isEnded){	//如果是，更新任务单信息
				WorkmanageBizprocessTaskinfoConf taskInfoConf = this.bizprocessTaskinfoConfService
				.getWorkmanageBizprocessTaskinfoConfByCondition(
						processDefineId, PROCESS_ENDTASK_NAME);
		
				// 更新任务单对象信息
				String toId = variables.get("TOID").toString();
				WorkmanageTaskorder update_taskOrder = this.getTaskOrderByToId(toId);
				
				update_taskOrder.setCurrentTaskId(PROCESS_END_FLAG);
				update_taskOrder.setCurrentTaskName(PROCESS_ENDTASK_NAME);
				if (taskInfoConf != null) {
					update_taskOrder.setStatus(taskInfoConf.getTaskInitStatus());
				}
				this.hibernateTemplate.update(update_taskOrder);
			}
		}
		isSuccess = true;
		return isSuccess;
	}
	
	
	/**
	 * 受理任务单
	 * @param taskOrder 任务单对象
	 * @param acceptPeople
	 * @return
	 */
	public boolean acceptTaskOrder(WorkmanageTaskorder taskOrder,String acceptPeople){
		boolean isSuccess = false;
		try {
//			isSuccess=this.updateTaskOrderStatusProcess(toId, Constants.TASKORDER_HANDLING);
			
			//获取任务单
			WorkmanageTaskorder update_TaskOrder=this.getTaskOrderByToId(taskOrder.getToId());
			
			//修改任务单
			update_TaskOrder.setAcceptComment(taskOrder.getAcceptComment());
			update_TaskOrder.setAcceptTime(taskOrder.getAcceptTime());
			update_TaskOrder.setStatus(WorkManageConstant.TASKORDER_HANDLING);
			update_TaskOrder.setAcceptPeople(acceptPeople);
			//ou.jh
			SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(acceptPeople);
//			Staff staff=this.providerOrganizationService.getStaffByAccount(acceptPeople);
			if(sysOrgUserByAccount!=null){
				update_TaskOrder.setAcceptPeopleName(sysOrgUserByAccount.getName());
			}
			this.hibernateTemplate.update(update_TaskOrder);
			isSuccess=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isSuccess;
	}
	
	
	
	
	/**
	 * 结束任务单流程任务
	 * @param toId 任务单id
	 * @param currentHandler 当前处理人
	 * @param outcome 流向名称
	 * @param participantList 参与者列表
	 * @return
	 */
	public boolean completeTaskOrderProcessTask(String toId, String currentHandler,String outcome, List<String> participantList){
		
		boolean isSuccess = false;
		String pdId="";	//流程定义id
		String piId="";	//流程实例id
		String taskId="";
		try {

			// 获取任务单对象信息
			WorkmanageTaskorder wm_taskOrder = this.getTaskOrderByToId(toId);
			pdId=wm_taskOrder.getProcessDefineId();
			piId=wm_taskOrder.getProcessInstId();
			taskId=wm_taskOrder.getCurrentTaskId();
			
			if (taskId != null) {
				// 提供流程变量信息
				Map<String, Object> variables = new HashMap<String, Object>();
				
				StringBuffer sb_participant = new StringBuffer();
				if (participantList != null && !participantList.isEmpty()) {
					for (String participant : participantList) {
						sb_participant.append(participant).append(",");
					}
					sb_participant.delete(sb_participant.length() - 1,
							sb_participant.length());
					
					// 指定接收者
					variables.put(ProcessConstants.VariableKey.ASSIGNEE_TYPE,ProcessConstants.AcceptorType.ACCEPTOR_PEOPLE);
					variables.put(ProcessConstants.VariableKey.ASSIGNEE_ID,sb_participant.toString());
				}

				
				variables.put("TOID", toId);
				variables.put("WOID", wm_taskOrder.getWoId());

				
				// 结束任务的时候，要先判断是否是群组任务，如果是，要先take
//				boolean isCompleteSuccess = false;
				boolean isGroup = this.workFlowInfoService.judegeTaskIsGroup(taskId);
				if (isGroup) {
					boolean isTakeSuccess = this.workFlowService.takeTask(taskId, currentHandler);
					if (isTakeSuccess) {
						try {
							this.workFlowService.completeTask(taskId, outcome,currentHandler, variables);
						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}
					}
				} else {
					try {
						this.workFlowService.completeTask(taskId, outcome, currentHandler, variables);
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
				
				//更改任务单最终回复时间
				wm_taskOrder.setFinalCompleteTime(new Date());
				this.hibernateTemplate.update(wm_taskOrder);
				
				isSuccess=this.initTaskOrderInfo(pdId, piId,participantList, variables);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isSuccess;
	}
	
	
	
	
	public boolean completeRoutineInspectionProcessTask(String toId,String currentOperator){
		boolean isSuccess=false;
		
		String taskId="";
		
		WorkmanageTaskorder workManageOrder=this.getTaskOrderByToId(toId);
		taskId=workManageOrder.getCurrentTaskId();
		try {
			isSuccess=this.workFlowService.completeTask(taskId, null, currentOperator, null);
		} catch (WFException e) {
			e.printStackTrace();
			logger.error("调用工作流接口结束任务失败");
		}
		
		return isSuccess;
	}
	
	
	/**
	 * 更改任务单状态
	 * @param toId
	 * @param status
	 * @return
	 */
	public boolean updateTaskOrderStatusProcess(String toId,int status){
		boolean isSuccess=false;
		try {
			//获取任务单
			WorkmanageTaskorder updateTaskOrder=this.getTaskOrderByToId(toId);
			
			//修改任务单
			updateTaskOrder.setStatus(status);
			this.hibernateTemplate.update(updateTaskOrder);
			isSuccess=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	/**
	 * 更改任务单ReadStatus状态
	 * @param toId
	 * @param readStatus
	 * @return
	 */
	public boolean updateTaskOrderReadStatusProcess(String toId,int readStatus){
		boolean isSuccess=false;
		try {
			//获取任务单
			WorkmanageTaskorder updateTaskOrder=this.getTaskOrderByToId(toId);
			
			//修改任务单
			updateTaskOrder.setIsRead(readStatus);
			this.hibernateTemplate.update(updateTaskOrder);
			isSuccess=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	
	/**
	 * 根据用户账号与任务单号，判断当前用户是否是该任务单的当前处理人
	 * @param toId
	 * @param currentUser
	 * @return
	 */
	public boolean judgeTaskOrderCurrentHandler(String toId,String currentUser){
		boolean isYes=false;
		//获取任务单
		WorkmanageTaskorder taskOrder=this.getTaskOrderByToId(toId);
		if(taskOrder!=null && currentUser.equals(taskOrder.getCurrentHandler())){
			isYes=true;
		}
		return isYes;
	}
	
	
	
	/**
	 * 获取工单关联的任务单列表
	 * @param woId
	 * @return
	 */
	public List<Map> getTaskOrderListByWoIdProcess(String woId){
		List<Map> list=null;
//		String sql="";
//		sql="select * from view_workmanage_urgentrepair_taskorder t1 where t1.woId=?";
//		List<String> params=new ArrayList<String>();
//		params.add(woId);
//		list=this.dataSelectUtil.selectDataWithCondition(sql, params);
//		return list;
		
		//调用通用查询接口
		String queryEntityName="V_WM_URGENTREPAIR_TASKORDER";
		String conditionString="and \"woId\"='"+woId+"'";
		
		Map<String,Object> returnMap=this.commonQueryService.commonQueryService(null,null, null, null, queryEntityName, null, conditionString);
		if(returnMap!=null){
			list = (List<Map>)returnMap.get("entityList");
		}
		return list;
	}
	
	
	/**
	 * 获取工单关联的巡检任务单列表
	 * @param woId
	 * @return
	 */
	public List<Map> getRoutineInspectionTaskOrderListByWoIdProcess(String woId){
		List<Map> list=null;
//		String sql="";
//		sql="select * from view_workmanage_routineinspection_taskorder t1 where t1.woId=?";
//		List<String> params=new ArrayList<String>();
//		params.add(woId);
//		list=this.dataSelectUtil.selectDataWithCondition(sql, params);
//		return list;
		
		
		//调用通用查询接口
		String queryEntityName="V_WM_INSP_TASKORDER";
		String conditionString="and \"woId\"='"+woId+"'";
		
		Map<String,Object> returnMap=this.commonQueryService.commonQueryService(null,null, null, null, queryEntityName, null, conditionString);
		if(returnMap!=null){
			list = (List<Map>)returnMap.get("entityList");
		}
		return list;
	}
	
	
	
	/**
	 * 根据工单id，获取关联的已经关闭的任务单数目
	 * @param woId
	 * @return
	 */
	public int getCountOfRoutineInspectionCloseTaskOrderByWoId(String woId){
		int count=0;
		List<Map> resultList=null;
		final String sql="select count(*) as \"count\" from WM_TASKORDER where status="+WorkManageConstant.TASKORDER_INSPECT_CLOSED+" and woId='"+woId+"'";
		
		//获取总数
		resultList=this.hibernateTemplate.executeFind(new HibernateCallback(){
			public List doInHibernate(Session session) throws HibernateException, SQLException
			{
				SQLQuery sqlQuery =session.createSQLQuery(sql);
				sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List result=sqlQuery.list();
				return result;
			}
		});
		
		if(resultList!=null && !resultList.isEmpty()){
			String countString = resultList.get(0).get("count")==null?"0":resultList.get(0).get("count").toString();
			count = Integer.valueOf(countString);
		}
		
		return count;
	}
	
	/**
	 * 根据工单列表，获取关联的已经关闭的任务单数目
	 * @param woId
	 * @return
	 */
	public List<Map> getCountOfRoutineInspectionCloseTaskOrderByWoIdList(List<String> params){
		String woIds = "(";
		if(params!=null&&!params.isEmpty()){
			for(int i=0;i<params.size();i++){
				if(i==0){
					woIds += "'"+params.get(i)+"'";
				}else{
					woIds += ",'"+params.get(i)+"'";
				}
				
			}
		}
		woIds += ")";
		List<Map> resultList=null;
		final String sql="select count(*) as \"count\" , woId as \"woId\" from WM_TASKORDER where status="+WorkManageConstant.TASKORDER_INSPECT_CLOSED+" and woId in "+woIds+" group by woId";
		
		//获取总数
		resultList=this.hibernateTemplate.executeFind(new HibernateCallback(){
			public List doInHibernate(Session session) throws HibernateException, SQLException
			{
				SQLQuery sqlQuery =session.createSQLQuery(sql);
				sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List result=sqlQuery.list();
				return result;
			}
		});
		
		if(resultList!=null && !resultList.isEmpty()){
			for(Map map : resultList){
				if(map.get("count")==null||"".equals(map.get("count"))){
					map.put("count", "0");
				}
			}
		}
		
		return resultList;
	}
	
	/**
	 * 根据工单列表，获取任务单数目
	 * @param woId
	 * @return
	 */
	public List<Map> getCountOfRoutineInspectionTaskOrderByWoIdList(List<String> params){
		String woIds = "(";
		if(params!=null&&!params.isEmpty()){
			for(int i=0;i<params.size();i++){
				if(i==0){
					woIds += "'"+params.get(i)+"'";
				}else{
					woIds += ",'"+params.get(i)+"'";
				}
				
			}
		}
		woIds += ")";
		List<Map> resultList=null;
		final String sql="select count(*) as \"count\" , woId as \"woId\" from WM_TASKORDER where woId in "+woIds+" group by woId";
		
		//获取总数
		resultList=this.hibernateTemplate.executeFind(new HibernateCallback(){
			public List doInHibernate(Session session) throws HibernateException, SQLException
			{
				SQLQuery sqlQuery =session.createSQLQuery(sql);
				sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List result=sqlQuery.list();
				return result;
			}
		});
		
		if(resultList!=null && !resultList.isEmpty()){
			for(Map map : resultList){
				if(map.get("count")==null||"".equals(map.get("count"))){
					map.put("count", "0");
				}
			}
		}
		
		return resultList;
	}
	
	
	
	/**
	 * 根据工单id和状态，获取关联的巡检任务单数目
	 * @param woId
	 * @return
	 */
	public int getCountOfRoutineInspectionTaskOrderByWoIdAndStatus(String woId,int status){
		int count=0;
		List<Map> resultList=null;
		final String sql="select count(*) as \"count\" from WM_TASKORDER where status="+status+" and woId='"+woId+"'";
		
		//获取总数
		resultList=this.hibernateTemplate.executeFind(new HibernateCallback(){
			public List doInHibernate(Session session) throws HibernateException, SQLException
			{
				SQLQuery sqlQuery =session.createSQLQuery(sql);
				sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List result=sqlQuery.list();
				return result;
			}
		});
		
		if(resultList!=null && !resultList.isEmpty()){
			String countString = resultList.get(0).get("count")==null?"0":resultList.get(0).get("count").toString();
			count = Integer.valueOf(countString);
		}
		
		return count;
	}
	
	
	/**
	 * 根据工单id，获取关联的任务单数目
	 * @param woId
	 * @return
	 */
	public int getCountOfRoutineInspectionTaskOrderByWoId(String woId){
		int count=0;
		List<Map> resultList=null;
		final String sql="select count(*) as \"count\" from WM_TASKORDER where woId='"+woId+"'";
		
		//获取总数
		resultList=this.hibernateTemplate.executeFind(new HibernateCallback(){
			public List doInHibernate(Session session) throws HibernateException, SQLException
			{
				SQLQuery sqlQuery =session.createSQLQuery(sql);
				sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List result=sqlQuery.list();
				return result;
			}
		});
		
		if(resultList!=null && !resultList.isEmpty()){
			String countString = resultList.get(0).get("count")==null?"0":resultList.get(0).get("count").toString();
			count = Integer.valueOf(countString);
		}
		
		return count;
	}
	
	
	
	/**
	 * 转派任务单
	 * @param pk_workOrder 工单标识对象
	 * @param toId 任务单号
	 * @param updateWorkOrder 需要更新的工单单对象属性
	 * @param updateTaskOrder 需要更新的任务单单对象属性
	 * @return
	 */
	public boolean handoverTaskOrderProcess(WorkmanageWorkorder pk_workOrder,String toId,WorkmanageWorkorder updateWorkOrder,WorkmanageTaskorder updateTaskOrder){
		boolean isSuccess=false;
		//获取任务单对象
		WorkmanageTaskorder taskOrder=this.getTaskOrderByToId(toId);
		
		if(taskOrder!=null && updateTaskOrder!=null){
			String oldParticipant=taskOrder.getCurrentHandler();	//获取当前处理人
			String taskId=taskOrder.getCurrentTaskId();	//获取任务单所在任务节点id
			
			String newParticipant=updateTaskOrder.getCurrentHandler();	//获取新的当前处理人
			try {
				isSuccess=this.workFlowService.changeTaskAssignee(taskId, oldParticipant, newParticipant);
			} catch (WFException e) {
				e.printStackTrace();
				isSuccess=false;
			}
			
			//工作流更换成功
			if(isSuccess){
				
//				Staff staff=this.providerOrganizationService.getStaffByAccount(newParticipant);
//				if(staff!=null){
//					taskOrder.setCurrentHandlerName(staff.getName());
//				}
				
				if(updateWorkOrder!=null){
					pk_workOrder.setOrderOwner(updateWorkOrder.getOrderOwner());
					pk_workOrder.setOrderOwnerOrgId(updateWorkOrder.getOrderOwnerOrgId());
				}
				
				if(updateTaskOrder.getOrderOwnerOrgId()!=null && updateTaskOrder.getOrderOwnerOrgId().intValue()!=0l){
					taskOrder.setOrderOwnerOrgId(updateTaskOrder.getOrderOwnerOrgId());
				}
				
				if(updateTaskOrder.getOrderOwner()!=null && !"".equals(updateTaskOrder.getOrderOwner())){
					taskOrder.setOrderOwner(updateTaskOrder.getOrderOwner());
				}
				
				taskOrder.setCurrentHandler(newParticipant);
				taskOrder.setCurrentHandlerName(updateTaskOrder.getCurrentHandlerName());
				
				if(updateTaskOrder.getOrderOwnerOrgId()!=null && updateTaskOrder.getOrderOwnerOrgId().intValue()!=0l){
					taskOrder.setCurrentHandlerOrgId(updateTaskOrder.getOrderOwnerOrgId());
				}
				
				taskOrder.setStatus(WorkManageConstant.TASKORDER_WAIT4ACCEPT);
				this.hibernateTemplate.update(taskOrder);
				this.hibernateTemplate.update(pk_workOrder);
			}
		}
		return isSuccess;
	}
	
	/**
	 * 统计人或车的任务单数量（增加）
	 * @param countWorkorder
	 * @return
	 */
	public synchronized boolean countTaskOrderNumberForSave(WorkmanageCountTaskorderObject countTaskOrder){
		boolean isSuccess=false;
		String hql="";
		//先查找该网络设施是否存在相应记录，如果没有，则save；如果有，则更新相应数量
		hql="select o from WorkmanageCountTaskorderObject o where o.objectId=? and o.objectType=?";
		List list=this.hibernateTemplate.find(hql,countTaskOrder.getObjectId(),countTaskOrder.getObjectType());
		if(list!=null && !list.isEmpty()){
			WorkmanageCountTaskorderObject updateCountTaskorder=(WorkmanageCountTaskorderObject)list.get(0);
			long updateCount=updateCountTaskorder.getTaskCount()+1;
			updateCountTaskorder.setTaskCount(updateCount);
			this.hibernateTemplate.update(updateCountTaskorder);
		}else{
			countTaskOrder.setTaskCount(Long.valueOf("1"));
			this.hibernateTemplate.save(countTaskOrder);
		}
		isSuccess=true;
		return isSuccess;
	}
	
	
	/**
	 * 统计人或车的任务单数量（减去）
	 * @param countWorkorder
	 * @return
	 */
	public synchronized boolean countTaskOrderNumberForSubtract(WorkmanageCountTaskorderObject countTaskOrder){
		boolean isSuccess=false;
		String hql="";
		try {
			
			//先查找该网络设施是否存在相应记录
			hql="select o from WorkmanageCountTaskorderObject o where o.objectId=? and o.objectType=?";
			List list=this.hibernateTemplate.find(hql,countTaskOrder.getObjectId(),countTaskOrder.getObjectType());
			if(list!=null && !list.isEmpty()){
				WorkmanageCountTaskorderObject updateCountTaskorder=(WorkmanageCountTaskorderObject)list.get(0);
				
				if(updateCountTaskorder!=null && updateCountTaskorder.getTaskCount()>0){
					long updateCount=updateCountTaskorder.getTaskCount()-1;
					updateCountTaskorder.setTaskCount(updateCount);
					this.hibernateTemplate.update(updateCountTaskorder);
				}
			}
			isSuccess=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	
	/**
	 * 获取工单的子任务单列表
	 * @param woId
	 * @return
	 */
	public List<Map> getChildTaskOrderListByWoIdProcess(String woId){
		List<Map> list=null;
//		String sql="";
//		sql="select * from view_workmanage_urgentrepair_taskorder t1 where t1.parentBizOrderId=?";
//		List<String> params=new ArrayList<String>();
//		params.add(woId);
//		list=this.dataSelectUtil.selectDataWithCondition(sql, params);
		
		//调用通用查询接口
		String queryEntityName="V_WM_URGENTREPAIR_TASKORDER";
		String conditionString="and \"parentBizOrderId\"='"+woId+"'";
		
		Map<String,Object> returnMap=this.commonQueryService.commonQueryService(null,null, null, null, queryEntityName, null, conditionString);
		if(returnMap!=null){
			list = (List<Map>)returnMap.get("entityList");
		}
		
		
		return list;
	}
	
	
	/**
	 * 获取任务单的子任务单列表
	 * @param toId
	 * @return
	 */
	public List<Map> getChildTaskOrderListByToIdProcess(String toId){
		List<Map> list=null;
//		String sql="";
//		sql="select * from view_workmanage_urgentrepair_taskorder t1 where t1.parentBizOrderId=?";
//		List<String> params=new ArrayList<String>();
//		params.add(toId);
//		list=this.dataSelectUtil.selectDataWithCondition(sql, params);
		
		//调用通用查询接口
		String queryEntityName="V_WM_URGENTREPAIR_TASKORDER";
		String conditionString="and \"parentBizOrderId\"='"+toId+"'";
		
		Map<String,Object> returnMap=this.commonQueryService.commonQueryService(null,null, null, null, queryEntityName, null, conditionString);
		if(returnMap!=null){
			list = (List<Map>)returnMap.get("entityList");
		}
		
		return list;
	}
	
	
	
	/**
	 * 任务单驳回
	 * @param taskOrder 任务单对象
	 * @return
	 */
	public boolean rejectTaskOrderProcess(WorkmanageTaskorder taskOrder){
		boolean isSuccess=false;
		//获取任务单对象
		WorkmanageTaskorder update_TaskOrder=this.getTaskOrderByToId(taskOrder.getToId());
		
		update_TaskOrder.setRejectComment(taskOrder.getRejectComment());
		update_TaskOrder.setRejectTime(taskOrder.getRejectTime());
		update_TaskOrder.setStatus(WorkManageConstant.TASKORDER_WAIT4CANCEL);	//修改任务单状态
		
//		updateTaskOrder.setRejectPeople(currentHandler);
//		Staff staff=this.providerOrganizationService.getStaffByAccount(currentHandler);
//		if(staff!=null){
//			updateTaskOrder.setRejectPeopleName(staff.getName());
//		}
		this.hibernateTemplate.update(update_TaskOrder);
		isSuccess=true;
		return isSuccess;
	}
	
	
	/**
	 * 撤销任务单
	 * @param taskOrder 任务单对象
	 * @param currentCancelPeople
	 * @return
	 */
	public boolean cancelTaskOrderProcess(WorkmanageTaskorder taskOrder,String currentCancelPeople) throws WorkManageException{
		boolean isSuccess=false;
		//获取任务单对象
		WorkmanageTaskorder update_taskOrder=this.getTaskOrderByToId(taskOrder.getToId());
		
		if(update_taskOrder.getStatus().intValue()==WorkManageConstant.TASKORDER_CANCELED){
			throw new WorkManageException("任务单已经撤销，不能再操作");
		}
		
		String piId=update_taskOrder.getProcessInstId();
		update_taskOrder.setCancelComment(taskOrder.getCancelComment());
		update_taskOrder.setCancelTime(taskOrder.getCancelTime());
		update_taskOrder.setStatus(WorkManageConstant.TASKORDER_CANCELED);	//更改为‘已撤销’
		this.hibernateTemplate.update(update_taskOrder);
		try {
			isSuccess=this.workFlowService.endWorkflowInstance(piId, currentCancelPeople, "forceEnded");
		} catch (WFException e) {
			e.printStackTrace();
			isSuccess=false;
		}
		return isSuccess;
	}
	
	
	/**
	 * 撤销巡检任务单
	 * @param toId 任务单id
	 * @param currentCancelPeople
	 * @return
	 */
	public boolean cancelRoutineInspectionTaskOrderProcess(String toId,String currentCancelPeople){
		boolean isSuccess=false;
		//获取任务单对象
		WorkmanageTaskorder workmanageTaskorder=this.getTaskOrderByToId(toId);
		String piId=workmanageTaskorder.getProcessInstId();
		workmanageTaskorder.setStatus(WorkManageConstant.TASKORDER_DELETE);	//更改为删除状态
		this.hibernateTemplate.update(workmanageTaskorder);
		try {
			isSuccess=this.workFlowService.endWorkflowInstance(piId, currentCancelPeople, "forceEnded");
		} catch (WFException e) {
			e.printStackTrace();
			logger.error("调用工作流接口，撤销巡检任务流程失败");
		}
		return isSuccess;
	}
	
	
	/**
	 * 重新派发任务单（拒绝撤销）
	 * @param taskOrder 任务单对象
	 * @return
	 */
	public boolean reAssignTaskOrderProcess(WorkmanageTaskorder taskOrder) throws WorkManageException{
		boolean isSuccess=false;
		//获取任务单对象
		WorkmanageTaskorder update_taskOrder=this.getTaskOrderByToId(taskOrder.getToId());
		if(update_taskOrder.getStatus().intValue()==WorkManageConstant.TASKORDER_CANCELED){
			throw new WorkManageException("任务单已经撤销，不能再操作");
		}
		
		update_taskOrder.setReassignComment(taskOrder.getReassignComment());
		update_taskOrder.setReassignTime(taskOrder.getReassignTime());
		update_taskOrder.setStatus(WorkManageConstant.TASKORDER_WAIT4ACCEPT);
//		String rejectPeople=taskOrder.getRejectPeople();	//获取任务单的拒绝人
//		taskOrder.setCurrentHandler(rejectPeople);
//		
//		//获取拒绝人名称
//		Staff staff=this.providerOrganizationService.getStaffByAccount(rejectPeople);
//		if(staff!=null){
//			taskOrder.setCurrentHandlerName(staff.getName());
//		}
		
		this.hibernateTemplate.update(update_taskOrder);
		
		isSuccess=true;
		return isSuccess;
	}
	
	
	/**
	 * 根据对象类型与对象id，获取任务单单统计数量
	 * @param objectType 对象类型
	 * @param objectId 对象id
	 * @return
	 */
	public long getTaskOrderCountByObjectTypeAndObjectId(String objectType,String objectId){
		long count=-1l;
		String hql="select o from WorkmanageCountTaskorderObject o where o.objectType=? and o.objectId=?";
		List list=this.hibernateTemplate.find(hql, objectType,objectId);
		if(list!=null && !list.isEmpty()){
			WorkmanageCountTaskorderObject taskOrder_count=(WorkmanageCountTaskorderObject)list.get(0);
			count=taskOrder_count.getTaskCount();
		}else{
			count=0;
		}
		
		return count;
	}
	
	
	
	/**
	 * 根据业务单号，判断该单是否是任务单
	 * @param bizOrderId
	 * @return
	 */
	public boolean judgeIsTaskOrder(String bizOrderId){
		boolean isTaskOrder=false;
		WorkmanageTaskorder workmanageTaskorder=this.getTaskOrderByToId(bizOrderId);
		if(workmanageTaskorder!=null){
			isTaskOrder=true;
		}
		return isTaskOrder;
	}
	
	
	/**
	 * 判断任务单是否是群组任务
	 * @param toId
	 * @return
	 */
	public boolean judgeTaskIsGroup(String toId){
		boolean isGroupTask=false;
		WorkmanageTaskorder workmanageTaskorder=this.getTaskOrderByToId(toId);
		String taskId="";
		if(workmanageTaskorder!=null){
			taskId=workmanageTaskorder.getCurrentTaskId();
			isGroupTask=this.workFlowInfoService.judegeTaskIsGroup(taskId);
		}
		return isGroupTask;
	}
	
	
	
	/**
	 * 判断该任务单是否结束
	 * @return
	 */
	public boolean judgeTaskOrderIsEndProcess(String toId){
		String piid=null;
		WorkmanageTaskorder taskOrder=this.getTaskOrderByToId(toId);
		if(taskOrder!=null){
			piid=taskOrder.getProcessInstId();
		}
		boolean isEnd=this.workFlowInfoService.judgeProcessIsEnd(piid);
		return isEnd;
	}
	
	
	
	//----------for Mobile----------
	

	/**
	 * 分页获取任务待办列表（for mobile）
	 * @param userId 用户ID
	 * @param pageIndex 当前页
	 * @param pageSize 行数
	 * @param taskOrderType 任务单类型： pendingTaskOrder（待办任务单）、trackTaskOrder（跟踪任务单）、superviseTaskOrder（监督任务单）
	 * @return
	 */
	public List<Map> getUserTaskOrdersByTaskOrderTypeWithPageForMobile(String userId, int pageIndex, int pageSize ,String taskOrderType,
			String conditionValue,Map<String,String> sortCondition,String longitude,String latitude){
		if(userId==null || "".equals(userId)){
			return null;
		}
		
		List<Map> list  = null;
		String totalCount = "0";
		String sql="";
		if("pendingTaskOrder".equals(taskOrderType)){		//获取待办任务单
			
			//调用通用查询接口
			int start=(pageIndex-1)*pageSize;
			int limit=pageSize;
			String sortName="\"assignTime\"";
			String sortOrder="desc";
			String queryEntityName="V_WM_URGENTREPAIR_TASKORDER";
			String conditionString="and \"currentHandler\"='"+userId+"' and \"status\"<>"+WorkManageConstant.TASKORDER_ASSIGNED+" and \"status\"<>"+WorkManageConstant.TASKORDER_CANCELED+" and \"status\"<>"+WorkManageConstant.TASKORDER_UPGRADING+" and \"status\"<>"+WorkManageConstant.TASKORDER_CLOSED+" and \"status\"<>"+WorkManageConstant.TASKORDER_WAIT4CANCEL;
			
			//查询条件
			if(conditionValue!=null && !"".equals(conditionValue)){
				
			}
			
			Map<String,Object> returnMap=this.commonQueryService.commonQueryService(String.valueOf(start), String.valueOf(limit), sortName, sortOrder, queryEntityName, null, conditionString);
			if(returnMap!=null){
				list = (List<Map>)returnMap.get("entityList");
				totalCount=returnMap.get("count")+"";
			}
			
			//排序条件
			//如果是按距离排序，需求调用网络资源接口过滤
			if(sortCondition!=null && !sortCondition.isEmpty()){
				if(sortCondition.containsKey("distance")){
					if(list!=null && !list.isEmpty()){
						
						
						Map<String,Double> roomDistanceMap=new HashMap<String,Double>();
						
						if(longitude!=null && !"".equals(longitude.trim())){
							terminal_longitude=Double.valueOf(longitude);
						}
						
						if(latitude!=null && !"".equals(latitude.trim())){
							terminal_latitude=Double.valueOf(latitude);
						}
						
						for(int i=0;i<list.size();i++){
							Map tempMap=list.get(i);
							tempMap.put("totalCount",totalCount);
							//获取任务关联的资源
							String woId = tempMap.get("woId")==null?"":tempMap.get("woId").toString();
							//获取工单关联的站址Id
							List<Workorderassnetresource> resList = this.workOrderAssnetResourceDao.getWorkOrderAssnetResourceRecordDao("woId", woId);
							if(resList!=null && !resList.isEmpty()){
								for (Workorderassnetresource res : resList) {
									Long resourceId = res.getStationId();
									String resourceType = res.getNetworkResourceType();
									tempMap.put("resourceId", resourceId);
									tempMap.put("resourceType", WorkManageConstant.NETWORKRESOURCE_STATION);
								}
							}
							
							
							
							//获取站址id
							String resourceId=tempMap.get("resourceId")==null?"":tempMap.get("resourceId").toString();
							Map<String,String> stationMap=this.networkResourceInterfaceService.getResourceByReIdAndReTypeService(resourceId,  WorkManageConstant.NETWORKRESOURCE_STATION);
							
							if(stationMap!=null && !stationMap.isEmpty()){
									
								double station_longitude=stationMap.get("longitude")==null?0:Double.valueOf(stationMap.get("longitude").toString());
								double station_latitude=stationMap.get("latitude")==null?0:Double.valueOf(stationMap.get("latitude").toString());
								
								double gpsDistance=LatLngHelper.Distance(terminal_longitude, terminal_latitude, station_longitude, station_latitude);
								
								//存储机房对应终端location的距离
								tempMap.put("distance", String.format("%.2f",gpsDistance));
							}else{
								logger.info("根据资源类型【站址】和资源id："+resourceId+"，获取站址类型对象失败");
							}
						}
					}
					
					
					//System.out.println("排序前：\r\n"+list);
					
					
					//迭代比较任务所对应资源的距离
					for (int k = 0; k < list.size(); k++){
	                     for (int j = list.size() - 1; j > k; j--){
	                    	  final Map vo_j = list.get(j);
	                          final Map vo_k = list.get(j - 1);   
	                          double distance1=Double.valueOf(vo_j.get("distance")==null?"0":vo_j.get("distance").toString());
	                          double distance2=Double.valueOf(vo_k.get("distance")==null?"0":vo_k.get("distance").toString());
	                          if (distance1 < distance2){
	                               final Map temp = vo_k;
	                               list.remove(j - 1);
	                               list.add(j - 1, vo_j);
	                               list.remove(j);
	                               list.add(j, temp);
	                          }
	                      }   
					}
					
					
					//System.out.println("排序后：\r\n"+list);
					
				}else if(sortCondition.containsKey("requireCompleteTime")){
					if(list!=null && !list.isEmpty()){
						for (int k = 0; k < list.size(); k++){
		                     for (int j = list.size() - 1; j > k; j--){
		                    	  final Map vo_j = list.get(j);
		                          final Map vo_k = list.get(j - 1);   
		                          Date requireCompleteTime1=vo_j.get("requireCompleteTime")==null?null:TimeFormatHelper.setTimeFormat(vo_j.get("requireCompleteTime").toString());
		                          Date requireCompleteTime2=vo_k.get("requireCompleteTime")==null?null:TimeFormatHelper.setTimeFormat(vo_k.get("requireCompleteTime").toString());
		                          if (requireCompleteTime1!=null && requireCompleteTime2!=null){
		                        	  if(requireCompleteTime1.getTime()<requireCompleteTime2.getTime()){
		                        		  final Map temp = vo_k;   
			                              list.remove(j - 1);   
			                              list.add(j - 1, vo_j);   
			                              list.remove(j);   
			                              list.add(j, temp);  
		                        	  }
		                          }
		                      }
						}
					}
				}
			}
			
//			sql= "select * from view_workmanage_urgentrepair_taskorder t1 where t1.currentHandler=? and status<>"+WorkManageConstant.TASKORDER_ASSIGNED+" and status<>"+WorkManageConstant.TASKORDER_CANCELED+" and status<>"+WorkManageConstant.TASKORDER_UPGRADING+" and status<>"+WorkManageConstant.TASKORDER_CLOSED+" order by assignTime desc " 
//					+ "limit " + (pageIndex-1)*pageSize + "," + pageSize;
//			List<String> params=new ArrayList<String>();
//			params.add(userId);
//			list=this.dataSelectUtil.selectDataWithCondition(sql, params);
			
			return list;
		}else if("trackTaskOrder".equals(taskOrderType)){	//获取跟踪任务
			
			//调用通用查询接口
			int start=(pageIndex-1)*pageSize;
			int limit=pageSize;
			String sortName="\"assignTime\"";
			String sortOrder="desc";
			String queryEntityName="V_WM_URGENTREPAIR_TASKORDER";
			String conditionString="and \"currentHandler\"='"+userId+"' and (\"status\"="+WorkManageConstant.TASKORDER_ASSIGNED+" or \"status\"="+WorkManageConstant.TASKORDER_UPGRADING+") and \"status\"<>"+WorkManageConstant.TASKORDER_CLOSED;
			
			//查询条件
			if(conditionValue!=null && !"".equals(conditionValue)){
				
			}
			
			Map<String,Object> returnMap=this.commonQueryService.commonQueryService(String.valueOf(start), String.valueOf(limit), sortName, sortOrder, queryEntityName, null, conditionString);
			if(returnMap!=null){
				list = (List<Map>)returnMap.get("entityList");
				totalCount=returnMap.get("count")+"";
			}
			
			//排序条件
			//如果是按距离排序，需求调用网络资源接口过滤
			if(sortCondition!=null && !sortCondition.isEmpty()){
				if(sortCondition.containsKey("distance")){
					if(list!=null && !list.isEmpty()){
						
						Map<String,Double> roomDistanceMap=new HashMap<String,Double>();
						
						if(longitude!=null && !"".equals(longitude.trim())){
							terminal_longitude=Double.valueOf(longitude);
						}
						
						if(latitude!=null && !"".equals(latitude.trim())){
							terminal_latitude=Double.valueOf(latitude);
						}
						
						for(int i=0;i<list.size();i++){
							Map tempMap=list.get(i);
							tempMap.put("totalCount",totalCount);
							//获取任务关联的资源
							String woId = tempMap.get("woId")==null?"":tempMap.get("woId").toString();
							//获取工单关联的站址Id
							List<Workorderassnetresource> resList = this.workOrderAssnetResourceDao.getWorkOrderAssnetResourceRecordDao("woId", woId);
							if(resList!=null && !resList.isEmpty()){
								for (Workorderassnetresource res : resList) {
									Long resourceId = res.getStationId();
									String resourceType = res.getNetworkResourceType();
									tempMap.put("resourceId", resourceId);
									tempMap.put("resourceType", WorkManageConstant.NETWORKRESOURCE_STATION);
								}
							}
							
							//获取站址id
							String resourceId=tempMap.get("resourceId")==null?"":tempMap.get("resourceId").toString();
							Map<String,String> stationMap=this.networkResourceInterfaceService.getResourceByReIdAndReTypeService(resourceId,  WorkManageConstant.NETWORKRESOURCE_STATION);
							
							if(stationMap!=null && !stationMap.isEmpty()){
									
								double station_longitude=stationMap.get("longitude")==null?0:Double.valueOf(stationMap.get("longitude").toString());
								double station_latitude=stationMap.get("latitude")==null?0:Double.valueOf(stationMap.get("latitude").toString());
								
								double gpsDistance=LatLngHelper.Distance(terminal_longitude, terminal_latitude, station_longitude, station_latitude);
								
								//存储机房对应终端location的距离
								tempMap.put("distance", String.format("%.2f",gpsDistance));
							}else{
								logger.info("根据资源类型【站址】和资源id："+resourceId+"，获取站址类型对象失败");
							}
						}
					}
					
					//迭代比较任务所对应资源的距离
					for (int k = 0; k < list.size(); k++){
	                     for (int j = list.size() - 1; j > k; j--){
	                    	  final Map vo_j = list.get(j);
	                          final Map vo_k = list.get(j - 1);   
	                          double distance1=Double.valueOf(vo_j.get("distance")==null?"0":vo_j.get("distance").toString());
	                          double distance2=Double.valueOf(vo_k.get("distance")==null?"0":vo_k.get("distance").toString());
	                          if (distance1 < distance2){
	                               final Map temp = vo_k;
	                               list.remove(j - 1);
	                               list.add(j - 1, vo_j);
	                               list.remove(j);
	                               list.add(j, temp);
	                          }   
	                      }   
					}
					
				}else if(sortCondition.containsKey("requireCompleteTime")){
					if(list!=null && !list.isEmpty()){
						for (int k = 0; k < list.size(); k++){
		                     for (int j = list.size() - 1; j > k; j--){
		                    	  final Map vo_j = list.get(j);
		                          final Map vo_k = list.get(j - 1);   
		                          Date requireCompleteTime1=vo_j.get("requireCompleteTime")==null?null:TimeFormatHelper.setTimeFormat(vo_j.get("requireCompleteTime").toString());
		                          Date requireCompleteTime2=vo_k.get("requireCompleteTime")==null?null:TimeFormatHelper.setTimeFormat(vo_k.get("requireCompleteTime").toString());
		                          if (requireCompleteTime1!=null && requireCompleteTime2!=null){
		                        	  if(requireCompleteTime1.getTime()<requireCompleteTime2.getTime()){
		                        		  final Map temp = vo_k;   
			                              list.remove(j - 1);   
			                              list.add(j - 1, vo_j);   
			                              list.remove(j);   
			                              list.add(j, temp);  
		                        	  }
		                          }
		                      }
						}
					}
				}
			}
			
//			sql = "select * from view_workmanage_urgentrepair_taskorder t1 where t1.currentHandler=? and (status="+WorkManageConstant.TASKORDER_ASSIGNED+" or status="+WorkManageConstant.TASKORDER_UPGRADING+") and status<>"+WorkManageConstant.TASKORDER_CLOSED+" order by assignTime desc "+ "limit " + (pageIndex-1)*pageSize + "," + pageSize;
//			List<String> params=new ArrayList<String>();
//			params.add(userId);
//			list=this.dataSelectUtil.selectDataWithCondition(sql, params);
			
			return list;
		}else if("superviseTaskOrder".equals(taskOrderType)){
			
		}
		return list;
	}
	
	
	
	/**
	 * 分页获取用户的待办巡检任务
	 * @param userId 用户id
	 * @param pageIndex 当前页
	 * @param pageSize 行数
	 * @param taskOrderType
	 * @param conditionValue
	 * @param sortCondition
	 * @return
	 */
	public List<Map> getUserRoutineInspectionTaskOrderWithPageForMobile(String userId, int pageIndex, int pageSize ,String taskOrderType,String conditionValue,Map<String,String> sortCondition,String longitude,String latitude){
		if(userId==null||"".equals(userId)){
			return null;
		}
		
		String resourceType="Room";
		String selectReType="Station";
		String associatedType="PARENT";
		
		List<Map> resultList=new ArrayList<Map>();
		
		
		if("pendingTaskOrder".equals(taskOrderType)){		//获取待办任务单
			
			
			//获取用户群组任务
			List<FlowTaskInfo> taskInfoList=new ArrayList<FlowTaskInfo>();	//任务列表结果集
			
			//获取人员所属的组织
			//List<ProviderOrganization> orgList=this.providerOrganizationService.getOrgByAccountService(userId);
			//yuan.yw
			List<SysOrg> orgList=this.sysOrganizationService.getOrgByAccountService(userId);
			
			if(orgList!=null && !orgList.isEmpty()){
				for(SysOrg org:orgList){
//					System.out.println("org=="+org.getId());
					List<FlowTaskInfo> groupTaskList=null;
					try {
						groupTaskList=(List<FlowTaskInfo>)this.workFlowService.queryShareTasks(ProcessConstants.AcceptorType.ACCEPTOR_ORGROLE, String.valueOf(org.getOrgId()), ProcessConstants.TaskStatus.INIT);
					} catch (WFException e) {
						e.printStackTrace();
						logger.error("根据组织【"+org.getOrgId()+"】，获取对应的群组任务失败");
					}
					if(groupTaskList!=null && !groupTaskList.isEmpty()){
						taskInfoList.addAll(groupTaskList);
					}
				}
			}
			
			
			//获取用户个人的任务
			List<FlowTaskInfo> personalTaskList=null;
			try {
				personalTaskList = (List<FlowTaskInfo>)this.workFlowService.queryOwnTasks(ProcessConstants.AcceptorType.ACCEPTOR_PEOPLE, userId, ProcessConstants.TaskStatus.HANDLING);
			} catch (WFException e) {
				e.printStackTrace();
				logger.error("获取用户【"+userId+"】，的个人任务失败");
			}
			
			if(personalTaskList!=null && !personalTaskList.isEmpty()){
				taskInfoList.addAll(personalTaskList);
			}
			
			if(taskInfoList!=null && !taskInfoList.isEmpty()){
				
				Map<String,FlowTaskInfo> flowTaskInfoMap=new HashMap<String,FlowTaskInfo>();
				for(FlowTaskInfo tempFlowTaskInfo:taskInfoList){
					String flowInstanceId=tempFlowTaskInfo.getInstanceId();
					if(flowInstanceId!=null && !"".equals(flowInstanceId)){
						flowTaskInfoMap.put(flowInstanceId, tempFlowTaskInfo);
					}
				}
				
				//调用通用查询接口
				int start=(pageIndex-1)*pageSize;
				int limit=pageSize;
				String sortName="\"assignTime\"";
				String sortOrder="desc";
				String queryEntityName="V_WM_INSP_PENDINGTASKORDER";
				String conditionString="and \"status\"<>"+WorkManageConstant.TASKORDER_INSPECT_CLOSED;
				
				String orgIds="";
				
				if(orgList!=null && !orgList.isEmpty()){
					for(SysOrg org:orgList){
						orgIds=orgIds+org.getOrgId()+",";
					}
					orgIds=orgIds.substring(0, orgIds.length()-1);
					
				}
				
				if(orgIds!=null && !orgIds.isEmpty()){
					conditionString=conditionString+" and \"orgId\" in ("+orgIds+")";
				}
				
				
				//查询条件
				if(conditionValue!=null && !"".equals(conditionValue)){
					conditionString=conditionString+" and (\"toId\" like '%"+conditionValue+"%' or \"toTitle\" like '%"+conditionValue+"%')";
				}
				
				//排序条件
				if(sortCondition!=null && !sortCondition.isEmpty()){
					
					//如果不是按距离排序
					if(!sortCondition.containsKey("distance")){
						for(Map.Entry<String, String> entry:sortCondition.entrySet()){
//							sortName=sortName+","+entry.getKey();
//							sortOrder=sortOrder+","+entry.getValue();
							sortName=entry.getKey();
							sortOrder=entry.getValue();
						}
					}
					
				}
				
				List<Map> list=null;
				Map<String,Object> returnMap=this.commonQueryService.commonQueryService(String.valueOf(start), String.valueOf(limit), sortName, sortOrder, queryEntityName, null, conditionString);
				if(returnMap!=null){
					list = (List<Map>)returnMap.get("entityList");
				}
				
				//如果是按距离排序，需求调用网络资源接口过滤
				if(sortCondition!=null && !sortCondition.isEmpty()){
					if(sortCondition.containsKey("distance")){
						if(list!=null && !list.isEmpty()){
							
							
							if(longitude!=null && !"".equals(longitude.trim())){
								terminal_longitude=Double.valueOf(longitude);
							}
							
							if(latitude!=null && !"".equals(latitude.trim())){
								terminal_latitude=Double.valueOf(latitude);
							}
							
							
							for(int i=0;i<list.size();i++){
								Map tempMap=list.get(i);
								
								//获取机房id
								String resourceId=tempMap.get("resourceId")==null?"":tempMap.get("resourceId").toString();
								List<Map<String,String>> stationList=this.networkResourceInterfaceService.getResourceService(resourceId,resourceType,selectReType,associatedType);
								
								Map<String,String> resultMap=null;
								if(stationList!=null && !stationList.isEmpty()){
									resultMap=stationList.get(0);
									if(resultMap!=null && !resultMap.isEmpty()){
										
										double station_longitude=resultMap.get("longitude")==null?0:Double.valueOf(resultMap.get("longitude").toString());
										double station_latitude=resultMap.get("latitude")==null?0:Double.valueOf(resultMap.get("latitude").toString());
										
										double gpsDistance=LatLngHelper.Distance(terminal_longitude, terminal_latitude, station_longitude, station_latitude);
										
										//存储机房对应终端location的距离
										tempMap.put("distance", String.format("%.2f",gpsDistance));
										
									}else{
										logger.info("根据资源类型【机房】和资源id："+resourceId+"，获取站址类型的父级对象失败");
									}
								}else{
									logger.info("根据资源类型【机房】和资源id："+resourceId+"，获取站址类型的父级对象失败");
								}
							}
							//迭代比较任务所对应资源的距离
							for (int k = 0; k < list.size(); k++){
			                     for (int j = list.size() - 1; j > k; j--){
			                    	  final Map vo_j = list.get(j);
			                          final Map vo_k = list.get(j - 1);   
			                          double distance1=Double.valueOf(vo_j.get("distance")==null?"0":vo_j.get("distance").toString());
			                          double distance2=Double.valueOf(vo_k.get("distance")==null?"0":vo_k.get("distance").toString());
			                          if (distance1 < distance2){   
			                               final Map temp = vo_k;   
			                               list.remove(j - 1);   
			                               list.add(j - 1, vo_j);   
			                               list.remove(j);   
			                               list.add(j, temp);   
			                          }   
			                      }   
							}
						}else{
							logger.info("按距离排序获取巡检任务单的集合为空");
						}
						
//						System.out.println("排序前：\r\n"+resultList);
//						resultList.clear();
						
						
					}else if(sortCondition.containsKey("taskPlanBeginTime")){	//开始时间
						if(list!=null && !list.isEmpty()){
							
							
							if(longitude!=null && !"".equals(longitude.trim())){
								terminal_longitude=Double.valueOf(longitude);
							}
							
							if(latitude!=null && !"".equals(latitude.trim())){
								terminal_latitude=Double.valueOf(latitude);
							}
							
							
							for(int i=0;i<list.size();i++){
								Map tempMap=list.get(i);
								
								//获取机房id
								String resourceId=tempMap.get("resourceId")==null?"":tempMap.get("resourceId").toString();
								List<Map<String,String>> stationList=this.networkResourceInterfaceService.getResourceService(resourceId,resourceType,selectReType,associatedType);
								
								Map<String,String> resultMap=null;
								if(stationList!=null && !stationList.isEmpty()){
									resultMap=stationList.get(0);
									if(resultMap!=null && !resultMap.isEmpty()){
										
										double station_longitude=resultMap.get("longitude")==null?0:Double.valueOf(resultMap.get("longitude").toString());
										double station_latitude=resultMap.get("latitude")==null?0:Double.valueOf(resultMap.get("latitude").toString());
										
										double gpsDistance=LatLngHelper.Distance(terminal_longitude, terminal_latitude, station_longitude, station_latitude);
										
										//存储机房对应终端location的距离
										tempMap.put("distance", String.format("%.2f",gpsDistance));
										
									}else{
										logger.info("根据资源类型【机房】和资源id："+resourceId+"，获取站址类型的父级对象失败");
									}
								}else{
									logger.info("根据资源类型【机房】和资源id："+resourceId+"，获取站址类型的父级对象失败");
								}
							}
							
							
							
							for (int k = 0; k < list.size(); k++){
			                     for (int j = list.size() - 1; j > k; j--){
			                    	  final Map vo_j = list.get(j);
			                          final Map vo_k = list.get(j - 1);   
			                          Date taskPlanBeginTime1=vo_j.get("taskPlanBeginTime")==null?null:TimeFormatHelper.setTimeFormat(vo_j.get("taskPlanBeginTime").toString());
			                          Date taskPlanBeginTime2=vo_k.get("taskPlanBeginTime")==null?null:TimeFormatHelper.setTimeFormat(vo_k.get("taskPlanBeginTime").toString());
			                          if (taskPlanBeginTime1!=null && taskPlanBeginTime2!=null){
			                        	  if(taskPlanBeginTime1.getTime() < taskPlanBeginTime2.getTime()){
			                        		  final Map temp = vo_k;   
				                              list.remove(j - 1);   
				                              list.add(j - 1, vo_j);   
				                              list.remove(j);   
				                              list.add(j, temp);  
			                        	  }
			                          }
			                      }
							}
						}else{
							logger.info("按任务开始时间排序获取巡检任务单的集合为空");
						}
					}else if(sortCondition.containsKey("requireCompleteTime")){		//要求完成时间
						if(list!=null && !list.isEmpty()){
							
							
							if(longitude!=null && !"".equals(longitude.trim())){
								terminal_longitude=Double.valueOf(longitude);
							}
							
							if(latitude!=null && !"".equals(latitude.trim())){
								terminal_latitude=Double.valueOf(latitude);
							}
							
							
							for(int i=0;i<list.size();i++){
								Map tempMap=list.get(i);
								
								//获取机房id
								String resourceId=tempMap.get("resourceId")==null?"":tempMap.get("resourceId").toString();
								List<Map<String,String>> stationList=this.networkResourceInterfaceService.getResourceService(resourceId,resourceType,selectReType,associatedType);
								
								Map<String,String> resultMap=null;
								if(stationList!=null && !stationList.isEmpty()){
									resultMap=stationList.get(0);
									if(resultMap!=null && !resultMap.isEmpty()){
										
										double station_longitude=resultMap.get("longitude")==null?0:Double.valueOf(resultMap.get("longitude").toString());
										double station_latitude=resultMap.get("latitude")==null?0:Double.valueOf(resultMap.get("latitude").toString());
										
										double gpsDistance=LatLngHelper.Distance(terminal_longitude, terminal_latitude, station_longitude, station_latitude);
										
										//存储机房对应终端location的距离
										tempMap.put("distance", String.format("%.2f",gpsDistance));
										
									}else{
										logger.info("根据资源类型【机房】和资源id："+resourceId+"，获取站址类型的父级对象失败");
									}
								}else{
									logger.info("根据资源类型【机房】和资源id："+resourceId+"，获取站址类型的父级对象失败");
								}
							}
							
							
							
							for (int k = 0; k < list.size(); k++){
			                     for (int j = list.size() - 1; j > k; j--){
			                    	  final Map vo_j = list.get(j);
			                          final Map vo_k = list.get(j - 1);   
			                          Date requireCompleteTime1=vo_j.get("requireCompleteTime")==null?null:TimeFormatHelper.setTimeFormat(vo_j.get("requireCompleteTime").toString());
			                          Date requireCompleteTime2=vo_k.get("requireCompleteTime")==null?null:TimeFormatHelper.setTimeFormat(vo_k.get("requireCompleteTime").toString());
			                          if (requireCompleteTime1!=null && requireCompleteTime2!=null){
			                        	  if(requireCompleteTime1.getTime() < requireCompleteTime2.getTime()){
			                        		  final Map temp = vo_k;   
				                              list.remove(j - 1);   
				                              list.add(j - 1, vo_j);   
				                              list.remove(j);   
				                              list.add(j, temp);  
			                        	  }
			                          }
			                      }
							}
						}else{
							logger.info("按任务要求完成时间排序获取巡检任务单的集合为空");
						}
					}
				}else{
					
					//为资源添加离当前终端的位置
					if(list!=null && !list.isEmpty()){
						
						if(longitude!=null && !"".equals(longitude.trim())){
							terminal_longitude=Double.valueOf(longitude);
						}
						
						if(latitude!=null && !"".equals(latitude.trim())){
							terminal_latitude=Double.valueOf(latitude);
						}
						
						
						for(int i=0;i<list.size();i++){
							Map tempMap=list.get(i);
							
							//获取机房id
							String resourceId=tempMap.get("resourceId")==null?"":tempMap.get("resourceId").toString();
							List<Map<String,String>> stationList=this.networkResourceInterfaceService.getResourceService(resourceId,resourceType,selectReType,associatedType);
							
							Map<String,String> resultMap=null;
							if(stationList!=null && !stationList.isEmpty()){
								resultMap=stationList.get(0);
								if(resultMap!=null && !resultMap.isEmpty()){ 
									
									double station_longitude=resultMap.get("longitude")==null?0:Double.valueOf(resultMap.get("longitude").toString());
									double station_latitude=resultMap.get("latitude")==null?0:Double.valueOf(resultMap.get("latitude").toString());
									
									double gpsDistance=LatLngHelper.Distance(terminal_longitude, terminal_latitude, station_longitude, station_latitude);
									
									//存储机房对应终端location的距离
									tempMap.put("distance",String.format("%.2f",gpsDistance));
									
									
								}else{
									logger.info("根据资源类型【机房】和资源id："+resourceId+"，获取站址类型的父级对象失败");
								}
							}else{
								logger.info("根据资源类型【机房】和资源id："+resourceId+"，获取站址类型的父级对象失败");
							}
						}
					}
				}
				
//				//查询工作管理的任务表
//				String sql= "";
//				if(conditionValue==null || "".equals(conditionValue)){
//					sql= "select * from view_workmanage_routineinspection_taskorder t1 where status<>"+WorkManageConstant.TASKORDER_CLOSED+" order by assignTime desc " 
//					+ "limit " + (pageIndex-1)*pageSize + "," + pageSize;
//				}else{
//					sql= "select * from view_workmanage_routineinspection_taskorder t1 where status<>"+WorkManageConstant.TASKORDER_CLOSED+" and (toId like '%"+conditionValue+"%' or toTitle like '%"+conditionValue+"%') order by assignTime desc " 
//					+ "limit " + (pageIndex-1)*pageSize + "," + pageSize;
//				}
//				List<String> params=new ArrayList<String>();
//				List<Map> list  = this.dataSelectUtil.selectDataWithCondition(sql, params);
				
				if(list!=null && !list.isEmpty()){
					for(Map tempMap:list){
						String processInstanceId=tempMap.get("processInstId")==null?"":tempMap.get("processInstId").toString();
						if(flowTaskInfoMap.containsKey(processInstanceId)){
							resultList.add(tempMap);
						}
					}
				}
//				System.out.println("排序后：\r\n"+resultList);
			}
		}
		return resultList;
	}

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}
	
	
	
	
}
