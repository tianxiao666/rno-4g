package com.iscreate.op.service.rno.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;

import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.plat.tools.LatLngHelper;

public class CoordinateHelper {

	private static Log log = LogFactory.getLog(CoordinateHelper.class);
	static double M_PI = Math.PI;
	private static BASE64Decoder decoder= new BASE64Decoder();

	/**
	 * 经纬度转墨卡托
	 * 
	 * @param 经度(lon)，纬度(lat)
	 * @return 平面坐标一维数组xy
	 */
	public static double[] lonLat2Mercator(double lon, double lat) {

		double[] xy = new double[2];
		double x = lon * 20037508.342789 / 180;
		double y = Math.log(Math.tan((90 + lat) * M_PI / 360)) / (M_PI / 180);
		y = y * 20037508.34789 / 180;
		xy[0] = x;
		xy[1] = y;
		return xy;
	}

	/**
	 * 墨卡托转经纬度
	 * 
	 * @param mercatorX
	 * @param mercatorY
	 * @return 经纬度一维数组xy
	 */
	public static double[] Mercator2lonLat(double mercatorX, double mercatorY) {

		double[] xy = new double[2];
		double x = mercatorX / 20037508.34 * 180;
		double y = mercatorY / 20037508.34 * 180;
		y = 180 / M_PI * (2 * Math.atan(Math.exp(y * M_PI / 180)) - M_PI / 2);
		xy[0] = x;
		xy[1] = y;
		return xy;
	}

