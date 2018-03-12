/**
 * 事件绑定
 */
$(document).ready(function(){
	//修改按钮
	$("#orgrole_modify_role_confirm_btn").click(function(){
		modifyRole();
	});
	
	//删除按钮
	$("#orgrole_delete_role_btn").click(function(){
		deleteRole();
	});
	
	//确定添加角色按钮
	$("#orgrole_add_role_confirm_btn").click(function(){
		addRole();
	});
	
	
	//触发角色相关的初始化动作
	changeUsergroupRole();
});

/**
 * 点击某个role的响应事件
 * @param {Object} obj  事件发生的对象
 * @param {Object} rolekind 角色的类型
 * @param {Object} roleid
 *                 role的id
 * @param {Object} rolecode
 *                 role的code
 * @param {Object} rolename
 *                 role的name
 */
function chooseRole(obj,rolekind,roleid,rolecode,rolename){
//	alert("rolecode="+rolecode+",rolename="+rolename);
	rolecode=decodeURIComponent(rolecode);
	rolename=decodeURIComponent(rolename);
	
	
	$(obj).addClass("selected");
	$(obj).siblings("li").each(function(i){
		$(this).removeClass("selected");
	});
	
	//隐藏域的填充
	$("#_hidden_role_roleType_code").val(rolekind);//选择的角色类型
	$("#_hidden_"+rolekind+"_role_id").val(roleid);//选择的角色的id
	$("#_hidden_"+rolekind+"_li_id").val($(obj).attr("id"));//选择的角色所在li的id
	$("#role_rela_per_roleId").val(roleid);
	
	//信息编辑区域的信息填充
	$("#"+rolekind+"_role_id").val(roleid);
	$("#"+rolekind+"_modify_role_code").val(rolecode);
	$("#"+rolekind+"_modify_role_name").val(rolename);
	
	var myUrl = "ajaxInitPermissionTreeByRoleAction";
	$.post(myUrl,{roleId:roleid},function(data){
		createPermissionTreeByRole(data,"role_permissionTreeDiv","rptree","","fillHidden");
	},"json");
	
	
	//清空权限授权面板
	//clearAuthorizeDivArea(rolekind,"function");
	
}
//填充隐藏域
function fillHidden(dataStr,tableId,me){
	var data = eval("(" + dataStr + ")");
	$("#role_rela_per_perId").val(data.PID);
	//填充是否已经选择权限
	if($(me).attr("per") != null && $(me).attr("per") != ""){
		var per = $(me).attr("per");
		if(per.indexOf("read") >= 0){
			$("#read_checkbox").attr("checked",true);
		}else{
			$("#read_checkbox").attr("checked",false);
		}
		if(per.indexOf("write") >= 0){
			$("#write_checkbox").attr("checked",true);
		}else{
			$("#write_checkbox").attr("checked",false);
		}
	}else{
		$("#read_checkbox").attr("checked",false);
		$("#write_checkbox").attr("checked",false);
	}
}

/**
 * 点击某个roleType的响应事件
 * @param {Object} obj  事件发生的对象
 * @param {Object} rolekind 角色的类型
 * @param {Object} roleid
 *                 role的id
 * @param {Object} rolecode
 *                 role的code
 * @param {Object} rolename
 *                 role的name
 */
function chooseRoleType(obj,rolekind,roleid,rolecode,rolename){
//	alert("rolecode="+rolecode+",rolename="+rolename);
	rolecode=decodeURIComponent(rolecode);
	rolename=decodeURIComponent(rolename);
	
	
	$(obj).addClass("selected");
	$(obj).siblings("li").each(function(i){
		$(this).removeClass("selected");
	});
	
	//隐藏域的填充
	$("#_hidden_role_roleType_code").val(rolekind);//选择的角色类型
	$("#_hidden_"+rolekind+"_role_id").val(roleid);//选择的角色的id
	$("#_hidden_"+rolekind+"_li_id").val($(obj).attr("id"));//选择的角色所在li的id
	
	//信息编辑区域的信息填充
	$("#"+rolekind+"_role_id").val(roleid);
	$("#"+rolekind+"_modify_role_code").val(rolecode);
	$("#"+rolekind+"_modify_role_name").val(rolename);
	
	var myUrl = "ajaxInitPermissionTreeByRoleAction";
	$.post(myUrl,{roleId:roleid},function(data){
		createPermissionTreeByRole(data,"roleType_permissionTreeDiv","rtptree","","showResourceDetail");
	},"json");
	
	
	//清空权限授权面板
	//clearAuthorizeDivArea(rolekind,"function");
	
}

