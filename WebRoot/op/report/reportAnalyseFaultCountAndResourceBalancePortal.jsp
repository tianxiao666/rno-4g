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
		<title>每百基站人/车/任务量统计分析</title>
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
	
	
	//横向对比点击事件
	$("#horizontalCompare").click(function(){
		//$(".statements_search_date").get(0).style.visibility="hidden";
		clickClearFix(this);
		getHorizontalCompareReportData();
		/*
		$.ajax({
				"url" : "getAnalyseFaultCountAndResourceBalanceDataAction" , 
				"type" : "post" , 
				"success" : function ( data ) {
					//var jsonData = eval( "(" + data + ")" );
					var jsonData = data;
					var xAxis=jsonData.xAxis;
					var yAxis=jsonData.yAxis;
					var orgName=jsonData.topOrgName;
					show_orgId=jsonData.topOrgId;
					var orgId_xAxis=jsonData.orgId_xAxis;
	            	getReportComment("analyseFaultCountAndResourceBalance", "org", show_orgId, null);
					custom_columnBasic("每百基站人/车/任务量统计分析（"+orgName+"）","","","resultReport",xAxis,yAxis,orgId_xAxis);
				}
		});*/
	
	});
	
	
	//环比点击事件
	$("#loopCompare").click(function(){
		clickClearFix(this);
		getLoopCompareReportData();
	});
	
	
	//按组织显示报表
	$("#reportByOrg").click(function(){
		getHorizontalCompareReportData();
	});
	
	//按项目显示报表
	$("#reportByProject").click(function(){
		
	});
});


//获取对比周期的数据
function getHorizontalCompareReportData(){
	var param={"beginTime":beginTime,"endTime":endTime};	
	$.ajax({
			"url" : "getAnalyseFaultCountAndResourceBalanceDataAction" , 
			"type" : "post" ,
			"data":param,
			"success" : function ( data ) {
				//var jsonData = eval( "(" + data + ")" );
				var jsonData = data;
				var xAxis=jsonData.xAxis;
				var yAxis=jsonData.yAxis;
				var orgName=jsonData.topOrgName;
				show_orgId=jsonData.topOrgId;
				var orgId_xAxis=jsonData.orgId_xAxis;
				
				
				//alert("xAxis=="+xAxis);
				
				//var arr=[];
				//var obj={};
				//obj.name="故障数";
				//obj.data=yAxis;
				//arr.push(obj);

/*
				var arr=[{
                name: 'Tokyo', type:'column', yAxis: 1,
                data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
    
            	},{
                name: 'ff', type:'column',
                data: [49.9,null, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
    
            	},{
                name: 'ghg', type:'column',
                data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
    
            	}];*/
            	custom_columnAndSplineBasic("每百基站人/车/任务量统计分析（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）","","工单数 （张）","人员 （个） 车辆 （辆）","resultReport",xAxis,yAxis,orgId_xAxis);
				//custom_columnBasic("每百基站人/车/任务量统计分析（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）","(点击图形区域，可向下钻取)","","resultReport",xAxis,yAxis,orgId_xAxis);
			}
	});
};
		//var year = null;
		//var month = null;
		//var now = null;

//柱状图+折线(对比)
function custom_columnAndSplineBasic(title,subtitle,yAxisTitle,yAxisTitle2,divId,categories,series,flagId_xAxis){
     	 columnAndSplineBasicChart = new Highcharts.Chart({
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
              categories: categories,
              flagId_xAxis:flagId_xAxis
              ,
              labels: {
                  rotation: -45,
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
                  }
              },
              title: {
                  text: yAxisTitle,
                  style: {
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

</script>
	</head>
	<body>
		<div id="resultReport"  style="height:  315px; margin: 0 auto"></div>
	</body>
</html>