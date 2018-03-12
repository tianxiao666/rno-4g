package com.iscreate.op.dao.publicinterface;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.publicinterface.LoginRecord;

public class LoginRecordDaoImpl implements LoginRecordDao{

	private HibernateTemplate hibernateTemplate;
	
	/**
	 * 根据账号获取登陆记录
	 * @param userId
	 * @return
	 */
	public List<LoginRecord> getLoginRecordByUserId(String userId){
		String hql = "from LoginRecord l where l.userId='"+userId+"' order by l.loginTime desc";
		return this.hibernateTemplate.find(hql);
	}
	
	/**
	 * 保存登陆记录
	 * @param loginRecord
	 */
	public void saveLoginRecord(LoginRecord loginRecord){
		this.hibernateTemplate.save(loginRecord);
	}
	
	/**
	 * 修改登陆记录
	 * @param loginRecord
	 */
	public void updateLoginRecord(LoginRecord loginRecord){
		this.hibernateTemplate.update(loginRecord);
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
}
