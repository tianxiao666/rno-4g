package com.iscreate.op.service.rno;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.CellCondition;
import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.action.rno.model.Eri2GNcsDescQueryCond;
import com.iscreate.op.action.rno.model.GisCellQueryResult;
import com.iscreate.op.action.rno.model.Hw2GMrrDescQueryCond;
import com.iscreate.op.action.rno.model.LteCellQueryResult;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.Cell;
import com.iscreate.op.pojo.rno.RnoBsc;
import com.iscreate.op.pojo.rno.RnoLteCell;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.pojo.rno.RnoNcell;

public interface RnoResourceManagerService {

	
	/**
	 * 获取用户可访问的指定级别的区域
	 */
	public List<Area> gisfindAreaInSpecLevelListByUserId(String accountId,String areaLevel);
	
	/**
	 * 获取指定区域下的bsc列表
	 * @param areaId
	 * @return
	 * Sep 16, 2013 11:16:17 AM
	 * gmh
	 */
	public List<RnoBsc> getBscsResideInAreaByAreaId(long areaId);
	
	
	/**
	 * 分页查询小区
	 * @param page
	 * @param condition
	 * @param allAllowAreaIds
	 * @return
	 * Sep 16, 2013 3:49:17 PM
	 * gmh
	 */
	public List<Cell> queryCellByPage(Page page,CellCondition condition,List<Long> allAllowAreaIds);
	
	/**
	 * 获取满足条件的小区的总数
	 * @param conditions
	 * @param rebuildCache
	 * 是否重新生成缓存
	 * @return
	 * Sep 16, 2013 5:29:04 PM
	 * gmh
	 */
	public int getTotalQueryCellCnt(String conditions,boolean rebuildCache);
	
	/**
	 * 分页获取区/县的giscell
	 * 
	 * @param areaId
	 *            指定区域
	 * @param page
	 *            分页参数
	 * 
	 * @return Sep 17, 2013 11:54:22 AM gmh
	 * 
	 */
	public GisCellQueryResult getGisCellByPage(long areaId, Page page) ;


	/**
	 * 分页查询邻区
	 * @param page
	 * @param condition
	 * @param allAllowAreaIds
	 * @return
	 * Sep 17, 2013 3:45:01 PM
	 * gmh
	 */
	public List<RnoNcell> queryNCellByPage(Page page, CellCondition condition,
			List<Long> allAllowAreaIds);


	/**
	 *  获取满足条件的邻小区的总数
	 * @param buildNcellQuerySqlWhere
	 * @param rebuildCache
	 * 是否强制更新cache
	 * @return
	 * Sep 17, 2013 4:24:46 PM
	 * gmh
	 */
	public int getTotalQueryNCellCnt(String buildNcellQuerySqlWhere,boolean rebuildCache);


	/**
	 * 删除逗号分隔的邻区id指定的邻区关系
	 * @param allAllowAreaIds允许操作的区域
	 * @param ncellIds
	 * 如 1,2,3,4
	 * Sep 17, 2013 5:38:18 PM
	 * gmh
	 */
	public int deleteNcellByIds(String cell,List<Long> allAllowAreaIds,String ncellIds);


	/**
	 * 根据小区英文名查找小区详情
	 * @param label
	 * @return
	 * Sep 18, 2013 5:49:54 PM
	 * gmh
	 */
	public Cell getCellDetail(String label);
	
	/**
	 * 根据小区名查找小区详情
	 * @param busCellId
	 * @return
	 */
	public RnoLteCell getLteCellDetail(String busCellId);
	
	/**
	 * 通过小区ID查询一条数据
	 * @param id
	 * @return
	 */
	public Cell queryGisCellById(long id);
	/**
	 * 传入一个对象更新小区数据
	 * @param cell
	 * @return
	 */
	public boolean updateCellInfo(Cell cell);

	
	/**
	 * 获取指定区域下的小区的邻区
	 * @param cell
	 * @return
	 */
	public List<RnoNcell> getNcellsByCell(String cell);
	/**
	 * 通过gps坐标得到百度三点经纬度坐标
	 * @return lnglats
	 * @author chao.xj
	 */
	public String getGisPointArray(String gpsLng,String gpsLat,int azimuth, int ScatterAngle,int lenofside);

	/**
	 * 获取所有的覆盖区域、覆盖类型、重要等级
	 * @return
	 * @author brightming
	 * 2013-10-30 下午5:18:26
	 */
	public Map<String,List<String>> getAllCoverareaCovertypeAndImportances();

	/**
	 * 分页获取区/县的giscell通过配置或区域
	 * @title 
	 * @param reSelected
	 * @param areaIds
	 * @param configIds
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2014-4-15上午11:48:08
	 * @company 怡创科技
	 * @version 1.2
	 */
	public GisCellQueryResult getGisCellUseConfigIdOrAreaByPage(boolean reSelected,String areaIds,String configIds,Page page);
	/**
	 * 
	 * @title 分页获取区/县的giscell（带频点类型）
	 * @param areaId
	 * @param page
	 * @param freqType
	 * @return
	 * @author chao.xj
	 * @date 2014-6-27下午07:20:23
	 * @company 怡创科技
	 * @version 1.2
	 */
	public GisCellQueryResult getGisCellByPage(long areaId, Page page,String freqType);

	/**
	 * 查询符合条件的爱立信ncs描述记录数量
	 * @param eri2gNcsDescQueryCond
	 * @return
	 * @author brightming
	 * 2014-8-22 下午1:34:48
	 */
	public long queryEriNcsDescCnt(Eri2GNcsDescQueryCond eri2gNcsDescQueryCond);

