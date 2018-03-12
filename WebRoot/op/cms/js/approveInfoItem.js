var treesData;
var lastReleaseScopeList;
var currentCheckedBounds="radio_department";

var userOrg="";
var isSelectApprover=false;

var userScopeList="";	//用户选择发布的数据
var releaseScopeStaffName="";

/*
//发布权限：
具权范围类型（默认是下级部门，不用授权），
1：所在部门及下级部门；
2：所在部门（不含下级部门）；
3：所在部门的直接上级部门及以下；
4：全公司；
5：所有(包括客户等)
*/

$(function(){
	


	//获取用户选择发布的数据
	userScopeList=$("#releaseScopeList").val();
	releaseScopeStaffName=$("#releaseScopeStaffName").val();
	lastReleaseScopeList = "";
	lastReleaseScopeList = $("#releaseScopeList").val();
	//
	var releaseHistory = $("#releaseHistoryspanhide").text();
	var arRelease =  releaseHistory.split("|");
	var context = "";
	$.each(arRelease,function(key,value){
		context = context + value + "</br>";
	});
	$("#releaseHistoryspan").html(context);
	
		//推送到消息盒子 复选框
	$("#ckBizBox").click(function(){
		if($(this).get(0).checked){
			$(this).val("1");
		}else{
			$(this).val("0");
		}
	});
	
	//短信通知 复选框
	$("#ckSms").click(function(){
		if($(this).get(0).checked){
			$(this).val("1");
		}else{
			$(this).val("0");
		}
	});
	
	//短信通知 复选框
	$("#ckSEM").click(function(){
		if($(this).get(0).checked){
			$(this).val("1");
		}else{
			$(this).val("0");
		}
	});
	
	
	
	//获取用户所属的组织和发布权限限制
	$.ajax({ 
        type : "post", 
        url : "getUserOfOrganizationAndReleaseLimitAction", 
        async : true,
        dataType : "json", 
        success : function($data){ 
        	var result = $data;
        	//alert(result);
        	if(result){
        		userOrg=result.userOrg;
        		console.log("用户所在组织=="+userOrg);
        	}
        }
    });
	
	//生成组织架构树
	//var queryUrl = "getAllOrgTreeActionForAjax";
	var queryUrl="../organization/getProviderOrgTreeByOrgIdAction?orgId=16";
	$.ajax({ 
        type : "post", 
        url : queryUrl, 
        async : false,
        dataType : "json", 
        success : function($data){ 
        	var result = $data;
        	if(result){
        		treesData=result;
        		//根据部门、人员、角色，显示不同风格的树，默认是部门
        		//var allck=$("input[name=releaseBounds]");
        		var checkedRadioVal=$("input[name=releaseBounds]:checked").val();
        		//alert("checkedRadioVal=="+checkedRadioVal);
        		
				createOrgTree(treesData,"treeDiv","tree1","radio_department");	//创建组织架构树
        	}
        }
    });
 });
	
	
	/**
	 * 创建组织架构树
	 */
	function createOrgTree(data,divId,treeId,checkedRadioVal){
		var listViewDom = $("#"+divId);	//构造树容器对象
		var ul = $("<ul id=\""+treeId+"\" class=\"treeview\"></ul>");	//构造根节点

		if(!data){
			//清空数据
			listViewDom.html("");
			ul.appendTo(listViewDom);
			return false;
		}
		foreachCreateTreeByData(data,ul,checkedRadioVal,null);
		
		//清空数据
		listViewDom.html("");
		ul.appendTo(listViewDom);
		
		//console.log("formatUrl=="+ul.html());
		//treeview事件
		$("#"+treeId+"").treeview({
			collapsed: false,
			animated: "fast",
			control:"#sidetreecontrol"
		});
	}

	
