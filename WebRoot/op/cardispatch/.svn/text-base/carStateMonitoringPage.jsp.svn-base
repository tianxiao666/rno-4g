<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%
	String carNumber = "";
	String display = "";
	if (request.getParameter("carNumber") != null) {
		carNumber = request.getParameter("carNumber");
	}
	if (carNumber != null && !carNumber.equals(""))
		carNumber = new String(carNumber.getBytes("ISO-8859-1"),
				"UTF-8");

	if (request.getParameter("display") != null) {
		display = request.getParameter("display");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车辆综合查询</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<link rel="stylesheet" type="text/css"
	href="../../jslib/jquery/css/jquery-ui-1.8.23.custom.css" />
<link type="text/css" rel="stylesheet"
	href="../../jslib/gantt/gantt_small.css" />
<link type="text/css" rel="stylesheet" href="css/alert_div.css" />
<link type="text/css" rel="stylesheet"
	href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="css/car.css" />
<link rel="stylesheet" type="text/css"
	href="css/jquery.autocomplete.css"></link>
<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css"
	type="text/css"></link>
<link rel="stylesheet" href="css/skins/default.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/loading_cover.css" />

<%-- js --%>

<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript"
	src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript"
	src="../../jslib/jquery/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="../../js/public.js"></script>
<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="js/tool/new_formcheck.js"></script>
<script type="text/javascript" src="js/gisCarLocus.js"></script>
<script type="text/javascript" src="js/carStateMonitoringPage.js"></script>
<script type="text/javascript" src="js/tool/tablePage.js"></script>
<script type="text/javascript" src="js/jquery.autocomplete.min.js"></script>
<script type="text/javascript" src="js/jquery.artDialog.min.js"></script>
<script type="text/javascript" src="js/artDialog.plugins.min.js"></script>

<%-- 加载地图模块需要引入的文件 --%>
<script type="text/javascript"
	src="../../jslib/gis/jslib/api/iscreate_map.js"></script>
<script type="text/javascript" src="../../jslib/gantt/gantt.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>

<script type="text/javascript">
	$(function(){
		if("<%=display%>" == "none") {
			$("#present_tab").css("display", "none");
			$(".tab_content_container").css("display", "none");
			$("#autoRefreshTime").css("display","none")
		}
	})
</script>

<style type="text/css">
#treeDiv,#treeDiv2 {
	z-index: 50;
	display: none;
	position: absolute;
	overflow: auto;
	background: #fff;
	border: 1px solid #ccc;
	width: 190px;
	min-height: 186px;
	text-align: left;
	margin-top: 1px;
	top: 23px;
	left: 0;
	line-height: 18px;
}

#treeDiv3 {
	z-index: 50;
	display: none;
	position: absolute;
	overflow: auto;
	background: #fff;
	border: 1px solid #ccc;
	width: 165px;
	min-height: 186px;
	text-align: left;
	margin-top: 1px;
	left: 0;
	line-height: 18px;
	margin-left: 140px;
}

/*mapView*/
#map_canvas {
	width: 100%;
	height: 734px;
}
/*播放器
#replayDiv{background:#fff; border:1px solid #CCCCCC;left: 80px;padding: 3px;position: absolute;top:10px;width: 220px;z-index: 15;display: none; box-shadow:2px 2px 5px #666;display:none;}
#replayCtrl .progress {border-radius: 3px;background: none repeat scroll 0 0 #FFFFFF;border: 1px solid #01aaef;display: inline-block;top: -1px;width: 145px;margin-top:5px;}
#replayCtrl .progress div {background: none repeat scroll 0 0 #7bc2eb;margin-top: 0;padding: 1px 0;}
#replayCtrl .progress div div {border-bottom: 3px solid #5dabd8; border-top: 3px solid #92d1f5;height: 0;overflow: hidden;}
#replayCtrl .progress_time{position: relative; top:0; *+top:-3px; top:-6px\0; padding-left:5px;}
#progress_control{ border:1px solid #ccc; padding:2px;height: 25px;margin-left:4px;margin-top:5px;}
#progress_control img{ margin-left:7px; cursor:pointer; }
*/
#replayDiv {
	background: none repeat scroll 0 0 #DFE8F6;
	border: 1px solid #608dd1;
	border-radius: 5px 5px 5px 5px;
	box-shadow: 2px 2px 5px #666666;
	display: none;
	left: 80px;
	position: absolute;
	top: 10px;
	width: 280px;
	z-index: 15;
}

