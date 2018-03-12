package com.iscreate.op.dao.rno;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.net.aso.l;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.op.action.rno.Page;
import com.iscreate.op.action.rno.StsCondition;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.pojo.rno.RnoReportTemplate;
import com.iscreate.op.pojo.rno.RnoRptTemplateDetail;
import com.iscreate.op.pojo.rno.RnoTableDict;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.sun.org.apache.xpath.internal.operations.And;

public class RnoReportDaoImpl implements RnoReportDao {
	private static Log log = LogFactory.getLog(RnoReportDaoImpl.class);
	// ---注入----//
	private HibernateTemplate hibernateTemplate;
	private RnoStsDao rnoStsDao;
	
	//单天求和和单天平均时的可统计字段和不可统计字段
	List<String> sumOrAvgAbleField = new ArrayList<String>();
	List<String> sumOrAvgDisableField = new ArrayList<String>();
	{
		sumOrAvgAbleField.add("DECLARE_CHANNEL_NUMBER");
		sumOrAvgAbleField.add("AVAILABLE_CHANNEL_NUMBER");
		sumOrAvgAbleField.add("CARRIER_NUMBER");
		sumOrAvgAbleField.add("TRAFFIC");
		sumOrAvgAbleField.add("DROP_CALL_NUM_TOGETHER");
		sumOrAvgAbleField.add("DATA_TRAFFIC");
		
		sumOrAvgDisableField.add("CELL_CHINESE_NAME");
		
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	public RnoStsDao getRnoStsDao() {
		return rnoStsDao;
	}

	public void setRnoStsDao(RnoStsDao rnoStsDao) {
		this.rnoStsDao = rnoStsDao;
	}
	/**
	 * 获取指定表的字典信息
	 * 
	 * @param tableName
	 * @return
	 * @author brightming 2014-1-2 下午2:53:24
	 */
	public List<RnoTableDict> getDictFieldsByTabName(String tableName) {
		log.info("进入dao方法：getDictFieldsByTabName。tableName=" + tableName);
		List res = hibernateTemplate.find(
				"from RnoTableDict where tableDbName=?", tableName);
		log.info("获取到的记录数：" + res == null ? 0 : res.size());
		return res;
	}

	/**
	 * 保存统计模板
	 * 
	 * @param rptTemplate
	 * @param rptDetails
	 * @return
	 * @author brightming 2014-1-3 上午11:43:45
	 */
	public boolean saveReportTemplate(final RnoReportTemplate rptTemplate,
			final List<RnoRptTemplateDetail> rptDetails) {
		log.info("进入dao方法：saveReportTemplate。rptTemplate=" + rptTemplate
				+ ",rptDetails=" + rptDetails);
		if (rptTemplate == null || rptDetails == null) {
			log.error("保存统计模板时传入的参数为空！");
			return false;
		}

		return hibernateTemplate.execute(new HibernateCallback<Boolean>() {

			
			public Boolean doInHibernate(Session session)
					throws HibernateException, SQLException {

				Transaction tx = session.beginTransaction();
				tx.begin();
				boolean ok = true;
				try {
					session.save(rptTemplate);
					long id=rptTemplate.getId();
//					System.out.println("id==="+id);
					for (RnoRptTemplateDetail det : rptDetails) {
						det.setTemplateId(id);
						session.save(det);
					}
				} catch (Exception e) {
					e.printStackTrace();
					ok = false;
				}
				if (ok) {
					tx.commit();
				} else {
					tx.rollback();
				}
				return ok;
			}

		});
	}
	
	/**
	 * 获取指定报表模板的报表信息
	 * @param tabAlias 表别名
	 * @param templateId
	 * @return
	 * @author brightming
	 * 2014-1-3 下午2:42:33
	 */
	public String getReportFieldsAsSqlByTemplateId(String tabAlias,long templateId){
		RnoReportTemplate template=hibernateTemplate.get(RnoReportTemplate.class,templateId);
		if(template==null){
			log.error("不存在id=["+templateId+"]的统计报表模板！");
			return "";
		}
		List<RnoRptTemplateDetail> details=hibernateTemplate.find("from RnoRptTemplateDetail where templateId=?",templateId);
		if(details==null || details.isEmpty()){
			return "";
		}
		if(tabAlias==null || "".equals(tabAlias.trim())){
			tabAlias=template.getTableName();
		}
		String buf=" ";
		for(RnoRptTemplateDetail rpd:details){
//			buf+=tabAlias+"."+rpd.getTableFields()+" as "+rpd.getDisplayName()+",";
			buf+=tabAlias+"."+rpd.getTableFields()+" ,";
		}
		if(buf.length()>2){
			buf=buf.substring(1,buf.length()-1);
		}
		log.info("退出getReportFieldsAsSqlByTemplateId获取的字段："+buf);
		return buf;
	}
	
	/**
	 * 根据模板的应用范围和值获取模板
	 * @param tabName 适应表名
	 * @param scope
	 * @param value
	 * @return
	 * @author brightming
	 * 2014-1-3 下午2:50:43
	 */
	public List<RnoReportTemplate> getReportTemplateByScopeAndValue(String tabName,String scope,String value){
		log.info("进入dao方法：getReportTemplateByScopeAndValue。tabName="+tabName+",scope="+scope+",value="+value);
		if(tabName==null || "".equals(tabName.trim()) || scope==null || "".equals(scope.trim())
				|| value==null || "".equals(value.trim())){
			log.error("方法getReportTemplateByScopeAndValue传入的参数为空！");
			return Collections.EMPTY_LIST;
		}
		String systemApplyScope = RnoConstant.ApplyScope.SYSTEM.getCode();
		List<RnoReportTemplate> tmps=hibernateTemplate.find("from RnoReportTemplate where tableName=? and applyScope=? and  scopeValue=?  and status='N' or applyScope='"+systemApplyScope+"' order by id asc", tabName,scope,value);
		return tmps;
	}
	

	
	/**
	 * 获取自定义统计模板指定的话统信息
	 * @param reportTemplateId
	 * @param page
	 * @param queryCondition
	 * @param isAudio
	 * @return
	 * @author brightming
	 * 2014-1-6 上午10:05:27
	 */
	public List<Map<String,Object>> queryStsByRptTemplateAndPage(final long reportTemplateId,final Page page, final StsCondition queryCondition,final boolean isAudio) {
		log.info("进入方法：queryStsByCellVideoOrDataByPage(final Page page, final StsCondition queryCondition).page=" + page + ",queryCondition=" + queryCondition);
		return hibernateTemplate
				.executeFind(new HibernateCallback<List<Map<String,Object>>>() {
					public List<Map<String,Object>> doInHibernate(Session arg0)
							throws HibernateException, SQLException {
						String sql = "";
						
						//单天求和、单天平均、单天最大值、单天最小值
						if("sum".equals(queryCondition.getStsType()) || "avg".equals(queryCondition.getStsType()) || "max".equals(queryCondition.getStsType()) || "min".equals(queryCondition.getStsType()))
						{
							sql = getStatisticSql(reportTemplateId,queryCondition,isAudio);
							log.info("sum/avg/max/min_sql: " + sql);
						}
						//不汇总
						else
						{
							String staticField="rsd.AREA_ID,rs.STS_ID,rsd.STS_DESC_ID,TO_CHAR(rsd.STS_DATE,'yyyy-MM-dd') STS_DATE,rsd.STS_PERIOD,rb.ENGNAME,rs.CELL";
							String fieldsStr=getReportFieldsAsSqlByTemplateId("rs",reportTemplateId);
							if(!"".equals(fieldsStr)){
								fieldsStr=staticField+","+fieldsStr;
							}
							log.info("获取报表字段field："+fieldsStr);
							//String sql = ((RnoStsDaoImpl)rnoStsDao).getQueryStsByCellVideoOrDataSql(field,queryCondition,"sts",isAudio);
							sql = rnoStsDao.getQueryStsByCellVideoOrDataSql(fieldsStr,queryCondition,"sts",isAudio);
						}
						SQLQuery query = arg0.createSQLQuery(sql);
						int start = (page.getPageSize()
								* (page.getCurrentPage() - 1) + 1);
						int end = (page.getPageSize() * page.getCurrentPage());
						query.setLong(0, start);
						query.setLong(1, end);
						//System.out.println(sql+":"+start+":"+end+query);
						query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
						List<Map<String,Object>> stsList = query.list();
						log.info("获取到结果集记录数量："
								+ (stsList == null ? 0 : stsList.size()));
						for (int i = 0; i < stsList.size(); i++) {
							Map map=stsList.get(i);
							//System.out.println(map.get("STS_PERIOD")+":"+map.get("CELL"));
						}
						return stsList;
					}
				});
	}
	/**
	 * 通过模板ID获取话统报表详情字段信息
	 * @param id
	 * @return
	 * @author chao.xj
	 * @date 2014-1-7下午04:40:23
	 */
	public List<Map<String,Object>> getStsRptTemplateFiledByTemplateId(final long id) {
		log.info("进入getStsRptTemplateFiledByTemplateId id:"+id);
		return hibernateTemplate.executeFind(new HibernateCallback<List<Map<String, Object>>>(){

			
			public List<Map<String, Object>> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				String sql="SELECT TABLE_FIELDS,DISPLAY_NAME,DISPLAY_ORDER from RNO_RPT_TEMPLATE_DETAIL WHERE TEMPLATE_ID=? order by DISPLAY_ORDER asc";
				SQLQuery query=arg0.createSQLQuery(sql);
				query.setLong(0, id);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				List<Map<String, Object>> relList=query.list();
				log.info("退出getStsRptTemplateFiledByTemplateId relist"+relList);
				return relList;
			}
		});
	}
	
