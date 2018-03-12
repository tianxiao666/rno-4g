var treesData;


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

	$($("input[name=releaseType]")[0]).attr("checked",true);	//默认选中'所在部门及下级部门'
	
	/*
	//更改默认审核人
	$("#btnUpdateDefaultApprover").click(function(){
		var newApprover=$("#otherApprover").val();
		location.href="updateDefaultApproverAction?newDefaultApprover="+newApprover;
	});*/
	
	
	/*
	//用户权限特殊设置
	$("#saveUserLimit").click(function(){
		
		var staffList=$("input[name=selectStaff]:checked");
		if(staffList.length==0){
			alert("请选择要修改的用户");
		}else{
			
			var radioUserLimit=$("input[name=radioUserLimit]:checked");
			if(radioUserLimit.length==0){
				alert("请选择用户的权限定制");
			}else{
				//alert(radioUserLimit.val());
				
				location.href="updateUserReleaseLimitAction?selectUser="+staffList.val()+"&selectUserLimit="+radioUserLimit.val();
				
			}
		}
		
	});*/
	
	
	
	//生成组织架构树
	var queryUrl="getCmsOrgTreeActionForAjaxAction";
	$.ajax({ 
        type : "post", 
        url : queryUrl, 
        async : false,
        dataType : "json", 
        success : function($data){ 
        	var result = $data;
        	if(result){
        		treesData=result;
				createOrgTree(treesData,"treeDiv","tree1");	//创建组织架构树
        	}
        }
    });
	
	
	/**
	 * 创建组织架构树
	 */
	function createOrgTree(data,divId,treeId){
		var listViewDom = $("#"+divId);	//构造树容器对象
		var ul = $("<ul id=\""+treeId+"\" class=\"treeview\"></ul>");	//构造根节点
		
		if(!data){
			//清空数据
			listViewDom.html("");
			ul.appendTo(listViewDom);
			return false;
		}
		foreachCreateTreeByData(data,ul);
		
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

	/**
	 * 递归生成组织架构树（带部分折叠的）
	 */
	function foreachCreateTreeByData(data,ul){
		for(var i=0;i<data.length;i++){
			var data2 = data[i];
			//判断有无子级菜单
			var childTree = data2.children;
			if(childTree == undefined || childTree.length==0){
				if(data2.bizunitInstanceId != undefined && data2.name != undefined) {
					var li = $("<li></li>");
					//var eleCheckbox=$("<input  name=\"ckOrg\" type=\"checkbox\" value=\""+data2.bizunitInstanceId+"\"   />");
					var eleSpan=$("<span onclick=\"getStaffListByOrg('"+data2.bizunitInstanceId+"')\">"+data2.name+"</span>");
					var eleText=$("<input type=\"hidden\" value=\""+data2.bizunitInstanceId+"\" />");
					//li.append(eleCheckbox);
					li.append(eleSpan);
					li.append(eleText);
					li.appendTo(ul);
				}
			}else{
				if(data2.bizunitInstanceId != undefined && data2.name != undefined) {
					var li;
					if(data2.bizunitInstanceId==16 || data2.bizunitInstanceId=="16"){
						li = $("<li></li>");
					}else{
						li = $("<li class=\"closed\"></li>");
					}
					//var eleCheckbox=$("<input  name=\"ckOrg\" type=\"checkbox\" value=\""+data2.bizunitInstanceId+"\" onclick=\"getStaffListByOrg('"+data2.bizunitInstanceId+"')\" />")
					var eleSpan=$("<span onclick=\"getStaffListByOrg('"+data2.bizunitInstanceId+"')\">"+data2.name+"</span>");
					var eleText=$("<input type=\"hidden\" value=\""+data2.bizunitInstanceId+"\" />");
					//li.append(eleCheckbox);
					li.append(eleSpan);
					li.append(eleText);
					li.appendTo(ul);
				}
				var ul2 = $("<ul></ul>");
				foreachCreateTreeByData(childTree,ul2);
				ul2.appendTo(li);
			}
		}
	}
	
});

//根据orgId，获取该组织下的人员
function getStaffListByOrg(orgId){
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
        		createStaffTree(result,"tree2Div","tree2","radio");	//创建人员树
        	}
        }
    });
}

//根据数据，构建staff树，selectType：复选or单选
function createStaffTree(data,divId,treeId,selectType){
	
	var listViewDom = $("#"+divId);
	var ul;
	
	if(!data || data.length==0){
		//清空数据
		listViewDom.html("");
		ul = $("<ul>可选人员列表：</div><div id=\""+treeId+"\" style=\"margin-left:10px;\">该组织暂时没有用户</ul>");
		ul.appendTo(listViewDom);
		//return false;
	}else{
		ul = $("<ul id=\""+treeId+"\" class=\"treeview\">用户列表：</ul>");
		//构建用户列表树
		for(var i=0;i<data.length;i++){
			var staffObj = data[i];
			var li = $("<li></li>");
			var eleDiv=$("<div class=\"divAlign\"></div>");
			var eleCheckbox;
			if(selectType=="checkbox"){
				eleCheckbox=$("<input type=\"checkbox\" name=\"selectUser\" value=\""+staffObj.accountId+"\" />");
			}else{
				eleCheckbox=$("<input type=\"radio\" name=\"selectUser\" value=\""+staffObj.accountId+"\" />");
			}
			var eleSpan=$("<span >"+staffObj.cnName+"</span>");
			var eleText=$("<input type=\"hidden\" value=\""+staffObj.accountId+"\" />");
			eleDiv.append(eleCheckbox);
			eleDiv.append(eleSpan);
			eleDiv.append(eleText);
			li.append(eleDiv);
			li.appendTo(ul);
		}
		//清空数据
		listViewDom.html("");
		ul.appendTo(listViewDom);
		
	}
	//console.log("formatUrl=="+ul.html());
	//treeview事件
	$("#"+treeId+"").treeview({
		collapsed: false,
		animated: "fast",
		control:"#sidetreecontrol"
	});
}


//验证角色权限修改
function validateRoleLimit(){
	var flag=false;
	if($("input[name=roleLimitLevel]:checked").length==0){
		alert("请选择角色定制的权限类型");
	}else{
		flag=true;
	}
	
	return flag;
}


//验证更改默认审核员
function validateUpdateDefaultApprover(){
	var flag=false;
	if($("#newDefaultApprover").val()==""){
		alert("请选择要修改的用户");
	}else{
		flag=true;
	}
	return flag;
}


//验证用户发布权限
function validateUserReleaseLimit(){
	var flag=false;
	var staffList=$("input[name=selectUser]:checked");
	if(staffList.length==0){
		alert("请选择要修改的用户");
	}else{
		var radioUserLimit=$("input[name=selectUserLimit]:checked");
		if(radioUserLimit.length==0){
			alert("请选择用户的权限定制");
		}else{
			flag=true;
		}
	}
	
	return flag;
}
