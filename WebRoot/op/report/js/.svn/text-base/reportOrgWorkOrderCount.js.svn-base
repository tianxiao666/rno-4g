var curYear = null;
var curMonth = null;
var beginTime = null;
var endTime = null;
var curOrgId = null;
var userOrgId = null;
$(function(){
	$("#compareTab li").each(function(i){
		$(this).click(function(){
			$("#compareTab li").each(function(k){
				var clz = "";
				if(i==k){
					clz = "selected";
				}
				$(".st_full_screen").show();
				$(this).attr("class",clz);
			})
		})
	});
	/*获取当前用户报表信息*/
	getCurUserReportInfo();
	//对比点击事件
	$("#compareLi").click(function(){
		var curDate = new Date();
		var year=curDate.getFullYear();
		var month=curDate.getMonth();
		var spanMonth = "";
		if(month < 10){
			if(month<1){
				year = year - 1;
				month = 12;
				spanMonth = month;
			}else{
				spanMonth = "0"+month;
			}
		}else{
			spanMonth = month;
		}
		curYear = year;
		curMonth = month;
		$("#DateTimeSpan").text(year+"年"+spanMonth+"月");
		//默认获取当前用户组织架构下工单数报表数据
		var currentMonth1 = parseInt(curMonth) +1;
		var currentYear1 = curYear;
		if(currentMonth1 > 12){
			currentMonth1 = 1;
			currentYear1 = currentYear1 + 1;
		}
		beginTime = curYear+"-"+curMonth+"-01 00:00:00";
		endTime = currentYear1+"-"+currentMonth1+"-01 00:00:00";
		if($("#projectLi").attr("class") == "ontab"){
			getOrgProjectReportInfo();
		}else{
			getUserOrgWorkOrderCountReport(beginTime,endTime);
		}
	});
	//同比点击事件
	$("#sameCompareLi").click(function(){
		$("#reportDetailTable").html("");
		$("#countDetailSpan").html("");
		$(".st_full_screen").hide();
		$("#statements_back").hide();	
		$("#orgWorkOrderCountDiv").html("暂无数据");
		getReportComment("orgWorkOrderCountSameCompare", "organization", curOrgId, beginTime);
	});
	//环比点击事件
	$("#chainLi").click(function(){
		getUserOrgChainReport();
	});
	/*表格控制*/
	$("#tableControl").click(function(){
		if($("#reportDetailTable").css("display") == "none"){
			$(this).html("<em>[-]</em>展开表格");
			$("#reportDetailTable").css("display","table");
		}else{
			$(this).html("<em>[+]</em>展开表格");
			$("#reportDetailTable").css("display","none");
		}
	});
});
/*环比*/
function getUserOrgChainReport(){
	$("#reportDetailTable").html("");
	$("#countDetailSpan").html("");
	var chainList = new Array();
	var lastMonth = new Array();
	lastMonth[0] = curYear;
	lastMonth[1] = curMonth;
	//参数集合
	var param = {};
	param["yearMonthList[0]"] = curYear+"-"+curMonth;
	var num = 5;	
	for(var i=0;i<num;i++){
		lastMonth = dateFormat(lastMonth[0],lastMonth[1]-1);
		var index = i+1;
		param["yearMonthList["+index+"]"] = lastMonth[0]+"-"+lastMonth[1];
	}
	param.orgId = curOrgId;
	$.ajax({ 
	   type : "post", 
	   url : "getOrgWorkOrderCountChainReportAction", 
	   data : param,
	   async : true,
	   dataType:'json', 
	   success : function($data){ 
	   		var res = $data;
	   		if(res){
	   			var reportMap = [res.reportMap];
		   		var nameList = res.nameList;
	   			var orgId = res.orgId;
	   			var isTeam = res.isTeam;
	   			var orgName = res.orgName;
	   			if(isTeam){
	   				lineBasic("人员任务量环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","任务数（张）","orgWorkOrderCountDiv",nameList,reportMap);
	   				lineBasic("人员任务量环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","任务数（张）","ireportDiv",nameList,reportMap);
	   			}else{
	   				lineBasic("人员任务量环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","工单数（张）","orgWorkOrderCountDiv",nameList,reportMap);
	   				lineBasic("人员任务量环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","工单数（张）","ireportDiv",nameList,reportMap);
	   			}
	   			curOrgId = orgId;
	   		}
   			getReportComment("orgWorkOrderCountChain", "organization", curOrgId, beginTime);
   		} 
	});
}
/*报表上钻*/
function clickReturnReport(){
	if(userOrgId == curOrgId){
		$("#statements_back").hide();
		return false;	
	}
	$("#statements_back").show();
	if($("#chainLi").attr("class") == "selected"){
		$("#reportDetailTable").html("");
		$("#countDetailSpan").html("");
		var chainList = new Array();
		var lastMonth = new Array();
		lastMonth[0] = curYear;
		lastMonth[1] = curMonth;
		//参数集合
		var param = {};
		param["yearMonthList[0]"] = curYear+"-"+curMonth;
		var num = 2;	
		for(var i=0;i<num;i++){
			lastMonth = dateFormat(lastMonth[0],lastMonth[1]-1);
			var index = i+1;
			param["yearMonthList["+index+"]"] = lastMonth[0]+"-"+lastMonth[1];
		}
		param.orgId = curOrgId;
		$.ajax({ 
		   type : "post", 
		   url : "getUpOrgWorkOrderCountChainReportAction", 
		   data : param,
		   async : false,
		   dataType:'json', 
		   success : function($data){ 
		   		var res = $data;
		   		if(res){
		   			var reportMap = [res.reportMap];
			   		var nameList = res.nameList;
		   			var orgId = res.orgId;
		   			var isTeam = res.isTeam;
		   			var orgName = res.orgName;
		   			curOrgId = orgId;
		   			if(isTeam){
		   				lineBasic("人员任务量环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","任务数（张）","orgWorkOrderCountDiv",nameList,reportMap);
		   				lineBasic("人员任务量环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","任务数（张）","ireportDiv",nameList,reportMap);
		   			}else{
		   				lineBasic("人员任务量环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","工单数（张）","orgWorkOrderCountDiv",nameList,reportMap);
		   				lineBasic("人员任务量环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","任务数（张）","ireportDiv",nameList,reportMap);
		   			}
		   			curOrgId = orgId;
		   		}
	   		} 
		});
		getReportComment("orgWorkOrderCountChain", "organization", curOrgId, beginTime);
	}else if($("#compareLi").attr("class") == "selected"){
		$.ajax({ 
		   type : "post", 
		   url : "getUpOrgWorkOrderCountReportByOrgIdAction", 
		   data : {beginTime:beginTime,endTime:endTime,orgId:curOrgId},
		   async : false,
		   dataType:'json', 
		   success : function($data){ 
		   		var res = $data;
		   		if(res){
			   		var reportMap = [res.reportMap];
			   		var reportType = res.reportType;
			   		var orgName = res.orgName;
			   		var orgId = res.userOrgId;
			   		curOrgId = orgId;
			   		if(reportType=='org'){
			   			var orgNameList = res.orgNameList;
			   			var orgIdList = res.orgIdList;
			   			columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）',"orgWorkOrderCountDiv",orgNameList,reportMap,orgIdList);
			   			columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）',"ireportDiv",orgNameList,reportMap,orgIdList);
			   		}else if(reportType=='human'){
			   			var staffNameList = res.staffNameList;
			   			columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'','任务数（张）',"orgWorkOrderCountDiv",staffNameList,reportMap,null);
			   			columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'','任务数（张）',"ireportDiv",staffNameList,reportMap,null);
			   		}
			   		loadWorkOrderCountTableData(res,reportType);
		   		}
			} 
		});
		getReportComment("orgWorkOrderCount", "organization", curOrgId, beginTime);
	}
	if(curOrgId == userOrgId){
		$("#statements_back").hide();
	}
}
/*加载报表表格数据*/
function loadWorkOrderCountTableData(data,type){
	$("#reportDetailTable").html("");
	$("#countDetailSpan").html("");
	if(!data){
		return false;
	}
	var orgCount = 0;
	var staffCount = 0;
	var orgNameList = data.orgNameList;
	var curType = "组织";
	if(type=='org'){
		type = "工单";
	}else if(type=='project'){
		type = "工单";
		curType = "项目";
	}else{
		orgNameList = data.staffNameList;
		type="任务单";
		curType = "人员";
	}
	var th = $("<tr><th>"+curType+"名称</th><th>"+type+"数量</th></tr>");
	th.appendTo($("#reportDetailTable"));
	var reportMap = data.reportMap;
	if(orgNameList && reportMap){
		var dataList = reportMap.data;
		orgCount = orgNameList.length;
		for(var i=0;i<orgNameList.length;i++){
			var count = dataList[i];
			staffCount+=count;
			var tr = "<tr><td>"+orgNameList[i]+"</td><td>"+count+"</td></tr>";		
			tr = $(tr);
			tr.appendTo($("#reportDetailTable"));
		}
	}
	$("#countDetailSpan").html("<span>"+curType+"总数：<em> "+orgCount+" </em></span><span>"+type+"总数：<em> "+staffCount+" </em></span>");
}
function getCurUserReportInfo(){
	$("#chainLi").show();
	$("#sameCompareLi").show();
	var curDate = new Date();
	var year=curDate.getFullYear();
	var month=curDate.getMonth();
	var spanMonth = "";
	if(month < 10){
		if(month<1){
			year = year - 1;
			month = 12;
			spanMonth = month;
		}else{
			spanMonth = "0"+month;
		}
	}else{
		spanMonth = month;
	}
	curYear = year;
	curMonth = month;
	$("#DateTimeSpan").text(year+"年"+spanMonth+"月");
	var currentMonth1 = parseInt(curMonth) +1;
		var currentYear1 = curYear;
		if(currentMonth1 > 12){
			currentMonth1 = 1;
			currentYear1 = currentYear1 + 1;
		}
	//默认获取当前用户组织架构下工单数报表数据
	beginTime = curYear+"-"+curMonth+"-01 00:00:00";
	endTime = currentYear1+"-"+currentMonth1+"-01 00:00:00";
	getUserOrgWorkOrderCountReport(beginTime,endTime);
}
/*日期格式化*/
function dateFormat(year,month){
	if(month<1){
		year = year-1;
		month = 12;
	}
	var  day = new Date(year,month,0); 
	year = day.getFullYear();
	month = day.getMonth()+1;
	var date = new Array();
	date[0] = year;
	date[1] = month;
	return date;
}
/**按开始时间结束时间获取用户报表**/
function getUserOrgWorkOrderCountReport(beginTime,endTime){
	$.ajax({ 
	   type : "post", 
	   url : "getUserOrgWorkOrderCountReportAction", 
	   data : {beginTime:beginTime,endTime:endTime},
	   async : true,
	   dataType:'json', 
	   success : function($data){ 
	   		var res = $data;
	   		if(res){
		   		var reportMap = [res.reportMap];
		   		var reportType = res.reportType;
		   		var orgName = res.orgName;
		   		if(reportType=='org'){
		   			var orgNameList = res.orgNameList;
		   			var orgIdList = res.orgIdList;
		   			columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）',"orgWorkOrderCountDiv",orgNameList,reportMap,orgIdList);
		   			columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）',"ireportDiv",orgNameList,reportMap,orgIdList);
		   		}else if(reportType=='human'){
		   			var staffNameList = res.staffNameList;
		   			columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'','任务数（张）',"orgWorkOrderCountDiv",staffNameList,reportMap,null);
		   			columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'','任务数（张）',"ireportDiv",staffNameList,reportMap,null);
		   		}
		   		loadWorkOrderCountTableData(res,reportType);
		   		var orgId = res.userOrgId;
		   		curOrgId = orgId;
		   		userOrgId = orgId;
		   		getReportComment("orgWorkOrderCount", "organization", curOrgId, beginTime);
	   		}
		} 
	});
}
/* 重写柱状图 */
var columnBasicChart;	
//柱状图(对比)
function columnBasic2(title,subtitle,yAxisTitle,divId,categories,series,resourceIdList){
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
                        	if(!resourceIdList){
                        		return false;
                        	}
                        	var resourceId = resourceIdList[this.x];
                        	getNextOrgReportInfo(resourceId);
                        }
                    }
                }
            }
        },
        series: series
    });
}

