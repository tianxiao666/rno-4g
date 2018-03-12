package com.iscreate.op.dao.routineinspection;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.routineinspection.RoutineinspectionPlanworkorder;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorder;

public class RoutineInspectionTaskDaoImpl implements RoutineInspectionTaskDao{

	private HibernateTemplate hibernateTemplate;
	private Log log = LogFactory.getLog(this.getClass());
	private String className = "com.iscreate.op.dao.routineinspection.RoutineInspectionTaskDaoImpl";
	
	/**
	 * 根据TOID获取任务单信息
	 * @param toId
	 */
	public RoutineinspectionTaskorder getRoutineInspectionTaskByToId(String toId){
		RoutineinspectionTaskorder r = null;
		String hql = "from RoutineinspectionTaskorder r where r.routineinspectionToId='"+toId+"'";
		List<RoutineinspectionTaskorder> list = (List<RoutineinspectionTaskorder>)this.hibernateTemplate.find(hql);
		if(list!=null && list.size()>0){
			r = list.get(0);
		}
		return r;
	}
	
	/**
	 * 保存任务单信息
	 * @param routineinspectionTaskorder
	 * @return
	 */
	public boolean saveRoutineInspectionTaskOrder(RoutineinspectionTaskorder routineinspectionTaskorder){
		boolean isSuccess = true;
		try {
			this.hibernateTemplate.save(routineinspectionTaskorder);
		} catch (Exception e) {
			log.error(this.className+"类里的saveRoutineInspectionTaskOrder方法数据库链接错误");
			isSuccess = false;
		}
		return isSuccess;
	}
	
	/**
	 * 修改任务单信息
	 * @param routineinspectionTaskorder
	 * @return
	 */
	public boolean updateRoutineInspectionTaskOrder(RoutineinspectionTaskorder routineinspectionTaskorder){
		boolean isSuccess = true;
		try {
			this.hibernateTemplate.update(routineinspectionTaskorder);
		} catch (Exception e) {
			log.error(this.className+"类里的updateRoutineInspectionTaskOrder方法数据库链接错误");
			isSuccess = false;
		}
		return isSuccess;
	}
	
	/**
	 * 根据WOID获取任务单信息
	 * @param toId
	 */
	public List<RoutineinspectionTaskorder> getRoutineInspectionTaskByWoId(String woId){
		List<RoutineinspectionTaskorder> list = null;
		try {
			list = (List<RoutineinspectionTaskorder>)this.hibernateTemplate.find("select r from RoutineinspectionTaskorder r where r.routineinspectionWoId=?", woId );
		} catch (Exception e) {
			log.error(this.className+"类里的getRoutineInspectionTaskByWoId方法数据库链接错误");
		}
		return list;
	}
	
	/**
	 * 根据工单号获取平均偏离距离数
	 * @param orgId
	 * @return
	 */
	public float getTaskOrderAvgDeviateByWoId(String woId){
		final String sqlString = "select avg(nvl(DEVIATE,0)) from insp_taskorder it where it.routineinspectionwoid='"+woId+"'";
		Float count = hibernateTemplate.execute(new HibernateCallback<Float>() {
			public Float doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sqlString);
				float num = Float.valueOf(query.uniqueResult()+"");
				return num;
			}
		});
		return count;
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
