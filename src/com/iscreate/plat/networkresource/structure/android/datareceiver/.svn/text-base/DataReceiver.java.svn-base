package com.iscreate.plat.networkresource.structure.android.datareceiver;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iscreate.plat.networkresource.application.tool.ApplicationEntity;
import com.iscreate.plat.networkresource.common.tool.BasicEntity;
import com.iscreate.plat.networkresource.common.tool.DefaultParam;
import com.iscreate.plat.networkresource.common.tool.Entity;
import com.iscreate.plat.networkresource.dataservice.ContextFactory;
import com.iscreate.plat.networkresource.engine.figure.Figureline;
import com.iscreate.plat.networkresource.engine.figure.FigurelineType;
import com.iscreate.plat.networkresource.engine.figure.Figurenode;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandCreateNewFigure;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandDeleteAppEntityById;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandDeleteFigureline;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandDeleteFigurelineByNodeId;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandDeleteFigurenode;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandSaveOrUpdateAppEntity;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandSaveOrUpdateFigureline;
import com.iscreate.plat.networkresource.engine.figure.cmd.CommandSaveOrUpdateFigurenode;
import com.iscreate.plat.networkresource.engine.figure.execution.BasicListFigurenodeExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryAesFigurenode;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryApplicationEntityByIdExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryApplicationEntityByIdsExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryFigureIdByNameExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryFigureNamesExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryFigurenodeByIdsExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryOneFigureNameExecution;
import com.iscreate.plat.networkresource.engine.figure.execution.QueryParentIdExecution;
import com.iscreate.plat.networkresource.structure.template.StructureModule;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntity;
import com.iscreate.plat.networkresource.structure.template.StructureModuleEntityGroup;
import com.iscreate.plat.networkresource.structure.template.StructureModuleNode;
import com.iscreate.plat.networkresource.structure.template.execution.QueryStructureModuleByName;

public class DataReceiver {
	private Gson gson = new Gson();

	private ContextFactory contextFactory;

	private final String DELAE = "DELAE"; // 删除应用数据对象
	private final String SOUAE = "SOUAE"; // 保存或者更新应用数据对象
	private final String GETAE = "GETAE"; // 获取应用数据对象
	private final String GETAES = "GETAES"; // 获取应用数据对象列表
	private final String CRF = "CRF"; // 创建图对象
	private final String GETFID = "GETFID"; // 获取图ID
	private final String GETFNM = "GETFNM"; // 获取图名称
	private final String GETFNMS = "GETFNMS"; // 获取图名称列表
	private final String DELFN = "DELFN"; // 删除图节点
	private final String SOUFN = "SOUFN"; // 保存或者更新图节点
	private final String GETFN = "GETFN"; // 获取图节点
	private final String GETFN2 = "GETFN2"; // 获取图节点
	private final String GETAFNS = "GETAFNS";// 获取关联的图节点列表
	private final String GETPIDS = "GETPIDS";// 获取父节点ID列表
	private final String DELFL = "DELFL";
	private final String DELFL2 = "DELFL2";
	private final String SOUFL = "SOUFL";
	private final String TRACEROAD = "TRACEROAD";
	private final String GETSTM = "GETSTM";

	public Object ReceiverNetworkData(String data) {
		DataAnalysis ans = new DataAnalysis(data);
		return executeCmd(ans);
	}

	public void setContextFactory(ContextFactory contextFactory) {
		this.contextFactory = contextFactory;
	}

