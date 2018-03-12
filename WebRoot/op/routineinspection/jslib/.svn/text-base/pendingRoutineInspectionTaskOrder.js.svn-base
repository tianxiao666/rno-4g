$(function(){
	loadTaskList();
	
	//searchProviderOrgTree();
	
		/*绑定按钮，弹出组织架构树*/
	$("#chooseAreaButton").click(function(){
		$("#treeDiv").toggle("fast");
	});
});

//获取巡检待办任务列表
function loadTaskList(){
	//var orgId = $("#searchOrgId").val();
	var type = $("#type").val();
	//var status = $("#status").val();
	var planName = $("#planName").val();
	var taskName = $("#taskName").val();
	var pageDivId = "taskListPage";
	var showDivId = "taskList";
	var actionName = "getPendingRoutineInspectionTaskToPageAction";
	var pageSize = "10";
	var param={
		//orgId:orgId,
		type:type,
		//status:status,
		planName:planName,
		taskName:taskName,
		currentPage:"1",
		pageSize:pageSize
	};
	pagingColumnByBackgroundJsp(pageDivId,showDivId,actionName,param);
}

function openTaskOrderInfo(toId){
	window.open("loadRoutineInspectionTaskInfoByToIdAction?TOID="+toId);
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
			var myUrl="op/organization/getProviderOrgTreeByOrgIdAction";
			
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