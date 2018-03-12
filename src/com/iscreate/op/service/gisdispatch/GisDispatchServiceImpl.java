package com.iscreate.op.service.gisdispatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.informationmanage.common.StringUtil;
import com.iscreate.op.constant.BizAuthorityConstant;
import com.iscreate.op.constant.GisDispatchGraphConstant;
import com.iscreate.op.constant.OrganizationConstant;
import com.iscreate.op.constant.WorkManageConstant;
import com.iscreate.op.dao.cardispatch.CardispatchCarDao;
import com.iscreate.op.dao.gisdispatch.GisDispatchDao;
import com.iscreate.op.dao.networkresourcemanage.NetworkResourceQueryDao;
import com.iscreate.op.dao.system.SysOrgUserDao;
import com.iscreate.op.dao.system.SysOrganizationDao;
import com.iscreate.op.pojo.gisdispatch.BasicPicUnit;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_ActiveArea;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_GraphElementType;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_GraphElementTypeAndMileSetting;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_GraphElementTypeSetting;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_GraphLayer;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_GraphLayerSetting;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_LittleIcon;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_LittleIconSetting;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_UserWork;
import com.iscreate.op.pojo.gisdispatch.LatLng;
import com.iscreate.op.pojo.gisdispatch.LatLngBounds;
import com.iscreate.op.pojo.gisdispatch.LittleIcon;
import com.iscreate.op.pojo.gisdispatch.PicLayer;
import com.iscreate.op.pojo.gisdispatch.PicLayerManager;
import com.iscreate.op.pojo.gisdispatch.PicUnitType;
import com.iscreate.op.pojo.gisdispatch.PicUnitTypeMile;
import com.iscreate.op.pojo.gisdispatch.TaskDetailInfo;
import com.iscreate.op.pojo.gisdispatch.TaskInfo;
import com.iscreate.op.pojo.gisdispatch.UserConfigTree;
import com.iscreate.op.pojo.gisdispatch.UserLayerSetting;
import com.iscreate.op.pojo.gisdispatch.UserLayerTypeSetting;
import com.iscreate.op.pojo.gisdispatch.UserLittleIconSetting;
import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;
import com.iscreate.op.pojo.system.SysOrg;
import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.op.pojo.system.SysRole;
import com.iscreate.op.pojo.workmanage.WorkmanageCountWorkorderObject;
import com.iscreate.op.pojo.workmanage.WorkmanageWorkorder;
import com.iscreate.op.service.networkresourcemanage.StaffOrganizationService;
import com.iscreate.op.service.publicinterface.NetworkResourceInterfaceService;
import com.iscreate.op.service.publicinterface.SessionService;
import com.iscreate.op.service.system.SysOrgUserService;
import com.iscreate.op.service.system.SysOrganizationService;
import com.iscreate.op.service.system.SysRoleService;
import com.iscreate.plat.location.pojo.PdaGpsLocation;
import com.iscreate.plat.login.constant.UserInfo;
import com.iscreate.plat.networkresource.common.dao.NetworkResourceManageDao;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.tools.GisDispatchJSONTools;
import com.iscreate.plat.tools.LatLngHelper;
import com.iscreate.plat.tools.TimeFormatHelper;


/**
 * @author che.yd 
 * Gis调度接口Service
 */
public class GisDispatchServiceImpl implements GisDispatchService {

	private GisDaiFactory gisDaiFactory;

	private final static String TABLE_GP_SETTING = "gisdispatch_graphlayersetting";
	private final static String TABLE_GETYPE_SETTING = "gisdispatch_graphelementtypesetting";
	private final static String TABLE_FOOTICON_SETTING = "gisdispatch_littleiconsetting";
	
	private String moduleWorkFlag = "GISDISPATCH";	//默认是获取左边数的工作列表
	
	private final static String STATION_FLAG="Station";
	
	private HibernateTemplate hibernateTemplate;
	
	private GisDispatchDao gisDispatchDao;
	
	private CardispatchCarDao cardispatchCarDao;
	
	private NetworkResourceQueryDao networkResourceQueryDao;
	
	private SysOrganizationDao sysOrganizationDao; //du.hw添加
	
	private List<Map<String, String>> userAreaIdList; 
	
	static Collection<String> userOrgIds;
	
	static List<WorkmanageCountWorkorderObject> stationWorkOrderList;

	private SysOrgUserDao sysOrgUserDao;
	
	private SysRoleService sysRoleService;
	
	private SysOrgUserService sysOrgUserService;
	
	private SysOrganizationService sysOrganizationService;//组织service yuan.yw
	

	private  StaffOrganizationService staffOrganizationService;
	
	
	private NetworkResourceManageDao networkResourceManageDao;

	public StaffOrganizationService getStaffOrganizationService() {
		return staffOrganizationService;
	}

	public void setStaffOrganizationService(
			StaffOrganizationService staffOrganizationService) {
		this.staffOrganizationService = staffOrganizationService;
	}

	public SysOrganizationService getSysOrganizationService() {
		return sysOrganizationService;
	}

	public void setSysOrganizationService(
			SysOrganizationService sysOrganizationService) {
		this.sysOrganizationService = sysOrganizationService;
	}
	
	
	public SysOrgUserService getSysOrgUserService() {
		return sysOrgUserService;
	}

	public void setSysOrgUserService(SysOrgUserService sysOrgUserService) {
		this.sysOrgUserService = sysOrgUserService;
	}

	public SysRoleService getSysRoleService() {
		return sysRoleService;
	}

	public void setSysRoleService(SysRoleService sysRoleService) {
		this.sysRoleService = sysRoleService;
	}

	public SysOrgUserDao getSysOrgUserDao() {
		return sysOrgUserDao;
	}

	public void setSysOrgUserDao(SysOrgUserDao sysOrgUserDao) {
		this.sysOrgUserDao = sysOrgUserDao;
	}

	public SysOrganizationDao getSysOrganizationDao() {
		return sysOrganizationDao;
	}

	public void setSysOrganizationDao(SysOrganizationDao sysOrganizationDao) {
		this.sysOrganizationDao = sysOrganizationDao;
	}

	public void setGisDaiFactory(GisDaiFactory gisDaiFactory) {
		this.gisDaiFactory = gisDaiFactory;
	}