	private Object executeCmd(DataAnalysis ans) {
		Object result = null;
		if (DELAE.equals(ans.getCmd())) {
			result = doCommandDeleteAppEntityById(ans);
		} else if (SOUAE.equals(ans.getCmd())) {
			result = doCommandSaveOrUpdateAppEntity(ans);
		} else if (GETAE.equals(ans.getCmd())) {
			result = doQueryApplicationEntityByIdExecution(ans);
		} else if (GETAES.equals(ans.getCmd())) {
			result = doQueryApplicationEntityByIdsExecution(ans);
		} else if (CRF.equals(ans.getCmd())) {
			result = doCommandCreateNewFigure(ans);
		} else if (GETFID.equals(ans.getCmd())) {
			result = doQueryFigureIdByNameExecution(ans);
		} else if (GETFNM.equals(ans.getCmd())) {
			result = doQueryOneFigureNameExecution(ans);
		} else if (GETFNMS.equals(ans.getCmd())) {
			result = doQueryFigureNamesExecution(ans);
		} else if (DELFN.equals(ans.getCmd())) {
			result = doCommandDeleteFigurenode(ans);
		} else if (SOUFN.equals(ans.getCmd())) {
			result = doCommandSaveOrUpdateFigurenode(ans);
		} else if (GETFN.equals(ans.getCmd())) {
			result = doQueryAesFigurenode(ans);
		} else if (GETFN2.equals(ans.getCmd())) {
			result = doQueryFigurenodeByIdsExecution(ans);
		} else if (GETAFNS.equals(ans.getCmd())) {
			result = doBasicListFigurenodeExecution(ans);
		} else if (GETPIDS.equals(ans.getCmd())) {
			result = doQueryParentIdExecution(ans);
		} else if (DELFL.equals(ans.getCmd())) {
			result = doCommandDeleteFigurelineByNodeId(ans);
		} else if (DELFL2.equals(ans.getCmd())) {
			result = doCommandDeleteFigureline(ans);
		} else if (SOUFL.equals(ans.getCmd())) {
			result = doCommandSaveOrUpdateFigureline(ans);
		} else if (TRACEROAD.equals(ans.getCmd())) {

		} else if (GETSTM.equals(ans.getCmd())) {
			result = doQueryStructureModuleByName(ans);
		}

		return result;
	}

	// /**
	// * 保存或更新节点边对象
	// *
	// * @param ans
	// * @return
	// */
	// private String doCommandSaveOrUpdateFigureline(DataAnalysis ans) {
	// String s = ans.getData("s");
	// Map<String, String> smap = gson.fromJson(s,
	// new TypeToken<Map<String, String>>() {
	// }.getType());
	// BasicEntity sbe = BasicEntity.fromMap(smap);
	// ApplicationEntity start = ApplicationEntity.changeFromEntity(start);
	// String e = ans.getData("e");
	// Map<String, String> emap = gson.fromJson(e,
	// new TypeToken<Map<String, String>>() {
	// }.getType());
	// BasicEntity ebe = BasicEntity.fromMap(emap);
	// ApplicationEntity end = ApplicationEntity.changeFromEntity(ebe);
	// return "";
	// }

	/**
	 * 保存或更新节点边对象
	 * 
	 * @param ans
	 * @return StructureModule
	 *         有四部分组成，每部分的数据用$分隔。数据的顺序分别为StructureModule、ModuleEntitys
	 *         ,ModuleEntityGroups,EntityNode
	 */
	private String doQueryStructureModuleByName(DataAnalysis ans) {
		String n = ans.getData("n");
		long domainId = 1l;
		StructureModule module = new QueryStructureModuleByName(domainId, n)
				.doExecution(contextFactory);
		Map<String, StructureModuleEntity> smes = module.getModuleEntitys();
		Map<String, StructureModuleEntityGroup> smegs = module
				.getModuleEntityGroups();
		Collection<StructureModuleNode> ns = module.getModuleNodes();
		// 对象的转换
		// StructureModule 转换
		String result1 = gson.toJson(module.toMap());
		// StructureModuleEntity 转换
		Map<String, Map<String, Object>> forResult2 = new HashMap<String, Map<String, Object>>();
		for (String key : smes.keySet()) {
			forResult2.put(key, smes.get(key).toMap());
		}
		String result2 = gson.toJson(forResult2);
		// StructureModuleEntityGroup 转换
		Map<String, Map<String, Object>> forResult3 = new HashMap<String, Map<String, Object>>();
		for (String key : smegs.keySet()) {
			forResult3.put(key, smes.get(key).toMap());
		}
		String result3 = gson.toJson(forResult3);
		// StructureModuleNode 转换
		Collection<Map<String, Object>> forResult4 = new ArrayList<Map<String, Object>>();
		for (StructureModuleNode node : ns) {
			forResult4.add(node.toMap());
		}
		String result4 = gson.toJson(forResult4);
		String result = result1 + "$" + result2 + "$" + result3 + "$" + result4;
		return result;
	}

