
var currentDate;
var currentYear = null;
var currentMonth = null;
var beginTime = null;
var endTime = null;

var custom_columnBasicChart;

var isExistSubOrg=false;
var show_orgId;
var clickSelectOrgId;

var dataOfLarge = {};

var topOrgId;

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
	
	//获取当前登录人最高级组织
	getUserTopOrg();
	
	
	getHorizontalCompareReportData();
	
	
	//按组织显示报表
	$("#reportByOrg").click(function(){
		$("#statements_back").hide();
		var children_li=$("#compareTab li");
		$.each(children_li,function(i,n){
			if($(n).attr("class")=="selected"){
				var li_id=$(n).attr("id");
				if(li_id=="horizontalCompare"){
					getHorizontalCompareReportData();
				}else if(li_id=="loopCompare"){
					getLoopCompareReportData();
				}
			}
		});
		
	});
	
	//按项目显示报表
	$("#reportByProject").click(function(){
		
		$("#statements_back").hide();
		var children_li=$("#compareTab li");
		$.each(children_li,function(i,n){
			if($(n).attr("class")=="selected"){
				var li_id=$(n).attr("id");
				if(li_id=="horizontalCompare"){
					getHorizontalCompareReportDataForProject();
				}else if(li_id=="loopCompare"){
					getLoopCompareReportDataForProject();
				}
			}
		});
	});

	
	//横向对比点击事件
	$("#horizontalCompare").click(function(){
		//$(".statements_search_date").get(0).style.visibility="hidden";
		$("#statements_back").hide();
		clickClearFix(this);
		var children_li=$("#typeTabDiv li");
		$.each(children_li,function(i,n){
			if($(n).hasClass("ontab")){
				var li_id=$(n).attr("id");
				if(li_id=="reportByOrg"){
					getHorizontalCompareReportData();
				}else if(li_id=="reportByArea"){
					
				}else if(li_id=="reportByProject"){
					getHorizontalCompareReportDataForProject();
				}
			}
		});
	});
	
	//环比点击事件
	$("#loopCompare").click(function(){
		clickClearFix(this);
		$("#statements_back").hide();
		var children_li=$("#typeTabDiv li");
		$.each(children_li,function(i,n){
			if($(n).hasClass("ontab")){
				var li_id=$(n).attr("id");
				if(li_id=="reportByOrg"){
					getLoopCompareReportData();
				}else if(li_id=="reportByArea"){
					
				}else if(li_id=="reportByProject"){
					getLoopCompareReportDataForProject();
				}
			}
		});
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
				topOrgId=jsonData.topOrgId;
				var orgId_xAxis=jsonData.orgId_xAxis;
				
				//显示table数据
				$.loadReportDetailData(jsonData,"reportDetailDiv");
				
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
            	
            	var title="每百基站人/车/任务量统计分析（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）";
            	var subTitle="(点击图形区域，可向下钻取)";
            	var yAxisRightTitle="工单数 （张）";
            	var yAxisLeftTitle="人员 （个） 车辆 （辆）";
            	dataOfLarge.title=title;
            	dataOfLarge.yAxisRightTitle=yAxisRightTitle;
            	dataOfLarge.yAxisLeftTitle=yAxisLeftTitle;
            	dataOfLarge.xAxis=xAxis;
            	dataOfLarge.yAxis=yAxis;
            	dataOfLarge.orgId_xAxis=orgId_xAxis;
            	
            	getReportComment("analyseFaultCountAndResourceBalance", "org", show_orgId, beginTime);
            	custom_columnAndSplineBasic(title,subTitle,yAxisRightTitle,yAxisLeftTitle,"resultReport",xAxis,yAxis,orgId_xAxis);
				//custom_columnBasic("每百基站人/车/任务量统计分析（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）","(点击图形区域，可向下钻取)","","resultReport",xAxis,yAxis,orgId_xAxis);
			}
	});
};


