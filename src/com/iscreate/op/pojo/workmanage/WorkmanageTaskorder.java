package com.iscreate.op.pojo.workmanage;

import java.util.Date;


/**
 * WorkmanageTaskorder entity. @author MyEclipse Persistence Tools
 */

public class WorkmanageTaskorder  implements java.io.Serializable {


    // Fields    

     private Long id;
     private String processDefineId;
     private String processInstId;
     private String toId;
     private String woId;
     private String parentBizOrderId;
     private Integer status;
     private String currentTaskId;
     private String currentTaskName;
     private String toTitle;
     private String toDesc;
     private String toType;
     private String assigner;
     private String assignerName;
     private Long assignerOrgId;
     private String assignComment;
     private Date assignTime;
     private Date requireCompleteTime;
     private String lastReplyPeople;
     private String lastReplyPeopleName;
     private Date finalCompleteTime;
     private String acceptPeople;
     private String acceptPeopleName;
     private Date acceptTime;
     private String acceptComment;
     private String currentHandler;
     private String currentHandlerName;
     private Long currentHandlerOrgId;
     private Date rejectTime;
     private String rejectComment;
     private Date cancelTime;
     private String cancelComment;
     private Date reassignTime;
     private String reassignComment;
     private Integer isOverTime;
     private Integer isRead;
     private Integer isSendWillOverTime;
     private Integer isSendOverTime;
     
     private String urgeWorkComment;	//催办意见
     private String orderOwner;
     private Long orderOwnerOrgId;

    // Constructors

    /** default constructor */
    public WorkmanageTaskorder() {
    }

    
    /** full constructor */
    public WorkmanageTaskorder(String processDefineId, String processInstId, String toId, String woId, String parentBizOrderId, Integer status, String currentTaskId, String currentTaskName, String toTitle, String toDesc, String toType, String assigner, String assignerName, Long assignerOrgId, String assignComment, Date assignTime, Date requireCompleteTime, String lastReplyPeople, String lastReplyPeopleName, Date finalCompleteTime, String acceptPeople, String acceptPeopleName, Date acceptTime, String acceptComment, String currentHandler, String currentHandlerName, Long currentHandlerOrgId, Date rejectTime, String rejectComment, Date cancelTime, String cancelComment, Date reassignTime, String reassignComment, Integer isOverTime, Integer isRead) {
        this.processDefineId = processDefineId;
        this.processInstId = processInstId;
        this.toId = toId;
        this.woId = woId;
        this.parentBizOrderId = parentBizOrderId;
        this.status = status;
        this.currentTaskId = currentTaskId;
        this.currentTaskName = currentTaskName;
        this.toTitle = toTitle;
        this.toDesc = toDesc;
        this.toType = toType;
        this.assigner = assigner;
        this.assignerName = assignerName;
        this.assignerOrgId = assignerOrgId;
        this.assignComment = assignComment;
        this.assignTime = assignTime;
        this.requireCompleteTime = requireCompleteTime;
        this.lastReplyPeople = lastReplyPeople;
        this.lastReplyPeopleName = lastReplyPeopleName;
        this.finalCompleteTime = finalCompleteTime;
        this.acceptPeople = acceptPeople;
        this.acceptPeopleName = acceptPeopleName;
        this.acceptTime = acceptTime;
        this.acceptComment = acceptComment;
        this.currentHandler = currentHandler;
        this.currentHandlerName = currentHandlerName;
        this.currentHandlerOrgId = currentHandlerOrgId;
        this.rejectTime = rejectTime;
        this.rejectComment = rejectComment;
        this.cancelTime = cancelTime;
        this.cancelComment = cancelComment;
        this.reassignTime = reassignTime;
        this.reassignComment = reassignComment;
        this.isOverTime = isOverTime;
        this.isRead = isRead;
    }

   
    // Property accessors

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessDefineId() {
        return this.processDefineId;
    }
    
    public void setProcessDefineId(String processDefineId) {
        this.processDefineId = processDefineId;
    }

    public String getProcessInstId() {
        return this.processInstId;
    }
    
    public void setProcessInstId(String processInstId) {
        this.processInstId = processInstId;
    }

    public String getToId() {
        return this.toId;
    }
    
    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getWoId() {
        return this.woId;
    }
    
    public void setWoId(String woId) {
        this.woId = woId;
    }

    public String getParentBizOrderId() {
        return this.parentBizOrderId;
    }
    
    public void setParentBizOrderId(String parentBizOrderId) {
        this.parentBizOrderId = parentBizOrderId;
    }

    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCurrentTaskId() {
        return this.currentTaskId;
    }
    
    public void setCurrentTaskId(String currentTaskId) {
        this.currentTaskId = currentTaskId;
    }

    public String getCurrentTaskName() {
        return this.currentTaskName;
    }
    
    public void setCurrentTaskName(String currentTaskName) {
        this.currentTaskName = currentTaskName;
    }

    public String getToTitle() {
        return this.toTitle;
    }
    
    public void setToTitle(String toTitle) {
        this.toTitle = toTitle;
    }

    public String getToDesc() {
        return this.toDesc;
    }
    
    public void setToDesc(String toDesc) {
        this.toDesc = toDesc;
    }

    public String getToType() {
        return this.toType;
    }
    
