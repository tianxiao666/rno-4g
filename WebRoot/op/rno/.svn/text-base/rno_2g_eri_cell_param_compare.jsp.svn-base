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

<title>爱立信小区参数对比</title>

<%@include file="commonheader.jsp"%>
<link rel="stylesheet" href="css/jquery.treeview.css" />
<script src="jslib/tree/jquery.treeview.js"></script>
<script src="js/rno_2g_eri_cell_param_compare.js"></script>
</head>

<body>
	<%-- 数据加载遮罩层 --%>
	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="loadContentId"></em>,请稍侯...</br>
			 <span id="progressDesc"></span>
		</h4>
	</div>

	<div class="div_left_main" width="auto">	
	<table width="100%" heigth="100%" class="main-table1 half-width">
		<tr>
			<td width="255px;">
				<input type="radio" name="paramType" value="cell" checked="checked" onclick="changeParam()"/> Cell &nbsp;
				<input type="radio" name="paramType" value="channel" onclick="changeParam()" /> Channel &nbsp;
				<input type="radio" name="paramType" value="neighbour" onclick="changeParam()" /> Neighbour &nbsp;
			</td>
			<td>
				日期	<input id="date1" name="date1"
						value="<s:property value="dayBefore" />" type="text"
						onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly class="Wdate input-text" style="width: 132px;" />&nbsp;&nbsp;
				日期 <s:set name="date2" value="new java.util.Date()" />
					<input id="date2" name="date2"
						value="<s:date name="date2" format="yyyy-MM-dd" />" type="text"
						onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly class="Wdate input-text" style="width: 132px;" />
				&nbsp;&nbsp;	
				<input id="compareBtn" type="button" value="比较"/>&nbsp;&nbsp;	
				<input id="exportBtn" type="button" value="导出"/>
				<form id="downloadEriCellAnalysisDataFileForm"
					 action="downloadEriCellAnalysisDataFileAction" method="post">
					 <input type="hidden" id="token" name="token" value=""/>
				</form>
			</td>
		</tr>
		<tr>
			<td>
				<table>
					<tr>
						<td>
						省：<select name="provinceId" id="provinceId" style="width:80px;">
							<%-- option value="-1">请选择</option --%>
							<s:iterator value="provinceAreas" id="onearea">
								<option value="<s:property value='#onearea.area_id' />">
									<s:property value="#onearea.name" />
								</option>
							</s:iterator>
						</select>
						市：<select name="cityId" id="cityId" style="width:100px;">
							<s:iterator value="cityAreas" id="onearea">
								<option value="<s:property value='#onearea.area_id' />">
									<s:property value="#onearea.name" />
								</option>
							</s:iterator>
						</select> 
						<br/>
						<div style="border:1px solid #CDCDCD;background:white;overflow-x:scroll;overflow-y:scroll;width: 240px;height: 400px">
							<ul id="allBsc" >
							</ul>
						</div>
						</td>
					</tr>
					<tr>
						<td>
							<div style="border:1px solid #CDCDCD;background:white;overflow-x:scroll;overflow-y:scroll;width: 240px;height: 400px">
								<ul id="paramCheckBox">
								</ul>
							</div>
						</td>					
					</tr>
				</table>

			</td>
			<td style="vertical-align:top;">

					<table id="contentTab" width="100%">
						<tr>
							<td align="center">
								<span>BSC小区参数比较概要内容</span>
								<div id="paramDiffDiv" style="overflow:scroll;height:470px;">
								<table id="paramDiffTable" class="main-table1 half-width" width="80%">
	
								</table>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								参数：&nbsp;&nbsp;<span id="paramName" style="color :red;"></span>
							</td>
						</tr>
						<tr>
							<td>
								<div id="paramDiffDetailDiv" style="overflow:scroll;height:300px;">
								<table id="paramDiffDetailTable" class="main-table1 half-width" width="80%">
								
								</table>
								</div>
							</td>
						</tr>
					</table>
			</td>
		</tr>
	</table>
	
</body>
</html>

