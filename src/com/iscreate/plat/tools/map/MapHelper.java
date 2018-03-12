package com.iscreate.plat.tools.map;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import com.google.gson.Gson;
import com.iscreate.plat.tools.InterfaceUtil;

public class MapHelper {
	
	//经纬度转换地址 百度地图api url
	public static final String MAPCODEURL = "http://api.map.baidu.com/geocoder/v2/?ak=844f1e1721c48a98b2c8dd39fee35a78&output=json&pois=0";
	
	/**
	 * 
	 * @description: 经纬度转换为实际地址
	 * @author：yuan.yw
	 * @param longitude
	 * @param latitude
	 * @return     
	 * @return String     
	 * @date：Jul 22, 2013 2:30:21 PM
	 */
	public static String convertLatlngToAddress(String longitude,String latitude){
		String result = "";
		try {
			String location ="&location="+latitude+","+longitude;
			result = httpClientReuqest(MAPCODEURL+location);//http 链接
			if(result!=null && !"".equals(result)){//返回结果 取出地址
				Gson gson = new Gson();
				MapReturnResult mapReturnResult = null;
				mapReturnResult = gson.fromJson(result,MapReturnResult.class);//json格式数据转换
				if(mapReturnResult!=null){
					MapAddressResult r = mapReturnResult.getResult();
					if("0".equals(mapReturnResult.getStatus())){//status 为 0 返回成功 具体status值参见mapReturnResult.class
						result = r.getFormatted_address();//实际地址
					}else{
						result = "";
					}
				}
			}
		} catch (Exception e) {
			result="";
		} 
		return result;//返回结果
	}
	
	/**
	 * 
	 * @description: http连接获取位置信息
	 * @author：yuan.yw
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException     
	 * @return String     
	 * @date：Jul 30, 2013 3:01:54 PM
	 */
	public static String httpClientReuqest(String url) throws ClientProtocolException, IOException{
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		if (url == null || "".equals(url))
			return "";
		try {
			//超时设置
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000); 
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
			HttpGet httpget = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = httpclient.execute(httpget, responseHandler);
			responseBody  = java.net.URLDecoder.decode(responseBody, "UTF-8");
		}  catch (Exception e) {
			return "";
		}finally {
			httpclient.getConnectionManager().shutdown();
		}
		return responseBody;
	}
	/*public static void main(String[] args){
		System.out.println(MapHelper.convertLatlngToAddress("116.322987", "39.983424")+"-----");
	}*/
}
