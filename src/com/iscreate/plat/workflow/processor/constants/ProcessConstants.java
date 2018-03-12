package com.iscreate.plat.workflow.processor.constants;

public interface ProcessConstants {

	/**
	 * 记录主体类型
	 * @author brightming
	 *
	 */
	public interface RecordObjectType{
		//流程
		public final static String PROCESS="process";
		//任务
		public final static String TASK="task";
	}
	/**
	 * 启动流程的主体类型
	 * @author brightming
	 *
	 */
	public interface StarterType{
		
		
		/**
		 * 人
		 */
		public final static String STARTER_PEOPLE="_USER_";
		/**
		 * 某个任务
		 */
		public final static String STARTER_TASK="_TASK_";
		/**
		 * 某个流程
		 */
		public final static String STARTER_PROCESS="_PROCESS_";
	}
	
	/**
	 * 流程变量里的保留字
	 * @author brightming
	 *
	 */
	public interface VariableKey{
		//任务接收者类型
		public final static String ASSIGNEE_TYPE="_ASSIGNEE_TYPE_";
		//任务接收者id
		public final static String ASSIGNEE_ID="_ASSIGNEE_ID_";
	}
	
	/**
	 * 任务状态
	 * @author brightming
	 *
	 */
	public interface TaskStatus{
		public final static String INIT="未领取";
//		public final static String BEEN_TAKEN="已领取";//已分配
		public final static String HANDLING="处理中";//已受理
		public final static String COMPLETED="已完成";
		public final static String SUSPENDED="暂停";
		public final static String CANCELED="已撤销";
//		public final static String STOPPED="已停止";
	}
	
	/**
	 * 流程实例状态
	 * @author brightming
	 *
	 */
	public interface InstanceStatus{
		public final static String CANCEL="cancel";//撤销
		public final static String END="ended";//正常结束
		public final static String RUNNING="running";//暂停
		public final static String PAUSE="pause";//暂停
	}
	
	/**
	 * 流程实例的动作
	 * @author brightming
	 *
	 */
	public interface InstanceActionType{
		public final static int CREATE=0;//创建
		public final static int PAUSE=1;//暂停
		public final static int RESUME=2;//唤醒
		public final static int CANCEL=3;//撤销
		public final static int END=4;//结束
	}
	
	/**
	 * 接收任务的主体类型
	 * @author brightming
	 *
	 */
	public interface AcceptorType{
		public final static String ACCEPTOR_PEOPLE="_people_";//人员
		public final static String ACCEPTOR_ORGROLE="_orgrole_";//组织角色
		public final static String ACCEPTOR_BIZROLE="_bizrole_";//业务角色
	}
	
	/**
	 * 处理任务的动作类型
	 * @author brightming
	 *
	 */
	public interface TaskActionType{
		public final static int CREATE=0;//创建
		public final static int TAKE_TASK=1;//领取
		public final static int RETURN_TASK=2;//退回
		public final static int REJECT_TASK=3;//驳回
		public final static int CONFIRM_REJECT=4;//确认驳回
		public final static int DENY_REJECT=5;//否决驳回
		public final static int CANCEL_TASK=6;//撤销
		public final static int COMPLETE=7;//结束
		public final static int CASCADE_CANCELED_BY_INSTANCE=8;//因流程撤销而撤销
		public final static int REASSIGEN=9;//转派
	}
	
}
