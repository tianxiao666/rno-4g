<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>RNO页面链接列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>

  </head>
  
  <body>
  	<h2>RNO页面链接列表</h2>
    <a href="/ops/op/rno/initCellManagerPageAction" target="_blank">初始化小区管理页面</a></br>
    <a href="/ops/op/rno/initTrafficStaticsImportPageAction" target="_blank">初始化话统计指标导入页面</a></br>
    <a href="/ops/op/rno/initModifyCellPageAction" target="_blank">修改小区页面</a></br>
    <a href="/ops/op/rno/initMapSpotDataImportPageAction" target="_blank">初始化小区区域导入经纬度信息页面</a></br>
    <a href="/ops/op/rno/initGisCellDisplayAction" target="_blank">初始化地图展现数据</a></br>
    <a href="/ops/op/rno/initRnoMapSearchAction" target="_blank">进入地图操作页面</a></br>
    <a href="/ops/op/rno/initRnoStsManagerPageAction" target="_blank">初始化话务性能查询页面</a></br>
    <%-- a href="/ops/op/rno/initLoadTeleTrafficCapabilityPageAction" target="_blank">初始化加载话务性能页面</a></br --%>
    <a href="/ops/op/rno/initRnoTrafficStaticsPageAction" target="_blank">初始化话务专题统计页面</a></br>
    <a href="/ops/op/rno/initHeavyLoadCellAnalyPageAction" target="_blank">初始化忙小区邻区分析页面</a></br>
    <a href="/ops/op/rno/initInterferenceAnalysisPageAction" target="_blank">初始化总干扰分析页面</a></br>
    <a href="/ops/op/rno/initCellLoadConfigureImportPageAction" target="_blank">初始化小区加载配置数据导入页面</a></br>
    <a href="/ops/op/rno/initRnoFreqReusePageAction" target="_blank">初始化频率复用分析页面</a></br>
    <a href="/ops/op/rno/initPlandesignInterferenceDataImportPageAction" target="_blank">初始化干扰数据导入和加载</a></br>
    <a href="/ops/op/rno/initCoBsicAnalyseAction" target="_blank">初始化Cobsic小区地图展现数据</a></br>
    <a href="/ops/op/rno/initChannelSwitchStatisticsAction" target="_blank">初始化小区切换统计指标页面</a></br>
    <a href="/ops/op/rno/initNcsImportAndLoadPageAction" target="_blank">ncs导入加载功能</a></br>
    <a href="/ops/op/rno/initCellStructImportAndLoadPageAction" target="_blank">初始化小区结构数据导入页面</a></br>
    <a href="/ops/op/rno/initPlanNcellAnalysisPageAction" target="_blank">进入邻区分析的页面</a></br>
    
  </body>
</html>
