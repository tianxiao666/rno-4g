package com.iscreate.op.service.workmanage;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.workmanage.WorkmanageBizprocessTaskinfoConf;
import com.iscreate.op.pojo.workmanage.WorkmanageCountWorkorderObject;
import com.iscreate.op.pojo.workmanage.WorkmanageStatusreg;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorderResourceAssociate;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.workmanage.util.CommonTools;
import com.iscreate.op.service.workmanage.util.DataSelectUtil;
import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.datainput.FlowTaskInfo;
import com.iscreate.plat.workflow.processor.constants.ProcessConstants;
import com.iscreate.plat.workflow.serviceaccess.ServiceBean;

public class WorkOrderHandleServiceImpl implements WorkOrderHandleService {

	private HibernateTemplate hibernateTemplate;
	private ServiceBean workFlowService;
	private BizprocessTaskinfoConfService bizprocessTaskinfoConfService;
	private WorkFlowInfoService workFlowInfoService;
	private WorkmanageStatusregService workmanageStatusregService;
	private CommonQueryService commonQueryService;
	private SysOrgUserService sysOrgUserService;
	
	
	private DataSelectUtil dataSelectUtil;
	
	private static final String PROCESS_END_FLAG="ended";
	private static final String PROCESS_ENDTASK_NAME="end1";
	
	private static final Log logger = LogFactory.getLog(WorkOrderHandleServiceImpl.class);

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void setWorkFlowService(ServiceBean workFlowService) {
		this.workFlowService = workFlowService;
	}

	public void setBizprocessTaskinfoConfService(
			BizprocessTaskinfoConfService bizprocessTaskinfoConfService) {
		this.bizprocessTaskinfoConfService = bizprocessTaskinfoConfService;
	}
	
