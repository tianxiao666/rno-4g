<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>LTE邻区关系导入</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>

  <script type="text/javascript" src="js/rno_lte_ncellmanage_import.js"></script>
</head>

<body>
	<form id="formImportLteNCell" enctype="multipart/form-data" method="post">
		<input type="hidden" name="needPersist" value="true" /> 
		<input type="hidden" name="systemConfig" value="true" />
		<input type="hidden" name="fileCode" value="LTENCELLFILE" />
		<input type="hidden" name="areaId" value="1000" />
		<div>
			<table class="main-table1 half-width">
				<tbody>	
					<tr>
						<td class="menuTd">LTE邻区关系文件（EXCEL）<span class="txt-impt">*</span>
							<br />
						</td>
						<td style="width: 50%; font-weight: bold" colspan="0">
							<input
								type="file" style="width: 44%;" name="file" id="fileid"
								class="canclear  required" /> &nbsp; 
							<a	href="fileDownloadAction?fileName=LTE邻区导入模板.xlsx" 
								title="点击下载模板"  id="downloadHref">下载LTE邻区导入模板</a><br/>
						</td>
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
				style="padding-top: 10px">
			</div>

	</form>
</body>
</html>