    public void setToType(String toType) {
        this.toType = toType;
    }

    public String getAssigner() {
        return this.assigner;
    }
    
    public void setAssigner(String assigner) {
        this.assigner = assigner;
    }

    public String getAssignerName() {
        return this.assignerName;
    }
    
    public void setAssignerName(String assignerName) {
        this.assignerName = assignerName;
    }

    public Long getAssignerOrgId() {
        return this.assignerOrgId;
    }
    
    public void setAssignerOrgId(Long assignerOrgId) {
        this.assignerOrgId = assignerOrgId;
    }

    public String getAssignComment() {
        return this.assignComment;
    }
    
    public void setAssignComment(String assignComment) {
        this.assignComment = assignComment;
    }

    public Date getAssignTime() {
        return this.assignTime;
    }
    
    public void setAssignTime(Date assignTime) {
        this.assignTime = assignTime;
    }

    public Date getRequireCompleteTime() {
        return this.requireCompleteTime;
    }
    
    public void setRequireCompleteTime(Date requireCompleteTime) {
        this.requireCompleteTime = requireCompleteTime;
    }

    public String getLastReplyPeople() {
        return this.lastReplyPeople;
    }
    
    public void setLastReplyPeople(String lastReplyPeople) {
        this.lastReplyPeople = lastReplyPeople;
    }

    public String getLastReplyPeopleName() {
        return this.lastReplyPeopleName;
    }
    
    public void setLastReplyPeopleName(String lastReplyPeopleName) {
        this.lastReplyPeopleName = lastReplyPeopleName;
    }

    public Date getFinalCompleteTime() {
        return this.finalCompleteTime;
    }
    
    public void setFinalCompleteTime(Date finalCompleteTime) {
        this.finalCompleteTime = finalCompleteTime;
    }

    public String getAcceptPeople() {
        return this.acceptPeople;
    }
    
    public void setAcceptPeople(String acceptPeople) {
        this.acceptPeople = acceptPeople;
    }

    public String getAcceptPeopleName() {
        return this.acceptPeopleName;
    }
    
    public void setAcceptPeopleName(String acceptPeopleName) {
        this.acceptPeopleName = acceptPeopleName;
    }

    public Date getAcceptTime() {
        return this.acceptTime;
    }
    
    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getAcceptComment() {
        return this.acceptComment;
    }
    
    public void setAcceptComment(String acceptComment) {
        this.acceptComment = acceptComment;
    }

    public String getCurrentHandler() {
        return this.currentHandler;
    }
    
    public void setCurrentHandler(String currentHandler) {
        this.currentHandler = currentHandler;
    }

    public String getCurrentHandlerName() {
        return this.currentHandlerName;
    }
    
    public void setCurrentHandlerName(String currentHandlerName) {
        this.currentHandlerName = currentHandlerName;
    }

    public Long getCurrentHandlerOrgId() {
        return this.currentHandlerOrgId;
    }
    
    public void setCurrentHandlerOrgId(Long currentHandlerOrgId) {
        this.currentHandlerOrgId = currentHandlerOrgId;
    }

    public Date getRejectTime() {
        return this.rejectTime;
    }
    
    public void setRejectTime(Date rejectTime) {
        this.rejectTime = rejectTime;
    }

    public String getRejectComment() {
        return this.rejectComment;
    }
    
    public void setRejectComment(String rejectComment) {
        this.rejectComment = rejectComment;
    }

    public Date getCancelTime() {
        return this.cancelTime;
    }
    
    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelComment() {
        return this.cancelComment;
    }
    
    public void setCancelComment(String cancelComment) {
        this.cancelComment = cancelComment;
    }

    public Date getReassignTime() {
        return this.reassignTime;
    }
    
    public void setReassignTime(Date reassignTime) {
        this.reassignTime = reassignTime;
    }

    public String getReassignComment() {
        return this.reassignComment;
    }
    
    public void setReassignComment(String reassignComment) {
        this.reassignComment = reassignComment;
    }

    public Integer getIsOverTime() {
        return this.isOverTime;
    }
    
    public void setIsOverTime(Integer isOverTime) {
        this.isOverTime = isOverTime;
    }

    public Integer getIsRead() {
        return this.isRead;
    }
    
    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }


	public Integer getIsSendWillOverTime() {
		return isSendWillOverTime;
	}


	public void setIsSendWillOverTime(Integer isSendWillOverTime) {
		this.isSendWillOverTime = isSendWillOverTime;
	}
	
	public Integer getIsSendOverTime() {
		return isSendOverTime;
	}

	public void setIsSendOverTime(Integer isSendOverTime) {
		this.isSendOverTime = isSendOverTime;
	}

	public String getUrgeWorkComment() {
		return urgeWorkComment;
	}


	public void setUrgeWorkComment(String urgeWorkComment) {
		this.urgeWorkComment = urgeWorkComment;
	}


	public String getOrderOwner() {
		return orderOwner;
	}


	public void setOrderOwner(String orderOwner) {
		this.orderOwner = orderOwner;
	}


	public Long getOrderOwnerOrgId() {
		return orderOwnerOrgId;
	}


	public void setOrderOwnerOrgId(Long orderOwnerOrgId) {
		this.orderOwnerOrgId = orderOwnerOrgId;
	}
   
	


}