package com.iscreate.plat.networkresource.dictionary;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.application.tool.ModuleProvider;
import com.iscreate.plat.networkresource.common.tool.Attribute;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;

class Entry extends ApplicationEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4390944758022964655L;

	static final String ENTRYMODULE_NAME = "DictionaryEntry";

	static final String RDN_KEY = "rdn";

	static final String DN_KEY = "dn";

	static final String ATTRDEFINE_KEY = "attrDefine";

	private Entry() {

	}

	public String getRdn() {
		return super.getValue(RDN_KEY);
	}

	public void setRdn(String rdn) {
		super.setValue(RDN_KEY, rdn);
	}

	public String getDn() {
		return super.getValue(DN_KEY);
	}

	public void setDn(String dn) {
		super.setValue(DN_KEY, dn);
	}

	public BasicEntity getEntryDefine() {
		String d = super.getValue(ATTRDEFINE_KEY);
		d = d.replace("'", "\"");
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		Map<String, String> define = ModuleProvider.getGson().fromJson(d, type);
		define.put("dn", this.getDn());
		BasicEntity entity = BasicEntity.fromMap(define);
		return entity;
	}

	public void setEntryDefine(BasicEntity entity) {
		Map<String, Object> define = entity.toMap();
		String d = ModuleProvider.getGson().toJson(define);
		d = d.replace("\"", "'");
		d=d.replace("u0026", "&");
		super.setValue(ATTRDEFINE_KEY, d);
	}

	public static Entry changeFromEntity(BasicEntity entity) {
		if (entity == null) {
			return null;
		}
		Entry e = new Entry();
		for (String key : entity.keyset()) {
			e.set(new Attribute(key, entity.getValue(key)));
		}
		return e;
	}

	public static Entry createEntry(BasicEntity entity) {
		if (entity == null) {
			return null;
		}
		ApplicationEntity e = ModuleProvider.getModule(ENTRYMODULE_NAME)
				.createApplicationEntity();
		Entry entry = changeFromEntity(e);
		entry.setEntryDefine(entity);
		return entry;
	}

}
