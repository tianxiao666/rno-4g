package com.iscreate.op.constant;

/**
 * 权限相关的常量
 * */
public class CardispatchConstant {
	
	public static final String CARDISPATCHMESSAGE_TYPE = "车辆";
	
	public static final String CARDISPATCHMESSAGE_HANDLERWAY_CREATE = "提交申请单";
	public static final String CARDISPATCHMESSAGE_HANDLERWAY_SENDCAR = "派发车辆";
	public static final String CARDISPATCHMESSAGE_HANDLERWAY_USECAR = "使用车辆";
	public static final String CARDISPATCHMESSAGE_HANDLERWAY_RETURNCAR = "归还车辆";
	public static final String CARDISPATCHMESSAGE_HANDLERWAY_CLOSED = "关闭申请单";
	
	
	//调度单状态
	public static final String CARDISATCHWORKORDER_CSTATE_SENDCAR = "已派车";
	public static final String CARDISATCHWORKORDER_CSTATE_USECAR = "用车";
	public static final String CARDISATCHWORKORDER_CSTATE_RETURNCAR = "还车";
	public static final String CARDISATCHWORKORDER_CSTATE_CLOSEORDER = "关闭工单";
	public static final String CARDISATCHWORKORDER_CSTATE_CREATEORDER = "创建工单";
	public static final String CARDISATCHWORKORDER_CSTATE_UNFINISH = "未完成";
	public static final String CARDISATCHWORKORDER_CSTATE_URGE = "催办";
	
	public static final String CARDISATCHWORKORDER_ESTATE_SENDCAR = "sendedCar";					//已派车
	public static final String CARDISATCHWORKORDER_ESTATE_USECAR = "usedCar";					//已用车
	public static final String CARDISATCHWORKORDER_ESTATE_RETURNCAR = "returnedCar";		//已还车
	public static final String CARDISATCHWORKORDER_ESTATE_CLOSEORDER = "closedWorker";			//已完成,已结束
	public static final String CARDISATCHWORKORDER_ESTATE_CREATEORDER = "createdWorkorder";	//已创建
	public static final String CARDISATCHWORKORDER_ESTATE_PENDINGWORKORDER = "pendingWorkOrder";	//待办
	public static final String CARDISATCHWORKORDER_ESTATE_UNFINISH = "unFinish";			//未完成
	
	//调度单的关联类型
	public static final String CARDISATCHWORKORDER_APPLYTYPE_URGENTREPAIR = "urgentrepair";			//抢修
	public static final String CARDISATCHWORKORDER_APPLYTYPE_CAR = "car";			//车辆
	
	
	/**
	 * 车辆状态
	 * @author andy
	 */
	public static enum CardispatchCarState {
		INIT( "待初始化" , 3 ) ,
		RUNNING( "行驶中" , 1 ) , 
		STOP( "静止" , 2 ) , 
		OFFLINE( "离线" , 0 ) ;
		
		private String cName ;
		private int num;
		
		private CardispatchCarState(String cName, int num) {
			this.cName = cName;
			this.num = num;
		}
		
		public static String getCNameByNum ( Integer num ) {
			CardispatchCarState[] values = CardispatchCarState.values();
			for (int i = 0; i < values.length; i++) {
				CardispatchCarState state = values[i];
				if ( state.num == num ) {
					return state.cName;
				}
			}
			return "待初始化";
		}
	}
	
	
	/**
	 * 车辆工单状态
	 * @author andy
	 */
	public static enum CardispatchWorkorderState {
		SENDEDCAR(WorkManageConstant.WORKORDER_WAIT4USECAR,"已派车","sendedCar") , 
		USEDCAR(WorkManageConstant.WORKORDER_WAIT4RETURNCAR,"已用车","usedCar") , 
		RETURNEDCAR(WorkManageConstant.WORKORDER_END,"已关闭","returnedCar") , 
		CLOSEORDER(WorkManageConstant.WORKORDER_END,"已关闭","closedWorkorder") , 
		CREATEORDER(WorkManageConstant.WORKORDER_WAIT4ASSIGNCAR,"已创建","createdWorkorder"),
		CANCELORDER(WorkManageConstant.WORKORDER_CANCELED,"已撤销","cancelWorkorder");
		
