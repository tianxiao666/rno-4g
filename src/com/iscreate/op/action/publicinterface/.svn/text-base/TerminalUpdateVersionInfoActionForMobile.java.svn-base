package com.iscreate.op.action.publicinterface;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.service.outlinking.OutLinkingService;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.mobile.util.MobilePackageUtil;

public class TerminalUpdateVersionInfoActionForMobile {
	
	private  static final  Log log  =  LogFactory.getLog(TerminalUpdateVersionInfoActionForMobile.class);
	/**
	 * 
	 * @description: 通过读取配置文件获取终端版本更新信息
	 * @author：yuan.yw
	 * @return     版本更新信息
	 * @return void
	 * @date：Jun 20, 2013 4:50:45 PM
	 */
	public void getTerminalUpdateVersionInfoActionForMobile(){
		log.info("进入getTerminalUpdateVersionInfoActionForMobile方法,获取终端版本更新信息。");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/json");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		MobilePackage mobilePackage = MobilePackageUtil.getMobilePackage();
		// mobilePackage为空，返回错误信息
		if (mobilePackage == null) {
			MobilePackage newMobilePackage = new MobilePackage();
			newMobilePackage.setResult("error");
			// 返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(newMobilePackage);
			try {
				log.info("退出 getTerminalUpdateVersionInfoActionForMobile，返回结果"+resultPackageJsonStr);
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e) {
				log.error("退出 getTerminalUpdateVersionInfoActionForMobile，发生异常，返回结果"+resultPackageJsonStr+"失败");
				e.printStackTrace();
			}
		}
		String content = mobilePackage.getContent();
		MobileContentHelper mch = new MobileContentHelper();
		mch.setContent(content);
		Map<String, String> formJsonMap = mch.getGroupByKey("request");
		String versionCode = formJsonMap.get("versionCode");//版本码
		String versionName = formJsonMap.get("versionName");//版本号
		long currentCode = 0L;
		Map<String,String> map = new HashMap<String, String>();//版本更新信息
		try {
			String filePath = TerminalUpdateVersionInfoActionForMobile.class.getClassLoader().getResource("/").getPath();
			filePath += "sysconfig.xml";//配置文件
			SAXReader reader = new SAXReader();
			Document doc;
			doc = reader.read(filePath);
			List<Element> selectNodes = doc.selectNodes("SystemConfig/Terminalupdatemess");//读取节点信息
			List<Element> enviromentElementList = selectNodes.get(0).elements();
			if(enviromentElementList!=null && enviromentElementList.size()>0){
				for (Element enviromentElement : enviromentElementList) {
					map.put(enviromentElement.getName(), enviromentElement.getText());//节点信息保存在map里
				}
			}
			String code = map.get("versionCode");
			//String name = map.get("versionName");
			String url = map.get("url");
			long codeValue = 0L;
			if(code!=null && !"".equals(code) && versionCode!=null && !"".equals(versionCode)){
				codeValue = Long.valueOf(code);
				currentCode=Long.valueOf(versionCode);
			}
			if(currentCode<codeValue){
				map.put("fileSize", this.getFileSize(url));
			}else{
				for(String key:map.keySet()){
					map.put(key, "");
				}
				map.put("fileSize", "");
			}
		} catch (Exception e) {
			log.error("退出getTerminalUpdateVersionInfoActionForMobile方法，获取终端版本更新信息失败，可能配置文件缺失。");
			mobilePackage.setResult("error");
			mobilePackage.setContent(mch.mapToJson ());
			//返回content的JSON字符串信息
			String resultPackageJsonStr = gson.toJson(mobilePackage);
			try {
				log.info("退出getTerminalUpdateVersionInfoActionForMobile方法，返回结果"+resultPackageJsonStr+"");
				response.getWriter().write(resultPackageJsonStr);
			} catch (IOException e1) {
				log.error("退出getTerminalUpdateVersionInfoActionForMobile方法，返回结果"+resultPackageJsonStr+"失败");
				e1.printStackTrace();
			}
		}
		mch.addGroup("versionInfo",map);//返回版本信息
		mobilePackage.setResult("success");
		mobilePackage.setContent(mch.mapToJson ());
		//返回content的JSON字符串信息
		String resultPackageJsonStr = gson.toJson(mobilePackage);
		try {
			log.info("退出getTerminalUpdateVersionInfoActionForMobile方法，返回结果"+resultPackageJsonStr+"");
			response.getWriter().write(resultPackageJsonStr);
		} catch (IOException e) {
			log.error("退出getTerminalUpdateVersionInfoActionForMobile方法，返回结果"+resultPackageJsonStr+"失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @description: 取得文件大小
	 * @author：yuan.yw
	 * @return     文件大小
	 * @return String     
	 * @throws IOException 
	 * @date：Mar 1, 2013 4:16:15 PM
	 */
	public String getFileSize(String path) throws IOException{
        try{   
        	URL url = new URL(path);
        	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        	String result = String.valueOf(connection.getContentLength());
        	return result;   
      }catch(FileNotFoundException e2){   
          log.error("获取"+path+"文件大小失败，文件找不到，返回结果文件大小：0");
          e2.printStackTrace();
          return "0";
      }   

	}
}
