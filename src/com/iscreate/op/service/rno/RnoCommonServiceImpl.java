package com.iscreate.op.service.rno;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.action.rno.model.Area;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.AuthDsDataDaoImpl;
import com.iscreate.op.dao.rno.RnoCellDao;
import com.iscreate.op.dao.rno.RnoCommonDao;
import com.iscreate.op.dao.system.SysAreaDao;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.RnoDataCollectRec;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.client.api.JobClient;
import com.iscreate.op.service.rno.job.client.api.JobClientDelegate;
import com.iscreate.op.service.rno.job.common.JobState;
import com.iscreate.op.service.rno.job.server.JobAddCallback;
import com.iscreate.op.service.rno.parser.FileParserManager.ParserToken;
import com.iscreate.op.service.rno.parser.IFileParserManager;
import com.iscreate.op.service.system.SysAreaService;
import com.iscreate.plat.tools.LatLngHelper;

public class RnoCommonServiceImpl implements RnoCommonService {

	private static Log log = LogFactory.getLog(RnoCommonServiceImpl.class);
	private SysAreaDao sysAreaDao;// 系统区域
	private SysAreaService sysAreaService;//
	private IFileParserManager fileParserManager;
	public MemcachedClient memCached;
	private RnoCellDao rnoCellDao;
	// --------------------注入--------------------------------
	private RnoCommonDao rnoCommonDao;
	
	//---
//	private JobClient jobClient=com.iscreate.op.service.rno.job.client.api.JobClient.createJobClient(new IsConfiguration());

	public RnoCommonDao getRnoCommonDao() {
		return rnoCommonDao;
	}

	public void setRnoCommonDao(RnoCommonDao rnoCommonDao) {
		this.rnoCommonDao = rnoCommonDao;
	}

	public void setSysAreaDao(SysAreaDao sysAreaDao) {
		this.sysAreaDao = sysAreaDao;
	}

	public void setSysAreaService(SysAreaService sysAreaService) {
		this.sysAreaService = sysAreaService;
	}

