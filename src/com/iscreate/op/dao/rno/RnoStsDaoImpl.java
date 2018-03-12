package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
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
import com.iscreate.op.action.rno.StsCondition;
import com.iscreate.op.pojo.rno.RnoSts;

public class RnoStsDaoImpl implements RnoStsDao {

	private static Log log = LogFactory.getLog(RnoStsDaoImpl.class);

	// ---注入----//
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	/**
	 * 
	 * @description: 城市网络质量指标分页查询话务数据
	 * @author：yuan.yw
	 * @param page
	 * @param queryCondition
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Oct 10, 2013 2:22:52 PM
	 */
	public List<Map<String,Object>> queryStsByCityQuaByPage(final Page page, final StsCondition queryCondition) {
		log.info("进入方法：queryStsByCityQuaByPage(final Page page, final StsCondition queryCondition).page=" + page + ",queryCondition=" + queryCondition);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String,Object>>>() {
					public List<Map<String,Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = getQueryStsByCityQuaSql("",queryCondition,false);
						SQLQuery query = arg0.createSQLQuery(sql);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						query.setLong(0, start);
						query.setLong(1, end);
						//System.out.println(start+":"+end);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> stsList = query.list();
						log.info("获取到结果集记录数量："
								+ (stsList == null ? 0 : stsList.size()));
						return stsList;
					}
				});
	}
	/**
	 * 
	 * @description: 获取城市网络质量指标查询话务数据sql
	 * @author：yuan.yw
	 * @param field
	 * @param queryCondition
	 * @param sqlForFloat sql为了查询数量
	 * @return     
	 * @return String     
	 * @date：Oct 10, 2013 3:15:48 PM
	 */
	public String getQueryStsByCityQuaSql(String field,StsCondition queryCondition,boolean sqlForCount){
		log.info("进入方法getQueryStsByCityQuaSql  field="+field+",queryCondition="+queryCondition+",sqlForCount="+sqlForCount);
		StringBuffer sbf = new StringBuffer(" 1=1 ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(queryCondition.getCityId()!=null){
			sbf.append(" and rcq.AREA_ID in ( select AREA_ID from SYS_AREA connect by prior AREA_ID=PARENT_ID start with AREA_ID="+queryCondition.getCityId()+")");
		}
		if(queryCondition.getGrade()!=null&&!"".equals(queryCondition.getGrade())){
			sbf.append(" and rcq.GRADE='"+queryCondition.getGrade()+"'");		
		}
		if(queryCondition.getBeginTime()!=null && !queryCondition.getBeginTime().equals("") && queryCondition.getLatestAllowedTime()!=null && !queryCondition.getLatestAllowedTime().equals("")){
			//System.out.println("来了");
			sbf.append(" and to_char(STATIC_TIME,'yyyy-MM-dd') BETWEEN '"+sdf.format(queryCondition.getBeginTime())+"' AND '"+sdf.format(queryCondition.getLatestAllowedTime())+"'");
			//to_char(STATIC_TIME,'yyyy-MM-dd') BETWEEN '2013-07-28' AND '2013-07-30'
		}else {
			//System.out.println("又来了");
			if(queryCondition.getBeginTime()!=null && !queryCondition.getBeginTime().equals("")){
				//sbf.append(" and to_char(rcq.STATIC_TIME,'yyyy-mm-dd')>='"+sdf.format(queryCondition.getBeginTime())+"'");
				sbf.append(" and rcq.STATIC_TIME>=to_date('"+sdf.format(queryCondition.getBeginTime())+"','yyyy-mm-dd')");
			}
			if(queryCondition.getLatestAllowedTime()!=null && !queryCondition.getLatestAllowedTime().equals("")){
				//sbf.append(" and to_char(rcq.STATIC_TIME,'yyyy-mm-dd')<='"+sdf.format(queryCondition.getBeginTime())+"'");
				sbf.append(" and rcq.STATIC_TIME<=to_date('"+sdf.format(queryCondition.getLatestAllowedTime())+"','yyyy-mm-dd')");
			}
		}
		
		String sql="";
		if(sqlForCount){//获取数量sql
				//sql = "select count(rcq.CITYQUL_ID) from RNO_CITY_QUALITY rcq  where "
			sql = "select count(STATIC_TIME) from (select TO_CHAR(rcq.STATIC_TIME,'yyyy-MM-dd') STATIC_TIME,rcq.GRADE,area.NAME,rcq.SCORE, rcd.INDEX_CLASS, rcd.INDEX_NAME, rcd.INDEX_VALUE,rownum rn from AREA area,RNO_CITY_QUALITY rcq left join RNO_CITYQUL_DETAIL rcd on rcd.CITYQUL_ID=rcq.CITYQUL_ID where rcq.AREA_ID=area.ID and "
				+ sbf.toString() + ")";
			
		}else{//获取list sql
			sql = "select * from (select TO_CHAR(rcq.STATIC_TIME,'yyyy-MM-dd') STATIC_TIME,rcq.GRADE,area.NAME,rcq.SCORE, rcd.INDEX_CLASS, rcd.INDEX_NAME, rcd.INDEX_VALUE,rownum rn from AREA area,RNO_CITY_QUALITY rcq left join RNO_CITYQUL_DETAIL rcd on rcd.CITYQUL_ID=rcq.CITYQUL_ID where rcq.AREA_ID=area.ID and "
			+ sbf.toString() + " ) where  rn>=? and rn<=?";
		}
		log.info("生成sql:"+sql);
		return sql;
	}
	/**
	 * 
	 * @description: 城市网络质量指标分页查询话务数据数量
	 * @author：yuan.yw
	 * @param queryCondition
	 * @return     
	 * @return int     
	 * @date：Oct 10, 2013 3:50:36 PM
	 */
	public int getTotalQueryStsByCityQua(
			final StsCondition queryCondition) {
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = getQueryStsByCityQuaSql("",queryCondition,true);
				SQLQuery query = arg0.createSQLQuery(sql);
				List<Object> list = query.list();
				int cnt = 0;
				if (list != null && list.size() > 0) {
					cnt = Integer.valueOf(list.get(0).toString());
				}
				return cnt;
			}
		});
	}
	
	/**
	 * 
	 * @description: 小区语音业务指标或小区数据业务指标分页获取数据
	 * @author：yuan.yw
	 * @param page
	 * @param queryCondition
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Oct 10, 2013 2:22:52 PM
	 */
	public List<Map<String,Object>> queryStsByCellVideoOrDataByPage(final Page page, final StsCondition queryCondition,final boolean isAudio) {
		log.info("进入方法：queryStsByCellVideoOrDataByPage(final Page page, final StsCondition queryCondition).page=" + page + ",queryCondition=" + queryCondition);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String,Object>>>() {
					public List<Map<String,Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						List<Map<String,Object>> stsList = null;
						//单天求和
						if("sum".equals(queryCondition.getStsType()))
						{
							String field = "avg(rsd.AREA_ID) AREA_ID,rs.CELL CELL,TO_CHAR(rsd.STS_DATE,'yyyy-MM-dd') STS_DATE,max(rb.ENGNAME) ENGNAME,max(rs.CELL_CHINESE_NAME) CELL_CHINESE_NAME,sum(rs.DECLARE_CHANNEL_NUMBER) DECLARE_CHANNEL_NUMBER,sum(rs.AVAILABLE_CHANNEL_NUMBER) AVAILABLE_CHANNEL_NUMBER,sum(rs.CARRIER_NUMBER) CARRIER_NUMBER,sum(rs.TRAFFIC) TRAFFIC,sum(rs.DROP_CALL_NUM_TOGETHER) DROP_CALL_NUM_TOGETHER,sum(rs.DATA_TRAFFIC) DATA_TRAFFIC,count(*) CNT ";
							String sql = getQueryStsByCellVideoOrDataSql(field,queryCondition,queryCondition.getStsType(),isAudio);
							SQLQuery query = arg0.createSQLQuery(sql);
							int start = (page.getPageSize()
									* (page.getCurrentPage() - 1) + 1);
							int end = (page.getPageSize() * page.getCurrentPage());
							query.setLong(0, start);
							query.setLong(1, end);
							//System.out.println(sql+":"+start+":"+end+query);
							query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
							stsList = query.list();
						}
						//单天平均
						else if("avg".equals(queryCondition.getStsType()))
						{
							String field = "avg(rsd.AREA_ID) AREA_ID,rs.CELL CELL,TO_CHAR(rsd.STS_DATE,'yyyy-MM-dd') STS_DATE,max(rb.ENGNAME) ENGNAME,max(rs.CELL_CHINESE_NAME) CELL_CHINESE_NAME,avg(rs.DECLARE_CHANNEL_NUMBER) DECLARE_CHANNEL_NUMBER,avg(rs.AVAILABLE_CHANNEL_NUMBER) AVAILABLE_CHANNEL_NUMBER,avg(rs.CARRIER_NUMBER) CARRIER_NUMBER,avg(rs.TRAFFIC) TRAFFIC,avg(rs.DROP_CALL_NUM_TOGETHER) DROP_CALL_NUM_TOGETHER,avg(rs.DATA_TRAFFIC) DATA_TRAFFIC,count(*) CNT ";
							String sql = getQueryStsByCellVideoOrDataSql(field,queryCondition,queryCondition.getStsType(),isAudio);
							SQLQuery query = arg0.createSQLQuery(sql);
							int start = (page.getPageSize()
									* (page.getCurrentPage() - 1) + 1);
							int end = (page.getPageSize() * page.getCurrentPage());
							query.setLong(0, start);
							query.setLong(1, end);
							//System.out.println(sql+":"+start+":"+end+query);
							query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
							stsList = query.list();
						}
						//不汇总
						else
						{
							String field = "rsd.AREA_ID,rs.STS_ID,rsd.STS_DESC_ID,TO_CHAR(rsd.STS_DATE,'yyyy-MM-dd') STS_DATE,rsd.STS_PERIOD,rb.ENGNAME,rs.CELL,rs.CELL_CHINESE_NAME,rs.TCH_INTACT_RATE,rs.DECLARE_CHANNEL_NUMBER,rs.AVAILABLE_CHANNEL_NUMBER,rs.CARRIER_NUMBER,RESOURCE_USE_RATE,TRAFFIC,rs.TRAFFIC_PER_LINE,rs.ACCESS_OK_RATE,RADIO_ACCESS,rs.DROP_CALL_NUM_TOGETHER,rs.RADIO_DROP_RATE_NO_HV,ICM,rs.HANDOUT_SUC_RATE,rs.HANDIN_SUC_RATE,rs.HANDOVER_SUC_RATE,rs.PS_RADIO_USE_RATE,rs.PDCH_CARRYING_EFFICIENCY,rs.DATA_TRAFFIC,rs.DOWNLINK_BPDCH_REUSE,rs.DOWNLINK_EPDCH_REUSE,rs.DOWNLINK_TBF_CONG_RATE";
							String sql = getQueryStsByCellVideoOrDataSql(field,queryCondition,"sts",isAudio);
							
							SQLQuery query = arg0.createSQLQuery(sql);
							int start = (page.getPageSize()
									* (page.getCurrentPage() - 1) + 1);
							int end = (page.getPageSize() * page.getCurrentPage());
							query.setLong(0, start);
							query.setLong(1, end);
							//System.out.println(sql+":"+start+":"+end+query);
							query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
							stsList = query.list();
							log.info("获取到结果集记录数量："
									+ (stsList == null ? 0 : stsList.size()));
							/*for (int i = 0; i < stsList.size(); i++) {
								Map map=stsList.get(i);
								System.out.println(map.get("STS_PERIOD")+":"+map.get("CELL"));
							}*/
						}
						return stsList;
					}
				});
	}
	/**
	 * 
	 * @description: 获取小区语音业务指标或小区数据业务指标获取数据sql
	 * @author：yuan.yw
	 * @param field
	 * @param queryCondition
	 * @pramm sqlForWhat  sql查询目的（数量 count  话务数据 sts  小区数据 cell）
	 * @return     
	 * @return String     
	 * @date：Oct 10, 2013 3:15:48 PM
	 */
	public String getQueryStsByCellVideoOrDataSql(String field,StsCondition queryCondition,String sqlForWhat,boolean isAudio){
		String stsTableSql = "";//话筒数据table sql
		StringBuffer stsWhereSql = new StringBuffer();// 话筒数据 where sql
		StringBuffer stsDescWhereSql = new StringBuffer(" 1=1 ");//话筒数据描述 where sql
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String specType=isAudio?"CELLAUDIOINDEX":"CELLDATAINDEX";
		/*
		if(queryCondition.getStsDate()!=null && !queryCondition.getStsDate().equals("")){
			stsDescWhereSql.append(" and to_char(rsd.STS_DATE,'yyyy-mm-dd')='"+sdf.format(queryCondition.getStsDate())+"'");
			//stsDescWhereSql.append(" and to_char(rsd.STS_DATE,'yyyy-mm-dd')='"+sdf.format(queryCondition.getStsDate())+"'");
		}*/
		if(queryCondition.getBeginTime()!=null && !queryCondition.getBeginTime().equals("") && queryCondition.getLatestAllowedTime()!=null && !queryCondition.getLatestAllowedTime().equals("")){
			//System.out.println("来了");
			stsDescWhereSql.append(" and to_char(STS_DATE,'yyyy-MM-dd') BETWEEN '"+sdf.format(queryCondition.getBeginTime())+"' AND '"+sdf.format(queryCondition.getLatestAllowedTime())+"'");
			//to_char(STATIC_TIME,'yyyy-MM-dd') BETWEEN '2013-07-28' AND '2013-07-30'
		}else {
			//System.out.println("又来了");
			if(queryCondition.getBeginTime()!=null && !queryCondition.getBeginTime().equals("")){
				//sbf.append(" and to_char(rcq.STATIC_TIME,'yyyy-mm-dd')>='"+sdf.format(queryCondition.getBeginTime())+"'");
				stsDescWhereSql.append(" and rsd.STS_DATE>=to_date('"+sdf.format(queryCondition.getBeginTime())+"','yyyy-mm-dd')");
			}
			if(queryCondition.getLatestAllowedTime()!=null && !queryCondition.getLatestAllowedTime().equals("")){
				//sbf.append(" and to_char(rcq.STATIC_TIME,'yyyy-mm-dd')<='"+sdf.format(queryCondition.getBeginTime())+"'");
				stsDescWhereSql.append(" and rsd.STS_DATE<=to_date('"+sdf.format(queryCondition.getLatestAllowedTime())+"','yyyy-mm-dd')");
			}
		}
		
		if(queryCondition.getStsPeriod()!=null&&!"".equals(queryCondition.getStsPeriod())){
			stsDescWhereSql.append(" and rsd.STS_PERIOD='"+queryCondition.getStsPeriod()+"'");
		}
		if(queryCondition.getAreaId()!=null && !queryCondition.getAreaId().equals("")){
			stsDescWhereSql.append(" and rsd.AREA_ID in ( select AREA_ID from SYS_AREA connect by prior AREA_ID=PARENT_ID start with AREA_ID="+queryCondition.getAreaId()+")");
			//stsDescWhereSql.append(" and rsd.AREA_ID in ( queryCondition.getAreaId());
		}
		if(queryCondition.getEngName()!=null && !queryCondition.getEngName().equals("")){
			//System.out.println("queryCondition.getEngName():"+queryCondition.getEngName());
			stsWhereSql.append(" and rs.BSC_ID in ( select BSC_ID from RNO_BSC where instr(ENGNAME,'"+queryCondition.getEngName()+"')>0)");
		}
		if(queryCondition.getCell()!=null && !queryCondition.getCell().equals("")){
			stsWhereSql.append(" and instr(rs.CELL,'"+queryCondition.getCell()+"')>0");
		}
		if(queryCondition.getCellChineseName()!=null && !queryCondition.getCellChineseName().equals("")){
			stsWhereSql.append(" and instr(rs.CELL_CHINESE_NAME,'"+queryCondition.getCellChineseName()+"')>0");
		}
		
		if(stsWhereSql.toString()!=null && !"".equals(stsWhereSql)){
			stsTableSql=" inner join  RNO_STS  rs on rsd.sts_desc_id=rs.DESCRIPTOR_ID";
			//stsWhereSql.append(" and rs.DESCRIPTOR_ID=rsd.STS_DESC_ID ");
		}else{
			stsTableSql="left join RNO_STS  rs on rs.DESCRIPTOR_ID=rsd.DESCRIPTOR_ID ";	
		}
		String sql = "";
		if("count".equals(sqlForWhat)){//获取数量sql
			if(stsWhereSql.toString()==null || "".equals(stsWhereSql)){
				//单天求、单天平均、单天最大值、单天最小值
				if("sum".equals(queryCondition.getStsType()) || "avg".equals(queryCondition.getStsType()) || "max".equals(queryCondition.getStsType()) || "min".equals(queryCondition.getStsType()))
				{
					sql = "select count(*) from(select rs.CELL CELL from RNO_STS_DESCRIPTOR rsd " + stsTableSql+"  where "
							+ stsDescWhereSql.toString() + " and rsd.SPEC_TYPE='"+specType+"'  group by rs.CELL, TO_CHAR(rsd.STS_DATE,'yyyy-MM-dd'))";
				}else
				{
					sql = "select count(rsd.STS_DESC_ID) from RNO_STS_DESCRIPTOR rsd   where "
							+ stsDescWhereSql.toString()+ " and rsd.SPEC_TYPE='"+specType+"'";
				}
			}else{
				if("sum".equals(queryCondition.getStsType()) || "avg".equals(queryCondition.getStsType()) || "max".equals(queryCondition.getStsType()) || "min".equals(queryCondition.getStsType()))
				{
					sql = "select count(*) from(select rs.CELL CELL from RNO_STS_DESCRIPTOR rsd " + stsTableSql+"  where "
							+ stsDescWhereSql.toString() + stsWhereSql.toString() + " and rsd.SPEC_TYPE='"+specType+"'  group by rs.CELL, TO_CHAR(rsd.STS_DATE,'yyyy-MM-dd'))";
				}else
				{
					
					sql = "select count(rsd.STS_DESC_ID) from RNO_STS_DESCRIPTOR rsd "+stsTableSql+"  where "
							+ stsDescWhereSql.toString()+stsWhereSql.toString() + " and rsd.SPEC_TYPE='"+specType+"'";
				}
			}
			
			
			
		}else if("sts".equals(sqlForWhat)){//获取list sql
			sql = "select * from (select "
			+ field + ",rownum rn from RNO_STS_DESCRIPTOR rsd "+stsTableSql+" left join RNO_BSC rb on rb.BSC_ID=rs.BSC_ID where  "
			+ stsDescWhereSql.toString()+stsWhereSql.toString() + " and rsd.SPEC_TYPE='"+specType+"' ) where  rn>=? and rn<=?";
		}else if("cell".equals(sqlForWhat)){
			sql = "select rs.cell from RNO_STS_DESCRIPTOR rsd "+stsTableSql+"  where "
			+ stsDescWhereSql.toString()+stsWhereSql.toString() + " and rsd.SPEC_TYPE='"+specType+"'  group by rs.cell";
		}else if("stsAll".equals(sqlForWhat)){//获取全部查询记录
			sql = "select "
				+ field + " from RNO_STS_DESCRIPTOR rsd "+stsTableSql+" left join RNO_BSC rb on rb.BSC_ID=rs.BSC_ID where  "
				+ stsDescWhereSql.toString()+stsWhereSql.toString() +" and rsd.SPEC_TYPE='"+specType+"'";
		}else if("sum".equals(sqlForWhat) || "avg".equals(sqlForWhat) || "max".equals(sqlForWhat) || "min".equals(sqlForWhat)){
			sql = "select * from (select tb.*, rownum rn from (select "
					+ field + " from RNO_STS_DESCRIPTOR rsd "+stsTableSql+" left join RNO_BSC rb on rb.BSC_ID=rs.BSC_ID where "
			+ stsDescWhereSql.toString()+stsWhereSql.toString() + " and rsd.SPEC_TYPE='"+specType+"'  group by rs.CELL, TO_CHAR(rsd.STS_DATE,'yyyy-MM-dd')) tb ) where rn>=? and rn<=?";
		}else if("statisticExportByTime".equals(sqlForWhat))
		{
			sql = "select " + field + " from RNO_STS_DESCRIPTOR rsd "+stsTableSql+" left join RNO_BSC rb on rb.BSC_ID=rs.BSC_ID where "
					+ stsDescWhereSql.toString()+stsWhereSql.toString() + " and rsd.SPEC_TYPE='"+specType+"'  group by rs.CELL, TO_CHAR(rsd.STS_DATE,'yyyy-MM-dd')";
		}
		log.info("sql: "+sql);
		return sql;
	}
	/**
	 * 
	 * @description: 小区语音业务指标或小区数据业务指标条件获取数据数量
	 * @author：yuan.yw
	 * @param queryCondition
	 * @return     
	 * @return int     
	 * @date：Oct 10, 2013 3:50:36 PM
	 */
	public int getTotalQueryStsByCellVideoOrData(
			final StsCondition queryCondition,final boolean isAudio) {
		return hibernateTemplate.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String sql = getQueryStsByCellVideoOrDataSql("",queryCondition,"count",isAudio);
				SQLQuery query = arg0.createSQLQuery(sql);
				List<Object> list = query.list();
				int cnt = 0;
				if (list != null && list.size() > 0) {
					cnt = Integer.valueOf(list.get(0).toString());
				}
				log.info("cnt:"+cnt);
				return cnt;
			}
		});
	}
	/**
	 * 
	 * @description: 小区语音业务指标或小区数据业务指标条件获取小区信息（加载分析列表使用）
	 * @author：yuan.yw
	 * @param queryCondition
	 * @return     
	 * @return int     
	 * @date：Oct 10, 2013 3:50:36 PM
	 */
	public List<Map<String,Object>> getCellQueryByCellVideoOrData(
			final StsCondition queryCondition,final boolean isAudio){
		log.info("进入方法：getCellQueryByCellVideoOrData(final StsCondition queryCondition). queryCondition=" + queryCondition);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String,Object>>>() {
					public List<Map<String,Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = getQueryStsByCellVideoOrDataSql("",queryCondition,"cell",isAudio);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> stsList = query.list();
						log.info("获取到结果集记录数量："+ (stsList == null ? 0 : stsList.size()));
						return stsList;
					}
				});
	}
	
	/**
	 * 在指定区域插入新的RnoSts对象
	 */
	public Long insertRnoSts(RnoSts sts) {
		log.debug("进入类RnoStsDaoImpl方法：insertRnoSts。sts="
				+ sts);
		Long id = (Long) hibernateTemplate.save(sts);
		log.debug("退出类RnoStsDaoImpl方法：insertRnoSts。得到插入的id=" + id == null ? "空"
				: id.longValue());
		return id;
	}
	
	/**
	 * 根据描述ID获取话统数据
	* @author ou.jh
	* @date Oct 10, 2013 4:00:44 PM
	* @Description: TODO 
	* @param @param descriptorId
	* @param @return        
	* @throws
	 */
	public List<RnoSts> getRnoStsBydescriptorId(final long descriptorId){
		return hibernateTemplate.executeFind(new HibernateCallback<List<RnoSts>>(){
			public List<RnoSts> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				
				SQLQuery query=arg0.createSQLQuery("select rs.* from rno_sts rs where rs.descriptor_id = ?");
				query.setLong(0, descriptorId);
				//System.out.println(query.getQueryString());
				List<RnoSts> bscs=query.addEntity(RnoSts.class).list();
				return bscs;
			}
		});
	}
	
	public List<RnoSts> getAllRnoSts() {
		log.debug("进入类RnoStsDaoImpl方法：getAllRnoSts。");
		List<RnoSts> rnoStss = hibernateTemplate.loadAll(RnoSts.class);
		log.debug("退出类RnoStsDaoImpl方法：getAllRnoSts。获取记录数量："
				+ (rnoStss == null ? 0 : rnoStss.size()));
		return rnoStss;
	}
	/**
	 * 
	 * @description: 小区语音业务指标或小区数据业务指标条件获取话务数据
	 * @author：yuan.yw
	 * @param queryCondition
	 * @return     
	 * @return List<Map<String,Object>>     
	 * @date：Oct 29, 2013 10:28:05 AM
	 */
	public List<Map<String,Object>> queryStsByVideoOrDataCondition(final StsCondition queryCondition,final boolean isAudio){
		log.info("进入方法：queryStsByVideoOrDataCondition(final StsCondition queryCondition),queryCondition=" + queryCondition);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String,Object>>>() {
					public List<Map<String,Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String field = "rsd.AREA_ID,rs.STS_ID,rsd.STS_DESC_ID,TO_CHAR(rsd.STS_DATE,'yyyy-MM-dd') STS_DATE,rsd.STS_PERIOD,rb.ENGNAME,rs.CELL,rs.CELL_CHINESE_NAME,rs.TCH_INTACT_RATE,rs.DECLARE_CHANNEL_NUMBER,rs.AVAILABLE_CHANNEL_NUMBER,rs.CARRIER_NUMBER,RESOURCE_USE_RATE,TRAFFIC,rs.TRAFFIC_PER_LINE,rs.ACCESS_OK_RATE,RADIO_ACCESS,rs.DROP_CALL_NUM_TOGETHER,rs.RADIO_DROP_RATE_NO_HV,ICM,rs.HANDOUT_SUC_RATE,rs.HANDIN_SUC_RATE,rs.HANDOVER_SUC_RATE,rs.PS_RADIO_USE_RATE,rs.PDCH_CARRYING_EFFICIENCY,rs.DATA_TRAFFIC,rs.DOWNLINK_BPDCH_REUSE,rs.DOWNLINK_EPDCH_REUSE,rs.DOWNLINK_TBF_CONG_RATE";
						String sql = getQueryStsByCellVideoOrDataSql(field,queryCondition,"stsAll",isAudio);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> stsList = query.list();
						log.info("获取到结果集记录数量："
								+ (stsList == null ? 0 : stsList.size()));
						return stsList;
					}
				});
	}

