package com.iscreate.op.service.rno.parser.vo;
/**
 * 
 * @title 
 * @author chao.xj
 * @date 2014-12-3下午17:45:43
 * @company 怡创科技
 * @version 1.2
 */
public enum NcsIndex {

	ERI_NCS_T("RNO_2G_ERI_NCS_HBASE"), 
	ERI_NCS_CELL_T("RNO_2G_ERI_NCS_CELL_DATA_HBASE"), 
	HW_NCS_T("RNO_2G_HW_NCS_HBASE"), 
	LTE4G_CELL_MEATIME_T("LTE4G_CELL_MEATIME"), 
	NCS_CF("NCSINFO"),
	ERI_NCS_T_CQ(
			"RNO_2G_ERI_NCS_DESC_ID,CELL,NCELL,CHGR,BSIC,ARFCN,DEFINED_NEIGHBOUR,RECTIMEARFCN,REPARFCN,TIMES,NAVSS,TIMES1,NAVSS1,TIMES2,NAVSS2,TIMES3,NAVSS3,TIMES4,NAVSS4,TIMES5,NAVSS5,TIMES6,NAVSS6,TIMESRELSS,TIMESRELSS2,TIMESRELSS3,TIMESRELSS4,TIMESRELSS5,TIMESABSS,TIMESALONE,DISTANCE,INTERFER,CA_INTERFER,NCELLS,CELL_FREQ_CNT,NCELL_FREQ_CNT,SAME_FREQ_CNT,ADJ_FREQ_CNT,CI_DIVIDER,CA_DIVIDER,CELL_INDOOR,NCELL_INDOOR,MEA_TIME,CELL_LON,CELL_LAT,NCELL_LON,NCELL_LAT,CELL_BEARING,CELL_DOWNTILT,CELL_SITE,NCELL_SITE,CELL_FREQ_SECTION,NCELL_FREQ_SECTION,CELL_TO_NCELL_DIR,CELL_BCCH,CELL_TCH,NCELL_TCH,CITY_ID,NCELL_BEARING"), 
	ERI_NCS_CELL_T_CQ(
			"RNO_2G_ERI_NCS_DESC_ID,CELL,CHGR,REP,REPHR,REPUNDEFGSM,AVSS,MEA_TIME,CITY_ID"), 
	HW_NCS_T_CQ(
			"RNO_2GHWNCS_DESC_ID,CELL,ARFCN,BSIC,AS360,S389,S368,S386,S363,S371,AS362,S387,S372,S360,S394,S364,S369,S388,S390,S366,S362,S361,S393,S365,S392,S391,S367,S370,MEA_TIME,NCELL,NCELLS,CELL_LON,CELL_LAT,NCELL_LON,NCELL_LAT,CELL_FREQ_CNT,NCELL_FREQ_CNT,SAME_FREQ_CNT,ADJ_FREQ_CNT,CELL_BEARING,CELL_INDOOR,NCELL_INDOOR,CELL_DOWNTILT,CELL_SITE,NCELL_SITE,CELL_FREQ_SECTION,NCELL_FREQ_SECTION,DISTANCE,CI_DIVIDER,CA_DIVIDER,CELL_TO_NCELL_DIR,CELL_BCCH,CELL_TCH,NCELL_TCH,CITY_ID,S3013,NCELL_BEARING,S374,S375,S376,S377,S378,S379,S380,S381,S382,S383,S384,S385");
	public final String str; // 定义枚举成员属性

	NcsIndex(String str) { // 内部方法设定枚举成员属性
		this.str = str;
	}
	public static void main(String[] args) {
		System.out.println(NcsIndex.ERI_NCS_CELL_T_CQ.str);
	}
}
