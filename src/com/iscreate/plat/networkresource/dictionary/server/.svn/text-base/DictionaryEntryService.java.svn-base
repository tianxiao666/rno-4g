package com.iscreate.plat.networkresource.dictionary.server;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.Entity;
import com.iscreate.plat.networkresource.dictionary.Dictionary;
import com.iscreate.plat.networkresource.dictionary.EntryOperationException;
import com.iscreate.plat.networkresource.dictionary.SearchScope;
import com.iscreate.plat.networkresource.engine.tool.Queue;
import com.iscreate.substance.Substance;
import com.iscreate.substance.SubstanceException;
import com.iscreate.substance.service.EntityDBManager;
import com.iscreate.substance.tool.EntityModel;

public class DictionaryEntryService {
	final String networkResource = "networkResource";
	private Dictionary dictionary;

	private EntityDBManager engtityDBManager;

	public void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void setEngtityDBManager(EntityDBManager engtityDBManager) {
		this.engtityDBManager = engtityDBManager;
	}

	/**
	 * 获取资源树
	 * 
	 * @return
	 * @throws SubstanceException
	 */
	public List<Map<String, Object>> listResourceRelation()
			throws SubstanceException {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Substance> roots = engtityDBManager.getCollection()
				.getSubstanceRelation("_station_resource").getRoots();
		if (roots == null) {
			return result;
		}
		Queue<EntityModel> queue = new Queue<EntityModel>();
		for (Substance s : roots) {
			EntityModel em = (EntityModel) s;
			HashMap<String, Object> element = new HashMap<String, Object>();
			element.put("parentName", 0);
			element.put("resourceName", em.getEntityModelName());
			result.add(element);
			queue.push(em);
		}
		while (queue.hasElements()) {
			EntityModel em = queue.pop();
			List<Substance> children = engtityDBManager.getCollection()
					.getSubstanceRelation("_station_resource")
					.getSubffixSubstance(em);
			if (children != null) {
				for (Substance child : children) {
					EntityModel c = (EntityModel) child;
					queue.push(c);
					HashMap<String, Object> element = new HashMap<String, Object>();
					element.put("parentName", em.getEntityModelName());
					element.put("resourceName", c.getEntityModelName());
					result.add(element);
				}
			}
		}
		return result;
	}

	/**
	 * 获取单个资源的详细信息
	 * 
	 * @param resourceName
	 * @return
	 * @throws SubstanceException
	 */
	public List<String> listOneSubstanceDetail(String resourceName)
			throws SubstanceException {
		List<String> result = new ArrayList<String>();
		EntityModel em = this.engtityDBManager.getCollection().getModel(
				resourceName);
		for (String key : em.getAttributeNames()) {
			result.add(em.getEntityModelName() + "." + key);
		}
		return result;
	}

	/**
	 * 在指定资源目录下获取指定类型的属性条目
	 * 
	 * @param resourceName
	 * @param propertyName
	 * @return
	 */
	public List<BasicEntity> listPropertyDictionaryInfo(String resourceName,
			String propertyName) {
		List<BasicEntity> result = new ArrayList<BasicEntity>();
		if (propertyName == null) {
			return result;
		}
		String dn = resourceName + "," + networkResource;
		List<BasicEntity> children;
		try {
			children = dictionary.getEntry(dn, SearchScope.SUB_TREE, "");
		} catch (EntryOperationException e) {
			return result;
		}
		for (BasicEntity child : children) {
			if (propertyName.equals(child.getValue("type"))) {
				result.add(child);
			}
		}
		return result;
	}

	/**
	 * 在指定资源目录下获取指定类型的属性条目
	 * 
	 * @param resourceName
	 * @param propertyName
	 * @return
	 */
	public String getOneEntryDn(String resourceName, String propertyName) {
		if (propertyName == null) {
			return "";
		}
		String dn = resourceName + "," + networkResource;
		List<BasicEntity> children;
		try {
			children = dictionary.getEntry(dn, SearchScope.SUB_TREE, "");
		} catch (EntryOperationException e) {
			return "";
		}
		for (BasicEntity child : children) {
			if (propertyName.equals(child.getValue("type"))) {
				return child.getValue("dn") + "";
			}
		}
		return "";
	}

	/**
	 * 获取指定目录的下级字典信息
	 * 
	 * @param dn
	 * @param type
	 * @return
	 * @throws EntryOperationException
	 */
	public List<BasicEntity> listOneLevelEntry(String dn, String type)
			throws EntryOperationException {
		List<BasicEntity> entrys = dictionary.getEntry(dn,
				SearchScope.ONE_LEVEL, "");
		List<BasicEntity> result = new ArrayList<BasicEntity>();
		for (BasicEntity entry : entrys) {
			if (type == null || type.isEmpty()) {
				result.add(entry);
			} else {
				if (type.equals(entry.getValue("type"))) {
					result.add(entry);
				}
			}
		}
		return result;
	}
	
	public void delPropertyEntry(String dn) throws EntryOperationException{
		this.dictionary.removeEntry(dn);
	}

	public void addPropertyEntry(String parentDn, String resourceName,
			BasicEntity entry) throws EntryOperationException {
		String dn = "";
		if (parentDn != null && !parentDn.isEmpty()) {
			dn = entry.getValue("type") + "-" + entry.getValue("value") + ","
					+ parentDn;
		} else {
			dn = entry.getValue("type") + "-" + entry.getValue("value") + ","
					+ resourceName + "," + networkResource;
		}
		try {
			this.dictionary.addEntry(dn, entry);
		} catch (EntryOperationException e) {
			if (e.getConsequence().getCode().indexOf("0002") > 0) {
				String[] rdns = parentDn.split(",");
				if (rdns.length < 2 || resourceName == null
						|| resourceName.isEmpty()) {
					throw e;
				}
				createResourceEntry(resourceName);
				this.dictionary.addEntry(dn, entry);
			} else {
				throw e;
			}
		}
	}

	private void createResourceEntry(String resourceName) {
		String dn = resourceName + "," + networkResource;
		try {
			dictionary.addEntry(dn, new Entity());
		} catch (EntryOperationException e) {
			if (e.getConsequence().getCode().indexOf("0002") > 0) {
				try {
					dictionary.addEntry(networkResource, new Entity());
					dictionary.addEntry(dn, new Entity());
				} catch (EntryOperationException e1) {
				}
			} else {
				e.printStackTrace();
			}
		}
	}
}
