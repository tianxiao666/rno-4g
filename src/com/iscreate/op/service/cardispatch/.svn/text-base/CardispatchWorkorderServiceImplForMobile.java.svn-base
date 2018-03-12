package com.iscreate.op.service.cardispatch;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.BizAuthorityConstant;
import com.iscreate.op.constant.CardispatchConstant;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.constant.CardispatchConstant.CardispatchWorkorderState;
import com.iscreate.op.dao.cardispatch.CardispatchCarDao;
import com.iscreate.op.dao.cardispatch.CardispatchTerminalDao;
import com.iscreate.op.dao.cardispatch.CardispatchWorkorderDaoForMobile;
import com.iscreate.op.pojo.bizmsg.BizMessage;
import com.iscreate.op.pojo.cardispatch.CardispatchWorkorder;
import com.iscreate.op.pojo.publicinterface.Tasktracerecord;
import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.op.service.informationmanage.BaseServiceImpl;
import com.iscreate.op.service.publicinterface.TaskTracingRecordService;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.networkresource.common.tool.LatLngConversion;
import com.iscreate.plat.tools.LatLngHelper;
import com.iscreate.plat.tools.LatLngHelper.LatLng;
import com.iscreate.plat.tools.map.MapHelper;



@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class CardispatchWorkorderServiceImplForMobile extends BaseServiceImpl<CardispatchWorkorder> implements CardispatchWorkorderServiceForMobile {
	
	/************ 依赖注入 ***********/
	private CardispatchWorkorderDaoForMobile cardispatchWorkorderDaoForMobile;
	private WorkManageService workManageService;
	private CardispatchCarDao cardispatchCarDao;
	private CardispatchTerminalDao cardispatchTerminalDao;
	private String loginPerson;
	private SysOrgUser loginStaff;
	private SysAccount sysAccount;
	private TaskTracingRecordService taskTracingRecordService; 
	private BizMessageService bizMessageService;
	private SysOrgUserService sysOrgUserService;
	private SysAccountService sysAccountService;
	
	
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	/****************** service *******************/
	
	
	/**
	 * 根据woId,获取相关行车费用信息集合
	 * @param woId - 工单woId
	 * @return (List<Map<String,String>>) 行车费用信息集合
	 */
	public List<Map<String,String>> findCardispatchWorkorderFeerecordByWoIdForMobile ( String woId ) {
		List<Map<String, String>> list = cardispatchWorkorderDaoForMobile.findCardispatchWorkorderFeerecordByWoIdForMobile(woId);
		return list;
	}
	 
	
	public Map<String,String> findCardispatchWorkorderAssociateTaskByWoIdForMobile ( String woId ) {
		Map<String, String> associate = cardispatchWorkorderDaoForMobile.findCardispatchWorkorderAssociateTaskByWoIdForMobile(woId);
		Map<String, String> result_map = new HashMap<String, String>();
		if ( associate != null && associate.size() > 0 ) {
			if ( associate.containsKey("associateWorkType") && associate.get("associateWorkType") != null ) {
				if ( associate.containsKey("associateToId") && associate.get("associateToId") != null ) {
					if ( associate.get("associateWorkType").equals(CardispatchConstant.CARDISATCHWORKORDER_APPLYTYPE_URGENTREPAIR) ) {
						//是抢修
						result_map = workManageService.getTaskOrderForShow(associate.get("associateToId"));
					}
				}
			}
		}
		return result_map;
	}
	
	
	/**
	 * 根据工单woId,获取单张车辆调度单信息
	 * @param woId - 车辆调度单woId
	 * @return (Map<String,String>) 相应的车辆调度单信息
	 */
	public Map<String,String> findSingleCardispatchWorkorderByWoIdForMobile ( String woId ) {
		Map<String, String> workorder = cardispatchWorkorderDaoForMobile.findSingleCardispatchWorkorderByWoIdForMobile(woId);
		//createTime
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat c_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String createTime = workorder.get("createTime");
		if ( !StringUtil.isNullOrEmpty(createTime) ) {
			try {
				Date date = sdf.parse(createTime);
				createTime = c_sdf.format(date);
				workorder.put("createTime",createTime);
			} catch (ParseException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		//planUseCarTime
		String planUseCarTime = workorder.get("planUseCarTime");
		if ( !StringUtil.isNullOrEmpty(planUseCarTime) ) {
			try {
				Date date = sdf.parse(planUseCarTime);
				planUseCarTime = c_sdf.format(date);
				workorder.put("planUseCarTime",planUseCarTime);
			} catch (ParseException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		//planReturnCarTime
		String planReturnCarTime = workorder.get("planReturnCarTime");
		if ( !StringUtil.isNullOrEmpty(planReturnCarTime) ) {
			try {
				Date date = sdf.parse(planReturnCarTime);
				planReturnCarTime = c_sdf.format(date);
				workorder.put("planReturnCarTime",planReturnCarTime);
			} catch (ParseException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		//用车人(中文)
		String useCarPersonId = workorder.get("useCarPersonId");
		if ( !StringUtil.isNullOrEmpty(useCarPersonId) ) {
			//ou.jh
			SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(useCarPersonId);
//			Staff staff = providerOrganizationService.getStaffByAccount(useCarPersonId);
			if( sysOrgUserByAccount != null ) {
				workorder.put("useCarPersonCName",sysOrgUserByAccount.getName());
				if ( !StringUtil.isNullOrEmpty(sysOrgUserByAccount.getMobile()) ) {
					workorder.put("useCarPersonCNamePhone",sysOrgUserByAccount.getMobile());
				} else if ( !StringUtil.isNullOrEmpty(sysOrgUserByAccount.getTel()) ) {
					workorder.put("useCarPersonCNamePhone",sysOrgUserByAccount.getTel());
				}
			}
		}
		return workorder;
	}
	
	
	
	/**
	 * 根据工单、车辆
	 */
	public List<Map<String,String>> findCardispatchWordorderByStateForSize ( Map param_map , String state , Integer size ) {
		List<Map<String, String>> workorder_list = cardispatchWorkorderDaoForMobile.findCardispatchWordorderByStateForSize(param_map, state , size );
		for (int j = 0; workorder_list != null && j < workorder_list.size(); j++) {
			Map<String, String> workorder = workorder_list.get(j);
			formatWorkorder(workorder);
		}
		return workorder_list;
	}
	
	/**
	 * 根据工单、车辆
	 */
	public Integer findCardispatchWordorderByStateForSizeCount ( Map param_map , String state , Integer size ) {
		List<Map<String, String>> workorder_list = cardispatchWorkorderDaoForMobile.findCardispatchWordorderByStateForSize(param_map, state , size );
		Integer num = 0;
		if ( workorder_list != null ) {
			num = workorder_list.size();
		}
		return num;
	}
	   
	/**
	 * 根据车辆、工单信息为参,获取工单信息
	 * @param param_map 车辆、工单信息
	 * @param useCarTime_startTime 计划用车起始时间
	 * @param useCarTime_endTime 计划用车结束时间
	 * @param gender 排序字段
	 * @param upordown 升序降序
	 * @return (List<Map<String,String>>) 信息集合
	 */
	public List<Map<String,String>> findCarWorkOrderListBySearchForMobile ( Map param_map , String useCarTime_startTime , String useCarTime_endTime , String gender , String upordown , Integer size ) {
		List<Map<String, String>> workorder_list = cardispatchWorkorderDaoForMobile.findCarWorkOrderListBySearchForMobile( param_map , useCarTime_startTime , useCarTime_endTime , gender , upordown , size );
		for (int j = 0; workorder_list != null && j < workorder_list.size(); j++) {
			Map<String, String> workorder = workorder_list.get(j);
			formatWorkorder(workorder);
		}
		return workorder_list;
	}
	
	
	
	
	/**
	 * 车辆调度单 - 根据条件,判断下个跳转页面
	 * @param woId - 工单id
	 * @param requestParamMap - 提交参数
	 * return url - 跳转地址
	 */
	public String enterCardispatchWorkorder( String woId , Map<String,String> requestParamMap ) {
		Map<String, String> workorder = cardispatchWorkorderDaoForMobile.findCardispatchWorkerorderType(woId);
		Integer workorderType = Integer.valueOf(workorder.get("status"));
		String path = "";
		String url = "";
		if( workorderType == WorkManageConstant.WORKORDER_END ) {
			//已关闭
			url = path+ "cardispatch_look.html?WOID="+woId;
		} else if ( workorderType == WorkManageConstant.WORKORDER_WAIT4RETURNCAR  ) {
			//已用车、待还车
			url = path+ "cardispatch_returnCar.html?WOID="+woId;
		} else if ( workorderType == WorkManageConstant.WORKORDER_WAIT4USECAR ) {
			//已派车、待用车
			if ( requestParamMap.containsKey("type") && !StringUtil.isNullOrEmpty(requestParamMap.get("type")) ){
				url = path+ "cardispatch_look.html?WOID="+woId+"&type="+requestParamMap.get("type");
			} else {
				//判断用车人是否当前登录人
				boolean isUseCarPerson = cardispatchWorkorderDaoForMobile.checkUseCarPerson(loginPerson,woId);
				if ( isUseCarPerson ) {
					//当前登录人是用车人
					url = path+ "cardispatch_useCar.html?WOID="+woId;
				} else {
					//当前登录人不是用车人
					url = path+ "cardispatch_look.html?WOID="+woId;
				}
			}
		} else if ( workorderType == WorkManageConstant.WORKORDER_WAIT4ASSIGNCAR ) {
			//已创建、待派车
			//判断当前登录人是否调度员
			boolean flag = this.sysOrgUserService.checkUserRoleByAccountAndCode(loginPerson, BizAuthorityConstant.ROLE_CARDISPATCHER);
			//boolean flag = providerOrganizationService.checkAccountAndRoleCodeService(loginPerson, BizAuthorityConstant.ROLE_CARDISPATCHER);
			if ( flag ) {
				//当前登录人是调度人员
				url = path+ "cardispatch_look.html?WOID="+woId;
				//派车页面(陈立斌调用)
				String driverCarId = requestParamMap.get("driverCarId");
				String carNumber = requestParamMap.get("carNumber");
				if ( !StringUtil.isNullOrEmpty(driverCarId) && !StringUtil.isNullOrEmpty(carNumber) ) {
					url += "&carNumber="+carNumber+"&driverCarId="+driverCarId;
				}
			} else {
				//当前登录人不是调度员
				url = path+ "cardispatch_urge.html?WOID="+woId;
			}
		}
		return url;
	}
	
	
	/**
	 * 催办
	 * @param WOID - 工单id
	 * @param remindersReason - 催办内容
	 * @return - 是否催办成功
	 */
	public boolean remindersCarTask ( String WOID , String remindersReason ) {		
		//流程处理者
		Tasktracerecord tc = new Tasktracerecord();
		tc.setWoType(WorkManageConstant.WORKORDER_TYPE_CARDISPATCH);
		tc.setWoId(WOID);
		tc.setHandler(this.loginPerson);
		tc.setHandlerName(this.loginStaff.getName());
		tc.setHandleResultDescription("【催办原因】" + remindersReason);
		tc.setHandleTime(new Date());
		tc.setHandleWay(CardispatchConstant.CARDISATCHWORKORDER_CSTATE_URGE);
		String flag = taskTracingRecordService.txSaveTaskTracingRecordService(tc);
		//消息盒
		//List<ProviderOrganization> topLevelOrgByAccount = providerOrganizationService.getTopLevelOrgByAccount(loginPerson);
		//yuan.yw
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(loginPerson);
		
		List<String> participantList = new ArrayList<String>();
		if ( topLevelOrgByAccount != null && topLevelOrgByAccount.size() > 0 ) {
			//获取当前登录人最高级别组织
			SysOrg providerOrganization = topLevelOrgByAccount.get(0);
			//获取该组织的所有上级组织
			//List<ProviderOrganization> orgList = providerOrganizationService.getOrgListUpwardByOrg(providerOrganization.getId());
			//yuan.yw
			List<SysOrg> orgList = this.sysOrganizationService.getOrgListUpwardByOrg(providerOrganization.getOrgId());
			Map<String,String> map = new LinkedHashMap<String, String>();
			for (int i = 0; i < orgList.size(); i++) {
				SysOrg org = orgList.get(i);
				//List<Staff> staffs = providerOrganizationService.getStaffListDownwardByRoleAndOrg(org.getOrgId(), BizAuthorityConstant.ROLE_CARDISPATCHER);
				//yuan.yw
				List<Map<String,Object>> staffs =this.sysOrganizationService.getProviderStaffByOrgIdAndRoleCode(org.getOrgId(), BizAuthorityConstant.ROLE_CARDISPATCHER, "downward");
				
				for (int j = 0; j < staffs.size(); j++) {
					Map<String,Object> staff = staffs.get(j);
					map.put(staff.get("ACCOUNT")+"", staff.get("ACCOUNT")+"");
					
				}
			}
			if ( map != null && map.size() > 0 ) {
				for (Iterator<String> it = map.keySet().iterator(); it.hasNext() ; ) {
					String key = it.next();
					//发送消息给派车员
					BizMessage bizMessage = new BizMessage();
					bizMessage.setContent(remindersReason);
					bizMessage.setSendTime( new Date());
					bizMessage.setReceivePerson(key);
					bizMessage.setSendPerson(this.sysAccount.getAccount());
					bizMessage.setType(CardispatchConstant.CARDISPATCHMESSAGE_TYPE);
					String link = "op/cardispatch/cardispatchWorkorder!enterCardispatchWorkorder.action?WOID="+WOID;
					bizMessage.setLink(link);
					bizMessage.setTitle(WOID);
					bizMessage.setLinkForMobile("enterCardispatchWorkorderActionForMobile?WOID="+WOID);
					bizMessage.setFunctionType("CardispatchWorkOrder");
					bizMessage.setWoId(WOID);
					bizMessageService.txAddBizMessageService(bizMessage,"hasten");
				}
			}
		}
		
		boolean returnFlag = false;
		if(flag != null && flag.equals("success")){
			returnFlag = true;
		}
		return returnFlag;
	}
	
	
	
	public void formatWorkorder ( Map<String,String> workorder ) {
		//计算总里程
		String useCarMileage = workorder.get("realUseCarMileage");
		String returnCarMileage = workorder.get("realReturnCarMileage");
		if ( !StringUtil.isNullOrEmpty(useCarMileage) && !StringUtil.isNullOrEmpty(returnCarMileage) ) {
			Double realUseCarMileage = Double.valueOf(useCarMileage);
			Double realReturnCarMileage = Double.valueOf(returnCarMileage);
			Double realmileage = realReturnCarMileage - realUseCarMileage;
			workorder.put("totalMileage", realmileage+"");
		}
		//计算GPS里程
		String realUseCarTime = workorder.get("realUseCarTime");
		String realReturnCarTime = workorder.get("realReturnCarTime");
		String pairId = workorder.get("carDriverPairId");
		String carId = workorder.get("carId");
		if ( !StringUtil.isNullOrEmpty(realUseCarTime) && !StringUtil.isNullOrEmpty(realReturnCarTime) ) {
			//获取这段时间内的车辆轨迹集合
			String terminalId = cardispatchCarDao.findTerminalIdByCarId(carId);
			List<Map<String, String>> list = cardispatchTerminalDao.findTerminalGpsList(terminalId, realUseCarTime, realReturnCarTime);
			List<LatLng> latlng_list = new ArrayList<LatLng>(); 
			for (int i = 0; i < list.size() ; i++) {
				Map<String, String> map = list.get(i);
				Double longitude = Double.valueOf(map.get("jingdu"));
				Double latitude = Double.valueOf(map.get("weidu")); 
				LatLng latLng = new LatLngHelper.LatLng(longitude ,latitude );
				latlng_list.add(latLng);
			}
			double totalgpsMileage = LatLngHelper.getDistanceByArray(latlng_list) / 1000.0 ;
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(1);
			nf.setMinimumFractionDigits(1);
			String format_gpsMilage = nf.format(totalgpsMileage);
			if (StringUtil.isNullOrEmpty(format_gpsMilage)) {
				format_gpsMilage = "0";
			}
			//获取用车时间的gps
			if ( !StringUtil.isNullOrEmpty(realUseCarTime) ) {
				Map<String,String> para = new LinkedHashMap<String, String>();
				para.put("carNumber", workorder.get("carNumber"));
				para.put("pickTime", realUseCarTime);
				Map<String, String> useCarGps = cardispatchTerminalDao.findMaxGpsByClientId(terminalId);
				workorder.put("realUseCarLongitude", useCarGps.get("jingdu"));
				workorder.put("realUseCarLatitude", useCarGps.get("weidu"));
			}
			//获取还车时间的gps
			if ( !StringUtil.isNullOrEmpty(realReturnCarTime) ) {
				Map<String,String> para = new LinkedHashMap<String, String>();
				para.put("carNumber", workorder.get("carNumber"));
				para.put("pickTime", realReturnCarTime);
				Map<String, String> returnCarGps = cardispatchTerminalDao.findMaxGpsByClientId(terminalId);
				workorder.put("realReturnCarLongitude", returnCarGps.get("jingdu"));
				workorder.put("realReturnCarLatitude", returnCarGps.get("weidu"));
			}
			workorder.put("totalgpsMileage", format_gpsMilage+"");
		}
		//获取用车人中文名
		String useCarPersonId = workorder.get("useCarPersonId");
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(useCarPersonId);
//		Staff useCarPersonIdStaff = providerOrganizationService.getStaffByAccount(useCarPersonId);
		if ( sysOrgUserByAccount != null ) {
			workorder.put("useCarPersonCName", sysOrgUserByAccount.getName() );
		}
		if ( !workorder.containsKey("creatorName") || StringUtil.isNullOrEmpty(workorder.get("creatorName")) ) {
			//ou.jh
			SysOrgUser creator = this.sysOrgUserService.getSysOrgUserByAccount(workorder.get("creator"));
//			Staff creator = providerOrganizationService.getStaffByAccount(workorder.get("creator"));
			if ( creator != null ) {
				workorder.put("creatorName", creator.getName() );
			}
		}
		//工单状态
		String cNameByStateNum = "";
		if ( workorder.containsKey("workorderStatus") && !StringUtil.isNullOrEmpty(workorder.get("workorderStatus"))) {
			int workorderStatus = Integer.valueOf(workorder.get("workorderStatus"));
			cNameByStateNum = CardispatchWorkorderState.getCNameByStateNum(workorderStatus);
		}
		workorder.put("workorderStateCName", cNameByStateNum );
		//格式化时间
		String planReturnCarTime = workorder.get("planReturnCarTime");
		String planUseCarTime = workorder.get("planUseCarTime");
		
		SimpleDateFormat sdf_string = new SimpleDateFormat("MM/dd HH:mm");
//		if ( !StringUtil.isNullOrEmpty(realUseCarTime) ) {
//			Date realUseCarTime_date = DateUtil.parseDateByString(realUseCarTime);
//			realUseCarTime = sdf_string.format(realUseCarTime_date);
//			workorder.put("realUseCarTime",realUseCarTime);
//		}
//		if ( !StringUtil.isNullOrEmpty(realReturnCarTime) ) {
//			Date realReturnCarTime_date = DateUtil.parseDateByString(realReturnCarTime);
//			realReturnCarTime = sdf_string.format(realReturnCarTime_date);
//			workorder.put("realReturnCarTime",realReturnCarTime);
//		}  
		if ( !StringUtil.isNullOrEmpty(planReturnCarTime) ) {
			Date planReturnCarTime_date = DateUtil.parseDateByString(planReturnCarTime);
			planReturnCarTime = sdf_string.format(planReturnCarTime_date);
			workorder.put("fplanReturnCarTime",planReturnCarTime);
		}
		if ( !StringUtil.isNullOrEmpty(planUseCarTime) ) {
			Date planUseCarTime_date = DateUtil.parseDateByString(planUseCarTime);
			planUseCarTime = sdf_string.format(planUseCarTime_date);
			workorder.put("fplanUseCarTime",planUseCarTime);
		}  
	}
	
	
	/**
	 * 根据调度单woId,获取服务跟踪记录
	 * @param woId - 调度单woId
	 * @return
	 */
	public List<Map<String,String>> findCardispatchTasktracerecordByWoIdForMobile ( String woId ) {
		List<Tasktracerecord> list = taskTracingRecordService.getTaskTracingRecordByKeyService("woId", woId);
		List<Map<String,String>> result_list = new ArrayList<Map<String,String>>();
		for (int i = 0; i < list.size() ; i++) {
			Tasktracerecord tt = list.get(i);
			Map<String, String> map = null;
			try {
				map = ObjectUtil.object2MapString(tt, false);
				//查询人员信息
				String account = map.get("handler");
				//ou.jh
				SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(account);
//				Staff staff = providerOrganizationService.getStaffByAccount(account);
				if ( sysOrgUserByAccount != null ) {
					if ( !StringUtil.isNullOrEmpty(sysOrgUserByAccount.getMobile() )) {
						map.put("handlerPhone", sysOrgUserByAccount.getMobile());
					} else if ( !StringUtil.isNullOrEmpty(sysOrgUserByAccount.getTel() )) {
						map.put("handlerPhone", sysOrgUserByAccount.getTel());
					} else {
						map.put("handlerPhone", "");
					}
				}
				result_list.add(map);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return result_list;
	}
	
	/**
	 * 
	 * @description: 根据条件获取车辆相关信息（分页）
	 * @author：yuan.yw
	 * @param conditionMap
	 * @param indexStart
	 * @param indexEnd
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jul 23, 2013 5:09:03 PM
	 */
	public  List<Map<String,Object>> getCarStateMonitorListByCondition(Map<String,Object> conditionMap,String indexStart,String indexEnd){
		log.info("进入getCarStateMonitorListByCondition(Map<String,Object> conditionMap,String indexStart,String indexEnd)，根据条件获取车辆相关信息（分页）");
		log.info("参数conditionMap："+conditionMap+",indexStart:"+indexStart+",indexEnd:"+indexEnd);		
		Double lat = Double.valueOf(conditionMap.get("latitude")+"");
		Double lng = Double.valueOf(conditionMap.get("longitude")+"");
		List<Map<String,Object>> list =null;
		if(conditionMap.get("distance")!=null){
			int range = Integer.valueOf(conditionMap.get("distance")+"");
			Map areaLeftRightTopBottom = LatLngConversion.getAreaLeftRightTopBottomByRing(lat,lng, 0, range);
			//查询
			list =this.cardispatchCarDao.getCarStateMonitorListByCondition(conditionMap,areaLeftRightTopBottom, indexStart, indexEnd);
		}else{
			//查询
			list =this.cardispatchCarDao.getCarStateMonitorListByCondition(conditionMap,null, indexStart, indexEnd);
		}
		/*for(int distance = 3000;distance<=15000;distance=distance+3000){//最大 15公里范围
			
			Map areaLeftRightTopBottom = LatLngConversion.getAreaLeftRightTopBottomByRing(lat,lng, 0, distance);
			//查询

			list =this.cardispatchCarDao.getCarStateMonitorListByCondition(conditionMap,areaLeftRightTopBottom, indexStart, indexEnd);
			if(list!=null && list.size()>0){
				Map<String,Object> mp = list.get(0);
				int count = Integer.parseInt(mp.get("count")+"");
				Gson gson = new GsonBuilder().create();
				List<Map<String,Object>> carList = (List<Map<String,Object>>)mp.get("carList");
				if(count>0){
					for(Map<String,Object> map:carList){
						String longitude = map.get("longitude")+"";
						String latitude = map.get("latitude")+"";
					//	String address= MapHelper.convertLatlngToAddress(longitude,latitude);
						map.put("location", "");
					}
					break;
				}
			}
		}*/
		log.info("退出getCarStateMonitorListByCondition(Map<String,Object> conditionMap,String indexStart,String indexEnd)，返回结果"+list);
		return list;
	}
	/**
	 * 
	 * @description: 根据车辆id获取相关信息
	 * @author：yuan.yw
	 * @param carId
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jul 24, 2013 10:19:48 AM
	 */
	public  Map<String,Object> getCarRelatedInformationByCarId(String carId){
		Map<String,Object> map = this.cardispatchCarDao.getCarRelatedInformationByCarId(carId);
		if(map!=null && !map.isEmpty()){
			String longitude = map.get("LONGITUDE")+"";
			String latitude = map.get("LATITUDE")+"";
			String address = MapHelper.convertLatlngToAddress(longitude,latitude);
			map.put("currentPosition", address);
		}
		return map;
	}
	
	/***************** getter setter *******************/
	
	
	public CardispatchWorkorderDaoForMobile getCardispatchWorkorderDaoForMobile() {
		return cardispatchWorkorderDaoForMobile;
	}


	public void setCardispatchWorkorderDaoForMobile(
			CardispatchWorkorderDaoForMobile cardispatchWorkorderDaoForMobile) {
		this.cardispatchWorkorderDaoForMobile = cardispatchWorkorderDaoForMobile;
	}



	public WorkManageService getWorkManageService() {
		return workManageService;
	}
	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}
	public CardispatchCarDao getCardispatchCarDao() {
		return cardispatchCarDao;
	}
	public void setCardispatchCarDao(CardispatchCarDao cardispatchCarDao) {
		this.cardispatchCarDao = cardispatchCarDao;
	}
	public TaskTracingRecordService getTaskTracingRecordService() {
		return taskTracingRecordService;
	}
	public void setTaskTracingRecordService(
			TaskTracingRecordService taskTracingRecordService) {
		this.taskTracingRecordService = taskTracingRecordService;
	}
	public CardispatchTerminalDao getCardispatchTerminalDao() {
		return cardispatchTerminalDao;
	}
	public void setCardispatchTerminalDao(
			CardispatchTerminalDao cardispatchTerminalDao) {
		this.cardispatchTerminalDao = cardispatchTerminalDao;
	}
	public BizMessageService getBizMessageService() {
		return bizMessageService;
	}
	public void setBizMessageService(BizMessageService bizMessageService) {
		this.bizMessageService = bizMessageService;
	}


	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}

	public SysAccountService getSysAccountService() {
		return sysAccountService;
	}

	public void setSysAccountService(SysAccountService sysAccountService) {
		this.sysAccountService = sysAccountService;
	}
	
	
	
}
