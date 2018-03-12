	  
var columnAndSplineBasicChart;	
		//柱状图+折线(对比)
      function columnAndSplineBasic(divId,categories,data1,data2,data3,data4,data5){
        columnAndSplineBasicChart = new Highcharts.Chart({
            chart: {
                renderTo: divId,
                zoomType: 'xy'
            },
            title: {
                text: ''
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                categories: categories
                ,
                labels: {
                    rotation: -80,
                    align: 'right',
                    style: {
                        fontSize: '12px'
                    }
                }
            },
            yAxis: [{ // Primary yAxis
                labels: {
                    formatter: function() {
                        return this.value;
                    },
                    style: {
                        color: '#AA4643'
                    }
                },
                
                opposite: true
                ,
                title: {
                    text: '',
                    style: {
                        color: '#AA4643'
                    }
                }
    
            }, { // Secondary yAxis
                gridLineWidth: 0,
                title: {
                    text: '',
                    style: {
                        color: '#AA4643'
                    }
                },
                labels: {
                    formatter: function() {
                        return this.value;
                    },
                    style: {
                        color: '#4572A7'
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
                spline: {
                    dataLabels: {
                        enabled: true
                    }
                }
            },
            series:[{
                name: '站址',
                type: 'column',
                yAxis: 1,
                color: '#FF6600',
                data: data1
    
            },{
                name: '基站',
                type: 'column',
                color: '#4572A7',
                yAxis: 1,
                data: data5
            },{
                name: '车辆',
                type: 'spline',
                color: '#89A54E',
                data: data2
            }, {
                name: '人员',
                type: 'spline',
                color: '#AA4643',
                data: data3
            },{
                name: '工单',
                type: 'spline',
                color: '#9966CC',
                data: data4
            }
            ]
            
        });
      }
		
		
	function loadReportChar(divId){
	$("#"+divId).html("<div  style=\"text-align: center; margin-top: 30%;\" valt=\"Char\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
     $(".statistics-table").show("fast");
     $.post("getReportCharAction",function(data){
     	if($("#"+divId).children().eq(0).attr("valt") == "Char"){
     		data = eval(data);
      	var categories = "";
      	var data1 = "";
      	var data2 = "";
      	var data3 = "";
      	var data4 = "";	
      	var data5 = "";
      	$.each(data,function(index,value){
      		$("#"+divId).html("");
      		categories = categories + "'"+value.areaName+ "',";
      		data1 = data1 + value.station + ",";
      		data2 = data2 + value.car + ",";
      		data3 = data3 + value.human + ",";
      		data4 = data4 + value.workorder + ",";
      		data5 = data5 + value.BaseStation + ",";
      	}
      	);
      	if(data1 != null && data1 != ""){
      		data1 = data1.substring(0,data1.length - 1);
      	}
      	if(data2 != null && data2 != ""){
      		data2 = data2.substring(0,data2.length - 1);
      	}
      	if(data3 != null && data3 != ""){
      		data3 = data3.substring(0,data3.length - 1);
      	}
      	if(data4 != null && data4 != ""){
      		data4 = data4.substring(0,data4.length - 1);
      	}
      	if(data5 != null && data5 != ""){
      		data5 = data5.substring(0,data5.length - 1);
      	}
      		categories = eval("["+categories+"]");
      		data1 = eval("["+data1+"]");
      		data2 = eval("["+data2+"]");
      		data3 = eval("["+data3+"]");
      		data4 = eval("["+data4+"]");
      		data5 = eval("["+data5+"]");
            columnAndSplineBasic(divId,categories,data1,data2,data3,data4,data5);     
     	}
      });
      }
      
      
      //人员监控
      function loadStaffReport(divId){
      $("#"+divId).html("<div  style=\"text-align: center; margin-top: 30%;\" valt=\"Staff\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
     	$(".statistics-table").show("fast");
      	$.post("getPeakStatisticsByStaffAction",function(data){
     	 if($("#"+divId).children().eq(0).attr("valt") == "Staff"){
      		var peakStatisticsHtml = "";
      		var i = 0;
      		$.each(data,function(index,value){
      		if(i == 0){
      			peakStatisticsHtml = peakStatisticsHtml + "<table id='Station_Tables'><tr><th colspan='4'>人员任务监控</th></tr>"
      								+"<tr><th>人员姓名</th><th>所属组织</th><th>未完成任务</th><th>已超时任务</th></tr>"
      								+"<tr class='Station_Tables_tr'><td class=\"tl\"><a href=\"#\" onclick=\"openResourceInfoWindow('_resource_human_"+value.staffId+"','_resource_human')\" >"
      								+value.staffName+"</a></td><td class=\"assignedtime\">"+value.orgName+"</td><td><a href=\"#\" onclick=\"showReportGantt('report_gantt','report_gantt_ContentId','report_gantt_datepicker','"
      								+value.staffId+"','people',this);\">"+value.unfinishedTasks+"</a>"
      								+"</td><td><a href=\"#\" onclick=\"showReportGanttByIsOverTime('report_gantt','report_gantt_ContentId','report_gantt_datepicker','"+value.staffId+"','people',this);\">"+value.hasTimedOutTasks+"</a></td></tr>";
      		}else{
      			peakStatisticsHtml = peakStatisticsHtml
      								+"<tr class='Station_Tables_tr'><td class=\"tl\"><a href=\"#\" onclick=\"openResourceInfoWindow('_resource_human_"+value.staffId+"','_resource_human')\" >"
      								+value.staffName+"</a></td><td class=\"assignedtime\">"+value.orgName+"</td><td><a href=\"#\" onclick=\"showReportGantt('report_gantt','report_gantt_ContentId','report_gantt_datepicker','"
      								+value.staffId+"','people',this);\">"+value.unfinishedTasks+"</a>"
      								+"</td><td><a href=\"#\" onclick=\"showReportGanttByIsOverTime('report_gantt','report_gantt_ContentId','report_gantt_datepicker','"+value.staffId+"','people',this);\">"+value.hasTimedOutTasks+"</a></td></tr>";
      		}
      			i++;
      		});
      		peakStatisticsHtml = peakStatisticsHtml +"</table><div id='table-gaging'></div>";
      		$("#"+divId).html(peakStatisticsHtml);
      		
			//$(".statistics-layer-info").hide();
			pagingColumnByForeground("table-gaging",$("#Station_Tables .Station_Tables_tr"),10);
			}
      		},'json');
      		
      }
      
      //车辆监控
      function loadCarReport(divId){
      $("#"+divId).html("<div  style=\"text-align: center; margin-top: 30%;\" valt=\"Car\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
      	$(".statistics-table").show("fast");
      	$.post("getPeakStatisticsByCarAction",function(data){
      	if($("#"+divId).children().eq(0).attr("valt") == "Car"){
      		var peakStatisticsHtml = "";
      		var i = 0;
      		$.each(data,function(index,value){
      		var carState = value.carState;
      		var stat = carState;
			if(carState && carState=='0'){
				    	carState = "<em style='color:grey;'>离线</em>";
				    }else if(carState && carState=='1'){
				    	carState = "<em style='color:blue;'>行驶中</em>";
				    }else if(carState && carState=='2'){
				    	carState = "<em style='color:green;'>静止</em>";
				    }else{
				    	carState = "<em style='color:grey;'>待初始化</em>";
				    }
      		var phone = "";
      		var carName = "";
      		if(value.driverPhone && value.driverPhone != 'null'){
      			phone = value.driverPhone;
      		}if(value.driverName && value.driverName != 'null'){
      			carName = value.driverName;
      		}
      		if(i == 0){
      			peakStatisticsHtml = peakStatisticsHtml + "<table id='Station_Tables'><tr><th colspan='4'>车辆任务监控</th></tr>"
      								+"<tr><th>车牌号码</th><th>司机名(电话号码)</th><th>所属组织</th><th>当前数任务</th><th>车辆状态</th></tr>"
      								+"<tr class='Station_Tables_tr'><td class=\"tl\"><a href=\"#\" onclick=\"openResourceInfoWindow('_resource_car_"+value.id+"','_resource_car')\" >"+value.carNumber+"</a></td><td>"+carName+"("+phone+")</td><td>"+value.orgName+"</td><td><a href=\"#\" onclick=\"showReportGantt('report_gantt','report_gantt_ContentId','report_gantt_datepicker','"+value.id+"','car',this);\">"+value.unfinishedTasks+"</a><td>"+carState+"</td></td></tr>";
      		}else{
      			peakStatisticsHtml = peakStatisticsHtml
      									+"<tr class='Station_Tables_tr'><td class=\"tl\"><a href=\"#\" onclick=\"openResourceInfoWindow('_resource_car_"+value.id+"','_resource_car')\" >"+value.carNumber+"</a></td><td>"+carName+"("+phone+")</td><td>"+value.orgName+"</td><td><a  href=\"#\" onclick=\"showReportGantt('report_gantt','report_gantt_ContentId','report_gantt_datepicker','"+value.id+"','car',this);\">"+value.unfinishedTasks+"</a><td>"+carState+"</td></td></tr>";
      		}
      			i++;
      		});
      		peakStatisticsHtml = peakStatisticsHtml +"</table><div id='table-gaging'></div>";
      		$("#"+divId).html(peakStatisticsHtml);
      		 
			//$(".statistics-layer-info").hide();
			pagingColumnByForeground("table-gaging",$("#Station_Tables .Station_Tables_tr"),10);
			}
      		},'json');
      	}
      	
      	
     //基站监控 	
     function loadBaseStationReport(divId){
     $("#"+divId).html("<div  style=\"text-align: center; margin-top: 30%;\" valt=\"BaseStation\"><img width=\"20\" height=\"20\" style=\"vertical-align:middle\" src=\"image/loading.gif\"><em>数据加载中，请稍侯...</em></div>");
		$(".statistics-table").show("fast");
      		$.post("getPeakStatisticsByStationAction",function(data){
      		if($("#"+divId).children().eq(0).attr("valt") == "BaseStation"){
      		var peakStatisticsHtml = "";
      		var i = 0;
      		$.each(data,function(index,value){
      		if(i == 0){
      			peakStatisticsHtml = peakStatisticsHtml + "<table id='Station_Tables'><tr><th colspan='4'>基站工单监控</th></tr>"
      								+"<tr><th>基站名称</th><th>所属站址</th><th>未完成工单数</th><th>已超时工单数</th></tr>"
      								+"<tr class='Station_Tables_tr'><td class=\"tl\">"+value.baseStationName+"</td><td><a href=\"#\" onclick=\"openResourceInfoWindow('_resource_station_"
      								+value.stationId+"','_resource_station')\" >"+value.stationName+"</a></td><td><a  href=\"#\" onclick=\"showReportGantt('report_gantt','report_gantt_ContentId','report_gantt_datepicker','"+value.baseStationId+"','"+value.baseStationType+"',this);\">"
      								+value.unfinishedTasks+"</a></td><td><a href=\"#\" onclick=\"showReportGanttByIsOverTime('report_gantt','report_gantt_ContentId','report_gantt_datepicker','"+value.baseStationId+"','"+value.baseStationType+"',this);\">"+value.routineCount+"</a></td></tr>";
      		}else{
      			peakStatisticsHtml = peakStatisticsHtml
      								+"<tr class='Station_Tables_tr'><td class=\"tl\">"+value.baseStationName+"</td><td><a href=\"#\" onclick=\"openResourceInfoWindow('_resource_station_"
      								+value.stationId+"','_resource_station')\" >"+value.stationName+"</a></td><td><a  href=\"#\" onclick=\"showReportGantt('report_gantt','report_gantt_ContentId','report_gantt_datepicker','"+value.baseStationId+"','"+value.baseStationType+"',this);\">"
      								+value.unfinishedTasks+"</a></td><td><a href=\"#\" onclick=\"showReportGanttByIsOverTime('report_gantt','report_gantt_ContentId','report_gantt_datepicker','"+value.baseStationId+"','"+value.baseStationType+"',this);\">"+value.routineCount+"</a></td></tr>";
      		}
      			i++;
      		});
      		peakStatisticsHtml = peakStatisticsHtml +"</table><div id='table-gaging'></div>";
      		$("#"+divId).html(peakStatisticsHtml);
      		 
			//$(".statistics-layer-info").hide();
			pagingColumnByForeground("table-gaging",$("#Station_Tables .Station_Tables_tr"),10);
			//showAssignedtime();
			}
      },'json');
      }
      
      
      function showReportGantt(targetContent,ganttContentId,datepickerClass,resourceId,resourceType,be){
    		createGanttContent(targetContent,ganttContentId,datepickerClass,resourceId,resourceType,null);
    		var offset = $(be).offset();
			$("#"+targetContent).css("top",offset.top + 20 + "px");
			$("#"+targetContent).css("left",offset.left - 50 + "px");
			$("#"+targetContent).show();
			$("#"+targetContent).hover(function(){},function(){
				$("#"+targetContent).hide();
				$("#"+targetContent).html("");
			});
				
      }
      
      
      function showReportGanttByIsOverTime(targetContent,ganttContentId,datepickerClass,resourceId,resourceType,be){
    		createGanttContent(targetContent,ganttContentId,datepickerClass,resourceId,resourceType,1);
    		var offset = $(be).offset();
			$("#"+targetContent).css("top",offset.top + 20 + "px");
			$("#"+targetContent).css("left",offset.left - 50 + "px");
			$("#"+targetContent).show();
			$("#"+targetContent).hover(function(){},function(){
				$("#"+targetContent).hide();
				$("#"+targetContent).html("");
			});
				
      }