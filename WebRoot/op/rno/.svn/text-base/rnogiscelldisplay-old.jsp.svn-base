<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>

		<title>小区GIS呈现</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<link rel="stylesheet" type="text/css" href="../../css/base.css">
		<link rel="stylesheet" type="text/css" href="../../css/input.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public.css">
		<link rel="stylesheet" type="text/css"
			href="../../css/public-table.css" />
		<link rel="stylesheet" type="text/css"
			href="../../css/public-div-standard.css">
		<link rel="stylesheet" type="text/css"
			href="../../css/public-table-standard.css" />
		<link rel="stylesheet" type="text/css"
			href="../../jslib/paging/iscreate-paging.css" />

		<link rel="stylesheet" type="text/css" href="css/source.css" />

		<link rel="stylesheet" type="text/css" href="css/loading_cover.css" />
		<script type="text/javascript"
			src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="../js/tab.js"></script>
		<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
		<script type="text/javascript"
			src="http://api.map.baidu.com/api?v=1.5&ak=85E6658a58abb433b32f6505997a0c25"></script>
		<script type="text/javascript" src="js/rnogiscelldisplay.js"></script>
		<style type="text/css">
html,body {
	width: 100%;
	height: 100%;
	margin: 0px;
	background: #EEEEEE;
}

#map_canvas {
	width: 100%;
	height: 100%;
	position: absolute;
	z-index: 0;
}
;
</style>
	</head>


	<body>
		<div class="loading_cover" style="display: none">
			<div class="cover"></div>
			<h4 class="loading">
				正在加载
				<em class="loading_fb">小区</em>资源,请稍侯...
			</h4>
		</div>
		<div>
			<form id="conditionForm" method="post">
				<input type="hidden" id="hiddenZoom" name="zoom" value="14" />
				<input type="hidden" id="hiddenLng"
					value="<s:property value='centerPoint.lng' />" />
				<input type="hidden" id="hiddenLat"
					value="<s:property value='centerPoint.lat' />" />
				<input type="hidden" id="hiddenPageSize" name="page.pageSize"
					value="50" />
				<input type="hidden" id="hiddenCurrentPage" name="page.currentPage"
					value="1" />
				<input type="hidden" id="hiddenTotalPageCnt" />
				<input type="hidden" id="hiddenTotalCnt" />

				<select id="queryCellAreaId" name="areaId">
					<s:iterator value="zoneAreaLists" id="onearea">
						<option value="<s:property value='#onearea.area_id' />">
							<s:property value="#onearea.name" />
						</option>
					</s:iterator>
				</select>
				<input type="button" id="btn" value="zoom" />
		</div>
		</form>
		<div id="map_canvas" style="width: 100%; height: 90%">

		</div>
	</body>
</html>
