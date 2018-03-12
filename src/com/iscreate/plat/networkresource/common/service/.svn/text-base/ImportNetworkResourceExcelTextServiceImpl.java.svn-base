package com.iscreate.plat.networkresource.common.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.Entity;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.plat.networkresource.dictionary.SearchScope;
import com.iscreate.tools.ReadExcelService;

public class ImportNetworkResourceExcelTextServiceImpl implements ImportNetworkResourceExcelTextService{

	private ReadExcelService readExcelService;
	private File file1;
	private File file2;
	private File file3;
	private File file4;
	private String file1FileName;
	private String file2FileName;
	private String file3FileName;
	private String file4FileName;
	private String message;
	static Dictionary dictionary;
	 private static final Log log = LogFactory.getLog(ImportNetworkResourceExcelTextServiceImpl.class);
	/**
	 * @param args
	 * @throws EntryOperationException
	 */
	public static void main(String[] args) throws EntryOperationException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"com/iscreate/conf/spring/*.xml");

		dictionary = ctx.getBean(Dictionary.class);
//		Entity entry0 = new Entity();
//		dictionary.addEntry("networkResourceValueEmun", entry0);
//		dictionary.addEntry("networkResourceDefination", entry0);
		List<BasicEntity> entry = dictionary.getEntry("Station,networkResourceDefination", SearchScope.OBJECT, "");
		if(entry==null){
			return;
		}
		
		BasicEntity b = entry.get(0);
		
		System.out.println(b.toMap());
		System.out.println(b.getValue("display"));

	}
	
	
	/**
	 * 对entry中英文对应表（表1）的处理
	 * 
	 * @param ls
	 * @param rowProperty
	 * @param line
	 * @param result
	 * @return
	 * @throws EntryOperationException
	 */
	public int dealwithDataInOneLineForTextOne(List ls, List rowProperty, Integer line,
			StringBuffer result) throws EntryOperationException {
		log.info("进入dealwithDataInOneLineForTextOne(List ls, List rowProperty, Integer line,StringBuffer result),ls="+ls+",rowProperty="+rowProperty+",line="+line+",result="+result+",对entry中英文对应表（表1）的处理");
		int status = 0; // 导入结果状态码
		Map<String,String> map = new HashMap<String, String>();
		String dn = "";
		for (int k = 0; k < ls.size(); k++) {
			if (rowProperty.get(k) == null) {
				result.append("第" + (k + 1) + "属性行存在错误，此行导入操作失败！\n");
				status = -1;
				break;
			}
			Object o = ls.get(k);
			if (o == null) {
				result.append("第" + line + "行第" + (k + 1)
						+ "个值存在错误，此行导入操作失败！\n");
				status = -1;
				break;
			} else {	
				
				String property = rowProperty.get(k).toString();
				String objectValue = o.toString();
				if ("Entity".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Entity为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("Entity", objectValue);
					//目录路径
				} else if ("Display".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Display为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("Display",objectValue);
				}
				
			}

			status = 0;
		}
		if(status == 0){
			dn = map.get("Entity")+",networkResourceDefination";
			boolean isOk = dictionary.hasEntry(dn);
			Entity entry = new Entity();
			entry.setValue("display", map.get("Display"));
			if(!isOk){
				//目录不存在，添加entry
				dictionary.addEntry(dn, entry);	
			}else{
				//目录存在，更新entry
				dictionary.replaceEntry(dn, entry);
			}

			result.append("第" + line + "行资源更新成功！\n");
		}else{
			result.append("第" + line + "行资源更新失败！\n");
		}
		log.info("退出dealwithDataInOneLineForTextOne(List ls, List rowProperty, Integer line,StringBuffer result),返回结果status="+status);		
		return status;
	}
	
	/**
	 * 对attribute取值与显示表（表2）的处理
	 * 
	 * @param ls
	 * @param rowProperty
	 * @param line
	 * @param result
	 * @return
	 * @throws EntryOperationException
	 */
	public int dealwithDataInOneLineForTextTwo(List ls, List rowProperty, Integer line,
			StringBuffer result) throws EntryOperationException {
		log.info("进入dealwithDataInOneLineForTextTwo(List ls, List rowProperty, Integer line,StringBuffer result),ls="+ls+",rowProperty="+rowProperty+",line="+line+",result="+result+",对attribute取值与显示表（表2）的处理");
		int status = 0; // 导入结果状态码
		Map<String,String> map = new HashMap<String, String>();
		String dn = "";
		for (int k = 0; k < ls.size(); k++) {
			if (rowProperty.get(k) == null) {
				result.append("第" + (k + 1) + "属性行存在错误，此行导入操作失败！\n");
				status = -1;
				break;
			}
			Object o = ls.get(k);
			if (o == null) {
				result.append("第" + line + "行第" + (k + 1)
						+ "个值存在错误，此行导入操作失败！\n");
				status = -1;
				break;
			} else {	
				
				String property = rowProperty.get(k).toString();
				String objectValue = o.toString();
				if ("Entity".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Entity为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("Entity", objectValue);
					//目录路径
				} else if ("Attribute".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Display为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("Attribute",objectValue);
				}else if ("Display".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Display为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("Display",objectValue);
				}else if ("OrderID".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Display为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("OrderID",objectValue);
				}else if ("IsDictionaryType".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Display为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("IsDictionaryType",objectValue);
				}else if ("DictionaryType".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						objectValue = "";
					}
					map.put("DictionaryType",objectValue);
				}else if ("GeneralEntryName".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						objectValue = "";
					}
					map.put("GeneralEntryName",objectValue);
				}
				
				
			}

			status = 0;
		}
		if(status == 0){
			dn = map.get("Attribute")+","+map.get("Entity")+",networkResourceDefination";
			boolean isOk = dictionary.hasEntry(dn);
			Entity entry = new Entity();
			entry.setValue("display", map.get("Display"));
			entry.setValue("orderID", map.get("OrderID"));
			entry.setValue("isDictionaryType", map.get("IsDictionaryType"));
			entry.setValue("dictionaryType", map.get("DictionaryType"));
			entry.setValue("generalEntryName", map.get("GeneralEntryName"));
			if(!isOk){
				//目录不存在，添加entry
				dictionary.addEntry(dn, entry);	
			}else{
				//目录存在，更新entry
				dictionary.replaceEntry(dn, entry);
			}

			result.append("第" + line + "行资源更新成功！\n");
		}else{
			result.append("第" + line + "行资源更新失败！\n");
		}
		log.info("退出dealwithDataInOneLineForTextTwo(List ls, List rowProperty, Integer line,StringBuffer result),返回结果status="+status);
		return status;
	}
	
	
	/**
	 * 对entry级别字典定义表（表3）的处理
	 * 
	 * @param ls
	 * @param rowProperty
	 * @param line
	 * @param result
	 * @return
	 * @throws EntryOperationException
	 */
	public int dealwithDataInOneLineForTextThr(List ls, List rowProperty, Integer line,
			StringBuffer result) throws EntryOperationException {
		log.info("进入dealwithDataInOneLineForTextThr(List ls, List rowProperty, Integer line,StringBuffer result),ls="+ls+",rowProperty="+rowProperty+",line="+line+",result="+result+",对entry级别字典定义表（表3）的处理");

		int status = 0; // 导入结果状态码
		Map<String,String> map = new HashMap<String, String>();
		String dn1 = "";
		String dn2 = "";
		for (int k = 0; k < ls.size(); k++) {
			if (rowProperty.get(k) == null) {
				result.append("第" + (k + 1) + "属性行存在错误，此行导入操作失败！\n");
				status = -1;
				break;
			}
			Object o = ls.get(k);
			if (o == null) {
				result.append("第" + line + "行第" + (k + 1)
						+ "个值存在错误，此行导入操作失败！\n");
				status = -1;
				break;
			} else {	
				
				String property = rowProperty.get(k).toString();
				String objectValue = o.toString();
				if ("Entity".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Entity为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("Entity", objectValue);
					//目录路径
				} else if ("Attribute".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Attribute为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("Attribute",objectValue);
				}else if ("Value".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Value为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("Value",objectValue);
				}else if ("Display".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Display为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("Display",objectValue);
				}
				
			}

			status = 0;
		}
		if(status == 0){
			//二级路径
			dn1 = map.get("Entity")+",networkResourceValueEmun"; 
			//三级路径
			dn2 = map.get("Attribute")+","+map.get("Entity")+",networkResourceValueEmun"; 
			
			boolean isOk1 = dictionary.hasEntry(dn1);
			boolean isOk2 = dictionary.hasEntry(dn2);
			
			Entity entity1 = new Entity();
			entity1.setValue("attribute",map.get("Attribute"));
			BasicEntity entityA = new BasicEntity();
			if(isOk1){
				List<BasicEntity> be = dictionary.getEntry(dn1, SearchScope.OBJECT,"");
				entityA = be.get(0);
				String attribute  =  entityA.getValue("attribute")+"";
				if(attribute.contains(map.get("Attribute"))){
					entity1.setValue("attribute", attribute);
				}else{
					entity1.setValue("attribute",attribute+","+map.get("Attribute"));
				}
			}
			BasicEntity entityB = new BasicEntity();
			Entity entity2 = new  Entity();
			
			
			if(isOk2){
				List<BasicEntity> be = dictionary.getEntry(dn2, SearchScope.OBJECT,"");
				entityB = be.get(0);
				String mixValue  =  entityB.getValue("mixValue")+"";
				
			//	if(line == 2){
				//	mixValue = "";
				//}
//				//重新导入时，如果导入值已存在则跳过
//				if(map.get("Display").toString().indexOf(mixValue)!=-1){
//					return status;
//				}
				entity2.setValue("mixValue", mixValue);
			}
			
			String vd = map.get("Value")+":"+map.get("Display");
			String ev = "";
			if(entity2.getValue("mixValue") == null||entity2.getValue("mixValue").equals("null")||entity2.getValue("mixValue").equals("")){
				ev =  "";
			}else{
				ev = entity2.getValue("mixValue")+",";
			}
				
			entity2.setValue("mixValue", ev+vd);
			if(!isOk1){
				//目录不存在，添加entry
				dictionary.addEntry(dn1, entity1);	
			}else{
				//目录存在，更新entry
				dictionary.replaceEntry(dn1, entity1);
			}
			
			if(!isOk2){
				//目录不存在，添加entry
				dictionary.addEntry(dn2, entity2);	
			}else{
				//目录存在，更新entry
				dictionary.replaceEntry(dn2, entity2);
			}
			
			result.append("第" + line + "行资源更新成功！\n");
			
		}else{
			result.append("第" + line + "行资源更新失败！\n");
		}
		log.info("退出dealwithDataInOneLineForTextThr(List ls, List rowProperty, Integer line,StringBuffer result),返回结果status="+status);

		return status;
	}
	
	/**
	 * 通用字典表（表4）的处理
	 * 
	 * @param ls
	 * @param rowProperty
	 * @param line
	 * @param result
	 * @return
	 * @throws EntryOperationException
	 */
	public int dealwithDataInOneLineForTextFou(List ls, List rowProperty, Integer line,
			StringBuffer result) throws EntryOperationException {
		log.info("进入dealwithDataInOneLineForTextFou(List ls, List rowProperty, Integer line,StringBuffer result),ls="+ls+",rowProperty="+rowProperty+",line="+line+",result="+result+",通用字典表（表4）的处理");

		int status = 0; // 导入结果状态码
		Map<String,String> map = new HashMap<String, String>();
		String dn = "";
		for (int k = 0; k < ls.size(); k++) {
			if (rowProperty.get(k) == null) {
				result.append("第" + (k + 1) + "属性行存在错误，此行导入操作失败！\n");
				status = -1;
				break;
			}
			Object o = ls.get(k);
			if (o == null) {
				result.append("第" + line + "行第" + (k + 1)
						+ "个值存在错误，此行导入操作失败！\n");
				status = -1;
				break;
			} else {	
				
				String property = rowProperty.get(k).toString();
				String objectValue = o.toString();
				if ("EntryName".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Entity为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("EntryName", objectValue);
					//目录路径
					
				} else if ("Value".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Value为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("Value",objectValue);
				}else if ("Display".equals(property.trim())) {
					if ("".equals(objectValue.trim())) {
						result.append("第" + line + "行的Display为空，此行导入操作失败！\n");
						status = -1;
						break;
					}
					map.put("Display",objectValue);
				}
				
			}

			status = 0;
		}
		if(status == 0){
			dn = map.get("EntryName")+",networkResourceValueEmun";
			boolean isOk = dictionary.hasEntry(dn);
			
			BasicEntity entityB = new BasicEntity();
			Entity entity = new  Entity();
			if(isOk){
				List<BasicEntity> be = dictionary.getEntry(dn, SearchScope.OBJECT,"");
				entityB = be.get(0);
				entity.setValue("mixValue", entityB.getValue("mixValue")+"");
			}
			
			String vd = map.get("Value")+":"+map.get("Display");
			String ev = "";
			if(entity.getValue("mixValue") == null||entity.getValue("mixValue").equals("null")){
				ev =  "";
			}else{
				ev = entity.getValue("mixValue")+",";
			}
			entity.setValue("mixValue", ev+vd);
			
			if(!isOk){
				//目录不存在，添加entry
				dictionary.addEntry(dn, entity);	
			}else{
				//目录存在，更新entry
				dictionary.replaceEntry(dn, entity);
			}

			result.append("第" + line + "行资源更新成功！\n");
		}else{
			result.append("第" + line + "行资源更新失败！\n");
		}
		log.info("退出dealwithDataInOneLineForTextFou(List ls, List rowProperty, Integer line,StringBuffer result),返回结果status="+status);

		return status;
	}
	
	/**
	 * 导入excel表
	 * @param file
	 * @param fileName
	 * @return
	 * @throws EntryOperationException 
	 */
	public String importNetworkResourceExcelText(File file, String fileName,String flag) throws EntryOperationException {
		log.info("进入importNetworkResourceExcelText(File file, String fileName,String flag)，file="+file+",fileName="+fileName+",flag="+flag+",导入excel表");
		StringBuffer result = new StringBuffer();
		if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
			result.append("导入的数据文件名应该为以为xls、xlsx结尾的Excel文件！");
			return result.toString();
		}
		Integer saveCount = 0;
		Integer updateCount = 0;
		Integer errorCount = 0;
		List<List<String>> dataList = readExcelService.getListStringRows(file,
				0);
		if (dataList == null || dataList.size() == 0 || dataList.size() == 1) {
			result.append("该Excel文件中无任何人员记录可以导入！\n");
		} else {
			// 属性行的列表
			List rowProperty = dataList.get(0);
//			dictionary.removeEntry("networkResourceDefination");
//			dictionary.addEntry("networkResourceDefination", new Entity());
			for (int j = 1; j < dataList.size(); j++) {
				List ls = dataList.get(j);	
				
				if (ls != null) {
					//处理每一行数据
					int status = -2;
					if("one".equals(flag)){
						status = this.dealwithDataInOneLineForTextOne(ls, rowProperty,
								j + 1, result);
					}else if("two".equals(flag)){
						status = this.dealwithDataInOneLineForTextTwo(ls, rowProperty,
								j + 1, result);
					}else if("thr".equals(flag)){
						status = this.dealwithDataInOneLineForTextThr(ls, rowProperty,
								j + 1, result);
					}else if("fou".equals(flag)){
						status = this.dealwithDataInOneLineForTextFou(ls, rowProperty,
								j + 1, result);
					}
					
					if (status == 0) {
						saveCount = saveCount + 1;
					} else if (status == -1) {
						errorCount = errorCount + 1;
					} else {
						updateCount = updateCount + 1;
					}
				}
			}
		}
		result.append("本次操作成功导入" + saveCount + "条记录，成功更新" + updateCount
				+ "条记录，导入失败" + errorCount + "条记录！");
		log.info("退出importNetworkResourceExcelText(File file, String fileName,String flag)，返回结果result="+result);
		return result.toString();
	}

	
	/**
	 * 导入表1类型excelAction
	 * 
	 * @throws Exception
	 * */
	public void importNetworkResourceExcelTextOneAction() throws Exception {
		log.info("进入importNetworkResourceExcelTextOneAction，导入表1类型excelAction");
		StringBuffer tishi = new StringBuffer();
		tishi.append(importNetworkResourceExcelText(file1, file1FileName,"one"));
		message = tishi.toString();
		message.replaceAll("\n", "<br>");
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", java.net.URLEncoder.encode(message, "utf-8"));
		Gson g = new Gson();
		String result = g.toJson(map);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		log.info("退出importNetworkResourceExcelTextOneAction，返回结果result="+result);
		response.getWriter().write(result);
	}
	
	/**
	 * 导入表2类型excelAction
	 * 
	 * @throws Exception
	 * */
	public void importNetworkResourceExcelTextTwoAction() throws Exception {
		log.info("进入importNetworkResourceExcelTextTwoAction，导入表2类型excelAction");
		StringBuffer tishi = new StringBuffer();
		tishi.append(importNetworkResourceExcelText(file2, file2FileName,"two"));
		message = tishi.toString();
		message.replaceAll("\n", "<br>");
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", java.net.URLEncoder.encode(message, "utf-8"));
		Gson g = new Gson();
		String result = g.toJson(map);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		log.info("退出importNetworkResourceExcelTextTwoAction，返回结果result="+result);
		response.getWriter().write(result);
	}
	
	/**
	 * 导入表3类型excelAction
	 * 
	 * @throws Exception
	 * */
	public void importNetworkResourceExcelTextThrAction() throws Exception {
		log.info("进入importNetworkResourceExcelTextThrAction，导入表3类型excelAction");
		StringBuffer tishi = new StringBuffer();
		tishi.append(importNetworkResourceExcelText(file3, file3FileName,"thr"));
		message = tishi.toString();
		message.replaceAll("\n", "<br>");
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", java.net.URLEncoder.encode(message, "utf-8"));
		Gson g = new Gson();
		String result = g.toJson(map);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		log.info("退出importNetworkResourceExcelTextThrAction，返回结果result="+result);
		response.getWriter().write(result);
	}
	
	/**
	 * 导入表4类型excelAction
	 * 
	 * @throws Exception
	 * */
	public void importNetworkResourceExcelTextFouAction() throws Exception {
		log.info("进入importNetworkResourceExcelTextFouAction，导入表4类型excelAction");
		StringBuffer tishi = new StringBuffer();
		tishi.append(importNetworkResourceExcelText(file4, file4FileName,"fou"));
		message = tishi.toString();
		message.replaceAll("\n", "<br>");
		Map<String, String> map = new HashMap<String, String>();
		map.put("message", java.net.URLEncoder.encode(message, "utf-8"));
		Gson g = new Gson();
		String result = g.toJson(map);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html");
		log.info("退出importNetworkResourceExcelTextFouAction，返回结果result="+result);
		response.getWriter().write(result);
	}


	public ReadExcelService getReadExcelService() {
		return readExcelService;
	}


	public void setReadExcelService(ReadExcelService readExcelService) {
		this.readExcelService = readExcelService;
	}

	
	
	public File getFile1() {
		return file1;
	}


	public void setFile1(File file1) {
		this.file1 = file1;
	}


	public File getFile2() {
		return file2;
	}


	public void setFile2(File file2) {
		this.file2 = file2;
	}


	public File getFile3() {
		return file3;
	}


	public void setFile3(File file3) {
		this.file3 = file3;
	}


	public File getFile4() {
		return file4;
	}


	public void setFile4(File file4) {
		this.file4 = file4;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}


	public String getFile1FileName() {
		return file1FileName;
	}


	public void setFile1FileName(String file1FileName) {
		this.file1FileName = file1FileName;
	}


	public String getFile2FileName() {
		return file2FileName;
	}


	public void setFile2FileName(String file2FileName) {
		this.file2FileName = file2FileName;
	}


	public String getFile3FileName() {
		return file3FileName;
	}


	public void setFile3FileName(String file3FileName) {
		this.file3FileName = file3FileName;
	}


	public String getFile4FileName() {
		return file4FileName;
	}


	public void setFile4FileName(String file4FileName) {
		this.file4FileName = file4FileName;
	}


	
	
}