		private int stateNum;
		private String cName;
		private String eName;
		
		private CardispatchWorkorderState(int stateNum, String cName,
				String eName) {
			this.stateNum = stateNum;
			this.cName = cName;
			this.eName = eName;
		}
		
		public static String getCNameByStateNum( int stateNum ) {
			CardispatchWorkorderState[] values = CardispatchWorkorderState.values();
			for (int i = 0; i < values.length; i++) {
				CardispatchWorkorderState car = values[i];
				if ( car.stateNum == stateNum ) {
					return car.cName;
				}
			}
			return "";
		}
		
	}
	
	
	
	
	
	
	
	
	public static final String CAR_COLUMNTEXT = " car.id  \"carId\" , " + 
												" car.id  \"id\" ,  " + 
												" car.carModel  \"carModel\" ,  " + 
												" car.carBizId \"carBizId\" ,  " + 
												" car.carNumber \"carNumber\" ,  " + 
												" car.carPic  \"carPic\" ,  " + 
												" car.status \"status\" ,  " + 
												" car.carBrand \"carBrand\" ,  " + 
												" car.carType \"carType\" ,  " + 
												" car.loadWeight \"loadWeight\" ,  " + 
												" car.passengerNumber \"passengerNumber\" ,  " + 
												" car.leasePay \"leasePay\" ,  " + 
												" car.custodyFee \"custodyFee\" ,  " + 
												" car.carAge \"carAge\" ,  " + 
												" car.carMarker \"carMarker\" ,  " + 
												" car.seatCount \"seatCount\" , " +
												" car.longitude \"longitude\" , " +
												" car.latitude \"latitude\" , " +
												" car.lonlat_modify_time \"lonlat_modify_time\" ";
	
	
	
	public static final String TERMINAL_COLUMNTEXT = " terminal.id  \"terminalId\" , " + 
														" terminal.id  \"id\" ,  " + 
														" terminal.terminalName  \"terminalName\" ,  " + 
														" terminal.clientversion \"clientversion\" ,  " + 
														" terminal.terminalComment \"terminalComment\" ,  " + 
														" terminal.terminalState  \"terminalState\" ,  " + 
														" terminal.clientimei \"clientimei\" ,  " + 
														" terminal.telphoneNo \"telphoneNo\" ,  " + 
														" terminal.launchedTime \"launchedTime\" ,  " + 
														" terminal.expiredTime \"expiredTime\" ,  " + 
														" terminal.terminalPic \"terminalPic\" ,  " + 
														" terminal.mobileType \"mobileType\" ,  " + 
														" terminal.stateLastTime \"stateLastTime\" ,  " + 
														" terminal.terminalBizId \"terminalBizId\" ,  " + 
														" terminal.monthlyRent \"monthlyRent\"   " + 
														"";

