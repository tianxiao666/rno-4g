package com.iscreate.plat.workflow.processor.jbpm.extend.cmd;
//package com.iscreate.workflow.processor.jbpm.extend.cmd;
//
//import org.jbpm.api.cmd.Command;
//import org.jbpm.api.cmd.Environment;
//import org.jbpm.pvm.internal.session.DbSession;
//
//import com.iscreate.workflow.WFException;
//import com.iscreate.workflow.datainput.FlowTaskInfo;
//import com.iscreate.workflow.processor.constants.ProcessConstants;
//
///**
// * 任务状态转移
// * @author brightming
// *
// */
//public class TransferTaskStatusCmd implements Command {
//
//	private String toStatus;
//	private String taskId;
//	public TransferTaskStatusCmd(){
//		
//	}
//	public TransferTaskStatusCmd(String toStatus,String taskId){
//		this.toStatus=toStatus;
//		this.taskId=taskId;
//	}
//	
//	@Override
//	public Object execute(Environment environment) throws Exception {
//		
//		//获取当前的任务
//		DbSession session=environment.get(DbSession.class);
//		FlowTaskInfo fi=session.get(FlowTaskInfo.class, taskId);
//		if(fi==null){
//			throw new WFException("不存在id为:"+taskId+"的任务！");
//		}
//		
//		String currentStatus=fi.getTaskStatus();
//		if(!canTransfer(currentStatus,toStatus)){
//			throw new WFException("任务当前状态为:"+currentStatus+",不能转向状态："+toStatus);
//		}
//		transferStatus(currentStatus,toStatus);
//		
//		return null;
//	}
//	
//	private void transferStatus(String currentStatus,String toStatus){
//		if(ProcessConstants.TaskStatus.CANCELED.equals(toStatus)){
//			//取消所在流程
//		}
//		else if(ProcessConstants.TaskStatus.STOPPED.equals(toStatus)){
//			//结束所在流程
//		}
//		else if(ProcessConstants.TaskStatus.COMPLETED.equals(toStatus)){
//			//完成任务
//			
//		}
//		
//	}
//	
//	private boolean canTransfer(String currentStatus,String toStatus){
//		
//		if(ProcessConstants.TaskStatus.INIT.equals(currentStatus)){
//			//未领取状态，只能转向“已领取”，“撤销”
//			if(ProcessConstants.TaskStatus.CANCELED.equals(toStatus) ||
//					ProcessConstants.TaskStatus.BEEN_TAKEN.equals(toStatus)){
//				return true;
//			}else{
//				return false;
//			}
//		}
//		//已领取状态
//		else if(ProcessConstants.TaskStatus.BEEN_TAKEN.equals(currentStatus)){
//			if(ProcessConstants.TaskStatus.HANDLING.equals(toStatus) ||
//					ProcessConstants.TaskStatus.CANCELED.equals(toStatus)){
//				return true;
//			}else{
//				return false;
//			}
//		}
//		//当前是处理中
//		//不能转向完成
//		else if(ProcessConstants.TaskStatus.HANDLING.equals(currentStatus)){
//			if(ProcessConstants.TaskStatus.SUSPENDED.equals(toStatus) ){
//				return true;
//			}else{
//				return false;
//			}
//		}
//		//当前是暂停
//		else if(ProcessConstants.TaskStatus.SUSPENDED.equals(currentStatus)){
//			if(ProcessConstants.TaskStatus.STOPPED.equals(toStatus) ||
//					ProcessConstants.TaskStatus.HANDLING.equals(toStatus)){
//				return true;
//			}else{
//				return false;
//			}
//		}
//		
//		//其他状态不可流转
//		return false;
//	}
//
//}
