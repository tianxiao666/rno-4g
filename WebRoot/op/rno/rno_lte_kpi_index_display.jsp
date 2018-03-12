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

<title>KPI指标图表展现</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>
<link rel="stylesheet" href="css/jquery.treeview.css" />
<script src="jslib/tree/jquery.cookie.js"></script>
<script src="jslib/tree/jquery.treeview.js"></script>
<%--script src="jslib/highcharts/highcharts.js"></script  --%>
<%--script src="jslib/highcharts/exporting.js"></script  --%>
<script src="jslib/echarts/echarts.js"></script>
<script src="js/rno_lte_kpi_index_display.js"></script>
<link href="jslib/jquery-ui-1.11.1.custom/jquery-ui.css"
	rel="stylesheet">
<link
	href="jslib/jquery-ui-1.11.1.custom/jquery-ui-timepicker-addon.css"
	rel="stylesheet">
<script
	src="jslib/jquery-ui-1.11.1.custom/jquery-ui-timepicker-addon.js"></script>

<style type="text/css">
.bscCls {
	
}

.cellCls {
	cursor: pointer
}
</style>
<script type="text/javascript">
require.config({
    packages: [
        {
            name: 'echarts',
            location: './jslib/echarts',
            main: 'echarts'
        }
    ]
});
</script>
</head>

<body>
<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在加载 <em class="loading_fb" id="tipcontentId"></em>,请稍侯...
		</h4>
	</div>
	<table width="100%" height="100%">
		<tr>
			<td width="16%" bgcolor="#E2ECF7" style="vertical-align: top">
				<div style="margin:10px">
					<div style=" width:152px">
						<select name="provinceId" class="required" id="provinceId1">
							<%-- option value="-1">请选择</option --%>
							<s:iterator value="provinceAreas" id="onearea">
								<option value="<s:property value='#onearea.area_id' />">
									<s:property value="#onearea.name" />
								</option>
							</s:iterator>
						</select>
						<select name="cityId" class="required" id="cityId1">
							<s:iterator value="cityAreas" id="onearea">
								<option value="<s:property value='#onearea.area_id' />">
									<s:property value="#onearea.name" />
								</option>
							</s:iterator>
						</select> <br /> 输入小区:<input type="text" name="" id="inputCell" value="" style="width: 194px"/>
					</div>
					<div style="border:1px solid #CDCDCD;background:white;overflow-x:scroll;overflow-y:scroll;width: 192px;height: 524px">
						<ul id="allBscCell" class="filetree">
						</ul>

					</div>
				</div>


			</td>
			<td bgcolor="#E2ECF7" style="vertical-align: top">
				<table width="100%">
					<tr style="vertical-align: top">
						<td></td>
					</tr>

					<tr>
						<td style="width:70%;vertical-align: top" align="center">
							<div id="chartDiv" style="width:100%;height: 450px" title="双击全屏" alt="双击全屏"></div>
						</td>
						<td style="vertical-align: top">
						<div class="resource_list300_box" style="height:610px;width: 250px">
						<div class="resource_list300">
							<div id="div_tab" class="div_tab divtab_menu">
								<ul>
									<li class="selected">查询条件</li>
									<li>指标详情</li>
								</ul>
							</div>
						</div>
						<div class="divtab_content" style="overflow-y:auto; overflow-x:auto; width:100%; height:600px;">
							<div id="div_tab_0" style="background-color: #F5F5F5">
							<%-- 分隔栏--%>
								<div class="div-m-split-10px"></div>
								<div class="div_title_24px_blue title_width">
									<span class="sp_title analysis_list_title">开始日期</span>&nbsp;&nbsp;&nbsp;<input type="text" value="" id="mrMeaBegDate"
															name="" style="width:100px;margin-left:8px;margin-right:8px" />
								</div>
								<%-- 分隔栏--%>
								<div class="div-m-split-10px"></div>
								<div class="div_title_24px_blue title_width">
									<span class="sp_title analysis_list_title">结束日期</span>&nbsp;&nbsp;&nbsp;<input type="text" value="" id="mrMeaEndDate"
															name="" style="width:100px;margin-left:8px;margin-right:8px" />
								</div>
								<%-- 分隔栏--%>
								<div class="div-m-split-10px"></div>
								<div class="div_title_24px_blue title_width">
									<span class="sp_title analysis_list_title">数据类型</span>&nbsp;&nbsp;&nbsp;<select
																	id='mrDataType'
																	style="width:100px;margin-left:8px;margin-right:8px">
																	<option>RSRP</option>
																	<option>RSRQ</option>
																	<option>覆盖率</option>
																	</select>
								</div>
								
								<%-- 分隔栏--%>
								<div class="div-m-split-10px"></div>
								<div class="div_title_24px_blue title_width">
									<span class="sp_title analysis_list_title">展示方式</span>&nbsp;&nbsp;&nbsp;<select
																	id='mrDisMode'
																	style="width:100px;margin-left:8px;margin-right:8px">
																	<option>柱状图</option>
																	</select>
								</div>
							</div>
							<div id="div_tab_1" style="display:none">
							<div id="mrrInfoDiv" style="border:1px solid #CDCDCD;background:white;float: left; width:100%;overflow: scroll;height: 565px">
								<table class="main-table1 half-width" id="mrrInfoTab" width="100%">
									<tr>
										<td colspan=2>测量信息</td>
									</tr>
									
								</table>
							</div>
							</div>
						</div>
					</div>
							
						<td>
					</tr>
					<tr>
						<td colspan="2">
							
						</td>

					</tr>

				</table>

			</td>
		</tr>
		<tr valign="top"><td colspan="2" bgcolor="#E2ECF7">
		<div style="margin-left: 10px">
		<div id="fullScreenChart" style="display:none" title="双击关闭" alt="双击关闭">
		</div>
		<%-- 浮窗 --%>
		<div class="dialog2 draggable ui-draggable" style="left: 381px; top: 119px;display: none">
							<div style="padding: 5px">
							
							</div>
						</div>
	<div id="operInfo"
		style="display:none; top:40px;left:600px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
		<table height="100%" width="100%" style="text-align:center">
			<tr>
				<td><span id="operTip"></span></td>
			</tr>
		</table>

	</div>
</body>
</html>