	/**
	 * 
	 * @author Liang YJ
	 * @date 2014-1-21 下午7:07:59
	 * @param tableShortcut
	 * @param statisticType
	 * @return statisticField
	 * @description 获取统计字段
	 */
	public Map<String, String> getstatisticField(String tableName,String tableShortcut, String statisticType)
	{
		Map<String, String> statisticField = new HashMap<String, String>();
		List<RnoTableDict> rnoTableDictList = getDictFieldsByTabName(tableName);
		for(RnoTableDict rnoTableDict : rnoTableDictList)
		{
			//cell_chinese_name需要做特殊处理
			if("CELL_CHINESE_NAME".equalsIgnoreCase(rnoTableDict.getFieldDbName()))
			{
				String key = tableShortcut+"."+rnoTableDict.getFieldDbName();
				String value= "max("+tableShortcut+"."+rnoTableDict.getFieldDbName()+") "+rnoTableDict.getFieldDbName();
				statisticField.put(key, value);
			}
			String key = tableShortcut+"."+rnoTableDict.getFieldDbName();
			String value= statisticType+"("+tableShortcut+"."+rnoTableDict.getFieldDbName()+") "+rnoTableDict.getFieldDbName();
			statisticField.put(key, value);
		}
		return statisticField;
	}
	
