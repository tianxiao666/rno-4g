package com.iscreate.op.dao.gisdispatch;

import java.util.List;
import java.util.Map;

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
import com.iscreate.op.pojo.gisdispatch.PicLayer;
import com.iscreate.op.pojo.gisdispatch.PicLayerManager;
import com.iscreate.op.pojo.gisdispatch.TaskInfo;
import com.iscreate.op.pojo.gisdispatch.UserLayerSetting;
import com.iscreate.op.pojo.gisdispatch.UserLayerTypeSetting;
import com.iscreate.op.pojo.gisdispatch.UserLittleIconSetting;

public interface GisDispatchDao {
	/**
	 * 根据用户id，返回用户的图层设置信息
	 * @param userId 用户id
	 * @return
	 */
	public List<GisDispatch_GraphLayerSetting> getUserGraphSettings(String userId);
	
	/**
	 * 根据用户id，返回用户的图层设置信息
	 * @param userId 用户id
	 * @return
	 */
	public Map<Long,UserLayerSetting> getUserGraphSettingsConfig(String userId);
	
	
	/**
	 * 获取用户对于图元类型设置的信息
	 * @param userId 用户id
	 * @return
	 */
	public List<GisDispatch_GraphElementTypeSetting> getUserGraphElementTypeSettings(String userId);
	
	
	/**
	 * 获取用户对于图元类型设置的信息
	 * @param userId 用户id
	 * @return
	 */
	public Map<Long,UserLayerTypeSetting> getUserGraphElementTypeSettingsConfig(String userId);
	
	
	/**
	 * 获取用户设定的脚标显示信息
	 * @param userId
	 * @return
	 */
	public List<GisDispatch_LittleIconSetting> getUserGraphLittleIconSettings(String userId);
	
	
	/**
	 * 获取用户设定的脚标显示信息
	 * @param userId
	 * @return
	 */
	public Map<Long,UserLittleIconSetting> getUserGraphLittleIconSettingsConfig(String userId);
	
	/**
	 * 获取所有的图层信息
	 * @return
	 */
	public List<GisDispatch_GraphLayer> getGraphLayer();
	
	/**
	 * 通过图层的id获取其下的图元类型
	 * @param glId 图层id
	 * @return
	 */
	public List<GisDispatch_GraphElementType> getGraphElementTypesByGraphLayer(long glId);
	
	/**
	 * 通过图层entity获取所有其下的图元类型
	 * @param gl图层
	 * @return
	 */
	public List<GisDispatch_GraphElementType> getGraphElementTypesByGraphLayer(GisDispatch_GraphLayer gl);
	
	/**
	 * 获取某个图元类型下的脚标
	 * @param geType 图元类型
	 * @return
	 */
	public List<GisDispatch_LittleIcon> getLittleIconsOfGraphElementType(GisDispatch_GraphElementType geType);
	
	/**
	 * 获取某个图元类型下的脚标
	 * @param geTypeId 图元类型id
	 * @return
	 */
	public List<GisDispatch_LittleIcon> getLittleIconsOfGraphElementType(long geTypeId);
	
	/**
	 * 获取所有的图元类型与公里数的设定信息
	 * @return
	 */
	public List<GisDispatch_GraphElementTypeAndMileSetting> getAllMileSettings();
	
	/**
	 * 获取某个图元类型的可见公里数配置
	 * @param geType 图元类型
	 * @return
	 */
	public List<GisDispatch_GraphElementTypeAndMileSetting> getMileSettingsOfGeType(GisDispatch_GraphElementType geType);
	
	/**
	 *获取某个图元类型的可见公里数配置
	 * @param geTypeId 图元类型id
	 * @return
	 */
	public List<GisDispatch_GraphElementTypeAndMileSetting> getMileSettingsOfGeType(long geTypeId);
	
	
	/**
	 * 根据Id获取用户可创建工单信息
	 * @param workId
	 * @return
	 */
	public GisDispatch_UserWork getUserWorkByWorkId(String workId);
	
	
	/**
	 * 添加活动区域
	 * @param activeArea 
	 */
	public void addGisdispatchActiveArea(GisDispatch_ActiveArea activeArea);
	
	/**
	 * 修改活动区域
	 * @param activeArea
	 */
	public void updateGisdispatchActiveArea(GisDispatch_ActiveArea activeArea);
	
	/**
	 * 删除活动区域
	 * @param activeArea
	 */
	public void deleteGisdispatchActiveArea(GisDispatch_ActiveArea activeArea);
	
	/**
	 * 根据Id删除活动区域
	 * @param id
	 */
	public void deleteGisdispatchActiveArea(long id);
	
	/**
	 * 获取最活跃的几个区域
	 * @param returnsNumber 返回数量
	 * @return
	 */
	public List<GisDispatch_ActiveArea> getTopActiveArea(int returnsNumber);
	
	/**
	 * 根据区域Id获取区域活动信息
	 * @param areaId
	 * @return
	 */
	public GisDispatch_ActiveArea getGisdispatchActiveAreaByAreaId(long areaId);
	
}
