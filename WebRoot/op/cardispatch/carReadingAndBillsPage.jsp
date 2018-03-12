<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>车辆仪表读数与油费</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery-ui-1.8.23.custom.css" />
<link type="text/css" rel="stylesheet" href="../../jslib/gantt/gantt_small.css" />
<link type="text/css" rel="stylesheet" href="css/alert_div.css" />
<link type="text/css" rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="css/car.css" />
<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css"></link>
<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css"></link>
<link rel="stylesheet" href="css/skins/default.css" type="text/css"/>
<link rel="stylesheet" type="text/css" href="css/loading_cover.css" />
<style type="text/css">
.select_button {
    background: url("images/select.png") no-repeat scroll 0 0 transparent;
    display: inline-block;
    height: 24px;
    position: absolute;
    width: 24px;
}
.delete_people {
    background: url("images/delete_button.png") no-repeat scroll 0 0 transparent;
    display: inline-block;
    height: 20px;
    width: 20px;
}
#treeDiv {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    background: none repeat scroll 0 0 #F9F9F9;
    border-color: -moz-use-text-color #CCCCCC #CCCCCC;
    border-image: none;
    border-right: 1px solid #CCCCCC;
    border-style: none solid solid;
    border-width: medium 1px 1px;
    display: none;
    height: 200px;
    left: 4px;
    line-height: 18px;
    overflow: auto;
    position: absolute;
    text-align: left;
    top: 28px;
    width: 200px;
    z-index: 199;
}
</style>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js "></script>
<script type="text/javascript" src="jslib/public.js "></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript">
//判断整型与浮点型
var r = /^((-{0,1}[0-9]+[\\.]?[0-9]+)|-{0,1}[0-9])$/;
$(function(){
	/*弹出组织架构树*/
		$("#orgSelectButton").click(function(){
			$("#treeDiv").toggle("fast");
		});
		
		$(".search_tab ul li").click(function(){
			$(".search_tab ul li").removeClass("ontab");
			$(this).addClass("ontab");
		});
		
		$("#Reading").click(function(){
			$("#distinction").val("Reading");
			selectCarReadingOrBills("Reading",1);
		});
		
		$("#Bills").click(function(){
		$("#distinction").val("Bills");
			selectCarReadingOrBills("Bills",1);
		});
		
		$("#simpleQueryButton").click(function(){
			selectPage(1);
		});
		//设置当前时间
		setNowDate();
		searchProviderOrgTree();
		selectPage(1);
});

//设置当前时间
function setNowDate(){
		var now = new Date();
		var startYear = now.getFullYear();
		var startMonth = now.getMonth() + 1;
		if(startMonth < 10){
			startMonth = "0"+startMonth+"";
		}
		now.setMonth(now.getMonth()+1);
		now.setDate(0);
		var endYear = now.getFullYear();
		var endMonth = now.getMonth() + 1;
		if(endMonth < 10){
			endMonth = "0"+endMonth+"";
		}
		var endDay = now.getDate();
		if(endDay < 10){
			endDay = "0"+endDay+"";
		}
		$("#startTime").val(startYear + "-" + startMonth + "-01");
		$("#endTime").val(endYear + "-" + endMonth + "-" + endDay);
}

//生成服务商的组织架构树
function searchProviderOrgTree(){
	var orgId = $("#orgId").val();
		var values = {"orgId":orgId}
		var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
		$.post(myUrl,values,function(data){
			createOrgTreeOpenFirstNode(data,"treeDiv","carDutySearch_org_div","a","searchOrgTreeClick");
		},"json");
	selectCarByOrgId(orgId);
}

//显示服务商的组织信息
function searchOrgTreeClick(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#org_Name").val(data.name);
	$("#hidden_orgId").val(orgId);
	selectCarByOrgId(orgId);
	$("#treeDiv").slideUp("fast");
}