/**
 * 将function类型的资源json数据data转换为授权资源树
 * @param {Object} rolekind
 * @param {Object} data
 */
function translateFunctionJsonDataToAuthorizeTree(rolekind, roleId, data) {
	//	alert(data)
	var htmlStr = "";
	if (data.length > 0) {
		for ( var i = 0; i < data.length; i++) {
			var preInfo = data[i];
			var resource = preInfo['resource'];
			var opers = preInfo['operations'];
			var subPrevilidgeInfos = preInfo['subPrevilidgeInfo'];
			var operarr = "";
			var operstr = "";
			if (opers && opers.length > 0) {
				operstr += "(";
				for ( var k = 0; k < opers.length; k++) {
					var oper=opers[k];
					operarr+=oper['id'];
					operstr += "<em>" + oper['name'] + "</em>";
					if (k != opers.length - 1) {
						operstr += "/";
						operarr+=",";
					}
				}
				operstr += ")";
			}
			htmlStr += "<li><span class='folder'  id='"+rolekind+"_function_span_"+resource['id']+"' onclick=showResourcePrevilidgeDetail(this,'"
				    +rolekind
				    +"','function',"
					+ roleId
					+ ","
					+ resource['id']
					+ ",'"
					+ operarr
					+ "')>"
					+ resource['typeName'] + ":" + resource['name'];

			//加上操作后缀
			htmlStr += operstr;
			htmlStr += "</span>";
			//处理子资源树
			//
			if (subPrevilidgeInfos && subPrevilidgeInfos.length > 0) {
				htmlStr += "<ul>";

				for ( var subI = 0; subI < subPrevilidgeInfos.length; subI++) {
					htmlStr += dealEachPrevilidgeInfo(rolekind,'function',roleId,
							subPrevilidgeInfos[subI]);
				}

				htmlStr += "</ul>";
			}

			htmlStr += "</li>";
		}
	}
	$("#" + rolekind + "_function_tree").html("");
	$("#" + rolekind + "_function_tree").append($(htmlStr));

	//需要先清除treeview  TODO
	$("#" + rolekind + "_function_tree").treeview( {
		collapsed : true,
		animated : "fast"
	});
	$("#" + rolekind + "_function_tree li span").each(
			function() {
				$(this).click(
						function() {
							$("#" + rolekind + "_function_tree li span")
									.removeClass("on_selected");
							$(this).addClass("on_selected");
						})
			});
}

/**
 * 
 * @param {Object} rolekind
 * @param {string} resourcekind
 * @param {Object} roleId
 * @param {Object} preInfo
 * @return {TypeName} 
 */
function dealEachPrevilidgeInfo(rolekind,resourcekind,roleId,preInfo){
	var htmlStr="";
	
	var resource = preInfo['resource'];
	var opers = preInfo['operations'];
	var subPrevilidgeInfos = preInfo['subPrevilidgeInfo'];
	var operarr="";
	var operstr="";
	if(opers && opers.length>0){
		operstr+="(";
		for(var k=0;k<opers.length;k++){
			var oper=opers[k];
			operarr+=oper['id'];
			operstr+= "<em>" + oper['name'] + "</em>";
			if (k != opers.length - 1) {
				operstr += "/";
				operarr+=",";
			}
		}
		operstr+=")";
	}
	htmlStr += "<li><span class='file' id='"+rolekind+"_"+resourcekind+"_span_"+resource['id']+"' onclick=showResourcePrevilidgeDetail(this,'"+rolekind+"','"+resourcekind+"',"+roleId+","+resource['id']+",'"+operarr+"')>" + resource['typeName'] + ":"
			+ resource['name'];
	
	//加上操作后缀
	htmlStr+=operstr;
	
	htmlStr += "</span>";
	//处理子资源树
	//
	if (subPrevilidgeInfos && subPrevilidgeInfos.length > 0) {
		htmlStr += "<ul>";
		
		for(var subI=0;subI<subPrevilidgeInfos.length;subI++){
			htmlStr+=dealEachPrevilidgeInfo(rolekind,resourcekind,roleId,subPrevilidgeInfos[subI]);
		}
		
		htmlStr += "</ul>";
	}

	htmlStr += "</li>";
	
	return htmlStr;
}

