package com.iscreate.plat.tools.xmlhelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlServiceImpl implements XmlService
{
	public XmlServiceImpl()
	{
	}

	private String getFilePath(String file)
	{
		String filepath = "";
		boolean b = file.contains("\\");
		if (b == false)
		{
			filepath = this.getClass().getResource("XmlServiceImpl.class")
					.toString();
			filepath = filepath.substring(0, filepath.indexOf("classes"))
					+ "classes/";
			filepath = filepath + file;
		}
		//System.out.println("filePath:" + filepath);
		filepath = filepath.replace("file:", "");
		File f = new File(filepath);
		if (!f.exists())
		{
			return null;
		}
		return filepath;
	}

	public List<String> getElementValue(String file, String node)
			throws DocumentException
	{
		String filePath = "";
		filePath = getFilePath(file);
		SAXReader reader = new SAXReader();
		Document doc;
		doc = reader.read(filePath);
		List childNodes = doc.selectNodes(node);
		List<String> listNode = new ArrayList<String>();
		for (Object obj : childNodes)
		{
			Node childNode = (Node) obj;
			listNode.add(childNode.getText());
		}
		return listNode;
	}

	public String getSingleElementValue(String file, String node)
			throws DocumentException
	{
		String filePath = "";
		filePath = getFilePath(file);
		SAXReader reader = new SAXReader();
		Document doc;
		doc = reader.read(filePath);
		List childNodes = doc.selectNodes(node);
		Node childNode = (Node) childNodes.get(0);
		return childNode.getText();
	}

	public List getElementValueByParentNode(String file, String pathNode,
			String nodeName, Object obj) throws Exception
	{
		return this.getElementValueByParentNode(file, pathNode, nodeName, obj,
				"");
	}

	public List getElementValueByParentNode(String file, String pathNode,
			String nodeName, Object obj, String id) throws Exception
	{
		List list = new ArrayList();
		String filePath = "";
		filePath = getFilePath(file);
		SAXReader reader = new SAXReader();
		Document doc;
		doc = reader.read(filePath);
		Element n = (Element) doc.selectSingleNode(pathNode);
		Iterator iterator = n.elementIterator(nodeName);
		Class c = obj.getClass();
		Method[] methods = c.getDeclaredMethods();
		while (iterator.hasNext())
		{
			Element e = (Element) iterator.next();
			Attribute attribute = (Attribute) e.attribute("id");
			if (!id.trim().equals(""))
			{
				if (!id.equals(attribute.getData()))
				{
					continue;
				}
			}
			for (Method m : methods)
			{
				String name = m.getName();
				if (name.startsWith("set"))
				{
					if (name.equals("setId"))
					{
						m.invoke(obj, attribute.getData());
					}
					else
					{
						name = name.substring(3);
						name = name.substring(0, 1).toLowerCase()
								+ name.substring(1);
						m.invoke(obj, e.elementText(name));
					}
				}
			}
			list.add(obj);
			obj = c.newInstance();
		}
		return list;
	}

	public boolean modifyXmlValue(String file, String node, String value)
			throws DocumentException, IOException
	{
		String filePath = "";
		filePath = getFilePath(file);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(filePath);
		Node n = doc.selectSingleNode(node);
		n.setText(value);
		XMLWriter xWriter = new XMLWriter(new FileOutputStream(filePath));
		xWriter.write(doc);
		// xWriter.flush();
		xWriter.close();
		return true;
	}

	public boolean modifyElementAttributeValue(String file, String node,
			String value) throws DocumentException, IOException
	{
		String filePath = "";
		filePath = getFilePath(file);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(filePath);
		Node n = doc.selectSingleNode(node);
		Attribute attribute = (Attribute) n;
		attribute.setValue(value);
		XMLWriter xWriter = new XMLWriter(new FileOutputStream(filePath));
		xWriter.write(doc);
		xWriter.flush();
		xWriter.close();
		return true;
	}

	public boolean modifyObjectById(String file, String pathNode,
			String nodeName, Object obj) throws Exception
	{
		String filePath = "";
		filePath = getFilePath(file);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(filePath);
		Element n = (Element) doc.selectSingleNode(pathNode);
		Method[] methods = obj.getClass().getDeclaredMethods();
		Iterator iterator = n.elementIterator(nodeName);
		while (iterator.hasNext())
		{
			Element e = (Element) iterator.next();
			int i = 0;
			for (Method m : methods)
			{
				Attribute attribute = e.attribute("id");
				String name = m.getName();
				if (name.equals("getId"))
				{
					String id = String.valueOf(m.invoke(obj, null));
					if (!attribute.getData().equals(id))
					{
						i = 1;
					}
				}
			}
			if (i == 1)
			{
				continue;
			}
			for (Method m : methods)
			{
				String name = m.getName();
				if (name.startsWith("get") && !name.equals("getId"))
				{
					name = name.substring(3);
					name = name.substring(0, 1).toLowerCase()
							+ name.substring(1);
					String value = String.valueOf(m.invoke(obj, null));
					Element ee = e.element(name);
					ee.setText(value);
				}
			}
		}
		XMLWriter xWriter = new XMLWriter(new FileOutputStream(filePath));
		xWriter.write(doc);
		xWriter.flush();
		xWriter.close();
		return true;
	}

	public boolean addXmlNode(String file, String path, String name,
			String value) throws DocumentException, IOException
	{
		boolean b = false;
		String filePath = "";
		filePath = getFilePath(file);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(filePath);
		Element n = (Element) doc.selectSingleNode(path);
		Element newElement = n.addElement(name);
		newElement.setText(value);
		XMLWriter xWriter = new XMLWriter(new FileOutputStream(filePath));
		xWriter.write(doc);
		xWriter.flush();
		xWriter.close();
		b = true;
		return b;
	}

	public String addXmlNodeByXmlContent(String content, String path,
			String name, String value) throws DocumentException, IOException
	{
		Document doc = DocumentHelper.parseText(content);
		Element n = (Element) doc.selectSingleNode(path);
		Element newElement = n.addElement(name);
		newElement.setText(value);
		return doc.asXML();
	}

	public boolean createRootElement(String file, String rootName)
			throws DocumentException, IOException
	{
		String filePath = getFilePath(file);
		Document document = DocumentHelper.createDocument();
		document.addElement(rootName);
		XMLWriter xWriter = new XMLWriter(new FileOutputStream(filePath));
		xWriter.write(document);
		xWriter.flush();
		xWriter.close();
		return true;
	}

	public boolean addXmlNodeAttribute(String file, String path,
			String attributeName, String value) throws DocumentException,
			IOException
	{
		boolean b = false;
		String filePath = "";
		filePath = getFilePath(file);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(filePath);
		Element n = (Element) doc.selectSingleNode(path);
		n.addAttribute(attributeName, value);
		XMLWriter xWriter = new XMLWriter(new FileOutputStream(filePath));
		xWriter.write(doc);
		xWriter.flush();
		xWriter.close();
		b = true;
		return b;
	}

	public String addXmlNodeAttributeByXmlContent(String content, String path,
			String attributeName, String value) throws DocumentException,
			IOException
	{
		Document doc = DocumentHelper.parseText(content);
		Element n = (Element) doc.selectSingleNode(path);
		n.addAttribute(attributeName, value);
		return doc.asXML();
	}

	public boolean addXmlNodeByObject(String file, String path, Object obj)
			throws Exception
	{
		boolean b = false;
		String filePath = "";
		filePath = getFilePath(file);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(filePath);
		Element n = (Element) doc.selectSingleNode(path);
		String name = obj.getClass().getSimpleName();
		Element element = n.addElement(name);
		Method[] methods = obj.getClass().getDeclaredMethods();
		for (int i = 0; i < methods.length; i++)
		{
			Method m = methods[i];
			String name1 = m.getName();
			if (name1.startsWith("get"))
			{
				if (name1.equals("getId"))
				{
					element.addAttribute("id", String.valueOf(m.invoke(obj,
							null)));
				}
				else
				{
					name1 = name1.replace("get", "");
					name1 = name1.substring(0, 1).toLowerCase()
							+ name1.substring(1);
					Element e = element.addElement(name1);
					e.setText(String.valueOf(m.invoke(obj, null)));
				}
			}
		}
		XMLWriter xWriter = new XMLWriter(new FileOutputStream(filePath));
		xWriter.write(doc);
		xWriter.flush();
		xWriter.close();
		b = true;
		return b;
	}

	public boolean deleteXmlNode(String file, String path)
			throws DocumentException, IOException
	{
		boolean b = false;
		String filePath = "";
		filePath = getFilePath(file);
		SAXReader reader = new SAXReader();
		Document doc = reader.read(filePath);
		Element n = (Element) doc.selectSingleNode(path);
		Element parent = n.getParent();
		if (n != null)
		{
			parent.remove(n);
		}
		XMLWriter xWriter = new XMLWriter(new FileOutputStream(filePath));
		xWriter.write(doc);
		xWriter.flush();
		xWriter.close();
		b = true;
		return b;
	}

	public static void main(String[] args) throws Exception
	{
		System.out
				.println(new XmlServiceImpl()
						.addXmlNodeByXmlContent(
								"<?xml version='1.0' encoding='gb2312'?><SystemConfig><name>SupportConfigFile</name><comment>Thisisthe config file ofsupport</comment><LogSetting><Level>monitor</Level><path>lgs</path></LogSetting></SystemConfig>",
								"//SystemConfig/LogSetting/Level", "a", "1"));
	}
}
