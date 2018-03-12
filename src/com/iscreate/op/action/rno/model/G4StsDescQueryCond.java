package com.iscreate.op.action.rno.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.iscreate.op.service.rno.tool.DateUtil;


/**
 * 查询4g话统数据查询条件
 * @author chen.c10
 *
 */
public class G4StsDescQueryCond {
	private long cityId=-1;
	private String meaBegTime;
	private String meaEndTime;
	private String cellName;
	private String beginTime;
	private long cellId;
	
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
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
	public long getCellId() {
		return cellId;
	}
	public void setCellId(long cellId) {
		this.cellId = cellId;
	}
	public String buildWhereCont() {
		String where = "";
		DateUtil dateUtil=new DateUtil();
		
		if (cityId != -1) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " AREA_ID=" + cityId;
		}
		
		if (!StringUtils.isBlank(meaBegTime)) {
			Date bd = dateUtil.parseDateArbitrary(meaBegTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " BEGINTIME>=to_date('"
						+ dateUtil.format_yyyyMMddHHmmss(bd)
						+ "','yyyy-mm-dd hh24:mi:ss')";
			}
		}
		if (!StringUtils.isBlank(meaEndTime)) {
			Date bd = dateUtil.parseDateArbitrary(meaEndTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " ENDTIME<=to_date('"
						+ dateUtil.format_yyyyMMddHHmmss(bd)
						+ "','yyyy-mm-dd hh24:mi:ss')";
			}
		}
		
		return where;
	}
	public String buildWhereQuota() {
		String where = "";
		DateUtil dateUtil=new DateUtil();
		
		if (cityId != -1) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " AREA_ID=" + cityId;
		}
		if (!StringUtils.isBlank(beginTime)) {
			Date bd = dateUtil.parseDateArbitrary(beginTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " BEGINTIME=to_date('"
						+ dateUtil.format_yyyyMMddHHmmss(bd)
						+ "','yyyy-mm-dd hh24:mi:ss')";
			}
		} else {
			if (!StringUtils.isBlank(meaBegTime)) {
				Date bd = dateUtil.parseDateArbitrary(meaBegTime);
				if (bd != null) {
					where += StringUtils.isBlank(where) ? "" : " and ";
					where += " BEGINTIME>=to_date('"
							+ dateUtil.format_yyyyMMddHHmmss(bd)
							+ "','yyyy-mm-dd hh24:mi:ss')";
				}
			}
			if (!StringUtils.isBlank(meaEndTime)) {
				Date bd = dateUtil.parseDateArbitrary(meaEndTime);
				if (bd != null) {
					where += StringUtils.isBlank(where) ? "" : " and ";
					where += " ENDTIME<=to_date('"
							+ dateUtil.format_yyyyMMddHHmmss(bd)
							+ "','yyyy-mm-dd hh24:mi:ss')";
				}
			}
		}
		if (!StringUtils.isBlank(cellName)) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " PMUSERLABEL='" + cellName+"' ";
		}
		if (cellId != -1) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " BUSINESS_CELL_ID=" + cellId;
		}
		return where;
	}
}
