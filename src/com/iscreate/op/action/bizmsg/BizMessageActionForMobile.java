package com.iscreate.op.action.bizmsg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.constant.Constant;
import com.iscreate.op.pojo.bizmsg.BizMessage;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageCommunicationHelper;
import com.iscreate.plat.mobile.util.MobilePackageUtil;


public class BizMessageActionForMobile {

	private BizMessageService bizMessageService;
	private String loadPageReadState;
	private int newTaskMsgCount;
	private int overTimeMsgCount;
	private int hastenMsgCount;
	private String ids;
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * 加载消息盒子内容层
	 * @return
	 */
	public void loadBizMessageForMobileAction() {
		log.info("终端：进入loadBizMessageForMobileAction方法");
		Gson gson = null;
		HttpServletResponse response = null;
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			//从session获取user
			String userId = (String)session.getAttribute("userId");
			if(userId==null || "".equals(userId)){
				log.info("获取不到用户Id");
				MobilePackageCommunicationHelper.responseMobileError("获取不到用户Id");
			}else{
				response = ServletActionContext.getResponse();
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/json");
				
				GsonBuilder builder = new GsonBuilder();
				gson = builder.create();
				
				MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
				
				//mobilePackage为空，返回错误信息
				if(mobilePackage == null) {
					log.info("loadBizMessageForMobileAction方法中的mobilePackage为空，返回错误信息");
					MobilePackage newMobilePackage = new MobilePackage();
					newMobilePackage.setResult("error");
					//返回content的JSON字符串信息
					String resultPackageJsonStr = gson.toJson(newMobilePackage);
					response.getWriter().write(resultPackageJsonStr);
					return;
				}
				String content = mobilePackage.getContent();
				MobileContentHelper mch = new MobileContentHelper();
				mch.setContent(content);
				
				Map<String, String> formJsonMap = mch.getGroupByKey("request");
				this.loadPageReadState = formJsonMap.get("loadPageReadState");
				if(this.loadPageReadState == null) {
					this.loadPageReadState = "noRead";
				}
				
				//获取消息列表的类型
				String msgType = formJsonMap.get("msgType");
				//若没有传消息类型参数，默认值为未读消息
				if(msgType == null) {
					msgType = "newTask";
				}
				
				String newTaskPageIndex = formJsonMap.get("newTaskPageIndex");
				String overTimeTaskPageIndex = formJsonMap.get("overTimeTaskPageIndex");
				String hastenTaskPageIndex = formJsonMap.get("hastenTaskPageIndex");
				
				int pageIndex = 0;
				
				if(newTaskPageIndex == null && overTimeTaskPageIndex == null && hastenTaskPageIndex == null) {
					pageIndex = 1;
				} else if(newTaskPageIndex != null) {
					pageIndex = Integer.parseInt(newTaskPageIndex);
				} else if(overTimeTaskPageIndex != null) {
					pageIndex = Integer.parseInt(overTimeTaskPageIndex);
				} else if(hastenTaskPageIndex != null) {
					pageIndex = Integer.parseInt(hastenTaskPageIndex);
				}
				
				int readState = "noRead".equals(this.loadPageReadState) ? Constant.MESSAGE_NOREAD : Constant.MESSAGE_HASREAD;
				//获取新任务、超时任务、催办任务的消息数量
				this.newTaskMsgCount = bizMessageService.getBizMessageCountByStateAndType(userId, readState, "newTask");
				log.info("获取新任务数为："+this.newTaskMsgCount);
				this.overTimeMsgCount = bizMessageService.getBizMessageCountByStateAndType(userId, readState, "overTime");
				log.info("获取超时任务数为："+this.overTimeMsgCount);
				this.hastenMsgCount = bizMessageService.getBizMessageCountByStateAndType(userId, readState, "hasten");
				log.info("获取催办任务数为："+this.hastenMsgCount);
	//			System.out.println(this.newTaskMsgCount+"   "+this.overTimeMsgCount+"   "+this.hastenMsgCount);
				Map<String,String> bizMsgCountMap = new HashMap<String, String>();
				bizMsgCountMap.put("newTaskMsgCount", this.newTaskMsgCount+"");
				bizMsgCountMap.put("overTimeMsgCount", this.overTimeMsgCount+"");
				bizMsgCountMap.put("hastenMsgCount", this.hastenMsgCount+"");
				
				mch.addGroup("msgCountDiv", bizMsgCountMap);
				//消息状态id
				int msgState = "noRead".equals(this.loadPageReadState) ? Constant.MESSAGE_NOREAD : Constant.MESSAGE_HASREAD;
				//获取未读消息(根据类型获取，类型有：新任务、快超时、催办)(分页获取，获取10行数据)
				List<BizMessage> noReadMsgAeList = bizMessageService.getBizMessageByStateAndTypeWithPage(userId, msgState, msgType,pageIndex-1,10);
				
				List<Map<String, Object>> bizMsgMapList = new ArrayList<Map<String,Object>>();
				if(noReadMsgAeList != null && !noReadMsgAeList.isEmpty()) {
					for(BizMessage bizMsg : noReadMsgAeList) {
						Map<String, Object> bizMsgMap = new HashMap<String, Object>();
						//格式化url(去除模板名)
						bizMsgMap.put("MOBILEFORMURL", bizMsg.getLinkForMobile());
						//生成任务单类型，中文描述
						bizMsgMap.put("toTypeName", bizMsg.getType());
						
						//消息盒子的id号
						bizMsgMap.put("id", bizMsg.getId());
						
						//消息盒子内容
						bizMsgMap.put("content", bizMsg.getContent());
								
						SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
						bizMsgMap.put("sendTime", sdf.format(bizMsg.getSendTime()));
						
						//整个为一个带消息类型的消息map
						bizMsgMapList.add(bizMsgMap);
					}
				}else{
					log.info("获取未读消息数为：0条");
				}
				
				//根据不同类型的消息，加载到不同的消息列表中
				String divName = "";
				String divAreaName = "";
				if("newTask".equals(msgType)) {
					log.info("把消息加载到”新任务“列表中");
					divName = "newTaskDiv";
					divAreaName = "newTaskDivArea";
				} else if("overTime".equals(msgType)) {
					log.info("把消息加载到”快超时“列表中");
					divName = "overTimeTaskDiv";
					divAreaName = "overTimeTaskDivArea";
				} else if("hasten".equals(msgType)) {
					log.info("把消息加载到”催办任务“列表中");
					divName = "hastenTaskDiv";
					divAreaName = "hastenTaskDivArea";
				}
				
				Map<String,String> noReadMsgListMap = new HashMap<String, String>();
				noReadMsgListMap.put(divName, gson.toJson(bizMsgMapList));
				
				mch.addGroup(divAreaName, noReadMsgListMap);
				mobilePackage.setContent(mch.mapToJson());
				MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			}
		} catch (Exception e) {
			log.error("loadBizMessageForMobileAction方法运行时出错");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：成功执行了loadBizMessageForMobileAction方法，该方法实现了“加载消息盒子的内容层”");
		log.info("退出loadBizMessageForMobileAction方法，返回void");
	}
	
