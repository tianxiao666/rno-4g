package com.iscreate.op.service.workmanage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.pojo.bizmsg.BizMessage;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.op.service.workmanage.util.DataSelectUtil;
import com.iscreate.op.service.workmanage.util.InterfaceConfLoadUtil;
import com.iscreate.plat.tools.TimeFormatHelper;


public class WorkManageTimer{

	
	static Logger logger = Logger.getLogger(WorkManageTimer.class);
	
	private DataSelectUtil dataSelectUtil;
	private BizMessageService bizMessageService;
	private WorkManageService workManageService;
	private HibernateTemplate hibernateTemplate;
	
	static Integer bizMsgOverTime=null;
	static boolean isInitData=false;
	
	
	//工单与任务单快超时集合缓存
	public static List<Map<String,Object>> orderCache=new ArrayList<Map<String,Object>>();
	
	//超时工单/任务单集合缓存
	public static List<Map<String,Object>> overTimeOrderCache=new ArrayList<Map<String,Object>>();
	
	
	//已发送消息的工单记录
	public List<String> isSendWorkOrderCache=new ArrayList<String>();
	
	//已发送消息的任务单记录
	public List<String> isSendTaskOrderCache=new ArrayList<String>();
	
	
	
	
	public void setDataSelectUtil(DataSelectUtil dataSelectUtil) {
		this.dataSelectUtil = dataSelectUtil;
	}
	
