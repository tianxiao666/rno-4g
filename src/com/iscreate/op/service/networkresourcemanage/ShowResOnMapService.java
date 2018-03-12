package com.iscreate.op.service.networkresourcemanage;


import java.util.List;
import java.util.Map;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;

public interface ShowResOnMapService {
	
	/**
	 * 获取每条光缆段的详细信息（包括所属光缆及光缆段两端的设施，有坐标的上级设施)
	 * @param fiberSectionEntityArray
	 * @return
	 */
	public Map<String, Object> getFiberSectionFacility(
			ApplicationEntity fiberSectionEntityArray);
	
	/**
	 * 获取纤芯的详细信息（包括纤芯所属的光缆段，及两端的设施，有坐标的上级设施）
	 * @param fiberSectionEntityArray
	 * @return
	 */
	public List<Map<String, Object>> getFiberCoreFacility(
			ApplicationEntity[] fiberSectionEntityArray);
	
	/**
	 * 获取局向光纤的详细信息（包括局向光纤所属的光路纤芯，及两端的设施，有坐标的上级设施）
	 * @param opticalRouteSP2SPEntityArray
	 * @return
	 */
	public Map<String,Object> getOpticalRouteSP2SPFacility(ApplicationEntity opticalRouteSP2SPEntity,String areaId);
	
	/**
	 * 获取传输段的详细信息（包括传输段所属的传输系统,关联的光路纤芯,两端的设施，有坐标的上级设施）
	 * @param transmissionSectionEntityArray
	 * @return
	 */
	public List<Map<String,Object>> getTransmissionSectionFacility(ApplicationEntity[] transmissionSectionEntityArray,String areaId);
	
	/**
	 * 获取接头的父设施
	 * @param jointEntity
	 * @return
	 */
	public ApplicationEntity getJointParent(ApplicationEntity jointEntity);
	
	/**
	 * 获取接头
	 * @param entityArray
	 * @return
	 */
	public List<Map<String,Object>> getJoint(ApplicationEntity[] entityArray);
	
	/**
	 * 获取ODF父设施
	 * @param odfEntity
	 * @return
	 */
	public ApplicationEntity getOdfParent(ApplicationEntity odfEntity);
	
	/**
	 * 获取ODF
	 * @param stationEntityArray
	 * @return
	 */
	public List<Map<String, Object>> getOdf(ApplicationEntity[] stationEntityArray);
}
