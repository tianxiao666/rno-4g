package com.iscreate.op.pojo.gisdispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图元
 * 
 * @author gmh
 * 
 * 2012-3-20下午12:15:21
 */
public class PicLayer {

	// 数据库主键id
	private long id;
	// 图层名称
	private String name;
	// 图层是否可见
	private boolean isShow;
	// 可见图元列表
	private Map<String, BasicPicUnit> visiblePicUnitMap = new HashMap<String, BasicPicUnit>();
	// 隐藏图元列表
	private Map<String, BasicPicUnit> hiddenPicUnitMap = new HashMap<String, BasicPicUnit>();
	// 新增可见图元列表
	private Map<String, BasicPicUnit> newVisiblePicUnitMap = new HashMap<String, BasicPicUnit>();
	// 新增隐藏图元列表
	private Map<String, BasicPicUnit> newHiddenPicUnitMap = new HashMap<String, BasicPicUnit>();
	// 包含的图元类型
	private List<PicUnitType> picUnitTypes = new ArrayList<PicUnitType>();
	// 图层管理器
	private PicLayerManager picLayerManager;
	// 所有图元列表
	private Map<String, BasicPicUnit> allPicUnitMap = new HashMap<String, BasicPicUnit>();
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public Map<String, BasicPicUnit> getVisiblePicUnitMap() {
		return visiblePicUnitMap;
	}

	public void setVisiblePicUnitMap(Map<String, BasicPicUnit> visiblePicUnitMap) {
		this.visiblePicUnitMap = visiblePicUnitMap;
	}

	public Map<String, BasicPicUnit> getHiddenPicUnitMap() {
		return hiddenPicUnitMap;
	}

	public void setHiddenPicUnitMap(Map<String, BasicPicUnit> hiddenPicUnitMap) {
		this.hiddenPicUnitMap = hiddenPicUnitMap;
	}

	public Map<String, BasicPicUnit> getNewVisiblePicUnitMap() {
		return newVisiblePicUnitMap;
	}

	public void setNewVisiblePicUnitMap(
			Map<String, BasicPicUnit> newVisiblePicUnitMap) {
		this.newVisiblePicUnitMap = newVisiblePicUnitMap;
	}

	public Map<String, BasicPicUnit> getNewHiddenPicUnitMap() {
		return newHiddenPicUnitMap;
	}

	public void setNewHiddenPicUnitMap(
			Map<String, BasicPicUnit> newHiddenPicUnitMap) {
		this.newHiddenPicUnitMap = newHiddenPicUnitMap;
	}

	public List<PicUnitType> getPicUnitTypes() {
		return picUnitTypes;
	}

	public void setPicUnitTypes(List<PicUnitType> picUnitTypes) {
		this.picUnitTypes = picUnitTypes;
	}

	public PicLayerManager getPicLayerManager() {
		return picLayerManager;
	}

	public void setPicLayerManager(PicLayerManager picLayerManager) {
		this.picLayerManager = picLayerManager;
	}

	public Map<String, BasicPicUnit> getAllPicUnitMap() {
		return allPicUnitMap;
	}

	public void setAllPicUnitMap(Map<String, BasicPicUnit> allPicUnitMap) {
		this.allPicUnitMap = allPicUnitMap;
	}

	/**
	 * 增加到可见列表
	 * 
	 * @param bpu
	 *            2012-3-20 下午03:39:32
	 */
	public boolean addToVisiblePicUnitMap(BasicPicUnit bpu) {
		if (bpu == null) {
			return false;
		}
		if (!visiblePicUnitMap.containsKey(bpu.getKey())) {
			visiblePicUnitMap.put(bpu.getKey(), bpu);
		}
		return true;
	}