	public static final String DRIVER_COLUMNTEXT = " driver.id  \"driverId\" , " + 
											" driver.id \"id\" ,  " + 
											" driver.accountId \"accountId\" ,  " + 
											" driver.driverName  \"driverName\" ,  " + 
											" driver.identificationId \"identificationId\" ,  " + 
											" driver.driverPhone  \"driverPhone\" ,  " + 
											" driver.driverLicenseClass \"driverLicenseClass\" ,  " + 
											" driver.driverLicenseNumber \"driverLicenseNumber\" ,  " + 
											" driver.driverStatus \"driverStatus\" ,  " + 
											" driver.driverLicenseType \"driverLicenseType\" ,  " + 
											" driver.drivingOfAge \"drivingOfAge\" ,  " + 
											" driver.driverPic \"driverPic\" ,  " + 
											" driver.driverAddress \"driverAddress\" ,  " + 
											" driver.wage \"wage\" ,  " + 
											" driver.driverAge \"driverAge\" ,  " + 
											" driver.driverBizId \"driverBizId\" ";
	public static final String WORKORDER_COLUMNTEXT = " workorder.id  \"workorderId\" , " + 
											" workorder.woId  \"woId\" ,  " + 
											" workorder.useCarType \"useCarType\" ,  " + 
											" workorder.applyDescription \"applyDescription\" ,  " + 
											" workorder.useCarPersonId  \"useCarPersonId\" ,  " + 
											" workorder.criticalClass \"criticalClass\" ,  " + 
											" workorder.workType \"workType\" ,  " + 
											" workorder.planUseCarTime \"planUseCarTime\" ,  " + 
											" workorder.planUseCarAddress \"planUseCarAddress\" ,  " + 
											" workorder.realUseCarMeetAddress \"realUseCarMeetAddress\" ,  " + 
											" workorder.realUseCarLatitude \"realUseCarLatitude\" ,  " + 
											" workorder.realUseCarLongitude \"realUseCarLongitude\" ,  " + 
											" workorder.realUseCarTime \"realUseCarTime\" ,  " + 
											" workorder.realUseCarMileage \"realUseCarMileage\" ,  " + 
											" workorder.useCarAddressDescription \"useCarAddressDescription\" , " +
											" workorder.carDriverPairId \"carDriverPairId\" , " +
											" workorder.planReturnCarTime \"planReturnCarTime\" , " +
											" workorder.realReturnCarTime \"realReturnCarTime\" , " +
											" workorder.realReturnCarAddress \"realReturnCarAddress\" , " +
											" workorder.realReturnCarLatitude \"realReturnCarLatitude\" , " +
											" workorder.realReturnCarLongitude \"realReturnCarLongitude\" , " +
											" workorder.realReturnCarMileage \"realReturnCarMileage\" , " +
											" workorder.dispatchTime \"dispatchTime\" , " +
											" workorder.dispatchDescription \"dispatchDescription\" , " +
											" workorder.sendCarPersonId \"sendCarPersonId\" , " +
											" workorder.sendCarPersonName \"sendCarPersonName\" , " +
											" workorder.useCarPersonName \"useCarPersonName\"  " +
											"";
	
	public static final String FEECORD_COLUMNTEXT = " fee.id  \"feeId\" , " + 
														" fee.woId  \"woId\" ,  " + 
														" fee.feeType \"feeType\" ,  " + 
														" fee.feeAmount \"feeAmount\" ,  " + 
														" fee.description  \"description\" ,  " + 
														" fee.confirmPeople \"confirmPeople\" ,  " + 
														" fee.happenTime \"happenTime\" ,  " + 
														" fee.feeLongitude \"feeLongitude\" ,  " + 
														" fee.feeLatitude \"feeLatitude\"  " + 
														"";
	
	
	
	public static final String PRO_RESOURCE_COLUMNTEXT = " res.id  \"resId\" , " + 
															" res.areaId  \"areaId\" ,  " + 
															" res.orgId \"orgId\" ,  " + 
															" res.resourceType \"resourceType\" , " + 
															" res.status  \"status\" ,  " + 
															" res.projectId \"projectId\"  " + 
															"";
	
	public static final String SEQ_DUTY = "seq_car_duty";
	public static final String SEQ_CARDRIVERPAIR = "SEQ_CAR_CARDRIVERPAIR";

	public static final String SEQCARTERMINALPAIR = "SEQ_CAR_CARTERMINALPAIR";

	public static final String SEQ_WORKORDER = "SEQ_CAR_WORKORDER";

	public static final String SEQ_APPLY = "SEQ_CAR_APPLYWORKORDER";

	public static final String SEQ_FEERECORD = "SEQ_CAR_FEERECORD";
	
}


