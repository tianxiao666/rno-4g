//默认是显示“资源管理”页面
var currentShowDivBlock='resource_manage_div';
var currentOperation="";//弹出框是用于添加还是修改。添加：add，修改：modify

$(document).ready(function() {
	//---################资源管理块的事件绑定######################---//
	    //tab选项卡
		//tab("resources_tab", "li", "onclick");//受控资源类别切换
		
		//加载权限树
		permissionTree();

		//修改/添加受控资源弹出框
		$(".resources_showBtn").click(function() {
			$("#resources_Dialog").show();
			$(".black").show();
			if ($(this).attr("value") == '修改') {
				$(".dialog_title").html('修改受控资源');
				prepareForModifyResource();
				currentOperation="modify";
				$("#modify_add_resource_form").attr("action","ajaxModifyPermissionAction");
			} else {
				$(".dialog_title").html('添加受控资源');
				prevpareForAddResource("false");
				currentOperation="add";
				$("#modify_add_resource_form").attr("action","ajaxAddPermissionAction");
			}
		});
		
		$(".deleteResource").click(function(){
			deleteResource();
		});
		$(".dialog_closeBtn").click(function() {
//			alert("close btn")
			$("#resources_Dialog").hide();
			$(".black").hide();
		});

		//“修改”、“添加”点击事件
		//确定按钮
		$("#confirmAddOrModify").click(function() {
			confirmAddOrModifyAction();
		});
		//取消按钮
		$("#cancelAddOrModify").click(function(){
//			alert("cancel btn");
			$(".dialog_closeBtn").trigger("click");
			return false;
		});
		

		//---################角色管理块的事件绑定######################---//

		
		//修改/添加受控资源弹出框
		$(".resources_showBtn").click(function() {
			$("#resources_Dialog").show();
			$(".black").show();
		});
		$(".dialog_closeBtn").click(function() {
			$("#resources_Dialog").hide();
			$(".black").hide();
		});

		//组织添加角色
		$(".role_org_showBtn").click(function() {
			$(".role_org_dialog").show();
		});
		$(".role_org_closeBtn").click(function() {
			$(".role_org_dialog").hide();
		});
	});

//添加第一级权限
function showAddTopPerDiv(){
    $("#resources_Dialog").show();
	$(".black").show();
	$(".dialog_title").html('添加第一级资源');
	prevpareForAddResource("true");
	currentOperation="add";
	$("#modify_add_resource_form").attr("action","ajaxAddPermissionAction");
}
//生成权限树
function permissionTree(){
	var myUrl = "ajaxInitPermissionTreeAction";
	$.post(myUrl,{},function(data){
		createPermissionTree(data,"permissionTreeDiv","ptree","permissionInfoDiv","showResourceDetail");
	},"json");
}

function showDiv(obj,divId) {
	if(currentShowDivBlock==divId){
		return;
	}
    var arr=['resource_manage_div','role_manage_div','account_manage_div'];
    for(var i in arr){
    	if(arr[i]!=divId){
    		$("#"+arr[i]).css("display","none");
    	}
    }
    $("#" + divId).css("display", "block");
    currentShowDivBlock=divId;
    
    $(obj).siblings().each(function(index) {
		$(this).removeClass("menu_selected");
	});
	$(obj).addClass("menu_selected");
}


/**
 * 点击“受控资源控制”菜单-〉“页面类”tab下的某个业务模块
 * @param {Object} obj 事件发生的主体
 * @param {Object} kind
 * @param {Object} moduleId
 * @param {Object} moduleCode
 * @param {Object} moduleName
 */
function chooseResourceModule(obj,kind,moduleId, moduleCode, moduleName) {
//	console.log("choose one module :" + moduleId + "," + moduleCode + ","
//			+ moduleName);
	clearResourceFormData();
	
	//解码
	moduleCode=decodeURIComponent(moduleCode);
	moduleName=decodeURIComponent(moduleName);
	
//	console.log("after decode one module :" + moduleId + "," + moduleCode + ","
//			+ moduleName);
		
	//清空隐藏域修改
	$("#hiddenArea").children("input").each(function(){
		$(this).val("");
	});
	
	//
	$("#tabVal").val(kind);
	$("#_hidden_tabVal_id").val(kind);
	$("#_hidden_module_id").val(moduleId);
	$("#_hidden_module_code").val(moduleCode);
	$("#_hidden_module_name").val(moduleName);
	
	// 对应的资源展示区域清空
	clearResourceShowAreaInfo(kind);
	$("#_hidden_resource_li_id").val($(obj).parent("li:first").attr("id"));//记住该模块的li的id
}

