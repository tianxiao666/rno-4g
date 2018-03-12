package com.iscreate.plat.networkresource.application.tool;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;

public class XMLAEMLibrary extends BasicEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8576681312476402561L;

	// /************** 配置文件的TAG信息 ****************/
	// private final String configNodeTag = "Mappings";
	// private final String mappingNodeTag = "Mapping";
	// private final String filepathTag = "file-path";
	/************** 映射文件的TAG信息 ****************/
	private final String nameNodeTag = "name";
	private final String attributeNodeTag = "attribute";
	/*************** 正则表达式 ***********************/
	private final String regexInt = "^-?\\d+(\\.\\d+)?$";// 匹配整数
	/*********** 属性类型不合法时默认的类型 *************/
	private final String defaultAttrType = "java.lang.String";

	// private final String config = "_configuration";
	private String folder = "entitymodel";
	private String basePath;

	ModuleProvider mp;
	Log log = LogFactory.getLog(getClass());

	// public XMLAEMLibrary(String configFile) {
	// // set(config, configFile);
	// mp = new ModuleProvider(this);
	// basePath = XMLAEMLibrary.class.getResource("/").getPath() + "/"
	// + folder;
	// }

	public XMLAEMLibrary() {
		mp = new ModuleProvider(this);
		if (getClass().getClassLoader().getResource("") == null) {
			basePath = "/data/iscsi.disk2.20G/structureTest/" + folder;
		} else {
			basePath = getClass().getClassLoader().getResource("").getPath()
					+ "/" + folder;
		}
		//System.out.println("my basePath : " + basePath);
	}
	
	public XMLAEMLibrary(String fileName) {
		mp = new ModuleProvider(this);
		if (getClass().getClassLoader().getResource("") == null) {
			basePath = "/data/iscsi.disk2.20G/structureTest/" + folder;
		} else {
			basePath = getClass().getClassLoader().getResource("").getPath()
					+ "/" + folder;
		}
		//System.out.println("my basePath : " + basePath);
	}

	/**
	 * 读取XML实体对象字典池的配置文件，从中获取所有映射文件，并将它们映射成字典文件
	 */
	public void init() {
		// String configPath = searchConfigFile(new File(basePath));
		// manager.moduleConfigFilePath = configPath;
		String[] paths = getMappingFilePath(basePath);
		for (String path : paths) {
			log.debug("开始解析文件：'" + path + "'。");
			ApplicationModule mol = createModuleFromMappingFile(path);
			if (mol != null) {
				set(mol.getModuleName(), mol);
			} else {
				System.out.print("the file :'" + path + "'s");
				System.out.println("module is null.");
			}
		}
	}

	/**
	 * 从新初始化指定的字典对象。
	 * 
	 * @param moduleName
	 */
	public void init(String moduleName) {
		Map<String, String> maps = this.getMappingFiles();
		String path = maps.get(moduleName);
		ApplicationModule mol = createModuleFromMappingFile(path);
		if (mol != null) {
			// 如果模板池中没有该模板的引用，直接设值到模板池即可。
			// 如果模板池中已经存在该模板的引用，则需要将该引用的信息替换。
			// 这样可以达到已经有该模板引用的应用对象同步更新模板的效果。
			if (!this.containKey(moduleName))
				set(mol.getModuleName(), mol);
			else {
				ApplicationModule mol2 = this.getModule(moduleName);
				mol2.clear();
				for (String key : mol.keyset())
					mol2.setAttribute(mol.getAttribute(key));
			}
		}
	}

	void removeModule(String moduleName) {
		super.remove(moduleName);
	}

	/**
	 * 解析映射文件，并获取相应的数据字典对象
	 * 
	 * @param filePath
	 * @return
	 */
	ApplicationModule createModuleFromMappingFile(String filePath) {
		if (filePath == null)
			return null;
		File file = new File(filePath);
		if (!file.isFile() || filePath.indexOf(".xml") < 0) {
			log.debug("找不到文件：'" + filePath + "'");
			return null;
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			// 根据解析器工厂来获得具体的加载文档对象
			DocumentBuilder builder = factory.newDocumentBuilder();
			// 加载XML文件 路径不能是中文
			FileInputStream fis = new FileInputStream(filePath);
			Document doc = builder.parse(fis);
			NodeList nameNodeList = doc.getElementsByTagName(nameNodeTag);
			if (nameNodeList.getLength() < 0) {
				log.debug("模板文件'" + filePath + "'没有定义名称标签'" + nameNodeTag
						+ "'。");
				return null;
			}
			NodeList attributeNodeList = doc
					.getElementsByTagName(attributeNodeTag);
			if (attributeNodeList.getLength() < 0) {
				log.debug("模板文件'" + filePath + "'没有属性标签'" + attributeNodeTag
						+ "'。");
				return null;
			}
			// 创建字典对象
			String moduleName = nameNodeList.item(0).getFirstChild()
					.getNodeValue();
			log.debug("准备生成模板：'" + moduleName + "'");
			ApplicationModule am = createApplicationModuleFromNodeList(
					moduleName, attributeNodeList);
			set(moduleName, am);
			fis.close();
			return am;
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从一组nodeList中创建出一个应用数据模板
	 * 
	 * @param moduleName
	 * @param attributeNodeList
	 * @return
	 */
	private ApplicationModule createApplicationModuleFromNodeList(
			String moduleName, NodeList attributeNodeList) {
		ApplicationModule mol = new ApplicationModule(moduleName);
		for (int i = 0; i < attributeNodeList.getLength(); i++) {
			Node n = attributeNodeList.item(i);
			String attrName = n.getFirstChild().getNodeValue();
			log.debug("正在解析属性:" + attrName + "");
			AttributeModule attr = new AttributeModule(attrName);
			NamedNodeMap nnm = n.getAttributes();
			for (int j = 0; j < nnm.getLength(); j++) {
				String nodeName = nnm.item(j).getNodeName();
				String nodeValue = nnm.item(j).getNodeValue();
				if (nodeValue == null) {
					continue;
				}
				Object value = null;
				if (nodeValue.matches(regexInt)) {
					value = Integer.parseInt(nodeValue);
				} else if ("true".equals(nodeValue.toLowerCase())
						|| "false".equals(nodeValue.toLowerCase())) {
					value = Boolean.parseBoolean(nodeValue);
				} else if ("type".equals(nodeName)) {
					try {
						Class.forName(nodeValue);
					} catch (ClassNotFoundException e) {
						System.out.println(moduleName + "'s attribute: '"
								+ attrName + "', it's type is illegel.");
						value = defaultAttrType;
					}
					value = nodeValue;
				} else {
					value = nodeValue;
				}
				attr.setAttributeInfo(nodeName, value);
			}
			mol.setAttribute(attr);
		}
		return mol;
	}

	/**
	 * 获取所有字典名称及对应映射文件路径的集合
	 * 
	 * @return
	 */
	public Map<String, String> getMappingFiles() {
		Map<String, String> map = new HashMap<String, String>();
		String[] paths = getMappingFilePath(basePath);
		for (String path : paths) {
			File file = new File(path);
			if (file.isFile() && path.indexOf(".xml") > 0) {
				try {
					DocumentBuilderFactory factory = DocumentBuilderFactory
							.newInstance();
					// 根据解析器工厂来获得具体的加载文档对象
					DocumentBuilder builder = factory.newDocumentBuilder();
					// 加载XML文件 路径不能是中文
					FileInputStream fis = new FileInputStream(path);
					Document doc = builder.parse(fis);
					NodeList nodeList = doc.getElementsByTagName(nameNodeTag);
					if (nodeList.getLength() > 0) {
						String name = nodeList.item(0).getFirstChild()
								.getNodeValue();
						map.put(name, path);
					}
					fis.close();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	/**
	 * 从配置文件读取所有映射文件路径
	 * 
	 * @param filePath
	 * @return
	 */
	private String[] getMappingFilePath(String filePath) {
		File file = new File(filePath);
		File[] nextfiles = file.listFiles();
		// try {
		// if (file.isFile() && file.getPath().indexOf(".xml") > 0) {
		// DocumentBuilderFactory factory = DocumentBuilderFactory
		// .newInstance();
		// // 根据解析器工厂来获得具体的加载文档对象
		// DocumentBuilder builder = factory.newDocumentBuilder();
		// // 加载XML文件 路径不能是中文
		// log.debug("正在解析文件配置文件：" + filePath);
		// FileInputStream fis = new FileInputStream(filePath);
		// Document doc = builder.parse(fis);
		// // 获取mappings 标签
		// NodeList mappingsNodeList = doc
		// .getElementsByTagName(configNodeTag);
		// HashSet<String> filePaths = new HashSet<String>();
		// for (int i = 0; i < mappingsNodeList.getLength(); i++) {
		// Node mappingsNode = mappingsNodeList.item(i);
		// NodeList nodes = mappingsNode.getChildNodes();
		// String root = "";
		// for (int index = 0; index < nodes.getLength(); index++) {
		// Node node = nodes.item(index);
		// String tag = node.getNodeName();
		// if (filepathTag.equals(tag)) {
		// root = node.getFirstChild().getNodeValue().trim();
		// }
		// if (mappingNodeTag.equals(tag)) {
		// String path = root.isEmpty() ? node.getFirstChild()
		// .getNodeValue() : root
		// + "/"
		// + node.getFirstChild().getNodeValue()
		// .trim();
		// filePaths.add(path);
		// }
		// }
		// }
		// String[] paths = new String[filePaths.size()];
		// filePaths.toArray(paths);
		// fis.close();
		// return paths;
		// }
		// } catch (SAXException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (ParserConfigurationException e) {
		// e.printStackTrace();
		// }
		// return new String[] {};
		if (nextfiles == null || nextfiles.length == 0) {
			return new String[] {};
		}
		String[] paths = new String[nextfiles.length];
		for (int i = 0; i < nextfiles.length; i++) {
			paths[i] = nextfiles[i].getPath();
		}
		return paths;
	}

	// /**
	// * 查找XMLAEMPool的配置文件信息表
	// *
	// * @param file
	// * @return
	// */
	// private String searchConfigFile(File file) {
	// String configFile = getValue(config);
	// if (file.getPath().indexOf(configFile) >= 0) {
	// return file.getPath();
	// }
	// if (file.isFile()) {
	// return "";
	// } else if (file.isDirectory()) {
	// for (File f : file.listFiles()) {
	// String path = searchConfigFile(f);
	// if (!path.isEmpty()) {
	// return path;
	// }
	// }
	// }
	// return "";
	// }

	/**
	 * 判断xml模块池中是否包含指定字典
	 * 
	 * @param moduleName
	 * @return
	 */
	public boolean containModule(String moduleName) {
		if (containKey(moduleName)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取指定的应用数据对象模块
	 * 
	 * @param moduleName
	 * @return
	 */
	public ApplicationModule getModule(String moduleName) {
		ApplicationModule aem = getValue(moduleName);
		// ApplicationModule clone = (ApplicationModule) aem.clone();
		return aem;
	}

	/**
	 * 获取模板池中所有模板名称
	 * 
	 * @return
	 */
	public Set<String> moduleNames() {
		Set<String> set = this.keyset();
		// set.remove(config);
		return set;
	}

	public void dump() {
		File file = new File(basePath);
		log.debug("根路径是:'" + basePath + "'");
		String[] nextFiles = file.list();
		if (nextFiles == null || nextFiles.length <= 0) {
			log.debug("找不到文件根路径。");
		} else {
			log.debug("下一级文件/目录有：");
			for (String name : nextFiles) {
				log.debug(name);
			}
		}
		log.debug("");
		log.debug("当前库中包含的模板信息：");
		for (String key : this.keyset()) {
			// if (key.equals(config)) {
			// continue;
			// }
			Object o = getValue(key);
			if (o instanceof ApplicationModule) {
				ApplicationModule module = (ApplicationModule) o;
				log.debug(module.getModuleName());
			}
		}
	}
}
