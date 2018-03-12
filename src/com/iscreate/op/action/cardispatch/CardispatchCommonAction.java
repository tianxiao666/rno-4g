package com.iscreate.op.action.cardispatch;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.service.cardispatch.CardispatchCommonService;
import com.iscreate.op.service.cardispatch.CardispatchManageService;
import com.iscreate.plat.tools.FileHelper;
import com.iscreate.plat.tools.IdGenerator;


/**
 * 公共Action
 * cardispatchCommon_ajax
 * @author andy
 */
@SuppressWarnings({"rawtypes","unchecked","unused","hiding"})
public class CardispatchCommonAction {
	
	/************* 依赖注入 *************/
	private CardispatchManageService cardispatchManageService;
	private CardispatchCommonService cardispatchCommonService;
	
	/********* 属性 *********/
	private File[] file;
	private String fileNameString;
	private String[] fileFileName;
	private String[] fileContentType;
	//日志
	private Log log = LogFactory.getLog(CardispatchCommonAction.class);
	
	/**************** action ******************/
	
	/**
	 * 车辆车牌自动补全
	 * (请求参数) carBizId - 车辆所在组织id
	 * 				  carNumber - 车牌号
	 * (响应) (List<Map<String, String>>) 车辆集合
	 */
	public void carNumberAutoComplete () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("carBizId","carNumber");
		List<Map<String, Object>> list = cardispatchManageService.findCarDriverPairList(requestParamMap);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 非司机人员的自动补全
	 * (请求参数) name - 人名获取account
	 * (响应) (List<Map<String, String>>) 人员集合
	 */
	public void notDriverStaffAutoComplete () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("name","bizId");
		String driverName = requestParamMap.get("name");
		String bizId = requestParamMap.get("bizId");
		List<Map<String, String>> list = cardispatchManageService.getNotDriverStaffAutoComplete(driverName,bizId);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 查询所有省份
	 * (响应) (List<Map<String,String>>) 区域集合
	 */
	public void getAllProvince () {
		List<Map<String, String>> allProvince = cardispatchCommonService.getAllProvince();
		try {
			ActionUtil.responseWrite(allProvince);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据地区id获取下级地区集合
	 * (请求参数) id - 区域集合
	 * (响应) (List<Map<String,String>>) 区域集合
	 */
	public void getSubArea () {
		Map<String, String> requestParamMap = ActionUtil.getRequestParamMap("id");
		String id = requestParamMap.get("id");
		List<Map<String, String>> list = cardispatchCommonService.getSubArea(id);
		try {
			ActionUtil.responseWrite(list,false);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取上传文件并获取上传的路径的url
	 * (请求参数) file 上传的文件
	 * (响应) (String) 文件上传的路径
	 */
	public void getFileURL () {
		String carImg = "";
		String filePath = "";
		if(this.file!=null && this.file.length>0 && this.file[0]!=null){
			String savePath = ServletActionContext.getServletContext().getRealPath("");
			savePath = savePath + "/upload/cardispatch";
			for (int i = 0; i < this.fileFileName.length; i++) {
				String fileFileName = this.fileFileName[i];
				String fileType = fileFileName.substring(fileFileName.lastIndexOf("."));
				this.fileFileName[i] = IdGenerator.makeUuidString()+fileType;
			}
			List<String> saveFiles = FileHelper.saveFileByPath(savePath,this.file, this.fileFileName);
			if(saveFiles!=null && saveFiles.size() == this.file.length){
				filePath = saveFiles.get(0);
				carImg=saveFiles.get(0);
				carImg=carImg.replaceAll("\\\\", "/");
				filePath = "/"+ActionUtil.getProjectName() + "/" + carImg.substring(carImg.lastIndexOf("upload/") , carImg.length());
			}
		}
		try {
			ActionUtil.responseWrite(filePath);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 根据站址id获取所在区域的id集合
	 * (请求参数) stationId - 站址id
	 * (响应) (List<String>) 区域集合 (由下到上 - 最后是省份 )
	 */
	public void getAreaIdListByStationId () {
		String stationId = ActionUtil.getParamString("stationId");
		List<String> list = cardispatchCommonService.getAreaIdListByStationId(stationId);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据基站id获取所在区域的id集合
	 * (请求参数) baseStationId - 站址id
	 * (响应) (List<String>) 区域集合 (由下到上 - 最后是省份 )
	 */ 
	public void getAreaIdListByBaseStationId () {
		String baseStationId = ActionUtil.getParamString("baseStationId");
		String baseStationType = ActionUtil.getParamString("baseStationType");
		List<String> list = cardispatchCommonService.getAreaIdListByBaseStationIdAndType(baseStationId, baseStationType);
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据当前登录人账号获取所在区域
	 * (响应) (List<String>) 区域集合 (由下到上 - 最后是省份 )
	 */
	public void getAreaIdByLoginPerson () {
		List<String> list = cardispatchCommonService.getAreaIdByLoginPerson();
		try {
			ActionUtil.responseWrite(list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据资源Id、类型,获取资源信息
	 */
	public void getResourceInfoByType() {
		HttpServletRequest request = ServletActionContext.getRequest();
		String resId = request.getParameter("resId");
		String resType = request.getParameter("resType");
		Map<String, String> resoueceInfo = cardispatchCommonService.getResoueceInfo(resId, resType);
		try {
			ActionUtil.responseWrite(resoueceInfo);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取当前登录人的下级组织
	 */
	public void getUserDownOrg () {
		List<Map<String, String>> result_list = this.cardispatchCommonService.getUserDownOrg();
		try {
			ActionUtil.responseWrite(result_list);
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	/*********** getter setter ***********/
	public CardispatchManageService getCardispatchManageService() {
		return cardispatchManageService;
	}
	public void setCardispatchManageService(
			CardispatchManageService cardispatchManageService) {
		this.cardispatchManageService = cardispatchManageService;
	}
	public CardispatchCommonService getCardispatchCommonService() {
		return cardispatchCommonService;
	}
	public void setCardispatchCommonService(
			CardispatchCommonService cardispatchCommonService) {
		this.cardispatchCommonService = cardispatchCommonService;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public String getFileNameString() {
		return fileNameString;
	}

	public void setFileNameString(String fileNameString) {
		this.fileNameString = fileNameString;
	}

	public String[] getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String[] getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String[] fileContentType) {
		this.fileContentType = fileContentType;
	}
	
	
	
	
	
	
	
	
	
	
	
}
