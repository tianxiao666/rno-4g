$(function(){


	//监听“上一步”按钮事件
	$("#btn_createPre").click(function(){
		location.href="op/routineinspection/showRoutineInspectionPlanWorkOrderAction";
	});
	
	
	//显示分页
	pagingColumnByForeground("table-gaging",$("#thcenter_table_1 .pageTr"),10);
	
	
	
	//监听全选事件
	$('#ckall').click(function(){
		var flag=$(this).get(0).checked;
		checkAll(flag);
	});
	
	//显示组织树
	searchProviderOrgTree();
	
	searchProviderOrgTree1();
	
	/*绑定按钮，弹出组织架构树*/
	$("#chooseAreaButton").click(function(){
		$("#treeDiv").toggle("fast");
	});
	
	/*绑定按钮，弹出组织架构树*/
	$("#chooseAreaButton1").click(function(){
		$("#treeDiv1").toggle("fast");
	});
	
	
	/*绑定按钮，弹出组织架构树*/
	$("#chooseAreaButton2").click(function(){
		$("#treeDiv2").toggle("fast");
	});
	
	
	//为form绑定监听事件
	$('#submitRoutineInspectionPlanWorkOrderForm').submit(function() {
		/*var flag = judgeCheckboxIsSelected("selectRoomWithOrgList");
		if (flag == false) {
			alert("请至少选择一个要执行巡检任务的机房");
			return false;
		};*/
		
		var selectRoomWithOrgList=$("input[name='selectRoomWithOrgList']");
		if(!selectRoomWithOrgList || selectRoomWithOrgList.length==0){
			alert("没有要执行巡检任务的机房");
			return false;
		}
	});
	
	
	
	$("#submitSelectRoutineInspectionToPlanForm").validate({
		submitHandler: function(form) {
			var isCanSubmit=true;
			if(!judgeCheckboxIsSelected("selectRoomToPlanList")){
				isCanSubmit=false;
				alert("请至少选择一个要执行巡检任务的机房");
				return;
			}
			
			var searchOrgName=$("#searchOrgName").val();
			if(searchOrgName=="" || !searchOrgName){
				isCanSubmit=false;
				alert("请选择要执行任务的组织");
				return;
			}
			
			var orgId=$("#searchOrgId").val();
			if(!judgeOrgIsMaintenanceTeam(orgId)){
				isCanSubmit=false;
				alert("执行巡检任务的组织必须是维护队类型");
				return;
			}
			
			
			var showPlanStartTime=$("#showPlanStartTime").val();
			var showPlanEndTime=$("#showPlanEndTime").val();
			
			var selectTaskStartTime=$("#date_text1").val();
			var selectTaskEndTime=$("#date_text2").val();
			
			//alert("showPlanStartTime=="+showPlanStartTime);
			//alert("showPlanEndTime=="+showPlanEndTime);
			//alert("selectTaskStartTime=="+selectTaskStartTime);
			//alert("selectTaskEndTime=="+selectTaskEndTime);
			
			if(compareTime(selectTaskStartTime,showPlanStartTime)==1 || compareTime(selectTaskStartTime,showPlanEndTime)==0){
				isCanSubmit=false;
				alert("任务的巡检时间必须要在巡检计划的时间范围【"+showPlanStartTime+"至"+showPlanEndTime+"】内");
				return;
			}
			
			if(compareTime(selectTaskEndTime,showPlanStartTime)==1 || compareTime(selectTaskEndTime,showPlanEndTime)==0){
				isCanSubmit=false;
				alert("任务的巡检时间必须要在巡检计划的时间范围【"+showPlanStartTime+"至"+showPlanEndTime+"】内");
				return;
			}
			
			if(isCanSubmit){
				$("#btnSelectRoom").attr("disabled","disabled");
				$("#submitSelectRoutineInspectionToPlanForm").ajaxSubmit({
					dataType:'text',
					async : false,
					success:function(result){
						if(result!="error"){
							window.location.href="autoCreateRoutineInspectionTaskAction";
						}else{
							alert("添加任务失败！");
							$("#btnSelectRoom").removeAttr("disabled","disabled");
						}
					}
				});
			}
		}
	});
	
	//监听任务‘添加’按钮事件
	$("#btn_addTask").click(function(){
		//$(this).attr("disabled","disabled");
		var url="getAllRoomForAjaxAction";
		//获取具有多个组织的机房列表
		$.ajax({
			url : url,
			dataType : 'text',
			type : 'post',
			success : function(result) {
				if(result && result.trim()!=""){
					var data = eval("(" + result + ")");
					if(data && data!=""){
						var htmlContent="<tr><th style=\"width:40px;\"><input type=\"checkbox\" id=\"ckall2\" />"+
						"</th><th>机房名称</th><th>机房等级</th><th>维护组织</th><th style=\"width:150px;\">机房地址</th><th>上一次巡检时间</th></tr>";
						for(var i=0;i<data.length;i++){
							var obj=data[i];
							var content="<tr class=\"pageTr\"><td><input name=\"selectRoomToPlanList\" type=\"checkbox\" value=\""+obj.id+"\" /></td>"+
							"<td>"+obj.name+"</td><td></td>"+
							"<td>"+obj.orgName+"</td><td>"+obj.name+"</td><td></td></tr>";
							htmlContent=htmlContent+content;
						}
						
						$("#thcenter_table_2").html(htmlContent);
						pagingColumnByForeground("table-gaging2",$("#thcenter_table_2 .pageTr"),10);
						$('#ckall2').click(function(){
							var flag=$(this).get(0).checked;
							checkAll2(flag);
						});
						
						var roomIdJsonStr = "";
						$("#thcenter_table_1 .pageTr").each(function(){
							var reId = $(this).children().eq(0).children().eq(0).val();
							roomIdJsonStr += reId+":"+reId+",";
						});
						if(roomIdJsonStr!=""){
							roomIdJsonStr = roomIdJsonStr.substring(0,roomIdJsonStr.length);
						}
						roomIdJsonStr = "{"+roomIdJsonStr+"}";
						var roomIdJson = eval("(" + roomIdJsonStr + ")");
						$("#thcenter_table_2 .pageTr").each(function(){
							var reId = $(this).children().eq(0).children().eq(0).val();
							if(roomIdJson[reId]){
								$(this).remove();
							}
						});
						/*
						$("#thcenter_table_2 .pageTr").each(function(){
							var addObj = $(this);
							var reId = $(this).children().eq(0).children().eq(0).val();
							$("#thcenter_table_1 .pageTr").each(function(){
								var reId1 = $(this).children().eq(0).children().eq(0).val();
								if(reId1==reId){
									addObj.remove();
									return false;
								}
							});
						});
						*/
						if($("#thcenter_table_2 .pageTr").length==0){
							alert("机房数据为空");
							return;
						}
						
						//显示对话框
						$("#jizhan_Dialog").show();
						$(".black").show();
						
						//监听分页控件事件
						$("#table-gaging2 a").each(function(index){
							//alert("name=="+this.title);
							$(this).live("click",function(){
							 	//$("#ckall2").removeAttr("checked");
						    });
						});
					}else{
						alert("机房数据为空");
					}
				}else{
					alert("该巡检组织下的机房已经建立了巡检任务");
				}
			}
		});
	});
	
	
	//监听返回按钮事件（关闭弹出框）
	$(".dialog_closeBtn").click(function(){
		$("#jizhan_Dialog").hide();
		$(".black").hide();
		//$("#btn_addTask").removeAttr("disabled");
		$("#searchRoomName").val("");
	});
	
	//监听查询按钮事件
	$("#btnSearchRoom").click(function(){
		var url="searchRoomForAjaxAction";
		var searchRoomName=$("#searchRoomName").val();
		var searchOrgId=$("#searchOrgId1").val();
		var values={"searchRoomName":searchRoomName};
		$.ajax({
			url : url,
			data:values,
			dataType : 'text',
			type : 'post',
			success : function(result) {
				var data = eval("(" + result + ")");
				if(data && data!=""){
					var htmlContent="<tr><th style=\"width:40px;\"><input type=\"checkbox\" id=\"ckall2\" />"+
					"</th><th>机房名称</th><th>机房等级</th><th>维护组织</th><th style=\"width:150px;\">机房地址</th><th>上一次巡检时间</th></tr>";
					for(var i=0;i<data.length;i++){
						var obj=data[i];
						var content="<tr class=\"pageTr\"><td><input name=\"selectRoomToPlanList\" type=\"checkbox\" value=\""+obj.resourceId+"\" /></td>"+
						"<td>"+obj.resourceName+"</td><td></td>"+
						"<td>"+obj.orgName+"</td><td>"+obj.resourceName+"</td><td></td></tr>";
						htmlContent=htmlContent+content;
					}
					
					$("#thcenter_table_2").html(htmlContent);
					pagingColumnByForeground("table-gaging2",$("#thcenter_table_2 .pageTr"),10);
					$('#ckall2').click(function(){
						var flag=$(this).get(0).checked;
						checkAll2(flag);
					});
					
					//显示对话框
					$("#jizhan_Dialog").show();
					$(".black").show();
					
					//监听分页控件事件
					$("#table-gaging2 a").each(function(index){
						//alert("name=="+this.title);
						$(this).live("click",function(){
						 	//alert("zzzzzzzz");
						 	//$("#ckall2").removeAttr("checked");
					    });
					});
				}else{
					//var htmlContent="<tr><th style=\"width:40px;\"><input type=\"checkbox\" id=\"ckall2\" />"+
					//"</th><th>机房名称</th><th>机房等级</th><th>维护组织</th><th style=\"width:150px;\">机房地址</th><th>上一次巡检时间</th></tr>";
					
					//$("#thcenter_table_2").html(htmlContent);
				}
			}
		});
	});
	
	
	//监听任务‘修改’按钮事件
	$("#btn_modifyTask").click(function(){
		var selectRoomWithOrgList=$("input[name='selectRoomWithOrgList']");
		if(!selectRoomWithOrgList || selectRoomWithOrgList.length==0){
			alert("没有要执行巡检任务的机房");
			return false;
		}else{
			var flag = judgeCheckboxIsSelected("selectRoomWithOrgList");
			if (flag == false) {
				alert("请至少选择一个要执行巡检任务的机房");
				return false;
			};
			$(".modify_alert").slideDown("fast");
			searchProviderOrgTree2();	//显示组织树
		}
		
	});
	
	$(".modify_alert_close").click(function(){
		$(".modify_alert").slideUp("fast");
		
		$("#btn_saveModifyRoom").removeAttr("disabled");
	});
	
	
	$("#modifyRoutineInspectionTaskToPlanOfRoomForm").validate({
		submitHandler: function(form) {
			var isCanSubmit=true;
			
			var orgId=$("#searchOrgId2").val();
			if(!judgeOrgIsMaintenanceTeam(orgId)){
				isCanSubmit=false;
				alert("执行巡检任务的组织必须是维护队类型");
				return;
			}
			
			var showPlanStartTime=$("#showPlanStartTime").val();
			var showPlanEndTime=$("#showPlanEndTime").val();
			
			var selectTaskStartTime=$("#date_text3").val();
			var selectTaskEndTime=$("#date_text4").val();
			
			if(compareTime(selectTaskStartTime,showPlanStartTime)==1 || compareTime(selectTaskStartTime,showPlanEndTime)==0){
				isCanSubmit=false;
				alert("任务的巡检时间必须要在巡检计划的时间范围【"+showPlanStartTime+"至"+showPlanEndTime+"】内");
				return;
			}
			
			if(compareTime(selectTaskEndTime,showPlanStartTime)==1 || compareTime(selectTaskEndTime,showPlanEndTime)==0){
				isCanSubmit=false;
				alert("任务的巡检时间必须要在巡检计划的时间范围【"+showPlanStartTime+"至"+showPlanEndTime+"】内");
				return;
			}
			
			//isCanSubmit=false;
			if(isCanSubmit){
				
				var values={};
				
				var searchOrgId2=$("#searchOrgId2").val();
				var searchOrgName2=$("#searchOrgName2").val();
				var selectTaskStartTime=$("#date_text3").val();
				var selectTaskEndTime=$("#date_text4").val();
				
				values.modifySelectRoomToPlanBeginTime=selectTaskStartTime;
				values.modifySelectRoomToPlanEndTime=selectTaskEndTime;
				values.modifySelectRoomToPlanOrgId=searchOrgId2;
				values.modifySelectRoomToPlanOrgName=searchOrgName2;
				
				
				var roomIdArray=new Array();
				//获取选中要修改的执行巡检任务的机房
				$("input[name='selectRoomWithOrgList']:checked").each(function(index){
					if($(this) && $(this).val()!=""){
						values["selectRoomWithOrgList["+index+"]"]=$(this).val();
					}
				});
				
				$("#btn_saveModifyRoom").attr("disabled","disabled");
				
				
				$.ajax({
					"url" : "modifyRoutineInspectionTaskToPlanOfRoomAction" , 
					"type" : "post" ,
					"data":values,
					"async":false,
					"success" : function ( data ) {
						if(data!="error"){
							window.location.href="autoCreateRoutineInspectionTaskAction";
						}else{
							alert("修改失败！");
						}
					}
				});
			}
		}
	});
	
	
	
	
	
});

