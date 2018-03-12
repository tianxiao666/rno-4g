<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

<title>BSC信息管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<%@include file="commonheader.jsp" %>
<script type="text/javascript" src="js/rno_bsc_import.js"></script>
<script type="text/javascript" src="js/rno_bsc_manager.js"></script>
<script type="text/javascript">
	var $ = jQuery.noConflict();
	$(function() {
		//tab选项卡
		tab("div_tab", "li", "onclick");//项目服务范围类别切换

	})
	
</script>
<style type="text/css">
select.areaCls{
 width:90px;
}

</style>
</head>

<body >
	<%-- 遮罩层 --%>
	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="loadContentId"></em>,请稍侯...
		</h4>
	</div>
	<div class="div_left_main" style="width: 100%">
		<div class="div_left_content">
			<%--  <div class="div_left_top">小区信息管理</div> --%>
			<div style="padding: 10px">
				<div id="frame" style="border: 1px solid #ddd">
					<div id="div_tab" class="div_tab divtab_menu">
						<ul>
							<li class="selected">BSC信息查询</li>
							<li>BSC信息导入</li>
						</ul>
					</div>

					<div class="divtab_content">
						<%-- BSC信息查询 --%>
						<div id="div_tab_0">
							<div>
								<form id="conditionForm" method="post">
									<input type="hidden" id="hiddenPageSize" name="page.pageSize"
										value="25" /> <input type="hidden" id="hiddenCurrentPage"
										name="page.currentPage" value="1" /> <input type="hidden"
										id="hiddenTotalPageCnt" name="page.totalPageCnt" value="-1"/> <input type="hidden"
										id="hiddenTotalCnt" name="page.totalCnt" value="-1"/>
									<table class="main-table1 half-width"
										style="width: 100%; padding-top: 10px">
										
										<tr>
											<td class="menuTd" style="text-align:center">区域</td>
											<td class="menuTd" style="text-align:center">BSC</td>
											<td class="menuTd" style="text-align:center">厂家</td>		
										</tr>
										<tr>
											<td style="text-align:left">
												省：<select class="areaCls"
													name="bscQuery.provinceId" class="required" id="provinceId" style="width:70px">
														<%-- option value="-1">请选择</option --%>
														<s:iterator value="provinceAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select>
												市：<select class="areaCls" name="bscQuery.cityId" class="required" id="cityId" style="width:70px">
														<s:iterator value="cityAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select> 
											<%--	区：<select class="areaCls" name="bscQuery.areaId" class="required"
													id="queryCellAreaId" style="width:70px">
													   <option value="-1" selected="true">全部</option>
														<s:iterator value="countryAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select> --%>
												</td>
												<td style="text-align: left">
												<input
													type="text" name="bscQuery.bscEnName"  /></td>
												<td style="width: 10%; text-align: left">
												<input
													type="text" name="bscQuery.manufacturers"  /></td>
										</tr>			
										<tr>
											<td style="width: 10%; text-align: center" colspan="3">
											<input type="submit" onclick="" value="查  询" name="search" /></td>
										</tr>
									</table>
								</form>
							</div>
							<%--查询结果  --%>
							<div style="padding-top: 10px">
								<table width="100%">
									<tr>
										<td style="width: 10%">
											<p>
												<font style="font-weight: bold">BSC信息表：</font>
											</p>
										</td>
										<td style="">
											<input type="button" id="openAddWin" value="新增" onclick="$('#addSingleBscDiv').show();"/> 
										</td>
									</tr>
								</table>
							</div>
							<div style="padding-top: 10px">
								<table id="queryResultTab" class="greystyle-standard"
									width="100%">
									<th style="width: 8%">区域</th>
									<th style="width: 8%">BSC</th>
									<th style="width: 8%">厂家</th>
									<th style="width: 10%">操作</th>
								</table>
							</div>
							<div class="paging_div" style="border: 1px solid #ddd">
								<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
									条记录
								</span>
									<a class="paging_link page-first" title="首页"
										onclick="showListViewByPage('first')"></a>
									<a class="paging_link page-prev" title="上一页"
										onclick="showListViewByPage('back')"></a> 第
									<input type="text" id="showCurrentPage"
										class="paging_input_text" value="1" />
									页/
									<em id="emTotalPageCnt">0</em>页
									<a class="paging_link page-go" title="GO"
										onclick="showListViewByPage('num')">GO</a>
									<a class="paging_link page-next" title="下一页"
										onclick="showListViewByPage('next')"></a>
									<a class="paging_link page-last" title="末页"
										onclick="showListViewByPage('last')"></a>
							</div>
							
							<%-- 新增单个BSC --%>
							<div id="addSingleBscDiv" class="dialog2 draggable"
									style="display:none; left: 35%; top: 50%;">
									<div class="dialog_header">
										<div class="dialog_title">新增</div>
										<div class="dialog_tool">
											<div class="dialog_tool_close dialog_closeBtn"
												onclick="$('#addSingleBscDiv').hide();"></div>
										</div>
									</div>
									<div class="dialog_content" style="background:#f9f9f9;padding:10px">
										<form id="addSingleBscForm">
										<table class="main-table1 half-width">
											<tr>
												<td >
												省：<select class="areaCls"
															name="bscQuery.provinceId" class="required" id="provinceId3" style="width:70px">
														<%-- option value="-1">请选择</option --%>
														<s:iterator value="provinceAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
										 				</s:iterator>
													</select>
												市：<select class="areaCls" name="bscQuery.cityId" class="required" id="cityId3" style="width:70px">
														<s:iterator value="cityAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select> 
												</td>
											</tr>
											<tr>
												<td>BSC：<input type="text" id="bscEngName" /></td>
											</tr>
											<tr>
												<td>厂家：
												<select id="manufacturers">
													<option value="1">爱立信</option>
													<option value="2">华为</option>
												</select>
												</td>
											</tr>
											<tr>
												<td class="menuTd">
												<input type="button" id="addBscBtn" value="确定" />
												</td>
											</tr>
										</table>	
										</form>
								    </div>
							</div>

						</div>
						<%-- BSC信息导入 --%>
						<div id="div_tab_1" style="display: none;">
							<form id="formImportBsc" enctype="multipart/form-data" method="post">
								<input type="hidden" name="needPersist" value="true" /> 
								<input type="hidden" name="systemConfig" value="true" />
								<input type="hidden" name="fileCode" id="3"
										value="BSCEXCELFILE" class="canclear required" />
								<div>
									<table class="main-table1 half-width">
										<tbody>
											<tr>
												<td class="menuTd">所属地市<span class="txt-impt">*</span>
												</td>
												<td style="width: 50%; font-weight: normal;" colspan="0">
												省：<select name="provinceId2" class="required" id="provinceId2">
														<%-- option value="-1">请选择</option --%>
														<s:iterator value="provinceAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select> 
												市：<select name="areaId" class="required" id="cityId2">
														<s:iterator value="cityAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select> 
												<%--
												区：<select name="areaId" class="required" id="areaId2">
														<s:iterator value="countryAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select>
												 --%>
												</td>
											</tr>
											<tr>
												<td class="menuTd">BSC信息文件（EXCEL）<span class="txt-impt">*</span> <br />
												</td>
												<td style="width: 50%; font-weight: bold" colspan="0">
													<input type="file" style="width: 44%;" name="file"
													class="canclear  required" /> &nbsp; <a href="javascript:void(0);" title="点击下载模板" id="downloadHref">&nbsp;</a><br />
												</td>
											</tr>
											<input type="hidden" name="update" id="3"
													value="true" class="canclear  required" />
												<tr>
													<td class="menuTd">
														导入模式
														<span class="txt-impt">*</span>
														<br />
													</td>
													<td>
														<input type="radio" name="attachParams['mode']" id="3" value="delete"
															class="canclear  required" checked="checked"/>
														<label for="3">
															全覆盖
														</label>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														<input type="radio" name="attachParams['mode']" id="32" value="overwrite"
															class="canclear  required" />
														<label for="32">
															增量
														</label>
														<br />
														全量覆盖：对于系统中有，而导入文件没有的BSC，将删除系统中已有的BSC。<br/>
														增量导入：对于系统中有，而导入文件没有的BSC，系统不作处理。
													</td>
												</tr>
											</tbody>
									</table>
								</div>
								<div class="container-bottom" style="padding-top: 10px">
									<table style="width: 60%; margin: auto" align="center">
										<tr>
											<td><input type="button" value="导    入"
												style="width: 90px;" id="importBtn" name="import" /> <br />
											</td>
										</tr>
									</table>
									<div id="importResultDiv" class="container-bottom"
										style="padding-top: 10px"></div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>