package com.iscreate.op.dao.rno;

import java.util.List;

import com.iscreate.op.pojo.rno.RnoBsc;
import com.iscreate.op.pojo.rno.RnoBscRelaArea;

public interface RnoBscDao {

	/**
	 * 获取所有的bsc
	 * @return
	 * Sep 11, 2013 2:12:15 PM
	 * gmh
	 */
	public List<RnoBsc> getAllBsc();

	/**
	 * 在指定区域插入新的bsc对象
	 */
	public Long insertBsc(long areaId,RnoBsc bsc);
	
	/**
	 * 获取指定区域下的bsc
	 * @param areaId
	 * @return
	 * Sep 16, 2013 11:18:27 AM
	 * gmh
	 */
	public List<RnoBsc> getBscsResideInArea(long areaId);
	
	/**
	 * 
	 * @author Liang YJ
	 * @date 2014-1-28 下午3:35:13
	 * @param areaId
	 * @return
	 * @description 根据areaId返回该区域下bscId和areaId关系的记录
	 */
	public List<RnoBscRelaArea> getBscRelaAreaByAreaId(long areaId);
}
