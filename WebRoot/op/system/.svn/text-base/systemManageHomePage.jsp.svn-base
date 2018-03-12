<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统权限管理</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css"/>
<link rel="stylesheet" type="text/css" href="../../css/public.css"/>
<link rel="stylesheet" type="text/css" href="css/systemManage.css"/>
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
<link rel="stylesheet" type="text/css" href="../css/leftMenu.css"/>
<link rel="stylesheet" type="text/css" href="../../jslib/dialog/dialog.css" />

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="jslib/tab.js"></script>
<script type="text/javascript" src="<%=basePath%>op/system/jslib/authorityManage.js"></script>
<script type="text/javascript" src="jslib/authorityrole.js"></script>
<script type="text/javascript" src="jslib/authorityAccount.js"></script>
<script type="text/javascript" src="../js/leftMenu.js"></script>
<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js "></script>

<script type="text/javascript">

	$(function(){
		tab("resources_tab","li","onclick");//资源系统tab切换 yuan.yw
		tab("systemService_tab","li","onclick");//项目服务范围类别切换

			
		/*var myUrl = "ajaxInitPermissionTreeAction";
		$.post(myUrl,function(data){
			createPermissionTreeByRole(data,"role_permissionTreeDiv","rptree","","fillHidden");
		},"json");*/
		
		
		$("#_hidden_PM_orgrole_role_id").val("");//选择的角色的id
		$("#_hidden_PM_orgrole_li_id").val("");//选择的角色所在li的id
		$("#role_rela_per_PM_orgrole_roleId").val("");
		
		//信息编辑区域的信息填充
		$("#PM_orgrole_role_id").val("");
		$("#PM_orgrole_modify_role_code").val("");
		$("#PM_orgrole_modify_role_name").val("");
		
		//新增区域的信息清除
		$("#PM_orgrole_add_role_name").val("");
		$("#PM_orgrole_add_role_code").val("");
		
		$("#_hidden_IOSM_orgrole_role_id").val("");//选择的角色的id
		$("#_hidden_IOSM_orgrole_li_id").val("");//选择的角色所在li的id
		$("#role_rela_per_IOSM_orgrole_roleId").val("");
		
		//信息编辑区域的信息填充
		$("#IOSM_orgrole_role_id").val("");
		$("#IOSM_orgrole_modify_role_code").val("");
		$("#IOSM_orgrole_modify_role_name").val("");
		
		//新增区域的信息清除
		$("#IOSM_orgrole_add_role_name").val("");
		$("#IOSM_orgrole_add_role_code").val("");
		
		$("#_hidden_RNO_orgrole_role_id").val("");//选择的角色的id
		$("#_hidden_RNO_orgrole_li_id").val("");//选择的角色所在li的id
		$("#role_rela_per_RNO_orgrole_roleId").val("");
		
		//信息编辑区域的信息填充
		$("#RNO_orgrole_role_id").val("");
		$("#RNO_orgrole_modify_role_code").val("");
		$("#RNO_orgrole_modify_role_name").val("");
		
		//新增区域的信息清除
		$("#RNO_orgrole_add_role_name").val("");
		$("#RNO_orgrole_add_role_code").val("");
		
	})
	
	function clear(type)
	{

		//隐藏域的填充
		$("#_hidden_"+type+"_orgrole_role_id").val("");//选择的角色的id
		$("#_hidden_"+type+"_orgrole_li_id").val("");//选择的角色所在li的id
		$("#role_rela_per_"+type+"_orgrole_roleId").val("");
		
		//信息编辑区域的信息填充
		$("#"+type+"_orgrole_role_id").val("");
		$("#"+type+"_orgrole_modify_role_code").val("");
		$("#"+type+"_orgrole_modify_role_name").val("");
		
		//新增区域的信息清除
		$("#"+type+"_orgrole_add_role_name").val("");
		$("#"+type+"_orgrole_add_role_code").val("");
		
	}
	
	
	/**
	 * 递归生成权限树
	 */
	function foreachInitPermissionByRoleTree(data,ul,tableId,functionName) {
		//var reg=new RegExp(" ","g"); //创建正则RegExp对象     yuan.yw add 
		for(var i=0;i<data.length;i++){
			//循环生成
			var data2 = data[i];
			var dataStr = ObjectToStr(data2);
			//dataStr=dataStr.replace(reg,"&nbsp;");
			//判断有无子级菜单
			var childTree = data2.children;
			var auth;
			var perAccessText = "";
			/* if(data2.permission.PER_ACCESS != null && data2.permission.PER_ACCESS.indexOf('read') >= 0){
				perAccessText = perAccessText + "查看/";
			}
			if(data2.permission.PER_ACCESS != null && data2.permission.PER_ACCESS.indexOf('write') >= 0){
				perAccessText = perAccessText + "编辑/";
			} */
			
			var parent = "";
			
			if(data2.PARENTID!=null)
			{
				parent = "parentid=\""+data2.PARENTID+"\"" ;
			}
			
			auth="<input type=\"checkbox\" class=\"permission\" name=\"permission\"   id=\"permission"+data2.PID+"\" "+parent+" value=\""+data2.PID+"\"/>";
			
			if(childTree == undefined || childTree.length==0){
				var li = $("<li><span class='folder' per='"+data2.PER_ACCESS+"' onclick="+functionName+"('"+dataStr+"','"+tableId+"',this);changeColor(this)>"+data2.NAME+auth+"</span></li>");
				li.appendTo(ul);
			}else{
				var li = $("<li class='closed'><span class='folder' per='"+data2.PER_ACCESS+"' onclick="+functionName+"('"+dataStr+"','"+tableId+"',this);changeColor(this)>"+data2.NAME+auth+"</span></li>");
				li.appendTo(ul);
				var ul2 = $("<ul></ul>");
				foreachInitPermissionByRoleTree(childTree,ul2,tableId,functionName);
				ul2.appendTo(li);
			}
		}
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
		
		
		//checkbox 按钮联动选择
		if(data.PARENTID!=null && data.PARENTID != "")
		{
			var nodes = $(me).parent().parent().find("input[type='checkbox']:checked");
			
			if(nodes.size()>0)
			{
				$("#permission"+data.PARENTID).attr("checked","checked");	
			}
			else
			{
				$("#permission"+data.PARENTID).removeAttr("checked");
			}
		}
		else
		{
			var checked = $(me).find("input").attr("checked");
			
			if(checked == "checked")
			{
				$("input[parentid="+data.PID+"]").attr("checked","checked");	
			}
			else
			{
				$("input[parentid="+data.PID+"]").removeAttr("checked");
			}
			
		}
		
	}
	
	/**
	 * 保存对角色的授权
	 * 角色id从隐藏域取出
	 * @param {Object} rolekind
	 * @param {Object} resourcekind
	 */
	function saveRoleRelaPermission(type){
		var role_id=$("#role_rela_per_"+type+"_orgrole_roleId").val();//选择的角色的id
		if(role_id==""){
			alert("请先选择某个角色进行授权");
			return;
		}
		
		var checkboxs = $("#rptree_"+type).find("input[type='checkbox']:checked");
		
		var permissionIds = "";
		
		$.each(checkboxs,function(){
			
			permissionIds += $(this).val()+",";
			
		})
		
		permissionIds = permissionIds.substring(0,permissionIds.length-1);
		
		//var permissionIds=$("#role_rela_per_perId").val();
		/* if(permissionIds==""){
			alert("请先选择要对角色进行授权的权限");
			return;
		} */
		/* 
		var perAccess = "";
		$("input[name='perAccess']:checked").each(function(){
			perAccess = perAccess + $(this).val() + "/";
		}); */
		$.ajax({
			url: "ajaxSaveRoleRelaPermissionsAction",
			async:false,
			type:"POST",
			data:{roleId:role_id,permissionIds:permissionIds},
			success : function(result) {
				result = eval("("+result+")");
				if(result['flag']==true){
					alert("保存授权成功!");
					/* var perAccessText = "";
					
					/* $("input[name='perAccess']:checked").each(function(){
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
					$(".on_selected").attr("per",perAccess); */
				}else{
					alert("保存授权失败!");
				}
			}
		});
		
	}
	 
	 
	function chooseRole(obj,rolekind,roleid,rolecode,rolename,systemcode)
	{
		rolecode=decodeURIComponent(rolecode);
		rolename=decodeURIComponent(rolename);
		
		$(obj).addClass("selected");
		$(obj).siblings("li").each(function(i)
		{
			$(this).removeClass("selected");
		});
		
		//隐藏域的填充
		$("#_hidden_role_roleType_code").val(rolekind);//选择的角色类型
		$("#_hidden_"+rolekind+"_role_id").val(roleid);//选择的角色的id
		$("#_hidden_"+rolekind+"_li_id").val($(obj).attr("id"));//选择的角色所在li的id
		$("#role_rela_per_"+rolekind+"_roleId").val(roleid);
		
		//信息编辑区域的信息填充
		$("#"+rolekind+"_role_id").val(roleid);
		$("#"+rolekind+"_modify_role_code").val(rolecode);
		$("#"+rolekind+"_modify_role_name").val(rolename);
		
		var myUrl = "ajaxLoadPermissionTreeByRoleAction";
		$.post(myUrl,{roleId:roleid},function(data){
			
			var checked = $("#rptree_"+systemcode).find("input[type='checkbox']:checked");
			
			$.each(checked,function(){
				
				$(this).removeAttr("checked");
				
			})
			
			if(data!=null)
			{
				$.each(data,function(k,v){
					$("#permission"+v.PERMISSION_ID).attr("checked","checked");
				})	
			}
			
			
		},"json");
		
		
	}

	
