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

<title>NCS小区信息查看</title>

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
<script src="jslib/highcharts/highcharts.js"></script>
<script src="jslib/highcharts/exporting.js"></script>
<script src="js/rno_structure_ncs_cell_report.js"></script>

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
</head>

<body>
	<%-- 遮罩层 --%>
	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="loadContentId"></em>,请稍侯...
		</h4>
	</div>
	<%-- 日期选择div --%>
	<div id="selectCheckDateDiv" class="dialog2 draggable"
									style="display:none; left:25%; top: 40%;width: 360px;">
			<div class="dialog_header">
				<div class="dialog_title">选择日期</div>
				<div class="dialog_tool">
					<div class="dialog_tool_close dialog_closeBtn" onclick="$('#selectCheckDateDiv').hide();">
					</div>
				</div>
			</div>
			<div class="dialog_content" style="background:#f9f9f9;padding:10px">
			 	开始日期<input type="text" value="" id="mrrMeaBegDate"
							name="" style="width:100px;margin-left:8px;margin-right:8px" />
				结束日期<input type="text" value="" id="mrrMeaEndDate"
							name="" style="width:100px;margin-left:8px;margin-right:8px" />
			</div>
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
					<tr>
						<td style="vertical-align: top" colspan="3">
							<div style="margin:10px">
								日期:<select id='ncsDate' style="width:100px"></select> &nbsp;&nbsp;&nbsp;
								时间:<select id="ncsTime" style="width:80px"></select> &nbsp;&nbsp;&nbsp;
								<%-- <input type="button" id="selectDate" value="更多" /> &nbsp;&nbsp;&nbsp;--%>
								数据类型:<select id="ncsDataType" style="width:120px">
									<option value='six'>六强比例</option>
									<option value='two'>两强比例</option>
									<option value='cellrate'>强于主小区比例</option>
									<option value='abss'>ABSS</option>
									<option value='alone'>ALONE</option>
								</select>&nbsp;&nbsp;
								<%-- <input type="button" value="小区频点干扰" id="interfer" style="top:2px;left:5px"/> --%>
							</div>
						</td>
					</tr>

					<tr>
						<td style="width:70%;vertical-align: top">
							<div id="chartDiv" style="padding:3px;width:800px;height: 572px"></div>
						</td>
						<td style="vertical-align: top">
							<div id="ncsInfoDiv" style="float: left; width:240px">
								<table class="main-table1 half-width" id="ncsInfoTab" width="100%">
									<tr>
										<td colspan=2>测量信息</td>
									</tr>
									<TR>
										<td class='menuTd'>BSC</td>
										<td></td>
									</TR>
									<TR>
										<td class='menuTd'>CREATE_TIME</td>
										<td></td>
									<TR>
										<td class='menuTd'>RECORDCOUNT</td>
										<td></td>
									<TR>
										<td class='menuTd'>RID</td>
										<td></td>
									<TR>
										<td class='menuTd'>RELSS</td>
										<td></td>
									<TR>
										<td class='menuTd'>RELSS2</td>
										<td></td>
									<TR>
										<td class='menuTd'>RELSS3</td>
										<td></td>
									<TR>
										<td class='menuTd'>RELSS4</td>
										<td></td>
									<TR>
										<td class='menuTd'>RELSS5</td>
										<td></td>
									<TR>
										<td class='menuTd'>NCELLTYPE</td>
										<td></td>
									<TR>
										<td class='menuTd'>NUMFREQ</td>
										<td></td>
									<TR>
										<td class='menuTd'>PERIODLEN</td>
										<td></td>
									<TR>
										<td class='menuTd'>TERMREASON</td>
										<td></td>
									<TR>
										<td class='menuTd'>RECTIME</td>
										<td></td>
									<TR>
										<td class='menuTd'>ECNOABSS</td>
										<td></td>
									<TR>
										<td class='menuTd'>NUCELLTYPE</td>
										<td></td>
									<TR>
										<td class='menuTd'>TFDDMRR</td>
										<td></td>
									<TR>
										<td class='menuTd'>NUMUMFI</td>
										<td></td>
								</table>
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
								<div id="toggleCellInfoDiv" style="padding:2px;background:#B1C3D9;cursor:pointer; margin-left:6px">原始数据(点击展开)</div>
								<%-- main-table1 half-width --%>
		<table class="greystyle-standard" id="cellInfoTab" style="display:none" align="left">
									<tr>
										<td>序号</td>
										<TD>CELL</TD>
										<TD>BSIC</TD>
										<TD>ARFCN</TD>
										<TD>REP</TD>
										<TD>TOPSIX</TD>
										<TD>TOPTWO</TD>
										<td>ABSS</td>
										<TD>ALONE</td>
										<td>NCELL</td>
										<TD>DISTANCE</TD>
										<TD>NCELLS</TD>
										<TD>DEFINED</TD>
									</tr>
								</table>
							</div>
		</td></tr>
	</table>
	
	<div id="operInfo"
		style="display:none; top: 40%; left:35%; width: 400px; height: 40px; background-color: rgb(125, 255, 63); opacity: 0.8; z-index: 9999; position: fixed;">
		<table height="100%" width="100%" style="text-align:center">
			<tr>
				<td><span id="operTip"></span></td>
			</tr>
		</table>

	</div>
</body>
</html>
