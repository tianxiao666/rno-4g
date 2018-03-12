<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车辆油费统计</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="css/informationManager.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css"></link>
<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css"></link>
<style type="text/css">
.paging_div{border: medium none;}

</style>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript"src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js "></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>

<script>
	//初始化
	$(document).ready(function(){
		var date= new Date();
		var year = date.getFullYear();
		var month=date.getMonth() ;
		
		if(month<10){
			month = "0"+month;
		}
		if(month==0){
			year=year-1;
			month=12;
		}
		$("#sDate").val(year+"-"+month);
		$("#selectWorkPlaceButton").click(function() {
			$("#selectWorkPlace").toggle("fast");
		})
		//获取当前登录人的下级组织
		providerOrgTree();
		searchCarFuel();
		
	})
	/*统计*/
	function searchCarFuel(){
		var queryUrl="getCarFuelBillsAction";
		var currentPage = 1;
		var totalPage = 1;
		var dateString = $("#sDate").val();
		var orgId = $("#choice_bizId").val();
		var orgName = $("#choice_bizName").val();
		if($.trim(orgId)==""||$.trim(orgName)==""){
			alert("请选择统计组织。");
			return false;
		}
		if($.trim(dateString)==""){
			alert("请选择统计月份。");
			return false;
		}
		
		var param = {currentPage:currentPage,totalPage:totalPage,orgId:orgId,orgName:orgName,dateString:dateString};
		pagingColumnByBackgroundJsp("pageContent","listDiv",queryUrl,param);
	}
	/*导出*/
	function expotBtnDate(){
		if($.trim($("#listDiv").html())!=""){
			document.getElementById('exportDate').submit();
		}else{
			date_checkout.innerHTML = "请统计后，导出数据！";
		}
	}

	/***************** 组织结构 *******************/	
		
	//生成组织架构树
	function providerOrgTree(){
		var orgId = "16";
		$.ajax({
			"url" : "cardispatchForeign_ajax!getLoginUserBiz.action" , 
			"type" : "post" , 
			"async" : false , 
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
	<form id="exportDate" action="exportCarFuelBillsAction" method="post" target="_blank">
	<div class="container">
		<%-- 查询菜单 begin --%>
    	<div class="search_bar">
        	<span>统计范围：</span>
        	<input name="choice_bizName" value="" type="text"
				id="choice_bizName" readonly/>
			<input name="choice_bizId" value="" type="hidden"
				id="choice_bizId" />
            <a href="javascript:void(0);" class="select_button selectWorkPlaceButton" id="selectWorkPlaceButton" title="选择组织"></a>
			<div id="selectWorkPlace" class="text_org_tree" style="border: 1px solid #CCCCCC;left: 78px;">
				<%-- 放置组织架构树 --%>
			</div>
			<span>统计月份：</span>
            <input type="text" id="sDate" onFocus="WdatePicker({dateFmt:'yyyy-MM'})" readonly class="Wdate required input-text" />
            <input type="button" id="censusBtn" onclick="searchCarFuel();" value="统计" style="margin-left:30px;" />
            <input type="button" id="exportBtn" onclick="expotBtnDate();" value="导出" style="margin-left:15px;" />
            <span id="date_checkout" class="red"></span>
        </div>
        <%-- 查询菜单 end --%>
        <div class="search_result" id="listDiv">
        </div>
        <%-- 默认每页10条或20条记录 --%>
	    <div id="pageContent"></div>
    </div>
     </form> 
    <div>
    	
    </div>
    
    
</body>
</html>

