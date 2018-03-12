<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>干扰矩阵管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>
<script type="text/javascript" src="js/rno_interfer_martix_manage.js?v=<%=(String)session.getAttribute("version")%>"></script>

  </head>
  <input type="hidden" id="cityIdFromRes" value="<s:property value='cityId'/>"/>
  <body>
	<%-- 数据加载遮罩层 --%>
	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="loadContentId"></em>,请稍侯...
		</h4>
	</div>
	
	<font style="font-weight: bold;">当前位置： 资源管理 > 干扰矩阵管理</font>
	<br>
	
  	<%-- 干扰矩阵查询条件 --%>
    <div style="width: 80%;margin-top: 20px; margin-left: 6%;">
	    <form id="interferMartixForm" method="post">
			<input type="hidden" id="hiddenPageSize" name="page.pageSize" value="25" /> 
			<input type="hidden" id="hiddenCurrentPage" name="page.currentPage" value="1" /> 
			<input type="hidden" id="hiddenTotalPageCnt" /> 
			<input type="hidden" id="hiddenTotalCnt" />
			
		    <table class="main-table1 half-width" style="padding-top: 10px;">
					<tr>
						<td style="text-align: left">
						省：<select name="provinceId"
							class="required" id="provinceId2">
								<%-- option value="-1">请选择</option --%>
								<s:iterator value="provinceAreas" id="onearea">
									<option value="<s:property value='#onearea.area_id' />">
										<s:property value="#onearea.name" />
									</option>
								</s:iterator>
						</select>
						市：<select name="cond['cityId']" class="required"
							id="cityId2">
								<s:iterator value="cityAreas" id="onearea">
									<option value="<s:property value='#onearea.area_id' />">
										<s:property value="#onearea.name" />
									</option>
								</s:iterator>
						</select>
						</td>
						<td style="text-align: left">类型：<select
							name="cond['interMartixType']" id="facturer">
								<option value='ALL'>全部</option>
								<option value='NCS'>ncs干扰矩阵</option>
								<option value='FORTE'>forte干扰矩阵</option>
						</select></td>
	
						<td style="text-align: left">干扰矩阵创建时间：
							<s:set name="begtime" value="new java.util.Date()" /> 
							<input id="beginTime" name="cond['begTime']"
								value="<s:date name="begtime" format="yyyy-MM-01 00:00:00" />"
								type="text"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){latestAllowedTime.focus();},maxDate:'#F{$dp.$D(\'latestAllowedTime\')}'})"
								readonly class="Wdate input-text" style="width: 132px;" />
							到 
							<s:set name="endtime" value="new java.util.Date()" />
							<input id="latestAllowedTime" name="cond['endTime']"
								value="<s:date name="endtime" format="yyyy-MM-dd HH:mm:ss" />"
								type="text"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\')}'})"
								readonly class="Wdate input-text" style="width: 132px;" />
						</td>
					</tr>
					<tr>
						<td colspan="3" style=" text-align: left;">
							<div style="margin: 11px"></div>
							<input id="searchInterMartix" type="button" name="search"
							 style="width: 90px;" value="查 询" onclick="">
						</td>
					</tr>
			</table>
		</form>
	</div>
	
	<div style="width: 80%;margin-left: 6%;padding-top: 10px">
		<table width="50%">
			<tbody>
				<tr>
					<td style="width: 5%">
						<p><font style="font-weight: bold">干扰矩阵列表</font></p>
					</td>
					<td style="width: 2%">
						<input id="addInterMartix" type="button" name="addInterMartix"
						 style="width: 90px;" value="新计算" onclick="">
					</td>
					<td style="width: 10%">
						<input id="importForteMartix" type="button" name="importForteMartix"
						 style="width: 90px;" value="forte矩阵导入" onclick="">
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<%-- 干扰矩阵详情列表 --%>
	<div style="width: 80%;margin-left: 6%;padding-top: 10px">
		<table id="interferMartixTable" class="greystyle-standard" width="80%">
			<tr>
				<th style="width: 8%">地市</th>
				<th style="width: 8%">创建时间</th>
				<th style="width: 8%">使用的NCS测量时间</th>
				<th style="width: 8%">数据量</th>
				<th style="width: 8%">类型</th>
				<th style="width: 8%">状态</th>
			</tr>
		</table>
	</div>
	<div class="paging_div" id="interMartixPageDiv"
		style="border: 1px solid #ddd; width: 80%;margin-left: 6%;">
		<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
			条记录
		</span> <a class="paging_link page-first" title="首页"
			onclick="showListViewByPage('first',sumbitInterferMartixForm,'interferMartixForm','interMartixPageDiv')"></a>
		<a class="paging_link page-prev" title="上一页"
			onclick="showListViewByPage('back',sumbitInterferMartixForm,'interferMartixForm','interMartixPageDiv')"></a>
		第 <input type="text" id="showCurrentPage" class="paging_input_text"
			value="0" /> 页/<em id="emTotalPageCnt">0</em>页 <a
			class="paging_link page-go" title="GO"
			onclick="showListViewByPage('num',sumbitInterferMartixForm,'interferMartixForm','interMartixPageDiv')">GO</a>
		<a class="paging_link page-next" title="下一页"
			onclick="showListViewByPage('next',sumbitInterferMartixForm,'interferMartixForm','interMartixPageDiv')"></a>
		<a class="paging_link page-last" title="末页"
			onclick="showListViewByPage('last',sumbitInterferMartixForm,'interferMartixForm','interMartixPageDiv')"></a>
	</div>
  </body>
</html>
