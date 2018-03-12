<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
	<script type="text/javascript" src="../jslib/common.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		var obj = $("#workOrderInfoTab").val();
		var objects = eval("("+obj+")");
		jsonToForm(objects);
	})
	</script>
	<input type="hidden" id="workOrderInfoTab" value="${workOrderInfo}" />
						<table class="main-table1 half-width">
							<tr class="main-table1-tr"><td colspan="4" class="main-table1-title"> <img class="hide-show-img" src="images/ico_show.gif" />故障工单信息</td></tr>
							<tr>
                            	<td class="menuTd">工单主题：</td>
                                <td colspan="3">
                                	<div class="requiredInput woTitle">
									</div>
                                </td>
                            </tr>
                             <tr>
                            	<td class="menuTd">告警基站：</td>
                                <td style="width:40%">
                                	<div class="requiredInput faultStationName">
									</div>
                                </td>
                                <td class="menuTd">告警网元名称：</td>
                                <td>
                                	<div class="netElementName"> 
									</div>
                                </td>
                            </tr>
                             <tr>
                            	<td class="menuTd">区域：</td>
                                <td >
                                	<div class="stationOfArea">
									</div>
                                </td>
                                <td class="menuTd">站址名称：</td>
                                <td >
                                	<div class="stationName">
									</div>
                                </td>
                            </tr>
                             <tr>
                            	<td class="menuTd">站址地址：</td>
                                <td colspan="3">
                                	<div class="faultStationAddress">
									</div>
                                </td>
                            </tr>
                             <tr>
                            	<td class="menuTd">基站等级：</td>
                                <td>
                                	<div class="baseStationLevel">
									</div>
                                </td>
                                <td class="menuTd">受理专业：</td>
                                <td>
                                	<div class="acceptProfessional">
								</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">故障类型：</td>
                                <td>
                                	<div class="faultType">
									</div>
                                </td>
                                <td class="menuTd">故障级别：</td>
                                <td>
                                	<div class="faultLevel">
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">故障发生时间：</td>
                                <td>
                                	<div class="requiredInput faultOccuredTime" >
								</div>
                                </td>
                                <td class="menuTd">故障处理时限：</td>
                                <td>
                                	<div class="requiredInput latestAllowedTime">
								</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd higherLine">故障描述：</td>
                                <td colspan="3">
                                <div class="requiredInput faultDescription">
							</div></td>
                                
                            </tr>
						</table>	
							