/**
 * 显示选择资源的详情
 * @param {Object} resourcekind
 *                 哪个分类的资源
 * @param {Object} id
 *                 资源主键
 * @param {Object} typecode
 *                 资源类型code
 * @param {Object} typename
 *                 资源类型名称
 * @param {Object} code
 *                 资源code
 * @param {Object} name
 *                 资源名称
 * @param {Object} modulecode
 *                 所属模块code
 * @param {Object} modulename
 *                 所属模块name
 * @param {Object} needauth
 *                 是否需要验证
 * @param {Object} supername
 *                 上级资源name
 * @param {Object} accessurl
 *                 访问url
 */
function showResourceDetail(dataStr,tableId) {
	var data = eval("(" + dataStr + ")");
	//	var res="";
	//	alert("resource detail:"+res)
//	alert($(element).parent("li").attr("id"));
	$("#_show_page_resType_name").html("&nbsp;");
	$("#_show_page_res_name").html("&nbsp;");
	$("#_show_page_super_type_name").html("&nbsp;");
	$("#_show_page_accessUrl").html("&nbsp;");
	$("#_show_page_parameter").html("&nbsp;");
	$("#_show_page_description").html("&nbsp;");
	
	$("#_hidden_resource_type_id").val("");
	$("#_hidden_resource_type_name").val("");
	$("#_hidden_resource_code").val("");
	$("#_hidden_resource_name").val("");
	$("#_hidden_resource_title").val("");
	$("#_hidden_resource_needAuthenticate").val("");
	$("#_hidden_resource_isShowWorkplat").val("");
	$("#_hidden_resource_accessUrl").val("");
	$("#_hidden_resource_parameter").val("");
	$("#_hidden_resource_description").val("");
	$("#_hidden_resource_super_id").val("");
	$("#_show_page_description").val("");
	
	$("#_show_page_resType_name").html(data.TYPENAME);
	$("#_show_page_res_name").html(data.NAME);
	$("#_show_page_res_code").html(data.CODE);
	$("#_show_page_res_title").html(data.TITLE);
	//	alert("needauth = "+needauth)
	if (data.ENALBED == 1) {
		//		alert("check")
		$("#_show_page_needAuth").attr("checked", "checked");
	} else {
		//		alert("not check")
		$("#_show_page_needAuth").attr("checked", false);
	}
	if (data.IS_SHOW_WORKPLAT == 1) {
		//		alert("check")
		$("#_show_page_isShowWorkplat").attr("checked", "checked");
	} else {
		//		alert("not check")
		$("#_show_page_isShowWorkplat").attr("checked", false);
	}
	$("#_show_page_super_type_name").html(data.PTNAME);
	$("#_show_page_accessUrl").html(data.URL);
	$("#_show_page_parameter").html(data.PARAMETER);
	$("#_show_page_description").html(data.DESCRIPTION);

	//放进隐藏域
	$("#_hidden_resource_type_id").val(data.TYPEID);
	$("#_hidden_resource_type_name").val(data.TYPENAME);
	$("#_hidden_resource_id").val(data.PID);
	$("#_hidden_resource_code").val(data.CODE);
	$("#_hidden_resource_name").val(data.NAME);
	$("#_hidden_resource_title").val(data.TITLE);
	$("#_hidden_resource_needAuthenticate").val(data.ENALBED);
	$("#_hidden_resource_isShowWorkplat").val(data.IS_SHOW_WORKPLAT);
	$("#_hidden_resource_accessUrl").val(data.URL);
	$("#_hidden_resource_parameter").val(data.PARAMETER);
	$("#_hidden_resource_description").val(data.DESCRIPTION);
	$("#_hidden_resource_super_id").val(data.PARENTID);
	$("#_hidden_super_resource_type_name").val(data.PTNAME);
}

/**
 * 准备修改资源需要的信息
 */
