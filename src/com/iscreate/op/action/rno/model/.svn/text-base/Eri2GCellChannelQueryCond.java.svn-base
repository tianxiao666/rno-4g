package com.iscreate.op.action.rno.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;


/**
 * 查询爱立信2g小区信道查询条件
 * @author chao.xj
 *
 */
public class Eri2GCellChannelQueryCond {
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
			where += " bsc  IN (" + bsc + ")";
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
			where += " cell  IN (" + str + ")";
		}
		
		return where;
	}
	public String buildSelectCont() {
		String select="select ";
		//select MEA_DATE,MSC,t2.ENGNAME BSC,CELL from rno_2g_eri_cell_ch_group t1  left join(rno_bsc t2) on (t1.bsc=t2.bsc_id) 
		String field = "TO_CHAR(MEA_DATE,'YYYY-MM-DD') MEA_DATE,MSC,t.ENGNAME BSC,CELL,CH_GROUP,CHGR_STATE,CHGR_TG,";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			//逗号字符串
			field += param;
		}else{
			field+="BAND             ,BCCD             ,CBCH             ,CCCH             ,DCHNO_32         ,EACPREF          ,ETCHTN_8         ,EXCHGR           ,HOP              ,HOPTYPE          ,HSN              ,MAIO_16          ,NUMREQBPC        ,NUMREQCS3CS4BPC  ,NUMREQE2ABPC     ,NUMREQEGPRSBPC   ,ODPDCHLIMIT      ,SAS              ,SCTYPE           ,SDCCH            ,TN_8             ,TN7BCCH          ,TNBCCH           ,TSC              ,USERDATA         ,CITY_ID";
		}
		String from=", row_number() OVER(ORDER BY null) AS rn from rno_2g_eri_cell_ch_group t1  left join(rno_bsc t) on (t1.bsc=t.bsc_id) ";
		select=select+field+from;
		return select;
	}
	public String buildFromCont() {
		
		String from="  rno_2g_eri_cell_ch_group t1  left join(rno_bsc t) on (t1.bsc=t.bsc_id) ";
		return from;
	}
	public String buildFieldOutCont() {
		//select MEA_DATE,MSC,t2.ENGNAME BSC,CELL from rno_2g_eri_ncell_param t1  left join(rno_bsc t2) on (t1.bsc=t2.bsc_id) 
		String field_out = " TO_CHAR(MEA_DATE,'YYYY-MM-DD') MEA_DATE,MSC,BSC,CELL,CH_GROUP,CHGR_STATE,CHGR_TG,";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			//逗号字符串
			field_out += param;
		}else{
			field_out+="BAND             ,BCCD             ,CBCH             ,CCCH             ,DCHNO_32         ,EACPREF          ,ETCHTN_8         ,EXCHGR           ,HOP              ,HOPTYPE          ,HSN              ,MAIO_16          ,NUMREQBPC        ,NUMREQCS3CS4BPC  ,NUMREQE2ABPC     ,NUMREQEGPRSBPC   ,ODPDCHLIMIT      ,SAS              ,SCTYPE           ,SDCCH            ,TN_8             ,TN7BCCH          ,TNBCCH           ,TSC              ,USERDATA         ,CITY_ID";
		}
		return field_out;
	}
	public String buildFieldInnerCont() {
		//select MEA_DATE,MSC,t2.ENGNAME BSC,CELL from rno_2g_eri_ncell_param t1  left join(rno_bsc t2) on (t1.bsc=t2.bsc_id) 
		String field_inner = "   MEA_DATE,MSC,t.ENGNAME BSC,CELL,CH_GROUP,CHGR_STATE,CHGR_TG,";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			//逗号字符串
			field_inner += param;
		}else{
			field_inner+="BAND             ,BCCD             ,CBCH             ,CCCH             ,DCHNO_32         ,EACPREF          ,ETCHTN_8         ,EXCHGR           ,HOP              ,HOPTYPE          ,HSN              ,MAIO_16          ,NUMREQBPC        ,NUMREQCS3CS4BPC  ,NUMREQE2ABPC     ,NUMREQEGPRSBPC   ,ODPDCHLIMIT      ,SAS              ,SCTYPE           ,SDCCH            ,TN_8             ,TN7BCCH          ,TNBCCH           ,TSC              ,USERDATA         ,CITY_ID";
		}
		return field_inner;
	}
	public String buildFieldExportCont() {
		String field_out = "MEA_DATE,MSC,BSC,CELL,CH_GROUP,CHGR_STATE,CHGR_TG,";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			//逗号字符串
			field_out += param;
		}else{
			field_out+="BAND             ,BCCD             ,CBCH             ,CCCH             ,DCHNO_32         ,EACPREF          ,ETCHTN_8         ,EXCHGR           ,HOP              ,HOPTYPE          ,HSN              ,MAIO_16          ,NUMREQBPC        ,NUMREQCS3CS4BPC  ,NUMREQE2ABPC     ,NUMREQEGPRSBPC   ,ODPDCHLIMIT      ,SAS              ,SCTYPE           ,SDCCH            ,TN_8             ,TN7BCCH          ,TNBCCH           ,TSC              ,USERDATA         ,CITY_ID";
		}
		return field_out;
	}
	public static void main(String[] args) {
		Eri2GCellChannelQueryCond cond=new Eri2GCellChannelQueryCond();
		cond.setBsc("505,506,507,508,509,510,511,512,513,514,515");
		cond.setMeaDate("2014-10-20,2014-10-15");
		cond.setCell("ALL");
		cond.setCityId(91);
		cond.setParam("DCHNO_32,EACPREF,ETCHTN_8,EXCHGR,HOP,HOPTYPE,HSN,MAIO_16,NUMREQBPC");
		
		System.out.println(cond.buildSelectCont()+"    where   "+cond.buildWhereCont());
	}
}
