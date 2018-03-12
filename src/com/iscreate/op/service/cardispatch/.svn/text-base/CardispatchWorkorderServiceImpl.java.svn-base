package com.iscreate.op.service.cardispatch;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.informationmanage.common.ArrayUtil;
import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.BizAuthorityConstant;
import com.iscreate.op.constant.CardispatchConstant;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.constant.CardispatchConstant.CardispatchWorkorderState;
import com.iscreate.op.dao.cardispatch.CardispatchCarDao;
import com.iscreate.op.dao.cardispatch.CardispatchTerminalDao;
import com.iscreate.op.dao.cardispatch.CardispatchWorkorderDao;
import com.iscreate.op.pojo.cardispatch.CardispatchApplyworkorder;
import com.iscreate.op.pojo.cardispatch.CardispatchWorkorder;
import com.iscreate.op.pojo.publicinterface.Tasktracerecord;
import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.service.bizmsg.BizMessageService;
import com.iscreate.op.service.informationmanage.BaseServiceImpl;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.publicinterface.TaskTracingRecordService;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.tools.LatLngHelper;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.tools.LatLngHelper.LatLng;



@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class CardispatchWorkorderServiceImpl extends BaseServiceImpl<CardispatchWorkorder> implements CardispatchWorkorderService {
	
	/************ 依赖注入 ***********/
	CardispatchWorkorderDao cardispatchWorkorderDao;
	WorkManageService workManageService;
	CardispatchCarDao cardispatchCarDao;
	CardispatchTerminalDao cardispatchTerminalDao;
	String loginPerson;
	SysOrgUser loginStaff;
	SysAccount sysAccount;
	private TaskTracingRecordService taskTracingRecordService; 
	private BizMessageService bizMessageService;
	//ou.jh
	private SysOrgUserService sysOrgUserService;
	private SysAccountService sysAccountService;
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
		//当前登录人信息
		this.loginPerson = (String) SessionService.getInstance().getValueByKey("userId");	
		//ou.jh
		this.loginStaff = this.sysOrgUserService.getSysOrgUserByAccount(loginPerson);
		this.sysAccount = this.sysAccountService.getSysAccountByAccount(loginPerson);
//		this.loginStaff = providerOrganizationService.getStaffByAccount(loginPerson);

	}
	
	private Log log = LogFactory.getLog(this.getClass());
	/****************** service *******************/
	
	/**
	 * 车辆调度单 - 根据条件,判断下个跳转页面
	 * @param woId - 工单id
	 * @param requestParamMap - 提交参数
	 * return url - 跳转地址
	 */
	public String enterCardispatchWorkorder( String woId , Map<String,String> requestParamMap ) {
		String workorderType = findCardispatchWorkerorderType(woId);
		String path = "";
		String url = "";
		if( workorderType.equals(CardispatchConstant.CARDISATCHWORKORDER_CSTATE_RETURNCAR) ) {
			//已关闭
			url = path+ "cardispatch_lookupworkorder.jsp?WOID="+woId;
		} else if ( workorderType.equals(CardispatchConstant.CARDISATCHWORKORDER_CSTATE_USECAR) ) {
			//已用车、待还车
			if ( requestParamMap.containsKey("type") && !StringUtil.isNullOrEmpty(requestParamMap.get("type")) ){
				url = path+ "cardispatch_sendCar.jsp?WOID="+woId+"&type="+requestParamMap.get("type");
			} else {
				//判断用车人是否当前登录人
				boolean isUseCarPerson = cardispatchWorkorderDao.checkUseCarPerson(loginPerson,woId);
				if ( isUseCarPerson ) {
					//当前登录人是用车人
					List<String> list = new ArrayList<String>();
					list.add(woId);
					this.bizMessageService.txUpdateBizMessageToHasReadByTitleList(list);
					url = path+ "cardispatch_returnCar.jsp?WOID="+woId;
				} else {
					//当前登录人不是用车人
					url = path+ "cardispatch_lookupworkorder.jsp?WOID="+woId;
				}
			}
		} else if ( workorderType.equals(CardispatchConstant.CARDISATCHWORKORDER_CSTATE_SENDCAR) ) {
			//已派车、待用车
			if ( requestParamMap.containsKey("type") && !StringUtil.isNullOrEmpty(requestParamMap.get("type")) ){
				url = path+ "cardispatch_sendCar.jsp?WOID="+woId+"&type="+requestParamMap.get("type");
			} else {
				//判断用车人是否当前登录人
				boolean isUseCarPerson = cardispatchWorkorderDao.checkUseCarPerson(loginPerson,woId);
				if ( isUseCarPerson ) {
					//当前登录人是用车人
					List<String> list = new ArrayList<String>();
					list.add(woId);
					this.bizMessageService.txUpdateBizMessageToHasReadByTitleList(list);
					url = path+ "cardispatch_useCar.jsp?WOID="+woId;
				} else {
					//当前登录人不是用车人
					url = path+ "cardispatch_lookupworkorder.jsp?WOID="+woId;
				}
			}
		} else if ( workorderType.equals(CardispatchConstant.CARDISATCHWORKORDER_CSTATE_CREATEORDER) ) {
			//已创建、待派车
			//判断当前登录人是否调度员
			boolean flag = this.sysOrgUserService.checkUserRoleByAccountAndCode(loginPerson, BizAuthorityConstant.ROLE_CARDISPATCHER);
			//boolean flag = providerOrganizationService.checkAccountAndRoleCodeService(loginPerson, BizAuthorityConstant.ROLE_CARDISPATCHER);
			if ( flag ) {
				//当前登录人是调度人员
				url = path+ "cardispatch_sendCar.jsp?WOID="+woId;
				//派车页面(陈立斌调用)
				String driverCarId = requestParamMap.get("driverCarId");
				String carNumber = requestParamMap.get("carNumber");
				List<String> list = new ArrayList<String>();
				list.add(woId);
				this.bizMessageService.txUpdateBizMessageToHasReadByTitleList(list);
				if ( !StringUtil.isNullOrEmpty(driverCarId) && !StringUtil.isNullOrEmpty(carNumber) ) {
					url += "&carNumber="+carNumber+"&driverCarId="+driverCarId;
				}
			} else {
				//当前登录人不是调度员
				url = path+ "cardispatch_lookupworkorder.jsp?WOID="+woId;
			}
		}
		
		return url;
	}
	
	
	/**
	 * 根据车辆调度单id , 查询车辆工单状态
	 * @param woId - 车辆调度单id
	 * @return 状态 (中文)
	 */
	public String findCardispatchWorkerorderType ( String woId ) {
		List<Map<String, String>> list = cardispatchWorkorderDao.findCardispatchWorkorderTasktracerecord(woId);
		Map<String, String> handleWay_map = new LinkedHashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = list.get(i);
			String handleWay = map.get("handleWay");
			handleWay_map.put(handleWay, handleWay);
		}
		String workorderType = "";
		if( handleWay_map.containsKey(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_CLOSED) || handleWay_map.containsKey(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_RETURNCAR)) {
			//已还车
			workorderType = CardispatchConstant.CARDISATCHWORKORDER_CSTATE_RETURNCAR;
		} else if ( handleWay_map.containsKey(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_USECAR) ) {
			//已用车
			workorderType = CardispatchConstant.CARDISATCHWORKORDER_CSTATE_USECAR;
		} else if ( handleWay_map.containsKey(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_SENDCAR) ) {
			//已派车
			workorderType = CardispatchConstant.CARDISATCHWORKORDER_CSTATE_SENDCAR;
		} else {
			workorderType = CardispatchConstant.CARDISATCHWORKORDER_CSTATE_CREATEORDER;
		}
		return workorderType;
	}
	
	/**
	 * 根据工单、车辆
	 */
	public List<Map<String,String>> findCardispatchWordorderByState ( Map param_map , String state ) {
		List<Map<String, String>> workorder_list = cardispatchWorkorderDao.findCardispatchWordorderByState(param_map, state);
			for (int j = 0; workorder_list != null && j < workorder_list.size(); j++) {
				Map<String, String> workorder = workorder_list.get(j);
				formatWorkorder(workorder);
			}
		return workorder_list;
	}
	
	
	
	
	/**
	 * 创建车辆调度工单
	 * @param workorderMap - applyDescription useCarPersonId planUseCarAddress planUseCarTime planReturnCarTime criticalClass useCarType
	 * @param apply_param_map
	 * @return (String) 创建的woId
	 */
	public String txCreateCardiaptchWorkorder ( Map<String, String> workorderMap , Map apply_param_map ) {
		//List<ProviderOrganization> list = providerOrganizationService.getTopLevelOrgByAccount(loginPerson);
		//yuan.yw
		List<SysOrg> list = this.sysOrganizationService.getTopLevelOrgByAccount(loginPerson);
		//获取工单提交信息
		String useCarPersonId = workorderMap.get("useCarPersonId");
		String planUseCarAddress = workorderMap.get("planUseCarAddress");
		//保存车辆调度工单
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(useCarPersonId);
//		Staff useCarPerson = providerOrganizationService.getStaffByAccount(useCarPersonId);
		String useCarPersonName = ""; 
		if ( sysOrgUserByAccount != null ) {
			useCarPersonName = sysOrgUserByAccount.getName();
		}
		workorderMap.put("useCarPersonName", useCarPersonName);
		//生成工单title
		String staffName = ""; 
		String contactPhone = "";
		if( sysOrgUserByAccount != null ){
			staffName = sysOrgUserByAccount.getName();
			contactPhone = sysOrgUserByAccount.getTel()==null?"":sysOrgUserByAccount.getTel();
		}
		String workorderTitle = "接"+staffName + "(" + contactPhone+") 到" + planUseCarAddress + "处理故障";
		//公共工单信息
		WorkmanageWorkorder workOrder=new WorkmanageWorkorder();
		workOrder.setWoTitle(workorderTitle);
		workOrder.setWoType(WorkManageConstant.WORKORDER_TYPE_CARDISPATCH);
		workOrder.setCreator(this.loginPerson);
		workOrder.setCreatorName(this.loginStaff.getName());
		if ( list != null && list.size() > 0 ) {
			SysOrg providerOrganization = list.get(0);
			workOrder.setCreatorOrgId(providerOrganization.getOrgId());
		}
		//List<ProviderOrganization> topLevelOrgByAccount = providerOrganizationService.getTopLevelOrgByAccount(loginPerson);
		//yuan.yw
		List<SysOrg> topLevelOrgByAccount = this.sysOrganizationService.getTopLevelOrgByAccount(loginPerson);
		List<String> participantList = new ArrayList<String>();
		if ( topLevelOrgByAccount != null && topLevelOrgByAccount.size() > 0 ) {
			//获取当前登录人最高级别组织
			SysOrg providerOrganization = topLevelOrgByAccount.get(0);
			//获取该组织的所有上级组织
			//List<ProviderOrganization> orgList = providerOrganizationService.getOrgListUpwardByOrg(providerOrganization.getId());
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
			participantList.addAll(map.keySet());
		} else {
			System.err.println("当前人员没配组织");
		}
		//流程处理者
		Tasktracerecord tc = new Tasktracerecord();
		tc.setWoType(WorkManageConstant.WORKORDER_TYPE_CARDISPATCH);
		tc.setHandler(loginPerson);
		tc.setHandlerName(loginStaff.getName());
		String desription = "【用车人】" + workorderMap.get("useCarPersonName") + " , 【用车地点】" + workorderMap.get("planUseCarAddress");
		tc.setHandleResultDescription(desription);
		tc.setHandleTime(new Date());
		tc.setHandleWay(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_CREATE);
		CardispatchWorkorder cardisaptch_workorder = null;
		try {
			cardisaptch_workorder = ObjectUtil.map2Object(workorderMap, CardispatchWorkorder.class);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		//保存公共工单信息
		String woId = workManageService.createCarDispatchWorkOrder(WorkManageConstant.PROCESS_CARDISPATCH_WORKORDER_CODE, workOrder, participantList, cardisaptch_workorder, tc);
//		String woId = workManageService.createWorkOrder(WorkManageConstant.PROCESS_CARDISPATCH_WORKORDER_CODE, workOrder, participantList, null, tc);
		
		workorderMap.put("woId", woId);
		Long id = cardispatchWorkorderDao.saveCardispatchWorkorder(workorderMap);
		//保存关系
		if ( !apply_param_map.containsKey("associateWorkType") || StringUtil.isNullOrEmpty(apply_param_map.get("associateWorkType")+"") ) {
			apply_param_map.put("associateWorkType", CardispatchConstant.CARDISATCHWORKORDER_APPLYTYPE_CAR);
		}
		apply_param_map.put("carWoId", woId);
		apply_param_map.put("id", new StringBuffer( CardispatchConstant.SEQ_APPLY + ".nextval"));
		super.txsave(apply_param_map, CardispatchApplyworkorder.class);
		return woId;
	}
	
	
	public Map<String, String> findSingleCardispatchWorkorder ( Map param_map ) {
		String woId = param_map.get("wo.woId")+"";
		String remove_woId = (String) param_map.remove("wo.woId");
		param_map.put("workorder.woId" , remove_woId);
		//查询工单信息
		Map<String, String> workorder = cardispatchWorkorderDao.findSingleCardispatchWorkorder(param_map);
		//获取服务跟中记录数据
		List<Map<String, String>> tracerecord = cardispatchWorkorderDao.findCardispatchWorkorderTasktracerecord(woId);
		for (int i = 0; i < tracerecord.size(); i++) {
			Map<String, String> map = tracerecord.get(i);
			String handleWay = map.get("handleWay");
			String handleTimeKey = "";
			if ( handleWay.equals(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_CREATE) ) {
				handleTimeKey = "createOrderTime";
			} else if ( handleWay.equals(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_SENDCAR) ) {
				handleTimeKey = "sendCarTime";
			} else if ( handleWay.equals(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_USECAR) ) {
				handleTimeKey = "useCarTime";
			} else if ( handleWay.equals(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_RETURNCAR) ) {
				handleTimeKey = "returnCarTime";
			} else if ( handleWay.equals(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_CLOSED) ) {
				handleTimeKey = "closeOrderTime";
			} else {
				continue;
			}
			Date date = DateUtil.parseDateByString(map.get("handleTime"));
			if ( date != null ) {
				String handleTime = DateUtil.formatDate(date , "MM-dd HH:mm");
				workorder.put(handleTimeKey,handleTime );
			}
		}
		formatWorkorder(workorder);
		ArrayUtil.setMapDefault(workorder, "0.0", "totalMileage","realUseCarMileage","realReturnCarMileage","totalgpsMileage");
		return workorder;
	}
	
	
	/**
	 * 派车
	 * @param param_map - is_pc woId carDriverPairId dispatchDescription carNumber 
	 * @param actionMap - 
	 */
	public Boolean txSendCar ( Map<String, String> param_map , Map<String,Object> actionMap ) {
		String woId = param_map.get("woId");
		Boolean is_pc = Boolean.valueOf(param_map.get("is_pc"));
		Boolean flag = true;
		if ( is_pc ) {
			//派车
			//查询车辆信息
			Map<String,Object> car_map = null;
			if ( param_map.containsKey("carDriverPairId") && param_map.get("carDriverPairId") != null ) {
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("cardriverpairId", param_map.get("carDriverPairId"));
				List<Map<String, Object>> list = cardispatchCarDao.findCarDriverPairList(hashMap);
				if ( list != null && list.size() > 0 ) {
					car_map = list.get(0);
				}
			} else {
				System.err.println("sendCar -- the cardriverpairId is null");
				return false;
			}
			if ( car_map == null || car_map.size() == 0 ){
				System.err.println("sendCar -- the cardriverpairId (" + param_map.get("carDriverPairId") + ") is not exists");
				return false;
			}
			//去除消息
			bizMessageService.updateBizMsgToHasReadByReceivePersonAndOrderIdService(this.loginPerson, woId);
			//添加跟踪记录
			Tasktracerecord tc = new Tasktracerecord();
			tc.setWoId(woId);
			tc.setWoType(WorkManageConstant.WORKORDER_TYPE_CARDISPATCH);
			tc.setHandler(loginPerson);
			tc.setHandlerName(loginStaff.getName());
			String handleDescription = "【是否派车】是, 【选派车辆】"+car_map.get("carNumber")+", 【备注】"+ param_map.get("dispatchDescription") ;
			tc.setHandleResultDescription(handleDescription);
			tc.setHandleTime(new Date());
			tc.setHandleWay(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_SENDCAR);
			//查询用车人
			List<String> participantList = new ArrayList<String>();
			Map<String, String> workorder = cardispatchWorkorderDao.findSingleCardispatchWorkorderByWoId(woId);
			participantList.add(workorder.get("useCarPersonId"));
			//根据woId获取carId
			Map<String, String> workorder_data = cardispatchWorkorderDao.findSingleCardispatchWorkorderByWoId(woId);
			String carId = car_map.get("carId")+"";
			CardispatchWorkorder cardispatchworkorder = null;
			try {
				cardispatchworkorder = ObjectUtil.map2Object(param_map, CardispatchWorkorder.class);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}

			
			boolean assignCar = workManageService.assignCar(woId, this.loginPerson, participantList, tc , carId,cardispatchworkorder);
//			boolean assignCar = workManageService.assignCar(woId, this.loginPerson, participantList, tc , carId);
			
			Map<String,String> where_map = new LinkedHashMap<String, String>();
			where_map.put("woId", param_map.get("woId"));
			param_map.remove("is_pc");
			param_map.remove("carNumber");
			//添加派车人
			param_map.put("sendCarPersonName", this.loginStaff.getName());
			param_map.put("sendCarPersonId", this.sysAccount.getAccount());
			int updateCardispatchWorkorder = cardispatchWorkorderDao.updateCardispatchWorkorder(where_map, param_map);
			flag = updateCardispatchWorkorder > 0 ;
		} else {
			//关闭工单
			Tasktracerecord tc = new Tasktracerecord();
			tc.setWoId(woId);
			tc.setHandlerName(loginStaff.getName());
			tc.setWoType(WorkManageConstant.WORKORDER_TYPE_CARDISPATCH);
			tc.setHandler(loginPerson);
			tc.setHandleResultDescription(param_map.get("dispatchDescription"));
			tc.setHandleTime(new Date());
			tc.setHandleWay(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_CLOSED);
			flag = workManageService.cancelCarDispatchOrder(woId, loginPerson, tc);
		}
		return flag;
	}
	
	
	
	
	/**
	 * 用车
	 * @param param_map - woId realUseCarMileage realUseCarTime
	 * @param actionMap - action数据
	 * @return 操作是否成功
	 */
	public Boolean  txUseCar ( Map<String, String> param_map , Map<String,Object> actionMap ) {
		//"woId","realUseCarMileage","realUseCarTime"
		String address = param_map.get("realUseCarMeetAddress");
		if ( StringUtil.isNullOrEmpty(address) ) {
			param_map.put("realUseCarMeetAddress","");
		}
		String woId = param_map.get("woId");
		String realUseCarTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm");
		param_map.put("realUseCarTime", realUseCarTime);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(1);
		String realUseCarMileage = param_map.get("realUseCarMileage");
		realUseCarMileage = nf.format(Double.valueOf(realUseCarMileage));
		param_map.put( "realUseCarMileage" , realUseCarMileage );
		//去除消息
		bizMessageService.updateBizMsgToHasReadByReceivePersonAndOrderIdService(this.loginPerson, woId);
		//添加服务过程记录
		Tasktracerecord tc = new Tasktracerecord();
		tc.setWoId(woId);
		tc.setHandlerName(loginStaff.getName());
		tc.setWoType(WorkManageConstant.WORKORDER_TYPE_CARDISPATCH);
		tc.setHandler(loginPerson);
		tc.setHandleResultDescription("【用车里程读数】：" + realUseCarMileage + "KM");
		tc.setHandleTime(new Date());
		tc.setHandleWay(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_USECAR);
		//查询用车人
		List<String> participantList = new ArrayList<String>();
		Map<String, String> workorder = cardispatchWorkorderDao.findSingleCardispatchWorkorderByWoId(woId);
		participantList.add(workorder.get("useCarPersonId"));
		boolean flag = workManageService.useCar(woId, this.loginPerson, participantList, tc);
		if ( !flag ) {
			return false;
		}
		Map<String,String> where_map = new LinkedHashMap<String, String>();
		where_map.put("woId", param_map.get("woId"));
		int up = cardispatchWorkorderDao.updateCardispatchWorkorder(where_map, param_map);
		flag = up > 0;
		return flag;
	}
	
	
	public boolean saveFeeAmount ( Map param_map ) {
		String happenTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		param_map.put("happenTime", happenTime);
		param_map.put("confirmPeople", loginPerson);
		Long saveFeerecord = cardispatchWorkorderDao.saveFeerecord(param_map);
		boolean flag = saveFeerecord > 0;
		//添加服务跟踪记录
		Tasktracerecord tasktracerecord = new Tasktracerecord();
		tasktracerecord.setHandler(loginPerson);
		tasktracerecord.setHandleResultDescription(param_map.get("feeType")+":"+param_map.get("feeAmount")+"元");
		tasktracerecord.setHandlerName(this.loginStaff.getName());
		tasktracerecord.setHandleTime(DateUtil.parseDateByString(happenTime));
		tasktracerecord.setHandleWay("用车费用");
		tasktracerecord.setWoId(param_map.get("woId")+"");
		tasktracerecord.setWoType(WorkManageConstant.WORKORDER_TYPE_CARDISPATCH);
		taskTracingRecordService.txSaveTaskTracingRecordService(tasktracerecord );
		return flag;
	}
	
	
	public List<Map<String, String>> findFeerecordListByWoId (  String woId  ) {
		List<Map<String, String>> list = cardispatchWorkorderDao.findFeerecordListByWoId(woId);
		return list;
	}
	
	
	public List<Map<String, String>> findFeerecordList (  Map param_map , String startTime , String endTime ) {
		List<Map<String, String>> list = cardispatchWorkorderDao.findFeerecordList( param_map , startTime , endTime );
		param_map.put("carId", Long.valueOf(param_map.get("carId")+""));
		//计算历时
		for (int i = 0; i < list.size() ; i++) {
			Map<String, String> map = list.get(i);
			computeUseTime(map);
			checkIsOverTime(map);
		}
		return list;
	}
	
	
	public boolean deleteFeeAmount (  Map param_map  ) {
		boolean flag = cardispatchWorkorderDao.deleteFeerecord(param_map);
		return flag;
	}
	
	/**
	 * 还车
	 * @param param_map -- realReturnCarMileage woId 
	 */
	public boolean txReturnCar ( Map<String, String> param_map , Map<String,Object> actionMap ) {
//		woId realReturnCarMileage realReturnCarTime
		String realReturnCarTime = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
		param_map.put("realReturnCarTime", realReturnCarTime);
		String woId = param_map.get("woId");
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(1);
		String realReturnCarMileage = param_map.get("realReturnCarMileage");
		realReturnCarMileage = nf.format(Double.valueOf(realReturnCarMileage));
		param_map.put( "realReturnCarMileage" , realReturnCarMileage );
		
		//添加服务跟踪记录
		Tasktracerecord tc = new Tasktracerecord();
		tc.setWoId(woId);
		tc.setHandlerName(loginStaff.getName());
		tc.setWoType(WorkManageConstant.WORKORDER_TYPE_CARDISPATCH);
		tc.setHandler(loginPerson);
		tc.setHandleResultDescription("【还车里程读数】：" + realReturnCarMileage + "KM" );
		tc.setHandleTime(new Date());
		tc.setHandleWay(CardispatchConstant.CARDISPATCHMESSAGE_HANDLERWAY_RETURNCAR);
		//根据woId获取carId
		Map<String, String> workorder_data = cardispatchWorkorderDao.findSingleCardispatchWorkorderByWoId(woId);
		String carId = workorder_data.get("carId");
		boolean flag = workManageService.returnCar(woId, loginPerson, tc , carId);
		Map<String,String> where_map = new LinkedHashMap<String, String>();
		where_map.put("woId", woId);
		int up = cardispatchWorkorderDao.updateCardispatchWorkorder(where_map, param_map);
		flag = up > 0;
		return flag;
	}
	
	
	
	
	/**
	 * 根据任务单号,获取对应的车辆调度单信息集合
	 * @param toId 任务单号
	 * @param workType 工单的申请类型
	 * @return (List<Map<String,String>>) 相应车辆调度单信息集合
	 */
	public List<Map<String,Object>> findApplyWorkorderByToId ( String woId , String toId , String workType ) {
		List<Map<String, Object>> list = cardispatchWorkorderDao.findApplyWorkorderByToId(woId, toId, workType);
		for (int i = 0; list != null && i < list.size() ; i++) {
			Map<String, Object> workorder = list.get(i);
			
			String wId = workorder.get("woId")+"";
			Map<String, String> workOrderForShow = workManageService.getCarDispatchWorkOrderForShow(wId);
			if ( workOrderForShow != null ) {
				workorder.putAll(workOrderForShow);
			}
			String createTime = workorder.get("createTime")+"";
			if ( createTime != null && !createTime.isEmpty() ) {
				String timeFormatBySecond = TimeFormatHelper.getTimeFormatBySecond(createTime);
				workorder.put("createTime", timeFormatBySecond);
			} else {
				workorder.put("createTime", "");
			}
		}
		return list;
	}
	
	
	
	/**
	 * 根据工单、车辆
	 */
	public List<Map<String,String>> findCardispatchWordorderByStateForSize ( Map param_map , String state , Integer size ) {
		List<Map<String, String>> workorder_list = cardispatchWorkorderDao.findCardispatchWordorderByStateForSize(param_map, state , size );
			for (int j = 0; workorder_list != null && j < workorder_list.size(); j++) {
				Map<String, String> workorder = workorder_list.get(j);
				formatWorkorder(workorder);
			}
		return workorder_list;
	}
	
	
	
	/**************** 计算 ******************/
	
	/**
	 * 计算历时
	 * @param workorder - 调度单对象
	 * @key diffHour
	 */
	public void computeUseTime ( Map<String,String> workorder ) {
		String realUseCarTime = workorder.get("realUseCarTime");
		String realReturnCarTime = workorder.get("realReturnCarTime");
		//历时
		Double diffHour = 0d;
		if ( !StringUtil.isNullOrEmpty(realUseCarTime) && !StringUtil.isNullOrEmpty(realReturnCarTime) ) {
			diffHour = DateUtil.diffHour(DateUtil.parseDateByString(realUseCarTime), DateUtil.parseDateByString(realReturnCarTime));
		}
		workorder.put("diffHour", diffHour+"");
	}
	
	/**
	 * 判断是否超时
	 * @param workorder - 调度单对象
	 * @key isOverTime
	 */
	public void checkIsOverTime ( Map<String,String> workorder ) {
		boolean isOverTime = false;
		if ( workorder.containsKey("planReturnCarTime") && StringUtil.isNullOrEmpty(workorder.get("planReturnCarTime")) ) {
			Date date = DateUtil.parseDateByString(workorder.get("planReturnCarTime"));
			Date now = new Date();
			isOverTime = date.getTime() > now.getTime() ;
		}
		workorder.put("isOverTime", isOverTime+"");
	}
	
	/**
	 * 格式化车辆调度单对象
	 * @param workorder - 工单对象
	 */
	public void formatWorkorder ( Map<String,String> workorder ) {
		//计算总里程
		String useCarMileage = workorder.get("realUseCarMileage");
		Double realmileage = 0d;
		String returnCarMileage = workorder.get("realReturnCarMileage");
		if ( !StringUtil.isNullOrEmpty(useCarMileage) && !StringUtil.isNullOrEmpty(returnCarMileage) ) {
			Double realUseCarMileage = Double.valueOf(useCarMileage);
			Double realReturnCarMileage = Double.valueOf(returnCarMileage);
			realmileage = realReturnCarMileage - realUseCarMileage;
		}
		workorder.put("totalMileage", realmileage+"");
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
				format_gpsMilage = "0.0";
			}
			//获取用车时间的gps
			if ( !StringUtil.isNullOrEmpty(realUseCarTime) ) {
				Map<String,String> para = new LinkedHashMap<String, String>();
				para.put("carNumber", workorder.get("carNumber"));
				para.put("pickTime", realUseCarTime);
				Map<String, String> useCarGps = cardispatchTerminalDao.findMaxGpsByClientId(terminalId);
				if( useCarGps != null ) {
					if ( useCarGps.get("jingdu") != null ) {
						workorder.put("realUseCarLongitude", useCarGps.get("jingdu"));
					}
					if ( useCarGps.get("weidu") != null ) {
						workorder.put("realUseCarLatitude", useCarGps.get("weidu"));
					}
				}
				
			}
			//获取还车时间的gps
			if ( !StringUtil.isNullOrEmpty(realReturnCarTime) ) {
				Map<String,String> para = new LinkedHashMap<String, String>();
				para.put("carNumber", workorder.get("carNumber"));
				para.put("pickTime", realReturnCarTime);
				Map<String, String> returnCarGps = cardispatchTerminalDao.findMaxGpsByClientId(terminalId);
				if ( returnCarGps != null ) {
					if ( returnCarGps.get("jingdu") != null ) {
						workorder.put("realReturnCarLongitude", returnCarGps.get("jingdu"));
					}
					if ( returnCarGps.get("weidu") != null ) {
						workorder.put("realReturnCarLatitude", returnCarGps.get("weidu"));
					}
				}
			}
			workorder.put("totalgpsMileage", format_gpsMilage+"");
		} else {
			workorder.put("totalgpsMileage", "0.0");
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
		
		if ( workorder.containsKey("workorderStatus") && !StringUtil.isNullOrEmpty(workorder.get("workorderStatus"))) {
			int workorderStatus = Integer.valueOf(workorder.get("workorderStatus"));
			String cNameByStateNum = CardispatchWorkorderState.getCNameByStateNum(workorderStatus);
			workorder.put("workorderStateCName", cNameByStateNum );
		}
		//工单类型
		String workorderApplyType = "";
		if ( workorder.get("associateWorkType") != null ) {
			if ( workorder.get("associateWorkType").equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_APPLYTYPE_CAR) ) {
				//车辆申请单
				workorderApplyType = "车辆";
			} else if ( workorder.get("associateWorkType").equalsIgnoreCase(CardispatchConstant.CARDISATCHWORKORDER_APPLYTYPE_URGENTREPAIR) ) {
				//抢修申请单
				workorderApplyType = "抢修";
			} else {
				//车辆申请单
				workorderApplyType = "车辆";
			}
			workorder.put("workorderApplyType", workorderApplyType);
		}
		//计算行车费用总和
		List<Map<String, String>> list = cardispatchWorkorderDao.findFeerecordListByWoId(workorder.get("woId"));
		Double feeSum = 0d;
		for (int i = 0; i < list.size() ; i++) {
			Map<String, String> map = list.get(i);
			if ( map.containsKey("feeAmount") && map.get("feeAmount") != null ) {
				Double feeAmount = Double.valueOf(map.get("feeAmount"));
				feeSum += feeAmount;
			}
		}
		workorder.put("totalFee", feeSum+"");
		//历时
		Double diffHour = 0d;
		if ( !StringUtil.isNullOrEmpty(realUseCarTime) && !StringUtil.isNullOrEmpty(realReturnCarTime) ) {
			diffHour = DateUtil.diffHour(DateUtil.parseDateByString(realUseCarTime), DateUtil.parseDateByString(realReturnCarTime));
		}
		workorder.put("diffHour", diffHour+"");
		String planUseCarTime = workorder.get("planUseCarTime");
		if ( !StringUtil.isNullOrEmpty(planUseCarTime) ) {
			planUseCarTime = DateUtil.formatDateString(planUseCarTime, "yyyy-MM-dd HH:mm");
			workorder.put("planUseCarTime", planUseCarTime);
		}
		String planReturnCarTime = workorder.get("planReturnCarTime");
		if ( !StringUtil.isNullOrEmpty(planReturnCarTime) ) {
			planReturnCarTime = DateUtil.formatDateString(planReturnCarTime, "yyyy-MM-dd HH:mm");
			workorder.put("planReturnCarTime", planReturnCarTime);
		}
		String createTime = workorder.get("createTime");
		if ( !StringUtil.isNullOrEmpty(createTime) ) {
			createTime = DateUtil.formatDateString(createTime, "yyyy-MM-dd HH:mm");
			workorder.put("createTime", createTime);
		}
		realReturnCarTime = workorder.get("realReturnCarTime");
		if ( !StringUtil.isNullOrEmpty(realReturnCarTime) ) {
			realReturnCarTime = DateUtil.formatDateString(realReturnCarTime, "yyyy-MM-dd HH:mm");
			workorder.put("realReturnCarTime", realReturnCarTime);
		}
		realUseCarTime = workorder.get("realUseCarTime");
		if ( !StringUtil.isNullOrEmpty(realUseCarTime) ) {
			realUseCarTime = DateUtil.formatDateString(realUseCarTime, "yyyy-MM-dd HH:mm");
			workorder.put("realUseCarTime", realUseCarTime);
		}
		
	}
	
	
	
	/***************** getter setter *******************/
	public CardispatchWorkorderDao getCardispatchWorkorderDao() {
		return cardispatchWorkorderDao;
	}
	public void setCardispatchWorkorderDao(
			CardispatchWorkorderDao cardispatchWorkorderDao) {
		this.cardispatchWorkorderDao = cardispatchWorkorderDao;
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
