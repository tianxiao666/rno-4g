package com.iscreate.plat.networkresource.structure.template.helper;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntity;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntityGroup;
import com.iscreate.plat.networkresource.structure.template.StructureModuleNode;


public class StructureModuleSerializable {

	private final XmlSerializableToStructureName toStructureName = new XmlSerializableToStructureName();
	private final XmlSerializableToModuleEntity toModuleEntity = new XmlSerializableToModuleEntity();
	private final XmlSerializableToModuleEntityGroup toModuleEntityGroup = new XmlSerializableToModuleEntityGroup();
	private final XmlSerializableToModuleNode toModuleNode = new XmlSerializableToModuleNode();
	private final XmlSerializableToViews toView = new XmlSerializableToViews();

	public StructureModule deserializable(File file) {
		if (file == null) {
			return null;
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// 根据解析器工厂来获得具体的加载文档对象
		try {
			InputStream inputStream = new FileInputStream(file);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputStream);
			NodeList basicNodeList = XmlSerializableNodeFilter
					.getStructureNodeList(doc);
			Node basicNode = basicNodeList.item(0);
			String structureName = toStructureName
					.deserializable(basicNodeList);
			if (structureName.isEmpty()) {
				return null;
			}
			StructureModule structureModule = new StructureModule(structureName);
			Map<String, StructureModuleEntity> mes = toModuleEntity
					.deserializable(XmlSerializableNodeFilter
							.getModuleEntityNodeList(basicNode));
			Map<String, StructureModuleEntityGroup> megs = toModuleEntityGroup
					.deserializable(XmlSerializableNodeFilter
							.getModuleEntityGroupNodeList(basicNode));
			Collection<StructureModuleNode> mns = toModuleNode
					.deserializable(XmlSerializableNodeFilter
							.getModulenodeNodeList(basicNode));
			inputStream.close();
			// 使用StructureModuleBuilder去创建StructureModule
			for (StructureModuleEntity me : mes.values()) {
				structureModule.increaseEntity(me);
			}
			for (StructureModuleEntityGroup meg : megs.values()) {
				structureModule.increaseEntityGroup(meg);
			}
			for (StructureModuleNode mn : mns) {
				structureModule.increaseRelation(mn);
			}
			// 构建视图
			toView.setSerializor(toModuleEntityGroup, toModuleNode,
					structureModule);
			toView.deserializable(XmlSerializableNodeFilter
					.getViewNodeList(basicNode));
			return structureModule;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void serializable(StructureModule module) {
		// TODO
	}
}
