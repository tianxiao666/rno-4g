package com.iscreate.op.action.networkresourcemanage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.constant.OperationType;
import com.iscreate.op.pojo.maintain.ResourceMaintenance;
import com.iscreate.op.pojo.maintain.ServiceMaintenance;
import com.iscreate.op.service.dictionary.ResourceDictionaryService;
import com.iscreate.op.service.maintain.NetworkResourceMaintenanceService;
import com.iscreate.op.service.networkresourcemanage.PhysicalresService;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ApplicationModule;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
import com.iscreate.plat.networkresource.application.tool.XMLAEMLibrary;
import com.iscreate.plat.networkresource.common.action.ActionHelper;
import com.iscreate.plat.networkresource.common.service.StructureCommonService;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.GetChineseFirstLetter;
import com.iscreate.plat.networkresource.common.tool.QuickSort;
import com.iscreate.plat.networkresource.common.tool.ResourceCommon;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.plat.networkresource.dictionary.SearchScope;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.tools.InterfaceUtil;


public class PhysicalresAction {
	private static final Log log = LogFactory.getLog(PhysicalresAction.class);
	private PhysicalresService physicalresService;
	private StructureCommonService structureCommonService;
	private XMLAEMLibrary moduleLibrary;
	private Dictionary dictionary;

	private String currentEntityType;//当前节点类型
	private String currentEntityId;//当前节点id
	private Map<String, Object> currentEntityMap; //当前节点
	private Map<String, Object> currentEntityChineseMap;//获取中文的key
	
	private Map<String, Object> parentEntityMap; //父级节点
	private Map<String, Object> grandpaEntityMap; //爷级节点
	
	private String chooseResEntityType;//隶属资源的类型
	private String chooseResEntityId;//隶属资源的id
	private String needChildEntityType;//隶属资源所需的子资源的类型
	/*需要更新的物理资源属性*/
	private String updatedEntityType;
	
	/*更新关系时所需要的变量(老与新的比较)*/
	private String operatedCurrentEntityId;
	private String operatedCurrentEntityType;
	private String oldParentResEntityId;
	private String oldParentResEntityType;
	private String newParentResEntityId;
	private String newParentResEntityType;
	
	private String loadedType;//要被加载子类型的父类型
	
	private Map<String, Object> addedResMap;//添加物理资源(加载被添加的物理资源的内容列表)
	
	/*上传图片时使用(增加图片)*/
	private String photoParentEntityType;
	private String photoParentEntityId;
	private String photoName;
	private String photoOldName;
	
	/*上传图片时使用(修改图片)*/
	private String currentPhotoEntityType;
	private String currentPhotoEntityId;
	
	
	
	/*需要添加的物理资源属性*/
	private String addedResEntityType;//被添加的物理资源类型
	private String addedResParentEntityType;//被添加的物理资源的父entity类型
	private String addedResParentEntityId;//被添加的物理资源的父entity ID
	
	/*处理那特殊的三种物理资源时，进行使用，获取其父级entity信息*/
	private String parentEntityId;
	private String parentEntityType;
	//当前资源相关联的类型组
	private String parentEntityTypeGrp;
	
	
	private List<Map<String, Object>> noUseTerminalList;//加载ODM以及其端子
	private List<Map<String, Object>> useOneTimeTerminalList;//加载ODM以及其端子
	private List<Map<String, Object>> noUseFiberCoreList;//加载纤芯以及其光缆段(未用纤芯)
	private List<Map<String, Object>> resultList;//用于已生成的纤芯成端、跳纤或尾纤、熔纤接续的显示
	private String entityTypeGroup;//获取其要被删除的entity组类型(纤芯成端、熔纤接续中使用)
	
	private List<Map<String, Object>> canUseFiberCoreList;//进行熔纤接续维护时，可使用的纤芯集合
	
	private String parentEntityName;
	
	private String loadPartPage;//AJAX加载小部分页面
	private String loadBigPage;//AJAX加载大部分页面
	private String loadBasicPage;//AJAX加载基本信息页面
	/*资源导入*/
	//所有的资源类型信息
	private List<Map<String, Object>> allTypeMapList; //所有类型
	private String chosenType; //选择的类型
	private List<Map<String, Object>> assTypeMapList; //关联的类型集合
	private List<String> attrList; //上级资源类型对应的key集合
	
	/*资源查询*/
	private String selectResType; //需要查询的资源的类型
	private String selectResChosenType; //需要查询的资源所关联的类型
	private String hasCondition;//判断是否根据属性查询资源
	private Map<String, Object> searchMap; //需要查询的资源的map
	
	private List<Map<String, Object>> searchResourceMapList;
	//查询资源大集合，包括中文标题，资源信息
	private Map<String, Object> searchBigMap;
	//关联查询条件中所选择的entity的信息
	private String assEntityId;
	private String assEntityType;
	
	
	/*分页处理属性*/
	private int pageIndex;
	private int lastPage;
	private int nextPage;
	private int pageSize;
	//是否尾页的状态
	private String lastPageStatus;
	
	//导出excel
	//导出文件的输入流
	private InputStream resultInputStream;
	private Map<String, Object> sheetHeadMap;
	//文件名
	private String chaxunFileName;
	
	//需要查找父类型的当前节点类型
	private String targetEntityType;
	
	/*属性判断*/
	//判断属性是否为非空的map
	private Map<String, Object> nullableMap;
	//判断属性类型的map
	private Map<String, Object> attrTypeMap;
	//判断属性长度的map
	private Map<String,Object> attrLengthMap;
	//生成下拉框的map
	private Map<String, Object> dropdownListMap;
	
	private QuickSort<Map<String,Object>> quickSort;//排序
	
	private List<Map<String,Object>> photoMapList=new ArrayList<Map<String,Object>>(); //资源关联图片list
	
	private String hasPhotos; //判断资源是否有关联图片 true为有  false为无
	
	//页面加载管理模式与浏览模式控制
	private String modelType;
	
	private List<Map<String,Object>> odmTerminalList = new ArrayList<Map<String,Object>>();//模块端子list
	
	
	//批量增加模块端子
	private String startOdmNumber; //模块开始编号
	private String endOdmNumber;//模块结束编号
	private String startTerminalNumber;//端子开始编号
	private String endTerminalNumber;//端子结束编号
	private String useOdmName;//是否使用模块前缀
	private String useLinklabel;//是否使用连接符
	private String linklabel;//使用连接符
	private String leftUpTerminalNumber;//单回路端子名（左上）
	private String rightUpTerminalNumber;//单回路端子名（右上）
	private String leftDownTerminalNumber;//单回路端子名（左下）
	private String rightDownTerminalNumber;//单回路端子名（右上）
	private String tLinkLabel;//2M回路端子与单回路端子连接符

	//关联对象对应数据字典(父子关系)
	private Map<String,Object> infoChildMapChinese;
	
	//关联对象对应数据字典(关联关系)
	private Map<String,Object> infoLinkMapChinese;
	
	
	//关联对象对应数据(父子关系)
	private Map<String,List<Map<String, Object>>> childAssociatedResourcMap;
	
	//关联对象对应数据(关联关系)
	private Map<String,List<Map<String, Object>>> linkAssociatedResourcMap;
	
	//关联对象对应数量(父子关系)
	private Map<String, Object> childAssociatedResourcCount;
	
	
	//关联对象对应数量(关联关系)
	private Map<String, Object> linkAssociatedResourcCount;
	
	
	private int associatedResourcCount;
	
	private String areaId;
	private String areaName;
	private String currentEntityLabel;
	
	/*上传附件使用*/
	private String attachmentParentEntityType;
	private String attachmentParentEntityId;
	private String attachmentName;
	private String attachmentOldName;
	
	/*修改附件)*/
	private String currentAttachmentEntityType;
	private String currentAttachmentEntityId;
	
	private String showType;
	
	
	private String longitude;
	private String latitude;//经纬度
	
	private Map<String,Object> odmandterminallayoutMap;//模块端子默认布局设置
	
	private ResourceDictionaryService resourceDictionaryService;//字典
	
	private String firstLetter;//首字母
	
	private String dateString;//查询资源 日期字符串条件
	
	private String aetgName; //资源组名
	
	private String assRangeEntityId;//起点资源id
	
	private String assRangeEntityType;//起点资源类型
	
	private String selectRangeResChosenType;//选择的起点资源类型
	
	private String searchConditionText;//查询条件内容
	
	private String searchConditionType;//查询条件类型
	
	private String isAetg; //判断查询的是否是aetg
	
	
	private String linkType; //连接类型
	
	
	private List<Map<String,Object>> aetgList;//扁平化呈现组信息
	
	private String searchType;//搜索类型
	private String searchMsg;//搜索信息
	
	private Map<String,Object> titleMap;//资源属性中文
	
	
	private String op_scene;//操作场景
	
	private String op_cause;//操作原因
	
	
	//资源维护记录
	private NetworkResourceMaintenanceService networkResourceMaintenanceService;
	
	//业务模块
	private String bizModule;
	
	
	//业务信息唯一标识
	private String bizProcessCode;
	
	
	//业务信息id
	private String bizProcessId;
	
	
	private String attrMap; //属性key value
	
	private String exportModel; //自定义导出
	
	private String forwardurl;//跳转url
	
	private String  hasPhoto;
	
	private String cName;//中文名
	
	private String resParentName;//资源归属父级名
	private String resParentTitle;
	
	

	public String getResParentName() {
		return resParentName;
	}

	public void setResParentName(String resParentName) {
		this.resParentName = resParentName;
	}

	public String getResParentTitle() {
		return resParentTitle;
	}

	public void setResParentTitle(String resParentTitle) {
		this.resParentTitle = resParentTitle;
	}

	public String getTLinkLabel() {
		return tLinkLabel;
	}

	public void setTLinkLabel(String linkLabel) {
		tLinkLabel = linkLabel;
	}

	public String getLeftUpTerminalNumber() {
		return leftUpTerminalNumber;
	}

	public void setLeftUpTerminalNumber(String leftUpTerminalNumber) {
		this.leftUpTerminalNumber = leftUpTerminalNumber;
	}

	public String getRightUpTerminalNumber() {
		return rightUpTerminalNumber;
	}

	public void setRightUpTerminalNumber(String rightUpTerminalNumber) {
		this.rightUpTerminalNumber = rightUpTerminalNumber;
	}

	public String getLeftDownTerminalNumber() {
		return leftDownTerminalNumber;
	}

	public void setLeftDownTerminalNumber(String leftDownTerminalNumber) {
		this.leftDownTerminalNumber = leftDownTerminalNumber;
	}

	public String getRightDownTerminalNumber() {
		return rightDownTerminalNumber;
	}

	public void setRightDownTerminalNumber(String rightDownTerminalNumber) {
		this.rightDownTerminalNumber = rightDownTerminalNumber;
	}

	public String getCName() {
		return cName;
	}

	public void setCName(String name) {
		cName = name;
	}

	public String getHasPhoto() {
		return hasPhoto;
	}

	public void setHasPhoto(String hasPhoto) {
		this.hasPhoto = hasPhoto;
	}

	public String getForwardurl() {
		return forwardurl;
	}

	public void setForwardurl(String forwardurl) {
		this.forwardurl = forwardurl;
	}

	public String getExportModel() {
		return exportModel;
	}

	public void setExportModel(String exportModel) {
		this.exportModel = exportModel;
	}

	public String getAttrMap() {
		return attrMap;
	}

	public void setAttrMap(String attrMap) {
		this.attrMap = attrMap;
	}

	public String getBizModule() {
		return bizModule;
	}

	public void setBizModule(String bizModule) {
		this.bizModule = bizModule;
	}

	public String getBizProcessCode() {
		return bizProcessCode;
	}

	public void setBizProcessCode(String bizProcessCode) {
		this.bizProcessCode = bizProcessCode;
	}

	public String getBizProcessId() {
		return bizProcessId;
	}

	public void setBizProcessId(String bizProcessId) {
		this.bizProcessId = bizProcessId;
	}

	public String getHasCondition() {
		return hasCondition;
	}

	public void setHasCondition(String hasCondition) {
		this.hasCondition = hasCondition;
	}

	public String getOp_scene() {
		return op_scene;
	}

	public void setOp_scene(String op_scene) {
		this.op_scene = op_scene;
	}

	public String getOp_cause() {
		return op_cause;
	}

	public void setOp_cause(String op_cause) {
		this.op_cause = op_cause;
	}

	public NetworkResourceMaintenanceService getNetworkResourceMaintenanceService() {
		return networkResourceMaintenanceService;
	}

	public void setNetworkResourceMaintenanceService(
			NetworkResourceMaintenanceService networkResourceMaintenanceService) {
		this.networkResourceMaintenanceService = networkResourceMaintenanceService;
	}
	
	
	public Map<String, Object> getTitleMap() {
		return titleMap;
	}

