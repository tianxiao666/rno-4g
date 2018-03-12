package com.iscreate.plat.networkresource.engine.figure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.dataservice.Context;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.engine.figure.cmd.Command;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandDeleteAppEntity;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandDeleteAppEntityById;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandDeleteFigureline;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandDeleteFigurelineByNodeId;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandDeleteFigurenode;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandSaveAppEntity;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandSaveOrUpdateAppEntity;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandSaveOrUpdateFigureline;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandSaveOrUpdateFigurenode;
import com.iscreate.plat.networkresource.engine.figure.execution.BasicListFigurenodeExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.Execution;
import com.iscreate.plat.networkresource.engine.figure.execution.ListFigurenodeExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryAesFigurenode;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryAllFigurelineExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryApplicationEntityByIdExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryApplicationEntityByIdsExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryFigurenodeByEntityIdAndTypeExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryFigurenodeByIdsExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryFigurenodeByPathExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryParentIdExecution;
import com.iscreate.plat.networkresource.engine.tool.Queue;
import com.iscreate.plat.networkresource.engine.tool.Stack;
import com.iscreate.plat.networkresource.structure.template.StructurePrimary;

public class Figure {

	public static final String MY_TYPE = Figure.class.getSimpleName();
	public static final String FIGURENAME_KEY = "figureName";
	public static final String FIGUREID_KEY = "figureId";

	private long figureId;
	private ContextFactory contextFactory;
	private Queue<Command> cmdList = new Queue<Command>();
	private ApplicationEntity begin;
	private Log log = LogFactory.getLog(getClass());

	/**
	 * 获取加载图时开始的应用数据对象
	 * 
	 * @return
	 */
	public ApplicationEntity getBegin() {
		return begin;
	}

	public Figure(long figureId) {
		this.figureId = figureId;
	}

	public Figure(long figureId, ContextFactory contextFactory) {
		this.figureId = figureId;
		this.contextFactory = contextFactory;
	}

	/**
	 * 获取图ID
	 * 
	 * @return
	 */
	public long getFigureId() {
		return this.figureId;
	}

	/**
	 * 获取图对象的数据操作工厂对象
	 * 
	 * @return
	 */
	public ContextFactory getContextFactory() {
		return contextFactory;
	}

	/**
	 * 为图对象设置数据操作工厂对象
	 * 
	 * @param contextFactory
	 */
	public void setContextFactory(ContextFactory contextFactory) {
		this.contextFactory = contextFactory;
	}

	/**
	 * 执行加载数据的命令
	 * 
	 * @param <T>
	 * @param execution
	 * @return
	 */
	public <T> T doExecution(Execution<T> execution) {
		return execution.doExecution(contextFactory);
	}

	/**
	 * 在图中添加应用数据对象
	 * 
	 * @param entity
	 */
	public void addApplicationEntity(ApplicationEntity entity) {
		createFigurenodeAndCommand(entity);
		return;
	}

	/**
	 * 在图中创建应用数据对象之间的关系
	 * 
	 * @param left
	 * @param right
	 * @param figurelineType
	 * @updateauthor yuan.yw
	 * @updatedate 2013-06-28
	 * @updatereason  保存path 
	 */
	public void createRelation(ApplicationEntity left, ApplicationEntity right,
			FigurelineType figurelineType) {
		String funtionName = "createRelation(ApplicationEntity left, ApplicationEntity right,"
				+ "FigurelineType figurelineType)";
		log.debug("开始执行方法：'" + funtionName + "'");
		if (left == null) {
			log.debug("方法参数：left 为空，方法结束");
			return;
		}
		if (right == null) {
			log.debug("方法参数：right 为空，方法结束");
			return;
		}
		if (figurelineType == null) {
			log.debug("方法参数：figurelineType 为空，方法结束");
			return;
		}
		log.debug("创建两个对象对应的节点命令以及边命令");
		//yuan.yw 2013-06-28 add
		if(figurelineType.equals(FigurelineType.CLAN)||figurelineType.equals(FigurelineType.CHILD)||figurelineType.equals(FigurelineType.PARENT)){
			BasicEntity be = this.doExecution(new QueryFigurenodeByEntityIdAndTypeExecution(left.getId(),left.getType()));
			if(be!=null){
				String path = ""; //上下级路径path
				String rightId =this.getTableEntityIdValue(right)+"";//当前资源id
				if(be.getValue(Figurenode.PATH)!=null && !"".equals(be.getValue(Figurenode.PATH))){
					path=be.getValue(Figurenode.PATH)+right.getType()+"/"+rightId+"/";  //path设值
				}
				this.updateFigurenodeAndCommand(right, path,Long.valueOf(be.getValue("id")+""));
				if(right.getType().indexOf("Area")>=0){//资源为区域时
					right.setValue("path", path.replace("/"+right.getType()+"/",""));//保存资源path
					right.setValue("parent_id", left.getValue("id"));
					CommandSaveOrUpdateAppEntity sacmd = new CommandSaveOrUpdateAppEntity(
							right);
					addCommand(sacmd);
				}
				String parPath = "/"+right.getType()+"/"+rightId+"/";//当前自身path
				List<BasicEntity> resultList = this.doExecution(new QueryFigurenodeByPathExecution(parPath));//查询
				if(resultList!=null && !resultList.isEmpty()){
					int i=0;
					for(BasicEntity b:resultList){
						if(i>0){//当前资源不更新
							String cPath = b.getValue(Figurenode.PATH);//查询结果path
							ApplicationEntity ae  = ApplicationEntity.changeFromEntity(b);
							cPath = cPath.replace(cPath.substring(0,cPath.indexOf(parPath))+parPath,path);//path 截取设值
							ae.setValue(Figurenode.PATH, cPath);//上下级路径path
							this.updateFigurenodeCommand(ae);
						}
						i++;
					}
				}
			}
		}else{
			Figurenode ln = createOrUpdateFigurenodeAndCommand(left);
			Figurenode rn = createOrUpdateFigurenodeAndCommand(right);
			createFigurelineAndCommand(ln, rn, figurelineType);
		}
		
		log.debug("方法'" + funtionName + "'执行结束。");
	}

