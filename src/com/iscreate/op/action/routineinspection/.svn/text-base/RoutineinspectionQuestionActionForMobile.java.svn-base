package com.iscreate.op.action.routineinspection;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.dao.routineinspection.RoutineInspectionPlanDao;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionPlanworkorder;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionQuestion;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorder;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorderQuestion;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.routineinspection.RoutineInspectionTaskService;
import com.iscreate.op.service.routineinspection.RoutineinspectionQuestionService;
import com.iscreate.op.service.routineinspection.RoutineinspectionTaskorderQuestionService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageCommunicationHelper;

public class RoutineinspectionQuestionActionForMobile {
	private RoutineinspectionQuestionService routineinspectionQuestionService;
	private RoutineinspectionTaskorderQuestionService routineinspectionTaskorderQuestionService;
	private RoutineInspectionTaskService routineInspectionTaskService;
	private RoutineInspectionPlanDao routineInspectionPlanDao;
	private NetworkResourceInterfaceService networkResourceService;
	private SysOrgUserService sysOrgUserService;
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	
	private  static final  Log log = LogFactory.getLog(RoutineinspectionQuestionActionForMobile.class);
	
	/**
	 * 加载机房遗留问题列表页面
	 */
	public void loadRoutineinspectionQuestionListByResourceActionForMoblie(){
		log.info("进入 loadRoutineinspectionQuestionListByResourceActionForMoblie");
		log.info("loadRoutineinspectionQuestionListByResourceActionForMoblie Action层  加载机房遗留问题列表。");
		try {

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
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
			String woId = formJsonMap.get("WOID");
			String toId = formJsonMap.get("TOID");
			log.debug("巡检计划号 woId = "+woId);
			log.debug("巡检任务单号 toId = "+toId);
			
			RoutineinspectionTaskorder to =  routineInspectionTaskService.getRoutineInspectionTaskByToIdService(toId);
			
			if(to == null){
				log.error("巡检任务单号 toId = "+toId+" 的对象为空，调用routineInspectionTaskService.getRoutineInspectionTaskByToIdService");
				return;
			}
			
			RoutineinspectionPlanworkorder wo = routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId(woId);
			
			if(wo == null){
				log.error("巡检计划单号 woId = "+woId+" 的对象为空，调用routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId");
				return;
			}
			
			//获取问题列表
			List<Map> list =  routineinspectionQuestionService.getRoutineinspectionQuestionListMapByResource(toId);
			
			if(list!=null&&!list.isEmpty()){
				//巡检问题列表
				Map routineinspectionQuestionListMap = new HashMap();
				routineinspectionQuestionListMap.put("routineinspectionQuestionList", gson.toJson(list));
				log.debug("机房遗留问题列表 list == "+list);
				mch.addGroup("routineinspectionQuestionListMapArea",routineinspectionQuestionListMap); 
			}else{
				log.debug("巡检问题列表为空");
			}
			
			
			String resourceType=to.getResourceType();
			String resourceId=to.getResourceId();
			
			Map headerMap = new HashMap();
			headerMap.put("TOID", toId);
			headerMap.put("WOID", woId);
			headerMap.put("resourceType", resourceType);
			headerMap.put("resourceId", resourceId);
			mch.addGroup("header",headerMap); 
			
			Map wrapperMap = new HashMap();
			wrapperMap.put("taskTitle",to.getTaskTitle() );
			wrapperMap.put("planTitle",wo.getPlanTitle() );
			wrapperMap.put("taskId",toId );
			mch.addGroup("wrapper",wrapperMap);
			
			mobilePackage.setContent(mch.mapToJson());
			log.info("退出 loadRoutineinspectionQuestionListByResourceActionForMoblie");
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			
		} catch (RuntimeException e) {
			log.error("执行 loadRoutineinspectionQuestionListByResourceActionForMoblie 失败");
			MobilePackageCommunicationHelper.responseMobileException(e);
		} 
	}
	
	
	/**
	 * 加载添加问题页面
	 */
	public void loadAddRoutineinspectionQuestionPageActionForMobile(){
		log.info("进入 loadAddRoutineinspectionQuestionPageActionForMobile");
		log.info("loadAddRoutineinspectionQuestionPageActionForMobile Action层  加载添加问题页面。");
		try {

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
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
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			String resourceType = formJsonMap.get("resourceType");
			String resourceId = formJsonMap.get("resourceId");
			log.debug("巡检任务单号 toId = "+toId);
			
			RoutineinspectionTaskorder to =  routineInspectionTaskService.getRoutineInspectionTaskByToIdService(toId);
			
			if(to == null){
				log.error("巡检任务单号 toId = "+toId+" 的对象为空，调用routineInspectionTaskService.getRoutineInspectionTaskByToIdService");
				return;
			}
			
			RoutineinspectionPlanworkorder wo = routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId(woId);
			
			if(wo == null){
				log.error("巡检计划单号 woId = "+woId+" 的对象为空，调用routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId");
				return;
			}
			
			Map formMap = new HashMap();
			formMap.put("TOID", toId);
			formMap.put("WOID", woId);
			formMap.put("resourceType", resourceType);
			formMap.put("resourceId", resourceId);
			formMap.put("taskTitle", to.getTaskTitle());
			formMap.put("planTitle", wo.getPlanTitle());
			formMap.put("taskId", toId);
			mch.addGroup("addQuestionForm",formMap);
			
			mobilePackage.setContent(mch.mapToJson());
			log.info("退出 loadAddRoutineinspectionQuestionPageActionForMobile");
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			
		} catch (RuntimeException e) {
			log.error("执行 loadAddRoutineinspectionQuestionPageActionForMobile 失败");
			MobilePackageCommunicationHelper.responseMobileException(e);
		} 
	}
	
