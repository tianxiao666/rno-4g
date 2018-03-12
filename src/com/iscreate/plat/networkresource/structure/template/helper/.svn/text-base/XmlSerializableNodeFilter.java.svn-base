package com.iscreate.plat.networkresource.structure.template.helper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlSerializableNodeFilter {

	/**
	 * 
	 * @param doc
	 * @return
	 */
	public static NodeList getStructureNodeList(Document doc) {
		String structureTag = "structure";
		NodeList nameNodeList = doc.getElementsByTagName(structureTag);
		return nameNodeList;
	}

	/**
	 * 从doc中抽取所有的entity节点放入NodeList中
	 * 
	 * @param doc
	 * @return
	 */
	public static NodeList getModuleEntityNodeList(Node basicNode) {
		String entitiesTag = "entities";
		String entityTag = "entity";
		NodeList list = basicNode.getChildNodes();
		XmlNodeList resultList = new XmlNodeList();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (entitiesTag.equals(node.getNodeName())) {
				NodeList aetgList = node.getChildNodes();
				for (int index = 0; index < aetgList.getLength(); index++) {
					if (entityTag.equals(aetgList.item(index).getNodeName())) {
						resultList.add(aetgList.item(index));
					}
				}
				return resultList;
			}
		}
		return resultList;
	}

	/**
	 * 从structure下的entitygroups标签中，将entitygroup抽取出来，放入NodeList中
	 * 
	 * @param doc
	 * @return
	 */
	public static NodeList getModuleEntityGroupNodeList(Node basicNode) {
		String entitygroupsTag = "entitygroups";
		String entitygroupTag = "entitygroup";
		NodeList list = basicNode.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (entitygroupsTag.equals(node.getNodeName())) {
				XmlNodeList resultList = new XmlNodeList();
				NodeList aetgList = node.getChildNodes();
				for (int index = 0; index < aetgList.getLength(); index++) {
					if (entitygroupTag.equals(aetgList.item(index)
							.getNodeName())) {
						resultList.add(aetgList.item(index));
					}
				}
				return resultList;
			}
		}
		return new XmlNodeList();
	}

	/**
	 * 从structure下的nodes标签中，将node抽取出来，放入NodeList中
	 * 
	 * @param doc
	 * @return
	 */
	public static NodeList getModulenodeNodeList(Node basicNode) {
		String nodesTag = "nodes";
		String nodeTag = "node";
		NodeList list = basicNode.getChildNodes();
		XmlNodeList resultList = new XmlNodeList();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (nodesTag.equals(node.getNodeName())) {
				NodeList nodeList = node.getChildNodes();
				for (int index = 0; index < nodeList.getLength(); index++) {
					if (nodeTag.equals(nodeList.item(index).getNodeName())) {
						resultList.add(nodeList.item(index));
					}
				}
				return resultList;
			}
		}
		return resultList;
	}

	/**
	 * 从structure下的views标签中，将view抽取出来，放入NodeList中
	 * 
	 * @param doc
	 * @return
	 */
	public static NodeList getViewNodeList(Node basicNode) {
		String viewsTag = "views";
		String viewTag = "view";
		NodeList list = basicNode.getChildNodes();
		XmlNodeList resultList = new XmlNodeList();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (viewsTag.equals(node.getNodeName())) {
				NodeList nodeList = node.getChildNodes();
				for (int index = 0; index < nodeList.getLength(); index++) {
					if (viewTag.equals(nodeList.item(index).getNodeName())) {
						resultList.add(nodeList.item(index));
					}
				}
				return resultList;
			}
		}
		return resultList;
	}
}
