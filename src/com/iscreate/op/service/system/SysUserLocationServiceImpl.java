package com.iscreate.op.service.system;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.iscreate.op.action.publicinterface.TerminalUpdateVersionInfoActionForMobile;
import com.iscreate.plat.login.constant.UserInfo;
import com.iscreate.plat.mobile.pojo.MobilePackage;
import com.iscreate.plat.mobile.util.MobileContentHelper;
import com.iscreate.plat.networkresource.dataservice.DataSourceConn;

public class SysUserLocationServiceImpl implements SysUserLocationService{
	
	private  static final  Log log  =  LogFactory.getLog(TerminalUpdateVersionInfoActionForMobile.class);
	
	/**
	 * 
	 * @description: 保存用户当前登录终端位置（由于在loginfiter 过滤器调用 直接jdbc）
	 * @author：yuan.yw     
	 * @return String     
	 * @date：Jul 9, 2013 4:39:15 PM
	 */
	public String saveSysUserLocationServiceForMobile(ServletRequest req, ServletResponse res){
		log.info("进入saveSysUserLocationActionForMobile方法,保存用户当前登录终端位置。");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpSession session = request.getSession();
		MobilePackage mobilePackage = null;
		String jsonStr = request.getParameter("jsonArrayObj"); // 获取请求参数
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		if(jsonStr!=null && !jsonStr.trim().equals("")){
			try {
				jsonStr=URLDecoder.decode(jsonStr,"UTF-8");
				mobilePackage = gson.fromJson(jsonStr,new TypeToken<MobilePackage>(){}.getType());
			} catch (Exception e) {
				log.info("mobilePackage为空，退出saveSysUserLocationActionForMobile方法，返回结果error");
				return "error";
			}
		}else{
			log.info("jsonArrayObj参数为空，退出saveSysUserLocationActionForMobile方法，返回结果error");
			return "error";
		}
		String content = mobilePackage.getContent();
		MobileContentHelper mch = new MobileContentHelper();
		mch.setContent(content);
		Map<String, String> formJsonMap = mch.getGroupByKey("request");
		String longitude = formJsonMap.get("longitude");//经度
		String latitude = formJsonMap.get("latitude");//纬度
		if(longitude!=null && !"".equals(longitude) && !"null".equals(longitude)&&latitude!=null && !"".equals(latitude) && !"null".equals(latitude)){
			Double lote = Double.parseDouble(longitude);
			Double late = Double.parseDouble(latitude);
			int status = 0;
			if(session.getAttribute(UserInfo.ORG_USER_ID)==null){
				log.info("mobilePackage为空，退出saveSysUserLocationActionForMobile方法，返回结果error");
				return "error";
			}
			Long orgUserId = Long.parseLong(session.getAttribute(UserInfo.ORG_USER_ID)+"");
			if(lote<=135.05 && lote>=73.33 && late<=53.33 && late>=3.51 ){//中国范围经纬度 状态设置为1
				status = 1;
			}
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date=dateformat.format(new Date());
			String sql ="insert into sys_user_location(org_user_id,longitude,latitude,status,createtime) values('"+orgUserId+"','"+lote+"','"+late+"','"+status+"',TO_DATE('"+date+"','yyyy-mm-dd hh24:mi:ss'))";
			this.saveEntityObjectBySql(sql);
			log.info("退出saveSysUserLocationActionForMobile方法，保存用户当前登录终端位置成功,返回结果success");
			return "success";
		}else{
			log.info("经度或纬度为空或格式不合法，退出saveSysUserLocationActionForMobile方法，返回结果error");
			return "error";
		}
		
	}
	/**
	 * 
	 * @description:保存实体对象
	 * @author：yuan.yw
	 * @param sql     
	 * @return int     
	 * @throws SQLException 
	 * @date：Jul 10, 2013 9:34:14 AM
	 */
	public int saveEntityObjectBySql(String sql){
		Connection conn = null;
		Statement pstm = null;
		if (sql == null
				|| "".equals(sql))
			return 0;
		try {
			conn = DataSourceConn.initInstance().getConnection();  //Conn.getConn().getConnection();
			conn.setAutoCommit(false);
			pstm = conn.createStatement();
			pstm.execute(sql);
			conn.commit();
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			log.debug("数据保存失败");
			return 0;
		} finally {
			if (pstm != null)
				try {
					pstm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		log.debug("数据保存成功");
		return 1;
	}
}
