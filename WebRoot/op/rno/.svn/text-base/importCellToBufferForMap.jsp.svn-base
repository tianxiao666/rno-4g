<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="net.rubyeye.xmemcached.MemcachedClient"%>
<%@page import="com.iscreate.op.pojo.rno.RnoXlsCell"%>
<%-- 引入struts2标签库 --%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>导入小区到地图显示</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@include file="commonheader.jsp"%>
	
	<style type="text/css">  
	body, html,#map_canvas {width: 100%;height: 100%;overflow: hidden;margin:0;}  
	#l-map{height:100%;width:78%;float:left;border-right:2px solid #bcbcbc;}  
	#r-result{height:100%;width:20%;float:left;}  
	</style> 
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
	  
	<script type="text/javascript" src="http://api.map.baidu.com/library/DistanceTool/1.2/src/DistanceTool_min.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/library/GeoUtils/1.2/src/GeoUtils_min.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay_min.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer_min.js"></script>
	
	<script type="text/javascript" src="js/cellimportformem.js"></script>
	<script type="text/javascript" src="js/provincecityareacascade.js"></script>
	<%--<style type="text/css">
	.comm{ font-family:"新宋体";font-size:12px; font-weight:normal; border-collapse:collapse;}
	</style> --%>
	<script type="text/javascript">
	var glomapid='<%=session.getAttribute("mapId") %>';
		// 增加一个名为 trim 的函数作为
		// String 构造函数的原型对象的一个方法。
		String.prototype.trim = function()
		{
		    // 用正则表达式将前后空格
		    // 用空字符串替代。
		    return this.replace(/(^\s*)|(\s*$)/g, "");
		}
		$(function() {
			$("#trigger").val(glomapid=='null' || glomapid=='baidu'?"切换谷歌":"切换百度");
            
            });
			
	</script>
  </head>
  <body>
  <div>
    <form  id="formImportCell" method="post" menctype="multipart/form-data">
    <input type="hidden" name="fileCode" value="GSMCELLBRIEFFILE" class="canclear">
    <input type="hidden" name="needPersist" value="false" />
<table id="table1" border="1" align="center" class="main-table1 half-width" style="width:100%;font-size: 12px">
  <tr><th colspan="2" align="center">经纬度简要信息数据录入</th></tr>
  <tr>
   		<td align="right">区域：</td>
   		<td>
        省：<select name="provinceId" class="required" id="provinceId" style="width:80px">
				<%-- option value="-1">请选择</option --%>
				<s:iterator value="zoneProvinceLists" id="onearea">
					<option value="<s:property value='#onearea.area_id' />">
						<s:property value="#onearea.name" />
					</option>
				</s:iterator>
			</select>
			市：<select name="cityId" class="required" id="cityId" style="width:80px">
					<%-- option value="-1">请选择</option --%>
			</select>
			区：<select name="areaId" class="required" id="areaId" style="width:80px">
					<%-- option value="-1">请选择</option --%>
			</select>
		<input  type="button" id="trigger" name="trigger" value=""/>
		<input type="hidden" name="outjson" id="outjson" value="{
		<s:iterator value='zoneCountyLists' id='onearea' status="st">
		<s:if test="#st.last">
		<s:property value='#onearea.name' />:[{lng:<s:property value='#onearea.longitude' />,lat:<s:property value='#onearea.latitude' />}]
  		</s:if>
  		<s:else>
		<s:property value='#onearea.name' />:[{lng:<s:property value='#onearea.longitude' />,lat:<s:property value='#onearea.latitude' />}],
		</s:else>
		</s:iterator>}"/>
        </td>
        </tr>
  <tr>
    <td align="right">录入经纬度信息数据：</td>
    <td align="left"><input type="file" name="file" style="width:352px" class="required"/><a href="fileDownloadAction?fileName=地图打点数据导入模板.xlsx" title="点击下载模板" id="downloadHref">地图打点数据导入模板</a></td>
  </tr>
  <tr>
    <td align="right">&nbsp;</td>
    <td align="left"><input type="button" name="readexcel2" id="importBtn" value="导入表数据" /></td>
  </tr>
</table>
<div id="importResultDiv" style="text-align:center; padding:0; padding-bottom:0px;"></div>
</form>
</div>
<div id="map_canvas"></div>
  </body>
</html>