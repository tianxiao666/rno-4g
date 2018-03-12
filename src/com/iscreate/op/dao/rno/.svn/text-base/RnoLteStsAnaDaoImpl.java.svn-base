package com.iscreate.op.dao.rno;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.G4StsDescQueryCond;
import com.iscreate.op.pojo.rno.RnoLteCellForPm;

public class RnoLteStsAnaDaoImpl implements RnoLteStsAnaDao {
	private static Logger log = LoggerFactory.getLogger(RnoLteStsAnaDaoImpl.class);

	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 查询符合条件的4G话统记录数
	 * @param cond
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteStsAnaDao#queryG4StsDescCnt(com.iscreate.op.action.rno.model.G4StsDescQueryCond)
	 * @author chen.c10
	 * @date 2015-7-17 下午7:28:11
	 * @company 怡创科技
	 * @version V1.0
	 */
	public long queryG4StsDescCnt(final G4StsDescQueryCond cond){
		log.debug("queryG4StsDescCnt.cond=" + cond);
		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@SuppressWarnings("unchecked")
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("queryG4StsDescCnt ,where=" + where);
				String sql = "select count(ID) from RNO_4G_STS_DESC "
						+ where;
				log.debug("queryG4StsDescCnt,sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				List<Object> list =(List<Object>) query.list();
				Long cnt = 0l;
				if (list != null && list.size() > 0) {
					cnt = Long.valueOf(list.get(0).toString());
				}
				log.debug("queryG4StsDescCnt.result="+cnt);
				return cnt;
			}
		});
	}

	/**
	 * 分页查询符合条件的话统数据
	 * @param cond
	 * @param page
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteStsAnaDao#queryG4StsDescByPage(com.iscreate.op.action.rno.model.G4StsDescQueryCond, com.iscreate.op.action.rno.Page)
	 * @author chen.c10
	 * @date 2015-7-17 下午7:28:35
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryG4StsDescByPage(final 
			G4StsDescQueryCond cond,final Page page){
		log.debug("queryG4StsDescByPage.cond=" + cond);
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= "	ID,AREA_ID,CREATE_TIME,INFOMODELREFERENCED,DNPREFIX,SENDERNAME,VENDORNAME,JOBID,BEGINTIME,ENDTIME,CNT";
						String field_inner = "ID,AREA_ID,TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME,INFOMODELREFERENCED,DNPREFIX,SENDERNAME,VENDORNAME,JOBID,TO_CHAR(BEGINTIME,'YYYY-MM-DD HH24:MI:SS') BEGINTIME,TO_CHAR(ENDTIME,'YYYY-MM-DD HH24:MI:SS') ENDTIME,CNT";
						String where = cond.buildWhereCont();
						log.debug("queryG4StsDescByPage ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ ",rownum rn from RNO_4G_STS_DESC "
								+ whereResult + " order by ID desc) where  rn>="+start+" and rn<="+end+" order by BEGINTIME desc";
						log.debug("queryG4StsDescByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						log.debug("queryG4StsDescByPage.result="+rows);
						return rows;
					}
				});
	}

	/**
	 * 查询4G话统数据
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteStsAnaDao#queryG4PmDate()
	 * @author chen.c10
	 * @date 2015-7-20 下午2:19:05
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryG4StsData(final G4StsDescQueryCond cond) {
		log.debug("queryG4StsData.cond="+cond );
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String param = "pmm.ID,FHEAD_ID,PMDN,PMUSERLABEL cellName,CONTEXT_AttRelEnb,CONTEXT_AttRelEnb_Normal,CONTEXT_SuccInitalSetup,ERAB_HoFail,ERAB_HoFail_1,ERAB_HoFail_2,ERAB_HoFail_3,ERAB_HoFail_4,ERAB_HoFail_5,ERAB_HoFail_6,ERAB_HoFail_7,ERAB_HoFail_8,ERAB_HoFail_9,ERAB_NbrAttEstab,ERAB_NbrAttEstab_1,ERAB_NbrAttEstab_2,ERAB_NbrAttEstab_3,ERAB_NbrAttEstab_4,ERAB_NbrAttEstab_5,ERAB_NbrAttEstab_6,ERAB_NbrAttEstab_7,ERAB_NbrAttEstab_8,ERAB_NbrAttEstab_9,ERAB_NbrHoInc_1,ERAB_NbrHoInc_2,ERAB_NbrHoInc_3,ERAB_NbrHoInc_4,ERAB_NbrHoInc_5,ERAB_NbrHoInc_6,ERAB_NbrHoInc_7,ERAB_NbrHoInc_8,ERAB_NbrHoInc_9,ERAB_NbrLeft_1,ERAB_NbrLeft_2,ERAB_NbrLeft_3,ERAB_NbrLeft_4,ERAB_NbrLeft_5,ERAB_NbrLeft_6,ERAB_NbrLeft_7,ERAB_NbrLeft_8,ERAB_NbrLeft_9,ERAB_NbrReqRelEnb,ERAB_NbrReqRelEnb_1,ERAB_NbrReqRelEnb_2,ERAB_NbrReqRelEnb_3,ERAB_NbrReqRelEnb_4,ERAB_NbrReqRelEnb_5,ERAB_NbrReqRelEnb_6,ERAB_NbrReqRelEnb_7,ERAB_NbrReqRelEnb_8,ERAB_NbrReqRelEnb_9,ERAB_NbrReqRelEnb_Normal,ERAB_NbrReqRelEnb_Normal_1,ERAB_NbrReqRelEnb_Normal_2,ERAB_NbrReqRelEnb_Normal_3,ERAB_NbrReqRelEnb_Normal_4,ERAB_NbrReqRelEnb_Normal_5,ERAB_NbrReqRelEnb_Normal_6,ERAB_NbrReqRelEnb_Normal_7,ERAB_NbrReqRelEnb_Normal_8,ERAB_NbrReqRelEnb_Normal_9,ERAB_NbrSuccEstab,ERAB_NbrSuccEstab_1,ERAB_NbrSuccEstab_2,ERAB_NbrSuccEstab_3,ERAB_NbrSuccEstab_4,ERAB_NbrSuccEstab_5,ERAB_NbrSuccEstab_6,ERAB_NbrSuccEstab_7,ERAB_NbrSuccEstab_8,ERAB_NbrSuccEstab_9,HO_AttOutInterEnbS1,HO_AttOutInterEnbX2,HO_AttOutIntraEnb,HO_SuccOutInterEnbS1,HO_SuccOutInterEnbX2,HO_SuccOutIntraEnb,PDCP_UpOctDl,PDCP_UpOctDl_1,PDCP_UpOctDl_2,PDCP_UpOctDl_3,PDCP_UpOctDl_4,PDCP_UpOctDl_5,PDCP_UpOctDl_6,PDCP_UpOctDl_7,PDCP_UpOctDl_8,PDCP_UpOctDl_9,PDCP_UpOctUl,PDCP_UpOctUl_1,PDCP_UpOctUl_2,PDCP_UpOctUl_3,PDCP_UpOctUl_4,PDCP_UpOctUl_5,PDCP_UpOctUl_6,PDCP_UpOctUl_7,PDCP_UpOctUl_8,PDCP_UpOctUl_9,RRC_AttConnEstab,RRC_AttConnReestab,RRC_SuccConnEstab";
				String where = cond.buildWhereQuota();
				log.debug("queryG4StsData ,where=" + where);
				String whereResult = (where == null || where.trim()
						.isEmpty()) ? ("") : (" where " + where);
				String sql = "select * from (select "+param+",pmh.BEGINTIME,pmh.ENDTIME"+" from RNO_4G_STS_MEA_DATA pmm,RNO_4G_STS_DESC pmh " +
						",RNO_LTE_CELL rlc " + whereResult+" and pmm.FHEAD_ID=pmh.ID and rlc.CELL_NAME=pmm.PMUSERLABEL ) where rownum <=1";
				log.debug("queryG4StsData ,sql=" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String,Object>> rows = query.list();
				log.debug("queryG4StsData.result="+rows);
				return 	rows;
			}
			});
	}
	/**
	 * 查询4G话统小区名列表
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteStsAnaDao#queryG4StsCellNameList()
	 * @author chen.c10
	 * @date 2015-7-21 下午2:57:20
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public List<String> queryG4StsCellNameList(){
		List<Map<String,String>> list=hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, String>>>() {
			public List<Map<String, String>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				log.debug("进入方法queryG4StsData。");
				String sql="select distinct PMUSERLABEL cellName from RNO_4G_STS_MEA_DATA";
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String,String>> rows = query.list();
				return rows;
			}
			});
		List<String> cellNames=new ArrayList<String>();
		for(Map<String,String> map:list){
			cellNames.add(map.get("CELLNAME"));
		}
		log.debug("退出方法queryG4StsData。result="+cellNames);
				return cellNames;
	}
	/**
	 * 通过网格和分页获取4G话统小区名列表
	 * @param areaStr
	 * @param blPoint
	 * @param trPoint
	 * @param page
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteStsAnaDao#getRnoLteCellByGridAndPageInAreaWithSts(java.lang.String, java.lang.String[], java.lang.String[], com.iscreate.op.action.rno.Page)
	 * @author chen.c10
	 * @date 2015-7-30 下午3:33:14
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public List<String> getRnoLteCellByGridAndPageInAreaWithSts(String areaStr,
			String[] blPoint, String[] trPoint, final Page page) { 
		log.debug("进入方法:getRnoLteCellByGridAndPageInAreaWithPm。 areaStr=" + areaStr
				+ ", blPoint=" + blPoint + ", trPoint=" + trPoint +", page="+page);
	    String minLng = blPoint[0];
	    String minLat = blPoint[1];
	    String maxLng = trPoint[0];
	    String maxLat = trPoint[1];
		String wheresqlString = "";
		wheresqlString += "  rlc.longitude > " + minLng + " and rlc.longitude < " + maxLng
						 + " and rlc.latitude > " + minLat + " and rlc.latitude < " + maxLat + " and ";
		wheresqlString += " rlc.area_id in ("+areaStr+")";
		
		final String where = wheresqlString;
	
		return hibernateTemplate.executeFind(new HibernateCallback<List<String>>() {
					public List<String> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
								
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						
						//rownum从1开始
						String sql = "select CHINESENAME from (" +
								"select CHINESENAME,rownum as rn from  (" +
								"select distinct(CHINESENAME) from (" +
								"select rlc.LTE_CELL_ID as id,"+
								" rlc.pci as pci," +
								" rlc.business_cell_id as cell," +
								" rlc.CELL_NAME as chineseName," +
								" rlc.LONGITUDE as lng," +
								" rlc.LATITUDE as lat," +
								" rlc.AZIMUTH   as azimuth," +
								" rlcms.MAP_TYPE   as mapType," +
								" rlcms.SHAPE_TYPE as shapeType," +
								" rlcms.SHAPE_DATA as allLngLats" +
								" from  RNO_LTE_CELL rlc," +
								" RNO_LTE_CELL_MAP_SHAPE rlcms, " +
								" RNO_4G_STS_MEA_DATA r4pm" +
								" where rlc.LTE_CELL_ID = rlcms.LTE_CELL_ID AND" +
								" rlc.CELL_NAME = r4pm.PMUSERLABEL " +
								" and "+where+" ))) where rn>="+start+" and rn<="+end;
						log.debug("分页获取指定区域的网格下的指定频点类型的小区的start="+start+",end="+end+",sql:" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> result = query.list();
						List<String> re=new ArrayList<String>();
						for(Map<String,Object> map:result){
							re.add(map.get("CHINESENAME").toString());
						}
						log.debug("getRnoLteCellByGridAndPageInAreaWithPm.result="+re);
						return re;
					}
				});
	}
	/**
	 * 通过小区名查询4G话统小区数据
	 * @param cellName
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteStsAnaDao#getRnoLteCellForStsByCellName(java.lang.String)
	 * @author chen.c10
	 * @date 2015-7-30 下午3:34:20
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public List<RnoLteCellForPm> getRnoLteCellForStsByCellName(final String cellName){
		log.debug("进入方法：getRnoLteCellForStsByCellName.cellName="+cellName);
		return hibernateTemplate.executeFind(new HibernateCallback<List<RnoLteCellForPm>>() {
					public List<RnoLteCellForPm> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						//rownum从1开始
						String sql = "select  distinct rlc.LTE_CELL_ID as id,"+
											" rlc.pci as pci," +
											" rlc.business_cell_id as cell," +
											" rlc.CELL_NAME    as chineseName," +
											" rlc.LONGITUDE    as lng," +
											" rlc.LATITUDE     as lat," +
											" rlc.AZIMUTH   as azimuth," +
											" rlc.INTEGRATED AS impotgrade," +
											" rlcms.MAP_TYPE   as mapType," +
											" rlcms.SHAPE_TYPE as shapeType," +
											" rlcms.SHAPE_DATA as allLngLats," +
											" r4pm.PDCP_UPOCTUL," +
											" r4pm.PDCP_UPOCTDL," +
											" r4pm.CONTEXT_SUCCINITALSETUP," +
											" r4pm.CONTEXT_ATTRELENB," +
											" r4pm.CONTEXT_ATTRELENB_NORMAL," +
											" r4pm.ERAB_NBRSUCCESTAB," +
											" r4pm.ERAB_NBRATTESTAB," +
											" r4pm.HO_SUCCOUTINTERENBS1," +
											" r4pm.HO_SUCCOUTINTERENBX2," +
											" r4pm.HO_ATTOUTINTRAENB," +
											" r4pm.HO_SUCCOUTINTRAENB," +
											" r4pm.HO_ATTOUTINTERENBS1," +
											" r4pm.HO_ATTOUTINTERENBX2," +
											" r4pm.RRC_ATTCONNESTAB," +
											" r4pm.ERAB_NBRREQRELENB," +
											" r4pm.ERAB_NBRREQRELENB_NORMAL," +
											" r4pm.ERAB_HOFAIL," +
											" r4pm.RRC_SUCCCONNESTAB," +
											" r4pf.BEGINTIME" +
											" from  RNO_LTE_CELL rlc," +
											" RNO_LTE_CELL_MAP_SHAPE rlcms, " +
											" RNO_4G_STS_MEA_DATA r4pm, " +
											" RNO_4G_STS_DESC r4pf" +
											" where rlc.LTE_CELL_ID = rlcms.LTE_CELL_ID AND" +
											" rlc.CELL_NAME = r4pm.PMUSERLABEL AND" +
											" r4pm.FHEAD_ID=r4pf.ID AND" +
											" r4pm.PMUSERLABEL='"+cellName+"' " +
											" order by r4pf.BEGINTIME asc";
						log.debug("获取小区详情getRnoLteCellForStsByCellName.SQL="+sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> result = query.list();
						
						Map<String,Object> map = null;
						List<RnoLteCellForPm> lteCells = new ArrayList<RnoLteCellForPm>();
						RnoLteCellForPm lteCell = null;

						for (int i = 0; i < result.size(); i++) {
							map = (Map<String,Object>)result.get(i);
							lteCell = new RnoLteCellForPm();
							lteCell.setId(((BigDecimal) map.get("ID")).longValue());
							lteCell.setCell(map.get("CELL").toString());
							lteCell.setLng(((BigDecimal) map.get("LNG")).doubleValue());
							lteCell.setLat(((BigDecimal) map.get("LAT")).doubleValue());
							lteCell.setChineseName(map.get("CHINESENAME").toString());
							if(map.get("AZIMUTH")==null){
								lteCell.setAzimuth(null);
							} else {
								lteCell.setAzimuth(((BigDecimal)map.get("AZIMUTH")).floatValue());
							}
							if(map.get("IMPOTGRADE")==null){
								lteCell.setImpotGrade(null);
							}else{
								lteCell.setImpotGrade(map.get("IMPOTGRADE").toString());
							}
							lteCell.setMapType(map.get("MAPTYPE").toString());
							lteCell.setShapeType(map.get("SHAPETYPE").toString());
							lteCell.setAllLngLats(map.get("ALLLNGLATS").toString());
							lteCell.setPci(map.get("PCI").toString());
							lteCell.setPdcp_UpOctUl(((BigDecimal) map.get("PDCP_UPOCTUL")).floatValue());
							lteCell.setPdcp_UpOctDl(((BigDecimal) map.get("PDCP_UPOCTDL")).floatValue());
							lteCell.setContext_SuccInitalSetup(((BigDecimal) map.get("CONTEXT_SUCCINITALSETUP")).floatValue());
							lteCell.setContext_AttRelEnb(((BigDecimal) map.get("CONTEXT_ATTRELENB")).floatValue());
							lteCell.setContext_AttRelEnb_Normal(((BigDecimal) map.get("CONTEXT_ATTRELENB_NORMAL")).floatValue());
							lteCell.setErab_NbrSuccEstab(((BigDecimal) map.get("ERAB_NBRSUCCESTAB")).floatValue());
							lteCell.setErab_NbrAttEstab(((BigDecimal) map.get("ERAB_NBRATTESTAB")).floatValue());
							lteCell.setHo_SuccOutInterEnbS1(((BigDecimal) map.get("HO_SUCCOUTINTERENBS1")).floatValue());
							lteCell.setHo_SuccOutInterEnbX2(((BigDecimal) map.get("HO_SUCCOUTINTERENBX2")).floatValue());
							lteCell.setHo_AttOutIntraEnb(((BigDecimal) map.get("HO_ATTOUTINTRAENB")).floatValue());
							lteCell.setHo_SuccOutIntraEnb(((BigDecimal) map.get("HO_SUCCOUTINTRAENB")).floatValue());
							lteCell.setHo_AttOutInterEnbS1(((BigDecimal) map.get("HO_ATTOUTINTERENBS1")).floatValue());
							lteCell.setHo_AttOutInterEnbX2(((BigDecimal) map.get("HO_ATTOUTINTERENBX2")).floatValue());
							lteCell.setRrc_AttConnEstab(((BigDecimal) map.get("RRC_ATTCONNESTAB")).floatValue());
							lteCell.setErab_NbrReqRelEnb(((BigDecimal) map.get("ERAB_NBRREQRELENB")).floatValue());
							lteCell.setErab_NbrReqRelEnb_Normal(((BigDecimal) map.get("ERAB_NBRREQRELENB_NORMAL")).floatValue());
							lteCell.setErab_HoFail(((BigDecimal) map.get("ERAB_HOFAIL")).floatValue());
							lteCell.setRrc_SuccConnEstab(((BigDecimal) map.get("RRC_SUCCCONNESTAB")).floatValue());
							lteCell.setBeginTime((Timestamp) map.get("BEGINTIME"));
							lteCells.add(lteCell);
						}
						log.debug("getRnoLteCellForStsByCellName.result="+lteCells);
						return lteCells;
					}
				});		
	}
	/**
	 * 获取区域内总小区数目
	 * @param areaStr
	 * @param blPoint
	 * @param trPoint
	 * @param freqType
	 * @return
	 * @author chen.c10
	 * @date 2015-7-27 下午7:24:06
	 * @company 怡创科技
	 * @version V1.0
	 */
	public Integer getRnoLteStsCellCntByGridInArea(String areaStr,
			String[] blPoint, String[] trPoint, String freqType) {
		log.debug("进入方法:getRnoLteStsCellCntByGridInArea。 areaStr=" + areaStr
				+ ", blPoint=" + blPoint + ", trPoint=" + trPoint + ", freqType="+ freqType);
	    String minLng = blPoint[0];
	    String minLat = blPoint[1];
	    String maxLng = trPoint[0];
	    String maxLat = trPoint[1];
	    
	    String wheresqlString = "";
		if ("E频段".equals(freqType)) {
			wheresqlString = " BAND_TYPE='E频段' AND ";
		}
		if ("D频段".equals(freqType)) {
			wheresqlString = " BAND_TYPE='D频段' AND ";
		}
		if ("F频段".equals(freqType)) {
			wheresqlString = " BAND_TYPE='F频段' AND ";
		}
		wheresqlString += "  longitude > " + minLng + " and longitude < " + maxLng
						 + " and latitude > " + minLat + " and latitude < " + maxLat + " and ";
		wheresqlString += " area_id in ("+areaStr+")";
		final String where = wheresqlString;
		
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			@SuppressWarnings("unchecked")
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				String w = (where == null || where.trim().isEmpty()) ? ""
						: " and " + where;
				String sql = "select count(lte_cell_id) from RNO_LTE_CELL rlc,(select distinct(PMUSERLABEL) from RNO_4G_STS_MEA_DATA) r4pm where rlc.CELL_NAME = r4pm.PMUSERLABEL " + w;
				log.debug("获取指定区域的网格下的指定频点类型的小区数量的sql：" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				List<Object> list = query.list();
				int cnt = 0;
				if (list != null && list.size() > 0) {
					cnt = Integer.valueOf(list.get(0).toString());
				}
				log.debug("getRnoLteStsCellCntByGridInArea.result="+cnt);
				return cnt;
			}
		});
	}
	/**
	 * 通过小区列表查找4G话统数据。
	 * @param cellNameList
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteStsAnaDao#getRnoLteCellForStsByCellNameList(java.util.List)
	 * @author chen.c10
	 * @date 2015-8-12 下午5:19:54
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public Map<String,List<RnoLteCellForPm>> getRnoLteCellForStsByCellNameList(List<String> cellNameList){
		log.debug("进入方法:getRnoLteCellForStsByCellNameList。 cellNameList=" + cellNameList);
		StringBuffer sb = new StringBuffer();
		for(String cell:cellNameList){
			sb.append("'"+cell.trim()+"',");
		}
		final String cellNames =(sb.length()>0)?sb.substring(0,sb.length()-1):"''";
		List<RnoLteCellForPm> lteCells = hibernateTemplate.executeFind(new HibernateCallback<List<RnoLteCellForPm>>() {
					public List<RnoLteCellForPm> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						//rownum从1开始
						String sql = "select  distinct rlc.LTE_CELL_ID as id,"+
											" rlc.pci as pci," +
											" rlc.business_cell_id as cell," +
											" rlc.CELL_NAME    as chineseName," +
											" rlc.LONGITUDE    as lng," +
											" rlc.LATITUDE     as lat," +
											" rlc.AZIMUTH   as azimuth," +
											" rlc.INTEGRATED AS impotgrade," +
											" rlcms.MAP_TYPE   as mapType," +
											" rlcms.SHAPE_TYPE as shapeType," +
											" rlcms.SHAPE_DATA as allLngLats," +
											" r4pm.PDCP_UPOCTUL," +
											" r4pm.PDCP_UPOCTDL," +
											" r4pm.CONTEXT_SUCCINITALSETUP," +
											" r4pm.CONTEXT_ATTRELENB," +
											" r4pm.CONTEXT_ATTRELENB_NORMAL," +
											" r4pm.ERAB_NBRSUCCESTAB," +
											" r4pm.ERAB_NBRATTESTAB," +
											" r4pm.HO_SUCCOUTINTERENBS1," +
											" r4pm.HO_SUCCOUTINTERENBX2," +
											" r4pm.HO_ATTOUTINTRAENB," +
											" r4pm.HO_SUCCOUTINTRAENB," +
											" r4pm.HO_ATTOUTINTERENBS1," +
											" r4pm.HO_ATTOUTINTERENBX2," +
											" r4pm.RRC_ATTCONNESTAB," +
											" r4pm.ERAB_NBRREQRELENB," +
											" r4pm.ERAB_NBRREQRELENB_NORMAL," +
											" r4pm.ERAB_HOFAIL," +
											" r4pm.RRC_SUCCCONNESTAB," +
											" r4pf.BEGINTIME" +
											" from  RNO_LTE_CELL rlc," +
											" RNO_LTE_CELL_MAP_SHAPE rlcms, " +
											" RNO_4G_STS_MEA_DATA r4pm, " +
											" RNO_4G_STS_DESC r4pf" +
											" where rlc.LTE_CELL_ID = rlcms.LTE_CELL_ID AND" +
											" rlc.CELL_NAME = r4pm.PMUSERLABEL AND" +
											" r4pm.FHEAD_ID=r4pf.ID AND" +
											" r4pm.PMUSERLABEL in("+cellNames+") " +
											" order by r4pf.BEGINTIME asc";
						log.debug("获取小区详情getRnoLteCellForStsByCellNameList.SQL="+sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> result = query.list();
						List<RnoLteCellForPm> lteCells = new ArrayList<RnoLteCellForPm>();
						Map<String,Object> map = null;
						RnoLteCellForPm lteCell = null;
						for (int i = 0; i < result.size(); i++) {
							map = (Map<String,Object>)result.get(i);
							lteCell = new RnoLteCellForPm();
							lteCell.setId(((BigDecimal) map.get("ID")).longValue());
							lteCell.setCell(map.get("CELL").toString());
							lteCell.setLng(((BigDecimal) map.get("LNG")).doubleValue());
							lteCell.setLat(((BigDecimal) map.get("LAT")).doubleValue());
							lteCell.setChineseName(map.get("CHINESENAME").toString());
							if(map.get("AZIMUTH")==null){
								lteCell.setAzimuth(null);
							} else {
								lteCell.setAzimuth(((BigDecimal)map.get("AZIMUTH")).floatValue());
							}
							if(map.get("IMPOTGRADE")==null){
								lteCell.setImpotGrade(null);
							}else{
								lteCell.setImpotGrade(map.get("IMPOTGRADE").toString());
							}
							lteCell.setMapType(map.get("MAPTYPE").toString());
							lteCell.setShapeType(map.get("SHAPETYPE").toString());
							lteCell.setAllLngLats(map.get("ALLLNGLATS").toString());
							lteCell.setPci(map.get("PCI").toString());
							lteCell.setPdcp_UpOctUl(((BigDecimal) map.get("PDCP_UPOCTUL")).floatValue());
							lteCell.setPdcp_UpOctDl(((BigDecimal) map.get("PDCP_UPOCTDL")).floatValue());
							lteCell.setContext_SuccInitalSetup(((BigDecimal) map.get("CONTEXT_SUCCINITALSETUP")).floatValue());
							lteCell.setContext_AttRelEnb(((BigDecimal) map.get("CONTEXT_ATTRELENB")).floatValue());
							lteCell.setContext_AttRelEnb_Normal(((BigDecimal) map.get("CONTEXT_ATTRELENB_NORMAL")).floatValue());
							lteCell.setErab_NbrSuccEstab(((BigDecimal) map.get("ERAB_NBRSUCCESTAB")).floatValue());
							lteCell.setErab_NbrAttEstab(((BigDecimal) map.get("ERAB_NBRATTESTAB")).floatValue());
							lteCell.setHo_SuccOutInterEnbS1(((BigDecimal) map.get("HO_SUCCOUTINTERENBS1")).floatValue());
							lteCell.setHo_SuccOutInterEnbX2(((BigDecimal) map.get("HO_SUCCOUTINTERENBX2")).floatValue());
							lteCell.setHo_AttOutIntraEnb(((BigDecimal) map.get("HO_ATTOUTINTRAENB")).floatValue());
							lteCell.setHo_SuccOutIntraEnb(((BigDecimal) map.get("HO_SUCCOUTINTRAENB")).floatValue());
							lteCell.setHo_AttOutInterEnbS1(((BigDecimal) map.get("HO_ATTOUTINTERENBS1")).floatValue());
							lteCell.setHo_AttOutInterEnbX2(((BigDecimal) map.get("HO_ATTOUTINTERENBX2")).floatValue());
							lteCell.setRrc_AttConnEstab(((BigDecimal) map.get("RRC_ATTCONNESTAB")).floatValue());
							lteCell.setErab_NbrReqRelEnb(((BigDecimal) map.get("ERAB_NBRREQRELENB")).floatValue());
							lteCell.setErab_NbrReqRelEnb_Normal(((BigDecimal) map.get("ERAB_NBRREQRELENB_NORMAL")).floatValue());
							lteCell.setErab_HoFail(((BigDecimal) map.get("ERAB_HOFAIL")).floatValue());
							lteCell.setRrc_SuccConnEstab(((BigDecimal) map.get("RRC_SUCCCONNESTAB")).floatValue());
							lteCell.setBeginTime((Timestamp) map.get("BEGINTIME"));
							lteCells.add(lteCell);
						}
						log.debug("getRnoLteCellForStsByCellNameList.read="+lteCells);
						return lteCells;
					}
				});	
		//根据小区名进行封装。
		Map<String,List<RnoLteCellForPm>> cellToNameMap = new HashMap<String,List<RnoLteCellForPm>>();
		String cellNameTemp =null;
		List<RnoLteCellForPm> lteCellsTemp = null;
		for(RnoLteCellForPm c:lteCells){
			cellNameTemp = new String(c.getChineseName());
			
			if(!cellToNameMap.containsKey(cellNameTemp)){
				lteCellsTemp = new ArrayList<RnoLteCellForPm>();
				lteCellsTemp.add(c);
				cellToNameMap.put(cellNameTemp, lteCellsTemp);
			}else{
				cellToNameMap.get(cellNameTemp).add(c);
			}
		}
		//按测量开始时间进行降序排列
		List<RnoLteCellForPm> lteCellsSort = null;
		RnoLteCellForPm lcTemp =null;
		for(String c:cellToNameMap.keySet()){
			lteCellsSort = cellToNameMap.get(c);
			lcTemp = new RnoLteCellForPm();
			for (int i = 0; i < lteCellsSort.size(); i++) {
				for (int j = i+1; j < lteCellsSort.size(); j++) {
					if(lteCellsSort.get(i).getBeginTime().getTime()<lteCellsSort.get(j).getBeginTime().getTime()){
						lcTemp = lteCellsSort.get(i);
						lteCellsSort.set(i,lteCellsSort.get(j));
						lteCellsSort.set(j,lcTemp);
					}
				}
			}
		}
		log.debug("getRnoLteCellForStsByCellNameList.result="+cellToNameMap);
		return cellToNameMap;
	}
	/**
	 * 获取4G话统指标项
	 * @return
	 * @author chen.c10
	 * @date 2015-9-17 下午5:04:49
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getRno4GStsIndexDesc() {
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String,Object>>>() {
			public List<Map<String,Object>> doInHibernate(Session session)	throws HibernateException, SQLException {
				log.debug("进入方法:getRno4GStsIndexDesc。");
				String sql = "select r4sid.Index_Real_Name, r4sid.Index_Display_Name from RNO_4G_STS_INDEX_DESC r4sid order by r4sid.Index_Order";
				log.debug("getRno4GStsIndexDesc,sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String,Object>> rows = query.list();
				log.debug("离开方法:getRno4GStsIndexDesc。list=" + rows);
				return rows;
				}
			});
		}
}
