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
	 $(".tab_menu ul li").each(function(index){
		$(this).click(function(){
			$(".tab_menu ul li").removeClass("ontab");
			$(this).addClass("ontab");
		})
	});
	//默认获取用户报表数据
	getCurUserReportInfo();
	$("#classifyCheckBox").click(function(){
		if($(this).attr("checked")=="checked"){
			$(".statements_classify_info").show();
			getUserOrgStaffSkillReportInfo();
		}else{
			$(".statements_classify_info").hide();
			getNextOrgReportInfo(curOrgId);
		}
	});
	//对比点击事件
	$("#compareLi").click(function(){
		$(".statements_select").css("top","151.5px");
		$(".statements_classify").show();
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
			getUserOrgStaffCountReport(beginTime,endTime);
		}
	});
	//同比点击事件
	$("#sameCompareLi").click(function(){
		$(".statements_classify").hide();
		$(".st_full_screen").hide();
		$("#statements_back").hide();	
		$("#reportDetailTable").html("");
		$("#countDetailSpan").html("");
		$("#orgStaffCountDiv").html("暂无数据");
		getReportComment("orgStaffCountSameCompare", "organization", curOrgId, beginTime);
	});
	//环比点击事件
	$("#chainLi").click(function(){
		$(".statements_classify").hide();
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
//获取用户组织人员数环比统计
function getUserOrgChainReport(){
	$(".statements_select").css("top","99px");
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
	   url : "getOrgStaffCountChainReportAction", 
	   data : param,
	   async : true,
	   dataType:'json', 
	   success : function($data){ 
	   		var res = $data;
	   		if(res){
	   			var reportMap = [res.reportMap];
		   		var nameList = res.nameList;
	   			var orgId = res.orgId;
	   			var orgName = res.orgName;
	   			lineBasic("人员数环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","人员数（个）","orgStaffCountDiv",nameList,reportMap);
	   			lineBasic("人员数环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","人员数（个）","ireportDiv",nameList,reportMap);
	   			curOrgId = orgId;
	   		}
   			getReportComment("orgStaffCountChain", "organization", curOrgId, beginTime);
   		} 
	});
}
/*获取用户组织架构下人员技能统计*/
function getUserOrgStaffSkillReportInfo(){
	$("#statements_back").hide();	
	$.ajax({ 
	   type : "post", 
	   url : "getOrgStaffSkillReportAction", 
	   data : {beginTime:beginTime,endTime:endTime,orgId:curOrgId},
	   async : true,
	   dataType:'json', 
	   success : function($data){ 
	   		var res = $data;
	   		if(res){
	   			var pieJson = eval(res.pieJson);
	   			var orgName = res.orgName;
		   		pieBasic2("人员技能分类统计("+orgName+","+curYear+"年"+curMonth+"月)","","orgStaffCountDiv",pieJson);
	   			pieBasic2("人员技能分类统计("+orgName+","+curYear+"年"+curMonth+"月)","","ireportDiv",pieJson);
	   			curOrgId = res.userOrgId;
	   			getReportComment("orgStaffCount", "organization", curOrgId, beginTime);
	   			loadOrgStaffSkillTableData(res);
	   		}
		} 
	});
}
/*人员技能统计分类Table*/
function loadOrgStaffSkillTableData(data){
	$("#reportDetailTable").html("");
	$("#countDetailSpan").html("");
	if(!data){
		return false;
	}
	var totalCount = 0;
	var staffCount = 0;
	var th = $("<tr><th>技能名称</th><th>人员数量</th></tr>");
	th.appendTo($("#reportDetailTable"));
	var pieJson = data.pieJson;
	pieJson = eval(pieJson);
	if(pieJson){
		totalCount = pieJson.length;
		for(var i=0;i<pieJson.length;i++){
			var ps = pieJson[i];
			if(ps){
				var name = ps[0];
				var count = ps[1];
				var tr = "<tr><td>"+name+"</td><td>"+count+"</td></tr>";		
				tr = $(tr);
				tr.appendTo($("#reportDetailTable"));
				staffCount += count;
			}
		}
	}
	$("#countDetailSpan").html("<span>技能总数：<em> "+totalCount+" </em></span><span>人员总数：<em> "+staffCount+" </em></span>");
	
}
/*获取用户报表信息（对比）*/
function getCurUserReportInfo(){
	$("#chainLi").show();
	$("#sameCompareLi").show();
	$("#classifyCheckBox").removeAttr("checked");
	$(".statements_classify_info").hide();
	$(".statements_classify").show();
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
	getUserOrgStaffCountReport(beginTime,endTime);
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
function getUserOrgStaffCountReport(beginTime,endTime){
	$("#compareTab li").each(function(k){
		var clz = "";
		if(0==k){
			clz = "selected";
		}
		$(this).attr("class",clz);
	})
	$.ajax({ 
	   type : "post", 
	   url : "getUserOrgStaffCountReportAction", 
	   data : {beginTime:beginTime,endTime:endTime},
	   async : true,
	   dataType:'json', 
	   success : function($data){ 
	   		var res = $data;
	   		if(res){
		   		var reportMap = [res.reportMap];
		   		var orgNameList = res.orgNameList;
	   			var orgIdList = res.orgIdList;
	   			var canClick = res.canClick;
	   			var orgId = res.userOrgId;
	   			var orgName = res.orgName;
	   			if(canClick){
	   				columnBasic2("人员数统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','人员数（个）',"orgStaffCountDiv",orgNameList,reportMap,orgIdList);
	   				columnBasic2("人员数统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','人员数（个）',"ireportDiv",orgNameList,reportMap,orgIdList);
	   			}else{
	   				columnBasic2("人员数统计("+orgName+","+curYear+"年"+curMonth+"月)",'','人员数（个）',"orgStaffCountDiv",orgNameList,reportMap,null);
	   				columnBasic2("人员数统计("+orgName+","+curYear+"年"+curMonth+"月)",'','人员数（个）',"ireportDiv",orgNameList,reportMap,null);
	   			}
	   			curOrgId = orgId;
	   			userOrgId = orgId;
	   			loadStaffCountTableData(res);
	   			getReportComment("orgStaffCount", "organization", curOrgId, beginTime);
	   		}
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
		   url : "getUpOrgStaffCountChainReportAction", 
		   data : param,
		   async : false,
		   dataType:'json', 
		   success : function($data){ 
		   		var res = $data;
		   		if(res){
		   			var reportMap = [res.reportMap];
			   		var nameList = res.nameList;
		   			var orgId = res.orgId;
		   			var orgName = res.orgName;
		   			lineBasic("人员数环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","人员数（个）","orgStaffCountDiv",nameList,reportMap);
		   			lineBasic("人员数环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","人员数（个）","ireportDiv",nameList,reportMap);
		   			curOrgId = orgId;
		   		}
	   			getReportComment("orgStaffCountChain", "organization", curOrgId, beginTime);
	   		} 
		});
	}else if($("#compareLi").attr("class") == "selected"){
		$.ajax({ 
		   type : "post", 
		   url : "getUpOrgStaffCountReportByOrgIdAction", 
		   data : {beginTime:beginTime,endTime:endTime,orgId:curOrgId},
		   async : false,
		   dataType:'json', 
		   success : function($data){ 
		   		var res = $data;
		   		if(res){
			   		var reportMap = [res.reportMap];
			   		var orgNameList = res.orgNameList;
		   			var orgIdList = res.orgIdList;
		   			var canClick = res.canClick;
		   			var orgId = res.userOrgId;
		   			var orgName = res.orgName;
		   			if(canClick){
		   				columnBasic2("人员数统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','人员数（个）',"orgStaffCountDiv",orgNameList,reportMap,orgIdList);
		   				columnBasic2("人员数统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','人员数（个）',"ireportDiv",orgNameList,reportMap,orgIdList);
		   			}else{
		   				columnBasic2("人员数统计("+orgName+","+curYear+"年"+curMonth+"月)",'','人员数（个）',"orgStaffCountDiv",orgNameList,reportMap,null);
		   				columnBasic2("人员数统计("+orgName+","+curYear+"年"+curMonth+"月)",'','人员数（个）',"ireportDiv",orgNameList,reportMap,null);
		   			}
		   			curOrgId = orgId;
		   			loadStaffCountTableData(res);
		   		}
	   		} 
		});
		getReportComment("orgStaffCount", "organization", curOrgId, beginTime);
	}
	if(curOrgId == userOrgId){
		$("#statements_back").hide();
	}
}
/*加载报表表格数据*/
function loadStaffCountTableData(data){
	$("#reportDetailTable").html("");
	$("#countDetailSpan").html("");
	if(!data){
		return false;
	}
	var orgCount = 0;
	var staffCount = 0;
	var th = $("<tr><th>组织名称</th><th>人员数量</th></tr>");
	th.appendTo($("#reportDetailTable"));
	var orgNameList = data.orgNameList;
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
	$("#countDetailSpan").html("<span>组织总数：<em> "+orgCount+" </em></span><span>人员总数：<em> "+staffCount+" </em></span>");
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
	$("#statements_back").hide();	
	$.ajax({ 
	   type : "post", 
	   url : "getNextOrgStaffCountReportByOrgIdAction", 
	   data : {beginTime:beginTime,endTime:endTime,orgId:orgId},
	   async : true,
	   dataType:'json', 
	   success : function($data){ 
	   		var res = $data;
	   		if(res){
		   		var reportMap = [res.reportMap];
		   		var orgNameList = res.orgNameList;
	   			var orgIdList = res.orgIdList;
	   			var canClick = res.canClick;
	   			var orgName = res.orgName;
	   			curOrgId = orgId;
	   			if(curOrgId!=userOrgId){
   					$("#statements_back").show();	
   				}
	   			if(canClick){
	   				columnBasic2("人员数统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','人员数（个）',"orgStaffCountDiv",orgNameList,reportMap,orgIdList);
	   				columnBasic2("人员数统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','人员数（个）',"ireportDiv",orgNameList,reportMap,orgIdList);
	   			}else{
	   				return false;
	   			}
	   			loadStaffCountTableData(res);
	   		}
		} 
	});
	getReportComment("orgStaffCount", "organization", curOrgId, beginTime);
}
function clickGetUrgentRepairworkorderReport(){
	if($("#chainLi").attr("class") == "selected"){
		getReportComment("orgStaffCountChain", "organization", curOrgId, beginTime);
	}else if($("#projectLi").attr("class") == "ontab"){
		getReportComment("orgProjectWorkOrderCount", "organization", userOrgId, beginTime);
	}else if($("#compareLi").attr("class") == "selected"){
		getReportComment("orgStaffCount", "organization", curOrgId, beginTime);
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
	if($("#classifyCheckBox").attr("checked")=="checked"){
		getUserOrgStaffSkillReportInfo();		
	}else if($("#projectLi").attr("class") == "ontab"){
		getOrgProjectReportInfo();
	}else if($("#chainLi").attr("class") == "selected"){
		getUserOrgChainReport();
	}else{
		getUserOrgStaffCountReport(beginTime,endTime);
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
	if($("#classifyCheckBox").attr("checked")=="checked"){
		getUserOrgStaffSkillReportInfo();		
	}else if($("#projectLi").attr("class") == "ontab"){
		getOrgProjectReportInfo();
	}else if($("#chainLi").attr("class") == "selected"){
		getUserOrgChainReport();
	}else{
		getUserOrgStaffCountReport(beginTime,endTime);
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
	$(".statements_classify").hide();
	$.ajax({ 
	   type : "post", 
	   url : "getOrgProjectStaffCountReportAction", 
	   data : {beginTime:beginTime,endTime:endTime,orgId:userOrgId},
	   async : true,
	   dataType:'json', 
	   success : function($data){ 
	   		var res = $data;
	   		if(res){
	   			var reportMap = [res.reportMap];
		   		var orgNameList = res.orgNameList;
	   			var orgIdList = res.orgIdList;
	   			var orgName = res.orgName;
   				columnBasic2("人员数统计("+orgName+","+curYear+"年"+curMonth+"月)",'','人员数（个）',"orgStaffCountDiv",orgNameList,reportMap,null);
   				columnBasic2("人员数统计("+orgName+","+curYear+"年"+curMonth+"月)",'','人员数（个）',"ireportDiv",orgNameList,reportMap,null);
	   			loadStaffCountTableData(res);
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
