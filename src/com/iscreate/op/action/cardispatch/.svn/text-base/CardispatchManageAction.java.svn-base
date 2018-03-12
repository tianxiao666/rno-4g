package com.iscreate.op.action.cardispatch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.action.informationmanage.common.ArrayUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.pojo.cardispatch.CardispatchFuelBills;
import com.iscreate.op.pojo.cardispatch.CardispatchInstrumentReading;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.cardispatch.CardispatchManageService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.plat.login.constant.UserInfo;
import com.iscreate.plat.tools.paginghelper.PagingHelper;
import com.iscreate.plat.tools.map.MapHelper;
import com.opensymphony.xwork2.ActionContext;


/**
 * 
 * @author andy
 * cardispatchManage_ajax
 */
@SuppressWarnings("unused")
public class CardispatchManageAction {

	
	/************* 依赖注入 *************/
	private CardispatchManageService cardispatchManageService;
	private SysOrganizationService sysOrganizationService;
	/********* 属性 *********/
	private Log log = LogFactory.getLog(this.getClass());
	
	/**************** 页面数据 ******************/
	private File[] file;
	private String fileNameString;
	private String[] fileFileName;
	private String[] fileContentType;
	private long orgId;
	private String orgName;
	private String startTime;
	private String endTime;
	private String carId;
	private int currentPage;
	private int pageSize;
	private String distinction;
	private long total;
	private List<Map<String, Object>> pageList;
	private long id;
	private Map<String, Object> dataMap;
	private InputStream excelStream;
	private String filename;
	
	/********** XML 使用属性 ************/
	private String url;
	

	/************************* Action ******************************/
	
