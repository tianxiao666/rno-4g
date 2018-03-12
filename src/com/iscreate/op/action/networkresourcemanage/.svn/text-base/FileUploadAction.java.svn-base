package com.iscreate.op.action.networkresourcemanage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.constant.OperationType;
import com.iscreate.op.pojo.maintain.ResourceMaintenance;
import com.iscreate.op.service.maintain.NetworkResourceMaintenanceService;
import com.iscreate.op.service.networkresourcemanage.ImportService;
import com.iscreate.op.service.networkresourcemanage.PhysicalresService;
import com.iscreate.op.service.networkresourcemanage.StaffOrganizationService;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
import com.iscreate.plat.networkresource.application.tool.XMLAEMLibrary;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.QuickSort;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.plat.networkresource.dictionary.SearchScope;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.tools.ReadExcelService;

public class FileUploadAction {
	private static final Log log = LogFactory.getLog(FileUploadAction.class);
	private PhysicalresService physicalresService;
	private StructureCommonService structureCommonService;
	private ReadExcelService readExcelService;
	private Dictionary dictionary;
	private XMLAEMLibrary moduleLibrary;
	
	private File file;
	private String fileFileName;
	private String fileContentType;
	
	/*上传文件隶属类型和id*/
	private String assEntityId;
	private String assEntityType;
	//导入资源的id
	private String importEntityId;
	//导入资源的类型
	private String importEntityType;
	//隶属资源绑定模式
	private String rdoAssModel;
	//隶属资源组合类型，如Station_parent，用来获取需要绑定的关系
	private String selectResChosenType;
	//需要精确匹配字段的字段名
	private String selectAttrExactMatch;
	//需要模糊匹配字段的字段名
	private String selectAttrIndistinctMatch;
	//需要导入的entity的map集合
	private List<Map<String, Object>>  resultMapList;
	//需要导入的entity的隶属资源的map集合
	private List<List<Map<String, Object>>> assListMapList;
	//中文属性集合
	private Map<String, Object> chineseMap;
	//大集合，套着中文key集合和entity集合
	private Map<String, Object> bigResultMap;
	//资源的关系类型
	private String assType;
	//隶属资源名
	private String selectResChosenTypeName;
	private QuickSort<Map<String,Object>> quickSort;//排序
	private PhysicalresAction physicalresAction;//物理资源相关action
	//导出文件的输入流
	private InputStream resultInputStream;
	//资源导入模板文件名
	private String moduleFileName;
	
	private String uploadType;//上传附件或图片
	
	private String importModel; //导入模式
	
	private String attributeChange;//更新导入或删除重建导入模式的属性匹配
	
	private List<List<Map<String,Object>>> entityHaveMapList;//数据库已存在的entity list
	
	private List<List<Map<String,Object>>> hasChildMapList;//资源实例是否有子资源
	
	private String entityMap;//待更新的entitymap
	
	private String updateEntityId;//更新资源的id

	private String hasEntity;//更新导入模式时数据库是否有记录
	
	private String chosenType;//选择导入的资源类型
	
	
	private String opScene = "";//操作场景
	
	private String opCause = "";//操作原因
	
	private int sheetIndex;//选中sheet下标
	
	
	
	private NetworkResourceMaintenanceService networkResourceMaintenanceService;
	
	private String areaId;
	
	private String matchContent;//匹配内容
	
	private List<Map<String,Object>> entityHaveMap;//是否有记录map list
	
	private List<Map<String,Object>> hasChildMap;//是否有子资源map list
	
	private String importMaps;
	private String importRecord;
	private String rowNums;
	private String assRecord;
	
    private StaffOrganizationService staffOrganizationService;
	
    private ImportService importService;
    
	public ImportService getImportService() {
		return importService;
	}
	public void setImportService(ImportService importService) {
		this.importService = importService;
	}
	public StaffOrganizationService getStaffOrganizationService() {
		return staffOrganizationService;
	}
	public void setStaffOrganizationService(
			StaffOrganizationService staffOrganizationService) {
		this.staffOrganizationService = staffOrganizationService;
	}

	
	public String getImportMaps() {
		return importMaps;
	}

	public void setImportMaps(String importMaps) {
		this.importMaps = importMaps;
	}

	public String getImportRecord() {
		return importRecord;
	}

	public void setImportRecord(String importRecord) {
		this.importRecord = importRecord;
	}

	public String getRowNums() {
		return rowNums;
	}

	public void setRowNums(String rowNums) {
		this.rowNums = rowNums;
	}

	public String getAssRecord() {
		return assRecord;
	}

	public void setAssRecord(String assRecord) {
		this.assRecord = assRecord;
	}
	
	public List<Map<String, Object>> getEntityHaveMap() {
		return entityHaveMap;
	}

	public void setEntityHaveMap(List<Map<String, Object>> entityHaveMap) {
		this.entityHaveMap = entityHaveMap;
	}

	public List<Map<String, Object>> getHasChildMap() {
		return hasChildMap;
	}

	public void setHasChildMap(List<Map<String, Object>> hasChildMap) {
		this.hasChildMap = hasChildMap;
	}

	public String getMatchContent() {
		return matchContent;
	}

