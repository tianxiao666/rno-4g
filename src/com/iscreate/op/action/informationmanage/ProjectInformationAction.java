package com.iscreate.op.action.informationmanage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.iscreate.op.action.informationmanage.common.ActionUtil;
import com.iscreate.op.action.informationmanage.common.DateUtil;
import com.iscreate.op.action.informationmanage.common.ObjectUtil;
import com.iscreate.op.pojo.informationmanage.InformationProject;
import com.iscreate.op.service.informationmanage.ProjectInformationService;
import com.iscreate.plat.tools.FileHelper;


/**
 * 项目管理action
 * projectmanage
 * @author andy
 */
@SuppressWarnings( { "unused", "unchecked" })
public class ProjectInformationAction {

	/** *********** 依赖注入 ************ */
	private ProjectInformationService projectInformationService;

	/** ******* 属性 ******** */
	private Log log = LogFactory.getLog(this.getClass());
	private InformationProject project = new InformationProject();
	private File[] file;
	private String fileNameString;
	private String[] fileFileName;
	private String[] fileContentType;

	/** ************** 页面数据 ***************** */

	private Map pageData_Map = new HashMap();

	/** ******** XML 使用属性 *********** */
	private String url;

	/** *********************** Action ***************************** */

	/**
	 * 验证项目编号是否唯一
	 * 
	 * @page - 项目信息管理页面
	 * @write true : 已存在 , false : 不存在
	 */
	public void checkProjectNumberExists() {
		log.debug(" ProjectInformationAction ->> checkProjectNumberExists ");
		Map<String, String> param_map = ActionUtil.getRequestParamMap("projectNumber",
				"id");
		boolean exists = projectInformationService.checkProjectNumberExists(
				param_map.get("projectNumber"), param_map.get("id"));
		try {
			ActionUtil.responseWrite(exists);
			log.debug(" ProjectInformationAction ->> checkProjectNumberExists --> write(exists:"
							+ exists + ")");
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 查询所有项目的信息(Ajax)
	 * 
	 * @page - 项目信息管理页面
	 * @write 所有项目的信息
	 */
	public void findAllProjectInfoAjax() {
		log.debug(" ProjectInformationAction ->> findAllProjectInfoAjax ");
		Map<String, String> param_map = ActionUtil.getRequestParamMap(null);
		List<InformationProject> list = projectInformationService
				.findByParam(param_map);
		List<Map<String,Object>> result_list = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < list.size() ; i++) {
			InformationProject info = list.get(i);
			try {
				Map<String, Object> map = ObjectUtil.object2Map(info, false);
				result_list.add(map);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		try {
			ActionUtil.responseWrite(list);
			log.debug(" ProjectInformationAction ->> findAllProjectInfoAjax --> write(list:"
							+ list + ")");
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 查询单个项目的信息
	 * 
	 * @page - 查看单个项目信息页面
	 * @write 项目的信息
	 */
	public void findSingleProjectInfo() {
		log.debug(" ProjectInformationAction ->> findSingleProjectInfo ");
		Map<String, String> param_Map = ActionUtil.getRequestParamMap("id");

		try {
			Map<String, Object> project_map = projectInformationService
					.findSingleProjectInfo(param_Map);
			ActionUtil.responseWrite(project_map);
			log
					.debug(" ProjectInformationAction ->> findSingleProjectInfo --> write(project_map:"
							+ project_map.entrySet() + ")");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 添加单个项目对象的信息
	 * 
	 * @page - 项目信息页面列表 -> 查看项目信息
	 * @write - 操作是否成功
	 */
	public void saveProjectInfo() {
		log.debug(" ProjectInformationAction ->> saveProjectInfo ");
		Map<String, String> requestParamMap = ActionUtil
				.getRequestParamMapByChoiceMap("Project_save#");
		try {
			
			InformationProject informationProject = ObjectUtil.map2Object(
					requestParamMap, InformationProject.class);
			Long num = projectInformationService.txinsert(informationProject);
			ActionUtil.responseWrite(num);
			log
					.debug(" ProjectInformationAction ->> saveProjectInfo --> write(num:"
							+ num + ")");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 更新单个项目对象的信息
	 * 
	 * @param -
	 *            项目信息页面列表 -> 查看项目信息对话框
	 * @write - 修改是否成功
	 */
	public void updateProjectInfo() {
		log.debug(" ProjectInformationAction ->> updateProjectInfo ");
		Map<String, Object> requestParamMap = ActionUtil
				.getRequestParamMapByChoiceMapObject("Project_update#");
		String filePathString = "";
		try {
			Map<String,Object> whereMap = new LinkedHashMap<String, Object>();
			Long id = Long.valueOf(requestParamMap.get("id")+"");
			requestParamMap.put("id", Long.valueOf(id));
			requestParamMap.remove("id");
			Long cityId = Long.valueOf(requestParamMap.remove("cityId")+"");
			requestParamMap.put("cityId", Long.valueOf(cityId));
			Long clientOrgId = Long.valueOf(requestParamMap.remove("clientOrgId")+"");
			requestParamMap.put("clientOrgId", Long.valueOf(clientOrgId));
			Long serverOrgId = Long.valueOf(requestParamMap.remove("serverOrgId")+"");
			requestParamMap.put("serverOrgId", Long.valueOf(serverOrgId));
			String planEndDate = requestParamMap.remove("planEndDate")+"";
			requestParamMap.put("planEndDate", new StringBuffer( "to_date('" + planEndDate + "' , 'yyyy-mm-dd')" ));
			String startDate = requestParamMap.remove("startDate")+"";
			requestParamMap.put("startDate", new StringBuffer( "to_date('" + startDate + "' , 'yyyy-mm-dd')" ));
			int num = projectInformationService.updateProjectInfoById(id, requestParamMap);
			ActionUtil.responseWrite(num);
			log.debug(" ProjectInformationAction ->> updateProjectInfo --> write(num:" + num + ")" );
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 删除单个项目对象的信息
	 * 
	 * @param -
	 *            项目信息页面列表 -> 查看项目信息对话框
	 * @write - 删除是否成功
	 */
	public void deleteProjectInfo() {
		log.debug(" ProjectInformationAction ->> deleteProjectInfo ");
		Map<String, String> requestParamMap = ActionUtil
				.getRequestParamMap("id");
		Long id = Long.valueOf(requestParamMap.get("id"));

		try {
			int num = projectInformationService.txremove(id);
			ActionUtil.responseWrite(num);
			log
					.debug(" ProjectInformationAction ->> deleteProjectInfo --> write(num:"
							+ num + ")");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/** ********* getter setter ********** */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map getPageData_Map() {
		return pageData_Map;
	}

	public void setPageData_Map(Map pageData_Map) {
		this.pageData_Map = pageData_Map;
	}

	public ProjectInformationService getProjectInformationService() {
		return projectInformationService;
	}

	public void setProjectInformationService(
			ProjectInformationService projectInformationService) {
		this.projectInformationService = projectInformationService;
	}

	public InformationProject getProject() {
		return project;
	}

	public void setProject(InformationProject project) {
		this.project = project;
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
