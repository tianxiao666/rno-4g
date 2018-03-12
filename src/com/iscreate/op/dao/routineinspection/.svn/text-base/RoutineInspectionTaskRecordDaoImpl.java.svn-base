package com.iscreate.op.dao.routineinspection;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.routineinspection.RoutineinspectionExample;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionPlanworkorder;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionRecordExample;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionRecordTemplate;

public class RoutineInspectionTaskRecordDaoImpl implements RoutineInspectionTaskRecordDao{

	private HibernateTemplate hibernateTemplate;
	private Log log = LogFactory.getLog(this.getClass());
	private String className = "com.iscreate.op.dao.routineinspection.RoutineInspectionTaskRecordDaoImpl";
	
	/**
	 * 保存巡检实例
	 * @param re
	 */
	public boolean saveRoutineInspectionExample(RoutineinspectionExample routineinspectionExample){
		boolean isSuccess = true;
		try {
			this.hibernateTemplate.save(routineinspectionExample);
		} catch (Exception e) {
			log.error(this.className+"类里的saveRoutineInspectionExample方法数据库链接错误");
			isSuccess = false;
		}
		return isSuccess;
	}
	
	/**
	 * 根据资源Id、资源类型、任务单Id获取巡检实例
	 * @param reId
	 * @param reType
	 * @param toId
	 * @return
	 */
	public RoutineinspectionExample getRoutineInspectionExampleByReIdAndReTypeAndToId(String reId,String reType,String toId){
			RoutineinspectionExample re = null;
		try {
			List<RoutineinspectionExample> list = (List<RoutineinspectionExample>)this.hibernateTemplate.find("select r from RoutineinspectionExample r where r.reId=? and r.reType=? and toId=?", reId , reType , toId);
			if(list!=null && list.size()>0){
				re = list.get(0);
			}
		} catch (Exception e) {
			log.error(this.className+"类里的getRoutineInspectionExampleByReIdAndReTypeAndToId方法数据库链接错误");
		}
		return re;
	}
	
	/**
	 * 根据资源Id、资源类型、模板Id和终端类型获取巡检记录模板
	 * @param reId
	 * @param reType
	 * @param templateId
	 * @param terminalType
	 * @return
	 */
	public List<RoutineinspectionRecordTemplate> getRoutineInspectionTemplateByReIdAndReTypeAndTemplateIdAndTerminalType(String reId,String reType,long templateId,String terminalType){
		List list = null;
		try {
			list = this.hibernateTemplate.find("select r from RoutineinspectionRecordTemplate r where r.reType=? and r.rou_temp_id=? and terminalType=?", reType , templateId , terminalType);
		} catch (Exception e) {
			log.error(this.className+"类里的getRoutineInspectionTemplateByReIdAndReTypeAndTemplateIdAndTerminalType方法数据库链接错误");
		}
		return list;
	}
	
	/**
	 * 保存巡检内容
	 * @param re
	 */
	public boolean saveRoutineInspectionRecordExample(RoutineinspectionRecordExample routineinspectionRecordExample){
		boolean isSuccess = true;
		try {
			this.hibernateTemplate.save(routineinspectionRecordExample);
		} catch (Exception e) {
			log.error(this.className+"类里的saveRoutineInspectionRecordExample方法数据库链接错误");
			isSuccess = false;
		}
		return isSuccess;
	}
	
	/**
	 * 根据资源Id、资源类型、任务单Id和巡检内容模板Id获取巡检内容实例
	 * @param reId
	 * @param reType
	 * @param toId
	 * @param templateId
	 * @return
	 */
	public RoutineinspectionRecordExample getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdAndTemplateId(String reId,String reType,String toId,long templateId){
		RoutineinspectionRecordExample rre = null;
		try {
			List<RoutineinspectionRecordExample> list = (List<RoutineinspectionRecordExample>)this.hibernateTemplate.find("select r from RoutineinspectionRecordExample r where r.reId=? and r.reType=? and r.toId=? and record_temp_id=?", reId , reType , toId , templateId);
			if(list!=null && list.size()>0){
				rre = list.get(0);
			}
		} catch (Exception e) {
			log.error(this.className+"类里的getRoutineinspectionRecordExampleByReIdAndReTypeAndToIdAndTemplateId方法数据库链接错误");
		}
		return rre;
	}
	
	/**
	 * 修改巡检内容
	 * @param re
	 */
	public boolean updateRoutineInspectionRecordExample(RoutineinspectionRecordExample routineinspectionRecordExample){
		boolean isSuccess = true;
		try {
			this.hibernateTemplate.update(routineinspectionRecordExample);
		} catch (Exception e) {
			log.error(this.className+"类里的updateRoutineInspectionRecordExample方法数据库链接错误");
			isSuccess = false;
		}
		return isSuccess;
	}
	
	/**
	 * 根据资源Id、资源类型、任务单Id获取巡检内容实例
	 * @param reId
	 * @param reType
	 * @param toId
	 * @return
	 */
	public List<RoutineinspectionRecordExample> getRoutineinspectionRecordExampleByReIdAndReTypeAndToId(String reId,String reType,String toId){
		List<RoutineinspectionRecordExample> list = null;
		try {
			list = (List<RoutineinspectionRecordExample>)this.hibernateTemplate.find("select r from RoutineinspectionRecordExample r where r.reId=? and r.reType=? and r.toId=?", reId , reType , toId );
		} catch (Exception e) {
			log.error(this.className+"类里的getRoutineinspectionRecordExampleByReIdAndReTypeAndToId方法数据库链接错误");
		}
		return list;
	}
	
	//get set=======================================================================================
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
