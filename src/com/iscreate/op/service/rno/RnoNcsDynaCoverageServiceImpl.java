package com.iscreate.op.service.rno;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.dao.rno.RnoNcsDynaCoverageDao;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.service.rno.job.JobProfile;
import com.iscreate.op.service.rno.job.client.api.JobClient;
import com.iscreate.op.service.rno.job.client.api.JobClientDelegate;
import com.iscreate.op.service.rno.job.server.JobAddCallback;
import com.iscreate.op.service.rno.tool.CoordinateHelper;
import com.iscreate.plat.tools.LatLngHelper;

public class RnoNcsDynaCoverageServiceImpl implements RnoNcsDynaCoverageService {
	private static Log log = LogFactory.getLog(RnoNcsDynaCoverageServiceImpl.class);

	private static double defaultImgSize = 0.005; // 曲线点距离服务小区的长度系数0.005
	private static double evalCoeff = 0.3; // 服务小区评估方向的长度系数，经调试0.3较好
	private static float scale = 0.6f; //控制点收缩系数 ，经调试0.6较好
	private static double step = 0.01; //曲线密度(0-1)0.01
	private static double defaultImgSizeCoff = 10; // 矢量长度系数10
	private RnoNcsDynaCoverageDao rnoNcsDynaCoverageDao;
	private MemcachedClient memCached;


	public MemcachedClient getMemCached() {
		return memCached;
	}

	public void setMemCached(MemcachedClient memCached) {
		this.memCached = memCached;
	}

	public RnoNcsDynaCoverageDao getRnoNcsDynaCoverageDao() {
		return rnoNcsDynaCoverageDao;
	}

	public void setRnoNcsDynaCoverageDao(RnoNcsDynaCoverageDao rnoNcsDynaCoverageDao) {
		this.rnoNcsDynaCoverageDao = rnoNcsDynaCoverageDao;
	}

	/**
	 * 获取画小区动态覆盖图所需的数据
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	public Map<String, List<Map<String, Object>>> getDynaCoverageDataByCityAndDate(
			String cell, long cityId, String startDate, String endDate,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {

		log.debug("getDynaCoverageDataByCityAndDate, cell=" + cell + ", cityId=" + cityId
				+ ", startDate=" + startDate + ", endDate=" + endDate);

		long stime = System.currentTimeMillis();
		//先判断小区是华为小区还是爱立信小区
		String manu = rnoNcsDynaCoverageDao.checkCellIsHwOrEri(cell);
		if(manu == null) {
			log.error("不能判断该小区是哪个厂家！ cell="+cell);
			return null;
		}
		//RELSS>-12
		List<Map<String,Object>> dynaCoverData_12 = new ArrayList<Map<String,Object>>();
		//RELSS>3
		List<Map<String,Object>> dynaCoverData_3 = new ArrayList<Map<String,Object>>();

		if(("1").equals(manu)) {
			//爱立信小区
			dynaCoverData_12 = rnoNcsDynaCoverageDao
					.queryEriDataFromOracle(cityId,cell,startDate,endDate,"-12");
			dynaCoverData_3 = rnoNcsDynaCoverageDao
					.queryEriDataFromOracle(cityId,cell,startDate,endDate,"+3");
		} else if(("2").equals(manu)){
			//华为小区
			dynaCoverData_12 = rnoNcsDynaCoverageDao
					.queryHwDataFromOracle(cityId,cell,startDate,endDate,"-12");
			dynaCoverData_3 = rnoNcsDynaCoverageDao
					.queryHwDataFromOracle(cityId,cell,startDate,endDate,"+3");
		}
		if((dynaCoverData_12 == null || dynaCoverData_12.size() == 0)
				&&(dynaCoverData_3 == null || dynaCoverData_3.size() == 0)) {
			return null;
		}
		long etime = System.currentTimeMillis();
		log.debug("从数据库读取(RELSS>-12)数据量："+dynaCoverData_12.size()
				+";(RELSS>3)数据量："+dynaCoverData_3.size()+"; 共耗时："+(etime-stime));

		//获取画(RELSS>-12)图形所需曲线点坐标集合
		Map<String, List<Map<String, Object>>> res_12
				= calcDynaCoveragePointsData(dynaCoverData_12,standardPoints);
		//获取画(RELSS>3)图形所需曲线点坐标集合
		Map<String, List<Map<String, Object>>> res_3
				= calcDynaCoveragePointsData(dynaCoverData_3,standardPoints);

		Map<String, List<Map<String,Object>>> result = new HashMap<String, List<Map<String,Object>>>();
		result.put("vectorPoint_12", res_12.get("vectorPoint"));
		result.put("vectorPoint_3", res_3.get("vectorPoint"));
		result.put("curvePoints_12", res_12.get("curvePoints"));
		result.put("curvePoints_3", res_3.get("curvePoints"));
		return result;
	}


	/**
	 * 获取画小区动态覆盖图(折线)所需的数据
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年4月30日9:55:32
	 * @company 怡创科技
	 */
	public Map<String, List<Map<String, Object>>> getDynaCoverageData2ByCityAndDate(
			String cell, long cityId, String startDate, String endDate,
			double imgCoeff,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {
		log.debug("getDynaCoverageData2ByCityAndDate, cell=" + cell + ", cityId=" + cityId
				+ ", startDate=" + startDate + ", endDate=" + endDate);

		long stime = System.currentTimeMillis();
		//先判断小区是华为小区还是爱立信小区
		String manu = rnoNcsDynaCoverageDao.checkCellIsHwOrEri(cell);
		if(manu == null) {
			log.error("不能判断该小区是哪个厂家！ cell="+cell);
			return null;
		}
		//RELSS>-12
		List<Map<String,Object>> dynaCoverData_12 = new ArrayList<Map<String,Object>>();
		//RELSS>3
		List<Map<String,Object>> dynaCoverData_3 = new ArrayList<Map<String,Object>>();

		if(("1").equals(manu)) {
			//爱立信小区
			dynaCoverData_12 = rnoNcsDynaCoverageDao
					.queryEriDataFromOracle(cityId,cell,startDate,endDate,"-12");
			dynaCoverData_3 = rnoNcsDynaCoverageDao
					.queryEriDataFromOracle(cityId,cell,startDate,endDate,"+3");
		} else if(("2").equals(manu)){
			//华为小区
			dynaCoverData_12 = rnoNcsDynaCoverageDao
					.queryHwDataFromOracle(cityId,cell,startDate,endDate,"-12");
			dynaCoverData_3 = rnoNcsDynaCoverageDao
					.queryHwDataFromOracle(cityId,cell,startDate,endDate,"+3");
		}
		if((dynaCoverData_12 == null || dynaCoverData_12.size() == 0)
				&&(dynaCoverData_3 == null || dynaCoverData_3.size() == 0)) {
			return null;
		}
		long etime = System.currentTimeMillis();
		log.debug("从数据库读取(RELSS>-12)数据量："+dynaCoverData_12.size()
				+";(RELSS>3)数据量："+dynaCoverData_3.size()+"; 共耗时："+(etime-stime));

		//获取画(RELSS>-12)图形所需折线点坐标集合
		Map<String, List<Map<String, Object>>> res_12
				= calcDynaCoveragePointsData2(dynaCoverData_12,imgCoeff,standardPoints);
		//获取画(RELSS>3)图形所需折线点坐标集合
		Map<String, List<Map<String, Object>>> res_3
				= calcDynaCoveragePointsData2(dynaCoverData_3,imgCoeff,standardPoints);

		Map<String, List<Map<String,Object>>> result = new HashMap<String, List<Map<String,Object>>>();
		result.put("vectorPoint_12", res_12.get("vectorPoint"));
		result.put("vectorPoint_3", res_3.get("vectorPoint"));
		result.put("curvePoints_12", res_12.get("curvePoints"));
		result.put("curvePoints_3", res_3.get("curvePoints"));
		return result;
	}

	/**
	 * 获取画图所需折线点坐标集合
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	private Map<String, List<Map<String, Object>>> calcDynaCoveragePointsData2(
			List<Map<String, Object>> dynaCoverData,
			double imgCoeff,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {

		long stime = System.currentTimeMillis();

		Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String,Object>>>();

		double cellLng = Double.parseDouble(dynaCoverData.get(0).get("CELL_LNG").toString());
		double cellLat = Double.parseDouble(dynaCoverData.get(0).get("CELL_LAT").toString());


		//图形大小系数取最强前六邻区与本小区的距离平均值
		double imgSize = 0.0;
		//取出最强6邻区的相关数据，用于获取距离平均值
		Map<String,Map<String,Double>> ncellIdToLngLat = new HashMap<String, Map<String,Double>>();
		Map<String,Double> lnglatMap = null;
		Map<String,Double> ncellIdToVal = new HashMap<String, Double>();
		for (Map<String, Object> one : dynaCoverData) {
			double val = Double.parseDouble(one.get("VAL").toString());
			double lng = Double.parseDouble(one.get("LNG").toString());
			double lat = Double.parseDouble(one.get("LAT").toString());
			String ncellId = one.get("NCELL_ID").toString();
			ncellIdToVal.put(ncellId, val);
			lnglatMap = new HashMap<String, Double>();
			lnglatMap.put("LNG", lng);
			lnglatMap.put("LAT", lat);
			ncellIdToLngLat.put(ncellId, lnglatMap);
		}
		//排序
		ncellIdToVal = sortMapByValue(ncellIdToVal);
		int num = 0;
		double totDistance = 0.0;
		for (String ncellId : ncellIdToVal.keySet()) {
			if(num >= 6) break;//迭加前6强距离
//			if(num >= 1) break;//取最大值
			lnglatMap = ncellIdToLngLat.get(ncellId);
			double lng = lnglatMap.get("LNG");
			double lat = lnglatMap.get("LAT");
			if(lng==0 || lat==0) continue;
			totDistance += Math.sqrt((lng-cellLng)*(lng-cellLng)+(lat-cellLat)*(lat-cellLat));
			num++;
		}
		imgSize = totDistance/num;

		//如果图形大小系数未设置，取默认值
		if(imgSize == 0) {
			imgSize = defaultImgSize;
		}

		String cellBaiduPoint[] = getBaiduLnglat(cellLng, cellLat,standardPoints);
		if(cellBaiduPoint==null) {
			return null;
		}
		double cellbaiduLng = Double.parseDouble(cellBaiduPoint[0]);
		double cellbaiduLat = Double.parseDouble(cellBaiduPoint[1]);

		List<ReferencePoint> points = new ArrayList<ReferencePoint>();
		ReferencePoint point = null;
		for (Map<String, Object> one : dynaCoverData) {
			double val = Double.parseDouble(one.get("VAL").toString());
			val =val ;//乘以常量10
			double lng = Double.parseDouble(one.get("LNG").toString());
			double lat = Double.parseDouble(one.get("LAT").toString());

			if(lng==0 || lat==0) continue;

			//转化成百度坐标
			String baiduPoint[] = getBaiduLnglat(lng, lat,standardPoints);
			if(baiduPoint==null) {
				continue;
			}
			double oneBaiduLng = Double.parseDouble(baiduPoint[0]);
			double oneBaiduLat = Double.parseDouble(baiduPoint[1]);

			double lngDiff = oneBaiduLng - cellbaiduLng;
			double latDiff = oneBaiduLat - cellbaiduLat;
			double cosV = lngDiff/(Math.sqrt(lngDiff*lngDiff+latDiff*latDiff)); //正弦值
			double sinV = latDiff/(Math.sqrt(lngDiff*lngDiff+latDiff*latDiff)); //余弦值

			double oneLng = cellbaiduLng + val*imgSize*cosV;
			double oneLat = cellbaiduLat + val*imgSize*sinV;

			point = new ReferencePoint(oneLng, oneLat);
			points.add(point);
		}
		long etime = System.currentTimeMillis();
		log.debug("数据通过基准点转为百度坐标，耗时："+(etime-stime));

		//以服务小区为中心，从第一象限到第四象限，按照角度大小排序
		//归纳16个点
		List<ReferencePoint> originPoints = ascAndInduce16Points(points,cellbaiduLng,cellbaiduLat);
		//归纳8个点
		//List<ReferencePoint> originPoints = ascAndInducePoints(points,cellbaiduLng,cellbaiduLat);

		//计算矢量相加后的点坐标
		//stime = System.currentTimeMillis();
		double vectorlng = cellbaiduLng;
		double vectorlat = cellbaiduLat;

 		for (ReferencePoint p : originPoints) {
			double lngdiff = p.getBaiduLng() - cellbaiduLng;
			double latdiff = p.getBaiduLat() - cellbaiduLat;
			vectorlng += lngdiff*evalCoeff;
			vectorlat += latdiff*evalCoeff;
		}
 		List<Map<String,Object>> vectorPoints = new ArrayList<Map<String,Object>>();
 		Map<String,Object> vectorPoint = new HashMap<String, Object>();
 		vectorPoint.put("lng", vectorlng);
 		vectorPoint.put("lat", vectorlat);
 		vectorPoints.add(vectorPoint);
 		result.put("vectorPoint", vectorPoints);

		//计算折线的点集合
		List<Map<String,Object>> curvePoints = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;
		//保证imgCoeff不为0
		if(imgCoeff == 0) {
			imgCoeff = 0.5;
		}
		//加入中点
	    for(int i = 0 ;i < originPoints.size(); i++){
	        int nexti = (i + 1) % originPoints.size();
	        double preLng = originPoints.get(i).getBaiduLng();
	        double preLat = originPoints.get(i).getBaiduLat();
	        double nextLng = originPoints.get(nexti).getBaiduLng();
	        double nextLat = originPoints.get(nexti).getBaiduLat();

	        double midLng = imgCoeff * ((preLng-cellbaiduLng) + (nextLng-cellbaiduLng)) + cellbaiduLng;
	        double midLat = imgCoeff * ((preLat-cellbaiduLat) + (nextLat-cellbaiduLat)) + cellbaiduLat;
	        //中点
	        map = new HashMap<String, Object>();
	        map.put("lng", midLng);
			map.put("lat", midLat);
			curvePoints.add(map);
			//next点
	        map = new HashMap<String, Object>();
	        map.put("lng", nextLng);
			map.put("lat", nextLat);
			curvePoints.add(map);
	    }

		result.put("curvePoints", curvePoints);
		etime = System.currentTimeMillis();
		log.debug("通过数据计算折线点集合，耗时："+(etime-stime));

		return result;
	}
	/**
	 *
	 * @title 4G获取画图所需折线点坐标集合
	 * @param dynaCoverData
	 * @param imgCoeff
	 * @param imgSizeCoeff
	 * @param standardPoints
	 * @return
	 * @author chao.xj
	 * @date 2015-5-28下午5:39:49
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	private Map<String, List<Map<String, Object>>> calc4GDynaCoveragePointsData2(
			List<Map<String, Object>> dynaCoverData,
			double imgCoeff,double imgSizeCoeff,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {

		long stime = System.currentTimeMillis();

		Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String,Object>>>();

		double cellLng = Double.parseDouble(dynaCoverData.get(0).get("CELL_LNG").toString());
		double cellLat = Double.parseDouble(dynaCoverData.get(0).get("CELL_LAT").toString());


		//图形大小系数取最强前六邻区与本小区的距离平均值
		double imgSize = 0.0;
		//取出最强6邻区的相关数据，用于获取距离平均值
		Map<String,Map<String,Double>> ncellIdToLngLat = new HashMap<String, Map<String,Double>>();
		Map<String,Double> lnglatMap = null;
		Map<String,Double> ncellIdToVal = new HashMap<String, Double>();
		for (Map<String, Object> one : dynaCoverData) {
			double val = Double.parseDouble(one.get("VAL").toString());
			double lng = Double.parseDouble(one.get("LNG").toString());
			double lat = Double.parseDouble(one.get("LAT").toString());
			String ncellId = one.get("NCELL_ID").toString();
			ncellIdToVal.put(ncellId, val);
			lnglatMap = new HashMap<String, Double>();
			lnglatMap.put("LNG", lng);
			lnglatMap.put("LAT", lat);
			ncellIdToLngLat.put(ncellId, lnglatMap);
		}
		//排序
		ncellIdToVal = sortMapByValue(ncellIdToVal);
		int num = 0;
		double totDistance = 0.0;
		for (String ncellId : ncellIdToVal.keySet()) {
			if(num >= 6) break;
			lnglatMap = ncellIdToLngLat.get(ncellId);
			double lng = lnglatMap.get("LNG");
			double lat = lnglatMap.get("LAT");
			if(lng==0 || lat==0) continue;
			totDistance += Math.sqrt((lng-cellLng)*(lng-cellLng)+(lat-cellLat)*(lat-cellLat));
			num++;
		}
		imgSize = totDistance/num;

		//如果图形大小系数未设置，取默认值
		if(imgSize == 0) {
			imgSize = defaultImgSize;
		}

		String cellBaiduPoint[] = getBaiduLnglat(cellLng, cellLat,standardPoints);
		if(cellBaiduPoint==null) {
			return null;
		}
		double cellbaiduLng = Double.parseDouble(cellBaiduPoint[0]);
		double cellbaiduLat = Double.parseDouble(cellBaiduPoint[1]);

		List<ReferencePoint> points = new ArrayList<ReferencePoint>();
		ReferencePoint point = null;
		//通过经纬度存储对应的矢量长度
		Map<String, Double> lnglatToVector=new HashMap<String,Double>();
		for (Map<String, Object> one : dynaCoverData) {
			double val = Double.parseDouble(one.get("VAL").toString());
			/*if(val<0.02){
				continue;
			}*/
			val =val * imgSizeCoeff;//乘以常量10
			double lng = Double.parseDouble(one.get("LNG").toString());
			double lat = Double.parseDouble(one.get("LAT").toString());
			//增加
			double ncellLng = Double.parseDouble(one.get("CELL_LNG").toString());
			double ncellLat = Double.parseDouble(one.get("CELL_LAT").toString());
			double dis=LatLngHelper.Distance(lng, lat, ncellLng, ncellLat);
			val =val * dis; //乘以小区间的距离

			if(lng==0 || lat==0) continue;

			//转化成百度坐标
			String baiduPoint[] = getBaiduLnglat(lng, lat,standardPoints);
			if(baiduPoint==null) {
				continue;
			}
			double oneBaiduLng = Double.parseDouble(baiduPoint[0]);
			double oneBaiduLat = Double.parseDouble(baiduPoint[1]);

			double lngDiff = oneBaiduLng - cellbaiduLng;
			double latDiff = oneBaiduLat - cellbaiduLat;
			double cosV = lngDiff/(Math.sqrt(lngDiff*lngDiff+latDiff*latDiff)); //正弦值
			double sinV = latDiff/(Math.sqrt(lngDiff*lngDiff+latDiff*latDiff)); //余弦值

			double oneLng = cellbaiduLng + val*imgSize*cosV;
			double oneLat = cellbaiduLat + val*imgSize*sinV;

			point = new ReferencePoint(oneLng, oneLat);
			points.add(point);
			//缓存矢量长度
			lnglatToVector.put(oneLng+"_"+oneLat, val);
		}
		long etime = System.currentTimeMillis();
		log.debug("数据通过基准点转为百度坐标，耗时："+(etime-stime));

