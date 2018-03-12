package com.iscreate.op.service.cardispatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.CardispatchConstant;
import com.iscreate.op.dao.cardispatch.CardispatchDutyDao;
import com.iscreate.op.pojo.cardispatch.CardispatchOndutydrivercarpair;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.informationmanage.BaseServiceImpl;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.plat.tools.TimeFormatHelper;


@SuppressWarnings({ "unused", "unchecked" })
public class CardispatchDutyServiceImpl extends BaseServiceImpl<CardispatchOndutydrivercarpair> implements CardispatchDutyService {
	
	/************ 依赖注入 **************/
	CardispatchDutyDao cardispatchDutyDao;
	NetworkResourceInterfaceService networkResourceInterfaceService;
	
	/************ 属性 **************/
	private Log log = LogFactory.getLog(this.getClass());
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}

	/********************** service ***********************/
	
	/**
	 * 根据业务单元、日期、账号,获取条件符合的排班信息
	 * @param dutyDate 日期List集合
	 * @param carId 车辆id
	 * @param freId 班次List集合
	 * @param carBizId 车辆所在组织List集合
	 * @return 排班信息集合
	 */
	public Map<String,Map<String,List<Map<String,Object>>>> cardispatchDutyCalendar ( Map<String, String> requestParamMap ) {
		String date_string = requestParamMap.get("date");
		String carNumber_string = requestParamMap.get("carNumber");
		String freId_string = requestParamMap.get("freId");
		String carId_string = requestParamMap.get("carId");
		String carBizId_string = requestParamMap.get("carBizId");
		//业务单元
		List<String> userBizId_list = new ArrayList<String>();
		if ( StringUtil.isNullOrEmpty(carBizId_string) ) {
			//获取当前登录人的业务单元
			String accountId = (String) SessionService.getInstance().getValueByKey("userId");
			//List<ProviderOrganization> orgByAccountAndOrgTypes = providerOrganizationService.getTopLevelOrgByAccount( accountId );
			List<SysOrg> orgByAccountAndOrgTypes = this.sysOrganizationService.getTopLevelOrgByAccount( accountId );
			if ( orgByAccountAndOrgTypes != null && orgByAccountAndOrgTypes.size() > 0 ) {
				carBizId_string = orgByAccountAndOrgTypes.get(0).getOrgId()+"";
			} else {
				carBizId_string = "16";
			}
		}
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getOrgListDownwardByOrg(Long.valueOf(carBizId_string));
		//yuan.yw
		List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getOrgListDownwardByOrg(Long.valueOf(carBizId_string));
		for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
			SysOrg providerOrganization = orgListDownwardByOrg.get(i);
			userBizId_list.add(providerOrganization.getOrgId()+"");
		}
		//日期集合转换成字符集合
		GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd");
		Gson gson = builder.create();
		List<Date> date_array = gson.fromJson(date_string, new TypeToken<List<Date>>(){}.getType());
		List<String> date_list = DateUtil.changeDate2StringForList(date_array,"yyyy-MM-dd");
		//组装参数
		Map param_map = new HashMap();
		param_map.put("dutyDate", date_list);//     
		param_map.put("carNumber", carNumber_string);
		param_map.put("carBizId", userBizId_list);
		param_map.put("freId", freId_string);
		param_map.put("carId", carId_string);
		List<Map<String, Object>> carDutyList = cardispatchDutyDao.getCarDutyList(param_map);
		
		//拼装数据
		Map<String,Map<String,List<Map<String,Object>>>> result_map = new LinkedHashMap<String, Map<String,List<Map<String,Object>>>>();
		//  日期         日晚班	   实例
		for (int i = 0; carDutyList != null && i < carDutyList.size(); i++) {
			Map<String, Object> map = carDutyList.get(i);
			String dutydate = map.get("dutyDate")+"";//值班日期
			//日期集合
			Map<String,List<Map<String,Object>>> freq_map = new LinkedHashMap<String, List<Map<String,Object>>>();
			if ( result_map.containsKey(dutydate) ) {
				freq_map = result_map.get(dutydate);
			}
			//班次集合
			String freq_eng = map.get("freq_eng").toString();//值班日期
			List<Map<String,Object>> be_list = new ArrayList<Map<String,Object>>(); 
			if ( freq_map.containsKey(freq_eng) ) {
				be_list = freq_map.get(freq_eng);
			}
			//实例
			be_list.add(map);
			freq_map.put(freq_eng, be_list);
			result_map.put(dutydate, freq_map);
		}
		return result_map;
	}
	
	/**
	 * 根据业务单元、日期、账号,获取条件符合的排班信息
	 * @param dutyDate 日期List集合
	 * @param carId 车辆id
	 * @param freId 班次List集合
	 * @param carBizId 车辆所在组织List集合
	 * @return 排班信息集合
	 */
	public Map<String,Map<String,Map<String,Map<String,Object>>>> cardispatchDutyList ( Map<String, String> requestParamMap ) {
		String date_string = requestParamMap.get("date");
		String carNumber_string = requestParamMap.get("carNumber");
		String freId_string = requestParamMap.get("freId");
		String carId_string = requestParamMap.get("carId");
		String carBizId_string = requestParamMap.get("carBizId");
		//业务单元
		List<String> userBizId_list = new ArrayList<String>();
		if ( StringUtil.isNullOrEmpty(carBizId_string) ) {
			//获取当前登录人的业务单元
			String accountId = (String) SessionService.getInstance().getValueByKey("userId");
			//List<ProviderOrganization> orgByAccountAndOrgTypes = providerOrganizationService.getTopLevelOrgByAccount( accountId );
			//yuan.yw
			List<SysOrg> orgByAccountAndOrgTypes = this.sysOrganizationService.getTopLevelOrgByAccount( accountId );
			if ( orgByAccountAndOrgTypes != null && orgByAccountAndOrgTypes.size() > 0 ) {
				carBizId_string = orgByAccountAndOrgTypes.get(0).getOrgId()+"";
			} else {
				carBizId_string = "16";
			}
		}
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getOrgListDownwardByOrg(Long.valueOf(carBizId_string));
		//yuan.yw
		List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getOrgListDownwardByOrg(Long.valueOf(carBizId_string));
		for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
			SysOrg providerOrganization = orgListDownwardByOrg.get(i);
			userBizId_list.add(providerOrganization.getOrgId()+"");
		}
		userBizId_list.add(carBizId_string);
		//日期集合转换成字符集合
		GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd");
		Gson gson = builder.create();
		List<Date> date_array = gson.fromJson(date_string, new TypeToken<List<Date>>(){}.getType());
		List<String> date_list = DateUtil.changeDate2StringForList(date_array,"yyyy-MM-dd");
		//组装参数
		Map param_map = new HashMap();
		param_map.put("dutyDate", date_list);//     
		param_map.put("carNumber", carNumber_string);
		param_map.put("carBizId", userBizId_list);
		param_map.put("freId", freId_string);
		param_map.put("carId", carId_string);
		//获取排班列表
		List<Map<String, Object>> carDutyList = cardispatchDutyDao.getCarDutyList(param_map);
		
		Map<String,Map<String,Map<String,Map<String,Object>>>> list_map = new LinkedHashMap<String, Map<String,Map<String,Map<String,Object>>>>();
		//	人名			日期			班次
		for (int i = 0; carDutyList != null && i < carDutyList.size(); i++) {
			Map<String, Object> map = carDutyList.get(i);
			if ( map.get("carNumber") == null ) {
				continue;
			}
			String staffName = map.get("carNumber")+"";
			Map<String,Map<String,Map<String,Object>>> staff_map = new LinkedHashMap<String, Map<String,Map<String,Object>>>();
			if ( list_map.containsKey(staffName) ) {
				staff_map = list_map.get(staffName);
			}
			String dutyDate = map.get("dutyDate") + "" ;
			
			Map<String,Map<String,Object>> date_map = new LinkedHashMap<String, Map<String,Object>>();
			if ( date_map.containsKey(dutyDate) ) {
				date_map = staff_map.get(dutyDate);
			}
			String freq_eng = map.get("freq_eng")+"";
			date_map.put(freq_eng, map);
			staff_map.put(dutyDate, date_map);
			list_map.put(staffName, staff_map);
		}
		return list_map;
	}


	
	public List<Map<String,Object>> findCarDutyFreq () {
		List<Map<String, Object>> list = cardispatchDutyDao.findCarDutyFreq();
		return list;
	}

	
	public void txupdateDuty ( Map param_map ) {
		List<Map<String,String>> addIds = (List) param_map.get("addIds");
		List<String> add_list = new ArrayList<String>();
		for (int i = 0; i < addIds.size(); i++) {
			Map<String, String> map = addIds.get(i);
			if ( map != null ) {
				add_list.add(map.get("carId"));
			}
		}
		
		List<Map<String,String>> delIds = (List) param_map.get("delIds");
		List<String> del_list = new ArrayList<String>();
		for (int i = 0; i < delIds.size(); i++) {
			Map<String, String> map = delIds.get(i);
			if ( map != null ) {
				del_list.add(map.get("carId"));
			}
		}
		//删除值班数据
		for (int i = 0; i < del_list.size(); i++) {
			String carId = del_list.get(i);
			String dutyDate = param_map.get("dutyDate")+"";
			String freId = param_map.get("freId")+"";
			Map map = new HashMap();
			map.put("dutyDate", new StringBuffer("to_date('"+dutyDate+"','yyyy-mm-dd')") );
			map.put("freId",Long.valueOf(freId));
			map.put("carId",Long.valueOf(carId));
			txdelete(map);
		}
		
		//添加值班数据
		for (int i = 0; i < add_list.size(); i++) {
			String carId = add_list.get(i);
			String dutyDate = param_map.get("dutyDate")+"";
			String freId = param_map.get("freId")+"";
			
			try {
				boolean flag = this.cardispatchDutyDao.findDutyExists(dutyDate, freId, carId);
				if ( flag ) {
					continue;
				}
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", new StringBuffer(CardispatchConstant.SEQ_DUTY + ".nextval") );
				map.put("dutyDate", new StringBuffer("to_date('"+dutyDate+"','yyyy-mm-dd')") );
				map.put("freId",Long.valueOf(freId));
				map.put("carId",Long.valueOf(carId));
				map.put("isSendOut",0);
				Long id = super.txsave(map);
				int a = 1;
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public boolean deleteDuty( Map param_map ) {
		param_map.put("dutyDate", new StringBuffer("to_date('"+param_map.get("dutyDate")+"','yyyy-mm-dd')"));
		param_map.put("freId", Long.valueOf(param_map.get("freId")+""));
		int txdelete = super.txdelete(param_map, CardispatchOndutydrivercarpair.class);
		boolean flag = txdelete > 0 ;
		return flag;
	}
	
	/**
	 * 判断是否当天排班
	 * @param paramMap 参数集合
	 * @return
	 */
	public boolean checkIsPlanDutyToday(Map<String, String> paramMap) {
		String sql = "SELECT t3.* FROM cardispatch_car t1,cardispatch_drivercarpair t2,cardispatch_ondutydrivercarpair t3 " +
					 "WHERE t1.id=t2.car_id AND t2.id=t3.drivercarpairId ";
		String dutyDate = TimeFormatHelper.getTimeFormatByFree(new Date(),"yyyy-MM-dd");
		sql += "AND t3.dutyDate='"+dutyDate+"' ";
		String carNumber = paramMap.get("carNumber");
		String carId = paramMap.get("carId");
		if(carNumber!=null && !"".equals(carNumber)){
			sql += "AND t1.carNumber='"+carNumber+"' ";
		}
		if(carId!=null && !"".equals(carId)){
			sql += "AND t1.id="+carId+" ";
		}
		List<Map<String, Object>> list = executeFindListMapObject(sql);
		if(list!=null && !list.isEmpty()){
			return true;
		}
		return false;
	}
	
	/*************** getter setter ****************/
	public CardispatchDutyDao getCardispatchDutyDao() {
		return cardispatchDutyDao;
	}
	public void setCardispatchDutyDao(CardispatchDutyDao cardispatchDutyDao) {
		this.cardispatchDutyDao = cardispatchDutyDao;
	}
	public NetworkResourceInterfaceService getNetworkResourceInterfaceService() {
		return networkResourceInterfaceService;
	}
	public void setNetworkResourceInterfaceService(
			NetworkResourceInterfaceService networkResourceInterfaceService) {
		this.networkResourceInterfaceService = networkResourceInterfaceService;
	}

	
	
}
