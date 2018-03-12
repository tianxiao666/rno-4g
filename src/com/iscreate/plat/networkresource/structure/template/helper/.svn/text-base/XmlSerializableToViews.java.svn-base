package com.iscreate.plat.networkresource.structure.template.helper;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntity;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntityGroup;
import com.iscreate.plat.networkresource.structure.template.StructureModuleNode;

class XmlSerializableToViews implements XmlSerializable<Void> {
	private XmlSerializableToModuleEntityGroup toModuleEntityGroup;
	private XmlSerializableToModuleNode toModuleNode;
	private StructureModule basicModule;

	public void setSerializor(
			XmlSerializableToModuleEntityGroup toModuleEntityGroup,
			XmlSerializableToModuleNode toModuleNode,
			StructureModule basicModule) {
		this.toModuleEntityGroup = toModuleEntityGroup;
		this.toModuleNode = toModuleNode;
		this.basicModule = basicModule;
	}

	public Void deserializable(NodeList nameNodeList) {
		for (int index = 0; index < nameNodeList.getLength(); index++) {
			Node viewNode = nameNodeList.item(index);
			deserializableAsView(viewNode);
		}
		return null;
	}

	private String viewnameTag = "viewname";

	private void deserializableAsView(Node viewNode) {
		Node viewnode = viewNode.getAttributes().getNamedItem(viewnameTag);
		if (viewnode == null) {
			return;
		}
		// 获取视图的名称
		String viewname = viewnode.getNodeValue();
		// 获取视图中的应用数据对象组
		Map<String, StructureModuleEntityGroup> megs = toModuleEntityGroup
				.deserializable(XmlSerializableNodeFilter
						.getModuleEntityGroupNodeList(viewNode));
		// 与主结构模板中的组合并(视图可以继承主结构模板中的aetg)
//		megs.putAll(basicModule.getModuleEntityGroups());
		// 获取视图中定义的关联节点信息
		List<StructureModuleNode> mns = toModuleNode
				.deserializable(XmlSerializableNodeFilter
						.getModulenodeNodeList(viewNode));
		// 临时的structureModule对象，需要使用其increase方法去创建必要的关联节点
		StructureModule t = new StructureModule(viewname);
		for (StructureModuleEntity me : basicModule.getModuleEntitys().values()) {
			t.increaseEntity(me);
		}
		for (StructureModuleEntityGroup meg : megs.values()) {
			t.increaseEntityGroup(meg);
		}
		for(StructureModuleEntityGroup meg : basicModule.getModuleEntityGroups().values()){
			t.increaseEntityGroup(meg);
		}
		for (StructureModuleNode mn : mns) {
			t.increaseRelation(mn);
		}
		// 去除view中未引用定义信息
		Map<String, StructureModuleEntity> vae = new HashMap<String, StructureModuleEntity>();
		Map<String,StructureModuleEntityGroup>vmegs = new HashMap<String, StructureModuleEntityGroup>();
		// 从关联节点中查找aet及aetg
		for (StructureModuleNode node : t.getModuleNodes()) {
			String left = node.getLeftEntity();
			String right = node.getRightEntity();
			if (t.getModuleEntitys().containsKey(left))
				vae.put(left, t.getModuleEntitys().get(left));
			if (t.getModuleEntitys().containsKey(right))
				vae.put(right, t.getModuleEntitys().get(right));
			if (t.getModuleEntityGroups().containsKey(left))
				vmegs.put(left, t.getModuleEntityGroups().get(left));
			if (t.getModuleEntityGroups().containsKey(right))
				vmegs.put(right, t.getModuleEntityGroups().get(right));
		}
		// 从aetg中查找aet
		for(StructureModuleEntityGroup group : megs.values()){
			Set<String> aetNames = new HashSet<String>();
			searchAetgsAet(group.getAetgname(),new HashSet<String>(),aetNames,t.getModuleEntityGroups());
			for(String aetName: aetNames){
				vae.put(aetName, t.getModuleEntitys().get(aetName));
			}
		}
		vmegs.putAll(megs);
		// 最后的view对象
		StructureModule view = new StructureModule(viewname);
		// 使用反射的方法，将这些值的引用放入view中
		try {
			Method method = view.getClass().getDeclaredMethod(
					"setModuleEntitys", new Class[] { Map.class });
			method.setAccessible(true);
			method.invoke(view, new Object[] { vae });
			method = view.getClass().getDeclaredMethod("setModuleEntityGroups",
					new Class[] { Map.class });
			method.setAccessible(true);
			method.invoke(view, new Object[] { megs });
			method = view.getClass().getDeclaredMethod("setModuleNodes",
					new Class[] { Collection.class });
			method.setAccessible(true);
			method.invoke(view, new Object[] { t.getModuleNodes() });
		} catch (Exception e) {
			e.printStackTrace();
		}
		basicModule.increaseView(view);
	}
	
	private void searchAetgsAet(String aetgName, Set<String> cache,
			Set<String> aetNames,Map<String,StructureModuleEntityGroup> groups) {
		if (cache.contains(aetgName)) {
			return;
		}
		cache.add(aetgName);
		StructureModuleEntityGroup group = groups.get(aetgName);
		if (group != null) {
			List<String> names = group.getAets();
			for (String name : names) {
				if (groups.containsKey(name)) {
					searchAetgsAet(name, cache, aetNames,groups);
				} else {
					aetNames.add(name);
				}
			}
		}
	}

	public static void main(String[] args) {
		File file = new StructureModuleXmlFileLoader().listMappingFile()[0];
		new StructureModuleSerializable().deserializable(file);
	}
}
