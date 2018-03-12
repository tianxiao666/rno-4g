<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="../jslib/common.js"></script>
						<table class="main-table1 half-width">
							<tr>
                            	<td class="menuTd">客户工单编号：</td>
                                <td>
                                	<div>
										${customerWorkOrderMapInfo['customerWoId']}
									</div>
                                </td>
                                <td class="menuTd">状态：</td>
                                <td>
                                	<div>
										<em style="color:#0000FF">${customerWorkOrderMapInfo['woStatus']}</em>
									</div>
                                </td>
                            </tr>
                            <tr>
                                <td class="menuTd">受理代维：</td>
                                <td colspan="3">
                                	<div>
										${customerWorkOrderMapInfo['acceptMaintain']}
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">派单时间：</td>
                                <td >
                                	<div>
										${customerWorkOrderMapInfo['assginOrderTime']}
									</div>
                                </td>
                                <td class="menuTd">受理时间：</td>
                                <td >
                                	<div>
										${customerWorkOrderMapInfo['acceptTime']}
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">是否已超时：</td>
                                <td>
                                	<div>
										${customerWorkOrderMapInfo['isOverTime']}
									</div>
                                </td>
                                <td class="menuTd">vip等级：</td>
                                <td>
                                	<div>
										${customerWorkOrderMapInfo['vipLevel']}
									</div>
                                </td>
                            </tr>
                            <tr>
                            	<td class="menuTd">小区名：</td>
                                <td>
                                	<div>
										${customerWorkOrderMapInfo['smallAreaName']}
									</div>
                                </td>
                            	<td class="menuTd">小区号：</td>
                                <td>
                                	<div>
										${customerWorkOrderMapInfo['smallAreaNumber']}
									</div>
                                </td>
                                
                            </tr>
                             <tr>
                                <td class="menuTd">告警类型：</td>
                                <td>
                                	<div>
										${customerWorkOrderMapInfo['alarmType']}
									</div>
                                </td>
                                <td class="menuTd">告警级别：</td>
                                <td>
                                	<div>
										${customerWorkOrderMapInfo['alarmLevel']}
									</div>
                                </td>
                            </tr>
                            
                             <tr>
                            	<td class="menuTd">故障大类：</td>
                                <td>
                                	<div>
										${customerWorkOrderMapInfo['faultType']}
									</div>
                                </td>
                                <td class="menuTd">故障原因：</td>
                                <td>
                                	<div>
										${customerWorkOrderMapInfo['faultCause']}
									</div>
                                </td>
                            </tr>
                             <tr>
                             	 <td class="menuTd">首次告警时间：</td>
                                <td>
                                	<div>
										${customerWorkOrderMapInfo['firstAlarmTime']}
									</div>
                                </td>
                                <td class="menuTd">最新告警时间：</td>
                                <td>
                                	<div>
										${customerWorkOrderMapInfo['latestAlarmTime']}
									</div>
                                </td>
                            </tr>
                            <tr>
                           		<td class="menuTd">恢复时间：</td>
                                <td>
                                	<div>
										${customerWorkOrderMapInfo['recoverTime']}
									</div>
                                </td>
                                <td class="menuTd">关闭时间：</td>
                                <td>
                                	
                                </td>
                            </tr>
                        	 <tr>
                                <td class="menuTd">告警内容：</td>
                                <td colspan="3">
                                	<div>
										${customerWorkOrderMapInfo['alarmContent']}
									</div>
                                </td>
                            </tr>
						</table>	
							
