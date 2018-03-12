package com.iscreate.op.action.gisdispatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.constant.Constant;
import com.iscreate.op.constant.GisDispatchGraphConstant;
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
import com.iscreate.op.pojo.gisdispatch.LittleIcon;
import com.iscreate.op.pojo.gisdispatch.PicLayer;
import com.iscreate.op.pojo.gisdispatch.PicLayerManager;
import com.iscreate.op.pojo.gisdispatch.PicUnitType;
import com.iscreate.op.pojo.gisdispatch.PicUnitTypeMile;
import com.iscreate.op.pojo.gisdispatch.TaskInfo;
import com.iscreate.op.pojo.gisdispatch.UserConfigTree;
import com.iscreate.op.pojo.gisdispatch.UserLayerSetting;
import com.iscreate.op.pojo.gisdispatch.UserLayerTypeSetting;
import com.iscreate.op.pojo.gisdispatch.UserLittleIconSetting;
import com.iscreate.op.service.gisdispatch.DoubleBackgroundLatLngCalculator;
import com.iscreate.op.service.gisdispatch.GisDispatchService;
import com.iscreate.op.service.publicinterface.StationQueryTimerService;
import com.iscreate.plat.login.constant.UserInfo;
import com.iscreate.plat.tools.GisDispatchJSONTools;
import com.iscreate.plat.tools.InterfaceUtil;
import com.iscreate.plat.tools.LatLngHelper;
import com.sun.corba.se.impl.ior.GenericIdentifiable;

public class GisDispatchAction {

	// 注入
	private GisDispatchService gisDispatchService;
	
	private StationQueryTimerService stationQueryTimer;
	
	// 页面参数
	private LatLng centerLatLng;// 地图中心点
	private int zoom;// 地图缩放级别
	private double currentVisibleKM;// 可见公里数
	private LatLngBounds latLngBounds;// 当前经纬度窗口
	private long distance;// 距离，米为单位
	private LatLng geLatLng;// 某个图元经纬度
	
	//-----------che.yd-------begin------------
	
	private String baseStationId;	//创建抢修工单的时候的站址id
	private String baseStationType;//创建抢修工单的时候的基站类型
	private long bizunitInstanceId;	//创建抢修工单的时候的业务单元区域id

	
	private String workType;	//创建车辆调度申请单的工作类型	目前的值，1是标识抢修，2是标识巡检
	private String criticalClass="normal";	//创建车辆调度申请单的紧急程度	hardcode 可选值有：extraurgent,urgent,normal
	
	private String WOID;	//连线派发任务时候的工单号
	private String TOID;	//连线派发任务时候的任务单号
	private String assignTaskUserId;	// 连线派发任务时候的用户id
	private String assignTaskLocationAction;	//连线派发任务时候，跳转的地址
	
	
	private String infoType;
	private int pageIndex;
	private int pageSize;
	//-----------che.yd-------end------------
	
	
	public GisDispatchService getGisDispatchService() {
		return gisDispatchService;
	}

	public void setGisDispatchService(GisDispatchService gisDispatchService) {
		this.gisDispatchService = gisDispatchService;
	}


	public LatLng getCenterLatLng() {
		return centerLatLng;
	}

	public void setCenterLatLng(LatLng centerLatLng) {
		this.centerLatLng = centerLatLng;
	}

	public int getZoom() {
		return zoom;
	}

	public void setZoom(int zoom) {
		this.zoom = zoom;
	}

	public double getCurrentVisibleKM() {
		return currentVisibleKM;
	}

	public void setCurrentVisibleKM(double currentVisibleKM) {
		this.currentVisibleKM = currentVisibleKM;
	}

	public LatLngBounds getLatLngBounds() {
		return latLngBounds;
	}

	public void setLatLngBounds(LatLngBounds latLngBounds) {
		this.latLngBounds = latLngBounds;
	}
	public LatLng getGeLatLng() {
		return geLatLng;
	}

	public void setGeLatLng(LatLng geLatLng) {
		this.geLatLng = geLatLng;
	}

	public long getBizunitInstanceId() {
		return bizunitInstanceId;
	}

