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

<title>小区信息管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<%@include file="commonheader.jsp" %>
<script type="text/javascript" src="js/cellimport.js"></script>
<script type="text/javascript" src="js/cellmanager.js"></script>
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
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在加载 <em class="loading_fb">小区</em>资源,请稍侯...
		</h4>
	</div>
	<div class="div_left_main" style="width: 100%">
		<div class="div_left_content">
			<%--  <div class="div_left_top">小区信息管理</div> --%>
			<div style="padding: 10px">
				<div id="frame" style="border: 1px solid #ddd">
					<div id="div_tab" class="div_tab divtab_menu">
						<ul>
							<li class="selected">小区信息查询</li>
							<li>小区信息导入</li>

						</ul>
					</div>

					<div class="divtab_content">
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
										<td  class="menuTd" style="text-align:center">BSC/NodeB</td>
										<td  class="menuTd" style="text-align:center">CELL
											</td>
										<td  class="menuTd" style="text-align:center">小区中文名
											</td>
											
											<td  class="menuTd" style="text-align:center">重要级别
											</td>
											<td class="menuTd" style="text-align:center">覆盖范围
											</td>
											<td class="menuTd" style="text-align:center">覆盖类型
											</td>
											
										</tr>
										<tr>
										
										<td style="text-align:left">
											省：<select class="areaCls"
												name="queryCell.provinceId" class="required" id="provinceId" style="width:70px">
													<%-- option value="-1">请选择</option --%>
													<s:iterator value="provinceAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select><br/> 市：<select class="areaCls" name="queryCell.cityId" class="required" id="cityId" style="width:70px">
													<s:iterator value="cityAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> <br/>区：<select class="areaCls" name="queryCell.areaId" class="required"
												id="queryCellAreaId" style="width:70px">
												   <option value="-1" selected="true">全部</option>
													<s:iterator value="countryAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select>
											</td>
											
											<td style=" text-align:left">
											<select
												id="queryCellBscId" name="queryCell.bscId"
												>
													<option value="-1">请选择</option>
											</select></td>
											<td style="text-align: left">
											<input
												type="text" name="queryCell.label"  /></td>
												<td style="width: 10%; text-align: left">
											<input
												type="text" name="queryCell.name"  /></td>
												
											<td style=" text-align: left">
											<select
												name="queryCell.importancegrade" >
													<option value="">请选择</option>
													<s:iterator value="importancegrade" id="one">
													<option value="<s:property value='one' />"><s:property value='one' /></option>
													</s:iterator>

											</select></td>	
											
											<td style="text-align: left">
											<select
												name="queryCell.coverarea" >
													<option value="">请选择</option>
													<s:iterator value="coverareas" id="one">
													<option value="<s:property value='one' />"><s:property value='one' /></option>
													</s:iterator>
											</select></td>
											<td style=" text-align: left">
											<select
												name="queryCell.coverType" >
													<option value="" selected="selected">请选择</option>
													<s:iterator value="covertypes" id="one">
													<option value="<s:property value='one' />"><s:property value='one' /></option>
													</s:iterator>
											</select></td>
											
										</tr>
										
										<tr>
											
											<td style="width: 10%; text-align: center" colspan="7">
											<input
												type="submit" onclick="" value="查  询" 
												name="search" /></td>
										</tr>
									</table>
								</form>
							</div>
							<%--查询结果  --%>
							<div style="padding-top: 10px">
								<table width="100%">
									<tr>
										<td style="width: 20%">
											<p>
												<font style="font-weight: bold">小区信息表：</font>
											</p>
										</td>


									</tr>

								</table>

							</div>
							<div style="padding-top: 10px">
								<table id="queryResultTab" class="greystyle-standard"
									width="100%">
									<th style="width: 8%">CELL</th>
									<th style="width: 10%">小区中文名</th>
									<th style="width: 8%">LAC</th>
									<th style="width: 8%">CI</th>
									<th style="width: 8%">BCCH</th>
									<th style="width: 10%">BSIC</th>
									<th style="width: 10%">TCH</th>

									<th style="width: 10%">操作</th>
									<%-- 
										<tr class="greystyle-standard-whitetr">
											<td>
												S7CDKG2
											</td>
											<td>
												东坑2
											</td>
											<td>
											</td>
											<td>
											</td>
											</td>
											<td>
											</td>
											<td>
											</td>
											<td>
											</td>
											<td>
												<a href="javascript://" onclick=""><p>
														<img src="../../images/edit-go.png" align="absmiddle"
															width="16" height="16" alt="查看/编辑明细" />
														修改
													</p> </a>
											</td>
										</tr>
