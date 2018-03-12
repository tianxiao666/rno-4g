var thisDataP = {thistitle:"",thissubtitle:"",thisyAxisTitle2:"",thisyAxisTitle:"",thiscategories:"",thisseries:""};

var thisDataB = {thistitle:"",thissubtitle:"",thisyAxisTitle2:"",thisyAxisTitle:"",thiscategories:"",thisseries:""};

//生成组织架构树
$(function(){
	var queryUrl = "../organization/getProviderOrgTreeByOrgIdAction";
	var orgId = $("#orgId").val();
	$("#orgIdText").val(orgId);
				var aDate = new Date();
				var year=aDate.getFullYear();
				var month=aDate.getMonth();
				if(month == 0){
					year = year - 1;
					month = 12;
				}
				$("#hiddenYear").val(year);
				$("#hiddenMonth").val(month);
				var spanMonth = "";
				year = parseInt(year);
				month = parseInt(month);
				if(month == 0){
					month = 12;
					year = year - 1;
				}
				if(month < 10){
					spanMonth = "0"+month;
				}else{
					spanMonth = month;
				}
		$("#DateTimeSpan").text(year+"年"+spanMonth+"月");
		getUrgentRepairworkorderStatistics();
	
	
	
	/*显示/隐藏区域*/
		$("#statements_tree_button1").click(function(){
			$("#treeDiv").slideToggle("fast");
		});
		
		$("#statements_tree_button2").click(function(){
			$("#treeDiv2").slideToggle("fast");
		});
		

		
		
	/*显示/隐藏报表设置*/
	    $(".show_report_settings").click(function(){
			$(".report_settings").slideToggle("fast");
		});
		
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
			if($("#orginnstId1").val() == ""){
				$("#orginnstId1").val($("#orgIdText").val());
			}if($("#orginnstName2").val() == ""){
			$("#orginnstName2").val($("#orginnstName1").val());
			}
			$("#beginTime1").val($("#beginTime").val());
			$("#endTime1").val($("#endTime").val());
			$(".report_settings").slideToggle("fast");	
			return true;
		});
		
		$("#hide_settings_btn").click(function(){
			$(".report_settings").slideToggle("fast");	
		});
});

	function setReportOrg(orgData,tableId){
		var data = eval("(" + orgData + ")");
		$("#orgIdText").val(data.orgId);
  		//默认赋值
  		$("#orgNameText").val(data.name);
  		$("#orginnstName").val(data.name);
  		$("#treeDiv").slideToggle("fast");
		//alert(data);
	}
	
	function setReportOrg2(orgData,tableId){
		var data = eval("(" + orgData + ")");
		$("#orginnstId1").val(data.orgId);
  		//默认赋值
  		$("#orgNameText").val(data.name);
  		$("#orginnstName1").val(data.name);
  		$("#treeDiv2").slideToggle("fast");
		//alert(data);
	}
	
//获取故障抢修工单数量
function getUrgentRepairworkorderStatistics(){
	if($("#timehidden").val()){
		//获取组织ID
		var bizunitId = $("#orgId").val();
		var dates = getDay($("#timehidden").val());
		//开始时间
		var beginTime = dates[0];
		//结束时间
		var endTime = dates[1];
		$("#beginTime").val(beginTime);
		$("#endTime").val(endTime);
		$("#hiddenbeginTime").val(beginTime);
		$("#hiddenendTime").val(endTime);
		//按组织获取故障抢修处理历时
		getUrgentRepairworkorderStatisticsByDate(bizunitId,beginTime,endTime);
	}else{
		//获取组织ID
		var bizunitId = $("#orgId").val();
		var dates= new Array(); //定义一数组;
		dates = getFirestAndLastToMonthDay();
		//开始时间
		var beginTime = dates[0];
		//结束时间
		var endTime = dates[1];
		$("#beginTime").val(beginTime);
		$("#endTime").val(endTime);
		$("#hiddenbeginTime").val(beginTime);
		$("#hiddenendTime").val(endTime);
		//按组织获取故障抢修工单数量
		getUrgentRepairworkorderStatisticsByDate(bizunitId,beginTime,endTime);
	}

}

//点击确定时获取故障抢修工单数量
function clickGetUrgentRepairworkorderStatistics(){
	//组织ID
	var orgId = $("#orgId").val();
	//开始时间
	var beginTime = $("#hiddenbeginTime").val();
	//结束时间
	var endTime = $("#hiddenendTime").val();
	//按组织获取故障抢修工单数量
	getUrgentRepairworkorderStatisticsByDate(orgId,beginTime,endTime);
}

//点击确定时获取故障抢修工单数量
function clickGetUrgentRepairworkorderStatisticsGROUPBY(rowName,rowString){
	//组织ID
	var orgId = $("#orgId").val();
	//开始时间
	var beginTime = $("#hiddenbeginTime").val();
	//结束时间
	var endTime = $("#hiddenendTime").val();
	//按组织获取故障抢修工单数量
	getUrgentRepairworkorderStatisticsByDateGROUPBY(orgId,beginTime,endTime,rowName,rowString);
}

