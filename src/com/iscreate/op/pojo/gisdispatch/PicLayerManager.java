package com.iscreate.op.pojo.gisdispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.service.gisdispatch.BackgroundLatLngCalculator;
import com.iscreate.plat.tools.GisDispatchJSONTools;


/**
 * 图层管理器
 * 
 * @author gmh
 * 
 * 2012-3-20下午03:57:45
 */
public class PicLayerManager {

	// 图层管理器器名称
	private String name = "图层管理器";
	// 当前缩放级别
	private int currentZoomLevel;
	// 当前可见公里数
	private double currentVisiblegKM;
	// 当前窗口经纬度范围
	private LatLngBounds windowLatLngRange;
	// 背景窗口经纬度范围
	private LatLngBounds backgroundLatLngRange;
	// 当前地图中心点
	private LatLng center;
	// 是否自动刷新
	private boolean isAutoRefresh;
	// 自动刷新时间间隔
	private long autoRefreshTime;
	// 拥有的可见图层列表
	private Map<String, PicLayer> visiblePicLayers = new HashMap<String, PicLayer>();
	// 拥有的隐藏图层列表
	private Map<String, PicLayer> hiddenPicLayers = new HashMap<String, PicLayer>();
	// 新增隐藏图层
	private Map<String, PicLayer> newHiddenPicLayers = new HashMap<String, PicLayer>();
	// 新增可见图层
	private Map<String, PicLayer> newVisiblePicLayers = new HashMap<String, PicLayer>();
	// 计算背景窗口经纬度范围的策略
	private BackgroundLatLngCalculator backgroundLatLngCalculator;

	// 用户的图层设置信息，关联的图层id为key
	private Map<Long, UserLayerSetting> userLayerSettings = new HashMap<Long, UserLayerSetting>();
	// 用户的图元类型设置信息，关联的图元类型的id为key
	private Map<Long, UserLayerTypeSetting> userLayerTypeSettings = new HashMap<Long, UserLayerTypeSetting>();
	// 用户的脚标设置信息，关联的脚标id为key
	private Map<Long, UserLittleIconSetting> userLittleIconSettings = new HashMap<Long, UserLittleIconSetting>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCurrentZoomLevel() {
		return currentZoomLevel;
	}

	public void setCurrentZoomLevel(int currentZoomLevel) {
		this.currentZoomLevel = currentZoomLevel;
	}

	public double getCurrentVisiblegKM() {
		return currentVisiblegKM;
	}

	public void setCurrentVisiblegKM(double currentVisiblegKM) {
		this.currentVisiblegKM = currentVisiblegKM;
	}

	public LatLngBounds getWindowLatLngRange() {
		return windowLatLngRange;
	}

	public void setWindowLatLngRange(LatLngBounds windowLatLngRange) {
		this.windowLatLngRange = windowLatLngRange;
	}

	public LatLngBounds getBackgroundLatLngRange() {
		return backgroundLatLngRange;
	}

	public void setBackgroundLatLngRange(LatLngBounds backgroundLatLngRange) {
		this.backgroundLatLngRange = backgroundLatLngRange;
	}

	public LatLng getCenter() {
		return center;
	}

	public void setCenter(LatLng center) {
		this.center = center;
	}

	public boolean isAutoRefresh() {
		return isAutoRefresh;
	}

	public void setAutoRefresh(boolean isAutoRefresh) {
		this.isAutoRefresh = isAutoRefresh;
	}

	public long getAutoRefreshTime() {
		return autoRefreshTime;
	}

	public void setAutoRefreshTime(long autoRefreshTime) {
		this.autoRefreshTime = autoRefreshTime;
	}

	public Map<String, PicLayer> getVisiblePicLayers() {
		return visiblePicLayers;
	}

	public void setVisiblePicLayers(Map<String, PicLayer> visiblePicLayers) {
		this.visiblePicLayers = visiblePicLayers;
	}

