<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车辆里程统计</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="css/informationManager.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css"></link>
<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css"></link>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript"src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="js/util/objutil.js"></script>
<script type="text/javascript" src="js/util/dateutil.js"></script>
<script type="text/javascript" src="js/tool/loading_div.js"></script>
<script type="text/javascript" src="js/tool/tablePage.js"></script>
<script type="text/javascript" src="js/tool/new_formcheck.js"></script>

<script>

$(function(){
	var sTime = $("#sHour");
	var eTime = $("#eHour");
	
	for(var i=0;i<25;i++)
	{
		if(i==0)
		{
			$("<option value="+i+" selected>"+i+"</option>").appendTo(sTime);
			$("<option value="+i+">"+i+"</option>").appendTo(eTime);
			
		}
		else if(i==24)
		{
			$("<option value="+i+" selected>"+i+"</option>").appendTo(eTime);
			$("<option value="+i+">"+i+"</option>").appendTo(sTime);
		}
		else
		{
			$("<option value="+i+">"+i+"</option>").appendTo(sTime);
			$("<option value="+i+">"+i+"</option>").appendTo(eTime);
		}
	}
	
})


function dateCheckout(){
	var date_checkout = document.getElementById("date_checkout");
	var sDate = $("#sDate").val();
	var eDate = $("#eDate").val();
	sDate = sDate.toDate("yyyy-MM-dd HH:mm:ss");
	eDate = eDate.toDate("yyyy-MM-dd HH:mm:ss");
	
	var sHour = parseInt(document.getElementById("sHour").value);
	var eHour = parseInt(document.getElementById("eHour").value);
	
	var sMonth = sDate.getMonth() + 1;
	var eMonth = eDate.getMonth() + 1;
	var sYear = sDate.getFullYear();
	var eYear = eDate.getFullYear();
	if(sDate == "" || eDate == ""){
		date_checkout.innerHTML = "请选择日期！";
		return;
	}else if( sYear != eYear || sMonth != eMonth ){
		date_checkout.innerHTML = "起始日期与结束日期,必须为同一个月！";
		return;
	}else if(sDate > eDate){
		date_checkout.innerHTML = "起始日期大于结束日期，请正确选择！";
		return;
	}else if(sHour >= eHour){
		date_checkout.innerHTML = "起始小时大于或等于结束小时，请正确输入！";
		return;
	}else{
		date_checkout.innerHTML = "";
		getDateTr();
		findCarMileageByBizInTime();
	}
	
}
</script>

