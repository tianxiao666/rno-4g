package com.iscreate.op.action.workmanage;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iscreate.plat.workflow.serviceaccess.ServiceBean;

/**
 * 初始化工作流action
 * @author che.yd
 *
 */
public class InitWorkflowAction {
	
	private ServiceBean workFlowService;
	private HibernateTemplate hibernateTemplate;
	
	public void setWorkFlowService(ServiceBean workFlowService) {
		this.workFlowService = workFlowService;
	}
	
	

	
	
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}





	/**
	 * 初始化系统工作流数据
	 */
	public String initSystemWorkflowAction() throws Exception{
		System.out.println("invoke initSystemWorkflowAction method to initialize system workflow data------ begin --------");
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session=request.getSession();
		
		
		List list=this.hibernateTemplate.find("from FlowInfo");
		
		if(list==null || list.isEmpty()){
			//System.out.println("run code to initialize system workflow");
			String path = this.getClass().getClassLoader().getResource("").getPath();
		    String source_file = "";//源配置文件
		    if(path.indexOf("/") != -1){//windows环境
		    	 source_file = "//";
		    }else{//linux环境
		    	 source_file = "\\";
		    }
			//deploy 抢修工单流程 -----------
			String fileName="workflow"+source_file+"chuli_qiangxiu.zip";
			InputStream input=getConfigFileIn(fileName);
			ZipInputStream zip_input=new ZipInputStream(input);
			String deployId=workFlowService.deployWorkflow(zip_input,"che.yd");
			System.out.println("deployId=="+deployId);
			
			//deploy 抢修现场任务单流程 -----------
			fileName="workflow"+source_file+"chuli_qiangxiu_scene.zip";
			input=getConfigFileIn(fileName);
			zip_input=new ZipInputStream(input);
			deployId=workFlowService.deployWorkflow(zip_input,"che.yd");
			System.out.println("deployId=="+deployId);
			
			
			//deploy 抢修专家任务单流程 -----------
			fileName="workflow"+source_file+"chuli_qiangxiu_expert.zip";
			input=getConfigFileIn(fileName);
			zip_input=new ZipInputStream(input);
			deployId=workFlowService.deployWorkflow(zip_input,"che.yd");
			System.out.println("deployId=="+deployId);
			
			
			//deploy 车辆调度流程 -----------
			fileName="workflow"+source_file+"proc_cardispatch.zip";
			input=getConfigFileIn(fileName);
			zip_input=new ZipInputStream(input);
			deployId=workFlowService.deployWorkflow(zip_input,"che.yd");
			System.out.println("deployId=="+deployId);
			
			//deploy 巡检计划流程 -----------
			fileName="workflow"+source_file+"proc_routineinspection_plan.zip";
			input=getConfigFileIn(fileName);
			zip_input=new ZipInputStream(input);
			deployId=workFlowService.deployWorkflow(zip_input,"che.yd");
			System.out.println("deployId=="+deployId);
			
			
			//deploy 巡检计划流程 -----------
			fileName="workflow"+source_file+"proc_routineinspection_task.zip";
			input=getConfigFileIn(fileName);
			zip_input=new ZipInputStream(input);
			deployId=workFlowService.deployWorkflow(zip_input,"che.yd");
			System.out.println("deployId=="+deployId);
			
			session.setAttribute("infoTips", "工作流数据初始化操作成功");
		}else{
			//System.out.println("system workflow data had initialize");
			session.setAttribute("infoTips", "工作流数据之前已初始化，请不要再重复操作");
		}
		return "success"; 
	}
	
	public InputStream getConfigFileIn(String processZipName) {
		return this.getClass().getResourceAsStream(
				"/"+processZipName);
	}
	
	
}
