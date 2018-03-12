package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.G4StsDescQueryCond;
import com.iscreate.op.pojo.rno.RnoLteCellForPm;

public interface RnoLteStsAnaDao {
	
	/**
	 * 查询符合条件的4G话统记录数
	 * @param cond
	 * @return
	 * @author chen.c10
	 * @date 2015-7-17 下午7:30:04
	 * @company 怡创科技
	 * @version V1.0
	 */
	public long queryG4StsDescCnt(final G4StsDescQueryCond cond);
	/**
	 * 分页查询符合条件的4G话统数据
	 * @param cond
	 * @param page
	 * @return
	 * @author chen.c10
	 * @date 2015-7-17 下午7:30:21
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryG4StsDescByPage(final 
			G4StsDescQueryCond cond,final Page page);
	/**
	 * 查询4G话统数据
	 * @return
	 * @author chen.c10
	 * @date 2015-7-20 下午2:12:28
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> queryG4StsData(G4StsDescQueryCond g4StsDescQueryCond);
	/**
	 * 查询4G话统小区名列表
	 * @return
	 * @author chen.c10
	 * @date 2015-7-21 下午2:55:54
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<String> queryG4StsCellNameList();
	/**
	 * 分网格分页获取4G话统小区名
	 * @param areaStr
	 * @param blPoint
	 * @param trPoint
	 * @param page
	 * @return
	 * @author chen.c10
	 * @date 2015-7-27 下午7:25:38
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<String> getRnoLteCellByGridAndPageInAreaWithSts(String areaStr,
			String[] blPoint, String[] trPoint, final Page page);
	/**
	 * 根据小区名获取4G话统记录
	 * @param cellName
	 * @return
	 * @author chen.c10
	 * @date 2015-7-27 下午7:27:32
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<RnoLteCellForPm> getRnoLteCellForStsByCellName(String cellName);
	/**
	 * 通过小区名列表获取4G话统记录
	 * @param cellNameList
	 * @return
	 * @author chen.c10
	 * @date 2015-8-12 下午5:18:18
	 * @company 怡创科技
	 * @version V1.0
	 */
	public Map<String,List<RnoLteCellForPm>> getRnoLteCellForStsByCellNameList(List<String> cellNameList);
	/**
	 * 获取网格内的4G话统小区分页总数
	 * @param areaStr
	 * @param blPoint
	 * @param trPoint
	 * @param freqType
	 * @return
	 * @author chen.c10
	 * @date 2015-7-27 下午7:27:51
	 * @company 怡创科技
	 * @version V1.0
	 */
	public Integer getRnoLteStsCellCntByGridInArea(String areaStr,
			String[] blPoint, String[] trPoint, String freqType);
	/**
	 * 获取4G话统指标项
	 * @return
	 * @author chen.c10
	 * @date 2015-9-17 下午5:04:49
	 * @company 怡创科技
	 * @version V1.0
	 */
	public List<Map<String, Object>> getRno4GStsIndexDesc();
}