<script>
	
	var loading = null;
	var carOpt = null;
	var date_list = null;
	var info = null;
	//初始化
	$(document).ready(function(){
		$("#selectWorkPlaceButton").click(function() {
			$("#selectWorkPlace").toggle("fast");
		})
		addKeyEvent();
		//信息集合分页
		carOpt = {
					"pageSize" : 10 , 
					"dataArray" : new Array() , 
					"table" : $("#mileage_table") , 
					"effect" : 2 , 
					"columnMethod" : function ( i,key,tdData , tr ){
										if ( tdData == null ) {
											return;
										}

										info = tdData;
										
										var total_gps_mileage = info["COUNT"];
										
										var instrumentcount = info["instrumentcount"];
										
										var carNumberTd = $("<td ter='" + info["terminalId"] + "'/>").appendTo($(tr));
										
										var carNumberA = $("<a ter='" + info["terminalId"] + "' />").css({ "color" : "blue" , "text-decoration" : "underline" }).attr({"target" : "_blank" , "href":"cargeneral_index.jsp?carId="+info["carId"]}).text(info["carNumber"]).appendTo($(carNumberTd));
										
										var gisEm = $("<em/>").appendTo($(carNumberTd));
										
										var gisA = $("<img/>").css({"cursor":"pointer"}).attr({ "align" : "middle" , "src":"images/map_ico.png"}).appendTo($(gisEm));
										
										$(gisA).click(function(){
											var carNumber = encodeURI(encodeURI(info["carNumber"]));
											window.open("loadCarStateMonitoringPageAction?carNumber="+carNumber+"&display=none");
										});
										
										var area = $("<td />").html(info["carBizName"]).appendTo($(tr));
										
										
										for(var i =1;date_list != null && i <= date_list.length ; i++)
										{
											
											var date = date_list[i-1].toString("yyyy-MM-dd");
											
											var day = info[date];
											
											
											
											if(!day || day == "null" || day == 0)
											{
												var dayTd = $("<td/>").text("0").css({"color":"#D4D4D4"}).appendTo($(tr));
											}
											else
											{
												var cNumber = info["carNumber"];
												
												var sd = date + " 00:00:00";
												var ed = date + " 23:59:59";
												
												var u = "#";
												
												if ( date != "" ) 
												{
													var carNumber = encodeURI(encodeURI(cNumber));
													u = "loadCarStateMonitoringPageAction?carNumber=" + carNumber + "&beginTime=" + sd + "&endTime=" + ed +"&display=none";
												}
												
												var dayA = $("<a/>").html(day).attr({ 
													"href" : u , 
													"target" : "_blank" 
												});
												var dayTd = $("<td d='" + day + "'/>").append($(dayA)).appendTo($(tr));
												
												if ( day > 400 ) {
													$(dayTd).css({"background-color":"peachpuff"});
												} else if ( day > 200 ) {
													$(dayTd).css({"background-color":"lightblue"});
												}
											}
											
										}
										
										if(total_gps_mileage==0)
										{
											var total_gps_mileageTd = $("<td/>").text(total_gps_mileage).css({"color":"#D4D4D4"}).appendTo($(tr));	
										}
										else
										{
											var total_gps_mileageTd = $("<td/>").text(total_gps_mileage).appendTo($(tr));	
										}
										
										if(instrumentcount==0)
										{
											var total_mileageTd = $("<td/>").text(instrumentcount).css({"color":"#D4D4D4"}).appendTo($(tr));
										}
										else
										{
											var total_mileageTd = $("<td/>").text(instrumentcount).appendTo($(tr));
										}
										
					}
										
				}
		carPage = new TablePage(carOpt);
		
		
		
		var now = new Date();
		var first = now.getFirstDate().toString("yyyy-MM-dd");
		var last = now.getLastDate().toString("yyyy-MM-dd");
		$("#sDate").val(first);
		$("#eDate").val(last);
		loading = new LoadingDiv({
			"id" : "gps_loading"
		});
		
		getDateTr();
		//获取当前登录人的下级组织
		providerOrgTree();
	})
	
	function expotBtnDate()
	{
		if(info!=null && date_list!=null)
		{
			document.getElementById('exportDate').submit();
		}else
		{
			date_checkout.innerHTML = "请先统计后，再导出数据！";
		}
	}

	//获取日期表头
	function getDateTr () {
		 var sDate = $("#sDate").val().toDate("yyyy-MM-dd HH:mm:ss");
		 var eDate = $("#eDate").val().toDate("yyyy-MM-dd HH:mm:ss");
		 var date_list = Date.getDateByStartEnd( sDate , eDate );
		 var tr = $(".day_tr");
		 
		 var table = $("#mileage_table tbody");
		 $(table).empty();
		 $("#gps_m_title").attr({"colspan":date_list.length});
		 $(tr).empty();
		 for ( var i = 0 ; i < date_list.length ; i++ ) {
			 var d = date_list[i];
			 var day = d.getDate();
			 var td = $("<td/>").text( day + "日" ).appendTo($(tr)) ;
		 }
	}
	
	

	
	
	
	
	function findCarMileageByBizInTime () {
		//获取时间集合
		var sDate = $("#sDate").val().toDate("yyyy-MM-dd HH:mm:ss");
		var eDate = $("#eDate").val().toDate("yyyy-MM-dd HH:mm:ss");
		var now = new Date();
		if ( !sDate || sDate == null || sDate == "" ) {
			sDate = now.getFirstDate().toString("yyyy-MM-dd");
		}
		if ( !eDate || eDate == null || eDate == "" ) {
			eDate = now.getLastDate().toString("yyyy-MM-dd");
		}
		date_list = Date.getDateByStartEnd( sDate , eDate );
		var date_string_list = [];
		for ( var i = 0 ; i < date_list.length ; i++ ) {
			var date = date_list[i];
			date = date.toString("yyyy-MM-dd");
			date_string_list.push(date);
		}
		date_string_list = obj2String(date_string_list);
		var sHour = $("#sHour").val()+":00:00";
		var eHour = $("#eHour").val()+":00:00";
		

		if ( $("#sHour").val().length == 1 ) {
			sHour = "0" + sHour;
		}
		
		if ( $("#eHour").val().length == 1 ) {
			eHour = "0" + eHour;
		}
		
		eHour = eHour.toDate("HH:mm:ss").addSeconds(-1).toString("HH:mm:ss");	
		
		
		
		
		//获取组织id
		var bizId = $("#choice_bizId").val();
		if ( !bizId || bizId == "" ) {
			throw "biz is null";
		}

		//显示loading
		loading.showLoading();
		$.ajax({
			"url" : "cardispatchManage_ajax!findCarMileageByBizInTime.action" , 
			"type" : "post" , 
			"data" : { "bizId" : bizId , "date" : date_string_list , "startDate" : $("#sDate").val()+" "+sHour , "endDate" : $("#eDate").val()+" "+eHour } , 
			"async" : true ,
			"success" : function ( result ) {
				var data = eval( "(" + result + ")");
				loading.closeLoading();
				if ( !data || data == null ) {
					return;
				}
				carPage.setDataArray(data);
				carPage.refreshTable();
				carPage.checkButton();
			}
		});
	}
	
	
	
	/***************** 组织结构 *******************/	
		
	//生成组织架构树
	function providerOrgTree(){
		var orgId = "16";
		$.ajax({
			"url" : "cardispatchForeign_ajax!getLoginUserBiz.action" , 
			"type" : "post" , 
			"async" : true , 
			"success" : function ( data ) {
				data = eval( "(" + data + ")" );
				orgId = data.orgId;
				$("#choice_bizName").val(data.name);
				$("#choice_bizId").val(data.orgId);
				if(orgId==null||orgId==""){
					orgId="16";
				}
				var values = {"orgId":orgId}
				var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
				$.post(myUrl,values,function(data){
					createOrgTreeOpenFirstNode(data,"selectWorkPlace","carmanage_org_div","a","orgTreeClick");
				},"json");
			}
		});
		
	}
	
	
	//显示组织信息
	function orgTreeClick(dataStr,tableId){
		var data = eval( "(" + dataStr + ")" ) ;
		var orgId = data.orgId;
		$("#choice_bizName").val(data.name);
		$("#choice_bizId").val(data.orgId);
		$("#selectWorkPlace").slideUp("fast");
	} 
	
