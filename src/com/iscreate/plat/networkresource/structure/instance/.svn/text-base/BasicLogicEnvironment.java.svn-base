package com.iscreate.plat.networkresource.structure.instance;

import java.util.HashMap;
import java.util.Map;

import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.LogicEnvironment;

class BasicLogicEnvironment implements LogicEnvironment {

	private ContextFactory contextFactory;
	private Map<String, Object> envContext = new HashMap<String, Object>();
	private Map<String, Object> resource = new HashMap<String, Object>();

	public BasicLogicEnvironment(ContextFactory contextFactory) {
		this.contextFactory = contextFactory;
	}

	public void set(Object envc) {
		String key = envc.getClass().getName();
		envContext.put(key, envc);
	}

	void setResource(String resourceTag, Object resource) {
		this.resource.put(resourceTag, resource);
	}

	public <T> T get(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		String key = clazz.getName();
		return clazz.cast(envContext.get(key));
	}

	public ContextFactory getContextFactory() {
		return contextFactory;
	}

	public Object getResource(String resourceTag) {
		return this.resource.get(resourceTag);
	}

}
