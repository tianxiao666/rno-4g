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

<title>小区结构指标数据加载</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>
<script type="text/javascript"
	src="js/rno_plan_cellstruct_importandload.js"></script>
</head>

<script type="text/javascript">
	var $ = jQuery.noConflict();
	$(function() {
		//tab选项卡
		tab("div_tab", "li", "onclick");//项目服务范围类别切换

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
			<%-- <div class="div_left_top">小区结构指标数据管理</div> --%>
			<div style="padding: 10px">
				<div id="frame" class="loadframe"
					style="border: 1px solid #ddd;width:50%;float:left">
					<div id="div_tab" class="div_tab divtab_menu">
						<ul style="width:100%">
							<li class="selected" style="width:12%">指标数据导入</li>
							<li style="width:12%">从系统中加载</li>

						</ul>
					</div>

					<div class="divtab_content">
						<div id="div_tab_0" style="display: block;">
							<form id="formImportCellStruct" enctype="multipart/form-data" method="post">
								<input type="hidden" name="needPersist" value="true" /> <input
									type="hidden" name="systemConfig" value="true" /> <input
									type="hidden" name="fileCode" value="GSMCELLSTRUCTEXCELFILE"
									class="canclear" /> <input type="hidden" id="autoload"
									name="autoload" value="false" />
								<div>
									<table class="main-table1 half-width">
										<tbody>
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
												</select><br />市：<select name="cityId" class="required" id="cityId1">
														<s:iterator value="cityAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select> <br />区：<select name="areaId" class="required"
													id="areaId1">
														<s:iterator value="countryAreas" id="onearea">
															<option value="<s:property value='#onearea.area_id' />">
																<s:property value="#onearea.name" />
															</option>
														</s:iterator>
												</select>
												</td>
											</tr>
											<tr>
												<td class="menuTd">文件名称<span class="txt-impt">*</span>
												</td>
												<td><input type="text" name="attachParams['name']"
													class="required" /></td>
											</tr>
											<tr>
												<td class="menuTd">小区结构指标文件（EXCEL）<span
													class="txt-impt">*</span>
												</td>
												<td style="width: 50%; font-weight: bold" colspan="0">
													<input type="file" style="width: 44%;" name="file"
													class="canclear required" /> &nbsp;<a
													href="fileDownloadAction?fileName=小区结构指标导入模板.xlsx"
													title="点击下载模板" id="downloadHref">小区结构指标导入模板</a> <br />

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
									<div id="loadlistDiv">
										<table class="tb-transparent-standard" style="width:100%"
											id="loadlistTable1">
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
									<table class="main-table1 half-width"
										style="width: 100%; padding-top: 10px">
										<tr>
											<td class="menuTd" style="text-align:center"><span
												style="padding-top: 0px">区域</span></td>

											</td>
											<td class="menuTd" style="text-align:center">指标统计时间</td>

											<td class="menuTd" style="text-align:center">文件名称</td>

										</tr>
										<tr>
											<td style="text-align: left">省：<select name="provinceId"
												class="required" id="provinceId2">
													<%-- option value="-1">请选择</option --%>
													<s:iterator value="provinceAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> <br />市：<select name="cityId" class="required" id="cityId2">
													<s:iterator value="cityAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> <br />区：<select name="attachParams['AREA_ID']"
												class="required" id="areaId2">
													<s:iterator value="countryAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select>
											<td style="text-align: left">从 <s:set name="begtime"
													value="new java.util.Date()" /> <input
												name="attachParams['BEGTIME']"
												value="<s:date name="BEGTIME" format="yyyy-MM-dd HH:mm:ss" />"
												type="text"
												onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
												readonly class="Wdate input-text" style="width: 132px;" /><br />
												至 <s:set name="endtime" value="new java.util.Date()" /> <input
												name="attachParams['ENDTIME']"
												value="<s:date name="ENDTIME" format="yyyy-MM-dd HH:mm:ss" />"
												type="text"
												onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
												readonly class="Wdate input-text" style="width: 132px;" />

											</td>
											<td style="text-align: left"><input type="text"
												style="width: 100%;" name="attachParams['NAME']" /></td>

										</tr>
										<tr>

											<td style=" text-align: right" colspan="3"><input
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
												<font style="font-weight: bold">小区结构列表</font>
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

									<th>地区</th>
									<th>名称</th>
									<th>时间</th>

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


				<div id="frame2" class="loadframe"
					style="border: 1px solid #ddd;width:45%;float:right">

					<div style="padding-top: 10px">
						<span
							style="font:normal normal 900 10pt 黑体;display:block;margin-bottom:5px">已加载到分析列表的小区结构数据</span>
						<table width="100%">
							<tr>
								<td style="text-align:left;padding-left:10px"><input
									type="button" id="refreshLoadedBtn" onclick="" value="刷新"
									style="width: 90px;" name="search" /></td>
								<td style="text-align: right;padding-right:10px"><input
									type="button" id="removeFromAnalysis" value="从分析列表删除"
									style="width: 90px;" name="search" /></td>

							</tr>

						</table>

					</div>
					<div style="padding-top: 10px">
						<table id="queryResultTab2" class="greystyle-standard"
							width="100%">
							<th>地区</th>
							<th>名称</th>
							<th>时间</th>
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
	
	<div id="operInfo" style="display:none; top:40px;left:600px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
            	<table height="100%" width="100%" style="text-align:center">
                	<tr>
                    	<td>
                        	<span id="operTip"></span>
                        </td>
                    </tr>
                </table>
             
            </div>
</body>


</html>
