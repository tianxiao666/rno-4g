package com.iscreate.op.service.bizmsg;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.constant.Constant;
import com.iscreate.op.dao.bizmsg.BizMessageDao;
import com.iscreate.op.pojo.bizmsg.BizMessage;
import com.iscreate.op.pojo.bizmsg.BizmsgMessageType;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.IdGenerator;

public class BizMessageServiceImpl implements BizMessageService{

	private BizMessageDao bizMessageDao;
	
	/**
	 * 添加新消息
	 * @param bizMsg
	 * @return
	 */
	public void txAddBizMessageService(BizMessage bizMsg,String type){
		this.saveBaseMessage(bizMsg, type);
	}
	
//	/**
//	 * 添加新任务消息
//	 * @param bizMsg
//	 * @return
//	 */
//	public void txAddNewTaskBizMessageService(BizMessage bizMsg){
//		this.saveBaseMessage(bizMsg, "newTask");
//	}
//	
//	/**
//	 * 添加超时任务消息
//	 * @param bizMsg
//	 * @return
//	 */
//	public void txAddOverTimeTaskBizMessageService(BizMessage bizMsg){
//		this.saveBaseMessage(bizMsg, "overTime");
//	}
//	
//	/**
//	 * 添加催办任务消息
//	 * @param bizMsg
//	 * @return
//	 */
//	public void txAddHastenTaskBizMessageService(BizMessage bizMsg){
//		this.saveBaseMessage(bizMsg, "hasten");
//	}
//	
//	/**
//	 * 添加消息
//	 * @param bizMsg
//	 * @return
//	 */
//	public void txAddNoteBizMessageService(BizMessage bizMsg){
//		this.saveBaseMessage(bizMsg, "note");
//	}
//	
//	/**
//	 * 添加撤销消息
//	 * @param bizMsg
//	 */
//	public void txAddRevokeBizMessageService(BizMessage bizMsg){
//		this.saveBaseMessage(bizMsg, "revoke");
//	}
//	
//	/**
//	 * 添加驳回消息
//	 * @param bizMsg
//	 */
//	public void txAddRebutBizmessageService(BizMessage bizMsg){
//		this.saveBaseMessage(bizMsg, "rebut");
//	}
//	
//	/**
//	 * 添加快超时
//	 * @param bizMsg
//	 */
//	public void txAddQuickOverTimeBizmessageService(BizMessage bizMsg){
//		this.saveBaseMessage(bizMsg, "quickOverTime");
//	}
	
	/**
	 * 根据接收人分页获取未读信息(包括响铃)
	 * @param receivePerson
	 * @param indexPage
	 * @param pageSize
	 * @return
	 */
	public List<BizMessage> getNoReadAndRingByPageService(String receivePerson){
		List<BizMessage> noReadMessageByPage = this.bizMessageDao.getNoReadMessageByPageForMobile(receivePerson, 0, 10);
		if(noReadMessageByPage!=null && noReadMessageByPage.size()>0){
			for (BizMessage bizMessage : noReadMessageByPage) {
				BizmsgMessageType messageTypeById = this.bizMessageDao.getMessageTypeById(bizMessage.getMessage_type_id());
				if(messageTypeById!=null){
					bizMessage.setTypeName(messageTypeById.getTypeName());
				}
				/*
				if(Constant.MESSAGE_TYPE_WORKORDER==bizMessage.getOrderType()){
					String woId = bizMessage.getOrderId();
					WorkmanageWorkorder workOrderEntity = workManageService.getWorkOrderEntity(woId);
					if(workOrderEntity!=null){
						bizMessage.setType(workOrderEntity.getWoType());
						bizMessage.setWoId(workOrderEntity.getWoId());
						bizMessage.setOrgId(workOrderEntity.getCreatorOrgId()+"");
					}
				}else
				if(Constant.MESSAGE_TYPE_TASKORDER==bizMessage.getOrderType()){
					String toId = bizMessage.getOrderId();
					Map<String, String> taskOrderForShow = workManageService.getTaskOrderForShow(toId);
					if(taskOrderForShow.containsKey("terminalFormUrl")){
						bizMessage.setType(taskOrderForShow.get("toType"));
						bizMessage.setWoId(taskOrderForShow.get("woId"));
						bizMessage.setToId(taskOrderForShow.get("toId"));
						bizMessage.setOrgId(taskOrderForShow.get("sssignerOrgId"));
						bizMessage.setLinkForMobile(taskOrderForShow.get("terminalFormUrl"));
					}
				}else if(Constant.MESSAGE_TYPE_NOTE==bizMessage.getOrderType()){
					
				}
				*/ 
			}
		}
		return noReadMessageByPage;
	}
	