	/**
	 * 从图中删除一个应用对象及其相应的图节点
	 * 
	 * @param entity
	 * @updateauthor yuan.yw
	 * @updatedate 2013-07-01
	 * @updatereason 删除上下级逻辑增加path
	 */
	public void delApplicationEntity(ApplicationEntity entity) {
		String funtionName = "delApplicationEntity(ApplicationEntity entity)";
		log.debug("开始执行方法：'" + funtionName + "'");
		if (entity == null) {
			log.debug("方法参数：entity 为空，方法结束");
			return;
		}
		log.debug("开始查找应用数据对象对应的图节点");
		QueryAesFigurenode execution = new QueryAesFigurenode(figureId, entity);
		Figurenode node = doExecution(execution);
		if (node == null) {
			log.debug("没有找到应用对象：'" + entity.getType() + ":" + entity.getId()
					+ "'对应的图节点，方法结束");
			return;
		}
		log.debug("创建相应的删除命令");
		//yuan.yw add 2013-07-01
		List<BasicEntity> beList = this.doExecution(new QueryFigurenodeByPathExecution("/"+entity.getType()+"/"+this.getTableEntityIdValue(entity)+"/"));
		if(beList!=null && !beList.isEmpty()){
			for(BasicEntity be:beList){
				ApplicationEntity ae = ApplicationEntity.changeFromEntity(be);
				ApplicationEntity e=this.doExecution(new QueryApplicationEntityByIdExecution(ae.getValue("entityType")+"",Long.valueOf(ae.getValue("entityId")+"")));
				ae.setValue(Figurenode.PATH,"/"+e.getType()+"/"+this.getTableEntityIdValue(e)+"/");
				ae.setValue(Figurenode.PARENT_FIGURENODE_ID, 0);
				this.updateFigurenodeCommand(ae);
			}
		}
		CommandDeleteFigurenode dncmd = new CommandDeleteFigurenode(node);
		addCommand(dncmd);
		
		CommandDeleteFigurelineByNodeId dlcmd = new CommandDeleteFigurelineByNodeId(
				node.getId(), null);
		addCommand(dlcmd);
		// 删除应用数据对象的命令
		CommandDeleteAppEntity dacmd = new CommandDeleteAppEntity(entity);
		addCommand(dacmd);
		log.debug("方法'" + funtionName + "'执行结束。");
	}

	/**
	 * 删除对象节点及其相邻关系
	 * 
	 * @param entity
	 * @updateauthor yuan.yw
	 * @updatedate 2013-07-01
	 * @updatereason 删除上下级逻辑增加path 
	 */
	public void delApplicationEntityRelated(ApplicationEntity entity) {
		String funtionName = "delApplicationEntityRelated(ApplicationEntity entity)";
		log.debug("开始执行方法：'" + funtionName + "'");
		if (entity == null) {
			log.debug("方法参数：entity 为空，方法结束");
			return;
		}
		log.debug("开始查找应用数据对象对应的图节点");
		QueryAesFigurenode execution = new QueryAesFigurenode(figureId, entity);
		Figurenode node = doExecution(execution);
		if (node == null) {
			log.debug("没有找到应用对象：'" + entity.getType() + ":" + entity.getId()
					+ "'对应的图节点，方法结束");
			return;
		}
		log.debug("创建相应的删除命令");
		//yuan.yw add 2013-07-01
		List<BasicEntity> beList = this.doExecution(new QueryFigurenodeByPathExecution("/"+entity.getType()+"/"+this.getTableEntityIdValue(entity)+"/"));
		if(beList!=null && !beList.isEmpty()){
			for(BasicEntity be:beList){
				ApplicationEntity ae = ApplicationEntity.changeFromEntity(be);
				ApplicationEntity e=this.doExecution(new QueryApplicationEntityByIdExecution(ae.getValue("entityType")+"",Long.valueOf(ae.getValue("entityId")+"")));
				ae.setValue(Figurenode.PATH,"/"+e.getType()+"/"+this.getTableEntityIdValue(e)+"/");
				ae.setValue(Figurenode.PARENT_FIGURENODE_ID, 0);
				this.updateFigurenodeCommand(ae);
			}
		}
		CommandDeleteFigurenode dncmd = new CommandDeleteFigurenode(node);
		addCommand(dncmd);
		CommandDeleteFigurelineByNodeId dlcmd = new CommandDeleteFigurelineByNodeId(
				node.getId(), null);
		addCommand(dlcmd);
		log.debug("方法'" + funtionName + "'执行结束。");
	}

