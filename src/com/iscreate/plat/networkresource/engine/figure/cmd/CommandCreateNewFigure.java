package com.iscreate.plat.networkresource.engine.figure.cmd;

import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.dataservice.sql.SqlContainer;
import com.iscreate.plat.networkresource.engine.figure.Figure;

public class CommandCreateNewFigure implements Command {
	private String figureName;
	private long figureId;

	public CommandCreateNewFigure(String figureName,long figureId) {
		this.figureName = figureName;
		this.figureId = figureId;
	}
	/**
	 * Figure表插入一个记录   （dataService 源码因为没有 采用下面方法整改）
	 *(mysql to oracle 迁移:dateService封装插入记录方法context.insert(e)会发生SqlException，sql语句不标准，用context.executeInsertSQL方法替代，拼装sql）
	 *
	 */
	public Integer doExecution(ContextFactory contextFactory) {
		Context context = contextFactory.CreateContext();
		/*Entity e = new Entity();
		e.setValue(DefaultParam.typeKey, Figure.MY_TYPE);
		e.setValue(Figure.FIGURENAME_KEY, figureName);
		e.setValue(Figure.FIGUREID_KEY, figureId);*/
		String sqlString = "insert into "+Figure.MY_TYPE+"(ENTITY_TYPE,figureName,figureId) "
		                   +"values('"+Figure.MY_TYPE+"','"+figureName+"',"+figureId+")";
		//System.out.println("sqlString:"+sqlString);
		SqlContainer sc = context.createSqlContainer(sqlString);
		int result = context.executeInsertSQL(sc, Figure.MY_TYPE);
		//int result = context.insert(e);
		return result;
	}

}
