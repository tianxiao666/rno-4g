package com.iscreate.op.service.routineinspection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.dao.routineinspection.RoutineinspectionQuestionDao;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionQuestion;
import com.iscreate.op.pojo.routineinspection.RoutineinspectionTaskorder;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.workmanage.CommonQueryService;
import com.iscreate.plat.exceptioninteceptor.service.UserDefinedException;
import com.iscreate.plat.tools.TimeFormatHelper;

public class RoutineinspectionQuestionServiceImpl implements RoutineinspectionQuestionService{
	private RoutineinspectionQuestionDao routineinspectionQuestionDao;
	private RoutineInspectionTaskService routineInspectionTaskService;
	private CommonQueryService commonQueryService;
	private  static final  Log log = LogFactory.getLog(RoutineinspectionQuestionServiceImpl.class);
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	/**
	 * 保存巡检问题
	 * @param routineinspectionQuestion
	 */
	public long saveRoutineinspectionQuestion(RoutineinspectionQuestion routineinspectionQuestion){
		log.info("进入 saveRoutineinspectionQuestion");
		log.info("saveRoutineinspectionQuestion Service层 保存巡检问题。");
		if(routineinspectionQuestion!=null){
			routineinspectionQuestion.setIsOver(0);
			long qid = routineinspectionQuestionDao.saveRoutineinspectionQuestionDao(routineinspectionQuestion);
			log.info("退出 saveRoutineinspectionQuestion");
			return qid;
		}else{
			
			log.error("参数 巡检问题类 routineinspectionQuestion 为空");
			log.error("执行 saveRoutineinspectionQuestion 失败");
			return -1;
		}
		
	}
	
	/**
	 * 更新巡检问题
	 * @param routineinspectionQuestion
	 */
	public void updateRoutineinspectionQuestion(RoutineinspectionQuestion routineinspectionQuestion){
		log.info("进入 updateRoutineinspectionQuestion");
		log.info("updateRoutineinspectionQuestion Service层 更新巡检问题。");
		if(routineinspectionQuestion!=null){
			routineinspectionQuestion.setIsOver(1);
			routineinspectionQuestionDao.updateRoutineinspectionQuestionDao(routineinspectionQuestion);
			log.info("退出 updateRoutineinspectionQuestion");
		}else{
			log.error("参数 巡检问题类 routineinspectionQuestion 为空");
			log.error("执行 updateRoutineinspectionQuestion 失败");
		}
		
	}
	
	
	/**
	 *  根据资源获取相关巡检问题列表
	 *  巡检问题点其实就是每个网络资源发生的问题记录，所以问题点既跟任务单挂钩又关网络资源挂钩
	 */
	public List<Map> getRoutineinspectionQuestionListMapByResource(String toId){
		log.info("进入 queryRoutineinspectionQuestionListByResource");
		log.info("queryRoutineinspectionQuestionListByResource Service层  根据巡检任务单号获取相关巡检问题列表。");
		log.info("参数 巡检任务单号 toId = "+toId);
		if(toId==null||"".equals(toId)){
			log.error("in getRoutineinspectionQuestionListMapByResource 参数 toId为空");
			throw new UserDefinedException("in getRoutineinspectionQuestionListMapByResource 参数 toId为空");
		}
		List<Map> list = new ArrayList<Map>();
		//找出任务单号的任务单实例
		RoutineinspectionTaskorder to = routineInspectionTaskService.getRoutineInspectionTaskByToIdService(toId);
		if(to!=null){
			//获取与该任务关联的网络资源
			String resourceType = to.getResourceType();
			String resourceId = to.getResourceId();
			log.info("resourceType = "+resourceType);
			log.info("resourceId = "+resourceId);
			//找出与网络资源关联的问题点
			if(resourceType!=null&&!"".equals(resourceType)&&resourceId!=null&&!"".equals(resourceId)){
				list = routineinspectionQuestionDao.queryRoutineinspectionQuestionListMapByResource(resourceType, resourceId);
				if(list==null||list.isEmpty()){
					log.debug("类型为"+resourceType+"，标识为"+resourceId+"的资源关联的巡检问题为空");
				}else{
					for(Map map : list){
						map.put("createTime", TimeFormatHelper.getTimeFormatBySecond(map.get("createTime")));
						map.put("handleTime", TimeFormatHelper.getTimeFormatBySecond(map.get("handleTime")));
					}
				}
			}
		}
		
		log.info("退出 queryRoutineinspectionQuestionListByResource");
		return list;
	}
	
