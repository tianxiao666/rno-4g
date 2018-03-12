package com.iscreate.op.dao.system;

import java.util.List;
import java.util.Map;

/** 

* @ClassName: SysUserRelaRoleDao 

* @Description: TODO

* @author zhang.wy1

* @create 2014-1-15 下午12:44:33 

*/ 
public interface SysUserRelaRoleDao {
		/**
		 * @author du.hw
		 * @create 2014-01-14
		 * 根据用户标识得到用户角色
		 * return:所有的角色（通过flag标识出用户是否有角色） 
		 */
		public List<Map<String,Object>> getUserRolesByUserId(long user_id,String system);
        
			/**
			 * 
			 * @description: 删除用户角色关联
			 * @author：zhang.wy1
			 * @param userId
			 * @return: void          
			 * @date：2014-1-15 下午12:44:07
			 */
		public void deleteUserRelaRole(long userId);
		/**
		 * 
		 * @description: 添加用户角色关联
		 * @author：zhang.wy1
		 * @param userId
		 * @return: void          
		 * @date：2014-1-15 下午12:44:07
		 */
        public void addUserRelaRole(long userId,String roleId);
	
}
