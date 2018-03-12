package com.iscreate.plat.networkresource.dataservice;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author HuangFu
 * 
 */
public class ContextFactoryImpl implements ContextFactory {
	private static final Log logger = LogFactory
			.getLog(ContextFactoryImpl.class);
	
	private String systemDataSourceConfig;
	private String entityDataSourceConfig;

	public String getSystemDataSourceConfig() {
		return this.systemDataSourceConfig;
	}

	public void setSystemDataSourceConfig(String systemDataSourceConfig) {
		this.systemDataSourceConfig = systemDataSourceConfig;
	}

	public String getEntityDataSourceConfig() {
		return this.entityDataSourceConfig;
	}

	public void setEntityDataSourceConfig(String entityDataSourceConfig) {
		this.entityDataSourceConfig = entityDataSourceConfig;
	}

	public ContextImpl CreateContext() {
		return new ContextImpl(null);
	}

	private void init() {
		try {
			//DataSourceManager.init(this.systemDataSourceConfig);

			//EntityConfigUtil.init(this.entityDataSourceConfig);
		} catch (Exception e) {
			logger.debug("dataSoure Init Faile");
			e.printStackTrace();
		}
		logger.debug("dataSource Init successful");
	}

	public String getEntityConfigFilePath() {
		return this.entityDataSourceConfig;
	}

	public String getSystemDataSourceConfigFilePath() {
		return this.systemDataSourceConfig;
	}
}
