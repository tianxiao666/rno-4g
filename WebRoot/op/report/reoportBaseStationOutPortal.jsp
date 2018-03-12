<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE>
<html>
<head>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="../../css/base.css"/>
<link rel="stylesheet" type="text/css" href="../../css/public.css"/>
<link rel="stylesheet" type="text/css" href="../css/leftMenu.css"></script>
<link rel="stylesheet" type="text/css" href="css/statements.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/dialog/dialog.css"/>
<style type="text/css">
.statements_classify{float:left;}
.statements_select1{ float:right;margin: 10px;}
.statements_back{background:url("images/statements_back.png") no-repeat; width:16px; height:16px; display:inline-block; cursor:pointer;margin: 0 3px;}
</style>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/highcharts.src.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/exporting.src.js"></script>
<script type="text/javascript" src="js/report.js"></script>
<script type="text/javascript">
var thisDataP = {thistitle:"",thissubtitle:"",thisyAxisTitle2:"",thisyAxisTitle:"",thiscategories:"",thisseries:""};

var thisDataB = {thistitle:"",thissubtitle:"",thisyAxisTitle2:"",thisyAxisTitle:"",thiscategories:"",thisseries:""};

var thisorgId = null;
var isO = false;
var thisorgName = "";
var thisData = "";
	$(function(){
				var aDate = new Date();
				var year=aDate.getFullYear();
				var month=aDate.getMonth();
				var spanMonth = "";
				if(month == 0){
					month = 12;
					year = year - 1;
				}
				year = parseInt(year);
				month = parseInt(month);
				if(month < 10){
					spanMonth = "0"+month;
				}else{
					spanMonth = month;
				}
				dates = getFirstAndLastMonthDay(year, month);
				getbaseStationOutByOrg(dates[0],dates[1]);
	});
	
	

function getbaseStationOutByOrg(beginTime,endTime){
		$("#columnBasicDiv").html("<div  style=\"text-align: center;  padding-top: 20px;\" valt=\"Char\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
		$(".clearfix li").removeClass("selected");
		$("#selected").addClass("selected");
		var url = "getbaseStationOutByOrgAction";
		var countext = "";
		var categories = new Array();
		var series = "";
		var pName = "";
		var baseStationCount = "{ name: '基站退服率 (‰)', data: [";
			var params = {beginTime:beginTime,endTime:endTime};
				$.post(url, params, function(data){
				var i = 0;	
				$.each(data, function(key, value){
					countext = countext+"<span id='"+value.orgName+"'>"+value.orgId+"</span>";
					categories[i]=value.orgName;
					var bsOut = 0;
					if(value.baseStationOut && value.baseStationOut != 0 && value.baseStationCount && value.baseStationCount != 0){
						bsOut = parseFloat(value.baseStationOut)/parseFloat(value.baseStationCount)*1000;
					}
					bsOut = bsOut + "";
					if(bsOut.indexOf(".") >= 0){
						bsOut = bsOut.substring(0,bsOut.indexOf(".") + 3);
					}
					baseStationCount = baseStationCount + bsOut + ",";
					pName = value.pName;
					i++;
				});
				if(baseStationCount != null && baseStationCount != ""){
					baseStationCount = baseStationCount.substring(0,baseStationCount.length - 1);
				}
				baseStationCount = baseStationCount + "]},";
				if(baseStationCount != null && baseStationCount != ""){
					baseStationCount = baseStationCount.substring(0,baseStationCount.length - 1);
				}
				series = eval("[" +baseStationCount + "]");
				$("#di").html(countext);
				var title = "每千基站退服率统计("+pName + ","+$("#DateTimeSpan").text()+")";
				thisDataB.thistitle = title;
				thisDataB.thissubtitle = "(点击图形区域,可向下钻取)";
				thisDataB.thisyAxisTitle = "基站退服率 (‰)";
				thisDataB.thiscategories = categories;
				thisDataB.thisseries = series;
				columnBasic(title,"(点击图形区域,可向下钻取)","基站退服率 (‰)","columnBasicDiv",categories,series);
				//getTable(data,"项目");
			},'json');
			//getReportComment("reoportBaseStationOut", "org", orgId, beginTime);
}



//计算年月
function getYearMonth(year, month){
	if(month > 12){
		month = 1;
		year = year +1;
	}
	if(month < 1){
		month = 12;
		year = year -1;
	}
	var dates= new Array(); //定义一数组;
	dates[0] = year;
   dates[1] = month;
   return dates;
}

//获取当前时间的月份的第一天与最后一天的时间
function getFirestAndLastToMonthDay(){
	var aDate = new Date();
	var year=aDate.getFullYear();
	var month=aDate.getMonth()+1;
	var dates= new Array(); //定义一数组;
	//根据年月获取月第一个天与最后一天
	dates = getFirstAndLastMonthDay(year, month);
	return dates;
}

//根据年月获取月第一个天与最后一天
function getFirstAndLastMonthDay(year, month){  
   var   firstdate = year + '-' + month + '-01 00:00:00';
   var  day = new Date(year,month,0); 
   var lastdate = year + '-' + month + '-' + day.getDate() + ' 23:59:59';//获取当月最后一天日期  
   var dates= new Array(); //定义一数组;
   dates[0] = firstdate;
   dates[1] = lastdate;
   return dates;
}


function showAlert(){
	alert("没有可比数据");
}

</script>
</head>
<body>
	<div class="clearfix">
		<div class="statements_img_half">
		<em class="st_full_screen" onclick="addDiv('columnBasicDiv');"></em>
			<div id="columnBasicDiv"  style="height: 200px; margin: 0 auto"></div>
		</div>
	</div>
	<input type="hidden" id="DateTimeSpan"/>
	<div id="ireportB" style="height: 500px; width: 1000px; display: none;"></div>
</body>
