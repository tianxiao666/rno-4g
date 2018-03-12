package com.iscreate.plat.networkresource.common.tool;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public final class GetChineseFirstLetter {

	    private final static int[] posValue =   
	            {   
	                    1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472,   
	                    3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5590  
	            };   
	    private final static String[] firstLetter =   
	            {   
	                    "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "l", "m", "n", "o", "p",   
	                    "q", "r", "s", "t", "w", "x", "y", "z"  
	            };   
	    private static final Log log = LogFactory.getLog(GetChineseFirstLetter.class);

	        /**  
	         * 取得给定汉字的首字母,即声母  
	         *  
	         * @param chinese 给定的汉字  
	         * @return 给定汉字的声母  
	         */  
	        public static String getFirstLetter(String chinese) {   
	          log.info("进入getFirstLetter(String chinese)，chinese="+chinese+"，取得给定汉字的首字母,即声母  ");
	          try{
	        	  if (chinese == null || chinese.trim().length() == 0) {   
		                return "";   
		            }else if(chinese.indexOf("(")==0){
		            	chinese = chinese.substring(1,chinese.length()-1);
		            	
		            }  
						chinese =new String(chinese.getBytes("GBK"),"ISO8859-1");
		            if (chinese.length() > 1){ //判断是不是汉字   
		                int li_SectorCode = (int) chinese.charAt(0); //汉字区码   
		                int li_PositionCode = (int) chinese.charAt(1); //汉字位码   
		                li_SectorCode = li_SectorCode - 160;   
		                li_PositionCode = li_PositionCode - 160;   
		                int li_SecPosCode = li_SectorCode * 100 + li_PositionCode; //汉字区位码   
		                if (li_SecPosCode > 1600 && li_SecPosCode < 5590) {   
		                    for (int i = 0; i < 23; i++) {   
		                        if (li_SecPosCode >= posValue[i] &&   
		                                li_SecPosCode < posValue[i + 1]) {   
		                            chinese = firstLetter[i].toUpperCase();   
		                            break;   
		                        }   
		                    }   
		                } else {  //非汉字字符,如图形符号或ASCII码   
		                	chinese = new String(chinese.getBytes("ISO8859-1"),"GBK");   
		                    chinese = chinese.substring(0, 1).toUpperCase();   
		                }   
		            }   
		            log.info("退出getFirstLetter(String chinese)，返回结果"+chinese);
		            return chinese;
	          }catch(UnsupportedEncodingException e){
	        	  log.error("退出getFirstLetter(String chinese)，发生UnsupportedEncodingException异常，返回空值");
	        	  e.printStackTrace();
	        	  return "";
	          }    
	        }   
	       


}