	/**************** 车辆 begin *****************/
	
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * 更新车辆信息
	 * (请求参数) car#
	 * (响应) (boolean) 是否操作成功 (true：成功)
	 */
	public void updateCar () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMapByChoiceMap("car#");
		ArrayUtil.removeEmpty(requestParamMap);
		Long id = 0l;
		boolean flag = cardispatchManageService.updateCar(requestParamMap);
		try {
			ActionUtil.responseWrite(flag);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取车辆最新gps位置
	 * (请求参数) carNumber 车牌号
	 * (响应) (Map<String,String>) 车辆信息
	 */
	public void getCarTopGps () {
		String carNumber = ActionUtil.getParamString("carNumber");
		Map<String, String> gps = cardispatchManageService.getCarTopGps(carNumber);
		try {
			ActionUtil.responseWrite(gps);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据车辆id，获取车辆信息
	 * (请求参数) carId
	 * (响应) (Map<String,String>) 车辆信息
	 */
	public void findCarInfoById () {
		String carId = ActionUtil.getParamString("carId");
		Map<String, String> car = cardispatchManageService.findCarInfoById(carId);
		//根据经纬度获取实际地址
		String address = "";
		if(car!=null && !car.isEmpty()){
			String longitude =  car.get("longitude");
			String latitude = car.get("latitude");
			if(longitude!=null && !"".equals(longitude)&&latitude!=null && !"".equals(latitude)){
				address = MapHelper.convertLatlngToAddress(longitude,latitude);
			}
			car.put("address", address);
		}
		try {
			ActionUtil.responseWrite(car);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据参数，获取车辆信息列表
	 * (请求参数) car# 
	 * (响应) 车辆信息集合
	 */
	public void findCarInfoList () {
		Map<String, Object> request_param_map = ActionUtil.getRequestParamMapByChoiceMapObject("car#");
		List<Map<String, Object>> list = cardispatchManageService.findCarList(request_param_map);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据参数，获取车辆配对信息列表
	 * (请求参数) car#
	 */
	public void findCarPairInfoList () {
		Map<String, Object> request_param_map = ActionUtil.getRequestParamMapByChoiceMapObject("car#");
		List<Map<String, Object>> list = cardispatchManageService.findCarDriverPairList(request_param_map);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据车辆Id数组，删除车辆信息
	 * (请求参数) carIds(车辆id数组)
	 * (响应) 删除是否成功(true：成功)
	 */
	public void deleteCarByIdsAjax () {
		String ids = ActionUtil.getParamString("carIds");
		boolean flag = cardispatchManageService.txdeleteCarByIds(ids);
		try {
			ActionUtil.responseWrite(flag+"");
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void getCarGpsInTime () {
		String carNumber = ActionUtil.getParamString("carNumber");
		String startTime = ActionUtil.getParamString("beginTime");
		String endTime = ActionUtil.getParamString("endTime");
		String orgId = ActionUtil.getParamString("orgId");
		Map<String, Object> carGpsInTime = cardispatchManageService.getCarGpsInTime(carNumber,startTime,endTime,orgId);
		try {
			ActionUtil.responseWrite(carGpsInTime);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 获取心跳包信息
	 * 
	 * @author Li.hb
	 * @date 2013-08-14
	 * 
	 */
	public void getCarHeartbeat()
	{
		
		String carNumber = ActionUtil.getParamString("carNumber");
		String startTime = ActionUtil.getParamString("beginTime");
		String endTime = ActionUtil.getParamString("endTime");
		
		Map<String, Object> carHeartbeat = cardispatchManageService.getCarHeartbeat(carNumber, startTime, endTime);
		
		try {
			ActionUtil.responseWrite(carHeartbeat);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	
	
	
	/**
	 * 根据组织ID查看车辆牌照
	 */
	public void getCarnumber()
	{
		String orgId = ActionUtil.getParamString("orgId");
		
		List<String> List = cardispatchManageService.findCarNumber(orgId);
		
		try {
			ActionUtil.responseWrite(List);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * 根据车牌号获取车辆最新坐标
	 */
	
	public void getGPSLoactionByCarNumber()
	{
		String carNumber = ActionUtil.getParamString("carNumber");
		
		Map<String, String> findGPSLoactionByCarNumber = cardispatchManageService.findGPSLoactionByCarNumber(carNumber);
		
		try {
			ActionUtil.responseWrite(findGPSLoactionByCarNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void checkCarNumberExists () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("carNumber","carId");
		ArrayUtil.removeEmpty(requestParamMap);
		String carNumber = requestParamMap.get("carNumber");
		String carId = requestParamMap.get("carId");
		boolean isExit = cardispatchManageService.checkCarNumberExists( carNumber  , carId );
		try {
			ActionUtil.responseWrite(isExit);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据车辆id,获取车辆最后一次还车里程读数
	 * @param carId 车辆id
	 * @return 车辆信息
	 */
	public void findCarLastMileage () {
		String carId = ActionUtil.getParamString("carId");
		Double mileage = cardispatchManageService.findCarLastMileage(carId);
		try {
			ActionUtil.responseWrite(mileage);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void a () {
		try {
			ActionUtil.responseWrite("a");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**************** 车辆 end *****************/
	
	/**************** 车辆流程 begin *****************/
	
	public void saveCarAjax () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMapByChoiceMap("car#");
		ArrayUtil.removeEmpty(requestParamMap);
		Long id = 0l;
		if ( requestParamMap.containsKey("id") ) {
			boolean flag = cardispatchManageService.updateCar(requestParamMap);
			id = Long.valueOf(requestParamMap.get("id"));
		} else {
			id = cardispatchManageService.saveCar(requestParamMap);
		}
		requestParamMap.put("carId", id+"");
		try {
			ActionUtil.responseWrite(requestParamMap);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void bindCarAndDriverAjax () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("carId","driverId");
		String carId = requestParamMap.get("carId");
		String driverId = requestParamMap.get("driverId");
		boolean flag = cardispatchManageService.bindCarAndDriver( carId , driverId );
		if ( flag ) {
			try {
				ActionUtil.responseWrite(requestParamMap);
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 保存司机,并绑定车辆
	 */
	public void saveDriverAndBindCarAjax () {
		Map<String, String> driver_param_map = ActionUtil.getRequestParamMapByChoiceMap("driver#");
		String carId = ActionUtil.getParamString("carId");
		boolean flag = cardispatchManageService.saveDriverAndBindCar( driver_param_map, carId);
		if ( flag ) {
			try {
				ActionUtil.responseWrite(driver_param_map);
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	
	public void bindCarAndTerminalAjax () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("carId","terminalId");
		String carId = requestParamMap.get("carId");
		String terminalId = requestParamMap.get("terminalId");
		boolean flag = cardispatchManageService.bindCarAndTerminal( carId , terminalId );
		if ( flag ) {
			try {
				ActionUtil.responseWrite(requestParamMap);
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	
	public void saveTerminalAndBindCarAjax () {
		Map<String, String> terminal_param_map = ActionUtil.getRequestParamMapByChoiceMap("terminal#");
		String carId = ActionUtil.getParamString("carId");
		boolean flag = cardispatchManageService.saveTerminalAndBindCar( terminal_param_map , carId );
		if ( flag ) {
			try {
				ActionUtil.responseWrite(terminal_param_map);
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public void getCarByCarDriverPairId () {
		String cardriverpairId = ActionUtil.getParamString("cardriverpairId");
		Map<String, String> pairInfo = cardispatchManageService.getCarByCarDriverPairId(cardriverpairId);
		try {
			ActionUtil.responseWrite(pairInfo);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**************** 车辆流程 end *****************/
	
	
	/**************** 司机 begin *****************/
	
	
	public void checkAccountIdIsExists () {
		String accountId = ActionUtil.getParamString("driver#accountId");
		boolean isExists = cardispatchManageService.checkAccountId(accountId);
		try {
			ActionUtil.responseWrite(isExists);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 司机信息管理 - 司机信息列表
	 * @page 
	 * (响应) 司机信息集合
	 */
	public void findDriverInfoList () {
		Map<String, Object> request_param_map = ActionUtil.getRequestParamMapByChoiceMapObject("driver#");
		ArrayUtil.removeEmpty(request_param_map);
		String isFreeString = ActionUtil.getParamString("isFree");
		Boolean isFree = null;
		if ( isFreeString != null ) {
			isFree = Boolean.valueOf(isFreeString);
		}
		List<Map<String, Object>> list = cardispatchManageService.findDriverList(request_param_map,isFree);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void deleteDriverByIdsAjax () {
		String ids = ActionUtil.getParamString("driverIds");
		boolean flag = cardispatchManageService.txdeleteDriverByIds(ids);
		try {
			ActionUtil.responseWrite(flag+"");
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存司机信息
	 */
	public void saveDriver () {
		Map<String, String> driver_param_map = ActionUtil.getRequestParamMapByChoiceMap("driver#");
		//保存司机
		boolean flag = cardispatchManageService.txSaveDriver( driver_param_map );
		try {
			ActionUtil.responseWrite(flag+"");
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据司机id,更新司机信息
	 */
	public void updateDriverById () {
		Map<String, String> driver_param_map = ActionUtil.getRequestParamMapByChoiceMap("driver#");
		//保存司机
		boolean flag = cardispatchManageService.txUpdateDriverById( driver_param_map);
		try {
			ActionUtil.responseWrite(flag+"");
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**************** 司机 end *****************/
	
	
	/**************** 终端 begin *****************/
	
	
	public void checkTerminalImeiIsExists () {
		String clientimei = ActionUtil.getParamString("terminal#clientimei");
		boolean isExists = cardispatchManageService.checkTerminalImeiIsExists(clientimei);
		try {
			ActionUtil.responseWrite(isExists);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 终端信息管理 - 终端信息列表
	 * (响应) 终端信息集合
	 */
	public void findMobileInfoList () {
		Map<String, Object> request_param_map = ActionUtil.getRequestParamMapByChoiceMapObject("terminal#");
		String isFreeString = ActionUtil.getParamString("isFree");
		ArrayUtil.removeEmpty(request_param_map);
		Boolean isFree = null;
		if ( isFreeString != null ) {
			isFree = Boolean.valueOf(isFreeString);
		}
		List<Map<String, Object>> list = cardispatchManageService.findTerminalList(request_param_map,isFree);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 终端待绑定列表
	 * 
	 * @author li.hb
	 * @createTime 2013.06.26
	 * 
	 */
	public void findMobileBindingList () {
		Map<String, Object> request_param_map = ActionUtil.getRequestParamMapByChoiceMapObject("terminal#");
		String isFreeString = ActionUtil.getParamString("isFree");
		ArrayUtil.removeEmpty(request_param_map);
		Boolean isFree = null;
		if ( isFreeString != null ) {
			isFree = Boolean.valueOf(isFreeString);
		}
		List<Map<String, Object>> list = cardispatchManageService.findTerminalBindingList(request_param_map,isFree);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void deleteTerminalByIdsAjax () {
		String ids = ActionUtil.getParamString("termianlIds");
		boolean flag = cardispatchManageService.txdeleteTerminalByIds(ids);
		try {
			ActionUtil.responseWrite(flag+"");
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public void saveTerminal () {
		Map<String, String> terminal_param_map = ActionUtil.getRequestParamMapByChoiceMap("terminal#");
		//保存终端
		boolean flag = cardispatchManageService.txSaveTerminal(terminal_param_map);
		try {
			ActionUtil.responseWrite(flag+"");
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void updateTerminalById () {
		Map<String, String> terminal_param_map = ActionUtil.getRequestParamMapByChoiceMap("terminal#");
		//更新终端
		boolean flag = cardispatchManageService.txUpdateTerminalById(terminal_param_map);
		try {
			ActionUtil.responseWrite(terminal_param_map);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	/**************** 终端 end *****************/
	
	/**************** gps里程统计 begin *****************/
	
	/**
	 * 查询组织下的所有车辆时间内的里程读数
	 */
	public void findCarMileageByBizInTime () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("bizId","date","startDate","endDate");
		String bizId = requestParamMap.get("bizId");
		String date_string = requestParamMap.get("date");
		String startDate = requestParamMap.get("startDate");
		String endDate = requestParamMap.get("endDate");
		if ( StringUtil.isNullOrEmpty(date_string) ) {
			log.error("date is null!");
		}
		Gson gson = new Gson();
		List<String> date_list = gson.fromJson(date_string, new TypeToken<List<String>>() {}.getType());
		
		
		List<Map<String, String>> result_map = this.cardispatchManageService.findCarMileageByBizInTime(bizId, date_list,startDate, endDate);
		
		
		Map session = ActionContext.getContext().getSession(); 
		
		session.put("carData", result_map);
		session.put("carDate", date_list);
		
		try {
			ActionUtil.responseWrite(result_map);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	
	/**************** gps里程统计 end *****************/
	
	/**
	 * 添加车辆仪表读数与油费
	* @author ou.jh
	* @date Aug 1, 2013 2:46:11 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void addInstrumentReadingAndFuelBillsAction(){
		Map<String, String> requestParamMap = 
			ActionUtil.getRequestParamMap("carId","instrumentReading","readingRecordingTime","readingCreateRemarks","fuelBills","billsCreateRemarks","billsRecordingTime");
		long carId = Long.parseLong(requestParamMap.get("carId"));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		long org_user_id = Long.parseLong(SessionService.getInstance().getValueByKey(UserInfo.ORG_USER_ID)+"");
		Date now = new Date();
		CardispatchInstrumentReading reading = null;
		CardispatchFuelBills bills = null;
		String result = "";
		Map<String, Object> result_map = new HashMap<String, Object>();
		//判断仪表读数与读数录入时间是否为空
		if(requestParamMap.get("instrumentReading") == null || requestParamMap.get("instrumentReading").equals("")
				|| requestParamMap.get("readingRecordingTime") == null || requestParamMap.get("readingRecordingTime").equals("")){
			log.info("没有需要添加的车辆仪表读数");
		}else{
			log.info("添加车辆仪表读数");
			//仪表读数
			double instrumentReading = Double.parseDouble(requestParamMap.get("instrumentReading"));
			//备注
			String readingCreateRemarks = requestParamMap.get("readingCreateRemarks");
			//读数录入时间
			String recordingTime = requestParamMap.get("readingRecordingTime");
			//获取车辆当前日期仪表数据 
			int count = this.cardispatchManageService.getReadingCountByDate(recordingTime, carId);
			if(count > 0){
				result_map.put("message", "日期:" + recordingTime + "已存在仪表读数，如需要添加请删除原有数据。");
				result_map.put("flag", false);
				try {
					ActionUtil.responseWrite(result_map);
					return;
				} catch (IOException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
			//判断填写上一次最近日期记录
			double max = this.cardispatchManageService.getmaxReadingByDateService(recordingTime,carId);
			if(instrumentReading < max){
				result_map.put("message", "填写的仪表读数请大于上一次记录的仪表读数 "+ max + "(公里)");
				result_map.put("flag", false);
				try {
					ActionUtil.responseWrite(result_map);
					return;
				} catch (IOException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
			
			//判断填写上一次最近日期记录
			double min = this.cardispatchManageService.getMinReadingByDate(recordingTime,carId);
			if(instrumentReading > min && min != 0){
				result_map.put("message", "填写的仪表读数小于当前记录时间之后已录入的仪表读数 "+ min + "(公里)");
				result_map.put("flag", false);
				try {
					ActionUtil.responseWrite(result_map);
					return;
				} catch (IOException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
			Date recordingDate = null;
			try {
				recordingDate = simpleDateFormat.parse(recordingTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//创建仪表读数pojo
			reading = new CardispatchInstrumentReading();
			reading.setCarId(carId);
			reading.setCreateRemarks(readingCreateRemarks);
			reading.setCreateTime(now);
			reading.setCreateUserId(org_user_id);
			reading.setInstrumentReading(instrumentReading);
			reading.setRecordingTime(recordingDate);
			reading.setStatus(1);
		}
		//判断油费与油费录入时间
		if(requestParamMap.get("fuelBills") == null || requestParamMap.get("fuelBills").equals("")
				|| requestParamMap.get("billsRecordingTime") == null || requestParamMap.get("billsRecordingTime").equals("") ){
			log.info("没有需要添加的车辆油费");
		}else{
			log.info("添加车辆油费");
			//油费
			double fuelBills = Double.parseDouble(requestParamMap.get("fuelBills"));
			//备注
			String billsCreateRemarks = requestParamMap.get("billsCreateRemarks");
			//读数录入时间
			String recordingTime = requestParamMap.get("billsRecordingTime");
//			//判断填写上一次最近日期记录
//			double max = this.cardispatchManageService.getmaxBillsByDateService(recordingTime,carId);
//			if(fuelBills < max){
//				result_map.put("message", "填写的油费小于上一次记录的油费 " + max);
//				result_map.put("flag", false);
//				try {	
//					ActionUtil.responseWrite(result_map);
//					return;
//				} catch (IOException e) {
//					log.error(e.getMessage());
//					e.printStackTrace();
//				}
//			}
			Date recordingDate = null;
			try {
				recordingDate = simpleDateFormat.parse(recordingTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//创建油费pojo
			bills = new CardispatchFuelBills();
			bills.setCarId(carId);
			bills.setCreateRemarks(billsCreateRemarks);
			bills.setCreateTime(now);
			bills.setCreateUserId(org_user_id);
			bills.setFuelBills(fuelBills);
			bills.setRecordingTime(recordingDate);
			bills.setStatus(1);
		}
		boolean flag = this.cardispatchManageService.addCardispatchInstrumentReadingAndFuelBills(reading, bills);
		if(flag == true){
			result_map.put("message", "添加成功");
			result_map.put("flag", true);
		}else{
			result_map.put("message", "添加失败");
			result_map.put("flag", false);
		}
		try {
			ActionUtil.responseWrite(result_map);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载添加仪表读数与油费页面
	* @author ou.jh
	* @date Aug 1, 2013 5:46:22 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public String addReadingAndBillsViewAction(){
		//获取当前登录人的业务单元
		String accountId = (String) SessionService.getInstance().getValueByKey("userId");
		//List<ProviderOrganization> list = providerOrganizationService.getTopLevelOrgByAccount(accountId);
		List<SysOrg> list =  this.sysOrganizationService.getTopLevelOrgByAccount(accountId);
		if(list != null){
			this.orgId = list.get(0).getOrgId();
			this.orgName = list.get(0).getName();
		}
		return "success";
	}
	
	/**
	 * 根据ID获取仪表读数与油费页面
	* @author ou.jh
	* @date Aug 1, 2013 5:46:22 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public String getReadingAndBillsAction(){
		if(distinction != null && distinction.equals("Bills")){
			this.dataMap = this.cardispatchManageService.getCardispatchFuelBillsById(id);
		}else if(distinction != null && distinction.equals("Reading")){
			this.dataMap = this.cardispatchManageService.getCardispatchInstrumentReadingById(id);
		}
		return "success";
	}
	
	
	/**
	 * 加载仪表读数与油费列表页面
	* @author ou.jh
	* @date Aug 1, 2013 5:46:22 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public String carReadingAndBillsPageViewAction(){
		//获取当前登录人的业务单元
		String accountId = (String) SessionService.getInstance().getValueByKey("userId");
		//List<ProviderOrganization> list = providerOrganizationService.getTopLevelOrgByAccount(accountId);
		List<SysOrg> list =  this.sysOrganizationService.getTopLevelOrgByAccount(accountId);
		if(list != null){
			this.orgId = list.get(0).getOrgId();
			this.orgName = list.get(0).getName();
		}
		return "success";
	}
	
	/**
	 * 根据组织ID查找车辆 
	* @author ou.jh
	* @date Aug 2, 2013 10:01:39 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void selectCarByOrgIdAction(){
		List<Map<String,Object>> result_map = this.cardispatchManageService.findCarByOrgIdService(this.orgId);
		try {
			ActionUtil.responseWrite(result_map);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取仪表读数或者油费(分页)
	* @author ou.jh
	* @date Aug 2, 2013 4:33:05 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public String selectReadingOrBillsAction(){
		int count = 0;
		if(distinction != null && distinction.equals("Bills")){
			count = this.cardispatchManageService.getCardispatchFuelBillsCount(startTime,endTime, orgId, carId);
			this.pageList = this.cardispatchManageService.getCardispatchFuelBillsPaging(startTime,endTime, orgId, carId, currentPage, pageSize);
		}else if(distinction != null && distinction.equals("Reading")){
			count = this.cardispatchManageService.getCardispatchInstrumentReadingCount(startTime,endTime, orgId, carId);
			this.pageList = this.cardispatchManageService.getCardispatchInstrumentReadingPaging(startTime,endTime, orgId, carId, currentPage, pageSize);
		}
		if(count > 0){
			PagingHelper ph = new PagingHelper();
			Map<String, Object> phMap = ph.calculatePagingParamService(count,
					currentPage, pageSize);
			this.total = Long.parseLong(phMap.get("totalPage").toString());
		}
		return distinction;
	}
	
	/**
	 * 删除仪表读数或者油费(逻辑删除)
	* @author ou.jh
	* @date Aug 2, 2013 4:33:05 PM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public void deleteReadingOrBillsAction(){
		int count = 0;
		long deleteUserId = Long.parseLong(SessionService.getInstance().getValueByKey(UserInfo.ORG_USER_ID)+"");
		boolean flag = true;
		try {
			if(distinction != null && distinction.equals("Bills")){
				this.cardispatchManageService.deleteCardispatchFuelBills(this.id,deleteUserId);
			}else if(distinction != null && distinction.equals("Reading")){
				this.cardispatchManageService.deleteCardispatchInstrumentReading(this.id,deleteUserId);
			}
		} catch (Exception e) {
			flag = false;
		} finally {
			Map<String, Object> result_map = new HashMap<String, Object>();
			result_map.put("flag", flag);
			try {
				ActionUtil.responseWrite(result_map);
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 导出仪表读数或油费
	* @author ou.jh
	* @date Aug 13, 2013 1:42:23 PM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
	public String exportCarReadingOrBillsAction(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("GBK");	
		excelStream = this.cardispatchManageService.exportCarReadingOrBillsService(distinction, startTime, endTime, orgId, carId);
        String downFileName;
		try {
			downFileName = new String((startTime+"至"+endTime+"里程读数与油费记录").getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			downFileName="carfuelreport";
		}
		this.filename=downFileName+".xls";      
		return "success";
	}
	
	
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	/*********** getter setter ***********/
	public CardispatchManageService getCardispatchManageService() {
		return cardispatchManageService;
	}
	public void setCardispatchManageService(
			CardispatchManageService cardispatchManageService) {
		this.cardispatchManageService = cardispatchManageService;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public File[] getFile() {
		return file;
	}
	public void setFile(File[] file) {
		this.file = file;
	}
	public String getFileNameString() {
		return fileNameString;
	}
	public void setFileNameString(String fileNameString) {
		this.fileNameString = fileNameString;
	}
	public String[] getFileFileName() {
		return fileFileName;
	}
	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}
	public String[] getFileContentType() {
		return fileContentType;
	}
	public void setFileContentType(String[] fileContentType) {
		this.fileContentType = fileContentType;
	}

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}



	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getDistinction() {
		return distinction;
	}

	public void setDistinction(String distinction) {
		this.distinction = distinction;
	}

	public List<Map<String, Object>> getPageList() {
		return pageList;
	}

	public void setPageList(List<Map<String, Object>> pageList) {
		this.pageList = pageList;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	
	
}
