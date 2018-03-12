package com.iscreate.plat.networkresource.structure.template;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.engine.figure.FigureEngine;
import com.iscreate.plat.networkresource.structure.template.execution.QueryStructureModuleByName;
import com.iscreate.plat.networkresource.structure.template.execution.SaveOrUpdateStructureModule;
import com.iscreate.plat.networkresource.structure.template.helper.StructureModuleSerializable;
import com.iscreate.plat.networkresource.structure.template.helper.StructureModuleXmlFileLoader;

public class StructureModuleLibrary {

	private FigureEngine figureEngine;
	private Map<String, StructureModule> structureModuleCache = new HashMap<String, StructureModule>();

	private final StructureModuleXmlFileLoader structureModuleXmlFileLoader = new StructureModuleXmlFileLoader();
	private final StructureModuleSerializable structureModuleSerializable = new StructureModuleSerializable();

	public void setContextFactory(ContextFactory contextFactory) {
		figureEngine = new FigureEngine();
		figureEngine.setContextFactory(contextFactory);
	}

	/**
	 * 初始化方法
	 */
	public void init() {
		File[] xmlFiles = structureModuleXmlFileLoader.listMappingFile();
		for (File xmlFile : xmlFiles) {
			StructureModule module = structureModuleSerializable
					.deserializable(xmlFile);
			increaseModule(module);
		}
	}

	public void dumpIntoDb(File xmlFile) {
		if (xmlFile == null || !xmlFile.isFile()) {
			return;
		}
		StructureModule module = structureModuleSerializable
				.deserializable(xmlFile);
		figureEngine.doEngineExecution(new SaveOrUpdateStructureModule(module));
	}

	/**
	 * 获取结构模板
	 * 
	 * @param structureModuleName
	 * @return
	 */
	public StructureModule getStructureModule(String structureModuleName) {
		StructureModule module = structureModuleCache.get(structureModuleName);
		return module;
	}

	public StructureModule loadStructureModule(String structureModuleName) {
		long domainId = 1l;
		StructureModule module = figureEngine
				.doEngineExecution(new QueryStructureModuleByName(domainId,
						structureModuleName));
		return module;
	}

	public boolean hasModule(String structureModuleName) {
		return structureModuleCache.containsKey(structureModuleName);
	}

	public String[] listStructureModuleNames() {
		String[] keys = new String[structureModuleCache.keySet().size()];
		structureModuleCache.keySet().toArray(keys);
		return keys;
	}

	public Context createContext() {
		return figureEngine.getContextFactory().CreateContext();
	}

	void releaseModule(String structureModuleName) {
		structureModuleCache.remove(structureModuleName);
	}

	void increaseModule(StructureModule structureModule) {
		if (structureModule == null) {
			return;
		}
		String structureModuleName = structureModule.getStructureModuleName();
		if (structureModuleName.isEmpty()) {
			return;
		}
		structureModule.setFigureEngine(figureEngine);
		structureModule.setStructureModuleLibrary(this);
		structureModuleCache.put(structureModuleName, structureModule);
	}
}