/*
//根据orgId，获取该组织下的人员
function getStaffListByOrg(orgId,optionalTree){
	var queryUrl="getCmsStaffTreeByOrgAction";
	var values={"orgId":orgId};
	$.ajax({ 
        type : "post", 
        url : queryUrl,
        data:values,
        async : false,
        dataType : "json", 
        success : function($data){ 
        	var result = $data;
        	if(result){
        		if(optionalTree=="optionalTree"){
        			createStaffTree(result,"tree2Div","tree2");	//可选人员列表
        		}else{
        			createStaffTree(result,"tree2Div","tree2");	//可选人员列表
					createStaffTree(result,"tree3Div","tree3");	//已选人员列表
        		}
				
        	}
        }
    });
};
*/

/*
//根据数据，构建staff树
function createStaffTree(data,divId,treeId){
	
	var listViewDom = $("#"+divId);
	var ul;

	if(!data || data.length==0){
		//清空数据
		if(divId=="tree2Div"){
			listViewDom.html("");
			ul = $("<ul>可选人员列表：</div><div id=\""+treeId+"\" style=\"margin-left:10px;\">该组织暂时没有人员</ul>");
			ul.appendTo(listViewDom);
		}else if(divId=="tree3Div"){
			listViewDom.html("");
			ul = $("<ul>已选人员列表：</div><div id=\""+treeId+"\" style=\"margin-left:10px;\">未选取任何人员</ul>");
			ul.appendTo(listViewDom);
		}
		//return false;
	}else{
		if(divId=="tree2Div"){
			ul = $("<ul id=\""+treeId+"\" class=\"treeview\">可选人员列表：</ul>");
			//构建可选人员列表树
			for(var i=0;i<data.length;i++){
				var staffObj = data[i];
				var li = $("<li></li>");
				var eleCheckbox=$("<input type=\"checkbox\" name=\"optionalCkStaff\" value=\""+staffObj.accountId+"\" />");
				var eleSpan=$("<span >"+staffObj.cnName+"</span>");
				var eleText=$("<input type=\"hidden\" value=\""+staffObj.accountId+"\" />");
				li.append(eleCheckbox);
				li.append(eleSpan);
				li.append(eleText);
				li.appendTo(ul);
			}
			//清空数据
			listViewDom.html("");
			ul.appendTo(listViewDom);
		}else if(divId="tree3Div"){
			
			//判断是否已经选择人员，如没有就清空
			var eleIsExist=$("#"+divId+" li").length;
			//alert("eleIsExist=="+eleIsExist);
			if(eleIsExist<=0){
				ul = $("<ul id=\""+treeId+"\" class=\"treeview\">已选人员列表：</ul>");
				//构建已选人员列表树
				for(var i=0;i<data.length;i++){
					var staffObj = data[i];
					var li = $("<li orgId=\""+staffObj.bizunitInstanceId+"\" ></li>");
					var eleCheckbox=$("<input type=\"checkbox\" name=\"selectedCkStaff\" value=\""+staffObj.accountId+"\" />");
					var eleSpan=$("<span >"+staffObj.cnName+"</span>");
					var eleText=$("<input type=\"hidden\" value=\""+staffObj.accountId+"\" />");
					li.append(eleCheckbox);
					li.append(eleSpan);
					li.append(eleText);
					li.appendTo(ul);
				}
				//清空数据
				listViewDom.html("");
				ul.appendTo(listViewDom);
			}else{
				ul = $("#"+treeId);
				//alert("已经选择人员了");
				//构建已选人员列表树
				for(var i=0;i<data.length;i++){
					var staffObj = data[i];
					var li = $("<li orgId=\""+staffObj.bizunitInstanceId+"\"></li>");
					var eleCheckbox=$("<input type=\"checkbox\" name=\"selectedCkStaff\" value=\""+staffObj.accountId+"\" />");
					var eleSpan=$("<span >"+staffObj.cnName+"</span>");
					var eleText=$("<input type=\"hidden\" value=\""+staffObj.accountId+"\" />");
					li.append(eleCheckbox);
					li.append(eleSpan);
					li.append(eleText);
					li.appendTo(ul);
				};
				ul.appendTo(listViewDom);
			}
		}
		
	}
	//console.log("formatUrl=="+ul.html());
	//treeview事件
	$("#"+treeId+"").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
}
*/