		//以服务小区为中心，从第一象限到第四象限，按照角度大小排序
		//归纳16个点
		List<ReferencePoint> originPoints = ascAndInduce16Points(points,cellbaiduLng,cellbaiduLat);
//		List<ReferencePoint> originPoints = ascAndInduce16Points4GData2(points,cellbaiduLng,cellbaiduLat,lnglatToVector);
		//归纳8个点
		//List<ReferencePoint> originPoints = ascAndInducePoints(points,cellbaiduLng,cellbaiduLat);

		//计算矢量相加后的点坐标
		//stime = System.currentTimeMillis();
		double vectorlng = cellbaiduLng;
		double vectorlat = cellbaiduLat;

 		for (ReferencePoint p : originPoints) {
			double lngdiff = p.getBaiduLng() - cellbaiduLng;
			double latdiff = p.getBaiduLat() - cellbaiduLat;
			vectorlng += lngdiff*evalCoeff;
			vectorlat += latdiff*evalCoeff;
		}
 		List<Map<String,Object>> vectorPoints = new ArrayList<Map<String,Object>>();
 		Map<String,Object> vectorPoint = new HashMap<String, Object>();
 		vectorPoint.put("lng", vectorlng);
 		vectorPoint.put("lat", vectorlat);
 		vectorPoints.add(vectorPoint);
 		result.put("vectorPoint", vectorPoints);

		//计算折线的点集合
		List<Map<String,Object>> curvePoints = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;
		//保证imgCoeff不为0
		if(imgCoeff == 0) {
			imgCoeff = 0.5;
		}
		//加入中点
	    for(int i = 0 ;i < originPoints.size(); i++){
	        int nexti = (i + 1) % originPoints.size();
	        double preLng = originPoints.get(i).getBaiduLng();
	        double preLat = originPoints.get(i).getBaiduLat();
	        double nextLng = originPoints.get(nexti).getBaiduLng();
	        double nextLat = originPoints.get(nexti).getBaiduLat();

	        double midLng = imgCoeff * ((preLng-cellbaiduLng) + (nextLng-cellbaiduLng)) + cellbaiduLng;
	        double midLat = imgCoeff * ((preLat-cellbaiduLat) + (nextLat-cellbaiduLat)) + cellbaiduLat;
	        //中点
	        map = new HashMap<String, Object>();
	        map.put("lng", midLng);
			map.put("lat", midLat);
			curvePoints.add(map);
			//next点
	        map = new HashMap<String, Object>();
	        map.put("lng", nextLng);
			map.put("lat", nextLat);
			curvePoints.add(map);
	    }

		result.put("curvePoints", curvePoints);
		etime = System.currentTimeMillis();
		log.debug("通过数据计算折线点集合，耗时："+(etime-stime));

