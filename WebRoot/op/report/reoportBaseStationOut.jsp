<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>故障抢修</title>
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

var TimeString = "";
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
				$("#hiddenYear").val(year);
				$("#hiddenMonth").val(month);
				thisorgName = $("#orgName").val();
				year = parseInt(year);
				month = parseInt(month);
				if(month < 10){
					spanMonth = "0"+month;
				}else{
					spanMonth = month;
				}
				dates = getFirstAndLastMonthDay(year, month);
				$("#hiddenbeginTime").val(dates[0]);
				$("#hiddenendTime").val(dates[1]);
				$("#DateTimeSpan").text(year+"年"+spanMonth+"月");
				var orgId = $("#orgId").val();
				thisorgId = $("#orgId").val();
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
				getTable(data,"项目");
			},'json');
			var orgId = $("#orgId").val();
			thisorgId = $("#orgId").val();
			getReportComment("reoportBaseStationOut", "org", orgId, beginTime);
}


function getbaseStationOutByOrgId(orgId,beginTime,endTime){
		$("#columnBasicDiv").html("<div  style=\"text-align: center;   padding-top: 20px;\" valt=\"Char\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
		$(".clearfix li").removeClass("selected");
		$("#selected").addClass("selected");
		var url = "getbaseStationOutByOrgIdAction";
		var countext = "";
		var categories = new Array();
		var series = "";
		var pName = "";
		var baseStationCount = "{ name: '基站退服率 (‰)', data: [ ";
			var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
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
				getTable(data,"项目");
			},'json');
			thisorgId = orgId;
			getReportComment("reoportBaseStationOut", "org", orgId, beginTime);
}

function getTable(data,Oname){
	//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr>"
								+"<th style='width:55px;'>"+Oname+"</th>"
								+"<th>基站退服率 (‰)</th>"
							+"</tr>";
		var bsouts = 0;
		$.each(data, function(key, value){
			var name = "";
			if(value.areaName){
				name = value.areaName;
			}else{
				name = value.orgName;
			}
					var bsOut = 0;
					if(value.baseStationOut && value.baseStationOut != 0 && value.baseStationCount && value.baseStationCount != 0){
						bsOut = parseFloat(value.baseStationOut)/parseFloat(value.baseStationCount)*1000;
					}
					bsouts = bsouts + bsOut;
					bsOut = bsOut + "";
					if(bsOut.indexOf(".") >= 0){
						bsOut = bsOut.substring(0,bsOut.indexOf(".") + 3);
					}
				counttext = counttext + "<tr>"
								+"<td>"+name+"</td>"
								+"<td>"+bsOut+"</td>"
							"</tr>";
				});	
		bsouts = bsouts + "";
					if(bsouts.indexOf(".") >= 0){
						bsouts = bsouts.substring(0,bsouts.indexOf(".") + 3);
					}
		counttext = counttext + "<tr>"
								+"<td>汇总</td>"
								+"<td>"+bsouts+"</td>"
							"</tr>";
		counttext = counttext + "</table>";	
		$("#stationTableDiv").html(counttext);			
}


//获取上一个月的故障抢修工单数量
function getlastMonth(){
	var year = $("#hiddenYear").val();
	var month = $("#hiddenMonth").val();
	$(".hitarea-div_close").removeClass("hitarea-div_open");
	$("#stationTableDiv").hide();
	year = parseInt(year);
	month = parseInt(month);
	month = month - 1;
	var dates= new Array(); //定义一数组;
	dates = getYearMonth(year, month);
	$("#hiddenYear").val(dates[0]);
	$("#hiddenMonth").val(dates[1]);
	var spanMonth = "";
	if(dates[1] < 10){
		spanMonth = "0"+dates[1];
	}else{
		spanMonth = dates[1];
	}
	//根据年月获取月第一个天与最后一天
	lastMonthDates = getFirstAndLastMonthDay(dates[0], dates[1]);
	var beginTime = lastMonthDates[0];
	var endTime = lastMonthDates[1];
	$("#hiddenbeginTime").val(beginTime);
	$("#hiddenendTime").val(endTime);
	$("#DateTimeSpan").text(dates[0]+"年"+spanMonth+"月");
	clickGet();
	
}

