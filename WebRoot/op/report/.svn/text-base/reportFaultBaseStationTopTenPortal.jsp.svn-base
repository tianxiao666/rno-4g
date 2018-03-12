<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
	<head>
		
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="../../css/base.css" type="text/css" />
		<link rel="stylesheet" href="../../css/public.css" type="text/css" />
		<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css" />
		<link rel="stylesheet" href="../../css/input.css" type="text/css" />
		<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css" />
		<link rel="stylesheet" href="../../jslib/dialog/dialog.css" type="text/css" />
		<link rel="stylesheet" href="css/statements.css" type="text/css" />
		<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css" />
		<link rel="stylesheet" type="text/css" href="../css/leftMenu.css"></script>

		<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
		<script type="text/javascript" src="../js/tab.js" ></script>
		<script type="text/javascript" src="../../jslib/highcharts/highcharts.src.js"></script>
		<script type="text/javascript" src="../../jslib/highcharts/exporting.src.js"></script>
		<script type="text/javascript" src="js/report.js"></script>
		<title>故障数top-10最差基站排名</title>
		<script type="text/javascript">
$(function(){

	currentDate = new Date();
	currentYear=currentDate.getFullYear();
	currentMonth=currentDate.getMonth();
	var spanMonth = "";
	if(currentMonth < 10){
		if(currentMonth<1){
			currentYear = currentYear - 1;
			currentMonth = 12;
			spanMonth = currentMonth;
		}else{
			spanMonth = "0"+currentMonth;
		}
	}else{
		spanMonth = currentMonth;
	}
	$("#DateTimeSpan").text(currentYear+"年"+spanMonth+"月");
	
	beginTime = currentYear+"-"+currentMonth+"-01 00:00:00";
	var currentMonth1 = parseInt(currentMonth) +1;
	var currentYear1 = currentYear;
	if(currentMonth1 > 12){
		currentMonth1 = 1;
		currentYear1 = currentYear1 + 1;
	}
	endTime = currentYear1+"-"+currentMonth1+"-01 00:00:00";
	
	getHorizontalCompareReportData();
	
});



//获取基站数前10的对比周期的数据
function getHorizontalCompareReportData(){
	var param={"beginTime":beginTime,"endTime":endTime};	
	$.ajax({
		"url" : "getFaultBaseStationTopTenDataAction" , 
		"type" : "post" , 
		"data":param,
		"success" : function ( data ) {
			//var jsonData = eval( "(" + data + ")" );
			var jsonData = data;
			var xAxis=jsonData.xAxis;
			var yAxis=jsonData.yAxis;
			var orgName=jsonData.orgName;
			//alert("xAxis=="+xAxis);
			
			show_orgId=jsonData.topOrgId;
			
			/*
			var arr=[];
			var obj={};
			obj.name="故障数";
			obj.data=yAxis;
			arr.push(obj);

			var arr=[{
               name: 'Tokyo',
               data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
   
           	}];*/
			columnBasicFault("故障数top-10最差基站排名（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）","","基站数 （个）","resultReport",xAxis,yAxis);
		}
	});

};


//格式化月时间
function formatMonth(currentMonth){
	var res="";
	if(currentMonth< 10){
		res = "0"+currentMonth;
	}else{
		res=currentMonth;
	}
	return res+"";
}




           
var columnBasicChart;	
//柱状图(对比)
function columnBasicFault(title,subtitle,yAxisTitle,divId,categories,series){
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
                          	
                          }
                      }
                  }
              }
          },
              series: series
      });
}

</script>
	</head>
	<body>
		<div id="resultReport"  style="height: 200px; margin: 0 auto"></div>
	</body>
</html>