/* 获取下级组织架构报表信息 */
function getNextOrgReportInfo(orgId){
	$("#statements_back").show();
	$.ajax({ 
	   type : "post", 
	   url : "getNextOrgWorkOrderCountReportByOrgIdAction", 
	   data : {beginTime:beginTime,endTime:endTime,orgId:orgId},
	   async : true,
	   dataType:'json', 
	   success : function($data){ 
	   		var res = $data;
	   		if(res){
		   		var reportMap = [res.reportMap];
		   		var reportType = res.reportType;
		   		var orgName = res.orgName;
		   		if(reportType=='org'){
		   			var orgNameList = res.orgNameList;
		   			var orgIdList = res.orgIdList;
		   			columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）',"orgWorkOrderCountDiv",orgNameList,reportMap,orgIdList);
		   			columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）',"ireportDiv",orgNameList,reportMap,orgIdList);
		   		}else if(reportType=='human'){
		   			var staffNameList = res.staffNameList;
		   			columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'','任务数（张）',"orgWorkOrderCountDiv",staffNameList,reportMap,null);
		   			columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）',"ireportDiv",orgNameList,reportMap,orgIdList);
		   		}
		   		loadWorkOrderCountTableData(res,reportType);
	   		}
		} 
	});
	curOrgId = orgId;
	getReportComment("orgWorkOrderCount", "organization", curOrgId, beginTime);
}
function clickGetUrgentRepairworkorderReport(){
	if($("#chainLi").attr("class") == "selected"){
		getReportComment("orgWorkOrderCountChain", "organization", curOrgId, beginTime);
	}else if($("#compareLi").attr("class") == "selected"){
		getReportComment("orgWorkOrderCount", "organization", curOrgId, beginTime);
	}
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
	var dates= new Array(); //定义一数组;
	dates[0] = year;
   dates[1] = month;
   return dates;
}

