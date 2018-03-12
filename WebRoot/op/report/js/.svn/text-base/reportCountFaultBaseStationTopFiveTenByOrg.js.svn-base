
var currentDate;
var currentYear = null;
var currentMonth = null;
var beginTime = null;
var endTime = null;


var custom_columnBasicChart;

var isExistSubOrg=false;
var show_orgId;

var clickSelectOrgId;
var clickSelectAreaLevel;

var dataOfLarge = {};

var topOrgId;


$(function(){
	currentDate = new Date();
	currentYear=currentDate.getFullYear();
	currentMonth=currentDate.getMonth();
	//currentMonth=currentMonth+1;
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
	var currentMonth1 = parseInt(currentMonth) +1;
	var currentYear1 = currentYear;
	if(currentMonth1 > 12){
		currentMonth1 = 1;
		currentYear1 = currentYear1 + 1;
	}
	beginTime = currentYear+"-"+currentMonth+"-01 00:00:00";
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
	
	//按地域显示报表
	$("#reportByArea").click(function(){
		$("#statements_back").hide();
		var children_li=$("#compareTab li");
		$.each(children_li,function(i,n){
			if($(n).attr("class")=="selected"){
				var li_id=$(n).attr("id");
				if(li_id=="horizontalCompare"){
					getHorizontalCompareReportDataForArea();
				}else if(li_id=="loopCompare"){
					//alert("开发中");
					getLoopCompareReportDataForArea();
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
					getHorizontalCompareReportDataForArea();
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
					getLoopCompareReportDataForArea();
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
	//alert("beginTime=="+beginTime+",endTime==="+endTime);
	$.ajax({
			"url" : "getCountFaultBaseStationTopFiveTenDataAction" , 
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
				
				//var arr=[];
				//var obj={};
				//obj.name="故障数";
				//obj.data=yAxis;
				//arr.push(obj);

/*
				var arr=[{
                name: 'Tokyo',
                data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
    
            	},{
                name: 'ff',
                data: [49.9,null, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
    
            	},{
                name: 'ghg',
                data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
    
            	}];*/
            	
            	
            	var title="故障数Top-50最差基站分布（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）";
            	var subTitle="(点击图形区域，可向下钻取)";
            	
            	dataOfLarge.title=title;
            	dataOfLarge.xAxis=xAxis;
            	dataOfLarge.yAxis=yAxis;
            	dataOfLarge.orgId_xAxis=orgId_xAxis;
            	
            	$.loadReportDetailData(jsonData,"reportDetailDiv");
            	getReportComment("countFaultBaseStationTopFiveTenByOrg", "org", show_orgId, null);
				custom_columnBasic(title,subTitle,"","resultReport",xAxis,yAxis,orgId_xAxis);
			}
	});
};

//获取子组织对比周期数据
function getSubOrgHorizontalCompareReportData(orgId){
	var param={"beginTime":beginTime,"endTime":endTime,"orgId":orgId};
	$.ajax({
		"url" : "getSubOrgCountFaultBaseStationTopFiveTenDataAction" , 
		"type" : "post" , 
		"data":param,
		"success" : function ( data ) {
			var jsonData = data;
			var xAxis=jsonData.xAxis;
			var yAxis=jsonData.yAxis;
			var orgName=jsonData.topOrgName;
			show_orgId=jsonData.topOrgId;
			var orgId_xAxis=jsonData.orgId_xAxis;
			
			
			var title="故障数Top-50最差基站分布（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）";
			var subTitle="(点击图形区域，可向下钻取)";
			
			dataOfLarge.title=title;
			dataOfLarge.xAxis=xAxis;
           	dataOfLarge.yAxis=yAxis;
           	dataOfLarge.orgId_xAxis=orgId_xAxis;
			
			//显示table数据
			$.loadReportDetailData(jsonData,"reportDetailDiv");
			getReportComment("countFaultBaseStationTopFiveTenByOrg", "org", show_orgId, null);
			custom_columnBasic(title,subTitle,"","resultReport",xAxis,yAxis,orgId_xAxis);
		}
	});
}


//按地域获取对比周期的数据
function getHorizontalCompareReportDataForArea(areaId){
	var param={"beginTime":beginTime,"endTime":endTime};
	//alert("beginTime=="+beginTime+",endTime==="+endTime);
	$.ajax({
			"url" : "getCountFaultBaseStationTopFiveTenDataForAreaAction" , 
			"type" : "post" ,
			"data":param,
			"dataType":"json",
			"success" : function ( data ) {
				//var jsonData = eval( "(" + data + ")" );
				
				var jsonData = data;
				var xAxis=jsonData.xAxis;
				var yAxis=jsonData.yAxis;
				var areaId_xAxis=jsonData.areaId_xAxis;
				
				var title="故障数Top-50最差基站分布（"+currentYear+"年"+formatMonth(currentMonth)+"月）";
				var subTitle="(点击图形区域，可向下钻取)";
				dataOfLarge.title=title;
				dataOfLarge.xAxis=xAxis;
	           	dataOfLarge.yAxis=yAxis;
	           	dataOfLarge.orgId_xAxis=areaId_xAxis;
				
				
				$.loadReportDetailData(jsonData,"reportDetailDiv");
            	getReportComment("countFaultBaseStationTopFiveTenByOrg", "area", show_orgId, null);
				custom_columnBasic(title,subTitle,"","resultReport",xAxis,yAxis,areaId_xAxis);
			}
	});
};

//按地域获取子区域对比周期数据
function getSubAreaHorizontalCompareReportDataForArea(areaId){
	var param={"beginTime":beginTime,"endTime":endTime,"areaId":areaId};
	$.ajax({
		"url" : "getSubAreaCountFaultBaseStationTopFiveTenDataForAreaAction" , 
		"type" : "post" , 
		"data":param,
		"dataType":"json",
		"success" : function ( data ) {
			var jsonData = data;
			var isExistSubArea=jsonData.isExistSubArea;
			if(isExistSubArea){
				var xAxis=jsonData.xAxis;
				var yAxis=jsonData.yAxis;
				var areaId_xAxis=jsonData.areaId_xAxis;
				
				var title="故障数Top-50最差基站分布（"+currentYear+"年"+formatMonth(currentMonth)+"月）";
				var subTitle="(点击图形区域，可向下钻取)";
				dataOfLarge.title=title;
				dataOfLarge.xAxis=xAxis;
	           	dataOfLarge.yAxis=yAxis;
	           	dataOfLarge.orgId_xAxis=areaId_xAxis;
				
				
				//显示table数据
				$.loadReportDetailData(jsonData,"reportDetailDiv");
				getReportComment("countFaultBaseStationTopFiveTenByOrg", "area", show_orgId, null);
				custom_columnBasic(title,subTitle,"","resultReport",xAxis,yAxis,areaId_xAxis);
			}
			
		}
	});
}

//按项目获取对比周期的数据
function getHorizontalCompareReportDataForProject(){
	var param={"beginTime":beginTime,"endTime":endTime};
	//alert("beginTime=="+beginTime+",endTime==="+endTime);
	$.ajax({
			"url" : "getCountFaultBaseStationTopFiveTenDataForProjectAction" , 
			"type" : "post" ,
			"data":param,
			"dataType":"json",
			"success" : function ( data ) {
				//var jsonData = eval( "(" + data + ")" );
				
				var jsonData = data;
				var xAxis=jsonData.xAxis;
				var yAxis=jsonData.yAxis;
				var areaId_xAxis=jsonData.areaId_xAxis;
				
				var title="故障数Top-50最差基站分布（"+currentYear+"年"+formatMonth(currentMonth)+"月）";
				var subTitle="";
				dataOfLarge.title=title;
				dataOfLarge.xAxis=xAxis;
	           	dataOfLarge.yAxis=yAxis;
	           	dataOfLarge.orgId_xAxis=areaId_xAxis;
				
				
				$.loadReportDetailData(jsonData,"reportDetailDiv");
            	getReportComment("countFaultBaseStationTopFiveTenByOrg", "area", show_orgId, null);
				custom_columnBasic(title,subTitle,"","resultReport",xAxis,yAxis,areaId_xAxis);
			}
	});
}



//按组织获取环比周期的数据
function  getLoopCompareReportData(){
	//var tempYear=currentDate.getFullYear();
	//var temptMonth=currentDate.getMonth()+1;
	var tempYear=currentYear;
	var temptMonth=currentMonth;
	
	//var test=[];
	
	//参数集合
	var param = {};
	var num = 6;
	for(var i=0;i<num;i++){
		var str_yearMonth = getYearMonth(tempYear,temptMonth);
		//test.push(str_yearMonth);
		param["yearMonthList["+i+"]"] = str_yearMonth;
		//tempYear=tempYear-1;
		temptMonth=temptMonth-1;
		if(temptMonth==0){
			temptMonth=12;
			tempYear=currentYear-1;
		}
	}
	
	$.ajax({
	   type : "post", 
	   url : "getCountFaultBaseStationTopFiveTenLoopCompareDataAction", 
	   data : param,
	   async : true,
	   dataType:'json', 
	   success : function($data){ 
	   		var jsonData = $data;
	   		if(jsonData){
	   			var xAxis=jsonData.xAxis;
				var yAxis=jsonData.yAxis;
				var orgName=jsonData.topOrgName;
				show_orgId=jsonData.topOrgId;
				
				var title="故障数Top-50最差基站分布（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）";
				
				dataOfLarge.title=title;
				dataOfLarge.xAxis=xAxis;
            	dataOfLarge.yAxis=yAxis;
				
	   			lineBasic(title,"","","resultReport",xAxis,yAxis);
	   			//显示数据
	   			getReportComment("countFaultBaseStationTopFiveTenByOrg_loop", "org", show_orgId, null);
	   		}
   		}
	});
}



//按地域获取环比周期的数据
function  getLoopCompareReportDataForArea(){
	var tempYear=currentYear;
	var temptMonth=currentMonth;
	//参数集合
	var param = {};
	var num = 6;
	for(var i=0;i<num;i++){
		var str_yearMonth = getYearMonth(tempYear,temptMonth);
		//test.push(str_yearMonth);
		param["yearMonthList["+i+"]"] = str_yearMonth;
		//tempYear=tempYear-1;
		temptMonth=temptMonth-1;
		
		if(temptMonth==0){
			temptMonth=12;
			tempYear=currentYear-1;
		}
	}
	
	$.ajax({
	   type : "post", 
	   url : "getCountFaultBaseStationTopFiveTenLoopCompareDataForAreaAction",
	   data : param,
	   async : true,
	   dataType:'json', 
	   success : function($data){ 
	   		var jsonData = $data;
	   		if(jsonData){
	   			var xAxis=jsonData.xAxis;
				var yAxis=jsonData.yAxis;
				
				var title="故障数Top-50最差基站分布（"+currentYear+"年"+formatMonth(currentMonth)+"月）";
				dataOfLarge.xAxis=xAxis;
            	dataOfLarge.yAxis=yAxis;
				
	   			lineBasic(title,"","","resultReport",xAxis,yAxis);
	   			//显示数据
	   			getReportComment("countFaultBaseStationTopFiveTenByOrg_loop", "area", show_orgId, null);
	   		}
   		}
	});
}



//按项目获取环比周期的数据
function  getLoopCompareReportDataForProject(){
	var tempYear=currentYear;
	var temptMonth=currentMonth;
	//参数集合
	var param = {};
	param.orgId=topOrgId;
	var num = 3;
	for(var i=0;i<num;i++){
		var str_yearMonth = getYearMonth(tempYear,temptMonth);
		//test.push(str_yearMonth);
		param["yearMonthList["+i+"]"] = str_yearMonth;
		//tempYear=tempYear-1;
		temptMonth=temptMonth-1;
		
		if(temptMonth==0){
			temptMonth=12;
			tempYear=currentYear-1;
		}
	}
	
	$.ajax({
	   type : "post", 
	   url : "getCountFaultBaseStationTopFiveTenLoopCompareDataForProjectAction",
	   data : param,
	   async : true,
	   dataType:'json', 
	   success : function($data){ 
	   		var jsonData = $data;
	   		if(jsonData){
	   			var xAxis=jsonData.xAxis;
				var yAxis=jsonData.yAxis;
				
				var title="故障数Top-50最差基站分布（"+currentYear+"年"+formatMonth(currentMonth)+"月）";
				dataOfLarge.xAxis=xAxis;
            	dataOfLarge.yAxis=yAxis;
				
	   			lineBasic(title,"","","resultReport",xAxis,yAxis);
	   			//显示数据
	   			getReportComment("countFaultBaseStationTopFiveTenByOrg_loop", "project", show_orgId, null);
	   		}
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
		var currentMonth1 = parseInt(spanMonth) +1;
	var currentYear1 = currentYear;
	if(currentMonth1 > 12){
		currentMonth1 = 1;
		currentYear1 = currentYear1 + 1;
	}
		beginTime = currentYear+"-"+spanMonth+"-01 00:00:00";
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
					$.each(children_li,function(i,n){
						if($(n).attr("class")=="selected"){
							var li_id=$(n).attr("id");
							if(li_id=="horizontalCompare"){
								getHorizontalCompareReportDataForArea();
							}else if(li_id=="loopCompare"){
								getLoopCompareReportDataForArea();
							}
						}
					});
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
		
		//console.log("timeId=="+timeId);
		//getUserOrgWorkOrderCountReport(beginTime,endTime);
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
		var currentMonth1 = parseInt(currentMonth) +1;
	var currentYear1 = currentYear;
	if(currentMonth1 > 12){
		currentMonth1 = 1;
		currentYear1 = currentYear1 + 1;
	}
		beginTime = currentYear+"-"+currentMonth+"-01 00:00:00";
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
					$.each(children_li,function(i,n){
						if($(n).attr("class")=="selected"){
							var li_id=$(n).attr("id");
							if(li_id=="horizontalCompare"){
								getHorizontalCompareReportDataForArea();
							}else if(li_id=="loopCompare"){
								getLoopCompareReportDataForArea();
							}
						}
					});
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
		//getUserOrgWorkOrderCountReport(beginTime,endTime);
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
	if((!data && data!=0) || (data=="null" && data!=0)){
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
	
	
	var children_li=$("#typeTabDiv li");
	$.each(children_li,function(i,n){
		if($(n).hasClass("ontab")){
			var li_id=$(n).attr("id");
			if(li_id=="reportByOrg"){
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
			}else if(li_id=="reportByArea"){
				
				if($("#areaLevel").val()=="省"){
					getHorizontalCompareReportDataForArea();
					$("#statements_back").hide();
				}else{
					$.ajax({
						"url" : "getParentAreaBySubAreaAction" , 
						"type" : "post" ,
						"data":param,
						"success" : function ( data ) {
							var jsonData = data;
							var parentAreaId = jsonData.id;
							getSubAreaHorizontalCompareReportDataForArea(parentAreaId);
							$("#subOrgId").val(parentAreaId);
							$("#areaLevel").val(jsonData.level);
							if(parentAreaId==topOrgId){
								$("#statements_back").hide();
							}
						}
					});
				}
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
	
		//构建tableId
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
		
		var baseStationCountData=yAxis[0].data;
		if(yAxis && yAxis!=""){
			for(var i=0;i<xAxis.length;i++){
				var trElement=$("<tr></tr>");
				var tdElement=$("<td>"+xAxis[i]+"</td>");
				trElement.append(tdElement);
				tdElement=$("<td>"+formatEmptyShow(baseStationCountData[i])+"</td>");
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
		var trEle=$("<tr><th style=\"width: 55px;\">项目</th><th>故障基站数</th></tr>");
		tableElement.append(trEle);
		return tableElement;
	},
	createCountSummationDiv :function(jsonData){
	
		var tableId=this.tableId;
	
		var titleDiv=$("<div class=\"statements_table_top\"></div>");
		titleDiv.append("<span><a id=\""+tableId+"Div\" class=\"\"><em>[+]</em>展开表格</a></span>");
		titleDiv.append("<span> 故障基站总数：<em id=\"uCount\">"+jsonData.baseStationTotalCount+"</em></span>");
		return titleDiv;
	},
	customer_addEventsListeners :function(){
		var tableId=this.tableId;
		$("#"+tableId+"Div").toggle(function(){
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
                          
                          	if(flagId_xAxis && flagId_xAxis!=""){
                          		clickSelectOrgId=flagId_xAxis[this.x];
	                          	$("#statements_back").show();
	                          	$("#subOrgId").val(clickSelectOrgId);
	                          	
	                          	
	                          	var children_li=$("#typeTabDiv li");
								$.each(children_li,function(i,n){
									if($(n).hasClass("ontab")){
										var li_id=$(n).attr("id");
										if(li_id=="reportByOrg"){
										}else if(li_id=="reportByArea"){
											//获取区域信息
				                          	$.ajax({
												"url" : "getAreaInfoByAreaIdAction" , 
												"type" : "post" , 
												"data":{
													"areaId":clickSelectOrgId
												},
												"async":false,
												'dataType':'text',
												"success" : function ( data ) {
													var jsonData = eval( "(" + data + ")" );
													$("#areaLevel").val(jsonData.level);
												}
											});
										}
									}
								});
	                          	
	                          	clickShowMore(clickSelectOrgId,this.series.name);
                          	}
                          	//clickShowMore(this.x,this.series.name);
                          	
                          }
                      }
                  }
              }
          },
              series: series
      });
}


function clickShowMore (flagId_xAxis,series_name){
	if(flagId_xAxis=="" || flagId_xAxis=="0"){
		
	}else{
		var children_li=$("#typeTabDiv li");
		$.each(children_li,function(i,n){
			if($(n).hasClass("ontab")){
				var li_id=$(n).attr("id");
				if(li_id=="reportByOrg"){	//按组织
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
							isExistSubOrg=jsonData.isExistSubOrg;
						}
					});
					
					if(isExistSubOrg){
						getSubOrgHorizontalCompareReportData(flagId_xAxis);
					}else{
						//alert("没有下级组织");
					}
				}else if(li_id=="reportByArea"){	//按区域
					getSubAreaHorizontalCompareReportDataForArea(flagId_xAxis);
				}
			}
		});
		
	
		
	}
}


//监听提交评论按钮事件
function clickGetUrgentRepairworkorderReport(){

	//先按维度分，再按比较分

	var children_li=$("#compareTab li");
	$.each(children_li,function(i,n){
		if($(n).attr("class")=="selected"){
			var li_id=$(n).attr("id");
			if(li_id=="horizontalCompare"){
				getReportComment("countFaultBaseStationTopFiveTenByOrg", "org", show_orgId, null);
			}else if(li_id=="loopCompare"){
				getReportComment("countFaultBaseStationTopFiveTenByOrg_loop", "org", show_orgId, null);
			}
		}
	});
}


function addDiv(divId){


	var children_li=$("#compareTab li");
	$.each(children_li,function(i,n){
		if($(n).attr("class")=="selected"){
			var li_id=$(n).attr("id");
			if(li_id=="horizontalCompare"){
				custom_columnBasic(dataOfLarge.title,"","",divId,dataOfLarge.xAxis,dataOfLarge.yAxis);
			}else if(li_id=="loopCompare"){
				lineBasic(dataOfLarge.title,"","",divId,dataOfLarge.xAxis,dataOfLarge.yAxis);
			}
		}
	});
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