	/**
	 *  根据toId关联查找关联的问题（包括与任务单关联的问题和与该任务单关联的网络资源所关联的问题）
	 *  这是当巡检任务状态为“已关闭”时调用的接口
	 */
	public Set<Map> getRoutineinspectionQuestionListMapByCloseStatus(String toId){
		log.info("进入 getRoutineinspectionQuestionListMapByCloseStatus");
		log.info("getRoutineinspectionQuestionListMapByCloseStatus Service层  根据资源获取相关巡检问题列表,任务已关闭状态。");
		log.info("参数 巡检任务单号 toId = "+toId);
		if(toId==null||"".equals(toId)){
			log.error("in getRoutineinspectionQuestionListMapByResource 参数 toId为空");
			throw new UserDefinedException("in getRoutineinspectionQuestionListMapByResource 参数 toId为空");
		}
		Set<Map> set = new HashSet<Map>();
		RoutineinspectionTaskorder to = routineInspectionTaskService.getRoutineInspectionTaskByToIdService(toId);
		if(to!=null){
			String resourceType = to.getResourceType();
			String resourceId = to.getResourceId();
			log.info("resourceType = "+resourceType);
			log.info("resourceId = "+resourceId);
			if(resourceType!=null&&!"".equals(resourceType)&&resourceId!=null&&!"".equals(resourceId)){
				//与网络资源关联的问题
				List<Map> list = routineinspectionQuestionDao.queryRoutineinspectionQuestionListMapByResource(resourceType, resourceId);
				//任务单关联的问题
				List<Map> list2 = routineinspectionQuestionDao.queryRoutineinspectionQuestionListMapByToId(toId);
				if(list==null||list.isEmpty()){
					log.debug("类型为"+resourceType+"，标识为"+resourceId+"的资源关联的巡检问题为空");
				}else{
					set.addAll(list);
					if(list2!=null&&!list2.isEmpty()){
						for(Map map1 : list){
							for(Map map2 : list2){
								if(map1.get("id").equals(map2.get("id"))){
									break;
								}else{
									set.add(map2);
								}
								
							}
						}
					}
					for(Map map : set){
						map.put("createTime", TimeFormatHelper.getTimeFormatBySecond(map.get("createTime")));
						map.put("handleTime", TimeFormatHelper.getTimeFormatBySecond(map.get("handleTime")));
						map.put("toId", toId);
					}
				}
			}
		}
		
		log.info("退出 getRoutineinspectionQuestionListMapByCloseStatus");
		return set;
	}
	
