package com.iscreate.op.service.workmanage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.workmanage.WorkmanageCountWorkorderObject;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorderResourceAssociate;


public interface WorkOrderHandleService {

	
	/**
	 * 根据工单id，获取工单对象
	 * @param woId
	 */
	public WorkmanageWorkorder getWorkOrderByWoId(String woId);
	
	
	/**
	 * 更新公共工单对象
	 * @param workmanageWorkorder
	 */
	public void updateWorkOrder(WorkmanageWorkorder workmanageWorkorder);
	
	
	/**
	 * 根据工单id获取用于显示的工单信息
	 * @param woId
	 * @return
	 */
	public Map getWorkOrderByWoIdForShowProcess(String woId);
	
	
	
	/**
	 * 根据工单id获取用于显示的工单信息by Hibernate
	 * @param woId
	 * @return
	 */
	public Map getWorkOrderByWoIFowShowByHibernate(String woId);
	
	
	
	/**
	 * 根据工单id获取用于显示的车辆工单信息
	 * @param woId
	 * @return
	 */
	public Map getCarDispatchWorkOrderByWoIdForShowProcess(String woId);
	
	
	
	/**
	 * 根据工单id获取用于显示的车辆工单信息
	 * @param woId
	 * @return
	 */
	public Map getRoutineInspectionWorkOrderByWoIdForShowProcess(String woId);
	
	
	
	
	/**
	 * 删除工单对象
	 * @param workOrder
	 */
	public void deleteWorkOrder(WorkmanageWorkorder workOrder);
	
	
	/**
	 * 创建工单流程
	 * @param processDefineId 流程定义id
	 * @param workOrder 公共工单对象
	 * @param participantList 参与者列表
	 * @return
	 */
	public String createWordOrderProcess(String processDefineId,WorkmanageWorkorder workOrder,List<String> participantList);
	
//	/**
//	 * 初始化公共工单表单信息
//	 * @param processDefineId 流程定义id
//	 * @param processInstId 流程实例id
//	 * @param participants 参与者列表
//	 * @param variables 流程变量
//	 * @return
//	 */
//	public boolean initCommonWorkOrder(String processDefineId,String processInstId,List<String> participants,Map<String,Object> variables); 
	
	
	/**
	 * 受理工单
	 * @param woId 工单id
	 * @param acceptPeople 受理人
	 */
	public boolean acceptWorkOrder(String woId,String acceptPeople);
	
	
	/**
	 * 结束工单流程任务
	 * @param woId 工单id
	 * @param currentHandler 当前处理人
	 * @param outcome 流向名称
	 * @param participantList 参与者列表
	 * @return
	 */
	public boolean completeWordOrderProcessTask(String woId,String currentHandler,String outcome,List<String> participantList);
	
	
	/**
	 * 更改工单状态
	 * @param woId
	 * @param status
	 * @return
	 */
	public boolean updateWorkOrderStatusProcess(String woId,int status);
	
	
	/**
	 * 更改工单ReadStatus状态
	 * @param woId
	 * @param readStatus
	 * @return
	 */
	public boolean updateWorkOrderReadStatusProcess(String woId,int readStatus);
	
	
	
	/**
	 * 根据用户账号与工单号，判断当前用户是否是该工单的当前处理人
	 * @param woId
	 * @param currentUser
	 * @return
	 */
	public boolean judgeWorkOrderCurrentHandlerProcess(String woId,String currentUser);
	
	
	
	/**
	 * 转派工单
	 * @param woId 工单id
	 * @param updateWorkOrder 需要更新的工单对象属性
	 * @return
	 */
	public boolean handoverWorkOrderProcess(String woId,WorkmanageWorkorder updateWorkOrder);
	
	
	/**
	 * 统计网络设施或人的工单数量（增加）
	 * @param countWorkorder
	 * @return
	 */
	public boolean countWorkOrderNumberForSave(WorkmanageCountWorkorderObject countWorkorder);
	
	
	/**
	 * 统计网络设施或人的工单数量（减去）
	 * @param countWorkorder
	 * @return
	 */
	public boolean countWorkOrderNumberForSubtract(WorkmanageCountWorkorderObject countWorkorder);
	
	
	
	/**
	 * 工单驳回
	 * @param woId 工单id
	 * @return
	 */
	public boolean rejectWorkOrderProcess(String woId);
	
	
	/**
	 * 根据资源类型与资源id，获取工单统计数量
	 * @param resourceType 资源类型
	 * @param resourceId 资源id
	 * @return
	 */
	public long getWorkOrderCountByResourceTypeAndResourceId(String resourceType,String resourceId);
	
	
	/**
	 * 根据资源类型获取工单统计对象列表
	 * @param resourceType
	 * @return
	 */
	public List<WorkmanageCountWorkorderObject> getWorkmanageCountWorkorderObjectListByResourceType(String resourceType);
	
	
	/**
	 * 派车
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @param participantList 下个任务环节待办人
	 * @return
	 */
	public boolean assignCarProcess(String woId,String currentOperator,List<String> participantList);
	
	
	/**
	 * 用车
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @param participantList 下个任务环节待办人
	 * @return
	 */
	public boolean useCarProcess(String woId,String currentOperator,List<String> participantList);
	
	
	/**
	 * 还车
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @return
	 */
	public boolean returnCarProcess(String woId,String currentOperator);
	
	
	/**
	 * 撤销车辆调度单
	 * @param woId
	 * @param currentOperator
	 * @return
	 */
	public boolean cancelCarDispatchOrderProcess(String woId,String currentOperator);
	
	
	
	/**
	 * 撤销巡检计划工单
	 * @param woId
	 * @param currentOperator
	 * @return
	 */
	public boolean cancelRoutineInspectionWorkOrderProcess(String woId,String currentOperator);
	
	
	
	/**
	 * 保存工单与网络资源关联对象
	 * */
	public Serializable saveWorkmanageWorkorderResourceAssociate(WorkmanageWorkorderResourceAssociate workmanageWorkorderResourceAssociate);
	
	
	/**
	 * 根据参数删除工单与资源关联的对象
	 * @param params
	 * @return true:删除成功 false:删除失败
	 */
	public boolean deleteWorkmanageWorkorderResourceAssociate(String key, Object value);
	
	
	/**
	 * 判断该工单是否结束
	 * @return
	 */
	public boolean judgeWorkOrderIsEndProcess(String woId);
	

}