//根据组织ID获取车辆列表
function selectCarByOrgId(orgId){
	var values = {"orgId":orgId}
	var myUrl = "cardispatchManage!selectCarByOrgIdAction.action";
	$.post(myUrl,values,function(data){
			var context = "<option value=\"\">请选择车辆</option>";
			if(data){
				$.each(data,function(k,v){
					context = context + "<option value=\""+v.carId+"\">"+v.carNumber+"</option>";
				});
			}
			$("#car_select").html(context);
		},"json");
}

//根据类型与页数查询
function selectCarReadingOrBills(distinction,currentPage){
	var pageDivId = "pageContent";
	var showDivId = "listView1";
	var actionName = "selectReadingOrBillsAction";
	var orgId = $("#hidden_orgId").val();
	var carId;
	if($("#car_select").val()){
		carId = $("#car_select").val();
	}else{
		carId = "";
	}
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var pageSize = "10";
	var param={
		orgId:orgId,
		distinction:distinction,
		startTime:startTime,
		endTime:endTime,
		carId:carId,
		currentPage:currentPage,
		pageSize:pageSize
	};
	pagingColumnByBackgroundJsp(pageDivId,showDivId,actionName,param);
}

//获取页数查询
function selectPage(currentPage){
	$("#distinction").val($(".ontab").attr("id"));
	selectCarReadingOrBills($(".ontab").attr("id"),currentPage);
}
    
//逻辑删除
function deleteReadingOrBills(id){
	var distinction = $(".ontab").attr("id");
	$("#distinction").val($(".ontab").attr("id"));
	var param={
		id:id,
		distinction:distinction
	};
	$.ajax({
		"url" : "deleteReadingOrBillsAction" , 
		"type" : "post" , 
		"data":param,
		"dataType":'json',
		"async" : false , 
		"success" : function(result){
			if(result.flag == true){
				var currentPage = $("#pagingColumnCurrentPagepageContent").val();
				selectPage(currentPage);
			}
		}
	});
}           

/*导出*/
	function expotBtnDate(){
		document.getElementById('exportDate').submit();
	}     
</script>
</head>

<body>

	<div class="top_tab_container">
	<form id="exportDate" action="exportCarReadingOrBillsAction" method="post" target="_blank">
	<input type="hidden" id="orgId" value="${orgId }"/>
	<input type="hidden" name="distinction" id="distinction" value="Reading"/>
        
        <div class="tab_content_container">
        	<div class="search_tab clearfix">
                <ul>
                    <li class="ontab" id="Reading" style=" cursor: pointer;">仪表读数查询</li>
                    <li id="Bills" style=" cursor: pointer;">车辆油费查询</li>
                </ul>
            </div>
            <div style="display:block;" class="search_tab_content">
                <table class="search_table">
                    <tbody><tr>
                        <td>
	                        <input type="hidden" id="hidden_orgId" name="orgId" value="${orgId }">
			               	<input type="text" class="pt1" readonly="readonly"  value="${orgName }" id="org_Name"><a id="orgSelectButton" href="javascript:void(0);" title="选择组织" class="areaButton"></a>
		                    <div id="treeDiv">
		                    </div>
		                    <select id="car_select" name="carId">
                            	
                            </select>
                            &nbsp;记录时间：&nbsp;
                            <input type="text" class="pt1" name="startTime"  id="startTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'})" readonly class="Wdate required input-text"/>
                            &nbsp;至&nbsp;
                            <input type="text" class="pt1" name="endTime" id="endTime" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})" readonly class="Wdate required input-text"/>
							<input type="button" id="simpleQueryButton" value="查询" style="width:50px;" class="">
							<input type="button" id="exportBtn" onclick="expotBtnDate();" value="导出" style="width:50px;" style="margin-left:15px;" />
							
                        </td>
                    </tr>
                </tbody></table>
            </div>
            <div class="search_tab_content">
                
            </div>
        </div>
        </form>
    </div>
    <div class="main_tab_container">
    
        
        
        <%-- 搜索结果 --%>
        <div class="search_result">
        	<div id="listView">
	            <div id="listView1" class="present_tab_content">
        	
				</div>
	            <%-- 默认每页10条或20条记录 --%>
	             <div id="pageContent"></div>
             </div>
            
        </div>
    </div>
</body>
</html>

