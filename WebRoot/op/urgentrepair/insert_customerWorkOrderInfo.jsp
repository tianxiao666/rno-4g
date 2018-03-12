<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
	<script type="text/javascript" src="../jslib/common.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		var obj = $("#customerWorkOrderInfoTab").val();
		var objects = eval("("+obj+")");
		jsonToForm(objects);
	})
	</script>
	<input type="hidden" id="customerWorkOrderInfoTab" value="${customerWorkOrderInfo}" />
	
						<table class="main-table1 half-width">
							<tr>
                            	<td class="menuTd">客户工单编号：</td>
                                <td colspan="3">
                                	<div class="customerWoId">
									</div>
                                </td>
                                
                            </tr>
                            <tr>
                            	<td class="menuTd">客户工单主题：</td>
                                <td colspan="3">
                                	<div class="customerWoTitle">
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">派单人部门：</td>
                                <td style="width:40%">
                                	<div class="customerSenderDepartment">
									</div>
                                </td>
                                <td class="menuTd">派单人：</td>
                                <td>
                                	<div class="customerWoSender">
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">当前处理人部门：</td>
                                <td>
                                	<div class="customerWoCurrentDepartment">
									</div>
                                </td>
                                <td class="menuTd">当前处理人：</td>
                                <td>
                                	<div class="customerWoCurrentHandler">
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">是否紧急故障：</td>
                                <td>
                                	<div class="isEmergencyFault">
									</div>
                                </td>
                                <td class="menuTd">派单方式：</td>
                                <td>
                                	<div class="sendWoWay">
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">是否影响业务：</td>
                                <td>
                                	<div class="sideeffectService">
									</div>
                                </td>
                                <td class="menuTd">影响业务：</td>
                                <td>
                                	<div class="affectedServiceName">
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">告警标准名：</td>
                                <td>
                                	<div class="alarmFormalName">
									</div>
                                </td>
                                <td class="menuTd">告警网管来源：</td>
                                <td>
                                	<div class="alarmNetManageSource">
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">告警逻辑分类：</td>
                                <td>
                                	<div class="alarmLogicalClass">
									</div>
                                </td>
                                <td class="menuTd">告警逻辑子类：</td>
                                <td>
                                	<div class="alarmLogicalSubClass">
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">告警类别：</td>
                                <td>
                                	<div class="alarmClass">
									</div>
                                </td>
                                <td class="menuTd">网元名称：</td>
                                <td>
                                	<div class="netElementName">
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">网管告警级别：</td>
                                <td>
                                	<div class="netmanageAlarmLevel">
									</div>
                                </td>
                                <td class="menuTd">告警关联标识：</td>
                                <td>
                                	<div class="alarmAssociatedId">
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">子告警数量：</td>
                                <td>
                                	<div class="subAlarmNumber">
									</div>
                                </td>
                                <td class="menuTd">子告警信息：</td>
                                <td>
                                	<div class="subAlarmInfo">
									</div>
                                </td>
                            </tr>
						 </table>	
							