	public void setMatchContent(String matchContent) {
		this.matchContent = matchContent;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public NetworkResourceMaintenanceService getNetworkResourceMaintenanceService() {
		return networkResourceMaintenanceService;
	}

	public void setNetworkResourceMaintenanceService(
			NetworkResourceMaintenanceService networkResourceMaintenanceService) {
		this.networkResourceMaintenanceService = networkResourceMaintenanceService;
	}

	public String getOpScene() {
		return opScene;
	}

	public void setOpScene(String opScene) {
		this.opScene = opScene;
	}

	public String getOpCause() {
		return opCause;
	}

	public void setOpCause(String opCause) {
		this.opCause = opCause;
	}



	public String getChosenType() {
		return chosenType;
	}

	public void setChosenType(String chosenType) {
		this.chosenType = chosenType;
	}

	public String getHasEntity() {
		return hasEntity;
	}

	public void setHasEntity(String hasEntity) {
		this.hasEntity = hasEntity;
	}

	public String getUpdateEntityId() {
		return updateEntityId;
	}

	public void setUpdateEntityId(String updateEntityId) {
		this.updateEntityId = updateEntityId;
	}

	public String getEntityMap() {
		return entityMap;
	}

	public void setEntityMap(String entityMap) {
		this.entityMap = entityMap;
	}

	public List<List<Map<String, Object>>> getHasChildMapList() {
		return hasChildMapList;
	}

	public void setHasChildMapList(List<List<Map<String, Object>>> hasChildMapList) {
		this.hasChildMapList = hasChildMapList;
	}

	public List<List<Map<String, Object>>> getEntityHaveMapList() {
		return entityHaveMapList;
	}

	public void setEntityHaveMapList(
			List<List<Map<String, Object>>> entityHaveMapList) {
		this.entityHaveMapList = entityHaveMapList;
	}

	public String getAttributeChange() {
		return attributeChange;
	}

	public void setAttributeChange(String attributeChange) {
		this.attributeChange = attributeChange;
	}

	public String getImportModel() {
		return importModel;
	}

	public void setImportModel(String importModel) {
		this.importModel = importModel;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public InputStream getResultInputStream() {
		return resultInputStream;
	}

	public void setResultInputStream(InputStream resultInputStream) {
		this.resultInputStream = resultInputStream;
	}

	public String getModuleFileName() {
		return moduleFileName;
	}

	public void setModuleFileName(String moduleFileName) {
		this.moduleFileName = moduleFileName;
	}

	public PhysicalresAction getPhysicalresAction() {
		return physicalresAction;
	}

	public void setPhysicalresAction(PhysicalresAction physicalresAction) {
		this.physicalresAction = physicalresAction;
	}

	public QuickSort<Map<String, Object>> getQuickSort() {
		return quickSort;
	}

	public void setQuickSort(QuickSort<Map<String, Object>> quickSort) {
		this.quickSort = quickSort;
	}

	public String getSelectResChosenTypeName() {
		return selectResChosenTypeName;
	}

	public void setSelectResChosenTypeName(String selectResChosenTypeName) {
		this.selectResChosenTypeName = selectResChosenTypeName;
	}

	public String getAssType() {
		return assType;
	}

	public void setAssType(String assType) {
		this.assType = assType;
	}

	public String getImportEntityId() {
		return importEntityId;
	}

	public void setImportEntityId(String importEntityId) {
		this.importEntityId = importEntityId;
	}

	public List<List<Map<String, Object>>> getAssListMapList() {
		return assListMapList;
	}

	public void setAssListMapList(List<List<Map<String, Object>>> assListMapList) {
		this.assListMapList = assListMapList;
	}

	public String getSelectAttrExactMatch() {
		return selectAttrExactMatch;
	}

	public void setSelectAttrExactMatch(String selectAttrExactMatch) {
		this.selectAttrExactMatch = selectAttrExactMatch;
	}

	public String getSelectAttrIndistinctMatch() {
		return selectAttrIndistinctMatch;
	}

	public void setSelectAttrIndistinctMatch(String selectAttrIndistinctMatch) {
		this.selectAttrIndistinctMatch = selectAttrIndistinctMatch;
	}

	public Map<String, Object> getChineseMap() {
		return chineseMap;
	}

	public void setChineseMap(Map<String, Object> chineseMap) {
		this.chineseMap = chineseMap;
	}

	public Map<String, Object> getBigResultMap() {
		return bigResultMap;
	}

	public void setBigResultMap(Map<String, Object> bigResultMap) {
		this.bigResultMap = bigResultMap;
	}

	public List<Map<String, Object>> getResultMapList() {
		return resultMapList;
	}

	public void setResultMapList(List<Map<String, Object>> resultMapList) {
		this.resultMapList = resultMapList;
	}

	public XMLAEMLibrary getModuleLibrary() {
		return moduleLibrary;
	}

	public void setModuleLibrary(XMLAEMLibrary moduleLibrary) {
		this.moduleLibrary = moduleLibrary;
	}

	public String getImportEntityType() {
		return importEntityType;
	}

	public void setImportEntityType(String importEntityType) {
		this.importEntityType = importEntityType;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String getSelectResChosenType() {
		return selectResChosenType;
	}

	public void setSelectResChosenType(String selectResChosenType) {
		this.selectResChosenType = selectResChosenType;
	}

	public String getRdoAssModel() {
		return rdoAssModel;
	}

	public void setRdoAssModel(String rdoAssModel) {
		this.rdoAssModel = rdoAssModel;
	}

	public String getAssEntityId() {
		return assEntityId;
	}

	public void setAssEntityId(String assEntityId) {
		this.assEntityId = assEntityId;
	}

	public String getAssEntityType() {
		return assEntityType;
	}

	public void setAssEntityType(String assEntityType) {
		this.assEntityType = assEntityType;
	}

	public ReadExcelService getReadExcelService() {
		return readExcelService;
	}

	public void setReadExcelService(ReadExcelService readExcelService) {
		this.readExcelService = readExcelService;
	}


	public PhysicalresService getPhysicalresService() {
		return physicalresService;
	}

	public void setPhysicalresService(PhysicalresService physicalresService) {
		this.physicalresService = physicalresService;
	}

	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}

	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	/**
	 * 上传文件处理
	 * @return
	 */
	public void uploadAction() {
		 /*this.fileFileName = new Date().getTime() +
		 getExtention(this.fileFileName); File serverFile = new File(
		 ServletActionContext.getServletContext().getRealPath("/upload") + "/"
		 + this.fileFileName); copy(this.file, serverFile);*/
		log.info("进入uploadAction,上传文件。");
		String savePath = ServletActionContext.getServletContext().getRealPath("");
		if(this.uploadType.equals("attachment")){
			//savePath = savePath + "/resource/physicalres/uploadFile/";
			savePath = savePath+"/upload/";
		}else{
			//savePath = savePath + "/resource/physicalres/uploadImage/";
			savePath = savePath+"/upload/";
		}
		
		log.info("取得文件上传路径：savePath"+savePath);
		//若文件不存在或不是一个路径，创建保存文件的目标路径
		File targetFile = new File(savePath);
		if(!targetFile.exists() || !targetFile.isDirectory()) {
			log.info("文件上传路径当前不存在，创建路径。");
			targetFile.mkdirs();
		}
		
		String extName = "";// 扩展名
		String newFileName = "";// 新文件名
		//System.out.println("原文件名称：" + this.fileFileName);
		// 获取扩展名
		if (this.fileFileName.lastIndexOf(".") > -1) {
			extName = this.fileFileName.substring(fileFileName.lastIndexOf("."));
			log.info("取得文件扩展名：extName="+extName);
		}

		newFileName = UUID.randomUUID() + extName;
		//System.out.println("保存的文件名称：" + savePath + newFileName);

		//目标文件(要被copy的文件)
		File dstFile = new File(savePath + newFileName);
		
		//copy上传的文件
		copy(this.file, dstFile);

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		try {
			if(this.uploadType.equals("attachment")){
				// 输出附件保存后所在的服务器的路径
				response.getWriter().print("../../upload/" + newFileName);
			}else{
				// 输出图片保存后所在的服务器的路径
				response.getWriter().print("../../upload/" + newFileName);
			}
			log.info("退出uploadAction,上传文件"+newFileName+"成功至"+savePath);
		} catch (IOException e) {
			log.error("退出uploadAction,上传文件"+newFileName+"失败至"+savePath);
			e.printStackTrace();
		}
	}

	/**
	 * 截取文件名的扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	/*private static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos);
	}*/

	/**
	 * 接收上传文件，并生成一个文件放在服务器文件内
	 * 
	 * @param src
	 * @param dst
	 */
	private static void copy(File src, File dst) {
		log.info("进入copy(File src, File dst)，接收上传文件，并生成一个文件放在服务器文件内.");
		log.info("进入copy(File src, File dst)，src="+src+",dst="+dst);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src));
			out = new BufferedOutputStream(new FileOutputStream(dst));
			byte[] buffer = new byte[2048];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			log.info("退出copy(File src, File dst)，上传文件成功。");
		} catch (Exception e) {
			log.error("退出copy(File src, File dst)，上传文件失败，文件不存在或路径不正确");
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("退出copy(File src, File dst)，关闭文件输入流InputStream失败。");
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					log.error("退出copy(File src, File dst)，关闭文件输出流OutputStream失败。");
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 资源导入(导入excel)
	 */
	public void resourceImportAction() {
		log.info("进入resourceImportAction(),资源导入解析Excel,返回相关信息.");
		if(this.rdoAssModel==null||"".equals(this.rdoAssModel)){
			this.rdoAssModel="noContent";
		}
		log.info("进入resourceImportAction(),匹配模式rdoAssModel="+rdoAssModel);
		log.info("进入resourceImportAction(),区域Id areaId="+areaId);
		ApplicationEntity areaAe = null;
		if(areaId!=null){//选择区域不为空
			BasicEntity be = physicalresService.getPhysicalresById("Sys_Area", Long.valueOf(areaId));
			if(be!=null){
				areaAe = ApplicationEntity.changeFromEntity(be);//选择区域实体
			}
			List<List<String>> rowList = readExcelService.getListStringRows(this.file, this.sheetIndex);//读取excel内容
			//获取第一行的标题行
			List<String> titleColumnList = null;
			if (rowList != null && rowList.size() > 1) {
				titleColumnList = rowList.get(0);//excel标题行
			} else {
				//没有内容的excel，不进行导入
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/html");
				
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				//返回null，提示用户重新选择excel
				String result = gson.toJson(null);
				try {
					log.info("退出resourceImportAction()，资源导入文件Excel内容为空，不进行导入。返回结果：result="+result);
					response.getWriter().write(result);
				} catch (IOException e) {
					log.error("退出resourceImportAction()，返回结果result="+result+"失败。");
					e.printStackTrace();
				}
				return;
			}

			//标题行内容集合
			List<String> titleContentList = new ArrayList<String>();
			boolean hasParentColumn = false;//判断是否有所属资源列（即上级资源）
			if(titleColumnList != null && !titleColumnList.isEmpty()) {
				String firstColumn = titleColumnList.get(0);
				if(firstColumn.indexOf("所属")>=0){//有所属资源列（即上级资源）
					hasParentColumn = true;
				}
				for (String content : titleColumnList) {
					//获取标题行(标题之间，不能出现空标题)
					titleContentList.add(content.trim());
					if(content.indexOf("parent::")>=0){//判断是否存在关联父资源列
						hasParentColumn = true;
					}
				}
			}
			if("exactMatch".equals(this.rdoAssModel) || "indistinctMatch".equals(this.rdoAssModel)){//需要精确或模糊匹配excel中的上级资源时判断是否存在上级资源列
				if(!hasParentColumn){
					//没有规范的excel，不进行导入
					HttpServletResponse response = ServletActionContext.getResponse();
					response.setCharacterEncoding("utf-8");
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					String result = gson.toJson("noParentColumn");
					//提示用户
					try {
						log.info("退出resourceImportAction()，资源导入文件Excel不规范，没有相关所属父资源的列，不进行导入。返回结果：result="+result);
						response.getWriter().write(result);
					} catch (IOException e) {
						log.error("退出resourceImportAction()，资源导入文件Excel不规范，没有相关所属父资源的列，不进行导入。返回结果：result="+result+"失败。");
						e.printStackTrace();
					}
					return;
				}
			}
			ApplicationModule module = ModuleProvider.getModule(this.importEntityType);//当前资源导入模板
			Map<String, Object> assMap = module.toMap();
			Map<String,Object> orderIdMap = new HashMap<String,Object>();//排序map
			this.chineseMap = new HashMap<String, Object>();//中文map
			for (String key : assMap.keySet()) {//获取中文字典
				List<BasicEntity> entry = null;
				try {
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						//数据字典，中英文转换
						entry = dictionary.getEntry(key + "," + this.importEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						
					}
					if(entry != null && !entry.isEmpty()) {
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							this.chineseMap.put(key, entry.get(0).getValue("display"));
							orderIdMap.put(entry.get(0).getValue("orderID")+"", key);
						}
					}
				} catch (EntryOperationException e) {
					log.error("获取"+this.importEntityType+"属性字段"+key+"的中文字典失败，可能该中文字典不存在");
					e.printStackTrace();
					//找不到对应的中文key，继续以英文显示
					this.chineseMap.put(key, key);
					//orderIdMap.put(entry.get(0).getValue("orderID")+"", key);
				}
			}
			//获取excel表中的中文字段对应所存在的英文字段(用来为ae设值使用，根据中文标题找到对应的字段)
			Map<String, Object> srcKeyMap = new HashMap<String, Object>();
			if(titleContentList != null && !titleContentList.isEmpty()) {
				for (int i = 0; i <titleContentList.size(); i++) {
					for(String key : assMap.keySet()) {
						if(titleContentList.get(i).equals(chineseMap.get(key))) {
							srcKeyMap.put(i+"", key);
							break;
						}
					}
				}
			}

			//被导入对象的app的list
			this.resultMapList = new ArrayList<Map<String,Object>>();
			List<String> matchContentList = new ArrayList<String>();//导入匹配规则的每行值
			StringBuffer resultMsg = new StringBuffer();//失败信息
			StringBuffer failrowIndex = new StringBuffer();//验证失败行数
			for (int i = 0; i < rowList.size(); i++) {//循环行数
				if(rowList.get(i) != null && !rowList.get(i).isEmpty()) {
					//跳过标题行
					if(i > 0) {
						ApplicationEntity importApp = moduleLibrary.getModule(this.importEntityType).createApplicationEntity();
						Map<String,Object> appMap = new HashMap<String,Object>();
						if(importApp != null) {
							for(int j = 0; j <rowList.get(i).size(); j++) {//获取一行资源数据
								if(srcKeyMap.containsKey(j+"")) {
									String key = srcKeyMap.get(j+"")+"";
									String value = rowList.get(i).get(j);
									if(value != null) {
										value = rowList.get(i).get(j).trim();	
										if(!"".equals(value)) {
											String type = module.getAttribute(key).getValue("type")+"";
											try{//数据格式验证
												if(type.indexOf("Date")>=0){
													SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
													sf.parse(value);
												}else if(type.indexOf("Integer")>=0||type.indexOf("Long")>=0){
													Long.parseLong(value);
												}else if(type.indexOf("Float")>=0||type.indexOf("Double")>=0){
													Double.parseDouble(value);
												}
											}catch(Exception e){
												 log.info("excel表中第"+(i+1)+"行第"+(j+1)+"列数据的值"+value+"转换为"+type+"类型失败");
												 resultMsg.append("第"+(i+1)+"行第"+(j+1)+"列数据的值与库表中类型("+type+")不对应,转换出错。 \n");
												 if(failrowIndex.indexOf(",i,")<0){
													 failrowIndex.append(","+i+",");
												 } 
											}
											
											appMap.put(key, value);
										}else{
											String nullable = module.getAttribute(key).getValue("nullable")+"";
											if("newAdd".equals(this.importModel)){//新增导入 必填验证
												if("false".equals(nullable)){
													resultMsg.append("第"+(i+1)+"行第"+(j+1)+"列数据为必填项,请填写内容。 \n");
													if(failrowIndex.indexOf(",i,")<0){
														failrowIndex.append(","+i+",");
													} 
												}
											}
											
										}
										if("updateAdd".equals(this.importModel)||"deleteAdd".equals(this.importModel)){//更新或删除导入
											if(key.equals(this.attributeChange)){
												matchContentList.add(value);//匹配值
											}
										}else if("newAdd".equals(this.importModel)){ //新增导入
											if(key.equals("name")){
												if(this.importEntityType.indexOf("BaseStation")>=0 || "Station".equals(this.importEntityType)){
													matchContentList.add(value);//匹配值
												}
											}
										}
									}
								}
							}
							for (String key : assMap.keySet()) {
								if(!appMap.containsKey(key)){
									appMap.put(key,"");
								}
							}
							Map<String,Object> map = quickSort.sortMap(appMap,orderIdMap);
							if(map!=null && !map.isEmpty()){
								appMap=map;
							}
							this.resultMapList.add(appMap);//要导入的资源list
						}
					}
				}
			}
			
			if("newAdd".equals(this.importModel)){//新增导入
				List<List> curConditionList = null;//当前条件已有资源记录
				if(this.importEntityType.indexOf("BaseStation")>=0 || "Station".equals(this.importEntityType)){//基站和站址要判断同名存在
					ApplicationEntity[] curApps = this.structureCommonService.getAppArrsByRecursionForSrcSameType(areaAe, this.importEntityType, "networkresourcemanage");
					if(curApps==null){
						curApps = this.structureCommonService.getStrutureSelationsApplicationEntity(areaAe, this.importEntityType, AssociatedType.LINK,"networkresourcemanage");
					}//当前区域 要导入的资源类型 全部实例
					Map<String,Object> curMap = new HashMap<String,Object>();//保存为map信息
					List<BasicEntity> noAssociateResourceBeList = physicalresService.getNoAssociateResource(this.importEntityType);//未绑关系的关联资源
					if(noAssociateResourceBeList!=null && noAssociateResourceBeList.size()>0 ){
						String mStr ="";
						for (BasicEntity resBe : noAssociateResourceBeList) {
							mStr = resBe.getValue("name")+"";
							if(curMap.containsKey(mStr)){
								curMap.put(mStr,curMap.get(mStr)+","+resBe.getValue("id"));
							}else{
								curMap.put(mStr, resBe.getValue("id"));
							}
							
						}
					}
					if(curApps != null && curApps.length>=0){
						String mStr ="";
						for(ApplicationEntity ap:curApps){
							mStr = ap.getValue("name")+"";
							if(curMap.containsKey(mStr)){
								curMap.put(mStr,curMap.get(mStr)+","+ap.getValue("id"));
							}else{
								curMap.put(mStr, ap.getValue("id"));
							}
							
						}
					}
					curConditionList = new ArrayList<List>();
					if(matchContentList!=null && !matchContentList.isEmpty()){//与条件资源 对比 获取信息
						for(String v:matchContentList){
							if(curMap.containsKey(v)){
								String ids = curMap.get(v)+"";
								List<Map<String,Object>> curConditionMapList = new ArrayList<Map<String,Object>>();
								for(String id:ids.split(",")){
									Map<String,Object> mp = new HashMap<String,Object>();
									mp.put("name",v);
									mp.put("id", id);
									if(!mp.isEmpty()){
										curConditionMapList.add(mp);
									}else{
										curConditionMapList.add(null);
									}
								}
								curConditionList.add(curConditionMapList);
							}else{
								curConditionList.add(null);
							}
							
						}
					}
				}
				
				//精确指定上级资源
				if("exactContent".equals(this.rdoAssModel)) {
					//精确获取隶属资源
					if(this.assEntityId != null && this.assEntityType != null 
							&& !"".equals(this.assEntityId) && !"".equals(this.assEntityType)) {
						//大集合，放着中文标题map和entity结果集合
						if(this.selectResChosenType != null && !"".equals(this.selectResChosenType)) {
							assType = this.selectResChosenType.substring(this.selectResChosenType.lastIndexOf("_") + 1);
							this.assEntityType = this.selectResChosenType.substring(0,this.selectResChosenType.lastIndexOf("_"));
						}
						this.bigResultMap = new HashMap<String, Object>();
						this.bigResultMap.put("titleMap", this.chineseMap);
						this.bigResultMap.put("contentMapList", this.resultMapList);
						this.bigResultMap.put("assEntityId", this.assEntityId);
						this.bigResultMap.put("assType",this.assType);
						this.bigResultMap.put("assEntityType",this.assEntityType);
						this.bigResultMap.put("curConditionList",curConditionList);
						this.bigResultMap.put("resultMsg",resultMsg+"");
						this.bigResultMap.put("failrowIndex",failrowIndex+"");
					}
				} else if("exactMatch".equals(this.rdoAssModel) || "indistinctMatch".equals(this.rdoAssModel)) {//精确或模糊匹配上级资源
					if(this.selectResChosenType != null && !"".equals(this.selectResChosenType)) {//上级资源类型
						assType = this.selectResChosenType.substring(this.selectResChosenType.lastIndexOf("_") + 1);
						this.assEntityType = this.selectResChosenType.substring(0,this.selectResChosenType.lastIndexOf("_"));
					}
					//当前区域范围存在的上级资源
					ApplicationEntity[] parApps = this.structureCommonService.getAppArrsByRecursionForSrcSameType(areaAe, this.assEntityType, "networkresourcemanage");
					if(parApps==null){
						parApps = this.structureCommonService.getStrutureSelationsApplicationEntity(areaAe, this.assEntityType, AssociatedType.LINK,"networkresourcemanage");
					}
					Map<String,Object> parMap = new HashMap<String,Object>();
					if(parApps!=null && parApps.length>0){
						String label = "";
						String name = "";
						for(ApplicationEntity ae:parApps){
							name = ae.getValue("name");
							label = ae.getValue("label");
							if(name!=null&&!"".equals(name)){
								if(parMap.containsKey(name)){
									parMap.put(name, parMap.get(name)+","+ae.getValue("id"));
								}else{
									parMap.put(name,ae.getValue("id"));
								}
							}else{
								if(parMap.containsKey(label)){
									parMap.put(label, parMap.get(label)+","+ae.getValue("id"));
								}else{
									parMap.put(label,ae.getValue("id"));
								}
							}
						}
						
					}
					/*if("Sys_Area".equals(this.assEntityType)){
						if(parMap.containsKey(areaAe.getValue("name")+"")){
							parMap.put(areaAe.getValue("name")+"",parMap.get(areaAe.getValue("name")+"")+","+areaAe.getValue("id"));
						}else{
							parMap.put(areaAe.getValue("name")+"", areaAe.getValue("id"));
						}
						
					}*/
					List<BasicEntity> noAssociateResourceBeList = physicalresService.getNoAssociateResource(this.assEntityType);//未绑关系的关联资源
					if(noAssociateResourceBeList!=null && noAssociateResourceBeList.size()>0 ){
						String label = "";
						String name = "";
						for (BasicEntity resBe : noAssociateResourceBeList) {
							name = resBe.getValue("name");
							label = resBe.getValue("label");
							if(name!=null&&!"".equals(name)){
								if(parMap.containsKey(name)){
									parMap.put(name, parMap.get(name)+","+resBe.getValue("id"));
								}else{
									parMap.put(name,resBe.getValue("id"));
								}
							}else{
								if(parMap.containsKey(label)){
									parMap.put(label, parMap.get(label)+","+resBe.getValue("id"));
								}else{
									parMap.put(label,resBe.getValue("id"));
								}
							}
						}
					}
					
					//精确匹配字段名或者模糊匹配字段名
					List<List> parNameList= new ArrayList<List>();
					List<String> parMatchList= new ArrayList<String>();
					//获取标题行中，需要查找隶属的字段列名，格式(parent::)
					int index=-1;
					if(titleContentList != null && !titleContentList.isEmpty()) {
						for(int i = 0; i <titleContentList.size(); i++) {
							if(titleContentList.get(i).trim().indexOf("parent::") > -1) {
								index = i;
								break;
							}
						}
						if(index==-1){
							if(titleContentList.get(0).trim().indexOf("所属") > -1){
								index = 0;
							}
						}
					}
					
					//获取需要查找的字段列的字段值的集合
					for(int i = 0; i < rowList.size(); i++) {
						if(i > 0) {
							for(int j = 0; j < rowList.get(i).size(); j++) {
								if(j == index) {
									String ne = rowList.get(i).get(j).trim();
									parMatchList.add(ne);
									if("exactMatch".equals(this.rdoAssModel)){
										if(parMap!=null && !parMap.isEmpty()){
											if(parMap.containsKey(ne)){//获取到上级资源信息
												List<Map<String,Object>> parNameMapList= new ArrayList<Map<String,Object>>();
												String ids = parMap.get(ne)+"";
												for(String id:ids.split(",")){
													Map<String,Object> mp = new HashMap<String,Object>();
													mp.put("id", id);
													mp.put("name", ne);
													parNameMapList.add(mp);
												}
												parNameList.add(parNameMapList);
											}else{
												parNameList.add(null);
											}
										}else{
											parNameList.add(null);
										}
										
									}else{
										if(parMap!=null && !parMap.isEmpty()){//获取到上级资源信息
											boolean hasPar = false;
											for(String k:parMap.keySet()){
												if(k.contains(ne)){
													hasPar = true;
													String ids = parMap.get(k)+"";
													List<Map<String,Object>> parNameMapList= new ArrayList<Map<String,Object>>();
													for(String id:ids.split(",")){
														Map<String,Object> mp = new HashMap<String,Object>();
														mp.put("id", id);
														mp.put("name",k);
														parNameMapList.add(mp);
													}
													parNameList.add(parNameMapList);
												}
											}
											if(!hasPar){
												parNameList.add(null);
											}
											
										}else{
											parNameList.add(null);
										}
									}
									
									continue;
								}
							}
						}
					}
					//大集合，放着中文标题map和entity结果集合
					this.bigResultMap = new HashMap<String, Object>();
					this.bigResultMap.put("titleMap", this.chineseMap);
					this.bigResultMap.put("contentMapList", this.resultMapList);
					this.bigResultMap.put("parNameList", parNameList);
					this.bigResultMap.put("assType",this.assType);
					this.bigResultMap.put("curConditionList",curConditionList);
					this.bigResultMap.put("assEntityType",this.assEntityType);
					this.bigResultMap.put("parMatchList",parMatchList);
					this.bigResultMap.put("resultMsg",resultMsg+"");
					this.bigResultMap.put("failrowIndex",failrowIndex+"");
				} else if("noContent".equals(this.rdoAssModel)) {
					//大集合，放着中文标题map和entity结果集合
					this.bigResultMap = new HashMap<String, Object>();
					this.bigResultMap.put("titleMap", this.chineseMap);
					this.bigResultMap.put("contentMapList", this.resultMapList);
					this.bigResultMap.put("curConditionList",curConditionList);
					this.bigResultMap.put("resultMsg",resultMsg+"");
					this.bigResultMap.put("failrowIndex",failrowIndex+"");
				} else {
					log.info("退出resourceImportAction，匹配模式rdoAssModel="+rdoAssModel);
					return;
				}
			}else if("updateAdd".equals(this.importModel)||"deleteAdd".equals(this.importModel)){//更新导入 删除导入
				Map<String,Object> childflagMap = null;//用于判断是否有子资源
				if("deleteAdd".equals(this.importModel)){
					String[] types=this.structureCommonService.getAssociatedAetName(this.importEntityType, AssociatedType.CHILD, "networkresourcemanage");
					childflagMap = new HashMap<String,Object>();//保存为map信息
					if(types!=null){
						for(String type:types){
							String idStr = "";
							StringBuffer sf = new StringBuffer();
							ApplicationEntity[] apps = this.structureCommonService.getAppArrsByRecursionForSrcSameType(areaAe, type, "networkresourcemanage");
							if(apps!=null && apps.length>0){
								for(ApplicationEntity ae:apps){
									idStr +=","+ae.getValue("id");
									sf.append(","+ae.getValue("id"));
								}
								idStr = sf.toString();
								idStr = idStr.substring(1,idStr.length());
								Map<String,Object> searchMap= this.physicalresService.getSomeParentTypeIdsMapByIds(idStr,type,this.importEntityType);
								if(searchMap!=null && !searchMap.isEmpty()){
									for(String k:searchMap.keySet()){
										if(!childflagMap.containsKey(k)){
											childflagMap.put(k, k);
										}
									}
								}
							}
							
						}
					}
				}
				ApplicationEntity[] curApps = this.structureCommonService.getAppArrsByRecursionForSrcSameType(areaAe, this.importEntityType, "networkresourcemanage");
				if(curApps==null){
					curApps = this.structureCommonService.getStrutureSelationsApplicationEntity(areaAe, this.importEntityType, AssociatedType.LINK,"networkresourcemanage");
				}//当前区域 要导入的资源类型 全部实例
				Map<String,Object> curMap = new HashMap<String,Object>();//保存为map信息
				List<BasicEntity> noAssociateResourceBeList = physicalresService.getNoAssociateResource(this.importEntityType);//未绑关系的关联资源
				if(noAssociateResourceBeList!=null && noAssociateResourceBeList.size()>0 ){
					String mStr ="";
					for (BasicEntity resBe : noAssociateResourceBeList) {
						mStr = resBe.getValue("name")+"";
						if(curMap.containsKey(mStr)){
							curMap.put(mStr,curMap.get(mStr)+","+resBe.getValue("id"));
						}else{
							curMap.put(mStr, resBe.getValue("id"));
						}
						
					}
				}
				if(curApps != null && curApps.length>=0){
					String mStr ="";
					for(ApplicationEntity ap:curApps){
						mStr = ap.getValue(this.attributeChange)+"";
						if(curMap.containsKey(mStr)){
							curMap.put(mStr,curMap.get(mStr)+","+ap.getValue("id"));
						}else{
							curMap.put(mStr, ap.getValue("id"));
						}
						
					}
				}
				List<List> curConditionList = new ArrayList<List>();
				if(matchContentList!=null && !matchContentList.isEmpty()){//与条件资源 对比 获取信息
					for(String v:matchContentList){
						if(curMap.containsKey(v)){
							String ids = curMap.get(v)+"";
							List<Map<String,Object>> curConditionMapList = new ArrayList<Map<String,Object>>();
							for(String id:ids.split(",")){
								Map<String,Object> mp = new HashMap<String,Object>();
								mp.put("name",v);
								mp.put("id", id);
								if("deleteAdd".equals(this.importModel)){
									if(childflagMap!=null && !childflagMap.isEmpty()){
										if(childflagMap.containsKey(id)){
											mp.put("hasChild","has");
										}else{
											mp.put("hasChild","no");
										}
									}else{
										mp.put("hasChild","no");
									}
								}
								if(!mp.isEmpty()){
									curConditionMapList.add(mp);
								}else{
									curConditionMapList.add(null);
								}
							}
							curConditionList.add(curConditionMapList);
						}else{
							curConditionList.add(null);
						}
						
					}
				}
				
				
				//精确指定上级资源
				if("exactContent".equals(this.rdoAssModel)) {
					//精确获取隶属资源
					if(this.assEntityId != null && this.assEntityType != null 
							&& !"".equals(this.assEntityId) && !"".equals(this.assEntityType)) {
						//大集合，放着中文标题map和entity结果集合
						if(this.selectResChosenType != null && !"".equals(this.selectResChosenType)) {
							assType = this.selectResChosenType.substring(this.selectResChosenType.lastIndexOf("_") + 1);
							this.assEntityType = this.selectResChosenType.substring(0,this.selectResChosenType.lastIndexOf("_"));
						}
						this.bigResultMap = new HashMap<String, Object>();
						this.bigResultMap.put("titleMap", this.chineseMap);
						this.bigResultMap.put("contentMapList", this.resultMapList);
						this.bigResultMap.put("assEntityId", this.assEntityId);
						this.bigResultMap.put("assType",this.assType);
						this.bigResultMap.put("curConditionList",curConditionList);
						this.bigResultMap.put("assEntityType",this.assEntityType);
						this.bigResultMap.put("resultMsg",resultMsg+"");
						this.bigResultMap.put("failrowIndex",failrowIndex+"");
					}
				} else if("exactMatch".equals(this.rdoAssModel) || "indistinctMatch".equals(this.rdoAssModel)) {//精确或模糊匹配上级资源
					if(this.selectResChosenType != null && !"".equals(this.selectResChosenType)) {//上级资源类型
						assType = this.selectResChosenType.substring(this.selectResChosenType.lastIndexOf("_") + 1);
						this.assEntityType = this.selectResChosenType.substring(0,this.selectResChosenType.lastIndexOf("_"));
					}
					//当前区域范围上级资源
					ApplicationEntity[] parApps = this.structureCommonService.getAppArrsByRecursionForSrcSameType(areaAe, this.assEntityType, "networkresourcemanage");
					if(parApps==null){
						parApps = this.structureCommonService.getStrutureSelationsApplicationEntity(areaAe, this.assEntityType, AssociatedType.LINK,"networkresourcemanage");
					}
					Map<String,Object> parMap = new HashMap<String,Object>();
					
					if(parApps!=null && parApps.length>0){
						String label = "";
						String name = "";
						for(ApplicationEntity ae:parApps){
							name = ae.getValue("name");
							label = ae.getValue("label");
							if(name!=null&&!"".equals(name)){
								if(parMap.containsKey(name)){
									parMap.put(name, parMap.get(name)+","+ae.getValue("id"));
								}else{
									parMap.put(name,ae.getValue("id"));
								}
							}else{
								if(parMap.containsKey(label)){
									parMap.put(label, parMap.get(label)+","+ae.getValue("id"));
								}else{
									parMap.put(label,ae.getValue("id"));
								}
							}
						}
						
					}
				/*	if("Sys_Area".equals(this.assEntityType)){
						if(parMap.containsKey(areaAe.getValue("name")+"")){
							parMap.put(areaAe.getValue("name")+"",parMap.get(areaAe.getValue("name")+"")+","+areaAe.getValue("id"));
						}else{
							parMap.put(areaAe.getValue("name")+"", areaAe.getValue("id"));
						}
						
					}*/
					noAssociateResourceBeList = physicalresService.getNoAssociateResource(this.assEntityType);//未绑关系的关联资源
					if(noAssociateResourceBeList!=null && noAssociateResourceBeList.size()>0 ){
						String label = "";
						String name = "";
						for (BasicEntity resBe : noAssociateResourceBeList) {
							name = resBe.getValue("name");
							label = resBe.getValue("label");
							if(name!=null&&!"".equals(name)){
								if(parMap.containsKey(name)){
									parMap.put(name, parMap.get(name)+","+resBe.getValue("id"));
								}else{
									parMap.put(name,resBe.getValue("id"));
								}
							}else{
								if(parMap.containsKey(label)){
									parMap.put(label, parMap.get(label)+","+resBe.getValue("id"));
								}else{
									parMap.put(label,resBe.getValue("id"));
								}
							}
						}
					}
					//精确匹配字段名或者模糊匹配字段名
					List<List> parNameList= new ArrayList<List>();
					List<String> parMatchList= new ArrayList<String>();
					//获取标题行中，需要查找隶属的字段列名，格式(parent::)
					int index=-1;
					if(titleContentList != null && !titleContentList.isEmpty()) {
						for(int i = 0; i <titleContentList.size(); i++) {
							if(titleContentList.get(i).trim().indexOf("parent::") > -1) {
								index = i;
								break;
							}
						}
						if(index==-1){
							if(titleContentList.get(0).trim().indexOf("所属") > -1){
								index = 0;
							}
						}
					}
					
					//获取需要查找的字段列的字段值的集合
					for(int i = 0; i < rowList.size(); i++) {
						if(i > 0) {
							for(int j = 0; j < rowList.get(i).size(); j++) {
								if(j == index) {
									String ne = rowList.get(i).get(j).trim();
									parMatchList.add(ne);
									if("exactMatch".equals(this.rdoAssModel)){
										if(parMap!=null && !parMap.isEmpty()){//获取到每行匹配上级资源信息
											if(parMap.containsKey(ne)){
												List<Map<String,Object>> parNameMapList= new ArrayList<Map<String,Object>>();
												String ids = parMap.get(ne)+"";
												for(String id:ids.split(",")){
													Map<String,Object> mp = new HashMap<String,Object>();
													mp.put("id", id);
													mp.put("name", ne);
													parNameMapList.add(mp);
												}
												parNameList.add(parNameMapList);
											}else{
												parNameList.add(null);
											}
										}else{
											parNameList.add(null);
										}
										
									}else{
										if(parMap!=null && !parMap.isEmpty()){//获取到每行匹配上级资源信息
											boolean hasPar = false;
											for(String k:parMap.keySet()){
												if(k.contains(ne)){
													hasPar = true;
													String ids = parMap.get(k)+"";
													List<Map<String,Object>> parNameMapList= new ArrayList<Map<String,Object>>();
													for(String id:ids.split(",")){
														Map<String,Object> mp = new HashMap<String,Object>();
														mp.put("id", id);
														mp.put("name",k);
														parNameMapList.add(mp);
													}
													parNameList.add(parNameMapList);
												}
											}
											if(!hasPar){
												parNameList.add(null);
											}
											
										}else{
											parNameList.add(null);
										}
									}
									
									continue;
								}
							}
						}
					}
					//大集合，放着中文标题map和entity结果集合
					this.bigResultMap = new HashMap<String, Object>();
					this.bigResultMap.put("titleMap", this.chineseMap);
					this.bigResultMap.put("contentMapList", this.resultMapList);
					this.bigResultMap.put("parNameList", parNameList);
					this.bigResultMap.put("assType",this.assType);
					this.bigResultMap.put("curConditionList",curConditionList);
					this.bigResultMap.put("assEntityType",this.assEntityType);
					this.bigResultMap.put("matchContentList",matchContentList);
					this.bigResultMap.put("parMatchList",parMatchList);
					this.bigResultMap.put("resultMsg",resultMsg+"");
					this.bigResultMap.put("failrowIndex",failrowIndex+"");
				} else if("noContent".equals(this.rdoAssModel)) {
					//大集合，放着中文标题map和entity结果集合
					this.bigResultMap = new HashMap<String, Object>();
					this.bigResultMap.put("titleMap", this.chineseMap);
					this.bigResultMap.put("contentMapList", this.resultMapList);
					this.bigResultMap.put("curConditionList",curConditionList);
					this.bigResultMap.put("resultMsg",resultMsg+"");
					this.bigResultMap.put("failrowIndex",failrowIndex+"");
				} else {
					log.info("退出resourceImportAction，匹配模式rdoAssModel="+rdoAssModel);
					return;
				}
			}
		}

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(this.bigResultMap);
		try {
			log.info("退出resourceImportAction，返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出resourceImportAction，返回结果result="+result+"失败");
			e.printStackTrace();
		}
		
		
	}
	/**
	 * 
	 * @description:取得当前导入成功数量的信息
	 * @author：     
	 * @return void     
	 * @date：Jan 30, 2013 1:24:45 PM
	 */
	public void getCurrentImportCoutAction(){
		log.info("进入getCurrentImportCoutAction()，取得当前导入成功数量的信息。");
		HttpSession session = ServletActionContext.getRequest().getSession();
		String result = "";
		if(session.getAttribute("currenImportMs") != null && !"".equals(session.getAttribute("currenImportMs"))){	
			result = session.getAttribute("currenImportMs").toString();
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			result =gson.toJson(result);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		try {
			log.info("退出getCurrentImportCoutAction()，返回result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getCurrentImportCoutAction()，返回result="+result+"失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @description: 导入新增action
	 * @author：     
	 * @return void     
	 * @date：Jan 29, 2013 9:57:15 AM
	 */
	public  void importAddResourceAction(){
		log.info("进入importAddResourceAction(),对导入的资源数据进行入库操作。");
		//获取登录人ID
		HttpSession session = ServletActionContext.getRequest().getSession();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String userId = "";
		String userName = "";
		if(session.getAttribute("userId") != null && !"".equals(session.getAttribute("userId"))){	
			userId = session.getAttribute("userId").toString();
			Map<String, String> userByUserId = this.staffOrganizationService.getUserByUserId(userId);
			if(userByUserId != null){
				userName = userByUserId.get("name");
			}
		}
		log.info("进入importAddResourceAction(),userId="+userId+",userName="+userName);
		session.setAttribute("currenImportMs", "");
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		List<Map<String,String>> listMap = null;
		List<String>  rows = null;
		List<String> assIds = null;
		try{
			listMap = gson.fromJson(this.importMaps,new TypeToken<List<Map<String,String>>>(){}.getType());
			rows = gson.fromJson(this.rowNums,new TypeToken<List<String>>(){}.getType());
			assIds = gson.fromJson(this.assRecord,new TypeToken<List<String>>(){}.getType());
		}catch(Exception e){
			log.error("导入的资源数据转换json数据发生异常，可能存在特殊字符。");
			e.printStackTrace();
			Map<String,Object> mp = new HashMap<String,Object>();
			mp.put("resultList",resultList);
			mp.put("resultMsg","共导入成功<span style='color:red'>0</span>个");
			String r= gson.toJson(mp);
			try {
				log.info("退出importAddResourceAction(),返回结果r="+r);
				response.getWriter().write(r);
			} catch (IOException ie) {
				log.error("退出importAddResourceAction(),返回结果r="+r+"失败。");
				ie.printStackTrace();
			}
			return;
		}
		
		List<String> updateIds = null;
		
		if(!"newAdd".equals(this.importModel)){
			 updateIds = gson.fromJson(this.importRecord,new TypeToken<List<String>>(){}.getType());
		}
		if(listMap!=null && !listMap.isEmpty()){
			if(listMap.size()>100){
				int count = listMap.size()%100;
				if(count==0){
					count = listMap.size()/100;
				}else{
					count = listMap.size()/100+1;
				}
				for(int index=1;index<count+1;index++){
					int curIndex=(index-1)*100;
					List<Map<String,String>> lMap = new ArrayList<Map<String,String>>();
					List<String> rs = new ArrayList<String>();
					List<String> uIds = new ArrayList<String>();
					for(int i=curIndex;i<curIndex+100;i++){
						if(i<listMap.size()){
							lMap.add(listMap.get(i));
							rs.add(rows.get(i));
							if(!"newAdd".equals(this.importModel)){
								uIds.add(updateIds.get(i));
							}
						}
					}
					List<Map<String,Object>> rMapList = null;
					if("newAdd".equals(this.importModel)){
						rMapList=this.importService.importAddResource(userId, userName, importEntityType, importModel, opCause, opScene, rdoAssModel, assType, assEntityType, lMap, rs, assIds);
					}else if("updateAdd".equals(this.importModel)){
						rMapList=this.importService.importUpdateResource(userId, userName, importEntityType, importModel, opCause, opScene, rdoAssModel, assType, assEntityType, lMap, rs, assIds, uIds);
					}else if("deleteAdd".equals(this.importModel)){
						rMapList=this.importService.importDeleteAddResource(userId, userName, importEntityType, importModel, opCause, opScene, rdoAssModel, assType, assEntityType, lMap, rs, assIds, uIds);
					}	
					if(rMapList!=null && !rMapList.isEmpty()){
						resultList.addAll(rMapList);
						if(index*100<listMap.size()){
							session.setAttribute("currenImportMs", "已导入成功<span style='color:red'>"+index*100+"</span>个");
						}else{
							session.setAttribute("currenImportMs", "共导入成功<span style='color:red'>"+listMap.size()+"</span>个");
						}
					}else{
						break;
					}
				}
			}else{
				List<Map<String,Object>> rMapList  = null;
				if("newAdd".equals(this.importModel)){
					rMapList=this.importService.importAddResource(userId, userName, importEntityType, importModel, opCause, opScene, rdoAssModel, assType, assEntityType, listMap, rows, assIds);
				}else if("updateAdd".equals(this.importModel)){
					rMapList=this.importService.importUpdateResource(userId, userName, importEntityType, importModel, opCause, opScene, rdoAssModel, assType, assEntityType, listMap, rows, assIds, updateIds);
				}else if("deleteAdd".equals(this.importModel)){
					rMapList=this.importService.importDeleteAddResource(userId, userName, importEntityType, importModel, opCause, opScene, rdoAssModel, assType, assEntityType, listMap, rows, assIds, updateIds);
				}	
				if(rMapList!=null && !rMapList.isEmpty()){
					resultList.addAll(rMapList);
					session.setAttribute("currenImportMs", "共导入成功<span style='color:red'>"+listMap.size()+"</span>个");
				}else{
					session.setAttribute("currenImportMs", "共导入成功<span style='color:red'>0</span>个");
				}
				
			}
			
			Map<String,Object> mp = new HashMap<String,Object>();
			mp.put("resultList",resultList);
			mp.put("resultMsg",session.getAttribute("currenImportMs")+"");
			String r= gson.toJson(mp);
			try {
				log.info("退出importAddResourceAction(),返回结果r="+r);
				response.getWriter().write(r);
			} catch (IOException e) {
				log.error("退出importAddResourceAction(),返回结果r="+r+"失败。");
				e.printStackTrace();
			}
		}
	}
	/**
	 * 创建两个物理资源的关系(导入资源与隶属资源)
	 */
	public void createAssAppAction() {
		String result = "";
		try {
			log.info("进入createAssAppAction，创建两个物理资源的关系(导入资源与隶属资源)");
			//被导入的资源
			ApplicationEntity importApp = ApplicationEntity.changeFromEntity(
					physicalresService.getPhysicalresById(this.importEntityType, Long.parseLong(this.importEntityId)));
			//隶属资源
			ApplicationEntity assApp = ApplicationEntity.changeFromEntity(
					physicalresService.getPhysicalresById(this.assEntityType, Long.parseLong(this.assEntityId)));
			if(importApp != null && assApp != null && this.assType != null && !"".equals(this.assType)) {
				AssociatedType associatedType = null;
				//获取需要绑定的关系类型
				if("parent".equals(this.assType)) {
					associatedType = AssociatedType.CLAN;
				} else if("link".equals(this.assType)) {
					associatedType = AssociatedType.LINK;
				}
				//创建隶属资源与导入资源的关系
				int count=0;
				if("updateAdd".equals(this.importModel)){
					
					ApplicationEntity[] parentApps=null;					
					if("parent".equals(this.assType)) {
						parentApps=structureCommonService.getStrutureSelationsApplicationEntity(importApp, AssociatedType.PARENT,"networkresourcemanage");
					} else if("link".equals(this.assType)) {
						parentApps=structureCommonService.getStrutureSelationsApplicationEntity(importApp, AssociatedType.LINK,"networkresourcemanage");
					}
					boolean hasFlag = false;
					if(parentApps!=null&&parentApps.length>0){
						for(int i=0;i<parentApps.length;i++){
							if("parent".equals(this.assType)){
								hasFlag=true;
								int state=structureCommonService.delStrutureAssociation(parentApps[i], importApp,associatedType, "networkresourcemanage");
								if(state>0){
									String resNameChinese = "";
									if(parentApps[i] != null){
										try {
											List<BasicEntity> entry = null;
												entry = dictionary.getEntry(parentApps[i] + ",networkResourceDefination" ,SearchScope.OBJECT, "");
												resNameChinese = entry.get(0).getValue("display");
										} catch (EntryOperationException e) {
											e.printStackTrace();
										}
									}

									//添加资源维护记录
									ResourceMaintenance maintenance = new ResourceMaintenance();
									maintenance.setBiz_module(OperationType.NETWORK);
									maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
									maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
									String lresName = "";
									if(assApp.getValue("name") != null){
										lresName = parentApps[i].getValue("name");
									}else{
										lresName = parentApps[i].getValue("label");
									}
									String rresName = "";
									if(importApp.getValue("name") != null){
										rresName = importApp.getValue("name");
									}else{
										rresName = importApp.getValue("label");
									}
									maintenance.setRes_keyinfo(rresName);
									String content = OperationType.DELETE+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
									//String content = OperationType.DELETE +"与" + lresName + OperationType.getAssociatedTypeChinese(associatedType.toString());
									maintenance.setOp_category(OperationType.DELETELINK);
									maintenance.setContent(content);
									maintenance.setRes_id((Long)importApp.getValue("id"));
									maintenance.setRes_type(importApp.getValue("_entityType").toString());
									networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
									if("parent".equals(this.assType)) {
										count = structureCommonService.createAssociatedRelation(assApp, importApp, associatedType, "networkresourcemanage");
									} else if("link".equals(this.assType)) {
										count = structureCommonService.createAssociatedRelation( importApp,assApp, associatedType, "networkresourcemanage");
									}
								}
							}else{
								if(parentApps[i].getValue("_entityType").toString().equals(assApp.getValue("_entityType").toString())){
									hasFlag=true;
									int state = structureCommonService.delStrutureAssociation(parentApps[i], importApp,associatedType, "networkresourcemanage");//link 有先后顺序
									int state2 = structureCommonService.delStrutureAssociation(importApp,parentApps[i],associatedType, "networkresourcemanage");
									if(state>0||state2>0){
										String resNameChinese = "";
										if(parentApps[i] != null){
											try {
												List<BasicEntity> entry = null;
													entry = dictionary.getEntry(parentApps[i] + ",networkResourceDefination" ,SearchScope.OBJECT, "");
													resNameChinese = entry.get(0).getValue("display");
														//System.out.println(infoNameChinese);
											} catch (EntryOperationException e) {
												e.printStackTrace();
											}
										}
										//添加资源维护记录
										ResourceMaintenance maintenance = new ResourceMaintenance();
										maintenance.setBiz_module(OperationType.NETWORK);
										maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
										maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
										String lresName = "";
										if(assApp.getValue("name") != null){
											lresName = parentApps[i].getValue("name");
										}else{
											lresName = parentApps[i].getValue("label");
										}
										String rresName = "";
										if(importApp.getValue("name") != null){
											rresName = importApp.getValue("name");
										}else{
											rresName = importApp.getValue("label");
										}
										maintenance.setRes_keyinfo(rresName);
										String content = OperationType.DELETE+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
										maintenance.setOp_category(OperationType.DELETELINK);
										maintenance.setContent(content);
										maintenance.setRes_id((Long)importApp.getValue("id"));
										maintenance.setRes_type(importApp.getValue("_entityType").toString());
										networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
										if("parent".equals(this.assType)) {
											count = structureCommonService.createAssociatedRelation(assApp, importApp, associatedType, "networkresourcemanage");
										} else if("link".equals(this.assType)) {
											count = structureCommonService.createAssociatedRelation( importApp,assApp, associatedType, "networkresourcemanage");
										}
										break;
									}
								}
							}
							
						}
						if(!hasFlag){
							if("parent".equals(this.assType)) {
								count = structureCommonService.createAssociatedRelation(assApp, importApp, associatedType, "networkresourcemanage");
							} else if("link".equals(this.assType)) {
								count = structureCommonService.createAssociatedRelation( importApp,assApp, associatedType, "networkresourcemanage");
							}
						}	
					}else{
						if("parent".equals(this.assType)) {
							count = structureCommonService.createAssociatedRelation(assApp, importApp, associatedType, "networkresourcemanage");
						} else if("link".equals(this.assType)) {
							count = structureCommonService.createAssociatedRelation( importApp,assApp, associatedType, "networkresourcemanage");
						}
					}
				}else{
					if("parent".equals(this.assType)) {
						count = structureCommonService.createAssociatedRelation(assApp, importApp, associatedType, "networkresourcemanage");
					} else if("link".equals(this.assType)) {
						count = structureCommonService.createAssociatedRelation( importApp,assApp, associatedType, "networkresourcemanage");
					}
				}
				
				
				
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/html");
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				
				
				if(count > 0) {
					String resNameChinese = "";
					if(assEntityType != null){
						try {
							List<BasicEntity> entry = null;
								entry = dictionary.getEntry(assEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								resNameChinese = entry.get(0).getValue("display");
									//System.out.println(infoNameChinese);
						} catch (EntryOperationException e) {
							log.error("获取"+assEntityType+"中文字典失败，可能该字典不存在。");
							e.printStackTrace();
						}
					}
					//添加资源维护记录
					ResourceMaintenance maintenance = new ResourceMaintenance();
					maintenance.setBiz_module(OperationType.NETWORK);
					maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
					maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
					String lresName = "";
					if(assApp.getValue("name") != null){
						lresName = assApp.getValue("name");
					}else{
						lresName = assApp.getValue("label");
					}
					String rresName = "";
					if(importApp.getValue("name") != null){
						rresName = importApp.getValue("name");
					}else{
						rresName = importApp.getValue("label");
					}
					maintenance.setRes_keyinfo(rresName);
					String content = OperationType.INSERT+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
					//String content = OperationType.INSERT +"与" + lresName + OperationType.getAssociatedTypeChinese(associatedType.toString());
					maintenance.setOp_category(OperationType.INSERTLINK);
					maintenance.setContent(content);
					maintenance.setRes_id((Long)importApp.getValue("id"));
					maintenance.setRes_type(importApp.getValue("_entityType").toString());
					networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
					result = gson.toJson("success");
				} else {
					result = gson.toJson("error");
				}
				try {
					log.info("退出createAssAppAction，返回结果result="+result);
					response.getWriter().write(result);
				} catch (IOException e) {
					log.error("退出createAssAppAction，返回结果result="+result+"失败");
					e.printStackTrace();
				}
			}else{
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/html");
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				result = gson.toJson("error");
				try {
					log.info("退出createAssAppAction，返回结果result="+result);
					response.getWriter().write(result);
				} catch (IOException e) {
					log.error("退出createAssAppAction，返回结果result="+result+"失败");
					e.printStackTrace();
				}	
			}
		} catch (Exception e) {
			log.error("退出createAssAppAction，发生异常，结果result="+result);
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description: 更新导入资源
	 * @author：     
	 * @return void     
	 * @date：Jul 6, 2012 1:52:12 PM
	 */
	public void addOrUpdateResourceForImportAction(){
		log.info("进入addOrUpdateResourceForImportAction()，更新导入资源");
		String result="error";
		try{
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			if(this.entityMap!=null){
				//System.out.println(entityMap+"------------");
				this.entityMap=this.entityMap.replace("\\","\\u005c"); //对\符号处理
				Map<String,String> map = gson.fromJson(this.entityMap,new TypeToken<Map<String,String>>(){}.getType());
				//System.out.println(map+"fdfffffffffffffff");
				ApplicationModule module = ModuleProvider.getModule(this.importEntityType);
				if("updateAdd".equals(this.importModel)){
					if("yes".equals(this.hasEntity)){
						BasicEntity updateBe = physicalresService.getPhysicalresById(this.importEntityType,Long.valueOf(this.updateEntityId));
						ApplicationEntity updateApp= ApplicationEntity.changeFromEntity(updateBe);
						for(String key:map.keySet()){
							if(key.equals("id")){
								continue;
							}
							String varType = module.getAttribute(key).getValue("type");
							//通过AE属性的类型转换后，进行set值
							structureCommonService.addValueTo(updateApp, varType, key, map.get(key));
						}
						//System.out.println(updateApp.toMap());
						ApplicationEntity sectionEntity = structureCommonService.getSectionEntity(this.importEntityType, this.updateEntityId);
						//int status = structureCommonService.updateInfoEntity(updateApp);
						int status = this.structureCommonService.updateApplicationEntityBySql(updateApp, module);
						//System.out.println(status);
						if(status==1){
							ApplicationEntity upddateAe = structureCommonService.getSectionEntity(this.importEntityType, this.updateEntityId);
							//添加资源维护记录
							//updateApp
							String updateValue = getUpdateValue(sectionEntity,upddateAe);
							String resName = "";
							if(updateApp.getValue("name") != null){
								resName = updateApp.getValue("name");
							}else{
								resName = updateApp.getValue("label");
							}
							ResourceMaintenance maintenance = new ResourceMaintenance();
							maintenance.setBiz_module(OperationType.NETWORK);
							maintenance.setOp_cause(opCause);
							maintenance.setOp_scene(opScene);
							maintenance.setOp_category(OperationType.RESOURCEUPDATE);
							maintenance.setRes_id((Long)updateApp.getValue("id"));
							maintenance.setRes_type(updateApp.getValue("_entityType").toString());
							maintenance.setRes_keyinfo(resName);
							maintenance.setContent(updateValue);
							networkResourceMaintenanceService.insertResourceMaintenanceRecordsByBizUse(maintenance);
							result="success";
						}
					}else{
						ApplicationEntity importApp = module.createApplicationEntity();
						for(String key:map.keySet()){
							if("id".equals(key)){
								long maxId=this.physicalresService.getMaxIdFromTable(this.importEntityType);
								long id = Long.valueOf(map.get(key));
								List<ResourceMaintenance> listRM=this.networkResourceMaintenanceService.getResourceMaintenanceRecordsByResTypeAndResId(this.importEntityType,id);
								if(maxId>=id ||(listRM != null && !listRM.isEmpty())){//判断当前id是否需要重新生成
									map.put("id", this.structureCommonService.getEntityPrimaryKey(this.importEntityType)+"");
								}
							}
							String varType = module.getAttribute(key).getValue("type");
							//通过AE属性的类型转换后，进行set值
							structureCommonService.addValueTo(importApp, varType, key, map.get(key));
						
						}
						if(importApp.getValue("id")==null || "".equals(importApp.getValue("id"))){
							importApp.setValue("id",structureCommonService.getEntityPrimaryKey(this.importEntityType));
						}
						int status = structureCommonService.saveInfoEntity(importApp,"networkresourcemanage");
						if(status==1){
							//添加资源维护记录
								String resName = "";
								if(importApp.getValue("name") != null){
									resName = importApp.getValue("name");
								}else{
									resName = importApp.getValue("label");
								}
								ResourceMaintenance maintenance = new ResourceMaintenance();
								maintenance.setBiz_module(OperationType.NETWORK);
								maintenance.setOp_cause(opCause);
								maintenance.setOp_scene(opScene);
								maintenance.setOp_category(OperationType.RESOURCEINSERT);
								maintenance.setRes_id((Long)importApp.getValue("id"));
								maintenance.setRes_type(importApp.getValue("_entityType").toString());
								maintenance.setRes_keyinfo(resName);
								networkResourceMaintenanceService.insertResourceMaintenanceRecordsByBizUse(maintenance);
							result=importApp.getValue("id")+"";
						}
					}
				}else if("deleteAdd".equals(this.importModel)){
					ApplicationEntity importApp = module.createApplicationEntity();
					if("yes".equals(this.hasEntity)){
						BasicEntity updateBe = physicalresService.getPhysicalresById(this.importEntityType,Long.valueOf(this.updateEntityId));
						ApplicationEntity updateApp= ApplicationEntity.changeFromEntity(updateBe);
						int state=structureCommonService.delEntityByRecursion(updateApp, "networkresourcemanage");	
						if(state==1){
							String resNameChinese = "";
							if(importEntityType != null){
								try {
									List<BasicEntity> entry = null;
										entry = dictionary.getEntry(importEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
										resNameChinese = entry.get(0).getValue("display");
											//System.out.println(infoNameChinese);
								} catch (EntryOperationException e) {
									log.error("获取"+importEntityType+"中文字典失败，可能该字典不存在。");
									e.printStackTrace();
								}
							}
							//添加资源维护记录
								String resName = "";
								if(updateApp.getValue("name") != null){
									resName = updateApp.getValue("name");
								}else{
									resName = updateApp.getValue("label");
								}
								ResourceMaintenance maintenance = new ResourceMaintenance();
								maintenance.setBiz_module(OperationType.NETWORK);
								maintenance.setOp_cause(opCause);
								maintenance.setOp_scene(opScene);
								maintenance.setOp_category(OperationType.RESOURCEDELETE);
								maintenance.setRes_id((Long)updateApp.getValue("id"));
								maintenance.setRes_type(updateApp.getValue("_entityType").toString());
								maintenance.setRes_keyinfo("资源被删除前的关键信息<"+resNameChinese+">："+resName);
								networkResourceMaintenanceService.insertResourceMaintenanceRecordsByBizUse(maintenance);
							for(String key:map.keySet()){
								
								String varType = module.getAttribute(key).getValue("type");
								//通过AE属性的类型转换后，进行set值
								structureCommonService.addValueTo(importApp, varType, key, map.get(key));

							
							}
							if(importApp.getValue("id")==null || "".equals(importApp.getValue("id"))){
								importApp.setValue("id",structureCommonService.getEntityPrimaryKey(this.importEntityType));
							}
							int status = structureCommonService.saveInfoEntity(importApp,"networkresourcemanage");
							if(status==1){
								//添加资源维护记录
								String resName1 = "";
								if(importApp.getValue("name") != null){
									resName1 = importApp.getValue("name");
								}else{
									resName1 = importApp.getValue("label");
								}
								ResourceMaintenance maintenance1 = new ResourceMaintenance();
								maintenance1.setBiz_module(OperationType.NETWORK);
								maintenance1.setOp_cause(opCause);
								maintenance1.setOp_scene(opScene);
								maintenance1.setOp_category(OperationType.RESOURCEINSERT);
								maintenance1.setRes_id((Long)importApp.getValue("id"));
								maintenance1.setRes_type(importApp.getValue("_entityType").toString());
								maintenance1.setRes_keyinfo(resName1);
								networkResourceMaintenanceService.insertResourceMaintenanceRecordsByBizUse(maintenance1);
								result="success";
							}
						}
					}else{
						for(String key:map.keySet()){
							
							String varType = module.getAttribute(key).getValue("type");
							//通过AE属性的类型转换后，进行set值
							structureCommonService.addValueTo(importApp, varType, key, map.get(key));

							
						}
						if(importApp.getValue("id")==null || "".equals(importApp.getValue("id"))){
							importApp.setValue("id",structureCommonService.getEntityPrimaryKey(this.importEntityType));
						}
						int status = structureCommonService.saveInfoEntity(importApp,"networkresourcemanage");
						if(status==1){
							//添加资源维护记录
								String resName = "";
								if(importApp.getValue("name") != null){
									resName = importApp.getValue("name");
								}else{
									resName = importApp.getValue("label");
								}
								ResourceMaintenance maintenance = new ResourceMaintenance();
								maintenance.setBiz_module(OperationType.NETWORK);
								maintenance.setOp_cause(opCause);
								maintenance.setOp_scene(opScene);
								maintenance.setOp_category(OperationType.RESOURCEINSERT);
								maintenance.setRes_id((Long)importApp.getValue("id"));
								maintenance.setRes_type(importApp.getValue("_entityType").toString());
								maintenance.setRes_keyinfo(resName);
								networkResourceMaintenanceService.insertResourceMaintenanceRecordsByBizUse(maintenance);
							result="success";
						}
					}			
				}else if("newAdd".equals(this.importModel)){
						if("yes".equals(this.hasEntity)){
							BasicEntity updateBe = physicalresService.getPhysicalresById(this.importEntityType,Long.valueOf(this.updateEntityId));
							ApplicationEntity updateApp= ApplicationEntity.changeFromEntity(updateBe);
							for(String key:map.keySet()){
								if(key.equals("id")){
									continue;
								}
								String varType = module.getAttribute(key).getValue("type");
								//通过AE属性的类型转换后，进行set值
								structureCommonService.addValueTo(updateApp, varType, key, map.get(key));
							}
							ApplicationEntity sectionEntity = structureCommonService.getSectionEntity(this.importEntityType, this.updateEntityId);
							int status = this.structureCommonService.updateApplicationEntityBySql(updateApp, module);
							if(status==1){
								ApplicationEntity upddateAe = structureCommonService.getSectionEntity(this.importEntityType, this.updateEntityId);
								//添加资源维护记录
								//updateApp
								String updateValue = getUpdateValue(sectionEntity,upddateAe);
								String resName = "";
								if(updateApp.getValue("name") != null){
									resName = updateApp.getValue("name");
								}else{
									resName = updateApp.getValue("label");
								}
								ResourceMaintenance maintenance = new ResourceMaintenance();
								maintenance.setBiz_module(OperationType.NETWORK);
								maintenance.setOp_cause(opCause);
								maintenance.setOp_scene(opScene);
								maintenance.setOp_category(OperationType.RESOURCEUPDATE);
								maintenance.setRes_id((Long)updateApp.getValue("id"));
								maintenance.setRes_type(updateApp.getValue("_entityType").toString());
								maintenance.setRes_keyinfo(resName);
								maintenance.setContent(updateValue);
								networkResourceMaintenanceService.insertResourceMaintenanceRecordsByBizUse(maintenance);
								result="success";
							}
						}else{
							ApplicationEntity importApp = module.createApplicationEntity();
							for(String key:map.keySet()){
								
								String varType = module.getAttribute(key).getValue("type");
								//通过AE属性的类型转换后，进行set值
								structureCommonService.addValueTo(importApp, varType, key, map.get(key));

								
							}
							if(importApp.getValue("id")==null || "".equals(importApp.getValue("id"))){
								importApp.setValue("id",structureCommonService.getEntityPrimaryKey(this.importEntityType));
							}
							int status = structureCommonService.saveInfoEntity(importApp,"networkresourcemanage");
							if(status==1){
								//添加资源维护记录
									String resName = "";
									if(importApp.getValue("name") != null){
										resName = importApp.getValue("name");
									}else{
										resName = importApp.getValue("label");
									}
									ResourceMaintenance maintenance = new ResourceMaintenance();
									maintenance.setBiz_module(OperationType.NETWORK);
									maintenance.setOp_cause(opCause);
									maintenance.setOp_scene(opScene);
									maintenance.setOp_category(OperationType.RESOURCEINSERT);
									maintenance.setRes_id((Long)importApp.getValue("id"));
									maintenance.setRes_type(importApp.getValue("_entityType").toString());
									maintenance.setRes_keyinfo(resName);
									networkResourceMaintenanceService.insertResourceMaintenanceRecordsByBizUse(maintenance);
								result="success";
							}
						}			
				}	
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			result = gson.toJson(result);
			try {
				log.info("退出addOrUpdateResourceForImportAction()，返回结果reuslt="+result);
				response.getWriter().write(result);
			} catch (IOException e) {
				log.info("退出addOrUpdateResourceForImportAction()，返回结果reuslt="+result+"失败。");
				e.printStackTrace();
			}
		}catch(Exception e){
			log.info("退出addOrUpdateResourceForImportAction()，发生异常结果reuslt="+result);
			e.printStackTrace();
		}
		
		

		
	}
	/**
	 * 
	 * @description: 下载资源导入模板
	 * @author：
	 * @return     
	 * @return String     
	 * @date：2012-5-28 下午03:12:11
	 */
	public String downloadResourceImportModuleAction(){
		log.info("进入downloadResourceImportModuleAction，下载资源导入模板。");
		try {
			ApplicationModule module = ModuleProvider.getModule(this.importEntityType);
			Map<String,Object> moduleResourceMap = new HashMap<String,Object>();
			Map<String, Object> searchedResModuleMap = module.toMap();
			Map<String,Object> orderIdMap = new HashMap<String,Object>();
			Map<String, Object> titleMap = new HashMap<String, Object>();
			if(this.selectResChosenTypeName!=null&&!"".equals(this.selectResChosenTypeName)){
				this.selectResChosenTypeName=new String(this.selectResChosenTypeName.getBytes("ISO-8859-1"), "UTF-8");
				//System.out.println(this.selectResChosenTypeName);
				//System.out.println(this.moduleFileName);
				moduleResourceMap.put("parent::所属上级资源","测试资源");
				titleMap.put("parent::所属上级资源","parent::所属上级资源");
				orderIdMap.put("1", "parent::所属上级资源");
			}
			if(searchedResModuleMap != null) {
				for(String key : searchedResModuleMap.keySet()) {
					try {
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							//数据字典，中英文转换
							entry = dictionary.getEntry(key + "," + this.importEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								titleMap.put(key, entry.get(0).getValue("display"));
								orderIdMap.put( String.valueOf(Integer.valueOf(entry.get(0).getValue("orderID").toString())+1), key);
							}
						}
						
					} catch (EntryOperationException e) {
						log.error("获取"+this.importEntityType+"属性字段"+key+"的中文字典失,可能该字典不存在");
						e.printStackTrace();
						titleMap.put(key, key);
					}
					if (!"_entityType".equals(key) && !"_entityId".equals(key)&&!"id".equals(key)) {
						String type = module.getAttribute(key).getValue("type");
						if (type.equals("java.lang.Integer")||type.equals("java.lang.long")) {
							moduleResourceMap.put(key,0);
						} else if (type.equals("java.lang.Double")||type.equals("java.lang.Float")) {
							moduleResourceMap.put(key,0.0);
						} else if (type.equals("java.lang.Boolean")) {
							moduleResourceMap.put(key,"是");
						} else if (type.equals("java.util.Date")) {
							SimpleDateFormat formater = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							moduleResourceMap.put(key,formater.format(new Date()));
						} else {
							moduleResourceMap.put(key,"请填写"+titleMap.get(key));
						}
					}
				}
				Map<String,Object> sortedMap = quickSort.sortMap(titleMap,orderIdMap);//中文字典排序
				if(sortedMap!=null){
					titleMap = sortedMap;
				}
				sortedMap = quickSort.sortMap(moduleResourceMap,orderIdMap);
				if(sortedMap!=null){
					moduleResourceMap = sortedMap;
				}
					//获取标题行
					List titleList = new ArrayList();
					if(titleMap != null)
					for (String key : titleMap.keySet()) {
						if(!"id".equals(key)) {
							titleList.add(titleMap.get(key));
						}
					}
					//内容数据行
					List<List> dataList = new ArrayList<List>();
						List contentList = new ArrayList();
						if(moduleResourceMap != null) {
							for(String key : moduleResourceMap.keySet()) {
								if(!("id".equals(key) || "_entityId".equals(key) || "_entityType".equals(key))) {
									contentList.add(moduleResourceMap.get(key));
								}
							}
						}
						dataList.add(contentList);

					
					if (!titleList.isEmpty() && !dataList.isEmpty()) {
						this.resultInputStream = physicalresAction.creatExcel2003InputStream(titleList, dataList);
						//System.out.println(moduleFileName+"....................");
						log.info("退出downloadResourceImportModuleAction，返回结果result=success");
						return "success";
					} else {
						log.info("退出downloadResourceImportModuleAction，返回结果result=error");
						return "error";
					}
				}
			log.info("退出downloadResourceImportModuleAction，返回结果result=error");
			return "error";
		} catch (Exception e) {
			log.info("退出downloadResourceImportModuleAction，发生异常，返回结果result=error");
			e.printStackTrace();
			return "error";
		}
	}
	/**
	 * 
	 * @description:  按orderID排序取得属性key value
	 * @author：     
	 * @return void     
	 * @date：Jul 10, 2012 11:19:00 AM
	 */
	public void getAttributesByOrderIdAction(){
		log.info("进入getAttributesByOrderIdAction，按orderID排序取得属性的key value对信息");
		try {
			ApplicationModule module = ModuleProvider.getModule(this.chosenType);
			Map<String, Object> attrMap = module.toMap();
			//System.out.println(attrMap+"*****************");
			Map<String,Object> resultMap = new HashMap<String,Object>();
			Map<String,Object> orderMap = new HashMap<String,Object>();
			try {
				for(String key : attrMap.keySet()) {
					if(key.equals("id")||key.equals("name")||key.equals("label")||key.equals("assettag")||key.equals("barcode")){
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							//数据字典，中英文转换
							if(dictionary.hasEntry(key + "," + this.chosenType + ",networkResourceDefination")){
								entry = dictionary.getEntry(key + "," + this.chosenType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							}
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								if(entry.get(0).getValue("display") != null) {
									resultMap.put(key,entry.get(0).getValue("display")+"");
									orderMap.put(entry.get(0).getValue("orderID")+"",key);
								}
							} 
						}
					}
								
				}
			} catch (EntryOperationException e) {
				log.error("获取"+chosenType+"的属性字段中文字典发生异常，可能字典不存在。");
				e.printStackTrace();
			}
			//属性排序
			Map<String,Object> sortedMap = quickSort.sortMap(resultMap,orderMap);
			if(sortedMap!=null){
				resultMap=sortedMap;
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(resultMap);
			try {
				log.info("退出getAttributesByOrderIdAction，返回结果：result="+result);
				response.getWriter().write(result);
			} catch (IOException e) {
				log.info("退出getAttributesByOrderIdAction，返回结果：result="+result+"失败");
				e.printStackTrace();
			}
		} catch (RuntimeException e) {
			log.info("退出getAttributesByOrderIdAction，发生运行时异常");
			e.printStackTrace();
		}
	}
	
	//获取修改前后对象属性修改信息
	public String getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe){
		log.info("进入getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe)，获取修改前后对象属性修改信息");
		log.info("进入getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe)，originalAe="+originalAe.toMap());
		log.info("进入getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe)，updateAe="+updateAe.toMap());
		String reString = "";
		ApplicationModule module = moduleLibrary.getModule(originalAe.getType());
		for(String key:module.keyset()){
			//System.out.println(originalAe.getValue("width")+ "     " +updateAe.getValue("width"));
			if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
				if(originalAe.getValue(key) == null && updateAe.getValue(key) != null || ("null".equals(originalAe.getValue(key)) && !"null".equals(updateAe.getValue(key)) ) || ("".equals(originalAe.getValue(key)) && !"".equals(updateAe.getValue(key)))){
					String keyString = "";
					try {
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							entry = dictionary.getEntry(key + "," + originalAe.getType() + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								keyString = "<"+ entry.get(0).getValue("display").toString().trim() +">";
							}
						}
					} catch (EntryOperationException e) {
						log.error("获取"+originalAe.getType()+"属性字段"+key+"中文字典失败，可能该字典不存在。");
						keyString =  "<"+key+">" ;
					}
					reString = reString + keyString + "，修改前：" + " 空值 " + ";修改后： " + updateAe.getValue(key) + " $_$" + (char)10;
				}else if((originalAe.getValue(key) != null && updateAe.getValue(key) == null ) || (!"null".equals(originalAe.getValue(key)) && "null".equals(updateAe.getValue(key)) ) || (!"".equals(originalAe.getValue(key)) && "".equals(updateAe.getValue(key)))){
					String keyString = "";
					try {
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							entry = dictionary.getEntry(key + "," + originalAe.getType() + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								keyString =  "<"+ entry.get(0).getValue("display").toString().trim() +">";
							}
						}
					} catch (EntryOperationException e) {
						log.error("获取"+originalAe.getType()+"属性字段"+key+"中文字典失败，可能该字典不存在。");
						keyString =  "<"+key+">";
					}
					reString = reString + keyString + "，修改前： " + originalAe.getValue(key) + " ;修改后：" + " 空值 " + " $_$" + (char)10;
				}else if(originalAe.getValue(key) != null && updateAe.getValue(key) != null  && !"null".equals(originalAe.getValue(key)) && !"null".equals(updateAe.getValue(key)) && !"".equals(originalAe.getValue(key)) && !"".equals(updateAe.getValue(key))){
					if(!originalAe.getValue(key).equals(updateAe.getValue(key))){
						String keyString = "";
						try {
							List<BasicEntity> entry = null;
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								entry = dictionary.getEntry(key + "," + originalAe.getType() + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								
							}
							if(entry != null && !entry.isEmpty()) {
								if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
									keyString =  "<"+ entry.get(0).getValue("display").toString().trim() +">";
								}
							}
						} catch (EntryOperationException e) {
							keyString =  "<"+key+">" ;
						}
						reString = reString + keyString + "，修改前： " + originalAe.getValue(key) + " ;修改后： " + updateAe.getValue(key) +" $_$" + (char)10;
					}
				}
			}
		}
		log.info("退出getUpdateValue(ApplicationEntity originalAe , ApplicationEntity updateAe),返回结果reString="+reString);
		return reString;
	}
	
	//获取Sheet
	public void getFileSheetAction(){
		log.info("进入getFileSheetAction()，获取excel表的sheet列表信息。");
		List<String> list = new ArrayList<String>();
		 try {
			FileInputStream fis = new FileInputStream(this.file);
			Workbook wb = WorkbookFactory.create(fis);  
			for(int i = 0;;i++){
				try {
					list.add(wb.getSheetName(i));
				} catch (Exception e) {
					log.error("获取excel表的第"+(i+1)+"个sheet发生异常。");
					break;
				}
			}
		} catch (Exception e) {
			log.error("获取excel表的sheet列表发生异常。list="+list);
			e.printStackTrace();
		}    
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(list);
		try {
			log.info("退出getFileSheetAction()。返回result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.info("退出getFileSheetAction()。返回result="+result+"失败");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description: 精确匹配或模糊匹配取得关联上级资源
	 * @author：     
	 * @return void     
	 * @date：Nov 23, 2012 10:12:37 AM
	 */
	public void getMatchResourceToAssociateForImportAction(){
		log.info("进入getMatchResourceToAssociateForImportAction()，精确匹配或模糊匹配取得关联上级资源。");
		String enType="";
		String assType="";
		String areaName="";
		if(areaId!=null){
			BasicEntity be = physicalresService.getPhysicalresById("Sys_Area", Long.valueOf(areaId));
			if(be!=null){
				areaName = be.getValue("name")+"";
			}
		}
		if(this.selectResChosenType != null && !"".equals(this.selectResChosenType)) {
			enType = this.selectResChosenType.substring(0, this.selectResChosenType.lastIndexOf("_"));
			assType = this.selectResChosenType.substring(this.selectResChosenType.lastIndexOf("_") + 1);
		}
		ApplicationEntity parentAe=null;
		//获取隶属的entity的map集合
		this.resultMapList = new ArrayList<Map<String,Object>>();
		//this.selectAttrExactMatch需要进行精确查找的字段名,columnContent为条件，查询隶属资源
		List<BasicEntity> assEntityList = null;
		if("exactMatch".equals(this.rdoAssModel)) {
			//精确查找
			assEntityList = physicalresService.getPhysicalresByCondition(
					enType, this.selectAttrExactMatch, this.matchContent, "exactMatch");
		} else if("indistinctMatch".equals(this.rdoAssModel)) {
			//模糊查找
			assEntityList = physicalresService.getPhysicalresByCondition(
					enType, this.selectAttrIndistinctMatch, this.matchContent, "indistinctMatch");
		} else if("exactContent".equals(this.rdoAssModel)){
			parentAe = this.structureCommonService.getSectionEntity(enType,this.assEntityId);	
		}
		
		List<ApplicationEntity> assAppList = new ArrayList<ApplicationEntity>();
		if(assEntityList != null && !assEntityList.isEmpty()) {
			ApplicationEntity ae=null;
			for (BasicEntity be : assEntityList) {
				ae = ApplicationEntity.changeFromEntity(be);
				String name="";
				String type=ae.getValue("_entityType")+"";
				if("Sys_Area".equals(type)){ 
					name=ae.getValue("name");
					if(areaName.equals(name)){
						assAppList.add(ae);
					}else{
						ApplicationEntity se=physicalresService.searchParentAreaResourceForSrc(ae, areaName, assType);
						if(se!=null){
							assAppList.add(ae);
						}
					}
				}else{
					ApplicationEntity se=physicalresService.searchParentAreaResourceForSrc(ae, areaName, assType);
					if(se!=null){
						assAppList.add(ae);
					}
				}	
			}
		}else{
			if(parentAe!=null){
				assAppList.add(parentAe);
			}
			
		}
		assEntityList=physicalresService.getNoAssociateResource(enType);
		if(assEntityList != null && !assEntityList.isEmpty()) {
			ApplicationEntity ae=null;
			for (BasicEntity be : assEntityList) {
				String attr="";
				if("exactMatch".equals(this.rdoAssModel)) {
					attr=be.getValue(this.selectAttrExactMatch)+"";
					if(attr.equals(this.matchContent)){
						ae = ApplicationEntity.changeFromEntity(be);	
						assAppList.add(ae);	
					}
				}else if("indistinctMatch".equals(this.rdoAssModel)) {
					attr=be.getValue(this.selectAttrIndistinctMatch)+"";
					if(attr.contains(this.matchContent)){
						ae = ApplicationEntity.changeFromEntity(be);	
						assAppList.add(ae);	
					}
				}
				
			}
		}
		if(!assAppList.isEmpty()) {
			//获取隶属资源map
			for (ApplicationEntity app : assAppList) {
				//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
				Map<String, Object> assAppMap = ResourceCommon.applicationEntityConvertMap(app);
				//将null值转换为空串值
				assAppMap = ResourceCommon.mapRecombinationToString(assAppMap);
				assAppMap.put("aeType", app.getType());
				this.resultMapList.add(assAppMap);
			}
		} 
		String result="";
		if(this.resultMapList!=null && !this.resultMapList.isEmpty()){
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			result = gson.toJson(this.resultMapList);
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出getMatchResourceToAssociateForImportAction()，返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.info("退出getMatchResourceToAssociateForImportAction()，返回结果result="+result+"失败");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description: 获取与导入的资源有相同条件值的数据库已存在资源
	 * @author：     
	 * @return void     
	 * @date：Nov 23, 2012 5:43:00 PM
	 */
	public void getResourcesWithSameConditionForImportAction(){
		log.info("进入getResourcesWithSameConditionForImportAction()，获取与导入的资源有相同条件值的数据库已存在资源。");
		String areaName="";
		if(areaId!=null){
			BasicEntity be = physicalresService.getPhysicalresById("Sys_Area", Long.valueOf(areaId));
			if(be!=null){
				areaName = be.getValue("name")+"";
			}
		}
		List<BasicEntity> assEntityList = physicalresService.getPhysicalresByCondition(this.importEntityType, this.attributeChange,this.matchContent , "exactMatch");
		List<ApplicationEntity> assAppList = new ArrayList<ApplicationEntity>();
		if(assEntityList != null && !assEntityList.isEmpty()) {
			ApplicationEntity ae=null;
			for (BasicEntity be : assEntityList) {
				ae = ApplicationEntity.changeFromEntity(be);
				String name="";
				String type=ae.getValue("_entityType")+"";
				if("Sys_Area".equals(type)){ 
					name=ae.getValue("name");
					if(areaName.equals(name)){
						assAppList.add(ae);
					}else{
						ApplicationEntity se=physicalresService.searchParentAreaResourceForSrc(ae, areaName, null);
						if(se==null){
							se=physicalresService.searchParentAreaResourceForSrc(ae, areaName, "link");
						}
						if(se!=null){
							assAppList.add(ae);
						}
					}
				}else{
					ApplicationEntity se=physicalresService.searchParentAreaResourceForSrc(ae, areaName, null);
					if(se==null){
						se=physicalresService.searchParentAreaResourceForSrc(ae, areaName, "link");
					}
					if(se!=null){
						assAppList.add(ae);
					}
				}	
				
			}
		}
		assEntityList=physicalresService.getNoAssociateResource(this.importEntityType);
		if(assEntityList != null && !assEntityList.isEmpty()) {
			ApplicationEntity ae=null;
			for (BasicEntity be : assEntityList) {
				String attr=be.getValue(this.attributeChange)+"";
				if(attr.equals(this.matchContent)){
					ae = ApplicationEntity.changeFromEntity(be);	
					assAppList.add(ae);	
				}
			}
		}
		if(!assAppList.isEmpty()) {
			//获取数据库已存在资源Entity
			this.entityHaveMap = new ArrayList<Map<String,Object>>();
			this.hasChildMap = new ArrayList<Map<String,Object>>();
			for (ApplicationEntity app : assAppList) {
				if("deleteAdd".equals(this.importEntityType)){
					ApplicationEntity[] appArray=structureCommonService.getStrutureSelationsApplicationEntity(app, AssociatedType.CHILD,"networkresourcemanage");
					ApplicationEntity[] appArray2=structureCommonService.getStrutureSelationsApplicationEntity(app, AssociatedType.LINK,"networkresourcemanage");
					if(appArray.length==0&&appArray2.length==0){
						Map<String, Object> map = new HashMap<String,Object>();
						map.put("has","no");
						hasChildMap.add(map);
					}else{
						Map<String, Object> map = new HashMap<String,Object>();
						map.put("has","has");
						hasChildMap.add(map);
					}
				}
				//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
				Map<String, Object> assAppMap = ResourceCommon.applicationEntityConvertMap(app);
				//将null值转换为空串值
				assAppMap = ResourceCommon.mapRecombinationToString(assAppMap);
				assAppMap.put("aeType", app.getType());
				entityHaveMap.add(assAppMap);
			}
		} 
		String result="";
		this.bigResultMap = new HashMap<String,Object>();
		if(this.entityHaveMap!=null && !this.entityHaveMap.isEmpty()){
			bigResultMap.put("entityHaveMap", entityHaveMap);
		}
		if(this.hasChildMap!=null && !this.hasChildMap.isEmpty()){
			bigResultMap.put("hasChildMap", hasChildMap);
		}
		if(bigResultMap!=null && !this.bigResultMap.isEmpty()){
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			result = gson.toJson(this.bigResultMap);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出getResourcesWithSameConditionForImportAction()，返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.info("退出getResourcesWithSameConditionForImportAction()，返回结果result="+result+"失败");
			e.printStackTrace();
		}
	}
	
	public int getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}
}
