<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>地理场景管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%-- 	<link rel="stylesheet" type="text/css" href="css/TableLock.css"> --%>
	<%@include file="commonheader.jsp"%>
	<script type="text/javascript" src="js/rno_4g_region_scene_manage.js?v=<%=(String)session.getAttribute("version")%>"></script>
<%-- 	<script type="text/javascript" src="js/TableLock.js"></script> --%>
	<style type="text/css">
.errorTip {
	color: red;
	width: 100px;
	font-family: 华文中宋;
}

.paramTable {
	margin: auto;
	padding-left: 5px;
	width: 100%
}

.paramTable th {
	padding:0px 10px; 
	background:url(../../images/cmp-bg2.gif) repeat-x; 
	color:#444; 
	white-space:nowrap; 
	border:#CCC 1px solid; 
	line-height:30px;
	font-size: 14px; 
}

.paramTable tr td {
	border:1px solid #CCC; 
	padding:4px; 
	padding-left:6px; 
	color:#000; 
	line-height:21px; 
	vertical-align:middle; 
	background:WhiteSmoke;	
}
.paramTable  input{
    width: 100%;
}
.paramTable  div{
 	display:inline-block;
	height: 21px;
	width:100%;
	text-align:center;
}
.paramTable  span{
	text-align:center;
	font-weight:bold;
	color:red;
	display:none;
}
</style>
  <script type="text/javascript">

  </script>
  </head>
  
  <body>
	<%-- 数据加载遮罩层 --%>
	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="loadContentId"></em>,请稍侯...
		</h4>
	</div>
<h2><span class='navTitle'>当前位置：场景管理 &gt; 地理场景管理 </span></h2>
	<center>
	  	 <font style="font-weight: bold;font-size:16px">地理场景信息</font> <br>
  	</center>
  	<div id="displaySceneInfoDiv"  style="padding-top: 10px">
  	<div id="sceneAction" align="center" >
  	<div id="optionsDiv">
  	<table class="options"  border="0" cellpadding="0" cellspacing="0">
  	<tr>
  	<td><input type="button" value="刷新"	style="width: 120px;" id="flashtable" name="flashtable"  title="刷新页面的内容，未提交的修改将消失。"/></td>
<%--   	<td><input type="button" value="修改"	style="width: 90px;" id="altertable" name="altertable" /></td> --%>  	
  	<td><input type="button" value="删除"	style="width: 120px;" id="deleteScene" name="deleteScene"  title="删除当前的场景信息"/></td>
  	<td><input type="button" value="新增"	style="width: 120px;" id="addScene" name="addScene"  title="增加新场景"/></td>
  	<td><input type="button" value="提交"	style="width: 120px;" id="submitalter" name="submitalter" title="修改场景信息或增加新场景后提交更改"/></td>
  	</tr>
  	</table>
  	</div>
  	<div style="padding-top: 10px">
  	<br>
  	<table id="sceneSelect">
  	<tr id="sceneNameTr">
<%--   	<td style="margin: 0; text-align: center">场景类型：</td>
  	<td id="sceneTypeTd" style="text-align: left"><select 	name="cond['sceneTypeList]" id="sceneTypeList">
  	<option value="region" >地理场景</option>
  	<option value="time">时间场景</option>
  	</select></td> --%>
	<td style="margin: 0; text-align: center">场景名称：</td>
  	<td id="sceneNameTd"  style="text-align: left"></td>
  	<td><span id="sceneNameTip"></span></td> 
  	</tr>
  	</table>
  	<br>
  	</div>
  	</div>
  	<div  align="center" style="width: 100%;" >
<%-- 	<div    style="overflow: auto; width: 80%;height: 100%; border:solid 3px #a7a7a7 ; "> --%>
	<div style="width: 600px;height: 100%;padding-top: 10px">
  	<table class="paramTable"  id="scenceInfoTable" >
  	<caption> <font style="color: red;font-weight: bold;"></font> 的场景信息<br></caption>
  	<thead>
  	<tr>   	  	 
  	  	 <th style="width: 360px">参数名</th>
  	  	 <th style="width: 240px">参数值(点击修改)</th>
<%--   	  	 <th style="width: 40%">说明</th> --%>
	</tr>
	</thead>
  	 <tfoot></tfoot>
  	 <tbody>
  	  	 <tr>
  	  	 <td>异系统小区测量启动门限A2</td>
  	  	  <td id="INTERRATHOA2THDRSRP_td">
  	  	  <div id="INTERRATHOA2THDRSRP"  class="editbox" ></div>
  	  	  <span id="INTERRATHOA2THDRSRP_old" class="oldDataTip"></span>
  	  	  <span id="INTERRATHOA2THDRSRP_err" class="errTip"></span>
  	  	  </td>
