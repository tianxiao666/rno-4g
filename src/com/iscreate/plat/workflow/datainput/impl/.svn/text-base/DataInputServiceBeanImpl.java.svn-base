package com.iscreate.plat.workflow.datainput.impl;

import org.hibernate.Query;
import org.hibernate.Transaction;


import com.iscreate.plat.workflow.*;
import com.iscreate.plat.workflow.dataconfig.*;
import com.iscreate.plat.workflow.datainput.*;

import org.hibernate.Session;
import java.sql.*;
import java.util.*;

public class DataInputServiceBeanImpl implements DataInputServiceBean {
	// 实例信息入库
	public FlowInstanceInfo instanceDataInput(String flowId,
			String InstanceInfoType, String ObjectId,String userId)
			throws WFException {
		FlowInstanceInfo flowInstanceInfo = new FlowInstanceInfo();
		FlowBeanFactory flowBeanFactory = new FlowBeanFactory();
		ExtDataConfigBean config = (ExtDataConfigBean) flowBeanFactory
				.getFlowBean(BeanType.Bean_Type_ExtDataConfig);
		Session session=SessionFactory.getSession();
		FlowInfo flowInfo = config.getFlowInfo(flowId);
		//  提取实例id的值
		Query query = session
				.createSQLQuery(" select max(instance_id) from flow_instance ");
		Object obj = query.uniqueResult();
		String instance_id = obj.toString();
		// 实例状态
		String status="";
		List list = config.getNodes(flowId) ;
		Iterator iterator=list.iterator();
		while(iterator.hasNext()){
			FlowNodeInfo node=(FlowNodeInfo)iterator.next();
			if(node.node_type.equals(Constant.NodeType.NODE_TYPE0)){
				status = node.node_id;
				break;
			}
		}
		// 提取摘要
		String summary;
		try{
		//TODO 对summary_sql的检测
		String sql = flowInfo.summary_sql.replaceFirst("?",ObjectId );		
		Connection conn=session.connection();
		Statement statement = conn.createStatement();
		ResultSet res=statement.executeQuery(sql);
		res.next();
		summary=res.getString(1);
		}catch(Exception ex){
			throw new WFException("提取摘要失败！ 提取的sql="+flowInfo.summary_sql);
		}
		
		//赋值
		flowInstanceInfo.flow_id=flowId;
		flowInstanceInfo.instance_id=instance_id;
		flowInstanceInfo.instance_status=status;
		flowInstanceInfo.instance_type=InstanceInfoType;
		flowInstanceInfo.object_id=ObjectId;
		flowInstanceInfo.summary=summary;
		flowInstanceInfo.create_user_id=userId;
		flowInstanceInfo.create_time = new java.util.Date();
		// 实例信息入库
/*		Transaction tx = session.beginTransaction();  
		session.save(flowInstanceInfo);
		tx.commit();*/
		

		return flowInstanceInfo;

	}

	// 任务信息入库
	public FlowTaskInfo doTask(String flowId,
			String nodeType, String ObjectId,String userId)
			throws WFException {
		// 为任务设置完成信息。
		return null;
	}

	public FlowTaskInfo setTaskForUser(FlowTaskInfo task)
			throws WFException {
		// 添加一条记录表示 某人有代办事情。
		return null;

	}

	// 处理意见
	public NotionInfo notionDataInput(NotionInfo notionInfo)
			throws WFException {
		return null;
	}

	// 送阅读
	public void sendRead(String instanceId, String[] userIds)
			throws WFException {
		// TODO 最后做

	}

	// 实例状态改变
	public FlowInstanceInfo instanceStatusUpdate(FlowInstanceInfo instance
		) throws WFException {
		// 实例的状态就是环节

		return null;
	}
}
