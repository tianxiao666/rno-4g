package com.iscreate.op.action.routineinspection;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.dao.routineinspection.RoutineInspectionPlanDao;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionQuestion;
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
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.FileHelper;
import com.iscreate.plat.tools.TimeFormatHelper;
import com.iscreate.plat.tools.paginghelper.PagingHelper;


public class RoutineinspectionQuestionAction {
	private RoutineinspectionQuestionService routineinspectionQuestionService;
	private RoutineinspectionTaskorderQuestionService routineinspectionTaskorderQuestionService;
	private RoutineInspectionTaskService routineInspectionTaskService;
	private RoutineInspectionPlanDao routineInspectionPlanDao;
	private NetworkResourceInterfaceService networkResourceService;
	private SysOrgUserService sysOrgUserService;
	
	
	
	private String WOID;
	private String TOID;
	private String QID;
	private int currentPage=1;
	private int totalPage=1;
	private int pageSize;
	private String orgId;
	private String questionType;
	private String isOver;
	private String seriousLevel;
	private String description;
	private String resourceId;;
	private String resourceType;
	private Set<Map> routineinspectionQuestionList;
	private Map<String,String> questionInfo;
	private RoutineinspectionQuestion routineinspectionQuestion = new RoutineinspectionQuestion();
	private RoutineinspectionTaskorderQuestion routineinspectionTaskorderQuestion = new RoutineinspectionTaskorderQuestion();
	
	private File[] file;
	private String[] fileFileName;
	private String[] fileContentType;
	private String filePath;
	
