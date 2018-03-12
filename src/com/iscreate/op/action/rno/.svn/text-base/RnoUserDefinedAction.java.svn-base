package com.iscreate.op.action.rno;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import sun.util.logging.resources.logging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iscreate.op.constant.RnoConstant;
import com.iscreate.op.constant.RnoConstant.DBConstant;
import com.iscreate.op.pojo.rno.RnoUserDefinedFormul;
import com.iscreate.op.service.rno.RnoUserDefinedService;
import com.iscreate.op.service.rno.tool.HttpTools;
import com.iscreate.sso.session.UserInfo;

public class RnoUserDefinedAction extends RnoCommonAction{

	//log打印
	private static Log log = LogFactory.getLog(RnoUserDefinedAction.class);
	private static Gson gson = new GsonBuilder().create();// 线程安全
	//注入
	private RnoUserDefinedService rnoUserDefinedService;
	//表单类
	private RnoUserDefinedFormul rnoUserDefinedFormul;
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	//页面参数
	private long areaId;
	private long cityId;
	public RnoUserDefinedService getRnoUserDefinedService() {
		return rnoUserDefinedService;
	}
	public void setRnoUserDefinedService(RnoUserDefinedService rnoUserDefinedService) {
		this.rnoUserDefinedService = rnoUserDefinedService;
	}
	public RnoUserDefinedFormul getRnoUserDefinedFormul() {
		return rnoUserDefinedFormul;
	}
	public void setRnoUserDefinedFormul(RnoUserDefinedFormul rnoUserDefinedFormul) {
		this.rnoUserDefinedFormul = rnoUserDefinedFormul;
	}
	public long getAreaId() {
		return areaId;
	}
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	public long getCityId() {
		return cityId;
	}
	public void setCityId(long cityId) {
		this.cityId = cityId;
	}
	/**
	 * 初始化用户自定义公式分析页面
	 * @return
	 * @author chao.xj
	 * @date 2014-1-3上午10:26:55
	 */
	public String initRnoUserDefinedFormulAnalysisPageAction() {
		initAreaList();
		return "success";
	}
	/**
	 * 
	 * 接收话统用户自定义条件并存储
	 * @author chao.xj
	 * @date 2014-1-2上午10:34:41
	 */
	public void reciveStsUserDefinedConditionAndStorageAction() {
		log.info("进入reciveStsUserDefinedConditionAndStorageAction　rnoUserDefinedFormul:"+this.rnoUserDefinedFormul);
		//当前用户ID、当前用户名
		// 从session获取user
		HttpServletRequest request = ServletActionContext.getRequest();
		String userId = (String) request.getSession().getAttribute(
				UserInfo.CAS_FILTER_USERID);
		/*String userName=(String) request.getSession().getAttribute(
				UserInfo.CAS_FILTER_USERNAME);
		log.info("userId:"+userId+"    userName:"+userName);*/
		//创建时间、修改时间
		//状态:Y/N
		//模块类型：STS
		String applyScope=this.rnoUserDefinedFormul.getApplyScope();
		Date curDate=new Date();
		String result="";
		int a=0;
//		log.info("cityId:"+cityId+"    areaid:"+areaId);
//		System.out.println("areaId:"+Long.toString(areaId));
		String areaString=Long.toString(areaId);
		String cityString=Long.toString(cityId);
		try {
			rnoUserDefinedFormul.setCreateTime(sdf.parse(sdf.format(curDate)));
			rnoUserDefinedFormul.setModTime(sdf.parse(sdf.format(curDate)));
//			userDefinedCondition.setCreater(creater);
			if ("perscope".equals(applyScope)) {
				rnoUserDefinedFormul.setScopeValue(userId);
			}else if ("cityscope".equals(applyScope)) {
				rnoUserDefinedFormul.setScopeValue(cityString);
			}else if ("areascope".equals(applyScope)) {
				rnoUserDefinedFormul.setScopeValue(areaString);
			}
			rnoUserDefinedFormul.setModuleType("STS");
			rnoUserDefinedFormul.setCreater(userId);
			rnoUserDefinedFormul.setStatus("Y");
//			rnoUserDefinedService.saveOneUserDefinedFormul(rnoUserDefinedFormul);
			a=rnoUserDefinedService.insertOneUserDefinedFormul(rnoUserDefinedFormul);
			if (a>0) {
				result="插入成功!";
			}else {
				result="插入失败";
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result="err";
		}
		log.info("退出reciveStsUserDefinedConditionAndStorageAction　result:"+result);
		String relString=gson.toJson(result);
		HttpTools.writeToClient(relString);
	}
	
	



}
