package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.pojo.rno.Cell;
import com.iscreate.op.pojo.rno.RnoCellDescriptor;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoLteCell;
import com.iscreate.op.pojo.rno.RnoNcell;

public interface RnoCellDao {

	/**
	 * 通过areaid或取小区
	 * @param areaId
	 * @return
	 * Sep 11, 2013 11:21:04 AM
	 * gmh
	 */
	public List<String> getCellNameByAreaId(Long areaId);
	
	/**
	 * 获取区域的默认配置方案id
	 * @param areaId
	 * @return
	 * Sep 12, 2013 3:51:24 PM
	 * gmh
	 */
	public RnoCellDescriptor getSystemCellDescriptorByAreaId(Long areaId);
	
	/**
	 * 增加指定区域的默认配置方案
	 * @param areaId
	 * @param cellDesc
	 * @return
	 * Sep 12, 2013 3:53:52 PM
	 * gmh
	 */
	public Long saveSystemCellDecriptorUnderArea(Long areaId,RnoCellDescriptor cellDesc);
	
	/**
	 * 分页查询指定条件的小区
	 * @param page
	 * @param where
	 * @return
	 * Sep 16, 2013 3:54:02 PM
	 * gmh
	 */
	public List<Cell> queryCellByPage(Page page,String where);
	
	/**
	 * 获取符合条件的记录的数量
	 * 
	 * @param where
	 * @return Sep 16, 2013 4:20:57 PM gmh
	 */
	public int getTotalCellCntMeetCondition(final String where) ;
	
	
	/**
	 * 获取某个区域下的小区数量
	 * @param areaId
	 * @return
	 * @author brightming
	 * 2014-8-11 上午10:39:34
	 */
	public int getGisCellCntInArea(long areaId);
	/**
	 * 获取指定区/县区域下的小区
	 * @param areaId
	 * @return
	 * Sep 17, 2013 2:02:04 PM
	 * gmh
	 */
	public List<RnoGisCell> getRnoGisCellInArea(long areaId);

	/**
	 * 分页获取指定区/县区域下的小区
	 * @param areaId
	 * @param page
	 * @return
	 * @author brightming
	 * 2014-8-11 上午10:22:31
	 */
	public List<RnoGisCell> getRnoGisCellInAreaByPage(final long areaId,final Page page) ;
	
	/**
	 * 分页查询满足条件的邻区
	 * @param page
	 * @param where
	 * @return
	 * Sep 17, 2013 3:47:44 PM
	 * gmh
	 */
	public List<RnoNcell> queryNCellByPage(Page page, String where);

	/**
	 * 获取满足条件的邻区关系的数量
	 * @param buildNcellQuerySqlWhere
	 * @return
	 * Sep 17, 2013 4:33:53 PM
	 * gmh
	 */
	public int getTotalNcellCntMeetCondition(String buildNcellQuerySqlWhere);

	/**
	 * 删除id列表指定的邻区
	 * @param ncellIds
	 * Sep 17, 2013 5:40:27 PM
	 * gmh
	 */
	public int deleteNcellByIds(List<Long> allAllowAreaIds,String ncellIds);

	/**
	 * 根据小区label获取小区详情
	 * @param label
	 * @return
	 * Sep 18, 2013 5:51:03 PM
	 * gmh
	 */
	public Cell getCellByLabel(String label);
	
	/**
	 * 通过ID查询一条小区数据记录
	 * @param id
	 * @return
	 */
	public Cell queryCellById(long id);
	
	/**
	 * 通过ID保存一个实体对象
	 * @param cell
	 * @return
	 */
	public void updateCellInfo(Cell cell);

	/**
	 * 获取指定区域下的小区的邻区
	 * @param cell
	 * @return
	 * @author brightming
	 * 2013-9-25 上午11:19:41
	 */
	public List<RnoNcell> getNcellByCell(String cell);

