package com.iscreate.op.service.workmanage;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.cardispatch.CardispatchWorkorder;
import com.iscreate.op.pojo.publicinterface.Tasktracerecord;
import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.workmanage.WorkmanageBizprocessConf;
import com.iscreate.op.pojo.workmanage.WorkmanageCountWorkorderObject;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.service.workmanage.exception.WorkManageDefineException;


public interface WorkManageService {

	
	/**
	 * 创建工单
	 * @param bizProcessCode 业务流程Code
	 * @param workOrder 公共工单对象
	 * @param participantList 参与者列表
	 * @param workorderassnetresource 工单关联资源对象
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return 工单号
	 */
	public String createWorkOrder(String bizProcessCode,WorkmanageWorkorder workOrder,List<String> participantList,Workorderassnetresource workorderassnetresource,Tasktracerecord tasktracerecord);
	
	
	/**
	 * 受理工单
	 * @param woId 工单id
	 * @param currentHandler 当前处理人
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean acceptWorkOrder(String woId,String currentHandler,Tasktracerecord tasktracerecord);
	
	
	
	/**
	 * 结束完成工单
	 * @param woId
	 * @param currentHandler
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean finishWorkOrder(String woId,String currentHandler,Tasktracerecord tasktracerecord);
	
	
	/**
	 * 更改工单状态
	 * @param woId
	 * @param status
	 * @return
	 */
	public boolean updateWorkOrderStatus(String woId,int status);
	
	
	
	
	
	/**
	 * 派发任务单
	 * @param bizProcessCode 业务流程Code
	 * @param woId 工单id
	 * @param parentBizOrderId 父业务单号
	 * @param taskOrder 任务单对象
	 * @param participantList 参与者列表
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return 任务单号
	 */
	public String assignTaskOrder(String bizProcessCode,String woId,String parentBizOrderId,WorkmanageTaskorder taskOrder,List<String> participantList,Tasktracerecord tasktracerecord);
	
	
	
	/**
	 * 受理任务单
	 * @param taskOrder 任务单对象
	 * @param currentHandler 当前处理人
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean acceptTaskOrder(WorkmanageTaskorder taskOrder,String currentHandler,Tasktracerecord tasktracerecord);
	
	
	
	/**
	 * 结束完成任务单
	 * @param toId
	 * @param currenthandler
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean finishTaskOrder(String toId,String currentHandler,Tasktracerecord tasktracerecord);
	
	
	
	/**
	 * 更改任务单状态
	 * @param toId
	 * @param status
	 * @return
	 */
	public boolean updateTaskOrderStatus(String toId,int status);
	
	
	
	/**
	 * 根据工单id获取需要展示的工单信息
	 * @param woId
	 * @return
	 */
	public Map<String,String> getWorkOrderForShow(String woId);
	
	
	/**
	 * 根据工单id获取需要展示的车辆工单信息
	 * @param woId
	 * @return
	 */
	public Map<String,String> getCarDispatchWorkOrderForShow(String woId);
	
	
	/**
	 * 根据工单id获取需要展示的巡检计划工单信息
	 * @param woId
	 * @return
	 */
	public Map<String,String> getRoutineInsepctionWorkOrderForShow(String woId);
	
	
	
	/**
	 * 根据任务单id获取需要展示的任务单信息
	 * @param toId
	 * @return
	 */
	public Map<String,String> getTaskOrderForShow(String toId);
	
	
	/**
	 * 根据任务单id获取需要展示的巡检任务单信息
	 * @param toId
	 * @return
	 */
	public Map<String,String> getRoutineInsepctionTaskOrderForShow(String toId);
	
	
	/**
	 * 根据工单id获取工单对象
	 * @param woId
	 * @return
	 */
	public WorkmanageWorkorder getWorkOrderEntity(String woId);
	
	
	
	/**
	 * 根据任务单id获取任务单对象
	 * @param toId
	 * @return
	 */
	public WorkmanageTaskorder getTaskOrderEntity(String toId);
	
	
	
	
	/**
	 * 根据用户账号与工单号，判断当前用户是否是该工单的当前处理人
	 * @param woId
	 * @param currentHandler
	 * @return
	 */
	public boolean judgeWorkOrderCurrentHandler(String woId,String currentHandler);
	
	
	