#replayDiv .replay_title {
	padding: 5px 10px;
	margin-bottom: 10px;
	border-bottom: 1px solid #608dd1;
	border-radius: 5px 5px 0px 0px;
	font-weight: bold;
	background: url("../../images/cmp-bg2.gif") repeat-x scroll 0 0
		transparent;
	cursor: move;
}

#replayCtrl,#progress_control {
	padding: 0 10px 10px;
}

#replayDiv .end_time {
	position: absolute;
	right: 10px;
	top: 36px;
}

#replayCtrl .progress {
	background: none repeat scroll 0 0 #FFFFFF;
	border: 1px solid #01AAEF;
	border-radius: 3px 3px 3px 3px;
	display: inline-block;
	height: 20px;
	margin: 5px 0;
	top: -1px;
	width: 100%;
	cursor: pointer;
}

#replayCtrl .progress div {
	background: none repeat scroll 0 0 #7BC2EB;
	margin-top: 0;
	padding: 2.4px 0;
}

#replayCtrl .progress div div {
	border-bottom: 3px solid #5DABD8;
	border-top: 3px solid #92D1F5;
	height: 0;
	overflow: hidden;
}

#replayCtrl .progress_time {
	padding-left: 5px;
	position: relative;
	top: 0;
}

#progress_control {
	height: 25px;
	text-align: center;
}

#progress_control img {
	margin-left: 7px;
	cursor: pointer;
}

#replayDiv .beishu {
	position: absolute;
	right: 10px;
	margin-top: 5px;
}

/*自动补全*/
#resultDiv ul {
	cursor: default;
	margin: 0px;
	padding: 0px;
	list-style: none;
}

.esultDivLiHover {
	background: #cfcfcf;
}

#resultDiv {
	background: Wheat;
	border-bottom: 1px solid #CCCCCC;
	border-left: 1px solid #CCCCCC;
	border-right: 1px solid #CCCCCC;
	position: absolute;
	top: 90px;
	width: 144px;
	z-index: 100;
	display: none;
}

#moreMenuDiv {
	display: none;
	background: none repeat scroll 0 0 #FFFFFF;
	border: 1px solid #CCCCCC;
	box-shadow: 3px 3px 5px #999999;
	left: 55px;
	padding: 5px 10px;
	position: absolute;
	top: 105px;
	z-index: 100;
}

/*右边详细信息框*/
.gis_right_information {
	height: 100%;
	background: #fff;
	position: absolute;
	top: -1px;
	right: 0px;
	border-left: 1px solid #ccc;
	z-index: 2;
	display: none;
	width: 330px;
}

.gis_content_right_show {
	height: 49px;
	width: 14px;
	position: absolute;
	left: -14px;
	top: 40%;
	cursor: pointer;
}

.gis_right_information_top {
	height: 26px;
	line-height: 26px;
	background-position: 1px 0px;
	text-align: center;
	font-weight: bold;
}

.gis_content_right_show {
	height: 49px;
	width: 14px;
	position: absolute;
	left: -14px;
	top: 40%;
	cursor: pointer;
}

.gis_content_right_main {
	width: 330px;
	padding-top: 0px;
	background: #fff;
}

.top_tab {
	margin: 0 auto 10px;
	padding-left: 5px;
	border-top: 1px solid #99BCE8;
	border-bottom: 1px solid #99BCE8;
	background: none repeat scroll 0 0 #D0E0F4;
	height: 30px;
	line-height: 30px;
	border-top: 1px solid #99BCE8;
	width: 96%;
}

.gis_content_right_main_tab li {
	float: left;
	cursor: pointer;
	border-right: 1px solid #99BCE8;
	height: 28px;
	width: 80px;
	line-height: 26px;
	padding: 2px 3px;
}

.gis_content_right_main_tab .active {
	background-color: #fff;
	border-bottom: 1px solid #FFFFFF;
	margin-bottom: -1px;
}

.top_tab h4 {
	float: left;
	cursor: pointer;
	border: 0px solid #CCCCCC;
	height: 28px;
	width: 47%;
	line-height: 26px;
	padding: 1.5px 3px;
}

.top_tab .active {
	background-color: #fff;
	border: 1px #CCCCCC;
	margin-bottom: -1px;
}

.tab_content_ul li {
	text-align: left;
	padding-left: 5px;
}

.pt1 {
	position: relative;
	top: -1px\0;
	top: 0px\9\0;
}

.pt2 {
	position: relative;
	top: 1px;
}

.search_table .areaButton { *+
	margin: 2px 0 0 -22px;
}

.resource_item {
	border-bottom: 1px dashed #CCCCCC;
	padding: 10px 0 10px 80px;
	cursor: pointer;
	text-align: left;
}