/**
 * 将page,process类型的资源json数据data转换为授权资源树
 * @param {Object} rolekind
 * @param {string} roleId
 * @param {Object} resourcekind
 * @param {Object} data
 */
function translateModuleJsonDataToAuthorizeTree(rolekind,roleId,resourcekind,data){
	var htmlStr = "";
	if (data.length > 0) {
		for ( var i = 0; i < data.length; i++) {
			var preInfo = data[i];
			var module = preInfo['module'];
			var previlidges = preInfo['previlidgeInfos'];
			htmlStr += "<li><span class='folder' onclick=choosePrevilidgeResourceTreeModule('"+rolekind+"','"+resourcekind+"')>"
					+"模块:" + module['name'];

			htmlStr += "</span>";
			//处理子资源树
			//
			if (previlidges && previlidges.length > 0) {
				htmlStr += "<ul>";
//				alert("module:"+module['name']+"有资源");
				for ( var subI = 0; subI < previlidges.length; subI++) {
					htmlStr += dealEachPrevilidgeInfo(rolekind,resourcekind,roleId,
							previlidges[subI]);
				}

				htmlStr += "</ul>";
			}

			htmlStr += "</li>";
		}
	}
	$("#" + rolekind + "_"+resourcekind+"_tree").html("");
	$("#" + rolekind + "_"+resourcekind+"_tree").append($(htmlStr));

	//需要先清除treeview  TODO
	$("#" + rolekind + "_"+resourcekind+"_tree").treeview( {
		collapsed : true,
		animated : "fast"
	});
	$("#" + rolekind + "_"+resourcekind+"_tree li span").each(
			function() {
				$(this).click(
						function() {
							$("#" + rolekind + "_"+resourcekind+"_tree li span")
									.removeClass("on_selected");
							$(this).addClass("on_selected");
						})
			});
}

/**
 * 点击组织角色、业务角色tab下的用户群下拉框的响应事件
 * @param {Object} rolecode
 */
function changeUsergroupRole(){
//	alert("下来框值改变：选择的是"+rolekind+"下的用户群。"+$("#"+rolekind+"_usergrouproles").text())
	//TODO 结合“组织级别”或者“业务模块”获取相应的下属角色
	
	
	//一定要有
	var roleTypeId = $("#usergroup_roleType").val();
	if(roleTypeId==null ||roleTypeId==undefined || roleTypeId==0){
		return;
	}
	//填充添加&修改的角色类型Id隐藏域
	$("#add_roleTypeId").val(roleTypeId);
	$("#modify_roleTypeId").val(roleTypeId);
	$("#role_rela_per_perId").val("");
	$("#role_rela_per_roleId").val("");
	
	$.ajax({
		url:'getRolesByTypeForAjaxAction',
		data:{'role_ass_org_id':roleTypeId},
	    success:function(d){
		    var data=eval("("+d+")");
		    	$("#orgrole_role_ul").html("");
		    if(data['flag']==true){
		    	$("#orgrole_role_ul").append($(data['msg']));
		    }else{
		    	return;
		    }
	    }
	})
}

/**
 * 点击“业务角色”tab下的“业务模块”下拉框的响应事件
 * @param {Object} obj
 *                 下拉框对象
 */
function changeRoleBizmodule(obj){
//	alert("选择的业务模块是："+$(obj).children("option:selected").text()+",id="+$(obj).val());
	
	changeUsergroupRole('businessrole');
}

/**
 * 点击“组织级别”下拉框的响应事件
 * @param {Object} obj
 */
