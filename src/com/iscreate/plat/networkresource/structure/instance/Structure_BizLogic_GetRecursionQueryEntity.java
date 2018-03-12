package com.iscreate.plat.networkresource.structure.instance;


import java.util.Map;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.engine.figure.Figure;
import com.iscreate.plat.networkresource.engine.figure.FigurelineType;



/**
 * 结构递归查询指定应用数据对象
 * 
 * @author joe
 * 
 */
class Structure_BizLogic_GetRecursionQueryEntity extends
		BizLogic_GetRecursionEntityAbstrace {

	//private Query query;
    private String query;
	public Structure_BizLogic_GetRecursionQueryEntity(ApplicationEntity begin,
			String query,String aetName) {
		this.begin = begin;
		this.query = query;
		this.aetName = aetName;//query.getEntityOrClassName();
		this.recursionType = CHILDRECURSION;
	}

	@Override
	protected ApplicationEntity[] loadEntity(Map<String, Object> condition) {
		Figure figure = this.logicEnv.get(Figure.class);
		String destinationAenType = (String) condition
				.get("destinationAenType");
		ApplicationEntity source = (ApplicationEntity) condition.get("source");
		ApplicationEntity[] result = null;
		if (destinationAenType.equals(aetName)) {
			result = figure.getAssociateEntity(source, FigurelineType.CHILD, query,this.aetName);
		} else {
			ApplicationEntity[] result1 = figure.getAssociateEntity(source,
					FigurelineType.CHILD, destinationAenType);
			ApplicationEntity[] result2 = figure.getAssociateEntity(source,
					FigurelineType.CHILD, source.getType());
			result = new ApplicationEntity[result1.length
					+ result2.length];
			System.arraycopy(result1, 0, result, 0, result1.length);
			System.arraycopy(result2, 0, result, result1.length, result2.length);
		}
		return result;
	}

}
