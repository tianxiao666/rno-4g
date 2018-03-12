package com.iscreate.op.constant;

import java.util.HashMap;
import java.util.Map;

public class OperationType {
	//添加
	public static String INSERT = "添加";
	//更新，修改
	public static String UPDATE = "修改";
	//删除
	public static String DELETE = "删除";
	//查询
	public static String SELECT = "查询";
	//添加资源
	public static String RESOURCEINSERT = "添加资源";
	//资源属性修改
	public static String RESOURCEUPDATE = "属性修改";
	//删除资源
	public static String RESOURCEDELETE = "删除资源";
	//递归删除资源
	public static String RECURSIVEDELETE = "删除资源";
	//网络资源
	public static String NETWORK = "网络资源";
	//网络资源管理维护
	public static String NETWORKRESOURCEMANAGE = "网络资源管理维护";
	//资源编辑维护
	public static String RESOURCEEDITORMAINTENANCE = "维护服务";
	//资源初始化
	public static String RESOURCEINITIALIZATION = "资源初始化";
	//递归删除资源关系
	public static String RECURSIVEDELETELINK = "递归删除资源关系";
	//解除关联
	public static String DELETELINK = "解除关联";
	//资源关联
	public static String INSERTLINK = "资源关联";
	
	public static String getAssociatedTypeChinese(Object ob){
		Map<Object, String> map = new HashMap<Object, String>();
		map.put("LINK", "关联关系");
		map.put("PARENT", "父级关系");
		map.put("CHILD", "子级关系");
		map.put("CLAN", "隶属关系");
		map.put("BACKWORD", "前关联关系");
		map.put("FORWORD", "后关联关系");
		map.put("ALL", "所有关系");
		return map.get(ob);
	}
	
}