--%>
								</table>
							</div>
							<div class="paging_div" style="border: 1px solid #ddd">
								<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
									条记录
								</span> <a class="paging_link page-first" title="首页"
									onclick="showListViewByPage('first')"></a> <a
									class="paging_link page-prev" title="上一页"
									onclick="showListViewByPage('back')"></a> 第 <input type="text"
									id="showCurrentPage" class="paging_input_text" value="1" /> 页/<em
									id="emTotalPageCnt">0</em>页 <a class="paging_link page-go"
									title="GO" onclick="showListViewByPage('num')">GO</a> <a
									class="paging_link page-next" title="下一页"
									onclick="showListViewByPage('next')"></a> <a
									class="paging_link page-last" title="末页"
									onclick="showListViewByPage('last')"></a>
							</div>
						</div>
						<div id="div_tab_1" style="display: none;">
							<form id="formImportCell" enctype="multipart/form-data" method="post">
								<input type="hidden" name="needPersist" value="true" /> <input
									type="hidden" name="systemConfig" value="true" />
								<div>
									<table class="main-table1 half-width">
										<tbody>
											<tr>
												<td class="menuTd">所属地市<span class="txt-impt">*</span>
												</td>
												<td style="width: 50%; font-weight: normal;" colspan="0">
													省：<select name="provinceId2" class="required"
													id="provinceId2">
														<%-- option value="-1">请选择</option --%>
														<s:iterator value="provinceAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select> 市：<select name="areaId" class="required" id="cityId2">
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
											<input type="hidden" name="fileCode" id="3"
													value="GSMCELLFILE" class="canclear required" />
											<%--
											<tr>
												<td class="menuTd">网络制式<span class="txt-impt">*</span> <br />
												</td>
												<td><input type="radio" name="fileCode" id="3"
													value="GSMCELLFILE" class="canclear required" /> <label
													for="3"> GSM </label>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<input type="radio" name="fileCode" id="32"
													value="TDCELLFILE" class="canclear  required" /> <label
													for="32"> TD </label>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<input type="radio" name="fileCode" id="32"
													value="WLANCELLFILE" class="canclear  required" /> <label
													for="32"> WLAN </label> <br /></td>
											</tr>
											 --%>
											<tr>
												<td class="menuTd">小区信息文件（EXCEL）<span class="txt-impt">*</span> <br />
												</td>
												<td style="width: 50%; font-weight: bold" colspan="0">
													<input type="file" style="width: 44%;" name="file"
													class="canclear  required" /> &nbsp; <a href="fileDownloadAction?fileName=GSM小区信息导入模板.xlsx" title="点击下载模板" id="downloadHref">GSM小区信息导入模板</a><br />
												</td>
											</tr>
											<input type="hidden" name="update" id="3"
													value="true" class="canclear  required" />
											<%--
											<tr>
												<td class="menuTd">重复记录处理方式<span class="txt-impt">*</span> <br />
												</td>
												<td><input type="radio" name="update" id="3"
													value="true" class="canclear  required" /> <label for="3">
														覆盖(更新信息) </label>
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													<input type="radio" name="update" id="32" value="false"
													class="canclear  required" /> <label for="32"> 忽略
												</label> <br /></td>
											</tr>
											 --%>
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