//获取下一个月的故障抢修工单数量
function getNextMonth(){
	var year = $("#hiddenYear").val();
	var month = $("#hiddenMonth").val();
	$(".hitarea-div_close").removeClass("hitarea-div_open");
	$("#stationTableDiv").hide();
	year = parseInt(year);
	month = parseInt(month);
	month = month + 1;
	var dates= new Array(); //定义一数组;
	dates = getYearMonth(year, month);
	$("#hiddenYear").val(dates[0]);
	$("#hiddenMonth").val(dates[1]);
	var spanMonth = "";
	if(dates[1] < 10){
		spanMonth = "0"+dates[1];
	}else{
		spanMonth = dates[1];
	}
	//根据年月获取月第一个天与最后一天
	lastMonthDates = getFirstAndLastMonthDay(dates[0], dates[1]);
	var beginTime = lastMonthDates[0];
	var endTime = lastMonthDates[1];
	$("#hiddenbeginTime").val(beginTime);
	$("#hiddenendTime").val(endTime);
	$("#DateTimeSpan").text(dates[0]+"年"+spanMonth+"月");
	//点击确定时根据TAB的显示隐藏获取故障抢修工单数量
	clickGet();
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


//TAB切换是按组织加载故障工单数量
function getReportByorginst(me){
	$(".hitarea-div_close").removeClass("hitarea-div_open");
	$(".tab_menu ul li").removeClass("ontab");
	$(me).addClass("ontab");
	$("#orgName").val(thisorgName);
	var beginTime = $("#hiddenbeginTime").val();
	var endTime = $("#hiddenendTime").val();
	$("#statements_back").hide();
	//判断按组织TAB是否为显示
	if($(me).text().replace(" ","") == "按组织"){
		$("#Ring").show();
		$("#Period").show();
		var orgId = $("#orgId").val();
		$("#returnOrgId").val(orgId);
		getbaseStationOutByOrgId(orgId,beginTime,endTime);
	}if($(me).text().replace(" ","") == "按项目"){
		//var orgId = $("#orgId").val();
		//getStationByArea(orgId,beginTime,endTime);
		$("#Ring").hide();
		$("#Period").hide();
		var orgId = $("#orgId").val();
		$("#returnOrgId").val(orgId);
		getbaseStationOutByOrgIdProject(orgId,beginTime,endTime);
	}
	
	$("#bizunitinstDiv").show();
	$("#stationTypeDiv").hide();
	//获取故障抢修工单数量
}



//TAB切换是按组织加载故障工单数量
function getReportS(){
	$("#orgName").val(thisorgName);
	var beginTime = $("#hiddenbeginTime").val();
	var endTime = $("#hiddenendTime").val();
	//判断按组织TAB是否为显示
	if($(".ontab").text().replace(" ","") == "按组织"){
		var orgId = $("#orgId").val();
		getbaseStationOutByOrgId(thisorgId,beginTime,endTime);
	}if($(".ontab").text().replace(" ","") == "按项目"){
		//var orgId = $("#orgId").val();
		//getStationByArea(orgId,beginTime,endTime);
		var orgId = $("#orgId").val();
		//$("#returnOrgId").val(orgId);
		getbaseStationOutByOrgIdProject(thisorgId,beginTime,endTime);
	}
	
	$("#bizunitinstDiv").show();
	$("#stationTypeDiv").hide();
	//获取故障抢修工单数量
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
function clickShowMore(divId,name){
	$("#orgName").val(divId);
	var beginTime = $("#hiddenbeginTime").val();
	var endTime = $("#hiddenendTime").val();
	thisorgId = $("#"+divId).text();
	if(thisorgId != null && thisorgId != ""){
			$("#returnOrgId").val(thisorgId);
	}
	isO = true;
	if($(".ontab").text().replace(" ","") == "按组织"){
			$("#statements_back").show();
			getbaseStationOutByOrgId(thisorgId,beginTime,endTime);
	}if($(".ontab").text().replace(" ","") == "按项目"){
			//getStationByAreaId(thisorgId,beginTime,endTime);
			
	}
}

function clickGetUrgentRepairworkorderReport(){
	var beginTime = $("#hiddenbeginTime").val();
	var endTime = $("#hiddenendTime").val();
	if(isO == true){
		if($(".ontab").text().replace(" ","") == "按组织"){
			//getStationByOrgId(thisorgId,beginTime,endTime);
			getReportComment("reoportBaseStationOut", "org", thisorgId, beginTime);
			if($(".selected").text().replace(" ","") == "对比"){
					getReportComment("reoportBaseStationOut", "org", thisorgId, beginTime);
				}else if($(".selected").text().replace(" ","") == "同比"){
					getReportComment("reoportBaseStationOutPeriod", "org", thisorgId, beginTime);
				}else if($(".selected").text().replace(" ","") == "环比"){
					getReportComment("reoportBaseStationOutRing", "org", thisorgId, beginTime);
			}
		}if($(".ontab").text().replace(" ","") == "按项目"){
			//getStationByAreaId(thisorgId,beginTime,endTime);
			getReportComment("reoportBaseStationOutProject", "org", thisorgId, beginTime);
		}
	}else{
		if($(".ontab").text().replace(" ","") == "按组织"){
			//getStationByOrgId(thisorgId,beginTime,endTime);
			getReportComment("reoportBaseStationOut", "org", thisorgId, beginTime);
			if($(".selected").text().replace(" ","") == "对比"){
					getReportComment("reoportBaseStationOut", "org", thisorgId, beginTime);
				}else if($(".selected").text().replace(" ","") == "同比"){
					getReportComment("reoportBaseStationOutPeriod", "org", thisorgId, beginTime);
				}else if($(".selected").text().replace(" ","") == "环比"){
					getReportComment("reoportBaseStationOutRing", "org", thisorgId, beginTime);
			}
		}if($(".ontab").text().replace(" ","") == "按项目"){
			//getStationByAreaId(thisorgId,beginTime,endTime);
			getReportComment("reoportBaseStationOutProject", "org", thisorgId, beginTime);
		}
	}
}

function clickGet(){
	var beginTime = $("#hiddenbeginTime").val();
	var endTime = $("#hiddenendTime").val();
	if(isO == true){
		if($(".ontab").text().replace(" ","") == "按组织"){
			//getStationByOrgId(thisorgId,beginTime,endTime);
			//getReportComment("reoportBaseStationOut", "Org", thisorgId, beginTime);
			if($(".selected").text().replace(" ","") == '对比'){
				getbaseStationOutByOrgId(thisorgId,beginTime,endTime);
			}if($(".selected").text().replace(" ","") == '同比'){
				getUPeriod();
			}if($(".selected").text().replace(" ","") == '环比'){
				getURing();
			}
		}if($(".ontab").text().replace(" ","") == "按项目"){
			//getStationByAreaId(thisorgId,beginTime,endTime);
			//getReportComment("reoportBaseStationOut", "Area", thisorgId, beginTime);
			getbaseStationOutByOrgIdProject(thisorgId,beginTime,endTime);
		}
	}else{
		if($(".ontab").text().replace(" ","") == "按组织"){
			var orgId = $("#orgId").val();
			//getStationByOrg(orgId,beginTime,endTime);
			//getReportComment("reoportBaseStationOut", "Org", orgId, beginTime);
			if($(".selected").text().replace(" ","") == '对比'){
				getbaseStationOutByOrgId(thisorgId,beginTime,endTime);
			}if($(".selected").text().replace(" ","") == '同比'){
				getUPeriod();
			}if($(".selected").text().replace(" ","") == '环比'){
				getURing();
			}
		}if($(".ontab").text().replace(" ","") == "按项目"){
			var orgId = $("#orgId").val();
			getbaseStationOutByOrgIdProject(thisorgId,beginTime,endTime);
			//getStationByArea(orgId,beginTime,endTime);
			//getReportComment("reoportBaseStationOut", "Area", orgId, beginTime);
		}
	}
}


function clickClearFix(me){
   		$("#compareTab li").removeClass("selected");
   		$(me).addClass("selected");
   }
   
function showAlert(){
	alert("没有可比数据");
}



function getUrgentRepairPeriod(orgId,orgName,beginTime,endTime,TimeString){
	var url = "getbaseStationOutByOrgIdAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
	 $.ajax({
          type: "POST",                                         //ajax的方式为post(get方式对传送数据长度有限制)
          url: url,           //一般处理程序页面AddUser.ashx(在2中会写出该页面内容)
		  cache : false, 
          async : false,
          dataType: "json",     
	 data: params,       //要传送的数据键值对adduserName为键（方便2中的文件用此名称接受数据）txtuserName为值（要传递的变量，例如用户名）
     success: function (data) {
	var baseStationOut = 0;
	var baseStationCount = 0;
	var size = 0;
	var rData = "";
	var bsOut = 0;
	$.each(data, function(key, value){
					//if(value.baseStationOut && value.baseStationOut != 0 ){
					//	baseStationOut = baseStationOut + parseFloat(value.baseStationOut);
					//}
					if(value.baseStationOut && value.baseStationOut != 0 && value.baseStationCount && value.baseStationCount != 0 ){
						//baseStationCount = baseStationCount + parseFloat(value.baseStationCount);
						bsOut = bsOut + parseFloat(value.baseStationOut)/parseFloat(value.baseStationCount)*1000;
					}
					size++;
	});
	//if(baseStationOut && baseStationOut != 0 && baseStationCount && baseStationCount != 0){
	//	bsOut = parseFloat(baseStationOut)/parseFloat(baseStationCount)*1000;
	//}
		bsOut = bsOut + "";
		if(bsOut.indexOf(".") >= 0){
			bsOut = bsOut.substring(0,bsOut.indexOf(".") + 3);
		}
		rData = "{'bsOut':"+bsOut+",'orgId':"+orgId+",'TimeString':'"+TimeString+"'},";
		thisData = thisData + rData;
	}
	});
}
	
function getUPeriod(){
		thisData = "";
		var orgId = thisorgId;
		var orgName = $("#orgName").val();
		var year = $("#hiddenYear").val();
		var month = $("#hiddenMonth").val();
		year = parseInt(year);
		month = parseInt(month);
		var beginTime = $("#hiddenbeginTime").val();
		for(var i = 5;i >= 0;i--){
			var yeart = year - i;
			var al = getYMTime(yeart,month);
			getUrgentRepairPeriod(orgId,orgName,al[0],al[1],al[2]);
		}
		if(thisData != null && thisData != ""){
			thisData = thisData.substring(0,thisData.length - 1);
		}
		
		thisData = eval("["+thisData+"]");
		getReportComment("reoportBaseStationOutPeriod", "org", orgId, beginTime);
		var series = "";
		var size = 0;
		var categories= new Array(); //定义一数组;	
		var bsOut = "{ type: 'line',name: '基站退服率 (‰)' ,color: '#336600', data: [ ";
		$.each(thisData, function(key, value){
			categories[size] = value.TimeString;
			bsOut = bsOut + value.bsOut +  ",";
			size++;
		});
		if(bsOut != null && bsOut != ""){
			bsOut = bsOut.substring(0,bsOut.length -1);
		}
		bsOut = bsOut + "]}";
		var orgName = $("#orgName").val();
		var title2 = "每千基站退服率统计("+orgName+",同比分析)";
		series = "[" + bsOut + "]";
		series = eval(series);
		thisDataP.thistitle = title2;
		thisDataP.thissubtitle = "";
		thisDataP.thisyAxisTitle = "基站退服率 (‰)";
		thisDataP.thisyAxisTitle2 = "";
		thisDataP.thiscategories = categories;
		thisDataP.thisseries = series;
		columnAndSplineBasicReportBaseStationOut(title2,"",'基站退服率 (‰)','',"columnBasicDiv",categories,series);
}


function getURing(){
		thisData = "";
		var orgId = thisorgId;
		var orgName = $("#orgName").val();
		var year = $("#hiddenYear").val();
		var month = $("#hiddenMonth").val();
		year = parseInt(year);
		month = parseInt(month);
		var beginTime = $("#hiddenbeginTime").val();
		var arr = new Array();
		arr[5] = year+","+month;
		for(var i = 4;i >= 0;i--){
			var montht = month - 1;
			if( month == 1){
				montht = month;
				month = 12;
				year = year - 1;
			}else{
				month--;
			}
			arr[i] = year+","+month;
		}
		if(arr != null){
			for(var i = 0;i < 6;i++){
				var de = arr[i].split(",");
				var al = getYMTime(de[0],de[1]);
				getUrgentRepairPeriod(orgId,orgName,al[0],al[1],al[2]);
			};
		}
		if(thisData != null && thisData != ""){
			thisData = thisData.substring(0,thisData.length - 1);
		}
		thisData = eval("["+thisData+"]");
		getReportComment("reoportBaseStationOutRing", "org", orgId, beginTime);
		var series = "";
		var size = 0;
		var categories= new Array(); //定义一数组;	
		var bsOut = "{ type: 'line',name: '基站退服率 (‰)' ,color: '#336600', data: [ ";
		$.each(thisData, function(key, value){
			categories[size] = value.TimeString;
			bsOut = bsOut + value.bsOut +  ",";
			size++;
		});
		if(bsOut != null && bsOut != ""){
			bsOut = bsOut.substring(0,bsOut.length -1);
		}
		bsOut = bsOut + "]}";
		var orgName = $("#orgName").val();
		var title2 = "每千基站退服率统计("+orgName+",环比分析)";
		series = "[" + bsOut + "]";
		series = eval(series);
		thisDataP.thistitle = title2;
		thisDataP.thissubtitle = "";
		thisDataP.thisyAxisTitle = "基站退服率 (‰)";
		thisDataP.thisyAxisTitle2 = "";
		thisDataP.thiscategories = categories;
		thisDataP.thisseries = series;
		columnAndSplineBasicReportBaseStationOut(title2,"",'基站退服率 (‰)','',"columnBasicDiv",categories,series);
	
}

function clickGetUrgentRepairPeriod(){
	thisData = "";
	$("#statements_classify").hide();
	//判断按组织TAB是否为显示
	if($(".ontab").text().replace(" ","") == "按组织"){
		getUPeriod();
	}
}

function clickGetUrgentRepairRing(){
	thisData = "";
	$("#statements_classify").hide();
	//判断按组织TAB是否为显示
	if($(".ontab").text().replace(" ","") == "按组织"){
		getURing();
	}
}



function clickReturnReport(){
	var returnOrgId = $("#returnOrgId").val();
	//alert(returnOrgId);
	var thisOrgId = $("#thisOrgId").val();
	if(thisOrgId == returnOrgId){
		$("#statements_back").hide();
		return;
	}
	//开始时间
	var beginTime = $("#hiddenbeginTime").val();
	//结束时间
	var endTime = $("#hiddenendTime").val();
	if($(".selected").text().replace(" ","") == "对比"){
		//$("#uradiodiv").show();
		//clickGetUrgentRepairworkorderStatisticsGROUPBY(type,text);	
		getbaseStationOutByOrgIdTopOrg(returnOrgId,beginTime,endTime);
	}else if($(".selected").text().replace(" ","") == "同比"){
		getUPeriodTopOrg(returnOrgId);
	}else if($(".selected").text().replace(" ","") == "环比"){
		getURingTopOrg(returnOrgId);
	}
	
	
}




function getbaseStationOutByOrgIdTopOrg(orgId,beginTime,endTime){
		$("#columnBasicDiv").html("<div  style=\"text-align: center;   padding-top: 20px;\" valt=\"Char\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
		$(".clearfix li").removeClass("selected");
		$("#selected").addClass("selected");
		var url = "getbaseStationOutByOrgIdTopOrgAction";
		var countext = "";
		var categories = new Array();
		var series = "";
		var pName = "";
		var pOrgId = "";
		var baseStationCount = "{ name: '基站退服率 (‰)', data: [ ";
			var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
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
					pOrgId = value.pOrgId;
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
				thisDataB.thisyAxisTitle2 = "";
				thisDataB.thiscategories = categories;
				thisDataB.thisseries = series;
				columnBasic(title,"(点击图形区域,可向下钻取)","基站退服率 (‰)","columnBasicDiv",categories,series);
				getTable(data,"项目");
				thisorgId = orgId;
				getReportComment("reoportBaseStationOut", "org", pOrgId, beginTime);
				$("#orgName").val(pName);
				$("#orgId").val(pOrgId);
				$("#returnOrgId").val(pOrgId);
				thisorgId = pOrgId;
				var returnOrgId = $("#returnOrgId").val();
				var thisOrgId1 = $("#thisOrgId").val();
				if(thisOrgId1 == pOrgId){
						$("#statements_back").hide();
				}
			},'json');
			//getReportComment("reoportBaseStationOut", "org", orgId, beginTime);
}


	
function getUPeriodTopOrg(returnOrgId){
		thisData = "";
		var orgId = returnOrgId;
		var orgName = $("#orgName").val();
		var year = $("#hiddenYear").val();
		var month = $("#hiddenMonth").val();
		var pOrgId = 0;
		var pOrgName = "";
		year = parseInt(year);
		month = parseInt(month);
		var beginTime = $("#hiddenbeginTime").val();
		for(var i = 5;i >= 0;i--){
			var yeart = year - i;
			var al = getYMTime(yeart,month);
			getUrgentRepairPeriodTopOrg(orgId,orgName,al[0],al[1],al[2]);
		}
		if(thisData != null && thisData != ""){
			thisData = thisData.substring(0,thisData.length - 1);
		}
		thisData = eval("["+thisData+"]");
		getReportComment("reoportBaseStationOutPeriod", "org", orgId, beginTime);
		var series = "";
		var size = 0;
		var categories= new Array(); //定义一数组;	
		var bsOut = "{ type: 'line',name: '基站退服率 (‰)' ,color: '#336600', data: [ ";
		$.each(thisData, function(key, value){
			pOrgId = value.pOrgId;
			pOrgName = value.pName;
			categories[size] = value.TimeString;
			bsOut = bsOut + value.bsOut +  ",";
			size++;
		});
		if(bsOut != null && bsOut != ""){
			bsOut = bsOut.substring(0,bsOut.length -1);
		}
		bsOut = bsOut + "]}";
		var orgName = $("#orgName").val();
		var title2 = "每千基站退服率统计("+pOrgName+",同比分析)";
		$("#returnOrgId").val(pOrgId);
		thisorgId = pOrgId;
		$("#orgName").val(pOrgName);
		var returnOrgId = $("#returnOrgId").val();
		var thisOrgId = $("#thisOrgId").val();
		if(thisOrgId == returnOrgId){
				$("#statements_back").hide();
		}
		series = "[" + bsOut + "]";
		series = eval(series);
		thisDataP.thistitle = title2;
		thisDataP.thissubtitle = "";
		thisDataP.thisyAxisTitle = "基站退服率 (‰)";
		thisDataP.thisyAxisTitle2 = "";
		thisDataP.thiscategories = categories;
		thisDataP.thisseries = series;
		columnAndSplineBasicReportBaseStationOut(title2,"",'基站退服率 (‰)','',"columnBasicDiv",categories,series);
}


function getURingTopOrg(returnOrgId){
		thisData = "";
		var orgId = returnOrgId;
		var orgName = $("#orgName").val();
		var year = $("#hiddenYear").val();
		var month = $("#hiddenMonth").val();
		var pOrgId = 0;
		var pOrgName = "";
		year = parseInt(year);
		month = parseInt(month);
		var beginTime = $("#hiddenbeginTime").val();
		var arr = new Array();
		arr[5] = year+","+month;
		for(var i = 4;i >= 0;i--){
			var montht = month - 1;
			if( month == 1){
				montht = month;
				month = 12;
				year = year - 1;
			}else{
				month--;
			}
			arr[i] = year+","+month;
		}
		if(arr != null){
			for(var i = 0;i < 6;i++){
				var de = arr[i].split(",");
				var al = getYMTime(de[0],de[1]);
				getUrgentRepairPeriodTopOrg(orgId,orgName,al[0],al[1],al[2]);
			};
		}
		if(thisData != null && thisData != ""){
			thisData = thisData.substring(0,thisData.length - 1);
		}
		thisData = eval("["+thisData+"]");
		getReportComment("reoportBaseStationOutRing", "org", orgId, beginTime);
		var series = "";
		var size = 0;
		var categories= new Array(); //定义一数组;	
		var bsOut = "{ type: 'line',name: '基站退服率 (‰)' ,color: '#336600', data: [ ";
		$.each(thisData, function(key, value){
		pOrgId = value.pOrgId;
		pOrgName = value.pName;
			categories[size] = value.TimeString;
			bsOut = bsOut + value.bsOut +  ",";
			size++;
		});
		if(bsOut != null && bsOut != ""){
			bsOut = bsOut.substring(0,bsOut.length -1);
		}
		bsOut = bsOut + "]}";
		var orgName = $("#orgName").val();
		var title2 = "每千基站退服率统计("+pOrgName+",环比分析)";
		$("#returnOrgId").val(pOrgId);
		thisorgId = pOrgId;
		$("#orgName").val(pOrgName);
		var returnOrgId = $("#returnOrgId").val();
		var thisOrgId = $("#thisOrgId").val();
		if(thisOrgId == returnOrgId){
				$("#statements_back").hide();
		}
		series = "[" + bsOut + "]";
		series = eval(series);
		thisDataP.thistitle = title2;
		thisDataP.thissubtitle = "";
		thisDataP.thisyAxisTitle = "基站退服率 (‰)";
		thisDataP.thisyAxisTitle2 = "";
		thisDataP.thiscategories = categories;
		thisDataP.thisseries = series;
		columnAndSplineBasicReportBaseStationOut(title2,"",'基站退服率 (‰)','',"columnBasicDiv",categories,series);
	
}



function getUrgentRepairPeriodTopOrg(orgId,orgName,beginTime,endTime,TimeString){
	var url = "getbaseStationOutByOrgIdTopOrgAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
	 $.ajax({
          type: "POST",                                         //ajax的方式为post(get方式对传送数据长度有限制)
          url: url,           //一般处理程序页面AddUser.ashx(在2中会写出该页面内容)
		  cache : false, 
          async : false,
          dataType: "json",     
	 data: params,       //要传送的数据键值对adduserName为键（方便2中的文件用此名称接受数据）txtuserName为值（要传递的变量，例如用户名）
     success: function (data) {
	var baseStationOut = 0;
	var baseStationCount = 0;
	var size = 0;
	var rData = "";
	var bsOut = 0;
	var pOrgId = 0;
	var pOrgName = "";
	$.each(data, function(key, value){
		pOrgId = value.pOrgId;
		pOrgName = value.pName;
					//if(value.baseStationOut && value.baseStationOut != 0 ){
					//	baseStationOut = baseStationOut + parseFloat(value.baseStationOut);
					//}
					if(value.baseStationOut && value.baseStationOut != 0 && value.baseStationCount && value.baseStationCount != 0 ){
						//baseStationCount = baseStationCount + parseFloat(value.baseStationCount);
						bsOut = bsOut + parseFloat(value.baseStationOut)/parseFloat(value.baseStationCount)*1000;
					}
					size++;
	});
	//if(baseStationOut && baseStationOut != 0 && baseStationCount && baseStationCount != 0){
	//	bsOut = parseFloat(baseStationOut)/parseFloat(baseStationCount)*1000;
	//}
		bsOut = bsOut + "";
		if(bsOut.indexOf(".") >= 0){
			bsOut = bsOut.substring(0,bsOut.indexOf(".") + 3);
		}
		rData = "{'bsOut':"+bsOut+",'orgId':"+orgId+",'TimeString':'"+TimeString+"','pOrgId':"+pOrgId+",'pName':'"+pOrgName+"'},";
		thisData = thisData + rData;
	}
	});
}


//显示报表表格
	function showOrHideStationTableDiv(me){
		if($(me).html() == "<em>[+]</em>展开表格"){
			$(me).html("<em>[-]</em>展开表格");
			$("#stationTableDiv").show();
		}else{
			$(me).html("<em>[+]</em>展开表格");
			$("#stationTableDiv").hide();
		}
	}
	
	
	
	 function addDiv(divId){
		 	if($(".ontab").text().replace(" ","") == "按组织"){
				if($(".selected").text().replace(" ","") == "对比"){
					columnBasic(thisDataB.thistitle,thisDataB.thissubtitle,thisDataB.thisyAxisTitle,"ireportB",thisDataB.thiscategories,thisDataB.thisseries);
				}else if($(".selected").text().replace(" ","") == "同比"){
					columnAndSplineBasicReportBaseStationOut(thisDataP.thistitle,thisDataP.thissubtitle,thisDataP.thisyAxisTitle,thisDataP.thisyAxisTitle2,"ireportB",thisDataP.thiscategories,thisDataP.thisseries);
				}else if($(".selected").text().replace(" ","") == "环比"){
					columnAndSplineBasicReportBaseStationOut(thisDataP.thistitle,thisDataP.thissubtitle,thisDataP.thisyAxisTitle,thisDataP.thisyAxisTitle2,"ireportB",thisDataP.thiscategories,thisDataP.thisseries);
				}
			}if($(".ontab").text().replace(" ","") == "按项目"){
				if($(".selected").text().replace(" ","") == "对比"){
					columnBasic(thisDataB.thistitle,thisDataB.thissubtitle,thisDataB.thisyAxisTitle,"ireportB",thisDataB.thiscategories,thisDataB.thisseries);
				}else if($(".selected").text().replace(" ","") == "同比"){
					columnAndSplineBasicReportBaseStationOut(thisDataP.thistitle,thisDataP.thissubtitle,thisDataP.thisyAxisTitle,thisDataP.thisyAxisTitle2,"ireportB",thisDataP.thiscategories,thisDataP.thisseries);
				}else if($(".selected").text().replace(" ","") == "环比"){
					columnAndSplineBasicReportBaseStationOut(thisDataP.thistitle,thisDataP.thissubtitle,thisDataP.thisyAxisTitle,thisDataP.thisyAxisTitle2,"ireportB",thisDataP.thiscategories,thisDataP.thisseries);
				}
			}
			setTimeout("addDivChar();",700);
   	}      
   
   function addDivChar(){
   	var report_statements_full = document.getElementById("report_statements_full_newNode");
	  // 	window.top.document.body.removeChild(report_statements_full); 
   		if(report_statements_full != null){
   		}else{
   		var chartext = $("#ireportB").html();
	   		 var newNode = document.createElement("div");
	   		 newNode.id = "report_statements_full_newNode";
	   		var context = "<div id='report_statements_full' style='display: block; z-index: 2000; padding:20px; margin-left: -500px; top: 100px; left: 50%; background-color:#333; border-radius: 15px; position: absolute;'>"
							+"<div class='statements_full_info' id='report_statements_full_info' style='background-color:#fff; width:1000px; height:500px;'>"
							+chartext
						+"</div>"
						+"<div onclick='window.top.document.body.removeChild(window.top.document.getElementById(\"report_statements_full_newNode\"));' class='statements_full_close' style='background-color: #333; border-radius: 16px; color: #fff; font-family: Helvetica,STHeiti; font-size: 34px; height: 32px; line-height: 32px; position: absolute; right: -10px; text-align: center; top: -10px; width: 32px; cursor:pointer;'>×</div>"
					+"</div>";
			newNode.innerHTML = context;
			window.top.document.body.appendChild(newNode);
   		}
   }
   
   

function getbaseStationOutByOrgIdProject(orgId,beginTime,endTime){
		$("#columnBasicDiv").html("<div  style=\"text-align: center;   padding-top: 20px;\" valt=\"Char\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
		$(".clearfix li").removeClass("selected");
		$("#selected").addClass("selected");
		var url = "getbaseStationOutByOrgIdProjectAction";
		var countext = "";
		var categories = new Array();
		var series = "";
		var pName = "";
		var baseStationCount = "{ name: '基站退服率 (‰)', data: [ ";
			var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
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
				getTable(data,"项目");
			},'json');
			thisorgId = orgId;
			getReportComment("reoportBaseStationOutProject", "org", orgId, beginTime);
}


 
var columnAndSplineBasicReportBaseStationOut2;	
		//柱状图+折线(对比)
		function columnAndSplineBasicReportBaseStationOut(title,subtitle,yAxisTitle,yAxisTitle2,divId,categories,series){
       	 columnAndSplineBasicReportBaseStationOut2 = new Highcharts.Chart({
            chart: {
                renderTo: divId,
                zoomType: 'line'
            },
            title: {
                text: title
            },
            subtitle: {
                text: subtitle
            },
            xAxis: {
                categories: categories
                ,
                labels: {
                    rotation: -60,
                    align: 'right',
                    style: {
                        fontSize: '12px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: [{ // Primary yAxis
                labels: {
                    formatter: function() {
                        return this.value;
                    },
                    style: {
                        color: '#336600'
                    }
                },
                title: {
                    text: yAxisTitle,
                    style: {
                        color: '#336600'
                    }
                },
                opposite: true
    
            }, { // Secondary yAxis
                gridLineWidth: 0,
                title: {
                    text: yAxisTitle2,
                    style: {
                    }
                },
                labels: {
                    formatter: function() {
                        return this.value;
                    },
                    style: {
                    }
                }
    
            }],
            
            tooltip: {
                	formatter: function() {
                    var unit = {
                        '故障处理平均历时': '小时',
                        '故障处理及时率': '%'
                    }[this.series.name];
     				var yvar = this.y +"";
					if(yvar.indexOf(".") >= 0){
						yvar = yvar.substring(0,yvar.indexOf(".") + 3);
					}
                    return ''+
                        this.x +':'+this.series.name+':'+ yvar;
                }
                 
            },
            plotOptions: {
                column: {
                	dataLabels: {
                        enabled: true
                    },
                    pointPadding: 0.2,
                    borderWidth: 0,
                    point: {
                        events: {
                            click: function(e) {
                            }
                        }
                    }
                },
                line: {
                    dataLabels: {
                        enabled: true
                    }
                }
            },
                series: series
        });
		}  
		
		
		
function getYMTime(year,month){
	year = parseInt(year);
	month = parseInt(month);
	var dates= new Array(); //定义一数组;
	dates = getYearMonth(year, month);
	var spanMonth = "";
	if(dates[1] < 10){
		spanMonth = "0"+dates[1];
	}else{
		spanMonth = dates[1];
	}
	var lastMonthDates= new Array(); //定义一数组;
	//根据年月获取月第一个天与最后一天
	lastMonthDates = getFirstAndLastMonthDay(dates[0], dates[1]);
	lastMonthDates[2] = dates[0]+"年"+spanMonth+"月";
	return lastMonthDates;
	}      
</script>
</head>
<body>
<%--主体开始--%>
<div id="statements_content">
   <input type="hidden" value="${orgId }" id="orgId"/>
   <input type="hidden" value="${orgName }" id="orgName"/>
   <input type="hidden" value="" id="hiddenbeginTime"/>
	<input type="hidden" value="" id="hiddenendTime"/>
	<%--右边报表开始--%>
	<div class="statements_right">
	    <div class="statements_menu_title clearfix">
		    <p class="fl">故障抢修工单统计</p>
			<p class="fr">
			   <span title="指标说明" class="text-icon explain"></span>
			   <span title="报表评论" class="text-icon comment_list comment_dialog_show"></span>
			   <span title="导出EXCEL" class="text-icon excel"></span>
			   <span title="自定义查询" class="text-icon search show_report_settings"></span>
			</p>
		</div>
		
		<div class="statements_search_date">
			<p>&lt;周期&gt;</p>
			<span class="time_tool_left" onclick="getlastMonth();"></span>
			<span id="DateTimeSpan"></span><input type="hidden" id="hiddenYear" value=""/><input type="hidden" id="hiddenMonth" value=""/>
			<span class="time_tool_right" onclick="getNextMonth();"></span>
		</div>
		
		<div id="compareTab" class="compareTab_menu">
			<ul class="clearfix">
				<li id="selected" class="selected" onclick="getReportS();clickClearFix(this);">对比</li>
				<li onclick="clickClearFix(this);clickGetUrgentRepairRing();"  title="不同统计周期的趋势分析(通常是按月)" id="Ring">环比</li>
				<li onclick="clickClearFix(this);clickGetUrgentRepairPeriod();" title="不同年度的同期趋势分析" id="Period">同比</li>
			</ul>
		</div>
		<div class="compare_info">
			<div id="compareTab_0">
				<div class="statements_tab">
					<div class="tab_menu">
						<ul>
							<li class="ontab first_tab" onclick="getReportByorginst(this);">按组织</li>
							<%-- <li onclick="getReportByorginst(this);">按地域</li> --%>
							<li onclick="getReportByorginst(this);">按项目</li>
						</ul>
					</div>
					<div class="tab_container">
						<div class="tab_content">
							<div class="clearfix">
								<div class="statements_select1">
									<span class="statements_back" style="display:none" id="statements_back" title="返回上级" onclick="clickReturnReport();"></span>
									<input type="hidden" id="returnOrgId" value="${orgId }" />
									<input type="hidden" id="thisOrgId" value="${orgId }" />
									<select>
										<option selected="" value="全部">全部</option>
									</select>
								</div>
						</div>
						</div>
							<div class="clearfix">
								<div class="statements_img_half">
								<em class="st_full_screen" onclick="addDiv('columnBasicDiv');"></em>
									<div id="columnBasicDiv"></div>
								</div>
							</div>
							<div class="statements_table_top" style="height: 26px;" id="statements_table_top">
								<span style="float: left;"><a onclick="showOrHideStationTableDiv(this);"><em>[+]</em>展开表格</a></span>
							</div>
							<%--表单位置--%>
							<div class="statements_main_table" id="stationTableDiv" style="display: none;">
								
							</div>
							<%--报表评论--%>
							<div class="statements_comment">
								<div id="reportComment">
								</div>
							</div>
						</div>
						<div class="tab_content" style="display:none;">
							<%--报表位置--%>
							<div class="statements_img">按地域</div>
						</div>
						<div class="tab_content" style="display:none;">
							<%--报表位置--%>
							<div class="statements_img">按项目</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%--右边报表结束--%>
</div>
<div id="di" style="display: none;"></div>
<%--主体结束--%>
<div id="ireportB" style="height: 500px; width: 1000px; display: none;"></div>
</body>
</html>