	/**
	 * 根据用户账号与任务单号，判断当前用户是否是该任务单的当前处理人
	 * @param toId
	 * @param currentHandler
	 * @return
	 */
	public boolean judgeTaskOrderCurrentHandler(String toId,String currentHandler);
	
	
	
	/**
	 * 获取工单关联的任务单列表
	 * @param woId
	 * @return
	 */
	public List<Map> getTaskOrderListByWoId(String woId);
	
	
	/**
	 * 获取工单关联的任务单列表
	 * @param woId
	 * @return
	 */
	public List<Map> getRoutineInsepctionTaskOrderListByWoId(String woId);
	
	
	/**
	 * 转派工单
	 * @param woId 工单号
	 * @param updateWorkOrder 需要更新的工单对象属性
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean handoverWorkOrder(String woId,WorkmanageWorkorder updateWorkOrder,Tasktracerecord tasktracerecord);
	
	
	
	/**
	 * 转派任务单
	 * @param woId 工单号
	 * @param toId 任务单号
	 * @param updateWorkOrder 需要更新的工单对象属性
	 * @param updateTaskOrder 需要更新的任务单对象属性
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean handoverTaskOrder(String woId,String toId,WorkmanageWorkorder updateWorkOrder,WorkmanageTaskorder updateTaskOrder,Tasktracerecord tasktracerecord);
	
	
	/**
	 * 抢修阶段回复
	 * @param taskRecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean stepReply(Tasktracerecord taskRecord);
	
	
	
	/**
	 * 获取工单的子任务单列表
	 * @param woId
	 * @return
	 */
	public List<Map> getChildTaskOrderListByWoId(String woId);
	
	
	
	/**
	 * 获取任务单的子任务单列表
	 * @param toId
	 * @return
	 */
	public List<Map> getChildTaskOrderListByToId(String toId);
	
	
	
	/**
	 * 工单驳回
	 * @param woId 工单id
	 * @param tasktracerecord 服务过程记录
	 * @return
	 */
	public boolean rejectWorkOrder(String woId,Tasktracerecord tasktracerecord);
	
	
	/**
	 * 任务单驳回
	 * @param taskOrder 任务单对象
	 * @param tasktracerecord 服务过程记录
	 * @return
	 */
	public boolean rejectTaskOrder(WorkmanageTaskorder taskOrder,Tasktracerecord tasktracerecord);
	
	
	/**
	 * 任务单撤销
	 * @param taskOrder 任务单对象
	 * @param currentCancelPeople 当前撤销人（操作人）
	 * @param tasktracerecord 服务过程记录
	 * @return
	 * @throws WorkManageException
	 */
	public boolean cancelTaskOrder(WorkmanageTaskorder taskOrder,String currentCancelPeople,Tasktracerecord tasktracerecord) throws WorkManageException;
	
	
	/**
	 * 重新派发任务单（拒绝撤销）
	 * @param taskOrder 任务单对象
	 * @param currentHandler
	 * @param tasktracerecord 服务过程记录
	 * @return
	 */
	public boolean reAssignTaskOrder(WorkmanageTaskorder taskOrder,Tasktracerecord tasktracerecord) throws WorkManageException;
	
	
	/**
	 * 根据资源类型与资源id，获取工单统计数量
	 * @param resourceType 资源类型
	 * @param resourceId 资源id
	 * @return
	 */
	public long getWorkOrderCountByResourceTypeAndResourceId(String resourceType,String resourceId);
	
	
	
