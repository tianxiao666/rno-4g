package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.util.ArrayList;
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

import com.iscreate.op.pojo.rno.RnoAnalysisGisCell;
import com.iscreate.op.pojo.rno.RnoGisCell;
import com.iscreate.op.pojo.rno.RnoGisCellInterference;
import com.iscreate.op.pojo.rno.RnoAnalysisGisCellTopN;
import com.iscreate.op.pojo.rno.RnoInterferenceAnalysisGisCell;
/**
 * 总干扰分析Dao
	 * 
	 * @author ou.jh
	 * @date Nov 6, 2013
	 * @Description: TODO
	 * @param 
	 * @return 
	 * @throws
 */
public class RnoInterferenceAnalysisDaoImpl implements RnoInterferenceAnalysisDao {
	
	private static Log log = LogFactory.getLog(RnoInterferenceAnalysisDaoImpl.class);
	// ---注入----//
	private HibernateTemplate hibernateTemplate;
	
	/**
	 * 获取指定区/县区域下的小区
	* @author ou.jh
	* @date Nov 6, 2013 4:01:26 PM
	* @Description: TODO 
	* @param @param areaId
	* @param @return        
	* @throws
	 */
	public List<RnoAnalysisGisCell> getRnoGisCellInArea(final long areaId,final long cellConfigId,final boolean isCellTempStorage,final long interConfigId,final boolean isInterTemp) {
		log.info("进入getRnoGisCellInArea(final long areaId："+areaId+",final long cellConfigId:"+cellConfigId+",final boolean isCellTempStorage,final long interConfigId:"+interConfigId+",final boolean isInterTemp)");
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoAnalysisGisCell>>() {
					public List<RnoAnalysisGisCell> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String intertableName = " ";
						
						log.info("isInterTemp:"+isInterTemp+",isCellTempStorage:"+isCellTempStorage);
						if(isInterTemp){
							intertableName = " RNO_TEMP_INTERFERENCE ";
						}else{
							intertableName = " RNO_INTERFERENCE ";
						}
						String celltableName="";
						if(isCellTempStorage){
							celltableName="RNO_TEMP_CELL";
						}else{
							celltableName="CELL";
						}
						String interferenceSql ="  select t.label cell ,t.sumcica sumcica from ( "
						     +" select c.label, "
							 +"    sum(r.CI + r.CA) sumcica "
							 +" from (select * from "+intertableName+" r where r.DESCRIPTOR_ID = ?) r "
							 +" left join cell c on c.label = r.CELL "
							 +" left join cell ic on ic.label = r.interference_cell "
							 +" where instr(',' || ic.bcch || ',' || ic.tch, ',' || c.bcch || ',') > 0 "
							 +"   or instr(',' || ic.bcch || ',' || ic.tch, ',' || (c.bcch + 1) || ',') > 0 "
							 +"   or instr(',' || ic.bcch || ',' || ic.tch, ',' || (c.bcch - 1) || ',') > 0 "
							 +" group by c.label "
							 +" order by sumcica desc) t ";
						String sql = "select c.label as cell,"
								    +"   c.name as chineseName,"
								    +"   c.LONGITUDE as lng,"
								    +"   c.LATITUDE as lat,"
								    +"   c.LNGLATS as allLngLats,"
								    +"   c.BEARING as azimuth,"
								    +"   c.GSMFREQUENCESECTION as freqType,"
								    +"   c.SITE as site,"
								    +"   c.LAC as lac,"
								    +"   c.CI as ci,"
								    +"   c.BCCH as bcch,"
								    +"   c.TCH as tch,"
								    +"   r.sumcica as sumcica"
								    +"  from (select * from "+celltableName+" where CELL_DESC_ID = ?)c "
								    +"  left join ("+interferenceSql+") r on (c.label = r.cell) ";
						log.info("获取指定区/县区域下的小区的sql：" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoAnalysisGisCell.class);
						query.setLong(0, cellConfigId);
						query.setLong(1, interConfigId);
						List<RnoAnalysisGisCell> gisCells = query.list();
//						SQLQuery query1 = arg0.createSQLQuery(sql);
//						query1.setLong(0, areaId);
//						query1.setLong(1, configId);
//						query1.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//						List find = query1.list();
//						System.out.println(find);
						log.info("退出getRnoGisCellInArea获取的结果：" + gisCells == null ? 0
								: gisCells.size());
//						if(gisCells != null ){
//							for(RnoAnalysisGisCell t:gisCells){
//								System.out.println(t.getSumcica()+"|||");
//							}
//						}
						return gisCells;
					}
				});
	}
	
	/**
	 * top-N 最大干扰小区标注
	* @author ou.jh
	* @date Nov 6, 2013 4:01:26 PM
	* @Description: TODO 
	* @param @return        
	* @throws
	 */