	public void setBizunitInstanceId(long bizunitInstanceId) {
		this.bizunitInstanceId = bizunitInstanceId;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getCriticalClass() {
		return criticalClass;
	}

	public void setCriticalClass(String criticalClass) {
		this.criticalClass = criticalClass;
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

	public String getAssignTaskUserId() {
		return assignTaskUserId;
	}

	public void setAssignTaskUserId(String assignTaskUserId) {
		this.assignTaskUserId = assignTaskUserId;
	}

	public String getAssignTaskLocationAction() {
		return assignTaskLocationAction;
	}

	public void setAssignTaskLocationAction(String assignTaskLocationAction) {
		this.assignTaskLocationAction = assignTaskLocationAction;
	}
	
	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}
	public String getBaseStationId() {
		return baseStationId;
	}

	public void setBaseStationId(String baseStationId) {
		this.baseStationId = baseStationId;
	}

	public String getBaseStationType() {
		return baseStationType;
	}

	public void setBaseStationType(String baseStationType) {
		this.baseStationType = baseStationType;
	}

	public StationQueryTimerService getStationQueryTimer() {
		return stationQueryTimer;
	}

	public void setStationQueryTimer(StationQueryTimerService stationQueryTimer) {
		this.stationQueryTimer = stationQueryTimer;
	}

	
	/**
	 * 初始化地图
	 * 
	 * 2012-3-21 上午11:49:25
	 */
	public void initMapAction() {
		/**
		 * 
		 * 前台输入参数： center latLngBounds zoom currentVisiblegKM
		 * 
		 * 获取基本数据， 创建图层， 获取可见图层的数据， 返回gson数据
		 * 
		 */
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");

		// 输出
		try {
			//获取数据
			String neLat = request.getParameter("neLat");
			String neLng = request.getParameter("neLng");
			String swLat = request.getParameter("swLat");
			String swLng = request.getParameter("swLng");
			LatLng sw = new LatLng(Double.valueOf(swLat),Double.valueOf(swLng));
			LatLng ne = new LatLng(Double.valueOf(neLat),Double.valueOf(neLng));
			latLngBounds = new LatLngBounds(sw,ne);
			
			
			// 读取用户id信息
			String userId = (String) ServletActionContext.getRequest().getSession().getAttribute(UserInfo.USERID);
			String json = "";
			PicLayerManager plm = (PicLayerManager)session.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY);
			plm = null;
			if( plm == null ){
				// 获取初始化的图层管理器
				plm = gisDispatchService.getInitPicLayerManagerService(userId,centerLatLng,currentVisibleKM, latLngBounds);
				
				json = plm.toInitMapJson();// 将结果转换为json
				// 保存进session
				session.setAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY, plm);
			}else{
				json = plm.toInitMapJson();// 将结果转换为json
			}
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取可见图元的任务、位置信息
	 * 刷新图元
	 * 2012-3-21 上午11:51:08
	 */
	public void refreshGraphElementAction() {
		/**
		 * 前台输入参数： 无 { layerManagerName:'', layerList: [ { key:'', name:'',
		 * simpleGeListWithTaskInfo: [ { key:'', taskInfo: { totalTaskCount:'',
		 * taskDetailInfos: [ { name:'', count:'', needShowLittleIcon:'',
		 * littleIcon:'' } ] } } ] } ] }
		 * 
		 */
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		
		String userId = (String) ServletActionContext.getRequest().getSession().getAttribute(UserInfo.USERID);
		PicLayerManager plm = null;
		// 如果已经存在
		if (session.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY) != null) {
			plm = (PicLayerManager) session
					.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY);
		}
		// 是否已经有数据
		if (plm == null) {
			try {
				response.getWriter().write("{flag:'fail',msg:'当前没有任何数据'}");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		// 找可见图层
		Map<String, PicLayer> picLayers = plm.getVisiblePicLayers();
		if (picLayers == null || picLayers.isEmpty()) {
			try {
				response.getWriter().write("{flag:'fail',msg:'当前没有任何可见图层数据'}");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		// 遍历可见图层，得到可见图元，获取图元任务信息
		for (String key : picLayers.keySet()) {
			PicLayer pl = picLayers.get(key);
			// 获取可见图元
			Map<String, BasicPicUnit> visiblePicUnits = pl
					.getVisiblePicUnitMap();
			if (visiblePicUnits != null && !visiblePicUnits.isEmpty()) {
				// 循环获取可见图元的任务信息
				for (String bpKey : visiblePicUnits.keySet()) {
					BasicPicUnit bpu = visiblePicUnits.get(bpKey);
					// 获取图元的任务信息
					TaskInfo tinfo = gisDispatchService.getTaskInfoOfGraphElementService(bpu,userId);
					bpu.setTaskInfo(tinfo);
				}
			}
		}
		// 转换为json格式
		String json = plm.toGetTaskInfoJson();
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ;
	}

	/**
	 * 响应缩放级别改变事件
	 * 
	 * 
	 * $Author gmh
	 * 
	 * 2012-3-23 上午10:31:08
	 */
	public void responseZoomLevelChangeAction() {

		/**
		 * 前台输入参数：centerLatLng zoom currentVisibleKM latLngBounds
		 * 
		 * 计算缩放级别改变后的经纬度窗口 将图元类型根据新的可见公里数进行调整：显示或者不显示
		 * 
		 * 综合：根据改变后的经纬度窗口以及图元类型的显示调整，调整可见图元
		 */
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		
		PicLayerManager plm = null;
		// 如果已经存在
		if (session.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY) != null) {
			plm = (PicLayerManager) session.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY);
		}
		// 是否已经有数据
		if (plm == null) {
			try {
				response.getWriter().write("{flag:'fail',msg:'当前没有任何数据'}");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		try {
			//获取数据
			String neLat = request.getParameter("neLat");
			String neLng = request.getParameter("neLng");
			String swLat = request.getParameter("swLat");
			String swLng = request.getParameter("swLng");
			LatLng sw = new LatLng(Double.valueOf(swLat),Double.valueOf(swLng));
			LatLng ne = new LatLng(Double.valueOf(neLat),Double.valueOf(neLng));
			latLngBounds = new LatLngBounds(sw,ne);
			
			String json = gisDispatchService.responseZoomLevelChangeService(plm,currentVisibleKM,latLngBounds,centerLatLng);
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}

	/**
	 * 获取某个图元详细的任务信息
	 * 
	 * 2012-3-21 上午11:52:28
	 */
	@SuppressWarnings("unchecked")
	public void getTaskDetailInfoOfGraphElementAction() {
		/*
		//返回的数据格式：
  		{
			basicInfo:{name:'金山大厦',address:'天河五山路',key:entityId,rank:'VIP基站'},
			taskListInfo:[
				{toId:'20120322-01',taskName:'任务单1',statuName:'处理中'},
				{toId:'20120322-02',taskName:'任务单2',statuName:'已派发'},
				{toId:'20120322-03',taskName:'任务单3',statuName:'处理中'}
			]
		};*/
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		String userId="";
		try {
			userId = (String) ServletActionContext.getRequest().getSession().getAttribute(UserInfo.USERID);
			PicLayerManager plm = null;
			// 如果已经存在
			if (session.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY) != null) {
				plm = (PicLayerManager) session
						.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY);
			}
			// 是否已经有数据
			if (plm == null) {
				response.getWriter().write("{flag:'fail',msg:'当前没有任何数据'}");
				return;
			}
			
			HttpServletRequest request=ServletActionContext.getRequest();
			String strParam=request.getParameter("resourceTypeAndId");	//格式:resourceType,resourceId;
			String[] param=strParam.split(",");
			BasicPicUnit bpu = new BasicPicUnit();
			bpu.setType(param[0]);
			String idFlag=param[1];
			int underIndex=idFlag.lastIndexOf("_");
			String resourceId=idFlag.substring(underIndex+1);
			bpu.setId(resourceId);
			infoType = request.getParameter("infoType");
			
			//获取图元任务详细信息
			String json = gisDispatchService.getTaskDetailInfoOfGraphElement(bpu, infoType, userId);
				
			response.getWriter().write(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 保存图层设置事件  author:che.yd
	 */
	public void savePicLayerConfigAction() {
		// ---------che.yd----------begin------
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");

		String userId = (String) session.getAttribute(UserInfo.USERID);
		String strUserConfigTree = request.getParameter("userConfigTree");
		
		//保存图层设置
		String resultJson = gisDispatchService.savePicLayerConfigService(strUserConfigTree, userId, currentVisibleKM);
		try {
			response.getWriter().write(resultJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * 获取用户设置的图层左边树
	 * 
	 * 
	 * $Author gmh
	 * 
	 * 2012-3-24 下午12:00:16
	 */
	public void getLayerTreeConfigAction() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");

		PicLayerManager plm = null;
		// 如果已经存在
		if (session.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY) != null) {
			plm = (PicLayerManager) session
					.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY);
		}
		// 是否已经有数据
		if (plm == null) {
			try {
				response.getWriter().write("{flag:'fail',msg:'当前没有任何数据'}");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		String json = GisDispatchJSONTools.getLayerTreeOfPicLayerManagerJson(plm);
		
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}
	
	

	/**
	 * 获取用户的工单列表信息
	 * 
	 * Mar 26, 2012 11:39:30 AM gmh
	 */
	public void getWorkOrderListOfUserForGisDispatch() {
		/**
		 * [ { type:'workorder',//工单还是任务单 data://任务单或者工单的数据 { key:value } }]
		 */

		/**
		 * 读取用户工单列表 读取用户任务单列表 关联工单所属基站，以使得在点击工单时，基站在GIS地图居中显示同时打开基站的信息浮窗
		 * 
		 */

		HttpServletRequest request = null;
		HttpServletResponse response = null;
		
		String userId = "";
		StringBuilder json = new StringBuilder();
		String workOrderType="";
		String bizTypeCode="";
		try {
			request=ServletActionContext.getRequest();
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/plain");
			userId=(String) ServletActionContext.getRequest().getSession().getAttribute(UserInfo.USERID);
			workOrderType=request.getParameter("workOrderType");
			bizTypeCode=request.getParameter("bizTypeCode");
			
			String woId = request.getParameter("orderNumber");
			String woTitle = request.getParameter("orderTitle");
			String woState = request.getParameter("orderState");
			//封装参数
			Map param = new HashMap();
			param.put("woId", woId);
			param.put("woTitle", woTitle);
			param.put("woState", woState);
			
			json.append("[");

			//获取具体数据
			List<Map<String, Object>> wos=null;
			wos = gisDispatchService.getUserWorkOrdersService(userId,bizTypeCode,workOrderType,pageIndex,pageSize,param);
			
			//获取数量，并计算总页数
			int totalCount=0;
			int totalPage=0;

			if (wos != null && wos.size() > 0) {
				for (Map<String,Object> wo : wos) {
					json.append(GisDispatchJSONTools.getWorkOrderJson(wo));
					json.append(",");
				}
				totalCount=Integer.parseInt(wos.get(0).get("totalCount").toString());
				totalPage=totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
				json.append("{'totalPage':"+totalPage+",'totalCount':"+totalCount+"}");
				//json = json.delete(json.length() - 1, json.length());	//删除逗号
			}
			json.append("]");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 输出到前台
		try {
			response.getWriter().write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

	/**
	 * 获取用户的任务单列表信息
	 * 
	 * Mar 26, 2012 2:58:58 PM gmh
	 */
	public void getTaskOrderListOfUserForGisDispatch() {
		/**
		 * [ { type:'taskorder',//工单还是任务单 data://任务单或者工单的数据 { key:value } }]
		 */
		/**
		 * 读取用户工单列表 读取用户任务单列表 关联工单所属基站，以使得在点击工单时，基站在GIS地图居中显示同时打开基站的信息浮窗
		 * 
		 */

		HttpServletRequest request = null;
		HttpServletResponse response = null;
		
		String userId = "";
		StringBuilder json = new StringBuilder();
		String taskOrderType="";
		String bizTypeCode = "";
		try {
			request=ServletActionContext.getRequest();
			response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/plain");
			userId=(String) ServletActionContext.getRequest().getSession().getAttribute(UserInfo.USERID);
			taskOrderType=request.getParameter("taskOrderType");
			bizTypeCode = request.getParameter("bizTypeCode");
			
			String toId = request.getParameter("orderNumber");
			String toTitle = request.getParameter("orderTitle");
			String toState = request.getParameter("orderState");
			
			Map param = new HashMap();
			param.put("toId", toId);
			param.put("toTitle", toTitle);
			param.put("toState", toState);
			
			json.append("[");
			// 读取用户的任务单信息
			List<Map<String, Object>> tos = gisDispatchService.getUserTaskOrdersService(userId,bizTypeCode,taskOrderType,pageIndex,pageSize,param);
			if (tos != null && tos.size() > 0) {
				for (Map<String, Object> to : tos) {
					json.append(GisDispatchJSONTools.getTaskOrderJson(to));
					json.append(",");
				}
				int totalCount=Integer.parseInt(tos.get(0).get("totalCount").toString());
				int totalPage=totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
				json.append("{'totalPage':"+totalPage+",'totalCount':"+totalCount+"}");
				//json = json.delete(json.length() - 1, json.length());// 删除逗号
			}
			json.append("]");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 输出到前台
		try {
			response.getWriter().write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	
	
	/**
	 * 搜索基站周边的人员
	 * 
	 * Mar 26, 2012 3:57:41 PM gmh
	 */
	public void getResourceByConditionsAction() {
		/**
		 * 输入： 基站经纬度 距离：米
		 */

		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");

		PicLayerManager plm = null;
		// 如果已经存在
		if (session.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY) != null) {
			plm = (PicLayerManager) session
					.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY);
		}
		// 是否已经有数据
		if (plm == null) {
			try {
				response.getWriter().write("{flag:'fail',msg:'当前没有任何数据'}");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		String strSearchType=ServletActionContext.getRequest().getParameter("searchType");
		String searchName = ServletActionContext.getRequest().getParameter("searchName");
		String searchDistance = ServletActionContext.getRequest().getParameter("distance");
		if(searchDistance!=null && !"".equals(searchDistance)){
			distance = Long.valueOf(searchDistance);
		}
		
		//根据条件搜索资源
		String resultJson = gisDispatchService.getResourceByConditionsService(strSearchType, searchName, geLatLng, distance, plm);
		
		try {
			response.getWriter().write(resultJson);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 只获取某个图元的信息
	 * 
	 * Apr 28, 2012 10:42:50 AM gmh
	 */
	public void getSpecifiedResourceInfoAction() {
		/**
		 * 
		 * {key:'1',layerName:'网络资源设施', geList:[{}]
		 * 
		 */
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		
		PicLayerManager plm = null;
		// 如果已经存在
		if (session.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY) != null) {
			plm = (PicLayerManager) session
					.getAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY);
		}
		// 是否已经有数据
		if (plm == null) {
			try {
				response.getWriter().write("{flag:'fail',msg:'当前没有任何数据'}");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		try {
			String picUnitTypeCode = request.getParameter("picUnitTypeCode");
			String resourceId = request.getParameter("resourceId");
			
			//获取特定图元信息
			String resultJson = gisDispatchService.getSpecifiedResourceInfoService(picUnitTypeCode, resourceId, plm);
			
			response.getWriter().write(resultJson);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	
	
	/**
	 * author:che.yd 基于GIS图元对象直接创建工单 
	 * @category 预留
	 */
	public String createWorkOrderOnPicUnitAction(){
		
		//参数定义：
		//工单类别格式：抢修：urgentrepair；修缮：xiushan：巡检:routineinspection
		HttpServletRequest request = null;
		String workOrderType="";
		try {
			request=ServletActionContext.getRequest();
			workOrderType=request.getParameter("workOrderType");
			
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return workOrderType;
	}
	
	
	/**
	 * author:che.yd 基于GIS调度选择人员并分配任务 
	 */
	public void assignTaskOnPicUnitAction(){
		
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		String orderNum="";
		String workobjectType="";
		boolean isAssign=true;
		try {
			request=ServletActionContext.getRequest();
			response=ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/plain");
			orderNum=request.getParameter("orderNum");
			workobjectType=request.getParameter("workobjectType");
			//获取派发结果
			Map assignResultMap = gisDispatchService.checkAssignTaskOnPicUnitService(orderNum, assignTaskUserId);
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(assignResultMap);
			
//			String json = "";
//			json+="{isAssign:'"+isAssign+"',assignTaskLocationAction:'"+this.assignTaskLocationAction+
//			"?assignTaskUserId="+this.assignTaskUserId+"&bizunitInstanceId="+bizunitInstanceId+
//			"&WOID="+orderNum+"&TOID="+this.TOID+"'}";
			
			response.getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * author:che.yd 获取报表显示的数据
	 * @return
	 */
	public void getReportCharAction(){
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		String userId = "";
		try {
			request=ServletActionContext.getRequest();
			response=ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			userId=(String) request.getSession().getAttribute(UserInfo.USERID);
			
			List<Map<String,String>> resourceMap=this.gisDispatchService.getResouceCountListService(userId);
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(resourceMap);
//			System.out.println(result);
			try {
				response.getWriter().write(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 根据组织架构Id获取组织架构详情GIS展示
	 * @author chen.lb
	 */
	public void getOrgInfoAction(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String orgId = request.getParameter("orgId");
		Map resultMap = new HashMap();
		/*List<Account> accountList=organizationService.getAllStaffListDownwardByOrg(Long.valueOf(orgId));
		Map resultMap = new HashMap();
		Organization tempOrg=organizationService.getOrganizationById(orgId);
		if(tempOrg!=null){
			resultMap.put("orgId", tempOrg.getId());
			resultMap.put("orgName", tempOrg.getOrgName());
			resultMap.put("parentOrgId", tempOrg.getParentOrgId());
			resultMap.put("orgType", tempOrg.getOrgType());
			resultMap.put("orgDuty", tempOrg.getOrgDuty());
			resultMap.put("address", tempOrg.getAddress());
			resultMap.put("latitude", tempOrg.getLatitude());
			resultMap.put("longitude", tempOrg.getLongitude());
			resultMap.put("contactPhone", tempOrg.getContactPhone());
			resultMap.put("dutyPerson", tempOrg.getDutyPerson());
			resultMap.put("dutyPersonPhone", tempOrg.getDutyPersonPhone());
			resultMap.put("administrativeAreaId", tempOrg.getAdministrativeAreaId());
		}
		if(accountList!=null && !accountList.isEmpty()){
			resultMap.put("orgStaffCount", accountList.size());
		}*/
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(resultMap, new TypeToken<Map>(){}.getType());		
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取图元的任务信息（网络资源使用）
	 * @author chen.lb
	 */
	public String getGraphElementTaskDetailAction(){
		HttpServletRequest request=ServletActionContext.getRequest();
		String resourceType = request.getParameter("resourceType");
		String resourceId = request.getParameter("resourceId");
		
		return "success";
	}
	
	/**
	 * 获取当前登录人区域信息
	 * @author chen.lb
	 */
	public void getCurrentUserAreaAction(){
		//获取当前登录人区域信息
		Map resMap = gisDispatchService.getCurrentUserAreaInfoService();
		// json转换
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(resMap, new TypeToken<Map>(){}.getType());		
		//转发
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 手动触发初始化集中调度数据
	 */
	public void initGisDataAction(){
		stationQueryTimer.getStationData();
	}
	
	/**
	 * 获取监控资源
	 */
	public void getMonitorResourceAction(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		//获取数据
		String resJson = this.gisDispatchService.getMonitorResourceService();
		try {
			response.getWriter().write(resJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 刷新cache数据
	 */
	public void refreshCacheDataAction(){
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpSession session = request.getSession();
			//获取数据
			String neLat = request.getParameter("neLat");
			String neLng = request.getParameter("neLng");
			String swLat = request.getParameter("swLat");
			String swLng = request.getParameter("swLng");
			LatLng sw = new LatLng(Double.valueOf(swLat),Double.valueOf(swLng));
			LatLng ne = new LatLng(Double.valueOf(neLat),Double.valueOf(neLng));
			latLngBounds = new LatLngBounds(sw,ne);
			// 读取用户id信息
			String userId = (String) ServletActionContext.getRequest().getSession().getAttribute(UserInfo.USERID);
			// 获取初始化的图层管理器
			PicLayerManager plm = gisDispatchService.getInitPicLayerManagerService(userId, centerLatLng,currentVisibleKM, latLngBounds);
			// 保存进session
			session.setAttribute(GisDispatchGraphConstant.GRAPHMANAGERKEY, plm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