	/**
	 * 
	 * @author Liang YJ
	 * @date 2014-1-27 上午11:57:07
	 * @param reportTemplateId
	 * @param queryCondition
	 * @param isAudio
	 * @return
	 * @description 根据统计类型和标题返回sql字段
	 */
	public String getStatisticSql(final long reportTemplateId, final StsCondition queryCondition,final boolean isAudio)
	{
		String fieldsStr = "";
		StringBuffer fields = new StringBuffer("rs.CELL CELL,TO_CHAR(rsd.STS_DATE,'yyyy-MM-dd') STS_DATE,max(rb.ENGNAME) ENGNAME, ");
		//List<RnoRptTemplateDetail> details=hibernateTemplate.find("from RnoRptTemplateDetail where templateId=? order by displayOrder asc",reportTemplateId);
		List<RnoRptTemplateDetail> details = queryRptTemplateDetailListByRptTempId(reportTemplateId);
		if("sum".equals(queryCondition.getStsType()) || "avg".equals(queryCondition.getStsType()))
		{
			
			for(RnoRptTemplateDetail detail :details)
			{
				if(sumOrAvgDisableField.contains((detail.getTableFields())))
				{
					fields.append("max(rs."+detail.getTableFields()+") "+detail.getTableFields()+",");
				}
				else if(sumOrAvgAbleField.contains(detail.getTableFields()))
				{
					fields.append(queryCondition.getStsType()+"(rs."+detail.getTableFields()+") "+detail.getTableFields()+",");
				}
			}
		}
		if("max".equals(queryCondition.getStsType()) || "min".equals(queryCondition.getStsType()))
		{
			for(RnoRptTemplateDetail detail :details)
			{
				fields.append(queryCondition.getStsType()+"(rs."+detail.getTableFields()+") "+detail.getTableFields()+",");
			}
		}
		fields.append("count(*) CNT ");
		fieldsStr = fields.toString();
		log.info("获取报表字段field："+fieldsStr);
		return  rnoStsDao.getQueryStsByCellVideoOrDataSql(fieldsStr,queryCondition,queryCondition.getStsType(),isAudio);
	}
	
	
	
