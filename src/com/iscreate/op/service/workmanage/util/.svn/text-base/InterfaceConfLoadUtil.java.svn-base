package com.iscreate.op.service.workmanage.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class InterfaceConfLoadUtil {

	
	public InputStream getConfigFileIn() {
		String path = InterfaceConfLoadUtil.class.getClassLoader().getResource("").getPath();
	    String source_file = "";//源配置文件
	    if(path.indexOf("/") != -1){//windows环境
	    	 source_file = "properties//InterfaceURLConfig.properties";
	    }else{//linux环境
	    	 source_file = "properties\\InterfaceURLConfig.properties";
	    }
		return this.getClass().getResourceAsStream(
				source_file);
	}
	
	
	public static String getResponseFromClient(String url) {
		String responseBody = null;
		HttpClient httpclient = new DefaultHttpClient();
		if (url == null || "".equals(url))
			return null;
		try {
			HttpGet httpget = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = httpclient.execute(httpget, responseHandler);
			responseBody  = java.net.URLDecoder.decode(responseBody, "UTF-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		
		return responseBody;
	}

	public static String getConfigValueFromProperties(String keyName) {
		String configValue = null;
		Properties prop = null;
		InputStream in = null;
		try {
			prop = new Properties();
			InterfaceConfLoadUtil interfaceUtil = new InterfaceConfLoadUtil();
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
	
	
	
	
	/**
	 * 获取配置文件的InpustStream
	 * @param pathFile
	 * @return
	 */
	public static InputStream getConfigFileInput(String pathFile) {
		return InterfaceConfLoadUtil.class.getResourceAsStream(pathFile);
	}
	
	
	
	/**
	 * 获取消息盒子配置的快超时时间定义
	 * @return
	 */
	public static Integer getBizMsgOverTimeConfig(String pathFile){
		Integer res=null;
		try {
			InputStream input=InterfaceConfLoadUtil.getConfigFileInput(pathFile);
			SAXReader reader = new SAXReader();
			Document doc = reader.read(input);
			Element root = doc.getRootElement();
			for (Iterator it = root.elementIterator(); it.hasNext();) {
				Element element = (Element) it.next();
				String str_tempBizMsgOverTime=element.getText();
				res=Integer.parseInt(str_tempBizMsgOverTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	/**
	 * 获取工单导出配置的列表信息
	 * @param pathFile
	 * @return
	 */
	public static Map<String,String> getExportOrderMapping(String pathFile){
		Map<String,String> resultMap=new HashMap<String,String>();
		InputStream input=getConfigFileInput(pathFile);
		SAXReader reader = new SAXReader();
		Document doc=null;
		try {
			doc = reader.read(input);
			Element root = doc.getRootElement();
			resultMap=getElementList(root,resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return resultMap;
	}
	
	 /** 
     * 遍历所有子孙节点方法，并以Map形式返回
     * 
     * @param element 
     */ 
    public static Map<String,String> getElementList(Element element,Map<String,String> resultMap) {
        List elements = element.elements();
        if (!elements.isEmpty()) {	//有子元素 
        	
            for (Iterator it = elements.iterator(); it.hasNext();) { 
                Element children = (Element) it.next();
                String elementName=children.getName();
                Attribute attrName=children.attribute("name");
                if(attrName!=null){
                	resultMap.put(elementName, attrName.getText());
                }
               
                getElementList(children,resultMap); 
            } 
        }
        
        return resultMap;
    }
	
}