	/**
	 * 加载处理问题页面
	 */
	public void loadHandleRoutineinspectionQuestionPageActionForMobile(){
		log.info("进入 loadHandleRoutineinspectionQuestionPageActionForMobile");
		log.info("loadHandleRoutineinspectionQuestionPageActionForMobile Action层  加载处理问题页面。");
		try {

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
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
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			String qId = formJsonMap.get("QID");
			String resourceType = formJsonMap.get("resourceType");
			String resourceId = formJsonMap.get("resourceId");
			log.debug("巡检任务单号 toId = "+toId);
			
			RoutineinspectionTaskorder to =  routineInspectionTaskService.getRoutineInspectionTaskByToIdService(toId);
			
			if(to == null){
				log.error("巡检任务单号 toId = "+toId+" 的对象为空，调用routineInspectionTaskService.getRoutineInspectionTaskByToIdService");
				return;
			}
			
			RoutineinspectionPlanworkorder wo = routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId(woId);
			
			if(wo == null){
				log.error("巡检计划单号 woId = "+woId+" 的对象为空，调用routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId");
				return;
			}
			
			RoutineinspectionQuestion routineinspectionQuestion = routineinspectionQuestionService.getRoutineinspectionQuestionById(qId);
			if(routineinspectionQuestion==null){
				log.error("巡检问题Id="+qId+" 的实例为空");
				MobilePackageCommunicationHelper.responseMobileError("巡检问题Id="+qId+" 的实例为空");
				return;
			}
			//获取实景照片路径
//			HttpServletRequest request = ServletActionContext.getRequest();
//			String uri = request.getRequestURI().toString();
//			String url = request.getRequestURL().toString();
//			String subUrl = url.replace(uri, "").trim() + "/";
			String picturePath = routineinspectionQuestion.getQuestionPicture();
			Map formMap = new HashMap();
			formMap.put("TOID", toId);
			formMap.put("WOID", woId);
			formMap.put("QID", qId);
			formMap.put("resourceType", resourceType);
			formMap.put("resourceId", resourceId);
			formMap.put("picturePath", picturePath);
			formMap.put("taskTitle", to.getTaskTitle());
			formMap.put("planTitle", wo.getPlanTitle());
			formMap.put("taskId", toId);
			
			mch.addGroup("handleQuestionForm",formMap);
			mobilePackage.setContent(mch.mapToJson());
			log.info("退出 loadHandleRoutineinspectionQuestionPageActionForMobile");
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			
		} catch (RuntimeException e) {
			log.error("执行 loadHandleRoutineinspectionQuestionPageActionForMobile 失败");
			MobilePackageCommunicationHelper.responseMobileException(e);
		} 
	}
	
