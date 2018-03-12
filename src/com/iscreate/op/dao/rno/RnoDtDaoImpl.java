package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.pojo.rno.RnoCellStructDescWrap;

public class RnoDtDaoImpl implements RnoDtDao {

	private static Log log = LogFactory.getLog(RnoBscDaoImpl.class);

	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	/**
	 * 获取符合条件的dt采样点的数量
	 * @param dtDescId
	 * @return
	 * @author chao.xj
	 * @date 2013-12-2上午09:36:11
	 */
	public int getRnoGsmDtSampleCount(final long dtDescId){
		
//		final String sql = "SELECT count(*) cnt from (SELECT to_number(SUBSTR(map_lnglat,1,INSTR(map_lnglat,',',1,1)-1)) \"lng\",to_number(SUBSTR(map_lnglat,INSTR(map_lnglat,',',1,1)+1)) \"lat\",CELL_LONGITUDE \"cellLng\",CELL_LATITUDE \"cellLat\",CELL \"cell\",RXLEVSUB \"rxLevSub\",RXQUALSUB \"rxQualSub\",DISTANCE \"distance\",ANGLE \"angle\",(NCELL_RXLEV_1||','||NCELL_RXLEV_2||','||NCELL_RXLEV_3||','||NCELL_RXLEV_4||','||NCELL_RXLEV_5||','||NCELL_RXLEV_6) \"ncellRxLevSubs\",(NCELL_NAME_1||','||NCELL_NAME_2||','||NCELL_NAME_3||','||NCELL_NAME_4||','||NCELL_NAME_5||','||NCELL_NAME_6) \"ncells\",TO_CHAR(SAMPLE_TIME,'yyyy-MM-dd hh24:mi:ss') \"sampleTimeStr\",( (nvl(NCELL_RXLEV_1,0)+nvl(NCELL_RXLEV_2,0)+nvl(NCELL_RXLEV_3,0)+nvl(NCELL_RXLEV_4,0)+nvl(NCELL_RXLEV_5,0)+nvl(NCELL_RXLEV_6,0))/NCELL_COUNT) \"avgNcellLevSub\",(SELECT bearing from CELL WHERE label=CELL) \"bearing\",cell_indoor \"cellIndoor\",(NCELL_INDOOR_1||','||NCELL_INDOOR_2||','||NCELL_INDOOR_3||','||NCELL_INDOOR_4||','||NCELL_INDOOR_5||','||NCELL_INDOOR_6) \"ncellIndoors\" from RNO_GSM_DT_SAMPLE WHERE RNO_DT_DESCRIPTOR_ID in ("+dtDescId+"))";
		final String sql="select count(rno_gsm_dt_sample_id) from RNO_GSM_DT_SAMPLE where RNO_DT_DESCRIPTOR_ID="+dtDescId;
		int cnt = hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				SQLQuery query = arg0.createSQLQuery(sql);

				List<Object> list = query.list();
				int cnt = 0;
				if (list != null && list.size() > 0) {
					cnt = Integer.valueOf(list.get(0).toString());
				}

				return cnt;
			}
		});
		return cnt;
	}
	/**
	 * 通过区分页查询采样点集合MAP数据
	 * @param dtDescId
	 * @param page
	 * @param attachParams
	 * @return
	 * @author chao.xj
	 * @date 2013-12-2上午10:05:58
	 */
	public List<Map<String, Object>> queryRnoGsmDtSampleListMapsByDescIdAndPage(final long dtDescId,
			Page page, final String mapId){
		if (page == null) {
			log.error("获取dt数据时，传入的分页参数为空！");
			return Collections.EMPTY_LIST;
		}
		
		final int startIndex = (page.getCurrentPage() - 1) * page.getPageSize()
				+ 1;
		final int endIndex = startIndex + page.getPageSize();
		//System.out.println("startIndex="+startIndex+"-----------------endIndex="+endIndex);
//		final String sql = "select * from (SELECT to_number(SUBSTR(map_lnglat,1,INSTR(map_lnglat,',',1,1)-1)) \"lng\",to_number(SUBSTR(map_lnglat,INSTR(map_lnglat,',',1,1)+1)) \"lat\",CELL_LONGITUDE \"cellLng\",CELL_LATITUDE \"cellLat\",CELL \"cell\",RXLEVSUB \"rxLevSub\",RXQUALSUB \"rxQualSub\",DISTANCE \"distance\",ANGLE \"angle\",(NCELL_RXLEV_1||','||NCELL_RXLEV_2||','||NCELL_RXLEV_3||','||NCELL_RXLEV_4||','||NCELL_RXLEV_5||','||NCELL_RXLEV_6) \"ncellRxLevSubs\",(NCELL_NAME_1||','||NCELL_NAME_2||','||NCELL_NAME_3||','||NCELL_NAME_4||','||NCELL_NAME_5||','||NCELL_NAME_6) \"ncells\",TO_CHAR(SAMPLE_TIME,'yyyy-MM-dd hh24:mi:ss') \"sampleTimeStr\",( (nvl(NCELL_RXLEV_1,0)+nvl(NCELL_RXLEV_2,0)+nvl(NCELL_RXLEV_3,0)+nvl(NCELL_RXLEV_4,0)+nvl(NCELL_RXLEV_5,0)+nvl(NCELL_RXLEV_6,0))/NCELL_COUNT) \"avgNcellLevSub\",(SELECT bearing from CELL WHERE label=CELL) \"bearing\",cell_indoor \"cellIndoor\",(NCELL_INDOOR_1||','||NCELL_INDOOR_2||','||NCELL_INDOOR_3||','||NCELL_INDOOR_4||','||NCELL_INDOOR_5||','||NCELL_INDOOR_6) \"ncellIndoors\",to_number(SUBSTR(CELL.LNGLATS,1,INSTR(CELL.LNGLATS,',',1,1)-1)) \"cellMapLng\",to_number(SUBSTR(CELL.LNGLATS,INSTR(CELL.LNGLATS,',',1,1)+1,INSTR(CELL.LNGLATS,';',1,1)-1-(INSTR(CELL.LNGLATS,',',1,1)+1)+1)) \"cellMapLat\",avg_distance \"avgDistance\",rownum as rn from RNO_GSM_DT_SAMPLE left join CELL on RNO_DT_DESCRIPTOR_ID in("+dtDescId+") AND RNO_GSM_DT_SAMPLE.CELL=CELL.LABEL ORDER BY SAMPLE_TIME ASC) where rn>="
//				+ startIndex
//				+ " and rn<" + endIndex;
		
		final String sql;
		
		if("google".equals(mapId))
		{
			sql="select mid2.*,CELL.LONGITUDE \"cellMapLng\",CELL.LATITUDE \"cellMapLat\","
					+" CELL.bearing as \"bearing\" from ( "
					+" select mid1.*  FROM  ("
					+" SELECT RNO_GSM_DT_SAMPLE.LONGITUDE \"lng\",RNO_GSM_DT_SAMPLE.LATITUDE \"lat\",CELL_LONGITUDE \"cellLng\",CELL_LATITUDE \"cellLat\","
					+" RNO_GSM_DT_SAMPLE.CELL \"cell\" ,avg_distance \"avgDistance\",RXLEVSUB \"rxLevSub\",RXQUALSUB \"rxQualSub\",DISTANCE \"distance\",ANGLE \"angle\",(NCELL_RXLEV_1||','||NCELL_RXLEV_2||','||NCELL_RXLEV_3||','||NCELL_RXLEV_4||','||NCELL_RXLEV_5||','||NCELL_RXLEV_6) \"ncellRxLevSubs\",(NCELL_NAME_1||','||NCELL_NAME_2||','||NCELL_NAME_3||','||NCELL_NAME_4||','||NCELL_NAME_5||','||NCELL_NAME_6) \"ncells\",TO_CHAR(SAMPLE_TIME,'yyyy-MM-dd hh24:mi:ss') \"sampleTimeStr\",( (nvl(NCELL_RXLEV_1,0)+nvl(NCELL_RXLEV_2,0)+nvl(NCELL_RXLEV_3,0)+nvl(NCELL_RXLEV_4,0)+nvl(NCELL_RXLEV_5,0)+nvl(NCELL_RXLEV_6,0))/NCELL_COUNT) \"avgNcellLevSub\","
					+" cell_indoor \"cellIndoor\",(NCELL_INDOOR_1||','||NCELL_INDOOR_2||','||NCELL_INDOOR_3||','||NCELL_INDOOR_4||','||NCELL_INDOOR_5||','||NCELL_INDOOR_6) \"ncellIndoors\",rownum as rn "
					+" from RNO_GSM_DT_SAMPLE where RNO_DT_DESCRIPTOR_ID in("+dtDescId+") ORDER BY SAMPLE_TIME ASC "
					+" )mid1  where rn>="+startIndex+" and rn<"+endIndex+" )mid2 "
					+" left join CELL on  MID2.\"cell\"=CELL.LABEL ";
			
		}
		else
		{
			/*sql="select mid2.*,to_number(SUBSTR(CELL.LNGLATS,1,INSTR(CELL.LNGLATS,',',1,1)-1)) \"cellMapLng\",to_number(SUBSTR(CELL.LNGLATS,INSTR(CELL.LNGLATS,',',1,1)+1,INSTR(CELL.LNGLATS,';',1,1)-1-(INSTR(CELL.LNGLATS,',',1,1)+1)+1)) \"cellMapLat\","
					+" CELL.bearing as \"bearing\" from ( "
					+" select mid1.*  FROM  ("
					+" SELECT to_number(SUBSTR(map_lnglat,1,INSTR(map_lnglat,',',1,1)-1)) \"lng\",to_number(SUBSTR(map_lnglat,INSTR(map_lnglat,',',1,1)+1)) \"lat\",CELL_LONGITUDE \"cellLng\",CELL_LATITUDE \"cellLat\","
					+" RNO_GSM_DT_SAMPLE.CELL \"cell\" ,avg_distance \"avgDistance\",RXLEVSUB \"rxLevSub\",RXQUALSUB \"rxQualSub\",DISTANCE \"distance\",ANGLE \"angle\",(NCELL_RXLEV_1||','||NCELL_RXLEV_2||','||NCELL_RXLEV_3||','||NCELL_RXLEV_4||','||NCELL_RXLEV_5||','||NCELL_RXLEV_6) \"ncellRxLevSubs\",(NCELL_NAME_1||','||NCELL_NAME_2||','||NCELL_NAME_3||','||NCELL_NAME_4||','||NCELL_NAME_5||','||NCELL_NAME_6) \"ncells\",TO_CHAR(SAMPLE_TIME,'yyyy-MM-dd hh24:mi:ss') \"sampleTimeStr\",( (nvl(NCELL_RXLEV_1,0)+nvl(NCELL_RXLEV_2,0)+nvl(NCELL_RXLEV_3,0)+nvl(NCELL_RXLEV_4,0)+nvl(NCELL_RXLEV_5,0)+nvl(NCELL_RXLEV_6,0))/NCELL_COUNT) \"avgNcellLevSub\","
					+" cell_indoor \"cellIndoor\",(NCELL_INDOOR_1||','||NCELL_INDOOR_2||','||NCELL_INDOOR_3||','||NCELL_INDOOR_4||','||NCELL_INDOOR_5||','||NCELL_INDOOR_6) \"ncellIndoors\",rownum as rn "
					+" from RNO_GSM_DT_SAMPLE where RNO_DT_DESCRIPTOR_ID in("+dtDescId+") ORDER BY SAMPLE_TIME ASC "
					+" )mid1  where rn>="+startIndex+" and rn<"+endIndex+" )mid2 "
					+" left join CELL on  MID2.\"cell\"=CELL.LABEL ";*/
			//Liang YJ 2014-3-25 15:39修改，为适应新的坐标格式"baidu":"....","google":"...."
			sql="select mid2.*,CELL.LONGITUDE AS \"cellMapLng\" ,CELL.LATITUDE AS \"cellMapLat\"," 
					//"to_number(SUBSTR(CELL.LNGLATS,INSTR(CELL.LNGLATS,':',1)+2,(INSTR(CELL.LNGLATS,',',1)-INSTR(CELL.LNGLATS,':',1)-2))) \"cellMapLng\",to_number(SUBSTR(CELL.LNGLATS,INSTR(CELL.LNGLATS,',',1)+1,(INSTR(CELL.LNGLATS,';',1)-INSTR(CELL.LNGLATS,',',1)-1))) \"cellMapLat\","
					+" CELL.bearing as \"bearing\" from ( "
					+" select mid1.*  FROM  ("
					+" SELECT to_number(SUBSTR(map_lnglat,1,INSTR(map_lnglat,',',1,1)-1)) \"lng\",to_number(SUBSTR(map_lnglat,INSTR(map_lnglat,',',1,1)+1)) \"lat\",CELL_LONGITUDE \"cellLng\",CELL_LATITUDE \"cellLat\","
					+" RNO_GSM_DT_SAMPLE.CELL \"cell\" ,avg_distance \"avgDistance\",RXLEVSUB \"rxLevSub\",RXQUALSUB \"rxQualSub\",DISTANCE \"distance\",ANGLE \"angle\",(NCELL_RXLEV_1||','||NCELL_RXLEV_2||','||NCELL_RXLEV_3||','||NCELL_RXLEV_4||','||NCELL_RXLEV_5||','||NCELL_RXLEV_6) \"ncellRxLevSubs\",(NCELL_NAME_1||','||NCELL_NAME_2||','||NCELL_NAME_3||','||NCELL_NAME_4||','||NCELL_NAME_5||','||NCELL_NAME_6) \"ncells\",TO_CHAR(SAMPLE_TIME,'yyyy-MM-dd hh24:mi:ss') \"sampleTimeStr\",( (nvl(NCELL_RXLEV_1,0)+nvl(NCELL_RXLEV_2,0)+nvl(NCELL_RXLEV_3,0)+nvl(NCELL_RXLEV_4,0)+nvl(NCELL_RXLEV_5,0)+nvl(NCELL_RXLEV_6,0))/NCELL_COUNT) \"avgNcellLevSub\","
					+" cell_indoor \"cellIndoor\",(NCELL_INDOOR_1||','||NCELL_INDOOR_2||','||NCELL_INDOOR_3||','||NCELL_INDOOR_4||','||NCELL_INDOOR_5||','||NCELL_INDOOR_6) \"ncellIndoors\",rownum as rn "
					+" from RNO_GSM_DT_SAMPLE where RNO_DT_DESCRIPTOR_ID in("+dtDescId+") ORDER BY SAMPLE_TIME ASC "
					+" )mid1  where rn>="+startIndex+" and rn<"+endIndex+" )mid2 "
					+" left join CELL on  MID2.\"cell\"=CELL.LABEL ";
		}

		
		log.info("获取dt数据的sql："+sql);
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

			public List<Map<String, Object>> doInHibernate(
					Session arg0) throws HibernateException,
					SQLException {

				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List re= query.list();
				log.info("获取到的dt的数量："+re==null?0:re.size());
				return re;
			}
		});
	}
	/**
	 * 通过区域ID获得某一DT描述ID下的采样点map数据集合
	 * @param dtDescId
	 * @return
	 * @author chao.xj
	 * @date 2013-11-26上午10:48:45
	 */
	public List<Map<String, Object>> getRnoGsmDtSampleListMapsByArea(final long dtDescId){
		
	return	hibernateTemplate.executeFind(new HibernateCallback<List<Map<String, Object>>>(){
			
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				String sql="SELECT to_number(SUBSTR(map_lnglat,1,INSTR(map_lnglat,',',1,1)-1)) \"lng\",to_number(SUBSTR(map_lnglat,INSTR(map_lnglat,',',1,1)+1)) \"lat\",CELL_LONGITUDE \"cellLng\",CELL_LATITUDE \"cellLat\",CELL \"cell\",RXLEVSUB \"rxLevSub\",RXQUALSUB \"rxQualSub\",DISTANCE \"distance\",ANGLE \"angle\",(NCELL_RXLEV_1||','||NCELL_RXLEV_2||','||NCELL_RXLEV_3||','||NCELL_RXLEV_4||','||NCELL_RXLEV_5||','||NCELL_RXLEV_6) \"ncellRxLevSubs\",(NCELL_NAME_1||','||NCELL_NAME_2||','||NCELL_NAME_3||','||NCELL_NAME_4||','||NCELL_NAME_5||','||NCELL_NAME_6) \"ncells\",TO_CHAR(SAMPLE_TIME,'yyyy-MM-dd hh24:mi:ss') \"sampleTimeStr\",( (nvl(NCELL_RXLEV_1,0)+nvl(NCELL_RXLEV_2,0)+nvl(NCELL_RXLEV_3,0)+nvl(NCELL_RXLEV_4,0)+nvl(NCELL_RXLEV_5,0)+nvl(NCELL_RXLEV_6,0))/NCELL_COUNT) \"avgNcellLevSub\",(SELECT bearing from CELL WHERE label=CELL) \"bearing\",cell_indoor \"cellIndoor\",(NCELL_INDOOR_1||','||NCELL_INDOOR_2||','||NCELL_INDOOR_3||','||NCELL_INDOOR_4||','||NCELL_INDOOR_5||','||NCELL_INDOOR_6) \"ncellIndoors\" from RNO_GSM_DT_SAMPLE WHERE RNO_DT_DESCRIPTOR_ID=?";
				SQLQuery query=arg0.createSQLQuery(sql);
				query.setLong(0, dtDescId);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				query.list();
				return query.list();
			}
		});
	}
	/**
	 * 查询符合条件的dt描述项的数量
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-27 下午3:12:48
	 */
	public int getDtDescriptorTotalCnt(Map<String, Object> attachParams){
		//查询条件NET_MODE{GSM.TD},TYPE{DT},AREA_ID,NAME,TEST_DATE_BEGIN,TEST_DATE_END
		log.info("进入dao方法：getDtDescriptorTotalCnt.attachParams="
				+ attachParams);
		if (attachParams == null || attachParams.isEmpty()) {
			log.warn("传入的查询条件参数为空！");
			return 0;
		}
		String where = buildWhereClauseOfDtDescriptor("a", attachParams);
		log.info("查询满足条件的dt desc总数量的条件：where : " + where);
		if (where == null) {
			log.warn("传入的查询条件无效！");
			return 0;
		}
		final String sql = "select count(DT_DESC_ID) from RNO_DT_DESCRIPTOR a WHERE "
				+ where;
		log.info("查询满足条件的DT desc的查询语句：" + sql);

		int cnt = hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {

				SQLQuery query = arg0.createSQLQuery(sql);

				List<Object> list = query.list();
				int cnt = 0;
				if (list != null && list.size() > 0) {
					cnt = Integer.valueOf(list.get(0).toString());
				}

				return cnt;
			}
		});
		log.info("查询到满足条件的cell struct desc的数量：" + cnt);
		return cnt;
	}

	/**
	 * 分页查询满足条件的dt描述项
	 * @param page
	 * @param attachParams
	 * @return
	 * @author brightming
	 * 2013-11-27 下午3:13:07
	 */
	public List<Map<String, Object>> queryDtDescriptorByPage(Page page,
			Map<String, Object> attachParams){
		log.info("进入dao方法：queryDtDescriptorByPage。page=" + page
				+ ",attachParams=" + attachParams);
		if (page == null) {
			log.warn("传入的分页参数为空！");
			return Collections.EMPTY_LIST;
		}
		if (attachParams == null || attachParams.isEmpty()) {
			log.warn("传入的查询条件参数为空！");
			return Collections.EMPTY_LIST;
		}

		final int startIndex = (page.getCurrentPage() - 1) * page.getPageSize()
				+ 1;
		final int endIndex = startIndex + page.getPageSize();
		String where = buildWhereClauseOfDtDescriptor("a", attachParams);
		log.info("分页查询 dt desc的查询条件：where : " + where);
		if (where == null) {
			log.warn("传入的查询条件无效！");
			return Collections.EMPTY_LIST;
		}
		final String sql = "select * from (select a.*,TO_CHAR(a.TEST_DATE,'YYYY-MM-DD HH24:mi:ss') AS timeStr,b.name as areaName ,rownum as rn from RNO_DT_DESCRIPTOR a ,SYS_AREA b WHERE "
				+ where
				+ " and a.AREA_ID=b.AREA_ID) where rn>="
				+ startIndex
				+ " and rn<" + endIndex;
		log.info("分页查询DT desc 的查询语句：" + sql);

		List list= hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String, Object>>>() {

					public List<Map<String, Object>> doInHibernate(
							Session arg0) throws HibernateException,
							SQLException {

						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						return query.list();
					}
				});
		log.info("分页查询dt descriptor得到的结果："+list);
		return list;
	}
	
	private String buildWhereClauseOfDtDescriptor(String alias,Map<String, Object> attachParams){
		if(attachParams==null || attachParams.isEmpty()){
			return null;
		}
		String where = "";
		String v="";
		for(String k:attachParams.keySet()){
			v=attachParams.get(k)+"";
			v=v.trim();
			if("".equals(v)){
				continue;
			}
			if("NET_MODE".equalsIgnoreCase(k) ){
				where+=" and "+alias+".NET_MODE='"+v+"' ";
			}else if("type".equalsIgnoreCase(k)){
				where+=" and "+alias+".TYPE='"+v+"' ";
			}else if("area_id".equalsIgnoreCase(k)){
				where+=" and "+alias+".AREA_ID="+v;
			}else if("name".equalsIgnoreCase(k)){
				where+=" and "+alias+".NAME like '%"+v+"%'";
			}else if("TEST_DATE_BEGIN".equalsIgnoreCase(k)){
				where += " and " + alias + ".TEST_DATE>=TO_DATE('"
						+ v + "','YYYY-MM-DD HH24:mi:ss') ";
			}else if("TEST_DATE_END".equalsIgnoreCase(k)){
				where += " and " + alias + ".TEST_DATE<=TO_DATE('"
						+ v + "','YYYY-MM-DD HH24:mi:ss') ";
			}
		}
		if ("".equals(where)) {
			log.warn("查询条件无效！");
			return null;
		} else {
			where = where.substring(4);
		}
		return where;
	}
}
