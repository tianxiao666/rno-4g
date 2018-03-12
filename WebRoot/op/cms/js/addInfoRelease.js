var treesData;
var lastReleaseScopeList;
var currentCheckedBounds="radio_department";
var userScopeList="";	//用户选择发布的数据

var userOrg = "";
var isSelectApprover=false;

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
	lastReleaseScopeList = "";
	
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
	
	
	
	$("input[name=releaseBounds][value=radio_department]").attr("checked",true);	//默认选中部门单选按钮
	
	//返回上一步查看
	$("#btnPrevLook").click(function(){
		var ifId=$("#infoItemId").val();
		//alert("infoItemId==="+ifId);
		$.post("loadInfoItemPageAction",{infoItemId:ifId},function(data){
			$("#right_content").html(data);
		});
	});
	
	//发布范围单选按钮绑定事件
	$("input[name=releaseBounds]").click(function(){
		var tempVal=$(this).val();
		if(tempVal=="radio_department"){
			//$("#tree2Div").css("display","none");
			//$("#tree3Div").css("display","none");
			
		}else if(tempVal=="radio_staff"){
			//$("#tree2Div").css("display","block");
			//$("#tree2Div").html("");
			//$("#tree3Div").css("display","block");
			//$("#tree3Div").html("");
			
		}else if(tempVal=="radio_role"){
		
		}
		
		//如果当前按钮选中的话，就不去加载树和清空text
		if($(this).val()!=currentCheckedBounds){
			//$("#releaseScopeList").val("");
			//lastReleaseScopeList="";
			//createOrgTree(treesData,"treeDiv","tree1",tempVal);	//动态创建组织架构树
		}
		currentCheckedBounds=$(this).val();
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
}

//根据数据，构建staff树
function createStaffTree(data,divId,treeId){
	
	var listViewDom = $("#"+divId);
	var ul;
	
	/*if(divId=="tree2Div"){
		//ul = $("<ul id=\""+treeId+"\" class=\"treeview\">可选人员列表：</ul>");
	}else{
		//ul = $("<ul id=\""+treeId+"\" class=\"treeview\">已选人员列表：</ul>");
	}*/

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
				var eleCheckbox=$("<input type=\"checkbox\" name=\"optionalCkStaff\" onclick=\"optionPersonTreeClickEvent(this.checked,this.value,'"+staffObj.cnName+"','"+staffObj.bizunitInstanceId+"')\" value=\""+staffObj.accountId+"\" />");
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
					var eleCheckbox=$("<input type=\"checkbox\" name=\"selectedCkStaff\" value=\""+staffObj.accountId+"\" onclick=\"removeStaffId(this.value)\" />");
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
					var isExist;
					isExist=judgeIsExistInTree3(staffObj.accountId);
					//如果已选列表已经存在，就不要构造
					if(!isExist){
						var li = $("<li orgId=\""+staffObj.bizunitInstanceId+"\"></li>");
						var eleCheckbox=$("<input type=\"checkbox\" name=\"selectedCkStaff\" value=\""+staffObj.accountId+"\" onclick=\"removeStaffId(this.value)\" />");
						var eleSpan=$("<span >"+staffObj.cnName+"</span>");
						var eleText=$("<input type=\"hidden\" value=\""+staffObj.accountId+"\" />");
						li.append(eleCheckbox);
						li.append(eleSpan);
						li.append(eleText);
						li.appendTo(ul);
					}
				};
				ul.appendTo(listViewDom);
			}
		}
		
	}
	//treeview事件
	$("#"+treeId+"").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
}


