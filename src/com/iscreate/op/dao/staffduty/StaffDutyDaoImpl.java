package com.iscreate.op.dao.staffduty;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.pojo.staffduty.StaffdutyDutyinst;
import com.iscreate.op.pojo.staffduty.StaffdutyDutylog;
import com.iscreate.op.pojo.staffduty.StaffdutyDutytemplate;
import com.iscreate.op.pojo.staffduty.StaffdutyFrequency;
import com.iscreate.plat.tools.TimeFormatHelper;

public class StaffDutyDaoImpl implements StaffDutyDao {

	private HibernateTemplate hibernateTemplate;
	
	
	
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 添加工作日志
	 * @param dutyLog
	 * @return
	 */
	public void addWorkLog(StaffdutyDutylog dutyLog) {
		hibernateTemplate.save(dutyLog);
	}

	/**
	 * 根据值班Id获取人员值班信息
	 * @param dutyInstId
	 * @return
	 */
	public StaffdutyDutyinst getStaffDutyInfoById(String dutyInstId) {
		return hibernateTemplate.get(StaffdutyDutyinst.class, dutyInstId);
	}

	/**
	 * 下班
	 * @param dutyInst
	 * @return
	 */
	public void offDuty(StaffdutyDutyinst dutyInst) {
		Long dutyInstId = dutyInst.getId();
		StaffdutyDutyinst sdInst = hibernateTemplate.get(StaffdutyDutyinst.class, dutyInstId);
		if(sdInst!=null){
			sdInst.setDutyEndTime(new Date());
			sdInst.setRelieveBeginTime(new Date());
			sdInst.setRelieveEndTime(new Date());
			sdInst.setOffDutyAddress(dutyInst.getOffDutyAddress());
			sdInst.setOffDutyLatLng(dutyInst.getOffDutyLatLng());
			hibernateTemplate.update(sdInst);
		}
	}

	/**
	 * 开始值班
	 * @param dutyInst
	 * @return
	 */
	public void startDuty(StaffdutyDutyinst dutyInst) {
		hibernateTemplate.save(dutyInst);
	}

	/**
	 * 获取当前班别
	 * @return
	 */
	public StaffdutyFrequency getCurrentFrequency() {
		//TODO 获取当前班别待修改
		StaffdutyFrequency frequency = new StaffdutyFrequency();
		String frequencyId = "1";
		Date curDate = new Date();
		String date = TimeFormatHelper.getTimeFormatByFree(curDate, "yyyy-MM-dd")+" 18:00:0";
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date2 = sdf2.parse(date);
			if(curDate.after(date2)){
				frequencyId = "2";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return hibernateTemplate.get(StaffdutyFrequency.class, Long.valueOf(frequencyId));
	}

	/**
	 * 获取人员排班信息
	 * @param userId 人员Id
	 * @param dutyDate 排班日期
	 * @param frequencyId 排班班别Id
	 * @return
	 */
	public StaffdutyDutytemplate getStaffDutyInfo(String userId,
			String dutyDate, String frequencyId) {
		StaffdutyDutytemplate dutyTemplate = null;
		String hql = "from StaffdutyDutytemplate where dutyDate=to_date('"+dutyDate+"','yyyy-mm-dd') and userId='"+userId+"' and frequencyId="+frequencyId;
		List<StaffdutyDutytemplate> resList = hibernateTemplate.find(hql);
		if(resList!=null&&!resList.isEmpty()){
			dutyTemplate = resList.get(0);
		}
		return dutyTemplate;
	}

	/**
	 * 通过排班Id获取人员值班信息
	 * @param dutyTemplateId 排班Id
	 * @return
	 */
	public StaffdutyDutyinst getStaffDutyInfoByDutyTemplateId(String dutyTemplateId) {
		StaffdutyDutyinst dutyInst = null;
		String hql = "from StaffdutyDutyinst where dutyTemplateId="+dutyTemplateId;
		List<StaffdutyDutyinst> resList = hibernateTemplate.find(hql);
		if(resList!=null&&!resList.isEmpty()){
			dutyInst = resList.get(0);
		}
		return dutyInst;
	}

	/**
	 * 获取某天的值班工作日志
	 * @param dutyDate
	 * @return
	 */
	public List<StaffdutyDutylog> getStaffDutyLogByDutyDate(String dutyDate) {
		String beginTime = dutyDate+" 00:00:00";
		String endTime = dutyDate+" 23:59:59";
		String hql = "from StaffdutyDutylog where lastEditTime between to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and  to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
		return (List<StaffdutyDutylog>) hibernateTemplate.find(hql);
	}
	/**
	 * 获取某人某天的值班工作日志
	 * @param dutyDate
	 * @param staffId
	 * @return
	 */
	public List<StaffdutyDutylog> getStaffDutyLogByDutyDate(String dutyDate,String staffId) {
		String beginTime = dutyDate+" 00:00:00";
		String endTime = dutyDate+" 23:59:59";
		String hql = "from StaffdutyDutylog where userid='"+staffId+"' and  lastEditTime between to_date('"+beginTime+"','yyyy-mm-dd hh24:mi:ss') and  to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')";
		return (List<StaffdutyDutylog>) hibernateTemplate.find(hql);
	}
	/**
	 * 通过Id获取班别信息
	 * @param frequencyId
	 * @return
	 */
	public StaffdutyFrequency getFrequencyById(String frequencyId) {
		return hibernateTemplate.get(StaffdutyFrequency.class, Long.valueOf(frequencyId));
	}

	/**
	 * 判断当天是否排班
	 * @param accountId
	 * @param dutyDate
	 * @return
	 */
	public boolean checkIsDuty(String accountId,String dutyDate) {
		String hql = "from StaffdutyDutytemplate where to_char(dutyDate,'yyyy-mm-dd')='"+dutyDate+"' and userId='"+accountId+"'";
		List resList = hibernateTemplate.find(hql);
		if(resList!=null&&!resList.isEmpty()){
			return true;
		}
		return false;
	}

	/**
	 * 获取某天排班模版信息
	 * @param dutyTemplateId
	 * @return
	 */
	public StaffdutyDutytemplate getDutyTemplateById(String dutyTemplateId) {
		return hibernateTemplate.get(StaffdutyDutytemplate.class, Long.valueOf(dutyTemplateId));
	}
	
}