	/**
	 * 递归删除所有的子节点及其关系
	 * 
	 * @param entity
	 * @updateauthor yuan.yw
	 * @updatedate 2013-07-01
	 * @updatereason 删除上下级逻辑增加path 
	 */
	public void delApplicationEntityRelatedRecursion(ApplicationEntity entity) {
		String funtionName = "delApplicationEntityRelatedRecursion(ApplicationEntity entity)";
		log.debug("开始执行方法：'" + funtionName + "'");
		if (entity == null) {
			log.debug("方法参数：entity 为空，方法结束");
			return;
		}
		log.debug("开始查找应用数据对象对应的图节点");
		QueryAesFigurenode execution = new QueryAesFigurenode(figureId, entity);
		Figurenode node = doExecution(execution);
		if (node == null) {
			log.debug("没有找到应用对象：'" + entity.getType() + ":" + entity.getId()
					+ "'对应的图节点，方法结束");
			return;
		}
		log.debug("开始查询当前应用对象所有的子节点");
		List<Figurenode> children = new ArrayList<Figurenode>();
		//yuan.yw add 2013-07-01
		List<BasicEntity> beList = this.doExecution(new QueryFigurenodeByPathExecution("/"+entity.getType()+"/"+this.getTableEntityIdValue(entity)+"/"));
		if(beList!=null && !beList.isEmpty()){
			int i=0;
			for(BasicEntity be:beList){
				if(i>0){
					ApplicationEntity ae = ApplicationEntity.changeFromEntity(be);
					CommandDeleteFigurenode dncmd = new CommandDeleteFigurenode(ae);
					addCommand(dncmd);
				}
				i++;
			}
		}
		getAllChildrenForDelete(node, children, new HashSet<Long>());
		log.debug("节点个数：" + children.size());
		for (Figurenode child : children) {
			CommandDeleteFigurenode dncmd = new CommandDeleteFigurenode(child);
			addCommand(dncmd);
			CommandDeleteFigurelineByNodeId dlcmd = new CommandDeleteFigurelineByNodeId(
					child.getId(), null);
			addCommand(dlcmd);
		}
		log.debug("方法'" + funtionName + "'执行结束。");
	}

	/**
	 * 递归删除所有的应用数据对象及其所有的关系
	 * 
	 * @param entity
	 */
	public void delApplicationEntityRecursion(ApplicationEntity entity) {
		String funtionName = "delApplicationEntityRecursion(ApplicationEntity entity)";
		log.debug("开始执行方法：'" + funtionName + "'");
		if (entity == null) {
			log.debug("方法参数：entity 为空，方法结束");
			return;
		}
		log.debug("开始查找应用数据对象对应的图节点");
		QueryAesFigurenode execution = new QueryAesFigurenode(figureId, entity);
		Figurenode node = doExecution(execution);
		if (node == null) {
			log.debug("没有找到应用对象：'" + entity.getType() + ":" + entity.getId()
					+ "'对应的图节点，方法结束");
			return;
		}
		log.debug("开始查询当前应用对象所有的子节点");
		List<Figurenode> children = new ArrayList<Figurenode>();
		//yuan.yw add 2013-07-01
		List<BasicEntity> beList = this.doExecution(new QueryFigurenodeByPathExecution("/"+entity.getType()+"/"+this.getTableEntityIdValue(entity)+"/"));
		if(beList!=null && !beList.isEmpty()){
			for(BasicEntity be:beList){
				ApplicationEntity ae = ApplicationEntity.changeFromEntity(be);
				ApplicationEntity e=this.doExecution(new QueryApplicationEntityByIdExecution(ae.getValue("entityType")+"",Long.valueOf(ae.getValue("entityId")+"")));
				CommandDeleteFigurenode dncmd = new CommandDeleteFigurenode(ae);
				addCommand(dncmd);
				CommandDeleteAppEntity acmd = new CommandDeleteAppEntity(e);
				addCommand(acmd);
			}
		}
		getAllChildrenForDelete(node, children, new HashSet<Long>());
		log.debug("节点个数：" + children.size());
		for (Figurenode child : children) {
			CommandDeleteFigurenode dncmd = new CommandDeleteFigurenode(child);
			addCommand(dncmd);
			CommandDeleteFigurelineByNodeId dlcmd = new CommandDeleteFigurelineByNodeId(
					child.getId(), null);
			addCommand(dlcmd);
			// 删除应用数据对象的命令
			CommandDeleteAppEntityById dacmd = new CommandDeleteAppEntityById(
					child.getEntityType(), child.getEntityId());
			addCommand(dacmd);
		}
		log.debug("方法'" + funtionName + "'执行结束。");
	}

