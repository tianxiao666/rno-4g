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
 * 查询爱立信2g小区查询条件
 * @author chao.xj
 *
 */
public class Eri2GCellQueryCond {
	private long cityId;
	private String meaDate;
	private String bsc;
	private String cell;
	private String param;
	
	private boolean mulTables;
	
	public boolean isMulTables() {
		return mulTables;
	}
	public void setMulTables(boolean mulTables) {
		this.mulTables = mulTables;
	}
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
			where += " t1.bsc  IN (" + bsc + ")";
		}
		
		if (cityId != -1) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " t1.CITY_ID=" + cityId;
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
							where += " (t1.MEA_DATE=to_date('"
									+ dateUtil.format_yyyyMMdd(bd)
									+ "','yyyy-mm-dd')";
						}else{
							where += StringUtils.isBlank(where) ? "" : " or ";
							where += " t1.MEA_DATE=to_date('"
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
					where += " t1.MEA_DATE=to_date('"
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
			where += " t1.cell  IN (" + str + ")";
		}
		
		return where;
	}
	/*public String buildSelectCont() {
		String select="select ";
		//select MEA_DATE,MSC,t2.ENGNAME BSC,CELL from rno_2g_eri_cell t1  left join(rno_bsc t2) on (t1.bsc=t2.bsc_id) 
		//select t1.MEA_DATE,t1.MSC,t2.ENGNAME BSC,t1.CELL,t3.* from rno_2g_eri_cell t1  left join(rno_bsc t2) on (t1.bsc=t2.bsc_id) left join(rno_2g_eri_cell_extra_info t3) on (t1.mea_date=t3.mea_date and t1.msc=t3.msc and t1.bsc=t3.bsc and t1.cell=t3.cell)
		//RNO_2G_ERI_CELL:ACC_16,ACCMIN;RNO_2G_ERI_CELL_EXTRA_INFO:AFLP,AFRCSCC
		
		String field = "MEA_DATE,MSC,t2.ENGNAME BSC,CELL";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			//逗号字符串
			field += param;
		}else{
			field+="ACC_16           ,ACCMIN           ,ACSTATE          ,ACTIVE_32        ,AGBLK            ,ALLOCPREF        ,AMRPCSTATE       ,ATT              ,AW               ,BCC              ,BCCHDTCB         ,BCCHDTCBHYST     ,BCCHLOSS         ,BCCHLOSSHYST     ,BCCHNO           ,BCCHREUSE        ,BCCHTYPE         ,BPDCHBR          ,BSPWR            ,BSPWRB           ,BSPWRMIN         ,BSPWRT           ,BSRXMIN          ,BSRXSUFF         ,BSTXPWR          ,BTSPS            ,BTSPSHYST        ,CB               ,CBCHD            ,CBQ              ,CCHPWR           ,CELLQ            ,CELL_STATE       ,CELL_TYPE        ,CHAP             ,CHCSDL           ,CI               ,CLSACC           ,CLSLEVEL         ,CLSRAMP          ,CLS_STATUS       ,CMDR             ,CODECREST        ,CRH              ,CRO              ,CSPSALLOC        ,CSPSPRIO         ,C_SYS_TYPE       ,DCHNO_128        ,DHA              ,DMPR             ,DMQB             ,DMQBAMR_15       ,DMQBNAMR_15      ,DMQG             ,DMQGAMR_15       ,DMQGNAMR_15      ,DMSUPP           ,DMTFAMR          ,DMTFNAMR         ,DMTHAMR          ,DMTHNAMR         ,DRX              ,DTHAMR_15        ,DTHNAMR_15       ,DTMSTATE         ,DTXD             ,DTXU             ,DYNBTSPWR_STATE  ,DYNMSPWR_STATE   ,DYNVGCH          ,ECSC             ,EFACTOR          ,EITEXCLUDED      ,EPDCHBR          ,EXTPEN           ,FASTMSREG        ,FBOFFS           ,FBVGCHALLOC      ,FDDMRR           ,FDDQMIN          ,FDDQMINOFF       ,FDDQOFF          ,FDDREPTHR2       ,FDDRSCPMIN       ,FERLEN           ,FLEXHIGHGPRS     ,FNOFFSET         ,FPDCH            ,GAMMA            ,GHPRIO           ,GLPRIOTHR        ,GPDCHBR          ,GPRSPRIO         ,GPRSSUP          ,HCSIN            ,HCSOUT           ,HOCLSACC         ,HPBSTATE         ,HYSTSEP          ,IAN              ,ICMSTATE         ,IDLE_32          ,IHO              ,INTAVE           ,IRC              ,ISHOLEV          ,LA               ,LAC              ,LAYER            ,LAYERHYST        ,LAYERTHR         ,LCOMPDL          ,LCOMPUL          ,LIMIT1           ,LIMIT2           ,LIMIT3           ,LIMIT4           ,MAXIHO           ,MAXISHO          ,MAXLAPDM         ,MAXRET           ,MAXSBLK          ,MAXSMSG          ,MAXTA            ,MBCR             ,MC               ,MCC              ,MFRMS            ,MINREQTCH        ,MISSNM           ,MNC              ,MPDCH            ,MSRXMIN          ,MSRXSUFF         ,MSTXPWR          ,NCC              ,NCCPERM_8        ,NECI             ,OPTMSCLASS       ,PDCHPREEMPT      ,PHCSTHR          ,PLAYER           ,PRACHBLK         ,PRIMPLIM         ,PSKONBCCH        ,PSSBQ            ,PSSHF            ,PSSTA            ,PSSTEMP          ,PT               ,PTIMBQ           ,PTIMHF           ,PTIMTA           ,PTIMTEMP         ,QCOMPDL          ,QCOMPUL          ,QDESDL           ,QDESDLAFR        ,QDESDLAHR        ,QDESDLAWB        ,QDESUL           ,QDESULAFR        ,QDESULAHR        ,QDESULAWB        ,QEVALSD          ,QEVALSI          ,QLENGTH          ,QLENSD           ,QLENSI           ,QLIMDL           ,QLIMDLAFR        ,QLIMDLAWB        ,QLIMUL           ,QLIMULAFR        ,QLIMULAWB        ,QOFFSETDL        ,QOFFSETDLAFR     ,QOFFSETDLAWB     ,QOFFSETUL        ,QOFFSETULAFR     ,QOFFSETULAWB     ,QSC              ,QSCI             ,QSI              ,REPPERNCH        ,RESLIMIT         ,RHYST            ,RLINKT           ,RLINKTAFR        ,RLINKTAHR        ,RLINKTAWB        ,RLINKUP          ,RLINKUPAFR       ,RLINKUPAHR       ,RLINKUPAWB       ,RTTI             ,SCALLOC          ,SCHO             ,SCLD             ,SCLDLOL          ,SCLDLUL          ,SCLDSC           ,SLEVEL           ,SPDCH            ,SPRIO            ,SSDESDL          ,SSDESDLAFR       ,SSDESDLAHR       ,SSDESDLAWB       ,SSDESUL          ,SSDESULAFR       ,SSDESULAHR       ,SSDESULAWB       ,SSEVALSD         ,SSEVALSI         ,SSLENDL          ,SSLENSD          ,SSLENSI          ,SSLENUL          ,SSOFFSETDL       ,SSOFFSETDLAFR    ,SSOFFSETDLAWB    ,SSOFFSETUL       ,SSOFFSETULAFR    ,SSOFFSETULAWB    ,SSRAMPSD         ,SSRAMPSI         ,STIME            ,STREAMSUP        ,T3212            ,TALIM            ,TBFDLLIM         ,TBFULLIM         ,TIHO             ,TMAXIHO          ,TO             ,TRAFBLK          ,TX               ,VGCHALLOC        ,XRANGE           ,DTCB             ,DTCBHYST         ,LOL              ,LOLHYST          ,NDIST            ,NNCELLS          ,TAOL             ,TAOLHYST         ,CITY_ID";
		}
		String from=" from rno_2g_eri_cell t1  left join(rno_bsc t2) on (t1.bsc=t2.bsc_id) ";
		select=select+field+from;
		return select;
	}*/
	public  String buildSelectCont() {
		String select="select ";
		String oriField="";
		/*oriField="RNO_2G_ERI_CELL:ACC_16,ACCMIN;RNO_2G_ERI_CELL_EXTRA_INFO:AFLP,AFRCSCC";
		oriField="RNO_2G_ERI_CELL:ACC_16,ACCMIN";*/
		oriField=param;
		String field[]=oriField.split(";");
		String comField = "";
		String firstTab="";
		String seconTab="";
		String firstTabField="";
		String seconTabField="";
		String from="";
		oriField="ALL";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			if(field.length==1){
				//涉及一张表
				firstTab=field[0].split(":")[0];
				firstTabField=field[0].split(":")[1];
				comField = "  TO_CHAR(t1.MEA_DATE,'YYYY-MM-DD') MEA_DATE,t1.MSC,t.ENGNAME BSC,t1.CELL";
				if("RNO_2G_ERI_CELL".equals(firstTab)){
					comField+=","+firstTabField+",";
				}else {
					comField+=","+seconTabField+",";
				}
				comField=comField.substring(0, comField.length()-1)+", row_number() OVER(ORDER BY null) AS rn";;
				from=" from "+firstTab+" t1  left join(rno_bsc t) on (t1.bsc=t.bsc_id) ";
			}
			if(field.length==2){
				//涉及两张表
				firstTab=field[0].split(":")[0];
				firstTabField=field[0].split(":")[1];
				seconTab=field[1].split(":")[0];
				seconTabField=field[1].split(":")[1];
				comField = "  TO_CHAR(t1.MEA_DATE,'YYYY-MM-DD') MEA_DATE,t1.MSC,t.ENGNAME BSC,t1.CELL";
				/*if("RNO_2G_ERI_CELL".equals(firstTab)){
					comField+=","+firstTabField+",";
				}else {
					comField+=","+seconTabField+",";
				}*/
				comField+=","+firstTabField+","+seconTabField+",";
				comField=comField.substring(0, comField.length()-1)+", row_number() OVER(ORDER BY null) AS rn";
				from="	from rno_2g_eri_cell t1  left join(rno_bsc t) on (t1.bsc=t.bsc_id) left join(rno_2g_eri_cell_extra_info t2) on (t1.mea_date=t2.mea_date and t1.msc=t2.msc and t1.bsc=t2.bsc and t1.cell=t2.cell)";
			}
		}else{
			//爱立信2G小区字段对应标题
			Map<String,DBFieldToLogTitle> eri2GCellDbFieldsToTitles  = Eri2GCellParserJobRunnable.readDbToTitleCfgFromXml("eri2GCellToTitle.xml");
			//爱立信2G小区额外信息字段对应标题
			Map<String,DBFieldToLogTitle> eri2GCellExtraInfoDbFieldsToTitles  = Eri2GCellParserJobRunnable.readDbToTitleCfgFromXml("eri2GCellExtraInfoToTitle.xml");
			comField = "  TO_CHAR(t1.MEA_DATE,'YYYY-MM-DD') MEA_DATE,t1.MSC,t.ENGNAME BSC,t1.CELL";
			for (String key : eri2GCellDbFieldsToTitles.keySet()) {
				if("MSC".equals(key)||"BSC".equals(key)||"CELL".equals(key) || "TO".equals(key)){
					continue;
				}
				comField+=","+key;
			}
			for (String key : eri2GCellExtraInfoDbFieldsToTitles.keySet()) {
				if("MSC".equals(key)||"BSC".equals(key)||"CELL".equals(key)){
					continue;
				}
				comField+=","+key;
			}
			comField=comField+", row_number() OVER(ORDER BY null) AS rn";
			from="	from rno_2g_eri_cell t1  left join(rno_bsc t) on (t1.bsc=t.bsc_id) left join(rno_2g_eri_cell_extra_info t2) on (t1.mea_date=t2.mea_date and t1.msc=t2.msc and t1.bsc=t2.bsc and t1.cell=t2.cell)";
		}

		return select+comField+from;
	}
	public  String buildSelectContForCnt() {
		String select="select ";
		String oriField="";
		/*oriField="RNO_2G_ERI_CELL:ACC_16,ACCMIN;RNO_2G_ERI_CELL_EXTRA_INFO:AFLP,AFRCSCC";
		oriField="RNO_2G_ERI_CELL:ACC_16,ACCMIN";*/
		oriField=param;
		String field[]=oriField.split(";");
		String comField = "";
		String firstTab="";
		String seconTab="";
		String firstTabField="";
		String seconTabField="";
		String from="";
		oriField="ALL";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			if(field.length==1){
				//涉及一张表
				firstTab=field[0].split(":")[0];
				if("BASICTAB".equals(firstTab)){
					firstTab="RNO_2G_ERI_CELL";
				}else if("EXTRATAB".equals(firstTab)){
					firstTab="RNO_2G_ERI_CELL_EXTRA_INFO";
				}
				from=" from "+firstTab+" t1  left join(rno_bsc t) on (t1.bsc=t.bsc_id) ";
			}
			if(field.length==2){
				//涉及两张表
				from="	from rno_2g_eri_cell t1  left join(rno_bsc t) on (t1.bsc=t.bsc_id) left join(rno_2g_eri_cell_extra_info t2) on (t1.mea_date=t2.mea_date and t1.msc=t2.msc and t1.bsc=t2.bsc and t1.cell=t2.cell)";
			}
		}else{
			
			from="	from rno_2g_eri_cell t1  left join(rno_bsc t) on (t1.bsc=t.bsc_id) left join(rno_2g_eri_cell_extra_info t2) on (t1.mea_date=t2.mea_date and t1.msc=t2.msc and t1.bsc=t2.bsc and t1.cell=t2.cell)";
		}

		return from;
	}
	public String buildFieldOutCont() {
		//select MEA_DATE,MSC,t2.ENGNAME BSC,CELL from rno_2g_eri_ncell_param t1  left join(rno_bsc t2) on (t1.bsc=t2.bsc_id) 
		String oriField="";
		/*oriField="RNO_2G_ERI_CELL:ACC_16,ACCMIN;RNO_2G_ERI_CELL_EXTRA_INFO:AFLP,AFRCSCC";
		oriField="RNO_2G_ERI_CELL:ACC_16,ACCMIN";*/
		oriField=param;
		String field[]=oriField.split(";");
		String comField = "";
		String firstTab="";
		String seconTab="";
		String firstTabField="";
		String seconTabField="";
		String from="";
		oriField="ALL";
		String table="";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			//BASICTAB,EXTRATAB
			if(field.length==1){
				//涉及一张表
				firstTab=field[0].split(":")[0];
				firstTabField=field[0].split(":")[1];
				comField = "  TO_CHAR(MEA_DATE,'YYYY-MM-DD') MEA_DATE,MSC,BSC,CELL";
				if("BASICTAB".equals(firstTab)){
					comField+=","+firstTabField+",";
					table="RNO_2G_ERI_CELL";
				}else {
					comField+=","+firstTabField+",";
					table="RNO_2G_ERI_CELL_EXTRA_INFO";
				}
				comField=comField.substring(0, comField.length()-1);
			}
			if(field.length==2){
				//涉及两张表
				firstTab=field[0].split(":")[0];
				firstTabField=field[0].split(":")[1];
				seconTab=field[1].split(":")[0];
				seconTabField=field[1].split(":")[1];
				comField = "  TO_CHAR(MEA_DATE,'YYYY-MM-DD') MEA_DATE,MSC,BSC,CELL";
				//if("BASICTAB".equals(firstTab)){
					comField+=","+firstTabField;
				//}else {
					comField+=","+seconTabField+",";
				//}
				comField=comField.substring(0, comField.length()-1);
			}
		}else{
			//爱立信2G小区字段对应标题
			Map<String,DBFieldToLogTitle> eri2GCellDbFieldsToTitles  = Eri2GCellParserJobRunnable.readDbToTitleCfgFromXml("eri2GCellToTitle.xml");
			//爱立信2G小区额外信息字段对应标题
			Map<String,DBFieldToLogTitle> eri2GCellExtraInfoDbFieldsToTitles  = Eri2GCellParserJobRunnable.readDbToTitleCfgFromXml("eri2GCellExtraInfoToTitle.xml");
			comField = "  TO_CHAR(MEA_DATE,'YYYY-MM-DD') MEA_DATE,MSC,BSC,CELL";
			for (String key : eri2GCellDbFieldsToTitles.keySet()) {
				if("MSC".equals(key)||"BSC".equals(key)||"CELL".equals(key) || "TO".equals(key)){
					continue;
				}
				comField+=","+key;
			}
			for (String key : eri2GCellExtraInfoDbFieldsToTitles.keySet()) {
				if("MSC".equals(key)||"BSC".equals(key)||"CELL".equals(key)){
					continue;
				}
				comField+=","+key;
			}
		}
		return comField;
	}
	public String buildFieldInnerAndFromCont() {
		//select MEA_DATE,MSC,t2.ENGNAME BSC,CELL from rno_2g_eri_ncell_param t1  left join(rno_bsc t2) on (t1.bsc=t2.bsc_id) 
		String oriField="";
		/*oriField="RNO_2G_ERI_CELL:ACC_16,ACCMIN;RNO_2G_ERI_CELL_EXTRA_INFO:AFLP,AFRCSCC";
		oriField="RNO_2G_ERI_CELL:ACC_16,ACCMIN";*/
		oriField=param;
		String field[]=oriField.split(";");
		String comField = "";
		String firstTab="";
		String seconTab="";
		String firstTabField="";
		String seconTabField="";
		String from="";
		oriField="ALL";
		String table="";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			//BASICTAB,EXTRATAB
			if(field.length==1){
				//涉及一张表
				firstTab=field[0].split(":")[0];
				firstTabField=field[0].split(":")[1];
				comField = "  t1.MEA_DATE,t1.MSC,t.ENGNAME BSC,t1.CELL";
				if("BASICTAB".equals(firstTab)){
					comField+=","+firstTabField+",";
					table="RNO_2G_ERI_CELL";
				}else {
					comField+=","+firstTabField+",";
					table="RNO_2G_ERI_CELL_EXTRA_INFO";
				}
				comField=comField.substring(0, comField.length()-1);
				from=",row_number() over(partition by null order by t1.MEA_DATE desc) rn from "+table+" t1  left join(rno_bsc t) on (t1.bsc=t.bsc_id) ";
			}
			if(field.length==2){
				//涉及两张表
				firstTab=field[0].split(":")[0];
				firstTabField=field[0].split(":")[1];
				seconTab=field[1].split(":")[0];
				seconTabField=field[1].split(":")[1];
				comField = "  t1.MEA_DATE,t1.MSC,t.ENGNAME BSC,t1.CELL";
				//if("BASICTAB".equals(firstTab)){
					comField+=","+firstTabField;
				//}else {
					comField+=","+seconTabField+",";
				//}
				comField=comField.substring(0, comField.length()-1);
				from=",row_number() over(partition by null order by t1.MEA_DATE desc) rn from rno_2g_eri_cell t1  left join(rno_bsc t) on (t1.bsc=t.bsc_id) left join(rno_2g_eri_cell_extra_info t2) on (t1.mea_date=t2.mea_date and t1.msc=t2.msc and t1.bsc=t2.bsc and t1.cell=t2.cell)";
			}
		}else{
			//爱立信2G小区字段对应标题
			Map<String,DBFieldToLogTitle> eri2GCellDbFieldsToTitles  = Eri2GCellParserJobRunnable.readDbToTitleCfgFromXml("eri2GCellToTitle.xml");
			//爱立信2G小区额外信息字段对应标题
			Map<String,DBFieldToLogTitle> eri2GCellExtraInfoDbFieldsToTitles  = Eri2GCellParserJobRunnable.readDbToTitleCfgFromXml("eri2GCellExtraInfoToTitle.xml");
			comField = "  t1.MEA_DATE,t1.MSC,t.ENGNAME BSC,t1.CELL";
			for (String key : eri2GCellDbFieldsToTitles.keySet()) {
				if("MSC".equals(key)||"BSC".equals(key)||"CELL".equals(key) || "TO".equals(key)){
					continue;
				}
				comField+=","+key;
			}
			for (String key : eri2GCellExtraInfoDbFieldsToTitles.keySet()) {
				if("MSC".equals(key)||"BSC".equals(key)||"CELL".equals(key)){
					continue;
				}
				comField+=","+key;
			}
			from=",row_number() over(partition by null order by t1.MEA_DATE desc) rn from rno_2g_eri_cell t1  left join(rno_bsc t) on (t1.bsc=t.bsc_id) left join(rno_2g_eri_cell_extra_info t2) on (t1.mea_date=t2.mea_date and t1.msc=t2.msc and t1.bsc=t2.bsc and t1.cell=t2.cell)";
		}
		return comField+from;
	}
	public String buildFieldExportCont() {
		//select MEA_DATE,MSC,t2.ENGNAME BSC,CELL from rno_2g_eri_ncell_param t1  left join(rno_bsc t2) on (t1.bsc=t2.bsc_id) 
		String oriField="";
		/*oriField="RNO_2G_ERI_CELL:ACC_16,ACCMIN;RNO_2G_ERI_CELL_EXTRA_INFO:AFLP,AFRCSCC";
		oriField="RNO_2G_ERI_CELL:ACC_16,ACCMIN";*/
		oriField=param;
		String field[]=oriField.split(";");
		String comField = "";
		String firstTab="";
		String seconTab="";
		String firstTabField="";
		String seconTabField="";
		String from="";
		oriField="ALL";
		String table="";
		if (!StringUtils.isBlank(param) && !"ALL".equals(param)) {
			//BASICTAB,EXTRATAB
			if(field.length==1){
				//涉及一张表
				firstTab=field[0].split(":")[0];
				firstTabField=field[0].split(":")[1];
				comField = "MEA_DATE,MSC,BSC,CELL";
				if("BASICTAB".equals(firstTab)){
					comField+=","+firstTabField+",";
					table="RNO_2G_ERI_CELL";
				}else {
					comField+=","+firstTabField+",";
					table="RNO_2G_ERI_CELL_EXTRA_INFO";
				}
				comField=comField.substring(0, comField.length()-1);
			}
			if(field.length==2){
				//涉及两张表
				firstTab=field[0].split(":")[0];
				firstTabField=field[0].split(":")[1];
				seconTab=field[1].split(":")[0];
				seconTabField=field[1].split(":")[1];
				comField = "MEA_DATE,MSC,BSC,CELL";
				//if("BASICTAB".equals(firstTab)){
					comField+=","+firstTabField;
				//}else {
					comField+=","+seconTabField+",";
				//}
				comField=comField.substring(0, comField.length()-1);
			}
		}else{
			//爱立信2G小区字段对应标题
			Map<String,DBFieldToLogTitle> eri2GCellDbFieldsToTitles  = Eri2GCellParserJobRunnable.readDbToTitleCfgFromXml("eri2GCellToTitle.xml");
			//爱立信2G小区额外信息字段对应标题
			Map<String,DBFieldToLogTitle> eri2GCellExtraInfoDbFieldsToTitles  = Eri2GCellParserJobRunnable.readDbToTitleCfgFromXml("eri2GCellExtraInfoToTitle.xml");
			comField = "MEA_DATE,MSC,BSC,CELL";
			for (String key : eri2GCellDbFieldsToTitles.keySet()) {
				if("MSC".equals(key)||"BSC".equals(key)||"CELL".equals(key) || "TO".equals(key)){
					continue;
				}
				comField+=","+key;
			}
			for (String key : eri2GCellExtraInfoDbFieldsToTitles.keySet()) {
				if("MSC".equals(key)||"BSC".equals(key)||"CELL".equals(key)){
					continue;
				}
				comField+=","+key;
			}
		}
		return comField;
	}
	public static void main(String[] args) {
		Eri2GCellQueryCond cellQueryCond=new Eri2GCellQueryCond();
		cellQueryCond.setCityId(91);
		cellQueryCond.setMeaDate("2014-10-20");
		cellQueryCond.setParam("ALL");
		System.out.println(cellQueryCond.buildSelectCont());
	}
}
