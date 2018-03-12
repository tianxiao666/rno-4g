package com.iscreate.op.dao.system;

import java.util.List;
import java.util.Map;

public interface SysUserManagerDao  
{
	/**
	 * 根据姓名/账号 和 部门ID 查询用户列表
	 * @author li.hb
	 * @date 2014-1-9 下午5:47:59
	 * @Description: TODO 
	 * @param @param userName
	 * @param @param orgId
	 * @param @return        
	 * @throws
	 */
	public List<Map<String,Object>> findSysUserManagerListDao(String userName,long orgId,int currentPage,int pageSize);
	
	/**
	 * 根据姓名/账号 和 部门ID 查询用户列表总数
	 * @author li.hb
	 * @date 2014-1-9 下午5:47:59
	 * @Description: TODO 
	 * @param @param userName
	 * @param @param orgId
	 * @param @return        
	 * @throws
	 */
	public int findSysUserManagerCountDao(String userName,long orgId);
}
