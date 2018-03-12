package com.iscreate.op.action.bizmsg;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;


public class BizMessageAction {

	private BizMessageService bizMessageService;
	
	private Map<String,Object> bizMsgMap;
	private String typeKey;   //类型
	private String msgState;  //状态
	private String msgId;
	private String ids;
	private String soundState;  //声音状态
	//AJAX控制声音的状态
	private String soundAjaxState;
	//由于打开消息盒子页面时，需要关闭声音，通过这个变量标识加载loadBizMessageAction时，是否是打开页面的状态
	private String openPageState;
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * 加载消息盒子内容层
	 * @return
	 */
	public String loadBizMessageAction() {
		log.info("进入loadBizMessageAction方法");
		//从session获取user
		this.soundState = "on";
		HttpSession session = ServletActionContext.getRequest().getSession();
		if(session.getAttribute("soundState") != null) {
			this.soundState = session.getAttribute("soundState").toString();
		} else {
			this.soundState = "on";
		}
		//先忽略，页面是否打开问题，都响
		this.openPageState = "No";
		String userId = (String)session.getAttribute("userId");
		this.bizMsgMap = this.bizMessageService.loadBizMessageByUserIdService(userId);
		if(this.bizMsgMap==null || this.bizMsgMap.size()==0){
			log.info("加载消息盒子的内容为空");
		}
		log.info("成功执行了loadBizMessageAction方法，该方法实现了“加载消息盒子的内容层”");
		log.info("退出loadBizMessageAction方法，返回String为success");
		return "success";
	}
	
	/**
	 * 修改消息为已读
	 */
	public void updateMsgToHasReadAction(){
		log.info("进入updateMsgToHasReadAction方法");
		this.bizMessageService.updateBizMsgByIdService(msgId);
		log.info("成功执行了updateMsgToHasReadAction方法，该方法实现了“修改消息为已读的功能”");
		log.info("退出updateMsgToHasReadAction方法，返回void");
	}
	
	/**
	 * 根据类型和状态获取消息
	 * @return
	 */
	public void loadBizMessageByTypeForAjaxAction(){
		log.info("进入loadBizMessageByTypeForAjaxAction方法");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		//从session获取user
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId = (String)session.getAttribute("userId");
		this.bizMsgMap = this.bizMessageService.loadBizMessageByUserIdAndTypeAndStateService(userId, this.typeKey, this.msgState);
		log.info("");
		String result = gson.toJson(this.bizMsgMap);
		try {
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("loadBizMessageByTypeForAjaxAction方法运行失败!");
			throw new UserDefinedException("返回时出错");
		}
		log.info("成功执行了loadBizMessageByTypeForAjaxAction方法，该方法实现了“根据消息类型、消息状态和登陆人获取消息”");
		log.info("退出loadBizMessageByTypeForAjaxAction方法，返回void");
	}
	
	/**
	 * 批量删除消息
	 */
	public void deleteBizMessageAction(){
		log.info("进入deleteBizMessageAction方法");
		this.bizMessageService.deleteBizMessageByIdsService(this.ids);
		log.info("成功执行了deleteBizMessageAction方法，该方法实现了“根据消息盒子的ID，批量删除消息的功能”");
		log.info("退出deleteBizMessageAction方法，返回void");
	}
	
	/**
	 * 根据用户获取响铃消息
	 */
	public void getNoReadBizMessageCountAction(){
		//从session获取user
		log.info("进入getNoReadBizMessageCountAction方法");
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId = (String)session.getAttribute("userId");
		this.bizMessageService.getNoReadBizMessageCountAjaxService(userId);
		log.info("成功执行了getNoReadBizMessageCountAction方法，该方法实现了“根据用户获取响铃消息的功能”");
		log.info("退出getNoReadBizMessageCountAction方法，返回void");
	}
	
	/**
	 * 根据用户获取响铃数并修改响铃状态为未读
	 */
	public void getNoReadBizMessageCountAndUpdateBizMessageAjaxAction(){
		//从session获取user
		log.info("进入getNoReadBizMessageCountAndUpdateBizMessageAjaxAction方法");
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId = (String)session.getAttribute("userId");
		this.bizMessageService.txGetRingBizMessageByUserIdAjaxService(userId);
		log.info("成功执行了getNoReadBizMessageCountAndUpdateBizMessageAjaxAction方法，该方法实现了“根据用户获取响铃数并修改响铃状态为未读的功能”");
		log.info("退出getNoReadBizMessageCountAndUpdateBizMessageAjaxAction方法，返回void");
	}
	
	/**
	 * 根据图片，更改声音控制的session
	 */
	public void updateSoundSessionAction() {
		log.info("进入updateSoundSessionAction方法");
		try {
			if(this.soundAjaxState != null && !"".equals(this.soundAjaxState)) {
				HttpSession session = ServletActionContext.getRequest().getSession();
				if("on".equals(this.soundAjaxState)) {
					session.setAttribute("soundState", "on");
				} else {
					session.setAttribute("soundState", "off");
				}
			}
		} catch (RuntimeException e) {
			log.error("updateSoundSessionAction方法运行失败");
		}
		log.info("成功执行了updateSoundSessionAction方法，该方法实现了“根据图片，更改声音控制的session的功能”");
		log.info("退出updateSoundSessionAction方法，返回void");
	}
	
	public BizMessageService getBizMessageService() {
		return bizMessageService;
	}

	public void setBizMessageService(BizMessageService bizMessageService) {
		this.bizMessageService = bizMessageService;
	}

	public String getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}

	public Map<String, Object> getBizMsgMap() {
		return bizMsgMap;
	}

	public void setBizMsgMap(Map<String, Object> bizMsgMap) {
		this.bizMsgMap = bizMsgMap;
	}

	public String getMsgState() {
		return msgState;
	}

	public void setMsgState(String msgState) {
		this.msgState = msgState;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getSoundState() {
		return soundState;
	}

	public void setSoundState(String soundState) {
		this.soundState = soundState;
	}

	public String getSoundAjaxState() {
		return soundAjaxState;
	}

	public void setSoundAjaxState(String soundAjaxState) {
		this.soundAjaxState = soundAjaxState;
	}

	public String getOpenPageState() {
		return openPageState;
	}

	public void setOpenPageState(String openPageState) {
		this.openPageState = openPageState;
	}
	
}
