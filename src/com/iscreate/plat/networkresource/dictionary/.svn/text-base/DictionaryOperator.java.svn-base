package com.iscreate.plat.networkresource.dictionary;
import java.util.List;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;

/**
 * 
 * @filename: DictionaryOperator.java
 * @classpath: com.iscreate.dictionary
 * @description: 字典相关数据库操作
 * @author：
 * @date：Mar 23, 2013 1:44:46 PM
 * @version：
 */
class DictionaryOperator {
	private ContextFactory contextFactory;

	public void setContextFactory(ContextFactory contextFactory) {
		this.contextFactory = contextFactory;
	}
	/**
	 * 
	 * @description: 根据dn值 获取相关字典dictionaryEntry记录
	 * @author：
	 * @param dn
	 * @return     
	 * @return BasicEntity     
	 * @date：Mar 23, 2013 1:45:26 PM
	 */
	public BasicEntity getEntryByDn(String dn) {
		Context c = contextFactory.CreateContext();
		//Query q = c.createQueryBuilder(Entry.ENTRYMODULE_NAME);
		/*q.add(com.iscreate.dataservice.query.restrictions.Restrictions.eq(
				Entry.DN_KEY, dn));*/
		String sqlString = "select "+ResourceCommon.getSelectSqlAttributsString(Entry.ENTRYMODULE_NAME)+" from "+Entry.ENTRYMODULE_NAME+" where "+Entry.DN_KEY+"='"+dn+"'";
		//List<BasicEntity> list = c.queryList(q);
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = c.createSqlContainer(sqlString);
		List<BasicEntity> list = c.executeSelectSQL(sc, Entry.ENTRYMODULE_NAME);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	/**
	 * 
	 * @description: 根据dn值 更新相关字典dictionaryEntry记录
	 * @author：
	 * @param dn
	 * @return     
	 * @return BasicEntity     
	 * @date：Mar 23, 2013 1:45:26 PM
	 */
	public void updateEntryByDn(String dn, Entry entry) {
		Context context = contextFactory.CreateContext();
		//Query q = context.createQueryBuilder(Entry.ENTRYMODULE_NAME);
		//q.add(Restrictions.eq(Entry.DN_KEY, dn));
		String sqlString = "update "+Entry.ENTRYMODULE_NAME+" set "+ResourceCommon.getUpdateAttributesSqlString(entry)+" where "+Entry.DN_KEY+"='"+dn+"'";
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		context.executeUpdateSQL(sc, Entry.ENTRYMODULE_NAME);
		//context.update(entry, q);
	}

}
