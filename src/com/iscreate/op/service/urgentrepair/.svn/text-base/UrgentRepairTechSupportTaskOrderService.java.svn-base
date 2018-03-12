package com.iscreate.op.service.urgentrepair;

import java.util.Map;
import java.util.Set;

import com.iscreate.op.pojo.urgentrepair.UrgentrepairTechsupporttaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;

public interface UrgentRepairTechSupportTaskOrderService {
	/**
	 * 受理专家任务
	 * @param accept
	 * @param toId
	 * @param urgentrepairTechsupporttaskorder
	 */
	public boolean txAcceptUrgentRepairTechSupportTaskOrder(String accept,String woId,String toId,WorkmanageTaskorder workmanageTaskorder);
	
	/**
	 * 专家任务阶回复
	 * @param actor
	 * @param woId
	 * @param toId
	 * @param stepReply_desc
	 * @param stepReply_processingProgress
	 * @param stepReply_faultType
	 * @param stepReply_faultResult
	 * @return
	 */
	public String txStepReplyUrgentRepairTechSupportTaskOrder(String actor,String woId,String toId,String stepReply_desc,String stepReply_processingProgress,String stepReply_faultType,String stepReply_faultResult,String customerWoType);
	
	/**
	 * 驳回专家任务
	 * @param actor
	 * @param toId
	 */
	public boolean txRejectUrgentRepairTechSupportTaskOrder(String actor,String woId,String toId,WorkmanageTaskorder workmanageTaskorder);
	
	/**
	 *  转派专家任务
	 * @param actor
	 * @param woId
	 * @param toId
	 * @param recipient
	 * @param stepReply_desc
	 * @return
	 */
	public boolean txToSendUrgentRepairTechSupportTaskOrder(String actor,String woId,String toId,String recipient,
			String stepReply_desc);
	/**
	 * 最终回复专家任务
	 * @param reply
	 * @param toId
	 * @param urgentrepairTechsupporttaskorder
	 */
	public boolean txReplyUrgentRepairTechSupportTaskOrder(String reply,String woId,String toId,UrgentrepairTechsupporttaskorder urgentrepairTechsupporttaskorder);
	
	/**
	 * 获取专家任务单
	 *
	 * @param toId
	 */
	public UrgentrepairTechsupporttaskorder getUrgentRepairTechSupportTaskOrder(String toId);
	
	/**
	 * 获取专家
	 */
	public Set<Map>  loadToSendSpecialistInfoService(String providerOrgId,String search_value);
}
