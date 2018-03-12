//生成组织架构树
$(function(){
	var queryUrl = "../organization/getProviderOrgTreeByOrgIdAction";
	var orgId = $("#hiddenorgId").val();
	$.post(queryUrl, {orgId:orgId}, function(data){
        	var result = data;
        	if(result){
        		$("#orgIdText").val(result[0].orgId);
        		//默认赋值
        		$("#orgNameText").val(result[0].orgName);
        		$("#orgName1").val(result[0].orgName);
				createOrgTree(result,"treeDiv2","tree3","tree3table","setReportOrg2");
				var aDate = new Date();
				var year=aDate.getFullYear();
				var month=aDate.getMonth()+1;
				var spanMonth = "";
				if(month < 10){
					spanMonth = "0"+month;
				}else{
					spanMonth = month;
				}
        	}
    },'json');
    
    $("#statements_tree_button2").click(function(){
			$("#treeDiv2").slideToggle("fast");
		});
		
    
    
    
	var hiddenrowNameTexts = $("#hiddenrowNameText").val().split(",");
	var hiddenjudges = $("#hiddenjudge").val().split(",");
	var hiddenrowValues = $("#hiddenrowValue").val().split(",");
	var judgeHTML =  "业务数据筛选条件:<p>";
	if(hiddenrowNameTexts && hiddenjudges && hiddenrowValues){
		for(var i = 0;i < hiddenrowNameTexts.length;i++){
			if(hiddenrowNameTexts[i] != "" && hiddenjudges[i] != "" && hiddenrowValues[i] != "" ){
				judgeHTML = judgeHTML +hiddenrowNameTexts[i]+","+hiddenjudges[i]+","+hiddenrowValues[i];
			}
		}
	}
	judgeHTML = judgeHTML +"</p>";
	$("#judgeDiv").html(judgeHTML);
    $("#settings_btn").click(function(){
			var rowName = "";
			var judge = "";
			var rowValue = "";
			var rowNameText = "";
			$(".rowName").each(function(){
				rowName = rowName + $(this).val() + ",";
			});
			$(".Judge").each(function(){
				judge = judge + $(this).text() + ",";
			});
			$(".rowValue").each(function(){
				rowValue = rowValue + $(this).text() + ",";
			});
			$(".rowNameText").each(function(){
				rowNameText = rowNameText + $(this).text() + ",";
			});
			
			$("#rowName1").val(rowName);
			//fff
			$("#rowNameText").val(rowNameText);
			$("#Judge1").val(judge);
			$("#rowValue1").val(rowValue);
			if($("#orgId1").val() == ""){
				$("#orgId1").val($("#hiddenorgId").val());
			}
			if($("#orgName2").val() == ""){
				$("#orgName2").val($("#orgName1").val());
			}
			$("#beginTime1").val($("#beginTime").val());
			$("#endTime1").val($("#endTime").val());
			$(".report_settings").slideToggle("fast");	
			return true;
		});
    
		
		$("#hide_settings_btn").click(function(){
			$(".report_settings").slideToggle("fast");	
		});
		

    
	var series = "";
	var categories= new Array(); //定义一数组;	
	var workorderhtml = "{ name: '工单数', data: [";
	var processTimehtml = "{ name: '超时工单数', data: [";
	$(".workorderCount").each( function(){
		workorderhtml = workorderhtml + $(this).val() + ",";
	});
	$(".hiddenacceptProfessional").each( function(){
		processTimehtml = processTimehtml + $(this).val() + ",";
	});
	var i = 0;
	$(".orgName").each( function(){
		categories[i]=$(this).val();
		i++;
	});
	var selectDivCountext = "";
	$(".bn").each( function(){
		selectDivCountext = selectDivCountext+"<div id='"+$(this).val()+"'>"+$(this).attr("va")+"</div>";
	});
	$("#bn").html(selectDivCountext);
	workorderhtml = workorderhtml + "]},";
		processTimehtml = processTimehtml + "]},";
		series = "[" +workorderhtml + processTimehtml + "]";
	series = eval(series);
	var title = "故障工单数量统计分析报表";
		var subtitle = "";
		if($("#beginTime").val()&&$("#endTime").val()){
			subtitle = "报表时间("+$("#beginTime").val()+"~"+$("#endTime").val()+")";
		}
		var yAxisTitle = "工单数量(张)";
		columnStacked(title,subtitle,yAxisTitle,"statements_img",categories,series);
		
		
		
	//------------------
	
	 
		
		/*input样式*/
		$(":button,:submit").mousedown(function(){
			$(this).addClass("input_button_down");
		});
		$(":button,:submit").mouseup(function(){
			$(this).removeClass("input_button_down");
		});
		
		/* checkbox点击隐藏 */
		$(".statements_right fieldset legend :checkbox").each(function(){
			if($(this).attr("checked")=="checked"){
				$(this).parent().parent().removeClass("fieldset-hide");
				$(this).parent().parent().find(".output_table").show();
			}
			else{
				$(this).parent().parent().addClass("fieldset-hide");
				$(this).parent().parent().find(".output_table").hide();
			}
			$(this).click(function(){
				if($(this).attr("checked")=="checked"){
					$(this).parent().parent().removeClass("fieldset-hide");
					$(this).parent().parent().find(".output_table").show();
				}
				else{
					$(this).parent().parent().addClass("fieldset-hide");
					$(this).parent().parent().find(".output_table").hide();
				}
			})
		});
		
		/*显示/隐藏报表设置*/
	    $(".show_report_settings").click(function(){
			$(".report_settings").slideToggle("fast");
		});
		
	
			
	});	

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

            },
            tooltip: {
                formatter: function() {
                    return '<b>'+ this.x +'</b><br/>'+
                        this.series.name +'(张): '+ this.y +'<br/>';
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
                            	clickMore(categories[this.x],this.series.name);
                            }
                        }
                    }
                }
            },
            series: series
        });
    }
    
    //按类型获取工单
    function getReportByStationType(me){
		$(".tab_menu ul li").removeClass("ontab");
		$(me).addClass("ontab");
		if($(me).text() == "按组织"){
			getUrgentRepairworkorder("getUrgentRepairProcessTimeRateByBizunitinnstAction");
		}else if($(me).text() == "按基站类型"){
			getUrgentRepairworkorder("getUrgentRepairProcessTimeRateByBaseStationLevelAction");
		}else if($(me).text() == "按故障类型"){
			getUrgentRepairworkorder("getUrgentRepairProcessTimeRateByFaultTypeAction");
		}else if($(me).text() == "按告警级别"){
			getUrgentRepairworkorder("getUrgentRepairProcessTimeRateByFaultLevelAction");
		}else if($(me).text() == "按受理专业"){
			getUrgentRepairworkorder("getUrgentRepairProcessTimeRateByAcceptProfessionalAction");
		}else if($(me).text() == "按故障大类"){
			getUrgentRepairworkorder("getUrgentRepairProcessTimeRateByFaultGeneraAction");
		}
		$("#orginstDiv").show();
	}
	
	function getUrgentRepairworkorder(actionName){
		var orgId = $("#hiddenorgId").val();
		var beginTime = $("#hiddenbeginTime").val();
		var endTime = $("#hiddenendTime").val();
		var rowName = $("#hiddenrowName").val();
		var rowValue = $("#hiddenrowValue").val();
		var judge = $("#hiddenjudge").val();
		var param = {orgId:orgId,beginTime:beginTime,endTime:endTime,rowName:rowName,rowValue:rowValue,judge:judge};
		var url = actionName;
		$.post(url,param,function(data){
			createWorkorderSumColumnStacked(data);
		},'json');
	}
	
	
	
	function createWorkorderSumColumnStacked(data){
		var series = "";
		var categories= new Array(); //定义一数组;	
		var workorderhtml = "{ name: '工单数', data: [";
		var processTimehtml = "{ name: '超时工单数', data: [";
		var i = 0;
		$.each(data,function(key,value){
			workorderhtml = workorderhtml + value.wCount + ",";
			processTimehtml = processTimehtml + value.AcceptedTimeRateCount + ",";
			if(value.orgName){
				categories[i]=value.orgName;
			}else{
				categories[i]=value.statisticsType;
			}
			i++;
		});
		workorderhtml = workorderhtml + "]},";
		processTimehtml = processTimehtml + "]},";
		series = "[" +workorderhtml + processTimehtml + "]";
		series = eval(series);
		var title = "故障工单数量统计分析报表";
		var subtitle = "";
		if($("#beginTime").val()&&$("#endTime").val()){
			subtitle = "报表时间("+$("#beginTime").val()+"~"+$("#endTime").val()+")";
		}
		var yAxisTitle = "工单数量(张)";
		//alert(categories);
		columnStacked(title,subtitle,yAxisTitle,"statements_img",categories,series);
	}
	
	
	
	
	function getReportIndicatorsDescription(){
   		var reportType = $("#reportType").text();
   		var params ={reportType:reportType};
   		var url = "getReportIndicatorsDescriptionAction";
   		$.post(url, params, function(data){
			$("#statements_right").html(data);
			$("#statements_right").show();
			$("#statements_left").hide();
		});
   }
   
   
  	function clickMore(divId,name){
  		
  	}
  	
  	
  	function setReportOrg2(orgData,tableId){
		var data = eval("(" + orgData + ")");
		$("#orgId1").val(data.orgId);
  		//默认赋值
  		$("#orgNameText").val(data.name);
  		$("#orgName1").val(data.name);
  		$("#treeDiv2").slideToggle("fast");
		//alert(data);
	}