	/**
	 * 获取接收人的消息总数
	 * @param receivePerson
	 * @return
	 */
	public int getNoReadMessageCountService(String receivePerson){
		int count = 0;
		List<BizMessage> noReadMessageByReceivePerson = this.bizMessageDao.getNoReadMessageByReceivePersonForMobile(receivePerson);
		if(noReadMessageByReceivePerson!=null && noReadMessageByReceivePerson.size()>0){
			count = noReadMessageByReceivePerson.size();
		}
		return count;
	}
	
	/**
	 * 接收人是否需要响铃
	 * @param receivePerson
	 * @return
	 */
	public boolean getRingMessageByReceivePersonService(String receivePerson){
		boolean isRing = false;
		List<BizMessage> stateMessageByReceivePerson = this.bizMessageDao.getStateMessageByReceivePersonForMobile(Constant.MESSAGE_RING, receivePerson);
		if(stateMessageByReceivePerson!=null && stateMessageByReceivePerson.size()>0){
			for (BizMessage bizMessage : stateMessageByReceivePerson) {
				bizMessage.setState(Constant.MESSAGE_NOREAD);
				//把响铃修改为未读状态
				this.bizMessageDao.updateMessage(bizMessage);
			}
			isRing = true;
		}
		return isRing;
	}
	
	/**
	 * 根据接收人和类型获取未读消息
	 * @param receivePerson
	 * @param type
	 * @return
	 */
	public List<BizMessage> getNoReadMessageByReceivePersonAndTypeService(String receivePerson,String type){
		List<BizMessage> noReadList = null;
		BizmsgMessageType messageTypeByTypeKey = this.bizMessageDao.getMessageTypeByTypeKey(type);
		if(messageTypeByTypeKey!=null){
			long msgTypeId = messageTypeByTypeKey.getId();
			noReadList = this.bizMessageDao.getStateMessageByReceivePersonAndMsgType(Constant.MESSAGE_NOREAD, receivePerson, msgTypeId);
		}
		return noReadList;
	}
	
	/**
	 * 根据接收人和类型获取响铃消息
	 * @param receivePerson
	 * @param type
	 * @return
	 */
	public List<BizMessage> getRingMessageByReceivePersonAndTypeService(String receivePerson,String type){
		List<BizMessage> ringList = null;
		BizmsgMessageType messageTypeByTypeKey = this.bizMessageDao.getMessageTypeByTypeKey(type);
		if(messageTypeByTypeKey!=null){
			long msgTypeId = messageTypeByTypeKey.getId();
			ringList = this.bizMessageDao.getStateMessageByReceivePersonAndMsgType(Constant.MESSAGE_RING, receivePerson, msgTypeId);
		}
		return ringList;
	}
	
	/**
	 * 根据接收人和类型获取已读消息
	 * @param receivePerson
	 * @param type
	 * @return
	 */
	public List<BizMessage> getHasReadMessageByReceivePersonAndTypeService(String receivePerson,String type){
		List<BizMessage> hasRingList = null;
		BizmsgMessageType messageTypeByTypeKey = this.bizMessageDao.getMessageTypeByTypeKey(type);
		if(messageTypeByTypeKey!=null){
			long msgTypeId = messageTypeByTypeKey.getId();
			hasRingList = this.bizMessageDao.getStateMessageByReceivePersonAndMsgType(Constant.MESSAGE_HASREAD, receivePerson, msgTypeId);
		}
		return hasRingList;
	}
	
