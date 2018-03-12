$(function(){
	searchProviderOrgTree();
	
		/*绑定按钮，弹出组织架构树*/
	$("#chooseAreaButton").click(function(){
		$("#treeDiv").toggle("fast");
	});
});

//加载问题列表
function loadQuestionList(){
	var orgId = $("#searchOrgId").val();
	var questionType = $("#questionType").val();
	var seriousLevel = $("#seriousLevel").val();
	var isOver = $("#isOver").val();
	var description = $("#description").val();
	var pageDivId = "questionListPage";
	var showDivId = "questionList";
	var actionName = "searchRoutineinspectionQuestionListAction";
	var pageSize = "10";
	var param={
		orgId : orgId,
		questionType:questionType,
		seriousLevel:seriousLevel,
		isOver:isOver,
		description:description,
		currentPage:"1",
		pageSize:pageSize
	};
	pagingColumnByBackgroundJsp(pageDivId,showDivId,actionName,param);
}

//打开问题信息
function openQuestionInfo(id,toId){
	window.open("loadRoutineinspectionQuestionInfoAction?QID="+id+"&TOID="+toId);
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