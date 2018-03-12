<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>	
	<script type="text/javascript">
	$(function(){
		//任务流转过程
		
		$(".main-table1 .hide-show-img").each(function(){
			if($(this).attr("src") == "images/ico_show.gif"){
				$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).show();
			}else{
				$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).hide();
				}
		});	
	
		var STATUS = $("#status").val();
		var hasReject = $("#hasReject").val();
		var hasReAssign = $("#hasReAssign").val();
		if (STATUS == "待受理") {
			if(hasReAssign == "yes"){
				$("#reject_wo_div").show();
				$("#reAssign_wo_div").show();
			}
			$("#assign_wo_div").show();
			$("#img1").attr("src", "images/ico_show.gif");
			$("#img1").parent().parent().parent().find("tr").not(
				$(".main-table1-tr")).show();
	
		} else if(STATUS == "待撤销"){
			$("#assign_wo_div").show();
			$("#reject_wo_div").show();
			$("#img5").attr("src", "images/ico_show.gif");
			$("#img5").parent().parent().parent().find("tr").not(
				$(".main-table1-tr")).show();
		} else if(STATUS == "已撤销"){
			$("#assign_wo_div").show();
			$("#reject_wo_div").show();
			$("#cancel_wo_div").show();
			$("#img6").attr("src", "images/ico_show.gif");
			$("#img6").parent().parent().parent().find("tr").not(
				$(".main-table1-tr")).show();
		}else if (STATUS == "处理中" || STATUS == "已派发" || STATUS == "升级中") {
			if(hasReject == "yes"){
				$("#reject_wo_div").show();
			}
			if(hasReAssign == "yes"){
				$("#reAssign_wo_div").show();
			}
			$("#assign_wo_div").show();
			$("#accept_wo_div").show();
			$("#img2").attr("src", "images/ico_show.gif");
			$("#img2").parent().parent().parent().find("tr").not(
					$(".main-table1-tr")).show();
		} else if (STATUS == "已结束" ) {
			if(hasReject == "yes"){
				$("#reject_wo_div").show();
			}
			if(hasReAssign == "yes"){
				$("#reAssign_wo_div").show();
			}
			$("#assign_wo_div").show();
			$("#accept_wo_div").show();
			$("#reply_wo_div").show();
			$("#img4").attr("src", "images/ico_show.gif");
			$("#img4").parent().parent().parent().find("tr").not(
			$(".main-table1-tr")).show();
		}
	});
	</script>
	<input type="hidden" id="TOID" value="${taskOrderInfo['toId']}" />
	<input type="hidden" id="status" value="${taskOrderInfo['statusName']}" />
	<input type="hidden" id="hasReject" value="${taskOrderInfo['hasReject']}" />
	<input type="hidden" id="hasReAssign" value="${taskOrderInfo['hasReAssign']}" />
			 <%-- 派发 开始 --%>
             <div id="assign_wo_div" style="display:none;">
               <table class="main-table1 half-width" >
				 <tr class="main-table1-tr">
	                <td class="main-table1-title" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="img1"/>
	                <span>派发任务单</span>
	                <span style="display: inline-block; width:60%; text-align:center;"> 处理人：${taskOrderInfo['assignerName']} </span>
                	<span style="display: inline-block; position:absolute;right:8px;"> 处理时间：${taskOrderInfo['assignTime']}</span>
	                </td>
	            </tr>
	            <tr>
					<td width="13%" class="menuTd">
							任务名称：
						</td>
						<td colspan="3">
							<div>
								${taskOrderInfo['toTitle']}
							</div>
						</td>
					</tr>
					<tr>
						<td class="menuTd">
							任务描述：
						</td>
						<td colspan="3">
							<div>
								${taskOrderInfo['assignComment']}
							</div>
						</td>
					</tr>
					<tr>
						<td class="menuTd">
							任务派发人：
						</td>
						<td width="38%">
							<div>
								${taskOrderInfo['assignerName']}
							</div>
						</td>
	
						<td class="menuTd" width="18%">
							任务派发时间：
						</td>
						<td  width="31%">
							<div>
								${taskOrderInfo['assignTime']}
							</div>
						</td>
					</tr>
					<tr>
						<td class="menuTd">
							任务处理人：
						</td>
						<td >
							<div>
								${taskOrderInfo['currentHandlerName']}
							</div>
						</td>
	
						<td class="menuTd">
							任务处理截止时间：
						</td>
	
						<td >
							<div>
								${taskOrderInfo['requireCompleteTime']}
							</div>
						</td>
					</tr>
				</table>
			</div>
			 <%-- 派发 结束 --%>
			 <%-- 驳回 开始 --%>
			 <div id="reject_wo_div" style="display:none;">
                <table class="main-table1 half-width" >
				 <tr class="main-table1-tr">
	                <td class="main-table1-title" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="img5"/>
	                <span>驳回</span>
	                <span style="display: inline-block; width:60%; text-align:center;"> 处理人：${taskOrderInfo['currentHandlerName']} </span>
                	<span style="display: inline-block;position:absolute;right:8px;"> 处理时间：${taskOrderInfo['rejectTime']}</span>
	                </td>
	              </tr> 
	              <tr>
					<%--如果此行显示文本过多，可以适用class为bigLine的样式，行高会增加，也可自己设定--%>
					<td class="menuTd">
						驳回人：
					</td>
					<td>
						<div>
							${taskOrderInfo['currentHandlerName']}
						</div>
					</td>
				</tr>
				<tr>
					<td class="menuTd">
						驳回意见：
					</td>
					<td>
						<div>
							${taskOrderInfo['rejectComment']}
						</div>
					</td>
				</tr>
				<tr>
					<td class="menuTd">
						驳回时间：
					</td>
					<td>
						<div>
							${taskOrderInfo['rejectTime']}
						</div>
					</td>
				</tr>
			</table>
          </div> 
			  <%-- 驳回 结束 --%>
			  <%-- 重派 开始 --%>
			 <div id="reAssign_wo_div" style="display:none;">
               <table class="main-table1 half-width" >
				 <tr class="main-table1-tr">
	                <td class="main-table1-title" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="img7"/>
	                <span>重派任务</span>
	                <span style="display: inline-block; width:60%; text-align:center;"> 处理人：${taskOrderInfo['assignerName']} </span>
                	<span style="display: inline-block; position:absolute;right:8px;"> 处理时间：${taskOrderInfo['reassignTime']}</span>
	                </td>
	             <tr>
					<tr>
						<td class="menuTd">
							任务派发人：
						</td>
						<td width="38%">
							<div>
								${taskOrderInfo['assignerName']}
							</div>
						</td>
	
						<td class="menuTd" width="18%">
							任务重派时间：
						</td>
						<td  width="31%">
							<div>
								${taskOrderInfo['reassignTime']}
							</div>
						</td>
					</tr>
					<tr>
						<td class="menuTd">
							任务处理人：
						</td>
						<td colspan="3">
							<div>
								${taskOrderInfo['currentHandlerName']}
							</div>
						</td>
					</tr>
					<tr>
						<td class="menuTd">
							重派意见：
						</td>
						<td colspan="3">
							<div>
								${taskOrderInfo['reassignComment']}
							</div>
						</td>
					</tr>
	           </table>
	          </div> 
			  <%-- 重派 结束 --%>
			  <%-- 受理 开始 --%>
			<div id="accept_wo_div" style="display:none;"> 
			<table class="main-table1" >
				<tr class="main-table1-tr" >
	                <td class="main-table1-title" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="img2"/>
	                <span>受理</span>
	                <span style="display: inline-block; width:60%; text-align:center;"> 处理人：${taskOrderInfo['currentHandlerName']} </span>
                	<span style="display: inline-block;position:absolute;right:8px;"> 处理时间：${taskOrderInfo['acceptTime']}</span>
	                </td>
	            </tr>
				<tr>
					<%--如果此行显示文本过多，可以适用class为bigLine的样式，行高会增加，也可自己设定--%>
					<td class="menuTd">
						受理人：
					</td>
					<td>
						<div>
							${taskOrderInfo['currentHandlerName']}
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
							${taskOrderInfo['acceptComment']}
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
							${taskOrderInfo['acceptTime']}
						</div>
					</td>
				</tr>
			</table>
			</div>
			  <%-- 受理 结束 --%>
			  <%-- 撤销 开始 --%>
			 <div id="cancel_wo_div" style="display:none;">
               <table class="main-table1 half-width" >
				 <tr class="main-table1-tr">
	                <td class="main-table1-title" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="img6"/>
	                <span>撤销</span>
	               	<span style="display: inline-block; width:60%; text-align:center;"> 处理人：${taskOrderInfo['assignerName']} </span>
                	<span style="display: inline-block;position:absolute;right:8px;"> 处理时间：${taskOrderInfo['cancelTime']}</span>
	                </td>
	            </tr>
	            <tr>
						<td class="menuTd">
							撤销人：
						</td>
						<td width="38%">
							<div>
								${taskOrderInfo['assignerName']}
							</div>
						</td>
	
						<td class="menuTd" width="18%">
							撤销时间时间：
						</td>
						<td  width="31%">
							<div>
								${taskOrderInfo['cancelTime']}
							</div>
						</td>
					</tr>
					<tr>
						<td class="menuTd">
							任务处理人：
						</td>
						<td colspan="3">
							<div>
								${taskOrderInfo['currentHandlerName']}
							</div>
						</td>
					</tr>
					<tr>
						<td class="menuTd">
							撤销意见：
						</td>
						<td colspan="3">
							<div>
								${taskOrderInfo['cancelComment']}
							</div>
						</td>
					</tr>
	           </table>
	          </div> 
			  <%-- 撤销 结束 --%>
			  <%-- table4开始 --%>
			  <div id="reply_wo_div" style="display:none;">
			  	<table class="main-table1 half-width"  >
			 <tr class="main-table1-tr" >
                <td class="main-table1-title" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="img4"/>
                <span>最终回复</span>
                <span style="display: inline-block; width:60%; text-align:center;"> 处理人：${taskOrderInfo['currentHandlerName']} </span>
                <span style="display: inline-block;position:absolute;right:8px;"> 处理时间：${taskOrderInfo['finalCompleteTime']}</span>
                </td>
            </tr>
      		<tr>
				<td class="menuTd">
					回复人：
				</td>
				<td >
					<div>
						${taskOrderInfo['currentHandlerName']}
					</div>
				</td>
				<td class="menuTd">
					回复时间：
				</td>
				<td >
					<div>					
						${taskOrderInfo['finalCompleteTime']}
					</div>
				</td>
			</tr>
			<tr>
				<td class="menuTd">
					故障大类：
				</td>
				<td>${taskOrderInfo['faultGenera']}
				</td>
				<td class="menuTd">
					故障原因：
				</td>
				<td >${taskOrderInfo['faultReason']}
				</td>
			</tr>
			
			<tr>
				<td class="menuTd">
					是否影响业务：</td>
				<td width="38%">
				<s:if test="%{taskOrderInfo.sideeffectService==1}">
				是
                </s:if>
                <s:elseif test="%{taskOrderInfo.sideeffectService==0}">否
              	</s:elseif>
				</td>
				<td class="menuTd">受影响业务:</td>
				<td>${taskOrderInfo['affectedServiceName']}
				</td>

			</tr>
			<tr>
				<td class="menuTd">
					故障处理结果：
				</td>
				<td >
				<s:if test="%{taskOrderInfo.faultHandleResult==1}">
					已解决
                </s:if>
                <s:elseif test="%{taskOrderInfo.faultHandleResult==0}">延期解决
                </s:elseif>
                <s:if test="%{taskOrderInfo.faultHandleResult==1}">
					<td class="menuTd bigLine">
						<em >故障解决时间：</em>
					</td>
					<td>${taskOrderInfo['faultSolveTime']}
						
					</td>
                 </s:if>
                 <s:elseif test="%{taskOrderInfo.faultHandleResult==0}">
                 	<td class="menuTd bigLine" >
						<em >预计解决时间：</em>
					</td>
					<td>${taskOrderInfo['foreseeSolveTime']}
						
					</td>
                  </s:elseif>
				</td>
			</tr>
			<tr>
				<s:if test="%{taskOrderInfo.faultHandleResult==1}">
					<td class="menuTd bigLine">
						<em >故障处理措施：</em>
					</td>
					<td colspan="3">
						${taskOrderInfo['faultHandleDetail']}
					</td>
                 </s:if>
                 <s:elseif test="%{taskOrderInfo.faultHandleResult==0}">
                 	<td class="menuTd bigLine" >
						<em >延迟解决原因：</em>
					</td>
					<td  colspan="3">
						${taskOrderInfo['reasonForDelayApply']}
					</td>
                  </s:elseif>
			</tr>
			<tr class="show-alarm-tr">
				<td class="menuTd">
					告警是否清除：
				</td>
				<td>
				<s:if test="%{taskOrderInfo.isAlarmClear==1}">
				是
                                      </s:if>
                        <s:elseif test="%{taskOrderInfo.isAlarmClear==0}">否
                                      </s:elseif>
				</td>
					<td class="menuTd">告警清除时间</td>
					<td>${taskOrderInfo['alarmClearTime']}
				</td>
			</tr>
			</table>
							
							
