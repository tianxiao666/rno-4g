package com.iscreate.op.service.cardispatch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.informationmanage.common.ArrayUtil;
import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.constant.CardispatchConstant.CardispatchCarState;
import com.iscreate.op.dao.cardispatch.CardispatchCarDao;
import com.iscreate.op.dao.cardispatch.CardispatchDriverDao;
import com.iscreate.op.dao.cardispatch.CardispatchTerminalDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.cardispatch.CardispatchCar;
import com.iscreate.op.pojo.system.SysAccount;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysUserRelaOrg;
import com.iscreate.op.service.informationmanage.BaseServiceImpl;
import com.iscreate.op.service.system.SysAccountService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.workmanage.WorkManageService;
import com.mockrunner.util.common.StringUtil;


@SuppressWarnings({"rawtypes","unchecked"})
public class CardispatchManageServiceImplForMobile extends BaseServiceImpl<CardispatchCar> implements CardispatchManageServiceForMobile {

	/************ 依赖注入 ***********/
	CardispatchCarDao cardispatchCarDao;
	CardispatchDriverDao cardispatchDriverDao;
	private CardispatchTerminalDao cardispatchTerminalDao;
	private WorkManageService workManageService;
	private SysOrgUserService sysOrgUserService;
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	private SysOrganizationDao sysOrganizationDao;//ou.jh
	private SysAccountService sysAccountService;
	
	
	public SysAccountService getSysAccountService() {
		return sysAccountService;
	}

	public void setSysAccountService(SysAccountService sysAccountService) {
		this.sysAccountService = sysAccountService;
	}

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
	
	
	private Log log = LogFactory.getLog(this.getClass());
	/****************** service *******************/
	
	
	
	/********************** 车辆 begin **********************/
	
	/**
	 * 根据车辆id,查询车辆信息状态
	 * @param carId - 车辆id
	 * @return 车辆信息 - stateCName 为车辆状态中文
	 */
	public Map<String,String> getCarStateByCarId ( String carId ) {
		Map<String,String> result_map = cardispatchCarDao.getCarStateByCarId(carId);
		//中文解释
		Integer terminalState = 0;
		if ( result_map.containsKey("terminalState") && result_map.get("terminalState") != null ) {
			terminalState = Integer.valueOf(result_map.get("terminalState"));
		}
		String cName = CardispatchCarState.getCNameByNum(terminalState);
		result_map.put("stateCName", cName);
		return result_map;
	}
	
	
	public Map<String,String> getCarByCarDriverPairId ( String cardriverpairId ) {
		Map<String,String> result_map = cardispatchCarDao.getCarByCarDriverPairId(cardriverpairId);
		return result_map;
	}
	
	
	public Map<String,String> getCarTopGps ( String carNumber ) {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> carMap = cardispatchCarDao.findByCarNumber(carNumber);
		if ( carMap != null && carMap.size() > 0 ) {
			String carId = carMap.get("carId");
			String terminalId = cardispatchCarDao.findTerminalIdByCarId(carId);
			map = cardispatchTerminalDao.findMaxGpsByClientId(terminalId);
		}
		return map;
	}
	
