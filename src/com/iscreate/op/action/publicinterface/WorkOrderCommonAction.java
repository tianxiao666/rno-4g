package com.iscreate.op.action.publicinterface;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.action.urgentrepair.UrgentRepairWorkOrderAction;
import com.iscreate.op.constant.TreeConstant;
import com.iscreate.op.service.publicinterface.TaskOrderCommonService;
import com.iscreate.op.service.publicinterface.WorkOrderCommonService;
import com.iscreate.op.service.urgentrepair.BizResourceReader;
import com.iscreate.plat.datadictionary.DataDictionaryService;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageCommunicationHelper;
import com.iscreate.plat.tree.TreeNode;

public class WorkOrderCommonAction {
	
	/**
	 * 工单信息公共参数
	 * */
	private String WOID;
	private String TOID;
	
	/**
	 * 工单信息返回变量
	 * */
	private String workOrderInfo;
	private String customerWorkOrderInfo;
	private Map<String,String> customerWorkOrderMapInfo;
	private String taskOrderInfo;
	private WorkOrderCommonService workOrderCommonService;
	private TaskOrderCommonService taskOrderCommonService;
	private DataDictionaryService dataDictionaryService;
	private String referenceValue;
	private String id;
	private String value1;
	private String value2;
	
	private  static final  Log log = LogFactory.getLog(WorkOrderCommonAction.class);
	
	/**
	 * 获取抢修工单涉及的所有信息
	 * */
	public String getAllUrgentRepairWorkOrderInfoAction()
	{
		workOrderCommonService.getAllUrgentRepairWorkOrderInfoService();
		return "success";
	}
	
	/**
	 * 工单信息
	 * @return
	 */
	public String getUrgentRepairWorkOrderInfoAction(){
		log.info("进入 getUrgentRepairWorkOrderInfoAction");
		log.info("getUrgentRepairWorkOrderInfoAction 为获取工单信息，作展示用。");
		workOrderInfo = "";
		if(WOID!=null&&!"".equals(WOID)){
			log.info("参数：WOID="+WOID);
			Map workOrderInfoMap = new HashMap();
			workOrderInfoMap = workOrderCommonService.getUrgentRepairWorkOrderService(WOID);
			Gson gson = new Gson();
			workOrderInfo = gson.toJson(workOrderInfoMap);
			workOrderInfo = workOrderInfo.replace("\"", "\'");
		}else{
			log.info("参数：WOID为空");
		}
		log.info("退出 getUrgentRepairWorkOrderInfoAction");
		return "success";
	}
	
	/**
	 * 客户工单信息
	 * @return
	 */
	public String getUrgentRepairCustomerWorkOrderInfoAction(){
		log.info("进入 getUrgentRepairCustomerWorkOrderInfoAction");
		log.info("getUrgentRepairCustomerWorkOrderInfoAction 为获取客户工单信息，作展示用。");
		customerWorkOrderInfo = "";
		if(WOID!=null&&!"".equals(WOID)){
			log.info("参数：WOID="+WOID);
			Map customerWorkOrderInfoMap = new HashMap();
			customerWorkOrderInfoMap = workOrderCommonService.getUrgentRepairCustomerWorkOrderService(WOID);
			Gson gson = new Gson();
			customerWorkOrderInfo = gson.toJson(customerWorkOrderInfoMap);
			customerWorkOrderInfo = customerWorkOrderInfo.replace("\"", "\'");
		}else{
			log.info("参数：WOID为空");
		}
		log.info("退出 getUrgentRepairCustomerWorkOrderInfoAction");
		return "success";
	}
	