/**
 * 组织树复选框点击操作
 */
function orgTreeCheckClickEvent(ckObj,orgId,releaseBounds){
	if(releaseBounds && "radio_department"==releaseBounds){		//部门范围
		if(ckObj.checked){	//选中操作
				lastReleaseScopeList="";
			
			$("#"+orgId+" input[type='checkbox']").each(function(){
				$(this).attr("checked","checked");
				//alert($(this).next().next().val());
				//lastReleaseScopeList=lastReleaseScopeList+$(this).next().next().val()+"-";
			});
			$("#treeDiv input[type='checkbox'][checked='checked']").each(function(){
				//$(this).attr("checked","checked");
				//alert($(this).next().next().val());
				lastReleaseScopeList=lastReleaseScopeList+$(this).next().next().val()+"-";
			});
			$("#releaseScopeList").val(lastReleaseScopeList);
			lastReleaseScopeList=$("#releaseScopeList").val();
			console.log("lastReleaseScopeList=="+lastReleaseScopeList);
		}else{
			lastReleaseScopeList="";
				$("#"+orgId+" input[type='checkbox']").each(function(){
					$(this).removeAttr("checked");
				//alert($(this).next().next().val());
					//lastReleaseScopeList=lastReleaseScopeList.replace($(this).next().next().val()+"-","");
			});
			$("#treeDiv input[type='checkbox'][checked='checked']").each(function(){
				//$(this).attr("checked","checked");
				//alert($(this).next().next().val());
				lastReleaseScopeList=lastReleaseScopeList+$(this).next().next().val()+"-";
			});
			$("#releaseScopeList").val(lastReleaseScopeList);
			lastReleaseScopeList=$("#releaseScopeList").val();
				console.log("lastReleaseScopeList=="+lastReleaseScopeList);
				//$("#releaseScopeList").val(lastReleaseScopeList);
		}
		
	}else if(releaseBounds && "radio_staff"==releaseBounds){		//人员范围
		
		if(ckObj.checked){	//如果组织选中，就把该组织下‘已选人员列表’的人员选中；相反，就移除
			getStaffListByOrg(orgId,"all");	//创建‘可选人员列表树’和‘已选人员列表树’
			$("input[name=selectedCkStaff]").attr("checked",ckObj.checked);
		}else{
			//不是全部去除，而是有选择性的去除
			//$("#tree3Div li").remove();
			var eleLiList=$("#tree3Div li");
			for(var i=0;i<eleLiList.length;i++){
				var eleLi=$(eleLiList[i]);
				if(eleLi.attr("orgId")==orgId){
					//alert("orgId=="+orgId+"要移除");
					eleLi.remove();
				}
			}
		}
		
		buildStaffListTesxt();
	}
}

/**
 * 组织树复选框点击操作
 */
 /*
function orgTreeCheckClickEvent(ckObj,orgId,releaseBounds){
	if(releaseBounds && "radio_department"==releaseBounds){		//部门范围
		if(ckObj.checked){	//选中操作
			if(!lastReleaseScopeList){
				lastReleaseScopeList=orgId+"-";
			}else{
				lastReleaseScopeList=lastReleaseScopeList+orgId+"-";
			}
			
			$("#releaseScopeList").val(lastReleaseScopeList);
			lastReleaseScopeList=$("#releaseScopeList").val();
		}else{
			if(lastReleaseScopeList.indexOf(orgId)!=-1){
				lastReleaseScopeList=lastReleaseScopeList.replace(orgId+"-","");
				//console.log("lastReleaseScopeList=="+lastReleaseScopeList);
				$("#releaseScopeList").val(lastReleaseScopeList);
			}
		}
		
	}else if(releaseBounds && "radio_staff"==releaseBounds){		//人员范围
		
		if(ckObj.checked){	//如果组织选中，就把该组织下‘已选人员列表’的人员选中；相反，就移除
			getStaffListByOrg(orgId,"all");	//创建‘可选人员列表树’和‘已选人员列表树’
			$("input[name=selectedCkStaff]").attr("checked",ckObj.checked);
		}else{
			//不是全部去除，而是有选择性的去除
			//$("#tree3Div li").remove();
			var eleLiList=$("#tree3Div li");
			for(var i=0;i<eleLiList.length;i++){
				var eleLi=$(eleLiList[i]);
				if(eleLi.attr("orgId")==orgId){
					//alert("orgId=="+orgId+"要移除");
					eleLi.remove();
				}
			}
		}
	}
}

*/