.lineoff {
	background: url('icon-for-map/LineOff.png') 10px 15px no-repeat;
}

.static {
	background: url('icon-for-map/Static.png') 10px 15px no-repeat;
}

.travel {
	background: url('icon-for-map/Travel.png') 10px 15px no-repeat;
}

.gis_content_right_main .resource_item:hover {
	background-color: #cee5fe;
}

.address {
	color: #FF6600;
	font-weight: normal;
}

.title_content {
	background: #F5F5F5;
	height: 50px;
	line-height: 50px;
	margin: 0 auto 10px;
	width: 96%;
	border: 1px solid #CCCCCC;
}

.title_content em {
	font-size: 13px;
}
</style>
</head>

<body>
	<%-- 隐藏域  --%>
	<input type="hidden" value="${carLocusInfoMap.display}"
		id="hiddendisplay" />
	<input type="hidden" value="${carLocusInfoMap.carNumber}"
		id="hiddenCarNumber" />
	<input type="hidden" value="${carLocusInfoMap.beginTime}"
		id="hiddenBeginTime" />
	<input type="hidden" value="${carLocusInfoMap.endTime}"
		id="hiddenEndTime" />
	<input type="hidden" value="${carLocusInfoMap.woId}" id="hiddenWoId" />
	<input type="hidden" value="${param.curFrame}" id="hiddenFrame" />
	<input type="hidden" value="${carLocusInfoMap.curPosition}"
		id="curPosition" />


	<div class="loading_cover" style="display:none">
		<div class="cover"></div>
		<h4 class="loading">正在加载地图与车辆,请稍侯...</h4>
	</div>
	<div id="container">
		<%-- 搜索区tab --%>
		<div class="top_tab_container">

			<div class="tab_content_container">
				<div class="search_tab clearfix">
					<ul>
						<li class="ontab">普通查询</li>
						<li>值班查询</li>
					</ul>
				</div>
				<div class="search_tab_content" style="display:block;">
					<table class="search_table">
						<tr>
							<td><input type="hidden" id="bizunitIdText" /> <input
								type="text" id="bizunitNameText" placehoder="请选择组织"
								readonly="readonly" class="pt1" /><a class="areaButton"
								title="选择组织" href="javascript:void(0);" id="orgSelectButton"></a>
								<div id="treeDiv">
									<%-- 放置组织架构树  --%>
								</div> <select id="carType">
									<option value="">请选择车辆类型</option>
									<option value="面包车">面包车</option>
									<option vlaue="小轿车">小轿车</option>
									<option value="小货车">小货车</option>
							</select> <select id="carState">
									<option value="">请选择车辆状态</option>
									<option value="0">离线</option>
									<option value="2">静止</option>
									<option value="1">行驶中</option>
									<option value="3">待初始化</option>
							</select> <input type="text" id="carNumber" class="pt2" /> <input
								type="button" style="width:50px;" value="查询"
								id="simpleQueryButton" /></td>
						</tr>
					</table>
				</div>
				<div class="search_tab_content">
					<table class="search_table">
						<tr>
							<td>
								<div style="position:relative;">
									<input type="hidden" id="carBizId" /> <input type="text"
										id="carBizName" placehoder="请选择组织" readonly="readonly" /><a
										id="orgSelectButton2" class="areaButton" title="选择组织"
										href="javascript:void(0);"></a>
									<div id="treeDiv2">
										<%-- 放置组织架构树  --%>
									</div>
									<input type="text" name="_dutyDate" id="dutyDate"
										readonly="readonly" /><a href="javascript:void(0)"
										class="dateButton"
										onclick="fPopCalendar(event,document.getElementById('dutyDate'),document.getElementById('dutyDate'),false)"></a>
									<input type="checkbox" name="_freId" class="shiftType"
										checked="checked" value="1" /> 白班 <input type="checkbox"
										name="_freId" class="shiftType" value="2" /> 晚班 <input
										type="button" style="width:50px;" value="查询"
										id="multiSearchButton" />
								</div></td>
						</tr>
					</table>
				</div>
			</div>
		</div>

		<%-- 展示区tab --%>
		<div class="main_tab_container">

			<ul class="present_tab clearfix" id="present_tab">
				<li id="listViewButton"><a href="javascript:void(0);"
					class="list select"><em></em>列表呈现</a></li>
				<li id="mapViewButton"><a href="javascript:void(0);"
					class="gis" style="border-left:none;"><em></em>GIS呈现</a></li>
			</ul>

			<%-- 工具 --%>
			<div class="main_tab_tool">
				<input type="checkbox" id="autoRefreshButton" checked="checked" />自动刷新
				<select id="autoRefreshTime">
					<option value="10000">10秒</option>
					<option value="20000">20秒</option>
					<option value="30000">30秒</option>
					<option value="60000">1分钟</option>
					<option value="300000" selected="selected">5分钟</option>
				</select>
			</div>

			<%-- 搜索结果、地图展示区 --%>
			<div class="search_result">
				<div id="listView">
					<div class="present_tab_content" id="listView1">
						<table class="search_result_table" id="resultListTable">
							<thead>
								<tr>
									<th style="width:55px">车辆牌照</th>
									<th style="width:55px">任务总数</th>
									<th style="width:152px">车辆归属地</th>
									<th style="width:55px">车辆类型</th>
									<th style="width:55px">司机姓名</th>
									<th style="width:73px">司机电话</th>
									<th style="width:55px">定位状态</th>
									<th style="width:55px">是否排班</th>
									<th style="width:360px">最新位置(时间)</th>
									<th style="width:71px">当天里程统计</th>
									<th style="width:55px">操作</th>
								</tr>
							</thead>
							<tbody>

							</tbody>
						</table>

					</div>
					<%-- 默认每页10条或20条记录 --%>
					<div id="pageContent"></div>
				</div>
				<div class="present_tab_content"
					style="display:none; position:relative;" id="MapView">
					<%-- 放置地图  --%>
					<div id="map_canvas"></div>
					<%-- 车辆轨迹播放控件  --%>
					<div id="replayDiv" style="width: 450px;">
						<div id="replay_title" class="replay_title" style="height: 15px;">
							<%-- <span id="replay_title_text">轨迹回放</span> --%>
							<span style="float: left;">轨迹回放</span><span style="float: right;">回放日期:<span
								id="replayDiv_locusDate"></span> </span>
						</div>
						<div id="replayCtrl">
							<span class="start_time">00:00:00</span> <span
								id="replayIndexDiv" class="end_time">00:00:00</span> <span class="jpp"
								style="padding-left:0px; 
									    cursor: pointer;
									    display: inline-block;
									    padding-top:10px;
									    margin-bottom: -8px;
									    width: 100%;">
								<span id="tcspan" style="color: red; padding-top: -15px; width: 100%; margin-left:-4px;">▼</span>
							</span> <span class="progress jpp" id="progressSpan"
								style="padding-left:0px;">
								<div id="progressDiv" style="width: 0%">
									<div></div>
								</div> </span>
							<div
								style="padding-bottom:10px; width:100%; text-align: center; margin-top: 5px;">
								<span
									style="background: none repeat scroll 0 0 #FFFFFF;background-color:#FAFAFA; border: 1px solid #01AAEF; border-radius: 3px 3px 3px 3px; width:250px; padding-top:5px; padding-bottom:5px; text-align: center; display: inline-table;">
									<span
									style="background: none repeat scroll 0 0 #FFFFFF; background-color:#00A80B; border: 1px solid #01AAEF; border-radius: 3px 3px 3px 3px; width:25px; height: 20px; ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;<span>行驶</span>
									<span
									style="background: none repeat scroll 0 0 #FFFFFF; background-color:#b2c4f7; border: 1px solid #01AAEF; border-radius: 3px 3px 3px 3px; width:25px; height: 20px; ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;<span>静止</span>
									<span
									style="background: none repeat scroll 0 0 #FFFFFF; background-color:#CCCCCC; border: 1px solid #01AAEF; border-radius: 3px 3px 3px 3px; width:25px; height: 20px; ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;<span>离线</span>
								</span>
							</div>
						</div>
						<div id="progress_control">
							<img id="back" src="icon-for-map/last.png" /> <img id="start"
								src="icon-for-map/play.png" /> <img id="pause"
								style="display:none" src="icon-for-map/pause.png" /> <img
								id="forward" src="icon-for-map/next.png" /> <img id="finish"
								src="icon-for-map/stop.png" /> <span class="beishu"
								style="display:none;">倍数：<em id="beishu"></em> </span>
						</div>
					</div>
					<%-- 右边轨迹回放查询区 BEGIN --%>
					<div class="gis_right_information" id="rightInformation">
						<div class="gis_content_right_show">
							<img id="gis_content_right_show_img" src="images/show_right.png">
						</div>
						<div class="gis_right_information_top">
							<div class="top_tab" style="width:96%">
								<ul>
									<h4 class="active">车辆轨迹信息</h4>
									<h4>车辆状态记录</h4>
								</ul>
							</div>
							<div class="gis_content_right_main">
								<table class="main-table1" style="width:96%;">
									<tr>
										<th colspan="2" class="main-table1-title tc">车辆轨迹查询
											</td>
									</tr>

									<tr>
										<td>所属组织：</td>
										<td align="left"><input type="hidden" id="gisBizId" /> <input
											type="text" id="gisBizName" placehoder="请选择组织"
											readonly="readonly" /> <a id="orgSelectButton3"
											class="areaButton" title="选择组织" href="javascript:void(0);"></a>
											<div id="treeDiv3">
												<%-- 放置组织架构树  --%>
											</div></td>
									</tr>

									<tr>
										<td>车辆牌照：</td>
										<td align="left"><input type="text" id="queryCarNumber"
											value="" />
											<div id="resultDiv"></div></td>
									</tr>
									<tr>
										<td>轨迹回放日期：</td>
										<td align="left"><input type="text" id="locusDate"
											readonly="readonly" value=""
											onclick="fPopCalendar(event,document.getElementById('locusDate'),document.getElementById('locusDate'),false)" />
										</td>
									</tr>
									<tr style="display:none;">
										<td>轨迹结束日期：</td>
										<td align="left"><input type="text" id="locusEndTime"
											readonly="readonly" value=""
											onclick="fPopCalendar(event,document.getElementById('locusEndTime'),document.getElementById('locusEndTime'),false)" />
										</td>
									</tr>
									<tr>
										<td colspan="2" class="tc"><input type="button"
											value="确定" id="search" /></td>
									</tr>
								</table>
								<div class="map_table" style="height:300px;" id="taskDiv">
									<div class="top_tab">
										<ul class="gis_content_right_main_tab">
											<li>按出车任务</li>
											<li class="active">按时段</li>
										</ul>
									</div>
									<div class="top_tab_content" style="display:none;">
										<table class="main-table1 tc" id="taskTable"
											style="width:96%;">
											<tr>
												<th>操作</th>
												<th>工单号</th>
												<th>仪表里程数</th>
												<th>GPS里程数</th>
											</tr>
										</table>
									</div>
									<div class="top_tab_content">
										<table class="main-table1 tc" id="hoursTable"
											style="width:96%;">
											<tr>
												<th>操作</th>
												<th>小时数</th>
												<th>GPS里程数</th>
											</tr>
											<tr>
												<td><input type="checkbox" bTime="00:00:00"
													eTime="08:59:59" onclick="showRwCarLocus('',this)" />
												</td>
												<td>0-9(时)</td>
												<td><em id="zehEm">0</em>
												</td>
											</tr>
											<tr>
												<td><input type="checkbox" bTime="09:00:00"
													eTime="11:59:59" onclick="showRwCarLocus('',this)" />
												</td>
												<td>9-12(时)</td>
												<td><em id="nehEm">0</em></em>
												</td>
											</tr>
											<tr>
												<td><input type="checkbox" bTime="12:00:00"
													eTime="13:59:59" onclick="showRwCarLocus('',this)" />
												</td>
												<td>12-14(时)</td>
												<td><em id="tthEm">0</em>
												</td>
											</tr>
											<tr>
												<td><input type="checkbox" bTime="14:00:00"
													eTime="18:59:59" onclick="showRwCarLocus('',this)" />
												</td>
												<td>14-19(时)</td>
												<td><em id="fehEm">0</em>
												</td>
											</tr>
											<tr>
												<td><input type="checkbox" bTime="19:00:00"
													eTime="23:59:59" onclick="showRwCarLocus('',this)" />
												</td>
												<td>19-24(时)</td>
												<td><em id="nthEm">0</em>
												</td>
											</tr>
										</table>
									</div>
								</div>
							</div>
							<div class="gis_content_right_main " style="display:none;">
								<ul class="title_content">
									<em>车牌:</em>
									<em></em>
									<em style="padding-left:20px;">日期:</em>
									<em></em>
								</ul>
								<ul
									style="border:1px solid #CCCCCC;margin:0 auto 10px;width:96%;background:#F5F5F5;">
									<img src='images/newLoading.gif'>
								</ul>
								<div id="ssPageContent"
									style="border:1px solid #CCCCCC;margin:0 auto 10px;width:96%"></div>
							</div>
						</div>
					</div>
					<%-- 右边轨迹回放查询区 END --%>
				</div>
			</div>
		</div>
	</div>
	<%-- 放置甘特图插件  --%>
	<div id="ctGantt"
		style="display: none;  position: absolute; z-index: 250;background:#F9F9F9;border: 1px solid #CCCCCC;"></div>
	<%-- 甘特图 END --%>

	<%-- 更多菜单  --%>
	<div id="moreMenuDiv"></div>
</body>
</html>
