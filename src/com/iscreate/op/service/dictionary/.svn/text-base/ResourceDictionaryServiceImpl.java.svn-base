package com.iscreate.op.service.dictionary;



import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;


public class ResourceDictionaryServiceImpl implements ResourceDictionaryService {
	private ContextFactory contextFactory;
	
	private static final Log log = LogFactory.getLog(ResourceDictionaryServiceImpl.class);
	
	public ContextFactory getContextFactory() {
		return contextFactory;
	}



	public void setContextFactory(ContextFactory contextFactory) {
		this.contextFactory = contextFactory;
	}

	/**
	 * 
	 * @description: 获取资源字典
	 * @author：
	 * @return     
	 * @return List<BasicEntity>     
	 * @date：Jun 19, 2012 1:24:26 PM
	 */
	public List<BasicEntity> getResourceDictionary(
			String attributeName1, String attributeValue1,
			String attributeName2, String attributeValue2) {
		log.info("进入getResourceDictionary(String attributeName1, String attributeValue1,String attributeName2, String attributeValue2)，获取资源字典");
		log.info("进入getResourceDictionary,attributeName1="+attributeName1+",attributeValue1="+attributeValue1+",attributeName2="+attributeName2+",attributeValue2="+attributeValue2);
		List<BasicEntity> list = new ArrayList<BasicEntity>();
		Context ctx=contextFactory.CreateContext();
		//Query query = ctx.createQueryBuilder("DictionaryEntry");
		StringBuffer  conStr = new StringBuffer(); 
		if(attributeValue1!=null){
			//query.add(Restrictions.like(attributeName1,attributeValue1));
			if(!"".equals(conStr+"")){
				conStr.append(" and "+attributeName1+" like '" +attributeValue1+"'");
			}else{
				conStr.append(attributeName1+" like '" +attributeValue1+"'");
			}
			
		}
		if(attributeValue2!=null){
			//query.add(Restrictions.like(attributeName2,attributeValue2));
			if(!"".equals(conStr+"")){
				conStr.append(" and "+attributeName2+" like '" +attributeValue2+"'");
			}else{
				conStr.append(attributeName2+" like '" +attributeValue2+"'");
			}
			
		}

		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString("DictionaryEntry")+" from  DictionaryEntry where "+conStr;
		//System.out.println(sqlString);
		SqlContainer sc = ctx.createSqlContainer(sqlString);
		list = ctx.executeSelectSQL(sc,"DictionaryEntry");

		//list =ctx.queryList(query);
		log.info("退出getResourceDictionary(String attributeName1, String attributeValue1,String attributeName2, String attributeValue2)，返回结果list="+list);
		
		return list;
	}
	
	
	
	
	
}
