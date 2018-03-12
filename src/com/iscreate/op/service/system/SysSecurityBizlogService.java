package com.iscreate.op.service.system;

import com.iscreate.op.pojo.system.SysSecurityBizlog;


public interface SysSecurityBizlogService {
	
	/**
	 * 保存bizlog
	 */
	public boolean txSaveSysSecurityBizlog(SysSecurityBizlog bizlog);
	
}
