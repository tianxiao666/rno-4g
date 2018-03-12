package com.iscreate.plat.system.util;

public interface AuthorityConstant {

	/**
	 * session 常量
	 * @author sunny
	 *
	 */
	public interface SessionConstant{
		public final String ACCOUNT_KEY="_account_";
	}
	/**
	 * 角色约束关系
	 * @author sunny
	 *
	 */
	public interface RoleConstrainCode{
		public final int PRE_MUST=1;//角色1是角色2的前提角色
		public final int EXCLUSION=2;//角色1和角色2不能并存
	}
	
	/**
	 * 操作码
	 * @author sunny
	 *
	 */
	public interface OperationCode{
		public final String VIEW="view";//查看
		public final String EDIT="edit";//编辑
		public final String DELETE="delete";//删除
		public final String ADD="add";//新增
		public final String FORBIDDEN="forbidden";//禁止
	}
	
	/**
	 * 权限的约束关系
	 * @author sunny
	 *
	 */
	public interface PrevilidgeConstrainCode{
		public final int PRE_MUST=1;//权限1和权限2的前提角色
		public final int EXCLUSION=2;//权限1和权限2不能并存
	}
	
	/**
	 * 密码检查结果
	 * @author sunny
	 *
	 */
	public interface CheckPasswordResult{
		public final int CHECKOK=0;//检查通过
		public final int NOTEXIST=1;//不存在此账号
		public final int NOTVALID=2;//账号的状态无效
		public final int OUTOFTIMERANGE=3;//账号不在有效期内
		public final int PASSWORDNOTMEET=4;//账号密码不对
	}
	
	/**
	 * 账号状态
	 * @author sunny
	 *
	 */
	public interface AccountStatus{
		public final int VALID=1;//有效
		public final int NOTENABLE=0;//未启用
		public final int LOCKED=-1;//被锁
		public final int UNAVAILABLE = 2;//不可用
		public final int AVAILABLE = 3;//可用
	}
}
