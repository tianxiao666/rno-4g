package com.iscreate.plat.workflow.dataconfig;

import com.iscreate.plat.workflow.*;

import  java.util.*;

/****
 * 
 * 为流程定义的扩展配置信息
 * 
 * FLOW_INFO
 * 
 * FLOW_NODE_INFO
 * 
 * FLOW_NODE_USER_REL
 * 
 * 
 * @author iscreate
 *
 */
public interface ExtDataConfigBean extends FlowBean {
   
	/** 获取所有流程   **/
	public List<FlowInfo>  flowInfos()throws WFException;
   
    
	public FlowInfo   getFlowInfo(String flowId)throws WFException;
    
	public List<FlowNodeInfo>  getNodes(String flowId) throws WFException;
    
    public List<String>  getNodeActionEvents(String nodeId) throws WFException;
	
    public List<FlowInfo>  getFlowInfosSendByNode(String nodeId)throws WFException;
    
    public FlowNodeInfo   getNodeInfoById(String nodeId)throws WFException;
    
    public List<FlowNodeInfo>  getNextNodes(String nodeId)throws WFException;
    
    public List<FlowNodeInfo>  getNodes()throws WFException;
   
	
	
	
}
