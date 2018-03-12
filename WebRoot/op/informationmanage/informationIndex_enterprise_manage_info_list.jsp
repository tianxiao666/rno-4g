<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>企业信息管理 - 列表</title>
    <link rel="stylesheet" type="text/css" href="../../css/base.css"/>
	<link rel="stylesheet" type="text/css" href="../../css/public.css"/>
	<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css"></link>
	<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css"></link>
	<link rel="stylesheet" type="text/css" href="../../jslib/dialog/dialog.css"/>
	<link rel="stylesheet" type="text/css" href="css/projectInfoManage.css"/>
	
	
	<%-- 公用插件 --%>
	<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
	<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="../../jslib/date/date.js"></script>
	<%-- 帮助类 --%>
	<script type="text/javascript" src="js/util/objutil.js"></script>
	<script type="text/javascript" src="js/util/tablePage.js"></script>
	<script type="text/javascript" src="js/util/new_formcheck.js"></script>
	<script type="text/javascript" src="js/util/showedit.js"></script>
	<script type="text/javascript" src="js/util/objutil.js"></script>
	<%-- 类库 --%>
	<script type="text/javascript" src="js/class/enterprise.js"></script>
	<%-- 对象 --%>
	<script type="text/javascript" src="js/obj/enterprisemanage_list_obj.js"></script>
	<%-- 页面js --%>
	<script type="text/javascript" src="js/enterprisemanage_list.js"></script>
	<script type="text/javascript" src="js/enterprisemanage_list_save.js"></script>
	<script type="text/javascript" src="js/enterprisemanage_list_update.js"></script>
  </head>
  <script>
  	//页面js
  	$(document).ready(function(){
  		$("body").keyup(function( _ev ){
  			var keyCode = _ev.keyCode;
			//判断是否esc
			if ( keyCode == 27 ) {
				$("#ent_Dialog").fadeOut(100);
				$("#entAdd_Dialog").fadeOut(100);
				$(".black").fadeOut(100);
			}
  		});
  	})
  </script>
  <body>
    <%--右边工作区开始--%>
		<div >
			
			<div class="projectInfoManage_infoList">
				<h3 class="clearfix">
					<span class="name">企业列表：</span>
					<span class="input"><input type="button" class="entAdd_showBtn" value="增加企业" /></span>
				</h3>
				<list_data>
					<table class="main_table" id="enterprise_list_table">
						<thead>
							<tr>
								<th>企业全称</th>
								<th>注册号</th>
								<th>所有制</th>
								<th>法人代表</th>
								<th>经营范围</th>
								<th>地址</th>
								<th>合作身份</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							
						</tbody>
					</table>
				</list_data>
			</div>
		</div>
		<%--右边工作区结束--%>
		
		<%--基本信息弹出框 开始--%>
		<div class="black"></div>
		<%-- 查看企业信息 开始 --%>
		<div id="ent_Dialog" class="dialog ent_dialog" style="display:none;">
			<div class="dialog_header">
				<div class="dialog_title">查看企业信息</div>
				<div class="dialog_tool">
				   <div class="dialog_tool_close dialog_closeBtn"></div>
				</div>
			</div>
			<form action="enterprisemanage!updateEnterpriseInfo.action" id="update_enterprise_form" method="post">
				<input type="hidden" column="enterprise#id" name="Enterprise_update#id" id="enterprise_update_id" />
				
				<div class="dialog_content">
					<table class="dialog_table">
						<tbody>
							<tr>
								<td class="menu_td">企业名称：</td>
								<td>
									<span column="enterprise#fullName" id="update_enterpriseName" showSpan="true" ></span>
									<input type="text" editInput="true" checknull="企业名称不能为空" name="Enterprise_update#fullName" />
								</td>
								<td class="menu_td">注册号：</td>
								<td>
									<span column="enterprise#registerNumber" showSpan="true" >&nbsp;</span>
									<input type="text" promtInfo="注册号必须唯一" checknull="注册号不能为空" checkajax="{'url':'enterprisemanage_ajax!checkEnterpriseRegisterNumberExists.action','msg':'注册号已存在','param':{'registerNumber':'#update_registerNumber','id':'#enterprise_update_id'}}"  id="update_registerNumber"editInput="true" name="Enterprise_update#registerNumber" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">企业类型：</td>
								<td>
									<span column="enterprise#ownership" showSpan="true" >&nbsp;</span>
									<select class="" id="" editInput="true" name="Enterprise_update#ownership" >
										<option value="股份有限公司" selected="selected">股份有限公司</option>
										<option value="非股份有限公司">非股份有限公司</option>
										<option value="责任有限公司">责任有限公司</option>
										<option value="股份合作企业">股份合作企业</option>
										<option value="私营企业">私营企业</option>
										<option value="国有企业">国有企业</option>
										<option value="集体企业">集体企业</option>
										<option value="联营企业">联营企业</option>
										<option value="其他组织">其他组织</option>
									</select>
								</td>
								<td class="menu_td">法人代表：</td>
								<td>
									<span column="enterprise#legalRepresentative" showSpan="true" >&nbsp;</span>
									<input type="text" editInput="true" name="Enterprise_update#legalRepresentative" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">合作身份：</td>
								<td>
									<span column="enterprise#cooperative" showSpan="true" ></span>
									<select class="" id="" name="Enterprise_update#cooperative" editInput="true" >
										<option value="服务商" selected="selected">服务商</option>
										<option value="运营商">运营商</option>
									</select>
								</td>
								<td class="menu_td">注册地址：</td>
								<td>
									<span column="enterprise#registerAddress" showSpan="true" >&nbsp;</span>
									<input type="text" editInput="true" name="Enterprise_update#registerAddress" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">经营范围：</td>
								<td colspan="3">
									<span column="enterprise#businessSphere" showSpan="true" >&nbsp;</span>
									<textarea  id="enterprise_update" rows="4" style="width:555px;" editInput="true" name="Enterprise_update#businessSphere" ></textarea><div id="enterprise_div_update"  style="color:#FF0000" width="60"  ></div>
								</td>
							</tr>
							<tr>
								<td class="menu_td">经营地址：</td>
								<td>
									<span column="enterprise#businessAddress" showSpan="true" >&nbsp;</span>
									<input type="text" editInput="true" name="Enterprise_update#businessAddress" />
								</td>
								<td class="menu_td">邮政编码：</td>
								<td>
									<span column="enterprise#zipCode" showSpan="true" >&nbsp;</span>
									<input type="text" editInput="true" name="Enterprise_update#zipCode" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">注册资本：</td>
								<td>
									<span column="enterprise#registerMoney" showSpan="true" >&nbsp;</span>
									<input type="text" editInput="true" name="Enterprise_update#registerMoney" />
								</td>
								<td class="menu_td">行业分类：</td>
								<td>
									<span column="enterprise#industryType" showSpan="true" >&nbsp;</span>
									<input type="text" editInput="true" name="Enterprise_update#industryType" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">纳税人名称：</td>
								<td>
									<span column="enterprise#taxBearer" showSpan="true" >&nbsp;</span>
									<input type="text" editInput="true" name="Enterprise_update#taxBearer" />
								</td>
								<td class="menu_td">税务登记号：</td>
								<td>
									<span column="enterprise#taxRegistrationNumber" showSpan="true" >&nbsp;</span>
									<input type="text" editInput="true" name="Enterprise_update#taxRegistrationNumber" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">成立日期：</td>
								<td>
									<span column="enterprise#createDate" showSpan="true" >&nbsp;</span>
									<input type="text" editInput="true" name="Enterprise_update#createDate" id="update_createDate" />
									<a class="dateButton" href="#" editButton="true" onclick="fPopCalendar(event,document.getElementById('update_createDate'),document.getElementById('update_createDate'),false)" ></a>
								</td>
								<td class="menu_td">企业状态：</td>
								<td>
									<span column="enterprise#state" showSpan="true" >开业</span>
									<select class="" id="" editInput="true" name="Enterprise_update#state" >
										<option value="开业">开业</option>
										<option value="在册">在册</option>
									</select>
								</td>
							</tr>
							<tr>
								<td class="menu_td">电话：</td>
								<td>
									<span column="enterprise#phone" showSpan="true" >&nbsp;</span>
									<input type="text" editInput="true" name="Enterprise_update#phone" />
								</td>
								<td class="menu_td">传真：</td>
								<td>
									<span column="enterprise#telautogram" showSpan="true" >&nbsp;</span>
									<input type="text" editInput="true" name="Enterprise_update#telautogram" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">邮箱：</td>
								<td>
									<span column="enterprise#mailbox" showSpan="true" >&nbsp;</span>
									<input type="text" editInput="true" name="Enterprise_update#mailbox" />
								</td>
								<td class="menu_td">网址：</td>
								<td>
									<span column="enterprise#internetUrl" showSpan="true" >&nbsp;</span>
									<input type="text" editInput="true" name="Enterprise_update#internetUrl" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">邮箱后缀：</td>
								<td>
									<span column="enterprise#enterpriseSuffix"></span>
									<input type="hidden" column="enterprise#enterpriseSuffix" name="Enterprise_update#enterpriseSuffix"></input>
								</td>
							</tr>
						</tbody>
					</table>
					<div class="dialog_but">
						<button type="button" class="aui_state_highlight" editBtn="true">修改</button>
						<button type="button" class="aui_state_highlight" id="updateEnterprise_btn" saveBtn="true">保存</button>
						<button type="button" class="aui_state_highlight" cancelBtn="true">返回</button>
						<button type="button" class="aui_state_highlight" showBtn="true" id="deleteEnterprise_btn">删除</button>
					</div>
				</div>
			</form>
		</div>
		<%-- 查看企业信息 结束 --%>
		
		
		
		
		
		
		<%-- 添加企业信息 开始 --%>
		<div id="entAdd_Dialog" class="dialog entadd_dialog" style="display:none;">
			<div class="dialog_header">
				<div class="dialog_title">添加企业信息</div>
				<div class="dialog_tool">
				   <div class="dialog_tool_close dialog_closeBtn"></div>
				</div>
			</div>
			<form action="enterprisemanage_ajax!saveEnterpriseInfo.action" id="save_enterprise_form" method="post">
				<div class="dialog_content">
					<table class="dialog_table">
						<tbody>
							<tr>
								<td class="menu_td">企业名称：</td>
								<td>
									<input type="text" name="Enterprise_save#fullName" checknull="企业名称不能为空" />
								</td>
								<td class="menu_td">注册号：</td>
								<td>
									<input type="text" id="save_registerNumber" promtInfo="注册号必须唯一" checknull="注册号不能为空" checkajax="{'url':'enterprisemanage_ajax!checkEnterpriseRegisterNumberExists.action','msg':'注册号已存在','param':{'registerNumber':'#save_registerNumber','id':'0'}}"  name="Enterprise_save#registerNumber" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">企业类型：</td>
								<td>
									<select class="" id="" name="Enterprise_save#ownership" >
										<option value="股份有限公司" selected="selected">股份有限公司</option>
										<option value="非股份有限公司">非股份有限公司</option>
										<option value="责任有限公司">责任有限公司</option>
										<option value="股份合作企业">股份合作企业</option>
										<option value="私营企业">私营企业</option>
										<option value="国有企业">国有企业</option>
										<option value="集体企业">集体企业</option>
										<option value="联营企业">联营企业</option>
										<option value="其他组织">其他组织</option>
									</select>
								</td>
								<td class="menu_td">法人代表：</td>
								<td>
									<input type="text" name="Enterprise_save#legalRepresentative" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">合作身份：</td>
								<td>
									<select class="" id="save_cooperative" name="Enterprise_save#cooperative" >
										<option value="服务商" selected="selected">服务商</option>
										<option value="运营商">运营商</option>
									</select>
								</td>
								<td class="menu_td">注册地址：</td>
								<td>
									<input type="text" name="Enterprise_save#registerAddress" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">经营范围：</td>
								<td colspan="3">
									<textarea rows="4" id="enterprise_save" style="width:555px;" name="Enterprise_save#businessSphere" ></textarea>
									<div id="enterprise_div_save"  style="color:#FF0000" width="60"  >  </div>
								</td>
							</tr>
							<tr>
								<td class="menu_td">经营地址：</td>
								<td>
									<input type="text" name="Enterprise_save#businessAddress" />
								</td>
								<td class="menu_td">邮政编码：</td>
								<td>
									<input type="text" name="Enterprise_save#zipCode" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">注册资本：</td>
								<td>
									<input type="text" name="Enterprise_save#registerMoney" />
								</td>
								<td class="menu_td">行业分类：</td>
								<td>
									<input type="text" name="Enterprise_save#industryType" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">纳税人名称：</td>
								<td>
									<input type="text" name="Enterprise_save#taxBearer" />
								</td>
								<td class="menu_td">税务登记号：</td>
								<td>
									<input type="text" name="Enterprise_save#taxRegistrationNumber" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">成立日期：</td>
								<td>
									<input type="text" id="save_createDate" name="Enterprise_save#createDate" >
									<a class="dateButton" href="#" onclick="fPopCalendar(event,document.getElementById('save_createDate'),document.getElementById('save_createDate'),false)" ></a>
								</td>
								<td class="menu_td">企业状态：</td>
								<td>
									<select class="" id="" name="Enterprise_save#state" >
										<option value="开业">开业</option>
										<option value="在册">在册</option>
									</select>
								</td>
							</tr>
							<tr>
								<td class="menu_td">电话：</td>
								<td>
									<input type="text" name="Enterprise_save#phone" />
								</td>
								<td class="menu_td">传真：</td>
								<td>
									<input type="text" name="Enterprise_save#telautogram" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">邮箱：</td>
								<td>
									<input type="text" name="Enterprise_save#mailbox" />
								</td>
								<td class="menu_td">网址：</td>
								<td>
									<input type="text" name="Enterprise_save#internetUrl" />
								</td>
							</tr>
							<tr>
								<td class="menu_td">邮箱后缀：</td>
								<td>
									<em style="font-size: 20px;">@</em>&nbsp;<input type="text" style="width:135px;" id="save_suffix" checknull="企业后缀不能为空" 
										 checkregex="{'reg' : '/^[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.{1}[A-Za-z0-9]+$/' , 'msg' : '企业后缀不规范'}" />
									<input type="hidden" id="save_enterpriseSuffix" name="Enterprise_save#enterpriseSuffix" />
								</td>
							</tr>
						</tbody>
					</table>
					<div class="dialog_but">
						<button class="aui_state_highlight" type="button" id="saveEnterprise_btn">保存</button>
						<button class="aui_state_highlight" type="button" onclick="$('.dialog_closeBtn').click();">取消</button>
						<input type="reset" style="display:none;"  />
					</div>
				</div>
			</form>
		</div>
		<%-- 添加企业信息 结束 --%>
		<%--基本信息弹出框 结束--%>
		
  </body>
</html>