	/**
	 * 递归删除所有的应用数据对象及其所有的关系
	 * 
	 * @param entity
	 */
	public void delApplicationEntityRecursionByFource(ApplicationEntity entity) {
		String funtionName = "delApplicationEntityRecursionByFource(ApplicationEntity entity)";
		log.debug("开始执行方法：'" + funtionName + "'");
		if (entity == null) {
			log.debug("方法参数：entity 为空，方法结束");
			return;
		}
		log.debug("开始查找应用数据对象对应的图节点");
		QueryAesFigurenode execution = new QueryAesFigurenode(figureId, entity);
		Figurenode node = doExecution(execution);
		if (node == null) {
			log.debug("没有找到应用对象：'" + entity.getType() + ":" + entity.getId()
					+ "'对应的图节点，方法结束");
			return;
		}
		log.debug("开始查询当前应用对象所有的子节点");
		List<Figurenode> children = new ArrayList<Figurenode>();
		//yuan.yw add 2013-07-01
		List<BasicEntity> beList = this.doExecution(new QueryFigurenodeByPathExecution("/"+entity.getType()+"/"+this.getTableEntityIdValue(entity)+"/"));
		if(beList!=null && !beList.isEmpty()){
			for(BasicEntity be:beList){
				ApplicationEntity ae = ApplicationEntity.changeFromEntity(be);
				ApplicationEntity e=this.doExecution(new QueryApplicationEntityByIdExecution(ae.getValue("entityType")+"",Long.valueOf(ae.getValue("entityId")+"")));
				CommandDeleteFigurenode dncmd = new CommandDeleteFigurenode(ae);
				addCommand(dncmd);
				CommandDeleteAppEntity acmd = new CommandDeleteAppEntity(e);
				addCommand(acmd);
			}
		}
		getAllChildrenForDeleteByFource(node, children, new HashSet<Long>());
		log.debug("节点个数：" + children.size());
		for (Figurenode child : children) {
			CommandDeleteFigurenode dncmd = new CommandDeleteFigurenode(child);
			addCommand(dncmd);
			CommandDeleteFigurelineByNodeId dlcmd = new CommandDeleteFigurelineByNodeId(
					child.getId(), null);
			addCommand(dlcmd);
			// 删除应用数据对象的命令
			CommandDeleteAppEntityById dacmd = new CommandDeleteAppEntityById(
					child.getEntityType(), child.getEntityId());
			addCommand(dacmd);
		}
		log.debug("方法'" + funtionName + "'执行结束。");
	}

	/**
	 * 删除两个对象之间的关系
	 * 
	 * @param left
	 * @param right
	 * @param figurelineType
	 * @updateauthor yuan.yw
	 * @updatedate 2013-07-01 
	 * @updatereason  上下级关系删除逻辑修改
	 */
	public void removeRelation(ApplicationEntity left, ApplicationEntity right,
			FigurelineType figurelineType) {
		String funtionName = "removeRelation(ApplicationEntity left, "
				+ "ApplicationEntity right,FigurelineType figurelineType)";
		log.debug("开始执行方法：'" + funtionName + "'");
		if (left == null) {
			log.debug("方法参数：left 为空，方法结束");
			return;
		}
		if (right == null) {
			log.debug("方法参数：right 为空，方法结束");
			return;
		}
		Figurenode ln = doExecution(new QueryAesFigurenode(figureId, left));
		if (ln == null) {
			log.debug("应用对象left对应的图节点为空，方法结束");
			return;
		}
		Figurenode rn = doExecution(new QueryAesFigurenode(figureId, right));
		if (rn == null) {
			log.debug("应用对象right对应的图节点为空，方法结束");
			return;
		}
		log.debug("创建删除边的命令");
		//yuan.yw add 2013-7-1
		if(figurelineType.equals(FigurelineType.CLAN)||figurelineType.equals(FigurelineType.CHILD)||figurelineType.equals(FigurelineType.PARENT)){
			String rnPath = "";
			if(right.getType().indexOf("Area")>=0){
				right.setValue("path","/"+this.getTableEntityIdValue(right)+"/");
				right.setValue("parent_id", 0);
				rnPath="/"+right.getType()+"/"+this.getTableEntityIdValue(right)+"/";
			}else{
				rnPath = "/"+right.getType()+"/"+this.getTableEntityIdValue(right)+"/";
			}
			this.updateFigurenodeAndCommand(right, rnPath,0);
		}else{
			deleteFigurelineCommand(ln, rn, figurelineType);
		}
		
		log.debug("方法'" + funtionName + "'执行结束。");
	}

	/**
	 * 查找指定对象、指定方向上的关联对象
	 * 
	 * @param begin
	 * @param figurelineType
	 * @return
	 */
	public ApplicationEntity[] getAssociateEntity(ApplicationEntity entity,
			FigurelineType figurelineType) {
		String funtionName = "getAssociateEntity(ApplicationEntity entity,"
				+ "FigurelineType figurelineType)";
		log.debug("开始执行方法：'" + funtionName + "'");
		log.debug("引用方法：'getAssociateEntity(ApplicationEntity entity,"
				+ "FigurelineType figurelineType, String sourceAenType)'");
		ApplicationEntity[] aes = getAssociateEntity(entity, figurelineType, "");
		log.debug("方法'" + funtionName + "'执行结束。");
		return aes;
	}

