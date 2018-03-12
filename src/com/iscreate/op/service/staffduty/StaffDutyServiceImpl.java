package com.iscreate.op.service.staffduty;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.dao.staffduty.StaffDutyDao;
import com.iscreate.op.pojo.staffduty.StaffdutyDutyinst;
import com.iscreate.op.pojo.staffduty.StaffdutyDutylog;
import com.iscreate.op.pojo.staffduty.StaffdutyDutytemplate;
import com.iscreate.op.pojo.staffduty.StaffdutyFrequency;
import com.iscreate.plat.tools.TimeFormatHelper;

public class StaffDutyServiceImpl implements StaffDutyService{
	
	private StaffDutyDao staffDutyDao;
	
	

	public StaffDutyDao getStaffDutyDao() {
		return staffDutyDao;
	}

	public void setStaffDutyDao(StaffDutyDao staffDutyDao) {
		this.staffDutyDao = staffDutyDao;
	}

	/**
	 * 添加工作活动日志
	 * @param dutyLog
	 * @return
	 */
	public void addWorkLog(StaffdutyDutylog dutyLog) {
		staffDutyDao.addWorkLog(dutyLog);
	}

	/**
	 * 获取人员值班信息
	 * @param staffId 人员Id
	 * @param dutyDate 值班日期
	 * @param frequencyId 班次
	 * @return
	 */
	public Map getStaffDutyInfo(String staffId, String dutyDate,String frequencyId) {
		Map resultMap = new HashMap();
		String FREQUENCYNAME = "";
		//若参数为空，则默认获取当天值班信息
		if(dutyDate==null || dutyDate.equals("")){
			dutyDate = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd");
		}
		//若参数为空，则判断获取某班次信息
		if(frequencyId==null || frequencyId.equals("")){
			StaffdutyFrequency frequency = this.getCurrentFrequency();
			FREQUENCYNAME = frequency.getFrequencyName();
			if(frequency!=null){
				frequencyId = frequency.getId()+"";
			}else{
				frequencyId = "1";
			}
		}
		//获取人员排班信息
		StaffdutyDutytemplate dutyTemplate = staffDutyDao.getStaffDutyInfo(staffId, dutyDate, frequencyId);
		if(dutyTemplate!=null){
			try {
				resultMap = ObjectUtil.object2Map(dutyTemplate, false);
			} catch (Exception e1) {
				e1.printStackTrace();
			} 
			String dutyTemplateId = resultMap.get("id")+"";
			resultMap.put("dutyTemplateId", dutyTemplateId);
			//获取人员值班信息
			StaffdutyDutyinst dutyInst = staffDutyDao.getStaffDutyInfoByDutyTemplateId(dutyTemplateId);
			if(dutyInst!=null){
				try {
					Map<String, Object> map = ObjectUtil.object2Map(dutyInst, false);
					resultMap.putAll(map);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				Date RELIEVEBT = dutyInst.getRelieveBeginTime();
				if(!"".equals(RELIEVEBT)&&RELIEVEBT!=null){
					//已经下班,按钮不可用
					resultMap.put("canOffDuty", false);
					resultMap.put("relieveBeginTime",TimeFormatHelper.getTimeFormatByFree(RELIEVEBT, "yyyy-MM-dd HH:mm:ss"));
				}else{
					//已经上班可以下班
					resultMap.put("canOffDuty", true);
				}
				Date TAKESHIFTSBT = dutyInst.getTakeShiftsBeginTime();
				if(TAKESHIFTSBT!=null&&!"".equals(TAKESHIFTSBT)){
					resultMap.put("takeShiftsBeginTime", TimeFormatHelper.getTimeFormatByFree(TAKESHIFTSBT, "yyyy-MM-dd HH:mm:ss"));
				}
				//保存值班实例Id
				resultMap.put("dutyInstId", dutyInst.getId());
			}else{
				//可以上班
				Date wanban = new Date();
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					wanban = sdf2.parse(TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd")+" 18:00:00");
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Date curTime = new Date();
				if(frequencyId.equals("1")&&curTime.before(wanban)){
					resultMap.put("canOnDuty", true);
				}
				if(frequencyId.equals("2")&&curTime.after(wanban)){
					resultMap.put("canOnDuty", true);
				}
			}
			StaffdutyFrequency fc = staffDutyDao.getFrequencyById(frequencyId);
			if(fc!=null){
				String beginTime = dutyDate+" "+fc.getBeginTime();
				String endTime = dutyDate+" "+fc.getEndTime();
				resultMap.put("STARTTIME", beginTime);
				resultMap.put("ENDTIME", endTime);
				if(frequencyId.equals("2")){
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					try {
						cal.setTime(sdf3.parse(endTime));
					} catch (Exception e) {
						e.printStackTrace();
					}
					cal.add(Calendar.DATE, 1);
					endTime = sdf3.format(cal.getTime());
					resultMap.put("ENDTIME", endTime);
				}
				FREQUENCYNAME = fc.getFrequencyName();
			}
			//判断是否为当天值班信息
			String curDate = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd");
			if(curDate.equals(dutyDate)){
				resultMap.put("canDuty", true);
			}else{
				resultMap.put("canDuty", false);
			}
		}
		if("".equals(FREQUENCYNAME)){
			StaffdutyFrequency fc = staffDutyDao.getFrequencyById(frequencyId);
			FREQUENCYNAME = fc.getFrequencyName();
		}
		resultMap.put("FREQUENCYNAME", FREQUENCYNAME);
		//获取工作活动日志
		List<StaffdutyDutylog> dutyLogList = staffDutyDao.getStaffDutyLogByDutyDate(dutyDate,staffId);
		if(dutyLogList!=null){
			resultMap.put("workActivityList", dutyLogList);
		}
		resultMap.put("dutyDate", dutyDate);
		return resultMap;
	}

	/**
	 * 指定用户下班
	 * @param dutyInst
	 * @return
	 */
	public void offDuty(StaffdutyDutyinst dutyInst) {
		staffDutyDao.offDuty(dutyInst);
	}

	/**
	 * 开始值班
	 * @param dutyDate 值班日期
	 * @param dutyInst 值班信息
	 * @return
	 */
	public void startDuty(String dutyDate, StaffdutyDutyinst dutyInst) {
		staffDutyDao.startDuty(dutyInst);
	}

	/**
	 * 获取当前班别
	 * @return
	 */
	public StaffdutyFrequency getCurrentFrequency() {
		return staffDutyDao.getCurrentFrequency();
	}

	/**
	 * 获取某一天的值班工作日志
	 * @param dutyDate 值班日期
	 * @return
	 */
	public List<StaffdutyDutylog> getStaffDutyLogByDutyDate(String dutyDate) {
		return staffDutyDao.getStaffDutyLogByDutyDate(dutyDate);
	}

	/**
	 * 判断当天是否排班
	 * @param accountId
	 * @return
	 */
	public boolean checkIsDutyToday(String accountId) {
		String dutyDate = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM-dd");
		return staffDutyDao.checkIsDuty(accountId, dutyDate);
	}

	/**
	 * 获取某天排班模版信息
	 * @param dutyTemplateId
	 * @return
	 */
	public StaffdutyDutytemplate getDutyTemplateById(String dutyTemplateId) {
		StaffdutyDutytemplate dutyTemplate = staffDutyDao.getDutyTemplateById(dutyTemplateId);
		return dutyTemplate;
	}

}
