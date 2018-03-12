package com.iscreate.plat.networkresource.structure.instance;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.engine.figure.Figure;
import com.iscreate.plat.networkresource.engine.figure.FigureEngine;
import com.iscreate.plat.networkresource.engine.figure.FigurelineType;
import com.iscreate.plat.networkresource.structure.instance.api.StructureModifyInterface;
import com.iscreate.plat.networkresource.structure.instance.api.StructureSearchInterface;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.StructureBizLogic;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureConsequence;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureRestrainCode;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureRestraint;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleLibrary;

public class Structure implements StructureSearchInterface,StructureModifyInterface {
	// 结构的名称
	private String structureName = "";
	// 图引擎对象，由图引擎生成图对象
	private FigureEngine figureEngine;
	// 获取结构的初始点，新建立的结构对象没有初始点
	private ApplicationEntity penetration;
	// 图对象，默认为空，只有使用的时候才会生成
	private Figure figure;
	// 该结构对象对应的结构模板
	private StructureModule structureModule;

	public Structure(String structureName, StructureModule structureModule,
			FigureEngine figureEngine) {
		this.structureName = structureName;
		this.structureModule = structureModule;
		this.figureEngine = figureEngine;
	}

	public Structure(String structureName, StructureModule structureModule,
			FigureEngine figureEngine, ApplicationEntity penetration) {
		this.structureName = structureName;
		this.structureModule = structureModule;
		this.figureEngine = figureEngine;
		this.penetration = penetration;
	}

	/**
	 * 获取该结构对象对应的结构模板
	 * 
	 * @return 该结构实例所引用的结构模板对象
	 */
	public StructureModule getStructureModule() {
		return structureModule;
	}

	/**
	 * 获取该结构对象的名称
	 * 
	 * @return 获取结构实例的名字
	 */
	public String getStructureName() {
		return structureName;
	}

	/**
	 * 获取结构对象的初始点对象
	 * 
	 * @return
	 */
	ApplicationEntity getPenetration() {
		return this.penetration;
	}

	/**
	 * 结构处理自定义的业务逻辑
	 * 
	 * @param <E>
	 *            返回值的数据类型
	 * @param bizLogic
	 *            自定义业务逻辑
	 * @return 业务逻辑的返回值
	 */
	public <E> E doBizLogic(StructureBizLogic<E> bizLogic) {
		if (!structureLegel()) {
			return null;
		}
		BasicLogicEnvironment env = new BasicLogicEnvironment(
				this.figureEngine.getContextFactory());
		env.set(this.figureEngine.getContextFactory());
		env.set(this);
		env.set(this.figure);
		env.set(this.figureEngine);
		env.set(this.structureModule);
		env.set(getStructureModuleLibrary());
		
		return bizLogic.bizLogic(env);
	}

	/**
	 * 获取指定对象begin的关联方向上的关联对象
	 * 
	 * @param begin
	 *            需要查找的指定对象
	 * @param type
	 *            关联的类型
	 * @return 关联方向上的目标应用数据对象
	 */
	public ApplicationEntity[] getAssociatedEntity(ApplicationEntity begin,
			AssociatedType type) {
		return getAssociatedEntity(begin, "", type);
	}

	/**
	 * 获取指定对象begin在关联方向上特定类型desAetName的应用数据对象
	 * 
	 * @param begin
	 *            需要查找的指定对象
	 * @param desAetName
	 *            目标对象的数据类型
	 * @param type
	 *            关联的类型
	 * @return 关联方向上的目标应用数据对象
	 */
	public ApplicationEntity[] getAssociatedEntity(ApplicationEntity begin,
			String desAetName, AssociatedType type) {
		if (!structureLegel()) {
			return new ApplicationEntity[] {};
		}
		// 校验
		StructureRestraint restrain = null;
		StructureConsequence consequence = null;
		restrain = RestrainFactory.getAetNameRestrain(begin, structureModule);
		consequence = restrain.restrain();
		if (consequence.getCode().indexOf("0000") > 0) {
			Structure_BizLogic_GetAssociatedEntity logic = new Structure_BizLogic_GetAssociatedEntity(
					begin, desAetName, type);
			ApplicationEntity[] es = doBizLogic(logic);
			return es;
		} else {
			return new ApplicationEntity[] {};
		}
	}

