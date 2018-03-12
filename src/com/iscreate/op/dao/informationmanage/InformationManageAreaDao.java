package com.iscreate.op.dao.informationmanage;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.informationmanage.Area;

public interface InformationManageAreaDao extends BaseDao<Area> {
	
	
	
	public List<Map<String, String>> getAllProvince ();
	
}
