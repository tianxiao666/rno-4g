package com.iscreate.op.action.rno.model;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.iscreate.op.service.rno.tool.DateUtil;
/**
 * 
 * @title Hbase NCS查询条件
 * rowkey :地市编码+”_”+测量时间的毫秒表示+”_”+cell+”_”+Chgr+”_”+Bsic+”_”+Arfcn 
 * @author chao.xj
 * @date 2014-12-3下午17:25:44
 * @company 怡创科技
 * @version 1.2
 */
public class G2NcsQueryCond {

	private String cell;
	private long cityId;
	private String meaBegTime;
	private String meaEndTime;
	private String ncsDateTime;
	private String vendor;//1 厂家为爱立信 2 厂家为华为
	private DateUtil dateUtil=new DateUtil();
	
	public String getNcsDateTime() {
		return ncsDateTime;
	}
	public void setNcsDateTime(String ncsDateTime) {
		this.ncsDateTime = ncsDateTime;
	}
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public void setMeaBegTime(String meaBegTime) {
		this.meaBegTime = meaBegTime;
	}
	public void setMeaEndTime(String meaEndTime) {
		this.meaEndTime = meaEndTime;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getMeaBegTime() {
		if (StringUtils.isBlank(meaBegTime)) {
			//获取以当天为准的前三十天的日期
			Date d = new Date(); 
			Calendar calendar=Calendar.getInstance(); 
			calendar.setTime(d); 
			calendar.add(Calendar.DATE,-30); 
			Date d2 = calendar.getTime();
			return String.valueOf(d2.getTime());
		} else {
			Date d3=dateUtil.to_yyyyMMddHHmmssDate(meaBegTime);
			return String.valueOf(d3.getTime());
		}
	}
	public String getMeaEndTime() {
//		String eDate = dateUtil.format_yyyyMMddHHmmss(new Date());
		if (StringUtils.isBlank(meaEndTime)) {
			//获取以当天为准的前三十天的日期
			long eDate=new Date().getTime();
			return String.valueOf(eDate);
		} else {
			Date d3=dateUtil.to_yyyyMMddHHmmssDate(meaEndTime);
			return String.valueOf(d3.getTime());
		}
	}
	//地市编码+”_”+测量时间的毫秒表示+”_”+cell
	public String startRowKey() {
		return String.valueOf(cityId)+"_"+getMeaBegTime()+"_"+cell;
	}
	public String stopRowKey() {
		return String.valueOf(cityId)+"_"+getMeaEndTime()+"_"+cell;
	}
}