//可选人员列表点击操作
function optionPersonTreeClickEvent(isChecked,staffId,staffName,staffBizId){
	var listViewDom = $("#tree3Div");
	var ul;
	
	if(isChecked){
		if(staffId){
			//判断是否已经选择人员，如没有就清空
			var eleIsExist=$("#tree3Div li").length;
			//alert("eleIsExist=="+eleIsExist);
			//alert("staffId=="+staffId);
			//orgId:staffObj.bizunitInstanceId
			
			if(eleIsExist<=0){
				ul = $("<ul id=\"tree3\" class=\"treeview\">已选人员列表：</ul>");
				//构建已选人员列表树
				var li = $("<li orgId=\""+staffBizId+"\"></li>");
				var eleCheckbox=$("<input type=\"checkbox\" name=\"selectedCkStaff\" checked value=\""+staffId+"\" />");
				var eleSpan=$("<span >"+staffName+"</span>");
				var eleText=$("<input type=\"hidden\" value=\""+staffId+"\" />");
				li.append(eleCheckbox);
				li.append(eleSpan);
				li.append(eleText);
				li.appendTo(ul);
				
				//清空数据
				listViewDom.html("");
				ul.appendTo(listViewDom);
			}else{
				
				var isExistSelectStaff=false;
				//alert("isChecked=="+isChecked);
				var eleHiddenTextList=$("#tree3Div li input[type=hidden]");
				for(var i=0;i<eleHiddenTextList.length;i++){
					var eleHidden=$(eleHiddenTextList[i]);
					if(eleHidden.val()==staffId){
						//alert("staffId=="+staffId+"要移除");
						isExistSelectStaff=true;
						if(!isChecked){
							eleHidden.parent().remove();
						}
					}
				}
				//alert("isExistSelectStaff=="+isExistSelectStaff);
				if(!isExistSelectStaff){
					ul = $("#tree3");
					//构建已选人员列表树
					var li = $("<li orgId=\""+staffBizId+"\"></li>");
					var eleCheckbox=$("<input type=\"checkbox\" name=\"selectedCkStaff\" checked value=\""+staffId+"\" />");
					var eleSpan=$("<span >"+staffName+"</span>");
					var eleText=$("<input type=\"hidden\" value=\""+staffId+"\" />");
					li.append(eleCheckbox);
					li.append(eleSpan);
					li.append(eleText);
					li.appendTo(ul);
					ul.appendTo(listViewDom);
				}
				
			}
			
		}
	}else{
		//alert("去除人员");
		var eleHiddenTextList=$("#tree3Div li input[type=hidden]");
		for(var i=0;i<eleHiddenTextList.length;i++){
			var eleHidden=$(eleHiddenTextList[i]);
			if(eleHidden.val()==staffId){
				//alert("staffId=="+staffId+"要移除");
				eleHidden.parent().remove();
			}
		}
	}
	
	//console.log("formatUrl=="+ul.html());
	//treeview事件
	$("#tree3").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
	
	
	buildStaffListTesxt();
}



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

//从已选人员列表中去除人员
function removeStaffId(staffId){
	var eleHiddenTextList=$("#tree3Div li input[type=hidden]");
	for(var i=0;i<eleHiddenTextList.length;i++){
		var eleHidden=$(eleHiddenTextList[i]);
		if(eleHidden.val()==staffId){
			eleHidden.parent().remove();
			break;
		}
	}
	buildStaffListTesxt();
}

//生成已选人员的字符text
function buildStaffListTesxt(){
	
	$("#releaseScopeList").val("");
	var tempText="";
	var tempStaffNameText="";
	var eleHiddenTextList=$("#tree3Div li input[type=hidden]");
	for(var i=0;i<eleHiddenTextList.length;i++){
		var eleHidden=$(eleHiddenTextList[i]);
		tempText+=eleHidden.val()+"-";
		
	}
	var eleSpanList=$("#tree3Div li span");
	for(var i=0;i<eleSpanList.length;i++){
		var eleSpan=$(eleSpanList[i]);
		tempStaffNameText+=eleSpan.text()+"-";
	}
	$("#releaseScopeList").val(tempText);
	$("#releaseScopeStaffName").val(tempStaffNameText);
	
}


