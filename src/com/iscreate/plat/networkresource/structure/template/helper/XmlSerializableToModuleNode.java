package com.iscreate.plat.networkresource.structure.template.helper;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.networkresource.structure.template.StructureModuleNode;

class XmlSerializableToModuleNode implements
		XmlSerializable<Collection<StructureModuleNode>> {

	private final String containTag = "contain";
	private final String childTag = "child";
	private final String linkTag = "link";
	private final String cardinalityTag = "cardinality";

	public List<StructureModuleNode> deserializable(NodeList nameNodeList) {
		List<StructureModuleNode> mns = new ArrayList<StructureModuleNode>();
		serializedNode(nameNodeList, mns);
		return mns;
	}

	private void serializedNode(NodeList nodes,
			List<StructureModuleNode> mns) {
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.hasAttributes()) {
				Node aetnode = node.getAttributes().getNamedItem(containTag);
				String aetname = aetnode.getNodeValue().trim();
				if (aetname.isEmpty()) {
					continue;
				}
				NodeList references = node.getChildNodes();
				List<StructureModuleNode> onerelated = serializedRelated(
						aetname, references);
				for (StructureModuleNode one : onerelated) {
//					String k = one.getLeftEntity() + ":" + one.getRightEntity()
//							+ ":" + one.getAssociatedType();
//					k = one.getRightEntity() + ":" + one.getLeftEntity() + ":"
//							+ one.getAssociatedType();
					mns.add(one);
				}
			}
		}
	}

	private List<StructureModuleNode> serializedRelated(String aetname,
			NodeList oneNode) {
		List<StructureModuleNode> mns = new ArrayList<StructureModuleNode>();
		for (int i = 0; i < oneNode.getLength(); i++) {
			Node reference = oneNode.item(i);
			if (reference.getFirstChild() != null) {
				String ref = reference.getNodeName().trim();
				String rightName = reference.getFirstChild().getNodeValue()
						.trim();
				if (rightName.isEmpty()) {
					continue;
				}
				if (childTag.equals(ref)) {
					StructureModuleNode mn = new StructureModuleNode();
					mn.setAssociatedType(AssociatedType.CLAN);
					mn.setLeftEntity(aetname);
					mn.setRightEntity(rightName);
					addCardinality(reference, mn);
					mns.add(mn);
				} else if (linkTag.equals(ref)) {
					StructureModuleNode mn = new StructureModuleNode();
					mn.setAssociatedType(AssociatedType.LINK);
					mn.setLeftEntity(aetname);
					mn.setRightEntity(rightName);
					addCardinality(reference, mn);
					mns.add(mn);
				}
			}
		}
		return mns;
	}

	private void addCardinality(Node reference, StructureModuleNode mn) {
		if (!reference.hasAttributes()) {
			mn.setCardinality(-1);
		} else {
			String cardinality = reference.getAttributes()
					.getNamedItem(cardinalityTag).getNodeValue().trim();
			try {
				int c = Integer.parseInt(cardinality);
				if (c > 0) {
					mn.setCardinality(c);
				} else {
					mn.setCardinality(-1);
				}
			} catch (NumberFormatException e) {
				mn.setCardinality(-1);
			}
		}
	}
}
