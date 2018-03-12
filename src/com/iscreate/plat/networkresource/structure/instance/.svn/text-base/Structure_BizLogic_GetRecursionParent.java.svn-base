package com.iscreate.plat.networkresource.structure.instance;


import java.util.Map;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.engine.figure.Figure;
import com.iscreate.plat.networkresource.engine.figure.FigurelineType;



class Structure_BizLogic_GetRecursionParent extends BizLogic_GetRecursionEntityAbstrace {

	public Structure_BizLogic_GetRecursionParent(ApplicationEntity begin, String aetName) {
		this.begin = begin;
		this.aetName = aetName;
		this.recursionType = PARENTRECURSION;
		this.className = "Structure_BizLogic_GetRecursionParent";
	}

	@Override
	protected ApplicationEntity[] loadEntity(Map<String, Object> condition) {
		Figure figure = this.logicEnv.get(Figure.class);
		String destinationAenType = (String) condition
				.get("destinationAenType");
		ApplicationEntity source = (ApplicationEntity) condition.get("source");
		ApplicationEntity[] result1 = figure.getAssociateEntity(source,
				FigurelineType.PARENT, destinationAenType);
		ApplicationEntity[] result2 = figure.getAssociateEntity(source,
				FigurelineType.PARENT, source.getType());
		ApplicationEntity[] result = new ApplicationEntity[result1.length
				+ result2.length];
		System.arraycopy(result1, 0, result, 0, result1.length);
		System.arraycopy(result2, 0, result, result1.length, result2.length);
		return result;
	}
}
