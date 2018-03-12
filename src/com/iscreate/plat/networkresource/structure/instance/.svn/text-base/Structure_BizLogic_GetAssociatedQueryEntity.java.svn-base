package com.iscreate.plat.networkresource.structure.instance;


import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.engine.figure.Figure;
import com.iscreate.plat.networkresource.engine.figure.FigurelineType;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.LogicEnvironment;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.StructureBizLogic;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;

class Structure_BizLogic_GetAssociatedQueryEntity implements
		StructureBizLogic<ApplicationEntity[]> {

	private ApplicationEntity begin;
	//private Query query;
	private AssociatedType type;
	private String query;
	private String  aetName;
	public Structure_BizLogic_GetAssociatedQueryEntity(ApplicationEntity begin,
			String query, AssociatedType type,String aetName) {
		this.begin = begin;
		this.query = query;
		this.type = type;
		this.aetName = aetName;
		
	}

	public ApplicationEntity[] bizLogic(LogicEnvironment logicEnv) {
		Figure figure = logicEnv.get(Figure.class);
		ApplicationEntity[] aes = null;
		ApplicationEntity[] aes1, aes2;
		switch (type) {
		case CLAN:
			aes1 = figure.getAssociateEntity(begin, FigurelineType.PARENT,
					query,aetName);
			aes2 = figure
					.getAssociateEntity(begin, FigurelineType.CHILD, query);
			aes = new ApplicationEntity[aes1.length + aes2.length];
			System.arraycopy(aes1, 0, aes, 0, aes1.length);
			System.arraycopy(aes2, 0, aes, aes1.length, aes2.length);
			break;
		case PARENT:
			aes = figure
					.getAssociateEntity(begin, FigurelineType.PARENT, query);
			break;
		case CHILD:
			aes = figure.getAssociateEntity(begin, FigurelineType.CHILD, query);
			break;
		case LINK:
			aes1 = figure.getAssociateEntity(begin, FigurelineType.FORWORD,
					query);
			aes2 = figure.getAssociateEntity(begin, FigurelineType.BACKWORD,
					query);
			aes = new ApplicationEntity[aes1.length + aes2.length];
			System.arraycopy(aes1, 0, aes, 0, aes1.length);
			System.arraycopy(aes2, 0, aes, aes1.length, aes2.length);
			break;
		case FORWORD:
			aes = figure.getAssociateEntity(begin, FigurelineType.FORWORD,
					query);
			break;
		case BACKWORD:
			aes = figure.getAssociateEntity(begin, FigurelineType.BACKWORD,
					query);
		case ALL:
			int position = 0;
			aes1 = figure.getAssociateEntity(begin, FigurelineType.FORWORD,
					query);
			aes2 = figure.getAssociateEntity(begin, FigurelineType.BACKWORD,
					query);
			ApplicationEntity[] aes3 = figure.getAssociateEntity(begin,
					FigurelineType.CHILD, query);
			ApplicationEntity[] aes4 = figure.getAssociateEntity(begin,
					FigurelineType.PARENT, query);
			aes = new ApplicationEntity[aes1.length + aes2.length + aes3.length
					+ aes4.length];
			System.arraycopy(aes1, 0, aes, position, aes1.length);
			position = aes1.length;
			System.arraycopy(aes2, 0, aes, position, aes2.length);
			position += aes2.length;
			System.arraycopy(aes3, 0, aes, position, aes3.length);
			position += aes3.length;
			System.arraycopy(aes4, 0, aes, position, aes4.length);
			break;
		}
		return aes;
	}
}
