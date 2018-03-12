<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>统计报表</title>
<link rel="stylesheet" href="../../css/base.css" type="text/css" />
<link rel="stylesheet" href="../../css/public.css" type="text/css" />
<link rel="stylesheet" href="../../css/input.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/dialog/dialog.css" type="text/css" />
<style>
.tab_menu ul li.ontab {
    background: none repeat scroll 0 0 #FFFFFF;
    border-left: 1px solid #99BCE8;
    border-right: 1px solid #99BCE8;
    border-top: 3px solid #99BCE8;
    height: 22px;
    line-height: 20px;
}

.tab_menu {
    clear: both;
    height: 25px;
    padding-left: 5px;
}


.statements_tab {
    padding-top: 10px;
}





.tab_menu ul {
    position: absolute;
    z-index: 2;
}



.tab_content {
    background: none repeat scroll 0 0 #FFFFFF;
    border-top: 1px solid #99BCE8;
    padding: 5px 0;
    position: relative;
    top: -1px;
}


.time_tool_left {
    background: url("../../images/tool-sprites.gif") no-repeat scroll 0 -180px transparent;
}
.time_tool_left, .time_tool_right {
    cursor: pointer;
    display: inline-block;
    height: 15px;
    margin: 0 5px;
    vertical-align: middle;
    width: 15px;
}

.statements_search_date {
    margin-top: 10px;
    text-align: center;
}

.time_tool_right {
    background: url("../../images/tool-sprites.gif") no-repeat scroll 0 -165px transparent;
}

.st_full_screen {
    background: url("../report/images/full_screen.png") no-repeat scroll 0 0 transparent;
    float: right;
    margin: 5px;
}
.statements_back, .st_full_screen {
    cursor: pointer;
    display: inline-block;
    height: 16px;
    margin: 0 3px;
    position: relative;
    width: 16px;
    z-index: 1000;
}
.st_full_screen {
    background: url("../report/images/full_screen.png") no-repeat scroll 0 0 transparent;
    float: right;
    margin: 5px;
}
.statements_back, .st_full_screen {
    cursor: pointer;
    display: inline-block;
    height: 16px;
    margin: 0 3px;
    position: relative;
    width: 16px;
    z-index: 1000;
}


