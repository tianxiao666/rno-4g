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

<title>爱立信小区参数一致性检查</title>

<%@include file="commonheader.jsp"%>
<link rel="stylesheet" href="css/jquery.treeview.css" />
<script src="jslib/tree/jquery.treeview.js"></script>
<script src="js/rno_2g_eri_cell_param_check.js"></script>

</head>

<body>
	
	<%-- 数据加载遮罩层 --%>
	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="loadContentId"></em>,请稍侯...<br/>
			 <span id="progressDesc"></span>
		</h4>
	</div>
	<div class="div_left_main" width="auto">	
		<table id="contentTab" width="100%" heigth="100%" class="main-table1 half-width">
			<tr>
				<td style="vertical-align:top;">
					<div style="border: 1px solid #CDCDCD; background: white; overflow-x: scroll; 
								width:240px; overflow-y: scroll; height: 700px;">
						<ul id="paramCheckMenu">
						</ul>
					</div>
				</td>
				<td style="vertical-align:top;">
					<div style="width: 100%">
						BSC: <input id="bscStr" type="text" style="width:200px;"/>
						&nbsp;<a onclick="$('#selectBscDiv').show()">更多</a>&nbsp;&nbsp;&nbsp;&nbsp;
						日期: <s:set name="date1" value="new java.util.Date()" />
						<input id="date1" name="date1"
							value="<s:date name="date1" format="yyyy-MM-dd" />" type="text"
							onFocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" readonly class="Wdate input-text" style="width: 132px;" />
						&nbsp;&nbsp;	
						<input id="checkBtn" type="button" value="查询"/>&nbsp;&nbsp;	
						<input id="exportBtn" type="button" value="导出"/>&nbsp;&nbsp;	
						<input id="settingsBtn" type="button" value="设置"/>
						<form id="downloadEriCellAnalysisDataFileForm"
							 action="downloadEriCellAnalysisDataFileAction" method="post">
							 <input type="hidden" id="token" name="token" value=""/>
						</form>
					</div>
					<div style="margin-top: 10px;"></div>
					<div style="width: 100%">
						<div style="position: relative; z-index: 4;float:left;height: 0px;">
							<button id="left" class="btnLeft"><<</button>
							<button id="right"  class="btnRight">>></button>
						</div>
						<div id="frame" style="overflow: hidden; margin-left:72px;">
							<div id="div_tab" class="div_tab divtab_menu"
								style="width: 2000px; position: relative;">
								<ul id="paramCheckTab">
								
								</ul>
							</div>
						</div>
						<div id="paramCheckContent" class="divtab_content" style=" position: relative;">

						</div>
					</div>
				</td>
			</tr>
		</table>
		<%-- 查询设置窗口 --%>
		<div id="settingsDiv"  class="dialog2 draggable"
									style="display:none; left:25%; top: 40%;width: 350px;">
			<div class="dialog_header">
				<div class="dialog_title">查询设置</div>
				<div class="dialog_tool">
					<div class="dialog_tool_close dialog_closeBtn" onclick="$('#settingsDiv').hide();">
					</div>
				</div>
			</div>
			<div class="dialog_content" style="background:#f9f9f9;padding:10px">
				<table width="100%" class="main-table1">
					<tr>
						<td><input id="isCheckMaxChgr" type="checkbox" name="isCheckMaxChgr">启动最大信道组检查</td>
					</tr>
					<tr>
						<td><input id="isCheckBaNum" type="checkbox" name="isCheckBaNum">BA表个数:&nbsp;
							少于&nbsp;<input type="text" id="MINNUM" style="width: 50px;"/>&nbsp;&nbsp;
							或多于&nbsp;<input type="text" id="MAXNUM" style="width: 50px;"/></td>
					</tr>
					<tr>
						<td><input id="isCheckCoBsic" type="checkbox" name="isCheckCoBsic">同频同bsic检查:&nbsp;
							距离&nbsp;<input type="text" id="DISTANCE" style="width: 100px;"/></td>
					</tr>
					<tr>
						<td><input id="isCheckNcellNum" type="checkbox" name="isCheckNcellNum">邻区过多过少检查:&nbsp;
							少于&nbsp;<input type="text" id="NCELL_MINNUM" style="width: 50px;"/>&nbsp;&nbsp;
							或多于&nbsp;<input type="text" id="NCELL_MAXNUM" style="width: 50px;"/></td>
					</tr>
					<tr>
						<td align="right"><input type="button" value="确认" onclick="$('#settingsDiv').hide();"></td>
					</tr>
				</table>
			</div>
		</div>
		
		<%-- BSC选择窗口 --%>
		<div id="selectBscDiv" class="dialog2 draggable"
									style="display:none; left:25%; top: 40%;width: 690px;">
			<div class="dialog_header">
				<div class="dialog_title">选择BSC</div>
				<div class="dialog_tool">
					<div class="dialog_tool_close dialog_closeBtn" onclick="$('#selectBscDiv').hide();">
					</div>
				</div>
			</div>
			<div class="dialog_content" style="background:#f9f9f9;padding:10px">
				省：<select name="provinceId" id="provinceId" style="width:80px;">
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
				</select><br/>
				<div style="float: left;">
					<select size="10" id="defaultBsc" 
							style="width: 300px; height: 200px;" multiple="multiple" ondblclick="PutRightOneClk()">
					</select>
				</div>
				<div style="float: left; padding: 0 10 0 10;">
					<input type="button" value="选择" id="PutRightOne"
						onclick="PutRightOneClk()" /><br /><br />
					<input type="button" value="删除" id="PutLeftOne"
						onclick="PutLeftOneClk()" /><br /><br />
					<input type="button" value="确认" id="PutLeftOne"
						onclick="sumBsc()" /><br /><br />
				</div>
				<div>
					<select size="10" id="selectedBsc"
							style="width: 300px; height: 200px;" multiple="multiple" ondblclick="PutLeftOneClk()">
					</select>
				</div>
		    </div>
	    </div>
	</div>
</body>
</html>