		return result;
	}
	/**
	 * 获取画图所需曲线点坐标集合
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	private Map<String, List<Map<String, Object>>> calcDynaCoveragePointsData(
			List<Map<String, Object>> dynaCoverData,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {

		long stime = System.currentTimeMillis();

		Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String,Object>>>();

		double cellLng = Double.parseDouble(dynaCoverData.get(0).get("CELL_LNG").toString());
		double cellLat = Double.parseDouble(dynaCoverData.get(0).get("CELL_LAT").toString());

		//图形大小系数取最强前六邻区与本小区的距离平均值
		double imgSize = 0.0;
		//取出最强6邻区的相关数据，用于获取距离平均值
		Map<String,Map<String,Double>> ncellIdToLngLat = new HashMap<String, Map<String,Double>>();
		Map<String,Double> lnglatMap = null;
		Map<String,Double> ncellIdToVal = new HashMap<String, Double>();
		for (Map<String, Object> one : dynaCoverData) {
			double val = Double.parseDouble(one.get("VAL").toString());
			double lng = Double.parseDouble(one.get("LNG").toString());
			double lat = Double.parseDouble(one.get("LAT").toString());
			String ncellId = one.get("NCELL_ID").toString();
			ncellIdToVal.put(ncellId, val);
			lnglatMap = new HashMap<String, Double>();
			lnglatMap.put("LNG", lng);
			lnglatMap.put("LAT", lat);
			ncellIdToLngLat.put(ncellId, lnglatMap);
		}
		//排序
		ncellIdToVal = sortMapByValue(ncellIdToVal);
		int num = 0;
		double totDistance = 0.0;
		for (String ncellId : ncellIdToVal.keySet()) {
			if(num >= 6) break;
			lnglatMap = ncellIdToLngLat.get(ncellId);
			double lng = lnglatMap.get("LNG");
			double lat = lnglatMap.get("LAT");
			if(lng==0 || lat==0) continue;
			//System.out.println(lng+"  "+lat);
			totDistance += Math.sqrt((lng-cellLng)*(lng-cellLng)+(lat-cellLat)*(lat-cellLat));
			num++;
		}
		imgSize = totDistance/num;

		//如果图形大小系数未设置，取默认值
		if(imgSize == 0) {
			imgSize = defaultImgSize;
		}


		String cellBaiduPoint[] = getBaiduLnglat(cellLng, cellLat,standardPoints);
		if(cellBaiduPoint==null) {
			return null;
		}
		double cellbaiduLng = Double.parseDouble(cellBaiduPoint[0]);
		double cellbaiduLat = Double.parseDouble(cellBaiduPoint[1]);

		List<ReferencePoint> points = new ArrayList<ReferencePoint>();
		ReferencePoint point = null;
		for (Map<String, Object> one : dynaCoverData) {
			double val = Double.parseDouble(one.get("VAL").toString())*8;
//			val =val;//乘以常量0.005
			double lng = Double.parseDouble(one.get("LNG").toString());
			double lat = Double.parseDouble(one.get("LAT").toString());

			if(lng==0 || lat==0) continue;

			//转化成百度坐标
			String baiduPoint[] = getBaiduLnglat(lng, lat,standardPoints);
			if(baiduPoint==null) {
				continue;
			}
			double oneBaiduLng = Double.parseDouble(baiduPoint[0]);
			double oneBaiduLat = Double.parseDouble(baiduPoint[1]);

			double lngDiff = oneBaiduLng - cellbaiduLng;
			double latDiff = oneBaiduLat - cellbaiduLat;
			//System.out.println(lngDiff+"    "+latDiff);
			double cosV = lngDiff/(Math.sqrt(lngDiff*lngDiff+latDiff*latDiff)); //正弦值
			double sinV = latDiff/(Math.sqrt(lngDiff*lngDiff+latDiff*latDiff)); //余弦值

			double oneLng = cellbaiduLng + val*imgSize*cosV;
			double oneLat = cellbaiduLat + val*imgSize*sinV;
			//System.out.println(oneLng+"    "+oneLat);
			point = new ReferencePoint(oneLng, oneLat);
			points.add(point);
		}
		long etime = System.currentTimeMillis();
		log.debug("数据通过基准点转为百度坐标，耗时："+(etime-stime));

		//以服务小区为中心，从第一象限到第四象限，按照角度大小排序
		stime = System.currentTimeMillis();
		List<ReferencePoint> originPoints = ascAndInducePoints(points,cellbaiduLng,cellbaiduLat);
		etime = System.currentTimeMillis();
		log.debug("数据坐标以服务小区为中心按照角度大小排序，耗时："+(etime-stime));

		//计算矢量相加后的点坐标
		stime = System.currentTimeMillis();
		double vectorlng = cellbaiduLng;
		double vectorlat = cellbaiduLat;
 		for (ReferencePoint p : originPoints) {
			double lngdiff = p.getBaiduLng() - cellbaiduLng;
			double latdiff = p.getBaiduLat() - cellbaiduLat;
	 		//System.out.println(lngdiff+	"      "+latdiff);
			vectorlng += lngdiff*evalCoeff;
			vectorlat += latdiff*evalCoeff;
		}

 		//System.out.println(vectorlng + "   " +vectorlat);
 		List<Map<String,Object>> vectorPoints = new ArrayList<Map<String,Object>>();
 		Map<String,Object> vectorPoint = new HashMap<String, Object>();
 		vectorPoint.put("lng", vectorlng);
 		vectorPoint.put("lat", vectorlat);
 		vectorPoints.add(vectorPoint);
 		result.put("vectorPoint", vectorPoints);

		//计算曲线的点集合
		List<Map<String,Object>> curvePoints = calculatePoints(originPoints);
		result.put("curvePoints", curvePoints);
		etime = System.currentTimeMillis();
		log.debug("通过数据计算曲线点集合，耗时："+(etime-stime));

		return result;
	}
	/**
	 *
	 * @title 4G获取画图所需曲线点坐标集合
	 * @param dynaCoverData
	 * @param standardPoints
	 * @return
	 * @author chao.xj
	 * @date 2015-5-28下午5:40:54
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	private Map<String, List<Map<String, Object>>> calc4GDynaCoveragePointsData(
			List<Map<String, Object>> dynaCoverData,double imgSizeCoeff,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {

		long stime = System.currentTimeMillis();

		Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String,Object>>>();

		double cellLng = Double.parseDouble(dynaCoverData.get(0).get("CELL_LNG").toString());
		double cellLat = Double.parseDouble(dynaCoverData.get(0).get("CELL_LAT").toString());

		//图形大小系数取最强前六邻区与本小区的距离平均值
		double imgSize = 0.0;
		//取出最强6邻区的相关数据，用于获取距离平均值
		Map<String,Map<String,Double>> ncellIdToLngLat = new HashMap<String, Map<String,Double>>();
		Map<String,Double> lnglatMap = null;
		Map<String,Double> ncellIdToVal = new HashMap<String, Double>();
		for (Map<String, Object> one : dynaCoverData) {
			double val = Double.parseDouble(one.get("VAL").toString());
			double lng = Double.parseDouble(one.get("LNG").toString());
			double lat = Double.parseDouble(one.get("LAT").toString());
			String ncellId = one.get("NCELL_ID").toString();
			ncellIdToVal.put(ncellId, val);
			lnglatMap = new HashMap<String, Double>();
			lnglatMap.put("LNG", lng);
			lnglatMap.put("LAT", lat);
			ncellIdToLngLat.put(ncellId, lnglatMap);
		}
		//排序
		ncellIdToVal = sortMapByValue(ncellIdToVal);
		int num = 0;
		double totDistance = 0.0;
		for (String ncellId : ncellIdToVal.keySet()) {
			if(num >= 6) break;
			lnglatMap = ncellIdToLngLat.get(ncellId);
			double lng = lnglatMap.get("LNG");
			double lat = lnglatMap.get("LAT");
			if(lng==0 || lat==0) continue;
			//System.out.println(lng+"  "+lat);
			totDistance += Math.sqrt((lng-cellLng)*(lng-cellLng)+(lat-cellLat)*(lat-cellLat));
			num++;
		}
		imgSize = totDistance/num;

		//如果图形大小系数未设置，取默认值
		if(imgSize == 0) {
			imgSize = defaultImgSize;
		}


		String cellBaiduPoint[] = getBaiduLnglat(cellLng, cellLat,standardPoints);
		if(cellBaiduPoint==null) {
			return null;
		}
		double cellbaiduLng = Double.parseDouble(cellBaiduPoint[0]);
		double cellbaiduLat = Double.parseDouble(cellBaiduPoint[1]);

		List<ReferencePoint> points = new ArrayList<ReferencePoint>();
		ReferencePoint point = null;
		for (Map<String, Object> one : dynaCoverData) {
			double val = Double.parseDouble(one.get("VAL").toString())*8;
			val =val * imgSizeCoeff;//乘以常量0.005
			double lng = Double.parseDouble(one.get("LNG").toString());
			double lat = Double.parseDouble(one.get("LAT").toString());
			//增加
			/*double ncellLng = Double.parseDouble(one.get("CELL_LNG").toString());
			double ncellLat = Double.parseDouble(one.get("CELL_LAT").toString());
			double dis=LatLngHelper.Distance(lng, lat, ncellLng, ncellLat);
			val =val * dis;*/ //乘以小区间的距离

			if(lng==0 || lat==0) continue;

			//转化成百度坐标
			String baiduPoint[] = getBaiduLnglat(lng, lat,standardPoints);
			if(baiduPoint==null) {
				continue;
			}
			double oneBaiduLng = Double.parseDouble(baiduPoint[0]);
			double oneBaiduLat = Double.parseDouble(baiduPoint[1]);

			double lngDiff = oneBaiduLng - cellbaiduLng;
			double latDiff = oneBaiduLat - cellbaiduLat;
			//System.out.println(lngDiff+"    "+latDiff);
			double cosV = lngDiff/(Math.sqrt(lngDiff*lngDiff+latDiff*latDiff)); //正弦值
			double sinV = latDiff/(Math.sqrt(lngDiff*lngDiff+latDiff*latDiff)); //余弦值

			double oneLng = cellbaiduLng + val*imgSize*cosV;
			double oneLat = cellbaiduLat + val*imgSize*sinV;
			//System.out.println(oneLng+"    "+oneLat);
			point = new ReferencePoint(oneLng, oneLat);
			points.add(point);
		}
		long etime = System.currentTimeMillis();
		log.debug("数据通过基准点转为百度坐标，耗时："+(etime-stime));

		//以服务小区为中心，从第一象限到第四象限，按照角度大小排序
		stime = System.currentTimeMillis();
		List<ReferencePoint> originPoints = ascAndInducePoints(points,cellbaiduLng,cellbaiduLat);
		etime = System.currentTimeMillis();
		log.debug("数据坐标以服务小区为中心按照角度大小排序，耗时："+(etime-stime));

		//计算矢量相加后的点坐标
		stime = System.currentTimeMillis();
		double vectorlng = cellbaiduLng;
		double vectorlat = cellbaiduLat;
 		for (ReferencePoint p : originPoints) {
			double lngdiff = p.getBaiduLng() - cellbaiduLng;
			double latdiff = p.getBaiduLat() - cellbaiduLat;
	 		//System.out.println(lngdiff+	"      "+latdiff);
			vectorlng += lngdiff*evalCoeff;
			vectorlat += latdiff*evalCoeff;
		}

 		//System.out.println(vectorlng + "   " +vectorlat);
 		List<Map<String,Object>> vectorPoints = new ArrayList<Map<String,Object>>();
 		Map<String,Object> vectorPoint = new HashMap<String, Object>();
 		vectorPoint.put("lng", vectorlng);
 		vectorPoint.put("lat", vectorlat);
 		vectorPoints.add(vectorPoint);
 		result.put("vectorPoint", vectorPoints);

		//计算曲线的点集合
		List<Map<String,Object>> curvePoints = calculatePoints(originPoints);
		result.put("curvePoints", curvePoints);
		etime = System.currentTimeMillis();
		log.debug("通过数据计算曲线点集合，耗时："+(etime-stime));

		return result;
	}
	/**
	 * 以服务小区为中心，从第一象限到第四象限，按照角度大小排序并归纳成8个点
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	private List<ReferencePoint> ascAndInducePoints(List<ReferencePoint> points,
			double cellbaiduLng, double cellbaiduLat) {

		List<ReferencePoint> one = new ArrayList<ReferencePoint>();
		List<ReferencePoint> two = new ArrayList<ReferencePoint>();
		List<ReferencePoint> three = new ArrayList<ReferencePoint>();
		List<ReferencePoint> four = new ArrayList<ReferencePoint>();

		//划分落在每个象限的点

		for (ReferencePoint p : points) {
			//第一象限
			if(p.getBaiduLng()>=cellbaiduLng && p.getBaiduLat()>cellbaiduLat)
				one.add(p);
			//第二象限
			if(p.getBaiduLng()<cellbaiduLng && p.getBaiduLat()>=cellbaiduLat)
				two.add(p);
			//第三象限
			if(p.getBaiduLng()<=cellbaiduLng && p.getBaiduLat()<cellbaiduLat)
				three.add(p);
			//第四象限
			if(p.getBaiduLng()>cellbaiduLng && p.getBaiduLat()<=cellbaiduLat)
				four.add(p);
		}

		ReferencePoint temp = null;
		double xxi,yyi,xxj,yyj,sini,sinj;
		//将各自象限内的点按照角度划分顺序
		//第一象限
		for (int i = 0; i < one.size(); i++) {
			for (int j = i+1; j < one.size(); j++) {
				xxi = one.get(i).getBaiduLng() - cellbaiduLng;
				yyi = one.get(i).getBaiduLat() - cellbaiduLat;
				xxj = one.get(j).getBaiduLng() - cellbaiduLng;
				yyj = one.get(j).getBaiduLat() - cellbaiduLat;
				sini = yyi/(Math.sqrt(xxi*xxi+yyi*yyi));
				sinj = yyj/(Math.sqrt(xxj*xxj+yyj*yyj));
				if(sini > sinj) {
					temp = one.get(i);
					one.set(i, one.get(j));
					one.set(j, temp);
				}
			}
		}
		//第二象限
		for (int i = 0; i < two.size(); i++) {
			for (int j = i+1; j < two.size(); j++) {
				xxi = two.get(i).getBaiduLng() - cellbaiduLng;
				yyi = two.get(i).getBaiduLat() - cellbaiduLat;
				xxj = two.get(j).getBaiduLng() - cellbaiduLng;
				yyj = two.get(j).getBaiduLat() - cellbaiduLat;
				sini = yyi/(Math.sqrt(xxi*xxi+yyi*yyi));
				sinj = yyj/(Math.sqrt(xxj*xxj+yyj*yyj));
				if(sini < sinj) {
					temp = two.get(i);
					two.set(i, two.get(j));
					two.set(j, temp);
				}
			}
		}
		//第三象限
		for (int i = 0; i < three.size(); i++) {
			for (int j = i+1; j < three.size(); j++) {
				xxi = three.get(i).getBaiduLng() - cellbaiduLng;
				yyi = three.get(i).getBaiduLat() - cellbaiduLat;
				xxj = three.get(j).getBaiduLng() - cellbaiduLng;
				yyj = three.get(j).getBaiduLat() - cellbaiduLat;
				sini = yyi/(Math.sqrt(xxi*xxi+yyi*yyi));
				sinj = yyj/(Math.sqrt(xxj*xxj+yyj*yyj));
				if(sini < sinj) {
					temp = three.get(i);
					three.set(i, three.get(j));
					three.set(j, temp);
				}
			}
		}
		//第四象限
		for (int i = 0; i < four.size(); i++) {
			for (int j = i+1; j < four.size(); j++) {
				xxi = four.get(i).getBaiduLng() - cellbaiduLng;
				yyi = four.get(i).getBaiduLat() - cellbaiduLat;
				xxj = four.get(j).getBaiduLng() - cellbaiduLng;
				yyj = four.get(j).getBaiduLat() - cellbaiduLat;
				sini = yyi/(Math.sqrt(xxi*xxi+yyi*yyi));
				sinj = yyj/(Math.sqrt(xxj*xxj+yyj*yyj));
				if(sini > sinj) {
					temp = four.get(i);
					four.set(i, four.get(j));
					four.set(j, temp);
				}
			}
		}

		List<ReferencePoint> res = new ArrayList<ReferencePoint>();
		//int oneNum=0,twoNum=0,threeNum=0,fourNum=0;

		//对每一象限分成两份，取每份的数据矢量相加作为一个参考点，即每个象限取两个参考点
		ReferencePoint tempPoint = null;
		double tempLng = cellbaiduLng;
		double tempLat = cellbaiduLat;
		for (int i = 0; i < one.size()/2; i++) {
			tempLng += (one.get(i).getBaiduLng() - cellbaiduLng);
			tempLat += (one.get(i).getBaiduLat() - cellbaiduLat);
		}
		//System.out.println(tempLng+"---"+tempLat);
		tempPoint = new ReferencePoint(tempLng,tempLat);
		res.add(tempPoint);
		tempLng = cellbaiduLng;
		tempLat = cellbaiduLat;
		for (int i = one.size()/2; i < one.size(); i++) {
			tempLng += (one.get(i).getBaiduLng() - cellbaiduLng);
			tempLat += (one.get(i).getBaiduLat() - cellbaiduLat);
		}
		//System.out.println(tempLng+"---"+tempLat);
		tempPoint = new ReferencePoint(tempLng,tempLat);
		res.add(tempPoint);
		//第二象限
		tempLng = cellbaiduLng;
		tempLat = cellbaiduLat;
		for (int i = 0; i < two.size()/2; i++) {
			tempLng += (two.get(i).getBaiduLng() - cellbaiduLng);
			tempLat += (two.get(i).getBaiduLat() - cellbaiduLat);
		}
		//System.out.println(tempLng+"---"+tempLat);
		tempPoint = new ReferencePoint(tempLng,tempLat);
		res.add(tempPoint);
		tempLng = cellbaiduLng;
		tempLat = cellbaiduLat;
		for (int i = two.size()/2; i < two.size(); i++) {
			tempLng += (two.get(i).getBaiduLng() - cellbaiduLng);
			tempLat += (two.get(i).getBaiduLat() - cellbaiduLat);
		}
		//System.out.println(tempLng+"---"+tempLat);
		tempPoint = new ReferencePoint(tempLng,tempLat);
		res.add(tempPoint);
		//第三象限
		tempLng = cellbaiduLng;
		tempLat = cellbaiduLat;
		for (int i = 0; i < three.size()/2; i++) {
			tempLng += (three.get(i).getBaiduLng() - cellbaiduLng);
			tempLat += (three.get(i).getBaiduLat() - cellbaiduLat);
		}
		//System.out.println(tempLng+"---"+tempLat);
		tempPoint = new ReferencePoint(tempLng,tempLat);
		res.add(tempPoint);
		tempLng = cellbaiduLng;
		tempLat = cellbaiduLat;
		for (int i = three.size()/2; i < three.size(); i++) {
			tempLng += (three.get(i).getBaiduLng() - cellbaiduLng);
			tempLat += (three.get(i).getBaiduLat() - cellbaiduLat);
		}
		//System.out.println(tempLng+"---"+tempLat);
		tempPoint = new ReferencePoint(tempLng,tempLat);
		res.add(tempPoint);
		//第四象限
		tempLng = cellbaiduLng;
		tempLat = cellbaiduLat;
		for (int i = 0; i < four.size()/2; i++) {
			tempLng += (four.get(i).getBaiduLng() - cellbaiduLng);
			tempLat += (four.get(i).getBaiduLat() - cellbaiduLat);
		}
		//System.out.println(tempLng+"---"+tempLat);
		tempPoint = new ReferencePoint(tempLng,tempLat);
		res.add(tempPoint);
		tempLng = cellbaiduLng;
		tempLat = cellbaiduLat;
		for (int i = four.size()/2; i < four.size(); i++) {
			tempLng += (four.get(i).getBaiduLng() - cellbaiduLng);
			tempLat += (four.get(i).getBaiduLat() - cellbaiduLat);
		}
		//System.out.println(tempLng+"---"+tempLat);
		tempPoint = new ReferencePoint(tempLng,tempLat);
		res.add(tempPoint);
//		System.out.println("第一象限个数："+oneNum+"，第二象限个数："
//				+twoNum+"，第三象限个数："+threeNum+"，第四象限个数："+fourNum);

		return res;
	}

	/**
	 * 以服务小区为中心，从第一象限到第四象限，按照角度大小排序并归纳叠加成16个点
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	private List<ReferencePoint> ascAndInduce16Points(List<ReferencePoint> points,
			double cellbaiduLng, double cellbaiduLat) {

		List<ReferencePoint> one = new ArrayList<ReferencePoint>();
		List<ReferencePoint> two = new ArrayList<ReferencePoint>();
		List<ReferencePoint> three = new ArrayList<ReferencePoint>();
		List<ReferencePoint> four = new ArrayList<ReferencePoint>();

		//划分落在每个象限的点

		for (ReferencePoint p : points) {
			//第一象限
			if(p.getBaiduLng()>=cellbaiduLng && p.getBaiduLat()>cellbaiduLat)
				one.add(p);
			//第二象限
			if(p.getBaiduLng()<cellbaiduLng && p.getBaiduLat()>=cellbaiduLat)
				two.add(p);
			//第三象限
			if(p.getBaiduLng()<=cellbaiduLng && p.getBaiduLat()<cellbaiduLat)
				three.add(p);
			//第四象限
			if(p.getBaiduLng()>cellbaiduLng && p.getBaiduLat()<=cellbaiduLat)
				four.add(p);
		}

		ReferencePoint temp = null;
		double xxi,yyi,xxj,yyj,sini,sinj;
		//将各自象限内的点按照角度划分顺序
		//第一象限
		for (int i = 0; i < one.size(); i++) {
			for (int j = i+1; j < one.size(); j++) {
				xxi = one.get(i).getBaiduLng() - cellbaiduLng;
				yyi = one.get(i).getBaiduLat() - cellbaiduLat;
				xxj = one.get(j).getBaiduLng() - cellbaiduLng;
				yyj = one.get(j).getBaiduLat() - cellbaiduLat;
				sini = yyi/(Math.sqrt(xxi*xxi+yyi*yyi));
				sinj = yyj/(Math.sqrt(xxj*xxj+yyj*yyj));
				if(sini > sinj) {
					temp = one.get(i);
					one.set(i, one.get(j));
					one.set(j, temp);
				}
			}
		}
		//第二象限
		for (int i = 0; i < two.size(); i++) {
			for (int j = i+1; j < two.size(); j++) {
				xxi = two.get(i).getBaiduLng() - cellbaiduLng;
				yyi = two.get(i).getBaiduLat() - cellbaiduLat;
				xxj = two.get(j).getBaiduLng() - cellbaiduLng;
				yyj = two.get(j).getBaiduLat() - cellbaiduLat;
				sini = yyi/(Math.sqrt(xxi*xxi+yyi*yyi));
				sinj = yyj/(Math.sqrt(xxj*xxj+yyj*yyj));
				if(sini < sinj) {
					temp = two.get(i);
					two.set(i, two.get(j));
					two.set(j, temp);
				}
			}
		}
		//第三象限
		for (int i = 0; i < three.size(); i++) {
			for (int j = i+1; j < three.size(); j++) {
				xxi = three.get(i).getBaiduLng() - cellbaiduLng;
				yyi = three.get(i).getBaiduLat() - cellbaiduLat;
				xxj = three.get(j).getBaiduLng() - cellbaiduLng;
				yyj = three.get(j).getBaiduLat() - cellbaiduLat;
				sini = yyi/(Math.sqrt(xxi*xxi+yyi*yyi));
				sinj = yyj/(Math.sqrt(xxj*xxj+yyj*yyj));
				if(sini < sinj) {
					temp = three.get(i);
					three.set(i, three.get(j));
					three.set(j, temp);
				}
			}
		}
		//第四象限
		for (int i = 0; i < four.size(); i++) {
			for (int j = i+1; j < four.size(); j++) {
				xxi = four.get(i).getBaiduLng() - cellbaiduLng;
				yyi = four.get(i).getBaiduLat() - cellbaiduLat;
				xxj = four.get(j).getBaiduLng() - cellbaiduLng;
				yyj = four.get(j).getBaiduLat() - cellbaiduLat;
				sini = yyi/(Math.sqrt(xxi*xxi+yyi*yyi));
				sinj = yyj/(Math.sqrt(xxj*xxj+yyj*yyj));
				if(sini > sinj) {
					temp = four.get(i);
					four.set(i, four.get(j));
					four.set(j, temp);
				}
			}
		}

		List<ReferencePoint> res = new ArrayList<ReferencePoint>();
		//int oneNum=0,twoNum=0,threeNum=0,fourNum=0;
		//对每一象限分成两份，取每份的数据矢量相加作为一个参考点，即每个象限取两个参考点
		ReferencePoint tempPoint = null;
		double tempLng = cellbaiduLng;
		double tempLat = cellbaiduLat;
		int num = 0;
		if(one.size() < 4) {
			for (int i = 0; i < one.size(); i++) {
				res.add(one.get(i));
			}
		} else {
			num = one.size()/4;
			for (int i = 0; i < num; i++) {
				tempLng += (one.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (one.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*1; i < num*2; i++) {
				tempLng += (one.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (one.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*2; i < num*3; i++) {
				tempLng += (one.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (one.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*3; i < num*4; i++) {
				tempLng += (one.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (one.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*4; i < one.size(); i++) {
				tempLng += (one.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (one.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
		}

		//第二象限
		if(two.size() < 4) {
			for (int i = 0; i < two.size(); i++) {
				res.add(two.get(i));
			}
		} else {
			num = two.size()/4;
			for (int i = 0; i < num; i++) {
				tempLng += (two.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (two.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*1; i < num*2; i++) {
				tempLng += (two.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (two.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*2; i < num*3; i++) {
				tempLng += (two.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (two.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*3; i < num*4; i++) {
				tempLng += (two.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (two.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*4; i < two.size(); i++) {
				tempLng += (two.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (two.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
		}
		//第三象限
		if(three.size() < 4) {
			for (int i = 0; i < three.size(); i++) {
				res.add(three.get(i));
			}
		} else {
			num = three.size()/4;
			for (int i = 0; i < num; i++) {
				tempLng += (three.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (three.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*1; i < num*2; i++) {
				tempLng += (three.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (three.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*2; i < num*3; i++) {
				tempLng += (three.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (three.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*3; i < num*4; i++) {
				tempLng += (three.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (three.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*4; i < three.size(); i++) {
				tempLng += (three.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (three.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
		}
		//第四象限
		if(four.size() < 4) {
			for (int i = 0; i < four.size(); i++) {
				res.add(four.get(i));
			}
		} else {
			num = four.size()/4;
			for (int i = 0; i < num; i++) {
				tempLng += (four.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (four.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*1; i < num*2; i++) {
				tempLng += (four.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (four.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*2; i < num*3; i++) {
				tempLng += (four.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (four.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*3; i < num*4; i++) {
				tempLng += (four.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (four.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*4; i < four.size(); i++) {
				tempLng += (four.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (four.get(i).getBaiduLat() - cellbaiduLat);
			}
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
		}

		return res;
	}

	/**
	 * 计算贝塞尔曲线的点集合
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	private List<Map<String,Object>> calculatePoints(
						List<ReferencePoint> originPoints) {


	    int originCount = originPoints.size();
	    ReferencePoint midpoints[] = new ReferencePoint[originCount];
	    //生成中点
	    for(int i = 0 ;i < originCount ; i++){
	        int nexti = (i + 1) % originCount;
	        double x = (originPoints.get(i).getBaiduLng() + originPoints.get(nexti).getBaiduLng())/2;
	        double y = (originPoints.get(i).getBaiduLat() + originPoints.get(nexti).getBaiduLat())/2;
	        midpoints[i] = new ReferencePoint(x,y);
	    }

	    //平移中点
	    ReferencePoint extrapoints[] = new ReferencePoint[2 * originCount];
	    for(int i = 0 ;i < originCount ; i++){
	    	 //int nexti = (i + 1) % originCount;
	         int backi = (i + originCount - 1) % originCount;
	         ReferencePoint midinmid = new ReferencePoint();
	         midinmid.setBaiduLng((midpoints[i].getBaiduLng() + midpoints[backi].getBaiduLng())/2);
	         midinmid.setBaiduLat((midpoints[i].getBaiduLat() + midpoints[backi].getBaiduLat())/2);
	         double offsetx = originPoints.get(i).getBaiduLng() - midinmid.getBaiduLng();
	         double offsety = originPoints.get(i).getBaiduLat() - midinmid.getBaiduLat();
	         int extraindex = 2 * i;
	         double x = midpoints[backi].getBaiduLng() + offsetx;
	         double y = midpoints[backi].getBaiduLat() + offsety;
	         extrapoints[extraindex] = new ReferencePoint(x,y);
	         //朝 originPoint[i]方向收缩
	         double addx = (extrapoints[extraindex].getBaiduLng() - originPoints.get(i).getBaiduLng()) * scale;
	         double addy = (extrapoints[extraindex].getBaiduLat() - originPoints.get(i).getBaiduLat()) * scale;
	         extrapoints[extraindex].setBaiduLng(originPoints.get(i).getBaiduLng() + addx);
	         extrapoints[extraindex].setBaiduLat(originPoints.get(i).getBaiduLat() + addy);

	         int extranexti = (extraindex + 1)%(2 * originCount);
	         x = midpoints[i].getBaiduLng() + offsetx;
	         y = midpoints[i].getBaiduLat() + offsety;
	         extrapoints[extranexti] = new ReferencePoint(x,y);
	         //朝 originPoint[i]方向收缩
	         addx = (extrapoints[extranexti].getBaiduLng() - originPoints.get(i).getBaiduLng()) * scale;
	         addy = (extrapoints[extranexti].getBaiduLat() - originPoints.get(i).getBaiduLat()) * scale;
	         extrapoints[extranexti].setBaiduLng(originPoints.get(i).getBaiduLng() + addx);
	         extrapoints[extranexti].setBaiduLat(originPoints.get(i).getBaiduLat() + addy);
	    }

	    List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
	    Map<String,Object> tempP = null;
	    ReferencePoint controlPoint[] = new ReferencePoint[4];
	    //生成4控制点，产生贝塞尔曲线
	    for(int i = 0 ;i < originCount ; i++){
	           controlPoint[0] = originPoints.get(i);
	           int extraindex = 2 * i;
	           controlPoint[1] = extrapoints[extraindex + 1];
	           int extranexti = (extraindex + 2) % (2 * originCount);
	           controlPoint[2] = extrapoints[extranexti];
	           int nexti = (i + 1) % originCount;
	           controlPoint[3] = originPoints.get(nexti);
	           float u = 1;
	           while(u >= 0){
	        	   //计算贝塞尔曲线
	               double px = bezier3funcX(u,controlPoint);
	               double py = bezier3funcY(u,controlPoint);
	               //u的步长决定曲线的疏密
	               u -= step;
	               tempP = new HashMap<String, Object>();
	               tempP.put("lng", px);
	               tempP.put("lat", py);
	               //存入曲线点
	               result.add(tempP);
	           }
	    }

	    return result;
	}

	private double bezier3funcX(float uu, ReferencePoint[] controlPoint) {
		double part0 = controlPoint[0].getBaiduLng() * uu * uu * uu;
		double part1 = 3 * controlPoint[1].getBaiduLng() * uu * uu * (1 - uu);
		double part2 = 3 * controlPoint[2].getBaiduLng() * uu * (1 - uu) * (1 - uu);
		double part3 = controlPoint[3].getBaiduLng() * (1 - uu) * (1 - uu) * (1 - uu);
		return part0 + part1 + part2 + part3;
	}

	private double bezier3funcY(float uu, ReferencePoint[] controlPoint) {
		double part0 = controlPoint[0].getBaiduLat() * uu * uu * uu;
		double part1 = 3 * controlPoint[1].getBaiduLat() * uu * uu * (1 - uu);
		double part2 = 3 * controlPoint[2].getBaiduLat() * uu * (1 - uu) * (1 - uu);
		double part3 = controlPoint[3].getBaiduLat() * (1 - uu) * (1 - uu) * (1 - uu);
		return part0 + part1 + part2 + part3;
	}

	/**
	 * 获取百度坐标
	 * @param longitude
	 * @param latitude
	 * @param standardPoints
	 * @return
	 */
	private String[] getBaiduLnglat(double longitude, double latitude,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {

		String[] baidulnglat = null;
//		try {
//			// 先从缓存获取
//			baidulnglat = memCached
//					.get(RnoConstant.CacheConstant.GPSPOINT_POINT_PRE + suf);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		// log.info("缓存获取的gps坐标(" + suf + ")到百度坐标的映射关系为：" + baidulnglat);
		if (baidulnglat == null) {
			if (standardPoints != null && standardPoints.size() > 0) {
				baidulnglat = CoordinateHelper.getLngLatCorrectValue(longitude
						, latitude , standardPoints);
			} else {
				log.info("区域不存在基准点，将使用百度在线接口进行校正。");
				baidulnglat = CoordinateHelper.changeFromGpsToBaidu(longitude
						+ "", latitude + "");
			}
			// 保存入缓存
//			try {
//				memCached.set(RnoConstant.CacheConstant.GPSPOINT_POINT_PRE
//						+ suf, RnoConstant.TimeConstant.GPSTOBSIDUPOINTTIME,
//						baidulnglat);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}

		return baidulnglat;
	}

	/**
	 * 分页获取2g小区方向角计算任务信息
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年5月4日14:04:38
	 * @company 怡创科技
	 */
	public List<Map<String, Object>> query2GDirectionAngleTaskByPage(
			Map<String, String> cond, Page page) {
		log.info("进入方法：query2GDirectionAngleTaskByPage。condition=" + cond
				+ ",page=" + page);
		if (page == null) {
			log.warn("方法：query2GDirectionAngleTaskByPage的参数：page 是空！无法查询!");
			return Collections.EMPTY_LIST;
		}
		long totalCnt = page.getTotalCnt();
		if (totalCnt < 0) {
			totalCnt = rnoNcsDynaCoverageDao.get2GDirectionAngleTaskCount(cond);
			page.setTotalCnt((int) totalCnt);
		}
		int startIndex = page.calculateStart();
		int cnt = page.getPageSize();
		List<Map<String, Object>> res = rnoNcsDynaCoverageDao
				.query2GDirectionAngleTaskByPage(cond, startIndex, cnt);
		return res;
	}


	class ReferencePoint {
		double baiduLng;
		double baiduLat;

		public ReferencePoint(){
		}
		public ReferencePoint(double baiduLng, double baiduLat) {
			this.baiduLng = baiduLng;
			this.baiduLat = baiduLat;
		}
		public double getBaiduLng() {
			return baiduLng;
		}
		public void setBaiduLng(double baiduLng) {
			this.baiduLng = baiduLng;
		}
		public double getBaiduLat() {
			return baiduLat;
		}
		public void setBaiduLat(double baiduLat) {
			this.baiduLat = baiduLat;
		}
	}


	/**
	 * 查询对应条件的NCS数据记录数量
	 *
	 * @param cond
	 * @return
	 * @author peng.jm
	 * @date 2014-8-16下午05:02:24
	 */
	public long queryNcsDataCountByCond(Map<String, String> cond) {
		return (int) rnoNcsDynaCoverageDao.getNcsDataCount(cond);
	}

	/**
	 * 提交2g小区方向角计算任务信息
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年5月4日14:04:38
	 * @company 怡创科技
	 */
	public void add2GDirectionAngleTask(Map<String, String> cond, String account) {
		log.info("进入方法：add2GDirectionAngleTask。condition=" + cond
				+ ", account=" + account);
		// 创建主键
		final long descId = rnoNcsDynaCoverageDao
				.getNextSeqValue("SEQ_2G_DIRANGLE_DESC_ID");
		// 区域id
		final long cityId = Long.parseLong(cond.get("cityId").toString());
		// 创建日期
		Calendar cal = Calendar.getInstance();
		cal = Calendar.getInstance();
		final String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(cal.getTime());
		// NCS数据的开始测量日期
		final String startMeaDate = cond.get("begTime").toString();
		// NCS数据的结束测量日期
		final String endMeaDate = cond.get("endTime").toString();
		// 数据量
		final long recordNum = rnoNcsDynaCoverageDao.queryNcsDataRecordsCount(
				cityId, startMeaDate, endMeaDate);

//		System.out.println("统计数据量耗时："+((System.currentTimeMillis()-s)/1000));
//		s = System.currentTimeMillis();
		// 结果文件路径
		String rootPath = ServletActionContext.getServletContext()
				.getRealPath("/op/rno/ana_result/");
		final String filePath = rootPath+"/" + cal.get(Calendar.YEAR) + "/"
				+ (cal.get(Calendar.MONTH) + 1) +"/2g_angle/";

		// 创建job
		JobProfile job = new JobProfile();
		job.setAccount(account);
		job.setJobName("计算2G小区方向角");
		String jobType = cond.get("jobType").toString();
		log.debug("jobType=" + jobType);
		job.setJobType(jobType);
		job.setSubmitTime(new Date());
		job.setDescription("2g小区方向角计算job");

		JobClient jobClient=JobClientDelegate.getJobClient();
		jobClient.submitJob(job, new JobAddCallback<Boolean>() {
			@Override
			public Boolean doWhenJobAdded(JobProfile job) {
				// 获取jobId
				long jobId = job.getJobId();
				// 新建一个计算任务
				Map<String, Object> task = new HashMap<String, Object>();
				task.put("desc_id", descId);
				task.put("city_id", cityId);
				task.put("create_date", createDate);
				task.put("start_mea_date", startMeaDate);
				task.put("end_mea_date", endMeaDate);
				task.put("record_num", recordNum);
				task.put("file_path", filePath);
				task.put("work_status", "排队中");
				task.put("job_id", jobId); // 系统分配的工作id
				task.put("status", "N"); // 是否在系统中删除

				// 在数据库创建2g小区方向角计算任务
				boolean flag = rnoNcsDynaCoverageDao
						.create2GDirectionAngleTask(task);
				if (!flag) {
					log.info(descId + "的2g小区方向角计算任务创建失败");
					return false;
				}
				return true;
			}
		});
	}

	/**
	 * 获取画LTE小区动态覆盖图(贝塞尔曲线)所需的数据
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	public Map<String, List<Map<String, Object>>> get4GDynaCoverageDataByCityAndDate(
			String lteCellId, long cityId, String startDate, String endDate,double imgSizeCoeff,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {

		log.debug("get4GDynaCoverageDataByCityAndDate, lteCellId=" + lteCellId + ", cityId=" + cityId
				+ ", startDate=" + startDate + ", endDate=" + endDate);

		long stime = System.currentTimeMillis();

		//RELSS>-12
		List<Map<String,Object>> dynaCoverData_12 = new ArrayList<Map<String,Object>>();
		//RELSS>3
		List<Map<String,Object>> dynaCoverData_3 = new ArrayList<Map<String,Object>>();
		//关联度详情
		List<Map<String,Object>> resInterDetail = new ArrayList<Map<String,Object>>();

		Map<String,List<Map<String,Object>>> res = rnoNcsDynaCoverageDao
					.queryDynaCoverDataFromHbaseMrTable(cityId,lteCellId,startDate,endDate);

		dynaCoverData_12 = res.get("res1");
		dynaCoverData_3 = res.get("res2");
		resInterDetail=res.get("resInterDetail");
		if((dynaCoverData_12 == null || dynaCoverData_12.size() == 0)
				&&(dynaCoverData_3 == null || dynaCoverData_3.size() == 0)) {
			return null;
		}
		long etime = System.currentTimeMillis();
		log.debug("从数据库读取(RELSS>-12)数据量："+dynaCoverData_12.size()
				+";(RELSS>3)数据量："+dynaCoverData_3.size()+"; 共耗时："+(etime-stime));

		//获取画(RELSS>-12)图形所需曲线点坐标集合
		Map<String, List<Map<String, Object>>> res_12
				= calc4GDynaCoveragePointsData(dynaCoverData_12,imgSizeCoeff,standardPoints);
		//获取画(RELSS>3)图形所需曲线点坐标集合
		Map<String, List<Map<String, Object>>> res_3
				= calc4GDynaCoveragePointsData(dynaCoverData_3,imgSizeCoeff,standardPoints);

		Map<String, List<Map<String,Object>>> result = new HashMap<String, List<Map<String,Object>>>();
		result.put("vectorPoint_12", res_12.get("vectorPoint"));
		result.put("vectorPoint_3", res_3.get("vectorPoint"));
		result.put("curvePoints_12", res_12.get("curvePoints"));
		result.put("curvePoints_3", res_3.get("curvePoints"));
		result.put("resInterDetail", resInterDetail);
		return result;
	}

	/**
	 * 获取画LTE小区动态覆盖图(折线)所需的数据
	 * @title
	 * @return
	 * @author peng.jm
	 * @date 2015年3月10日10:59:56
	 * @company 怡创科技
	 */
	public Map<String, List<Map<String, Object>>> get4GDynaCoverageData2ByCityAndDate(
			String lteCellId, long cityId, String startDate, String endDate,
			double imgCoeff,double imgSizeCoeff,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {
		log.debug("getDynaCoverageData2ByCityAndDate, lteCellId=" + lteCellId + ", cityId=" + cityId
				+ ", startDate=" + startDate + ", endDate=" + endDate);

		long stime = System.currentTimeMillis();

		//RELSS>-12
		List<Map<String,Object>> dynaCoverData_12 = new ArrayList<Map<String,Object>>();
		//RELSS>3
		List<Map<String,Object>> dynaCoverData_3 = new ArrayList<Map<String,Object>>();
		//关联度详情
		List<Map<String,Object>> resInterDetail = new ArrayList<Map<String,Object>>();

		Map<String,List<Map<String,Object>>> res = rnoNcsDynaCoverageDao
					.queryDynaCoverDataFromHbaseMrTable(cityId,lteCellId,startDate,endDate);

		dynaCoverData_12 = res.get("res1");
		dynaCoverData_3 = res.get("res2");
		resInterDetail=res.get("resInterDetail");

		if((dynaCoverData_12 == null || dynaCoverData_12.size() == 0)
				&&(dynaCoverData_3 == null || dynaCoverData_3.size() == 0)) {
			return null;
		}
		long etime = System.currentTimeMillis();
		log.debug("从数据库读取(RELSS>-12)数据量："+dynaCoverData_12.size()
				+";(RELSS>3)数据量："+dynaCoverData_3.size()+"; 共耗时："+(etime-stime));

		//获取画(RELSS>-12)图形所需折线点坐标集合
		Map<String, List<Map<String, Object>>> res_12 = calc4GDynaCoveragePointsData2(
				dynaCoverData_12, imgCoeff,imgSizeCoeff, standardPoints);
		//获取画(RELSS>3)图形所需折线点坐标集合
		Map<String, List<Map<String, Object>>> res_3 = calc4GDynaCoveragePointsData2(
				dynaCoverData_3, imgCoeff,imgSizeCoeff, standardPoints);

		Map<String, List<Map<String,Object>>> result = new HashMap<String, List<Map<String,Object>>>();
		result.put("vectorPoint_12", res_12.get("vectorPoint"));
		result.put("vectorPoint_3", res_3.get("vectorPoint"));
		result.put("curvePoints_12", res_12.get("curvePoints"));
		result.put("curvePoints_3", res_3.get("curvePoints"));
		result.put("resInterDetail", resInterDetail);
		return result;
	}

	/**
	 * map根据value值,从大到小排序
	 * @param unsortMap
	 * @return
	 * @author peng.jm
	 */
	public Map sortMapByValue(Map unsortMap) {
        List list = new LinkedList(unsortMap.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                                       .compareTo(((Map.Entry) (o1)).getValue());
            }
        });
        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
	/**
	 *
	 * @title 以服务小区为中心，从第一象限到第四象限，按照角度大小排序并归纳叠加成16个点
	 * @param points
	 * @param cellbaiduLng
	 * @param cellbaiduLat
	 * @return
	 * @author chao.xj
	 * @date 2015-6-2上午10:26:49
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	private List<ReferencePoint> ascAndInduce16Points4GData2(List<ReferencePoint> points,
			double cellbaiduLng, double cellbaiduLat,Map<String, Double> lnglatToVector) {

		List<ReferencePoint> one = new ArrayList<ReferencePoint>();
		List<ReferencePoint> two = new ArrayList<ReferencePoint>();
		List<ReferencePoint> three = new ArrayList<ReferencePoint>();
		List<ReferencePoint> four = new ArrayList<ReferencePoint>();

		//划分落在每个象限的点

		for (ReferencePoint p : points) {
			//第一象限
			if(p.getBaiduLng()>=cellbaiduLng && p.getBaiduLat()>cellbaiduLat)
				one.add(p);
			//第二象限
			if(p.getBaiduLng()<cellbaiduLng && p.getBaiduLat()>=cellbaiduLat)
				two.add(p);
			//第三象限
			if(p.getBaiduLng()<=cellbaiduLng && p.getBaiduLat()<cellbaiduLat)
				three.add(p);
			//第四象限
			if(p.getBaiduLng()>cellbaiduLng && p.getBaiduLat()<=cellbaiduLat)
				four.add(p);
		}

		ReferencePoint temp = null;
		double xxi,yyi,xxj,yyj,sini,sinj;
		//将各自象限内的点按照角度划分顺序
		//第一象限
		for (int i = 0; i < one.size(); i++) {
			for (int j = i+1; j < one.size(); j++) {
				xxi = one.get(i).getBaiduLng() - cellbaiduLng;
				yyi = one.get(i).getBaiduLat() - cellbaiduLat;
				xxj = one.get(j).getBaiduLng() - cellbaiduLng;
				yyj = one.get(j).getBaiduLat() - cellbaiduLat;
				sini = yyi/(Math.sqrt(xxi*xxi+yyi*yyi));
				sinj = yyj/(Math.sqrt(xxj*xxj+yyj*yyj));
				if(sini > sinj) {
					temp = one.get(i);
					one.set(i, one.get(j));
					one.set(j, temp);
				}
			}
		}
		//第二象限
		for (int i = 0; i < two.size(); i++) {
			for (int j = i+1; j < two.size(); j++) {
				xxi = two.get(i).getBaiduLng() - cellbaiduLng;
				yyi = two.get(i).getBaiduLat() - cellbaiduLat;
				xxj = two.get(j).getBaiduLng() - cellbaiduLng;
				yyj = two.get(j).getBaiduLat() - cellbaiduLat;
				sini = yyi/(Math.sqrt(xxi*xxi+yyi*yyi));
				sinj = yyj/(Math.sqrt(xxj*xxj+yyj*yyj));
				if(sini < sinj) {
					temp = two.get(i);
					two.set(i, two.get(j));
					two.set(j, temp);
				}
			}
		}
		//第三象限
		for (int i = 0; i < three.size(); i++) {
			for (int j = i+1; j < three.size(); j++) {
				xxi = three.get(i).getBaiduLng() - cellbaiduLng;
				yyi = three.get(i).getBaiduLat() - cellbaiduLat;
				xxj = three.get(j).getBaiduLng() - cellbaiduLng;
				yyj = three.get(j).getBaiduLat() - cellbaiduLat;
				sini = yyi/(Math.sqrt(xxi*xxi+yyi*yyi));
				sinj = yyj/(Math.sqrt(xxj*xxj+yyj*yyj));
				if(sini < sinj) {
					temp = three.get(i);
					three.set(i, three.get(j));
					three.set(j, temp);
				}
			}
		}
		//第四象限
		for (int i = 0; i < four.size(); i++) {
			for (int j = i+1; j < four.size(); j++) {
				xxi = four.get(i).getBaiduLng() - cellbaiduLng;
				yyi = four.get(i).getBaiduLat() - cellbaiduLat;
				xxj = four.get(j).getBaiduLng() - cellbaiduLng;
				yyj = four.get(j).getBaiduLat() - cellbaiduLat;
				sini = yyi/(Math.sqrt(xxi*xxi+yyi*yyi));
				sinj = yyj/(Math.sqrt(xxj*xxj+yyj*yyj));
				if(sini > sinj) {
					temp = four.get(i);
					four.set(i, four.get(j));
					four.set(j, temp);
				}
			}
		}

		List<ReferencePoint> res = new ArrayList<ReferencePoint>();
		//int oneNum=0,twoNum=0,threeNum=0,fourNum=0;
		//对每一象限分成两份，取每份的数据矢量相加作为一个参考点，即每个象限取两个参考点
		//每个象限分四份,四个象限一共是16份
		ReferencePoint tempPoint = null;
		double tempLng = cellbaiduLng;
		double tempLat = cellbaiduLat;
		int num = 0;
		double maxVecVal=0;
		if(one.size() < 4) {
			for (int i = 0; i < one.size(); i++) {
				res.add(one.get(i));
			}
		} else {
			num = one.size()/4;

			for (int i = 0; i < num; i++) {
				/*tempLng += (one.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (one.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat());
						tempLng = one.get(i).getBaiduLng();
						tempLat = one.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;

			for (int i = num*1; i < num*2; i++) {
				//每等份矢量相加
				/*tempLng += (one.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (one.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat());
						tempLng = one.get(i).getBaiduLng();
						tempLat = one.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*2; i < num*3; i++) {
				/*tempLng += (one.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (one.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat());
						tempLng = one.get(i).getBaiduLng();
						tempLat = one.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*3; i < num*4; i++) {
				/*tempLng += (one.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (one.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat());
						tempLng = one.get(i).getBaiduLng();
						tempLat = one.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*4; i < one.size(); i++) {
				/*tempLng += (one.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (one.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(one.get(i).getBaiduLng()+"_"+one.get(i).getBaiduLat());
						tempLng = one.get(i).getBaiduLng();
						tempLat = one.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
		}

		//第二象限
		if(two.size() < 4) {
			for (int i = 0; i < two.size(); i++) {
				res.add(two.get(i));
			}
		} else {
			num = two.size()/4;
			for (int i = 0; i < num; i++) {
				/*tempLng += (two.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (two.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat());
						tempLng = two.get(i).getBaiduLng();
						tempLat = two.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*1; i < num*2; i++) {
				/*tempLng += (two.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (two.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat());
						tempLng = two.get(i).getBaiduLng();
						tempLat = two.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*2; i < num*3; i++) {
				/*tempLng += (two.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (two.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat());
						tempLng = two.get(i).getBaiduLng();
						tempLat = two.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*3; i < num*4; i++) {
			/*	tempLng += (two.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (two.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat());
						tempLng = two.get(i).getBaiduLng();
						tempLat = two.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*4; i < two.size(); i++) {
				/*tempLng += (two.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (two.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(two.get(i).getBaiduLng()+"_"+two.get(i).getBaiduLat());
						tempLng = two.get(i).getBaiduLng();
						tempLat = two.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
		}
		//第三象限
		if(three.size() < 4) {
			for (int i = 0; i < three.size(); i++) {
				res.add(three.get(i));
			}
		} else {
			num = three.size()/4;
			for (int i = 0; i < num; i++) {
				/*tempLng += (three.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (three.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat());
						tempLng = three.get(i).getBaiduLng();
						tempLat = three.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*1; i < num*2; i++) {
				/*tempLng += (three.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (three.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat());
						tempLng = three.get(i).getBaiduLng();
						tempLat = three.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*2; i < num*3; i++) {
				/*tempLng += (three.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (three.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat());
						tempLng = three.get(i).getBaiduLng();
						tempLat = three.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*3; i < num*4; i++) {
				/*tempLng += (three.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (three.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
				if(lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat());
						tempLng = three.get(i).getBaiduLng();
						tempLat = three.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*4; i < three.size(); i++) {
				/*tempLng += (three.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (three.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(three.get(i).getBaiduLng()+"_"+three.get(i).getBaiduLat());
						tempLng = three.get(i).getBaiduLng();
						tempLat = three.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
		}
		//第四象限
		if(four.size() < 4) {
			for (int i = 0; i < four.size(); i++) {
				res.add(four.get(i));
			}
		} else {
			num = four.size()/4;
			for (int i = 0; i < num; i++) {
				/*tempLng += (four.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (four.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat());
						tempLng = four.get(i).getBaiduLng();
						tempLat = four.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*1; i < num*2; i++) {
				/*tempLng += (four.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (four.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat());
						tempLng = four.get(i).getBaiduLng();
						tempLat = four.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*2; i < num*3; i++) {
				/*tempLng += (four.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (four.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat());
						tempLng = four.get(i).getBaiduLng();
						tempLat = four.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*3; i < num*4; i++) {
				/*tempLng += (four.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (four.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat());
						tempLng = four.get(i).getBaiduLng();
						tempLat = four.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
			tempLng = cellbaiduLng;
			tempLat = cellbaiduLat;
			for (int i = num*4; i < four.size(); i++) {
				/*tempLng += (four.get(i).getBaiduLng() - cellbaiduLng);
				tempLat += (four.get(i).getBaiduLat() - cellbaiduLat);*/
				//比较矢量长度
if(lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat())!=null){

					if(maxVecVal<lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat())){
						maxVecVal=lnglatToVector.get(four.get(i).getBaiduLng()+"_"+four.get(i).getBaiduLat());
						tempLng = four.get(i).getBaiduLng();
						tempLat = four.get(i).getBaiduLat();
					}
				}
			}
			maxVecVal=0;
			//System.out.println(tempLng+"---"+tempLat);
			tempPoint = new ReferencePoint(tempLng,tempLat);
			res.add(tempPoint);
		}
		//再次排序
		//已经排好序了
		//相邻矢量迭加
		List<ReferencePoint> vectorTmp = new ArrayList<ReferencePoint>();

		/*for (int i = 0; i < res.size(); i++) {

			if (i == res.size() - 1) {
				tempLng = cellbaiduLng;
				tempLat = cellbaiduLat;
				tempLng += (res.get(i).getBaiduLng() - cellbaiduLng);
				tempLng += (res.get(0).getBaiduLng() - cellbaiduLng);
				tempLat += (res.get(i).getBaiduLat() - cellbaiduLat);
				tempLat += (res.get(0).getBaiduLat() - cellbaiduLat);

				vectorTmp.add(new ReferencePoint(tempLng, tempLat));
			}

			for (int j = i + 1; j < res.size(); j++) {
				tempLng = cellbaiduLng;
				tempLat = cellbaiduLat;
				tempLng += (res.get(i).getBaiduLng() - cellbaiduLng);
				tempLng += (res.get(j).getBaiduLng() - cellbaiduLng);
				tempLat += (res.get(i).getBaiduLat() - cellbaiduLat);
				tempLat += (res.get(j).getBaiduLat() - cellbaiduLat);
				vectorTmp.add(new ReferencePoint(tempLng, tempLat));
				break;
			}
		}*/
		res.addAll(vectorTmp);
		return res;
	}
	/**
	 *
	 * @title 获取画LTE小区动态覆盖 in干扰所需的数据【小区位置】
	 * @param lteCellId
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @param standardPoints
	 * @return
	 * @author chao.xj
	 * @date 2015-6-12下午2:40:35
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String,String>> get4GDynaCoverageInInferDataByCityAndDate(
			String lteCellId, long cityId, String startDate, String endDate,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {
		log.debug("get4GDynaCoverageInInferDataByCityAndDate, lteCellId=" + lteCellId + ", cityId=" + cityId
				+ ", startDate=" + startDate + ", endDate=" + endDate);

		long stime = System.currentTimeMillis();
		List<Map<String,String>> sumDynaCoverData=new ArrayList<Map<String,String>>();
		List<Map<String,String>> dynaCoverDataEri = rnoNcsDynaCoverageDao
					.query4GCellMrIndexFromHBase("LTE4G_ERI_MR", cityId, lteCellId, startDate, endDate, "LTE4G_CELL_MEATIME");

		List<Map<String,String>> dynaCoverDataZte = rnoNcsDynaCoverageDao
				.query4GCellMrIndexFromHBase("LTE4G_ZTE_MR", cityId, lteCellId, startDate, endDate, "LTE4G_CELL_MEATIME");

		if((dynaCoverDataEri == null || dynaCoverDataEri.size() == 0)
				&&(dynaCoverDataZte == null || dynaCoverDataZte.size() == 0)) {
			return null;
		}
		//获取小区对应的形状坐标  cellid->113.105,23.0255;113.10571,23.02539;113.10525,23.02488
		Map<String, Object> cellToShapes=rnoNcsDynaCoverageDao.queryLteCellShapeDataByCityId(cityId);
		Map<String, Object> cellIdToBandTypes=rnoNcsDynaCoverageDao.queryCellBandTypeByCityId(cityId);

		if(dynaCoverDataEri!=null && dynaCoverDataEri.size()!=0){
			sumDynaCoverData.addAll(dynaCoverDataEri);
		}
		if(dynaCoverDataZte!=null && dynaCoverDataZte.size()!=0){
			sumDynaCoverData.addAll(dynaCoverDataZte);
		}
		long etime = System.currentTimeMillis();
		log.debug("从数据库读取(eri)数据量："+dynaCoverDataEri.size()
				+";(zte)数据量："+dynaCoverDataEri.size()+"; 共耗时："+(etime-stime));

		String cellId,ncellId;
		double cellLon,cellLat,ncellLon,ncellLat;
		String celllonlatArr[]=new String[2];
		String ncelllonlatArr[]=new String[2];

		List<Map<String,String>> cells=new ArrayList<Map<String,String>>();
		Map<String, String> cell=null;
		String cellshapelonlatArr[]=new String[2];
		String ncellshapelonlatArr[]=new String[2];
		//工参数据为GPS坐标，通过基准点数据进行纠集获取百度坐标
		for (int i = 0; i < sumDynaCoverData.size(); i++) {

			cell=new HashMap<String, String>();

			cellId=sumDynaCoverData.get(i).get("CELL_ID");
			/*cellLon=sumDynaCoverData.get(i).get("CELL_LON");
			cellLat=sumDynaCoverData.get(i).get("CELL_LAT");*/
			ncellId=sumDynaCoverData.get(i).get("NCELL_ID");
			/*ncellLon=sumDynaCoverData.get(i).get("NCELL_LON");
			ncellLat=sumDynaCoverData.get(i).get("NCELL_LAT");*/
			if("".equals(ncellId)){
				continue;
			}
			/*1.	室分不参与计算，只考虑大站
			2.	近距离邻区不考虑，暂定为距离小于100米的邻区不参与计算
			3.	距离大于5公里小区剔除。
			4.	合并后timestotal小区50的小区剔除。*/
			if (Double.parseDouble(sumDynaCoverData.get(i).get("DISTANCE")) > 5
					|| Double.parseDouble(sumDynaCoverData.get(i).get(
							"DISTANCE")) < 0.1
					|| Double.parseDouble(sumDynaCoverData.get(i).get(
							"TIMESTOTAL")) < 50
					|| Pattern.matches("e|E|E频段", cellIdToBandTypes.get(cellId)
							.toString())) {
				continue;
			}
			cellshapelonlatArr=cellToShapes.get(cellId).toString().split(";");
			//获取中心点坐标
			cellLon = (Double.parseDouble(cellshapelonlatArr[1].substring(0,
					cellshapelonlatArr[1].indexOf(","))) + Double
					.parseDouble(cellshapelonlatArr[2].substring(0,
							cellshapelonlatArr[2].indexOf(",")))) / 2;
			cellLat = (Double.parseDouble(cellshapelonlatArr[1].substring(
					cellshapelonlatArr[1].split(";")[0].indexOf(",") + 1,
					cellshapelonlatArr[1].length())) + Double
					.parseDouble(cellshapelonlatArr[2].substring(
							cellshapelonlatArr[2].indexOf(",") + 1,
							cellshapelonlatArr[2].length()))) / 2;
			ncellshapelonlatArr=cellToShapes.get(ncellId).toString().split(";");

			ncellLon = (Double.parseDouble(ncellshapelonlatArr[1].substring(0,
					ncellshapelonlatArr[1].indexOf(","))) + Double
					.parseDouble(ncellshapelonlatArr[2].substring(0,
							ncellshapelonlatArr[2].indexOf(",")))) / 2;
			ncellLat = (Double.parseDouble(ncellshapelonlatArr[1].substring(
					ncellshapelonlatArr[1].split(";")[0].indexOf(",") + 1,
					ncellshapelonlatArr[1].length())) + Double
					.parseDouble(ncellshapelonlatArr[2].substring(
							ncellshapelonlatArr[2].indexOf(",") + 1,
							ncellshapelonlatArr[2].length()))) / 2;

			cell.put("CELL_ID", cellId);
			cell.put("NCELL_ID", ncellId);
			//此处校正坐标
			celllonlatArr=getBaiduLnglat(cellLon,cellLat, standardPoints);
			ncelllonlatArr=getBaiduLnglat(ncellLon,ncellLat, standardPoints);
			cell.put("CELL_LON", celllonlatArr[0]);
			cell.put("CELL_LAT", celllonlatArr[1]);
			cell.put("NCELL_LON", ncelllonlatArr[0]);
			cell.put("NCELL_LAT", ncelllonlatArr[1]);

			cells.add(cell);
		}


		return cells;
	}
	/**
	 *
	 * @title 获取画LTE小区动态覆盖 out干扰所需的数据[邻区位置]
	 * @param lteCellId
	 * @param cityId
	 * @param startDate
	 * @param endDate
	 * @param standardPoints
	 * @return
	 * @author chao.xj
	 * @date 2015-6-12下午2:40:04
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String,String>> get4GDynaCoverageOutInferDataByCityAndDate(
			String lteCellId, long cityId, String startDate, String endDate,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {
		log.debug("get4GDynaCoverageOutInferDataByCityAndDate, lteCellId=" + lteCellId + ", cityId=" + cityId
				+ ", startDate=" + startDate + ", endDate=" + endDate);

		long stime = System.currentTimeMillis();
		List<Map<String,String>> sumDynaCoverData=new ArrayList<Map<String,String>>();
		List<Map<String,String>> dynaCoverDataEri = rnoNcsDynaCoverageDao
					.query4GCellMrIndexFromHBase("LTE4G_ERI_MR", cityId, lteCellId, startDate, endDate, "LTE4G_NCELL_MEATIME");

		List<Map<String,String>> dynaCoverDataZte = rnoNcsDynaCoverageDao
				.query4GCellMrIndexFromHBase("LTE4G_ZTE_MR", cityId, lteCellId, startDate, endDate, "LTE4G_NCELL_MEATIME");

		if((dynaCoverDataEri == null || dynaCoverDataEri.size() == 0)
				&&(dynaCoverDataZte == null || dynaCoverDataZte.size() == 0)) {
			return null;
		}
		//获取小区对应的形状坐标  cellid->113.105,23.0255;113.10571,23.02539;113.10525,23.02488
		Map<String, Object> cellToShapes=rnoNcsDynaCoverageDao.queryLteCellShapeDataByCityId(cityId);
		Map<String, Object> cellIdToBandTypes=rnoNcsDynaCoverageDao.queryCellBandTypeByCityId(cityId);

		if(dynaCoverDataEri!=null && dynaCoverDataEri.size()!=0){
			sumDynaCoverData.addAll(dynaCoverDataEri);
		}
		if(dynaCoverDataZte!=null && dynaCoverDataZte.size()!=0){
			sumDynaCoverData.addAll(dynaCoverDataZte);
		}
		long etime = System.currentTimeMillis();
		log.debug("从数据库读取(eri)数据量："+dynaCoverDataEri.size()
				+";(zte)数据量："+dynaCoverDataEri.size()+"; 共耗时："+(etime-stime));

		String cellId,ncellId;
		double cellLon,cellLat,ncellLon,ncellLat;
		String celllonlatArr[]=new String[2];
		String ncelllonlatArr[]=new String[2];

		List<Map<String,String>> cells=new ArrayList<Map<String,String>>();
		Map<String, String> cell=null;
		String cellshapelonlatArr[]=new String[2];
		String ncellshapelonlatArr[]=new String[2];
		//工参数据为GPS坐标，通过基准点数据进行纠集获取百度坐标
		for (int i = 0; i < sumDynaCoverData.size(); i++) {

			cell=new HashMap<String, String>();

			cellId=sumDynaCoverData.get(i).get("CELL_ID");
			/*cellLon=sumDynaCoverData.get(i).get("CELL_LON");
			cellLat=sumDynaCoverData.get(i).get("CELL_LAT");*/
			ncellId=sumDynaCoverData.get(i).get("NCELL_ID");
			/*ncellLon=sumDynaCoverData.get(i).get("NCELL_LON");
			ncellLat=sumDynaCoverData.get(i).get("NCELL_LAT");*/
			if("".equals(ncellId)){
				continue;
			}
			/*1.	室分不参与计算，只考虑大站
			2.	近距离邻区不考虑，暂定为距离小于100米的邻区不参与计算
			3.	距离大于5公里小区剔除。
			4.	合并后timestotal小区50的小区剔除。*/
			if (Double.parseDouble(sumDynaCoverData.get(i).get("DISTANCE")) > 5
					|| Double.parseDouble(sumDynaCoverData.get(i).get(
							"DISTANCE")) < 0.1
					|| Double.parseDouble(sumDynaCoverData.get(i).get(
							"TIMESTOTAL")) < 50
					|| Pattern.matches("e|E|E频段", cellIdToBandTypes.get(cellId)
							.toString())) {
				continue;
			}
			cellshapelonlatArr=cellToShapes.get(cellId).toString().split(";");
			//获取中心点坐标
			cellLon = (Double.parseDouble(cellshapelonlatArr[1].substring(0,
					cellshapelonlatArr[1].indexOf(","))) + Double
					.parseDouble(cellshapelonlatArr[2].substring(0,
							cellshapelonlatArr[2].indexOf(",")))) / 2;
			cellLat = (Double.parseDouble(cellshapelonlatArr[1].substring(
					cellshapelonlatArr[1].split(";")[0].indexOf(",") + 1,
					cellshapelonlatArr[1].length())) + Double
					.parseDouble(cellshapelonlatArr[2].substring(
							cellshapelonlatArr[2].indexOf(",") + 1,
							cellshapelonlatArr[2].length()))) / 2;
			ncellshapelonlatArr=cellToShapes.get(ncellId).toString().split(";");

			ncellLon = (Double.parseDouble(ncellshapelonlatArr[1].substring(0,
					ncellshapelonlatArr[1].indexOf(","))) + Double
					.parseDouble(ncellshapelonlatArr[2].substring(0,
							ncellshapelonlatArr[2].indexOf(",")))) / 2;
			ncellLat = (Double.parseDouble(ncellshapelonlatArr[1].substring(
					ncellshapelonlatArr[1].split(";")[0].indexOf(",") + 1,
					ncellshapelonlatArr[1].length())) + Double
					.parseDouble(ncellshapelonlatArr[2].substring(
							ncellshapelonlatArr[2].indexOf(",") + 1,
							ncellshapelonlatArr[2].length()))) / 2;

			cell.put("CELL_ID", ncellId);
			cell.put("NCELL_ID", cellId);
			//此处校正坐标
			celllonlatArr=getBaiduLnglat(cellLon, cellLat, standardPoints);
			ncelllonlatArr=getBaiduLnglat(ncellLon, ncellLat, standardPoints);
			cell.put("CELL_LON", ncelllonlatArr[0]);
			cell.put("CELL_LAT", ncelllonlatArr[1]);
			cell.put("NCELL_LON", celllonlatArr[0]);
			cell.put("NCELL_LAT", celllonlatArr[1]);

			cells.add(cell);
		}


		return cells;
	}
}