	/**
	 * 加载消息盒子
	 * @param userId
	 * @return
	 */
	public Map<String,Object> loadBizMessageByUserIdService(String userId){
		Map<String,Object> map = new HashMap<String, Object>();
		List<BizMessage> list = this.bizMessageDao.getNoReadMessageByReceivePerson(userId);
		if(list!=null && list.size()>0){
			for (BizMessage bizMessage : list) {
				this.changeBizMessageObj(bizMessage);
			}
		}
		map.put("allMsgList", list);
		List<BizmsgMessageType> bizMsgTypeList = new ArrayList<BizmsgMessageType>();
		List<BizmsgMessageType> allMessageType = this.bizMessageDao.getAllMessageType();
		if(allMessageType!=null && allMessageType.size()>0){
			bizMsgTypeList.addAll(allMessageType);
		}
		map.put("msgTypeList", bizMsgTypeList);
		return map;
		/*
		Map<String,Object> map = new HashMap<String, Object>();
		//获取未读的新任务列表
		List<BizMessage> noReadList = this.getNoReadMessageByReceivePersonAndTypeService(userId, "newTask");
		List<BizMessage> ringList = this.getRingMessageByReceivePersonAndTypeService(userId, "newTask");
		List<BizMessage> newTaskList = new ArrayList<BizMessage>();
		newTaskList.addAll(noReadList);
		newTaskList.addAll(ringList);
		map.put("newTaskList", newTaskList);
		*/
		/*
		if(newTaskList!=null && newTaskList.size()>0){
			for (BizMessage bizMsg : newTaskList) {
				Integer orderType = bizMsg.getOrderType();
				if(Constant.MESSAGE_TYPE_WORKORDER==orderType){
					String woId = bizMsg.getOrderId();
					Map<String, String> workOrderForShow = this.workManageService.getWorkOrderForShow(woId);
					if(workOrderForShow.containsKey("formUrl")){
						bizMsg.setLink(workOrderForShow.get("formUrl"));
						bizMsg.setWoId(workOrderForShow.get("woId"));
						bizMsg.setOrgId(workOrderForShow.get("creatorOrgId"));
					}
				}else if(Constant.MESSAGE_TYPE_TASKORDER==orderType){
					String toId = bizMsg.getOrderId();
					Map<String, String> taskOrderForShow = this.workManageService.getTaskOrderForShow(toId);
					if(taskOrderForShow.containsKey("formUrl")){
						bizMsg.setLink(taskOrderForShow.get("formUrl"));
						bizMsg.setWoId(taskOrderForShow.get("woId"));
						bizMsg.setToId(taskOrderForShow.get("toId"));
						bizMsg.setOrgId(taskOrderForShow.get("assignerOrgId"));
					}
				}else if(Constant.MESSAGE_TYPE_NOTE==orderType){
					
				}
			}
			
		}*/
		/*
		//获取新任务未读数
		int newTaskCount = 0;
		if(noReadList!=null && noReadList.size()>0){
			newTaskCount += noReadList.size();
		}
		if(ringList!=null && ringList.size()>0){
			newTaskCount += ringList.size();
		}
		map.put("newTaskCount", newTaskCount);
		
		//获取超时任务数
		int overTimeCount = 0;
		List<BizMessage> noReadOverTimeList = this.getNoReadMessageByReceivePersonAndTypeService(userId, "overTime");
		List<BizMessage> ringOverTimeList = this.getRingMessageByReceivePersonAndTypeService(userId, "overTime");
		if(noReadOverTimeList!=null && noReadOverTimeList.size()>0){
			overTimeCount += noReadOverTimeList.size();
		}
		if(ringOverTimeList!=null && ringOverTimeList.size()>0){
			overTimeCount += ringOverTimeList.size();
		}
		map.put("overTimeCount", overTimeCount);
		
		//获取催办任务数
		int hastenCount = 0;
		List<BizMessage> noReadHastenList = this.getNoReadMessageByReceivePersonAndTypeService(userId, "hasten");
		List<BizMessage> ringHastenList = this.getRingMessageByReceivePersonAndTypeService(userId, "hasten");
		if(noReadHastenList!=null && noReadHastenList.size()>0){
			hastenCount += noReadHastenList.size();
		}
		if(ringHastenList!=null && ringHastenList.size()>0){
			hastenCount += ringHastenList.size();
		}
		map.put("hastenCount", hastenCount);
		
		//获取催办任务数
		int noteCount = 0;
		List<BizMessage> noReadNoteList = this.getNoReadMessageByReceivePersonAndTypeService(userId, "note");
		List<BizMessage> ringNoteList = this.getRingMessageByReceivePersonAndTypeService(userId, "note");
		if(noReadNoteList!=null && noReadNoteList.size()>0){
			noteCount += noReadNoteList.size();
		}
		if(ringNoteList!=null && ringNoteList.size()>0){
			noteCount += ringNoteList.size();
		}
		map.put("noteCount", noteCount);
		return map;
		*/
	}
	
