package com.iscreate.plat.networkresource.structure.instance;


import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.engine.figure.Figure;
import com.iscreate.plat.networkresource.engine.figure.FigurelineType;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.LogicEnvironment;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.StructureBizLogic;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;

class Structure_BizLogic_GetAssociatedEntity implements
		StructureBizLogic<ApplicationEntity[]> {

	private ApplicationEntity begin;
	private String desAetName;
	private AssociatedType type;

	public Structure_BizLogic_GetAssociatedEntity(ApplicationEntity begin,
			String desAetName, AssociatedType type) {
		this.begin = begin;
		this.desAetName = desAetName;
		this.type = type;
	}

	public ApplicationEntity[] bizLogic(LogicEnvironment logicEnv) {
		Figure figure = logicEnv.get(Figure.class);
		ApplicationEntity[] aes = null;
		ApplicationEntity[] aes1, aes2;
		switch (type) {
		case CLAN:
			aes1 = figure.getAssociateEntity(begin, FigurelineType.PARENT,
					desAetName);
			aes2 = figure.getAssociateEntity(begin, FigurelineType.CHILD,
					desAetName);
			aes = new ApplicationEntity[aes1.length + aes2.length];
			System.arraycopy(aes1, 0, aes, 0, aes1.length);
			System.arraycopy(aes2, 0, aes, aes1.length, aes2.length);
			break;
		case PARENT:
			aes = figure.getAssociateEntity(begin, FigurelineType.PARENT,
					desAetName);
			break;
		case CHILD:
			aes = figure.getAssociateEntity(begin, FigurelineType.CHILD,
					desAetName);
			break;
		case LINK:
			aes1 = figure.getAssociateEntity(begin, FigurelineType.FORWORD,
					desAetName);
			aes2 = figure.getAssociateEntity(begin, FigurelineType.BACKWORD,
					desAetName);
			aes = new ApplicationEntity[aes1.length + aes2.length];
			System.arraycopy(aes1, 0, aes, 0, aes1.length);
			System.arraycopy(aes2, 0, aes, aes1.length, aes2.length);
			break;
		case FORWORD:
			aes = figure.getAssociateEntity(begin, FigurelineType.FORWORD,
					desAetName);
			break;
		case BACKWORD:
			aes = figure.getAssociateEntity(begin, FigurelineType.BACKWORD,
					desAetName);
			break;
		case ALL:
			int position = 0;
			aes1 = figure.getAssociateEntity(begin, FigurelineType.FORWORD,
					desAetName);
			aes2 = figure.getAssociateEntity(begin, FigurelineType.BACKWORD,
					desAetName);
			ApplicationEntity[] aes3 = figure.getAssociateEntity(begin,
					FigurelineType.CHILD, desAetName);
			ApplicationEntity[] aes4 = figure.getAssociateEntity(begin,
					FigurelineType.PARENT, desAetName);
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