.tab_menu ul li {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    background: none repeat scroll 0 0 #EEEEEE;
    border-color: #99BBE8 #99BBE8 -moz-use-text-color;
    border-image: none;
    border-radius: 3px 3px 0 0;
    border-style: solid solid none;
    border-width: 1px 1px medium;
    cursor: pointer;
    float: left;
    line-height: 24px;
    margin: 0 2px;
    padding: 0 10px;
    position: relative;
    text-align: center;
}
</style>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/highcharts/highcharts.src.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/exporting.src.js"></script>
<script type="text/javascript">
//,"average","ranking","score"
//var contKey = ["janData","febData","marData","aprData","mayData","junData","julData","augData","sepData","octData","novData","decData"];
//var contKeyCh = ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"];
var thisDataB = {thistitle:"",thissubtitle:"",thisyAxisTitle2:"",thisyAxisTitle:"",thiscategories:"",thisseries:""};
var contKey = ["decData","novData","octData","sepData","augData","julData","junData","mayData","aprData","marData","febData","janData"];
var contKeyCh = ["十二月","十一月","十月","九月","八月","七月","六月","五月","四月","三月","二月","一月"];
var contKeyline = ["janData","febData","marData","aprData","mayData","junData","julData","augData","sepData","octData","novData","decData"];
var contKeyChline = ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"];
var categories = new Array();
var isC = false;
$(function(){
	var aDate = new Date();
	var year=aDate.getFullYear();
	$("#hiddenYear").val(year);
	var projectName = $("#projectName").val();
	year = year+"年";
	$("#DateTimeSpan").text(year);
	loadChar();
});
	function loadTotalAnnualRanking(){
		isC = false;
		var queryUrl = "getCmsPeportProjectAppraisalAction";
				var projectName = $("#projectName").val();
				var year = $("#DateTimeSpan").text();
				var params = {projectName:projectName,year:year};
				$.post(queryUrl, params, function(data){
					var series = "";
            		//var projectName = "";
            		//var year = "";
            		$.each(data,function(key,value){
            			var company = value['company'];
						categories[key] = company;
            		});;
            		$.each(contKey,function(k,v){
						series = series + "{ name: '"+contKeyCh[k]+"' , data:[ ";
						$.each(data,function(key,value){
						//	projectName = value['projectName'];
						//	year = value['year'];
							var company = value['company'];
								series = series + value[v] + ",";
							
						});
						if(series != null && series != ""){
							series = series.substring(0,series.length - 1);
						}
						series = series +"]},";
					});
					if(series != null && series != ""){
							series = series.substring(0,series.length - 1);
					}
					series = "[" + series + "]";
					//alert(series);
					series = eval(series);
					var title = projectName + "("+ year +")";
					var subtitle = "(点击图形区域，可钻取月度数据)";
					var yAxisTitle = "得分";
					thisDataB.thistitle = title;
					thisDataB.thissubtitle = subtitle;
					thisDataB.thisyAxisTitle = yAxisTitle;
					thisDataB.thisyAxisTitle2 = "";
					thisDataB.thiscategories = categories;
					thisDataB.thisseries = series;
					columnStacked(title,subtitle,yAxisTitle,"columnStackedDiv",categories,series);
				},'json');
	}
	
	function loadMonthlyScoreTrend(){
		isC = false;
		var queryUrl = "getCmsPeportProjectAppraisalAction";
				var projectName = $("#projectName").val();
				var year = $("#DateTimeSpan").text();
				var params = {projectName:projectName,year:year};
				$.post(queryUrl, params, function(data){
					var series = "";
            		//var projectName = "";
            		//var year = "";
            		$.each(data,function(key,value){
            			var company = value['company'];
            			series = series + "{ name: '"+company+"' , data:[ ";
            			$.each(contKeyline,function(k,v){
            				var cvalue = 0;
            				if(value[v] && value[v] != null && value[v] != "null"){
            					cvalue = value[v];
            				}
            				series = series + cvalue + ",";
            			});
            			if(series != null && series != ""){
							series = series.substring(0,series.length - 1);
						}
						series = series +"]},";
					});
					if(series != null && series != ""){
							series = series.substring(0,series.length - 1);
					}
					series = "[" + series + "]";
					series = eval(series);
					var title = projectName + "("+ year +")";
					var subtitle = "(点击图形区域，可钻取月度数据)";
					var yAxisTitle = "得分";
					thisDataB.thistitle = title;
					thisDataB.thissubtitle = subtitle;
					thisDataB.thisyAxisTitle = yAxisTitle;
					thisDataB.thisyAxisTitle2 = "";
					thisDataB.thiscategories = categories;
					thisDataB.thisseries = series;
					lineBasic(title,subtitle,yAxisTitle,"columnStackedDiv",contKeyChline,series);
				},'json');
	}
	
	function loadChar(){
		if($("#ontabul .ontab").text().replace(" ","")  == "年度总排名"){
			loadTotalAnnualRanking();
		}else{
			loadMonthlyScoreTrend();
		}
	}
	
	function switchChar(me){
		$("#ontabul li").removeClass("ontab");
		$(me).addClass("ontab");
		loadChar();
	}
	
	
	function clickShowMore(index){
		isC = true;
		var queryUrl = "getCmsPeportProjectAppraisalAction";
				//var aDate = new Date();
				var year=$("#DateTimeSpan").text(year);
				var projectName = $("#projectName").val();
				var params = {projectName:projectName,year:year};
				$.post(queryUrl, params, function(data){
					var series = "";
            		//var projectName = "";
            		//var year = "";
            		$.each(data,function(key,value){
            			var company = value['company'];
						categories[key] = company;
            		});;
            			var idx = contKey[index];
						series = series + "{ name: '"+contKeyCh[index]+"' , data:[ ";
						$.each(data,function(key,value){
						//	projectName = value['projectName'];
						//	year = value['year'];
							var company = value['company'];
								series = series + value[idx] + ",";
							
						});
					if(series != null && series != ""){
						series = series.substring(0,series.length - 1);
					}
					series = series +"]}";
					series = "[" + series + "]";
					series = eval(series);
					var title = projectName + "("+ year +")";
					var subtitle = "";
					var yAxisTitle = "得分";
					thisDataB.thistitle = title;
					thisDataB.thissubtitle = subtitle;
					thisDataB.thisyAxisTitle = yAxisTitle;
					thisDataB.thisyAxisTitle2 = "";
					thisDataB.thiscategories = categories;
					thisDataB.thisseries = series;
					columnStackedClickShowMore(title,subtitle,yAxisTitle,"columnStackedDiv",categories,series);
				},'json');
	}
	
	function clickShowMoreline(index){
		isC = true;
		var queryUrl = "getCmsPeportProjectAppraisalAction";
				var year=$("#DateTimeSpan").text(year);
				var projectName = $("#projectName").val();
				var params = {projectName:projectName,year:year};
				$.post(queryUrl, params, function(data){
					var series = "";
            		//var projectName = "";
            		//var year = "";
            		$.each(data,function(key,value){
            			var company = value['company'];
						categories[key] = company;
            		});;
            			var idx = contKeyline[index];
						series = series + "{ name: '"+contKeyChline[index]+"' , data:[";
						$.each(data,function(key,value){
						//	projectName = value['projectName'];
						//	year = value['year'];
							var company = value['company'];
								series = series + value[idx] + ",";
							
						});
					if(series != null && series != ""){
						series = series.substring(0,series.length - 1);
					}
					series = series +"]}";
					series = "[" + series + "]";
					series = eval(series);
					var title = projectName + "("+ year +")";
					var subtitle = "";
					var yAxisTitle = "";
					thisDataB.thistitle = title;
					thisDataB.thissubtitle = subtitle;
					thisDataB.thisyAxisTitle = yAxisTitle;
					thisDataB.thisyAxisTitle2 = "";
					thisDataB.thiscategories = categories;
					thisDataB.thisseries = series;
					columnStackedClickShowMore(title,subtitle,yAxisTitle,"columnStackedDiv",categories,series);
				},'json');
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
                    rotation: -45,
                    align: 'right',
                    style: {
                        fontSize: '12px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
            	tickInterval: 25,  //自定义刻度   
        		  max:100,//纵轴的最大值   
        		  min: 0,//纵轴的最小值   
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
		
		


		var columnStackedChart;
		//柱状图(堆积)
    	function columnStacked(title,subtitle,yAxisTitle,divId,categories,series){
        columnStackedChart = new Highcharts.Chart({
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
                },
                stackLabels: {
                    enabled: true,
                    style: {
                        fontWeight: 'bold',
                        color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                    }
                }
            },legend: {
                backgroundColor: '#FFFFFF',
                reversed: true
            },
            tooltip: {
                formatter: function() {
                    return '<b>'+ this.x +'</b><br/>'+
                        this.series.name +': '+ this.y +'<br/>'+
                        '总分数: '+ this.point.stackTotal;
                }
            },
            plotOptions: {
                column: {
                    stacking: 'normal',
                    dataLabels: {
                        enabled: true,
                        color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
                    },
                    point: {
                        events: {
                            click: function(e) {
                                clickShowMore(this.series.index);
                            }
                        }
                    }
                }
            },
            series: series
        });
    }
    
    var columnStackedChart12;
		//柱状图(堆积)
    	function columnStackedClickShowMore(title,subtitle,yAxisTitle,divId,categories,series){
        columnStackedChart12 = new Highcharts.Chart({
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
                },
                stackLabels: {
                    enabled: true,
                    style: {
                        fontWeight: 'bold',
                        color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                    }
                }
            },
            tooltip: {
                formatter: function() {
                    return '<b>'+ this.x +'</b><br/>'+
                        this.series.name +': '+ this.y +'<br/>'+
                        '总分数: '+ this.point.stackTotal;
                }
            },
            plotOptions: {
                column: {
                    stacking: 'normal',
                    dataLabels: {
                    },
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
    
     var lineBasicChart;
    //折线图（对比）
    function lineBasic(title,subtitle,yAxisTitle,divId,categories,series){
        lineBasicChart = new Highcharts.Chart({
            chart: {
                renderTo: divId,
                type: 'line'
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
            plotOptions: {
            	line:{
            		dataLabels: {
                        enabled: true
                    },
            		point: {
                        events: {
                            click: function(e) {
                                clickShowMoreline(this.x);
                            }
                        }
                    }
                }
            },
            tooltip: {
                formatter: function() {
                    return ''+
                        this.x +':'+this.series.name+':分数'+ this.y+'';
                }
            },
            series: series
        });
    }
	
	
	function getlastMonth(){
		var yearh = $("#hiddenYear").val();
		if( yearh > 0 && yearh != null){
			yearh = parseInt(yearh) - 1;
			$("#DateTimeSpan").text(yearh+"年");
			$("#hiddenYear").val(yearh);
			loadChar();
		}
	}
	
	function getNextMonth(){
		var yearh = $("#hiddenYear").val();
		if( yearh > 0 && yearh != null){
			yearh = parseInt(yearh) + 1;
			$("#DateTimeSpan").text(yearh+"年");
			$("#hiddenYear").val(yearh);
			loadChar();
		}
	}
	
	 function addDiv(divId){
	 		if($("#ontabul .ontab").text().replace(" ","") == "年度总排名"){
	 			if(isC == true){
					columnStackedClickShowMore(thisDataB.thistitle,thisDataB.thissubtitle,thisDataB.thisyAxisTitle,"ireportB",thisDataB.thiscategories,thisDataB.thisseries);
	 			}else{
	 				columnStacked(thisDataB.thistitle,thisDataB.thissubtitle,thisDataB.thisyAxisTitle,"ireportB",thisDataB.thiscategories,thisDataB.thisseries);
	 			}
			}else{
				if(isC == true){
	 				columnStackedClickShowMore(thisDataB.thistitle,thisDataB.thissubtitle,thisDataB.thisyAxisTitle,"ireportB",thisDataB.thiscategories,thisDataB.thisseries);
	 			}else{
					lineBasic(thisDataB.thistitle,thisDataB.thissubtitle,thisDataB.thisyAxisTitle,"ireportB",thisDataB.thiscategories,thisDataB.thisseries);
	 			}
			}
			setTimeout("addDivChar();",1000);
			//addDivChar();
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
			//var timeID=window.setInterval(function(){
			//	var chartext = $("#ireportB").html();
   			//	$("#report_statements_full_info").html(chartext);
			//},1);
   		}
   }
   

</script>
</head>
<body>
	<div class="statements_tab">
			<div class="statements_search_date">
				<p><周期></p>
				<span class="time_tool_left" onclick="getlastMonth();"></span>
				<span id="DateTimeSpan" class="time_tool_main"></span><input type="hidden" id="hiddenYear" value=""/>
				<span class="time_tool_right" onclick="getNextMonth();"></span>
			</div>
			<div class="tab_menu">
				<ul id="ontabul">
					<li class="ontab first_tab" onclick="switchChar(this);">年度总排名</li>
					<li onclick="switchChar(this);">月度得分趋势</li>
				</ul>		
			</div>
			<div class="tab_content">
				<span style="margin-left:20px;" >统计项目:</span><select id="projectName" onchange="loadChar();">
					<option value="广州基站维护">广州基站维护</option>
					<option value="广州直放站室分维护">广州直放站室分维护</option>
					<option value="广州传输管线代维">广州传输管线代维</option>
					<option value="韶关基站代维">韶关基站代维</option>
					<option value="深圳基站代维">深圳基站代维</option>
					<option value="深圳传输代维">深圳传输代维</option>
					<option value="潮州直放站">潮州直放站</option>
					<option value="潮州传输代维">潮州传输代维</option>
					<option value="汕头基站代维">汕头基站代维</option>
					<option value="汕头传输代维">汕头传输代维</option>
					<option value="汕尾传输代维">汕尾传输代维</option>
					<option value="梅州基站代维">梅州基站代维</option>
					<option value="梅州直放站代维">梅州直放站代维</option>
					<option value="梅州传输代维">梅州传输代维</option>
					<option value="揭阳基站代维">揭阳基站代维</option>
					<option value="揭阳传输代维">揭阳传输代维</option>
					<option value="河源基站代维">河源基站代维</option>
					<option value="惠州直放站">惠州直放站</option>
					<option value="惠州基站代维">惠州基站代维</option>
					<option value="惠州传输代维">惠州传输代维</option>
					<option value="东莞本地传输网代维">东莞本地传输网代维</option>
				</select>
				<em class="st_full_screen" style="display: block;" onclick="addDiv('columnBasicDiv');"></em>
				<div style="width: 100%; height: 300px;" id="columnStackedDiv">
					
				</div>
			</div>
	</div>
<div id="ireportB" style="height: 500px; width: 1000px; display: none;"></div>
</body>