	public void setTitleMap(Map<String, Object> titleMap) {
		this.titleMap = titleMap;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchMsg() {
		return searchMsg;
	}

	public void setSearchMsg(String searchMsg) {
		this.searchMsg = searchMsg;
	}

	public List<Map<String, Object>> getAetgList() {
		return aetgList;
	}

	public void setAetgList(List<Map<String, Object>> aetgList) {
		this.aetgList = aetgList;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getLoadBasicPage() {
		return loadBasicPage;
	}

	public void setLoadBasicPage(String loadBasicPage) {
		this.loadBasicPage = loadBasicPage;
	}

	public String getIsAetg() {
		return isAetg;
	}

	public void setIsAetg(String isAetg) {
		this.isAetg = isAetg;
	}

	public String getSearchConditionText() {
		return searchConditionText;
	}

	public void setSearchConditionText(String searchConditionText) {
		this.searchConditionText = searchConditionText;
	}

	public String getSearchConditionType() {
		return searchConditionType;
	}

	public void setSearchConditionType(String searchConditionType) {
		this.searchConditionType = searchConditionType;
	}

	public String getAssRangeEntityId() {
		return assRangeEntityId;
	}

	public void setAssRangeEntityId(String assRangeEntityId) {
		this.assRangeEntityId = assRangeEntityId;
	}

	public String getAssRangeEntityType() {
		return assRangeEntityType;
	}

	public void setAssRangeEntityType(String assRangeEntityType) {
		this.assRangeEntityType = assRangeEntityType;
	}

	public String getSelectRangeResChosenType() {
		return selectRangeResChosenType;
	}

	public void setSelectRangeResChosenType(String selectRangeResChosenType) {
		this.selectRangeResChosenType = selectRangeResChosenType;
	}

	public String getAetgName() {
		return aetgName;
	}

	public void setAetgName(String aetgName) {
		this.aetgName = aetgName;
	}

	public String getDateString() {
		return dateString;
	}

	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

	public ResourceDictionaryService getResourceDictionaryService() {
		return resourceDictionaryService;
	}

	public void setResourceDictionaryService(
			ResourceDictionaryService resourceDictionaryService) {
		this.resourceDictionaryService = resourceDictionaryService;
	}

	public Map<String, Object> getOdmandterminallayoutMap() {
		return odmandterminallayoutMap;
	}

	public void setOdmandterminallayoutMap(
			Map<String, Object> odmandterminallayoutMap) {
		this.odmandterminallayoutMap = odmandterminallayoutMap;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getAttachmentParentEntityType() {
		return attachmentParentEntityType;
	}

	public void setAttachmentParentEntityType(String attachmentParentEntityType) {
		this.attachmentParentEntityType = attachmentParentEntityType;
	}

	public String getAttachmentParentEntityId() {
		return attachmentParentEntityId;
	}

	public void setAttachmentParentEntityId(String attachmentParentEntityId) {
		this.attachmentParentEntityId = attachmentParentEntityId;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getAttachmentOldName() {
		return attachmentOldName;
	}

	public void setAttachmentOldName(String attachmentOldName) {
		this.attachmentOldName = attachmentOldName;
	}

	public String getCurrentAttachmentEntityType() {
		return currentAttachmentEntityType;
	}

	public void setCurrentAttachmentEntityType(String currentAttachmentEntityType) {
		this.currentAttachmentEntityType = currentAttachmentEntityType;
	}

	public String getCurrentAttachmentEntityId() {
		return currentAttachmentEntityId;
	}

	public void setCurrentAttachmentEntityId(String currentAttachmentEntityId) {
		this.currentAttachmentEntityId = currentAttachmentEntityId;
	}

	public String getCurrentEntityLabel() {
		return currentEntityLabel;
	}

	public void setCurrentEntityLabel(String currentEntityLabel) {
		this.currentEntityLabel = currentEntityLabel;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public int getAssociatedResourcCount() {
		return associatedResourcCount;
	}

	public void setAssociatedResourcCount(int associatedResourcCount) {
		this.associatedResourcCount = associatedResourcCount;
	}

	public Map<String, Object> getChildAssociatedResourcCount() {
		return childAssociatedResourcCount;
	}

	public void setChildAssociatedResourcCount(
			Map<String, Object> childAssociatedResourcCount) {
		this.childAssociatedResourcCount = childAssociatedResourcCount;
	}

	public Map<String, Object> getLinkAssociatedResourcCount() {
		return linkAssociatedResourcCount;
	}

	public void setLinkAssociatedResourcCount(
			Map<String, Object> linkAssociatedResourcCount) {
		this.linkAssociatedResourcCount = linkAssociatedResourcCount;
	}

	public Map<String, List<Map<String, Object>>> getChildAssociatedResourcMap() {
		return childAssociatedResourcMap;
	}

	public void setChildAssociatedResourcMap(
			Map<String, List<Map<String, Object>>> childAssociatedResourcMap) {
		this.childAssociatedResourcMap = childAssociatedResourcMap;
	}

	public Map<String, List<Map<String, Object>>> getLinkAssociatedResourcMap() {
		return linkAssociatedResourcMap;
	}

	public void setLinkAssociatedResourcMap(
			Map<String, List<Map<String, Object>>> linkAssociatedResourcMap) {
		this.linkAssociatedResourcMap = linkAssociatedResourcMap;
	}

	public Map<String, Object> getInfoChildMapChinese() {
		return infoChildMapChinese;
	}

	public void setInfoChildMapChinese(Map<String, Object> infoChildMapChinese) {
		this.infoChildMapChinese = infoChildMapChinese;
	}

	public Map<String, Object> getInfoLinkMapChinese() {
		return infoLinkMapChinese;
	}

	public void setInfoLinkMapChinese(Map<String, Object> infoLinkMapChinese) {
		this.infoLinkMapChinese = infoLinkMapChinese;
	}
	
	public String getUseLinklabel() {
		return useLinklabel;
	}

	public void setUseLinklabel(String useLinklabel) {
		this.useLinklabel = useLinklabel;
	}

	public String getStartOdmNumber() {
		return startOdmNumber;
	}

	public void setStartOdmNumber(String startOdmNumber) {
		this.startOdmNumber = startOdmNumber;
	}

	public String getEndOdmNumber() {
		return endOdmNumber;
	}

	public void setEndOdmNumber(String endOdmNumber) {
		this.endOdmNumber = endOdmNumber;
	}

	public String getStartTerminalNumber() {
		return startTerminalNumber;
	}

	public void setStartTerminalNumber(String startTerminalNumber) {
		this.startTerminalNumber = startTerminalNumber;
	}

	public String getEndTerminalNumber() {
		return endTerminalNumber;
	}

	public void setEndTerminalNumber(String endTerminalNumber) {
		this.endTerminalNumber = endTerminalNumber;
	}

	public String getUseOdmName() {
		return useOdmName;
	}

	public void setUseOdmName(String useOdmName) {
		this.useOdmName = useOdmName;
	}

	public String getLinklabel() {
		return linklabel;
	}

	public void setLinklabel(String linklabel) {
		this.linklabel = linklabel;
	}

	public List<Map<String, Object>> getOdmTerminalList() {
		return odmTerminalList;
	}

	public void setOdmTerminalList(List<Map<String, Object>> odmTerminalList) {
		this.odmTerminalList = odmTerminalList;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	
	public String getHasPhotos() {
		return hasPhotos;
	}

	public void setHasPhotos(String hasPhotos) {
		this.hasPhotos = hasPhotos;
	}

	public List<Map<String, Object>> getPhotoMapList() {
		return photoMapList;
	}

	public void setPhotoMapList(List<Map<String, Object>> photoMapList) {
		this.photoMapList = photoMapList;
	}

	public QuickSort<Map<String, Object>> getQuickSort() {
		return quickSort;
	}

	public void setQuickSort(QuickSort<Map<String, Object>> quickSort) {
		this.quickSort = quickSort;
	}

	public Map<String, Object> getDropdownListMap() {
		return dropdownListMap;
	}

	public void setDropdownListMap(Map<String, Object> dropdownListMap) {
		this.dropdownListMap = dropdownListMap;
	}

	public Map<String, Object> getNullableMap() {
		return nullableMap;
	}

	public void setNullableMap(Map<String, Object> nullableMap) {
		this.nullableMap = nullableMap;
	}

	public Map<String, Object> getAttrTypeMap() {
		return attrTypeMap;
	}

	public void setAttrTypeMap(Map<String, Object> attrTypeMap) {
		this.attrTypeMap = attrTypeMap;
	}

	public Map<String, Object> getAttrLengthMap() {
		return attrLengthMap;
	}

	public void setAttrLengthMap(Map<String, Object> attrLengthMap) {
		this.attrLengthMap = attrLengthMap;
	}

	public String getTargetEntityType() {
		return targetEntityType;
	}

	public void setTargetEntityType(String targetEntityType) {
		this.targetEntityType = targetEntityType;
	}

	public String getParentEntityTypeGrp() {
		return parentEntityTypeGrp;
	}

	public void setParentEntityTypeGrp(String parentEntityTypeGrp) {
		this.parentEntityTypeGrp = parentEntityTypeGrp;
	}

	public InputStream getResultInputStream() {
		return resultInputStream;
	}

	public void setResultInputStream(InputStream resultInputStream) {
		this.resultInputStream = resultInputStream;
	}

	public Map<String, Object> getSheetHeadMap() {
		return sheetHeadMap;
	}

	public void setSheetHeadMap(Map<String, Object> sheetHeadMap) {
		this.sheetHeadMap = sheetHeadMap;
	}

	public String getChaxunFileName() {
		return chaxunFileName;
	}

	public void setChaxunFileName(String chaxunFileName) {
		this.chaxunFileName = chaxunFileName;
	}

	public String getLastPageStatus() {
		return lastPageStatus;
	}

	public void setLastPageStatus(String lastPageStatus) {
		this.lastPageStatus = lastPageStatus;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Map<String, Object> getSearchBigMap() {
		return searchBigMap;
	}

	public void setSearchBigMap(Map<String, Object> searchBigMap) {
		this.searchBigMap = searchBigMap;
	}

	public String getSelectResChosenType() {
		return selectResChosenType;
	}

	public void setSelectResChosenType(String selectResChosenType) {
		this.selectResChosenType = selectResChosenType;
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

	public List<Map<String, Object>> getSearchResourceMapList() {
		return searchResourceMapList;
	}

	public void setSearchResourceMapList(
			List<Map<String, Object>> searchResourceMapList) {
		this.searchResourceMapList = searchResourceMapList;
	}

	public Map<String, Object> getSearchMap() {
		return searchMap;
	}

	public void setSearchMap(Map<String, Object> searchMap) {
		this.searchMap = searchMap;
	}

	public String getSelectResType() {
		return selectResType;
	}

	public void setSelectResType(String selectResType) {
		this.selectResType = selectResType;
	}

	public List<String> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<String> attrList) {
		this.attrList = attrList;
	}

	public List<Map<String, Object>> getAssTypeMapList() {
		return assTypeMapList;
	}

	public void setAssTypeMapList(List<Map<String, Object>> assTypeMapList) {
		this.assTypeMapList = assTypeMapList;
	}

	public String getChosenType() {
		return chosenType;
	}

	public void setChosenType(String chosenType) {
		this.chosenType = chosenType;
	}

	public List<Map<String, Object>> getAllTypeMapList() {
		return allTypeMapList;
	}

	public void setAllTypeMapList(List<Map<String, Object>> allTypeMapList) {
		this.allTypeMapList = allTypeMapList;
	}

	public String getLoadBigPage() {
		return loadBigPage;
	}

	public void setLoadBigPage(String loadBigPage) {
		this.loadBigPage = loadBigPage;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public Map<String, Object> getCurrentEntityChineseMap() {
		return currentEntityChineseMap;
	}

	public void setCurrentEntityChineseMap(
			Map<String, Object> currentEntityChineseMap) {
		this.currentEntityChineseMap = currentEntityChineseMap;
	}

	public String getLoadPartPage() {
		return loadPartPage;
	}

	public void setLoadPartPage(String loadPartPage) {
		this.loadPartPage = loadPartPage;
	}

	public String getParentEntityName() {
		return parentEntityName;
	}

	public void setParentEntityName(String parentEntityName) {
		this.parentEntityName = parentEntityName;
	}

	public List<Map<String, Object>> getCanUseFiberCoreList() {
		return canUseFiberCoreList;
	}

	public void setCanUseFiberCoreList(List<Map<String, Object>> canUseFiberCoreList) {
		this.canUseFiberCoreList = canUseFiberCoreList;
	}

	public String getEntityTypeGroup() {
		return entityTypeGroup;
	}

	public void setEntityTypeGroup(String entityTypeGroup) {
		this.entityTypeGroup = entityTypeGroup;
	}

	public List<Map<String, Object>> getResultList() {
		return resultList;
	}

	public void setResultList(List<Map<String, Object>> resultList) {
		this.resultList = resultList;
	}

	public List<Map<String, Object>> getNoUseTerminalList() {
		return noUseTerminalList;
	}

	public void setNoUseTerminalList(List<Map<String, Object>> noUseTerminalList) {
		this.noUseTerminalList = noUseTerminalList;
	}

	public List<Map<String, Object>> getUseOneTimeTerminalList() {
		return useOneTimeTerminalList;
	}

	public void setUseOneTimeTerminalList(
			List<Map<String, Object>> useOneTimeTerminalList) {
		this.useOneTimeTerminalList = useOneTimeTerminalList;
	}

	public List<Map<String, Object>> getNoUseFiberCoreList() {
		return noUseFiberCoreList;
	}

	public void setNoUseFiberCoreList(List<Map<String, Object>> noUseFiberCoreList) {
		this.noUseFiberCoreList = noUseFiberCoreList;
	}

	public String getParentEntityId() {
		return parentEntityId;
	}

	public void setParentEntityId(String parentEntityId) {
		this.parentEntityId = parentEntityId;
	}

	public String getParentEntityType() {
		return parentEntityType;
	}

	public void setParentEntityType(String parentEntityType) {
		this.parentEntityType = parentEntityType;
	}

	public String getCurrentPhotoEntityType() {
		return currentPhotoEntityType;
	}

	public void setCurrentPhotoEntityType(String currentPhotoEntityType) {
		this.currentPhotoEntityType = currentPhotoEntityType;
	}

	public String getCurrentPhotoEntityId() {
		return currentPhotoEntityId;
	}

	public void setCurrentPhotoEntityId(String currentPhotoEntityId) {
		this.currentPhotoEntityId = currentPhotoEntityId;
	}

	public String getPhotoOldName() {
		return photoOldName;
	}

	public void setPhotoOldName(String photoOldName) {
		this.photoOldName = photoOldName;
	}

	public XMLAEMLibrary getModuleLibrary() {
		return moduleLibrary;
	}

	public void setModuleLibrary(XMLAEMLibrary moduleLibrary) {
		this.moduleLibrary = moduleLibrary;
	}

	public String getAddedResParentEntityType() {
		return addedResParentEntityType;
	}

	public void setAddedResParentEntityType(String addedResParentEntityType) {
		this.addedResParentEntityType = addedResParentEntityType;
	}

	public String getAddedResParentEntityId() {
		return addedResParentEntityId;
	}

	public void setAddedResParentEntityId(String addedResParentEntityId) {
		this.addedResParentEntityId = addedResParentEntityId;
	}

	public String getAddedResEntityType() {
		return addedResEntityType;
	}

	public void setAddedResEntityType(String addedResEntityType) {
		this.addedResEntityType = addedResEntityType;
	}

	public Map<String, Object> getAddedResMap() {
		return addedResMap;
	}

	public void setAddedResMap(Map<String, Object> addedResMap) {
		this.addedResMap = addedResMap;
	}

	public String getOperatedCurrentEntityId() {
		return operatedCurrentEntityId;
	}

	public void setOperatedCurrentEntityId(String operatedCurrentEntityId) {
		this.operatedCurrentEntityId = operatedCurrentEntityId;
	}

	public String getOperatedCurrentEntityType() {
		return operatedCurrentEntityType;
	}

	public void setOperatedCurrentEntityType(String operatedCurrentEntityType) {
		this.operatedCurrentEntityType = operatedCurrentEntityType;
	}

	public String getLoadedType() {
		return loadedType;
	}

	public void setLoadedType(String loadedType) {
		this.loadedType = loadedType;
	}

	public String getOldParentResEntityId() {
		return oldParentResEntityId;
	}

	public void setOldParentResEntityId(String oldParentResEntityId) {
		this.oldParentResEntityId = oldParentResEntityId;
	}

	public String getOldParentResEntityType() {
		return oldParentResEntityType;
	}

	public void setOldParentResEntityType(String oldParentResEntityType) {
		this.oldParentResEntityType = oldParentResEntityType;
	}

	public String getNewParentResEntityId() {
		return newParentResEntityId;
	}

	public void setNewParentResEntityId(String newParentResEntityId) {
		this.newParentResEntityId = newParentResEntityId;
	}

	public String getNewParentResEntityType() {
		return newParentResEntityType;
	}

	public void setNewParentResEntityType(String newParentResEntityType) {
		this.newParentResEntityType = newParentResEntityType;
	}

	public String getUpdatedEntityType() {
		return updatedEntityType;
	}

	public void setUpdatedEntityType(String updatedEntityType) {
		this.updatedEntityType = updatedEntityType;
	}

	public String getChooseResEntityType() {
		return chooseResEntityType;
	}

	public void setChooseResEntityType(String chooseResEntityType) {
		this.chooseResEntityType = chooseResEntityType;
	}

	public String getChooseResEntityId() {
		return chooseResEntityId;
	}

	public void setChooseResEntityId(String chooseResEntityId) {
		this.chooseResEntityId = chooseResEntityId;
	}

	public String getNeedChildEntityType() {
		return needChildEntityType;
	}

	public void setNeedChildEntityType(String needChildEntityType) {
		this.needChildEntityType = needChildEntityType;
	}

	public Map<String, Object> getParentEntityMap() {
		return parentEntityMap;
	}

	public void setParentEntityMap(Map<String, Object> parentEntityMap) {
		this.parentEntityMap = parentEntityMap;
	}

	public Map<String, Object> getGrandpaEntityMap() {
		return grandpaEntityMap;
	}

	public void setGrandpaEntityMap(Map<String, Object> grandpaEntityMap) {
		this.grandpaEntityMap = grandpaEntityMap;
	}

	public StructureCommonService getStructureCommonService() {
		return structureCommonService;
	}

	public void setStructureCommonService(
			StructureCommonService structureCommonService) {
		this.structureCommonService = structureCommonService;
	}

	public PhysicalresService getPhysicalresService() {
		return physicalresService;
	}

	public void setPhysicalresService(PhysicalresService physicalresService) {
		this.physicalresService = physicalresService;
	}

	public String getCurrentEntityType() {
		return currentEntityType;
	}

	public void setCurrentEntityType(String currentEntityType) {
		this.currentEntityType = currentEntityType;
	}

	public String getCurrentEntityId() {
		return currentEntityId;
	}

	public void setCurrentEntityId(String currentEntityId) {
		this.currentEntityId = currentEntityId;
	}

	public Map<String, Object> getCurrentEntityMap() {
		return currentEntityMap;
	}

	public void setCurrentEntityMap(Map<String, Object> currentEntityMap) {
		this.currentEntityMap = currentEntityMap;
	}
	

	/**
	 * 查询一个节点的信息(右边菜单信息栏)
	 * @return
	 */
	public String getPhysicalresAction() {
		log.info("进入getPhysicalresAction(),查询一个节点的信息(右边菜单信息栏)");
		try {
			if(areaId!=null&&!"".equals(areaId)){
				BasicEntity areaEntity =physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
				if(areaEntity!=null){
					this.areaName = areaEntity.getValue("name");
				}
			}
			BasicEntity currentEntity = null;
			long currentEntityId = Long.parseLong(this.currentEntityId);
			currentEntity = physicalresService.getPhysicalresById(this.currentEntityType, currentEntityId);
		
			ApplicationEntity currentAppEntity = ApplicationEntity.changeFromEntity(currentEntity);
			Map<String,Object> orderIdMap = new HashMap<String,Object>();
			if(currentAppEntity != null) {
				this.currentEntityMap = ResourceCommon.applicationEntityConvertMap(currentAppEntity);
				this.currentEntityChineseMap = new HashMap<String, Object>();
				for(String key : this.currentEntityMap.keySet()) {
					try {
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							//数据字典，中英文转换
							entry = dictionary.getEntry(key + "," + currentEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								this.currentEntityChineseMap.put(key, entry.get(0).getValue("display"));
								orderIdMap.put(entry.get(0).getValue("orderID").toString(),key);
							}
						}
					} catch (EntryOperationException e) {
						log.error("获取"+currentEntityType+"属性字段"+key+"的中文字典失败，可能该字典不存在。");
						e.printStackTrace();
					}
					
					//若类型为时间类型，进行格式化
					if(this.currentEntityMap.get(key) != null 
							&& this.currentEntityMap.get(key).getClass().getName().indexOf("Date") > -1) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
						this.currentEntityMap.put(key, sdf.format(this.currentEntityMap.get(key)));
					}
				}
			}
			//数据处理
			ResourceCommon.mapRecombinationToString(this.currentEntityMap);
			
			
			//属性排序
			Map<String,Object> sortedMap = quickSort.sortMap(currentEntityMap,orderIdMap);
			if(sortedMap!=null){
				this.currentEntityMap=sortedMap;
			}
			if("Photo".equals(this.currentEntityType)) {
				ApplicationEntity[] apps=structureCommonService.getStrutureSelationsApplicationEntity(currentAppEntity,  AssociatedType.PARENT, "networkresourcemanage");
				if(apps!=null && apps.length!=0){
					for(ApplicationEntity app:apps){
						List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
						list=structureCommonService.getStrutureSelationsApplicationMap(app,"Photo", AssociatedType.CHILD, "networkresourcemanage");
						//System.out.println(list+"****************");
						photoMapList.addAll(list);				
					}
					quickSort.sort(photoMapList, "name");
				}
				return "photoPage";
			}else{
				//资源是否有图片子资源处理
				hasPhotos="false";
				String[] appNames=structureCommonService.getStrutureSelationsArray(currentAppEntity, AssociatedType.CHILD,  "networkresourcemanage");
				if(appNames!=null&&appNames.length!=0){
					for(String s:appNames){
						if(s.equals("Photo")){
							hasPhotos ="true";
						}
					}
				}
				//System.out.println(hasPhotos+"********************");
				if(hasPhotos.equals("true")){
					photoMapList=structureCommonService.getStrutureSelationsApplicationMap(currentAppEntity,"Photo", AssociatedType.CHILD, "networkresourcemanage");
					quickSort.sort(photoMapList, "name");
				}
			}
			if("loadBasicPage".equals(this.loadBasicPage)){
				log.info("退出getPhysicalresAction(),返回结果result=loadBasicPage");
				return "loadBasicPage";
			}
			log.info("退出getPhysicalresAction(),返回结果result=success");
			return "success";
		} catch (NumberFormatException e) {
			log.info("进入getPhysicalresAction(),发生数据NumberFormatException异常，返回result=error");
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 获取一个物理资源类型的父类型
	 */
	public void getPhysicalresParentTypeAction() {
		log.info("进入getPhysicalresParentTypeAction(),获取一个物理资源类型的父类型");
		//获取当前资源类型关联的父类型，用来控制隶属资源树单选按钮的生成
		String[] parentTypeArrs = structureCommonService.getAssociatedAetName(this.targetEntityType, AssociatedType.PARENT, "networkresourcemanage");
		String typeGrp = "";
		if(parentTypeArrs != null && parentTypeArrs.length > 0) {
			for (String type : parentTypeArrs) {
				typeGrp += type + "_";
			}
			typeGrp = typeGrp.substring(0, typeGrp.length() - 1);
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson(typeGrp);
		try {
			log.info("退出getPhysicalresParentTypeAction(),返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getPhysicalresParentTypeAction(),返回结果result="+result+"失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询一个节点(用于对节点进行编辑操作)
	 * @return
	 */
	public String getPhysicalresForOperaAction() {
		log.info("进入getPhysicalresForOperaAction()，查询一个节点(用于对节点进行编辑操作)");
		if(bizModule != null && !bizModule.equals("")){
			try {
				this.bizModule = new String(this.bizModule.getBytes("iso-8859-1"),"UTF-8") ;
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		//集中调度跳转url
		
		List<BasicEntity> entry1=null;
		this.cName = this.currentEntityType;
		try {
			entry1 = dictionary.getEntry(this.currentEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
		} catch (EntryOperationException e) {
			log.error("获取"+currentEntityType+"的中文字典失败，可能该字典不存在。");
			e.printStackTrace();
		}
		if(entry1 != null && !entry1.isEmpty()) {
			this.cName = entry1.get(0).getValue("display");
		}
		
		this.forwardurl=InterfaceUtil.getConfigValueFromProperties("App.domain");
		try {
			if(areaId!=null&&!"".equals(areaId)){
				BasicEntity areaEntity =physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
				if(areaEntity!=null){
					this.areaName = areaEntity.getValue("name");
				}
			}
			
			ApplicationModule module = ModuleProvider.getModule(this.currentEntityType);
			
			//long nextLong = Unique.nextLong("fdfd_id");
			BasicEntity currentEntity =null;
			if(this.currentEntityId!=null&&!"".equals(this.currentEntityId)){
				long currentEntityId = Long.parseLong(this.currentEntityId);
			    currentEntity = physicalresService.getPhysicalresById(this.currentEntityType, currentEntityId);
			}else{
				 currentEntity = physicalresService.getPhysicalresByLabel(this.currentEntityType,this.currentEntityLabel);
			}
			
			ApplicationEntity currentAppEntity = ApplicationEntity.changeFromEntity(currentEntity);
			this.currentEntityId=currentAppEntity.getValue("id").toString();
			Map<String,Object> orderIdMap = new HashMap<String,Object>();//排序map
			if(currentAppEntity != null) {
				//转换map，包含空属性
				this.currentEntityMap = ResourceCommon.applicationEntityConvertMap(currentAppEntity);
				
				//中英文转换map
				this.currentEntityChineseMap = new HashMap<String, Object>();
				//非空属性判断map
				this.nullableMap = new HashMap<String, Object>();
				//属性类型判断map
				this.attrTypeMap = new HashMap<String, Object>();
				//生成下拉框的map
				this.dropdownListMap = new HashMap<String, Object>();
				//属性长度判断map
				this.attrLengthMap =new HashMap<String,Object>();
				if("Sys_Area".equals(this.currentEntityType)){//区域特殊处理
					this.currentEntityMap.remove("path");
					this.currentEntityMap.remove("parent_id");
					this.currentEntityMap.put("id", this.currentEntityMap.get("area_id"));
					this.currentEntityMap.remove("area_id");
				}
				for(String key : this.currentEntityMap.keySet()) {
					try {
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							//数据字典，中英文转换
							entry = dictionary.getEntry(key + "," + this.currentEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								this.currentEntityChineseMap.put(key, entry.get(0).getValue("display"));
								orderIdMap.put(entry.get(0).getValue("orderID").toString(), key);
							}
						}
						
						if(!"_entityId".equals(key)&&!"_entityType".equals(key)) {
							
							
							//System.out.println(entry.get(0).getValue("dictionaryType"));
							
							//根据数据字典中，属性为entity类型或者general类型，获取属性对应的下拉框的值，提供前台进行下拉框选择操作
							//拿出来的值，例如:其他-其他,置换-置换,局用-局用
							if("entity".equals(entry.get(0).getValue("dictionaryType"))) {
								//entity类型下拉框数据组装
								List<BasicEntity> dropDownEntry = dictionary.getEntry(key + "," + this.currentEntityType + ",networkResourceValueEmun", SearchScope.OBJECT, "");
								if(dropDownEntry != null && !dropDownEntry.isEmpty()) {
									String dropdownMixVal = dropDownEntry.get(0).getValue("mixValue");
									//先用逗号进行分隔
									String[] firstSplitArrs = dropdownMixVal.split(",");
									if(firstSplitArrs != null && firstSplitArrs.length > 0) {
										List<String> attrDropdownList = new ArrayList<String>();
										for(String secondVal : firstSplitArrs) {
											//再通过"-"获取key，value，获取value值
											String[] secondSplitArrs = secondVal.split(":");
											if(secondSplitArrs != null && secondSplitArrs.length == 2) {
												attrDropdownList.add(secondSplitArrs[1]);
												
											}
										}
										this.dropdownListMap.put(key, attrDropdownList);
									}
								}
								
							} else if("general".equals(entry.get(0).getValue("dictionaryType"))) {
								String generalEntryName = entry.get(0).getValue("generalEntryName");
								//general类型下拉框数据组装
								List<BasicEntity> dropDownEntry = dictionary.getEntry(generalEntryName + ",networkResourceValueEmun", SearchScope.OBJECT, "");
								if(dropDownEntry != null && !dropDownEntry.isEmpty()) {
									String dropdownMixVal = dropDownEntry.get(0).getValue("mixValue");
									//先用逗号进行分隔
									String[] firstSplitArrs = dropdownMixVal.split(",");
									if(firstSplitArrs != null && firstSplitArrs.length > 0) {
										List<String> attrDropdownList = new ArrayList<String>();
										for(String secondVal : firstSplitArrs) {
											//再通过"-"获取key，value，获取value值
											String[] secondSplitArrs = secondVal.split(":");
											if(secondSplitArrs != null && secondSplitArrs.length == 2) {
												attrDropdownList.add(secondSplitArrs[1]);
												
											}
										}
										this.dropdownListMap.put(key, attrDropdownList);
									}
								}
							}
						}
					} catch (EntryOperationException e) {
						log.error("获取"+currentEntityType+"属性字段"+key+"的中文字典失败，可能该字典不存在。");
						e.printStackTrace();
					}
					
					if(!"_entityId".equals(key)&&!"_entityType".equals(key)) {
						//判断是否需要增加非空判断
						if(module.getAttribute(key) != null){
							Object nullVal = module.getAttribute(key).getValue("nullable");
							if(nullVal != null && !"".equals(nullVal)) {
								this.nullableMap.put(key, nullVal.toString());
							}
							//用map保存属性的类型,例如java.lang.String
							Object attrTypeVal = module.getAttribute(key).getValue("type");
							if(attrTypeVal != null && !"".equals(attrTypeVal)) {
								this.attrTypeMap.put(key, attrTypeVal.toString());
								
								if(attrTypeVal.toString().equals("java.lang.String")){
									//用map保存String类型属性的长度
									Object attrLengthVal = module.getAttribute(key).getValue("length");
									if(attrLengthVal!=null && !"".equals(attrLengthVal)){
										this.attrLengthMap.put(key,Integer.valueOf(attrLengthVal.toString()));
									}
								}
							}
						}
						
					}
					
					//若类型为时间类型，进行格式化
					if(this.currentEntityMap.get(key) != null 
							&& this.currentEntityMap.get(key).getClass().getName().indexOf("Date") > -1) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
						this.currentEntityMap.put(key, sdf.format(this.currentEntityMap.get(key)));
					}
				}
			}
			
			//获取当前资源类型关联的父类型，用来控制隶属资源树单选按钮的生成
			String[] parentTypeArrs = structureCommonService.getAssociatedAetName(this.currentEntityType, AssociatedType.PARENT, "networkresourcemanage");
			if(parentTypeArrs != null && parentTypeArrs.length > 0) {
				this.parentEntityTypeGrp = "";
				for (String type : parentTypeArrs) {
					this.parentEntityTypeGrp += type + "_";
				}
				this.parentEntityTypeGrp = this.parentEntityTypeGrp.substring(0, this.parentEntityTypeGrp.length() - 1);
			}
			
			//数据处理(空值处理)
			ResourceCommon.mapRecombinationToString(this.currentEntityMap);
			//ApplicationEntity[] parentAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(currentAppEntity, AssociatedType.PARENT, "networkresourcemanage");
			
			//if(parentAppEntityArrs != null && parentAppEntityArrs.length > 0) {
				//获取父级entity
				//ApplicationEntity parentAppEntity = parentAppEntityArrs[0];
			ApplicationEntity parentAppEntity = null;
			if(this.parentEntityType != null && this.parentEntityId != null 
					&& !"undefined".equals(this.parentEntityType) && !"undefined".equals(this.parentEntityId) 
					&& !"null".equals(this.parentEntityType) && !"null".equals(this.parentEntityId) 
					&& !"".equals(this.parentEntityType) && !"".equals(this.parentEntityId) ) {
				//能够获取父级类型和id的情况，直接获取父级entity
				parentAppEntity = ApplicationEntity.changeFromEntity(
						physicalresService.getPhysicalresById(this.parentEntityType, Long.parseLong(this.parentEntityId)));
			} else {
				//未能获取父级类型和id的情况，通过查询获取
				ApplicationEntity[] parentAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(currentAppEntity, AssociatedType.PARENT, "networkresourcemanage");
				if(parentAppArrs != null && parentAppArrs.length > 0) {
					//获取父级entity
					parentAppEntity = parentAppArrs[0];
				}
			}
			
			String parentType = "";
			if(parentAppEntity != null && parentAppEntity.getType() != null) {
				parentType = parentAppEntity.getType();
				
				//求出父entity后，获取其id和类型，供属性编辑页面，隶属资源树控制使用
				this.parentEntityId = parentAppEntity.getValue("id").toString();
				this.parentEntityType = parentType;
				
				this.parentEntityMap = parentAppEntity.toMap();
				this.parentEntityMap.put("type", parentType);
				
				ApplicationEntity[] grandpaAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity, AssociatedType.PARENT, "networkresourcemanage");
				if(grandpaAppEntityArrs != null && grandpaAppEntityArrs.length > 0) {
					//获取爷级节点
					ApplicationEntity grandpaAppEntity = grandpaAppEntityArrs[0];
					String grandpaType = "";
					if(grandpaAppEntity.getType() != null) {
						grandpaType = grandpaAppEntity.getType();
					}
					this.grandpaEntityMap = grandpaAppEntity.toMap();
					this.grandpaEntityMap.put("type", grandpaType);
				}
				
			}
			
			if("Station".equals(this.currentEntityType)||"Room".equals(this.currentEntityType)||"Sys_Area".equals(this.currentEntityType)){
				this.aetgList = new ArrayList<Map<String,Object>>();
				if("Station".equals(this.currentEntityType)){
					if("showTask".equals(this.showType)){
						Map<String,Object> aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","noload");
						aetgMap.put("chineseName","基本信息");
						this.aetgList.add(aetgMap);
						
						aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","FlatNavigation_4_Station_4_Wireless");
						aetgMap.put("chineseName","无线");
						List<Map<String,Object>> childAetgList = new ArrayList<Map<String,Object>>();
						Map<String,Object> childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Wireless_GSM");
						childAetgMap.put("chineseName","GSM设备");
						childAetgList.add(childAetgMap);
						childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Wireless_TD");
						childAetgMap.put("chineseName","TD设备");
						childAetgList.add(childAetgMap);
						childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Wireless_AntennaSys");
						childAetgMap.put("chineseName","天馈系统");
						childAetgList.add(childAetgMap);
						aetgMap.put("childAetgList", childAetgList);
						this.aetgList.add(aetgMap);
						
						aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","FlatNavigation_4_Station_4_IndoorCover");
						aetgMap.put("chineseName","室分");
						this.aetgList.add(aetgMap);
						
						aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","FlatNavigation_4_Station_4_WLAN");
						aetgMap.put("chineseName","WLAN");
						this.aetgList.add(aetgMap);
						
						
						aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","FlatNavigation_4_Station_4_Transmission");
						aetgMap.put("chineseName","传输");
						this.aetgList.add(aetgMap);
						
						aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","FlatNavigation_4_Station_4_Power");
						aetgMap.put("chineseName","动力");
						childAetgList = new ArrayList<Map<String,Object>>();
						childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Power_ACSys");
						childAetgMap.put("chineseName","AC系统");
						childAetgList.add(childAetgMap);
						childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Power_DCSys");
						childAetgMap.put("chineseName","DC系统");
						childAetgList.add(childAetgMap);
						childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Power_UPSSys");
						childAetgMap.put("chineseName","UPS系统");
						childAetgList.add(childAetgMap);
						childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Power_EarthConnEtc");
						childAetgMap.put("chineseName","防雷接地与引电");
						childAetgList.add(childAetgMap);
						aetgMap.put("childAetgList", childAetgList);
						this.aetgList.add(aetgMap);
						
						aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","FlatNavigation_4_Station_4_EvironmentAndMonitoring");
						aetgMap.put("chineseName","环境监控");
						this.aetgList.add(aetgMap);	
					}else{
						
						Map<String,Object> aetgMap = new HashMap<String,Object>();
						
						aetgMap.put("name","FlatNavigation_4_Station_4_Transmission");
						aetgMap.put("chineseName","传输");
						this.aetgList.add(aetgMap);
						
						aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","FlatNavigation_4_Station_4_Power");
						aetgMap.put("chineseName","动力");
						List<Map<String,Object>> childAetgList = new ArrayList<Map<String,Object>>();
						Map<String,Object> childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Power_ACSys");
						childAetgMap.put("chineseName","AC系统");
						childAetgList.add(childAetgMap);
						childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Power_DCSys");
						childAetgMap.put("chineseName","DC系统");
						childAetgList.add(childAetgMap);
						childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Power_UPSSys");
						childAetgMap.put("chineseName","UPS系统");
						childAetgList.add(childAetgMap);
						childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Power_EarthConnEtc");
						childAetgMap.put("chineseName","防雷接地与引电");
						childAetgList.add(childAetgMap);
						aetgMap.put("childAetgList", childAetgList);
						this.aetgList.add(aetgMap);
						
						aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","FlatNavigation_4_Station_4_Wireless");
						aetgMap.put("chineseName","无线");
						childAetgList = new ArrayList<Map<String,Object>>();
					    childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Wireless_GSM");
						childAetgMap.put("chineseName","GSM设备");
						childAetgList.add(childAetgMap);
						childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Wireless_TD");
						childAetgMap.put("chineseName","TD设备");
						childAetgList.add(childAetgMap);
						childAetgMap = new HashMap<String,Object>();
						childAetgMap.put("name","FlatNavigation_4_Station_4_Wireless_AntennaSys");
						childAetgMap.put("chineseName","天馈系统");
						childAetgList.add(childAetgMap);
						aetgMap.put("childAetgList", childAetgList);
						this.aetgList.add(aetgMap);
						
						aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","FlatNavigation_4_Station_4_IndoorCover");
						aetgMap.put("chineseName","室分");
						this.aetgList.add(aetgMap);
						
						aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","FlatNavigation_4_Station_4_WLAN");
						aetgMap.put("chineseName","WLAN");
						this.aetgList.add(aetgMap);
						
						aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","FlatNavigation_4_Station_4_EvironmentAndMonitoring");
						aetgMap.put("chineseName","环境监控");
						this.aetgList.add(aetgMap);	
						
					}
					
					
				}else if("Room".equals(this.currentEntityType)){
					if("showTask".equals(this.showType)){
						Map<String,Object> aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","noload");
						aetgMap.put("chineseName","基本信息");
						this.aetgList.add(aetgMap);
					}
					Map<String,Object> aetgMap = new HashMap<String,Object>();
					
					aetgMap.put("name","FlatNavigation_4_Room_4_Transmission");
					aetgMap.put("chineseName","传输");
					this.aetgList.add(aetgMap);
					
					aetgMap = new HashMap<String,Object>();
					aetgMap.put("name","FlatNavigation_4_Room_4_Power");
					aetgMap.put("chineseName","动力");
					List<Map<String,Object>> childAetgList = new ArrayList<Map<String,Object>>();
					Map<String,Object> childAetgMap = new HashMap<String,Object>();
					childAetgMap.put("name","FlatNavigation_4_Room_4_Power_ACSys");
					childAetgMap.put("chineseName","AC系统");
					childAetgList.add(childAetgMap);
					childAetgMap = new HashMap<String,Object>();
					childAetgMap.put("name","FlatNavigation_4_Room_4_Power_DCSys");
					childAetgMap.put("chineseName","DC系统");
					childAetgList.add(childAetgMap);
					childAetgMap = new HashMap<String,Object>();
					childAetgMap.put("name","FlatNavigation_4_Room_4_Power_UPSSys");
					childAetgMap.put("chineseName","UPS系统");
					childAetgList.add(childAetgMap);
					childAetgMap = new HashMap<String,Object>();
					childAetgMap.put("name","FlatNavigation_4_Room_4_Power_EarthConnEtc");
					childAetgMap.put("chineseName","防雷接地与引电");
					childAetgList.add(childAetgMap);
					aetgMap.put("childAetgList", childAetgList);
					this.aetgList.add(aetgMap);
					
					aetgMap = new HashMap<String,Object>();
					aetgMap.put("name","FlatNavigation_4_Room_4_Wireless");
					aetgMap.put("chineseName","无线");
					childAetgList = new ArrayList<Map<String,Object>>();
				    childAetgMap = new HashMap<String,Object>();
					childAetgMap.put("name","FlatNavigation_4_Room_4_Wireless_GSM");
					childAetgMap.put("chineseName","GSM设备");
					childAetgList.add(childAetgMap);
					childAetgMap = new HashMap<String,Object>();
					childAetgMap.put("name","FlatNavigation_4_Room_4_Wireless_TD");
					childAetgMap.put("chineseName","TD设备");
					childAetgList.add(childAetgMap);
					childAetgMap = new HashMap<String,Object>();
					childAetgMap.put("name","FlatNavigation_4_Room_4_Wireless_AntennaSys");
					childAetgMap.put("chineseName","天馈系统");
					childAetgList.add(childAetgMap);
					aetgMap.put("childAetgList", childAetgList);
					this.aetgList.add(aetgMap);
					
					aetgMap = new HashMap<String,Object>();
					aetgMap.put("name","FlatNavigation_4_Room_4_IndoorCover");
					aetgMap.put("chineseName","室分");
					this.aetgList.add(aetgMap);
					
					aetgMap = new HashMap<String,Object>();
					aetgMap.put("name","FlatNavigation_4_Room_4_WLAN");
					aetgMap.put("chineseName","WLAN");
					this.aetgList.add(aetgMap);
					
					aetgMap = new HashMap<String,Object>();
					aetgMap.put("name","FlatNavigation_4_Room_4_EvironmentAndMonitoring");
					aetgMap.put("chineseName","环境监控");
					this.aetgList.add(aetgMap);
					
				}else{
					if("showTask".equals(this.showType)){
						Map<String,Object> aetgMap = new HashMap<String,Object>();
						aetgMap.put("name","noload");
						aetgMap.put("chineseName","基本信息");
						this.aetgList.add(aetgMap);
					}
					Map<String,Object> aetgMap = new HashMap<String,Object>();
					aetgMap.put("name","FlatNavigation_4_Area_4_Geographical");
					aetgMap.put("chineseName","地理位置类");
					this.aetgList.add(aetgMap);
					
					aetgMap = new HashMap<String,Object>();
					aetgMap.put("name","FlatNavigation_4_Area_4_Basestation");
					aetgMap.put("chineseName","基站");
					this.aetgList.add(aetgMap);
					
					aetgMap = new HashMap<String,Object>();
					aetgMap.put("name","FlatNavigation_4_Area_4_OptDisFac");
					aetgMap.put("chineseName","光交接设施");
					this.aetgList.add(aetgMap);
					
					aetgMap = new HashMap<String,Object>();
					aetgMap.put("name","FlatNavigation_4_Area_4_CommPriDev");
					aetgMap.put("chineseName","通信主设备");
					this.aetgList.add(aetgMap);
					
					aetgMap = new HashMap<String,Object>();
					aetgMap.put("name","FlatNavigation_4_Area_4_KeyOfPower");
					aetgMap.put("chineseName","动力(核心)");
					this.aetgList.add(aetgMap);
					
					aetgMap = new HashMap<String,Object>();
					aetgMap.put("name","FlatNavigation_4_Area_4_KeyOfTransmission");
					aetgMap.put("chineseName","传输(核心)");
					this.aetgList.add(aetgMap);
					
					aetgMap = new HashMap<String,Object>();
					aetgMap.put("name","FlatNavigation_4_Area_4_KeyOfEvirMonit");
					aetgMap.put("chineseName","环境监控(核心)");
					this.aetgList.add(aetgMap);
					
					aetgMap = new HashMap<String,Object>();
					aetgMap.put("name","FlatNavigation_4_Area_4_LogicNet");
					aetgMap.put("chineseName","管线逻辑网");
					List<Map<String,Object>> childAetgList = new ArrayList<Map<String,Object>>();
					Map<String,Object> childAetgMap = new HashMap<String,Object>();
					childAetgMap.put("name","FlatNavigation_4_Area_4_LogicNet_Pl");
					childAetgMap.put("chineseName","管线网");
					childAetgList.add(childAetgMap);
					childAetgMap = new HashMap<String,Object>();
					childAetgMap.put("name","FlatNavigation_4_Area_4_LogicNet_Fb");
					childAetgMap.put("chineseName","光缆网");
					childAetgList.add(childAetgMap);
					childAetgMap = new HashMap<String,Object>();
					childAetgMap.put("name","FlatNavigation_4_Area_4_LogicNet_Op");
					childAetgMap.put("chineseName","光路网");
					childAetgList.add(childAetgMap);
					childAetgMap = new HashMap<String,Object>();
					childAetgMap.put("name","FlatNavigation_4_Area_4_LogicNet_Transm");
					childAetgMap.put("chineseName","传输网");
					childAetgList.add(childAetgMap);
					aetgMap.put("childAetgList", childAetgList);
					this.aetgList.add(aetgMap);
				}
			}
				
			//}
			//entity 属性排序
			Map<String,Object> sortedMap = quickSort.sortMap(this.currentEntityMap, orderIdMap);
			if(sortedMap!=null){
				this.currentEntityMap = sortedMap;
			}
			
			//获取图片信息
		//	if("showTask".equals(this.showType)){
				if(!"Photo".equals(this.currentEntityType)){
					String[] aets=this.structureCommonService.getAssociatedAetName(this.currentEntityType,AssociatedType.CHILD, "networkresourcemanage");
					if(aets!=null&&aets.length>0){
						boolean hasPhotoType= false;
						for(String aet:aets){
							if("Photo".equals(aet)){
								hasPhotoType= true;
							}
						}
						if(hasPhotoType){
							this.photoMapList = this.structureCommonService.getStrutureSelationsApplicationMap(currentAppEntity, "Photo", AssociatedType.CHILD,  "networkresourcemanage");
							if(this.photoMapList==null || this.photoMapList.size()<=0){
								this.photoMapList = null;
							}
						}else{
							this.hasPhoto="no";
						}
					}else{
						this.hasPhoto="no";
					}
				}
		//	}
			
			if(this.loadBigPage != null) {
				log.info("退出getPhysicalresForOperaAction()，返回结果result=loadBigPage");
				return "loadBigPage"; //AJAX加载大部分页面
			}else if("showTask".equals(this.showType)){
				log.info("退出getPhysicalresForOperaAction()，返回结果result=showTaskPage");
				return "showTaskPage";
			}else{
				log.info("退出getPhysicalresForOperaAction()，返回结果result=success");
				return "success";
			}
		} catch (NumberFormatException e) {
			log.info("退出getPhysicalresForOperaAction()，发生NumberFormatException异常，返回结果result=error");
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 加载添加物理资源页面
	 * @return
	 */
	public String loadAddPhysicalresPageAction() {
		log.info("进入loadAddPhysicalresPageAction，加载添加物理资源页面");
		try {
			if(areaId!=null&&!"".equals(areaId)){
				BasicEntity areaEntity =physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
				if(areaEntity!=null){
					this.areaName = areaEntity.getValue("name");
				}
			}
			ApplicationModule module = ModuleProvider.getModule(this.addedResEntityType);
			this.addedResMap = module.toMap();
			//中英文转换map
			this.currentEntityChineseMap = new HashMap<String, Object>();
			//非空属性判断map
			this.nullableMap = new HashMap<String, Object>();
			//属性类型判断map
			this.attrTypeMap = new HashMap<String, Object>();
			//生成下拉框的map
			this.dropdownListMap = new HashMap<String, Object>();
			//属性长度判断map
			this.attrLengthMap = new HashMap<String,Object>();
			Map<String,Object> orderIdMap =new HashMap<String,Object>();//entity属性排序
			
			for(String key : this.addedResMap.keySet()) {
				try {
					List<BasicEntity> entry = null;
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						//数据字典，中英文转换
						entry = dictionary.getEntry(key + "," + this.addedResEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					}
					if(entry != null && !entry.isEmpty()) {
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							this.currentEntityChineseMap.put(key, entry.get(0).getValue("display"));
						//	System.out.println(entry.get(0).getValue("orderID").toString()+"--");
							orderIdMap.put(entry.get(0).getValue("orderID").toString(),key);
						}
					}
					
					//根据数据字典中，属性为entity类型或者general类型，获取属性对应的下拉框的值，提供前台进行下拉框选择操作
					//拿出来的值，例如:其他-其他,置换-置换,局用-局用
					if("entity".equals(entry.get(0).getValue("dictionaryType"))) {
						//entity类型下拉框数据组装
						List<BasicEntity> dropDownEntry = dictionary.getEntry(key + "," + this.addedResEntityType + ",networkResourceValueEmun", SearchScope.OBJECT, "");
						if(dropDownEntry != null && !dropDownEntry.isEmpty()) {
							String dropdownMixVal = dropDownEntry.get(0).getValue("mixValue");
							//先用逗号进行分隔
							String[] firstSplitArrs = dropdownMixVal.split(",");
							if(firstSplitArrs != null && firstSplitArrs.length > 0) {
								List<String> attrDropdownList = new ArrayList<String>();
								for(String secondVal : firstSplitArrs) {
									//再通过"-"获取key，value，获取value值
									String[] secondSplitArrs = secondVal.split(":");
									if(secondSplitArrs != null && secondSplitArrs.length == 2) {
										attrDropdownList.add(secondSplitArrs[1]);
										
									}
								}
								this.dropdownListMap.put(key, attrDropdownList);
							}
						}
						
					} else if("general".equals(entry.get(0).getValue("dictionaryType"))) {
						String generalEntryName = entry.get(0).getValue("generalEntryName");
						//general类型下拉框数据组装
						List<BasicEntity> dropDownEntry = dictionary.getEntry(generalEntryName + ",networkResourceValueEmun", SearchScope.OBJECT, "");
						if(dropDownEntry != null && !dropDownEntry.isEmpty()) {
							String dropdownMixVal = dropDownEntry.get(0).getValue("mixValue");
							//先用逗号进行分隔
							String[] firstSplitArrs = dropdownMixVal.split(",");
							if(firstSplitArrs != null && firstSplitArrs.length > 0) {
								List<String> attrDropdownList = new ArrayList<String>();
								for(String secondVal : firstSplitArrs) {
									//再通过"-"获取key，value，获取value值
									String[] secondSplitArrs = secondVal.split(":");
									if(secondSplitArrs != null && secondSplitArrs.length == 2) {
										attrDropdownList.add(secondSplitArrs[1]);
										
									}
								}
								this.dropdownListMap.put(key, attrDropdownList);
							}
						}
					}
				} catch (EntryOperationException e) {
					log.error("获取"+addedResEntityType+"属性字段"+key+"的中文字典失败，可能该字典不存在。");
					e.printStackTrace();
				}
				
				//判断是否需要增加非空判断
				Object nullVal = module.getAttribute(key).getValue("nullable");
				if(nullVal != null && !"".equals(nullVal)) {
					this.nullableMap.put(key, nullVal.toString());
				}
				//用map保存属性的类型,例如java.lang.String
				Object attrTypeVal = module.getAttribute(key).getValue("type");
				if(attrTypeVal != null && !"".equals(attrTypeVal)) {
					this.attrTypeMap.put(key, attrTypeVal.toString());
					if(attrTypeVal.toString().equals("java.lang.String")){
						//用map保存String类型属性的长度
						Object attrLengthVal = module.getAttribute(key).getValue("length");
						if(attrLengthVal!=null && !"".equals(attrLengthVal)){
							this.attrLengthMap.put(key,Integer.valueOf(attrLengthVal.toString()));
						}
					}
				}
				
				//生成属性的下拉框，前台中提供选择操作
				
			}
			
			//获取当前资源类型关联的父类型，用来控制隶属资源树单选按钮的生成
			String[] parentTypeArrs = structureCommonService.getAssociatedAetName(this.addedResEntityType, AssociatedType.PARENT, "networkresourcemanage");
			if(parentTypeArrs != null && parentTypeArrs.length > 0) {
				this.parentEntityTypeGrp = "";
				for (String type : parentTypeArrs) {
					this.parentEntityTypeGrp += type + "_";
				}
				this.parentEntityTypeGrp = this.parentEntityTypeGrp.substring(0, this.parentEntityTypeGrp.length() - 1);
			}
			
			//获取父级entity
			ApplicationEntity parentAppEntity = ApplicationEntity.changeFromEntity(physicalresService.getPhysicalresById(
					this.addedResParentEntityType, Long.parseLong(this.addedResParentEntityId)));
			String parentType = "";
			if(parentAppEntity.getType() != null) {
				parentType = parentAppEntity.getType();
			}
			this.parentEntityMap = parentAppEntity.toMap();
			this.parentEntityMap.put("type", parentType);
			
			ApplicationEntity[] grandpaAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity, AssociatedType.PARENT, "networkresourcemanage");
			if(grandpaAppEntityArrs != null && grandpaAppEntityArrs.length > 0) {
				//获取爷级节点
				ApplicationEntity grandpaAppEntity = grandpaAppEntityArrs[0];
				String grandpaType = "";
				if(grandpaAppEntity.getType() != null) {
					grandpaType = grandpaAppEntity.getType();
				}
				this.grandpaEntityMap = grandpaAppEntity.toMap();
				this.grandpaEntityMap.put("type", grandpaType);
			}
			//addedResMap排序
			Map<String,Object> sortedMap = quickSort.sortMap(this.addedResMap, orderIdMap);
			if(sortedMap!=null){
				this.addedResMap = sortedMap;
				//System.out.println(this.addedResMap+"*****");
			}
			if(this.loadBigPage != null) {
				log.info("退出loadAddPhysicalresPageAction，返回结果result=loadBigPage");
				return "loadBigPage"; //AJAX加载大部分页面
			} else {
				log.info("退出loadAddPhysicalresPageAction，返回结果result=success");
				return "success"; //页面跳转
			}
		} catch (RuntimeException e) {
			log.info("退出loadAddPhysicalresPageAction，发生RuntimeException异常，返回结果result=error");
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 加载修改纤芯成端维护页面
	 * @return
	 */
	public String loadUpdateFiberCoreAndTerminalAction() {
		log.info("进入loadUpdateFiberCoreAndTerminalAction()， 加载修改纤芯成端维护页面"); 
		try {
			this.noUseFiberCoreList = new ArrayList<Map<String,Object>>();
			this.useOneTimeTerminalList = new ArrayList<Map<String,Object>>();
			this.noUseTerminalList = new ArrayList<Map<String,Object>>();
			
			//获取父级 entity
			BasicEntity parentEntity = physicalresService.getPhysicalresById(this.parentEntityType, Long.parseLong(this.parentEntityId));
			ApplicationEntity parentAppEntity = ApplicationEntity.changeFromEntity(parentEntity);
			//获取父级entity名字
			if(parentAppEntity.getValue("name") != null && !"".equals(parentAppEntity.getValue("name"))) {
				this.parentEntityName = parentAppEntity.getValue("name");
			} else {
				this.parentEntityName = parentAppEntity.getValue("label");
			}
			//获取父级entity下的ODM
			ApplicationEntity[] odmAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity,"ODM",AssociatedType.CHILD, "networkresourcemanage");
			
			//存在ODM的情况
			if(odmAppArrs != null && odmAppArrs.length > 0) {
				//获取ODM下的端子
				for (ApplicationEntity odmApp : odmAppArrs) {
					//用过一次的端子集合
					List<Map<String, Object>> useOneTimeTerminalMapList = new ArrayList<Map<String, Object>>();
					//没用过的端子集合
					List<Map<String, Object>> noUseTerminalMapList = new ArrayList<Map<String, Object>>();
					ApplicationEntity[] odmTerminalAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmApp,"Terminal",AssociatedType.CHILD, "networkresourcemanage");
					if(odmTerminalAppArrs != null && odmTerminalAppArrs.length > 0) {
						for (ApplicationEntity odmTerminalApp : odmTerminalAppArrs) {
							//获取ODM端子关联的纤芯
							ApplicationEntity[] fiberCoreArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmTerminalApp,"FiberCore",AssociatedType.LINK, "networkresourcemanage");
							//获取ODM端子关联的尾纤
							ApplicationEntity[] PigtailedFiberArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmTerminalApp,"PigtailedFiber",AssociatedType.LINK, "networkresourcemanage");
							
							if(fiberCoreArrs != null || PigtailedFiberArrs != null) {
								int resultCount = fiberCoreArrs.length + PigtailedFiberArrs.length;
								if(resultCount == 1) {
									//已成端子
									Map<String, Object> useOneTimeTerminalMap = new HashMap<String, Object>();
									useOneTimeTerminalMap.put("entityName", odmTerminalApp.getValue("name"));
									useOneTimeTerminalMap.put("entityId", odmTerminalApp.getValue("id"));
									useOneTimeTerminalMap.put("type", odmTerminalApp.getType());
									useOneTimeTerminalMapList.add(useOneTimeTerminalMap);
								} else if (resultCount == 0) {
									//未成端子
									Map<String, Object> noUseTerminalMap = new HashMap<String, Object>();
									noUseTerminalMap.put("entityName", odmTerminalApp.getValue("name"));
									noUseTerminalMap.put("entityId", odmTerminalApp.getValue("id"));
									noUseTerminalMap.put("type", odmTerminalApp.getType());
									noUseTerminalMapList.add(noUseTerminalMap);
								}
							}
						}
					}
					//模板与端子的组合集合(一模板对应多个端子，为了页面进行展示用)
					//已成端子集合
					if(useOneTimeTerminalMapList != null && !useOneTimeTerminalMapList.isEmpty()) {
						quickSort.sort(useOneTimeTerminalMapList,"entityName");
						Map<String,Object> useOneTimeTerminalMap = new HashMap<String,Object>();
						useOneTimeTerminalMap.put("module", odmApp.getValue("label"));
						useOneTimeTerminalMap.put("contentList", useOneTimeTerminalMapList);
						useOneTimeTerminalMap.put("count", useOneTimeTerminalMapList.size());
						this.useOneTimeTerminalList.add(useOneTimeTerminalMap);
					}
					//未成端子集合
					if(noUseTerminalMapList != null && !noUseTerminalMapList.isEmpty()) {
						quickSort.sort(noUseTerminalMapList, "entityName");
						Map<String,Object> noUseTerminalMap = new HashMap<String,Object>();
						noUseTerminalMap.put("module", odmApp.getValue("label"));
						noUseTerminalMap.put("contentList", noUseTerminalMapList);
						noUseTerminalMap.put("count", noUseTerminalMapList.size());
						this.noUseTerminalList.add(noUseTerminalMap);
					}
				}
			} else {
				//不存在ODM的情况(直接从父级entity获取端子)
				ApplicationEntity[] terminalAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity,"FiberCore",AssociatedType.LINK, "networkresourcemanage");
				//用过一次的端子集合
				List<Map<String, Object>> useOneTimeTerminalMapList = new ArrayList<Map<String, Object>>();
				//没用过的端子集合
				List<Map<String, Object>> noUseTerminalMapList = new ArrayList<Map<String, Object>>();
				for (ApplicationEntity terminalApp : terminalAppArrs) {
					//获取端子关联的纤芯
					ApplicationEntity[] fiberCoreArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp,"FiberCore",AssociatedType.LINK, "networkresourcemanage");
					//获取端子关联的尾纤
					ApplicationEntity[] PigtailedFiberArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp,"PigtailedFiber",AssociatedType.LINK, "networkresourcemanage");
					if(fiberCoreArrs != null || PigtailedFiberArrs != null) {
						int resultCount = fiberCoreArrs.length + PigtailedFiberArrs.length;
						if(resultCount == 1) {
							//已成端子
							Map<String, Object> useOneTimeTerminalMap = new HashMap<String, Object>();
							useOneTimeTerminalMap.put("entityName", terminalApp.getValue("name"));
							useOneTimeTerminalMap.put("entityId", terminalApp.getValue("id"));
							useOneTimeTerminalMap.put("type", terminalApp.getType());
							useOneTimeTerminalMapList.add(useOneTimeTerminalMap);
						} else if (resultCount == 0) {
							//未成端子
							Map<String, Object> noUseTerminalMap = new HashMap<String, Object>();
							noUseTerminalMap.put("entityName", terminalApp.getValue("name"));
							noUseTerminalMap.put("entityId", terminalApp.getValue("id"));
							noUseTerminalMap.put("type", terminalApp.getType());
							noUseTerminalMapList.add(noUseTerminalMap);
						}
					}
				}
				//已成端子集合
				if(useOneTimeTerminalMapList != null && !useOneTimeTerminalMapList.isEmpty()) {
					quickSort.sort(useOneTimeTerminalMapList, "entityName");
					Map<String,Object> useOneTimeTerminalMap = new HashMap<String,Object>();
					useOneTimeTerminalMap.put("module", "默认模板");
					useOneTimeTerminalMap.put("contentList", useOneTimeTerminalMapList);
					useOneTimeTerminalMap.put("count", useOneTimeTerminalMapList.size());
					this.useOneTimeTerminalList.add(useOneTimeTerminalMap);
				}
				//未成端子集合
				if(noUseTerminalMapList != null && !noUseTerminalMapList.isEmpty()) {
					quickSort.sort(noUseTerminalMapList, "entityName");
					Map<String,Object> noUseTerminalMap = new HashMap<String,Object>();
					noUseTerminalMap.put("module", "默认模板");
					noUseTerminalMap.put("contentList", noUseTerminalMapList);
					noUseTerminalMap.put("count", noUseTerminalMapList.size());
					this.noUseTerminalList.add(noUseTerminalMap);
				}
			}
			if(this.noUseTerminalList!=null){
				quickSort.sort(noUseTerminalList, "module");
			}
			if(this.useOneTimeTerminalList!=null){
				quickSort.sort(this.useOneTimeTerminalList, "module");
			}
			
			/*if(noUseTerminalList != null && !noUseTerminalList.isEmpty()) {
				//System.out.println(noUseTerminalList.size());
				for (Map<String, Object> map : noUseTerminalList) {
					System.out.println(map.get("contentList"));
				}
			}*/
			
			//获取ODF下的光缆段
			ApplicationEntity[] FiberSectionAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity,"FiberSection",AssociatedType.LINK, "networkresourcemanage");
			if(FiberSectionAppArrs != null && FiberSectionAppArrs.length > 0) {
				//获取光缆段下的纤芯
				for (ApplicationEntity fiberSectionApp : FiberSectionAppArrs) {
					ApplicationEntity[] fiberCoreAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberSectionApp,"FiberCore",AssociatedType.LINK, "networkresourcemanage");
					if(fiberCoreAppArrs != null && fiberCoreAppArrs.length > 0) {
						//没用过的纤芯集合
						List<Map<String, Object>> noUseFiberCoreMapList = new ArrayList<Map<String, Object>>();
						//获取与纤芯关联的端子
						for (ApplicationEntity fiberCoreApp : fiberCoreAppArrs) {
							//ApplicationEntity[] fiberCoreLinkArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreApp, AssociatedType.LINK, "networkresourcemanage");
							ApplicationEntity[] terminalAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreApp,"Terminal",AssociatedType.LINK, "networkresourcemanage");
							//纤芯的连接数是一，同时关联的端子数是一个
							if(terminalAppArrs != null && terminalAppArrs.length == 1) {
								boolean useOneTime = false;
								for (ApplicationEntity terminalApp : terminalAppArrs) {
									//存在ODM的情况
									if(odmAppArrs != null && odmAppArrs.length > 0) {
										//获取ODM下的端子
										for (ApplicationEntity odmApp : odmAppArrs) {
											ApplicationEntity[] odmTerminalAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmApp,"Terminal",AssociatedType.CHILD, "networkresourcemanage");
											if(odmTerminalAppArrs != null && odmTerminalAppArrs.length > 0) {
												//boolean useOneTime = false;
												for (ApplicationEntity odmTerminalApp : odmTerminalAppArrs) {
													//判断纤芯连接的端子是否在该ODM下面
													if(terminalApp.getValue("id").equals(odmTerminalApp.getValue("id"))) {
														//纤芯连接的端子在该ODM下面
														useOneTime = true;
														break;
													}
												}
												//纤芯的连接端子数为一，但该端子并不是本端端子的情况
											/*	if(!useOneTime) {
													//该纤芯未进行任何关联，为未成纤芯
													Map<String, Object> noUseFiberCoreMap = new HashMap<String, Object>();
													noUseFiberCoreMap.put("entityName", fiberCoreApp.getValue("label"));
													noUseFiberCoreMap.put("entityId", fiberCoreApp.getValue("id"));
													noUseFiberCoreMap.put("type", fiberCoreApp.getType());
													noUseFiberCoreMapList.add(noUseFiberCoreMap);
												}*/
											}
										}
										
									} else {
										//不存在ODM的情况(直接从父级entity获取端子)
										ApplicationEntity[] terminalArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity,"FiberCore",AssociatedType.LINK, "networkresourcemanage");
										//boolean useOneTime = false;
										for (ApplicationEntity terminalAe : terminalArrs) {
											if(terminalApp.getValue("id").equals(terminalAe.getValue("id"))) {
												//纤芯连接的端子该父级entity下
												useOneTime = true;
												break;
											}
										}
										//纤芯的连接端子数为一，但该端子并不是本端端子的情况
										/*if(!useOneTime) {
											//该纤芯未进行任何关联，为未成纤芯
											Map<String, Object> noUseFiberCoreMap = new HashMap<String, Object>();
											noUseFiberCoreMap.put("entityName", fiberCoreApp.getValue("label"));
											noUseFiberCoreMap.put("entityId", fiberCoreApp.getValue("id"));
											noUseFiberCoreMap.put("type", fiberCoreApp.getType());
											noUseFiberCoreMapList.add(noUseFiberCoreMap);
										}*/
									}
								}
								//System.out.println(useOneTime);
								if(!useOneTime) {
									//该纤芯未进行任何关联，为未成纤芯
									Map<String, Object> noUseFiberCoreMap = new HashMap<String, Object>();
									noUseFiberCoreMap.put("entityName", fiberCoreApp.getValue("label"));
									noUseFiberCoreMap.put("entityId", fiberCoreApp.getValue("id"));
									noUseFiberCoreMap.put("type", fiberCoreApp.getType());
									noUseFiberCoreMapList.add(noUseFiberCoreMap);
								}
							} else if(terminalAppArrs != null && terminalAppArrs.length == 0){
								//该纤芯未进行任何关联，为未成纤芯
								Map<String, Object> noUseFiberCoreMap = new HashMap<String, Object>();
								noUseFiberCoreMap.put("entityName", fiberCoreApp.getValue("label"));
								noUseFiberCoreMap.put("entityId", fiberCoreApp.getValue("id"));
								noUseFiberCoreMap.put("type", fiberCoreApp.getType());
								noUseFiberCoreMapList.add(noUseFiberCoreMap);
							}
						}
						
						//光缆段与纤芯的组合集合(一光缆段对应多条纤芯，为了页面进行展示用)
						//未成纤芯集合
						if(noUseFiberCoreMapList != null && !noUseFiberCoreMapList.isEmpty()) {
							quickSort.sort(noUseFiberCoreMapList, "entityName");
							Map<String,Object> noUseFiberSectionMap = new HashMap<String,Object>();
							noUseFiberSectionMap.put("module", fiberSectionApp.getValue("name"));
							noUseFiberSectionMap.put("contentList", noUseFiberCoreMapList);
							noUseFiberSectionMap.put("count", noUseFiberCoreMapList.size());
							this.noUseFiberCoreList.add(noUseFiberSectionMap);
						}
					}
				}
			}
			if(this.noUseFiberCoreList!=null){
				quickSort.sort(this.noUseFiberCoreList, "module");
			}
			
			this.resultList = new ArrayList<Map<String, Object>>();
			//获取父级entity下的ODM
			ApplicationEntity[] odmAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity, "ODM", AssociatedType.CHILD, "networkresourcemanage");
			//存在ODM的情况
			if(odmAppEntityArrs != null && odmAppEntityArrs.length > 0) {
				for(ApplicationEntity odmApp: odmAppEntityArrs) {
					//获取ODM下的端子
					ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmApp, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
					if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
						//获取与端子关联的纤芯
						for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
							ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
							if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
								for (ApplicationEntity fiberCoreAppEntity : fiberCoreAppEntityArrs) {
									//获取纤芯所关联的光缆段
									ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
									if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
										//一条纤芯只属于一个光缆段
										ApplicationEntity fiberSectionAppEntity = fiberSectionAppEntityArrs[0];
										
										//获取父级entity下的光缆段
										/*ApplicationEntity[] fiberSectionAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
										boolean isTheSameFiberSection = false;
										if(fiberSectionAppArrs != null && fiberSectionAppArrs.length > 0) {
											for (ApplicationEntity fiberSectionApp : fiberSectionAppArrs) {
												if(fiberSectionAppEntity.getValue("id").equals(fiberSectionApp.getValue("id"))) {
													isTheSameFiberSection = true;
												}
											}
										}
										//纤芯所属光缆段，不在该ODF下
										if(!isTheSameFiberSection) {
											continue;
										}*/
										Map<String, Object> extraMap = new HashMap<String, Object>();
										extraMap.put("entityName", fiberSectionAppEntity.getValue("name") + "/" +fiberCoreAppEntity.getValue("label") + "-" + terminalApp.getValue("name"));
										//纤芯和端子的id
										extraMap.put("id", fiberCoreAppEntity.getValue("id") + "-" + terminalApp.getValue("id"));
										extraMap.put("type", fiberCoreAppEntity.getType() + "-" + terminalApp.getType());
										this.resultList.add(extraMap);
									}
								}
							}
						}
					}
				}
			} else {
				//不存在ODM的情况(直接从父级entity获取端子)
				ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
				if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
					//获取与端子关联的纤芯
					for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
						ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
						if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
							if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
								for (ApplicationEntity fiberCoreAppEntity : fiberCoreAppEntityArrs) {
									//获取纤芯所关联的光缆段
									ApplicationEntity[] fiberSectionAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
									if(fiberSectionAppEntityArrs != null && fiberSectionAppEntityArrs.length > 0){
										//一条纤芯只属于一个光缆段
										ApplicationEntity fiberSectionAppEntity = fiberSectionAppEntityArrs[0];
										Map<String, Object> extraMap = new HashMap<String, Object>();
										extraMap.put("entityName", fiberSectionAppEntity.getValue("name") + "/" +fiberCoreAppEntity.getValue("label") + "-" + terminalApp.getValue("name"));
										//纤芯和端子的id
										extraMap.put("id", fiberCoreAppEntity.getValue("id") + "-" + terminalApp.getValue("id"));
										extraMap.put("type", fiberCoreAppEntity.getType() + "-" + terminalApp.getType());
										this.resultList.add(extraMap);
									}
								}
							}
						}
					}
				}
			}
			if(resultList!=null){
				quickSort.sort(this.resultList, "entityName");
			}
			log.info("退出loadUpdateFiberCoreAndTerminalAction()，返回结果result=success"); 
			return "success";
		} catch (NumberFormatException e) {
			log.error("退出loadUpdateFiberCoreAndTerminalAction()，发生NumberFormatException异常，返回结果result=error"); 
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 加载熔纤接续维护页面
	 * @return
	 */
	public String loadUpdateFiberCoreAndFiberCoreAction() {
		log.info("进入loadUpdateFiberCoreAndFiberCoreAction()，加载熔纤接续维护页面");
		try {
			this.canUseFiberCoreList = new ArrayList<Map<String,Object>>();
			//获取父级 entity
			BasicEntity parentEntity = physicalresService.getPhysicalresById(this.parentEntityType, Long.parseLong(this.parentEntityId));
			ApplicationEntity parentAppEntity = ApplicationEntity.changeFromEntity(parentEntity);
			//获取父级entity名字
			if(parentAppEntity.getValue("name") != null && !"".equals(parentAppEntity.getValue("name"))) {
				this.parentEntityName = parentAppEntity.getValue("name");
			} else {
				this.parentEntityName = parentAppEntity.getValue("label");
			}
			//获取接头下关联的光缆段
			ApplicationEntity[] fiberSectionAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
			if(fiberSectionAppArrs != null && fiberSectionAppArrs.length > 0) {
				for (ApplicationEntity fiberSectionApp : fiberSectionAppArrs) {
					//需要显示的光缆段(纤芯集合对应的光缆段)
					Map<String, Object> fiberSectionMap = new HashMap<String, Object>();
					//获取光缆段下关联的纤芯
					ApplicationEntity[] fiberCoreAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberSectionApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
					//用于保存可使用的纤芯集合
					List<Map<String, Object>> fiberCoreList = new ArrayList<Map<String,Object>>();
					for (ApplicationEntity fiberCoreApp : fiberCoreAppArrs) {
						//获取光缆段下纤芯所关联的纤芯
						ApplicationEntity[] fiberCoreLinkAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
						if(fiberCoreLinkAppArrs != null && fiberCoreLinkAppArrs.length == 0) {
							//没有关联的纤芯，所以该纤芯可使用
							Map<String, Object> fiberCoreMap = new HashMap<String, Object>();
							fiberCoreMap.put("entityName", fiberCoreApp.getValue("label"));
							fiberCoreMap.put("entityId", fiberCoreApp.getValue("id"));
							fiberCoreMap.put("type", fiberCoreApp.getType());
							fiberCoreList.add(fiberCoreMap);
						} else if(fiberCoreLinkAppArrs != null && fiberCoreLinkAppArrs.length == 1) {
							//关联的纤芯数为1
							ApplicationEntity fiberCoreLinkApp = fiberCoreLinkAppArrs[0];
							//获取所关联的纤芯所在的光缆段
							ApplicationEntity[] fiberCoreLinkFiberSectionAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreLinkApp, "FiberSection", AssociatedType.LINK, "networkresourcemanage");
							if(fiberCoreLinkFiberSectionAppArrs != null && fiberCoreLinkFiberSectionAppArrs.length == 1) {
								//一条纤芯，只属于一个光缆段
								ApplicationEntity fiberCoreLinkFiberSectionApp = fiberCoreLinkFiberSectionAppArrs[0];
								//判断所关联的纤芯，是否在本接头关联的光缆段下
								boolean isLinkMyJoint = false;
								for(ApplicationEntity myFiberSectionApp : fiberSectionAppArrs) {
									if(fiberCoreLinkFiberSectionApp.getValue("id").equals(myFiberSectionApp.getValue("id"))) {
										isLinkMyJoint = true;
									}
								}
								//关联的纤芯所关联的光缆段，并非是本接头的光缆段的情况，因此该纤芯可使用
								if(!isLinkMyJoint) {
									Map<String, Object> fiberCoreMap = new HashMap<String, Object>();
									fiberCoreMap.put("entityName", fiberCoreApp.getValue("label"));
									fiberCoreMap.put("entityId", fiberCoreApp.getValue("id"));
									fiberCoreMap.put("type", fiberCoreApp.getType());
									fiberCoreList.add(fiberCoreMap);
								}
							}
						}
					}
					if(fiberCoreList != null && fiberCoreList.size() > 0) {
						quickSort.sort(fiberCoreList, "entityName");
						fiberSectionMap.put("module", fiberSectionApp.getValue("name"));
						fiberSectionMap.put("contentList", fiberCoreList);
						fiberSectionMap.put("count", fiberCoreList.size());
						this.canUseFiberCoreList.add(fiberSectionMap);
					}
					
					//加载结果熔纤接续集合
					this.resultList = new ArrayList<Map<String,Object>>();
					if(fiberSectionAppArrs != null && fiberSectionAppArrs.length > 0) {
						//此map用来过滤重复数据
						List<Map<String, Object>> targetMapList = new ArrayList<Map<String,Object>>();
						for (ApplicationEntity fiberSectionAppEntity : fiberSectionAppArrs) {
							//查出光缆段连接的纤芯
							ApplicationEntity[] fiberCoreAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberSectionAppEntity, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
							if(fiberCoreAppEntityArrs != null && fiberCoreAppEntityArrs.length > 0) {
								//查出纤芯所连接的纤芯
								for (ApplicationEntity fiberCoreApp : fiberCoreAppEntityArrs) {
									ApplicationEntity[] fiberCoreLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreApp, "FiberCore", AssociatedType.LINK, "networkresourcemanage");
									if(fiberCoreLinkAppEntityArrs != null && fiberCoreLinkAppEntityArrs.length > 0) {
										for (ApplicationEntity fiberCoreLinkApp : fiberCoreLinkAppEntityArrs) {
											//查出连接纤芯所属的光缆段
											ApplicationEntity[] fiberSectionLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(fiberCoreLinkApp,"FiberSection",AssociatedType.LINK, "networkresourcemanage");
											if(fiberSectionLinkAppEntityArrs != null && fiberSectionLinkAppEntityArrs.length > 0) {
												//一条纤芯只属于一个光缆段
												ApplicationEntity fiberSectionLinkAppEntity = fiberSectionLinkAppEntityArrs[0];
												//获取纤芯所连接的纤芯是否属于本接头的光缆段
												
												//System.out.println("纤芯所属的光缆段：" + fiberSectionLinkAppEntity.getValue("id"));
												
												boolean isTheSameFiberSection = false;
												for (ApplicationEntity fiberSectionAppByJoin : fiberSectionAppArrs) {
													//纤芯所连接的纤芯不属于该接头的光缆段的情况
													if(fiberSectionLinkAppEntity.getValue("id").toString().equals(fiberSectionAppByJoin.getValue("id").toString())) {
														isTheSameFiberSection = true;
													}
												}
												//纤芯所连接的纤芯不属于该接头的光缆段的情况
												if(!isTheSameFiberSection) {
													continue;
												}
									
												//进行重复数据的过滤操作
												Map<String ,Object> targetMap = new HashMap<String, Object>();
												targetMap.put("leftId", fiberCoreApp.getValue("id").toString());
												targetMap.put("rightId", fiberCoreLinkApp.getValue("id").toString());
												targetMapList.add(targetMap);
											}
										}
									}
								}
							}
						}
						
						for(Map<String, Object> targetMap : targetMapList) {
							for(Map<String, Object> targetOppositeMap : targetMapList) {
								String targetMapStr = targetMap.get("leftId") + "-" + targetMap.get("rightId");
								String targetOppositeMapStr = targetOppositeMap.get("rightId") + "-" + targetOppositeMap.get("leftId");
								if(targetMapStr.equals(targetOppositeMapStr)) {
									targetMap.remove("leftId");
									targetMap.remove("rightId");
								}
							}
						}
						
						for(Map<String, Object> targetMap : targetMapList) {
							if(targetMap.containsKey("leftId") && targetMap.containsKey("rightId")) {
								BasicEntity leftFiberCoreEntity = physicalresService.getPhysicalresById("FiberCore", Long.parseLong(targetMap.get("leftId").toString()));
								BasicEntity rightFiberCoreEntity = physicalresService.getPhysicalresById("FiberCore", Long.parseLong(targetMap.get("rightId").toString()));
								ApplicationEntity leftFiberCoreAppEntity = ApplicationEntity.changeFromEntity(leftFiberCoreEntity);
								ApplicationEntity rightFiberCoreAppEntity = ApplicationEntity.changeFromEntity(rightFiberCoreEntity);
								//获取纤芯所属的光缆段
								ApplicationEntity[] leftFiberSectionLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(leftFiberCoreAppEntity,"FiberSection",AssociatedType.LINK, "networkresourcemanage");
								ApplicationEntity[] rightFiberSectionLinkAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(rightFiberCoreAppEntity,"FiberSection",AssociatedType.LINK, "networkresourcemanage");
								if(leftFiberSectionLinkAppEntityArrs != null && leftFiberSectionLinkAppEntityArrs.length > 0) {
									//一条纤芯只属于一条光缆段
									ApplicationEntity leftFiberSectionLinkAppEntity = leftFiberSectionLinkAppEntityArrs[0];
									ApplicationEntity rightFiberSectionLinkAppEntity = rightFiberSectionLinkAppEntityArrs[0];
									String leftEntityName = leftFiberSectionLinkAppEntity.getValue("name") + "/" + leftFiberCoreAppEntity.getValue("label");
									String rightEntityName = rightFiberSectionLinkAppEntity.getValue("name") + "/" + rightFiberCoreAppEntity.getValue("label");
									
									Map<String ,Object> extraMap = new HashMap<String, Object>();
									//拼接两个纤芯的id，以备日后与光缆段解除关系使用
									extraMap.put("id", targetMap.get("leftId") + "-" + targetMap.get("rightId"));
									extraMap.put("entityName", leftEntityName + " - " + rightEntityName);
									//获取纤芯类型
									extraMap.put("type", leftFiberCoreAppEntity.getType() + "-" + rightFiberCoreAppEntity.getType());
									this.resultList.add(extraMap);
								}
							}
						}
					}	
				}
			}
			if(resultList!=null){
				quickSort.sort(this.resultList, "entityName");
			}
			if(this.canUseFiberCoreList!=null){
				quickSort.sort(this.canUseFiberCoreList,"module");
			}
			log.info("退出loadUpdateFiberCoreAndFiberCoreAction()，返回结果result=success"); 
			return "success";
		} catch (NumberFormatException e) {
			log.error("退出loadUpdateFiberCoreAndFiberCoreAction()，发生NumberFormatException异常，返回结果result=error"); 
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 加载跳纤或尾纤维护页面
	 * @return
	 */
	public String loadPigtailedFiberAction() {
		log.info("进入loadPigtailedFiberAction，加载跳纤或尾纤维护页面");
		try {
			if(areaId!=null&&!"".equals(areaId)){
				BasicEntity areaEntity =physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
				if(areaEntity!=null){
					this.areaName = areaEntity.getValue("name");
				}
			}
			this.noUseTerminalList = new ArrayList<Map<String,Object>>();
			this.useOneTimeTerminalList = new ArrayList<Map<String,Object>>();
			
			//获取父级 entity
			BasicEntity parentEntity = physicalresService.getPhysicalresById(this.parentEntityType, Long.parseLong(this.parentEntityId));
			ApplicationEntity parentAppEntity = ApplicationEntity.changeFromEntity(parentEntity);
			//获取父级entity名字
			if(parentAppEntity.getValue("name") != null && !"".equals(parentAppEntity.getValue("name"))) {
				this.parentEntityName = parentAppEntity.getValue("name");
			} else {
				this.parentEntityName = parentAppEntity.getValue("label");
			}
			//获取父级entity下的ODM
			ApplicationEntity[] odmAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity,"ODM",AssociatedType.CHILD, "networkresourcemanage");
			
			//存在ODM的情况
			if(odmAppArrs != null && odmAppArrs.length > 0) {
				//获取ODM下的端子
				for (ApplicationEntity odmApp : odmAppArrs) {
					//用过一次的端子集合
					List<Map<String, Object>> useOneTimeTerminalMapList = new ArrayList<Map<String, Object>>();
					//没用过的端子集合
					List<Map<String, Object>> noUseTerminalMapList = new ArrayList<Map<String, Object>>();
					ApplicationEntity[] odmTerminalAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmApp,"Terminal",AssociatedType.CHILD, "networkresourcemanage");
					if(odmTerminalAppArrs != null && odmTerminalAppArrs.length > 0) {
						for (ApplicationEntity odmTerminalApp : odmTerminalAppArrs) {
							//获取ODM端子关联的纤芯
							ApplicationEntity[] fiberCoreArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmTerminalApp,"FiberCore",AssociatedType.LINK, "networkresourcemanage");
							//获取ODM端子关联的尾纤
							ApplicationEntity[] PigtailedFiberArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmTerminalApp,"PigtailedFiber",AssociatedType.LINK, "networkresourcemanage");
							
							if(fiberCoreArrs != null || PigtailedFiberArrs != null) {
								int resultCount = fiberCoreArrs.length + PigtailedFiberArrs.length;
								if(resultCount == 1) {
									//已成端子
									Map<String, Object> useOneTimeTerminalMap = new HashMap<String, Object>();
									useOneTimeTerminalMap.put("entityName", odmTerminalApp.getValue("name"));
									useOneTimeTerminalMap.put("entityId", odmTerminalApp.getValue("id"));
									useOneTimeTerminalMap.put("type", odmTerminalApp.getType());
									useOneTimeTerminalMapList.add(useOneTimeTerminalMap);
								} else if (resultCount == 0) {
									//未成端子
									Map<String, Object> noUseTerminalMap = new HashMap<String, Object>();
									noUseTerminalMap.put("entityName", odmTerminalApp.getValue("name"));
									noUseTerminalMap.put("entityId", odmTerminalApp.getValue("id"));
									noUseTerminalMap.put("type", odmTerminalApp.getType());
									noUseTerminalMapList.add(noUseTerminalMap);
								}
							}
						}
					}
					//模板与端子的组合集合(一模板对应多个端子，为了页面进行展示用)
					//已成端子集合
					if(useOneTimeTerminalMapList != null && !useOneTimeTerminalMapList.isEmpty()) {
						quickSort.sort(useOneTimeTerminalMapList, "entityName");
						Map<String,Object> useOneTimeTerminalMap = new HashMap<String,Object>();
						useOneTimeTerminalMap.put("module", odmApp.getValue("label"));
						useOneTimeTerminalMap.put("contentList", useOneTimeTerminalMapList);
						useOneTimeTerminalMap.put("count", useOneTimeTerminalMapList.size());
						this.useOneTimeTerminalList.add(useOneTimeTerminalMap);
					}
					//未成端子集合
					if(noUseTerminalMapList != null && !noUseTerminalMapList.isEmpty()) {
						quickSort.sort(noUseTerminalMapList, "entityName");
						Map<String,Object> noUseTerminalMap = new HashMap<String,Object>();
						noUseTerminalMap.put("module", odmApp.getValue("label"));
						noUseTerminalMap.put("contentList", noUseTerminalMapList);
						noUseTerminalMap.put("count", noUseTerminalMapList.size());
						this.noUseTerminalList.add(noUseTerminalMap);
					}
				}
			} else {
				//不存在ODM的情况(直接从父级entity获取端子)
				ApplicationEntity[] terminalAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity,"FiberCore",AssociatedType.LINK, "networkresourcemanage");
				//用过一次的端子集合
				List<Map<String, Object>> useOneTimeTerminalMapList = new ArrayList<Map<String, Object>>();
				//没用过的端子集合
				List<Map<String, Object>> noUseTerminalMapList = new ArrayList<Map<String, Object>>();
				for (ApplicationEntity terminalApp : terminalAppArrs) {
					//获取端子关联的纤芯
					ApplicationEntity[] fiberCoreArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp,"FiberCore",AssociatedType.LINK, "networkresourcemanage");
					//获取端子关联的尾纤
					ApplicationEntity[] PigtailedFiberArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp,"PigtailedFiber",AssociatedType.LINK, "networkresourcemanage");
					if(fiberCoreArrs != null || PigtailedFiberArrs != null) {
						int resultCount = fiberCoreArrs.length + PigtailedFiberArrs.length;
						if(resultCount == 1) {
							//已成端子
							Map<String, Object> useOneTimeTerminalMap = new HashMap<String, Object>();
							useOneTimeTerminalMap.put("entityName", terminalApp.getValue("name"));
							useOneTimeTerminalMap.put("entityId", terminalApp.getValue("id"));
							useOneTimeTerminalMap.put("type", terminalApp.getType());
							useOneTimeTerminalMapList.add(useOneTimeTerminalMap);
						} else if (resultCount == 0) {
							//未成端子
							Map<String, Object> noUseTerminalMap = new HashMap<String, Object>();
							noUseTerminalMap.put("entityName", terminalApp.getValue("name"));
							noUseTerminalMap.put("entityId", terminalApp.getValue("id"));
							noUseTerminalMap.put("type", terminalApp.getType());
							noUseTerminalMapList.add(noUseTerminalMap);
						}
					}
				}
				//已成端子集合
				if(useOneTimeTerminalMapList != null && !useOneTimeTerminalMapList.isEmpty()) {
					quickSort.sort(useOneTimeTerminalMapList, "entityName");
					Map<String,Object> useOneTimeTerminalMap = new HashMap<String,Object>();
					useOneTimeTerminalMap.put("module", "默认模板");
					useOneTimeTerminalMap.put("contentList", useOneTimeTerminalMapList);
					useOneTimeTerminalMap.put("count", useOneTimeTerminalMapList.size());
					this.useOneTimeTerminalList.add(useOneTimeTerminalMap);
				}
				//未成端子集合
				if(noUseTerminalMapList != null && !noUseTerminalMapList.isEmpty()) {
					quickSort.sort(noUseTerminalMapList, "entityName");
					Map<String,Object> noUseTerminalMap = new HashMap<String,Object>();
					noUseTerminalMap.put("module", "默认模板");
					noUseTerminalMap.put("contentList", noUseTerminalMapList);
					noUseTerminalMap.put("count", noUseTerminalMapList.size());
					this.noUseTerminalList.add(noUseTerminalMap);
				}
			}
			if(this.useOneTimeTerminalList!=null){
				quickSort.sort(this.useOneTimeTerminalList, "module");
			}
			if(this.noUseTerminalList!=null){
				quickSort.sort(this.noUseTerminalList, "module");
			}
			//加载跳纤或尾纤结果集合
			this.resultList = new ArrayList<Map<String,Object>>();
			//获取父级entity下的ODM
			ApplicationEntity[] odmAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity, "ODM", AssociatedType.CHILD, "networkresourcemanage");
			//存在ODM的情况
			Set<Map<String, Object>> filterSet = new HashSet<Map<String, Object>>();//通过Set对端子连接的同一跳纤或尾纤进行过滤
			if(odmAppEntityArrs != null && odmAppEntityArrs.length > 0) {
				for(ApplicationEntity odmApp: odmAppEntityArrs) {
					//获取ODM下的端子
					ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmApp, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
					if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
						for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
							//获取与端子关联的跳纤或尾纤
							ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, "networkresourcemanage");
							if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
								
								for (ApplicationEntity pigtailedFiberApp : pigtailedFiberAppEntityArrs) {
									Map<String, Object> extraMap = new HashMap<String, Object>();
									
									extraMap.put("id", pigtailedFiberApp.getValue("id"));
									extraMap.put("entityName", pigtailedFiberApp.getValue("label"));
									extraMap.put("type", pigtailedFiberApp.getType());
									filterSet.add(extraMap);
								}
							}
						}
					}
				}
			} else {
				//不存在ODM的情况(直接从父级entity获取端子)
				ApplicationEntity[] terminalAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(parentAppEntity, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
				if(terminalAppEntityArrs != null && terminalAppEntityArrs.length > 0) {
					for(ApplicationEntity terminalApp : terminalAppEntityArrs) {
						//获取与端子关联的跳纤或尾纤
						ApplicationEntity[] pigtailedFiberAppEntityArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp, "PigtailedFiber", AssociatedType.LINK, "networkresourcemanage");
						if(pigtailedFiberAppEntityArrs != null && pigtailedFiberAppEntityArrs.length > 0) {
							
							for (ApplicationEntity pigtailedFiberApp : pigtailedFiberAppEntityArrs) {
								Map<String, Object> extraMap = new HashMap<String, Object>();
								extraMap.put("id", pigtailedFiberApp.getValue("id"));
								extraMap.put("entityName", pigtailedFiberApp.getValue("label"));
								extraMap.put("type", pigtailedFiberApp.getType());
								filterSet.add(extraMap);
							}
						}
					}
				}
			}
			//通过Set过滤重复的跳纤或尾纤数据，再放进list里面
			for (Map<String, Object> map : filterSet) {
				this.resultList.add(map);
			}
			if(this.resultList!=null){
				quickSort.sort(this.resultList, "entityName");
			}
			if(this.loadPartPage != null) {
				log.info("退出loadPigtailedFiberAction()，返回结果result=loadPartPage"); 
				//AJAX局部加载
				return "loadPartPage";
			} else {
				log.info("退出loadPigtailedFiberAction()，返回结果result=success"); 
				//整个页面加载
				return "success";
			}
		} catch (NumberFormatException e) {
			log.error("退出loadPigtailedFiberAction()，发生NumberFormatException异常，返回结果result=error"); 
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 纤芯成端、熔纤接续的删除(删除关系，只删除一个)
	 */
	public void deleteRelationForExtraForOnlyOneAction() {
		log.info("进入deleteRelationForExtraForOnlyOneAction()，纤芯成端、熔纤接续的删除(删除关系，只删除一个)");
		//获取要删除资源的id组和类型组
		String[] typeArrs = this.chooseResEntityType.split("-");
		String[] idArrs = this.chooseResEntityId.split("-");
		if(typeArrs != null && typeArrs.length == 2 && idArrs != null && idArrs.length == 2) {
			//获取要被删除的两个对象
			ApplicationEntity leftAe = ApplicationEntity.changeFromEntity(physicalresService.getPhysicalresById(typeArrs[0], Long.parseLong(idArrs[0])));
			ApplicationEntity rightAe = ApplicationEntity.changeFromEntity(physicalresService.getPhysicalresById(typeArrs[1], Long.parseLong(idArrs[1])));
			//删除关系
			structureCommonService.delStrutureAssociation(leftAe, rightAe, AssociatedType.LINK, "networkresourcemanage");
			structureCommonService.delStrutureAssociation(rightAe,leftAe,  AssociatedType.LINK, "networkresourcemanage");		
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson("success");
		try {
			log.info("退出deleteRelationForExtraForOnlyOneAction()，返回结果result="+result); 
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出deleteRelationForExtraForOnlyOneAction()，返回结果result="+result+"失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 纤芯成端、熔纤接续的删除(删除关系)
	 */
	public void deleteRelationForExtraAction() {
		log.info("进入deleteRelationForExtraAction()，纤芯成端、熔纤接续的删除(删除关系)");
		try {
			String[] entityIdGrpArrs = ServletActionContext.getRequest().getParameterValues("entityIdGroup");
			List<String> entityIdGrpFilterList = new ArrayList<String>();
			if(entityIdGrpArrs != null && entityIdGrpArrs.length >0) {
				for (String str : entityIdGrpArrs) {
					if(str != null && !"".equals(str)) {
						entityIdGrpFilterList.add(str);
					}
				}
				
				if(entityIdGrpFilterList != null && !entityIdGrpFilterList.isEmpty()) {
					for (String entityIdGrp : entityIdGrpFilterList) {
						String[] entityTypeArrs = this.entityTypeGroup.split("-");
						String[] entityIdArrs = entityIdGrp.split("-");
						if(entityTypeArrs != null && entityTypeArrs.length == 2 
								&& entityIdArrs != null && entityIdArrs.length == 2) {
							//获取要被删除的两个对象
							ApplicationEntity leftAe = ApplicationEntity.changeFromEntity(physicalresService.getPhysicalresById(entityTypeArrs[0], Long.parseLong(entityIdArrs[0])));
							ApplicationEntity rightAe = ApplicationEntity.changeFromEntity(physicalresService.getPhysicalresById(entityTypeArrs[1], Long.parseLong(entityIdArrs[1])));
							//删除关系
							structureCommonService.delStrutureAssociation(leftAe, rightAe, AssociatedType.LINK, "networkresourcemanage");
							structureCommonService.delStrutureAssociation(rightAe,leftAe,AssociatedType.LINK, "networkresourcemanage");
						}
					}
				}
				
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/html");
				
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				String result = gson.toJson("success");
				try {
					log.info("退出deleteRelationForExtraAction()，返回结果result="+result); 
					response.getWriter().write(result);
				} catch (IOException e) {
					log.error("退出deleteRelationForExtraAction()，返回结果result="+result+"失败"); 
					e.printStackTrace();
				}
			}
		} catch (RuntimeException e) {
			log.error("退出deleteRelationForExtraAction()，发生RuntimeException"); 
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除跳纤或尾纤
	 */
	public void deletePigtailedFiberAction() {
		log.info("进入deletePigtailedFiberAction()，删除跳纤或尾纤");
		try {
			String[] entityIdGrpArrs = ServletActionContext.getRequest().getParameterValues("entityIdGroup");
			List<String> entityIdGrpFilterList = new ArrayList<String>();
			if(entityIdGrpArrs != null && entityIdGrpArrs.length >0) {
				for (String str : entityIdGrpArrs) {
					if(str != null && !"".equals(str)) {
						entityIdGrpFilterList.add(str);
					}
				}
				
				if(entityIdGrpFilterList != null && !entityIdGrpFilterList.isEmpty()) {
					for (String entityId : entityIdGrpFilterList) {
						//获取跳纤或尾纤app
						ApplicationEntity pigtailedFiberApp = ApplicationEntity.changeFromEntity(physicalresService.getPhysicalresById("PigtailedFiber", Long.parseLong(entityId)));
						structureCommonService.delEntityByRecursion(pigtailedFiberApp, "networkresourcemanage");
					}
				}
				
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/html");
				
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				String result = gson.toJson("success");
				try {
					log.info("退出deletePigtailedFiberAction()，返回结果result="+result); 
					response.getWriter().write(result);
				} catch (IOException e) {
					log.error("退出deletePigtailedFiberAction()，返回结果result="+result+"失败"); 
					e.printStackTrace();
				}
			}
		} catch (RuntimeException e) {
			log.error("退出deletePigtailedFiberAction()，发生RuntimeException异常"); 
			e.printStackTrace();
		}
	}
	
	/**
	 * 建立跳纤或尾纤
	 */
	public void savePigtailedFiberAction() {
		log.info("进入savePigtailedFiberAction()，建立跳纤或尾纤");
		//生成关系
		String[] typeArrs = ServletActionContext.getRequest().getParameterValues("entityType");
		String[] idArrs = ServletActionContext.getRequest().getParameterValues("entityId");
		if(typeArrs != null && typeArrs.length > 0 && idArrs != null && idArrs.length > 0 && typeArrs.length == idArrs.length) {
			List<ApplicationEntity> appList = new ArrayList<ApplicationEntity>();
			for(int i = 0; i < typeArrs.length; i++) {
				//把传过来的类型和id查出entity，并放进集合内
				appList.add(ApplicationEntity.changeFromEntity(physicalresService.getPhysicalresById(typeArrs[i], Long.parseLong(idArrs[i]))));
			}
			if(appList != null && !appList.isEmpty()) {
				for(int i = 0; i < appList.size(); i++) {
					if(i % 2 == 0) {
						//通过端子名的拼接，生成一个跳纤或尾纤
						ApplicationEntity pigtailedfiberApp = moduleLibrary.getModule("PigtailedFiber").createApplicationEntity();
						//pigtailedfiberApp.setValue("id", Unique.nextLong("pigtailedfiber_id"));
						pigtailedfiberApp.setValue("id", structureCommonService.getEntityPrimaryKey("PigtailedFiber"));
						pigtailedfiberApp.setValue("label", appList.get(i).getValue("name") + " - " + appList.get(i + 1).getValue("name"));
						/*用structure的接口存ae*/
						structureCommonService.saveInfoEntity(pigtailedfiberApp, "networkresourcemanage");
						//physicalresService.addPhysicalres(pigtailedfiberApp);
						
						//建立两个跳纤或尾纤与端子的link关系
						structureCommonService.createAssociatedRelation(appList.get(i),pigtailedfiberApp, AssociatedType.LINK, "networkresourcemanage");
						structureCommonService.createAssociatedRelation(appList.get(i+1),pigtailedfiberApp, AssociatedType.LINK, "networkresourcemanage");
					}
				}
			}
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson("success");
		try {
			log.info("退出savePigtailedFiberAction()，返回结果result="+result); 
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出savePigtailedFiberAction()，返回结果result="+result+"失败"); 
			e.printStackTrace();
		}
	}
	
	/**
	 * 建立纤芯成端、熔纤接续中，元素的关系
	 */
	public void saveExtraPhysicalresAction() {
		log.info("进入saveExtraPhysicalresAction， 建立纤芯成端、熔纤接续中，元素的关系");
		//生成关系
		String[] typeArrs = ServletActionContext.getRequest().getParameterValues("entityType");
		String[] idArrs = ServletActionContext.getRequest().getParameterValues("entityId");
		if(typeArrs != null && typeArrs.length > 0 && idArrs != null && idArrs.length > 0 && typeArrs.length == idArrs.length) {
			List<ApplicationEntity> appList = new ArrayList<ApplicationEntity>();
			for(int i = 0; i < typeArrs.length; i++) {
				//把传过来的类型和id查出entity，并放进集合内
				appList.add(ApplicationEntity.changeFromEntity(physicalresService.getPhysicalresById(typeArrs[i], Long.parseLong(idArrs[i]))));
			}
			if(appList != null && !appList.isEmpty()) {
				for(int i = 0; i < appList.size(); i++) {
					if(i % 2 == 0) {
						//建立两个entity的link关系
						structureCommonService.createAssociatedRelation(appList.get(i + 1), appList.get(i), AssociatedType.LINK, "networkresourcemanage");
					}
				}
			}
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String result = gson.toJson("success");
		try {
			log.info("退出saveExtraPhysicalresAction()，返回结果result="+result); 
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出saveExtraPhysicalresAction()，返回结果result="+result+"失败"); 
			e.printStackTrace();
		}
		/*ApplicationEntity app = ApplicationEntity.changeFromEntity(physicalresService.getPhysicalresById("FiberCore",24));
		ApplicationEntity[] arrs = structureCommonService.getStrutureSelationsApplicationEntity(app, AssociatedType.LINK, "networkresourcemanage");
		if(arrs != null && arrs.length > 0) {
			for (ApplicationEntity appE : arrs) {
				System.out.println(appE.getType());
				System.out.println(appE.getValue("id"));
			}
		}*/
	}
	
	/**
	 * 添加一个物理资源
	 * @return
	 */
	public String addPhysicalresAction() {
		log.info("进入addPhysicalresAction，添加一个物理资源");
		try {
			if(areaId!=null&&!"".equals(areaId)){
				BasicEntity areaEntity =physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
				if(areaEntity!=null){
					this.areaName = areaEntity.getValue("name");
				}
			}
			ApplicationEntity appEntity = ActionHelper.getApplicationEntity(this.addedResEntityType);
			//appEntity.setValue("id", Unique.nextLong(this.addedResEntityType.toLowerCase() + "_id"));
			if(this.addedResEntityType.indexOf("Area")>=0){
				appEntity.setValue("area_id", structureCommonService.getEntityPrimaryKey(this.addedResEntityType));
			}else{
				appEntity.setValue("id", structureCommonService.getEntityPrimaryKey(this.addedResEntityType));
			}
			/*用structure保存ae*/
			int saveInfoEntity = structureCommonService.saveInfoEntity(appEntity, "networkresourcemanage");
			//添加资源维护记录
			if(saveInfoEntity == 1){
				String resName = "";
				if(appEntity.getValue("name") != null){
					resName = appEntity.getValue("name");
				}else{
					resName = appEntity.getValue("label");
				}
				ResourceMaintenance maintenance = new ResourceMaintenance();
				maintenance.setBiz_module(OperationType.NETWORK);
				maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
				maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
				maintenance.setOp_category(OperationType.RESOURCEINSERT);
				maintenance.setRes_id((Long)appEntity.getValue("id"));
				maintenance.setRes_type(appEntity.getValue("_entityType").toString());
				maintenance.setRes_keyinfo(resName);
				networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
			}
			//physicalresService.addPhysicalres(appEntity);//添加物理资源
			
			//获取当前被添加的entity
			BasicEntity addedEntity = physicalresService.getPhysicalresById(this.addedResEntityType, Long.parseLong(appEntity.getValue("id").toString()));
			ApplicationEntity addedAppEntity = ApplicationEntity.changeFromEntity(addedEntity);
			
			//赋值给该两个变量，以供跳转的修改物理资源页面，获取这两个参数值
			this.currentEntityType = addedAppEntity.getType();
			this.currentEntityId = addedAppEntity.getValue("id").toString();
			
			//判断是被增加的物理资源，是增加在当前节点下，还是增加在其他父节点下
			if(this.newParentResEntityId != null && this.newParentResEntityType != null 
					&& !"".equals(this.newParentResEntityId) && !"".equals(this.newParentResEntityType)) {
				if(!this.oldParentResEntityId.equals(this.newParentResEntityId)) {
					//获取当前被更新对象的新的父对象(旧id与新id不一致，证明已经选择了新的父节点)
					BasicEntity newParentEntity = physicalresService.getPhysicalresById(this.newParentResEntityType, Long.parseLong(this.newParentResEntityId));
					ApplicationEntity newParentAppEntity = ApplicationEntity.changeFromEntity(newParentEntity);
					
					//建立新父对象与当前被更新对象的父子关系
					int createAssociatedRelation = structureCommonService.createAssociatedRelation(newParentAppEntity, addedAppEntity, AssociatedType.CLAN, "networkresourcemanage");
					if(createAssociatedRelation == 1){
						String resNameChinese = "";
						if(newParentResEntityType != null){
							try {
								List<BasicEntity> entry = null;
								entry = dictionary.getEntry(newParentResEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								resNameChinese = entry.get(0).getValue("display");
									//System.out.println(infoNameChinese);
							} catch (EntryOperationException e) {
								e.printStackTrace();
							}
						}
						ResourceMaintenance maintenance = new ResourceMaintenance();
						maintenance.setBiz_module(OperationType.NETWORK);
						maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
						maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
						String lresName = "";
						if(newParentAppEntity.getValue("name") != null){
							lresName = newParentAppEntity.getValue("name");
						}else{
							lresName = newParentAppEntity.getValue("label");
						}
						String rresName = "";
						if(addedAppEntity.getValue("name") != null){
							rresName = addedAppEntity.getValue("name");
						}else{
							rresName = addedAppEntity.getValue("label");
						}
						maintenance.setRes_keyinfo(rresName);
						String content = OperationType.INSERT+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
						//String content = OperationType.INSERT +"与" + lresName + OperationType.getAssociatedTypeChinese(associatedType.toString());
						maintenance.setOp_category(OperationType.INSERTLINK);
						maintenance.setContent(content);
						maintenance.setRes_id((Long)addedAppEntity.getValue("id"));
						maintenance.setRes_type(addedAppEntity.getValue("_entityType").toString());
						networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
					}
					
					this.parentEntityType = this.newParentResEntityType;
					this.parentEntityId = this.newParentResEntityId;
				} else {
					//获取当前被更新对象的旧的父对象(旧id与新id一致，证明还是选择旧的父节点)
					BasicEntity oldParentEntity = physicalresService.getPhysicalresById(this.oldParentResEntityType, Long.parseLong(this.oldParentResEntityId));
					ApplicationEntity oldParentAppEntity = ApplicationEntity.changeFromEntity(oldParentEntity);
					
					//建立旧父对象与当前被更新对象的父子关系
					int createAssociatedRelation = structureCommonService.createAssociatedRelation(oldParentAppEntity, addedAppEntity, AssociatedType.CLAN, "networkresourcemanage");
					if(createAssociatedRelation == 1){
						String resNameChinese= "";
						if(oldParentResEntityType != null){
							try {
								List<BasicEntity> entry = null;
									entry = dictionary.getEntry(oldParentResEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
									resNameChinese = entry.get(0).getValue("display");
										//System.out.println(infoNameChinese);
							} catch (EntryOperationException e) {
								e.printStackTrace();
							}
						}
						ResourceMaintenance maintenance = new ResourceMaintenance();
						maintenance.setBiz_module(OperationType.NETWORK);
						maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
						maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
						String lresName = "";
						if(oldParentAppEntity.getValue("name") != null){
							lresName = oldParentAppEntity.getValue("name");
						}else{
							lresName = oldParentAppEntity.getValue("label");
						}
						String rresName = "";
						if(addedAppEntity.getValue("name") != null){
							rresName = addedAppEntity.getValue("name");
						}else{
							rresName = addedAppEntity.getValue("label");
						}
						maintenance.setRes_keyinfo(rresName);
						String content = OperationType.INSERT+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
						//String content = OperationType.INSERT +"与" + lresName + OperationType.getAssociatedTypeChinese(associatedType.toString());
						maintenance.setOp_category(OperationType.INSERTLINK);
						maintenance.setContent(content);
						maintenance.setRes_id((Long)addedAppEntity.getValue("id"));
						maintenance.setRes_type(addedAppEntity.getValue("_entityType").toString());
						networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
					}
					this.parentEntityType = this.oldParentResEntityType;
					this.parentEntityId = this.oldParentResEntityId;
				}
			} else {
				//获取当前被更新对象的旧的父对象(新id为空，证明还是选择旧的父节点)
				BasicEntity oldParentEntity = physicalresService.getPhysicalresById(this.oldParentResEntityType, Long.parseLong(this.oldParentResEntityId));
				ApplicationEntity oldParentAppEntity = ApplicationEntity.changeFromEntity(oldParentEntity);
				this.parentEntityType = this.oldParentResEntityType;
				this.parentEntityId = this.oldParentResEntityId;
				//建立旧父对象与当前被更新对象的父子关系
				int createAssociatedRelation = structureCommonService.createAssociatedRelation(oldParentAppEntity, addedAppEntity, AssociatedType.CLAN, "networkresourcemanage");
				if(createAssociatedRelation == 1){
					String resNameChinese= "";
					if(oldParentResEntityType != null){
						try {
							List<BasicEntity> entry = null;
								entry = dictionary.getEntry(oldParentResEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								resNameChinese = entry.get(0).getValue("display");
									//System.out.println(infoNameChinese);
						} catch (EntryOperationException e) {
							e.printStackTrace();
						}
					}
					ResourceMaintenance maintenance = new ResourceMaintenance();
					maintenance.setBiz_module(OperationType.NETWORK);
					maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
					maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
					String lresName = "";
					if(oldParentAppEntity.getValue("name") != null){
						lresName = oldParentAppEntity.getValue("name");
					}else{
						lresName = oldParentAppEntity.getValue("label");
					}
					String rresName = "";
					if(addedAppEntity.getValue("name") != null){
						rresName = addedAppEntity.getValue("name");
					}else{
						rresName = addedAppEntity.getValue("label");
					}
					maintenance.setRes_keyinfo(rresName);
					String content = OperationType.INSERT+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
					//String content = OperationType.INSERT +"与" + lresName + OperationType.getAssociatedTypeChinese(associatedType.toString());
					maintenance.setOp_category(OperationType.INSERTLINK);
					maintenance.setContent(content);
					maintenance.setRes_id((Long)addedAppEntity.getValue("id"));
					maintenance.setRes_type(addedAppEntity.getValue("_entityType").toString());
					networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
				}
			}
			log.info("退出addPhysicalresAction()，返回结果result=success"); 
			return "success";
		} catch (NumberFormatException e) {
			log.error("退出addPhysicalresAction()，发生NumberFormatException异常，返回结果result=error"); 
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * AJAX添加一个物理资源
	 */
	public void addPhysicalresForAjaxAction() {
		log.info("进入addPhysicalresForAjaxAction()，AJAX添加一个物理资源");
		try {
			ApplicationEntity appEntity = ActionHelper.getApplicationEntity(this.addedResEntityType);
			//System.out.println(appEntity.toMap());
			//appEntity.setValue("id", Unique.nextLong(this.addedResEntityType.toLowerCase() + "_id"));
			if(this.addedResEntityType.indexOf("Area")>=0){
				appEntity.setValue("area_id",structureCommonService.getEntityPrimaryKey(this.addedResEntityType));
			}else{
				appEntity.setValue("id",structureCommonService.getEntityPrimaryKey(this.addedResEntityType));
			}
			
			/*用structure保存ae*/
			//System.out.println(appEntity.toMap()+"test+++++++++++++++++++++");
			int resultCount = structureCommonService.saveInfoEntity(appEntity, "networkresourcemanage");
			//添加资源维护记录
			if(resultCount == 1){
				String resName = "";
				if(appEntity.getValue("name") != null){
					resName = appEntity.getValue("name");
				}else{
					resName = appEntity.getValue("label");
				}
				ResourceMaintenance maintenance = new ResourceMaintenance();
				maintenance.setBiz_module(OperationType.NETWORK);
				maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
				maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
				maintenance.setOp_category(OperationType.RESOURCEINSERT);
				maintenance.setRes_id((Long)appEntity.getValue("id"));
				maintenance.setRes_type(appEntity.getValue("_entityType").toString());
				maintenance.setRes_keyinfo(resName);
				networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
			}
			//int resultCount = physicalresService.addPhysicalres(appEntity);//添加物理资源
			//System.out.println("test+-----------------------");
			//获取当前被添加的entity
			BasicEntity addedEntity = physicalresService.getPhysicalresById(this.addedResEntityType, Long.parseLong(appEntity.getValue("id").toString()));
			ApplicationEntity addedAppEntity = ApplicationEntity.changeFromEntity(addedEntity);
			
			//获取当前被添加的entity的父entity
			BasicEntity addedParentEntity = physicalresService.getPhysicalresById(this.addedResParentEntityType, Long.parseLong(this.addedResParentEntityId));
			ApplicationEntity addedParentAppEntity = ApplicationEntity.changeFromEntity(addedParentEntity);
			
			//赋值给该两个变量，以供跳转的修改物理资源页面，获取这两个参数值
			this.currentEntityType = addedAppEntity.getType();
			this.currentEntityId = addedAppEntity.getValue("id").toString();
			
			//建立新资源与其父entity的父子关系
			int createAssociatedRelation = structureCommonService.createAssociatedRelation(addedParentAppEntity, addedAppEntity, AssociatedType.CLAN, "networkresourcemanage");
			if(createAssociatedRelation == 1){
				String resNameChinese= "";
				if(addedParentEntity != null){
					try {
						List<BasicEntity> entry = null;
							entry = dictionary.getEntry(addedParentEntity + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							resNameChinese = entry.get(0).getValue("display");
								//System.out.println(infoNameChinese);
					} catch (EntryOperationException e) {
						e.printStackTrace();
					}
				}
				ResourceMaintenance maintenance = new ResourceMaintenance();
				maintenance.setBiz_module(OperationType.NETWORK);
				maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
				maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
				String lresName = "";
				if(addedParentAppEntity.getValue("name") != null){
					lresName = addedParentAppEntity.getValue("name");
				}else{
					lresName = addedParentAppEntity.getValue("label");
				}
				String rresName = "";
				if(addedAppEntity.getValue("name") != null){
					rresName = addedAppEntity.getValue("name");
				}else{
					rresName = addedAppEntity.getValue("label");
				}
				maintenance.setRes_keyinfo(rresName);
				String content = OperationType.INSERT+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
				//String content = OperationType.INSERT +"与" + lresName + OperationType.getAssociatedTypeChinese(associatedType.toString());
				maintenance.setOp_category(OperationType.INSERTLINK);
				maintenance.setContent(content);
				maintenance.setRes_id((Long)addedAppEntity.getValue("id"));
				maintenance.setRes_type(addedAppEntity.getValue("_entityType").toString());
				networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
			}
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = null;
			if (resultCount > 0) {
				result = gson.toJson(appEntity.getValue("id")+"");
			} else {
				result = gson.toJson("error");
			}
			try {
				log.info("退出addPhysicalresForAjaxAction()，返回结果result="+result);
				response.getWriter().write(result);
			} catch (IOException e) {
				log.error("退出addPhysicalresForAjaxAction()，返回结果result="+result+"失败");
				e.printStackTrace();
			}
			
		} catch (NumberFormatException e) {
			log.error("退出addPhysicalresForAjaxAction()，发生NumberFormatException异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新一个物理资源
	 * @return
	 */
	public void updatePhysicalresAction() {
		log.info("进入updatePhysicalresAction()，更新一个物理资源");
		try {
			//更新对象信息
			ApplicationEntity appEntity = ActionHelper.getApplicationEntity(this.updatedEntityType);
			ApplicationModule module = ModuleProvider.getModule(this.updatedEntityType);
			String uId = appEntity.getValue("id").toString();
			ApplicationEntity sectionEntity = structureCommonService.getSectionEntity(this.updatedEntityType, uId);
			//int updateResult = physicalresService.updatePhysicalres(appEntity, this.updatedEntityType);
			int updateResult =structureCommonService.updateApplicationEntityBySql(appEntity, module);
			if(updateResult>0){
				ApplicationEntity upddateAe = structureCommonService.getSectionEntity(this.updatedEntityType, uId);
					//updateApp
					
					String updateValue = getUpdateValue(sectionEntity,upddateAe);
					
					if(updateValue!=null && !"".equals(updateValue)){
						String resName = "";
						if(upddateAe.getValue("name") != null){
							resName = upddateAe.getValue("name");
						}else{
							resName = upddateAe.getValue("label");
						}
						ResourceMaintenance maintenance = new ResourceMaintenance();
						maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
						maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
						maintenance.setOp_category(OperationType.RESOURCEUPDATE);
						maintenance.setRes_id((Long)upddateAe.getValue("id"));
						maintenance.setRes_type(upddateAe.getValue("_entityType").toString());
						maintenance.setRes_keyinfo(resName);
						maintenance.setContent(updateValue);
						maintenance.setBiz_module(OperationType.NETWORK);
						int forces = networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
						if(forces > 0){
							if(this.bizModule != null && !this.bizModule.equals("")){
								ServiceMaintenance serviceMaintenance = new ServiceMaintenance();
								serviceMaintenance.setBiz_processcode(this.bizProcessCode);
								serviceMaintenance.setMaintenance_id(forces);
								if(this.bizProcessId != null && !this.bizProcessId.equals("")){
									long parseLong = Long.parseLong(bizProcessId);
									serviceMaintenance.setBiz_processId(parseLong);
								}
								if(this.bizModule != null && !this.bizModule.equals("")){
									serviceMaintenance.setBiz_module(this.bizModule);
								}
								networkResourceMaintenanceService.insertServiceMaintenance(serviceMaintenance);
							}
						}
				
					}
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = "";
			if(updateResult < 1) {
				result = gson.toJson("error");
				try {
					log.info("退出updatePhysicalresAction()，更新失败，返回结果result="+result); 
					response.getWriter().write(result);
				} catch (IOException e) {
					log.error("退出updatePhysicalresAction()，更新失败,返回结果result="+result+"失败"); 
					e.printStackTrace();
				}
				return;
			}

			if(this.newParentResEntityId != null && this.newParentResEntityType != null) {
				if(!"".equals(this.newParentResEntityId) && !"".equals(this.newParentResEntityType)) {
					if(!this.oldParentResEntityId.equals(this.newParentResEntityId)) {
						//获取当前被更新的对象
						BasicEntity operatedEntity = physicalresService.getPhysicalresById(this.operatedCurrentEntityType, Long.parseLong(this.operatedCurrentEntityId));
						ApplicationEntity operatedAppEntity = ApplicationEntity.changeFromEntity(operatedEntity);
						
						//获取当前被更新对象的旧的父对象
						//获取当前被更新对象的旧的父对象
						if(this.oldParentResEntityType!=null&&!"".equals(this.oldParentResEntityType)&&this.oldParentResEntityId!=null&&!"".equals(this.oldParentResEntityId)){
							BasicEntity oldParentEntity = physicalresService.getPhysicalresById(this.oldParentResEntityType, Long.parseLong(this.oldParentResEntityId));
							ApplicationEntity oldParentAppEntity = ApplicationEntity.changeFromEntity(oldParentEntity);
							//删除当前被更新对象的所有关系
							int delStrutureAssociation = structureCommonService.delStrutureAssociation(oldParentAppEntity, operatedAppEntity, AssociatedType.CHILD, "networkresourcemanage");
							//资源维护记录
							if(delStrutureAssociation == 1){
								String resNameChinese= "";
								if(oldParentResEntityType != null){
									try {
										List<BasicEntity> entry = null;
											entry = dictionary.getEntry(oldParentResEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
											resNameChinese = entry.get(0).getValue("display");
												//System.out.println(infoNameChinese);
									} catch (EntryOperationException e) {
										e.printStackTrace();
									}
								}
								ResourceMaintenance maintenance = new ResourceMaintenance();
								maintenance.setBiz_module(OperationType.NETWORK);
								maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
								maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
								String lresName = "";
								if(oldParentAppEntity.getValue("name") != null){
									lresName = oldParentAppEntity.getValue("name");
								}else{
									lresName = oldParentAppEntity.getValue("label");
								}
								String rresName = "";
								if(operatedAppEntity.getValue("name") != null){
									rresName = operatedAppEntity.getValue("name");
								}else{
									rresName = operatedAppEntity.getValue("label");
								}
								maintenance.setRes_keyinfo(rresName);
								String content = OperationType.DELETE+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
								//String content = OperationType.INSERT +"与" + lresName + OperationType.getAssociatedTypeChinese(associatedType.toString());
								maintenance.setOp_category(OperationType.DELETELINK);
								maintenance.setContent(content);
								maintenance.setRes_id((Long)operatedAppEntity.getValue("id"));
								maintenance.setRes_type(operatedAppEntity.getValue("_entityType").toString());
								networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
							}
						}
						//获取当前被更新对象的新的父对象
						BasicEntity newParentEntity = physicalresService.getPhysicalresById(this.newParentResEntityType, Long.parseLong(this.newParentResEntityId));
						ApplicationEntity newParentAppEntity = ApplicationEntity.changeFromEntity(newParentEntity);
						
						
						//建立新父对象与当前被更新对象的父子关系
						int createAssociatedRelation = structureCommonService.createAssociatedRelation(newParentAppEntity, operatedAppEntity, AssociatedType.CLAN, "networkresourcemanage");
						//资源维护记录
						if(createAssociatedRelation == 1){
							String resNameChinese= "";
							if(newParentResEntityType != null){
								try {
									List<BasicEntity> entry = null;
										entry = dictionary.getEntry(newParentResEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
										resNameChinese = entry.get(0).getValue("display");
											//System.out.println(infoNameChinese);
								} catch (EntryOperationException e) {
									e.printStackTrace();
								}
							}
							ResourceMaintenance maintenance = new ResourceMaintenance();
							maintenance.setBiz_module(OperationType.NETWORK);
							maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
							maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
							String lresName = "";
							if(newParentAppEntity.getValue("name") != null){
								lresName = newParentAppEntity.getValue("name");
							}else{
								lresName = newParentAppEntity.getValue("label");
							}
							String rresName = "";
							if(operatedAppEntity.getValue("name") != null){
								rresName = operatedAppEntity.getValue("name");
							}else{
								rresName = operatedAppEntity.getValue("label");
							}
							maintenance.setRes_keyinfo(rresName);
							String content = OperationType.INSERT+OperationType.getAssociatedTypeChinese(AssociatedType.LINK.toString())+"到 "+resNameChinese +" ：" + lresName;
							//String content = OperationType.INSERT +"与" + lresName + OperationType.getAssociatedTypeChinese(associatedType.toString());
							maintenance.setOp_category(OperationType.INSERTLINK);
							maintenance.setContent(content);
							maintenance.setRes_id((Long)operatedAppEntity.getValue("id"));
							maintenance.setRes_type(operatedAppEntity.getValue("_entityType").toString());
							networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
						}
					}
				}
			}
			
			result = gson.toJson("success");
			try {
				log.info("退出updatePhysicalresAction()，返回结果result="+result); 
				response.getWriter().write(result);
			} catch (IOException e) {
				log.error("退出updatePhysicalresAction()，返回结果result="+result+"失败"); 
				e.printStackTrace();
			}
			
		} catch (RuntimeException e) {
			log.error("退出updatePhysicalresAction()，发生RuntimeException异常"); 
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除物理资源(同时删除与其有关系的下级资源)
	 */
	public void delPhysicalresByRecursionAction() {
		log.info("进入delPhysicalresByRecursionAction()，删除物理资源(同时删除与其有关系的下级资源)");
		try {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result=gson.toJson("error");;
			long chooseResEntityId = Long.parseLong(this.chooseResEntityId);
			BasicEntity chooseResEntity = physicalresService.getPhysicalresById(this.chooseResEntityType, chooseResEntityId);
			if(chooseResEntity != null) {
				ApplicationEntity chooseResAppEntity = ApplicationEntity.changeFromEntity(chooseResEntity);
				int delEntityByRecursion = structureCommonService.delEntityByRecursion(chooseResAppEntity, "networkresourcemanage");
				
				if(delEntityByRecursion == 1){
					String resNameChinese = "";
					if(chooseResEntityType != null){
						try {
							List<BasicEntity> entry = null;
								entry = dictionary.getEntry(chooseResEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								resNameChinese = entry.get(0).getValue("display");
									//System.out.println(infoNameChinese);
						} catch (EntryOperationException e) {
							e.printStackTrace();
						}
					}
					String resName = "";
					if(chooseResAppEntity.getValue("name") != null){
						resName = chooseResAppEntity.getValue("name");
					}else{
						resName = chooseResAppEntity.getValue("label");
					}
					ResourceMaintenance maintenance = new ResourceMaintenance();
					maintenance.setBiz_module(OperationType.NETWORK);
					maintenance.setOp_cause(OperationType.RESOURCEEDITORMAINTENANCE);
					maintenance.setOp_scene(OperationType.NETWORKRESOURCEMANAGE);
					maintenance.setOp_category(OperationType.RESOURCEDELETE);
					maintenance.setRes_id((Long)chooseResAppEntity.getValue("id"));
					maintenance.setRes_type(chooseResAppEntity.getValue("_entityType").toString());
					maintenance.setRes_keyinfo("资源被删除前的关键信息<"+resNameChinese+">："+resName);
					networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
					result = gson.toJson("success");
				}
			} 
			//文件已被删除的情况
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			try {
				log.info("退出delPhysicalresByRecursionAction()，返回结果result="+result);
				response.getWriter().write(result);
			} catch (IOException e) {
				log.error("退出delPhysicalresByRecursionAction()，返回结果result="+result+"失败");
				e.printStackTrace();
			}
		} catch (NumberFormatException e) {
			log.error("退出delPhysicalresByRecursionAction()，发生NumberFormatException异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建当前图片对象并与其父级entity建立关系
	 */
	public void createPhotoAssociatedRelationAction() {
		log.info("进入createPhotoAssociatedRelationAction()，创建当前图片对象并与其父级entity建立关系");
		try {
			//获取图片的父级entity
			BasicEntity imgParentEntity = physicalresService.getPhysicalresById(this.photoParentEntityType, Long.parseLong(this.photoParentEntityId));
			ApplicationEntity photoParentAppEntity = ApplicationEntity.changeFromEntity(imgParentEntity);
			
			ApplicationEntity photoAppEntity = moduleLibrary.getModule("Photo").createApplicationEntity();
			//photoAppEntity.setValue("id", Unique.nextLong("photo_id"));
			photoAppEntity.setValue("id", structureCommonService.getEntityPrimaryKey("Photo"));
			photoAppEntity.setValue("name", this.photoOldName);
			photoAppEntity.setValue("uuidname", this.photoName);
			/*用structure保存ae*/
			structureCommonService.saveInfoEntity(photoAppEntity, "networkresourcemanage");
			//physicalresService.addPhysicalres(photoAppEntity);
			
			structureCommonService.createAssociatedRelation(photoParentAppEntity, photoAppEntity, AssociatedType.CLAN, "networkresourcemanage");
			log.info("退出createPhotoAssociatedRelationAction()，创建当前图片对象并与其父级entity建立关系成功");
		} catch (NumberFormatException e) {
			log.error("退出createPhotoAssociatedRelationAction()，发生NumberFormatException异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改图片，新图片与其父级entity建立关系
	 */
	public void updatePhotoAssociatedRelationAction() {
		log.info("进入updatePhotoAssociatedRelationAction()，修改图片，新图片与其父级entity建立关系");
		try {
			//旧图片的enitty
			BasicEntity oldPhotoEntity = physicalresService.getPhysicalresById(this.currentPhotoEntityType, Long.parseLong(this.currentPhotoEntityId));
			ApplicationEntity oldPhotoAppEntity = ApplicationEntity.changeFromEntity(oldPhotoEntity);
			
			oldPhotoAppEntity.setValue("name", this.photoOldName);//旧图片名
			oldPhotoAppEntity.setValue("uuidname", this.photoName);//新图片uuid名
			
			physicalresService.updatePhysicalres(oldPhotoAppEntity, this.currentPhotoEntityType);
			
			//获取旧图片的父级entity
			/*ApplicationEntity[] photoParentAppEntityArrs = structureCommonService
					.getStrutureSelationsApplicationEntity(oldPhotoAppEntity, AssociatedType.PARENT, "networkresourcemanage");
			
			if(photoParentAppEntityArrs != null && photoParentAppEntityArrs.length >0) {
				ApplicationEntity photoParentAppEntity = photoParentAppEntityArrs[0];
				
				//创建新图片entity
				ApplicationEntity newPhotoAppEntity = moduleLibrary.getModule("Photo").createApplicationEntity();
				newPhotoAppEntity.setValue("id", Unique.nextLong("photo_id"));
				newPhotoAppEntity.setValue("name", this.photoOldName);
				newPhotoAppEntity.setValue("uuidname", this.photoName);
				
				//用structure保存ae
				structureCommonService.saveInfoEntity(newPhotoAppEntity, "networkresourcemanage");
				//physicalresService.addPhysicalres(newPhotoAppEntity);
				
				//删除旧图片entity
				structureCommonService.delEntityByRecursion(oldPhotoAppEntity, "networkresourcemanage");
				//新图片entity与其父级entity建立父子关系
				structureCommonService.createAssociatedRelation(photoParentAppEntity, newPhotoAppEntity, AssociatedType.CLAN, "networkresourcemanage");
			}*/
			log.info("退出updatePhotoAssociatedRelationAction()，修改图片，新图片与其父级entity建立关系成功");
		} catch (RuntimeException e) {
			log.error("退出updatePhotoAssociatedRelationAction()，发生NumberFormatException异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * 加载资源导入页面
	 * @return
	 */
	public String loadResourceImportPageAction() {
		log.info("进入loadResourceImportPageAction()，加载资源导入页面");
		try {
			if(areaId!=null&&!"".equals(areaId)){
				BasicEntity areaEntity =physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
				if(areaEntity!=null){
					this.areaName = areaEntity.getValue("name");
				}
			}
		/*	List<String> allTypeList = structureCommonService.getAllEntityTypes("networkresourcemanage");
			this.allTypeMapList = new ArrayList<Map<String,Object>>();
			for (String type : allTypeList) {
				List<BasicEntity> entry = null;
				Map<String, Object> typeMap = new HashMap<String, Object>();
				try {
					entry = dictionary.getEntry(type + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					if(entry != null && !entry.isEmpty()) {
						typeMap.put("type", type);
						//typeMap.put("chineseType", entry.get(0).getValue("display"));
						String display = entry.get(0).getValue("display").toString();
						String chinese= GetChineseFirstLetter.getFirstLetter(display);
				        typeMap.put("chineseType", chinese+":"+display);
						this.allTypeMapList.add(typeMap);
					}
				} catch (EntryOperationException e) {
					e.printStackTrace();
					if(entry != null && !entry.isEmpty()) {
						typeMap.put("type", type);
						typeMap.put("chineseType", type);
						this.allTypeMapList.add(typeMap);
					}
				}
			}
			if(allTypeMapList!=null){
				quickSort.sort(allTypeMapList, "chineseType");
			}*/
			log.info("退出loadResourceImportPageAction()，返回结果result=success");
			return "success";
		} catch (RuntimeException e) {
			log.error("退出loadResourceImportPageAction()，发生RuntimeException异常，返回结果result=error");
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 加载资源查询页面
	 * @return
	 */
	public String loadSearchResourcePageAction() {
		//调用资源导入页面加载方法，只是控制跳转到不同页面
		if(areaId!=null&&!"".equals(areaId)){
			BasicEntity areaEntity =physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
			if(areaEntity!=null){
				this.areaName = areaEntity.getValue("name");
			}
		}
		return loadResourceImportPageAction();
	}
	
	/**
	 * 获取查询资源的属性
	 * @return
	 */
	public String loadSearchResourceAttrPageAction(){
		log.info("进入loadSearchResourceAttrPageAction()， 获取查询资源的属性");
		try {
			ApplicationModule module = ModuleProvider.getModule(this.selectResType);
			this.searchMap = module.toMap();
			this.currentEntityChineseMap = new HashMap<String, Object>();
			//生成下拉框的map
			this.dropdownListMap = new HashMap<String, Object>();
			this.attrTypeMap = new HashMap<String,Object>();
			Map<String,Object> orderIdMap =new HashMap<String,Object>();//entity属性排序
			
			for(String key : this.searchMap.keySet()) {
				try {
					List<BasicEntity> entry = null;
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						//数据字典，中英文转换
						entry = dictionary.getEntry(key + "," + this.selectResType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					}
					if(entry != null && !entry.isEmpty()) {
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							this.currentEntityChineseMap.put(key, entry.get(0).getValue("display"));
						//	System.out.println(entry.get(0).getValue("orderID").toString()+"--");
							orderIdMap.put(entry.get(0).getValue("orderID").toString(),key);
						}
					}
					this.attrTypeMap.put(key,module.getAttribute(key).getValue("type"));
					//根据数据字典中，属性为entity类型或者general类型，获取属性对应的下拉框的值，提供前台进行下拉框选择操作
					//拿出来的值，例如:其他-其他,置换-置换,局用-局用
					if("entity".equals(entry.get(0).getValue("dictionaryType"))) {
						//entity类型下拉框数据组装
						List<BasicEntity> dropDownEntry = dictionary.getEntry(key + "," + this.selectResType + ",networkResourceValueEmun", SearchScope.OBJECT, "");
						if(dropDownEntry != null && !dropDownEntry.isEmpty()) {
							String dropdownMixVal = dropDownEntry.get(0).getValue("mixValue");
							//先用逗号进行分隔
							String[] firstSplitArrs = dropdownMixVal.split(",");
							if(firstSplitArrs != null && firstSplitArrs.length > 0) {
								List<String> attrDropdownList = new ArrayList<String>();
								for(String secondVal : firstSplitArrs) {
									//再通过"-"获取key，value，获取value值
									String[] secondSplitArrs = secondVal.split(":");
									if(secondSplitArrs != null && secondSplitArrs.length == 2) {
										attrDropdownList.add(secondSplitArrs[1]);
										
									}
								}
								this.dropdownListMap.put(key, attrDropdownList);
							}
						}
						
					} else if("general".equals(entry.get(0).getValue("dictionaryType"))) {
						String generalEntryName = entry.get(0).getValue("generalEntryName");
						//general类型下拉框数据组装
						List<BasicEntity> dropDownEntry = dictionary.getEntry(generalEntryName + ",networkResourceValueEmun", SearchScope.OBJECT, "");
						if(dropDownEntry != null && !dropDownEntry.isEmpty()) {
							String dropdownMixVal = dropDownEntry.get(0).getValue("mixValue");
							//先用逗号进行分隔
							String[] firstSplitArrs = dropdownMixVal.split(",");
							if(firstSplitArrs != null && firstSplitArrs.length > 0) {
								List<String> attrDropdownList = new ArrayList<String>();
								for(String secondVal : firstSplitArrs) {
									//再通过"-"获取key，value，获取value值
									String[] secondSplitArrs = secondVal.split(":");
									if(secondSplitArrs != null && secondSplitArrs.length == 2) {
										attrDropdownList.add(secondSplitArrs[1]);
										
									}
								}
								this.dropdownListMap.put(key, attrDropdownList);
							}
						}
					}
				} catch (EntryOperationException e) {
					log.error("获取"+selectResType+"属性字段"+key+"的中文字典失败，可能该字典不存在。");
					e.printStackTrace();
				}
				
			}
			//addedResMap排序
			Map<String,Object> sortedMap = quickSort.sortMap(this.searchMap, orderIdMap);
			if(sortedMap!=null){
				this.searchMap = sortedMap;
				//System.out.println(this.addedResMap+"*****");
			}
			//忽略掉id项
			this.searchMap.remove("id");
			log.info("退出loadSearchResourceAttrPageAction()，返回结果result=success");
			return "success";
		} catch (RuntimeException e) {
			log.error("退出loadSearchResourceAttrPageAction()，发生RuntimeException异常，返回结果result=error");
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 根据选择的类型，获取关联的类型
	 */
	public void getAssTypeByChosenTypeAction() {
		log.info("进入getAssTypeByChosenTypeAction()，根据选择的类型，获取关联的类型");
		//获取父级类型
		String[] assParentType = structureCommonService.getAssociatedAetName(this.chosenType, AssociatedType.PARENT, "networkresourcemanage");
		//获取link类型
		String[] assLinkType = structureCommonService.getAssociatedAetName(this.chosenType, AssociatedType.LINK, "networkresourcemanage");
		
		try {
			this.assTypeMapList = new ArrayList<Map<String,Object>>();
			//关联类型的中英文获取和关联类型保存
			for (String type : assParentType) {
				List<BasicEntity> entry = null;
				entry = dictionary.getEntry(type + ",networkResourceDefination" ,SearchScope.OBJECT, "");
				if(entry != null && !entry.isEmpty()) {
					Map<String, Object> typeMap = new HashMap<String, Object>();
					typeMap.put("type", type);
					typeMap.put("chineseType", entry.get(0).getValue("display"));
					typeMap.put("assType", "parent"); //关联类型
					this.assTypeMapList.add(typeMap);
				}
			}
			for (String type : assLinkType) {
				List<BasicEntity> entry = null;
				entry = dictionary.getEntry(type + ",networkResourceDefination" ,SearchScope.OBJECT, "");
				if(entry != null && !entry.isEmpty()) {
					Map<String, Object> typeMap = new HashMap<String, Object>();
					typeMap.put("type", type);
					typeMap.put("chineseType", entry.get(0).getValue("display"));
					typeMap.put("assType", "link"); //关联类型
					this.assTypeMapList.add(typeMap);
				}
			}
			if(assTypeMapList!=null){
				quickSort.sort(assTypeMapList, "chineseType");
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(this.assTypeMapList);
			try {
				log.info("退出getAssTypeByChosenTypeAction()，返回结果result="+result);
				response.getWriter().write(result);
			} catch (IOException e) {
				log.error("退出getAssTypeByChosenTypeAction()，返回结果result="+result+"失败");
				e.printStackTrace();
			}
		} catch (Exception e) {
			log.error("退出getAssTypeByChosenTypeAction()，发生Exception异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据所选择的类型，获取该类型的字段信息
	 */
	public void getAttrByChosenTypeAction() {
		log.info("进入getAttrByChosenTypeAction()，根据所选择的类型，获取该类型的字段信息");
		String result="";
		try {
			ApplicationModule module = ModuleProvider.getModule(this.chosenType);
			Map<String, Object> attrMap = module.toMap();
			
			//System.out.println(attrMap+"*****************");
			//List<String> resultList = new ArrayList<String>();
			Map<String,Object> typeMap = new HashMap<String,Object>();
			Map<String,Object> orderIdMap = new HashMap<String,Object>();
			try {
				for(String key : attrMap.keySet()) {
					if("import".equals(this.showType)){
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
										typeMap.put(key , entry.get(0).getValue("display"));
										orderIdMap.put(entry.get(0).getValue("orderID").toString(),key);
									}
									
								} else {
									typeMap.put(key , key);
									orderIdMap.put(entry.get(0).getValue("orderID").toString(),key);
								}	
							}		
						}
						
					}else{
						List<BasicEntity> entry = null;
						if("selfExport".equals(this.showType)){
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								//数据字典，中英文转换
								if(dictionary.hasEntry(key + "," + this.chosenType + ",networkResourceDefination")){
									entry = dictionary.getEntry(key + "," + this.chosenType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								}
							}
						}else{
							if(!"_entityType".equals(key) && !"_entityId".equals(key) && !"id".equals(key)) {
								//数据字典，中英文转换
								if(dictionary.hasEntry(key + "," + this.chosenType + ",networkResourceDefination")){
									entry = dictionary.getEntry(key + "," + this.chosenType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								}
							}
						}
						
						
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								if(entry.get(0).getValue("display") != null) {
									typeMap.put(key , entry.get(0).getValue("display"));
									orderIdMap.put(entry.get(0).getValue("orderID").toString(),key);									
								}
							} else {
								typeMap.put(key ,key);
								orderIdMap.put(entry.get(0).getValue("orderID").toString(),key);
							}	
						}	
						
					}
						
				}
			} catch (EntryOperationException e) {
				log.error("获取中文字典失败，可能该字典不存在。");
				e.printStackTrace();
			}
			if(orderIdMap!=null && !orderIdMap.isEmpty() && typeMap!=null && !typeMap.isEmpty()){
				this.attrList = new ArrayList<String>();
				Map<String,Object> map=quickSort.sortMap(typeMap, orderIdMap);
				if(map!=null && !map.isEmpty()){
					typeMap=map;
				}
				
				if("addResouceDirectly".equals(this.showType)){//直接录入资源所需
					Map<String,Object> nullMap = new HashMap<String,Object>(); 
					Map<String,Object> typesMap = new HashMap<String,Object>(); 
					for(String key:typeMap.keySet()){
						this.attrList.add(key+"_"+typeMap.get(key));//排序后的结果
						nullMap.put(key,module.getAttribute(key).getValue("nullable")+"");//是否可空
						typesMap.put(key,module.getAttribute(key).getValue("type")+"");//属性类型
					}	
					Map<String,Object> resultMap = new HashMap<String,Object>();
					resultMap.put("attrList",this.attrList);
					resultMap.put("nullMap",nullMap);
					resultMap.put("typesMap",typesMap);
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					result = gson.toJson(resultMap);
				}else{
					for(String key:typeMap.keySet()){
						this.attrList.add(key+"_"+typeMap.get(key));//排序后的结果
					}
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					result = gson.toJson(this.attrList);
				}
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			
			try {
				log.info("退出getAttrByChosenTypeAction()，返回结果result="+result); 
				response.getWriter().write(result);
			} catch (IOException e) {
				log.error("退出getAttrByChosenTypeAction()，返回结果result="+result+"失败"); 
				e.printStackTrace();
			}
		} catch (RuntimeException e) {
			log.error("退出getAttrByChosenTypeAction()，发生RuntimeException异常"); 
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description: 查询资源
	 * @author：     
	 * @return void     
	 * @date：Feb 5, 2013 4:21:36 PM
	 */
	public void searchResourceAction(){
		log.info("进入searchResourceAction(),查询资源");
		//查询资源集合
		this.searchResourceMapList = new ArrayList<Map<String,Object>>();
		Map<String, Object> searchedResModuleMap = ModuleProvider.getModule(this.selectResType).toMap();
		Map<String,Object> orderIdMap = new HashMap<String,Object>();
		ApplicationEntity areaAE = null;
		//String areaName="";
		if(this.areaId!=null && !"".equals(this.areaId)){
			BasicEntity be = physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
			areaAE = ApplicationEntity.changeFromEntity(be);
			//areaName = areaAE.getValue("name");
		}
		if(searchedResModuleMap != null) {
			//获取中文标题(不能正常获取的继续显示英文)(这里用linkedHashMap，使其与模板顺序一致，确保跟内容相对应)
			Map<String, Object> titleMap = new LinkedHashMap<String, Object>();
			for(String key : searchedResModuleMap.keySet()) {
				try {
					List<BasicEntity> entry = null;
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						//数据字典，中英文转换
						entry = dictionary.getEntry(key + "," + this.selectResType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					}
					if(entry != null && !entry.isEmpty()) {
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							titleMap.put(key, entry.get(0).getValue("display"));
							orderIdMap.put( entry.get(0).getValue("orderID").toString(), key);
						}
					}
				} catch (EntryOperationException e) {
					log.error("获取"+this.selectResType+"属性字段"+key+"的中文字典失败，可能该字典不存在");
					e.printStackTrace();
					titleMap.put(key, key);
				}
			}
			Map<String,Object> sortedMap = quickSort.sortMap(titleMap,orderIdMap);//中文字典排序
			if(sortedMap!=null){
				titleMap = sortedMap;
			}
			ApplicationEntity searchedAe = ActionHelper.getApplicationEntity(this.selectResType);
			Map<String,String> dateMap = null;
			if(this.dateString!=null&&!"".equals(this.dateString)){//属性类型为日期特殊处理 
				dateMap = new HashMap<String,String>();
				String[] dates=this.dateString.split(",");
				for(String s:dates){
					String key=s.substring(0,s.indexOf(":"));
					String date=s.substring(s.indexOf(":")+1,s.length());
					dateMap.put(key,date);
				}
			}
			//获取条件筛选后的被查询的资源信息
			String queryType="";
			List<BasicEntity> searchResourceBeList=null;
			//System.out.println(this.hasCondition);
			List<ApplicationEntity> searchResourceAeList = new ArrayList<ApplicationEntity>();//查询结果list
			String ids ="";//主键id字符串
			
			if("hasCondition".equals(this.hasCondition)){
				int conditionFlag=0;
				for(String key : searchedAe.keyset()) {
					if (searchedAe.getValue(key) != null && !"".equals(searchedAe.getValue(key))) {
						if("_entityId".equals(key) || "_entityType".equals(key)) {
							continue;
						} else {
							conditionFlag=conditionFlag+1;
						}
					}
				}

				//当属性为日期类型时特殊处理
				if(dateMap!=null&&!dateMap.isEmpty()){
					for(String k:dateMap.keySet()){
						String dates= dateMap.get(k).toString();
						String date1= dates.substring(0,dates.indexOf("/"));
						String date2 = dates.substring(dates.indexOf("/")+1,dates.length());
						if((date1!=null && !"".equals(date1)) || (date2!=null && !"".equals(date2)) ){
							conditionFlag=conditionFlag+1;
						}
					}	
				}
				if(conditionFlag<=0){
					queryType="all";
				}
			}else{
				queryType="all";//没有查询条件资源 
			}
			List<ApplicationEntity> filterResList = new ArrayList<ApplicationEntity>();
			ApplicationEntity[] assLinkAeList=null;
			if(this.assRangeEntityId != null && this.assRangeEntityType != null 
					&& !"".equals(this.assRangeEntityId) && !"".equals(this.assRangeEntityType)){
				if(this.selectRangeResChosenType != null && !"请选择".equals(this.selectRangeResChosenType)) {
					ApplicationEntity assAe = ApplicationEntity.changeFromEntity(
							physicalresService.getPhysicalresById(this.assRangeEntityType, Long.parseLong(this.assRangeEntityId)));
					//System.out.println(assAe.toMap());
					assLinkAeList=structureCommonService.getAppArrsByRecursionForSrcSameType(assAe,this.selectResType, "networkresourcemanage");
					if(!(assLinkAeList != null && assLinkAeList.length > 0)){
						assLinkAeList=structureCommonService.getStrutureSelationsApplicationEntity(assAe, this.selectResType, AssociatedType.LINK,  "networkresourcemanage");
					}
					if("all".equals(queryType)){
						if(assLinkAeList != null && assLinkAeList.length > 0) {
							for(ApplicationEntity assLinkApp : assLinkAeList) {
								//筛选出查询的类型集合中，跟关联资源关联的集合
									filterResList.add(assLinkApp);
							}
						}
						if(!filterResList.isEmpty()) {
							for (ApplicationEntity filterResAe : filterResList) {
								//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
								Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(filterResAe);
								//将null值转换为空串值
								resMap = ResourceCommon.mapRecombinationToString(resMap);
								for(String key:resMap.keySet()){
									resMap.put(key, ResourceCommon.dateTimeFormat(resMap.get(key)));
								}
								resMap.put("aeType", filterResAe.getType());
								//生成被查询资源的map(已进行关联资源的过滤)
								sortedMap = quickSort.sortMap(resMap,orderIdMap);//排序
								if(sortedMap!=null){
									resMap=sortedMap;
								}
								this.searchResourceMapList.add(resMap);
								
							}
						}
						quickSort.sort(searchResourceMapList, "name");
						//查询资源大集合(包括中文标题和查询资源集合)
						this.searchBigMap = new HashMap<String, Object>();
						this.searchBigMap.put("titleMap", titleMap);
						this.searchBigMap.put("contentMapList", this.searchResourceMapList);
						
						HttpServletResponse response = ServletActionContext.getResponse();
						response.setCharacterEncoding("utf-8");
						response.setContentType("text/html");
						
						GsonBuilder builder = new GsonBuilder();
						Gson gson = builder.create();
						String result = gson.toJson(this.searchBigMap);
						
						try {
							log.info("退出searchResourceAction,返回结果result="+result);
							response.getWriter().write(result);
						} catch (IOException e) {
							log.error("退出searchResourceAction,返回结果result="+result+"失败");
							e.printStackTrace();
						}
						return;
					}
					
				}
				
			}else if(this.assEntityId != null && this.assEntityType != null 
					&& !"".equals(this.assEntityId) && !"".equals(this.assEntityType)) {
				if(this.selectResChosenType != null && !"请选择".equals(this.selectResChosenType)) {
					String linkType = this.selectResChosenType.substring(this.selectResChosenType.lastIndexOf("_") + 1);
					//关联资源的entity
					ApplicationEntity assAe = ApplicationEntity.changeFromEntity(
							physicalresService.getPhysicalresById(this.assEntityType, Long.parseLong(this.assEntityId)));
					//获取查询关联资源关联实体的关系
					AssociatedType aAssociatedType = null;
					if("parent".equals(linkType)) {
						aAssociatedType = AssociatedType.CHILD;
					} else if ("link".equals(linkType)) {
						aAssociatedType = AssociatedType.LINK;
					}
					//获取关联资源的关联实体集合
					assLinkAeList = structureCommonService.getStrutureSelationsApplicationEntity(
							assAe, this.selectResType, aAssociatedType, "networkresourcemanage");
					if("all".equals(queryType)){
						if(assLinkAeList != null && assLinkAeList.length > 0) {
							for(ApplicationEntity assLinkApp : assLinkAeList) {
								//筛选出查询的类型集合中，跟关联资源关联的集合
								filterResList.add(assLinkApp);
							}
						}
						if(!filterResList.isEmpty()) {
							for (ApplicationEntity filterResAe : filterResList) {
								//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
								Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(filterResAe);
								//将null值转换为空串值
								resMap = ResourceCommon.mapRecombinationToString(resMap);
								for(String key:resMap.keySet()){
									resMap.put(key, ResourceCommon.dateTimeFormat(resMap.get(key)));
								}
								resMap.put("aeType", filterResAe.getType());
								//生成被查询资源的map(已进行关联资源的过滤)
								sortedMap = quickSort.sortMap(resMap,orderIdMap);//排序
								if(sortedMap!=null){
									resMap=sortedMap;
								}
								this.searchResourceMapList.add(resMap);
								
							}
						}
						quickSort.sort(searchResourceMapList, "name");
						//查询资源大集合(包括中文标题和查询资源集合)
						this.searchBigMap = new HashMap<String, Object>();
						this.searchBigMap.put("titleMap", titleMap);
						this.searchBigMap.put("contentMapList", this.searchResourceMapList);
						
						HttpServletResponse response = ServletActionContext.getResponse();
						response.setCharacterEncoding("utf-8");
						response.setContentType("text/html");
						
						GsonBuilder builder = new GsonBuilder();
						Gson gson = builder.create();
						String result = gson.toJson(this.searchBigMap);
						
						try {
							log.info("退出searchResourceAction,返回结果result="+result);
							response.getWriter().write(result);
						} catch (IOException e) {
							log.error("退出searchResourceAction,返回结果result="+result+"失败");
							e.printStackTrace();
						}
						return;
					}
				}
			} 
			if("all".equals(queryType)){
				List<ApplicationEntity> currentAreaResourceList = new ArrayList<ApplicationEntity>();//本区域关联的资源
				//查询   递归
				ApplicationEntity[] aeList = this.structureCommonService.getAppArrsByRecursionForSrcSameType(areaAE,this.selectResType,"networkresourcemanage");
				ApplicationEntity[] linkList = this.structureCommonService.getStrutureSelationsApplicationEntity(areaAE, this.selectResType, AssociatedType.LINK, "networkresourcemanage");
				
				if(aeList!=null && aeList.length>0){
					currentAreaResourceList.addAll(Arrays.asList(aeList));
				}
				
				if(linkList!=null && linkList.length>0){
					currentAreaResourceList.addAll(Arrays.asList(linkList));
				}
				if(currentAreaResourceList!=null && currentAreaResourceList.size()>0 ){
					searchResourceAeList.addAll(currentAreaResourceList);//保存本区域资源
				}
				List<BasicEntity> noAssociateResourceBeList = physicalresService.getNoAssociateResource(this.selectResType);//未绑关系的关联资源
				List<ApplicationEntity> noAssociateResourceList = new ArrayList<ApplicationEntity>();
				if(noAssociateResourceBeList!=null && noAssociateResourceBeList.size()>0 ){
					for (BasicEntity resBe : noAssociateResourceBeList) {
						noAssociateResourceList.add(ApplicationEntity.changeFromEntity(resBe));
					}
				}
				if(noAssociateResourceList!=null && noAssociateResourceList.size()>0 ){
					searchResourceAeList.addAll(noAssociateResourceList); //未绑关系资源
				}
			}else{
				searchResourceBeList = physicalresService.searchResourceByCondition(searchedAe,dateMap);//查询相关条件的资源
				if(searchResourceBeList!=null && searchResourceBeList.size()>0){//有关条件的资源查询不为空
					if(searchResourceBeList != null && !searchResourceBeList.isEmpty()) {
						for (BasicEntity resBe : searchResourceBeList) {
							searchResourceAeList.add(ApplicationEntity.changeFromEntity(resBe));//查询结果保存
							ids +=","+resBe.getValue("id");
						}
						ids = ids.substring(1,ids.length());
					}	
					
				}
			}
			
			if(assLinkAeList!=null && assLinkAeList.length>0 ) {
				for (ApplicationEntity assApp : searchResourceAeList) {
					for(ApplicationEntity assLinkApp : assLinkAeList) {
						//筛选出查询的类型集合中，跟关联资源关联的集合
						if(assApp.getValue("id").equals(assLinkApp.getValue("id"))) {
							filterResList.add(assApp);
						}
					}
				}
				if(!filterResList.isEmpty()) {
					for (ApplicationEntity filterResAe : filterResList) {
						//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
						Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(filterResAe);
						//将null值转换为空串值
						resMap = ResourceCommon.mapRecombinationToString(resMap);
						for(String key:resMap.keySet()){
							resMap.put(key, ResourceCommon.dateTimeFormat(resMap.get(key)));
						}
						resMap.put("aeType", filterResAe.getType());
						//生成被查询资源的map(已进行关联资源的过滤)
						sortedMap = quickSort.sortMap(resMap,orderIdMap);//排序
						if(sortedMap!=null){
							resMap=sortedMap;
						}
						this.searchResourceMapList.add(resMap);	
					}
				}
			}else{
				if(queryType.equals("all")){
					if(searchResourceAeList!=null && searchResourceAeList.size()>0){	
						//没选择关联的资源的情况
						for (ApplicationEntity resAe : searchResourceAeList) {
							if(resAe != null) {
								//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
								Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(resAe);
								//将null值转换为空串值
								resMap = ResourceCommon.mapRecombinationToString(resMap);
								for(String key:resMap.keySet()){
									resMap.put(key, ResourceCommon.dateTimeFormat(resMap.get(key)));
								}
								resMap.put("aeType", resAe.getType());
								sortedMap = quickSort.sortMap(resMap,orderIdMap);
								if(sortedMap!=null){
									resMap=sortedMap;
								}
								//生成被查询资源的map
								this.searchResourceMapList.add(resMap);	
							}
						}
					}
				}else{
					if(searchResourceAeList!=null && searchResourceAeList.size()>0){
						String entityId="";
						ApplicationEntity[] areaAps = this.structureCommonService.getAppArrsByRecursionForSrcSameType(areaAE, "Sys_Area","networkresourcemanage");
						Map<String,Object> mp = new HashMap<String,Object>();
						mp.put(areaAE.getValue("id")+"",areaAE.getValue("id"));
						if(areaAps!=null && areaAps.length>=0){
							for(ApplicationEntity ac: areaAps){
								mp.put(ac.getValue("id")+"",ac.getValue("id"));
							}
						}
						
						Map<String,Object> reMp=this.physicalresService.getSomeParentTypeApplicationEntitysMapByIds(ids, this.selectResType, "Sys_Area");
						
						if(reMp!=null && !reMp.isEmpty()){
							Map<String,Object> rMp =  new HashMap<String,Object>();
							for(String key:reMp.keySet()){
								String id = ((Map<String,Object>)reMp.get(key)).get("id")+"";
								if(mp.containsKey(id)){
									rMp.put(key,key);
								}
							}
							for(ApplicationEntity resAe : searchResourceAeList){
								String enId = resAe.getValue("id")+"";
								if(rMp.containsKey(enId)){
									//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
									Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(resAe);
									//将null值转换为空串值
									resMap = ResourceCommon.mapRecombinationToString(resMap);
									for(String key:resMap.keySet()){
										resMap.put(key, ResourceCommon.dateTimeFormat(resMap.get(key)));
									}
									resMap.put("aeType", resAe.getType());
									sortedMap = quickSort.sortMap(resMap,orderIdMap);
									if(sortedMap!=null){
										resMap=sortedMap;
									}
									//生成被查询资源的map
									this.searchResourceMapList.add(resMap);
								}else{
									entityId = resAe.getValue("_entityId")+"";
									if(entityId!=null && !"".equals(entityId)){
										List<BasicEntity> noAssociateList = this.physicalresService.getNoAssociateResource(this.selectResType,entityId);
										if(noAssociateList!=null){
											//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
											Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(resAe);
											//将null值转换为空串值
											resMap = ResourceCommon.mapRecombinationToString(resMap);
											for(String key:resMap.keySet()){
												resMap.put(key, ResourceCommon.dateTimeFormat(resMap.get(key)));
											}
											resMap.put("aeType", resAe.getType());
											sortedMap = quickSort.sortMap(resMap,orderIdMap);
											if(sortedMap!=null){
												resMap=sortedMap;
											}
											//生成被查询资源的map
											this.searchResourceMapList.add(resMap);	
										}
									}
								}	
							}
						}
					}	
			    }
			}
			quickSort.sort(searchResourceMapList, "name");
			//查询资源大集合(包括中文标题和查询资源集合)
			this.searchBigMap = new HashMap<String, Object>();
			this.searchBigMap.put("titleMap", titleMap);
			this.searchBigMap.put("contentMapList", this.searchResourceMapList);
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(this.searchBigMap);
			
			try {
				log.info("退出searchResourceAction,返回结果result="+result);
				response.getWriter().write(result);
			} catch (IOException e) {
				log.error("退出searchResourceAction,返回结果result="+result+"失败");
				e.printStackTrace();
			}
			
		}
		
	}
	
	/**
	 * 根据条件查询资源(分页)
	 */
	public void searchResourceByPageAction() {
		log.info("进入searchResourceByPageAction,根据条件查询资源(分页)");
		Map<String, Object> searchedResModuleMap = ModuleProvider.getModule(this.selectResType).toMap();
		Map<String,Object> orderIdMap = new HashMap<String,Object>();
		if(searchedResModuleMap != null) {
			//获取中文标题(不能正常获取的继续显示英文)(这里用linkedHashMap，使其与模板顺序一致，确保跟内容相对应)
			Map<String, Object> titleMap = new LinkedHashMap<String, Object>();
			for(String key : searchedResModuleMap.keySet()) {
				try {
					List<BasicEntity> entry = null;
					if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
						//数据字典，中英文转换
						entry = dictionary.getEntry(key + "," + this.selectResType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
					}
					if(entry != null && !entry.isEmpty()) {
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							titleMap.put(key, entry.get(0).getValue("display"));
							orderIdMap.put( entry.get(0).getValue("orderID").toString(), key);
						}
					}
				} catch (EntryOperationException e) {
					e.printStackTrace();
					titleMap.put(key, key);
				}
			}
			Map<String,Object> sortedMap = quickSort.sortMap(titleMap,orderIdMap);//中文字典排序
			if(sortedMap!=null){
				titleMap = sortedMap;
			}
			ApplicationEntity searchedAe = ActionHelper.getApplicationEntity(this.selectResType);
			
			//选择了关联的资源的情况
			if(this.assEntityId != null && this.assEntityType != null 
					&& !"".equals(this.assEntityId) && !"".equals(this.assEntityType)) {
				
				//获取条件筛选后的被查询的资源信息
				List<BasicEntity> searchResourceBeList = physicalresService.searchResourceByCondition(searchedAe);
				
				List<ApplicationEntity> searchResourceAeList = new ArrayList<ApplicationEntity>();
				if(searchResourceBeList != null && !searchResourceBeList.isEmpty()) {
					for (BasicEntity resBe : searchResourceBeList) {
						searchResourceAeList.add(ApplicationEntity.changeFromEntity(resBe));
					}
				}
				
				//查询资源集合
				this.searchResourceMapList = new ArrayList<Map<String,Object>>();
				
				if(this.selectResChosenType != null && !"请选择".equals(this.selectResChosenType)) {
					//String entityType = this.selectResChosenType.substring(0, this.selectResChosenType.lastIndexOf("_"));
					String linkType = this.selectResChosenType.substring(this.selectResChosenType.lastIndexOf("_") + 1);
					//关联资源的entity
					ApplicationEntity assAe = ApplicationEntity.changeFromEntity(
							physicalresService.getPhysicalresById(this.assEntityType, Long.parseLong(this.assEntityId)));
					//获取查询关联资源关联实体的关系
					AssociatedType aAssociatedType = null;
					if("parent".equals(linkType)) {
						aAssociatedType = AssociatedType.CHILD;
					} else if ("link".equals(linkType)) {
						aAssociatedType = AssociatedType.LINK;
					}
					//获取关联资源的关联实体集合
					ApplicationEntity[] assLinkAeList = structureCommonService.getStrutureSelationsApplicationEntity(
							assAe, this.selectResType, aAssociatedType, "networkresourcemanage");
					
					List<ApplicationEntity> filterResList = new ArrayList<ApplicationEntity>();
					if(assLinkAeList != null && assLinkAeList.length > 0) {
						for (ApplicationEntity assApp : searchResourceAeList) {
							for(ApplicationEntity assLinkApp : assLinkAeList) {
								//筛选出查询的类型集合中，跟关联资源关联的集合
								if(assApp.getValue("id").equals(assLinkApp.getValue("id"))) {
									filterResList.add(assApp);
								}
							}
						}
						if(!filterResList.isEmpty()) {
							for (ApplicationEntity filterResAe : filterResList) {
								//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
								Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(filterResAe);
								//将null值转换为空串值
								resMap = ResourceCommon.mapRecombinationToString(resMap);
								resMap.put("aeType", filterResAe.getType());
								sortedMap = quickSort.sortMap(resMap,orderIdMap);
								if(sortedMap!=null){
									resMap=sortedMap;
								}
								//生成被查询资源的map(已进行关联资源的过滤)
								this.searchResourceMapList.add(resMap);
								quickSort.sort(searchResourceMapList, "name");
							}
						}
					}
				}
			} else {
				
				//获取条件筛选后的被查询的资源信息
				List<BasicEntity> searchResourceBeList = physicalresService.searchResourceByCondition(searchedAe);

				List<ApplicationEntity> searchResourceAeList = new ArrayList<ApplicationEntity>();
				if(searchResourceBeList != null && !searchResourceBeList.isEmpty()) {
					for (BasicEntity resBe : searchResourceBeList) {
						searchResourceAeList.add(ApplicationEntity.changeFromEntity(resBe));
					}
				}
				
				//查询资源集合
				this.searchResourceMapList = new ArrayList<Map<String,Object>>();

				//没选择关联的资源的情况
				for (ApplicationEntity resAe : searchResourceAeList) {
					if(resAe != null) {
						//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
						Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(resAe);
						//将null值转换为空串值
						resMap = ResourceCommon.mapRecombinationToString(resMap);
						resMap.put("aeType", resAe.getType());
						sortedMap = quickSort.sortMap(resMap,orderIdMap);
						if(sortedMap!=null){
							resMap=sortedMap;
						}
						//生成被查询资源的map
						this.searchResourceMapList.add(resMap);
						quickSort.sort(searchResourceMapList, "name");
					}
				}
			}
			
			//查询资源大集合(包括中文标题和查询资源集合)
			this.searchBigMap = new HashMap<String, Object>();
			this.searchBigMap.put("titleMap", titleMap);
			this.searchBigMap.put("contentMapList", this.searchResourceMapList);
			this.searchBigMap.put("pageIndex", this.pageIndex);
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(this.searchBigMap);
			
			try {
				log.info("退出searchResourceByPageAction,返回结果result="+result);
				response.getWriter().write(result);
			} catch (IOException e) {
				log.error("退出searchResourceByPageAction,返回结果result="+result+"失败");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 导出资源信息excel
	 * @return
	 */
	public String exportBizunitRelationAction() {
		log.info("进入exportBizunitRelationAction,导出资源信息excel");
		if(this.chooseResEntityId!=null && !"".equals(this.chooseResEntityId) && this.chooseResEntityType!=null && !"".equals(this.chooseResEntityType)){
			try{
				Map<String, Object> searchedResModuleMap = ModuleProvider.getModule(this.chooseResEntityType).toMap();
				Map<String,Object> orderIdMap = new HashMap<String,Object>();
				Map<String,Object> sortedMap = null;
				if(searchedResModuleMap != null) {
					//获取中文标题(不能正常获取的继续显示英文)(这里用linkedHashMap，使其与模板顺序一致，确保跟内容相对应)
					Map<String, Object> titleMap = new LinkedHashMap<String, Object>();
					if("selfExport".equals(this.exportModel)){
						GsonBuilder builder = new GsonBuilder();
						Gson gson = builder.create();
						Map<String,String>  attrMp = gson.fromJson(this.attrMap, new TypeToken<Map<String,String>>(){}.getType());
						for(String key:attrMp.keySet()){
							titleMap.put(key, attrMp.get(key));
						}
						for(String key : searchedResModuleMap.keySet()) {
							try {
								List<BasicEntity> entry = null;
								if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
									//数据字典，中英文转换
									entry = dictionary.getEntry(key + "," + this.chooseResEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								}
								if(entry != null && !entry.isEmpty()) {
									if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
										if(titleMap.containsKey(key)){
											titleMap.put(key, entry.get(0).getValue("display").toString());
										}
										orderIdMap.put( entry.get(0).getValue("orderID").toString(), key);
									}
								}
							} catch (EntryOperationException e) {
								log.error("获取"+this.chooseResEntityType+"属性字段"+key+"中文字典失败，可能该字典不存在");
								e.printStackTrace();
							}
						}	
					}else{
						for(String key : searchedResModuleMap.keySet()) {
							try {
								List<BasicEntity> entry = null;
								if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
									//数据字典，中英文转换
									entry = dictionary.getEntry(key + "," + this.chooseResEntityType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								}
								if(entry != null && !entry.isEmpty()) {
									if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
										titleMap.put(key, entry.get(0).getValue("display"));
										orderIdMap.put( entry.get(0).getValue("orderID").toString(), key);
									}
								}
							} catch (EntryOperationException e) {
								log.error("获取"+this.chooseResEntityType+"属性字段"+key+"中文字典失败，可能该字典不存在");
								e.printStackTrace();
								titleMap.put(key, key);
							}
						}	
						sortedMap = quickSort.sortMap(titleMap,orderIdMap);//中文字典排序
						if(sortedMap!=null){
							titleMap = sortedMap;
						}
					}
					
					String[] idsArray = this.chooseResEntityId.split(",");
					if(idsArray!=null&&idsArray.length>0){
						List<ApplicationEntity> searchResourceAeList = new ArrayList<ApplicationEntity>();
						for(String eId:idsArray){
							ApplicationEntity ae = ApplicationEntity.changeFromEntity(structureCommonService.getSectionEntity(this.chooseResEntityType,eId));
							if(ae!=null){
								searchResourceAeList.add(ae);
							}
						}
						this.searchResourceMapList = new ArrayList<Map<String,Object>>();
						if(searchResourceAeList!=null&&searchResourceAeList.size()>0){
							for (ApplicationEntity resAe : searchResourceAeList) {
								if(resAe != null) {
									//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
									Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(resAe);
									//将null值转换为空串值
									resMap = ResourceCommon.mapRecombinationToString(resMap);
									resMap.put("aeType", resAe.getType());
									//生成被查询资源的map
									sortedMap = quickSort.sortMap(resMap,orderIdMap);
									if(sortedMap!=null){
										resMap=sortedMap;
									}
									this.searchResourceMapList.add(resMap);
									
								}
							}
							quickSort.sort(searchResourceMapList, "name");
						}
						//获取标题行
						List titleList = new ArrayList();
						String[] resParentNameArray = null;//资源归属
						if(this.resParentTitle!=null && !"".equals(this.resParentName)){
							titleList.add(this.resParentTitle);
							resParentNameArray = this.resParentName.split(",");
							//System.out.println(resParentNameArray.length+"---");
						}	
						for (String key : titleMap.keySet()) {
						/*	if(!"id".equals(key)) {
								titleList.add(titleMap.get(key));
							}*/
							
							titleList.add(titleMap.get(key));
						}
						//内容数据行
						List<List> dataList = new ArrayList<List>();
						int resIndex=0;
						for(Map<String, Object> map : this.searchResourceMapList) {
							List contentList = new ArrayList();
							if(resParentNameArray!=null){
								contentList.add(resParentNameArray[resIndex]);
								resIndex++;
							}
							if(map != null) {
								for(String key : map.keySet()) {
									/*if(!("id".equals(key) || "aeType".equals(key) 
											|| "_entityId".equals(key) || "_entityType".equals(key))) {
										contentList.add(map.get(key));
									}*/
									if(!("aeType".equals(key) 
											|| "_entityId".equals(key) || "_entityType".equals(key))) {
										if("selfExport".equals(this.exportModel)){
											if(titleMap.containsKey(key)){
												contentList.add(map.get(key));
											}
										}else{
											contentList.add(map.get(key));
										}
									}
								}
							}
							dataList.add(contentList);
						}
						if (!titleList.isEmpty() && !dataList.isEmpty()) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
							//导出excel数据
							resultInputStream = creatExcel2003InputStream(titleList, dataList);
							try {
								chaxunFileName = sdf.format(new Date()) + " "
										+ new String("资源数据导出表".getBytes("GBK"), "ISO-8859-1");
							} catch (UnsupportedEncodingException e) {
								
								chaxunFileName = "resource";
								log.error("发生UnsupportedEncodingException异常,设置chaxunFileName="+chaxunFileName);
								e.printStackTrace();
							}
							log.info("退出exportBizunitRelationAction,返回结果result=success");
							return "success";
						} else {
							log.info("退出exportBizunitRelationAction,返回结果result=error");
							return "error";
						}
					}
				}
				log.info("退出exportBizunitRelationAction,返回结果result=error");
				return "error";
			}catch(Exception e){
				log.error("退出exportBizunitRelationAction,发生异常,返回结果result=error");
				e.printStackTrace();
				return "error";
				
			}
		}else{
			try {
				Map<String, Object> searchedResModuleMap = ModuleProvider.getModule(this.selectResType).toMap();
				Map<String,Object> orderIdMap = new HashMap<String,Object>();
				ApplicationEntity areaAE = null;
				if(this.areaId!=null && !"".equals(this.areaId)){
					BasicEntity be = physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
					areaAE = ApplicationEntity.changeFromEntity(be);
				}
				Map<String,Object> sortedMap = null;
				if(searchedResModuleMap != null) {
					//获取中文标题(不能正常获取的继续显示英文)(这里用linkedHashMap，使其与模板顺序一致，确保跟内容相对应)
					titleMap = new LinkedHashMap<String, Object>();
					if("selfExport".equals(this.exportModel)){
						GsonBuilder builder = new GsonBuilder();
						Gson gson = builder.create();
						Map<String,String>  attrMp = gson.fromJson(this.attrMap, new TypeToken<Map<String,String>>(){}.getType());
						for(String key:attrMp.keySet()){
							titleMap.put(key, attrMp.get(key));
						}
						for(String key : searchedResModuleMap.keySet()) {
							try {
								List<BasicEntity> entry = null;
								if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
									//数据字典，中英文转换
									entry = dictionary.getEntry(key + "," + this.selectResType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								}
								if(entry != null && !entry.isEmpty()) {
									if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
										if(titleMap.containsKey(key)){
											titleMap.put(key, entry.get(0).getValue("display").toString());
										}
										orderIdMap.put( entry.get(0).getValue("orderID").toString(), key);
									}
								}
							} catch (EntryOperationException e) {
								e.printStackTrace();
							}
						}	
					}else{
						for(String key : searchedResModuleMap.keySet()) {
							try {
								List<BasicEntity> entry = null;
								if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
									//数据字典，中英文转换
									entry = dictionary.getEntry(key + "," + this.selectResType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								}
								if(entry != null && !entry.isEmpty()) {
									if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
										titleMap.put(key, entry.get(0).getValue("display"));
										orderIdMap.put( entry.get(0).getValue("orderID").toString(), key);
									}
								}
							} catch (EntryOperationException e) {
								e.printStackTrace();
								titleMap.put(key, key);
							}
						}	
						sortedMap = quickSort.sortMap(titleMap,orderIdMap);//中文字典排序
						if(sortedMap!=null){
							titleMap = sortedMap;
						}
					}
					ApplicationEntity searchedAe = ActionHelper.getApplicationEntity(this.selectResType);
					Map<String,String> dateMap = null;
					if(this.dateString!=null&&!"".equals(this.dateString)){//属性类型为日期特殊处理 
						dateMap = new HashMap<String,String>();
						String[] dates=this.dateString.split(",");
						for(String s:dates){
							String key=s.substring(0,s.indexOf(":"));
							String date=s.substring(s.indexOf(":")+1,s.length());
							dateMap.put(key,date);
						}
					}
					//获取条件筛选后的被查询的资源信息
					String queryType="";
					List<BasicEntity> searchResourceBeList=null;
					//System.out.println(this.hasCondition);
					List<ApplicationEntity> searchResourceAeList = new ArrayList<ApplicationEntity>();//查询结果list
					List<ApplicationEntity> currentAreaResourceList = new ArrayList<ApplicationEntity>();//本区域关联的资源
					//查询   递归
					ApplicationEntity[] aeList = this.structureCommonService.getAppArrsByRecursionForSrcSameType(areaAE,this.selectResType,"networkresourcemanage");
					ApplicationEntity[] linkList = this.structureCommonService.getStrutureSelationsApplicationEntity(areaAE, this.selectResType, AssociatedType.LINK, "networkresourcemanage");
					
					if(aeList!=null && aeList.length>0){
						currentAreaResourceList.addAll(Arrays.asList(aeList));
					}
					
					if(linkList!=null && linkList.length>0){
						currentAreaResourceList.addAll(Arrays.asList(linkList));
					}
					
					if("hasCondition".equals(this.hasCondition)){
						int conditionFlag=0;
						for(String key : searchedAe.keyset()) {
							if (searchedAe.getValue(key) != null && !"".equals(searchedAe.getValue(key))) {
								if("_entityId".equals(key) || "_entityType".equals(key)) {
									continue;
								} else {
									conditionFlag=conditionFlag+1;
								}
							}
						}

						//当属性为日期类型时特殊处理
						if(dateMap!=null&&!dateMap.isEmpty()){
							for(String k:dateMap.keySet()){
								String dates= dateMap.get(k).toString();
								String date1= dates.substring(0,dates.indexOf("/"));
								String date2 = dates.substring(dates.indexOf("/")+1,dates.length());
								if((date1!=null && !"".equals(date1)) || (date2!=null && !"".equals(date2)) ){
									conditionFlag=conditionFlag+1;
								}
							}	
						}
						if(conditionFlag!=0){
							searchResourceBeList = physicalresService.searchResourceByCondition(searchedAe,dateMap);//查询相关条件的资源
							if(searchResourceBeList!=null && searchResourceBeList.size()>0){//有关条件的资源查询不为空
								if(searchResourceBeList != null && !searchResourceBeList.isEmpty()) {
									for (BasicEntity resBe : searchResourceBeList) {
										searchResourceAeList.add(ApplicationEntity.changeFromEntity(resBe));//查询结果保存
									}
								}		
							}
							
						}else{
							queryType="all";
							if(currentAreaResourceList!=null && currentAreaResourceList.size()>0 ){
								searchResourceAeList.addAll(currentAreaResourceList);//保存本区域资源
							}
						}
					}else{
						queryType="all";//没有查询条件资源 
						if(currentAreaResourceList!=null && currentAreaResourceList.size()>0 ){
							searchResourceAeList.addAll(currentAreaResourceList);//保存本区域资源
						}
					}
					
					List<BasicEntity> noAssociateResourceBeList = physicalresService.getNoAssociateResource(this.selectResType);//未绑关系的关联资源
					List<ApplicationEntity> noAssociateResourceList = new ArrayList<ApplicationEntity>();
					if(noAssociateResourceBeList!=null && noAssociateResourceBeList.size()>0 ){
						for (BasicEntity resBe : noAssociateResourceBeList) {
							noAssociateResourceList.add(ApplicationEntity.changeFromEntity(resBe));
						}
					}
					
					//查询资源集合
					this.searchResourceMapList = new ArrayList<Map<String,Object>>();
				/*	System.out.println(this.assEntityId+"****1");
					System.out.println(this.assEntityType+"****1");
					System.out.println(this.selectResChosenType+"****1");
					System.out.println(this.assRangeEntityId+"****2");
					System.out.println(this.assRangeEntityType+"****2");
					System.out.println(this.selectRangeResChosenType+"****2");*/
					if(!searchResourceAeList.isEmpty()) {
						if(this.assRangeEntityId != null && this.assRangeEntityType != null 
								&& !"".equals(this.assRangeEntityId) && !"".equals(this.assRangeEntityType)){
							if(this.selectRangeResChosenType != null && !"请选择".equals(this.selectRangeResChosenType)) {
								ApplicationEntity assAe = ApplicationEntity.changeFromEntity(
										physicalresService.getPhysicalresById(this.assRangeEntityType, Long.parseLong(this.assRangeEntityId)));
								//System.out.println(assAe.toMap());
								ApplicationEntity[] assLinkAeList=structureCommonService.getAppArrsByRecursionForSrcSameType(assAe,this.selectResType, "networkresourcemanage");
								if(!(assLinkAeList != null && assLinkAeList.length > 0)){
									assLinkAeList=structureCommonService.getStrutureSelationsApplicationEntity(assAe, this.selectResType, AssociatedType.LINK,  "networkresourcemanage");
								}
								List<ApplicationEntity> filterResList = new ArrayList<ApplicationEntity>();
								if(assLinkAeList != null && assLinkAeList.length > 0) {
									for (ApplicationEntity assApp : searchResourceAeList) {
										for(ApplicationEntity assLinkApp : assLinkAeList) {
											//筛选出查询的类型集合中，跟关联资源关联的集合
											if(assApp.getValue("id").equals(assLinkApp.getValue("id"))) {
												filterResList.add(assApp);
											}
										}
									}
									if(noAssociateResourceList!=null && noAssociateResourceList.size()>0 ){
										filterResList.addAll(noAssociateResourceList); //未绑关系资源
									}
									
									if(!filterResList.isEmpty()) {
										for (ApplicationEntity filterResAe : filterResList) {
											//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
											Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(filterResAe);
											//将null值转换为空串值
											resMap = ResourceCommon.mapRecombinationToString(resMap);
											resMap.put("aeType", filterResAe.getType());
											//生成被查询资源的map(已进行关联资源的过滤)
											sortedMap = quickSort.sortMap(resMap,orderIdMap);//排序
											if(sortedMap!=null){
												resMap=sortedMap;
											}
											this.searchResourceMapList.add(resMap);
											
										}
									}
								}
							}
							
						}else if(this.assEntityId != null && this.assEntityType != null 
								&& !"".equals(this.assEntityId) && !"".equals(this.assEntityType)) {
							if(this.selectResChosenType != null && !"请选择".equals(this.selectResChosenType)) {
								//String entityType = this.selectResChosenType.substring(0, this.selectResChosenType.lastIndexOf("_"));
								String linkType = this.selectResChosenType.substring(this.selectResChosenType.lastIndexOf("_") + 1);
								//关联资源的entity
								ApplicationEntity assAe = ApplicationEntity.changeFromEntity(
										physicalresService.getPhysicalresById(this.assEntityType, Long.parseLong(this.assEntityId)));
								//获取查询关联资源关联实体的关系
								AssociatedType aAssociatedType = null;
								if("parent".equals(linkType)) {
									aAssociatedType = AssociatedType.CHILD;
								} else if ("link".equals(linkType)) {
									aAssociatedType = AssociatedType.LINK;
								}
								//获取关联资源的关联实体集合
								ApplicationEntity[] assLinkAeList = structureCommonService.getStrutureSelationsApplicationEntity(
										assAe, this.selectResType, aAssociatedType, "networkresourcemanage");
								
								List<ApplicationEntity> filterResList = new ArrayList<ApplicationEntity>();
								if(assLinkAeList != null && assLinkAeList.length > 0) {
									for (ApplicationEntity assApp : searchResourceAeList) {
										for(ApplicationEntity assLinkApp : assLinkAeList) {
											//筛选出查询的类型集合中，跟关联资源关联的集合
											if(assApp.getValue("id").equals(assLinkApp.getValue("id"))) {
												filterResList.add(assApp);
											}
										}
									}
									if(noAssociateResourceList!=null && noAssociateResourceList.size()>0 ){
										filterResList.addAll(noAssociateResourceList); //未绑关系资源
									}
									if(!filterResList.isEmpty()) {
										for (ApplicationEntity filterResAe : filterResList) {
											//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
											Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(filterResAe);
											//将null值转换为空串值
											resMap = ResourceCommon.mapRecombinationToString(resMap);
											resMap.put("aeType", filterResAe.getType());
											//生成被查询资源的map(已进行关联资源的过滤)
											sortedMap = quickSort.sortMap(resMap,orderIdMap);//排序
											if(sortedMap!=null){
												resMap=sortedMap;
											}
											this.searchResourceMapList.add(resMap);
											
										}
									}
								}
							}
						} else {
							

							if(queryType.equals("all")){
								if(noAssociateResourceList!=null && noAssociateResourceList.size()>0 ){
									searchResourceAeList.addAll(noAssociateResourceList); //未绑关系资源
								}
								if(searchResourceAeList!=null && searchResourceAeList.size()>0){	
									//没选择关联的资源的情况
									for (ApplicationEntity resAe : searchResourceAeList) {
										if(resAe != null) {
											//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
											Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(resAe);
											//将null值转换为空串值
											resMap = ResourceCommon.mapRecombinationToString(resMap);
											resMap.put("aeType", resAe.getType());
											sortedMap = quickSort.sortMap(resMap,orderIdMap);
											if(sortedMap!=null){
												resMap=sortedMap;
											}
											//System.out.println(sortedMap+"+++++++++++++++++++++++++++");
											//生成被查询资源的map
											this.searchResourceMapList.add(resMap);	
										}
									}
								}
							}else{
								String resAeId="";
								String rAeId="";
								if(searchResourceAeList!=null && searchResourceAeList.size()>0 && currentAreaResourceList!=null &&currentAreaResourceList.size()>0){
									for (ApplicationEntity resAe : searchResourceAeList) {
										resAeId= resAe.getValue("id")+"";
										for(ApplicationEntity rAe:currentAreaResourceList){
											rAeId = rAe.getValue("id")+"";
											if(resAeId.equals(rAeId)){
												//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
												Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(resAe);
												//将null值转换为空串值
												resMap = ResourceCommon.mapRecombinationToString(resMap);
												resMap.put("aeType", resAe.getType());
												sortedMap = quickSort.sortMap(resMap,orderIdMap);
												if(sortedMap!=null){
													resMap=sortedMap;
												}
												//System.out.println(sortedMap+"+++++++++++++++++++++++++++");
												//生成被查询资源的map
												this.searchResourceMapList.add(resMap);
											}
										}
								    }
								}
								if(searchResourceAeList!=null && searchResourceAeList.size()>0 && noAssociateResourceList!=null && noAssociateResourceList.size()>0){	//未绑关系资源
									for (ApplicationEntity resAe : noAssociateResourceList) {
										resAeId= resAe.getValue("id")+"";
										for(ApplicationEntity rAe : searchResourceAeList){
											rAeId = rAe.getValue("id")+"";	
											if(resAeId.equals(rAeId)){
												//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
												Map<String, Object> resMap = ResourceCommon.applicationEntityConvertMap(resAe);
												//将null值转换为空串值
												resMap = ResourceCommon.mapRecombinationToString(resMap);
												resMap.put("aeType", resAe.getType());
												sortedMap = quickSort.sortMap(resMap,orderIdMap);
												if(sortedMap!=null){
													resMap=sortedMap;
												}
												//System.out.println(sortedMap+"+++++++++++++++++++++++++++");
												//生成被查询资源的map
												this.searchResourceMapList.add(resMap);
											}
										}
									}
								}
						}
							
							
							
						}
						quickSort.sort(searchResourceMapList, "name");
						
						//查询资源大集合(包括中文标题和查询资源集合)
						this.searchBigMap = new HashMap<String, Object>();
						this.searchBigMap.put("titleMap", titleMap);
						this.searchBigMap.put("contentMapList", this.searchResourceMapList);
						//获取标题行
						List titleList = new ArrayList();
						if(titleMap != null)
							
						for (String key : titleMap.keySet()) {
						/*	if(!"id".equals(key)) {
								titleList.add(titleMap.get(key));
							}*/
							titleList.add(titleMap.get(key));
						}
						//内容数据行
						List<List> dataList = new ArrayList<List>();
						for(Map<String, Object> map : this.searchResourceMapList) {
							List contentList = new ArrayList();
							if(map != null) {
								for(String key : map.keySet()) {
									/*if(!("id".equals(key) || "aeType".equals(key) 
											|| "_entityId".equals(key) || "_entityType".equals(key))) {
										contentList.add(map.get(key));
									}*/
									if(!("aeType".equals(key) 
											|| "_entityId".equals(key) || "_entityType".equals(key))) {
										if("selfExport".equals(this.exportModel)){
											if(titleMap.containsKey(key)){
												contentList.add(map.get(key));
											}
										}else{
											contentList.add(map.get(key));
										}
										
									}
								}
							}
							dataList.add(contentList);
						}
						//System.out.println("dfkdkfddkfdsjkfjksd");
						if (!titleList.isEmpty() && !dataList.isEmpty()) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
							//导出excel数据
							resultInputStream = creatExcel2003InputStream(titleList, dataList);
							try {
								chaxunFileName = sdf.format(new Date()) + " "
										+ new String("资源数据导出表".getBytes("GBK"), "ISO-8859-1");
							} catch (UnsupportedEncodingException e) {
								chaxunFileName = "resource";
								log.error("发生UnsupportedEncodingException异常,设置chaxunFileName="+chaxunFileName);
								e.printStackTrace();
							}
							log.info("退出exportBizunitRelationAction,返回结果result=success");
							return "success";
						} else {
							log.info("退出exportBizunitRelationAction,返回结果result=error");
							return "error";
						}
					}
				}
				log.info("退出exportBizunitRelationAction,返回结果result=error");
				return "error";
			} catch (NumberFormatException e) {
				log.error("退出exportBizunitRelationAction,发生NumberFormatException异常,返回结果result=error");
				e.printStackTrace();
				return "error";
			}
			
		}
		
	}

	/**
	 * 
	 * @param biaotou
	 *            表头数据
	 * @param data
	 *            内容
	 * @return ByteArrayInputStream 可以处理的数据类型有int
	 *         ,Integer,Date，double，Double，Float，float，Calendar，RichTextString
	 */
	public static ByteArrayInputStream creatExcel2003InputStream(List biaotou,
			List<List> data) {
		log.info("进入creatExcel2003InputStream(List biaotou,List<List> data),数据导出到Excel表");
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;

		ByteArrayOutputStream out = null;
		try {
			HSSFRow row = null;
			HSSFCell cell = null;
			wb = new HSSFWorkbook();
			//wb.setSheetName(1, "table1");
			sheet = (HSSFSheet) wb.createSheet();
			
			row = sheet.createRow(0);
			for (int i = 0; i < biaotou.size(); i++) {
				// 建立新cell（单元格）对象
				cell = row.createCell(i);
				cell.setCellValue((String) biaotou.get(i));
			}
			List object = null;

			for (int i = 0; i < data.size(); i++) {
				// 建立新row（行）对象
				row = sheet.createRow(i + 1);
				object = data.get(i);
				for (int j = 0; j < object.size(); j++) {
					// 一行的内容
					cell = row.createCell(j);
					Object cellValue = object.get(j);
					//类型转换
					if(cellValue == null || "null".equals(cellValue) || "".equals(cellValue)) {
						//数据中get出来是空值的情况，excel中导出空串值
						cell.setCellValue("");
					} else {
						//有正常数据的情况
						if(cellValue.getClass().getName().indexOf("Integer") > -1) {
							int value = Integer.parseInt(cellValue.toString());
							cell.setCellValue(value);
						} else if(cellValue.getClass().getName().indexOf("Double") > -1) {
							double value = Double.parseDouble(cellValue.toString());
							cell.setCellValue(value);
						} else if(cellValue.getClass().getName().indexOf("Float") > -1) {
							float value = Float.parseFloat(cellValue.toString());
							cell.setCellValue(value);
						} else if(cellValue.getClass().getName().indexOf("Boolean") > -1) {
							boolean booleanValue = Boolean.parseBoolean(cellValue.toString());
							String value = booleanValue ? "是" : "否";
							cell.setCellValue(value);
						} else if(cellValue.getClass().getName().indexOf("Date") > -1) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
							String value = sdf.format(cellValue);
							cell.setCellValue(value);
						} else {
							String value = cellValue + "";
							cell.setCellValue(value);
						}
					}
				}
			}

			out = new ByteArrayOutputStream();
			wb.write(out);
			out.close();
			log.info("退出creatExcel2003InputStream(List biaotou,List<List> data),数据导出成功");
		} catch (Exception e) {
			log.error("退出creatExcel2003InputStream(List biaotou,List<List> data),发生异常，返回null");
			e.printStackTrace();
			return null;
		}
		return new ByteArrayInputStream(out.toByteArray());
	}
	/**
	 * 
	 * @description: 取得某个资源下得模块及端子(ODF 光交接箱 光分纤箱 终端盒)
	 * @author：
	 * @return     
	 * @return String     
	 * @date：2012-4-19 上午10:32:23
	 */
	public String getTerminalsAction(){
		log.info("进入getTerminalsAction()，取得某个资源下得模块及端子(ODF 光交接箱 光分纤箱 终端盒)");
		try{
			if(!"".equals(this.currentEntityType)&&!"".equals(this.currentEntityId)&&this.currentEntityType!=null&&this.currentEntityId!=null){
				BasicEntity currentBasicEntity = physicalresService.getPhysicalresById(this.currentEntityType, Integer.valueOf(this.currentEntityId));
				if(currentBasicEntity!=null){
					ApplicationEntity currentEntity = ApplicationEntity.changeFromEntity(currentBasicEntity);
					Map<String,Object> resultMap =null;
					List<Map<String,Object>> TerminalList=null;
					//当前资源类型为odf和FiberCrossCabinet时要取得odm相关信息及端子
					if("ODF".equals(this.currentEntityType)||"FiberCrossCabinet".equals(this.currentEntityType)){
						ApplicationEntity[] odmApps= structureCommonService.getStrutureSelationsApplicationEntity(currentEntity, "ODM", AssociatedType.CHILD, "networkresourcemanage");
						if(odmApps!=null){
							for(ApplicationEntity app:odmApps){
								resultMap = new HashMap<String,Object>();
								TerminalList=new ArrayList<Map<String,Object>>();
								TerminalList= structureCommonService.getStrutureSelationsApplicationMap(app, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
								
								if(TerminalList!=null){
									quickSort.sort(TerminalList, "name");
									resultMap.put("terminalcount", TerminalList.size());
									resultMap.put("TerminalList",TerminalList);
									//System.out.println(odmMap+"-----------------");
									resultMap.put("name", app.getValue("label"));
									resultMap.put("odmId",app.getValue("id"));
									odmTerminalList.add(resultMap);
								}	
							}
						}
					}else if("DDF".equals(this.currentEntityType)){
						ApplicationEntity[] odmApps= structureCommonService.getStrutureSelationsApplicationEntity(currentEntity, "DDM", AssociatedType.CHILD, "networkresourcemanage");
						if(odmApps!=null){
							for(ApplicationEntity app:odmApps){
								resultMap = new HashMap<String,Object>();
								TerminalList=new ArrayList<Map<String,Object>>();
								TerminalList= structureCommonService.getStrutureSelationsApplicationMap(app, "DDFTerminal", AssociatedType.CHILD, "networkresourcemanage");
								
								if(TerminalList!=null){
									quickSort.sort(TerminalList, "name");
									resultMap.put("terminalcount", TerminalList.size());
									resultMap.put("TerminalList",TerminalList);
									//System.out.println(odmMap+"-----------------");
									resultMap.put("name", app.getValue("label"));
									resultMap.put("odmId",app.getValue("id"));
									odmTerminalList.add(resultMap);
								}	
							}
						}
					}
					//当前资源下端子信息
					resultMap = new HashMap<String,Object>();
					TerminalList=new ArrayList<Map<String,Object>>();
					if("DDF".equals(this.currentEntityType)){
						TerminalList= structureCommonService.getStrutureSelationsApplicationMap(currentEntity, "DDFTerminal", AssociatedType.CHILD, "networkresourcemanage");
					}else{
						TerminalList= structureCommonService.getStrutureSelationsApplicationMap(currentEntity, "Terminal", AssociatedType.CHILD, "networkresourcemanage");
					}
					
					
					if(TerminalList!=null&& !TerminalList.isEmpty()){
						quickSort.sort(TerminalList, "name");
						//System.out.println(TerminalList+"---");
						resultMap.put("terminalcount", TerminalList.size());
						resultMap.put("TerminalList",TerminalList);
						resultMap.put("name","");
						resultMap.put("odmId","");	
						odmTerminalList.add(resultMap);	
					}
				}
				quickSort.sort(odmTerminalList, "name");
			}
			String result="error";
			if("DDF".equals(this.currentEntityType)){
				log.info("退出getTerminalsAction()，返回结果result=addDDF");
				result = "addDDF";
			}else{
				log.info("退出getTerminalsAction()，返回结果result=success");
				result = "success";
			}
		    return result;
		}catch(Exception e){
			log.error("退出入getTerminalsAction()，发生异常，返回result=error");
			e.printStackTrace();
			return "error";
		}
		
	}
	/**
	 * 
	 * @description: 批量增加模块端子
	 * @author：
	 * @return     
	 * @return String     
	 * @date：2012-4-19 上午11:25:46
	 */
	public void addOdmTerminalLotAction(){
		log.info("进入addOdmTerminalLotAction()，批量增加模块端子");
	    try {
	    	
	    	char TerminalNum1 = 0;
			char TerminalNum2 = 0;
			int TerminalNumLength =0;
			if(!"".equals(startTerminalNumber) && !"".equals(endTerminalNumber) && startTerminalNumber!=null && endTerminalNumber!=null){
		    	if((startTerminalNumber.toCharArray()!=null&&startTerminalNumber.toCharArray().length>1)||(endTerminalNumber.toCharArray()!=null&&endTerminalNumber.toCharArray().length>1)){	
		    		TerminalNum1=(char)Integer.parseInt(startTerminalNumber);
		    		TerminalNum2=(char)Integer.parseInt(endTerminalNumber);	
		    		TerminalNumLength =Integer.parseInt(endTerminalNumber)-Integer.parseInt(startTerminalNumber);
		    	}else{
		    		TerminalNum1 = startTerminalNumber.toCharArray()[0];
		    		TerminalNum2 = endTerminalNumber.toCharArray()[0];
		    		TerminalNumLength = TerminalNum2 - TerminalNum1;
		    	}
			}
			//int TerminalNumLength = TerminalNum2 - TerminalNum1;//端子开始编号和结束编号间隔长度
			//System.out.println(TerminalNumLength+"++++");
			if(!"".equals(startOdmNumber) && !"".equals(endOdmNumber)&&startOdmNumber!=null&&endOdmNumber!=null){//有模块设置, 先保存odm再保存端子
				char odmNum1 = 0;
				char odmNum2 = 0;
				int odmNumLength=0;;
		    	if((startOdmNumber.toCharArray()!=null && startOdmNumber.toCharArray().length>1)||(endOdmNumber.toCharArray()!=null && endOdmNumber.toCharArray().length>1)){
		    		odmNum1=(char)Integer.parseInt(startOdmNumber);
		    		odmNum2=(char)Integer.parseInt(endOdmNumber);
		    		odmNumLength=Integer.parseInt(endOdmNumber)-Integer.parseInt(startOdmNumber);
		    	}else{
		    		odmNum1 = startOdmNumber.toCharArray()[0];
		    		odmNum2 = endOdmNumber.toCharArray()[0];
		    		odmNumLength=odmNum2-odmNum1;
		    	}
				//int odmNumLength=odmNum2-odmNum1; //模块开始编号和结束编号间隔长度
				for(int i=0;i<=odmNumLength;i++){//批量增加
					ApplicationEntity appEntity = ModuleProvider.getModule("ODM").createApplicationEntity();
					//appEntity.setValue("id", Unique.nextLong("odm_id"));
					appEntity.setValue("id",structureCommonService.getEntityPrimaryKey("ODM"));
					String label="";
					if(startOdmNumber.toCharArray().length>1||endOdmNumber.toCharArray().length>1){
						if(Integer.valueOf(odmNum1)+i>9){
							label=String.valueOf((Integer.valueOf(odmNum1)+i));
						}else{
							if(startOdmNumber.indexOf("0")!=0){
								label=String.valueOf((Integer.valueOf(odmNum1)+i));
							}else{
								label="0"+String.valueOf((Integer.valueOf(odmNum1)+i));
							}	
						}
						
						
					}else{
						label=String.valueOf(((char)(odmNum1+i)));
					}
					
					appEntity.setValue("label",label);
					appEntity.setValue("odmType","");
					appEntity.setValue("capacity","");
					int status=structureCommonService.saveInfoEntity(appEntity,"networkresourcemanage"); //保存模块信息
					if(status==1){
						BasicEntity addedBasicEntity=physicalresService.getPhysicalresById("ODM",Long.valueOf(appEntity.getValue("id").toString()));
						ApplicationEntity addAppEntity = ApplicationEntity.changeFromEntity(addedBasicEntity);
						BasicEntity parentBasicEntity=physicalresService.getPhysicalresById(this.currentEntityType,Long.valueOf(this.currentEntityId));
						ApplicationEntity parentAppEntity = ApplicationEntity.changeFromEntity(parentBasicEntity);
					    status = structureCommonService.createAssociatedRelation(parentAppEntity, addAppEntity, AssociatedType.CLAN,"networkresourcemanage");//模块与当前资源创建父子关系
					    if(status==1){
					    	if(!"".equals(startTerminalNumber) && !"".equals(endTerminalNumber) && startTerminalNumber!=null && endTerminalNumber!=null){//保存模块中端子信息
								 for(int j=0;j<=TerminalNumLength;j++){//批量增加
									 ApplicationEntity terminalAppEntity = ModuleProvider.getModule("Terminal").createApplicationEntity();
									 String name="";
									
									if(startTerminalNumber.toCharArray().length>1||endTerminalNumber.toCharArray().length>1){
										if(Integer.valueOf(TerminalNum1)+j>9){
											name=String.valueOf((Integer.valueOf(TerminalNum1)+j));
										}else{
											if(startTerminalNumber.indexOf("0")!=0){
												name=String.valueOf((Integer.valueOf(TerminalNum1)+j));
											}else{
												name="0"+String.valueOf((Integer.valueOf(TerminalNum1)+j));
											}
											
										}
										
									}else{
											name=String.valueOf(((char)(TerminalNum1+j)));
									}
									 if(useOdmName.equals("true")){//使用模块名作为前缀
										 if(useLinklabel.equals("true")){//使用连接符
											 if(linklabel.equals("straight")){//直
												 name=label+"-"+name;
											 }else if(linklabel.equals("curve")){//曲
												 name=label+"~"+name;
											 }else if(linklabel.equals("slant")){//斜
												 name=label+"/"+name;
											 }
											 terminalAppEntity.setValue("name",name);
										 }else{//不使用连接符
											 terminalAppEntity.setValue("name",label+name);
										 } 
									 }else{//不使用模块名作为前缀
										 terminalAppEntity.setValue("name",name);										 
									 }
									 //terminalAppEntity.setValue("id",Unique.nextLong("terminal_id"));
									 terminalAppEntity.setValue("id",structureCommonService.getEntityPrimaryKey("Terminal"));
									 terminalAppEntity.setValue("label","");
									 terminalAppEntity.setValue("status","");
									 status = structureCommonService.saveInfoEntity(terminalAppEntity,"networkresourcemanage");//保存端子信息
									 if(status==1){
										 addedBasicEntity=physicalresService.getPhysicalresById("Terminal",Long.valueOf(terminalAppEntity.getValue("id").toString()));
										 addAppEntity = ApplicationEntity.changeFromEntity(addedBasicEntity);
										 parentBasicEntity=physicalresService.getPhysicalresById("ODM",Long.valueOf(appEntity.getValue("id").toString()));
										 parentAppEntity = ApplicationEntity.changeFromEntity(parentBasicEntity);
										 structureCommonService.createAssociatedRelation(parentAppEntity, addAppEntity, AssociatedType.CLAN,"networkresourcemanage");//创建模块与端子关系
									 }
								 }
					    	}
					    }
					}
				}
			}else if(!"".equals(startTerminalNumber) && !"".equals(endTerminalNumber) && startTerminalNumber!=null && endTerminalNumber!=null){//无（ODM）模块设置,在当前资源直接添加端子
				 
				 for(int i=0;i<=TerminalNumLength;i++){
					 String name="";
						
						if(startTerminalNumber.toCharArray().length>1||endTerminalNumber.toCharArray().length>1){
								name=String.valueOf((Integer.valueOf(TerminalNum1)+i));
						}else{
								name=String.valueOf(((char)(TerminalNum1+i)));
						}
					 ApplicationEntity terminalAppEntity = ModuleProvider.getModule("Terminal").createApplicationEntity();
					// System.out.println(name+"++++++++++++++++++");
					 terminalAppEntity.setValue("name",name);
					// terminalAppEntity.setValue("id",Unique.nextLong("terminal_id"));
					 terminalAppEntity.setValue("id",structureCommonService.getEntityPrimaryKey("Terminal"));
					 terminalAppEntity.setValue("label","");
					 terminalAppEntity.setValue("status","");
					 int status = structureCommonService.saveInfoEntity(terminalAppEntity,"networkresourcemanage");//保存端子信息
					 if(status==1){
						BasicEntity addedBasicEntity=physicalresService.getPhysicalresById("Terminal",Long.valueOf(terminalAppEntity.getValue("id").toString()));
						ApplicationEntity addAppEntity = ApplicationEntity.changeFromEntity(addedBasicEntity);
						BasicEntity parentBasicEntity=physicalresService.getPhysicalresById(this.currentEntityType,Long.valueOf(this.currentEntityId));
						ApplicationEntity parentAppEntity = ApplicationEntity.changeFromEntity(parentBasicEntity);
						 structureCommonService.createAssociatedRelation(parentAppEntity, addAppEntity, AssociatedType.CLAN,"networkresourcemanage");//当前资源与端子创建关系
					 }
				 }
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			try {
				log.info("退出addOdmTerminalLotAction()，返回结果result=success");
				response.getWriter().write("success");
			} catch (IOException e) {
				log.error("退出addOdmTerminalLotAction()，返回结果result=success失败");
				e.printStackTrace();
			}
		} catch (Exception e) {
			log.error("退出addOdmTerminalLotAction()，发生异常Exception");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description:取得当前资源的模块（ODM）及端子信息(为了面板图显示)
	 * @author：
	 * @return     
	 * @return String     
	 * @date：2012-4-23 上午10:24:00
	 */
	public void getOdmAndTerminalMessageAction(){
		log.info("进入getOdmAndTerminalMessageAction()，取得当前资源的模块（ODM）及端子信息(为了面板图显示)");
		try{
			int terminalOtherCount=0; //其他
		//	int terminalDiscardCount=0;//废弃
			int terminalBreakCount=0;//故障/损坏
			//int terminalPartUseCount=0;//部分使用
			//int terminalReadyUseCount=0;//备用
			//int terminalUsingCount=0;//在用
			int terminalPreUsedCount=0;//预占用
			int terminalUsedCount=0;//占用
			int terminalNoUseCount=0;//空闲
			if(this.currentEntityType!=""&&this.currentEntityId!=""&&this.currentEntityType!=null&&this.currentEntityId!=null){
				BasicEntity currentBasicEntity = physicalresService.getPhysicalresById(this.currentEntityType, Integer.valueOf(this.currentEntityId));
				if(currentBasicEntity!=null){
					ApplicationEntity currentEntity = ApplicationEntity.changeFromEntity(currentBasicEntity);
					//当前资源类型为odf和FiberCrossCabinet时要取得odm相关信息及端子
					if("ODF".equals(this.currentEntityType)||"FiberCrossCabinet".equals(this.currentEntityType)){
						ApplicationEntity[] odmApps= structureCommonService.getStrutureSelationsApplicationEntity(currentEntity, "ODM", AssociatedType.CHILD, "networkresourcemanage");
						if(odmApps!=null&&odmApps.length>0){
							
							for(ApplicationEntity app:odmApps){
								Map<String,Object> resultMap = new HashMap<String,Object>();
								resultMap.put("name",app.getValue("label"));
								ApplicationEntity[] odmTerminalAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(app,"Terminal",AssociatedType.CHILD, "networkresourcemanage");
								if(odmTerminalAppArrs != null && odmTerminalAppArrs.length > 0) {
									List<Map<String,Object>> terminalList = new ArrayList<Map<String,Object>>();
									for (ApplicationEntity odmTerminalApp : odmTerminalAppArrs) {
										//获取ODM端子关联的纤芯
										ApplicationEntity[] fiberCoreArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmTerminalApp,"FiberCore",AssociatedType.LINK, "networkresourcemanage");
										//获取ODM端子关联的尾纤
										ApplicationEntity[] PigtailedFiberArrs = structureCommonService.getStrutureSelationsApplicationEntity(odmTerminalApp,"PigtailedFiber",AssociatedType.LINK, "networkresourcemanage");
										
										if(fiberCoreArrs != null || PigtailedFiberArrs != null) {
											int resultCount = fiberCoreArrs.length + PigtailedFiberArrs.length;
											if(resultCount == 1) {
												//已成端子
												Map<String,Object> useOneTimeTerminalMap = new HashMap<String,Object>();
												//useOneTimeTerminalMap = odmTerminalApp.toMap();
												useOneTimeTerminalMap.put("name",odmTerminalApp.getValue("name"));
												useOneTimeTerminalMap.put("id",odmTerminalApp.getValue("id"));
												useOneTimeTerminalMap.put("status",odmTerminalApp.getValue("status"));
												String status= useOneTimeTerminalMap.get("status").toString();
												if(status!=null&&!"".equals(status)){
													/*if(status.equals("其他")){
														terminalOtherCount++;
													}else if(status.equals("废弃")){
														terminalDiscardCount++;
													}else */
													if(status.equals("故障/损坏")){
														terminalBreakCount++;
													}
												/*	else if(status.equals("部分使用")){
														terminalPartUseCount++;
													}else if(status.equals("备用")){
														terminalReadyUseCount++;
													}else if(status.equals("在用")){
														terminalUsingCount++;
													}*/
													else if(status.equals("预占用")){
														terminalPreUsedCount++;
													}else if(status.equals("占用")){
														terminalUsedCount++;
													}else if(status.equals("空闲")||status.equals("请选择")){
														terminalNoUseCount++;
													}else{
														useOneTimeTerminalMap.put("status","其他");
														terminalOtherCount++;
													}			
												}else{
													useOneTimeTerminalMap.put("status","占用");
													terminalUsedCount++;
												}
												terminalList.add(useOneTimeTerminalMap);
											} else if (resultCount == 0) {
												//未成端子
												Map<String, Object> noUseTerminalMap = new HashMap<String,Object>();
												//noUseTerminalMap=odmTerminalApp.toMap();
												noUseTerminalMap.put("name",odmTerminalApp.getValue("name"));
												noUseTerminalMap.put("id",odmTerminalApp.getValue("id"));
												noUseTerminalMap.put("status",odmTerminalApp.getValue("status"));
												String status= noUseTerminalMap.get("status").toString();
												//System.out.println(status+"**********");
												//System.out.println((status!=null&&!"".equals(status))+"+++++++++++++++++++++++++++++++++++++++++");
												if(status!=null&&!"".equals(status)){
													/*if(status.equals("其他")){
														terminalOtherCount++;
													}else if(status.equals("废弃")){
														terminalDiscardCount++;
													}else*/
													if(status.equals("故障/损坏")){
														terminalBreakCount++;
													}
													/*else if(status.equals("部分使用")){
														terminalPartUseCount++;
													}else if(status.equals("备用")){
														terminalReadyUseCount++;
													}else if(status.equals("在用")){
														terminalUsingCount++;
													}*/
													else if(status.equals("预占用")){
														terminalPreUsedCount++;
													}else if(status.equals("占用")){
														terminalUsedCount++;
													}else if(status.equals("空闲")||status.equals("请选择")||status.equals("")){
														terminalNoUseCount++;
													}else{
														noUseTerminalMap.put("status","其他");
														terminalOtherCount++;
													}			
												}else{
													
													noUseTerminalMap.put("status","空闲");
													terminalNoUseCount++;
												}
												terminalList.add(noUseTerminalMap);
												
											}
										}
										
									}
									quickSort.sort(terminalList,"name");
									resultMap.put("terminalList", terminalList);
									resultMap.put("terminalCount",terminalList.size());
									odmTerminalList.add(resultMap);
								}
							}
						}
					}

					ApplicationEntity[] terminalAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(currentEntity,"Terminal",AssociatedType.CHILD, "networkresourcemanage");
					List<Map<String,Object>> terminalList = new ArrayList<Map<String,Object>>();
					if(terminalAppArrs!=null&&terminalAppArrs.length>0){
						for (ApplicationEntity terminalApp : terminalAppArrs) {
							//获取端子关联的纤芯
							ApplicationEntity[] fiberCoreArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp,"FiberCore",AssociatedType.LINK, "networkresourcemanage");
							//获取端子关联的尾纤
							ApplicationEntity[] PigtailedFiberArrs = structureCommonService.getStrutureSelationsApplicationEntity(terminalApp,"PigtailedFiber",AssociatedType.LINK, "networkresourcemanage");
							if(fiberCoreArrs != null || PigtailedFiberArrs != null) {
								int resultCount = fiberCoreArrs.length + PigtailedFiberArrs.length;
								if(resultCount == 1) {
									Map<String,Object> useOneTimeTerminalMap = new HashMap<String,Object>();
									//useOneTimeTerminalMap = terminalApp.toMap();
									useOneTimeTerminalMap.put("name",terminalApp.getValue("name"));
									useOneTimeTerminalMap.put("id",terminalApp.getValue("id"));
									useOneTimeTerminalMap.put("status",terminalApp.getValue("status"));
									String status= useOneTimeTerminalMap.get("status").toString();
									if(status!=null&&!"".equals(status)){
									/*	if(status.equals("其他")){
											terminalOtherCount++;
										}else if(status.equals("废弃")){
											terminalDiscardCount++;
										}else*/
										if(status.equals("故障/损坏")){
											terminalBreakCount++;
										}
										/*else if(status.equals("部分使用")){
											terminalPartUseCount++;
										}else if(status.equals("备用")){
											terminalReadyUseCount++;
										}else if(status.equals("在用")){
											terminalUsingCount++;
										}*/
										else if(status.equals("预占用")){
											terminalPreUsedCount++;
										}else if(status.equals("占用")){
											terminalUsedCount++;
										}else if(status.equals("空闲")||status.equals("请选择")||status.equals("")){
											terminalNoUseCount++;
										}else{
											useOneTimeTerminalMap.put("status","其他");
											terminalOtherCount++;
										}		
									}else{
										useOneTimeTerminalMap.put("status","占用");
										terminalUsedCount++;
									}
								} else if (resultCount == 0) {
									//未成端子
									Map<String, Object> noUseTerminalMap = new HashMap<String,Object>();
									noUseTerminalMap=terminalApp.toMap();
									noUseTerminalMap.put("name",terminalApp.getValue("name"));
									noUseTerminalMap.put("id",terminalApp.getValue("id"));
									noUseTerminalMap.put("status",terminalApp.getValue("status"));
									String status= noUseTerminalMap.get("status").toString();
									if(status!=null&&!"".equals(status)){
										/*if(status.equals("其他")){
											terminalOtherCount++;
										}else if(status.equals("废弃")){
											terminalDiscardCount++;
										}else*/ 
										if(status.equals("故障/损坏")){
											terminalBreakCount++;
										}
										/*else if(status.equals("部分使用")){
											terminalPartUseCount++;
										}else if(status.equals("备用")){
											terminalReadyUseCount++;
										}else if(status.equals("在用")){
											terminalUsingCount++;
										}*/
										else if(status.equals("预占用")){
											terminalPreUsedCount++;
										}else if(status.equals("占用")){
											terminalUsedCount++;
										}else if(status.equals("空闲")||status.equals("请选择")){
											terminalNoUseCount++;
										}else{
											noUseTerminalMap.put("status","其他");
											terminalOtherCount++;
										}			
									}else{
										noUseTerminalMap.put("status","空闲");
										terminalNoUseCount++;
									}
									terminalList.add(noUseTerminalMap);
									
								}
							}
							
						}
						Map<String,Object> resultMap = new HashMap<String,Object>();
						quickSort.sort(terminalList,"name");
						resultMap.put("name","noHasOdm");
						resultMap.put("terminalList", terminalList);
						resultMap.put("terminalCount",terminalList.size());
						odmTerminalList.add(resultMap);
					}	
				}
			}
		   quickSort.sort(odmTerminalList,"name");
		   Map<String,Object> mapCount = new HashMap<String,Object>();
		   mapCount.put("terminalNoUseCount",terminalNoUseCount);
		   mapCount.put("terminalUsedCount", terminalUsedCount);
		   mapCount.put("terminalPreUsedCount",terminalPreUsedCount);
		 //  mapCount.put("terminalUsingCount",terminalUsingCount);
		 //  mapCount.put("terminalReadyUseCount",terminalReadyUseCount);
		 //  mapCount.put("terminalPartUseCount",terminalPartUseCount);
		   mapCount.put("terminalBreakCount",terminalBreakCount);
		  // mapCount.put("terminalDiscardCount",terminalDiscardCount);
		   mapCount.put("terminalOtherCount",terminalOtherCount);
		   Map<String,Object> mapName = new HashMap<String,Object>();
		   mapName.put("terminalNoUseCount","空闲");
		   mapName.put("terminalUsedCount", "占用");
		   mapName.put("terminalPreUsedCount","预占用");
		  // mapName.put("terminalUsingCount","在用");
		 //  mapName.put("terminalReadyUseCount","备用");
		 //  mapName.put("terminalPartUseCount","部分使用");
		   mapName.put("terminalBreakCount","故障/损坏");
		 //  mapName.put("terminalDiscardCount","废弃");
		   mapName.put("terminalOtherCount","其他");
		   Map<String,Object> resultMaps = new HashMap<String,Object>();
		   resultMaps.put("odmTerminalList",odmTerminalList);
		   resultMaps.put("mapCount",mapCount);
		   resultMaps.put("mapName",mapName);
		   HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(resultMaps);
			log.info("退出getOdmAndTerminalMessageAction()，返回结果result="+result);
			response.getWriter().write(result);
		}catch(Exception e){
			log.error("退出getOdmAndTerminalMessageAction()，发生异常");
			e.printStackTrace();

		}
		
	}


	//资源管理的关联信息 
	public String associatedResourceAction(){
		log.info("进入associatedResourceAction，资源管理的关联信息");
		ApplicationEntity entity = structureCommonService.getSectionEntity(this.currentEntityType,this.currentEntityId);
		String[] selationsArray = structureCommonService.getStrutureSelationsArray(entity,AssociatedType.CHILD,"networkresourcemanage");
		String[] LinkArray = structureCommonService.getStrutureSelationsArray(entity,AssociatedType.LINK,"networkresourcemanage");
		//获取父子关系的数据
		childAssociatedResourcMap = new HashMap<String, List<Map<String, Object>>>(); 
		this.infoChildMapChinese =  new HashMap<String, Object>();
		this.childAssociatedResourcCount =  new HashMap<String, Object>();
		for(String st:selationsArray){
			ApplicationEntity[] childRecursion = structureCommonService.getAppArrsByRecursion(
					entity, new String[]{st}, AssociatedType.CHILD, "networkresourcemanage");
			if(childRecursion != null){
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				for(ApplicationEntity ap:childRecursion){
					list.add(ap.toMap());
				}
				quickSort.sort(list,"name");
				childAssociatedResourcMap.put(st, list);
				childAssociatedResourcCount.put(st, list.size());
				this.associatedResourcCount = this.associatedResourcCount + list.size();
			}else{
				childAssociatedResourcMap.put(st, null);
				childAssociatedResourcCount.put(st, 0);
				this.associatedResourcCount = this.associatedResourcCount + 0;
			}
			//获取父子关系的数据字典
			List<BasicEntity> entry;
			try {
				entry = dictionary.getEntry(st + ",networkResourceDefination", SearchScope.OBJECT, "");
				if(entry != null && !entry.isEmpty()) {
					this.infoChildMapChinese.put(st, entry.get(0).getValue("display"));
					
				}
			} catch (EntryOperationException e) {
				log.error("获取"+st+"中文字典失败，可能该字典不存在");
				e.printStackTrace();
			}
		}
		//获取关联关系的数据
		linkAssociatedResourcMap = new HashMap<String, List<Map<String, Object>>>(); 
		this.infoLinkMapChinese =  new HashMap<String, Object>();
		this.linkAssociatedResourcCount =  new HashMap<String, Object>();
		for(String st:LinkArray){
			ApplicationEntity[] linkRecursion = structureCommonService.getAppArrsByRecursion(
					entity, new String[]{st}, AssociatedType.LINK, "networkresourcemanage");
			if(linkRecursion != null){
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				for(ApplicationEntity ap:linkRecursion){
					list.add(ap.toMap());
				}
				quickSort.sort(list,"name");
				linkAssociatedResourcMap.put(st, list);
				linkAssociatedResourcCount.put(st, list.size());
				this.associatedResourcCount = this.associatedResourcCount + list.size();
				
			}else{
				linkAssociatedResourcMap.put(st, null);
				linkAssociatedResourcCount.put(st, 0);
				this.associatedResourcCount = this.associatedResourcCount + 0;
			}
			//获取关联关系的数据字典
			List<BasicEntity> entry;
			try {
				entry = dictionary.getEntry(st + ",networkResourceDefination", SearchScope.OBJECT, "");
				if(entry != null && !entry.isEmpty()) {
					this.infoLinkMapChinese.put(st, entry.get(0).getValue("display"));
					
				}
			} catch (EntryOperationException e) {
				log.error("获取"+st+"中文字典失败，可能该字典不存在");
				e.printStackTrace();
			}
		}
		log.info("退出associatedResourceAction,返回结果result=success");
		return "success";
	}
	/**
	 * 
	 * @description: 上传附件 保存
	 * @author：     
	 * @return void     
	 * @date：2012-6-1 下午04:00:39
	 */
	public void addAttachMentAction(){
		log.info("进入addAttachMentAction()，上传附件 保存");
		log.info("进入addAttachMentAction()，attachmentParentEntityType="+attachmentParentEntityType+",attachmentParentEntityId="+attachmentParentEntityId);
		try {
			//获取图片的父级entity
			BasicEntity attachmentParentEntity = physicalresService.getPhysicalresById(this.attachmentParentEntityType, Long.parseLong(this.attachmentParentEntityId));
			ApplicationEntity attachmentParentAppEntity = ApplicationEntity.changeFromEntity(attachmentParentEntity);
			
			ApplicationEntity attachmentAppEntity = moduleLibrary.getModule("Attachment").createApplicationEntity();
			//attachmentAppEntity.setValue("id", Unique.nextLong("attachment_id"));
			attachmentAppEntity.setValue("id", structureCommonService.getEntityPrimaryKey("Attachment"));
			attachmentAppEntity.setValue("name", this.attachmentOldName);
			attachmentAppEntity.setValue("uuidname", this.attachmentName);
			/*用structure保存ae*/
			structureCommonService.saveInfoEntity(attachmentAppEntity, "networkresourcemanage");
			//physicalresService.addPhysicalres(photoAppEntity);
			
			structureCommonService.createAssociatedRelation(attachmentParentAppEntity, attachmentAppEntity, AssociatedType.CLAN, "networkresourcemanage");
			log.info("退出addAttachMentAction(),上传附件 保存成功");
		} catch (NumberFormatException e) {
			log.error("退出addAttachMentAction()，发生异常");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description: 修改附件
	 * @author：     
	 * @return void     
	 * @date：2012-6-1 下午04:01:37
	 */
	public void updateAttachMentAction(){
		log.info("进入updateAttachMentAction()，修改附件");
		log.info("进入updateAttachMentAction()，currentAttachmentEntityType="+currentAttachmentEntityType+",attachmentParentEntityId="+currentAttachmentEntityId);
		try {
			//旧图片的enitty
			BasicEntity oldAttachmentEntity = physicalresService.getPhysicalresById(this.currentAttachmentEntityType, Long.parseLong(this.currentAttachmentEntityId));
			ApplicationEntity oldAttachmentAppEntity = ApplicationEntity.changeFromEntity(oldAttachmentEntity);
			
			oldAttachmentAppEntity.setValue("name", this.attachmentOldName);//旧附件名
			oldAttachmentAppEntity.setValue("uuidname", this.attachmentName);//新附件uuid名
			
			physicalresService.updatePhysicalres(oldAttachmentAppEntity, this.currentAttachmentEntityType);
			log.info("退出updateAttachMentAction(),修改附件成功");
		} catch (RuntimeException e) {
			log.info("退出updateAttachMentAction(),发生异常");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description: 获取模块 端子默认布局设置
	 * @author：
	 * @return     
	 * @return String     
	 * @date：Jul 23, 2012 4:02:48 PM
	 */
	public void getOdmandterminallayoutAction(){
		log.info("进入getOdmandterminallayoutAction()，获取模块 端子默认布局设置");
		String result= "";
		List<BasicEntity> aeList=physicalresService.getResourceEntityList("Odmterminallayout",this.currentEntityId,this.currentEntityType);
		if(aeList!=null&&aeList.size()>0){
			ApplicationEntity ae = ApplicationEntity.changeFromEntity(aeList.get(0));
			this.odmandterminallayoutMap=ae.toMap();
			 GsonBuilder builder = new GsonBuilder();
			 Gson gson = builder.create();
		     result = gson.toJson(this.odmandterminallayoutMap);
		}else{
			this.odmandterminallayoutMap=new HashMap<String,Object>();
			BasicEntity be=physicalresService.getPhysicalresById(this.currentEntityType,Long.valueOf(this.currentEntityId));
			//System.out.println(be.toMap()+"----");
			ApplicationEntity et = ApplicationEntity.changeFromEntity(be);
			ApplicationEntity[] odmAes=structureCommonService.getStrutureSelationsApplicationEntity(et,"ODM",AssociatedType.CHILD,"networkresourcemanage");
			//System.out.println(odmAes.length);
			if(odmAes!=null&&odmAes.length>0){
				ApplicationEntity at = odmAes[0];
				ApplicationEntity[] tAes=structureCommonService.getStrutureSelationsApplicationEntity(at,"Terminal",AssociatedType.CHILD,"networkresourcemanage");
				if(tAes!=null&&tAes.length>0){
					this.odmandterminallayoutMap.put("maxCount",tAes.length);
				}else{
					if("DDF".equals(this.currentEntityType)){
						this.odmandterminallayoutMap.put("maxCount","8");
					}else{
						this.odmandterminallayoutMap.put("maxCount","12");
					}
					
				}
			}else{
				if("DDF".equals(this.currentEntityType)){
					this.odmandterminallayoutMap.put("maxCount","8");
				}else{
					this.odmandterminallayoutMap.put("maxCount","12");
				}
			}
			 GsonBuilder builder = new GsonBuilder();
			 Gson gson = builder.create();
		     result = gson.toJson(this.odmandterminallayoutMap);
		}
		 HttpServletResponse response = ServletActionContext.getResponse();
		 response.setCharacterEncoding("utf-8");
		 response.setContentType("text/html");
		 try {
			 log.info("退出getOdmandterminallayoutAction(),返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getOdmandterminallayoutAction(),返回结果result="+result+"失败");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description: 保存更新 模块端子默认布局设置
	 * @author：     
	 * @return void     
	 * @date：Jul 23, 2012 4:19:03 PM
	 */
	public void updateOdmandterminallayoutAction(){
		log.info("进入updateOdmandterminallayoutAction()，保存更新 模块端子默认布局设置");
		String result="error";
		ApplicationEntity appEntity = ActionHelper.getApplicationEntity("Odmterminallayout");
		if(appEntity!=null){
			String resourceId = appEntity.getValue("resourceid").toString();
			String resourceType = appEntity.getValue("resourcetype").toString();
			if(resourceId!=null && !"".equals(resourceId)&&resourceType!=null && !"".equals(resourceType)){
				List<BasicEntity> aeList=physicalresService.getResourceEntityList("Odmterminallayout",resourceId,resourceType);
				 if(!(aeList!=null&&aeList.size()>0)){
					 appEntity.setValue("id",Long.valueOf(String.valueOf(structureCommonService.getEntityPrimaryKey("Odmterminallayout"))));
					 int i = physicalresService.addPhysicalres(appEntity);
					 if(i>0){
						 result=appEntity.getValue("id").toString();
					 }
				 }else{
					 int i=physicalresService.updatePhysicalres(appEntity,"Odmterminallayout");
						if(i>0){
							result=appEntity.getValue("id").toString();
						} 
				 }
			}
			
		}
		 HttpServletResponse response = ServletActionContext.getResponse();
		 response.setCharacterEncoding("utf-8");
		 response.setContentType("text/html");
			
		 GsonBuilder builder = new GsonBuilder();
		 Gson gson = builder.create();
	     result = gson.toJson(result);
		 try {
			log.info("退出updateOdmandterminallayoutAction(),返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出updateOdmandterminallayoutAction(),返回结果result="+result+"失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @description: 根据字母查询entity类型
	 * @author：     
	 * @return void     
	 * @date：Aug 28, 2012 2:27:18 PM
	 */
	public void getEntityChineseByFirstLetterAction(){
		log.info("进入getEntityChineseByFirstLetterAction()，根据字母查询entity类型");
		String result="";
		List<BasicEntity> beList = resourceDictionaryService.getResourceDictionary("dn","%,networkResourceDefination","attrDefine","{'display':'%'}");
		this.resultList = new ArrayList<Map<String,Object>>();
		String type="";
		String chineseType="";
		if(beList!=null){
			for (BasicEntity b : beList) {		
				type=b.getValue("rdn");
				chineseType=b.getValue("attrDefine");
				chineseType=chineseType.substring(chineseType.indexOf(":")+2,chineseType.length()-2);
				String chinese= GetChineseFirstLetter.getFirstLetter(chineseType);
				if(chinese.equals(this.firstLetter)){
					if(!"null".equals(type)){
						Map<String, Object> typeMap = new HashMap<String, Object>();
						typeMap.put("type",type);
						typeMap.put("chineseType",chineseType);
						resultList.add(typeMap);
					}
				}
			}
		}
		if(resultList!=null&&resultList.size()>0){
			quickSort.sort(resultList, "chineseType");
			GsonBuilder builder = new GsonBuilder();
			 Gson gson = builder.create();
		     result = gson.toJson(resultList);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出getEntityChineseByFirstLetterAction(),返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getEntityChineseByFirstLetterAction(),返回结果result="+result+"失败");
			e.printStackTrace();
		}	
	}

	/*根据aetg组获取entityChinese*/
	public void getEntityChineseByAetgAction(){
		log.info("进入getEntityChineseByAetgAction()，根据aetg组获取entityChinese");
		String result="";
		this.allTypeMapList = new ArrayList<Map<String,Object>>();
		if(this.aetgName!=null&&!"".equals(this.aetgName)){
			String[] aetNames=structureCommonService.getAetNameOfAetg(this.aetgName,"networkresourcemanage");
			if(aetNames!=null&&!"".equals(this.aetgName)){
				for(String aetName:aetNames){
					List<BasicEntity> entry = null;
					Map<String, Object> typeMap = new HashMap<String, Object>();
					try {
						entry = dictionary.getEntry(aetName + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							typeMap.put("type", aetName);
							String display = entry.get(0).getValue("display").toString();
					        typeMap.put("chineseType", display);
							this.allTypeMapList.add(typeMap);
						}
					} catch (EntryOperationException e) {
						log.error("获取"+aetName+"中文字典失败，可能该字典不存在");
						e.printStackTrace();
						if(entry != null && !entry.isEmpty()) {
							typeMap.put("type", aetName);
							typeMap.put("chineseType", aetName);
							this.allTypeMapList.add(typeMap);
						}
					}
				}
				if(this.allTypeMapList!=null&&this.allTypeMapList.size()>0){
					//quickSort.sort(this.allTypeMapList, "chineseType");
					GsonBuilder builder = new GsonBuilder();
					 Gson gson = builder.create();
				     result = gson.toJson(this.allTypeMapList);
				}
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出getEntityChineseByAetgAction(),返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getEntityChineseByAetgAction(),返回结果result="+result+"失败");
			e.printStackTrace();
		}	
	}
	/**
	 * 
	 * @description: 取得网络资源全部entity中文
	 * @author：     
	 * @return void     
	 * @date：Aug 29, 2012 3:15:35 PM
	 */
	public void getAllEntityChineseAction(){
		log.info("进入getAllEntityChineseAction()，取得网络资源全部entity中文");
		String result="";
		List<String> allTypeList = structureCommonService.getAllEntityTypes("networkresourcemanage");
		this.allTypeMapList = new ArrayList<Map<String,Object>>();
		for (String type : allTypeList) {
			List<BasicEntity> entry = null;
			Map<String, Object> typeMap = new HashMap<String, Object>();
			try {
				entry = dictionary.getEntry(type + ",networkResourceDefination" ,SearchScope.OBJECT, "");
				if(entry != null && !entry.isEmpty()) {
					typeMap.put("type", type);
					//typeMap.put("chineseType", entry.get(0).getValue("display"));
					String display = entry.get(0).getValue("display").toString();
					if("ALL".equals(this.firstLetter)){
						
				        typeMap.put("chineseType", display);
					}else{
						String chinese= GetChineseFirstLetter.getFirstLetter(display);
				        typeMap.put("chineseType", chinese+":"+display);
					}
					
					this.allTypeMapList.add(typeMap);
				}
			} catch (EntryOperationException e) {
				log.error("获取"+type+"中文字典失败，可能该字典不存在");
				e.printStackTrace();
				if(entry != null && !entry.isEmpty()) {
					typeMap.put("type", type);
					typeMap.put("chineseType", type);
					this.allTypeMapList.add(typeMap);
				}
			}
		}
		if(allTypeMapList!=null&&this.allTypeMapList.size()>0){
			quickSort.sort(allTypeMapList, "chineseType");
			GsonBuilder builder = new GsonBuilder();
			 Gson gson = builder.create();
		     result = gson.toJson(this.allTypeMapList);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出getAllEntityChineseAction(),返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getAllEntityChineseAction(),返回结果result="+result+"失败");
			e.printStackTrace();
		}	
	}
	
	/**
	 * 
	 * @description: 为选择起点资源，关联资源而搜索资源
	 * @author：     
	 * @return void     
	 * @date：Sep 3, 2012 9:51:51 AM
	 */
	public void searchResourceForChoosenResourceAction(){
		log.info("进入searchResourceForChoosenResourceAction()，为选择起点资源，关联资源而搜索资源");
		String result="";
		if((this.selectRangeResChosenType != null && !"请选择".equals(this.selectRangeResChosenType))||(this.selectResChosenType != null && !"请选择".equals(this.selectResChosenType))){
			BasicEntity areaEntity=null;
			if(areaId!=null&&!"".equals(areaId)){
				areaEntity =physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
				ApplicationEntity areaAentity = ApplicationEntity.changeFromEntity(areaEntity);
				this.searchResourceMapList = new ArrayList<Map<String,Object>>();
				if(this.selectRangeResChosenType != null && !"请选择".equals(this.selectRangeResChosenType)) {  //起点资源
					if("GeneralBaseStation".equals(this.selectRangeResChosenType)||"GeneralRoomContainer".equals(this.selectRangeResChosenType)){
						String[] aetNames = this.structureCommonService.getAetNameOfAetg(this.selectRangeResChosenType,"networkresourcemanage");
						for(String aetName:aetNames){
							String[] parentTypes = this.structureCommonService.getAssociatedAetName(aetName, AssociatedType.PARENT,"networkresourcemanage");
							if(parentTypes!=null && parentTypes.length>0){
								/*if(parentTypes[0].equals("Sys_Area")){
									List<Map<String,Object>> searchResult=this.structureCommonService.getStrutureSelationsApplicationMap(areaAentity,this.selectRangeResChosenType, AssociatedType.CHILD, "networkresourcemanage");
									if(searchResult!=null && searchResult.size()>0){
										String text = "";
										for(Map<String,Object> map:searchResult){
											if(map.get(this.searchConditionType)!=null){
												text =map.get(this.searchConditionType).toString();
												//System.out.println(text);
												if(text.contains(this.searchConditionText)){
													this.searchResourceMapList.add(map);
												}
											}
										}
									}
								}else{*/
									List<BasicEntity> searchResourceBeList = physicalresService.getPhysicalresByCondition(aetName, this.searchConditionType, this.searchConditionText,"indistinctMatch");
									if(searchResourceBeList!=null && searchResourceBeList.size()>0 ){
										String areaName=areaAentity.getValue("name");
										for(BasicEntity be : searchResourceBeList){
											ApplicationEntity ae=physicalresService.searchParentAreaResourceForSrc(ApplicationEntity.changeFromEntity(be),areaName,null);
											if(ae!=null){
												this.searchResourceMapList.add(be.toMap());
											}
										}
									}
								//}
							}
						}
						
					}else{
						String[] parentTypes = this.structureCommonService.getAssociatedAetName(this.selectRangeResChosenType, AssociatedType.PARENT,"networkresourcemanage");
						if(parentTypes!=null && parentTypes.length>0){
						/*	if(parentTypes[0].equals("Sys_Area")){
								List<Map<String,Object>> searchResult=this.structureCommonService.getStrutureSelationsApplicationMap(areaAentity,this.selectRangeResChosenType, AssociatedType.CHILD, "networkresourcemanage");
								if(searchResult!=null && searchResult.size()>0){
									String text = "";
									for(Map<String,Object> map:searchResult){
										if(map.get(this.searchConditionType)!=null){
											text =map.get(this.searchConditionType).toString();
										//	System.out.println(text);
											if(text.contains(this.searchConditionText)){
												this.searchResourceMapList.add(map);
											}
										}
										
									}
								}
							}else{*/
								List<BasicEntity> searchResourceBeList = physicalresService.getPhysicalresByCondition(this.selectRangeResChosenType, this.searchConditionType, this.searchConditionText,"indistinctMatch");
								if(searchResourceBeList!=null && searchResourceBeList.size()>0 ){
									String areaName=areaAentity.getValue("name");
									for(BasicEntity be : searchResourceBeList){
										ApplicationEntity ae=physicalresService.searchParentAreaResourceForSrc(ApplicationEntity.changeFromEntity(be),areaName,null);
										if(ae!=null){
											this.searchResourceMapList.add(be.toMap());
										}
									}
								}
							//}
						}
					}					
				}else{ //关联资源
					
					String linkType = this.selectResChosenType.substring(this.selectResChosenType.indexOf("_")+1, this.selectResChosenType.length());
					this.selectResChosenType = this.selectResChosenType.substring(0,this.selectResChosenType.indexOf("_"));
					String[] parentTypes=null;
					AssociatedType associate=null;
					if("link".equals(linkType)){
						//parentTypes = this.structureCommonService.getAssociatedAetName(this.selectResChosenType, AssociatedType.LINK,"networkresourcemanage");
						associate = AssociatedType.LINK;
					}else{
						//parentTypes = this.structureCommonService.getAssociatedAetName(this.selectResChosenType, AssociatedType.PARENT,"networkresourcemanage");
						associate = AssociatedType.CHILD;
					}
					/*if(parentTypes!=null && parentTypes.length>0){
						boolean flag=false;
						for(String parentType:parentTypes){
							//System.out.println(parentType+"-+-+");
							if(parentType.equals("Sys_Area")){
								flag=true;
								List<Map<String,Object>> searchResult=this.structureCommonService.getStrutureSelationsApplicationMap(areaAentity,this.selectResChosenType,associate, "networkresourcemanage");
								if(searchResult!=null && searchResult.size()>0){
									String text = "";
									for(Map<String,Object> map:searchResult){
										if(map.get(this.searchConditionType)!=null){
											text =map.get(this.searchConditionType).toString();
											if(text.contains(this.searchConditionText)){
												this.searchResourceMapList.add(map);
											}
										}
									}
								}
								break;
							}
						}
						if(!flag){*/
							List<BasicEntity> searchResourceBeList = physicalresService.getPhysicalresByCondition(this.selectResChosenType, this.searchConditionType, this.searchConditionText,"indistinctMatch");
							if(searchResourceBeList!=null && searchResourceBeList.size()>0 ){
								String areaName=areaAentity.getValue("name");
								for(BasicEntity be : searchResourceBeList){
									ApplicationEntity ae=physicalresService.searchParentAreaResourceForSrc(ApplicationEntity.changeFromEntity(be),areaName,linkType);
									if(ae!=null){
										this.searchResourceMapList.add(be.toMap());
									}
								}
							}
					/*	}
						
					}*/
					
				}
			}
		}
		if(this.searchResourceMapList!=null&&this.searchResourceMapList.size()>0){
			quickSort.sort(this.searchResourceMapList, this.searchConditionType);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			result = gson.toJson(this.searchResourceMapList);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");

		try {
			log.info("退出searchResourceForChoosenResourceAction(),返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出searchResourceForChoosenResourceAction(),返回结果result="+result+"失败");
			e.printStackTrace();
		}	
			
	}
	//首页递归搜索资源
	public void searchResourceByConditionAction(){
		log.info("进入searchResourceByConditionAction()，首页递归搜索资源");
		String result="";
		if(this.selectResType!=null){
			ApplicationEntity areaAentity=null;
			if(areaId!=null&&!"".equals(areaId)){
				BasicEntity areaEntity =physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
				areaAentity = ApplicationEntity.changeFromEntity(areaEntity);
			}
			this.searchResourceMapList = new ArrayList<Map<String,Object>>();
			if("yes".equals(this.isAetg)){
				String[] aetNames=null;
				if("Search_4_ResWithLatLon".equals(this.selectResType)){ //默认搜索
					aetNames=this.structureCommonService.getAetNameOfAetg("Search_8_ResWithLatLon", "networkresourcemanage");
					List<String> aetList=null;
					if(aetNames!=null && aetNames.length>0){//对默认搜索的aetName 一定顺序
						aetList = new ArrayList<String>();
						for(String aetName:aetNames){
							aetList.add(aetName);
							if("Station".equals(aetName)){
								String[] aeNames=this.structureCommonService.getAetNameOfAetg("QuickSearch_4_Basestation", "networkresourcemanage");
								if(aeNames!=null&&aeNames.length>0){
									for(String ae:aeNames){
										aetList.add(ae);
									}
								}
							}
							
						}
					}
					if(aetList!=null&&aetList.size()>0){
						for(String aetName:aetList){
							//List<Map<String,Object>> searchResult=null;
						//	if(aetName.indexOf("BaseStation")>=0){//对基站类特殊处理
								List<BasicEntity> searchResourceBeList = physicalresService.getPhysicalresByCondition(aetName, this.searchConditionType, this.searchConditionText,"indistinctMatch");
								if(searchResourceBeList!=null && searchResourceBeList.size()>0 ){
									String areaName=areaAentity.getValue("name");
									for(BasicEntity be : searchResourceBeList){
										ApplicationEntity ae=physicalresService.searchParentAreaResourceForSrc(ApplicationEntity.changeFromEntity(be),areaName,null);
										if(ae!=null){
											this.searchResourceMapList.add(be.toMap());
										}
									}
								}
						/*	}else{//八大类带经纬度资源
								searchResult=this.structureCommonService.getStrutureSelationsApplicationMap(areaAentity,aetName, AssociatedType.CHILD, "networkresourcemanage");
								if(searchResult!=null && searchResult.size()>0){
									String text = "";
									for(Map<String,Object> map:searchResult){
										if(map.get(this.searchConditionType)!=null){
											text =map.get(this.searchConditionType).toString();
											if(text.contains(this.searchConditionText)){
												this.searchResourceMapList.add(map);
											}
										}
									}
								}
							}*/
						}
					}
				}else{
					aetNames=this.structureCommonService.getAetNameOfAetg(this.selectResType, "networkresourcemanage");
					if(aetNames!=null&&aetNames.length>0){
						for(String aetName:aetNames){
							String[] parentTypes=null;
							AssociatedType associate=null;
							if("link".equals(linkType)){
								//parentTypes = this.structureCommonService.getAssociatedAetName(aetName, AssociatedType.LINK,"networkresourcemanage");
								associate = AssociatedType.LINK;
							}else{
								//parentTypes = this.structureCommonService.getAssociatedAetName(aetName, AssociatedType.PARENT,"networkresourcemanage");
								associate = AssociatedType.CHILD;
							}
						/*	if(parentTypes!=null && parentTypes.length>0){
								boolean flag=false;
								for(String parentType:parentTypes){
									if(parentType.equals("Sys_Area")){
										flag=true;
										List<Map<String,Object>> searchResult=this.structureCommonService.getStrutureSelationsApplicationMap(areaAentity,aetName,associate, "networkresourcemanage");
										if(searchResult!=null && searchResult.size()>0){
											String text = "";
											for(Map<String,Object> map:searchResult){
												if(map.get(this.searchConditionType)!=null){
													text =map.get(this.searchConditionType).toString();
													if(text.contains(this.searchConditionText)){
														this.searchResourceMapList.add(map);
													}
												}
											}
											break;
										}
										
									}
								}
								if(!flag){*/
									List<BasicEntity> searchResourceBeList = physicalresService.getPhysicalresByCondition(aetName, this.searchConditionType, this.searchConditionText,"indistinctMatch");
									if(searchResourceBeList!=null && searchResourceBeList.size()>0 ){
										String areaName=areaAentity.getValue("name");
										for(BasicEntity be : searchResourceBeList){
											ApplicationEntity ae=physicalresService.searchParentAreaResourceForSrc(ApplicationEntity.changeFromEntity(be),areaName,linkType);
											if(ae!=null){
												this.searchResourceMapList.add(be.toMap());
											}
										}
									}
							/*	}
							}*/
						}
					}
				}
			}else{
				String[] parentTypes=null;
				AssociatedType associate=null;
				if("link".equals(linkType)){
					//parentTypes = this.structureCommonService.getAssociatedAetName(this.selectResType, AssociatedType.LINK,"networkresourcemanage");
					associate = AssociatedType.LINK;
				}else{
					//parentTypes = this.structureCommonService.getAssociatedAetName(this.selectResType, AssociatedType.PARENT,"networkresourcemanage");
					associate = AssociatedType.CHILD;
				}
			/*	if(parentTypes!=null && parentTypes.length>0){
					boolean flag=false;
					for(String parentType:parentTypes){
						if(parentType.equals("Sys_Area")){
							flag=true;
							List<Map<String,Object>> searchResult=this.structureCommonService.getStrutureSelationsApplicationMap(areaAentity,this.selectResType,associate, "networkresourcemanage");
							if(searchResult!=null && searchResult.size()>0){
								String text = "";
								for(Map<String,Object> map:searchResult){
									if(map.get(this.searchConditionType)!=null){
										text =map.get(this.searchConditionType).toString();
										if(text.contains(this.searchConditionText)){
											this.searchResourceMapList.add(map);
										}
									}
								}
								break;
							}
							
						}
					}
					if(!flag){*/
						List<BasicEntity> searchResourceBeList = physicalresService.getPhysicalresByCondition(this.selectResType, this.searchConditionType, this.searchConditionText,"indistinctMatch");
						if(searchResourceBeList!=null && searchResourceBeList.size()>0 ){
							String areaName=areaAentity.getValue("name");
							for(BasicEntity be : searchResourceBeList){
								ApplicationEntity ae=physicalresService.searchParentAreaResourceForSrc(ApplicationEntity.changeFromEntity(be),areaName,linkType);
								if(ae!=null){
									this.searchResourceMapList.add(be.toMap());
								}
							}
						}
					/*}
				}*/
			}
		}
		if(this.searchResourceMapList!=null&&this.searchResourceMapList.size()>0){
			quickSort.sort(this.searchResourceMapList, this.searchConditionType);
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			result = gson.toJson(this.searchResourceMapList);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");

		try {
			log.info("退出searchResourceByConditionAction(),返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出searchResourceByConditionAction(),返回结果result="+result+"失败");
			e.printStackTrace();
		}	
	}
	/**
	 * 
	 * @description: 获取搜索的entity或组中文
	 * @author：
	 * @return     
	 * @return vois     
	 * @date：Sep 5, 2012 10:15:24 AM
	 */
	public void getEntityChineseForSearchAction(){
		log.info("进入getEntityChineseForSearchAction(),获取搜索的entity或组中文");
		String result="";
		this.allTypeMapList = new ArrayList<Map<String,Object>>();
		if(this.aetgName!=null&&!"".equals(this.aetgName)){
			String[] aetNames=structureCommonService.getAetNameOfAetg(this.aetgName,"networkresourcemanage");
			if(aetNames!=null&&!"".equals(this.aetgName)){
				for(String aetName:aetNames){
					List<BasicEntity> entry = null;
					Map<String, Object> typeMap = new HashMap<String, Object>();
					try {
						entry = dictionary.getEntry(aetName + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							typeMap.put("type", aetName);
							String display = entry.get(0).getValue("display").toString();
					        typeMap.put("chineseType", display);
					        typeMap.put("isAetg","no");
					        typeMap.put("linkType","parent");
							this.allTypeMapList.add(typeMap);
							if("Station".equals(aetName)){
								typeMap = new HashMap<String, Object>();
								typeMap.put("type", "QuickSearch_4_Basestation");
						        typeMap.put("chineseType", "基站");	
						        typeMap.put("isAetg","yes");
						        typeMap.put("linkType","parent");
								this.allTypeMapList.add(typeMap);
							}
						}
					} catch (EntryOperationException e) {
						log.error("获取"+aetName+"中文字典失败，可能该字典不存在");
						e.printStackTrace();
						if(entry != null && !entry.isEmpty()) {
							typeMap.put("type", aetName);
							typeMap.put("chineseType", aetName);
							this.allTypeMapList.add(typeMap);
						}
					}
				}
				Map<String, Object>	typeMap = new HashMap<String, Object>();
				typeMap.put("type", "QuickSearch_4_OptDisFac");
		        typeMap.put("chineseType", "光交接设施");	
		        typeMap.put("isAetg","yes");
		        typeMap.put("linkType","parent");
				this.allTypeMapList.add(typeMap);
				typeMap = new HashMap<String, Object>();
				typeMap.put("type", "QuickSearch_4_CommPriDev");
		        typeMap.put("chineseType", "通信主设备");		
		        typeMap.put("isAetg","yes");
		        typeMap.put("linkType","parent");
				this.allTypeMapList.add(typeMap);
				typeMap = new HashMap<String, Object>();
				typeMap.put("type", "QuickSearch_4_BoardDev");
		        typeMap.put("chineseType", "板件");		
		        typeMap.put("isAetg","yes");
		        typeMap.put("linkType","parent");
				this.allTypeMapList.add(typeMap);
				typeMap = new HashMap<String, Object>();
				typeMap.put("type", "QuickSearch_4_PowDev");
		        typeMap.put("chineseType", "动力设备资源");	
		        typeMap.put("isAetg","yes");
		        typeMap.put("linkType","parent");
				this.allTypeMapList.add(typeMap);
				typeMap = new HashMap<String, Object>();
				typeMap.put("type", "QuickSearch_4_TransmDev");
		        typeMap.put("chineseType", "传输设备资源");	
		        typeMap.put("isAetg","yes");
		        typeMap.put("linkType","parent");
				this.allTypeMapList.add(typeMap);
				typeMap = new HashMap<String, Object>();
				typeMap.put("type", "QuickSearch_4_EvirMonitDev");
		        typeMap.put("chineseType", "环境监控设备资源");
		        typeMap.put("isAetg","yes");
		        typeMap.put("linkType","parent");
				this.allTypeMapList.add(typeMap);
				typeMap = new HashMap<String, Object>();
				typeMap.put("type", "QuickSearch_4_OthCompAccDev");
		        typeMap.put("chineseType", "其他关键零部件/耗材资源");	
		        typeMap.put("isAetg","yes");
		        typeMap.put("linkType","parent");
				this.allTypeMapList.add(typeMap);
				typeMap = new HashMap<String, Object>();
				typeMap.put("type", "QuickSearch_4_FrameDev");
		        typeMap.put("chineseType", "机架/箱/柜");
		        typeMap.put("isAetg","yes");
		        typeMap.put("linkType","parent");
				this.allTypeMapList.add(typeMap);
				typeMap = new HashMap<String, Object>();
				typeMap.put("type", "QuickSearch_4_LogicNetPlDev");
		        typeMap.put("chineseType", "连接类资源(逻辑资源)-管线网");	
		        typeMap.put("isAetg","yes");
		        typeMap.put("linkType","link");
				this.allTypeMapList.add(typeMap);
				typeMap = new HashMap<String, Object>();
				typeMap.put("type", "QuickSearch_4_LogicNetFbDev");
		        typeMap.put("chineseType", "连接类资源(逻辑资源)-光缆网");	
		        typeMap.put("isAetg","yes");
		        typeMap.put("linkType","link");
				this.allTypeMapList.add(typeMap);
				typeMap = new HashMap<String, Object>();
				typeMap.put("type", "QuickSearch_4_LogicNetOpDev");
		        typeMap.put("chineseType", "连接类资源(逻辑资源)-光路网");	
		        typeMap.put("isAetg","yes");
		        typeMap.put("linkType","link");
				this.allTypeMapList.add(typeMap);
				typeMap = new HashMap<String, Object>();
				typeMap.put("type", "QuickSearch_4_LogicNetTransmDev");
		        typeMap.put("chineseType", "连接类资源(逻辑资源)-传输网");	
		        typeMap.put("isAetg","yes");
		        typeMap.put("linkType","link");
				this.allTypeMapList.add(typeMap);
				if(this.allTypeMapList!=null&&this.allTypeMapList.size()>0){
					//quickSort.sort(this.allTypeMapList, "chineseType");
					GsonBuilder builder = new GsonBuilder();
					 Gson gson = builder.create();
				     result = gson.toJson(this.allTypeMapList);
				}
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出getEntityChineseForSearchAction(),返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getEntityChineseForSearchAction(),返回结果result="+result+"失败");
			e.printStackTrace();
		}	
	}
	/***
	 * 
	 * @description: 集中调度跳转资源浏览 获取扁平化信息
	 * @author：     
	 * @return void     
	 * @date：Oct 17, 2012 10:34:24 AM
	 */
	public void getResourcesForSrcForGisDispatchAction(){
		log.info("进入getResourcesForSrcForGisDispatchAction，集中调度跳转资源浏览 获取扁平化信息");
		String result="";
		this.searchResourceMapList= new ArrayList<Map<String,Object>>();
		BasicEntity be = this.structureCommonService.getSectionEntity(this.currentEntityType,this.currentEntityId);
		ApplicationEntity ae = ApplicationEntity.changeFromEntity(be);
		if(this.aetgName!=null&&!"".equals(this.aetgName)){
			String[] aetNames=null;
			if("noAetg".equals(this.aetgName)){
				aetNames=this.structureCommonService.getAssociatedAetName(this.currentEntityType, AssociatedType.CHILD, "networkresourcemanage");
			}else{
				aetNames=structureCommonService.getAetNameOfAetg(this.aetgName,"networkresourcemanage");
			}	
			if(aetNames!=null&&!"".equals(this.aetgName)){
				String display ="";
				for(String aetName:aetNames){
					List<BasicEntity> entry = null;
					display=aetName;
					try {
						entry = dictionary.getEntry(aetName + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							display = entry.get(0).getValue("display").toString();
						}
					} catch (EntryOperationException e) {
						log.error("获取"+aetName+"中文字典失败，可能该字典不存在");
						e.printStackTrace();
					}
					List<ApplicationEntity> aeList = new ArrayList<ApplicationEntity>();
					ApplicationEntity[] aeList1 =structureCommonService.getAppArrsByRecursionForSrcSameType(ae,aetName, "networkresourcemanage");
					ApplicationEntity[] linkAppArrs1 = structureCommonService.getStrutureSelationsApplicationEntity(
							ae, aetName, AssociatedType.LINK, "networkresourcemanage");	
					if(aeList1!=null && aeList1.length>0){
						List<ApplicationEntity> list = Arrays.asList(aeList1);
						aeList.addAll(list);
					}
					if(linkAppArrs1!=null && linkAppArrs1.length>0){
						List<ApplicationEntity> linkAppArrsList = Arrays.asList(linkAppArrs1);
						aeList.addAll(linkAppArrsList);
					}
					Map<String,Object> resultMap = null;
					if(aeList != null && aeList.size() >0){
						for(ApplicationEntity ap:aeList){
							resultMap = new HashMap<String,Object>();
							resultMap.put("id",ap.getValue("id"));
							resultMap.put("_entityType",ap.getValue("_entityType"));
							if(ap.getValue("name")!=null&&!"".equals(ap.getValue("name"))){
								resultMap.put("name",ap.getValue("name"));
							}else{
								resultMap.put("name",ap.getValue("label"));
							}
							resultMap.put("chineseName",display);
							this.searchResourceMapList.add(resultMap);
						}
					}
				}
			}
		}
		if(this.searchResourceMapList!=null&&this.searchResourceMapList.size()>0){
			//quickSort.sort(this.searchResourceMapList,"name");
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			result = gson.toJson(this.searchResourceMapList);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出getResourcesForSrcForGisDispatchAction(),返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getResourcesForSrcForGisDispatchAction(),返回结果result="+result+"失败");
			e.printStackTrace();
		}	
	}
	/**
	 * 
	 * @description: 获得特定专业组各组的实例数量
	 * @author：     
	 * @return void     
	 * @date：Oct 26, 2012 12:06:22 PM
	 */
	public void getAetgCountMessForFlattenInfoAction(){
		log.info("进入getAetgCountMessForFlattenInfoAction(),获得特定专业组各组的实例数量");
		String result="";
		BasicEntity be = this.structureCommonService.getSectionEntity(this.currentEntityType,this.currentEntityId);
		ApplicationEntity ae = ApplicationEntity.changeFromEntity(be);
		this.allTypeMapList = new ArrayList<Map<String,Object>>();
		if(this.aetgName!=null&&!"".equals(this.aetgName)){
			if(this.aetgName.indexOf("_Power")>=0 || this.aetgName.indexOf("_Wireless")>=0 ||this.aetgName.indexOf("_LogicNet")>=0){
				String[] aetNames=null;
				String[] aetgNames=null;
				if(this.aetgName.indexOf("_Power")>=0){
					String[] aetgNames1 = {aetgName+"_ACSys",aetgName+"_DCSys",aetgName+"_UPSSys",aetgName+"_EarthConnEtc"};
					aetgNames=aetgNames1;
				}else if(this.aetgName.indexOf("_Wireless")>=0){
					String[] aetgNames1 = {aetgName+"_GSM",aetgName+"_TD",aetgName+"_AntennaSys"};
					aetgNames=aetgNames1;
				}else if(this.aetgName.indexOf("_LogicNet")>=0){
					String[] aetgNames1 = {aetgName+"_Pl",aetgName+"_Fb",aetgName+"_Op",aetgName+"_Transm"};
					aetgNames=aetgNames1;
				}
				for(String aetg:aetgNames){
					resultList= new ArrayList<Map<String,Object>>();
					aetNames=structureCommonService.getAetNameOfAetg(aetg,"networkresourcemanage");
					Map<String,Object> resultMap = null;
					if(aetNames!=null&&!"".equals(this.aetgName)){
						int count=0;
						for(String aetName:aetNames){
							List<ApplicationEntity> aeList = new ArrayList<ApplicationEntity>();
							ApplicationEntity[] aeList1 =structureCommonService.getAppArrsByRecursionForSrcSameType(ae,aetName, "networkresourcemanage");
							ApplicationEntity[] linkAppArrs1 = structureCommonService.getStrutureSelationsApplicationEntity(
									ae, aetName, AssociatedType.LINK, "networkresourcemanage");	
							if(aeList1!=null && aeList1.length>0){
								List<ApplicationEntity> list = Arrays.asList(aeList1);
								aeList.addAll(list);
							}
							if(linkAppArrs1!=null && linkAppArrs1.length>0){
								List<ApplicationEntity> linkAppArrsList = Arrays.asList(linkAppArrs1);
								aeList.addAll(linkAppArrsList);
							}
							if(aeList != null && aeList.size() >0){
								count += aeList.size();
							}
						}
						resultMap = new HashMap<String,Object>();
						resultMap.put("type",aetg);
						resultMap.put("count",count);
						allTypeMapList.add(resultMap);
					}
				}
				if(allTypeMapList!=null&&allTypeMapList.size()>0){
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					result = gson.toJson(this.allTypeMapList);
				}	
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出getAetgCountMessForFlattenInfoAction(),返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getAetgCountMessForFlattenInfoAction(),返回结果result="+result+"失败");
			e.printStackTrace();
		}		
	}
	
	/**
	 * 
	 * @description: 扁平化信息组entity中文
	 * @author：     
	 * @return void     
	 * @date：Sep 6, 2012 1:49:34 PM
	 */
	public void getEntityChineseForFlattenInfoAction(){
		log.info("进入getEntityChineseForFlattenInfoAction(),扁平化信息组entity中文");
		String result="";
		this.allTypeMapList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		if(this.aetgName!=null&&!"".equals(this.aetgName)){
			if(this.aetgName.indexOf("_Power")>=0 || this.aetgName.indexOf("_Wireless")>=0 ||this.aetgName.indexOf("_LogicNet")>=0){
				String[] aetNames=null;
				String[] aetgNames=null;
				if(this.aetgName.indexOf("_Power")>=0){
					String[] aetgNames1 = {aetgName+"_ACSys",aetgName+"_DCSys",aetgName+"_UPSSys",aetgName+"_EarthConnEtc"};
					aetgNames=aetgNames1;
				}else if(this.aetgName.indexOf("_Wireless")>=0){
					String[] aetgNames1 = {aetgName+"_GSM",aetgName+"_TD",aetgName+"_AntennaSys"};
					aetgNames=aetgNames1;
				}else if(this.aetgName.indexOf("_LogicNet")>=0){
					String[] aetgNames1 = {aetgName+"_Pl",aetgName+"_Fb",aetgName+"_Op",aetgName+"_Transm"};
					aetgNames=aetgNames1;
				}
				for(String aetg:aetgNames){
					resultList= new ArrayList<Map<String,Object>>();
					aetNames=structureCommonService.getAetNameOfAetg(aetg,"networkresourcemanage");
					if(aetNames!=null&&!"".equals(this.aetgName)){
						for(String aetName:aetNames){
							List<BasicEntity> entry = null;
							Map<String, Object> typeMap = new HashMap<String, Object>();
							try {
								entry = dictionary.getEntry(aetName + ",networkResourceDefination" ,SearchScope.OBJECT, "");
								if(entry != null && !entry.isEmpty()) {
									typeMap.put("type", aetName);
									String display = entry.get(0).getValue("display").toString();
							        typeMap.put("chineseType", display);
									resultList.add(typeMap);
								}
							} catch (EntryOperationException e) {
								log.error("获取"+aetName+"中文字典失败,可能该字典不存在");
								e.printStackTrace();
								if(entry != null && !entry.isEmpty()) {
									typeMap.put("type", aetName);
									typeMap.put("chineseType", aetName);
									resultList.add(typeMap);
								}
							}
						}
					}
					if(resultList!=null&&resultList.size()>0){
						quickSort.sort(resultList, "chineseType");
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("aetgName",aetg);
						map.put("resultList",resultList);
						this.allTypeMapList.add(map);
					}
				}
				if(allTypeMapList!=null&&allTypeMapList.size()>0){
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					result = gson.toJson(this.allTypeMapList);
				}	
			}else{
				String[] aetNames=null;
				if("noAetg".equals(this.aetgName)){
					aetNames=this.structureCommonService.getAssociatedAetName(this.currentEntityType, AssociatedType.CHILD, "networkresourcemanage");
				}else{
					aetNames=structureCommonService.getAetNameOfAetg(this.aetgName,"networkresourcemanage");
				}	
				if(aetNames!=null&&!"".equals(this.aetgName)){
					for(String aetName:aetNames){
						List<BasicEntity> entry = null;
						Map<String, Object> typeMap = new HashMap<String, Object>();
						try {
							entry = dictionary.getEntry(aetName + ",networkResourceDefination" ,SearchScope.OBJECT, "");
							if(entry != null && !entry.isEmpty()) {
								typeMap.put("type", aetName);
								String display = entry.get(0).getValue("display").toString();
						        typeMap.put("chineseType", display);
								resultList.add(typeMap);
							}
						} catch (EntryOperationException e) {
							log.error("获取"+aetName+"中文字典失败,可能该字典不存在");
							e.printStackTrace();
							if(entry != null && !entry.isEmpty()) {
								typeMap.put("type", aetName);
								typeMap.put("chineseType", aetName);
								resultList.add(typeMap);
							}
						}
					}
				}
					
				if(resultList!=null&&resultList.size()>0){
					//quickSort.sort(this.allTypeMapList, "chineseType");
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("aetgName",this.aetgName);
					map.put("resultList",resultList);
					this.allTypeMapList.add(map);
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					result = gson.toJson(this.allTypeMapList);
				}	
			}
			
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出getEntityChineseForFlattenInfoAction,返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getEntityChineseForFlattenInfoAction,返回结果result="+result+"失败");
			e.printStackTrace();
		}		
	} 
	/**
	 * 搜索扁平化所需资源信息
	 */
	public String getResourceListForSrcAction(){
		log.info("进入getResourceListForSrcAction(),搜索扁平化所需资源信息");
		if(this.currentEntityId!=null && this.searchType!=null){
			Map<String, Object> searchedResModuleMap = ModuleProvider.getModule(this.searchType).toMap();
			Map<String,Object> orderIdMap = new HashMap<String,Object>();
			
			if(searchedResModuleMap != null) {
				this.titleMap = new HashMap<String, Object>();
				for(String key : searchedResModuleMap.keySet()) {
					try {
						List<BasicEntity> entry = null;
						if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
							//数据字典，中英文转换
							entry = dictionary.getEntry(key + "," + this.searchType + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						}
						if(entry != null && !entry.isEmpty()) {
							if(!"_entityType".equals(key) && !"_entityId".equals(key)) {
								titleMap.put(key, entry.get(0).getValue("display"));
								orderIdMap.put( entry.get(0).getValue("orderID").toString(), key);
							}
						}
					} catch (EntryOperationException e) {
						log.error("获取"+this.searchType+"属性字段"+key+"的中文字典失败，可能该字典不存在");
						e.printStackTrace();
						titleMap.put(key, key);
					}
				}
				Map<String,Object> sortedMap = quickSort.sortMap(titleMap,orderIdMap);//中文字典排序
				if(sortedMap!=null){
					titleMap = sortedMap;
				}
				this.searchResourceMapList= new ArrayList<Map<String,Object>>();
				BasicEntity be = this.structureCommonService.getSectionEntity(this.currentEntityType,this.currentEntityId);
				ApplicationEntity ae = ApplicationEntity.changeFromEntity(be);
				List<ApplicationEntity> aeList = new ArrayList<ApplicationEntity>();
				ApplicationEntity[] aeList1 =structureCommonService.getAppArrsByRecursionForSrcSameType(ae,this.searchType, "networkresourcemanage");
				ApplicationEntity[] linkAppArrs1 = null;
				if(ResourceCommon.isLinkType(this.parentEntityType)){
					linkAppArrs1 = structureCommonService.getStrutureSelationsApplicationEntity(
							ae, this.searchType, AssociatedType.LINK, "networkresourcemanage");
				}
				if(aeList1!=null && aeList1.length>0){
					List<ApplicationEntity> list = Arrays.asList(aeList1);
					aeList.addAll(list);
					this.linkType="parent";
				}
				if(linkAppArrs1!=null && linkAppArrs1.length>0){
					List<ApplicationEntity> linkAppArrsList = Arrays.asList(linkAppArrs1);
					aeList.addAll(linkAppArrsList);
					this.linkType="link";
				}
					Map<String,Object> resultMap = new HashMap<String,Object>();
					Map<String,Object> photoMap = null;
					if(aeList != null && aeList.size() >0){
						for(ApplicationEntity ap:aeList){
							/*if(!"Photo".equals(this.searchType)){
								List<Map<String,Object>> photoList = this.structureCommonService.getStrutureSelationsApplicationMap(ap, "Photo", AssociatedType.CHILD, "networkresourcemanage");
								if(photoList!=null&&photoList.size()>0){
									photoMap = new HashMap<String,Object>();
									String resourceName="";
									if(ap.getValue("name")!=null){
										resourceName= ap.getValue("name");
									}else{
										resourceName= ap.getValue("label");
									}
									photoMap.put("resourceName",resourceName);
									String uuidname="";
									for(Map<String,Object> mp:photoList){
										uuidname=mp.get("uuidname")+"";
										uuidname=uuidname.substring(uuidname.indexOf("."),uuidname.length());
										mp.put("name",mp.get("name")+uuidname);
									}
									photoMap.put("photoList", photoList);
									this.photoMapList.add(photoMap);
								}
							}*/
							
							//(获取app map)由于数据库为空时，get不出属性，所以要进行设置
							resultMap = ResourceCommon.applicationEntityConvertMap(ap);
							//将null值转换为空串值
							resultMap = ResourceCommon.mapRecombinationToString(resultMap);
							for(String key:resultMap.keySet()){
								resultMap.put(key, ResourceCommon.dateTimeFormat(resultMap.get(key)));
							}
							//生成被查询资源的map(已进行关联资源的过滤)
							sortedMap = quickSort.sortMap(resultMap,orderIdMap);//排序
							if(sortedMap!=null){
								resultMap=sortedMap;
							}
							
							//System.out.println(resultMap+"....");
							this.searchResourceMapList.add(resultMap);
						}
					}
			}
			if(this.searchResourceMapList!=null&&this.searchResourceMapList.size()>0){
				quickSort.sort(this.searchResourceMapList,"name");
			}
			//System.out.println(titleMap);
			log.info("退出getResourceListForSrcAction,返回结果result=success");	
			return "success";
		}else{
			log.info("退出getResourceListForSrcAction,返回结果result=error");	
			return "error";
		}			
					
	}
	
	/**
	 * 直接录入添加一个物理资源
	 */
	public void addPhysicalresDirectlyAction() {
		log.info("进入addPhysicalresDirectlyAction(),直接录入添加一个物理资源");
		String result="";
		try {
			ApplicationEntity appEntity = ActionHelper.getApplicationEntity(this.addedResEntityType);
			appEntity.setValue("id",structureCommonService.getEntityPrimaryKey(this.addedResEntityType));
			//System.out.println(appEntity.toMap());

			int resultCount = structureCommonService.saveInfoEntity(appEntity, "networkresourcemanage");
			//System.out.println(resultCount);
			if(resultCount>0){
				//添加资源维护记录
				String resName = "";
				if(appEntity.getValue("name") != null){
					resName = appEntity.getValue("name");
				}else{
					resName = appEntity.getValue("label");
				}
				ResourceMaintenance maintenance = new ResourceMaintenance();
				maintenance.setBiz_module(OperationType.NETWORK);
				maintenance.setOp_cause(this.op_cause);
				maintenance.setOp_scene(this.op_scene);
				maintenance.setOp_category(OperationType.RESOURCEINSERT);
				maintenance.setRes_id((Long)appEntity.getValue("id"));
				maintenance.setRes_type(appEntity.getValue("_entityType").toString());
				maintenance.setRes_keyinfo(resName);
				networkResourceMaintenanceService.insertResourceMaintenanceRecordsBySystemForces(maintenance);
				if(this.addedResParentEntityType!=null && this.addedResParentEntityId!=null){
					//获取当前被添加的entity
					BasicEntity addedEntity = physicalresService.getPhysicalresById(this.addedResEntityType, Long.parseLong(appEntity.getValue("id").toString()));
					ApplicationEntity addedAppEntity = ApplicationEntity.changeFromEntity(addedEntity);
					
					//获取当前被添加的entity的父entity
					BasicEntity addedParentEntity = physicalresService.getPhysicalresById(this.addedResParentEntityType, Long.parseLong(this.addedResParentEntityId));
					ApplicationEntity addedParentAppEntity = ApplicationEntity.changeFromEntity(addedParentEntity);
				
					//建立新资源与其父entity的关系
					if("link".equals(this.linkType)){
						structureCommonService.createAssociatedRelation(addedParentAppEntity, addedAppEntity, AssociatedType.LINK, "networkresourcemanage");
						
					}else{
						structureCommonService.createAssociatedRelation(addedParentAppEntity, addedAppEntity, AssociatedType.CLAN, "networkresourcemanage");
					}
					
					HttpServletResponse response = ServletActionContext.getResponse();
					response.setCharacterEncoding("utf-8");
					response.setContentType("text/html");
					
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					if (resultCount > 0) {
						result = gson.toJson(appEntity.getValue("id")+"");
					}
					
				}else{
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();
					result = gson.toJson(appEntity.getValue("id")+"");
				}
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			try {
				log.info("退出addPhysicalresDirectlyAction,返回结果result="+result);
				response.getWriter().write(result);
			} catch (IOException e) {
				log.error("退出addPhysicalresDirectlyAction,返回结果result="+result+"失败");
				e.printStackTrace();
			}		
			
		} catch (NumberFormatException e) {
			log.error("退出addPhysicalresDirectlyAction,发生NumberFormatException异常");
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
	/**
	 * 
	 * @description: 资源全路径
	 * @author：     
	 * @return void     
	 * @date：Sep 21, 2012 11:45:30 AM
	 */
	public void getParentResourceListAction(){
		log.info("进入getParentResourceListAction，资源全路径");
		String result="";
		BasicEntity be=physicalresService.getPhysicalresById(this.currentEntityType,Long.valueOf(this.currentEntityId));
		if(be!=null){
			ApplicationEntity ae = ApplicationEntity.changeFromEntity(be);
			List<Map<String,Object>> resultList=physicalresService.searchParentResourceForSrc(ae,null);
			if(resultList==null || resultList.isEmpty()){
				resultList=physicalresService.searchParentResourceForSrc(ae,"link");
			}
			if(resultList!=null && !resultList.isEmpty()){
				for(Map<String,Object> map:resultList){
					List<BasicEntity> entry = null;
					try {
						entry = dictionary.getEntry(map.get("_entityType") + ",networkResourceDefination" ,SearchScope.OBJECT, "");
						if(entry != null && !entry.isEmpty()) {
							String display = entry.get(0).getValue("display").toString();
							map.put("chineseType", display);		
						}
					} catch (EntryOperationException e) {
						e.printStackTrace();
						map.put("chineseType", map.get("_entityType")+"");
					}
				}
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				result = gson.toJson(resultList);
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出getParentResourceListAction,返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getParentResourceListAction,返回结果result="+result+"失败");
			e.printStackTrace();
		}	
	}
	/**
	 * 
	 * @description: 显示面板 取得板件
	 * @author：     
	 * @return void     
	 * @date：Nov 13, 2012 3:17:41 PM
	 */
	public void getEquipBoardForShowPanelAction(){
		log.info("进入getEquipBoardForShowPanelAction，显示面板 取得板件");
		String result="";
		ApplicationEntity curAe=this.structureCommonService.getSectionEntity(this.currentEntityType,this.currentEntityId);
		List<Map<String,Object>> resultList = null;
		if(curAe!=null){
			ApplicationEntity[] egAes = null;
			if("BaseStation_GSM".equals(this.currentEntityType)){	
				ApplicationEntity[] pefAes = this.structureCommonService.getStrutureSelationsApplicationEntity(curAe,"PrimaryEquipFrame_GSM", AssociatedType.CHILD,"networkresourcemanage");
				if(pefAes!=null&&pefAes.length>0){
					resultList = new ArrayList<Map<String,Object>>();
					List<Map<String,Object>> list= null;
					for(ApplicationEntity ae : pefAes){
						egAes = this.structureCommonService.getStrutureSelationsApplicationEntity(ae,"EquipBoard_GSM", AssociatedType.CHILD,"networkresourcemanage");
						if(egAes!=null && egAes.length>0){
							list = new ArrayList<Map<String,Object>>();
							Map<String,Object> mp = null;
							for(ApplicationEntity egAe:egAes){
								mp = new HashMap<String,Object>();
								mp.put("model",egAe.getValue("model")+"");
								mp.put("setupslot", egAe.getValue("setupslot")+"");
								mp.put("paneltext",egAe.getValue("model")+""+egAe.getValue("setupslot")+"");
								mp.put("name", egAe.getValue("name")+"");
								mp.put("id", egAe.getValue("id")+"");
								mp.put("type", egAe.getValue("_entityType"));
								list.add(mp);
							}
						}
						if(list!=null && list.size()>0){
							quickSort.sort(list,"paneltext");
							Map<String,Object> map = new HashMap<String,Object>();
							map.put("name", ae.getValue("name")+"");
							map.put("id", ae.getValue("id")+"");
							map.put("type", ae.getValue("_entityType"));
							map.put("list",list);
							resultList.add(map);
						}
					}
				}
			}else if("PrimaryEquipFrame_GSM".equals(this.currentEntityType)){
				egAes = this.structureCommonService.getStrutureSelationsApplicationEntity(curAe, "EquipBoard_GSM", AssociatedType.CHILD, "networkresourcemanage");
				if(egAes!=null && egAes.length>0){
					resultList = new ArrayList<Map<String,Object>>();
					Map<String,Object> mp = null;
					for(ApplicationEntity ae:egAes){
						mp = new HashMap<String,Object>();
						mp.put("model",ae.getValue("model")+"");
						mp.put("setupslot", ae.getValue("setupslot")+"");
						mp.put("paneltext",ae.getValue("model")+""+ae.getValue("setupslot")+"");
						mp.put("name", ae.getValue("name")+"");
						mp.put("id", ae.getValue("id")+"");
						mp.put("type", ae.getValue("_entityType"));
						resultList.add(mp);
					}
				}
				if(resultList!=null && resultList.size()>0){
					quickSort.sort(resultList,"paneltext");
				}
			}
		
		}
		if(resultList!=null && resultList.size()>0){
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			result = gson.toJson(resultList);			
		}
		
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			log.info("退出getEquipBoardForShowPanelAction,返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getEquipBoardForShowPanelAction,返回结果result="+result+"失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @description: 批量增加DDF模块端子
	 * @author：
	 * @return     
	 * @return String     
	 * @date：2012-4-19 上午11:25:46
	 */
	public void addDDFOdmTerminalLotAction(){
		log.info("进入addDDFOdmTerminalLotAction， 批量增加DDF模块端子");
	    try {
	    	char TerminalNum1 = 0;
			char TerminalNum2 = 0;
			int TerminalNumLength =0;
			if(!"".equals(startTerminalNumber) && !"".equals(endTerminalNumber) && startTerminalNumber!=null && endTerminalNumber!=null){
		    	if((startTerminalNumber.toCharArray()!=null&&startTerminalNumber.toCharArray().length>1)||(endTerminalNumber.toCharArray()!=null&&endTerminalNumber.toCharArray().length>1)){	
		    		TerminalNum1=(char)Integer.parseInt(startTerminalNumber);
		    		TerminalNum2=(char)Integer.parseInt(endTerminalNumber);	
		    		TerminalNumLength =Integer.parseInt(endTerminalNumber)-Integer.parseInt(startTerminalNumber);
		    	}else{
		    		TerminalNum1 = startTerminalNumber.toCharArray()[0];
		    		TerminalNum2 = endTerminalNumber.toCharArray()[0];
		    		TerminalNumLength = TerminalNum2 - TerminalNum1;
		    	}
			}
			//int TerminalNumLength = TerminalNum2 - TerminalNum1;//端子开始编号和结束编号间隔长度
			//System.out.println(TerminalNumLength+"++++");
			if(!"".equals(startOdmNumber) && !"".equals(endOdmNumber)&&startOdmNumber!=null&&endOdmNumber!=null){//有模块设置, 先保存odm再保存端子
				char odmNum1 = 0;
				char odmNum2 = 0;
				int odmNumLength=0;;
		    	if((startOdmNumber.toCharArray()!=null && startOdmNumber.toCharArray().length>1)||(endOdmNumber.toCharArray()!=null && endOdmNumber.toCharArray().length>1)){
		    		odmNum1=(char)Integer.parseInt(startOdmNumber);
		    		odmNum2=(char)Integer.parseInt(endOdmNumber);
		    		odmNumLength=Integer.parseInt(endOdmNumber)-Integer.parseInt(startOdmNumber);
		    	}else{
		    		odmNum1 = startOdmNumber.toCharArray()[0];
		    		odmNum2 = endOdmNumber.toCharArray()[0];
		    		odmNumLength=odmNum2-odmNum1;
		    	}
				//int odmNumLength=odmNum2-odmNum1; //模块开始编号和结束编号间隔长度
				for(int i=0;i<=odmNumLength;i++){//批量增加
					ApplicationEntity appEntity = ModuleProvider.getModule("DDM").createApplicationEntity();
					//appEntity.setValue("id", Unique.nextLong("odm_id"));
					appEntity.setValue("id",structureCommonService.getEntityPrimaryKey("DDM"));
					String label="";
					if(startOdmNumber.toCharArray().length>1||endOdmNumber.toCharArray().length>1){
						if(Integer.valueOf(odmNum1)+i>9){
							label=String.valueOf((Integer.valueOf(odmNum1)+i));
						}else{
							if(startOdmNumber.indexOf("0")!=0){
								label=String.valueOf((Integer.valueOf(odmNum1)+i));
							}else{
								label="0"+String.valueOf((Integer.valueOf(odmNum1)+i));
							}	
						}
						
						
					}else{
						label=String.valueOf(((char)(odmNum1+i)));
					}
					
					appEntity.setValue("label",label);
					appEntity.setValue("odmType","");
					appEntity.setValue("capacity","");
					int status=structureCommonService.saveInfoEntity(appEntity,"networkresourcemanage"); //保存模块信息
					if(status==1){
						BasicEntity addedBasicEntity=physicalresService.getPhysicalresById("DDM",Long.valueOf(appEntity.getValue("id").toString()));
						ApplicationEntity addAppEntity = ApplicationEntity.changeFromEntity(addedBasicEntity);
						BasicEntity parentBasicEntity=physicalresService.getPhysicalresById(this.currentEntityType,Long.valueOf(this.currentEntityId));
						ApplicationEntity parentAppEntity = ApplicationEntity.changeFromEntity(parentBasicEntity);
					    status = structureCommonService.createAssociatedRelation(parentAppEntity, addAppEntity, AssociatedType.CLAN,"networkresourcemanage");//模块与当前资源创建父子关系
					    if(status==1){
					    	if(!"".equals(startTerminalNumber) && !"".equals(endTerminalNumber) && startTerminalNumber!=null && endTerminalNumber!=null){//保存模块中端子信息
								 for(int j=0;j<=TerminalNumLength;j++){//批量增加
									 ApplicationEntity terminalAppEntity = null;
									 String name="";
									
									if(startTerminalNumber.toCharArray().length>1||endTerminalNumber.toCharArray().length>1){
										if(Integer.valueOf(TerminalNum1)+j>9){
											name=String.valueOf((Integer.valueOf(TerminalNum1)+j));
										}else{
											if(startTerminalNumber.indexOf("0")!=0){
												name=String.valueOf((Integer.valueOf(TerminalNum1)+j));
											}else{
												name="0"+String.valueOf((Integer.valueOf(TerminalNum1)+j));
											}
											
										}
										
									}else{
											name=String.valueOf(((char)(TerminalNum1+j)));
									}
									 if(useOdmName.equals("true")){//使用模块名作为前缀
										 if(useLinklabel.equals("true")){//使用连接符
											 if(linklabel.equals("straight")){//直
												 name=label+"-"+name;
											 }else if(linklabel.equals("curve")){//曲
												 name=label+"~"+name;
											 }else if(linklabel.equals("slant")){//斜
												 name=label+"/"+name;
											 }
										 }else{//不使用连接符
											 name=label+name;
										 } 
									 }
									 String tname = name;
									 for(int index=0;index<=3;index++){
										 if(tLinkLabel.equals("straight")){//直
											 name=tname+"-";
										 }else if(tLinkLabel.equals("curve")){//曲
											 name=tname+"~";
										 }else if(tLinkLabel.equals("slant")){//斜
											 name=tname+"/";
										 }else{
											 name=tname;
										 }
										 if(index==0){
											 name=name+this.leftUpTerminalNumber;
										 }else if(index==1){
											 name=name+this.rightUpTerminalNumber;
										 }else if(index==2){
											 name=name+this.leftDownTerminalNumber;
										 }else if(index==3){
											 name=name+this.rightDownTerminalNumber;
										 }
										 terminalAppEntity = ModuleProvider.getModule("DDFTerminal").createApplicationEntity();
										 terminalAppEntity.setValue("name",name);
										 //terminalAppEntity.setValue("id",Unique.nextLong("terminal_id"));
										 terminalAppEntity.setValue("id",structureCommonService.getEntityPrimaryKey("DDFTerminal"));
										 terminalAppEntity.setValue("label","");
										 terminalAppEntity.setValue("status","");
										 status = structureCommonService.saveInfoEntity(terminalAppEntity,"networkresourcemanage");//保存端子信息
										 if(status==1){
											 addedBasicEntity=physicalresService.getPhysicalresById("DDFTerminal",Long.valueOf(terminalAppEntity.getValue("id").toString()));
											 addAppEntity = ApplicationEntity.changeFromEntity(addedBasicEntity);
											 parentBasicEntity=physicalresService.getPhysicalresById("DDM",Long.valueOf(appEntity.getValue("id").toString()));
											 parentAppEntity = ApplicationEntity.changeFromEntity(parentBasicEntity);
											 structureCommonService.createAssociatedRelation(parentAppEntity, addAppEntity, AssociatedType.CLAN,"networkresourcemanage");//创建模块与端子关系
										 }
									 }
									 
								 }
					    	}
					    }
					}
				}
			}else if(!"".equals(startTerminalNumber) && !"".equals(endTerminalNumber) && startTerminalNumber!=null && endTerminalNumber!=null){//无（ODM）模块设置,在当前资源直接添加端子
				 
				 for(int i=0;i<=TerminalNumLength;i++){
					 String name="";
						
						if(startTerminalNumber.toCharArray().length>1||endTerminalNumber.toCharArray().length>1){
								name=String.valueOf((Integer.valueOf(TerminalNum1)+i));
						}else{
								name=String.valueOf(((char)(TerminalNum1+i)));
						}
					 ApplicationEntity terminalAppEntity = null;
					// System.out.println(name+"++++++++++++++++++");
					 String tname = name;
					 for(int index=0;index<=3;index++){
						 if(tLinkLabel.equals("straight")){//直
							 name=tname+"-";
						 }else if(tLinkLabel.equals("curve")){//曲
							 name=tname+"~";
						 }else if(tLinkLabel.equals("slant")){//斜
							 name=tname+"/";
						 }else{
							 name=tname;
						 }
						 if(index==0){
							 name=name+this.leftUpTerminalNumber;
						 }else if(index==1){
							 name=name+this.rightUpTerminalNumber;
						 }else if(index==2){
							 name=name+this.leftDownTerminalNumber;
						 }else if(index==3){
							 name=name+this.rightDownTerminalNumber;
						 }
						 terminalAppEntity = ModuleProvider.getModule("DDFTerminal").createApplicationEntity();
						 terminalAppEntity.setValue("name",name);
						 terminalAppEntity.setValue("id",structureCommonService.getEntityPrimaryKey("DDFTerminal"));
						 terminalAppEntity.setValue("label","");
						 terminalAppEntity.setValue("status","");
						 int status = structureCommonService.saveInfoEntity(terminalAppEntity,"networkresourcemanage");//保存端子信息
						 if(status==1){
							 BasicEntity addedBasicEntity=physicalresService.getPhysicalresById("DDFTerminal",Long.valueOf(terminalAppEntity.getValue("id").toString()));
							 ApplicationEntity addAppEntity = ApplicationEntity.changeFromEntity(addedBasicEntity);
							 BasicEntity parentBasicEntity=physicalresService.getPhysicalresById(this.currentEntityType,Long.valueOf(this.currentEntityId));
							 ApplicationEntity parentAppEntity = ApplicationEntity.changeFromEntity(parentBasicEntity);
							 structureCommonService.createAssociatedRelation(parentAppEntity, addAppEntity, AssociatedType.CLAN,"networkresourcemanage");//当前资源与端子创建关系
						 }
					 }
				 }
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			try {
				log.info("退出addDDFOdmTerminalLotAction,返回结果result=success");
				response.getWriter().write("success");
			} catch (IOException e) {
				log.error("退出addDDFOdmTerminalLotAction,返回结果result=success失败");
				e.printStackTrace();
			}	
		} catch (Exception e) {
			log.error("退出addDDFOdmTerminalLotAction,发生异常");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @description:取得DDF的模块（ODM）及端子信息(为了面板图显示)
	 * @author：
	 * @return     
	 * @return String     
	 * @date：2012-4-23 上午10:24:00
	 */
	public void getDDFOdmAndTerminalMessageAction(){
		log.info("进入getDDFOdmAndTerminalMessageAction(),取得DDF的模块（ODM）及端子信息(为了面板图显示)");
		try{
			int terminalOtherCount=0; //其他
			int terminalBreakCount=0;//故障/损坏
			int terminalPreUsedCount=0;//预占用
			int terminalUsedCount=0;//占用
			int terminalNoUseCount=0;//空闲
			if(this.currentEntityType!=""&&this.currentEntityId!=""&&this.currentEntityType!=null&&this.currentEntityId!=null){
				BasicEntity currentBasicEntity = physicalresService.getPhysicalresById(this.currentEntityType, Integer.valueOf(this.currentEntityId));
				if(currentBasicEntity!=null){
					ApplicationEntity currentEntity = ApplicationEntity.changeFromEntity(currentBasicEntity);
					//当前资源类型为odf和FiberCrossCabinet时要取得odm相关信息及端子
						ApplicationEntity[] odmApps= structureCommonService.getStrutureSelationsApplicationEntity(currentEntity, "DDM", AssociatedType.CHILD, "networkresourcemanage");
						if(odmApps!=null&&odmApps.length>0){
							for(ApplicationEntity app:odmApps){
								Map<String,Object> resultMap = new HashMap<String,Object>();
								resultMap.put("name",app.getValue("label"));
								ApplicationEntity[] odmTerminalAppArrs = structureCommonService.getStrutureSelationsApplicationEntity(app,"DDFTerminal",AssociatedType.CHILD, "networkresourcemanage");
								if(odmTerminalAppArrs != null && odmTerminalAppArrs.length > 0) {
									List<Map<String,Object>> terminalList = new ArrayList<Map<String,Object>>();
									for (ApplicationEntity odmTerminalApp : odmTerminalAppArrs) {
											Map<String,Object> terminalMap = new HashMap<String,Object>();
											terminalMap.put("name",odmTerminalApp.getValue("name"));
											terminalMap.put("id",odmTerminalApp.getValue("id"));
											terminalMap.put("status",odmTerminalApp.getValue("status"));
											String status= terminalMap.get("status").toString();
											if(status!=null&&!"".equals(status)){
												if(status.equals("故障/损坏")){
													terminalBreakCount++;
												}
												else if(status.equals("预占用")){
													terminalPreUsedCount++;
												}else if(status.equals("占用")){
													terminalUsedCount++;
												}else if(status.equals("空闲")||status.equals("请选择")){
													terminalNoUseCount++;
												}else{
													terminalMap.put("status","其他");
													terminalOtherCount++;
												}			
											}else{
												terminalMap.put("status","空闲");
												terminalNoUseCount++;
											}
											terminalList.add(terminalMap);
									}
									quickSort.sort(terminalList,"name");
									resultMap.put("terminalList", terminalList);
									resultMap.put("terminalCount",terminalList.size());
									odmTerminalList.add(resultMap);
								}
							}
						}
				}
			}
		   quickSort.sort(odmTerminalList,"name");
		   Map<String,Object> mapCount = new HashMap<String,Object>();
		   mapCount.put("terminalNoUseCount",terminalNoUseCount);
		   mapCount.put("terminalUsedCount", terminalUsedCount);
		   mapCount.put("terminalPreUsedCount",terminalPreUsedCount);
		   mapCount.put("terminalBreakCount",terminalBreakCount);
		   mapCount.put("terminalOtherCount",terminalOtherCount);
		   Map<String,Object> mapName = new HashMap<String,Object>();
		   mapName.put("terminalNoUseCount","空闲");
		   mapName.put("terminalUsedCount", "占用");
		   mapName.put("terminalPreUsedCount","预占用");
		   mapName.put("terminalBreakCount","故障/损坏");
		   mapName.put("terminalOtherCount","其他");
		   Map<String,Object> resultMaps = new HashMap<String,Object>();
		   resultMaps.put("odmTerminalList",odmTerminalList);
		   resultMaps.put("mapCount",mapCount);
		   resultMaps.put("mapName",mapName);
		   HttpServletResponse response = ServletActionContext.getResponse();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html");
			
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			String result = gson.toJson(resultMaps);
			log.info("退出getDDFOdmAndTerminalMessageAction,返回result="+result);
			response.getWriter().write(result);
		}catch(Exception e){
			log.error("退出getDDFOdmAndTerminalMessageAction,发生异常");
			e.printStackTrace();

		}
		
	}
	/**
	 * 
	 * @description: 取得某个资源类型是否有上级资源类型（GeneralBaseStation基站五大类，区域，站址，机房）中一个
	 * @author：     
	 * @return void     
	 * @date：Nov 21, 2012 5:24:26 PM
	 */
	public void  getDirectestParentResourceTypeAction(){
		log.info("进入getDirectestParentResourceTypeAction,取得某个资源类型是否有上级资源类型（GeneralBaseStation基站五大类，区域，站址，机房）中一个");
		String result = this.physicalresService.getDirectestParentResourceType(this.selectResType);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出getDirectestParentResourceTypeAction,返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getDirectestParentResourceTypeAction,返回结果result="+result+"失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @description: 某个资源的某个类型的上级资源信息;
	 * @author：     
	 * @return void     
	 * @date：Nov 22, 2012 9:16:40 AM
	 */
	public void getDirectestParentResourceInfoAction(){
		log.info("进入getDirectestParentResourceInfoAction,某个资源的某个类型的上级资源信息");
		String result = "";
		this.resultList = new ArrayList<Map<String,Object>>();
		Map<String,Object> resultMap1 =null;
		if(this.currentEntityId!=null&&!"".equals(this.currentEntityId)){
			if(!"link".equals(this.linkType)){
				resultMap1 = this.physicalresService.getSomeParentTypeApplicationEntitysMapByIds(this.currentEntityId,this.currentEntityType,this.selectResType);
			}
			String[] ids=this.currentEntityId.split(",");
			for(String currentId:ids){
				if("link".equals(this.linkType)){
					ApplicationEntity curAe= this.structureCommonService.getSectionEntity(this.currentEntityType,currentId);
					if(curAe!=null){
						Map<String,Object> resultMap=this.physicalresService.getDirectestParentResourceInfo(curAe,this.selectResType,this.linkType);
						if(resultMap!=null&&!resultMap.isEmpty()){
							resultList.add(resultMap);
						}else{
							resultList.add(null);
						}
					}
				}else{
					if(resultMap1!=null && !resultMap1.isEmpty()){
						if(resultMap1.containsKey(currentId)){
							Map<String,Object> resultMap = (Map<String,Object>)resultMap1.get(currentId);
							if(resultMap!=null&&!resultMap.isEmpty()){
								resultList.add(resultMap);
							}else{
								resultList.add(null);
							}
						}else{
							resultList.add(null);
						}
					}else{
						resultList.add(null);
					}
					
				}
			}
			
		}
		if(this.resultList!=null && !this.resultList.isEmpty()){
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			result = gson.toJson(this.resultList);
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出getDirectestParentResourceInfoAction,返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出getDirectestParentResourceInfoAction,返回结果result="+result+"失败");
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @description: 判断基站 站址是否已有同名站
	 * @author：     
	 * @return void     
	 * @date：Jan 9, 2013 10:54:41 AM
	 */
	public void hasStationResourceRecordAction(){
		log.info("进入hasStationResourceRecordAction,判断基站 站址是否已有同名站");
		String result = "0";
		if(areaId!=null&&!"".equals(areaId)){
			BasicEntity areaEntity =physicalresService.getPhysicalresById("Sys_Area",Long.valueOf(this.areaId));
			if(areaEntity!=null){
				this.areaName = areaEntity.getValue("name");
			}
		}
		if("Station".equals(this.addedResEntityType)||this.addedResEntityType.indexOf("BaseStation")>=0){
			if(this.currentEntityId!=null &&!"".equals(this.currentEntityId)&&!"null".equals(this.currentEntityId)){
				ApplicationEntity curae = this.structureCommonService.getSectionEntity(this.addedResEntityType, this.currentEntityId);
				if(curae!=null){
					String ename = curae.getValue("name")+"";
					if(!ename.equals(this.cName)){
						List<BasicEntity> assEntityList = this.physicalresService.getPhysicalresByCondition(this.addedResEntityType, "name",this.cName, "exactMatch");
						List<ApplicationEntity> assAppList = new ArrayList<ApplicationEntity>();
						if(assEntityList != null && !assEntityList.isEmpty()) {
							ApplicationEntity ae=null;
							for (BasicEntity be : assEntityList) {
								ae = ApplicationEntity.changeFromEntity(be);
								ApplicationEntity se=physicalresService.searchParentAreaResourceForSrc(ae, areaName, null);
								if(se!=null){
									assAppList.add(ae);
								}				
							}
						}
						assEntityList=physicalresService.getNoAssociateResource(this.addedResEntityType);
						if(assEntityList != null && !assEntityList.isEmpty()) {
							ApplicationEntity ae=null;
							for (BasicEntity be : assEntityList) {
								String attr=be.getValue("name")+"";
								if(attr.equals(this.cName)){
									ae = ApplicationEntity.changeFromEntity(be);	
									assAppList.add(ae);	
								} 
							}
						}
						if(assAppList!=null && !assAppList.isEmpty()){
							result=assAppList.size()+"";
						}
					}
				}
			}else{
				List<BasicEntity> assEntityList = this.physicalresService.getPhysicalresByCondition(this.addedResEntityType, "name",this.cName, "exactMatch");
				List<ApplicationEntity> assAppList = new ArrayList<ApplicationEntity>();
				if(assEntityList != null && !assEntityList.isEmpty()) {
					ApplicationEntity ae=null;
					for (BasicEntity be : assEntityList) {
						ae = ApplicationEntity.changeFromEntity(be);
						ApplicationEntity se=physicalresService.searchParentAreaResourceForSrc(ae, areaName, null);
						if(se!=null){
							assAppList.add(ae);
						}				
					}
				}
				assEntityList=physicalresService.getNoAssociateResource(this.addedResEntityType);
				if(assEntityList != null && !assEntityList.isEmpty()) {
					ApplicationEntity ae=null;
					for (BasicEntity be : assEntityList) {
						String attr=be.getValue("name")+"";
						if(attr.equals(this.cName)){
							ae = ApplicationEntity.changeFromEntity(be);	
							assAppList.add(ae);	
						} 
					}
				}
				if(assAppList!=null && !assAppList.isEmpty()){
					result=assAppList.size()+"";
				}
			}
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		result = gson.toJson(result);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		try {
			log.info("退出hasStationResourceRecordAction,返回结果result="+result);
			response.getWriter().write(result);
		} catch (IOException e) {
			log.error("退出hasStationResourceRecordAction,返回结果result="+result+"失败");
			e.printStackTrace();
		}
	}
	
	public String getPhotoParentEntityType() {
		return photoParentEntityType;
	}

	public void setPhotoParentEntityType(String photoParentEntityType) {
		this.photoParentEntityType = photoParentEntityType;
	}

	public String getPhotoParentEntityId() {
		return photoParentEntityId;
	}

	public void setPhotoParentEntityId(String photoParentEntityId) {
		this.photoParentEntityId = photoParentEntityId;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
}