	/**
	 * 通过目标类型去筛选节点对象数据
	 * 
	 * @param entity
	 * @param figurelineType
	 * @param destinationAenType
	 * @return
	 */
	public ApplicationEntity[] getAssociateEntity(ApplicationEntity entity,
			FigurelineType figurelineType, String destinationAenType) {
		String funtionName = "getAssociateEntitygetAssociateEntity(ApplicationEntity entity,"
				+ "FigurelineType figurelineType, String destinationAenType)";
		log.debug("开始执行方法：'" + funtionName + "'");
		log.debug("方法参数：entity=" + entity);
		log.debug("方法参数：figurelineType=" + figurelineType);
		log.debug("方法参数：sourceAenType=" + destinationAenType);
		if (entity == null) {
			log.debug("方法参数begin为空，方法结束。");
			return new ApplicationEntity[] {};
		}
		if (figurelineType == null) {
			log.debug("方法参数figurelineType为空，方法结束。");
			return new ApplicationEntity[] {};
		}
		log.debug("开始查找应用数据对象对应的图节点");
		QueryAesFigurenode execution = new QueryAesFigurenode(figureId, entity);
		Figurenode aenode = doExecution(execution);
		if (aenode == null) {
			log.debug("没有找到应用对象：'" + entity.getType() + ":" + entity.getId()
					+ "'对应的图节点，方法结束");
			return new ApplicationEntity[] {};
		}
		log.debug("开始组建查询条件：BasicListFigurenodeExecution");
		BasicListFigurenodeExecution exe = new BasicListFigurenodeExecution(
				aenode, figurelineType, destinationAenType);
		log.debug("开始获取相应的邻接节点");
		List<Figurenode> nodes = getNodes(exe);
		List<ApplicationEntity> aearray = new ArrayList<ApplicationEntity>();
		for (Figurenode node : nodes) {
			ApplicationEntity ae = node.getApplicationEntity();
			if (ae != null) {
				aearray.add(ae);
			}
		}
		ApplicationEntity[] aes = new ApplicationEntity[aearray.size()];
		aearray.toArray(aes);
		log.debug("方法'" + funtionName + "'执行结束。");
		return aes;
	}

	/**
	 * 根据对象的部分条件,使用指定的对象去获取关联应用数据对象
	 * 
	 * @param entity
	 * @param figurelineType
	 * @param query
	 * @return
	 */
	public ApplicationEntity[] getAssociateEntity(ApplicationEntity entity,
			FigurelineType figurelineType, String query,String aetName) {
		if (query == null) {
			return getAssociateEntity(entity, figurelineType);
		}
		String funtionName = "getAssociateEntity(ApplicationEntity entity,"
				+ "FigurelineType figurelineType, Query query)";
		log.debug("开始执行方法：'" + funtionName + "'");
		log.debug("方法参数：entity=" + entity);
		log.debug("方法参数：figurelineType=" + figurelineType);
		if (entity == null) {
			log.debug("方法参数begin为空，方法结束。");
			return new ApplicationEntity[] {};
		}
		if (figurelineType == null) {
			log.debug("方法参数figurelineType为空，方法结束。");
			return new ApplicationEntity[] {};
		}
		log.debug("开始查找应用数据对象对应的图节点");
		QueryAesFigurenode execution = new QueryAesFigurenode(figureId, entity);
		Figurenode aenode = doExecution(execution);
		if (aenode == null) {
			log.debug("没有找到应用对象：'" + entity.getType() + ":" + entity.getId()
					+ "'对应的图节点，方法结束");
			return new ApplicationEntity[] {};
		}
		String destinationAenType = aetName;//query.getEntityOrClassName();
		log.debug("开始组建查询条件：BasicListFigurenodeExecution");
		BasicListFigurenodeExecution exe = new BasicListFigurenodeExecution(
				aenode, figurelineType, destinationAenType);
		log.debug("开始获取相应的邻接节点");
		List<Figurenode> nodes = getNodes(exe);
		if (nodes.size() == 0) {
			log.debug("没有找到相应的图节点。");
		} else {
			log.debug("关联的图节点个数为:" + nodes.size() + ",准备进一步筛选符合条件的应用数据对象");
		}
		String[] aeids = new String[nodes.size()];
		int i = 0;
		for (Figurenode node : nodes) {
			aeids[i++] = Long.toString(node.getEntityId());
		}
		QueryApplicationEntityByIdsExecution qaisExe = new QueryApplicationEntityByIdsExecution(
				query, aeids,aetName);
		log.debug("准备执行查询");
		List<ApplicationEntity> aearray = doExecution(qaisExe);
		ApplicationEntity[] aes = new ApplicationEntity[aearray.size()];
		aearray.toArray(aes);
		log.debug("方法'" + funtionName + "'执行结束。");
		return aes;
	}

	// public ApplicationEntity[] getAssociateEntity(
	// ListFigurenodeExecution mynodes) {
	// List<Figurenode> nodes = getNodes(mynodes);
	// if (nodes.isEmpty()) {
	// return new ApplicationEntity[] {};
	// }
	// ApplicationEntity[] aes = new ApplicationEntity[nodes.size()];
	// for (int i = 0; i < nodes.size(); i++) {
	// aes[i] = nodes.get(i).getApplicationEntity();
	// }
	// return aes;
	// }

	/**
	 * 执行命令队列内的命令
	 */
	public int storeFigure() {
		int result=1;
		while (cmdList.hasElements()) {
			Command cmd = cmdList.pop();
			if(cmd.doExecution(contextFactory)<=0){
				result=0;
				break;
			}
		}
		return result;
		
	}