	/**
	 * 根据接收人和类型和状态获取消息
	 * @param userId
	 * @param type
	 * @param state "0":未读  "1":已读  "2":所有  "3":没有
	 * @return
	 */
	public Map<String,Object> loadBizMessageByUserIdAndTypeAndStateService(String userId,String type,String state){
		Map<String,Object> map = new HashMap<String, Object>();
		List<BizMessage> list = new ArrayList<BizMessage>();
		if("0".equals(state)){
			//消息集合
			List<BizMessage> noReadList = new ArrayList<BizMessage>();
			List<BizMessage> ringReadList = new ArrayList<BizMessage>();
			if("all".equals(type)){
				List<BizMessage> listAll = this.bizMessageDao.getNoReadMessageByReceivePerson(userId);
				if(listAll!=null && listAll.size()>0){
					noReadList.addAll(listAll);
				}
			}else{
				noReadList = this.getNoReadMessageByReceivePersonAndTypeService(userId,type);
				ringReadList = this.getRingMessageByReceivePersonAndTypeService(userId,type);
			}
			
			
			//消息数
			int count = 0;
			if(noReadList!=null && noReadList.size()>0){
				for (BizMessage bizMessage : noReadList) {
					this.changeBizMessageObj(bizMessage);
				}
				list.addAll(noReadList);
				count = noReadList.size();
			}
			if(ringReadList!=null && ringReadList.size()>0){
				for (BizMessage bizMessage : ringReadList) {
					this.changeBizMessageObj(bizMessage);
				}
				list.addAll(ringReadList);
				count = ringReadList.size();
			}
			
			map.put("count", count);
		}else if("1".equals(state)){
			List<BizMessage> hasReadList = new ArrayList<BizMessage>();
			if("all".equals(type)){
				List<BizMessage> messageByReceivePerson = this.bizMessageDao.getMessageByReceivePerson(userId);
				if(messageByReceivePerson!=null && messageByReceivePerson.size()>0){
					for (BizMessage bizMessage : messageByReceivePerson) {
						if("1".equals(bizMessage.getState()+"")){
							hasReadList.add(bizMessage);
						}
					}
				}
			}else{
				hasReadList = this.getHasReadMessageByReceivePersonAndTypeService(userId,type);
			}
			//消息集合
			if(hasReadList!=null && hasReadList.size()>0){
				for (BizMessage bizMessage : hasReadList) {
					this.changeBizMessageObj(bizMessage);
				}
				list.addAll(hasReadList);
			}
			
			//消息数
			int count = 0;
			if(hasReadList!=null && hasReadList.size()>0){
				count = hasReadList.size();
			}
			map.put("count", count);
		}else if("2".equals(state)){
			//消息集合
			List<BizMessage> noReadList = new ArrayList<BizMessage>();
			List<BizMessage> hasReadList = new ArrayList<BizMessage>();
			List<BizMessage> ringReadList = new ArrayList<BizMessage>();
			if("all".equals(type)){
				noReadList =  this.bizMessageDao.getMessageByReceivePerson(userId);
			}else{
				noReadList = this.getNoReadMessageByReceivePersonAndTypeService(userId,type);
				hasReadList = this.getHasReadMessageByReceivePersonAndTypeService(userId,type);
				ringReadList = this.getRingMessageByReceivePersonAndTypeService(userId,type);
			}
			
			
			//消息数
			int count = 0;
			if(noReadList!=null && noReadList.size()>0){
				for (BizMessage bizMessage : noReadList) {
					this.changeBizMessageObj(bizMessage);
				}
				list.addAll(noReadList);
				count += noReadList.size();
			}
			if(hasReadList!=null && hasReadList.size()>0){
				for (BizMessage bizMessage : hasReadList) {
					this.changeBizMessageObj(bizMessage);
				}
				list.addAll(hasReadList);
				count += hasReadList.size();
			}
			if(ringReadList!=null && ringReadList.size()>0){
				for (BizMessage bizMessage : ringReadList) {
					this.changeBizMessageObj(bizMessage);
				}
				list.addAll(ringReadList);
				count += ringReadList.size();
			}
			map.put("count", count);
		}else{
			map.put("count", 0);
		}
		
		/*
		if(list!=null && list.size()>0){
			for (BizMessage bizMsg : list) {
				Integer orderType = bizMsg.getOrderType();
				if(Constant.MESSAGE_TYPE_WORKORDER==orderType){
					String woId = bizMsg.getOrderId();
					Map<String, String> workOrderForShow = this.workManageService.getWorkOrderForShow(woId);
					if(workOrderForShow.containsKey("formUrl")){
						bizMsg.setLink(workOrderForShow.get("formUrl"));
						bizMsg.setWoId(workOrderForShow.get("woId"));
						bizMsg.setOrgId(workOrderForShow.get("creatorOrgId"));
					}
				}else if(Constant.MESSAGE_TYPE_TASKORDER==orderType){
					String toId = bizMsg.getOrderId();
					Map<String, String> taskOrderForShow = this.workManageService.getTaskOrderForShow(toId);
					if(taskOrderForShow.containsKey("formUrl")){
						bizMsg.setLink(taskOrderForShow.get("formUrl"));
						bizMsg.setWoId(taskOrderForShow.get("woId"));
						bizMsg.setToId(taskOrderForShow.get("woId"));
						bizMsg.setOrgId(taskOrderForShow.get("assignerOrgId"));
					}
				}else if(Constant.MESSAGE_TYPE_NOTE==orderType){
					
				}
			}
		}
		*/
		map.put("list", list);
		return map;
	}
	