	/**
	 * 
	 * @author Liang YJ
	 * @date2014-1-20 下午2:20:21
	 * @param queryCondition
	 * @param isAudio
	 * @param rptTemplateId
	 * @return statisticList;
	 * @description 根据查询条件返回统计结果
	 */
	public List<Map<String,Object>> getRnoStsResultList(StsCondition queryCondition,boolean isAudio,long rptTemplateId,List<String> statisticFieldList, List<String> statisticTitleList)
	{
		String fieldsStr = "";
		StringBuffer fields = new StringBuffer("rs.CELL CELL,TO_CHAR(rsd.STS_DATE,'yyyy-MM-dd') STS_DATE,max(rb.ENGNAME) ENGNAME, ");
		statisticFieldList.add("STS_DATE");
		statisticTitleList.add("DATE");
		statisticFieldList.add("ENGNAME");
		statisticTitleList.add("BSC");
		statisticFieldList.add("CELL");
		statisticTitleList.add("CELL");
		List<Map<String,Object>> rnoStsList = null;
		List<RnoRptTemplateDetail> details = queryRptTemplateDetailListByRptTempId(rptTemplateId);
		if("sum".equals(queryCondition.getStsType()) || "avg".equals(queryCondition.getStsType()))
		{
			
			for(RnoRptTemplateDetail detail :details)
			{
				if(sumOrAvgDisableField.contains((detail.getTableFields())))
				{
					fields.append("max(rs."+detail.getTableFields()+") "+detail.getTableFields()+",");
					statisticFieldList.add(detail.getTableFields());
					statisticTitleList.add(detail.getDisplayName());
					
				}
				else if(sumOrAvgAbleField.contains(detail.getTableFields()))
				{
					fields.append(queryCondition.getStsType()+"(rs."+detail.getTableFields()+") "+detail.getTableFields()+",");
					statisticFieldList.add(detail.getTableFields());
					statisticTitleList.add(detail.getDisplayName());
				}
			}
		}
		else if("max".equals(queryCondition.getStsType()) || "min".equals(queryCondition.getStsType()))
		{
			for(RnoRptTemplateDetail detail :details)
			{
				fields.append(queryCondition.getStsType()+"(rs."+detail.getTableFields()+") "+detail.getTableFields()+",");
				statisticFieldList.add(detail.getTableFields());
				statisticTitleList.add(detail.getDisplayName());
			}
		}
		fields.append("count(*) CNT ");
		statisticFieldList.add("CNT");
		statisticTitleList.add("记录数");
		fieldsStr = fields.toString();
		log.info("获取报表字段field："+fieldsStr);
		log.info("statisticTitleList: " + statisticTitleList);
		log.info("statisticFieldList: " + statisticFieldList);
		rnoStsList = rnoStsDao.queryStsByVideoOrDataCondition(fieldsStr,queryCondition,isAudio);
		log.info("rnoStsList2: "+ rnoStsList.toString());
		return rnoStsList;	
	}
	
	
	