	/**
	 * 网优之家2g工单信息
	 * @return
	 */
	public String getUrgentRepairDevice2gWorkOrderInfoAction(){
		log.info("进入 getUrgentRepairDevice2gWorkOrderInfoAction");
		log.info("getUrgentRepairDevice2gWorkOrderInfoAction 为获取网优之家2g工单信息(客户工单信息)，作展示用。");
		customerWorkOrderMapInfo = new HashMap();
		if(WOID!=null&&!"".equals(WOID)){
			log.info("参数：WOID="+WOID);
			customerWorkOrderMapInfo = workOrderCommonService.getUrgentRepairHome2GWorkOrderService(WOID);
		}else{
			log.info("参数：WOID为空");
		}
		log.info("退出 getUrgentRepairDevice2gWorkOrderInfoAction");
		return "success";
	}
	
	/**
	 * 网优之家td工单信息
	 * @return
	 */
	public String getUrgentRepairDevicetdWorkOrderInfoAction(){
		log.info("进入 getUrgentRepairDevicetdWorkOrderInfoAction");
		log.info("getUrgentRepairDevicetdWorkOrderInfoAction 为获取网优之家td工单信息(客户工单信息)，作展示用。");
		customerWorkOrderMapInfo = new HashMap();
		if(WOID!=null&&!"".equals(WOID)){
			log.info("参数：WOID="+WOID);
			customerWorkOrderMapInfo = workOrderCommonService.getUrgentRepairHomeTdWorkOrderService(WOID);
		}else{
			log.info("参数：WOID为空");
		}
		log.info("退出 getUrgentRepairDevicetdWorkOrderInfoAction");
		return "success";
	}
	
	/**
	 * 任务单信息
	 * @return
	 */
	public String getUrgentRepairTaskOrderInfoAction(){
		log.info("进入 getUrgentRepairTaskOrderInfoAction");
		log.info("getUrgentRepairTaskOrderInfoAction 为获取任务单信息，作展示用。");
		if(TOID!=null&&!"".equals(TOID)){
			log.info("参数：TOID="+TOID);
			Map taskOrderInfoMap = new HashMap();
			taskOrderInfoMap = workOrderCommonService.getUrgentRepairTaskOrderService(TOID);
			Gson gson = new Gson();
			taskOrderInfo = gson.toJson(taskOrderInfoMap);
			taskOrderInfo = taskOrderInfo.replace("\"", "\'");
		}else{
			log.info("参数：TOID为空");
		}
		
		log.info("退出 getUrgentRepairTaskOrderInfoAction");
		return "success";
	}
	
	/**
	 * 生成工单流转过程树
	 */
	public void createWorkOrderProcedureTreeAction(){
		log.info("进入 createWorkOrderProcedureTreeAction");
		log.info("createWorkOrderProcedureTreeAction 生成工单流转过程树。");
		List<Map> mapList = new ArrayList<Map>();
		if(WOID!=null&&!"".equals(WOID)){
			log.info("参数：WOID="+WOID);
			mapList = workOrderCommonService.getUrgentRepairWorkOrderProcessInfoService(WOID);
		}else{
			log.info("参数：WOID为空");
		}
		
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(mapList);		
		try {
			log.info("退出 getUrgentRepairTaskOrderInfoAction");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getUrgentRepairTaskOrderInfoAction 失败");
		}
	}
	
