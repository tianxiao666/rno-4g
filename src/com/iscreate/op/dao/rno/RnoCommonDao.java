package com.iscreate.op.dao.rno;

import java.util.List;
import java.util.Map;

import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;

public interface RnoCommonDao {

	/**
	 * 
	 * 通过区域ID获取指定区域的地图经纬度纠偏集合
	 * @author chao.xj
	 * @date 2013-10-24下午02:10:37
	 */
	public List<RnoMapLnglatRelaGps> getSpecialAreaRnoMapLnglatRelaGpsList(long areaid,String mapType) ;
	
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
	 * 获取城市下所有的bsc
	 * @param cityId
	 * @return
	 * @author brightming
	 * 2014-2-10 下午3:57:14
	 */
	public List<Map<String,Object>>  getAllBscsInCity(long cityId);
	
	/**
	 * 获取指定bsc下的所有小区
	 * @param bsc
	 * @return
	 * @author brightming
	 * 2014-2-10 下午4:04:41
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
	 * 获取指定城市下的bsc和小区
	 * @param cityId
	 * @return
	 * key:engname为bsc名称
	 * key:label为cell名称
	 * key:name为cell中文名称
	 * @author brightming
	 * 2014-2-11 下午12:09:05
	 */
	public List<Map<String, Object>> getBscCellInCity(long cityId);

	/**
	 * 保存数据上传的记录
	 * @param dataRec
	 * @return
	 * @author brightming
	 * 2014-8-21 下午3:51:58
	 */
	public long saveDataUploadRec(RnoDataCollectRec dataRec);
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
	 * 通过areaStr获取所有BSC
	 * @param areaStr
	 * @return
	 * @author peng.jm
	 * @date 2014-10-22下午02:04:29
	 */
	public List<Map<String, Object>> getAllBscByAreaStr(String areaStr);
	/**
	 * 
	 * @title 获取指定城市下的LTE enodeb和小区
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-9-14下午2:58:48
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> getLteCellInCity(final long cityId);

}
