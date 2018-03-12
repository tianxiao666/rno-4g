package com.iscreate.op.service.workmanage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.bizmsg.BizMessage;
import com.iscreate.op.pojo.cardispatch.CardispatchWorkorder;
import com.iscreate.op.pojo.publicinterface.Tasktracerecord;
import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.workmanage.WorkmanageBizprocessConf;
import com.iscreate.op.pojo.workmanage.WorkmanageCountTaskorderObject;
import com.iscreate.op.pojo.workmanage.WorkmanageCountWorkorderObject;
import com.iscreate.op.pojo.workmanage.WorkmanageTaskorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorderResourceAssociate;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.workmanage.exception.WorkManageDefineException;
import com.iscreate.op.service.workmanage.util.CommonTools;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.workflow.WFException;
import com.iscreate.plat.workflow.serviceaccess.ServiceBean;

public class WorkManageServiceImpl implements WorkManageService {
	
	private static final Log logger = LogFactory.getLog(WorkManageServiceImpl.class);
	private BizInfoFactoryService bizInfoFactoryService;
	private NetworkResourceInterfaceService networkResourceInterfaceService;	//networkResourceService
	private ServiceBean workFlowService;
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	
	private SysOrganizationDao sysOrganizationDao;//组织dao du.hw
	
	//ou.jh
	private SysOrgUserService sysOrgUserService;
	
	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	
	
	
	public void setBizInfoFactoryService(BizInfoFactoryService bizInfoFactoryService) {
		this.bizInfoFactoryService = bizInfoFactoryService;
	}
	

	
	
	public SysOrganizationDao getSysOrganizationDao() {
		return sysOrganizationDao;
	}

