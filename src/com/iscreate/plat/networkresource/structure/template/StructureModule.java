package com.iscreate.plat.networkresource.structure.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.engine.figure.FigureEngine;
import com.iscreate.plat.networkresource.structure.instance.Structure;
import com.iscreate.plat.networkresource.structure.template.execution.ListStructureInstanceNames;

public class StructureModule extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3614185193001055548L;

	// 对象的模板名称
	public final static String MY_TYPE = StructureModule.class.getSimpleName();
	// 模板名称的属性KEY
	public final static String STRUCTURE_MODULE_NAME_KEY = "structureModuleName";
	public final static String STRUCTURE_PARENT_NAME_KEY = "parentStructureName";
	public final static String DOMAIN_KEY = "domainId";
	public final static String MODULE_TYPE_KEY = "structureModuleType";

	// 模板中的AET定义
	Map<String, StructureModuleEntity> moduleEntitys = new HashMap<String, StructureModuleEntity>();
	// 模板中的AETG的定义
	Map<String, StructureModuleEntityGroup> moduleEntityGroups = new HashMap<String, StructureModuleEntityGroup>();
	// 模板中的节点定义
	List<StructureModuleNode> moduleNodes = new ArrayList<StructureModuleNode>();
	// 模板中的视图
	Map<String, StructureModule> views = new HashMap<String, StructureModule>();

	// 结构模板中的图引擎
	private FigureEngine figureEngine;
	// 由结构模板到结构模板库的反向引用
	private StructureModuleLibrary structureModuleLibrary;

	// 结构模板中的模板构造器
	private final StructureModuleBuilder structureModuleBuilder = new StructureModuleBuilder();

	/**
	 * 返回结构模板中的AET引用
	 * 
	 * @return
	 */
	public Map<String, StructureModuleEntity> getModuleEntitys() {
		return moduleEntitys;
	}

	/**
	 * 返回结构模板中的AETG的引用
	 * 
	 * @return
	 */
	public Map<String, StructureModuleEntityGroup> getModuleEntityGroups() {
		return moduleEntityGroups;
	}

	/**
	 * 返回结构模板中的节点引用
	 * 
	 * @return
	 */
	public List<StructureModuleNode> getModuleNodes() {
		return moduleNodes;
	}

	/**
	 * 返回结构模板中的视图引用
	 * 
	 * @return
	 */
	public Map<String, StructureModule> getModuleViews() {
		return views;
	}

	/**
	 * 带参数的构造函数，参数为模板名称
	 * 
	 * @param structureModuleName
	 */
	public StructureModule(String structureModuleName) {
		set(DefaultParam.typeKey, MY_TYPE);
		long entityPrimaryKey =0;
		if(structureModuleLibrary != null ){
			
			Context createContext = structureModuleLibrary.createContext();
			entityPrimaryKey = StructurePrimary.getEntityPrimaryKey(MY_TYPE,createContext);
		}
		String string = Long.toString(entityPrimaryKey);
		set(DefaultParam.idKey,string);
		set(STRUCTURE_MODULE_NAME_KEY, structureModuleName);
		set(DOMAIN_KEY, 1l);
		asStructure();
	}

	/**
	 * 内部无参构造函数
	 */
	private StructureModule() {

	}

	/**
	 * 获取结构模板的名称
	 * 
	 * @return
	 */
	public String getStructureModuleName() {
		String structureModuleName = getValue(STRUCTURE_MODULE_NAME_KEY);
		if (structureModuleName == null) {
			return "";
		} else {
			return structureModuleName.trim();
		}
	}

	void setStructureModuleName(String structureModuleName) {
		set(STRUCTURE_MODULE_NAME_KEY, structureModuleName);
	}

	/**
	 * 为结构模板设置图引擎
	 * 
	 * @param figureEngine
	 */
	public void setFigureEngine(FigureEngine figureEngine) {
		this.figureEngine = figureEngine;
		// this.contextFactory = figureEngine.getContextFactory();
	}

	public List<String> listMyInstanceNames() {
		String structureModuleName = getValue(STRUCTURE_MODULE_NAME_KEY);
		return figureEngine.doEngineExecution(new ListStructureInstanceNames(
				structureModuleName));
	}

	/**
	 * 通过结构名称，去获取该结构模板生成的结构实例
	 */
	public Structure myStructure(String structureName) {
		if (structureName == null || structureName.trim().isEmpty()) {
			structureName = randomStructureName();
		}
		Structure structure = new Structure(structureName, this,
				this.figureEngine);
		return structure;
	}

	public StructureModule myView(String viewName) {
		StructureModule view = this.views.get(viewName);
		return view;
	}

	/**
	 * 创建一个数据库查询上下文
	 * 
	 * @return
	 */
	public Context createContext() {
		return this.figureEngine.getContextFactory().CreateContext();
	}

	/**
	 * 往结构实例中增加一个应用数据模板的引用
	 * 
	 * @param entity
	 * @return
	 */
	public StructureModule increaseEntity(StructureModuleEntity entity) {
		structureModuleBuilder.increaseEntity(this, entity);
		return this;
	}

	/**
	 * 往结构实例中删除一个应用数据模板的引用
	 * 
	 * @param entity
	 * @return
	 */
	public StructureModule decreaseEntity(StructureModuleEntity entity) {
		structureModuleBuilder.decreaseEntity(this, entity);
		return this;
	}

	/**
	 * 
	 * @param entityGroup
	 * @return
	 */
	public StructureModule increaseEntityGroup(
			StructureModuleEntityGroup entityGroup) {
		structureModuleBuilder.increaseEntityGroup(this, entityGroup);
		return this;
	}

	public StructureModule decreaseEntityGroup(
			StructureModuleEntityGroup entityGroup) {
		structureModuleBuilder.decreaseEntityGroup(this, entityGroup);
		return this;
	}

	public StructureModule increaseRelation(StructureModuleNode node) {
		structureModuleBuilder.increaseRelation(this, node);
		return this;
	}

	public StructureModule decreaseRelation(StructureModuleNode node) {
		structureModuleBuilder.decreaseRelation(this, node);
		return this;
	}

	public StructureModule increaseView(StructureModule view) {
		structureModuleBuilder.increaseView(this, view);
		return this;
	}

	public StructureModule decreaseView(StructureModule view) {
		structureModuleBuilder.decreaseView(this, view);
		return this;
	}

	public static StructureModule changeFromEntity(BasicEntity be) {
		StructureModule module = new StructureModule();
		for (String key : be.keyset()) {
			module.set(key, be.getValue(key));
		}
		return module;
	}

	/**
	 * dump方法，打印结构模板的内部结构
	 */
	public void dump() {
		//System.out.println(this.moduleEntitys);
		//System.out.println(this.moduleEntityGroups);
		//System.out.println(this.moduleNodes);
	}

	StructureModuleLibrary getStructureModuleLibrary() {
		return structureModuleLibrary;
	}

	void setStructureModuleLibrary(StructureModuleLibrary structureModuleLibrary) {
		this.structureModuleLibrary = structureModuleLibrary;
	}

	void asView(String parentName) {
		set(STRUCTURE_PARENT_NAME_KEY, parentName);
		set(MODULE_TYPE_KEY, "VIEW");
	}

	void asStructure() {
		set(STRUCTURE_PARENT_NAME_KEY, "");
		set(MODULE_TYPE_KEY, "STRUCTURE");
	}

	void setModuleEntitys(Map<String, StructureModuleEntity> moduleEntitys) {
		this.moduleEntitys = moduleEntitys;
	}

	void setModuleEntityGroups(
			Map<String, StructureModuleEntityGroup> moduleEntityGroups) {
		this.moduleEntityGroups = moduleEntityGroups;
	}

	void setModuleNodes(Collection<StructureModuleNode> moduleNodes) {
		this.moduleNodes.addAll(moduleNodes);
	}

	private String randomStructureName() {
		String structureName = UUID.randomUUID().toString().replace("-", "");
		return structureName;
	}
	
	
	public static void main(String[] args) {
		String uu =  UUID.randomUUID().toString();
		//System.out.println(IDBuilder.randomId());
		System.out.println(uu.toString().replace("-", ""));
		Date date = new Date();
		Date date1 = new Date();
		long time = date.getTime();
		System.out.println(date);
		System.out.println(time);
		long time1 = date1.getTime();
		System.out.println(date1);
		System.out.println(time1);
	}
	
	
	
}
