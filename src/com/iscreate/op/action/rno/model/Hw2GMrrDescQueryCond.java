package com.iscreate.op.action.rno.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;


/**
 * 查询华为mrr描述新的查询条件
 * @author chao.xj
 *
 */
public class Hw2GMrrDescQueryCond {
	private long cityId;
	private String bsc;
	private String meaBegTime;
	private String meaEndTime;
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public String getBsc() {
		return bsc;
	}
	public void setBsc(String bsc) {
		this.bsc = bsc;
	}
	public String getMeaBegTime() {
		return meaBegTime;
	}
	public void setMeaBegTime(String meaBegTime) {
		this.meaBegTime = meaBegTime;
	}
	public String getMeaEndTime() {
		return meaEndTime;
	}
	public void setMeaEndTime(String meaEndTime) {
		this.meaEndTime = meaEndTime;
	}
	
	public String buildWhereCont() {
		String where = "";
		DateUtil dateUtil=new DateUtil();
		if (!StringUtils.isBlank(bsc)) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " bsc  like '%" + bsc + "%'";
		}
		
		if (cityId != -1) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " CITY_ID=" + cityId;
		}
		
		if (!StringUtils.isBlank(meaBegTime)) {
			Date bd = RnoHelper.parseDateArbitrary(meaBegTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " MEA_date>=to_date('"
						+ dateUtil.format_yyyyMMddHHmmss(bd)
						+ "','yyyy-MM-dd HH24:mi:ss')";
			}
		}
		if (!StringUtils.isBlank(meaEndTime)) {
			Date bd = RnoHelper.parseDateArbitrary(meaEndTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " MEA_date<=to_date('"
						+ dateUtil.format_yyyyMMddHHmmss(bd)
						+ "','yyyy-MM-dd HH24:mi:ss')";
			}
		}
		
		return where;
	}

}