	public GisDaiFactory getGisDaiFactory() {
		return gisDaiFactory;
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	public GisDispatchDao getGisDispatchDao() {
		return gisDispatchDao;
	}

	public void setGisDispatchDao(GisDispatchDao gisDispatchDao) {
		this.gisDispatchDao = gisDispatchDao;
	}
   

	public NetworkResourceQueryDao getNetworkResourceQueryDao() {
		return networkResourceQueryDao;
	}

	public void setNetworkResourceQueryDao(
			NetworkResourceQueryDao networkResourceQueryDao) {
		this.networkResourceQueryDao = networkResourceQueryDao;
	}

	public CardispatchCarDao getCardispatchCarDao() {
		return cardispatchCarDao;
	}

	public void setCardispatchCarDao(CardispatchCarDao cardispatchCarDao) {
		this.cardispatchCarDao = cardispatchCarDao;
	}

	/**
	 * 获取用户的工单列表
	 * @param userId
	 * @param bizTypeCode 业务类型，如urgentrepair（抢修）、cardispatch（车辆调度）、resourcedispatch（物资调度）
	 * @param workOrderType
	 * @param paramMap 参数Map
	 * @return
	 */
	public List<Map<String, Object>> getUserWorkOrdersService(String userId,String bizTypeCode,String workOrderType,int pageIndex,int pageSize,Map paramMap){
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			List<Map> workOrderList = null;	//工单列表
			String totalCount = "0";		//总记录行数
			String conditionStr = "";
			if(paramMap!=null){
				String woId = paramMap.get("woId")+"";
				if(!StringUtil.isNullOrEmpty(woId)){
					conditionStr+=" and woId like '%"+woId+"%'";
				}
				String woTitle = paramMap.get("woTitle")+"";
				if(!StringUtil.isNullOrEmpty(woTitle)){
					conditionStr+=" and woTitle like '%"+woTitle+"%'";
				}
				String woState = paramMap.get("woState")+"";
				if(!StringUtil.isNullOrEmpty(woState)){
					conditionStr+=" and status="+woState;
				}
			}
			//获取用户工单列表
			Map<String, Object> userWorkOrders = gisDaiFactory.getWorkManageService().getUserWorkOrders(userId, bizTypeCode, workOrderType, pageIndex, pageSize,conditionStr);
			if(userWorkOrders!=null){
				workOrderList = (List<Map>) userWorkOrders.get("entityList");
				totalCount = userWorkOrders.get("count")+"";
			}
			if(workOrderList!=null && !workOrderList.isEmpty()){
				for (Map map : workOrderList) {
					String WOID = (String)map.get("woId");
					if(bizTypeCode.toLowerCase().equals("cardispatch")){
						String useCarPersonId = (String)map.get("useCarPersonId");
						map.put("resourceId", useCarPersonId);
						map.put("resourceType", "human");
					}else if(bizTypeCode.toLowerCase().equals("urgentrepair")){
						//获取工单关联的站址Id
						List<Workorderassnetresource> resList = gisDaiFactory.getWorkOrderAssnetResourceDao().getWorkOrderAssnetResourceRecordDao("woId", WOID);
						if(resList!=null && !resList.isEmpty()){
							for (Workorderassnetresource res : resList) {
								Long resourceId = res.getStationId();
								String resourceType = res.getNetworkResourceType();
								map.put("resourceId", resourceId);
								map.put("resourceType", resourceType);
							}
						}
					}
					map.put("totalCount",totalCount);
					//日期转换
					String createTime = map.get("createTime")+"";
					if(createTime!=null && !"".equals(createTime)){
						createTime = TimeFormatHelper.getTimeFormatByFree(createTime, "yyyy-MM-dd HH:mm:ss");
						map.put("createTime", createTime);
					}
					String requireCompleteTime = map.get("requireCompleteTime")+"";
					if(requireCompleteTime!=null && !"".equals(requireCompleteTime)){
						requireCompleteTime = TimeFormatHelper.getTimeFormatByFree(requireCompleteTime, "yyyy-MM-dd HH:mm:ss");
						map.put("requireCompleteTime", requireCompleteTime);
					}
					resultList.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	
	
	/**
	 * 获取用户的任务单列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserTaskOrdersService(String userId,String bizTypeCode,String taskOrderType,int pageIndex,int pageSize,Map paramMap) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			List<Map> taskOrderList = null;
			String totalCount = "";	
			String conditionStr = "";
			if(paramMap!=null){
				String toId = paramMap.get("toId")+"";
				if(!StringUtil.isNullOrEmpty(toId)){
					conditionStr+=" and toId like '%"+toId+"%'";
				}
				String toTitle = paramMap.get("toTitle")+"";
				if(!StringUtil.isNullOrEmpty(toTitle)){
					conditionStr+=" and toTitle like '%"+toTitle+"%'";
				}
				String toState = paramMap.get("toState")+"";
				if(!StringUtil.isNullOrEmpty(toState)){
					conditionStr+=" and status="+toState;
				}
			}
			//获取用户任务单列表
			Map<String, Object> userTaskOrders = gisDaiFactory.getWorkManageService().getUserTaskOrders(userId, bizTypeCode, taskOrderType, pageIndex, pageSize,conditionStr);
			if(userTaskOrders!=null){
				taskOrderList = (List<Map>) userTaskOrders.get("entityList");
				totalCount = userTaskOrders.get("count")+"";
			}
			if(taskOrderList!=null && !taskOrderList.isEmpty()){
				for (Map map : taskOrderList) {
					String WOID = (String) map.get("woId");
					if(WOID!=null && !"".equals(WOID)){
						//获取工单关联的站址Id
						List<Workorderassnetresource> resList = gisDaiFactory.getWorkOrderAssnetResourceDao().getWorkOrderAssnetResourceRecordDao("woId", WOID);
						if(resList!=null && !resList.isEmpty()){
							for (Workorderassnetresource res : resList) {
								Long resourceId = res.getStationId();
								String resourceType = res.getNetworkResourceType();
								map.put("resourceId", resourceId);
								map.put("resourceType", resourceType);
							}
						}
					}
					map.put("totalCount", totalCount);
					//日期转换
					String assignTime = (String)map.get("assignTime");
					if(assignTime!=null && !"".equals(assignTime)){
						assignTime = TimeFormatHelper.getTimeFormatByFree(assignTime, "yyyy-MM-dd HH:mm:ss");
						map.put("assignTime", assignTime);
					}
					String requireCompleteTime = (String)map.get("requireCompleteTime");
					if(requireCompleteTime!=null && !"".equals(requireCompleteTime)){
						requireCompleteTime = TimeFormatHelper.getTimeFormatByFree(requireCompleteTime, "yyyy-MM-dd HH:mm:ss");
						map.put("requireCompleteTime", requireCompleteTime);
					}
					resultList.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;

	}

	
	
	/**
	 * 获取工单关联的资源
	 * 
	 * @param WOID
	 * @return
	 */
	public List<Workorderassnetresource> getWorkOrderAssResourcesService(String WOID) {
		List<Workorderassnetresource> resList = null;
		try {
			if (WOID == null || "".equals(WOID)) {
				return null;
			}
			resList =  gisDaiFactory.getWorkOrderAssnetResourceDao().getWorkOrderAssnetResourceRecordDao("woId",WOID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resList;
	}

	/**
	 * 获取任务单关联的资源列表
	 * 
	 * @param TOID
	 * @return
	 */
	public List<Workorderassnetresource> getTaskOrderAssResourcesService(String TOID) {
		//TODO 获取任务单关联资源列表
		return null;
	}

	/**
	 * 获取资源关联的任务数
	 * 
	 * @param resource
	 *            资源：人,车,站址,管井
	 * @return
	 */
	public int getTaskCountOfResourceService(Map resource) {
		int totalCount = 0;
		try {
			if (resource == null) {
				return totalCount;
			}
			String resourceId = resource.get("resourceId")+"";
			String resourceType = resource.get("resourceType")+"";
			totalCount = (int)gisDaiFactory.getWorkManageService().getTaskOrderCountByObjectTypeAndObjectId(resourceType, resourceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalCount;
	}

	/**
	 * 获取资源关联的任务数
	 * 
	 * @param resourceType
	 *            资源类型
	 * @param resourceId
	 *            资源id
	 * @return
	 */
	public int getTaskCountOfResourceService(String resourceType, String resourceId) {
		int totalCount = 0;
		try {
			totalCount = (int)gisDaiFactory.getWorkManageService().getTaskOrderCountByObjectTypeAndObjectId(resourceType, resourceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalCount;
	}

	private void getSubArea(String areaId){
		List<Map<String, String>> subAreaList = gisDaiFactory.getNetworkResourceService().getResourceService(areaId, "Sys_Area", "Sys_Area", "CHILD");
		if(subAreaList != null && !subAreaList.isEmpty()){
			userAreaIdList.addAll(subAreaList);
			for (Map<String, String> map : subAreaList) {
				String subAreaId = map.get("id");
				getSubArea(subAreaId);
			}
		}
	}
	/**
	 * 填充某个图层
	 * 
	 * @param userId
	 *            用户id
	 * @param picLayer
	 *            图层
	 */
	public void fillPicLayerService(String userId, PicLayer picLayer) {
		try {
			// 填充图层的意思就是根据图层的‘包含的图元类型’，查询该图元类型对应的实例数据，然后塞进‘可见图元列表’
			//System.out.println(picLayer.getName());
			List<PicUnitType> puTypeList = picLayer.getPicUnitTypes();
			
			//临时方案(du.hw添加)
			long org_user_id =   Long.parseLong(ServletActionContext.getRequest().getSession().getAttribute(UserInfo.ORG_USER_ID).toString());
			
			Map<String, BasicPicUnit> visiblePicUnitMap = picLayer.getVisiblePicUnitMap();// 可见图元列表
			Map<String, BasicPicUnit> hiddenPicUnitMap = picLayer.getHiddenPicUnitMap();// 隐藏图元列表
			Map<String, BasicPicUnit> allPicUnitMap = picLayer.getAllPicUnitMap();//所有图元列表
			
			LatLngBounds latLngBounds = picLayer.getPicLayerManager().getBackgroundLatLngRange();// 背景窗口经纬度
			
			if (puTypeList != null && !puTypeList.isEmpty()) {
				for (PicUnitType tempType : puTypeList) {
					
					String typeName = tempType.getCode();
					//System.out.println("1--------typeName:"+typeName);
					List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
					
					if (typeName.equals(GisDispatchGraphConstant.RESOURCE_STATION)) {// 站址类型
						List<Long> user_area_list = new ArrayList<Long>();
						List<BasicEntity> areaByUserId = this.staffOrganizationService.getAreaByUserId(userId);
						for (int i = 0; areaByUserId != null && i < areaByUserId.size(); i++) {
							BasicEntity basicEntity = areaByUserId.get(i);
							Map<String, Object> map = basicEntity.toMap();
							long parseLong = Long.parseLong(map.get("id")+"");
							user_area_list.add(parseLong);
						}
						List<BasicEntity> resourceByResourceType = this.networkResourceManageDao.getResourceByResourceType(user_area_list, "Sys_Area", "Station");
						List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
						if(resourceByResourceType != null && resourceByResourceType.size() > 0){
							for(BasicEntity be:resourceByResourceType){
								list.add(be.toMap());
							}
							resultList = list;
						}
//						resultList = networkResourceQueryDao.getResourceListByUserId(org_user_id, "Station");
						/* 
						System.out.println("开始获取站址类型:");
						userAreaIdList = new ArrayList<Map<String,String>>();	//用户区域Id List
						//获取内存中的站址数据Map
						Map<Long, List<Map<String, Object>>> opsStationMap = StationQueryTimerService.opsStationMap;
						List<Map> resourceList=new ArrayList<Map>();
						//获取用户区域
						List<Map<String, String>> areaList = gisDaiFactory.getNetworkResourceService().getAreaByAccountService(userId);
						System.out.println(userId);
						System.out.println(areaList);
						if(areaList!=null && !areaList.isEmpty()){
							List<String> areaIdList = new ArrayList<String>();
							for (Map<String, String> map : areaList) {
								String areaId = (String)map.get("id");
								//递归获取子级区域
								getSubArea(areaId);
								//增加本区域关注次数
								this.addAreaActiveNumber(Long.valueOf(areaId));
							}
							//保存用户区域Id
							userAreaIdList.addAll(areaList);
							System.out.println(userAreaIdList.size());
							System.out.println("userAreaIdList:"+userAreaIdList);
							for (Map<String, String> map : userAreaIdList) {
								String areaId = map.get("id");
								if(opsStationMap!=null){
									List<Map<String, Object>> stationList = opsStationMap.get(Long.valueOf(areaId));
									if(stationList==null){
										//获取不到内存中该区域站址数据，则调用接口获取
										List<Map<String, String>> rsList = gisDaiFactory.getNetworkResourceService().getResourceService(areaId, "Sys_Area", "Station", "CHILD");
										if(rsList!=null && !rsList.isEmpty()){
											for (Map be : rsList) {
												//modifyCode,label去掉，改用id标识
												be.put("objectIdentity", be.get("id"));
												be.put("objectName", be.get("name"));
												resultList.add(be);
											}
										}
									}else{
										resultList.addAll(stationList);
									}
								}else{
									//获取不到内存中该区域站址数据，则调用接口获取
									List<Map<String, String>> rsList = gisDaiFactory.getNetworkResourceService().getResourceService(areaId, "Sys_Area", "Station", "CHILD");
									if(rsList!=null && !rsList.isEmpty()){
										for (Map be : rsList) {
											//modifyCode,label去掉，改用id标识
											be.put("objectIdentity", be.get("id"));
											be.put("objectName", be.get("name"));
											resultList.add(be);
										}
									}
								}
							}
							
						}
						System.out.println("resultList:"+resultList);
						System.out.println("resultList:"+resultList.size());
						System.out.println("结束获取站址类型:");
					*/} else if (typeName.equals(GisDispatchGraphConstant.RESOURCE_MANHOLE)) { // 人手井
//						String queryResourceType = "Manhole";
//						//TODO 获取人手井信息
						
					} else if (typeName.equals(GisDispatchGraphConstant.RESOURCE_HUMAN)) { // 人员
						/*List<ProviderOrganization> teamList = new ArrayList<ProviderOrganization>();
						//获取当前用户最高组织架构
						List<ProviderOrganization> blist = gisDaiFactory.getProviderOrganizationService().getOrgByAccountService(userId);
						if(blist!=null && !blist.isEmpty()){
							teamList.addAll(blist);
						}
						ProviderOrganizationService providerOrganizationService = gisDaiFactory.getProviderOrganizationService();
						//获取用户所能看到的最高的组织
						Map<String,String> allStaffs=new HashMap<String,String>();
						if (teamList != null && teamList.size() > 0) {
							for (ProviderOrganization org : teamList) {
								//---- 更改为只获取 角色 为 队长 的人员
								List<Staff> accounts = providerOrganizationService.getStaffListDownwardByRoleAndOrg(org.getId(), BizAuthorityConstant.ROLE_TRAMLEADER);
								if (accounts != null && accounts.size() > 0) {
									for (Staff staff : accounts) {
										if (!allStaffs.containsKey(staff.getAccount())) {
											Map<String, Object> humanMap = new HashMap<String, Object>();
											//获取人员信息(经纬度、姓名，Id)
											String accountId=staff.getAccount();
											String staffCnName = staff.getName();
											if(accountId!=null && !accountId.equals("")){
												PdaGpsLocation gpsLocation = gisDaiFactory.getPdaGpsService().getLastGpsLocationByUserIdService(accountId);
												double lat = 0.0;
												double lng = 0.0;
												if(gpsLocation!=null){
													lat = gpsLocation.getLatitude();
													lng = gpsLocation.getLongitude();
												}
												humanMap.put("longitude", lng);
												humanMap.put("latitude", lat);
											}
											humanMap.put("objectIdentity", accountId);
											humanMap.put("objectName", staffCnName);
											resultList.add(humanMap);
											allStaffs.put(accountId, staffCnName);
										}
									}
								}
							}
						}*/
						List<Long> orgIds = new ArrayList<Long>();
						if(userOrgIds != null){
							for(String d:userOrgIds){
								orgIds.add(Long.parseLong(d));
							}
						}
						//ou.jh
						List<Map<String,Object>> userAndLastCoordinatesByOrgIdSAndRoleCode = this.sysOrgUserDao.getUserAndLastCoordinatesByOrgIdSAndRoleCode(orgIds, BizAuthorityConstant.ROLE_TRAMLEADER);
//						List<Map> accountList = gisDaiFactory.getProviderOrganizationService().getAccountInfoByOrgIdAndRoleType(userOrgIds, BizAuthorityConstant.ROLE_TRAMLEADER);
						if(userAndLastCoordinatesByOrgIdSAndRoleCode!=null && !userAndLastCoordinatesByOrgIdSAndRoleCode.isEmpty()){
							for(Map<String,Object> m:userAndLastCoordinatesByOrgIdSAndRoleCode){
								m.put("userId", m.get("account"));
							}
							resultList.addAll((Collection<? extends Map<String, Object>>)userAndLastCoordinatesByOrgIdSAndRoleCode);
						}
					} else if (typeName.equals(GisDispatchGraphConstant.RESOURCE_MAINTAINGROUP)) { // 维护组
						// TODO 获取维护组信息
						
					} else if (typeName.equals(GisDispatchGraphConstant.RESOURCE_CAR)) { // 车辆
						//获取组织架构下的车辆
						//System.out.println("start car");
						//List<Map<String, Object>> carList = gisDaiFactory.getCardispatchManageService().findCarListWithTopGpsAndStateByOrgIds(userOrgIds);
						//System.out.println(carList);
						//System.out.println(carList.size());
						
						List<Map<String, Object>> carList = cardispatchCarDao.getCarListByUserId(org_user_id);
						if(carList!=null && !carList.isEmpty()){
							resultList.addAll(carList);
						}
					}else if ((typeName.equals(GisDispatchGraphConstant.RESOURCE_QUESTIONSPOT)) || 
							(typeName.equals(GisDispatchGraphConstant.RESOURCE_HIDDENTROUBLESPOT)) ||
							(typeName.equals(GisDispatchGraphConstant.RESOURCE_FAULTSPOT)) ||
							(typeName.equals(GisDispatchGraphConstant.RESOURCE_WATCHSPOT)) ) {	//问题点 or 隐患点   or 故障点  or 盯防点
							//全部为空
							
					}else if ((typeName.equals(GisDispatchGraphConstant.RESOURCE_HEADERQUARTER)) || 
							(typeName.equals(GisDispatchGraphConstant.RESOURCE_SHIYEBU)) ||
							(typeName.equals(GisDispatchGraphConstant.RESOURCE_PROJECTGROUP)) ||
							(typeName.equals(GisDispatchGraphConstant.RESOURCE_MAINTAINTEAMAAREADDRESS))) { // 组织架构
						String orgType = "";
						if(typeName.equals(GisDispatchGraphConstant.RESOURCE_SHIYEBU)){	//事业部
							orgType = OrganizationConstant.ORGANIZATION_BUSINESSDIVISION;
						}else if(typeName.equals(GisDispatchGraphConstant.RESOURCE_MAINTAINTEAMAAREADDRESS)){	//维护片区
							orgType = OrganizationConstant.ORGANIZATION_MAINTENANCEAREA;
						}else if(typeName.equals(GisDispatchGraphConstant.RESOURCE_HEADERQUARTER)){	//公司总部
							orgType = OrganizationConstant.ORGANIZATION_COMPANY;
						}else if(typeName.equals(GisDispatchGraphConstant.RESOURCE_PROJECTGROUP)){	//项目组驻地
							orgType = OrganizationConstant.ORGANIZATION_PROJECTTEAM;
						}
						//List<Map> accountList = gisDaiFactory.getProviderOrganizationService().getOrgInfoOrgTreeByOrgIdsAndOrgType(userOrgIds, orgType);
						//yuan.yw
						String orgIds = "";
						if(userOrgIds!=null && !userOrgIds.isEmpty()){
							for(String orgId:userOrgIds){
								orgIds += ","+orgId;
							}
						    if(orgIds != ""){
						    	orgIds = orgIds.substring(1);
						    }
						   
							List<Map<String, Object>> accountList = this.sysOrganizationDao.getOrgListDownwardOrUpwardByOrgTypeAndOrgId(orgIds, orgType, "self");
							if(accountList!=null && !accountList.isEmpty()){
								resultList.addAll((Collection<? extends Map<String, Object>>)accountList);
							}
						}
						
					}else if((typeName.equals(GisDispatchGraphConstant.RESOURCE_STOREHOUSE))){		//仓库
						//根据组织Id集合获取仓库信息
						
					}
					try {
						// 转换为对应BasicPicUnit格式
						if (resultList != null && !resultList.isEmpty()) {
							for (Map<String, Object> tempMap : resultList) {
								BasicPicUnit bp = new BasicPicUnit();
								String objectIdentity = tempMap.get("objectIdentity")+"";
								String objectName = tempMap.get("objectName")+"";
								if(objectIdentity==null || objectIdentity.equals("") || objectIdentity.equals("null")){
									if(typeName.equals(GisDispatchGraphConstant.RESOURCE_HUMAN)){
										objectIdentity =  tempMap.get("userId")+"";
									}else{
										objectIdentity = tempMap.get("id")+"";
									}
								}
								if(objectIdentity==null || objectIdentity.equals("") || objectIdentity.equals("null")){
									objectIdentity = tempMap.get("account")+"";
								}
								if(objectName==null || objectName.equals("") || objectName.equals("null")){
									objectName = tempMap.get("name")+"";
								}
								bp.setId(objectIdentity);
								bp.setName(objectName);
								bp.setIcon(tempType.getIcon()); //
								bp.setType(typeName);
								Object carState = tempMap.get("carState");
								if ( carState != null ) {
									bp.setIsOnDuty(""+carState);
								}
								
								LatLng ll = new LatLng();
								double lng =0;
								double lat =0;
								if(tempMap.get("longitude")!=null && !"".equals(tempMap.get("longitude").toString())){
									lng = Double.valueOf(tempMap.get("longitude").toString());
								}
								
								if(tempMap.get("latitude")!=null && !"".equals(tempMap.get("latitude").toString())){
									lat = Double.valueOf(tempMap.get("latitude").toString());
								}
								
								ll.setLongitude(lng);
								ll.setLatitude(lat);
								bp.setLatlng(ll);
								
								String key = bp.getKey();
								bp.setPicUnitType(tempType);
								
								//获取图元任务信息
								if(typeName.equals("_resource_station")){
									String address = tempMap.get("address")+"";
									address = address.replace("\n","");
									bp.setAddress(address);
									TaskInfo ti = this.getTaskInfoOfGraphElementService(bp, userId);
									bp.setTaskInfo(ti);
								}
								
								// 加进可见或不可见列表
								if (tempType.isNeedShow() && tempType.isGonglishuVisible()) {//用户设定该图元类型可见，并且该图元类型在可见公里数内
									if (bp.containedIn(latLngBounds)) {
										visiblePicUnitMap.put(key, bp);
									} else {
										hiddenPicUnitMap.put(key, bp);
									}
								}else{
									hiddenPicUnitMap.put(key, bp);
								}
								
								bp.setPicUnitType(tempType);
								
								//System.out.println(key);
								//System.out.println(bp.toJson());
								//保存进所有图元列表Map
								allPicUnitMap.put(key, bp);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				picLayer.setAllPicUnitMap(allPicUnitMap);
				picLayer.setVisiblePicUnitMap(visiblePicUnitMap);
				picLayer.setHiddenPicUnitMap(hiddenPicUnitMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取图元的任务信息：任务总数、任务类型
	 * 
	 * @param bpu 图元
	 */
	public TaskInfo getTaskInfoOfGraphElementService(BasicPicUnit bpu,String userId) {
		TaskInfo taskInfo = new TaskInfo();
		int totalCount = 0;
		try {
			if (bpu == null) {
				return null;
			}
			String typeName = bpu.getType(); // 样式:_resource_station
			
			if(GisDispatchGraphConstant.RESOURCE_STATION.equals(typeName)){	//站址：获取工单数
				long workOrderCount = 0;
				if(stationWorkOrderList!=null && !stationWorkOrderList.isEmpty()){
					for (WorkmanageCountWorkorderObject wc : stationWorkOrderList) {
						if(wc.getResourceId().equals(bpu.getId())){
							workOrderCount = wc.getTaskCount();
							break;
						}
					}
				}
				//TODO 阿达接口，巡检、抢修 共单数
				int qiangxiuNum = 0;// 抢修任务总数
				int xunjianNum = 0;// 巡检任务总数
				int xiushanNum = 0;// 修缮任务总数
				totalCount = (int) workOrderCount;
				qiangxiuNum = (int) workOrderCount;
//				xunjianNum = (int) workOrderCount;
//				xiushanNum = (int) workOrderCount;
				
				// 设置站址脚标相关
				if (bpu.getType().equals(GisDispatchGraphConstant.RESOURCE_STATION)) {
					if (bpu.getPicUnitType() != null) {
						//创建子任务列表
						List<TaskDetailInfo> taskDetailInfos=new ArrayList<TaskDetailInfo>();
						//或取用户关于该脚标的设置情况，如是否显示
						Map<Long, UserLittleIconSetting> userLittleIconSettings=bpu.getPicUnitType().getPicLayer().getPicLayerManager().getUserLittleIconSettings();
						List<LittleIcon> icons = bpu.getPicUnitType().getLittleIcons();
						if (icons != null && icons.size() > 0) {
							for (LittleIcon icon : icons) {//对于该bpu的每一个脚标，都设置一个相应的任务详情
								UserLittleIconSetting ulis=userLittleIconSettings.get(icon.getId());
								TaskDetailInfo tdi = new TaskDetailInfo();
								tdi.setName(icon.getName());
								tdi.setLittleIcon(icon.getIcon());
								if(icon.getName().equals(GisDispatchGraphConstant.LITTLEICON_QIANGXIU)){
									tdi.setCount(qiangxiuNum);
								}else if(icon.getName().equals(GisDispatchGraphConstant.LITTLEICON_XUNJIAN)){
									tdi.setCount(xunjianNum);
								}else if(icon.getName().equals(GisDispatchGraphConstant.LITTLEICON_XIUSHAN)){
									tdi.setCount(xiushanNum);
								}
								
								tdi.setNeedShowLittleIcon(ulis.isShowOrNot());//用户是否设置了显示
								taskDetailInfos.add(tdi);
							}
						}
						//加进任务总数里
						taskInfo.setTaskDetailInfo(taskDetailInfos);
					}
				}
			}else if((GisDispatchGraphConstant.RESOURCE_HUMAN.equals(typeName))){	//人员：任务单/工单数
				long taskOrderCount = gisDaiFactory.getWorkManageService().getTaskOrderCountByObjectTypeAndObjectId("people", bpu.getId());
				totalCount = (int)taskOrderCount;
				//更新人员经纬度
				PdaGpsLocation gps = this.gisDaiFactory.getPdaGpsService().getLastGpsLocationByUserIdService(bpu.getId());
				double lat = 0.0;
				double lng = 0.0;
				if(gps!=null){
					lat = gps.getLatitude();
					lng = gps.getLongitude();
				}
				bpu.setLatlng(new LatLng(lat,lng));
			}else if(GisDispatchGraphConstant.RESOURCE_CAR.equals(typeName)){	//车辆：工单数
				long workOrderCount = gisDaiFactory.getWorkManageService().getWorkOrderCountByResourceTypeAndResourceId("car", bpu.getId());
				totalCount = (int)workOrderCount;
				
			}else if((typeName.equals(GisDispatchGraphConstant.RESOURCE_HEADERQUARTER)) || 
					(typeName.equals(GisDispatchGraphConstant.RESOURCE_SHIYEBU)) ||
					(typeName.equals(GisDispatchGraphConstant.RESOURCE_PROJECTGROUP)) ||
					(typeName.equals(GisDispatchGraphConstant.RESOURCE_MAINTAINTEAMAAREADDRESS))){	//组织驻地：人数
					String bTime = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM");
					bTime+="-01 00:00:00";
					String eTime = TimeFormatHelper.getTimeFormatByFree(new Date(), "yyyy-MM");
					Calendar cal = Calendar.getInstance();
					int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
					
					eTime+="-"+maxDay+" 23:59:59";
					List<Map> scList = gisDaiFactory.getOrgStaffCountMonthReportService().getOrgStaffCountByOrgIdService(Long.valueOf(bpu.getId()), bTime, eTime);
					if(scList!=null && !scList.isEmpty()){
						Map map = scList.get(0);
						String osCount = map.get("staffCount")+"";
						totalCount = Integer.valueOf(osCount);
					}
			}
			taskInfo.setTotalTaskCount(totalCount);
			//bpu.setTaskInfo(taskInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return taskInfo;
	}

	/**
	 * 获取某个图元关联的工单、任务单列表
	 * @param bpu 图元
	 * @return 工单、任务单列表
	 */
	public Map<String, List<Map<String,Object>>> getWorkOrderAndTaskOrderOfGraphElementService(
			BasicPicUnit bpu,String userId) {
		List<Map<String,Object>> ownWorkOrderList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> notOwnWorkOrderList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> ownTaskOrderList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> notOwnTaskOrderList = new ArrayList<Map<String,Object>>();
		Map<String, List<Map<String,Object>>> resultMap = new HashMap<String, List<Map<String,Object>>>();
		try {
			if (bpu == null) {
				return null;
			}
			String bpuType = bpu.getType(); // 样式:_resource_station
			int _Index = bpuType.lastIndexOf("_");
			String lowerType = bpuType.substring(_Index + 1);
			String typeName = lowerType.substring(0, 1).toUpperCase()
					+ lowerType.substring(1);
			
			//获取站址 工单
			if(GisDispatchGraphConstant.RESOURCE_STATION.equals(bpuType)){
				//TODO 站址获取的是已经受理过的未结束的工单
				List<Map> workOrderList = gisDaiFactory.getWorkManageService().getWorkOrderListByResourceTypeAndResourceId(typeName, bpu.getId(), "urgentrepair", "pendingWorkOrder",null);
				if(workOrderList!=null && !workOrderList.isEmpty()){
					for (Map map : workOrderList) {
						String currentHandler = (String)map.get("currentHandler");
						if(currentHandler.equals(userId)){
							ownWorkOrderList.add(map);
						}else{
							notOwnWorkOrderList.add(map);
						}
					}
				}
				
			}else if(GisDispatchGraphConstant.RESOURCE_CAR.equals(bpuType)){
				//车辆获取的是未结束工单
				List<Map> workOrderList = gisDaiFactory.getWorkManageService().getWorkOrderListByResourceTypeAndResourceId("car", bpu.getId(), "cardispatch", "pendingWorkOrder", null);
				if(workOrderList!=null && !workOrderList.isEmpty()){
					for (Map map3 : workOrderList) {
						map3.put("formUrl", "cardispatch_lookupworkorder.jsp");
						ownTaskOrderList.add(map3);
					}
				}
			}else if(GisDispatchGraphConstant.RESOURCE_HUMAN.equals(bpuType)){
				//人获取的是待办车辆调度单
				String conditionString = " or(\"creator\"='"+bpu.getId()+"' and \"status\"<>"+WorkManageConstant.WORKORDER_END+" and \"status\"="+WorkManageConstant.WORKORDER_WAIT4ASSIGNCAR+")";
				Map<String, Object> userWorkOrders = gisDaiFactory.getWorkManageService().getUserWorkOrders(bpu.getId(), "cardispatch", "pendingWorkOrder", null, null,conditionString);
				if(userWorkOrders!=null){
					List<Map> workOrderList = (List<Map>)userWorkOrders.get("entityList");
					if(workOrderList!=null && !workOrderList.isEmpty()){
						for (Map map2 : workOrderList) {
							ownWorkOrderList.add(map2);
						}
					}
				}
			}
			resultMap.put("ownWorkOrderList", ownWorkOrderList);
			resultMap.put("notOwnWorkOrderList", notOwnWorkOrderList);
			resultMap.put("ownTaskOrderList", ownTaskOrderList);
			resultMap.put("notOwnTaskOrderList", notOwnTaskOrderList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 判断当前资源是否在经纬度的显示范围之内
	 * 
	 * @param targetPoint
	 * @param latLngBounds
	 * @return
	 */
	private boolean judgeResourceIsInLngLatBound(LatLng target,
			LatLngBounds latLngBounds) {
		boolean x = false;
		boolean y = false;

		// 获取西南
		LatLng sw = latLngBounds.getSw();
		// 获取东北
		LatLng ne = latLngBounds.getNe();
		if ((target.getLatitude() >= sw.getLatitude() && target.getLatitude() <= ne
				.getLatitude())
				|| (target.getLatitude() <= sw.getLatitude() && target
						.getLatitude() >= ne.getLatitude())) {
			x = true;
		}

		if ((target.getLongitude() >= sw.getLongitude() && target
				.getLongitude() <= ne.getLongitude())
				|| (target.getLongitude() <= sw.getLongitude() && target
						.getLongitude() >= ne.getLongitude())) {
			y = true;
		}
		if (x && y) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 保存用户设置的图层or图元or脚标 是否显示
	 * 
	 * @param userId
	 * @param targetId
	 * @param isShow
	 */
	public void savePicLayerConfigService(String userId, String targetType,
			long targetId, boolean isShow) {
		int isShowFlag = 0;
		if (isShow) {
			isShowFlag = 1;
		}
		try {
			if (userId == null || "".equals(userId)) {
				return;
			}
			if (targetType == null || "".equals(targetType)) {
				return;
			}
			if (targetId == 0) {
				return;
			}
			if (GisDispatchGraphConstant.GRAPHTYPE_LAYER.equals(targetType)) { // 图层
				// 先查找一下用户之前有没有配置图层信息
				List<GisDispatch_GraphLayerSetting> gpList  = null;
				String hql = "from GisDispatch_GraphLayerSetting where userId='"+userId+"' and gl_id="+targetId;
				gpList = hibernateTemplate.find(hql);
				
				if (gpList == null || gpList.size() == 0) { // 如果没有，就为当前用户复制默认用户的的设置记录
					getDefaultUserLayerConfig("default", userId,
							TABLE_GP_SETTING);
				}else{
					int glsId = gpList.get(0).getId();
					GisDispatch_GraphLayerSetting gls = new GisDispatch_GraphLayerSetting();
					gls.setId(glsId);
					gls.setShowOrNot(isShowFlag);
					gls.setUserId(userId);
					gls.setGl_id((int)targetId);
					hibernateTemplate.update(gls);
				}
			} else if (GisDispatchGraphConstant.GRAPHTYPE_ELEMENTTYPE.equals(targetType)) { // 图元类型
				// 先查找一下用户之前有没有配置图层信息
				String hql = "from GisDispatch_GraphElementTypeSetting where userId='"+userId+"' and geType_id="+targetId;
				List<GisDispatch_GraphElementTypeSetting> gpList = hibernateTemplate.find(hql);
				
				if (gpList == null || gpList.size() == 0) { // 如果没有，就为当前用户复制默认用户的的设置记录
					getDefaultUserGraphElementTypeConfig("default", userId,
							TABLE_GETYPE_SETTING);
				}else{
					int geId = gpList.get(0).getId();
					GisDispatch_GraphElementTypeSetting ge = new GisDispatch_GraphElementTypeSetting();
					ge.setId(geId);
					ge.setIsVisible(isShowFlag);
					ge.setUserId(userId);
					ge.setGeType_id((int)targetId);
					hibernateTemplate.update(ge);
				}
			} else if (GisDispatchGraphConstant.GRAPHTYPE_LITTLTICON.equals(targetType)) {// 图元脚标
				// 先查找一下用户之前有没有配置图元脚标信息
				String hql = "from GisDispatch_LittleIconSetting where userId='"+userId+"' and littleicon_id="+targetId;
				List<GisDispatch_LittleIconSetting> gpList = hibernateTemplate.find(hql);
				
				if (gpList == null || gpList.size() == 0) { // 如果没有，就为当前用户复制默认用户的的设置记录
					getDefaultUserLittleIconConfigService("default", userId,
							TABLE_FOOTICON_SETTING);
				}else{
					int glId = gpList.get(0).getId();
					GisDispatch_LittleIconSetting gl = new GisDispatch_LittleIconSetting();
					gl.setId(glId);
					gl.setShowOrNot(isShowFlag);
					gl.setUserId(userId);
					gl.setLittleicon_id((int)targetId);
					hibernateTemplate.update(gl);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	


	/**
	 * 获取图元信息窗口的详细信息
	 * @param bpu
	 * @return Mar 28, 2012 12:05:51 PM gmh
	 */
	public Map getInfoWindowMsgOfPicUnitService(BasicPicUnit bpu) {
		
		Map resourceMap=null;
		if(bpu!=null){
			String resourceType=bpu.getType();
			
			String resourceId=bpu.getId();
			
			//判断当前图元是什么类型的资源
			String type="";
			if(GisDispatchGraphConstant.RESOURCE_STATION.equals(resourceType)){	//站址
				type=STATION_FLAG;
				resourceMap=this.getResourceInfoByResourceTypeAndResourceId(type, resourceId);
				resourceMap.put("resourceType", GisDispatchGraphConstant.RESOURCE_STATION);
				
			}else if(GisDispatchGraphConstant.RESOURCE_MANHOLE.equals(resourceType)){		//管井
				type="Manhole";
				resourceMap=this.getResourceInfoByResourceTypeAndResourceId(type, resourceId);
				resourceMap.put("resourceType", GisDispatchGraphConstant.RESOURCE_MANHOLE);
				
			}else if(GisDispatchGraphConstant.RESOURCE_HUMAN.equals(resourceType)){	//人员
				resourceMap=this.getHumanResourceInfo(resourceId);
				resourceMap.put("resourceType", GisDispatchGraphConstant.RESOURCE_HUMAN);
				
			}else if(GisDispatchGraphConstant.RESOURCE_CAR.equals(resourceType)){	//车辆
				
				//hardcode
				//resourceId="1";
				resourceMap=this.getCarResourceInfo(resourceId);
				resourceMap.put("resourceType", GisDispatchGraphConstant.RESOURCE_CAR);
			}else if((GisDispatchGraphConstant.RESOURCE_MAINTAINGROUP.equals(resourceType))){	//维护组
				resourceMap=this.getTeamHumanInfo(resourceId);
				resourceMap.put("resourceType", GisDispatchGraphConstant.RESOURCE_MAINTAINGROUP);
				
			}
			else if((GisDispatchGraphConstant.RESOURCE_SHIYEBU.equals(resourceType)) ||			//事业部
					(GisDispatchGraphConstant.RESOURCE_HEADERQUARTER.equals(resourceType)) ||			//公司
					(GisDispatchGraphConstant.RESOURCE_PROJECTGROUP.equals(resourceType)) ||			//项目组
					(GisDispatchGraphConstant.RESOURCE_MAINTAINTEAMAAREADDRESS.equals(resourceType))){		//维护片区
				
				//获取组织架构的人数
				int orgStaffCount = 0; 
				//List<ProviderOrganization> orgList = gisDaiFactory.getProviderOrganizationService().getOrgListDownwardByOrg(Long.valueOf(resourceId));
				//yuan.yw
				List<Map<String,Object>> orgList = this.sysOrganizationDao.getDownwardOrgListByOrgId(Long.valueOf(resourceId));
		
				if(orgList!=null && !orgList.isEmpty()){
					for (Map<String,Object> org : orgList) {
						//ou.jh
						List<Map<String,Object>> userByOrgId = this.sysOrgUserService.getUserByOrgId(Long.valueOf(org.get("orgId")+""));
//						List<Staff> orgStaffList = gisDaiFactory.getProviderOrganizationService().getStaffListByOrgIdService(org.getId());
						if(userByOrgId!=null && !userByOrgId.isEmpty()){
							orgStaffCount += userByOrgId.size();
						}
					}
				}
				resourceMap=this.getOrganizationInfo(resourceId);
				resourceMap.put("orgStaffCount", orgStaffCount);
				resourceMap.put("resourceType",resourceType);
			}
			else if((GisDispatchGraphConstant.RESOURCE_QUESTIONSPOT.equals(resourceType)) ||	//问题点
					(GisDispatchGraphConstant.RESOURCE_HIDDENTROUBLESPOT.equals(resourceType)) ||			//隐患点
					(GisDispatchGraphConstant.RESOURCE_FAULTSPOT.equals(resourceType)) ||			//故障点
					(GisDispatchGraphConstant.RESOURCE_WATCHSPOT.equals(resourceType)) ){			//盯防点

				
				resourceMap=this.getHotSportInfo(resourceId);
				resourceMap.put("resourceType",resourceType);
			}else if((GisDispatchGraphConstant.RESOURCE_STOREHOUSE.equals(resourceType)) ){			//仓库
				resourceMap=this.getWarehousetInfo(resourceId);
				resourceMap.put("resourceType",resourceType);
			}
		}
		return resourceMap;
	}

	

	/**
	 * 根据资源类型和资源id，获取资源的信息
	 * 
	 * @return
	 */
	private Map<String, Object> getResourceInfoByResourceTypeAndResourceId(String resourceType, String resourceId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (resourceType == null && "".equals(resourceType)) {
				return resultMap;
			}
			if (resourceId == null && "".equals(resourceId)) {
				return resultMap;
			}
			NetworkResourceInterfaceService networkResourceService = gisDaiFactory.getNetworkResourceService();
			
			// 获取站址信息
			Map beStation = networkResourceService.getResourceByReIdAndReTypeService(resourceId, resourceType);
			
			if (beStation != null) {
				resultMap.put("objectIdentity", beStation.get("id")==null?"":beStation.get("id"));
				resultMap.put("objectName", beStation.get("name")==null?"":beStation.get("name"));
				resultMap.put("objectType", resourceType);
				resultMap.put("longitude", beStation.get("longitude")==null?"":beStation.get("longitude"));
				resultMap.put("latitude", beStation.get("latitude")==null?"":beStation.get("latitude"));
				resultMap.put("location", beStation.get("address")==null?"":beStation.get("address"));
				
				List<Map<String,String>> baseStationListMap=new ArrayList<Map<String,String>>();
				//获取下属基站
				List<Map<String, String>> baseStationList = networkResourceService.getStationByStationIdAndBaseStationTypeService(resourceId, STATION_FLAG);
				if(baseStationList!=null && !baseStationList.isEmpty()){
					for (Map<String, String> map : baseStationList) {
						Map<String,String> tempMap=new HashMap<String,String>();
						String baseStationId = map.get("id")+"";	//获取基站id
						String baseStationName = map.get("name");	//获取基站名称
						String baseStationGrade = map.get("importancegrade");	//获取基站等级
						String baseStationType = map.get("_entityType");	
						
						tempMap.put("id", baseStationId);
						tempMap.put("name", baseStationName);
						tempMap.put("importancegrade", baseStationGrade);
						tempMap.put("baseStationType", baseStationType);
						baseStationListMap.add(tempMap);
					}
				}
				resultMap.put("baseStationList", baseStationListMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 获取车辆资源详细信息
	 * 
	 * @param carResourceId
	 * @return
	 */
	private Map<String, Object> getCarResourceInfo(String carResourceId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (carResourceId.equals("")||carResourceId==null) {
				return resultMap;
			}
			//获取车辆信息
			Map<String, Object> carMap = gisDaiFactory.getCardispatchCarDao().findCarInfoById(carResourceId);
			if(carMap!=null){
				resultMap.putAll(carMap);
				resultMap.put("carPic", carMap.get("carPic"));
			}
			//获取司机信息
			Map pMap = new HashMap();
			pMap.put("carId", carResourceId);
			List<Map<String, Object>> driverList = gisDaiFactory.getCardispatchDriverDao().findDriverList(pMap);
			if(driverList!=null && !driverList.isEmpty()){
				Map<String, Object> driverMap = driverList.get(0);
				String driverCarId = driverMap.get("cardriverpairId")+"";
				resultMap.put("driverCarId", driverCarId);
				resultMap.put("driverName", driverMap.get("driverName"));
				resultMap.put("driverAccount", driverMap.get("accountId"));
				resultMap.put("driverPhone", driverMap.get("driverPhone"));
			}
			String carState = null;
			//获取车辆终端信息
			List<Map<String, Object>> terminalList = gisDaiFactory.getCardispatchTerminalDao().findTerminalList(pMap);
			if(terminalList!=null && !terminalList.isEmpty()){
				Map<String, Object> tMap = terminalList.get(0);
				carState = tMap.get("terminalState")+"";
			}
			carMap.put("carState", carState);
			resultMap.put("carState", carState);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}
	
	
	/**
	 * 获取人员资源详细信息，包括技能信息
	 * @param userId
	 * @return
	 */
	private Map<String,Object> getHumanResourceInfo(String userId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(userId==null || "".equals(userId)){
				return null;
			}
			// 获取人员信息
			//ou.jh
			SysOrgUser sysOrgUserByAccount = this.sysOrgUserService.getSysOrgUserByAccount(userId);
//			Staff staff = orgService.getStaffByAccount(userId);
			if(sysOrgUserByAccount!=null){
				resultMap.put("staffId", userId);
				resultMap.put("staffName", sysOrgUserByAccount.getName());
				String sex = "";
				if(sysOrgUserByAccount.getGender() != null){
					
					if(sysOrgUserByAccount.getGender().equals("male")){
						sex = "男";
					}else if(sysOrgUserByAccount.getGender().equals("female")){
						sex = "女";
					}else{
						sex = "";
					}
				}else{
					sex = "";
				}
				resultMap.put("sex", sex);
				resultMap.put("phone", sysOrgUserByAccount.getMobile());
			}
			
			//获取人员技能信息
			if(resultMap!=null){
				List<Map> skillList = gisDaiFactory.getStaffSkillService().getStaffSkillListByAccount(userId);
				resultMap.put("skillList", skillList);
			}
			List<Map<String,Object>> boxList=new ArrayList<Map<String,Object>>();
			
			if(resultMap!=null){
				resultMap.put("materialList", boxList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 根据组织id，获取组织信息
	 * @param orgId
	 * @return
	 */
	private Map<String, Object> getOrganizationInfo(String orgId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
			
			
			SysOrg tempOrg = sysOrganizationDao.getOrgByOrgId(Long.valueOf(orgId));//du.hw修改
			//调用外部接口，获取信息

			if(tempOrg!=null){
				resultMap.put("orgId", tempOrg.getOrgId());
				resultMap.put("orgName", tempOrg.getName());
				resultMap.put("parentOrgId", tempOrg.getEnterpriseId());
				resultMap.put("orgType", tempOrg.getOrgTypeId());
				resultMap.put("orgDuty", tempOrg.getOrgUserId());
				resultMap.put("address", tempOrg.getAddress());
				resultMap.put("latitude", tempOrg.getLatitude());
				resultMap.put("longitude", tempOrg.getLongitude());
				resultMap.put("contactPhone", tempOrg.getMobile());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	
	
	/**
	 * 获取标记热点的浮窗信息
	 * @param bpuId 图元id
	 * @return
	 */
	private Map<String,Object> getHotSportInfo(String bpuId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
	/**
	 * 获取仓库的浮窗信息
	 * @param bpuId 图元id
	 * @return
	 */
	private Map<String,Object> getWarehousetInfo(String bpuId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//TODO 获取仓库信息
			
			List<Map<String,Object>> boxList=new ArrayList<Map<String,Object>>();
			if(resultMap!=null){
				resultMap.put("materialList", boxList);
			}
			List<Map<String,Object>> outboundList=new ArrayList<Map<String,Object>>();
			if(resultMap!=null){
				resultMap.put("outboundList", outboundList);
			}
			List<Map<String,Object>> incomingList=new ArrayList<Map<String,Object>>();
			if(resultMap!=null){
				resultMap.put("incomingList", incomingList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
	/**
	 * 获取维护组的浮窗信息
	 * @param bpuId
	 * @return
	 */
	private Map<String,Object> getTeamHumanInfo(String bpuId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if(resultMap!=null){
				resultMap.put("teamMemberList", new ArrayList<Map>());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	
	/**
	 * 获取图元忙闲任务数
	 * @param bpu
	 * @return
	 */
	public Map<String,String> getBusyOrNotTaskByPicUnitService(BasicPicUnit bpu){
		Map<String, String> busyOrNotMap=new HashMap<String, String>();
		try {
			if(bpu==null){
				return busyOrNotMap;
			}
			if(!GisDispatchGraphConstant.RESOURCE_HUMAN.equals(bpu.getType())){
				return busyOrNotMap; 
			}
			//人员待办任务单总数
			long taskOrderCount = gisDaiFactory.getWorkManageService().getTaskOrderCountByObjectTypeAndObjectId("people", bpu.getId());
			
			busyOrNotMap.put("totalTask", ""+taskOrderCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return busyOrNotMap;
	}
	

	/**
	 * 查找默认用户的图层配置记录,并且保存当前用户的用户配置
	 * 
	 * @param defaultUserId
	 * @return
	 */
	public List<GisDispatch_GraphLayerSetting> getDefaultUserLayerConfig(String defaultUserId,
			String targetUserId, String table) {
		List<GisDispatch_GraphLayerSetting> list = null;
		try {
			String hql = "from GisDispatch_GraphLayerSetting where userId='"+defaultUserId+"'";
			list = hibernateTemplate.find(hql);
			if (list != null && list.size() > 0) {
				for (GisDispatch_GraphLayerSetting be : list) {
					GisDispatch_GraphLayerSetting gls = new GisDispatch_GraphLayerSetting();
					gls.setShowOrNot(be.getShowOrNot());
					gls.setUserId(targetUserId);
					gls.setGl_id(be.getGl_id());
					hibernateTemplate.save(gls);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查找默认用户的图元类型配置记录,并且保存当前用户的用户配置
	 * 
	 * @param defaultUserId
	 * @return
	 */
	public List<GisDispatch_GraphElementTypeSetting> getDefaultUserGraphElementTypeConfig(
			String defaultUserId, String targetUserId, String table) {
		List<GisDispatch_GraphElementTypeSetting> list = null;
		try {
			String hql = "from GisDispatch_GraphElementTypeSetting where userId='"+defaultUserId+"'";
			list = hibernateTemplate.find(hql);
			if (list != null && list.size() > 0) {
				for (GisDispatch_GraphElementTypeSetting be : list) {
					GisDispatch_GraphElementTypeSetting geType = new GisDispatch_GraphElementTypeSetting();
					geType.setIsVisible(be.getIsVisible());
					geType.setGeType_id(be.getGeType_id());
					geType.setUserId(targetUserId);
					hibernateTemplate.save(geType);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查找默认用户的图元下标配置记录,并且保存当前用户的用户配置
	 * 
	 * @param defaultUserId
	 * @return
	 */
	public List<GisDispatch_LittleIconSetting> getDefaultUserLittleIconConfigService(
			String defaultUserId, String targetUserId, String table) {
		List<GisDispatch_LittleIconSetting> list = null;
		try {
			String hql = "from GisDispatch_LittleIconSetting where userId='"+defaultUserId+"'";
			list = hibernateTemplate.find(hql);
			if (list != null && list.size() > 0) {
				for (GisDispatch_LittleIconSetting be : list) {
					GisDispatch_LittleIconSetting lis = new GisDispatch_LittleIconSetting();
					lis.setLittleicon_id(be.getLittleicon_id());
					lis.setShowOrNot(be.getShowOrNot());
					lis.setUserId(targetUserId);
					hibernateTemplate.save(lis);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	/**
	 * 根据用户id默认获取集中调度的工作列表
	 * @param userId
	 */
	public Map<String,Map> getWorkListByUserService(String userId){
		return this.getWorkListByUserService(userId, moduleWorkFlag);
	}
	
	
	/**
	 * 根据用户id默认获取集中调度的工作列表
	 */
	public Map<String,Map> getWorkListByUserService(String userId,String moduleWorkFlag){
		//获取用户所在的可用业务单元列表
		List<String> workIdList=new ArrayList<String>();
		Map<String,Map> workInfoMap=new HashMap<String,Map>();
		//标识是否可以创建工单
		boolean canCreateWO = false;
		//获取用户可用组织架构角色
		//ou.jh
		List<SysRole> userRoles = this.sysRoleService.getUserRoles(userId);
//		List<Role> roleList = gisDaiFactory.getProviderOrganizationService().getOrgRoleToRoleByAccountService(userId);
		if(userRoles!=null){
			for (SysRole role : userRoles) {
				//TODO 判断一个人是否为调度员角色
				if(role.getName().indexOf("调度员")>-1 || role.getName().indexOf("文员")>-1){
					canCreateWO = true;
				}
			}
		}
		//TODO Hard code,将来可能由 权限管理模块 提供接口
		GisDispatch_UserWork userWork = gisDispatchDao.getUserWorkByWorkId("4");
		if(userWork!=null){
			Map tempMap = new HashMap();
			tempMap.put("id", userWork.getId());
			tempMap.put("workName", userWork.getWorkName());
			tempMap.put("workLocation", userWork.getWorkLocation());
			tempMap.put("workFlag", userWork.getWorkFlag());
			workInfoMap.put(userWork.getId()+"",tempMap);
		}
		if(canCreateWO){
			userWork = gisDispatchDao.getUserWorkByWorkId("1");
			if(userWork!=null){
				Map tempMap = new HashMap();
				tempMap.put("id", userWork.getId());
				tempMap.put("workName", userWork.getWorkName());
				tempMap.put("workLocation", userWork.getWorkLocation());
				tempMap.put("workFlag", userWork.getWorkFlag());
				workInfoMap.put(userWork.getId()+"",tempMap);
			}
		}
		return workInfoMap;
	}
	
	
	/**
	 * 根据userId，获取站址，车辆，人员的数量统计
	 * @param userId
	 * @return
	 */
	public List<Map<String,String>> getResouceCountListService(String userId){
		List<Map<String,String>> resultMap=new ArrayList<Map<String,String>>();
		
		return resultMap;
	}
	
	

	/**
	 * 根据资源类型、资源Id获取该资源关联的工单、任务单
	 * @param resourceType 资源类型
	 * @param resourceId 资源Id
	 * @return
	 */
	public Map getResourceWorkOrderAndTaskOrderListService(String resourceType, String resourceId) {
		Map resultMap = new HashMap();	//返回结果
		List<Map> workOrderList = new ArrayList<Map>();	//工单List
		List<Map> taskOrderList = new ArrayList<Map>();	//任务单List
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(resourceType.equals(GisDispatchGraphConstant.RESOURCE_STATION)){
			//获取站址关联的工单
			//modifyCode,改为站址id
			//查询巡检-----------end---------
		}
		
		resultMap.put("taskOrderList", taskOrderList);
		resultMap.put("workOrderList", workOrderList);
		return resultMap;
	}

	/**
	 * 根据资源类型、资源Id获取该资源的待办工单总数
	 * @param resourceType
	 * @param resourceId
	 * @return
	 */
	public Map getResourcePendingWorkOrderAmountService(String resourceType,String resourceId) {
		Map resultMap = new HashMap();
		try {
			/*Context context = this.gisDaiFactory.getContextFactory().CreateContext();
			Query query = context.createQueryBuilder("workmanage_count_workorder_networkresource");
			query.add(Restrictions.eq("resourceId", Long.valueOf(resourceId)));
			query.add(Restrictions.eq("resourceType", resourceType));
			BasicEntity be = context.queryEntity(query);
			if(be!=null){
				resultMap = be.toMap();
			}else{
				
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("qiangxiuCount", "0");
		resultMap.put("xunjianCount", "0");
		resultMap.put("totalCount", "0");
		return resultMap;
	}

	/**
	 * 获取初始化的图层管理器
	 * @param userId 用户Id
	 * @param centerLatLng 当前经纬度
	 * @param currentVisibleKM 当前可见公里数
	 * @param latLngBounds 窗口范围
	 * @author chen
	 * @return
	 */
	public PicLayerManager getInitPicLayerManagerService(String userId,LatLng centerLatLng, double currentVisibleKM,LatLngBounds latLngBounds) {
		//获取用户整个组织架构
		userOrgIds = new ArrayList<String>();
		//List<ProviderOrganization> orgList = gisDaiFactory.getProviderOrganizationService().getOrgByAccountService(userId);
		List<SysOrg> orgList=this.sysOrganizationService.getOrgByAccountService(userId);
		if(orgList!=null && !orgList.isEmpty()){
			for (SysOrg org : orgList) {
				userOrgIds.add(org.getOrgId()+"");
				//List<ProviderOrganization> subOrgList = gisDaiFactory.getProviderOrganizationService().getOrgListDownwardByOrg(org.getId());
				//yuan.yw
				List<Map<String,Object>> subOrgList = this.sysOrganizationDao.getDownwardOrgListByOrgId(Long.valueOf(org.getOrgId()));
		
				if(subOrgList!=null && !subOrgList.isEmpty()){
					for (Map<String,Object> subOrg : subOrgList) {
						userOrgIds.add(subOrg.get("orgId")+"");
					}
				}
			}
		}
		//获取图元任务信息
		stationWorkOrderList = null;
		stationWorkOrderList = gisDaiFactory.getWorkManageService().getWorkmanageCountWorkorderObjectListByResourceType(STATION_FLAG);
		// 用户设定的图层信息
		Map<Long, UserLayerSetting> userLayerSettings = getUserLayerSetting(userId);
		
		// 获取用户设定的图元类型显示信息
		Map<Long, UserLayerTypeSetting> userLayerTypeSettings = getUserLayerTypeSetting(userId);
		
		// 获取用户设定的脚标显示信息
		Map<Long, UserLittleIconSetting> userLittleIconSettings = getUserLittleIconSetting(userId);


		// 获取所有的图层信息
		List<GisDispatch_GraphLayer> graphLayers = gisDispatchDao.getGraphLayer();

		// 图层的可见性
		Map<Long, Boolean> glVisibles = new HashMap<Long, Boolean>();

		if (userLayerSettings != null && userLayerSettings.size() > 0) {
			for (UserLayerSetting uls : userLayerSettings.values()) {
				glVisibles.put(uls.getPicLayerId(), uls.isShowOrNot());
			}
		}
		
		//che.yd----图元类型的可见性----------begin
		// 图元类型的可见性
		Map<Long, Boolean> getVisibles = new HashMap<Long, Boolean>();

		if (userLayerTypeSettings != null && userLayerTypeSettings.size() > 0) {
			for (UserLayerTypeSetting uts : userLayerTypeSettings.values()) {
				getVisibles.put(uts.getPicUnitTypeId(), uts.isVisible());
			}
		}
		
		//che.yd----图元类型的可见性----------end

		// 建立图层管理器
		PicLayerManager plm = new PicLayerManager();
		plm.setCenter(centerLatLng);// 设置中心点
		plm.setCurrentVisiblegKM(currentVisibleKM);// 可见公里数
		plm.setWindowLatLngRange(latLngBounds);// 设置当前窗口经纬度
		plm.setBackgroundLatLngCalculator(new DoubleBackgroundLatLngCalculator());// 设置背景窗口经纬度
		plm.setBackgroundLatLngRange(plm.calculateBackgroundLatLngBounds());// 计算背景窗口经纬度
		
		
		plm.setUserLayerSettings(userLayerSettings);// 将用户的图层设置信息保存进图层管理器
		plm.setUserLayerTypeSettings(userLayerTypeSettings);// 将用户的图元类型设置信息保存进图层管理器
		plm.setUserLittleIconSettings(userLittleIconSettings);// 将用户的脚标设置信息保存进图层管理器
		
		// 创建图层，并获取图层关联的图元类型信息
		// 结构如下：

		// 图层
		// |___图元类型（列表）
		// |___脚标（列表）
		// |___可见公里数（单个）
		if ( graphLayers!= null ) {
			for (GisDispatch_GraphLayer gl : graphLayers) {
				// 创建图层
				PicLayer pl = new PicLayer();
				pl.setPicLayerManager(plm);// 关联图层管理器
				
				//long glId = Long.parseLong((String) gl.getValue("id"));
				long glId = gl.getId();
				if (glVisibles.get(glId) != null) {
					pl.setShow(glVisibles.get(glId));// 是否可见
				} else {
					pl.setShow(false);
				}
				pl.setId(glId);
				pl.setName(gl.getName());

				// 关联该gl和用户的图层设置对象
				if (userLayerSettings.get(glId) != null) {
					userLayerSettings.get(glId).setPicLayer(pl);
				}
				
				// 图层的图元类型列表
				List<PicUnitType> picUnitTypes = new ArrayList<PicUnitType>();
				// 根据图层获取图层关联的 图元类型 信息
				List<GisDispatch_GraphElementType> getypes = gisDispatchDao.getGraphElementTypesByGraphLayer(gl);
				
				if (getypes != null && getypes.size() > 0) {// 获取图元类型设置的可见公里数、脚标
					
					for (GisDispatch_GraphElementType getype : getypes) {
						// 创建图元类型对象
						PicUnitType picUnitType = new PicUnitType();
						picUnitType.setCode(getype.getType());
						picUnitType.setName(getype.getName());
						picUnitType.setId(getype.getId());
						picUnitType.setIcon(getype.getDefaultIcon());
						
						//-------------che.yd-----start
						
						if (getVisibles.get(picUnitType.getId()) != null) {
							picUnitType.setNeedShow(getVisibles.get(picUnitType.getId()));
						} else {
							picUnitType.setNeedShow(false);
						}
						
						picUnitType.setPicLayer(pl);	//设置图元类型关联的图层
						
						//-------------che.yd-----end

						// 关联该图元类型和用户的图元类型设置对象
						if (userLayerTypeSettings.get(picUnitType.getId()) != null) {
							userLayerTypeSettings.get(picUnitType.getId()).setPicUnitType(picUnitType);
						}
						
						List<LittleIcon> lis = new ArrayList<LittleIcon>();
						
						// 根据图元类型获取脚标信息
						List<GisDispatch_LittleIcon> littleIcons = gisDispatchDao.getLittleIconsOfGraphElementType(getype);
						if (littleIcons != null && littleIcons.size() > 0) {
							
							for (GisDispatch_LittleIcon lbe : littleIcons) {
								LittleIcon li = new LittleIcon();
								li.setIcon(lbe.getIcon());
								li.setId(lbe.getId());
								li.setName(lbe.getLittleiconName());
								li.setPosition(lbe.getPosition()+"");
								li.setPicUnitType(picUnitType);

								//-------------che.yd-----start
								li.setPicUnitType(picUnitType);
								//-------------che.yd-----end
								
								lis.add(li);// 增加进脚标列表

								// 关联该脚标和用户的脚标设置对象
								if (userLittleIconSettings.get(li.getId()) != null) {
									userLittleIconSettings.get(li.getId()).setLittleIcon(li);
								}
							}
						}
						// 将脚标增加进图元类型对象
						picUnitType.setLittleIcons(lis);

						// 获取可见公里数
						List<GisDispatch_GraphElementTypeAndMileSetting> mileSettings = gisDispatchDao.getMileSettingsOfGeType(getype);
						
						if (mileSettings != null && mileSettings.size() > 0) {
							GisDispatch_GraphElementTypeAndMileSetting ms = mileSettings.get(0);
							
							// 创建图元类型与可见公里数对象
							PicUnitTypeMile putm = new PicUnitTypeMile();
							putm.setId(ms.getId());
							putm.setMaxVisibleMile(ms.getMaxVisibleMile());
							putm.setMinVisibleMile(ms.getMinVisibleMile());
							putm.setPicUnitType(picUnitType);
							
							
							picUnitType.setPicUnitTypeMile(putm);// 跟图元类型挂上关系
							boolean isGonglishuVisible = picUnitType.inShowRange(currentVisibleKM);
							picUnitType.setGonglishuVisible(isGonglishuVisible);
						}

						// 将该图元类型增加进列表
						picUnitTypes.add(picUnitType);
					}
					
					// 给图层增加图元类型列表
					pl.setPicUnitTypes(picUnitTypes);
				
				}

				// 将图层增加进图层管理器
				if (pl.isShow()) {// 可见，则加进可见列表
					plm.addAVisiblePicLayer(pl);
					this.fillPicLayerService(userId, pl);// 填充该图层
					
				} else {// 不可见，则加进不可见列表
					plm.addAHiddenPicLayer(pl);
				}
				
			}
		}
		
		return plm;
	}
	
	/**
	 * 获取用户的图层设置信息
	 * 
	 * @param userId
	 * @return Mar 28, 2012 5:03:37 PM gmh
	 */
	private Map<Long, UserLayerSetting> getUserLayerSetting(String userId) {
		List<GisDispatch_GraphLayerSetting> userGraphSettings = gisDispatchDao.getUserGraphSettings(userId);
		
		// 关联的图层id为key
		Map<Long, UserLayerSetting> userLayerSettings = new HashMap<Long, UserLayerSetting>();
		if (userGraphSettings != null && userGraphSettings.size() > 0) {
			for (GisDispatch_GraphLayerSetting be : userGraphSettings) {
				UserLayerSetting us = new UserLayerSetting();
				us.setPicLayerId(be.getGl_id());
				if (be.getId() != 0) {
					us.setId(be.getId());
				} else {
					us.setId(-1);
				}
				try {
					us.setShowOrNot(be.getShowOrNot() == 1 ? true : false);
				} catch (Exception e) {
					us.setShowOrNot(false);
				}
				us.setUserId(userId);
				userLayerSettings.put(us.getPicLayerId(), us);
			}
		}
		return userLayerSettings;
	}

	/**
	 * 返回用户的图元类型设置信息
	 * 
	 * @param userId
	 * @return Mar 28, 2012 5:24:06 PM gmh
	 */
	private Map<Long, UserLayerTypeSetting> getUserLayerTypeSetting(
			String userId) {
		List<GisDispatch_GraphElementTypeSetting> userGraphTypeSettings = gisDispatchDao.getUserGraphElementTypeSettings(userId);
		// 关联的图元类型的id为key
		Map<Long, UserLayerTypeSetting> ults = new HashMap<Long, UserLayerTypeSetting>();

		if (userGraphTypeSettings != null && userGraphTypeSettings.size() > 0) {
			for (GisDispatch_GraphElementTypeSetting be : userGraphTypeSettings) {
				UserLayerTypeSetting ult = new UserLayerTypeSetting();
				
				if (be.getId() != 0) {
					ult.setId(be.getId());
				} else {
					ult.setId(-1);
				}

				ult.setUserId(userId);
				try {
					ult.setVisible(be.getIsVisible() == 1 ? true : false);
				} catch (Exception e) {
					ult.setVisible(false);
				}

				ult.setPicUnitTypeId(be.getGeType_id());

				ults.put(ult.getPicUnitTypeId(), ult);
			}
		}

		return ults;
	}
	/**
	 * 获取用户的图元类型脚标设置信息
	 * 
	 * @param userId
	 * @return Mar 28, 2012 5:25:43 PM gmh
	 */
	private Map<Long, UserLittleIconSetting> getUserLittleIconSetting(
			String userId) {
		
		List<GisDispatch_LittleIconSetting> userLittleIconSettings = gisDispatchDao.getUserGraphLittleIconSettings(userId);
		// 关联的脚标id为key
		Map<Long, UserLittleIconSetting> ulis = new HashMap<Long, UserLittleIconSetting>();

		if (userLittleIconSettings != null && userLittleIconSettings.size() > 0) {
			for (GisDispatch_LittleIconSetting be : userLittleIconSettings) {
				UserLittleIconSetting uli = new UserLittleIconSetting();
				if (be.getId() != 0) {
					uli.setId(be.getId());
				} else {
					uli.setId(-1);
				}

				uli.setUserId(userId);
				try {
					uli.setShowOrNot(be.getShowOrNot() == 1 ? true : false);
				} catch (Exception e) {
					uli.setShowOrNot(false);
				}

				uli.setLittleIconId(be.getLittleicon_id());
				ulis.put(uli.getLittleIconId(), uli);
			}
		}
		return ulis;
	}

	/**
	 * 获取当前登录人 区域信息
	 * @author chen
	 * @return
	 */
	public Map getCurrentUserAreaInfoService() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = (String)request.getSession().getAttribute(UserInfo.USERID);
		Map resMap = new HashMap();
		//List<ProviderOrganization> orgList = gisDaiFactory.getProviderOrganizationService().getOrgByAccountAndOrgType(userId, OrganizationConstant.ORGANIZATION_MAINTENANCEAREA);
		//yuan.yw
		
		List<SysOrg> orgList = this.sysOrganizationService.getOrgByAccountAndOrgType(userId, OrganizationConstant.ORGANIZATION_MAINTENANCETEAM);
	
		if(orgList==null || orgList.isEmpty()){
			//orgList = gisDaiFactory.getProviderOrganizationService().getTopLevelOrgByAccount(userId);
			orgList= this.sysOrganizationService.getTopLevelOrgByAccount(userId);
		}
		if(orgList!=null && !orgList.isEmpty()){
			for (SysOrg org : orgList) {
				if(org.getLatitude()!=null && org.getLatitude() > 0 && org.getLongitude() != null && org.getLatitude() > 0){
					resMap.put("latitude", org.getLatitude());
					resMap.put("longitude", org.getLongitude());
				}
			}
		}
		if(resMap.get("latitude") == null || resMap.get("latitude") == ""){
			//Hard code 防止拿不到数据
			resMap.put("latitude", 23.12651273063735);
			resMap.put("longitude", 113.41254847709276);
		}
		//获取用户可用组织架构角色
		boolean isTaskDispatcher = false;
		List<SysRole> userRoles = this.sysRoleService.getUserRoles(userId);
//		List<Role> roleList = gisDaiFactory.getProviderOrganizationService().getOrgRoleToRoleByAccountService(userId);
		if(userRoles!=null){
			for (SysRole role : userRoles) {
				//hard code 任务调度员角色
				if(role.getCode().toLowerCase().equals("taskdispatcher")){
					isTaskDispatcher = true;
					resMap.put("isTaskDispatcher", isTaskDispatcher);
					break;
				}
			}
		}
		return resMap;
	}

	/**
	 * 地图缩放级别响应事件
	 * @param plm 图层管理器
	 * @param currentVisibleKM 当前地图可见公里数
	 * @param latLngBounds 当前地图窗口范围
	 * @param centerLatLng 当前地图中心经纬度
	 * @return
	 */
	public String responseZoomLevelChangeService(PicLayerManager plm,Double currentVisibleKM,LatLngBounds latLngBounds,LatLng centerLatLng) {
		//刷新图元任务信息
		stationWorkOrderList = gisDaiFactory.getWorkManageService().getWorkmanageCountWorkorderObjectListByResourceType(STATION_FLAG);
		String resultJson = "";
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			String userId = (String)request.getSession().getAttribute("userId");
			String userName = userId;
			// 处理所有可见图层
			// 找可见图层
			Map<String, PicLayer> picLayers = plm.getVisiblePicLayers();
			if (picLayers == null || picLayers.isEmpty()) {
				resultJson = "{hasChange:false,msg:'当前没有任何可见图层数据'}";
				return resultJson;
			}
			// 重新设置图层管理器的当前窗口、背景窗口
			plm.setWindowLatLngRange(latLngBounds);
			plm.setCenter(centerLatLng);
			plm.setBackgroundLatLngRange(plm.calculateBackgroundLatLngBounds());
			LatLngBounds newLatLngBounds = plm.getBackgroundLatLngRange();

			// 遍历可见图层，修改图元类型的可见性
			for (String key : picLayers.keySet()) {
				PicLayer pl = picLayers.get(key);
				
				pl.modifyPicUnitTypeShowOrNot(currentVisibleKM);

				//System.out.println("图层：" + pl.getName() + ",缩放前,不可见图元队列长度："+ pl.getHiddenPicUnitMap().size());
				//System.out.println("图层：" + pl.getName() + ",缩放动作前,可见图元队列长度："+ pl.getVisiblePicUnitMap().size());

				// 处理可见图元列表
				Map<String, BasicPicUnit> visiblePicUnitMap = pl.getVisiblePicUnitMap();
				
				// 待从某个队列删除的图元
				List<BasicPicUnit> deleteBpus = null;
				if (!visiblePicUnitMap.isEmpty()) {
					for (String bkey : visiblePicUnitMap.keySet()) {
						BasicPicUnit bpu = visiblePicUnitMap.get(bkey);
						
						//che.yd   增加bpu.getPicUnitType().isGonglishuVisible()，可见公里数条件
						if (!bpu.getLatlng().containedIn(latLngBounds) || !bpu.getPicUnitType().isNeedShow() || !bpu.getPicUnitType().isGonglishuVisible()) {

							if (deleteBpus == null) {
								deleteBpus = new ArrayList<BasicPicUnit>();
							}

							// 不在处于新窗口内，或者该图元类型不需要显示，则移至新隐藏队列
							pl.addToNewHiddenBasicPicUnitList(bpu);// 增加进新隐藏队列
							pl.addToHiddenPicUnitMap(bpu);// 加进隐藏列表

							deleteBpus.add(bpu);// 加进待删除队列
							// pl.removeFromVisiblePicUnitMap(bpu);// 从可见队列删除
							
						}
					}
				}

				if (deleteBpus != null && deleteBpus.size() > 0) {
					// 有待从可见列表删除的图元
					for (BasicPicUnit bpuu : deleteBpus) {
						pl.removeFromVisiblePicUnitMap(bpuu);// 从可见队列删除
					}
				}
				// 处理隐藏图元列表
				deleteBpus = null;

				Map<String, BasicPicUnit> hiddenPicUnitMap = pl.getHiddenPicUnitMap();
				
				if (!hiddenPicUnitMap.isEmpty()) {
					for (String bkey : hiddenPicUnitMap.keySet()) {
						BasicPicUnit bpu = hiddenPicUnitMap.get(bkey);
						
						//che.yd   增加bpu.getPicUnitType().isGonglishuVisible()，可见公里数条件
						PicUnitType picUnitType = bpu.getPicUnitType();
						if (bpu.containedIn(newLatLngBounds) && picUnitType.isNeedShow() && picUnitType.isGonglishuVisible()) {
							if (deleteBpus == null) {
								deleteBpus = new ArrayList<BasicPicUnit>();
							}
							// 处于新窗口范围内,且该种类型的图元需要显示
							pl.addToNewVisibleBasicPicUnitList(bpu);// 增加进新可见图元队列
							pl.addToVisiblePicUnitMap(bpu);// 加进可见列表

							deleteBpus.add(bpu);// 增加进待从隐藏列表删除的图元队列
							// pl.removeFromHiddenPicUnitMap(bpu);// 从隐藏图元队列删除
						}
					}
				}
				if (deleteBpus != null && deleteBpus.size() > 0) {
					// 有待从隐藏列表删除的图元
					for (BasicPicUnit bpuu : deleteBpus) {
						pl.removeFromHiddenPicUnitMap(bpuu);// 从隐藏队列删除
					}
				}
				//System.out.println("图层：" + pl.getName() + ",缩放后,不可见图元队列长度："+ pl.getHiddenPicUnitMap().size());
				//System.out.println("图层：" + pl.getName() + ",缩放动作后,可见图元队列长度："+ pl.getVisiblePicUnitMap().size());
			}

			// 转换为json
			resultJson = plm.toZoomChangedJson();
			
			//直接在这里加，免得改其它类的代码
			resultJson = resultJson.substring(0,resultJson.length()-1)+",userId:'"+userId+"',userName:'"+userName+"'}";
			
			// 清空暂存列表
			for (String key : picLayers.keySet()) {
				PicLayer pl = picLayers.get(key);
				pl.clearNewHiddenList();// 清空新增隐藏列表
				pl.clearNewVisibleList();// 清空新增可见列表
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson;
	}

	/**
	 * 获取图元任务详细信息
	 * @param bpu 图元
	 * @param infoType 类型
	 * @param userId 用户Id
	 * @return
	 */
	public String getTaskDetailInfoOfGraphElement(BasicPicUnit bpu,
			String infoType, String userId) {
		
		//获取图元对应的资源信息
		Map resourceMap=this.getInfoWindowMsgOfPicUnitService(bpu);
		
		//获取资源的工单/任务单信息
		Map<String, List<Map<String,Object>>> totalMap=this.getWorkOrderAndTaskOrderOfGraphElementService(bpu,userId);
		//获取忙闲任务数信息
		//TODO 去除BusyOrNotMap
		Map<String,String> busyOrNotMap=this.getBusyOrNotTaskByPicUnitService(bpu);
		//获取当前登录人的工作
		Map<String,Map> gisWorkMap=new HashMap<String,Map>();
		if(bpu.getType().equals(GisDispatchGraphConstant.RESOURCE_STATION)){ //只有基站才显示工作列表
			gisWorkMap=this.getWorkListByUserService(userId);
		}
		
		String json="";
		//信息浮窗展示的数据格式
		if("resourceInfoWindow".equals(infoType)){
			json=GisDispatchJSONTools.transferResourceAndTaskInfoToJson(resourceMap, totalMap,busyOrNotMap,gisWorkMap);
		}else{	//连线派发的工单树数据格式
			json=GisDispatchJSONTools.transferResourceAndTaskInfoToJsonOfWorkOrderTree(resourceMap, totalMap,busyOrNotMap,gisWorkMap);
		}
		return json;
	}

	/**
	 * 按条件搜索资源
	 * @param strSearchType 搜索类型
	 * @param searchName 搜索名称
	 * @param geLatLng 图元经纬度
	 * @param distance 半径
	 * @param plm 图层管理器
	 * @return
	 */
	public String getResourceByConditionsService(String strSearchType,String searchName,LatLng geLatLng,long distance,PicLayerManager plm) {
		StringBuffer sb=new StringBuffer();
		//若类型不为空，则根据类型、名称搜索
		if(strSearchType!=null && !"".equals(strSearchType)){
			String[] searchType=strSearchType.split(",");
			Map<String,String> searchTypeMap=new HashMap<String,String>();
			if(searchType!=null && searchType.length>0){
				for(String t:searchType){
					searchTypeMap.put(t, t);
				}
			}
			// 搜索窗口
			LatLngBounds searchBounds = null;
			if(geLatLng!=null){
				// 计算窗口
				double neLng = LatLngHelper.moveEast(geLatLng.getLatitude(),
						geLatLng.getLongitude(), distance);
				double neLat = LatLngHelper.moveNorth(geLatLng.getLatitude(),
						geLatLng.getLongitude(), distance);
				double swLng = LatLngHelper.moveEast(geLatLng.getLatitude(),
						geLatLng.getLongitude(), -distance);
				double swLat = LatLngHelper.moveNorth(geLatLng.getLatitude(),
						geLatLng.getLongitude(), -distance);
				searchBounds = new LatLngBounds(new LatLng(swLat, swLng),new LatLng(neLat, neLng));
			}
			
			Map<Long, UserLayerTypeSetting> ultsMap=plm.getUserLayerTypeSettings();
			
			if(ultsMap!=null && !ultsMap.isEmpty()){
				sb.append("{");
				//若类型不为空，则根据该类资源信息搜索
				if(strSearchType!=null && !"".equals(strSearchType)){
					for(Map.Entry<Long, UserLayerTypeSetting> entry:ultsMap.entrySet()){
						UserLayerTypeSetting ults=entry.getValue();
						PicUnitType put=ults.getPicUnitType();
						if(searchTypeMap.get(put.getCode())!=null){
							String type=searchTypeMap.get(put.getCode());
							PicLayer layer=put.getPicLayer();
							if(layer!=null){
								sb.append(type+":");
								if(type.equals(GisDispatchGraphConstant.RESOURCE_HUMAN)){
									String searchHumanDuty = ServletActionContext.getRequest().getParameter("searchHumanDuty");
									String searchHumanTaskCount = ServletActionContext.getRequest().getParameter("searchHumanTaskCount");
									//参数集合
									Map conditions = new HashMap();
									//封装参数
									conditions.put("isDuty", searchHumanDuty);
									conditions.put("taskCount", searchHumanTaskCount);
									//search in layer
									String json = this.searchResourceInPicLayer(layer, type, searchName, conditions);
									sb.append(json);
								}else if(type.equals(GisDispatchGraphConstant.RESOURCE_CAR)){
									String searchCarStatu = ServletActionContext.getRequest().getParameter("searchCarStatu");
									String searchCarDuty = ServletActionContext.getRequest().getParameter("searchCarDuty");
									String searchCarTaskCount = ServletActionContext.getRequest().getParameter("searchCarTaskCount");
									//封装参数
									Map conditions = new HashMap();
									conditions.put("carState", searchCarStatu);
									conditions.put("isDuty", searchCarDuty);
									conditions.put("taskCount", searchCarTaskCount);
									//search in layer
									String json = this.searchResourceInPicLayer(layer, type, searchName, conditions);
									sb.append(json);
								}else if(type.equals(GisDispatchGraphConstant.RESOURCE_STATION)){
									//判断是否是通过基站来获取站址
									String searchBsWoCount = ServletActionContext.getRequest().getParameter("searchBsWoCount");
									String searchBsGrade = ServletActionContext.getRequest().getParameter("searchBsGrade");
									//封装参数
									Map conditions = new HashMap();
									conditions.put("taskCount", searchBsWoCount);
									conditions.put("stationGrade", searchBsGrade);
									//search in layer
									String json = this.searchResourceInPicLayer(layer, type, searchName, conditions);
//									String json = searchPepleInPicLayer(layer, searchBounds , type , searchName);
									sb.append(json);
								}else{
									String json = searchPepleInPicLayer(layer, searchBounds , type , searchName);
									sb.append(json);
								}
								sb.append(",");
							}
						}
					}
				}else{
					//根据名称、范围进行全文检索
					for(Map.Entry<Long, UserLayerTypeSetting> entry:ultsMap.entrySet()){
						UserLayerTypeSetting ults=entry.getValue();
						PicUnitType put=ults.getPicUnitType();
						PicLayer layer=put.getPicLayer();
						if(layer!=null){
							sb.append(put.getCode()+":");
							String json = searchPepleInPicLayer(layer, searchBounds , put.getCode() , searchName);
							sb.append(json);
							sb.append(",");
						}
					}
				}
				sb.delete(sb.length()-1, sb.length());
				sb.append("}");
			}
		}else{
			//根据名称、范围进行全文检索
			// 计算窗口
			LatLngBounds searchBounds = null;
			if(geLatLng!=null){
				double neLng = LatLngHelper.moveEast(geLatLng.getLatitude(),
						geLatLng.getLongitude(), distance);
				double neLat = LatLngHelper.moveNorth(geLatLng.getLatitude(),
						geLatLng.getLongitude(), distance);
				double swLng = LatLngHelper.moveEast(geLatLng.getLatitude(),
						geLatLng.getLongitude(), -distance);
				double swLat = LatLngHelper.moveNorth(geLatLng.getLatitude(),
						geLatLng.getLongitude(), -distance);
				// 搜索窗口
//				searchBounds = new LatLngBounds(new LatLng(swLat, swLng),new LatLng(neLat, neLng));
			}
			Map<Long, UserLayerTypeSetting> ultsMap=plm.getUserLayerTypeSettings();
			
			if(ultsMap!=null && !ultsMap.isEmpty()){
				sb.append("{");
				for(Map.Entry<Long, UserLayerTypeSetting> entry:ultsMap.entrySet()){
					UserLayerTypeSetting ults=entry.getValue();
					PicUnitType put=ults.getPicUnitType();
					PicLayer layer=put.getPicLayer();
					if(layer!=null){
						sb.append(put.getCode()+":");
						String json = searchPepleInPicLayer(layer, searchBounds , put.getCode() , searchName);
						sb.append(json);
						sb.append(",");
					}
				}
				sb.delete(sb.length()-1, sb.length());
				sb.append("}");
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * 根据条件在图元内搜索资源
	 * @param pl
	 * @param targetGeType
	 * @param targetGeName
	 * @param conditions
	 * @return
	 */
	private String searchResourceInPicLayer(PicLayer pl,String targetGeType,String targetGeName,Map conditions){
		String isDuty = (String)conditions.get("isDuty");
		String taskCount = (String)conditions.get("taskCount");
		String carState = (String)conditions.get("carState");
		String stationGrade = (String)conditions.get("stationGrade");
		int i = 0;

		StringBuilder json = new StringBuilder();
		json.append("[");
		
		// 查看可见图元
		Map<String, BasicPicUnit> vbpus = pl.getVisiblePicUnitMap();
		if (vbpus != null && !vbpus.isEmpty()) {
			for (String bpuKey : vbpus.keySet()) {
				BasicPicUnit bpu = vbpus.get(bpuKey);
				String bpuId = bpu.getId();
				if (targetGeType.equals(bpu.getType()) && bpu.getName().indexOf(targetGeName)!=-1) {
					//如果是人员的话，带上电话和性别
					if(GisDispatchGraphConstant.RESOURCE_HUMAN.equals(bpu.getType())){
						// 不满足条件则更改变量的值
						boolean taskRes = true;
						boolean dutyRes = true;
						if(taskCount!=null && !"".equals(taskCount)){
							long taskOrderCount = gisDaiFactory.getWorkManageService().getTaskOrderCountByObjectTypeAndObjectId("people", bpuId);
							if(taskCount.equals("0") && taskOrderCount!=0){
								taskRes = false;
							}else if(taskCount.equals("5") && taskOrderCount>5){
								//5个任务以下
								taskRes = false;
							}else if(taskCount.equals("6") && taskOrderCount<6){
								//5个任务以上
								taskRes = false;
							}
						}
						if(isDuty!=null && !"".equals(isDuty)){
							boolean isDutyToday = gisDaiFactory.getStaffDutyService().checkIsDutyToday(bpuId);
							//1:值班 0:不值班
							if(isDuty.equals("1") && !isDutyToday){
								dutyRes = false;
							}else if(isDuty.equals("0") && isDutyToday){
								dutyRes = false;
							}
						}
						if(taskRes && dutyRes){
							//获取任务数
							TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, null);
							bpu.setTaskInfo(ti);
							json.append(bpu.toJson());
							Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
							//直接在这里加，免得改其它类的代码
							json=json.delete(json.length()-1, json.length()).append(",sex:'"+map.get("sex")+"',phone:'"+map.get("phone")+"'}");
							json.append(",");
							i++;
						}
					}else if(GisDispatchGraphConstant.RESOURCE_STATION.equals(bpu.getType())){
						//站址
						boolean taskRes = true;
						boolean gradeRes = true;
						if(taskCount!=null && !"".equals(taskCount)){
							long taskOrderCount = gisDaiFactory.getWorkManageService().getTaskOrderCountByObjectTypeAndObjectId(STATION_FLAG, bpuId);
							if(taskCount.equals("0") && taskOrderCount!=0){
								taskRes = false;
							}else if(taskCount.equals("1") && taskOrderCount<1){
								//1个任务以上
								taskRes = false;
							}
						}
						if(stationGrade!=null && !"".equals(stationGrade)){
							
						}
						if(taskRes && gradeRes){
							json.append(bpu.toJson());
							json.append(",");
							i++;
						}
					}else if(GisDispatchGraphConstant.RESOURCE_CAR.equals(bpu.getType())){
						// 不满足条件则更改变量的值
						boolean taskRes = true;
						boolean dutyRes = true;
						boolean stateRes = true;
						if(taskCount!=null && !"".equals(taskCount)){
							long taskOrderCount = gisDaiFactory.getWorkManageService().getTaskOrderCountByObjectTypeAndObjectId("car", bpuId);
							if(taskCount.equals("0") && taskOrderCount!=0){
								taskRes = false;
							}else if(taskCount.equals("5") && taskOrderCount<5){
								//5个任务以下
								taskRes = false;
							}else if(taskCount.equals("6") && taskOrderCount<6){
								//5个任务以上
								taskRes = false;
							}
						}
						if(isDuty!=null && !"".equals(isDuty)){
							boolean isDutyToday = gisDaiFactory.getStaffDutyService().checkIsDutyToday(bpuId);
							//1:值班 0:不值班
							if(isDuty.equals("1") && !isDutyToday){
								dutyRes = false;
							}else if(isDuty.equals("0") && isDutyToday){
								dutyRes = false;
							}
						}
						if(taskRes && dutyRes && stateRes){
							//获取任务数
							TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, null);
							bpu.setTaskInfo(ti);
							json.append(bpu.toJson());
							//获取车辆司机信息
							Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
							if(map!=null){
								json = json.delete(json.length()-1,json.length()).append(",driverName:'"+map.get("driverName")+"',driverPhone:'"+map.get("driverPhone")+"',carType:'"+map.get("carType")+"'}");
							}
							json.append(",");
							i++;
						}
					}
				}
			}
		}
		
		if(i>0){
			json.delete(json.length()-1, json.length());
		}

		int j = 0;
		// 查看不可见图元
		Map<String, BasicPicUnit> hbpus = pl.getHiddenPicUnitMap();
		if (hbpus != null && !hbpus.isEmpty()) {
			for (String bpuKey : hbpus.keySet()) {
				BasicPicUnit bpu = hbpus.get(bpuKey);
				String bpuId = bpu.getId();
				if (targetGeType.equals(bpu.getType()) && bpu.getName().indexOf(targetGeName)!=-1) {
					if(j==0 && i>0){
						json.append(",");
					}
					//如果是人员的话，带上电话和性别
					if(GisDispatchGraphConstant.RESOURCE_HUMAN.equals(bpu.getType())){
						// 不满足条件则更改变量的值
						boolean taskRes = true;
						boolean dutyRes = true;
						if(taskCount!=null && !"".equals(taskCount)){
							long taskOrderCount = gisDaiFactory.getWorkManageService().getTaskOrderCountByObjectTypeAndObjectId("people", bpuId);
							if(taskCount.equals("0") && taskOrderCount!=0){
								taskRes = false;
							}else if(taskCount.equals("5") && taskOrderCount>5){
								//5个任务以下
								taskRes = false;
							}else if(taskCount.equals("6") && taskOrderCount<6){
								//5个任务以上
								taskRes = false;
							}
						}
						if(isDuty!=null && !"".equals(isDuty)){
							boolean isDutyToday = gisDaiFactory.getStaffDutyService().checkIsDutyToday(bpuId);
							//1:值班 0:不值班
							if(isDuty.equals("1") && !isDutyToday){
								dutyRes = false;
							}else if(isDuty.equals("0") && isDutyToday){
								dutyRes = false;
							}
						}
						if(taskRes && dutyRes){
							//获取任务数
							TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, null);
							bpu.setTaskInfo(ti);
							json.append(bpu.toJson());
							Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
							//直接在这里加，免得改其它类的代码
							json=json.delete(json.length()-1, json.length()).append(",sex:'"+map.get("sex")+"',phone:'"+map.get("phone")+"'}");
							json.append(",");
							j++;
						}
					}else if(GisDispatchGraphConstant.RESOURCE_STATION.equals(bpu.getType())){
						//站址
						boolean taskRes = true;
						boolean gradeRes = true;
						if(taskCount!=null && !"".equals(taskCount)){
							long taskOrderCount = gisDaiFactory.getWorkManageService().getTaskOrderCountByObjectTypeAndObjectId(STATION_FLAG, bpuId);
							if(taskCount.equals("0") && taskOrderCount!=0){
								taskRes = false;
							}else if(taskCount.equals("1") && taskOrderCount<1){
								//1个任务以上
								taskRes = false;
							}
						}
						if(stationGrade!=null && !"".equals(stationGrade)){
							
						}
						if(taskRes && gradeRes){
							json.append(bpu.toJson());
							json.append(",");
							j++;
						}
					}else if(GisDispatchGraphConstant.RESOURCE_CAR.equals(bpu.getType())){
						// 不满足条件则更改变量的值
						boolean taskRes = true;
						boolean dutyRes = true;
						boolean stateRes = true;
						if(taskCount!=null && !"".equals(taskCount)){
							long taskOrderCount = gisDaiFactory.getWorkManageService().getTaskOrderCountByObjectTypeAndObjectId("car", bpuId);
							if(taskCount.equals("0") && taskOrderCount!=0){
								taskRes = false;
							}else if(taskCount.equals("5") && taskOrderCount<5){
								//5个任务以下
								taskRes = false;
							}else if(taskCount.equals("6") && taskOrderCount<6){
								//5个任务以上
								taskRes = false;
							}
						}
						if(isDuty!=null && !"".equals(isDuty)){
							Map<String,String> map = new HashMap<String, String>();
							map.put("carNumber", bpu.getName());
							boolean isDutyToday = gisDaiFactory.getCardispatchDutyService().checkIsPlanDutyToday(map);
							//1:值班 0:不值班
							if(isDuty.equals("1") && !isDutyToday){
								dutyRes = false;
							}else if(isDuty.equals("0") && isDutyToday){
								dutyRes = false;
							}
						}
						if(carState!=null && !"".equals(carState)){
							String terminalState = "";
							Map pMap = new HashMap();
							pMap.put("carId", bpu.getId());
							List<Map<String, Object>> terminalList = gisDaiFactory.getCardispatchTerminalDao().findTerminalList(pMap);
							if(terminalList!=null && !terminalList.isEmpty()){
								Map<String, Object> tMap = terminalList.get(0);
								terminalState = tMap.get("terminalState")+"";
							}
							if(!"".equals(terminalState) && !terminalState.equals(carState)){
								stateRes = false;
							}
						}
						if(taskRes && dutyRes && stateRes){
							//获取任务数
							TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, null);
							bpu.setTaskInfo(ti);
							json.append(bpu.toJson());
							//获取车辆司机信息
							Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
							if(map!=null){
								json = json.delete(json.length()-1,json.length()).append(",driverName:'"+map.get("driverName")+"',driverPhone:'"+map.get("driverPhone")+"',carType:'"+map.get("carType")+"'}");
							}
							json.append(",");
							j++;
						}
					}
				}
			}
		}
		
		if (j > 0) {
			json = json.delete(json.length() - 1, json.length());// 删除逗号
		}

		json.append("]");

		return json.toString();
	}
	
	
	/**
	 * 获取某个图层的在指定经纬度范围内的“人”的json格式
	 * 
	 * @param pl
	 * @param searchBounds
	 * @return Mar 28, 2012 10:30:14 AM gmh
	 */
	private String searchPepleInPicLayer(PicLayer pl, LatLngBounds searchBounds,String targetGeType,String targetGeName) {
		int i = 0;

		StringBuilder json = new StringBuilder();
		json.append("[");// 人员开始
		
		//System.out.println("图层key=="+pl.getId()+",图层名称=="+pl.getName());
		
		// 查看可见图元
		Map<String, BasicPicUnit> vbpus = pl.getVisiblePicUnitMap();
		if (vbpus != null && !vbpus.isEmpty()) {
			for (String bpuKey : vbpus.keySet()) {
				BasicPicUnit bpu = vbpus.get(bpuKey);
				//-----------chen.lb修改,判断搜索范围是否为空 BEGIN
				if(searchBounds!=null){
					if (targetGeType.equals(bpu.getType()) && bpu.getLatlng().containedIn(searchBounds) && bpu.getName().indexOf(targetGeName)!=-1) {
						// 属于查找的目标类型，且在指定搜索窗口内
						json.append(bpu.toJson());
						//如果是人员的话，带上电话和性别
						if(GisDispatchGraphConstant.RESOURCE_HUMAN.equals(bpu.getType())){
							//获取任务数
							TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, null);
							bpu.setTaskInfo(ti);
							Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
							//直接在这里加，免得改其它类的代码
							json=json.delete(json.length()-1, json.length()).append(",sex:'"+map.get("sex")+"',phone:'"+map.get("phone")+"'}");
						}else if(GisDispatchGraphConstant.RESOURCE_STATION.equals(bpu.getType())){
							//获取站址信息（地址）
							
						}else if(GisDispatchGraphConstant.RESOURCE_CAR.equals(bpu.getType())){
							//获取任务数
							TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, null);
							bpu.setTaskInfo(ti);
							//获取车辆司机信息
							Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
							if(map!=null){
								json = json.delete(json.length()-1,json.length()).append(",driverName:'"+map.get("driverName")+"',driverPhone:'"+map.get("driverPhone")+"',carType:'"+map.get("carType")+"'}");
							}
						}
						json.append(",");
						i++;
					}
				}else{
					if (targetGeType.equals(bpu.getType()) && bpu.getName().indexOf(targetGeName)!=-1) {
						// 属于查找的目标类型，且在指定搜索窗口内
						json.append(bpu.toJson());
						//如果是人员的话，带上电话和性别
						if(GisDispatchGraphConstant.RESOURCE_HUMAN.equals(bpu.getType())){
							//获取任务数
							TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, null);
							bpu.setTaskInfo(ti);
							Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
							//直接在这里加，免得改其它类的代码
							json=json.delete(json.length()-1, json.length()).append(",sex:'"+map.get("sex")+"',phone:'"+map.get("phone")+"'}");
						}else if(GisDispatchGraphConstant.RESOURCE_STATION.equals(bpu.getType())){
							//获取站址信息（地址）
							
						}else if(GisDispatchGraphConstant.RESOURCE_CAR.equals(bpu.getType())){
							//获取任务数
							TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, null);
							bpu.setTaskInfo(ti);
							//获取车辆司机信息
							Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
							if(map!=null){
								json = json.delete(json.length()-1,json.length()).append(",driverName:'"+map.get("driverName")+"',driverPhone:'"+map.get("driverPhone")+"',carType:'"+map.get("carType")+"'}");
							}
						}
						json.append(",");
						i++;
					}
				}
			}
		}
		
		if(i>0){
			json.delete(json.length()-1, json.length());
		}

		int j = 0;
		// 查看不可见图元
		Map<String, BasicPicUnit> hbpus = pl.getHiddenPicUnitMap();
		if (hbpus != null && !hbpus.isEmpty()) {
			for (String bpuKey : hbpus.keySet()) {
				BasicPicUnit bpu = hbpus.get(bpuKey);
				//---------------chen.lb 添加,判断搜索范围是否为空 BEGIN
				if(searchBounds!=null){
					if (targetGeType.equals(bpu.getType()) && bpu.getLatlng().containedIn(searchBounds) && bpu.getName().indexOf(targetGeName)!=-1) {
						// 属于查找的目标类型，且在指定搜索窗口内
						if(j==0 && i>0){
							json.append(",");
						}
						json.append(bpu.toJson());
						//如果是人员的话，带上电话和性别
						if(GisDispatchGraphConstant.RESOURCE_HUMAN.equals(bpu.getType())){
							//获取任务数
							TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, null);
							bpu.setTaskInfo(ti);
							Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
							//直接在这里加，免得改其它类的代码
							json=json.delete(json.length()-1, json.length()).append(",sex:'"+map.get("sex")+"',phone:'"+map.get("phone")+"'}");
						}else if(GisDispatchGraphConstant.RESOURCE_STATION.equals(bpu.getType())){
							//获取站址信息（地址）
							
						}else if(GisDispatchGraphConstant.RESOURCE_CAR.equals(bpu.getType())){
							//获取任务数
							TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, null);
							bpu.setTaskInfo(ti);
							//获取车辆司机信息
							Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
							if(map!=null){
								json = json.delete(json.length()-1,json.length()).append(",driverName:'"+map.get("driverName")+"',driverPhone:'"+map.get("driverPhone")+"',carType:'"+map.get("carType")+"'}");
							}
						}
						json.append(",");
						j++;
					}
				}else{
					if (targetGeType.equals(bpu.getType()) && bpu.getName().indexOf(targetGeName)!=-1) {
						if(j==0 && i>0){
							json.append(",");
						}
						// 属于查找的目标类型，且在指定搜索窗口内
						json.append(bpu.toJson());
						//如果是人员的话，带上电话和性别
						if(GisDispatchGraphConstant.RESOURCE_HUMAN.equals(bpu.getType())){
							//获取任务数
							TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, null);
							bpu.setTaskInfo(ti);
							Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
							//直接在这里加，免得改其它类的代码
							json=json.delete(json.length()-1, json.length()).append(",sex:'"+map.get("sex")+"',phone:'"+map.get("phone")+"'}");
						}else if(GisDispatchGraphConstant.RESOURCE_STATION.equals(bpu.getType())){
							//获取站址信息（地址）
							
						}else if(GisDispatchGraphConstant.RESOURCE_CAR.equals(bpu.getType())){
							//获取任务数
							TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, null);
							bpu.setTaskInfo(ti);
							//获取车辆司机信息
							Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
							if(map!=null){
								json = json.delete(json.length()-1,json.length()).append(",driverName:'"+map.get("driverName")+"',driverPhone:'"+map.get("driverPhone")+"',carType:'"+map.get("carType")+"'}");
							}
						}
						json.append(",");
						j++;
					}
				}
				//----------------chen.lb END
			}
		}
		
		if (j > 0) {
			json = json.delete(json.length() - 1, json.length());// 删除逗号
		}

		json.append("]");// 人员结束

		return json.toString();
	}

	/**
	 * 保存图层树设置 
	 * @param strUserConfigTree 用户图层树设置
	 * @param userId 用户Id
	 * @param currentVisibleKM 当前可见公里数
	 * @return
	 */
	public String savePicLayerConfigService(String strUserConfigTree,String userId,Double currentVisibleKM) {
		// 主要是同步后台的数据
		HttpSession session = ServletActionContext.getRequest().getSession();
		// 分解前台参数
		// 参数格式：图层id:true,图层id-图元类型id:false,图层id-图元类型id-图元脚标id：true,
		List<UserConfigTree> userConfigList = this.parseUserConfigTree(strUserConfigTree);
		// 看用户是否传递了有效的设置参数
		if (userConfigList != null && userConfigList.size() > 0) {
			PicLayerManager plm = (PicLayerManager) session.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY);

			// 用户的图层设置信息，关联的图层id为key
			Map<Long, UserLayerSetting> userLayerSettings = plm.getUserLayerSettings();
			
			// 用户的图元类型设置信息，关联的图元类型的id为key
			Map<Long, UserLayerTypeSetting> userLayerTypeSettings = plm.getUserLayerTypeSettings();
			
			// 用户的脚标设置信息，关联的脚标id为key
			Map<Long, UserLittleIconSetting> userLittleIconSettings = plm.getUserLittleIconSettings();

			String targetType="";
			String targetKey="";
			boolean isShow=false;
			boolean findTarget=false;
			PicLayer pl = null;
			List<PicUnitType> puts=null;
			for (UserConfigTree uct : userConfigList) {
				// 更新数据库的设置
				// 更新内存设置
				targetType = uct.getTargetType();
				targetKey = uct.getTargetKey();
				isShow = uct.getIsShow();
				pl = null;
				findTarget=false;
				puts = null;

				long dbId = 0;
				if (GisDispatchGraphConstant.GRAPHTYPE_LAYER.equals(targetType)) {	//改变目标是图层
					pl = plm.findAPicLayerByKey(targetKey);
					pl.modifyPicUnitTypeShowOrNot(currentVisibleKM);	//che.yd添加
					
					if (pl != null) {
						dbId = pl.getId();// 获取图层数据库id2
						userLayerSettings.get(dbId).setShowOrNot(isShow);// 更新内存图层树设置用户设置
						userLayerSettings.get(dbId).getPicLayer().setShow(isShow);//更新关联的图层的可见情况
						findTarget=true;
						
						//处理可见、隐藏队列
						if(isShow ){//需要可见,并且图层本身不可见
							this.fillPicLayerService(userId, pl);// 填充该图层，由不可见到可见，需要到数据库去同步一次数据
							plm.addToNewVisibleLayers(pl);//添加该图层到新可见列表
							plm.deleteAHiddenPicLayer(pl);//从隐藏列表删除
						}else{
							plm.addToNewHiddenLayers(pl);//添加该图层进新隐藏队列
							plm.deleteAVisiblePicLayer(pl);//从可见队列删除
						}
					} else {
						continue;
					}
				} else if (GisDispatchGraphConstant.GRAPHTYPE_ELEMENTTYPE.equals(targetType)) {	//改变目标是图元类型
					String plKey=uct.getPicLayerKey();	//che.yd添加的
					pl = plm.findAPicLayerByKey(plKey);
					pl.modifyPicUnitTypeShowOrNot(currentVisibleKM);	//che.yd添加的
					
					if(pl==null){
						continue;
					}
					puts = pl.getPicUnitTypes();
					if (puts != null && puts.size() > 0) {
						for (PicUnitType put : puts) {
							if (put.getKey().equals(targetKey)) {
								dbId = put.getId();// 获取图元类型数据库id
								userLayerTypeSettings.get(dbId).setVisible(isShow);// 更新内存图层树设置用户设置
								userLayerTypeSettings.get(dbId).getPicUnitType().setNeedShow(isShow);	//更新关联的图元类型的可见情况
								findTarget=true;
								
								//处理可见、隐藏队列
								if(isShow ){//需要可见,并且图元类型本身不可见
									if(put.isGonglishuVisible()){	//che.yd 添加，保存图层设置的时候，也要判断图元类型是否在可见公里数范围内
										//使得某个图元类型可见
										pl.enableAPicUnitType(put);
										
									}
								}else{
									pl.disableAPicUnitType(put);//使得某种图元类型不可见
								}
								break;
							}
						}
					}
				}else if(GisDispatchGraphConstant.GRAPHTYPE_LITTLTICON.equals(targetType)){//改变目标是脚标
					pl = plm.findAPicLayerByKey(uct.getPicLayerKey());
					puts = pl.getPicUnitTypes();
					if (puts != null && puts.size() > 0) {
						for (PicUnitType put : puts) {
							if (put.getKey().equals(uct.getPicUnitTypeKey())) {
								List<LittleIcon> littleIcons=put.getLittleIcons();
								if(littleIcons!=null && littleIcons.size()>0){
									for(LittleIcon li:littleIcons){
										if(li.getKey().equals(targetKey)){
											dbId = li.getId();// 获取图元类型数据库id
											userLittleIconSettings.get(dbId).setShowOrNot(isShow);// 更新内存图层树设置用户设置
											userLittleIconSettings.get(dbId).getLittleIcon().setNeedShow(isShow);	//更新关联的脚标的可见情况
											findTarget=true;
											//不影响图元是否显示（只是影响显示的样子，不用更新可见、隐藏图元队列）
											break;
										}
									}
								}
								if(findTarget){
									break;
								}
							}
						}
					}
				}
				// 更新数据库用户图层树设置
				if (findTarget) {
					this.savePicLayerConfigService(userId,targetType, dbId, isShow);
				}
			}
			
			String resultJson = plm.toLeftPicTreeChangeJson();
			
			//清空新增可见图层列表、新增隐藏图层列表、新增可见图元列表、新增隐藏图元列表
			
			Map<String,PicLayer> visiblePls=plm.getVisiblePicLayers();
			for(String key:visiblePls.keySet()){
				
				pl=visiblePls.get(key);
				
				//合并新增可见图元到可见图元列表
				if(pl.getNewVisiblePicUnitMap()!=null && pl.getNewVisiblePicUnitMap().size()>0){
					pl.getVisiblePicUnitMap().putAll(pl.getNewVisiblePicUnitMap());
				}
				
				//合并新增隐藏图元到隐藏图元列表
				if(pl.getNewHiddenPicUnitMap()!=null && pl.getNewHiddenPicUnitMap().size()>0){
					pl.getHiddenPicUnitMap().putAll(pl.getNewHiddenPicUnitMap());
				}
				
				pl.clearNewHiddenList();	//清空新增隐藏队列
				pl.clearNewVisibleList();	//清空新增可见队列
			}
			
			//合并新增可见图层到可见图层队列
			if(plm.getNewVisiblePicLayers()!=null && plm.getNewVisiblePicLayers().size()>0){
				plm.getVisiblePicLayers().putAll(plm.getNewVisiblePicLayers());
				plm.getNewVisiblePicLayers().clear();
			}
			
			//合并新增隐藏图层到隐藏图层队列
			if(plm.getNewHiddenPicLayers()!=null && plm.getNewHiddenPicLayers().size()>0){
				plm.getHiddenPicLayers().putAll(plm.getNewHiddenPicLayers());
				plm.getNewHiddenPicLayers().clear();
			}
			return resultJson;
		}else{
			String resultJson = "{hasChanged:false}";
			return resultJson;
		}
	}
	
	/**
	 * 将前台传来的图层改变参数，转换为对象模型
	 * 
	 * @param strUserConfigTree
	 * @return Mar 30, 2012 10:41:47 AM gmh
	 */
	private List<UserConfigTree> parseUserConfigTree(String strUserConfigTree) {
		String[] updateTotalCount = strUserConfigTree.split(",");

		// 遍历用户的左边树设置
		List<UserConfigTree> userConfigList = new ArrayList<UserConfigTree>();
		if (updateTotalCount != null && updateTotalCount.length > 0) {
			for (String str : updateTotalCount) {
				UserConfigTree uct = new UserConfigTree(); // 根据参数的格式，把用户设置的信息装进变量里面
				String[] kv=str.split(":");
				if(kv.length!=2){
					continue;
				}
				String[] param = kv[0].split("-"); //分解key
				String targetType = "";
//				long targetId = 0;
				boolean isShow = false;

				
				String targetKey=param[param.length-1];//最后一个是目标key
				isShow = Boolean.valueOf(kv[1]);

				if (param.length == 1) { // sample:【6:true】
					targetType = GisDispatchGraphConstant.GRAPHTYPE_LAYER;
					uct.setTargetType(targetType);		//che.yd-----补上
					uct.setTargetKey(targetKey);
					uct.setPicLayerKey(targetKey);
					uct.setIsShow(isShow);
				} else if (param.length == 2) { // sample:【6-4:true】
					targetType = GisDispatchGraphConstant.GRAPHTYPE_ELEMENTTYPE;
					uct.setTargetType(targetType);		//che.yd-----补上
					uct.setPicLayerKey(param[0]);
					uct.setPicUnitTypeKey(targetKey);
					//uct.setTargetKey(str.substring(2, 3));
					uct.setTargetKey(targetKey);
					uct.setIsShow(isShow);
				} else if (param.length == 3) { // sample:【6-9-4:true】
					targetType = GisDispatchGraphConstant.GRAPHTYPE_LITTLTICON;
					uct.setTargetType(targetType);		//che.yd-----补上
					uct.setPicLayerKey(param[0]);
					uct.setPicUnitTypeKey(param[1]);
					uct.setLittleIconKey(targetKey);
					uct.setTargetKey(targetKey);
					uct.setIsShow(isShow);
				}

				userConfigList.add(uct);
			}
		}
		return userConfigList;
	}

	/**
	 * 获取特定图元信息
	 * @param picUnitTypeCode 图元类型
	 * @param resourceId 图元Id
	 * @return
	 */
	public String getSpecifiedResourceInfoService(String picUnitTypeCode,
			String resourceId,PicLayerManager plm) {
		String resultJson = "";
		// 通过类型找图层
		PicLayer pl = plm.findAPicLayerFromPicUnitTypeCode(picUnitTypeCode);
		if (pl == null) {
			resultJson = "{flag:'fail',msg:'没有数据'}";
			return resultJson;
		} else {
			BasicPicUnit bpu = pl.findABasicPicUnitByKey(picUnitTypeCode + "_" + resourceId);
			if (bpu == null) {
				resultJson = "{flag:'fail',msg:'没有数据'}";
				return resultJson;
			} else {
				// 构建返回json信息
				resultJson = "{";
				resultJson += "key:'" + pl.getKey() + "',";
				resultJson += "layerName:'" + pl.getName() + "',";
				resultJson += "geList:[";
				resultJson += bpu.toJson();
				resultJson += "]";
				resultJson += "}";
				return resultJson;
			}
		}
	}

	/**
	 * 验证连线派发信息
	 * @param WOID 工单Id
	 * @param assignStaffId 派发人员Id
	 * @return
	 */
	public Map checkAssignTaskOnPicUnitService(String WOID, String assignStaffId) {
		Map resMap = new HashMap();
		String assignRes = "";
		String assignInfo = "";
		String assignUrl = "";
		//获取要派发的工单信息
		WorkmanageWorkorder workOrderEntity = gisDaiFactory.getWorkManageService().getWorkOrderEntity(WOID);
		if(workOrderEntity!=null){
			Integer workOrderStatus = workOrderEntity.getStatus();
			if(workOrderStatus == WorkManageConstant.WORKORDER_WAIT4ACCEPT){
				assignRes = "fail";
				assignInfo = "派发提示：不能派发状态为待受理的工单！是否先受理该工单？";
				assignUrl = "urgentrepair/loadUrgentRepairWorkOrderPageForGISAction?WOID="+WOID+"&accountForGIS="+assignStaffId+"&entryType=GIS";
			}else{
				assignRes = "success";
				assignInfo = "该人员可派发给该工单！";
				assignUrl = "urgentrepair/loadUrgentRepairWorkOrderPageForGISAction?WOID="+WOID+"&accountForGIS="+assignStaffId+"&entryType=GIS";
			}
		}else{
			assignRes = "error";
			assignInfo = "派发失败！获取不到待派发工单信息！";
		}
		resMap.put("assignRes", assignRes);
		resMap.put("assignInfo", assignInfo);
		resMap.put("assignUrl", assignUrl);
		return resMap;
	}

	/**
	 * 增加区域活动次数
	 * @param areaId
	 */
	public void addAreaActiveNumber(long areaId) {
		GisDispatch_ActiveArea activeArea = gisDispatchDao.getGisdispatchActiveAreaByAreaId(areaId);
		if(activeArea!=null){
			long activeNumber = activeArea.getActiveNumber();
			activeNumber+=1;
			activeArea.setActiveNumber(activeNumber);
			gisDispatchDao.updateGisdispatchActiveArea(activeArea);
		}else{
			activeArea = new GisDispatch_ActiveArea();
			activeArea.setAreaId(areaId);
			activeArea.setActiveNumber(1);
			gisDispatchDao.addGisdispatchActiveArea(activeArea);
		}
	}

	/**
	 * 获取监控资源
	 */
	public String getMonitorResourceService() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		String userId = (String)session.getAttribute(UserInfo.USERID);
		PicLayerManager plm = (PicLayerManager)session.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY);
		if(plm==null){
			return "{stationList:[],staffList:[],carList:[]}";
		}
		List<String> staffIdList = new ArrayList<String>();	//人员Id集合
		List<String> carIdList = new ArrayList<String>();	//车辆Id集合
		String stationJson = "stationList:[ ";
		String staffJson = "staffList:[ ";
		String carJson = "carList:[ ";
		//获取所有用户图层，判断获取图元信息
		Map<Long, UserLayerTypeSetting> ultsMap=plm.getUserLayerTypeSettings();
		for(Map.Entry<Long, UserLayerTypeSetting> entry : ultsMap.entrySet()){
			UserLayerTypeSetting ults = entry.getValue();
			PicUnitType put = ults.getPicUnitType();
			String code = put.getCode();
			//获取监控车辆列表
			if(code.equals(GisDispatchGraphConstant.RESOURCE_CAR) || code.equals(GisDispatchGraphConstant.RESOURCE_STATION)
					|| code.equals(GisDispatchGraphConstant.RESOURCE_HUMAN)){
				PicLayer layer = put.getPicLayer();
				if(layer!=null){
					Map<String, BasicPicUnit> apu = layer.getAllPicUnitMap();
					for (String str : apu.keySet()) {
						BasicPicUnit bpu = apu.get(str);
						String bpuId = bpu.getId();
						if(bpu.getType().equals(GisDispatchGraphConstant.RESOURCE_CAR)){
							if(!carIdList.contains(bpuId)){
								//获取任务数
								TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, userId);
								bpu.setTaskInfo(ti);
								String json = bpu.toJson();
								//获取车辆司机信息
								Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
								if(map!=null){
									json = json.substring(0,json.length()-1)+",driverName:'"+map.get("driverName")+"',driverPhone:'"+map.get("driverPhone")+"',carType:'"+map.get("carType")+"'}";
								}
								carJson += json+",";
								carIdList.add(bpuId);
							}
						}else if(bpu.getType().equals(GisDispatchGraphConstant.RESOURCE_HUMAN)){
							if(!staffIdList.contains(bpuId)){
								//获取任务数
								TaskInfo ti = this.getTaskInfoOfGraphElementService(bpu, userId);
								bpu.setTaskInfo(ti);
								String json = bpu.toJson();
								Map map=this.getInfoWindowMsgOfPicUnitService(bpu);
								//人员的话 加上性别、电话
								
								if(map!=null){
									json = json.substring(0,json.length()-1)+",sex:'"+map.get("sex")+"',phone:'"+map.get("phone")+"'}";
								}
								staffJson += json+",";
								staffIdList.add(bpuId);
							}
						}else if(bpu.getType().equals(GisDispatchGraphConstant.RESOURCE_STATION)){
							if(stationWorkOrderList!=null && !stationWorkOrderList.isEmpty()){
								for (WorkmanageCountWorkorderObject wc : stationWorkOrderList) {
									if(wc.getResourceId().equals(bpu.getId()) && wc.getTaskCount()>0){
										long taskCount = wc.getTaskCount();
										int qiangxiuNum = (int) taskCount;
										//创建子任务列表
										List<TaskDetailInfo> taskDetailInfos=new ArrayList<TaskDetailInfo>();
										//或取用户关于该脚标的设置情况，如是否显示
										Map<Long, UserLittleIconSetting> userLittleIconSettings=bpu.getPicUnitType().getPicLayer().getPicLayerManager().getUserLittleIconSettings();
										List<LittleIcon> icons = bpu.getPicUnitType().getLittleIcons();
										if (icons != null && icons.size() > 0) {
											for (LittleIcon icon : icons) {//对于该bpu的每一个脚标，都设置一个相应的任务详情
												UserLittleIconSetting ulis=userLittleIconSettings.get(icon.getId());
												TaskDetailInfo tdi = new TaskDetailInfo();
												tdi.setName(icon.getName());
												tdi.setLittleIcon(icon.getIcon());
												if(icon.getName().equals(GisDispatchGraphConstant.LITTLEICON_QIANGXIU)){
													tdi.setCount(qiangxiuNum);
												}else if(icon.getName().equals(GisDispatchGraphConstant.LITTLEICON_XUNJIAN)){
													tdi.setCount(0);
												}else if(icon.getName().equals(GisDispatchGraphConstant.LITTLEICON_XIUSHAN)){
													tdi.setCount(0);
												}
												tdi.setNeedShowLittleIcon(ulis.isShowOrNot());//用户是否设置了显示
												taskDetailInfos.add(tdi);
											}
										}
										TaskInfo taskInfo = new TaskInfo();
										taskInfo.setTotalTaskCount((int)taskCount);
										//加进任务总数里
										taskInfo.setTaskDetailInfo(taskDetailInfos);
										bpu.setTaskInfo(taskInfo);
										String json = bpu.toJson();
										stationJson += json+",";
									}
								}
							}
						}
					}
				}
			}
		}
		stationJson = stationJson.substring(0,stationJson.length()-1);
		staffJson = staffJson.substring(0,staffJson.length()-1);
		carJson = carJson.substring(0,carJson.length()-1);
		stationJson += "]";
		staffJson += "]";
		carJson += "]";
		//返回结果
		String resJson = "{"+stationJson+","+staffJson+","+carJson+"}";
		return resJson;
	}

	public NetworkResourceManageDao getNetworkResourceManageDao() {
		return networkResourceManageDao;
	}

	public void setNetworkResourceManageDao(
			NetworkResourceManageDao networkResourceManageDao) {
		this.networkResourceManageDao = networkResourceManageDao;
	}


	
	
}
