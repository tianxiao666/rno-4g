package com.iscreate.plat.networkresource.engine.figure.execution;

import java.util.ArrayList;
import java.util.List;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figureline;

public class QueryFigurelineIdsByIdsExecution implements
		Execution<List<String>> {
	public final String PARENTRECURSION = "PARENTRECURSION";
	public final String CHILDRECURSION = "CHILDRECURSION";
	private List<String> ids;
	private String recursionType;
	private String aetType;
	private String figureId;
	public QueryFigurelineIdsByIdsExecution(List<String> ids,String recursionType,String aetType,String figureId) {
		this.recursionType=recursionType;
		this.aetType = aetType;
		this.ids = ids;
		this.figureId = figureId;
	}
	/**
	 * 通过实体类型，figureId，关联关系recursionType,图节点id，获取Figureline表 id记录集
	 * (mysql to oracle 迁移:dateService封装查询记录方法context.queryList(q)会发生SqlException，sql语句不标准，用context.executeSelectSQL方法替代）
	 */
	public List<String> doExecution(ContextFactory contextFactory) {
		String resultFlag = ""; 
		Context context = contextFactory.CreateContext();
		//Query query = context.createQueryBuilder(Figureline.MY_TYPE);
		StringBuffer conStr = new StringBuffer();
		//query.add(Restrictions.eq(Figureline.FIGUREID_KEY, this.figureId));
		conStr.append(Figureline.FIGUREID_KEY+"="+this.figureId);
		String[] idsArray =  new String[ids.size()];
		ids.toArray(idsArray);
		
		if(CHILDRECURSION.equals(this.recursionType)){
			//query.add(Restrictions.in(Figureline.LEFTID_KEY, idsArray));
			StringBuffer idStr = new StringBuffer();
			int i=0;
			String cdStr="";
			for(int index=0;index<ids.size();index++){
				idStr.append(","+ids.get(index));
				if(i>=50||index==ids.size()-1){
					if(!"".equals(cdStr)){
						cdStr += " or "+Figureline.LEFTID_KEY+" in  ("+idStr.substring(1)+")";
					}else{
						cdStr += "( "+Figureline.LEFTID_KEY+" in  ("+idStr.substring(1)+")";
					}
					idStr=new StringBuffer();
					i=0;
				}
				i++;
			}
			conStr.append(" and "+cdStr+") ");
			conStr.append(" and "+Figureline.RIGHTTYPE_KEY+"='"+this.aetType+"' ");
			//query.add(Restrictions.eq(Figureline.RIGHTTYPE_KEY,this.aetType));
			resultFlag = Figureline.RIGHTID_KEY;
		}else{
			//query.add(Restrictions.in(Figureline.RIGHTID_KEY, idsArray));
			//query.add(Restrictions.eq(Figureline.LEFTTYPE_KEY,this.aetType));
			StringBuffer idStr = new StringBuffer();
			int i=0;
			String cdStr="";
			for(int index=0;index<ids.size();index++){
				idStr.append(","+ids.get(index));
				if(i>=50||index==ids.size()-1){
					if(!"".equals(cdStr)){
						cdStr += " or "+Figureline.RIGHTID_KEY+" in  in("+idStr.substring(1)+")";
					}else{
						cdStr += "( "+Figureline.RIGHTID_KEY+" in  ("+idStr.substring(1)+")";
					}
					idStr=new StringBuffer();
					i=0;
				}
				i++;
			}
			conStr.append(" and "+cdStr+") ");
			conStr.append(" and "+Figureline.LEFTTYPE_KEY+"='"+this.aetType+"' ");
			resultFlag = Figureline.LEFTID_KEY;
		}
		String sql = "select "+ResourceCommon.getSelectSqlAttributsString(Figureline.MY_TYPE)+" from "+Figureline.MY_TYPE+" where "+conStr;
		//System.out.println("sqlString:"+sql);
		SqlContainer sqlContainer = context.createSqlContainer(sql);
		List<BasicEntity> bes = context.executeSelectSQL(sqlContainer,
				Figureline.MY_TYPE);
		//List<BasicEntity> bes = context.queryList(query);
		List<String> aes = new ArrayList<String>();
		if (bes != null) {
			for (BasicEntity be : bes) {
				aes.add(be.getValue(resultFlag)+"");
			}
		}
		return aes;
	}

}