//点击确定时根据TAB的显示隐藏获取故障抢修工单数量
function clickGetUrgentRepairworkorderReport(){
	//判断按组织TAB是否为显示
	if($(".ontab").text().replace(" ","") == "按组织"){
		if($("#typecheckbox").attr("checked") == "checked"){
				var type = $(".uradio:checked").val();
				var text = $(".uradio:checked").attr("title");
				//clickGetUrgentRepairworkorderStatisticsGROUPBY(type,text);	
				var orgId = $("#orgId").val();
				var beginTime = $("#hiddenbeginTime").val();
				getReportComment("urgentRepair", type, orgId, beginTime);
			}else{
				//获取故障抢修工单数量
				//clickGetUrgentRepairworkorderStatistics();	
				if($(".selected").text().replace(" ","") == "对比"){
					var orgId = $("#orgId").val();
					var beginTime = $("#hiddenbeginTime").val();
					getReportComment("urgentRepair", "organization", orgId, beginTime);
				}else if($(".selected").text().replace(" ","") == "同比"){
					var orgId = $("#orgId").val();
					var beginTime = $("#hiddenbeginTime").val();
					getReportComment("urgentRepairPeriod", "org", orgId, beginTime);
				}else if($(".selected").text().replace(" ","") == "环比"){
					var orgId = $("#orgId").val();
					var beginTime = $("#hiddenbeginTime").val();
					getReportComment("urgentRepairRing", "org", orgId, beginTime);
				}
				
			}
	}else if($(".ontab").text().replace(" ","") == "按项目"){
		if($("#typecheckbox").attr("checked") == "checked"){
				var type = $(".uradio:checked").val();
				var text = $(".uradio:checked").attr("title");
				//clickGetUrgentRepairworkorderStatisticsGROUPBY(type,text);	
				var orgId = $("#orgId").val();
				var beginTime = $("#hiddenbeginTime").val();
				getReportComment("urgentRepairProject", type, orgId, beginTime);
			}else{
				//获取故障抢修工单数量
				//clickGetUrgentRepairworkorderStatistics();	
				if($(".selected").text().replace(" ","") == "对比"){
					var orgId = $("#orgId").val();
					var beginTime = $("#hiddenbeginTime").val();
					getReportComment("urgentRepairProject", "organization", orgId, beginTime);
				}else if($(".selected").text().replace(" ","") == "同比"){
					var orgId = $("#orgId").val();
					var beginTime = $("#hiddenbeginTime").val();
					getReportComment("urgentRepairPeriodProject", "org", orgId, beginTime);
				}else if($(".selected").text().replace(" ","") == "环比"){
					var orgId = $("#orgId").val();
					var beginTime = $("#hiddenbeginTime").val();
					getReportComment("urgentRepairRingProject", "org", orgId, beginTime);
				}
				
			}
	}
	//}if($(".ontab").text() == "按基站类型"){
		//获取故障抢修工单数量
	//	clickGetUrgentRepairworkorderStatisticsGROUPBY("BaseStationLevel",$(".ontab").text());	
	//}if($(".ontab").text() == "按故障类型"){
		//获取故障抢修工单数量
	//	clickGetUrgentRepairworkorderStatisticsGROUPBY("FaultType",$(".ontab").text());	
	//}if($(".ontab").text() == "按告警级别"){
		//获取故障抢修工单数量
	//	clickGetUrgentRepairworkorderStatisticsGROUPBY("FaultLevel",$(".ontab").text());	
	//}if($(".ontab").text() == "按受理专业"){
		//获取故障抢修工单数量
	//	clickGetUrgentRepairworkorderStatisticsGROUPBY("AcceptProfessional",$(".ontab").text());	
	//}if($(".ontab").text() == "按故障大类"){
		//获取故障抢修工单数量
	//	clickGetUrgentRepairworkorderStatisticsGROUPBY("FaultGenera",$(".ontab").text());	
	//}
}
//按组织获取故障抢修工单数量
function getUrgentRepairworkorderStatisticsByDate(orgId,beginTime,endTime){
getReportComment("urgentRepair", "organization", orgId, beginTime);
//访问地址
var url = "getUrgentRepairProcessTimeRateByBizunitinnstAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
	$.post(url, params, function(data){

		data = eval(data);
		//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr>"
								+"<th style='width:55px;'>项目</th>"
								+"<th>工单数</th>"
								+"<th>工单处理历时 (小时)</th>"
								+"<th>故障处理及时率</th>"
							+"</tr>";
							
		var categories= new Array(); //定义一数组;	
		//图形报表数据源
		var series = "";
		var workorderhtml = "{ name: '工单数', data: [";
		var processTimehtml = "{ name: '超时工单数', data: [";
		var workorderCount = 0;
		var processTimeCount = 0;
		var i = 0;		
		var selectDivCountext = "";
		$.each(data, function(key, value){
			workorderhtml = workorderhtml + value.wCount + ",";
			processTimehtml = processTimehtml + value.wCount + ",";
			var orgNameC = value.orgName.replace("(","");
			orgNameC = orgNameC.replace(")","");
			orgNameC = orgNameC.replace("(","");
			orgNameC = orgNameC.replace(")","");
			counttext = counttext + "<tr>"
								+"<td>"+value.orgName+"</td>"
								+"<td><a href='#' onclick='clickShowMoreTable("+value.orgId+",\""+value.orgName+"\")'>"+value.wCount+"</a></td>"
								+"<td id='"+orgNameC+"ProcessTime'></td>"
								+"<td id='"+orgNameC+"TroudleshootingTime'></td>"
							"</tr>";
							var planTitle = "";
					if(value.orgName.length > 8 && value.orgName != null){
						var parseIndex = Math.ceil(value.orgName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.orgName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.orgName;
					}
					categories[i]=planTitle;
							i++;
		    workorderCount = workorderCount + value.wCount;
		    selectDivCountext = selectDivCountext+"<div id='"+orgNameC+"'>"+value.orgId+"</div>";
		});
		$("#selectDivHidden").html(selectDivCountext);
		workorderhtml = workorderhtml + "]},";
		processTimehtml = processTimehtml + "]},";
		series = "[" +workorderhtml + processTimehtml + "]";
		counttext = counttext + "<tr>"
								+"<td>汇总/平均</td>"
								+"<td>"+workorderCount+"</td>"
								+"<td id='ProcessTime'></td>"
								+"<td id='TroudleshootingTime'></td>"
							"</tr>";
		counttext = counttext + "</table>";
		var uCount = workorderCount + processTimeCount;
		if(!uCount){
			uCount = 0;
		}else{
			uCount = uCount + "";
			if(uCount.indexOf(".") > 0){
				uCount = uCount.substring(0,uCount.indexOf(".") + 2);
			}
		}
		$("#uCount").text(workorderCount);
		var upCount = (workorderCount)/i;
		if(!uCount){
			upCount = 0;
		}else{
			upCount = upCount + "";
			if(upCount.indexOf(".") > 0){
				upCount = upCount.substring(0,upCount.indexOf(".") + 2);
			}
		}
		$("#upCount").text(upCount);
		$("#stationTableDiv").html(counttext);
		getUrgentRepairworkorderProcessTimeRateByDate(data,orgId,beginTime,endTime);
	});
}


//按组织获取故障抢修工单数量(按类型)
function getUrgentRepairworkorderStatisticsByDateGROUPBY(orgId,beginTime,endTime,rowName,rowString){
getReportComment("urgentRepair", rowName, orgId, beginTime);
//访问地址
var url = "getUrgentRepairProcessTimeRateBy"+rowName+"Action";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
	$.post(url, params, function(data){
		data = eval(data);
		//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr>"
								+"<th style='width:55px;'>"+rowString+"</th>"
								+"<th>工单数</th>"
								+"<th>工单处理历时 (小时)</th>"
								+"<th>故障处理及时率</th>"
							+"</tr>";
							
		var categories= new Array(); //定义一数组;	
		//图形报表数据源
		var series = "";
		var workorderhtml = "{ name: '工单数', data: [";
		var processTimehtml = "{ name: '超时工单数', data: [";
		var workorderCount = 0;
		var processTimeCount = 0;
		var i = 0;		
		var selectDivCountext = "";
		$.each(data, function(key, value){
			workorderhtml = workorderhtml + value.wCount + ",";
			processTimehtml = processTimehtml + value.ProcessTimeCount + ",";
			counttext = counttext + "<tr>"
								+"<td>"+value.statisticsType+"</td>"
								+"<td><a href='#' onclick='clickShowMoreTable("+orgId+",\""+value.statisticsType+"\")'>"+value.wCount+"</a></td>"
								+"<td id='"+value.statisticsType+"ProcessTime'></td>"
								+"<td id='"+value.statisticsType+"TroudleshootingTime'></td>"
							"</tr>";
							var planTitle = "";
							if(value.statisticsType.length > 8 && value.statisticsType != null){
								var parseIndex = Math.ceil(value.statisticsType.length/8);
								if(parseIndex != null && parseIndex > 0){
									for(var j = 0;j < parseIndex;j++){
										var sIndex = j*8;
										var dIndex = (j+1)*8;
										planTitle = planTitle + value.statisticsType.substring(sIndex,dIndex) + "<br/>";
										//alert(planTitle);
									}
								}
							}else{
								planTitle = value.statisticsType;
							}
							categories[i]=planTitle;
							i++;
		    workorderCount = workorderCount + value.wCount;
		    processTimeCount = processTimeCount + value.ProcessTimeCount;
		    selectDivCountext = selectDivCountext+"<div id='"+value.statisticsType+"'>"+orgId+"</div>";
		});
		$("#selectDivHidden").html(selectDivCountext);
		workorderhtml = workorderhtml + "]},";
		processTimehtml = processTimehtml + "]},";
		series = "[" +workorderhtml + processTimehtml + "]";
		counttext = counttext + "<tr>"
								+"<td>汇总</td>"
								+"<td>"+workorderCount+"</td>"
								+"<td id='ProcessTime'></td>"
								+"<td id='TroudleshootingTime'></td>"
							"</tr>";
		counttext = counttext + "</table>";
		var uCount = workorderCount + processTimeCount;
		if(!uCount){
			uCount = 0;
		}else{
			uCount = uCount + "";
			if(uCount.indexOf(".") > 0){
				uCount = uCount.substring(0,uCount.indexOf(".") + 2);
			}
		}
		$("#uCount").text(workorderCount);
		var upCount = (workorderCount)/i;
		if(upCount){
		
			upCount = upCount + "";
			if(upCount.indexOf(".") > 0){
				upCount = upCount.substring(0,upCount.indexOf(".") + 2);
			}
		}
		$("#upCount").text(upCount);
		$("#stationTableDiv").html(counttext);
		getUrgentRepairworkorderProcessTimeRateByDateGROUPBY(data,orgId,beginTime,endTime,rowName,rowString);
	});
}

//获取当前时间的月份的第一天与最后一天的时间
function getFirestAndLastToMonthDay(){
	var aDate = new Date();
	var year=aDate.getFullYear();
	var month=aDate.getMonth();
	if(month == 0){
					year = year - 1;
					month = 12;
				}
	var dates= new Array(); //定义一数组;
	//根据年月获取月第一个天与最后一天
	dates = getFirstAndLastMonthDay(year, month);
	return dates;
}

//计算年月
function getYearMonth(year, month){
	if(month > 12){
		month = 1;
		year = year + 1;
	}
	if(month < 1){
		month = 12;
		year = year -1;
	}
	var dates= new Array(); //定义一数组;
	dates[0] = year;
   dates[1] = month;
   return dates;
}

//获取上一个月的故障抢修工单数量
function getlastMonth(){
	var year = $("#hiddenYear").val();
	var month = $("#hiddenMonth").val();
	$(".hitarea-div_close").removeClass("hitarea-div_open");
	$("#stationTableDiv").hide();
	year = parseInt(year);
	month = parseInt(month);
	month = month - 1;
	var dates= new Array(); //定义一数组;
	dates = getYearMonth(year, month);
	$("#hiddenYear").val(dates[0]);
	$("#hiddenMonth").val(dates[1]);
	var spanMonth = "";
	if(dates[1] < 10){
		spanMonth = "0"+dates[1];
	}else{
		spanMonth = dates[1];
	}
	$("#DateTimeSpan").text(dates[0]+"年"+spanMonth+"月");
	var lastMonthDates= new Array(); //定义一数组;
	//根据年月获取月第一个天与最后一天
	lastMonthDates = getFirstAndLastMonthDay(dates[0], dates[1]);
	var beginTime = lastMonthDates[0];
	var endTime = lastMonthDates[1];
	$("#beginTime").val(beginTime);
	$("#endTime").val(endTime);
	$("#hiddenbeginTime").val(beginTime);
	$("#hiddenendTime").val(endTime);
	//点击确定时根据TAB的显示隐藏获取故障抢修工单数量
	if($(".ontab").text().replace(" ","") == "按组织"){
		getReportByorginstByCheckbox();
	}else if($(".ontab").text().replace(" ","") == "按项目"){
		//获取故障抢修工单数量
		clickGetUrgentRepairworkorderStatisticsProject();	
	}
}

//获取下一个月的故障抢修工单数量
function getNextMonth(){
	var year = $("#hiddenYear").val();
	var month = $("#hiddenMonth").val();
	$(".hitarea-div_close").removeClass("hitarea-div_open");
	$("#stationTableDiv").hide();
	year = parseInt(year);
	month = parseInt(month);
	month = month + 1;
	var dates= new Array(); //定义一数组;
	dates = getYearMonth(year, month);
	$("#hiddenYear").val(dates[0]);
	$("#hiddenMonth").val(dates[1]);
	var spanMonth = "";
	if(dates[1] < 10){
		spanMonth = "0"+dates[1];
	}else{
		spanMonth = dates[1];
	}
	$("#DateTimeSpan").text(dates[0]+"年"+spanMonth+"月");
	var nextMonthDates= new Array(); //定义一数组;
	//根据年月获取月第一个天与最后一天
	nextMonthDates = getFirstAndLastMonthDay(dates[0], dates[1]);
	var beginTime = nextMonthDates[0];
	var endTime = nextMonthDates[1];
	$("#beginTime").val(beginTime);
	$("#endTime").val(endTime);
	$("#hiddenbeginTime").val(beginTime);
	$("#hiddenendTime").val(endTime);
	//点击确定时根据TAB的显示隐藏获取故障抢修工单数量
	if($(".ontab").text().replace(" ","") == "按组织"){
		getReportByorginstByCheckbox();
	}else if($(".ontab").text().replace(" ","") == "按项目"){
		//获取故障抢修工单数量
		clickGetUrgentRepairworkorderStatisticsProject();	
	}
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

//TAB切换是按组织加载故障工单数量
function getReportByorginst(me){
	$("#compareTab li").removeClass("selected");
   	$("#Dselected").addClass("selected");
	$("#orgId").val($("#orgIdT").val());
	$("#orgName").val($("#orgNameT").val());
	$("#pieBasicDiv").html("");
	$("#columnBasicDiv").html("");
	$("#uradiodiv").hide();
	$("#statements_classify").show();
	$(".hitarea-div_close").removeClass("hitarea-div_open");
	$("#stationTableDiv").hide();
	$(".tab_menu ul li").removeClass("ontab");
	$(me).addClass("ontab");
	$("#statements_back").hide();
	$("#typecheckbox").removeAttr("checked");
	//判断按组织TAB是否为显示
	if($(me).text() == "按组织"){
				//获取故障抢修工单数量
				$("#statements_classify").show();
				$("#Period").show();
				$("#Ring").show();
				clickGetUrgentRepairworkorderStatistics();	
	}else if($(me).text() == "按项目"){
		//获取故障抢修工单数量
				$("#statements_classify").hide();
				$("#Period").hide();
				$("#Ring").hide();
				clickGetUrgentRepairworkorderStatisticsProject();	
	}
	//}if($(me).text() == "按基站类型"){
		//获取故障抢修工单数量
	//	clickGetUrgentRepairworkorderStatisticsGROUPBY("BaseStationLevel",$(me).text());	
	//}if($(me).text() == "按故障类型"){
		//获取故障抢修工单数量
	//	clickGetUrgentRepairworkorderStatisticsGROUPBY("FaultType",$(me).text());	
	//}if($(me).text() == "按告警级别"){
		//获取故障抢修工单数量
	//	clickGetUrgentRepairworkorderStatisticsGROUPBY("FaultLevel",$(me).text());	
	//}if($(me).text() == "按受理专业"){
		//获取故障抢修工单数量
	//	clickGetUrgentRepairworkorderStatisticsGROUPBY("AcceptProfessional",$(me).text());	
	//}if($(me).text() == "按故障大类"){
		//获取故障抢修工单数量
	//	clickGetUrgentRepairworkorderStatisticsGROUPBY("FaultGenera",$(me).text());	
	//}
	$("#bizunitinstDiv").show();
	$("#stationTypeDiv").hide();
	//获取故障抢修工单数量
}

function getReportByorginstByRadio(){
	if($("#typecheckbox").attr("checked") == "checked"){
		var type = $(".uradio:checked").val();
		var text = $(".uradio:checked").attr("title");
		clickGetUrgentRepairworkorderStatisticsGROUPBY(type,text);	
	}
}

function getReportByorginstByCheckbox(){
	if($(".selected").text().replace(" ","") == "对比"){
		if($("#typecheckbox").attr("checked") == "checked"){
		var type = $(".uradio:checked").val();
		var text = $(".uradio:checked").attr("title");
		$("#uradiodiv").show();
		clickGetUrgentRepairworkorderStatisticsGROUPBY(type,text);	
	}else{
		$("#uradiodiv").hide();
		clickGetUrgentRepairworkorderStatistics();	
	}
	}else if($(".selected").text().replace(" ","") == "同比"){
		getUPeriod();
	}else if($(".selected").text().replace(" ","") == "环比"){
		getURing();
	}
	
}



//按基站类型获取故障抢修工单数量
function getUrgentRepairworkorderStatisticsByStationType(){
var orgId = $("#orgId").val();
	var beginTime = $("#hiddenbeginTime").val();
	var endTime = $("#hiddenendTime").val();
getReportComment("urgentRepair", "station", orgId, beginTime);
var url = "getUrgentRepairworkorderCountByNetworkResourceTypeAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
	$.post(url, params, function(data){
		data = eval(data);
		//图形报表数据源
		var series = "";
		var categories= new Array(); //定义一数组;
		var normalStationCount = 0;
		var VIPStationCount = 0;
		var normalStationhtml = "{ name: '普通', data: [";
		var VIPStationhtml = "{ name: 'VIP', data: [";
		//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr class='main_table_title'>"
								+"<td style='width:35px;'></td>"
								+"<td colspan='4'>基站重要级别</td>"
							+"</tr>"
							+"<tr>"
								+"<th style='width:55px;'>项目</th>"
								+"<th>VIP</th>"
								+"<th>普通</th>"
							+"</tr>";
		var i = 0;
		var selectDivCountext = "";
		$.each(data, function(key, value){
			var VIPTableCount = 0;
			var normalTableCount = 0;
			if(value['VIP']){
				VIPStationCount = VIPStationCount + value['VIP'];
				VIPStationhtml = VIPStationhtml + value['VIP'] + ",";
				VIPTableCount = value['VIP'];
			}else{
				VIPStationhtml = VIPStationhtml + "0,";
			}
			if(value['普通']){
				normalStationCount = normalStationCount + value['普通'];
				normalStationhtml = normalStationhtml + value['普通'] + ",";
				normalTableCount = value['普通'];
			}else{
				normalStationhtml = normalStationhtml + "0,";
			}
			categories[i] = value['bizunitinnstName'];
			counttext = counttext + "<tr>"
								+"<td>"+value['bizunitinnstName']+"</td>"
								+"<td>"+VIPTableCount+"</td>"
								+"<td>"+normalTableCount+"</td>"
							"</tr>";
			i++;
			selectDivCountext = selectDivCountext+"<div id='"+value['orgName']+"'>"+value['orgId']+"</div>";
		});
		$("#selectDivHidden").html(selectDivCountext);
		normalStationhtml = normalStationhtml + "]},";
		VIPStationhtml = VIPStationhtml + "]},";
		series = "[" +normalStationhtml + VIPStationhtml + "]";
		counttext = counttext + "<tr>"
								+"<td>汇总</td>"
								+"<td>"+VIPStationCount+"</td>"
								+"<td>"+normalStationCount+"</td>"
							"</tr>";
		series = eval(series);
        var seriesData = "[['VIP',"+ VIPStationCount + "],['普通'," + normalStationCount +"]]";
        seriesData = eval(seriesData);
        var title = "抢修处理历时统计("+$("#orgName").val()+","+$("#DateTimeSpan").text()+")";
		var subtitle = "";
		var yAxisTitle = "工单数量(张)";
		$("#stationTableDiv").html(counttext);
		thisDataP.thistitle = title;
		thisDataP.thissubtitle = subtitle;
		thisDataP.thisseries = seriesData;
		thisDataB.thistitle = title;
		thisDataB.thissubtitle = subtitle;
		thisDataB.thisyAxisTitle = yAxisTitle;
		thisDataB.thiscategories = categories;
		thisDataB.thisseries = series;
		pieBasic(title,subtitle,"pieBasicDiv",seriesData);
		columnBasic(title,subtitle,yAxisTitle,"columnBasicDiv",categories,series);
	});
}


//按组织获取故障抢修处理及时率
function getUrgentRepairworkorderProcessTimeRateByDate(data,orgId,beginTime,endTime){
//访问地址
var url = "getUrgentRepairworkorderProcessTimeRateByBizunitinnstAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
		//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr class='main_table_title'>"
								+"<td style='width:35px;'></td>"
								+"<td colspan='4'>故障处理及时率</td>"
							+"</tr>"
							+"<tr>"
								+"<th style='width:55px;'>项目</th>"
								+"<th>故障处理及时率</th>"
								+"<th>故障处理及时率时(小时)</th>"
							+"</tr>";
							
		var categories= new Array(); //定义一数组;	
		//图形报表数据源
		var series = "";
		var wCounthtml = "{ type:'column', name: '工单数', yAxis: 1, data: [ ";
		var processTimeRatehtml = "{ type: 'line' ,name: '工单处理历时 (小时)',color: '#FF6600', data: [ ";
		var troudleshootingTimeAverageCount = 0;
		var processTimeRateCount = 0;
		var i = 0;		
		var selectDivCountext = "";
		var size = 0;
		$.each(data, function(key, value){
			size++;
			wCounthtml = wCounthtml + value.wCount + ",";
			var parseTime =  parseFloat(value.troudleshootingTimeAverage/60)+"";
			if(parseTime.indexOf(".") >= 0){
				parseTime = parseTime.substring(0,parseTime.indexOf(".") + 3);
			}
			processTimeRatehtml = processTimeRatehtml + parseTime + ",";
			var orgNameC = value.orgName.replace("(","");
			orgNameC = orgNameC.replace(")","");
			orgNameC = orgNameC.replace("(","");
			orgNameC = orgNameC.replace(")","");
			$("#"+orgNameC+"TroudleshootingTime").text(value.processTimeRateCount);
			counttext = counttext + "<tr>"
								+"<td>"+value.orgName+"</td>"
								+"<td>"+value.processTimeRateCount+"%</td>"
								+"<td>"+value.wCount+"</td>"
							"</tr>";
							var planTitle = "";
					if(value.orgName.length > 8 && value.orgName != null){
						var parseIndex = Math.ceil(value.orgName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.orgName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.orgName;
					}
					categories[i]=planTitle;
							i++;
		    troudleshootingTimeAverageCount = troudleshootingTimeAverageCount + parseFloat(value.troudleshootingTimeAverage);
		    processTimeRateCount = processTimeRateCount + parseFloat(value.processTimeRateCount);
		     selectDivCountext = selectDivCountext+"<div id='"+orgNameC+"'>"+value.orgId+"</div>";
		});
		$("#selectDivHidden").html(selectDivCountext);
		if(wCounthtml != null && wCounthtml != ""){
			wCounthtml = wCounthtml.substring(0,wCounthtml.length - 1);
		}
		if(processTimeRatehtml != null && processTimeRatehtml != ""){
			processTimeRatehtml = processTimeRatehtml.substring(0,processTimeRatehtml.length - 1);
		}
		wCounthtml = wCounthtml + "]},";
		processTimeRatehtml = processTimeRatehtml + "]}";
		
		series = "[" +  wCounthtml + processTimeRatehtml + "]";
		processTimeRateCount = processTimeRateCount/size;
		troudleshootingTimeAverageCount = troudleshootingTimeAverageCount/size;
		counttext = counttext + "<tr>"
								+"<td>平均</td>"
								+"<td>"+parseFloat(processTimeRateCount)+"%</td>"
								+"<td>"+parseFloat(troudleshootingTimeAverageCount)+"</td>"
							"</tr>";
		counttext = counttext + "</table>";
		var parseFloatTime =  processTimeRateCount + "";
		if(parseFloatTime.indexOf(".") >= 0){
			parseFloatTime = parseFloatTime.substring(0,parseFloatTime.indexOf(".") + 3);
		}
		$("#TroudleshootingTime").text(parseFloatTime);
		series = eval(series);
		var title = "抢修处理历时统计("+$("#orgName").val()+","+$("#DateTimeSpan").text()+")";
		var subtitle = "(点击图形区域,可向下钻取)";
		var yAxisTitle2 = "工单数 (张)";
		var yAxisTitle = "工单处理历时 (小时)";
		//columnBasic(title,subtitle,yAxisTitle,"pieBasicDiv",categories,series);
		thisDataP.thistitle = title;
		thisDataP.thissubtitle = subtitle;
		thisDataP.thisyAxisTitle2 = yAxisTitle2;
		thisDataP.thisyAxisTitle = yAxisTitle;
		thisDataP.thiscategories = categories;
		thisDataP.thisseries = series;
		columnAndSplineBasicUR(title,subtitle,yAxisTitle,yAxisTitle2,"pieBasicDiv",categories,series)
		getUrgentRepairworkorderTroudleshootingTimeByDate(data,orgId,beginTime,endTime);
		//columnBasic(title,subtitle,yAxisTitle,"columnBasicDiv",categories,series);
}




//按组织获取故障抢修处理历时(按类型)
function getUrgentRepairworkorderTroudleshootingTimeByDate(data,orgId,beginTime,endTime){
//访问地址
var url = "getUrgentRepairworkorderProcessTimeRateByBizunitinnstAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
		//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr class='main_table_title'>"
								+"<td style='width:35px;'></td>"
								+"<td colspan='4'>故障处理及时率</td>"
							+"</tr>"
							+"<tr>"
								+"<th style='width:55px;'>项目</th>"
								+"<th>故障处理及时率</th>"
								+"<th>故障处理及时率</th>"
							+"</tr>";
							
		var categories= new Array(); //定义一数组;	
		//图形报表数据源
		var series = "";
		var wCounthtml = "{ type:'column', name: '工单数', yAxis: 1, data: [";
		var processTimeRatehtml = "{ type: 'line',name: '工单处理历时 (小时)', yAxis: 1,color: '#FF6600', data: [";
		var processRatehtml = "{ type: 'line',name: '故障处理及时率 (%)',color: '#336600', data: [";
		var troudleshootingTimeAverageCount = 0;
		var processTimeRateCount = 0;
		var i = 0;		
		var selectDivCountext = "";
		var size = 0;
		$.each(data, function(key, value){
			size++;
			var parseTime =  parseFloat(value.troudleshootingTimeAverage/60)+"";
			if(parseTime.indexOf(".") >= 0){
				parseTime = parseTime.substring(0,parseTime.indexOf(".") + 3);
			}
			var orgNameC = value.orgName.replace("(","");
			orgNameC = orgNameC.replace(")","");
			orgNameC = orgNameC.replace("(","");
			orgNameC = orgNameC.replace(")","");
			$("#"+orgNameC+"ProcessTime").text(parseTime);
			wCounthtml = wCounthtml + value.wCount + ",";
			processTimeRatehtml = processTimeRatehtml + parseTime + ",";
			processRatehtml = processRatehtml + value.processTimeRateCount + ",";
			counttext = counttext + "<tr>"
								+"<td>"+value.orgName+"</td>"
								+"<td>"+value.processTimeRateCount+"%</td>"
								+"<td>"+value.wCount+"</td>"
							"</tr>";
							var planTitle = "";
					if(value.orgName.length > 8 && value.orgName != null){
						var parseIndex = Math.ceil(value.orgName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.orgName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.orgName;
					}
					categories[i]=planTitle;
							i++;
		    troudleshootingTimeAverageCount = troudleshootingTimeAverageCount + parseFloat(value.troudleshootingTimeAverage);
		    processTimeRateCount = processTimeRateCount + parseFloat(value.processTimeRateCount);
		     selectDivCountext = selectDivCountext+"<div id='"+orgNameC+"'>"+value.orgId+"</div>";
		});
		$("#selectDivHidden").html(selectDivCountext);
		if(wCounthtml != null && wCounthtml != ""){
			wCounthtml = wCounthtml.substring(0,wCounthtml.length - 1);
		}
		if(processRatehtml != null && processRatehtml != ""){
			processRatehtml = processRatehtml.substring(0,processRatehtml.length - 1);
		}
		wCounthtml = wCounthtml + "]},";
		processTimeRatehtml = processTimeRatehtml + "]},";
		processRatehtml = processRatehtml + "]}";
		series = "[" + wCounthtml +  processRatehtml + "]";
		processTimeRateCount = processTimeRateCount/size;
		troudleshootingTimeAverageCount = troudleshootingTimeAverageCount/size;
		counttext = counttext + "<tr>"
								+"<td>平均</td>"
								+"<td>"+parseFloat(processTimeRateCount)+"%</td>"
								+"<td>"+parseFloat(troudleshootingTimeAverageCount)+"</td>"
							"</tr>";
		var parseFloatTime =  parseFloat(troudleshootingTimeAverageCount/60)+"";
		if(parseFloatTime.indexOf(".") >= 0){
			parseFloatTime = parseFloatTime.substring(0,parseFloatTime.indexOf(".") + 3);
		}
		$("#uProcessTime").text(parseFloatTime);
		$("#ProcessTime").text(parseFloatTime);
		counttext = counttext + "</table>";
		series = eval(series);
		var title = "抢修及时率统计("+$("#orgName").val()+","+$("#DateTimeSpan").text()+")";
		var subtitle = "";
		var yAxisTitle = "故障处理及时率 (%)";
		var yAxisTitle2 = "工单数 (张)";
		thisDataB.thistitle = title;
		thisDataB.thissubtitle = subtitle;
		thisDataB.thisyAxisTitle2 = yAxisTitle2;
		thisDataB.thisyAxisTitle = yAxisTitle;
		thisDataB.thiscategories = categories;
		thisDataB.thisseries = series;
		columnAndSplineBasicU(title,subtitle,yAxisTitle,yAxisTitle2,"columnBasicDiv",categories,series);
}


//按组织获取故障抢修处理及时率(按类型)
function getUrgentRepairworkorderProcessTimeRateByDateGROUPBY(data,orgId,beginTime,endTime,rowName,rowString){
//访问地址
var url = "getUrgentRepairworkorderProcessTimeRateBy"+rowName+"Action";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
		//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr class='main_table_title'>"
								+"<td style='width:35px;'></td>"
								+"<td colspan='4'>故障处理及时率</td>"
							+"</tr>"
							+"<tr>"
								+"<th style='width:55px;'>项目</th>"
								+"<th>故障处理及时率</th>"
								+"<th>故障处理平均历时(小时)</th>"
							+"</tr>";
							
		var categories= new Array(); //定义一数组;	
		//图形报表数据源
		var series = "";
		var wCounthtml = "[ ";
		var troudleshootingTimeAverageCount = 0;
		var processTimeRateCount = 0;
		var i = 0;		
		var selectDivCountext = "";
		var size = 0;
		$.each(data, function(key, value){
			size++;
			wCounthtml = wCounthtml + "['" + value.statisticsType + "'," + value.wCount + "],";
			$("#"+value.statisticsType+"TroudleshootingTime").text(value.processTimeRateCount);
			counttext = counttext + "<tr>"
								+"<td>"+value.statisticsType+"</td>"
								+"<td>"+value.processTimeRateCount+"%</td>"
								+"<td>"+value.wCount+"</td>"
							"</tr>";
							var planTitle = "";
							if(value.statisticsType.length > 8 && value.statisticsType != null){
								var parseIndex = Math.ceil(value.statisticsType.length/8);
								if(parseIndex != null && parseIndex > 0){
									for(var j = 0;j < parseIndex;j++){
										var sIndex = j*8;
										var dIndex = (j+1)*8;
										planTitle = planTitle + value.statisticsType.substring(sIndex,dIndex) + "<br/>";
										//alert(planTitle);
									}
								}
							}else{
								planTitle = value.statisticsType;
							}
							categories[i]=planTitle;
							i++;
		    troudleshootingTimeAverageCount = troudleshootingTimeAverageCount + parseFloat(value.troudleshootingTimeAverage);
		    processTimeRateCount = processTimeRateCount + parseFloat(value.processTimeRateCount);
		     selectDivCountext = selectDivCountext+"<div id='"+value.statisticsType+"'>"+value.statisticsType+"</div>";
		});
		$("#selectDivHidden").html(selectDivCountext);
		if(wCounthtml != null && wCounthtml != ""){
			wCounthtml = wCounthtml.substring(0,wCounthtml.length - 1);
		}
		wCounthtml = wCounthtml + "]";
		series = wCounthtml;
		processTimeRateCount = processTimeRateCount/size;
		troudleshootingTimeAverageCount = troudleshootingTimeAverageCount/size;
		counttext = counttext + "<tr>"
								+"<td>平均</td>"
								+"<td>"+parseFloat(processTimeRateCount)+"%</td>"
								+"<td>"+parseFloat(troudleshootingTimeAverageCount)+"</td>"
							"</tr>";
		counttext = counttext + "</table>";
		var parseFloatTime = processTimeRateCount + "";
		if(parseFloatTime.indexOf(".") >= 0){
			parseFloatTime = parseFloatTime.substring(0,parseFloatTime.indexOf(".") + 3);
		}
		$("#TroudleshootingTime").text(parseFloatTime);
		series = eval(series);
		var title = "抢修处理历时统计("+$("#orgName").val()+","+$("#DateTimeSpan").text()+")";
		var subtitle = "";
		var yAxisTitle = "工单数(张)";
		thisDataP.thistitle = title;
		thisDataP.thissubtitle = subtitle;
		thisDataP.thiscategories = categories;
		thisDataP.thisseries = series;
		pieBasic2(title,subtitle,"pieBasicDiv",series);
		//pieBasic(title,subtitle,yAxisTitle,"pieBasicDiv",categories,series);
		getUrgentRepairworkorderTroudleshootingTimeByDateGROUPBY(data,orgId,beginTime,endTime,rowName,rowString);
}




//按组织获取故障抢修处理历时
function getUrgentRepairworkorderTroudleshootingTimeByDateGROUPBY(data,orgId,beginTime,endTime,rowName,rowString){
//访问地址
var url = "getUrgentRepairworkorderProcessTimeRateBy"+rowName+"Action";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
		//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr class='main_table_title'>"
								+"<td style='width:35px;'></td>"
								+"<td colspan='4'>故障处理及时率</td>"
							+"</tr>"
							+"<tr>"
								+"<th style='width:55px;'>项目</th>"
								+"<th>故障处理及时率</th>"
								+"<th>故障处理平均历时(小时)</th>"
							+"</tr>";
							
		var categories= new Array(); //定义一数组;	
		//图形报表数据源
		var series = "";
		var processTimeRatehtml = "{ type: 'line',name: '工单处理历时 (小时)', yAxis: 1,color: '#FF6600', data: [ ";
		var processRatehtml = "{ type: 'line',name: '故障处理及时率 (%)',color: '#336600', data: [ ";
		var troudleshootingTimeAverageCount = 0;
		var processTimeRateCount = 0;
		var i = 0;		
		var selectDivCountext = "";
		var size = 0;
		$.each(data, function(key, value){
			size++;
			processRatehtml = processRatehtml + value.processTimeRateCount + ",";
			var parseTime =  parseFloat(value.troudleshootingTimeAverage/60)+"";
			if(parseTime.indexOf(".") >= 0){
				parseTime = parseTime.substring(0,parseTime.indexOf(".") + 3);
			}
			$("#"+value.statisticsType+"ProcessTime").text(parseTime);
			processTimeRatehtml = processTimeRatehtml + parseTime + ",";
			counttext = counttext + "<tr>"
								+"<td>"+value.statisticsType+"</td>"
								+"<td>"+value.processTimeRateCount+"%</td>"
								+"<td>"+value.wCount+"</td>"
							"</tr>";
							var planTitle = "";
							if(value.statisticsType.length > 8 && value.statisticsType != null){
								var parseIndex = Math.ceil(value.statisticsType.length/8);
								if(parseIndex != null && parseIndex > 0){
									for(var j = 0;j < parseIndex;j++){
										var sIndex = j*8;
										var dIndex = (j+1)*8;
										planTitle = planTitle + value.statisticsType.substring(sIndex,dIndex) + "<br/>";
										//alert(planTitle);
									}
								}
							}else{
								planTitle = value.statisticsType;
							}
							categories[i]=planTitle;
							i++;
		    troudleshootingTimeAverageCount = troudleshootingTimeAverageCount + parseFloat(value.troudleshootingTimeAverage);
		    processTimeRateCount = processTimeRateCount + parseFloat(value.processTimeRateCount);
		     selectDivCountext = selectDivCountext+"<div id='"+value.statisticsType+"'>"+value.statisticsType+"</div>";
		});
		$("#selectDivHidden").html(selectDivCountext);
		if(processRatehtml != null && processRatehtml != ""){
			processRatehtml = processRatehtml.substring(0,processRatehtml.length - 1);
		}
		if(processTimeRatehtml != null && processTimeRatehtml != ""){
			processTimeRatehtml = processTimeRatehtml.substring(0,processTimeRatehtml.length - 1);
		}
		processTimeRatehtml = processTimeRatehtml + "]},";
		processRatehtml = processRatehtml + "]}";
		series = "[" + processTimeRatehtml +  processRatehtml + "]";
		processTimeRateCount = processTimeRateCount/size;
		troudleshootingTimeAverageCount = troudleshootingTimeAverageCount/size;
		counttext = counttext + "<tr>"
								+"<td>平均</td>"
								+"<td>"+parseFloat(processTimeRateCount)+"%</td>"
								+"<td>"+parseFloat(troudleshootingTimeAverageCount)+"</td>"
							"</tr>";
		var parseFloatTime =  parseFloat(troudleshootingTimeAverageCount/60)+"";
		if(parseFloatTime.indexOf(".") >= 0){
			parseFloatTime = parseFloatTime.substring(0,parseFloatTime.indexOf(".") + 3);
		}
		$("#uProcessTime").text(parseFloatTime);
		$("#ProcessTime").text(parseFloatTime);
		counttext = counttext + "</table>";
		series = eval(series);
		var title = "抢修及时率统计("+$("#orgName").val()+","+$("#DateTimeSpan").text()+")";
		var subtitle = "";
		var yAxisTitle = "故障处理及时率 (%)";
		var yAxisTitle2 = "工单处理历时 (小时)";
		thisDataB.thistitle = title;
		thisDataB.thissubtitle = subtitle;
		thisDataB.thisyAxisTitle2 = yAxisTitle2;
		thisDataB.thisyAxisTitle = yAxisTitle;
		thisDataB.thiscategories = categories;
		thisDataB.thisseries = series;
		columnAndSplineBasicL(title,subtitle,yAxisTitle,yAxisTitle2,"columnBasicDiv",categories,series);
}



//获取当前时间的月份的第一天与最后一天的时间
function getDay(day){
	var aDate = new Date();
	var des = day.split("-");
	var year=des[0];
	var month=des[1];
	$("#hiddenYear").val(year);
	$("#hiddenMonth").val(month);
	$("#DateTimeSpan").text(year+"年"+month+"月");
	var dates= new Array(); //定义一数组;
	//根据年月获取月第一个天与最后一天
	dates = getFirstAndLastMonthDay(year, month);
	return dates;
}

 function clickShowMore(divId,name){
 	if($(".selected").text().replace(" ","") == "对比"){
 	var orgId = $("#"+divId).html();
 	$("#orgId").val(orgId);
 	$("#orgName").val(divId);
 	if(orgId != null && orgId != ""){
				$("#returnOrgId").val(orgId);
	}
 	if($(".ontab").text().replace(" ","") == "按组织"){
 		$("#statements_back").show();
	 	getReportByorginstByCheckbox();
 	}
 	}
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
   
   
   function clickClearFix(me){
   		$("#compareTab li").removeClass("selected");
   		$(me).addClass("selected");
   }
   
   
function clickGetUrgentRepairContrast(){
	$("#statements_classify").show();
	//判断按组织TAB是否为显示
	if($(".ontab").text() == "按组织"){
		if($("#typecheckbox").attr("checked") == "checked"){
				var type = $(".uradio:checked").val();
				var text = $(".uradio:checked").attr("title");
				clickGetUrgentRepairworkorderStatisticsGROUPBY(type,text);	
				//var orgId = $("#orgId").val();
				//var beginTime = $("#hiddenbeginTime").val();
				//getReportComment("urgentRepair", type, orgId, beginTime);
			}else{
				//获取故障抢修工单数量
				clickGetUrgentRepairworkorderStatistics();	
				//var orgId = $("#orgId").val();
				//var beginTime = $("#hiddenbeginTime").val();
				//getReportComment("urgentRepair", "organization", orgId, beginTime);
				
			}
	}else if($(".ontab").text() == "按项目"){
		clickGetUrgentRepairworkorderStatisticsProject();
	}
}

var thisData = "";

function getUrgentRepairPeriod(orgId,orgName,beginTime,endTime,TimeString){
	var url = "getUrgentRepairProcessTimeRateByBizunitinnstAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
	 $.ajax({
          type: "POST",                                         //ajax的方式为post(get方式对传送数据长度有限制)
          url: url,           //一般处理程序页面AddUser.ashx(在2中会写出该页面内容)
		  cache : false, 
          async : false,
          dataType: "json",     
	 data: params,       //要传送的数据键值对adduserName为键（方便2中的文件用此名称接受数据）txtuserName为值（要传递的变量，例如用户名）
     success: function (data) {
	var troudleshootingTimeAverageCount = 0;
	var processTimeRateCount = 0;
	var wCount = 0;
	var size = 0;
	var rData = "";
	var ProcessTimeCount = 0;

	$.each(data, function(key, value){
		wCount = wCount + parseInt(value.wCount);
		troudleshootingTimeAverageCount = troudleshootingTimeAverageCount + parseFloat(value.troudleshootingTimeCountA);
		processTimeRateCount = processTimeRateCount + parseFloat(value.processTimeRateCount);
		ProcessTimeCount = ProcessTimeCount + parseFloat(value.ProcessTimeCount);
		size++;
	});
		//alert(wCount);
		//alert(processTimeRateCount);
		if( ProcessTimeCount != 0){
			//alert(ProcessTimeCount + "==" + wCount);
			ProcessTimeCount = ProcessTimeCount/wCount*100;
		}else{
			ProcessTimeCount = 0;
		}
		ProcessTimeCount = ProcessTimeCount +"";
		if(ProcessTimeCount.indexOf(".") >= 0){
			ProcessTimeCount = ProcessTimeCount.substring(0,ProcessTimeCount.indexOf(".") + 3);
		}
		//alert(processTimeRateCount);
		//alert("processTimeRateCount==="+processTimeRateCount);
		//troudleshootingTimeAverageCount = troudleshootingTimeAverageCount/size;
		//alert("troudleshootingTimeAverageCount=="+troudleshootingTimeAverageCount+"=="+wCount);
		if( troudleshootingTimeAverageCount != 0){
			troudleshootingTimeAverageCount = troudleshootingTimeAverageCount/wCount;
		}else{
			troudleshootingTimeAverageCount = 0;
		}
		var parseFloatTime =  parseFloat(troudleshootingTimeAverageCount)/60+"";
		if(parseFloatTime.indexOf(".") >= 0){
			parseFloatTime = parseFloatTime.substring(0,parseFloatTime.indexOf(".") + 3);
		}
		rData = "{'wCount':"+wCount+",'parseFloatTime':"+parseFloatTime+",'processTimeRateCount':"+ProcessTimeCount+",'orgId':"+orgId+",'orgName':'"+orgName+"','TimeString':'"+TimeString+"'},";
		thisData = thisData + rData;
	}
	});
}

function getYMTime(year,month){
	year = parseInt(year);
	month = parseInt(month);
	var dates= new Array(); //定义一数组;
	dates = getYearMonth(year, month);
	var spanMonth = "";
	if(dates[1] < 10){
		spanMonth = "0"+dates[1];
	}else{
		spanMonth = dates[1];
	}
	var lastMonthDates= new Array(); //定义一数组;
	//根据年月获取月第一个天与最后一天
	lastMonthDates = getFirstAndLastMonthDay(dates[0], dates[1]);
	lastMonthDates[2] = dates[0]+"年"+spanMonth+"月";
	return lastMonthDates;
	}
	
	
function clickGetUrgentRepairPeriod(){
	thisData = "";
	$("#statements_classify").hide();
	//判断按组织TAB是否为显示
	if($(".ontab").text() == "按组织"){
		getUPeriod();
	}else if($(".ontab").text() == "按项目"){
		getUPeriodProject();
	}
}

function clickGetUrgentRepairRing(){
	thisData = "";
	$("#statements_classify").hide();
	//判断按组织TAB是否为显示
	if($(".ontab").text() == "按组织"){
		getURing();	
	}else if($(".ontab").text() == "按项目"){
		getURingProject();
	}
}
	
function getUPeriod(){
		thisData = "";
		var orgId = $("#orgId").val();
		var orgName = $("#orgName").val();
		var year = $("#hiddenYear").val();
		var month = $("#hiddenMonth").val();
		year = parseInt(year);
		month = parseInt(month);
		var beginTime = $("#hiddenbeginTime").val();
		for(var i = 5;i >= 0;i--){
			var yeart = year - i;
			var al = getYMTime(yeart,month);
			getUrgentRepairPeriod(orgId,orgName,al[0],al[1],al[2]);
		}
		thisData = eval("["+thisData+"]");
		getReportComment("urgentRepairPeriod", "org", orgId, beginTime);
		var series = "";
		var seriesC = "";
		var size = 0;
		var categories= new Array(); //定义一数组;	
		var processTimeRateCount = "";
		var parseFloatTime = "{ type: 'line',name: '工单处理历时 (小时)',color: '#FF6600', data: [";
		var processTimeRateCount = "{ type: 'line',name: '故障处理及时率 (%)' ,color: '#336600', data: [";
		var wCount = "{name: '工单数', type:'column', yAxis: 1,  data: [";
		$.each(thisData, function(key, value){
			categories[size] = value.TimeString;
			parseFloatTime = parseFloatTime + value.parseFloatTime +  ",";
			processTimeRateCount = processTimeRateCount + value.processTimeRateCount +  ",";
			wCount = wCount + parseInt(value.wCount) +  ",";
			size++;
		});
		parseFloatTime = parseFloatTime + "]},";
		processTimeRateCount = processTimeRateCount + "]},";
		wCount = wCount + "]},";
		var orgName = $("#orgName").val();
		var title1 = "抢修工单("+orgName+",同比分析)";
		var title2 = "历时/及时率统计("+orgName+",同比分析)";
		series = "[" + wCount +  processTimeRateCount + "]";
		series = eval(series);
		seriesC = "[" + wCount + parseFloatTime +"]";
		seriesC = eval(seriesC);
		thisDataP.thistitle = title2;
		thisDataP.thissubtitle = "";
		thisDataP.thisyAxisTitle2 = "工单数(张)";
		thisDataP.thisyAxisTitle = "工单处理历时 (小时)";
		thisDataP.thiscategories = categories;
		thisDataP.thisseries = seriesC;
		thisDataB.thistitle = title2;
		thisDataB.thissubtitle = "";
		thisDataB.thisyAxisTitle2 = "工单数(张)";
		thisDataB.thisyAxisTitle = "故障处理及时率 (%)";
		thisDataB.thiscategories = categories;
		thisDataB.thisseries = series;
		columnAndSplineBasicU(title2,"",'故障处理及时率 (%)',"工单数(张)","columnBasicDiv",categories,series);
		columnAndSplineBasicUR(title2,"",'工单处理历时 (小时)','工单数(张)',"pieBasicDiv",categories,seriesC);
		//columnAndSplineBasicUR(title2,"",'故障处理及时率 (%)',"工单数(张)","columnBasicDiv",categories,series);
		//columnAndSplineBasicU(title2,"",'工单处理历时 (小时)','工单数(张)',"pieBasicDiv",categories,series);
		//columnBasic(title1,"","工单数(张)","pieBasicDiv",categories,seriesC);
}


function getURing(){
		thisData = "";
		var orgId = $("#orgId").val();
		var orgName = $("#orgName").val();
		var year = $("#hiddenYear").val();
		var month = $("#hiddenMonth").val();
		year = parseInt(year);
		month = parseInt(month);
		var beginTime = $("#hiddenbeginTime").val();
		var arr = new Array();
		arr[5] = year+","+month;
		for(var i = 4;i >= 0;i--){
			var montht = month - 1;
			if( month == 1){
				montht = month;
				month = 12;
				year = year - 1;
			}else{
				month--;
			}
			arr[i] = year+","+month;
		}
		if(arr != null){
			for(var i = 0;i < 6;i++){
				var de = arr[i].split(",");
				var al = getYMTime(de[0],de[1]);
				getUrgentRepairPeriod(orgId,orgName,al[0],al[1],al[2]);
			};
		}
		thisData = eval("["+thisData+"]");
		getReportComment("urgentRepairRing", "org", orgId, beginTime);
		var series = "";
		var seriesC = "";
		var size = 0;
		var categories= new Array(); //定义一数组;	
		var processTimeRateCount = "";
		var parseFloatTime = "{ type: 'line',name: '工单处理历时 (小时)',color: '#FF6600', data: [";
		var processTimeRateCount = "{ type: 'line',name: '故障处理及时率 (%)' ,color: '#336600', data: [";
		var wCount = "{name: '工单数', type:'column', yAxis: 1,  data: [";
		$.each(thisData, function(key, value){
			categories[size] = value.TimeString;
			parseFloatTime = parseFloatTime + value.parseFloatTime +  ",";
			processTimeRateCount = processTimeRateCount + value.processTimeRateCount +  ",";
			wCount = wCount + parseInt(value.wCount) +  ",";
			size++;
		});
		parseFloatTime = parseFloatTime + "]},";
		processTimeRateCount = processTimeRateCount + "]},";
		wCount = wCount + "]},";
		var orgName = $("#orgName").val();
		var title1 = "抢修工单("+orgName+",环比分析)";
		var title2 = "历时/及时率统计("+orgName+",环比分析)";
		series = "[" + wCount +  processTimeRateCount + "]";
		series = eval(series);
		seriesC = "[" + wCount + parseFloatTime +"]";
		seriesC = eval(seriesC);
		thisDataP.thistitle = title2;
		thisDataP.thissubtitle = "";
		thisDataP.thisyAxisTitle2 = "工单数(张)";
		thisDataP.thisyAxisTitle = "工单处理历时 (小时)";
		thisDataP.thiscategories = categories;
		thisDataP.thisseries = seriesC;
		thisDataB.thistitle = title2;
		thisDataB.thissubtitle = "";
		thisDataB.thisyAxisTitle2 = "工单数(张)";
		thisDataB.thisyAxisTitle = "故障处理及时率 (%)";
		thisDataB.thiscategories = categories;
		thisDataB.thisseries = series;
		columnAndSplineBasicU(title2,"",'故障处理及时率 (%)',"工单数(张)","columnBasicDiv",categories,series);
		columnAndSplineBasicUR(title2,"",'工单处理历时 (小时)','工单数(张)',"pieBasicDiv",categories,seriesC);
		//columnAndSplineBasicL(title2,"",'故障处理及时率 (%)','工单处理历时 (小时)',"columnBasicDiv",categories,series);
		//columnBasic(title1,"","工单数(张)","pieBasicDiv",categories,seriesC);
	
}



var columnAndSplineBasicChart1;	
		//柱状图+折线(对比)
		function columnAndSplineBasicL(title,subtitle,yAxisTitle,yAxisTitle2,divId,categories,series){
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
                    	color: '#FF6600'
                    }
                },
                labels: {
                    formatter: function() {
                        return this.value;
                    },
                    style: {
                    	color: '#FF6600'
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


function clickShowMoreTable(orgId,orgName){
	orgName = orgName.replace("(","");
	orgName = orgName.replace(")","");
	orgName = orgName.replace("(","");
	orgName = orgName.replace(")","");
   var reportType = $("#hidereportType").val();
   if(reportType == "UrgentRepair"){
		if($(".ontab").text() == "按组织"){
			if($("#typecheckbox").attr("checked") == "checked"){
				var rowName = "";
			//if($(".ontab").text() == "按基站类型"){
				//获取故障抢修工单数量
			//	rowName = "BaseStationLevel";
			//}if($(".ontab").text() == "按故障类型"){
				//获取故障抢修工单数量
			//	rowName = "FaultType";
			//}if($(".ontab").text() == "按告警级别"){
				//获取故障抢修工单数量
			//	rowName = "FaultLevel";
			//}if($(".ontab").text() == "按受理专业"){
				//获取故障抢修工单数量
			//	rowName = "AcceptProfessional";
			//}if($(".ontab").text() == "按故障大类"){
				//获取故障抢修工单数量
			//	rowName = "FaultGenera";
			//}
			rowName = $(".uradio:checked").val();
				var rowValue = orgName;
	   			var beginTime = $("#hiddenbeginTime").val();
				var endTime = $("#hiddenendTime").val();
				var reportType = "故障抢修工单明细报表";
				var DateTimeString = $("#DateTimeSpan").text();
				var url = "getUrgentRepairworkorderManageAction";
				var orgName = $(".ontab").text()+":"+orgName;
	   			var orgId = $("#orgIdText").val();
	   			if(rowValue == '空值'){
	   				rowValue  = '';
	   			}
	   			//alert(orgId);
				var url = "getUrgentRepairWorkorderManageAction?statisticsType=biz&rowName="+rowName+"&judge=等于&rowValue="+rowValue+"&orgId="+orgId+"&beginTime="+beginTime+"&endTime="+endTime+"&orgName="+orgName+"&reportType="+reportType+"&DateTimeString="+DateTimeString;
				window.open(url);
			//params = {statisticsType:"",bizunitinnstId:bizunitId,rowName:rowName,rowValue:rowValue,beginTime:beginTime,endTime:endTime,bizunitinnstName:bizunitinnstName,reportType:reportType,DateTimeString:DateTimeString};
			//$.post(url, params, function(data){
				//$("#statements_right").html(data);
			//});
			}else{
				
	   			var beginTime = $("#hiddenbeginTime").val();
				var endTime = $("#hiddenendTime").val();
				var reportType = "故障抢修工单明细报表";
				var DateTimeString = $("#DateTimeSpan").text();
			//	var url = "getUrgentRepairWorkorderManageAction";
			//var params = "";
				var url = "getUrgentRepairWorkorderManageAction?statisticsType=biz&orgId="+orgId+"&beginTime="+beginTime+"&endTime="+endTime+"&orgName="+orgName+"&reportType="+reportType+"&DateTimeString="+DateTimeString;
				window.open(url);
			//params = {statisticsType:"biz",bizunitinnstId:bizunitinnstId,beginTime:beginTime,endTime:endTime,bizunitinnstName:bizunitinnstName,reportType:reportType,DateTimeString:DateTimeString};
			//$.post(url, params, function(data){
				//$("#statements_right").html(data);
			//});
			}
			
			
		}else{
			var beginTime = $("#hiddenbeginTime").val();
				var endTime = $("#hiddenendTime").val();
				var reportType = "故障抢修工单明细报表";
				var DateTimeString = $("#DateTimeSpan").text();
			//	var url = "getUrgentRepairWorkorderManageAction";
			//var params = "";
				var url = "getUrgentRepairWorkorderManageAction?statisticsType=pro&orgId="+orgId+"&beginTime="+beginTime+"&endTime="+endTime+"&orgName="+orgName+"&reportType="+reportType+"&DateTimeString="+DateTimeString;
				window.open(url);
		}
   }
   }

function clickReturnReport(){
	var returnOrgId = $("#returnOrgId").val();
	var thisOrgId = $("#thisOrgId").val();
	if(thisOrgId == returnOrgId){
		$("#statements_back").hide();
		return;
	}
	//开始时间
	var beginTime = $("#hiddenbeginTime").val();
	//结束时间
	var endTime = $("#hiddenendTime").val();
	if($(".selected").text().replace(" ","") == "对比"){
		if($("#typecheckbox").attr("checked") == "checked"){
		var type = $(".uradio:checked").val();
		var text = $(".uradio:checked").attr("title");
		//$("#uradiodiv").show();
		//clickGetUrgentRepairworkorderStatisticsGROUPBY(type,text);	
		getUrgentRepairworkorderStatisticsByDateGROUPBYAndTopOrg(returnOrgId,beginTime,endTime,type,text);

	}else{
		//$("#uradiodiv").hide();
		getUrgentRepairworkorderStatisticsByDateAndTopOrg(returnOrgId,beginTime,endTime);
	}
	}else if($(".selected").text().replace(" ","") == "同比"){
		getUPeriodTopOrg(returnOrgId);
	}else if($(".selected").text().replace(" ","") == "环比"){
		getURingTopOrg(returnOrgId);
	}
	
	
}





//按组织获取故障抢修工单数量(向上)
function getUrgentRepairworkorderStatisticsByDateAndTopOrg(orgId,beginTime,endTime){

//访问地址
var url = "getUrgentRepairProcessTimeRateByBizunitinnstTopOrgAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
	var pOrgId = 0;
	var pOrgName = "";
	$.post(url, params, function(data){

		data = eval(data);
		//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr>"
								+"<th style='width:55px;'>项目</th>"
								+"<th>工单数</th>"
								+"<th>工单处理历时 (小时)</th>"
								+"<th>故障处理及时率</th>"
							+"</tr>";
							
		var categories= new Array(); //定义一数组;	
		//图形报表数据源
		var series = "";
		var workorderhtml = "{ name: '工单数', data: [";
		var processTimehtml = "{ name: '超时工单数', data: [";
		var workorderCount = 0;
		var processTimeCount = 0;
		var i = 0;		
		var selectDivCountext = "";
		$.each(data, function(key, value){
			pOrgId = value.pOrgId;
			pOrgName = value.pOrgName;
			workorderhtml = workorderhtml + value.wCount + ",";
			processTimehtml = processTimehtml + value.wCount + ",";
			var orgNameC = value.orgName.replace("(","");
			orgNameC = orgNameC.replace(")","");
			orgNameC = orgNameC.replace("(","");
			orgNameC = orgNameC.replace(")","");
			counttext = counttext + "<tr>"
								+"<td>"+value.orgName+"</td>"
								+"<td><a href='#' onclick='clickShowMoreTable("+value.orgId+",\""+value.orgName+"\")'>"+value.wCount+"</a></td>"
								+"<td id='"+orgNameC+"ProcessTime'></td>"
								+"<td id='"+orgNameC+"TroudleshootingTime'></td>"
							"</tr>";
							var planTitle = "";
					if(value.orgName.length > 8 && value.orgName != null){
						var parseIndex = Math.ceil(value.orgName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.orgName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.orgName;
					}
					categories[i]=planTitle;
							i++;
		    workorderCount = workorderCount + value.wCount;
		    selectDivCountext = selectDivCountext+"<div id='"+orgNameC+"'>"+value.orgId+"</div>";
		});
		$("#selectDivHidden").html(selectDivCountext);
		workorderhtml = workorderhtml + "]},";
		processTimehtml = processTimehtml + "]},";
		series = "[" +workorderhtml + processTimehtml + "]";
		counttext = counttext + "<tr>"
								+"<td>汇总/平均</td>"
								+"<td>"+workorderCount+"</td>"
								+"<td id='ProcessTime'></td>"
								+"<td id='TroudleshootingTime'></td>"
							"</tr>";
		counttext = counttext + "</table>";
		var uCount = workorderCount + processTimeCount;
		if(!uCount){
			uCount = 0;
		}else{
			uCount = uCount + "";
			if(uCount.indexOf(".") > 0){
				uCount = uCount.substring(0,uCount.indexOf(".") + 2);
			}
		}
		$("#uCount").text(workorderCount);
		var upCount = (workorderCount)/i;
		if(!uCount){
			upCount = 0;
		}else{
			upCount = upCount + "";
			if(upCount.indexOf(".") > 0){
				upCount = upCount.substring(0,upCount.indexOf(".") + 2);
			}
		}
		$("#upCount").text(upCount);
		$("#stationTableDiv").html(counttext);
		getReportComment("urgentRepair", "organization", pOrgId, beginTime);
		$("#orgName").val(pOrgName);
		$("#orgId").val(pOrgId);
		$("#returnOrgId").val(pOrgId);
		getUrgentRepairworkorderProcessTimeRateByDate(data,orgId,beginTime,endTime);
		var returnOrgId = $("#returnOrgId").val();
		var thisOrgId = $("#thisOrgId").val();
		if(thisOrgId == returnOrgId){
				$("#statements_back").hide();
		}
	});
}


//按组织获取故障抢修工单数量(按类型)(向上)
function getUrgentRepairworkorderStatisticsByDateGROUPBYAndTopOrg(orgId,beginTime,endTime,rowName,rowString){

//访问地址
var url = "getUrgentRepairProcessTimeRateBy"+rowName+"AndTopOrgAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
	var pOrgId = 0;
	var pOrgName = "";
	$.post(url, params, function(data){
		data = eval(data);
		//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr>"
								+"<th style='width:55px;'>"+rowString+"</th>"
								+"<th>工单数</th>"
								+"<th>工单处理历时 (小时)</th>"
								+"<th>故障处理及时率</th>"
							+"</tr>";
							
		var categories= new Array(); //定义一数组;	
		//图形报表数据源
		var series = "";
		var workorderhtml = "{ name: '工单数', data: [";
		var processTimehtml = "{ name: '超时工单数', data: [";
		var workorderCount = 0;
		var processTimeCount = 0;
		var i = 0;		
		var selectDivCountext = "";
		$.each(data, function(key, value){
			pOrgId = value.pOrgId;
			pOrgName = value.pOrgName;
			workorderhtml = workorderhtml + value.wCount + ",";
			processTimehtml = processTimehtml + value.ProcessTimeCount + ",";
			counttext = counttext + "<tr>"
								+"<td>"+value.statisticsType+"</td>"
								+"<td><a href='#' onclick='clickShowMoreTable("+value.orgId+",\""+value.statisticsType+"\")'>"+value.wCount+"</a></td>"
								+"<td id='"+value.statisticsType+"ProcessTime'></td>"
								+"<td id='"+value.statisticsType+"TroudleshootingTime'></td>"
							"</tr>";
							var planTitle = "";
							if(value.statisticsType.length > 8 && value.statisticsType != null){
								var parseIndex = Math.ceil(value.statisticsType.length/8);
								if(parseIndex != null && parseIndex > 0){
									for(var j = 0;j < parseIndex;j++){
										var sIndex = j*8;
										var dIndex = (j+1)*8;
										planTitle = planTitle + value.statisticsType.substring(sIndex,dIndex) + "<br/>";
										//alert(planTitle);
									}
								}
							}else{
								planTitle = value.statisticsType;
							}
							categories[i]=planTitle;
							i++;
		    workorderCount = workorderCount + value.wCount;
		    processTimeCount = processTimeCount + value.ProcessTimeCount;
		    selectDivCountext = selectDivCountext+"<div id='"+value.statisticsType+"'>"+value.orgId+"</div>";
		});
		$("#selectDivHidden").html(selectDivCountext);
		workorderhtml = workorderhtml + "]},";
		processTimehtml = processTimehtml + "]},";
		series = "[" +workorderhtml + processTimehtml + "]";
		counttext = counttext + "<tr>"
								+"<td>汇总</td>"
								+"<td>"+workorderCount+"</td>"
								+"<td id='ProcessTime'></td>"
								+"<td id='TroudleshootingTime'></td>"
							"</tr>";
		counttext = counttext + "</table>";
		var uCount = workorderCount + processTimeCount;
		if(!uCount){
			uCount = 0;
		}else{
			uCount = uCount + "";
			if(uCount.indexOf(".") > 0){
				uCount = uCount.substring(0,uCount.indexOf(".") + 2);
			}
		}
		$("#uCount").text(workorderCount);
		var upCount = (workorderCount)/i;
		if(upCount){
		
			upCount = upCount + "";
			if(upCount.indexOf(".") > 0){
				upCount = upCount.substring(0,upCount.indexOf(".") + 2);
			}
		}
		$("#upCount").text(upCount);
		$("#stationTableDiv").html(counttext);
		getReportComment("urgentRepair", rowName, pOrgId, beginTime);
		$("#orgName").val(pOrgName);
		$("#orgId").val(pOrgId);
		$("#returnOrgId").val(pOrgId);
		getUrgentRepairworkorderProcessTimeRateByDateGROUPBY(data,orgId,beginTime,endTime,rowName,rowString);
		var returnOrgId = $("#returnOrgId").val();
		var thisOrgId = $("#thisOrgId").val();
		if(thisOrgId == returnOrgId){
				$("#statements_back").hide();
		}
	});
}



function getUPeriodTopOrg(returnOrgId){
		thisData = "";
		var orgId = returnOrgId;
		var orgName = $("#orgName").val();
		var year = $("#hiddenYear").val();
		var month = $("#hiddenMonth").val();
		var pOrgId = 0;
		var pOrgName = "";
		year = parseInt(year);
		month = parseInt(month);
		var beginTime = $("#hiddenbeginTime").val();
		for(var i = 5;i >= 0;i--){
			var yeart = year - i;
			var al = getYMTime(yeart,month);
			getUrgentRepairPeriodTopOrg(orgId,orgName,al[0],al[1],al[2]);
		}
		thisData = eval("["+thisData+"]");
		getReportComment("urgentRepairPeriod", "org", orgId, beginTime);
		var series = "";
		var seriesC = "";
		var size = 0;
		var categories= new Array(); //定义一数组;	
		var processTimeRateCount = "";
		var parseFloatTime = "{ type: 'line',name: '工单处理历时 (小时)',color: '#FF6600', data: [";
		var processTimeRateCount = "{ type: 'line',name: '故障处理及时率 (%)' ,color: '#336600', data: [";
		var wCount = "{name: '工单数', type:'column', yAxis: 1,  data: [";
		$.each(thisData, function(key, value){
			pOrgId = value.pOrgId;
		pOrgName = value.pOrgName;
			categories[size] = value.TimeString;
			parseFloatTime = parseFloatTime + value.parseFloatTime +  ",";
			processTimeRateCount = processTimeRateCount + value.processTimeRateCount +  ",";
			wCount = wCount + parseInt(value.wCount) +  ",";
			size++;
		});
		parseFloatTime = parseFloatTime + "]},";
		processTimeRateCount = processTimeRateCount + "]},";
		wCount = wCount + "]},";
		var orgName = $("#orgName").val();
		var title1 = "抢修工单("+pOrgName+",同比分析)";
		var title2 = "历时/及时率统计("+pOrgName+",同比分析)";
		series = "[" + wCount +  processTimeRateCount + "]";
		series = eval(series);
		seriesC = "[" + wCount + parseFloatTime +"]";
		seriesC = eval(seriesC);
		$("#orgName").val(pOrgName);
		$("#orgId").val(pOrgId);
		$("#returnOrgId").val(pOrgId);
		var returnOrgId = $("#returnOrgId").val();
		var thisOrgId = $("#thisOrgId").val();
		if(thisOrgId == returnOrgId){
				$("#statements_back").hide();
		}
		thisDataP.thistitle = title2;
		thisDataP.thissubtitle = "";
		thisDataP.thisyAxisTitle2 = "工单数(张)";
		thisDataP.thisyAxisTitle = "工单处理历时 (小时)";
		thisDataP.thiscategories = categories;
		thisDataP.thisseries = seriesC;
		thisDataB.thistitle = title2;
		thisDataB.thissubtitle = "";
		thisDataB.thisyAxisTitle2 = "工单数(张)";
		thisDataB.thisyAxisTitle = "故障处理及时率 (%)";
		thisDataB.thiscategories = categories;
		thisDataB.thisseries = series;
		columnAndSplineBasicU(title2,"",'故障处理及时率 (%)',"工单数(张)","columnBasicDiv",categories,series);
		columnAndSplineBasicUR(title2,"",'工单处理历时 (小时)','工单数(张)',"pieBasicDiv",categories,seriesC);
		//columnAndSplineBasicUR(title2,"",'故障处理及时率 (%)',"工单数(张)","columnBasicDiv",categories,series);
		//columnAndSplineBasicU(title2,"",'工单处理历时 (小时)','工单数(张)',"pieBasicDiv",categories,series);
		//columnBasic(title1,"","工单数(张)","pieBasicDiv",categories,seriesC);
}


function getURingTopOrg(returnOrgId){
		thisData = "";
		var orgId = returnOrgId;
		var orgName = $("#orgName").val();
		var year = $("#hiddenYear").val();
		var month = $("#hiddenMonth").val();
			var pOrgId = 0;
		var pOrgName = "";
		year = parseInt(year);
		month = parseInt(month);
		var beginTime = $("#hiddenbeginTime").val();
		var arr = new Array();
		arr[5] = year+","+month;
		for(var i = 4;i >= 0;i--){
			var montht = month - 1;
			if( month == 1){
				montht = month;
				month = 12;
				year = year - 1;
			}else{
				month--;
			}
			arr[i] = year+","+month;
		}
		if(arr != null){
			for(var i = 0;i < 6;i++){
				var de = arr[i].split(",");
				var al = getYMTime(de[0],de[1]);
				getUrgentRepairPeriodTopOrg(orgId,orgName,al[0],al[1],al[2]);
			};
		}
		thisData = eval("["+thisData+"]");
		getReportComment("urgentRepairRing", "org", orgId, beginTime);
		var series = "";
		var seriesC = "";
		var size = 0;
		var categories= new Array(); //定义一数组;	
		var processTimeRateCount = "";
		var parseFloatTime = "{ type: 'line',name: '工单处理历时 (小时)',color: '#FF6600', data: [";
		var processTimeRateCount = "{ type: 'line',name: '故障处理及时率 (%)' ,color: '#336600', data: [";
		var wCount = "{name: '工单数', type:'column', yAxis: 1,  data: [";
		$.each(thisData, function(key, value){
			pOrgId = value.pOrgId;
		pOrgName = value.pOrgName;
			categories[size] = value.TimeString;
			parseFloatTime = parseFloatTime + value.parseFloatTime +  ",";
			processTimeRateCount = processTimeRateCount + value.processTimeRateCount +  ",";
			wCount = wCount + parseInt(value.wCount) +  ",";
			size++;
		});
		parseFloatTime = parseFloatTime + "]},";
		processTimeRateCount = processTimeRateCount + "]},";
		wCount = wCount + "]},";
		var orgName = $("#orgName").val();
		var title1 = "抢修工单("+pOrgName+",环比分析)";
		var title2 = "历时/及时率统计("+pOrgName+",环比分析)";
		series = "[" + wCount +  processTimeRateCount + "]";
		series = eval(series);
		seriesC = "[" + wCount + parseFloatTime +"]";
		seriesC = eval(seriesC);
		$("#orgName").val(pOrgName);
		$("#orgId").val(pOrgId);
		$("#returnOrgId").val(pOrgId);
		var returnOrgId = $("#returnOrgId").val();
		var thisOrgId = $("#thisOrgId").val();
		if(thisOrgId == returnOrgId){
				$("#statements_back").hide();
		}
		thisDataP.thistitle = title2;
		thisDataP.thissubtitle = "";
		thisDataP.thisyAxisTitle2 = "工单数(张)";
		thisDataP.thisyAxisTitle = "工单处理历时 (小时)";
		thisDataP.thiscategories = categories;
		thisDataP.thisseries = seriesC;
		thisDataB.thistitle = title2;
		thisDataB.thissubtitle = "";
		thisDataB.thisyAxisTitle2 = "工单数(张)";
		thisDataB.thisyAxisTitle = "故障处理及时率 (%)";
		thisDataB.thiscategories = categories;
		thisDataB.thisseries = series;
		columnAndSplineBasicU(title2,"",'故障处理及时率 (%)',"工单数(张)","columnBasicDiv",categories,series);
		columnAndSplineBasicUR(title2,"",'工单处理历时 (小时)','工单数(张)',"pieBasicDiv",categories,seriesC);
		//columnAndSplineBasicL(title2,"",'故障处理及时率 (%)','工单处理历时 (小时)',"columnBasicDiv",categories,series);
		//columnBasic(title1,"","工单数(张)","pieBasicDiv",categories,seriesC);
	
}



var thisData = "";

function getUrgentRepairPeriodTopOrg(orgId,orgName,beginTime,endTime,TimeString){
	var url = "getUrgentRepairProcessTimeRateByBizunitinnstTopOrgAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
	 $.ajax({
          type: "POST",                                         //ajax的方式为post(get方式对传送数据长度有限制)
          url: url,           //一般处理程序页面AddUser.ashx(在2中会写出该页面内容)
		  cache : false, 
          async : false,
          dataType: "json",     
	 data: params,       //要传送的数据键值对adduserName为键（方便2中的文件用此名称接受数据）txtuserName为值（要传递的变量，例如用户名）
     success: function (data) {
	var troudleshootingTimeAverageCount = 0;
	var processTimeRateCount = 0;
	var wCount = 0;
	var size = 0;
	var rData = "";
	var ProcessTimeCount = 0;
	var pOrgId = 0;
	var pOrgName = "";
	$.each(data, function(key, value){
		pOrgId = value.pOrgId;
		pOrgName = value.pOrgName;
		wCount = wCount + parseInt(value.wCount);
		troudleshootingTimeAverageCount = troudleshootingTimeAverageCount + parseFloat(value.troudleshootingTimeCountA);
		processTimeRateCount = processTimeRateCount + parseFloat(value.processTimeRateCount);
		ProcessTimeCount = ProcessTimeCount + parseFloat(value.ProcessTimeCount);
		size++;
	});
		//alert(wCount);
		//alert(processTimeRateCount);
		if( ProcessTimeCount != 0){
			//alert(ProcessTimeCount + "==" + wCount);
			ProcessTimeCount = ProcessTimeCount/wCount*100;
		}else{
			ProcessTimeCount = 0;
		}
		ProcessTimeCount = ProcessTimeCount +"";
		if(ProcessTimeCount.indexOf(".") >= 0){
			ProcessTimeCount = ProcessTimeCount.substring(0,ProcessTimeCount.indexOf(".") + 3);
		}
		//alert(processTimeRateCount);
		//alert("processTimeRateCount==="+processTimeRateCount);
		//troudleshootingTimeAverageCount = troudleshootingTimeAverageCount/size;
		//alert("troudleshootingTimeAverageCount=="+troudleshootingTimeAverageCount+"=="+wCount);
		if( troudleshootingTimeAverageCount != 0){
			troudleshootingTimeAverageCount = troudleshootingTimeAverageCount/wCount;
		}else{
			troudleshootingTimeAverageCount = 0;
		}
		var parseFloatTime =  parseFloat(troudleshootingTimeAverageCount)/60+"";
		if(parseFloatTime.indexOf(".") >= 0){
			parseFloatTime = parseFloatTime.substring(0,parseFloatTime.indexOf(".") + 3);
		}
		rData = "{'wCount':"+wCount+",'parseFloatTime':"+parseFloatTime+",'processTimeRateCount':"+ProcessTimeCount+",'orgId':"+orgId+",'orgName':'"+orgName+"','TimeString':'"+TimeString+"','pOrgId':"+pOrgId+",'pOrgName':'"+pOrgName+"'},";
		thisData = thisData + rData;
	}
	});
}


//点击确定时获取故障抢修工单数量
function clickGetUrgentRepairworkorderStatisticsProject(){
	//组织ID
	var orgId = $("#thisOrgId").val();
	//开始时间
	var beginTime = $("#hiddenbeginTime").val();
	//结束时间
	var endTime = $("#hiddenendTime").val();
	//按组织获取故障抢修工单数量
	getUrgentRepairworkorderStatisticsByDateProject(orgId,beginTime,endTime);
}




//按组织获取故障抢修工单数量
function getUrgentRepairworkorderStatisticsByDateProject(orgId,beginTime,endTime){
getReportComment("urgentRepairProject", "organization", orgId, beginTime);
//访问地址
var url = "getUrgentRepairProcessTimeRateByProjectAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
	$.post(url, params, function(data){
		//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr>"
								+"<th style='width:55px;'>项目</th>"
								+"<th>工单数</th>"
								+"<th>工单处理历时 (小时)</th>"
								+"<th>故障处理及时率</th>"
							+"</tr>";
							
		var categories= new Array(); //定义一数组;	
		//图形报表数据源
		var series = "";
		var workorderhtml = "{ name: '工单数', data: [";
		var processTimehtml = "{ name: '超时工单数', data: [";
		var workorderCount = 0;
		var processTimeCount = 0;
		var i = 0;		
		var selectDivCountext = "";
		$.each(data, function(key, value){
			workorderhtml = workorderhtml + value.wCount + ",";
			processTimehtml = processTimehtml + value.wCount + ",";
			var orgNameC = value.orgName.replace("(","");
			orgNameC = orgNameC.replace(")","");
			orgNameC = orgNameC.replace("(","");
			orgNameC = orgNameC.replace(")","");
			counttext = counttext + "<tr>"
								+"<td>"+value.orgName+"</td>"
								+"<td><a href='#' onclick='clickShowMoreTable("+value.orgId+",\""+value.orgNameC+"\")'>"+value.wCount+"</a></td>"
								+"<td id='"+orgNameC+"ProcessTime'></td>"
								+"<td id='"+orgNameC+"TroudleshootingTime'></td>"
							"</tr>";
							var planTitle = "";
					if(value.orgName.length > 8 && value.orgName != null){
						var parseIndex = Math.ceil(value.orgName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.orgName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.orgName;
					}
					categories[i]=planTitle;
							i++;
		    workorderCount = workorderCount + value.wCount;
		    selectDivCountext = selectDivCountext+"<div id='"+orgNameC+"'>"+value.orgId+"</div>";
		});
		$("#selectDivHidden").html(selectDivCountext);
		workorderhtml = workorderhtml + "]},";
		processTimehtml = processTimehtml + "]},";
		series = "[" +workorderhtml + processTimehtml + "]";
		counttext = counttext + "<tr>"
								+"<td>汇总/平均</td>"
								+"<td>"+workorderCount+"</td>"
								+"<td id='ProcessTime'></td>"
								+"<td id='TroudleshootingTime'></td>"
							"</tr>";
		counttext = counttext + "</table>";
		var uCount = workorderCount + processTimeCount;
		if(!uCount){
			uCount = 0;
		}else{
			uCount = uCount + "";
			if(uCount.indexOf(".") > 0){
				uCount = uCount.substring(0,uCount.indexOf(".") + 2);
			}
		}
		$("#uCount").text(workorderCount);
		var upCount = (workorderCount)/i;
		if(!uCount){
			upCount = 0;
		}else{
			upCount = upCount + "";
			if(upCount.indexOf(".") > 0){
				upCount = upCount.substring(0,upCount.indexOf(".") + 2);
			}
		}
		$("#upCount").text(upCount);
		$("#stationTableDiv").html(counttext);
		getUrgentRepairworkorderProcessTimeRateByDateProject(data,orgId,beginTime,endTime);
	},"json");
}



//按组织获取故障抢修处理及时率
function getUrgentRepairworkorderProcessTimeRateByDateProject(data,orgId,beginTime,endTime){
//访问地址
var url = "getUrgentRepairProcessTimeRateByProjectAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
		//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr class='main_table_title'>"
								+"<td style='width:35px;'></td>"
								+"<td colspan='4'>故障处理及时率</td>"
							+"</tr>"
							+"<tr>"
								+"<th style='width:55px;'>项目</th>"
								+"<th>故障处理及时率</th>"
								+"<th>故障处理及时率时(小时)</th>"
							+"</tr>";
							
		var categories= new Array(); //定义一数组;	
		//图形报表数据源
		var series = "";
		var wCounthtml = "{ type:'column', name: '工单数', yAxis: 1, data: [";
		var processTimeRatehtml = "{ type: 'line' ,name: '工单处理历时 (小时)',color: '#FF6600', data: [";
		var troudleshootingTimeAverageCount = 0;
		var processTimeRateCount = 0;
		var i = 0;		
		var selectDivCountext = "";
		var size = 0;
		$.each(data, function(key, value){
			size++;
			wCounthtml = wCounthtml + value.wCount + ",";
			var parseTime =  parseFloat(value.troudleshootingTimeAverage/60)+"";
			if(parseTime.indexOf(".") >= 0){
				parseTime = parseTime.substring(0,parseTime.indexOf(".") + 3);
			}
			processTimeRatehtml = processTimeRatehtml + parseTime + ",";
			var orgNameC = value.orgName.replace("(","");
			orgNameC = orgNameC.replace(")","");
			orgNameC = orgNameC.replace("(","");
			orgNameC = orgNameC.replace(")","");
			$("#"+orgNameC+"TroudleshootingTime").text(value.processTimeRateCount);
			counttext = counttext + "<tr>"
								+"<td>"+value.orgName+"</td>"
								+"<td>"+value.processTimeRateCount+"%</td>"
								+"<td>"+value.wCount+"</td>"
							"</tr>";
							var planTitle = "";
					if(value.orgName.length > 8 && value.orgName != null){
						var parseIndex = Math.ceil(value.orgName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.orgName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.orgName;
					}
					categories[i]=planTitle;
							i++;
		    troudleshootingTimeAverageCount = troudleshootingTimeAverageCount + parseFloat(value.troudleshootingTimeAverage);
		    processTimeRateCount = processTimeRateCount + parseFloat(value.processTimeRateCount);
		     selectDivCountext = selectDivCountext+"<div id='"+orgNameC+"'>"+value.orgId+"</div>";
		});
		$("#selectDivHidden").html(selectDivCountext);
		wCounthtml = wCounthtml + "]},";
		processTimeRatehtml = processTimeRatehtml + "]},";
		
		series = "[" +  wCounthtml + processTimeRatehtml + "]";
		processTimeRateCount = processTimeRateCount/size;
		troudleshootingTimeAverageCount = troudleshootingTimeAverageCount/size;
		counttext = counttext + "<tr>"
								+"<td>平均</td>"
								+"<td>"+parseFloat(processTimeRateCount)+"%</td>"
								+"<td>"+parseFloat(troudleshootingTimeAverageCount)+"</td>"
							"</tr>";
		counttext = counttext + "</table>";
		var parseFloatTime =  processTimeRateCount + "";
		if(parseFloatTime.indexOf(".") >= 0){
			parseFloatTime = parseFloatTime.substring(0,parseFloatTime.indexOf(".") + 3);
		}
		$("#TroudleshootingTime").text(parseFloatTime);
		series = eval(series);
		var title = "抢修处理历时统计("+$("#orgName").val()+","+$("#DateTimeSpan").text()+")";
		var subtitle = "";
		var yAxisTitle2 = "工单数 (张)";
		var yAxisTitle = "工单处理历时 (小时)";
		//columnBasic(title,subtitle,yAxisTitle,"pieBasicDiv",categories,series);
		thisDataP.thistitle = title;
		thisDataP.thissubtitle = subtitle;
		thisDataP.thisyAxisTitle2 = yAxisTitle2;
		thisDataP.thisyAxisTitle = yAxisTitle;
		thisDataP.thiscategories = categories;
		thisDataP.thisseries = series;
		columnAndSplineBasicUR(title,subtitle,yAxisTitle,yAxisTitle2,"pieBasicDiv",categories,series)
		getUrgentRepairworkorderTroudleshootingTimeByDateProject(data,orgId,beginTime,endTime);
		//columnBasic(title,subtitle,yAxisTitle,"columnBasicDiv",categories,series);
}


//按组织获取故障抢修处理历时(按类型)
function getUrgentRepairworkorderTroudleshootingTimeByDateProject(data,orgId,beginTime,endTime){
//访问地址
var url = "getUrgentRepairProcessTimeRateByProjectAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
		//TABLE数据
		var counttext = "<table class='main_table tc'>"
							+"<tr class='main_table_title'>"
								+"<td style='width:35px;'></td>"
								+"<td colspan='4'>故障处理及时率</td>"
							+"</tr>"
							+"<tr>"
								+"<th style='width:55px;'>项目</th>"
								+"<th>故障处理及时率</th>"
								+"<th>故障处理及时率</th>"
							+"</tr>";
							
		var categories= new Array(); //定义一数组;	
		//图形报表数据源
		var series = "";
		var wCounthtml = "{ type:'column', name: '工单数', yAxis: 1, data: [";
		var processTimeRatehtml = "{ type: 'line',name: '工单处理历时 (小时)', yAxis: 1,color: '#FF6600', data: [";
		var processRatehtml = "{ type: 'line',name: '故障处理及时率 (%)',color: '#336600', data: [";
		var troudleshootingTimeAverageCount = 0;
		var processTimeRateCount = 0;	
		var i = 0;		
		var selectDivCountext = "";
		var size = 0;
		$.each(data, function(key, value){
			size++;
			var parseTime =  parseFloat(value.troudleshootingTimeAverage/60)+"";
			if(parseTime.indexOf(".") >= 0){
				parseTime = parseTime.substring(0,parseTime.indexOf(".") + 3);
			}
			var orgNameC = value.orgName.replace("(","");
			orgNameC = orgNameC.replace(")","");
			orgNameC = orgNameC.replace("(","");
			orgNameC = orgNameC.replace(")","");
			$("#"+orgNameC+"ProcessTime").text(parseTime);
			wCounthtml = wCounthtml + value.wCount + ",";
			processTimeRatehtml = processTimeRatehtml + parseTime + ",";
			processRatehtml = processRatehtml + value.processTimeRateCount + ",";
			counttext = counttext + "<tr>"
								+"<td>"+value.orgName+"</td>"
								+"<td>"+value.processTimeRateCount+"%</td>"
								+"<td>"+value.wCount+"</td>"
							"</tr>";
							var planTitle = "";
					if(value.orgName.length > 8 && value.orgName != null){
						var parseIndex = Math.ceil(value.orgName.length/8);
						if(parseIndex != null && parseIndex > 0){
							for(var j = 0;j < parseIndex;j++){
								var sIndex = j*8;
								var dIndex = (j+1)*8;
								planTitle = planTitle + value.orgName.substring(sIndex,dIndex) + "<br/>";
								//alert(planTitle);
							}
						}
					}else{
						planTitle = value.orgName;
					}
					categories[i]=planTitle;
							i++;
		    troudleshootingTimeAverageCount = troudleshootingTimeAverageCount + parseFloat(value.troudleshootingTimeAverage);
		    processTimeRateCount = processTimeRateCount + parseFloat(value.processTimeRateCount);
		     selectDivCountext = selectDivCountext+"<div id='"+orgNameC+"'>"+value.orgId+"</div>";
		});
		$("#selectDivHidden").html(selectDivCountext);
		wCounthtml = wCounthtml + "]},";
		processTimeRatehtml = processTimeRatehtml + "]},";
		processRatehtml = processRatehtml + "]},";
		series = "[" + wCounthtml +  processRatehtml + "]";
		processTimeRateCount = processTimeRateCount/size;
		troudleshootingTimeAverageCount = troudleshootingTimeAverageCount/size;
		counttext = counttext + "<tr>"
								+"<td>平均</td>"
								+"<td>"+parseFloat(processTimeRateCount)+"%</td>"
								+"<td>"+parseFloat(troudleshootingTimeAverageCount)+"</td>"
							"</tr>";
		var parseFloatTime =  parseFloat(troudleshootingTimeAverageCount/60)+"";
		if(parseFloatTime.indexOf(".") >= 0){
			parseFloatTime = parseFloatTime.substring(0,parseFloatTime.indexOf(".") + 3);
		}
		$("#uProcessTime").text(parseFloatTime);
		$("#ProcessTime").text(parseFloatTime);
		counttext = counttext + "</table>";
		series = eval(series);
		var title = "抢修及时率统计("+$("#orgName").val()+","+$("#DateTimeSpan").text()+")";
		var subtitle = "";
		var yAxisTitle = "故障处理及时率 (%)";
		var yAxisTitle2 = "工单数 (张)";
		thisDataB.thistitle = title;
		thisDataB.thissubtitle = subtitle;
		thisDataB.thisyAxisTitle2 = yAxisTitle2;
		thisDataB.thisyAxisTitle = yAxisTitle;
		thisDataB.thiscategories = categories;
		thisDataB.thisseries = series;
		columnAndSplineBasicU(title,subtitle,yAxisTitle,yAxisTitle2,"columnBasicDiv",categories,series);
}



	
function getUPeriodProject(){
		thisData = "";
		var orgId = $("#thisOrgId").val();
		var orgName = $("#orgName").val();
		var year = $("#hiddenYear").val();
		var month = $("#hiddenMonth").val();
		year = parseInt(year);
		month = parseInt(month);
		var beginTime = $("#hiddenbeginTime").val();
		for(var i = 5;i >= 0;i--){
			var yeart = year - i;
			var al = getYMTime(yeart,month);
			getUrgentRepairPeriodProject(orgId,orgName,al[0],al[1],al[2]);
		}
		thisData = eval("["+thisData+"]");
		getReportComment("urgentRepairPeriodProject", "org", orgId, beginTime);
		var series = "";
		var seriesC = "";
		var size = 0;
		var categories= new Array(); //定义一数组;	
		var processTimeRateCount = "";
		var parseFloatTime = "{ type: 'line',name: '工单处理历时 (小时)',color: '#FF6600', data: [";
		var processTimeRateCount = "{ type: 'line',name: '故障处理及时率 (%)' ,color: '#336600', data: [";
		var wCount = "{name: '工单数', type:'column', yAxis: 1,  data: [";
		$.each(thisData, function(key, value){
			categories[size] = value.TimeString;
			parseFloatTime = parseFloatTime + value.parseFloatTime +  ",";
			processTimeRateCount = processTimeRateCount + value.processTimeRateCount +  ",";
			wCount = wCount + parseInt(value.wCount) +  ",";
			size++;
		});
		parseFloatTime = parseFloatTime + "]},";
		processTimeRateCount = processTimeRateCount + "]},";
		wCount = wCount + "]},";
		var orgName = $("#orgName").val();
		var title1 = "抢修工单("+orgName+",同比分析)";
		var title2 = "历时/及时率统计("+orgName+",同比分析)";
		series = "[" + wCount +  processTimeRateCount + "]";
		series = eval(series);
		seriesC = "[" + wCount + parseFloatTime +"]";
		seriesC = eval(seriesC);
		thisDataP.thistitle = title2;
		thisDataP.thissubtitle = "";
		thisDataP.thisyAxisTitle2 = "工单数(张)";
		thisDataP.thisyAxisTitle = "工单处理历时 (小时)";
		thisDataP.thiscategories = categories;
		thisDataP.thisseries = seriesC;
		thisDataB.thistitle = title2;
		thisDataB.thissubtitle = "";
		thisDataB.thisyAxisTitle2 = "工单数(张)";
		thisDataB.thisyAxisTitle = "故障处理及时率 (%)";
		thisDataB.thiscategories = categories;
		thisDataB.thisseries = series;
		columnAndSplineBasicU(title2,"",'故障处理及时率 (%)',"工单数(张)","columnBasicDiv",categories,series);
		columnAndSplineBasicUR(title2,"",'工单处理历时 (小时)','工单数(张)',"pieBasicDiv",categories,seriesC);
		//columnAndSplineBasicUR(title2,"",'故障处理及时率 (%)',"工单数(张)","columnBasicDiv",categories,series);
		//columnAndSplineBasicU(title2,"",'工单处理历时 (小时)','工单数(张)',"pieBasicDiv",categories,series);
		//columnBasic(title1,"","工单数(张)","pieBasicDiv",categories,seriesC);
}


function getURingProject(){
		thisData = "";
		var orgId = $("#thisOrgId").val();
		var orgName = $("#orgName").val();
		var year = $("#hiddenYear").val();
		var month = $("#hiddenMonth").val();
		year = parseInt(year);
		month = parseInt(month);
		var beginTime = $("#hiddenbeginTime").val();
		var arr = new Array();
		arr[5] = year+","+month;
		for(var i = 4;i >= 0;i--){
			var montht = month - 1;
			if( month == 1){
				montht = month;
				month = 12;
				year = year - 1;
			}else{
				month--;
			}
			arr[i] = year+","+month;
		}
		if(arr != null){
			for(var i = 0;i < 6;i++){
				var de = arr[i].split(",");
				var al = getYMTime(de[0],de[1]);
				getUrgentRepairPeriodProject(orgId,orgName,al[0],al[1],al[2]);
			};
		}
		thisData = eval("["+thisData+"]");
		getReportComment("urgentRepairRingProject", "org", orgId, beginTime);
		var series = "";
		var seriesC = "";
		var size = 0;
		var categories= new Array(); //定义一数组;	
		var processTimeRateCount = "";
		var parseFloatTime = "{ type: 'line',name: '工单处理历时 (小时)',color: '#FF6600', data: [";
		var processTimeRateCount = "{ type: 'line',name: '故障处理及时率 (%)' ,color: '#336600', data: [";
		var wCount = "{name: '工单数', type:'column', yAxis: 1,  data: [";
		$.each(thisData, function(key, value){
			categories[size] = value.TimeString;
			parseFloatTime = parseFloatTime + value.parseFloatTime +  ",";
			processTimeRateCount = processTimeRateCount + value.processTimeRateCount +  ",";
			wCount = wCount + parseInt(value.wCount) +  ",";
			size++;
		});
		parseFloatTime = parseFloatTime + "]},";
		processTimeRateCount = processTimeRateCount + "]},";
		wCount = wCount + "]},";
		var orgName = $("#orgName").val();
		var title1 = "抢修工单("+orgName+",环比分析)";
		var title2 = "历时/及时率统计("+orgName+",环比分析)";
		series = "[" + wCount +  processTimeRateCount + "]";
		series = eval(series);
		seriesC = "[" + wCount + parseFloatTime +"]";
		seriesC = eval(seriesC);
		thisDataP.thistitle = title2;
		thisDataP.thissubtitle = "";
		thisDataP.thisyAxisTitle2 = "工单数(张)";
		thisDataP.thisyAxisTitle = "工单处理历时 (小时)";
		thisDataP.thiscategories = categories;
		thisDataP.thisseries = seriesC;
		thisDataB.thistitle = title2;
		thisDataB.thissubtitle = "";
		thisDataB.thisyAxisTitle2 = "工单数(张)";
		thisDataB.thisyAxisTitle = "故障处理及时率 (%)";
		thisDataB.thiscategories = categories;
		thisDataB.thisseries = series;
		columnAndSplineBasicU(title2,"",'故障处理及时率 (%)',"工单数(张)","columnBasicDiv",categories,series);
		columnAndSplineBasicUR(title2,"",'工单处理历时 (小时)','工单数(张)',"pieBasicDiv",categories,seriesC);
		//columnAndSplineBasicL(title2,"",'故障处理及时率 (%)','工单处理历时 (小时)',"columnBasicDiv",categories,series);
		//columnBasic(title1,"","工单数(张)","pieBasicDiv",categories,seriesC);
	
}


function getUrgentRepairPeriodProject(orgId,orgName,beginTime,endTime,TimeString){
	var url = "getUrgentRepairProcessTimeRateByProjectAction";
	var params = {orgId:orgId,beginTime:beginTime,endTime:endTime};
	 $.post(url, params, function(data){
	var troudleshootingTimeAverageCount = 0;
	var processTimeRateCount = 0;
	var wCount = 0;
	var size = 0;
	var rData = "";
	var ProcessTimeCount = 0;

	$.each(data, function(key, value){
		wCount = wCount + parseInt(value.wCount);
		troudleshootingTimeAverageCount = troudleshootingTimeAverageCount + parseFloat(value.troudleshootingTimeCountA);
		processTimeRateCount = processTimeRateCount + parseFloat(value.processTimeRateCount);
		ProcessTimeCount = ProcessTimeCount + parseFloat(value.ProcessTimeCount);
		size++;
	});
		//alert(wCount);
		//alert(processTimeRateCount);
		if( ProcessTimeCount != 0){
			//alert(ProcessTimeCount + "==" + wCount);
			ProcessTimeCount = ProcessTimeCount/wCount*100;
		}else{
			ProcessTimeCount = 0;
		}
		ProcessTimeCount = ProcessTimeCount +"";
		if(ProcessTimeCount.indexOf(".") >= 0){
			ProcessTimeCount = ProcessTimeCount.substring(0,ProcessTimeCount.indexOf(".") + 3);
		}
		//alert(processTimeRateCount);
		//alert("processTimeRateCount==="+processTimeRateCount);
		//troudleshootingTimeAverageCount = troudleshootingTimeAverageCount/size;
		//alert("troudleshootingTimeAverageCount=="+troudleshootingTimeAverageCount+"=="+wCount);
		if( troudleshootingTimeAverageCount != 0){
			troudleshootingTimeAverageCount = troudleshootingTimeAverageCount/wCount;
		}else{
			troudleshootingTimeAverageCount = 0;
		}
		var parseFloatTime =  parseFloat(troudleshootingTimeAverageCount)/60+"";
		if(parseFloatTime.indexOf(".") >= 0){
			parseFloatTime = parseFloatTime.substring(0,parseFloatTime.indexOf(".") + 3);
		}
		rData = "{'wCount':"+wCount+",'parseFloatTime':"+parseFloatTime+",'processTimeRateCount':"+ProcessTimeCount+",'orgId':"+orgId+",'orgName':'"+orgName+"','TimeString':'"+TimeString+"'},";
		thisData = thisData + rData;
	});
}



   
var columnAndSplineBasicChart1;	
		//柱状图+折线(对比)
		function columnAndSplineBasicU(title,subtitle,yAxisTitle,yAxisTitle2,divId,categories,series){
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
		
		
		
		
		 function addDiv(divId){
		 	if($("#typecheckbox").attr("checked") == "checked"){
					if(divId == 'pieBasicDiv'){
						pieBasic2(thisDataP.thistitle,thisDataP.thissubtitle,"ireportB",thisDataP.thisseries);
					}else if(divId == 'columnBasicDiv'){
						columnAndSplineBasicL(thisDataB.thistitle,thisDataB.thissubtitle,thisDataB.thisyAxisTitle,thisDataP.thisyAxisTitle2,"ireportB",thisDataB.thiscategories,thisDataB.thisseries);
					}
			}else{
					if(divId == 'pieBasicDiv'){
						columnAndSplineBasicUR(thisDataP.thistitle,thisDataP.thissubtitle,thisDataP.thisyAxisTitle,thisDataP.thisyAxisTitle2,"ireportB",thisDataP.thiscategories,thisDataP.thisseries);
					
					}else if(divId == 'columnBasicDiv'){
						columnAndSplineBasicU(thisDataB.thistitle,thisDataB.thissubtitle,thisDataB.thisyAxisTitle,thisDataB.thisyAxisTitle2,"ireportB",thisDataB.thiscategories,thisDataB.thisseries);
					}
			}
			setTimeout("addDivChar();",1000);
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
   		}
   }
   
   
   
   

var columnAndSplineBasicChart;	
		//柱状图+折线(对比)
		function columnAndSplineBasicUR(title,subtitle,yAxisTitle,yAxisTitle2,divId,categories,series){
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
		function columnAndSplineBasicUR1(title,subtitle,yAxisTitle,yAxisTitle2,divId,categories,series){
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