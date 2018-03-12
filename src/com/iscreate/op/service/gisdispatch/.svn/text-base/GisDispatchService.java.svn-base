package com.iscreate.op.service.gisdispatch;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.gisdispatch.BasicPicUnit;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_GraphElementType;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_GraphElementTypeAndMileSetting;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_GraphElementTypeSetting;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_GraphLayer;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_GraphLayerSetting;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_LittleIcon;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_LittleIconSetting;
import com.iscreate.op.pojo.gisdispatch.LatLng;
import com.iscreate.op.pojo.gisdispatch.LatLngBounds;
import com.iscreate.op.pojo.gisdispatch.PicLayer;
import com.iscreate.op.pojo.gisdispatch.PicLayerManager;
import com.iscreate.op.pojo.gisdispatch.TaskInfo;
import com.iscreate.op.pojo.gisdispatch.UserLayerSetting;
import com.iscreate.op.pojo.gisdispatch.UserLayerTypeSetting;
import com.iscreate.op.pojo.gisdispatch.UserLittleIconSetting;
import com.iscreate.op.pojo.publicinterface.Workorderassnetresource;


/**
 * @author che.yd
 * Gis调度接口Service
 */
public interface GisDispatchService {
	
	/**
	 * 获取用户的工单列表
	 * @param userId
	 * @param bizTypeCode 业务类型，如urgentrepair（抢修）、cardispatch（车辆调度）、resourcedispatch（物资调度）
	 * @param workOrderType
	 * @param paramMap 参数Map
	 * @return
	 */
	public List<Map<String, Object>> getUserWorkOrdersService(String userId,String bizTypeCode,String workOrderType,int pageIndex,int pageSize,Map paramMap);
	
	/**
	 * 获取用户的任务单列表
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserTaskOrdersService(String userId,String bizTypeCode,String taskOrderType,int pageIndex,int pageSize,Map paramMap);
	
	/**
	 * 获取工单关联的资源
	 * @param WOID
	 * @return
	 * Author gmh
	 * 2012-3-21 下午04:20:14
	 */
	public List<Workorderassnetresource> getWorkOrderAssResourcesService(String WOID);
	
	/**
	 * 获取任务单关联的资源列表
	 * @param TOID
	 * @return
	 * Author gmh
	 * 2012-3-21 下午04:21:01
	 */
	public List<Workorderassnetresource> getTaskOrderAssResourcesService(String TOID);
	
	/**
	 * 获取资源关联的任务数
	 * @param resource 资源：人,车,站址,管井
	 * @return
	 */
	public int getTaskCountOfResourceService(Map resource);
	
	/**
	 * 获取资源关联的任务数
	 * @param resourceType 资源类型
	 * @param resourceId 资源id
	 * @return
	 */
	public int getTaskCountOfResourceService(String resourceType,String resourceId);
	
	/**
	 * 填充某个图层
	 * @param userId 用户id
	 * @param picLayer 图层
	 */
	public void fillPicLayerService(String userId,PicLayer picLayer);
	
	
	
	/**
	 * 获取图元的任务信息：任务总数、任务类型
	 * @param bpu 图元
	 */
	public TaskInfo getTaskInfoOfGraphElementService(BasicPicUnit bpu,String userId);
	
	/**
	 * 获取某个图元关联的工单、任务单列表
	 * @param bpu 图元
	 * @return 工单、任务单列表
	 */
	public Map<String, List<Map<String,Object>>> getWorkOrderAndTaskOrderOfGraphElementService(BasicPicUnit bpu,String userId);
	
	/**
	 * 保存用户设置的图层or图元or脚标 是否显示
	 * @param userId
	 * @param targetId
	 * @param isShow
	 */
	public void savePicLayerConfigService(String userId,String targetType,long targetId,boolean isShow);
	
