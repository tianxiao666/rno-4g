<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%-- 引入struts2标签库 --%>
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

		<title>小区信息修改</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="../../css/base.css">
		<link rel="stylesheet" type="text/css" href="../../css/input.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public.css">
		<link rel="stylesheet" type="text/css"
			href="../../css/public-table.css" />
		<link rel="stylesheet" type="text/css"
			href="../../css/public-div-standard.css">
		<link rel="stylesheet" type="text/css"
			href="../../css/public-table-standard.css" />
		<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
		<script type="text/javascript"
			src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="js/cellmanager.js"></script>
		<script type="text/javascript" src="js/commontimedtask.js"></script>
		<script type="text/javascript">
		function checkSelJq(){
		var converareaunchange=$('#coverareaunchange').val();
		$('#coverarea').find("option[value='"+converareaunchange+"']").attr("selected",true);
		}
		</script>
	</head>

	<body onload="checkSelJq();">
		<center>
			<h3>
				小区编辑
			</h3>
		</center>
		<form action="updateCellInfoByIdAction" method="post" onsubmit="return subformupdatecell();">
			<s:iterator var="cell" value="#request.cell">
				<table  border="1" align="center"
					class="main-table1 half-width" style="width:60%">
					<tr>
						<td  align="right" scope="col" class="menuTd">
							cell
						</th>
						<td  align="left" scope="col">
							<input type="hidden" name="cell.id" id="id"
								value="<s:property value='#cell.id'/>" />
							<input type="hidden" name="cell.address" id="address"
								value="<s:property value='#cell.address'/>" />
							<input type="hidden" name="cell.antGain" id="antGain"
								value="<s:property value='#cell.antGain'/>" />
							<input type="hidden" name="cell.antManufactory" id="antManufactory"
								value="<s:property value='#cell.antManufactory'/>" />
							<input type="hidden" name="cell.antModel" id="antModel"
								value="<s:property value='#cell.antModel'/>" />
							<input type="hidden" name="cell.areaId" id="areaId"
								value="<s:property value='#cell.areaId'/>" />
							<input type="hidden" name="cell.basetype" id="basetype"
								value="<s:property value='#cell.basetype'/>" />
							<input type="hidden" name="cell.bscId" id="bscId"
								value="<s:property value='#cell.bscId'/>" />
							<input type="hidden" name="cell.cellDescId" id="cellDescId"
								value="<s:property value="#cell.cellDescId"/>" />
							<input type="hidden" name="cell.covertype" id="covertype"
								value="<s:property value="#cell.covertype"/>" />
							<input type="hidden" name="cell.electricaldowntilt" id="electricaldowntilt"
								value="<s:property value="#cell.electricaldowntilt"/>" />
							<input type="hidden" name="cell.entityId" id="entityId"
								value="<s:property value="#cell.entityId"/>" />
							<input type="hidden" name="cell.entityType" id="entityType"
								value="<s:property value="#cell.entityType"/>" />
							<input type="hidden" name="cell.gsmfrequencesection" id="gsmfrequencesection"
								value="<s:property value="#cell.gsmfrequencesection"/>" />
							<input type="hidden" name="cell.importancegrade" id="importancegrade"
								value="<s:property value="#cell.importancegrade"/>" />
							<input type="hidden" name="cell.lngLats" id="lngLats"
								value="<s:property value="#cell.lngLats"/>" />
							<input type="hidden" name="cell.maxTxBs" id="maxTxBs"
								value="<s:property value="#cell.maxTxBs"/>" />
							<input type="hidden" name="cell.maxTxMs" id="maxTxMs"
								value="<s:property value="#cell.maxTxMs"/>" />
							<input type="hidden" name="cell.mechanicaldowntilt" id="mechanicaldowntilt"
								value="<s:property value="#cell.mechanicaldowntilt"/>" />	
							<input type="hidden" name="cell.passcheckingdate" id="passcheckingdate"
								value="<s:property value="#cell.passcheckingdate"/>" />
							<input type="hidden" name="cell.site" id="site"
								value="<s:property value="#cell.site"/>" />
							<input type="hidden" name="cell.takeoverdate" id="takeoverdate"
								value="<s:property value="#cell.takeoverdate"/>" />
							<input type="hidden" name="cell.ifpasschecking" id="ifpasschecking"
								value="<s:property value="#cell.ifpasschecking"/>" />
							<input type="hidden" name="cell.ifsharetg" id="ifsharetg"
								value="<s:property value="#cell.ifsharetg"/>" />
							<input type="hidden" name="cell.iftranssingleline" id="iftranssingleline"
								value="<s:property value="#cell.iftranssingleline"/>" />
							<span style="font-size:12px"><s:property value="#cell.label"/></span>				
							<input type="hidden" name="cell.label" id="label"
							onkeyup="value=value.replace(/[^A-Za-z0-9]/g,'')"	value="<s:property value="#cell.label"/>" readonly="readonly"/>
						</th>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							<p>
								小区中文名
							</p>
						</td>
						<td align="left">
							<input type="text" name="cell.name" id="name"
								value="<s:property value='#cell.name'/>" />
						</td>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							LAC
						</td>
						<td align="left">
							<input type="text" name="cell.lac" id="lac"
							onkeyup="value=value.replace(/[^\d]/g,'')"	value="<s:property value='#cell.lac'/>" />
						</td>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							CI
						</td>
						<td align="left">
							<input type="text" name="cell.ci" id="ci"
							onkeyup="value=value.replace(/[^\d]/g,'')"	value="<s:property value="#cell.ci"/>" />
						</td>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							ARFCN
						</td>
						<td align="left">
							<input type="text" name="cell.bcch" id="bcch"
							onkeyup="value=value.replace(/[^\d]/g,'')"	value="<s:property value="#cell.bcch"/>" />
						</td>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							BSIC
						</td>
						<td align="left">
							<input type="text" name="cell.bsic" id="bsic"
							onkeyup="value=value.replace(/[^\d]/g,'')"	value="<s:property value="#cell.bsic"/>" />
						</td>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							TCH
						</td>
						<td align="left">
							<input type="text" name="cell.tch" id="tch"
							onkeyup="value=value.replace(/[^\d,]/g,'')"	value="<s:property value="#cell.tch"/>" />
						</td>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							天线方向角
						</td>
						<td align="left">
							<input type="text" name="cell.bearing" id="bearing"
							onkeyup="value=value.replace(/[^\d]/g,'')"	value="<s:property value="#cell.bearing"/>" />
						</td>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							天线下倾
						</td>
						<td align="left">
							<input type="text" name="cell.downtilt" id="downtilt"
							onkeyup="value=value.replace(/[^\d]/g,'')"	value="<s:property value="#cell.downtilt"/>" />
						</td>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							基站类型
						</td>
						<td align="left">
							<input type="text" name="cell.btstype" id="btstype"
								value="<s:property value="#cell.btstype"/>" />
						</td>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							天线高度
						</td>
						<td align="left">
							<input type="text" name="cell.antHeigh" id="antHeigh"
							onkeyup="value=value.replace(/[^\d]/g,'')"	value="<s:property value="#cell.antHeigh"/>" />
						</td>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							天线类型
						</td>
						<td align="left">
							<input type="text" name="cell.antType" id="antType"
								value="<s:property value="#cell.antType"/>" />
						</td>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							LON
						</td>
						<td align="left">
							<input type="hidden" name="longitudeunchange" id="longitudeunchange"
								value="<s:property value="#cell.longitude"/>" />
							<input type="text" name="cell.longitude" id="longitude"
							onkeyup="value=value.replace(/[^\d.]/g,'')"	value="<s:property value="#cell.longitude"/>" />
						</td>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							LAT
						</td>
						<td align="left">
							<input type="hidden" name="latitudeunchange" id="latitudeunchange"
								value="<s:property value="#cell.latitude"/>" />
							<input type="text" name="cell.latitude" id="latitude"
							onkeyup="value=value.replace(/[^\d.]/g,'')"	value="<s:property value="#cell.latitude"/>" />
						</td>
					</tr>
					<tr>
						<td align="right" class="menuTd">
							覆盖范围
						</td>
						<td align="left">
							<input type="hidden" name="coverareaunchange" id="coverareaunchange"
								value="<s:property value="#cell.coverarea"/>" />
							<select id="coverarea" name="cell.coverarea">
														<option value="室外覆盖">室外覆盖</option>
														<option value="室分覆盖">室分覆盖</option>
														<option value="混合覆盖">混合覆盖</option>
														</select>
						</td>
					</tr>
					
					<tr>
						<td align="right" class="menuTd">
							
						</td>
						<td>
							<input type="submit" name="button" id="button" value="修　改" />　　<input type="reset" name="button2" id="button2" value="取　消" />
						</td>
					</tr>
				</table>
			</s:iterator>
		</form>
	</body>
</html>
