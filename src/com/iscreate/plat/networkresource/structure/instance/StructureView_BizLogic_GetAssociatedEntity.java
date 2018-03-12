package com.iscreate.plat.networkresource.structure.instance;


import java.util.HashMap;
import java.util.Map;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.engine.figure.Figure;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.LogicEnvironment;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.StructureBizLogic;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;

class StructureView_BizLogic_GetAssociatedEntity implements
		StructureBizLogic<ApplicationEntity[]> {

	protected ApplicationEntity begin;
	private String desAetName;
	protected AssociatedType type;

	public StructureView_BizLogic_GetAssociatedEntity(ApplicationEntity begin,
			String desAetName, AssociatedType type) {
		this.begin = begin;
		this.desAetName = desAetName;
		this.type = type;
	}

	// 在结构视图中，与指定节点相邻的父、子、前、后应用数据对象类型
	private String[] parents = {};
	private String[] children = {};
	private String[] forwords = {};
	private String[] backwords = {};
	private LogicEnvironment logicEnv;

	public ApplicationEntity[] bizLogic(LogicEnvironment logicEnv) {
		this.logicEnv = logicEnv;
		// 结果数组
		ApplicationEntity[] aes = null;
		// 存储临时数据的列表
		HashMap<String, ApplicationEntity> entitys = new HashMap<String, ApplicationEntity>();
		// 如果没有目标应用数据对象类型，必须拿出起点应用数据对象类型的所有相邻类型
		switch (type) {
		case CLAN:
			this.getParentAssociateEntity(entitys);
			this.getChildAssociateEntity(entitys);
			aes = new ApplicationEntity[entitys.size()];
			entitys.values().toArray(aes);
			break;
		case PARENT:
			this.getParentAssociateEntity(entitys);
			aes = new ApplicationEntity[entitys.size()];
			entitys.values().toArray(aes);
			break;
		case CHILD:
			this.getChildAssociateEntity(entitys);
			aes = new ApplicationEntity[entitys.size()];
			entitys.values().toArray(aes);
			break;
		case LINK:
			this.getForwordAssociateEntity(entitys);
			this.getBackwordAssociateEntity(entitys);
			aes = new ApplicationEntity[entitys.size()];
			entitys.values().toArray(aes);
			break;
		case FORWORD:
			this.getForwordAssociateEntity(entitys);
			aes = new ApplicationEntity[entitys.size()];
			entitys.values().toArray(aes);
			break;
		case BACKWORD:
			this.getBackwordAssociateEntity(entitys);
			aes = new ApplicationEntity[entitys.size()];
			entitys.values().toArray(aes);
			break;
		case ALL:
			this.getParentAssociateEntity(entitys);
			this.getChildAssociateEntity(entitys);
			this.getForwordAssociateEntity(entitys);
			this.getBackwordAssociateEntity(entitys);
			aes = new ApplicationEntity[entitys.size()];
			entitys.values().toArray(aes);
			break;
		}
		return aes;
	}

	/**
	 * 递归获取父节点
	 * 
	 * @param container
	 */
	private void getParentAssociateEntity(
			Map<String, ApplicationEntity> container) {
		ApplicationEntity[] aes = {};
		if (desAetName == null || desAetName.trim().isEmpty()) {
			// 如果下一类型列表的长度为0，从结构视图中获取下一类型的列表
			if (parents.length == 0) {
				StructureView structureView = logicEnv.get(StructureView.class);
				String currentAetName = begin.getType();
				parents = structureView.getAssociatedAetName(currentAetName,
						AssociatedType.PARENT);
			}
			for (String paetName : parents) {
				// 使用的是  源  去查询数据时，当前的logEnv上存在的是结构视图的结构模板，
				// 它不能用于制作  源  的查询方案。因此需要为源的查询重新生成一个logEnv
				//  forStructureLogicEnvironment()
				aes = new Structure_BizLogic_GetRecursionParent(begin, paetName)
						.bizLogic(forStructureLogicEnvironment());
				for (ApplicationEntity ae : aes) {
					container.put(Long.toString(ae.getId()), ae);
				}
			}
		} else {
			aes = new Structure_BizLogic_GetRecursionParent(begin, desAetName)
					.bizLogic(forStructureLogicEnvironment());
			for (ApplicationEntity ae : aes) {
				container.put(Long.toString(ae.getId()), ae);
			}
		}
	}

	/**
	 * 递归获取子节点
	 * 
	 * @param container
	 */
	private void getChildAssociateEntity(
			Map<String, ApplicationEntity> container) {
		ApplicationEntity[] aes = {};
		// 类型存在
		if (desAetName == null || desAetName.trim().isEmpty()) {
			// 如果下一类型列表的长度为0，从结构视图中获取下一类型的列表
			if (children.length == 0) {
				StructureView structureView = logicEnv.get(StructureView.class);
				String currentAetName = begin.getType();
				children = structureView.getAssociatedAetName(currentAetName,
						AssociatedType.CHILD);
			}
			for (String caetName : children) {
				// 使用的是  源  去查询数据时，当前的logEnv上存在的是结构视图的结构模板，
				// 它不能用于制作  源  的查询方案。因此需要为源的查询重新生成一个logEnv
				//  forStructureLogicEnvironment()
				aes = new Structure_BizLogic_GetRecursionEntity(begin, caetName)
						.bizLogic(forStructureLogicEnvironment());
				for (ApplicationEntity ae : aes) {
					container.put(Long.toString(ae.getId()), ae);
				}
			}
		} else {
			aes = new Structure_BizLogic_GetRecursionEntity(begin, desAetName)
					.bizLogic(forStructureLogicEnvironment());
			for (ApplicationEntity ae : aes) {
				container.put(Long.toString(ae.getId()), ae);
			}
		}
	}

	/**
	 * 递归获取前一节点
	 * 
	 * @param container
	 */
	private void getForwordAssociateEntity(
			Map<String, ApplicationEntity> container) {
		ApplicationEntity[] aes = {};
		// 类型存在
		if (desAetName == null || desAetName.trim().isEmpty()) {
			// 如果下一类型列表的长度为0，从结构视图中获取下一类型的列表
			if (forwords.length == 0) {
				StructureView structureView = logicEnv.get(StructureView.class);
				String currentAetName = begin.getType();
				forwords = structureView.getAssociatedAetName(currentAetName,
						AssociatedType.FORWORD);
			}
			for (String faetName : forwords) {
				// 使用的是  源  去查询数据时，当前的logEnv上存在的是结构视图的结构模板，
				// 它不能用于制作  源  的查询方案。因此需要为源的查询重新生成一个logEnv
				//  forStructureLogicEnvironment()
				aes = new Structure_BizLogic_GetAssociatedEntity(begin,
						faetName, AssociatedType.LINK)
						.bizLogic(forStructureLogicEnvironment());
				for (ApplicationEntity ae : aes) {
					container.put(Long.toString(ae.getId()), ae);
				}
			}
		} else {
			aes = new Structure_BizLogic_GetAssociatedEntity(begin, desAetName,
					AssociatedType.LINK)
					.bizLogic(forStructureLogicEnvironment());
			for (ApplicationEntity ae : aes) {
				container.put(Long.toString(ae.getId()), ae);
			}
		}
	}

	/**
	 * 递归获取后一节点
	 * 
	 * @param container
	 */
	private void getBackwordAssociateEntity(
			Map<String, ApplicationEntity> container) {
		ApplicationEntity[] aes = {};
		// 类型存在
		if (desAetName == null || desAetName.trim().isEmpty()) {
			// 如果下一类型列表的长度为0，从结构视图中获取下一类型的列表
			if (backwords.length == 0) {
				StructureView structureView = logicEnv.get(StructureView.class);
				String currentAetName = begin.getType();
				backwords = structureView.getAssociatedAetName(currentAetName,
						AssociatedType.BACKWORD);
			}
			for (String baetName : backwords) {
				// 使用的是  源  去查询数据时，当前的logEnv上存在的是结构视图的结构模板，
				// 它不能用于制作  源  的查询方案。因此需要为源的查询重新生成一个logEnv
				//  forStructureLogicEnvironment()
				aes = new Structure_BizLogic_GetAssociatedEntity(begin,
						baetName, AssociatedType.LINK)
						.bizLogic(forStructureLogicEnvironment());
				for (ApplicationEntity ae : aes) {
					container.put(Long.toString(ae.getId()), ae);
				}
			}
		} else {
			aes = new Structure_BizLogic_GetAssociatedEntity(begin, desAetName,
					AssociatedType.LINK)
					.bizLogic(forStructureLogicEnvironment());
			for (ApplicationEntity ae : aes) {
				container.put(Long.toString(ae.getId()), ae);
			}
		}
	}

	/**
	 * 构建业务逻辑的执行环境
	 * 
	 * @return
	 */
	private LogicEnvironment forStructureLogicEnvironment() {
		BasicLogicEnvironment env = new BasicLogicEnvironment(
				logicEnv.get(ContextFactory.class));
		env.set(logicEnv.get(Figure.class));
		env.set(logicEnv.get(Structure.class));
		env.set(logicEnv.get(Structure.class).getStructureModule());
		return env;
	}
}