//获取子组织对比周期数据
function getSubOrgHorizontalCompareReportData(orgId){
	var param={"beginTime":beginTime,"endTime":endTime,"orgId":orgId};	
	$.ajax({
			"url" : "getSubOrgAnalyseFaultCountAndResourceBalanceDataAction" , 
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
				
				
				var title="每百基站人/车/任务量统计分析（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）";
				var subTitle="(点击图形区域，可向下钻取)";
				var yAxisRightTitle="工单数 （张）";
            	var yAxisLeftTitle="人员 （个） 车辆 （辆）";
				dataOfLarge.title=title;
            	dataOfLarge.yAxisRightTitle=yAxisRightTitle;
            	dataOfLarge.yAxisLeftTitle=yAxisLeftTitle;
            	dataOfLarge.xAxis=xAxis;
            	dataOfLarge.yAxis=yAxis;
            	dataOfLarge.orgId_xAxis=orgId_xAxis;
				
				
				//显示table数据
				$.loadReportDetailData(jsonData,"reportDetailDiv");
            	getReportComment("analyseFaultCountAndResourceBalance", "org", show_orgId, beginTime);
            	custom_columnAndSplineBasic(title,subTitle,yAxisRightTitle,yAxisLeftTitle,"resultReport",xAxis,yAxis,orgId_xAxis);
				//custom_columnBasic("每百基站人/车/任务量统计分析（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）","(点击图形区域，可向下钻取)","","resultReport",xAxis,yAxis,orgId_xAxis);
			}
	});
};




//获取环比周期的数据
function  getLoopCompareReportData(){
	//var tempYear=currentDate.getFullYear();
	//var temptMonth=currentDate.getMonth()+1;
	
	var tempYear=currentYear;
	var temptMonth=currentMonth;
	
	//参数集合
	var param = {};
	var num = 6;
	for(var i=0;i<num;i++){
		//alert("tempYear=="+tempYear+"，temptMonth==="+temptMonth);
		var str_yearMonth = getYearMonth(tempYear,temptMonth);
		//alert("str_yearMonth=="+str_yearMonth);
		param["yearMonthList["+i+"]"] = str_yearMonth;
		
		temptMonth=temptMonth-1;
		
		if(temptMonth==0){
			temptMonth=12;
			tempYear=currentYear-1;
		}
	}
	
	$.ajax({
	   type : "post", 
	   url : "getAnalyseFaultCountAndResourceBalanceReportLoopCompareDataAction", 
	   data : param,
	   async : true,
	   dataType:'json', 
	   success : function($data){
	   		var jsonData = $data;
	   		if(jsonData){
	   			var xAxis=jsonData.xAxis;
				var yAxis=jsonData.yAxis;
				var orgName=jsonData.topOrgName;
				getReportComment("analyseFaultCountAndResourceBalance_loop", "org", show_orgId, null);
				
				
				
				var title="每百基站人/车/任务量统计分析（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）";
				var yAxisRightTitle="工单数 （张）";
            	var yAxisLeftTitle="人员 （个） 车辆 （辆）";
            	dataOfLarge.title=title;
            	dataOfLarge.yAxisRightTitle=yAxisRightTitle;
            	dataOfLarge.yAxisLeftTitle=yAxisLeftTitle;
            	dataOfLarge.xAxis=xAxis;
            	dataOfLarge.yAxis=yAxis;
				
	   			custom_columnAndSplineBasic(title,"",yAxisRightTitle,yAxisLeftTitle,"resultReport",xAxis,yAxis);
	   			//curOrgId = orgId;
	   		}
   			//getReportComment("orgWorkOrderCountChain", "organization", curOrgId, beginTime);
   		}
	});
}



