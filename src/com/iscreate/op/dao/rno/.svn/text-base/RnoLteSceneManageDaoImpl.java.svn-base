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
import com.iscreate.op.pojo.rno.AreaRectangle;
import com.iscreate.op.pojo.rno.RnoLteCell;
import com.iscreate.op.pojo.rno.RnoMapLnglatRelaGps;
import com.iscreate.op.service.rno.tool.RnoHelper;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;
import com.iscreate.plat.system.datasourcectl.DataSourceConst;
import com.iscreate.plat.system.datasourcectl.DataSourceContextHolder;

public class RnoLteSceneManageDaoImpl implements RnoLteSceneManageDao {
	private static Log log = LogFactory.getLog(RnoLteSceneManageDaoImpl.class);

	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 
	 * 
	 * @title 查询lte网格数据数量通过城市ID
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-7-5上午11:56:15
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public long queryLteGridDataCntByCityId(final long cityId){
		log.debug("queryLteGridDataCntByCityId.cityId=" + cityId);

		return hibernateTemplate.execute(new HibernateCallback<Long>() {
			@Override
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				
				String sql = "select count(ID) from RNO_4G_GRID_DATA  where area_id="
						+ cityId;
				log.debug("queryLteGridDataCntByCityId,sql=" + sql);
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
	 * @title 分页查询符合条件的LTE网格数据记录
	 * @param cityId
	 * @param page
	 * @return
	 * @author chao.xj
	 * @date 2015-7-5下午12:05:29
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryLteGridDataByPage(final 
			long cityId,final Page page){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						String field_inner="id,area_id,grid_id,Grid_Desc,grid_lnglats,Grid_Center,to_char(create_time,'yyyy/mm/dd hh24:mi:ss') Create_Time";
						String sql = "select * from (select "
								+ field_inner
								+ ",rownum rn from RNO_4G_GRID_DATA  where area_id="
								+ cityId + " order by id desc) where  rn>="+start+" and rn<="+end+" order by create_time asc";
						log.debug("queryLteGridDataByPage ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 通过城市ID获取符合条件的LTE网格数据记录
	 * @param cityId
	 * @return
	 * @author chao.xj
	 * @date 2015-7-6上午9:49:00
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> queryLteGridDataByCityId(final 
			long cityId){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "select grid_id,grid_desc,grid_lnglats,grid_center from RNO_4G_GRID_DATA where area_id="
								+ cityId + " order by grid_id asc";
						log.debug("queryLteGridDataByCityId ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						StringBuffer sb=new StringBuffer();
						if(rows!=null){
							for (Map<String, Object> map : rows) {
								Clob lnglatsClob = (Clob) map.get("GRID_LNGLATS");
								Reader inStream = null;
								try {
									inStream = lnglatsClob.getCharacterStream();
									char[] lnglatsChar = new char[(int) lnglatsClob.length()];
									inStream.read(lnglatsChar);
									String[] lnglatStrs = new String(lnglatsChar).trim().split(";");
									for (String c : lnglatStrs) { 
										if (!"".equals(c.trim())) {
											sb.append(c+";");
										}
									}
									map.put("GRID_LNGLATS", sb.substring(0, sb.length()-1).toString());
									sb.setLength(0);
								} catch (SQLException e2) {
									e2.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} 
							}
						}
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 通过城市ID导出符合条件的LTE网格数据记录
	 * @param cityId
	 * @return
	 * @author li.tf
	 * @date 2015-9-14上午16:42:00
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<Map<String, Object>> exportLteGridDataByCityId(final 
			long cityId){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "select * from RNO_LTE_CELL rlc where rlc.area_id="+ cityId; 
								/*+ "union select grid_id from RNO_4G_GRID_DATA r4gd where r4gd.area_id=rlc.area_id and rlc.area_id="+cityId;*/
						log.debug("exportLteGridDataByCityId ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> rows = query.list();
						return rows;
					}
				});
	}
	/**
	 * 
	 * @title 通过区域ID获取指定区域的地图经纬度纠偏map集合
	 * @param areaid
	 * @param mapType
	 * @return
	 * @author chao.xj
	 * @date 2015-7-6下午5:07:53
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public Map<AreaRectangle, List<RnoMapLnglatRelaGps>> getSpecialAreaRnoMapLnglatRelaGpsMapList(Statement stmt,
			long areaid, String mapType) {
		List<RnoMapLnglatRelaGps> list = getSpecialAreaRnoMapLnglatRelaGpsList(stmt,areaid, mapType);
		Map<AreaRectangle, List<RnoMapLnglatRelaGps>> map = new HashMap<AreaRectangle, List<RnoMapLnglatRelaGps>>();

		AreaRectangle rec;
		String topleft = null;
		String bottomright = null;
		double minLng, maxLng, minLat, maxLat;
		for (RnoMapLnglatRelaGps rnoMapLnglatRelaGps : list) {
			topleft = rnoMapLnglatRelaGps.getTopleft();
			bottomright = rnoMapLnglatRelaGps.getBottomright();

			if (topleft == null || bottomright == null) {
				log.error("topleft =" + topleft + ",bottomRight=" + bottomright);
				continue;
			}

			String[] tls = topleft.split(",");
			String[] brs = bottomright.split(",");
			if (tls.length != 2 || tls.length != 2) {
				log.error("topleft =" + topleft + ",bottomRight=" + bottomright
						+ ",用逗号分解得到的数组元素数量不等于2");
				continue;
			}

			try {
				minLng = Double.parseDouble(tls[0]);
				maxLng = Double.parseDouble(brs[0]);
				minLat = Double.parseDouble(brs[1]);
				maxLat = Double.parseDouble(tls[1]);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			rec = new AreaRectangle(minLng, maxLng, minLat, maxLat);

			if (!map.containsKey(rec)) {
				// log.info("子矩形：" + rec);
				List<RnoMapLnglatRelaGps> rmgpsList = new ArrayList<RnoMapLnglatRelaGps>();
				rmgpsList.add(rnoMapLnglatRelaGps);
				// System.out.println("不包括："+rmgpsList.size());
				map.put(rec, rmgpsList);
			} else {
				map.get(rec).add(rnoMapLnglatRelaGps);
			}
		}
		return map;
	}
	/**
	 * 
	 * @title 通过区域ID获取指定区域的地图经纬度纠偏集合
	 * @param areaid
	 * @param mapType
	 * @return
	 * @author chao.xj
	 * @date 2015-7-6下午5:07:34
	 * @company 怡创科技
	 * @version 2.0.1
	 */
	public List<RnoMapLnglatRelaGps> getSpecialAreaRnoMapLnglatRelaGpsList(Statement stmt,
			long areaid, String mapType) {

		log.info("进入方法：getSpecialAreaRnoMapLnglatRelaGpsList。areaId=" + areaid);
		String fieldName = "RESOURCE_USE_RATE";// "TRAFFIC_PER_LINE";

		// final String
		// sql="SELECT DISTINCT(bottomleft),GPS,BAIDU,OFFSET,AREAID,TOPLEFT,TOPRIGHT,BOTTOMRIGHT,MAP_LNGLAT_RELA_GPS_ID from RNO_MAP_LNGLAT_RELA_GPS where areaid='"+areaid+"' ORDER BY bottomleft";
		final String sql = "SELECT * from RNO_MAP_LNGLAT_RELA_GPS WHERE AREAID='"
				+ areaid
				+ "' and MAPTYPE='"
				+ mapType
				+ "' ORDER BY BOTTOMLEFT";
		List<Map<String, Object>> relagps= RnoHelper.commonQuery(stmt, sql);
		Map<String, Object> relagpsMap = null;
		RnoMapLnglatRelaGps mapLnglatRelaGps=null;
		List<RnoMapLnglatRelaGps> list=new ArrayList<RnoMapLnglatRelaGps>();
		for (int i = 0; i < relagps.size(); i++) {
			relagpsMap = relagps.get(i);
			mapLnglatRelaGps = new RnoMapLnglatRelaGps();
			mapLnglatRelaGps.setAreaid(areaid);
			mapLnglatRelaGps.setBottomleft(relagpsMap.get("BOTTOMLEFT").toString());
			mapLnglatRelaGps.setBottomright(relagpsMap.get("BOTTOMRIGHT").toString());
			mapLnglatRelaGps.setGps(relagpsMap.get("GPS").toString());
//			mapLnglatRelaGps.setId(id);
			mapLnglatRelaGps.setMaptype(relagpsMap.get("MAPTYPE").toString());
			mapLnglatRelaGps.setOffset(relagpsMap.get("OFFSET").toString());
			mapLnglatRelaGps.setTargetlnglat(relagpsMap.get("TARGETLNGLAT").toString());
			mapLnglatRelaGps.setTopleft(relagpsMap.get("TOPLEFT").toString());
			mapLnglatRelaGps.setTopright(relagpsMap.get("TOPRIGHT").toString());
			list.add(mapLnglatRelaGps);
		}
		return list;
		/*return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoMapLnglatRelaGps>>() {
					public List<RnoMapLnglatRelaGps> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						SQLQuery query = arg0.createSQLQuery(sql);
						// query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						query.addEntity(RnoMapLnglatRelaGps.class);
						List list = query.list();
						
						 * for (int i = 0; i < list.size(); i++) {
						 * System.out.println
						 * (((RnoMapLnglatRelaGps)list.get(i)).getGps()); }
						 
						return list;
					}
				});
*/
	}
	/**
	 *  通过场景ID查询时间场景信息
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteSceneManageDao#queryLteRegionSceneInfo()
	 * @author chen.c10
	 * @date 2015-7-7 下午1:46:58
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLteTimeSceneInfoBySceneId(final String sceneId){
		return  hibernateTemplate.executeFind(new HibernateCallback <List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "select *  from RNO_4G_Time_SCENE_MANAGEMENT where ID="+Integer.parseInt(sceneId);
						log.debug("queryLteTimeRegionSceneInfo ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> scenes = query.list();
						return scenes;
					}
				});
	}

	/**
	 * 更新时间场景信息
	 * @param sceneDataMap
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteSceneManageDao#updateLteRegionSceneData(java.util.Map)
	 * @author chen.c10
	 * @date 2015-7-8 下午6:19:24
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean updateLteTimeSceneDataBySceneId(final Map<String, String> sceneParam) {
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
					public Boolean doInHibernate(Session arg0)
							throws HibernateException, SQLException {					
						Map<String, String> map=sceneParam;
							String sql = "update RNO_4G_Time_SCENE_MANAGEMENT R4SM " +
						" set	 R4SM.INTERRATHOA2THDRSRP =" +map.get("INTERRATHOA2THDRSRP")+
						" , R4SM.INTERRATHOA1THDRSRP   =" + map.get("INTERRATHOA1THDRSRP")+
						" , R4SM.INTERRATHOUTRANB1HYST =" + map.get("INTERRATHOUTRANB1HYST")+
						" , R4SM.INTERRATHOUTRANB1THDRSCP = " + map.get("INTERRATHOUTRANB1THDRSCP")+
						" , R4SM.INTERRATHOA1A2TIMETOTRIG =" + map.get("INTERRATHOA1A2TIMETOTRIG")+
						" , R4SM.INTERRATHOA1A2HYST =" + map.get("INTERRATHOA1A2HYST")+
						" , R4SM.BLINDHOA1A2THDRSRP  =" + map.get("BLINDHOA1A2THDRSRP")+
						" , R4SM.INTERFREQHOA1A2TIMETOTRIG  =" + map.get("INTERFREQHOA1A2TIMETOTRIG")+
						" , R4SM.A3INTERFREQHOA1THDRSRP    =" + map.get("A3INTERFREQHOA1THDRSRP")+
						" , R4SM.A3INTERFREQHOA2THDRSRP    =" + map.get("A3INTERFREQHOA2THDRSRP")+
						" , R4SM.INTERFREQHOA3OFFSET    =" + map.get("INTERFREQHOA3OFFSET")+
						" , R4SM.INTERFREQHOA1A2HYST   =" + map.get("INTERFREQHOA1A2HYST")+
						" , R4SM.QRXLEVMIN   =" + map.get("QRXLEVMIN")+
						" , R4SM.SNONINTRASEARCH   =" + map.get("SNONINTRASEARCH")+
						" , R4SM.THRSHSERVLOW  =" + map.get("THRSHSERVLOW")+
						" , R4SM.TRESELEUTRAN  =" + map.get("TRESELEUTRAN")+
						" , R4SM.CELLRESELPRIORITY =" + map.get("CELLRESELPRIORITY")+
						"  where R4SM.ID=" +map.get("sceneId");
							log.debug("updateLteTimeSceneDataBySceneId ,sql=" + sql);
							SQLQuery query = arg0.createSQLQuery(sql);
							int cnt=query.executeUpdate();
							if(cnt>0){
							return true;
						}else {
							return false;
						}
					}
		});
	}

	/**
	 * 查询时间场景名称列表
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteSceneManageDao#queryLteSceneNameListBySceneType(java.lang.String)
	 * @author chen.c10
	 * @date 2015-7-9 下午12:21:29
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLteTimeSceneNameList() {
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = "select ID,NAME  from RNO_4G_Time_SCENE_MANAGEMENT";
				log.debug("queryLteTimeSceneNameListBySceneType ,sql=" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> rows = query.list();
				return rows;
			}
		});
	}
	/**
	 * 通过场景ID删除时间场景信息
	 * @param sceneId
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteSceneManageDao#deleteLteSceneDataForAjaxAction(java.lang.String)
	 * @author chen.c10
	 * @date 2015-7-10 上午11:20:08
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean deleteLteTimeSceneData(final String sceneId){
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {	
				log.info("进入方法：deleteLteRegionSceneDataForAjaxAction.sceneId=" + sceneId);
				String sql = "delete from RNO_4G_Time_SCENE_MANAGEMENT where ID=" +sceneId;
				log.debug("deleteLteTimeSceneDataForAjaxAction ,sql=" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				int cnt=query.executeUpdate();
				if(cnt>0){
					return true;
					}else {
						return false;
						}
				}
			});
		}
/**
 * 插入时间场景信息
 * @param sceneParam
 * @return
 * @see com.iscreate.op.dao.rno.RnoLteSceneManageDao#insertLteRegionSceneData(java.util.Map)
 * @author chen.c10
 * @date 2015-7-13 上午11:59:01
 * @company 怡创科技
 * @version V1.0
 */
	public boolean insertLteTimeSceneData(final Map<String, String> sceneParam) {
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {	
				log.info("进入方法：insertLteTimeSceneData.sceneParam=" + sceneParam);
				Map<String, String> map=sceneParam;
				String sql = "insert into RNO_4G_TIME_SCENE_MANAGEMENT (ID,NAME" +
						",INTERRATHOA2THDRSRP,INTERRATHOA1THDRSRP,INTERRATHOUTRANB1HYST" +
						",INTERRATHOUTRANB1THDRSCP,INTERRATHOA1A2TIMETOTRIG,INTERRATHOA1A2HYST" +
						",BLINDHOA1A2THDRSRP,INTERFREQHOA1A2TIMETOTRIG,A3INTERFREQHOA1THDRSRP" +
						",A3INTERFREQHOA2THDRSRP,INTERFREQHOA3OFFSET,INTERFREQHOA1A2HYST" +
						",QRXLEVMIN,SNONINTRASEARCH,THRSHSERVLOW,TRESELEUTRAN,CELLRESELPRIORITY) " +
						"values(SEQ_RNO_4G_TIME_SCENE_MANA.NEXTVAL,'"+map.get("sceneName")+
						"',"+map.get("INTERRATHOA2THDRSRP")+","+map.get("INTERRATHOA1THDRSRP")+","+map.get("INTERRATHOUTRANB1HYST")+
						","+map.get("INTERRATHOUTRANB1THDRSCP")+","+map.get("INTERRATHOA1A2TIMETOTRIG")+","+map.get("INTERRATHOA1A2HYST")+","+map.get("BLINDHOA1A2THDRSRP")+
						","+map.get("INTERFREQHOA1A2TIMETOTRIG")+","+map.get("A3INTERFREQHOA1THDRSRP")+","+map.get("A3INTERFREQHOA2THDRSRP")+","+map.get("INTERFREQHOA3OFFSET")+
						","+map.get("INTERFREQHOA1A2HYST")+","+map.get("QRXLEVMIN")+","+map.get("SNONINTRASEARCH")+","+map.get("THRSHSERVLOW")+
						","+map.get("TRESELEUTRAN")+","+map.get("CELLRESELPRIORITY")+")";
				log.debug("insertLteTimeSceneData ,sql=" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				int cnt=query.executeUpdate();
				if(cnt>0){
					return true;
					}else {
						return false;
						}
				}
			});
	}
	
	/**
	 *  通过场景ID查询地理场景信息
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteSceneManageDao#queryLteRegionSceneInfo()
	 * @author chen.c10
	 * @date 2015-7-7 下午1:46:58
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLteRegionSceneInfoBySceneId(final String sceneId){
		return  hibernateTemplate.executeFind(new HibernateCallback <List<Map<String, Object>>>() {
					public List<Map<String, Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "select *  from RNO_4G_Region_SCENE_MANAGEMENT where ID="+Integer.parseInt(sceneId);
						log.debug("queryLteRegionRegionSceneInfo ,sql=" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String, Object>> scenes = query.list();
						return scenes;
					}
				});
	}

	/**
	 * 更新地理场景信息
	 * @param sceneDataMap
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteSceneManageDao#updateLteRegionSceneData(java.util.Map)
	 * @author chen.c10
	 * @date 2015-7-8 下午6:19:24
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean updateLteRegionSceneDataBySceneId(final Map<String, String> sceneParam) {
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
					public Boolean doInHibernate(Session arg0)
							throws HibernateException, SQLException {					
						Map<String, String> map=sceneParam;
							String sql = "update RNO_4G_Region_SCENE_MANAGEMENT R4SM " +
						" set	 R4SM.INTERRATHOA2THDRSRP =" +map.get("INTERRATHOA2THDRSRP")+
						" , R4SM.INTERRATHOA1THDRSRP   =" + map.get("INTERRATHOA1THDRSRP")+
						" , R4SM.INTERRATHOUTRANB1HYST =" + map.get("INTERRATHOUTRANB1HYST")+
						" , R4SM.INTERRATHOUTRANB1THDRSCP = " + map.get("INTERRATHOUTRANB1THDRSCP")+
						" , R4SM.INTERRATHOA1A2TIMETOTRIG =" + map.get("INTERRATHOA1A2TIMETOTRIG")+
						" , R4SM.INTERRATHOA1A2HYST =" + map.get("INTERRATHOA1A2HYST")+
						" , R4SM.BLINDHOA1A2THDRSRP  =" + map.get("BLINDHOA1A2THDRSRP")+
						" , R4SM.INTERFREQHOA1A2TIMETOTRIG  =" + map.get("INTERFREQHOA1A2TIMETOTRIG")+
						" , R4SM.A3INTERFREQHOA1THDRSRP    =" + map.get("A3INTERFREQHOA1THDRSRP")+
						" , R4SM.A3INTERFREQHOA2THDRSRP    =" + map.get("A3INTERFREQHOA2THDRSRP")+
						" , R4SM.INTERFREQHOA3OFFSET    =" + map.get("INTERFREQHOA3OFFSET")+
						" , R4SM.INTERFREQHOA1A2HYST   =" + map.get("INTERFREQHOA1A2HYST")+
						" , R4SM.QRXLEVMIN   =" + map.get("QRXLEVMIN")+
						" , R4SM.SNONINTRASEARCH   =" + map.get("SNONINTRASEARCH")+
						" , R4SM.THRSHSERVLOW  =" + map.get("THRSHSERVLOW")+
						" , R4SM.TRESELEUTRAN  =" + map.get("TRESELEUTRAN")+
						" , R4SM.CELLRESELPRIORITY =" + map.get("CELLRESELPRIORITY")+
						"  where R4SM.ID=" +map.get("sceneId");
							log.debug("updateLteRegionSceneDataBySceneId ,sql=" + sql);
							SQLQuery query = arg0.createSQLQuery(sql);
							int cnt=query.executeUpdate();
							if(cnt>0){
							return true;
						}else {
							return false;
						}
					}
		});
	}

	/**
	 * 查询地理场景名称列表
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteSceneManageDao#queryLteSceneNameListBySceneType(java.lang.String)
	 * @author chen.c10
	 * @date 2015-7-9 下午12:21:29
	 * @company 怡创科技
	 * @version V1.0
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryLteRegionSceneNameList() {
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = "select ID,NAME  from RNO_4G_Region_SCENE_MANAGEMENT";
				log.debug("queryLteRegionSceneNameListBySceneType ,sql=" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> rows = query.list();
				return rows;
			}
		});
	}
	/**
	 * 通过场景ID删除地理场景信息
	 * @param sceneId
	 * @return
	 * @see com.iscreate.op.dao.rno.RnoLteSceneManageDao#deleteLteSceneDataForAjaxAction(java.lang.String)
	 * @author chen.c10
	 * @date 2015-7-10 上午11:20:08
	 * @company 怡创科技
	 * @version V1.0
	 */
	public boolean deleteLteRegionSceneData(final String sceneId){
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {	
				log.info("进入方法：deleteLteRegionSceneDataForAjaxAction.sceneId=" + sceneId);
				String sql = "delete from RNO_4G_Region_SCENE_MANAGEMENT where ID=" +sceneId;
				log.debug("deleteLteRegionSceneDataForAjaxAction ,sql=" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				int cnt=query.executeUpdate();
				if(cnt>0){
					return true;
					}else {
						return false;
						}
				}
			});
		}
/**
 * 插入地理场景信息
 * @param sceneParam
 * @return
 * @see com.iscreate.op.dao.rno.RnoLteSceneManageDao#insertLteRegionSceneData(java.util.Map)
 * @author chen.c10
 * @date 2015-7-13 上午11:59:01
 * @company 怡创科技
 * @version V1.0
 */
	public boolean insertLteRegionSceneData(final Map<String, String> sceneParam) {
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			public Boolean doInHibernate(Session arg0)
					throws HibernateException, SQLException {	
				log.info("进入方法：insertLteRegionSceneData.sceneParam=" + sceneParam);
				Map<String, String> map=sceneParam;
				String sql = "insert into RNO_4G_REGION_SCENE_MANAGEMENT (ID,NAME" +
						",INTERRATHOA2THDRSRP,INTERRATHOA1THDRSRP,INTERRATHOUTRANB1HYST" +
						",INTERRATHOUTRANB1THDRSCP,INTERRATHOA1A2TIMETOTRIG,INTERRATHOA1A2HYST" +
						",BLINDHOA1A2THDRSRP,INTERFREQHOA1A2TIMETOTRIG,A3INTERFREQHOA1THDRSRP" +
						",A3INTERFREQHOA2THDRSRP,INTERFREQHOA3OFFSET,INTERFREQHOA1A2HYST" +
						",QRXLEVMIN,SNONINTRASEARCH,THRSHSERVLOW,TRESELEUTRAN,CELLRESELPRIORITY) " +
						"values(SEQ_RNO_4G_REGION_SCENE_MANA.NEXTVAL,'"+map.get("sceneName")+
						"',"+map.get("INTERRATHOA2THDRSRP")+","+map.get("INTERRATHOA1THDRSRP")+","+map.get("INTERRATHOUTRANB1HYST")+
						","+map.get("INTERRATHOUTRANB1THDRSCP")+","+map.get("INTERRATHOA1A2TIMETOTRIG")+","+map.get("INTERRATHOA1A2HYST")+","+map.get("BLINDHOA1A2THDRSRP")+
						","+map.get("INTERFREQHOA1A2TIMETOTRIG")+","+map.get("A3INTERFREQHOA1THDRSRP")+","+map.get("A3INTERFREQHOA2THDRSRP")+","+map.get("INTERFREQHOA3OFFSET")+
						","+map.get("INTERFREQHOA1A2HYST")+","+map.get("QRXLEVMIN")+","+map.get("SNONINTRASEARCH")+","+map.get("THRSHSERVLOW")+
						","+map.get("TRESELEUTRAN")+","+map.get("CELLRESELPRIORITY")+")";
				log.debug("insertLteRegionSceneData ,sql=" + sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				int cnt=query.executeUpdate();
				if(cnt>0){
					return true;
					}else {
						return false;
						}
				}
			});
	}
}
