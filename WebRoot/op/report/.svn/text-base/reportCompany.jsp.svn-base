<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>公司考核</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="report/css/base.css" type="text/css" />
	<link rel="stylesheet" href="report/css/statements.css" type="text/css" />
	<link rel="stylesheet" href="report/css/public.css" type="text/css" />
	<style type="text/css">
	.time_tool_left{margin-bottom:-3px}
	.time_tool_right{margin-bottom:-3px}
	
	</style>
	<script type="text/javascript" src="report/js/jquery-1.6.2.min.js"></script>
	
<script type="text/javascript" >
//获取上一个月的车辆数量
function getlastMonth(){
	var year = $("#hiddenYear").val();
	var month = $("#hiddenMonth").val();
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
	$("#DateTimeSpan").text(dates[0]+"年"+spanMonth+"月");
	var lastMonthDates= new Array(); //定义一数组;
	//根据年月获取月第一个天与最后一天
	lastMonthDates = getFirstAndLastMonthDay(dates[0], dates[1]);
	var beginTime = lastMonthDates[0];
	var endTime = lastMonthDates[1];
	$("#currentYear").html(year);
	$("#currentMonth").html(month);
	var params ={beginTime:beginTime,endTime:endTime};
	loadData(params);
}

//获取下一个月的车辆数量
function getNextMonth(){
	var year = $("#hiddenYear").val();
	var month = $("#hiddenMonth").val();
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
	$("#DateTimeSpan").text(dates[0]+"年"+spanMonth+"月");
	var nextMonthDates= new Array(); //定义一数组;
	//根据年月获取月第一个天与最后一天
	nextMonthDates = getFirstAndLastMonthDay(dates[0], dates[1]);
	var beginTime = nextMonthDates[0];
	var endTime = nextMonthDates[1];
	$("#currentYear").html(dates[0]);
	$("#currentMonth").html(dates[1]);
	var params ={beginTime:beginTime,endTime:endTime};
	loadData(params);
	
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
//根据年月获取月第一个天与最后一天
function getFirstAndLastMonthDay(year, month){
    if(parseInt(month)<=9){
   		month="0"+month;
   }  
   var   firstdate = year + '-' + month + '-01 00:00:00';
   var  day = new Date(year,month,0); 
   var lastdate = year + '-' + month + '-' + day.getDate() + ' 23:59:59';//获取当月最后一天日期  
   var dates= new Array(); //定义一数组;
   dates[0] = firstdate;
   dates[1] = lastdate;
   return dates;
}

function loadData(params){
		var totalContent="";
		$.post("getCompanyTargetReportListByDateAction",params,function(data){
		$("tr").not(".head").remove();
		if(data!=null){
			var bsequipmenttarget=0;
		var wlantarget=0;
		var basestationtarget=0;
		var chambertarget=0;
		var cityvillagetarget=0;
		var row = 0;
		var bsequipmenttargetrow=0
		var wlantargetrow=0;
		var basestationtargetrow=0;
		var chambertargetrow=0;
		var cityvillagetargetrow=0;
		var firstContent ="";
		$.each(data,function(index,value){
			var content = "";
			
			if(value.company=="广东怡创科技股份有限公司"){
				firstContent += "<tr ><td rowspan='"+value.info.length+"' style='width:5%'>"+value.company+"</td>";
			}else{
				content += "<tr ><td rowspan='"+value.info.length+"' style='width:5%'>"+value.company+"</td>";
			}
			
		 	
		 	row +=parseInt(value.info.length);
			var indexFlag=1;
			$.each(value.info,function(i,v){
				if(value.company=="广东怡创科技股份有限公司"){
					if(indexFlag==1){
						firstContent +="<td>"+v.area+"</td><td id='bsequipmenttarget'>"+v.bsequipmenttarget+"</td><td id='wlantarget'>"+v.wlantarget+"</td><td id='basestationtarget'>"+v.basestationtarget+"</td><td id='chambertarget'>"+v.chambertarget+"</td><td id='cityvillagetarget'>"+v.cityvillagetarget+"</td></tr>";
					}else{
						firstContent +="<tr><td>"+v.area+"</td><td id='bsequipmenttarget'>"+v.bsequipmenttarget+"</td><td id='wlantarget'>"+v.wlantarget+"</td><td id='basestationtarget'>"+v.basestationtarget+"</td><td id='chambertarget'>"+v.chambertarget+"</td><td id='cityvillagetarget'>"+v.cityvillagetarget+"</td></tr>";
					}
				}else{
					if(indexFlag==1){
						content +="<td>"+v.area+"</td><td id='bsequipmenttarget'>"+v.bsequipmenttarget+"</td><td id='wlantarget'>"+v.wlantarget+"</td><td id='basestationtarget'>"+v.basestationtarget+"</td><td id='chambertarget'>"+v.chambertarget+"</td><td id='cityvillagetarget'>"+v.cityvillagetarget+"</td></tr>";
					}else{
						content +="<tr><td>"+v.area+"</td><td id='bsequipmenttarget'>"+v.bsequipmenttarget+"</td><td id='wlantarget'>"+v.wlantarget+"</td><td id='basestationtarget'>"+v.basestationtarget+"</td><td id='chambertarget'>"+v.chambertarget+"</td><td id='cityvillagetarget'>"+v.cityvillagetarget+"</td></tr>";
					}
				}
					
				
				indexFlag++;
				bsequipmenttarget +=parseInt(v.bsequipmenttarget);
				wlantarget +=parseInt(v.wlantarget);
				basestationtarget +=parseInt(v.basestationtarget);
				chambertarget +=parseInt(v.chambertarget);
				cityvillagetarget +=parseInt(v.cityvillagetarget);
				
				if(v.bsequipmenttarget!=0){
				
					bsequipmenttargetrow++;
				}
				if(v.wlantarget!=0){
					wlantargetrow++;
				}
				if(v.basestationtarget!=0){
					basestationtargetrow++;
				}
				if(v.chambertarget!=0){
					chambertargetrow++;
				}
				if(v.cityvillagetarget!=0){
					cityvillagetargetrow++;
				}
				
			})
			
				totalContent=totalContent+content;
			
		})
		totalContent=firstContent+totalContent;
		bsequipmenttarget = bsequipmenttarget/bsequipmenttargetrow;
		wlantarget = wlantarget/wlantargetrow;
		basestationtarget = basestationtarget/basestationtargetrow;
		chambertarget=chambertarget/chambertargetrow;
		cityvillagetarget=cityvillagetarget/cityvillagetargetrow;
		totalContent +="<tr><td colspan='2'>统计平均</td><td>"+bsequipmenttarget+"</td><td>"+wlantarget+"</td><td>"+basestationtarget+"</td><td>"+chambertarget+"</td><td>"+cityvillagetarget+"</td></tr>"
		$("#main_table_task").append(totalContent);
		$("td[id='bsequipmenttarget']").each(function(){
			var count=parseInt($(this).html());
			if(count<bsequipmenttarget){
				$(this).css("color","red");
			}		
		})
		
		$("td[id='wlantarget']").each(function(){
			var count=parseInt($(this).html());
			if(count<wlantarget){
				$(this).css("color","red");
			}		
		})
		$("td[id='basestationtarget']").each(function(){
			var count=parseInt($(this).html());
			if(count<basestationtarget){
				$(this).css("color","red");
			}		
		})
		$("td[id='chambertarget']").each(function(){
			var count=parseInt($(this).html());
			if(count<chambertarget){
				$(this).css("color","red");
			}		
		})
		$("td[id='cityvillagetarget']").each(function(){
			var count=parseInt($(this).html());
			if(count<cityvillagetarget){
				$(this).css("color","red");
			}		
		})
			$(".shows").hide();
			$(".infos").show();
		}else{
			$(".shows").show();
			$(".infos").hide();
		}
		
		},'json')

}
$(function(){
		var aDate = new Date();
		var year=aDate.getFullYear();
		var month=aDate.getMonth();
		$("#hiddenYear").val(year);
		$("#hiddenMonth").val(month);
		if(parseInt(month)<=9){
	   		month="0"+month;
	   }  
	    $("#currentYear").html(year);
		$("#currentMonth").html(month);
		$("#DateTimeSpan").html(year+"年"+month+"月");
		var params={beginTime:year+"-"+month+"-01 00:00:00",endTime:year+"-"+month+"-31 00:00:00"}
		loadData(params);

})
		
</script>
<style>
.main_table tr td {
    background-color: #fff;
    border: 1px solid #CAD9EA;
    line-height: 21px;
    padding:0 2px;
    vertical-align: middle;
    white-space: pre-wrap;
}
.main_table th {
    background: url("report/images/th_bg1.gif") repeat-x scroll 0 0 transparent;
    border: 1px solid #CAD9EA;
    color: #444444;
    line-height: 30px;
    padding: 0 2px;
    white-space: nowrap;
}
</style>
  </head>
  
  <body>
  
  
  <div>
    <div class="statements_main_top" style="text-align:center">
    	<span id="currentYear"></span>年<span id="currentMonth"></span>月各代维公司考核报表
    </div>
    <div style="position: absolute; right: 10px; top: 15px;">
    	<span class="time_tool_left" onclick="getlastMonth();"></span>
		<span id="DateTimeSpan"></span>
		<input type="hidden" id="hiddenYear" value=""/>
		<input type="hidden" id="hiddenMonth" value=""/>
		<span class="time_tool_right" onclick="getNextMonth();"></span>
	</div>
	<div class="statements_main_table">
		<div class="shows" style="display:none">暂无数据</div>
			<table class="main_table tc infos" id="main_table_task" style="display:none">
				<tr class="head">
					<th style="width:20%;">代维公司</th>
					<th>区域</th>
					<th>基站主设备代维</th>
					<th>分布系统代维</th>
					<th>基站维护支撑</th>
					<th>室分维护支撑</th>
					<th>城中村专项维护</th>
				</tr>
				
				
				<tr class="main_table_task_tr">
					
				</tr>
			
				<tr class="tl" style="text-align:center">
					
				</tr>
			</table>
		</div>
	</div>
  </body>
</html>