	private  static final  Log log = LogFactory.getLog(RoutineinspectionQuestionAction.class);
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	/**
	 * 加载问题列表
	 * @return
	 */
	public String loadRoutineinspectionQuestionListAction(){
		String handler = (String) SessionService.getInstance().getValueByKey(
					"userId");
		String result = "success";
		this.routineinspectionQuestionList = new HashSet<Map>();
		Map<String,String> strParams = new HashMap<String, String>();
		Map<String,String> intParams = new HashMap<String, String>();
		if(this.questionType != null && !"".equals(this.questionType)){
			intParams.put("questionType", this.questionType);
		}
		if(this.seriousLevel != null && !"".equals(this.seriousLevel)){
			intParams.put("seriousLevel", this.seriousLevel);
		}
		if(this.description != null && !"".equals(this.description)){
			strParams.put("description", this.description);
		}
		if(this.currentPage>this.totalPage){
			this.currentPage = this.totalPage;
		}
		
		//获取问题列表
		Map<String, Object> map =  routineinspectionQuestionService.getRoutineinspectionQuestionListMapByOrg(currentPage,pageSize ,strParams,intParams,orgId,handler);
		if(map!=null){
			int totalCount = Integer.parseInt(map.get("count")+"");
			PagingHelper ph = new PagingHelper();
			Map<String, Object> pageMap = ph.calculatePagingParamService(totalCount, this.currentPage, this.pageSize);
			if(pageMap!=null && pageMap.size()>0){
				this.currentPage = Integer.parseInt(pageMap.get("currentPage")+"");
				this.pageSize = Integer.parseInt(pageMap.get("pageSize")+"");
				this.totalPage = Integer.parseInt(pageMap.get("totalPage")+"");
			}
			Date date = new Date();
			long treeDate = 1000*60*60*24*3;
			List<Map<String,Object>> list = (List<Map<String,Object>>) map.get("entityList");
			if(list!=null && list.size()>0){
				for (Map<String, Object> map2 : list) {
					String id = map2.get("id").toString();
					Map tqMap = routineinspectionTaskorderQuestionService.getRoutineinspectionTaskorderQuestion(id);
					String toId = "";
					if(tqMap!=null){
						toId = tqMap.get("toId").toString();
					}
					map2.put("toId", toId);
					if(map2.get("createTime")!=null){
						map2.put("createTime", TimeFormatHelper.getTimeFormatByDay(map2.get("createTime")));
					}else{
						map2.put("createTime", "");
					}
					if(map2.get("handleTime")!=null){
						map2.put("handleTime", TimeFormatHelper.getTimeFormatByDay(map2.get("handleTime")));
					}else{
						map2.put("handleTime", "");
					}
					this.routineinspectionQuestionList.add(map2);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 加载问题列表(查询)
	 * @return
	 */
	public String searchRoutineinspectionQuestionListAction(){
		log.info("进入 searchRoutineinspectionQuestionListAction");
		log.info("searchRoutineinspectionQuestionListAction Action层 加载问题列表(查询)。");
		String result = "success";
		this.routineinspectionQuestionList = new HashSet<Map>();
		Map<String,String> strParams = new HashMap<String, String>();
		Map<String,String> intParams = new HashMap<String, String>();
		if(this.questionType != null && !"".equals(this.questionType)){
			intParams.put("questionType", this.questionType);
		}
		if(this.seriousLevel != null && !"".equals(this.seriousLevel)){
			intParams.put("seriousLevel", this.seriousLevel);
		}
		if(this.isOver != null && !"".equals(this.isOver)){
			intParams.put("isOver", this.isOver);
		}
		if(this.description != null && !"".equals(this.description)){
			strParams.put("description", this.description);
		}
		if(this.currentPage>this.totalPage){
			this.currentPage = this.totalPage;
		}
		
		//获取问题列表
		Map<String, Object> map =  routineinspectionQuestionService.getRoutineinspectionQuestionListMap(currentPage,pageSize ,strParams,intParams,orgId);
		
		if(map!=null){
			int totalCount = Integer.parseInt(map.get("count")+"");
			PagingHelper ph = new PagingHelper();
			Map<String, Object> pageMap = ph.calculatePagingParamService(totalCount, this.currentPage, this.pageSize);
			if(pageMap!=null && pageMap.size()>0){
				this.currentPage = Integer.parseInt(pageMap.get("currentPage")+"");
				this.pageSize = Integer.parseInt(pageMap.get("pageSize")+"");
				this.totalPage = Integer.parseInt(pageMap.get("totalPage")+"");
			}
			Date date = new Date();
			long treeDate = 1000*60*60*24*3;
			List<Map<String,Object>> list = (List<Map<String,Object>>) map.get("entityList");
			if(list!=null && list.size()>0){
				for (Map<String, Object> map2 : list) {
					String id = map2.get("id").toString();
					Map tqMap = routineinspectionTaskorderQuestionService.getRoutineinspectionTaskorderQuestion(id);
					String toId = "";
					if(tqMap!=null){
						toId = tqMap.get("toId").toString();
					}
					map2.put("toId", toId);
					if(map2.get("createTime")!=null){
						map2.put("createTime", TimeFormatHelper.getTimeFormatByDay(map2.get("createTime")));
					}else{
						map2.put("createTime", "");
					}
					if(map2.get("handleTime")!=null){
						map2.put("handleTime", TimeFormatHelper.getTimeFormatByDay(map2.get("handleTime")));
					}else{
						map2.put("handleTime", "");
					}
					this.routineinspectionQuestionList.add(map2);
				}
			}
		}
		log.info("退出 searchRoutineinspectionQuestionListAction");
		return result;
	}
	
	/**
	 * 加载问题列表(任务管理)
	 * @return
	 */
	public String loadRoutineinspectionQuestionListByToIdAction(){
		log.info("进入 loadRoutineinspectionQuestionListByToIdAction");
		log.info("loadRoutineinspectionQuestionListByToIdAction Action层 跳转至新增问题页面。");
		if(TOID==null||"".equals(TOID)){
			log.error("巡检任务单号为空");
			throw new UserDefinedException("该巡检任务单不存在！");
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		String result = "success";
		Map<String,String> map = routineInspectionTaskService.loadRoutineInspectionInfoByToIdService(TOID);
		if(map==null){
			log.error("TOID = "+TOID+"的巡检任务单不存在");
			throw new UserDefinedException("该巡检任务单不存在！");
		}
		
		String status = map.get("statusName");
		if("已关闭".equals(status)){
			routineinspectionQuestionList =  routineinspectionQuestionService.getRoutineinspectionQuestionListMapByToId(TOID);
		}else{
			routineinspectionQuestionList =  routineinspectionQuestionService.getRoutineinspectionQuestionListMapByCloseStatus(TOID);
		}
		
		//获取问题列表
		if(routineinspectionQuestionList==null||routineinspectionQuestionList.isEmpty()){
			log.debug(" 巡检问题列表为空");
		}
		log.info("退出 loadRoutineinspectionQuestionListByToIdAction");
		return result;
	}
	
	/**
	 * 跳转至新增问题页面
	 * @return
	 */
	public String jumpAddRoutineinspectionQuestionPageAction(){
		log.info("进入 jumpAddRoutineinspectionQuestionPageAction");
		log.info("jumpAddRoutineinspectionQuestionPageAction Action层 跳转至新增问题页面。");
		log.info("参数 [TOID="+TOID+",resourceType="+resourceType+",resourceId="+resourceId+"]");
		if(TOID==null||"".equals(TOID)){
			log.error("巡检任务单号为空");
			throw new UserDefinedException("该巡检任务单不存在！");
		}
		String resourceName = "";
		if(resourceType!=null&&!"".equals(resourceType)&&resourceId!=null&&!"".equals(resourceId)){
			Map<String,String> resource =  networkResourceService.getResourceByReIdAndReTypeService(resourceId,resourceType);
			if(resource!=null){
				resourceName = resource.get("name");
			}
		}else{
			log.error("jumpAddRoutineinspectionQuestionPageAction 传入的资源Id和资源类型为空");
			throw new UserDefinedException("新增问题无法关联资源，该资源可能不存在");
		}
		questionInfo = new HashMap<String,String>();
		questionInfo.put("resourceName", resourceName);
		questionInfo.put("resourceType", resourceType);
		questionInfo.put("resourceId", resourceId);
		questionInfo.put("TOID", TOID);
		log.info("退出 jumpAddRoutineinspectionQuestionPageAction");
		return "success";
	}
	
	
	/**
	 * 新增问题
	 * @return
	 */public String addRoutineinspectionQuestionAction(){
		log.info("进入 handleRoutineinspectionQuestionAction");
		log.info("addRoutineinspectionQuestionAction Action层  处理问题。");
		log.info("参数 [TOID="+TOID+"]");
		if(TOID==null||"".equals(TOID)){
			log.error("巡检任务单号为空");
			throw new UserDefinedException("该巡检任务单不存在！");
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		String creator = (String) SessionService.getInstance().getValueByKey(
					"userId");
		String result = "success";
		
		 //获取当前登录人的中文名和所属最高组织
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(creator);
//		Account account = providerOrganizationService.getAccountByAccountId(creator);
		if(sysOrgUserByAccount==null){
			log.error("在 addRoutineinspectionQuestionAction 中调用 sysOrgUserService.getSysOrgUserByAccount() 参数 creator = "+creator+"，获取到的Account 对象为空。");
			throw new UserDefinedException("账号： "+creator+"不存在！");
			
		}
		
		//List<ProviderOrganization> orglist = providerOrganizationService.getTopLevelOrgByAccount(creator);
		//yuan.yw
		List<SysOrg> orglist = this.sysOrganizationService.getTopLevelOrgByAccount(creator);
		if(orglist==null||orglist.isEmpty()){
			log.error("在 addRoutineinspectionQuestionAction 中调用 providerOrganizationService.getTopLevelOrgByAccount() 参数 creator = "+creator+"，获取到的组织集合为空。");
			throw new UserDefinedException("账号： "+creator+"所属组织不存在！");
		}
		
		
		//上传图片
		filePath = "";
		if(this.file!=null && this.file.length>0){
			String savePath = ServletActionContext.getServletContext().getRealPath("");
			savePath = savePath + "/upload/routineinspection";
			List<String> saveFiles = FileHelper.saveFiles(savePath, this.file, this.fileFileName);
			//returnResult="success";
			filePath=saveFiles.get(0);
			filePath=filePath.replaceAll("\\\\", "/");
			filePath = filePath.substring(filePath.lastIndexOf("upload/routineinspection/") , filePath.length());
			String uri = request.getRequestURI().toString();
			String url = request.getRequestURL().toString();
			String subUrl = url.replace(uri, "").trim() + "/";
			filePath = subUrl+"ops/"+filePath;
			routineinspectionQuestion.setQuestionPicture(filePath);
		}
		
		//保存巡检问题
		routineinspectionQuestion.setCreateTime(new Date());
		routineinspectionQuestion.setCreator(creator);
		routineinspectionQuestion.setCreatorName(sysOrgUserByAccount.getName());
		routineinspectionQuestion.setCreatorOrgId(orglist.get(0).getOrgId());
		routineinspectionQuestion.setCreatorOrgName(orglist.get(0).getName());
		
		long qid = routineinspectionQuestionService.saveRoutineinspectionQuestion(routineinspectionQuestion);
		if(qid == -1){
			log.error("新增巡检问题失败");
			return "error";
		}
		log.info("成功新增巡检问题");
		
		//保存问题与任务的关联关系
		RoutineinspectionTaskorderQuestion routineinspectionTaskorderQuestion = new RoutineinspectionTaskorderQuestion();
		routineinspectionTaskorderQuestion.setQuestionId(qid+"");
		routineinspectionTaskorderQuestion.setToId(TOID);
		
		long qtid = routineinspectionTaskorderQuestionService.saveRoutineinspectionTaskorderQuestion(routineinspectionTaskorderQuestion);
		if(qtid == -1){
			log.error("保存巡检任务与问题关联关系失败 routineinspectionTaskorderQuestionService.saveRoutineinspectionTaskorderQuestion");
			return "error";
		}
		log.info("成功保存巡检任务与问题关联关系");
		log.info("退出addRoutineinspectionQuestionAction");
		return result;
	}
	 
	 /**
	  * 处理问题
	  * @return
	  */
	 public void handleRoutineinspectionQuestionAction(){
		log.info("进入 handleRoutineinspectionQuestionAction");
		log.info("handleRoutineinspectionQuestionAction Action层  处理问题。");
		log.info("参数 [TOID="+TOID+"]");
		if(TOID==null||"".equals(TOID)){
			log.error("巡检任务单号为空");
			throw new UserDefinedException("该巡检任务单不存在！");
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String handler = (String) SessionService.getInstance().getValueByKey(
					"userId");
		String result = "success";
		
		
		//获取当前登录人的中文名和所属最高组织
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(handler);
//		Account account = providerOrganizationService.getAccountByAccountId(handler);
		if(sysOrgUserByAccount==null){
			log.error("在 handleRoutineinspectionQuestionAction 中调用 sysOrgUserService.getSysOrgUserByAccount() 参数 creator = "+handler+"，获取到的Account 对象为空。");
			throw new UserDefinedException("账号： "+handler+"不存在！");
		}
		
		//更新巡检问题
		String qId = QID;
		if(qId==null||"".equals(qId)){
			log.error("巡检问题Id为空");
		}
		RoutineinspectionQuestion rq = routineinspectionQuestionService.getRoutineinspectionQuestionById(qId);
		if(routineinspectionQuestion==null){
			log.error("巡检问题Id="+qId+" 的实例为空");
			throw new UserDefinedException("巡检问题Id="+qId+" 的实例为空！");
		}
		
		//上传图片
		filePath = "";
		if(this.file!=null && this.file.length>0){
			String savePath = ServletActionContext.getServletContext().getRealPath("");
			savePath = savePath + "/upload/routineinspection";
			List<String> saveFiles = FileHelper.saveFiles(savePath, this.file, this.fileFileName);
			//returnResult="success";
			filePath=saveFiles.get(0);
			filePath=filePath.replaceAll("\\\\", "/");
			filePath = filePath.substring(filePath.lastIndexOf("upload/routineinspection/") , filePath.length());
			String uri = request.getRequestURI().toString();
			String url = request.getRequestURL().toString();
			String subUrl = url.replace(uri, "").trim() + "/";
			filePath = subUrl+"ops/"+filePath;
			rq.setHandlePicture(filePath);
		}
		
		rq.setHandler(handler);
		rq.setHandlerName(sysOrgUserByAccount.getName());
		rq.setHandleTime(new Date());
		rq.setHandleResult(routineinspectionQuestion.getHandleResult());
		
		routineinspectionQuestionService.updateRoutineinspectionQuestion(rq);
		log.info("成功新增巡检问题");
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			log.info("退出 handleRoutineinspectionQuestionAction()");
			response.getWriter().write(result);
		} catch (Exception e) {
			log.error("执行 handleRoutineinspectionQuestionAction() 发送数据到页面失败");
		}
	}
	 
	/**
	 * 展示问题信息
	 * @return
	 */ 
	public String loadRoutineinspectionQuestionInfoAction(){
		log.info("进入 loadRoutineinspectionQuestionInfoAction");
		log.info("loadRoutineinspectionQuestionInfoAction Action层  展示问题信息。");
		log.info("参数 [QID="+QID+",TOID="+TOID+"]");
		if(TOID==null||"".equals(TOID)){
			log.error("巡检任务单号为空");
			throw new UserDefinedException("参数 巡检任务单号为空！");
		}
		if(QID==null||"".equals(QID)){
			log.error("巡检问题Id为空");
			throw new UserDefinedException("参数 巡检问题Id为空！");
		}
		questionInfo = routineinspectionQuestionService.getRoutineinspectionQuestionInfoById(QID);
		if(questionInfo==null){
			log.error("巡检问题Id="+QID+" 的实例为空");
			throw new UserDefinedException("巡检问题Id="+QID+" 的实例为空！");
		}
		questionInfo.put("TOID", TOID);
		questionInfo.put("QID", QID);
		String accountId = questionInfo.get("creator").toString();
		//ou.jh
		SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(accountId);
//		Account account = providerOrganizationService.getAccountByAccountId(accountId);
		String creatorPhone = "";
		if(sysOrgUserByAccount.getMobile() != null){
			creatorPhone = sysOrgUserByAccount.getMobile();
		}else{
			if(sysOrgUserByAccount.getTel() != null){
				creatorPhone = sysOrgUserByAccount.getTel();
			}else{
				creatorPhone = "";
			}
		}
		questionInfo.put("creatorPhone", creatorPhone);
		log.info("退出 loadRoutineinspectionQuestionInfoAction");
		return "success";
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




	public String getWOID() {
		return WOID;
	}


	public void setWOID(String woid) {
		WOID = woid;
	}


	public String getTOID() {
		return TOID;
	}


	public void setTOID(String toid) {
		TOID = toid;
	}


	public String getQID() {
		return QID;
	}


	public void setQID(String qid) {
		QID = qid;
	}


	

	public Set<Map> getRoutineinspectionQuestionList() {
		return routineinspectionQuestionList;
	}

	public void setRoutineinspectionQuestionList(
			Set<Map> routineinspectionQuestionList) {
		this.routineinspectionQuestionList = routineinspectionQuestionList;
	}

	public RoutineinspectionQuestion getRoutineinspectionQuestion() {
		return routineinspectionQuestion;
	}


	public void setRoutineinspectionQuestion(
			RoutineinspectionQuestion routineinspectionQuestion) {
		this.routineinspectionQuestion = routineinspectionQuestion;
	}


	public RoutineinspectionTaskorderQuestion getRoutineinspectionTaskorderQuestion() {
		return routineinspectionTaskorderQuestion;
	}


	public void setRoutineinspectionTaskorderQuestion(
			RoutineinspectionTaskorderQuestion routineinspectionTaskorderQuestion) {
		this.routineinspectionTaskorderQuestion = routineinspectionTaskorderQuestion;
	}


	public int getCurrentPage() {
		return currentPage;
	}


	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}


	public int getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public Map<String, String> getQuestionInfo() {
		return questionInfo;
	}


	public void setQuestionInfo(Map<String, String> questionInfo) {
		this.questionInfo = questionInfo;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getSeriousLevel() {
		return seriousLevel;
	}

	public void setSeriousLevel(String seriousLevel) {
		this.seriousLevel = seriousLevel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsOver() {
		return isOver;
	}

	public void setIsOver(String isOver) {
		this.isOver = isOver;
	}

	public NetworkResourceInterfaceService getNetworkResourceService() {
		return networkResourceService;
	}

	public void setNetworkResourceService(
			NetworkResourceInterfaceService networkResourceService) {
		this.networkResourceService = networkResourceService;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}


	
	 
}

	
