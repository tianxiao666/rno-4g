function getUrgentRepairworkorder(reportType){
	//var bizunitId = $("#bizunitIdText").val();
	//var dates= new Array(); //定义一数组;
	//dates = getFirestAndLastToMonthDay();
	var url = "getUrgentRepairAction";
	//var params = {bizunitinnstId:bizunitId,beginTime:dates[0],endTime:dates[1]};
	var params = {reportType:reportType};
	$.post(url, params, function(data){
		$("#statements_right").html(data);
	});
}

    
var columnAndSplineBasicChart;	
		//柱状图+折线(对比)
		function columnAndSplineBasic(title,subtitle,yAxisTitle,yAxisTitle2,divId,categories,series){
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
                        color: '#FF6600'
                    }
                },
                title: {
                    text: yAxisTitle,
                    style: {
                    	color: '#FF6600'
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
                            	clickShowMore(categories[this.x],this.series.name);
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
                    rotation: -60,
                    align: 'right',
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
                    rotation: -60,
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
                        this.series.name +'(个): '+ this.y +'<br/>'+
                        '总数量(个): '+ this.point.stackTotal;
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
                                clickShowMore(categories[this.x],this.series.name);
                            }
                        }
                    }
                }
            },
            series: series
        });
    }
    
    
    
    var areaBasicChart;
    //区域图（对比）
    function areaBasic(title,subtitle,yAxisTitle,divId,categories,series){
        areaBasicChart = new Highcharts.Chart({
            chart: {
                renderTo: divId,
                type: 'area'
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
            yAxis: {
            	min: 0,
                title: {
                    text: yAxisTitle
                }
                
            },
            plotOptions: {
            	area: {
            		dataLabels: {
                        enabled: true
                    },
            		point: {
                        events: {
                            click: function(e) {
                                clickShowMore(categories[this.x],this.series.name);
                            }
                        }
                    }
            	}
            },
            tooltip: {
                formatter: function() {
                    return ''+
                        this.x +':'+this.series.name+':数量'+ this.y+'(个)';
                }
            },
            series: series
        });
    }
    
    
    var areaStackedChart;
    //区域图（堆积）
    function areaStacked(title,subtitle,yAxisTitle,divId,categories,series) {
        areaStackedChart = new Highcharts.Chart({
            chart: {
                renderTo: divId,
                type: 'area'
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
            yAxis: {
            	min: 0,
                title: {
                    text: yAxisTitle
                }
               
            },
            tooltip: {
                formatter: function() {
                   return '<b>'+ this.x +'</b><br/>'+
                        this.series.name +'(个): '+ this.y +'<br/>'+
                        '总数量(个): '+ this.point.stackTotal;
                }
            },
            plotOptions: {
                area: {
                	dataLabels: {
                        enabled: true
                    },
                    stacking: 'normal',
                    lineColor: '#666666',
                    lineWidth: 1,
                    marker: {
                        lineWidth: 1,
                        lineColor: '#666666'
                    },
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
                                clickShowMore(categories[this.x],this.series.name);
                            }
                        }
                    }
                }
            },
            tooltip: {
                formatter: function() {
                    return ''+
                        this.x +':'+this.series.name+':数量'+ this.y+'(个)';
                }
            },
            series: series
        });
    }
    
    
    var lineStackedChart;
    //折线图（堆积）
    function lineStacked(title,subtitle,yAxisTitle,divId,categories,series) {
        lineStackedChart = new Highcharts.Chart({
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
            yAxis: {
            	min: 0,
                title: {
                    text: yAxisTitle
                }
               
            },
            tooltip: {
                formatter: function() {
                    return '<b>'+ this.x +'</b><br/>'+
                        this.series.name +'(个): '+ this.y +'<br/>';
                }
            },
            plotOptions: {
                line: {
                    stacking: 'normal',
                	dataLabels: {
                        enabled: true
                    },
                    lineWidth: 1,
                    marker: {
                        lineWidth: 1
                    },
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
                    }
                }
            },
            series: [{
                type: 'pie',
                name: 'Browser share',
                data: data
            }]
        });
   }
   
   
  
   
   function getReportIndicatorsDescription(){
   		var reportType = $("#reportType").text();
   		//var params ={reportType:reportType};
   		//var url = "getReportIndicatorsDescriptionAction";
   		//$.post(url, params, function(data){
			//$("#statements_right").html(data);
		//});
		var url = "getReportIndicatorsDescriptionAction?reportType="+reportType;
		window.open(url);
   }
   
   function getReportComment(indicators, dimension, organizationId, statisticaltime){
   		var reportType = $("#reportType").text();
   		var params ={indicators:indicators,dimension:dimension,organizationId:organizationId,statisticaltime:statisticaltime};
   		var url = "getReportCommentAction";
   		$.post(url, params, function(data){
			$("#reportComment").html(data);
		});
   }
   
   
     //饼形
    var pieBasicChart;
   function pieBasic2(title,subtitle,divId,data){
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
  
   
   