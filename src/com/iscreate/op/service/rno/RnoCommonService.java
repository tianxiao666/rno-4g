package com.iscreate.op.service.rno;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.service.rno.parser.FileParserManager.ParserToken;

public interface RnoCommonService {

	/**
	 * 导入文件
	 * 
	 * @param file
	 * @param fileCode
	 * @param needPersist
	 * @param autoload
	 * @param update
	 * @param oldConfigId
	 * @param areaId
	 * @param isSystemConfig
	 * @param attachParams
	 * @return Sep 9, 2013 11:59:13 AM gmh
	 */
	public String importData(File file, String fileCode,
			boolean needPersist, boolean autoload, boolean update,
			long oldConfigId,long areaId,boolean isSystemConfig,Map<String,Object> attachParams);
	
	
	/**
	 * 获取token详情
	 * @param token
	 * @return
	 * Sep 12, 2013 5:24:06 PM
	 * gmh
	 */
	public ParserToken getToken(String token);
	
	/**
	 * 获取并删除上传结果
	 * @param token
	 * @return
	 * Sep 13, 2013 11:38:34 AM
	 * gmh
	 */
	public Object popUploadResult(String token);
	
	
	/**
	 * 获取用户可访问的指定级别的区域
	 * @param accountId
	 * @param areaLevel
	 * @return
	 * @author brightming
	 * 2013-10-17 上午11:49:35
	 */
	public List<Area> getSpecialLevalAreaByAccount(String accountId,
			String areaLevel) ;
	
	/**
	 * 获取指定区域集合的所有上级区域
	 * 
	 * @param area_ids
	 * @param area_level
	 * @return
	 * @author brightming 2013-10-17 上午11:39:22
	 */
	public List<Area> getSpecialUpperAreas(long[] area_ids,
			String area_level) ;
	
	/**
	 * 根据父区域，获取用户可访问的指定级别的子区域
	 * @param account
	 * @param parentAreaId
	 * @param subAreaLevel
	 * @return
	 * @author brightming
	 * 2013-10-17 上午11:46:56
	 */
	public List<Area> getSpecialSubAreasByAccountAndParentArea(String account,long parentAreaId,String subAreaLevel);
	/**
	 * 获取纠偏后的GPS经纬度值
	 * @param lng
	 * @param lat
	 * @return
	 * @author chao.xj
	 * @date 2013-10-24下午05:37:33
	 */
	public String[] getLngLatCorrectValue(double lng,double lat,Map<AreaRectangle, List<RnoMapLnglatRelaGps>> map);
	/**
	 * 通过区域ID获取指定区域的地图经纬度纠偏map集合
	 * @param areaid
	 * @param mapType
	 * 地图类型
	 * @return
	 * @author chao.xj
	 * @date 2013-10-28上午10:49:29
	 */
	public Map<AreaRectangle, List<RnoMapLnglatRelaGps>> getSpecialAreaRnoMapLnglatRelaGpsMapList(long areaid,String mapType);


	/**
	 * 获取指定类型的数据
	 * @param class1
	 * @param idField
	 * @param areaIdField
	 * @param cfids
	 * @param areaIds
	 * @author brightming
	 * 2013-11-27 下午2:50:11
	 */
	public List getObjectByIdsInArea(Class<?> class1,
			String idField,String areaIdField, List<Long> cfids, List<Long> areaIds);
	
	
	/**
	 * 获取城市下的所有的bsc
	 * @param cityId
	 * @return
	 * @author brightming
	 * 2014-2-10 下午3:52:17
	 */
	public List<Map<String,Object>> getAllBscsInCity(long cityId);
	
	/**
	 * 获取bsc下的所有的小区
	 * @param bscId
	 * @return
	 * @author brightming
	 * 2014-2-10 下午3:52:55
	 */
	public List<Map<String,Object>> getAllCellsInBsc(long bscId);
	
	/**
	 * 模糊匹配小区
	 * @param cityId
	 * @param cellWord
	 * @return
	 * @author brightming
	 * 2014-2-10 下午3:55:04
	 */
	public List<Map<String,Object>> findCellWithPartlyWords(long cityId,String cellWord);


	/**
	 * 获取指定城市下的所有的bsc和小区
	 * @param cityId
	 * @return
	 * key:bsc名称
	 * value:bsc下的小区
	 * @author brightming
	 * 2014-2-11 下午12:07:31
	 */
	public List<Map<String, Object>> getAllBscCellsInCity(long cityId);
	
	/**
	 * 数据上传以后，提交数据解析任务
	 * @param fileCode
	 * @param dataRec
	 * @return
	 * @author brightming
	 * 2014-8-21 下午3:48:21
	 */
	public RnoDataCollectRec  submitDataUploadJob(String fileCode,RnoDataCollectRec dataRec);

	/**
	 * 保存用户的配置
	 * 
	 * @author peng.jm
	 * @date 2014-9-25下午02:51:15
	 */
	public boolean saveUserConfig(String account, long cityId);

	/**
	 * 获取用户配置的默认城市id
	 * @param account
	 * @return
	 * @author peng.jm
	 * @date 2014-9-25下午04:12:21
	 */
	public long getUserConfigAreaId(String account);


	/**
	 * 获取城市下的所有的bsc
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-10-22下午02:00:32
	 */
	public List<Map<String, Object>> getAllBscByCityId(long cityId);
	
	/**
	 * 
	 * @title 通过城市ID获取父ID即省
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-6-8上午10:41:39
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long getParentIdByCityId(long cityId);
	/**
	 * 
	 * @title 获取指定城市下的所有的lte enodeb和小区
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-9-14下午3:08:51
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> getAllLteCellsInCity(long cityId);
}
