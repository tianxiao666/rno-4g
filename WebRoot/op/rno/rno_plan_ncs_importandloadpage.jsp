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

<title>NCS数据加载</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp" %>
<script type="text/javascript" src="js/rno_plan_ncs_importandload.js"></script>
<style type="text/css">
.greystyle-standard-noborder tr td {
	padding: 0
}
</style>
</head>

<script type="text/javascript">
	var $ = jQuery.noConflict();
	$(function() {
		//tab选项卡
		tab("div_tab", "li", "onclick");//项目服务范围类别切换
		//触发刷新按钮
		$("#refreshLoadedBtn").click();
	})
</script>

<body>
<body>
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在加载 <em class="loading_fb" id="tipcontentId"></em>,请稍侯...
		</h4>
	</div>
	<div class="div_left_main" style="width: 100%">
		<div class="div_left_content">
			<%--  <div class="div_left_top">NCS数据管理</div> --%>
			<div style="padding: 10px">
				<div id="frame"
					style="border: 1px solid #ddd;overflow: hidden;float: left;width: 60%">
					<div id="div_tab" class="div_tab divtab_menu">
						<ul>
							<li class="selected">NCS数据导入</li>
							<li>从系统中加载</li>
							<%-- li id="loadedLi">已加载列表</li --%>
						</ul>
					</div>

					<div class="divtab_content">
						<div id="div_tab_0" style="display: block;">
							<form id="formImportNcs" enctype="multipart/form-data" method="post">
								<input type="hidden" name="needPersist" value="true" /> <input
									type="hidden" name="systemConfig" value="true" /> <input
									type="hidden" name="fileCode" value="GSMNCSEXCELFILE"
									class="canclear" /> <input type="hidden" id="autoload"
									name="autoload" value="false" />
								<div>
									<table class="main-table1 half-width">
										<tbody>
											<tr>

												<td  class="menuTd">名称<span class="txt-impt">*</span>
												</td>
												<td><input type="text" name="attachParams['name']"
													class="required" /></td>
											</tr>
											<tr>
												<td  class="menuTd">开始测量时间<span class="txt-impt">*</span>
												</td>
												<td><s:set name="todayDay" value="new java.util.Date()" />
													<input name="attachParams['startTime']" class="required"
													value="<s:date name="todayDay" format="yyyy-MM-dd HH:mm:ss" />"
													type="text"
													onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
													readonly class="Wdate required input-text"
													style="width: 132px;" /> &nbsp;&nbsp;&nbsp;&nbsp;</br> </td>
											</tr>
											<tr>
												<td  class="menuTd">记录时长(RECTIME)<span class="txt-impt">*</span>
												</td>
												<td><input type="text" name="attachParams['rectime']"
													class="required digits" />分钟&nbsp;&nbsp;&nbsp;&nbsp;<br /></td>
											</tr>
											<tr>
												<td  class="menuTd">记录时间间隔(SEGTIME)<span class="txt-impt">*</span>
												</td>
												<td><input type="text" name="attachParams['segtime']"
													class="required digits" />分钟</td>
											</tr>




											<tr>
												<td class="menuTd">所属地市<span class="txt-impt">*</span>
												</td>
												<td style="width: 50%; font-weight: normal;" colspan="0">
													省：<select name="provinceId" class="required"
													id="provinceId1">
														<%-- option value="-1">请选择</option --%>
														<s:iterator value="provinceAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select> 市：<select name="cityId" class="required" id="cityId1">
														<s:iterator value="cityAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select> 区：<select name="areaId" class="required" id="areaId1">
														<s:iterator value="countryAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select>
												</td>
											</tr>

											<tr>
												<td class="menuTd">NCS测量文件（EXCEL）<span class="txt-impt">*</span>
												</td>
												<td style="width: 50%; font-weight: bold" colspan="0">
													<input type="file" style="width: 44%;" name="file"
													class="canclear required" /> &nbsp;<a
													href="fileDownloadAction?fileName=ncs导入模板.xlsx"
													title="点击下载模板" id="downloadHref">ncs导入模板</a> <br />

												</td>
											</tr>

										</tbody>


									</table>
								</div>
								<div class="container-bottom" style="padding-top: 10px">
									<table style="width: 60%; margin: auto" align="center">
										<tr>
											<td><input type="button" value="导    入  并  加  载"
												style="width: 110px;" id="importAndLoadBtn"
												name="importandload" /> <input type="button" value="导    入"
												style="width: 90px;" id="importBtn" name="import" /> <br />
												<br /></td>
										</tr>
									</table>
									<div id="loadlistDiv" style="display:none">
										<table class="tb-transparent-standard" style="width:100%"
											id="loadlistTable1">
											<tr>
												<th>名称</th>
												<th>开始测量时间</th>
											</tr>
										</table>
									</div>
									<div id="importResultDiv" class="container-bottom"
										style="padding-top: 10px"></div>
								</div>
							</form>
						</div>
						<div id="div_tab_1" style="display: none;">
							<form id="conditionForm" method="post">
								<input type="hidden" id="hiddenPageSize" name="page.pageSize"
									value="25" /> <input type="hidden" id="hiddenCurrentPage"
									name="page.currentPage" value="1" /> <input type="hidden"
									id="hiddenTotalPageCnt" /> <input type="hidden"
									id="hiddenTotalCnt" />
								<div>
									<%-- class="greystyle-standard-noborder" --%>
									<table class="main-table1 half-width"
										style="width: 100%; padding-top: 10px">
										<tr>
											<td style="text-align: right;width: 15%" class="menuTd"><span
												style="padding-top: 0px">区域：</span></td>
											<td style="text-align: left;">省：<select
												name="provinceId" class="required" id="provinceId2">
													<%-- option value="-1">请选择</option --%>
													<s:iterator value="provinceAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> 市：<select name="cityId" class="required" id="cityId2">
													<s:iterator value="cityAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> 区：<select name="attachParams['AREA_ID']" class="required"
												id="areaId2">
													<s:iterator value="countryAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select>
											</td>
										</tr>
										<tr>
											<td style="text-align: right" class="menuTd" rowspan="4">NCS描述信息：</td>
											<td style="text-align: left">NCS名称:<input type="text"
												name="attachParams['NAME']" /> 
												
											</td>
										</tr>
										<tr><td>NCS收集时间： 从 <s:set
													name="begtime" value="new java.util.Date()" /> <input
												name="attachParams['BEGTIME']"
												value="<s:date name="BEGTIME" format="yyyy-MM-dd HH:mm:ss" />"
												type="text"
												onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
												readonly class="Wdate input-text" style="width: 132px;" />
												至 <s:set name="endtime" value="new java.util.Date()" /> <input
												name="attachParams['ENDTIME']"
												value="<s:date name="ENDTIME" format="yyyy-MM-dd HH:mm:ss" />"
												type="text"
												onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
												readonly class="Wdate input-text" style="width: 132px;" /></td></tr>
										<tr><td><span style="display:block;margin-top:6px">
													记录时长(RECTIME)： <input type="text"
													name="attachParams['RECTIME']" class="digits" />分钟
											</span></td></tr>
										<tr><td>记录时间间隔(SEGTIME)： <input type="text"
													name="attachParams['SEGTIME']" class="digits" />分钟</td></tr>
										<tr>

											<td style=" text-align: right" colspan="10"><input
												type="submit" onclick="" value="查  询" style="width: 90px;"
												name="search" /></td>
										</tr>
									</table>
								</div>
							</form>
							<%--查询结果  --%>
							<div style="padding-top: 10px">
								<table width="100%">
									<tr>
										<td style="width: 20%">
											<p>
												<font style="font-weight: bold">NCS列表</font>
											</p>
											<span style="display:block;margin-top:4px"><input
											type="button" id="addToAnalysis" value="添加到分析列表"
											style="width: 90px;" name="search" /></span>
										</td>
										<td style="width: 15%; text-align: right"></td>

									</tr>

								</table>

							</div>
							<div style="padding-top: 0px">
								<table id="queryResultTab" class="greystyle-standard"
									width="100%">
									<th>名称</th>
									<th>开始测量时间</th>
									<th>记录的时间间隔</th>
									<th>记录时长（分钟）</th>
									<th>网络制式</th>
									<th>厂家</th>

									<th style="width: 10%">全选 <input type="checkbox"
										onclick="javascript:operAllCheckbox(this);" name="1" id="1" />
										<label for="1"></label>
									</th>
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


					</div>
				</div>
				<div
					style="float: left;margin-left: 4px;width: 38%;border: 1px solid #ddd">
					<div style="padding-top: 2px">
						<table width="100%">
							<tr>
								<td style="width: 20%">
									<p>
										<font style="font-weight: bold">加载到分析列表的NCS</font>
									</p> <span style="display: block;margin-top: 2px"><input
										type="button" id="refreshLoadedBtn" onclick="" value="刷新"
										style="width: 90px;" name="search" /></span>
								</td>
								<td style="width: 15%; text-align: right"><input
									type="button" id="removeFromAnalysis" value="从分析列表删除"
									style="width: 90px;" name="search" /></td>

							</tr>

						</table>

					</div>
					<div style="padding-top: 2px">
						<table id="queryResultTab2" class="greystyle-standard"
							width="100%">
							<th>名称</th>
							<th>开始测量时间</th>
							<th style="width: 10%">全选 <input type="checkbox"
								onclick="javascript:operAllCheckbox(this,2);" name="selectall"
								id="2" /> <label for="1"></label>
							</th>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="operInfo"
		style="display:none; top:40px;left:600px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
		<table height="100%" width="100%" style="text-align:center">
			<tr>
				<td><span id="operTip"></span></td>
			</tr>
		</table>

	</div>
</body>
</html>