//全选
function checkAll(flag) {
	$.each($("#thcenter_table_1 .pageTr"),function(){
		if($(this).css("display")!="none"){
			$(this).children().eq(0).children().eq(0).attr("checked",flag);
		}
	});
	/*
  for (var i = 0; i < document.getElementsByName("selectRoomWithOrgList").length; i++) {
   	document.getElementsByName("selectRoomWithOrgList")[i].checked = flag;
  }*/
}

function checkAll2(flag) {
	$.each($("#thcenter_table_2 .pageTr"),function(){
		if($(this).css("display")!="none"){
			$(this).children().eq(0).children().eq(0).attr("checked",flag);
		}
	});
	/*
  for (var i = 0; i < document.getElementsByName("selectRoomToPlanList").length; i++) {
   	document.getElementsByName("selectRoomToPlanList")[i].checked = flag;
  }*/
}


function judgeCheckboxIsSelected(checkboxName){
	var flag=false;
	var cks=$("input[name="+checkboxName+"]");
	for (i = 0;i< cks.length;i++){
		if (cks[i].checked==true) {
			flag= true;
			break;
		}
	}
	return flag;
}





//生成组织架构树
function searchProviderOrgTree(){
	var orgId = "16";
	var orgName="";
	
	$.ajax({
		"url" : "cardispatchForeign_ajax!getLoginUserBiz.action" , 
		"type" : "post" ,
		"async":false,
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
			
			
			//var values = {"orgId":orgId}
			//var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			
			
			var values={"orgId":orgId,"orgType":"MaintenanceTeam"};
			var myUrl="op/organization/getProviderOrgTreeByOrgIdAction";
			
			$.post(myUrl,values,function(data){
				createOrgTreeOpenFirstNode(data,"treeDiv","search_org_div","a","searchOrgTreeClick");
			},"json");
		}
	});
	
	
	var planOrgId=$("#showPlanOrgId").val();
	//默认显示创建巡检计划的组织
	$.ajax({
		"url" : "getProviderOrgByOrgIdInRoutineInspectionAction" , 
		"type" : "post" ,
		"data":{"orgId":planOrgId},
		"success" : function ( data ) {
			data = eval( "(" + data + ")" );
			orgId = data.id;
			orgName=data.name;
			$("#searchOrgId").val(orgId);
			$("#searchOrgName").val(orgName);
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

//生成组织架构树
function searchProviderOrgTree1(){
	var orgId = "16";
	var orgName="";
	$.ajax({
		"url" : "cardispatchForeign_ajax!getLoginUserBiz.action" , 
		"type" : "post" , 
		"async":false,
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
			
			$("#searchOrgId1").val(orgId);
			$("#searchOrgName1").val(orgName);
			
			
			//var values = {"orgId":orgId}
			//var myUrl = "../organization/getProviderOrgTreeByOrgIdAction";
			
			
			var values={"orgId":orgId,"orgType":"MaintenanceTeam"};
			var myUrl="op/organization/getProviderOrgTreeByOrgIdAction";
			
			$.post(myUrl,values,function(data){
				createOrgTreeOpenFirstNode(data,"treeDiv1","search_org_div1","a","searchOrgTreeClick1");
			},"json");
		}
	});
	
	var planOrgId=$("#showPlanOrgId").val();
	//默认显示创建巡检计划的组织
	$.ajax({
		"url" : "getProviderOrgByOrgIdInRoutineInspectionAction" , 
		"type" : "post" ,
		"data":{"orgId":planOrgId},
		"success" : function ( data ) {
			data = eval( "(" + data + ")" );
			orgId = data.id;
			orgName=data.name;
			$("#searchOrgId1").val(orgId);
			$("#searchOrgName1").val(orgName);
		}
	});
}


//选择组织
function searchOrgTreeClick1(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#searchOrgName1").val(data.name);
	$("#searchOrgId1").val(data.orgId);
	$("#treeDiv1").slideUp("fast");
}


//判断组织是否是维护队
function judgeOrgIsMaintenanceTeam(orgId){
	var isMaintenanceTeam=false;
	var values={"orgId":orgId};
	$.ajax({
		async:false,
		url: "getProviderOrgByOrgIdInRoutineInspectionAction" , 
		data:values,
		type: "post" , 
		success : function ( dataStr ) {
			var data = eval( "(" + dataStr + ")" ) ;
			if(data && data!=""){
				if("MaintenanceTeam"==data.type){
					isMaintenanceTeam=true;
				}
			}
		}
	});
	return isMaintenanceTeam;
}



/*
*比较两个时间的大小 输入时间格式:yyyy-MM-dd HH:mm:ss
*若time2>=time1 则返回1 
*若tim2<time1 则回0
*/
function compareTime(time1,time2){
	var dA=new Date(time1.replace(/-/g,"/"));
	var dB=new Date(time2.replace(/-/g,"/"));
	if(Date.parse(dA)<Date.parse(dB)){
		return 1;
	}else if(Date.parse(dA)>Date.parse(dB)){
		return 0;
	}else{
		return 2;
	}
}



//生成组织架构树
function searchProviderOrgTree2(){
	var orgId = "16";
	var orgName="";
	$.ajax({
		"url" : "cardispatchForeign_ajax!getLoginUserBiz.action" , 
		"type" : "post" , 
		"async":false,
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
			
			$("#searchOrgId2").val(orgId);
			$("#searchOrgName2").val(orgName);
			
			var values={"orgId":orgId,"orgType":"MaintenanceTeam"};
			var myUrl="op/organization/getProviderOrgTreeByOrgIdAction";
			
			$.post(myUrl,values,function(data){
				createOrgTreeOpenFirstNode(data,"treeDiv2","search_org_div2","a","searchOrgTreeClick2");
			},"json");
		}
	});
	
	var planOrgId=$("#showPlanOrgId").val();
	//默认显示创建巡检计划的组织
	$.ajax({
		"url" : "getProviderOrgByOrgIdInRoutineInspectionAction" , 
		"type" : "post" ,
		"data":{"orgId":planOrgId},
		"success" : function ( data ) {
			data = eval( "(" + data + ")" );
			orgId = data.id;
			orgName=data.name;
			$("#searchOrgId2").val(orgId);
			$("#searchOrgName2").val(orgName);
		}
	});
}

//选择组织
function searchOrgTreeClick2(dataStr,tableId){
	var data = eval( "(" + dataStr + ")" ) ;
	var orgId = data.orgId;
	$("#searchOrgName2").val(data.name);
	$("#searchOrgId2").val(data.orgId);
	$("#treeDiv2").slideUp("fast");
}
