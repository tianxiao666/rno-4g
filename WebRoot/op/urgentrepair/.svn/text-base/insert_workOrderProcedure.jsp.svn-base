<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js" ></script>
<script type="text/javascript">
	$(document).ready(function() {
		//任务流转过程
		
		$(".main-table1 .hide-show-img").each(function(){
			if($(this).attr("src") == "images/ico_show.gif"){
				$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).show();
			}else{
				$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).hide();
				}
		});
		
		var STATUS = ${workOrderInfo['status']};
		if (STATUS == "2") {
			//待受理
			$("#createWorkOrderTable").show();
			$("#createWorkOrderImg").attr("src", "images/ico_show.gif");
			$("#createWorkOrderImg").parent().parent().parent().find("tr").not($(".main-table1-tr")).show();
		} else if (STATUS == "3" || STATUS == "6") {
			//处理中或已派发
			$("#createWorkOrderTable").show();
			$("#acceptWorkOrderTable").show();
			$("#acceptWorkOrderImg").attr("src", "images/ico_show.gif");
			$("#acceptWorkOrderImg").parent().parent().parent().find("tr").not($(".main-table1-tr")).show();	
		} else if (STATUS == "7") {
			//已结束
			$("#createWorkOrderTable").show();
			$("#acceptWorkOrderTable").show();
			$("#replyWorkOrderTable").show();
			$("#replyWorkOrderImg").attr("src", "images/ico_show.gif");
			$("#replyWorkOrderImg").parent().parent().parent().find("tr").not($(".main-table1-tr")).show();
		}
		
	});

</script>
		<input type="hidden" id="WOID" value="${workOrderInfo['woId']}" />
		<input type="hidden" id="status" value="${workOrderInfo['status']}" />
		<div id="container">
		<div class="toggle-main" id="acceptWorkOrderTable" style="display:none;">
			<table class="main-table1 half-width">
			 <tr class="main-table1-tr">
                <td class="main-table1-title orderTable tc" style="white-space: normal;" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" style="float:left;" id="acceptWorkOrderImg"/>
                <span style="display: inline-block;">受理</span>
                	<span style="display: inline-block;text-align: center; width: 60%;"> 处理人：${workOrderInfo['currentHandlerName']} </span>
                	<span style="display: inline-block;"> 处理时间：${workOrderInfo['workOrderAcceptedTime']}</span>
                
                </td>
            </tr>
			<tr>
					<%--如果此行显示文本过多，可以适用class为bigLine的样式，行高会增加，也可自己设定--%>
					<td class="menuTd">
						受理人：
					</td>
					<td>
						<div>
							${workOrderInfo['currentHandlerName']}
						</div>
					</td>
				</tr>
				<tr>
					<%--如果此行显示文本过多，可以适用class为bigLine的样式，行高会增加，也可自己设定--%>
					<td class="menuTd">
						受理意见：
					</td>
					<td>
						<div>
							${workOrderInfo['workOrderAcceptedComment']}
						</div>
					</td>
				</tr>
				<tr>
					<%--如果此行显示文本过多，可以适用class为bigLine的样式，行高会增加，也可自己设定--%>
					<td class="menuTd">
						受理时间：
					</td>
					<td>
						<div>
							${workOrderInfo['workOrderAcceptedTime']}
						</div>
					</td>
				</tr>
			</table>
		</div>
		
		<div class="toggle-main" id="responseWorkOrderTable" style="display:none;">
			<table class="main-table1 half-width" id="phaseResponseTable">
			 <tr class="main-table1-tr">
                <td class="main-table1-title orderTable" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="responseWorkOrderImg"/><span style="float:left;">阶段回复</span>
                </td>
            </tr>
			</table>
		</div>

		<div class="toggle-main" id="replyWorkOrderTable" style="display:none;">
			<table class="main-table1 half-width">
			 <tr class="main-table1-tr">
                <td class="main-table1-title orderTable" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="replyWorkOrderImg"/>
                <span style="display: inline-block;">最终回复</span>
                <span style="display: inline-block;text-align: center; width: 60%;"> 处理人：${workOrderInfo['currentHandlerName']} </span>
                <span style="display: inline-block;"> 处理时间：${workOrderInfo['finalCompleteTime']}</span>
                </td>
            </tr>
            	<tr>
					<td class="menuTd bigLine">
						回复人：
					</td>
					<td >
						<div>
							${workOrderInfo['currentHandlerName']}
						</div>
					</td>
				
					<td class="menuTd bigLine">
						回复时间：
					</td>
					<td >
						<div>
							${workOrderInfo['finalCompleteTime']}
						</div>
					</td>
				</tr>
				<tr>
					<td class="menuTd">
						故障大类：
					</td>
					<td>
						<div>
							${workOrderInfo['faultGenera']}
						</div>
					</td>
					<td class="menuTd">
						故障原因：
					</td>
					<td>
						<div>
							${workOrderInfo['faultCause']}
						</div>
					</td>
				</tr>
				<tr>
					<td class="menuTd">
						是否影响业务：
					</td>
					<td>
						<s:if test="%{workOrderInfo.sideeffectService==1}">是</s:if>
						<s:elseif test="%{workOrderInfo.sideeffectService==0}">否</s:elseif>
					</td>
					<td class="menuTd">
						受影响业务：
					</td>
					<td>
						<s:if test="%{workOrderInfo.sideeffectService==1}">${workOrderInfo['affectedServiceName']}</s:if>
						<s:elseif test="%{workOrderInfo.sideeffectService==0}"></s:elseif>
					</td>
				</tr>
				<tr>
					<td class="menuTd">
						故障处理结果：
					</td>
					<td>
						<s:if test="%{workOrderInfo.faultDealResult==1}">已解决</s:if>
						<s:elseif test="%{workOrderInfo.faultDealResult==0}">延期解决</s:elseif>
					</td>
					<s:if test="%{workOrderInfo.faultDealResult==1}">
						<td class="menuTd bigLine">
							<em >故障消除时间：</em>
						</td>
						<td>
							${workOrderInfo['faultSolveTime']}
						</td>
                	 </s:if>
                 	<s:elseif test="%{workOrderInfo.faultDealResult==0}">
	                 	<td class="menuTd bigLine" >
							<em >预计解决时间：</em>
						</td>
						<td>
							${workOrderInfo['foreseeResolveTime']}
						</td>
                  </s:elseif>
				</tr>
				<tr>
					<s:if test="%{workOrderInfo.faultDealResult==1}">
						<td class="menuTd bigLine">
							<em >故障处理措施：</em>
						</td>
						<td  colspan="3">
							${workOrderInfo['howToDeal']}
						</td>
                	 </s:if>
                 	<s:elseif test="%{workOrderInfo.faultDealResult==0}">
	                 	<td class="menuTd bigLine" >
							<em >延迟解决原因：</em>
						</td>
						<td  colspan="3">
							${workOrderInfo['resonForDelayApply']}
						</td>
                  </s:elseif>
				</tr>
				<tr>
					<td class="menuTd">
						告警是否清除：
					</td>
					<td>
						<s:if test="%{workOrderInfo.isAlarmClear==1}">是</s:if>
						<s:elseif test="%{workOrderInfo.isAlarmClear==0}">否</s:elseif>
					</td>
					<td class="menuTd">
						告警清除时间：
					</td>
					<td>
						<div>
							${workOrderInfo['alarmClearTime']}
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>