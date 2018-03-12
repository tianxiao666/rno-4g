package com.iscreate.plat.networkresource.engine.figure.execution;

import java.util.HashMap;

import com.iscreate.plat.networkresource.dataservice.ContextFactory;

public class QueryEnvironment {
	private HashMap<String, Object> envParam = new HashMap<String, Object>();
	private ContextFactory contextFactory;

	public QueryEnvironment(ContextFactory contextFactory) {
		this.contextFactory = contextFactory;
	}

	public ContextFactory getContextFactory() {
		return contextFactory;
	}
	

	public void set(Object object) {
		String key = object.getClass().getName();
		envParam.put(key, object);
	}

	public <T> T get(Class<T> clazz) {
		String key = clazz.getName();
		T param = clazz.cast(envParam.get(key));
		return param;
	}

}