/**
 * 
 * @author Liang YJ
 * 2014-1-20 上午11:06:21
 * @param field
 * @param queryCondition
 * @param isAudio
 * @return List<Map<String,Object>> 
 * @description: 小区语音业务指标或小区数据业务指标条件获取话务数据
 */
public List<Map<String,Object>> queryStsByVideoOrDataCondition(String field, final StsCondition queryCondition,final boolean isAudio)
{
	final String temp = field;
	log.info("进入方法：queryStsByVideoOrDataCondition(final StsCondition queryCondition),queryCondition=" + queryCondition);
	return hibernateTemplate
			.executeFind(new HibernateCallback<List<Map<String,Object>>>() {
				public List<Map<String,Object>> doInHibernate(Session arg0)
						throws HibernateException, SQLException {
					String sql = "";
					if("sum".equals(queryCondition.getStsType()) || "avg".equals(queryCondition.getStsType()) || "max".equals(queryCondition.getStsType()) || "min".equals(queryCondition.getStsType()))
					{
						sql = getQueryStsByCellVideoOrDataSql(temp,queryCondition,"statisticExportByTime",isAudio);
					}else
					{
						String field1 = "rsd.AREA_ID,rs.STS_ID,rsd.STS_DESC_ID,TO_CHAR(rsd.STS_DATE,'yyyy-MM-dd') STS_DATE,rsd.STS_PERIOD,rb.ENGNAME,rs.CELL,rs.CELL_CHINESE_NAME,rs.TCH_INTACT_RATE,rs.DECLARE_CHANNEL_NUMBER,rs.AVAILABLE_CHANNEL_NUMBER,rs.CARRIER_NUMBER,RESOURCE_USE_RATE,TRAFFIC,rs.TRAFFIC_PER_LINE,rs.ACCESS_OK_RATE,RADIO_ACCESS,rs.DROP_CALL_NUM_TOGETHER,rs.RADIO_DROP_RATE_NO_HV,ICM,rs.HANDOUT_SUC_RATE,rs.HANDIN_SUC_RATE,rs.HANDOVER_SUC_RATE,rs.PS_RADIO_USE_RATE,rs.PDCH_CARRYING_EFFICIENCY,rs.DATA_TRAFFIC,rs.DOWNLINK_BPDCH_REUSE,rs.DOWNLINK_EPDCH_REUSE,rs.DOWNLINK_TBF_CONG_RATE";
						sql = getQueryStsByCellVideoOrDataSql(field1,queryCondition,"stsAll",isAudio);
					}
					SQLQuery query = arg0.createSQLQuery(sql);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					List<Map<String,Object>> stsList = query.list();
					log.info("获取到结果集记录数量："
							+ (stsList == null ? 0 : stsList.size()));
					return stsList;
				}
			});
}

}
