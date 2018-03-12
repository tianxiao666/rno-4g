package com.iscreate.plat.networkresource.engine.figure.execution;

import com.iscreate.plat.networkresource.dataservice.ContextFactory;

public interface Execution<T> {
	public T doExecution(ContextFactory contextFactory);
}