	public void setBizMessageService(BizMessageService bizMessageService) {
		this.bizMessageService = bizMessageService;
	}


	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void execute() {
		/**
		 * 
		
		//------------ test begin --------------------
		
		Map<String,Object> map1=new HashMap<String,Object>();
		map1.put("woId", "123");
		map1.put("isWorkOrder", "1");
		Map<String,Object> map2=new HashMap<String,Object>();
		map2.put("woId", "234");
		map2.put("isWorkOrder", "1");
		
//		boolean wo_addRes1=WorkManageTimer.addBizOrderToOrderListMap(orderCache, map1);
//		System.out.println("wo_addRes1=="+wo_addRes1);
		
//		boolean wo_addRes2=WorkManageTimer.addBizOrderToOrderListMap(orderCache, map2);
//		System.out.println("wo_addRes2=="+wo_addRes2);
		
		
//		String sub_woId="234";
//		boolean wo_subRes1=WorkManageTimer.substractWorkOrderFromOrderListMap(orderCache, sub_woId);
//		System.out.println("wo_subRes1=="+wo_subRes1);
		
		
//		String sub_woId2="123";
//		boolean wo_subRes2=WorkManageTimer.substractWorkOrderFromOrderListMap(orderCache, sub_woId2);
//		System.out.println("wo_subRes2=="+wo_subRes2);
		
		
		
		//任务单测试
		Map<String,Object> map3=new HashMap<String,Object>();
		map3.put("toId", "112233");
		map3.put("woId", "123");
		map3.put("isWorkOrder", "0");
		
		Map<String,Object> map4=new HashMap<String,Object>();
		map4.put("toId", "223344");
		map4.put("woId", "234");
		map4.put("isWorkOrder", "0");
		
//		boolean to_addRes1=WorkManageTimer.addBizOrderToOrderListMap(orderCache, map3);
//		System.out.println("to_addRes1=="+to_addRes1);
		
		
//		boolean to_addRes2=WorkManageTimer.addBizOrderToOrderListMap(orderCache, map4);
//		System.out.println("to_addRes2=="+to_addRes2);
		
		
//		String sub_toId="112233";
//		boolean to_subRes1=WorkManageTimer.substractTaskOrderFromOrderListMap(orderCache, sub_toId);
//		System.out.println("to_subRes1=="+to_subRes1);
		
		
//		String sub_toId2="223344";
//		boolean to_subRes2=WorkManageTimer.substractTaskOrderFromOrderListMap(orderCache, sub_toId2);
//		System.out.println("wo_subRes2=="+to_subRes2);
		
		//------------ test end --------------------
		
		*/
		
		
		//初始化数据库到缓存
		if(!isInitData){
			initDataFromDataBaseToCache();
			isInitData=true;
		}
		
		sendBizMsg();		//发送快超时信息到消息盒子
		
		sendOverTimeBizMsg();	//发送超时信息到消息盒子
	}
	
	
	/**
	 * 初始化数据
	 */
	private void initDataFromDataBaseToCache(){
		logger.info("in method initDataFromDataBaseToCache()");
		Date nowTime=new Date();
		String str_nowTime=TimeFormatHelper.getTimeFormatBySecond(nowTime);
		
		//获取未超时的抢修工单信息
//		String sql="select * from view_workmanage_urgentrepair_workorder where status<>"+WorkManageConstant.WORKORDER_END+" and requireCompleteTime is not null and (isSendWillOverTime<>1 or isSendWillOverTime is null ) and requireCompleteTime>str_to_date('"+str_nowTime+"','%Y-%m-%d %H:%i:%s') order by createTime desc";
		String sql="select * from V_WM_URGENTREPAIR_WORKORDER where \"status\"<>"+WorkManageConstant.WORKORDER_END+" and \"requireCompleteTime\" is not null and (\"isSendWillOverTime\"<>1 or \"isSendWillOverTime\" is null ) and \"requireCompleteTime\">to_date('"+str_nowTime+"','yyyy-MM-dd HH24:mi:ss') order by \"createTime\" desc";
		
		
		List<Map> list=dataSelectUtil.selectDataWithCondition(sql, null);
		if(list!=null && !list.isEmpty()){
			for(Map workOrderMap:list){
				workOrderMap.put("isWorkOrder", "1");
				WorkManageTimer.addBizOrderToOrderListMap(orderCache, workOrderMap);
			}
		}
		
		//获取未超时的抢修任务单信息
//		sql="select * from view_workmanage_urgentrepair_taskorder where status<>"+WorkManageConstant.TASKORDER_CLOSED+" and requireCompleteTime is not null and (isSendWillOverTime<>1 or isSendWillOverTime is null ) and requireCompleteTime>str_to_date('"+str_nowTime+"','%Y-%m-%d %H:%i:%s') order by assignTime desc";
		sql="select * from V_WM_URGENTREPAIR_TASKORDER where \"status\"<>"+WorkManageConstant.TASKORDER_CLOSED+" and \"requireCompleteTime\" is not null and (\"isSendWillOverTime\"<>1 or \"isSendWillOverTime\" is null ) and \"requireCompleteTime\">to_date('"+str_nowTime+"','yyyy-MM-dd HH24:mi:ss') order by \"assignTime\" desc";
		list=dataSelectUtil.selectDataWithCondition(sql, null);
		if(list!=null && !list.isEmpty()){
			for(Map taskOrderMap:list){
				taskOrderMap.put("isWorkOrder", "0");
				WorkManageTimer.addBizOrderToOrderListMap(orderCache, taskOrderMap);
			}
		}
		
		
//		//获取用车时间未超时的车辆调度单信息
//		sql="select t1.*,t2.planReturnCarTime from view_workmanage_cardispatch_workorder t1,cardispatch_workorder t2 where t1.woId=t2.woId and t1.status<>"+WorkManageConstant.WORKORDER_END+" and t1.planUseCarTime is not null and t1.planUseCarTime>str_to_date('"+str_nowTime+"','%Y-%m-%d %H:%i:%s') order by t1.planUseCarTime desc";
//		list=dataSelectUtil.selectDataWithCondition(sql, null);
//		if(list!=null && !list.isEmpty()){
//			for(Map workOrderMap:list){
//				workOrderMap.put("isWorkOrder", "1");
//				WorkManageTimer.addBizOrderToOrderListMap(orderCache, workOrderMap);
//			}
//		}
//		
//		//获取用车时间已经超时的车辆调度单信息
//		sql="select t1.*,t2.planReturnCarTime from view_workmanage_cardispatch_workorder t1,cardispatch_workorder t2 where t1.woId=t2.woId and t1.status<>"+WorkManageConstant.WORKORDER_END+" and t1.planUseCarTime is not null and t1.planUseCarTime<str_to_date('"+str_nowTime+"','%Y-%m-%d %H:%i:%s') order by t1.planUseCarTime desc";
//		list=dataSelectUtil.selectDataWithCondition(sql, null);
//		if(list!=null && !list.isEmpty()){
//			for(Map workOrderMap:list){
//				workOrderMap.put("isWorkOrder", "1");
//				WorkManageTimer.addBizOrderToOrderListMap(overTimeOrderCache, workOrderMap);
//			}
//		}
		
	}
	
	
	
