<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡检报表</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css"/>
<link rel="stylesheet" type="text/css" href="../../css/public.css"/>
<link rel="stylesheet" type="text/css" href="../css/leftMenu.css"></script>
<link rel="stylesheet" type="text/css" href="css/statements.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/dialog/dialog.css"/>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/highcharts.src.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/exporting.src.js"></script>
<script type="text/javascript" src="js/report.js"></script>
<script type="text/javascript">
$(function(){
	loadCharselect();
});

function loadCharselect(){
	var projectRoOrg = $("#projectName").val();
	var projectRoOrg = $("#projectName").val();
     if(projectRoOrg == "" || projectRoOrg == null){
     	$("#columnBasicDiv").html("<em><p style='font-size: 18px; line-height: 200px; color: rgb(153, 153, 153);'>没有对应数据</p><em>");
     	return;
     }
	var arr =  projectRoOrg.split(",");
	var id = arr[0];
	var type = arr[1];
	loadChar(id,type);
}

function loadChar(id,type){
	var params = null;
	$("#columnBasicDiv").html("<div  style=\"text-align: center; padding-top: 10%; height: 120px;\" valt=\"Char\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
	var url = "getRoutineinspectionReportAction";
	if(type == "Org"){
		params = {orgId:id,type:type};
		$.post(url, params, function(data){
		var series = "";
		var progresshtml = "{ type:'line', name: '计划工期%', data: [";
		var actualProgresshtml = "{ type: 'line',name: '实际进度%', data: [";
		var averageDeviationDistancehtml = "{ type: 'column',name: '平均偏离距离(米)', yAxis: 1, data: [";
		var categories = new Array();
		$("#columnBasicDiv").html("");
		if(data != null && data != ""){
			var size = data.length - 1;
			var i = 0;
			for(;size >= 0;size--){
				var da = data[size];
				var progress = 0;
				if(data[size].progress){
					progress = parseFloat(data[size].progress).toFixed(2);
				}else{
					progress = 0;
				}
				progresshtml = progresshtml+ progress + ",";
				var actualProgress = 0;
				if(data[size].actualProgress){
					actualProgress = parseFloat(data[size].actualProgress).toFixed(2);
				}else{
					actualProgress = 0;
				}
				actualProgresshtml = actualProgresshtml + actualProgress + ",";
				var averageDeviationDistance = 0;
				if(data[size].averageDeviationDistance){
					averageDeviationDistance = parseFloat(data[size].averageDeviationDistance).toFixed(2);
				}else{
					averageDeviationDistance = 0;
				}
				averageDeviationDistancehtml = averageDeviationDistancehtml+ averageDeviationDistance+ ",";
				var planTitle = "";
				if(data[size].planTitle.length > 8 && data[size].planTitle != null){
					var parseIndex = Math.ceil(data[size].planTitle.length/8);
					if(parseIndex != null && parseIndex > 0){
						for(var j = 0;j < parseIndex;j++){
							var sIndex = j*8;
							var dIndex = (j+1)*8;
							planTitle = planTitle + data[size].planTitle.substring(sIndex,dIndex) + "<br/>";
							//alert(planTitle);
						}
					}
				}else{
					planTitle = data[size].planTitle;
				}
				categories[i] = planTitle;
				i++;
			}
			if(progresshtml != null && progresshtml != ""){
				progresshtml = progresshtml.substring(0,progresshtml.length - 1);
			}
			if(actualProgresshtml != null && actualProgresshtml != ""){
				actualProgresshtml = actualProgresshtml.substring(0,actualProgresshtml.length - 1);
			}
			if(averageDeviationDistancehtml != null && averageDeviationDistancehtml != ""){
				averageDeviationDistancehtml = averageDeviationDistancehtml.substring(0,averageDeviationDistancehtml.length - 1);
			}
			averageDeviationDistancehtml = averageDeviationDistancehtml + "]},";
			progresshtml = progresshtml + "]},";
			actualProgresshtml = actualProgresshtml + "]}";
			series = "[" + averageDeviationDistancehtml + progresshtml +  actualProgresshtml  + "]";
			//series = "[{ type:'column', name: '计划进度', yAxis: 1, data: [10.7986111,11.2681159,8.0989583,2.6672979]},{ type: 'column',name: '实际进度', yAxis: 1, data: [0,0,0,0.333333333]},{ type: 'line',name: '平均偏离距离(米)', data: [0.666666666,0.5,0.2,0]}]";
			series = eval(series);
			var title = $("#projectName option:selected").text() + "巡检计划统计分析报表";
			columnAndSplineBasic1(title,"","","","columnBasicDiv",categories,series);
		}else{
			$("#columnBasicDiv").html("<em><p style='font-size: 18px; line-height: 200px; color: rgb(153, 153, 153);'>没有对应数据</p><em>");
		}
	},"json");
	}else{
		params = {projectId:id,type:type};
		$.post(url, params, function(data){
		var series = "";
		var timelyRatehtml = "{ type:'line', name: '巡检及时率', data: [";
		var averageDeviationDistancehtml = "{ type: 'column',name: '平均偏离距离(米)', yAxis: 1, data: [";
		var categories = new Array();
		$("#columnBasicDiv").html("");
		if(data != null && data != ""){
			var size = data.length - 1;
			var i = 0;
			for(;size >= 0;size--){
				var da = data[size];
				var timelyRate = 0;
				if(data[size].timelyRate){
					timelyRate = parseFloat(data[size].timelyRate).toFixed(2);
				}else{
					timelyRate = 0;
				}
				timelyRatehtml = timelyRatehtml+ timelyRate + ",";
				var averageDeviationDistance = 0;
				if(data[size].averageDeviationDistance){
					averageDeviationDistance = parseFloat(data[size].averageDeviationDistance).toFixed(2);
				}else{
					averageDeviationDistance = 0;
				}
				averageDeviationDistancehtml = averageDeviationDistancehtml+ averageDeviationDistance+ ",";
				var planTitle = "";
				if(data[size].planTitle.length > 8 && data[size].planTitle != null){
					var parseIndex = Math.ceil(data[size].planTitle.length/8);
					if(parseIndex != null && parseIndex > 0){
						for(var j = 0;j < parseIndex;j++){
							var sIndex = j*8;
							var dIndex = (j+1)*8;
							planTitle = planTitle + data[size].planTitle.substring(sIndex,dIndex) + "<br/>";
							//alert(planTitle);
						}
					}
				}else{
					planTitle = data[size].planTitle;
				}
				categories[i] = planTitle;
				i++;
			}
			if(timelyRatehtml != null && timelyRatehtml != ""){
				timelyRatehtml = timelyRatehtml.substring(0,timelyRatehtml.length - 1);
			}
			if(averageDeviationDistancehtml != null && averageDeviationDistancehtml != ""){
				averageDeviationDistancehtml = averageDeviationDistancehtml.substring(0,averageDeviationDistancehtml.length - 1);
			}
			averageDeviationDistancehtml = averageDeviationDistancehtml + "]},";
			timelyRatehtml = timelyRatehtml + "]}";
			series = "[" + averageDeviationDistancehtml + timelyRatehtml + "]";
			//series = "[{ type:'column', name: '计划进度', yAxis: 1, data: [10.7986111,11.2681159,8.0989583,2.6672979]},{ type: 'column',name: '实际进度', yAxis: 1, data: [0,0,0,0.333333333]},{ type: 'line',name: '平均偏离距离(米)', data: [0.666666666,0.5,0.2,0]}]";
			series = eval(series);
			var title = $("#projectName option:selected").text() + "巡检计划统计分析报表";
			columnAndSplineBasic1(title,"","","","columnBasicDiv",categories,series);
		}else{
			$("#columnBasicDiv").html("<em><p style='font-size: 18px; line-height: 200px; color: rgb(153, 153, 153);'>没有对应数据</p><em>");
		}
	},"json");
	}
	
}

 
var columnAndSplineBasicChart1;	
		//柱状图+折线(对比)
		function columnAndSplineBasic1(title,subtitle,yAxisTitle,yAxisTitle2,divId,categories,series){
       	 columnAndSplineBasicChart1 = new Highcharts.Chart({
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
                    align: 'center',
                    style: {
                        fontSize: '12px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: [{ // Primary yAxis
            	tickInterval: 25,  //自定义刻度   
        		  max:100,//纵轴的最大值   
        		  min: 0,//纵轴的最小值   
                labels: {
                    formatter: function() {
                        return this.value;
                    }
                },
                title: {
                    text: yAxisTitle
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
</script>
</head>
<body>
	<div class="clearfix">
		<div class="statements_top">
			<span style="margin-left:20px;" >统计范围:</span><select id="projectName" onchange="loadCharselect();">
				<s:iterator value="orgRoProjectList" id="vs" status="st2">
					<option value="<s:property value="#vs.id"/>,<s:property value="#vs.type"/>"><s:property value="#vs.name"/></option>
				</s:iterator>
			</select>
		</div>
		
		<div class="statements_img_half">
			<div id="columnBasicDiv"  style="height: 200px; margin: 0 auto; text-align: center;">
				
			</div>
		</div>
	</div>
</body>