	/**
	 * 根据Id修改消息为已读
	 * @param id
	 */
	public void updateBizMsgByIdService(String id){
		BizMessage bizMsg = this.bizMessageDao.getMessageById(id);
		if(bizMsg!=null){
			bizMsg.setState(Constant.MESSAGE_HASREAD);
			bizMsg.setReadTime(new Date());
			this.bizMessageDao.updateMessage(bizMsg);
		}
	}
	
	/**
	 * 根据接收人和单号修改消息为已读状态
	 * @param receivePerson
	 * @param orderId
	 */
	public void updateBizMsgToHasReadByReceivePersonAndOrderIdService(String receivePerson,String orderId){
		List<BizMessage> msgList = this.bizMessageDao.getMessageByReceivePersonAndOrderId(receivePerson, orderId);
		if(msgList!=null && msgList.size()>0){
			for (BizMessage bizMessage : msgList) {
				bizMessage.setState(Constant.MESSAGE_HASREAD);
				this.bizMessageDao.updateMessage(bizMessage);
			}
		}
	}
	
	/**
	 * 根据id删除信息
	 * @param id
	 */
	public void deleteBizMessage(String id){
		BizMessage bizMsg = this.bizMessageDao.getMessageById(id);
		this.bizMessageDao.deleteMessage(bizMsg);
	}
	