	/**
	 * 根据组织以及其子组织关联查找关联的问题（分页）
	 * @param handler
	 * @return
	 */
	public Map<String,Object> getRoutineinspectionQuestionListMapByOrg(int currentPage,int pageSize ,Map<String,String> strParams,Map<String,String> intParams,String orgId,String handler){
		log.info("进入 getRoutineinspectionQuestionListMapByOrg");
		log.info("getRoutineinspectionQuestionListMapByOrg Service层 根据组织以及其子组织关联查找关联的问题（分页）。");
		if(currentPage<=0){
			currentPage = 1;
		}
		String start = (currentPage-1)*pageSize+"";
		String sql = "";
		if(strParams!=null && strParams.size()>0){
			for (String key : strParams.keySet()) {
				sql += " and \""+key+"\" like '%" + strParams.get(key) + "%'";
			}
		}
		if(intParams!=null && intParams.size()>0){
			for (String key : intParams.keySet()) {
				sql += " and \""+key+"\" ='" + intParams.get(key) + "'";
			}
		}
		List<Map> questionList = new ArrayList<Map>();
		
		long selectOrgId = 0;
		if(orgId!=null&&!"".equals(orgId)){
			selectOrgId = Long.parseLong(orgId);
		}else{
			//List<ProviderOrganization> topOrgList = providerOrganizationService.getTopLevelOrgByAccount(handler);
			List<SysOrg> topOrgList = this.sysOrganizationService.getTopLevelOrgByAccount(handler);

			if(topOrgList == null || topOrgList.isEmpty()){
				return null;
			}
			//所属最高组织
			SysOrg topOrg = topOrgList.get(0);
			selectOrgId = topOrg.getOrgId();
		}
		
		//获取子组织
		//List<ProviderOrganization> orgList = providerOrganizationService.getOrgListDownwardByOrg(selectOrgId);
		//yuan.yw
		List<SysOrg> orgList = this.sysOrganizationService.getOrgListDownwardByOrg(selectOrgId);
		List<String> params = new ArrayList<String>();
		if(orgList!=null&&!orgList.isEmpty()){
			for(SysOrg org : orgList ){
				params.add(org.getOrgId()+"");
			}
		}else{
			params.add(selectOrgId+"");
		}
		
		String orgIds = "(";
		for(int i=0;i<params.size();i++){
			if(i==0){
				orgIds += params.get(0);
			}else{
				orgIds += ","+params.get(i);
			}
			
		}
		orgIds += ")";
		sql += " and \"creatorOrgId\" in "+orgIds;
		sql += " and \"isOver\" = 0";
		log.info("后缀sql=="+sql);
		
		Map<String, Object> commonQueryService = this.commonQueryService.commonQueryService( start , pageSize+"" , null , null , "v_insp_question" , null , sql);
		log.info("退出 getRoutineinspectionQuestionListMapByOrg");
		return commonQueryService;
	}
	
	/**
	 * 查找关联的问题（分页）
	 * 问题点查询页面
	 * @param handler
	 * @return
	 */
	public Map<String,Object> getRoutineinspectionQuestionListMap(int currentPage,int pageSize ,Map<String,String> strParams,Map<String,String> intParams,String orgId){
		log.info("进入 getRoutineinspectionQuestionListMap");
		log.info("getRoutineinspectionQuestionListMap Service层 查找关联的问题（分页）。");
		if(currentPage<=0){
			currentPage = 1;
		}
		String start = (currentPage-1)*pageSize+"";
		String sql = "";
		if(strParams!=null && strParams.size()>0){
			for (String key : strParams.keySet()) {
				sql += " and \""+key+"\" like '%" + strParams.get(key) + "%'";
			}
		}
		if(intParams!=null && intParams.size()>0){
			for (String key : intParams.keySet()) {
				if(key.equals("isOver")){
					sql += " and \""+key+"\"=" + intParams.get(key);
				}else{
					sql += " and \""+key+"\"='" + intParams.get(key) + "'";
				}
				
			}
		}
		List<Map> questionList = new ArrayList<Map>();
		
		long selectOrgId = 0;
		if(orgId!=null&&!"".equals(orgId)){
			selectOrgId = Long.parseLong(orgId);
			//获取子组织
			//List<ProviderOrganization> orgList = providerOrganizationService.getOrgListDownwardByOrg(selectOrgId);
			//yuan.yw
			List<SysOrg> orgList = this.sysOrganizationService.getOrgListDownwardByOrg(selectOrgId);
			List<String> params = new ArrayList<String>();
			if(orgList!=null&&!orgList.isEmpty()){
				for(SysOrg org : orgList ){
					params.add(org.getOrgId()+"");
				}
			}else{
				params.add(selectOrgId+"");
			}
			
			String orgIds = "(";
			for(int i=0;i<params.size();i++){
				if(i==0){
					orgIds += params.get(0);
				}else{
					orgIds += ","+params.get(i);
				}
				
			}
			orgIds += ")";
			
			sql += " and \"creatorOrgId\" in "+orgIds;
		}
		log.info("后缀sql=="+sql);
		
		Map<String, Object> commonQueryService = this.commonQueryService.commonQueryService( start , pageSize+"" , null , null , "v_insp_question" , null , sql);
		
		log.info("退出 getRoutineinspectionQuestionListMap");
		return commonQueryService;
	}
	