	/**
	 * 加载原图
	 */
	public void loadRoutineinspectionQuestionPictureActionForMobile(){
		log.info("进入 loadRoutineinspectionQuestionPictureActionForMobile");
		log.info("loadRoutineinspectionQuestionPictureActionForMobile Action层  加载处理问题页面。");
		try {

			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
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
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			String qId = formJsonMap.get("QID");
			log.debug("巡检任务单号 toId = "+toId);
			
			RoutineinspectionQuestion routineinspectionQuestion = routineinspectionQuestionService.getRoutineinspectionQuestionById(qId);
			if(routineinspectionQuestion==null){
				log.error("巡检问题Id="+qId+" 的实例为空");
				MobilePackageCommunicationHelper.responseMobileError("巡检问题Id="+qId+" 的实例为空");
				return;
			}
			//获取实景照片路径
			String picturePath = routineinspectionQuestion.getQuestionPicture();
			Map formMap = new HashMap();
			formMap.put("TOID", toId);
			formMap.put("WOID", woId);
			formMap.put("picturePath", picturePath);
			mch.addGroup("header",formMap);
			mobilePackage.setContent(mch.mapToJson());
			log.info("退出 loadRoutineinspectionQuestionPictureActionForMobile");
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			
		} catch (RuntimeException e) {
			log.error("执行 loadRoutineinspectionQuestionPictureActionForMobile 失败");
			MobilePackageCommunicationHelper.responseMobileException(e);
		} 
	}
	