//按项目获取对比周期的数据
function getHorizontalCompareReportDataForProject(){
	var param={"beginTime":beginTime,"endTime":endTime};	
	$.ajax({
			"url" : "getAnalyseFaultCountAndResourceBalanceDataForProjectAction" , 
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
				
				//显示table数据
				$.loadReportDetailData(jsonData,"reportDetailDiv");
            	
            	var title="每百基站人/车/任务量统计分析（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）";
            	var subTitle="";
            	var yAxisRightTitle="工单数 （张）";
            	var yAxisLeftTitle="人员 （个） 车辆 （辆）";
            	dataOfLarge.title=title;
            	dataOfLarge.yAxisRightTitle=yAxisRightTitle;
            	dataOfLarge.yAxisLeftTitle=yAxisLeftTitle;
            	dataOfLarge.xAxis=xAxis;
            	dataOfLarge.yAxis=yAxis;
            	dataOfLarge.orgId_xAxis=orgId_xAxis;
            	
            	getReportComment("analyseFaultCountAndResourceBalance_project", "org", show_orgId, beginTime);
            	custom_columnAndSplineBasic(title,subTitle,yAxisRightTitle,yAxisLeftTitle,"resultReport",xAxis,yAxis,orgId_xAxis);
				//custom_columnBasic("每百基站人/车/任务量统计分析（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）","(点击图形区域，可向下钻取)","","resultReport",xAxis,yAxis,orgId_xAxis);
			}
	});
};


//按项目获取环比周期的数据
function  getLoopCompareReportDataForProject(){

	var tempYear=currentYear;
	var temptMonth=currentMonth;
	
	//参数集合
	var param = {};
	var num = 6;
	for(var i=0;i<num;i++){
		//alert("tempYear=="+tempYear+"，temptMonth==="+temptMonth);
		var str_yearMonth = getYearMonth(tempYear,temptMonth);
		//alert("str_yearMonth=="+str_yearMonth);
		param["yearMonthList["+i+"]"] = str_yearMonth;
		
		temptMonth=temptMonth-1;
		
		if(temptMonth==0){
			temptMonth=12;
			tempYear=currentYear-1;
		}
	}

	$.ajax({
			"url" : "getAnalyseFaultCountAndResourceBalanceReportLoopCompareDataForProjectAction" , 
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
				
				//显示table数据
				$.loadReportDetailData(jsonData,"reportDetailDiv");
            	
            	var title="每百基站人/车/任务量统计分析（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）";
            	var subTitle="";
            	var yAxisRightTitle="工单数 （张）";
            	var yAxisLeftTitle="人员 （个） 车辆 （辆）";
            	dataOfLarge.title=title;
            	dataOfLarge.yAxisRightTitle=yAxisRightTitle;
            	dataOfLarge.yAxisLeftTitle=yAxisLeftTitle;
            	dataOfLarge.xAxis=xAxis;
            	dataOfLarge.yAxis=yAxis;
            	dataOfLarge.orgId_xAxis=orgId_xAxis;
            	
            	getReportComment("analyseFaultCountAndResourceBalance_project", "org", show_orgId, null);
            	custom_columnAndSplineBasic(title,subTitle,yAxisRightTitle,yAxisLeftTitle,"resultReport",xAxis,yAxis,orgId_xAxis);
				//custom_columnBasic("每百基站人/车/任务量统计分析（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）","(点击图形区域，可向下钻取)","","resultReport",xAxis,yAxis,orgId_xAxis);
			}
	});
}



//计算年月
function getYearMonth(year, month){

	if(month > 12){
		month = 1;
		year = year +1;
	}
	if(month < 1){
		month = 12;
		year = year -1;
	}
	
	if(month<10){
		month="0"+month;
	}
	
    var res=year+"-"+month;
   	return res;
}

var timeId=0;

