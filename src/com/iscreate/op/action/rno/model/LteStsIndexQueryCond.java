package com.iscreate.op.action.rno.model;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang3.StringUtils;

import com.iscreate.op.service.rno.tool.DateUtil;
import com.iscreate.op.service.rno.tool.RnoHelper;


/**
 * 查询lte Sts指标的查询条件
 * @author chao.xj
 *
 */
public class LteStsIndexQueryCond {
	private long cityId;
	private String cellNameList;
	//private String cellLabelList;
	private String columnList;
	private String columnNameList;
	private String meaBegTime;//开始日期
	private String meaEndTime;//结束日期

	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	public String getCellNameList() {
		return cellNameList;
	}
	public void setCellNameList(String cellList) {
		this.cellNameList = cellList;
	}
	public String getColumnList() {
		return columnList;
	}
	public void setColumnList(String columnList) {
		this.columnList = columnList;
	}
	public String getColumnNameList() {
		return columnNameList;
	}
	public void setColumnNameList(String columnNameList) {
		this.columnNameList = columnNameList;
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
		
		if (!StringUtils.isBlank(meaBegTime)) {
			Date bd = RnoHelper.parseDateArbitrary(meaBegTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " r4sd.Begintime>=to_date('"
						+ dateUtil.format_yyyyMMddHHmmss(bd)
						+ "','yyyy-mm-dd HH24:mi:ss')";
			}
		}
		if (!StringUtils.isBlank(meaEndTime)) {
			Date bd = RnoHelper.parseDateArbitrary(meaEndTime);
			if (bd != null) {
				where += StringUtils.isBlank(where) ? "" : " and ";
				where += " r4sd.Begintime<=to_date('"
						+ dateUtil.format_yyyyMMddHHmmss(bd)
						+ "','yyyy-mm-dd HH24:mi:ss')";
			}
			
		}
		if (cityId != -1) {
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " r4sd.area_id=" + cityId;
		}
		if (!StringUtils.isBlank(cellNameList)) {
			String[] cellNameArr = cellNameList.split(",");
			StringBuffer sb = new StringBuffer();
			for(String cellName:cellNameArr){
				sb.append("'").append(cellName).append("',");
			}
			where += StringUtils.isBlank(where) ? "" : " and ";
			where += " PMUSERLABEL  IN (" + sb.substring(0,sb.length()-1) + ")";
		}
		return (where == null || where.trim().isEmpty()) ? ("") : (" where r4smd.FHEAD_ID=r4sd.ID  and " + where);
	}
	public String buildFieldCont(){
/*		String field="select ";
		if(columnList!=null ||columnList.length()>0){
			String[] columnArr = columnList.split(",");
			for(String colunmName:columnArr){
				field = colunmName.trim()+",";
			}
		}
		return field.substring(0, field.length()-1)+" ";*/
		String field = "select ";
		field += " distinct r4smd.PMUSERLABEL cellName,"
				+ " r4smd.CONTEXT_AttRelEnb,"
				+ " r4smd.CONTEXT_AttRelEnb_Normal,"
				+ " r4smd.CONTEXT_SuccInitalSetup,"
				+ " r4smd.ERAB_HoFail,"
				+ " r4smd.ERAB_HoFail_1,"
				+ " r4smd.ERAB_HoFail_2,"
				+ " r4smd.ERAB_HoFail_3,"
				+ " r4smd.ERAB_HoFail_4,"
				+ " r4smd.ERAB_HoFail_5,"
				+ " r4smd.ERAB_HoFail_6,"
				+ " r4smd.ERAB_HoFail_7,"
				+ " r4smd.ERAB_HoFail_8,"
				+ " r4smd.ERAB_HoFail_9,"
				+ " r4smd.ERAB_NbrAttEstab,"
				+ " r4smd.ERAB_NbrAttEstab_1,"
				+ " r4smd.ERAB_NbrAttEstab_2,"
				+ " r4smd.ERAB_NbrAttEstab_3,"
				+ " r4smd.ERAB_NbrAttEstab_4,"
				+ " r4smd.ERAB_NbrAttEstab_5,"
				+ " r4smd.ERAB_NbrAttEstab_6,"
				+ " r4smd.ERAB_NbrAttEstab_7,"
				+ " r4smd.ERAB_NbrAttEstab_8,"
				+ " r4smd.ERAB_NbrAttEstab_9,"
				+ " r4smd.ERAB_NbrHoInc_1,"
				+ " r4smd.ERAB_NbrHoInc_2,"
				+ " r4smd.ERAB_NbrHoInc_3,"
				+ " r4smd.ERAB_NbrHoInc_4,"
				+ " r4smd.ERAB_NbrHoInc_5,"
				+ " r4smd.ERAB_NbrHoInc_6,"
				+ " r4smd.ERAB_NbrHoInc_7,"
				+ " r4smd.ERAB_NbrHoInc_8,"
				+ " r4smd.ERAB_NbrHoInc_9,"
				+ " r4smd.ERAB_NbrLeft_1,"
				+ " r4smd.ERAB_NbrLeft_2,"
				+ " r4smd.ERAB_NbrLeft_3,"
				+ " r4smd.ERAB_NbrLeft_4,"
				+ " r4smd.ERAB_NbrLeft_5,"
				+ " r4smd.ERAB_NbrLeft_6,"
				+ " r4smd.ERAB_NbrLeft_7,"
				+ " r4smd.ERAB_NbrLeft_8,"
				+ " r4smd.ERAB_NbrLeft_9,"
				+ " r4smd.ERAB_NbrReqRelEnb,"
				+ " r4smd.ERAB_NbrReqRelEnb_1,"
				+ " r4smd.ERAB_NbrReqRelEnb_2,"
				+ " r4smd.ERAB_NbrReqRelEnb_3,"
				+ " r4smd.ERAB_NbrReqRelEnb_4,"
				+ " r4smd.ERAB_NbrReqRelEnb_5,"
				+ " r4smd.ERAB_NbrReqRelEnb_6,"
				+ " r4smd.ERAB_NbrReqRelEnb_7,"
				+ " r4smd.ERAB_NbrReqRelEnb_8,"
				+ " r4smd.ERAB_NbrReqRelEnb_9,"
				+ " r4smd.ERAB_NbrReqRelEnb_Normal,"
				+ " r4smd.ERAB_NbrReqRelEnb_Normal_1,"
				+ " r4smd.ERAB_NbrReqRelEnb_Normal_2,"
				+ " r4smd.ERAB_NbrReqRelEnb_Normal_3,"
				+ " r4smd.ERAB_NbrReqRelEnb_Normal_4,"
				+ " r4smd.ERAB_NbrReqRelEnb_Normal_5,"
				+ " r4smd.ERAB_NbrReqRelEnb_Normal_6,"
				+ " r4smd.ERAB_NbrReqRelEnb_Normal_7,"
				+ " r4smd.ERAB_NbrReqRelEnb_Normal_8,"
				+ " r4smd.ERAB_NbrReqRelEnb_Normal_9,"
				+ " r4smd.ERAB_NbrSuccEstab,"
				+ " r4smd.ERAB_NbrSuccEstab_1,"
				+ " r4smd.ERAB_NbrSuccEstab_2,"
				+ " r4smd.ERAB_NbrSuccEstab_3,"
				+ " r4smd.ERAB_NbrSuccEstab_4,"
				+ " r4smd.ERAB_NbrSuccEstab_5,"
				+ " r4smd.ERAB_NbrSuccEstab_6,"
				+ " r4smd.ERAB_NbrSuccEstab_7,"
				+ " r4smd.ERAB_NbrSuccEstab_8,"
				+ " r4smd.ERAB_NbrSuccEstab_9,"
				+ " r4smd.HO_AttOutInterEnbS1,"
				+ " r4smd.HO_AttOutInterEnbX2,"
				+ " r4smd.HO_AttOutIntraEnb,"
				+ " r4smd.HO_SuccOutInterEnbS1,"
				+ " r4smd.HO_SuccOutInterEnbX2,"
				+ " r4smd.HO_SuccOutIntraEnb,"
				+ " r4smd.PDCP_UpOctDl,"
				+ " r4smd.PDCP_UpOctDl_1,"
				+ " r4smd.PDCP_UpOctDl_2,"
				+ " r4smd.PDCP_UpOctDl_3,"
				+ " r4smd.PDCP_UpOctDl_4,"
				+ " r4smd.PDCP_UpOctDl_5,"
				+ " r4smd.PDCP_UpOctDl_6,"
				+ " r4smd.PDCP_UpOctDl_7,"
				+ " r4smd.PDCP_UpOctDl_8,"
				+ " r4smd.PDCP_UpOctDl_9,"
				+ " r4smd.PDCP_UpOctUl,"
				+ " r4smd.PDCP_UpOctUl_1,"
				+ " r4smd.PDCP_UpOctUl_2,"
				+ " r4smd.PDCP_UpOctUl_3,"
				+ " r4smd.PDCP_UpOctUl_4,"
				+ " r4smd.PDCP_UpOctUl_5,"
				+ " r4smd.PDCP_UpOctUl_6,"
				+ " r4smd.PDCP_UpOctUl_7,"
				+ " r4smd.PDCP_UpOctUl_8,"
				+ " r4smd.PDCP_UpOctUl_9,"
				+ " r4smd.RRC_AttConnEstab,"
				+ " r4smd.RRC_AttConnReestab,"
				+ " r4smd.RRC_SuccConnEstab,"
				+ " r4sd.BEGINTIME,"
				+ " r4sd.ENDTIME";
		return field;
	}
	public String buildTableCont(){
		String table=" from ";
		table += " RNO_4G_STS_MEA_DATA r4smd, RNO_4G_STS_DESC r4sd";
		return table;
	}
	public  Map<String, Object> getLteStsIndex(Map<String, Object> dataMap){
		Map<String, Object> resMap = new HashMap<String, Object>();
		if(!dataMap.isEmpty()){
			resMap = calLteStsIndex(dataMap);
		}
		return resMap;
	}
	private Map<String, Object> calLteStsIndex(Map<String, Object> dataMap) {
		
		String[] columnArr = {};
		if(columnList!=null ||columnList.length()>0){
			columnArr = columnList.split(",");
		}
		Map<String, Object> resMap = new HashMap<String, Object>();
		for(String colunmName:columnArr){
		if(colunmName.trim().equalsIgnoreCase("rrc_ConnEstabSucc")){
		float rrc_ConnEstabSucc = 0;
		if (Float.valueOf(dataMap.get("RRC_ATTCONNESTAB").toString()) == 0) {
			rrc_ConnEstabSucc = 100;
		} else {
			rrc_ConnEstabSucc = 100* Float.valueOf(dataMap.get("RRC_SUCCCONNESTAB").toString())/ Float.valueOf(dataMap.get("RRC_ATTCONNESTAB").toString());
		}
		resMap.put(colunmName,formatData(rrc_ConnEstabSucc));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_EstabSucc")){
		float erab_EstabSucc = 0;
		if (getF("ERAB_NBRATTESTAB", dataMap) == 0) {
			erab_EstabSucc = 100;
		} else {
			erab_EstabSucc = 100 * getF("ERAB_NBRSUCCESTAB", dataMap)/ getF("ERAB_NBRATTESTAB", dataMap);
		}
		resMap.put(colunmName,formatData(erab_EstabSucc));
		}
		if(colunmName.trim().equalsIgnoreCase("wireConn")){
		float wireConn = 0;
		if (getF("ERAB_NBRATTESTAB", dataMap) * getF("RRC_ATTCONNESTAB", dataMap) == 0	|| getF("RRC_ATTCONNESTAB", dataMap) == 0) {
			wireConn = 100;
		} else {
			wireConn = 100 * getF("ERAB_NBRSUCCESTAB", dataMap)/ getF("ERAB_NBRATTESTAB", dataMap)* getF("RRC_SUCCCONNESTAB", dataMap)/ getF("RRC_ATTCONNESTAB", dataMap);
		}
		resMap.put(colunmName,formatData(wireConn));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_Drop_CellLevel")){
			
		float erab_Drop_CellLevel = 0;
		if (getF("ERAB_NBRSUCCESTAB", dataMap) == 0) {
			erab_Drop_CellLevel = 0;
		} else {
			erab_Drop_CellLevel = 100	* (getF("ERAB_NBRREQRELENB", dataMap)- getF("ERAB_NBRREQRELENB_NORMAL", dataMap) + getF("ERAB_HOFAIL", dataMap))
					/(getF("ERAB_NBRSUCCESTAB", dataMap)+ getF("HO_SUCCOUTINTERENBS1", dataMap)+ getF("HO_SUCCOUTINTERENBX2", dataMap) + getF("HO_ATTOUTINTRAENB", dataMap));
		}
		resMap.put(colunmName,formatData(erab_Drop_CellLevel));
		}
		if(colunmName.trim().equalsIgnoreCase("rrc_ConnRebuild")){
		float rrc_ConnRebuild = 0;
		if ((getF("RRC_ATTCONNREESTAB", dataMap) + getF("RRC_ATTCONNESTAB", dataMap)) == 0) {
			rrc_ConnRebuild = 0;
		} else {
			rrc_ConnRebuild = 100* getF("RRC_ATTCONNREESTAB", dataMap)	/ (getF("RRC_ATTCONNREESTAB", dataMap) + getF("RRC_ATTCONNESTAB",	dataMap));
		}
		resMap.put(colunmName,formatData(rrc_ConnRebuild));
		}
		if(colunmName.trim().equalsIgnoreCase("switchSucc")){		
		float switchSucc = 0;
		if ((getF("HO_ATTOUTINTERENBS1", dataMap) + getF("HO_ATTOUTINTERENBX2", dataMap) + getF("HO_ATTOUTINTRAENB", dataMap)) == 0) {
			switchSucc = 100;
		} else {
			switchSucc = 100	* (getF("HO_SUCCOUTINTERENBS1", dataMap)+ getF("HO_SUCCOUTINTERENBX2", dataMap) + getF("HO_SUCCOUTINTRAENB", dataMap))
					/ (getF("HO_ATTOUTINTERENBS1", dataMap)+ getF("HO_ATTOUTINTERENBX2", dataMap) + getF("HO_ATTOUTINTRAENB", dataMap));
		}
		resMap.put(colunmName,formatData(switchSucc));
		}
		if(colunmName.trim().equalsIgnoreCase("emUplinkSerBytes")){
			float emUplinkSerBytes = 0;
			emUplinkSerBytes = getF("PDCP_UPOCTUL", dataMap);
		resMap.put(colunmName,emUplinkSerBytes);
		}
		if(colunmName.trim().equalsIgnoreCase("emDownlinkSerBytes")){
			float emDownlinkSerBytes = 0;
			emDownlinkSerBytes = getF("PDCP_UPOCTDL", dataMap);
		resMap.put(colunmName,emDownlinkSerBytes);
		}
		if(colunmName.trim().equalsIgnoreCase("erab_ConnSuccQCI1")){
		float erab_ConnSuccQCI1 = 0;
		if (getF("ERAB_NBRATTESTAB_1", dataMap) == 0) {
			erab_ConnSuccQCI1 = 100;
		} else {
			erab_ConnSuccQCI1 = 100 * getF("ERAB_NBRSUCCESTAB_1", dataMap)	/ getF("ERAB_NBRATTESTAB_1", dataMap);
		}
		resMap.put(colunmName,formatData(erab_ConnSuccQCI1));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_ConnSuccQCI2")){
		
		float erab_ConnSuccQCI2 = 0;
		if (getF("ERAB_NBRATTESTAB_2", dataMap) == 0) {
			erab_ConnSuccQCI2 = 100;
		} else {
			erab_ConnSuccQCI2 = 100 * getF("ERAB_NBRSUCCESTAB_2", dataMap)	/ getF("ERAB_NBRATTESTAB_2", dataMap);
		}
		resMap.put(colunmName,formatData(erab_ConnSuccQCI2));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_ConnSuccQCI3")){		
		float erab_ConnSuccQCI3 = 0;
		if (getF("ERAB_NBRATTESTAB_3", dataMap) == 0) {
			erab_ConnSuccQCI3 = 100;
		} else {
			erab_ConnSuccQCI3 = 100 * getF("ERAB_NBRSUCCESTAB_3", dataMap)	/ getF("ERAB_NBRATTESTAB_3", dataMap);
		}
		resMap.put(colunmName,formatData(erab_ConnSuccQCI3));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_ConnSuccQCI4")){	
		float erab_ConnSuccQCI4 = 0;
		if (getF("ERAB_NBRATTESTAB_4", dataMap) == 0) {
			erab_ConnSuccQCI4 = 100;
		} else {
			erab_ConnSuccQCI4 = 100 * getF("ERAB_NBRSUCCESTAB_4", dataMap)	/ getF("ERAB_NBRATTESTAB_4", dataMap);
		}
		resMap.put(colunmName,formatData(erab_ConnSuccQCI4));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_ConnSuccQCI5")){		
		float erab_ConnSuccQCI5 = 0;
		if (getF("ERAB_NBRATTESTAB_5", dataMap) == 0) {
			erab_ConnSuccQCI5 = 100;
		} else {
			erab_ConnSuccQCI5 = 100 * getF("ERAB_NBRSUCCESTAB_5", dataMap)	/ getF("ERAB_NBRATTESTAB_5", dataMap);
		}
		resMap.put(colunmName,formatData(erab_ConnSuccQCI5));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_ConnSuccQCI6")){		
		float erab_ConnSuccQCI6 = 0;
		if (getF("ERAB_NBRATTESTAB_6", dataMap) == 0) {
			erab_ConnSuccQCI6 = 100;
		} else {
			erab_ConnSuccQCI6 = 100 * getF("ERAB_NBRSUCCESTAB_6", dataMap)	/ getF("ERAB_NBRATTESTAB_6", dataMap);
		}
		resMap.put(colunmName,formatData(erab_ConnSuccQCI6));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_ConnSuccQCI7")){
		float erab_ConnSuccQCI7 = 0;
		if (getF("ERAB_NBRATTESTAB_7", dataMap) == 0) {
			erab_ConnSuccQCI7 = 100;
		} else {
			erab_ConnSuccQCI7 = 100 * getF("ERAB_NBRSUCCESTAB_7", dataMap)	/ getF("ERAB_NBRATTESTAB_7", dataMap);
		}
		resMap.put(colunmName,formatData(erab_ConnSuccQCI7));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_ConnSuccQCI8")){		
		float erab_ConnSuccQCI8 = 0;
		if (getF("ERAB_NBRATTESTAB_8", dataMap) == 0) {
			erab_ConnSuccQCI8 = 100;
		} else {
			erab_ConnSuccQCI8 = 100 * getF("ERAB_NBRSUCCESTAB_8", dataMap)	/ getF("ERAB_NBRATTESTAB_8", dataMap);
		}
		resMap.put(colunmName,formatData(erab_ConnSuccQCI8));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_ConnSuccQCI9")){
		
		float erab_ConnSuccQCI9 = 0;
		if (getF("ERAB_NBRATTESTAB_9", dataMap) == 0) {
			erab_ConnSuccQCI9 = 100;
		} else {
			erab_ConnSuccQCI9 = 100 * getF("ERAB_NBRSUCCESTAB_9", dataMap)	/ getF("ERAB_NBRATTESTAB_9", dataMap);
		}
		resMap.put(colunmName,formatData(erab_ConnSuccQCI9));
		}
		if(colunmName.trim().equalsIgnoreCase("wireConnQCI1")){	
		float wireConnQCI1 = 0;
		if (getF("ERAB_NBRATTESTAB_1", dataMap) == 0
				|| getF("RRC_ATTCONNESTAB", dataMap) == 0) {
			wireConnQCI1 = 100;
		} else {
			wireConnQCI1 = 100 * getF("ERAB_NBRSUCCESTAB_1", dataMap)/ getF("ERAB_NBRATTESTAB_1", dataMap)* getF("RRC_SUCCCONNESTAB", dataMap)	/ getF("RRC_ATTCONNESTAB", dataMap);
		}
		resMap.put(colunmName,formatData(wireConnQCI1));
		}
		if(colunmName.trim().equalsIgnoreCase("wireConnQCI2")){		
		float wireConnQCI2 = 0;
		if (getF("ERAB_NBRATTESTAB_2", dataMap) == 0|| getF("RRC_ATTCONNESTAB", dataMap) == 0) {
			wireConnQCI2 = 100;
		} else {
			wireConnQCI2 = 100 * getF("ERAB_NBRSUCCESTAB_2", dataMap)/ getF("ERAB_NBRATTESTAB_2", dataMap)* getF("RRC_SUCCCONNESTAB", dataMap)	/ getF("RRC_ATTCONNESTAB", dataMap);
		}
		resMap.put(colunmName,formatData(wireConnQCI2));
		}
		if(colunmName.trim().equalsIgnoreCase("wireConnQCI3")){		
		float wireConnQCI3 = 0;
		if (getF("ERAB_NBRATTESTAB_3", dataMap) == 0|| getF("RRC_ATTCONNESTAB", dataMap) == 0) {
			wireConnQCI3 = 100;
		} else {
			wireConnQCI3 = 100 * getF("ERAB_NBRSUCCESTAB_3", dataMap)/ getF("ERAB_NBRATTESTAB_3", dataMap)* getF("RRC_SUCCCONNESTAB", dataMap)	/ getF("RRC_ATTCONNESTAB", dataMap);
		}
		resMap.put(colunmName,formatData(wireConnQCI3));
		}
		if(colunmName.trim().equalsIgnoreCase("wireConnQCI4")){		
		float wireConnQCI4 = 0;
		if (getF("ERAB_NBRATTESTAB_4", dataMap) == 0 || getF("RRC_ATTCONNESTAB", dataMap) == 0) {
			wireConnQCI4 = 100;
		} else {
			wireConnQCI4 = 100 * getF("ERAB_NBRSUCCESTAB_4", dataMap)/ getF("ERAB_NBRATTESTAB_4", dataMap)
					* getF("RRC_SUCCCONNESTAB", dataMap)/ getF("RRC_ATTCONNESTAB", dataMap);
		}
		resMap.put(colunmName,formatData(wireConnQCI4));
		}
		if(colunmName.trim().equalsIgnoreCase("wireConnQCI5")){		
		float wireConnQCI5 = 0;
		if (getF("ERAB_NBRATTESTAB_5", dataMap) == 0|| getF("RRC_ATTCONNESTAB", dataMap) == 0) {
			wireConnQCI5 = 100;
		} else {
			wireConnQCI5 = 100 * getF("ERAB_NBRSUCCESTAB_5", dataMap)/ getF("ERAB_NBRATTESTAB_5", dataMap)
					* getF("RRC_SUCCCONNESTAB", dataMap)/ getF("RRC_ATTCONNESTAB", dataMap);
		}
		resMap.put(colunmName,formatData(wireConnQCI5));
		}
		if(colunmName.trim().equalsIgnoreCase("wireConnQCI6")){		
		float wireConnQCI6 = 0;
		if (getF("ERAB_NBRATTESTAB_6", dataMap) == 0|| getF("RRC_ATTCONNESTAB", dataMap) == 0) {
			wireConnQCI6 = 100;
		} else {
			wireConnQCI6 = 100 * getF("ERAB_NBRSUCCESTAB_6", dataMap)/ getF("ERAB_NBRATTESTAB_6", dataMap)
					* getF("RRC_SUCCCONNESTAB", dataMap)/ getF("RRC_ATTCONNESTAB", dataMap);
		}
		resMap.put(colunmName,formatData(wireConnQCI6));
		}
		if(colunmName.trim().equalsIgnoreCase("wireConnQCI7")){		
		float wireConnQCI7 = 0;
		if (getF("ERAB_NBRATTESTAB_7", dataMap) == 0 || getF("RRC_ATTCONNESTAB", dataMap) == 0) {
			wireConnQCI7 = 100;
		} else {
			wireConnQCI7 = 100 * getF("ERAB_NBRSUCCESTAB_7", dataMap)	/ getF("ERAB_NBRATTESTAB_7", dataMap)
					* getF("RRC_SUCCCONNESTAB", dataMap)	/ getF("RRC_ATTCONNESTAB", dataMap);
		}
		resMap.put(colunmName,formatData(wireConnQCI7));
		}
		if(colunmName.trim().equalsIgnoreCase("wireConnQCI8")){
		float wireConnQCI8 = 0;
		if (getF("ERAB_NBRATTESTAB_8", dataMap) == 0|| getF("RRC_ATTCONNESTAB", dataMap) == 0) {
			wireConnQCI8 = 100;
		} else {
			wireConnQCI8 = 100 * getF("ERAB_NBRSUCCESTAB_8", dataMap)/ getF("ERAB_NBRATTESTAB_8", dataMap)
					* getF("RRC_SUCCCONNESTAB", dataMap)/ getF("RRC_ATTCONNESTAB", dataMap);
		}
		resMap.put(colunmName,formatData(wireConnQCI8));
		}
		if(colunmName.trim().equalsIgnoreCase("wireConnQCI9")){
		float wireConnQCI9 = 0;
		if (getF("ERAB_NBRATTESTAB_9", dataMap) == 0|| getF("RRC_ATTCONNESTAB", dataMap) == 0) {
			wireConnQCI9 = 100;
		} else {
			wireConnQCI9 = 100 * getF("ERAB_NBRSUCCESTAB_9", dataMap)/ getF("ERAB_NBRATTESTAB_9", dataMap)
					* getF("RRC_SUCCCONNESTAB", dataMap)/ getF("RRC_ATTCONNESTAB", dataMap);
		}
		resMap.put(colunmName,formatData(wireConnQCI9));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI1_CellLevel")){
		float erab_DropQCI1_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_1", dataMap) + getF("ERAB_NBRSUCCESTAB_1", dataMap) + getF("ERAB_NBRHOINC_1", dataMap)) == 0) {
			erab_DropQCI1_CellLevel = 100;
		} else {
			erab_DropQCI1_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_1", dataMap)- getF("ERAB_NBRREQRELENB_NORMAL_1", dataMap) + getF(	"ERAB_HOFAIL_1", dataMap))
					/ (getF("ERAB_NBRLEFT_1", dataMap)	+ getF("ERAB_NBRSUCCESTAB_1", dataMap) + getF("ERAB_NBRHOINC_1", dataMap));
		}
		resMap.put(colunmName,formatData(erab_DropQCI1_CellLevel));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI2_CellLevel")){		
		float erab_DropQCI2_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_2", dataMap) + getF("ERAB_NBRSUCCESTAB_2", dataMap) + getF("ERAB_NBRHOINC_2", dataMap)) == 0) {
			erab_DropQCI2_CellLevel = 100;
		} else {
			erab_DropQCI2_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_2", dataMap)- getF("ERAB_NBRREQRELENB_NORMAL_2", dataMap) + getF("ERAB_HOFAIL_2", dataMap))
					/ (getF("ERAB_NBRLEFT_2", dataMap)	+ getF("ERAB_NBRSUCCESTAB_2", dataMap) + getF("ERAB_NBRHOINC_2", dataMap));
		}
		resMap.put(colunmName,formatData(erab_DropQCI2_CellLevel));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI3_CellLevel")){		
		float erab_DropQCI3_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_3", dataMap) + getF("ERAB_NBRSUCCESTAB_3", dataMap) + getF("ERAB_NBRHOINC_3", dataMap)) == 0) {
			erab_DropQCI3_CellLevel = 100;
		} else {
			erab_DropQCI3_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_3", dataMap)- getF("ERAB_NBRREQRELENB_NORMAL_3", dataMap) + getF("ERAB_HOFAIL_3", dataMap))
					/ (getF("ERAB_NBRLEFT_3", dataMap)	+ getF("ERAB_NBRSUCCESTAB_3", dataMap) + getF("ERAB_NBRHOINC_3", dataMap));
		}
		resMap.put(colunmName,formatData(erab_DropQCI3_CellLevel));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI4_CellLevel")){		
		float erab_DropQCI4_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_4", dataMap) + getF("ERAB_NBRSUCCESTAB_4", dataMap) + getF("ERAB_NBRHOINC_4", dataMap)) == 0) {
			erab_DropQCI4_CellLevel = 100;
		} else {
			erab_DropQCI4_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_4", dataMap)- getF("ERAB_NBRREQRELENB_NORMAL_4", dataMap) + getF("ERAB_HOFAIL_4", dataMap))
					/ (getF("ERAB_NBRLEFT_4", dataMap)	+ getF("ERAB_NBRSUCCESTAB_4", dataMap) + getF("ERAB_NBRHOINC_4", dataMap));
		}
		resMap.put(colunmName,formatData(erab_DropQCI4_CellLevel));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI5_CellLevel")){		
		float erab_DropQCI5_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_5", dataMap) + getF("ERAB_NBRSUCCESTAB_5", dataMap) + getF("ERAB_NBRHOINC_5", dataMap)) == 0) {
			erab_DropQCI5_CellLevel = 100;
		} else {
			erab_DropQCI5_CellLevel = 100 	* (getF("ERAB_NBRREQRELENB_5", dataMap)- getF("ERAB_NBRREQRELENB_NORMAL_5", dataMap) + getF("ERAB_HOFAIL_5", dataMap))
					/ (getF("ERAB_NBRLEFT_5", dataMap)	+ getF("ERAB_NBRSUCCESTAB_5", dataMap) + getF("ERAB_NBRHOINC_5", dataMap));
		}
		resMap.put(colunmName,formatData(erab_DropQCI5_CellLevel));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI6_CellLevel")){		
		float erab_DropQCI6_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_6", dataMap) + getF("ERAB_NBRSUCCESTAB_6", dataMap) + getF("ERAB_NBRHOINC_6", dataMap)) == 0) {
			erab_DropQCI6_CellLevel = 100;
		} else {
			erab_DropQCI6_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_6", dataMap)- getF("ERAB_NBRREQRELENB_NORMAL_6", dataMap) + getF("ERAB_HOFAIL_6", dataMap))
					/ (getF("ERAB_NBRLEFT_6", dataMap)	+ getF("ERAB_NBRSUCCESTAB_6", dataMap) + getF("ERAB_NBRHOINC_6", dataMap));
		}
		resMap.put(colunmName,formatData(erab_DropQCI6_CellLevel));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI7_CellLevel")){		
		float erab_DropQCI7_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_7", dataMap) + getF("ERAB_NBRSUCCESTAB_7", dataMap) + getF("ERAB_NBRHOINC_7", dataMap)) == 0) {
			erab_DropQCI7_CellLevel = 100;
		} else {
			erab_DropQCI7_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_7", dataMap)- getF("ERAB_NBRREQRELENB_NORMAL_7", dataMap) + getF("ERAB_HOFAIL_7", dataMap))
					/ (getF("ERAB_NBRLEFT_7", dataMap)	+ getF("ERAB_NBRSUCCESTAB_7", dataMap) + getF("ERAB_NBRHOINC_7", dataMap));
		}
		resMap.put(colunmName,formatData(erab_DropQCI7_CellLevel));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI8_CellLevel")){		
		float erab_DropQCI8_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_8", dataMap) + getF("ERAB_NBRSUCCESTAB_8", dataMap) + getF("ERAB_NBRHOINC_8", dataMap)) == 0) {
			erab_DropQCI8_CellLevel = 100;
		} else {
			erab_DropQCI8_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_8", dataMap)- getF("ERAB_NBRREQRELENB_NORMAL_8", dataMap) + getF("ERAB_HOFAIL_8", dataMap))
					/ (getF("ERAB_NBRLEFT_8", dataMap)	+ getF("ERAB_NBRSUCCESTAB_8", dataMap) + getF("ERAB_NBRHOINC_8", dataMap));
		}
		resMap.put(colunmName,formatData(erab_DropQCI8_CellLevel));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI9_CellLevel")){
		float erab_DropQCI9_CellLevel = 0;
		if ((getF("ERAB_NBRLEFT_9", dataMap) + getF("ERAB_NBRSUCCESTAB_9", dataMap) + getF("ERAB_NBRHOINC_9", dataMap)) == 0) {
			erab_DropQCI9_CellLevel = 100;
		} else {
			erab_DropQCI9_CellLevel = 100	* (getF("ERAB_NBRREQRELENB_9", dataMap)- getF("ERAB_NBRREQRELENB_NORMAL_9", dataMap) + getF("ERAB_HOFAIL_9", dataMap))
					/ (getF("ERAB_NBRLEFT_9", dataMap)	+ getF("ERAB_NBRSUCCESTAB_9", dataMap) + getF("ERAB_NBRHOINC_9", dataMap));
		}
		resMap.put(colunmName,formatData(erab_DropQCI9_CellLevel));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI1")){		
		float erab_DropQCI1 = 0;
		if(getF("ERAB_NBRSUCCESTAB_1",dataMap)==0){
			erab_DropQCI1 = 100;
		}else{
			erab_DropQCI1 = (getF("ERAB_NBRREQRELENB_1",dataMap)-getF("ERAB_NBRREQRELENB_NORMAL_1",dataMap)+getF("ERAB_HOFAIL_1",dataMap))/getF("ERAB_NBRSUCCESTAB_1",dataMap);
		}
		resMap.put(colunmName,formatData(erab_DropQCI1));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI2")){		
		float erab_DropQCI2 = 0;
		if(getF("ERAB_NBRSUCCESTAB_2",dataMap)==0){
			erab_DropQCI2 = 100;
		}else{
			erab_DropQCI2 = (getF("ERAB_NBRREQRELENB_2",dataMap)-getF("ERAB_NBRREQRELENB_NORMAL_2",dataMap)+getF("ERAB_HOFAIL_2",dataMap))/getF("ERAB_NBRSUCCESTAB_2",dataMap);
		}
		resMap.put(colunmName,formatData(erab_DropQCI2));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI3")){		
		float erab_DropQCI3 = 0;
		if(getF("ERAB_NBRSUCCESTAB_3",dataMap)==0){
			erab_DropQCI3 = 100;
		}else{
			erab_DropQCI3 = (getF("ERAB_NBRREQRELENB_3",dataMap)-getF("ERAB_NBRREQRELENB_NORMAL_3",dataMap)+getF("ERAB_HOFAIL_3",dataMap))/getF("ERAB_NBRSUCCESTAB_3",dataMap);
		}
		resMap.put(colunmName,formatData(erab_DropQCI3));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI4")){		
		float erab_DropQCI4 = 0;
		if(getF("ERAB_NBRSUCCESTAB_4",dataMap)==0){
			erab_DropQCI4 = 100;
		}else{
			erab_DropQCI4 = (getF("ERAB_NBRREQRELENB_4",dataMap)-getF("ERAB_NBRREQRELENB_NORMAL_4",dataMap)+getF("ERAB_HOFAIL_4",dataMap))/getF("ERAB_NBRSUCCESTAB_4",dataMap);
		}
		resMap.put(colunmName,formatData(erab_DropQCI4));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI5")){		
		float erab_DropQCI5 = 0;
		if(getF("ERAB_NBRSUCCESTAB_5",dataMap)==0){
			erab_DropQCI5 = 100;
		}else{
			erab_DropQCI5 = (getF("ERAB_NBRREQRELENB_5",dataMap)-getF("ERAB_NBRREQRELENB_NORMAL_5",dataMap)+getF("ERAB_HOFAIL_5",dataMap))/getF("ERAB_NBRSUCCESTAB_5",dataMap);
		}
		resMap.put(colunmName,formatData(erab_DropQCI5));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI6")){		
		float erab_DropQCI6 = 0;
		if(getF("ERAB_NBRSUCCESTAB_6",dataMap)==0){
			erab_DropQCI6 = 100;
		}else{
			erab_DropQCI6 = (getF("ERAB_NBRREQRELENB_6",dataMap)-getF("ERAB_NBRREQRELENB_NORMAL_6",dataMap)+getF("ERAB_HOFAIL_6",dataMap))/getF("ERAB_NBRSUCCESTAB_6",dataMap);
		}
		resMap.put(colunmName,formatData(erab_DropQCI6));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI7")){		
		float erab_DropQCI7 = 0;
		if(getF("ERAB_NBRSUCCESTAB_7",dataMap)==0){
			erab_DropQCI7 = 100;
		}else{
			erab_DropQCI7 = (getF("ERAB_NBRREQRELENB_7",dataMap)-getF("ERAB_NBRREQRELENB_NORMAL_7",dataMap)+getF("ERAB_HOFAIL_7",dataMap))/getF("ERAB_NBRSUCCESTAB_7",dataMap);
		}
		resMap.put(colunmName,formatData(erab_DropQCI7));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI8")){		
		float erab_DropQCI8 = 0;
		if(getF("ERAB_NBRSUCCESTAB_8",dataMap)==0){
			erab_DropQCI8 = 100;
		}else{
			erab_DropQCI8 = (getF("ERAB_NBRREQRELENB_8",dataMap)-getF("ERAB_NBRREQRELENB_NORMAL_8",dataMap)+getF("ERAB_HOFAIL_8",dataMap))/getF("ERAB_NBRSUCCESTAB_8",dataMap);
		}
		resMap.put(colunmName,formatData(erab_DropQCI8));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_DropQCI9")){		
		float erab_DropQCI9 = 0;
		if(getF("ERAB_NBRSUCCESTAB_9",dataMap)==0){
			erab_DropQCI9 = 100;
		}else{
			erab_DropQCI9 = (getF("ERAB_NBRREQRELENB_9",dataMap)-getF("ERAB_NBRREQRELENB_NORMAL_9",dataMap)+getF("ERAB_HOFAIL_9",dataMap))/getF("ERAB_NBRSUCCESTAB_9",dataMap);
		}
		resMap.put(colunmName,formatData(erab_DropQCI9));
		}
		if(colunmName.trim().equalsIgnoreCase("emUplinkSerBytesQCI1")){		
		float emUplinkSerBytesQCI1 = 0;
		emUplinkSerBytesQCI1 =getF("PDCP_UPOCTUL_1",dataMap);
		resMap.put(colunmName,emUplinkSerBytesQCI1);
		}
		if(colunmName.trim().equalsIgnoreCase("emUplinkSerBytesQCI2")){		
		float emUplinkSerBytesQCI2 = 0;
		emUplinkSerBytesQCI2 =getF("PDCP_UPOCTUL_2",dataMap);
		resMap.put(colunmName,emUplinkSerBytesQCI2);
		}
		if(colunmName.trim().equalsIgnoreCase("emUplinkSerBytesQCI3")){
		float emUplinkSerBytesQCI3 = 0;
		emUplinkSerBytesQCI3 =getF("PDCP_UPOCTUL_3",dataMap);
		resMap.put(colunmName,emUplinkSerBytesQCI3);
		}
		if(colunmName.trim().equalsIgnoreCase("emUplinkSerBytesQCI4")){		
		float emUplinkSerBytesQCI4 = 0;
		emUplinkSerBytesQCI4 =getF("PDCP_UPOCTUL_4",dataMap);
		resMap.put(colunmName,emUplinkSerBytesQCI4);
		}
		if(colunmName.trim().equalsIgnoreCase("emUplinkSerBytesQCI5")){		
		float emUplinkSerBytesQCI5 = 0;
		emUplinkSerBytesQCI5 =getF("PDCP_UPOCTUL_5",dataMap);
		resMap.put(colunmName,emUplinkSerBytesQCI5);
		}
		if(colunmName.trim().equalsIgnoreCase("emUplinkSerBytesQCI6")){		
		float emUplinkSerBytesQCI6 = 0;
		emUplinkSerBytesQCI6 =getF("PDCP_UPOCTUL_6",dataMap);
		resMap.put(colunmName,emUplinkSerBytesQCI6);
		}
		if(colunmName.trim().equalsIgnoreCase("emUplinkSerBytesQCI7")){		
		float emUplinkSerBytesQCI7 = 0;
		emUplinkSerBytesQCI7 =getF("PDCP_UPOCTUL_7",dataMap);
		resMap.put(colunmName,emUplinkSerBytesQCI7);
		}
		if(colunmName.trim().equalsIgnoreCase("emUplinkSerBytesQCI8")){		
		float emUplinkSerBytesQCI8 = 0;
		emUplinkSerBytesQCI8 =getF("PDCP_UPOCTUL_8",dataMap);
		resMap.put(colunmName,emUplinkSerBytesQCI8);
		}
		if(colunmName.trim().equalsIgnoreCase("emUplinkSerBytesQCI9")){		
		float emUplinkSerBytesQCI9 = 0;
		emUplinkSerBytesQCI9 =getF("PDCP_UPOCTUL_9",dataMap);
		resMap.put(colunmName,emUplinkSerBytesQCI9);
		}
		if(colunmName.trim().equalsIgnoreCase("emDownlinkSerBytesQCI1")){		
		float emDownlinkSerBytesQCI1 = 0;
		emDownlinkSerBytesQCI1 = getF("PDCP_UPOCTDL_1",dataMap);
		resMap.put(colunmName,emDownlinkSerBytesQCI1);
		}
		if(colunmName.trim().equalsIgnoreCase("emDownlinkSerBytesQCI2")){		
		float emDownlinkSerBytesQCI2 = 0;
		emDownlinkSerBytesQCI2 = getF("PDCP_UPOCTDL_2",dataMap);
		resMap.put(colunmName,emDownlinkSerBytesQCI2);
		}
		if(colunmName.trim().equalsIgnoreCase("emDownlinkSerBytesQCI3")){		
		float emDownlinkSerBytesQCI3 = 0;
		emDownlinkSerBytesQCI3 = getF("PDCP_UPOCTDL_3",dataMap);
		resMap.put(colunmName,emDownlinkSerBytesQCI3);
		}
		if(colunmName.trim().equalsIgnoreCase("emDownlinkSerBytesQCI4")){		
		float emDownlinkSerBytesQCI4 = 0;
		emDownlinkSerBytesQCI4 = getF("PDCP_UPOCTDL_4",dataMap);
		resMap.put(colunmName,emDownlinkSerBytesQCI4);
		}
		if(colunmName.trim().equalsIgnoreCase("emDownlinkSerBytesQCI5")){		
		float emDownlinkSerBytesQCI5 = 0;
		emDownlinkSerBytesQCI5 = getF("PDCP_UPOCTDL_5",dataMap);
		resMap.put(colunmName,emDownlinkSerBytesQCI5);
		}
		if(colunmName.trim().equalsIgnoreCase("emDownlinkSerBytesQCI6")){		
		float emDownlinkSerBytesQCI6 = 0;
		emDownlinkSerBytesQCI6 = getF("PDCP_UPOCTDL_6",dataMap);
		resMap.put(colunmName,emDownlinkSerBytesQCI6);
		}
		if(colunmName.trim().equalsIgnoreCase("emDownlinkSerBytesQCI7")){		
		float emDownlinkSerBytesQCI7 = 0;
		emDownlinkSerBytesQCI7 = getF("PDCP_UPOCTDL_7",dataMap);
		resMap.put(colunmName,emDownlinkSerBytesQCI7);
		}
		if(colunmName.trim().equalsIgnoreCase("emDownlinkSerBytesQCI8")){		
		float emDownlinkSerBytesQCI8 = 0;
		emDownlinkSerBytesQCI8 = getF("PDCP_UPOCTDL_8",dataMap);
		resMap.put(colunmName,emDownlinkSerBytesQCI8);
		}
		if(colunmName.trim().equalsIgnoreCase("emDownlinkSerBytesQCI9")){		
		float emDownlinkSerBytesQCI9 = 0;
		emDownlinkSerBytesQCI9 = getF("PDCP_UPOCTDL_9",dataMap);
		resMap.put(colunmName,emDownlinkSerBytesQCI9);
		}
		if(colunmName.trim().equalsIgnoreCase("wireDrop_CellLevel")){		
		float wireDrop_CellLevel = 0;
		if(getF("CONTEXT_SUCCINITALSETUP",dataMap)==0){
			wireDrop_CellLevel = 0;
		}else{
			wireDrop_CellLevel = (getF("CONTEXT_ATTRELENB",dataMap)-getF("CONTEXT_ATTRELENB_NORMAL",dataMap))/getF("CONTEXT_SUCCINITALSETUP",dataMap)*100;
		}
		resMap.put(colunmName,formatData(wireDrop_CellLevel));
		}
		if(colunmName.trim().equalsIgnoreCase("erab_EstabSucc_SuccTimes")){		
		float erab_EstabSucc_SuccTimes = 0;
		erab_EstabSucc_SuccTimes = getF("ERAB_NBRSUCCESTAB",dataMap);
		resMap.put(colunmName,erab_EstabSucc_SuccTimes);
		}
		if(colunmName.trim().equalsIgnoreCase("erab_EstabSucc_ReqTimes")){		
		float erab_EstabSucc_ReqTimes = 0;
		erab_EstabSucc_ReqTimes = getF("ERAB_NBRATTESTAB",dataMap);
		resMap.put(colunmName,erab_EstabSucc_ReqTimes);		
		}
		if(colunmName.trim().equalsIgnoreCase("erab_Drop_ReqTimes_CellLevel")){		
		float erab_Drop_ReqTimes_CellLevel = 0;
		erab_Drop_ReqTimes_CellLevel = getF("ERAB_NBRSUCCESTAB",dataMap)+getF("HO_SUCCOUTINTERENBS1",dataMap)+getF("HO_SUCCOUTINTERENBX2",dataMap)+getF("HO_ATTOUTINTRAENB",dataMap);
	    resMap.put(colunmName,erab_Drop_ReqTimes_CellLevel);	    
		}
		if(colunmName.trim().equalsIgnoreCase("switchSucc_SuccTimes")){		
		float switchSucc_SuccTimes = 0;
		switchSucc_SuccTimes = getF("HO_SUCCOUTINTERENBS1",dataMap)+getF("HO_SUCCOUTINTERENBX2",dataMap)+getF("HO_SUCCOUTINTRAENB",dataMap);
		resMap.put(colunmName,switchSucc_SuccTimes);		
		}
		if(colunmName.trim().equalsIgnoreCase("switchSucc_ReqTimes")){		
		float switchSucc_ReqTimes = 0;
		switchSucc_ReqTimes = getF("HO_ATTOUTINTERENBS1",dataMap)+getF("HO_ATTOUTINTERENBX2",dataMap)+getF("HO_ATTOUTINTRAENB",dataMap); 
		resMap.put(colunmName,switchSucc_ReqTimes);		
		}
		if(colunmName.trim().equalsIgnoreCase("wireDrop_ReqTimes_CellLevel")){		
		float wireDrop_ReqTimes_CellLevel = 0;
		wireDrop_ReqTimes_CellLevel = getF("CONTEXT_SUCCINITALSETUP",dataMap);
		resMap.put(colunmName,wireDrop_ReqTimes_CellLevel);		
		}
		if(colunmName.trim().equalsIgnoreCase("wireConn_ReqTimes")){		
		float wireConn_ReqTimes = 0;  
		wireConn_ReqTimes = getF("ERAB_NBRATTESTAB",dataMap)*getF("RRC_ATTCONNESTAB",dataMap);
		resMap.put(colunmName,wireConn_ReqTimes);		
		}
		if(colunmName.trim().equalsIgnoreCase("erab_Drop_DropTimes_CellLevel")){		
		float erab_Drop_DropTimes_CellLevel = 0;
		erab_Drop_DropTimes_CellLevel = getF("ERAB_NBRREQRELENB",dataMap)-getF("ERAB_NBRREQRELENB_NORMAL",dataMap)+getF("ERAB_HOFAIL",dataMap);
	    resMap.put(colunmName,erab_Drop_DropTimes_CellLevel);	    
		}
		if(colunmName.trim().equalsIgnoreCase("wireConn_SuccTimes")){		
		float wireConn_SuccTimes = 0;
		wireConn_SuccTimes = getF("ERAB_NBRSUCCESTAB",dataMap)*getF("RRC_SUCCCONNESTAB",dataMap);
		resMap.put(colunmName,wireConn_SuccTimes);		
		}
		if(colunmName.trim().equalsIgnoreCase("rrc_ConnEstabSucc_SuccTimes")){		
		float rrc_ConnEstabSucc_SuccTimes = 0;
		rrc_ConnEstabSucc_SuccTimes = getF("RRC_SUCCCONNESTAB",dataMap);
		resMap.put(colunmName,rrc_ConnEstabSucc_SuccTimes);
		}
		if(colunmName.trim().equalsIgnoreCase("rrc_ConnEstabSucc_ReqTimes")){		
		float rrc_ConnEstabSucc_ReqTimes = 0;
		rrc_ConnEstabSucc_ReqTimes = getF("RRC_ATTCONNESTAB",dataMap);
		resMap.put(colunmName,rrc_ConnEstabSucc_ReqTimes);
		}
		if(colunmName.trim().equalsIgnoreCase("wireDrop_DropTimes_CellLevel")){		
		float wireDrop_DropTimes_CellLevel =0;
		wireDrop_DropTimes_CellLevel = getF("CONTEXT_ATTRELENB",dataMap)-getF("CONTEXT_ATTRELENB_NORMAL",dataMap);
		resMap.put(colunmName,wireDrop_DropTimes_CellLevel);
		}
		}
		return resMap;
	}
	private static float getF(String prName,Map<String, Object> p) {
		return Float.valueOf(p.get(prName).toString());		
	}

	public ListOrderedMap getColumnTitles() {
		String[] columnArr = columnList.split(",");
		String[] columnNameArr = columnNameList.split(",");
		ListOrderedMap columnMap = new ListOrderedMap();
		columnMap.put("MEABEGTIME", "起始时间");
		columnMap.put("CELLNAME", "小区名称");
		if (columnArr.length == columnNameArr.length) {
			for (int i = 0; i < columnArr.length; i++) {
				columnMap.put(columnArr[i], columnNameArr[i]);
			}
		}
		return columnMap;
	}
	private static String formatData(float data){
		DecimalFormat   df   =new  DecimalFormat("0.##%");
		return df.format(data/100);
	}
}