	/**
	 * 获取指定对象begin在关联方向上某个组的数据
	 * 
	 * @param begin
	 *            需要查找的指定对象
	 * @param type
	 *            关联的类型
	 * @param aetgName
	 *            目标应用数据对象所在的组
	 * @return 关联方向上的目标应用数据对象
	 */
	public ApplicationEntity[] getAssociatedEntityOfAetg(
			ApplicationEntity begin, AssociatedType type, String aetgName) {
		if (!structureLegel()) {
			return new ApplicationEntity[] {};
		}
		// 校验
		StructureRestraint restrain = null;
		StructureConsequence consequence = null;
		restrain = RestrainFactory.getAetNameRestrain(begin, structureModule);
		consequence = restrain.restrain();
		if (consequence.getCode().indexOf("0000") > 0) {
			String[] aets = this.getAetNameOfAetg(aetgName);
			ApplicationEntity[] es = {};
			for (String desAetName : aets) {
				Structure_BizLogic_GetAssociatedEntity logic = new Structure_BizLogic_GetAssociatedEntity(
						begin, desAetName, type);
				ApplicationEntity[] result = doBizLogic(logic);
				if (result != null) {
					ApplicationEntity[] tmp = new ApplicationEntity[result.length
							+ es.length];
					System.arraycopy(result, 0, tmp, 0, result.length);
					System.arraycopy(es, 0, tmp, result.length, es.length);
					es = tmp;
				}
			}
			return es;
		} else {
			return new ApplicationEntity[] {};
		}
	}

	/**
	 * 获取指定对象begin在关联方向上特定条件query对象的应用数据对象
	 * 
	 * @param begin
	 *            需要查找的指定对象
	 * @param type
	 *            关联的类型
	 * @param query
	 *            目标对象的条件
	 * @return 关联方向上的目标应用数据对象
	 */
	public ApplicationEntity[] getAssociatedEntity(ApplicationEntity begin,
			AssociatedType type, String query,String aetName) {
		if (query == null) {
			return getAssociatedEntity(begin, "", type);
		}
		if (!structureLegel()) {
			return new ApplicationEntity[] {};
		}
		// 校验
		String desAetName = aetName;//query.getEntityOrClassName();
		StructureConsequence consequence = RestrainFactory
				.getAetAssociationRestrain(begin, desAetName, type, this)
				.restrain();
		if (consequence.getCode().indexOf("0000") > 0) {
			Structure_BizLogic_GetAssociatedQueryEntity logic = new Structure_BizLogic_GetAssociatedQueryEntity(
					begin, query, type,aetName);
			ApplicationEntity[] es = doBizLogic(logic);
			return es;
		} else {
			return new ApplicationEntity[] {};
		}
	}