	/**
	 * 检查工单的下级任务是否全部结束
	 * @return
	 */
	public void checkAllSubTaskIsFinishedForWorkOrderAction(){
		log.info("进入 checkAllSubTaskIsFinishedForWorkOrderAction");
		log.info("checkAllSubTaskIsFinishedForWorkOrderAction 检查工单的下级任务是否全部结束。");
		String result = "";
		if(WOID!=null&&!"".equals(WOID)){
			log.info("参数：WOID="+WOID);
			boolean flag = workOrderCommonService.hasAllSubTasksFinishedByWoId(WOID);
			if(flag){
				log.info("WOID="+WOID+"的工单的下级任务全部结束");
				result = "success";
			}else{
				log.info("WOID="+WOID+"的工单的下级任务还没全部结束");
				result = "error";
			}
		}else{
			log.info("参数：WOID为空");
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			log.info("退出 checkAllSubTaskIsFinishedForWorkOrderAction");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 checkAllSubTaskIsFinishedForWorkOrderAction 失败");
		}
	}
	
	
	/**
	 * 检查现场任务单的下级任务是否全部结束
	 * @return
	 */
	public void checkAllSubTaskIsFinishedForTaskOrderAction(){
		log.info("进入 checkAllSubTaskIsFinishedForTaskOrderAction");
		log.info("checkAllSubTaskIsFinishedForTaskOrderAction 检查工单的下级任务是否全部结束。");
		String result = "";
		if(TOID!=null&&!"".equals(TOID)){
			log.info("参数：TOID="+TOID);
			boolean flag = taskOrderCommonService.hasAllSubTasksFinishedByToId(TOID);
			if(flag){
				log.info("TOID="+TOID+"的任务单的下级任务全部结束");
				result = "success";
			}else{
				log.info("TOID="+TOID+"的任务单的下级任务还没全部结束");
				result = "error";
			}
		}else{
			log.info("参数：TOID为空");
		}
		
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			log.info("退出 checkAllSubTaskIsFinishedForTaskOrderAction");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 checkAllSubTaskIsFinishedForTaskOrderAction 失败");
		}
	}
	
	/**
	 * 获取数据字典-故障大类（网优之家）
	 */
	public void getFaultGeneralDictionarySHENZHEAction(){
		log.info("进入 getFaultGeneralDictionarySHENZHEAction");
		log.info("getFaultGeneralDictionarySHENZHEAction  获取数据字典-故障大类（网优之家）。");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			value1 = URLDecoder.decode(value1,"UTF-8");
			value2 = URLDecoder.decode(value2,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			log.error("参数转码失败");
			log.error("执行 getFaultGeneralDictionarySHENZHEAction 失败");
			return;
		}
		if(value1==null||"".equals(value1)){
			log.debug("参数客户工单类型为空");
			try {
				response.getWriter().write("");
			} catch (Exception e) {
				log.error("执行 getFaultGeneralDictionarySHENZHEAction 失败");
				return;
			}
		}
		if(value2==null||"".equals(value2)){
			log.debug("参数受理专业为空");
			try {
				response.getWriter().write("");
			} catch (Exception e) {
				log.error("执行 getFaultGeneralDictionarySHENZHEAction 失败");
				return;
			}
		}
		
		List<Map> strList = new  ArrayList<Map>();
		//获取受理专业
		List<TreeNode> treeNodes1 = dataDictionaryService.getNextDictionaryByReferenceValueAndTreeIdService(value1, TreeConstant.SHENZHEN_ACCEPTPROFESSIONAL_FAULTGENERA_FAULTCAUSE);
		List<TreeNode> treeNodes2 = new ArrayList<TreeNode>();
		if(treeNodes1!=null&&!treeNodes1.isEmpty()){
			for(TreeNode treeNode : treeNodes1){
				//获取故障大类
				long subId1 = treeNode.getId();
				if(value2.equals( treeNode.getReferenceValue())){
					treeNodes2.addAll(dataDictionaryService.getNextDictionaryByTreeNodeIdService(subId1)) ;
				}
				
			}
			
			if(treeNodes2!=null&&!treeNodes2.isEmpty()){
				for(TreeNode treeNode :treeNodes2){
					Map map = new HashMap();
					map.put("id", treeNode.getId());
					map.put("value", treeNode.getReferenceValue());
					strList.add(map);
				}
				
			}else{
				log.debug("网优之家客户工单类型为"+value1+"，受理专业为"+value2+"的数据字典为空。");
			}
			
		}else{
			log.debug("网优之家客户工单类型为"+value1+"的数据字典为空。");
		}
		
		
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(strList);		
		try {
			log.info("退出 getFaultGeneralDictionarySHENZHEAction");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getFaultGeneralDictionarySHENZHEAction 失败");
		}
	}
	
	/**
	 * 获取数据字典-下一级(网优之家)
	 */
	public void getNextTreeNodeDictionarySHENZHENAction(){
		log.info("进入 getNextTreeNodeDictionarySHENZHENAction");
		log.info("getNextTreeNodeDictionarySHENZHENAction  取数据字典-下一级(网优之家)。");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			id = URLDecoder.decode(id,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			log.error("参数树节点Id转码失败");
			log.error("执行 getNextTreeNodeDictionarySHENZHENAction 失败");
		}
		if(id==null||"".equals(id)){
			log.debug("参数树节点Id为空");
			try {
				response.getWriter().write("");
			} catch (Exception e) {
				log.error("执行 getNextTreeNodeDictionarySHENZHENAction 失败");
				return;
			}
		}
		
		List<Map> strList = new  ArrayList<Map>();
		List<TreeNode> treeNodes = dataDictionaryService.getNextDictionaryByTreeNodeIdService(Long.parseLong(id));
		if(treeNodes!=null&&!treeNodes.isEmpty()){
			for(TreeNode treeNode : treeNodes){
				Map strMap = new HashMap();
				strMap.put("id", treeNode.getId());
				strMap.put("value", treeNode.getReferenceValue());
				strList.add(strMap);
			}
		}else{
			log.debug("Id为"+id+"的树节点的下级树为空");
		}
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(strList);		
		try {
			log.info("退出 getNextTreeNodeDictionarySHENZHENAction");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getNextTreeNodeDictionarySHENZHENAction 失败");
		}
	}
	
	/**
	 * 获取数据字典-故障大类
	 */
	public void getFaultGeneraDictionaryAction(){
		log.info("进入 getFaultGeneraDictionaryAction");
		log.info("getFaultGeneraDictionaryAction  获取数据字典-故障大类。");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		List<Map> strList = new  ArrayList<Map>();
		if(value1!=null&&!"".equals(value1)){
			String treeId = BizResourceReader.getCityToTreeNameMappingInfo("faultGenera.mapping",value1);
			List<TreeNode> treeNodes = dataDictionaryService.getTheTopDictionaryByTreeIdService(Long.parseLong(treeId));
			if(treeNodes!=null&&!treeNodes.isEmpty()){
				for(TreeNode treeNode : treeNodes){
					Map strMap = new HashMap();
					strMap.put("id", treeNode.getId());
					strMap.put("value", treeNode.getReferenceValue());
					strList.add(strMap);
				}
			}else{
				log.debug("参数 故障大类为"+value1+"的数据字典树为空");
			}
		}else{
			log.debug("参数 故障大类 为空");
		}
		
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(strList);		
		try {
			log.info("退出 getFaultGeneraDictionaryAction");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getFaultGeneraDictionaryAction 失败");
		}
	}
	
	/**
	 * 根据故障大类获取数据字典-故障原因
	 */
	public void getFaultReasonDictionaryByFaultGeneraAction(){
		log.info("进入 getFaultReasonDictionaryByFaultGeneraAction");
		log.info("getFaultReasonDictionaryByFaultGeneraAction  根据故障大类获取数据字典-故障原因。");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		try {
			id = URLDecoder.decode(id,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			log.error("参数 树节点Id转码失败");
			log.error("执行 getFaultReasonDictionaryByFaultGeneraAction 失败");
			return;
		}
		if(id==null||"".equals(id)){
			log.debug("参数 树节点Id为空");
			try {
				response.getWriter().write("");
			} catch (Exception e) {
				log.error("执行 getFaultReasonDictionaryByFaultGeneraAction 失败");
				return;
			}
		}
		
		List<Map> strList = new  ArrayList<Map>();
		List<TreeNode> treeNodes = dataDictionaryService.getNextDictionaryByTreeNodeIdService(Long.parseLong(id));
		if(treeNodes!=null&&!treeNodes.isEmpty()){
			for(TreeNode treeNode : treeNodes){
				Map strMap = new HashMap();
				strMap.put("id", treeNode.getId());
				strMap.put("value", treeNode.getReferenceValue());
				strList.add(strMap);
			}
		}else{
			log.debug("Id为"+id+"的树节点的下级树（故障原因）为空");
		}
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(strList);		
		try {
			log.info("退出 getFaultReasonDictionaryByFaultGeneraAction");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getFaultReasonDictionaryByFaultGeneraAction 失败");
		}
	}
	
	/**
	 * 根据故障大类获取数据字典-故障原因(终端)
	 */
	public void getFaultReasonDictionaryByFaultGeneraActionForMobile(){
		log.info("进入 getFaultReasonDictionaryByFaultGeneraActionForMobile");
		log.info("getFaultReasonDictionaryByFaultGeneraActionForMobile  根据故障大类获取数据字典-故障原因(终端)。");
		try{
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);

			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			
			String id = formJsonMap.get("id");
			
			List<Map> strList = new  ArrayList<Map>();
			List<TreeNode> treeNodes = dataDictionaryService.getNextDictionaryByTreeNodeIdService(Long.parseLong(id));
			if(treeNodes!=null&&!treeNodes.isEmpty()){
				for(TreeNode treeNode : treeNodes){
					Map strMap = new HashMap();
					strMap.put("id", treeNode.getId());
					strMap.put("value", treeNode.getReferenceValue());
					strList.add(strMap);
				}
			}else{
				log.debug("Id为"+id+"的树节点的下级树（故障原因）为空");
			}
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String json = gson.toJson(strList);
			formJsonMap.put("selectFaultReason", json);
			MobilePackage newMobilePackage = new MobilePackage();
			mch.addGroup("header", formJsonMap);
			newMobilePackage.setContent(mch.mapToJson());
			MobilePackageCommunicationHelper.responseMobileSuccess(newMobilePackage);
			log.info("退出 getFaultReasonDictionaryByFaultGeneraActionForMobile");
		}catch (RuntimeException e){
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
		
	}
	
	/**
	 * 获取数据字典-基站等级
	 */
	public void getBaseStationLevelDictionaryAction(){
		log.info("进入 getBaseStationLevelDictionaryAction");
		log.info("getBaseStationLevelDictionaryAction  获取数据字典-基站等级。");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		List<String> strList = new  ArrayList<String>();
		List<TreeNode> treeNodes = dataDictionaryService.getTheTopDictionaryByTreeIdService(TreeConstant.BASESTATIONTYPE);
		if(treeNodes!=null&&!treeNodes.isEmpty()){
			for(TreeNode treeNode : treeNodes){
				strList.add(treeNode.getTreeNodeName());
			}
		}else{
			log.debug("基站等级的数据字典获取失败");
		}
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(strList);		
		try {
			log.info("退出 getBaseStationLevelDictionaryAction");	
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getBaseStationLevelDictionaryAction 失败");
		}
	}
	
	/**
	 * 获取数据字典-故障类型
	 */
	public void getFaultTypeDictionaryAction(){
		log.info("进入 getFaultTypeDictionaryAction");
		log.info("getFaultTypeDictionaryAction  获取数据字典-故障类型。");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		List<String> strList = new  ArrayList<String>();
		String treeId = BizResourceReader.getCityToTreeNameMappingInfo("faultType.mapping",value1);
		List<TreeNode> treeNodes = dataDictionaryService.getTheTopDictionaryByTreeIdService(Long.parseLong(treeId));
		if(treeNodes!=null&&!treeNodes.isEmpty()){
			for(TreeNode treeNode : treeNodes){
				strList.add(treeNode.getTreeNodeName());
			}
		}else{
			log.debug("故障类型的数据字典获取失败");
		}
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(strList);		
		try {
			log.info("退出 getFaultTypeDictionaryAction");		
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getFaultTypeDictionaryAction 失败");
		}
	}
	
	/**
	 * 获取数据字典-故障等级
	 */
	public void getFaultLevelDictionaryAction(){
		log.info("进入 getFaultLevelDictionaryAction");
		log.info("getFaultLevelDictionaryAction  获取数据字典-故障等级。");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		List<String> strList = new  ArrayList<String>();
		List<TreeNode> treeNodes = dataDictionaryService.getTheTopDictionaryByTreeIdService(TreeConstant.FAULTLEVEL);
		if(treeNodes!=null&&!treeNodes.isEmpty()){
			for(TreeNode treeNode : treeNodes){
				strList.add(treeNode.getTreeNodeName());
			}
		}else{
			log.debug("故障等级的数据字典获取失败");
		}
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(strList);		
		try {
			log.info("退出 getFaultLevelDictionaryAction");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getFaultLevelDictionaryAction 失败");
		}
	}
	
	/**
	 * 获取数据字典-受理专业
	 */
	public void getAcceptProfessinalDictionaryAction(){
		log.info("进入 getAcceptProfessinalDictionaryAction");
		log.info("getAcceptProfessinalDictionaryAction  获取数据字典-受理专业。");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		List<String> strList = new  ArrayList<String>();
		String treeId = BizResourceReader.getCityToTreeNameMappingInfo("acceptProfessional.mapping",value1);
		List<TreeNode> treeNodes = dataDictionaryService.getTheTopDictionaryByTreeIdService(Long.parseLong(treeId));
		if(treeNodes!=null&&!treeNodes.isEmpty()){
			for(TreeNode treeNode : treeNodes){
				strList.add(treeNode.getTreeNodeName());
			}
		}else{
			log.debug("受理专业的数据字典获取失败");
		}
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(strList);		
		try {
			log.info("退出 getAcceptProfessinalDictionaryAction");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getAcceptProfessinalDictionaryAction 失败");
		}
	}
	
	/**
	 * 获取数据字典-告警网管来源
	 */
	public void getAlarmNetManageSourceDictionaryAction(){
		log.info("进入 getAlarmNetManageSourceDictionaryAction");
		log.info("getAlarmNetManageSourceDictionaryAction  获取数据字典-告警网管来源。");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		List<String> strList = new  ArrayList<String>();
		List<TreeNode> treeNodes = dataDictionaryService.getTheTopDictionaryByTreeIdService(TreeConstant.ALARMNETMANAGESOURCE);
		if(treeNodes!=null&&!treeNodes.isEmpty()){
			for(TreeNode treeNode : treeNodes){
				strList.add(treeNode.getTreeNodeName());
			}
		}else{
			log.debug("告警网管来源的数据字典获取失败");
		}
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(strList);		
		try {
			log.info("退出 getAlarmNetManageSourceDictionaryAction");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getAlarmNetManageSourceDictionaryAction 失败");
		}
	}
	
	/**
	 * 获取数据字典-告警逻辑分类
	 */
	public void getAlarmLogicalClassDictionaryAction(){
		log.info("进入 getAlarmLogicalClassDictionaryAction");
		log.info("getAlarmLogicalClassDictionaryAction  获取数据字典-告警逻辑分类。");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		List<String> strList = new  ArrayList<String>();
		List<TreeNode> treeNodes = dataDictionaryService.getTheTopDictionaryByTreeIdService(TreeConstant.ALARMLOGICALCLASS_ALARMLOGICALSUBCLASS);
		if(treeNodes!=null&&!treeNodes.isEmpty()){
			for(TreeNode treeNode : treeNodes){
				strList.add(treeNode.getTreeNodeName());
			}
		}else{
			log.debug("告警逻辑分类的数据字典获取失败");
		}
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(strList);		
		try {
			log.info("退出 getAlarmLogicalClassDictionaryAction");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getAlarmLogicalClassDictionaryAction 失败");
		}
	}
	
	/**
	 * 根据告警逻辑分类获取数据字典-告警逻辑子类
	 */
	public void getAlarmLogicalSubClassDictionaryByAlarmLogicalClassAction(){
		log.info("进入 getAlarmLogicalSubClassDictionaryByAlarmLogicalClassAction");
		log.info("getAlarmLogicalSubClassDictionaryByAlarmLogicalClassAction  获取数据字典-告警逻辑子类。");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		try {
			referenceValue = URLDecoder.decode(referenceValue,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			log.error("参数 告警逻辑分类 转码失败");
			log.error("执行 getAlarmLogicalSubClassDictionaryByAlarmLogicalClassAction 失败");
			return;
		}
		
		if(referenceValue==null||"".equals(referenceValue)){
			log.debug("参数 告警逻辑分类 为空");
			try {
				response.getWriter().write("");
			} catch (Exception e) {
				log.error("执行 getAlarmLogicalSubClassDictionaryByAlarmLogicalClassAction 失败");
				return;
			}
		}
		
		List<String> strList = new  ArrayList<String>();
		List<TreeNode> treeNodes = dataDictionaryService.getNextDictionaryByReferenceValueAndTreeIdService(referenceValue, TreeConstant.ALARMLOGICALCLASS_ALARMLOGICALSUBCLASS);
		if(treeNodes!=null&&!treeNodes.isEmpty()){
			for(TreeNode treeNode : treeNodes){
				strList.add(treeNode.getReferenceValue());
			}
		}else{
			log.debug("告警逻辑分类为"+referenceValue+"的告警逻辑子类的数据字典获取失败");
		}
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(strList);		
		try {
			log.info("退出 getAlarmLogicalSubClassDictionaryByAlarmLogicalClassAction");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 getAlarmLogicalSubClassDictionaryByAlarmLogicalClassAction 失败");
		}
	}
	

	public String getWOID() {
		return WOID;
	}

	public void setWOID(String woid) {
		WOID = woid;
	}

	public WorkOrderCommonService getWorkOrderCommonService() {
		return workOrderCommonService;
	}

	public void setWorkOrderCommonService(
			WorkOrderCommonService workOrderCommonService) {
		this.workOrderCommonService = workOrderCommonService;
	}

	public String getTOID() {
		return TOID;
	}

	public void setTOID(String toid) {
		TOID = toid;
	}

	public TaskOrderCommonService getTaskOrderCommonService() {
		return taskOrderCommonService;
	}

	public void setTaskOrderCommonService(
			TaskOrderCommonService taskOrderCommonService) {
		this.taskOrderCommonService = taskOrderCommonService;
	}

	public String getWorkOrderInfo() {
		return workOrderInfo;
	}

	public void setWorkOrderInfo(String workOrderInfo) {
		this.workOrderInfo = workOrderInfo;
	}

	public String getCustomerWorkOrderInfo() {
		return customerWorkOrderInfo;
	}

	public void setCustomerWorkOrderInfo(String customerWorkOrderInfo) {
		this.customerWorkOrderInfo = customerWorkOrderInfo;
	}

	public String getTaskOrderInfo() {
		return taskOrderInfo;
	}

	public void setTaskOrderInfo(String taskOrderInfo) {
		this.taskOrderInfo = taskOrderInfo;
	}

	public DataDictionaryService getDataDictionaryService() {
		return dataDictionaryService;
	}

	public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
		this.dataDictionaryService = dataDictionaryService;
	}

	public String getReferenceValue() {
		return referenceValue;
	}

	public void setReferenceValue(String referenceValue) {
		this.referenceValue = referenceValue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public Map<String, String> getCustomerWorkOrderMapInfo() {
		return customerWorkOrderMapInfo;
	}

	public void setCustomerWorkOrderMapInfo(
			Map<String, String> customerWorkOrderMapInfo) {
		this.customerWorkOrderMapInfo = customerWorkOrderMapInfo;
	}
	
}