function nomvalidateLimit(){
	if($("#nom_titlte").val() == ""){
		//alert("标题/主题不能为空");
		$("#nom_titlte").next().text("*标题/主题不能为空");
		return false;
		
	}else{
		$("#nom_titlte").next().text("");
	}
	if($("#nom_content").val() == ""){
		//alert("正文不能为空");
		$("#nom_content").next().text("*正文不能为空");
		return false;
		
	}else{
		$("#nom_content").next().text("");
	}
	if($("#nom_titlte").val().length > 100){
		$("#nom_titlte").next().text("*标题/主题不能超过100字");
		//alert("标题/主题不能超过100字");
		return false;
	}else{
		$("#nom_titlte").next().text("");
	}
	if($("#nom_label").val().length > 50){
		$("#nom_label").next().text("*文件号不能超过50字");
		//alert("文件号不能超过50字");
		return false;
	}else{
		$("#nom_label").next().text("");
	}
	if($("#nom_titlte").val().length > 100){
		$("#nom_titlte").next().text("*标题/主题不能超过100字");
		//alert("标题/主题不能超过100字");
		return false;
	}else{
		$("#nom_titlte").next().text("");
	}
	if($("#nom_content").val().length > 500){
		$("#nom_content").next().text("*正文不能超过500字");
		//alert("标题/主题不能超过100字");
		return false;
	}else{
		$("#nom_content").next().text("");
	}
}

	//验证发布权限
	function validateLimit(){
		var releaseBounds=$("input[name=releaseBounds]:checked");
		var textarea_c = $("#textarea_c").val();
		if(textarea_c == ""){
			$("#textarea_c").next().text("*信息内容不能为空");
			return false;
		}else{
			$("#textarea_c").next().text("");
		}
		if($("#nom_titlte").val() == ""){
			$("#nom_titlte").next().text("*标题/主题不能为空");
			//alert("标题/主题不能为空");
			return false;			
		}else{
			$("#nom_titlte").next().text("");
		}
		if($("#nom_content").val() == ""){
			$("#nom_content").next().text("*正文不能为空");
			//alert("正文不能为空");
			return false;	
		}else{
			$("#nom_content").next().text("");
		}
		if($("#releasePeriodStart") && $("#releasePeriodStart").val() == ""){
			$("#releasePeriodStart").next().text("*开始时间不能为空");
			//alert("开始时间不能为空");
			return false;
		}else{
			$("#releasePeriodStart").next().text("");
		}
		if($("#releasePeriodEnd") && $("#releasePeriodEnd").val() == ""){
			$("#releasePeriodEnd").next().text("*结束时间不能为空");
			//alert("结束时间不能为空");
			return false;
		}else{
			$("#releasePeriodEnd").next().text("");
		}
		if(isSelectApprover){
			return true;
		}else{
			var cmsCategory = $("#cmsCategory").val();
			if(cmsCategory == "安全生产"){
				//质量安全部
				if(userOrg.indexOf("-58-") > -1){
					return true;
				}
			}if(cmsCategory != "安全生产"){
				//人力资源部
				if(userOrg.indexOf("-59-") > -1){
					return true;
				}
			}
			
			var releaseBounds=$("input[name=releaseBounds]:checked");
			var selectOrg=$("input[name=ckOrg]:checked");	//获取要发布信息的组织
			
			//按部门发布
			if("radio_department"==releaseBounds.val()){
					if(selectOrg.length==1){
						var selectOneValue=selectOrg.val();
						//alert('selectOneValue=='+selectOneValue);
						//判断选中的组织是否是用户所在的组织
						if(userOrg.indexOf("-"+selectOneValue+"-")!=-1){
							//alert("可以发布");
							limitFlag=true;
						}else{
							//alert("没有权限发布，请选择审批人");
							$("#limitDiv").css("display","block");
							//$("#releaseOrApprove").val("2");	//改为审批状态
							
							isSelectApprover=true;
						}
					}else{
						selectOrg.each(function(index){
							var tempVal=$(this).val();
							//alert('tempVal=='+tempVal);
						});
					}
			}
		}
				
		//limitFlag=false;	//hardcode测试
	}
	
	
	
	/**
	 * 递归生成组织架构树（带部分折叠的）
	 */
	function foreachCreateTreeByData(data,ul,checkedRadioVal,functionName){
		//“加或减”图标: collapsable  可折叠 || expandable	可展开
		//closed	用来打开还是关闭的
		for(var i=0;i<data.length;i++){
			var data2 = data[i];
			var childTree = data2.children;	//判断有无子级菜单
			var dataStr = ObjectToStr(data2);
				if(childTree == undefined || childTree.length==0){
					if(data2.orgId != undefined && data2.name != undefined) {
						var li = $("<li class=\"vm\" id=\""+data2.orgId+"\"></li>");
						var eleSpan;
						if(userScopeList.indexOf(data2.orgId+"-") == 0 || userScopeList.indexOf("-" + data2.orgId+"-") != -1){
							eleSpan=$("<input  name=\"ckOrg\" checked type=\"checkbox\" value=\""+data2.orgId+"\" onclick=\"orgTreeCheckClickEvent(this,'"+data2.orgId+"','radio_department')\"  /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+data2.orgId+"');changeColor(this)>"+data2.name+"</span>");
						}else{
							eleSpan=$("<input  name=\"ckOrg\" type=\"checkbox\" value=\""+data2.orgId+"\" onclick=\"orgTreeCheckClickEvent(this,'"+data2.orgId+"','radio_department')\"  /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+data2.orgId+"');changeColor(this)>"+data2.name+"</span>");
						}
						var eleText=$("<input type=\"hidden\" value=\""+data2.orgId+"\" />");
						li.append(eleSpan);
						li.append(eleText);
						li.appendTo(ul);
					}
				}else{

					if(data2.orgId != undefined && data2.name != undefined) {
						var eleSpan;
						var li = $("<li class=\"vm\" id=\""+data2.orgId+"\"></li>");
						if(userScopeList.indexOf(data2.orgId+"-")==0 || userScopeList.indexOf("-" + data2.orgId+"-") != -1){
							eleSpan=$("<input  name=\"ckOrg\" checked type=\"checkbox\" value=\""+data2.orgId+"\" onclick=\"orgTreeCheckClickEvent(this,'"+data2.orgId+"','radio_department')\" /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+data2.orgId+"');changeColor(this)>"+data2.name+"</span>")
						}else{
							eleSpan=$("<input  name=\"ckOrg\" type=\"checkbox\" value=\""+data2.orgId+"\" onclick=\"orgTreeCheckClickEvent(this,'"+data2.orgId+"','radio_department')\" /><span class='folder' onclick="+functionName+"('"+dataStr+"','"+data2.orgId+"');changeColor(this)>"+data2.name+"</span>");
						}
						var eleText=$("<input type=\"hidden\" value=\""+data2.orgId+"\" />");
						li.append(eleSpan);
						li.append(eleText);
						li.appendTo(ul);
					}
					var ul2 = $("<ul></ul>");
					
					foreachCreateTreeByData(childTree,ul2,checkedRadioVal,functionName);
					ul2.appendTo(li);
				}
			
		}
	}
	
	

$(function(){
	//返回上一步查看
	$("#btnPrevLook").click(function(){
		//var ifId=$("#infoItemId").val();
		//var infoReleaseId = $("#infoReleaseId").val();
		//alert("infoItemId==="+ifId);
		//$.post("loadInfoItemPageAction",{infoItemId:ifId,infoReleaseId:infoReleaseId,Itype:"approve"},function(data){
		//	$("#right_content").html(data);
		//});
		var tabType = $("#tabType").val();
		loadInfoReleaseAction(tabType);
	});
	
	$("#roleCode").val($("#hideRole").val());
	
	if($("#infoReleaseStatus").val() != 21 || $("#isOperator").val() == 0){
		$("input").attr("disabled","disabled");
		$("textarea").attr("disabled","disabled");
		$("select").attr("disabled","disabled");
		$("#btnPrevLook").removeAttr("disabled");
		
		$("#limitDiv").hide();
	}else{
		$("#btnApprove").removeAttr("disabled");
		$("#btnReject").removeAttr("disabled");
		$("#btnReleaseInfo").removeAttr("disabled");
		$(".nod").removeAttr("disabled");
		$(".newsMessage_approver input").removeAttr("checked");
		$("#btnPrevLook").removeAttr("disabled");
	}
	
	//$("#infoReleaseId").removeAttr("disabled");
	//$("#releaseOrApprove").removeAttr("disabled");
	
	//如果不是当前处理人，隐藏操作按钮
	if($("#isOperator").val()=="1" && $("#infoReleaseStatus").val()!="25"){
		$("div.cms_content_but").css("display","block");
	}else{
		$("div.cms_content_but").css("display","none");
	}
	$("input[type=hidden]").removeAttr("disabled");


	//驳回操作
	$("#btnReject").click(function(){
		var infoReleaseId=$("#infoReleaseId").val();
		$("#releaseOrApprove").val("4");	//审批不通过
		var releaseOrApprove=$("#releaseOrApprove").val();
		//location.href=;
		window.open("approveInfoReleaseAction?infoReleaseId="+infoReleaseId+"&releaseOrApprove="+releaseOrApprove);
	});
	
	//表单AJAX提交
		$("#operaForm").ajaxForm({
			success:function(data){
				data = eval("("+data+")");
				alert(data.returnString);
				$("#showInfo").click();
			}
		},"json");
});



function clickCheckedapprover(){
	var i = 0;
	var fl = validateLimit();
		if(fl == false){
			return false;
		}
	$(".nod").each(function(){
		if($(this).attr("checked") == "checked"){
			i++;
		}
	});
	if(i == 0){
		
		if($("#limitDiv").css("display") == 'block'){
			alert("请选审核人");
		}else{
			var cmsCategory = $("#cmsCategory").val();
			if(cmsCategory == "安全生产"){
				//质量安全部
				if(userOrg.indexOf("-58-") > -1){
					return true;
				}
			}if(cmsCategory != "安全生产"){
				//人力资源部
				if(userOrg.indexOf("-59-") > -1){
					return true;
				}
			}
			$("#limitDiv").show();
		}
		return false;
	}else{
		return true;
	}
}


function clickNodRadio(me){
	$("#superiorsAccId").hide();
	$("#companyLeadersId").hide();
	$("#qualityId").hide();
	$("#humanResourcesId").hide();
	$("#infoReleaseStaffId").hide();
	$("#"+me).show();
}


function clickReleaseBounds(){
	if($("#releaseBounds_raido").attr("checked") == "checked"){
		$("#roleCode").show();
	}else{
		$("#roleCode").hide();
	}
}