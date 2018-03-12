package com.iscreate.op.action.rno.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;


/**
 * 查询爱立信FAS指标的查询条件
 * @author chao.xj
 *
 */
public class Eri2GFasQueryCond {
	private long cityId;
	private String cell;
	private String meaBegTime;//开始日期
	private String meaEndTime;//结束日期
	
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
			where += " cell  like '%" + cell + "%'";
		}
		
		if (cityId != -1) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " CITY_ID=" + cityId;
		}
		
		if (!StringUtils.isBlank(meaBegTime)) {
			Date bd = RnoHelper.parseDateArbitrary(meaBegTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " MEA_TIME>=to_date('"
						+ dateUtil.format_yyyyMMddHHmmss(bd)
						+ "','yyyy-mm-dd HH24:mi:ss')";
			}
		}
		if (!StringUtils.isBlank(meaEndTime)) {
			Date bd = RnoHelper.parseDateArbitrary(meaEndTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " MEA_TIME<=to_date('"
						+ dateUtil.format_yyyyMMdd(bd)
						+ " 23:59:59','yyyy-mm-dd HH24:mi:ss')";
			}
		}
		return where;
	}
	public String buildFieldCont(){
		String field="";
//		field="FAS_DESC_ID,MEA_TIME,CELL,FREQ_NUM,ARFCN_1_150,AVMEDIAN_1_150,AVPERCENTILE_1_150,NOOFMEAS_1_150,CITY_ID";
		field="t1.FAS_DESC_ID,t1.MEA_TIME,t1.CELL,t2.bcch,t2.tch,t1.FREQ_NUM,t1.ARFCN_1_150,t1.AVMEDIAN_1_150,t1.AVPERCENTILE_1_150,t1.NOOFMEAS_1_150,t1.CITY_ID";
		/*if(dataType.equals(Index.Rxlev.toString())){
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
		if (!StringUtils.equals(chgr, "ALL")) {
			field = "cell_name,channel_group_num,"+field;
		}else{
			field = "cell_name,"+field;
		}*/
		return field;
	}
	public String buildTableCont(){
		String table="RNO_ERI_FAS_CELL_DATA t1 left join cell t2 on t2.label=t1.cell";
		/*if(dataType.equals(Index.Rxlev.toString())){
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
		}*/
		return table;
	}
}
