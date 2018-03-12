<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>结构分析任务总览信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>
<script type="text/javascript" src="js/rno_structure_ncs_analysis_v2.js?v=<%=(String)session.getAttribute("version")%>"></script>
  <style type="text/css">
  	.divcenter { MARGIN-RIGHT: auto; MARGIN-LEFT: auto; }
  	.detailTable{border-collapse:separate;border-spacing:10px;} 
  </style>
  </head>
  
  <body>
  	<%-- 数据加载遮罩层 --%>
	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="loadContentId"></em>,请稍侯...
		</h4>
	</div>
	
	<form id='initNcsAnalysisPageForm' action="initNcsAnalysisPageAction" method='post' style="display:none">
		<input type="hidden" id="cityIdParam" name="cityId" value="<s:property value="#session.NCSTASKINFO.taskInfo.cityId" />"/>
	</form>
    <font style="font-weight: bold;">当前位置： 专项优化 &gt; 结构分析 &gt; 新建结构分析任务</font>
	<br>
	<center>
	 <a href="initNcsAnalysisPageAction" style="text-decoration: underline;font-weight: bold;">&lt;&lt;返回任务列表</a><br>
  	 <font style="font-weight: bold;">任务信息>>参数配置>><font style="color: #31FF81;">提交任务</font></font>
  	 <br/><br/>
  	</center>


		<div class="divcenter" style="width: 800px;">
			<center>
				<font style="font-size: large; font-weight: bold;">任务提交</font>
				</h1>
			</center>
			<input type="button" id="overviewInfoPreStep" name="" value="<上一步 " />
			<div style="height: 2px; border-bottom: 1px #000 solid;"></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="detailTable">
				<tr>
					<td colspan="2" align="center" style="font-size: medium;">
						任务信息
					</td>
				</tr>
				<tr>
					<td width="19%">
						任务名称
					</td>
					<td width="81%">
						<s:property value="#session.NCSTASKINFO.taskInfo.taskName" />
					</td>
				</tr>
				<tr>
					<td width="19%">
						分析地市
					</td>
					<td width="81%">
						<s:property value="#session.NCSTASKINFO.taskInfo.provinceName" />
						<s:property value="#session.NCSTASKINFO.taskInfo.cityName" />
					</td>
				</tr>
				<tr>
					<td width="19%">
						测量时间
					</td>
					<td width="81%">
						<s:property value="#session.NCSTASKINFO.taskInfo.startTime" /> - 
						<s:property value="#session.NCSTASKINFO.taskInfo.endTime" />
					</td>
				</tr>
				<tr>
					<td>
						任务描述
					</td>
					<td>
						<s:property value="#session.NCSTASKINFO.taskInfo.taskDesc" />
					</td>
				</tr>
			</table>
			<div style="height: 2px; border-bottom: 1px #000 solid;"></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"  class="detailTable">
				<tr>
					<td colspan="2" align="center" style="font-size: medium;">
						爱立信数据
					</td>
				</tr>
				<tr>
					<td width="19%">
						<table id="eriDataDetailTable"  class="main-table1 half-width">
							<tr>
								<th>日期</th>
								<%-- <th>小区配置小区数</th>
								<th>NCS内小区数</th>
								<th>MRR内小区数</th>--%>
								<th>BSC个数</th>
								<th>NCS文件数量</th>
								<th>MRR文件数量</th>
							</tr>
							<s:iterator value="#session.NCSTASKINFO.eriInfo" id="one">
								<tr>
									<td><s:property value='#one["DATETIME"]' /></td>
									<%--  <td><s:property value='#one["CELL_NUM"]' /></td>
									<td><s:property value='#one["NCS_NUM"]' /></td>
									<td><s:property value='#one["MRR_NUM"]' /></td>--%>
									<td><s:property value='#one["BSC_NUM"]' /></td>
									<td><s:property value='#one["NCSFILE_NUM"]' /></td>
									<td><s:property value='#one["MRRFILE_NUM"]' /></td>
								</tr>
							</s:iterator>
						</table>
					</td>
				</tr>

			</table>
			<div style="height: 2px; border-bottom: 1px #000 solid;"></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"  class="detailTable">
				<tr>
					<td colspan="2" align="center" style="font-size: medium;">
						华为数据
					</td>
				</tr>
				<tr>
					<td>
						<table id="hwDataDetailTable" class="main-table1 half-width">
							<tr>
								<th>日期</th>
								<%-- <th>小区配置小区数</th>
								<th>NCS内小区数</th>
								<th>MRR内小区数</th>--%>
								<th>BSC个数</th>
							</tr>
							<s:iterator value="#session.NCSTASKINFO.hwInfo" id="one">
								<tr>
									<td><s:property value='#one["DATETIME"]' /></td>
									<%--  <td><s:property value='#one["CELL_NUM"]' /></td>
									<td><s:property value='#one["NCS_NUM"]' /></td>
									<td><s:property value='#one["MRR_NUM"]' /></td>--%>
									<td><s:property value='#one["BSC_NUM"]' /></td>
								</tr>
							</s:iterator>
						</table>
					</td>
				</tr>
			</table>
			<div style="height: 2px; border-bottom: 1px #000 solid;"></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"  class="detailTable">
				<tr>
					<td style="padding-left: 80px">
						<input type="button" id="submitTask" value="提交任务" onclick="this.disabled=true;"/>
					</td>
					<td style="padding-left: 2px">
						<input type="button" id="cancleTask" value="取消任务" />
					</td>
				</tr>
			</table>
		</div>
		<div id="operInfo"
			style="display: none; top: 40px; left: 600px; z-index: 999; width: 400px; height: 40px; background-color: #7dff3f; filter: alpha(Opacity = 80); -moz-opacity: 0.5; opacity: 0.5; z-index: 9999; position: fixed;">
			<table height="100%" width="100%" style="text-align: center">
				<tr>
					<td>
						<span id="operTip"></span>
					</td>
				</tr>
			</table>

		</div>
	</body>
</html>
