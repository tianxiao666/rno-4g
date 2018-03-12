package com.iscreate.op.action.informationmanage.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ctc.wstx.io.EBCDICCodec;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ActionContext;

/**
 * Action工具类
 * @author andy
 */
@SuppressWarnings({"unused","unchecked"})
public class ActionUtil {
	
	public static String getParamString ( String requestName ) {
		HttpServletRequest request = ServletActionContext.getRequest();
		String parameter = request.getParameter(requestName);
		return parameter;
	}
	
	
	/**
	 * 响应信息(默认使用ServletActionContext获取response对象)
	 * @param - obj - 对象 (如不是String类型,会对其进行转换) 
	 */
	public static void responseWrite ( Object obj ) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		responseWrite(obj , response , true );
	}
	
	public static void responseWrite ( Object obj , boolean isWithOutExpose  ) throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		responseWrite(obj , response , isWithOutExpose );
	}
	
	public static void responseWrite ( Object obj , HttpServletResponse response , boolean isWithOutExpose ) throws IOException {
		Gson gson = null;
		if ( isWithOutExpose ) {
			gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		} else {
			gson = new Gson();
		}
		responseWrite(obj, response, gson);
	}
	
	/**
	 * 响应信息
	 * @param obj - 对象 (如不是String类型,会对其进行转换) 
	 * @param response - 响应对象
	 */
	public static void responseWrite ( Object obj , HttpServletResponse response , Gson gson ) throws IOException {
		String json = null;
		if ( obj instanceof String ) {
			json = obj.toString();
		} else {
			json = gson.toJson(obj);
		}
		response.setContentType ("text/html;charset=utf-8");
		PrintWriter out = ServletActionContext.getResponse().getWriter();
		out.print(json);
		out.flush();
		out.close();
	}
	
	public static Map<String, String> getRequestParamMap ( String... param_arr ) {
		Map<String, String> requestParamMap = getRequestParamMap( false , param_arr );
		return requestParamMap;
	}
	
	/**
	 * 根据参数名数组,获取request参数
	 * @param param_arr - 参数名 数组
	 * @return request参数
	 */
	public static Map<String, String> getRequestParamMap ( boolean isChange , String... param_arr ) {
		Map<String , String> map = new LinkedHashMap<String, String>();
		HttpServletRequest request = ServletActionContext.getRequest();
		for (int i = 0; param_arr != null && i < param_arr.length; i++) {
			String param_key = param_arr[i];
			try {
				String param = request.getParameter(param_key);
				if ( isChange ) {
					param = new String(param.getBytes("ISO-8859-1"),"UTF-8");
				}
				map.put(param_key, param);
			} catch (Exception e) {
			}
		}
		return map;
	}
	
	
	public static Map<String,String> getRequestParamMapByChoiceMap ( String choiceString ) {
		Map<String, String> requestParamMapByChoiceMap = getRequestParamMapByChoiceMap(choiceString,false);
		return requestParamMapByChoiceMap;
	}
	
	/**
	 * 根据字符串筛选key值,获取参数
	 * @param choiceString - 筛选的字符串
	 * @return 筛选后的参数集合
	 */
	public static Map<String,String> getRequestParamMapByChoiceMap ( String choiceString , boolean isChange ) {
		Map<String,String> param_map = new LinkedHashMap<String, String>();
		ActionContext context = ActionContext.getContext(); 
		Map<String, Object> request_map = context.getParameters();
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		for ( Iterator<String> it = request_map.keySet().iterator(); it.hasNext() ;) {
			String key = it.next();
			String parameter = request.getParameter(key);
			if ( isChange ) {
				try {
					parameter = new String(parameter.getBytes("ISO-8859-1"),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if ( parameter == null || key.indexOf(choiceString) != 0 ) {
				continue;
			}
			String newKey = key.replace(choiceString, "");
			String newValue = parameter;
			param_map.put(newKey, newValue);
		}
		return param_map;
	}
	
	public static Map<String,Object> getRequestParamMapByChoiceMapObject ( String choiceString ) {
		Map<String, Object> requestParamMapByChoiceMapObject = getRequestParamMapByChoiceMapObject(choiceString,false);
		return requestParamMapByChoiceMapObject;
	}
	
	/**
	 * 根据字符串筛选key值,获取参数
	 * @param choiceString - 筛选的字符串
	 * @return 筛选后的参数集合
	 */
	public static Map<String,Object> getRequestParamMapByChoiceMapObject ( String choiceString , Boolean isChange ) {
		Map<String,Object> param_map = new LinkedHashMap<String, Object>();
		ActionContext context = ActionContext.getContext();
		Map<String, Object> request_map = context.getParameters();
		HttpServletRequest request = ServletActionContext.getRequest();
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		for ( Iterator<String> it = request_map.keySet().iterator(); it.hasNext() ;) {
			String key = it.next();
			String[] strs = (String[])request_map.get(key);
			if ( strs == null || key.indexOf(choiceString) != 0 ) {
				continue;
			}
			String newKey = key.replace(choiceString, "");
			for (int i = 0; isChange && strs != null && i < strs.length; i++) {
				String parameter = strs[i];
				try {
					parameter = new String(parameter.getBytes("ISO-8859-1"),"UTF-8");
					strs[i] = parameter;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			if ( strs.length == 1 && strs[0] != null ) {
				param_map.put(newKey, strs[0]);
				continue;
			}
			param_map.put(newKey, strs);
		}
		return param_map;
	}
	
	/**
	 * 获取项目名称
	 * @return
	 */
	public static String getProjectName () {
		HttpServletRequest request = ServletActionContext.getRequest();
		String contextPath = request.getContextPath().replace("/", "");
		return contextPath;
	}
	
	/**
	 * 根据参数名数组,获取request参数
	 * @param param_arr 参数名 数组
	 * @return request参数
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getRequestParaMap ( String[] param_arr ) {
		Map<String , Object> map = new LinkedHashMap<String, Object>();
		HttpServletRequest request = ServletActionContext.getRequest();
		for (int i = 0; i < param_arr.length; i++) {
			String param_key = param_arr[i];
			try {
				String param = request.getParameter(param_key);
				map.put(param_key, param);
			} catch (Exception e) {
				String[] param = request.getParameterValues(param_key);
				map.put(param_key, param);
			}
		}
		return map;
	}
}
