package com.iscreate.op.service.workmanage;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.workmanage.WorkmanageCountTaskorderObject;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;


public interface TaskOrderHandleService {

	
	/**
	 * 根任务单id，获取任务单对象
	 * @param toId
	 */
	public WorkmanageTaskorder getTaskOrderByToId(String toId); 
	
	
	/**
	 * 根据任务单id获取用于显示的任务单信息
	 * @param toId
	 * @return
	 */
	public Map<String,String> getTaskOrderByToIdForShowProcess(String toId);
	
	
	/**
	 * 根据任务单id获取用于显示的巡检任务单信息
	 * @param toId
	 * @return
	 */
	public Map<String,String> getRoutineInspectionTaskOrderByToIdForShowProcess(String toId);
	
	
	
	
	
	/**
	 * 根据任务单id获取用于任务单InHibernate
	 * @param toId
	 * @return
	 */
	public Map<String,String> getTaskOrderByToIdInHibernate(String toId);
	
	
	
	/**
	 * 更新任务单对象
	 * @param workmanageTaskorder
	 */
	public void updateTaskOrder(WorkmanageTaskorder workmanageTaskorder);
	
	
	
	/**
	 * 创建任务单流程
	 * @param bizProcessCode 业务流程code
	 * @param processDefineId 流程定义id
	 * @param workOrder 任务单对象
	 * @param participantList 参与者列表
	 * @return
	 */
	public String createTaskOrderProcess(String bizProcessCode,String processDefineId,
			WorkmanageTaskorder taskOrder, List<String> participantList);
	
	
	
	
	/**
	 * 创建任务单流程（带群组）
	 * @param bizProcessCode
	 * @param processDefineId
	 * @param taskOrder
	 * @param personalOrGroup 个人任务还是群组任务
	 * @param taskAcceptorId 任务接收者id
	 * @return
	 */
	public String createTaskOrderProcess(String bizProcessCode,String processDefineId,
			WorkmanageTaskorder taskOrder, String personalOrGroup,String taskAcceptorId);
	
	
	
	/**
	 * 受理任务单
	 * @param taskOrder 任务单对象
	 * @param acceptPeople
	 * @return
	 */
	public boolean acceptTaskOrder(WorkmanageTaskorder taskOrder,String acceptPeople);
	
	
	
	/**
	 * 结束任务单流程任务
	 * @param toId 任务单id
	 * @param currentHandler 当前处理人
	 * @param outcome 流向名称
	 * @param participantList 参与者列表
	 * @return
	 */
	public boolean completeTaskOrderProcessTask(String toId, String currentHandler,String outcome, List<String> participantList);
	
	
	
	
	
	public boolean completeRoutineInspectionProcessTask(String toId,String currentOperator);
	
	/**
	 * 更改任务单状态
	 * @param toId
	 * @param status
	 * @return
	 */
	public boolean updateTaskOrderStatusProcess(String toId,int status);
	
	
	/**
	 * 更改任务单ReadStatus状态
	 * @param toId
	 * @param readStatus
	 * @return
	 */
	public boolean updateTaskOrderReadStatusProcess(String toId,int readStatus);
	
	
	
	/**
	 * 根据用户账号与任务单号，判断当前用户是否是该任务单的当前处理人
	 * @param toId
	 * @param currentUser
	 * @return
	 */
	public boolean judgeTaskOrderCurrentHandler(String toId,String currentUser);
	
	
	/**
	 * 获取工单关联的任务单列表
	 * @param woId
	 * @return
	 */
	public List<Map> getTaskOrderListByWoIdProcess(String woId);
	
	
	
	/**
	 * 获取工单关联的巡检任务单列表
	 * @param woId
	 * @return
	 */
	public List<Map> getRoutineInspectionTaskOrderListByWoIdProcess(String woId);
	
	
	
	
	/**
	 * 根据工单id，获取关联的已经关闭的任务单数目
	 * @param woId
	 * @return
	 */
	public int getCountOfRoutineInspectionCloseTaskOrderByWoId(String woId);
	
	
	
	/**
	 * 根据工单id和状态，获取关联的巡检任务单数目
	 * @param woId
	 * @return
	 */
	public int getCountOfRoutineInspectionTaskOrderByWoIdAndStatus(String woId,int status);
	
	
	
