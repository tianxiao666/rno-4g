package com.iscreate.op.dao.routineinspection;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.routineinspection.RoutineinspectionPlanworkorder;

public class RoutineInspectionPlanDaoImpl implements RoutineInspectionPlanDao {

	
	
	private static final Log logger = LogFactory.getLog(RoutineInspectionPlanDaoImpl.class);
	
	private HibernateTemplate hibernateTemplate;
	

	/**
	 * 保存巡检计划工单对象
	 * @param routineinspectionPlanworkorder
	 * @return
	 */
	public boolean saveRoutineInspectionPlanWorkOrder(RoutineinspectionPlanworkorder routineinspectionPlanworkorder){
		boolean isSuccess=false;
		try {
			this.hibernateTemplate.save(routineinspectionPlanworkorder);
			isSuccess=true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存巡检计划工单对象时，数据库链接错误");
		}
		
		return isSuccess;
	}
	
	
	/**
	 * 更新巡检计划工单对象
	 * @param routineinspectionPlanworkorder
	 * @return
	 */
	public boolean updateRoutineInspectionPlanWorkOrder(RoutineinspectionPlanworkorder routineinspectionPlanworkorder){
		boolean isSuccess=false;
		try {
			this.hibernateTemplate.update(routineinspectionPlanworkorder);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新巡检计划工单对象时，数据库链接错误");
		}
		return isSuccess;
	}
	
	
	
	/**
	 * 根据巡检计划工单id，获取对应的巡检计划对象
	 * @param woId
	 * @return
	 */
	public RoutineinspectionPlanworkorder getRoutineinspectionPlanworkorderByWoId(String routineinspectionWoId){
		RoutineinspectionPlanworkorder routineinspectionPlanworkorder=null;
		
		try {
			List list=this.hibernateTemplate.find("select o from RoutineinspectionPlanworkorder o where o.routineinspectionWoId=?", routineinspectionWoId);
			if(list!=null && !list.isEmpty()){
				routineinspectionPlanworkorder=(RoutineinspectionPlanworkorder)list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据巡检计划工单id，获取对应的巡检计划对象的时候，数据库链接错误");
		}
		return routineinspectionPlanworkorder;
	}
	
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
