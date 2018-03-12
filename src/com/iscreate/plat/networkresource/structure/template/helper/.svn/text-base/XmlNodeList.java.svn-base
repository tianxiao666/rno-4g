package com.iscreate.plat.networkresource.structure.template.helper;


import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class XmlNodeList implements NodeList {
	private List<Node> nodes = new ArrayList<Node>();

	void add(Node node) {
		nodes.add(node);
	}

	public Node item(int index) {
		return nodes.get(index);
	}

	public int getLength() {
		return nodes.size();
	}

}