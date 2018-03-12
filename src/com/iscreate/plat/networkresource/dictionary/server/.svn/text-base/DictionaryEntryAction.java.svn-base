package com.iscreate.plat.networkresource.dictionary.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.iscreate.plat.networkresource.common.action.ActionHelper;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.Entity;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.substance.SubstanceException;

public class DictionaryEntryAction {
	private DictionaryEntryService dictionaryEntryService;
	private BasicEntityQuickSort sort = new BasicEntityQuickSort();
	private Gson gson;
	private String resourceName;
	private String propertyName;
	private String dn;

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	// 获取所有的资源对象
	public void setDictionaryEntryService(
			DictionaryEntryService dictionaryEntryService) {
		this.dictionaryEntryService = dictionaryEntryService;
	}

	public void listAllSubstances() throws IOException, SubstanceException {
		Object result = dictionaryEntryService.listResourceRelation();
		write(result);
	}

	// 获取一个资源对象的详细信息
	public void listOneSubstanceDetail() throws SubstanceException, IOException {
		Object result = dictionaryEntryService
				.listOneSubstanceDetail(resourceName);
		write(result);
	}

	/**
	 * 获取资源指定属性的条目信息
	 * 
	 * @throws IOException
	 */
	public void listPropertyDictionaryInfo() throws IOException {
		List<BasicEntity> dics = dictionaryEntryService
				.listPropertyDictionaryInfo(resourceName, propertyName);
		sort.sort(dics);
		HashMap<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> es = new ArrayList<Map<String, Object>>();
		if (dics.isEmpty()) {
			result.put("size", 0);
		} else {
			result.put("size", dics.size());
			for (BasicEntity e : dics) {
				es.add(e.toMap());
			}
		}
		result.put("result", es);
		write(result);
	}

	/**
	 * 获取资源指定属性的条目信息
	 * 
	 * @throws IOException
	 */
	public void getOneEntryDn() throws IOException {
		String dn = dictionaryEntryService.getOneEntryDn(resourceName,
				propertyName);
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("dn", dn);
		write(result);
	}

	/**
	 * 获取下一级指定属性类型的详细字典信息
	 * 
	 * @throws EntryOperationException
	 * @throws IOException
	 */
	public void listNextLevelPropertyEntryInfo() throws IOException {
		List<BasicEntity> es = new ArrayList<BasicEntity>();
		try {
			es = dictionaryEntryService.listOneLevelEntry(dn, propertyName);
		} catch (EntryOperationException e1) {
		}
		sort.sort(es);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (BasicEntity e : es) {
			result.add(e.toMap());
		}
		write(result);
	}

	// 将一字典信息保存到指定目录下
	// 1、需要dn信息、需要详细信息
	// 2、在指定目录下创建该条目信息
	public void addPropertyEntry() throws IOException {
		Entity e = (Entity) ActionHelper.getBasicEntity();
		String parentDn = e.getValue("dn");
		String resourceName = e.getValue("resourceName");
		e.remove("dn");
		HashMap<String, Object> result = new HashMap<String, Object>();
		if (parentDn == null || parentDn.trim().isEmpty()) {
			result.put("result", 0);
			result.put("msg", "请指定父资源类型。");
			write(result);
			return;
		}
		try {
			this.dictionaryEntryService.addPropertyEntry(parentDn,
					resourceName, e);
			result.put("result", 1);
		} catch (EntryOperationException e1) {
			result.put("result", 0);
			result.put("msg", e1.getConsequence().getConsequence());
		}
		write(result);
	}

	// 根据前台传入的DN列表，逐个删除字典信息
	public void delPropertyEntry() throws IOException {
		String[] dns = ActionHelper.getParameterValues("deleteDns[]");
		StringBuilder builder = new StringBuilder();
		if (dns != null) {
			for (String dn : dns) {
				try {
					this.dictionaryEntryService.delPropertyEntry(dn);
				} catch (EntryOperationException e) {
					String msg = e.getConsequence().getConsequence();
					builder.append(msg);
					builder.append("\r\n");
				}
			}
		}
		String msg = builder.toString();
		HashMap<String, Object> result = new HashMap<String, Object>();
		if (msg.isEmpty()) {
			result.put("result", 1);
		} else {
			result.put("result", 0);
			result.put("msg", msg);
		}
		write(result);
	}

	private class BasicEntityQuickSort extends QuickSort<BasicEntity> {
		public int compare(BasicEntity obja, BasicEntity objb) {
			if (obja.containKey("orderId") && objb.containKey("orderId")) {
				String s1 = obja.getValue("orderId") + "";
				String s2 = objb.getValue("orderId") + "";
				return s1.compareTo(s2);
			}
			if (!obja.containKey("orderId") && !objb.containKey("orderId")) {
				return 0;
			}
			if (!obja.containKey("orderId") && objb.containKey("orderId")) {
				return -1;
			} else {
				return 1;
			}
		}

	}

	private void write(Object src) throws IOException {
		String result = gson.toJson(src);
		// System.out.println(result);
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().write(result);
	}

	public static void main(String[] args) throws IOException {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:applicationContext.xml");
		DictionaryEntryAction a = ctx.getBean(DictionaryEntryAction.class);
		a.setResourceName("SiteRoom");
		a.setPropertyName("powerSupplyType");
		long begin = System.currentTimeMillis();
		a.listPropertyDictionaryInfo();
		long end = System.currentTimeMillis();
		System.out.println(end - begin);
	}

}
