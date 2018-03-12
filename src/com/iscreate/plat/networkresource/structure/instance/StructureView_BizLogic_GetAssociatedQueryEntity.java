package com.iscreate.plat.networkresource.structure.instance;

import java.util.HashMap;
import java.util.Map;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.engine.figure.Figure;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.LogicEnvironment;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.StructureBizLogic;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;

class StructureView_BizLogic_GetAssociatedQueryEntity implements
		StructureBizLogic<ApplicationEntity[]> {

	protected ApplicationEntity begin;
	//private Query query;
	protected AssociatedType type;
	private String query;
	private String aetName;
	public StructureView_BizLogic_GetAssociatedQueryEntity(
			ApplicationEntity begin, String query, AssociatedType type,String aetName) {
		this.begin = begin;
		this.query = query;
		this.type = type;
		this.aetName = aetName;
	}
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
		aes = new Structure_BizLogic_GetRecursionQueryParent(begin, query,aetName)
				.bizLogic(forStructureLogicEnvironment());
		for (ApplicationEntity ae : aes) {
			container.put(Long.toString(ae.getId()), ae);
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
		aes = new Structure_BizLogic_GetRecursionQueryEntity(begin, query,aetName)
				.bizLogic(forStructureLogicEnvironment());
		for (ApplicationEntity ae : aes) {
			container.put(Long.toString(ae.getId()), ae);
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
		aes = new Structure_BizLogic_GetAssociatedQueryEntity(begin, query,
				AssociatedType.LINK,aetName).bizLogic(forStructureLogicEnvironment());
		for (ApplicationEntity ae : aes) {
			container.put(Long.toString(ae.getId()), ae);
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
		aes = new Structure_BizLogic_GetAssociatedQueryEntity(begin, query,
				AssociatedType.LINK,aetName).bizLogic(forStructureLogicEnvironment());
		for (ApplicationEntity ae : aes) {
			container.put(Long.toString(ae.getId()), ae);
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