//获取上一个月
function getLastMonth(){

	if(timeId){
		clearTimeout(timeId);
		timeId=0;
	}
	
	timeId  = setTimeout(function(){
		currentMonth = currentMonth - 1;
		if(currentMonth==0){
			currentMonth=12;
			currentYear=currentYear-1;
		}
		var spanMonth = "";
		if(currentMonth< 10){
			spanMonth = "0"+currentMonth;
		}else{
			spanMonth = currentMonth
		}
		$("#DateTimeSpan").text(currentYear+"年"+spanMonth+"月");
		
		//获取上个月报表数据
		beginTime = currentYear+"-"+spanMonth+"-01 00:00:00";
		var currentMonth1 = parseInt(spanMonth) +1;
		var currentYear1 = currentYear;
		if(currentMonth1 > 12){
			currentMonth1 = 1;
			currentYear1 = currentYear1 + 1;
		}
		endTime = currentYear1+"-"+currentMonth1+"-01 00:00:00";
	
		var tab_li=$("#typeTabDiv li");
		var children_li=$("#compareTab li");
		$.each(tab_li,function(i,n){
			var className=$(n).attr("class");
			if(className=="ontab"){
				var li_id=$(n).attr("id");
				if(li_id=="reportByOrg"){
					$.each(children_li,function(i,n){
						if($(n).attr("class")=="selected"){
							var li_id=$(n).attr("id");
							if(li_id=="horizontalCompare"){
								getHorizontalCompareReportData();
							}else if(li_id=="loopCompare"){
								getLoopCompareReportData();
							}
						}
					});
				}else if(li_id=="reportByArea"){
				
				}else if(li_id=="reportByProject"){
					$.each(children_li,function(i,n){
						if($(n).attr("class")=="selected"){
							var li_id=$(n).attr("id");
							if(li_id=="horizontalCompare"){
								getHorizontalCompareReportDataForProject();
							}else if(li_id=="loopCompare"){
								getLoopCompareReportDataForProject();
							}
						}
					});
				}
			}
		});

	},300);
	
	
}

