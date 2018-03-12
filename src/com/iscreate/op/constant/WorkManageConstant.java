package com.iscreate.op.constant;

public class WorkManageConstant {
	//消息盒子快超时时间定义
	public final static String BIZMSGOVERTIME_PATH="/com/iscreate/op/service/workmanage/util/workManageBizMsgConf.xml";
	
	
	//抢修流程code
	public final static String PROCESS_URGENTREPAIR_WORKORDER_CODE="process_urgentrepair_workorder";
	public final static String PROCESS_URGENTREPAIR_SCENE_CODE="process_urgentrepair_scene";
	public final static String PROCESS_URGENTREPAIR_EXPERT_CODE="process_urgentrepair_expert";
	
	
	//车辆调度流程code
	public final static String PROCESS_CARDISPATCH_WORKORDER_CODE="process_cardispatch_workorder";
	
	
	//巡检流程code
	public final static String PROCESS_ROUTINEINSPECTION_PLAN_CODE="process_routineinspection_plan";
	public final static String PROCESS_ROUTINEINSPECTION_TASK_CODE="process_routineinspection_task";
	
	
	
	//工单类型code
	public final static String WORKORDER_TYPE_URGENTREPAIR="抢修";
	public final static String WORKORDER_TYPE_CARDISPATCH="车辆调度";
	
	
	public final static String QUERYRESULT_KEY="queryResult";
	
	//网络资源常量
	public final static String NETWORKRESOURCE_STATION="Station";
	
	
	public final static String RESOURCE_CAR="car";
	public final static String RESOURCE_PEOPLE="people";
	
	
	//工单状态
	public final static int WORKORDER_DRAFT=1;//工单草稿
	public final static int WORKORDER_WAIT4ACCEPT=2;//待受理
	public final static int WORKORDER_HANDLING=3;//处理中
	public final static int WORKORDER_ASSIGNED=6;	//已派发
	public final static int WORKORDER_END=7;	//已结束
	public final static int WORKORDER_WAIT4CANCEL=4;	//待撤销
	public final static int WORKORDER_CANCELED=5;	//已撤销
	
	public final static int WORKORDER_WAIT4ASSIGNCAR=14;	//待派车
	public final static int WORKORDER_WAIT4USECAR=15;	//待用车
	public final static int WORKORDER_WAIT4RETURNCAR=16;	//待还车
	
	public final static int WORKORDER_EXECUTING=20;	//执行中
	public final static int WORKORDER_CLOSED=21;	//已关闭
	public final static int WORKORDER_DELETE=25;	//已删除
	
	
//	public final static int BIZORDER_DELETE=101;
	
	
	//任务单状态
	public final static int TASKORDER_WAIT4ACCEPT=8;//待受理
	public final static int TASKORDER_HANDLING=9;//处理中
	public final static int TASKORDER_ASSIGNED=10;//已派发
	public final static int TASKORDER_CLOSED=11;//已结束
	public final static int TASKORDER_WAIT4CANCEL=12;	//待撤销
	public final static int TASKORDER_CANCELED=13;	//已撤销
	public final static int TASKORDER_UPGRADING=18;	//升级中
	public final static int TASKORDER_NOTTAKE=19;	//未领取
	
	public final static int TASKORDER_WAIT4INSPECT=22;	//待巡检
	public final static int TASKORDER_INSPECTING=23;	//巡检中
	public final static int TASKORDER_INSPECT_CLOSED=24;	//已关闭
	public final static int TASKORDER_DELETE=26;//已删除
	
}