function changeOrgTemplate(obj){
//	alert("选择的组织级别是："+$(obj).children("option:selected").text());
	changeUsergroupRole('orgrole');
}

/**
 * 删除某种指定类型的角色
 * @param {Object} rolekind
 */
function modifyRole(){
	if($("#_hidden_orgrole_role_id").val()=="" ){
		alert("请先选择要修改的角色。");
		return;
	}
	$("#orgrole_modify_role_form").validate({submitHandler: function(form) { 
		$("#orgrole_modify_role_form").ajaxSubmit( {
			dataType : 'text',
			success : function(data) {
	//			alert(data);
				var obj=eval("("+data+")");
	//			alert(obj['flag'],"  "+decodeURIComponent(obj['msg']));
				if(obj['flag']==true){
					alert("修改成功");
					//更新几个地方的信息 
					//更新本tab的角色li
					$("#"+$("#_hidden_orgrole_li_id").val()).replaceWith($(obj['msg']));
				}else{
					alert("修改失败。原因："+obj['msg']);
					//重新触发该li的点击事件，
					$("#"+$("#_hidden_orgrole_li_id").val()).trigger("click");
					
				}
			}
		});
		return false;
	}});
}

/**
 * 删除某种选定的角色
 * @param {Object} rolekind
 */
function deleteRole(){
	if(confirm("你确定要删除该角色？该操作不可复原！")){
		$.ajax({
			type:'POST',
			url:'ajaxDeleteRoleAction',
			data:{'roleId':$("#_hidden_orgrole_role_id").val()},
			success:function(d){
				var data=eval("("+d+")");
				if(data['flag']==true){
					//清除树控件列表
					$("#orgrole_role_ul").children("li[id="+$("#_hidden_orgrole_li_id").val()+"]").remove();
					
					//清除其他信息
					if($("#orgrole_role_ul").children("li").length>0){
					  $("#orgrole_role_ul").children("li:first").trigger('click');
					}else{
						//已经没有该类型的角色了，需要手工进行下面的操作
						//清除对应的资源权限树
					    //清空隐藏域信息
						unselectRole();
					}
					alert("删除角色成功。");				
				}else{
					alert("删除角色失败。原因："+data['msg']?data['msg']:"");
				}
				
			}
		});
	}
}

/**
 * 新增某类角色
 * @param {Object} rolekind
 */
function addRole(){
	$("#orgrole_add_role_form").validate({submitHandler: function(form) {
		$("#orgrole_add_role_form").ajaxSubmit({
			dataType:'text',
			success:function(data){
			  var obj=eval("("+data+")");
			  if(obj['flag']==true){
				  alert("添加成功");
				  var newLi=$(obj['msg']);
				  $("#orgrole_role_ul").append(newLi);
				  $(obj['msg']).trigger('click');
				   $(".role_org_dialog").hide();
			  }else{
				  alert("添加失败。原因："+obj['msg']);
				   $(".role_org_dialog").hide();
			  }
			}
		});
		
		return false;
	}});
	
	
}

/**
 * 某类型的角色没有任何一个被选中时
 * @param {Object} rolekind
 */
function unselectRole(){
	//隐藏域
	$("#_hidden_orgrole_role_id").val("");
	$("#_hidden_orgrole_li_id").val("");
	
	$("#orgrole_modify_role_name").val("");
	$("#orgrole_modify_role_code").val("");
	$("#orgrole_modify_role_id").val("");
	
	//权限树
}

/**
 * @param {object} obj
 *                 产生点击事件的对象
 * @param {string} rolekind
 *                 当前角色类型
 * @param {string} resourcekind
 *                 资源类型
 * @param {string} roleid
 *                 当前所选角色id
 * @param {string} resourceid
 *                 当前点击资源id
 * @param {string} operids
 *                 当前操作集合
 */
