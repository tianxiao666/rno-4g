package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.util.ArrayList;
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

public class Rno2GCellAnalysisDaoImpl implements Rno2GCellAnalysisDao {

	private static Log log=LogFactory.getLog(Rno2GCellAnalysisDaoImpl.class);
	
	private HibernateTemplate hibernateTemplate;

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
	/**
	 * 判断某个日期是否存在爱立信小区数据
	 * @param cityId
	 * @param bscStr
	 * @param paramType
	 * @param date
	 * @return
	 * @author peng.jm
	 * @date 2014-10-22下午04:39:12
	 */
	public boolean isEriCellDataExistedOnTheDate(long cityId, String paramType,
			String bscStr, String date) {
		log.debug("进入方法isEriCellDataExistedOnTheDate。");
		
		String s = "";
		if(("cell").equals(paramType)) {
			s += " rno_2g_eri_cell cell, rno_2g_eri_cell_extra_info extra "+
				"         where cell.cell = extra.cell "+
				"          and cell.mea_date = extra.mea_date "+
				"          and cell.city_id = extra.city_id "+
				"          and cell.bsc = extra.bsc "+
				"          and cell.mea_date = to_date('"+date+"', 'yyyy-MM-dd') "+
				"          and cell.city_id = " + cityId +
				"          and cell.bsc in ("+bscStr+")";
		} else if(("channel").equals(paramType)) {
			s += " rno_2g_eri_cell_ch_group "+
				"     where mea_date = to_date('"+date+"', 'yyyy-MM-dd') "+
				"     and city_id = " + cityId +
				"     and bsc in ("+bscStr+")";			
		} else if(("neighbour").equals(paramType)) {
			s += " rno_2g_eri_ncell_param "+
			"     where mea_date = to_date('"+date+"', 'yyyy-MM-dd') "+
			"     and city_id = " + cityId +
			"     and bsc in ("+bscStr+")";			
		}
		final String sqlStr = s;
		
		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {
			@Override
			public Boolean doInHibernate(Session session)
					throws HibernateException, SQLException {
				String sql = "select count(*) from " + sqlStr;
				log.debug("判断某个日期是否存在爱立信小区数据,sql=" + sql);
				SQLQuery query = session.createSQLQuery(sql);
				List<Object> list = query.list();
				Long cnt = 0l;
				if (list != null && list.size() > 0) {
					cnt = Long.valueOf(list.get(0).toString());
				}
				boolean flag = false;
				if(cnt > 0) {
					flag = true;
				}
				log.debug("该日期是否存在数据,结果="+flag);
				return flag;
			}
		});
	}

	/**
	 * 获取对应bscId的BSC信息
	 * @param bscIdStr
	 * @return
	 * @author peng.jm
	 * @date 2014-10-22下午05:20:32
	 */
	public List<Map<String, Object>> getBscByBscIdStr(final String bscIdStr) {
		log.info("进入方法：getBscByBscIdStr. bscIdStr=" + bscIdStr);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				String sql = "select * from rno_bsc where bsc_id in("+bscIdStr+") order by engname";
				log.info("getBscByBscIdStr,  sql="+sql);
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	/**
	 * 通过参数类型获取爱立信小区两个日期的参数比较结果
	 * @param cityId
	 * @param paramType
	 * @param paramStr
	 * @param bscStr
	 * @param date1
	 * @param date2
	 * @return
	 * @author peng.jm
	 * @date 2014-10-23下午03:08:21
	 */
	public List<Map<String, Object>> getEriCellParamsCompareResByTypeAndDate(
			long cityId, String paramType, String paramStr, String bscStr,
			String date1, String date2) {
		log.info("进入方法：getEriCellParamsCompareResByTypeAndDate。 cityId=" + cityId 
				+ ",paramType="+ paramType + ",paramStr=" + paramStr + ",bscStr=" + bscStr
				+ ",date1=" + date1 + ",date2=" + date2);
		
		final String sql = makeupCompareSql(cityId,paramType,paramStr,bscStr,date1,date2);
		log.info("getEriCellParamsCompareResByTypeAndDate。sql="+sql);
		
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	private String makeupCompareSql(long cityId, String paramType,
			String paramStr, String bscStr, String date1, String date2) {
		String sumSql = "";
		String caseSql = "";
		String paramsSql = "";
		String params[] = paramStr.split(",");
		if(params.length == 0) {
			log.error("组成sql所需参数为空！");
			return "";
		}
		for (String param : params) {
			//TO改为“TO”
			if(("TO").equals(param)) {
				sumSql += "sum(\""+param+"\") as \""+param+"\",";
			} else {
				sumSql += "sum("+param+") as "+param+",";
			}
			//TO改为“TO”
			if(("TO").equals(param)) {
				caseSql += "case when t1.\""+param+"\"<> t2.\""+param+"\" then 1 else 0 end as \""+param+"\",";
			} else {
				caseSql += "case when t1."+param+"<> t2."+param+" then 1 else 0 end as "+param+",";
			}
			/**CELL**/
			//TO改为“TO”
			if(("TO").equals(param)) {
				paramsSql += "\"TO\"" + ",";
			} 
			//BCCH改为BCCHNO
			else if(("BCCH").equals(param)) {
				paramsSql += "BCCHNO as BCCH" + ",";
			} 
			//BSIC改为NCC||BCC as BSIC
			else if(("BSIC").equals(param)) {
				paramsSql += " NCC||BCC as BSIC" + ",";
			}
			//ACC改为ACC_16
			else if(("ACC").equals(param)) {
				paramsSql += " ACC_16 as ACC" + ",";
			}
			//NCCPERM改为NCCPERM_8
			else if(("NCCPERM").equals(param)) {
				paramsSql += " NCCPERM_8 as NCCPERM" + ",";
			}
			//ACTIVEMBCCHNO改为ACTIVE_32
			else if(("ACTIVEMBCCHNO").equals(param)) {
				paramsSql += " ACTIVE_32 as ACTIVEMBCCHNO" + ",";
			}
			//IDLEMBCCHNO改为IDLE_32
			else if(("IDLEMBCCHNO").equals(param)) {
				paramsSql += " IDLE_32 as IDLEMBCCHNO" + ",";
			}
			/**Channel**/
			//MAIO改为MAIO_16
			else if(("MAIO").equals(param)) {
				paramsSql += " MAIO_16 as MAIO" + ",";
			}
			//MAIO改为MAIO_16
			else if(("ETCHTN").equals(param)) {
				paramsSql += " ETCHTN_8 as ETCHTN" + ",";
			}
			else {
				paramsSql += param + ",";
			}
		}
		sumSql = sumSql.substring(0, sumSql.length() - 1);
		caseSql = caseSql.substring(0, caseSql.length() - 1);
		paramsSql = paramsSql.substring(0, paramsSql.length() - 1);
		
		//参数集sql
		String resultSql = "";
		if(("cell").equals(paramType)) {
			resultSql += "select BSC,"+sumSql+" from (" +
					"select t1.BSC,"+caseSql+" from (" +
								" select cell.BSC,cell.CELL," + paramsSql +
								"	 from rno_2g_eri_cell cell, " +
                            "         rno_2g_eri_cell_extra_info extra " +
                            "  where cell.cell = extra.cell " +
                            "    and cell.mea_date = extra.mea_date " +
                            "    and cell.city_id = extra.city_id " +
                            "    and cell.bsc = extra.bsc " +
                            "    and cell.mea_date = to_date('"+date1+"', 'yyyy-MM-dd') " +
                            "    and cell.city_id = " + cityId +
                            "    and cell.bsc in("+bscStr+")) t1 " +
		             " left join (select cell.BSC,cell.CELL," + paramsSql +
		             				" from rno_2g_eri_cell cell,  " +
		                       "       			rno_2g_eri_cell_extra_info extra " +
		                       "   where cell.cell = extra.cell   " +
		                       "    and cell.mea_date = extra.mea_date " +
		                       "    and cell.city_id = extra.city_id " +
		                       "    and cell.bsc = extra.bsc " +
		                       "    and cell.mea_date = to_date('"+date2+"', 'yyyy-MM-dd') " +
		                       "    and cell.city_id = " + cityId +
		                       "    and cell.bsc in("+bscStr+")) t2 " +
		               " on (t1.cell = t2.cell and t1.bsc = t2.bsc)) " +
				" group by BSC";
		} 
		else if(("channel").equals(paramType)) {
			resultSql += "select BSC,"+sumSql+" from (" +
				"select t1.BSC,"+caseSql+" from (" +
							" select BSC,CELL,CH_GROUP," + paramsSql +
							"	 from rno_2g_eri_cell_ch_group " +
	                    "  where mea_date = to_date('"+date1+"', 'yyyy-MM-dd') " +
	                    "    and city_id = " + cityId +
	                    "    and bsc in("+bscStr+")) t1 " +
	               " left join (select BSC,CELL,CH_GROUP," + paramsSql +
	             			    " from rno_2g_eri_cell_ch_group " +
	                       "   where mea_date = to_date('"+date2+"', 'yyyy-MM-dd') " +
	                       "    and city_id = " + cityId +
	                       "    and bsc in("+bscStr+")) t2 " +
	               " on (t1.cell = t2.cell and t1.bsc = t2.bsc and t1.ch_group = t2.ch_group)) " +
               " group by BSC ";
		} 
		else if(("neighbour").equals(paramType)) {
			resultSql += "select BSC,"+sumSql+" from (" +
			"select t1.BSC,"+caseSql+" from (" +
						" select BSC,CELL,N_CELL," + paramsSql +
						"	 from rno_2g_eri_ncell_param " +
                    "  where mea_date = to_date('"+date1+"', 'yyyy-MM-dd') " +
                    "    and city_id = " + cityId +
                    "    and bsc in("+bscStr+")) t1 " +
               " left join (select BSC,CELL,N_CELL," + paramsSql +
             			    " from rno_2g_eri_ncell_param " +
                       "   where mea_date = to_date('"+date2+"', 'yyyy-MM-dd') " +
                       "    and city_id = " + cityId +
                       "    and bsc in("+bscStr+")) t2 " +
               " on (t1.cell = t2.cell and t1.n_cell = t2.n_cell)) " +
           " group by BSC ";
		}
		return resultSql;
	}

	/**
	 * 获取爱立信小区某两个日期的参数不一致的详情
	 * @param cityId
	 * @param bsc
	 * @param param
	 * @param date1
	 * @param date2
	 * @return
	 * @author peng.jm
	 * @date 2014-10-23下午06:05:13
	 */
	public List<Map<String, Object>> getEriCellParamsDiffDetail(long cityId,
			String bsc, String paramType, String param, String date1, String date2) {
		log.info("进入方法：getEriCellParamsDiffDetail。 cityId=" + cityId 
				+ ",bsc="+ bsc + ",paramType=" + paramType + ",param=" + param
				+ ",date1=" + date1 + ",date2=" + date2);
		
		final String sql = makeupCompareDetailSql(cityId, bsc, paramType, param, date1, date2);
		log.info("getEriCellParamsDiffDetail。sql="+sql);
		
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	private String makeupCompareDetailSql(long cityId, String bsc, String paramType,
			String param, String date1, String date2) {
		
		/**CELL**/
		//TO改为“TO”
		if(("TO").equals(param)) {
			param = "\"TO\"";
		} 
		//BCCH改为BCCHNO
		else if(("BCCH").equals(param)) {
			param = "BCCHNO";
		} 
		//BSIC改为NCC||BCC as BSIC
		else if(("BSIC").equals(param)) {
			param = "NCC||BCC";
		}
		//ACC改为ACC_16
		else if(("ACC").equals(param)) {
			param = "ACC_16";
		}
		//NCCPERM改为NCCPERM_8
		else if(("NCCPERM").equals(param)) {
			param = "NCCPERM_8";
		}
		//ACTIVEMBCCHNO改为ACTIVE_32
		else if(("ACTIVEMBCCHNO").equals(param)) {
			param = "ACTIVE_32";
		}
		//IDLEMBCCHNO改为IDLE_32
		else if(("IDLEMBCCHNO").equals(param)) {
			param = "IDLE_32";
		}
		/**Channel**/
		//MAIO改为MAIO_16
		else if(("MAIO").equals(param)) {
			param = "MAIO_16";
		}
		//ETCHTN改为ETCHTN_8
		else if(("ETCHTN").equals(param)) {
			param = "ETCHTN_8";
		}
		
		String resultSql = "";
		
		if(("cell").equals(paramType)) {
			resultSql += " select t1.cell,param1,param2 " +
				 "   from  (select cell.cell,cell.bsc,"+param+" as param1 " +
				 						" from rno_2g_eri_cell cell, " +
				 "                              rno_2g_eri_cell_extra_info extra" +
				 "                       where cell.cell = extra.cell" +
				 "                         and cell.mea_date = extra.mea_date" +
				 "                         and cell.city_id = extra.city_id" +
				 "                         and cell.bsc = extra.bsc" +
				 "                         and cell.mea_date = to_date('"+date1+"', 'yyyy-MM-dd')" +
				 "                         and cell.city_id =" + cityId +
				 "                         and cell.bsc=(select bsc_id " +
			 								 " from rno_bsc where engname='"+bsc+"')) t1" +
				 "          left join (select cell.cell,cell.bsc,"+param+" as param2 " +
				 						" from rno_2g_eri_cell cell, " +
				 "                              rno_2g_eri_cell_extra_info extra" +
				 "                       where cell.cell = extra.cell  " +
				 "                         and cell.mea_date = extra.mea_date" +
				 "                         and cell.city_id = extra.city_id " +
				 "                         and cell.bsc = extra.bsc " +
				 "                         and cell.mea_date = to_date('"+date2+"', 'yyyy-MM-dd')" +
				 "                         and cell.city_id =" + cityId +
				 "                         and cell.bsc=(select bsc_id " +
			 								 " from rno_bsc where engname='"+bsc+"')) t2" +
				 "            on (t1.cell = t2.cell and t1.bsc = t2.bsc)" +
				 "    where param1 <> param2";
		} 
		else if(("channel").equals(paramType)) {
			resultSql += " select t1.cell,t1.ch_group,param1,param2 " +
			 "   from  (select cell,bsc,ch_group,"+param+" as param1 " +
			 						" from rno_2g_eri_cell_ch_group " +
			 "                   where mea_date = to_date('"+date1+"', 'yyyy-MM-dd')" +
			 "                      and city_id =" + cityId +
			 "                      and bsc=(select bsc_id " +
		 								 " from rno_bsc where engname='"+bsc+"')) t1" +
			 "          left join (select cell,bsc,ch_group,"+param+" as param2 " +
			 							" from rno_2g_eri_cell_ch_group " +
			 "                   	where mea_date = to_date('"+date2+"', 'yyyy-MM-dd')" +
			 "                         and city_id =" + cityId +
			 "                         and bsc=(select bsc_id " +
		 								 " from rno_bsc where engname='"+bsc+"')) t2" +
			 "            on (t1.cell = t2.cell and t1.bsc = t2.bsc and t1.ch_group = t2.ch_group) " +
			 "    where param1 <> param2";
		} 
		else if(("neighbour").equals(paramType)) {
			resultSql += " select t1.cell,t1.n_cell,param1,param2 " +
			 "   from  (select cell,n_cell,"+param+" as param1 " +
			 						" from rno_2g_eri_ncell_param " +
			 "                   where mea_date = to_date('"+date1+"', 'yyyy-MM-dd')" +
			 "                     and city_id =" + cityId +
			 "                     and bsc=(select bsc_id " +
		 								 " from rno_bsc where engname='"+bsc+"')) t1" +
			 "          left join (select cell,n_cell,"+param+" as param2 " +
			 							" from rno_2g_eri_ncell_param " +
			 "                   	where mea_date = to_date('"+date2+"', 'yyyy-MM-dd')" +
			 "                         and city_id =" + cityId +
			 "                         and bsc=(select bsc_id " +
		 								 " from rno_bsc where engname='"+bsc+"')) t2" +
			 "            on (t1.cell = t2.cell and t1.n_cell = t2.n_cell)" +
			 "    where param1 <> param2";
		}
		return resultSql;
	}
	
	/**
	 * 通过bsc名称字符串获取bsc信息
	 * @param bscStr
	 * @return
	 * @author peng.jm
	 * @date 2014-10-27下午06:17:45
	 */
	public List<Map<String, Object>> getBscDetailByBscs(final String bscStr) {
		log.info("进入方法：getBscDetailByBscs. bscStr=" + bscStr);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				String sql = "select * from rno_bsc where engname in("+bscStr+") order by engname";
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	
	/**
	 * 获取爱立信小区功率检查结果
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @return
	 * @author peng.jm
	 * @date 2014-10-27下午02:19:17
	 */
	public List<Map<String, Object>> getEriCellPowerCheckResult(String bscIdStr,
			String date, long cityId) {
		log.info("进入方法：getEriCellPowerCheckResult。 cityId=" + cityId 
				+ ",bscIdStr="+ bscIdStr + ",date=" + date);
		
		final String sql = " select bsc.engname as bsc,t.cell,t.bspwrb,t.bspwrt, "+
			" 'RLCPC:CELL='||t.cell||',BSPWRB='||t.bspwrb||',BSPWRT='||t.bspwrb||';' as command "+
					" 			from rno_2g_eri_cell t "+
					" 			  left join rno_bsc bsc " +
					"			  on bsc.bsc_id = t.bsc "+
					" 		where t.bspwrb <> t.bspwrt "+
					" 		   and t.mea_date = to_date('"+date+"', 'yyyy-MM-dd') "+
					" 		   and t.city_id ="+cityId+
					"		   and t.bsc in("+bscIdStr+")"+
					" 		order by bsc.engname ";
		
		log.info("功率检查。sql="+sql);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	/**
	 * 获取爱立信小区跳频检查结果
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-27下午06:05:57
	 */
	public List<Map<String, Object>> getEriCellFreqHopCheckResult(
			String bscIdStr, String date, long cityId, Map<String,String> settings) {
		log.info("进入方法：getEriCellFreqHopCheckResult。 cityId=" + cityId 
				+ ",bscIdStr="+ bscIdStr + ",date=" + date + ",settings="+settings);
		String isCheckMaxChgr = settings.get("isCheckMaxChgr") != null ? settings
				.get("isCheckMaxChgr").toString()
				: "";
		String s = "";
		if(("true").equals(isCheckMaxChgr)) {
			s = " select * from( " +
				"     select bsc.engname as bsc," +
				"			 cell," +
				"			 ch_group," +
				"			 hop," +
				"            RNO_GET_FREQ_CNT(dchno_32) as dchno," +
				"            dchno_32 as dch," +
				"            'RLCHC:CELL='||cell||',HOP=ON,CHGR='||ch_group||';' as command" +
				"       from rno_2g_eri_cell_ch_group t" +
				"    left join rno_bsc bsc on (bsc.bsc_id = t.bsc) "+
				"      where t.hop = 'OFF'" +
				"       and t.mea_date = to_date('"+date+"', 'yyyy-MM-dd')" +
				"       and t.city_id =" + cityId +
				"       and t.bsc in ("+bscIdStr+"))" +
				"  where dchno > 2";
		} else {
			//信道组最大忽略掉
			s = " select bsc, cell, ch_group, hop, dchno, dch, command "+
				"       from (select bsc.engname as bsc,"+
				"                   cell,"+
				"                   ch_group,"+
				"                   rank() over(partition by cell order by ch_group desc) as chgr_rn,"+
				"                   hop,"+
				"                   RNO_GET_FREQ_CNT(dchno_32) as dchno,"+
				"                   dchno_32 as dch,"+
				"                   'RLCHC:CELL=' || cell || ',HOP=ON,CHGR=' || ch_group || ';' as command"+
				"             from rno_2g_eri_cell_ch_group t "+
				"         left join rno_bsc bsc on (bsc.bsc_id = t.bsc) "+
				"              where t.hop = 'OFF'"+
				"                and t.mea_date = to_date('"+date+"', 'yyyy-MM-dd')"+
				"                and t.city_id = " + cityId +
				"                and t.bsc in ("+bscIdStr+")) "+
				"     where dchno > 2 and chgr_rn > 1 ";
		}
		final String sql = s;
		log.info("跳频检查。sql="+sql);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	/**
	 * 获取爱立信小区Nccperm检查结果
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-28下午04:16:24
	 */
	public List<Map<String, Object>> getEriCellNccpermResult(String bscIdStr,
			String date, long cityId, Map<String, String> settings) {
		log.info("进入方法：getEriCellNccpermResult。 cityId=" + cityId 
				+ ",bscIdStr="+ bscIdStr + ",date=" + date + ",settings="+settings);
		/*
		 * 1、获取爱立信小区表的cell，bsc，nccperm信息作为t1
		 * 2、获取对应cell，ncell，邻区对应ncc转成字符串作为t2，
		 * 3、t1连接t2作为结果集
		 */
		final String sql = " select t1.bsc, t1.cell, t1.nccperm, t2.ncell_ncc " +
			 	"    from (select cell.cell, bsc.engname as bsc, cell.nccperm_8 as nccperm 				" +
			 	"               from rno_2g_eri_cell cell 												" +
				"                left join rno_bsc bsc on bsc.bsc_id = cell.bsc 						" +
				"		    where cell.city_id =" + cityId +
				"			  and cell.mea_date = to_date('"+date+"', 'yyyy-MM-dd') 					" +
				"			  and cell.bsc in("+bscIdStr+")												" +
				"		   ) t1 																		" +
				"   left join (select cell, wm_concat(distinct(ncc)) as ncell_ncc 						" +
			    "            from (select ncell.cell, ncell.n_cell, cell.ncc 							" +
			    "                    from (select cell, n_cell 											" +
			    "                            from rno_2g_eri_ncell_param 								 " +
			    "                           where city_id =" + cityId +
			    "                             and mea_date = to_date('"+date+"', 'yyyy-MM-dd')			 " +
			    "							  and bsc in("+bscIdStr+")) ncell 					  		 " +
			    "                    left join (select cell as ncell, ncc 								 " +
			    "                                from rno_2g_eri_cell 					 				 " +
			    "                               where city_id =" + cityId +
			    "                                 and mea_date = to_date('"+date+"', 'yyyy-MM-dd')		 " +
			    "							 	  and bsc in("+bscIdStr+")) cell 						 " + 
		        "                   on (ncell.n_cell = cell.ncell) order by cell.ncc)  							         " +
			   	"				group by cell 						                                     " +
				"           ) t2  									                                     " +
			    "     on t1.cell = t2.cell 								                                     " +
				"  where nccperm <> ncell_ncc ";
		log.info("Nccperm检查。sql="+sql);
		
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	/**
	 * 获取爱立信小区单向邻区检查
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-29下午01:54:41
	 */
	public List<Map<String, Object>> getEriCellUnidirNcellResult(
			String bscIdStr, String date, long cityId,
			Map<String, String> settings) {
		log.info("进入方法：getEriCellUnidirNcellResult。 cityId=" + cityId 
				+ ",bscIdStr="+ bscIdStr + ",date=" + date + ",settings="+settings);

		final String sql = " select bsc, " +
				"				cell, " +
				"				n_cell as CELLR," +
				"				cast('SINGLE'as varchar2(6)) as DIR," +
				"				n_bsc as CELLR_BSC," +
			"  		 case when bsc=n_bsc then '是' else '否' end as IS_SAME_BSC,  " +
			"  		'RLNRC:CELLR='||n_cell||',CELL='||cell||',SINGLE;' as COMMAND  " +
			" 	     from ( select t1.bsc, " +
			" 	                 t1.cell, " +
			" 	                 t2.n_bsc, " +
			" 	                 t2.n_cell  " +
			" 	                 from( select bsc.engname as bsc,cell.cell   " +
			" 	                        from rno_2g_eri_cell cell " +
			" 	                         left join rno_bsc bsc on(bsc.bsc_id = cell.bsc) " +
			" 	                       where cell.city_id = " + cityId +
			" 	                         and cell.mea_date = to_date('"+date+"', 'yyyy-MM-dd')" +
			"							 and cell.bsc in ("+bscIdStr+")) t1 " +
			" 	              left join (  " +
			" 	                        select bsc.engname as bsc,param.cell,param.n_bsc,param.n_cell " +
			" 	                          from rno_2g_eri_ncell_param param " +
			" 	                         left join rno_bsc bsc on(bsc.bsc_id = param.bsc) " +
			" 	                         where param.city_id = " + cityId +
			" 	                           and param.mea_date = to_date('"+date+"', 'yyyy-MM-dd')" +
			"							   and param.bsc in ("+bscIdStr+")) t2 " +
			" 	              on (t1.cell=t2.cell and t1.bsc = t2.bsc)   " +                       
			" 	          minus " +
			" 	          select t2.n_bsc as bsc, " +
			" 	                 t2.n_cell as cell, " +
			" 	                 t1.bsc as n_bsc, " +
			" 	                 t1.cell as ncell  " +
			" 	                 from( select bsc.engname as bsc,cell.cell   " +
			" 	                        from rno_2g_eri_cell cell " +
			" 	                         left join rno_bsc bsc on(bsc.bsc_id = cell.bsc) " +
			" 	                       where cell.city_id = " + cityId +
			" 	                         and cell.mea_date = to_date('"+date+"', 'yyyy-MM-dd')" +
			"							 and cell.bsc in ("+bscIdStr+")) t1 " +
			" 	              left join (  " +
			" 	                        select bsc.engname as bsc,param.cell,param.n_bsc,param.n_cell " +
			" 	                          from rno_2g_eri_ncell_param param " +
			" 	                         left join rno_bsc bsc on(bsc.bsc_id = param.bsc) " +
			" 	                         where param.city_id = " + cityId +
			" 	                           and param.mea_date = to_date('"+date+"', 'yyyy-MM-dd')" +
			"							   and param.bsc in ("+bscIdStr+")) t2 " +
			" 	              on (t1.cell=t2.cell and t1.bsc = t2.bsc)    " +
			" 	    )   where n_bsc is not null " +
			" 	          and n_cell is not null";
		log.info("单向邻区检查。sql="+sql);
		
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	/**
	 * 获取爱立信小区同邻频数据信息
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-30下午02:04:06
	 */
	public List<Map<String, Object>> getEriCellSameNcellFreqData(
			String bscIdStr, String date, long cityId,
			Map<String, String> settings) {
		log.info("进入方法：getEriCellSameNcellFreqData。 cityId=" + cityId 
				+ ",bscIdStr="+ bscIdStr + ",date=" + date + ",settings="+settings);

		final String sql = "  select tt.bsc, tt.cell, tt.bcchno, tt.tch," +
				"					 tt2.n_cell, tt2.n_bcchno, tt2.n_tch," +
				"					fun_rno_getdistance(tt.lng,tt.lat,tt2.n_lng,tt2.n_lat) as distance," +
			" 			            case when substr(tt.cell, 0, 6) = substr(tt2.n_cell, 0, 6) then " +
			" 				           cast('YES' as varchar2(3)) " +
			" 					          else " +
			" 				           cast('NO'as varchar2(2)) " +
			" 				         end as cs " +
			"         from ( select t2.bsc,t2.bsc_id,t2.cell , " +
			"                        t2.bcchno,t1.tch,t1.band,t1.lng,t1.lat " +
			"                     from ( select g.cell," +
			"									wm_concat(g.dchno_32) as tch," +
			"									g.band, " +
			"									c.longitude as lng, " +
			"									c.latitude as lat " +
			"                               from rno_2g_eri_cell_ch_group g " +
			"								left join cell c on (c.label = g.cell)" +
			"                              where g.city_id ="+cityId+
			"                                and g.mea_date = to_date('"+date+"', 'yyyy-MM-dd') " +
			"                                and g.bsc in ("+bscIdStr+") " +
			"                              group by g.cell,g.band,c.longitude,c.latitude) t1 " +
			"                      left join ( select bsc.engname as bsc,cell.bsc as bsc_id,cell.cell,cell.bcchno " + 
			"                                from rno_2g_eri_cell cell " +
			"                                left join rno_bsc bsc on bsc.bsc_id = cell.bsc " +
			"                             where  cell.city_id ="+cityId+
			"                                and cell.mea_date = to_date('"+date+"', 'yyyy-MM-dd') " +
			"                                and cell.bsc in ("+bscIdStr+")) t2 " +
			"                        on (t1.cell = t2.cell) ) tt " +
			" 	  left join (  select t1.bsc as bsc_id, " +
			" 	                     t1.cell, " +
			" 	                     t1.n_cell, " +
			" 	                     t2.bcchno as n_bcchno, " +
			"						 t2.n_lng,	" +
			"						 t2.n_lat,	" +
			" 	                     t3.n_tch, " +
			" 	                     t3.n_band " +
			" 	                from (select bsc,n_bsc, cell, n_cell " +
			" 	                        from rno_2g_eri_ncell_param " +
			" 	                       where city_id ="+cityId+
			" 	                         and mea_date = to_date('"+date+"', 'yyyy-MM-dd') " +
			" 	                         and bsc in ("+bscIdStr+")) t1 " +
			" 	                left join (select bsc.engname as bsc, " +
			" 	                                  cell.cell, " +
			" 	                                  cell.bcchno," +
			"									  c.longitude as n_lng, "+
            "                      				  c.latitude as n_lat " +
			" 	                            from rno_2g_eri_cell cell " +
			" 	                             left join rno_bsc bsc on (cell.bsc = bsc.bsc_id) " +
			"								 left join cell c on (c.label = cell.cell) " +
			" 	                           where cell.city_id = "+cityId+
			" 	                             and cell.mea_date =to_date('"+date+"', 'yyyy-MM-dd') " +
			" 	                             and cell.bsc in ("+bscIdStr+")) t2  " +
			" 	                     on (t1.n_cell =t2.cell and t1.n_bsc = t2.bsc) " +
			" 	                left join (select cell,wm_concat(dchno_32) as n_tch,band as n_band " +
			" 	                             from rno_2g_eri_cell_ch_group " +
			" 	                            where city_id = "+cityId+
			" 	                              and mea_date = to_date('"+date+"', 'yyyy-MM-dd') " +
			" 	                              and bsc in ("+bscIdStr+") " +
			" 	                            group by cell,band) t3 " +
			" 	                      on (t1.n_cell = t3.cell)) tt2 " +
			"   on(tt.bsc_id = tt2.bsc_id and tt.cell = tt2.cell and tt.band = tt2.n_band) ";
							          
		log.info("获取同邻频数据。sql="+sql);
		
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	
	/**
	 * 获取爱立信小区邻区数据检查
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-29下午06:20:45
	 */
	public List<Map<String, Object>> getEriCellNcellDataCheckResult(
			String bscIdStr, String date, long cityId,
			Map<String, String> settings) {
		log.info("进入方法：getEriCellNcellDataCheckResult。 cityId=" + cityId 
				+ ",bscIdStr="+ bscIdStr + ",date=" + date + ",settings="+settings);

		final String sql = "";
		log.info("邻区数据检查。sql="+sql);
		
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	
	/**
	 * 获取爱立信小区测量频点的active，idle以及邻区组成的bcch结果集
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author peng.jm
	 * @date 2014-10-28下午06:06:07
	 */
	public List<Map<String, Object>> getEriCellMeaFreqResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings) {
		log.info("进入方法：getEriCellMeaFreqResult。 cityId=" + cityId 
				+ ",bscIdStr="+ bscIdStr + ",date=" + date + ",settings="+settings);

		final String sql = " select t1.bsc, t1.cell, t1.active,t1.idle,t2.ncell_bcch " +
			"    from (select bsc.engname as bsc, cell, active_32 as active, idle_32 as idle " +
			"            from rno_2g_eri_cell cell " +
			"            left join rno_bsc bsc on bsc.bsc_id = cell.bsc " +
			"           where cell.city_id = " + cityId +
			"             and cell.mea_date = to_date('"+date+"', 'yyyy-MM-dd') " +
			"             and cell.bsc in ("+bscIdStr+") " +
			"          ) t1 " +
			"    left join (select cell, wm_concat(distinct(bcch)) as ncell_bcch " +
			"                 from (select ncell.cell, ncell.n_cell, cell.bcchno as bcch " +
			"                         from (select cell, n_cell " +
			"                                 from rno_2g_eri_ncell_param " +
			"                                where city_id = " + cityId +
			"                                  and mea_date = to_date('"+date+"', 'yyyy-MM-dd') " +
			"                                  and bsc in ("+bscIdStr+") ) ncell " +
			"                         left join (select cell as ncell, bcchno " +
			"                                     from rno_2g_eri_cell " +
			"                                    where city_id =  " + cityId +
			"                                      and mea_date = to_date('"+date+"', 'yyyy-MM-dd') " +
			"                                      and bsc in ("+bscIdStr+") ) cell  " +
			"                        on (ncell.n_cell = cell.ncell)) group by cell " +
			"            ) t2  " +
			"      on t1.cell = t2.cell";
		log.info("获取测量频点的结果集。sql="+sql);
		
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 
	 * @title 获取爱立信小区BaNum的结果集
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-10-29上午10:39:57
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getEriCellBaNumCheckResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings) {
		log.info("进入方法：getEriCellBaNumCheckResult。 cityId=" + cityId 
				+ ",bscIdStr="+ bscIdStr + ",date=" + date + ",settings="+settings);
		final String sql ="select bsc.engname as bsc,						 "		
							+"       cell.cell,                                              "
							+"       length(cell.active_32 || ',') -                         "
							+"       nvl(length(REPLACE(cell.active_32, ',')), 0) AS active, "
							+"       length(cell.idle_32 || ',') -                           "
							+"       nvl(length(REPLACE(cell.idle_32, ',')), 0) AS idle      "
							+"  from rno_2g_eri_cell cell                                    "
							+"  left join rno_bsc bsc                                        "
							+"    on bsc.bsc_id = cell.bsc                                   "
							+" where cell.city_id = "+cityId+"                                       "
							+"   and cell.mea_date = to_date('"+date+"', 'yyyy-MM-dd')     "
							+"   and cell.bsc in ("+bscIdStr+")                                       ";
		log.info("获取Ba数量的结果集。sql="+sql);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 
	 * @title 获取爱立信小区TALIM_MAXTA检查结果集
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-10-29下午1:55:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getEriCellTalimAndMaxtaCheckResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings) {
		log.info("进入方法：getEriCellTalimAndMaxtaCheckResult。 cityId=" + cityId 
				+ ",bscIdStr="+ bscIdStr + ",date=" + date + ",settings="+settings);
		final String sql ="select cell.mea_date create_time,							 "
				+"       bsc.engname   as bsc,                               "
				+"       cell.cell,                                          "
				+"       cell.maxta,                                         "
				+"       cell.talim                                          "
				+"  from rno_2g_eri_cell cell                                "
				+"  left join rno_bsc bsc                                    "
				+"    on bsc.bsc_id = cell.bsc                               "
				+" where cell.city_id = "+cityId+"                                   "
				+"   and cell.mea_date = to_date('"+date+"', 'yyyy-MM-dd') "
				+"   and cell.bsc in ("+bscIdStr+")                                   "
				+"   and cell.maxta < cell.talim                            ";
		log.info("获取TALIM_MAXTA数量的结果集。sql="+sql);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 
	 * @title 获取爱立信小区同频同bsic检查结果集
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-10-29下午1:55:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getEriCellCoBsicCheckResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings) {
		log.info("进入方法：getEriCellCoBsicCheckResult。 cityId=" + cityId 
				+ ",bscIdStr="+ bscIdStr + ",date=" + date + ",settings="+settings);
		/*final String sql ="SELECT bsc.engname||'-'||cell cell, BSIC, BCCH												 "
				+"  FROM (select distinct c1.bsc,c1.cell, c1.ncc || c1.bcc BSIC, C1.bcchno BCCH "
				+"          from rno_2g_eri_cell c1, rno_2g_eri_cell c2                  "
				+"         where C1.cell <> C2.cell                                      "
				+"           and c1.bcchno = c2.bcchno                                   "
				+"           and c1.ncc || c1.bcc = c2.ncc || c2.bcc                     "
				+"           and C1.CITY_ID = "+cityId+"                                         "
				+"           and C2.CITY_ID = "+cityId+"                                         "
				+"           and c1.mea_date = to_date('"+date+"', 'yyyy-MM-dd')       "
				+"           and c2.mea_date = to_date('"+date+"', 'yyyy-MM-dd')       "
				+"           and c1.bsc in ("+bscIdStr+")                                         "
				+"           and c2.bsc in ("+bscIdStr+")                                         "
				+"         ORDER BY c1.bcchno, c1.ncc || c1.bcc)  t  left join rno_bsc bsc on bsc.bsc_id = t.bsc";*/
		final String sql="select incell.bsc || '-' || incell.cell || '-' || decode(c.name,null,'空',c.name)|| '-' || decode(c.LONGITUDE,null,'空',c.LONGITUDE)|| ',' || decode(c.latitude,null,'空',c.latitude) cell,			   "
				+"       incell.bsic,                                                      "
				+"       incell.bcch                                                       "
				+"  from (SELECT bsc.engname bsc, cell, BSIC, BCCH                         "
				+"          FROM (select distinct c1.bsc,                                  "
				+"                                c1.cell,                                 "
				+"                                c1.ncc || c1.bcc BSIC,                   "
				+"                                C1.bcchno BCCH                           "
				+"                  from rno_2g_eri_cell c1, rno_2g_eri_cell c2            "
				+"                 where C1.cell <> C2.cell                                "
				+"                   and c1.bcchno = c2.bcchno                             "
				+"                   and c1.ncc || c1.bcc = c2.ncc || c2.bcc               "
				+"                   and C1.CITY_ID = "+cityId+"                                   "
				+"                   and C2.CITY_ID = "+cityId+"                                   "
				+"                   and c1.mea_date = to_date('"+date+"', 'yyyy-MM-dd') "
				+"                   and c2.mea_date = to_date('"+date+"', 'yyyy-MM-dd') "
				+"                   and c1.bsc in ("+bscIdStr+")     "
				+"                   and c2.bsc in ("+bscIdStr+")     "
				+"                                                                         "
				+"                 ORDER BY c1.bcchno, c1.ncc || c1.bcc) t                 "
				+"          left join rno_bsc bsc                                          "
				+"            on bsc.bsc_id = t.bsc) incell                                "
				+"  left join cell c                                                       "
				+"    on c.label = incell.cell                                             ";
		log.info("获取co_bsic数量的结果集。sql="+sql);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 
	 * @title 通过源与目标小区得到其经纬度数据113.4087,23.07341,113.27481,23.04926
	 * @param sourcecell
	 * @param targetcell
	 * @return
	 * @author chao.xj
	 * @date 2014-10-29下午3:10:09
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<String> getLnglatsBySourceCellAndTargetCell(
			final String sourcecell, final String targetcell) {
//		log.info("进入getLnglatsBySourceCellAndTargetCell(final String sourcecell, final String targetcell)"+sourcecell+":"+targetcell);
		List<String> lnglats = (List<String>) hibernateTemplate
				.executeFind(new HibernateCallback<List<String>>() {
					public List<String> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						// TODO Auto-generated method stub
						List<String> lnglats = new ArrayList<String>();
						String sql = "SELECT wmsys.wm_concat(LONGITUDE||','||LATITUDE) as  LNGLATS from CELL WHERE LABEL ='"
								+ sourcecell
								+ "' or LABEL='"
								+ targetcell
								+ "'";
//						log.debug("getLnglatsBySourceCellAndTargetCell sql:"+sql);
						SQLQuery query = arg0.createSQLQuery(sql);
						List<Object> objects = query.list();
						if (objects != null && objects.size() != 0) {
							for (Object o : objects) {
								if(o!=null){
									lnglats.add(o.toString());
								}
							}
						}
						return lnglats;
					}

				});
//		log.info("退出getLnglatsBySourceCellAndTargetCell");
		return lnglats;
	}
	/**
	 * 
	 * @title 获取爱立信小区邻区过多过少检查结果集
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-10-29下午1:55:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getEriCellNcellNumCheckResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings) {
		log.info("进入方法：getEriCellNcellNumCheckResult。 cityId=" + cityId 
				+ ",bscIdStr="+ bscIdStr + ",date=" + date + ",settings="+settings);
		String isCheckNcellNum = settings.get("isCheckNcellNum") != null ? settings
				.get("isCheckNcellNum").toString() : "";
		int maxNum=32;//默认
		int minNum=2;
		if (("true").equals(isCheckNcellNum)) {
			maxNum = Integer.parseInt(settings.get("NCELL_MAXNUM"));
			minNum = Integer.parseInt(settings.get("NCELL_MINNUM"));
		} 
		final String sql ="select * from (select bsc.engname bsc,										"
				+"       cell.cell,                                                         "
				+"       count(cell.cell) n_cell_num,                                       "
				+"       wm_concat(cell.n_cell) n_cells                                     "
				+"  from rno_2g_eri_ncell_param cell                                        "
				+"  left join rno_bsc bsc                                                   "
				+"    on bsc.bsc_id = cell.bsc                                              "
				+" where cell.city_id = "+cityId+"                                                  "
				+"   and cell.mea_date = to_date('"+date+"', 'yyyy-MM-dd')                "
				+"   and cell.bsc in ("+bscIdStr+")                                                  "
				+" group by bsc.engname, cell.cell) where  n_cell_num<"+minNum+" or n_cell_num>"+maxNum+"    ";
		log.info("获取NcellNum数量的结果集。sql="+sql);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	/**
	 * 
	 * @title 获取爱立信小区本站邻区漏定义检查结果集
	 * @param bscIdStr
	 * @param date
	 * @param cityId
	 * @param settings
	 * @return
	 * @author chao.xj
	 * @date 2014-10-30下午14:55:51
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<Map<String, Object>> getEriCellNcellMomitCheckResult(
			String bscIdStr, String date, long cityId, Map<String, String> settings) {
		log.info("进入方法：getEriCellNcellMomitCheckResult。 cityId=" + cityId 
				+ ",bscIdStr="+ bscIdStr + ",date=" + date + ",settings="+settings);
		final String sql ="select bsc,																		"
				+"       cell,                                                                      "
				+"       cellr,                                                                     "
				+"       cellr_bsc,                                                                 "
				+"       cs,                                                                        "
				+"       'RLNRC:CELL=' || cell || ',CELLR=' || cellr || ',CS=YES;' mml              "
				+"  from (select substr(cell.cell, 0, 6) site,                  "
				+"               bsc.engname bsc,                                                   "
				+"               cell.n_cell cell,                                                  "
				+"               cell.cell cellr,                                                   "
				+"               cell.n_bsc cellr_bsc,                                              "
				+"               cell.cs                                                            "
				+"          from rno_2g_eri_ncell_param cell                                        "
				+"          left join rno_bsc bsc                                                   "
				+"            on bsc.bsc_id = cell.bsc                                              "
				+"         where cell.city_id = "+cityId+"                                                  "
				+"           and cell.mea_date = to_date('"+date+"', 'yyyy-MM-dd')                "
				+"           and cell.bsc in ("+bscIdStr+")                                                  "
				+"           and substr(cell.cell, 0, 6) = substr(cell.n_cell, 0, 6)                "
				+"         order by substr(cell.cell, 0, 6), cell.n_cell) A                         "
				+" where not exists                                                                 "
				+" (select *                                                                        "
				+"          from (select substr(cell.cell, 0, 6) site,          "
				+"                       bsc.engname bsc,                                           "
				+"                       cell.cell,                                                 "
				+"                       cell.n_cell cellr,                                         "
				+"                       cell.n_bsc cellr_bsc,                                      "
				+"                       cell.cs                                                    "
				+"                  from rno_2g_eri_ncell_param cell                                "
				+"                  left join rno_bsc bsc                                           "
				+"                    on bsc.bsc_id = cell.bsc                                      "
				+"                 where cell.city_id = "+cityId+"                                          "
				+"                   and cell.mea_date = to_date('"+date+"', 'yyyy-MM-dd')        "
				+"                   and cell.bsc in ("+bscIdStr+")                                          "
				+"                   and substr(cell.cell, 0, 6) = substr(cell.n_cell, 0, 6)        "
				+"                 order by substr(cell.cell, 0, 6), cell.cell) B                   "
				+"         where A.site = B.site                                                    "
				+"           and A.bsc = B.bsc                                                      "
				+"           and A.cell = B.cell                                                    "
				+"           and A.cellr = B.cellr                                                  "
				+"           and A.cellr_bsc = B.cellr_bsc                                          "
				+"           and A.cs = B.cs)                                                       ";
		log.info("获取NcellMomit的结果集。sql="+sql);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}

	/**
	 * 获取爱立信小区参数对比结果数据
	 * @param cityId
	 * @param paramType
	 * @param paramStr
	 * @param bscStr
	 * @param date1
	 * @param date2
	 * @author peng.jm
	 * @date 2014-11-6上午11:41:36
	 */
	public List<Map<String, Object>> exportEriCellCompareData(long cityId,
			String paramType, String paramStr, String bscStr, String date1,
			String date2) {
		String s = buildExportCompareDataSql(cityId,paramType,paramStr,bscStr,date1,date2);
		if(("").equals(s)) {
			return Collections.emptyList();
		}
		final String sql = s;
		log.info("exportEriCellCompareData。sql="+sql);
		return hibernateTemplate.executeFind(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session arg0) throws HibernateException,
					SQLException {
				SQLQuery query = arg0.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				return query.list();
			}
		});
	}
	
	private String buildExportCompareDataSql(long cityId, String paramType,
			String paramStr, String bscStr, String date1, String date2) {
		String resultSql = "";
		
		String paramsTot = "";
		String params1 = "";
		String params2 = "";
		String whereSql = "";
		String params[] = paramStr.split(",");
		if(params.length == 0) {
			log.error("组成sql所需参数为空！");
			return "";
		}
		for (String param : params) {
			
			paramsTot += " "+param+"_d1,"+param+"_d2,";
			whereSql +=  " "+param+"_d1<>"+param+"_d2 or";
			
			//TO改为“TO”
			if(("TO").equals(param)) {
				params1 += " \"TO\"" + " as TO_d1,";
				params2 += " \"TO\"" + " as TO_d2,";
			} 
			//BCCH改为BCCHNO
			else if(("BCCH").equals(param)) {
				params1 += " BCCHNO as BCCH_d1" + ",";
				params2 += " BCCHNO as BCCH_d2" + ",";
			} 
			//BSIC改为NCC||BCC as BSIC
			else if(("BSIC").equals(param)) {
				params1 += " NCC||BCC as BSIC_d1" + ",";
				params2 += " NCC||BCC as BSIC_d2" + ",";
			}
			//ACC改为ACC_16
			else if(("ACC").equals(param)) {
				params1 += " ACC_16 as ACC_d1" + ",";
				params2 += " ACC_16 as ACC_d2" + ",";
			}
			//NCCPERM改为NCCPERM_8
			else if(("NCCPERM").equals(param)) {
				params1 += " NCCPERM_8 as NCCPERM_d1" + ",";
				params2 += " NCCPERM_8 as NCCPERM_d2" + ",";
			}
			//ACTIVEMBCCHNO改为ACTIVE_32
			else if(("ACTIVEMBCCHNO").equals(param)) {
				params1 += " ACTIVE_32 as ACTIVEMBCCHNO_d1" + ",";
				params2 += " ACTIVE_32 as ACTIVEMBCCHNO_d2" + ",";
			}
			//IDLEMBCCHNO改为IDLE_32
			else if(("IDLEMBCCHNO").equals(param)) {
				params1 += " IDLE_32 as IDLEMBCCHNO_d1" + ",";
				params2 += " IDLE_32 as IDLEMBCCHNO_d2" + ",";
			}
			/**Channel**/
			//MAIO改为MAIO_16
			else if(("MAIO").equals(param)) {
				params1 += " MAIO_16 as MAIO_d1" + ",";
				params2 += " MAIO_16 as MAIO_d2" + ",";
			}
			//ETCHTN改为ETCHTN_8
			else if(("ETCHTN").equals(param)) {
				params1 += " ETCHTN_8 as ETCHTN_d1" + ",";
				params2 += " ETCHTN_8 as ETCHTN_d2" + ",";
			}
			else {
				params1 += " "+param+" as "+param+"_d1,";
				params2 += " "+param+" as "+param+"_d2,";
			}
		}

		params1 = params1.substring(0, params1.length() - 1);
		params2 = params2.substring(0, params2.length() - 1);
		paramsTot = paramsTot.substring(0, paramsTot.length() - 1);
		whereSql = whereSql.substring(0, whereSql.length() - 2); //去掉or
		
		
		if(("cell").equals(paramType)) {
			resultSql += "select bsc.engname as bsc,t.cell," +paramsTot+
				"   from (select t1.bsc,t1.cell,"+paramsTot+
				"       from (select cell.bsc, cell.cell,"+params1+
				"	           from rno_2g_eri_cell cell,"+
				"	                rno_2g_eri_cell_extra_info extra"+
				"	         where cell.cell = extra.cell "+
				"	           and cell.mea_date = extra.mea_date "+
				"	           and cell.city_id = extra.city_id"+
				"	           and cell.bsc = extra.bsc"+
				"	           and cell.mea_date = to_date('"+date1+"', 'yyyy-MM-dd')"+
				"	           and cell.city_id ="+cityId+
				"	           and cell.bsc in("+bscStr+")) t1 "+
				"	     left join (select cell.bsc, cell.cell,"+params2+
				"	           from rno_2g_eri_cell cell,"+
				"	                rno_2g_eri_cell_extra_info extra"+
				"	         where cell.cell = extra.cell "+
				"	           and cell.mea_date = extra.mea_date "+
				"	           and cell.city_id = extra.city_id"+
				"	           and cell.bsc = extra.bsc"+
				"	           and cell.mea_date = to_date('"+date2+"', 'yyyy-MM-dd')"+
				"	           and cell.city_id ="+cityId+
				"	           and cell.bsc in("+bscStr+")) t2 " +
				"	      on (t1.bsc = t2.bsc and t1.cell = t2.cell ) " +
				"	   where  ("+whereSql+")) t " +
				" left join rno_bsc bsc on bsc.bsc_id = t.bsc ";
		} 
		else if(("channel").equals(paramType)) {
			resultSql += "select bsc.engname as bsc,t.cell,t.ch_group," +paramsTot+
				"   from (select t1.bsc,t1.cell,t1.ch_group,"+paramsTot+
				"       from (select chg.bsc, chg.cell, chg.ch_group,"+params1+
				"	           from rno_2g_eri_cell_ch_group chg "+
				"	         where chg.mea_date = to_date('"+date1+"', 'yyyy-MM-dd')"+
				"	           and chg.city_id ="+cityId+
				"	           and chg.bsc in("+bscStr+")) t1 "+
				"	     left join (select chg.bsc, chg.cell, chg.ch_group,"+params2+
				"	           from  rno_2g_eri_cell_ch_group chg "+
				"	         where chg.mea_date = to_date('"+date2+"', 'yyyy-MM-dd')"+
				"	           and chg.city_id ="+cityId+
				"	           and chg.bsc in("+bscStr+")) t2 " +
				"	      on (t1.bsc = t2.bsc and t1.cell = t2.cell and t1.ch_group = t2.ch_group) " +
				"	   where  ("+whereSql+")) t " +
				" left join rno_bsc bsc on bsc.bsc_id = t.bsc ";
		} 
		else if(("neighbour").equals(paramType)) {
			resultSql += "select bsc.engname as bsc,t.cell,t.n_cell," +paramsTot+
				"   from (select t1.bsc,t1.cell,t1.n_cell,"+paramsTot+
				"       from (select nc.bsc, nc.cell, nc.n_cell,"+params1+
				"	           from rno_2g_eri_ncell_param nc "+
				"	         where nc.mea_date = to_date('"+date1+"', 'yyyy-MM-dd')"+
				"	           and nc.city_id ="+cityId+
				"	           and nc.bsc in("+bscStr+")) t1 "+
				"	     left join (select nc.bsc, nc.cell, nc.n_cell,"+params2+
				"	           from  rno_2g_eri_ncell_param nc "+
				"	         where nc.mea_date = to_date('"+date2+"', 'yyyy-MM-dd')"+
				"	           and nc.city_id ="+cityId+
				"	           and nc.bsc in("+bscStr+")) t2 " +
				"	      on (t1.cell = t2.cell and t1.n_cell = t2.n_cell ) " +
				"	   where  ("+whereSql+")) t " +
				" left join rno_bsc bsc on bsc.bsc_id = t.bsc ";
		}
		return resultSql;
	}
	
	
}