	/**
	 * 发送快超时消息到消息盒子
	 */
	private void sendBizMsg(){
		try {
			if(bizMsgOverTime==null){
				//读取快超时时间定义
				bizMsgOverTime=InterfaceConfLoadUtil.getBizMsgOverTimeConfig(WorkManageConstant.BIZMSGOVERTIME_PATH);
			}else{
				logger.info("不用读取快超时时间定义");
			}
			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, bizMsgOverTime);
			String willOverTime = TimeFormatHelper.getTimeFormatBySecond(calendar.getTime());	//快超时时间
			//logger.info("bizMsgOverTime======"+bizMsgOverTime);
			
			List<Map<String,Object>> removeList=new ArrayList<Map<String,Object>>();
			
			synchronized (orderCache) {
				if(orderCache!=null && !orderCache.isEmpty()){
					//遍历缓存中的工单/任务单集合
					for(Map<String,Object> orderMap:orderCache){
						if(orderMap!=null && !orderMap.isEmpty()){
							//logger.info("orderMap=="+orderMap);
							String isWorkOrder=orderMap.get("isWorkOrder")==null?"":orderMap.get("isWorkOrder").toString();
							String currentHandler=orderMap.get("currentHandler")==null?"":orderMap.get("currentHandler").toString();
							String woType=orderMap.get("woType")==null?"":orderMap.get("woType").toString();
							
							if(WorkManageConstant.WORKORDER_TYPE_URGENTREPAIR.equals(woType)){
								String requireCompleteTime=orderMap.get("requireCompleteTime")==null?"":orderMap.get("requireCompleteTime").toString();
								if("1".equals(isWorkOrder)){	//是工单
									String woId=orderMap.get("woId")==null?"":orderMap.get("woId").toString();
									String creatorOrgId=orderMap.get("creatorOrgId")==null?"":orderMap.get("creatorOrgId").toString();
									//如果符合快超时的规则，发送消息
									if(requireCompleteTime!=null && !"".equals(requireCompleteTime)){
										boolean isWillOverTime=compareTime(requireCompleteTime, willOverTime);
										if(isWillOverTime){	//快超时了
											BizMessage bizMsg=new BizMessage();
											bizMsg.setContent("【截止时间："+requireCompleteTime+"】快超时");
											bizMsg.setSendTime(new Date());
											bizMsg.setTitle(woId);
											bizMsg.setReceivePerson(currentHandler);
											
											//获取工单业务类型、工单跳转url、工单终端跳转url
											Map<String,String> workOrderMap=this.workManageService.getWorkOrderForShow(woId);
											String formUrl=workOrderMap.get("formUrl");
											String terminalFormUrl=workOrderMap.get("terminalFormUrl");
											
											formUrl=formUrl+"?WOID="+woId+"&TOID=&orgId="+creatorOrgId;
											terminalFormUrl=terminalFormUrl+"?WOID="+woId+"&TOID=&orgId="+creatorOrgId;
											String functionType = getFunctionType(formUrl);
											bizMsg.setFunctionType(functionType);
											bizMsg.setLink(formUrl);
											bizMsg.setLinkForMobile(terminalFormUrl);
											bizMsg.setType(woType);
											bizMsg.setWoId(woId);
											this.bizMessageService.txAddBizMessageService(bizMsg,"quickOverTime");
											logger.info("工单号【"+woId+"】即将超时了");
											
											isSendWorkOrderCache.add(woId);	//记录已发送消息的工单
											
											//对已发送超时消息的工单/任务单，从缓存中清除
											substractWorkOrderFromOrderListMap(orderCache, woId,removeList);
											
											//把已发送超时消息的工单/任务，添加到缓存中
											WorkManageTimer.addBizOrderToOrderListMap(overTimeOrderCache, orderMap);
											
										}
									}
									
								}else{
									String toId=orderMap.get("toId")==null?"":orderMap.get("toId").toString();
									String woId=orderMap.get("woId")==null?"":orderMap.get("woId").toString();
									String assignerOrgId=orderMap.get("assignerOrgId")==null?"":orderMap.get("assignerOrgId").toString();
									//如果符合快超时的规则，发送消息
									if(requireCompleteTime!=null && !"".equals(requireCompleteTime)){
										boolean isWillOverTime=compareTime(requireCompleteTime, willOverTime);
										if(isWillOverTime){	//快超时了
											BizMessage bizMsg=new BizMessage();
											bizMsg.setContent("【截止时间："+requireCompleteTime+"】快超时");
											bizMsg.setSendTime(new Date());
											bizMsg.setTitle(toId);
											bizMsg.setReceivePerson(currentHandler);
											
											//获取工单业务类型、工单跳转url、工单终端跳转url
											Map<String,String> taskOrderMap=this.workManageService.getTaskOrderForShow(toId);
											String formUrl=taskOrderMap.get("formUrl");
											String terminalFormUrl=taskOrderMap.get("terminalFormUrl");
											
											
											formUrl=formUrl+"?WOID="+woId+"&TOID="+toId+"&orgId="+assignerOrgId;
											terminalFormUrl=terminalFormUrl+"?WOID="+woId+"&TOID="+toId+"&orgId="+assignerOrgId;
											
											String toType=taskOrderMap.get("toType");
											String functionType = getFunctionType(formUrl);
											bizMsg.setFunctionType(functionType);
											bizMsg.setLink(formUrl);
											bizMsg.setLinkForMobile(terminalFormUrl);
											bizMsg.setType(toType);
											bizMsg.setWoId(woId);
											bizMsg.setToId(toId);
											this.bizMessageService.txAddBizMessageService(bizMsg,"quickOverTime");
											logger.info("任务单号【"+toId+"】即将超时了");
											
											isSendTaskOrderCache.add(toId);	//记录已发送消息的任务单
											
											//对已发送超时消息的工单/任务单，从缓存中清除
											substractTaskOrderFromOrderListMap(orderCache, toId,removeList);
											
											//把已发送超时消息的工单/任务，添加到缓存中
											WorkManageTimer.addBizOrderToOrderListMap(overTimeOrderCache, orderMap);
										}
									}
								}
							}else if(WorkManageConstant.WORKORDER_TYPE_CARDISPATCH.equals(woType)){
								String woId=orderMap.get("woId")==null?"":orderMap.get("woId").toString();
								String planUseCarTime=orderMap.get("planUseCarTime")==null?"":orderMap.get("planUseCarTime").toString();
								String planReturnCarTime=orderMap.get("planReturnCarTime")==null?"":orderMap.get("planReturnCarTime").toString();
								
								if(planUseCarTime!=null && !"".equals(planUseCarTime)){
									//符合用车快超时
									boolean isWillOverTime=compareTime(planUseCarTime, willOverTime);
									if(isWillOverTime){	//快超时了
										BizMessage bizMsg=new BizMessage();
										bizMsg.setContent("【用车时间："+planUseCarTime+"】快超时");
										bizMsg.setSendTime(new Date());
										bizMsg.setTitle(woId);
										bizMsg.setReceivePerson(currentHandler);
										
										//获取工单业务类型、工单跳转url、工单终端跳转url
										Map<String,String> workOrderMap=this.workManageService.getCarDispatchWorkOrderForShow(woId);
										String formUrl=workOrderMap.get("formUrl");
										String terminalFormUrl=workOrderMap.get("terminalFormUrl");
										
										formUrl=formUrl+"?WOID="+woId;
										terminalFormUrl=terminalFormUrl+"?WOID="+woId;
										String functionType = getFunctionType(formUrl);
										bizMsg.setFunctionType(functionType);
										bizMsg.setLink(formUrl);
										bizMsg.setLinkForMobile(terminalFormUrl);
										bizMsg.setType(woType);
										bizMsg.setWoId(woId);
										this.bizMessageService.txAddBizMessageService(bizMsg,"quickOverTime");
										logger.info("工单号【"+woId+"】即将超时了");
										
										//对已发送超时消息的工单/任务单，从缓存中清除
										substractWorkOrderFromOrderListMap(orderCache, woId,removeList);
										
										//把已发送超时消息的工单/任务，添加到缓存中
										WorkManageTimer.addBizOrderToOrderListMap(overTimeOrderCache, orderMap);
									}
								}
								
								if(planReturnCarTime!=null && !"".equals(planReturnCarTime)){
									//符合还车快超时
									boolean isWillOverTime=compareTime(planReturnCarTime, willOverTime);
									if(isWillOverTime){	//快超时了
										BizMessage bizMsg=new BizMessage();
										bizMsg.setContent("【还车时间："+planReturnCarTime+"】快超时");
										bizMsg.setSendTime(new Date());
										bizMsg.setTitle(woId);
										bizMsg.setReceivePerson(currentHandler);
										
										//获取工单业务类型、工单跳转url、工单终端跳转url
										Map<String,String> workOrderMap=this.workManageService.getCarDispatchWorkOrderForShow(woId);
										String formUrl=workOrderMap.get("formUrl");
										String terminalFormUrl=workOrderMap.get("terminalFormUrl");
										
										formUrl=formUrl+"?WOID="+woId;
										terminalFormUrl=terminalFormUrl+"?WOID="+woId;
										String functionType = getFunctionType(formUrl);
										bizMsg.setFunctionType(functionType);
										bizMsg.setLink(formUrl);
										bizMsg.setLinkForMobile(terminalFormUrl);
										bizMsg.setType(woType);
										bizMsg.setWoId(woId);
										this.bizMessageService.txAddBizMessageService(bizMsg,"quickOverTime");
										logger.info("工单号【"+woId+"】即将超时了");
										
										//对已发送超时消息的工单/任务单，从缓存中清除
										substractWorkOrderFromOrderListMap(orderCache, woId,removeList);
										
										//把已发送超时消息的工单/任务，添加到缓存中
										WorkManageTimer.addBizOrderToOrderListMap(overTimeOrderCache, orderMap);
									}
								}
								
							}
						}
					}
				}
				
				//System.out.println("remove前orderCache大小==="+orderCache.size());
				orderCache.removeAll(removeList);	//清除缓存中已经发送消息的order
				//System.out.println("remove后orderCache大小==="+orderCache.size());
				
				//System.out.println("isSendWorkOrderCache大小=="+isSendWorkOrderCache.size());
				modifyOrderBizMsgStatus(true,isSendWorkOrderCache);	//更新工单发送消息的状态
				
				//System.out.println("isSendTaskOrderCache=="+isSendTaskOrderCache.size());
				modifyOrderBizMsgStatus(false,isSendTaskOrderCache);	//更新任务单发送消息的状态
				
				logger.info("----------------- end ----------------");
				removeList.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 发送超时消息到消息盒子
	 */
	private void sendOverTimeBizMsg(){
		
		List<Map<String,Object>> removeList=new ArrayList<Map<String,Object>>();
		
		synchronized (overTimeOrderCache) {
			if(overTimeOrderCache!=null && !overTimeOrderCache.isEmpty()){
//				System.out.println("轮询超时工单/任务单集合缓存-------begin-------");
				String currentTime = TimeFormatHelper.getTimeFormatBySecond(new Date());	//当前时间
				for(Map<String,Object> orderMap:overTimeOrderCache){
					String isWorkOrder=orderMap.get("isWorkOrder")==null?"":orderMap.get("isWorkOrder").toString();
					String currentHandler=orderMap.get("currentHandler")==null?"":orderMap.get("currentHandler").toString();
					String woType=orderMap.get("woType")==null?"":orderMap.get("woType").toString();
					
					if(WorkManageConstant.WORKORDER_TYPE_URGENTREPAIR.equals(woType)){
						String requireCompleteTime=orderMap.get("requireCompleteTime")==null?"":orderMap.get("requireCompleteTime").toString();
						if("1".equals(isWorkOrder)){	//是工单
							String woId=orderMap.get("woId")==null?"":orderMap.get("woId").toString();
							//如果符合超时的规则，发送消息
							if(requireCompleteTime!=null && !"".equals(requireCompleteTime)){
								boolean isOverTime=compareTime(requireCompleteTime, currentTime);
								if(isOverTime){
									BizMessage bizMsg=new BizMessage();
									bizMsg.setContent("【截止时间："+requireCompleteTime+"】已超时");
									bizMsg.setSendTime(new Date());
									bizMsg.setTitle(woId);
									bizMsg.setReceivePerson(currentHandler);
									
									//获取工单业务类型、工单跳转url、工单终端跳转url
									Map<String,String> workOrderMap=this.workManageService.getWorkOrderForShow(woId);
									String formUrl=workOrderMap.get("formUrl");
									String terminalFormUrl=workOrderMap.get("terminalFormUrl");
									
									formUrl=formUrl+"?WOID="+woId;
									terminalFormUrl=terminalFormUrl+"?WOID="+woId+"";
//									String woType=workOrderMap.get("woType");
									String functionType = getFunctionType(formUrl);
									bizMsg.setFunctionType(functionType);
									bizMsg.setLink(formUrl);
									bizMsg.setLinkForMobile(terminalFormUrl);
									bizMsg.setType(woType);
									
									this.bizMessageService.txAddBizMessageService(bizMsg,"overTime");
									logger.info("工单号【"+woId+"】已经超时了");
									
									isSendWorkOrderCache.add(woId);	//记录已发送消息的工单
									
									//对已发送超时消息的工单/任务单，从缓存中清除
									substractWorkOrderFromOrderListMap(overTimeOrderCache, woId,removeList);
								}
							}
						}else{
							String toId=orderMap.get("toId")==null?"":orderMap.get("toId").toString();
							String woId=orderMap.get("woId")==null?"":orderMap.get("woId").toString();
							
							if(requireCompleteTime!=null && !"".equals(requireCompleteTime)){
								boolean isOverTime=compareTime(requireCompleteTime, currentTime);
								if(isOverTime){
									BizMessage bizMsg=new BizMessage();
									bizMsg.setContent("【截止时间："+requireCompleteTime+"】已超时");
									bizMsg.setSendTime(new Date());
									bizMsg.setTitle(toId);
									bizMsg.setReceivePerson(currentHandler);
									
									//获取工单业务类型、工单跳转url、工单终端跳转url
									Map<String,String> taskOrderMap=this.workManageService.getTaskOrderForShow(toId);
									String formUrl=taskOrderMap.get("formUrl");
									String terminalFormUrl=taskOrderMap.get("terminalFormUrl");
									
									formUrl=formUrl+"?WOID="+woId+"&TOID="+toId;
									terminalFormUrl=terminalFormUrl+"?WOID="+woId+"&TOID="+toId;
									
									String toType=taskOrderMap.get("toType");
									String functionType = getFunctionType(formUrl);
									bizMsg.setFunctionType(functionType);
									bizMsg.setLink(formUrl);
									bizMsg.setLinkForMobile(terminalFormUrl);
									bizMsg.setType(toType);
									bizMsg.setWoId(woId);
									bizMsg.setToId(toId);
									this.bizMessageService.txAddBizMessageService(bizMsg,"overTime");
									logger.info("任务单号【"+toId+"】已经超时了");
									
									isSendTaskOrderCache.add(toId);	//记录已发送消息的任务单
									
									//对已发送超时消息的工单/任务单，从缓存中清除
									substractTaskOrderFromOrderListMap(overTimeOrderCache, toId,removeList);
								}
							}
						}
					}else if(WorkManageConstant.WORKORDER_TYPE_CARDISPATCH.equals(woType)){
						String woId=orderMap.get("woId")==null?"":orderMap.get("woId").toString();
						String planUseCarTime=orderMap.get("planUseCarTime")==null?"":orderMap.get("planUseCarTime").toString();
						String planReturnCarTime=orderMap.get("planReturnCarTime")==null?"":orderMap.get("planReturnCarTime").toString();
						
						//maybe have a bug:when sended 用车/还车‘s willOverTime bizMessage,the record will remove from the list,so can't send overTime bizMessage
						
						if(planUseCarTime!=null && !"".equals(planUseCarTime)){
							//符合用车超时
							boolean isOverTime=compareTime(planUseCarTime, currentTime);
							if(isOverTime){
								BizMessage bizMsg=new BizMessage();
								bizMsg.setContent("【用车时间："+planUseCarTime+"】已超时");
								bizMsg.setSendTime(new Date());
								bizMsg.setTitle(woId);
								bizMsg.setReceivePerson(currentHandler);
								
								//获取工单业务类型、工单跳转url、工单终端跳转url
								Map<String,String> workOrderMap=this.workManageService.getCarDispatchWorkOrderForShow(woId);
								String formUrl=workOrderMap.get("formUrl");
								String terminalFormUrl=workOrderMap.get("terminalFormUrl");
								
								formUrl=formUrl+"?WOID="+woId;
								terminalFormUrl=terminalFormUrl+"?WOID="+woId;
								String functionType = getFunctionType(formUrl);
								bizMsg.setFunctionType(functionType);
								bizMsg.setLink(formUrl);
								bizMsg.setLinkForMobile(terminalFormUrl);
								bizMsg.setType(woType);
								
								this.bizMessageService.txAddBizMessageService(bizMsg,"overTime");
								logger.info("工单号【"+woId+"】已经超时了");
								
								//对已发送超时消息的工单/任务单，从缓存中清除
								substractWorkOrderFromOrderListMap(overTimeOrderCache, woId,removeList);
							}
						}
						
						if(planReturnCarTime!=null && !"".equals(planReturnCarTime)){
							//符合还车超时
							boolean isOverTime=compareTime(planReturnCarTime, currentTime);
							if(isOverTime){	//快超时了
								BizMessage bizMsg=new BizMessage();
								bizMsg.setContent("【还车时间："+planReturnCarTime+"】已超时");
								bizMsg.setSendTime(new Date());
								bizMsg.setTitle(woId);
								bizMsg.setReceivePerson(currentHandler);
								
								//获取工单业务类型、工单跳转url、工单终端跳转url
								Map<String,String> workOrderMap=this.workManageService.getCarDispatchWorkOrderForShow(woId);
								String formUrl=workOrderMap.get("formUrl");
								String terminalFormUrl=workOrderMap.get("terminalFormUrl");
								
								formUrl=formUrl+"?WOID="+woId;
								terminalFormUrl=terminalFormUrl+"?WOID="+woId;
								String functionType = getFunctionType(formUrl);
								bizMsg.setFunctionType(functionType);
								bizMsg.setLink(formUrl);
								bizMsg.setLinkForMobile(terminalFormUrl);
								bizMsg.setType(woType);
								
								this.bizMessageService.txAddBizMessageService(bizMsg,"overTime");
								logger.info("工单号【"+woId+"】已经超时了");
								
								//对已发送超时消息的工单/任务单，从缓存中清除
								substractWorkOrderFromOrderListMap(overTimeOrderCache, woId,removeList);
							}
						}
					}
				}
			}
			overTimeOrderCache.removeAll(removeList);	//清除缓存中已经发送消息的order
			
			modifyOrderBizMsgOverTimeStatus(true,isSendWorkOrderCache);	//更新工单发送消息的状态
			modifyOrderBizMsgOverTimeStatus(false,isSendTaskOrderCache);	//更新任务单发送消息的状态
		}
	}
	
	private String getFunctionType(String url){
		if(url != null && !url.equals("")){
			if(url.indexOf("loadUrgentRepairWorkOrderPageAction") >= 0){
				return "UrgentRepairWorkOrder";
			}else if(url.indexOf("loadUrgentRepairSenceTaskOrderPageAction") >= 0){
				return "UrgentRepairSenceTaskOrder";
			}else if(url.indexOf("loadUrgentRepairTechSupportTaskOrderPageAction") >= 0){
				return "UrgentRepairTechSupportTaskOrder";
			}else if(url.indexOf("enterCardispatchWorkord") >= 0){
				return "CardispatchWorkOrder";
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	/**
	 * 为集合添加工单/任务记录
	 * @param orderListMap
	 * @param targetMap
	 * @return
	 */
	public static synchronized boolean addBizOrderToOrderListMap(List<Map<String,Object>> orderListMap,Map<String,Object> targetMap){
		try {
			if(orderListMap!=null && !orderListMap.isEmpty()){
				boolean isFind=false;
				for(Map<String,Object> orderMap:orderListMap){
					String isWorkOrder=targetMap.get("isWorkOrder")==null?"":targetMap.get("isWorkOrder").toString();
					if("1".equals(isWorkOrder)){	//是工单
						String targetWoId=targetMap.get("woId")==null?"":targetMap.get("woId").toString();
						String tempWoId=orderMap.get("woId")==null?"":orderMap.get("woId").toString();
						if(tempWoId.equals(targetWoId)){
							isFind=true;
							logger.info("工单id【"+targetWoId+"】已经存在集合中");
							break;
						}
					}else{
						String targetToId=targetMap.get("toId")==null?"":targetMap.get("toId").toString();
						String tempToId=orderMap.get("toId")==null?"":orderMap.get("toId").toString();
						if(tempToId.equals(targetToId)){
							isFind=true;
							logger.info("任务单id【"+targetToId+"】已经存在集合中");
							break;
						}
					}
				}
				if(!isFind){
					orderListMap.add(targetMap);
					return true;
				}
			}else if(orderListMap!=null){
				orderListMap.add(targetMap);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 从集合减去工单记录
	 * @param orderListMap
	 * @param woId
	 * @return
	 */
	public static synchronized boolean substractWorkOrderFromOrderListMap(List<Map<String,Object>> orderListMap,String woId,List<Map<String,Object>> removeList){
		try {
			if(orderListMap!=null && !orderListMap.isEmpty()){
				for(Map<String,Object> orderMap:orderListMap){
					String isWorkOrder=orderMap.get("isWorkOrder")==null?"":orderMap.get("isWorkOrder").toString();
					if("1".equals(isWorkOrder)){	//是工单
						String tempWoId=orderMap.get("woId")==null?"":orderMap.get("woId").toString();
						if(tempWoId.equals(woId)){
							//orderListMap.remove(orderMap);
							removeList.add(orderMap);
							logger.info("删除工单id【"+tempWoId+"】在集合中的记录");
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 从集合减去任务单记录
	 * @param orderListMap
	 * @param toId
	 * @return
	 */
	public static synchronized boolean substractTaskOrderFromOrderListMap(List<Map<String,Object>> orderListMap,String toId,List<Map<String,Object>> removeList){
		try {
			if(orderListMap!=null && !orderListMap.isEmpty()){
				for(Map<String,Object> orderMap:orderListMap){
					String isWorkOrder=orderMap.get("isWorkOrder")==null?"":orderMap.get("isWorkOrder").toString();
					if("0".equals(isWorkOrder)){	//是任务单
						String tempToId=orderMap.get("toId")==null?"":orderMap.get("toId").toString();
						if(tempToId.equals(toId)){
							//orderListMap.remove(orderMap);
							removeList.add(orderMap);
							logger.info("删除任务单id【"+tempToId+"】在集合中的记录");
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	
//	/**
//	 * 为集合添加任务单记录
//	 * @param orderListMap
//	 * @param targetMap
//	 * @return
//	 */
//	@Deprecated
//	public static synchronized boolean addTaskOrderToOrderListMap(List<Map<String,Object>> orderListMap,Map<String,Object> targetMap){
//		try {
//			if(orderListMap!=null && !orderListMap.isEmpty()){
//				boolean isFind=false;
//				String targetToId=targetMap.get("toId")==null?"":targetMap.get("toId").toString();
//				for(Map<String,Object> taskOrderMap:orderListMap){
//					String tempToId=taskOrderMap.get("toId")==null?"":taskOrderMap.get("toId").toString();
//					if(tempToId.equals(targetToId)){
//						isFind=true;
//						logger.info("任务单id【"+targetToId+"】已经存在集合中");
//						break;
//					}
//				}
//				if(!isFind){
//					orderListMap.add(targetMap);
//					return true;
//				}
//			}else if(orderListMap!=null){
//				orderListMap.add(targetMap);
//				return true;
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
	
	
	
	/**
	 * 比较2个时间大小，若time1<=time2，返回true；否则，返回false
	 * @param tim1
	 * @param time2
	 * @return
	 */
	public static boolean compareTime(String time1,String time2){
		boolean flag=false;
		try {
			Date date1=TimeFormatHelper.setTimeFormat(time1);
			Date date2=TimeFormatHelper.setTimeFormat(time2);
			if(date1!=null && date2!=null){
				if(date1.getTime()<=date2.getTime()){
					flag=true;
				}
			}
			
		} catch (Exception e) {
			logger.info("time1:"+time1+" ，time2"+time2);
			e.printStackTrace();
		}
		return flag;
	}
	
	
	/**
	 * 更新Order的快超时消息发送状态
	 * @param isWorkOrder true为工单类型，false为任务单类型
	 * @param isSendOrderCache
	 */
	@SuppressWarnings("unchecked")
	public void modifyOrderBizMsgStatus(boolean isWorkOrder,final List<String> isSendOrderCache){
		if(isSendOrderCache!=null && !isSendOrderCache.isEmpty()){
			if(isWorkOrder){
				this.hibernateTemplate.execute(new HibernateCallback(){
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuilder sbhql=new StringBuilder();
						StringBuilder sb_param=new StringBuilder();
						Query query=null;
						if(isSendOrderCache!=null && !isSendOrderCache.isEmpty()){
							for(int i=0;i<isSendOrderCache.size();i++){
								sb_param.append("?").append(",");
							}
							sb_param.deleteCharAt(sb_param.length()-1);
							if(sb_param!=null && !"".equals(sb_param.toString())){
								sbhql.append("update WorkmanageWorkorder o set o.isSendWillOverTime=1 where o.woId in(");
								sbhql.append(sb_param.toString()).append(")");
								query=session.createQuery(sbhql.toString());
								for(int i=0;i<isSendOrderCache.size();i++){
			        				query.setParameter(i,isSendOrderCache.get(i));
			        			}
								query.executeUpdate();
							}
						}
						return null;
					}
				});
			}else{
				this.hibernateTemplate.execute(new HibernateCallback(){
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuilder sbhql=new StringBuilder();
						StringBuilder sb_param=new StringBuilder();
						Query query=null;
						if(isSendOrderCache!=null && !isSendOrderCache.isEmpty()){
							for(int i=0;i<isSendOrderCache.size();i++){
								sb_param.append("?").append(",");
							}
							sb_param.deleteCharAt(sb_param.length()-1);
							if(sb_param!=null && !"".equals(sb_param.toString())){
								sbhql.append("update WorkmanageTaskorder o set o.isSendWillOverTime=1 where o.toId in(");
								sbhql.append(sb_param.toString()).append(")");
								query=session.createQuery(sbhql.toString());
								for(int i=0;i<isSendOrderCache.size();i++){
			        				query.setParameter(i,isSendOrderCache.get(i));
			        			}
								query.executeUpdate();
							}
						}
						return null;
					}
				});
			}
			isSendOrderCache.clear();
		}
	}
	
	
	/**
	 * 更新Order的已超时消息发送状态
	 * @param isWorkOrder true为工单类型，false为任务单类型
	 * @param isSendOrderCache
	 */
	@SuppressWarnings("unchecked")
	public void modifyOrderBizMsgOverTimeStatus(boolean isWorkOrder,final List<String> isSendOrderCache){
		if(isSendOrderCache!=null && !isSendOrderCache.isEmpty()){
			if(isWorkOrder){
				this.hibernateTemplate.execute(new HibernateCallback(){
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuilder sbhql=new StringBuilder();
						StringBuilder sb_param=new StringBuilder();
						Query query=null;
						if(isSendOrderCache!=null && !isSendOrderCache.isEmpty()){
							for(int i=0;i<isSendOrderCache.size();i++){
								sb_param.append("?").append(",");
							}
							sb_param.deleteCharAt(sb_param.length()-1);
							if(sb_param!=null && !"".equals(sb_param.toString())){
								sbhql.append("update WorkmanageWorkorder o set o.isSendOverTime=1 where o.woId in(");
								sbhql.append(sb_param.toString()).append(")");
								query=session.createQuery(sbhql.toString());
								for(int i=0;i<isSendOrderCache.size();i++){
			        				query.setParameter(i,isSendOrderCache.get(i));
			        			}
								query.executeUpdate();
							}
						}
						return null;
					}
				});
			}else{
				this.hibernateTemplate.execute(new HibernateCallback(){
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuilder sbhql=new StringBuilder();
						StringBuilder sb_param=new StringBuilder();
						Query query=null;
						if(isSendOrderCache!=null && !isSendOrderCache.isEmpty()){
							for(int i=0;i<isSendOrderCache.size();i++){
								sb_param.append("?").append(",");
							}
							sb_param.deleteCharAt(sb_param.length()-1);
							if(sb_param!=null && !"".equals(sb_param.toString())){
								sbhql.append("update WorkmanageTaskorder o set o.isSendOverTime=1 where o.toId in(");
								sbhql.append(sb_param.toString()).append(")");
								query=session.createQuery(sbhql.toString());
								for(int i=0;i<isSendOrderCache.size();i++){
			        				query.setParameter(i,isSendOrderCache.get(i));
			        			}
								query.executeUpdate();
							}
						}
						return null;
					}
				});
			}
			isSendOrderCache.clear();
		}
	}
	
}