	public void setFileParserManager(IFileParserManager fileParserManager) {
		this.fileParserManager = fileParserManager;
	}

	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}

	public void setRnoCellDao(RnoCellDao rnoCellDao) {
		this.rnoCellDao = rnoCellDao;
	}


	/**
	 * 导入数据
	 */
	public String importData(File file, String fileCode, boolean needPersist,
			boolean autoload, boolean update, long oldConfigId, long areaId,
			boolean isSystemConfig, Map<String, Object> attachParams) {
		log.debug("进入类RnoResourceManagerServiceImpl方法：importData。file=" + file
				+ ",fileCode=" + fileCode + ",needPersist=" + needPersist
				+ ",autoload=" + autoload + ",update=" + update + ",oldConfig="
				+ oldConfigId + ",areaId=" + areaId + ",isSystemConfig="
				+ isSystemConfig + ",attachParams=" + attachParams);

		if (oldConfigId == 0) {
			if (needPersist) {
				// // 需要持久化
				// if (isSystemConfig) {
				// // 是系统配置的
				// RnoCellDescriptor cellDesc = rnoCellDao
				// .getSystemCellDescriptorByAreaId(areaId);
				// if (cellDesc != null) {
				// oldConfigId = cellDesc.getCellDescriptorId();
				// } else {
				// // 保存默认配置方案
				// cellDesc = new RnoCellDescriptor();
				// cellDesc.setAreaId(areaId);
				// cellDesc.setName("系统默认配置方案");
				// cellDesc.setTempStorage(RnoConstant.StatusConstant.No);
				// oldConfigId = rnoCellDao
				// .saveSystemCellDecriptorUnderArea(areaId,
				// cellDesc);
				// }
				// } else {
				// // 不是系统默认配置
				// // TODO
				//
				// }
			} else {
				// 不需要持久化，生成一个代号即可
				oldConfigId = areaId + System.currentTimeMillis();
			}
		}
		// 清理缓存数据
		try {
			// 配置方案的缓存
			log.info("清除配置方案缓存key："
					+ RnoConstant.CacheConstant.CACHE_CELL_CONFIG_PRE
					+ oldConfigId);
			try {
				memCached
						.delete(RnoConstant.CacheConstant.CACHE_CELL_CONFIG_PRE
								+ oldConfigId);
			} catch (Exception e) {

			}
			// 区域的缓存
			log.info("清除区域下小区的缓存key："
					+ RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
					+ areaId);
			try {
				memCached
						.delete(RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
								+ areaId);
			} catch (Exception e) {

			}

			List<String> freqTypes = Arrays.asList("", "GSM900", "GSM1800");
			Integer totalPage = 0;
			for (String ft : freqTypes) {
				String totalPageKey = RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
						+ ft + areaId + "_TotalPage";
				totalPage = (Integer) memCached.get(totalPageKey);

				// 清除缓存的页数量
				try {
					memCached.delete(totalPageKey);
				} catch (Exception e) {

				}

				// 清除总数量
				String totalCountKey = RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
						+ ft + areaId + "_TotalCnt";
				try {
					memCached.delete(totalCountKey);
				} catch (Exception e) {

				}
				// 循环清除各页缓存
				if (totalPage != null) {
					for (int i = 1; i <= totalPage; i++) {
						String cacheKey = RnoConstant.CacheConstant.CACHE_GISCELL_IN_AREA_PRE
								+ ft + areaId + i;
						try {
							memCached.delete(cacheKey);
						} catch (Exception e) {

						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		String token = fileParserManager.parserData(file, fileCode,
				needPersist, autoload, update, oldConfigId, areaId,
				attachParams);
		log.debug("退出类RnoResourceManagerServiceImpl方法：importData。得到的token="
				+ token);
		return token;
	}

	/**
	 * 获取token详情
	 * 
	 * @param token
	 * @return Sep 12, 2013 5:24:06 PM gmh
	 */
	public ParserToken getToken(String token) {
		ParserToken tobj = fileParserManager.getToken(token);
		return tobj;
	}

	/**
	 * 获取上传结果
	 * 
	 * @param token
	 * @return Sep 13, 2013 11:38:34 AM gmh
	 */
	public Object popUploadResult(String token) {
		log.debug("进入getUploadResult方法。token=" + token);
		if (token == null || token.trim().isEmpty()) {
			log.warn("getUploadResult参数token为空！");
			return "";
		}
		// 销毁
		fileParserManager.destroyToken(token);

		Object ret = "";
		try {
			ret = memCached.get(token);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		log.debug("退出getUploadResult方法。token=" + token + ",返回：" + ret);
		return ret;
	}

	/**
	 * 获取用户可访问的指定级别的区域
	 * 
	 * @param accountId
	 * @param areaLevel
	 * @return
	 * @author brightming 2013-10-17 上午11:49:35
	 */
	public List<Area> getSpecialLevalAreaByAccount(String accountId,
			String areaLevel) {
		log.info("进入方法：getSpecialLevalAreaByAccount(String accountId,String areaLevel)。accountId="
				+ accountId + ",areaLevel=" + areaLevel);

		// 首先获取用户直接可见的area（也是最低级别的area）
		List<Map<String, Object>> user_area_list = sysAreaService
				.getAreaByAccount(accountId);

		// log.info("用户能直接看到的区域为：" + user_area_list);
		if (user_area_list == null || user_area_list.size() == 0) {
			return null;
		}

		HashSet<Long> areaIdSet=new HashSet<Long>();
		
		for (Map<String, Object> usea : user_area_list) {
			areaIdSet.add(Long.parseLong(usea.get("AREA_ID").toString()));
//			areaIds[i++] = Long.parseLong(usea.get("AREA_ID").toString());
		}

		long[] areaIds = new long[areaIdSet.size()];
		int i = 0;
		Iterator<Long> iter=areaIdSet.iterator();
		while(iter.hasNext()){
			areaIds[i++]=iter.next();
		}
		// 所有指定级别的列表
		List<Area> result = this.getSpecialUpperAreas(areaIds, areaLevel);
		log.info("退出getSpecialLevalAreaByAccount。 返回area：" + result);
		return result;
	}

	/**
	 * 获取指定区域集合的所有上级区域
	 * 
	 * @param area_ids
	 * @param area_level
	 * @return
	 * @author brightming 2013-10-17 上午11:39:22
	 */
	public List<Area> getSpecialUpperAreas(long[] area_ids, String area_level) {
		log.info("进入方法：getSpecialUpperAreas(long[] area_ids,String area_level)。area_ids="
				+ area_ids + ",area_level=" + area_level);
		if (area_ids.length == 0) {
			return Collections.EMPTY_LIST;
		}
		StringBuilder buf = new StringBuilder();
		buf.append("(");
		for (int i = 0; i < area_ids.length; i++) {
			buf.append(area_ids[i] + ",");
		}
		buf.deleteCharAt(buf.length() - 1);
		buf.append(")");
		String sql = "select * from (select distinct area_id,name,area_level,path,longitude,latitude,parent_id from sys_area a  start with a.area_id in "
				+ buf.toString()
				+ " connect by a.area_id=prior a.parent_id) tmp where tmp.area_level='"
				+ area_level + "'";
		List<Map<String, Object>> areaList = (List<Map<String, Object>>) sysAreaDao
				.executeSqlForObject(sql);
		log.info("退出getSpecialUpperAreas。 返回areaList：" + areaList);
		List<Area> result = new ArrayList<Area>();
		if (areaList != null) {
			Area area;
			for (Map<String, Object> one : areaList) {
				area = Area.fromMap(one);
				if (area != null) {
					result.add(area);
				}
			}
		}
		return result;
	}

	/**
	 * 根据父区域，获取用户可访问的指定级别的子区域
	 * 
	 * @param account
	 * @param parentAreaId
	 * @param subAreaLevel
	 * @return
	 * @author brightming 2013-10-17 上午11:46:56
	 */
	public List<Area> getSpecialSubAreasByAccountAndParentArea(String account,
			long parentAreaId, String subAreaLevel) {
		log.info("进入方法：getSpecialSubAreasByAccountAndParentArea。account="
				+ account + ",parentAreaId=" + parentAreaId + ",subAreaLevel="
				+ subAreaLevel);
		List<Area> allAreas = getSpecialLevalAreaByAccount(account,
				subAreaLevel);
		// 判断是否属于指定parentAreaId下的
		List<Area> subAreas = new ArrayList<Area>();
		if (allAreas != null && allAreas.size() > 0) {
			for (Area area : allAreas) {
				if (area.getParent_id() == parentAreaId) {
					subAreas.add(area);
				}
			}
		}
		log.info("退出方法getSpecialSubAreasByAccountAndParentArea。返回：" + subAreas);
		return subAreas;
	}

	/**
	 * 通过区域ID获取指定区域的地图经纬度纠偏map集合
	 * 
	 * @param areaid
	 * @param mapType
	 *            地图类型
	 * @return
	 * @author chao.xj
	 * @date 2013-10-28上午10:49:29
	 */
	public Map<AreaRectangle, List<RnoMapLnglatRelaGps>> getSpecialAreaRnoMapLnglatRelaGpsMapList(
			long areaid, String mapType) {
		List<RnoMapLnglatRelaGps> list = rnoCommonDao
				.getSpecialAreaRnoMapLnglatRelaGpsList(areaid, mapType);
		Map<AreaRectangle, List<RnoMapLnglatRelaGps>> map = new HashMap<AreaRectangle, List<RnoMapLnglatRelaGps>>();

		AreaRectangle rec;
		String topleft = null;
		String bottomright = null;
		double minLng, maxLng, minLat, maxLat;
		for (RnoMapLnglatRelaGps rnoMapLnglatRelaGps : list) {
			topleft = rnoMapLnglatRelaGps.getTopleft();
			bottomright = rnoMapLnglatRelaGps.getBottomright();

			if (topleft == null || bottomright == null) {
				log.error("topleft =" + topleft + ",bottomRight=" + bottomright);
				continue;
			}

			String[] tls = topleft.split(",");
			String[] brs = bottomright.split(",");
			if (tls.length != 2 || tls.length != 2) {
				log.error("topleft =" + topleft + ",bottomRight=" + bottomright
						+ ",用逗号分解得到的数组元素数量不等于2");
				continue;
			}

			try {
				minLng = Double.parseDouble(tls[0]);
				maxLng = Double.parseDouble(brs[0]);
				minLat = Double.parseDouble(brs[1]);
				maxLat = Double.parseDouble(tls[1]);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			rec = new AreaRectangle(minLng, maxLng, minLat, maxLat);

			if (!map.containsKey(rec)) {
				// log.info("子矩形：" + rec);
				List<RnoMapLnglatRelaGps> rmgpsList = new ArrayList<RnoMapLnglatRelaGps>();
				rmgpsList.add(rnoMapLnglatRelaGps);
				// System.out.println("不包括："+rmgpsList.size());
				map.put(rec, rmgpsList);
			} else {
				map.get(rec).add(rnoMapLnglatRelaGps);
			}
		}
		return map;
	}

	/**
	 * 获取纠偏后的GPS经纬度值
	 * 
	 * @param lng
	 * @param lat
	 * @return
	 * @author chao.xj
	 * @date 2013-10-24下午05:37:33
	 */
	public String[] getLngLatCorrectValue(double lng, double lat,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> map) {

		log.info("------------------开始处理gps点：(" + lng + "," + lat + ")");
		String[] newlnglat = new String[2];
		newlnglat[0] = lng + "";
		newlnglat[1] = lat + "";

		List<RnoMapLnglatRelaGps> allSpots = new ArrayList<RnoMapLnglatRelaGps>();
		for (AreaRectangle rec : map.keySet()) {
			if (rec.isContainsPoint(lng, lat)) {
				// System.out.println("rec="+rec+"  包含了坐标：("+lng+","+lat+")");
				if (map.get(rec) != null) {
					allSpots.addAll(map.get(rec));
				}
			}
		}
		double minDistance = Double.MAX_VALUE;
		double dis = minDistance;
		String tmp;
		String[] ss;
		double tlng = 0, tlat = 0, toffsetlng = 0, toffsetlat = 0;
		for (RnoMapLnglatRelaGps one : allSpots) {
			tmp = one.getGps();
			if (tmp == null) {
				continue;
			}
			ss = tmp.split(",");
			if (ss.length != 2) {
				continue;
			}
			try {
				tlng = Double.parseDouble(ss[0]);
				tlat = Double.parseDouble(ss[1]);
			} catch (Exception e) {
				continue;
			}
			dis = LatLngHelper.Distance(lng, lat, tlng, tlat);
			if (dis < minDistance) {
				minDistance = dis;
				tmp = one.getOffset();
				if (tmp == null) {
					continue;
				}
				ss = tmp.split(",");
				try {
					toffsetlng = Double.parseDouble(ss[0]);
					toffsetlat = Double.parseDouble(ss[1]);
				} catch (Exception e) {
					continue;
				}
				newlnglat[0] = lng + toffsetlng + "";
				newlnglat[1] = lat + toffsetlat + "";
			}
		}

		// System.out.println("最接近("+lng+","+lat+")的基准点是：("+tlng+","+tlat+")");
		return newlnglat;
	}

	/**
	 * 获取指定类型的数据
	 * 
	 * @param class1
	 * @param idField
	 * @param areaIdField
	 * @param cfids
	 * @param areaIds
	 * @author brightming 2013-11-27 下午2:50:11
	 */
	public List getObjectByIdsInArea(Class<?> class1, String idField,
			String areaIdField, List<Long> cfids, List<Long> areaIds) {
		log.info("进入方法：getObjectByIdsInArea。class1=" + class1 + ",ifField="
				+ idField + ",areaIdField=" + areaIdField + ",cfids=" + cfids
				+ ",areaIds=" + areaIds);
		if (class1 == null || idField == null || idField.trim().isEmpty()
				|| areaIdField == null || areaIdField.trim().isEmpty()
				|| cfids == null || cfids.size() == 0) {
			log.error("getObjectByIdsInArea 传入的参数不合法！不能为空！class1=" + class1
					+ ",ifField=" + idField + ",areaIdField=" + areaIdField
					+ ",cfids=" + cfids + ",areaIds=" + areaIds);
		}
		return rnoCommonDao.getObjectByIdsInArea(class1, idField, areaIdField,
				cfids, areaIds);
	}

	/**
	 * 获取城市下的所有的bsc
	 * 
	 * @param cityId
	 * @return
	 * @author brightming 2014-2-10 下午3:52:17
	 */
	public List<Map<String, Object>> getAllBscsInCity(long cityId) {
		return rnoCommonDao.getAllBscsInCity(cityId);
	}

	/**
	 * 获取bsc下的所有的小区
	 * 
	 * @param bscId
	 * @return
	 * @author brightming 2014-2-10 下午3:52:55
	 */
	public List<Map<String, Object>> getAllCellsInBsc(long bscId) {
		return rnoCommonDao.getAllCellsInBsc(bscId);
	}

	/**
	 * 模糊匹配小区
	 * 
	 * @param cityId
	 * @param cellWord
	 * @return
	 * @author brightming 2014-2-10 下午3:55:04
	 */
	public List<Map<String, Object>> findCellWithPartlyWords(long cityId,
			String cellWord) {
		return rnoCommonDao.findCellWithPartlyWords(cityId, cellWord);
	}

	/**
	 * 获取指定城市下的所有的bsc和小区
	 * 
	 * @param cityId
	 * @return key:bsc名称 value:bsc下的小区
	 * @author brightming 2014-2-11 下午12:07:31
	 */
	public List<Map<String, Object>> getAllBscCellsInCity(long cityId) {
		return rnoCommonDao.getBscCellInCity(cityId);
	}
	
	/**
	 * 数据上传以后，提交数据解析任务
	 * @param fileCode
	 * @param dataRec
	 * @return
	 * @author brightming
	 * 2014-8-21 下午3:48:21
	 */
	public RnoDataCollectRec  submitDataUploadJob(String fileCode,final RnoDataCollectRec dataRec){
		if(dataRec==null){
			return null;
		}
		JobProfile job=new JobProfile();
		job.setAccount(dataRec.getAccount());
		job.setDescription("");
		job.setJobName(RnoConstant.BusinessDataType.getByType(dataRec.getBusinessDataType()).getDesc()+"数据解析");
//		job.setJobRunningStatus(JobRunningStatus.Initiate.toString());
		job.modifyJobState(JobState.Initiate, "刚提交");
		job.setJobType(fileCode);
		job.setPriority(1);
		job.setSubmitTime(dataRec.getUploadTime());
		
		JobClient jobClient=JobClientDelegate.getJobClient();
		//---提交任务--//
		RnoDataCollectRec retRec=jobClient.submitJob(job,new JobAddCallback<RnoDataCollectRec>() {
			@Override
			public RnoDataCollectRec doWhenJobAdded(JobProfile jobProfile) {
				RnoDataCollectRec newDataRec=new RnoDataCollectRec(dataRec);
				newDataRec.setJobId(jobProfile.getJobId());
				long dataId=rnoCommonDao.saveDataUploadRec(newDataRec);
				newDataRec.setDataCollectId(dataId);
				return newDataRec;
			}
		});
		
		return retRec;
	}

	/**
	 * 保存用户的配置
	 * 
	 * @author peng.jm
	 * @date 2014-9-25下午02:51:15
	 */
	public boolean saveUserConfig(String account, long cityId) {
		return rnoCommonDao.saveUserConfig(account, cityId);
	}

	/**
	 * 获取用户配置的默认城市id
	 * @param account
	 * @return
	 * @author peng.jm
	 * @date 2014-9-25下午04:12:21
	 */
	public long getUserConfigAreaId(String account) {
		return rnoCommonDao.getUserConfigAreaId(account);
	}

	/**
	 * 获取城市下的所有的bsc
	 * @author peng.jm
	 * @date 2014-10-22下午01:56:51
	 */
	public List<Map<String, Object>> getAllBscByCityId(long cityId) {
		String areaStr = AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
		return rnoCommonDao.getAllBscByAreaStr(areaStr);
	}
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
	public long getParentIdByCityId(long cityId) {
		log.info("进入方法：getParentIdByCityId(long cityId)。cityId="
				+ cityId );
		
		String sql = "select parent_id from sys_area where area_id="+cityId;
		List<Map<String, Object>> areaList = (List<Map<String, Object>>) sysAreaDao
				.executeSqlForObject(sql);
		log.info("退出getParentIdByCityId。 返回areaList：" + areaList);
		String tmp="";
		long parentId=0;
		if (areaList != null) {
			tmp=areaList.get(0).get("PARENT_ID").toString();
			if(tmp!=null && !"".equals(tmp)){
				parentId=Long.parseLong(tmp);
			}
		}
		return parentId;
	}
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
	public List<Map<String, Object>> getAllLteCellsInCity(long cityId) {
		return rnoCommonDao.getLteCellInCity(cityId);
	}
}
