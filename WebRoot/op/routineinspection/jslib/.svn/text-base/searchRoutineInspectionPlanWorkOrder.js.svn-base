$(function(){
	loadPlanWorkOrderList();
	
	searchProviderOrgTree();
	
		/*绑定按钮，弹出组织架构树*/
	$("#chooseAreaButton").click(function(){
		$("#treeDiv").toggle("fast");
	});
});

//根据工单获取任务列表
function loadPlanWorkOrderList(){
	var orgId = $("#searchOrgId").val();
	var type = $("#type").val();
	var status = $("#status").val();
	var planName = $("#planName").val();
	var pageDivId = "planWorkOrderPage";
	var showDivId = "planWorkOrderList";
	var actionName = "searchRoutineInspectionPlanWorkOrderAction";
	var pageSize = "10";
	var param={
		orgId:orgId,
		type:type,
		status:status,
		planName:planName,
		currentPage:"1",
		pageSize:pageSize
	};
	pagingColumnByBackgroundJsp(pageDivId,showDivId,actionName,param);
}

//打开工单信息
function openPlanWorkOrderInfo(woId){
	window.open("loadRoutineInspectionPlanWorkOrderInfoAction?WOID="+woId);
}

//关闭工单
function closePlanWorkOrder(woId){
	if(confirm("是否关闭该巡检计划?")){	
		$(".insp_black").show();
		$.post("closePlanWorkOrderAction",{"WOID":woId},function(data){
			//设置遮罩
			loadPlanWorkOrderList();
			$(".insp_black").hide();
		},"json");
	}
}

//删除工单
function deletePlanWorkOrder(woId){
	if(confirm("是否删除该巡检计划?")){	
		$(".insp_black").show();
		$.post("deletePlanWorkOrderAction",{"WOID":woId},function(data){
			//设置遮罩
			loadPlanWorkOrderList();
			$(".insp_black").hide();
		},"json");
	}
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
			
			//$("#searchOrgId").val(orgId);
			//$("#searchOrgName").val(orgName);
			
			
			//var values = {"orgId":orgId}
			//var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			
			
			var values={"orgId":orgId};
			var myUrl="../organization/getProviderOrgTreeByOrgIdAction";
			
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
	$("#searchOrgName").val(data.name);
	$("#searchOrgId").val(data.orgId);
	$("#treeDiv").slideUp("fast");
}