	/**
	 * 递归获取指定对象begin下特定类型desAetName的子应用数据对象
	 * 
	 * @param begin
	 *            需要查找的指定对象
	 * @param desAetName
	 *            目标对象的数据类型
	 * @return 指定类型的目标应用数据对象
	 */
	public ApplicationEntity[] getApplicationEntityRecursion(
			ApplicationEntity begin, String desAetName) {
		if (!structureLegel()) {
			return new ApplicationEntity[] {};
		}
		// 校验
		StructureConsequence consequence = RestrainFactory.getAetNameRestrain(
				begin, structureModule).restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return new ApplicationEntity[] {};
		}
		consequence = RestrainFactory.getAetNameRestrain(desAetName,
				structureModule).restrain();
		if (consequence.getCode().indexOf("0000") > 0) {
			Structure_BizLogic_GetRecursionEntity logic = new Structure_BizLogic_GetRecursionEntity(
					begin, desAetName);
			ApplicationEntity[] es = doBizLogic(logic);
			return es;
		} else {
			return new ApplicationEntity[] {};
		}
	}

	/**
	 * 递归获取指定对象begin下特定条件query的子应用数据对象
	 * 
	 * @param begin
	 *            需要查找的指定对象
	 * @param query
	 *            目标类型的相关查询条件
	 * @return 指定类型的目标应用数据对象
	 */
	public ApplicationEntity[] getApplicationEntityRecursion(
			ApplicationEntity begin, String query,String aetName) {
		if (query == null) {
			return new ApplicationEntity[] {};
		}
		if (!structureLegel()) {
			return new ApplicationEntity[] {};
		}
		// 校验
		StructureConsequence consequence = RestrainFactory.getAetNameRestrain(
				begin, structureModule).restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return new ApplicationEntity[] {};
		}
		String desAetName = aetName;//query.getEntityOrClassName();
		consequence = RestrainFactory.getAetNameRestrain(desAetName,
				structureModule).restrain();
		if (consequence.getCode().indexOf("0000") > 0) {
			Structure_BizLogic_GetRecursionQueryEntity logic = new Structure_BizLogic_GetRecursionQueryEntity(
					begin, query,desAetName);
			ApplicationEntity[] es = doBizLogic(logic);
			return es;
		} else {
			return new ApplicationEntity[] {};
		}
	}

	/**
	 * 获取应用数据对象start、end之间的路由关系
	 * 
	 * @param start
	 *            起点应用数据对象
	 * @param end
	 *            终点应用数据对象
	 * @return 使用应用数据对象构成的路径列表
	 */
	public List<List<ApplicationEntity>> getTraceRoad(ApplicationEntity start,
			ApplicationEntity end) {
		StructureConsequence consequence = RestrainFactory.getAetNameRestrain(
				start, structureModule).restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return new ArrayList<List<ApplicationEntity>>();
		}
		consequence = RestrainFactory.getAetNameRestrain(end, structureModule)
				.restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return new ArrayList<List<ApplicationEntity>>();
		}
		if (!structureLegel()) {
			return new ArrayList<List<ApplicationEntity>>();
		}
		return figure.traceRoad(start, end);
	}

	/**
	 * 将entity加入到结构实例当中
	 * 
	 * @param entity
	 *            应用数据对象
	 * @return 状态对象
	 */
	public StructureConsequence addApplicationEntityToStructure(
			ApplicationEntity entity) {
		if (!structureLegel()) {
			return new StructureConsequence(
					StructureRestrainCode.STRUCTURE_NAME_NOT_DEFINE, "结构名称未定义");
		}
		StructureConsequence consequence = RestrainFactory.getAetNameRestrain(
				entity, structureModule).restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return consequence;
		}
		figure.addApplicationEntity(entity);
		return consequence;
	}

	/**
	 * 创建两个应用数据对象的关系
	 * 
	 * @param leftAe
	 *            应用数据对象
	 * @param rightAe
	 *            应用数据对象
	 * @param type
	 *            两个对象的关联类型
	 * @return 状态对象
	 */
	public StructureConsequence createAssociation(ApplicationEntity leftAe,
			ApplicationEntity rightAe, AssociatedType type) {
		if (!structureLegel()) {
			return new StructureConsequence(
					StructureRestrainCode.STRUCTURE_NAME_NOT_DEFINE, "结构名称未定义");
		}
		StructureConsequence consequence = RestrainFactory
				.getAetAssociationRestrain(leftAe, rightAe, type, this)
				.restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return consequence;
		}
		FigurelineType figurelineType = TypeSwitch
				.associatedTypeSwitchToFigurelineType(type);
		figure.createRelation(leftAe, rightAe, figurelineType);
		return new StructureConsequence(StructureRestrainCode.SUCCESS_CODE,
				"success");
	}

	/**
	 * 通过结构实例创建一个新的应用数据对象
	 * 
	 * @param aetName
	 *            应用数据对象类型
	 * @return 应用数据对象
	 */
	public ApplicationEntity newApplicationEntity(String aetName) {
		StructureConsequence consequence = RestrainFactory.getAetNameRestrain(
				aetName, structureModule).restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return null;
		}
		if (!structureLegel()) {
			return null;
		}
		ApplicationModule module = ModuleProvider.getModule(aetName);
		ApplicationEntity entity = module.createApplicationEntity();
		this.figure.addApplicationEntity(entity);
		return entity;
	}

	/**
	 * 在指定的应用数据对象source下创建一个指定类型的应用数据对象
	 * 
	 * @param source
	 *            父应用数据对象
	 * @param aetName
	 *            应用数据对象类型
	 * @return 应用数据对象
	 */
	public ApplicationEntity newApplicationEntity(ApplicationEntity source,
			String aetName) {
		// 校验
		StructureConsequence consequence = RestrainFactory.getAetNameRestrain(
				aetName, structureModule).restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return null;
		}
		consequence = RestrainFactory.getAetNameRestrain(source,
				structureModule).restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return null;
		}
		if (!structureLegel()) {
			return null;
		}
		ApplicationModule module = ModuleProvider.getModule(aetName);
		ApplicationEntity entity = module.createApplicationEntity();
		this.figure.createRelation(source, entity, FigurelineType.CLAN);
		return entity;
	}

	/**
	 * 单独删除一个应用数据对象
	 * 
	 * @param entity
	 *            应用数据对象
	 * @return 状态对象
	 */
	public StructureConsequence delApplicationEntityOnly(
			ApplicationEntity entity) {
		StructureConsequence consequence = RestrainFactory.getAetNameRestrain(
				entity, structureModule).restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return consequence;
		}
		if (!structureLegel()) {
			return new StructureConsequence(
					StructureRestrainCode.STRUCTURE_NAME_NOT_DEFINE, "结构名称未定义");
		}
		this.figure.delApplicationEntity(entity);
		return consequence;

	}

	/**
	 * 删除一个应用数据对象其下所有的子对象
	 * 
	 * @param entity
	 *            应用数据对象
	 * @return 状态对象
	 */
	public StructureConsequence delApplicationEntityRecursion(
			ApplicationEntity entity) {
		StructureConsequence consequence = RestrainFactory.getAetNameRestrain(
				entity, structureModule).restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return consequence;
		}
		if (!structureLegel()) {
			return new StructureConsequence(
					StructureRestrainCode.STRUCTURE_NAME_NOT_DEFINE, "结构名称未定义");
		}
		this.figure.delApplicationEntityRecursion(entity);
		return consequence;
	}

	/**
	 * 删除一个应用数据对象及其下所有的子对象
	 * 
	 * @param entity
	 *            应用数据对象
	 * @return 状态对象
	 */
	public StructureConsequence delApplicationEntityRecursionByFource(
			ApplicationEntity entity) {
		StructureConsequence consequence = RestrainFactory.getAetNameRestrain(
				entity, structureModule).restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return consequence;
		}
		if (!structureLegel()) {
			return new StructureConsequence(
					StructureRestrainCode.STRUCTURE_NAME_NOT_DEFINE, "结构名称未定义");
		}
		this.figure.delApplicationEntityRecursionByFource(entity);
		return consequence;
	}

	/**
	 * 删除两个对象之间的关系
	 * 
	 * @param leftAe
	 *            应用数据对象
	 * @param rightAe
	 *            应用数据对象
	 * @param type
	 *            两个对象的关联类型
	 * @return 状态对象
	 */
	public StructureConsequence delAssociation(ApplicationEntity leftAe,
			ApplicationEntity rightAe, AssociatedType type) {
		if (!structureLegel()) {
			return new StructureConsequence(
					StructureRestrainCode.STRUCTURE_NAME_NOT_DEFINE, "结构名称未定义");
		}
		FigurelineType figurelineType = TypeSwitch
				.associatedTypeSwitchToFigurelineType(type);
		this.figure.removeRelation(leftAe, rightAe, figurelineType);
		return null;
	}

	/**
	 * 查询结构模板，获取某一个应用数据对象类型在指定的方向上与其关联的其它应用数据对象类型
	 * 
	 * @param currentAetName
	 *            应用数据对象类型
	 * @param type
	 *            关联的方向
	 * @return 在关联方向上的应用数据对象列表
	 */
	public String[] getAssociatedAetName(String currentAetName,
			AssociatedType type) {
		BizLogic_GetAssociatedAetName bizLogic = new BizLogic_GetAssociatedAetName(
				currentAetName, type);
		String[] nextModuleName = doBizLogic(bizLogic);
		return nextModuleName;
	}

	/**
	 * 获取一个组中的AET NAME
	 * 
	 * @param aetgName
	 *            结构中的一个应用数据对象组名称
	 * @return 该组中的应用数据对象类型列表
	 */
	public String[] getAetNameOfAetg(String aetgName) {
		BizLogic_GetAetNameOfAetg bizLogic = new BizLogic_GetAetNameOfAetg(
				aetgName);
		return doBizLogic(bizLogic);
	}

	/**
	 * 查询结构模板，获取某一个应用数据对象在指定的方向上与其关联的其它应用数据对象类型
	 * 
	 * 
	 * @param begin
	 *            应用数据对象
	 * @param type
	 *            关联类型
	 * @return 在关联方向上的应用数据对象列表
	 */
	public String[] getAssociatedAetName(ApplicationEntity begin,
			AssociatedType type) {
		StructureConsequence consequence = RestrainFactory.getAetNameRestrain(
				begin, structureModule).restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return new String[] {};
		}
		String currentAetName = begin.getType();
		BizLogic_GetAssociatedAetName bizLogic = new BizLogic_GetAssociatedAetName(
				currentAetName, type);
		String[] nextAetName = doBizLogic(bizLogic);
		return nextAetName;
	}

	/**
	 * 将结构的数据对象保存起来
	 */
	public int store() {
		if (!structureLegel()) {
			return 0;
		}else{
			return figure.storeFigure();
		}
		}

	/**
	 * 通过名称获取结构对象相应的视图
	 * 
	 * @param viewName
	 *            视图在XML中的定义名称
	 * @return 结构视图对象
	 */
	public StructureView myView(String viewName) {
		StructureModule vm = this.structureModule.myView(viewName);
		if (vm == null) {
			return null;
		}
		StructureView view = new StructureView(this, vm);
		return view;
	}

	/**
	 * 工厂方法，创建一个数据操作的上下文
	 * 
	 * @return 数据操作上下文
	 */
	public Context createContext() {
		structureLegel();
		return this.figure.getContextFactory().CreateContext();
	}

	Figure myFigure() {
		structureLegel();
		return this.figure;
	}

	private boolean structureLegel() {
		if (this.structureName == null || this.structureName.trim().isEmpty()) {
			return false;
		}
		if (this.figure == null) {
			String name = this.structureModule.getStructureModuleName() + "-"
					+ structureName;
			this.figure = this.figureEngine.getFigure(name);
		}
		return true;
	}

	private StructureModuleLibrary getStructureModuleLibrary() {
		StructureModuleLibrary lib = null;
		try {
			Method method = structureModule.getClass().getDeclaredMethod(
					"getStructureModuleLibrary", new Class[] {});
			method.setAccessible(true);
			Object obj = method.invoke(structureModule, new Object[] {});
			lib = StructureModuleLibrary.class.cast(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lib;
	}
	/**
	 * 
	 * @description: 当前环境figure
	 * @author：yuan.yw
	 * @return     
	 * @return Figure     
	 * @date：Feb 2, 2013 10:43:38 AM
	 */
	public Figure getCurrentFigure() {
		structureLegel();
		return this.figure;
	}
}
