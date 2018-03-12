<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>抢修工单</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="css/fault.css" />
<link rel="stylesheet" type="text/css" href="../../css/input.css" />
<link rel="stylesheet" type="text/css" href="../css/selectStation.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/gantt/gantt_small.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
<link type="text/css" rel="stylesheet" href="../../jslib/jquery/css/jquery-ui-1.8.23.custom.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="jslib/public.js"></script>
<script type="text/javascript" src="../jslib/networkResourceViewDivPage.js"></script>
<script type="text/javascript" src="../jslib/baseStationSingleDivPage.js"></script>
<script type="text/javascript" src="jslib/dataOfSelect.js"></script>
<script type="text/javascript" src="jslib/pageSection.js"></script>
<script type="text/javascript" src="jslib/handleWorkOrder.js"></script>
<script type="text/javascript" src="jslib/woPageControl.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="../jslib/pageSection.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="../../jslib/propertyDictionary.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
<script type="text/javascript" src="../../jslib/gantt/gantt.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js"></script>
<script type="text/javascript" src="jslib/pageSection.js"></script>

<style type="text/css">
/* 选择站址样式 */
.selectStation-title{ width:100%-1px; text-align:left; padding-left:10px; border:1px solid #99BBE8; color:#15428B; font-size:13px; font-weight:bold; background:url(../../images/white-top-bottom.gif) repeat-x; line-height:30px;}
.selectStation-main{ width:100%; margin:10px 0; height:300px; overflow:scroll; overflow-x:hidden;}
.selectStation-bottom{ width:100%; text-align:center; margin:5px 0;}
.selectStation-table{ width:100%;}
.selectStation-table tr{ border:1px solid #fff; border-bottom:1px solid #CCCCCC;}
.selectStation-table tr td{padding:2px 5px; border-left:1px solid #fff; width:25%; white-space:nowrap;}
.selectStation-table tr td:hover{ background:#E8EDFF; color:#0033FF; cursor:pointer; border:1px solid #ccc;}
input[type="radio"]{background:none; border:none;}
#processDiv span{font-weight:normal;}

.personnel_info_table input[type="radio"]{left: 0; position: static; top: 0px;}


</style>

</head>

<body>
	<div id="loading_div" style="display:none"><em>数据加载中，请稍后...</em></div>
	<input type="hidden" id="hasPower" value="${com_workOrderInfo['hasPower']}" />
	<input type="hidden" id="ORGID" value="${com_workOrderInfo['ORGID']}" />
	<input type="hidden" id="currentHandler" value="${com_workOrderInfo['currentHandler']}" />
	<input type="hidden" id="_latestAllowedTime" value="${com_workOrderInfo['latestAllowedTime']}" />
	<input type="hidden" id="_faultStationName" value="${com_workOrderInfo['faultStationName']}" />
	<input type="hidden" id="baseStationLevel_value" value="${com_workOrderInfo['baseStationLevel']}" />
	<input type="hidden" id="faultType_value" value="${com_workOrderInfo['faultType']}" />
	<input type="hidden" id="acceptProfessional_value" value="${com_workOrderInfo['acceptProfessional']}" />
	<input type="hidden" id="faultLevel_value" value="${com_workOrderInfo['faultLevel']}" />
	<input type="hidden" id="customerWoType" name="customerWoType" value="${com_workOrderInfo['customerWoType']}" />
	<input type="hidden" id="city" value="${com_workOrderInfo['city']}" />
	<input type="hidden" id="STATUS" value="${com_workOrderInfo['status']}" />
	<input type="hidden" id="WOID" value="${com_workOrderInfo['woId']}" />
	<input type="hidden" id="hasSceneTask" value="${com_workOrderInfo['hasSceneTask']}" />
	<input type="hidden" id="hasTechTask" value="${com_workOrderInfo['hasTechTask']}" />
	<input type="hidden" value="<%=dateFormat.format(new Date())%>" id="nowTime"/>
	<div id="header960">
    	<div class="header-top960"><h2>故障抢修工单</h2></div>
        <div class="header-main">
        	<span class="text-l">工单号：${com_workOrderInfo['woId']}</span>
            <span>创建时间：${com_workOrderInfo['createTime']}</span>
            <span class="text-r">状态：${com_workOrderInfo['statusName']}</span>
       	</div>
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
    		<%--tab1开始--%>
    	<div class="container-tab1">
        	<fieldset id="fieldset-1">
            	<legend>
                	<input type="checkbox" checked="checked" /><span>工单信息</span>
                	<%-- 判断工单是否在待办被点击过 --%>
                    <input id="hasRead" type="hidden" value="${hasRead}" />
                </legend>
                
                <div class="container-main">
                	<%--tab2开始--%>
                    <div class="container-tab2 clearfix">
                        <ul>
                            <li class="tab2-li-show"><a>工单基本信息</a></li>
                            <li><a>客户工单信息</a></li>
                            <li><a id="woProcedure">流转过程</a></li>
                            <li><a id="woTraceRecord">服务跟踪记录</a></li>
                            <li><a>网络设施浏览</a></li>
                        </ul>
                    </div>
                    <%--tab2结束--%>
                    
                    <div class="container-main-table1 container-main-table1-tab">
                    	<form id="updateWorkOrdeForm" action="modifyUrgentRepairWorkOrderAction" method="post" enctype="multipart/form-data">
						<span  class="clearfix" id="buttonSpan" style="display:none;">
							<input type="button" style="float: right;" id="modify_button" value="修改"/><input  type="submit" style="float: right;" id="save_button" value="保存"/><input id="back_button" type="button" style="float: right;" value="返回"/>
						</span>
						<table class="main-table1 half-width" id="showUpdateOrder" style="display:none;">
                        	<tr class="main-table1-tr"><td colspan="4" class="main-table1-title"> <img class="hide-show-img" src="images/ico_show.gif" />故障工单信息</td></tr>
                            <tr>
                            	<td class="menuTd">工单主题：</td>
                                <td colspan="3">
                                <input type="hidden" name="WOID" value="${com_workOrderInfo['woId']}"/>
                                <div>${com_workOrderInfo['woTitle']}</div>	
                            </tr>
                            <tr>
                            	<td class="menuTd">告警基站：</td>
                                <td>
                                	<input class="required input-text" readonly="readonly"  name="urgentrepairWorkorder.faultStationName" type="text" id="selectStationName" value="${com_workOrderInfo['faultStationName']}"/>
									<input type="hidden" name="workorderassnetresource.networkResourceId" id="selectStationId" value="${com_workOrderInfo['networkResourceId']}"/><span class="red">*</span>
									<input type="button" value="选择基站" onclick="openCommonBaseStationSingleDivByAccount()" id="stationSelectButton" />
									<input type="hidden" name="workorderassnetresource.networkResourceType" id="stationType" value="${com_workOrderInfo['networkResourceType']}"/>
                                </td>
                                <td class="menuTd">告警网元名称：</td>
                                <td>
                                	<input type="text" name="urgentrepairWorkorder.netElementName" id="netElementName" value="${com_workOrderInfo['netElementName']}"/>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">区域：</td>
                                <td >
									<input id="stationOfArea" style="width:280px;" type="text" name="urgentrepairWorkorder.faultArea" value="${com_workOrderInfo['faultArea']}" />
									<input type="hidden"  id="areaId" />
									<span class="red">*</span>
									<%-- <div class="select_tree">
										<ul id="tree">
										</ul> --%>
									</div>
                                </td>
                                <td class="menuTd">站址名称：</td>
                                <td>
                                	<input type="text" id="station_Name" name="urgentrepairWorkorder.stationName" value="${com_workOrderInfo['stationName']}" />
                                	<input type="hidden" id="station_id" name="workorderassnetresource.stationId" value="${com_workOrderInfo['stationId']}"/>
                                </td>
                            </tr>
                            
                            <tr>
                            	<td class="menuTd">站址地址：</td>
                                <td colspan="3">
                                	<input id="stationLocation" style="width:90%" type="text" name="urgentrepairWorkorder.faultStationAddress" value="${com_workOrderInfo['faultStationAddress']}" />
                                	<input type="hidden"  name="commonWorkOrder.longitude" id="lng" value="${com_workOrderInfo['longitude']}"/>
									<input type="hidden"  name="commonWorkOrder.latitude"  id="lat" value="${com_workOrderInfo['latitude']}"/>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">基站等级：</td>
                                <td>
                                	<input id="baseStationLevel" name="urgentrepairWorkorder.baseStationLevel" validateIsEmpty="#baseStationLevel" readonly="readonly" value="${com_workOrderInfo['baseStationLevel']}">
                                    </input><span class="red">*</span>
                                </td>
                                <td class="menuTd">受理专业：</td>
                                <td>
                                	<select name="urgentrepairWorkorder.acceptProfessional" id="acceptProfessional" validateIsEmpty="#acceptProfessional"> 
                                	<option value="">请选择</option>
                                    </select><span class="red">*</span>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">故障类型：</td>
                                <td>
                                	<select id="faultType" name="urgentrepairWorkorder.faultType" id="faultType" validateIsEmpty="#faultType">
                                    <option value="">请选择</option>
                                    </select><span class="red">*</span>
                                </td>
                                <td class="menuTd">故障级别：</td>
                                <td>
                                	<select id="faultLevel" name="urgentrepairWorkorder.faultLevel" id="faultLevel" validateIsEmpty="#faultLevel">                     
                                    </select><span class="red">*</span>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">故障发生时间：</td>
                                <td>
                                	<input type="hidden" value="<%=dateFormat.format(new Date())%>" id="nowTime"/>
                                	<input type="text" id="faultOccuredTime" name="urgentrepairWorkorder.faultOccuredTime" onFocus="var dd=$dp.$('latestAllowedTime');WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){latestAllowedTime.focus();},maxDate:'#F{$dp.$D(\'latestAllowedTime\')}'})" readonly class="Wdate required input-text" value="<%=dateFormat.format(new Date())%>"/>
									<span class="red">*</span>
                                </td>
                                <td class="menuTd">截止解决时间：</td>
                                <td>
                               		<input type="text" id="latestAllowedTime" name="urgentrepairWorkorder.latestAllowedTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'faultOccuredTime\')}'})" readonly class="Wdate required input-text"/>
									<span class="red">*</span>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd higherLine">故障描述：</td>
                                <td colspan="3">
                                	<textarea rows="4" style="width:90%" name="urgentrepairWorkorder.faultDescription" class="required input-text" id="faultDescription" >${com_workOrderInfo['faultDescription']}</textarea><span class="red">*</span>
                                </td>
                                
                            </tr>
                        </table>
                        <div  id="showWorkOrderInfoDiv">
                        
                        </div>
                        </form>
                    </div>
                    
                    
                    <div class="container-main-table1 container-main-table1-tab" style="display:none;" id="showCustomerWorkOrderInfoDiv">
                    </div>
                    <%--tab第一部分结束--%>
                    <div class="container-main-table1 container-main-table1-tab" style="display:none;">
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
                    	<div class="container-main-table1 container-main-table1-tab tc" style="display:none;height:202px" id="woTraceRecordDiv">
                            
                        </div>
                    <%--tab第三部分结束--%>
                    	<div class="container-main-table1 container-main-table1-tab" style="display:none;" id="netresourceDiv">
                            <div class="a_tabs" style=" line-height:26px;">
                            <span class="fr">
                            	<input type="button" value="添加关联警告资源" />
                            </span>
                        	</div>
                        </div>
                    <%--tab第四部分结束--%>
                </div>
            </fieldset>
        </div>
        	<%--tab1结束，可重写--%>

		<div class="container-tab1" id="t2">
        	<fieldset id="fieldset-2">
            	<legend>
                	<input type="checkbox" checked="checked" /><span>工单处理</span>
                    	
                </legend>
                
                <div class="container-main">
                	<%--tab2开始--%>
                    <div class="container-tab2 clearfix" >
                        <ul id="tab04">
                            <li class="tab2-li-show current acceptField" style="border-right:1px solid #ccc;"><a>工单受理</a></li>
                            <li class="finalReplyField " ><a>工单回复</a></li>
                            <li class="stepReplyField" ><a>阶段回复</a></li>
                            <li class="assignTaskField" ><a onclick="showChildrenTaskOrder()">派发任务</a></li>
                            <%--  <li class="toSendField" ><a>转派</a></li>--%>
                        </ul>
                    </div>
                    <%--tab2结束--%>
                    <%--tab 第一部分开始--%>
                    <form action="acceptUrgentRepairWorkOrderAction" method="post" class="acceptDiv" enctype="multipart/form-data" id="acceptForm">
                    <div class="container-main-table1 container-main-table1-tab" >
                    <input type="hidden" id="accountForGIS" name="accountForGIS" value="${com_workOrderInfo['accountForGIS']}" />
					<input type="hidden" id="entryType" name="entryType" value="${com_workOrderInfo['entryType']}" />
                    <input type="hidden" name="WOID" value="${com_workOrderInfo['woId']}" />
                    <input type="hidden"  name="urgentrepairWorkorder.customerWoType" value="${com_workOrderInfo['customerWoType']}" />
                    <input type="hidden" name="urgentrepairWorkorder.woId" value="<s:property value='WOID' />" />
                    	<table class="main-table1">
                        	<tr class="main-table1-tr"><td colspan="4" class="main-table1-title">受理/驳回</td></tr>
                            <tr>
                            	<td class="menuTd">操作类型：</td>
                                <td colspan="3"><input type="radio" name="accept-this" checked="checked"/>受理 <input type="radio" name="accept-this" disabled="disabled" />驳回 </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">受理人：</td>
                                <td>${com_workOrderInfo['currentHandlerName']}</td>
                            </tr>
                            <tr>
                            	<td class="menuTd">受理时间：</td>
                                <td><%=dateFormat.format(new Date())%></td>
                            </tr>
                            <tr>
                            	<td  class="menuTd higherLine">受理意见：</td>
                                <td>
                                	<textarea name="urgentrepairWorkorder.workOrderAcceptedComment" rows="4" style="width:90%" class="{maxlength:100}">已受理</textarea>
                                </td>
                            </tr>
                            <td colspan="2" style="background:#FFF; border:none; text-align:center;">
                             	<input class="buttonControl" type="submit" value="提交" style="width:60px;"/>
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
													${com_workOrderInfo['currentHandlerName']}
												</div>
											</td>
											<td class="menuTd">
												回复时间：
											</td>
											<td >
												<div>
													${com_workOrderInfo['finalCompleteTime']}
												</div>
											</td>
										</tr>
										<tr>
											<td class="menuTd">
												故障大类：
											</td>
											<td>${com_workOrderInfo['faultGenera']}
											</td>
											<td class="menuTd">
												故障原因：
											</td>
											<td >${com_workOrderInfo['faultCause']}
											</td>
										</tr>
										
										<tr>
											<td class="menuTd">
												是否影响业务：</td>
											<td width="38%">
											<s:if test="%{com_workOrderInfo.sideeffectService==1}">
											是
                                             </s:if>
					                          <s:elseif test="%{com_workOrderInfo.sideeffectService==0}">否
                                             </s:elseif>
											</td>
											
											<td class="menuTd bigLine">
												受影响业务：
											</td>
											<td>
											<s:if test="%{com_workOrderInfo.sideeffectService==1}">
												${com_workOrderInfo['affectedServiceName']}
                                             </s:if>
					                          <s:elseif test="%{com_workOrderInfo.sideeffectService==0}">
                                             </s:elseif>
											</td>
											
										</tr>
										<tr>
											<td class="menuTd">
												故障处理结果：
											</td>
											<td >
											<s:if test="%{com_workOrderInfo.faultDealResult==1}">
												已解决
                                             </s:if>
                                             <s:elseif test="%{com_workOrderInfo.faultDealResult==0}">延期解决
                                             </s:elseif>
											</td>
											<s:if test="%{com_workOrderInfo.faultDealResult==1}">
												<td class="menuTd bigLine">
													<em >故障消除时间：</em>
												</td>
												<td>
													${com_workOrderInfo['faultSolveTime']}
												</td>
						                	 </s:if>
						                 	<s:elseif test="%{com_workOrderInfo.faultDealResult==0}">
							                 	<td class="menuTd bigLine" >
													<em >预计解决时间：</em>
												</td>
												<td>
													${com_workOrderInfo['foreseeResolveTime']}
												</td>
						                  </s:elseif>
										</tr>
										<tr>
											<s:if test="%{com_workOrderInfo.faultDealResult==1}">
												<td class="menuTd bigLine">
													<em >故障处理措施：</em>
												</td>
												<td  colspan="3">${com_workOrderInfo['howToDeal']}
													
												</td>
                                             </s:if>
                                             <s:elseif test="%{com_workOrderInfo.faultDealResult==0}">
                                             	<td class="menuTd bigLine">
													<em >延迟解决原因：</em>
												</td>
												<td  colspan="3">${com_workOrderInfo['resonForDelayApply']}
													
												</td>
                                             </s:elseif>
											
										</tr>
										<tr class="show-alarm-tr">
											<td class="menuTd">
												告警是否清除：
											</td>
											<td>
											<s:if test="%{com_workOrderInfo.isAlarmClear==1}">
											是
                                             </s:if>
					                          <s:elseif test="%{com_workOrderInfo.isAlarmClear==0}">否
                                             </s:elseif>
											</td>
												<td class="menuTd">告警清除时间</td>
												<td>${com_workOrderInfo['alarmClearTime']}
											</td>
										</tr>
										
									</table>
								</div>
		                    	<%-- 展现最终回复内容结束 --%>
		
		                        <%--最终回复开始--%>
		                        <div class="level2_tab">
		                        <div id="handleReply">
		                        	<form action="finalReplyUrgentRepairWorkOrderAction" method="post" id="replyForm" enctype="multipart/form-data">
									<input type="hidden" name="WOID" value="${com_workOrderInfo['woId']}" />
									<input type="hidden" name="urgentrepairWorkorder.woId" value="${com_workOrderInfo['woId']}" />			
									<input type="hidden" id=reply_sideeffectService value="${scene_taskOrderInfo['sideeffectService']}" />
									<input type="hidden" id=reply_faultDealResult value="${scene_taskOrderInfo['faultHandleResult']}" />
									<input type="hidden" id=reply_isAlarmClear value="${scene_taskOrderInfo['isAlarmClear']}" />
									<input type="hidden" name=customerWoType value="${com_workOrderInfo['customerWoType']}" />			
										<table class="main-table1 half-width"  >
											<tr>
												<td class="menuTd">故障大类：</td>
												<td >
													<input type="hidden" class="faultGenera" value="${scene_taskOrderInfo['faultGenera'] }"/>
				                                	<select id="reply_faultType" name="urgentrepairWorkorder.faultGenera" validateIsEmpty="#reply_faultType">
				                                    </select><span class="red">*</span>
				                                </td>
				                                <td class="menuTd">故障原因：</td>
				                                <td>
				                                	<input type="hidden" class="faultCause" value="${scene_taskOrderInfo['faultReason'] }"/>
				                                	<select id="reply_faultResult" name="urgentrepairWorkorder.faultCause" validateIsEmpty="#reply_faultResult">  
				                                	<option value="">请选择</option>                  
				                                    </select><span class="red">*</span>
				                                </td>
											</tr>
											<tr class="affected-tr">
												<td class="menuTd">
													是否影响业务：
												</td>
												<td style="width: 200px;">
													<input type="radio" id="sideeffectY" onclick="affectBiz()"
														name="urgentrepairWorkorder.sideeffectService" value="1" />
													是
													<input type="radio" id="sideeffectN" onclick="affectBiz()"
														name="urgentrepairWorkorder.sideeffectService" value="0" checked="true"/>
													否
												</td>
												<td class="menuTd">
													受影响业务：
												</td>
												<td>
													<span id="affectedServiceName" style="float: left">
														<input type="text"
															name="urgentrepairWorkorder.affectedServiceName"
															value="${scene_taskOrderInfo['affectedServiceName'] }" />
													</span>
												</td>
											</tr>
											
											<tr>
												<td class="menuTd">
													故障处理结果：
												</td>
												<td>
													<input type="radio" id="done"
														name="urgentrepairWorkorder.faultDealResult"
														onclick="delayApply()" value="1" checked="true"/>
													解决
													<input type="radio" id="reasonForDelayApply"
														name="urgentrepairWorkorder.faultDealResult"
														onclick="delayApply()" value="0"/>
													延期解决
												</td>
												<td class="menuTd">
													<span id="expectSolveTime"
														style="font-weight: normal; display: none">预计解决时间：</span>
												</td>
												<td >
													<span id="expectSolveTime2" style="float: left; display: none"> 
													<input type="text" id="foreseeResolveTime" name="urgentrepairWorkorder.foreseeResolveTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d'})" readonly class="Wdate required input-text" value="${scene_taskOrderInfo['foreseeSolveTime'] }"/>
													</span>
												</td>
											</tr>
											<input type="hidden" value="${scene_taskOrderInfo['faultHandleDetail'] }" id="handle-detail"/>
											<input type="hidden" value="${scene_taskOrderInfo['reasonForDelayApply'] }" id="handle-delay"/>
											<tr>
												<td class="menuTd higherLine" >
													<em id="handle-title">故障处理措施：</em>
												</td>
												<td colspan="3" id="handle-contant">
													<textarea class="required input-text {maxlength:1000}" id="howToDeal"
														name="urgentrepairWorkorder.howToDeal" rows="4"
														style="width: 90%">${scene_taskOrderInfo['faultHandleDetail'] }</textarea>
													<span class="red">*</span>
												</td>
											</tr>
											<tr class="alarm-tr">
												<td class="menuTd">
													告警是否清除：
												</td>
												<td style="width: 200px;">
													<input type="radio" id="isAlarmClearY"
														name="urgentrepairWorkorder.isAlarmClear"
														onclick="isClear()" value="1" checked="true"/>
													是
													<input type="radio" id="isAlarmClearN"
														name="urgentrepairWorkorder.isAlarmClear"
														onclick="isClear()" value="0"/>
													否
												</td>
												<td class="menuTd">
													告警清除时间：
												</td>
												<td>
													<span id="clearTime" style="float: left"> 
													<input type="text" id="alarmClearTime" name="urgentrepairWorkorder.alarmClearTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" readonly class="Wdate required input-text" value="<%=dateFormat.format(new Date())%>" />
													
													</span>
												</td>
											</tr>
										</table>
										<div class="container-bottom">
											<input class="buttonControl" type="submit" value="提交" style="width: 60px;" />
										</div>
									</form>
								</div>
							</div>
							<%--最终回复结束--%>

						</div>
                    <%--container-main-table1结束--%>
                    <%--tab第二部分结束--%>
                    <%--阶段回复开始--%>
                    	<form name="f3" action="stepReplyUrgentRepairWorkOrderAction" method="post" id="stepReplyForm" target="_blank" enctype="multipart/form-data">
								<input type="hidden" name="WOID" value="${com_workOrderInfo['woId']}" />
								<input type="hidden" name="city" value="${com_workOrderInfo['city']}" />
								<input type="hidden" name="customerWoType" value="${com_workOrderInfo['customerWoType']}" />
						  <div class="container-main-table1 container-main-table1-tab" style="display:none;">		
                        <div class="level2_tab">
             				<table id="reply1" class="main-table1">
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
	                                    <textarea name="stepReply_desc" rows="4" style="width:700px" class="{maxlength:1000}"></textarea>
	                                </td>
	                            </tr>
                            </table>
                              <div class="container-bottom">
	                           <input class="buttonControl" type="submit" value="提交" style=" width:60px;"  />
	                       </div>
                        </div>
                        </div>
                        </form>
                        <%--阶段回复结束--%>
                    
                    
                   
                     <%--tab第四部分开始--%>
                    <%--container-main-table1开始--%>
                    
                    <div class="container-main-table1 container-main-table1-tab" id="assignTaskDiv" style="display:none;">
                    	<form id="assignForm" method="post" action="createUrgentRepairSenceTaskOrderAction" target="stay" enctype="multipart/form-data">
                    		<input type="hidden" name="workmanageTaskorder.woId" value="${com_workOrderInfo['woId']}" />
							<input type="hidden" name="WOID" value="${com_workOrderInfo['woId']}" />
	                        <table class="main-table1 tc">
	                            <tr id="renwuguanli">
	                                <th class="tl" colspan="6">下级任务管理
	                                    <span class="button_tabs" style="position:absolute;right:4px; margin-top:1px;">
	                                        <input type="button" class="buttonControl " id="cb" value="催办"  /> 
			                                <input type="button" class="buttonControl " id="cx" value="撤销"  /> 
			                                <input type="button" class="buttonControl " id="cp" value="重派"  /> 
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
	                            <input type="button" value="派发任务 ▼" class="show_handle_content" />
	                        </div>
	                        
	                        <%-- 派发任务开始 --%>
	                        <div class="handle_content paifa_handle_content" style="display:none; border:1px solid #ccc; border-top:none;">
								
								<div class="handle_form personnel_assign">
									<h3 style="background:#eee;">
										<span class="fb">选择处理人</span>
	                                    <a href="#" class="gis_a">GIS呈现</a>
									</h3>
									<div class="personnel_info_table">
										<%-- 生成树 --%>
	                                	<div class="personnel_tree" id="treeDiv">
	                                    	
	                                    </div>
	                                    <div style="width:auto; height:260px; overflow-x:hidden; overflow-y:scroll;" >
	                                        <table id="peopleList" class='tc'>
												<tr><th>维护队</th><th>队长</th><th>今天是否值班</th><th>忙闲情况(任务数)</th></tr>
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
	                                                    <input type="text" style="width:80%" class="required input-text" name="workmanageTaskorder.toTitle" maxlength="220" value="${com_workOrderInfo['taskName']}" />
						                        		<em class="red">*</em>
	                                                </td>
	                                                
	                                            </tr>
	                                            <tr>
	                                                <td class="menuTd">任务描述：</td>
	                                                <td colspan="3">
	                                                    <textarea class="required w80 vm {maxlength:1000}" name="workmanageTaskorder.assignComment" rows="4" style="width:80%">${com_workOrderInfo['faultDescription']}</textarea>
	                                                    <em class="red">*</em>
	                                                </td>
	                                            </tr>
	                                            <tr>
	                                               <td class="menuTd">截止处理时间：</td>
	                                                <td colspan="3">
	                                                <input type="text" id="a_latestAllowedTime" name="workmanageTaskorder.requireCompleteTime" value="${com_workOrderInfo['latestAllowedTime']}" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text"/>
													<span class="red">*</span>
	                                                
	                                                </td> 
	                                            </tr>
	                                        </table>
	                                    </div>
									</div>
								</div>
	                            <div class="container-bottom mt10">
	                                <input type="submit" value="提&nbsp;&nbsp;交" class="buttonControl" />
	                            	<input type="button" value="取&nbsp;&nbsp;消" class="buttonControl close_handle_content" />
	                            </div>
							</div>
						</form>
                        <%-- 派发任务结束 --%>
                        <div class="handle_content button_tabs_content" style="right:100px;" id="hastenDiv">
                        	<form action="hastenUrgentRepairSceneTaskOrderAction" id="hastenForm" method="post" enctype="multipart/form-data">
                        	 <input type="hidden" name="WOID" value="${com_workOrderInfo['woId']}"/>
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
                        	<form action="cancelUrgentRepairSceneTaskOrderAction" id="cancelForm" method="post" enctype="multipart/form-data">
                        	 <input type="hidden" name="WOID" value="${com_workOrderInfo['woId']}"/>
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
                                    	<textarea style="width:90%;margin-top:10px;" name="workmanageTaskorder.cancelComment" class="required {maxlength:1000}"></textarea><em class="red">*</em>
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
                   			<form action="reAssignUrgentRepairSceneTaskOrderAction" id="reAssignForm" method="post" enctype="multipart/form-data">
                        	 <input type="hidden" name="WOID" value="${com_workOrderInfo['woId']}"/>
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
                                    	<textarea style="width:90%;margin-top:10px;"  name="workmanageTaskorder.reassignComment" class="required {maxlength:1000}"></textarea><em class="red">*</em>
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
                    
                    <%--tab第四部分结束--%>
                     <%-- tab转派部分开始 --%>
                    <%--   <div class="container-main-table1 container-main-table1-tab" style="display:none;">
                     	<form action="toSendUrgentRepairWorkOrderAction" method="post" id="toSendForm" enctype="multipart/form-data" >
                   		<input type="hidden" name="WOID" value="${com_workOrderInfo['woId']}" />
                   		<table class="main-table1">
							<tr class="main-table1-tr">
                            	<th class="tl f-bold" colspan="4">转派工单</th>
                            </tr>
                            <tr>
                            	<td class="menuTd">受理专业：</td>
                                <td colspan="3" style="color:blue;" >${com_workOrderInfo['acceptProfessional']}</td>
                            </tr>
                            <tr>
                            	<td class="menuTd">转派专业：</td>
                                <td>
                                	<select name="urgentrepairWorkorder.acceptProfessional">
                                    	<option>网维传输</option>
                                        <option>动力</option>
                                        <option>操控</option>
                                        <option>室内</option>
                                        <option>网优</option>
                                        <option>工程无线</option>
                                        <option>工程传输</option>
                                        <option>基站物业</option>
                                        <option>整改</option>
                                    </select>
                                </td>
                                <td class="menuTd">转派用户：</td>
                                <td>
                                	<input type="text" 	id="transpondTeamerName" class="required"/><em class="red">*</em>
                                	<input type="hidden" name="recipient" id="transpondTeamer"/>
                                    <input type="button" class="select_peo buttonControl" value="选择用户" />
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">转派原因：</td>
                                <td colspan="3">
                                	<textarea name="stepReply_desc" class="required {maxlength:1000}" rows="4"  style="width:90%;"></textarea><em class="red">*</em>
                                </td>
                            </tr>
						</table>  
                        <div class="container-bottom mt10">
                            <input type="submit" id="trans_button " value="转派" style="width:80px;" class="buttonControl"/>
                        </div>   
                        </form>              
                   </div>--%>
                    <%-- tab转派部分结束 --%>
                </div>
            </fieldset>
        </div>
		
	</div>
		<%-- 弹出转派对象框开始 --%>
		<div class="alert_select">
			<div class="alert_select_cover"></div>
		    <div class="alert_select_container">
		        <div class="alert_select_head">
		            <h4>人员选择
		                <span class="fr">
		                    <input type="text" value="按姓名查询" />
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
		            	<input type="button" class="alert_select_hide" id="trans_commit" value="确 定" />
		            	<input type="button" class="alert_select_hide" value="取 消" />
		            </div>
		        </div>
		    </div>
		</div>
		<%-- 弹出转派对象框开始 --%>
	<%-- 弹出站址 --%>
	<div id="stationList" style="position:fixed; left:50%; top:50px; z-index:300; width:850px; margin-left:-420px; border:2px solid #999; border-radius:5px;box-shadow:2px 2px 3px #999;background:#fff; display:none;">
	</div>
	<div id="black" style="background:rgba(255,255,255,0.5); z-index:200; display:none; width:100%; height:1200px; position:absolute; left:0px; top:0px;" >
	</div>
	<%-- 弹出站址 结束 --%>
	<%-- 放置甘特图插件  --%>
	 <div id="ctGantt" style="display: none;  position: absolute; z-index: 250;background:#F9F9F9;border: 1px solid #CCCCCC;"></div>
	 <%-- 甘特图 END --%>
  

</body>

</html>