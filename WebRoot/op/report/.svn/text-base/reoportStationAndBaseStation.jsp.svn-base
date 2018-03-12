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
<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css" />
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/highcharts.src.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/exporting.src.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<style type="text/css">
.statements_classify{float:left;}
.statements_select1{ float:right;margin: 10px;}
.statements_back{background:url("images/statements_back.png") no-repeat; width:16px; height:16px; display:inline-block; cursor:pointer;margin: 0 3px;}
</style>
<script type="text/javascript">
var thisDataP = {thistitle:"",thissubtitle:"",thisyAxisTitle2:"",thisyAxisTitle:"",thiscategories:"",thisseries:""};

var thisDataB = {thistitle:"",thissubtitle:"",thisyAxisTitle2:"",thisyAxisTitle:"",thiscategories:"",thisseries:""};


var thisorgId = null;
var isO = false;
	$(function(){
				var aDate = new Date();
				var year=aDate.getFullYear();
				var month=aDate.getMonth();
				$("#hiddenYear").val(year);
				$("#hiddenMonth").val(month);
				year = parseInt(year);
				month = parseInt(month);
				var spanMonth = "";
				if(month == 0){
					month = 12;
					year = year - 1;
				}
				if(month < 10){
					spanMonth = "0"+month;
				}else{
					spanMonth = month;
				}
				dates = getFirstAndLastMonthDay(year, month);
				$("#hiddenbeginTime").val(dates[0]);
				$("#hiddenendTime").val(dates[1]);
				//$("#DateTimeSpan").text(year+"年"+spanMonth+"月");
				var orgId = $("#orgId").val();
				getStationByOrg(orgId,dates[0],dates[1]);
	});
	
	
