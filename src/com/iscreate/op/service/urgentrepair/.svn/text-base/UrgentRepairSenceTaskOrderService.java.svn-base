package com.iscreate.op.service.urgentrepair;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.urgentrepair.UrgentrepairSencetaskorder;
import com.iscreate.op.pojo.urgentrepair.UrgentrepairTechsupporttaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;

public interface UrgentRepairSenceTaskOrderService {
	/**
	 * 受理现场任务
	 * @param accept
	 * @param toId
	 */
	public boolean txAcceptUrgentRepairSenceTaskOrder(String accept,String woId,String toId ,WorkmanageTaskorder workmanageTaskorder);
	
	/**
	 * 现场任务阶段回复
	 * @param actor
	 * @param woId
	 * @param toId
	 * @param stepReply_desc
	 * @param stepReply_processingProgress
	 * @param stepReply_faultType
	 * @param stepReply_faultResult
	 * @return
	 */
	public String txStepReplyUrgentRepairSenceTaskOrder(String actor,String woId,String toId,String stepReply_desc,String stepReply_processingProgress,String stepReply_faultType,String stepReply_faultResult,String customerType);
	
	/**
	 * 驳回现场任务
	 * 
	 * @param actor
	 * @param toId
	 */
	public boolean txRejectUrgentRepairSenceTaskOrder(String actor,String woId,String toId,
			WorkmanageTaskorder workmanageTaskorder);
	
	/**
	 *  转派现场任务
	 * @param actor
	 * @param woId
	 * @param toId
	 * @param recipient
	 * @param stepReply_desc
	 * @return
	 */
	public boolean txToSendUrgentRepairSenceTaskOrder(String actor,String woId,String toId,String recipient,
			String stepReply_desc);
	
	/**
	 * 最终回复现场任务
	 * @param reply
	 * @param toId
	 * @param urgentrepairSencetaskorder
	 */
	public boolean txFinalReplyUrgentRepairSenceTaskOrder(String reply,String woId,String toId,UrgentrepairSencetaskorder urgentrepairSencetaskorder);
	
	/**
	 * 重派任务
	 * @param actor
	 * @param toId
	 */
	public boolean txReAssignlUrgentRepairTechSupportTaskOrder(String actor,String woId,String toId,String subToId,
			WorkmanageTaskorder workmanageTaskorder);
	
	/**
	 * 撤销任务
	 * 
	 * @param actor
	 * @param toId
	 */
	public boolean txCancelUrgentRepairTechSupportTaskOrder(String actor,String woId,String toId,String subToId,
			WorkmanageTaskorder workmanageTaskorder);
	
	/**
	 * 催办任务
	 * @param actor
	 * @param toId
	 */
	public boolean txHastenUrgentRepairTechSupportTaskOrder(String actor,String woId,String toId,String subToId,
			WorkmanageTaskorder workmanageTaskorder);
	
	/**
	 * 派发专家任务
	 * @param sendor
	 * @param recipient
	 * @param woId
	 * @param toId
	 * @param urgentrepairTechsupporttaskorder
	 */
	public void txCreateUrgentRepairTechSupportTaskOrder(String sendor,String recipient,String woId,String senceToId,UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder,WorkmanageTaskorder workmanageTaskorder);
		
	
	
	/**
	 * 获取专家
	 * @param account
	 * @return
	 */
	public List<Map> loadSpecialistInfoService(String account);
	
	/**
	 * 获取维护人员
	 */
	public List<Map>  loadTeamersInfoService(String providerOrgId,String search_value);
	
	/**
	 * 获取现场任务单
	 *
	 * @param toId
	 */
	public UrgentrepairSencetaskorder getUrgentRepairSenceTaskOrder(String toId);
}
