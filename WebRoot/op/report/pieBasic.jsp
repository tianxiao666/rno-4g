<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
	<head>

		<title>My JSP 'pieBasic.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
		<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="../../jslib/highcharts/highcharts.src.js"></script>
		<script type="text/javascript" src="../../jslib/highcharts/exporting.src.js"></script>
		<script type="text/javascript">
			//获得上个月在昨天这一天的日期   
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
  
  
      function loadTest(){
      var date = new Date();

  	var date1 = getFirestAndLastToMonthDay();
  	var startTime = date1[0];
  	var endTime = date1[1];
  	$.post("getUrgentRepairReportOrderCountByAcceptProfessionalAction",{startTime:startTime,endTime:endTime},function(data){
  		var series = "";
  		var orgName = "";
  		$.each(data.value,function(k,v){ 
  			series = series + "['"+k+"',"+ v + "],";
  		});
  		if(series != null && series != ""){
  			series = series.substring(0,series.length - 1);
  		}
  		series = "["+series+"]";
  		series = eval(series);
  		var orgName = "";
  		var title = "本月故障抢修工单分类统计";
  		if(data.orgName){
  			orgName = "("+data.orgName+")";
  		}
  		var stitle = orgName;
        pieBasic(title,stitle,"container",series);
  	},'json'
  	);
     
      }
      
      
      //饼形
    var pieBasicChart;
   function pieBasic(title,subtitle,divId,data){
        pieBasicChart = new Highcharts.Chart({
            chart: {
                renderTo: divId,
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: title
            },
            subtitle: {
                text: subtitle
            },
            tooltip: {
                formatter: function() {
                    var percentage = this.percentage+"";
                        	var per = percentage.indexOf(".");
                        	if(per != -1){
                        		percentage = percentage.substring(0,per+3);
                        	}
                            return '<b>'+ this.point.name +'</b>: '+ percentage +' %';
                }
            },
            plotOptions: {
                 pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        color: '#000000',
                        connectorColor: '#000000',
                        formatter: function() {
                        	var percentage = this.percentage+"";
                        	var per = percentage.indexOf(".");
                        	if(per != -1){
                        		percentage = percentage.substring(0,per+3);
                        	}
                            return '<b>'+ this.point.name +'</b>: '+ percentage +' %';
                        }
                    },
                     showInLegend: true
                }
            },
            series: [{
                type: 'pie',
                name: 'Browser share',
                data: data
            }]
        });
   }
		</script>
	</head>

	<body onload="loadTest();">
		<div id="container"
			style="min-width: 40px; height: 230px; width:190px; margin: 0;"></div>
	</body>
</html>