	/**
	 * 查找开始节点到结束节点所有路径。
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<List<ApplicationEntity>> traceRoad(ApplicationEntity start,
			ApplicationEntity end) {
		// 校验参数
		String functionName = "traceRoad(ApplicationEntity start,ApplicationEntity end)";
		log.debug("开始执行方法:'" + functionName + "'.");
		Figurenode startnode = doExecution(new QueryAesFigurenode(figureId,
				start));
		Figurenode endnode = doExecution(new QueryAesFigurenode(figureId, end));
		if (startnode == null || endnode == null) {
			log.debug("没有参数start/end对应的图节点，方法结束");
			return new ArrayList<List<ApplicationEntity>>();
		}
		// 获取开始、结束节点的ID
		long startId = startnode.getId();
		long endId = endnode.getId();
		log.debug("开始查找下面两个节点的路径：'" + startId + "'->'" + endId + "'");
		log.debug("1、加载图'" + figureId + "'下的所有边。");
		// 加载图中所有边对象
		// 将这些边对象组成路由所使用的参数
		List<Figureline> mylines = doExecution(new QueryAllFigurelineExecution(
				figureId));
		log.debug("加载回来的边个数是：" + mylines.size());
		log.debug("2、准备使用这些边去组建路由条件。");
		// KEY是左节点ID，VALUE是相应的边列表。
		HashMap<String, List<Figureline>> leftlines = new HashMap<String, List<Figureline>>();
		// KEY是右节点ID，VALUE是相应的边列表。
		HashMap<String, List<Figureline>> rightlines = new HashMap<String, List<Figureline>>();
		groupingFigurelineForTraceRoad(leftlines, rightlines, mylines);
		log.debug("3、开始进行路由查询");
		// 进行路由查询
		List<List<String>> result = new ArrayList<List<String>>();
		traceRoad(startId, endId, new Stack(), leftlines, rightlines, result);
		// 查找这些节点ID对应的节点对象
		log.debug("开始查询节点ID对应的节点对象");
		HashSet<String> myids = new HashSet<String>();
		for (List<String> ids : result) {
			myids.addAll(ids);
		}
		if (myids.isEmpty()) {
			log.debug("节点对象的ID列表为空，方法结束。");
			return new ArrayList<List<ApplicationEntity>>();
		}
		log.debug("节点ID的个数：" + myids.size());
		Map<String, Figurenode> forlastResult = doExecution(new QueryFigurenodeByIdsExecution(
				myids));
		log.debug("开始获取数据对象以及组建结果列表");
		List<List<ApplicationEntity>> appentity = new ArrayList<List<ApplicationEntity>>();
		for (List<String> listIds : result) {
			ArrayList<ApplicationEntity> app = new ArrayList<ApplicationEntity>();
			for (String listId : listIds) {
				Figurenode n = forlastResult.get(listId);
				n.figure = this;
				app.add(n.getApplicationEntity());
			}
			appentity.add(app);
		}
		log.debug("方法'" + functionName + "'执行成功");
		return appentity;
	}

	/*------------------默认方法--------------------------*/
	/**
	 * 往命令队列中增加命令
	 */
	void addCommand(Command cmd) {
		cmdList.push(cmd);
	}

	/**
	 * 清空命令队列
	 */
	void clearCommand() {
		cmdList.clear();
	}

	/**
	 * 设置加载图对象时用到的开始参数
	 * 
	 * @param begin
	 */
	void setBegin(ApplicationEntity begin) {
		this.begin = begin;
	}

	/*------------------私有方法--------------------------*/
	/**
	 * 通过自定义的条件去获取节点对象
	 */
	private List<Figurenode> getNodes(ListFigurenodeExecution mynodes) {
		mynodes.setFigureId(figureId);
		mynodes.set(this);
		List<Figurenode> nodes = doExecution(mynodes);
		for (Figurenode node : nodes) {
			node.figure = this;
		}
		return nodes;
	}

