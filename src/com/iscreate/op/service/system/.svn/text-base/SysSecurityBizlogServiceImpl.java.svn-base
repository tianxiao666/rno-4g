package com.iscreate.op.service.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.iscreate.op.dao.system.SysSecurityBizlogDao;
import com.iscreate.op.pojo.system.SysSecurityBizlog;

public class SysSecurityBizlogServiceImpl implements SysSecurityBizlogService{
	private Log log = LogFactory.getLog(this.getClass());
	public SysSecurityBizlogDao sysSecurityBizlogDao;
	
	
	public SysSecurityBizlogDao getSysSecurityBizlogDao() {
		return sysSecurityBizlogDao;
	}


	public void setSysSecurityBizlogDao(SysSecurityBizlogDao sysSecurityBizlogDao) {
		this.sysSecurityBizlogDao = sysSecurityBizlogDao;
	}


	/**
	 * 保存bizlog
	 * @throws Exception 
	 */
	public boolean txSaveSysSecurityBizlog(SysSecurityBizlog bizlog){
		log.info("进入txSaveBizLog方法");
		log.info("参数bizlog="+bizlog);
		boolean isTrue = true;
		if(bizlog==null){
			isTrue = false;
		}
		this.sysSecurityBizlogDao.saveSysSecurityBizlog(bizlog);
		log.info("执行txSaveBizLog方法成功，实现了”保存bizlog“的功能");
		log.info("退出txSaveBizLog方法,返回boolean为："+isTrue);
		return isTrue;
	}



}
