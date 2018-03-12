package com.iscreate.op.dao.gisdispatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.constant.GisDispatchGraphConstant;
import com.iscreate.op.constant.OrganizationConstant;
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
import com.iscreate.op.pojo.gisdispatch.UserLayerSetting;
import com.iscreate.op.pojo.gisdispatch.UserLayerTypeSetting;
import com.iscreate.op.pojo.gisdispatch.UserLittleIconSetting;
import com.iscreate.op.pojo.organization.Staff;
import com.iscreate.op.service.gisdispatch.DoubleBackgroundLatLngCalculator;
import com.iscreate.op.service.gisdispatch.GisDaiFactory;
import com.iscreate.plat.location.pojo.PdaGpsLocation;

public class GisDispatchDaoImpl implements GisDispatchDao {
	
	private HibernateTemplate hibernateTemplate;
	
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 根据用户id，返回用户的图层设置信息
	 * 
	 * @param userId 用户id
	 * @return
	 */
	public List<GisDispatch_GraphLayerSetting> getUserGraphSettings(String userId) {
		List<GisDispatch_GraphLayerSetting> graphLayerSettingList = null;
		try {
			
			String hql = "from GisDispatch_GraphLayerSetting where userId='"+userId+"'";
			graphLayerSettingList = hibernateTemplate.find(hql);
			
			// 如果没有当前用户的左边tree记录,就用default用户去查找
			if (graphLayerSettingList == null || graphLayerSettingList.size() == 0) {
				String defaultUserId = "default";
				if(userId.indexOf("@")>-1){
					defaultUserId = defaultUserId+userId.substring(userId.indexOf("@"));
				}
				String hql2 = "from GisDispatch_GraphLayerSetting  where userId='"+defaultUserId+"'";
				graphLayerSettingList = hibernateTemplate.find(hql2);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return graphLayerSettingList;
	}

	/**
	 * 根据用户id，返回用户的图层设置信息
	 * 
	 * @param userId
	 *            用户id
	 * @return
	 */
	public Map<Long, UserLayerSetting> getUserGraphSettingsConfig(String userId) {
		List<GisDispatch_GraphLayerSetting> graphLayerSettingList = null;
		Map<Long, UserLayerSetting> resultMap = new HashMap<Long, UserLayerSetting>();
		try {
			if (userId == null || "".equals(userId)) {
				//USERID 为空
			}
			graphLayerSettingList = getUserGraphSettings(userId);
			
			if (graphLayerSettingList != null && graphLayerSettingList.size() > 0) {
				for (GisDispatch_GraphLayerSetting gls : graphLayerSettingList) {
					UserLayerSetting uls = new UserLayerSetting();
					uls.setId(gls.getId());
					uls.setShowOrNot(Boolean.valueOf(gls.getShowOrNot()+""));
					uls.setPicLayerId(Long.valueOf(gls.getGl_id()+""));
					uls.setUserId(userId);
					PicLayer pl = new PicLayer();
					pl.setId(uls.getPicLayerId());
					pl.setShow(uls.isShowOrNot());
					
					GisDispatch_GraphLayer layer = hibernateTemplate.get(GisDispatch_GraphLayer.class , gls.getGl_id());
					if (layer != null) {
						pl.setName(layer.getName());
					}
					uls.setPicLayer(pl);
					resultMap.put(uls.getPicLayerId(), uls);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	/**
	 * 获取用户对于图元类型设置的信息
	 * 
	 * @param userId
	 *            用户id
	 * @return
	 */
	public List<GisDispatch_GraphElementTypeSetting> getUserGraphElementTypeSettings(String userId) {
		List<GisDispatch_GraphElementTypeSetting> graphElementTypeSettingList = null;
		try {
			if (userId == null || "".equals(userId)) {
				return null;
			}
			String hql = "from GisDispatch_GraphElementTypeSetting where userId='"+userId+"'";
			graphElementTypeSettingList = hibernateTemplate.find(hql);
			
			// 如果没有当前用户的左边tree记录,就用default用户去查找
			if (graphElementTypeSettingList == null || graphElementTypeSettingList.size() == 0) {
				String defaultUserId = "default";
				if(userId.indexOf("@")>-1){
					defaultUserId = defaultUserId+userId.substring(userId.indexOf("@"));
				}
				graphElementTypeSettingList = hibernateTemplate.find("from GisDispatch_GraphElementTypeSetting where userId='"+defaultUserId+"'");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return graphElementTypeSettingList;
	}

	/**
	 * 获取用户对于图元类型设置的信息
	 * 
	 * @param userId
	 *            用户id
	 * @return
	 */
	public Map<Long, UserLayerTypeSetting> getUserGraphElementTypeSettingsConfig(
			String userId) {
		List<GisDispatch_GraphElementTypeSetting> graphElementTypeSettingList = null;
		Map<Long, UserLayerTypeSetting> resultMap = new HashMap<Long, UserLayerTypeSetting>();
		try {
			if (userId == null || "".equals(userId)) {
				return null;
			}
			graphElementTypeSettingList = getUserGraphElementTypeSettings(userId);
			
			if (graphElementTypeSettingList != null
					&& graphElementTypeSettingList.size() > 0) {
				for (GisDispatch_GraphElementTypeSetting ges : graphElementTypeSettingList) {
					UserLayerTypeSetting uts = new UserLayerTypeSetting();

					uts.setId(Long.valueOf(ges.getId()+""));
					uts.setUserId(userId);
					uts.setVisible(Boolean.valueOf(ges.getIsVisible()+""));
					uts.setPicUnitTypeId(Long.valueOf(ges.getGeType_id()+""));

					PicUnitType put = new PicUnitType();
					put.setId(uts.getPicUnitTypeId());
					put.setNeedShow(uts.isVisible());
					
					GisDispatch_GraphElementType ge = hibernateTemplate.get(GisDispatch_GraphElementType.class, ges.getGeType_id());
					if (ge != null) {
						put.setName(ge.getType());
					}
					uts.setPicUnitType(put);
					resultMap.put(uts.getPicUnitTypeId(), uts);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 获取用户设定的脚标显示信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<GisDispatch_LittleIconSetting> getUserGraphLittleIconSettings(String userId) {
		List<GisDispatch_LittleIconSetting> graphLittleIconSettingList = null;
		try {
			if (userId == null || "".equals(userId)) {
				return null;
			}
			
			String hql = "from GisDispatch_LittleIconSetting where userId='"+userId+"'";
			graphLittleIconSettingList = hibernateTemplate.find(hql);
			
			// 如果没有当前用户的左边tree记录,就用default用户去查找
			if (graphLittleIconSettingList == null || graphLittleIconSettingList.size() == 0) {
				String defaultUserId = "default";
				if(userId.indexOf("@")>-1){
					defaultUserId = defaultUserId+userId.substring(userId.indexOf("@"));
				}
				hql = "from GisDispatch_LittleIconSetting where userId='"+defaultUserId+"'";
				graphLittleIconSettingList = hibernateTemplate.find(hql);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return graphLittleIconSettingList;
	}

	/**
	 * 获取用户设定的脚标显示信息
	 * 
	 * @param userId
	 * @return
	 */
	public Map<Long, UserLittleIconSetting> getUserGraphLittleIconSettingsConfig(
			String userId) {
		List<GisDispatch_LittleIconSetting> graphLittleIconSettingList = null;
		Map<Long, UserLittleIconSetting> resultMap = new HashMap<Long, UserLittleIconSetting>();
		try {
			if (userId == null || "".equals(userId)) {
				
			}
			graphLittleIconSettingList = getUserGraphLittleIconSettings(userId);

			if (graphLittleIconSettingList != null && graphLittleIconSettingList.size() > 0) {
				for (GisDispatch_LittleIconSetting gls : graphLittleIconSettingList) {
					UserLittleIconSetting uis = new UserLittleIconSetting();

					uis.setId(Long.valueOf(gls.getId()+""));
					uis.setUserId(userId);
					uis.setShowOrNot(Boolean.valueOf(gls.getShowOrNot()+""));
					uis.setLittleIconId(Long.valueOf(gls.getLittleicon_id()+""));

					LittleIcon li = new LittleIcon();
					li.setId(uis.getLittleIconId());
					li.setNeedShow(uis.isShowOrNot());

					GisDispatch_LittleIcon gl = hibernateTemplate.get(GisDispatch_LittleIcon.class, gls.getLittleicon_id());
					
					if (gl != null) {
						li.setName(gl.getLittleiconName()+"");
					}
					uis.setLittleIcon(li);
					resultMap.put(uis.getLittleIconId(), uis);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 获取所有的图层信息
	 * 
	 * @return
	 */
	public List<GisDispatch_GraphLayer> getGraphLayer() {
		List<GisDispatch_GraphLayer> allGraphLayerList = null;
		try {
			String hql = "from GisDispatch_GraphLayer";
			allGraphLayerList = hibernateTemplate.find(hql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allGraphLayerList;
	}

	/**
	 * 通过图层的id获取其下的图元类型
	 * 
	 * @param glId
	 *            图层id
	 * @return
	 */
	public List<GisDispatch_GraphElementType> getGraphElementTypesByGraphLayer(long glId) {
		List<GisDispatch_GraphElementType> geTypeList = null;
		try {
			if (glId == 0) {
				return null;
			}
			
			String hql = "from GisDispatch_GraphElementType where gl_id="+glId;
			geTypeList = hibernateTemplate.find(hql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return geTypeList;
	}

	/**
	 * 通过图层entity获取所有其下的图元类型
	 * 
	 * @param gl图层
	 * @return
	 */
	public List<GisDispatch_GraphElementType> getGraphElementTypesByGraphLayer(GisDispatch_GraphLayer gl) {
		List<GisDispatch_GraphElementType> geTypeList = null;
		try {
			if (gl == null) {
				return null;
			}

			if (Long.valueOf(gl.getId()+"") == 0) {
				return null;
			}
			String hql = "from GisDispatch_GraphElementType where gl_id="+gl.getId();
			geTypeList = hibernateTemplate.find(hql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return geTypeList;
	}

	/**
	 * 获取某个图元类型下的脚标
	 * 
	 * @param geType
	 *            图元类型
	 * @return
	 */
	public List<GisDispatch_LittleIcon> getLittleIconsOfGraphElementType(GisDispatch_GraphElementType geType) {
		List<GisDispatch_LittleIcon> littleIconsList = null;
		try {
			if (geType == null) {
				return null;
			}

			if (Long.valueOf(geType.getId()+"") == 0) {
				return null;
			}

			String hql = "from GisDispatch_LittleIcon where geType_id="+geType.getId();
			littleIconsList = hibernateTemplate.find(hql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return littleIconsList;
	}

	/**
	 * 获取某个图元类型下的脚标
	 * 
	 * @param geTypeId
	 *            图元类型id
	 * @return
	 */
	public List<GisDispatch_LittleIcon> getLittleIconsOfGraphElementType(long geTypeId) {
		List<GisDispatch_LittleIcon> littleIconsList = null;
		try {
			if (geTypeId == 0) {
				return null;
			}
			
			String hql = "from GisDispatch_LittleIcon where geType_id="+geTypeId;
			littleIconsList = hibernateTemplate.find(hql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return littleIconsList;
	}

	/**
	 * 获取所有的图元类型与公里数的设定信息
	 * 
	 * @return
	 */
	public List<GisDispatch_GraphElementTypeAndMileSetting> getAllMileSettings() {
		List<GisDispatch_GraphElementTypeAndMileSetting> allMileSettingsList = null;
		try {
			
			String hql = "from GisDispatch_GraphElementTypeAndMileSetting";
			allMileSettingsList = hibernateTemplate.find(hql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allMileSettingsList;
	}

	/**
	 * 获取某个图元类型的可见公里数配置
	 * 
	 * @param geType
	 *            图元类型
	 * @return
	 */
	public List<GisDispatch_GraphElementTypeAndMileSetting> getMileSettingsOfGeType(GisDispatch_GraphElementType geType) {
		List<GisDispatch_GraphElementTypeAndMileSetting> mileSettingList = null;
		try {
			if (geType == null) {
				return null;
			}
			if (geType.getId() == 0) {
				return null;
			}
			
			String hql = "from GisDispatch_GraphElementTypeAndMileSetting where geType_id="+geType.getId();
			mileSettingList = hibernateTemplate.find(hql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mileSettingList;
	}

	/**
	 * 获取某个图元类型的可见公里数配置
	 * 
	 * @param geTypeId
	 *            图元类型id
	 * @return
	 */
	public List<GisDispatch_GraphElementTypeAndMileSetting> getMileSettingsOfGeType(long geTypeId) {
		List<GisDispatch_GraphElementTypeAndMileSetting> mileSettingList = null;
		try {
			if (geTypeId == 0) {
				return null;
			}

			String hql = "from GisDispatch_GraphElementTypeAndMileSetting where geType_id="+geTypeId;
			mileSettingList = hibernateTemplate.find(hql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mileSettingList;
	}

	/**
	 * 根据Id获取用户可创建工单信息
	 * @param workId
	 * @return
	 */
	public GisDispatch_UserWork getUserWorkByWorkId(String workId) {
		return hibernateTemplate.get(GisDispatch_UserWork.class, Integer.valueOf(workId));
	}
	
	/**
	 * 添加活动区域
	 * @param activeArea 
	 */
	public void addGisdispatchActiveArea(GisDispatch_ActiveArea activeArea) {
		hibernateTemplate.save(activeArea);
	}

	/**
	 * 删除活动区域
	 * @param activeArea
	 */
	public void deleteGisdispatchActiveArea(GisDispatch_ActiveArea activeArea) {
		hibernateTemplate.delete(activeArea);
	}

	/**
	 * 根据Id删除活动区域
	 * @param id
	 */
	public void deleteGisdispatchActiveArea(long id) {
		GisDispatch_ActiveArea activeArea = hibernateTemplate.get(GisDispatch_ActiveArea.class, id);
		if(activeArea!=null){
			hibernateTemplate.delete(activeArea);
		}
	}

	/**
	 * 修改活动区域
	 * @param activeArea
	 */
	public void updateGisdispatchActiveArea(GisDispatch_ActiveArea activeArea) {
		hibernateTemplate.update(activeArea);
	}


	/**
	 * 获取最活跃的几个区域
	 * @param returnsNumber 返回数量
	 * @return
	 */
	public List<GisDispatch_ActiveArea> getTopActiveArea(int returnsNumber) {
		String hql = "FROM GisDispatch_ActiveArea ORDER BY activeNumber DESC LIMIT 0,"+returnsNumber;
		return hibernateTemplate.find(hql);
	}

	/**
	 * 根据区域Id获取区域活动信息
	 * @param areaId
	 * @return
	 */
	public GisDispatch_ActiveArea getGisdispatchActiveAreaByAreaId(
			long areaId) {
		GisDispatch_ActiveArea activeArea = null;
		String hql = "from GisDispatch_ActiveArea where areaId="+areaId;
		List<GisDispatch_ActiveArea> resList = hibernateTemplate.find(hql);
		if(resList!=null && !resList.isEmpty()){
			activeArea = resList.get(0);
		}
		return activeArea;
	}
	
}
