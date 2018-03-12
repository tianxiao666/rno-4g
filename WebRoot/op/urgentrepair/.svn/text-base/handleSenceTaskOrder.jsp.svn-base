<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>现场任务单</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="css/fault.css" />
<link rel="stylesheet" type="text/css" href="../../css/input.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/gantt/gantt_small.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
<link type="text/css" rel="stylesheet" href="../../jslib/jquery/css/jquery-ui-1.8.23.custom.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="jslib/public.js"></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="../jslib/networkResourceViewDivPage.js"></script>
<script type="text/javascript" src="../jslib/baseStationSingleDivPage.js"></script>
<script type="text/javascript" src="jslib/dataOfSelect.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="jslib/pageSection.js"></script>
<script type="text/javascript" src="jslib/toPageControl.js"></script>
<script type="text/javascript" src="../jslib/pageSection.js"></script>
<script type="text/javascript" src="jslib/handleSenceTaskOrder.js"></script>
<script type="text/javascript" src="../../jslib/propertyDictionary.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
<script type="text/javascript" src="../../jslib/gantt/gantt.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js"></script>

<style type="text/css">

.selectStation-title{ width:100%-1px; text-align:left; padding-left:10px; border:1px solid #99BBE8; color:#15428B; font-size:13px; font-weight:bold; background:url(../../images/white-top-bottom.gif) repeat-x; line-height:30px;}
.selectStation-main{ width:100%; margin:10px 0; height:395px; overflow:scroll; overflow-x:hidden;}
.selectStation-bottom{ width:100%; text-align:center; margin:5px 0;}
.selectStation-table{ width:100%;}
.selectStation-table tr{ border:1px solid #fff; border-bottom:1px solid #CCCCCC;}
.selectStation-table tr td{padding:2px 5px; border-left:1px solid #fff; width:25%; white-space:nowrap;}
.selectStation-table tr td:hover{ background:#E8EDFF; color:#0033FF; cursor:pointer; border:1px solid #ccc;}
input[type="radio"]{background:none; border:none;}
.info {
	position: absolute;
	border: 1px solid #000000;
	width: 200px;
	margin:15% auto,auto;
	left: 40%;
	background-color: #FFFFFF;
	color: #000;
	font-size: 20px;
	font-style: bold;
	font-family: 微软雅黑;
	vertical-align: middle;
	z-index:250;
	top:70%;
}
#processDiv span{font-weight:normal;}

.personnel_info_table input[type="radio"]{left: 0; position: static; top: 0px;}

</style>
</head>

<body>
	<%-- 记录人员所隶属的事业部 --%>
	<div id="loading_div" class="loading_div" style="display:none">
		<div class="loading_img">
        	<img src="../../images/loading_img.gif" /><br />
        	数据加载中，请稍后...
        </div>
	</div>
	<input type="hidden" id="STATUS" name="STATUS" value="${scene_taskOrderInfo['status']}" />
	<input type="hidden" id="WOSTATUS" name="WOSTATUS" value="${scene_workOrderInfo['status']}" />
	<input type="hidden" id="currentHandler" value="${scene_taskOrderInfo['currentHandler']}" />
	<input type="hidden" id="city" value="${scene_workOrderInfo['city']}" />
	<input type="hidden" id="baseStationId" value="${scene_workOrderInfo['baseStationId']}" />
	<input type="hidden" id="baseStationType" value="${scene_workOrderInfo['baseStationType']}" />
	<input type="hidden" id="customerWoType" value="${scene_workOrderInfo['customerWoType']}" />
	<input type="hidden" id="WOID" name="WOID" value="${scene_workOrderInfo['woId']}" />
	<input type="hidden" id="TOID" name="TOID" value="${scene_taskOrderInfo['toId']}" />
	<input type="hidden" id="hasPower" value="${scene_taskOrderInfo['hasPower']}" />
	<input type="hidden" id="acceptProfessional_value" value="${scene_workOrderInfo['acceptProfessional']}" />
	<input type="hidden" id="faultLevel" name="faultLevel" value="${scene_workOrderInfo.faultLevel}" />
	<input type="hidden" id="assignTaskUserId" name="assignTaskUserId" value="${param.assignTaskUserId}" />
	<input type="hidden" id="hasSceneTask" name="hasSceneTask" value="${scene_workOrderInfo['hasSceneTask']}" />
	<input type="hidden" id="hasTechTask" name="hasTechTask" value="${scene_workOrderInfo['hasTechTask']}" />
	<input type="hidden" value="<%=dateFormat.format(new Date())%>" id="nowTime"/>
	<div id="header960">
    	<div class="header-top960"><h2>故障抢修-现场任务</h2></div>
        <div class="list_pic">
            <ul>
                <li>
                    <em class="list_pic_li list_pic_li_on">派发工单</em>
                    <em class="next_ico"></em>
                </li>
                <li>
                    <em class="list_pic_li" id="accept_icon">受理工单</em>
                    <em class="next_ico"></em>
                </li>
                <li>
                    <em class="list_pic_li" id="scene_icon">现场处理任务单</em>
                    <em class="next_ico"></em>
                </li>
                <li>
                    <em class="list_pic_li" id="techsupport_icon">升级技术支援</em>
                    <em class="next_ico"></em>
                </li>
                <li>
                    <em class="list_pic_li" id="reply_icon">工单处理/回复</em>
                </li>
            </ul>
            <div class="point_to" style="background:url(images/point_to.png) -12px 0px no-repeat; height:20px; width:100%;"></div>
        </div>
    </div>
    <%--头部结束--%>
    
    <div id="container960">
    	<%--工单信息 开始--%>
    	<div class="container-tab1">
        	<fieldset id="fieldset-1">
            	<legend>
                	<input type="checkbox" checked/><span>工单信息</span>
                    	<span class="container-tab1-message">
                            <span>工单号：</span>${scene_workOrderInfo['woId']}
                            <span>创建时间：</span>${scene_workOrderInfo['createTime']}
                            <span>状态：</span>${scene_workOrderInfo['statusName']}
                        </span>
                </legend>
                
                <div class="container-main">
                	<%--tab2开始--%>
                    <div class="container-tab2 clearfix">
                        <ul>
                            <li class="tab2-li-show"><a>工单基本信息</a></li>
                            <li><a>客户工单信息</a></li>
                            <li><a>流转过程</a></li>
                            <li><a id="woTraceRecord">服务跟踪记录</a></li>
                            <li><a>网络设施浏览</a></li>
                            
                        </ul>
                    </div>
                    <%--tab2结束--%>
                    
                    <div class="container-main-table1 container-main-table1-tab" id="showWorkOrderInfoDiv">
                    </div>
                   	<div class="container-main-table1 container-main-table1-tab" style="display:none;" id="showCustomerWorkOrderInfoDiv">
                    </div>
                    <%--tab第一部分结束--%>
                    <div id="processDiv" class="container-main-table1 container-main-table1-tab" style="display:none;">
                    	<table class="main-table1">
                        	<tr>
                            	<th width="20%">环节任务列表</th>
                                <th>环节任务详细描述</th>
                            </tr>
                            <tr>
                            	<td class="tl vt" width="20%">
                            		<div id="procedureTree">
                            		</div>
                            	</td>
                            	<td class="tl vt" width="80%">
                            		<div id="taskPocedureDiv">
                            			
                            		</div>
                            	</td>
                            </tr>
                        </table>
                    </div>
                    <%--tab第二部分结束--%>
                    	<div class="container-main-table1 container-main-table1-tab tc" style="display:none;height:202px;" id="woTraceRecordDiv">
                           
                        </div>
                    <%--tab第三部分结束--%>
                    	<div class="container-main-table1 container-main-table1-tab" style="display:none;" id="netresourceDiv">
                        </div>
                    <%--tab第四部分结束--%>
                </div>
            </fieldset>
        </div>
       	<%--工单信息 结束--%>

		<%--任务单信息 开始--%>
    	<div class="container-tab1">
        	<fieldset id="fieldset-2">
            	<legend>
                	<input type="checkbox" checked="checked" /><span>任务单信息</span>
                    	<span class="container-tab1-message">
                            <span>任务单号：</span>${scene_taskOrderInfo['toId']}
                            <span>创建时间：</span>${scene_taskOrderInfo['assignTime']}
                            <span>状态：</span>${scene_taskOrderInfo['statusName']}
                        </span>	
                </legend>
                
                <div class="container-main">
                	<%--tab2开始--%>
                    <div class="container-tab2 clearfix">
                        <ul>
                            <li class="tab2-li-show"><a>任务单派发信息</a></li>
                            <li><a id="taskProcedure">流转过程</a></li>
                            <li><a id="toTraceRecord">服务跟踪记录</a></li>
                        </ul>
                    </div>
                    <%--tab2结束--%>
                    <%--任务单派发信息 开始--%>
                    <div class="container-main-table1 container-main-table1-tab" id="showTaskOrderInfoDiv">
                    </div>
                    <%--任务单派发信息 结束--%>
                    <%--流转过程 开始--%>
                    <div class="container-main-table1 container-main-table1-tab" id="senceProcedureDiv" style="display:none;">
                    </div>
                    <%--流转过程 结束--%>
                    <%--服务跟踪记录 开始--%>
                   	<div class="container-main-table1 container-main-table1-tab tc" id="toTraceRecordDiv" style="display:none;height:202px;" >
                    </div>
                    <%--服务跟踪记录 结束--%>
                    	
                </div>
            </fieldset>
        </div>
		<%--任务单信息 结束--%>
		
		<%--任务单处理 开始--%>
		<div class="container-tab1" id="t3">
        	<fieldset id="fieldset-3">
            	<legend>
                	<input type="checkbox" checked="checked" /><span>任务单处理</span>
                    	
                </legend>
                
                <div class="container-main">
                	<%--tab2开始--%>
                    <div class="container-tab2 clearfix" >
                        <ul id="tab04">
                            <li class="tab2-li-show current acceptField" style="border-right:1px solid #ccc;" ><a>任务受理</a></li>
                            <li class="workField finalReplyField" id="askTask"><a>任务回复</a></li>
                            <li class="workField stepReplyField" ><a>阶段回复</a></li>
                            <li class="workField dispatchField" ><a>资源调度</a></li>
                           	<li class="workField assignTaskField"><a>派发任务</a></li>
                           	<li class="workField toSendField"><a>转派</a></li>
                        </ul>
                    </div>
                    <%--tab2结束--%>
                    <%--tab 第一部分开始--%>
                    <form name="f1" action="acceptUrgentRepairSenceTaskOrderAction" method="post" id="acceptForm" class="acceptDiv" enctype="multipart/form-data">
                    <input type="hidden" name="WOID" value="${scene_workOrderInfo['woId']}"/>
                    <input type="hidden" name="TOID" value="${scene_taskOrderInfo['toId']}"/>
                    <input type="hidden" name="workmanageTaskorder.toId" value="${scene_taskOrderInfo['toId']}"/>
                    <input type="hidden" name="workmanageTaskorder.woId" value="${scene_workOrderInfo['woId']}"/>
                    <div class="container-main-table1 container-main-table1-tab" >
                    	<table class="main-table1 ">
                        	<tr class="main-table1-tr"><td colspan="4" class="main-table1-title">受理/驳回</td></tr>
                            <tr>
                            	<td class="menuTd">操作类型：</td>
                                <td colspan="3"><input type="radio" name="acceptOrReject" checked="checked" value="accept"/>受理 <input type="radio" name="acceptOrReject" value="reject"/>驳回 </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">处理人：</td>
                                <td>${scene_taskOrderInfo['currentHandlerName']}</td>
                            </tr>
                            <tr>
                            	<td class="menuTd">处理时间：</td>
                                <td><%=dateFormat.format(new Date())%></td>
                            </tr>
                            <tr>
                            	<td  class="menuTd higherLine">处理意见：</td>
                                <td>
                                	<textarea id="handleCommentText" name="workmanageTaskorder.acceptComment" rows="4" style="width:90%" class="{required:true,maxlength:100}">已受理</textarea>
                                </td>
                            </tr>
                            <td colspan="2" style="background:#FFF; border:none; text-align:center;">
                                	<input class="buttonControl" type="submit" value="提交" name="f1s" />
                                </td>
                        </table>
                        
                    </div>
                    </form>
                    <%--tab第一部分结束--%>
                     <%--tab第二部分开始--%>
                    <%--container-main-table1开始--%>
                    <div class="container-main-table1 container-main-table1-tab finalReplyDiv" style="display:none;" id="replyDiv">
                    	<%-- 展现最终回复内容开始 --%>
                    	<div id="showReply" style="display:none">
							<table class="main-table1 half-width">
								<tr>
									<td class="menuTd">
										回复人：
									</td>
									<td >
										<div>
											${scene_taskOrderInfo['currentHandlerName']}
										</div>
									</td>
									<td class="menuTd">
										回复时间：
									</td>
									<td >
										<div>
											${scene_taskOrderInfo['finalCompleteTime']}
										</div>
									</td>
								</tr>
								<tr>
									<td class="menuTd">
										故障大类：
									</td>
									<td>${scene_taskOrderInfo['faultGenera']}
									</td>
									<td class="menuTd">
										故障原因：
									</td>
									<td >${scene_taskOrderInfo['faultReason']}
									</td>
								</tr>
								
								<tr>
									<td class="menuTd">
										是否影响业务：</td>
									<td width="38%">
									<s:if test="%{scene_taskOrderInfo.sideeffectService==1}">
									是
                                           </s:if>
			                          <s:elseif test="%{scene_taskOrderInfo.sideeffectService==0}">否
                                           </s:elseif>
									</td>
									<td class="menuTd">受影响业务:</td>
									<td>
									<s:if test="%{scene_taskOrderInfo.sideeffectService==1}">
									${scene_taskOrderInfo['affectedServiceName']}
                                           </s:if>
			                          <s:elseif test="%{scene_taskOrderInfo.sideeffectService==0}">
                                           </s:elseif>
									</td>

								</tr>
								<tr>
									<td class="menuTd">
										故障处理结果：
									</td>
									<td >
									<s:if test="%{scene_taskOrderInfo.faultHandleResult==1}">
										已解决
                                           </s:if>
                                           <s:elseif test="%{scene_taskOrderInfo.faultHandleResult==0}">延期解决
                                           </s:elseif>
									</td>
									 <s:if test="%{scene_taskOrderInfo.faultHandleResult==1}">
										<td class="menuTd bigLine">
											<em >故障解决时间：</em>
										</td>
										<td>${scene_taskOrderInfo['faultSolveTime']}
											
										</td>
					                 </s:if>
					                 <s:elseif test="%{scene_taskOrderInfo.faultHandleResult==0}">
					                 	<td class="menuTd bigLine" >
											<em >预计解决时间：</em>
										</td>
										<td>${scene_taskOrderInfo['foreseeSolveTime']}
											
										</td>
					                  </s:elseif>
								</tr>
								<tr>
									<s:if test="%{scene_taskOrderInfo.faultHandleResult==1}">
										<td class="menuTd bigLine">
											<em >故障处理措施：</em>
										</td>
										<td  colspan="3">${scene_taskOrderInfo['faultHandleDetail']}
											
										</td>
                                    </s:if>
                                    <s:elseif test="%{scene_taskOrderInfo.faultHandleResult==0}">
                                    	<td class="menuTd bigLine">
												<em >延迟解决原因：</em>
										</td>
										<td  colspan="3">
											${scene_taskOrderInfo['reasonForDelayApply']}
										</td>
                                    </s:elseif>
									
								</tr>
								<tr class="show-alarm-tr">
									<td class="menuTd">
										告警是否清除：
									</td>
									<td>
									<s:if test="%{scene_taskOrderInfo.isAlarmClear==1}">
									是
                                           </s:if>
			                          <s:elseif test="%{scene_taskOrderInfo.isAlarmClear==0}">否
                                           </s:elseif>
									</td>
										<td class="menuTd">告警清除时间</td>
										<td>${scene_taskOrderInfo['alarmClearTime']}
									</td>
								</tr>
							</table>
						</div>
                    	<%-- 展现最终回复内容结束 --%>
                    	<div class="level2_tab" >                      
                        <%--最终回复开始--%>
                       	<div id="handleReply">
                        	<form  action="finalReplyUrgentRepairSenceTaskOrderAction" method="post" id="replyForm" enctype="multipart/form-data">
                        	<input type="hidden" name="customerWoType" value="${scene_workOrderInfo['customerWoType']}" />
                        	<input type="hidden" name="WOID" value="${scene_workOrderInfo['woId']}"/>
                        	<input type="hidden" name="TOID" value="${scene_taskOrderInfo['toId']}"/>
                        	<input type="hidden" name="workmanageTaskorder.toId" value="${scene_taskOrderInfo['toId']}"/>
                        	<input type="hidden" name="workmanageTaskorder.woId" value="${scene_workOrderInfo['woId']}"/>
                        	<input type="hidden" name="urgentrepairSencetaskorder.toId" value="${scene_taskOrderInfo['toId']}"/>
                        	<input type="hidden" value="${scene_workOrderInfo['faultHandleDetail'] }" id="handle-detail"/>
							<input type="hidden" value="${scene_workOrderInfo['reasonForDelayApply'] }" id="handle-delay"/>
                        	<table class="main-table1 half-width" >
                                  <tr>
									<td class="menuTd">故障大类：</td>
									<td >
	                                	<select id="reply_faultType" name="urgentrepairSencetaskorder.faultGenera" validateIsEmpty="#reply_faultType">
	                                    </select><span class="red">*</span>
	                                </td>
	                                <td class="menuTd">故障原因：</td>
	                                <td>
	                                	<select id="reply_faultResult" name="urgentrepairSencetaskorder.faultReason" validateIsEmpty="#reply_faultResult">                     
	                                    	<option value="">请选择</option> 
	                                    </select><span class="red">*</span>
	                                </td>
								</tr>
                                
                                <tr class="affected-tr">
                                    <td class="menuTd">是否影响业务：</td>
                                    <td style="width:200px;">
                                       <input type="radio" id="sideeffectY" name="urgentrepairSencetaskorder.sideeffectService" value="1" onclick="affectBiz()" checked="checked" />是 
                                       <input type="radio" id="sideeffectN" name="urgentrepairSencetaskorder.sideeffectService" value="0" onclick="affectBiz()" />否
                                    </td>
                                    <td class="menuTd">受影响业务：</td>
                                    <td>
                                    	<span id="affectedServiceName" style="float: left">
                                        <input type="text" name="urgentrepairSencetaskorder.affectedServiceName"/>
                                        </span>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td class="menuTd">故障处理结果：</td>
                                    <td >
                                        <input type="radio"
											id="done"
											name="urgentrepairSencetaskorder.faultHandleResult"
											onclick="delayApply()" checked="checked" value="1"/>解决 <input type="radio"
											id="reasonForDelayApply" 
											name="urgentrepairSencetaskorder.faultHandleResult"
											onclick="delayApply()" value="0"/>延期解决 
                                    </td>
                                    <td class="menuTd"><span id="expectSolveTime" style=" font-weight:normal;display:none">预计解决时间：</span></td>
                                    <td colspan="3">
                                        <span id="expectSolveTime2" style="float: left;display:none"> 
										<input type="text" id="foreseeSolveTime" name="urgentrepairSencetaskorder.foreseeSolveTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d'})" readonly class="Wdate required input-text"/>
										</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="menuTd higherLine"><em id="handle-title">故障处理措施：</em></td>
                                    <td colspan="3" id="handle-contant">																
                                        <textarea class=" required input-text {maxlength:1000}" id="faultHandle" name="urgentrepairSencetaskorder.faultHandleDetail" rows="4" style="width:90%" ></textarea><span class="red">*</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="menuTd"><span id="expectSolveTime" style="display: none; font-weight:normal">延期申请原因：</span></td>
                                    <td colspan="3">
                                        <span id="expectSolveTime2"
											style="display: none;"> <textarea
											id="reasonForDelay" cols="70" rows="5" class="{maxlength:1000}"
											name="urgentrepairSencetaskorder.resonForDelayApply"  style="width:90%"></textarea> 
										</span>
                                    </td>
                                </tr>
                                
                                 <tr class="alarm-tr">
                                    <td class="menuTd">告警是否清除：</td>
                                    <td style="width:200px;">
                                        <input type="radio" id="isAlarmClearY" name="urgentrepairSencetaskorder.isAlarmClear" onclick="isClear()" value="1" checked="checked"/>是
										<input type="radio" id="isAlarmClearN" name="urgentrepairSencetaskorder.isAlarmClear" onclick="isClear()" value="0"/> 否
                                    </td>
                                    <td class="menuTd">告警清除时间：</td>
                                    <td>
                                    	<span id="clearTime" style="float: left">
                                        <input type="text" id="alarmClearTime" name="urgentrepairSencetaskorder.alarmClearTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" readonly class="Wdate required input-text" value="<%=dateFormat.format(new Date())%>" />
                                        </span>
                                    </td>
                                </tr>
                            </table>
                            <div class="container-bottom">
                            <input class="buttonControl" type="submit" value="提交" style="width:60px;" name="f3s"/>
                        	</div>
                        	 </form>
                        </div>
                        <%--最终回复结束--%>
                        
                       
                        </div>
                        </div>
                    </div>
                    <%--container-main-table1结束--%>
                    <%--tab第二部分结束--%>
                    <%--阶段回复开始--%>
                    	<div id="unHandeled">
                    	<form id="stepReplyForm" action="stepReplyUrgentRepairSenceTaskOrderAction" method="post" target="_blank" enctype="multipart/form-data">
                       	<input type="hidden" name="customerWoType" value="${scene_workOrderInfo['customerWoType']}" />
                       	<input type="hidden" name="WOID" value="${scene_workOrderInfo['woId']}" />
                       	<input type="hidden" name="TOID" value="${scene_taskOrderInfo['toId']}" />
                       <div class="container-main-table1 container-main-table1-tab" style="display:none;">
                        <div class="level2_tab" id="periodReplyDiv" >
                            <table id="periodReply" class="main-table1">
                               <tr class="extra_tr">
	                               <td class="menuTd">处理进度：</td>
	                               <td colspan="3">
	                               	<select id="stepReply_processingProgress" name="stepReply_processingProgress" class="valid required">                     
	                               		<option value="">请选择</option>
	                               		<option value="申请备件">申请备件</option>
	                               		<option value="联系业主">联系业主</option>
	                               		<option value="解决物业问题">解决物业问题</option>
	                               		<option value="整改(光缆)">整改(光缆)</option> 
	                               		<option value="整改(电缆)">整改(电缆)</option> 
	                               		<option value="常规抢修">常规抢修</option> 
	                               		<option value="最终回复">最终回复</option> 
	                                   </select><em class="red">*</em>
	                               </td>
	                           </tr>
	            				<tr class="extra_tr">
	                           	<td class="menuTd">故障大类：</td>
	                               <td>
	                               	<select id="stepReply_faultType" name="stepReply_faultType">
	                                   </select><em class="red">*</em>
	                               </td>
	                               <td class="menuTd">故障原因：</td>
	                               <td>
	                               	<select id="stepReply_faultResult" name="stepReply_faultResult">  
	                               	<option value="">请选择</option>                   
	                                   </select><em class="red">*</em>
	                               </td>
	                           </tr>
                                <tr>
	                                <td class="menuTd higherLine vm tc" style="width:100px;">处理意见：</td>
	                                <td colspan="3">
	                                    <textarea name="stepReply_desc" rows="4" style="width:700px" class="{maxlength:100}"></textarea>
	                                </td>
	                            </tr>
                            </table>
                            <div class="container-bottom">
	                           <input  type="submit" value="提交" id="stepReplySubmit"  style="width:60px;"  />
	                       </div>
                    	</div>
                    	</div>
                    	</form>
                        <%--阶段回复结束--%>
                    <%--tab第三部分开始--%>
                    <%--container-main-table1开始--%>
                    <div class="container-main-table1 container-main-table1-tab" style="display:none;">
                    	<div>
                        	<div style="line-height:35px;">
                            	资源调度列表
                                <input id="carApply" class="buttonControl" type="button" value="车辆申请" onclick="showCarApplyPage();" />
                                <input type="button" class="buttonControl" value="设备申请" disabled="disabled"/>
                                <input type="button" class="buttonControl" value="物资申请" disabled="disabled"/>
                            </div>
                             <table class="main-table1 tc" id="applyResource_div">
                                <tr>
                                    <th></th>
                                    <th>申请单主题</th>
                                    <th>申请单类型</th>
                                    <th>申请资源数量</th>
                                    <th>申请时间</th>
                                    <th>申请单状态</th>
                                </tr>

                            </table>
                           <div>
								<input type="checkbox" id="checkedAll_1"
									onclick="selectAll('applyResourceTable','cboxid')" />
								全选
								<input id="apply5" type="button" id="useResource" value="领用资源" disabled="disabled"/>
								<input id="apply6" type="button" id="useResource" value="释放资源" disabled="disabled"/>
								<input id="apply7" type="button" id="useResource" value="撤消申请" disabled="disabled"/>
									
							</div>
							<div class="paging_div" id="resourcePage">
                                     每页
                                     <input class="input_text" type="text" value="6" style="width:18px;"/>
                                     条记录 | 第
                                     <input class="input_text" type="text" value="1"  style="width:18px;" />
                                     页 
                                     <input type="button" value="go" />
                                     <a href="#" onclick="showListViewByPage('first')">首页</a>&nbsp;
                                     <a href="#" onclick="showListViewByPage('back')">上一页</a>&nbsp;
                                     <a href="#" onclick="showListViewByPage('next')">下一页</a>&nbsp;
                                     <a href="#" onclick="showListViewByPage('last')">尾页</a>&nbsp;
                             </div>
                        </div>
                    </div>
                    <%--container-main-table1结束--%>
                    <%--tab第三部分结束--%>
                    
                     <%--tab第四部分开始--%>
                    <%--container-main-table1开始--%>
                    
                    <%--container-main-table1结束--%>
                    <%--tab第四部分结束--%>
                    
                     <%--tab第五部分开始--%>
                    <%--container-main-table1开始--%>
                     <div class="container-main-table1 container-main-table1-tab" id="" style="display:none;">
                    	<form id="assignForm" method="post" action="createUrgentRepairTechSupportTaskOrderAction" target="stay" enctype="multipart/form-data">
                    		<input type="hidden" name="workmanageTaskorder.woId" value="${scene_workOrderInfo['woId']}"/>
                            <input type="hidden" name="TOID" value="${scene_taskOrderInfo['toId']}"/>
                            <input type="hidden" name="WOID" value="${scene_workOrderInfo['woId']}"/>
	                        <table class="main-table1 tc">
	                            <tr id="renwuguanli">
	                                <th class="tl" colspan="6">下级任务管理
	                                    <span class="button_tabs" style="position:absolute;right:4px; margin-top:1px;">
	                                        <input type="button"  id="cb" value="催办"  /> 
			                                <input type="button"  id="cx" value="撤销"  /> 
			                                <input type="button"  id="cp" value="重派"  /> 
	                                    </span>
	                                </th>
	                            </tr>
                            </table>
                            <table class="main-table1 tc" id="subtasktable"> 
	                            <tr>
	                                <td><input type="checkbox" />全选</td>
	                                <td>下级任务名</td>
	                                <td>派发时间</td>
	                                <td>处理人</td>
	                                <td>任务处理截止时间</td>
	                                <td>状态</td>
	                            </tr>
	                           
	                        </table>
	                        <div>
	                            <input type="button" value="派发任务 ▼" class="show_handle_content"  />
	                        </div>
	                        <%-- 派发任务开始 --%>
	                        <div class="handle_content paifa_handle_content" style="display:none; border:1px solid #ccc; border-top:none;">
								
								<div class="handle_form personnel_assign">
									<h3 style="background:#eee;">
										<span class="fb">选择处理人</span>
	                                    <a href="#" class="gis_a">GIS呈现</a>
									</h3>
									<div class="personnel_info_table">
	                                    <div style="width:auto; height:260px; overflow-x:hidden; overflow-y:scroll;" >
	                                        <table id="peopleList">
												<tr><th>技术专家</th><th>今天是否值班</th><th>当前工作忙闲情况(任务数)</th></tr>
											</table>
										</div>
	                                    <div class="handle_form handle_opinion">
	                                        <table class="main-table1">
	                                            <tr>
	                                                <th class="tl" colspan="4">派发任务</th>
	                                            </tr>
	                                            <tr>
	                                                <td class="menuTd">任务标题：</td>
	                                                <td colspan="3">
	                                                    <input type="text" style="width:80%" class="required input-text" name="workmanageTaskorder.toTitle" maxlength="220" value="${scene_taskOrderInfo['taskName']}" readonly="readonly" />
						                        		<em class="red">*</em>
	                                                </td>
	                                                
	                                            </tr>
	                                            <tr>
	                                                <td class="menuTd">任务描述：</td>
	                                                <td colspan="3">
	                                                    <textarea class="required w80 vm" name="workmanageTaskorder.assignComment" rows="4" style="width:80%">${scene_taskOrderInfo['assignComment']}</textarea>
	                                                    <em class="red">*</em>
	                                                </td>
	                                            </tr>
	                                            <tr>
	                                               <td class="menuTd">截止处理时间：</td>
	                                               <td colspan="3">
		                                            	<input type="text" id="a_latestAllowedTime" name="workmanageTaskorder.requireCompleteTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text" value="${scene_taskOrderInfo['requireCompleteTime']}" />
		                                              	<em class="red">*</em>
	                                               </td>
	                                            </tr>
	                                        </table>
	                                    </div>
									</div>
								</div>
	                            <div class="container-bottom mt10">
	                                <input type="submit" value="提&nbsp;&nbsp;交" class="buttonControl" />
	                            	<input type="button" value="取&nbsp;&nbsp;消" class="close_handle_content buttonControl" />
	                            </div>
							</div>
						</form>
                        <%-- 派发任务结束 --%>
 						<div class="handle_content button_tabs_content" style="right:100px;" id="hastenDiv">
                        	<form action="hastenUrgentRepairTechSupportTaskOrderAction" id="hastenForm" method="post" enctype="multipart/form-data">
                        	 <input type="hidden" name="WOID" value="${scene_workOrderInfo['woId']}"/>
                        	 <input type="hidden" name="TOID" value="${scene_taskOrderInfo['toId']}"/>
                        	 <input type="hidden" name="workmanageTaskorder.toId" class="subToIds"/>
                        	<span class="tip_top"></span>
                        	<table style="width:100%;">
                            	<tr>
                                    <th colspan="2" class="button_tabs_content_title">任务催办
                                    	<a href="#" class="close_handle_content"></a>
                                    </th>
                                </tr>
                                <tr>
                                	<td class="tc vm" style="width:75px;">
                                        催办原因：
                                    </td>
                                    <td>
                                    	<textarea class="required {maxlength:100}" style="width:90%;margin-top:10px;" name="workmanageTaskorder.urgeWorkComment"></textarea><em class="red vt">*</em>
                                    </td>
                                </tr>
                            </table>
                            <div class="container-bottom mt10">
                                <input type="submit" class="subTaskButton close_handle_content" value="提&nbsp;&nbsp;交" />
                            	<input type="button" value="取&nbsp;&nbsp;消" class="close_handle_content" />
                            </div>
                            </form>
                        </div>
                        
                        <div class="handle_content button_tabs_content" style="right:50px;">
                        	<form action="cancelUrgentRepairTechSupportTaskOrderAction" id="cancelForm" method="post" enctype="multipart/form-data">
                        	 <input type="hidden" name="WOID" value="${scene_workOrderInfo['woId']}"/>
                        	 <input type="hidden" name="TOID" value="${scene_taskOrderInfo['toId']}"/>
                        	 <input type="hidden" name="workmanageTaskorder.toId" class="subToIds"/>
                        	<span class="tip_top"></span>
                        	<table style="width:100%;">
                            	<tr>
                                	<th colspan="2" class="button_tabs_content_title">任务撤销
                                    	<a href="#" class="close_handle_content"></a>
                                    </th>
                                </tr>
                            	
                                <tr>
                                	<td class="tc vm" style="width:75px;">
                                    	撤销原因：
                                    </td>
                                    <td>
                                    	<textarea style="width:90%;margin-top:10px;" name="workmanageTaskorder.cancelComment" class="required {maxlength:100}"></textarea><em class="red">*</em>
                                    </td>
                                </tr>
                            </table>
                            <div class="container-bottom mt10">
                                <input type="submit" class="subTaskButton close_handle_content" value="提&nbsp;&nbsp;交" />
                            	<input type="button" value="取&nbsp;&nbsp;消" class="close_handle_content" />
                            </div>
                            </form>
                        </div>
                 
                  		 <div class="handle_content button_tabs_content" style="right:4px;">
                   			<form action="reAssignUrgentRepairTechSupportTaskOrderAction" id="reAssignForm" method="post" enctype="multipart/form-data">
                        	 <input type="hidden" name="WOID" value="${scene_workOrderInfo['woId']}"/>
                        	 <input type="hidden" name="TOID" value="${scene_taskOrderInfo['toId']}"/>
                        	 <input type="hidden" name="workmanageTaskorder.toId" class="subToIds"/>
                        	<span class="tip_top"></span>
                        	<table style="width:100%;">
                            	<tr>
                                	<th colspan="2" class="button_tabs_content_title">任务重派
                                    	<a href="#" class="close_handle_content"></a>
                                    </th>
                                </tr>
                            	
                                <tr>
                                	<td class="tc vm" style="width:75px;">
                                    	重派原因：
                                    </td>
                                    <td>
                                    	<textarea style="width:90%;margin-top:10px;"  name="workmanageTaskorder.reassignComment" class="required {maxlength:100}"></textarea><em class="red">*</em>
                                    </td>
                                </tr>
                            </table>
                            <div class="container-bottom mt10">
                                <input type="submit" class="subTaskButton close_handle_content" value="提&nbsp;&nbsp;交" />
                            	<input type="button" value="取&nbsp;&nbsp;消" class="close_handle_content" />
                            </div>
                            </form>
                        </div>
                   </div>
                    <%--container-main-table1结束--%>
                    <%--tab第五部分结束--%>   
                </div>
                 <%-- tab转派部分开始 --%>
                     <div class="container-main-table1 container-main-table1-tab" style="display:none;">
                     	<form action="toSendUrgentRepairSenceTaskOrderAction" method="post" id="toSendForm" enctype="multipart/form-data">
                   		<input type="hidden" name="TOID" value="${scene_taskOrderInfo['toId']}"/>
                   		<input type="hidden" name="WOID" value="${scene_workOrderInfo['woId']}"/>
                   		<table class="main-table1">
							<tr class="main-table1-tr">
                            	<th class="tl f-bold" colspan="4">转派工单</th>
                            </tr>
                            <tr>
                            	<td class="menuTd">受理专业：</td>
                                <td colspan="3" style="color:blue;">${scene_workOrderInfo['acceptProfessional']}</td>
                                
                            </tr>
                            <tr>
                            	
                                <td class="menuTd">转派用户：</td>
                                <td>
                                	<input type="text" 	id="transpondTeamerName" class="required input-text"/><em class="red">*</em>
                                	<input type="hidden" name="recipient" id="recipient"/>
                                    <input type="button" class="select_peo buttonControl" value="选择用户" />
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">转派原因：</td>
                                <td colspan="3">
                                	<textarea name=stepReply_desc class="required {maxlength:100}" rows="4"  style="width:90%;"></textarea><em class="red">*</em>
                                </td>
                            </tr>
						</table>  
                        <div class="container-bottom mt10">
                            <input type="submit" id="trans_button" class="buttonControl" value="转派" style="width:80px;" />
                        </div>   
                        </form>              
                   </div>
                    <%-- tab转派部分结束 --%>
            </fieldset>
        </div>
		
	</div>
	<%-- 弹出站址 --%>
    <div id="stationList" style="position:absolute; left:50%; top:0px; z-index:300; background:#FFFFFF; width:770px; margin-left:-385px; border:8px solid #999; border-radius:8px; display:none;">
	</div>
	<%-- 弹出站址 结束 --%>
    <div id="black" style="background:rgba(255,255,255,0.5); z-index:200; display:none; width:100%; height:1600px; position:absolute; left:0px; top:0px;" >
	</div>
	
	<%-- 弹出转派对象框开始 --%>
	<div class="alert_select">
		<div class="alert_select_cover"></div>
	    <div class="alert_select_container">
	    <input type="hidden" id="curOrgId"/>
	        <div class="alert_select_head">
	            <h4>人员选择
	                <span class="fr">
	                    <input type="text" id="search_value" value="按姓名查询" />
	                    <a class="search_button"></a>
	                </span>
	            </h4>
	        </div>
	        <div class="alert_select_main">
	        	<div class="alert_select_main_left" id="toSendTree">
	            	
	            </div>
	            <div class="alert_select_main_right" id="toSendDiv">
	            	<table class="alert_table">
	                	<tr>
	                    	<th>人员姓名</th>
	                    	<th>任务数</th>
	                    </tr>  
	                </table>
	                <div class="alert_table_scroll">
	                	
	                </div>
	            	
	            </div>
	            <div class="alert_select_main_bottom">
	            	<input type="button" class="alert_select_hide buttonControl" id="trans_commit" value="确 定" />
	            	<input type="button" class="alert_select_hide buttonControl" value="取 消" />
	            </div>
	        </div>
	    </div>
	</div>
	<%-- 弹出转派对象框开始 --%>
	<%-- 放置甘特图插件  --%>
	 <div id="ctGantt" style="display: none;  position: absolute; z-index: 250;background:#F9F9F9;border: 1px solid #CCCCCC;"></div>
	 <%-- 甘特图 END --%>
	<div id="black"></div>
	
	</body>
</html>