	/**
	 * 增加节点的命令
	 * 
	 * @param entity
	 * @return
	 * @updateauthor yuan.yw
	 * @updatedate 2013-06-28 
	 * @updatereason 增加path传参
	 */
	private Figurenode createFigurenodeAndCommand(ApplicationEntity entity) {
		CommandSaveAppEntity sacmd = new CommandSaveAppEntity(
				entity);
		addCommand(sacmd);
		Context createContext = this.contextFactory.CreateContext();
		long enid = 0;
		if(createContext != null){
			enid = StructurePrimary.getEntityPrimaryKey("Figurenode", createContext);
		}
		String path = "/"+entity.getType()+"/"+this.getTableEntityIdValue(entity)+"/";
		Figurenode node = new Figurenode(figureId, entity, this,enid,path,0);
		//Figurenode node = new Figurenode(figureId, entity, this,enid);
		CommandSaveOrUpdateFigurenode sfcmd = new CommandSaveOrUpdateFigurenode(
				node);
		addCommand(sfcmd);
		return node;
	}
	/**
	 * 
	 * @description: 创建或更新节点的命令
	 * @author：
	 * @param entity
	 * @return     
	 * @return Figurenode     
	 * @date：Aug 2, 2012 11:38:28 AM
	 * @updateauthor yuan.yw
	 * @updatedate 2013-06-28 
	 * @updatereason 增加path传参
	 */
	private Figurenode createOrUpdateFigurenodeAndCommand(ApplicationEntity entity){
		CommandSaveOrUpdateAppEntity sacmd = new CommandSaveOrUpdateAppEntity(
				entity);
		addCommand(sacmd);
		Context createContext = this.contextFactory.CreateContext();
		long enid = 0;
		if(createContext != null){
			enid = StructurePrimary.getEntityPrimaryKey("Figurenode", createContext);
		}
		String path = "/"+entity.getType()+"/"+this.getTableEntityIdValue(entity)+"/";
		Figurenode node = new Figurenode(figureId, entity, this,enid,path,0);
		CommandSaveOrUpdateFigurenode sfcmd = new CommandSaveOrUpdateFigurenode(
				node);
		addCommand(sfcmd);
		return node;
	}
	/**
	 * 
	 * @description: 更新节点的命令
	 * @author：yuan.yw
	 * @param entity
	 * @param path
	 * @return     
	 * @return Figurenode     
	 * @date：Jun 28, 2013 3:36:44 PM
	 */
	private Figurenode updateFigurenodeAndCommand(ApplicationEntity entity,String path,long parentId){
		CommandSaveOrUpdateAppEntity sacmd = new CommandSaveOrUpdateAppEntity(
				entity);
		addCommand(sacmd);
		Figurenode node = new Figurenode(figureId, entity, this,0,path,parentId);
		CommandSaveOrUpdateFigurenode sfcmd = new CommandSaveOrUpdateFigurenode(
				node);
		addCommand(sfcmd);
		return node;
	}
	/**
	 * 
	 * @description: 更新节点的命令
	 * @author：yuan.yw
	 * @param entity
	 * @return     
	 * @return Figurenode     
	 * @date：Jun 28, 2013 3:36:44 PM
	 */
	private void updateFigurenodeCommand(ApplicationEntity entity){
		CommandSaveOrUpdateFigurenode sfcmd = new CommandSaveOrUpdateFigurenode(
				entity);
		addCommand(sfcmd);
	}
	/**
	 * 增加边的命令
	 * 
	 * @param left
	 * @param right
	 * @param figurelineType
	 * @return
	 */
	private Figureline createFigurelineAndCommand(Figurenode left,
			Figurenode right, FigurelineType figurelineType) {
		Figureline line;
		Context createContext = this.contextFactory.CreateContext();
		long enid = 0;
		if(createContext != null){
			enid = StructurePrimary.getEntityPrimaryKey("Figureline", createContext);
		}
		switch (figurelineType) {
		case CLAN:
			line = new Figureline(figureId, left, right, FigurelineType.CLAN,enid);
			break;
		case PARENT:
			line = new Figureline(figureId, right, left, FigurelineType.CLAN,enid);
			break;
		case CHILD:
			line = new Figureline(figureId, left, right, FigurelineType.CLAN,enid);
			break;
		case LINK:
			line = new Figureline(figureId, left, right, FigurelineType.LINK,enid);
			break;
		case FORWORD:
			line = new Figureline(figureId, left, right, FigurelineType.LINK,enid);
			break;
		case BACKWORD:
			line = new Figureline(figureId, right, left, FigurelineType.LINK,enid);
			break;
		default:
			return null;
		}
		CommandSaveOrUpdateFigureline slcmd = new CommandSaveOrUpdateFigureline(
				line);
		addCommand(slcmd);
		return line;
	}

	/**
	 * 删除边的命令
	 * 
	 * @param left
	 * @param right
	 * @param figurelineType
	 */
	private void deleteFigurelineCommand(Figurenode left, Figurenode right,
			FigurelineType figurelineType) {
		Map<String, Object> condition = new HashMap<String, Object>();
		long lnid = left.getId();
		long rnid = right.getId();
		CommandDeleteFigureline cmd;
		switch (figurelineType) {
		case CLAN:
			condition.put(Figureline.LEFTID_KEY, lnid);
			condition.put(Figureline.RIGHTID_KEY, rnid);
			condition.put(Figureline.TYPE_KEY, FigurelineType.CLAN);
			cmd = new CommandDeleteFigureline(condition);
			addCommand(cmd);
			break;
		case PARENT:
			condition.put(Figureline.RIGHTID_KEY, lnid);
			condition.put(Figureline.LEFTID_KEY, rnid);
			condition.put(Figureline.TYPE_KEY, FigurelineType.CLAN);
			cmd = new CommandDeleteFigureline(condition);
			addCommand(cmd);
			break;
		case CHILD:
			condition.put(Figureline.LEFTID_KEY, lnid);
			condition.put(Figureline.RIGHTID_KEY, rnid);
			condition.put(Figureline.TYPE_KEY, FigurelineType.CLAN);
			cmd = new CommandDeleteFigureline(condition);
			addCommand(cmd);
			break;
		case LINK:
			condition.put(Figureline.LEFTID_KEY, lnid);
			condition.put(Figureline.RIGHTID_KEY, rnid);
			condition.put(Figureline.TYPE_KEY, FigurelineType.LINK);
			cmd = new CommandDeleteFigureline(condition);
			addCommand(cmd);
			condition = new HashMap<String, Object>();
			condition.put(Figureline.RIGHTID_KEY, lnid);
			condition.put(Figureline.LEFTID_KEY, rnid);
			condition.put(Figureline.TYPE_KEY, FigurelineType.LINK);
			cmd = new CommandDeleteFigureline(condition);
			addCommand(cmd);
			break;
		case FORWORD:
			condition.put(Figureline.RIGHTID_KEY, lnid);
			condition.put(Figureline.LEFTID_KEY, rnid);
			condition.put(Figureline.TYPE_KEY, FigurelineType.LINK);
			cmd = new CommandDeleteFigureline(condition);
			addCommand(cmd);
			break;
		case BACKWORD:
			condition.put(Figureline.LEFTID_KEY, lnid);
			condition.put(Figureline.RIGHTID_KEY, rnid);
			condition.put(Figureline.TYPE_KEY, FigurelineType.LINK);
			cmd = new CommandDeleteFigureline(condition);
			addCommand(cmd);
			break;
		}
	}

