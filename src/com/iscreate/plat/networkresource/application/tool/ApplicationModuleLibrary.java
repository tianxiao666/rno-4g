package com.iscreate.plat.networkresource.application.tool;



import java.util.Set;

/**
 * don't use it
 * @author joe
 *
 */
public interface ApplicationModuleLibrary {
	/**
	 * 判断xml模块池中是否包含指定字典
	 * 
	 * @param moduleName
	 * @return
	 */
	public boolean containModule(String moduleName);

	/**
	 * 获取指定的应用数据对象模块
	 * 
	 * @param moduleName
	 * @return
	 */
	public ApplicationModule getModule(String moduleName);

	/**
	 * 获取模板池中所有模板名称
	 * 
	 * @return
	 */
	public Set<String> moduleNames();

	/**
	 * 设置字典的配置文件名称。<br/>
	 * 
	 * XML文件格式可以参考如下：<br/>
	 * <configuration><br/>
	 * <file-path>./src/com/iscreate/biz/tool/xml</file-path><br/>
	 * <Mappings><br/>
	 * <Mapping>station.xml</Mapping><br/>
	 * </Mappings><br/>
	 * </configuration><br/>
	 * 
	 * '<file-path>'标签记录映射文件存储的目录位置<br/>
	 * '<Mapping>'标签记录映射文件的名称
	 * 
	 * @param configFile
	 */
	public void setConfigFile(String configFile);

	/**
	 * 读取XML实体对象字典池的配置文件，从中获取所有映射文件，并将它们映射成字典文件
	 */
	public void init();

	/**
	 * 从新初始化指定的字典对象。
	 * 
	 * @param moduleName
	 */
	public void init(String moduleName);
}
