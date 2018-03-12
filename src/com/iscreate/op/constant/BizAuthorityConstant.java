package com.iscreate.op.constant;

/**
 * 权限相关的常量
 * */
public class BizAuthorityConstant {

	public final static String USER_GROUP = "user_group";
	public final static String ORG_ROLE = "org_role";
	public final static String BIZ_ROLE = "biz_role";
	
	public final static long USER_GROUP_ID = 2;
	public final static long ORG_ROLE_ID = 3;
	public final static long BIZ_ROLE_ID = 1;

	// 人员角色
	public final static String ROLE_AREAPROJECTMANAGER = "ProjectManager";// 项目经理
	public final static String ROLE_PERSONRESPONSIBLE = "PersonResponsible";// 专业负责人
	public final static String ROLE_TECHNICALSPECIALIST = "TechnicalSpecialist";// 技术专员
	public final static String ROLE_TASKDISPATCHER = "TaskDispatcher";// 任务调度员
	public final static String ROLE_STAFFDISPATCHER = "StaffDispatcher";// 人员调度员
	public final static String ROLE_MAINTENANCEWORKER = "MaintenanceWorker";// 维护人员
	public final static String ROLE_TRAMLEADER = "TeamLeader";// 队长
	public final static String ROLE_TEAMMEMBER = "TeamMember";// 队员
	public final static String ROLE_ADMIN = "ADMIN";// 系统运维人员
	public final static String ROLE_CARDISPATCHER = "CarDispatcher";// 车辆调度员
	public final static String ROLE_DRIVER = "Driver";// 司机
	public final static String ROLE_KEEPER = "WarehouseKeeper";// 仓管员

	
	//资源分类
	public final static String RESOURCEKIND_PAGE="resource_kind_page";//页面类资源
	public final static String RESOURCEKIND_FUNCTION="resource_kind_function";//功能类资源
	public final static String RESOURCEKIND_PROCESS="resource_kind_process";//流程类资源
	//资源子类
	public final static String RESOURCETYPE_PAGE="page";//页面
	public final static String RESOURCETYPE_MENU="menu";//菜单
	public final static String RESOURCETYPE_PAGEAREA="pagearea";//页面块
	public final static String RESOURCETYPE_BUTTON="button";//按钮
	
	public final static String RESOURCETYPE_PORTALPROJECT="portalproject";//门户方案
	public final static String RESOURCETYPE_WORKZONE="workzone";//工作空间
	public final static String RESOURCETYPE_WORKZONESITE="workzonesite";//功能标签
	public final static String RESOURCETYPE_PORTALITEM="portalitem";//门户组件
	public final static String RESOURCETYPE_PROCESS="process";//流程
	public final static String RESOURCETYPE_PROCESSNODE="processnode";//流程节点
}
