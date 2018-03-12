$(function(){
	
	//显示组织树
	searchProviderOrgTree();
	
	/*绑定按钮，弹出组织架构树*/
	$("#chooseAreaButton").click(function(){
		$("#treeDiv").toggle("fast");
	});
	
	
	
	$("#form1").validate({
		submitHandler: function(form) {
			form.submit();
		}
	});
	
});


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
			
			$("#searchOrgId").val(orgId);
			$("#searchOrgName").val(orgName);
			
			if($("#show_orgId").val() && $("#show_orgId").val()!=""){
				$("#searchOrgId").val($("#show_orgId").val());
			}
			
			if($("#show_orgName").val() && $("#show_orgName").val()!=""){
				$("#searchOrgName").val($("#show_orgName").val());
			}
			
			var d = new Date();
			var year = d.getFullYear();
			var typeName = $("#type option:selected").text();
			//预填巡检计划名
			$("#planTitle").val(year+"年"+$("#searchOrgName").val()+typeName+"计划");
			
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
	$("#searchOrgName").val(data.name);
	$("#searchOrgId").val(data.orgId);
	$("#treeDiv").slideUp("fast");
}