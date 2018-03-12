<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>用户帐号管理</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		
		<link rel="stylesheet" type="text/css" href="../../css/base.css"/>
<link rel="stylesheet" type="text/css" href="../../css/public.css"/>
<link rel="stylesheet" type="text/css" href="css/systemManage.css"/>
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />

		<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js">
</script>
		<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js">
</script>
		<script type="text/javascript" src="jslib/tree_sm.js">
</script>
		<script type="text/javascript" src="../js/tab.js">
</script>
<script type="text/javascript" src="../../jslib/date/wdatePicker.js "></script>

<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="../../jslib/validate.ex.js"></script>
		<script type="text/javascript">
$(function() {
	//tab选项卡
	tab("role_tab", "li", "onclick");//角色类别切换
	tab("module_tab", "li", "onclick");//模块类别切换

	//帐号基本信息修改保存切换
	$(".accountInfo_modify").click(function() {
		$(".accountInfo_input").show();
		$(".accountInfo_value").hide();
	});
	$(".accountInfo_save").click(function() {
		$(".accountInfo_value").show();
		$(".accountInfo_input").hide();
	});
	//帐号密码修改保存切换
	$(".accountPassword_modify").click(function() {
		$(".accountPassword_info").show();
		$(".accountPassword_modify").hide();
	});
	$(".accountPassword_save").click(function() {
		$(".accountPassword_modify").show();
		$(".accountPassword_info").hide();
	});
	//组织架构树
	$("#OrgStructureTree").treeview({
		collapsed: true,  
		animated: "fast", 
	});
	$("#OrgStructureTree li span").each(function(){
		$(this).click(function(){
			$("#OrgStructureTree li span").removeClass("on_selected");
			$(this).addClass("on_selected");
		})
	});
})
</script>
	</head>

	<body>
		<%--主体开始--%>
		<div class="systemManage_main">
			<div class="systemManage_content">
				<div class="systemManage_top">
					用户帐号详细信息
				</div>
				<%--左边工作区开始--%>
				<div class="user_accountInfo">
					<h3>
						帐号基本信息：
					</h3>
					<div class="accountInfo">
						<ul class="">
							<li>
								<label>
									帐号：
								</label>
								<span class="accountInfo_value">${account.account}</span>
								<span class="accountInfo_input"><input class="required input-text" name="account.account" type="text" value="<s:property value="account.account"/>" />
									<em class="redStar">*</em>
								</span>
							</li>
							<li>
								<label>
									姓名：
								</label>
								<span class="accountInfo_value">${account.name}</span>
								<span class="accountInfo_input"><input class="required input-text" type="text" name="account.name" value="<s:property value="account.name" />"/><em
									class="redStar">*</em>
								</span>
							</li>
							<li>
								<label>
									邮箱：
								</label>
								<span class="accountInfo_value">${account.emailAddress}</span>
								<span class="accountInfo_input"><input class="required input-text" name="account.emailAddress" type="text" value="<s:property value="account.emailAddress" />"/><em
									class="redStar">*</em>
								</span>
							</li>
							  
							<li>
								<label>
									备用邮箱：
								</label>
								<span class="accountInfo_value">${account.backUpEmailAddress}</span>
								<span class="accountInfo_input"><input name="account.backUpEmailAddress" type="text" value="<s:property value="account.backUpEmailAddress" />"/>
								</span>
							</li>
							
							<li>
								<label>
									手机号码：
								</label>
								<span class="accountInfo_value">${account.cellPhoneNumber}</span>
								<span class="accountInfo_input"><input class="required number" type="text" name="account.cellPhoneNumber" value="<s:property value="account.cellPhoneNumber"/>" /><em
									class="redStar">*</em>
								</span>
							</li>
							<li>
								<label>
									手机邮箱：
								</label>
								<span class="accountInfo_value">${account.mobileEmailAddress}</span>
								<span class="accountInfo_input"><input class="required" type="text" name="account.mobileEmailAddress" value="<s:property value="account.mobileEmailAddress"/>" /><em
									class="redStar">*</em>
								</span>
							</li>
							<li>
								<label>
									生效时间：
								</label>
								<span class="accountInfo_value"><s:date name="account.time_range_begin" format="yyyy-MM-dd HH:mm:ss"></s:date></span>
								<span class="accountInfo_input"><input type="text" id="account_time_range_begin" name="account.time_range_begin" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text" value="<s:date name="account.time_range_begin" format="yyyy-MM-dd HH:mm:ss"/>">
								<%--  <input type="button" value="时间"
										onclick="fPopCalendar(event,document.getElementById('account_time_range_begin'),document.getElementById('account_time_range_begin'),true)" />
								 --%>
								</span>
							</li>
							<li>
								<label>
									失效时间：
								</label>
								<span class="accountInfo_value"><s:date name="account.time_range_end" format="yyyy-MM-dd HH:mm:ss"></s:date></span>
								<span class="accountInfo_input"><input type="text" id="account_time_range_end" name="account.time_range_end" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text" value="<s:date name="account.time_range_end" format="yyyy-MM-dd HH:mm:ss"/>">
							<%-- 	 <input type="button" value="时间"
										onclick="fPopCalendar(event,document.getElementById('account_time_range_end'),document.getElementById('account_time_range_end'),true)" />
							--%>
								</span>
							</li>
							<li>
								<label>
									状态：
								</label>
								<span class="accountInfo_value"><input type="checkbox"
										checked="checked" disabled="disabled" />  
										<s:if test="account.status==1">
							                    有效
							        </s:if>
							        <s:else>
							                   无效
							        </s:else></span>
								<span class="accountInfo_input"><input type="checkbox"
										checked="checked" />生效</span>
							</li>
						</ul>
						<div class="accountInfo_but">
							<input type="button"
								class="input_button accountInfo_value accountInfo_modify"
								value="修改基本信息" style="display: none;"/>
							<input type="button"
								class="input_button accountInfo_input accountInfo_save"
								value="保存" id="modifyAccountInfoBtn"/>
							<input type="button"
								class="input_button accountInfo_input accountInfo_save"
								value="放弃" />
						</div>
					</div>
					<div class="accountPassword" style="display: none;">
						<ul class="accountPassword_info">
							<li>
								<label>
									输入新密码：
								</label>
								<span><input type="password" id="new_pass_1"/>
								</span>
							</li>
							<li>
								<label>
									再输入新密码：
								</label>
								<span><input type="password"  id="new_pass_2"/>
								</span>
							</li>
							<li class="tc">
								<input type="button" class="input_button accountPassword_save" id="saveNewPaswordBtn"
									value="保存" />
								<input type="button" class="input_button accountPassword_save"
									value="放弃" />
							</li>
						</ul>
						<p class="accountPassword_modify tc">
							<input type="button" class="input_button" value="修改帐号密码" />
						</p>
					</div>
				</div>
				<%--左边工作区结束--%>
				<%--右边工作区开始--%>
				<div class="user_roleAuthorize">
					<h3>
						授予角色：
					</h3>
					<div class="user_roleAuthorize_content">
						<div class="roleAuthorize_top">
							<span>用户群<em class="redStar">*</em>：</span>
						</div>
						<div class="roleAuthorize_info">
							<div id="role_tab" class="role_tab tab_menu">
								<ul>
									<li class="selected">
										组织角色
									</li>
									<li>
										业务角色
									</li>
								</ul>
							</div>
							<div class="role_content">
								<div id="role_tab_0" class="clearfix">
									<div class="roleAuthorize_orgStructure">
										<h3>
											组织架构：
										</h3>
										<ul id="OrgStructureTree" class="filetree orgStructure_Tree">
											<li>
												<span class="folder">第一事业部</span>
												<ul>
													<li>
														<span class="folder">海珠一体化项目</span>
														<ul>
															<li>
																<span class="folder">东部片区</span>
																<ul>
																	<li>
																		<span class="file">东一片区</span>
																	</li>
																	<li>
																		<span class="file">东二片区</span>
																	</li>
																</ul>
															</li>
															<li>
																<span class="folder">中部片区</span>
															</li>
															<li>
																<span class="folder">西部片区</span>
															</li>
														</ul>
													</li>
												</ul>
											</li>
										</ul>
									</div>
									<div class="roleAuthorize_orgRole">
										<h3>
											组织角色：
										</h3>
										<ul>
											<li>
												<input type="checkbox">
												任务调度员
											</li>
											<li>
												<input type="checkbox">
												维护人员
											</li>
											<li>
												<input type="checkbox">
												技术专员
											</li>
											<li>
												<input type="checkbox">
												任务调度员
											</li>
											<li>
												<input type="checkbox">
												维护人员
											</li>
											<li>
												<input type="checkbox">
												技术专员
											</li>
											<li>
												<input type="checkbox">
												任务调度员
											</li>
											<li>
												<input type="checkbox">
												维护人员
											</li>
											<li>
												<input type="checkbox">
												技术专员
											</li>
											<li>
												<input type="checkbox">
												任务调度员
											</li>
											<li>
												<input type="checkbox">
												维护人员
											</li>
											<li>
												<input type="checkbox">
												技术专员
											</li>
											<li>
												<input type="checkbox">
												任务调度员
											</li>
											<li>
												<input type="checkbox">
												维护人员
											</li>
											<li>
												<input type="checkbox">
												技术专员
											</li>
											<li>
												<input type="checkbox">
												任务调度员
											</li>
											<li>
												<input type="checkbox">
												维护人员
											</li>
											<li>
												<input type="checkbox">
												技术专员
											</li>
											<li>
												<input type="checkbox">
												任务调度员
											</li>
											<li>
												<input type="checkbox">
												维护人员
											</li>
											<li>
												<input type="checkbox">
												技术专员
											</li>
											<li>
												<input type="checkbox">
												任务调度员
											</li>
											<li>
												<input type="checkbox">
												维护人员
											</li>
											<li>
												<input type="checkbox">
												技术专员
											</li>
											<li>
												<input type="checkbox">
												任务调度员
											</li>
											<li>
												<input type="checkbox">
												维护人员
											</li>
											<li>
												<input type="checkbox">
												技术专员
											</li>
										</ul>
										<p>
											<input type="button" class="input_button" value="保存" />
										</p>
									</div>
								</div>
								<div id="role_tab_1" style="display: none;">
									<div id="module_tab" class="module_tab tab_menu">
										<ul>
											<li class="selected" style="margin-left: 0px;">
												抢修业务
											</li>
											<li>
												巡检业务
											</li>
											<li>
												人员值班
											</li>
											<li>
												车辆调度
											</li>
											<li>
												物资调度
											</li>
										</ul>
									</div>
									<div class="module_content">
										<div id="module_tab_0">
											<ul>
												<li>
													<input type="checkbox">
													任务调度员
												</li>
												<li>
													<input type="checkbox">
													维护人员
												</li>
												<li>
													<input type="checkbox">
													技术专员
												</li>
												<li>
													<input type="checkbox">
													任务调度员
												</li>
												<li>
													<input type="checkbox">
													维护人员
												</li>
												<li>
													<input type="checkbox">
													技术专员
												</li>
												<li>
													<input type="checkbox">
													任务调度员
												</li>
												<li>
													<input type="checkbox">
													维护人员
												</li>
												<li>
													<input type="checkbox">
													技术专员
												</li>
												<li>
													<input type="checkbox">
													任务调度员
												</li>
												<li>
													<input type="checkbox">
													维护人员
												</li>
												<li>
													<input type="checkbox">
													技术专员
												</li>
												<li>
													<input type="checkbox">
													任务调度员
												</li>
												<li>
													<input type="checkbox">
													维护人员
												</li>
												<li>
													<input type="checkbox">
													技术专员
												</li>
												<li>
													<input type="checkbox">
													任务调度员
												</li>
												<li>
													<input type="checkbox">
													维护人员
												</li>
												<li>
													<input type="checkbox">
													技术专员
												</li>
												<li>
													<input type="checkbox">
													任务调度员
												</li>
												<li>
													<input type="checkbox">
													维护人员
												</li>
												<li>
													<input type="checkbox">
													技术专员
												</li>
												<li>
													<input type="checkbox">
													任务调度员
												</li>
												<li>
													<input type="checkbox">
													维护人员
												</li>
												<li>
													<input type="checkbox">
													技术专员
												</li>
											</ul>
											<p>
												<input type="button" class="input_button" value="保存" />
											</p>
										</div>
										<div id="module_tab_1" style="display: none;">
											<ul>
												<li>
													<input type="checkbox">
													任务调度员1
												</li>
												<li>
													<input type="checkbox">
													维护人员1
												</li>
												<li>
													<input type="checkbox">
													技术专员1
												</li>
											</ul>
											<p>
												<input type="button" class="input_button" value="保存" />
											</p>
										</div>
										<div id="module_tab_2" style="display: none;">
											<ul>
												<li>
													<input type="checkbox">
													任务调度员2
												</li>
												<li>
													<input type="checkbox">
													维护人员1
												</li>
												<li>
													<input type="checkbox">
													技术专员2
												</li>
											</ul>
											<p>
												<input type="button" class="input_button" value="保存" />
											</p>
										</div>
										<div id="module_tab_3" style="display: none;">
											<ul>
												<li>
													<input type="checkbox">
													任务调度员3
												</li>
												<li>
													<input type="checkbox">
													维护人员3
												</li>
												<li>
													<input type="checkbox">
													技术专员33
												</li>
											</ul>
											<p>
												<input type="button" class="input_button" value="保存" />
											</p>
										</div>
										<div id="module_tab_4" style="display: none;">
											<ul>
												<li>
													<input type="checkbox">
													任务调度员4
												</li>
												<li>
													<input type="checkbox">
													维护人员4
												</li>
												<li>
													<input type="checkbox">
													技术专员4
												</li>
											</ul>
											<p>
												<input type="button" class="input_button" value="保存" />
											</p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<%--右边工作区结束--%>
			</div>
		</div>
	</body>
</html>