	/**
	 * 根据对象类型与对象id，获取任务单单统计数量
	 * @param objectType 对象类型
	 * @param objectId 对象id
	 * @return
	 */
	public long getTaskOrderCountByObjectTypeAndObjectId(String objectType,String objectId);
	
	
	
	
	/**
	 * 获取用户的工单列表
	 * @param userId
	 * @param bizTypeCode 业务类型code：如urgentrepair（抢修）、cardispatch（车辆调度）、resourcedispatch（物资调度）
	 * @param workOrderType 工单类型：如pendingWorkOrder（待办工单）、如trackWorkOrder（跟踪工单）、superviseWorkOrder（监督工单）
	 * @param pageIndex 记录开始下标
	 * @param pageSize 记录每页显示数量
	 * @param conditionString sql查询条件
	 * @return
	 */
	public Map<String,Object> getUserWorkOrders(String userId,String bizTypeCode,String workOrderType,Integer pageIndex,Integer pageSize,String conditionString);
	
	
	
	/**
	 * 获取用户的任务单列表
	 * @param userId
	 * @param bizTypeCode  业务类型code：如urgentrepair（抢修）、cardispatch（车辆调度）、resourcedispatch（物资调度）、all(全部)
	 * @param taskOrderType 任务单类型：如pendingTaskOrder（待办任务单）、如trackTaskOrder（跟踪任务单）、superviseTaskOrder（监督任务单）
	 * @param pageIndex 记录开始下标
	 * @param pageSize 记录每页显示数量
	 * @param conditionString sql查询条件
	 * @return
	 */
	public Map<String,Object> getUserTaskOrders(String userId,String bizTypeCode,String taskOrderType,Integer pageIndex,Integer pageSize,String conditionString);
	
	
	
	
	/**
	 * 获取用户的任务单列表（for人员信息模块）
	 * @param userId
	 * @param bizTypeCode  业务类型code：如urgentrepair（抢修）、cardispatch（车辆调度）、resourcedispatch（物资调度）、all(全部)
	 * @param pageIndex 记录开始下标
	 * @param pageSize 记录每页显示数量
	 * @param conditionString sql查询条件
	 * @return
	 */
	public Map<String,Object> getUserTaskOrdersForStaffInfo(String userId,String bizTypeCode,Integer pageIndex,Integer pageSize,String conditionString);
	
	
	
	/**
	 * 根据资源类型、资源id获取该资源的工单单列表
	 * @param resourceType 资源类型
	 * @param resourceId 资源id
	 * @param bizTypeCode  业务类型code：如urgentrepair（抢修）、cardispatch（车辆调度）、resourcedispatch（物资调度）
	 * @param workOrderType 工单类型：如pendingWorkOrder（待办工单）、如trackWorkOrder（跟踪工单）、superviseWorkOrder（监督工单）
	 * @param conditionString sql查询条件
	 * @return
	 */
	public List<Map> getWorkOrderListByResourceTypeAndResourceId(String resourceType,String resourceId,String bizTypeCode,String workOrderType,String conditionString);
	
	
	
	
	/**
	 * 根据资源类型、资源id获取该资源的任务单列表
	 * @param resourceType 资源类型
	 * @param resourceId 资源id
	 * @param bizTypeCode  业务类型code：如urgentrepair（抢修）、cardispatch（车辆调度）、resourcedispatch（物资调度）
	 * @param taskOrderType 任务单类型：如pendingTaskOrder（待办任务单）、如trackTaskOrder（跟踪任务单）、superviseTaskOrder（监督任务单）
	 * @param conditionString sql查询条件
	 * @return
	 */
	public List<Map> getTaskOrderListByResourceTypeAndResourceId(String resourceType,String resourceId,String bizTypeCode,String taskOrderType,String conditionString);
	
	
	
//	/**
//	 * 根据资源id，获取资源关联的工单或任务单列表
//	 * @param resourceId 资源id
//	 * @param bizOrderCode 业务单类型code：如workOrder（工单）、taskOrder（任务单）
//	 * @return
//	 */
//	public List<Map> getWorkOrderOrTaskOrderByResourceId(String resourceId,String bizOrderCode);
	
	
	