function prepareForModifyResource() {
	$("#resource_type").html(
			"<option value=\"" + $("#_hidden_resource_type_id").val()
					+ "\" selected='true'>"
					+ $("#_hidden_resource_type_name").val() + "</option>");
	$("#resource_type").attr("disabled", false);
	$("#resource_name").val($("#_hidden_resource_name").val());
	$("#resource_title").val($("#_hidden_resource_title").val());
	$("#resource_code").val($("#_hidden_resource_code").val());
	if ($("#_hidden_resource_needAuthenticate").val() == '1') {
		$("#resource_needAuthenticate").attr("checked", "checked");
	} else {
		$("#resource_needAuthenticate").attr("checked", false);
	}
	if ($("#_hidden_resource_isShowWorkplat").val() == '1') {//是否加入到个人工作台
		$("#resource_isShowWorkplat").attr("checked", "checked");
	} else {
		$("#resource_isShowWorkplat").attr("checked", false);
	}
	$("#resource_superType_name").val($("#_hidden_super_resource_type_name").val());
	$("#resource_superType_name").attr("disabled", true);
	//$("#resource_superCode").val($("#_hidden_resource_super_code").val());
	//$("#resource_superCode").attr("disabled", true);
	//$("#resource_userDefineAccessRule").val(
	//		$("#_hidden_resource_userDefineAccessRule").val());
	//$("#resource_afterShowResource").val(
			//$("#_hidden_resource_afterShowResource").val());
	//$("#resource_order").val($("#_hidden_resource_order").val());
	//$("#resource_userDefineId").val($("#_hidden_resource_userDefineId").val());
	//从隐藏域获取
	$("#resource_id").val($("#_hidden_resource_id").val());
	$("#resource_accessUrl").val($("#_hidden_resource_accessUrl").val());
	$("#resource_parameter").val($("#_hidden_resource_parameter").val());
	$("#resource_description").val($("#_hidden_resource_description").val());
	//tab变量
	//$("#tabVal").val($("#_hidden_tabVal_id").val());
	//上级资源code
	//$("#superResourceCode").val($("#_hidden_resource_super_code").val());
	
}

/**
 * 提交资源修改或者添加
 */
function confirmAddOrModifyAction() {
//	alert($("#resource_needAuthenticate_check").attr("checked"));
	   if($("#resource_needAuthenticate_check").attr("checked")=="checked"){//是否需要验证
		   $("#resource_needAuthenticate").val(1);
	   }else{
		   $("#resource_needAuthenticate").val(0);
	   }
	    if($("#resource_isShowWorkplat_check").attr("checked")=="checked"){//是否加入到个人工作台
		   $("#resource_isShowWorkplat").val(1);
	   }else{
		   $("#resource_isShowWorkplat").val(0);
	   }
		$("#modify_add_resource_form").validate({submitHandler: function(form) { 
			$("#modify_add_resource_form").ajaxSubmit({
				dataType : 'text',
				success : function(data) {
					var obj=eval("("+data+")");
					$("#resources_Dialog").hide();
					$(".black").hide();
					
					if (currentOperation == "modify") {
						//根据加载回来的数据，刷新树控件的内容
						//将指定id的内容替换
//						var old_span = $("#" + $("#_hidden_resource_li_id").val())
//								.children("span:first");
//						obj.attr("class", old_span.attr("class"));
//						old_span.replaceWith(obj);
						
						//刷新对应的树
						//refreshTree($("#_hidden_tabVal_id").val());
						//obj.trigger("click");//触发span的点击事件
						 if(obj['flag']==true){
	                    	alert("修改成功!");
	                    	//考虑局部刷新资源树
	                    	//to refresh the resource tree
	                    	permissionTree();
	                    	//var selectedLiId=$("#_hidden_resource_li_id").val();
	                    	//addResourceTreeNode($("#_hidden_tabVal_id").val(),selectedLiId,$(obj['msg']));
	                    }else{
	                    	alert("修改失败!原因："+obj['msg']);
	                    }
					} else if (currentOperation == "add") {
	                    if(obj['flag']==true){
	                    	alert("添加成功!");
	                    	//考虑局部刷新资源树
	                    	//to refresh the resource tree
	                    	permissionTree();
	                    	//var selectedLiId=$("#_hidden_resource_li_id").val();
	                    	//addResourceTreeNode($("#_hidden_tabVal_id").val(),selectedLiId,$(obj['msg']));
	                    }else{
	                    	alert("添加失败!原因："+obj['msg']);
	                    }
					}
				}
			});
			return false;
		}});	

//	alert('ajax');
//	return;
	//	$.ajax({
	//		url:'modifyResourceForAjaxAction',
	//		data:{},
	//		complete:function(){
	//			alert('finish');
	//		}
	//		});

}