	/**
	 * 新增问题
	 */
	public void addRoutineinspectionQuestionActionForMoblie(){
		log.info("进入 addRoutineinspectionQuestionActionForMoblie");
		log.info("addRoutineinspectionQuestionActionForMoblie Action层  新增巡检问题。");
		try {
			String creator = (String) SessionService.getInstance().getValueByKey("userId");
			log.debug("当前登录人 ："+creator);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			HttpServletRequest request = ServletActionContext.getRequest();
			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();
			
			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			
			String fileStr = mobilePackage.getFileData();
			String picture = "";
			if(fileStr!=null && !fileStr.trim().equals(""))
			{
				String prefixPath = ServletActionContext.getServletContext().getRealPath("");
				String suffixPath = "/upload/routineinspection";
				picture = MobilePackageCommunicationHelper.copyPhoto(prefixPath,suffixPath, fileStr.trim());
				String uri = request.getRequestURI().toString();
				String url = request.getRequestURL().toString();
				String subUrl = url.replace(uri, "").trim() + "/";
				picture = subUrl+"ops"+picture;
				
			}

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
//			Map<String, String> formJsonMap = new HashMap<String,String>();
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			log.debug("参数 [woId="+woId+",toId="+toId+"]");
			
			RoutineinspectionTaskorder to =  routineInspectionTaskService.getRoutineInspectionTaskByToIdService(toId);
			
			if(to == null){
				log.error("巡检任务单号 toId = "+toId+" 的对象为空，调用routineInspectionTaskService.getRoutineInspectionTaskByToIdService");
				return;
			}
			
			RoutineinspectionPlanworkorder wo = routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId(woId);
			
			if(wo == null){
				log.error("巡检计划单号 woId = "+woId+" 的对象为空，调用routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId");
				return;
			}
			
			String resourceType=to.getResourceType();
			String resourceId=to.getResourceId();
			String resourceName = "";
			if(resourceType!=null&&!"".equals(resourceType)&&resourceId!=null&&!"".equals(resourceId)){
				Map<String,String> resource =  networkResourceService.getResourceByReIdAndReTypeService(resourceId,resourceType);
				if(resource!=null){
					resourceName = resource.get("name");
				}
			}
			
			//获取当前登录人的中文名和所属最高组织
			//ou.jh
			SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(creator);
//			Account account = providerOrganizationService.getAccountByAccountId(creator);
			if(sysOrgUserByAccount==null){
				log.error("在 addRoutineinspectionQuestionActionForMoblie 中调用 sysOrgUserService.getSysOrgUserByAccount() 参数 creator = "+creator+"，获取到的Account 对象为空。");
				MobilePackageCommunicationHelper.responseMobileError("在 addRoutineinspectionQuestionActionForMoblie 中调用 providerOrganizationService.getSysOrgUserByAccount 参数 creator = "+creator+"，获取到的Account 对象为空。");
				return;
			}
			
			//List<ProviderOrganization> orglist = providerOrganizationService.getTopLevelOrgByAccount(creator);
			List<SysOrg> orglist = this.sysOrganizationService.getTopLevelOrgByAccount(creator);
			if(orglist==null||orglist.isEmpty()){
				log.error("在 addRoutineinspectionQuestionActionForMoblie 中调用 providerOrganizationService.getTopLevelOrgByAccount() 参数 creator = "+creator+"，获取到的组织集合为空。");
				MobilePackageCommunicationHelper.responseMobileError("在 addRoutineinspectionQuestionActionForMoblie 中调用 providerOrganizationService.getSysOrgUserByAccount() 参数 creator = "+creator+"，获取到的组织集合为空。");
				return;
			}
			
//			File file = formJsonMap.get("questionPicture");
			
			//保存巡检问题
			RoutineinspectionQuestion routineinspectionQuestion = new RoutineinspectionQuestion();
			routineinspectionQuestion.setCreateTime(new Date());
			routineinspectionQuestion.setCreator(creator);
			routineinspectionQuestion.setCreatorName(sysOrgUserByAccount.getName());
			routineinspectionQuestion.setCreatorOrgId(orglist.get(0).getOrgId());
			routineinspectionQuestion.setCreatorOrgName(orglist.get(0).getName());
			routineinspectionQuestion.setDescription(formJsonMap.get("description"));
			routineinspectionQuestion.setQuestionPicture(picture);
			routineinspectionQuestion.setQuestionType(formJsonMap.get("questionType"));
			routineinspectionQuestion.setSeriousLevel(formJsonMap.get("seriousLevel"));
			routineinspectionQuestion.setResourceId(resourceId);
			routineinspectionQuestion.setResourceType(resourceType);
			routineinspectionQuestion.setResourceName(resourceName);
			
			long qid = routineinspectionQuestionService.saveRoutineinspectionQuestion(routineinspectionQuestion);
			if(qid == -1){
				log.error("新增巡检问题失败");
				MobilePackageCommunicationHelper.responseMobileError("新增巡检问题失败");
				return;
			}
			log.info("成功新增巡检问题");
			
			//保存问题与任务的关联关系
			RoutineinspectionTaskorderQuestion routineinspectionTaskorderQuestion = new RoutineinspectionTaskorderQuestion();
			routineinspectionTaskorderQuestion.setQuestionId(qid+"");
			routineinspectionTaskorderQuestion.setToId(toId);
			
			long qtid = routineinspectionTaskorderQuestionService.saveRoutineinspectionTaskorderQuestion(routineinspectionTaskorderQuestion);
			if(qtid == -1){
				log.error("保存巡检任务与问题关联关系失败 routineinspectionTaskorderQuestionService.saveRoutineinspectionTaskorderQuestion");
				MobilePackageCommunicationHelper.responseMobileError("保存巡检任务与问题关联关系失败 routineinspectionTaskorderQuestionService.saveRoutineinspectionTaskorderQuestion");
				return;
			}
			log.info("成功保存巡检任务与问题关联关系");
			
			//保存成功后，加载问题列表页面
			List<Map> list =  routineinspectionQuestionService.getRoutineinspectionQuestionListMapByResource(toId);
			if(list!=null&&!list.isEmpty()){
				//巡检问题列表
				Map routineinspectionQuestionListMap = new HashMap();
				routineinspectionQuestionListMap.put("routineinspectionQuestionList", gson.toJson(list));
				log.debug("机房遗留问题列表 list == "+list);
				mch.addGroup("routineinspectionQuestionListMapArea",routineinspectionQuestionListMap); 
				
			}else{
				log.debug("巡检问题列表为空");
			}
			
			
			Map headerMap = new HashMap();
			headerMap.put("TOID", toId);
			headerMap.put("WOID", woId);
			headerMap.put("resourceType", resourceType);
			headerMap.put("resourceId", resourceId);
			mch.addGroup("header",headerMap);
			
			Map wrapperMap = new HashMap();
			wrapperMap.put("taskTitle",to.getTaskTitle() );
			wrapperMap.put("planTitle",wo.getPlanTitle() );
			wrapperMap.put("taskId",toId );
			mch.addGroup("wrapper",wrapperMap);
			
			mobilePackage.setContent(mch.mapToJson());
			log.info("退出 addRoutineinspectionQuestionActionForMoblie");
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			
		} catch (RuntimeException e) {
			log.error("执行 addRoutineinspectionQuestionActionForMoblie 失败");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
	}
	
	
	/**
	 * 处理问题
	 */
	public void handleRoutineinspectionQuestionActionForMoblie(){
		log.info("进入 handleRoutineinspectionQuestionActionForMoblie");
		log.info("handleRoutineinspectionQuestionActionForMoblie Action层  处理巡检问题。");
		try {
			String handler = (String) SessionService.getInstance().getValueByKey("userId");
			log.debug("当前登录人 ："+handler);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			MobilePackage mobilePackage = MobilePackageCommunicationHelper.getMobilePackage();

//			MobilePackage mobilePackage =null;
			
			// mobilePackage为空，返回错误信息
			if (mobilePackage == null) {
				MobilePackageCommunicationHelper.responseMobilePackageIsNull();
				return;
			}
			
			HttpServletRequest request = ServletActionContext.getRequest();
			
//			String jsonStr = request.getParameter("jsonArrayObj"); // 获取请求参数
//			String fileStr = request.getParameter("fileData");
//			if(jsonStr!=null && !jsonStr.trim().equals(""))
//			{
//				try {
//					jsonStr=URLDecoder.decode(jsonStr,"UTF-8");
//					mobilePackage = gson.fromJson(jsonStr,new TypeToken<MobilePackage>(){}.getType());
//				} catch (Exception e) {
//					mobilePackage =null;
//				}
//			}
			
			String fileStr =mobilePackage.getFileData();
			String picture = "";
			if(fileStr!=null && !fileStr.trim().equals(""))
			{
				String prefixPath = ServletActionContext.getServletContext().getRealPath("");
				String suffixPath = "/upload/routineinspection";
				picture = MobilePackageCommunicationHelper.copyPhoto(prefixPath,suffixPath, fileStr.trim());
				String uri = request.getRequestURI().toString();
				String url = request.getRequestURL().toString();
				String subUrl = url.replace(uri, "").trim() + "/";
				picture = subUrl+"ops"+picture;
			}
			

			String content = mobilePackage.getContent();
			MobileContentHelper mch = new MobileContentHelper();
			mch.setContent(content);
//			Map<String, String> formJsonMap = new HashMap<String,String>();
			Map<String, String> formJsonMap = mch.getGroupByKey("request");
			String toId = formJsonMap.get("TOID");
			String woId = formJsonMap.get("WOID");
			String qId = formJsonMap.get("QID");
			log.debug("巡检任务单号 toId = "+toId);
			log.debug("巡检问题Id qId = "+qId);
			
			
			RoutineinspectionTaskorder to =  routineInspectionTaskService.getRoutineInspectionTaskByToIdService(toId);
			
			if(to == null){
				log.error("巡检任务单号 toId = "+toId+" 的对象为空，调用routineInspectionTaskService.getRoutineInspectionTaskByToIdService");
				return;
			}
			
			RoutineinspectionPlanworkorder wo = routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId(woId);
			
			if(wo == null){
				log.error("巡检计划单号 woId = "+woId+" 的对象为空，调用routineInspectionPlanDao.getRoutineinspectionPlanworkorderByWoId");
				return;
			}
			
			
			//获取当前登录人的中文名和所属最高组织
			//ou.jh
			SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(handler);
//			Account account = providerOrganizationService.getAccountByAccountId(handler);
			if(sysOrgUserByAccount==null){
				log.error("在 addRoutineinspectionQuestionActionForMoblie 中调用 providerOrganizationService.getSysOrgUserByAccount() 参数 creator = "+handler+"，获取到的Account 对象为空。");
				MobilePackageCommunicationHelper.responseMobileError("在 addRoutineinspectionQuestionActionForMoblie 中调用 providerOrganizationService.getSysOrgUserByAccount 参数 creator = "+handler+"，获取到的Account 对象为空。");
				return;
			}
			
			//更新巡检问题
			if(qId==null||"".equals(qId)){
				log.error("巡检问题Id为空");
				MobilePackageCommunicationHelper.responseMobileError("巡检问题Id为空");
				return;
			}
			RoutineinspectionQuestion routineinspectionQuestion = routineinspectionQuestionService.getRoutineinspectionQuestionById(qId);
			if(routineinspectionQuestion==null){
				log.error("巡检问题Id="+qId+" 的实例为空");
				MobilePackageCommunicationHelper.responseMobileError("巡检问题Id="+qId+" 的实例为空");
				return;
			}
			
			routineinspectionQuestion.setHandlePicture(picture);
			routineinspectionQuestion.setHandler(handler);
			routineinspectionQuestion.setHandleResult(formJsonMap.get("handleResult"));
			routineinspectionQuestion.setHandlerName(sysOrgUserByAccount.getName());
			routineinspectionQuestion.setHandleTime(new Date());
			
			routineinspectionQuestionService.updateRoutineinspectionQuestion(routineinspectionQuestion);
			log.info("成功新增巡检问题");
			
			
			//保存成功后，加载问题列表页面
			List<Map> list =  routineinspectionQuestionService.getRoutineinspectionQuestionListMapByResource(toId);
			if(list!=null&&!list.isEmpty()){
				//巡检问题列表
				Map routineinspectionQuestionListMap = new HashMap();
				routineinspectionQuestionListMap.put("routineinspectionQuestionList", gson.toJson(list));
				log.debug("机房遗留问题列表 list == "+list);
				mch.addGroup("routineinspectionQuestionListMapArea",routineinspectionQuestionListMap); 
				
			}else{
				log.debug("巡检问题列表为空");
			}
			
			String resourceType=to.getResourceType();
			String resourceId=to.getResourceId();
			
			
			Map headerMap = new HashMap();
			headerMap.put("TOID", toId);
			headerMap.put("WOID", woId);
			headerMap.put("resourceType", resourceType);
			headerMap.put("resourceId", resourceId);
			mch.addGroup("header",headerMap);
			
			Map wrapperMap = new HashMap();
			wrapperMap.put("taskTitle",to.getTaskTitle() );
			wrapperMap.put("planTitle",wo.getPlanTitle() );
			wrapperMap.put("taskId",toId );
			mch.addGroup("wrapper",wrapperMap);
			
			mobilePackage.setContent(mch.mapToJson());
			log.info("退出 addRoutineinspectionQuestionActionForMoblie");
			// 返回content的JSON字符串信息
			MobilePackageCommunicationHelper.responseMobileSuccess(mobilePackage);
			
		} catch (RuntimeException e) {
			log.error("执行 handleRoutineinspectionQuestionActionForMoblie 失败");
			MobilePackageCommunicationHelper.responseMobileException(e);
		}
	}
	

	public RoutineinspectionQuestionService getRoutineinspectionQuestionService() {
		return routineinspectionQuestionService;
	}

	public void setRoutineinspectionQuestionService(
			RoutineinspectionQuestionService routineinspectionQuestionService) {
		this.routineinspectionQuestionService = routineinspectionQuestionService;
	}

	public RoutineinspectionTaskorderQuestionService getRoutineinspectionTaskorderQuestionService() {
		return routineinspectionTaskorderQuestionService;
	}

	public void setRoutineinspectionTaskorderQuestionService(
			RoutineinspectionTaskorderQuestionService routineinspectionTaskorderQuestionService) {
		this.routineinspectionTaskorderQuestionService = routineinspectionTaskorderQuestionService;
	}



	public RoutineInspectionTaskService getRoutineInspectionTaskService() {
		return routineInspectionTaskService;
	}


	public void setRoutineInspectionTaskService(
			RoutineInspectionTaskService routineInspectionTaskService) {
		this.routineInspectionTaskService = routineInspectionTaskService;
	}


	public RoutineInspectionPlanDao getRoutineInspectionPlanDao() {
		return routineInspectionPlanDao;
	}


	public void setRoutineInspectionPlanDao(
			RoutineInspectionPlanDao routineInspectionPlanDao) {
		this.routineInspectionPlanDao = routineInspectionPlanDao;
	}


	public NetworkResourceInterfaceService getNetworkResourceService() {
		return networkResourceService;
	}


	public void setNetworkResourceService(
			NetworkResourceInterfaceService networkResourceService) {
		this.networkResourceService = networkResourceService;
	}

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}


	
	
}
