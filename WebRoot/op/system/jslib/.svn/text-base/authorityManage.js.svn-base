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
		$(".resources_showBtn").each(function(){
			$(this).click(function() {
				var inputId = $(this).attr("id");
				var code = inputId.substr(inputId.indexOf("_")+1);//项目编码
				if ($(this).attr("value") == '修改') {
					$(".dialog_title").html('修改受控资源');
					prepareForModifyResource(code);
					currentOperation="modify";
					$("#modify_add_resource_form").attr("action","ajaxModifyPermissionAction");
					$("#resources_Dialog").show();
					$(".black").show();
				} else {
					$(".dialog_title").html('添加受控资源');
					prevpareForAddResource("false",code);
					currentOperation="add";
					$("#modify_add_resource_form").attr("action","ajaxAddPermissionAction");
					$("#resources_Dialog").show();
					$(".black").show();
				}
				
			});
			
		})
		
		$(".deleteResource").each(function(){
			$(this).click(function(){
				deleteResource(this);
			});
			
		})
		
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
function showAddTopPerDiv(code){//yuan.yw 系统项目编码code 2014-01-14
    $("#resources_Dialog").show();
	$(".black").show();
	$(".dialog_title").html('添加第一级资源');
	prevpareForAddResource("true",code);
	currentOperation="add";
	$("#modify_add_resource_form").attr("action","ajaxAddPermissionAction");
}
//生成权限树
function permissionTree(){
	//yuan.yw 2014-01-13
	if($("#resources_tab_ul li").size()>0){
		var systemCodes ="";//系统项目编码
		$("#resources_tab_ul li").each(function(){
			systemCodes +=","+$(this).attr("id");
		})
		systemCodes = systemCodes.substring(1);
		var myUrl = "ajaxInitPermissionTreeAction";
		$.post(myUrl,{systemCodes:systemCodes},function(data){//获取权限数据
			$.each(data,function(key,value){
				//生成权限树
				createPermissionTree(value,"permissionTreeDiv_"+key,"ptree_"+key,"permissionInfoDiv_"+key,"showResourceDetail");
				createPermissionTreeByRole(value,"role_permissionTreeDiv_"+key,"rptree_"+key,"","fillHidden");
			})
		},"json");
	}
	
	
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
function showResourceDetail(dataStr,tableId) {//yuan.yw 修改
	var data = eval("(" + dataStr + ")");
	var proCode = tableId.split("_")[1];//系统项目编码 code
	$("#"+tableId+" #_show_page_res_name").html("&nbsp;");
	$("#"+tableId+" #_show_page_accessUrl").html("&nbsp;");
	$("#"+tableId+" #_show_page_parameter").html("&nbsp;");
	$("#"+tableId+" #_show_page_description").html("&nbsp;");
	

	$("#_hidden_resource_code_"+proCode).val("");
	$("#_hidden_resource_name_"+proCode).val("");
	$("#_hidden_resource_title_"+proCode).val("");
	$("#_hidden_resource_needAuthenticate_"+proCode).val("");
	$("#_hidden_resource_isShowWorkplat_"+proCode).val("");
	$("#_hidden_resource_accessUrl_"+proCode).val("");
	$("#_hidden_resource_parameter_"+proCode).val("");
	$("#_hidden_resource_description_"+proCode).val("");
	$("#_hidden_resource_super_id_"+proCode).val("");

	
	$("#"+tableId+" #_show_page_res_name").html(data.NAME);
	$("#"+tableId+" #_show_page_res_code").html(data.CODE);
	$("#"+tableId+" #_show_page_res_title").html(data.TITLE);
	//	alert("needauth = "+needauth)
	if (data.ENALBED == 1) {
		//		alert("check")
		$("#"+tableId+" #_show_page_needAuth").attr("checked", "checked");
	} else {
		//		alert("not check")
		$("#"+tableId+" #_show_page_needAuth").attr("checked", false);
	}
	if (data.IS_SHOW_WORKPLAT == 1) {
		//		alert("check")
		$("#"+tableId+" #_show_page_isShowWorkplat").attr("checked", "checked");
	} else {
		//		alert("not check")
		$("#"+tableId+" #_show_page_isShowWorkplat").attr("checked", false);
	}

	$("#"+tableId+" #_show_page_accessUrl").html(data.URL);
	$("#"+tableId+" #_show_page_parameter").html(data.PARAMETER);
	$("#"+tableId+" #_show_page_description").html(data.DESCRIPTION);

	//放进隐藏域

	$("#_hidden_resource_id_"+proCode).val(data.PID);
	$("#_hidden_resource_code_"+proCode).val(data.CODE);
	$("#_hidden_resource_name_"+proCode).val(data.NAME);
	$("#_hidden_resource_title_"+proCode).val(data.TITLE);
	$("#_hidden_resource_needAuthenticate_"+proCode).val(data.ENALBED);
	$("#_hidden_resource_isShowWorkplat_"+proCode).val(data.IS_SHOW_WORKPLAT);
	$("#_hidden_resource_accessUrl_"+proCode).val(data.URL);
	$("#_hidden_resource_parameter_"+proCode).val(data.PARAMETER);
	$("#_hidden_resource_description_"+proCode).val(data.DESCRIPTION);
	$("#_hidden_resource_super_id_"+proCode).val(data.PARENTID);
	$("#modify_"+proCode).removeAttr("disabled");
	$("#delete_"+proCode).removeAttr("disabled");
	$("#add_"+proCode).removeAttr("disabled");
}

/**
 * 准备修改资源需要的信息
 * yuan.yw 2014-01-14
 */
function prepareForModifyResource(code) {//code 2014-01-14 yuan.yw 项目编码
	
	/*$("#resource_type").html(
			"<option value=\"" + $("#_hidden_resource_type_id").val()
					+ "\" selected='true'>"
					+ $("#_hidden_resource_type_name").val() + "</option>");
	$("#resource_type").attr("disabled", false);*/
	
	$("#resource_proCode").val(code);
	$("#resource_type").val(code+"_MenuResource");
	$("#resource_name").val($("#_hidden_resource_name_"+code).val());
	$("#resource_title").val($("#_hidden_resource_title_"+code).val());
	$("#resource_code").val($("#_hidden_resource_code_"+code).val());
	if ($("#_hidden_resource_needAuthenticate_"+code).val() == '1') {
		$("#resource_needAuthenticate").attr("checked", "checked");
	} else {
		$("#resource_needAuthenticate").attr("checked", false);
	}
	if ($("#_hidden_resource_isShowWorkplat_"+code).val() == '1') {//是否加入到个人工作台
		$("#resource_isShowWorkplat").attr("checked", "checked");
	} else {
		$("#resource_isShowWorkplat").attr("checked", false);
	}
	/*$("#resource_superType_name_"+code).val($("#_hidden_super_resource_type_name").val());
	$("#resource_superType_name").attr("disabled", true);*/
	//$("#resource_superCode").val($("#_hidden_resource_super_code").val());
	//$("#resource_superCode").attr("disabled", true);
	//$("#resource_userDefineAccessRule").val(
	//		$("#_hidden_resource_userDefineAccessRule").val());
	//$("#resource_afterShowResource").val(
			//$("#_hidden_resource_afterShowResource").val());
	//$("#resource_order").val($("#_hidden_resource_order").val());
	//$("#resource_userDefineId").val($("#_hidden_resource_userDefineId").val());
	//从隐藏域获取
	$("#resource_id").val($("#_hidden_resource_id_"+code).val());
	$("#resource_accessUrl").val($("#_hidden_resource_accessUrl_"+code).val());
	$("#resource_parameter").val($("#_hidden_resource_parameter_"+code).val());
	$("#resource_description").val($("#_hidden_resource_description_"+code).val());
	//tab变量
	//$("#tabVal").val($("#_hidden_tabVal_id").val());
	//上级资源code
	//$("#superResourceCode").val($("#_hidden_resource_super_code").val());
	return true;
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
					var proCode = $("#resource_proCode").val();
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
	                    	//permissionTree();
	                    	getPermissionTreeByCode(proCode);//yuan.yw 2014-01-14

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
	                    	
	                    	//permissionTree();
	                    	getPermissionTreeByCode(proCode);//yuan.yw 2014-01-14
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
 * 
 *根据项目编码获取权限树 (资源修改时)
 * @param {} code
 */
function getPermissionTreeByCode(code){
	var myUrl = "ajaxInitPermissionTreeAction";
	$.post(myUrl,{systemCodes:code},function(data){//获取权限数据
		$.each(data,function(key,value){
			//生成权限树
			createPermissionTree(value,"permissionTreeDiv_"+key,"ptree_"+key,"permissionInfoDiv_"+key,"showResourceDetail");
			createPermissionTreeByRole(value,"role_permissionTreeDiv_"+key,"rptree_"+key,"","fillHidden");
			$("#ptree_"+key+" span[id='"+$("#resource_id").val()+"']").click();//树节点选中效果
		})
	},"json");
}

/**
 * 准备添加资源的信息
 */
function prevpareForAddResource(isRoot,code) {//yuan.yw 2014-01-14 code 项目编码
	
	//清除form的数据
	clearResourceFormData();
	
	//默认选中要验证
	$("#resource_needAuthenticate_check").attr("checked","checked");
	$("#resource_proCode").val(code);
	$("#resource_type").val(code+"_MenuResource");
	if(isRoot=="false"){
		
		//隐藏域初始化
		$("#superResourceId").val($("#_hidden_resource_id_"+code).val());//当前选择的资源就是待添加资源的上级资源		
	}else{
		$("#superResourceId").val("");
	}
	return true;
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
function clearResourceFormData(){//yuan.yw 2014-01-14
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
    $("#resource_proCode").val("");//项目编码清空
    $("#resource_type").val("");

}

/**
 * 删除选定资源
 */
function deleteResource(me){//yuan.yw 2014-01-14
//	alert("删除资源"+$("#_hidden_resource_id").val());
	var id = $(me).attr("id");
	var code = id.substr(id.indexOf("_")+1);
	var permissionId=$("#_hidden_resource_id_"+code).val();
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

/**
 * 清除空格
 * @param {} me
 */
function trim(me){   
	var str = $(me).val();
    str = str.replace(/^(\s|\u00A0)+/,'');   
    for(var i=str.length-1; i>=0; i--){   
        if(/\S/.test(str.charAt(i))){   
            str = str.substring(0, i+1);   
            break;   
        }   
    }   
    $(me).val(str);
}
