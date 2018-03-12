 <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>LTE地图页面</title>
		<%@include file="commonheader.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../jslib/dialog/dialog.css" />
		<script type="text/javascript" src="js/rno_ltecell_map_display.js"></script>

<style type="text/css">
img {
	border: none;
}

.menu {
	box-shadow: 0px 2px 9px rgba(0, 0, 0, .6);
	height: 40px;
	background: #eaeaea;
	position: relative;
	background: #eaeaea url(../img/bg-menu.gif) repeat-x 0 bottom;
	line-height: 40px;
	min-width: 1086px;
	z-index: 99
}

/*清除浮动*/
.clearfix {
	display: block;
	zoom: 1;
}
.clearfix:after {
	content: ".";
	display: block;
	height: 40;
	clear: both;
	visibility: hidden;
}

.menu ul {
	width: 980px;
	height: 40px;
}

ul li {
	/*列表自左往右排*/
	float: left;
	list-style: none;
}

.menu li a {
	padding: 0 0 0 11px;
	display: block;
	height: 40px;
	text-decoration: none;
	color: #000;
	font-family: "微软雅黑";
	float: left;
}

.menu li a span {
	float: left;
	background: #eaeaea url(../img/bg-menuli.gif) no-repeat 100% center;
	display: block;
	height: 39px;
	padding: 0 40px 0 0;
}

.menu .menu-item {
	margin-left: 40px;
}

.submenu {
	float: right;
	display: none;
	z-index: 99999999;
	padding: 10px 0 20px;
	position: absolute;
	top: 40px;
	left: 0;
	background: #fff /*url(img/bg-submenu.gif) repeat-x 0 bottom*/;
	width: 100%;
	filter: alpha(opacity = 90);	/*ie*/
	-moz-opacity: 0.9;			/*firefox*/
	-khtml-opacity: 0.9;		/*Safari (1.x)*/
	/*opacity: 0.9;			/*Chrome,firefox 谷歌地图会挡住透明div*/
	_width: 1280px;		/*ie6下的宽度*/
}

.submenu dl {
	width: 180px;
	float: left;
}

.submenu dl a {
	width: 160px;
	font-weight: 400;
	display: block;
	height: 30px;
	line-height: 30px;
	border-radius: 4px
}
.submenu dl a:hover {/*鼠标覆盖变色*/
	background: #eaeaea;
	color: #0083d6;
	text-decoration: none;
}
.submenu dl a.selected {
	font-weight: bold;
	background: #C0BFBF;
	color: #0083d6;
}
.submenu dt {
	padding: 0 0 0 20px;
	font-weight: 800;
	height: 30px;
	line-height: 30px;
}


#map_canvas {
	width: 100%;
	height: 100%;
	position: absolute;
	z-index: 0;
}
</style>
<script type="text/javascript">
	//百度地图和谷歌地图切换，默认是百度地图
	//var glomapid='<%=session.getAttribute("mapId") %>';
	//console.log("页面glomapid："+glomapid);
	$(function() {
		//console.log("页面glomapid："+glomapid)
		//glomapid =<%=session.getAttribute("mapId") %>;
		//console.log("glomapid:"+glomapid);
		//console.log("glomapid:"+(glomapid==null));
		//$("#trigger").val(glomapid=='null' || glomapid=='baidu'?"切换谷歌":"切换百度");
		//console.log("jspstr:"+glomapid);
		$(".switch").click(function() {
			$(this).hide();
			$(".switch_hidden").show();
			$(".resource_list_icon").animate({
				right : '0px'
			}, 'fast');
			$(".resource_list_box").hide("fast");
		})
		$(".switch_hidden").click(function() {
			$(this).hide();
			$(".switch").show();
			$(".resource_list_icon").animate({
				right : '286px'
			}, 'fast');
			$(".resource_list_box").show("fast");
		})
		$(".zy_show").click(function() {
			$(".search_box_alert").slideToggle("fast");
		})

	})