//获取上一个月的故障抢修工单数量
function getLastMonth(){
	var year = curYear;
	var month = curMonth;
	year = parseInt(year);
	month = parseInt(month);
	month = month - 1;
	var dates= new Array(); //定义一数组;
	dates = getYearMonth(year, month);
	curYear = dates[0];
	curMonth = dates[1];
	var spanMonth = "";
	if(dates[1] < 10){
		spanMonth = "0"+dates[1];
	}else{
		spanMonth = dates[1];
	}
	$("#DateTimeSpan").text(curYear+"年"+spanMonth+"月");
	//获取上个月报表数据
	var currentMonth1 = parseInt(curMonth) +1;
		var currentYear1 = curYear;
		if(currentMonth1 > 12){
			currentMonth1 = 1;
			currentYear1 = currentYear1 + 1;
		}
	beginTime = curYear+"-"+curMonth+"-01 00:00:00";
	endTime = currentYear1+"-"+currentMonth1+"-01 00:00:00";
	if($("#projectLi").attr("class") == "ontab"){
		getOrgProjectReportInfo();
	}else if($("#chainLi").attr("class") == "selected"){
		getUserOrgChainReport();
	}else{
		getUserOrgWorkOrderCountReport(beginTime,endTime);
	}
}

//获取下一个月的故障抢修工单数量
function getNextMonth(){
	var year = curYear;
	var month = curMonth;
	year = parseInt(year);
	month = parseInt(month);
	month = month + 1;
	var dates= new Array(); //定义一数组;
	dates = getYearMonth(year, month);
	curYear = dates[0];
	curMonth = dates[1];
	var spanMonth = "";
	if(dates[1] < 10){
		spanMonth = "0"+dates[1];
	}else{
		spanMonth = dates[1];
	}
	$("#DateTimeSpan").text(curYear+"年"+spanMonth+"月");
	//获取下个月报表数据
	var currentMonth1 = parseInt(curMonth) +1;
		var currentYear1 = curYear;
		if(currentMonth1 > 12){
			currentMonth1 = 1;
			currentYear1 = currentYear1 + 1;
		}
	beginTime = curYear+"-"+curMonth+"-01 00:00:00";
	endTime = currentYear1+"-"+currentMonth1+"-01 00:00:00";
	if($("#projectLi").attr("class") == "ontab"){
		getOrgProjectReportInfo();
	}else if($("#chainLi").attr("class") == "selected"){
		getUserOrgChainReport();
	}else{
		getUserOrgWorkOrderCountReport(beginTime,endTime);
	}
}
//获取组织下项目报表信息
function getOrgProjectReportInfo(){
	$("#compareTab li").each(function(k){
		var clz = "";
		if(0==k){
			clz = "selected";
		}
		$(this).attr("class",clz);
	})
	$("#statements_back").hide();
	$("#chainLi").hide();
	$("#sameCompareLi").hide();
	$.ajax({ 
	   type : "post", 
	   url : "getOrgProjectWorkOrderCountReportAction", 
	   data : {beginTime:beginTime,endTime:endTime,orgId:userOrgId},
	   async : true,
	   dataType:'json', 
	   success : function($data){ 
	   		var res = $data;
	   		if(res){
		   		var reportMap = [res.reportMap];
		   		var orgName = res.orgName;
		   		var orgNameList = res.orgNameList;
		   		var orgIdList = res.orgIdList;
		   		columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'','工单数（张）',"orgWorkOrderCountDiv",orgNameList,reportMap,null);
		   		columnBasic2("人员任务量统计("+orgName+","+curYear+"年"+curMonth+"月)",'','工单数（张）',"ireportDiv",orgNameList,reportMap,orgIdList);
	   			loadWorkOrderCountTableData(res,'project');
	   		}
		} 
	});
	getReportComment("orgProjectWorkOrderCount", "organization", userOrgId, beginTime);
}
function addDivChar(){
  	var report_statements_full = document.getElementById("report_statements_full_newNode");
//	window.top.document.body.removeChild(report_statements_full); 
	if(report_statements_full != null){
	}else{
		var chartext = $("#ireportDiv").html();
		var newNode = document.createElement("div");
		newNode.id = "report_statements_full_newNode";
		var context ="<div id='report_statements_full' style='display: block; z-index: 2000; padding:20px; margin-left: -500px; top: 100px; left: 50%; background-color:#333; border-radius: 15px; position: absolute;'>"
							+"<div class='statements_full_info' id='report_statements_full_info' style='background-color:#fff; width:1000px; height:500px;'>"
							+chartext
						+"</div>"
						+"<div onclick='window.top.document.body.removeChild(window.top.document.getElementById(\"report_statements_full_newNode\"));' class='statements_full_close' style='background-color: #333; border-radius: 16px; color: #fff; font-family: Helvetica,STHeiti; font-size: 34px; height: 32px; line-height: 32px; position: absolute; right: -10px; text-align: center; top: -10px; width: 32px; cursor:pointer;'>×</div>"
					+"</div>";
		newNode.innerHTML = context;
		window.top.document.body.appendChild(newNode);
	}
}

