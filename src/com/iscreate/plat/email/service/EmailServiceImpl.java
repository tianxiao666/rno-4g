package com.iscreate.plat.email.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.service.outlinking.OutLinkingService;
import com.iscreate.plat.email.pojo.EmailSenderInfo;
import com.iscreate.plat.email.pojo.MyAuthenticator;

public class EmailServiceImpl implements EmailService{
	
	private Log log = LogFactory.getLog(EmailServiceImpl.class);
	/**
	 * 以文本格式发送邮件   
	* @date Nov 20, 20122:31:13 PM
	* @Description: TODO 
	* @param @param emailSenderInfo待发送的邮件的信息   
	* @param @return        
	* @throws
	 */
	    public boolean sendTextMail(EmailSenderInfo emailSenderInfo) {    
	      // 判断是否需要身份认证    
	      MyAuthenticator authenticator = null;    
	      Properties pro = emailSenderInfo.getProperties();   
	      if (emailSenderInfo.isValidate()) {    
	      // 如果需要身份认证，则创建一个密码验证器    
	        authenticator = new MyAuthenticator(emailSenderInfo.getUserName(), emailSenderInfo.getPassword());    
	      }   
	      // 根据邮件会话属性和密码验证器构造一个发送邮件的session    
	      Session sendMailSession = Session.getDefaultInstance(pro,authenticator);    
	      try {    
	      // 根据session创建一个邮件消息    
	      Message mailMessage = new MimeMessage(sendMailSession);    
	      // 创建邮件发送者地址    
	      Address from = new InternetAddress(emailSenderInfo.getFromAddress());    
	      // 设置邮件消息的发送者    
	      mailMessage.setFrom(from);    
	      // 创建邮件的接收者地址，并设置到邮件消息中    
	      Address to = new InternetAddress(emailSenderInfo.getToAddress());    
	      mailMessage.setRecipient(Message.RecipientType.TO,to);    
	      // 设置邮件消息的主题    
	      mailMessage.setSubject(emailSenderInfo.getSubject());    
	      // 设置邮件消息发送的时间    
	      mailMessage.setSentDate(new Date());    
	      // 设置邮件消息的主要内容    
	      String mailContent = emailSenderInfo.getContent();  
	     
	   // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象    
	      Multipart mainPart = new MimeMultipart();  
	      String[] attachFileNames = emailSenderInfo.getAttachFileNames();
	      setMailAttachFileData(emailSenderInfo, mainPart, attachFileNames);
	   // 创建一个包含内容的MimeBodyPart    
	      BodyPart contentPart = new MimeBodyPart();
	      // 设置TEXT内容    
          contentPart.setText(mailContent);
          mainPart.addBodyPart(contentPart);
	      
	      // 将MiniMultipart对象设置为邮件内容    
	      mailMessage.setContent(mainPart);    
	      // 发送邮件    
	      Transport.send(mailMessage);    
	      return true;    
	      } catch (MessagingException ex) {    
	          ex.printStackTrace();    
	      }    
	      return false;    
	    }

	
	       
	    /**
	     * 以HTML格式发送邮件   
	    * @date Nov 20, 20122:31:35 PM
	    * @Description: TODO 
	    * @param @param emailSenderInfo 待发送的邮件信息   
	    * @param @return        
	    * @throws
	     */
	    public boolean sendHtmlMail(EmailSenderInfo emailSenderInfo){    
	      // 判断是否需要身份认证    
	      MyAuthenticator authenticator = null;   
	      Properties pro = emailSenderInfo.getProperties();   
	      //如果需要身份认证，则创建一个密码验证器     
	      if (emailSenderInfo.isValidate()) {    
	        authenticator = new MyAuthenticator(emailSenderInfo.getUserName(), emailSenderInfo.getPassword());   
	      }    
	      // 根据邮件会话属性和密码验证器构造一个发送邮件的session   
	      Session sendMailSession = Session.getDefaultInstance(pro,authenticator);    
	      try {    
	      // 根据session创建一个邮件消息    
	      Message mailMessage = new MimeMessage(sendMailSession);    
	      // 创建邮件发送者地址    
	      Address from = new InternetAddress(emailSenderInfo.getFromAddress());    
	      // 设置邮件消息的发送者    
	      mailMessage.setFrom(from);    
	      // 创建邮件的接收者地址，并设置到邮件消息中    
	      Address to = new InternetAddress(emailSenderInfo.getToAddress());    
	      // Message.RecipientType.TO属性表示接收者的类型为TO    
	      mailMessage.setRecipient(Message.RecipientType.TO,to);    
	      // 设置邮件消息的主题    
	      mailMessage.setSubject(emailSenderInfo.getSubject());    
	      // 设置邮件消息发送的时间    
	      mailMessage.setSentDate(new Date());    
	      // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象    
	      Multipart mainPart = new MimeMultipart();    
	      // 创建一个包含HTML内容的MimeBodyPart    
	      BodyPart html = new MimeBodyPart();    
	      // 设置HTML内容    
	      html.setContent(emailSenderInfo.getContent(), "text/html; charset=utf-8");    
	      mainPart.addBodyPart(html);    
	      String[] attachFileNames = emailSenderInfo.getAttachFileNames();
	      setMailAttachFileData(emailSenderInfo, mainPart, attachFileNames);
	      // 将MiniMultipart对象设置为邮件内容    
	      mailMessage.setContent(mainPart);    
	      // 发送邮件    
	      Transport.send(mailMessage);    
	      return true;    
	      } catch (MessagingException ex) {    
	          ex.printStackTrace();    
	      }    
	      return false;    
	    }   
	    
	    
	    /**
	     * 设置邮件附件
	    * @date Apr 15, 201310:42:13 AM
	    * @Description: TODO 
	    * @param @param emailSenderInfo 待发送的邮件信息
	    * @param @param mainPart 邮件内容主体
	    * @param @param attachFileNames 附件文件路径
	    * @param @throws MessagingException 异常类       
	    * @throws
	     */
	    private void setMailAttachFileData(EmailSenderInfo emailSenderInfo,
				Multipart mainPart, String[] attachFileNames)
				throws MessagingException {
			if(attachFileNames != null && attachFileNames.length > 0){
		    	  for(String attachFileName:attachFileNames){
		    		  // 创建一个包含HTML内容的MimeBodyPart    
		    		  BodyPart bodyPart = new MimeBodyPart();    
		    		  // 设置HTML内容    
		    		  bodyPart.setContent(emailSenderInfo.getContent(), "text/html; charset=utf-8");    
		    		  try {
		    			  //获取附件文件路径
		    			  String filePath = attachFileName;
		    			  //根据文件路径数据
		    			  FileDataSource fileDataSource = new FileDataSource(filePath);
		    			  //设置MimeBodyPart中DataHandler文件路径数据
		    			  bodyPart.setDataHandler(new DataHandler(fileDataSource));
		    			  //设置附件文件名，文件编码
		    			  bodyPart.setFileName(MimeUtility.encodeWord(fileDataSource.getName(), "GB2312",null));
		    			  //设置邮件主体内容
		    			  mainPart.addBodyPart(bodyPart);    
		    		  } catch (UnsupportedEncodingException e) {
		    			  // TODO Auto-generated catch block
		    			  log.debug("邮件发送添加附件失败");
		    			  e.printStackTrace();
		    		  }  
		    	  }
		      }else{
		    	  log.debug("sendTextMail接口emailSenderInfo类中没有附件");
		      }
		}    
		
	}
