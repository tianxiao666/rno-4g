<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>LTE小区导入</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>

  <script type="text/javascript" src="js/rno_lte_cellmanage_import.js"></script>
</head>

<body>
	<form id="formImportLteCell" enctype="multipart/form-data" method="post">
		<input type="hidden" name="needPersist" value="true" /> <input
			type="hidden" name="systemConfig" value="true" />
			<input
			type="hidden" name="fileCode" value="LTECELLFILE" />
		<div>
			<table class="main-table1 half-width">
				<tbody>
					<tr>
						<td class="menuTd">所属地市<span class="txt-impt">*</span>
						</td>
						<td style="width: 50%; font-weight: normal;" colspan="0">省：<select
							name="areaId" class="required" id="provinceId2">
								<%-- option value="-1">请选择</option --%>
								<s:iterator value="provinceAreas" id="onearea">
									<option value="<s:property value='#onearea.area_id' />">
										<s:property value="#onearea.name" />
									</option>
								</s:iterator>
						</select> <%-- 市：<select name="areaId" class="required" id="cityId2">
								<s:iterator value="cityAreas" id="onearea">
									<option value="<s:property value='#onearea.area_id' />">
										<s:property value="#onearea.name" />
									</option>
								</s:iterator>
						</select>  --%>
						</td>
					</tr>
					
					<tr>
						<td class="menuTd">LTE小区信息文件（CSV）<span class="txt-impt">*</span>
							<br />
						</td>
						<td style="width: 50%; font-weight: bold" colspan="0"><input
							type="file" style="width: 44%;" name="file" id="idFile"
							class="canclear  required" /> &nbsp; <a
							href="fileDownloadAction?fileName=LTE小区信息导入模板.xlsx" title="点击下载模板" id="downloadHref">下载LTE小区导入模板</a><br />
						</td>
					</tr>
					<tr>
						<td class="menuTd">导入模式<span class="txt-impt">*</span> <br />
						</td>
						<td><input type="radio" name="attachParams['mode']" id="3" value="overwrite"
							class="canclear  required" checked="checked"/> <label for="3"> 覆盖 </label>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
							<input
							type="radio" style="display:none" name="attachParams['mode']" id="32" value="append"
							class="canclear  required" /> <label style="display:none" for="32"> 新增 </label> 
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
							<input
							type="radio" style="display:none" name="attachParams['mode']" id="32" value="delete"
							class="canclear  required" /> <label style="display:none" for="32"> 删除 </label> 
							
							<br /></td>
					</tr>
				</tbody>


			</table>
		</div>
		<div class="container-bottom" style="padding-top: 10px">
			<table style="width: 60%; margin: auto" align="center">
				<tr>
					<td><input type="button" value="导    入" style="width: 90px;"
						id="importBtn" name="import" /> <br /></td>

				</tr>
			</table>
			<div id="importResultDiv" class="container-bottom"
				style="padding-top: 10px"></div>
	</form>
</body>
</html>
