<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>NCS原始数据解析</title>

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
	src="js/rno_structure_ncs_orifile_import.js"></script>
<style type="text/css">
.greystyle-standard-noborder tr td {
	padding: 0
}

.dict_left {
	width: 130px;
	float: left;
	left: 0;
	z-index: 1;
	border: 1px;
	margin: 5px
}

.dict_right {
	width: 100%;
	left: 200px;
	float: left;
	border: 1px;
	margin: 5px
}

/* 覆盖分页面板的对齐样式 */
.paging_div {
	text-align: left;
}
</style>
</head>

<script type="text/javascript">
	var $ = jQuery.noConflict();
	$(function() {
		//tab选项卡
		//tab("div_tab", "li", "onclick");//项目服务范围类别切换

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
			<div style="padding: 10px">
				<div id="frame" class="loadframe"
					style="border: 1px solid #ddd;width:100%;float:left">

					<div id="div_tab_0" style="display: block;">
						<form id="formImportDT" enctype="multipart/form-data"
							method="post">
							<input type="hidden" name="needPersist" value="true" /> <input
								type="hidden" name="systemConfig" value="true" />  <input type="hidden" id="autoload"
								name="autoload" value="false" />
							<div>
								<table class="main-table1 half-width">
									<tbody>

										<tr>
											<td class="menuTd" style="width:410px">所属地市<span class="txt-impt">*</span>
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
											</select> 市：<select name="attachParams['cityId']" class="required"
												id="cityId1">
													<s:iterator value="cityAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> 区：<select name="areaId" class="required" id="areaId1">
											       <option value="-1">不关注</option>
													<s:iterator value="countryAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select>
											</td>
										</tr>
										<tr>
										  <td class="menuTd">
										  NCS文件类型
										  </td>
										  <td>
										  爱立信&nbsp;&nbsp;<input type="radio" class="fileCodeCls" id="eric" name="fileCode" value="ERICSSONNCSFILE" checked="checked" />
										<span id="fileTip">（多个文件请用Zip格式压缩为一个压缩包，数量最多不超过50个）</span>
										<br/>
										  华为&nbsp;&nbsp;<input type="radio"  class="fileCodeCls" id='huawei' name="fileCode" value="HUAWEINCSFILE" />
										  <a title='点击下载' href="fileDownloadAction?fileName=华为Ncs导入模板.xlsx" >华为Ncs导入模板下载</a>
										  </td>
										</tr>
										<tr>
											<td class="menuTd">NCS测量文件<span class="txt-impt">*</span>
											</td>
											<td style="width: 50%; font-weight: bold" colspan="0"><input
												type="file" style="width: 44%;" name="file"
												class="canclear required" /></td>
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
							</div>
						</form>
					</div>

					<%--详情查看 --%>

				</div>



			</div>



		</div>


	</div>

	<div id="operInfo"
		style="display:none; top:40px;left:400px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
		<table height="100%" width="100%" style="text-align:center">
			<tr>
				<td><span id="operTip"></span></td>
			</tr>
		</table>

	</div>


</body>


</html>
