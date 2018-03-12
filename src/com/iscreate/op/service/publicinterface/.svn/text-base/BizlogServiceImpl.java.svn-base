package com.iscreate.op.service.publicinterface;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.iscreate.op.dao.publicinterface.BizlogDao;
import com.iscreate.op.pojo.publicinterface.Bizlog;

public class BizlogServiceImpl implements BizlogService{
	private Log log = LogFactory.getLog(this.getClass());
	public BizlogDao bizlogDao;
	
	/**
	 * 保存bizlog
	 * @throws AuthorityException 
	 */
	public boolean txSaveBizLog(Bizlog bizlog){
		log.info("进入txSaveBizLog方法");
		log.info("参数bizlog="+bizlog);
		boolean isTrue = true;
		if(bizlog==null){
			isTrue = false;
		}
		this.bizlogDao.saveBizlog(bizlog);
		log.info("执行txSaveBizLog方法成功，实现了”保存bizlog“的功能");
		log.info("退出txSaveBizLog方法,返回boolean为："+isTrue);
		return isTrue;
	}

	public BizlogDao getBizlogDao() {
		return bizlogDao;
	}

	public void setBizlogDao(BizlogDao bizlogDao) {
		this.bizlogDao = bizlogDao;
	}

}
