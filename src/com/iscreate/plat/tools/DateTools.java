package com.iscreate.plat.tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTools 
{
	public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) 
	{  
		List<Date> lDate = new ArrayList<Date>();  
		
		lDate.add(beginDate);//把开始时间加入集合  
		
		Calendar cal = Calendar.getInstance();  
		
		//使用给定的 Date 设置此 Calendar 的时间  
		
		cal.setTime(beginDate);  
		
		boolean bContinue = true;  
		
		while (bContinue) {  
			//根据日历的规则，为给定的日历字段添加或减去指定的时间量
			cal.add(Calendar.DAY_OF_MONTH, 1);  
			// 测试此日期是否在指定日期之后  
			if (endDate.after(cal.getTime())) 
			{  
				lDate.add(cal.getTime());  
			} 
			else 
			{  
				break;  
			}  
		}  
		lDate.add(endDate);//把结束时间加入集合  
		return lDate;  
	}
}
