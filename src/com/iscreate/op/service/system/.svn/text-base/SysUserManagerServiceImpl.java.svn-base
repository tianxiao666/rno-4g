package com.iscreate.op.service.system;

import java.util.List;
import java.util.Map;

import com.iscreate.op.dao.common.BaseDao;
import com.iscreate.op.dao.system.SysUserManagerDao;

public class SysUserManagerServiceImpl  implements SysUserManagerService 
{
	
	private SysUserManagerDao sysUserManagerDao;
	

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
	public List<Map<String,Object>> findSysUserManagerListService(String userName,long orgId,int currentPage,int pageSize)
	{
		
		List<Map<String, Object>> list = sysUserManagerDao.findSysUserManagerListDao(userName, orgId,currentPage,pageSize);
		
		return list;
	}
	
	

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
	public int findSysUserManagerCountService(String userName,long orgId)
	{
		return sysUserManagerDao.findSysUserManagerCountDao(userName, orgId);
	}


	public SysUserManagerDao getSysUserManagerDao() {
		return sysUserManagerDao;
	}


	public void setSysUserManagerDao(SysUserManagerDao sysUserManagerDao) {
		this.sysUserManagerDao = sysUserManagerDao;
	}
}