	/**
	 * 分页查询符合条件的爱立信ncs描述记录
	 * @param eri2gNcsDescQueryCond
	 * @param newPage
	 * @return
	 * @author brightming
	 * 2014-8-22 下午1:35:14
	 */
	public List<Map<String, Object>> queryEriNcsDescByPage(
			Eri2GNcsDescQueryCond eri2gNcsDescQueryCond, Page newPage);
	/**
	 * 分页查询符合条件的爱立信mrr描述记录
	 * @param attachParams
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2上午09:44:13
	 */
	public List<Map<String, Object>> queryEriMrrDescByPage(
			HashMap<String, Object> attachParams, Page newPage);
	/**
	 * 查询爱立信Mrr详情信息
	 * @param attachParams
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2下午05:17:42
	 */
	public List<Map<String, Object>> queryEriMrrDetailByPage(
			HashMap<String, Object> attachParams, Page newPage);
	
	/**
	 * 分页查询符合条件的爱立信fas描述记录
	 * @param attachParams
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2014-9-2上午09:44:13
	 */
	public List<Map<String, Object>> queryEriFasDescByPage(
			HashMap<String, Object> attachParams, Page newPage);
	/**
	 * 查询符合条件的华为ncs的描述信息的数量
	 * @param eri2gNcsDescQueryCond
	 * @return
	 * @author brightming
	 * 2014-8-24 下午5:15:52
	 */
	public long queryHwNcsDescCnt(Eri2GNcsDescQueryCond eri2gNcsDescQueryCond);

	/**
	 * 分页查询符合条件的华为ncs数据 
	 * @param eri2gNcsDescQueryCond
	 * @param newPage
	 * @return
	 * @author brightming
	 * 2014-8-24 下午5:16:18
	 */
	public List<Map<String, Object>> queryHwNcsDescByPage(
			Eri2GNcsDescQueryCond eri2gNcsDescQueryCond, Page newPage);
	/**
	 * 
	 * @title  查询符合条件的华为mrr的描述信息的数量
	 * @param hw2gMrrDescQueryCond
	 * @return
	 * @author chao.xj
	 * @date 2014-9-2下午2:45:09
	 * @company 怡创科技
	 * @version 1.2
	 */
	public long queryHwMrrDescCnt(Hw2GMrrDescQueryCond hw2gMrrDescQueryCond);
	/**
	 * 
	 * @title 分页查询符合条件的华为mrr数据 
	 * @param hw2gMrrDescQueryCond
	 * @param newPage
	 * @return
	 * @author chao.xj
	 * @date 2014-9-2下午2:45:25
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> queryHwMrrDescByPage(
			Hw2GMrrDescQueryCond hw2gMrrDescQueryCond, Page newPage);

	/**
	 * 通过地图网格方式分页获取小区
	 * @param areaId
	 * @param mapGrid
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-9-18上午09:15:13
	 */
	public GisCellQueryResult getGisCellByMapGrid(long areaId,
			Map<String, String> mapGrid, Page page);
	/**
	 * 通过地图网格方式分页获取小区（带频点类型）
	 * @param areaId
	 * @param mapGrid
	 * @param page
	 * @return
	 * @author peng.jm
	 * @date 2014-9-18上午09:15:13
	 */
	public GisCellQueryResult getGisCellByMapGrid(long areaId,
			Map<String, String> mapGrid, Page page, String freqType);
	
	
	public LteCellQueryResult getLteCellByMapGrid(long areaId,
			Map<String, String> mapGrid, Page page, String freqType,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints);

	/**
	 * 获取额外的giscell用于还没加载到却需要先显示的情况
	 * @param cells
	 * @param cityId
	 * @author peng.jm
	 * @date 2014-9-19下午07:00:39
	 */
	public GisCellQueryResult getRelaCellByLabels(String cells, long cityId);
	/**
	 * 通过小区参数获取cell进行小区数据预加载
	 * @param cellParam
	 * @param areaId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-28下午07:06:07
	 */
	public GisCellQueryResult getRelaCellByCellParamAndAreaId(
			Map<String, String> cellParam, long areaId);
	/**
	 * 通过cell获取其邻区数据及主小区数据进行预加载
	 * @param cell
	 * @param areaId
	 * @return
	 * @author peng.jm
	 * @date 2014-9-28下午06:49:57
	 */
	public GisCellQueryResult getNcellDetailsByCellAndAreaId(String cell,
			long areaId);

	/**
	 * 分页查询BSC信息
	 * @param bscQuery
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2014-9-30下午03:15:50
	 */
	public List<Map<String, Object>> queryBscByPage(
			Map<String, String> bscQuery, Page newPage);

	/**
	 * 通过名称删除BSC
	 * @param bscEngName
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-10-10上午10:52:19
	 */
	public Map<String, Object> deleteBscByName(String bscEngName, long cityId);

	/**
	 * 新增单个BSC
	 * @param bscEngName
	 * @param manufacturers
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-10-16下午06:12:15
	 */
	public Map<String, Object> addSingleBsc(String bscEngName, String manufacturers, long cityId);

	/**
	 * 分页获取Hbase表的数据
	 * @param tableName
	 * @param startRow
	 * @param stopRow
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2015年3月16日17:52:07
	 */
	public List<Map<String, String>> queryEriHoDataFromHbaseByPage(
			String tableName, String startRow, String stopRow, Page newPage);

	/**
	 * 分页获取Hbase表的数据
	 * @param tableName
	 * @param startRow
	 * @param stopRow
	 * @param newPage
	 * @return
	 * @author peng.jm
	 * @date 2015年3月17日18:19:51
	 */	
	public List<Map<String, String>> queryZteHoDataFromHbaseByPage(
			String tableName, String startRow, String stopRow, Page newPage);

	public LteCellQueryResult getLteCellByCellParamAndAreaId(
			Map<String, String> cellParam, long areaId,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints);


}