	/**
	 * 保存或更新节点边对象
	 * 
	 * @param ans
	 * @return 操作状态
	 */
	private String doCommandSaveOrUpdateFigureline(DataAnalysis ans) {
		String json = ans.getData("fl");
		Map<String, String> map = gson.fromJson(json,
				new TypeToken<Map<String, String>>() {
				}.getType());
		BasicEntity be = BasicEntity.fromMap(map);
		Figureline line = Figureline.changeFromEntity(be);
		// 执行并获取结果
		int result = new CommandSaveOrUpdateFigureline(line)
				.doExecution(contextFactory);
		return result + "";
	}

	/**
	 * 删除节点边对象
	 * 
	 * @param ans
	 * @return 操作状态
	 */
	private String doCommandDeleteFigureline(DataAnalysis ans) {
		String json = ans.getData("con");
		Map<String, String> condition = gson.fromJson(json,
				new TypeToken<Map<String, String>>() {
				}.getType());
		// 执行并获取结果
		int result = new CommandDeleteFigureline(condition)
				.doExecution(contextFactory);
		return result + "";
	}

	/**
	 * 删除节点边对象
	 * 
	 * @param ans
	 * @return 操作状态
	 */
	private String doCommandDeleteFigurelineByNodeId(DataAnalysis ans) {
		long nid = Long.parseLong(ans.getData("nid"));
		String at = ans.getData("at");
		FigurelineType type = switchToFigurelineType(at);
		int result = new CommandDeleteFigurelineByNodeId(nid, type)
				.doExecution(contextFactory);
		return result + "";
	}

	/**
	 * 保存或者更新图节点对象命令
	 * 
	 * @param ans
	 * @return List<String> 转换而成的json字符串
	 */
	private String doQueryParentIdExecution(DataAnalysis ans) {
		long cid = Long.parseLong(ans.getData("cid"));
		List<String> ids = new QueryParentIdExecution(cid)
				.doExecution(contextFactory);
		String result = gson.toJson(ids);
		return result;
	}

	/**
	 * 保存或者更新图节点对象命令
	 * 
	 * @param ans
	 * @return List<Map<String,Object>> 转换而成的json字符串
	 */
	private String doBasicListFigurenodeExecution(DataAnalysis ans) {
		String nid = ans.getData("nid");
		String at = ans.getData("at");
		FigurelineType type = switchToFigurelineType(at);
		String dt = ans.getData("dt");
		Entity e = new Entity();
		e.setValue(DefaultParam.typeKey, Figurenode.MY_TYPE);
		e.setValue(Figurenode.ID_KEY, nid);
		Figurenode fn = Figurenode.changeFromEntity(e);
		// 执行并获取结果
		List<Figurenode> nodes = new BasicListFigurenodeExecution(fn, type, dt)
				.doExecution(contextFactory);
		List<Map<String, Object>> forResult = new ArrayList<Map<String, Object>>();
		for (Figurenode node : nodes) {
			forResult.add(node.toMap());
		}
		String result = gson.toJson(forResult);
		return result;
	}

	/**
	 * 关联类型转换
	 * 
	 * @param at
	 * @return
	 */
	private FigurelineType switchToFigurelineType(String at) {
		if ("CLAN".equals(at)) {
			return FigurelineType.CLAN;
		} else if ("LINK".equals(at)) {
			return FigurelineType.LINK;
		} else if ("PARENT".equals(at)) {
			return FigurelineType.PARENT;
		} else if ("CHILD".equals(at)) {
			return FigurelineType.CHILD;
		} else if ("FORWORD".equals(at)) {
			return FigurelineType.FORWORD;
		} else if ("BACKWORD".equals(at)) {
			return FigurelineType.BACKWORD;
		} else {
			return FigurelineType.CLAN;
		}
	}

