package com.iscreate.op.service.publicinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import com.iscreate.op.dao.gisdispatch.GisDispatchDao;
import com.iscreate.op.pojo.gisdispatch.GisDispatch_ActiveArea;
import com.iscreate.op.service.outlinking.OutLinkingService;

public class StationQueryTimerService{
	
	private NetworkResourceInterfaceService networkResourceService;	//网络资源接口
	private GisDispatchDao gisDispatchDao;
	
	public static Map<Long,List<Map<String, Object>>> opsStationMap;	//OPS站址数据
	
	public static int returnsNumber = 50;
	
	
	// 刷新站址数据
	public void execute(){
		getStationData();
	}
	
	// 获取站址数据
	public void getStationData(){
		//初始化站址数据
		if(opsStationMap==null){
			opsStationMap = new HashMap<Long,List<Map<String, Object>>>();
			// 获取前N个活跃区域
			List<GisDispatch_ActiveArea> activeAreaList = gisDispatchDao.getTopActiveArea(returnsNumber);
			if(activeAreaList!=null && !activeAreaList.isEmpty()){
				for (GisDispatch_ActiveArea activeArea : activeAreaList) {
					long areaId = activeArea.getAreaId();
					List<Map<String, String>> stationList = networkResourceService.getResourceService(areaId+"", "Sys_Area", "Station", "CHILD");
					if(stationList!=null && !stationList.isEmpty()){
						if(!this.containsOpsStationKey(areaId)){
							List<Map<String,Object>> tempStationList = new ArrayList<Map<String,Object>>();
							for (Map<String, String> map : stationList) {
								//保存数据
								Map tempMap = new HashMap();
								tempMap.put("objectIdentity", map.get("id"));
								tempMap.put("objectName", map.get("name"));
								tempMap.put("latitude", map.get("latitude"));
								tempMap.put("longitude", map.get("longitude"));
								tempStationList.add(tempMap);
							}
							// key:区域Id , value:站址数据
							this.putOpsStationData(areaId, tempStationList);
						}
					}
				}
			}
		}else{
			//--------------------------- 调整活跃区域&站址数据 ------------------
			// 最新活跃区域Id列表
			List<Long> newActiveAreaIdList = new ArrayList<Long>();
			// 获取当前最活跃N个区域
			List<GisDispatch_ActiveArea> activeAreaList = gisDispatchDao.getTopActiveArea(returnsNumber);
			if(activeAreaList!=null && !activeAreaList.isEmpty()){
				for (GisDispatch_ActiveArea activeArea : activeAreaList) {
					long areaId = activeArea.getAreaId();
					newActiveAreaIdList.add(areaId);
				}
				// 移除活跃次数相对低的区域站址信息
				Set<Long> keySet = opsStationMap.keySet();
				for (Long key : keySet) {
					if(!newActiveAreaIdList.contains(key)){
						removeOpsStationData(key);
					}
				}
				// 重新获取新增活跃区域站址数据
				if(!newActiveAreaIdList.isEmpty()){
					for (Long areaId : newActiveAreaIdList) {
						//获取区域站址数据
						List<Map<String, String>> stationList = networkResourceService.getResourceService(areaId+"", "Sys_Area", "Station", "CHILD");
						if(stationList!=null && !stationList.isEmpty()){
							List<Map<String,Object>> tempStationList = new ArrayList<Map<String,Object>>();
							for (Map<String, String> map : stationList) {
								//保存数据
								Map tempMap = new HashMap();
								tempMap.put("objectIdentity", map.get("id"));
								tempMap.put("objectName", map.get("name"));
								tempMap.put("latitude", map.get("latitude"));
								tempMap.put("longitude", map.get("longitude"));
								tempStationList.add(tempMap);
							}
							// key:区域Id , value:站址数据
							this.putOpsStationData(areaId, tempStationList);
						}
					}
				}
			}
		}
	}

	
	public NetworkResourceInterfaceService getNetworkResourceService() {
		return networkResourceService;
	}

	public void setNetworkResourceService(
			NetworkResourceInterfaceService networkResourceService) {
		this.networkResourceService = networkResourceService;
	}

	public GisDispatchDao getGisDispatchDao() {
		return gisDispatchDao;
	}

	public void setGisDispatchDao(GisDispatchDao gisDispatchDao) {
		this.gisDispatchDao = gisDispatchDao;
	}
	
	//------------ 对于OpsStationMap所有的操作全部上锁，防止异步线程操作数据
	public Object getOpsStationData(Object key){
		return opsStationMap.get(key);
	}
	public synchronized void putOpsStationData(long key,List<Map<String, Object>> values){
		opsStationMap.put(key, values);
	}
	public synchronized void removeOpsStationData(long key){
		opsStationMap.remove(key);
	}
	public synchronized boolean containsOpsStationKey(long key){
		return opsStationMap.containsKey(key);
	}
}
