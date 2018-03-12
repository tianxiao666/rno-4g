package com.iscreate.op.service.outlinking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.iscreate.op.service.publicinterface.SystemConfigService;
import com.iscreate.plat.tools.xmlhelper.XmlServiceImpl;

public class OutLinkingService {
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * 获取配置文件中所有的url链接
	 * @return
	 */
	public Map<String,Map<String,String>> getAllUrl(){
		log.info("进入getAllUrl方法");
		Map<String,Map<String,String>> mapUrl = null;
		try {
			String filePath = OutLinkingService.class.getClassLoader().getResource("/").getPath();
			filePath += "sysconfig.xml";
			SAXReader reader = new SAXReader();
			Document doc;
			doc = reader.read(filePath);
			List<Element> selectNodes = doc.selectNodes("SystemConfig/outlinking");
			List<Element> enviromentElementList = selectNodes.get(0).elements();
			if(enviromentElementList!=null && enviromentElementList.size()>0){
				mapUrl = new HashMap<String, Map<String,String>>();
				for (Element enviromentElement : enviromentElementList) {
					List<Element> sysElementList = enviromentElement.elements();
					if(sysElementList!=null && sysElementList.size()>0){
						Map<String,String> map = new HashMap<String, String>();
						for (Element sysElement : sysElementList) {
							map.put(sysElement.getName(), sysElement.elementText("url"));
						}
						mapUrl.put(enviromentElement.getName(), map);
					}
				}
			}
		} catch (Exception e) {
			log.error("获取url链接失败");
			return null;
		}
		log.info("执行getAllUrl方法成功，实现了”获取配置文件中所有的url链接“的功能");
		log.info("退出getAllUrl方法,返回Map<String,Map<String,String>>");
		return mapUrl;
	}
	
	/**
	 * 获取环境代码
	 * */
	public String getEnviromentCode() {
		log.info("进入getEnviromentCode方法");
		String code = null;
		XmlServiceImpl xml = new XmlServiceImpl();
		try {
			String note = "SystemConfig/Enviroment";
			String singleElementValue = xml.getSingleElementValue("sysconfig.xml", note);
			code = singleElementValue;
		} catch (Exception e) {
			log.error("获取环境代码失败");
			return null;
		}
		log.info("执行getEnviromentCode方法成功，实现了”获取环境代码“的功能");
		log.info("退出getEnviromentCode方法,返回String为："+code);
		return code;
	}

	/**
	 * 获取邮箱配置
	 * @return
	 */
	public Map<String,String> getEmailSettingInfo(){
		log.info("进入getEmailSettingInfo方法");
		Map<String,String> map = new HashMap<String, String>();
		try {
			String filePath = OutLinkingService.class.getClassLoader().getResource("/").getPath();
			filePath += "sysconfig.xml";
			SAXReader reader = new SAXReader();
			Document doc;
			doc = reader.read(filePath);
			List<Element> selectNodes = doc.selectNodes("SystemConfig/EmailSetting");
			List<Element> enviromentElementList = selectNodes.get(0).elements();
			if(enviromentElementList!=null && enviromentElementList.size()>0){
				for (Element enviromentElement : enviromentElementList) {
					map.put(enviromentElement.getName(), enviromentElement.getText());
				}
			}
		} catch (Exception e) {
			log.error("获取邮箱配置失败");
			return null;
		}
		log.info("执行getEmailSettingInfo方法成功，实现了”获取邮箱配置“的功能");
		log.info("退出getEmailSettingInfo方法,返回Map<String,String>");
		return map;
	}
	
	/**
	 * 根据 项目名 获取链接
	 * @param proj (项目名)
	 * @return
	 */
	public String getUrlByProjService(String proj){
		log.info("进入getUrlByProjService方法");
		log.info("参数proj="+proj);
		String url = this.getUrlByEnviromentAndProjAndActionNameAndValuesService(SystemConfigService.getCode(), proj, null, null);
		log.info("执行getUrlByProjService方法成功，实现了”根据 项目名 获取链接“的功能");
		log.info("退出getUrlByProjService方法,返回String为："+url);
		return url;
	}
	
