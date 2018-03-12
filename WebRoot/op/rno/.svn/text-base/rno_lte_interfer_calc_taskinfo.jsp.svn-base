<%@	page language="java"  import="java.util.* , java.text.*"  pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>区域PCI翻频方案任务信息</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>
<script type="text/javascript" src="js/rno_lte_interfer_calc.js?v=<%=(String)session.getAttribute("version")%>"></script>
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
<script type="text/javascript">
var l=0;
var focusonce=function(){
	if(l<1){
	latestAllowedTime.focus();
	l++;
	}
};
var	extendint = function(i) //将1位数转换为0开头的2位数
{
	var j = "00";
	if (i < 10) {
		j = "0" + i.toString();
	} else {
		j = i.toString();
	}
	return j;
};
	var getenddate = function() { //根据结束时间调整开始时间，使其保持在10天以内
		var begdate = new Date($("#beginTime").val().replace(/-/g, "\/"));
		var enddate = new Date($("#latestAllowedTime").val()
				.replace(/-/g, "\/"));
		if ((enddate - begdate) / 1000 / 60 / 60 / 24 > 10) {
			enddate.setDate(enddate.getDate() - 9);
			var sss = enddate.getFullYear() + "-" + (enddate.getMonth() + 1)
					+ "-" + enddate.getDate() + " "
					+ extendint(begdate.getHours()) + ":"
					+ extendint(begdate.getMinutes()) + ":"
					+ extendint(begdate.getSeconds());
			$("#beginTime").val(sss);
			beginTime.focus();
		}
	};
	var getbegdate = function() { //根据开始时间调整结束时间，使其保持在10天以内
		var begdate = new Date($("#beginTime").val().replace(/-/g, "\/"));
		var enddate = new Date($("#latestAllowedTime").val()
				.replace(/-/g, "\/"));
//		var nowdate = new Date()
		if ((enddate - begdate) / 1000 / 60 / 60 / 24 > 10) {
			begdate.setDate(begdate.getDate() + 9);
			var sss = begdate.getFullYear() + "-" + (begdate.getMonth() + 1)
					+ "-" + begdate.getDate() + " " + enddate.getHours() + ":"
					+ enddate.getMinutes() + ":" + enddate.getSeconds();
			$("#latestAllowedTime").val(sss);
			if(l>0){
				latestAllowedTime.focus();
			}
		}
	};
</script>
  </head>
  <body >
  	<%-- 数据加载遮罩层 --%>
	<div class="loading_cover" id="loadingDataDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			 <em class="loading_fb" id="loadContentId"></em>,请稍侯...
		</h4>
	</div>

  	<font style="font-weight: bold;">当前位置： PCI优化 &gt; 新建区域PCI翻频方案任务</font>
	<br>
	<center>
  	<a href="initLteInterferCalcPageAction" style="text-decoration: underline;font-weight: bold;">&lt;&lt;返回任务列表</a><br/><br/>
  	 <font style="color: #31FF81;font-weight: bold;">任务信息</font><font style="font-weight: bold;">>>参数配置>>提交任务</font>
  	<br>
  	</center>

  	<input type="hidden" id="provinceIdFromSession" value="<s:property value="#session.MRTASKINFO.taskInfo.provinceId"/>"/>
	<input type="hidden" id="cityIdFromSession" value="<s:property value="#session.MRTASKINFO.taskInfo.cityId"/>"/>
  	<input type="hidden" id="startTimeFromSession" value="<s:property value="#session.MRTASKINFO.taskInfo.startTime"/>"/>
	<input type="hidden" id="endTimeFromSession" value="<s:property value="#session.MRTASKINFO.taskInfo.endTime"/>"/>

	<input type="hidden" id="planTypeSession" value="<s:property value="#session.MRTASKINFO.taskInfo.planType"/>"/>
	<input type="hidden" id="converTypeSession" value="<s:property value="#session.MRTASKINFO.taskInfo.converType"/>"/>
	<input type="hidden" id="cosiSession" value="<s:property value="#session.MRTASKINFO.taskInfo.relaNumerType"/>"/>
	<input type="hidden" id="CALCONCLUSTERSession" value="<s:property value="#session.MRTASKINFO.taskInfo.calProcedure.CALCONCLUSTER"/>"/>
	<input type="hidden" id="CALCLUSTERCONSTRAINSession" value="<s:property value="#session.MRTASKINFO.taskInfo.calProcedure.CALCLUSTERCONSTRAIN"/>"/>
	<input type="hidden" id="CALCLUSTERWEIGHTSession" value="<s:property value="#session.MRTASKINFO.taskInfo.calProcedure.CALCLUSTERWEIGHT"/>"/>
	<input type="hidden" id="CALCELLRESSession" value="<s:property value="#session.MRTASKINFO.taskInfo.calProcedure.CALCELLRES"/>"/>
	<input type="hidden" id="CALIDEALDISSession" value="<s:property value="#session.MRTASKINFO.taskInfo.calProcedure.CALIDEALDIS"/>"/>



    <div style="width: 100%;margin-top: 20px">
		    <table style="width:600px;margin: 0px auto;">
		    <tr><td><input type="button" id="taskInfoNextStep" name="" value="下一步 >"/></td></tr>
		    </table>

			<table class="taskInfoTab" rules="none">
				<tbody>
					<tr>
						<td align="right" style="width:150px;">
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
							<textarea style="width: 330px; height: 25px; " id="taskName"><s:property value="#session.MRTASKINFO.taskInfo.taskName"/></textarea>
							<span style="color:red;width:100px;font-family:华文中宋;" id="nameError"></span>
						</td>
					</tr>
					<tr>
						<td align="right">
							任务描述：<br>
							<span style="color:red;width:100px;font-family:华文中宋;" id="descErrorText"></span>
						</td>
						<td align="left"> <br>
							<textarea style="width: 330px; height: 184px; " id="taskDescription"><s:property value="#session.MRTASKINFO.taskInfo.taskDesc"/></textarea>
							<span style="color:red;width:100px;font-family:华文中宋;" id="descError"></span>
						</td>
					</tr>
					<tr>
						<td align="right">
							测量时间<font style="color: red;">*</font>：<br>
							<span style="color:red;width:100px;font-family:华文中宋;" id="dateErrorText"></span>
						</td>
						<td align="left">
						<s:set name="begtime" value="new java.util.Date()" />
							<input id="beginTime" name="cond['begTime']"
