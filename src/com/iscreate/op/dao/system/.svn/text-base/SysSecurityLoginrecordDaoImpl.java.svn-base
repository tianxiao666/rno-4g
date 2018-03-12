package com.iscreate.op.dao.system;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.system.SysSecurityLoginrecord;

public class SysSecurityLoginrecordDaoImpl implements SysSecurityLoginrecordDao{

	private HibernateTemplate hibernateTemplate;
	
	/**
	 * 根据账号获取登陆记录
	* @author ou.jh
	* @date Jun 9, 2013 10:01:21 AM
	* @Description: TODO 
	* @param @param userId
	* @param @return        
	* @throws
	 */
	public List<SysSecurityLoginrecord> getSysSecurityLoginrecordByUserId(String userId){
		String hql = "from SysSecurityLoginrecord l where l.userId='"+userId+"' order by l.loginTime desc";
		return this.hibernateTemplate.find(hql);
	}
	
	/**
	 * 保存登陆记录
	* @author ou.jh
	* @date Jun 9, 2013 10:01:26 AM
	* @Description: TODO 
	* @param @param sysSecurityLoginrecord        
	* @throws
	 */
	public void saveSysSecurityLoginrecord(SysSecurityLoginrecord sysSecurityLoginrecord){
		this.hibernateTemplate.save(sysSecurityLoginrecord);
	}
	
	/**
	 * 修改登陆记录
	* @author ou.jh
	* @date Jun 9, 2013 10:01:33 AM
	* @Description: TODO 
	* @param @param sysSecurityLoginrecord        
	* @throws
	 */
	public void updateSysSecurityLoginrecord(SysSecurityLoginrecord sysSecurityLoginrecord){
		this.hibernateTemplate.update(sysSecurityLoginrecord);
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
}