	/**
	 * 执行查询
	 * @param sql
	 * @return
	 * @author brightming
	 * 2013-10-30 下午5:40:27
	 */
	public List<Map<String, Object>> executeQuery(String sql);
	/**
	 * 从小区配置描述表中得到最新的小区配置描述标识CELL_DESCRIPTOR_ID
	 * @return
	 * @author chao.xj
	 * @date 2013-11-5下午03:40:55
	 */
	public long saveCellDescAndGetId(RnoCellDescriptor rnoCellDescriptor);
	/**
	 * 获取指定区/县区域下的小区通过配置和区域
	 * @title 
	 * @param reSelected
	 * @param areaIds
	 * @param configIds
	 * @return
	 * @author chao.xj
	 * @date 2014-4-15上午11:59:01
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RnoGisCell> getRnoGisCellUseConfigIdOrArea(boolean reSelected,String areaIds,String configIds);
	
	/**
	 * 获取指定区域指定频点类型的小区的数量
	 * @param areaId
	 * @param freqType
	 * @return
	 * @author brightming
	 * 2014-8-11 上午11:38:08
	 */
	public int getRnoGisCellCntByFreqTypeInArea(final long areaId,final String freqType);
	
	/**
	 * 
	 * @title 获取指定区/县区域下的小区(带频点类型)
	 * @param areaId
	 * @param freqType
	 * @return
	 * @author chao.xj
	 * @date 2014-6-27下午07:22:32
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RnoGisCell> getAllRnoGisCellByFreqTypeInArea(final long areaId,final String freqType);

	/**
	 * 分页获取指定频点类型的小区数据
	 * @param areaId
	 * @param freqType
	 * @param page
	 * @return
	 * @author brightming
	 * 2014-8-11 上午11:08:29
	 */
	public List<RnoGisCell> getRnoGisCellInAreaByFreqTypeAndPage(long areaId,
			String freqType, Page page);
	/**
	 * 获取指定区域的网格下的指定频点类型的小区数量
	 * @param areaStr 格式：xx,xx,xx
	 * @param blPoint
	 * @param trPoint
	 * @param freqType
	 * @return
	 * @author peng.jm
	 * @date 2014-9-18下午02:36:44
	 */
	public int getRnoGisCellCntByGridInArea(String areaStr, String[] blPoint,
			String[] trPoint, String freqType);

	/**
	 * 分页获取指定区域的网格下的指定频点类型的小区
	 * @param areaStr 格式：xx,xx,xx
	 * @param blPoint
	 * @param trPoint
	 * @param freqType
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-9-18下午02:36:55
	 */
	public List<RnoGisCell> getRnoGisCellByGridAndPageInArea(String areaStr, String[] blPoint,
			String[] trPoint, String freqType, Page page);
	/**
	 * 获取额外的giscell用于还没加载到却需要先显示的情况
	 * @param cells
	 * @param areaStr 格式：xx,xx,xx
	 * @author peng.jm
	 * @date 2014-9-19下午07:00:39
	 */
	public List<RnoGisCell> getRelaCellByLabels(String cells, String areaStr);

	/**
	 * 通过区域id获取其所有子区域（包括自身）
	 * @param areaId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-28下午04:27:15
	 */
	/*public List<Map<String, Object>> getSubAreaByAreaId(long areaId);*/
	
	/**
	 * 通过小区参数获取cell进行小区数据预加载
	 * @param cellParam
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-28下午07:06:07
	 */
	public List<RnoGisCell> getRelaCellByCellParamAndCityId(
			Map<String, String> cellParam, String areaStr);
	/**
	 * 通过cell获取其邻区数据及主小区数据进行预加载
	 * @param cell
	 * @param areaStr
	 * @return
	 * @author peng.jm
	 * @date 2014-9-28下午06:51:54
	 */
	public List<RnoGisCell> getNcellDetailsByCellandCityId(String cell,
			String areaStr);

	public List<RnoLteCell> getRnoLteCellByGridAndPageInArea(String areaStr,
			String[] blPoint, String[] trPoint, String freqType, Page page);

	public Integer getRnoLteCellCntByGridInArea(String areaStr,
			String[] blPoint, String[] trPoint, String freqType);

	public List<RnoLteCell> getLteCellByCellParamAndCityId(
			Map<String, String> cellParam, String areaStr);
/**
 * 通过小区名获取小区数据
 * @param busCellId
 * @return
 */
	public RnoLteCell getLteCellByBusCellId(String busCellId);

}
