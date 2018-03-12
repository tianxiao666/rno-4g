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
				$(this).attr("class",clz);
			})
		})
	});
	//默认获取用户报表数据
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
			getUserOrgReport(beginTime,endTime);
		}
	});
	//同比点击事件
	$("#sameCompareLi").click(function(){
		$("#reportDetailTable").html("");
		$("#countDetailSpan").html("");
		$(".st_full_screen").hide();
		$("#statements_back").hide();	
		$("#orgReportDiv").html("暂无数据");
		getReportComment("orgSameCompare", "organization", curOrgId, beginTime);
	});
	//环比点击事件
	$("#chainLi").click(function(){
		getOrgCompareCountChainReport();
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
function getOrgCompareCountChainReport(){
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
	   url : "getOrgCompareChainReportAction", 
	   data : param,
	   async : false,
	   dataType:'json', 
	   success : function($data){ 
	   		var res = $data;
	   		if(res){
	   			var reportMap = [res.reportMap];
	   			var sMap = res.sMap;
	   			var wMap = res.wMap;
	   			var reportData = [sMap,wMap];
	   			var orgName = res.orgName;
		   		var nameList = res.nameList;
	   			var orgId = res.userOrgId;
	   			columnAndSplineBasic2("人员及任务数环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","工单数（张）","人员数（个）","orgReportDiv",nameList,reportData);
	   			columnAndSplineBasic2("人员及任务数环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","工单数（张）","人员数（个）","ireportDiv",nameList,reportData);
	   			curOrgId = orgId;
	   		}
	   		loadChainReportDetailTable(res);
   		} 
	});
	getReportComment("orgCompareCountChain", "organization", curOrgId, beginTime);
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
		   url : "getUpOrgCompareChainReportAction", 
		   data : param,
		   async : false,
		   dataType:'json', 
		   success : function($data){ 
		   		var res = $data;
		   		if(res){
		   			var reportMap = [res.reportMap];
		   			var sMap = res.sMap;
		   			var wMap = res.wMap;
		   			var reportData = [sMap,wMap];
		   			var orgName = res.orgName;
			   		var nameList = res.nameList;
		   			var orgId = res.userOrgId;
		   			columnAndSplineBasic2("人员及任务数环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","工单数（张）","人员数（个）","orgReportDiv",nameList,reportData);
		   			columnAndSplineBasic2("人员及任务数环比统计("+orgName+","+curYear+"年"+curMonth+"月)","","工单数（张）","人员数（个）","ireportDiv",nameList,reportData);
		   			curOrgId = orgId;
		   		}
		   		loadChainReportDetailTable(res);
	   		} 
		});
		getReportComment("orgCompareCountChain", "organization", curOrgId, beginTime);
	}else if($("#compareLi").attr("class") == "selected"){
		$.ajax({ 
		   type : "post", 
		   url : "getUpOrgCompareReportByOrgIdAction", 
		   data : {beginTime:beginTime,endTime:endTime,orgId:curOrgId},
		   async : false,
		   dataType:'json', 
		   success : function($data){ 
		   		var res = $data;
		   		if(res){
		   			var sMap = res.sMap;
			   		var wMap = res.wMap;
			   		var reportData = [sMap,wMap];
			   		var nameList = res.nameList;
		   			var idList = res.idList;
		   			var orgName = res.orgName;
		   			if(!orgName){orgName="";}
		   			var reportType = res.reportType;
		   			if(reportType=='org'){
		   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）','人员数（个）',"orgReportDiv",nameList,reportData,idList);
		   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）','人员数（个）',"ireportDiv",nameList,reportData,idList);
		   			}else{
		   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）','人员数（个）',"orgReportDiv",nameList,reportData,null);
		   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）','人员数（个）',"ireportDiv",nameList,reportData,null);
		   			}
		   			var orgId = res.userOrgId;
		   			curOrgId = orgId;
		   		}
		   		loadReportDetailTable(res);
			} 
		});
		getReportComment("orgCompareCount", "organization", curOrgId, beginTime);
	}
	if(curOrgId == userOrgId){
		$("#statements_back").hide();
	}
}
/*加载报表详情Table*/
function loadReportDetailTable(data){
	$("#reportDetailTable").html("");
	$("#countDetailSpan").html("");
	if(!data){
		return false;
	}
	var nameList = data.nameList;
	var sMap = data.sMap;
	var wMap = data.wMap;
	var reportType=data.reportType;
	var name = "组织";
	if(reportType=='org'){
		reportType = "工单";
	}else if(reportType=='project'){
		name = "项目";
		reportType = "工单";
	}else{
		reportType = "任务单";
		name = "人员";
	}
	var orgCount = 0;
	var staffCount = 0;
	var woStaffCount = 0;
	
	var th = $("<tr><th>"+name+"名称</th><th>人员数量</th><th>"+reportType+"数量</th></tr>");
	th.appendTo($("#reportDetailTable"));
	if(nameList){
		var staffDataList = sMap.data;
		orgCount = nameList.length;
		
		var woDataList = wMap.data;
		woCount = woDataList.length;
		
		for(var i=0;i<nameList.length;i++){
			var wc = woDataList[i];
			if(!wc){wc=0;}
			woStaffCount += wc;
			
			var sc = staffDataList[i];
			if(!sc){sc=0;}
			staffCount += sc;
			
			
			var tr = "<tr><td>"+nameList[i]+"</td><td>"+sc+"</td><td>"+wc+"</td></tr>";		
			tr = $(tr);
			tr.appendTo($("#reportDetailTable"));
		}
	}
	if(!staffCount){staffCount = orgCount;}
	var ht = "<span>"+name+"总数：<em> "+orgCount+" </em></span><span>人员总数：<em> "+staffCount+" </em></span>";
	ht += "<span>"+reportType+"总数：<em> "+woStaffCount+" </em></span>";
	$("#countDetailSpan").html(ht);
}
/*加载报表详情Table*/
function loadChainReportDetailTable(data){
	$("#reportDetailTable").html("");
	$("#countDetailSpan").html("");
	if(!data){
		return false;
	}
	var nameList = data.nameList;
	var sMap = data.sMap;
	var wMap = data.wMap;
	
	var reportType=data.reportType;
	if(reportType=='org'){
		reportType = "工单";
	}else{
		reportType = "任务单";
	}
	
	var orgCount = 0;
	var staffCount = 0;
	var woStaffCount = 0;
	
	var th = $("<tr><th>日期</th><th>人员数量</th><th>"+reportType+"数量</th></tr>");
	th.appendTo($("#reportDetailTable"));
	if(nameList){
		var staffDataList = sMap.data;
		orgCount = nameList.length;
		
		var woDataList = wMap.data;
		woCount = woDataList.length;
		
		for(var i=0;i<nameList.length;i++){
			var wc = woDataList[i];
			if(!wc){wc=0;}
			woStaffCount += wc;
			
			var sc = staffDataList[i];
			if(!sc){sc=0;}
			staffCount += sc;
			
			
			var tr = "<tr><td>"+nameList[i]+"</td><td>"+sc+"</td><td>"+wc+"</td></tr>";		
			tr = $(tr);
			tr.appendTo($("#reportDetailTable"));
		}
	}
	if(!staffCount){staffCount = woCount;}
	var ht = "<span>环比月份总数：<em> "+orgCount+" </em></span><span>人员总数：<em> "+staffCount+" </em></span>";
	ht += "<span>"+reportType+"总数：<em> "+woStaffCount+" </em></span>";
	$("#countDetailSpan").html(ht);
}
function getCurUserReportInfo(){
	$("#chainLi").show();
	$("#sameCompareLi").show();
	$("#statements_back").hide();
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
	getUserOrgReport(beginTime,endTime);
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
function getUserOrgReport(beginTime,endTime){
	$.ajax({ 
	   type : "post", 
	   url : "getUserOrgCompareReportAction", 
	   data : {beginTime:beginTime,endTime:endTime},
	   async : false,
	   dataType:'json', 
	   success : function($data){ 
	   		var res = $data;
	   		if(res){
		   		var sMap = res.sMap;
		   		var wMap = res.wMap;
		   		var reportData = [sMap,wMap];
		   		var nameList = res.nameList;
	   			var idList = res.idList;
	   			var orgId = res.userOrgId;
	   			var orgName = res.orgName;
	   			if(!orgName){orgName="";}
	   			var reportType = res.reportType;
	   			if(reportType=='org'){
	   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）','人员数（个）',"orgReportDiv",nameList,reportData,idList);
	   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）','人员数（个）',"ireportDiv",nameList,reportData,idList);
	   			}else{
	   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'','工单数（张）','人员数（个）',"orgReportDiv",nameList,reportData,null);
	   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'','工单数（张）','人员数（个）',"ireportDiv",nameList,reportData,null);
	   			}
	   			curOrgId = orgId;
	   			userOrgId = orgId;
	   		}
	   		loadReportDetailTable(res);
   		} 
	});
	getReportComment("orgCompareCount", "organization", curOrgId, beginTime);
}
/* 获取下级组织架构报表信息 */
function getNextOrgReportInfo(orgId){
	$("#statements_back").show();
	$.ajax({ 
	   type : "post", 
	   url : "getNextOrgCompareReportByOrgIdAction", 
	   data : {beginTime:beginTime,endTime:endTime,orgId:orgId},
	   async : false,
	   dataType:'json', 
	   success : function($data){ 
	   		var res = $data;
	   		if(res){
	   			var sMap = res.sMap;
		   		var wMap = res.wMap;
		   		var reportData = [sMap,wMap];
		   		var nameList = res.nameList;
	   			var idList = res.idList;
	   			var orgName = res.orgName;
	   			if(!orgName){orgName="";}
	   			var reportType = res.reportType;
	   			if(reportType=='org'){
	   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）','人员数（个）',"orgReportDiv",nameList,reportData,idList);
	   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）','人员数（个）',"ireportDiv",nameList,reportData,idList);
	   			}else{
	   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）','人员数（个）',"orgReportDiv",nameList,reportData,null);
	   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'(点击图形区域，可向下钻取)','工单数（张）','人员数（个）',"ireportDiv",nameList,reportData,null);
	   			}
	   		}
	   		loadReportDetailTable(res);
		} 
	});
	curOrgId = orgId;
	getReportComment("orgCompareCount", "organization", curOrgId, beginTime);
}
function clickGetUrgentRepairworkorderReport(){
	if($("#chainLi").attr("class") == "selected"){
		getReportComment("orgCompareCountChain", "organization", curOrgId, beginTime);
	}else if($("#compareLi").attr("class") == "selected"){
		getReportComment("orgCompareCount", "organization", curOrgId, beginTime);
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
	if($("#projectLi").attr("class")=="ontab"){
		getOrgProjectReportInfo();
	}else{
		getUserOrgReport(beginTime,endTime);
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
	var currentMonth1 = parseInt(curMonth) +1;
		var currentYear1 = curYear;
		if(currentMonth1 > 12){
			currentMonth1 = 1;
			currentYear1 = currentYear1 + 1;
		}
	//获取下个月报表数据
	beginTime = curYear+"-"+curMonth+"-01 00:00:00";
	endTime = currentYear1+"-"+currentMonth1+"-01 00:00:00";
	if($("#projectLi").attr("class")=="ontab"){
		getOrgProjectReportInfo();
	}else{
		getUserOrgReport(beginTime,endTime);
	}
}

var columnAndSplineBasicChart;	
//柱状图+折线(对比)
function columnAndSplineBasic2(title,subtitle,yAxisTitle,yAxisTitle2,divId,categories,series,resourceIdList){
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
                 color:"#AA4643"
               }
           },
           title: {
               text: yAxisTitle,
               style: {
               	color:"#AA4643"
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
                       	if(!resourceIdList){
                   			return false;
                   		}
                   		var resourceId = resourceIdList[this.x];
                   		getNextOrgReportInfo(resourceId);
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
	   url : "getOrgProjectStaffAndWorderCountReportAction", 
	   data : {beginTime:beginTime,endTime:endTime,orgId:userOrgId},
	   async : false,
	   dataType:'json', 
	   success : function($data){ 
	   		var res = $data;
	   		if(res){
		   		var sMap = res.sMap;
		   		var wMap = res.wMap;
		   		var reportData = [sMap,wMap];
		   		var nameList = res.nameList;
	   			var idList = res.idList;
	   			var orgId = res.userOrgId;
	   			var orgName = res.orgName;
	   			if(!orgName){orgName="";}
	   			var reportType = res.reportType;
	   			if(reportType=='org'){
	   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'','工单数（张）','人员数（个）',"orgReportDiv",nameList,reportData,idList);
	   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'','工单数（张）','人员数（个）',"ireportDiv",nameList,reportData,idList);
	   			}else{
	   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'','工单数（张）','人员数（个）',"orgReportDiv",nameList,reportData,null);
	   				columnAndSplineBasic2("人员及任务数综合统计("+orgName+","+curYear+"年"+curMonth+"月)",'','工单数（张）','人员数（个）',"ireportDiv",nameList,reportData,null);
	   			}
	   			curOrgId = orgId;
	   		}
	   		loadReportDetailTable(res);
   		} 
	});
	getReportComment("orgProjectCompareCount", "organization", userOrgId, beginTime);
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