	public Map<String,String> findCarInfoById ( String carId ) {
		Map<String, String> car = cardispatchCarDao.findCarAllInfoById(carId);
		//获取司机组织
		findDriveBizByAccount(car);
		//获取车辆组织
		findCarBizName(car);
		return car;
	}
	
	
	public Map<String,Object> getCarGpsInTime ( String carNumber ,String startTime , String endTime ) {
		Map<String,Object> result_map = new LinkedHashMap<String, Object>();
		//查询车辆gps轨迹
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> carMap = cardispatchCarDao.findByCarNumber(carNumber);
		if ( carMap != null && carMap.size() > 0 ) {
			String carId = carMap.get("carId");
			String terminalId = cardispatchCarDao.findTerminalIdByCarId(carId);
			list = cardispatchTerminalDao.findTerminalGpsList(terminalId, startTime, endTime);
			for (int i = 0; i < list.size() ; i++) {
				Map<String, String> driver = list.get(i);
				findDriveBizByAccount(driver);
			}
			//查询车辆最新gps
			Map<String, String> map = cardispatchTerminalDao.findMaxGpsByClientId(terminalId);
			result_map.put("gpsList", list);
			result_map.put("mostNewGPSLocation", map);
		}
		return result_map;
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
				String id = car.get("id");
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
		List<String> bizIds = new ArrayList<String>();
		for (int i = 0; i < pros.size(); i++) {
			SysOrg p = pros.get(i);
			bizIds.add(p.getOrgId()+"");
		}
		param_map.put("carBizId", bizIds);
		List<Map<String, Object>> list = cardispatchCarDao.findCarDriverPairList(param_map);
		return list;
	}
	
	
	public List<Map<String, Object>> findCarList( Map param_map ) {
		if ( param_map.containsKey("carBizId") && param_map.get("carBizId") != null ) {
			//List<ProviderOrganization> pros = providerOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("carBizId")+""));
			//yuan.yw
			List<SysOrg> pros = this.sysOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("carBizId")+""));
			List<String> bizIds = new ArrayList<String>();
			for (int i = 0; i < pros.size(); i++) {
				SysOrg p = pros.get(i);
				bizIds.add(p.getOrgId()+"");
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
		List<Map<String, Object>> list = cardispatchCarDao.findCarDriverPairListIsDuty(param_map,duty_param_Map);
		List<Map<String, Object>> result_list = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String isArr = map.get("isArranged")+"";
			if ( isArranged == null || isArranged.isEmpty() || isArranged.equals("null")) {
				long totalTask = workManageService.getTaskOrderCountByObjectTypeAndObjectId("car", map.get("carId")+"");
				map.put("totalTask", totalTask);
				result_list.add(map);
			} else if ( isArr.equals(isArranged) ) {
				long totalTask = workManageService.getTaskOrderCountByObjectTypeAndObjectId("car", map.get("carId")+"");
				map.put("totalTask", totalTask);
				result_list.add(map);
			}
		}
		return result_list;
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
		map.put("car_id",carId);
		map.put("driver_id",driverId);
		map.put("status",1);
		try {
			flag = cardispatchCarDao.carBindDriver(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	

	public boolean saveDriverAndBindCar ( Map staff_map , Map driver_map , String carId , String orgId ){
		//保存司机
		boolean flag = cardispatchDriverDao.saveDriver( driver_map);
		//保存人员
//		Account account = new Account();
//		account.setName(driver_map.get("name")+"");
//		account.setAccount(driver_map.get("accountId")+"");
		//ou.jh
		SysAccount sysAccount = new SysAccount();
		sysAccount.setAccount(driver_map.get("accountId")+"");
		SysOrgUser sysOrgUser = new SysOrgUser();
		sysOrgUser.setName(driver_map.get("name")+"");
		Serializable txSaveAccount = null;
		Serializable txSaveSysOrgUser = null;
		try {
			//保存人员
			txSaveSysOrgUser = sysOrgUserService.txSaveSysOrgUser(sysOrgUser);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long sysOrgUserId = 0;
		if(txSaveSysOrgUser != null && !txSaveSysOrgUser.equals("")){
			sysOrgUserId = Long.parseLong(txSaveSysOrgUser.toString());
		}else{
			sysOrgUserId = 0;
		}
		if(sysOrgUserId == 0){
			flag = false;
		}else{
			flag = true;
		}
		sysAccount.setOrgUserId(sysOrgUserId);
		try {
			//保存人员
			txSaveAccount = sysAccountService.txSaveAccount(sysAccount);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long accountId = 0;
		if(txSaveAccount != null && !txSaveAccount.equals("")){
			accountId = Long.parseLong(txSaveAccount.toString());
		}else{
			accountId = 0;
		}
		if(accountId == 0){
			flag = false;
		}else{
			flag = true;
		}
		//清空关系
		flag = cardispatchCarDao.clearCarBindDriver( carId , null);
		SysUserRelaOrg so = new SysUserRelaOrg();
		so.setOrgId(Long.valueOf(orgId));
		so.setOrgUserId(sysOrgUserId);
		so.setCreatetime(new Date());
		//绑定组织架构
		this.sysOrganizationDao.saveEntity(so);
		//this.providerOrganizationService.addOrgAndStaffAjaxService(Long.valueOf(orgId),driver_map.get("accountId")+"");
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
	
	public boolean bindCarAndTerminal ( String carId , String driverId ){
		//清空关系
		boolean flag = cardispatchCarDao.clearCarBindTerminal( carId , driverId );
		//绑定关系
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("car_id",carId);
		map.put("terminal_id",driverId);
		map.put("allocateTime",DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
		try {
			flag = cardispatchCarDao.carBindTerminal(map);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return flag;
	}
	

	public boolean saveTerminalAndBindCar ( Map terminal_map , String carId ){
		boolean flag = cardispatchTerminalDao.saveTerminal(terminal_map);
		//清空关系
		flag = cardispatchCarDao.clearCarBindTerminal( carId , terminal_map.get("terminalId")+"" );
		//绑定车辆关系
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("car_id",carId);
		map.put("terminal_id",terminal_map.get("terminalId"));
		map.put("allocateTime",DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
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

	public boolean txdeleteDriverByIds ( String ids ) {
		String[] driver_ids = ids.split(",");
		List<String> ids_list = Arrays.asList(driver_ids);
		boolean flag = cardispatchDriverDao.deleteDriverByIds(ids_list);
		return flag;
	}
	
	public boolean txSaveDriver ( Map staff_map , Map driver_map , String orgId ) {
		//保存司机
		boolean flag = cardispatchDriverDao.saveDriver( driver_map);
		if ( flag ) {
//			Account account = new Account();
//			account.setName(driver_map.get("driverName")+"");
//			account.setAccount(driver_map.get("accountId")+"");
//			flag = this.providerOrganizationService.txSaveAccountService(account);
			//ou.jh
			SysAccount sysAccount = new SysAccount();
			sysAccount.setAccount(driver_map.get("accountId")+"");
			SysOrgUser sysOrgUser = new SysOrgUser();
			sysOrgUser.setName(driver_map.get("name")+"");
			Serializable txSaveAccount = null;
			Serializable txSaveSysOrgUser = null;
			try {
				//保存人员
				txSaveSysOrgUser = sysOrgUserService.txSaveSysOrgUser(sysOrgUser);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			long sysOrgUserId = 0;
			if(txSaveSysOrgUser != null && !txSaveSysOrgUser.equals("")){
				sysOrgUserId = Long.parseLong(txSaveSysOrgUser.toString());
			}else{
				sysOrgUserId = 0;
			}
			if(sysOrgUserId == 0){
				flag = false;
			}else{
				flag = true;
			}
			sysAccount.setOrgUserId(sysOrgUserId);
			try {
				//保存人员
				txSaveAccount = sysAccountService.txSaveAccount(sysAccount);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			long accountId = 0;
			if(txSaveAccount != null && !txSaveAccount.equals("")){
				accountId = Long.parseLong(txSaveAccount.toString());
			}else{
				accountId = 0;
			}
			if(accountId == 0){
				flag = false;
			}else{
				flag = true;
			}
			//清空关系
			SysUserRelaOrg so = new SysUserRelaOrg();
			so.setOrgId(Long.valueOf(orgId));
			so.setOrgUserId(sysOrgUserId);
			so.setCreatetime(new Date());
			//绑定组织架构
			this.sysOrganizationDao.saveEntity(so);
//			this.providerOrganizationService.addOrgAndStaffAjaxService(Long.valueOf(orgId),driver_map.get("accountId")+"");
		}
		return flag;
	}
	
	
	public boolean txUpdateDriverById ( Map staff_map , Map driver_map , String orgId )  {
		return false;
	}
	
	/********************** 司机 end **********************/
	
	
	/********************** 终端 begin **********************/
	
	public boolean checkTerminalImeiIsExists ( String clientImei ) {
		boolean isExists = false;
		isExists = cardispatchTerminalDao.checkTerminalImeiIsExists(clientImei);
		return isExists;
	}
	

	public List<Map<String, String>> getNotDriverStaffAutoComplete ( String driverName ) {
		List<Map<String, String>> list = cardispatchDriverDao.getNotDriverStaffListByNameOrAccount(driverName,null);
		return list;
	}
	
	
	public List<Map<String, Object>> findTerminalList( Map param_map , Boolean isFree ) {
		if ( param_map.containsKey("terminalBizId") && param_map.get("terminalBizId") != null ) {
			//List<ProviderOrganization> pros = providerOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("terminalBizId")+""));
			//yuan.yw
			List<SysOrg> pros = this.sysOrganizationService.getOrgListDownwardByOrg( Long.valueOf(param_map.get("terminalBizId")+""));
			List<String> bizIds = new ArrayList<String>();
			for (int i = 0; i < pros.size(); i++) {
				SysOrg p = pros.get(i);
				bizIds.add(p.getOrgId()+"");
			}
			param_map.put("terminalBizId", bizIds);
		}
		List<Map<String, Object>> list = cardispatchTerminalDao.findTerminalList(param_map , isFree );
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			ArrayUtil.setMapDefault(map, "0", "monthlyRent");
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
	
	
	
	
	
	
	/***************** 格式化 ******************/
	
	/**
	 * 根据司机账号获取该账号组织中文名
	 * @param driver - 信息(key包含accountId) 
	 * @key driverBizName - 司机组织中文名
	 * @key driverBizId - 司机组织Id
	 */
	public void findDriveBizByAccount ( Map<String,String> driver ) {
		//获取司机组织
		if ( driver.containsKey("accountId") && !StringUtil.isEmptyOrNull(driver.get("accountId")) ) {
			//List<ProviderOrganization> orgByAccountService = this.providerOrganizationService.getTopLevelOrgByAccount(driver.get("accountId"));
			//yuan.yw
			List<SysOrg> orgByAccountService = this.sysOrganizationService.getTopLevelOrgByAccount(driver.get("accountId"));
			
			if ( orgByAccountService != null && orgByAccountService.size() > 0 ) {
				driver.put("driverBizName", orgByAccountService.get(0).getName());
				driver.put("driverBizId", orgByAccountService.get(0).getOrgId()+"");
			}
		}
	}
	
	/**
	 * 根据车辆组织id,获取组织中文名
	 * @param car - 信息(key包含carBizId)
	 * @key carBizName - 车辆组织中文名
	 */
	public void findCarBizName ( Map<String,String> car ) {
		if ( car.containsKey("carBizId") && !StringUtil.isEmptyOrNull(car.get("carBizId")) ) {
			Long carBizId = Long.valueOf(car.get("carBizId"));
			Map<String,Object> orgByOrgIdService = sysOrganizationDao.getOrganizationMessageByOrgId(carBizId);
			
			car.put("carBizName", (String) orgByOrgIdService.get("name"));
		}
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

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}





	
	
	
}