	/**
	 * 
	 * @title 查询报表模板总量
	 * @param reportName
	 * @param displayName
	 * @return
	 * @author chao.xj
	 * @date 2014-1-23下午06:53:55
	 * @company 怡创科技
	 * @version 1.2
	 */
	public int getRnoRptTemplateCount(String reportName,String displayName) {
		log.info("进入getRnoRptTemplateCount(String reportName,"+reportName+",String displayName)"+displayName);
		String where="";
		if ("".equals(reportName) && !"".equals(displayName)) {
			where=" WHERE id IN (SELECT TEMPLATE_ID from RNO_RPT_TEMPLATE_DETAIL WHERE DISPLAY_NAME LIKE '%"+displayName+"%')";//--报表名为空
		}else if ("".equals(displayName)&&!"".equals(reportName)) {
			where=" WHERE REPORT_NAME LIKE '%"+reportName+"%'";//--字段为空
		}else if (!"".equals(displayName)&&!"".equals(reportName)) {
			where=" WHERE id IN (SELECT TEMPLATE_ID from RNO_RPT_TEMPLATE_DETAIL WHERE DISPLAY_NAME LIKE '%"+displayName+"%') AND   REPORT_NAME LIKE '%"+reportName+"%'";//--均不为空
		}
		final String sql="SELECT count(*) count from RNO_REPORT_TEMPLATE "+where;
		return hibernateTemplate.execute(new HibernateCallback<Integer>(){
			@Override
			public Integer doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				SQLQuery query=arg0.createSQLQuery(sql);
				List cntList=query.list();
				int count=0;
				if (cntList!=null && cntList.size()!=0) {
					count=Integer.parseInt(cntList.get(0).toString());
				}
				log.info("退出getRnoRptTemplateCount count:"+count);
				return count;
			}
		});
	}
	/**
	 * 
	 * @title 获取报表查询sql语句
	 * @param reportName
	 * @param displayName
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2014-1-24上午09:33:18
	 * @company 怡创科技
	 * @version 1.2
	 */
	public String createRptTemplateSql(String reportName,String displayName,long startIndex, long recordNum) {
		log.info("进入createRptTemplateSql(String reportName："+reportName+",String displayName:"+displayName+",long startIndex:"+startIndex+", long recordNum)"+recordNum);
		String where="";
		if ("".equals(reportName) && !"".equals(displayName)) {
			where=" WHERE id IN (SELECT TEMPLATE_ID from RNO_RPT_TEMPLATE_DETAIL WHERE DISPLAY_NAME LIKE '%"+displayName+"%')";//--报表名为空
		}else if ("".equals(displayName)&&!"".equals(reportName)) {
			where=" WHERE REPORT_NAME LIKE '%"+reportName+"%'";//--字段为空
		}else if (!"".equals(displayName)&&!"".equals(reportName)) {
			where=" WHERE id IN (SELECT TEMPLATE_ID from RNO_RPT_TEMPLATE_DETAIL WHERE DISPLAY_NAME LIKE '%"+displayName+"%') AND   REPORT_NAME LIKE '%"+reportName+"%'";//--均不为空
		}
		final String tempTab="SELECT * from RNO_REPORT_TEMPLATE "+where;
		final String sql = "select * "
			 +"from (select * "
	         +" from (select t.*, row_number() OVER(ORDER BY null) AS \"row_number\" "
	         +" from ("+tempTab+") t) p "
	         +" where p.\"row_number\" > "+startIndex+") q "
	         +" where rownum <= "+recordNum;
		log.info("退出createRptTemplateSql sql:"+sql);
		return sql;
	}
	/**
	 * 
	 * @title 通过分页方式查询报表模板列表
	 * @param reportName
	 * @param displayName
	 * @param startIndex
	 * @param cnt
	 * @return
	 * @author chao.xj
	 * @date 2014-1-24上午09:35:23
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RnoReportTemplate> queryReportTemplateListByPage(String reportName,String displayName,long startIndex, long recordNum) {
		
		log.info("进入queryReportTemplateListByPage(String reportName:"+reportName+",String displayName:"+displayName+",long startIndex:"+startIndex+", long recordNum)"+recordNum);
		final String sql=this.createRptTemplateSql(reportName, displayName, startIndex, recordNum);
		
		return hibernateTemplate.executeFind(new HibernateCallback<List<RnoReportTemplate>>(){
			@Override
			public List<RnoReportTemplate> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				SQLQuery query=arg0.createSQLQuery(sql);
				query.addEntity(RnoReportTemplate.class);
				List<RnoReportTemplate> rnorpttemplate=query.list();
				log.info("退出queryReportTemplateListByPage rnorpttemplate:"+rnorpttemplate);
				return rnorpttemplate;
			}
		});
	}
	/**
	 * 
	 * @title 通过报表模板ID查看字段详情
	 * @param rptTemplateId
	 * @return
	 * @author chao.xj
	 * @date 2014-1-24下午12:02:57
	 * @company 怡创科技
	 * @version 1.2
	 */
	public List<RnoRptTemplateDetail> queryRptTemplateDetailListByRptTempId(long rptTemplateId) {
		
		log.info("进入queryRptTemplateDetailList(long rptTemplateId:"+rptTemplateId);
		final String sql="SELECT * from RNO_RPT_TEMPLATE_DETAIL WHERE template_id="+rptTemplateId + " order by display_order asc";
		
		return hibernateTemplate.executeFind(new HibernateCallback<List<RnoRptTemplateDetail>>(){
			@Override
			public List<RnoRptTemplateDetail> doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				SQLQuery query=arg0.createSQLQuery(sql);
				query.addEntity(RnoRptTemplateDetail.class);
				List<RnoRptTemplateDetail> rnorpttemplate=query.list();
				log.info("退出queryRptTemplateDetailList rnorpttemplate:"+rnorpttemplate);
				return rnorpttemplate;
			}
		});
	}
	/**
	 * 
	 * @title 根据报表模板ID删除报表模板及其详情信息
	 * @param rptTemplateId
	 * @return
	 * @author chao.xj
	 * @date 2014-1-24下午02:36:30
	 * @company 怡创科技
	 * @version 1.2
	 */
	public boolean rmvRptTemplateAndDetailListByRptTempId(long rptTemplateId) {
		log.info("进入rmvRptTemplateAndDetailListByRptTempId(long rptTemplateId)"+rptTemplateId);
		boolean flag=false;
		List detailList=hibernateTemplate.find("from RnoRptTemplateDetail WHERE template_id="+rptTemplateId);
		try {
			if (detailList.size()>0) {
				hibernateTemplate.deleteAll(detailList);
				flag=true;
				 log.info("RnoRptTemplateDetail delete successful"+rptTemplateId);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("RnoRptTemplateDetail delete failed", e);
			flag=false;
			e.printStackTrace();
		}
		try {
			RnoReportTemplate rnoReportTemplate=hibernateTemplate.load(RnoReportTemplate.class, rptTemplateId);
			hibernateTemplate.delete(rnoReportTemplate);
			flag=true;
			log.info("rnoReportTemplate delete successful"+rptTemplateId);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("rnoReportTemplate delete failed", e);
			flag=false;
			e.printStackTrace();
		}
		log.info("退出rmvRptTemplateAndDetailListByRptTempId flag:"+flag);
		return flag;
	}
}
