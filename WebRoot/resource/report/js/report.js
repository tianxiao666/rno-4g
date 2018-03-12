 var columnBasicChart;	
		//柱状图(对比)
		function columnBasic(divId,categories,series){
       	 columnBasicChart = new Highcharts.Chart({
            chart: {
                renderTo: divId,
                type: 'column'
            },
            title: {
                text: '资源分类统计报表'
            },
            subtitle: {
                text: '点击图形区域，可加载资源列表'
            },
            xAxis: {
                categories: categories
            },
            yAxis: {
                min: 0,
                title: {
                    text: '资源数量(个)'
                }
            },
            
            tooltip: {
                formatter: function() {
                    return ''+
                        this.x +':'+this.series.name+':数量'+ this.y+'(个)';
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
                            	clickShowMore(this.x,categories[this.x],this.series.name);
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
    	function columnStacked(divId,categories,series){
        columnStackedChart = new Highcharts.Chart({
            chart: {
                renderTo: divId,
                type: 'column'
            },
            title: {
                text: '资源分类统计报表'
            },
            subtitle: {
                text: '点击图形区域，可加载资源列表'
            },
            xAxis: {
                categories: categories
            },
            yAxis: {
                min: 0,
                title: {
                    text: '资源数量(个)'
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
                        enabled: true
                    },
                    dataLabels: {
                        enabled: true,
                        color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
                    },
                    point: {
                        events: {
                            click: function(e) {
                                clickShowMore(this.x,categories[this.x],this.series.name);
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
    function areaBasic(divId,categories,series){
        areaBasicChart = new Highcharts.Chart({
            chart: {
                renderTo: divId,
                type: 'area'
            },
            title: {
                text: '资源分类统计报表'
            },
            subtitle: {
                text: '点击图形区域，可加载资源列表'
            },
            xAxis: {
                categories: categories
            },
            yAxis: {
            	min: 0,
                title: {
                    text: '资源数量(个)'
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
                                clickShowMore(this.x,categories[this.x],this.series.name);
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
    function areaStacked(divId,categories,series) {
        areaStackedChart = new Highcharts.Chart({
            chart: {
                renderTo: divId,
                type: 'area'
            },
            title: {
                text: '资源分类统计报表'
            },
            subtitle: {
                text: '点击图形区域，可加载资源列表'
            },
            xAxis: {
                categories: categories
            },
            yAxis: {
            	min: 0,
                title: {
                    text: '资源数量(个)'
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
                    stacking: 'normal',
                    dataLabels: {
                        enabled: true
                    },
                    lineColor: '#666666',
                    lineWidth: 1,
                    marker: {
                        lineWidth: 1,
                        lineColor: '#666666'
                    },
                    point: {
                        events: {
                            click: function(e) {
                                clickShowMore(this.x,categories[this.x],this.series.name);
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
    function lineBasic(divId,categories,series){
        lineBasicChart = new Highcharts.Chart({
            chart: {
                renderTo: divId,
                type: 'line'
            },
            title: {
                text: '资源分类统计报表'
            },
            subtitle: {
                text: '点击图形区域，可加载资源列表'
            },
            xAxis: {
                categories: categories
            },
            yAxis: {
            	min: 0,
                title: {
                    text: '资源数量(个)'
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
                                clickShowMore(this.x,categories[this.x],this.series.name);
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
    function lineStacked(divId,categories,series) {
        lineStackedChart = new Highcharts.Chart({
            chart: {
                renderTo: divId,
                type: 'line'
            },
            title: {
                text: '资源分类统计报表'
            },
            subtitle: {
                text: '点击图形区域，可加载资源列表'
            },
            xAxis: {
                categories: categories
            },
            yAxis: {
            	min: 0,
                title: {
                    text: '资源数量(个)'
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
                                clickShowMore(this.x,categories[this.x],this.series.name);
                            }
                        }
                    }
                }
            },
            series: series
        });
    }
    
    var allSelectType = "";
    function clickShowMore(xName,entityId,select){
    //alert(entityId + select);
    var marqueefont = entityId + ":" + select + ",共";
    $("#bottom_left").hide();
    $("#bottom_top").hide();
    $("#bottom_load_img").show();
   	var repertType = $("#repertType").val();
    if(repertType == null || repertType == ""){
    	// alert(entityId+xName);
   $("#DownLoadForm_conditions").val("");
    				$("#DownLoadForm_column").val("");
    				$("#DownLoadForm_columnValue").val("");
    var entityXName = entityId+xName;
    $("#bottom1").show();
    var selectStrsId = "";
    //alert(select);
					if(select.indexOf(")") == 5){
						selectStrsId = select.substring(0,2);
					}else if(select.indexOf(")")){
						selectStrsId = select.substring(select.indexOf(")")+1);
					}
					else{
						selectStrsId = select;
					}
		//var entityTypes = $("#selectEntityHidden").val();
		var entityId = $("#"+entityXName).text();
		$("#DownLoadForm_entityId").val(entityId);
		var entityTypes = $("#"+entityXName).attr("title");
		$("#DownLoadForm_currentType").val(entityTypes);
		//alert(entityTypes);
		//alert(entityId);
		var selectStrs= new Array(); //定义一数组
		//alert($("#"+selectStrsId).text());
		var selectStrsIdvar = $("#"+selectStrsId).text();
		var resourceTypeVar = "";
		$("#selectDiv input[name='selectType']").each(function(){
				 if($(this).attr("checked")){
				 	if(selectStrsIdvar == $(this).val()){
				 		resourceTypeVar = $(this).parent().attr("title");
				 	}
				 }
		});
		selectStrs = $("#"+selectStrsId).text().split(",");
		var selectType = selectStrs[0];
		$("#DownLoadForm_selectResType").val(selectType);
		//alert(selectType);
		var allLinks = selectStrs[1];
		$("#DownLoadForm_allLink").val(allLinks);
		//alert(selectType);
		var url = "getResourceEntityListAction";
		allSelectType = selectType;
		
		var params = {entityId:entityId,currentType:entityTypes,allLink:allLinks,selectResType:selectType};
			$.post(url, params, function(data){
				var dataeval = eval("("+data+")");
				var dataevalIndex = 0;
				$.each(dataeval.entityList,function(k,y){
					dataevalIndex++;
				});
				marqueefont = "("+ $("#selectType option:checked").text() + ") " + marqueefont + dataevalIndex + "个";
				$("#marqueefont").text(marqueefont);
				//alert(dataevalIndex);
				$("#extjsGridData").val(data);
				createExtjsGrid(false,"","","",data);
				$("#bottom_load_img").hide();
				$("#bottom_left").show();
   				$("#bottom_top").show();
			});
    }
    }
    
   
function groupgrid(fields,data,columns) {
    // wrapped in closure to prevent global vars.
    $("#bottom_left").css("width","100%");
    $("#bottom_right").html("");
    $("#bottom_right").css("height","0px");
    Ext.define('Restaurant', {
        extend: 'Ext.data.Model',
        fields: fields
    });

    var Restaurants = Ext.create('Ext.data.Store', {
        storeId: 'restaraunts',
        model: 'Restaurant',
        sorters: fields,
        data: data
    });
    
    var groupingFeature = Ext.create('Ext.grid.feature.Grouping',{
        groupHeaderTpl: '{columnName}:{name},({rows.length})',
        hideGroupedHeader: false
    });

    var grid = Ext.create('Ext.grid.Panel', {
        renderTo: Ext.getBody(),
        collapsible: false,
        iconCls: 'icon-grid',
        id:'bottom_top',
        renderTo:"bottom_left", 
        frame: true,
        store: Restaurants,
        height: 300,
        features: [groupingFeature],
        columns: columns,
        
        dockedItems: [{
            xtype: 'toolbar',
            items: [{
                text: '清除分组',
                handler: function(){
                	var data = $("#extjsGridData").val();
                    $("#bottom_left").css("width","100%");
                    $("#bottom_top").css("overflow","hidden");
             	   $("#bottom_right").html("");
             	   $("#bottom_right").css("height","0px");
                    var showList=$(".x-grid-row");
                	pagingColumnByForeground("paging_div",showList);
                	$("#paging_div").show();
                	createExtjsGrid(false,"","","",data);
                }
            }, '-', {
                text: '分页',
                handler: function(){
                   var showList=$(".x-grid-row");
                	pagingColumnByForeground("paging_div",showList);
                	$("#paging_div").show();
                }
            }, '-', {
                text: '筛选',
                handler: function(){
                	var keyValue = $("#keyValues").val();
                	var keyValues= new Array(); //定义一数组
					keyValues = keyValue.split(",");
					var countext = "<option value=''>请选择</option>";
					$.each(keyValues, function(key, value){
						var keyVal= new Array(); //定义一数组
						keyVal = value.split("|");
						countext = countext + "<option value='"+keyVal[0]+"'>"+keyVal[1]+"</option>";
					});
					$("#select_column").html(countext);
					$("#divDisplay1").show();
                }
            }, '-', {
                text: '清除筛选',
                handler: function(){
                	var data = $("#extjsGridData").val();
                	$("#bottom_left").css("width","100%");
                	$("#bottom_top").css("overflow","hidden");
             	   $("#bottom_right").html("");
             	   $("#bottom_right").css("height","0px");
    				$("#DownLoadForm_conditions").val("");
    				$("#DownLoadForm_column").val("");
    				$("#DownLoadForm_columnValue").val("");
    				createExtjsGrid(false,"","","",data);
                }
            }, '-', {
                text: '导出',
                handler: function(){
                	$("#DownLoadForm").attr("action", "downLoadResourceTotalInformationReportAction");
					document.getElementById("DownLoadForm").submit();
                }
            }]
        }]
        
    });	
    
        grid.addListener('itemdblclick',click,this);
}


		function createExtjsGrid(isBar,column,columnValue,conditions,data){
				var showList=$(".x-grid-row");
				var showCount = 0;
				var countextth = "";
				var countexttd = "";
				var fields= new Array(); //定义一数组
				var keyValues = new Array(); //定义一数组
				data = eval("["+data+"]");
				$.each(data, function(key, value){
					$.each(value.chineseList, function(ke, val){
						var i = 0;
						$.each(val, function(k, v){
							countextth = countextth + "{text:'"+v+"',flex:1,dataIndex:'"+k+"'},";
							fields[i] = k;
							keyValues[i] = k + "|" + v;
							i++;
						});
					});
					var j = 0;
					$.each(value.entityList, function(ke, val){
					showCount++;
					if(column){
					if(conditions == 'equal'){
						if(val[column] == columnValue){
							countexttd = countexttd + "{";
							$.each(val, function(k, v){
								if("maintainAttention" == k){
									v = "";
								}
								countexttd = countexttd + k+":\'"+v+"\',";
							});
							if(countexttd != null && countexttd != ""){
								countexttd = countexttd.substring(0,countexttd.length -1);
							}
							countexttd = countexttd + "},";
						}
					}else if(conditions == 'notequal'){
						if(val[column] != columnValue){
							countexttd = countexttd + "{";
							$.each(val, function(k, v){
								if("maintainAttention" == k){
									v = "";
								}
								countexttd = countexttd + k+":\'"+v+"\',";
							});
							if(countexttd != null && countexttd != ""){
								countexttd = countexttd.substring(0,countexttd.length -1);
							}
							countexttd = countexttd + "},";
						}
					}else if(conditions == 'LessThan'){
						if(val[column] < columnValue){
							countexttd = countexttd + "{";
							$.each(val, function(k, v){
								if("maintainAttention" == k){
									v = "";
								}
								countexttd = countexttd + k+":\'"+v+"\',";
							});
							if(countexttd != null && countexttd != ""){
								countexttd = countexttd.substring(0,countexttd.length -1);
							}
							countexttd = countexttd + "},";
						}
					}else if(conditions == 'greaterThan'){
						if(val[column] > columnValue){
							countexttd = countexttd + "{";
							$.each(val, function(k, v){
								if("maintainAttention" == k){
									v = "";
								}
								countexttd = countexttd + k+":\'"+v+"\',";
							});
							if(countexttd != null && countexttd != ""){
								countexttd = countexttd.substring(0,countexttd.length -1);
							}
							countexttd = countexttd + "},";
						}
					}else if(conditions == 'Contain'){
						if(val[column].indexOf(columnValue) >= 0){
							countexttd = countexttd + "{";
							$.each(val, function(k, v){
								if("maintainAttention" == k){
									v = "";
								}
								countexttd = countexttd + k+":\'"+v+"\',";
							});
							if(countexttd != null && countexttd != ""){
								countexttd = countexttd.substring(0,countexttd.length -1);
							}
							countexttd = countexttd + "},";
						}
					}else if(conditions == 'NoContain'){
						if(val[column].indexOf(columnValue) < 0){
							countexttd = countexttd + "{";
							$.each(val, function(k, v){
								if("maintainAttention" == k){
									v = "";
								}
								countexttd = countexttd + k+":\'"+v+"\',";
							});
							if(countexttd != null && countexttd != ""){
								countexttd = countexttd.substring(0,countexttd.length -1);
							}
							countexttd = countexttd + "},";
						}
					}
					}else{
						countexttd = countexttd + "{";
							$.each(val, function(k, v){
								if("maintainAttention" == k){
									v = "";
								}
								countexttd = countexttd + k+":\'"+v+"\',";
							});
							if(countexttd != null && countexttd != ""){
								countexttd = countexttd.substring(0,countexttd.length -1);
							}
						countexttd = countexttd + "},";
					}
					j++;
					});
				});
				$("#keyValues").val(keyValues);
				$("#bottom_top").remove();
				if(countextth != null && countextth != ""){
							countextth = countextth.substring(0,countextth.length -1);
				}
				if(countexttd != null && countexttd != ""){
							countexttd = countexttd.substring(0,countexttd.length -1);
				}
				countexttd = eval("["+countexttd+"]");
				countextth = eval("["+countextth+"]");
					$("#paging_div").hide();
					groupgrid(fields,countexttd,countextth);
					if(showCount != 0){
					if(isBar == true){
					$("#bottom_left").css("width","49%");
             	    $("#bottom_top").css("overflow","auto");
             	    $("#bottom_right").css("height","300px");
					setTimeout("getshowList('"+showCount+"')",1500); 
					}
					}
			}
   function getshowList(showCount){
   		var showList=$(".x-grid-row");
   		var newShowCount = parseInt(showList.size());
   		var count = parseInt(showCount);
   		var newcount = count - newShowCount;
   		var series = 
   		 [{
                name: '其他',
                data: [newcount]
            },{
                name: '筛选结果',
                data: [newShowCount]
            }];
   		barStacked(series);
   }
   
   function groupingData(){
   	$("#bottom_left").css("width","49%");
        $("#bottom_top").css("overflow","auto");
        $("#bottom_right").css("height","300px"); 
   	var data = "";
   	$(".x-grid-group-hd").each(function(){
   		var childrenHtml = $(this).children().children().children().html();
   		var count = childrenHtml.substring(childrenHtml.indexOf(",(")+2,childrenHtml.lastIndexOf(")"));
   		count = parseInt(count);
   		var text = childrenHtml.substring(0,childrenHtml.indexOf(",("));
   		if(text.indexOf("&nbsp;") >= 0){
   			text = text.substring(0,text.indexOf("&nbsp;") - 1);
   		}if(text.indexOf("undefined") >= 0){
   			text = text.substring(0,text.indexOf("undefined") - 1);
   		}
   		data = data + "['"+text+"',"+count+"],";
   	});
   	if(data != null && data != ""){
   		data = data.substring(0,data.length - 1);
   	}
   	data = eval("["+data+"]");
   	pieBasic(data);
   }
   
   
   var pieBasicChart;
   function pieBasic(data){
        pieBasicChart = new Highcharts.Chart({
            chart: {
                renderTo: 'bottom_right',
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: '分组结果'
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
   
   var barStackedChart;
   function barStacked(series){
        barStackedChart = new Highcharts.Chart({
            chart: {
                renderTo: 'bottom_right',
                type: 'bar'
            },
            title: {
                text: '筛选结果'
            },
            xAxis: {
                categories: ['筛选结果']
            },
            yAxis: {
                min: 0,
                title: {
                    text: ''
                }
            },
            legend: {
                backgroundColor: '#FFFFFF',
                reversed: true
            },
            tooltip: {
                formatter: function() {
                    return ''+
                       '<b>'+ this.x +'</b><br/>'+
                        this.series.name +': '+ this.y +'<br/>'+
                        '总数: '+ this.point.stackTotal;
                }
            },
            plotOptions: {
                series: {
                    stacking: 'normal'
                }
            },
                series: series
    });
    
    
    
   }
   

function click(view,record,item,index,e){
	var host = window.location.host;
	var url = "http://"+host+"/ops/resource/physicalres/getPhysicalresForOperaAction?currentEntityType="+allSelectType+"&currentEntityId="+record.get('id')+"&modelType=view";
	window.open(url);
	//alert(record.get('id') + "----------------" + record.get('_entityType'));


}


   
   
 function containSpecial(s)       
{       
    var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\*)(\()(\))(\-)(\_)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\,)(\.)(\/)(\<)(\>)(\?)(\)]+/);       
    return (containSpecial.test(s));       
}  