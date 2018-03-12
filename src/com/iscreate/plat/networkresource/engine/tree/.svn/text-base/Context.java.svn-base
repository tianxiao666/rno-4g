package com.iscreate.plat.networkresource.engine.tree;

import java.util.List;
import java.util.Map;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;



/**
 * 数据库的操作接口，该接口是临时接口，开发后期需要使用正确的接口将该接口覆盖。
 * 
 * @author joe
 * 
 */
public interface Context {
	public void saveApplicationEntity(ApplicationEntity e);

	public void updateApplicationEntity(ApplicationEntity e);

	public void deleteApplicationEntity(ApplicationEntity e);

	public ApplicationEntity getApplicationEntityById(String type, String id);

	public List<ApplicationEntity> getApplicationEntites(String type,Map<String, Object> condition);

	public ApplicationEntity getOneApplicationEntity(String type, Map<String, Object> condition);

	public void deleteApplicationEntity(String type, Map<String, Object> condition);

	public void saveTreenode(Treenode e);

	public void updateTreenode(Treenode e);

	public void deleteTreenode(Treenode e);

	public Treenode getTreenodeById(String type, String id);

	public List<Treenode> getTreenodes(String type,Map<String, Object> condition);

	public Treenode getOneTreenode(String type, Map<String, Object> condition);

	public void deleteTreenode(String type, Map<String, Object> condition);
	
	void setContextFactory(ContextFactory factory);
}
