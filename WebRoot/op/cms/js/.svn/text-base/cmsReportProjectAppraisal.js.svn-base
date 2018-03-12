
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
					//alert(categories);
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