<%--   	  	  <td></td> --%>
  	  	  </tr>
  	  	 <tr>
  	  	 <td>异系统小区测量停止门限A1</td>
  	  	 <td id="INTERRATHOA1THDRSRP_td">
  	  	 <div id="INTERRATHOA1THDRSRP"  class="editbox" ></div>
  	  	 <span id="INTERRATHOA1THDRSRP_old" class="oldDataTip"></span>
  	  	 <span id="INTERRATHOA1THDRSRP_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td>111</td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>B1事件迟滞值</td>
  	  	 <td  id="INTERRATHOUTRANB1HYST_td">
  	  	 <div id="INTERRATHOUTRANB1HYST"  class="editbox" ></div>
  	  	 <span id="INTERRATHOUTRANB1HYST_old" class="oldDataTip"></span>
  	  	 <span id="INTERRATHOUTRANB1HYST_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>B1事件异系统判决门限</td>
  	  	 <td id="INTERRATHOUTRANB1THDRSCP_td">
			<div id="INTERRATHOUTRANB1THDRSCP"  class="editbox" ></div>
  	  	 <span id="INTERRATHOUTRANB1THDRSCP_old" class="oldDataTip"></span>
  	  	 <span id="INTERRATHOUTRANB1THDRSCP_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>A1\A2事件触发时间</td>
  	  	 <td id="INTERRATHOA1A2TIMETOTRIG_td">
  	  	   	  	 <div id="INTERRATHOA1A2TIMETOTRIG"  class="editbox" ></div>
  	  	 <span id="INTERRATHOA1A2TIMETOTRIG_old" class="oldDataTip"></span>
  	  	 <span id="INTERRATHOA1A2TIMETOTRIG_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>A1、A2事件迟滞值</td>
  	  	 <td id="INTERRATHOA1A2HYST_td">
  	  	   	  	 <div id="INTERRATHOA1A2HYST"  class="editbox" ></div>
  	  	 <span id="INTERRATHOA1A2HYST_old" class="oldDataTip"></span>
  	  	 <span id="INTERRATHOA1A2HYST_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>4G往3G盲重定向</td>
  	  	 <td id="BLINDHOA1A2THDRSRP_td">
  	  	   	  	 <div id="BLINDHOA1A2THDRSRP"  class="editbox" ></div>
  	  	 <span id="BLINDHOA1A2THDRSRP_old" class="oldDataTip"></span>
  	  	 <span id="BLINDHOA1A2THDRSRP_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>事件触发持续时间</td>
  	  	 <td id="INTERFREQHOA1A2TIMETOTRIG_td">
  	  	   	  	 <div id="INTERFREQHOA1A2TIMETOTRIG"  class="editbox" ></div>
  	  	 <span id="INTERFREQHOA1A2TIMETOTRIG_old" class="oldDataTip"></span>
  	  	 <span id="INTERFREQHOA1A2TIMETOTRIG_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>异频小区测量停止门限A1</td>
  	  	 <td id="A3INTERFREQHOA1THDRSRP_td">
  	  	   	  	 <div id="A3INTERFREQHOA1THDRSRP"  class="editbox" ></div>
  	  	 <span id="A3INTERFREQHOA1THDRSRP_old" class="oldDataTip"></span>
  	  	 <span id="A3INTERFREQHOA1THDRSRP_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>异频小区测量启动门限  A2</td>
  	  	 <td id="A3INTERFREQHOA2THDRSRP_td">
  	  	   	  	 <div id="A3INTERFREQHOA2THDRSRP"  class="editbox" ></div>
  	  	 <span id="A3INTERFREQHOA2THDRSRP_old" class="oldDataTip"></span>
  	  	 <span id="A3INTERFREQHOA2THDRSRP_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>异频A3事件偏移量</td>
  	  	 <td id="INTERFREQHOA3OFFSET_td">
  	  	   	  	 <div id="INTERFREQHOA3OFFSET"  class="editbox" ></div>
  	  	 <span id="INTERFREQHOA3OFFSET_old" class="oldDataTip"></span>
  	  	 <span id="INTERFREQHOA3OFFSET_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>A1\A2事件的迟滞值</td>
  	  	 <td id="INTERFREQHOA1A2HYST_td">
  	  	   	  	 <div id="INTERFREQHOA1A2HYST"  class="editbox" ></div>
  	  	 <span id="INTERFREQHOA1A2HYST_old" class="oldDataTip"></span>
  	  	 <span id="INTERFREQHOA1A2HYST_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>最低接入电平</td>
  	  	 <td id="QRXLEVMIN_td">
  	  	  <div id="QRXLEVMIN"  class="editbox" ></div>
  	  	 <span id="QRXLEVMIN_old" class="oldDataTip"></span>
  	  	 <span id="QRXLEVMIN_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>频异系统启动测门限</td>
  	  	 <td id="SNONINTRASEARCH_td">
  	  	   	  	 <div id="SNONINTRASEARCH"  class="editbox" ></div>
  	  	 <span id="SNONINTRASEARCH_old" class="oldDataTip"></span>
  	  	 <span id="SNONINTRASEARCH_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>判决门限</td>
  	  	 <td id="THRSHSERVLOW_td">
  	  	   	  	 <div id="THRSHSERVLOW"  class="editbox" ></div>
  	  	 <span id="THRSHSERVLOW_old" class="oldDataTip"></span>
  	  	 <span id="THRSHSERVLOW_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td ></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>异系统重选迟滞时间</td>
  	  	 <td id="TRESELEUTRAN_td">
  	  	   	  	 <div id="TRESELEUTRAN"  class="editbox" ></div>
  	  	 <span id="TRESELEUTRAN_old" class="oldDataTip"></span>
  	  	 <span id="TRESELEUTRAN_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 <tr>
  	  	 <td>小区重选优先级</td>
  	  	 <td id="CELLRESELPRIORITY_td">
  	  	   	  	 <div id="CELLRESELPRIORITY"  class="editbox" ></div>
  	  	 <span id="CELLRESELPRIORITY_old" class="oldDataTip"></span>
  	  	 <span id="CELLRESELPRIORITY_err" class="errTip"></span>
  	  	 </td>
<%--   	  	 <td></td> --%>
  	  	 </tr>
  	  	 </tbody>
  	  	 </table>
  	  	 <br><br><br><br><br>
  	  	 </div>
  	  	 </div>
  	  	 </div>
  	</body>
  	</html>