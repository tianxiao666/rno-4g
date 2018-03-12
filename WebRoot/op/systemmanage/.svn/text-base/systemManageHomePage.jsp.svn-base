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
<script type="text/javascript" src="jslib/authorityManage.js"></script>
<script type="text/javascript" src="jslib/authorityrole.js"></script>
<script type="text/javascript" src="jslib/authorityAccount.js"></script>
<script type="text/javascript" src="../js/leftMenu.js"></script>
<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js "></script>



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
						
						
						<div class="resources_content clearfix"  >
							
							<div id="resources_tab_0">
								<%--左边树开始--%>
								<div class="resourcesType_tree" >
									<em class="add_root_permission" onclick="showAddTopPerDiv();"></em>
									<div id="permissionTreeDiv"></div>
								</div>
				                <%--左边树结束--%>
								<div class="resourcesType_content" id="permissionInfoDiv">
									<h2>
										受控资源详细信息
									</h2>
									<ul>
										<li>
											<label>
												类型：
											</label>
											<span id="_show_page_resType_name">&nbsp;</span>
										</li>
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
												上级资源类型：
											</label>
											<span id="_show_page_super_type_name">&nbsp;</span>
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
											value="修改" />
										<input type="button" class="input_button deleteResource" value="删除" />
										<input type="button" class="input_button resources_showBtn"
											value="添加下级" />
									</div>
								</div>
							</div>
							
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
								<ul class="role_dialog_info">
									<li>
										<label>
											类型：
										</label>
										<span> <select class="" id="resource_type" name="sysPermission.perTypeId">
											</select> </span>
									</li>
									<li>
										<label>
											名称：
										</label>
										<span><input type="text" name="sysPermission.name" id="resource_name" class="{required:true,maxlength:30}">
										</span><em class="redStar">*</em>
									</li>
									<li>
										<label>
											标题：
										</label>
										<span><input type="text" name="sysPermission.title" id="resource_title" class="{maxlength:30}">
										</span>
										<p align="center" style="color:red;font-size:10px">(此标题是页面中左侧菜单中的标题名称)
										</p>
									</li>
									<li>
										<label>
											编码：
										</label>
										<span><input type="text" name="sysPermission.code" id="resource_code" class="{required:true,maxlength:30}">
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
											上级资源类型：
										</label>
										<span><input type="text" id="resource_superType_name">
										</span>
									</li>
									<li>
										<label>
											页面/资源链接：
										</label>
										<span><input type="text"
												id="resource_accessUrl"
												name="sysPermission.url" class="{required:true,maxlength:200}">
										</span><em class="redStar">*</em>
									</li>
									<li>
										<label>
											链接参数：
										</label>
										<span><input type="text"
												id="resource_parameter"
												name="sysPermission.parameter" class="{maxlength:200}">
										</span>
									</li>
									<li>
										<label>
											说明：
										</label>
										<span><input type="text"
												id="resource_description"
												name="sysPermission.description" class="{maxlength:100}">
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

					<%-- 隐藏域 --%>
					<div id="hiddenArea">
						<input type="hidden" id="_hidden_module_id" value="" />
						<input type="hidden" id="_hidden_module_code" value="" />
						<input type="hidden" id="_hidden_module_name" value="" />
						<input type="hidden" id="_hidden_resource_type_id" value="" />
						<input type="hidden" id="_hidden_resource_type_name" value="" />
						<input type="hidden" id="_hidden_resource_id" value="" />
						<input type="hidden" id="_hidden_resource_code" value="" />
						<input type="hidden" id="_hidden_resource_name" value="" />
						<input type="hidden" id="_hidden_resource_title" value="" />
						<input type="hidden" id="_hidden_resource_needAuthenticate"
							value="" />
						<input type="hidden" id="_hidden_resource_isShowWorkplat" value="" />
						<input type="hidden" id="_hidden_resource_accessUrl"
							value="" />
						<input type="hidden" id="_hidden_resource_parameter" value="" />
						<input type="hidden" id="_hidden_resource_description"
							value="" />
						<input type="hidden" id="_hidden_resource_super_id" value="" />
						<input type="hidden" id="_hidden_super_resource_type_name" value="" />
						<%-- 当前选择的树节点的li的id --%>
						<input type="hidden" id="_hidden_resource_li_id" value="" />
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
		      
		      <%-- 组织角色类型当前选择的角色id --%>
		      <input type="hidden" id="_hidden_orgrole_role_id" />
		      <input type="hidden" id="_hidden_orgrole_li_id" />
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
								<div class="role_top">
									<span>用户群<em class="redStar">*</em>：</span>
									${orgrole_usergroupStr}
								</div>
								<div class="role_userGroups">
									<h3>
										角色：
									</h3>
									${orgRoleUlStr}
									<div class="role_modify">
										<div class="roleAdd_dialog role_org_dialog" style="width:372px;">
										<form id="orgrole_add_role_form" action="ajaxAddRoleAction" method="post" target="stay">
											<input id="add_roleTypeId" name="sysRole.roleTypeId" type="hidden" value="" />
											<h3>
												添加角色
											</h3>
											<ul>
												<li>
													<label>
														角色类型
														<em class="redStar">*</em>：
													</label>
													<span> ${orgroleRoleTypeStr}
												    </span>
												</li>
												<li>
													<label>
														角色名称
														<em class="redStar">*</em>：
													</label>
													<span><input id="orgrole_add_role_name" name="sysRole.name" type="text" class="{required:true,maxlength:30}" value="" />
													</span>
												</li>
												<li>
													<label>
														角色编码
														<em class="redStar">*</em>：
													</label>
													<span><input type="text" id="orgrole_add_role_code" name="sysRole.code" class="{required:true,maxlength:30}" value="" />
													</span>
												</li>
												<li class="roleBut">
													<input type="submit" id="orgrole_add_role_confirm_btn" class="input_button" value="保存" />
													<input type="button" id="orgrole_add_role_cancel_btn" class="input_button role_org_closeBtn"
														value="放弃" />
												</li>
											</ul>
											</form>
										</div>
										<form id="orgrole_modify_role_form" action="ajaxModifyRoleAction" method="post" target="stay">
										<input type="hidden" id="orgrole_role_id" name="sysRole.roleId" />
										<input id="modify_roleTypeId" name="sysRole.roleTypeId" type="hidden" value="" />
										<ul>
											<li>
												<label>
													角色名称
													<em class="redStar">*</em>：
												</label>
												<span><input id="orgrole_modify_role_name" name="sysRole.name" type="text" class="{required:true,maxlength:30}" value="" />
												</span>
											</li>
											<li>
												<label>
													角色编码
													<em class="redStar">*</em>：
												</label>
												<span><input id="orgrole_modify_role_code" name="sysRole.code" type="text" class="{required:true,maxlength:30}" value="" />
												</span>
											</li>
											<li class="roleBut">
												<input type="button" id="orgrole_add_role_btn" class="input_button role_org_showBtn"
													value="添加" />
												<input type=submit id="orgrole_modify_role_confirm_btn" class="input_button" value="修改并保存" />
												<input type="button" id="orgrole_delete_role_btn" class="input_button" value="删除" />
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
										<div class="filetree role_authorizeTree" id="role_permissionTreeDiv">
											
										</div>
											<input type="hidden" id="role_rela_per_perId" />
											<input type="hidden" id="role_rela_per_roleId"/>
											<div class="role_authorizeBut"
												id="orgrole_function_oper_div">
												<input type="checkbox" name="perAccess" value="read" id="read_checkbox">查看</input>
												<input type="checkbox" name="perAccess" value="write" id="write_checkbox">编辑</input>
												<span><input type="button" class="input_button"
														value="保存授权" id="orgrole_function_oper_save_btn" onclick="saveRoleRelaPermission()"/> </span>
											</div>
									</div>
								</div>
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