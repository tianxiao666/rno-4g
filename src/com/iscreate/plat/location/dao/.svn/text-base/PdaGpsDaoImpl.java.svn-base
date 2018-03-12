package com.iscreate.plat.location.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.plat.location.pojo.PdaClient;
import com.iscreate.plat.location.pojo.PdaGpsLocation;

public class PdaGpsDaoImpl implements PdaGpsDao{

	private HibernateTemplate hibernateTemplate;
	
	/**
	 * 保存GPS
	 * @param pgl
	 */
	public void saveGpsLocation(PdaGpsLocation pgl){
		this.hibernateTemplate.save(pgl);
	}
	
	/**
	 * 保存客户端
	 * @param pc
	 */
	public void saveClient(PdaClient pc){
		this.hibernateTemplate.save(pc);
	}
	
	/**
	 * 根据用户ID获取GPS
	 * @param userId
	 * @return
	 */
	public List<PdaGpsLocation> getGpsLocationByUserId(String userId){
		String hql = "from PdaGpsLocation p where p.userId='"+userId+"' order by p.pickTime desc";
		return (List<PdaGpsLocation>)this.hibernateTemplate.find(hql);
	}

	/**
	 * 根据用户Id获取最近的一个GPS位置信息
	 * @param userId
	 * @return
	 */
	public PdaGpsLocation getLastGpsLocationByUserId(String userId){
		String hql = "from PdaGpsLocation p where p.userId='"+userId+"' and p.pickTime=(select MAX(pgl.pickTime) from PdaGpsLocation pgl where pgl.userId='"+userId+"')";
		List<PdaGpsLocation> list = (List<PdaGpsLocation>)this.hibernateTemplate.find(hql);
		PdaGpsLocation pgl = null;
		if(list!=null && list.size()>0){
			pgl = list.get(0);
		}
		return pgl;
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
}
