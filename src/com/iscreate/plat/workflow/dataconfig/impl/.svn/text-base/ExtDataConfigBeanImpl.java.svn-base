package com.iscreate.plat.workflow.dataconfig.impl;

import java.sql.Connection;
import java.util.*;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.iscreate.plat.workflow.*;
import com.iscreate.plat.workflow.dataconfig.*;

public class ExtDataConfigBeanImpl implements ExtDataConfigBean {
	public HashMap<String, FlowInfo> flows = new HashMap();// 存放流程信息

	public HashMap<String, FlowNodeInfo> nodes = new HashMap();

	public HashMap<String, FlowRoutingInfo> routings = new HashMap();

	public HashMap<String, FlowNodeUserRel> nodeUsers = new HashMap();

	public ExtDataConfigBeanImpl() {
		this.init();
	}

	private void init() {

		System.out.println("从数据库获取配置信息");
		Session session=null;
		try {
			session = SessionFactory.getSession();

			List list = null;
			// 装载 FlowInfo 信息
			String sql0 = " from FlowInfo  ";
			list = session.createQuery(sql0).list();
			for (int i = 0; i < list.size(); i++) {
				FlowInfo object = (FlowInfo) list.get(i);
				flows.put(object.flow_id, object);
			}

			/** *** TODO 加过滤条件 **** */
			String sql1 = " from FlowNodeInfo   ";
			list = session.createQuery(sql1).list();
			for (int i = 0; i < list.size(); i++) {
				FlowNodeInfo object = (FlowNodeInfo) list.get(i);
				nodes.put(object.node_id, object);
			}

			String sql2 = " from FlowNodeUserRel  ";
			list = session.createQuery(sql2).list();
			for (int i = 0; i < list.size(); i++) {
				FlowNodeUserRel object = (FlowNodeUserRel) list.get(i);
				nodeUsers.put(object.node_user_Id, object);
			}

			String sql3 = " from FlowRoutingInfo  ";
			list = session.createQuery(sql3).list();
			for (int i = 0; i < list.size(); i++) {
				FlowRoutingInfo object = (FlowRoutingInfo) list.get(i);
				routings.put(object.routingId, object);
			}

		} catch (Exception e) {
			// throw new WFException(e.getMessage());
			e.printStackTrace();
		}finally{
			if (session != null) {
				session.close();
			}
		}

	}

	
	

	public List<FlowNodeInfo> getNodes(String flowId) throws WFException {
		ArrayList list = new ArrayList();
		Iterator iterator = this.nodes.entrySet().iterator();
		while (iterator.hasNext()) {
			FlowNodeInfo flowNodeInfo = (FlowNodeInfo) iterator.next();
			if (flowNodeInfo.flow_Id.equals(flowId))
				list.add(flowNodeInfo);
		}
		return list;
	}

	public List<FlowNodeInfo> getNodes() throws WFException {
		ArrayList list = new ArrayList();
		Iterator iterator = this.nodes.entrySet().iterator();
		while (iterator.hasNext()) {
			FlowNodeInfo flowNodeInfo = (FlowNodeInfo) iterator.next();
			list.add(flowNodeInfo);
		}
		return list;
	}

	/** 获取所有流程 * */
	public List<FlowInfo> flowInfos() throws WFException {
		List list = new ArrayList();
		Iterator iterator = flows.entrySet().iterator();
		while (iterator.hasNext())
			list.add(iterator.next());
		return list;
	}

	public FlowInfo getFlowInfo(String flowId) throws WFException {
		return (FlowInfo) flows.get(flowId);
	}

	/***************************************************************************
	 * 
	 * 获取事件信息；配置用"#"隔开
	 * 
	 **************************************************************************/
	public List<String> getNodeActionEvents(String nodeId) throws WFException {
		List list = new ArrayList();
		String evntStr = this.nodes.get(nodeId).getEventlist();
		if (evntStr != null) {
			String[] array = evntStr
					.split(Constant.FlowIdSeparator.Separator_0);
			for (int i = 0; i < array.length; i++)
				list.add(array[i]);
		}
		return list;
	}

	/***************************************************************************
	 * 
	 * 
	 * 
	 * 当前节点能发送多少流程; Other_flow_ids 流程Id号 用#87=Y#98=N隔开
	 * 
	 **************************************************************************/
	public List<FlowInfo> getFlowInfosSendByNode(String nodeId)
			throws WFException {
		String flow_ids = this.nodes.get(nodeId).getOther_flow_ids();
		if (flow_ids != null) {
			String[] ids = flow_ids.split(Constant.FlowIdSeparator.Separator_0);
			List list = new ArrayList();
			for (int i = 0; i < ids.length; i++) {
				list.add(this.flows.get(ids[i]));
			}
			return list;
		} else {

		}
		return null;
	}

	public FlowNodeInfo getNodeInfoById(String nodeId) throws WFException {

		return this.nodes.get(nodeId);
	}

	public List<FlowNodeInfo> getNextNodes(String nodeId) throws WFException {
		List list = new ArrayList();
		Iterator iterator = this.routings.entrySet().iterator();
		while (iterator.hasNext()) {
			FlowRoutingInfo rInfo = (FlowRoutingInfo) iterator.next();
			if (rInfo.start_node_id.equals(nodeId))
				list.add(rInfo);
		}
		return list;
	}

}
