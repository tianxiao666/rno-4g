package com.iscreate.op.service.rno.tool;

public class UnicodeUtil {

	/**
	 * 
	 * @title 将16进制数转换为汉字
	 * @param content
	 * @return
	 * @author chao.xj
	 * @date 2014-12-9下午4:56:08
	 * @company 怡创科技
	 * @version 1.2
	 */
	public static String deUnicode(String content){
		  String enUnicode=null;
		  String deUnicode=null;
		  for(int i=0;i<content.length();i++){
		      if(enUnicode==null){
		       enUnicode=String.valueOf(content.charAt(i));
		      }else{
		       enUnicode=enUnicode+content.charAt(i);
		      }
		      if(i%4==3){
		       if(enUnicode!=null){
		        if(deUnicode==null){
		         deUnicode=String.valueOf((char)Integer.valueOf(enUnicode, 16).intValue());
		        }else{
		         deUnicode=deUnicode+String.valueOf((char)Integer.valueOf(enUnicode, 16).intValue());
		        }
		       }
		       enUnicode=null;
		      }
		      
		     }
		  return deUnicode;
		 }
	/**
	 * 
	 * @title 将汉字转换为16进制数
	 * @param content
	 * @return
	 * @author chao.xj
	 * @date 2014-12-9下午4:56:23
	 * @company 怡创科技
	 * @version 1.2
	 */
		 public static String enUnicode(String content){
		  String enUnicode=null;
		  for(int i=0;i<content.length();i++){
		   if(i==0){
		       enUnicode=getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
		      }else{
		       enUnicode=enUnicode+getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
		      }
		  }
		  return enUnicode;
		 }
		 private static String getHexString(String hexString){
		      String hexStr="";
		      for(int i=hexString.length();i<4;i++){
		       if(i==hexString.length())
		        hexStr="0";
		       else
		        hexStr=hexStr+"0";
		      }
		      return hexStr+hexString;
		 }
}
