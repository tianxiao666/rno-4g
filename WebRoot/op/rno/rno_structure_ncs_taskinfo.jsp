<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>结构分析任务信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>
<script type="text/javascript" src="js/rno_structure_ncs_analysis_v2.js?v=<%=(String)session.getAttribute("version")%>"></script>

<style type="text/css">
   
.taskInfoTab
{
	width:600px;margin: 0px auto;border: 1px solid gray;margin-top: 4px;
}
.taskInfoTab td
{
	padding:10px;
}
</style>
  </head>
  
  <body>
  	<%-- 数据加载遮罩层 --%>
	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="loadContentId"></em>,请稍侯...
		</h4>
	</div>
	
  	<font style="font-weight: bold;">当前位置： 专项优化 &gt; 结构分析 &gt; 新建结构分析任务</font>
	<br>
	<center>
  	<a href="initNcsAnalysisPageAction" style="text-decoration: underline;font-weight: bold;">&lt;&lt;返回任务列表</a><br/><br/>
  	 <font style="color: #31FF81;font-weight: bold;">任务信息</font><font style="font-weight: bold;">>>参数配置>>提交任务</font>
  	<br>
  	</center>
  	
  	<input type="hidden" id="provinceIdFromSession" value="<s:property value="#session.NCSTASKINFO.taskInfo.provinceId"/>"/>
	<input type="hidden" id="cityIdFromSession" value="<s:property value="#session.NCSTASKINFO.taskInfo.cityId"/>"/>
  	<input type="hidden" id="startTimeFromSession" value="<s:property value="#session.NCSTASKINFO.taskInfo.startTime"/>"/>
	<input type="hidden" id="endTimeFromSession" value="<s:property value="#session.NCSTASKINFO.taskInfo.endTime"/>"/>
	<input type="hidden" id="useEriDataSession" value="<s:property value="#session.NCSTASKINFO.taskInfo.busDataType.USEERIDATA"/>"/>
	<input type="hidden" id="useHwDataSession" value="<s:property value="#session.NCSTASKINFO.taskInfo.busDataType.USEHWDATA"/>"/>
	<input type="hidden" id="CALCONCLUSTERSession" value="<s:property value="#session.NCSTASKINFO.taskInfo.calProcedure.CALCONCLUSTER"/>"/>
	<input type="hidden" id="CALCLUSTERCONSTRAINSession" value="<s:property value="#session.NCSTASKINFO.taskInfo.calProcedure.CALCLUSTERCONSTRAIN"/>"/>
	<input type="hidden" id="CALCLUSTERWEIGHTSession" value="<s:property value="#session.NCSTASKINFO.taskInfo.calProcedure.CALCLUSTERWEIGHT"/>"/>
	<input type="hidden" id="CALCELLRESSession" value="<s:property value="#session.NCSTASKINFO.taskInfo.calProcedure.CALCELLRES"/>"/>
	<input type="hidden" id="CALIDEALDISSession" value="<s:property value="#session.NCSTASKINFO.taskInfo.calProcedure.CALIDEALDIS"/>"/>
	
  	
  		
    <div style="width: 100%;margin-top: 20px">
		    <table style="width:600px;margin: 0px auto;">
		    <tr><td><input type="button" id="taskInfoNextStep" name="" value="下一步 >"/></td></tr>
		    </table>
    
			<table class="taskInfoTab" rules="none">
				<tbody>
					<tr>
						<td align="right"> 
							区域<font style="color: red;">*</font>：<br>
						</td>
						<td align="left">
							省：<select name=""
								class="required" id="provinceId2">
									<%-- option value="-1">请选择</option --%>
									<s:iterator value="provinceAreas" id="onearea">
										<option value="<s:property value='#onearea.area_id' />">
											<s:property value="#onearea.name" />
										</option>
									</s:iterator>
							</select>
							<input type="hidden" id="cityIdParam" value="<s:property value="cityId"/>"/>
							市：<select name="" class="required"
								id="cityId2">
									<s:iterator value="cityAreas" id="onearea">
										<option value="<s:property value='#onearea.area_id' />">
											<s:property value="#onearea.name" />
										</option>
									</s:iterator>
							</select>
						</td>
					</tr>
					<tr>
						<td align="right"> 
							任务名称<font style="color: red;">*</font>：<br>
							<span style="color:red;width:100px;font-family:华文中宋;" id="nameErrorText"></span>
						</td>
						<td align="left"> 
							<textarea style="width: 330px; height: 25px; " id="taskName"><s:property value="#session.NCSTASKINFO.taskInfo.taskName"/></textarea>
							<span style="color:red;width:100px;font-family:华文中宋;" id="nameError"></span>
						</td>
					</tr>
					<tr>
						<td align="right"> 
							任务描述：<br>
							<span style="color:red;width:100px;font-family:华文中宋;" id="descErrorText"></span>
						</td>
						<td align="left"> <br>
							<textarea style="width: 330px; height: 184px; " id="taskDescription"><s:property value="#session.NCSTASKINFO.taskInfo.taskDesc"/></textarea>
							<span style="color:red;width:100px;font-family:华文中宋;" id="descError"></span>
						</td>
					</tr>
					<tr>
	
						<td align="right"> 
							测试时间<font style="color: red;">*</font>：<br>
							<span style="color:red;width:100px;font-family:华文中宋;" id="dateErrorText"></span>
						</td>
						<td align="left">
							<input id="beginTime" name=""
								value="<s:property value="preFiveDayTime"/>"
								type="text"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){latestAllowedTime.focus();},maxDate:'#F{$dp.$D(\'latestAllowedTime\')}'})"
								readonly class="Wdate input-text" style="width: 132px;" />
							到 
							<s:set name="endtime" value="new java.util.Date()" />
							<input id="latestAllowedTime" name=""
								value="<s:date name="endtime" format="yyyy-MM-dd HH:mm:ss" />"
								type="text"
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\')}'})"
								readonly class="Wdate input-text" style="width: 132px;" />
							<span style="color:red;width:100px;font-family:华文中宋;" id="dateError"></span>
						</td>
					</tr>
					<tr><td align="right">数据类型：</td><td>爱立信<input type="checkbox" id="useEriData" name="busDataType['USEERIDATA']" checked/>　华为<input type="checkbox" id="useHwData" name="busDataType['USEHWDATA']" checked="checked"/><span id="dataTypeErrorText" style="color:red;width:100px;font-family:华文中宋;"></span></td></tr>
					<tr><td align="right">计算步骤：</td><td>最大连通簇<input type="checkbox" id="calConCluster" name="calProcedure['CALCONCLUSTER']" checked="checked"/>　簇约束因子<input type="checkbox" id="calClusterConstrain" name="calProcedure['CALCLUSTERCONSTRAIN']" checked="checked"/>　簇权重<input type="checkbox" id="calClusterWeight" name="calProcedure['CALCLUSTERWEIGHT']" checked="checked"/>　结构指数<input type="checkbox" id="calCellRes" name="calProcedure['CALCELLRES']" checked="checked"/>　理想距离<input type="checkbox" id="calIdealDis" name="calProcedure['CALIDEALDIS']"/></td></tr>
				</tbody>
			</table>
	</div>
  </body>
 <script  type="text/javascript" >
 		var cityIdParam = $("#cityIdParam").val();
 		var cityIdFromSession = $("#cityIdFromSession").val();
 		var provinceIdFromSession = $("#provinceIdFromSession").val();
 		var startTimeFromSession = $("#startTimeFromSession").val();
 		var endTimeFromSession = $("#endTimeFromSession").val();
 		
 		//console.log(cityIdFromSession+"  "+provinceIdFromSession );
 		//优先读取session的值，session值不存在读取初始值
 		if(cityIdFromSession == 0 || provinceIdFromSession == 0) {
 		 	$("#cityId2").val(cityIdParam);
 		}else {
 			$("#cityId2").val(cityIdFromSession);
 		}
 		if(startTimeFromSession != "" || endTimeFromSession != "") {
 			$("#beginTime").val(startTimeFromSession);
 			$("#latestAllowedTime").val(endTimeFromSession);
 		}
 		
 		
 		
 		//其他设置
 		//使用数据类型
 		//是否用爱立信数据
 		if($("#useEriDataSession").val()!="false"){
 			$("#useEriData").attr("checked","checked");
 		}else{
 			$("#useEriData").removeAttr("checked");
 		}
 		//是否用华为数据
 		if($("#useHwDataSession").val()!="false"){
 			$("#useHwData").attr("checked","checked");
 		}else{
 			$("#useHwData").removeAttr("checked");
 		}
 		
	    //计算步骤
	    if($("#CALCONCLUSTERSession").val()!="false"){
 			$("#calConCluster").attr("checked","checked");
 		}else{
 			$("#calConCluster").removeAttr("checked");
 		}
	    if($("#CALCLUSTERCONSTRAINSession").val()!="false"){
 			$("#calClusterConstrain").attr("checked","checked");
 		}else{
 			$("#calClusterConstrain").removeAttr("checked");
 		}
	    if($("#CALCLUSTERWEIGHTSession").val()!="false"){
 			$("#calClusterWeight").attr("checked","checked");
 		}else{
 			$("#calClusterWeight").removeAttr("checked");
 		}
	    if($("#CALCELLRESSession").val()!="false"){
 			$("#calCellRes").attr("checked","checked");
 		}else{
 			$("#calCellRes").removeAttr("checked");
 		}
	    if($("#CALIDEALDISSession").val()!="false"){
 			$("#calIdealDis").attr("checked","checked");
 		}else{
 			$("#calIdealDis").removeAttr("checked");
 		}
  	
 </script> 
</html>
