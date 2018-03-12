package com.iscreate.op.service.workmanage;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.workmanage.WorkmanageBizprocessTaskinfoConf;

public class BizprocessTaskinfoConfServiceImpl implements BizprocessTaskinfoConfService {

	
	private static final Log logger = LogFactory.getLog(BizprocessTaskinfoConfServiceImpl.class);
	
	private HibernateTemplate hibernateTemplate;
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/**
	 * 根据流程定义id与流程节点信息，获取对应的节点模板配置信息
	 * @return
	 */
	public WorkmanageBizprocessTaskinfoConf getWorkmanageBizprocessTaskinfoConfByCondition(String pdId,String taskName) {
		String hql="";
		WorkmanageBizprocessTaskinfoConf taskInfoConf=null;
		hql="select o from WorkmanageBizprocessTaskinfoConf o where o.ownerProcessDefineId=? and o.taskName=?";
		List list=null;
		try {
			list=this.hibernateTemplate.find(hql,pdId,taskName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取节点模板配置信息时，数据库链接异常或者执行的sql编写有误");
		}
		if (list != null && !list.isEmpty()) {
			taskInfoConf = (WorkmanageBizprocessTaskinfoConf) list.get(0);
		}
		return taskInfoConf;
	}

}
