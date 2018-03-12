<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
	<script type="text/javascript" src="../jslib/common.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		var obj = $("#taskOrderInfoTab").val();
		var objects = eval("("+obj+")");
		jsonToForm(objects);
	})
	</script>
	<input type="hidden" id="taskOrderInfoTab" value="${taskOrderInfo}" />
		<table class="main-table1 half-width">				
			<tr>
				<td width="13%" class="menuTd">
						任务名称：
					</td>
					<td colspan="3">
						<div class="toTitle">
						</div>
					</td>
				</tr>
				<tr>
					<td class="menuTd higherLine">
						任务描述：
					</td>
					<td colspan="3">
						<div class="assignComment" style="width:810px;word-wrap:break-word;word-break:break-all;overflow:auto;">
						</div>
					</td>
				</tr>
				<tr>
					<td class="menuTd">
						任务派发人：
					</td>
					<td width="38%">
						<div class="assignerName">
						</div>
					</td>

					<td class="menuTd" width="18%">
						任务派发时间：
					</td>
					<td  width="31%">
						<div class="assignTime">
						</div>
					</td>
				</tr>
				<tr>
					<td class="menuTd">
						任务处理人：
					</td>
					<td >
						<div class="currentHandlerName">
						</div>
					</td>

					<td class="menuTd">
						任务处理截止时间：
					</td>

					<td >
						<div class="requireCompleteTime">
						</div>
					</td>
				</tr>
			</table>				
							
