<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>项目基本信息管理 - 列表</title>
	<link rel="stylesheet" type="text/css" href="../../css/base.css"/>
	<link rel="stylesheet" type="text/css" href="../../css/public.css"/>
	<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css"></link>
	<link rel="stylesheet" type="text/css" href="css/projectInfoManage.css"/>
	<link rel="stylesheet" type="text/css" href="../../jslib/dialog/dialog.css"/>
	<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css"></link>
	
	<%-- 公用插件 --%>
	<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
	<script type="text/javascript" src="../../jslib/common.js"></script>
	<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
	<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="../jslib/generateTree.js"></script>
	<%-- 公用插件 --%>
	<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="../../jslib/date/date.js"></script>
	
	<%-- 帮助类 --%>
	<script type="text/javascript" src="js/util/objutil.js"></script>
	<script type="text/javascript" src="js/util/tablePage.js"></script>
	<script type="text/javascript" src="js/util/new_formcheck.js"></script>
	<script type="text/javascript" src="js/util/showedit.js"></script>
	<script type="text/javascript" src="js/util/areaTreeView.js"></script>
	<script type="text/javascript" src="js/util/uploadutil.js"></script>
	<script type="text/javascript" src="js/util/dateutil.js"></script>
	<script type="text/javascript" src="js/util/objutil.js"></script>
	<%-- 类库 --%>
	<script type="text/javascript" src="js/class/project.js"></script>
	<%-- 对象 --%>
	<script type="text/javascript" src="js/obj/projectmanage_list_obj.js"></script>
	<%-- 页面js --%>
	<script type="text/javascript" src="js/projectmanage_list.js"></script>
	<script type="text/javascript" src="js/projectmanage_list_save.js"></script>
	<script type="text/javascript" src="js/projectmanage_list_update.js"></script>
	
	
  </head>
  
  <body>
    	<%--右边工作区开始--%>
		<div>
			<div class="projectInfoManage_infoList">
				<h3 class="clearfix">
					<span class="name">服务项目列表：</span>
					<span class="input"><input type="button" class="projectAdd_showBtn" value="增加项目" /></span>
				</h3>
				<table class="main_table" id="project_list_table">
					<thead>
						<tr>
							<th>项目编号</th>
							<th>项目名称</th>
							<th>项目职责描述</th>
							<th>客户（甲方）</th>
							<th>服务商（乙方）</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						
					</tbody>
				</table>
			</div>
		</div>
		<%--右边工作区结束--%>
		
		
		
		<%--查看基本信息弹出框--%>
		<div class="black"></div>
		
		<%-- 添查看项目对话框 开始 --%>
		<div id="project_Dialog" class="dialog project_dialog" style="display:none; width: 730px;">
			<div class="dialog_header">
				<div class="dialog_title">查看项目信息</div>
				<div class="dialog_tool">
				   <div class="dialog_tool_close dialog_closeBtn"></div>
				</div>
			</div>
			<form action="projectmanage_ajax!updateProjectInfo.action" id="update_project_form" method="post">
				<%-- 隐藏域 --%>
				<input type="hidden" name="Project_update#id" column="project#id" id="update_project_id" />
				<input type="hidden" name="Project_update#cityId" column="project#cityId" id="update_cityId" />
				
				
				<div class="dialog_content">
					<%-- 隐藏域 --%>
					<input type="hidden" column="project#clientOrgId" id="update_client_orgId"  name="Project_update#clientOrgId" />
					<input type="hidden" column="project#serverOrgId" id="update_server_orgId"  name="Project_update#serverOrgId" />
					
					
					<table class="dialog_table1" width="100%">
						<tbody>
							<tr>
								<td class="menu_td">项目编号：</td>
								<td>
									<span showSpan="true" column="project#projectNumber"></span>
									<div id="update_checkproId_div" editDiv="true">
										<input editInput="true" name="Project_update#projectNumber" promtInfo=" 项目编号必须唯一" id="update_projectNumber" type="text" />
										<input editButton="true" id="update_checkProjectProId_btn" checknull=" 项目编号不能为空" checktarget="#update_projectNumber" checkajax="{'url':'projectmanage_ajax!checkProjectNumberExists.action','msg':' 项目编号已存在','smsg':' 编号可用','param':{'projectNumber':'#update_projectNumber','id':'#update_project_id'}}" type="button" value="冲突检查" />
										<input type="hidden"  checklength="{'minLength':'0','maxLength':'30','msg':' 长度超过[max]'}" checktarget="#update_projectNumber" />
									</div>
								</td>
								<td class="menu_td">项目所在地：</td>
								<td>
									<span showSpan="true" column="project#cityName" class="info_value"></span>
									<input editInput="true" readonly="readonly" id="update_city_txt" type="text" />
									<a class="orgButton" style="margin-left:-27px;" editButton="true"   href="#" id="update_area_treeView_btn"></a>
									<div class="area_tree" id="update_area_treeDiv"></div>
									<input type="hidden" checknull="&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;项目所在地不能为空"  checktarget="#update_city_txt" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">项目名称：</td>
								<td colspan="3">
									<span showSpan="true" column="project#name" class="info_value"></span>
									<input editInput="true" checknull="项目名称不能为空" name="Project_update#name"  checklength="{'minLength':'0','maxLength':'60','msg':'&nbsp;&nbsp;&nbsp;&nbsp;长度超过[max]'}"  type="text" style="width:515px;" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">项目职责描述：</td>
								<td colspan="3">
									<span showSpan="true" column="project#responsibilityDescription" class="info_value"></span>
									<textarea editInput="true" name="Project_update#responsibilityDescription" rows="4" style="width:520px;"></textarea>
								</td>
							</tr>
							<tr>
								<td class="menu_td">电子版项目合同：</td>
								<td colspan="3">
									<span showSpan="true" column="project#agreement" class="info_value"><em class="grayStar">(44.80k)</em></span>
									<div editDiv="true" >
										<input type="text" value="" class="update_agreement" id="update_upload_text"/>
										<input type="button" value="上传" class="uploadBtn" id="update_upload_btn" />
										<input type="hidden" class="update_agreement" id="update_upload_hidden" name="Project_update#agreement" />
									</div>
								</td>
							</tr>
							<tr>		
								<td class="menu_td">项目启动日期：</td>
								<td>
									<span showSpan="true" column="project#startDate" class="info_value">2012-01-01</span>
									<input editInput="true" name="Project_update#startDate" id="update_startDate" value="2012-01-01" readonly="readonly" type="text" />
									<a class="dateButton" style="margin-left:-27px;" editButton="true" onclick="fPopCalendar(event,document.getElementById('update_startDate'),document.getElementById('update_startDate'),false)" href="#">
								</td>
								<td class="menu_td">预计结束日期：</td>
								<td>
									<span showSpan="true" column="project#planEndDate" class="info_value">2013-01-01</span>
									<input editInput="true" id="update_planEndDate" name="Project_update#planEndDate" value="2013-01-01" readonly="readonly" type="text" />
									<a class="dateButton" style="margin-left:-27px;" onclick="fPopCalendar(event,document.getElementById('update_planEndDate'),document.getElementById('update_planEndDate'),false)" editButton="true" href="#">
								</td>
							</tr>
							<tr>
								<td class="menu_td">客户（甲方）：</td>
								<td>
									<span showSpan="true" column="project#clientEnterpriseName" class="info_value"></span>
									<select editInput="true" id="update_project_client_enterprise_select" name="Project_update#clientEnterpriseName" class="client_select" checknull="&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;客户企业不能为空">
									</select>
								</td>
								<td class="menu_td">服务商（乙方）：</td>
								<td>
									<span showSpan="true" column="project#serverEnterpriseName" class="info_value"></span>
									<select editInput="true"  id="update_project_server_enterprise_select" name="Project_update#serverEnterpriseName" class="server_select" id="">
									</select>
								</td>
							</tr>
							<tr>
								<td class="menu_td">客户负责组织：</td>
								<td>
									<span showSpan="true" column="project#clientOrgFullName" class="info_value"></span>
									<input editInput="true" id="update_client_choice_bizName" readonly="readonly" type="text" />
									<a class="orgButton" style="margin-left:-27px;" editButton="true"  href="#" id="update_client_choice_btn"></a>
									<div id="update_client_treeDiv" class="service_tree">
										<%-- 组织树 --%>
									</div>
									<input type="hidden"  checknull="&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;客户负责组织不能为空" checktarget="#update_client_choice_bizName" />
								</td>
								<td class="menu_td">服务商负责组织：</td>
								<td>
									<span showSpan="true" column="project#serverOrgFullName" class="info_value"></span>
									<input editInput="true" id="update_server_choice_bizName" readonly="readonly" type="text" />
									<a class="orgButton" style="margin-left:-27px;" href="#" editButton="true" id="update_server_choice_btn"></a>
									<div id="update_server_treeDiv" class="service_tree">
										<%-- 组织树 --%>
									</div>
									<input type="hidden"  checknull="&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;服务商负责组织不能为空" checktarget="#update_server_choice_bizName" />
								</td>
							</tr>
						</tbody>
					</table>
					<div class="dialog_but">
						<button type="button" editBtn="true" class="aui_state_highlight info_modify">修改</button>
						<button type="button" class="aui_state_highlight" showBtn="true" id="delete_btn">删除</button>
						<button type="button" saveBtn="true" id="updateProject_btn" class="aui_state_highlight info_input info_save">保存</button>
						<button type="button" cancelBtn="true" class="aui_state_highlight info_input cancelBtn">取消</button>
					</div>
				</div>
			</form>
		</div>
		<%-- 添查看项目对话框 结束 --%>
		
		<%-- 添加项目对话框 开始 --%>
		<div id="projectAdd_Dialog" class="dialog projectadd_dialog" style="display:none; width:730px;">
			<div class="dialog_header">
				<div class="dialog_title">添加项目信息</div>
				<div class="dialog_tool">
				   <div class="dialog_tool_close dialog_closeBtn"></div>
				</div>
			</div>
			<form action="projectmanage_ajax!saveProjectInfo.action" id="save_project_form" method="post" enctype="multipart/form-data">
				<%-- 隐藏域 --%>
				<input type="hidden" id="save_client_orgId" name="Project_save#clientOrgId" />
				<input type="hidden" id="save_server_orgId" name="Project_save#serverOrgId" />
				<input type="hidden" id="save_cityId"  name="Project_save#cityId" />
			
				<div class="dialog_content">
					<table class="dialog_table1" width="100%">
						<tbody>
							<tr>
								<td class="menu_td">项目编号：</td>
								<td>
									<div id="save_checkproId_div">
										<input  name="Project_save#projectNumber" promtInfo=" 项目编号必须唯一" id="save_projectNumber" type="text" />
										<input id="save_checkProjectProId_btn" checktarget="#save_projectNumber" type="button" value="冲突检查" checknull=" 项目编号不能为空" checkajax="{'url':'projectmanage_ajax!checkProjectNumberExists.action','msg':' 项目编号已存在','smsg':' 编号可用','param':{'projectNumber':'#save_projectNumber','id':0}}" />
										<input type="hidden"  checklength="{'minLength':'0','maxLength':'30','msg':' 长度超过[max]'}" checktarget="#save_projectNumber" />
									</div>
								</td>
								<td class="menu_td">项目所在地：</td>
								<td>
									<input type="text" readonly="readonly" id="save_city_txt" />
									<a class="orgButton" style="margin-left:-27px;"  href="#" id="save_area_treeView_btn"></a>
									<div class="area_tree" id="save_area_treeDiv"></div>
									<input type="hidden"  checknull="&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;所在地不能为空"   checktarget="#save_city_txt" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">项目名称：</td>
								<td colspan="3">
									<input type="text" name="Project_save#name" checknull="&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;项目名称不能为空" checklength="{'minLength':'0','maxLength':'60','msg':'&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;长度超过[max]'}" style="width:515px;" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">项目职责描述：</td>
								<td colspan="3">
									<textarea rows="4" name="Project_save#responsibilityDescription" style="width:520px;"></textarea>
								</td>
							</tr>
							<tr>
								<td class="menu_td">电子版项目合同：</td>
								<td colspan="3">
									<input type="text" value="" class="save_agreement" id="save_upload_text"/>
									<input type="button" value="上传" class="uploadBtn" id="save_upload_btn" />
									<input type="hidden" class="save_agreement" id="save_upload_hidden" name="Project_save#agreement" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">项目启动日期：</td>
								<td>
									<input type="text" readonly="readonly" name="Project_save#startDate" id="save_startDate" />
									<a class="dateButton" style="margin-left:-27px" href="#" onclick="fPopCalendar(event,document.getElementById('save_startDate'),document.getElementById('save_startDate'),false)" ></a>
								</td>
								<td class="menu_td">预计结束日期：</td>
								<td>
									<input type="text" readonly="readonly" name="Project_save#planEndDate" id="save_planEndDate" />
									<a class="dateButton" style="margin-left:-27px" href="#" onclick="fPopCalendar(event,document.getElementById('save_planEndDate'),document.getElementById('save_planEndDate'),false)"></a>
								</td>
							</tr>
							<tr>
								<td class="menu_td">客户（甲方）：</td>
								<td>
									<select class="client_select" id="save_custom_enterprise_select" name="Project_save#clientEnterpriseName">
									</select>
									<input type="hidden" checknull="&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;客户企业不能为空" checkTarget="#save_custom_enterprise_select" />
								</td>
								<td class="menu_td">服务商（乙方）：</td>
								<td>
									<select class="server_select" id="save_project_server_enterprise_select" name="Project_save#serverEnterpriseName">
									</select>
								</td>
							</tr>
							<tr>
								<td class="menu_td">客户负责组织：</td>
								<td>
									<input type="text" id="save_client_choice_bizName" readonly="readonly"   />
									<a class="orgButton" style="margin-left:-27px"  href="#" id="save_client_choice_btn"></a>
									<div id="save_client_treeDiv" class="service_tree">
										<%-- 组织树 --%>
									</div>
									<input type="hidden" checknull="&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;客户组织不能为空" checktarget="#save_client_choice_bizName" />
								</td>
								<td class="menu_td">服务商负责组织：</td>
								<td>
									<input type="text" id="save_server_choice_bizName" readonly="readonly" />
									<a class="orgButton" style="margin-left:-27px"  href="#" id="save_server_choice_btn"></a>
									<div id="save_server_treeDiv" class="service_tree">
										<%-- 组织树 --%>
									</div>
									<input type="hidden" checknull="&nbsp;&emsp;&emsp;&emsp;&emsp;&emsp;服务商组织不能为空" checktarget="#save_server_choice_bizName" />
								</td>
							</tr>
						</tbody>
					</table>
					<div class="dialog_but">
						<button type="button" id="save_project_submit" class="aui_state_highlight">保存</button>
						<button type="button" class="aui_state_highlight cancelBtn">取消</button>
					</div>
				</div>
			</form>
		</div>
    	<%-- 添加项目对话框 结束 --%>
  </body>
</html>
