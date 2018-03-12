<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>区域PCI翻频方案参数信息</title>

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
.errorTip {
	color: red;
	width: 100px;
	font-family: 华文中宋;
}

.paramTable {
	margin: auto;
	padding: auto;
	width: 100%;
	border:8px;
}

.paramTable th {
	padding: 0px 10px;
	background: #25B4C3;
	color: #444;
	white-space: nowrap;
	border: #a7a7a7 2px solid;
	line-height: 30px;
	font-size: 14px;

}

.paramTable tr td {
	border: 2px solid #CCC;
	padding: 4px;
	color: #000;
	line-height: 21px;
	vertical-align: middle;
	background: WhiteSmoke;
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
  	<font style="font-weight: bold;">当前位置： 干扰计算&gt; 新建区域PCI翻频方案任务</font>
	<br>
	<center>
	  	<a href="initLteInterferCalcPageAction" style="text-decoration: underline;font-weight: bold;">&lt;&lt;返回任务列表</a><br>
	  	 <font style="font-weight: bold;">任务信息>><font style="color: #31FF81;">参数配置</font>>>提交任务</font>
	  	<br>
  	</center>

  <%--<input type="hidden" id="ncsAreaCoverage" value="<s:property value="#session.MRTASKINFO.ncsInfo.ncsAreaCoverage"/>"/>
	<input type="hidden" id="ncsIds" value="<s:property value="#session.MRTASKINFO.ncsInfo.ncsIds"/>"/>  --%>

	<%-- 隐藏域 参数 start --%>
<%--	<input type="hidden" id="sameFreqInterThresholdDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.sameFreqInterThreshold"/>"/>
	<input type="hidden" id="overShootingIdealDisMultipleDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.overShootingIdealDisMultiple"/>"/>
	<input type="hidden" id="betweenCellIdealDisMultipleDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.betweenCellIdealDisMultiple"/>"/>
	<input type="hidden" id="cellCheckTimesIdealDisMultipleDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.cellCheckTimesIdealDisMultiple"/>"/>
	<input type="hidden" id="cellCheckTimesSameFreqInterThresholdDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.cellCheckTimesSameFreqInterThreshold"/>"/>
	<input type="hidden" id="cellIdealDisReferenceCellNumDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.cellIdealDisReferenceCellNum"/>"/>
	<input type="hidden" id="gsm900CellFreqNumDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.gsm900CellFreqNum"/>"/>
	<input type="hidden" id="gsm1800CellFreqNumDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.gsm1800CellFreqNum"/>"/>
	<input type="hidden" id="gsm900CellIdealCapacityDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.gsm900CellIdealCapacity"/>"/>
	<input type="hidden" id="gsm1800CellIdealCapacityDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.gsm1800CellIdealCapacity"/>"/>
	<input type="hidden" id="dlCoverMinimumSignalStrengthThresholdDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.dlCoverMinimumSignalStrengthThreshold"/>"/>
	<input type="hidden" id="ulCoverMinimumSignalStrengthThresholdDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.ulCoverMinimumSignalStrengthThreshold"/>"/>
	<input type="hidden" id="interFactorMostDistantDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.interFactorMostDistant"/>"/>
	<input type="hidden" id="interFactorSameAndAdjFreqMinimumThresholdDefault" value="<s:property value="#session.MRTASKINFO.thresholdDefault.interFactorSameAndAdjFreqMinimumThreshold"/>"/>
	 --%>
	<%-- session --%>
<%--	<input type="hidden" id="sameFreqInterThresholdSession" value="<s:property value="#session.MRTASKINFO.threshold.sameFreqInterThreshold"/>"/>
	<input type="hidden" id="overShootingIdealDisMultipleSession" value="<s:property value="#session.MRTASKINFO.threshold.overShootingIdealDisMultiple"/>"/>
	<input type="hidden" id="betweenCellIdealDisMultipleSession" value="<s:property value="#session.MRTASKINFO.threshold.betweenCellIdealDisMultiple"/>"/>
	<input type="hidden" id="cellCheckTimesIdealDisMultipleSession" value="<s:property value="#session.MRTASKINFO.threshold.cellCheckTimesIdealDisMultiple"/>"/>
	<input type="hidden" id="cellCheckTimesSameFreqInterThresholdSession" value="<s:property value="#session.MRTASKINFO.threshold.cellCheckTimesSameFreqInterThreshold"/>"/>
	<input type="hidden" id="cellIdealDisReferenceCellNumSession" value="<s:property value="#session.MRTASKINFO.threshold.cellIdealDisReferenceCellNum"/>"/>
	<input type="hidden" id="gsm900CellFreqNumSession" value="<s:property value="#session.MRTASKINFO.threshold.gsm900CellFreqNum"/>"/>
	<input type="hidden" id="gsm1800CellFreqNumSession" value="<s:property value="#session.MRTASKINFO.threshold.gsm1800CellFreqNum"/>"/>
	<input type="hidden" id="gsm900CellIdealCapacitySession" value="<s:property value="#session.MRTASKINFO.threshold.gsm900CellIdealCapacity"/>"/>
	<input type="hidden" id="gsm1800CellIdealCapacitySession" value="<s:property value="#session.MRTASKINFO.threshold.gsm1800CellIdealCapacity"/>"/>
	<input type="hidden" id="dlCoverMinimumSignalStrengthThresholdSession" value="<s:property value="#session.MRTASKINFO.threshold.dlCoverMinimumSignalStrengthThreshold"/>"/>
	<input type="hidden" id="ulCoverMinimumSignalStrengthThresholdSession" value="<s:property value="#session.MRTASKINFO.threshold.ulCoverMinimumSignalStrengthThreshold"/>"/>
	<input type="hidden" id="interFactorMostDistantSession" value="<s:property value="#session.MRTASKINFO.threshold.interFactorMostDistant"/>"/>
	<input type="hidden" id="interFactorSameAndAdjFreqMinimumThresholdSession" value="<s:property value="#session.MRTASKINFO.threshold.interFactorSameAndAdjFreqMinimumThreshold"/>"/>
	--%>
	<%-- 隐藏域 参数 end --%>

	<div style="text-align:center">
    	<div style="width: 700px;margin: 20px auto;">
		    <table style="width:500px;margin: 5px auto;">
			    <tr>
				    <td style="text-align:center"><input type="button" id="paramInfoPreStep" name="" value="<上一步 "/></td>
				    <td style="text-align:center"><input type="button" id="paramInfoNextStep" name="" value="下一步 >"/></td>
			    </tr>
		    </table>

			<table id="structureParamTable" class="paramTable" >
				<tr>
					<th style="width: 30%">
						参数名
					</th>
					<th style="width: 30%">
						默认值（点击修改）
					</th>
					<th style="width: 40%">
						含义
					</th>
				</tr>
				<s:iterator value="#session.MRTASKINFO.groupThresholds" id="groupThreshold">
					<tr>
					    <td>
					    	<s:iterator value="value" id="threshold" status="u">
							  	 <s:if test="!#u.last">
							  	 	<s:property value="#threshold.descInfo" /></br>且</br>
							  	 </s:if>
							     <s:else>
							     	<s:property value="#threshold.descInfo" />
							     </s:else>
							</s:iterator>
						</td>
						<td style="width: 30%">
					    	<s:iterator value="value" id="threshold" status="u">
						    	 <s:if test="!#u.last">
								  	<div id="<s:property value="#threshold.code" />"  class="editbox" style="height: 23px;">
						    			<s:property value="#threshold.defaultVal" />
						    		</div>
						    		<span id="<s:property value="#threshold.code" />" class="errorTip"></span>
						    		<br/>
							  	 </s:if>
							     <s:else>
							     	<div id="<s:property value="#threshold.code" />"  class="editbox" style="height: 23px;">
						    			<s:property value="#threshold.defaultVal" /></div>
						    			<span id="<s:property value="#threshold.code" />" class="errorTip"></span>
							     </s:else>
							</s:iterator>
						</td>
						<td>
					    	<s:iterator value="value" id="threshold" status="u">
							  	 <s:if test="!#u.last">
							  	 	<s:property value="#threshold.scopeDesc" /></br>>且</br>
							  	 </s:if>
							     <s:else>
							     	<s:property value="#threshold.scopeDesc" />
							     </s:else>
							</s:iterator>
						</td>
					</tr>
				</s:iterator>
				<%--  <s:iterator value="#session.MRTASKINFO.rnoThresholds" id="rnoThreshold">
				<tr>
					<td><s:property value="#rnoThreshold.descInfo" /></td>
				<td id="<s:property value="#rnoThreshold.code" />"  class="editbox" style="text-align: center">
						<s:property value="#rnoThreshold.defaultVal" />
					</td>
  				<td>
					 <span id="<s:property value="#rnoThreshold.code" />" class="errorTip"></span>
						<s:property value="#rnoThreshold.scopeDesc" />
					</td>
					</tr>
  				</s:iterator> --%>
				<%--
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.sameFreqInterThresholdDescInfo" />
					</td>
					<td id="sameFreqInterThreshold"  class="editbox" style="text-align: center">
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.sameFreqInterThreshold" />
					</td>
					<td>
					 <span id="sameFreqInterThreshold" class="errorTip"></span>
						大于等于该值，认为是强相关。 需大于0。
					</td>
				</tr>
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.overShootingIdealDisMultipleDescInfo" />
					</td>
					<td id="overShootingIdealDisMultiple" style="text-align: center" class="editbox" >
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.overShootingIdealDisMultiple" />
					</td>
					<td>
					<span id="overShootingIdealDisMultiple" class="errorTip"></span>
						需大于等于1。
					</td>
				</tr>
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.betweenCellIdealDisMultipleDescInfo" />
					</td>
					<td id="betweenCellIdealDisMultiple" style="text-align: center" class="editbox" >
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.betweenCellIdealDisMultiple" />
					</td>
					<td>
					<span id="betweenCellIdealDisMultiple" class="errorTip"></span>
						需大于等于1。
					</td>
				</tr>
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.cellCheckTimesIdealDisMultipleDescInfo" />
					</td>
					<td id="cellCheckTimesIdealDisMultiple" style="text-align: center" class="editbox" >
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.cellCheckTimesIdealDisMultiple" />
					</td>
					<td>
					<span id="cellCheckTimesIdealDisMultiple" class="errorTip"></span>
						需大于等于1。
					</td>
				</tr>
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.cellCheckTimesSameFreqInterThresholdDescInfo" />
					</td>
					<td id="cellCheckTimesSameFreqInterThreshold" style="text-align: center" class="editbox" >
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.cellCheckTimesSameFreqInterThreshold" />
					</td>
					<td>
					<span id="cellCheckTimesSameFreqInterThreshold" class="errorTip"></span>
						大于等于该值，需大于0。
					</td>
				</tr>
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.cellIdealDisReferenceCellNumDescInfo" />
					</td>
					<td id="cellIdealDisReferenceCellNum" style="text-align: center" class="editbox" >
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.cellIdealDisReferenceCellNum" />
					</td>
					<td>
					<span id="cellIdealDisReferenceCellNum" class="errorTip"></span>
						取3个强相关小区作为考虑。需要正整数，且小于10。
					</td>
				</tr>
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.gsm900CellFreqNumDescInfo" />
					</td>
					<td id="gsm900CellFreqNum" style="text-align: center" class="editbox" >
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.gsm900CellFreqNum" />
					</td>
					<td>
					<span id="gsm900CellFreqNum" class="errorTip"></span>
						需大于等于94
					</td>
				</tr>
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.gsm1800CellFreqNumDescInfo" />
					</td>
					<td id="gsm1800CellFreqNum" style="text-align: center" class="editbox" >
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.gsm1800CellFreqNum" />
					</td>
					<td>
					<span id="gsm1800CellFreqNum" class="errorTip"></span>
						需大于等于124
					</td>
				</tr>
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.gsm900CellIdealCapacityDescInfo" />
					</td>
					<td id="gsm900CellIdealCapacity" style="text-align: center" class="editbox" >
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.gsm900CellIdealCapacity" />
					</td>
					<td>
					<span id="gsm900CellIdealCapacity" class="errorTip"></span>
						900M小区理想容量=TCH容量+BCCH容量=67/12+1=6.58≈6。 需大于0。
					</td>
				</tr>
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.gsm1800CellIdealCapacityDescInfo" />
					</td>
					<td id="gsm1800CellIdealCapacity" style="text-align: center" class="editbox" >
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.gsm1800CellIdealCapacity" />
					</td>
					<td>
					<span id="gsm1800CellIdealCapacity" class="errorTip"></span>
						1800M小区理想容量=TCH容量+BCCH容量=96/12+1=9。 需大于0。
					</td>
				</tr>
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.dlCoverMinimumSignalStrengthThresholdDescInfo" />
					</td>
					<td id="dlCoverMinimumSignalStrengthThreshold" style="text-align: center" class="editbox" >
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.dlCoverMinimumSignalStrengthThreshold" />
					</td>
					<td>
					<span id="dlCoverMinimumSignalStrengthThreshold" class="errorTip"></span>
						大于等于该值。
					</td>
				</tr>
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.ulCoverMinimumSignalStrengthThresholdDescInfo" />
					</td>
					<td id="ulCoverMinimumSignalStrengthThreshold" style="text-align: center" class="editbox" >
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.ulCoverMinimumSignalStrengthThreshold" />
					</td>
					<td>
					<span id="ulCoverMinimumSignalStrengthThreshold" class="errorTip"></span>
						大于等于该值。
					</td>
				</tr>
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.interFactorMostDistantDescInfo" />
					</td>
					<td id="interFactorMostDistant" style="text-align: center" class="editbox" >
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.interFactorMostDistant" />
					</td>
					<td>
					<span id="interFactorMostDistant" class="errorTip"></span>
						小于该值。公里为单位。需大于0。
					</td>
				</tr>
				<tr>
					<td>
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.interFactorSameAndAdjFreqMinimumThresholdDescInfo" />
					</td>
					<td id="interFactorSameAndAdjFreqMinimumThreshold" style="text-align: center" class="editbox">
						<s:property
							value="#session.MRTASKINFO.thresholdDefault.interFactorSameAndAdjFreqMinimumThreshold" />
					</td>
					<td>
					<span id="interFactorSameAndAdjFreqMinimumThreshold" class="errorTip"></span>
						大于该值。需大于0。
					</td>
				</tr>
				--%>
			</table>
		</div>
		</div>
  </body>
<script  type="text/javascript" >
 	/*	var sameFreqInterThresholdSession = $("#sameFreqInterThresholdSession").val();
 		var overShootingIdealDisMultipleSession = $("#overShootingIdealDisMultipleSession").val();
 		var betweenCellIdealDisMultipleSession = $("#betweenCellIdealDisMultipleSession").val();
 		var cellCheckTimesIdealDisMultipleSession = $("#cellCheckTimesIdealDisMultipleSession").val();
 		var cellCheckTimesSameFreqInterThresholdSession = $("#cellCheckTimesSameFreqInterThresholdSession").val();
 		var cellIdealDisReferenceCellNumSession = $("#cellIdealDisReferenceCellNumSession").val();
 		var gsm900CellFreqNumSession = $("#gsm900CellFreqNumSession").val();
 		var gsm1800CellFreqNumSession = $("#gsm1800CellFreqNumSession").val();
 		var gsm900CellIdealCapacitySession = $("#gsm900CellIdealCapacitySession").val();
 		var gsm1800CellIdealCapacitySession = $("#gsm1800CellIdealCapacitySession").val();
 		var dlCoverMinimumSignalStrengthThresholdSession = $("#dlCoverMinimumSignalStrengthThresholdSession").val();
 		var ulCoverMinimumSignalStrengthThresholdSession = $("#ulCoverMinimumSignalStrengthThresholdSession").val();
 		var interFactorMostDistantSession = $("#interFactorMostDistantSession").val();
 		var interFactorSameAndAdjFreqMinimumThresholdSession = $("#interFactorSameAndAdjFreqMinimumThresholdSession").val();
 		*/
 		//console.log(sameFreqInterThresholdSession+"  "+overShootingIdealDisMultipleSession );

 		//优先读取session的值，session值不存在读取初始值
 		/* if(sameFreqInterThresholdSession != ""
 			&& overShootingIdealDisMultipleSession != ""
 			&& betweenCellIdealDisMultipleSession != ""
 			&& cellCheckTimesIdealDisMultipleSession != ""
 			&& cellCheckTimesSameFreqInterThresholdSession != ""
 			&& cellIdealDisReferenceCellNumSession != ""
 			&& gsm900CellFreqNumSession != ""
 			&& gsm1800CellFreqNumSession != ""
 			&& gsm900CellIdealCapacitySession != ""
 			&& gsm1800CellIdealCapacitySession != ""
 			&& dlCoverMinimumSignalStrengthThresholdSession != ""
 			&& ulCoverMinimumSignalStrengthThresholdSession != ""
 			&& interFactorMostDistantSession != ""
 			&& interFactorSameAndAdjFreqMinimumThresholdSession != "") {

 		 	$("#sameFreqInterThreshold").html(sameFreqInterThresholdSession);
 		 	$("#overShootingIdealDisMultiple").html(overShootingIdealDisMultipleSession);
 		 	$("#betweenCellIdealDisMultiple").html(betweenCellIdealDisMultipleSession);
 		 	$("#cellCheckTimesIdealDisMultiple").html(cellCheckTimesIdealDisMultipleSession);
 		 	$("#cellCheckTimesSameFreqInterThreshold").html(cellCheckTimesSameFreqInterThresholdSession);
 		 	$("#cellIdealDisReferenceCellNum").html(cellIdealDisReferenceCellNumSession);
 		 	$("#gsm900CellFreqNum").html(gsm900CellFreqNumSession);
 		 	$("#gsm1800CellFreqNum").html(gsm1800CellFreqNumSession);
 		 	$("#gsm900CellIdealCapacity").html(gsm900CellIdealCapacitySession);
 		 	$("#gsm1800CellIdealCapacity").html(gsm1800CellIdealCapacitySession);
 		 	$("#dlCoverMinimumSignalStrengthThreshold").html(dlCoverMinimumSignalStrengthThresholdSession);
 		 	$("#ulCoverMinimumSignalStrengthThreshold").html(ulCoverMinimumSignalStrengthThresholdSession);
 		 	$("#interFactorMostDistant").html(interFactorMostDistantSession);
 		 	$("#interFactorSameAndAdjFreqMinimumThreshold").html(interFactorSameAndAdjFreqMinimumThresholdSession);
 		} */

 </script>
</html>