function showResourcePrevilidgeDetail(obj,rolekind,resourcekind,roleid,resourceid,operids){
	$("#"+rolekind+"_"+resourcekind+"_resource_id").val(resourceid);//当前所选的资源id
	$("#"+rolekind+"_"+resourcekind+"_previlidge_tree_span_id").val($(obj).attr("id"));//当前所选的资源所在span的id
	
	var oids="";
	if (operids && operids != "") {
		if (oids.indexOf(",", 0) >= 0) {
			oids = operids.split(",");
		} else {
			oids = (operids+",").split(",");
		}
	}
	var operDiv=$("#"+rolekind+"_"+resourcekind+"_oper_div");
	var spans=operDiv.children("span");
	if (spans && spans.length > 0) {
		for ( var i = 0; i < spans.length; i++) {
			var span = spans[i];
			var checkbox = $(span).children('input:first');
			checkbox.removeAttr("checked");//先清掉选中状态
			if (oids != "") {
				var id = checkbox.attr('id');
				for ( var j = 0; j < oids.length; j++) {
					if (id == oids[j]) {
						checkbox.attr("checked", "checked");
						break;
					}
				}
			}
		}
	}
}

/**
 * 选中资源树的某个模块
 * @param {Object} rolekind
 * @param {Object} resourcekind
 */
function choosePrevilidgeResourceTreeModule(rolekind,resourcekind){
	clearAuthorizeDivArea(rolekind,resourcekind);
}

/**
 * 保存对角色的授权
 * 角色id从隐藏域取出
 * @param {Object} rolekind
 * @param {Object} resourcekind
 */
function saveRoleRelaPermission(){
	var role_id=$("#role_rela_per_roleId").val();//选择的角色的id
	if(role_id==""){
		alert("请先选择某个角色进行授权");
		return;
	}
	
	var resource_id=$("#role_rela_per_perId").val();
	if(resource_id==""){
		alert("请先选择要对角色进行授权的权限");
		return;
	}
	
	var perAccess = "";
	$("input[name='perAccess']:checked").each(function(){
		perAccess = perAccess + $(this).val() + "/";
	});
		$.ajax({
		url: "ajaxSaveRoleRelaPermissionAction",
		async:false,
		type:"POST",
		data:{roleId:role_id,permissionId:resource_id,perAccess:perAccess},
		success : function(result) {
			result = eval("("+result+")");
			if(result['flag']==true){
				alert("保存授权成功!");
				var perAccessText = "";
				$("input[name='perAccess']:checked").each(function(){
					if($(this).val() == 'read'){
						perAccessText = perAccessText + "查看/";
					}else if($(this).val() == 'write'){
						perAccessText = perAccessText + "编辑/";
					}else{
						perAccessText = perAccessText;
					}
				});
					var stext = "";
					if($(".on_selected").html().indexOf("(") >= 0){
						stext = $(".on_selected").html().substring(0,$(".on_selected").text().indexOf("("));
					}else{
						stext = $(".on_selected").html();
					}
					
					if(perAccessText != null && perAccessText != ""){
						perAccessText = perAccessText.substring(0,perAccessText.length-1);
						stext = stext + "("
							+"<em style=\"color:red\">"+perAccessText+"</em>"
						+")";
					}else{
						stext = stext;
					}
					$(".on_selected").html(stext);
					$(".on_selected").attr("per",perAccess);
			}else{
				alert("保存授权失败!");
			}
		}
		});
	
}

/**
 * 清除授权面板
 * @param {Object} rolekind
 * @param {Object} resourcekind
 */
function clearAuthorizeDivArea(rolekind,resourcekind){
	if(!$("#"+rolekind+"_"+resourcekind+"_resource_id")){
		return;
	}
	$("#"+rolekind+"_"+resourcekind+"_resource_id").val("");//清空当前所选的资源id
	$("#"+rolekind+"_"+resourcekind+"_previlidge_tree_span_id").val("");//清空当前所选的资源所在span的id
	
	//清除checkbox
	var div=$("#"+rolekind+"_"+resourcekind+"_oper_div");
	var spans=div.children('span:not(:last)');
	if(spans && spans.length>0){
		for(var si=0;si<spans.length;si++){
			var span=$(spans[si]);
			var checkbox=span.children("input:first");
			$(checkbox).removeAttr("checked");
		}
	}
}
