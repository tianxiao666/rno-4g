package com.iscreate.op.dao.rno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.op.pojo.rno.ResultInfo;
import com.iscreate.op.service.rno.parser.MrrParserContext;
import com.iscreate.op.service.rno.parser.vo.NcsAdmRecord;
import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.DbValInject;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.tools.LatLngHelper;

public class RnoStructAnaV2Impl implements RnoStructAnaV2 {

	private static Log log = LogFactory.getLog(RnoStructAnaV2Impl.class);

	private ThreadLocal CellMainInfo = new ThreadLocal(); // 线程本地环境

	/**
	 * 准备处理爱立信Ncs分析所需要的城市数据信息
	 * 
	 * @param stmt
	 * @param tabName
	 * @param cityId
	 * @return
	 * @author brightming 2014-8-19 上午11:58:11
	 */
	public ResultInfo prepareEriCityCellData(Statement stmt, String tabName,
			long cityId) {

		List<Long> subAreas = AuthDsDataDaoImpl
				.getSubAreaIdsByCityId(cityId);
		String areaStrs = cityId+",";
		for (Long id : subAreas) {
			areaStrs += id + ",";
		}
		if (areaStrs.length() > 0) {
			areaStrs = areaStrs.substring(0, areaStrs.length()-1);
		}
		
		String sql = "delete from " + tabName;
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultInfo result = new ResultInfo();
		result.setFlag(true);
		// 转移数据
		sql = "insert into "
				+ tabName
				+ " (CELL,NAME,CELL_FREQ_SECTION,LAC,CI,BSIC,BCCH,TCH,DOWNTILT,BEARING,LONGITUDE,LATITUDE,SITE,BSC,BTSTYPE,INDOOR_CELL_TYPE,CELL_FREQ_CNT) ";
//		String innerSql = "SELECT MID.LABEL,MID.NAME, "
//				+ " (CASE WHEN GSMFREQUENCESECTION IS NULL THEN (CASE WHEN BCCH IS NOT NULL THEN (CASE WHEN BCCH>100 AND BCCH<1000 THEN 'GSM1800' ELSE 'GSM900' END) ELSE NULL END) ELSE GSMFREQUENCESECTION END) AS GSMFREQUENCESECTION,"
//				+ " LAC, "
//				+ " CI, "
//				+ " (case when length(to_char(bsic))=1 then '0'||to_char(bsic) else to_char(bsic) end) BSIC, "
//				+ " BCCH, "
//				+ " TCH, "
//				+ " DOWNTILT, "
//				+ " BEARING, "
//				+ " LONGITUDE, "
//				+ " LATITUDE, "
//				+ " SITE, "
//				+ " RNO_BSC.ENGNAME, "
//				+ " BTSTYPE, "
//				+ " (CASE WHEN INDOOR_CELL_TYPE='室内覆盖' THEN 'Y' ELSE 'N' END) AS INDOOR_CELL_TYPE, "
//				+ " RNO_GET_FREQ_CNTV2(BCCH,TCH) AS CELL_FREQ_CNT "
//				+ " FROM "
//				+ " ( select * from ("
//				+ " SELECT LABEL,NAME,GSMFREQUENCESECTION,LAC,CI,BSIC,BCCH,TCH,DOWNTILT,BEARING,LONGITUDE,LATITUDE,SITE,BSC_ID,BTSTYPE,INDOOR_CELL_TYPE,ROW_NUMBER() OVER(PARTITION BY LABEL ORDER BY LABEL NULLS LAST) RN "
//				+ " FROM CELL WHERE CELL.AREA_ID IN ( " + areaStrs
//				+ " )) where rn=1 " + " )MID  LEFT JOIN RNO_BSC  "
//				+ " ON(MID.BSC_ID=RNO_BSC.BSC_ID) and rn=1";
		
		String innerSql = " SELECT MID.LABEL,MID.NAME,  "
				+ " (CASE WHEN GSMFREQUENCESECTION IS NULL THEN (CASE WHEN BCCH IS NOT NULL THEN (CASE WHEN BCCH>100 AND BCCH<1000 THEN 'GSM1800' ELSE 'GSM900' END) ELSE NULL END) ELSE GSMFREQUENCESECTION END) AS GSMFREQUENCESECTION, "
				+ " LAC,  "
				+ "  CI,  "
				+ "  (case when length(to_char(bsic))=1 then '0'||to_char(bsic) else to_char(bsic) end) BSIC,  "
				+ "  BCCH,  "
				+ "  TCH,  "
				+ "  DOWNTILT,  "
				+ "  BEARING,  "
				+ "  LONGITUDE,  "
				+ "  LATITUDE,  "
				+ "  SITE,  "
				+ "  ENGNAME,  "
				+ "  BTSTYPE,  "
				+ "  (CASE WHEN INDOOR_CELL_TYPE='室内覆盖' THEN 'Y' ELSE 'N' END) AS INDOOR_CELL_TYPE,  "
				+ "  RNO_GET_FREQ_CNTV2(BCCH,TCH) AS CELL_FREQ_CNT  "
				+ "  FROM  "
				+ " ( select * from ( "
				+ "  SELECT LABEL,NAME,GSMFREQUENCESECTION,LAC,CI,BSIC,BCCH,TCH,DOWNTILT,BEARING,LONGITUDE,LATITUDE,SITE,cell.BSC_ID,BTSTYPE,INDOOR_CELL_TYPE, "
				+ "  bsc.engname, "
				+ " ROW_NUMBER() OVER(PARTITION BY LABEL  ORDER BY LABEL ,bsc.engname NULLS LAST) RN "
				+ " 		 FROM CELL ,rno_bsc bsc  WHERE CELL.AREA_ID IN ( "
				+ areaStrs + " ) and cell.bsc_id=bsc.bsc_id "
				+ "  ) where rn=1  )MID ";

		sql += innerSql;
		log.debug("转移小区到分析空间的sql=" + sql);
		try {
			int affct = stmt.executeUpdate(sql);
			log.debug("转移小区数据： " + affct);
		} catch (SQLException e) {
			log.error("转移小区数据到分析空间失败！sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("转移小区数据到分析空间失败！");
		}
		return result;

	}

	/**
	 * 邻区匹配
	 * 
	 * @param stmt
	 * @param ncsTabName
	 * @param cellTab
	 * @param ncsId
	 * @param cityId
	 * @return
	 * @author brightming 2014-8-19 下午12:46:29
	 */
	@Deprecated
	public ResultInfo matchEriNcsNcell(Statement stmt, String ncsTabName,
			String cellTab, long cityId) {

		String sql = "";
		// printTemp(stmt, ncsTabName,"刚进入matchEriNcsNcell");

		ResultInfo result = new ResultInfo();
		result.setFlag(true);

		log.debug("进入方法：matchEriNcsNcell。stmt=" + stmt + ",tabName="
				+ ncsTabName);
		if (stmt == null || StringUtils.isBlank(ncsTabName)
				|| StringUtils.isBlank(cellTab)) {
			log.error("dao matchEriNcsNcell方法的参数不能为空！conn=" + stmt
					+ ",tabName=" + ncsTabName + ",cellTab=" + cellTab);
			result.setFlag(false);
			result.setMsg("邻区匹配时，需要的参数不齐全！");
			return result;
		}

		sql = "";
		ResultSet rs = null;

		// -------------检查该区域下是否有小区信息------------------//
		// sql = "select count(cell) from " + cellTab;
		// long cellCnt = 0;
		// try {
		// rs = stmt.executeQuery(sql);
		// while (rs.next()) {
		// cellCnt = rs.getLong(1);
		// }
		// } catch (SQLException e) {
		// e.printStackTrace();
		// result.setFlag(false);
		// result.setMsg("统计该市的小区数量是出错！");
		// return result;
		// } finally {
		// try {
		// rs.close();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// // printTemp(stmt, ncsTabName,"检查完小区数量后");
		//
		// log.debug("cityId=" + cityId + ",下有小区数量：" + cellCnt + "个");
		// if (cellCnt <= 0) {
		// log.error("cityId=" + cityId + "下没有任何小区！不能进行邻区信息匹配！");
		// result.setFlag(true);// 也不算失败
		// result.setMsg("该市的小区数量为0");
		// return result;
		// }

		int affectCnt = 0;

		long t1 = System.currentTimeMillis();
		// ----------用bsic，bcch组合为唯一的小区去更新ncs表的邻区信息----------//
		// ---更新邻区名称、邻区频点数---//
		// sql = "merge into "
		// + ncsTabName
		// + " tar using "
		// + "("
		// +
		// " select cell.cell,cell.NAME,cell.longitude,cell.latitude,cell.bsic,cell.bcch,cell.site,cell.INDOOR_CELL_TYPE,cell.CELL_FREQ_SECTION"
		// + " ,CELL_FREQ_CNT as freqcnt "
		// + " from "
		// + cellTab
		// + " cell inner join ("
		// + "  select bsic,bcch,count(*) as cnt from "
		// + cellTab
		// + " group by bsic,bcch having count(bsic)=1"
		// + ")mid2 "
		// + " on( cell.bsic=MID2.bsic and CELL.bcch=MID2.bcch ) "
		// + ")mid3 "
		// +
		// " on (tar.bsic=mid3.bsic and tar.arfcn=mid3.bcch and tar.cell<>mid3.cell)"
		// // 2014-7-23gmh添加：邻区不能是自己
		// +
		// " when matched then update set tar.ncell=mid3.CELL,tar.NCELL_FREQ_CNT=mid3.freqcnt,tar.ncells=mid3.name "
		// + ",TAR.NCELL_LON=mid3.longitude,tar.NCELL_LAT=mid3.latitude "
		// +
		// ",tar.NCELL_SITE=mid3.site,tar.NCELL_FREQ_SECTION=mid3.CELL_FREQ_SECTION,tar.NCELL_INDOOR=mid3.INDOOR_CELL_TYPE";
		// log.debug(">>>>>>>>>>>>>>>>准备用唯一的bsic、bcch组合更新ncs的信息的sql=" + sql);
		// try {
		// affectCnt = stmt.executeUpdate(sql);
		// log.debug("用唯一的bsic、bcch组合更新ncs的信息影响行数：" + affectCnt);
		// } catch (SQLException e) {
		// log.error("用唯一的bsic、bcch组合更新ncs的信息的操作失败！sql=" + sql);
		// e.printStackTrace();
		//
		// result.setFlag(false);
		// result.setMsg("邻区匹配阶段一出错！");
		// return result;
		// }
		long t2 = System.currentTimeMillis();
		// log.debug("<<<<<<<<<<<<<<<<完成用唯一的bsic、bcch组合更新ncs的信息。耗时:" + (t2 - t1)
		// + "ms");

		// printTemp(stmt, ncsTabName,"更新邻区名称、邻区频点数");
		// -----------处理ncs的ncell依旧为空的小区----------------//
		// 先清空第一个临时表
		try {
			affectCnt = stmt.executeUpdate("delete from rno_ncs_match_ncell");
			log.debug("清空第一个临时表rno_ncs_match_ncell影响行数：" + affectCnt);
		} catch (SQLException e) {
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配处理第一个分析表时出错！");
			return result;
		}
		// printTemp(stmt, ncsTabName,"清空第一个临时表后");
		t1 = System.currentTimeMillis();

		sql = "insert into rno_ncs_match_ncell "
				+ " ( "
				+ " TOKEN,CELL,S_LON,S_LAT,BSIC,ARFCN,LABEL,N_LON,N_LAT,DIS "
				+ " ,NCELL_NAME,CELL_FREQ_CNT,NCELL_FREQ_CNT,SAME_FREQ_CNT,ADJ_FREQ_CNT  "
				+ " ) "
				+ " select  "
				+ " MID2.cell||MID2.bsic||MID2.arfcn as token , "
				+ " mid2.cell,mid2.s_lon,mid2.s_lat ,mid2.bsic,mid2.arfcn "
				+ " ,mid.cell,mid.longitude as n_lon,mid.latitude as n_lat ,FUN_RNO_GetDistance(mid.longitude,mid.latitude,mid2.s_lon,mid2.s_lat) as dis "
				+ " ,MID.name,MID2.CELL_FREQ_CNT,MID.NCELL_FREQ_CNT,RNO_GETCOFREQCNT(mid2.s_tch,mid.n_tch) ,RNO_GETADJACENTFREQCNT(mid2.s_tch,mid.n_tch) "
				+ " from  "
				+ " ( "
				+ " select distinct(tar.cbb),tar.CELL,TAR.ncell,tar.BSIC,tar.ARFCN ,midcell.longitude as s_lon,midcell.latitude as s_lat ,midcell.CELL_FREQ_CNT ,midcell.tch as s_tch "
				+ " FROM (select cell,ncell,bsic,arfcn,cell||bsic||arfcn as cbb from "
				+ ncsTabName
				+ "  where (ncell is null)) TAR  "
				+ " left join (select cell,tch, CELL_FREQ_CNT,longitude,latitude from "
				+ cellTab
				+ ") midcell "
				+ " on (tar.cell=midcell.cell) "
				+ " ) mid2  "
				+ " left JOIN "
				+ " (SELECT cell,name,tch as n_tch,CELL_FREQ_CNT as NCELL_FREQ_CNT,LONGITUDE,LATITUDE,BCCH, bsic FROM "
				+ cellTab
				+ " ) MID "
				+ " ON ( mid2.BSIC=MID.BSIC AND mid2.ARFCN=MID.BCCH AND mid2.cell<>mid.cell) "// 2014-7-23
				+ " order by mid2.cell,mid2.bsic,mid2.arfcn,dis asc";
		log.debug(">>>>>>>>>>>>>准备将ncs中ncell为空的信息与cell表的信息进行组合，存入到中间表中,sql="
				+ sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("将ncs中ncell为空的信息与cell表的信息进行组合，存入到中间表中，影响行数=" + affectCnt);
		} catch (SQLException e) {
			log.error("将ncs中ncell为空的信息与cell表的信息进行组合，存入到中间表中时失败！sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配时，处理尚未匹配出来的邻区信息时失败！");
			return result;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成将ncs中ncell为空的信息与cell表的信息进行组合，存入临时表.耗时："
				+ (t2 - t1) + "ms");

		// ----------设置候选邻区信息-------------//
		sql = "merge into RNO_NCS_MATCH_NCELL tar "
				+ " using "
				+ " ( "
				+ " select TOKEN, to_char(rno_concat_clob(LABEL||'('||NCELL_NAME||',['||dis||'])')) as ncells from RNO_NCS_MATCH_NCELL group by token "
				+ ")mid "
				+ " on (tar.token=mid.token and mid.ncells<>'(,[10000000000])') "
				+ " when matched then update set tar.ncells=mid.ncells";
		log.debug(">>>>>>>>>>>准备更新候选邻区信息，sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("更新候选邻区，影响行数：" + affectCnt);
		} catch (SQLException e) {
			log.error("更新候选邻区失败！sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配时，更新分析表的候选邻区时失败！");
			return result;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成更新候选邻区信息，耗时：" + (t2 - t1) + "ms");

		// ---------将第一个临时表的重复内容筛选出到第二个临时表-----//
		// 清空第二个临时表
		try {
			stmt.executeUpdate("delete from rno_ncs_matchncell_mindis");
		} catch (SQLException e) {
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配时，清除第二个分析表时失败！");
			return result;
		}
		t1 = t2;
		sql = "insert into rno_ncs_matchncell_mindis(token,cnt,dis) "
				+ "select token,count(*),min(dis) from rno_ncs_match_ncell group by token having count(*) > 1 order by count(*) desc";
		log.debug(">>>>>>>>>>>将第一个临时表的重复内容筛选到第二个临时表，sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			log.error("将第一个临时表的重复内容筛选到第二个临时表时失败，sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配时，将第一个临时表的重复内容筛选到第二个临时表时失败！");
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成将第一个临时表的重复内容筛选到第二个临时表,耗时：" + (t2 - t1) + "ms");

		// ------------用第二个临时表的数据删除第一个临时表的重复数据----------------------//
		t1 = t2;
		sql = "delete from rno_ncs_match_ncell a where a.dis >" + " ( "
				+ " select min(b.dis) from rno_ncs_matchncell_mindis b "
				+ " where a.token = b.token)";
		log.debug(">>>>>>>>>>>准备用第二个临时表的数据删除第一个临时表的重复数据，sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			log.error("用第二个临时表的数据删除第一个临时表的重复数据时失败，sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配时，用第二个临时表的数据删除第一个临时表的重复数据时失败！");
			return result;

		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成用第二个临时表的数据删除第一个临时表的重复数据，耗时：" + (t2 - t1)
				+ "ms");

		// ----将第一个临时表的相同的token、相同的dis的多余记录删除掉----//
		t1 = t2;
		sql = "delete from rno_ncs_match_ncell a "
				+ " where a.rowid  in "
				+ "("
				+ "select a.rowid from rno_ncs_match_ncell a "
				+ " where a.rowid !=("
				+ "select max(b.rowid) from rno_ncs_match_ncell b where a.token = b.token"
				+ ")" + ")";
		log.debug(">>>>>>>>>>>准备删除第一个临时表的token与dis都相同的重复数据，sql=" + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			log.error("删除第一个临时表的token与dis都相同的重复数据时失败，sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配时，删除第一个临时表的token与dis都相同的重复数据时失败！");
			return result;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成删除第一个临时表的token与dis都相同的重复数据，耗时：" + (t2 - t1)
				+ "ms");

		// -----------将仍然没有匹配出来的第一个临时表的ncell名称用规定的命名代替--------------//
		sql = "update rno_ncs_match_ncell set label='NotF_'||ARFCN||'_'||BSIC WHERE LABEL IS NULL OR LABEL =' ' OR LABEL=''";
		log.debug("将临时表中未匹配到邻区名称的记录的邻区名称用默认名称代替，sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("不能匹配出来的邻区的个数用NOTF_将afrcn+\"_+\"bcch命名，共有：" + affectCnt);
		} catch (SQLException e) {
			log.error("将临时表中未匹配到邻区名称的记录的邻区名称用默认名称代替时失败，sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配时，将临时表中未匹配到邻区名称的记录的邻区名称用默认名称代替时失败！");
			return result;
		}

		// -------此时，第一个临时表的数据，可以用来更新目标ncs表了------------//
		sql = "merge into "
				+ ncsTabName
				+ " tar using rno_ncs_match_ncell mid "
				+ "on( tar.cell=mid.cell and tar.bsic=mid.bsic and tar.arfcn=mid.arfcn"
				+ ")"
				+ " when matched then update set "
				+ " tar.ncell=mid.label,tar.distance=mid.dis,tar.NCELLS=mid.NCELLS,tar.CELL_FREQ_CNT=mid.CELL_FREQ_CNT,"
				+ "tar.NCELL_FREQ_CNT=mid.NCELL_FREQ_CNT,tar.SAME_FREQ_CNT=mid.SAME_FREQ_CNT,tar.ADJ_FREQ_CNT=mid.ADJ_FREQ_CNT";
		t1 = System.currentTimeMillis();
		log.debug(">>>>>>>>>>>>准备用第一个临时表rno_ncs_match_ncell的内容，更新目标表："
				+ ncsTabName + "的信息。sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("最后，用邻区匹配中间表的数据最终更新目标表的记录，共有：" + affectCnt);
		} catch (SQLException e) {
			log.error("用邻区匹配中间表的数据最终更新目标表的记录时失败，sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配时，用邻区匹配中间表的数据最终更新目标表的记录时失败！");
			return result;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<完成用第一个临时表rno_ncs_match_ncell的内容，更新目标表："
				+ ncsTabName + "的信息。耗时：" + (t2 - t1) + "ms");

		// ---完善小区属性---//
		sql = "merge into "
				+ ncsTabName
				+ " tar using "
				+ cellTab
				+ " src on(tar.cell=src.cell) when matched "
				+ " then update set TAR.CELL_BCCH=SRC.BCCH,TAR.CELL_TCH=SRC.TCH,tar.CELL_FREQ_CNT=SRC.CELL_FREQ_CNT,"
				+ "TAR.CELL_INDOOR=SRC.INDOOR_CELL_TYPE,"
				+ "TAR.CELL_LON=SRC.LONGITUDE,TAR.CELL_LAT=SRC.LATITUDE,"
				+ "TAR.CELL_BEARING=SRC.BEARING,TAR.CELL_DOWNTILT=SRC.DOWNTILT,"
				+ "TAR.CELL_SITE=SRC.SITE,TAR.CELL_FREQ_SECTION=SRC.CELL_FREQ_SECTION";
		t1 = System.currentTimeMillis();
		log.debug(">>>>>>>>>>>>准备完善小区的信息。sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("完善小区的信息，共有：" + affectCnt);
		} catch (SQLException e) {
			log.error("完善小区的信息时失败，sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配时，完善小区的信息时失败！");
			return result;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<完成完善小区的信息信息。耗时：" + (t2 - t1) + "ms");

		// ---完善邻区属性---//
		sql = "merge into "
				+ ncsTabName
				+ " tar using "
				+ cellTab
				+ " src on(tar.ncell=src.cell) when matched "
				+ " then update set TAR.NCELL_TCH=SRC.TCH,tar.NCELL_FREQ_CNT=SRC.CELL_FREQ_CNT,"
				+ "TAR.NCELL_INDOOR=SRC.INDOOR_CELL_TYPE,"
				+ "TAR.NCELL_LON=SRC.LONGITUDE,TAR.NCELL_LAT=SRC.LATITUDE,"
				+ "TAR.NCELL_SITE=SRC.SITE,TAR.NCELL_FREQ_SECTION=SRC.CELL_FREQ_SECTION";
		t1 = System.currentTimeMillis();
		log.debug(">>>>>>>>>>>>准备完善邻区的信息。sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("完善邻区的信息，共有：" + affectCnt);
		} catch (SQLException e) {
			log.error("完善邻区的信息时失败，sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配时，完善邻区的信息时失败！");
			return result;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<完成完善邻区的信息信息。耗时：" + (t2 - t1) + "ms");

		// ----------------更新距离、同频数、邻频数、cell到ncell的夹角（正北方向为0）----------------------//
		t1 = t2;
		sql = "update "
				+ ncsTabName
				+ " set distance=FUN_RNO_GetDistance(CELL_LON,CELL_LAT,NCELL_LON,NCELL_LAT),same_freq_cnt=RNO_GETCOFREQCNT(CELL_BCCH||','||CELL_TCH,ARFCN||','||NCELL_TCH),adj_freq_cnt=RNO_GETADJACENTFREQCNT(CELL_BCCH||','||CELL_TCH,ARFCN||','||NCELL_TCH),CELL_TO_NCELL_DIR=calculateDirToNorth(CELL_LON,CELL_LAT,NCELL_LON,NCELL_LAT)";
		log.debug(">>>>>>>>>>>>>准备更新已有邻区的ncs记录的距离、同频数、邻频数信息，sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("更新距离、同频数、邻频数、cell到ncell的夹角（正北方向为0）影响行数：" + affectCnt);
		} catch (SQLException e) {
			log.error("更新距离、同频数、邻频数、cell到ncell的夹角（正北方向为0）的操作失败！sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配时，更新距离、同频数、邻频数、cell到ncell的夹角（正北方向为0）时失败！");
			return result;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成更新已有邻区的ncs记录的距离、同频数、邻频数信息。耗时：" + (t2 - t1)
				+ "ms");

		// ---2014-9-4 最后面有邻区没有匹配出来的，但是其频段是可以根据其主频确定的
		sql = "update "
				+ ncsTabName
				+ " set NCELL_FREQ_SECTION=CASE WHEN ARFCN<100 OR ARFCN>=1000 THEN 'GSM900' ELSE 'GSM1800' END WHERE NCELL_FREQ_SECTION IS NULL";

		t1 = System.currentTimeMillis();
		log.debug(">>>>>>>>>>>>>准备根据邻区主频设置未匹配出来的邻区的频段信息，sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("根据邻区主频设置未匹配出来的邻区的频段影响行数：" + affectCnt);
		} catch (SQLException e) {
			log.error("根据邻区主频设置未匹配出来的邻区的频段操作失败！sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("根据邻区主频设置未匹配出来的邻区的频段时失败！");
			return result;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成根据邻区主频设置未匹配出来的邻区的频段信息。耗时：" + (t2 - t1) + "ms");

		return result;
	}

	public static class CellForMatch {
		int cellCnt = -1;
		Map<String, List<CellLonLat>> bcchBsicToInfo = null;
		Map<String, CellLonLat> cells = new HashMap<String, CellLonLat>();
		// 缓存的数据
		Map<String, List<String>> quickCached = new HashMap<String, List<String>>();// key为cell+"_bcch_bsic"，value为:0:ncell,1:ncells,2:dis

		public CellLonLat getCellInfo(String cell) {
			return cells == null ? null : cells.get(cell);
		}

		public List<String> getCachedMatchNcell(String key) {
			return quickCached.get(key);
		}

		public void putCachedMatchNcell(String key, List<String> ncellsInfo) {
			quickCached.put(key, ncellsInfo);
		}

		public List<CellLonLat> getBcchBsicCells(String key) {
			return bcchBsicToInfo == null ? null : bcchBsicToInfo.get(key);
		}

		public int getCellCnt() {
			return cellCnt;
		}

		public void setCellCnt(int cellCnt) {
			this.cellCnt = cellCnt;
		}

		public Map<String, CellLonLat> getCells() {
			return cells;
		}

		public void setCells(Map<String, CellLonLat> cells) {
			this.cells = cells;
		}
	}

	public static class CellLonLat {
		@DbValInject(dbField = "LABEL", type = "String")
		String cell;
		String name;
		@DbValInject(dbField = "LONGITUDE", type = "Double")
		double lon;
		@DbValInject(dbField = "LATITUDE", type = "Double")
		double lat;
		// CELL_BCCH,CELL_TCH,CELL_FREQ_CNT,CELL_INDOOR,CELL_LON,CELL_LAT,CELL_BEARING,CELL_DOWNTILT,CELL_SITE,CELL_FREQ_SECTION
		int bcch;
		String tch;
		@DbValInject(dbField = "INDOOR_CELL_TYPE", type = "String")
		String indoor;
		@DbValInject(dbField = "BEARING", type = "Double")
		double bearing;
		double downtilt;
		@DbValInject(dbField = "SITE", type = "String")
		String site;
		@DbValInject(dbField = "GSMFREQUENCESECTION", type = "String")
		String freqSection;

		@DbValInject(dbField = "LAYER", type = "String")
		String layer;
		//
		int freqCnt = -1;
		// 一开始就要准备好
		List<Integer> freqSet = null;

		public CellLonLat() {
		}

		public CellLonLat(String cell, String name, double lon, double lat,
				int bcch, String tch, String indoor, double bearing,
				double downtilt, String site, String freqSection) {
			this.cell = cell;
			this.name = name;
			this.lon = lon;
			this.lat = lat;
			this.bcch = bcch;
			this.tch = tch;
			this.indoor = indoor;
			this.bearing = bearing;
			this.downtilt = downtilt;
			this.site = site;
			this.freqSection = freqSection;

			Set<Integer> tss = new HashSet<Integer>();
			if (tch != null) {
				String[] ts = tch.split(",");
				for (String t : ts) {
					try {
						tss.add(Integer.parseInt(t));
					} catch (Exception e) {

					}
				}
			}
			tss.add(bcch);

			freqSet = new ArrayList(tss);
			Collections.sort(freqSet);// 排序
			freqCnt = freqSet.size();
		}

		// 计算频点数量
		public int getTotalFreqCnt() {
			return freqCnt;
		}
		public int getFreqCnt() {
			return freqCnt;
		}
		public void setFreqCnt(int freqCnt) {
			this.freqCnt = freqCnt;
		}
		// 计算与另一个小区的同频数量
		public int getCoFreqCnt(CellLonLat another) {
			int co = 0;
			if (another == null) {
				return co;
			}
			HashSet<Integer> tmp = new HashSet<Integer>(freqSet);
			tmp.retainAll(another.freqSet);
			return tmp.size();
		}

		// 计算与另一个小区的邻频数量
		public int getAdjFreqCnt(CellLonLat another) {
			int adj = 0;
			if (another == null) {
				return adj;
			}

			for (int i : freqSet) {
				for (int j : another.freqSet) {
					if ((i + 1) == j || (i - 1) == j) {
						adj++;
					}
					if (j > i) {
						break;
					}
				}
			}

			return adj;
		}

		public double getCellToAnoterCellDir(CellLonLat another) {
			double dir = 360;
			if (another == null) {
				return dir;
			}
			double lon2 = another.lon;
			double lat2 = another.lat;

			double dx = lon2 - lon;
			double dy = lat2 - lat;
			double fact = 57.29577951308232;// 57.29578049;

			if (dx == 0) {
				if (dy >= 0) {
					return 0;
				} else {
					return 180;
				}
			}
			if (dy == 0) {
				if (dx >= 0) {
					return 90;
				} else {
					return 270;
				}
			}
			// 第一象限
			if (dx > 0 && dy > 0) {
				dir = Math.atan2(dx, dy) * fact;
				return dir;
			} else if (dx > 0 && dy < 0) {
				dir = 180 - Math.abs(Math.atan2(dx, Math.abs(dy)) * fact);
				return dir;
			} else if (dx < 0 && dy < 0) {
				dir = Math.abs(Math.atan2(Math.abs(dx), Math.abs(dy)) * fact);
				dir += 180;
				return dir;
			} else if (dx < 0 && dy > 0) {
				return 360 - Math.abs(Math.atan2(Math.abs(dx), dy) * fact);
			}
			return 0;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCell() {
			return cell;
		}

		public void setCell(String cell) {
			this.cell = cell;
		}

		public double getLon() {
			return lon;
		}

		public void setLon(double lon) {
			this.lon = lon;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public int getBcch() {
			return bcch;
		}

		public void setBcch(int bcch) {
			this.bcch = bcch;
		}

		public String getTch() {
			return tch;
		}

		public void setTch(String tch) {
			this.tch = tch;
		}

		public String getIndoor() {
			return indoor;
		}

		public void setIndoor(String indoor) {
			this.indoor = indoor;
		}

		public double getBearing() {
			return bearing;
		}

		public void setBearing(double bearing) {
			this.bearing = bearing;
		}

		public double getDowntilt() {
			return downtilt;
		}

		public void setDowntilt(double downtilt) {
			this.downtilt = downtilt;
		}

		public String getSite() {
			return site;
		}

		public void setSite(String site) {
			this.site = site;
		}

		public String getFreqSection() {
			return freqSection;
		}

		public void setFreqSection(String freqSection) {
			this.freqSection = freqSection;
		}

		public String getLayer() {
			return layer;
		}

		public void setLayer(String layer) {
			this.layer = layer;
		}

		@Override
		public String toString() {
			return "CellLonLat [cell=" + cell + ", name=" + name + ", lon="
					+ lon + ", lat=" + lat + ", bcch=" + bcch + ", tch=" + tch
					+ ", indoor=" + indoor + ", bearing=" + bearing
					+ ", downtilt=" + downtilt + ", site=" + site
					+ ", freqSection=" + freqSection + ", layer=" + layer
					+ ", freqCnt=" + freqCnt + ", freqSet=" + freqSet + "]";
		}

	}

	public void clearMatchCellContext() {
		CellMainInfo.remove();
	}

	// 获取邻区匹配需要的数据
	public CellForMatch getMatchCellContext(Statement stmt, long city_id) {
		log.debug("进行内存邻区匹配");
		CellForMatch cfm = (CellForMatch) CellMainInfo.get();
		if (cfm == null) {
			cfm = new CellForMatch();
			CellMainInfo.set(cfm);
		}
		if (cfm.cellCnt < 0) {
			prepareMatchCellInfo(stmt, cfm);
		}
		log.debug("CellForMatch中缓存小区数据量大小为：" + cfm.cellCnt);
		return cfm;
	}

	/**
	 * 准备邻区匹配需要的数据
	 * 
	 * @param stmt
	 * @param cfm
	 * @author brightming 2014-9-15 下午4:16:12
	 */
	private void prepareMatchCellInfo(Statement stmt, CellForMatch cfm) {
		List<Map<String, Object>> cells = RnoHelper
				.commonQuery(
						stmt,
						"select cell,name,bsic,bcch,tch,longitude,latitude,CELL_FREQ_SECTION,DOWNTILT,BEARING,SITE,INDOOR_CELL_TYPE from rno_cell_city_t");
		int cnt = 0;
		cfm.cellCnt = 0;
		cfm.bcchBsicToInfo = new HashMap<String, List<CellLonLat>>();
		if (cells != null && cells.size() > 0) {
			String key;
			String cell, name, bsic, tch, cellFreqSection, site, indoor;
			int bcch = 0;
			Double lon, lat, downtilt, bearing;
			for (Map<String, Object> one : cells) {
				cell = one.get("CELL") == null ? null : one.get("CELL")
						.toString();
				if (cell == null) {
					continue;
				}
				name = one.get("NAME") == null ? "" : one.get("NAME")
						.toString();

				bsic = one.get("BSIC") == null ? null : one.get("BSIC")
						.toString();
				if (bsic == null) {
					continue;
				}
				if (one.get("BCCH") == null) {
					continue;
				}
				bcch = Integer.parseInt(one.get("BCCH").toString());

				lon = one.get("LONGITUDE") == null ? -1l : Double
						.parseDouble(one.get("LONGITUDE").toString());
				lat = one.get("LATITUDE") == null ? -1l : Double
						.parseDouble(one.get("LATITUDE").toString());

				tch = one.get("TCH") == null ? "" : one.get("TCH").toString();
				cellFreqSection = one.get("CELL_FREQ_SECTION") == null ? ""
						: one.get("CELL_FREQ_SECTION").toString();
				site = one.get("SITE") == null ? cell : one.get("SITE")
						.toString();
				indoor = one.get("INDOOR_CELL_TYPE") == null ? "N" : one.get(
						"INDOOR_CELL_TYPE").toString();
				downtilt = one.get("DOWNTILT") == null ? -1l : Double
						.parseDouble(one.get("DOWNTILT").toString());
				bearing = one.get("BEARING") == null ? 0l : Double
						.parseDouble(one.get("BEARING").toString());

				cnt++;
				//
				key = bcch + "_" + bsic;
				List<CellLonLat> clls = cfm.bcchBsicToInfo.get(key);
				if (clls == null) {
					clls = new ArrayList<CellLonLat>();
					cfm.bcchBsicToInfo.put(key, clls);
				}
				// bcch+bsic与小区对应关系
				CellLonLat oneCell = new CellLonLat(cell, name, lon, lat, bcch,
						tch, indoor, bearing, downtilt, site, cellFreqSection);
				clls.add(oneCell);
				// 小区
				cfm.cells.put(cell, oneCell);
			}
		}
		cfm.cellCnt = cnt;
	}

	private void printTemp1(Statement stmt, String ncsTabName, String pre) {
		String sql = "select * from " + ncsTabName;
		List<Map<String, Object>> tempdatas = RnoHelper.commonQuery(stmt, sql);
		if (tempdatas != null && tempdatas.size() > 0) {
			// for(Map<String,Object> one:tempdatas){
			// System.out.println(pre+"--temptable data ="+one);
			// }
			System.out.println(pre + "--temptable data size ="
					+ tempdatas.size());
		} else {
			System.err.println(pre + "------temptable contains no data!");
		}
	}

	/**
	 * 自动匹配bsc和频段信息
	 * 
	 * @param stmt
	 * @param ncsTabName
	 * @param cellTab
	 * @param descTable
	 * @param ncsId
	 * @return
	 * @author brightming 2014-8-19 下午12:46:21
	 */
	public ResultInfo matchEriNcsBscAndFreqSection(Statement stmt,
			String ncsTabName, String cellTab, String descTable, long ncsId) {

		ResultInfo result = new ResultInfo();
		result.setFlag(true);

		String sql = "select ncs.cell,cell.bsc,cell.CELL_FREQ_SECTION from"
				+ ncsTabName
				+ " ncs left join "
				+ cellTab
				+ " cell on ncs.cell=cell.cell order by cell.bsc,cell.CELL_FREQ_SECTION nulls last";
		sql = "select bsc,cell_freq_section,rownum  from "
				+ " ( "
				+ " select cell.bsc,cell.cell_freq_section,count(cell.bsc) from "
				+ ncsTabName + " ncs " + " left join " + cellTab
				+ " cell on ncs.cell=cell.cell and cell.bsc is not null "
				+ " group by cell.bsc,cell.cell_freq_section "
				+ " order by count(cell.bsc) desc " + " )where rownum=1";

		List<Map<String, Object>> res = RnoHelper.commonQuery(stmt, sql);
		if (res != null && res.size() > 0) {
			Map<String, Object> one = res.get(0);
			sql = "update " + descTable + " set bsc='" + one.get("BSC")
					+ "',FREQ_SECTION='" + one.get("CELL_FREQ_SECTION")
					+ "' where RNO_2G_ERI_NCS_DESC_ID=" + ncsId;
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				log.error("更新bsc和频段信息时出错！sql=" + sql);
				e.printStackTrace();
				result.setFlag(false);
				result.setMsg("更新bsc和频段信息时出错！");
			}
		}
		return result;
	}

	/**
	 * 准备c/i,c/a分子
	 * 
	 * @param statement
	 * @param ncsTable
	 * @param admRec
	 * @return
	 * @author brightming 2014-8-20 下午2:21:58
	 */
	public ResultInfo prepareCIAndCADivider(Statement statement,
			String ncsTable, NcsAdmRecord admRec) {
		ResultInfo result = new ResultInfo();
		result.setFlag(true);

		if (admRec == null) {
			result.setFlag(false);
			result.setMsg("爱立信NCS文件中管理记录为空！");

			return result;
		}
		String c_i = admRec.getRelssValueName(-12);
		if (StringUtils.isBlank(c_i)) {
			c_i = admRec.getRelssValueName(0);
		}
		String c_a = admRec.getRelssValueName(3);
		if (StringUtils.isBlank(c_a)) {
			c_a = admRec.getRelssValueName(0);
		}
		if (StringUtils.isBlank(c_i) || StringUtils.isBlank(c_a)) {
			log.error("无法找到-12，+3对应的门限参数！");
			result.setFlag(false);
			result.setMsg("无法找到-12，+3对应的门限参数！");
			return result;
		}

		// 2014-9-4 不对分子做任何特殊处理
		String sql = "update " + ncsTable + " set "
		// +
		// "CI_DIVIDER=case when CELL_INDOOR=NCELL_INDOOR AND CELL_FREQ_SECTION=NCELL_FREQ_SECTION then TIMES"
				+ "CI_DIVIDER=TIMES" + c_i
				// + " ELSE 0 end,"
				// +
				// "CA_DIVIDER=case when CELL_INDOOR=NCELL_INDOOR AND CELL_FREQ_SECTION=NCELL_FREQ_SECTION then TIMES"
				+ ",CA_DIVIDER=TIMES" + c_a;
		// + " ELSE 0 end";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			log.error("计算同频干扰系数、邻频干扰系数的分子时出错！sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("计算同频干扰系数、邻频干扰系数的分子时出错！");
		}
		return result;
	}

	/**
	 * 华为ncs中的邻区匹配
	 * 
	 * @param stmt
	 * @param string
	 * @param string2
	 * @param cityId
	 * @return
	 * @author brightming 2014-8-23 下午6:45:29
	 */
	public ResultInfo matchHwNcsNcell(Statement stmt, String ncsTabName,
			String cellTab, long cityId) {

		/*
		 * String sql="";
		 * 
		 * // 先得到bsic sql = "update " + ncsTabName + " set BSIC=ncc||bcc";
		 * log.debug("华为ncs计算bsic sql=" + sql); try { long
		 * affct=stmt.executeUpdate(sql); log.debug("更新华为bsic的数量："+affct); }
		 * catch (SQLException e) { log.error("计算华为ncs的bsic失败！sql=" + sql);
		 * ResultInfo result = new ResultInfo(); result.setFlag(false);
		 * result.setMsg("计算华为ncs的bsic失败！"); e.printStackTrace(); return result;
		 * }
		 */

		return fullfillHw2GCellAndNcellInfo(stmt, ncsTabName, cellTab, cityId);

		// return matchEriNcsNcell(stmt, ncsTabName, cellTab, cityId);
	}

	/**
	 * 自动匹配bsc和频段
	 * 
	 * @param stmt
	 * @param string
	 * @param string2
	 * @return
	 * @author brightming 2014-8-23 下午6:46:05
	 */
	public ResultInfo matchHwNcsBscAndFreqSection(Statement stmt,
			String ncsTabName, String cellTab) {
		// 这里不需要，因为自带了bsc名称，而频段信息在邻区匹配的时候也一并做了处理
		ResultInfo result = new ResultInfo();
		result.setFlag(true);
		return result;
	}

	/**
	 * 计算华为ncs的c/i,c/a分子
	 * 
	 * @param stmt
	 * @param string
	 * @return
	 * @author brightming 2014-8-23 下午6:46:39
	 */
	public ResultInfo prepareHwCIAndCADivider(Statement stmt, String ncsTab) {

		String sql = "update " + ncsTab
				+ " set CI_DIVIDER=S361-S369,CA_DIVIDER=S361-S366";
		ResultInfo result = new ResultInfo();
		result.setFlag(true);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			log.error("计算华为ncs的C/I,C/A分子失败！sql=" + sql);
			result.setFlag(false);
			result.setMsg("计算华为ncs的C/I,C/A分子失败！");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 生成华为ncs描述信息
	 * 
	 * @param conn
	 * @param stmt
	 * @param ncsTab
	 * @param cityId
	 * @return
	 * @author brightming 2014-8-24 下午3:06:44
	 */
	public ResultInfo generateHwNcsDescRec(Connection conn, Statement stmt,
			String ncsTab, long cityId) {

		ResultInfo result = new ResultInfo();
		result.setFlag(true);

		DateUtil dateUtil=new DateUtil();
		String sql = "";
		String presql = "insert into RNO_2G_HW_NCS_DESC "
				+ "(ID,MEA_TIME,PERIOD,BSC,RECORD_COUNT,CITY_ID,STATUS,CREATE_TIME,MOD_TIME) values(?,?,?,?,?,"
				+ cityId + ",'N',sysdate,sysdate)";

		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(presql);
		} catch (SQLException e1) {
			log.error("准备华为ncs描述信息的数据库资源失败！presql=" + presql);
			e1.printStackTrace();
			result.setFlag(false);
			result.setMsg("准备华为ncs描述信息的数据库资源失败");
			return result;
		}
		// 搜索出需要的数据
		sql = "SELECT bsc_name,to_char(mea_time,'yyyy-mm-dd hh24:mi:ss') mea_time,period,count(bsc_name) as cnt FROM RNO_2G_HW_NCS_T GROUP BY mea_time,period,bsc_name";
		ResultSet rs = null;
		log.debug("获取华为ncs测量描述信息的sql=" + sql);
		List<Map<String, Object>> rawDescs = RnoHelper.commonQuery(stmt, sql);
		String bscName, meaTime;
		Integer period, count;
		long seqId = -1;
		int index = 1;
		Map<Long, List<String>> seqToBscAndMeats = new HashMap<Long, List<String>>();
		List<String> bscandmeat = null;
		if (rawDescs != null && rawDescs.size() > 0) {
			for (Map<String, Object> one : rawDescs) {
				bscandmeat = new ArrayList<String>();
				index = 1;
				bscName = (String) one.get("BSC_NAME");
				meaTime = (String) one.get("MEA_TIME");
				if (one.get("PERIOD") != null) {
					period = Integer.parseInt(one.get("PERIOD").toString());
				} else {
					period = -1;// (Integer)one.get("PERIOD");
				}
				count = Integer.parseInt(one.get("CNT").toString());

				seqId = RnoHelper.getNextSeqValue("SEQ_RNO_2G_HW_NCS_DESC_ID",
						stmt);
				try {
					// ID,MEA_TIME,PERIOD,BSC_NAME,RECORD_COUNT
					pstmt.setLong(index++, seqId);
					pstmt.setTimestamp(index++, new java.sql.Timestamp(dateUtil.parseDateArbitrary(meaTime).getTime()));
					pstmt.setInt(index++, period);
					pstmt.setString(index++, bscName);
					pstmt.setInt(index++, count);

					pstmt.addBatch();
				} catch (SQLException e) {
					e.printStackTrace();
					continue;
				}

				bscandmeat.add(bscName);
				bscandmeat.add(meaTime);
				seqToBscAndMeats.put(seqId, bscandmeat);
			}
			try {
				pstmt.executeBatch();
			} catch (Exception e) {
				log.error("批量插入华为ncs的描述信息失败！");
				e.printStackTrace();
				result.setFlag(false);
				result.setMsg("批量插入华为ncs的描述信息失败！");
			} finally {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		// 用得到的seqid更新ncstab
//		presql = "update " + ncsTab
//				+ " set RNO_2GHWNCS_DESC_ID=? where bsc_name=? and to_char(mea_time,'yyyy-mm-dd hh24:mi:ss')=?";
//		try {
//			pstmt = conn.prepareStatement(presql);
//		} catch (SQLException e1) {
//			log.error("准备更新华为ncs表的描述id时失败！presql=" + presql);
//			e1.printStackTrace();
//			result.setFlag(false);
//			result.setMsg("准备更新华为ncs表的描述id失败");
//			return result;
//		}

		for (long sid : seqToBscAndMeats.keySet()) {
			index = 1;
			bscandmeat = seqToBscAndMeats.get(sid);
			bscName = bscandmeat.get(0);
			meaTime = bscandmeat.get(1);
			
			sql="update " + ncsTab
					+ " set RNO_2GHWNCS_DESC_ID="+sid+" where bsc_name='"+bscName+"' and to_char(mea_time,'yyyy-mm-dd hh24:mi:ss')='"+meaTime+"'";
			try {
				int affcnt=stmt.executeUpdate(sql);
				log.debug("更新华为bsc="+bscName+",meaTime="+meaTime+"的描述id，影响数量："+affcnt);
			} catch (SQLException e1) {
				e1.printStackTrace();
				result.setFlag(false);
				result.setMsg("更新华为ncs表的描述id失败");
				
				
				return result;
			}
			
//			try {
//				pstmt.setLong(index++, seqId);
//				pstmt.setString(index++, bscName);
////				pstmt.setTimestamp(index++, new java.sql.Timestamp(dateUtil
////						.parseDateArbitrary(meaTime).getTime()));
//				pstmt.setString(index++, meaTime);
//				pstmt.addBatch();
//
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
		}
//		try {
//			pstmt.executeBatch();
//		} catch (SQLException e) {
//			log.error("更新华为ncs的描述id失败！");
//			e.printStackTrace();
//			result.setFlag(false);
//			result.setMsg("更新华为ncs的描述id失败！");
//		} finally {
//			try {
//				pstmt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
		

		return result;
	}

	/**
	 * 生成华为ncs的描述id
	 * 
	 * @param rateType
	 *            FULL,HALF
	 * @param mrrtab
	 * @param descTab
	 * @param stmt
	 * @param conn
	 * @param cityId
	 * @return
	 * @author brightming 2014-8-25 下午6:03:56
	 */
	public ResultInfo generateHwMrrDesc(String rateType, String mrrtab,
			String descTab, Statement stmt, Connection conn, long cityId) {
		ResultInfo result = new ResultInfo();
		result.setFlag(true);
		// MEA_DATE
		String sql = "";
		String presql = "insert into  "
				+ descTab
				+ "(MRR_DESC_ID,MEA_DATE,BSC,CELL_NUM,STATUS,CREATE_TIME,MOD_TIME,CITY_ID,RATE_TYPE) values(?,?,?,?"
				+ ",'N',sysdate,sysdate," + cityId + ",'" + rateType + "')";
		log.debug("2G华为MRR准备sql:" + presql);
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(presql);
		} catch (SQLException e1) {
			log.error("准备华为ncs描述信息的数据库资源失败！presql=" + presql);
			e1.printStackTrace();
			result.setFlag(false);
			result.setMsg("准备华为mrr描述信息的数据库资源失败");
			return result;
		}
		// 搜索出需要的数据
		sql = "SELECT bsc,to_char(mea_date,'yyyy-MM-dd hh24:mi:ss') mea_date,count(bsc) as cnt FROM "
				+ mrrtab + " GROUP BY bsc,mea_date";
		ResultSet rs = null;
		log.debug("获取华为mrr测量描述信息的sql=" + sql);
		try {
			printTmpTable1(sql, conn.createStatement());
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<Map<String, Object>> rawDescs = RnoHelper.commonQuery(stmt, sql);
		log.debug("rawDescs:" + rawDescs.size());
		String bscName, meaTime;
		Integer count;
		long seqId = -1;
		int index = 1;
		Map<Long, List<String>> seqToBscAndMeats = new HashMap<Long, List<String>>();
		List<String> bscandmeat = null;
		DateUtil dateUtil=new DateUtil();
		if (rawDescs != null && rawDescs.size() > 0) {
			for (Map<String, Object> one : rawDescs) {
				bscandmeat = new ArrayList<String>();
				index = 1;
				bscName = (String) one.get("BSC");
				meaTime = (String) one.get("MEA_DATE");
				count = Integer.parseInt(one.get("CNT").toString());

				seqId = RnoHelper.getNextSeqValue("SEQ_RNO_2G_HW_MRR_DESC",
						stmt);
				try {
					// MRR_DESC_ID,MEA_DATE,BSC,CELL_NUM,STATUS,CREATE_TIME,MOD_TIME,CITY_ID
					pstmt.setLong(index++, seqId);
					pstmt.setDate(index++, new java.sql.Date(dateUtil
							.parseDateArbitrary(meaTime).getTime()));
					pstmt.setString(index++, bscName);
					pstmt.setInt(index++, count);
					// log.debug("seqId,bscName,count"+seqId+"--"+bscName+"--"+count);
					pstmt.addBatch();
				} catch (SQLException e) {
					e.printStackTrace();
					continue;
				}

				bscandmeat.add(bscName);
				bscandmeat.add(meaTime);
				seqToBscAndMeats.put(seqId, bscandmeat);
			}
			try {
				pstmt.executeBatch();
				log.debug("批量插入华为mrr的描述信息成功！");
			} catch (Exception e) {
				log.error("批量插入华为mrr的描述信息失败！");
				e.printStackTrace();
				result.setFlag(false);
				result.setMsg("批量插入华为mrr的描述信息失败！");
			} finally {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		// 用得到的seqid更新mrrtab
		presql = "update " + mrrtab
				+ " set RNO_2G_HW_MRR_DESC_ID=? where bsc=? and mea_date=?";
		try {
			pstmt = conn.prepareStatement(presql);
		} catch (SQLException e1) {
			log.error("准备更新华为mrr表的描述id时失败！presql=" + presql);
			e1.printStackTrace();
			result.setFlag(false);
			result.setMsg("准备更新华为mrr表的描述id失败");
			return result;
		}

		for (long sid : seqToBscAndMeats.keySet()) {
			index = 1;
			bscandmeat = seqToBscAndMeats.get(sid);
			bscName = bscandmeat.get(0);
			meaTime = bscandmeat.get(1);

			try {
				pstmt.setLong(index++, sid);
				pstmt.setString(index++, bscName);
				pstmt.setDate(index++, new java.sql.Date(dateUtil
						.parseDateArbitrary(meaTime).getTime()));
				pstmt.addBatch();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			pstmt.executeBatch();
		} catch (SQLException e) {
			log.error("更新华为mrr的描述id失败！");
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("更新华为mrr的描述id失败！");
		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 检查爱立信mrr文件对应区域是否符合要求
	 * 
	 * @param mrrId
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-7-23上午11:47:26
	 */
	public boolean checkMrrArea(Connection connection, long mrrId, long cityId) {
		if (connection == null) {
			log.error("matchMrrBsc的参数中未提供有效的数据库连接！");
			return false;
		}
		String sql = "select rno_bsc_rela_area.area_id "
				+ "    from rno_bsc_rela_area "
				+ "  where rno_bsc_rela_area.bsc_id in "
				+ "         (select bsc_id "
				+ "            from (select eri_mrr_desc_id, "
				+ "                      bsc_id, "
				+ "                      rank() over(partition by eri_mrr_desc_id order by cnt desc) as seq "
				+ "                 from (select eri_mrr_desc_id, bsc_id, count(*) as cnt "
				+ "                        from (select mid.eri_mrr_desc_id, "
				+ "                                     cell.bsc_id "
				+ "                        from (select eri_mrr_desc_id, "
				+ "                                     cell_name as cell_name "
				+ "                              from RNO_ERI_MRR_TA_TEMP "
				+ "                              where eri_mrr_desc_id = "+mrrId+") mid "
				+ "                             inner join cell on (mid.cell_name = "
				+ "                                                     cell.label)) "
				+ "                            group by eri_mrr_desc_id, bsc_id "
				+ "                           order by eri_mrr_desc_id, cnt desc nulls last)) "
				+ "           where seq = 1) ";
		log.info("checkMrrArea的sql：" + sql);
		Statement stmt = null;
		try {
			// 按照缺省方式打开的ResultSet不支持结果集cursor的回滚
			// 如果想要完成操作，要在生成Statement对象时加入如下两个参数
			stmt = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		//从系统数据库获取区域id集
		String areaStr = AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
		String areas[] = areaStr.split(",");
		try {
			ResultSet rs = stmt.executeQuery(sql);
//			rs.last(); // 移到最后一行
//			int length = rs.getRow();
//			if (length > 0) {
//				return true;
//			}
			while (rs.next()) {
				long areaId = rs.getLong(1);
				for (String area : areas) {
					if(areaId == Long.parseLong(area)) {
						return true;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return false;
	}
	
	/**
	 * 检查爱立信fas文件对应区域是否符合要求
	 * 
	 * @param fasId
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2015年1月16日17:30:18
	 */
	public boolean checkEriFasArea(Connection connection, long mrrId, long cityId) {
		if (connection == null) {
			log.error("matchMrrBsc的参数中未提供有效的数据库连接！");
			return false;
		}
		String sql = "select rno_bsc_rela_area.area_id "
				+ "    from rno_bsc_rela_area "
				+ "  where rno_bsc_rela_area.bsc_id in "
				+ "         (select bsc_id"
				+ "            from (select bsc_id, cnt, rownum rn "
				+ "                 from (select bsc_id, count(*) as cnt "
				+ "                        from (select cell.bsc_id from " 
				+ "								   (select cell as cell_name "
				+ "                                from RNO_ERI_FAS_CELL_DATA_T ) mid "
				+ "                              inner join cell on (mid.cell_name " 
				+ "                                                     = cell.label)) "
				+ "                              group by bsc_id )"
				+ "					  order by cnt desc nulls last) "
				+ "				where rn=1) ";
		log.info("checkEriFasArea的sql：" + sql);
		Statement stmt = null;
		try {
			// 按照缺省方式打开的ResultSet不支持结果集cursor的回滚
			// 如果想要完成操作，要在生成Statement对象时加入如下两个参数
			stmt = connection.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		//从系统数据库获取区域id集
		String areaStr = AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(cityId);
		String areas[] = areaStr.split(",");
		try {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				long areaId = rs.getLong(1);
				for (String area : areas) {
					if(areaId == Long.parseLong(area)) {
						return true;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return false;
	}
	
	/**
	 * 检查华为mrr文件对应区域是否符合要求
	 * @param connection
	 * @param context
	 * @return
	 * @author peng.jm
	 * @date 2014-12-8下午01:56:26
	 */
	public boolean checkHwMrrArea(Connection connection,
			MrrParserContext context) {
		if (connection == null) {
			log.error("matchMrrBsc的参数中未提供有效的数据库连接！");
			return false;
		}
		//从系统数据库获取区域id集
		String areaStr = AuthDsDataDaoImpl.getSubAreaAndSelfIdListStrByParentId(context.getCityId());
		String areas[] = areaStr.split(",");
		String tempTable = "";
		if(("hrate").equals(context.getRateType())) {
			tempTable = "RNO_2G_HW_MRR_HRATE_T";
		}
		if(("frate").equals(context.getRateType())) {
			tempTable = "RNO_2G_HW_MRR_FRATE_T";
		}
		String sql = " select area_id "+
			" 	  from (select area_id,count(area_id) as cnt  "+
			" 		       from (select t1.area_id "+
			" 		             from "+tempTable+" t "+
			" 		             left join (select bsc.engname, rela.area_id "+
			" 		                         from rno_bsc bsc "+
			" 		                         left join rno_bsc_rela_area rela  "+
			" 		                         on bsc.bsc_id = rela.bsc_id) t1 "+
			" 		             on t.bsc = t1.engname) "+
			" 		       group by area_id) "+
			" 		  order by cnt desc  nulls last ";
		log.info("checkHwMrrArea的sql：" + sql);
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		try {
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			long areaId = rs.getLong("AREA_ID");
			for (String area : areas) {
				if(areaId == Long.parseLong(area)) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return false;
	}
	/**
	 * 自动匹配mrr文件对应的bsc
	 * 
	 * @param connection
	 * @param mrrTabName
	 *            mrr信号质量记录表名称
	 * @param mrrDescTabName
	 *            mrr描述表名称，将被更新
	 * @param mrrId
	 * @author peng.jm
	 * @date 2014-7-23上午09:44:58
	 */
	public ResultInfo matchMrrBsc(Connection connection, String mrrTabName,
			String mrrDescTabName, String mrrId) {

		ResultInfo result = new ResultInfo();
		result.setFlag(true);

		if (connection == null) {
			log.error("matchMrrBsc的参数中未提供有效的数据库连接！");
			result.setMsg("参数中未提供有效的数据库连接");
			result.setFlag(false);
		}
		if (mrrId == null || mrrId.isEmpty()) {
			log.error("未指明mrr范围，不执行匹配。");
			result.setMsg("未指明mrr范围，不执行匹配");
			result.setFlag(false);
		}

		String sql = "select eri_mrr_desc_id,bsc from "
				+ " ( select eri_mrr_desc_id,bsc,rank() over (partition by eri_mrr_desc_id order by cnt desc) as seq from "
				+ " ( select eri_mrr_desc_id,bsc,count(*) as cnt from "
				+ " ( select mid2.eri_mrr_desc_id,rno_bsc.engname as bsc from "
				+ "              ( select mid.eri_mrr_desc_id,mid.cell_name,cell.bsc_id from "
				+ "                     (select eri_mrr_desc_id,cell_name as cell_name from "
				+ mrrTabName
				+ " where eri_mrr_desc_id = "
				+ mrrId
				+ ") mid "
				+ "                  inner join cell on(mid.cell_name=cell.label) "
				+ "               ) mid2 "
				+ "            inner join rno_bsc on(mid2.bsc_id = rno_bsc.bsc_id) "
				+ "           ) " + "      group by  eri_mrr_desc_id,bsc "
				+ "      order by eri_mrr_desc_id,cnt desc nulls last "
				+ "    ) " + ")  " + " where seq = 1";
		sql = "merge into " + mrrDescTabName + " tar using (" + sql
				+ ") src on(tar.eri_mrr_desc_id=src.eri_mrr_desc_id) "
				+ " when matched then update set tar.bsc=src.bsc";
		log.debug("更新mrr描述表的bsc的sql=" + sql);
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("匹配MRR的BSC信息失败！");
		}
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return result;
	}
	
	/**
	 * 自动匹配fas文件对应的bsc
	 * @param connection
	 * @param fasTabName
	 * @param fasDescTabName
	 * @param fasId
	 * @author peng.jm
	 * @date 2015年1月19日10:26:01
	 */
	public ResultInfo matchFasBsc(Connection connection, String fasTabName,
			String fasDescTabName, String fasId) {
		ResultInfo result = new ResultInfo();
		result.setFlag(true);

		if (connection == null) {
			log.error("matchFasBsc的参数中未提供有效的数据库连接！");
			result.setMsg("参数中未提供有效的数据库连接");
			result.setFlag(false);
		}
		if (fasId == null || fasId.isEmpty()) {
			log.error("未指明fas范围，不执行匹配。");
			result.setMsg("未指明fas范围，不执行匹配");
			result.setFlag(false);
		}

		String sql = "select fas_desc_id,bsc from "
				+ " ( select fas_desc_id,bsc,rank() over (partition by fas_desc_id order by cnt desc) as seq from "
				+ "       ( select fas_desc_id,bsc,count(*) as cnt from "
				+ "           ( select mid2.fas_desc_id,rno_bsc.engname as bsc from "
				+ "              ( select mid.fas_desc_id,mid.cell_name,cell.bsc_id from "
				+ "                     (select "+fasId+" as fas_desc_id,cell as cell_name from "
											+ fasTabName + ") mid "
				+ "                  inner join cell on(mid.cell_name=cell.label)) mid2 "
				+ "            inner join rno_bsc on(mid2.bsc_id = rno_bsc.bsc_id)) " 
				+ "  		 group by fas_desc_id,bsc "
				+ "          order by cnt desc nulls last))  " 
				+ " where seq = 1";
		sql = "merge into " + fasDescTabName + " tar using (" + sql
				+ ") src on(tar.fas_desc_id = src.fas_desc_id) "
				+ " when matched then update set tar.bsc = src.bsc";
		log.debug("更新fas描述表的bsc的sql=" + sql);
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("匹配FAS的BSC信息失败！");
		}
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
		return result;
	}

	/**
	 * 填充ncs测量小区与邻区的信息
	 * 
	 * @param stmt
	 * @param ncsTabName
	 * @param cellTab
	 * @param cityId
	 * @return
	 * @author brightming 2014-9-15 下午5:41:12
	 */
	public ResultInfo fullfillHw2GCellAndNcellInfo(Statement stmt,
			String ncsTabName, String cellTab, long cityId) {
		String sql = "";

		ResultInfo result = new ResultInfo();
		result.setFlag(true);

		log.debug("进入方法：fullfillHw2GCellAndNcellInfo。stmt=" + stmt
				+ ",tabName=" + ncsTabName);
		if (stmt == null || StringUtils.isBlank(ncsTabName)
				|| StringUtils.isBlank(cellTab)) {
			log.error("dao fullfillHw2GCellAndNcellInfo方法的参数不能为空！conn=" + stmt
					+ ",tabName=" + ncsTabName + ",cellTab=" + cellTab);
			result.setFlag(false);
			result.setMsg("填充小区与邻区相关信息时，需要的参数不齐全！");
			return result;
		}

		sql = "";
		int affectCnt = 0;

		long t1 = System.currentTimeMillis();
		long t2 = System.currentTimeMillis();

		// ---完善小区属性---//
		sql = "merge into "
				+ ncsTabName
				+ " tar using "
				+ cellTab
				+ " src on(tar.cell=src.cell) when matched "
				+ " then update set TAR.CELL_BCCH=SRC.BCCH,TAR.CELL_TCH=SRC.TCH,tar.CELL_FREQ_CNT=SRC.CELL_FREQ_CNT,"
				+ "TAR.CELL_INDOOR=SRC.INDOOR_CELL_TYPE,"
				+ "TAR.CELL_LON=SRC.LONGITUDE,TAR.CELL_LAT=SRC.LATITUDE,"
				+ "TAR.CELL_BEARING=SRC.BEARING,TAR.CELL_DOWNTILT=SRC.DOWNTILT,"
				+ "TAR.CELL_SITE=SRC.SITE,TAR.CELL_FREQ_SECTION=SRC.CELL_FREQ_SECTION";
		t1 = System.currentTimeMillis();
		log.debug(">>>>>>>>>>>>准备完善小区的信息。sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("完善小区的信息，共有：" + affectCnt);
		} catch (SQLException e) {
			log.error("完善小区的信息时失败，sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配时，完善小区的信息时失败！");
			return result;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<完成完善小区的信息信息。耗时：" + (t2 - t1) + "ms");

		// ---完善邻区属性---//
		sql = "merge into "
				+ ncsTabName
				+ " tar using "
				+ cellTab
				+ " src on(tar.ncell=src.cell) when matched "
				+ " then update set TAR.NCELL_TCH=SRC.TCH,tar.NCELL_FREQ_CNT=SRC.CELL_FREQ_CNT,"
				+ "TAR.NCELL_INDOOR=SRC.INDOOR_CELL_TYPE,"
				+ "TAR.NCELL_LON=SRC.LONGITUDE,TAR.NCELL_LAT=SRC.LATITUDE,"
				+ "TAR.NCELL_SITE=SRC.SITE,TAR.NCELL_FREQ_SECTION=SRC.CELL_FREQ_SECTION";
		t1 = System.currentTimeMillis();
		log.debug(">>>>>>>>>>>>准备完善邻区的信息。sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("完善邻区的信息，共有：" + affectCnt);
		} catch (SQLException e) {
			log.error("完善邻区的信息时失败，sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("邻区匹配时，完善邻区的信息时失败！");
			return result;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<完成完善邻区的信息信息。耗时：" + (t2 - t1) + "ms");

		// ----------------更新同频数、邻频数、cell到ncell的夹角（正北方向为0）----------------------//
		t1 = t2;
		sql = "update "
				+ ncsTabName
				+ " set same_freq_cnt=RNO_GETCOFREQCNT(CELL_BCCH||','||CELL_TCH,ARFCN||','||NCELL_TCH),adj_freq_cnt=RNO_GETADJACENTFREQCNT(CELL_BCCH||','||CELL_TCH,ARFCN||','||NCELL_TCH),CELL_TO_NCELL_DIR=calculateDirToNorth(CELL_LON,CELL_LAT,NCELL_LON,NCELL_LAT)";
		log.debug(">>>>>>>>>>>>>准备更新已有邻区的ncs记录的距离、同频数、邻频数信息，sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("更新同频数、邻频数、cell到ncell的夹角（正北方向为0）影响行数：" + affectCnt);
		} catch (SQLException e) {
			log.error("更新同频数、邻频数、cell到ncell的夹角（正北方向为0）的操作失败！sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("填充小区与邻区相关信息时，更新同频数、邻频数、cell到ncell的夹角（正北方向为0）时失败！");
			return result;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成更新已有邻区的ncs记录的同频数、邻频数信息。耗时：" + (t2 - t1)
				+ "ms");
		// ---2014-9-4 最后面有邻区没有匹配出来的，但是其频段是可以根据其主频确定的
		sql = "update "
				+ ncsTabName
				+ " set NCELL_FREQ_SECTION=CASE WHEN ARFCN<100 OR ARFCN>=1000 THEN 'GSM900' ELSE 'GSM1800' END WHERE NCELL_FREQ_SECTION IS NULL";

		t1 = System.currentTimeMillis();
		log.debug(">>>>>>>>>>>>>准备根据邻区主频设置未匹配出来的邻区的频段信息，sql=" + sql);
		try {
			affectCnt = stmt.executeUpdate(sql);
			log.debug("根据邻区主频设置未匹配出来的邻区的频段影响行数：" + affectCnt);
		} catch (SQLException e) {
			log.error("根据邻区主频设置未匹配出来的邻区的频段操作失败！sql=" + sql);
			e.printStackTrace();
			result.setFlag(false);
			result.setMsg("根据邻区主频设置未匹配出来的邻区的频段时失败！");
			return result;
		}
		t2 = System.currentTimeMillis();
		log.debug("<<<<<<<<<<<<<完成根据邻区主频设置未匹配出来的邻区的频段信息。耗时：" + (t2 - t1) + "ms");

		return result;
	}

	private void printTmpTable1(String sql, Statement stmt) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> res = RnoHelper.commonQuery(stmt, sql);
		if (res != null && res.size() > 0) {
			log.debug("\r\n---数据如下：");
			for (Map<String, Object> one : res) {
				log.debug(one);
			}
			log.debug("---数据展示完毕\r\n");
		} else {
			log.debug("---数据为空！");
		}
	}

	public static void main(String[] args) {
		CellLonLat cell1 = new CellLonLat("48DF144", "48DF144", 114.0388,
				22.5695, 568, "568,523,548,552,599,515", "N", 265, 10, "cell1",
				"GSM900");
		CellLonLat cell2 = new CellLonLat("48DF123", "48DF123", 114.0285,
				22.5657, 572, "572,539,599,528,614", "N", 0.1, -1, "cell1",
				"GSM900");

		System.out.println(cell1.getTotalFreqCnt());
		System.out.println(cell2.getTotalFreqCnt());
		System.out.println(cell1.getCoFreqCnt(cell2));
		System.out.println(cell1.getAdjFreqCnt(cell2));
		System.out.println(cell1.getCellToAnoterCellDir(cell2));

		System.out.println(LatLngHelper.Distance(cell1.lon, cell1.lat,
				cell2.lon, cell2.lat) / 1000);
	}

	/**
	 * 根据小区基础信息，计算小区理想覆盖距离
	 * 
	 * @param stmt
	 * @param cityId
	 * @param coverAngle
	 *            服务小区覆盖方向的多少度覆盖范围，默认120
	 * @param refCellCnt
	 *            参考的临近基站数量，默认3
	 * @param force
	 *            是否强制计算
	 * @return
	 * @author brightming 2014-9-22 上午11:45:00
	 */
	public ResultInfo calculateCellIdealDis(Connection conn, long cityId,
			int coverAngle, int refCellCnt, boolean force) {

		// 先获取锁
		synchronized (RnoStructAnaV2Impl.class) {
			ResultInfo result = new ResultInfo(true);

			List<Long> subAreas=AuthDsDataDaoImpl.getSubAreaIdsByCityId(cityId);
			
			String areaStrs = "";
			for (Long id : subAreas) {
				areaStrs += id + ",";
			}
			if (areaStrs.length() > 0) {
				areaStrs = areaStrs.substring(0, areaStrs.length()-1);
			}
			if(areaStrs.length()==0){
				result.setFlag(false);
				result.setMsg("该city下无小区数据。");
				return result;
			}
			String sql = "SELECT LABEL,"
					+ " (CASE WHEN GSMFREQUENCESECTION IS NULL THEN (CASE WHEN BCCH IS NOT NULL THEN (CASE WHEN BCCH>100 AND BCCH<1000 THEN 'GSM1800' ELSE 'GSM900' END) ELSE NULL END) ELSE GSMFREQUENCESECTION END) AS GSMFREQUENCESECTION,"
					+ " BEARING, "
					+ " LONGITUDE, "
					+ " LATITUDE, "
					+ " SITE, "
					+ " (CASE WHEN INDOOR_CELL_TYPE='室内覆盖' THEN 'Y' ELSE 'N' END) AS INDOOR_CELL_TYPE"
					+ " FROM  ("
					+ " SELECT LABEL,NAME,GSMFREQUENCESECTION,LAC,CI,BSIC,BCCH,TCH,DOWNTILT,BEARING,LONGITUDE,LATITUDE,SITE,BSC_ID,BTSTYPE,INDOOR_CELL_TYPE,ROW_NUMBER() OVER(PARTITION BY LABEL ORDER BY LABEL NULLS LAST) RN "
					+ " FROM CELL WHERE CELL.AREA_ID IN (" + areaStrs+")"
					+ ") where rn=1 ";

			Statement stmt = null;
			try {
				stmt = conn.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
				result.setFlag(false);
				result.setMsg("创建数据库执行器失败。");
				return result;
			}

			// 如果已经有结果，则不需要进行计算了。
			if (!force) {
				ResultSet rs = null;
				long already = 0;
				try {
					rs = stmt
							.executeQuery("select count(*) cnt from RNO_CELL_IDEALDIS where city_id="
									+ cityId
									+ " and COVER_ANGLE="
									+ coverAngle
									+ " and REF_CNT=" + refCellCnt);
					if (rs != null) {
						while (rs.next()) {
							already = rs.getLong(1);
							break;
						}
					}
					if (already > 0) {
						log.debug("已经存在理想覆盖距离，不进行计算。");
						return result;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}

			// 保存计算结果
			String preSql = "insert into RNO_CELL_IDEALDIS(CITY_ID,CELL,IDEAL_DIS,COVER_ANGLE,REF_CNT) VALUES("
					+ cityId + ",?,?," + coverAngle + "," + refCellCnt + ")";
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(preSql);
			} catch (SQLException e) {
				e.printStackTrace();
				result.setFlag(false);
				result.setMsg("创建数据库执行器失败。");
				return result;
			}

			List<Map<String, Object>> cells = RnoHelper.commonQuery(stmt, sql);

			// 执行计算
			Map<String, Double> cellDis = doCalculateCellDis(cells);

			// 清除旧的数据
			try {
				stmt.executeUpdate("delete from RNO_CELL_IDEALDIS where COVER_ANGLE="
						+ coverAngle
						+ " and REF_CNT="
						+ refCellCnt
						+ " and city_id=" + cityId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// 保存结果
			int cnt = 0;
			for (String label : cellDis.keySet()) {
				// CELL,IDEAL_DIS
				try {
					pstmt.setString(1, label);
					pstmt.setDouble(2, cellDis.get(label));
					pstmt.addBatch();
					cnt++;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (cnt > 5000) {
					try {
						pstmt.executeBatch();
						pstmt.clearBatch();
						cnt = 0;
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			if (cnt > 0) {
				try {
					pstmt.executeBatch();
					pstmt.clearBatch();
					cnt = 0;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			//
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	/**
	 * 计算所有小区的理想覆盖距离
	 * 
	 * @param cells
	 * @return
	 * @author brightming 2014-9-25 下午2:26:07
	 */
	private Map<String, Double> doCalculateCellDis(
			List<Map<String, Object>> cells) {
		// 不同的layer的小区，放不同的队列里
		Map<String, List<CellLonLat>> cellInlayers = new HashMap<String, List<CellLonLat>>();
		List<CellLonLat> oneLayerCells = null;

		// 不同的layer的site
		Map<String, Map<String, CellLonLat>> layerSites = new HashMap<String, Map<String, CellLonLat>>();
		Map<String, CellLonLat> oneLayerSite = null;

		// site相同的经纬度点（此处的site是原始的site字段）
		Map<String, Map<String, List<GpsPoint>>> allLayerCoSiteDiffPoints = new HashMap<String, Map<String, List<GpsPoint>>>();
		Map<String, List<GpsPoint>> coSiteDiffPoints = null;// new
															// HashMap<String,
															// List<GpsPoint>>();
		List<GpsPoint> oneSitePoint = null;

		DecimalFormat df = new DecimalFormat("######0.000000");
		String site = "";
		double diffDis = 50;// 米
		boolean findSite = false;// 是否找到可以认为是同site的小区
		boolean needAddSite = false;
		DateUtil dateUtil=new DateUtil();
		if (cells != null && cells.size() > 0) {

			CellLonLat one = null;
			for (Map<String, Object> cell : cells) {
				one = RnoHelper.commonInjection(CellLonLat.class, cell,dateUtil);
				if (one == null) {
					continue;
				}
				// 如果layer为空，则用频段加上室内外来标识一个频段
				if (one.getLayer() == null) {
					one.setLayer(one.getFreqSection() + "_" + one.getIndoor());
				}
				if (one.getSite() == null) {
					one.setSite(one.getCell().substring(0,
							one.getCell().length() - 1));
				}
				// 处理site信息
				site = one.getSite();
				findSite = false;
				needAddSite = false;
				// 获取该层的cosite信息
				coSiteDiffPoints = allLayerCoSiteDiffPoints.get(one.getLayer());
				if (coSiteDiffPoints == null) {
					coSiteDiffPoints = new HashMap<String, List<GpsPoint>>();
					allLayerCoSiteDiffPoints.put(one.getLayer(),
							coSiteDiffPoints);
				}
				if (coSiteDiffPoints.containsKey(site)) {
					// 比较到各个同site小区的经纬度差距
					oneSitePoint = coSiteDiffPoints.get(site);
					for (GpsPoint gp : oneSitePoint) {
						if (LatLngHelper.Distance(gp.lon, gp.lat, one.getLon(),
								one.getLat()) <= diffDis) {
							// 与某个同site的小区的距离相差在一定范围内，认为是同一个
							findSite = true;
							// 重新设置site，设置为已存在的，这样，在下面计算距离的时候，可以排除
							one.setSite(site + df.format(gp.lon) + "_"
									+ df.format(gp.lat));// 用已有的经纬度
							break;
						}
					}
					if (!findSite) {
						// 与同site的其他小区的距离很远，新建一个，并且修改其site
						one.setSite(site + df.format(one.getLon()) + "_"
								+ df.format(one.getLat()));// 用自己的经纬度
						oneSitePoint.add(new GpsPoint(one.getLon(), one
								.getLat()));
						needAddSite = true;
					}
				} else {
					// 该小区的site字段是第一次出现
					needAddSite = true;
					oneSitePoint = new ArrayList<GpsPoint>();
					one.setSite(site + df.format(one.getLon()) + "_"
							+ df.format(one.getLat()));// 用自己的经纬度
					oneSitePoint.add(new GpsPoint(one.getLon(), one.getLat()));
					coSiteDiffPoints.put(site, oneSitePoint);
				}

				// 处理层信息
				if (!cellInlayers.containsKey(one.getLayer())) {
					// 该层的需要计算的小区
					oneLayerCells = new ArrayList<CellLonLat>();
					cellInlayers.put(one.getLayer(), oneLayerCells);

					// 该层的参考site
					oneLayerSite = new HashMap<String, CellLonLat>();
					layerSites.put(one.getLayer(), oneLayerSite);
				} else {
					oneLayerCells = cellInlayers.get(one.getLayer());
					oneLayerSite = layerSites.get(one.getLayer());
				}
				oneLayerCells.add(one);
				if (needAddSite) {
					oneLayerSite.put(one.getSite(), one);
				}

			}
		}

		Map<String, Double> cellDis = new HashMap<String, Double>();

		for (String layer : cellInlayers.keySet()) {
			oneLayerCells = cellInlayers.get(layer);
			oneLayerSite = layerSites.get(layer);
			calculateLayer(oneLayerCells, oneLayerSite, cellDis);
		}
		return cellDis;
	}

	/**
	 * 计算一层小区的理想覆盖距离
	 * 
	 * @param cells
	 * @param siteToCell
	 * @param cellDis
	 * @author brightming 2014-9-25 下午2:24:54
	 */
	private void calculateLayer(List<CellLonLat> cells,
			Map<String, CellLonLat> siteToCell, Map<String, Double> cellDis) {
		String cellSite = "";
		double smallAngle = 361;
		double largeAngle = -1;
		double bearing = 0;
		double refBearing = 0;
		double diss[] = new double[3];// 存放3个距离，从大到小
		CellLonLat ocell = null;
		double dis = 0;
		int cnt = 0;

		// String path =
		// "/tmp/层"+cells.get(0).getLayer()+"_"+cells.get(0).getCell()+"_理想距离原始数据.csv";
		// BufferedWriter writer = null;
		// try {
		// writer = new BufferedWriter(new OutputStreamWriter(
		// new FileOutputStream(path)));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		boolean isIndoor = false;
		if (cells.size() > 0) {
			for (CellLonLat cell : cells) {
				cellSite = cell.getSite();
				bearing = cell.getBearing();
				if (!isIndoor && "Y".equalsIgnoreCase(cell.getIndoor())) {
					isIndoor = true;
				}
				if (bearing >= 360) {
					bearing = bearing % 360;
				}
				smallAngle = bearing - 60;
				if (smallAngle < 0) {
					smallAngle += 360;
				}
				largeAngle = bearing + 60;
				if (largeAngle >= 360) {
					largeAngle -= 360;
				}

				for (int i = 0; i < diss.length; i++) {
					diss[i] = -1;
				}
				for (String osite : siteToCell.keySet()) {
					if (osite.equals(cellSite)) {
						continue;
					}
					ocell = siteToCell.get(osite);
					// 计算该小区到参考小区的方向角
					refBearing = cell.getCellToAnoterCellDir(ocell);
					// 如果方向角在参考覆盖范围内，则计算，否则，跳过
					if (betweenCoverRange(smallAngle, largeAngle, refBearing)) {
						// 计算
						dis = LatLngHelper.Distance(cell.getLon(),
								cell.getLat(), ocell.getLon(), ocell.getLat()) / 1000;// km为单位
						if (dis > 8) {
							// 超过8km
							// try {
							// writer.write(cell.getCell() + ","
							// + cell.getLon() + ","
							// + cell.getLat() + ","
							// + cell.getBearing() + " , "
							// + ocell.getCell() + ","
							// + ocell.getLon() + ","
							// + ocell.getLat() + ","
							// + ocell.getBearing()
							// + " , 小区到参考小区方向角：" + refBearing
							// + " , 在120度内 ,距离:" + dis + ",太远！");
							// writer.newLine();
							// } catch (IOException e) {
							// e.printStackTrace();
							// }
							continue;
						} else if (dis <= 0.05) {
							// 室外站，如果两个不同site的距离小于50米，则去除。
							// try {
							// writer.write(cell.getCell() + ","
							// + cell.getLon() + ","
							// + cell.getLat() + ","
							// + cell.getBearing() + " , "
							// + ocell.getCell() + ","
							// + ocell.getLon() + ","
							// + ocell.getLat() + ","
							// + ocell.getBearing()
							// + " , 室外小区到参考小区方向角：" + refBearing
							// + " , 在120度内 ,距离:" + dis + ",太近 了！");
							// writer.newLine();
							// } catch (IOException e) {
							// e.printStackTrace();
							// }
							continue;
						}

						// try {
						// writer.write(cell.getCell() + ","
						// + cell.getLon() + "," + cell.getLat()
						// + "," + cell.getBearing() + " , "
						// + ocell.getCell() + ","
						// + ocell.getLon() + "," + ocell.getLat()
						// + "," + ocell.getBearing()
						// + " , 小区到参考小区方向角：" + refBearing
						// + " , 在120度内 ,距离:" + dis);
						// writer.newLine();
						// } catch (IOException e) {
						// e.printStackTrace();
						// }
						if (diss[2] == -1) {
							diss[2] = dis;
						} else if (dis < diss[2]) {
							diss[0] = diss[1];
							diss[1] = diss[2];
							diss[2] = dis;
						} else if (diss[1] == -1) {
							diss[1] = dis;
						} else if (dis < diss[1]) {
							diss[0] = diss[1];
							diss[1] = dis;
						} else if (diss[0] == -1 || dis < diss[0]) {
							diss[0] = dis;
						}
					}
					// else{
					// try {
					// writer.write(cell.getCell() + ","
					// + cell.getLon() + "," + cell.getLat()
					// + "," + cell.getBearing() + " , "
					// + ocell.getCell() + ","
					// + ocell.getLon() + "," + ocell.getLat()
					// + "," + ocell.getBearing()
					// + " , 小区到参考小区方向角：" + refBearing
					// + " , 不在120度内!! ");
					// writer.newLine();
					// } catch (IOException e) {
					// e.printStackTrace();
					// }
					// }
				}
				// 计算距离
				dis = 0;
				cnt = 0;
				for (int i = 0; i < diss.length; i++) {
					if (diss[i] != -1) {
						dis += diss[i];
						cnt++;
					}
				}
				if (cnt > 0) {
					cellDis.put(cell.getCell(), dis / cnt);
				} else {
					cellDis.put(cell.getCell(), 0d);
				}

			}
		}
		// try{
		// writer.close();
		// }catch(Exception e){
		// e.printStackTrace();
		// }
	}

	private boolean betweenCoverRange(double smallAngle, double largeAngle,
			double refBearing) {
		if (smallAngle < largeAngle) {
			if (Math.ceil(refBearing) >= smallAngle
					&& Math.floor(refBearing) <= largeAngle) {
				return true;
			}
		} else if (Math.ceil(refBearing) >= smallAngle
				&& Math.floor(refBearing) <= 360 || refBearing >= 0
				&& Math.floor(refBearing) <= largeAngle) {
			return true;

		}
		return false;
	}

	public static class GpsPoint {
		double lon;
		double lat;

		public GpsPoint() {

		}

		public GpsPoint(double lon, double lat) {
			this.lon = lon;
			this.lat = lat;
		}

		public double largeDiffe(double lon2, double lat2) {
			double lo = Math.abs(lon - lon2);
			double la = Math.abs(lat - lat2);
			return lo > la ? lo : la;
		}
	}
}