<%--	value="<s:property value="preFiveDayTime"/>" --%>
								value="<s:property value="taskBeginTime"/>"
								type="text"
<%-- 						onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){getbegdate();$('#latestAllowedTime').val($dp.cal.getNewDateStr('yyyy-MM-dd 23:59:59'));latestAllowedTime.focus();},maxDate:'#F{$dp.$D(\'latestAllowedTime\',{H:-23,m:-59,s:-59})}'})" --%>
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){getbegdate();focusonce();},maxDate:'#F{$dp.$D(\'latestAllowedTime\',{H:-23,m:-59,s:-59})||\'%y-%M-{%d-1}\'}'})"
								readonly class="Wdate input-text" style="width: 132px;" />
							到
							<s:set name="endtime" value="new java.util.Date()" />
							<input id="latestAllowedTime"  name="cond['endTime']"
<%-- value="<s:date name="endtime" format="yyyy-MM-dd HH:mm:ss" />" --%>
								value="<s:property value="taskEndTime" />"
								type="text"
<%-- 						onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\',{H:23,m:59,s:59})}',maxDate:'#F{$dp.$D(\'beginTime\',{d:9,H:23,m:59,s:59})}'})" --%>
								onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',onpicked:function(){getenddate();},minDate:'#F{$dp.$D(\'beginTime\',{H:23,m:59,s:59})}',maxDate:'%y-%M-{%d-1} 23:59:59'})"
								readonly class="Wdate input-text" style="width: 132px;" />
							<span style="color:red;width:100px;font-family:华文中宋;" id="dateError"></span>
						</td>
					</tr>
					<tr><td align="right">评估方案：</td><td>方案评估1(三步法) <input type="radio" id="planType1" name="planType" value="ONE"　checked="checked"/>&nbsp;&nbsp;&nbsp;&nbsp;方案评估2(两步法) <input type="radio" id="planType2" name="planType" value="TWO"/></td></tr>
					<tr><td align="right">收敛方式：</td><td>方案一(根据Top差值比例) <input type="radio" id="useEriData" name="schemeType" value="ONE"　checked="checked"/>&nbsp;&nbsp;&nbsp;&nbsp;方案二(根据求方差) <input type="radio" id="useHwData" name="schemeType" value="TWO"/></td></tr>
					<tr><td align="right">关联度分子：</td><td><select name="cosi" id="cosi">
		<option value="timestotal">timestotal</option>
		<option value="RSRPtimes0">RSRPtimes0</option>
		<option value="RSRPtimes1">RSRPtimes1</option>
		<option value="RSRPtimes2">RSRPtimes2</option>
		<option value="RSRPtimes3">RSRPtimes3</option>
		<option value="RSRPtimes4">RSRPtimes4</option>
</select></td></tr>
				</tbody>
			</table>
			<div style="margin: 27px"></div>
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

 		//评估方案
 		if($("#planTypeSession").val()=="ONE"){
 			$("#planType1").attr("checked","checked");
 		}else if($("#converTypeSession").val()=="TWO"){
 			$("#planType2").attr("checked","checked");
 		}else{
			$("#planType1").attr("checked","checked");
 		}

 		//收敛方式
 		if($("#converTypeSession").val()=="ONE"){
 			$("#useEriData").attr("checked","checked");
 		}else if($("#converTypeSession").val()=="TWO"){
 			$("#useHwData").attr("checked","checked");
 		}else{
			$("#useEriData").attr("checked","checked");
 		}

 		//关联度分子
 		if($("#cosiSession").val() != "") {
 			$("#cosi").val($("#cosiSession").val());
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
