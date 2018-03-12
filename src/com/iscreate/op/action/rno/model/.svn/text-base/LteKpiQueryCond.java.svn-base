package com.iscreate.op.action.rno.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;


/**
 * 查询lte MR指标的查询条件
 * @author chao.xj
 *
 */
public class LteKpiQueryCond {
	private long cityId;
	private String cell;
	private String meaBegTime;//开始日期
	private String meaEndTime;//结束日期
	private String dataType;//数据类型  根据数据类型判断归属表 	判断属性
	private String chartType;//图表类型  （单个值、累积值） 判断累积还是单个值	判断属性
	private String disMode;//展示方式	后台无意义
	
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
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
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
		if (!StringUtils.isBlank(cell)) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " cell_name  like '%" + cell + "%'";
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
						+ dateUtil.format_yyyyMMddHHmmss(bd)
						+ "','yyyy-mm-dd HH24:mi:ss')";
			}
		}
		if (!StringUtils.isBlank(meaEndTime)) {
			Date bd = RnoHelper.parseDateArbitrary(meaEndTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " MEA_DATE<=to_date('"
						+ dateUtil.format_yyyyMMdd(bd)
						+ " 23:59:59','yyyy-mm-dd HH24:mi:ss')";
			}
		}
		return where;
	}
	public String buildFieldCont(){
		String field="";
		if(dataType.equals(Index.Rxlev.toString())){
			field=Index.Rxlev_UL.str+","+Index.Rxlev_DL.str;
		}else if(dataType.equals(Index.RxQual.toString())){
			field=Index.RxQual_UL.str+","+Index.RxQual_DL.str;
		}else if(dataType.equals(Index.POWER.toString())){
			field=Index.POWER_MS.str+","+Index.POWER_BTS.str;
		}else if(dataType.equals(Index.PATHLOSS.toString())){
			field=Index.PATHLOSS_UL.str+","+Index.PATHLOSS_DL.str;
		}else if(dataType.equals(Index.PLDIFF.toString())){
			field=Index.PLDIFF_.str;
		}else if(dataType.equals(Index.TA.toString())){
			field=Index.TA_.str;
		}
		return field;
	}
	public String buildTableCont(){
		String table="";
		if(dataType.equals(Index.Rxlev.toString())){
			table=Index.Rxlev.str;
		}else if(dataType.equals(Index.RxQual.toString())){
			table=Index.RxQual.str;
		}else if(dataType.equals(Index.POWER.toString())){
			table=Index.POWER.str;
		}else if(dataType.equals(Index.PATHLOSS.toString())){
			table=Index.PATHLOSS.str;
		}else if(dataType.equals(Index.PLDIFF.toString())){
			table=Index.PLDIFF.str;
		}else if(dataType.equals(Index.TA.toString())){
			table=Index.TA.str;
		}
		return table;
	}
}
