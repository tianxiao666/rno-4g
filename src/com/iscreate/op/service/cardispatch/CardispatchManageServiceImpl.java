package com.iscreate.op.service.cardispatch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.informationmanage.common.ArrayUtil;
import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.CardispatchConstant.CardispatchCarState;
import com.iscreate.op.dao.cardispatch.CardispatchCarDao;
import com.iscreate.op.dao.cardispatch.CardispatchDriverDao;
import com.iscreate.op.dao.cardispatch.CardispatchFuelBillsDao;
import com.iscreate.op.dao.cardispatch.CardispatchGpsDao;
import com.iscreate.op.dao.cardispatch.CardispatchInstrumentReadingDao;
import com.iscreate.op.dao.cardispatch.CardispatchTerminalDao;
import com.iscreate.op.dao.cardispatch.CardispatchWorkorderDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;
import com.iscreate.op.pojo.cardispatch.CardispatchFuelBills;
import com.iscreate.op.pojo.cardispatch.CardispatchInstrumentReading;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.informationmanage.BaseServiceImpl;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.iscreate.plat.tools.LatLngHelper;


@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class CardispatchManageServiceImpl extends BaseServiceImpl<CardispatchCar> implements CardispatchManageService {

	/************ 依赖注入 ***********/
	CardispatchCarDao cardispatchCarDao;
	CardispatchDriverDao cardispatchDriverDao;
	private CardispatchTerminalDao cardispatchTerminalDao;
	private WorkManageService workManageService;
	private CardispatchGpsDao cardispatchGpsDao;
	private CardispatchWorkorderDao cardispatchWorkorderDao;
	private SysOrgUserService sysOrgUserService;
	private SysOrganizationDao sysOrganizationDao;//du.hw添加
	private SysAccountService sysAccountService;
	private CardispatchInstrumentReadingDao cardispatchInstrumentReadingDao;
	private CardispatchFuelBillsDao cardispatchFuelBillsDao;
	
	
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationDao getSysOrganizationDao() {
		return sysOrganizationDao;
	}

	public void setSysOrganizationDao(SysOrganizationDao sysOrganizationDao) {
		this.sysOrganizationDao = sysOrganizationDao;
	}

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	
	/****************** service *******************/
	private Log log = LogFactory.getLog(this.getClass());
	
	
	
	/********************** 车辆 begin **********************/
	
	/**
	 * 根据车辆id,查询车辆信息状态
	 * @param carId - 车辆id
	 * @return 车辆信息 - stateCName 为车辆状态中文
	 */
	public Map<String,String> getCarStateByCarId ( String carId ) {
		Map<String,String> result_map = cardispatchCarDao.getCarStateByCarId(carId);
		findTerminalStateCName(result_map);
		return result_map;
	}
	
	
	public Map<String,String> getCarByCarDriverPairId ( String cardriverpairId ) {
		Map<String,String> result_map = cardispatchCarDao.getCarByCarDriverPairId(cardriverpairId);
		return result_map;
	}
	
	
	public Map<String,String> getCarTopGps ( String carNumber ) {
		Map<String, String> gps = new HashMap<String, String>();
		Map<String, String> terminal = cardispatchCarDao.findTerminalByCarNumber(carNumber);
		if ( terminal == null || terminal.size() == 0 || !terminal.containsKey("id") || terminal.get("id") == null ) {
			return gps; 
		}
		String clientId = terminal.get("id");
		gps = cardispatchTerminalDao.findMaxGpsByClientId(clientId);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(5);
		nf.setMinimumFractionDigits(1);
		if ( gps != null ) {
			String jingdu = gps.get("jingdu")+"";
			String weidu = gps.get("weidu")+"";
			if ( !StringUtil.isNullOrEmpty(jingdu) && !StringUtil.isNullOrEmpty(weidu) ) {
				gps.put("jingdu",nf.format(Double.valueOf(jingdu)));
				gps.put("weidu",nf.format(Double.valueOf(weidu)));
			}
		}
		return gps;
	}
	
	public Map<String,String> findCarInfoById ( String carId ) {
		Map<String, String> car = cardispatchCarDao.findCarAllInfoById(carId);
		//获取司机组织
		findDriveBizByAccount(car);
		//获取车辆组织
		findCarBizName(car);
		return car;
	}
	
	
	public Map<String,Object> getCarGpsInTime ( String carNumber ,String startTime , String endTime,String orgId ) {
		//查询车辆gps轨迹
		Map<String,Object> result_map = new LinkedHashMap<String, Object>();
		
		Map<String, String> car = cardispatchCarDao.findByCarNumberForOrgId(carNumber,orgId);
		
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		if ( car != null && car.size() > 0 ) {
			String carId = car.get("carId");
			String terminalId = cardispatchCarDao.findTerminalIdByCarId(carId);
			list = cardispatchTerminalDao.findTerminalGpsList(carId, startTime, endTime);
			for (int i = 0; i < list.size() ; i++) {
				Map<String, String> driver = list.get(i);
				findDriveBizByAccount(driver);
			}
			//查询车辆最新gps
			if ( !StringUtil.isNullOrEmpty(terminalId) ) {
				Map<String, String> map = cardispatchTerminalDao.findMinGpsByCarId(carId,startTime);
				if ( map != null ) {
					map.put("carNumber", car.get("carNumber"));
					map.put("carId", car.get("carId"));
					map.put("carType", car.get("carType"));
					//根据车辆id,获取司机信息
					String driverId = cardispatchCarDao.findDriverIdByCarId(carId);
					Map<String, String> driver = cardispatchDriverDao.findDriverById(driverId);
					map.put("telphoneNo", driver.get("telphoneNo"));
					map.put("driverName", driver.get("driverName"));
					result_map.put("gpsList", list);
					result_map.put("mostNewGPSLocation", map);
				}
			}
		}
		return result_map;
	}
	
	/**
	 * 
	 * 根据车牌号开始时间结束时间获取心跳包数据
	 * @author Li.hb
	 * @date 2013-08-14
	 * @param carNumber
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String,Object> getCarHeartbeat (String carNumber, String startTime,String endTime)
	{
		Map<String,Object> result_map = new LinkedHashMap<String, Object>();
		
		List<Map<String, String>> map = cardispatchTerminalDao.findCarHeartbeat(carNumber,startTime,endTime);
		
		result_map.put("Heartbeat", map);
		
		return result_map;
	}
	
	
	
	/**
	 * 根据车辆id,获取车辆最后一次还车里程读数
	 * @param carId 车辆id
	 * @return 车辆信息
	 */
	public Double findCarLastMileage ( String carId ) {
		String mileage = cardispatchCarDao.findCarLastMileage( carId );
		Double realReturnCarMileage = 0d;
		if ( !StringUtil.isNullOrEmpty(mileage) && !mileage.equalsIgnoreCase("null") ) {
			realReturnCarMileage = Double.valueOf(mileage);
		}
		return realReturnCarMileage;
	}
	
	
	/**
	 * 验证车牌是否存在
	 * @param carNumber 车牌号
	 * @param carId 车辆id(可为空)
	 * @return true 已存在 , false 不存在
	 */
	public Boolean checkCarNumberExists ( String carNumber , String carId ) {
		//查询carNumber是否存在
		Map<String, String> car = cardispatchCarDao.checkCarNumber(carNumber) ;
		Boolean result = false;
		if( car != null ){
			if (carId != null && !carId.isEmpty()) {
				String id = car.get("carId");
				if (  id.equals(carId)) {
					result = false;
				} else {
					result = true;
				}
			} else {
				//车牌号存在
				result = true;
			}
		}
		return result;
	}
	
	public List<Map<String, Object>> findCarDriverPairList( Map param_map ) {
		if ( !param_map.containsKey("carBizId") || param_map.get("carBizId") == null || (param_map.get("carBizId")+"").isEmpty() ) {
			param_map.put("carBizId", "16");
		}
		//List<ProviderOrganization> pros = providerOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("carBizId")+""));
		//yuan.yw
		List<SysOrg> pros = this.sysOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("carBizId")+""));
		List<Long> bizIds = new ArrayList<Long>();
		for (int i = 0; i < pros.size(); i++) {
			SysOrg p = pros.get(i);
			bizIds.add(p.getOrgId());
		}
		param_map.put("carBizId", bizIds);
		List<Map<String, Object>> list = cardispatchCarDao.findCarDriverPairList(param_map);
		return list;
	}
	
	
	/**
	 * 根据组织ID查找车辆牌照
	 */
	
	public List<String> findCarNumber(String orgId)
	{
		List<String> list = cardispatchCarDao.findCarNumber(orgId);
		return list;
	}
	
	
	
	public List<Map<String, Object>> findCarList( Map param_map ) {
		if ( param_map.containsKey("carBizId") && param_map.get("carBizId") != null ) {
			//List<ProviderOrganization> pros = providerOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("carBizId")+""));
			//yuan.yw
			List<SysOrg> pros = this.sysOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("carBizId")+""));
			
			List<Long> bizIds = new ArrayList<Long>();
			for (int i = 0; i < pros.size(); i++) {
				SysOrg p = pros.get(i);
				bizIds.add(p.getOrgId());
			}
			param_map.put("carBizId", bizIds);
		}
		List<Map<String, Object>> list = cardispatchCarDao.findCarList(param_map);
		return list;
	}

	/**
	 * @param param_map - 
	 * @param duty_param_Map - 
	 * @param isArranged - 是否排班
	 */
	public List<Map<String,Object>> findCarDriverPairListIsDuty ( Map param_map , Map duty_param_Map , String isArranged  )  {
		
		String carBizId = param_map.get("carBizId")+"";
		List<Long> subBizUnits = new ArrayList<Long>(); 
		if ( !param_map.containsKey("carBizId") || param_map.get("carBizId") == null ) {
			//没有传递,获取当前登录人的业务单元
			String loginPersonAccount = (String) SessionService.getInstance().getValueByKey("userId");
			//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getTopLevelOrgByAccount(loginPersonAccount);
			List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getTopLevelOrgByAccount(loginPersonAccount);
			for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
				SysOrg providerOrganization = orgListDownwardByOrg.get(i);
				if ( providerOrganization != null ) {
					carBizId = providerOrganization.getOrgId()+"";
				}
			}
		}
		//有传递业务单元id
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getOrgListDownwardByOrg(Long.valueOf(carBizId));
		//yuan.yw
		List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getOrgListDownwardByOrg(Long.valueOf(carBizId));
		for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
			SysOrg providerOrganization = orgListDownwardByOrg.get(i);
			if ( providerOrganization != null ) {
				subBizUnits .add(providerOrganization.getOrgId());
			}
		}
		param_map.put("carBizId", subBizUnits);
		if ( duty_param_Map == null || !duty_param_Map.containsKey("dutyDate") || duty_param_Map.get("dutyDate") == null ) {
			duty_param_Map.put("dutyDate", DateUtil.formatDate(new Date(),"yyyy-MM-dd"));
		}
		String[] freIds = (String[]) duty_param_Map.get("freId");
		if ( freIds == null || freIds.length == 0 || freIds[0] == null || freIds[0].equalsIgnoreCase("null") ) {
			duty_param_Map.remove("freId");
		}
		ArrayUtil.removeEmpty(duty_param_Map);
		List<Map<String, Object>> list = cardispatchCarDao.findCarDriverPairListIsDuty(param_map,duty_param_Map);
		
		List<Map<String, Object>> result_list = new ArrayList<Map<String,Object>>();
		//查询最新gps
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> car_map = list.get(i);
			
			if ( car_map == null || car_map.size() == 0 ) {
				continue;
			}
			
			//查询车辆组织
			String cBizId = car_map.get("carBizId")+"";
			if ( !StringUtil.isNullOrEmpty(cBizId) ) {
				Map<String,Object> c_biz = sysOrganizationDao.getOrganizationMessageByOrgId(Long.valueOf(cBizId));
				car_map.put("carBizName", c_biz.get("name"));
			}
			
			//查询司机组织
			String dBizId = car_map.get("driverBizId")+"";
			if ( !StringUtil.isNullOrEmpty(dBizId) ) {
				Map<String,Object> d_biz = sysOrganizationDao.getOrganizationMessageByOrgId(Long.valueOf(dBizId));
				car_map.put("driverBizName", d_biz.get("name"));
			}
			/*
			if ( car_map != null && car_map.size() > 0 ) {
				String carId = car_map.get("carId")+"";
				
				String terminalId = cardispatchCarDao.findTerminalIdByCarId(carId);
				
				if ( !StringUtil.isNullOrEmpty(terminalId) ) {
					Map<String, String> map = cardispatchTerminalDao.findMaxGpsByClientId(terminalId);
					
					car_map.putAll(map);
				}
				
			}
			*/
			String isArr = car_map.get("isArranged")+"";
			if ( isArranged == null || isArranged.isEmpty() || isArranged.equals("null")) {
				long totalTask = workManageService.getWorkOrderCountByResourceTypeAndResourceId("car", car_map.get("carId")+"");
				car_map.put("totalTask", totalTask);
				result_list.add(car_map);
			} else if ( isArr.equals(isArranged) ) {
				long totalTask = workManageService.getWorkOrderCountByResourceTypeAndResourceId("car", car_map.get("carId")+"");
				car_map.put("totalTask", totalTask);
				result_list.add(car_map);
			}
			
		}
		
		return result_list;
	}
	
	
	
	/**
	 * @param param_map - 
	 * @param duty_param_Map - 
	 * @param isArranged - 是否排班
	 */
	public List<Map<String,Object>> findCarDriverPairListIsDutyByGis ( Map param_map , Map duty_param_Map , String isArranged  )  {
		
		String carBizId = param_map.get("carBizId")+"";
		List<Long> subBizUnits = new ArrayList<Long>(); 
		if ( !param_map.containsKey("carBizId") || param_map.get("carBizId") == null ) {
			//没有传递,获取当前登录人的业务单元
			String loginPersonAccount = (String) SessionService.getInstance().getValueByKey("userId");
			//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getTopLevelOrgByAccount(loginPersonAccount);
			//yuan.yw
			List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getTopLevelOrgByAccount(loginPersonAccount);
			
			for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
				SysOrg providerOrganization = orgListDownwardByOrg.get(i);
				if ( providerOrganization != null ) {
					carBizId = providerOrganization.getOrgId()+"";
				}
			}
		}
		//有传递业务单元id
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getOrgListDownwardByOrg(Long.valueOf(carBizId));
		//yuan.yw
		List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getOrgListDownwardByOrg(Long.valueOf(carBizId));
		for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
			SysOrg providerOrganization = orgListDownwardByOrg.get(i);
			if ( providerOrganization != null ) {
				subBizUnits .add(providerOrganization.getOrgId());
			}
		}
		param_map.put("carBizId", subBizUnits);
		if ( duty_param_Map == null || !duty_param_Map.containsKey("dutyDate") || duty_param_Map.get("dutyDate") == null ) {
			duty_param_Map.put("dutyDate", DateUtil.formatDate(new Date(),"yyyy-MM-dd"));
		}
		String[] freIds = (String[]) duty_param_Map.get("freId");
		if ( freIds == null || freIds.length == 0 || freIds[0] == null || freIds[0].equalsIgnoreCase("null") ) {
			duty_param_Map.remove("freId");
		}
		ArrayUtil.removeEmpty(duty_param_Map);
		
		
		String carIds = param_map.get("carId")+"";
		if ( param_map.containsKey("carId") && param_map.get("carId") !=  null ) {
			String[] split = carIds.split(",");
			param_map.put("carId", split);
		}
		
		List<Map<String, Object>> list = cardispatchCarDao.findCarDriverPairListIsDuty(param_map,duty_param_Map);
		
		/*List<Map<String, Object>> result_list = new ArrayList<Map<String,Object>>();
		//查询最新gps
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> car_map = list.get(i);
			if ( car_map == null || car_map.size() == 0 ) {
				continue;
			}
			if ( car_map != null && car_map.size() > 0 ) {
				String carId = car_map.get("carId")+"";
				
				String terminalId = cardispatchCarDao.findTerminalIdByCarId(carId);
				
				if ( !StringUtil.isNullOrEmpty(terminalId) ) {
					Map<String, String> map = cardispatchTerminalDao.findMaxGpsByClientId(terminalId);
					if ( map != null ) {
						car_map.putAll(map);
					}
				}
			}
			result_list.add(car_map);
		}*/
		return list;
	}
	
	/**
	 * 查询车辆经纬度并计算里程
	 * 
	 * @author LI.HB
	 * @param carId  车辆ID
	 * @param sTime  开始时间
	 * @param eTime  结束时间
	 * @return 车辆ID里程数Map
	 * 
	 * @Create_Time 2013-08-07
	 * 
	 */
	public Map<String,Double> findCurrentDayMileage (String carId,String sTime,String eTime)
	{
		Map<String,Double> map = new HashMap<String,Double>();
		
		if(carId == null || sTime == null || eTime == null || "".equals(carId) || "".equals(sTime)|| "".equals(eTime))
		{
			return map;
		}
		
		List<Map<String, String>> list = cardispatchCarDao.findCurrentDayLatLngList(carId,sTime,eTime);
		
		DecimalFormat df = new DecimalFormat( "0.00");  
		
		double jingdu = 0.0;
		double weidu = 0.0;
		double oldjingdu = 0.0;
		double oldweidu = 0.0;
		double Distance = 0.0;
		
		for(int i = 0;list != null && i< list.size();i++)
		{
			
			Map<String,String> latlngMap = list.get(i);
			
			jingdu = Double.parseDouble(latlngMap.get("JINGDU"));
			weidu = Double.parseDouble(latlngMap.get("WEIDU"));
			
			if(oldjingdu!=0.0)
			{
				Distance += LatLngHelper.Distance(jingdu,weidu,oldjingdu,oldweidu);
			}
			
			oldjingdu = jingdu;
			oldweidu = weidu;
		}
		
		BigDecimal   b   =   new   BigDecimal(Distance/1000);  
		
		map.put(carId, b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		
		return map;
	}
	
	public boolean txdeleteCarByIds ( String ids ) {
		String[] driver_ids = ids.split(",");
		List<String> ids_list = Arrays.asList(driver_ids);
		boolean flag = cardispatchCarDao.deleteCarByIds(ids_list);
		return flag;
	}
	
	
	public Long saveCar ( Map param_map ) {
		CardispatchCar car = null;
		try {
			car = ObjectUtil.map2Object(param_map, CardispatchCar.class);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		Long id = 0l;
		if ( car != null ) {
			id = super.txinsert(car);
		}
		return id;
	}
	
	
	public boolean updateCar ( Map param_map ) {
		Map<String,String> where_map = new LinkedHashMap<String, String>();
		String carId = param_map.get("id")+"";
		where_map.put("id", carId);
		int num = super.txupdate(where_map, param_map);
		boolean flag = num > 0 ;
		return flag;
	}
	
	public boolean bindCarAndDriver ( String carId , String driverId ){
		//清空关系
		boolean flag = cardispatchCarDao.clearCarBindDriver( carId , driverId );
		//绑定关系
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("car_id",Long.valueOf(carId));
		map.put("driver_id", Long.valueOf(driverId));
		map.put("status",1);
		try {
			flag = cardispatchCarDao.carBindDriver(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	

	public boolean saveDriverAndBindCar ( Map driver_map , String carId ){
		//保存司机
		boolean flag = cardispatchDriverDao.saveDriver( driver_map);
		//保存人员
//		Account account = new Account();
//		account.setName(driver_map.get("name")+"");
//		account.setAccount(driver_map.get("accountId")+"");
//		account.setPassword("123456");
//		flag = providerOrganizationService.txSaveAccountService(account);
		//清空关系
		flag = cardispatchCarDao.clearCarBindDriver( carId , null);
		//绑定车辆关系
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("car_id",carId);
		map.put("driver_id",driver_map.get("driverId"));
		map.put("status",1);
		try {
			flag = cardispatchCarDao.carBindDriver(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	public boolean bindCarAndTerminal ( String carId , String terminalId ){
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try 
		{
			flag = cardispatchCarDao.isCarBindTerminal(carId, terminalId);
			
			if(flag)
			{
				flag = cardispatchCarDao.updateCarBindTerminalId(carId, terminalId);
			}
			else
			{
				Map<String,Object> param_map = new HashMap<String,Object>();
				param_map.put("car_id", carId);
				param_map.put("terminal_id", terminalId);
				flag = cardispatchCarDao.carBindTerminal(param_map);
			}
			
		} catch (Exception e) 
		{
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	
	public boolean saveTerminalAndBindCar ( Map terminal_map , String carId ){
		boolean flag = cardispatchTerminalDao.saveTerminal(terminal_map);
		//清空关系
		flag = cardispatchCarDao.clearCarBindTerminal( carId , terminal_map.get("terminalId")+"" );
		String terminalId = terminal_map.get("terminalId")+"";
		//绑定车辆关系
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("car_id",Long.valueOf(carId));
		map.put("terminal_id",Long.valueOf(terminalId));
		map.put("allocateTime", new StringBuffer("to_date('"+ DateUtil.formatDate(new Date(), "yyyy-MM-dd") +"','yyyy-mm-dd') " ));
		try {
			flag = cardispatchCarDao.carBindTerminal(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	
	
	
	/********************** 车辆 end **********************/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/********************** 司机 begin **********************/
	
	public boolean checkAccountId ( String accountId ) {
		boolean isExists = sysAccountService.checkProviderAccountService(accountId);
		//因旧数据存在,加多句判断
		if ( !isExists ) {
			isExists = cardispatchDriverDao.checkDriverAccountId(accountId);
		}
		return isExists;
	}
	
	
	
	public List<Map<String, Object>> findDriverList( Map param_map , Boolean isFree ) {
		if ( param_map.containsKey("driverBizId") && param_map.get("driverBizId") != null ) {
			//List<ProviderOrganization> pros = providerOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("driverBizId")+""));
			//yuan.yw
			List<SysOrg> pros = this.sysOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("driverBizId")+""));
			List<Long> bizIds = new ArrayList<Long>();
			for (int i = 0; i < pros.size(); i++) {
				SysOrg p = pros.get(i);
				bizIds.add(p.getOrgId());
			}
			param_map.put("driverBizId", bizIds);
		}
		List<Map<String, Object>> list = cardispatchDriverDao.findDriverList(param_map,isFree);
		return list;
	}

	/**
	 * 根据司机id集合,删除司机信息
	 * @param ids - 司机id集合
	 * @return (boolean) 操作是否成功(true ： 操作成功)
	 */
	public boolean txdeleteDriverByIds ( String ids ) {
		String[] driver_ids = ids.split(",");
		List<String> ids_list = Arrays.asList(driver_ids);
		boolean flag = false;
		try {
			for (int i = 0; i < driver_ids.length; i++) {
				String driverId = driver_ids[i];
				Map<String, String> driver = cardispatchDriverDao.findDriverById(driverId);
				flag = cardispatchDriverDao.deleteDriverById(driverId);
			}
			flag = true;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 保存司机,并绑定组织
	 * @param driver_map - 司机信息
	 * @param orgId - 组织id
	 * @return (boolean) 操作是否成功(true：操作成功)
	 */
	public boolean txSaveDriver ( Map driver_map ) {
		//保存司机
		boolean flag = cardispatchDriverDao.saveDriver( driver_map);
		return flag;
	}
	
	/**
	 * 更新司机信息,并更新组织绑定
	 * @param driver_map - 司机信息
	 * @param orgId - 组织id
	 * @return (boolean) 操作是否成功(true:操作成功)
	 */
	public boolean txUpdateDriverById ( Map driver_map )  {
		String driverId = driver_map.get("id")+"";
		//更新司机
		boolean flag = cardispatchDriverDao.updateDriverById( driver_map , driverId );
		return flag;
	}
	
	/********************** 司机 end **********************/
	
	
	/********************** 终端 begin **********************/
	
	public boolean checkTerminalImeiIsExists ( String clientImei ) {
		boolean isExists = false;
		isExists = cardispatchTerminalDao.checkTerminalImeiIsExists(clientImei);
		return isExists;
	}
	

	public List<Map<String, String>> getNotDriverStaffAutoComplete ( String driverName , String bizId ) {
		List<Long> subBizUnits = new ArrayList<Long>();
		if ( StringUtil.isNullOrEmpty(bizId) ) {
			//没有传递,获取当前登录人的业务单元
			String loginPersonAccount = (String) SessionService.getInstance().getValueByKey("userId");
			//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getTopLevelOrgByAccount(loginPersonAccount);
			//yuan.yw
			List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getTopLevelOrgByAccount(loginPersonAccount);
			
			for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
				SysOrg providerOrganization = orgListDownwardByOrg.get(i);
				if ( providerOrganization != null ) {
					bizId = providerOrganization.getOrgId()+"";
				}
			}
		}
		//有传递业务单元id
		//List<ProviderOrganization> orgListDownwardByOrg = providerOrganizationService.getOrgListDownwardByOrg(Long.valueOf(bizId));
		//yuan.yw
		List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getOrgListDownwardByOrg(Long.valueOf(bizId));
		for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
			SysOrg providerOrganization = orgListDownwardByOrg.get(i);
			if ( providerOrganization != null ) {
				subBizUnits .add(providerOrganization.getOrgId());
			}
		}
		List<Map<String, String>> list = cardispatchDriverDao.getNotDriverStaffListByNameOrAccount(driverName,subBizUnits);
		return list;
	}
	
	/**
	 * 
	 */
	public List<Map<String, Object>> findTerminalList( Map param_map , Boolean isFree ) {
		if ( param_map.containsKey("terminalBizId") && param_map.get("terminalBizId") != null ) {
			//List<ProviderOrganization> pros = providerOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("terminalBizId")+""));
			//yuan.yw
			List<SysOrg> pros = this.sysOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("terminalBizId")+""));
			List<Long> bizIds = new ArrayList<Long>();
			for (int i = 0; i < pros.size(); i++) {
				SysOrg p = pros.get(i);
				bizIds.add(p.getOrgId());
			}
			param_map.put("terminalBizId", bizIds);
		}
		List<Map<String, Object>> list = cardispatchTerminalDao.findTerminalList(param_map , isFree );
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			ArrayUtil.setMapDefault(map, "0", "monthlyRent");
			ArrayUtil.setMapDefault(map, " ", "terminalName");
		}
		return list;
	}
	
	
	
	public List<Map<String, Object>> findTerminalBindingList( Map param_map , Boolean isFree ) {
		if ( param_map.containsKey("terminalBizId") && param_map.get("terminalBizId") != null ) {
			//List<ProviderOrganization> pros = providerOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("terminalBizId")+""));
			//yuan.yw
			List<SysOrg> pros = this.sysOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("terminalBizId")+""));
			List<Long> bizIds = new ArrayList<Long>();
			for (int i = 0; i < pros.size(); i++) {
				SysOrg p = pros.get(i);
				bizIds.add(p.getOrgId());
			}
			param_map.put("terminalBizId", bizIds);
		}
		List<Map<String, Object>> list = cardispatchTerminalDao.findTerminalBindingList(param_map , isFree );
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			ArrayUtil.setMapDefault(map, "0", "monthlyRent");
			ArrayUtil.setMapDefault(map, " ", "terminalName");
		}
		return list;
	}
	
	
	
	public boolean txdeleteTerminalByIds ( String ids ) {
		String[] terminal_ids = ids.split(",");
		List<String> ids_list = Arrays.asList(terminal_ids);
		boolean flag = cardispatchTerminalDao.deleteTerminalByIds(ids_list);
		return flag;
	}
	
	public boolean txSaveTerminal ( Map param_map ) {
		boolean flag = cardispatchTerminalDao.saveTerminal(param_map);
		return flag;
	}
	
	public boolean txUpdateTerminalById ( Map param_map ) {
		boolean flag = cardispatchTerminalDao.updateTerminalById(param_map, param_map.get("id")+"");
		return flag;
	}
	
	
	/********************** 终端 end **********************/
	
	
	
	/**************** gps里程统计 begin *****************/
	
	/**
	 * 查询组织下的所有车辆时间内的里程读数
	 * @param bizId - 组织id 
	 * @param date_list - 日期集合
	 */
	public List<Map<String,String>> findCarMileageByBizInTime ( String bizId , List<String> date_list,String startHour , String endHour ) {
		List<Map<String,String>> result_list = new ArrayList<Map<String,String>>();
		if ( StringUtil.isNullOrEmpty( bizId ) ) {
			//如果组织id为空,则获取当前登录人信息
			String account = (String) SessionService.getInstance().getValueByKey("userId");
			//List<ProviderOrganization> list = providerOrganizationService.getTopLevelOrgByAccount(account);
			//yuan.yw
			List<SysOrg> list = this.sysOrganizationService.getTopLevelOrgByAccount(account);
			if ( list != null && list.size() > 0 ) {
				SysOrg p = list.get(0);
				bizId = p.getOrgId()+"";
			}
		}
		
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(0);
		nf.setMinimumFractionDigits(0);
		
		List<Map<String, String>> mi_list = cardispatchGpsDao.findAllCarGpsMileage(bizId, date_list, startHour, endHour);
		
		//排序
		return mi_list;
	}
	
	
	
	
	/**************** gps里程统计 end *****************/
	
	/***************** 格式化 ******************/
	
	/**
	 * 根据司机账号获取该账号组织中文名
	 * @param driver - 信息(key包含accountId) 
	 * @key driverBizName - 司机组织中文名
	 * @key driverBizId - 司机组织Id
	 */
	public void findDriveBizByAccount ( Map<String,String> driver ) {
		//获取司机组织
		if ( driver.containsKey("driverBizId") && !StringUtil.isNullOrEmpty(driver.get("driverBizId")) && !driver.get("driverBizId").equals("null") ) {
			String driverBizId = driver.get("driverBizId");
			Map<String,Object> pro = sysOrganizationDao.getOrganizationMessageByOrgId(Long.valueOf(driverBizId));
			
			if ( pro != null ) {
				driver.put("driverBizName", (String) pro.get("name"));
				driver.put("driverBizId", (String) pro.get("id"));
			}
		}
	}
	
	/**
	 * 根据车辆组织id,获取组织中文名
	 * @param car - 信息(key包含carBizId)
	 * @key carBizName - 车辆组织中文名
	 */
	public void findCarBizName ( Map<String,String> car ) {
		if ( car.containsKey("carBizId") && !StringUtil.isNullOrEmpty(car.get("carBizId")) && !car.get("carBizId").equals("null") ) {
			Long carBizId = Long.valueOf(car.get("carBizId"));
			Map<String,Object> orgByOrgIdService = sysOrganizationDao.getOrganizationMessageByOrgId(carBizId);
			
			car.put("carBizName", (String) orgByOrgIdService.get("name"));
		}
	}
	
	/**
	 * 根据终端状态id,获取终端状态中文名
	 * @param terminal - 信息(key包含terminalState)
	 * @key stateCName - 状态中文名
	 */
	public void findTerminalStateCName ( Map<String,String> terminal ) {
		//中文解释
		Integer terminalState = 0;
		if ( terminal.containsKey("terminalState") && !StringUtil.isNullOrEmpty(terminal.get("terminalState")) && !terminal.get("terminalState").equals("null") ) {
			terminalState = Integer.valueOf(terminal.get("terminalState"));
		}
		String cName = CardispatchCarState.getCNameByNum(terminalState);
		terminal.put("stateCName", cName);
	}
	
	
	/**
	 * 根据组织id集合,获取车辆信息(包括最新gps、车辆状态)
	 * @param orgIds - 组织id集合
	 * @return
	 */
	public List<Map<String,Object>> findCarListWithTopGpsAndStateByOrgIds ( Collection<String> orgIds ) {
		List<Map<String, Object>> car_list = this.cardispatchCarDao.findCarTerminalListByOrgIdList(orgIds);
		if ( car_list == null ) {
			return null;
		}
		for (int i = 0; i < car_list.size() ; i++) {
			Map<String, Object> car_map = car_list.get(i);
			String carNumber = car_map.get("carNumber")+"";
			car_map.put("objectIdentity",car_map.get("carId"));
			car_map.put("objectName", carNumber);
			String terminalId = car_map.get("terminalId")+"";
			
			String carState = null;
			if( car_map.get("terminalState") != null ) {
				car_map.put("carState", car_map.get("terminalState"));
			}
			
			//获取最新gps
			Map<String, String> gps_map = this.cardispatchTerminalDao.findMaxGpsByClientId(terminalId);
			if ( gps_map != null ) {
				car_map.put("longitude", gps_map.get("jingdu"));
				car_map.put("latitude", gps_map.get("weidu"));
			}
		}
		return car_list;
	}
	/**
	 * 根据车牌,查询车辆最新GPS信息
	 * @param carNumber
	 * @return
	 */
	public Map<String,String> findGPSLoactionByCarNumber ( String carNumber){
		Map<String, String> findGPSLoactionByCarNumber = this.cardispatchCarDao.findGPSLoactionByCarNumber(carNumber);
		return findGPSLoactionByCarNumber;
	}
	/**
	 * 
	 * @description: 车辆状态监控获取信息 
	 * @author：yuan.yw
	 * @param param_map
	 * @param duty_param_Map
	 * @param isArranged
	 * @param currentPage
	 * @param pageSize
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Jul 23, 2013 10:14:04 AM
	 */
	public List<Map<String,Object>> findCarDriverPairListForMonitor(Map<String,Object> param_map, Map<String,Object> duty_param_Map  , String isArranged,int currentPage,int pageSize){
		int indexStart=0;
		int indexEnd = 0;
		if(currentPage<=0){
			currentPage=1;
		}
		indexStart=1+(currentPage-1)*pageSize;
		if(pageSize>0){
			indexEnd = (currentPage)*pageSize;
		}
		String carBizId = param_map.get("carBizId")+"";
		List<Long> subBizUnits = new ArrayList<Long>(); 
		if ( !param_map.containsKey("carBizId") || param_map.get("carBizId") == null ) {
			//没有传递,获取当前登录人的业务单元
			String loginPersonAccount = (String) SessionService.getInstance().getValueByKey("userId");
			List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getTopLevelOrgByAccount(loginPersonAccount);
			for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
				SysOrg providerOrganization = orgListDownwardByOrg.get(i);
				if ( providerOrganization != null ) {
					carBizId = providerOrganization.getOrgId()+"";
				}
			}
		}
		//有传递业务单元id
		//yuan.yw
		List<SysOrg> orgListDownwardByOrg = this.sysOrganizationService.getOrgListDownwardByOrg(Long.valueOf(carBizId));
		for (int i = 0; i < orgListDownwardByOrg.size(); i++) {
			SysOrg providerOrganization = orgListDownwardByOrg.get(i);
			if ( providerOrganization != null ) {
				subBizUnits .add(providerOrganization.getOrgId());
			}
		}
		param_map.put("carBizId", subBizUnits);
		if ( duty_param_Map == null || !duty_param_Map.containsKey("dutyDate") || duty_param_Map.get("dutyDate") == null ) {
			duty_param_Map.put("dutyDate", DateUtil.formatDate(new Date(),"yyyy-MM-dd"));
		}
		String[] freIds = (String[]) duty_param_Map.get("freId");
		if ( freIds == null || freIds.length == 0 || freIds[0] == null || freIds[0].equalsIgnoreCase("null") ) {
			duty_param_Map.remove("freId");
		}
		ArrayUtil.removeEmpty(duty_param_Map);
		List<Map<String, Object>> resultList = cardispatchCarDao.findCarDriverPairListForMonitor(param_map, duty_param_Map, indexStart, indexEnd);
		/*if(resultList!=null && !resultList.isEmpty()){
			List<Map<String,Object>> list = (List<Map<String,Object>>)resultList.get(0).get("entityList");
			//查询最新gps
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> car_map = list.get(i);
				if ( car_map == null || car_map.size() == 0 ) {
					continue;
				}
				String isArr = car_map.get("isArranged")+"";
				if ( isArranged == null || isArranged.isEmpty() || isArranged.equals("null")) {
					long totalTask = workManageService.getWorkOrderCountByResourceTypeAndResourceId("car", car_map.get("carId")+"");
					car_map.put("totalTask", totalTask);
				} else if ( isArr.equals(isArranged) ) {
					long totalTask = workManageService.getWorkOrderCountByResourceTypeAndResourceId("car", car_map.get("carId")+"");
					car_map.put("totalTask", totalTask);
				}
				
			}
		}*/

		return resultList;
	}

	/***
	 * 
	 * @description: 根据车辆id  获取当前车辆最新Gps
	 * @author：yuan.yw
	 * @param carId
	 * @return     
	 * @return Map<String,Object>     
	 * @date：Jul 26, 2013 4:23:14 PM
	 */
	public Map<String,Object> getCarCurrentGpsInfoByCarId(String carId){
		log.info("进入getCarCurrentGpsInfoByCarId(String carId)，carId="+carId+",根据车辆id  获取当前车辆最新Gps");
		String dateString = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		Date date2 = calendar.getTime();
		dateString = sdf.format(date)+","+sdf.format(date2);
		Map<String,Object> resultMap = this.cardispatchCarDao.getCarCurrentGpsInfoByCarId(carId, dateString);
		log.info("退出getCarCurrentGpsInfoByCarId(String carId)，carId="+carId+",返回结果："+resultMap);
		return resultMap;
		
	}

	/**
	 * 添加车辆仪表读数与油费
	* @author ou.jh
	* @date Aug 1, 2013 2:06:56 PM
	* @Description: TODO 
	* @param @param cardispatchInstrumentReading
	* @param @param cardispatchFuelBills        
	* @throws
	 */
	public boolean addCardispatchInstrumentReadingAndFuelBills(CardispatchInstrumentReading cardispatchInstrumentReading,CardispatchFuelBills cardispatchFuelBills){
		boolean flag = false;
		log.info("进入addCardispatchInstrumentReadingAndFuelBills方法");
		if(cardispatchInstrumentReading != null){
			log.info("添加车辆仪表读数");
			try {
				cardispatchInstrumentReadingDao.addCardispatchInstrumentReading(cardispatchInstrumentReading);
				flag = true;
			} catch (Exception e) {
				flag = false;
			}
		}else{
			log.info("没有需要添加的车辆仪表读数");
		}
		if(cardispatchFuelBills != null){
			log.info("添加车辆油费");
			try {
				cardispatchFuelBillsDao.addCardispatchFuelBills(cardispatchFuelBills);
				flag = true;
			} catch (Exception e) {
				flag = false;
			}
		}else{
			log.info("没有需要添加的车辆油费");
		}
		log.info("执行addCardispatchInstrumentReadingAndFuelBills方法成功，实现了”添加车辆仪表读数与油费");
		log.info("退出addCardispatchInstrumentReadingAndFuelBills方法,返回boolean");
		return flag;
	}
	/***
	 * 
	 * @description: 根据车辆id获取当前车辆信息
	 * @author：yuan.yw
	 * @param carId
	 * @return     
	 * @return Map<String,String>     
	 * @date：Jul 26, 2013 4:23:14 PM
	 */
	public Map<String,String> getCarInfoByCarId(String carId){
		log.info("进入getCarInfoByCarId(String carId)，carId="+carId+",根据车辆id获取当前车辆信息");
		Map<String,String> resultMap = this.cardispatchCarDao.findByCarId(carId);
		log.info("退出getCarInfoByCarId(String carId)，carId="+carId+",返回结果："+resultMap);
		return resultMap;
	}

	/**
	 * 根据年月获取最大油费 
	* @author ou.jh
	* @date Aug 2, 2013 10:39:57 AM
	* @Description: TODO 
	* @param @param date
	* @param @return        
	* @throws
	 */
	public double getmaxBillsByDateService(String date,long carId){
		log.info("进入getmaxBillsByDateService方法");
		double max = this.cardispatchFuelBillsDao.getMaxBillsByDate(date,carId);
		log.info("执行getmaxBillsByDateService方法成功，实现了”根据年月获取最大油费");
		log.info("退出getmaxBillsByDateService方法,返回double");
		return max;
	}
	

	/**
	 * 根据年月获取最大仪表读数 
	* @author ou.jh
	* @date Aug 2, 2013 10:39:57 AM
	* @Description: TODO 
	* @param @param date
	* @param @return        
	* @throws
	 */
	public double getmaxReadingByDateService(String date,long carId){
		log.info("进入getmaxReadingByDateService方法");
		double max = this.cardispatchInstrumentReadingDao.getMaxReadingByDate(date,carId);
		log.info("执行getmaxReadingByDateService方法成功，实现了”根据年月获取最大仪表读数 ");
		log.info("退出getmaxReadingByDateService方法,返回double");
		return max;
	}
	
	/**
	 * 根据组织ID查找车辆 
	* @author ou.jh
	* @date Aug 2, 2013 10:09:59 AM
	* @Description: TODO 
	* @param @param orgId
	* @param @return        
	* @throws
	 */
	public List<Map<String, Object>> findCarByOrgIdService(long orgId){
		log.info("进入findCarByOrgIdService方法");
		List<Map<String,Object>> findCarByOrgId = this.cardispatchCarDao.findCarByOrgId(orgId);
		log.info("执行findCarByOrgIdService方法成功，实现了”根据组织ID查找车辆");
		log.info("退出findCarByOrgIdService方法,返回List<Map<String,Object>>集合");
		return findCarByOrgId;
	}
	
	/**
	 * 根据车辆仪表读数（分页）
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public List<Map<String,Object>> getCardispatchInstrumentReadingPaging(String startTime,String endTime,long orgId,String carId,int currentPage, int pageSize){
		log.info("进入getCardispatchInstrumentReadingPaging方法");
		List<Map<String,Object>> list = this.cardispatchInstrumentReadingDao.getCardispatchInstrumentReadingPaging(startTime,endTime, orgId, carId, currentPage, pageSize);
		log.info("执行getCardispatchInstrumentReadingPaging方法成功，实现了”根据车辆仪表读数（分页）");
		log.info("退出getCardispatchInstrumentReadingPaging方法,返回List<Map<String,Object>>集合");
		return list;
	}
	
	/**
	 * 根据车辆仪表读数总数
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public int getCardispatchInstrumentReadingCount(String startTime,String endTime,long orgId,String carId){
		log.info("进入getCardispatchInstrumentReadingCount方法");
		int count = this.cardispatchInstrumentReadingDao.getCardispatchInstrumentReadingCount(startTime,endTime, orgId, carId);
		log.info("执行getCardispatchInstrumentReadingPaging方法成功，实现了”根据车辆仪表读数总数");
		log.info("退出getCardispatchInstrumentReadingPaging方法,返回Long");
		return count;
	}
	
	/**
	 * 根据车辆油费（分页）
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public List<Map<String,Object>> getCardispatchFuelBillsPaging(String startTime,String endTime,long orgId,String carId,int currentPage, int pageSize){
		log.info("进入getCardispatchInstrumentReadingPaging方法");
		List<Map<String,Object>> list = this.cardispatchFuelBillsDao.getCardispatchFuelBillsPaging(startTime,endTime, orgId, carId, currentPage, pageSize);
		log.info("执行getCardispatchInstrumentReadingPaging方法成功，实现了”根据车辆仪表读数（分页）");
		log.info("退出getCardispatchInstrumentReadingPaging方法,返回List<Map<String,Object>>集合");
		return list;
	}
	
	/**
	 * 根据车辆油费总数 
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public int getCardispatchFuelBillsCount(String startTime,String endTime,long orgId,String carId){
		log.info("进入getCardispatchFuelBillsCount方法");
		int count = this.cardispatchFuelBillsDao.getCardispatchFuelBillsCount(startTime,endTime, orgId, carId);
		log.info("执行getCardispatchFuelBillsCount方法成功，实现了”根据车辆油费总数” ");
		log.info("退出getCardispatchFuelBillsCount方法,返回Long");
		return count;
	}
	

	/**
	 * 删除仪表读数状态
	* @author ou.jh
	* @date Aug 5, 2013 2:51:17 PM
	* @Description: TODO 
	* @param @param id
	* @param @param deleteUserId        
	* @throws
	 */
	public void deleteCardispatchInstrumentReading(long id,long deleteUserId){
		log.info("进入deleteCardispatchInstrumentReading方法");
		CardispatchInstrumentReading reading = this.cardispatchInstrumentReadingDao.getCardispatchInstrumentReading(id);
		reading.setDeleteTime(new Date());
		reading.setDeleteUserId(deleteUserId);
		reading.setStatus(0);
		this.cardispatchInstrumentReadingDao.updateCardispatchInstrumentReading(reading);
		log.info("执行deleteCardispatchInstrumentReading方法成功，实现了”删除仪表读数状态“ ");
		log.info("退出deleteCardispatchInstrumentReading方法,返回void");
	}
	
	/**
	 * 删除油费状态
	* @author ou.jh
	* @date Aug 5, 2013 2:51:22 PM
	* @Description: TODO 
	* @param @param id
	* @param @param deleteUserId        
	* @throws
	 */
	public void deleteCardispatchFuelBills(long id,long deleteUserId){
		log.info("进入deleteCardispatchFuelBills方法");
		CardispatchFuelBills bills = this.cardispatchFuelBillsDao.getCardispatchFuelBillsBy(id);
		bills.setFuelBillsId(id);
		bills.setDeleteTime(new Date());
		bills.setDeleteUserId(deleteUserId);
		bills.setStatus(0);
		this.cardispatchFuelBillsDao.updateCardispatchFuelBills(bills);
		log.info("执行deleteCardispatchFuelBills方法成功，实现了”删除油费状态“ ");
		log.info("退出deleteCardispatchFuelBills方法,返回void");
	}
	
	/**
	 * 根据ID获取车辆油费 
	* @author ou.jh
	* @date Aug 5, 2013 3:39:15 PM
	* @Description: TODO 
	* @param @param id
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getCardispatchFuelBillsById(long id){
		log.info("进入getCardispatchFuelBillsById方法");
		Map<String, Object> map = this.cardispatchFuelBillsDao.getCardispatchFuelBillsById(id);
		log.info("执行getCardispatchFuelBillsById方法成功，实现了”根据ID获取车辆油费“ ");
		log.info("退出getCardispatchFuelBillsById方法,返回Long");
		return map;
	}
	
	/**
	 * 根据ID获取车辆仪表读数 
	* @author ou.jh
	* @date Aug 5, 2013 3:39:15 PM
	* @Description: TODO 
	* @param @param id
	* @param @return        
	* @throws
	 */
	public Map<String, Object> getCardispatchInstrumentReadingById(long id){
		log.info("进入getCardispatchInstrumentReadingById方法");
		Map<String, Object> map = this.cardispatchInstrumentReadingDao.getCardispatchInstrumentReadingById(id);
		log.info("执行getCardispatchInstrumentReadingById方法成功，实现了”根据ID获取车辆仪表读数 “ ");
		log.info("退出getCardispatchInstrumentReadingById方法,返回Map<String, Object>");
		return map;
	}
	
	/**
	 * 获取车辆当前日期仪表数据
	* @author ou.jh
	* @date Aug 1, 2013 10:47:22 AM
	* @Description: TODO 
	* @param @param date
	* @param @param carId
	* @param @return        
	* @throws
	 */
	public int getReadingCountByDate(String date,long carId){
		log.info("进入getCardispatchInstrumentReadingCount方法");
		int count = this.cardispatchInstrumentReadingDao.getReadingCountByDate(date, carId);
		log.info("执行getCardispatchInstrumentReading方法成功，实现了”获取车辆当前日期仪表数据 “ ");
		log.info("退出getCardispatchInstrumentReading方法,返回int");
		return count;
	}
	
	/**
	 * 导出仪表读数或油费
	* @author ou.jh
	* @date Aug 13, 2013 1:42:23 PM
	* @Description: TODO 
	* @param @param distinction
	* @param @param startTime
	* @param @param endTime
	* @param @param orgId
	* @param @param carId
	* @param @return        
	* @throws
	 */
	public InputStream exportCarReadingOrBillsService(String distinction,String startTime,String endTime,long orgId,String carId){
		log.info("进入exportCarReadingOrBillsService方法");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<String> titlelist = new ArrayList<String>();
		titlelist.add(startTime+"至"+endTime+"里程读数与油费记录");
		titlelist.add("车辆牌照");
		titlelist.add("车辆类型");
		titlelist.add("所属组织");
		if(distinction != null && distinction.equals("Bills")){
			list = this.cardispatchFuelBillsDao.getCardispatchFuelBills(startTime, endTime, orgId, carId);
			titlelist.add("油费(元)");
		}else if(distinction != null && distinction.equals("Reading")){
			list = this.cardispatchInstrumentReadingDao.getCardispatchInstrumentReading(startTime, endTime, orgId, carId);
			titlelist.add("仪表读数(公里)");
		}
		titlelist.add("记录时间");
		titlelist.add("录入人");
		titlelist.add("录入时间");
		titlelist.add("删除人");
		titlelist.add("删除时间");
		titlelist.add("状态");
		//将OutputStream转化为InputStream   
	    ByteArrayOutputStream out = new ByteArrayOutputStream();   
	    //表头
	    
	    putDataOnExcelOutputStream(distinction,out,titlelist,list);   
	    log.info("执行exportCarReadingOrBillsService方法成功，实现了”导出仪表读数或油费 “ ");
		log.info("退出exportCarReadingOrBillsService方法,返回InputStream");
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	/**
	 * 
	 * @description: 生成excel文件流
	 * @author：yuan.yw
	 * @param os
	 * @param list
	 * @param dataList     
	 * @return void     
	 * @date：Aug 5, 2013 10:14:16 AM
	 */
	private void putDataOnExcelOutputStream(String distinction,OutputStream os,List<String> list,List<Map<String, Object>> dataList) {  	
		log.info("进入putDataOnOutputStream(OutputStream os,List<String> list,List<Map<String, Object>> dataList)，生成excel文件。");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {   
           WritableWorkbook wwb = Workbook.createWorkbook(os); 
		   WritableSheet ws  = wwb.createSheet("sheet1",0);
		   ws.setColumnView(0, 20);//单元格宽度
		   ws.setColumnView(1, 20);
		   ws.setColumnView(2, 15);
		   ws.setColumnView(3, 15);
		   ws.setColumnView(4, 20);
		   ws.setColumnView(5, 15);
		   ws.setColumnView(6, 20);
		   ws.setColumnView(7, 15);
		   ws.setColumnView(8, 20);
		   ws.setColumnView(9, 15);
		   //设置字体，内容的居中形式
		   WritableFont  wf = new WritableFont(WritableFont.TIMES,10,WritableFont.BOLD); 
		   WritableCellFormat wff = new WritableCellFormat(wf);
		   wff.setVerticalAlignment(VerticalAlignment.CENTRE);
		   wff.setAlignment(jxl.write.Alignment.CENTRE);
		   wff.setBackground(Colour.ICE_BLUE);
		   
		   WritableCellFormat wff1 = new WritableCellFormat();
		   wff1.setVerticalAlignment(VerticalAlignment.CENTRE);
		   wff1.setAlignment(jxl.write.Alignment.CENTRE);
		   //设置表头的内容
		   Label totalHead =  new Label(0,0,list.get(0),wff);
		   ws.addCell(totalHead);
		   //设置表头的合并
		   ws.mergeCells(0, 0, 9, 0);
		   for(int j=1;j<list.size();j++)
		   {   
			   Label label =  new Label(j-1,1,list.get(j),wff);
			   ws.addCell(label);
		   }
		   //输出查询出的车辆油费数据
		   if(dataList!=null && !dataList.isEmpty()){
			   String bizId = "";
			   String bizName = "";
			   int startIndex = 0;
			   for(int i=0;i<dataList.size();i++){
				  Map<String, Object> map = dataList.get(i);
				  Label  label =  new Label(0,i+2,map.get("carNumber")+"",wff1);//车辆牌照
				  ws.addCell(label);
				  if(map.get("carType")!=null){
					  label =  new Label(1,i+2,map.get("carType")+"",wff1);//车辆类型
				  }else{
					  label = new Label(1,i+2,"",wff1);
				  }
				  ws.addCell(label);
				  if(map.get("orgName")!=null){
					  label =  new Label(2,i+2,map.get("orgName")+"",wff1);//组织
				  }else{
					  label = new Label(2,i+2,"",wff1);
				  }
				  ws.addCell(label);
				  if(distinction != null && distinction.equals("Bills")){
					  if(map.get("fuel_bills")!=null){
						  label =  new Label(3,i+2,map.get("fuel_bills")+"",wff1);//油费
					  }else{
						  label = new Label(3,i+2,"0",wff1);
					  }
				  }else if(distinction != null && distinction.equals("Reading")){
					  if(map.get("instrument_reading")!=null){
						  label =  new Label(3,i+2,map.get("instrument_reading")+"",wff1);//仪表读数
					  }else{
						  label = new Label(3,i+2,"0",wff1);
					  }
				  }
				  ws.addCell(label);
				  if(map.get("recording_time")!=null){
					  Date parse = simpleDateFormat.parse(map.get("recording_time")+"");
					  String format = simpleDateFormat.format(parse);
					  label =  new Label(4,i+2,format,wff1);//记录时间
				  }else{
					  label = new Label(4,i+2,"",wff1);
				  }
				  ws.addCell(label);
				  if(map.get("create_user")!=null){
					  label =  new Label(5,i+2,map.get("create_user")+"",wff1);//录入人
				  }else{
					  label = new Label(5,i+2,"0",wff1);
				  }
				  ws.addCell(label);
				  if(map.get("create_time")!=null){
					  Date parse = simpleDateFormat2.parse(map.get("create_time")+"");
					  String format = simpleDateFormat2.format(parse);
					  label =  new Label(6,i+2,format,wff1);//录入时间
				  }else{
					  label = new Label(6,i+2,"",wff1);
				  }
				  ws.addCell(label);
				  if(map.get("delete_user")!=null){
					  label =  new Label(7,i+2,map.get("delete_user")+"",wff1);//删除人
				  }else{
					  label = new Label(7,i+2,"",wff1);
				  }
				  ws.addCell(label);
				  if(map.get("delete_time")!=null){
					  Date parse = simpleDateFormat2.parse(map.get("delete_time")+"");
					  String format = simpleDateFormat2.format(parse);
					  label =  new Label(8,i+2,format,wff1);//删除时间
				  }else{
					  label = new Label(8,i+2,"",wff1);
				  }
				  ws.addCell(label);
				  if(map.get("status")!=null){
					  String status = "";
					  if(map.get("status").toString().equals(1) || map.get("status").toString().equals("1")){
						  status = "正常";
					  }else{
						  status = "无效";
					  }
					  label =  new Label(9,i+2,status,wff1);//状态
				  }else{
					  label = new Label(9,i+2,"",wff1);
				  }
				  ws.addCell(label);
			   }
		   }
		  wwb.write();   
		  wwb.close();   
		  log.info("退出putDataOnOutputStream(OutputStream os,List<String> list,List<Map<String, Object>> dataList)，生成excel文件流成功");
      } catch (Exception e) {   
    	  log.error("退出putDataOnOutputStream(OutputStream os,List<String> list,List<Map<String, Object>> dataList)，生成excel文件流失败，exception："+e.getMessage());   
      }   

  }   
	
	/**
	 * 根据年月获取最小仪表读数
	* @author ou.jh
	* @date Aug 1, 2013 11:08:01 AM
	* @Description: TODO 
	* @param @param id        
	* @throws
	 */
	public double getMinReadingByDate(String date,long carId){
		log.info("进入getMinReadingByDateService方法");
		double min = this.cardispatchInstrumentReadingDao.getMinReadingByDate(date,carId);
		log.info("执行getMinReadingByDateService方法成功，实现了”根据年月获取最小仪表读数 ");
		log.info("退出getMinReadingByDateService方法,返回double");
		return min;
	}
	
	
	/***************** getter setter *******************/
	public CardispatchCarDao getCardispatchCarDao() {
		return cardispatchCarDao;
	}
	public void setCardispatchCarDao(CardispatchCarDao cardispatchCarDao) {
		this.cardispatchCarDao = cardispatchCarDao;
	}
	public CardispatchDriverDao getCardispatchDriverDao() {
		return cardispatchDriverDao;
	}
	public void setCardispatchDriverDao(CardispatchDriverDao cardispatchDriverDao) {
		this.cardispatchDriverDao = cardispatchDriverDao;
	}
	public CardispatchTerminalDao getCardispatchTerminalDao() {
		return cardispatchTerminalDao;
	}
	public void setCardispatchTerminalDao(
			CardispatchTerminalDao cardispatchTerminalDao) {
		this.cardispatchTerminalDao = cardispatchTerminalDao;
	}
	
	public WorkManageService getWorkManageService() {
		return workManageService;
	}
	public void setWorkManageService(WorkManageService workManageService) {
		this.workManageService = workManageService;
	}
	public CardispatchGpsDao getCardispatchGpsDao() {
		return cardispatchGpsDao;
	}
	public void setCardispatchGpsDao(CardispatchGpsDao cardispatchGpsDao) {
		this.cardispatchGpsDao = cardispatchGpsDao;
	}
	public CardispatchWorkorderDao getCardispatchWorkorderDao() {
		return cardispatchWorkorderDao;
	}
	public void setCardispatchWorkorderDao(
			CardispatchWorkorderDao cardispatchWorkorderDao) {
		this.cardispatchWorkorderDao = cardispatchWorkorderDao;
	}


	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}

	public SysAccountService getSysAccountService() {
		return sysAccountService;
	}

	public void setSysAccountService(SysAccountService sysAccountService) {
		this.sysAccountService = sysAccountService;
	}

	public CardispatchInstrumentReadingDao getCardispatchInstrumentReadingDao() {
		return cardispatchInstrumentReadingDao;
	}

	public void setCardispatchInstrumentReadingDao(
			CardispatchInstrumentReadingDao cardispatchInstrumentReadingDao) {
		this.cardispatchInstrumentReadingDao = cardispatchInstrumentReadingDao;
	}

	public CardispatchFuelBillsDao getCardispatchFuelBillsDao() {
		return cardispatchFuelBillsDao;
	}

	public void setCardispatchFuelBillsDao(
			CardispatchFuelBillsDao cardispatchFuelBillsDao) {
		this.cardispatchFuelBillsDao = cardispatchFuelBillsDao;
	}
	

	
	
	
}
