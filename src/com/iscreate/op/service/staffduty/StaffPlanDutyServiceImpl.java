package com.iscreate.op.service.staffduty;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.dao.staffduty.StaffPlanDutyDao;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrganizationService;


@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class StaffPlanDutyServiceImpl implements StaffPlanDutyService {
	
	/************ 依赖注入 ***********/
	private StaffPlanDutyDao staffPlanDutyDao;

	/***************** 属性 *******************/
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	
	
	
	/****************** service ********************/
	
	/**
	 * 根据业务单元、日期、账号,获取条件符合的排班信息
	 * (请求参数) 	isUser 判断是否查询登录人的排班信息
	 *              dutyDate 日期List集合
	 *  			account 人员账号
	 *  			freId 班次List集合
	 *  			bizId 人员所在组织List集合
	 * @return (Map<String, Map<String, List<Map<String, Object>>>>) 排班信息集合
	 */
	public Map<String, Map<String, List<Map<String, Object>>>> staffNewDutyCalendar (String staffId, String bizId , String name , String date_string ) {
		List<String> subBizUnits = new ArrayList<String>();
		if ( StringUtil.isNullOrEmpty(bizId) ) {
			//获取当前登录人的业务单元
			String userId = (String) SessionService.getInstance().getValueByKey("userId");
			//List<ProviderOrganization> orgByAccountAndOrgTypes = providerOrganizationService.getTopLevelOrgByAccount( userId );
			List<SysOrg> orgByAccountAndOrgTypes = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

			if ( orgByAccountAndOrgTypes != null && orgByAccountAndOrgTypes.size() > 0 ) {
				bizId = orgByAccountAndOrgTypes.get(0).getOrgId()+"";
			} else {
				bizId = "16";
			}
		}

		//有传递业务单元id
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getOrgListDownwardByOrg(Long.valueOf(bizId));
		//yuan.yw
		List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getOrgListDownwardByOrg(Long.valueOf(bizId));
		for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
			SysOrg providerOrganization = orgListDownwardByOrg.get(i);
			if ( providerOrganization != null ) {
				subBizUnits.add(providerOrganization.getOrgId()+"");
			}
		}
		GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd");
		Gson gson = builder.create();
		List<Date> date_array = gson.fromJson(date_string, new TypeToken<List<Date>>(){}.getType());
		//日期集合转换成字符集合
		List<String> changeDate2StringForList = DateUtil.changeDate2StringForList(date_array,"yyyy-MM-dd");
		//查询数据库
		List<Map<String, Object>> list = staffPlanDutyDao.getStaffDutyList(staffId,changeDate2StringForList, subBizUnits, null, name);
		//格式化
		//拼装数据
		Map<String,Map<String,List<Map<String,Object>>>> result_map = new LinkedHashMap<String, Map<String,List<Map<String,Object>>>>();
		//  日期         日晚班	   实例
		for (int i = 0; list != null && i < list.size(); i++) {
			Map<String,Object> duty_basic = list.get(i);
			duty_basic.remove("lastEditTime");
			String dutydate = duty_basic.get("dutyDate")+"";//值班日期
			//日期集合
			Map<String,List<Map<String,Object>>> freq_map = new LinkedHashMap<String, List<Map<String,Object>>>();
			if ( result_map.containsKey(dutydate) ) {
				freq_map = result_map.get(dutydate);
			}
			//班次集合
			String freq_eng = duty_basic.get("freq_eng").toString();//值班日期
			List<Map<String,Object>> be_list = new ArrayList<Map<String,Object>>(); 
			if ( freq_map.containsKey(freq_eng) ) {
				be_list = freq_map.get(freq_eng);
			}
			//实例
			be_list.add(duty_basic);
			freq_map.put(freq_eng, be_list);
			result_map.put(dutydate, freq_map);
		}
		return result_map;
	}


	
	/**
	 * 根据业务单元、日期、账号,获取条件符合的排班信息
	 * (请求参数) 	isUser 判断是否查询登录人的排班信息
	 *              dutyDate 日期List集合
	 *  			account 人员账号
	 *  			freId 班次List集合
	 *  			bizId 人员所在组织List集合
	 * @return (Map<String, Map<String, Map<String, Map<String, Object>>>>) 排班信息集合
	 */
	public Map<String, Map<String, Map<String, Map<String, Object>>>> staffNewDutyList ( String staffId,String bizId , String name , String date_string ) {
		List<String> subBizUnits = new ArrayList<String>();
		if ( StringUtil.isNullOrEmpty(bizId) ) {
			//获取当前登录人的业务单元
			String userId = (String) SessionService.getInstance().getValueByKey("userId");
			//List<ProviderOrganization> orgByAccountAndOrgTypes = providerOrganizationService.getTopLevelOrgByAccount( userId );
			List<SysOrg> orgByAccountAndOrgTypes = this.sysOrganizationService.getTopLevelOrgByAccount(userId);

			if ( orgByAccountAndOrgTypes != null && orgByAccountAndOrgTypes.size() > 0 ) {
				bizId = orgByAccountAndOrgTypes.get(0).getOrgId()+"";
			} else {
				bizId = "16";
			}
		}
		//有传递业务单元id
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getOrgListDownwardByOrg(Long.valueOf(bizId));
		//yuan.yw
		List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getOrgListDownwardByOrg(Long.valueOf(bizId));
		for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
			SysOrg providerOrganization = orgListDownwardByOrg.get(i);
			if ( providerOrganization != null ) {
				subBizUnits.add(providerOrganization.getOrgId()+"");
			}
		}
		GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd");
		Gson gson = builder.create();
		List<Date> date_array = gson.fromJson(date_string, new TypeToken<List<Date>>(){}.getType());
		//日期集合转换成字符集合
		List<String> changeDate2StringForList = DateUtil.changeDate2StringForList(date_array,"yyyy-MM-dd");
		//查询数据库
		List<Map<String, Object>> list = staffPlanDutyDao.getStaffDutyList(staffId,changeDate2StringForList, subBizUnits, null, name);
		//格式化
		//拼装数据
		Map<String,Map<String,Map<String,Map<String,Object>>>> list_map = new LinkedHashMap<String, Map<String,Map<String,Map<String,Object>>>>();
		//	人名			日期			班次
		for (int i = 0; list != null && i < list.size(); i++) {
			Map<String,Object> basicEntity = list.get(i);
			if ( basicEntity.get("staffName") == null ) {
				continue;
			}
			String staffName = basicEntity.get("staffName")+"";
			Map<String,Map<String,Map<String,Object>>> staff_map = new LinkedHashMap<String, Map<String,Map<String,Object>>>();
			if ( list_map.containsKey(staffName) ) {
				staff_map = list_map.get(staffName);
			}
			String dutyDate = basicEntity.get("dutyDate") + "" ;
			
			Map<String,Map<String,Object>> date_map = new LinkedHashMap<String, Map<String,Object>>();
			if ( staff_map.containsKey(dutyDate) ) {
				date_map = staff_map.get(dutyDate);
			}
			String freq_eng = basicEntity.get("freq_eng")+"";
			date_map.put(freq_eng, basicEntity);
			staff_map.put(dutyDate, date_map);
			list_map.put(staffName, staff_map);
		}
		return list_map;
	}
	
	
	/**
	 * 读取人员班次
	 * @return 
	 */
	public List<Map<String, String>> loadStaffDutyFreq () {
		List<Map<String, String>> list = this.staffPlanDutyDao.loadStaffDutyFreq();
		return list;
	}
	
	
	/**
	 * 根据人员姓名,查询人员信息集合
	 * @param staffName -- 人员姓名
	 * @return (List<Map<String, String>>) 人员信息集合
	 */
	public List<Map<String, String>> findStaffListByStaffName ( String staffName , String bizId ) {
		List<String> subBizUnits = new ArrayList<String>();
		if ( !StringUtil.isNullOrEmpty(bizId) ) {
			//有传递业务单元id
			//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getOrgListDownwardByOrg(Long.valueOf(bizId));
			//yuan.yw
			List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getOrgListDownwardByOrg(Long.valueOf(bizId));
			for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
				SysOrg providerOrganization = orgListDownwardByOrg.get(i);
				if ( providerOrganization != null ) {
					subBizUnits .add(providerOrganization.getOrgId()+"");
				}
			}
		} else {
			//没有传递,获取当前登录人的业务单元
			String loginPersonAccount = (String) SessionService.getInstance().getValueByKey("userId");
			//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getTopLevelOrgByAccount(loginPersonAccount);
			List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getTopLevelOrgByAccount(loginPersonAccount);
			for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
				SysOrg providerOrganization = orgListDownwardByOrg.get(i);
				if ( providerOrganization != null ) {
					subBizUnits.add(providerOrganization.getOrgId()+"");
				}
			}
		}
		List<Map<String, String>> list = this.staffPlanDutyDao.findStaffListByStaffName(staffName , subBizUnits);
		return list;
	}
	
	/**
	 * 更新排班
	 * @param dutyDate
	 * @param freId
	 * @param delIds
	 * @param addIds
	 */
	public boolean staffDutyUpdate ( String dutyDate , String freId , List<Map<String, String>> delIds , List<Map<String, String>> addIds ) {
		boolean flag = false;
		List<String> del_account = new ArrayList<String>();
		for (int i = 0; i < delIds.size(); i++) {
			String staffId = delIds.get(i).get("account");
			del_account.add(staffId);
		}
		List<String> add_account = new ArrayList<String>();
		for (int i = 0; i < addIds.size(); i++) {
			String staffId = addIds.get(i).get("account");
			add_account.add(staffId);
		}
		
		//删除值班数据
		for (int i = 0; i < del_account.size(); i++) {
			String account = del_account.get(i);
			Map map = new HashMap();
			map.put("dutyDate",dutyDate);
			map.put("frequencyId",freId);
			map.put("userId",account);
			this.staffPlanDutyDao.deleteStaffDutyTemplate(dutyDate,freId,account);
		}
		
		//添加值班数据
		for (int i = 0; i < add_account.size(); i++) {
			String account = add_account.get(i);
			try {
				List<Map<String, String>> list = this.staffPlanDutyDao.findStaffDutyTemplate(dutyDate,freId,account);
				if ( list != null && list.size() > 0 ) {
					continue;
				}
				Integer num = this.staffPlanDutyDao.saveStaffDutyTemplate(dutyDate,freId,account);
				flag = num > 0 ;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag ;
	}
	
	/**
	 * 根据排班日期,删除排班信息
	 * @param dutyDate - 排班日期
	 * @return (boolean) 操作是否成功
	 */
	public boolean deleteStaffDutyTemplateByDate ( String dutyDate ,String freId ) {
		Integer num = this.staffPlanDutyDao.deleteStaffDutyTemplate(dutyDate,freId,null);
		boolean flag = num > 0 ;
		return flag;
	}
	
	
	
	
	/***************** getter setter *******************/
	
	public StaffPlanDutyDao getStaffPlanDutyDao() {
		return staffPlanDutyDao;
	}
	public void setStaffPlanDutyDao(StaffPlanDutyDao staffPlanDutyDao) {
		this.staffPlanDutyDao = staffPlanDutyDao;
	}
	
	
	
	
	
	
}