	/**
	 * 自定义查询工单/任务单信息
	 * @param bizOrderCode 业务单类型code：如workOrder（工单）、taskOrder（任务单）
	 * @param conditionString 查询条件
	 * @return
	 */
	public List<Map> commonQuery(String bizOrderCode,String conditionString);
	
	
	/**
	 * 创建车辆调度单
	 * @param bizProcessCode 业务流程Code
	 * @param workOrder 公共工单对象
	 * @param participantList 参与者列表
	 * @param cardispatchWorkorder 个性车辆调度单对象
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return 工单号
	 */
	public String createCarDispatchWorkOrder(String bizProcessCode,WorkmanageWorkorder workOrder,List<String> participantList,CardispatchWorkorder cardispatchWorkorder,Tasktracerecord tasktracerecord);
	
	
	/**
	 * 派车操作
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @param participantList 下个任务环节待办人
	 * @param tasktracerecord 服务过程跟踪记录
	 * @param resource_carId 车辆资源Id
	 * @return
	 */
	public boolean assignCar(String woId,String currentOperator,List<String> participantList,Tasktracerecord tasktracerecord,String resource_carId);
	
	
	
	/**
	 * 派车操作
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @param participantList 下个任务环节待办人
	 * @param tasktracerecord 服务过程跟踪记录
	 * @param resource_carId 车辆资源Id
	 * @return
	 */
	public boolean assignCar(String woId,String currentOperator,List<String> participantList,Tasktracerecord tasktracerecord,String resource_carId,CardispatchWorkorder cardispatchWorkorder);
	
	
	/**
	 * 用车操作
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @param participantList 下个任务环节待办人
	 * @param tasktracerecord 服务过程跟踪记录
	 * @return
	 */
	public boolean useCar(String woId,String currentOperator,List<String> participantList,Tasktracerecord tasktracerecord);
	
	
	/**
	 * 还车操作
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @param tasktracerecord 服务过程跟踪记录
	 * @param resource_carId 车辆资源Id
	 * @return
	 */
	public boolean returnCar(String woId,String currentOperator,Tasktracerecord tasktracerecord,String resource_carId);
	
	
	/**
	 * 撤销车辆调度单
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @param tasktracerecord 服务过程跟踪记录
	 * @return
	 */
	public boolean cancelCarDispatchOrder(String woId,String currentOperator,Tasktracerecord tasktracerecord);
	
	
	
	/**
	 * 根据工单id，获取工单对象
	 * @param woId
	 */
	public WorkmanageWorkorder getWorkOrderByWoId(String woId);
	
	
	/**
	 * 更新公共工单对象信息
	 * @param workmanageWorkorder
	 */
	public void updateWorkmanageWorkorder(WorkmanageWorkorder workmanageWorkorder);
	
	
	
	/**
	 * 根据任务单id，获取任务单对象
	 * @param toId
	 * @return
	 */
	public WorkmanageTaskorder getTaskOrderByToId(String toId);
	
	
	
	/**
	 * 更新任务单对象信息
	 * @param workmanageTaskorder
	 */
	public void updateWorkmanageTaskOrder(WorkmanageTaskorder workmanageTaskorder);
	
	
	
	/**
	 * 根据资源类型获取工单统计对象列表
	 * @param resourceType
	 * @return
	 */
	public List<WorkmanageCountWorkorderObject> getWorkmanageCountWorkorderObjectListByResourceType(String resourceType);
	
	
	
	
	/**
	 * 判断任务单是否是群组任务
	 * @param toId
	 * @return
	 */
	public boolean judgeTaskIsGroup(String toId);
	
