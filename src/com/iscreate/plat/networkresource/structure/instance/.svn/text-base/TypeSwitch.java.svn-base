package com.iscreate.plat.networkresource.structure.instance;


import com.iscreate.plat.networkresource.engine.figure.FigurelineType;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;

class TypeSwitch {

	public static FigurelineType associatedTypeSwitchToFigurelineType(
			AssociatedType type) {
		FigurelineType figurelineType;
		switch (type) {
		case CLAN:
			figurelineType = FigurelineType.CLAN;
			break;
		case CHILD:
			figurelineType = FigurelineType.CHILD;
			break;
		case PARENT:
			figurelineType = FigurelineType.PARENT;
			break;
		case LINK:
			figurelineType = FigurelineType.LINK;
			break;
		case FORWORD:
			figurelineType = FigurelineType.FORWORD;
			break;
		case BACKWORD:
			figurelineType = FigurelineType.BACKWORD;
			break;
		default:
			figurelineType = null;
			break;
		}
		return figurelineType;
	}
}
