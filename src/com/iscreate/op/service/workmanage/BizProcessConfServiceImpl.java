package com.iscreate.op.service.workmanage;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.workmanage.WorkmanageBizprocessConf;


public class BizProcessConfServiceImpl implements BizProcessConfService {

	private static final Log logger = LogFactory.getLog(BizProcessConfServiceImpl.class);
	
	private HibernateTemplate hibernateTemplate;
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}



	/**
	 * 根据业务流程Code获取对应的业务流程配置对象
	 * @param bizProcessCode
	 * @return
	 */
	public WorkmanageBizprocessConf getBizProcessConfByProcessCode(String bizProcessCode){
		String hql="";
		WorkmanageBizprocessConf bizProcessConf=null;
		List list=null;
		try {
			hql="select o from WorkmanageBizprocessConf o where o.bizProcessCode=?";
			list=this.hibernateTemplate.find(hql,bizProcessCode);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据业务流程Code获取相应的业务流程配置对象失败，数据库链接异常");
		}
		if(list!=null && !list.isEmpty()){
			bizProcessConf=(WorkmanageBizprocessConf)list.get(0);
		}
		return bizProcessConf;
	}

	
	
}