	/**
	 * 根据 项目名、actionName 获取链接
	 * @param proj (项目名)
	 * @param actionName (action名)
	 * @return
	 */
	public String getUrlByProjAndActionNameService(String proj,String actionName){
		log.info("进入getUrlByProjService方法");
		log.info("参数proj="+proj+",actionName="+actionName);
		String url = this.getUrlByEnviromentAndProjAndActionNameAndValuesService(SystemConfigService.getCode(), proj, actionName, null);
		log.info("执行getUrlByProjService方法成功，实现了”根据 项目名、actionName 获取链接“的功能");
		log.info("退出getUrlByProjService方法,返回String为："+url);
		return url;
	}
	
	/**
	 * 根据 项目名、actionName、参数 获取链接
	 * @param proj (项目名)
	 * @param actionName (action名)
	 * @param values (参数)
	 * @return
	 */
	public String getUrlByProjAndActionNameAndValuesService(String proj,String actionName,Map<String,String> values){
		log.info("进入getUrlByProjAndActionNameAndValuesService方法");
		log.info("参数proj="+proj+",actionName="+actionName+",values="+values);
		String url = this.getUrlByEnviromentAndProjAndActionNameAndValuesService(SystemConfigService.getCode(), proj, actionName, values);
		log.info("执行getUrlByProjAndActionNameAndValuesService方法成功，实现了”根据 项目名、actionName、参数 获取链接“的功能");
		log.info("退出getUrlByProjAndActionNameAndValuesService方法,返回String为："+url);
		return url;
	}
	
	/**
	 * 根据环境代码、项目名 获取链接
	 * @param enviroment (环境代码)
	 * @param proj (项目名)
	 * @return
	 */
	public String getUrlByEnviromentAndProjService(String enviroment,String proj){
		log.info("进入getUrlByEnviromentAndProjService方法");
		log.info("参数proj="+proj+",enviroment="+enviroment);
		String url = this.getUrlByEnviromentAndProjAndActionNameAndValuesService(enviroment, proj, null, null);
		log.info("执行getUrlByEnviromentAndProjService方法成功，实现了”根据环境代码、项目名 获取链接“的功能");
		log.info("退出getUrlByEnviromentAndProjService方法,返回String为："+url);
		return url;
	}
	
	/**
	 * 根据环境代码、项目名、actionName 获取链接
	 * @param enviroment (环境代码)
	 * @param proj (项目名)
	 * @param actionName (action名)
	 * @return
	 */
	public String getUrlByEnviromentAndProjAndActionNameService(String enviroment,String proj,String actionName){
		log.info("进入getUrlByEnviromentAndProjAndActionNameService方法");
		log.info("参数proj="+proj+",enviroment="+enviroment+",actionName="+actionName);
		String url = this.getUrlByEnviromentAndProjAndActionNameAndValuesService(enviroment, proj, actionName, null);
		log.info("执行getUrlByEnviromentAndProjAndActionNameService方法成功，实现了”根据环境代码、项目名、actionName 获取链接“的功能");
		log.info("退出getUrlByEnviromentAndProjAndActionNameService方法,返回String为："+url);
		return url;
	}
	
	/**
	 * 根据环境代码、项目名、actionName、参数 获取链接
	 * @param enviroment (环境代码)
	 * @param proj (项目名)
	 * @param actionName (action名)
	 * @param values (参数)
	 * @return
	 */
	public String getUrlByEnviromentAndProjAndActionNameAndValuesService(String enviroment,String proj,String actionName,Map<String,String> values){
		log.info("进入getUrlByEnviromentAndProjAndActionNameAndValuesService方法");
		log.info("参数proj="+proj+",enviroment="+enviroment+",actionName="+actionName+",values="+values);
		String code = null;
		Map<String, Map<String, String>> mapUrl = SystemConfigService.getMapUrl();
		if(mapUrl.containsKey(enviroment) && mapUrl.get(enviroment).containsKey(proj)){
			code = mapUrl.get(enviroment).get(proj);
			if(actionName != null && !"".equals(actionName)){
				code += actionName;
				if(values != null && values.size() > 0){
					code += "?";
					for (String key : values.keySet()) {
						code += key + "=" + values.get(key) + "&";
					}
					code = code.substring(0,code.length()-1);
				}
			}
		}
		log.info("执行getUrlByEnviromentAndProjAndActionNameAndValuesService方法成功，实现了”根据环境代码、项目名、actionName、参数 获取链接“的功能");
		log.info("退出getUrlByEnviromentAndProjAndActionNameAndValuesService方法,返回String为："+code);
		return code;
	}
}
