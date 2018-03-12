package com.iscreate.op.service.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.G4HoDescQueryCond;
import com.iscreate.op.action.rno.model.G4KpiDescQueryCond;
import com.iscreate.op.action.rno.model.G4MrDescQueryCond;

public interface RnoResourceManagerHbaseService {

	/**
	 * 
	 * @title 分页获取Hbase的Mr数据描述表的数据
	 * @param cond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2015-3-18下午1:52:28
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, String>> queryMrDataFromHbaseByPage(
			G4MrDescQueryCond cond, Page newPage);
	
	/**
	 * 
	 * @title 分页获取Hbase的Ho数据描述表的数据
	 * @param cond
	 * @param newPage
	 * @return
	 */
	public List<Map<String, String>> queryHoDataFromHbaseByPage(
			G4HoDescQueryCond cond, Page newPage);
	/**
	 * 
	 * @title 分页获取Hbase的kpi 数据描述表的数据
	 * @param cond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2015-9-17下午4:55:28
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, String>> queryKpiDataFromHbaseByPage(
			G4KpiDescQueryCond cond, Page newPage);
}
