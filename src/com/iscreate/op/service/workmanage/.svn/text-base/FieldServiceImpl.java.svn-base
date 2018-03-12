package com.iscreate.op.service.workmanage;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.workmanage.WorkmanageField;

public class FieldServiceImpl implements FieldService {

	
	private static final Log logger = LogFactory.getLog(FieldServiceImpl.class);
	
	private HibernateTemplate hibernateTemplate;
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	
	/**
	 * 根据类型标识返回查询条件类型的字段列表
	 * @param categoryFlag
	 * @return
	 */
	public List<WorkmanageField> getInputFieldsByCategoryFlag(String categoryFlag) {
		List<WorkmanageField> fieldList = null;
		String hql="";
		try {
			hql="select o from WorkmanageField o where o.categoryFlag=? and displayType=1 order by o.displayOrder asc";
			fieldList=(List<WorkmanageField>)this.hibernateTemplate.find(hql,categoryFlag);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取查询条件类型的字段时，有可能数据库链接异常或者执行的sql编写有误，引起该异常");
		}
		
		return fieldList;
	}

	
	/**
	 * 根据类型标识返回查询结果类型的字段列表
	 * @param categoryFlag
	 * @return
	 */
	public List<WorkmanageField> getResultFieldsByCategoryFlag(
			String categoryFlag) {
		List<WorkmanageField> fieldList = null;
		String hql="";
		try {
			hql="select o from WorkmanageField o where o.categoryFlag=? and displayType=2 order by o.displayOrder asc";
			fieldList=(List<WorkmanageField>)this.hibernateTemplate.find(hql,categoryFlag);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取结果字段时，有可能数据库链接异常或者执行的sql编写有误，引起该异常");
		}
		
		return fieldList;
	}
	
}
