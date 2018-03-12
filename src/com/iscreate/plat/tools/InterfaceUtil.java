package com.iscreate.plat.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.iscreate.op.service.workmanage.util.InterfaceConfLoadUtil;

public class InterfaceUtil {

	public InputStream getConfigFileIn() {
		String path = InterfaceUtil.class.getClassLoader().getResource("").getPath();
	    String source_file = "";//源配置文件
	    if(path.indexOf("/") != -1){//windows环境
	    	 source_file = "//properties//InterfaceURLConfig.properties";
	    }else{//linux环境
	    	 source_file = "\\properties\\InterfaceURLConfig.properties";
	    }
		return this.getClass().getResourceAsStream(
				source_file);
	}
	
	
	/**
	 * che.yd httpClient请求
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static String httpPostClientReuqest(String url) throws ClientProtocolException, IOException{
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		if (url == null || "".equals(url))
			return null;
		try {
			HttpGet httpget = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = httpclient.execute(httpget, responseHandler);
			responseBody  = java.net.URLDecoder.decode(responseBody, "UTF-8");
		}  catch (Exception e) {
			
		}finally {
			httpclient.getConnectionManager().shutdown();
		}

		return responseBody;

	}
	

	public static String getResponseFromClient(String url) {
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		if (url == null || "".equals(url))
			return null;
		try {
			HttpGet httpget = new HttpGet(url);
			// Create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = httpclient.execute(httpget, responseHandler);
			responseBody  = java.net.URLDecoder.decode(responseBody, "UTF-8");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		
//		System.out.println("in class com.iscreate.util.InterfaceUtil");
//		System.out.println("return data===="+responseBody);
		return responseBody;
	}

	public static String getConfigValueFromProperties(String keyName) {
		String configValue = null;
		Properties prop = null;
		InputStream in = null;
		try {
			prop = new Properties();
			InterfaceUtil interfaceUtil = new InterfaceUtil();
			in = interfaceUtil.getConfigFileIn();
			prop.load(in);
			if (prop != null) {
				configValue = prop.getProperty(keyName.trim()) == null ? ""
						: prop.getProperty(keyName.trim()).trim();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return configValue;
	}
	
	public static String httpClientGetReuqest(String url) throws ClientProtocolException, IOException{
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),15000);
		HttpConnectionParams.setSoTimeout(httpclient.getParams(),15000);

		if (url == null || "".equals(url))
			return null;
		try {
			HttpGet httpget = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = httpclient.execute(httpget, responseHandler);
			responseBody  = java.net.URLDecoder.decode(responseBody, "UTF-8");
		}  finally {
			httpclient.getConnectionManager().shutdown();
		}
		return responseBody;
	}
}
