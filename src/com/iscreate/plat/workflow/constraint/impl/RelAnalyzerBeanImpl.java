package com.iscreate.plat.workflow.constraint.impl;

import com.iscreate.plat.workflow.*;
import com.iscreate.plat.workflow.constraint.*;
import com.iscreate.plat.workflow.dataconfig.*;

import java.util.*;

public class RelAnalyzerBeanImpl implements RelAnalyzerBean {

	public FlowRelation.Relation analyzeFlowRelation(String flowIdA,
			String flowIdB) throws WFException {
		// 提取FlowIdA 的所有节点信息
		// 查看是否有节点上允许发送流程，如果有看flowIdB是否在其中，发送流程的形式是等待类型还是非等待类型。
		ExtDataConfigBean extDataConfig = (ExtDataConfigBean) FlowBeanFactory
				.getFlowBean(BeanType.Bean_Type_ExtDataConfig);
		List list = extDataConfig.getNodes(flowIdA);
		for (int i = 0; i < list.size(); i++) {
			FlowNodeInfo nodeInfo = (FlowNodeInfo) list.get(i);
			if (nodeInfo.other_flow_ids != null
					&& nodeInfo.other_flow_ids.indexOf(flowIdB
							+ Constant.FlowIdSeparator.Separator_1+"Y") >= 0) {
				return FlowRelation.Relation.Relation_1;
			}
			
			if (nodeInfo.other_flow_ids != null
					&& nodeInfo.other_flow_ids.indexOf(flowIdB
							+ Constant.FlowIdSeparator.Separator_1+"N") >= 0) {
				return FlowRelation.Relation.Relation_2;
			}
		}

		// 同样查询flowIdB 的所有节点信息
		// 查看是否有节点上允许发送流程，如果有看flowIdA是否在其中，发送流程的形式是等待类型还是非等待类型。

		return FlowRelation.Relation.Relation_0;
	}

}