//判断是否在已选人员列表选中目标人员
function judgeIsExistInTree3(staffId){
	var isExist=false;
	var eleHiddenTextList=$("#tree3Div li input[type=hidden]");
	for(var i=0;i<eleHiddenTextList.length;i++){
		var eleHidden=$(eleHiddenTextList[i]);
		if(eleHidden.val()==staffId){
			isExist=true;
			break;
		}
	}
	return isExist;
}



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
	var limitFlag=false;
	var releaseBounds=$("input[name=releaseBounds]:checked");
	var textarea_c = $("#textarea_c").val();
	if(textarea_c == ""){
		$("#textarea_c").next().text("*信息内容不能为空");
		//alert("信息内容不能为空");
		return false;
	}else{
		$("#textarea_c").next().text("");
	}
	if($("#releasePeriodStart") && $("#releasePeriodStart").val() == ""){
		$("#releasePeriodStart").next().next().text("*开始时间不能为空");
		//alert("开始时间不能为空");
		return false;
	}else{
		if($("#releasePeriodStart")){
			var myDate = new Date();
			var myDate1 = myDate.getMonth() + 1;
			var   firstdate = myDate.getFullYear() + '-' + myDate1 + '-' + myDate.getDate() + ' 00:00:00';
			var b_date = $("#releasePeriodStart").val();
	        if(compareTime(b_date, firstdate)){
				$("#releasePeriodStart").next().next().text("*开始时间不能小于当前日期");
				return false;
	        }else{
	        	$("#releasePeriodStart").next().next().text("");
	        }
		}
		
		if($("#releasePeriodEnd") && $("#releasePeriodStart")){
	        if(compareTime($("#releasePeriodEnd").val(), $("#releasePeriodStart").val())){
				$("#releasePeriodEnd").next().next().text("*结束时间不能小于开始时间");
				return false;
	        }else{
	        	$("#releasePeriodEnd").next().next().text("");
	        }
		}
	}
	
	if($("#releasePeriodEnd") && $("#releasePeriodEnd").val() == ""){
		$("#releasePeriodEnd").next().next().text("*结束时间不能为空");
		//alert("结束时间不能为空");
		return false;
	}else{
		
		$("#releasePeriodEnd").next().next().text("");
	}
	var smsnor = $(".smsnor:checked").val();
	var cmsCategory = $("#cmsCategory").val();
	if(smsnor != "sms"){
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
	}
	if(smsnor == "sms"){
		var cmsCategorysms = $("#cmsCategorysms").val();
		if(cmsCategorysms == "安全生产"){
			//质量安全部
			if(userOrg.indexOf("-58-") > -1){
				return true;
			}
		}if(cmsCategorysms != "安全生产"){
			//人力资源部
			if(userOrg.indexOf("-59-") > -1){
				return true;
			}
		}
	}
	
	//按部门发布
	if("radio_department"==releaseBounds.val() || "radio_role"==releaseBounds.val()){
		var selectOrg=$("input[name=ckOrg]:checked");	//获取要发布信息的组织
			if(selectOrg.length==0){	//判断是否选择要发布的目标
				alert("请选择发布范围");
			}else{
				var isOverRelease=false;
				
				//判断发布的目标是否超出发布权限，如果超出的话，要选择审批人
				for(var i=0;i<selectOrg.length;i++){
					var tempVal=selectOrg[i].value;
					if(userOrg.indexOf("-"+tempVal+"-")==-1){
						//alert("超出发布的范围了");
						isOverRelease=true;
						break;
					}
				}
				
				//如果超出发布范围，请选择审批人
				if(isOverRelease){
					if($("#limitDiv").css("display")=="block"){
						if($("input[name=radioApprover]:checked").length==0){
							alert("请选择要审批人");
						}else{
							limitFlag=true;
						}
					}else{
						$("#limitDiv").css("display","block");
					}
				}else{
					limitFlag=true;
				}
			}
	}else if("radio_staff"==releaseBounds.val()){	//按人员
		
		var selectStaffList=$("input[name=selectedCkStaff]:checked");	//获取要发布信息的人员
		if(selectStaffList.length==0){
			alert("请选择要发布的目标人员");
		}else{
			limitFlag=true;
		}
		
	}
			
	//limitFlag=false;	//hardcode测试
	return limitFlag;
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
						var li = $("<li class=\"vm\"  id=\""+data2.orgId+"\"></li>");
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
						var li = $("<li class=\"vm\"  id=\""+data2.orgId+"\"></li>");
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

	
//判断日期，时间大小  
function compareTime(startDate, endDate) {  
 if (startDate.length > 0 && endDate.length > 0) {  
    var startDateTemp = startDate.split(" ");  
    var endDateTemp = endDate.split(" ");  
                  
    var arrStartDate = startDateTemp[0].split("-");  
    var arrEndDate = endDateTemp[0].split("-");  
  
    var arrStartTime = startDateTemp[1].split(":");  
    var arrEndTime = endDateTemp[1].split(":");  
  
var allStartDate = new Date(arrStartDate[0], arrStartDate[1], arrStartDate[2], arrStartTime[0], arrStartTime[1], arrStartTime[2]);  
var allEndDate = new Date(arrEndDate[0], arrEndDate[1], arrEndDate[2], arrEndTime[0], arrEndTime[1], arrEndTime[2]);  
                  
if (allStartDate.getTime() >= allEndDate.getTime()) {  
        return false;  
} else {  
    return true;  
       }  
} else {  
    return false;  
      }  
}  



$(function(){
	//alert(15156);
	$(".newsMessage_approver input").removeAttr("checked");
	$("input").removeAttr("disabled");
		$("textarea").removeAttr("disabled");
		$("select").removeAttr("disabled");
	//表单AJAX提交
		$("#operaForm").ajaxForm({
			success:function(data){
				data = eval("("+data+")");
				alert(data.returnString);
				$("#showInfo").click();
			}
		},"json");
	loadOrgTree();
});



function loadOrgTree(){
	$("#newsMessage_schedule_no").hide();
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
        		//alert("ck值=="+$("input[name=releaseBounds]:checked").val());
				createOrgTree(treesData,"treeDiv","tree1",checkedRadioVal);	//创建组织架构树
        	}
        }
    });
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