	/**
	 * 保存或者更新图节点对象命令
	 * 
	 * @param ans
	 * @return Map<String,Map<String,Object>>转换而成的json字符串
	 */
	private String doQueryFigurenodeByIdsExecution(DataAnalysis ans) {
		String json = ans.getData("nids");
		Collection<String> nids = gson.fromJson(json,
				new TypeToken<Collection<String>>() {
				}.getType());
		// 执行并获取结果
		Map<String, Figurenode> nodes = new QueryFigurenodeByIdsExecution(nids)
				.doExecution(contextFactory);
		Map<String, Map<String, Object>> forResult = new HashMap<String, Map<String, Object>>();
		for (String key : nodes.keySet()) {
			forResult.put(key, nodes.get(key).toMap());
		}
		String result = gson.toJson(forResult);
		return result;
	}

	/**
	 * 保存或者更新图节点对象命令
	 * 
	 * @param ans
	 * @return Map<String,Object>转换而成的json字符串
	 */
	private String doQueryAesFigurenode(DataAnalysis ans) {
		long fid = Long.parseLong(ans.getData("fid"));
		String t = ans.getData("t");
		String id = ans.getData("id");
		Entity e = new Entity();
		e.setValue(DefaultParam.typeKey, t);
		e.setValue(DefaultParam.idKey, id);
		ApplicationEntity ae = ApplicationEntity.changeFromEntity(e);
		// 执行并获取结果
		Figurenode node = new QueryAesFigurenode(fid, ae)
				.doExecution(contextFactory);
		Map<String, Object> forResult = node.toMap();
		String result = gson.toJson(forResult);
		return result;
	}

	/**
	 * 保存或者更新图节点对象命令
	 * 
	 * @param ans
	 * @return
	 */
	private String doCommandSaveOrUpdateFigurenode(DataAnalysis ans) {
		String json = ans.getData("fn");
		Map<String, String> map = gson.fromJson(json,
				new TypeToken<Map<String, String>>() {
				}.getType());
		BasicEntity be = BasicEntity.fromMap(map);
		Figurenode ae = Figurenode.changeFromEntity(be);
		int result = new CommandSaveOrUpdateFigurenode(ae)
				.doExecution(contextFactory);
		return result + "";
	}

	/**
	 * 删除图节点对象命令
	 * 
	 * @param ans
	 * @return
	 */
	private String doCommandDeleteFigurenode(DataAnalysis ans) {
		String fid = ans.getData("fid");
		String nid = ans.getData("nid");
		Entity e = new Entity();
		e.setValue(DefaultParam.typeKey, Figurenode.MY_TYPE);
		e.setValue(Figurenode.FIGUREID_KEY, fid);
		e.setValue(Figurenode.ID_KEY, nid);
		Figurenode fn = Figurenode.changeFromEntity(e);
		int result = new CommandDeleteFigurenode(fn)
				.doExecution(contextFactory);
		return result + "";
	}

	/**
	 * 通过应用数据对象查找图名称列表
	 * 
	 * @param ans
	 * @return String[] 转换而成的json字符串
	 */
	private String doQueryFigureNamesExecution(DataAnalysis ans) {
		String t = ans.getData("t");
		String id = ans.getData("id");
		Entity e = new Entity();
		e.setValue(DefaultParam.typeKey, t);
		e.setValue(DefaultParam.idKey, id);
		ApplicationEntity ae = ApplicationEntity.changeFromEntity(e);
		// 执行并获取结果
		String[] forResult = new QueryFigureNamesExecution(ae)
				.doExecution(contextFactory);
		String result = gson.toJson(forResult);
		return result;
	}

	/**
	 * 通过应用数据对象查找一个图名称
	 * 
	 * @param ans
	 * @return
	 */
	private String doQueryOneFigureNameExecution(DataAnalysis ans) {
		String t = ans.getData("t");
		String id = ans.getData("id");
		Entity e = new Entity();
		e.setValue(DefaultParam.typeKey, t);
		e.setValue(DefaultParam.idKey, id);
		ApplicationEntity ae = ApplicationEntity.changeFromEntity(e);
		String result = new QueryOneFigureNameExecution(ae)
				.doExecution(contextFactory);
		return result;
	}

