package com.iscreate.plat.networkresource.structure.template.helper;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.iscreate.plat.networkresource.structure.template.StructureModuleEntity;

class XmlSerializableToModuleEntity implements
		XmlSerializable<Map<String, StructureModuleEntity>> {

	public Map<String, StructureModuleEntity> deserializable(NodeList nameNodeList) {
		Map<String, StructureModuleEntity> mes = new HashMap<String, StructureModuleEntity>();
		if (nameNodeList.getLength() > 0) {
			for (int i = 0; i < nameNodeList.getLength(); i++) {
				Node node = nameNodeList.item(i);
				String aet = node.getFirstChild().getNodeValue().trim();
				StructureModuleEntity moduleEntity = new StructureModuleEntity();
				moduleEntity.setAet(aet);
				mes.put(aet, moduleEntity);
			}
		}
		return mes;
	}

}