	/**
	 * 递归获取某个父的子对象，用于删除
	 * 
	 * @param parent
	 * @param children
	 * @param nids
	 */
	private void getAllChildrenForDelete(Figurenode parent,
			List<Figurenode> children, HashSet<Long> nids) {
		long parentId = parent.getId();
		if (nids.contains(parentId)) {
			return;
		}
		List<String> pids = doExecution(new QueryParentIdExecution(parentId));
		if (pids.size() > 1) {
			for (String pid : pids) {
				// 如果目标节点有多个父，并且不是所有的父都在删除列表，则结束
				// 本次运算
				if (!nids.contains(pid)) {
					return;
				}
			}
		}
		children.add(parent);
		nids.add(parentId);
		BasicListFigurenodeExecution exe = new BasicListFigurenodeExecution(
				parent, FigurelineType.CLAN, "");
		log.debug("开始获取相应的邻接节点");
		List<Figurenode> nodes = getNodes(exe);
		for (Figurenode node : nodes) {
			getAllChildrenForDelete(node, children, nids);
		}
	}

	/**
	 * 递归获取某个父的子对象，用于删除
	 * 
	 * @param parent
	 * @param children
	 * @param nids
	 */
	private void getAllChildrenForDeleteByFource(Figurenode parent,
			List<Figurenode> children, HashSet<Long> nids) {
		long parentId = parent.getId();
		if (nids.contains(parentId)) {
			return;
		}
		children.add(parent);
		nids.add(parentId);
		BasicListFigurenodeExecution exe = new BasicListFigurenodeExecution(
				parent, FigurelineType.CLAN, "");
		List<Figurenode> nodes = getNodes(exe);
		for (Figurenode node : nodes) {
			getAllChildrenForDeleteByFource(node, children, nids);
		}
	}

	private void groupingFigurelineForTraceRoad(
			HashMap<String, List<Figureline>> leftlines,
			HashMap<String, List<Figureline>> rightlines,
			List<Figureline> mylines) {
		for (Figureline line : mylines) {
			long leftId = line.getLeftId();
			long rightId = line.getRightId();
			List<Figureline> lllist = leftlines.get(leftId);
			List<Figureline> rllist = rightlines.get(rightId);
			if (lllist == null) {
				lllist = new ArrayList<Figureline>();
				leftlines.put(Long.toString(leftId), lllist);
			}
			if (rllist == null) {
				rllist = new ArrayList<Figureline>();
				rightlines.put(Long.toString(rightId), rllist);
			}
			lllist.add(line);
			rllist.add(line);
		}
	}

	/**
	 * 路游检索,输入开始、结束节点的ID,获取这两个节点之间的路径
	 * 
	 * @param start
	 * @param end
	 * @param s
	 * @param es
	 * @param result
	 * @param entityCache
	 */
	private void traceRoad(long start, long end, Stack s,
			HashMap<String, List<Figureline>> leftLines,
			HashMap<String, List<Figureline>> rightLines,
			List<List<String>> result) {
		log.debug("stack info:" + s.getStackElements());
		if (s.contains(start)) {
			return;
		}
		s.push(start);
		if (start == end) {
			List<Object> os = s.getStackElements();
			List<String> vids = new ArrayList<String>();
			for (Object o : os) {
				vids.add(o.toString());
			}
			result.add(vids);
			s.pop();
			return;
		}
		Collection<String> nexts = getRelatedFigurenodeIds(leftLines,
				rightLines, start);
		if (nexts == null || nexts.isEmpty()) {
			s.pop();
			return;
		}
		for (String next : nexts) {
			traceRoad(Long.parseLong(next), end, s, leftLines, rightLines, result);
		}
		s.pop();
	}

	private Collection<String> getRelatedFigurenodeIds(
			HashMap<String, List<Figureline>> leftLines,
			HashMap<String, List<Figureline>> rightLines, long start) {
		HashSet<String> myIds = new HashSet<String>();
		HashSet<Figureline> ls = new HashSet<Figureline>();
		List<Figureline> lls = leftLines.get(start);
		List<Figureline> rls = rightLines.get(start);
		if (lls != null) {
			ls.addAll(lls);
		}
		if (rls != null) {
			ls.addAll(rls);
		}
		for (Figureline line : ls) {
			long leftId = line.getLeftId();
			long rightId = line.getRightId();
			if (leftId == rightId) {
				continue;
			}
			FigurelineType type = line.getLineType();
			if (type.equals(FigurelineType.CLAN)) {
				if (leftId == start) {
					myIds.add(Long.toString(rightId));
				}
			}
			if (type.equals(FigurelineType.LINK)) {
				if (leftId == start) {
					myIds.add(Long.toString(rightId));
				} else if (rightId == start) {
					myIds.add(Long.toString(leftId));
				}
			}
		}
		return myIds;
	}
	/**
	 * 
	 * @description: 获取entity里面的id值(由于区域表id列为area_id 加以区分)
	 * @author：yuan.yw
	 * @param entity     
	 * @return void     
	 * @date：Jul 1, 2013 4:36:39 PM
	 */
	private long getTableEntityIdValue(ApplicationEntity entity){
		if(entity==null){
			return 0;
		}
		if(entity.getType().indexOf("Area")>=0){
			return entity.getValue("area_id");
		}else{
			return entity.getValue("id");
		}
	}
}
