package com.iscreate.op.service.rno.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateUtil {
	private static Log log=LogFactory.getLog(DateUtil.class);
	private  SimpleDateFormat sdf1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private  SimpleDateFormat sdf12 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private  SimpleDateFormat sdf2 = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	private  SimpleDateFormat sdf6 = new SimpleDateFormat(
			"MM-dd-yyyy HH:mm:ss");
	private  SimpleDateFormat sdf9 = new SimpleDateFormat(
			"MM-dd-yyyy HH:mm");
	private  SimpleDateFormat sdf7 = new SimpleDateFormat(
			"MM-dd-yyyy");
	
	private  SimpleDateFormat sdf3 = new SimpleDateFormat(
			"yyyy/MM/dd");
	private  SimpleDateFormat sdf8 = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");
	private  SimpleDateFormat sdf10 = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm");
	
	private  SimpleDateFormat sdf4 = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss");
	private  SimpleDateFormat sdf11 = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm");
	private  SimpleDateFormat sdf5 = new SimpleDateFormat(
			"MM/dd/yyyy");
	
	private  SimpleDateFormat sdf20=new SimpleDateFormat("yyyy-MM-dd HH");
	private SimpleDateFormat sdf21 = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.CHINA);
	public   Date  parseDateArbitrary(String str) {
		
		Date date = null;
		if(StringUtils.isBlank(str)){
			return null;
		}
		if(StringUtils.contains(str, "-")){
			String[] sps=str.split("-");
			if(sps[0].length()>2){
				//yyyy-mm-dd 或yyyy-mm-dd hh:mm:ss
				if(StringUtils.contains(str, ":")){
					try {
						date=sdf1.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf1.parse(str) fail:str="+str);
						//可能是 或yyyy-mm-dd hh:mm
						try {
							date=sdf12.parse(str);
						} catch (ParseException e1) {
							e1.printStackTrace();
							log.error("sdf12.parse(str) fail:str="+str);
						}
					}
				}else{
					try {
						date=sdf2.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf2.parse(str) fail:str="+str);
					}
				}
			}else{
				//mm-dd-yyyy 或mm-dd-yyyy hh:mm:ss
				if(StringUtils.contains(str, ":")){
					try {
						date=sdf6.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf6.parse(str) fail:str="+str);
						//可能是mm-dd-yyyy hh:mm
						try {
							date=sdf9.parse(str);
						} catch (ParseException e1) {
							e1.printStackTrace();
							log.error("sdf9.parse(str) fail:str="+str);
						}
					}
				}else{
					try {
						date=sdf7.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf7.parse(str) fail:str="+str);
					}
				}
			}
		}else if(StringUtils.contains(str, "/")){
			String[] sps=str.split("/");
			if(sps[0].length()>2){
				//yyyy/mm/dd 或yyyy/mm/dd hh:mm:ss
				if(StringUtils.contains(str, ":")){
					try {
						date=sdf8.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf8.parse(str) fail:str="+str);
						//可能是yyyy/mm/dd hh:mm、
						try {
							date=sdf10.parse(str);
						} catch (ParseException e1) {
							e1.printStackTrace();
							log.error("sdf10.parse(str) fail:str="+str);
						}
					}
				}else{
					try {
						date=sdf3.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf3.parse(str) fail:str="+str);
					}
				}
			}else{
				//mm/dd/yyyy 或mm/dd/yyyy hh:mm:ss
				if(StringUtils.contains(str, ":")){
					try {
						date=sdf4.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf4.parse(str) fail:str="+str);
						//可能是mm/dd/yyyy hh:mm
						try {
							date=sdf11.parse(str);
						} catch (ParseException e1) {
							e1.printStackTrace();
							log.error("sdf11.parse(str) fail:str="+str);
						}
					}
				}else{
					try {
						date=sdf5.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
						log.error("sdf5.parse(str) fail:str="+str);
					}
				}
			}
		}
		
		
		
		return date;
	}
	
	public  Date to_yyyyMMddHHmmssDate(String dt){
		try {
			return sdf1.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	public  Date to_yyyyMMddDate(String dt){
		try {
			return sdf2.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String format_yyyyMMddHHmmss(Date date){
		return sdf1.format(date);
	}
	
	public String format_yyyyMMdd(Date date) {
		return sdf2.format(date);
	}
	
	public String format_yyyyMMddHH(Date date) {
		return sdf20.format(date);
	}
	public  boolean isValidDate(String s)
    {
        try
        {
        	sdf2.parse(s);
             return true;
         }
        catch (Exception e)
        {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
        	e.printStackTrace();
            return false;
        }
    }
	public boolean isSameDay(Date day1, Date day2) {
	    String ds1 = sdf2.format(day1);
	    String ds2 = sdf2.format(day2);
	    if (ds1.equals(ds2)) {
	        return true;
	    } else {
	        return false;
	    }
	}
	/**
	 * 
	 * @title 
	 * @param str
	 * @param priority
	 * DateFmt 
	 * SDF1("yyyy-MM-dd HH:mm:ss", 0), SDF2("yyyy-MM-dd", 1), SDF3(
				"yyyy/MM/dd", 2), SDF4("MM/dd/yyyy HH:mm:ss",3), SDF5(
				"MM/dd/yyyy", 4), SDF6("MM-dd-yyyy HH:mm:ss", 5), SDF7(
				"MM-dd-yyyy", 6), SDF8("yyyy/MM/dd HH:mm:ss",7), SDF9(
				"MM-dd-yyyy HH:mm", 8), SDF10("yyyy/MM/dd HH:mm", 9), SDF11(
				"MM/dd/yyyy HH:mm", 10), SDF12("yyyy-MM-dd HH:mm", 11), SDF20("yyyy-MM-dd HH", 12);
	 * @return
	 * @author chao.xj
	 * @date 2015-3-20下午3:43:02
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public  Date priorityAssignedParseDate(String str,int priority) {
		Date date = null;
		try {
			SimpleDateFormat []sdfs={
					sdf1,sdf2,sdf3,sdf4,sdf5,sdf6,sdf7,sdf8,sdf9,sdf10,sdf11,sdf12,sdf20
			};
			date=sdfs[priority].parse(str);
			if(date.getTime()<0){
				throw new Exception("时间不能为负值");
			}
		} catch (Exception e) {
			// TODO: handle exception
			date=parseDateArbitrary(str);
		}
		return date;
	}
	public  Date to_yyMMddHHmmssDate(String dt){
		try {
			return sdf21.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 
	 * @author chao.xj
	 *SDF1("yyyy-MM-dd HH:mm:ss", 0), SDF2("yyyy-MM-dd", 1), SDF3(
				"yyyy/MM/dd", 2), SDF4("MM/dd/yyyy HH:mm:ss",3), SDF5(
				"MM/dd/yyyy", 4), SDF6("MM-dd-yyyy HH:mm:ss", 5), SDF7(
				"MM-dd-yyyy", 6), SDF8("yyyy/MM/dd HH:mm:ss",7), SDF9(
				"MM-dd-yyyy HH:mm", 8), SDF10("yyyy/MM/dd HH:mm", 9), SDF11(
				"MM/dd/yyyy HH:mm", 10), SDF12("yyyy-MM-dd HH:mm", 11), SDF20("yyyy-MM-dd HH", 12);
	 */
	public enum DateFmt {
		SDF1("yyyy-MM-dd HH:mm:ss", 0), SDF2("yyyy-MM-dd", 1), SDF3(
				"yyyy/MM/dd", 2), SDF4("MM/dd/yyyy HH:mm:ss",3), SDF5(
				"MM/dd/yyyy", 4), SDF6("MM-dd-yyyy HH:mm:ss", 5), SDF7(
				"MM-dd-yyyy", 6), SDF8("yyyy/MM/dd HH:mm:ss",7), SDF9(
				"MM-dd-yyyy HH:mm", 8), SDF10("yyyy/MM/dd HH:mm", 9), SDF11(
				"MM/dd/yyyy HH:mm", 10), SDF12("yyyy-MM-dd HH:mm", 11), SDF20("yyyy-MM-dd HH", 12);
		// 成员变量
        private String name;
        private int index;

        // 构造方法
        private DateFmt(String name, int index) {
            this.name = name;
            this.index = index;
        }
        public int getIndex() {
			return this.index;
		}
    }
	public static void main(String[] args) {
		DateUtil dateUtil=new DateUtil();
		String str="2015/2/8  20:30";
//		System.out.println(dateUtil.priorityAssignedParseDate(str, DateFmt.SDF11.getIndex()).getTime());
	}
}
