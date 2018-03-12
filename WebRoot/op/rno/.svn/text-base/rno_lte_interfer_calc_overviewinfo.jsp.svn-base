<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>区域PCI翻频方案总览信息</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>
<link href="jslib/jquery-ui-1.11.1.custom/jquery-ui.css"
	rel="stylesheet">
<link
	href="jslib/jquery-ui-1.11.1.custom/jquery-ui-timepicker-addon.css"
	rel="stylesheet">
<script src="jslib/jquery-ui-1.11.1.custom/jquery-ui.js"></script>
<script type="text/javascript" src="js/rno_lte_interfer_calc.js?v=<%=(String)session.getAttribute("version")%>"></script>
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

	<input type="hidden" id="lteCellsFromSession" value="<s:property value="#session.MRTASKINFO.taskInfo.lteCells"/>"/>

	<form id='initNcsAnalysisPageForm' action="initLteInterferCalcPageAction" method='post' style="display:none">
		<input type="hidden" id="cityIdParam" name="cityId" value="<s:property value="#session.MRTASKINFO.taskInfo.cityId" />"/>
	</form>
    <font style="font-weight: bold;">当前位置： 干扰计算&gt; 新建区域PCI翻频方案任务</font>
	<br>
	<center>
	 <a href="initLteInterferCalcPageAction" style="text-decoration: underline;font-weight: bold;">&lt;&lt;返回任务列表</a><br>
  	 <font style="font-weight: bold;">任务信息>>参数配置>><font style="color: #31FF81;">提交任务</font></font>
  	 <br/><br/>
  	</center>

		<div style="margin-bottom:15px;">
		<div class="divcenter" style="width: 800px;">
			<center>
			<h1>
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
						<s:property value="#session.MRTASKINFO.taskInfo.taskName" />
					</td>
				</tr>
				<tr>
					<td width="19%">
						分析地市
					</td>
					<td width="81%">
						<s:property value="#session.MRTASKINFO.taskInfo.provinceName" />
						<s:property value="#session.MRTASKINFO.taskInfo.cityName" />
					</td>
				</tr>
				<tr>
					<td width="19%">
						测量时间
					</td>
					<td width="81%">
						<s:property value="#session.MRTASKINFO.taskInfo.startTime" /> -
						<s:property value="#session.MRTASKINFO.taskInfo.endTime" />
					</td>
				</tr>
				<tr>
					<td>
						任务描述
					</td>
					<td>
						<s:property value="#session.MRTASKINFO.taskInfo.taskDesc" />
					</td>
				</tr>
			</table>

			<%--  <div style="height: 2px; border-bottom: 1px #000 solid;"></div>
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
								<th>MR测量数量</th>
								<th>HO切换数量</th>
							</tr>
							<s:iterator value="#session.MRTASKINFO.eriInfo" id="one">
								<tr>
									<td><s:property value='#one["DATETIME"]' /></td>
									<td><s:property value='#one["MR_RECORD_NUM"]' /></td>
									<td><s:property value='#one["HO_RECORD_NUM"]' /></td>
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
						中兴数据
					</td>
				</tr>
				<tr>
					<td>
						<table id="hwDataDetailTable" class="main-table1 half-width">
							<tr>
								<th>日期</th>
								<th>MR测量数量</th>
								<th>HO切换数量</th>
							</tr>
							<s:iterator value="#session.MRTASKINFO.zteInfo" id="one">
								<tr>
									<td><s:property value='#one["DATETIME"]' /></td>
									<td><s:property value='#one["MR_RECORD_NUM"]' /></td>
									<td><s:property value='#one["HO_RECORD_NUM"]' /></td>
								</tr>
							</s:iterator>
						</table>
					</td>
				</tr>
			</table>
			<div style="height: 2px; border-bottom: 1px #000 solid;"></div>
			--%>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"  class="aa">
				<tr>
					<td colspan="2" align="center" style="font-size: medium;">
						变PCI小区表<span style="font-size: smaller"><font color="red">(以逗号</font>,<font color="red">分隔)</font></span>
					</td></tr>
					<tr>
					<td style="padding-left: 2px">
					<table id="11" class="main-table1 half-width">
								<tr>
									<td><textarea name="fd" id="pciCell" style="width:100%; height:300px"></textarea></td>
								</tr>

						</table>

					</td>
				</tr>
			</table>
			<%--<div style="height: 2px; border-bottom: 1px #000 solid;"></div>--%>

			<form id="formImportPciPlanFile" enctype="multipart/form-data"
					method="post">
				<input type="hidden" name="fileCode" id='fileCode'
					value="RNO_PCI_PLAN_IMPORT" />
				<input type="hidden" name="token"
					id="token" value="" />
				<input type="hidden" id="uploadCityId" name="cityId"
						value="<s:property value="#session.MRTASKINFO.taskInfo.cityId" />"/>
				<input type="hidden" name="meaTime" class="" id='meatime'
						value="<s:property value="#session.MRTASKINFO.taskInfo.startTime" />"/>
				<div>
					<table class="main-table1 half-width"
						style="margin-left:0;width:100%">
						<tbody>
							<tr>
								<td style="text-align:left;">
									<input type="radio" name="calcType" value="dbType"  id="pciPlan" checked="checked"/>
									采用日期范围数据</td>
								<td style="text-align:left;">
									<input type="radio" name="calcType" value="fileType" id="pciPlanImport"/>
									采用导入的干扰矩阵文件(csv)
								<input type="file" style="width: 44%;" name="file" id="fileid"
									class="canclear required" /></td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="container-bottom" style="padding-top: 10px;">
					<%--<table style="width: 60%; margin: auto" align="center">
						<tr>
							<td><input type="button" value="导    入"
								style="width: 90px;" id="importBtn" name="import" />
								<br />
								<div id="uploadMsgDiv" style="display:none"></div>
								</td>
						</tr>
					</table>--%>
					<div id="uploadMsgDiv" style="display:none"></div>
					<div id="importResultDiv" class="container-bottom"
						style="padding-top: 10px;height: 0px;">
						<div id="progressInfoDiv" style="width:100%;display:none">
							<h2 id="progressNum">0%</h2>
							<div id="progressbar"></div>
						</div>
					</div>
				</div>
			</form>

			<table width="100%" border="0" cellspacing="0" cellpadding="0"  class="detailTable">
				<tr>
					<td style="padding-left: 80px">
						<input type="button" id="submitTask" value="提交任务" />
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
		</div>
	</body>

	<script  type="text/javascript" >
 		//var lteCells = $("#lteCellsFromSession").val();
 		//$("#pciCell").val(lteCells);
 	</script>
</html>
