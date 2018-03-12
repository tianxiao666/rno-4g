package com.iscreate.op.service.bizmsg;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.bizmsg.BizMessage;


public interface BizMessageService {

	/**
	 * 添加新消息
	 * @param bizMsg
	 * @return
	 */
	public void txAddBizMessageService(BizMessage bizMsg,String type);
	
	
	/**
	 * 根据接收人分页获取未读信息(包括响铃)
	 * @param receivePerson
	 * @param indexPage
	 * @param pageSize
	 * @return
	 */
	public List<BizMessage> getNoReadAndRingByPageService(String receivePerson);
	
	/**
	 * 获取接收人的消息总数
	 * @param receivePerson
	 * @return
	 */
	public int getNoReadMessageCountService(String receivePerson);
	
	/**
	 * 接收人是否需要响铃
	 * @param receivePerson
	 * @return
	 */
	public boolean getRingMessageByReceivePersonService(String receivePerson);
	
	/**
	 * 根据接收人和类型获取未读消息
	 * @param receivePerson
	 * @param type
	 * @return
	 */
	public List<BizMessage> getNoReadMessageByReceivePersonAndTypeService(String receivePerson,String type);
	
	/**
	 * 根据接收人和类型获取响铃消息
	 * @param receivePerson
	 * @param type
	 * @return
	 */
	public List<BizMessage> getRingMessageByReceivePersonAndTypeService(String receivePerson,String type);
	
	/**
	 * 根据接收人和类型获取已读消息
	 * @param receivePerson
	 * @param type
	 * @return
	 */
	public List<BizMessage> getHasReadMessageByReceivePersonAndTypeService(String receivePerson,String type);
	
	/**
	 * 加载消息盒子
	 * @param userId
	 * @return
	 */
	public Map<String,Object> loadBizMessageByUserIdService(String userId);
	
	/**
	 * 根据接收人和类型和状态获取消息
	 * @param userId
	 * @param type
	 * @param state "0":未读  "1":已读  "2":所有
	 * @return
	 */
	public Map<String,Object> loadBizMessageByUserIdAndTypeAndStateService(String userId,String type,String state);
	
	/**
	 * 根据Id修改消息为已读
	 * @param id
	 */
	public void updateBizMsgByIdService(String id);
	
	/**
	 * 根据接收人和单号修改消息为已读状态
	 * @param receivePerson
	 * @param orderId
	 */
	public void updateBizMsgToHasReadByReceivePersonAndOrderIdService(String receivePerson,String orderId);
	
	/**
	 * 根据id删除信息
	 * @param id
	 */
	public void deleteBizMessage(String id);
	
	/**
	 * 批量删除消息
	 * @param ids
	 */
	public void deleteBizMessageByIdsService(String ids);
	
	/**
	 * 获取未响铃数
	 * @param userId
	 */
	public void getNoReadBizMessageCountAjaxService(String userId);
	
	/**
	 * 根据用户Id获取响铃消息
	 * @param userId
	 */
	public void txGetRingBizMessageByUserIdAjaxService(String userId);
	
	/**
	 * 根据用户、状态和类型获取信息数量
	 * @param userId
	 * @param state
	 * @param type
	 * @return
	 */
	public int getBizMessageCountByStateAndType(String userId,int state,String type);
	
	/**
	 * 根据用户、状态和类型获取信息（分页）
	 * @param receivePerson
	 * @param state
	 * @param type
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<BizMessage> getBizMessageByStateAndTypeWithPage(String receivePerson,int state,String type,int pageIndex,int pageSize);
	
	/**
	 * 修改信息为已读
	 * @param bizMessage
	 */
	public void updateBizMessageToHasRead(BizMessage bizMessage);
	
	/**
	 * 根据标题获取未读和响铃信息
	 * @param title
	 * @return
	 */
	public List<BizMessage> getNoReadAndRingMessageByTitleService(String title);
	
	/**
	 * 根据标题集合修改信息为已读
	 * @param titleList
	 */
	public void txUpdateBizMessageToHasReadByTitleList(List<String> titleList);
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
	public int getBizMessageCountByStatusAndFunctionType(String userId, String messageStateId,String functionType);
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
	public List<Map<String,Object>> getBizMessageListByStatusAndFunctionTypeAndMsgType(String userId, String messageStateId,String functionType,String messageType);
}