	public Map<String, PicLayer> getHiddenPicLayers() {
		return hiddenPicLayers;
	}

	public void setHiddenPicLayers(Map<String, PicLayer> hiddenPicLayers) {
		this.hiddenPicLayers = hiddenPicLayers;
	}

	public Map<String, PicLayer> getNewHiddenPicLayers() {
		return newHiddenPicLayers;
	}

	public void setNewHiddenPicLayers(Map<String, PicLayer> newHiddenPicLayers) {
		this.newHiddenPicLayers = newHiddenPicLayers;
	}

	public Map<String, PicLayer> getNewVisiblePicLayers() {
		return newVisiblePicLayers;
	}

	public void setNewVisiblePicLayers(Map<String, PicLayer> newVisiblePicLayers) {
		this.newVisiblePicLayers = newVisiblePicLayers;
	}

	public BackgroundLatLngCalculator getBackgroundLatLngCalculator() {
		return backgroundLatLngCalculator;
	}

	public void setBackgroundLatLngCalculator(
			BackgroundLatLngCalculator backgroundLatLngCalculator) {
		this.backgroundLatLngCalculator = backgroundLatLngCalculator;
	}

	public Map<Long, UserLayerSetting> getUserLayerSettings() {
		return userLayerSettings;
	}

	public void setUserLayerSettings(
			Map<Long, UserLayerSetting> userLayerSettings) {
		this.userLayerSettings = userLayerSettings;
	}

	public Map<Long, UserLayerTypeSetting> getUserLayerTypeSettings() {
		return userLayerTypeSettings;
	}

	public void setUserLayerTypeSettings(
			Map<Long, UserLayerTypeSetting> userLayerTypeSettings) {
		this.userLayerTypeSettings = userLayerTypeSettings;
	}

	public Map<Long, UserLittleIconSetting> getUserLittleIconSettings() {
		return userLittleIconSettings;
	}

	public void setUserLittleIconSettings(
			Map<Long, UserLittleIconSetting> userLittleIconSettings) {
		this.userLittleIconSettings = userLittleIconSettings;
	}

	/**
	 * 新增一个可见图层
	 * 
	 * @param pl
	 * @return 2012-3-20 下午04:21:38
	 */
	public boolean addAVisiblePicLayer(PicLayer pl) {
		if (pl == null) {
			return false;
		} else {
			if (visiblePicLayers.containsKey(pl.getKey())) {
				return false;
			} else {
				visiblePicLayers.put(pl.getKey(), pl);
				return true;
			}
		}
	}