/**
 * 准备添加资源的信息
 */
function prevpareForAddResource(isRoot) {
	//清除form的数据
	clearResourceFormData();
	
	//默认选中要验证
	$("#resource_needAuthenticate_check").attr("checked","checked");
	var url = "";
	//添加第一级权限类型资源
	if(isRoot=="true"){
		url="ajaxGetRootPermissionType";
	}else if(isRoot=="false"){
		//获取资源类型填充下拉框
		var parentId = $("#_hidden_resource_type_id").val();
		if(parentId==""){
			alert("请选择要进行操作的权限");
			return;
		}
		
		url="ajaxGetPermissionType";
	}
	
	
	$.post(url,{parentId:parentId},function(data){
		if(data==null||data=="[]"){
			alert("该权限类型无下级权限类型，请先添加下级权限类型！");
			$("#resources_Dialog").hide();
			$(".black").hide();
			return;
		}
		var arr=eval("("+data+")");
		var htmlStr="";
		for(var i in arr){
			htmlStr+="<option value=\""+arr[i]["perTypeId"]+"\">"+arr[i]["name"]+"</option>";
		}
		$("#resource_type").html(htmlStr);
		$("#resource_type").attr("disabled", false);
	});
	if(isRoot=="false"){
		//隐藏域初始化
		$("#superResourceId").val($("#_hidden_resource_id").val());//当前选择的资源就是待添加资源的上级资源
		//上级资源信息初始化,其实就是当前点击的资源
		$("#resource_superType_name").val($("#_hidden_resource_type_name").val());
		
	}else{
		$("#resource_superType_name").val("");
	}
	$("#resource_superType_name").attr("disabled",true);
	
}

/**
 * 清空对应的某个类型的资源展示区的信息
 * @param {Object} kind
 */
function clearResourceShowAreaInfo(resourcekind){
	$("#_show_" + resourcekind + "_resType_name").html("&nbsp;");
	$("#_show_" + resourcekind + "_res_name").html("&nbsp;");
	$("#_show_" + resourcekind + "_res_code").html("&nbsp;");
	$("#_show_" + resourcekind + "_module_name").html("&nbsp;");
	//	alert("needauth = "+needauth)
	$("#_show_" + resourcekind + "_needAuth").attr("checked", false);
	$("#_show_" + resourcekind + "_isShowWorkplat").attr("checked", false);
	$("#_show_" + resourcekind + "_super_type_name").html("&nbsp;");
	$("#_show_" + resourcekind + "_super_code").html("&nbsp;");
	$("#_show_" + resourcekind + "_userAccessRule").html("&nbsp;");
	$("#_show_" + resourcekind + "_afterAccess").html("&nbsp;");
	$("#_show_" + resourcekind + "_order").html("&nbsp;");
	$("#_show_" + resourcekind + "_userDefinedId").html("&nbsp;");

}

/**
 * 清除resource 的form数据
 */
function clearResourceFormData(){
	//隐藏域
	$("#resource_id").val("");
	//表单内容
    $("#resource_type").empty();
    $("#resource_name").val("");
    $("#resource_title").val("");
    $("#resource_code").val("");
    $("#resource_needAuthenticate_check").removeAttr("checked");
    $("#resource_needShow_check").removeAttr("checked");
    $("#resource_superType_name").val("");
    $("#resource_accessUrl").val("");
    $("#resource_description").val("");
}

/**
 * 删除选定资源
 */
function deleteResource(){
//	alert("删除资源"+$("#_hidden_resource_id").val());
	var permissionId=$("#_hidden_resource_id").val();
	if(permissionId=="" || permissionId==null){
		alert("请先选定要删除的资源");
		return;
	}
	
	if(confirm("你确定要删除该权限以及其子权限？此操作不能复原！")){
		$.ajax({
		url:'ajaxDeletePermissionAction',
		data:{'permissionId':permissionId},
		dataType:'text',
		success:function(data){
			var obj=eval("("+data+")");
//			alert(obj['flag']);
			if(obj['flag']=="success"){
				alert("删除成功!");
				// refresh the resource tree
				permissionTree();
				//removeResourceTreeNode($("#_hidden_tabVal_id").val(),$("#_hidden_resource_li_id").val());
			}else {
				alert('删除失败!原因：'+obj['msg']);
			}
		}
	});
	}
	
	
}

