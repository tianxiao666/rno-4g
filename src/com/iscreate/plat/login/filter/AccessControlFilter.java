package com.iscreate.plat.login.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.iscreate.op.pojo.system.SysOrgUser;
import com.iscreate.plat.login.constant.UserInfo;
import com.iscreate.plat.login.controller.AccessHandler;

/**
 * 访问权限控制过滤器,用户访问的资源将在这里做过滤判断
 * 
 */
public class AccessControlFilter implements Filter {
	private Log logger = LogFactory.getLog(this.getClass());

	public Object syncObj = new Object();

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain filter) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		response.setContentType("text/plain");
		response.setCharacterEncoding("utf-8");
		
		if(!request.getRequestURI().contains(".jsp") && !request.getRequestURI().contains("Action")){
			//由于struts过滤器设定拦截了所有url，系统中action写法没有规范，为了提升页面访问效率，在此处做了简单过滤
			filter.doFilter(req, res);
			return;
		}
		
		/**
		 * 获取用户请求访问的resource--->action
		 */
		String userId = (String) request.getSession().getAttribute(UserInfo.USERID);
		AccessHandler accessHandler;

		WebApplicationContext wc = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession()
						.getServletContext());
		accessHandler = (AccessHandler) wc.getBean("accessHandler");

		/**
		 * 在session中设置用户的中文名
		 */
		// 对于登录用户，获取其中文名
		if (userId != null
				&& request.getSession().getAttribute(UserInfo.USERNAME) == null) {

			// 补充中文名
			SysOrgUser user = accessHandler.getSysOrgUserService().getSysOrgUserByAccount(userId);
			if (user != null) {
				request.getSession().setAttribute(UserInfo.USERNAME,
						user.getName());
				logger.debug("获取账号：[" + userId + "]的中文名：" + user.getName()
						+ " - " + Thread.currentThread().getId());
			} else {
				// 有登录，却不存在，这是不允许的
				logger.error("账号：" + userId + " 不存在！");
				response.setStatus(401);// unauthorize
				return;
			}
		}

		/**
		 * 检查用户是否有权限访问请求的resource
		 */
//		synchronized (syncObj) {
			String requestUrl = request.getRequestURI();//连接地址
		    String parameterString =  request.getQueryString();//参数字符串
		    parameterString=parameterString==null?"":parameterString;
		    List<Map<String,Object>> permissionList =  
		    	(List<Map<String, Object>>) request.getSession().getAttribute(UserInfo.ACCOUNT_PERMISSION);
		    List<Map<String,Object>> permissionList1 =  
		    	(List<Map<String, Object>>) request.getSession().getAttribute(UserInfo.PM_ACCOUNT_PERMISSION);
		    
			logger.debug("用户：[" + userId + "] 尝试访问：" + requestUrl);
			
			if(checkUserPermissionUrl(permissionList,requestUrl,parameterString)){
				//检查用户是否有权限，有权访问
				logger.debug("允许访问");
				filter.doFilter(req, res);
			}else if(checkUserPermissionUrl(permissionList1,requestUrl,parameterString)){
				//检查用户是否有权限，有权访问
				logger.debug("允许访问");
				filter.doFilter(req, res);
			}else{
				//无权访问
				logger.debug("不允许访问资源链接" + requestUrl);
				response.setStatus(403);// forbidden
				response.getWriter().write("没有权限访问该资源!");
				return;
			}
//		}
		

	}
	/**
	 * @author du.hw
	 * @create_time 2013-06-06
	 * 检查Url是否在权限控制中
	 * @return
	 * true:用户有权访问
	 * flase:用户无权访问
	 */
    public boolean checkUserPermissionUrl(List<Map<String,Object>> permissionList,String url,String urlParameter){
    	if(permissionList != null && permissionList.size()>0){//有权限控制
    		boolean urlFlag = false;//当前url是否与权限控制中的url匹配
    		boolean parameterFlag = false;//当前参数是否与权限控制中的参数匹配
    		for(int i=0;i<permissionList.size();i++){
    			Map<String,Object> perMap = permissionList.get(i);
    			//System.out.println(perMap);
    			String perUrl = (String) perMap.get("URL");
    			String perParameter = (String) perMap.get("PARAMETER");
    			//String perUrl = (String) perMap.get("url");
    			//String perParameter = (String) perMap.get("parameter");
    			if(url.contains(perUrl)){
    				//如果访问的url包括权限控制的字符串（这里目前只错了简单的字符串比对，以后有时间需要改进）
    				urlFlag = true;
    				//检查url中的参数
    				parameterFlag = checkUserPermissionParameter(urlParameter,perParameter);
    				
    			}
    			if(urlFlag && parameterFlag){//url和参数都匹配上(此链接做了权限控制)
    				String userPerFlag =  perMap.get("FLAG").toString();//1:代表用户有权限，0：代表用户无权限
    				if(userPerFlag.equals("1")){
    					return true;
    				}else{
    					return false;
    				}
    			}
    		}
    		
    	}else{//没有权限控制
    	    return true;
    	}
    	return true;
    	
    }
    /**
     * @author du.hw
     * @create_time 2013-06-06
     * 检查url中的参数是否与某一权限的参数控制匹配
     * false:未找到匹配项
     * true:找到匹配项
     */
    public boolean checkUserPermissionParameter(String urlParameter,String permissionParmeter){
    	if(permissionParmeter != null && !permissionParmeter.equals("")){
    		 if(urlParameter==null || "".equals(urlParameter)){//当前访问链接没有参数
    			 return false;
    		 }
			 String[] perParameterArray= permissionParmeter.split("&");//拆分权限控制参数（可能多个参数）
			 if(perParameterArray.length != 0){//开始检查参数
				  for(int i=0;i<perParameterArray.length;i++){
					  if(!urlParameter.contains(perParameterArray[i])){
						  //当前URL的参数中不包括权限控制中的参数
						  return false;
					  }
				  }
			 }
		}
    	return true;
    }
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