	/**
	 * 创建新图对象的命令
	 * 
	 * @param ans
	 * @return
	 */
	private long doQueryFigureIdByNameExecution(DataAnalysis ans) {
		String n = ans.getData("n");
		long result = new QueryFigureIdByNameExecution(n)
				.doExecution(contextFactory);
		return result;
	}

	/**
	 * 创建新图对象的命令
	 * 
	 * @param ans
	 * @return
	 */
	private String doCommandCreateNewFigure(DataAnalysis ans) {
		String n = ans.getData("n");
		long id = Long.parseLong(ans.getData("id"));
		int result = new CommandCreateNewFigure(n, id)
				.doExecution(contextFactory);
		return result + "";
	}

	/**
	 * 查询应用数据对象的命令
	 * 
	 * @param ans
	 * @return List<Map<String,Object>>转换而成的json字符串
	 */
	private String doQueryApplicationEntityByIdsExecution(DataAnalysis ans) {
		String t = ans.getData("t");
		String ids = ans.getData("ids");
		String[] myids = gson.fromJson(ids, new TypeToken<String[]>() {
		}.getType());
		//Query q = new Query(t);
		String q="entity_type='"+t+"'";
		List<ApplicationEntity> aes = new QueryApplicationEntityByIdsExecution(
				q, myids,t).doExecution(contextFactory);
		List<Map<String, Object>> forResult = new ArrayList<Map<String, Object>>();
		for (ApplicationEntity ae : aes) {
			forResult.add(ae.toMap());
		}
		String result = gson.toJson(forResult);
		return result;
	}

	/**
	 * 查询应用数据对象的命令
	 * 
	 * @param ans
	 * @return Map<String,Object>转换而成的json字符串
	 */
	private String doQueryApplicationEntityByIdExecution(DataAnalysis ans) {
		String t = ans.getData("t");
		long id = Long.parseLong(ans.getData("id"));
		ApplicationEntity ae = new QueryApplicationEntityByIdExecution(t, id)
				.doExecution(contextFactory);
		String result = gson.toJson(ae.toMap());
		return result;
	}

	/**
	 * 创建保存或者更新应用数据对象的命令
	 * 
	 * @param ans
	 * @return
	 */
	private String doCommandSaveOrUpdateAppEntity(DataAnalysis ans) {
		String json = ans.getData("ae");
		Map<String, String> map = gson.fromJson(json,
				new TypeToken<Map<String, String>>() {
				}.getType());
		BasicEntity be = BasicEntity.fromMap(map);
		ApplicationEntity ae = ApplicationEntity.changeFromEntity(be);
		int result = new CommandSaveOrUpdateAppEntity(ae)
				.doExecution(contextFactory);
		return result + "";
	}

	/**
	 * 创建通过ID删除应用数据对象的名称
	 * 
	 * @param ans
	 * @return
	 */
	private String doCommandDeleteAppEntityById(DataAnalysis ans) {
		String t = ans.getData("t");
		long id = Long.parseLong(ans.getData("id"));
		int result = new CommandDeleteAppEntityById(t, id)
				.doExecution(contextFactory);
		return result + "";
	}

	// 传入的数据格式如下：
	// CMD&key=val&key=val
	private class DataAnalysis {
		private String CMD_KEY = "CMD";
		private String splitKey = "&";
		Map<String, String> data = new HashMap<String, String>();

		public DataAnalysis(String data) {
			if (data == null) {
				return;
			}
			String key = "";
			String val = "";
			if (data.indexOf(splitKey) > 0) {
				val = data.substring(0, data.indexOf(splitKey));
				data = data.substring(data.indexOf(splitKey) + 1);
				this.data.put(CMD_KEY, val);
			}
			String[] kvs = data.split(splitKey);
			for (String kv : kvs) {
				if (kv.indexOf("=") > 0) {
					key = kv.substring(0, kv.indexOf("="));
					val = kv.substring(kv.indexOf("=") + 1);
					this.data.put(key, val);
				}
			}
		}

		public String getCmd() {
			String cmd = data.get(CMD_KEY);
			if (cmd == null) {
				cmd = "";
			}
			return cmd;
		}

		public String getData(String key) {
			String result = data.get(key);
			if (result == null) {
				result = "";
			}
			return result;
		}
	}
}