	public void setSysOrganizationDao(SysOrganizationDao sysOrganizationDao) {
		this.sysOrganizationDao = sysOrganizationDao;
	}

	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}

	
	public void setWorkFlowService(ServiceBean workFlowService) {
		this.workFlowService = workFlowService;
	}
	
	
	/**
	 * 创建工单
	 * @param bizProcessCode 业务流程Code
	 * @param workOrder 公共工单对象
	 * @param participantList 参与者列表
	 * @param workorderassnetresource 工单关联资源对象
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return 工单号
	 */
	public String createWorkOrder(String bizProcessCode,WorkmanageWorkorder workOrder,List<String> participantList,Workorderassnetresource workorderassnetresource,Tasktracerecord tasktracerecord){
		String processDefineId="";
		String processInstanceId="";
		String woId="";
		Date currentDate=new Date();
		logger.info("----------------检查输入参数---------------begin------------");
		if(bizProcessCode==null || "".equals(bizProcessCode)){
			throw new WorkManageException("请指定要创建的工单的业务流程类型");
		}
		
		if(workOrder==null || workOrder.getWoTitle()==null || "".equals(workOrder.getWoTitle()) 
				|| workOrder.getWoType()==null || "".equals(workOrder.getWoType()) 
				|| workOrder.getCreator()==null || "".equals(workOrder.getCreator())
				|| workOrder.getCreatorOrgId()==null || 0==workOrder.getCreatorOrgId()){
			throw new WorkManageException("公共工单【标题、工单类型信息，创建人、创建人组织】不能为空");
		}
		
		if(participantList==null || participantList.isEmpty()){
			throw new WorkManageException("请指定任务待办责任人");
		}
		
		logger.info("----------------检查输入参数---------------end------------");
		
		
		logger.info("----------------获取启动流程时对应的模板信息---------------begin------------");
		WorkmanageBizprocessConf bizProcessConf=this.bizInfoFactoryService.
		getBizProcessConfService().getBizProcessConfByProcessCode(bizProcessCode);
		
		processDefineId=bizProcessConf.getProcessDefineId();	//获取流程定义id
		logger.info("----------------获取启动流程时对应的模板信息---------------end------------");
		
		
		logger.debug("------------构造公共工单信息对象----------begin----");
		
		//生成工单号
		WorkManageIdGenerator bizIdGenerator = WorkManageIdGenerator.getInstance();
		woId=bizIdGenerator.generateWOID(bizProcessConf.getBizFlag());
		
		
		workOrder.setWoId(woId);	//工单id
		workOrder.setCreateTime(currentDate);	//工单创建时间
		workOrder.setIsOverTime(0);	//默认不超时
		workOrder.setIsRead(0);	//默认未读

		logger.debug("------------构造公共工单信息对象----------end----");
		
		
		//创建工单流程
		processInstanceId=this.bizInfoFactoryService.getWorkOrderHandleService().createWordOrderProcess(processDefineId, workOrder,participantList);
		
		//流程启动成功后
		if(processInstanceId!=null && !processInstanceId.isEmpty()){
			//判断网络设施资源是否存在，如果存在，则建立工单与网络设施的关系
			if(workorderassnetresource!=null && workorderassnetresource.getNetworkResourceId()!=null && workorderassnetresource.getNetworkResourceId().intValue()!=0
					&& workorderassnetresource.getNetworkResourceType()!=null && !workorderassnetresource.getNetworkResourceType().isEmpty()
					&& workorderassnetresource.getStationId()!=null && workorderassnetresource.getStationId().intValue()!=0){
				
				//保存工单与网络设施的关系
				workorderassnetresource.setWoId(woId);
				this.bizInfoFactoryService.getWorkOrderAssnetResourceDao().saveWorkOrderAssnetResourceDao(workorderassnetresource);
				
				//保存工单与网络设施的关系（针对资源的工单数据统计）
				WorkmanageWorkorderResourceAssociate associate=new WorkmanageWorkorderResourceAssociate();
				associate.setWoId(woId);
				associate.setNetworkResourceId(workorderassnetresource.getNetworkResourceId());
				associate.setNetworkResourceType(workorderassnetresource.getNetworkResourceType());
				associate.setStationId(workorderassnetresource.getStationId());
				this.bizInfoFactoryService.getWorkOrderHandleService().saveWorkmanageWorkorderResourceAssociate(associate);
				
				
//				//统计网络设施的工单数量
//				WorkmanageCountWorkorderObject networkResource_workorder=new WorkmanageCountWorkorderObject();
//				networkResource_workorder.setResourceId(String.valueOf(workorderassnetresource.getNetworkResourceId()));
//				networkResource_workorder.setResourceType(workorderassnetresource.getNetworkResourceType());
//				this.bizInfoFactoryService.getWorkOrderHandleService().countWorkOrderNumberForSave(networkResource_workorder);
//				
//				//保存站址工单数
//				WorkmanageCountWorkorderObject networkResource_station=new WorkmanageCountWorkorderObject();
//				networkResource_station.setResourceId(String.valueOf(workorderassnetresource.getStationId()));
//				networkResource_station.setResourceType(WorkManageConstant.NETWORKRESOURCE_STATION);
//				this.bizInfoFactoryService.getWorkOrderHandleService().countWorkOrderNumberForSave(networkResource_station);
				
				/*
				//获取站址信息
				Map<String,String> stationMap=this.networkResourceInterfaceService.getResourceByReIdAndReTypeService(
						String.valueOf(workorderassnetresource.getStationId()), WorkManageConstant.NETWORKRESOURCE_STATION);
				
				if(stationMap!=null && !stationMap.isEmpty()){
					if(stationMap.get("name")!=null && !"".equals(stationMap.get("name"))){
						networkResource_station.setResourceName(stationMap.get("name"));
					}
					if(stationMap.get("longitude")!=null && !"".equals(stationMap.get("longitude"))){
						networkResource_station.setLongitude(stationMap.get("longitude"));
					}
					if(stationMap.get("latitude")!=null && !"".equals(stationMap.get("latitude"))){
						networkResource_station.setLatitude(stationMap.get("name"));
					}
				}
				//获取站址所属区域
				Map<String,String> stationOfAreaMap=this.networkResourceInterfaceService.getAreaByStationId(String.valueOf(workorderassnetresource.getStationId()), WorkManageConstant.NETWORKRESOURCE_STATION);
				if(stationOfAreaMap!=null && !stationOfAreaMap.isEmpty()){
					if(stationOfAreaMap.get("id")!=null && !"".equals(stationOfAreaMap.get("id"))){
						networkResource_station.setAreaId(stationOfAreaMap.get("id"));
					}
				}
				
				*/
				
				
//				//统计人的工单数量统计
//				if(participantList!=null && !participantList.isEmpty()){
//					String userId=participantList.get(0);
//					WorkmanageCountWorkorderObject people_workorder=new WorkmanageCountWorkorderObject();
//					people_workorder.setResourceId(userId);
//					people_workorder.setResourceType(WorkManageConstant.RESOURCE_PEOPLE);
//					this.bizInfoFactoryService.getWorkOrderHandleService().countWorkOrderNumberForSave(people_workorder);
//				}
			}
			
			//保存服务过程跟踪记录
			if(tasktracerecord!=null){
				tasktracerecord.setWoId(woId);
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
			
			//抢修消息盒子
			if(WorkManageConstant.PROCESS_URGENTREPAIR_WORKORDER_CODE.equals(bizProcessCode)){
				//查找工单状态
				Map woMap=this.bizInfoFactoryService.getWorkOrderHandleService().getWorkOrderByWoIFowShowByHibernate(woId);
				if(woMap!=null){
					String statusName=woMap.get("statusName")==null?"":woMap.get("statusName").toString();
					String bizMessageContent="";
					bizMessageContent="【"+statusName+"】"+workOrder.getWoTitle();
					
					BizMessage bizMsg=new BizMessage();
					bizMsg.setContent(bizMessageContent);
					bizMsg.setSendTime(new Date());
					bizMsg.setReceivePerson(participantList.get(0));
					bizMsg.setSendPerson(workOrder.getCreator());
					bizMsg.setType(workOrder.getWoType());
					bizMsg.setTitle(woId);
					if(woMap!=null){
						String formUrl=woMap.get("formUrl")==null?"":woMap.get("formUrl").toString();
						String terminalFormUrl=woMap.get("terminalFormUrl")==null?"":woMap.get("terminalFormUrl").toString();
						
						formUrl=formUrl+"?WOID="+woId+"&orgId="+workOrder.getCreatorOrgId();
						terminalFormUrl=terminalFormUrl+"?WOID="+woId+"&orgId="+workOrder.getCreatorOrgId();
						String functionType = getFunctionType(formUrl);
						bizMsg.setFunctionType(functionType);
						bizMsg.setWoId(woId);
						bizMsg.setFunctionType("UrgentRepairWorkOrder");
						bizMsg.setLink(formUrl);
						bizMsg.setLinkForMobile(terminalFormUrl);
						
						this.bizInfoFactoryService.getBizMessageService().txAddBizMessageService(bizMsg,"newTask");
					}
				}
			}
			//缓存工单信息
			Map<String,Object> cacheMap=new HashMap<String,Object>();
			cacheMap.put("woId", workOrder.getWoId());	//工单id
			cacheMap.put("isWorkOrder", "1");	//是否是工单
			cacheMap.put("woType", workOrder.getWoType());	//工单业务类型
			
			if(workOrder.getRequireCompleteTime()!=null){
				cacheMap.put("requireCompleteTime", TimeFormatHelper.getTimeFormatBySecond(workOrder.getRequireCompleteTime()));	//要求完成时间
			}
			cacheMap.put("currentHandler", workOrder.getCurrentHandler());
			WorkManageTimer.addBizOrderToOrderListMap(WorkManageTimer.orderCache, cacheMap);
			
		}else{
			this.bizInfoFactoryService.getWorkOrderHandleService().deleteWorkOrder(workOrder);
			return null;
		}
		return woId;
	}


	/**
	 * 受理工单
	 * @param woId 工单id
	 * @param currentHandler 当前处理人
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean acceptWorkOrder(String woId,String currentHandler,Tasktracerecord tasktracerecord){
		boolean isSuccess=false;
		if(woId==null || "".equals(woId)){
			throw new WorkManageException("公共工单id不能为空");
		}
		if(currentHandler==null || "".equals(currentHandler)){
			throw new WorkManageException("公共工单处理人不能为空");
		}

//		isSuccess=this.bizInfoFactoryService.getWorkOrderHandleService().completeWordOrderProcessTask(woId, 
//				currentHandler, outcome, participantList);
		isSuccess=this.bizInfoFactoryService.getWorkOrderHandleService().acceptWorkOrder(woId,currentHandler);
		if(isSuccess){
			if(tasktracerecord!=null){
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
		}
		
		return isSuccess;
	}
	
	
	/**
	 * 结束完成工单
	 * @param woId
	 * @param currenthandler
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean finishWorkOrder(String woId,String currentHandler,Tasktracerecord tasktracerecord){
		boolean isSuccess=false;
		if(woId==null || "".equals(woId)){
			throw new WorkManageException("公共工单id不能为空");
		}
		
		if(currentHandler==null || "".equals(currentHandler)){
			throw new WorkManageException("公共工单处理人不能为空");
		}
		
		isSuccess=this.bizInfoFactoryService.getWorkOrderHandleService()
		.completeWordOrderProcessTask(woId, currentHandler, null, null);
		
		if(isSuccess){
			if(tasktracerecord!=null){
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
			
			//统计网络设施的工单数量
			//根据工单id，获取对应关联的网络设施
			List<Workorderassnetresource> assnetresourceList=this.bizInfoFactoryService.getWorkOrderAssnetResourceDao().getWorkOrderAssnetResourceRecordDao("woId", woId);
			if(assnetresourceList!=null && !assnetresourceList.isEmpty()){
				Workorderassnetresource assnetresource=assnetresourceList.get(0);
				
				boolean isDelete=this.bizInfoFactoryService.getWorkOrderHandleService().deleteWorkmanageWorkorderResourceAssociate("woId", woId);
				
				
//				WorkmanageCountWorkorderObject networkResource_workorder=new WorkmanageCountWorkorderObject();
//				networkResource_workorder.setResourceId(String.valueOf(assnetresource.getNetworkResourceId()));
//				networkResource_workorder.setResourceType(assnetresource.getNetworkResourceType());
//				this.bizInfoFactoryService.getWorkOrderHandleService().countWorkOrderNumberForSubtract(networkResource_workorder);
//				
//				WorkmanageCountWorkorderObject networkResource_station=new WorkmanageCountWorkorderObject();
//				networkResource_station.setResourceId(String.valueOf(assnetresource.getStationId()));
//				networkResource_station.setResourceType(WorkManageConstant.NETWORKRESOURCE_STATION);
//				this.bizInfoFactoryService.getWorkOrderHandleService().countWorkOrderNumberForSubtract(networkResource_station);
				
				
				
				
			}
			
			
			//更改工单及其关联的任务单的消息的状态为“已读”----- begin ------
			List<String> bizTitleList=new ArrayList<String>();
			//更改工单
			if(woId!=null && !"".equals(woId)){
				bizTitleList.add(woId);
			}
			
			//更改工单及其关联的任务单
			List<Map> taskOrderList=this.getTaskOrderListByWoId(woId);
			if(taskOrderList!=null && !taskOrderList.isEmpty()){
				for(Map map:taskOrderList){
					String toId=map.get("toId")==null?null:map.get("toId").toString();
					if(toId!=null && !toId.isEmpty()){
						bizTitleList.add(toId);
					}
				}
			}
			
			//进行更新
			this.bizInfoFactoryService.getBizMessageService().txUpdateBizMessageToHasReadByTitleList(bizTitleList);
			
			//更改工单及其关联的任务单的消息的状态为“已读”----- end ------
			
			//统计人的工单数量
			WorkmanageCountWorkorderObject people_workorder=new WorkmanageCountWorkorderObject();
			people_workorder.setResourceId(currentHandler);
			people_workorder.setResourceType(WorkManageConstant.RESOURCE_PEOPLE);
			this.bizInfoFactoryService.getWorkOrderHandleService().countWorkOrderNumberForSubtract(people_workorder);
			
			
			//去除快超时的信息
			List<String> toIdAndWoIdList=this.getChildTaskOrderIdListByWoId(woId);
			toIdAndWoIdList.add(woId);
			this.bizInfoFactoryService.getBizMessageService().txUpdateBizMessageToHasReadByTitleList(toIdAndWoIdList);
			
		}
//			xxx
		
		return isSuccess;
	}
	
	
	
	/**
	 * 更改工单状态
	 * @param woId
	 * @param status
	 * @return
	 */
	public boolean updateWorkOrderStatus(String woId,int status){
		boolean isSuccess=false;
		isSuccess=this.bizInfoFactoryService.getWorkOrderHandleService().updateWorkOrderStatusProcess(woId, status);
		return isSuccess;
	}
	
	/**
	 * 派发任务单
	 * @param bizProcessCode 业务流程Code
	 * @param woId 工单id
	 * @param parentBizOrderId 父业务单号
	 * @param taskOrder 任务单对象
	 * @param participantList 参与者列表
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return 任务单号
	 */
	public String assignTaskOrder(String bizProcessCode,String woId,String parentBizOrderId,WorkmanageTaskorder taskOrder,List<String> participantList,Tasktracerecord tasktracerecord){
		String processDefineId="";
		String processInstanceId="";
		String toId="";
		logger.info("----------------检查输入参数---------------begin------------");
		if(bizProcessCode==null || "".equals(bizProcessCode)){
			throw new WorkManageException("请指定要创建的工单的业务流程类型");
		}
		
		if(woId==null || "".equals(woId) || parentBizOrderId==null || "".equals(parentBizOrderId)){
			throw new WorkManageException("工单id或者父业务单id不能为空");
		}
		
		if(taskOrder==null || taskOrder.getToTitle()==null || "".equals(taskOrder.getToTitle()) 
				|| taskOrder.getToType()==null || "".equals(taskOrder.getToType()) 
				|| taskOrder.getAssigner()==null || "".equals(taskOrder.getAssigner())
				|| taskOrder.getAssignerOrgId()==null || 0==taskOrder.getAssignerOrgId()){
			throw new WorkManageException("任务单【标题、任务单类型信息，派发人、派发人人组织】不能为空");
		}
		
		if(participantList==null || participantList.isEmpty()){
			throw new WorkManageException("请指定任务待办责任人");
		}
		
		logger.info("----------------检查输入参数---------------end------------");
		
		
		logger.info("----------------获取启动流程时对应的模板信息---------------begin------------");
		WorkmanageBizprocessConf bizProcessConf=this.bizInfoFactoryService.
		getBizProcessConfService().getBizProcessConfByProcessCode(bizProcessCode);
		
		processDefineId=bizProcessConf.getProcessDefineId();	//获取流程定义id
		logger.info("----------------获取启动流程时对应的模板信息---------------end------------");
		
		
		logger.debug("------------构造任务单信息对象----------begin----");
		
		//生成任务单号
		WorkManageIdGenerator bizIdGenerator = WorkManageIdGenerator.getInstance();
		toId=bizIdGenerator.generateTOID(woId);
		
		taskOrder.setToId(toId);	//任务单id
		taskOrder.setWoId(woId);	//工单id
		taskOrder.setParentBizOrderId(parentBizOrderId);	//父业务单号
		taskOrder.setAssignTime(new Date());	//任务单派发时间
		taskOrder.setIsOverTime(0);	//默认不超时
		taskOrder.setIsRead(0);	//默认未读
		
		logger.debug("------------构造任务单信息对象----------end----");
		
		
		//创建任务单流程
		processInstanceId=this.bizInfoFactoryService.getTaskOrderHandleService().createTaskOrderProcess(bizProcessCode,processDefineId, taskOrder, participantList);
		
		//流程启动成功后
		if(processInstanceId!=null && !processInstanceId.isEmpty()){
			
			if("抢修".equals(taskOrder.getToType())){
				//保存服务过程跟踪记录
				if(tasktracerecord!=null){
					tasktracerecord.setWoId(woId);
					tasktracerecord.setToId(toId);
					this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
				}
				
				//消息盒子（新工作）------------- begin----------
				Map toMap=this.bizInfoFactoryService.getTaskOrderHandleService().getTaskOrderByToIdInHibernate(toId);
				if(toMap!=null){
					String statusName=toMap.get("statusName")==null?"":toMap.get("statusName").toString();
					String bizMessageContent="";
					bizMessageContent="【"+statusName+"】"+taskOrder.getToTitle();
					
					BizMessage bizMsg=new BizMessage();
					bizMsg.setContent(bizMessageContent);
					bizMsg.setSendTime(new Date());
					bizMsg.setReceivePerson(participantList.get(0));
					bizMsg.setSendPerson(taskOrder.getAssigner());
					bizMsg.setType(taskOrder.getToType());
					bizMsg.setTitle(toId);
					if(toMap!=null){
						String formUrl=toMap.get("formUrl")==null?"":toMap.get("formUrl").toString();
						String terminalFormUrl=toMap.get("terminalFormUrl")==null?"":toMap.get("terminalFormUrl").toString();
						
						formUrl=formUrl+"?WOID="+woId+"&TOID="+toId+"&orgId="+taskOrder.getAssignerOrgId();
						terminalFormUrl=terminalFormUrl+"?WOID="+woId+"&TOID="+toId+"&orgId="+taskOrder.getAssignerOrgId();
						String functionType = getFunctionType(formUrl);
						bizMsg.setFunctionType(functionType);
						bizMsg.setWoId(woId);
						bizMsg.setToId(toId);
						bizMsg.setLink(formUrl);
						bizMsg.setLinkForMobile(terminalFormUrl);
						
						this.bizInfoFactoryService.getBizMessageService().txAddBizMessageService(bizMsg,"newTask");
					}
				}
				//消息盒子（新工作）------------- end----------
			}
			
			//统计人的任务单数量
			if(participantList!=null && !participantList.isEmpty()){
				String userId=participantList.get(0);
				WorkmanageCountTaskorderObject people_taskorder=new WorkmanageCountTaskorderObject();
				people_taskorder.setObjectId(userId);
				people_taskorder.setObjectType(WorkManageConstant.RESOURCE_PEOPLE);
				this.bizInfoFactoryService.getTaskOrderHandleService().countTaskOrderNumberForSave(people_taskorder);
			}
			
			//缓存任务单信息
			Map<String,Object> cacheMap=new HashMap<String,Object>();
			cacheMap.put("toId", taskOrder.getToId());	//任务单id
			cacheMap.put("woId", taskOrder.getWoId());	//工单id
			cacheMap.put("isWorkOrder", "0");	//是否是工单
			cacheMap.put("woType", taskOrder.getToType());	//任务单业务类型
			
			if(taskOrder.getRequireCompleteTime()!=null){
				cacheMap.put("requireCompleteTime", TimeFormatHelper.getTimeFormatBySecond(taskOrder.getRequireCompleteTime()));	//要求完成时间
			}
			cacheMap.put("currentHandler", taskOrder.getCurrentHandler());
			WorkManageTimer.addBizOrderToOrderListMap(WorkManageTimer.orderCache, cacheMap);
		}
		return toId;
	}
	
	
	
	/**
	 * 受理任务单
	 * @param taskOrder 任务单对象
	 * @param currentHandler 当前处理人
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean acceptTaskOrder(WorkmanageTaskorder taskOrder,String currentHandler,Tasktracerecord tasktracerecord){
		boolean isSuccess=false;
		if(taskOrder==null || taskOrder.getToId()==null || "".equals(taskOrder.getToId())){
			throw new WorkManageException("任务单id不能为空");
		}
		if(currentHandler==null || "".equals(currentHandler)){
			throw new WorkManageException("任务单处理人不能为空");
		}

		isSuccess=this.bizInfoFactoryService.getTaskOrderHandleService().acceptTaskOrder(taskOrder,currentHandler);
		if(isSuccess){
			if(tasktracerecord!=null){
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
		}
		return isSuccess;
	}
	
	/**
	 * 结束完成任务单
	 * @param toId
	 * @param currenthandler
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean finishTaskOrder(String toId,String currentHandler,Tasktracerecord tasktracerecord){
		boolean isSuccess=false;
		if(toId==null || "".equals(toId)){
			throw new WorkManageException("公共工单id不能为空");
		}
		if(currentHandler==null || "".equals(currentHandler)){
			throw new WorkManageException("公共工单处理人不能为空");
		}
		
		isSuccess=this.bizInfoFactoryService.getTaskOrderHandleService()
		.completeTaskOrderProcessTask(toId, currentHandler, null, null);
		
		if(isSuccess){
			if(tasktracerecord!=null){
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
			
			
			//更改任务单及其关联的子任务单的消息的状态为“已读”----- begin ------
			List<String> bizTitleList=new ArrayList<String>();
			//更改工单
			if(toId!=null && !"".equals(toId)){
				bizTitleList.add(toId);
			}
			//更改工单及其关联的任务单
			List<Map> childrenTaskOrderList=this.getChildTaskOrderListByToId(toId);
			if(childrenTaskOrderList!=null && !childrenTaskOrderList.isEmpty()){
				for(Map map:childrenTaskOrderList){
					String childrenToId=map.get("toId")==null?null:map.get("toId").toString();
					if(childrenToId!=null && !childrenToId.isEmpty()){
						bizTitleList.add(childrenToId);
					}
				}
			}
			
			//进行更新
			this.bizInfoFactoryService.getBizMessageService().txUpdateBizMessageToHasReadByTitleList(bizTitleList);
			//更改任务单及其关联的子任务单的消息的状态为“已读”----- end ------
			
			
			//统计人的任务单数量
			WorkmanageCountTaskorderObject people_taskorder=new WorkmanageCountTaskorderObject();
			people_taskorder.setObjectId(currentHandler);
			people_taskorder.setObjectType(WorkManageConstant.RESOURCE_PEOPLE);
			this.bizInfoFactoryService.getTaskOrderHandleService().countTaskOrderNumberForSubtract(people_taskorder);
			
			
			//去除快超时消息
			List<String> toIdList=new ArrayList<String>();
			toIdList.add(toId);
			this.bizInfoFactoryService.getBizMessageService().txUpdateBizMessageToHasReadByTitleList(toIdList);
			
		}
		return isSuccess;
	}
	
	
	/**
	 * 更改任务单状态
	 * @param toId
	 * @param status
	 * @return
	 */
	public boolean updateTaskOrderStatus(String toId,int status){
		boolean isSuccess=false;
		isSuccess=this.bizInfoFactoryService.getTaskOrderHandleService().updateTaskOrderStatusProcess(toId, status);
		return isSuccess;
	}
	
	/**
	 * 根据工单id获取需要展示的工单信息
	 * @param woId
	 * @return
	 */
	public Map<String,String> getWorkOrderForShow(String woId){
		Map<String,String> woMap=this.bizInfoFactoryService.getWorkOrderHandleService().getWorkOrderByWoIdForShowProcess(woId);
		return woMap;
	}
	
	
	/**
	 * 根据工单id获取需要展示的车辆工单信息
	 * @param woId
	 * @return
	 */
	public Map<String,String> getCarDispatchWorkOrderForShow(String woId){
		Map<String,String> woMap=this.bizInfoFactoryService.getWorkOrderHandleService().getCarDispatchWorkOrderByWoIdForShowProcess(woId);
		return woMap;
	}
	
	/**
	 * 根据工单id获取需要展示的巡检计划工单信息
	 * @param woId
	 * @return
	 */
	public Map<String,String> getRoutineInsepctionWorkOrderForShow(String woId){
		
		Map<String,String> woMap=this.bizInfoFactoryService.getWorkOrderHandleService().getRoutineInspectionWorkOrderByWoIdForShowProcess(woId);
		return woMap;
	}
	
	
	/**
	 * 根据任务单id获取需要展示的任务单信息
	 * @param toId
	 * @return
	 */
	public Map<String,String> getTaskOrderForShow(String toId){
		Map<String,String> toMap=this.bizInfoFactoryService.getTaskOrderHandleService().getTaskOrderByToIdForShowProcess(toId);
		return toMap;
	}
	
	
	/**
	 * 根据任务单id获取需要展示的巡检任务单信息
	 * @param toId
	 * @return
	 */
	public Map<String,String> getRoutineInsepctionTaskOrderForShow(String toId){
		
		Map<String,String> toMap=this.bizInfoFactoryService.getTaskOrderHandleService().getRoutineInspectionTaskOrderByToIdForShowProcess(toId);
		return toMap;
	}
	
	
	/**
	 * 根据工单id获取工单对象
	 * @param woId
	 * @return
	 */
	public WorkmanageWorkorder getWorkOrderEntity(String woId){
		WorkmanageWorkorder workOrder=this.bizInfoFactoryService.getWorkOrderHandleService().getWorkOrderByWoId(woId);
		return workOrder;
	}
	
	
	
	/**
	 * 根据任务单id获取任务单对象
	 * @param toId
	 * @return
	 */
	public WorkmanageTaskorder getTaskOrderEntity(String toId){
		WorkmanageTaskorder taskOrder=this.bizInfoFactoryService.getTaskOrderHandleService().getTaskOrderByToId(toId);
		return taskOrder;
	}
	
	
	
	
	
	/**
	 * 根据用户账号与工单号，判断当前用户是否是该工单的当前处理人
	 * @param woId
	 * @param currentUser
	 * @return
	 */
	public boolean judgeWorkOrderCurrentHandler(String woId,String currentUser){
		boolean isYes=this.bizInfoFactoryService.getWorkOrderHandleService().judgeWorkOrderCurrentHandlerProcess(woId, currentUser);
		return isYes;
	}
	
	
	/**
	 * 根据用户账号与任务单号，判断当前用户是否是该任务单的当前处理人
	 * @param toId
	 * @param currentUser
	 * @return
	 */
	public boolean judgeTaskOrderCurrentHandler(String toId,String currentUser){
		boolean isYes=this.bizInfoFactoryService.getTaskOrderHandleService().judgeTaskOrderCurrentHandler(toId, currentUser);
		return isYes;
	}
	
	
	/**
	 * 获取工单关联的任务单列表
	 * @param woId
	 * @return
	 */
	public List<Map> getTaskOrderListByWoId(String woId){
		List<Map> list=this.bizInfoFactoryService.getTaskOrderHandleService().getTaskOrderListByWoIdProcess(woId);
		return list;
	}
	
	
	
	/**
	 * 获取工单关联的任务单列表
	 * @param woId
	 * @return
	 */
	public List<Map> getRoutineInsepctionTaskOrderListByWoId(String woId){
		List<Map> list=this.bizInfoFactoryService.getTaskOrderHandleService().getRoutineInspectionTaskOrderListByWoIdProcess(woId);
		return list;
	}
	
	
	
	/**
	 * 转派工单
	 * @param woId 工单号
	 * @param updateWorkOrder 需要更新的工单对象属性
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean handoverWorkOrder(String woId,WorkmanageWorkorder updateWorkOrder,Tasktracerecord tasktracerecord){
		boolean isSuccess=this.bizInfoFactoryService.getWorkOrderHandleService().handoverWorkOrderProcess(woId, updateWorkOrder);
		if(isSuccess){
			if(tasktracerecord!=null){
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
		}
		return isSuccess;
	}
	
	
	
	/**
	 * 转派任务单
	 * @param woId 工单号
	 * @param toId 任务单号
	 * @param updateWorkOrder 需要更新的工单对象属性
	 * @param updateTaskOrder 需要更新的任务单对象属性
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean handoverTaskOrder(String woId,String toId,WorkmanageWorkorder updateWorkOrder,WorkmanageTaskorder updateTaskOrder,Tasktracerecord tasktracerecord){
		String oldPeople="";
		String newPeople="";
		WorkmanageWorkorder pk_workOrder=this.getWorkOrderEntity(woId);
		
		//获取任务单对象
		WorkmanageTaskorder taskOrder=this.bizInfoFactoryService.getTaskOrderHandleService().getTaskOrderByToId(toId);
		if(taskOrder!=null){
			oldPeople=taskOrder.getCurrentHandler();
			newPeople=updateTaskOrder.getCurrentHandler();
		}
		
		boolean isSuccess=this.bizInfoFactoryService.getTaskOrderHandleService().handoverTaskOrderProcess(pk_workOrder,toId, updateWorkOrder,updateTaskOrder);
		if(isSuccess){
			
			//服务过程跟踪记录
			if(tasktracerecord!=null){
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
			
			//消息盒子（新工作）------------- begin----------
			Map toMap=this.bizInfoFactoryService.getTaskOrderHandleService().getTaskOrderByToIdInHibernate(toId);
			if(toMap!=null){
				String statusName=toMap.get("statusName")==null?"":toMap.get("statusName").toString();
				String toTitle=toMap.get("toTitle")==null?"":toMap.get("toTitle").toString();
				String bizMessageContent="";
				bizMessageContent="【"+statusName+"】"+toTitle;
				
				BizMessage bizMsg=new BizMessage();
				bizMsg.setContent(bizMessageContent);
				bizMsg.setSendTime(new Date());
				bizMsg.setReceivePerson(newPeople);
				bizMsg.setSendPerson(oldPeople);
				bizMsg.setType(taskOrder.getToType());
				bizMsg.setTitle(toId);
				if(toMap!=null){
					String formUrl=toMap.get("formUrl")==null?"":toMap.get("formUrl").toString();
					String terminalFormUrl=toMap.get("terminalFormUrl")==null?"":toMap.get("terminalFormUrl").toString();
					
					formUrl=formUrl+"?WOID="+woId+"&TOID="+toId+"&orgId="+taskOrder.getAssignerOrgId();
					terminalFormUrl=terminalFormUrl+"?WOID="+woId+"&TOID="+toId+"&orgId="+taskOrder.getAssignerOrgId();
					String functionType = getFunctionType(formUrl);
					bizMsg.setFunctionType(functionType);
					bizMsg.setWoId(woId);
					bizMsg.setToId(toId);
					bizMsg.setLink(formUrl);
					bizMsg.setLinkForMobile(terminalFormUrl);
					
					this.bizInfoFactoryService.getBizMessageService().txAddBizMessageService(bizMsg,"newTask");
				}
			}
			//消息盒子（新工作）------------- end----------
		}
		return isSuccess;
	}
	
	/**
	 * 抢修阶段回复
	 * @param taskRecord 服务过程跟踪记录对象
	 * @return
	 */
	public boolean stepReply(Tasktracerecord taskRecord){
		boolean isSuccess=false;
		try {
			this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(taskRecord);
			isSuccess=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	
	/**
	 * 获取工单的子任务单列表
	 * @param woId
	 * @return
	 */
	public List<Map> getChildTaskOrderListByWoId(String woId){
		List<Map> list=this.bizInfoFactoryService.getTaskOrderHandleService().getChildTaskOrderListByWoIdProcess(woId);
		if(list!=null&&!list.isEmpty()){
			for(Map map :list){
				map.put("assignTime",TimeFormatHelper.getTimeFormatBySecond(map.get("assignTime")) );
				map.put("requireCompleteTime",TimeFormatHelper.getTimeFormatBySecond(map.get("requireCompleteTime")) );
			}
		}
		return list;
	}
	
	
	/**
	 * 获取任务单的子任务单列表
	 * @param toId
	 * @return
	 */
	public List<Map> getChildTaskOrderListByToId(String toId){
		List<Map> list=this.bizInfoFactoryService.getTaskOrderHandleService().getChildTaskOrderListByToIdProcess(toId);
		if(list!=null&&!list.isEmpty()){
			for(Map map :list){
				map.put("assignTime",TimeFormatHelper.getTimeFormatBySecond(map.get("assignTime")) );
				map.put("requireCompleteTime",TimeFormatHelper.getTimeFormatBySecond(map.get("requireCompleteTime")) );
			}
		}
		return list;
	}
	
	
	/**
	 * 工单驳回
	 * @param woId 工单id
	 * @param tasktracerecord 服务过程记录
	 * @return
	 */
	public boolean rejectWorkOrder(String woId,Tasktracerecord tasktracerecord){
		boolean isSuccess=false;
		isSuccess=this.bizInfoFactoryService.getWorkOrderHandleService().rejectWorkOrderProcess(woId);
		if(isSuccess){
			if(tasktracerecord!=null){
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
		}
		return isSuccess;
	}
	
	
	/**
	 * 任务单驳回
	 * @param taskOrder 任务单对象
	 * @param tasktracerecord 服务过程记录
	 * @return
	 */
	public boolean rejectTaskOrder(WorkmanageTaskorder taskOrder,Tasktracerecord tasktracerecord){
		boolean isSuccess=false;
		if(taskOrder!=null){
			isSuccess=this.bizInfoFactoryService.getTaskOrderHandleService().rejectTaskOrderProcess(taskOrder);
		}
		if(isSuccess){
			//保存服务过程跟踪记录
			if(tasktracerecord!=null){
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
			
			if(taskOrder!=null){
				//消息盒子（新工作）------------- begin----------
				String woId=taskOrder.getWoId();
				String toId=taskOrder.getToId();
				
				Map toMap=this.bizInfoFactoryService.getTaskOrderHandleService().getTaskOrderByToIdInHibernate(toId);
				if(toMap!=null){
					String statusName=toMap.get("statusName")==null?"":toMap.get("statusName").toString();
					String bizMessageContent="";
					bizMessageContent="【"+statusName+"】"+taskOrder.getToTitle();
					
					BizMessage bizMsg=new BizMessage();
					bizMsg.setContent(bizMessageContent);
					bizMsg.setSendTime(new Date());
					bizMsg.setReceivePerson(taskOrder.getAssigner());
					bizMsg.setSendPerson(taskOrder.getCurrentHandler());
					bizMsg.setType(taskOrder.getToType());
					bizMsg.setTitle(toId);
					if(toMap!=null){
						String formUrl=toMap.get("formUrl")==null?"":toMap.get("formUrl").toString();
						String terminalFormUrl=toMap.get("terminalFormUrl")==null?"":toMap.get("terminalFormUrl").toString();
						
						formUrl=formUrl+"?WOID="+woId+"&TOID="+toId+"&orgId="+taskOrder.getAssignerOrgId();
						terminalFormUrl=terminalFormUrl+"?WOID="+woId+"&TOID="+toId+"&orgId="+taskOrder.getAssignerOrgId();
						String functionType = getFunctionType(formUrl);
						bizMsg.setFunctionType(functionType);
						bizMsg.setWoId(woId);
						bizMsg.setToId(toId);
						bizMsg.setLink(formUrl);
						bizMsg.setLinkForMobile(terminalFormUrl);
						
						this.bizInfoFactoryService.getBizMessageService().txAddBizMessageService(bizMsg,"newTask");
					}
				}
				//消息盒子（新工作）------------- end----------
				
			}
		}
		return isSuccess;
	}
	
	
	/**
	 * 任务单撤销
	 * @param taskOrder 任务单对象
	 * @param currentCancelPeople 当前撤销人（操作人）
	 * @param tasktracerecord 服务过程记录
	 * @return
	 * @throws WorkManageException
	 */
	public boolean cancelTaskOrder(WorkmanageTaskorder taskOrder,String currentCancelPeople,Tasktracerecord tasktracerecord) throws WorkManageException {
		boolean isSuccess=false;
		if(taskOrder==null || taskOrder.getToId()==null || "".equals(taskOrder.getToId())){
			throw new WorkManageException("任务单id不能为空");
		}
		isSuccess=this.bizInfoFactoryService.getTaskOrderHandleService().cancelTaskOrderProcess(taskOrder, currentCancelPeople);
		if(isSuccess){
			if(tasktracerecord!=null){
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
		}
		return isSuccess;
	}
	
	
	/**
	 * 重新派发任务单（拒绝撤销）
	 * @param taskOrder 任务单对象
	 * @param currentHandler
	 * @param tasktracerecord 服务过程记录
	 * @return
	 */
	public boolean reAssignTaskOrder(WorkmanageTaskorder taskOrder,Tasktracerecord tasktracerecord) throws WorkManageException{
		if(taskOrder==null || taskOrder.getToId()==null || "".equals(taskOrder.getToId())){
			throw new WorkManageException("任务单id不能为空");
		}
		boolean isSuccess=false;
		isSuccess=this.bizInfoFactoryService.getTaskOrderHandleService().reAssignTaskOrderProcess(taskOrder);
		if(isSuccess){
			if(tasktracerecord!=null){
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
		}
		return isSuccess;
	}
	
	
	
	/**
	 * 根据资源类型与资源id，获取工单统计数量
	 * @param resourceType 资源类型
	 * @param resourceId 资源id
	 * @return
	 */
	public long getWorkOrderCountByResourceTypeAndResourceId(String resourceType,String resourceId){
		return this.bizInfoFactoryService.getWorkOrderHandleService().getWorkOrderCountByResourceTypeAndResourceId(resourceType, resourceId);
	}
	
	
	
	/**
	 * 根据对象类型与对象id，获取任务单单统计数量
	 * @param objectType 对象类型
	 * @param objectId 对象id
	 * @return
	 */
	public long getTaskOrderCountByObjectTypeAndObjectId(String objectType,String objectId){
		return this.bizInfoFactoryService.getTaskOrderHandleService().getTaskOrderCountByObjectTypeAndObjectId(objectType, objectId);
	}
	
	
	
	/**
	 * 获取用户的工单列表
	 * @param userId
	 * @param bizTypeCode 业务类型code：如urgentrepair（抢修）、cardispatch（车辆调度）、resourcedispatch（物资调度）
	 * @param workOrderType 工单类型：如pendingWorkOrder（待办工单）、如trackWorkOrder（跟踪工单）、superviseWorkOrder（监督工单）
	 * @param pageIndex 记录开始下标
	 * @param pageSize 记录每页显示数量
	 * @return
	 */
	public Map<String,Object> getUserWorkOrders(String userId,String bizTypeCode,String workOrderType,Integer pageIndex,Integer pageSize,String conditionString){
		Map<String,Object> resultMap=null;
		String defineSql="";
		if(bizTypeCode!=null && bizTypeCode!=null){
			if("urgentrepair".equals(bizTypeCode.toLowerCase())){		//抢修
				if("pendingworkorder".equals(workOrderType.toLowerCase())){		//获取用户待办工单
					
					defineSql="and 1=1 and \"currentHandler\"='"+userId+"' and \"status\"<>"+WorkManageConstant.WORKORDER_ASSIGNED+" and \"status\"<>"+WorkManageConstant.WORKORDER_END;
					if(conditionString!=null && !"".equals(conditionString)){
						defineSql=defineSql+" "+conditionString;
					}
					
					resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService(pageIndex==null?null:String.valueOf(pageIndex),
							pageSize==null?null:String.valueOf(pageSize), "\"createTime\"", "desc", "V_WM_URGENTREPAIR_WORKORDER", null,defineSql);
					
//					if(resultMap!=null && !resultMap.isEmpty()){
//						resultList=(List<Map>)resultMap.get("entityList");
//					}
				}else if("trackworkorder".equals(workOrderType.toLowerCase())){	//获取用户跟踪工单
					
					defineSql="and 1=1 and \"creator\"='"+userId+"' and \"status\"="+WorkManageConstant.WORKORDER_ASSIGNED+" and \"status\"<>"+WorkManageConstant.WORKORDER_END;
					if(conditionString!=null && !"".equals(conditionString)){
						defineSql=defineSql+" "+conditionString;
					}
					resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService(pageIndex==null?null:String.valueOf(pageIndex), 
							pageSize==null?null:String.valueOf(pageSize), "\"createTime\"", "desc", "V_WM_URGENTREPAIR_WORKORDER", null,defineSql);
//					if(resultMap!=null && !resultMap.isEmpty()){
//						resultList=(List<Map>)resultMap.get("entityList");
//					}
				}else if("superviseworkorder".equals(workOrderType.toLowerCase())){	//获取用户监督工单
					defineSql="and 1=1 and \"status\"<>"+WorkManageConstant.WORKORDER_END;
					
					List<String> orgIdList=new ArrayList<String>();
					//List<ProviderOrganization> topOrgList=this.providerOrganizationService.getTopLevelOrgByAccount(userId);
					List<SysOrg> topOrgList=this.sysOrganizationService.getTopLevelOrgByAccount(userId);
					
					if(topOrgList!=null && !topOrgList.isEmpty()){
						for(SysOrg org:topOrgList){
							//根据组织向下获取所有下属组织架构(服务商组织)
							//List<ProviderOrganization> childOrgList=this.providerOrganizationService.getOrgListDownwardByOrg(org.getId());
							//yuan.yw
							List<SysOrg> childOrgList=this.sysOrganizationService.getOrgListDownwardByOrg(org.getOrgId());
							if(childOrgList!=null && !childOrgList.isEmpty()){
								for(SysOrg childOrg:childOrgList){
									orgIdList.add(childOrg.getOrgId()+"");
								}
							}
						}
					}
					
					StringBuffer sb_orgIds=new StringBuffer("");
					//组装条件
					for(String tempId:orgIdList){
						sb_orgIds.append(tempId+",");
					}
					sb_orgIds.delete(sb_orgIds.length()-1, sb_orgIds.length());
					if(sb_orgIds!=null && !"".equals(sb_orgIds.toString())){
						defineSql =defineSql+" and \"creatorOrgId\" in ("+sb_orgIds.toString()+")";
					}
					
					if(conditionString!=null && !"".equals(conditionString)){
						defineSql=defineSql+" "+conditionString;
					}
					resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService(pageIndex==null?null:String.valueOf(pageIndex), 
							pageSize==null?null:String.valueOf(pageSize), "\"createTime\"", "desc", "V_WM_URGENTREPAIR_WORKORDER", 
							null,defineSql);
//					if(resultMap!=null && !resultMap.isEmpty()){
//						resultList=(List<Map>)resultMap.get("entityList");
//					}
				}
			}else if("cardispatch".equals(bizTypeCode.toLowerCase())){	//车辆调度
				if("pendingworkorder".equals(workOrderType.toLowerCase())){		//获取用户待办工单
					defineSql="and 1=1 and \"currentHandler\"='"+userId+"' and \"status\"<>"+WorkManageConstant.WORKORDER_END+" and (\"status\"="+WorkManageConstant.WORKORDER_WAIT4ASSIGNCAR+" or \"status\"="+WorkManageConstant.WORKORDER_WAIT4USECAR+" or \"status\"="+WorkManageConstant.WORKORDER_WAIT4RETURNCAR+")";
					if(conditionString!=null && !"".equals(conditionString)){
						defineSql=defineSql+" "+conditionString;
					}
					resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService(pageIndex==null?null:String.valueOf(pageIndex),
							pageSize==null?null:String.valueOf(pageSize), "\"createTime\"", "desc", "V_WM_CAR_WORKORDER", null,defineSql);
				}else if("trackworkorder".equals(workOrderType.toLowerCase())){	//获取用户跟踪工单
					
					defineSql="and 1=1 and \"currentHandler\"<>'"+userId+"' and \"recordHandler\"='"+userId+"' and \"status\"<>"+WorkManageConstant.WORKORDER_END+" and (\"status\"="+WorkManageConstant.WORKORDER_WAIT4ASSIGNCAR+" or \"status\"="+WorkManageConstant.WORKORDER_WAIT4USECAR+" or \"status\"="+WorkManageConstant.WORKORDER_WAIT4RETURNCAR+")";
					if(conditionString!=null && !"".equals(conditionString)){
						defineSql=defineSql+" "+conditionString;
					}
					resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService(pageIndex==null?null:String.valueOf(pageIndex),
							pageSize==null?null:String.valueOf(pageSize), "\"createTime\"", "desc", "V_WM_CAR_TRACEWORKORDER", null,defineSql);
				}
			}else if("resourcedispatch".equals(bizTypeCode.toLowerCase())){	//物资调度
				
			}
		}
		return resultMap;
	}

	
	/**
	 * 获取用户的任务单列表
	 * @param userId
	 * @param bizTypeCode  业务类型code：如urgentrepair（抢修）、cardispatch（车辆调度）、resourcedispatch（物资调度）、all(全部)
	 * @param taskOrderType 任务单类型：如pendingTaskOrder（待办任务单）、如trackTaskOrder（跟踪任务单）、superviseTaskOrder（监督任务单）
	 * @param pageIndex 记录开始下标
	 * @param pageSize 记录每页显示数量
	 * @param conditionString sql查询条件
	 * @return
	 */
	public Map<String,Object> getUserTaskOrders(String userId,String bizTypeCode,String taskOrderType,Integer pageIndex,Integer pageSize,String conditionString){
		Map<String,Object> resultMap=null;
		String defineSql="";
//		List<Map> resultList=null;
		if(bizTypeCode!=null && taskOrderType!=null){
			if("urgentrepair".equals(bizTypeCode.toLowerCase())){		//抢修
				if("pendingtaskorder".equals(taskOrderType.toLowerCase())){		//获取用户待办任务单
					defineSql="and 1=1 and \"currentHandler\"='"+userId+"' and \"status\"<>"+WorkManageConstant.TASKORDER_ASSIGNED+" and \"status\"<>"+WorkManageConstant.TASKORDER_CLOSED+" and \"status\"<>"+
					WorkManageConstant.TASKORDER_WAIT4CANCEL+" and \"status\"<>"+WorkManageConstant.TASKORDER_CANCELED+" and \"status\"<>"+WorkManageConstant.TASKORDER_UPGRADING;
					if(conditionString!=null && !"".equals(conditionString)){
						defineSql=defineSql+" "+conditionString;
					}
					resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService(pageIndex==null?null:String.valueOf(pageIndex), pageSize==null?null:String.valueOf(pageSize), 
							"\"assignTime\"", "desc", "V_WM_URGENTREPAIR_TASKORDER", null,defineSql);
//					if(resultMap!=null && !resultMap.isEmpty()){
//						resultList=(List<Map>)resultMap.get("entityList");
//					}
				}else if("tracktaskorder".equals(taskOrderType.toLowerCase())){	//获取用户跟踪任务单
					defineSql="and 1=1 and \"currentHandler\"='"+userId+"' and (\"status\"="+WorkManageConstant.TASKORDER_ASSIGNED+" or \"status\"="+WorkManageConstant.TASKORDER_UPGRADING+") and \"status\"<>"+WorkManageConstant.TASKORDER_CLOSED;
					if(conditionString!=null && !"".equals(conditionString)){
						defineSql=defineSql+" "+conditionString;
					}
					resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService(pageIndex==null?null:String.valueOf(pageIndex), pageSize==null?null:String.valueOf(pageSize), 
							"\"assignTime\"", "desc", "V_WM_URGENTREPAIR_TASKORDER", null,defineSql);
//					if(resultMap!=null && !resultMap.isEmpty()){
//						 resultList=(List<Map>)resultMap.get("entityList");
//					}
				}else if("supervisetaskorder".equals(taskOrderType.toLowerCase())){	//获取用户监督任务单
					defineSql="and 1=1 and \"status\"<>"+WorkManageConstant.TASKORDER_CLOSED;
					
					List<String> orgIdList=new ArrayList<String>();
					//List<ProviderOrganization> topOrgList=this.providerOrganizationService.getTopLevelOrgByAccount(userId);
					List<SysOrg> topOrgList=this.sysOrganizationService.getTopLevelOrgByAccount(userId);
					
					if(topOrgList!=null && !topOrgList.isEmpty()){
						for(SysOrg org:topOrgList){
							//根据组织向下获取所有下属组织架构(服务商组织)
								List<Map<String,Object>> childOrgList = sysOrganizationDao.getSelfAndChildOrgsListByOrgIds(org.getOrgId().toString());
							if(childOrgList!=null && !childOrgList.isEmpty()){
								for(Map<String,Object> childOrg:childOrgList){
									orgIdList.add(childOrg.get("orgId")+"");
								}
							}
						}
					}
					
					StringBuffer sb_orgIds=new StringBuffer("");
					//组装条件
					for(String tempId:orgIdList){
						sb_orgIds.append(tempId+",");
					}
					sb_orgIds.delete(sb_orgIds.length()-1, sb_orgIds.length());
					if(sb_orgIds!=null && !"".equals(sb_orgIds.toString())){
						defineSql =defineSql+" and \"assignerOrgId\" in ("+sb_orgIds.toString()+")";
					}
					
					
					if(conditionString!=null && !"".equals(conditionString)){
						defineSql=defineSql+" "+conditionString;
					}
					resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService(pageIndex==null?null:String.valueOf(pageIndex), pageSize==null?null:String.valueOf(pageSize), 
							"\"assignTime\"", "desc", "V_WM_URGENTREPAIR_TASKORDER", null,defineSql);
//					if(resultMap!=null && !resultMap.isEmpty()){
//						resultList=(List<Map>)resultMap.get("entityList");
//					}
				}
			}else if("cardispatch".equals(bizTypeCode.toLowerCase())){	//车辆调度
				
			}else if("resourcedispatch".equals(bizTypeCode.toLowerCase())){	//物资调度
				
			}else if("all".equals(bizTypeCode.toLowerCase())){	//全部业务类型的任务单
				defineSql="and 1=1";
				if(conditionString!=null && !"".equals(conditionString)){
					defineSql=defineSql+" "+conditionString;
				}
				resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService(pageIndex==null?null:String.valueOf(pageIndex), pageSize==null?null:String.valueOf(pageSize), 
						"\"assignTime\"", "desc", "V_WM_TASKORDER", null,defineSql);
			}
		}
		return resultMap;
	}
	
	
	
	/**
	 * 获取用户的任务单列表（for人员信息模块）
	 * @param userId
	 * @param bizTypeCode  业务类型code：如urgentrepair（抢修）、cardispatch（车辆调度）、resourcedispatch（物资调度）、all(全部)
	 * @param pageIndex 记录开始下标
	 * @param pageSize 记录每页显示数量
	 * @param conditionString sql查询条件
	 * @return
	 */
	public Map<String,Object> getUserTaskOrdersForStaffInfo(String userId,String bizTypeCode,Integer pageIndex,Integer pageSize,String conditionString){
		Map<String,Object> resultMap=null;
		String defineSql="and \"currentHandler\"='"+userId+"'";
//		List<Map> resultList=null;
		if(bizTypeCode!=null && !bizTypeCode.isEmpty()){
			if("urgentrepair".equals(bizTypeCode.toLowerCase())){		//抢修
				if(conditionString!=null && !"".equals(conditionString)){
					defineSql=defineSql+" "+conditionString;
				}
				resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService(pageIndex==null?null:String.valueOf(pageIndex), pageSize==null?null:String.valueOf(pageSize), 
						"\"assignTime\"", "desc", "V_WM_URGENTREPAIR_TASKORDER", null,defineSql);
			}else if("cardispatch".equals(bizTypeCode.toLowerCase())){	//车辆调度
				
				if(conditionString!=null && !"".equals(conditionString)){
					defineSql=defineSql+" "+conditionString;
				}
				resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService(pageIndex==null?null:String.valueOf(pageIndex), pageSize==null?null:String.valueOf(pageSize), 
						"\"createTime\"", "desc", "V_WM_CAR_WORKORDER", null,defineSql);
			}else if("resourcedispatch".equals(bizTypeCode.toLowerCase())){	//物资调度
				
			}else if("all".equals(bizTypeCode.toLowerCase())){	//全部业务类型的任务单
				if(conditionString!=null && !"".equals(conditionString)){
					defineSql=defineSql+" "+conditionString;
				}
				resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService(pageIndex==null?null:String.valueOf(pageIndex), pageSize==null?null:String.valueOf(pageSize), 
						"\"assignTime\"", "desc", "V_WM_TASKORDER", null,defineSql);
			}
		}
		return resultMap;
	}
	
	
	/**
	 * 根据资源类型、资源id获取该资源的工单单列表
	 * @param resourceType 资源类型
	 * @param resourceId 资源id
	 * @param bizTypeCode  业务类型code：如urgentrepair（抢修）、cardispatch（车辆调度）、resourcedispatch（物资调度）
	 * @param workOrderType 工单类型：如pendingWorkOrder（待办工单）、如trackWorkOrder（跟踪工单）、superviseWorkOrder（监督工单）
	 * @param conditionString sql查询条件
	 * @return
	 */
	public List<Map> getWorkOrderListByResourceTypeAndResourceId(String resourceType,String resourceId,String bizTypeCode,String workOrderType,String conditionString){
		List<Map> resultList=null;
		String sql="";
		List<String> params=null;
		if(bizTypeCode!=null && workOrderType!=null){
			if("urgentrepair".equals(bizTypeCode.toLowerCase())){		//抢修
				if("pendingworkorder".equals(workOrderType.toLowerCase())){
					if(WorkManageConstant.NETWORKRESOURCE_STATION.equals(resourceType)){
						sql="select t2.* from " +
						"wm_workorderassnetresource t1, V_WM_URGENTREPAIR_WORKORDER t2 " +
						"where t1.woId is not null and t1.woId = t2.\"woId\" and t2.\"status\" <> "+WorkManageConstant.WORKORDER_END+" and t1.stationId = ? ";
						if(conditionString!=null && !"".equals(conditionString)){
							sql=sql+conditionString;
						}
						String orderby=" order by t2.\"createTime\" desc";
						
						sql=sql+orderby;
						
						params=new ArrayList<String>();
						params.add(resourceId);
						resultList=this.bizInfoFactoryService.getDataSelectUtil().selectDataWithCondition(sql, params);
					}else{
						sql="select t2.* from " +
						"wm_workorderassnetresource t1, V_WM_URGENTREPAIR_WORKORDER t2 " +
						"where t1.woId is not null and t1.woId = t2.\"woId\" and t2.\"status\" <> "+WorkManageConstant.WORKORDER_END+" and t1.networkResourceType = ? and t1.networkResourceId=? ";
						
						if(conditionString!=null && !"".equals(conditionString)){
							sql=sql+conditionString;
						}
						String orderby=" order by t2.\"createTime\" desc";
						
						sql=sql+orderby;
						
						params=new ArrayList<String>();
						params.add(resourceType);
						params.add(resourceId);
						resultList=this.bizInfoFactoryService.getDataSelectUtil().selectDataWithCondition(sql, params);
					}
				}
			}else if("cardispatch".equals(bizTypeCode.toLowerCase())){	//车辆调度
				if(workOrderType.toLowerCase().equals("pendingworkorder")){
//					sql = 	" 	SELECT " + 
//									"		work.* , wo.* " + 
//									"	FROM  " + 
//									"		(SELECT w.*, status AS workorderStatus FROM wm_workorder w ) work " +
//									"		INNER JOIN car_workorder wo ON wo.woId = work.woId " + 
//									"		LEFT JOIN car_cardriverpair cardriverpair ON cardriverpair.id = wo.carDriverPairId " + 
//									"		LEFT JOIN (SELECT c.* , id AS carId FROM car_car c ) car ON car.id = cardriverpair.car_id " + 
//									"		LEFT JOIN car_driver driver ON driver.id = cardriverpair.driver_id " + 
//									"		LEFT JOIN car_carterminalpair carterminalpair ON carterminalpair.car_id = car.id " + 
//									"		LEFT JOIN car_terminal terminal ON terminal.id = carterminalpair.terminal_id ";
//					sql += " where car.id=? and workorderStatus != '" + WorkManageConstant.WORKORDER_END + "' ";
//					if(conditionString!=null && !"".equals(conditionString)){
//						sql += conditionString; 
//					}
//					sql += " order by work.\"createTime\" desc";
//					params=new ArrayList<String>();
//					params.add(resourceId);
//					resultList=this.bizInfoFactoryService.getDataSelectUtil().selectDataWithCondition(sql, params);
					
					sql="select t1.* from V_WM_CAR_WORKORDER t1,CAR_CARDRIVERPAIR t2,CAR_CAR t3 where t1.\"carDriverPairId\"=t2.id and t2.car_id=t3.id and t3.id=? and t1.\"status\"<>" + WorkManageConstant.WORKORDER_END;
					if(conditionString!=null && !"".equals(conditionString)){
						sql += conditionString; 
					}
					sql += " order by t1.\"createTime\" desc";
					params=new ArrayList<String>();
					params.add(resourceId);
					resultList=this.bizInfoFactoryService.getDataSelectUtil().selectDataWithCondition(sql, params);
					
				}
			}else if("resourcedispatch".equals(bizTypeCode.toLowerCase())){	//物资调度
				 
			}else if("all".equals(bizTypeCode.toLowerCase())){
				if(WorkManageConstant.NETWORKRESOURCE_STATION.equals(resourceType)){
					sql="select t2.* from " +
					"wm_workorderassnetresource t1, V_WM_URGENTREPAIR_WORKORDER t2 " +
					"where t1.woId is not null and t1.woId = t2.\"woId\" and t1.stationId = ? ";
					if(conditionString!=null && !"".equals(conditionString)){
						sql=sql+conditionString;
					}
					String orderby=" order by t2.\"createTime\" desc";
					
					sql=sql+orderby;
					
					params=new ArrayList<String>();
					params.add(resourceId);
					resultList=this.bizInfoFactoryService.getDataSelectUtil().selectDataWithCondition(sql, params);
				}else{
					sql="select t2.* from " +
					"wm_workorderassnetresource t1, V_WM_URGENTREPAIR_WORKORDER t2 " +
					"where t1.woId is not null and t1.woId = t2.\"woId\" and t1.networkResourceType = ? and t1.networkResourceId=? ";
					
					if(conditionString!=null && !"".equals(conditionString)){
						sql=sql+conditionString;
					}
					String orderby=" order by t2.\"createTime\" desc";
					
					sql=sql+orderby;
					
					params=new ArrayList<String>();
					params.add(resourceType);
					params.add(resourceId);
					resultList=this.bizInfoFactoryService.getDataSelectUtil().selectDataWithCondition(sql, params);
				}
			}
		}
		return resultList;
	}
	
	
	
	/**
	 * 根据资源类型、资源id获取该资源的任务单列表
	 * @param resourceType 资源类型
	 * @param resourceId 资源id
	 * @param bizTypeCode  业务类型code：如urgentrepair（抢修）、cardispatch（车辆调度）、resourcedispatch（物资调度）
	 * @param taskOrderType 任务单类型：如pendingTaskOrder（待办任务单）、如trackTaskOrder（跟踪任务单）、superviseTaskOrder（监督任务单）
	 * @param conditionString sql查询条件
	 * @return
	 */
	public List<Map> getTaskOrderListByResourceTypeAndResourceId(String resourceType,String resourceId,String bizTypeCode,String taskOrderType,String conditionString){
		List<Map> resultList=null;
		String sql="";
		List<String> params=null;
		if(bizTypeCode!=null && taskOrderType!=null){
			if("urgentrepair".equals(bizTypeCode.toLowerCase())){		//抢修
				if("pendingtaskorder".equals(taskOrderType.toLowerCase())){
					if(WorkManageConstant.NETWORKRESOURCE_STATION.equals(resourceType)){
						sql="select t1.* from V_WM_URGENTREPAIR_TASKORDER t1,(" +
							"select t3.woId from wm_workorderassnetresource t2,wm_workorder t3 " +
							"where t2.woId is not null and t2.woId = t3.woId and t2.stationId = ? ) t4 " +
							"where t1.\"toId\" is not null and t1.\"woId\" is not null and t1.\"woId\"=t4.woId and t1.\"status\"<>"+WorkManageConstant.TASKORDER_CLOSED+" ";
						
						if(conditionString!=null && !"".equals(conditionString)){
							sql=sql+conditionString;
						}
						String orderby=" order by t1.\"assignTime\" desc";
						
						sql=sql+orderby;
						
						params=new ArrayList<String>();
						params.add(resourceId);
						resultList=this.bizInfoFactoryService.getDataSelectUtil().selectDataWithCondition(sql, params);
					}else{
						sql="select t1.* from V_WM_URGENTREPAIR_TASKORDER t1,(" +
							"select t3.woId from wm_workorder t2,wm_workorder t3 " +
							"where t2.woId is not null and t2.woId=t3.woId  " +
							"and t2.networkResourceType = ? and t2.networkResourceId=? ) t4 " +
							"where t1.\"toId\" is not null and t1.\"woId\" is not null " +
							"and t1.\"woId\"=t4.woId and t1.\"status\"<>"+WorkManageConstant.TASKORDER_CLOSED+" ";
						
						if(conditionString!=null && !"".equals(conditionString)){
							sql=sql+conditionString;
						}
						String orderby=" order by t1.\"assignTime\" desc";
						
						sql=sql+orderby;
						
						params=new ArrayList<String>();
						params.add(resourceType);
						params.add(resourceId);
						resultList=this.bizInfoFactoryService.getDataSelectUtil().selectDataWithCondition(sql, params);
					}
				}
			}else if("cardispatch".equals(bizTypeCode.toLowerCase())){	//车辆调度
				
			}else if("resourcedispatch".equals(bizTypeCode.toLowerCase())){	//物资调度
				
			}else if("all".equals(bizTypeCode.toLowerCase())){
				if(WorkManageConstant.NETWORKRESOURCE_STATION.equals(resourceType)){
					sql="select t1.* from V_WM_URGENTREPAIR_TASKORDER t1,(" +
						"select t3.woId from wm_workorderassnetresource t2,wm_workorder t3 " +
						"where t2.woId is not null and t2.woId = t3.woId and t2.stationId = ? ) t4 " +
						"where t1.\"toId\" is not null and t1.\"woId\" is not null and t1.\"woId\"=t4.woId ";
					
					if(conditionString!=null && !"".equals(conditionString)){
						sql=sql+conditionString;
					}
					String orderby=" order by t1.\"assignTime\" desc";
					
					sql=sql+orderby;
					
					params=new ArrayList<String>();
					params.add(resourceId);
					resultList=this.bizInfoFactoryService.getDataSelectUtil().selectDataWithCondition(sql, params);
				}else{
					sql="select t1.* from V_WM_URGENTREPAIR_TASKORDER t1,(" +
						"select t3.woId from wm_workorderassnetresource t2,wm_workorder t3 " +
						"where t2.woId is not null and t2.woId=t3.woId  " +
						"and t2.networkResourceType = ? and t2.networkResourceId=? ) t4 " +
						"where t1.\"toId\" is not null and t1.\"woId\" is not null " +
						"and t1.\"woId\"=t4.woId ";
					
					
					if(conditionString!=null && !"".equals(conditionString)){
						sql=sql+conditionString;
					}
					String orderby=" order by t1.\"assignTime\" desc";
					
					sql=sql+orderby;
					
					params=new ArrayList<String>();
					params.add(resourceType);
					params.add(resourceId);
					resultList=this.bizInfoFactoryService.getDataSelectUtil().selectDataWithCondition(sql, params);
				}
			}
		}
		return resultList;
	}
	
	
	
	
	
//	/**
//	 * 根据资源id，获取资源关联的工单或任务单列表
//	 * @param resourceId 资源id
//	 * @param bizOrderCode 业务单类型code：如workOrder（工单）、taskOrder（任务单）
//	 * @return
//	 */
//	public List<Map> getWorkOrderOrTaskOrderByResourceId(String resourceId,String bizOrderCode){
//		List<Map> resultList=null;
//		String sql="";
//		List<String> params=null;
//		try {
//			if(bizOrderCode!=null){
//				if("workOrder".equals(bizOrderCode.toLowerCase())){	//获取资源关联的工单
//					
//					sql="select t2.* from " +
//					"workorderassnetresource t1, view_workmanage_urgentrepair_workorder t2 " +
//					"where t1.woId is not null and t1.woId = t2.woId and t2.`status` <> "+Constants.WORKORDER_WAIT4ACCEPT+" and " +
//					"t2.`status`<> "+Constants.WORKORDER_END+" and t1.stationId = ? order by t2.createTime;";
//					
//					params=new ArrayList<String>();
//					params.add(resourceId);
//					resultList=this.bizInfoFactoryService.getDataSelectUtil().selectDataWithCondition(sql, params);
//				}else if("taskOrder".equals(bizOrderCode.toLowerCase())){	//获取资源关联的任务单
//					
//					sql="select * from view_workmanage_urgentrepair_taskorder t3 " +
//					"where t3.toId is not null and t3.woId is not null and " +
//					"t3.`status`<>"+Constants.TASKORDER_WAIT4ACCEPT+" and t3.`status`<>"+Constants.TASKORDER_CLOSED+" and " +
//					"t3.woId in (select t2.woId from workorderassnetresource t1,view_workmanage_urgentrepair_workorder t2 " +
//					"where t1.woId is not null and t1.woId = t2.woId and t1.stationId = ?) order by t3.assignTime desc";
//					
//					params=new ArrayList<String>();
//					params.add(resourceId);
//					resultList=this.bizInfoFactoryService.getDataSelectUtil().selectDataWithCondition(sql, params);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return resultList;
//	}
	
	
	
	/**
	 * 自定义查询工单/任务单信息
	 * @param bizOrderCode 业务单类型code：如workOrder（工单）、taskOrder（任务单）
	 * @param conditionString 查询条件
	 * @return
	 */
	public List<Map> commonQuery(String bizOrderCode,String conditionString){
		Map<String,Object> resultMap=null;
		List<Map> resultList=null;
		if(bizOrderCode!=null){
			if("workorder".equals(bizOrderCode.toLowerCase())){	//获取工单
				resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService("V_WM_URGENTREPAIR_WORKORDER",conditionString);
				if(resultMap!=null && !resultMap.isEmpty()){
					resultList=(List<Map>)resultMap.get("entityList");
				}
			}else if("taskorder".equals(bizOrderCode.toLowerCase())){	//获取任务单
				resultMap=this.bizInfoFactoryService.getCommonQueryService().commonQueryService("V_WM_URGENTREPAIR_TASKORDER",conditionString);
				if(resultMap!=null && !resultMap.isEmpty()){
					resultList=(List<Map>)resultMap.get("entityList");
				}
			}
			
		}
		return resultList;
	}
	
	
//	/**
//	 * 根据资源id，获取资源关联的工单、任务单列表
//	 * @param resourceId
//	 * @param userId
//	 * @return
//	 */
//	public List<Map> getWorkOrderAndTaskOrderByResourceId(String resourceId,String userId){
//		List<Map> resultList=null;
//		String sql="";
//		List<String> params=null;
//		try {
//			
//			//获取资源关联的工单
//			sql="select t2.* from " +
//			"workorderassnetresource t1, view_workmanage_urgentrepair_workorder t2 " +
//			"where t1.woId is not null and t1.woId = t2.woId and t2.`status` <> "+Constants.WORKORDER_WAIT4ACCEPT+" and " +
//			"t2.`status`<> "+Constants.WORKORDER_END+" and t1.stationId = ? order by t2.createTime;";
//			
//			params=new ArrayList<String>();
//			params.add(resourceId);
//			List<Map> resourceUrgentrepairWorkOrderList=this.bizInfoFactoryService.getDataSelectUtil().selectDataWithCondition(sql, params);
//			
//			List<Map> aa=this.getUserWorkOrders(userId, "urgentrepair", "pendingWorkOrder", pageIndex, pageSize)
//			
//			//获取当前登录人的待办工单
//			List<Map> urgentrepairPendingWorkOrderList=null;
//			Map<String,Object> resultMap=this.bizInfoFactoryService.getCommonQueryService()
//			.commonQueryService("createTime", "desc", "view_workmanage_urgentrepair_workorder",
//					"and 1=1 AND currentHandler='"+userId+"' and status<>6 and status<>7");
//			if(resultMap!=null && !resultMap.isEmpty()){
//				urgentrepairPendingWorkOrderList=(List<Map>)resultMap.get("entityList");
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 创建车辆调度单
	 * @param bizProcessCode 业务流程Code
	 * @param workOrder 公共工单对象
	 * @param participantList 参与者列表
	 * @param cardispatchWorkorder 个性车辆调度单对象
	 * @param tasktracerecord 服务过程跟踪记录对象
	 * @return 工单号
	 */
	public String createCarDispatchWorkOrder(String bizProcessCode,WorkmanageWorkorder workOrder,List<String> participantList,CardispatchWorkorder cardispatchWorkorder,Tasktracerecord tasktracerecord){
		String processDefineId="";
		String processInstanceId="";
		String woId="";
		Date currentDate=new Date();
		logger.info("----------------检查输入参数---------------begin------------");
		if(bizProcessCode==null || "".equals(bizProcessCode)){
			throw new WorkManageException("请指定要创建的工单的业务流程类型");
		}
		
		if(workOrder==null || workOrder.getWoTitle()==null || "".equals(workOrder.getWoTitle()) 
				|| workOrder.getWoType()==null || "".equals(workOrder.getWoType()) 
				|| workOrder.getCreator()==null || "".equals(workOrder.getCreator())
				|| workOrder.getCreatorOrgId()==null || 0==workOrder.getCreatorOrgId()){
			throw new WorkManageException("公共工单【标题、工单类型信息，创建人、创建人组织】不能为空");
		}
		
		if(participantList==null || participantList.isEmpty()){
			throw new WorkManageException("请指定任务待办责任人");
		}
		
		logger.info("----------------检查输入参数---------------end------------");
		
		
		logger.info("----------------获取启动流程时对应的模板信息---------------begin------------");
		WorkmanageBizprocessConf bizProcessConf=this.bizInfoFactoryService.
		getBizProcessConfService().getBizProcessConfByProcessCode(bizProcessCode);
		
		processDefineId=bizProcessConf.getProcessDefineId();	//获取流程定义id
		logger.info("----------------获取启动流程时对应的模板信息---------------end------------");
		
		
		logger.debug("------------构造公共工单信息对象----------begin----");
		
		//生成工单号
		WorkManageIdGenerator bizIdGenerator = WorkManageIdGenerator.getInstance();
		woId=bizIdGenerator.generateWOID(bizProcessConf.getBizFlag());
		
		
		workOrder.setWoId(woId);	//工单id
		workOrder.setCreateTime(currentDate);	//工单创建时间
		workOrder.setIsOverTime(0);	//默认不超时
		workOrder.setIsRead(0);	//默认未读

		logger.debug("------------构造公共工单信息对象----------end----");
		
		
		//创建工单流程
		processInstanceId=this.bizInfoFactoryService.getWorkOrderHandleService().createWordOrderProcess(processDefineId, workOrder,participantList);
		
		//流程启动成功后
		if(processInstanceId!=null && !processInstanceId.isEmpty()){
			
			//消息盒子（新工作）------------- begin----------
			//发送车辆调度单消息盒子
			if(WorkManageConstant.PROCESS_CARDISPATCH_WORKORDER_CODE.equals(bizProcessCode) && cardispatchWorkorder!=null){
				//查找工单状态
				Map woMap=this.bizInfoFactoryService.getWorkOrderHandleService().getWorkOrderByWoIFowShowByHibernate(woId);
				if(woMap!=null){
					String statusName=woMap.get("statusName")==null?"":woMap.get("statusName").toString();
					String bizMessageContent="";
					bizMessageContent="【"+statusName+"】"+cardispatchWorkorder.getPlanUseCarAddress();
					BizMessage bizMsg=new BizMessage();
					bizMsg.setContent(bizMessageContent);
					bizMsg.setSendTime(new Date());
					bizMsg.setReceivePerson(participantList.get(0));
					bizMsg.setSendPerson(workOrder.getCreator());
					bizMsg.setType(workOrder.getWoType());
					bizMsg.setTitle(woId);
					if(woMap!=null){
						String formUrl=woMap.get("formUrl")==null?"":woMap.get("formUrl").toString();
						String terminalFormUrl=woMap.get("terminalFormUrl")==null?"":woMap.get("terminalFormUrl").toString();
						formUrl=formUrl+"?WOID="+woId+"&orgId="+workOrder.getCreatorOrgId();
						terminalFormUrl=terminalFormUrl+"?WOID="+woId+"&orgId="+workOrder.getCreatorOrgId();
						String functionType = getFunctionType(formUrl);
						bizMsg.setFunctionType(functionType);
						bizMsg.setWoId(woId);
						bizMsg.setLink(formUrl);
						bizMsg.setLinkForMobile(terminalFormUrl);
						this.bizInfoFactoryService.getBizMessageService().txAddBizMessageService(bizMsg,"newTask");
					}
				}
			}
			//消息盒子（新工作）------------- end----------
			
			//保存服务过程跟踪记录
			if(tasktracerecord!=null){
				tasktracerecord.setWoId(woId);
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
			
			
			if(cardispatchWorkorder!=null){
				//缓存工单信息
				Map<String,Object> cacheMap=new HashMap<String,Object>();
				cacheMap.put("woId", workOrder.getWoId());	//工单id
				cacheMap.put("isWorkOrder", "1");	//是否是工单
				cacheMap.put("woType", workOrder.getWoType());	//工单业务类型
				
				cacheMap.put("planUseCarTime", TimeFormatHelper.getTimeFormatBySecond(cardispatchWorkorder.getPlanUseCarTime()));	//预计用车时间
				cacheMap.put("planReturnCarTime", TimeFormatHelper.getTimeFormatBySecond(cardispatchWorkorder.getPlanReturnCarTime()));	//预计还车时间
				
				cacheMap.put("currentHandler", workOrder.getCurrentHandler());
				WorkManageTimer.addBizOrderToOrderListMap(WorkManageTimer.orderCache, cacheMap);
			}
			
		}else{
			this.bizInfoFactoryService.getWorkOrderHandleService().deleteWorkOrder(workOrder);
			return null;
		}
		
		return woId;
		
	}
	
	
	
	/**
	 * 派车操作
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @param participantList 下个任务环节待办人
	 * @param tasktracerecord 服务过程跟踪记录
	 * @param resource_carId 车辆资源Id
	 * @return
	 */
	public boolean assignCar(String woId,String currentOperator,List<String> participantList,Tasktracerecord tasktracerecord,String resource_carId){
		boolean isSuccess=false;
		isSuccess=this.bizInfoFactoryService.getWorkOrderHandleService().assignCarProcess(woId,currentOperator,participantList);
		if(isSuccess){
			//保存服务过程跟踪记录
			if(tasktracerecord!=null){
				tasktracerecord.setHandler(currentOperator);
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
			
			//更新车辆任务数
			if(resource_carId!=null && !"".equals(resource_carId)){
//				WorkmanageCountWorkorderObject resource_car=new WorkmanageCountWorkorderObject();
//				resource_car.setResourceId(String.valueOf(resource_carId));
//				resource_car.setResourceType(WorkManageConstant.RESOURCE_CAR);
//				this.bizInfoFactoryService.getWorkOrderHandleService().countWorkOrderNumberForSave(resource_car);
				
				WorkmanageWorkorderResourceAssociate associate=new WorkmanageWorkorderResourceAssociate();
				associate.setWoId(woId);
				associate.setNetworkResourceId(Long.valueOf(resource_carId));
				associate.setNetworkResourceType(WorkManageConstant.RESOURCE_CAR);
				this.bizInfoFactoryService.getWorkOrderHandleService().saveWorkmanageWorkorderResourceAssociate(associate);
			}
		}
		return isSuccess;
	}
	
	
	/**
	 * 派车操作
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @param participantList 下个任务环节待办人
	 * @param tasktracerecord 服务过程跟踪记录
	 * @param resource_carId 车辆资源Id
	 * @return
	 */
	public boolean assignCar(String woId,String currentOperator,List<String> participantList,Tasktracerecord tasktracerecord,String resource_carId,CardispatchWorkorder cardispatchWorkorder){
		boolean isSuccess=false;
		isSuccess=this.bizInfoFactoryService.getWorkOrderHandleService().assignCarProcess(woId,currentOperator,participantList);
		if(isSuccess){
			//保存服务过程跟踪记录
			if(tasktracerecord!=null){
				tasktracerecord.setHandler(currentOperator);
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
			
			//更新车辆任务数
			if(resource_carId!=null && !"".equals(resource_carId)){
//				WorkmanageCountWorkorderObject resource_car=new WorkmanageCountWorkorderObject();
//				resource_car.setResourceId(String.valueOf(resource_carId));
//				resource_car.setResourceType(WorkManageConstant.RESOURCE_CAR);
//				this.bizInfoFactoryService.getWorkOrderHandleService().countWorkOrderNumberForSave(resource_car);
				
				WorkmanageWorkorderResourceAssociate associate=new WorkmanageWorkorderResourceAssociate();
				associate.setWoId(woId);
				associate.setNetworkResourceId(Long.valueOf(resource_carId));
				associate.setNetworkResourceType(WorkManageConstant.RESOURCE_CAR);
				this.bizInfoFactoryService.getWorkOrderHandleService().saveWorkmanageWorkorderResourceAssociate(associate);
			}
			
			//消息盒子（新工作）------------- begin----------
			//查找工单状态
			Map woMap=this.bizInfoFactoryService.getWorkOrderHandleService().getWorkOrderByWoIFowShowByHibernate(woId);
			if(woMap!=null){
				String statusName=woMap.get("statusName")==null?"":woMap.get("statusName").toString();
				String woType=woMap.get("woType")==null?"":woMap.get("woType").toString();
				String bizMessageContent="";
				bizMessageContent="【"+statusName+"】"+cardispatchWorkorder.getPlanUseCarAddress();
				BizMessage bizMsg=new BizMessage();
				bizMsg.setContent(bizMessageContent);
				bizMsg.setSendTime(new Date());
				bizMsg.setReceivePerson(participantList.get(0));
				bizMsg.setSendPerson(currentOperator);
				bizMsg.setType(woType);
				bizMsg.setTitle(woId);
				if(woMap!=null){
					String formUrl=woMap.get("formUrl")==null?"":woMap.get("formUrl").toString();
					String terminalFormUrl=woMap.get("terminalFormUrl")==null?"":woMap.get("terminalFormUrl").toString();
					formUrl=formUrl+"?WOID="+woId;
					terminalFormUrl=terminalFormUrl+"?WOID="+woId;
					String functionType = getFunctionType(formUrl);
					bizMsg.setFunctionType(functionType);
					bizMsg.setWoId(woId);
					bizMsg.setLink(formUrl);
					bizMsg.setLinkForMobile(terminalFormUrl);
					this.bizInfoFactoryService.getBizMessageService().txAddBizMessageService(bizMsg,"newTask");
				}
			}
			//消息盒子（新工作）------------- end----------
		}
		return isSuccess;
	}
	
	/**
	 * 用车操作
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @param participantList 下个任务环节待办人
	 * @param tasktracerecord 服务过程跟踪记录
	 * @return
	 */
	public boolean useCar(String woId,String currentOperator,List<String> participantList,Tasktracerecord tasktracerecord){
		boolean isSuccess=false;
		isSuccess=this.bizInfoFactoryService.getWorkOrderHandleService().useCarProcess(woId,currentOperator,participantList);
		if(isSuccess){
			if(tasktracerecord!=null){
				tasktracerecord.setHandler(currentOperator);
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
		}
		return isSuccess;
		
	}
	
	
	/**
	 * 还车操作
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @param tasktracerecord 服务过程跟踪记录
	 * @param resource_carId 车辆资源Id
	 * @return
	 */
	public boolean returnCar(String woId,String currentOperator,Tasktracerecord tasktracerecord,String resource_carId){
		boolean isSuccess=false;
		isSuccess=this.bizInfoFactoryService.getWorkOrderHandleService().returnCarProcess(woId,currentOperator);
		if(isSuccess){
			if(tasktracerecord!=null){
				tasktracerecord.setHandler(currentOperator);
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
			
			if(resource_carId!=null && !"".equals(resource_carId)){
				//更新车辆任务数
//				WorkmanageCountWorkorderObject resource_car=new WorkmanageCountWorkorderObject();
//				resource_car.setResourceId(String.valueOf(resource_carId));
//				resource_car.setResourceType(WorkManageConstant.RESOURCE_CAR);
//				this.bizInfoFactoryService.getWorkOrderHandleService().countWorkOrderNumberForSubtract(resource_car);
				
				boolean isDelete=this.bizInfoFactoryService.getWorkOrderHandleService().deleteWorkmanageWorkorderResourceAssociate("woId", woId);
				
			}
		}
		return isSuccess;
	}
	
	
	/**
	 * 撤销车辆调度单
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @param tasktracerecord 服务过程跟踪记录
	 * @return
	 */
	public boolean cancelCarDispatchOrder(String woId,String currentOperator,Tasktracerecord tasktracerecord){
		boolean isSuccess=false;
		isSuccess=this.bizInfoFactoryService.getWorkOrderHandleService().cancelCarDispatchOrderProcess(woId, currentOperator);
		if(isSuccess){
			if(tasktracerecord!=null){
				tasktracerecord.setHandler(currentOperator);
				this.bizInfoFactoryService.getTaskTracingRecordDao().saveTaskTracingRecordDao(tasktracerecord);
			}
		}
		return isSuccess;
	}
	
	
	/**
	 * 根据工单id获取任务单id集合（只包括id属性）
	 * @param woId
	 * @return
	 */
	private List<String> getChildTaskOrderIdListByWoId(String woId){
		List<String> toIds=new ArrayList<String>();
		List<Map> list=this.bizInfoFactoryService.getTaskOrderHandleService().getTaskOrderListByWoIdProcess(woId);
		if(list!=null && !list.isEmpty()){
			for(Map map:list){
				String toId=map.get("toId").toString();
				toIds.add(toId);
			}
		}
		return toIds;
	}
	
	
	/**
	 * 根据工单id，获取工单对象
	 * @param woId
	 */
	public WorkmanageWorkorder getWorkOrderByWoId(String woId){
		return this.bizInfoFactoryService.getWorkOrderHandleService().getWorkOrderByWoId(woId);
	}
	
	/**
	 * 更新公共工单对象信息
	 * @param workmanageWorkorder
	 */
	public void updateWorkmanageWorkorder(WorkmanageWorkorder workmanageWorkorder){
		this.bizInfoFactoryService.getWorkOrderHandleService().updateWorkOrder(workmanageWorkorder);
	}
	
	
	
	/**
	 * 根据任务单id，获取任务单对象
	 * @param toId
	 * @return
	 */
	public WorkmanageTaskorder getTaskOrderByToId(String toId){
		return this.bizInfoFactoryService.getTaskOrderHandleService().getTaskOrderByToId(toId);
	}
	
	
	
	/**
	 * 更新任务单对象信息
	 * @param workmanageTaskorder
	 */
	public void updateWorkmanageTaskOrder(WorkmanageTaskorder workmanageTaskorder){
		this.bizInfoFactoryService.getTaskOrderHandleService().updateTaskOrder(workmanageTaskorder);
	}
	
	
	
	
	/**
	 * 根据资源类型获取工单统计对象列表
	 * @param resourceType
	 * @return
	 */
	public List<WorkmanageCountWorkorderObject> getWorkmanageCountWorkorderObjectListByResourceType(String resourceType){
		return this.bizInfoFactoryService.getWorkOrderHandleService().getWorkmanageCountWorkorderObjectListByResourceType(resourceType);
	}
	
	
	/**
	 * 判断任务单是否是群组任务
	 * @param toId
	 * @return
	 */
	public boolean judgeTaskIsGroup(String toId){
		boolean isGroupTask=false;
		isGroupTask=this.bizInfoFactoryService.getTaskOrderHandleService().judgeTaskIsGroup(toId);
		return isGroupTask;
	}
	
	
	
	/**
	 * 制定巡检计划工单
	 * @param bizProcessCode
	 * @param workOrder
	 * @param participantList
	 * @return
	 */
	public boolean createRoutineInspectionPlanWorkOrder(String bizProcessCode,WorkmanageWorkorder workmanageWorkorder) throws WorkManageDefineException{
		
		boolean isCreateSuccess=false;
		
		String processDefineId="";
		String processInstanceId="";
//		String woId="";
		Date currentDate=new Date();
		
		logger.info("----------------检查输入参数---------------begin------------");
		if(bizProcessCode==null || "".equals(bizProcessCode)){
			throw new WorkManageDefineException("请指定要创建的工单的业务流程类型");
		}
		
		if(workmanageWorkorder==null || workmanageWorkorder.getWoTitle()==null || "".equals(workmanageWorkorder.getWoTitle()) 
				|| workmanageWorkorder.getWoType()==null || "".equals(workmanageWorkorder.getWoType()) 
				|| workmanageWorkorder.getCreator()==null || "".equals(workmanageWorkorder.getCreator())){
			throw new WorkManageDefineException("公共工单【标题、工单类型信息，创建人】不能为空");
		}
		
		
		logger.info("----------------检查输入参数---------------end------------");
		
		
		logger.info("----------------获取启动流程时对应的模板信息---------------begin------------");
		WorkmanageBizprocessConf bizProcessConf=this.bizInfoFactoryService.
		getBizProcessConfService().getBizProcessConfByProcessCode(bizProcessCode);
		
		if(bizProcessConf!=null){
			processDefineId=bizProcessConf.getProcessDefineId();	//获取流程定义id
		}
		logger.info("----------------获取启动流程时对应的模板信息---------------end------------");
		
		
		logger.debug("------------构造公共工单信息对象----------begin----");
		
//		//生成工单号
//		WorkManageIdGenerator bizIdGenerator = WorkManageIdGenerator.getInstance();
//		woId=bizIdGenerator.generateWOID(bizProcessConf.getBizFlag());
		
		
//		workmanageWorkorder.setWoId(woId);	//工单id
		
		workmanageWorkorder.setStatus(WorkManageConstant.WORKORDER_EXECUTING);
		workmanageWorkorder.setCreateTime(currentDate);	//工单创建时间
		workmanageWorkorder.setIsOverTime(0);	//默认不超时
		workmanageWorkorder.setIsRead(0);	//默认未读

		logger.debug("------------构造公共工单信息对象----------end----");
		
		
		//默认创建人就是待办责任人
		List<String> participantList=new ArrayList<String>();
		participantList.add(workmanageWorkorder.getCreator());
		
		
		//创建工单流程
		processInstanceId=this.bizInfoFactoryService.getWorkOrderHandleService().createWordOrderProcess(processDefineId, workmanageWorkorder,participantList);
		
		
		System.out.println("processInstanceId=="+processInstanceId);
		
		//流程启动成功后
		if(processInstanceId!=null && !processInstanceId.isEmpty()){
			
			isCreateSuccess=true;
		}else{
			this.bizInfoFactoryService.getWorkOrderHandleService().deleteWorkOrder(workmanageWorkorder);
			return isCreateSuccess;
		}
		return isCreateSuccess;
	}
	
	
	/**
	 * 关闭巡检计划工单
	 * @param woId
	 * @param currentHandler
	 * @return
	 */
	public boolean finishRoutineInspectionPlanWorkOrder(String woId,String currentHandler) throws WorkManageDefineException{
		boolean isSuccess=false;
		if(woId==null || "".equals(woId)){
			throw new WorkManageDefineException("公共工单id不能为空");
		}
		
		if(currentHandler==null || "".equals(currentHandler)){
			throw new WorkManageDefineException("公共工单处理人不能为空");
		}
		
		isSuccess=this.bizInfoFactoryService.getWorkOrderHandleService()
		.completeWordOrderProcessTask(woId, currentHandler, null, null);
		
		if(isSuccess){	//调用工作接口成功
			WorkmanageWorkorder workmanageWorkorder=this.bizInfoFactoryService.getWorkOrderHandleService().getWorkOrderByWoId(woId);
			workmanageWorkorder.setStatus(WorkManageConstant.WORKORDER_CLOSED);
			this.bizInfoFactoryService.getWorkOrderHandleService().updateWorkOrder(workmanageWorkorder);
		}
		return isSuccess;
	}
	
	
	/**
	 * 创建巡检任务单
	 * @param bizProcessCode 业务流程Code
	 * @param woId 工单id
	 * @param parentBizOrderId 父业务单号
	 * @param workmanageTaskorder 任务单对象
	 * @param personalOrGroup 个人or群组任务标识
	 * @param taskAcceptorId 任务接收者
	 * @return
	 * @throws WorkManageDefineException
	 */
	public String createRoutineInspectionTaskOrder(String bizProcessCode,String woId,String parentBizOrderId,WorkmanageTaskorder workmanageTaskorder,String personalOrGroup,String taskAcceptorId) throws WorkManageDefineException{
		
		boolean isCreateSuccess=false;
		String processDefineId="";
		String processInstanceId="";
		String toId="";
		
		logger.info("----------------检查输入参数---------------begin------------");
		if(bizProcessCode==null || "".equals(bizProcessCode)){
			throw new WorkManageDefineException("请指定要创建的工单的业务流程类型");
		}
		
		if(woId==null || "".equals(woId) || parentBizOrderId==null || "".equals(parentBizOrderId)){
			throw new WorkManageDefineException("工单id或者父业务单id不能为空");
		}
		
		if(workmanageTaskorder==null || workmanageTaskorder.getToTitle()==null || "".equals(workmanageTaskorder.getToTitle()) 
				|| workmanageTaskorder.getToType()==null || "".equals(workmanageTaskorder.getToType())){
			throw new WorkManageDefineException("任务单【标题、任务单类型信息、任务单号】不能为空");
		}
		
		logger.info("----------------检查输入参数---------------end------------");
		
		
		logger.info("----------------获取启动流程时对应的模板信息---------------begin------------");
		WorkmanageBizprocessConf bizProcessConf=this.bizInfoFactoryService.
		getBizProcessConfService().getBizProcessConfByProcessCode(bizProcessCode);
		
		processDefineId=bizProcessConf.getProcessDefineId();	//获取流程定义id
		logger.info("----------------获取启动流程时对应的模板信息---------------end------------");
		
		
		logger.debug("------------构造任务单信息对象----------begin----");
		
		//生成任务单号
		WorkManageIdGenerator bizIdGenerator = WorkManageIdGenerator.getInstance();
		toId=bizIdGenerator.generateTOID(woId);
		workmanageTaskorder.setToId(toId);	//任务单id
		
		workmanageTaskorder.setStatus(WorkManageConstant.TASKORDER_WAIT4INSPECT);
		workmanageTaskorder.setWoId(woId);	//工单id
		workmanageTaskorder.setParentBizOrderId(parentBizOrderId);	//父业务单号
		workmanageTaskorder.setAssignTime(new Date());	//任务单派发时间
		workmanageTaskorder.setIsOverTime(0);	//默认不超时
		workmanageTaskorder.setIsRead(0);	//默认未读
		
		logger.debug("------------构造任务单信息对象----------end----");
		
		
		//创建任务单流程
//		processInstanceId=this.bizInfoFactoryService.getTaskOrderHandleService().createTaskOrderProcess(bizProcessCode,processDefineId, taskOrder, participantList);
		
		processInstanceId=this.bizInfoFactoryService.getTaskOrderHandleService().createTaskOrderProcess(bizProcessCode, processDefineId, workmanageTaskorder, personalOrGroup, taskAcceptorId);
		
		//流程启动成功后
		if(processInstanceId!=null && !processInstanceId.isEmpty()){
			isCreateSuccess=true;
		}
		return toId;
	}
	
	
	
	/**
	 * 关闭巡检任务单
	 * @param toId 任务单号
	 * @param currentHandler 当前处理人
	 * @return
	 */
	public boolean finishRoutineInspectionTaskOrder(String toId,String currentHandler) throws WorkManageDefineException{
		
		boolean isSuccess=false;
		if(toId==null || "".equals(toId)){
			throw new WorkManageDefineException("公共工单id不能为空");
		}
		if(currentHandler==null || "".equals(currentHandler)){
			throw new WorkManageDefineException("公共工单处理人不能为空");
		}
		
//		isSuccess=this.bizInfoFactoryService.getTaskOrderHandleService()
//		.completeTaskOrderProcessTask(toId, currentHandler, null, null);
		
		isSuccess=this.bizInfoFactoryService.getTaskOrderHandleService().completeRoutineInspectionProcessTask(toId, currentHandler);
		
		if(isSuccess){	//调用工作流接口成功，更新任务单信息
			WorkmanageTaskorder workmanageTaskorder=this.bizInfoFactoryService.getTaskOrderHandleService().getTaskOrderByToId(toId);
			workmanageTaskorder.setStatus(WorkManageConstant.TASKORDER_INSPECT_CLOSED);
			this.bizInfoFactoryService.getTaskOrderHandleService().updateTaskOrder(workmanageTaskorder);
			isSuccess=true;
		}
		return isSuccess;
	}
	
	
	
	/**
	 * 根据业务流程Code获取对应的业务流程配置对象
	 * @param bizProcessCode 业务流程Code
	 * @return
	 */
	public WorkmanageBizprocessConf getBizProcessConfByProcessCode(String bizProcessCode){
		return this.bizInfoFactoryService.
		getBizProcessConfService().getBizProcessConfByProcessCode(bizProcessCode);
	}
	
	
	
	
	
	
	/**
	 * 获取任务
	 * @param taskId 任务id
	 * @param currentOperator 当前操作人
	 * @return
	 */
	public boolean takeTask(String bizOrderId,String currentOperator){
		
		boolean isTaskSuccess=false;
		
		//根据业务单号，判断该单是工单or任务单
		boolean isTaskOrder=this.bizInfoFactoryService.getTaskOrderHandleService().judgeIsTaskOrder(bizOrderId);
		String taskId="";
		if(isTaskOrder){	//是任务单
			
			WorkmanageTaskorder workmanageTaskorder=this.bizInfoFactoryService.getTaskOrderHandleService().getTaskOrderByToId(bizOrderId);
			
			//更新当前处理人
			workmanageTaskorder.setCurrentHandler(currentOperator);
			workmanageTaskorder.setStatus(WorkManageConstant.TASKORDER_INSPECTING);
			//ou.jh
			SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(currentOperator);
//			Staff staff=this.providerOrganizationService.getStaffByAccount(currentOperator);
			if(sysOrgUserByAccount!=null){
				workmanageTaskorder.setCurrentHandlerName(sysOrgUserByAccount.getName());
			}
			this.bizInfoFactoryService.getTaskOrderHandleService().updateTaskOrder(workmanageTaskorder);
			
			try {
				//根据taskId，调用工作流接口，去获取任务
				taskId=workmanageTaskorder.getCurrentTaskId();
				isTaskSuccess = this.workFlowService.takeTask(taskId, currentOperator);
			} catch (WFException e) {
				e.printStackTrace();
				logger.error("根据taskId【"+taskId+"】，调用工作流接口，去获取对应的任务失败");
			}
		}else{
			
		}
		return isTaskSuccess;
	}
	
	
	/**
	 * 撤销巡检计划工单
	 * @param woId 工单id
	 * @param currentOperator 当前操作人
	 * @return
	 */
	public boolean cancelRoutineInspectionWorkOrder(String woId,String currentOperator){
		boolean isSuccess=false;
		isSuccess=this.bizInfoFactoryService.getWorkOrderHandleService().cancelRoutineInspectionWorkOrderProcess(woId, currentOperator);
		return isSuccess;
	}
	
	
	
	/**
	 * 撤销巡检任务单
	 * @param toId 任务单id
	 * @param currentCancelPeople 当前撤销人（操作人）
	 * @return
	 * @throws WorkManageException
	 */
	public boolean cancelRoutineInspectionTaskOrder(String toId,String currentCancelPeople) throws WorkManageException{
		boolean isSuccess=false;
		if(toId==null || "".equals(toId)){
			throw new WorkManageException("任务单id不能为空");
		}
		isSuccess=this.bizInfoFactoryService.getTaskOrderHandleService().cancelRoutineInspectionTaskOrderProcess(toId, currentCancelPeople);

		return isSuccess;
	}
	
	
	/**
	 * 根据工单id，获取关联的巡检已经关闭的任务单数目
	 * @param woId
	 * @return
	 */
	public int getCountOfRoutineInspectionCloseTaskOrderByWoId(String woId){
		return this.bizInfoFactoryService.getTaskOrderHandleService().getCountOfRoutineInspectionCloseTaskOrderByWoId(woId);
	}
	
	/**
	 * 根据工单列表，获取关联的巡检已经关闭的任务单数目
	 * @param woId
	 * @return
	 */
	public List<Map> getCountOfRoutineInspectionCloseTaskOrderByWoIdList(List<String> woIds){
		return this.bizInfoFactoryService.getTaskOrderHandleService().getCountOfRoutineInspectionCloseTaskOrderByWoIdList(woIds);
	}
	
	/**
	 * 根据工单id和状态，获取关联的巡检任务单数目
	 * @param woId
	 * @param status
	 * @return
	 */
	public int getCountOfRoutineInspectionTaskOrderByWoIdAndStatus(String woId,int status){
		return this.bizInfoFactoryService.getTaskOrderHandleService().getCountOfRoutineInspectionTaskOrderByWoIdAndStatus(woId, status);
	}
	
	
	/**
	 * 根据工单id，获取关联的巡检任务单数目
	 * @param woId
	 * @return
	 */
	public int getCountOfRoutineInspectionTaskOrderByWoId(String woId){
		return this.bizInfoFactoryService.getTaskOrderHandleService().getCountOfRoutineInspectionTaskOrderByWoId(woId);
	}
	
	
	/**
	 * 根据工单列表，获取关联的巡检任务单数目
	 * @param woId
	 * @return
	 */
	public List<Map> getCountOfRoutineInspectionTaskOrderByWoIdList(List<String> woIds){
		return this.bizInfoFactoryService.getTaskOrderHandleService().getCountOfRoutineInspectionTaskOrderByWoIdList(woIds);
	}
	
	/**
	 * 判断该任务单是否结束
	 * @return
	 */
	public boolean judgeTaskOrderIsEnd(String toId){
		return this.bizInfoFactoryService.getTaskOrderHandleService().judgeTaskOrderIsEndProcess(toId);
	}
	
	
	/**
	 * 判断该工单是否结束
	 * @param woId
	 * @return
	 */
	public boolean judgeWorkOrderIsEnd(String woId){
		return this.bizInfoFactoryService.getWorkOrderHandleService().judgeWorkOrderIsEndProcess(woId);
	}
	
	
	//	######  for mobile ######################
	
	
	/**
	 * author:che.yd 根据资源类型、资源id，用户id，获取任务单列表
	 * @param resType
	 * @param resourceId
	 * @param userId
	 * @param taskOrderType 任务单类型 pendingTaskOrder（待办任务单）、trackTaskOrder（跟踪任务单）、superviseTaskOrder（监督任务单）
	 * @return
	 */
	public List<Map> getTaskOrderListByResourceAndUserId(String resType,String resourceId,String userId,String taskOrderType){
		List<Map> resultList=new ArrayList<Map>();
		try {
			String resourceType=WorkManageConstant.NETWORKRESOURCE_STATION;
			if("basestation".equalsIgnoreCase(resType)){
				resourceType="basestation";
			}
			
			if("pendingTaskOrder".equals(taskOrderType)){	//待办任务单
				//------查询抢修的任务单集合---begin--------
				
				//获取站址关联的任务单
				List<Map> resourceTaskOrderList=getTaskOrderListByResourceTypeAndResourceId(resType,resourceId,"urgentrepair",taskOrderType,null);
				
				//获取当前人的待办任务单
				List<Map> userTaskOrderList=null;
				Map<String,Object> resultMap=getUserTaskOrders(userId, "urgentrepair", taskOrderType, null, null, null);
				if(resultMap!=null && !resultMap.isEmpty()){
					userTaskOrderList=(List<Map>)resultMap.get("entityList");
				}
				
				//获取2个集合的交集集合
				List<Map> taskOrderMixList=CommonTools.getTaskOrderMixList(resourceTaskOrderList,userTaskOrderList);
				
				if(taskOrderMixList!=null && !taskOrderMixList.isEmpty()){
					for(Map tempMap:taskOrderMixList){
						tempMap.put("taskOrderType", "urgentRepair");
						resultList.add(tempMap);
					}
				}
				//查询抢修的任务单集合-----end----------
				
				//查询巡检-----begin----------
				
//				//任务单
//				sqlContainer = context.createSqlContainer("select t2.* from routineinspection_stationtask t1,v_bizwf_taskorder t2 where t1.toId is not null " +
//						"and stationId=? and t1.toId=t2.TOID and status<>8;");
//				sqlContainer.setString(0,resourceId);
//				List<BasicEntity> xunjianTaskOrderList=context.executeSelectSQL(sqlContainer, "RoutineInspection_StationTask");
//				if(xunjianTaskOrderList!=null && !xunjianTaskOrderList.isEmpty()){
//					for(BasicEntity be:xunjianTaskOrderList){
//						Map<String,Object> tempMap=be.toMap();
//						tempMap.put("taskOrderType", "routineInspection");
//						resultList.add(tempMap);
//					}
////					resultList.addAll(xunjianTaskOrderList);
//				}
				
				//查询巡检-----end----------
			}else if("trackTaskOrder".equals(taskOrderType)){	//跟踪任务单
				
			}else if("superviseTaskOrder".equals(taskOrderType)){	//监督任务单
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
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

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}
	
}
