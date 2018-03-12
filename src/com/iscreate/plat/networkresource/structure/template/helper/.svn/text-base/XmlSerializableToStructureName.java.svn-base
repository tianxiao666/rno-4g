package com.iscreate.plat.networkresource.structure.template.helper;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlSerializableToStructureName implements XmlSerializable<String> {

	private final String nameTag = "name";

	public String deserializable(NodeList nameNodeList) {
		if (nameNodeList.getLength() > 0) {
			Node structureNode = nameNodeList.item(0);
			if (structureNode.hasAttributes()) {
				Node nameNode = structureNode.getAttributes().getNamedItem(
						nameTag);
				if (nameNode != null) {
					String val = nameNode.getFirstChild().getNodeValue();
					if (val != null) {
						return val.trim();
					} else {
						return "";
					}
				}
			}
		}
		return "";
	}

}
