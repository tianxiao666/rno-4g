<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<link rel="stylesheet" type="text/css" href="../../css/base.css">
<link rel="stylesheet" type="text/css" href="../../css/input.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css">
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-div-standard.css">
<link rel="stylesheet" type="text/css" href="../../css/public-table-standard.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-tb-std.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-span-std.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="../../op/rno/css/source.css" />
<link rel="stylesheet" type="text/css" href="../../op/rno/css/loading_cover.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/dialog/dialog.css"/>
<link rel="stylesheet" type="text/css" href="../../op/rno/css/jquery-ui-1.10.3.custom.min.css" />
<style>
.main-table1 .menuTd{
text-align:right;
}
.txt-err, .txt-impt {
color: #C00;
font-size: 14px;
}

/* 采样点的样式 */
.singleWrapCls {
	width: 5px;
	height: 5px;
	border:1px solid #A9A9F5;
	position: absolute;
}
.multiWrapCls {
	width: 5px;
	height: 5px;
	border:1px solid #FF0040;
	position: absolute;
}


/* 导入面板容器的样式 */
.loadframe{
    height:auto!important;  
    height:100px;  
    min-height:100px; 
}

/* 提示样式 */
span.operTip{
 font:bold normal 900 15pt 黑体;
}
</style>


<script type="text/javascript"	src="../../op/rno/jslib/jquery-1.8.2.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="../../op/rno/jslib/jquery-ui-1.10.3.custom.min.js"></script>

<%-- <script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=85E6658a58abb433b32f6505997a0c25"></script> --%>

<script type="text/javascript" src="../../op/js/tab.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js "></script>
<script type="text/javascript" src="../../op/rno/jslib/layer/layer.min.js "></script>
<script type="text/javascript" src="../../op/rno/js/selftools.js?v=<%=(String)session.getAttribute("version")%>"></script>
<script type="text/javascript" src="../../op/rno/js/rendererrule.js"></script>

<%-- var str='<%=session.getAttribute("mapId") %>';	 --%>
	
<%-- s:property value="#session.mapId"/ --%>

<%-- <s:if test="#session.mapId==null || #session.mapId=='baidu'">
	<script type="text/javascript" src="../../op/rno/jslib/libgiscelldisplay.js?v=<%=(String)session.getAttribute("version")%>"></script>
	<script type="text/javascript" src="../../op/rno/js/cellRelaModel.js?v=<%=(String)session.getAttribute("version")%>"></script>
	<script type="text/javascript" src="../../op/rno/jslib/lib_gis_dt_sample_display.js"></script>
</s:if>
<s:else>
	<script language="javascript" type="text/javascript" src="http://www.google.com/jsapi"></script>
	<script type="text/javascript" src="../../op/rno/jslib/gelibgiscelldisplay.js?v=<%=(String)session.getAttribute("version")%>"></script>
	<script type="text/javascript" src="../../op/rno/js/geCellRelaModel.js?v=<%=(String)session.getAttribute("version")%>"></script>
	<script type="text/javascript" src="../../op/rno/jslib/gelib_gis_dt_sample_display.js"></script>
</s:else> --%>
 
<%-- ge
<script language="javascript" type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript" src="jslib/gelibgiscelldisplay.js?v=<%=(String)session.getAttribute("version")%>"></script>
<script type="text/javascript" src="js/geCellRelaModel.js?v=<%=(String)session.getAttribute("version")%>"></script>
<script type="text/javascript" src="jslib/gelib_gis_dt_sample_display.js"></script>
--%>
<%-- osm 
<link rel="stylesheet" type="text/css"  href="jslib/leaflet/leaflet.css" />
<script type="text/javascript" src="jslib/leaflet/leaflet.js"></script>
<script type="text/javascript" src="jslib/osmleafletlibgiscelldisplay.js"></script>
<script type="text/javascript" src="js/osmleafletCellRelaModel.js"></script>
--%>

<script type="text/javascript" src="../../op/rno/jslib/libareacascade.js"></script>
<script type="text/javascript" src="../../op/rno/jslib/libfileupload.js"></script>

<script type="text/javascript" src="../../op/rno/js/commontimedtask.js?v=<%=(String)session.getAttribute("version")%>"></script>
<script type="text/javascript">
$(document).ready(function(){
	$(".draggable").draggable();
	/*if($("#trigger").length!=0){
		$("#trigger").remove();
	}*/
	$("#trigger").css("display","none");
	
});
</script>
