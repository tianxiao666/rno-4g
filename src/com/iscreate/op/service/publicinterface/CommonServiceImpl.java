package com.iscreate.op.service.publicinterface;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.iscreate.op.service.outlinking.OutLinkingService;
import com.iscreate.plat.email.pojo.EmailSenderInfo;
import com.iscreate.plat.email.service.EmailServiceImpl;

public class CommonServiceImpl implements CommonService {
	/**
	 * 移动专用发送邮件
	 * @param map
	 */
	public void sendCMCCEmail(Map map){
		OutLinkingService outLinkingService = new OutLinkingService();
		Map mailInfoMap = outLinkingService.getEmailSettingInfo();
		if(map.get("cellphone")!=null && !"".equals(map.get("cellphone"))){
			//发送邮件
			 EmailSenderInfo mailInfo = new EmailSenderInfo();
			 mailInfo.setMailServerHost(mailInfoMap.get("mailServerHost").toString());    
			 mailInfo.setMailServerPort(mailInfoMap.get("mailServerPort").toString());    
			 mailInfo.setValidate(Boolean.parseBoolean(mailInfoMap.get("validate").toString()));    
			 mailInfo.setUserName(mailInfoMap.get("userName").toString());    
			 mailInfo.setPassword(mailInfoMap.get("password").toString());//您的邮箱密码    
			 mailInfo.setFromAddress(mailInfoMap.get("fromAddress").toString());    
			 mailInfo.setContent(new Date().toString());    
			    //这个类主要来发送邮件   
			 EmailServiceImpl ems = new EmailServiceImpl(); 
			 mailInfo.setToAddress(map.get("cellphone").toString().trim()+"@139.com");
			 if(map.get("title")!=null){
				 mailInfo.setSubject(map.get("title").toString());
			 }
			 if(map.get("content")!=null){
				 mailInfo.setContent(map.get("content").toString()); 
			 }
			 boolean sendTextMail = ems.sendTextMail(mailInfo);//发送html格式   
		}
	}
	
}
