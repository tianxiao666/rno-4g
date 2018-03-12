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

<title>华为2G小区及MRR信息导入</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<%@include file="commonheader.jsp" %>
<%-- script type="text/javascript" src="js/rno_2g_hw_cell_mrr_import.js"></script--%>
<script type="text/javascript" src="js/rno_2g_hw_mrr_import.js"></script>
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
							<li class="selected">华为MRR信息导入</li>
						</ul>
					</div>

					<div id="div_tab_2">
						<form id="formImportMrr" enctype="multipart/form-data"
							method="post">
							<input type="hidden" name="needPersist" value="true" /> <input
								type="hidden" name="systemConfig" value="true" /> <input
								type="hidden" name="areaId" value="" id="areaId" />
								
								
								<input type="hidden" name="update" id="3"
												value="true" class="canclear  required" />
							    <input type="hidden" name="fileCode" id="3"
												value="2GHWMRRFILE" class="canclear required" />
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
											</select> 市：<select name="attachParams['cityId']" class="required"
												id="cityId2">
													<s:iterator value="cityAreas" id="onearea">
														<option value="<s:property value='#onearea.area_id' />">
															<s:property value="#onearea.name" />
														</option>
													</s:iterator>
											</select> <%--
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
										<%--
										<tr>
											<td class="menuTd">网络制式<span class="txt-impt">*</span> <br />
											</td>
											<td><input type="radio" name="fileCode" id="3"
												value="2GHWMRRFILE" class="canclear required" /> <label
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
											<td class="menuTd">MRR信息文件<span class="txt-impt">*</span>
												<br />
											</td>
											<td style="width: 50%; font-weight: bold" colspan="0"><input
												type="file" style="width: 44%;" multiple name="file" id='fileid'
												class="canclear  required" /> &nbsp; <a
												href="javascript:void(0);" title="点击下载模板" id="downloadHref">&nbsp;</a><br />
											</td>
										</tr>
										
										<%-- 
										<tr>
											<td class="menuTd">重复记录处理方式<span class="txt-impt">*</span>
												<br />
											</td>
											<td><input type="radio" name="update" id="3"
												value="true" class="canclear  required" /> <label for="3">
													覆盖(更新信息) </label>
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="radio" name="update" id="32" value="false"
												class="canclear  required" /> <label for="32"> 忽略 </label>
												<br /></td>
										</tr>
										--%>
									</tbody>


								</table>
							</div>
							<div class="container-bottom" style="padding-top: 10px">
								<table style="width: 60%; margin: auto" align="center">
									<tr>
										<td><input type="button" value="导    入"
											style="width: 90px;" id="importMrrBtn" name="import" /> <br />
										</td>


									</tr>
								</table>
								<div id="importMrrResultDiv" class="container-bottom"
									style="padding-top: 10px"></div>
						</form>
					</div>

				</div>
			</div>

		</div>


	</div>
</body>