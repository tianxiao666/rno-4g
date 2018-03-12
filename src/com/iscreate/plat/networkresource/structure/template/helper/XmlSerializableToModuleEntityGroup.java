package com.iscreate.plat.networkresource.structure.template.helper;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.iscreate.plat.networkresource.structure.template.StructureModuleEntityGroup;

class XmlSerializableToModuleEntityGroup implements
		XmlSerializable<Map<String, StructureModuleEntityGroup>> {
	private final String aetgnameTag = "aetgname";

	public Map<String, StructureModuleEntityGroup> deserializable(
			NodeList nameNodeList) {
		Map<String, StructureModuleEntityGroup> megs = new HashMap<String, StructureModuleEntityGroup>();
		Map<String, StructureModuleEntityGroup> mymegs = serializedEntitygroup(nameNodeList);
		for (String key : mymegs.keySet()) {
			megs.put(key, mymegs.get(key));
		}
		return megs;
	}

	private Map<String, StructureModuleEntityGroup> serializedEntitygroup(
			NodeList groupnodes) {
		Map<String, StructureModuleEntityGroup> megs = new HashMap<String, StructureModuleEntityGroup>();
		for (int i = 0; i < groupnodes.getLength(); i++) {
			Node groupnode = groupnodes.item(i);
			if (groupnode.hasAttributes()) {
				Node aetgnamenode = groupnode.getAttributes().getNamedItem(
						aetgnameTag);
				String aetgname = aetgnamenode.getNodeValue().trim();
				if (aetgname.isEmpty()) {
					continue;
				}
				StructureModuleEntityGroup meg = new StructureModuleEntityGroup();
				meg.setAetgname(aetgname);
				megs.put(aetgname, meg);
				NodeList references = groupnode.getChildNodes();
				serializedReference(references, meg);
			}
		}
		return megs;
	}

	private void serializedReference(NodeList references,
			StructureModuleEntityGroup meg) {
		for (int i = 0; i < references.getLength(); i++) {
			Node reference = references.item(i);
			if (reference.getFirstChild() != null) {
				String ref = reference.getFirstChild().getNodeValue().trim();
				meg.addAet(ref);
			}
		}
	}
}