function getStationByArea(orgId,beginTime,endTime){
		$("#columnBasicDiv").html("<div  style=\"text-align: center;  padding-top: 20px;\" valt=\"Char\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
		var url = "getStationByAreaAction";
		var countext = "";
		var categories = new Array();
		var series = "";
		var pName = "";
		var station = "{ name: '站址', data: [ ";
		var gsmB = "{ name: 'GSM基站', data: [ ";
		var tdB = "{ name: 'TD基站', data: [ ";
			var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
				$.post(url, params, function(data){
				var i = 0;	
				$.each(data, function(key, value){
					countext = countext+"<span id='"+value.areaName+"'>"+value.areaId+"</span>";
					var planTitle = "";
					if(value.areaName.length > 8 && value.areaName != null){
						var parseIndex = Math.ceil(value.areaName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.areaName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.areaName;
					}
					categories[i]=planTitle;
					station = station + value.Station + ",";
					gsmB = gsmB + value.BaseStation_GSM + ",";
					tdB = tdB + value.BaseStation_TD + ",";
					pName = value.pName;
					i++;
				});
				if(station != null && station != ""){
					station = station.substring(0,station.length - 1);
				}
				if(gsmB != null && gsmB != ""){
					gsmB = gsmB.substring(0,gsmB.length - 1);
				}
				if(tdB != null && tdB != ""){
					tdB = tdB.substring(0,tdB.length - 1);
				}
				station = station + "]},";
				gsmB = gsmB + "]},";
				tdB = tdB + "]}";
				series = eval("[" +station + gsmB + tdB + "]");
				$("#di").html(countext);
				//var title = "站址/基站资源数量统计("+pName + ","+$("#DateTimeSpan").text()+")";
				var title = "站址/基站资源数量统计("+pName + ")";
				thisDataB.thistitle = title;
				thisDataB.thissubtitle = "(点击图形区域,可向下钻取)";
				thisDataB.thisyAxisTitle = "";
				thisDataB.thisyAxisTitle2 = "";
				thisDataB.thiscategories = categories;
				thisDataB.thisseries = series;
				columnBasic(title,"(点击图形区域,可向下钻取)","","columnBasicDiv",categories,series);
				getTable(data,"区域");
			},'json');
			getReportComment("StationBaseStaion", "Area", orgId, beginTime);
}


function getStationByAreaId(areaId,beginTime,endTime){
		$("#columnBasicDiv").html("<div  style=\"text-align: center;   padding-top: 20px;\" valt=\"Char\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
		var url = "getStationByAreaIdAction";
		var countext = "";
		var categories = new Array();
		var series = "";
		var pName = "";
		var pId = "";
		var station = "{ name: '站址', data: [ ";
		var gsmB = "{ name: 'GSM基站', data: [ ";
		var tdB = "{ name: 'TD基站', data: [ ";
			var params = {areaId:areaId,beginTime:beginTime,endTime:endTime};
				$.post(url, params, function(data){
				var i = 0;	
				$.each(data, function(key, value){
					pId = value.pId;
					countext = countext+"<span id='"+value.areaName+"'>"+value.areaId+"</span>";
					var planTitle = "";
					if(value.areaName.length > 8 && value.areaName != null){
						var parseIndex = Math.ceil(value.areaName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.areaName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.areaName;
					}
					categories[i]=planTitle;
					station = station + value.Station + ",";
					gsmB = gsmB + value.BaseStation_GSM + ",";
					tdB = tdB + value.BaseStation_TD + ",";
					pName = value.pName;
					i++;
				});
				if(station != null && station != ""){
					station = station.substring(0,station.length - 1);
				}
				if(gsmB != null && gsmB != ""){
					gsmB = gsmB.substring(0,gsmB.length - 1);
				}
				if(tdB != null && tdB != ""){
					tdB = tdB.substring(0,tdB.length - 1);
				}
				station = station + "]},";
				gsmB = gsmB + "]},";
				tdB = tdB + "]}";
				series = eval("[" +station + gsmB + tdB + "]");
				$("#di").html(countext);
				//var title = "站址/基站资源数量统计("+pName + ","+$("#DateTimeSpan").text()+")";
				var title = "站址/基站资源数量统计("+pName + ")";
				thisDataB.thistitle = title;
				thisDataB.thissubtitle = "(点击图形区域,可向下钻取)";
				thisDataB.thisyAxisTitle = "";
				thisDataB.thisyAxisTitle2 = "";
				thisDataB.thiscategories = categories;
				thisDataB.thisseries = series;
				columnBasic(title,"(点击图形区域,可向下钻取)","","columnBasicDiv",categories,series);
				getTable(data,"区域");
				$("#returnAreaId").val(pId);
			},'json');
			getReportComment("StationBaseStaion", "Area", areaId, beginTime);
}


function getStationByOrg(orgId,beginTime,endTime){
		$("#columnBasicDiv").html("<div  style=\"text-align: center;  padding-top: 20px;\" valt=\"Char\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
		var url = "getStationByOrgAction";
		var countext = "";
		var categories = new Array();
		var series = "";
		var pName = "";
		var station = "{ name: '站址', data: [ ";
		var gsmB = "{ name: 'GSM基站', data: [ ";
		var tdB = "{ name: 'TD基站', data: [ ";
			var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
				$.post(url, params, function(data){
				var i = 0;	
				$.each(data, function(key, value){
					countext = countext+"<span id='"+value.orgName+"'>"+value.orgId+"</span>";
					var planTitle = "";
					if(value.orgName.length > 8 && value.orgName != null){
						var parseIndex = Math.ceil(value.orgName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.orgName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.orgName;
					}
					categories[i]=planTitle;
					station = station + value.Station + ",";
					gsmB = gsmB + value.BaseStation_GSM + ",";
					tdB = tdB + value.BaseStation_TD + ",";
					pName = value.pName;
					i++;
				});
				if(station != null && station != ""){
					station = station.substring(0,station.length - 1);
				}
				if(gsmB != null && gsmB != ""){
					gsmB = gsmB.substring(0,gsmB.length - 1);
				}
				if(tdB != null && tdB != ""){
					tdB = tdB.substring(0,tdB.length - 1);
				}
				station = station + "]},";
				gsmB = gsmB + "]},";
				tdB = tdB + "]}";
				series = eval("[" +station + gsmB + tdB + "]");
				$("#di").html(countext);
				//var title = "站址/基站资源数量统计("+pName + ","+$("#DateTimeSpan").text()+")";
				var title = "站址/基站资源数量统计("+pName + ")";
				thisDataB.thistitle = title;
				thisDataB.thissubtitle = "(点击图形区域,可向下钻取)";
				thisDataB.thisyAxisTitle = "";
				thisDataB.thisyAxisTitle2 = "";
				thisDataB.thiscategories = categories;
				thisDataB.thisseries = series;
				columnBasic(title,"(点击图形区域,可向下钻取)","","columnBasicDiv",categories,series);
				getTable(data,"项目");
			},'json');
			getReportComment("StationBaseStaion", "Org", orgId, beginTime);
}


function getStationByOrgId(orgId,beginTime,endTime){
		$("#columnBasicDiv").html("<div  style=\"text-align: center;   padding-top: 20px;\" valt=\"Char\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
		var url = "getStationByOrgIdAction";
		var countext = "";
		var categories = new Array();
		var series = "";
		var pName = "";
		var station = "{ name: '站址', data: [ ";
		var gsmB = "{ name: 'GSM基站', data: [ ";
		var tdB = "{ name: 'TD基站', data: [ ";
			var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
				$.post(url, params, function(data){
				var i = 0;	
				$.each(data, function(key, value){
					countext = countext+"<span id='"+value.orgName+"'>"+value.orgId+"</span>";
					var planTitle = "";
					if(value.orgName.length > 8 && value.orgName != null){
						var parseIndex = Math.ceil(value.orgName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.orgName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.orgName;
					}
					categories[i]=planTitle;
					station = station + value.Station + ",";
					gsmB = gsmB + value.BaseStation_GSM + ",";
					tdB = tdB + value.BaseStation_TD + ",";
					pName = value.pName;
					i++;
				});
				if(station != null && station != ""){
					station = station.substring(0,station.length - 1);
				}
				if(gsmB != null && gsmB != ""){
					gsmB = gsmB.substring(0,gsmB.length - 1);
				}
				if(tdB != null && tdB != ""){
					tdB = tdB.substring(0,tdB.length - 1);
				}
				station = station + "]},";
				gsmB = gsmB + "]},";
				tdB = tdB + "]}";
				series = eval("[" +station + gsmB + tdB + "]");
				$("#di").html(countext);
				//var title = "站址/基站资源数量统计("+pName + ","+$("#DateTimeSpan").text()+")";
				var title = "站址/基站资源数量统计("+pName + ")";
				thisDataB.thistitle = title;
				thisDataB.thissubtitle = "(点击图形区域,可向下钻取)";
				thisDataB.thisyAxisTitle = "";
				thisDataB.thisyAxisTitle2 = "";
				thisDataB.thiscategories = categories;
				thisDataB.thisseries = series;
				columnBasic(title,"(点击图形区域,可向下钻取)","","columnBasicDiv",categories,series);
				getTable(data,"项目");
			},'json');
			getReportComment("StationBaseStaion", "Org", orgId, beginTime);
}

function getTable(data,Oname){
	//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr>"
								+"<th style='width:55px;'>"+Oname+"</th>"
								+"<th>站址</th>"
								+"<th>GSM基站</th>"
								+"<th>TD基站</th>"
							+"</tr>";
		var Station = 0;
		var BaseStation_GSM = 0;
		var BaseStation_TD = 0;
		$.each(data, function(key, value){
			var name = "";
			if(value.areaName){
				name = value.areaName;
			}else{
				name = value.orgName;
			}
			counttext = counttext + "<tr>"
								+"<td>"+name+"</td>"
								+"<td>"+value.Station+"</td>"
								+"<td>"+value.BaseStation_GSM+"</td>"
								+"<td>"+value.BaseStation_TD+"</td>"
							"</tr>";
				Station = Station + parseInt(value.Station);
				BaseStation_GSM = BaseStation_GSM + parseInt(value.BaseStation_GSM);
				BaseStation_TD = BaseStation_TD + parseInt(value.BaseStation_TD);
				});	
		counttext = counttext + "<tr>"
								+"<td>汇总</td>"
								+"<td>"+Station+"</td>"
								+"<td>"+BaseStation_GSM+"</td>"
								+"<td>"+BaseStation_TD+"</td>"
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
	clickGetUrgentRepairworkorderReport();
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
	clickGetUrgentRepairworkorderReport();
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
	$("#statements_back").hide();
	$(me).addClass("ontab");
	var beginTime = $("#hiddenbeginTime").val();
	var endTime = $("#hiddenendTime").val();
	//判断按组织TAB是否为显示
	if($(me).text().replace(" ","") == "按组织"){
		$("#Ring").show();
		$("#Period").show();
		var orgId = $("#orgId").val();
		var thisOrgId = $("#thisOrgId").val();
		$("#returnOrgId").val(thisOrgId);
		getStationByOrg(orgId,beginTime,endTime);
	}if($(me).text().replace(" ","") == "按地域"){
		$("#Ring").show();
		$("#Period").show();
		var orgId = $("#orgId").val();
		var thisAreaId = $("#thisAreaId").val();
		$("#returnAreaId").val(thisAreaId);
		getStationByArea(orgId,beginTime,endTime);
	}if($(me).text().replace(" ","") == "按项目"){
		$("#Ring").hide();
		$("#Period").hide();
		var orgId = $("#orgId").val();
		var thisAreaId = $("#thisAreaId").val();
		$("#returnAreaId").val(thisAreaId);
		getStationByProject(orgId,beginTime,endTime);
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
	var beginTime = $("#hiddenbeginTime").val();
	var endTime = $("#hiddenendTime").val();
	
	isO = true;
	if($(".ontab").text().replace(" ","") == "按组织"){
			$("#statements_back").show();
			thisorgId = $("#"+divId).text();
			if(thisorgId != null && thisorgId != ""){
				$("#returnOrgId").val(thisorgId);
			}
			getStationByOrgId(thisorgId,beginTime,endTime);
	}if($(".ontab").text().replace(" ","") == "按地域"){
			$("#statements_back").show();
			thisorgId = $("#"+divId).text();
			if(thisorgId != null && thisorgId != ""){
				$("#returnAreaId").val(thisorgId);
			}
			getStationByAreaId(thisorgId,beginTime,endTime);
	}if($(".ontab").text().replace(" ","") == "按项目"){
			if(thisorgId != null && thisorgId != ""){
				$("#returnAreaId").val(thisorgId);
			}
			//getStationByProject(orgId,beginTime,endTime);
	}
	
}

function clickGetUrgentRepairworkorderReport(){
	var beginTime = $("#hiddenbeginTime").val();
	var endTime = $("#hiddenendTime").val();
	if(isO == true){
		if($(".ontab").text().replace(" ","") == "按组织"){
			//getStationByOrgId(thisorgId,beginTime,endTime);
			getReportComment("StationBaseStaion", "Org", thisorgId, beginTime);
		}if($(".ontab").text().replace(" ","") == "按地域"){
			//getStationByAreaId(thisorgId,beginTime,endTime);
			getReportComment("StationBaseStaion", "Area", thisorgId, beginTime);
		}if($(".ontab").text().replace(" ","") == "按项目"){
			//getStationByAreaId(thisorgId,beginTime,endTime);
			getReportComment("StationBaseStaionProject", "org", thisorgId, beginTime);
		}
	}else{
		if($(".ontab").text().replace(" ","") == "按组织"){
			var orgId = $("#orgId").val();
			//getStationByOrg(orgId,beginTime,endTime);
			getReportComment("StationBaseStaion", "Org", orgId, beginTime);
		}if($(".ontab").text().replace(" ","") == "按地域"){
			var orgId = $("#orgId").val();
			//getStationByArea(orgId,beginTime,endTime);
			getReportComment("StationBaseStaion", "Area", orgId, beginTime);
		}if($(".ontab").text().replace(" ","") == "按项目"){
			//getStationByAreaId(thisorgId,beginTime,endTime);
			getReportComment("StationBaseStaionProject", "org", thisorgId, beginTime);
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



function clickReturnReport(){
	
	//开始时间
	var beginTime = $("#hiddenbeginTime").val();
	//结束时间
	var endTime = $("#hiddenendTime").val();
	if($(".selected").text().replace(" ","") == "对比"){
		if($(".ontab").text().replace(" ","") == "按组织"){
			var returnOrgId = $("#returnOrgId").val();
			var thisOrgId = $("#thisOrgId").val();
			if(thisOrgId == returnOrgId){
				$("#statements_back").hide();
				return;
			}
			getStationByOrgIdAndTopOrg(returnOrgId,beginTime,endTime);
		}if($(".ontab").text().replace(" ","") == "按地域"){
			var returnAreaId = $("#returnAreaId").val();
			var thisAreaId = $("#thisAreaId").val();
			if(returnAreaId == thisAreaId){
				$("#statements_back").hide();
				return;
			}
			getStationByAreaIdAndTopOrg(returnAreaId,beginTime,endTime);
		}
	}else if($(".selected").text().replace(" ","") == "同比"){
		
	}else if($(".selected").text().replace(" ","") == "环比"){
		
	}
	
	
}


function getStationByAreaIdAndTopOrg(areaId,beginTime,endTime){
		$("#columnBasicDiv").html("<div  style=\"text-align: center;   padding-top: 20px;\" valt=\"Char\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
		var url = "getStationByAreaIdAndTopOrgAction";
		var countext = "";
		var categories = new Array();
		var series = "";
		var pName = "";
		var pId = "";
		var station = "{ name: '站址', data: [ ";
		var gsmB = "{ name: 'GSM基站', data: [ ";
		var tdB = "{ name: 'TD基站', data: [ ";
			var params = {areaId:areaId,beginTime:beginTime,endTime:endTime};
				$.post(url, params, function(data){
				var i = 0;	
				$.each(data, function(key, value){
					pId = value.pId;
					countext = countext+"<span id='"+value.areaName+"'>"+value.areaId+"</span>";
					var planTitle = "";
					if(value.areaName.length > 8 && value.areaName != null){
						var parseIndex = Math.ceil(value.areaName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.areaName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.areaName;
					}
					categories[i]=planTitle;
					station = station + value.Station + ",";
					gsmB = gsmB + value.BaseStation_GSM + ",";
					tdB = tdB + value.BaseStation_TD + ",";
					pName = value.pName;
					i++;
				});
				if(station != null && station != ""){
					station = station.substring(0,station.length - 1);
				}
				if(gsmB != null && gsmB != ""){
					gsmB = gsmB.substring(0,gsmB.length - 1);
				}
				if(tdB != null && tdB != ""){
					tdB = tdB.substring(0,tdB.length - 1);
				}
				station = station + "]},";
				gsmB = gsmB + "]},";
				tdB = tdB + "]}";
				series = eval("[" +station + gsmB + tdB + "]");
				$("#di").html(countext);
				//var title = "站址/基站资源数量统计("+pName + ","+$("#DateTimeSpan").text()+")";
				var title = "站址/基站资源数量统计("+pName + ")";
				thisDataB.thistitle = title;
				thisDataB.thissubtitle = "(点击图形区域,可向下钻取)";
				thisDataB.thisyAxisTitle = "基站退服率 (‰)";
				thisDataB.thiscategories = categories;
				thisDataB.thisseries = series;
				columnBasic(title,"(点击图形区域,可向下钻取)","","columnBasicDiv",categories,series);
				getTable(data,"区域");
				$("#returnAreaId").val(pId);
				var thisOrgId = $("#thisAreaId").val();
				if(thisOrgId == pId){
					$("#statements_back").hide();
				}
				getReportComment("StationBaseStaion", "Area", $("#returnAreaId").val(), beginTime);
			},'json');
}



function getStationByOrgIdAndTopOrg(orgId,beginTime,endTime){
		$("#columnBasicDiv").html("<div  style=\"text-align: center;   padding-top: 20px;\" valt=\"Char\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
		var url = "getStationByOrgIdAndTopOrgAction";
		var countext = "";
		var categories = new Array();
		var series = "";
		var pName = "";
		var pId = "";
		var station = "{ name: '站址', data: [ ";
		var gsmB = "{ name: 'GSM基站', data: [ ";
		var tdB = "{ name: 'TD基站', data: [ ";
			var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
				$.post(url, params, function(data){
				var i = 0;	
				$.each(data, function(key, value){
					pId = value.pId;
					countext = countext+"<span id='"+value.orgName+"'>"+value.orgId+"</span>";
					var planTitle = "";
					if(value.orgName.length > 8 && value.orgName != null){
						var parseIndex = Math.ceil(value.orgName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.orgName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.orgName;
					}
					categories[i]=planTitle;
					station = station + value.Station + ",";
					gsmB = gsmB + value.BaseStation_GSM + ",";
					tdB = tdB + value.BaseStation_TD + ",";
					pName = value.pName;
					i++;
				});
				station = station + "]},";
				gsmB = gsmB + "]},";
				tdB = tdB + "]}";
				series = eval("[" +station + gsmB + tdB + "]");
				$("#di").html(countext);
				//var title = "站址/基站资源数量统计("+pName + ","+$("#DateTimeSpan").text()+")";
				var title = "站址/基站资源数量统计("+pName + ")";
				thisDataB.thistitle = title;
				thisDataB.thissubtitle = "(点击图形区域,可向下钻取)";
				thisDataB.thisyAxisTitle = "基站退服率 (‰)";
				thisDataB.thiscategories = categories;
				thisDataB.thisseries = series;
				columnBasic(title,"(点击图形区域,可向下钻取)","","columnBasicDiv",categories,series);
				getTable(data,"项目");
				$("#returnOrgId").val(pId);
				var thisOrgId = $("#thisOrgId").val();
				if(thisOrgId == pId){
					$("#statements_back").hide();
				}
				getReportComment("StationBaseStaion", "Org", $("#returnOrgId").val(), beginTime);
			},'json');
}



function getStationByProject(orgId,beginTime,endTime){
		$("#columnBasicDiv").html("<div  style=\"text-align: center;   padding-top: 20px;\" valt=\"Char\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
		var url = "getStationByOrgIdProjectAction";
		var countext = "";
		var categories = new Array();
		var series = "";
		var pName = "";
		var pId = "";
		var station = "{ name: '站址', data: [ ";
		var gsmB = "{ name: 'GSM基站', data: [ ";
		var tdB = "{ name: 'TD基站', data: [ ";
			var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
				$.post(url, params, function(data){
				var i = 0;	
				$.each(data, function(key, value){
					pId = value.pId;
					countext = countext+"<span id='"+value.orgName+"'>"+value.orgId+"</span>";
					var planTitle = "";
					if(value.orgName.length > 8 && value.orgName != null){
						var parseIndex = Math.ceil(value.orgName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.orgName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.orgName;
					}
					categories[i]=planTitle;
					station = station + value.Station + ",";
					gsmB = gsmB + value.BaseStation_GSM + ",";
					tdB = tdB + value.BaseStation_TD + ",";
					pName = value.pName;
					i++;
				});
				if(station != null && station != ""){
					station = station.substring(0,station.length - 1);
				}
				if(gsmB != null && gsmB != ""){
					gsmB = gsmB.substring(0,gsmB.length - 1);
				}
				if(tdB != null && tdB != ""){
					tdB = tdB.substring(0,tdB.length - 1);
				}
				station = station + "]},";
				gsmB = gsmB + "]},";
				tdB = tdB + "]}";
				series = eval("[" +station + gsmB + tdB + "]");
				$("#di").html(countext);
				//var title = "站址/基站资源数量统计("+pName + ","+$("#DateTimeSpan").text()+")";
				var title = "站址/基站资源数量统计("+pName + ")";
				thisDataB.thistitle = title;
				thisDataB.thissubtitle = "(点击图形区域,可向下钻取)";
				thisDataB.thisyAxisTitle = "基站退服率 (‰)";
				thisDataB.thiscategories = categories;
				thisDataB.thisseries = series;
				columnBasic(title,"(点击图形区域,可向下钻取)","","columnBasicDiv",categories,series);
				getTable(data,"项目");
				$("#returnOrgId").val(pId);
				var thisOrgId = $("#thisOrgId").val();
				if(thisOrgId == pId){
					$("#statements_back").hide();
				}
			},'json');
			getReportComment("StationBaseStaionProject", "Org", orgId, beginTime);
}


//显示报表表格
	function showOrHideStationTableDiv(me){
		if($(me).html().replace(" ","") == "<em>[+]</em>展开表格"){
			$(me).html("<em>[-]</em>展开表格");
			$("#stationTableDiv").show();
		}else{
			$(me).html("<em>[+]</em>展开表格");
			$("#stationTableDiv").hide();
		}
	}
	
	
	 function addDiv(divId){
			columnBasic(thisDataB.thistitle,thisDataB.thissubtitle,thisDataB.thisyAxisTitle,"ireportB",thisDataB.thiscategories,thisDataB.thisseries);
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
   
   
   
var columnBasicChart;	
		//柱状图(对比)
		function columnBasic(title,subtitle,yAxisTitle,divId,categories,series){
       	 columnBasicChart = new Highcharts.Chart({
            chart: {
                renderTo: divId,
                type: 'column'
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
                    align: 'right',
                    style: {
                        fontSize: '12px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: yAxisTitle
                }
            },
            
            tooltip: {
                formatter: function() {
                    return ''+
                        this.x +':'+this.series.name+':'+ this.y+'';
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
                            	clickShowMore(categories[this.x],this.series.name);
                            }
                        }
                    }
                }
            },
                series: series
        });
		}
		
		
	function getReportComment(indicators, dimension, organizationId, statisticaltime){
   		var reportType = $("#reportType").text();
   		var params ={indicators:indicators,dimension:dimension,organizationId:organizationId,statisticaltime:statisticaltime};
   		var url = "getReportCommentAction";
   		$.post(url, params, function(data){
			$("#reportComment").html(data);
		});
   }
</script>
</head>
<body>
<%--主体开始--%>
<div id="statements_content">
   <input type="hidden" value="${orgId }" id="orgId"/>
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
		
		<div class="statements_search_date"  style="display: none;">
			<p>&lt;周期&gt;</p>
			<span class="time_tool_left" onclick="getlastMonth();"></span>
			<span id="DateTimeSpan"></span><input type="hidden" id="hiddenYear" value=""/><input type="hidden" id="hiddenMonth" value=""/>
			<span class="time_tool_right" onclick="getNextMonth();"></span>
		</div>
		
		<div id="compareTab" class="compareTab_menu">
			<ul class="clearfix" style="display: none;">
				<li id="selected" class="selected" onclick="clickClearFix(this);">对比</li>
				<li onclick="showAlert();" title="不同统计周期的趋势分析(通常是按月)" id="Ring">环比</li>
				<li onclick="showAlert();" title="不同年度的同期趋势分析" id="Period">同比</li>
			</ul>
		</div>
		<div class="compare_info">
			<div id="compareTab_0">
				<div class="statements_tab">
					<div class="tab_menu">
						<ul>
							<li class="ontab first_tab" onclick="getReportByorginst(this);">按组织</li>
							<li onclick="getReportByorginst(this);">按地域</li>
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
									<input type="hidden" id="returnAreaId" value="0" />
									<input type="hidden" id="thisAreaId" value="0" />
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