//获取下一个月
function getNextMonth(){

	if(timeId){
		clearTimeout(timeId);
		timeId=0;
	}
	
	timeId  = setTimeout(function(){
		currentMonth = currentMonth + 1;
		if(currentMonth==13){
			currentMonth=1;
			currentYear=currentYear+1;
		}
		var spanMonth = "";
		if(currentMonth< 10){
			spanMonth = "0"+currentMonth;
		}else{
			spanMonth = currentMonth
		}
		$("#DateTimeSpan").text(currentYear+"年"+spanMonth+"月");
		
		//获取下个月报表数据
		beginTime = currentYear+"-"+currentMonth+"-01 00:00:00";
		var currentMonth1 = parseInt(currentMonth) + 1;
		var currentYear1 = currentYear;
		if(currentMonth1 > 12){
			currentMonth1 = 1;
			currentYear1 = currentYear1 + 1;
		}
		endTime = currentYear1+"-"+currentMonth1+"-01 00:00:00";
		
		var tab_li=$("#typeTabDiv li");
		var children_li=$("#compareTab li");
		$.each(tab_li,function(i,n){
			var className=$(n).attr("class");
			if(className=="ontab"){
				var li_id=$(n).attr("id");
				if(li_id=="reportByOrg"){
					$.each(children_li,function(i,n){
						if($(n).attr("class")=="selected"){
							var li_id=$(n).attr("id");
							if(li_id=="horizontalCompare"){
								getHorizontalCompareReportData();
							}else if(li_id=="loopCompare"){
								getLoopCompareReportData();
							}
						}
					});
				}else if(li_id=="reportByArea"){
				
				}else if(li_id=="reportByProject"){
					$.each(children_li,function(i,n){
						if($(n).attr("class")=="selected"){
							var li_id=$(n).attr("id");
							if(li_id=="horizontalCompare"){
								getHorizontalCompareReportDataForProject();
							}else if(li_id=="loopCompare"){
								getLoopCompareReportDataForProject();
							}
						}
					});
				}
			}
		});
		
		
		
	},300);
	
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


function formatEmptyShow(data){
	if(!data || data=="null"){
		return "";
	}
	return data;
}



//获取当前登录人最高级组织id
function getUserTopOrg(){
	$.ajax({
		"url" : "getUserTopOrgAction" , 
		"type" : "post" ,
		"success" : function ( data ) {
			var jsonData = data;
			var topOrgId = jsonData.orgId;
			$("#subOrgId").val(topOrgId);
			$("#topOrgId").val(topOrgId);
			
		}
	});
}


//向上钻取获取报表
function clickReturnReport(){
	//alert("clickSelectOrgId=="+clickSelectOrgId);
	var subOrgId=$("#subOrgId").val();
	var topOrgId=$("#topOrgId").val();
	var param={"orgId":subOrgId};
	$.ajax({
		"url" : "getParentOrgBySubOrgAction" , 
		"type" : "post" ,
		"data":param,
		"success" : function ( data ) {
			var jsonData = data;
			var parentOrgId = jsonData.id;
			
			getSubOrgHorizontalCompareReportData(parentOrgId);
			$("#subOrgId").val(parentOrgId);
			
			if(parentOrgId==topOrgId){
				$("#statements_back").hide();
			}
		}
	});
}



/*
加载报表table统计数据
*/
$.extend({
	//load report Data
	loadReportDetailData : function(jsonData,divId) {
		this.createTable(jsonData,divId);
		this.customer_addEventsListeners();
	},
	//构造实体信息的显示
	createTable : function(jsonData,divId) {
		var dateId = this.createTableId();
		var tableId = "table_" + dateId;
		
		this.tableId=tableId;
		
		var $table=$("<table id=\""+tableId+"\" class=\"main_table tc\"></table>");
		var $tbody=$("<tbody></tbody>");
		$tbody.appendTo($table);
		$table=this.createTableHead($table);
		
		
		//dynamic construct entity
		var yAxis=jsonData.yAxis;
		var xAxis=jsonData.xAxis;
		
		var workOrderCountData=yAxis[0].data;
		var peopleCounttData=yAxis[1].data;
		var carCountData=yAxis[2].data;
		
		if(yAxis && yAxis!=""){
			for(var i=0;i<xAxis.length;i++){
				var trElement=$("<tr></tr>");
				var tdElement=$("<td>"+xAxis[i]+"</td>");
				trElement.append(tdElement);
				tdElement=$("<td>"+formatEmptyShow(workOrderCountData[i])+"</td>");
				trElement.append(tdElement);
				tdElement=$("<td>"+formatEmptyShow(peopleCounttData[i])+"</td>");
				trElement.append(tdElement);
				tdElement=$("<td>"+formatEmptyShow(carCountData[i])+"</td>");
				trElement.append(tdElement);
				$table.append(trElement);
			}
		}
		
		var summationDiv=this.createCountSummationDiv(jsonData);
		//summationDiv.appendTo($("#"+divId));
		$("#"+divId).html(summationDiv);
		
		var wrapDiv=$("<div class=\"statements_main_table\" style=\"display: none;\">");
		wrapDiv.append($table);
		wrapDiv.appendTo($("#"+divId));
		//$("#"+divId).html(wrapDiv);
		
	},
	createTableId : function() {	//创建table标识
		var today = new Date();
		var hh = today.getHours() + "";
		var mm = today.getMinutes() + "";
		var ss = today.getSeconds() + "";
		return (hh + mm + ss);
	},
	createTableHead :function(tableElement){
		var trEle=$("<tr><th style=\"width: 55px;\">项目</th><th>工单数量</th><th>人员数量</th><th>车辆数量</th></tr>");
		tableElement.append(trEle);
		return tableElement;
	},
	createCountSummationDiv :function(jsonData){
		var titleDiv=$("<div class=\"statements_table_top\"></div>");
		titleDiv.append("<span><a id=\"showCountDiv\" class=\"\"><em>[+]</em>展开表格</a></span>");
		titleDiv.append("<span> 工单总数：<em id=\"uCount\">"+jsonData.per_workOrderTotalCount+"</em></span>");
		titleDiv.append("<span> 人员总数：<em id=\"uCount\">"+jsonData.per_peopleTotalCount+"</em></span>");
		titleDiv.append("<span> 车辆总数：<em id=\"uCount\">"+jsonData.per_carTotalCount+"</em></span>");
		return titleDiv;
	},
	customer_addEventsListeners :function(){
		var tableId=this.tableId;
		$("#showCountDiv").toggle(function(){
			//show
			$("#"+tableId).parent().show();
			$(this).children().html("[-]");
		},function(){
			//hide
			$("#"+tableId).parent().hide();
			$(this).children().html("[+]");
		});
	},
	tableId:""
});


function clickShowMore (flagId_xAxis,series_name){
	//alert(flagId_xAxis+"----"+series_name);
	if(flagId_xAxis=="" || flagId_xAxis=="0"){
	
	}else{
		$.ajax({
			"url" : "judgeOrgIsExistSubOrgAction" , 
			"type" : "post" , 
			"data":{
				"orgId":flagId_xAxis
			},
			"async":false,
			'dataType':'text',
			"success" : function ( data ) {
				var jsonData = eval( "(" + data + ")" );
				//var jsonData = data;
				//alert("zzzzz");
				isExistSubOrg=jsonData.isExistSubOrg;
			}
		});
		
		if(isExistSubOrg){
			getSubOrgHorizontalCompareReportData(flagId_xAxis);
			/*$.ajax({
				"url" : "getSubOrgAnalyseFaultCountAndResourceBalanceDataAction" , 
				"type" : "post" , 
				"data":{
					"orgId":flagId_xAxis
				},
				"success" : function ( data ) {
					//var jsonData = eval( "(" + data + ")" );
					var jsonData = data;
					var xAxis=jsonData.xAxis;
					var yAxis=jsonData.yAxis;
					var orgName=jsonData.topOrgName;
					show_orgId=jsonData.topOrgId;
					var orgId_xAxis=jsonData.orgId_xAxis;
					getReportComment("analyseFaultCountAndResourceBalance", "org", show_orgId, null);
					custom_columnBasic("每百基站人/车/任务量统计分析（"+orgName+"）","(点击图形区域，可向下钻取)","","resultReport",xAxis,yAxis,orgId_xAxis);
				}
			});*/
		}else{
			//alert("没有下级组织");
		}
	}
}


//监听提交评论按钮事件
function clickGetUrgentRepairworkorderReport(){
	var children_li=$("#compareTab li");
	$.each(children_li,function(i,n){
		if($(n).attr("class")=="selected"){
			var li_id=$(n).attr("id");
			if(li_id=="horizontalCompare"){
				getReportComment("analyseFaultCountAndResourceBalance", "org", show_orgId, beginTime);
			}else if(li_id=="loopCompare"){
				getReportComment("analyseFaultCountAndResourceBalance_loop", "org", show_orgId, beginTime);
			}
		}
	});
}


//柱状图(对比)
function custom_columnBasic(title,subtitle,yAxisTitle,divId,categories,series,flagId_xAxis){
     	 custom_columnBasicChart = new Highcharts.Chart({
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
              categories: categories,
              flagId_xAxis :flagId_xAxis
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
                          	//clickShowMore(categories[this.x],this.series.name);
                          	clickShowMore(flagId_xAxis[this.x],this.series.name);
                          	//clickShowMore(this.x,this.series.name);
                          	
                          }
                      }
                  }
              }
          },
              series: series
      });
}


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
                  rotation: 0,
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
                          	clickSelectOrgId=flagId_xAxis[this.x];
                          	
                          	var tab_li=$("#typeTabDiv li");
							$.each(tab_li,function(i,n){
								var className=$(n).attr("class");
								if(className=="ontab"){
									var li_id=$(n).attr("id");
									if(li_id=="reportByOrg"){
										$("#statements_back").show();
									}else if(li_id=="reportByProject"){

									}
								}
							});
                          	
                          	
                          	$("#subOrgId").val(clickSelectOrgId);
                          	clickShowMore(clickSelectOrgId,this.series.name);
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
	custom_columnAndSplineBasic(dataOfLarge.title,"",dataOfLarge.yAxisRightTitle,dataOfLarge.yAxisLeftTitle,divId,dataOfLarge.xAxis,dataOfLarge.yAxis);
	setTimeout("addDivChar();",1000);
}


function addDivChar(){
  	var report_statements_full = document.getElementById("report_statements_full_newNode");
//	window.top.document.body.removeChild(report_statements_full); 
	if(report_statements_full != null){
	}else{
		var chartext = $("#ireportDiv").html();
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
