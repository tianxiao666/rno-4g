package com.iscreate.op.service.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.G4StsDescQueryCond;
import com.iscreate.op.action.rno.model.LteStsCellQueryResult;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;

public interface RnoLteStsAnaService {

	/**
	 * 查询符合条件的话统记录数
	 * @param g4StsDescQueryCond
	 * @return
	 * @author chen.c10
	 * @date 2015-7-17 下午7:27:27
	 * @company 怡创科技
	 * @version V1.0
	 */
	public long queryG4StsDescCnt(final G4StsDescQueryCond g4StsDescQueryCond);
	/**
	 * 分页查询符合条件的话统记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chen.c10
	 * @date 2015-7-17 下午7:27:02
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryG4StsDescByPage(final 
			G4StsDescQueryCond cond,final Page page);
	/**
	 * 查询4G话统数据
	 * @return
	 * @author chen.c10
	 * @date 2015-7-20 下午2:07:29
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryG4StsData(G4StsDescQueryCond g4StsDescQueryCond);
	/**
	 * 查询4G话统小区中文名列表
	 * @return
	 * @author chen.c10
	 * @date 2015-7-21 下午2:51:48
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<String> queryG4StsCellNameList();
	/**
	 * 通过MapGrid获取小区集合，并通过PmCellList进行筛选
	 * @param areaId
	 * @param mapGrid
	 * @param page
	 * @param freqType
	 * @param standardPoints
	 * @param cellNames
	 * @return
	 * @author chen.c10
	 * @date 2015-7-22 下午12:04:41
	 * @company 怡创科技
	 * @version V1.0
	 */
	public LteStsCellQueryResult getLteCellByMapGridWithStsCellList(long areaId,
			Map<String, String> mapGrid, Page page,String freqType,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints);
	/**
	 * 获取4G话统指标项
	 * @return
	 * @author chen.c10
	 * @date 2015-9-17 下午5:03:12
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> getRno4GStsIndexDesc();
}