	public void setWorkFlowInfoService(WorkFlowInfoService workFlowInfoService) {
		this.workFlowInfoService = workFlowInfoService;
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

	/**
	 * 根据工单id，获取工单对象
	 * 
	 * @param woId
	 */
	public WorkmanageWorkorder getWorkOrderByWoId(String woId) {
		logger.info("into method getWorkOrderByWoId(),根据woId【"+woId+"】去获取工单对象");
		WorkmanageWorkorder workOrder = null;
		String sql = "select o from WorkmanageWorkorder o where o.woId=?";
		List list = null;
		try {
			list=this.hibernateTemplate.find(sql, woId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据工单id【"+woId+"】获取工单对象时，数据库链接错误");
		}
		if (list != null && !list.isEmpty()) {
			workOrder = (WorkmanageWorkorder) list.get(0);
		}
		return workOrder;
	}
	
	
	/**
	 * 更新公共工单对象
	 * @param workmanageWorkorder
	 */
	public void updateWorkOrder(WorkmanageWorkorder workmanageWorkorder){
		this.hibernateTemplate.update(workmanageWorkorder);
	}
	
	
	/**
	 * 根据工单id获取用于显示的工单信息
	 * @param woId
	 * @return
	 */
	public Map getWorkOrderByWoIdForShowProcess(String woId){
		Map<String,String> map=null;
		
		
//		List<String> params=new ArrayList<String>();
//		params.add(woId);
//		String sql="select * from view_workmanage_urgentrepair_workorder where woId=?";
//		List<Map> list=this.dataSelectUtil.selectDataWithCondition(sql, params);
//		if(list!=null && !list.isEmpty()){
//			map=list.get(0);
//		}
		
		
		//调用通用查询接口
		String queryEntityName="V_WM_URGENTREPAIR_WORKORDER";
		String conditionString="and \"woId\"='"+woId+"'";
		
		List<Map> list  = null;
		Map<String,Object> returnMap=this.commonQueryService.commonQueryService(null,null, null, null, queryEntityName, null, conditionString);
		if(returnMap!=null){
			list = (List<Map>)returnMap.get("entityList");
		}
		
		if(list!=null && !list.isEmpty()){
			map=list.get(0);
		}
		
		return map;
		

//		List<Map> resultList=this.hibernateTemplate.execute(new HibernateCallback<List<Map>>(){
//			public List<Map> doInHibernate(Session session)
//					throws HibernateException, SQLException {
//				final String sql="select * from view_workmanage_urgentrepair_workorder where woId=?";
//				Query query=session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//				query.setParameter(0, woId);
//				List<Map> list=query.list();
//				return list;
//			}
//			
//		});
//		
//		if(resultList!=null && !resultList.isEmpty()){
//			map=resultList.get(0);
//		}
	}
	
	/**
	 * 根据工单id获取用于显示的工单信息by Hibernate
	 * @param woId
	 * @return
	 */
	public Map getWorkOrderByWoIFowShowByHibernate(final String woId){
		Map resultMap=new HashMap();
		WorkmanageWorkorder wm_workOrder=this.getWorkOrderByWoId(woId);
		if(resultMap!=null){
			CommonTools.getValueOfObjectByPropertyName(wm_workOrder,resultMap);
		}
		
		//获取节点的配置信息
		WorkmanageBizprocessTaskinfoConf taskNodeInfo=this.bizprocessTaskinfoConfService.getWorkmanageBizprocessTaskinfoConfByCondition(wm_workOrder.getProcessDefineId(), wm_workOrder.getCurrentTaskName());
		if(resultMap!=null){
			CommonTools.getValueOfObjectByPropertyName(taskNodeInfo,resultMap);
		}
		
		//获取工单状态信息
		if(resultMap!=null){
			String statusId=resultMap.get("status")==null?"":resultMap.get("status").toString();
			if(statusId!=null && !statusId.isEmpty()){
				WorkmanageStatusreg statusReg=this.workmanageStatusregService.getWorkmanageStatusregById(Long.valueOf(statusId));
				CommonTools.getValueOfObjectByPropertyName(statusReg,resultMap);
			}
			
		}
		
		
		
//		List<Map> resultList=this.hibernateTemplate.execute(new HibernateCallback<List<Map>>(){
//			public List<Map> doInHibernate(Session session)
//					throws HibernateException, SQLException {
//				final String sql="select * from view_workmanage_urgentrepair_workorder where woId=?";
//				Query query=session.createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//				query.setParameter(0, woId);
//				List<Map> list=query.list();
//				return list;
//			}
//			
//		});
//		if(resultList!=null && !resultList.isEmpty()){
//			resultMap=resultList.get(0);
//		}
		return resultMap;
	}
	
	
	/**
	 * 根据工单id获取用于显示的车辆工单信息
	 * @param woId
	 * @return
	 */
	public Map getCarDispatchWorkOrderByWoIdForShowProcess(String woId){
		Map<String,String> map=null;
//		List<String> params=new ArrayList<String>();
//		params.add(woId);
//		String sql="select * from view_workmanage_cardispatch_workorder where woId=?";
//		List<Map> list=this.dataSelectUtil.selectDataWithCondition(sql, params);
//		if(list!=null && !list.isEmpty()){
//			map=list.get(0);
//		}
		
		//调用通用查询接口
		String queryEntityName="V_WM_CAR_WORKORDER";
		String conditionString="and \"woId\"='"+woId+"'";
		
		List<Map> list  = null;
		Map<String,Object> returnMap=this.commonQueryService.commonQueryService(null,null, null, null, queryEntityName, null, conditionString);
		if(returnMap!=null){
			list = (List<Map>)returnMap.get("entityList");
		}
		
		if(list!=null && !list.isEmpty()){
			map=list.get(0);
		}
		
		return map;
	}
	
	
	/**
	 * 根据工单id获取用于显示的巡检工单信息
	 * @param woId
	 * @return
	 */
	public Map getRoutineInspectionWorkOrderByWoIdForShowProcess(String woId){
		Map<String,String> map=null;
//		List<String> params=new ArrayList<String>();
//		params.add(woId);
//		String sql="select * from view_workmanage_routineinspection_workorder where woId=?";
//		List<Map> list=this.dataSelectUtil.selectDataWithCondition(sql, params);
//		if(list!=null && !list.isEmpty()){
//			map=list.get(0);
//		}
		
		//调用通用查询接口
		String queryEntityName="V_WM_INSP_WORKORDER";
		String conditionString="and \"woId\"='"+woId+"'";
		
		List<Map> list  = null;
		Map<String,Object> returnMap=this.commonQueryService.commonQueryService(null,null, null, null, queryEntityName, null, conditionString);
		if(returnMap!=null){
			list = (List<Map>)returnMap.get("entityList");
		}
		
		if(list!=null && !list.isEmpty()){
			map=list.get(0);
		}
		
		return map;
	}
	

	/**
	 * 删除工单对象
	 * 
	 * @param workOrder
	 */
	public void deleteWorkOrder(WorkmanageWorkorder workOrder) {
		try {
			this.hibernateTemplate.delete(workOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建工单流程
	 * 
	 * @param processDefineId
	 *            流程定义id
	 * @param workOrder
	 *            公共工单对象
	 * @param participantList
	 *            参与者列表
	 * @return
	 */
	public String createWordOrderProcess(String processDefineId,
			WorkmanageWorkorder workOrder, List<String> participantList) {
		String processInstanceId = "";
		
		// 保存公共工单信息对象
		this.hibernateTemplate.save(workOrder);

		// 提供流程变量信息
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("WOID", workOrder.getWoId());// 将工单号存至流程变量,用于下个任务环节初始化表单时调用
		StringBuffer sb_participant = new StringBuffer();
		if (participantList != null && !participantList.isEmpty()) {
			for (String participant : participantList) {
				sb_participant.append(participant).append(",");
			}
			sb_participant.delete(sb_participant.length() - 1,
					sb_participant.length());
		}

		// 指定接收者
		variables.put(ProcessConstants.VariableKey.ASSIGNEE_TYPE,
				ProcessConstants.AcceptorType.ACCEPTOR_PEOPLE);
		variables.put(ProcessConstants.VariableKey.ASSIGNEE_ID,
				sb_participant.toString());

		// 工作流程引擎启动新流程
		try {
			processInstanceId = this.workFlowService.startWorkflow(
					processDefineId, workOrder.getCreator(),
					ProcessConstants.StarterType.STARTER_PEOPLE, workOrder
							.getCreator(), workOrder.getWoId(), variables);
		} catch (WFException e) {
			e.printStackTrace();
			logger.error("启动工作流程失败");
		}
		
		if(processInstanceId!=null && !"".equals(processInstanceId)){
			//流程实例化成功后，更新工单信息
			workOrder.setProcessDefineId(processDefineId);
			workOrder.setProcessInstId(processInstanceId);
			this.hibernateTemplate.update(workOrder);

			boolean initFlag = this.initCommonWorkOrder(processDefineId,
					processInstanceId,participantList, variables);

			logger.info("initFlag===" + initFlag);
		}
		return processInstanceId;
	}

	/**
	 * 初始化公共工单信息
	 * @param processDefineId 流程定义id
	 * @param processInstId 流程实例id
	 * @param participantList 参与者列表
	 * @param variables 流程变量
	 * @return
	 */
	private boolean initCommonWorkOrder(String processDefineId,
			String processInstId,List<String> participantList,
			Map<String, Object> variables) {
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
			
			// 更新工单对象信息
			String woId = variables.get("WOID").toString();
			WorkmanageWorkorder update_workOrder = this.getWorkOrderByWoId(woId);
			if (participantList != null && !participantList.isEmpty()) {
				String userId=participantList.get(0);
				//ou.jh
				SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(userId);
//				Staff staff=this.providerOrganizationService.getStaffByAccount(userId);
				
				update_workOrder.setCurrentHandler(userId);
				if(sysOrgUserByAccount!=null){
					update_workOrder.setCurrentHandlerName(sysOrgUserByAccount.getName());
				}
				
				update_workOrder.setCurrentTaskId(taskId);
				update_workOrder.setCurrentTaskName(taskName);
				if (taskInfoConf != null) {
					update_workOrder.setStatus(taskInfoConf.getTaskInitStatus());
				}
				this.hibernateTemplate.update(update_workOrder);
			}
		}else{
			//判断流程是否已经结束
			boolean isEnded=this.workFlowInfoService.judgeProcessIsEnd(processInstId);
			
			if(isEnded){	//如果是，更新工单信息
				WorkmanageBizprocessTaskinfoConf taskInfoConf = this.bizprocessTaskinfoConfService
				.getWorkmanageBizprocessTaskinfoConfByCondition(
						processDefineId, PROCESS_ENDTASK_NAME);
		
				// 更新工单对象信息
				String woId = variables.get("WOID").toString();
				WorkmanageWorkorder update_workOrder = this.getWorkOrderByWoId(woId);
				
				update_workOrder.setCurrentTaskId(PROCESS_END_FLAG);
				update_workOrder.setCurrentTaskName(PROCESS_ENDTASK_NAME);
				if (taskInfoConf != null) {
					update_workOrder.setStatus(taskInfoConf.getTaskInitStatus());
				}
				this.hibernateTemplate.update(update_workOrder);
			}
		}
		isSuccess = true;

		return isSuccess;
	}
	
	/**
	 * 受理工单
	 * @param woId 工单id
	 * @param acceptPeople 受理人
	 */
	public boolean acceptWorkOrder(String woId,String acceptPeople){
		boolean isSuccess = false;
		
		//获取工单
		WorkmanageWorkorder updateWorkOrder=this.getWorkOrderByWoId(woId);
		
		//修改工单
		updateWorkOrder.setStatus(WorkManageConstant.WORKORDER_HANDLING);
		updateWorkOrder.setAcceptPeople(acceptPeople);
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(acceptPeople);
//		Staff staff=this.providerOrganizationService.getStaffByAccount(acceptPeople);
		if(sysOrgUserByAccount!=null){
			updateWorkOrder.setAcceptPeopleName(sysOrgUserByAccount.getName());
		}
		this.hibernateTemplate.update(updateWorkOrder);
		isSuccess=true;
		
		return isSuccess;
	}

	/**
	 * 结束工单流程任务
	 * @param woId 工单id
	 * @param currentHandler 当前处理人
	 * @param outcome 流向名称
	 * @param participantList 参与者列表
	 * @return
	 */
	public boolean completeWordOrderProcessTask(String woId, String currentHandler,String outcome, List<String> participantList) {
		boolean isSuccess = false;
		String pdId="";	//流程定义id
		String piId="";	//流程实例id
		String taskId="";
		// 获取工单对象信息
		WorkmanageWorkorder wm_workOrder = this.getWorkOrderByWoId(woId);
		pdId=wm_workOrder.getProcessDefineId();
		piId=wm_workOrder.getProcessInstId();
		taskId=wm_workOrder.getCurrentTaskId();
		
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

			
			variables.put("WOID", woId);

			
			// 结束任务的时候，要先判断是否是群组任务，如果是，要先take
//			boolean isCompleteSuccess = false;
			boolean isGroup = this.workFlowInfoService.judegeTaskIsGroup(taskId);
			if (isGroup) {
				boolean isTakeSuccess =false;
				try {
					isTakeSuccess=this.workFlowService.takeTask(taskId, currentHandler);
				} catch (WFException e1) {
					e1.printStackTrace();
				}
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
			
			//更改工单最终回复时间
			wm_workOrder.setFinalCompleteTime(new Date());
			
			//ou.jh
			SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(currentHandler);
//			Staff staff=this.providerOrganizationService.getStaffByAccount(currentHandler);
			
			wm_workOrder.setLastReplyPeople(currentHandler);
			if(sysOrgUserByAccount!=null){
				wm_workOrder.setLastReplyPeopleName(sysOrgUserByAccount.getName());
			}
			
			this.hibernateTemplate.update(wm_workOrder);
			
			
			isSuccess=this.initCommonWorkOrder(pdId, piId,participantList, variables);
		}

		return isSuccess;
	}

	
	
	
	/**
	 * 更改工单状态
	 * @param woId
	 * @param status
	 * @return
	 */
	public boolean updateWorkOrderStatusProcess(String woId,int status){
		boolean isSuccess=false;
		//获取工单
		WorkmanageWorkorder updateWorkOrder=this.getWorkOrderByWoId(woId);
		if(updateWorkOrder!=null){
			//修改工单
			updateWorkOrder.setStatus(status);
			this.hibernateTemplate.update(updateWorkOrder);
			isSuccess=true;
		}
		return isSuccess;
	}
	
	/**
	 * 更改工单ReadStatus状态
	 * @param woId
	 * @param readStatus
	 * @return
	 */
	public boolean updateWorkOrderReadStatusProcess(String woId,int readStatus){
		boolean isSuccess=false;
		try {
			
			//获取工单
			WorkmanageWorkorder updateWorkOrder=this.getWorkOrderByWoId(woId);
			
			//修改工单
			updateWorkOrder.setIsRead(readStatus);
			this.hibernateTemplate.update(updateWorkOrder);
			
			isSuccess=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	
	/**
	 * 根据用户账号与工单号，判断当前用户是否是该工单的当前处理人
	 * @param woId
	 * @param currentUser
	 * @return
	 */
	public boolean judgeWorkOrderCurrentHandlerProcess(String woId,String currentUser){
		boolean isYes=false;
		//获取工单
		WorkmanageWorkorder workOrder=this.getWorkOrderByWoId(woId);
		if(workOrder!=null && currentUser.equals(workOrder.getCurrentHandler())){
			isYes=true;
		}
		return isYes;
	}
	
	/**
	 * 转派工单
	 * @param woId 工单id
	 * @param updateWorkOrder 需要更新的工单对象属性
	 * @return
	 */
	public boolean handoverWorkOrderProcess(String woId,WorkmanageWorkorder updateWorkOrder){
		boolean isSuccess=false;
		//获取工单
		WorkmanageWorkorder workOrder=this.getWorkOrderByWoId(woId);
		
		if(workOrder!=null && updateWorkOrder!=null){
			String oldParticipant=workOrder.getCurrentHandler();	//获取当前处理人
			String taskId=workOrder.getCurrentTaskId();	//获取工单所在任务节点id
			
			String newParticipant=updateWorkOrder.getCurrentHandler();	//获取新的当前处理人
			try {
				isSuccess=this.workFlowService.changeTaskAssignee(taskId, oldParticipant, newParticipant);
			} catch (WFException e) {
				e.printStackTrace();
				isSuccess=false;
			}
			
			//工作流更换成功
			if(isSuccess){
//				workOrder.setCurrentHandler(newParticipant);
//				Staff staff=this.providerOrganizationService.getStaffByAccount(newParticipant);
//				if(staff!=null){
//					workOrder.setCurrentHandlerName(staff.getName());
//				}
				workOrder.setCurrentHandler(newParticipant);
				workOrder.setCurrentHandlerName(updateWorkOrder.getCurrentHandlerName());
				
				if(updateWorkOrder.getOrderOwnerOrgId()!=null && updateWorkOrder.getOrderOwnerOrgId()!=0l){
					workOrder.setOrderOwnerOrgId(updateWorkOrder.getOrderOwnerOrgId());
				}
				
				if(updateWorkOrder.getOrderOwner()!=null && !"".equals(updateWorkOrder.getOrderOwner())){
					workOrder.setOrderOwner(updateWorkOrder.getOrderOwner());
				}
				
				this.hibernateTemplate.update(workOrder);
			}
		}
		return isSuccess;
	}
	

	/**
	 * 统计网络设施或人的工单数量（增加）
	 * @param countWorkorder
	 * @return
	 */
	public synchronized boolean countWorkOrderNumberForSave(WorkmanageCountWorkorderObject countWorkorder){
		boolean isSuccess=false;
		String hql="";
		//先查找该网络设施是否存在相应记录，如果没有，则save；如果有，则更新相应数量
		hql="select o from WorkmanageCountWorkorderObject o where o.resourceId=? and o.resourceType=?";
		List list=this.hibernateTemplate.find(hql,countWorkorder.getResourceId(),countWorkorder.getResourceType());
		if(list!=null && !list.isEmpty()){
			WorkmanageCountWorkorderObject updateCountWorkorder=(WorkmanageCountWorkorderObject)list.get(0);
			long updateCount=updateCountWorkorder.getTaskCount()+1;
			updateCountWorkorder.setTaskCount(updateCount);
			this.hibernateTemplate.update(updateCountWorkorder);
//			this.hibernateTemplate.getSessionFactory().openSession().update(updateCountWorkorder);
		}else{
			countWorkorder.setTaskCount(Long.valueOf("1"));
			this.hibernateTemplate.save(countWorkorder);
//			this.hibernateTemplate.getSessionFactory().openSession().update(countWorkorder);
		}
		isSuccess=true;
		return isSuccess;
	}
	
	
	/**
	 * 统计网络设施或人的工单数量（减去）
	 * @param countWorkorder
	 * @return
	 */
	public synchronized boolean countWorkOrderNumberForSubtract(WorkmanageCountWorkorderObject countWorkorder){
		boolean isSuccess=false;
		String hql="";
		
		//先查找该网络设施是否存在相应记录
		hql="select o from WorkmanageCountWorkorderObject o where o.resourceId=? and o.resourceType=?";
		List list=this.hibernateTemplate.find(hql,countWorkorder.getResourceId(),countWorkorder.getResourceType());
		if(list!=null && !list.isEmpty()){
			WorkmanageCountWorkorderObject updateCountWorkorder=(WorkmanageCountWorkorderObject)list.get(0);
			
			if(updateCountWorkorder!=null && updateCountWorkorder.getTaskCount()>0){
				long updateCount=updateCountWorkorder.getTaskCount()-1;
				updateCountWorkorder.setTaskCount(updateCount);
				this.hibernateTemplate.update(updateCountWorkorder);
			}
		}
		isSuccess=true;
		return isSuccess;
	}
	
	
	/**
	 * 工单驳回
	 * @param woId 工单id
	 * @return
	 */
	public boolean rejectWorkOrderProcess(String woId){
		boolean isSuccess=false;
		//获取工单对象
		WorkmanageWorkorder workOrder=this.getWorkOrderByWoId(woId);
		String piId=workOrder.getProcessInstId();
		
		workOrder.setStatus(WorkManageConstant.WORKORDER_CANCELED);	//更改为‘已撤销’
		String currentHandler=workOrder.getCurrentHandler();
		
		this.hibernateTemplate.update(workOrder);
		
		try {
			isSuccess=this.workFlowService.endWorkflowInstance(piId, currentHandler, "forceEnded");
		} catch (WFException e) {
			e.printStackTrace();
			isSuccess=false;
		}
		return isSuccess;
	}
	
	
	
	/**
	 * 根据资源类型与资源id，获取工单统计数量
	 * @param resourceType 资源类型
	 * @param resourceId 资源id
	 * @return
	 */
	public long getWorkOrderCountByResourceTypeAndResourceId(String resourceType,String resourceId){
		long count=-1l;
		String hql="select o from WorkmanageCountWorkorderObject o where o.resourceType=? and o.resourceId=?";
		List list=this.hibernateTemplate.find(hql, resourceType,resourceId);
		if(list!=null && !list.isEmpty()){
			WorkmanageCountWorkorderObject workOrder_count=(WorkmanageCountWorkorderObject)list.get(0);
			count=workOrder_count.getTaskCount();
		}else{
			count=0;
		}
		
		return count;
	}
	
	/**
	 * 根据资源类型获取工单统计对象列表
	 * @param resourceType
	 * @return
	 */
	public List<WorkmanageCountWorkorderObject> getWorkmanageCountWorkorderObjectListByResourceType(String resourceType){
		List list=null;
		String hql="select o from WorkmanageCountWorkorderObject o where o.resourceType=?";
		list=this.hibernateTemplate.find(hql, resourceType);
		return list;
	}
	
	
	/**
	 * 派车
	 * @param woId 工单编号
	 * @param currentOperator 当前操作人
	 * @param participantList 下个任务环节待办人
	 * @return
	 */
	public boolean assignCarProcess(String woId,String currentOperator,List<String> participantList){
		boolean isSuccess=false;
		isSuccess=this.completeWordOrderProcessTask(woId, currentOperator, null, participantList);
		isSuccess=this.updateWorkOrderStatusProcess(woId, WorkManageConstant.WORKORDER_WAIT4USECAR);
		return isSuccess;
	}
	
	
	/**
	 * 用车
	 * @param woId
	 * @param currentOperator 当前操作人
	 * @param participantList 下个任务环节待办人
	 * @return
	 */
	public boolean useCarProcess(String woId,String currentOperator,List<String> participantList){
		boolean isSuccess=false;
		isSuccess=this.completeWordOrderProcessTask(woId, currentOperator, null, participantList);
		isSuccess=this.updateWorkOrderStatusProcess(woId, WorkManageConstant.WORKORDER_WAIT4RETURNCAR);
		return isSuccess;
	}
	
	
	/**
	 * 还车
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @return
	 */
	public boolean returnCarProcess(String woId,String currentOperator){
		boolean isSuccess=false;
		String taskId="";
		// 获取工单对象信息
		WorkmanageWorkorder wm_workOrder = this.getWorkOrderByWoId(woId);
		taskId=wm_workOrder.getCurrentTaskId();
		isSuccess=this.completeWordOrderProcessTask(woId,currentOperator,null,null);
		isSuccess=this.updateWorkOrderStatusProcess(woId, WorkManageConstant.WORKORDER_END);
		return isSuccess;
	}
	
	/**
	 * 撤销车辆调度单
	 * @param woId
	 * @param currentOperator
	 * @return
	 */
	public boolean cancelCarDispatchOrderProcess(String woId,String currentOperator){
		boolean isSuccess=false;
		//获取工单对象
		WorkmanageWorkorder workOrder=this.getWorkOrderByWoId(woId);
		String piId=workOrder.getProcessInstId();
		workOrder.setStatus(WorkManageConstant.WORKORDER_CANCELED);	//更改为‘已撤销’
		this.hibernateTemplate.update(workOrder);
		
		try {
			isSuccess=this.workFlowService.endWorkflowInstance(piId, currentOperator, "forceEnded");
		} catch (WFException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	
	/**
	 * 撤销巡检计划工单
	 * @param woId
	 * @param currentOperator
	 * @return
	 */
	public boolean cancelRoutineInspectionWorkOrderProcess(String woId,String currentOperator){
		boolean isSuccess=false;
		//获取工单对象
		WorkmanageWorkorder workOrder=this.getWorkOrderByWoId(woId);
		String piId=workOrder.getProcessInstId();
		workOrder.setStatus(WorkManageConstant.WORKORDER_DELETE);	//更改为删除状态
		this.hibernateTemplate.update(workOrder);
		
		boolean isEnd=this.judgeWorkOrderIsEndProcess(woId);
		try {
			isSuccess=true;
			if(!isEnd){
				isSuccess=this.workFlowService.endWorkflowInstance(piId, currentOperator, "forceEnded");
			}
		} catch (WFException e) {
			e.printStackTrace();
			logger.error("调用工作流接口，撤销巡检计划流程失败");
		}
		return isSuccess;
	}
	
	/**
	 * 保存工单与网络资源关联对象
	 * */
	public Serializable saveWorkmanageWorkorderResourceAssociate(WorkmanageWorkorderResourceAssociate workmanageWorkorderResourceAssociate){
		return hibernateTemplate.save(workmanageWorkorderResourceAssociate);
	}
	
	
	/**
	 * 根据参数删除工单与资源关联的对象
	 * @param params
	 * @return true:删除成功 false:删除失败
	 */
	public boolean deleteWorkmanageWorkorderResourceAssociate(String key, Object value){
		boolean isSuccess=false;
		List<WorkmanageWorkorderResourceAssociate> list=this.getWorkmanageWorkorderResourceAssociate(key, value);
		if(list!=null && !list.isEmpty()){
			WorkmanageWorkorderResourceAssociate obj=list.get(0);
			this.hibernateTemplate.delete(obj);
			isSuccess=true;
		}
		return isSuccess;
	}
	
	/**
	 * 获取网络资源关联表
	 * @param key 以key为索引查找服务跟踪记录
	 * @param value key对应的值
	 * @return
	 */
	private List<WorkmanageWorkorderResourceAssociate> getWorkmanageWorkorderResourceAssociate(final String key,final Object value){
		return this.hibernateTemplate
		.execute(new HibernateCallback<List<WorkmanageWorkorderResourceAssociate>>() {
			public List<WorkmanageWorkorderResourceAssociate> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session
						.createCriteria(WorkmanageWorkorderResourceAssociate.class);
				criteria.add(Restrictions.eq(key, value));
				List<WorkmanageWorkorderResourceAssociate> list = criteria.list();
				if (list != null && !list.isEmpty()) {
					return list;
				}
				return null;
			}
		});
	}
	
	
	
	/**
	 * 判断该工单是否结束
	 * @return
	 */
	public boolean judgeWorkOrderIsEndProcess(String woId){
		String piid=null;
		WorkmanageWorkorder workOrder=this.getWorkOrderByWoId(woId);
		if(workOrder!=null){
			piid=workOrder.getProcessInstId();
		}
		boolean isEnd=this.workFlowInfoService.judgeProcessIsEnd(piid);
		return isEnd;
	}

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}

}