	/**
	 * 输入坐标原点,方位角,边长:输出其他两个顶点坐标
	 * 
	 * @param 经度(Longitude)
	 * @param 纬度(Latitude)
	 * @param 方位角(azimuth)
	 * @param 边长(lenofside)
	 * @return 二维坐标(coordinates)
	 */
	public static double[][] OutputCoordinates(double Longitude,
			double Latitude, int azimuth, int ScatterAngle,int lenofside) {
		double[] xy = new double[2];
		// 球面坐标原点到平面坐标
		xy = lonLat2Mercator(Longitude, Latitude);
		// 计算三角形顶点的平面坐标
		double topX = Math.sin((azimuth - ScatterAngle/2) * Math.PI / 180) * lenofside;// 后面的lenofside为边长,azimuth为方位角
		double topY = Math.cos((azimuth - ScatterAngle/2) * Math.PI / 180) * lenofside;
		// 计算三角形底部点的平面坐标
		double bottomY = Math.sin((90 - azimuth - ScatterAngle/2) * Math.PI / 180)
				* lenofside;
		double bottomX = Math.cos((90 - azimuth - ScatterAngle/2) * Math.PI / 180)
				* lenofside;
		// 平面坐标到球面坐标
		// 引用函数
		// 考虑原点偏移量
		double[] topLngLat = Mercator2lonLat(topX + xy[0], topY + xy[1]);
		double[] bottomLngLat = Mercator2lonLat(bottomX + xy[0], bottomY
				+ xy[1]);
		double[][] coordinates = { topLngLat, bottomLngLat };
		return coordinates;

	}

	
	public static String[] changeFromGpsToBaidu(String gpsLng,String gpsLat){
		long t1 = System.currentTimeMillis();
		String[] bdLngLat = null;
		try {
			Socket s = new Socket("api.map.baidu.com", 80);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					s.getInputStream(), "UTF-8"));
			OutputStream out = s.getOutputStream();
			StringBuffer sb = new StringBuffer(
					"GET /ag/coord/convert?from=0&to=4");
			sb.append("&x=" + gpsLng + "&y=" + gpsLat);
			sb.append("&callback=BMap.Convertor.cbk_3976 HTTP/1.1\r\n");
			sb.append("User-Agent: Java/1.6.0_20\r\n");
			sb.append("Host: api.map.baidu.com:80\r\n");
			sb.append("Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r\n");
			sb.append("Connection: Close\r\n");
			sb.append("\r\n");
			out.write(sb.toString().getBytes());
			String json = "";
			String tmp = "";
			while ((tmp = br.readLine()) != null) {
//				 System.out.println(tmp);
				json += tmp;
			}
			System.out.println("GPS转百度，连接用时： " + (System.currentTimeMillis()-t1) + " 豪秒");
			int start = json.indexOf("cbk_3976");
			int end = json.lastIndexOf("}");
			if (start != -1 && end != -1&& json.contains("\"x\":\"")) {
				json = json.substring(start, end);
				String[] point = json.split(",");
				String x = point[1].split(":")[1].replace("\"", "");
				String y = point[2].split(":")[1].replace("\"", "");
				/*return new String[]{new String(decode(x)) , 
						            new String(decode(y))};*/
				bdLngLat = new String[]{new String(decode(x)) , new String(decode(y))};
			} else {
				log.error("gps坐标("+gpsLng+","+gpsLat+")无效！！");
			}
			s.close();
			out.close();
			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		//return null;
		return bdLngLat;
	}
	
	public static String[] changeFromBaiduToGps(String baiduLng,String baiduLat){
		String[] gpsLngLat = null;
		int times = 0;
		do {
			try {
				Socket s = new Socket("api.map.baidu.com", 80);
				s.setSoTimeout(1000);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						s.getInputStream(), "UTF-8"));
				OutputStream out = s.getOutputStream();
				StringBuffer sb = new StringBuffer(
						"GET /ag/coord/convert?from=0&to=4");
				sb.append("&x=" + baiduLng + "&y=" + baiduLat);
				sb.append("&callback=BMap.Convertor.cbk_3976 HTTP/1.1\r\n");
				sb.append("User-Agent: Java/1.6.0_20\r\n");
				sb.append("Host: api.map.baidu.com:80\r\n");
				sb.append("Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r\n");
				sb.append("Connection: Close\r\n");
				sb.append("\r\n");
				out.write(sb.toString().getBytes());
				String json = "";
				String tmp = "";
				while ((tmp = br.readLine()) != null) {
					json += tmp;
				}
				int start = json.indexOf("cbk_3976");
				int end = json.lastIndexOf("}");
				if (start != -1 && end != -1 && json.contains("\"x\":\"")) {
					json = json.substring(start, end);
					String[] point = json.split(",");
					String x = point[1].split(":")[1].replace("\"", "");
					String y = point[2].split(":")[1].replace("\"", "");
					// 解码
					x = new String(decode(x));
					y = new String(decode(y));
					// 转换
					x = (2 * Double.parseDouble(baiduLng) - Double
							.parseDouble(x)) + "";
					y = (2 * Double.parseDouble(baiduLat) - Double
							.parseDouble(y)) + "";
					// return new String[]{x , y};
					gpsLngLat = new String[] { x, y };
				} else {
					times++;
					log.error("baidu坐标(" + baiduLng + "," + baiduLat + ")无效！！");
				}
				s.close();
				out.close();
				br.close();
			} catch (Exception e) {
				times++;
				String error = (times<3)?"尝试重新连接。。。":"失败次数过多！！！";
				log.error("转换baidu坐标(" + baiduLng + "," + baiduLat + ")为GPS坐标，第"+times+"次连接出错，"+error);
				//e.printStackTrace();
			}
		} while (gpsLngLat == null && times < 3);
		//return null;
		return gpsLngLat;
	}
	
	/**
	 * 解码
	 * 
	 * @param str
	 * @return string
	 */
	
	public static byte[] decode(String str) {

		byte[] bt = null;

		try {
			 
			bt = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bt;
	}

	
	public static String[] getLngLatCorrectValue(double lng, double lat,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> map) {

		log.info("------------------开始处理gps点：("+lng+","+lat+")");
		String[] newlnglat = new String[2];
		newlnglat[0] = lng+"";
		newlnglat[1] = lat+"";

		List<RnoMapLnglatRelaGps> allSpots=new ArrayList<RnoMapLnglatRelaGps>();
		for (AreaRectangle rec : map.keySet()) {
			if (rec.isContainsPoint(lng, lat)) {
				//System.out.println("rec="+rec+"  包含了坐标：("+lng+","+lat+")");
				if(map.get(rec)!=null){
					allSpots.addAll(map.get(rec));
				}
			}
		}
		double minDistance=Double.MAX_VALUE;
		double dis=minDistance;
		String tmp;
		String[] ss;
		double tlng=0,tlat=0,toffsetlng=0,toffsetlat=0;
		for(RnoMapLnglatRelaGps one:allSpots){
			tmp=one.getGps();
			if(tmp==null){
				continue;
			}
			ss=tmp.split(",");
			if(ss.length!=2){
				continue;
			}
			try{
				tlng=Double.parseDouble(ss[0]);
				tlat=Double.parseDouble(ss[1]);
			}catch(Exception e){
				continue;
			}
			dis=LatLngHelper.Distance(lng, lat, tlng, tlat);
			if(dis<minDistance){
				minDistance=dis;
				tmp=one.getOffset();
				if(tmp==null){
					continue;
				}
				ss=tmp.split(",");
				try{
					toffsetlng=Double.parseDouble(ss[0]);
					toffsetlat=Double.parseDouble(ss[1]);
				}catch(Exception e){
					continue;
				}
				newlnglat[0]=lng+toffsetlng+"";
				newlnglat[1]=lat+toffsetlat+"";
			}
		}
		
		//System.out.println("最接近("+lng+","+lat+")的基准点是：("+tlng+","+tlat+")");
		return newlnglat;
	}
	/**
	 * 
	 * @title 获取百度坐标
	 * @param longitude
	 * @param latitude
	 * @param standardPoints
	 * @return
	 * @author chao.xj
	 * @date 2015-7-20下午4:53:49
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public static String[] getBaiduLnglat(double longitude, double latitude,
			Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints) {

		String[] baidulnglat = null;
		if (baidulnglat == null) {
			if (standardPoints != null && standardPoints.size() > 0) {
				baidulnglat = getLngLatCorrectValue(longitude
						, latitude , standardPoints);
			} else {
				log.info("区域不存在基准点，将使用百度在线接口进行校正。");
				baidulnglat = changeFromGpsToBaidu(longitude
						+ "", latitude + "");
			}
		}
		
		return baidulnglat;
	}
	
	public static void main(String[] args) {

/*		double[] num;
		double[] num1;
		num = lonLat2Mercator(113.34691, 23.14401);
		// num1=lonLat2Mercator1(23.14401, 113.34691);
		for (int i = 0; i < num.length; i++) {
			System.out.println(num[i]);
		}
		num1 = Mercator2lonLat(1.2617720304190855E7, 2649443.5269665434);
		for (int i = 0; i < num1.length; i++) {
			System.out.println(num1[i]);
		}
		double[][] dd = OutputCoordinates(113.40195594215, 22.948665186881, 130,60, 120);
		for (int i = 0; i < dd.length; i++) {
			for (int j = 0; j < dd[i].length; j++) {
				System.out.println("aa:"+dd[i][j]);
				// System.out.println((dd[0][0])+","+dd[0][1]+","+dd[1][0]+","+dd[1][1]);
			}
		}*/
/*		String aa[] = null;
		for(int i=0;i<10;i++){
		aa=changeFromGpsToBaidu("113.38999", "22.9449");
		}
		System.out.println(aa[0]+"----"+aa[1]);*/
		// System.out.println(Math.PI*2*6378137);
		long t1 = System.currentTimeMillis();
		String bb[] = null;
		int cnt = 0;
		for(int i=0;i<100;i++){
			if(i%10 == 0)System.out.println(i);
		bb=changeFromBaiduToGps("113.528657", "22.661484");
		if(bb==null)cnt++;
		}
		System.out.println(System.currentTimeMillis()-t1+"*****"+cnt);
		System.out.println(bb[0]+"----"+bb[1]);
		//东莞市 百度区域边界：maxlng=114.265529,minlng=113.528657,maxlat=23.149034,minlat=22.661484
	}
}