</script>

</head>
<body>&nbsp;  
	<%--主体开始--%>
	<div class="systemManage_main">
		<%--左边菜单栏开始--%>
		<div class="leftMenu">
			<div class="leftMenu_top">
				<span>系统</span>
			</div>
			<ul class="leftLevelMenu_info">
				<li class="menu_selected" onclick="showDiv(this,'resource_manage_div')"><span></span>受控资源注册</li>
				<li onclick="showDiv(this,'role_manage_div')"><span></span>角色管理</li>
				<%--<li onclick="showDiv(this,'account_manage_div')"><span></span>用户帐号管理</li>--%>
			</ul>
		</div>
		<%--显示/隐藏左边栏--%>
		<div class="menu_title_tool">
			<div class="tool"></div>
		</div>
		<%--左边菜单栏结束--%>

			<%--右边工作区开始--%>
		<%-- ########################################################## --%>
			<%-- 资源管理开始 --%>
			<div id="resource_manage_div" class=".manageOutterDiv">
				<%-- 工作区开始 --%>
				<div class="systemManage_content">
					<div class="systemManage_top">
						受控资源注册
					</div>
					<div class="systemManage_resources">
						
						<div id="resources_tab" class="projectService_tab tab_menu" style="padding-top:10px; padding-left:10px">
	                           <ul id="resources_tab_ul">
	                           		<s:if test="systemList != null && systemList.size() > 0">
			  							<s:iterator id="system" value="systemList" status="st">
			  								<s:if test="#st.index==0">
			  									<li class="selected" id="${system.CODE}">${system.NAME}</li>
			  								</s:if>
			  								<s:else>
			  									<li id="${system.CODE}" >${system.NAME}</li>
			  								</s:else>
			  						 	</s:iterator>
			  						 </s:if>
	                           </ul>
		                </div>              
						<div class="resources_content clearfix"  >
							<s:if test="systemList != null && systemList.size() > 0">
	  							<s:iterator id="system" value="systemList" status="st">
	  								<s:if test="#st.index==0">
	  									<div id="resources_tab_${st.index}">
	  								</s:if>
	  								<s:else>
	  									<div id="resources_tab_${st.index}" style="display:none">
	  								</s:else>
										<%--左边树开始--%>
										<div class="resourcesType_tree" >
											<em class="add_root_permission" onclick="showAddTopPerDiv('${system.CODE}');"></em>
											<div id="permissionTreeDiv_${system.CODE}"></div>
										</div>
						                <%--左边树结束--%>
										<div class="resourcesType_content" id="permissionInfoDiv_${system.CODE}">
											<h2>
												受控资源详细信息
											</h2>
											<ul>
												<li>
													<label>
														名称：
													</label>
													<span id="_show_page_res_name">&nbsp;</span>
												</li>
												<li>
													<label>
														标题：
													</label>
													<span id="_show_page_res_title">&nbsp;</span>
												</li>
												<li>
													<label>
														编码：
													</label>
													<span id="_show_page_res_code">&nbsp;</span>
												</li>
												
												<li>
													<label>
														是否需要验证：
													</label>
													<span>
													<input id="_show_page_needAuth" type="checkbox"
														checked="checked" disabled="true">
														需要验证</input>
													</span>
												</li>
												<li>
													<label>
														是否加入个人工作台：
													</label>
													<span>
													<input id="_show_page_isShowWorkplat" type="checkbox" disabled="true">
														加入</input>
													</span>
												</li>
												<li>
													<label>
														页面/资源链接：
													</label>
													<span id="_show_page_accessUrl">&nbsp;</span>
												</li>
												<li>
													<label>
														链接参数：
													</label>
													<span id="_show_page_parameter">&nbsp;</span>
												</li>
												<li>
													<label>
														说明：
													</label>
													<span id="_show_page_description">&nbsp;</span>
												</li>
											</ul>
											<div class="resourcesType_but">
												<input type="button" class="input_button resources_showBtn"
													value="修改" id="modify_${system.CODE}" disabled="disabled" />
												<input type="button" class="input_button deleteResource" value="删除" id="delete_${system.CODE}" disabled="disabled" />
												<input type="button" class="input_button resources_showBtn"
													value="添加下级" id="add_${system.CODE}" disabled="disabled" />
											</div>
										</div>
										<%-- 隐藏域 --%>
										<div id="hiddenArea_${system.CODE}">
											<input type="hidden" id="_hidden_module_id_${system.CODE}" value="" />
											<input type="hidden" id="_hidden_module_code_${system.CODE}" value="" />
											<input type="hidden" id="_hidden_module_name_${system.CODE}" value="" />

											<input type="hidden" id="_hidden_resource_id_${system.CODE}" value="" />
											<input type="hidden" id="_hidden_resource_code_${system.CODE}" value="" />
											<input type="hidden" id="_hidden_resource_name_${system.CODE}" value="" />
											<input type="hidden" id="_hidden_resource_title_${system.CODE}" value="" />
											<input type="hidden" id="_hidden_resource_needAuthenticate_${system.CODE}"
												value="" />
											<input type="hidden" id="_hidden_resource_isShowWorkplat_${system.CODE}" value="" />
											<input type="hidden" id="_hidden_resource_accessUrl_${system.CODE}"
												value="" />
											<input type="hidden" id="_hidden_resource_parameter_${system.CODE}" value="" />
											<input type="hidden" id="_hidden_resource_description_${system.CODE}"
												value="" />
											<input type="hidden" id="_hidden_resource_super_id_${system.CODE}" value="" />

											<%-- 当前选择的树节点的li的id --%>
											<input type="hidden" id="_hidden_resource_li_id_${system.CODE}" value="" />
										</div>
									</div>
	  						 	</s:iterator>
	  						 </s:if>
						</div>
					</div>


					<%--基本信息弹出框--%>
					<div class="black"></div>
					<div id="resources_Dialog" class="dialog role_dialog"
						style="display: none;">
						<div class="dialog_header">
							<div class="dialog_title">
								修改受控资源
							</div>
							<div class="dialog_tool">
								<div class="dialog_tool_close dialog_closeBtn"></div>
							</div>
						</div>
						<form action="" id="modify_add_resource_form" method="post" target="stay">
							<div class="dialog_content">
								<input type="hidden" id="resource_id" name="sysPermission.permissionId" />
								<input type="hidden" id="resource_needAuthenticate" name="sysPermission.enalbed"/>
								<input type="hidden" id="resource_isShowWorkplat" name="sysPermission.isShowWorkplat"/>
								<input type="hidden" id="superResourceId" name="sysPermission.parentId"/>
								<input type="hidden" id="resource_proCode" name="sysPermission.proCode"/>
								<input type="hidden" id="resource_type" name="sysPermission.type" />
								<ul class="role_dialog_info">
									<li>
										<label>
											名称：
										</label>
										<span><input type="text" name="sysPermission.name" id="resource_name" class="{required:true,maxlength:30}" onblur="trim(this);">
										</span><em class="redStar">*</em>
									</li>
									<li>
										<label>
											标题：
										</label>
										<span><input type="text" name="sysPermission.title" id="resource_title" class="{maxlength:30}" onblur="trim(this);">
										</span>
										<p align="center" style="color:red;font-size:10px">(此标题是页面中左侧菜单中的标题名称)
										</p>
									</li>
									<li>
										<label>
											编码：
										</label>
										<span><input type="text" name="sysPermission.code" id="resource_code" class="{required:true,maxlength:30}" onblur="trim(this);">
										</span><em class="redStar">*</em>
									</li>
									<li>
										<label>
											是否需要验证：
										</label>
										<span>
										<input type="checkbox" id="resource_needAuthenticate_check" checked="checked">
												需要验证
										</span>
									</li>
									<li>
										<label>
											是否加入个人工作台：
										</label>
										<span>
										<input type="checkbox" id="resource_isShowWorkplat_check">
												加入<em style="color:red;font-size:10px">(是否加入到我的首页=>个人工作台)</em>
										</span>
									</li>
									<li>
										<label>
											页面/资源链接：
										</label>
										<span><input type="text"
												id="resource_accessUrl"
												name="sysPermission.url" class="{required:true,maxlength:200}" onblur="trim(this);">
										</span><em class="redStar">*</em>
									</li>
									<li>
										<label>
											链接参数：
										</label>
										<span><input type="text"
												id="resource_parameter"
												name="sysPermission.parameter" class="{maxlength:200}" onblur="trim(this);">
										</span>
									</li>
									<li>
										<label>
											说明：
										</label>
										<span><input type="text"
												id="resource_description"
												name="sysPermission.note" class="{maxlength:100}" onblur="trim(this);">
										</span>
									</li>
								</ul>
								<div class="dialog_but">
									<button class="aui_state_highlight" id="confirmAddOrModify">
										确定
									</button>
									<button class="aui_state_highlight" id="cancelAddOrModify">
										取消
									</button>
								</div>
							</div>
						</form>
					</div>

					
				</div>
				<%-- 工作区结束 --%>
			</div>
			<%-- 资源管理结束 --%>
		<%-- ########################################################## --%>	
			
		<%-- ########################################################## --%>
		    <div id="roleHiddenArea">
		      <%-- tab共用  --%>
		      <%-- 选择的是哪种类型的角色的tab --%>
		      <input type="hidden" id="_hidden_role_roleType_code" />
		      
		      <%-- 各tab专用 --%>
		      <%-- 用户群类型当前选择的角色id --%>
		      <input type="hidden" id="_hidden_usergrouprole_role_id" />
		      <input type="hidden" id="_hidden_usergrouprole_li_id" />
		      
		      
		      <%-- 业务角色类型当前选择的角色id --%>
		      <input type="hidden" id="_hidden_businessrole_role_id" />
		      <input type="hidden" id="_hidden_businessrole_li_id" />
		      
		    </div>	
			<%-- 角色管理开始 --%>
			<div id="role_manage_div" style="display: none;" class=".manageOutterDiv">
				<%-- 工作区开始 --%>
				<div class="systemManage_content">
					<div class="systemManage_top">
						角色管理
					</div>
					<div class="systemManage_resources">
						<div class="role_content clearfix">
							<div id="role_tab_1" >
								<div id="systemService_tab" class="tab_menu">
									<ul>
	
										<s:if test="systemList != null && systemList.size() > 0">
			  							<s:iterator id="system" value="systemList" status="st">
			  								<s:if test="#st.index==0">
			  									<li class="selected" id="tab_${st.index}">${system.NAME}</li>
			  								</s:if>
			  								<s:else>
			  									<li id="tab_${st.index}" >${system.NAME}</li>
			  								</s:else>
			  						 	</s:iterator>
			  						 </s:if>
									</ul>
								</div>
								<s:if test="systemList != null && systemList.size() > 0">
	  							<s:iterator id="system" value="systemList" status="st">
	  								<s:if test="#st.index==0">
	  									<div id="systemService_tab_${st.index}" class="system_tab">
	  								</s:if>
	  								<s:else>
	  									<div id="systemService_tab_${st.index}" class="system_tab" style="display:none">
	  								</s:else>
									<div class="role_userGroups">
										<h3>
											角色：
										</h3>
											<s:if test="#system.CODE=='PM'">
												${orgRoleUlStr}
											</s:if>
											<s:if test="#system.CODE=='IOSM'">
												${iosmOrgRoleUlStr}
											</s:if>
											<s:if test="#system.CODE=='RNO'">
												${rnoOrgRoleUlStr}
											</s:if>
											
										<div class="role_modify">
											<div class="roleAdd_dialog role_org_dialog" style="width:372px;">
											<form id="${system.CODE}_orgrole_add_role_form" action="ajaxAddRoleAction" method="post" target="stay">
												<input id="add_roleTypeId" name="sysRole.roleTypeId" type="hidden" value="" />
												<h3>
													添加角色
												</h3>
												<ul>
													<li>
														<label>
															角色名称
															<em class="redStar">*</em>：
														</label>
														<span><input id="${system.CODE}_orgrole_add_role_name" name="sysRole.name" type="text" class="{required:true,maxlength:30}" value="" />
														</span>
													</li>
													<li>
														<label>
															角色编码
															<em class="redStar">*</em>：
														</label>
														<span><input type="text" id="${system.CODE}_orgrole_add_role_code" name="sysRole.code" class="{required:true,maxlength:30}" value="" />
														</span>
													</li>
													<li class="roleBut">
														<input type="submit" id="orgrole_add_role_confirm_btn" class="input_button" value="保存" />
														<input type="button" id="orgrole_add_role_cancel_btn" class="input_button role_org_closeBtn"
															value="放弃" />
														<input type="hidden" name="sysRole.proCode" value="${system.CODE}"/>
													</li>
												</ul>
												</form>
											</div>
											<form id="${system.CODE}_orgrole_modify_role_form" action="ajaxModifyRoleAction" method="post" target="stay">
												<input type="hidden" id="${system.CODE}_orgrole_role_id" name="sysRole.roleId" />
												<input id="modify_roleTypeId" name="sysRole.roleTypeId" type="hidden" value="" />
												<ul>
													<li>
														<label>
															角色名称
															<em class="redStar">*</em>：
														</label>
														<span><input id="${system.CODE}_orgrole_modify_role_name" name="sysRole.name" type="text" class="{required:true,maxlength:30} " value="" />
														</span>
													</li>
													<li>
														<label>
															角色编码
															<em class="redStar">*</em>：
														</label>
														<span><input id="${system.CODE}_orgrole_modify_role_code" name="sysRole.code" type="text" class="{required:true,maxlength:30} " value="" />
														</span>
													</li>
													<li class="roleBut">
														<input type="button" id="orgrole_add_role_btn" class="input_button role_org_showBtn" onclick="addRole('${system.CODE}')"   value="添加" />
														<input type=submit id="orgrole_modify_role_confirm_btn" class="input_button" onclick="modifyRole('${system.CODE}');" value="修改并保存" />
														<input type="button" id="orgrole_delete_role_btn" class="input_button" onclick="deleteRole('${system.CODE}')"    value="删除" />
														<%-- 组织角色类型当前选择的角色id --%>
													    <input type="hidden" id="_hidden_${system.CODE}_orgrole_role_id" />
													    <input type="hidden" id="_hidden_${system.CODE}_orgrole_li_id" />
													    <input type="hidden" name="sysRole.proCode" value="${system.CODE}"/>
													</li>
												</ul>
											</form>
										</div>
									</div>
									<div class="role_authorize">
										<div id="role_org_tab" class="role_authorize_tab tab_menu">
											<h3>
												角色授权：
											</h3>
											<ul>
												<li class="selected">
													权限
												</li>
											</ul>
										</div>
										<div id="orgrole_function_role_authorize_tab">
											<div class="filetree role_authorizeTree" id="role_permissionTreeDiv_${system.CODE}">
												
											</div>
												<input type="hidden" id="role_rela_per_${system.CODE}_orgrole_perId" />
												<input type="hidden" id="role_rela_per_${system.CODE}_orgrole_roleId"/>
												<div class="role_authorizeBut" id="orgrole_function_oper_div">
													<%-- <input type="checkbox" name="perAccess" value="read" id="read_checkbox">授权</input>  --%>
													<span><input type="button" class="" value="保存授权" id="orgrole_function_oper_save_btn" onclick="saveRoleRelaPermission('${system.CODE}')"/> </span>
												</div>
										</div>
									</div>
								</div>
	  						 </s:iterator>
	  						 </s:if>
							</div>
						</div>
					</div>

				</div>
				<%-- 工作区结束 --%>
			</div>
			<%-- 角色管理结束 --%>
		<%-- ########################################################## --%>
		
			<%--右边工作区结束--%>

		</div>
		
</body>
</html>