/**
 * 
 * @param {Object} treeId
 */
function  refreshTree(treeCode){
		//页面类资源树
	$("#"+treeCode+"Tree li span").each(function(){
		$(this).click(function(){
			$("#"+treeCode+"Tree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
}

/**
 * 删除资源树节点
 * @param treeCode
 *        tree的code
 * @param {Object} liid
 *                 节点所在li的id
 */
function removeResourceTreeNode(treeCode,liid){
	var obj=$("#"+liid);
	var parentUl=obj.parent("ul");//ul父节点
	var lis=parentUl.children("li").length;
	var nextSelectedNode;//下一个默认选中的节点
    if(lis>1){
    	if(parentUl.children("li:last").attr("id")==liid){
//    		alert("删除的是处于末尾的节点");
    		obj.remove();
    		nextSelectedNode=parentUl.children("li:last");
    		nextSelectedNode.addClass("last");
    	}else if(parentUl.children("li:first").attr("id")==liid){
//    		alert("删除的是处于起始的节点");
    		obj.remove();
    		nextSelectedNode=parentUl.children("li:first");
    	}else{
//    		alert("删除的是处于中间状态的节点");
    		obj.remove();
    		nextSelectedNode=parentUl.children("li:first");
    	}
    }else{
    	//如果是删除了唯一的节点，则把该节点的上级li作为删除后被选中的节点
    	var parentLiid=$("#"+liid).parent("ul").parent("li").attr("id");
//    	alert("删除的是唯一节点");
    	nextSelectedNode=parentUl.parent("li");
    	var classes=parentUl.parent("li").attr("class");
    	var arr=classes.split(" ");
    	var isLast=true;
    	if(arr && arr.length>0){
    		for(var i in arr){
    			var cls=arr[i];
    			if(cls.indexOf("last")>=0){
    				isLast=true;
    			}
    			if(cls.indexOf('ollapsable')>=0){//可能是collapsable，lastCollapsable
    				nextSelectedNode.removeClass(cls);
    			}
    		}
    	}
    	if(isLast && nextSelectedNode.children("span:first").attr("onclick").indexOf("showResourceDetail")>=0){
    		//确实是资源展示的li
    		nextSelectedNode.addClass("last");
    	}
    	nextSelectedNode.children("div:first").remove();//div不用了
    	parentUl.remove();
    }
    
    //触发点击事件
    if(nextSelectedNode){
    	nextSelectedNode.children("span:first").trigger("click");
    }

}

/**
 * 添加树节点
 * @param {Object} treeCode
 * @param {Object} liid
 * @param {Object} ele
 */
function addResourceTreeNode(treeCode,liid,ele){
	var li=$("#"+liid);//当前选中的父节点
	var ulObj;
//	alert(li.attr("id"));
	if(li.children("ul").length==0){
//		alert("li has no child before");
		var div=$("<div class=\"hitarea collapsable-hitarea lastCollapsable-hitarea\"></div>");
		ulObj=$("<ul></ul>");
		ulObj.append(ele);
		li.append(ulObj);
		div.insertBefore(li.children("span:first"));
		
		//修改li的样式
		li.addClass("collapsable");
		if(li.hasClass("last")){
			li.removeClass("last");
			li.addClass("lastCollapsable");
		}
	}else{
		ulObj=li.children("ul:first");
		ulObj.children("li").each(function(i){
//			alert("i="+i+","+$(this).attr("id"));
			//新加的节点的兄弟节点都不是最后一个了
			if($(this).hasClass("last")){
//				alert("has last class")
				$(this).removeClass("last");
			}
			if($(this).hasClass("lastCollapsable")){
//				alert("has  lastCollapsable class")
				$(this).removeClass("lastCollapsable");
				$(this).children("div:first").removeClass("lastCollapsable-hitarea");
				if(!$(this).hasClass("collapsable")){
					$(this).addClass("collapsable");
				}
			}
		});
		
		ulObj.append(ele);
	}
		//修改样式
		ele.addClass("last");
	
	refreshTree(treeCode);
	
//	li.children("ul:first").children("li:last").trigger("click");
}