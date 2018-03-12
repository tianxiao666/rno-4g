package com.iscreate.op.service.rno.parser;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.service.rno.RnoCommonService;
import com.iscreate.op.service.rno.tool.CoordinateHelper;

public abstract class BaseParser implements FileParser {
	private static Log log = LogFactory.getLog(BaseParser.class);
	protected static Gson gson = new Gson();
	
	// --spring 注入---//
	protected IFileParserManager fileParserManager;
	protected MemcachedClient memCached;
	protected RnoCommonService rnoCommonService;

	protected String Field_Seperator = "\t";// 字段之间的分隔符
	protected String Field_Content_Seperator = " ";// 如果某字段是一个列表，则列表项之间的分隔符

	public IFileParserManager getFileParserManager() {
		return fileParserManager;
	}

	public void setFileParserManager(IFileParserManager fileParserManager) {
		this.fileParserManager = fileParserManager;
	}

	public MemcachedClient getMemCached() {
		return memCached;
	}

	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}

	public void setRnoCommonService(RnoCommonService rnoCommonService) {
		this.rnoCommonService = rnoCommonService;
	}

	protected void setCachedInfo(String key, int timeInSec, Object msg) {
		try {
			memCached.set(key, timeInSec, msg);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	protected void setCachedInfo(String key,String msg){
		
		try {
			memCached.set(key, RnoConstant.TimeConstant.TokenTime, msg);
		} catch (Exception e2) {
			//e2.printStackTrace();
		}
	}

	/**
	 * 更新token消息
	 * @param token
	 * @param progress
	 * @param msg
	 * @author brightming
	 * 2014-1-20 上午11:07:33
	 */
	protected void setTokenInfo(String token,float progress,String msg){
		fileParserManager.updateTokenProgress(token, progress,msg);
	}
	
	protected void setTokenInfo(String token,float progress){
		fileParserManager.updateTokenProgress(token, progress);
	}
	
	protected void setTokenInfo(String token,String msg){
		fileParserManager.updateTokenProgress(token, msg);
	}
	
	public abstract boolean parseData(String token, File file, boolean needPersist,
			boolean update, long oldConfigId, long areaId, boolean autoload,
			Map<String, Object> attachParams);

	protected long getNextSeqValue(String seq,Connection connection){
		long descId=-1;
		String vsql = "select "+seq+".NEXTVAL as id from dual";
		Statement pstmt =null;
		try {
			pstmt=connection.createStatement();
		} catch (SQLException e3) {
			e3.printStackTrace();
			return -1;
		}
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery(vsql);
			rs.next();
			descId= rs.getLong(1);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return descId;
	}

	/**
	 * 获取百度坐标
	 * @param longitude
	 * @param latitude
	 * @param standardPoints
	 * 指定区域的经纬度基准点
	 * @return
	 * @author brightming
	 * 2013-11-26 下午2:48:07
	 */
	protected String[] getBaiduLnglat(double longitude,double latitude,Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints){
		String suf = longitude + "," + latitude;
		String[] baidulnglat = null;
		try {
			// 先从缓存获取
			baidulnglat = memCached
					.get(RnoConstant.CacheConstant.GPSPOINT_POINT_PRE + suf);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		log.info("缓存获取的gps坐标(" + suf + ")到百度坐标的映射关系为：" + baidulnglat);
		if (baidulnglat == null) {
			if (standardPoints != null && standardPoints.size() > 0) {
				baidulnglat = CoordinateHelper.getLngLatCorrectValue(longitude
						, latitude , standardPoints);
			} else {
				log.warn("区域不存在基准点，将使用百度在线接口进行校正。");
				// 缓存不存在，需要计算
				baidulnglat = CoordinateHelper.changeFromGpsToBaidu(longitude
						+ "", latitude + "");
			}
			// 保存入缓存
			try {
				memCached.set(RnoConstant.CacheConstant.GPSPOINT_POINT_PRE
						+ suf, RnoConstant.TimeConstant.GPSTOBSIDUPOINTTIME,
						baidulnglat);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return baidulnglat;
	}
}