</script>

</head>

<body>
	<form id="exportDate" action = "CardispatchExportExcelAction" method="post">
	<div class="container">
		<%-- 查询菜单 begin --%>
    	<div class="search_bar">
        	<span>统计范围：</span>
        	<input name="choice_bizName" value="" type="text"
				id="choice_bizName" />
			<input name="choice_bizId" value="" type="hidden"
				id="choice_bizId" />
            <a href="javascript:void(0);" class="select_button selectWorkPlaceButton" id="selectWorkPlaceButton" title="选择组织"></a>
			<div id="selectWorkPlace" class="text_org_tree">
				<%-- 放置组织架构树 --%>
			</div>
            <input type="text" id="sDate" readonly="readonly" /><a class="dateButton" onclick="fPopCalendar(event,document.getElementById('sDate'),document.getElementById('sDate'),false)"></a>
            -
            <input type="text" id="eDate" readonly="readonly" /><a class="dateButton" onclick="fPopCalendar(event,document.getElementById('eDate'),document.getElementById('eDate'),false)"></a>
            
			时间段
			
           	<select id="sHour" style="width:45px"></select>  -  
            <select id="eHour" style="width:45px"></select>
            <input type="button" id="censusBtn" onclick="dateCheckout();" value="统计" style="margin-left:30px;" />
            <input type="button" id="exportBtn" onclick="expotBtnDate();" value="导出" style="margin-left:15px;" />
            <span id="date_checkout" class="red"></span>
        </div>
        <%-- 查询菜单 end --%>
        
        <div class="search_result">
        	<table class="search_result_table gps_mileage" id="mileage_table">
        		<thead>
        			<tr>
	                	<th rowspan="2">车辆牌照</th>
	                	<th rowspan="2">所属项目</th>
	                	<th id="gps_m_title" colspan="31">GPS里程（公里）</th>
	                	<th rowspan="2">GPS<br />总里程</th>
	                	<th rowspan="2">仪表<br />总里程</th>
	                </tr>
	                <tr class="day_tr">
	                </tr>
        		</thead>
            	<tbody>
            	
            	</tbody>
            </table>
        </div>
    </div>
     </form> 
    <div>
    	
    </div>
    
    
</body>
</html>