	/**
	 * 更新消息的状态，更新状态为已读
	 */
	public void updateMsgToHasReadForMobileAction() {
		log.info("终端：进入updateMsgToHasReadForMobileAction方法");
		Gson gson = null;
		HttpServletResponse response = null;
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			//从session获取user
			String userId = (String)session.getAttribute("userId");
			String userName = (String)session.getAttribute("userName");
			
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				log.error("updateMsgToHasReadForMobileAction方法中的mobilePackage为空，返回错误信息");
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String updatedIds = formJsonMap.get("checkMixVal");
			
			if(updatedIds != null && !"".equals(updatedIds)) {
				String[] idsArr = null;
				if(updatedIds.indexOf(",") > -1) {
					//需要删除的消息在一条以上
					idsArr = updatedIds.split(",");
				} else {
					//只需要删除一条消息
					idsArr = new String[]{updatedIds};
				}
				if(idsArr != null && idsArr.length > 0) {
					for (String msgId : idsArr) {
						this.bizMessageService.updateBizMsgByIdService(msgId);
					}
				}
				//重新加载消息
				loadBizMessageForMobileAction();
			}
		} catch (Exception e) {
			log.error("updateMsgToHasReadForMobileAction方法执行失败!");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：updateMsgToHasReadForMobileAction方法执行成功，实现了“更新消息的状态，更新状态为已读”的功能");
		log.info("终端：退出updateMsgToHasReadForMobileAction方法");
	}
	