</script>

	</head>

	<body>
		<%-- 菜单 start --%>
		<div class="menu clearfix">
			<ul style="width: 1150px" id="menu">
				<li onMouseOut="displayHidden('submenu1');"
					onMouseOver="displayBlock('submenu1');">
					<a class="menu-item" id="subTitle1">
						<span id="subTitle1a" onMouseOver="subTitle1On1();">PCI干扰分析</span>
					</a>
					<div id="submenu1" class="submenu">
						<dl>
							<dd>
								<a id="pciReuseAnalysis"
									onClick="showPciReuseAnalysis();" target="container">&nbsp;PCI复用分析</a>
							</dd>
						</dl>
						<dl>
							<dd>
								<a  id="pciCollisionAndConfusion"
									onClick="showPciCollisionAndConfusion();" target="container">&nbsp;PCI冲突与混淆检测</a>
							</dd>
						</dl>
				</li>
				<li onMouseOut="displayHidden('submenu2');"
					onMouseOver="displayBlock('submenu2');">
					<a class="menu-item" id="subTitle2">
						<span id="subTitle2a" onMouseOver="subTitle1On2();">栏目二</span>
					</a>
					<div id="submenu2" class="submenu">
						<dl>
							<dd>
								<a  id="Ba11111"
									onClick="setIntro('Ba11111');" target="container">&nbsp;Ba11111</a>
							</dd>
						</dl>
						<dl>
							<dd>
								<a  id="Ba22222"
									onClick="setIntro('Ba22222');" target="container">&nbsp;Ba22222</a>
							</dd>
						</dl>
						<dl>
							<dd>
								<a  id="Ba33333"
									onClick="setIntro('Ba33333');" target="container">&nbsp;Ba33333</a>
							</dd>
						</dl>
					</div>
				</li>
				<li onMouseOut="displayHidden('submenu3');"
					onMouseOver="displayBlock('submenu3');">
					<a class="menu-item" id="subTitle3">
					<span id="subTitle3a" onMouseOver="subTitle1On3();">栏目三</span>
					</a>
					<div id="submenu3" class="submenu">
						<dl>
							<dt>
								菜单一
							</dt>
							<dd>
								<a  id="Ca11111"
									onClick="setIntro('Ca11111');" target="container">&nbsp;Ca11111</a>
							</dd>
							<dd>
								<a  id="Ca22222"
									onClick="setIntro('Ca22222');" target="container">&nbsp;Ca22222</a>
							</dd>
							<dd>
								<a  id="Ca33333"
									onClick="setIntro('Ca33333');" target="container">&nbsp;Ca33333</a>
							</dd>
						</dl>
						<dl>
							<dt>
								菜单二
							</dt>
							<dd>
								<a  id="Cb11111"
									onClick="setIntro('Cb11111');" target="container">&nbsp;Cb11111</a>
							</dd>
							<dd>
								<a  id="Cb22222"
									onClick="setIntro('Cb22222');" target="container">&nbsp;Cb22222</a>
							</dd>
							<dd>
								<a  id="Cb33333"
									onClick="setIntro('Cb33333');" target="container">&nbsp;Cb33333</a>
							</dd>
						</dl>

						<dl>
							<dt>
								菜单三
							</dt>
							<dd>
								<a  id="Cc11111"
									onClick="setIntro('Cc11111');" target="container">&nbsp;Cc11111</a>
							</dd>
							<dd>
								<a  id="Cc22222"
									onClick="setIntro('Cc22222');" target="container">&nbsp;Cc22222</a>
							</dd>
							<dd>
								<a  id="Cc33333"
									onClick="setIntro('Cc33333');" target="container">&nbsp;Cc33333</a>
							</dd>
						</dl>
					</div>
				</li>
			</ul>
		</div>
		<%-- 菜单 end --%>

  		<%-- 地图展示区域 start --%>
			<div class="loading_cover" style="display: none;position:absolute;z-index:999">
				<div class="cover"></div>
				<h4 class="loading">
					正在加载 <em class="loading_fb">小区</em>资源,请稍侯...
				</h4>
			</div>

		  	<div class="div_left_main" style="width: auto; padding: 0px; margin: 0px;">
				<div class="div_left_content">
					<div style="padding-bottom: 0px; padding-top: 0px">
						<div class="map_hd" style="padding-bottom: 0px">
							<div class="head_box clearfix" style="padding-bottom: 0px; height: 0px;">
								<div class="dialog2 draggable ui-draggable">
										<div style="padding: 5px">

											<form id="conditionForm" method="post">
												省：<select name="provinceId" class="required" id="provinceId">
													<%-- option value="-1">请选择</option --%>
													<s:iterator value="provinceAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
												</select>
												市：<select name="cityId" class="required" id="cityId">
													<s:iterator value="cityAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
												</select>
												<%--  区：<select name="areaId" class="required" id="areaId">
													<s:iterator value="countryAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
												</select> --%>
												<input id="displaycell" type="button" value="加载小区" name="displaycell">
												<%-- 隐藏域参数 start--%>
												<input type="hidden" id="realPostAreaId" name="realPostAreaId" />
												<%-- <input  type="button" id="trigger" name="trigger" value=""/> --%>
			 									<input type="hidden" id="hiddenZoom" name="zoom" value="10" />
												<input type="hidden" id="hiddenLng"
													value="<s:property value='centerCityPoint.lng' />" />
												<input type="hidden" id="hiddenLat"
													value="<s:property value='centerCityPoint.lat' />" />
												<input
													type="hidden" id="hiddenPageSize" name="page.pageSize"
													value="100" />
												<input type="hidden" id="hiddenCurrentPage"
													name="page.currentPage" value="1" />
												<input type="hidden"
													name="page.forcedStartIndex" id="hiddenForcedStartIndex" />
												<input
													type="hidden" id="hiddenTotalPageCnt" />
												<input type="hidden"
													id="hiddenTotalCnt" />
												<%-- 隐藏域参数 end--%>
											</form>
											<div id="hiddenAreaLngLatDiv" style="display:none">
												<s:iterator value="cityAreas" id="onearea">
													<input type="hidden"
														id="areaid_<s:property value='#onearea.area_id' />"
														value="<s:property value='#onearea.longitude' />,<s:property value='#onearea.latitude' />">
												</s:iterator>
											</div>

										<%-- <div id="hiddenAreaLngLatDiv" style="display:none">
												<s:iterator value="countryAreas" id="onearea">
													<input type="hidden"
														id="areaid_<s:property value='#onearea.area_id' />"
														value="<s:property value='#onearea.longitude' />,<s:property value='#onearea.latitude' />">
												</s:iterator>
											</div> --%>

										</div>
								</div>
							</div>
						</div>
					</div>

					<%-- 地图--%>
					<div style="padding-top: 0px">
						<div class="map_bd">
							<div id="map_canvas"></div>
							<div class="resource_list_icon">
								<a href="#" class="switch"></a> <a href="#" class="switch_hidden"></a>
								<div class="shad_v"></div>
							</div>
							<div class="resource_list_box" style="height: 100%;">
								<input id="editLteCell" type="button" value="编辑信息" name="editLteCell">
								<div class="resource_list">
									<input type="hidden" id="lteCellId" value=""/>
									<table class="main-table1 half-width">
										<tr>
											<td class="menuTd" style="width: 20%">LTE小区名</td>
											<td id='showLteCellNameId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">覆盖类型</td>
											<td id='showLteCellCoverTypeId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">覆盖范围</td>
											<td id='showLteCellCoverRangeId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">下行带宽</td>
											<td id='showLteCellBandId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">下行频点</td>
											<td id='showLteCellEarfcnId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">频段类型</td>
											<td id='showLteCellBandTypeId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">天线挂高</td>
											<td id='showLteCellGroundHeightId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">RRU数量</td>
											<td id='showLteCellRrunumId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">RRU型号</td>
											<td id='showLteCellRruverId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">天线型号</td>
											<td id='showLteCellAntennaTypeId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">天线是否合路</td>
											<td id='showLteCellIntegratedId'></td>
										</tr>
										<tr>
											<td class="menuTd" style="width: 20%">参考信号功率dBm</td>
											<td id='showLteCellRspowerId'></td>
										</tr>
									</table>
								</div>

							</div>
							<%-- 地图高宽--%>
							<div class="htl_map_move">
								<div>
									<iframe frameborder="none" src=""
										style="border: medium none; width: 1600px; height: 650px;">
									</iframe>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>

			<%-- Lte小区信息编辑框 start--%>
			<div id="editLteCellMessage" class="dialog2 draggable"
					style="display:none; width:700px; right:450px;">
				<div class="dialog_header">
					<div class="dialog_title">
							LTE小区信息编辑
					</div>
					<div class="dialog_tool">
						<div class="dialog_tool_close dialog_closeBtn"
							onclick="$('#editLteCellMessage').hide()"></div>
					</div>
				</div>
				<div class="dialog_content" style="width:700px; background:#f9f9f9">
					<div style="font-weight: bold;margin-bottom:5px">
						eNodeB名称：
						<span id="VIEW_ENODEB_NAME"></span>
						&nbsp;&nbsp;&nbsp;->小区名称：
						<span id="VIEW_CELL_NAME"></span>
					</div>
					<form id="lteCellDetailForm">
						<input type="hidden" id="lteCellIdForEdit" name="lteCellId" value=""/>
						<table id="viewCellDetailTable" border="1" align="center" class="main-table1 half-width">

						</table>
						&nbsp;&nbsp;&nbsp;
						<input id="btnUpdate" type="button" value="修 改" name="btnUpdate">
						&nbsp;&nbsp;&nbsp;
						<input id="btnReset" type="button" value="重 置" name="btnReset">
					</form>
				</div>
			</div>
			<%-- Lte小区信息编辑框 end--%>

			<%-- PCI复用分析框 start --%>
			<div class="loading_cover" id="pciReuseCoverDiv"
					style="display: none;">
					<div class="cover"></div>
					<h4 class="loading">正在加载PCI复用分析数据,请稍侯...</h4>
			</div>
			<div id="pciReuse_Dialog" class="dialog2 draggable"
				style="display:none;width:902px;top:126px; right: 298px; z-index: 30;">
					<div class="dialog_header">
						<div class="dialog_title">PCI复用分析结果</div>
						<div class="dialog_tool">
							<div class="dialog_tool_close dialog_closeBtn"
								onclick="$('#pciReuse_Dialog').hide();"></div>
						</div>
					</div>
					<div class="dialog_content" id="report_Div"
						style="width:900px;height:422px; background:#f9f9f9">
					<table>
						<tr>
							<td width="90%">
								<table id="viewPciReuseAnalysis" class="greystyle-standard" width="100%">
									<tr>
										<th style="width: 1%">PCI</th>
										<th style="width: 11%">小区</th>
										<th style="width: 8%">复用距离小于小区覆盖距离</th>
									</tr>
								</table>
							</td>
							<td width="30%">
								<table id="viewPciReuseCell" border="1" align="center" class="greystyle-standard">
									<tr>
										<th id="pciReuseCellTitle" colspan="3" style="width: 8%">与<>同PCI()的小区，覆盖距离：</th>
									</tr>
									<tr>
										<th colspan="1" style="width: 10%">小区</th>
										<th colspan="1" style="width: 40%">小区覆盖距离</th>
										<th colspan="1" style="width: 40%">复用距离</th>
									</tr>

								</table>
							</td>
						</tr>
					</table>
					</div>
					<br>
					<form id="pciReuseForm" method="post">
							<input type="hidden" id="areaIdForPciAnalysis" name="areaIdForPciAnalysis" />
							<%-- 分页信息 --%>
							<input type="hidden" id="pciPageSize" name="page.pageSize" value="5" />
							<input type="hidden" id="pciCurrentPage" name="page.currentPage" value="1" />
							<input type="hidden" id="pciTotalPageCnt" name="page.totalPageCnt" value="-1" />
							<input type="hidden" id="pciTotalCnt" name="page.totalCnt" value="-1" />
					</form>
					<div class="paging_div" id="pciPageDiv" style="border: 1px solid #ddd">
						<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
							条记录
						</span>
						<a class="paging_link page-first" title="首页"
							onclick="showListViewByPage('first')"></a> <a
							class="paging_link page-prev" title="上一页"
							onclick="showListViewByPage('back')"></a> 第 <input type="text"
							id="showCurrentPage" class="paging_input_text" value="1" /> 页/<em
							id="emTotalPageCnt">0</em>页 <a class="paging_link page-go" title="GO"
							onclick="showListViewByPage('num')">GO</a> <a
							class="paging_link page-next" title="下一页"
							onclick="showListViewByPage('next')"></a> <a
							class="paging_link page-last" title="末页"
							onclick="showListViewByPage('last')"></a>
					</div>
			</div>
			<%-- PCI复用分析框 end --%>

			<%-- PCI冲突与混淆框 start --%>
			<div class="loading_cover" id="pciCollAndConfCoverDiv"
					style="display: none;">
					<div class="cover"></div>
					<h4 class="loading">正在加载PCI冲突与混淆的小区数据,请稍侯...</h4>
			</div>
			<div id="pciCollAndConf_Dialog" class="dialog2 draggable"
				style="display:none;width:902px;top:126px; right: 298px; z-index: 30;">
					<div class="dialog_header">
						<div class="dialog_title">PCI冲突与混淆分析结果</div>
						<div class="dialog_tool">
							<div class="dialog_tool_close dialog_closeBtn"
								onclick="$('#pciCollAndConf_Dialog').hide();"></div>
						</div>
					</div>
					<div class="dialog_content" id="report_Div"
						style="width:900px;height:422px; background:#f9f9f9">
							<table id="viewCollAndConfAnalysis" class="greystyle-standard" width="100%">
								<tr>
									<th style="width: 1%">PCI</th>
									<th style="width: 35%">冲突小区</th>
									<th style="width: 65%">混淆小区</th>
								</tr>
							</table>
					</div>
					<br>
					<form id="pciCollAndConfForm" method="post">
							<input type="hidden" id="areaIdForCollAndConfAnalysis" name="areaIdForCollAndConfAnalysis" />
							<%-- 分页信息 --%>
							<input type="hidden" id="collAndConfPageSize" name="page.pageSize" value="5" />
							<input type="hidden" id="collAndConfCurrentPage" name="page.currentPage" value="1" />
							<input type="hidden" id="collAndConfTotalPageCnt" name="page.totalPageCnt" value="-1" />
							<input type="hidden" id="collAndConfTotalCnt" name="page.totalCnt" value="-1" />
					</form>
					<div class="paging_div" id="collAndConfPageDiv" style="border: 1px solid #ddd">
						<span class="mr10">共 <em id="pciCollAndConfShowTotalCnt" class="blue">0</em>
							条记录
						</span>
						<a class="paging_link page-first" title="首页"
							onclick="showPciCollAndconfListViewByPage('first')"></a> <a
							class="paging_link page-prev" title="上一页"
							onclick="showPciCollAndconfListViewByPage('back')"></a> 第 <input type="text"
							id="pciCollAndConfShowCurrentPage" class="paging_input_text" value="1" /> 页/<em
							id="pciCollAndConfShowTotalPageCnt">0</em>页 <a class="paging_link page-go" title="GO"
							onclick="showPciCollAndconfListViewByPage('num')">GO</a> <a
							class="paging_link page-next" title="下一页"
							onclick="showPciCollAndconfListViewByPage('next')"></a> <a
							class="paging_link page-last" title="末页"
							onclick="showPciCollAndconfListViewByPage('last')"></a>
					</div>
			</div>
			<%-- PCI冲突与混淆 end --%>

		<%-- 地图展示区域 end --%>
	</body>
</html>
