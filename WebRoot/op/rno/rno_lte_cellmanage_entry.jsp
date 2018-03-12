<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>LTE小区信息管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>

<script type="text/javascript">
	var $ = jQuery.noConflict();
	$(function() {
		//tab选项卡
		tab("div_tab", "li", "onclick");//项目服务范围类别切换

	})
</script>
</head>

<body>
	<div class="div_left_main" style="width: 100%">
		<div class="div_left_content">
			<%--  <div class="div_left_top">小区信息管理</div> --%>
			<div style="padding: 10px">
				<div id="frame" style="border: 1px solid #ddd">
					<div id="div_tab" class="div_tab divtab_menu">
						<ul>
							<li class="selected">LTE小区信息查询</li>
							<li>LTE小区信息导入</li>
						</ul>
					</div>

					<div class="divtab_content">
						<div id="div_tab_0">
							<%@include file="rno_lte_cellmanage_query.jsp"%>
						</div>
						<div id="div_tab_1" style="display:none">
							<%@include file="rno_lte_cellmanage_import.jsp"%>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