	/**
	 * 删除消息
	 */
	public void deleteBizMessageForMobileAction() {
		log.info("终端:进入deleteBizMessageForMobileAction方法");
		Gson gson = null;
		HttpServletResponse response = null;
		
		try {
			
			HttpSession session = ServletActionContext.getRequest().getSession();
			//从session获取user
			String userId = (String)session.getAttribute("userId");
			String userName = (String)session.getAttribute("userName");
			
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			this.ids = formJsonMap.get("checkMixVal");
			
			if(this.ids != null && !"".equals(this.ids)) {
				String[] idsArr = null;
				if(ids.indexOf(",") > -1) {
					//需要删除的消息在一条以上
					idsArr = this.ids.split(",");
				} else {
					//只需要删除一条消息
					idsArr = new String[]{this.ids};
				}
				//批量删除消息
				for (String msgId : idsArr) {
					this.bizMessageService.deleteBizMessage(msgId);
				}
				
				//重新加载消息
				loadBizMessageForMobileAction();
			}
		} catch (Exception e) {
			log.error("deleteBizMessageForMobileAction方法执行失败!");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：deleteBizMessageForMobileAction方法执行成功，实现了“删除消息后重新加载消息”的功能");
		log.info("终端:退出deleteBizMessageForMobileAction方法,返回void");
	}
	
	/**
	 * 是否响铃
	 */
	public void isRingableActionForMobile(){
		log.info("终端:进入isRingableActionForMobile方法");
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			String userId = (String)session.getAttribute("userId");
			String userName = (String)session.getAttribute("userName");
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			
			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();
			if(mobilePackage == null) {
				mobilePackage = new MobilePackage();
			}
			MobileContentHelper mch = null;
			
			if(userId!=null && !"".equals(userId)){
				mch = new MobileContentHelper();
				Map<String, String> loginContentMap = new HashMap<String, String>();
				
				//保存用户信息的同时，未读消息数
				boolean isRingBool = this.bizMessageService.getRingMessageByReceivePersonService(userId);
				String isRing = "false";
				if(isRingBool){
					log.info("启动响铃状态");
					isRing = "true";
				}else{
					log.info("没有查找到响铃消息");
				}
				loginContentMap.put("isRing", isRing);
				loginContentMap.put("userId", userId);
				loginContentMap.put("userName", userName);
				mch.addGroup("header", loginContentMap);
				
				mobilePackage.setContent(mch.mapToJson());
			}
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			log.error("isRingableActionForMobile方法执行失败!");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：isRingableActionForMobile方法执行成功，实现了“终端响铃”的功能");
		log.info("终端:退出isRingableActionForMobile方法,返回void");
	}
	
	//////////////////////////////////////终端新接口//////////////
	
	/**
	 * 
	 * @description: 获取是否有未读消息信息
	 * @author：yuan.yw     
	 * @return void     
	 * @date：Jul 12, 2013 10:08:30 AM
	 */
	public void hasUnReadMessageActionForMobile(){
		log.info("终端:进入hasUnReadMessageActionForMobile方法");
		Gson gson = null;
		HttpServletResponse response = null;
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			//从session获取user
			String userId = (String)session.getAttribute("userId");
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String messageStateId = formJsonMap.get("messageStateId");
			String state = messageStateId;
			if( messageStateId.equals(Constant.MESSAGE_NOREAD+"")){
				state = Constant.MESSAGE_NOREAD +","+Constant.MESSAGE_RING;
			}
			String functionType = "'UrgentRepairSenceTaskOrder'";
			int newMessageCount = this.bizMessageService.getBizMessageCountByStatusAndFunctionType(userId, state,functionType);
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("messageStateId", messageStateId);
			resultMap.put("newMessageCount", newMessageCount+"");
			mch.addGroup("resultMap", resultMap);
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			
		} catch (Exception e) {
			log.error("hasUnReadMessageActionForMobile方法执行失败!");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：hasUnReadMessageActionForMobile方法执行成功，实现了“获取是否有未读消息信息”的功能");
		log.info("终端:退出hasUnReadMessageActionForMobile方法,返回void");
	}
	/**
	 * 
	 * @description: 获取未读消息
	 * @author：yuan.yw     
	 * @return void     
	 * @date：Jul 12, 2013 10:08:30 AM
	 */
	public void getUnReadMessageListActionForMobile(){
		log.info("终端:进入getUnReadMessageListActionForMobile方法");
		Gson gson = null;
		HttpServletResponse response = null;
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			//从session获取user
			String userId = (String)session.getAttribute("userId");
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String messageStateId = formJsonMap.get("messageStateId");
			String state = messageStateId;
			if( messageStateId.equals(Constant.MESSAGE_NOREAD+"")){
				state = Constant.MESSAGE_NOREAD +","+Constant.MESSAGE_RING;
			}
			String functionType = "'UrgentRepairSenceTaskOrder'";
			String messageType = formJsonMap.get("messageType");
			String indexStart = formJsonMap.get("indexStart");
			String indexEnd = formJsonMap.get("indexEnd");
			List<Map<String,Object>> newMessageList = this.bizMessageService.getBizMessageListByStatusAndFunctionTypeAndMsgType(userId, state,functionType,messageType);
			List<Map<String,Object>> resultList = null;
			int count = 0;
			if(newMessageList!=null && !newMessageList.isEmpty()){
				count = newMessageList.size();
			}
			if(indexStart!=null && !"".equals(indexStart) && indexEnd!=null && !"".equals(indexEnd)){
				int start = Integer.parseInt(indexStart); 
				int end = Integer.parseInt(indexEnd);
				if(start < 0)
				{
					start = 0;
				}
				if(end >= count )
				{
					end = count - 1;
				}
				if(newMessageList!=null && !newMessageList.isEmpty()){
					if(start>=0 && start<newMessageList.size()&& end<newMessageList.size() && end>=start ){
						resultList = new ArrayList<Map<String,Object>>();
						for(int i=0;i<newMessageList.size();i++){
							
							if(i>=start && i<=end){
								resultList.add(newMessageList.get(i));
							}
						}
					}
				}
			}else{
				resultList = newMessageList;
			}
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("messageStateId", messageStateId);
			resultMap.put("messageType", messageType);
			resultMap.put("newMessageList", gson.toJson(resultList));
			resultMap.put("newMessageCount", count+"");
			mch.addGroup("resultMap", resultMap);
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			
		} catch (Exception e) {
			log.error("getUnReadMessageListActionForMobile方法执行失败!");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：getUnReadMessageListActionForMobile方法执行成功，实现了“获取未读消息”的功能");
		log.info("终端:退出getUnReadMessageListActionForMobile方法,返回void");
	}
	/**
	 * 
	 * @description: 操作消息
	 * @author：yuan.yw     
	 * @return void     
	 * @date：Jul 12, 2013 10:08:30 AM
	 */
	public void doOperationForMessageActionForMobile(){
		log.info("终端:进入doOperationForMessageActionForMobile方法");
		Gson gson = null;
		HttpServletResponse response = null;	
		try {
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackage newMobilePackage = new MobilePackage();
				newMobilePackage.setResult("error");
				//返回content的JSON字符串信息
				String resultPackageJsonStr = gson.toJson(newMobilePackage);
				response.getWriter().write(resultPackageJsonStr);
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			String result = "fail";
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			this.ids = formJsonMap.get("messageId");
			String messageOperateId = formJsonMap.get("messageOperateId");
			if("1".equals(messageOperateId)){//更新消息状态为已读
				if(this.ids != null && !"".equals(this.ids)) {
					String[] idsArr = null;
					if(ids.indexOf(",") > -1) {
						//需要删除的消息在一条以上
						idsArr = this.ids.split(",");
					} else {
						//只需要删除一条消息
						idsArr = new String[]{this.ids};
					}
					//批量删除消息
					for (String msgId : idsArr) {
						this.bizMessageService.updateBizMsgByIdService(msgId);
					}	
					result="success";
				}
			}else if("2".equals(messageOperateId)){//删除消息
				if(this.ids != null && !"".equals(this.ids)) {
					String[] idsArr = null;
					if(ids.indexOf(",") > -1) {
						//需要删除的消息在一条以上
						idsArr = this.ids.split(",");
					} else {
						//只需要删除一条消息
						idsArr = new String[]{this.ids};
					}
					//批量删除消息
					for (String msgId : idsArr) {
						this.bizMessageService.deleteBizMessage(msgId);
					}	
					result="success";
				}
			}
			Map<String,String> resultMap = new HashMap<String,String>();
			resultMap.put("messageId", this.ids);
			resultMap.put("messageOperateId", messageOperateId);
			resultMap.put("messageOperationResult",result);
			mch.addGroup("resultMap", resultMap);
			mobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			log.error("doOperationForMessageActionForMobile方法执行失败!");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		log.info("终端：doOperationForMessageActionForMobile方法执行成功，实现了“操作消息(更新，删除)”的功能");
		log.info("终端:退出doOperationForMessageActionForMobile方法,返回void");
	}
	public BizMessageService getBizMessageService() {
		return bizMessageService;
	}

	public void setBizMessageService(BizMessageService bizMessageService) {
		this.bizMessageService = bizMessageService;
	}

	public String getLoadPageReadState() {
		return loadPageReadState;
	}

	public void setLoadPageReadState(String loadPageReadState) {
		this.loadPageReadState = loadPageReadState;
	}

	public int getNewTaskMsgCount() {
		return newTaskMsgCount;
	}

	public void setNewTaskMsgCount(int newTaskMsgCount) {
		this.newTaskMsgCount = newTaskMsgCount;
	}

	public int getOverTimeMsgCount() {
		return overTimeMsgCount;
	}

	public void setOverTimeMsgCount(int overTimeMsgCount) {
		this.overTimeMsgCount = overTimeMsgCount;
	}

	public int getHastenMsgCount() {
		return hastenMsgCount;
	}

	public void setHastenMsgCount(int hastenMsgCount) {
		this.hastenMsgCount = hastenMsgCount;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	
}