	/**
	 * 批量删除消息
	 * @param ids
	 */
	public void deleteBizMessageByIdsService(String ids){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String[] split = ids.split("-");
		if(ids!=null && !"".equals(ids)){
			for (String id : split) {
				BizMessage bizMsg = this.bizMessageDao.getMessageById(id);
				if(bizMsg!=null){
					this.bizMessageDao.deleteMessage(bizMsg);
				}
			}
		}
		String result = gson.toJson("success");
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			throw new UserDefinedException("返回时出错");
		}
	}
	
	/**
	 * 获取未响铃数
	 * @param userId
	 */
	public void getNoReadBizMessageCountAjaxService(String userId){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		int noReadMsgCount = 0;
		List<BizMessage> ringList = this.bizMessageDao.getStateMessageByReceivePerson(Constant.MESSAGE_RING, userId);
		if(ringList!=null && ringList.size()>0){
			noReadMsgCount = ringList.size();
		}
		String result = gson.toJson(noReadMsgCount);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			throw new UserDefinedException("返回时出错");
		}
	}
	
	/**
	 * 根据用户Id获取响铃消息
	 * @param userId
	 */
	public void txGetRingBizMessageByUserIdAjaxService(String userId){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		Map<String,String> map = new HashMap<String, String>();
		map.put("ringCount", "0");
		map.put("isRing", "false");
		List<BizMessage> ringList = this.bizMessageDao.getStateMessageByReceivePerson(Constant.MESSAGE_RING, userId);
		if(ringList!=null && ringList.size()>0){
			map.put("ringCount", ringList.size()+"");
			map.put("isRing", "true");
			for (BizMessage bizMessage : ringList) {
				bizMessage.setState(Constant.MESSAGE_NOREAD);
				this.bizMessageDao.updateMessage(bizMessage);
			}
		}
		String result = gson.toJson(ringList.size());
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			throw new UserDefinedException("返回时出错");
		}
	}
	
	/**
	 * 根据用户、状态和类型获取信息数量
	 * @param userId
	 * @param state
	 * @param type
	 * @return
	 */
	public int getBizMessageCountByStateAndType(String userId,int state,String type){
		int count = 0;
		BizmsgMessageType bmType = this.bizMessageDao.getMessageTypeByTypeKey(type);
		if(bmType!=null){
			List<BizMessage> bizMsgList = this.bizMessageDao.getStateMessageByReceivePersonAndMsgType(state, userId, bmType.getId());
			if(state==0){
				List<BizMessage> stateMessageByReceivePersonAndMsgType = this.bizMessageDao.getStateMessageByReceivePersonAndMsgType(2, userId, bmType.getId());
				bizMsgList.addAll(stateMessageByReceivePersonAndMsgType);
			}
			if(bizMsgList!=null && bizMsgList.size()>0){
				count = bizMsgList.size();
			}
		}
		return count;
	}
	
	/**
	 * 根据用户、状态和类型获取信息（分页）
	 * @param receivePerson
	 * @param state
	 * @param type
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<BizMessage> getBizMessageByStateAndTypeWithPage(String receivePerson,int state,String type,int pageIndex,int pageSize){
		List<BizMessage> list = new ArrayList<BizMessage>();
		BizmsgMessageType bmType = this.bizMessageDao.getMessageTypeByTypeKey(type);
		if(bmType!=null){
			list = this.bizMessageDao.getMessageToPageByReceivePersonAndStateAndType(receivePerson, state, bmType.getId(), pageIndex, pageSize);
		}
		return list;
	}
	
	/**
	 * 修改信息为已读
	 * @param bizMessage
	 */
	public void updateBizMessageToHasRead(BizMessage bizMessage){
		if(bizMessage!=null){
			bizMessage.setState(Constant.MESSAGE_HASREAD);
			this.bizMessageDao.updateMessage(bizMessage);
		}
	}
	
	/**
	 * 根据标题获取未读和响铃信息
	 * @param title
	 * @return
	 */
	public List<BizMessage> getNoReadAndRingMessageByTitleService(String title){
		return this.bizMessageDao.getNoReadAndRingMessageByTitle(title);
	}
	
	/**
	 * 根据标题集合修改信息为已读
	 * @param titleList
	 */
	public void txUpdateBizMessageToHasReadByTitleList(List<String> titleList){
		if(titleList!=null && titleList.size()>0){
			for (String title : titleList) {
				List<BizMessage> noReadAndRingMessageByTitleService = this.getNoReadAndRingMessageByTitleService(title);
				if(noReadAndRingMessageByTitleService!=null && noReadAndRingMessageByTitleService.size()>0){
					for (BizMessage bizMessage : noReadAndRingMessageByTitleService) {
						this.updateBizMessageToHasRead(bizMessage);
					}
				}
			}
		}
	}
	
	//内部方法=====================================================================
	
	/**
	 * 添加信息的基础方法
	 * @param bizMsg
	 * @param bizMessageType
	 */
	private void saveBaseMessage(BizMessage bizMsg,String bizMessageType){
		String id = IdGenerator.makeUuidString();
		bizMsg.setId(id);
		bizMsg.setState(Constant.MESSAGE_RING);
		if(bizMessageType!=null && !"".equals(bizMessageType)){
			BizmsgMessageType messageTypeByTypeKey = this.bizMessageDao.getMessageTypeByTypeKey(bizMessageType);
			if(messageTypeByTypeKey!=null){
				Long typeId = messageTypeByTypeKey.getId();
				bizMsg.setMessage_type_id(typeId);
				this.bizMessageDao.saveMessage(bizMsg);
			}
		}
	}

	/**
	 * 组装消息对象
	 * @param bizMsg
	 * @return
	 */
	private BizMessage changeBizMessageObj(BizMessage bizMessage){
		Long msgTypeId = bizMessage.getMessage_type_id();
		BizmsgMessageType messageTypeById = this.bizMessageDao.getMessageTypeById(msgTypeId);
		bizMessage.setBisMsgType(messageTypeById);
		String type = bizMessage.getType();
		String title = bizMessage.getTitle();
		bizMessage.setTitle("【"+type+"】"+" "+title);
		return bizMessage;
	}
	
	public BizMessageDao getBizMessageDao() {
		return bizMessageDao;
	}

	public void setBizMessageDao(BizMessageDao bizMessageDao) {
		this.bizMessageDao = bizMessageDao;
	}
	/**
	 * 
	 * @description: 根据消息状态 功能类型获取消息数量
	 * @author：yuan.yw
	 * @param userId
	 * @param messageStateId
	 * @param functionType
	 * @return     
	 * @return int     
	 * @date：Jul 12, 2013 10:30:10 AM
	 */
	public int getBizMessageCountByStatusAndFunctionType(String userId, String messageStateId,String functionType){
		int count =0;//数量
		//消息列表
		List<Map<String,Object>> resultList = this.bizMessageDao.getMessageListByStatusAndFunctionTypeAndMsgType(userId, messageStateId,functionType,null);
		if(resultList!=null && !resultList.isEmpty()){
			count = resultList.size();
		}
		return count;
	}
	/**
	 * 
	 * @description: 根据消息状态 功能类型获取消息列表
	 * @author：yuan.yw
	 * @param userId
	 * @param messageStateId
	 * @param functionType
	 * @param messageType
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jul 12, 2013 10:30:10 AM
	 */
	public List<Map<String,Object>> getBizMessageListByStatusAndFunctionTypeAndMsgType(String userId, String messageStateId,String functionType,String messageType){
		//消息列表
		List<Map<String,Object>> resultList = this.bizMessageDao.getMessageListByStatusAndFunctionTypeAndMsgType(userId, messageStateId,functionType,messageType);
		return resultList;
	}

}