	/**
	 * 根据toId关联查找关联的问题（任务单关联的问题）
	 * 这是当巡检任务的状态为非“已关闭”的状态下调用
	 * @param handler
	 * @return
	 */
	public Set<Map> getRoutineinspectionQuestionListMapByToId(String toId){
		log.info("进入 getRoutineinspectionQuestionListMapByToId");
		log.info("getRoutineinspectionQuestionListMapByToId Service层 根据toId关联查找关联的问题。");
		if(toId==null||"".equals(toId)){
			log.error("in getRoutineinspectionQuestionListMapByToId 参数 toId为空");
			throw new UserDefinedException("in getRoutineinspectionQuestionListMapByToId 参数 toId为空");
		}
		List<Map> questionList = new ArrayList<Map>();
		Set<Map> set = new HashSet<Map>();
		//获取任务单关联的问题
		questionList = routineinspectionQuestionDao.queryRoutineinspectionQuestionListMapByToId(toId);
		
		if(questionList==null||questionList.isEmpty()){
			log.debug("调用 getRoutineinspectionQuestionListMapByOrg 获取 资源关联的巡检问题为空");
			return null;
		}else{
			for(Map map : questionList){
				map.put("createTime", TimeFormatHelper.getTimeFormatBySecond(map.get("createTime")));
				map.put("handleTime", TimeFormatHelper.getTimeFormatBySecond(map.get("handleTime")));
				map.put("toId", toId);
			}
			set.addAll(questionList);
		}
		log.info("退出 getRoutineinspectionQuestionListMapByToId");
		return set;
	}
	
	/**
	 * 根据主键获取巡检问题详细信息
	 * 点击问题点标题，展示问题点的详细信息
	 * @param id
	 * @return
	 */
	public Map getRoutineinspectionQuestionInfoById(String id){
		log.info("进入 getRoutineinspectionQuestionInfoById");
		log.info("getRoutineinspectionQuestionInfoById Service层  根据巡检任务单号获取相关巡检问题列表。");
		log.info("参数 巡检任务主键 id = "+id);
		List<Map> list = new ArrayList<Map>();
		if(id!=null&&!"".equals(id)){
			list = routineinspectionQuestionDao.queryRoutineinspectionQuestionListMap("id", id);
			if(list==null||list.isEmpty()){
				log.debug("根据巡检任务单号="+id+"获取相关巡检问题列表为空");
			}
		}else{
			throw new UserDefinedException("in getRoutineinspectionQuestionInfoById 参数 id为空");
		}
		
		log.info("退出 queryRoutineinspectionQuestionListByToId");
		return list.get(0);
	}
	
	/**
	 * 根据主键获取巡检问题
	 * @param id
	 * @return
	 */
	public RoutineinspectionQuestion getRoutineinspectionQuestionById(String id){
		log.info("进入 getRoutineinspectionQuestionById");
		log.info("getRoutineinspectionQuestionById Service层  根据巡检任务单号获取相关巡检问题列表。");
		log.info("参数 巡检任务主键 id = "+id);
		List<RoutineinspectionQuestion> list = new ArrayList<RoutineinspectionQuestion>();
		if(id!=null&&!"".equals(id)){
			list = routineinspectionQuestionDao.queryRoutineinspectionQuestionListById(Long.parseLong(id));
			if(list==null||list.isEmpty()){
				log.debug("根据巡检任务单号="+id+"获取相关巡检问题列表为空");
			}
		}else{
			throw new UserDefinedException("in getRoutineinspectionQuestionById 参数 id为空");
		}
		
		log.info("退出 getRoutineinspectionQuestionById");
		return list.get(0);
	}

	public RoutineinspectionQuestionDao getRoutineinspectionQuestionDao() {
		return routineinspectionQuestionDao;
	}

	public void setRoutineinspectionQuestionDao(
			RoutineinspectionQuestionDao routineinspectionQuestionDao) {
		this.routineinspectionQuestionDao = routineinspectionQuestionDao;
	}

	public RoutineInspectionTaskService getRoutineInspectionTaskService() {
		return routineInspectionTaskService;
	}

	public void setRoutineInspectionTaskService(
			RoutineInspectionTaskService routineInspectionTaskService) {
		this.routineInspectionTaskService = routineInspectionTaskService;
	}

	

	public CommonQueryService getCommonQueryService() {
		return commonQueryService;
	}

	public void setCommonQueryService(CommonQueryService commonQueryService) {
		this.commonQueryService = commonQueryService;
	}
	
	

}
