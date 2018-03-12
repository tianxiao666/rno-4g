package com.iscreate.plat.email.service;

import com.iscreate.plat.email.pojo.EmailSenderInfo;

public interface EmailService {
	/**
	 * 以文本格式发送邮件   
	* @date Nov 20, 20122:31:13 PM
	* @Description: TODO 
	* @param @param emailSenderInfo待发送的邮件的信息   
	* @param @return        
	* @throws
	 */
	    public boolean sendTextMail(EmailSenderInfo emailSenderInfo) ;
	    
	    /**
	     * 以HTML格式发送邮件   
	    * @date Nov 20, 20122:31:35 PM
	    * @Description: TODO 
	    * @param @param emailSenderInfo 待发送的邮件信息   
	    * @param @return        
	    * @throws
	     */
	    public boolean sendHtmlMail(EmailSenderInfo emailSenderInfo);
}