//	public List<RnoAnalysisGisCellTopN> getRnoGisCellInAreaTopN(final long cellConfigId,final boolean isCellTemp,final long interConfigId,final long rank,final long areaId,final boolean isInterTempStorage) {
	public List<RnoAnalysisGisCellTopN> getRnoGisCellInAreaTopN(final long cellConfigId,final boolean isCellTemp,final long interConfigId,final boolean isInterTempStorage,final long rank,final long areaId){
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<RnoAnalysisGisCellTopN>>() {
					public List<RnoAnalysisGisCellTopN> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String intertableName = " ";
						if(isInterTempStorage){
							intertableName = " RNO_TEMP_INTERFERENCE ";
						}else{
							intertableName = " RNO_INTERFERENCE ";
						}
						String celltableName="";
						if(isCellTemp){
							celltableName="RNO_TEMP_CELL";
						}else{
							celltableName="CELL";
						}
						String interferenceSql ="  select t.label cell ,t.sumcica sumcica from ( "
						     +" select c.label, "
							 +"    sum(r.CI + r.CA) sumcica "
							 +" from (select * from "+celltableName+" where CELL_DESC_ID = ?) c "
							 +" left join (select * from "+intertableName+" r where r.DESCRIPTOR_ID = ?) r  on c.label = r.CELL "
							 +" left join cell ic on ic.label = r.interference_cell "
							 +" where instr(',' || ic.bcch || ',' || ic.tch, ',' || c.bcch || ',') > 0 "
							 +"   or instr(',' || ic.bcch || ',' || ic.tch, ',' || (c.bcch + 1) || ',') > 0 "
							 +"   or instr(',' || ic.bcch || ',' || ic.tch, ',' || (c.bcch - 1) || ',') > 0 "
							 +" group by c.label "
							 +" order by sumcica desc) t ";
						String sql = "select c.label as cell,"
								    +"   c.name as chineseName,"
								    +"   c.LONGITUDE as lng,"
								    +"   c.LATITUDE as lat,"
								    +"   c.LNGLATS as allLngLats,"
								    +"   c.BEARING as azimuth,"
								    +"   c.GSMFREQUENCESECTION as freqType,"
								    +"   c.SITE as site,"
								    +"   c.LAC as lac,"
								    +"   c.CI as ci,"
								    +"   c.BCCH as bcch,"
								    +"   c.TCH as tch,"
								    +"   r.sumcica "
								    +"  from (select * from "+celltableName+" where CELL_DESC_ID = ?)c "
								    +"  left join ("+interferenceSql+") r on (c.label = r.cell) ";
						sql = " select t.chineseName," +
								"t.lng," +
								"t.lat," +
								"t.allLngLats," +
								"t.cell," +
								"t.azimuth," +
								"t.freqType," +
								"t.site," +
								"t.lac," +
								"t.ci," +
								"t.bcch," +
								"t.tch," +
								"t.sumcica," +
								" rownum rank"
								+" from (  " +
								  sql
								 +" order by r.sumcica desc nulls Last) t "
								 +" where rownum <= ?";
						log.info("top-N 最大干扰小区标注的sql：" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoAnalysisGisCellTopN.class);
						query.setLong(0, cellConfigId);
						query.setLong(1, cellConfigId);
						query.setLong(2, interConfigId);
						query.setLong(3, rank);
						List<RnoAnalysisGisCellTopN> gisCells = query.list();
						log.info("getRnoGisCellInAreaTopN获取的结果：" + gisCells == null ? 0
								: gisCells.size());
						return gisCells;
					}
				});
	}
	
	/**
	 * 小区干扰分析
	* @author ou.jh
	* @date Nov 11, 2013 11:07:08 AM
	* @Description: TODO 
	* @param @param configId
	* @param @param areaId
	* @param @param cellLabel        
	* @throws
	 */
	public List<RnoInterferenceAnalysisGisCell> getCellInterferenceAnalysis(final long cellConfigId,final boolean isCellTemp,final long interConfigId,final boolean isInterTempStorage,final long areaId,final String cellLabel){
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<RnoInterferenceAnalysisGisCell>>() {
			public List<RnoInterferenceAnalysisGisCell> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String intertableName = " ";
				if(isInterTempStorage){
					intertableName = " RNO_TEMP_INTERFERENCE ";
				}else{
					intertableName = " RNO_INTERFERENCE ";
				}
				
				String celltableName="";
				if(isCellTemp){
					celltableName="RNO_TEMP_CELL";
				}else{
					celltableName="CELL";
				}
		String	sql =	"	select c.label as cell, "
						+"       c.name as chineseName, "
						+"       c.LONGITUDE as lng, "
						+"       c.LATITUDE as lat, "
						+"       c.LNGLATS as allLngLats, "
						+"       c.BEARING as azimuth, "
						+"       c.GSMFREQUENCESECTION as freqType, "
						+"       c.SITE as site, "
						+"       c.LAC as lac, "
						+"       c.CI as ci, "
						+"       c.BCCH as bcch, "
						+"       c.TCH as tch, "
						+"       r.sumcica , "
						+" CASE WHEN r.rank is not null THEN r.rank  "
						+" ELSE 0 END rank, "
						+"       (select count(*) from "+celltableName+" where CELL_DESC_ID = ?) tote, "
						+"       (select sum(r.CI + r.CA) "
						+"          from (select * from "+celltableName+" where CELL_DESC_ID = ?) c "
						+"          left join (select * "
						+"                      from "+intertableName+" r "
						+"                     where r.DESCRIPTOR_ID = ?) r on c.label = r.CELL "
						+"          left join cell ic on ic.label = r.interference_cell "
						+"         where instr(',' || ic.bcch || ',' || ic.tch, ',' || c.bcch || ',') > 0 "
						+"            or instr(',' || ic.bcch || ',' || ic.tch, "
						+"                     ',' || (c.bcch + 1) || ',') > 0 "
						+"            or instr(',' || ic.bcch || ',' || ic.tch, "
						+"                     ',' || (c.bcch - 1) || ',') > 0 "
						+"           and c.CELL_DESC_ID = ?) allsum "
						+"  from (select * "
						+"          from  "+celltableName
						+"         where CELL_DESC_ID = ? "
						+"           and label = ?) c "
						+"  left join (select t.label cell, t.sumcica sumcica, rownum rank "
						+"               from (select c.label, sum(r.CI + r.CA) sumcica "
						+"                       from (select * from "+celltableName+" where CELL_DESC_ID = ?) c "
						+"                       left join (select * "
						+"                                   from "+intertableName+" r "
						+"                                  where r.DESCRIPTOR_ID = ?) r on c.label = "
						+"                                                                   r.CELL "
						+"                       left join cell ic on ic.label = r.interference_cell "
						+"                      where instr(',' || ic.bcch || ',' || ic.tch, "
						+"                                  ',' || c.bcch || ',') > 0 "
						+"                         or instr(',' || ic.bcch || ',' || ic.tch, "
						+"                                  ',' || (c.bcch + 1) || ',') > 0 "
						+"                         or instr(',' || ic.bcch || ',' || ic.tch, "
						+"                                  ',' || (c.bcch - 1) || ',') > 0 "
						+"                        and c.CELL_DESC_ID = ? "
						+"                      group by c.label "
						+"                      order by sumcica desc nulls Last) t) r on (c.label = "
						+"                                                                r.cell) ";
						log.info("小区干扰分析的sql：" + sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						query.addEntity(RnoInterferenceAnalysisGisCell.class);
						query.setLong(0, cellConfigId);
						query.setLong(1, cellConfigId);
						query.setLong(2, interConfigId);
						query.setLong(3, cellConfigId);
						query.setLong(4, cellConfigId);
						query.setString(5, cellLabel);
						query.setLong(6, cellConfigId);
						query.setLong(7, interConfigId);
						query.setLong(8, cellConfigId);
						log.info("小区干扰分析设置后的sql：cellConfigId:" + cellConfigId+" cellLabel:"+cellLabel+" interConfigId:"+interConfigId);
						List<RnoInterferenceAnalysisGisCell> gisCells = query.list();
						log.info("getCellInterferenceAnalysis获取的结果：" + gisCells == null ? 0
								: gisCells.size());
						return gisCells;
					}
				});
	}
	
	/**
	 * 根据CELLLABEL获取干扰邻区
	* @author ou.jh
	* @date Nov 11, 2013 4:51:44 PM
	* @Description: TODO 
	* @param @param label        
	* @throws
	 */
	public List<RnoGisCell> getInterferenceCellByLabel(final long cellConfigId,final boolean isCellTemp,final long interConfigId,final boolean isInterTempStorage,final String label){
		log.info("进入getInterferenceCellByLabel(final long cellConfigId："+cellConfigId+",final boolean isCellTemp:"+isCellTemp+",final long interConfigId:"+interConfigId+",final boolean isInterTempStorage:"+isInterTempStorage+",final String label)"+label);
		return hibernateTemplate
		.executeFind(new HibernateCallback<List<RnoGisCell>>() {
			public List<RnoGisCell> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String intertableName = " ";
				if(isInterTempStorage){
					intertableName = " RNO_TEMP_INTERFERENCE ";
				}else{
					intertableName = " RNO_INTERFERENCE ";
				}
				

				String celltableName="";
				if(isCellTemp){
					celltableName="RNO_TEMP_CELL";
				}else{
					celltableName="CELL";
				}
				
				String sql = 
					" select c1.label as cell,"
				    +"   c1.name as chineseName,"
				    +"   c1.LONGITUDE as lng,"
				    +"   c1.LATITUDE as lat,"
				    +"   c1.LNGLATS as allLngLats,"
				    +"   c1.BEARING as azimuth,"
				    +"   c1.GSMFREQUENCESECTION as freqType,"
				    +"   c1.SITE as site,"
				    +"   c1.LAC as lac,"
				    +"   c1.CI as ci,"
				    +"   c1.BCCH as bcch,"
				    +"   c1.TCH as tch "
					+"   from "+celltableName+" c, rno_interference r, "+celltableName+" c1 "
					+"  where c.label = r.cell "
					+"    and r.interference_cell = c1.label "
					+"    and r.is_neighbour = 'Y' "
					+"    and c.label = ? "
					+"    and r.descriptor_id = ? and c1.area_Id is not null ";
				SQLQuery query = arg0.createSQLQuery(sql);
				query.addEntity(RnoGisCell.class);
				query.setString(0, label);
				query.setLong(1, interConfigId);
				log.info("sql:"+sql);
				List<RnoGisCell> gisCells = query.list();
				log.info("getInterferenceCellByLabel获取的结果：" + gisCells == null ? 0
						: gisCells.size());
				return gisCells;
			}
		});
	}
		
	/**
	 * 根据小区获取干扰频点
	* @author ou.jh
	* @date Nov 12, 2013 11:22:01 AM
	* @Description: TODO 
	* @param         
	* @throws
	 */
	public List<Map<String, Object>> getInterferenceTCH(final long cellConfigId,final boolean isCellTemp,final long interConfigId,final boolean isInterTempStorage,final long areaId,final String label){
		log.info("getInterferenceTCH(final long cellConfigId:"+cellConfigId+",final boolean isCellTemp:"+isCellTemp+",final long interConfigId:"+interConfigId+",final boolean isInterTempStorage:"+isInterTempStorage+",final long areaId:"+areaId+",final String label)"+label);	
		return hibernateTemplate
			.executeFind(new HibernateCallback<List<Map<String, Object>>>() {
				public List<Map<String, Object>> doInHibernate(Session arg0)
						throws HibernateException, SQLException {
					String intertableName = " ";
					if(isInterTempStorage){
						intertableName = " RNO_TEMP_INTERFERENCE ";
					}else{
						intertableName = " RNO_INTERFERENCE ";
					}
					String celltableName="";
					if(isCellTemp){
						celltableName="RNO_TEMP_CELL";
					}else{
						celltableName="CELL";
					}
					
					String sql = 
						" select c.bcch bcch, ic.bcch || ',' || ic.tch ictch "
						+"  from (select label,bcch from "+celltableName+" where CELL_DESC_ID = ? and label=?) c "
						+"  left join (select CELL,INTERFERENCE_CELL from "+intertableName+" r where r.DESCRIPTOR_ID = ? and r.CELL=?) r on c.label = r.CELL "
						+"  left join "+celltableName+" ic on ic.label = r.interference_cell "
						+" where instr(',' || ic.bcch || ',' || ic.tch, ',' || c.bcch || ',') > 0 "
						+"    or instr(',' || ic.bcch || ',' || ic.tch, ',' || (c.bcch + 1) || ',') > 0 "
						+"    or instr(',' || ic.bcch || ',' || ic.tch, ',' || (c.bcch - 1) || ',') > 0 ";
						
					log.info("根据小区获取干扰频点的sql="+sql);
					SQLQuery query = arg0.createSQLQuery(sql);
					query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
					query.setLong(0, cellConfigId);
					query.setString(1, label);
					query.setLong(2, interConfigId);
					query.setString(3, label);
					log.info("sql:"+sql);
//					query.setLong(1, interConfigId);
//					query.setLong(2, cellConfigId);
//					query.setString(3, label);
					List<Map<String, Object>> gisCells = query.list();
					log.info("getInterferenceCellByLabel获取的结果：" + gisCells == null ? 0
							: gisCells.size());
					return gisCells;
				}
			});
		}


	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
