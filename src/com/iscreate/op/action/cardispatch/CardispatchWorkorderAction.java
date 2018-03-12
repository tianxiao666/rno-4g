package com.iscreate.op.action.cardispatch;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.action.informationmanage.common.ArrayUtil;
import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.pojo.cardispatch.CardispatchWorkorder;
import com.iscreate.op.service.cardispatch.CardispatchManageService;
import com.iscreate.op.service.cardispatch.CardispatchWorkorderService;
import com.iscreate.plat.tools.map.MapHelper;
import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings({"unused","deprecation","unchecked"})
/**
 * 
 * cardispatchWorkorder
 * cardispatchWorkorder_ajax
 * 
 */
public class CardispatchWorkorderAction {
	
	
	/************* 依赖注入 *************/
	
	private CardispatchWorkorderService cardispatchWorkorderService;
	private CardispatchManageService cardispatchManageService;
	
	/********* 属性 *********/
	private String url;
	private Map<String,Object> actionMap = new LinkedHashMap<String, Object>();
	private String WOID;
	private String TOID;
	private Log log = LogFactory.getLog(this.getClass());
	
	private int currentPage;
	private int pageSize=10;
	private int totalPage;
	private List<Map<String,Object>> resultList;
	
	/**************** action ******************/
	
	/**
	 * 进入工单页面 - 通过该工单状态跳转到响应页面
	 * (请求参数) 	WOID - 工单id
	 * 					driverCarId - 车辆司机配对id
	 * 					carNumber - 车辆牌照
	 * 					type - 页面类型(view ： 视图)
	 * (返回) (String) 跳转字符 
	 */
	public String enterCardispatchWorkorderAction() {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("driverCarId","carNumber","type");
		String url = cardispatchWorkorderService.enterCardispatchWorkorder(WOID, requestParamMap);
		this.url = url;
		return "redirect";
	}
	
	
	/**
	 * 启动创建车辆调度工单
	 * (请求参数) - applyDescription useCarPersonId planUseCarAddress planUseCarTime planReturnCarTime criticalClass useCarType
	 * (响应) (String) 生成的工单woId
	 */
	public void createCardispatchWorkorderAction() {
		//获取工单信息
		Map<String, String> request_param_map = ActionUtil.getRequestParamMapByChoiceMap("Carworkorder_save#");
		Map<String, String> apply__param_map = ActionUtil.getRequestParamMapByChoiceMap("apply_save#");
		CardispatchWorkorder cardispatchWorkorder = null;
		//判断是否有用车人
		Boolean flag = false;
		if( !request_param_map.containsKey("useCarPersonId") || StringUtil.isNullOrEmpty(request_param_map.get("useCarPersonId")) ) {
			System.err.println("CardispatchWorkorderAction --> createCarDispatchWorkOrderAction the 'useCarPeople' is null !");
			flag = false;
		} else {
			WOID = cardispatchWorkorderService.txCreateCardiaptchWorkorder(request_param_map,apply__param_map);
			if ( !StringUtil.isNullOrEmpty(WOID) ) {
				flag = true;
			}
		}
		try {
			ActionUtil.responseWrite(WOID);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询车辆调度工单
	 * @reqeust_param - woId , state
	 * @description 	woId 为null时,查询所有车辆调度工单 
	 * 				 	state 为 CardispatchConstant常量 , 为null时查询所有状态
	 * (响应) (List<Map<String, String>>) 工单列表
	 */
	public void findCardispatchWordorderByState () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMapByChoiceMap("workorder#");
		ArrayUtil.removeEmpty(requestParamMap);
		if ( requestParamMap.containsKey("woId") ) {
			requestParamMap.put("wo.id", requestParamMap.get("woId"));
		}
		String state = requestParamMap.get("state");
		requestParamMap.remove("state");
		List<Map<String, String>> list = cardispatchWorkorderService.findCardispatchWordorderByState(requestParamMap, state);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 进入派车页面
	 * (请求参数) woId 工单woId
	 * (响应) (Map<String,String>)工单
	 */
	public void findSingleCardispatchWorkorder () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("woId");
		requestParamMap.put("wo.woId", requestParamMap.get("woId"));
		WOID = requestParamMap.remove("woId");
		String workorderType = cardispatchWorkorderService.findCardispatchWorkerorderType(WOID);
		try {
			Map<String, String> workorder = cardispatchWorkorderService.findSingleCardispatchWorkorder(requestParamMap);
			ActionUtil.responseWrite(workorder,false);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 派车
	 * (请求参数) - is_pc woId carDriverPairId dispatchDescription
	 * (响应) (boolean) 派车是否成功 (true：成功)
	 */
	public void sendCar () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap(false,"woId","carDriverPairId","is_pc","planUseCarAddress");
		HttpServletRequest request = ServletActionContext.getRequest();
		String dispatchDescription = request.getParameter("dispatchDescription");
		requestParamMap.put("dispatchDescription", dispatchDescription);
		WOID = requestParamMap.get("woId");
		Boolean is_pc = Boolean.valueOf(requestParamMap.get("is_pc"));
		//派车
		Boolean flag = cardispatchWorkorderService.txSendCar(requestParamMap,actionMap);
		try {
			ActionUtil.responseWrite(flag+"");
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取车辆列表
	 * @page (陈立斌) 车辆状态监控
	 * (请求参数) - isArranged , dutyDate , freId , carNumber
	 * (响应) (List<Map<String, Object>>) 车辆信息集合
	 */
	public void findCarListWithDuty () {
		Map<String, Object> request_param_map = ActionUtil.getRequestParamMapByChoiceMapObject("_");
		ArrayUtil.removeEmpty(request_param_map);
		// carType
		if ( request_param_map.containsKey("terminalState") && request_param_map.get("terminalState") != null ) {
			String terminalState = request_param_map.get("terminalState")+"";
			List<String> terminalState_list = StringUtil.split2List(terminalState,",");
			request_param_map.put("terminalState", terminalState_list);
		}
		if ( request_param_map.containsKey("carType") && request_param_map.get("carType") != null ) {
			String carType = request_param_map.get("carType")+"";
			List<String> carType_list = StringUtil.split2List(carType,",");
			request_param_map.put("carType", carType_list);
		}
		Map<String,Object> duty_param_map = new LinkedHashMap<String, Object>();
		String isArranged = request_param_map.get("isArranged")+"";
		request_param_map.remove("isArranged");
		//拼接duty
		String dutyDate = request_param_map.remove("dutyDate")+"";
		Object freId = request_param_map.remove("freId");
		if ( StringUtil.isNullOrEmpty(dutyDate) ) {
			dutyDate = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		}
		duty_param_map.put("dutyDate", dutyDate);
		String[] freIds = (freId+"").split(",");
		duty_param_map.put("freId", freIds);
		ArrayUtil.removeEmpty(duty_param_map);
		List<Map<String, Object>> list = cardispatchManageService.findCarDriverPairListIsDuty(request_param_map,duty_param_map,isArranged);
		try {
			ActionUtil.responseWrite(list);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取车辆列表
	 * @page (陈立斌) 车辆状态监控
	 * (请求参数) - isArranged , dutyDate , freId , carNumber
	 * (响应) (List<Map<String, Object>>) 车辆信息集合
	 */
	public void findCarListWithDutyByGis () {
		Map<String, Object> request_param_map = ActionUtil.getRequestParamMapByChoiceMapObject("_");
		ArrayUtil.removeEmpty(request_param_map);
		// carType
		if ( request_param_map.containsKey("terminalState") && request_param_map.get("terminalState") != null ) {
			String terminalState = request_param_map.get("terminalState")+"";
			List<String> terminalState_list = StringUtil.split2List(terminalState,",");
			request_param_map.put("terminalState", terminalState_list);
		}
		if ( request_param_map.containsKey("carType") && request_param_map.get("carType") != null ) {
			String carType = request_param_map.get("carType")+"";
			List<String> carType_list = StringUtil.split2List(carType,",");
			request_param_map.put("carType", carType_list);
		}
		Map<String,Object> duty_param_map = new LinkedHashMap<String, Object>();
		String isArranged = request_param_map.get("isArranged")+"";
		request_param_map.remove("isArranged");
		//拼接duty
		String dutyDate = request_param_map.remove("dutyDate")+"";
		Object freId = request_param_map.remove("freId");
		if ( StringUtil.isNullOrEmpty(dutyDate) ) {
			dutyDate = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		}
		duty_param_map.put("dutyDate", dutyDate);
		String[] freIds = (freId+"").split(",");
		duty_param_map.put("freId", freIds);
		ArrayUtil.removeEmpty(duty_param_map);
		List<Map<String, Object>> list = cardispatchManageService.findCarDriverPairListIsDutyByGis(request_param_map,duty_param_map,isArranged);
		try {
			ActionUtil.responseWrite(list);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * 查询车辆当天当前里程数
	 * @author LI.HB
	 * @create_time 2013-08-07
	 * 
	 */
	public void findCurrentDayMileage()
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String carId = (String)request.getParameter("carId");
		String sTime = (String)request.getParameter("sTime");
		String eTime = (String)request.getParameter("eTime");
		
		Map<String, Double> list = cardispatchManageService.findCurrentDayMileage(carId,sTime,eTime);
		
		try {
			ActionUtil.responseWrite(list);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	
	/**
	 * 根据车牌，查询车辆集合
	 * @page 派车模态窗口、车辆状态监控
	 * (请求参数) carNumber 、 isArranged(是否排班)
	 * (响应) (List<Map<String, Object>>) 车辆信息集合
	 */
	public void findCarListWithDutyByCarNumber () {
		Map<String, String> request_param_map = ActionUtil.getRequestParamMap("carNumber","isArranged");
		Map<String,Object> duty_param_map = new LinkedHashMap<String, Object>();
		String isArranged = request_param_map.get("isArranged")+"";
		request_param_map.remove("isArranged");
		//拼接duty
		List<Map<String, Object>> list = cardispatchManageService.findCarDriverPairListIsDuty(request_param_map,duty_param_map,isArranged);
		try {
			ActionUtil.responseWrite(list);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取车辆集合
	 * @page 派车模态窗口
	 * (请求参数) - isArranged , dutyDate , freId , carNumber
	 * (响应) (List<Map<String, Object>>) 车辆信息集合
	 */
	public void findCarListWithDutyWorkorder () {
		Map<String, Object> request_param_map = ActionUtil.getRequestParamMapByChoiceMapObject("car#");
		Map<String,Object> duty_param_map = new LinkedHashMap<String, Object>();
		String isArranged = request_param_map.get("isArranged")+"";
		request_param_map.remove("isArranged");
		//拼接duty
		String dutyDate = request_param_map.remove("dutyDate")+"";
		Object freId = request_param_map.remove("freId");
		if ( StringUtil.isNullOrEmpty(dutyDate) ) {
			dutyDate = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		}
		duty_param_map.put("dutyDate", dutyDate);
		duty_param_map.put("freId", freId);
		List<Map<String, Object>> list = cardispatchManageService.findCarDriverPairListIsDuty(request_param_map,duty_param_map,isArranged);
		try {
			ActionUtil.responseWrite(list);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 用车
	 * (请求参数) woId , realUseCarMileage 
	 * (返回) (String)跳转字符
	 */
	public String useCar () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("woId","realUseCarMileage");
		this.WOID = requestParamMap.get("woId");
		Boolean flag = cardispatchWorkorderService.txUseCar(requestParamMap, actionMap);
		if ( flag ) {
			//用车成功
			this.url = "cardispatchWorkorder!enterCardispatchWorkorderAction.action";
			return "redirectAction";
		}
		return "success";
	}
	
	/**
	 * 保存用车费用
	 * (请求参数) woId , feeType , feeAmount , description
	 * (响应) (boolean)保存是否成功 (true：成功)
	 */
	public void saveFeeAmount () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("woId","feeType","feeAmount","description");
		boolean saveFeeAmount = cardispatchWorkorderService.saveFeeAmount(requestParamMap);
		try {
			ActionUtil.responseWrite(saveFeeAmount);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除用车费用
	 * (请求参数) id (费用id)
	 * (响应) 删除是否成功
	 */
	public void deleteFeeAmount () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("id");
		boolean flag = cardispatchWorkorderService.deleteFeeAmount(requestParamMap);
		try {
			ActionUtil.responseWrite(flag);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据工单woId获取，用车费用
	 * @request_paran woId
	 * (响应) (List<Map<String, String>>) 用车费用集合
	 */
	public void findFeerecordListByWoId() {
		String woId = ActionUtil.getParamString("woId");
		List<Map<String, String>> list = cardispatchWorkorderService.findFeerecordListByWoId(woId);
		try {
			ActionUtil.responseWrite(list,false);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据费用产生时间段，获取响应工单的用车费用信息
	 * (请求参数) (workorder# : 工单信息参数) ， startTime ， endTime
	 * (响应) (List<Map<String, String>>) 用车费用信息集合
	 */
	public void findFeerecordList() {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMapByChoiceMap("workorder#");
		String startTime = ActionUtil.getParamString("startTime");
		String endTime = ActionUtil.getParamString("endTime");
		ArrayUtil.removeEmpty(requestParamMap);
		List<Map<String, String>> list = cardispatchWorkorderService.findFeerecordList( requestParamMap , startTime , endTime );
		try {
			ActionUtil.responseWrite(list,false);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 还车
	 * (请求参数) - woId realReturnCarMileage
	 */
	public String returnCar() {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("woId","realReturnCarMileage");
		this.WOID = requestParamMap.get("woId");
		boolean flag = cardispatchWorkorderService.txReturnCar(requestParamMap, actionMap);
		if ( flag ) {
			//还车成功
			this.url = "cardispatchWorkorder!enterCardispatchWorkorderAction.action";
			return "redirectAction";
		}
		return "success";
	}
	
	
	/**
	 * 根据任务单toId,获取相应的车辆调度单信息集合
	 * (请求参数) TOID , WOID , workType 
	 * (响应) (List<Map<String,String>>) 车辆调度单信息集合
	 */
	public void findApplyWorkorderByToId () {
		String workType = ActionUtil.getParamString("workType");
		List<Map<String, Object>> list = cardispatchWorkorderService.findApplyWorkorderByToId(WOID, TOID, workType);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	/***
	 * 
	 * @description: 根据经纬度获取实际地址
	 * @author：yuan.yw     
	 * @return void     
	 * @date：Jul 22, 2013 4:40:50 PM
	 */
	public void getAddressByLngLatAction(){
		String longitude =  ActionUtil.getParamString("longitude");
		String latitude = ActionUtil.getParamString("latitude");
		String carId = ActionUtil.getParamString("carId");
		String address = "";
		if(longitude!=null && !"".equals(longitude)&&latitude!=null && !"".equals(latitude)){
			address = MapHelper.convertLatlngToAddress(longitude,latitude);
		}else{
			Map<String,String> map = this.cardispatchManageService.getCarInfoByCarId(carId);
			if(map!=null && !map.isEmpty()){
				longitude =  map.get("longitude");
				latitude = map.get("latitude");
				if(longitude!=null && !"".equals(longitude)&&latitude!=null && !"".equals(latitude)){
					address = MapHelper.convertLatlngToAddress(longitude,latitude);
				}
			}
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("address", address);
		map.put("carId", carId);
		try {
			ActionUtil.responseWrite(map);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @description: 获取车辆列表 车辆状态监控
	 * @author：yuan.yw
	 * @return     
	 * @return String     
	 * @date：Jul 23, 2013 10:27:40 AM
	 */
	public String findCarListForMonitor(){
		Map<String, Object> request_param_map = ActionUtil.getRequestParamMapByChoiceMapObject("_");
		ArrayUtil.removeEmpty(request_param_map);
		// carType
		if ( request_param_map.containsKey("terminalState") && request_param_map.get("terminalState") != null ) {
			String terminalState = request_param_map.get("terminalState")+"";
			List<String> terminalState_list = StringUtil.split2List(terminalState,",");
			request_param_map.put("terminalState", terminalState_list);
		}
		if ( request_param_map.containsKey("carType") && request_param_map.get("carType") != null ) {
			String carType = request_param_map.get("carType")+"";
			List<String> carType_list = StringUtil.split2List(carType,",");
			request_param_map.put("carType", carType_list);
		}
		Map<String,Object> duty_param_map = new LinkedHashMap<String, Object>();
		String isArranged = request_param_map.get("isArranged")+"";
		request_param_map.remove("isArranged");
		//拼接duty
		String dutyDate = request_param_map.remove("dutyDate")+"";
		Object freId = request_param_map.remove("freId");
		if ( StringUtil.isNullOrEmpty(dutyDate) ) {
			dutyDate = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
		}
		duty_param_map.put("dutyDate", dutyDate);
		String[] freIds = (freId+"").split(",");
		duty_param_map.put("freId", freIds);
		ArrayUtil.removeEmpty(duty_param_map);
		if(this.currentPage>0 &&this.currentPage>this.totalPage){
			this.currentPage = this.totalPage;
		}else if(this.currentPage<0){
			this.currentPage = 0;
		}
		
		List<Map<String, Object>> list = cardispatchManageService.findCarDriverPairListForMonitor(request_param_map, duty_param_map, isArranged, this.currentPage, this.pageSize);
		if(list!=null && !list.isEmpty()){
			Map<String,Object> map =list.get(0);
			List<Map<String,Object>> reList = (List<Map<String,Object>>)map.get("entityList");
			if(reList!=null && !reList.isEmpty()){
				this.resultList = reList;
			}
			int count = Integer.parseInt(map.get("count")+"");
			if(count%this.pageSize==0){
				this.totalPage =count/this.pageSize;
			}else{
				this.totalPage =count/this.pageSize+1;
			}
		}
		this.url = "carListForMonitoring.jsp";
		return "success";
	}
	
	/*********** getter setter ***********/
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map<String, Object> getActionMap() {
		return actionMap;
	}
	public void setActionMap(Map<String, Object> actionMap) {
		this.actionMap = actionMap;
	}
	public CardispatchWorkorderService getCardispatchWorkorderService() {
		return cardispatchWorkorderService;
	}
	public void setCardispatchWorkorderService(
			CardispatchWorkorderService cardispatchWorkorderService) {
		this.cardispatchWorkorderService = cardispatchWorkorderService;
	}
	public CardispatchManageService getCardispatchManageService() {
		return cardispatchManageService;
	}
	public void setCardispatchManageService(
			CardispatchManageService cardispatchManageService) {
		this.cardispatchManageService = cardispatchManageService;
	}
	public String getWOID() {
		return WOID;
	}
	public void setWOID(String woid) {
		WOID = woid;
	}
	public String getTOID() {
		return TOID;
	}
	public void setTOID(String tOID) {
		TOID = tOID;
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


	public int getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}


	public List<Map<String, Object>> getResultList() {
		return resultList;
	}


	public void setResultList(List<Map<String, Object>> resultList) {
		this.resultList = resultList;
	}

	

	
	
	
	
	
}