	/**
	 * 制定巡检计划工单
	 * @param bizProcessCode
	 * @param workOrder
	 * @param participantList
	 * @return
	 */
	public boolean createRoutineInspectionPlanWorkOrder(String bizProcessCode,WorkmanageWorkorder workmanageWorkorder) throws WorkManageDefineException;
	
	
	/**
	 * 关闭巡检计划工单
	 * @param woId
	 * @param currentHandler
	 * @return
	 */
	public boolean finishRoutineInspectionPlanWorkOrder(String woId,String currentHandler) throws WorkManageDefineException;
	
	
//	/**
//	 * 创建巡检任务单
//	 * @param bizProcessCode 业务流程Code
//	 * @param woId 工单id
//	 * @param parentBizOrderId 父业务单号
//	 * @param taskOrder 任务单对象
//	 * @param participantList 参与者列表
//	 * @return 任务单号
//	 */
//	public String createRoutineInspectionTaskOrder(String bizProcessCode,String woId,String parentBizOrderId,WorkmanageTaskorder taskOrder,List<String> participantList) throws WorkManageDefineException;
	
	
	/**
	 * 创建巡检任务单
	 * @param bizProcessCode 业务流程Code
	 * @param woId 工单id
	 * @param parentBizOrderId 父业务单号
	 * @param workmanageTaskorder 任务单对象
	 * @param personalOrGroup 个人or群组任务标识
	 * @param taskAcceptorId 任务接收者
	 * @return
	 * @throws WorkManageDefineException
	 */
	public String createRoutineInspectionTaskOrder(String bizProcessCode,String woId,String parentBizOrderId,WorkmanageTaskorder workmanageTaskorder,String personalOrGroup,String taskAcceptorId) throws WorkManageDefineException;
	
	
	/**
	 * 关闭巡检任务单
	 * @param toId 任务单号
	 * @param currentHandler 当前处理人
	 * @return
	 */
	public boolean finishRoutineInspectionTaskOrder(String toId,String currentHandler) throws WorkManageDefineException;
	
	/**
	 * 根据业务流程Code获取对应的业务流程配置对象
	 * @param bizProcessCode 业务流程Code
	 * @return
	 */
	public WorkmanageBizprocessConf getBizProcessConfByProcessCode(String bizProcessCode);
	
	
	
	/**
	 * 获取任务
	 * @param taskId 任务id
	 * @param currentOperator 当前操作人
	 * @return
	 */
	public boolean takeTask(String bizOrderId,String currentOperator);
	
	
	
	
	/**
	 * 撤销巡检计划工单
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @return
	 */
	public boolean cancelRoutineInspectionWorkOrder(String woId,String currentOperator);
	
	
	/**
	 * 撤销巡检任务单
	 * @param toId 任务单id
	 * @param currentCancelPeople 当前撤销人（操作人）
	 * @return
	 * @throws WorkManageException
	 */
	public boolean cancelRoutineInspectionTaskOrder(String toId,String currentCancelPeople) throws WorkManageException;
	
	
	/**
	 * 根据工单id，获取关联的巡检已经关闭的任务单数目
	 * @param woId
	 * @return
	 */
	public int getCountOfRoutineInspectionCloseTaskOrderByWoId(String woId);
	
	
	/**
	 * 根据工单id和状态，获取关联的巡检任务单数目
	 * @param woId
	 * @param status
	 * @return
	 */
	public int getCountOfRoutineInspectionTaskOrderByWoIdAndStatus(String woId,int status);
	
	
	
	/**
	 * 根据工单id，获取关联的巡检任务单数目
	 * @param woId
	 * @return
	 */
	public int getCountOfRoutineInspectionTaskOrderByWoId(String woId);
	
	
	
	/**
	 * 判断该任务单是否结束
	 * @return
	 */
	public boolean judgeTaskOrderIsEnd(String toId);
	
	
	/**
	 * 判断该工单是否结束
	 * @param woId
	 * @return
	 */
	public boolean judgeWorkOrderIsEnd(String woId);
	 
	
	
//	######  for mobile ######################
	
	
	/**
	 * 根据资源类型、资源id，用户id，获取任务单列表
	 * @param resType
	 * @param resourceId
	 * @param userId
	 * @param taskOrderType 任务单类型 pendingTaskOrder（待办任务单）、trackTaskOrder（跟踪任务单）、superviseTaskOrder（监督任务单）
	 * @return
	 */
	public List<Map> getTaskOrderListByResourceAndUserId(String resType,String resourceId,String userId,String taskOrderType);
	
	
	/**
	 * 根据工单列表，获取关联的巡检已经关闭的任务单数目
	 * @param woId
	 * @return
	 */
	public List<Map> getCountOfRoutineInspectionCloseTaskOrderByWoIdList(List<String> woIds);
	
	/**
	 * 根据工单列表，获取关联的巡检任务单数目
	 * @param woId
	 * @return
	 */
	public List<Map> getCountOfRoutineInspectionTaskOrderByWoIdList(List<String> woIds);
	
}
