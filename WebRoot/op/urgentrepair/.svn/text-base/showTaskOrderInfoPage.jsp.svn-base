<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" import="java.util.Date"%>
<%@ page language="java" import="java.text.DateFormat"%>
<%@ page language="java" import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="javax.servlet.http.HttpServletRequest"%>
<%@ page language="java" import="javax.servlet.http.HttpServletResponse"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="auther" content="fq" />
		<title>${taskOrderInfo['toTitle']}</title>
		<link rel="stylesheet" type="text/css" href="../../css/base.css" />
		<link rel="stylesheet" type="text/css" href="../../css/input.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public.css" />
		<link rel="stylesheet" type="text/css" href="../../jslib/gantt/gantt_small.css" />
		<link type="text/css" rel="stylesheet" href="../../jslib/jquery/css/jquery-ui-1.8.23.custom.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
		<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
		<script type="text/javascript" src="../../jslib/jquery/jquery.ui.datepicker.js"></script>
		<script type="text/javascript" src="jslib/public.js"></script>
		<script type="text/javascript" src="jslib/pageSection.js"></script>
		<script type="text/javascript">
	$(function(){
		//任务流转过程
		$(".toggle-text").hide();
		$(".toggle-text img").parent().next().hide();
		$(".toggle-text img").each(function() {
			$(this).click(function() {
				if ($(this).attr("src") == "images/ico_hide.gif") {
					$(this).parent().next().first().toggle();
					$(this).attr("src", "images/ico_show.gif");
				} else {
					$(this).parent().next().first().toggle();
					$(this).attr("src", "images/ico_hide.gif");
				}
			})
		});
		
		$(".main-table1 .hide-show-img").each(function(){
			if($(this).attr("src") == "images/ico_show.gif"){
				$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).show();
			}else{
				$(this).parent().parent().parent().find("tr").not($(".main-table1-tr")).hide();
				}
		});	
		
		var STATUS = $("#produce_status").val();
		var hasReject = $("#produce_hasReject").val();
		var hasReAssign = $("#produce_hasReAssign").val();
		if (STATUS == "待受理") {
			$("#assign_div").show();
			$("#assign_img").attr("src", "images/ico_show.gif");
			$("#assign_img").parent().parent().parent().find("tr").not(
				$(".main-table1-tr")).show();
	
		} else if(STATUS == "待撤销"){
			$("#assign_div").show();
			$("#reject_div").show();
			$("#reject_img").attr("src", "images/ico_show.gif");
			$("#reject_img").parent().parent().parent().find("tr").not(
				$(".main-table1-tr")).show();
		} else if(STATUS == "已撤销"){
			$("#assign_div").show();
			$("#reject_div").show();
			$("#cancel_div").show();
			$("#cancel_img").attr("src", "images/ico_show.gif");
			$("#cancel_img").parent().parent().parent().find("tr").not(
				$(".main-table1-tr")).show();
		}else if (STATUS == "处理中" || STATUS == "已派发" || STATUS == "升级中") {
			if(hasReject == "yes"){
				$("#reject_div").show();
			}
			if(hasReAssign == "yes"){
				$("#reAssign_div").show();
			}
			$("#assign_div").show();
			$("#accept_div").show();
			$("#accept_img").attr("src", "images/ico_show.gif");
			$("#accept_img").parent().parent().parent().find("tr").not(
					$(".main-table1-tr")).show();
		} else if (STATUS == "已结束" ) {
			if(hasReject == "yes"){
				$("#reject_div").show();
			}
			if(hasReAssign == "yes"){
				$("#reAssign_div").show();
			}
			$("#assign_div").show();
			$("#accept_div").show();
			$("#reply_div").show();
			$("#reply_img").attr("src", "images/ico_show.gif");
			$("#reply_img").parent().parent().parent().find("tr").not(
			$(".main-table1-tr")).show();
		}
	});
	</script>
	</head>

	<body>
		
		<div id="header960">
    	<div class="header-top960"><h2>${taskOrderInfo['toTitle']}</h2></div>
        <div class="header-main">
        	<span class="fl">任务单号:${taskOrderInfo['toId']}</span>
            <span>创建时间: ${taskOrderInfo['assignTime']}</span>
            <span class="fr">状态：${taskOrderInfo['statusName']}</span>
       	</div>
    	</div>
		
		<div id="container960">
			<div class="container-main" style="padding:0px 7px;">
				<input type="hidden" id="TOID" value="${taskOrderInfo['toId']}" />
				<input type="hidden" id="status" value="${taskOrderInfo['status']}" />
				<input type="hidden" id="produce_status" value="${taskOrderInfo['statusName']}" />
				<input type="hidden" id="produce_hasReject" value="${taskOrderInfo['hasReject']}" />
				<input type="hidden" id="produce_hasReAssign" value="${taskOrderInfo['hasReAssign']}" />
				 <%-- 派发 开始 --%>
	             <div id="assign_div" style="display:none;">
	               <table class="main-table1 half-width" >
					 <tr class="main-table1-tr">
		                <td class="main-table1-title" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="assign_img"/><span>派发任务单</span>
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
								<div style="width:810px;word-wrap:break-word;word-break:break-all;overflow:auto;">
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
						<tr>
							<td class="menuTd">
								抄送人：
							</td>
							<td colspan="3">
								<div>
								</div>
							</td>
						</tr>
					</table>
				</div>
				 <%-- 派发 结束 --%>
				 <%-- 驳回 开始 --%>
				 <div id="reject_div" style="display:none;">
	                <table class="main-table1 half-width" >
					 <tr class="main-table1-tr">
		                <td class="main-table1-title" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="reject_img"/><span>驳回</span>
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
				 <div id="reAssign_div" style="display:none;">
	               <table class="main-table1 half-width" >
					 <tr class="main-table1-tr">
		                <td class="main-table1-title" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="img7"/><span>重派任务</span>
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
				<div id="accept_div" style="display:none;"> 
				<table class="main-table1" >
					<tr class="main-table1-tr" >
		                <td class="main-table1-title" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="accept_img"/><span>受理</span>
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
				 <div id="cancel_div" style="display:none;">
	               <table class="main-table1 half-width" >
					 <tr class="main-table1-tr">
		                <td class="main-table1-title" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="cancel_img"/><span>撤销</span>
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
				  <div id="reply_div" style="display:none;">
				  	<table class="main-table1 half-width"  >
				 <tr class="main-table1-tr" >
	                <td class="main-table1-title" colspan="4"><img class="hide-show-img" src="images/ico_hide.gif" id="reply_img"/><span>最终回复</span>
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
					<td class="menuTd">受影响业务：</td>
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
			</div>
		</div>
	</body>

</html>
