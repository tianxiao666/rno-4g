package com.iscreate.op.action.rno.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;


/**
 * 查询4g路测描述查询条件
 * @author chao.xj
 *
 */
public class G4DtDescQueryCond {
	private long cityId;
	private String dataType;
	private String meaBegTime;
	private String meaEndTime;
	private String areaType;
	
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
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
	public String getAreaType() {
		return areaType;
	}
	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}
	public String buildWhereCont() {
		String where = "";
		DateUtil dateUtil=new DateUtil();
		if (!StringUtils.isBlank(dataType)) {
			if(!"ALL".equals(dataType)){
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " data_Type  like '%" + dataType + "%'";
			}
		}
		
		if (cityId != -1) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " CITY_ID=" + cityId;
		}
		
		if (!StringUtils.isBlank(meaBegTime)) {
			Date bd = dateUtil.parseDateArbitrary(meaBegTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " MEA_TIME>=to_date('"
						+ dateUtil.format_yyyyMMdd(bd)
						+ "','yyyy-mm-dd')";
			}
		}
		if (!StringUtils.isBlank(meaEndTime)) {
			Date bd = dateUtil.parseDateArbitrary(meaEndTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " MEA_TIME<=to_date('"
						+ dateUtil.format_yyyyMMdd(bd)
						+ "','yyyy-mm-dd')";
			}
		}
		
		return where;
	}
	public String buildWhereForMap() {
		String where = "";
		DateUtil dateUtil=new DateUtil();
		if (!StringUtils.isBlank(dataType)) {
			if(!"ALL".equals(dataType)){
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " data_Type  like '%" + dataType + "%'";
			}
		}
		if (!StringUtils.isBlank(areaType)) {
			if(!"ALL".equals(areaType)){
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " area_Type  like '%" + areaType + "%'";
			}
		}
		if (cityId != -1) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " CITY_ID=" + cityId;
		}
		
		if (!StringUtils.isBlank(meaBegTime)) {
			Date bd = dateUtil.parseDateArbitrary(meaBegTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " MEA_TIME=to_date('"
						+ dateUtil.format_yyyyMMdd(bd)
						+ "','yyyy-mm-dd')";
			}
		}
		
		return where;
	}
}
