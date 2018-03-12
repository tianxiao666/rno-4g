package com.iscreate.op.action.rno.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;
/**
 * 查询华为2g小区描述查询条件
 * @author chao.xj
 *
 */
public class Hw2GCellDescQueryCond {

	private long cityId;
	private String dataType;
	private String meaBegTime;
	private String meaEndTime;
	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
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
		if (!StringUtils.isBlank(dataType)) {
			if(!"ALL".equals(dataType)){
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " DATA_TYPE  like '%" + dataType + "%'";
			}
		}
		
		if (cityId != -1) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " CITY_ID=" + cityId;
		}
		
		if (!StringUtils.isBlank(meaBegTime)) {
			Date bd = RnoHelper.parseDateArbitrary(meaBegTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " MEA_DATE>=to_date('"
						+ dateUtil.format_yyyyMMdd(bd)
						+ "','yyyy-mm-dd HH24:mi:ss')";
			}
		}
		if (!StringUtils.isBlank(meaEndTime)) {
			Date bd = RnoHelper.parseDateArbitrary(meaEndTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " MEA_DATE<=to_date('"
						+ dateUtil.format_yyyyMMdd(bd)
						+ "','yyyy-mm-dd HH24:mi:ss')";
			}
		}
		
		return where;
	}
}
