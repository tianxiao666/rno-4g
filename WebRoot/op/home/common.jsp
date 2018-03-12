<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<base href="<%=basePath%>">

<%-- ###ExtJS#begin## --%>
		<%-- use the extjs lib of old home, new will get some errors --%>
<script type="text/javascript" src="op/home/resource/ext-base.js"></script>
<script type="text/javascript" src="op/home/resource/ext-all.gzjs"></script>
		<%--  --%>
		
<script type="text/javascript" src="jslib/extjs/ext-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css" href="jslib/extjs/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css" href="jslib/extjs/resources/css/ext-patch.css">
<link rel="stylesheet" type="text/css" href="jslib/extjs/resources/shared/icons/silk.css">
<link rel="stylesheet" type="text/css" href="jslib/extjs/plugin/ux/css/RowEditor.css">
<%-- ###ExtJS#end## --%>


<script type="text/javascript" src="jslib/extjs/plugin/ux/RowEditor.js"></script>

<script type="text/javascript" src="jslib/extjs/plugin/ux/CheckColumn.js"></script>
<script type="text/javascript" src="jslib/extjs/plugin/shared/examples.js"></script>

<link rel="stylesheet" type="text/css" href="jslib/extjs/plugin/ux/css/Spinner.css">
<script type="text/javascript" src="jslib/extjs/plugin/ux/Spinner.js"></script>
<script type="text/javascript" src="jslib/extjs/plugin/ux/SpinnerField.js"></script>