	/**
	 * 删除一个可见图层
	 * 
	 * @param pl
	 * @return 2012-3-20 下午04:23:01
	 */
	public boolean deleteAVisiblePicLayer(PicLayer pl) {
		if (pl == null) {
			return false;
		} else {
			if (visiblePicLayers.containsKey(pl.getKey())) {
				visiblePicLayers.remove(pl.getKey());
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 通过layer的key移除一个可见图层
	 * 
	 * @param plKey
	 * @return 2012-3-20 下午04:23:56
	 */
	public boolean deleteAVisiblePicLayer(String plKey) {
		if (visiblePicLayers.containsKey(plKey)) {
			visiblePicLayers.remove(plKey);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 新增一个不可见图层
	 * 
	 * @param pl
	 * @return 2012-3-20 下午04:21:38
	 */
	public boolean addAHiddenPicLayer(PicLayer pl) {
		if (pl == null) {
			return false;
		} else {
			if (hiddenPicLayers.containsKey(pl.getKey())) {
				return false;
			} else {
				hiddenPicLayers.put(pl.getKey(), pl);
				return true;
			}
		}
	}

	/**
	 * 删除一个不可见图层
	 * 
	 * @param pl
	 * @return 2012-3-20 下午04:23:01
	 */
	public boolean deleteAHiddenPicLayer(PicLayer pl) {
		if (pl == null) {
			return false;
		} else {
			if (hiddenPicLayers.containsKey(pl.getKey())) {
				hiddenPicLayers.remove(pl.getKey());
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 通过layer的key移除一个不可见图层
	 * 
	 * @param plKey
	 * @return 2012-3-20 下午04:23:56
	 */
	public boolean deleteAHiddenPicLayer(String plKey) {
		if (hiddenPicLayers.containsKey(plKey)) {
			hiddenPicLayers.remove(plKey);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查找一个图层
	 * 
	 * @param plKey
	 * @return 2012-3-20 下午04:25:17
	 */
	public PicLayer findAPicLayerByKey(String plKey) {
		if (hiddenPicLayers.containsKey(plKey)) {
			PicLayer p=hiddenPicLayers.get(plKey);
			return p;
		}
		if (visiblePicLayers.containsKey(plKey)) {
			PicLayer p=visiblePicLayers.get(plKey);
			return p;
		}
		
		//che.yd-------begin-----
		if(newVisiblePicLayers.containsKey(plKey)){
			return newVisiblePicLayers.get(plKey);
		}
		
		if(newHiddenPicLayers.containsKey(plKey)){
			return newHiddenPicLayers.get(plKey);
		}
		//che.yd-------end-----
		return null;
	}

	/**
	 * 通过图层名称查找图层
	 * 
	 * @param plName
	 * @return Mar 28, 2012 10:33:15 AM gmh
	 */
	public PicLayer findAPicLayerByName(String plName) {
		PicLayer pl = null;
		for (String key : hiddenPicLayers.keySet()) {
			pl = hiddenPicLayers.get(key);
			if (pl != null && plName.equals(pl.getName())) {
				break;
			}
		}

		if (pl == null) {
			for (String key : visiblePicLayers.keySet()) {
				pl = visiblePicLayers.get(key);
				if (pl != null && plName.equals(pl.getName())) {
					break;
				}
			}
		}
		return pl;

	}

	/**
	 * 隐藏一个图层
	 * 
	 * @param plKey
	 *            2012-3-20 下午04:29:29
	 */
	public void hiddenOnePicLayer(String plKey) {
		if (visiblePicLayers.containsKey(plKey)) {
			PicLayer pl = visiblePicLayers.get(plKey);

			pl.setShow(false);
			newHiddenPicLayers.put(plKey, pl);// 增加进新增隐藏图层中

			visiblePicLayers.remove(plKey);// 从可见图层中删除

		}
	}

	/**
	 * 显示一个图层
	 * 
	 * @param plKey
	 *            2012-3-20 下午04:36:37
	 */
	public void showOnePicLayer(String plKey) {
		if (hiddenPicLayers.containsKey(plKey)) {
			PicLayer pl = hiddenPicLayers.get(plKey);
			pl.setShow(true);
			newVisiblePicLayers.put(plKey, pl);// 增加进新增可见图层中

			hiddenPicLayers.remove(plKey);// 从隐藏图层中删除
		}
	}

	/**
	 * 返回新隐藏的图层列表
	 * 
	 * @return 2012-3-20 下午04:39:54
	 */
	public List<PicLayer> getNewHiddenLayer() {
		if (newHiddenPicLayers.size() > 0) {
			List<PicLayer> pls = new ArrayList<PicLayer>();
			for (String key : newHiddenPicLayers.keySet()) {
				pls.add(newHiddenPicLayers.get(key));
			}

			return pls;
		} else {
			return null;
		}
	}

	/**
	 * 清空新隐藏的图层列表
	 * 
	 * 2012-3-20 下午04:40:49
	 */
	public void clearNewHiddenLayer() {
		newHiddenPicLayers.clear();
	}

	/**
	 * 返回新可见的图层列表
	 * 
	 * @return 2012-3-20 下午04:39:54
	 */
	public List<PicLayer> getNewVisibleLayer() {
		if (newVisiblePicLayers != null && newVisiblePicLayers.size() > 0) {
			List<PicLayer> pls = new ArrayList<PicLayer>();
			for (String key : newVisiblePicLayers.keySet()) {
				pls.add(newVisiblePicLayers.get(key));
			}

			return pls;
		} else {
			return null;
		}
	}

	/**
	 * 清空新可见的图层列表
	 * 
	 * 2012-3-20 下午04:40:49
	 */
	public void clearNewVisibleLayer() {
		newVisiblePicLayers.clear();
	}

	/**
	 * 计算背景窗口经纬度范围
	 * 
	 * @return Author gmh 2012-3-22 下午02:35:21
	 */
	public LatLngBounds calculateBackgroundLatLngBounds() {
		return backgroundLatLngCalculator.calculate(center);
	}

	/**
	 * 
	 * 新增一个图层到新可见列表 Mar 30, 2012 11:11:56 AM gmh
	 */
	public void addToNewVisibleLayers(PicLayer pl) {
		if (newVisiblePicLayers == null) {
			newVisiblePicLayers = new HashMap<String, PicLayer>();
		}
		if (!newVisiblePicLayers.containsKey(pl.getKey())) {
			newVisiblePicLayers.put(pl.getKey(), pl);
			pl.changeFromHiddenToVisible();
		}
	}

	/**
	 * 
	 * 新增一个图层到新隐藏列表 Mar 30, 2012 11:11:56 AM gmh
	 */
	public void addToNewHiddenLayers(PicLayer pl) {
		if (newHiddenPicLayers == null) {
			newHiddenPicLayers = new HashMap<String, PicLayer>();
		}
		if (!newHiddenPicLayers.containsKey(pl.getKey())) {
			newHiddenPicLayers.put(pl.getKey(), pl);
			pl.changeFromVisibleToHidden();
		}
	}
	

	/**
	 * 通过图元类型获取图层
	 * @param graphElementType
	 * @return
	 * Apr 28, 2012 10:50:30 AM
	 * gmh
	 */
	public PicLayer findAPicLayerFromPicUnitTypeCode(String picUnitTypeCode){
			
		for(String  plKey:hiddenPicLayers.keySet()){
			PicLayer p=visiblePicLayers.get(plKey);
			if(p==null){
				continue;
			}
			PicUnitType pt=p.findAPicUnitTypeByCode(picUnitTypeCode);
			if(pt!=null){
				return p;
			}
		}
		
		for(String  plKey:visiblePicLayers.keySet()){
			PicLayer p=visiblePicLayers.get(plKey);
			if(p==null){
				continue;
			}
			PicUnitType pt=p.findAPicUnitTypeByCode(picUnitTypeCode);
			if(pt!=null){
				return p;
			}
		}
			
		for(String  plKey:newVisiblePicLayers.keySet()){
			PicLayer p=newVisiblePicLayers.get(plKey);
			if(p==null){
				continue;
			}
			PicUnitType pt=p.findAPicUnitTypeByCode(picUnitTypeCode);
			if(pt!=null){
				return p;
			}
		}
		
		for(String  plKey:newHiddenPicLayers.keySet()){
			PicLayer p=newHiddenPicLayers.get(plKey);
			if(p==null){
				continue;
			}
			PicUnitType pt=p.findAPicUnitTypeByCode(picUnitTypeCode);
			if(pt!=null){
				return p;
			}
		}
		
		return null;
	}

	/**
	 * 转换初始化地图数据为json
	 * 
	 * @return Author gmh 2012-3-21 下午03:47:21
	 */
	public String toInitMapJson() {
		StringBuilder sr = new StringBuilder();
		sr.append("{layerManagerName:'" + name + "',layerList:[");

		if (!visiblePicLayers.isEmpty()) {
			for (String plKey : visiblePicLayers.keySet()) {
				PicLayer pl = visiblePicLayers.get(plKey);
				sr.append(pl.toInitMapJson());
				sr.append(",");
			}

			sr = sr.delete(sr.length() - 1, sr.length());
		}
		sr.append("]}");
		return sr.toString();
	}

	/**
	 * 返回可见图元的任务数量信息json结果
	 * 
	 * @return Author gmh 2012-3-23 上午10:06:02
	 */
	public String toGetTaskInfoJson() {
		StringBuilder sr = new StringBuilder();
		sr.append("{layerManagerName:'" + name + "',layerList:[");

		if (!visiblePicLayers.isEmpty()) {
			for (String plKey : visiblePicLayers.keySet()) {
				PicLayer pl = visiblePicLayers.get(plKey);
				sr.append(pl.toGetTaskInfoJson());// 可见图元的任务数量信息
				sr.append(",");
			}

			sr = sr.delete(sr.length() - 1, sr.length());
		}
		sr.append("]}");
		return sr.toString();
	}

	/**
	 * 经纬度变化json结果
	 * 
	 * @return Author gmh 2012-3-22 下午03:26:09
	 */
	public String toLatLngBoundsChangedJson() {
		StringBuilder result = new StringBuilder();

		StringBuilder sb = new StringBuilder();
		boolean hasChange = false;
		boolean hasNewlyShow = false;
		boolean hasNewlyHide = false;
		sb.append("newlyShow:[");
		// 新增可见图元
		if (!visiblePicLayers.isEmpty()) {
			for (String plKey : visiblePicLayers.keySet()) {
				PicLayer pl = visiblePicLayers.get(plKey);
				// 新增可见图元列表
				List<BasicPicUnit> bpus = pl.getNewVisibleBasicPicUnitList();
				if (bpus != null && bpus.size() > 0) {
					sb.append("{key:'" + pl.getKey() + "',layerName:'"
							+ pl.getName() + "',geList:[");
					hasNewlyShow = true;
					for (BasicPicUnit bpu : bpus) {
						sb.append(bpu.toJson());
						sb.append(",");
					}
					sb = sb.delete(sb.length() - 1, sb.length());
					sb.append("]");// gelist结束
					sb.append("}");// 图层结束
					sb.append(",");
				}
			}
			if (hasNewlyShow) {
				sb = sb.delete(sb.length() - 1, sb.length());
			}
		}
		sb.append("]");// newlyshow 结束
		sb.append(",");

		sb.append("newlyHide:[");
		if (!visiblePicLayers.isEmpty()) {
			for (String plKey : visiblePicLayers.keySet()) {
				PicLayer pl = visiblePicLayers.get(plKey);
				// 新增隐藏图元列表
				List<BasicPicUnit> bpus = pl.getNewHiddenBasicPicUnitList();
				if (bpus != null && bpus.size() > 0) {
					sb.append("{key:'" + pl.getKey() + "',layerName:'"
							+ pl.getName() + "',geList:[");
					hasNewlyHide = true;

					for (BasicPicUnit bpu : bpus) {
						sb.append(bpu.toSimpleJson());
						sb.append(",");
					}
					sb = sb.delete(sb.length() - 1, sb.length());
					sb.append("]");// gelist结束
					sb.append("}");// 图层结束
					sb.append(",");
				}
			}
			if (hasNewlyHide) {
				sb = sb.delete(sb.length() - 1, sb.length());
			}

		}
		sb.append("]");// newlyhide结束

		if (hasNewlyShow || hasNewlyHide) {
			hasChange = true;
		}
		result.append("{");
		result.append("hasChange:" + hasChange);
		result.append(",");
		result.append(sb);
		result.append("}");

		return result.toString();
	}

	/**
	 * 缩放级别改变json结果
	 * 
	 * @return Author gmh 2012-3-22 下午03:26:46
	 */
	public String toZoomChangedJson() {
		return toLatLngBoundsChangedJson();
	}

	/**
	 * 用户图层树设置改变
	 * 
	 * @return Mar 30, 2012 11:44:09 AM gmh
	 */
	public String toLeftPicTreeChangeJson() {
		// 新增可见图层
		// 新增可见图元
		// 新增隐藏图层
		// 新增隐藏图元

		StringBuilder bufForShow = new StringBuilder();
		StringBuilder bufForHide = new StringBuilder();
		StringBuilder result = new StringBuilder();

		boolean hasNewlyShowLayer = false;
		boolean hasNewlyHiddenLayer = false;
		boolean hasNewlyShow = false;
		boolean hasNewlyHidden = false;
		boolean hasChanged=false;

		bufForShow.append("newlyShow:[");// newlyshow 开始
		bufForHide.append("newlyHide:[");//newlyhide 开始
		
		//新增图层
		if (newVisiblePicLayers != null && newVisiblePicLayers.size() > 0) {
			hasNewlyShowLayer = true;
			for (String key : newVisiblePicLayers.keySet()) {
				PicLayer pl = newVisiblePicLayers.get(key);
				bufForShow.append(pl.getNewlyVisiblePicUnitJson());
				bufForShow.append(",");
			}
		}
		
		//新增隐藏图层
		if(newHiddenPicLayers!=null && newHiddenPicLayers.size()>0){
			hasNewlyHiddenLayer = true;
			for (String key : newHiddenPicLayers.keySet()) {
				PicLayer pl = newHiddenPicLayers.get(key);
//				bufForHide.append(pl.toBasicJson());
				bufForHide.append(pl.getNewlyHiddenPicUnitJson());	//che.yd  添加
				bufForHide.append(",");
			}
		}

		
		//在可见图元里找新增可见图元、新隐藏图元
		if (visiblePicLayers != null && visiblePicLayers.size() > 0) {
			for (String plKey : visiblePicLayers.keySet()) {
				PicLayer pl = visiblePicLayers.get(plKey);
				// 新增可见图元列表
				List<BasicPicUnit> bpus = pl.getNewVisibleBasicPicUnitList();
				if (bpus != null && bpus.size() > 0) {
					hasNewlyShow=true;
					bufForShow.append(pl.getNewlyVisiblePicUnitJson());
					bufForShow.append(",");
				}
				
				//新增隐藏图元
				List<BasicPicUnit> bpushide = pl.getNewHiddenBasicPicUnitList();
				if(bpushide!=null && bpushide.size()>0){
					hasNewlyHidden=true;
					bufForHide.append(pl.getNewlyHiddenPicUnitJson());
					bufForHide.append(",");
				}
			}
			
			if (hasNewlyShow || hasNewlyShowLayer) {
				bufForShow = bufForShow.delete(bufForShow.length() - 1,bufForShow.length());
			}
			if (hasNewlyHidden || hasNewlyHiddenLayer) {
				bufForHide = bufForHide.delete(bufForHide.length() - 1,bufForHide.length());
			}
		}
		
		//che.yd----------begin--------
//		if (hasNewlyShow || hasNewlyShowLayer) {
//			bufForShow = bufForShow.delete(bufForShow.length() - 1,bufForShow.length());
//		}
//		if (hasNewlyHidden || hasNewlyHiddenLayer) {
//			bufForHide = bufForHide.delete(bufForHide.length() - 1,bufForHide.length());
//		}
		//che.yd----------end--------
		
		
		bufForShow.append("]");//newlyshow结束
		bufForHide.append("]");//newlyhide 结束
		
		if(hasNewlyShow || hasNewlyShowLayer || hasNewlyHidden || hasNewlyHiddenLayer){
			hasChanged=true;
		}
		
		
		//-------che.yd--------start-------
		
		String treeJson=GisDispatchJSONTools.getLayerTreeOfPicLayerManagerJson(this);
		
//		-------che.yd--------end-------
		
		result.append("{");
		result.append("hasChanged:true"+","+bufForShow+","+bufForHide+",treeJson:"+treeJson);
		result.append("}");
		
		return result.toString();

	}
}
