package com.iscreate.op.dao.bizmsg;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.bizmsg.BizMessage;
import com.iscreate.op.pojo.bizmsg.BizmsgMessageType;



public interface BizMessageDao {

	/**
	 * 保存消息
	 * @param bizMsg
	 */
	public void saveMessage(BizMessage bizMsg);
	
	/**
	 * 修改消息
	 * @param bizMsg
	 */
	public void updateMessage(BizMessage bizMsg);
	
	/**
	 * 删除消息
	 * @param bizMsg
	 */
	public void deleteMessage(BizMessage bizMsg);
	
	/**
	 * 根据接收人获取消息
	 * @param receivePerson
	 * @return
	 */
	public List<BizMessage> getMessageByReceivePerson(String receivePerson);
	
	/**
	 * 根据接收人获取信息 [终端]
	 * @param receivePerson
	 * @return
	 */
	public List<BizMessage> getStateMessageByReceivePersonForMobile(int state,String receivePerson);
	
	/**
	 * 根据接收人分页获取未读信息(包括响铃)
	 * @param receivePerson
	 * @param indexPage
	 * @param pageSize
	 * @return
	 */
	public List<BizMessage> getNoReadMessageByPage(final String receivePerson,final int indexPage,final int pageSize);
	
	/**
	 * 根据接收人分页获取未读信息(包括响铃)[终端]
	 * @param receivePerson
	 * @param indexPage
	 * @param pageSize
	 * @return
	 */
	public List<BizMessage> getNoReadMessageByPageForMobile(final String receivePerson,final int indexPage,final int pageSize);
	
	/**
	 * 根据接收人、状态和类型分页获取信息[终端]
	 * @param receivePerson
	 * @param indexPage
	 * @param pageSize
	 * @return
	 */
	public List<BizMessage> getMessageToPageByReceivePersonAndStateAndType(final String receivePerson,final int state,final long typeId,final int indexPage,final int pageSize);
	
	/**
	 * 根据接收人获取未读信息（包括响铃）
	 * @param receivePerson
	 * @return
	 */
	public List<BizMessage> getNoReadMessageByReceivePerson(String receivePerson);
	
	/**
	 * 根据接收人获取未读信息（包括响铃）[终端]
	 * @param receivePerson
	 * @return
	 */
	public List<BizMessage> getNoReadMessageByReceivePersonForMobile(String receivePerson);
	
	/**
	 * 根据接收人获取响铃信息
	 * @param receivePerson
	 * @return
	 */
	public List<BizMessage> getStateMessageByReceivePerson(int state,String receivePerson);
	
	/**
	 * 根据接收人和类型获取消息
	 * @param state
	 * @param receivePerson
	 * @param msgTypeId
	 * @return
	 */
	public List<BizMessage> getStateMessageByReceivePersonAndMsgType(int state,String receivePerson,long msgTypeId);
	
	/**
	 * 根据id获取消息
	 * @param id
	 * @return
	 */
	public BizMessage getMessageById(String id);
	
	/**
	 * 根据接收人和单号获取消息
	 * @param receivePerson
	 * @param orderId
	 * @return
	 */
	public List<BizMessage> getMessageByReceivePersonAndOrderId(String receivePerson,String orderId);
	
	/**
	 * 根据标题获取未读和响铃信息
	 * @param title
	 * @return
	 */
	public List<BizMessage> getNoReadAndRingMessageByTitle(String title);
	
	//消息类型======================================================================================
	
	/**
	 * 根据类型获取消息类型对象
	 * @param typeKey
	 * @return
	 */
	public BizmsgMessageType getMessageTypeByTypeKey(String typeKey);
	
	/**
	 * 根据id获取消息类型对象
	 * @param typeKey
	 * @return
	 */
	public BizmsgMessageType getMessageTypeById(long id);
	
	/**
	 * 获取所有的消息类型
	 * @return
	 */
	public List<BizmsgMessageType> getAllMessageType();
	///---------------------新接口 2013-7-13
	/**
	 * 
	 * @description: 根据消息状态 功能类型获取消息列表
	 * @author：yuan.yw
	 * @param userId
	 * @param messageStateId
	 * 
	 * @param functionType
	 * @param messageType
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jul 12, 2013 10:30:10 AM
	 */
	public List<Map<String,Object>> getMessageListByStatusAndFunctionTypeAndMsgType(String userId, String messageStateId,String functionType,String messageType);

}
