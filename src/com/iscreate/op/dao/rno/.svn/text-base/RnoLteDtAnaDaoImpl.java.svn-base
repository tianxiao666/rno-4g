package com.iscreate.op.dao.rno;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.rno.LteCellQueryCondition;
import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.model.Eri2GNcsDescQueryCond;
import com.iscreate.op.action.rno.model.G4DtDescQueryCond;
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.RnoLteCell;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.service.rno.tool.CoordinateHelper;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class RnoLteDtAnaDaoImpl implements RnoLteDtAnaDao {
	private static Log log = LogFactory.getLog(RnoLteDtAnaDaoImpl.class);

	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 
	 * @title 查询符合条件的4g 路测描述记录数
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2015-7-8下午8:17:07
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long queryG4DtDescCnt(final G4DtDescQueryCond cond){
		log.debug("queryG4DtDescCnt.cond=" + cond);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String where = cond.buildWhereCont();
				if (!StringUtils.isBlank(where)) {
					where = " where " + where;
				}
				log.debug("queryG4DtDescCnt ,where=" + where);
				String sql = "select count(DT_SAMPLING_DESC_ID) from rno_4g_dt_sampling_desc "
						+ where;
				log.debug("queryG4DtDescCnt,sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				List<Object> list = query.list();
				Long cnt = 0l;
				if (list != null && list.size() > 0) {
					cnt = Long.valueOf(list.get(0).toString());
				}
				return cnt;
			}
		});
	}

	/**
	 * 
	 * @title 分页查询符合条件的4G 路测的描述记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-7-8下午8:23:59
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryG4DtDescByPage(final 
			G4DtDescQueryCond cond,final Page page){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field_out= " DT_SAMPLING_DESC_ID,FILE_NAME,DATA_TYPE,RECORD_COUNT,CITY_ID,MEA_TIME,create_time,area_type";
						String field_inner = "DT_SAMPLING_DESC_ID,FILE_NAME,DATA_TYPE,RECORD_COUNT,CITY_ID,TO_CHAR(MEA_TIME,'YYYY-MM-DD') MEA_TIME,TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME,area_type";
						String where = cond.buildWhereCont();
						log.debug("queryG4DtDescByPage ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select " + field_out + " from (select "
								+ field_inner
								+ ",rownum rn from rno_4g_dt_sampling_desc  "
								+ whereResult + " order by DT_SAMPLING_DESC_ID desc) where  rn>="+start+" and rn<="+end+" order by mea_time desc";
						log.debug("queryG4DtDescByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 通过条件查询某一天的DT描述信息返回分析页面
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2015-7-10上午9:48:19
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryG4DtDescInfoForMap(final 
			G4DtDescQueryCond cond){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field= " DT_SAMPLING_DESC_ID,FILE_NAME,DATA_TYPE,RECORD_COUNT,to_char(mea_time,'yyyy-MM-dd') MEA_TIME,area_type";
						String where = cond.buildWhereForMap();
						log.debug("queryG4DtDescInfoForMap ,where=" + where);
						String whereResult = (where == null || where.trim()
								.isEmpty()) ? ("") : (" where " + where);
						String sql = "select "+field+" from rno_4g_dt_sampling_desc where "+where;
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 通过条件查询某一天的DT详情信息返回分析页面
	 * @param descIdStr
	 * @return
	 * @author chao.xj
	 * @date 2015-7-10上午11:55:39
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryG4DtDetailInfoForMap(final 
			String descIdStr){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						log.debug("queryG4DtDetailInfoForMap ,descIdStr=" + descIdStr);
						String sql = "select * from rno_4g_dt_sampling where dt_sampling_desc_id in("+descIdStr+")";
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 查询符合条件的4g 路测描述记录数
	 * @param cond
	 * @return
	 * @author chao.xj
	 * @date 2015-7-8下午8:17:07
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long queryG4DtDetailDataCnt(final String descIdStr){
		log.debug("queryG4DtDetailDataCnt.descIdStr=" + descIdStr);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select count(DT_SAMPLING_DESC_ID) CNT from rno_4g_dt_sampling  where dt_sampling_desc_id in("+descIdStr+") and cell_name is not null ";
				log.debug("queryG4DtDetailDataCnt,sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				List<Object> list = query.list();
				Long cnt = 0l;
				if (list != null && list.size() > 0) {
					cnt = Long.valueOf(list.get(0).toString());
				}
				return cnt;
			}
		});
	}
	/**
	 * 
	 * @title 分页查询符合条件的4G 路测的詳情记录
	 * @param cond
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-7-8下午8:23:59
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryG4DtDetailDataByPage(final String descIdStr,final Page page,final Map<AreaRectangle, List<RnoMapLnglatRelaGps>> standardPoints){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
//						String fieldIner= " DT_SAMPLING_DESC_ID,FILE_NAME,DATA_TYPE,to_char(MEA_TIME,'yyyy-MM-dd') MEA_TIME,LONGITUDE ,LATITUDE ,CELL_NAME ,TAC,ECI,EARFCN,PCI,RSRP,RS_SINR,SCELL_DIST,SCELL_AZIMUTH_DIFF,APP_SPEED,NCELL_NAME_1,NCELL_EARFCN_1,NCELL_PCI_1,NCELL_RSRP_1,NCELL_NAME_2,NCELL_EARFCN_2,NCELL_PCI_2,NCELL_RSRP_2,NCELL_NAME_3,NCELL_EARFCN_3,NCELL_PCI_3,NCELL_RSRP_3,NCELL_NAME_4,NCELL_EARFCN_4,NCELL_PCI_4,NCELL_RSRP_4,NCELL_NAME_5,NCELL_EARFCN_5,NCELL_PCI_5,NCELL_RSRP_5,NCELL_NAME_6,NCELL_EARFCN_6,NCELL_PCI_6,NCELL_RSRP_6";
						String fieldOuter= " DT_SAMPLING_DESC_ID,FILE_NAME,DATA_TYPE,to_char(MEA_TIME,'yyyy-MM-dd') \"sampleTimeStr\",to_char(MEA_TIME,'yyyy-MM-dd hh24:mi:ss') MEA_TIME,LONGITUDE \"lng\",LATITUDE \"lat\",CELL_NAME \"cell\",TAC,ECI,EARFCN \"cellEarfcn\",PCI,RSRP \"cellRsrp\",RS_SINR \"cellRsSinr\",SCELL_DIST*1000 \"distance\",SCELL_AZIMUTH_DIFF,APP_SPEED,NCELL_NAME_1,NCELL_EARFCN_1,NCELL_PCI_1,NCELL_RSRP_1,ncell_name_1||','||ncell_name_2||','||ncell_name_3||','||ncell_name_4||','||ncell_name_5||','||ncell_name_6 \"ncells\"  ,ncell_rsrp_1||','||ncell_rsrp_2||','||ncell_rsrp_3||','||ncell_rsrp_4||','||ncell_rsrp_5||','||ncell_rsrp_6 \"ncellRsrps\" ,area_type \"areaType\",ncell_rsrp_1 \"ncellRsrp1\",ncell_rsrp_2 \"ncellRsrp2\",ncell_rsrp_3 \"ncellRsrp3\",ncell_rsrp_4 \"ncellRsrp4\",ncell_rsrp_5 \"ncellRsrp5\",ncell_rsrp_6 \"ncellRsrp6\" ";
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String sql = "select "+fieldOuter+" from (select  t1.*,t2.area_type "
//								+ fieldIner
								+ ",rownum rn from rno_4g_dt_sampling t1 left join rno_4g_dt_sampling_desc t2 on (t1.dt_sampling_desc_id=t2.dt_sampling_desc_id) where t1.dt_sampling_desc_id in("+descIdStr+")"
							    + " and t1.cell_name is not null order by t1.DT_SAMPLING_DESC_ID desc) where  rn>="+start+" and rn<="+end+" order by mea_time desc";
						log.debug("queryG4DtDetailDataByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						Map<String, Object> map = null;
						String cellBaiduPoint[] = null;
						// gps -> baidu 坐标
						for (int i = 0; i < rows.size(); i++) {
							map = rows.get(i);
							cellBaiduPoint = CoordinateHelper.getBaiduLnglat(
									Double.parseDouble(map.get("lng")
											.toString()), Double
											.parseDouble(map.get("lat")
													.toString()),
									standardPoints);
							if (cellBaiduPoint == null) {
								continue;
							}
//							System.out.println("经纬度==>>>>>>"+cellBaiduPoint[0]+"||"+cellBaiduPoint[1]);
							map.put("lng", Double.parseDouble(cellBaiduPoint[0]));
							map.put("lat", Double.parseDouble(cellBaiduPoint[1]));
						}
						return rows;
					}
				});
	}
}
