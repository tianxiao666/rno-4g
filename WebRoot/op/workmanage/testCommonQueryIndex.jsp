<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>测试公共查询页面</title>
		
		<style type="text/css">
			li{margin-bottom:10px;list-style:none;}
		</style>
	</head>

	<body>
	
		<div>
			<ul>
				<li><a href="../ops/op/urgentrepair/createWorkOrder.jsp?woTemplate=4">创建工单</a></li>
				<li><a href="testQueryIndexAction?menuId=1002&isToId=false">待办工单</a></li>
				<li><a href="testQueryIndexAction?menuId=1004&isToId=false">跟踪工单</a></li>
				<li><a href="testQueryIndexAction?menuId=1003&isToId=true">待办任务</a></li>
				<li><a href="testQueryIndexAction?menuId=1006&isToId=true">跟踪任务</a></li>
			</ul>
		</div>
		
		<%-- 如果是任务单 --%>
		<s:if test="isToId==true">
			<table id="totable" cellspacing="0" cellpadding="0" border=1 width=100%>
				<caption>
				</caption>
				<tr>
					<th scope="col" style="text-align: center">序号</th>
					<th scope="col" style="text-align: center">任务单号</th>
					<th scope="col" style="text-align: center">任务名称</th>
					<th scope="col" style="text-align: center">派发人</th>
					<th scope="col" style="text-align: center">任务派发时间</th>
					<th scope="col" style="text-align: center">任务状态</th>
				</tr>
					<s:iterator id="tempMap" value="entityList" status="st">
						<tr align="center">
							<td><s:property value="#st.getCount()" /></td>
							<td><a href="<s:property value='#tempMap.FORMURL' />?TOID=<s:property value='#tempMap.TOID' />&WOID=<s:property value='#tempMap.WOID' />"><s:property value="#tempMap.TOID" /></a></td>
							<td><s:property value="#tempMap.TASKNAME" /></td>
							<td><s:property value="#tempMap.ASSIGNEDPERSONID" /></td>
							<td><s:property value="#tempMap.ASSIGNEDTIME" /></td>
							<td><s:property value="#tempMap.STATUSNAME" /></td>
						</tr>
					</s:iterator>
			</table>
		</s:if>
		<s:else>
			<table id="wotable" cellspacing="0" cellpadding="0" border=1 width=100%>
				<caption></caption>
				<tr>
					<th scope="col" style="text-align: center">序号</th>
					<th scope="col" style="text-align: center">工单号</th>
					<th scope="col" style="text-align: center">工单类型</th>
					<th scope="col" style="text-align: center">工单主题</th>
					<th scope="col" style="text-align: center">创建人</th>
					<th scope="col" style="text-align: center">创建时间</th>
					<th scope="col" style="text-align: center">工单状态</th>
				</tr>
				<s:iterator id="tempMap" value="entityList" status="st">
					<tr align="center">
						<td><s:property value="#st.getCount()" /></td>
						<td><a href="<s:property value='#tempMap.WOEDITURL' />?WOID=<s:property value='#tempMap.WOID' />"><s:property value="#tempMap.WOID" /></a></td>
						<td><s:property value="#tempMap.WOTEMPID" /></td>
						<td><s:property value="#tempMap.WOTITLE" /></td>
						<td><s:property value="#tempMap.CREATEPERSON" /></td>
						<td><s:property value="#tempMap.CREATETIME" /></td>
						<td><s:property value="#tempMap.STATUSNAME" /></td>
					</tr>
				</s:iterator>
			</table>
		</s:else>
		
		
	</body>
</html>
