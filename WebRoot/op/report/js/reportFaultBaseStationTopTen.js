
var currentDate;
var currentYear = null;
var currentMonth = null;
var beginTime = null;
var endTime = null;

var show_orgId;

var dataOfLarge = {};

var topOrgName;

$(function(){

	currentDate = new Date();
	currentYear=currentDate.getFullYear();
	currentMonth=currentDate.getMonth();
	var spanMonth = "";
	if(currentMonth < 10){
		//spanMonth = "0"+currentMonth;
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
	endTime = currentYear+"-"+currentMonth1+"-01 00:00:00";
	
	
	//显示组织树
	searchProviderOrgTree();
	searchProjectTree();
	
	getHorizontalCompareReportData();
	
	
	//按组织显示报表
	$("#reportByOrg").click(function(){
		getHorizontalCompareReportData();
	});
	
	//按项目显示报表
	$("#reportByProject").click(function(){
		getHorizontalCompareReportDataForProject();
	});
});


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
		$.each(tab_li,function(i,n){
			var className=$(n).attr("class");
			if(className=="ontab"){
				var li_id=$(n).attr("id");
				if(li_id=="reportByOrg"){
					getHorizontalCompareReportData();
				}else if(li_id=="reportByArea"){
				
				}else if(li_id=="reportByProject"){
					getHorizontalCompareReportDataForProject();
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
		var currentMonth1 = parseInt(currentMonth) +1;
		var currentYear1 = currentYear;
		if(currentMonth1 > 12){
			currentMonth1 = 1;
			currentYear1 = currentYear1 + 1;
		}
		endTime = currentYear+"-"+currentMonth1+"-01 00:00:00";
		
		var tab_li=$("#typeTabDiv li");
		$.each(tab_li,function(i,n){
			var className=$(n).attr("class");
			if(className=="ontab"){
				var li_id=$(n).attr("id");
				if(li_id=="reportByOrg"){
					getHorizontalCompareReportData();
				}else if(li_id=="reportByArea"){
				
				}else if(li_id=="reportByProject"){
					getHorizontalCompareReportDataForProject();
				}
			}
		});
	},300)
	
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


//获取基站数前10的对比周期的数据
function getHorizontalCompareReportData(){
	var param={"beginTime":beginTime,"endTime":endTime};
	$.ajax({
		"url" : "getFaultBaseStationTopTenDataAction" , 
		"type" : "post" , 
		"data":param,
		"success" : function ( data ) {
			//var jsonData = eval( "(" + data + ")" );
			var jsonData = data;
			var xAxis=jsonData.xAxis;
			var yAxis=jsonData.yAxis;
			var orgName=jsonData.orgName;
			topOrgName=jsonData.orgName;
			//alert("xAxis=="+xAxis);
			show_orgId=jsonData.topOrgId;
			/*
			var arr=[];
			var obj={};
			obj.name="故障数";
			obj.data=yAxis;
			arr.push(obj);

			var arr=[{
               name: 'Tokyo',
               data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
   
           	}];*/
           	
           	var title="故障数top-10最差基站排名（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）";
           	var subTitle="";
           	var yAxisTitle="故障数 （个）";
           	dataOfLarge.title=title;
          	dataOfLarge.yAxisTitle=yAxisTitle;
          	dataOfLarge.xAxis=xAxis;
          	dataOfLarge.yAxis=yAxis;
           	
           	getReportComment("reportFaultBaseStationTopTen", "org", show_orgId, null);
			columnBasicFault(title,subTitle,yAxisTitle,"resultReport",xAxis,yAxis);
		}
	});
};

//按项目获取对比周期的数据
function getHorizontalCompareReportDataForProject(){
	var param={"beginTime":beginTime,"endTime":endTime};	
	$.ajax({
		"url" : "getFaultBaseStationTopTenDataForProjectAction" , 
		"type" : "post" , 
		"data":param,
		"success" : function ( data ) {
			//var jsonData = eval( "(" + data + ")" );
			var jsonData = data;
			var xAxis=jsonData.xAxis;
			var yAxis=jsonData.yAxis;
			var orgName=jsonData.topOrgName;
			//alert("xAxis=="+xAxis);
			show_orgId=jsonData.topOrgId;
           	var title="故障数top-10最差基站排名（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）";
           	var subTitle="";
           	var yAxisTitle="故障数 （个）";
           	dataOfLarge.title=title;
          	dataOfLarge.yAxisTitle=yAxisTitle;
          	dataOfLarge.xAxis=xAxis;
          	dataOfLarge.yAxis=yAxis;
           	
           	getReportComment("reportFaultBaseStationTopTen", "org", show_orgId, null);
			columnBasicFault(title,subTitle,yAxisTitle,"resultReport",xAxis,yAxis);
		}
	});
}


//生成组织架构树
function searchProviderOrgTree(){
	var orgId = "16";
	var orgName="";
	$.ajax({
		"url" : "cardispatchForeign_ajax!getLoginUserBiz.action" , 
		"type" : "post" , 
		"success" : function ( data ) {
			data = eval( "(" + data + ")" );
			orgId = data.orgId;
			orgName=data.name;
			if(orgId==null||orgId==""){
				orgId="16";
			}
			if(!orgName || orgName=="" || orgName=="undefined"){
				orgName="";
			}
			
			$("#searchOrgName").val(orgName);
			
			var values = {"orgId":orgId}
			var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			$.post(myUrl,values,function(data){
				createOrgTreeOpenFirstNode(data,"treeDiv","search_org_div","a","searchOrgTreeClick");
			},"json");
		}
	});
}

//选择组织
function searchOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	//alert("orgId=="+orgId);
	$("#searchOrgName").val(data.name);
	$("#searchOrgId").val(data.orgId);
	$("#treeDiv").slideUp("fast");
	
	var param={"beginTime":beginTime,"endTime":endTime};
	param.orgId=orgId
	
	$.ajax({
		"url" : "getFaultBaseStationTopTenDataByOrgAction" , 
		"type" : "post" ,
		"data":param,
		"success" : function ( data ) {
			var jsonData = data;
			var xAxis=jsonData.xAxis;
			var yAxis=jsonData.yAxis;
			var orgName=jsonData.orgName;
			show_orgId=jsonData.orgId;
			
			if(!orgName || orgName=="" || orgName=="undefined"){
				orgName="";
			}
			
			$("#searchOrgName").val(orgName);
			
			var title="故障数top-10最差基站排名（"+orgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）";
           	var subTitle="";
           	var yAxisTitle="故障数 （个）";
           	dataOfLarge.title=title;
          	dataOfLarge.yAxisTitle=yAxisTitle;
          	dataOfLarge.xAxis=xAxis;
          	dataOfLarge.yAxis=yAxis;
			
			getReportComment("reportFaultBaseStationTopTen", "org", show_orgId, null);
			columnBasicFault(title,subTitle,yAxisTitle,"resultReport",xAxis,yAxis);
		}
	});
}


//生成项目树
function searchProjectTree(){
	var myUrl="getProjectTreeDataAction";
	var values = {};
	$.post(myUrl,values,function(data){
		createOrgTreeOpenFirstNode(data,"treeDiv2","search_project_div","a","searchProjectTreeClick");
		$("#searchProjectId").val(data[0].orgId);
		$("#searchProjectName").val(data[0].orgName);
	},"json");
}

//选择项目
function searchProjectTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	//alert("orgId=="+orgId);
	$("#searchProjectName").val(data.name);
	$("#searchProjectId").val(data.orgId);
	$("#treeDiv2").slideUp("fast");
	
	var param={"beginTime":beginTime,"endTime":endTime};
	param.projectId=$("#searchProjectId").val();
	
	$.ajax({
		"url" : "getFaultBaseStationTopTenDataByProjectAction" , 
		"type" : "post" ,
		"data":param,
		"success" : function ( data ) {
			var jsonData = data;
			var xAxis=jsonData.xAxis;
			var yAxis=jsonData.yAxis;
			show_orgId=jsonData.orgId;
			/*
			var projectName=jsonData.projectName;
			if(!projectName || projectName=="" || projectName=="undefined"){
				projectName="";
			}
			$("#searchOrgName").val(projectName);*/
			
			var title="故障数top-10最差基站排名（"+topOrgName+"，"+currentYear+"年"+formatMonth(currentMonth)+"月）";
           	var subTitle="";
           	var yAxisTitle="故障数 （个）";
           	dataOfLarge.title=title;
          	dataOfLarge.yAxisTitle=yAxisTitle;
          	dataOfLarge.xAxis=xAxis;
          	dataOfLarge.yAxis=yAxis;
			
			getReportComment("reportFaultBaseStationTopTen", "org", show_orgId, null);
			columnBasicFault(title,subTitle,yAxisTitle,"resultReport",xAxis,yAxis);
		}
	});
}




//监听提交评论按钮事件
function clickGetUrgentRepairworkorderReport(){
	getReportComment("reportFaultBaseStationTopTen", "org", show_orgId, null);
}

           
var columnBasicChart;	
//柱状图(对比)
function columnBasicFault(title,subtitle,yAxisTitle,divId,categories,series){
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
                          	
                          }
                      }
                  }
              }
          },
          series: series
      });
}



function addDiv(divId){
	columnBasicFault(dataOfLarge.title,"",dataOfLarge.yAxisTitle,divId,dataOfLarge.xAxis,dataOfLarge.yAxis);
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
