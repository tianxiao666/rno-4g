package com.iscreate.plat.networkresource.structure.instance;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;

import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.engine.figure.Figure;
import com.iscreate.plat.networkresource.engine.figure.Figurenode;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryAesFigurenode;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryApplicationEntityByFigurenodeIdsExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryFigurelineIdsByIdsExecution;
import com.iscreate.plat.networkresource.engine.tool.Queue;
import com.iscreate.plat.networkresource.engine.tool.Stack;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.LogicEnvironment;
import com.iscreate.plat.networkresource.structure.instance.bizlogic.StructureBizLogic;
import com.iscreate.plat.networkresource.structure.template.AssociatedType;
import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleNode;

abstract class BizLogic_GetRecursionEntityAbstrace implements
		StructureBizLogic<ApplicationEntity[]> {

	public final String PARENTRECURSION = "PARENTRECURSION";
	public final String CHILDRECURSION = "CHILDRECURSION";

	protected ApplicationEntity begin;
	protected String aetName;
	protected LogicEnvironment logicEnv;
	protected String recursionType = "";
	protected String figureId = "";//figureId
	protected String className ="";
	protected int searchtimes =0;
	public ApplicationEntity[] bizLogic(LogicEnvironment logicEnv) {
		ApplicationEntity[] aes = null;
		this.logicEnv = logicEnv;
		this.figureId = this.logicEnv.get(Figure.class).getFigureId()+"";
		// 获取查询方案
		getScheme();
		if("Structure_BizLogic_GetRecursionParent".equals(this.className)||"Structure_BizLogic_GetRecursionEntity".equals(this.className)){
			Figure figure = this.logicEnv.get(Figure.class);
			QueryAesFigurenode execution = new QueryAesFigurenode(Long.valueOf(figureId), begin);
			Figurenode aenode = figure.doExecution(execution);
			List<ApplicationEntity> des = null;
			if(aenode!=null){
				List<String> beginNode = new ArrayList<String>();
				beginNode.add(aenode.getValue("id")+"");
				List<String> beginSameNode = getSameTypeFigureNodeIds(beginNode,begin.getType()); //递归获取同样类型的子应用对象的主键id数组（包括自身） 
				if(!aetName.equals(begin.getType())){
					beginSameNode = getFigureNodeIds(beginSameNode,begin.getType()); //递归获取指定目标类型的子应用对象的主键id数组 （查询路径）
				}
				if(beginSameNode!=null && !beginSameNode.isEmpty()){
					Figure f = this.logicEnv.get(Figure.class);
					QueryApplicationEntityByFigurenodeIdsExecution exe = new QueryApplicationEntityByFigurenodeIdsExecution(aetName, beginSameNode);
					des = f.doExecution(exe);
					if(des!=null){
						aes = new ApplicationEntity[des.size()];
						des.toArray(aes);
					}
				}	
			}
		}else{
			// 根据查询方案加载应用数据对象
			List<ApplicationEntity> des = loadDestinationObject();
			aes = new ApplicationEntity[des.size()];
			des.toArray(aes);		
		}
		return aes;
	}

	private Map<String, List<StructureModuleNode>> scheme = new HashMap<String, List<StructureModuleNode>>();
	private List<List<Object>> searchRoad = new ArrayList<List<Object>>();
	// 父子关系的节点, key是左右节点名称的字符串
	private HashMap<String, StructureModuleNode> clannode = new HashMap<String, StructureModuleNode>();
	
	// 同类型应用数据对象形成父子关系
	private boolean selfContain = false;
	//set 查询路径 用于判断是否已查
	private HashSet<String> searchPath = new HashSet<String>();
	
	private List<ApplicationEntity> loadDestinationObject() {
		List<ApplicationEntity> des = new ArrayList<ApplicationEntity>();
		Queue<ApplicationEntity> queue = new Queue<ApplicationEntity>();
		HashSet<String> idcache = new HashSet<String>();
		queue.push(begin);
		// 记录查询的次数
		int searchTime = 0;
		while (queue.hasElements()) {
			ApplicationEntity entity = queue.pop();
			String mytype = entity.getType();
			String myid = Long.toString(entity.getId());
			// 判断ID重复，如果重复跳出该查询
			if (idcache.contains(myid)) {
				continue;
			} else {
				idcache.add(myid);
			}
			// 判断目标对象，如果是目标对象，则存入目标列表中
			// 第一个应用对象就算类型相同，也不是目标对象,所以searchTime>0
			if (mytype.equals(aetName) && searchTime > 0) {
				LogFactory.getLog(getClass()).debug(
						"[method loadDestinationObject]当前数据类型：" + mytype
								+ ",查询次数：" + searchTime);
				des.add(entity);
				continue;
			}
			// 如果查询计划中没有当前数据对象类型的下一步查询计划，进入下一个查询
			if (!scheme.containsKey(mytype)) {
				// 如果是自包含，必须进行一次查询
				if (selfContain && searchTime == 0) {
					Map<String, Object> condition = new HashMap<String, Object>();
					condition.put("source", begin);
					condition.put("destinationAenType", "NULL");
					searchTime++;
					// 执行抽象方法，加载应用数据
					ApplicationEntity[] result = loadEntity(condition);
					for (ApplicationEntity e : result) {
						queue.push(e);
					}
				}
				continue;
			}
			List<StructureModuleNode> nextScheme = scheme.get(mytype);
			// 遍历方案中的节点，加载应用数据
			for (StructureModuleNode node : nextScheme) {
				String destinationAenType = "";
				// 递归查找父 或 子，目标类型不一样
				if (CHILDRECURSION.equals(recursionType)) {
					destinationAenType = node.getRightEntity();
				}else{
					destinationAenType = node.getLeftEntity();
				}
				// aetName为目标应用数据对象的类型名称，如果下一个关联类型destinationAenType与
				// aetName相同，则通过query对象去筛选目标对象
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("source", entity);
				condition.put("destinationAenType", destinationAenType);
				// 每次查询，计数器加1
				searchTime++;
				// 执行抽象方法，加载应用数据
				ApplicationEntity[] result = loadEntity(condition);
				// 结构都推入队列中
				for (ApplicationEntity e : result) {
					queue.push(e);
				}
			}

		}
		return des;
	}

	// 抽象方法，为结构实例与结构视图之间的实现差异
	protected abstract ApplicationEntity[] loadEntity(
			Map<String, Object> condition);

	/**
	 * 获取加载方案
	 */
	private void getScheme() {
		StructureModule module = logicEnv.get(StructureModule.class);
		// 获取模板中的所有连接节点
		Collection<StructureModuleNode> nodes = module.getModuleNodes();
		// 两个缓存对象
		// 该缓冲对象的KEY是连接节点的左节点ID
		HashMap<String, List<StructureModuleNode>> nodeCache = new HashMap<String, List<StructureModuleNode>>();
		// 遍历所有连接节点，将它们分类
		for (StructureModuleNode node : nodes) {
			// 下面的代码是建立链接节点的缓冲
			// 仅父子关系
			if (node.getAssociatedType() == AssociatedType.CLAN) {
				// keyAetName为查找路由时，获取关联节点的依据
				String keyAetName = "";
				// key为从路由中获取查询方案的关联节点的依据
				String key = "";
				if (CHILDRECURSION.equals(recursionType)) {
					keyAetName = node.getLeftEntity();
					key = node.getLeftEntity() + ":" + node.getRightEntity();
				} else {
					keyAetName = node.getRightEntity();
					key = node.getRightEntity() + ":" + node.getLeftEntity();
				}
				LogFactory.getLog(getClass()).debug(
						"[method loadDestinationObject]所有关联节点进行缓存的KEY："+key);
				List<StructureModuleNode> leftnode = nodeCache.get(keyAetName);
				if (leftnode == null) {
					leftnode = new ArrayList<StructureModuleNode>();
					nodeCache.put(keyAetName, leftnode);
				}
				leftnode.add(node);
				clannode.put(key, node);
			}
		}
		// 所有的查询路径
		searchScheme(begin.getType(), this.aetName, new Stack(), nodeCache,
				new HashSet<String>());

		// 根据路由信息，从clannode组建查询方案
		for (List<Object> tr : searchRoad) {
			for (int i = 1; i < tr.size(); i++) {
				String key = tr.get(i - 1) + ":" + tr.get(i);
				LogFactory.getLog(getClass()).debug(
						"[method loadDestinationObject]从路由中提取查询方案的KEY："+key);
				if (clannode.containsKey(key)) {
					StructureModuleNode node = clannode.get(key);
					// 将节点模板加入scheme
					List<StructureModuleNode> array = scheme.get(node
							.getLeftEntity());
					if (array == null) {
						array = new ArrayList<StructureModuleNode>();
						// 递归查找父 或 子，在scheme中缓存的KEY值也不一样
						if(CHILDRECURSION.equals(recursionType)){
							scheme.put(node.getLeftEntity(), array);
						}else{
							scheme.put(node.getRightEntity(), array);
						}
					}
					array.add(node);
				}
			}
		}
	}

	/**
	 * 在leftnode 、rightnode 查找两个模板之间的路由情况
	 * 
	 * @param start
	 * @param end
	 * @param s
	 * @param leftnode
	 * @param rightnode
	 * @param schemeCache
	 */
	private void searchScheme(String start, String end, Stack s,
			HashMap<String, List<StructureModuleNode>> leftnode,
			HashSet<String> schemeCache) {
		if (s.contains(start)) {
			return;
		}
		s.push(start);
		if (start.equals(end) && s.getStackElements().size() > 1) {
			List<Object> os = s.getStackElements();
			searchRoad.add(os);
			s.pop();
			return;
		}
		Collection<String> nexts = getRelatedFigurenodeIds(leftnode, start);
		if (nexts == null || nexts.isEmpty()) {
			s.pop();
			return;
		}
		for (String next : nexts) {
			searchScheme(next, end, s, leftnode, schemeCache);
		}
		s.pop();
	}

	/**
	 * 获取关联的节点ID列表
	 * 
	 * @param leftnode
	 * @param start
	 * @return
	 */
	private Collection<String> getRelatedFigurenodeIds(
			HashMap<String, List<StructureModuleNode>> leftnode, String start) {
		// 指定节点ID的关联ID
		HashSet<String> myIds = new HashSet<String>();
		HashSet<StructureModuleNode> ls = new HashSet<StructureModuleNode>();
		// 需要遍历的节点数组
		if (leftnode.get(start) != null) {
			ls.addAll(leftnode.get(start));
		}
		for (StructureModuleNode node : ls) {
			String leftAetName = node.getLeftEntity();
			String rightAetName = node.getRightEntity();
			if (leftAetName.equals(rightAetName)) {
				// 如果该节点是自包含，且与目标对象类型相同，selfContain设置为true
				if (leftAetName.equals(aetName)) {
					selfContain = true;
				}
				continue;
			}
			// 递归查询子对象时，所需要定位的关联类型
			if (leftAetName.equals(start)
					&& CHILDRECURSION.equals(recursionType)) {
				myIds.add(rightAetName);
			} else
			// 递归查询父对象时，所需要定义的关联类型
			if (rightAetName.equals(start)
					&& PARENTRECURSION.equals(recursionType)) {
				myIds.add(leftAetName);
			}
		}
		return myIds;
	}
	/**
	 * 
	 * @description: 递归获取同样类型的子应用对象的主键id数组（包括自身） 
	 * @author：
	 * @param nodeIds
	 * @return     
	 * @return String[]     
	 * @date：Jan 17, 2013 2:53:43 PM
	 */
	private List<String> getSameTypeFigureNodeIds(List<String> nodeIds,String aettype){
		List<String> resultList = new ArrayList<String>();
		if(searchtimes==0){
			if(!aetName.equals(aettype)){
				resultList.addAll(nodeIds);
			}
		}else {
			resultList.addAll(nodeIds);
		}
		Figure figure = this.logicEnv.get(Figure.class);
		QueryFigurelineIdsByIdsExecution queryFigurelineIdsByIdsExecution = new QueryFigurelineIdsByIdsExecution(nodeIds,recursionType,aettype,this.figureId);
		List<String> idList = figure.doExecution(queryFigurelineIdsByIdsExecution);
		searchtimes++;
		if(idList!=null && !idList.isEmpty()){
			//resultList.addAll(nodeIds);
			if(aetName.equals(aettype)){
				resultList.addAll(idList);
			}else{
				List<String> searchList = getSameTypeFigureNodeIds(idList,aettype);
				if(searchList!=null && !searchList.isEmpty()){
					resultList.addAll(searchList);
				}
			}
		}
		return resultList;
		
	}
	
	/**
	 * 
	 * @description: 递归获取指定目标类型的子应用对象的主键id数组 （查询路径）
	 * @author：
	 * @param nodeIds
	 * @return     
	 * @return String[]     
	 * @date：Jan 17, 2013 2:53:43 PM
	 */
	private List<String> getFigureNodeIds(List<String> nodeIds,String aettype){
		
		List<String> resultList = new ArrayList<String>();

		Figure figure = this.logicEnv.get(Figure.class);
		List<StructureModuleNode> nextScheme = scheme.get(aettype);
		// 遍历方案中的节点，加载应用数据
		if(nextScheme!=null&&!nextScheme.isEmpty()){
			for (StructureModuleNode node : nextScheme) {
				String destinationAenType = "";
				// 递归查找父 或 子，目标类型不一样
				if (CHILDRECURSION.equals(recursionType)) {
					destinationAenType = node.getRightEntity();
					if(searchPath.contains(aettype+"_"+destinationAenType)){//判断查询路径是否已查询过
						continue;
					}else{
						searchPath.add(aettype+"_"+destinationAenType);
					}
				}else{
					destinationAenType = node.getLeftEntity();
					if(searchPath.contains(destinationAenType+"_"+aettype)){
						continue;
					}else{
						searchPath.add(destinationAenType+"_"+aettype);
					}
				}
				QueryFigurelineIdsByIdsExecution queryFigurelineIdsByIdsExecution = new QueryFigurelineIdsByIdsExecution(nodeIds,recursionType,destinationAenType,this.figureId);
				List<String> idList = figure.doExecution(queryFigurelineIdsByIdsExecution);
				if(idList!=null && !idList.isEmpty()){
					if(aetName.equals(destinationAenType)){
						resultList.addAll(idList);
						continue;
					}else{
						List<String> searchList = getFigureNodeIds(idList,destinationAenType);
						if(searchList!=null && !searchList.isEmpty()){
							resultList.addAll(searchList);
						}
					}
				}else{
					continue;
				}
			}
		}
		
		return resultList;
	}
	/**
	 * 
	 * @description: 加载数据
	 * @author：
	 * @return     
	 * @return List<ApplicationEntity>     
	 * @date：Jan 17, 2013 4:51:39 PM
	 */
/*	public ApplicationEntity[] bizLogicRecursion(LogicEnvironment logicEnv) {
		ApplicationEntity[] aes = null;
		this.logicEnv = logicEnv;
		// 获取查询方案
		getScheme();
		// 根据查询方案加载应用数据对象
		//List<ApplicationEntity> des = loadDestinationObject();
		Figure figure = this.logicEnv.get(Figure.class);
		QueryAesFigurenode execution = new QueryAesFigurenode(Long.valueOf(figureId), begin);
		Figurenode aenode = figure.doExecution(execution);
		List<ApplicationEntity> des = null;
		if(aenode!=null){
			List<String> beginNode = new ArrayList<String>();
			beginNode.add(aenode.getValue("id")+"");
			List<String> beginSameNode = getSameTypeFigureNodeIds(beginNode,begin.getType()); //递归获取同样类型的子应用对象的主键id数组（包括自身） 
			if(!aetName.equals(begin.getType())){
				beginSameNode = getFigureNodeIds(beginSameNode,begin.getType()); //递归获取指定目标类型的子应用对象的主键id数组 （查询路径）
			}
			figure = this.logicEnv.get(Figure.class);
			QueryApplicationEntityByFigurenodeIdsExecution exe = new QueryApplicationEntityByFigurenodeIdsExecution(aetName, beginSameNode);
			des = figure.doExecution(exe);
			if(des!=null){
				aes = new ApplicationEntity[des.size()];
				des.toArray(aes);
			}
		}
		return aes;
	}*/
}
