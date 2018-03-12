package com.iscreate.plat.networkresource.engine.figure;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandCreateNewFigure;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandSaveOrUpdateAppEntity;
import com.iscreate.plat.networkresource.engine.figure.execution.Execution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryFigureIdByNameExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryFigureNamesExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryOneFigureNameExecution;
import com.iscreate.plat.networkresource.structure.template.StructurePrimary;

public class FigureEngine {
	private ContextFactory contextFactory;

	public ContextFactory getContextFactory() {
		return contextFactory;
	}

	public void setContextFactory(ContextFactory contextFactory) {
		this.contextFactory = contextFactory;
	}

	public Figure getFigure(String figureName) {
		if (figureName == null || figureName.trim().isEmpty()) {
			return null;
		}
		QueryFigureIdByNameExecution qfin = new QueryFigureIdByNameExecution(
				figureName);
		long figureId = doEngineExecution(qfin);
		Figure figure;
		if (figureId == 0 || figureId < 0) {
			figureId = StructurePrimary.getEntityPrimaryKey("Figure", contextFactory.CreateContext());
			figure = new Figure(figureId, contextFactory);
			CommandCreateNewFigure cmd = new CommandCreateNewFigure(figureName,
					figureId);
			figure.addCommand(cmd);
		} else {
			figure = new Figure(figureId, contextFactory);
		}
		return figure;
	}

	public Figure getFigure(String figureName, ApplicationEntity entity) {
		if (entity == null) {
			return getFigure(figureName);
		}
		if (figureName == null || figureName.trim().isEmpty()) {
			return null;
		}
		QueryFigureIdByNameExecution qfin = new QueryFigureIdByNameExecution(
				figureName);
		long figureId = doEngineExecution(qfin);
		Figure figure;
		if (figureId == 0 || figureId < 0) {
			figureId = StructurePrimary.getEntityPrimaryKey("Figure", contextFactory.CreateContext());
			figure = new Figure(figureId, contextFactory);
			CommandCreateNewFigure cmd = new CommandCreateNewFigure(figureName,
					figureId);
			figure.addCommand(cmd);
		} else {
			figure = new Figure(figureId, contextFactory);
		}
		CommandSaveOrUpdateAppEntity saecmd = new CommandSaveOrUpdateAppEntity(
				entity);
		figure.addCommand(saecmd);
		figure.setBegin(entity);
		return figure;
	}

	public String listOneAenFigureName(ApplicationEntity entity) {
		String figureName = doEngineExecution(new QueryOneFigureNameExecution(
				entity));
		return figureName;
	}

	public String[] listAenFigureName(ApplicationEntity entity) {
		String[] figureNames = doEngineExecution(new QueryFigureNamesExecution(
				entity));
		return figureNames;
	}

	public <T> T doEngineExecution(Execution<T> execution) {
		return execution.doExecution(contextFactory);
	}
	
	

}
