package com.iscreate.op.service.informationmanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.pojo.informationmanage.Area;

public interface InformationManageAreaService extends BaseService<Area> {

	public List<Map<String, Object>> getAreaTreeView( int max , String provinceId );

	/**
	 * 获取所有省份信息
	 * @return 省份信息集合
	 */
	public List<Map<String,String>> getAllProvince();

	public void getSubAreaRecursion(List<Map<String,String>> list, List<Map<String,Object>> list_map, int lv, int maxLv);

	public List<Map<String,String>> getSubArea(String areaId);

	
}