	/**
	 * 获取图元信息窗口的详细信息
	 * @param bpu
	 * @return Mar 28, 2012 12:05:51 PM gmh
	 */
	public Map getInfoWindowMsgOfPicUnitService(BasicPicUnit bpu);
	
	
	/**
	 * 获取图元忙闲任务数
	 * @param bpu
	 * @return
	 */
	public Map getBusyOrNotTaskByPicUnitService(BasicPicUnit bpu);
	
	
	/**
	 * 根据用户id默认获取集中调度的工作列表
	 * @param userId
	 */
	public Map getWorkListByUserService(String userId);
	
	
	/**
	 * 根据用户id默认获取集中调度的工作列表
	 */
	public Map getWorkListByUserService(String userId,String moduleWorkFlag);
	
	/**
	 * 根据userId，获取站址，车辆，人员的数量统计
	 * @param userId
	 * @return
	 */
	public List<Map<String,String>> getResouceCountListService(String userId);
	
	
	
	/**
	 * 根据资源类型、资源Id获取该资源关联的工单、任务单
	 * @param resourceType 资源类型
	 * @param resourceId 资源Id
	 * @return
	 */
	public Map getResourceWorkOrderAndTaskOrderListService(String resourceType,String resourceId);
	
	
	/**
	 * 根据资源类型、资源Id获取该资源的待办工单总数
	 * @param resourceType
	 * @param resourceId
	 * @return
	 */
	public Map getResourcePendingWorkOrderAmountService(String resourceType,String resourceId);
	
	/**
	 * 获取初始化的图层管理器
	 * @param userId 用户Id
	 * @param centerLatLng 当前经纬度
	 * @param currentVisibleKM 当前可见公里数
	 * @param latLngBounds 窗口范围
	 * @author chen
	 * @return
	 */
	public PicLayerManager getInitPicLayerManagerService(String userId,LatLng centerLatLng, double currentVisibleKM,LatLngBounds latLngBounds);
	
	/**
	 * 获取当前登录人 区域信息
	 * @author chen
	 * @return
	 */
	public Map getCurrentUserAreaInfoService();
	
	/**
	 * 地图缩放级别响应事件
	 * @param plm 图层管理器
	 * @param currentVisibleKM 当前地图可见公里数
	 * @param latLngBounds 当前地图窗口范围
	 * @param centerLatLng 当前地图中心经纬度
	 * @return
	 */
	public String responseZoomLevelChangeService(PicLayerManager plm,Double currentVisibleKM,LatLngBounds latLngBounds,LatLng centerLatLng);
	
	
	/**
	 * 保存图层树设置 
	 * @param strUserConfigTree 用户图层树设置
	 * @param userId 用户Id
	 * @param currentVisibleKM 当前可见公里数
	 * @return
	 */
	public String savePicLayerConfigService(String strUserConfigTree,String userId,Double currentVisibleKM);

	/**
	 * 获取图元任务详细信息
	 * @param bpu 图元
	 * @param infoType 类型
	 * @param userId 用户Id
	 * @return
	 */
	public String getTaskDetailInfoOfGraphElement(BasicPicUnit bpu,String infoType,String userId);
	
	/**
	 * 按条件搜索资源
	 * @param strSearchType 搜索类型
	 * @param searchName 搜索名称
	 * @param geLatLng 图元经纬度
	 * @param distance 半径
	 * @param plm 图层管理器
	 * @return
	 */
	public String getResourceByConditionsService(String strSearchType,String searchName,LatLng geLatLng,long distance,PicLayerManager plm);

	/**
	 * 获取特定图元信息
	 * @param picUnitTypeCode 图元类型
	 * @param resourceId 图元Id
	 * @param plm 图层管理器
	 * @return
	 */
	public String getSpecifiedResourceInfoService(String picUnitTypeCode,String resourceId,PicLayerManager plm);
	
	/**
	 * 验证连线派发信息
	 * @param WOID 工单Id
	 * @param assignStaffId 派发人员Id
	 * @return
	 */
	public Map checkAssignTaskOnPicUnitService(String WOID,String assignStaffId);
	
	/**
	 * 增加区域活动次数
	 * @param areaId
	 */
	public void addAreaActiveNumber(long areaId);
	
	/**
	 * 获取监控资源
	 * @return
	 */
	public String getMonitorResourceService();
}
