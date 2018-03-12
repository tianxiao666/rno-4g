package com.iscreate.plat.networkresource.structure.instance;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.structure.instance.api.StructureSearchInterface;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.StructureBizLogic;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureConsequence;
import com.iscreate.plat.networkresource.structure.instance.restrain.StructureRestraint;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.networkresource.structure.template.StructureModule;



public class StructureView implements StructureSearchInterface {
	// 源数据结构
	private StructureSearchInterface source;
	// 该结构对象对应的结构模板
	private StructureModule structureModule;

	public StructureView(StructureSearchInterface source,
			StructureModule structureModule) {
		this.source = source;
		this.structureModule = structureModule;
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
		if (bizLogic == null) {
			return null;
		}
		BasicLogicEnvironment env = new BasicLogicEnvironment(
				getSourceStructure().myFigure().getContextFactory());
		env.set(this);
		env.set(this.structureModule);
		env.set(getSourceStructure());
		env.set(getSourceStructure().myFigure());
		return bizLogic.bizLogic(env);
	}

	public String[] getAetNameOfAetg(String aetgName) {
		BizLogic_GetAetNameOfAetg bizLogic = new BizLogic_GetAetNameOfAetg(
				aetgName);
		return doBizLogic(bizLogic);
	}

	public ApplicationEntity[] getApplicationEntityRecursion(
			ApplicationEntity begin, String query,String aetName) {
		if (!this.structureViewLegel()) {
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
		// 校验成功进行数据查询
		if (consequence.getCode().indexOf("0000") > 0) {
			// 获取结构视图的源
			Structure structure = getSourceStructure();
			// 直接调用源的相应方法去获取应用数据对象
			ApplicationEntity[] aes = structure.getApplicationEntityRecursion(
					begin, query);
			return aes;
		} else {
			return new ApplicationEntity[] {};
		}
	}

	public ApplicationEntity[] getApplicationEntityRecursion(
			ApplicationEntity begin, String desAetName) {
		if (!this.structureViewLegel()) {
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
		// 校验成功进行数据查询
		if (consequence.getCode().indexOf("0000") > 0) {
			// 获取结构视图的源
			Structure structure = getSourceStructure();
			// 直接调用源的相应方法去获取应用数据对象
			ApplicationEntity[] aes = structure.getApplicationEntityRecursion(
					begin, desAetName);
			return aes;
		} else {
			return new ApplicationEntity[] {};
		}
	}

	public String[] getAssociatedAetName(ApplicationEntity begin,
			AssociatedType type) {
		// 校验
		StructureConsequence consequence = RestrainFactory.getAetNameRestrain(
				begin, structureModule).restrain();
		if (consequence.getCode().indexOf("0000") < 0) {
			return new String[] {};
		}
		// 获取当前应用数据对象的类型名称
		String currentAetName = begin.getType();
		// 查找关联类型的实现类
		BizLogic_GetAssociatedAetName bizLogic = new BizLogic_GetAssociatedAetName(
				currentAetName, type);
		String[] nextAetName = doBizLogic(bizLogic);
		return nextAetName;
	}

	public String[] getAssociatedAetName(String currentAetName,
			AssociatedType type) {
		// 查找关联类型的实现类
		BizLogic_GetAssociatedAetName bizLogic = new BizLogic_GetAssociatedAetName(
				currentAetName, type);
		String[] nextModuleName = doBizLogic(bizLogic);
		return nextModuleName;
	}

	public ApplicationEntity[] getAssociatedEntity(ApplicationEntity begin,
			AssociatedType type) {
		return getAssociatedEntity(begin, "", type);
	}

	public ApplicationEntity[] getAssociatedEntity(ApplicationEntity begin,
			AssociatedType type, String query,String aetName) {
		if (!structureViewLegel()) {
			return new ApplicationEntity[] {};
		}
		StructureRestraint restrain = RestrainFactory.getAetNameRestrain(
				structureModule, begin.getType(),aetName);
		StructureConsequence consequence = restrain.restrain();
		if (consequence.getCode().indexOf("0000") > 0) {
			StructureView_BizLogic_GetAssociatedQueryEntity bizlogic = new StructureView_BizLogic_GetAssociatedQueryEntity(
					begin, query, type,aetName);
			return doBizLogic(bizlogic);
		} else {
			return new ApplicationEntity[] {};
		}
	}

	public ApplicationEntity[] getAssociatedEntity(ApplicationEntity begin,
			String desAetName, AssociatedType type) {
		if (!structureViewLegel()) {
			return new ApplicationEntity[] {};
		}
		StructureRestraint restrain = null;
		StructureConsequence consequence = null;
		restrain = RestrainFactory.getAetNameRestrain(begin, structureModule);
		consequence = restrain.restrain();
		if (consequence.getCode().indexOf("0000") > 0) {
			StructureView_BizLogic_GetAssociatedEntity bizlogic = new StructureView_BizLogic_GetAssociatedEntity(
					begin, desAetName, type);
			return doBizLogic(bizlogic);
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
		if (!structureViewLegel()) {
			return new ApplicationEntity[] {};
		}
		StructureRestraint restrain = null;
		StructureConsequence consequence = null;
		restrain = RestrainFactory.getAetNameRestrain(begin, structureModule);
		consequence = restrain.restrain();
		if (consequence.getCode().indexOf("0000") > 0) {
			String[] aets = this.getAetNameOfAetg(aetgName);
			ApplicationEntity[] es = {};
			for (String desAetName : aets) {
				StructureView_BizLogic_GetAssociatedEntity logic = new StructureView_BizLogic_GetAssociatedEntity(
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

	public StructureModule getStructureModule() {
		return structureModule;
	}

	/**
	 * 返回源应用数据对象
	 * 
	 * @return
	 */
	public Structure getSourceStructure() {
		if (source == null) {
			return null;
		}
		if (source instanceof Structure) {
			return (Structure) source;
		} else {
			return ((StructureView) source).getSourceStructure();
		}
	}

	// private StructureModuleLibrary getStructureModuleLibrary() {
	// StructureModuleLibrary lib = null;
	// try {
	// Method method = structureModule.getClass().getDeclaredMethod(
	// "getStructureModuleLibrary", new Class[] {});
	// method.setAccessible(true);
	// Object obj = method.invoke(structureModule, new Object[] {});
	// lib = StructureModuleLibrary.class.cast(obj);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return lib;
	// }

	/**
	 * 判断结构视图的源是否存在
	 * 
	 * @return
	 */
	private boolean structureViewLegel() {
		Structure structure = this.getSourceStructure();
		if (structure == null) {
			return false;
		} else {
			return true;
		}
	}

	public List<List<ApplicationEntity>> getTraceRoad(ApplicationEntity start,
			ApplicationEntity end) {
		List<List<ApplicationEntity>> saes = this.getSourceStructure()
				.getTraceRoad(start, end);
		List<List<ApplicationEntity>> result = new ArrayList<List<ApplicationEntity>>();
		Set<String> traceRoadCache = new HashSet<String>();
		for (List<ApplicationEntity> tr : saes) {
			List<ApplicationEntity> traceRoad = new ArrayList<ApplicationEntity>();
			String cacheKey = "";
			for (ApplicationEntity ae : tr) {
				if (RestrainFactory.getAetNameRestrain(ae, structureModule)
						.restrain().getCode().indexOf("0000") > 0) {
					traceRoad.add(ae);
					cacheKey += ae.getId();
				}
			}
			if (!traceRoadCache.contains(cacheKey)) {
				result.add(traceRoad);
				traceRoadCache.add(cacheKey);
			}
		}
		return result;
	}
}
