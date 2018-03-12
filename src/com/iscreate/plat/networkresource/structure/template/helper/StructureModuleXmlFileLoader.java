package com.iscreate.plat.networkresource.structure.template.helper;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StructureModuleXmlFileLoader {

	private final String basicPath = "entityStructure";
	String path = "";

	public File[] listMappingFile() {
		if (getClass().getClassLoader().getResource("") == null) {
			path = "/data/iscsi.disk2.20G/structureTest/" + basicPath;
		} else {
			path = getClass().getClassLoader().getResource("").getPath() + "/"
					+ basicPath;
		}
		File file = new File(path);
		List<File> myfiles = new ArrayList<File>();
		listMappingFile(file, myfiles);
		File[] result = new File[myfiles.size()];
		myfiles.toArray(result);
		return result;
	}

	/**
	 * 从父目录parent下获取所有的XML文件，放入myfiles列表中
	 * 
	 * @param parent
	 * @param myfiles
	 */
	private void listMappingFile(File parent, List<File> myfiles) {
		if (parent == null) {
			return;
		}
		if (parent.isDirectory()) {
			File[] nextFiles = parent.listFiles();
			for (int i = 0; i < nextFiles.length; i++) {
				listMappingFile(nextFiles[i], myfiles);
			}
		} else {
			String path = parent.getPath();
			if (path.indexOf(".xml") > 0
					&& (path.indexOf(".xml") == path.length() - 4)) {
				myfiles.add(parent);
			}
		}
	}
}
