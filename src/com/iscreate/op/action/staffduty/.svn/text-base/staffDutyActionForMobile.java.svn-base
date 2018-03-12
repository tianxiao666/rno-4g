package com.iscreate.op.action.staffduty;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.pojo.staffduty.StaffdutyDutyinst;
import com.iscreate.op.pojo.staffduty.StaffdutyDutylog;
import com.iscreate.op.pojo.staffduty.StaffdutyDutytemplate;
import com.iscreate.op.pojo.staffduty.StaffdutyFrequency;
import com.iscreate.op.service.staffduty.StaffDutyService;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageCommunicationHelper;
import com.iscreate.plat.mobile.util.MobilePackageUtil;
import com.iscreate.plat.tools.IdGenerator;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.sso.session.UserInfo;
import com.iscreate.uniqueutil.Unique;

public class staffDutyActionForMobile {
	
	private StaffDutyService staffDutyService;
	
	
	public StaffDutyService getStaffDutyService() {
		return staffDutyService;
	}
	public void setStaffDutyService(StaffDutyService staffDutyService) {
		this.staffDutyService = staffDutyService;
	}


	/**
	 * 显示当前用户值班详细信息
	 * (下班而且未上班，可提交)
	 * (上班后，如果未下班，提交按钮变灰)
	 * @return 
	 * @throws IOException 
	 */
	public void showDutyDetailForMobileAction(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			
			//获取前台传过来的参数
			String dutyDate = "";
			String frequencyId = "";
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			if(formJsonMap!=null){
				dutyDate = formJsonMap.get("dutyDate");
				frequencyId = formJsonMap.get("frequencyId");
			}
			String staffId = (String)request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			Map dutyInfoMap = staffDutyService.getStaffDutyInfo(staffId, dutyDate,frequencyId);
			if (dutyInfoMap != null) {
				mch.addGroup("dutyInfoArea", dutyInfoMap);	//值班信息
			}
			mobilePackage.setContent(mch.mapToJson());
			//返回数据
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
	}
	
	
	/**
	 * 开始值班
	 * @return
	 */
	public void startOnDutyForMobileAction(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			
			//获取前台传过来的参数
			String dutyDate = "";
			String frequencyId = "";
			String dutyTemplateId = "";
			String staffName = "";
			String onDutyAddress = "";
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			if(formJsonMap!=null){
				dutyDate = formJsonMap.get("dutyDate");
				frequencyId = formJsonMap.get("frequencyId");
				dutyTemplateId = formJsonMap.get("dutyTemplateId");
				staffName = formJsonMap.get("staffName");
				onDutyAddress = formJsonMap.get("onDutyAddress");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//若参数为空，则默认获取当天值班信息
			Date curDate = new Date();
			if(dutyDate==null || dutyDate.equals("")){
				dutyDate = sdf.format(curDate);
			}
			//若参数为空，则判断获取某班次信息
			if(frequencyId==null || frequencyId.equals("")){
				if(dutyTemplateId!=null && !"".equals(dutyTemplateId)){
					StaffdutyDutytemplate dutyTemplate = staffDutyService.getDutyTemplateById(dutyTemplateId);
					if(dutyTemplate!=null){
						frequencyId = dutyTemplate.getFrequencyId()+"";
					}
				}
				if(frequencyId==null || frequencyId.equals("")){
					frequencyId = "1";
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String date = sdf.format(curDate)+" 18:00:00";
					Date date2 = sdf2.parse(date);
					if(curDate.after(date2)){
						frequencyId = "2";
					}
				}
			}
			String staffId = (String)request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			
			StaffdutyDutyinst dutyInst = new StaffdutyDutyinst();
			dutyInst.setDutyTemplateId(Long.valueOf(dutyTemplateId));
			dutyInst.setDutyBeginTime(new Date());
			dutyInst.setDutyEndTime(new Date());
			dutyInst.setTakeShiftsBeginTime(new Date());
			dutyInst.setTakeShiftsEndTime(new Date());
			dutyInst.setUserId(staffId);
			dutyInst.setOnDutyAddress(onDutyAddress);
			dutyInst.setOnDutyLatLng("");
			
			//开始值班
			staffDutyService.startDuty(dutyDate,dutyInst);	
			//获取值班信息
			Map dutyInfoMap = staffDutyService.getStaffDutyInfo(staffId, dutyDate,frequencyId);
			
			if (dutyInfoMap != null) {
				if(dutyInfoMap.get("FREQUENCYNAME")==null || dutyInfoMap.get("FREQUENCYNAME").equals("")){
					if(frequencyId.equals("2")){
						dutyInfoMap.put("FREQUENCYNAME", "晚班");
					}else{
						dutyInfoMap.put("FREQUENCYNAME", "白班");
					}
				}
				mch.addGroup("dutyInfoArea", dutyInfoMap);	//值班信息
			}
			mobilePackage.setContent(mch.mapToJson());
			// 返回数据
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
	}
	
	/**
	 * 跳转到添加工作活动记录页面
	 */
	public void loadFillLogPageAction(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			
			//获取前台传过来的参数
			String staffName = "";
			String dutyTemplateId = "";
			String dutyDate = "";
			String frequencyName = "";
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			if(formJsonMap!=null){
				staffName = formJsonMap.get("staffName");
				dutyTemplateId = formJsonMap.get("dutyTemplateId");
				dutyDate = formJsonMap.get("dutyDate");
				frequencyName = formJsonMap.get("frequencyName");
			}
			
			Map logMap = new HashMap();
			logMap.put("staffName", staffName);
			logMap.put("dutyTemplateId", dutyTemplateId);
			logMap.put("dutyDate", dutyDate);
			logMap.put("frequencyName", frequencyName);
			
			mch.addGroup("logInfoArea", logMap);	//值班信息
			mobilePackage.setContent(mch.mapToJson());
			
			// 返回数据
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
	}
	
	
	
	/**
	 * 添加工作活动
	 * @return
	 */
	public void addWorkLogAction(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			
			//获取前台传过来的参数
			String staffName = "";
			String dutyTemplateId = "";
			String workTime = "";
			String workLog = "";
			String workType = "";
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			if(formJsonMap!=null){
				staffName = formJsonMap.get("staffName");
				dutyTemplateId = formJsonMap.get("dutyTemplateId");
				workTime = formJsonMap.get("workTime");
				workLog = formJsonMap.get("workLog");
				workType = formJsonMap.get("workType");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(workTime==null || "".equals(workTime)){
				workTime = sdf.format(new Date());
			}
			if("".equals(dutyTemplateId)||dutyTemplateId==null){
				dutyTemplateId = "0";
			}
			String staffId = (String)request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			
			//添加值班记录
			StaffdutyDutylog dutyLog = new StaffdutyDutylog();
			dutyLog.setDutyTemplateId(Long.valueOf(dutyTemplateId));
			dutyLog.setBeginTime(sdf.parse(workTime));
			dutyLog.setEndTime(sdf.parse(workTime));
			dutyLog.setCostTime(0);
			dutyLog.setUserId(staffId);
			dutyLog.setUserName(staffName);
			dutyLog.setWorkLog(workLog);
			dutyLog.setWorkLogType(workType);
			dutyLog.setLastEditTime(new Date());
			
			staffDutyService.addWorkLog(dutyLog);
			
			mobilePackage.setContent(mch.mapToJson());
			
			// 返回数据
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
	}
	/**
	 * 下班
	 * @return
	 */
	public void offDutyForMobileAction(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			
			MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
			//mobilePackage为空，返回错误信息
			if(mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
			
			//获取前台传过来的参数
			String dutyInstId = "";
			String offDutyAddress = "";
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			if(formJsonMap!=null){
				dutyInstId = formJsonMap.get("dutyInstId");
				offDutyAddress = formJsonMap.get("offDutyAddress");
			}
			String staffId = (String)request.getSession().getAttribute(UserInfo.CAS_FILTER_USERID);
			String offDutyLatLng = "";
			StaffdutyDutyinst dutyInst = new StaffdutyDutyinst();
			dutyInst.setId(Long.valueOf(dutyInstId));
			dutyInst.setUserId(staffId);
			dutyInst.setOffDutyAddress(offDutyAddress);
			dutyInst.setOffDutyLatLng(offDutyLatLng);
			//开始下班
			staffDutyService.offDuty(dutyInst);
			
			mobilePackage.setContent(mch.mapToJson());
			
			// 返回数据
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
		} catch (Exception e) {
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
	}	
	
}
