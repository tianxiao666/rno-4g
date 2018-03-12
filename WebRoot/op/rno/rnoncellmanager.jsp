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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>邻区关系管理</title>
<%@include file="commonheader.jsp"%>
<script type="text/javascript" src="js/ncellimport.js"></script>
<script type="text/javascript" src="js/ncellmanager.js"></script>
<script type="text/javascript">
	var $ = jQuery.noConflict();
	$(function() {
		//tab选项卡
		tab("div_tab", "li", "onclick");//项目服务范围类别切换

	})
</script>
</head>

<body>
	<div class="loading_cover" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在加载 <em class="loading_fb" id="tipcontentId"></em>,请稍侯...
		</h4>
	</div>
	<div class="div_left_main" style="width: 100%">
		<div class="div_left_content">
			<%-- <div class="div_left_top">邻区关系管理</div>  --%>
			<div style="padding: 10px">
				<div id="frame" style="border: 1px solid #ddd">
					<div id="div_tab" class="div_tab divtab_menu">
						<ul>
							<li class="selected">邻区关系查询</li>
							<li>邻区关系导入</li>

						</ul>
					</div>

					<div class="divtab_content">
						<div id="div_tab_0">
							<form id="conditionForm" method="post">
								<input type="hidden" id="hiddenPageSize" name="page.pageSize"
									value="25" /> <input type="hidden" id="hiddenCurrentPage"
									name="page.currentPage" value="1" /> <input type="hidden"
									id="hiddenTotalPageCnt" /> <input type="hidden"
									id="hiddenTotalCnt" />
								<div>
									<table class="main-table1 half-width"
										style="width: 70%; padding-top: 10px">
										
										
										<tr>
										<td class="menuTd" style="text-align: center"><span
												style="padding-top: 0px">区域</span></td>
												<td class="menuTd" style="text-align: center">BSC/NodeB</td>
										<td class="menuTd" style="text-align: center">CELL</td>
											<td class="menuTd" style="text-align: center">小区中文名</td>
										</tr>
										<tr>
											
											<td style="text-align: center">省：<select name="provinceId"
												class="required" id="provinceId1" style="width:70px">
													<%-- option value="-1">请选择</option --%>
													<s:iterator value="provinceAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> <br/>市：<select name="queryCell.cityId" class="required" id="cityId1"
												style="width:70px">
													<s:iterator value="cityAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> <br/>区：<select name="queryCell.areaId" class="required"
												id="areaId1" style="width:70px">
												<option value="-1" selected="true">全部</option>
													<s:iterator value="countryAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select>
											</td>
											
											<td style="text-align: center"><select
												id="queryCellBscId" name="queryCell.bscId"
												style="width: 90%">
													<option value="-1">请选择</option>
											</select></td>
											
											<td style="text-align: center"><input type="text"
												style="width: 90%;" name="queryCell.label" /></td>
											
											<td style="text-align: center"><input type="text"
												style="width: 90%;" name="queryCell.name" /></td>
										</tr>
										<tr>

											<td style=" text-align: center" colspan="8"><input
												type="submit" onclick="" value="查  询" style="width: 90px;"
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
											<font style="font-weight: bold">邻区关系信息：</font>
										</p>
									</td>
									<td style="width: 15%; text-align: right"><input
										type="button" id="deleteNcell" value="删除邻区关系"
										style="width: 90px;" name="search" /></td>

								</tr>

							</table>

						</div>
						<div style="padding-top: 10px">
							<table id="queryResultTab" class="greystyle-standard"
								width="100%">

								<th style="width: 20%">主小区</th>
								<th style="width: 30%">邻小区</th>
								<th style="width: 30%">是否同site</th>
								<th style="width: 30%">邻区方向</th>

								<th style="width: 10%">全选 <input type="checkbox"
									onclick="javascript:operAllCheckbox(this);" name="1" id="1" />
									<label for="1"></label>
								</th>
								<%--  
											<tr class="greystyle-standard-whitetr">

												<td>
													S7CDKG2
												</td>
												<td>
													东坑2
												</td>

												<td>
													<input type="checkbox" name="1" id="1" />
													<label for="1"></label>
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
								type="hidden" name="systemConfig" value="true" /> <input
								type="hidden" name="fileCode" value="GSMNCELLFILE"
								class="canclear" />
							<div>
								<table class="main-table1 half-width">
									<tbody>
										<tr>
											<td class="menuTd">所属地市<span class="txt-impt">*</span>
											</td>
											<td style="width: 50%; font-weight: normal;" colspan="0">
												省：<select name="provinceId" class="required"
												id="provinceId2">
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
											</select> 区：<select name="areaId" class="required" id="areaId2">
													<s:iterator value="countryAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select>
											</td>
										</tr>

										<tr>
											<td class="menuTd">邻区关系定义文件（EXCEL）<span class="txt-impt">*</span>
											</td>
											<td style="width: 50%; font-weight: bold" colspan="0"><input
												type="file" style="width: 44%;" name="file"
												class="canclear required" /> &nbsp;<a
												href="fileDownloadAction?fileName=邻区关系导入模板.xlsx"
												title="点击下载模板" id="downloadHref">邻区关系导入模板</a> <br /></td>
										</tr>
										<tr>
											<td class="menuTd">主小区重复处理方式<span class="txt-impt">*</span>
											</td>
											<td><input type="radio" name="update" id="3"
												value="true" checked="true" class="canclear required" /> <label
												for="3"> 覆盖(删除已有邻区关系) </label>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" name="update" id="32" value="false"
												class="canclear required" /> <label for="32">
													追加(保留已有邻区关系) </label> <br /></td>
										</tr>
									</tbody>


								</table>
							</div>
							<div class="container-bottom" style="padding-top: 10px">
								<table style="width: 60%; margin: auto" align="center">
									<tr>
										<td><input type="button" value="导    入"
											style="width: 90px;" id="importBtn" name="import" /> <br />
											<br /></td>


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
</html>