	/**
	 * 根据工单id，获取关联的任务单数目
	 * @param woId
	 * @return
	 */
	public int getCountOfRoutineInspectionTaskOrderByWoId(String woId);
	
	
	
	
	/**
	 * 转派任务单
	 * @param pk_workOrder 工单标识对象
	 * @param toId 任务单号
	 * @param updateWorkOrder 需要更新的工单单对象属性
	 * @param updateTaskOrder 需要更新的任务单单对象属性
	 * @return
	 */
	public boolean handoverTaskOrderProcess(WorkmanageWorkorder pk_workOrder,String toId,WorkmanageWorkorder updateWorkOrder,WorkmanageTaskorder updateTaskOrder);
	
	
	
	/**
	 * 统计人或车的任务单数量（增加）
	 * @param countWorkorder
	 * @return
	 */
	public boolean countTaskOrderNumberForSave(WorkmanageCountTaskorderObject countTaskOrder);
	
	
	/**
	 * 统计人或车的任务单数量（减去）
	 * @param countWorkorder
	 * @return
	 */
	public boolean countTaskOrderNumberForSubtract(WorkmanageCountTaskorderObject countTaskOrder);
	
	
	
	/**
	 * 获取工单的子任务单列表
	 * @param woId
	 * @return
	 */
	public List<Map> getChildTaskOrderListByWoIdProcess(String woId);
	
	
	/**
	 * 获取任务单的子任务单列表
	 * @param toId
	 * @return
	 */
	public List<Map> getChildTaskOrderListByToIdProcess(String toId);
	
	
	
	/**
	 * 任务单驳回
	 * @param taskOrder 任务单对象
	 * @return
	 */
	public boolean rejectTaskOrderProcess(WorkmanageTaskorder taskOrder);
	

	
	/**
	 * 撤销任务单
	 * @param taskOrder 任务单对象
	 * @param currentCancelPeople
	 * @return
	 */
	public boolean cancelTaskOrderProcess(WorkmanageTaskorder taskOrder,String currentCancelPeople) throws WorkManageException;
	
	
	
	/**
	 * 撤销巡检任务单
	 * @param toId 任务单id
	 * @param currentCancelPeople
	 * @return
	 */
	public boolean cancelRoutineInspectionTaskOrderProcess(String toId,String currentCancelPeople);
	
	
	
	/**
	 * 重新派发任务单（拒绝撤销）
	 * @param taskOrder 任务单对象
	 * @return
	 */
	public boolean reAssignTaskOrderProcess(WorkmanageTaskorder taskOrder) throws WorkManageException;
	
	
	
	/**
	 * 根据对象类型与对象id，获取任务单单统计数量
	 * @param objectType 对象类型
	 * @param objectId 对象id
	 * @return
	 */
	public long getTaskOrderCountByObjectTypeAndObjectId(String objectType,String objectId);
	
	
	/**
	 * 根据业务单号，判断该单是否是任务单
	 * @param bizOrderId
	 * @return
	 */
	public boolean judgeIsTaskOrder(String bizOrderId);
	
	
	
	/**
	 * 判断任务单是否是群组任务
	 * @param toId
	 * @return
	 */
	public boolean judgeTaskIsGroup(String toId);
	
	
	
	
	/**
	 * 判断该任务单是否结束
	 * @return
	 */
	public boolean judgeTaskOrderIsEndProcess(String toId);
	
	
	
	
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
			String conditionValue,Map<String,String> sortCondition,String longitude,String latitude);
	
	
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
	public List<Map> getUserRoutineInspectionTaskOrderWithPageForMobile(String userId, int pageIndex, int pageSize ,String taskOrderType,String conditionValue,Map<String,String> sortCondition,String longitude,String latitude);
	
	
	/**
	 * 根据工单列表，获取关联的已经关闭的任务单数目
	 * @param woId
	 * @return
	 */
	public List<Map> getCountOfRoutineInspectionCloseTaskOrderByWoIdList(List<String> params);
	
	/**
	 * 根据工单列表，获取任务单数目
	 * @param woId
	 * @return
	 */
	public List<Map> getCountOfRoutineInspectionTaskOrderByWoIdList(List<String> params);
	
}

