package com.iscreate.op.action.rno.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.iscreate.op.service.rno.parser.jobrunnable.Eri2GCellParserJobRunnable;
import com.iscreate.op.service.rno.parser.jobrunnable.Eri2GCellParserJobRunnable.DBFieldToLogTitle;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;


/**
 * 查询华为2g小区查询条件
 * @author chao.xj
 *
 */
public class Hw2GCellQueryCond {
	private long cityId;
	private String meaDate;
	private String bsc;
	private String cell;
	private String param;
	
	
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public String getMeaDate() {
		return meaDate;
	}
	public void setMeaDate(String meaDate) {
		this.meaDate = meaDate;
	}
	public String getBsc() {
		return bsc;
	}
	public void setBsc(String bsc) {
		this.bsc = bsc;
	}
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}

	public String buildWhereCont() {
		String where = "";
		DateUtil dateUtil=new DateUtil();
		if (!StringUtils.isBlank(bsc) && !"ALL".equals(bsc)) {
			//id逗号字符串
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " t1.bsc_id  IN (" + bsc + ")";
		}
		
		if (cityId != -1) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " CITY_ID=" + cityId;
		}
		
		if (!StringUtils.isBlank(meaDate) && !"ALL".equals(meaDate)) {
			String dateStr[]=meaDate.split(",");
			//日期逗号字符串
			Date bd;
			if(dateStr.length>1){
				for (int i = 0; i < dateStr.length; i++) {
					bd = RnoHelper.parseDateArbitrary(dateStr[i]);
					if (bd != null) {
						if(i==0){
							where += StringUtils.isBlank(where) ? "" : " and ";
							where += " (MEA_DATE=to_date('"
									+ dateUtil.format_yyyyMMdd(bd)
									+ "','yyyy-mm-dd')";
						}else{
							where += StringUtils.isBlank(where) ? "" : " or ";
							where += " MEA_DATE=to_date('"
									+ dateUtil.format_yyyyMMdd(bd)
									+ "','yyyy-mm-dd')";
						}
					}
				}
				where += ")";
			}else {
				bd = RnoHelper.parseDateArbitrary(meaDate);
				if (bd != null) {
					where += StringUtils.isBlank(where) ? "" : " and ";
					where += " MEA_DATE=to_date('"
							+ dateUtil.format_yyyyMMdd(bd)
							+ "','yyyy-mm-dd')";
				}
			}
		}
		if (!StringUtils.isBlank(cell) && !"ALL".equals(cell)) {
			//逗号字符串
			String str="";
			where += StringUtils.isBlank(where) ? "" : " and ";
			for (int i = 0; i < cell.split(",").length; i++) {
				str+="'"+cell.split(",")[i]+"',";
			}
			str=str.substring(0, str.length()-1);
			where += " CELL_NAME  IN (" + str + ")";
		}
		
		return where;
	}
	public String buildSelectCont() {
		String select="select ";
		//select MEA_DATE,MSC,t2.ENGNAME BSC,CELL from rno_2g_eri_ncell_param t1  left join(rno_bsc t2) on (t1.bsc=t2.bsc_id) 
		String field = "  TO_CHAR(MEA_DATE,'YYYY-MM-DD') MEA_DATE,t.ENGNAME BSC,CELL_NAME,LAC,CI,";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			//逗号字符串
			field += param;
		}else{
			field+="FREQ_TYPE            ,BCCH                 ,BSIC                 ,TCH                  ,LON                  ,LAT                  ,BEARING              ,DOWNTILT             ,INDOOR_COVER_CELL    ,HYBRID_COVER_CELL    ,HALF_POWER_ANGLE     ,ANT_HEIGH            ,SITE                 ,SITE_TYPE            ,ANT_TYPE             ,COVERAREA            ,IMPORTANCEGRADE      ,ANT_MODEL            ,MECHANICALDOWNTILT   ,ELECTRICALDOWNTILT   ,TOWER_TYPE           ,HEIGHT_ABOVE_SEA     ,ANT_POLARIZATION_TYPE";
		}
		String from=", row_number() OVER(ORDER BY null) AS rn from RNO_2G_HW_CELL t1  left join(rno_bsc t) on (t1.bsc_id=t.bsc_id) ";
		select=select+field+from;
		return select;
	}
	public String buildFromCont() {
		String from="  RNO_2G_HW_CELL t1  left join(rno_bsc t) on (t1.bsc_id=t.bsc_id) ";
		return from;
	}
	public String buildFieldOutCont() {
		//select MEA_DATE,MSC,t2.ENGNAME BSC,CELL from rno_2g_eri_ncell_param t1  left join(rno_bsc t2) on (t1.bsc=t2.bsc_id) 
		String field_out = " TO_CHAR(MEA_DATE,'YYYY-MM-DD') MEA_DATE,BSC,CELL_NAME,LAC,CI,";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			//逗号字符串
			field_out += param;
		}else{
			field_out+="FREQ_TYPE            ,BCCH                 ,BSIC                 ,TCH                  ,LON                  ,LAT                  ,BEARING              ,DOWNTILT             ,INDOOR_COVER_CELL    ,HYBRID_COVER_CELL    ,HALF_POWER_ANGLE     ,ANT_HEIGH            ,SITE                 ,SITE_TYPE            ,ANT_TYPE             ,COVERAREA            ,IMPORTANCEGRADE      ,ANT_MODEL            ,MECHANICALDOWNTILT   ,ELECTRICALDOWNTILT   ,TOWER_TYPE           ,HEIGHT_ABOVE_SEA     ,ANT_POLARIZATION_TYPE";
		}
		return field_out;
	}
	public String buildFieldInnerCont() {
		//select MEA_DATE,MSC,t2.ENGNAME BSC,CELL from rno_2g_eri_ncell_param t1  left join(rno_bsc t2) on (t1.bsc=t2.bsc_id) 
		String field_inner = "  MEA_DATE,t.ENGNAME BSC,CELL_NAME,LAC,CI,";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			//逗号字符串
			field_inner += param;
		}else{
			field_inner+="FREQ_TYPE            ,BCCH                 ,BSIC                 ,TCH                  ,LON                  ,LAT                  ,BEARING              ,DOWNTILT             ,INDOOR_COVER_CELL    ,HYBRID_COVER_CELL    ,HALF_POWER_ANGLE     ,ANT_HEIGH            ,SITE                 ,SITE_TYPE            ,ANT_TYPE             ,COVERAREA            ,IMPORTANCEGRADE      ,ANT_MODEL            ,MECHANICALDOWNTILT   ,ELECTRICALDOWNTILT   ,TOWER_TYPE           ,HEIGHT_ABOVE_SEA     ,ANT_POLARIZATION_TYPE";
		}
		return field_inner;
	}
	public String buildFieldExportCont() {
		String field_out = " MEA_DATE,BSC,CELL_NAME,LAC,CI,";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			//逗号字符串
			field_out += param;
		}else{
			field_out+="FREQ_TYPE            ,BCCH                 ,BSIC                 ,TCH                  ,LON                  ,LAT                  ,BEARING              ,DOWNTILT             ,INDOOR_COVER_CELL    ,HYBRID_COVER_CELL    ,HALF_POWER_ANGLE     ,ANT_HEIGH            ,SITE                 ,SITE_TYPE            ,ANT_TYPE             ,COVERAREA            ,IMPORTANCEGRADE      ,ANT_MODEL            ,MECHANICALDOWNTILT   ,ELECTRICALDOWNTILT   ,TOWER_TYPE           ,HEIGHT_ABOVE_SEA     ,ANT_POLARIZATION_TYPE";
		}
		return field_out;
	}
	public static void main(String[] args) {
		Hw2GCellQueryCond cond=new Hw2GCellQueryCond();
		cond.setBsc("83147,83148,83149,83150,83151,83152,83153,83154,83155,83156,83157");
		cond.setMeaDate("2014-10-15");
		cond.setCell("ALL");
		cond.setCityId(89);
		cond.setParam("FREQ_TYPE            ,BCCH");
		
		System.out.println(cond.buildSelectCont()+"    where   "+cond.buildWhereCont());
	}
}