	/**
	 * 从可见队列删除
	 * 
	 * @param bpu
	 * @return
	 * 
	 * $Author gmh
	 * 
	 * 2012-3-23 上午11:34:37
	 */
	public boolean removeFromVisiblePicUnitMap(BasicPicUnit bpu) {
		if (bpu == null) {
			return false;
		}
		// 如果包含，则删除
		if (visiblePicUnitMap.containsKey(bpu.getKey())) {
			visiblePicUnitMap.remove(bpu.getKey());
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 增加进隐藏列表
	 * 
	 * @param bpu
	 * @return 2012-3-20 下午03:44:31
	 */
	public boolean addToHiddenPicUnitMap(BasicPicUnit bpu) {
		if (bpu == null) {
			return false;
		}
		if (!hiddenPicUnitMap.containsKey(bpu.getKey())) {
			hiddenPicUnitMap.put(bpu.getKey(), bpu);
		}
		return true;
	}

	/**
	 * 从隐藏队列删除
	 * 
	 * @param bpu
	 * @return
	 * 
	 * $Author gmh
	 * 
	 * 2012-3-23 上午11:35:26
	 */
	public boolean removeFromHiddenPicUnitMap(BasicPicUnit bpu) {
		if (bpu == null) {
			return false;
		}
		// 如果包含，则删除
		if (hiddenPicUnitMap.containsKey(bpu.getKey())) {
			hiddenPicUnitMap.remove(bpu.getKey());
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 将图元从可见列表移动至不可见列表
	 * 
	 * @param bpu
	 * @return 2012-3-20 下午03:45:41
	 */
	public boolean movePicUnitFromVisibleToHidden(BasicPicUnit bpu) {
		if (bpu == null) {
			return false;
		}
		boolean result = false;
		if (!visiblePicUnitMap.containsKey(bpu.getKey())) {
			if (!hiddenPicUnitMap.containsKey(bpu.getKey())) {
				hiddenPicUnitMap.put(bpu.getKey(), bpu);
				visiblePicUnitMap.remove(bpu.getKey());
				result = true;
			}
		}

		return result;
	}

	/**
	 * 将图元从隐藏列表移动到可见列表
	 * 
	 * @param bpu
	 * @return 2012-3-20 下午03:49:30
	 */
	public boolean movePicUnitFromHiddenToVisible(BasicPicUnit bpu) {
		if (bpu == null) {
			return false;
		}
		boolean result = false;
		if (!hiddenPicUnitMap.containsKey(bpu.getKey())) {
			if (!visiblePicUnitMap.containsKey(bpu.getKey())) {
				visiblePicUnitMap.put(bpu.getKey(), bpu);
				hiddenPicUnitMap.remove(bpu.getKey());
				result = true;
			}
		}

		return result;
	}

	/**
	 * 返回图层的key
	 * 
	 * @return 2012-3-20 下午04:19:08
	 */
	public String getKey() {
		return id + "";
	}

	/**
	 * 获取新显示图元列表
	 * 
	 * @return 2012-3-20 下午04:51:13
	 */
	public List<BasicPicUnit> getNewVisibleBasicPicUnitList() {
		if (newVisiblePicUnitMap.size() > 0) {
			List<BasicPicUnit> bps = new ArrayList<BasicPicUnit>();
			for (String key : newVisiblePicUnitMap.keySet()) {
				bps.add(newVisiblePicUnitMap.get(key));
			}
			return bps;
		} else {
			return null;
		}
	}

	/**
	 * 清除新增可见列表
	 * 
	 * 2012-3-20 下午04:57:10
	 */
	public void clearNewVisibleList() {
		if (newVisiblePicUnitMap != null) {
			newVisiblePicUnitMap.clear();
		}
	}

	/**
	 * 增加到可见列表
	 * 
	 * @param bpus
	 *            2012-3-20 下午04:56:29
	 */
	public void addToVisiblePicUnitMap(List<BasicPicUnit> bpus) {
		if (bpus == null || bpus.size() == 0) {
			return;
		}
		for (BasicPicUnit bp : bpus) {
			if (!visiblePicUnitMap.containsKey(bp.getKey())) {
				visiblePicUnitMap.put(bp.getKey(), bp);
			}
		}
	}

	/**
	 * 增加进新隐藏列表
	 * 
	 * @param bpu
	 * @return
	 * 
	 * $Author gmh
	 * 
	 * 2012-3-23 上午11:23:46
	 */
	public boolean addToNewHiddenBasicPicUnitList(BasicPicUnit bpu) {
		if (newHiddenPicUnitMap.containsKey(bpu.getKey())) {
			return false;// 已经存在
		} else {
			newHiddenPicUnitMap.put(bpu.getKey(), bpu);
			return true;
		}
	}

	/**
	 * 增加进新可见列表
	 * 
	 * @param bpu
	 * @return
	 * 
	 * $Author gmh
	 * 
	 * 2012-3-23 上午11:25:22
	 */
	public boolean addToNewVisibleBasicPicUnitList(BasicPicUnit bpu) {
		if (newVisiblePicUnitMap.containsKey(bpu.getKey())) {
			return false;// 已经存在
		} else {
			newVisiblePicUnitMap.put(bpu.getKey(), bpu);
			return true;
		}
	}

	/**
	 * 从新隐藏列表中删除
	 * 
	 * @param bpu
	 * @return
	 * 
	 * $Author gmh
	 * 
	 * 2012-3-23 上午11:26:05
	 */
	public boolean removeFromNewHiddenBasicPicUnitList(BasicPicUnit bpu) {
		if (newHiddenPicUnitMap.containsKey(bpu.getKey())) {
			newHiddenPicUnitMap.remove(bpu.getKey());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 从新可见列表中删除
	 * 
	 * @param bpu
	 * @return
	 * 
	 * $Author gmh
	 * 
	 * 2012-3-23 上午11:26:05
	 */
	public boolean removeFromNewVisibleBasicPicUnitList(BasicPicUnit bpu) {
		if (newVisiblePicUnitMap.containsKey(bpu.getKey())) {
			newVisiblePicUnitMap.remove(bpu.getKey());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取新隐藏图元列表
	 * 
	 * @return 2012-3-20 下午04:51:13
	 */
	public List<BasicPicUnit> getNewHiddenBasicPicUnitList() {
		if (newHiddenPicUnitMap.size() > 0) {
			List<BasicPicUnit> bps = new ArrayList<BasicPicUnit>();
			for (String key : newHiddenPicUnitMap.keySet()) {
				bps.add(newHiddenPicUnitMap.get(key));
			}
			return bps;
		} else {
			return null;
		}
	}

	/**
	 * 清除新增隐藏列表
	 * 
	 * 2012-3-20 下午04:57:10
	 */
	public void clearNewHiddenList() {
		if (newHiddenPicUnitMap != null) {
			newHiddenPicUnitMap.clear();
		}
	}

	/**
	 * 增加到隐藏列表
	 * 
	 * @param bpus
	 *            2012-3-20 下午04:56:29
	 */
	public void addToHiddenPicUnitMap(List<BasicPicUnit> bpus) {
		if (bpus == null || bpus.size() == 0) {
			return;
		}
		for (BasicPicUnit bp : bpus) {
			if (!hiddenPicUnitMap.containsKey(bp.getKey())) {
				hiddenPicUnitMap.put(bp.getKey(), bp);
			}
		}
	}

	/**
	 * 调整各图元类型是否可见
	 * 
	 * @param currentVisibleKM
	 * 
	 * $Author gmh
	 * 
	 * 2012-3-23 下午03:54:10
	 */
	public void modifyPicUnitTypeShowOrNot(double currentVisibleKM) {
		/*if (picUnitTypes != null && picUnitTypes.size() > 0) {
			for (PicUnitType putype : picUnitTypes) {
				if (putype.isNeedShow()) {//用户设定的可见
					if (putype.inShowRange(currentVisibleKM)) {//在可见公里数
						putype.setGonglishuVisible(true);
						
					} else {
						putype.setNeedShow(false);
					}
				}
			}
		}*/
		
		//che.yd---------begin-----
		if (picUnitTypes != null && picUnitTypes.size() > 0) {
			for (PicUnitType putype : picUnitTypes) {
//				System.out.println("图元类型："+putype.getName()+"，可见性：=="+putype.isNeedShow());
				if (putype.isNeedShow()) {//用户设定的可见
					if (putype.inShowRange(currentVisibleKM)) {//在可见公里数
//						System.out.println("公里数可见");
						putype.setGonglishuVisible(true);
						
					} else {
//						System.out.println("公里数不可见");
						putype.setGonglishuVisible(false);
					}
				}else{
					if (putype.inShowRange(currentVisibleKM)) {//在可见公里数
						putype.setGonglishuVisible(true);
						
					} else {
						putype.setGonglishuVisible(false);
					}
				}
				
			}
		}
		//che.yd---------end-----
	}

	/**
	 * 根据图元key找图元
	 * 
	 * @param bpuKey
	 * @return Mar 28, 2012 3:29:59 PM gmh
	 */
	public BasicPicUnit findABasicPicUnitByKey(String bpuKey) {
		BasicPicUnit bpu = null;
		// 先从可见列表找
		if (!visiblePicUnitMap.isEmpty()) {
			bpu = visiblePicUnitMap.get(bpuKey);
		}

		// 未找到，继续找
		if (bpu == null && !hiddenPicUnitMap.isEmpty()) {
			bpu = hiddenPicUnitMap.get(bpuKey);
		}

		return bpu;
	}

	/**
	 * 将图层从隐藏改为可见
	 * 
	 * Mar 30, 2012 11:17:29 AM gmh
	 */
	public void changeFromHiddenToVisible() {

		List<BasicPicUnit> bpus = new ArrayList<BasicPicUnit>();
		
		
		
		if (hiddenPicUnitMap != null && hiddenPicUnitMap.size() > 0) {
			//System.out.println("隐藏图层大小=="+hiddenPicUnitMap.size());
			for (String key : hiddenPicUnitMap.keySet()) {
				BasicPicUnit bpu = hiddenPicUnitMap.get(key);
				if (bpu.containedIn(this.getPicLayerManager()
						.getBackgroundLatLngRange())) {// 需要考虑图元是否在可见经纬度窗口内
					bpus.add(bpu);
					bpu.setVisible(true);
					this.addToNewVisibleBasicPicUnitList(bpu);
				}
			}
		}

		// 从隐藏队列删除
		if (bpus.size() > 0) {
			for (BasicPicUnit bpu : bpus) {
				hiddenPicUnitMap.remove(bpu);
			}
		}

	}

	/**
	 * 图层由可见变为不可见
	 * 
	 * Mar 30, 2012 11:27:09 AM gmh
	 */
	public void changeFromVisibleToHidden() {
		List<BasicPicUnit> bpus = new ArrayList<BasicPicUnit>();

		if (visiblePicUnitMap != null && visiblePicUnitMap.size() > 0) {
			for (String key : visiblePicUnitMap.keySet()) {
				BasicPicUnit bpu = visiblePicUnitMap.get(key);
				bpus.add(bpu);
				bpu.setVisible(false);
				this.addToNewHiddenBasicPicUnitList(bpu);
			}
		}

		visiblePicUnitMap.clear();
	}

	/**
	 * 使一种图元类型可见
	 * 
	 * @param put
	 *            Mar 30, 2012 11:32:48 AM gmh
	 */
	public void enableAPicUnitType(PicUnitType picUnitType) {
		picUnitTypes = this.getPicUnitTypes();
		PicUnitType put = null;

		// 找到该图元类型，让其可见
		if (picUnitTypes != null && picUnitTypes.size() > 0) {
			for (int i = 0; i < picUnitTypes.size(); i++) {
				put = picUnitTypes.get(i);
				if (put.getKey().equals(picUnitType.getKey())) {
					put.setNeedShow(true);
					break;
				}
			}
		}

		if (put != null) {
			// 遍历隐藏图元队列，如果有满足要求的图元：属于该种图元类型，且在经纬度窗口范围内
			List<BasicPicUnit> bpus = new ArrayList<BasicPicUnit>();

			if (hiddenPicUnitMap != null && hiddenPicUnitMap.size() > 0) {
				for (String key : hiddenPicUnitMap.keySet()) {
					BasicPicUnit bpu = hiddenPicUnitMap.get(key);
					try{
//						if (bpu.getPicUnitType().getCode().equals(put.getCode())// 类型相同
//								&& bpu.containedIn(this.getPicLayerManager()
//										.getBackgroundLatLngRange())) {// 需要考虑图元是否在可见经纬度窗口内
//							bpus.add(bpu);
//							bpu.setVisible(true);
//							this.addToNewVisibleBasicPicUnitList(bpu);
//						}}
					
						//che.yd------------begin-----------
						// 类型相同
						// 需要考虑图元是否在可见经纬度窗口内
						// 可见公里数
						if (bpu.getPicUnitType().getCode().equals(put.getCode())) {
							if(bpu.containedIn(this.getPicLayerManager()
									.getBackgroundLatLngRange()) && bpu.getPicUnitType().isGonglishuVisible()){
								bpus.add(bpu);
								bpu.setVisible(true);
								this.addToNewVisibleBasicPicUnitList(bpu);
							}
						}}
					//che.yd------------end---------------
					catch(Exception e){
						e.printStackTrace();
						//System.out.println("bpu==null-->"+(bpu==null) +",bpu.getPicUnitType()==null--->"+(bpu.getPicUnitType()==null) +","+bpu.toJson());
					}
				}
			}

			// 从隐藏队列删除
			if (bpus.size() > 0) {
				for (BasicPicUnit bpu : bpus) {
					hiddenPicUnitMap.remove(bpu);
				}
			}
		}
	}

	/**
	 * 使一种图元类型不可见
	 * 
	 * @param put
	 *            Mar 30, 2012 11:32:48 AM gmh
	 */
	public void disableAPicUnitType(PicUnitType picUnitType) {
		picUnitTypes = this.getPicUnitTypes();
		PicUnitType put = null;

		// 找到该图元类型，让其可见
		if (picUnitTypes != null && picUnitTypes.size() > 0) {
			for (int i = 0; i < picUnitTypes.size(); i++) {
				put = picUnitTypes.get(i);
				if (put.getKey().equals(picUnitType.getKey())) {
					put.setNeedShow(false);
					break;
				}
			}
		}

		if (put != null) {
			// 遍历可见图元队列，如果有满足要求的图元：属于该种图元类型，且在经纬度窗口范围内
			List<BasicPicUnit> bpus = new ArrayList<BasicPicUnit>();

			if (visiblePicUnitMap != null && visiblePicUnitMap.size() > 0) {
				for (String key : visiblePicUnitMap.keySet()) {
					BasicPicUnit bpu = visiblePicUnitMap.get(key);
//					if (bpu.getPicUnitType().getCode().equals(put.getCode()))// 类型相同
//					{
//						bpus.add(bpu);
//						bpu.setVisible(false);
//						this.addToNewHiddenBasicPicUnitList(bpu);
//					}
					
					//che.yd-----------begin--------
					if (bpu.getPicUnitType().getCode().equals(put.getCode()))// 类型相同
					{
						if(!bpu.containedIn(this.getPicLayerManager()
								.getBackgroundLatLngRange()) || !bpu.getPicUnitType().isGonglishuVisible())
						bpus.add(bpu);
						bpu.setVisible(false);
						this.addToNewHiddenBasicPicUnitList(bpu);
					}
					
					//che.yd-----------end----------
				}
			}

			// 从可见队列删除
			if (bpus.size() > 0) {
				for (BasicPicUnit bpu : bpus) {
					visiblePicUnitMap.remove(bpu);
				}
			}
		}
	}

	/**
	 * 通过picunittype code 获取一个picunittype
	 * @param code
	 * @return
	 * Apr 28, 2012 10:59:10 AM
	 * gmh
	 */
	public PicUnitType findAPicUnitTypeByCode(String code){
		for(PicUnitType pt:picUnitTypes){
			if(pt.getCode().equals(code)){
				return pt;
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
		sr.append("{key:'" + getKey() + "',layerName:'" + name + "',geList:[ ");
		if (!visiblePicUnitMap.isEmpty()) {
			for (String key : visiblePicUnitMap.keySet()) {
				BasicPicUnit bpu = visiblePicUnitMap.get(key);
				if (bpu.getLatlng().containedIn(
						this.getPicLayerManager().getBackgroundLatLngRange())
						&& bpu.getPicUnitType().isNeedShow()) {
					sr.append(bpu.toJson());
					sr.append(",");
				}
			}

			sr = sr.delete(sr.length() - 1, sr.length());
		}
		sr.append("]}");
		return sr.toString();
	}

	/**
	 * 基本信息json
	 * 
	 * @return Mar 30, 2012 12:25:13 PM gmh
	 */
	public String toBasicJson() {
		StringBuilder sr = new StringBuilder();
		sr.append("{key:'" + getKey() + "',layerName:'" + name + "'}");
		return sr.toString();
	}

	/**
	 * 返回可见图元的任务数量信息json结果
	 * 
	 * @return Author gmh 2012-3-23 上午10:06:02
	 */
	public String toGetTaskInfoJson() {
		StringBuilder sr = new StringBuilder();
		sr.append("{key:'" + getKey() + "',name:'" + name
				+ "',simpleGeListWithTaskInfo:[");
		if (!visiblePicUnitMap.isEmpty()) {
			for (String key : visiblePicUnitMap.keySet()) {
				BasicPicUnit bpu = visiblePicUnitMap.get(key);
				sr.append(bpu.toGetTaskInfoJson());
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
		return "";
	}

	/**
	 * 缩放级别改变json结果
	 * 
	 * @return Author gmh 2012-3-22 下午03:26:46
	 */
	public String toZoomChangedJson() {
		return "";
	}

	/**
	 * 获取新增可见图元列表的json
	 * 
	 * @return Mar 30, 2012 12:03:12 PM gmh
	 */
	public String getNewlyVisiblePicUnitJson() {
		StringBuilder bufForShow = new StringBuilder();
		// 新增可见图元列表
		List<BasicPicUnit> bpus = this.getNewVisibleBasicPicUnitList();
		if (bpus != null && bpus.size() > 0) {
			bufForShow.append("{");// 图层开始
			bufForShow.append("key:'" + this.getKey() + "',layerName:'"
					+ this.getName() + "',geList:[");
			for (BasicPicUnit bpu : bpus) {
				bufForShow.append(bpu.toJson());
				bufForShow.append(",");
			}
			bufForShow = bufForShow.delete(bufForShow.length() - 1, bufForShow
					.length());
			bufForShow.append("]");// gelist结束
			bufForShow.append("}");// 图层结束
		}

		return bufForShow.toString();
	}

	/**
	 * 获取新增隐藏图元的json数据
	 * 
	 * @return Mar 30, 2012 12:08:24 PM gmh
	 */
	public String getNewlyHiddenPicUnitJson() {
		StringBuilder bufForHide = new StringBuilder();

		List<BasicPicUnit> bpus = this.getNewHiddenBasicPicUnitList();
		if (bpus != null && bpus.size() > 0) {
			bufForHide.append("{");// 图层开始
			bufForHide.append("key:'" + this.getKey() + "',layerName:'"
					+ this.getName() + "',geList:[");
			for (BasicPicUnit bpu : bpus) {
				bufForHide.append(bpu.toSimpleJson());
				bufForHide.append(",");
			}
			bufForHide = bufForHide.delete(bufForHide.length() - 1, bufForHide.length());
			bufForHide.append("]");// gelist结束
			bufForHide.append("}");// 图层结束
		}

		return bufForHide.toString();
	}

}
