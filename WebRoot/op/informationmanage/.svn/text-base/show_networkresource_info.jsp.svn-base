<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>项目服务范围</title>
		<link rel="stylesheet" type="text/css" href="../../css/base.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public.css" />
		<link rel="stylesheet" type="text/css"
			href="css/projectInfoManage.css" />
		<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css"></link>
		<link rel="stylesheet" type="text/css"
			href="css/iscreate_treeview.css" />
		
		<script type="text/javascript"
			src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
		<script type="text/javascript"
			src="../../jslib/jquery/jquery.treeview.js"></script>
			
		<script type="text/javascript" src="../../jslib/common.js"></script>
		<script type="text/javascript" src="../jslib/generateTree.js"></script>

		<%-- 帮助类 --%>
		<script type="text/javascript" src="js/util/objutil.js"></script>
		<script type="text/javascript" src="js/util/tablePage.js"></script>
		<script type="text/javascript" src="js/util/new_formcheck.js"></script>
		<script type="text/javascript" src="js/util/showedit.js"></script>
		<script type="text/javascript" src="js/util/areaTreeView.js"></script>
		<%-- 类库 --%>
		<script type="text/javascript" src="js/class/project.js"></script>

		<script type="text/javascript">
			var projectId = null;
			$(document).ready(function(){
				projectId = "${param.projectId}";
			});
			
		</script>
		<%-- 对象 --%>
		<script type="text/javascript" src="js/obj/shownetworkinfo_obj.js"></script>
		<%-- 页面js --%>
		<script type="text/javascript" src="js/shownetworkinfo.js"></script>
		<style>
			#server_treeDiv .on_selected{color:red;}
		</style>
	</head>
	<body>
		<%--主体开始--%>
		<div class="projectInfoManage_main">
			<%--工作区 begin --%>
			<div class="projectInfoManage_content">
				<%-- 隐藏域 --%>
				<input type="hidden" value="" column="project#id" id="projectId" />
				<input type="hidden" value="" column="project#cityId" id="cityId" />
				<input type="hidden" value="" column="project#serverOrgId" id="serverOrgId" />
				
				<div class="projectInfoManage_top">
					项目服务范围
				</div>

				<div class="projectInfoManage_title">
					<span class="tl">项目编号：<em column="project#projectNumber"></em></span>
					<span>项目名称：<em column="project#name"></em></span>
					<span class="tr">项目客户：<em column="project#clientEnterpriseName"></em></span>
				</div>
				<div class="projectSserve_content">
					<ul>
						<li>
							<h2><em class="parent"></em><span>项目基本信息</span></h2>
							<div class="projectSserve_info">
							
								<%-- 项目基本信息 begin --%>
								<div id="projectService_tab_0">
									<table class="dialog_table">
										<tbody>
											<tr>
												<td class="menu_td">
													项目编号：
												</td>
												<td>
													<span column="project#proId"></span>
												</td>
												<td class="menu_td">
													项目所在地：
												</td>
												<td>
													<span column="project#cityName" class="info_value"></span>
												</td>
											</tr>
											<tr>
												<td class="menu_td">
													项目名称：
												</td>
												<td colspan="3">
													<span column="project#name" class="info_value"></span>
												</td>
											</tr>
											<tr>
												<td class="menu_td">
													项目职责描述：
												</td>
												<td colspan="3">
													<span column="project#responsibilityDescription"
														class="info_value"></span>
												</td>
											</tr>
											<tr>
												<td class="menu_td">
													电子版项目合同：
												</td>
												<td colspan="3">
													<span column="project#agreement" class="info_value"><em
														class="grayStar"></em>
													</span>
												</td>
											</tr>
											<tr>
												<td class="menu_td">
													项目启动日期：
												</td>
												<td colspan="3">
													<span column="project#startDate" class="info_value"></span>
												</td>
											</tr>
											<tr>
												<td class="menu_td">
													预计结束日期：
												</td>
												<td colspan="3">
													<span column="project#planEndDate" class="info_value"></span>
												</td>
											</tr>
											<tr>
												<td class="menu_td">
													客户（甲方）：
												</td>
												<td colspan="3">
													<span column="project#clientEnterpriseName" class="info_value"></span>
												</td>
											</tr>
											<tr>
												<td class="menu_td">
													服务商（乙方）：
												</td>
												<td colspan="3">
													<span column="project#serverEnterpriseName" class="info_value"></span>
												</td>
											</tr>
											<tr>
												<td class="menu_td">
													客户负责组织：
												</td>
												<td colspan="3">
													<span column="project#clientOrgFullName" class="info_value"></span>
												</td>
											</tr>
											<tr>
												<td class="menu_td">
													服务商负责组织：
												</td>
												<td colspan="3">
													<span column="project#serverOrgFullName" class="info_value"></span>
												</td>
											</tr>
										</tbody>
									</table>
								</div>
								<%-- 项目基本信息 end --%>
							</div>
						</li>
						<li>
							<h2><em class="parent" style="background: url(images/ico_show.gif) no-repeat"></em><span>项目网络资源</span></h2>
							<div class="projectSserve_info" style="display:block;">
								<%-- 项目网络资源 begin --%>
								<div id="projectService_tab_1">
									<%-- 隐藏域 --%>
									<input type="hidden" id="network_resource_areaId" />
									<input type="hidden" id="network_resource_orgId"
										column="project#serverOrgId" />
			
									<div class="network_area clearfix">
										<h5>
											所维护的地理区域：
										</h5>
										<div class="network_area_tree">
											<div class="area_tree"  id="project_network_areatree_div"></div>
										</div>
									</div>
									<div class="network_type">
										<h5>
											所维护的网络设施类型：
										</h5>
										<div class="network_type_info" id="network_area_type_info" >
											<h3>
												物理点资源：分类授权
											</h3>
											<div class="network_type_facility"
												id="project_reswork_resource_div" style="height: 50px;">
												<%-- 资源checkbox树 --%>
											</div>
											<h3>
												逻辑网资源：分类授权
											</h3>
											<div class="network_type_facility" id="project_reswork_line_div">
												<%-- 网络checkbox树 --%>
											</div>
										</div>
									</div>
									<div class="network_type_but">
										<input id="project_network_button" type="button" value="保存" />
									</div>
								</div>
								<%-- 项目网络资源 end --%>
							</div>
						</li>
						<li>
							<h2><em class="parent"></em><span>维护组织划分</span></h2>
							<div class="projectSserve_info">
								<%-- 维护组织划分 begin --%>
								<div id="projectService_tab_2">
									<%-- 隐藏域 --%>
									<input type="hidden" id="network_repair_resource_areaId" />
									<input type="hidden" id="network_repair_resource_orgId" />
									
									<div class="network_area clearfix">
										<h5>
											服务商维护组织：
										</h5>
										<div class="network_area_tree" id="server_treeDiv"
											style="height: 200px; overflow-y: auto;">
			
										</div>
									</div>
									<div class="network_area clearfix">
										<h5>
											所维护的地理区域：
										</h5>
										<div class="network_area_tree" id="repair_area_tree_div"
											style="height: 200px; overflow-y: auto;">
										</div>
									</div>
									<div class="network_type">
										<h5>
											所维护的网络设施类型：
										</h5>
										<div class="network_type_info" id="repair_area_type_info" >
											<h3>
												物理点资源：分类授权
											</h3>
											<div class="network_type_facility" id="repair_divide_resource_div">
												<%-- 资源checkbox树 --%>
											</div>
											<h3>
												逻辑网资源：分类授权
											</h3>
											<div class="network_type_facility" id="repair_divide_line_div">
												<%-- 网络checkbox树 --%>
											</div>
										</div>
									</div>
									<div class="network_type_but">
										<input id="project_repair_button" type="button" value="保存" />
									</div>
								</div>
								<%-- 维护组织划分 end --%>
							</div>
						</li>
					</ul>
				</div>
		
				
				<%--
				
				
				<div id="projectService_tab" class="projectService_tab tab_menu">
					<ul>
						<li>
							项目基本信息
						</li>
						<li class="selected">
							项目网络资源
						</li>
						<li>
							维护组织划分
						</li>
					</ul>
				</div>

				<%-- 内容 begin --%>
				<div class="projectService_content">
					<%-- 隐藏域 --%>
					<input type="hidden" value="" column="project#id" id="projectId" />
					<input type="hidden" value="" column="project#cityId" id="cityId" />
					<input type="hidden" value="" column="project#serverOrgId" id="serverOrgId" />

					<%-- 项目基本信息 begin --%>
					<div id="projectService_tab_0" style="display: none;">
						<table class="dialog_table">
							<tbody>
								<tr>
									<td class="menu_td">
										项目编号：
									</td>
									<td>
										<span column="project#proId"></span>
									</td>
									<td class="menu_td">
										项目所在地：
									</td>
									<td>
										<span column="project#cityName" class="info_value"></span>
									</td>
								</tr>
								<tr>
									<td class="menu_td">
										项目名称：
									</td>
									<td colspan="3">
										<span column="project#name" class="info_value"></span>
									</td>
								</tr>
								<tr>
									<td class="menu_td">
										项目职责描述：
									</td>
									<td colspan="3">
										<span column="project#responsibilityDescription"
											class="info_value"></span>
									</td>
								</tr>
								<tr>
									<td class="menu_td">
										电子版项目合同：
									</td>
									<td colspan="3">
										<span column="project#agreement" class="info_value"><em
											class="grayStar"></em>
										</span>
									</td>
								</tr>
								<tr>
									<td class="menu_td">
										项目启动日期：
									</td>
									<td colspan="3">
										<span column="project#startDate" class="info_value"></span>
									</td>
								</tr>
								<tr>
									<td class="menu_td">
										预计结束日期：
									</td>
									<td colspan="3">
										<span column="project#planEndDate" class="info_value"></span>
									</td>
								</tr>
								<tr>
									<td class="menu_td">
										客户（甲方）：
									</td>
									<td colspan="3">
										<span column="project#clientEnterpriseName" class="info_value"></span>
									</td>
								</tr>
								<tr>
									<td class="menu_td">
										服务商（乙方）：
									</td>
									<td colspan="3">
										<span column="project#serverEnterpriseName" class="info_value"></span>
									</td>
								</tr>
								<tr>
									<td class="menu_td">
										客户负责组织：
									</td>
									<td colspan="3">
										<span column="project#clientOrgFullName" class="info_value"></span>
									</td>
								</tr>
								<tr>
									<td class="menu_td">
										服务商负责组织：
									</td>
									<td colspan="3">
										<span column="project#serverOrgFullName" class="info_value"></span>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<%-- 项目基本信息 end --%>



					<%-- 项目网络资源 begin --%>
					<div id="projectService_tab_1">
						<%-- 隐藏域 --%>
						<input type="hidden" id="network_resource_areaId" />
						<input type="hidden" id="network_resource_orgId"
							column="project#serverOrgId" />

						<div class="network_area clearfix">
							<h5>
								所维护的地理区域：
							</h5>
							<div class="network_area_tree">
								<div class="area_tree"  id="project_network_areatree_div"></div>
							</div>
						</div>
						<div class="network_type">
							<h5>
								所维护的网络设施类型：
							</h5>
							<div class="network_type_info" id="network_area_type_info" >
								<h3>
									物理点资源：分类授权
								</h3>
								<div class="network_type_facility"
									id="project_reswork_resource_div" style="height: 215px;">
									<%-- 资源checkbox树 --%>
								</div>
								<h3>
									逻辑网资源：分类授权
								</h3>
								<div class="network_type_facility" id="project_reswork_line_div">
									<%-- 网络checkbox树 --%>
								</div>
							</div>
						</div>
						<div class="network_type_but">
							<input id="project_network_button" type="button" value="保存" />
						</div>
					</div>
					<%-- 项目网络资源 end --%>

					<%-- 维护组织划分 begin --%>
					<div id="projectService_tab_2" style="display: none;">
						<%-- 隐藏域 --%>
						<input type="hidden" id="network_repair_resource_areaId" />
						<input type="hidden" id="network_repair_resource_orgId" />
						
						<div class="network_area clearfix">
							<h5>
								服务商维护组织：
							</h5>
							<div class="network_area_tree" id="server_treeDiv"
								style="height: 200px; overflow-y: auto;">

							</div>
						</div>
						<div class="network_area clearfix">
							<h5>
								所维护的地理区域：
							</h5>
							<div class="network_area_tree" id="repair_area_tree_div"
								style="height: 200px; overflow-y: auto;">

							</div>
						</div>
						<div class="network_type">
							<h5>
								所维护的网络设施类型：
							</h5>
							<div class="network_type_info" id="repair_area_type_info" >
								<h3>
									物理点资源：分类授权
								</h3>
								<div class="network_type_facility" id="repair_divide_resource_div">
									<%-- 资源checkbox树 --%>
								</div>
								<h3>
									逻辑网资源：分类授权
								</h3>
								<div class="network_type_facility" id="repair_divide_line_div">
									<%-- 网络checkbox树 --%>
								</div>
							</div>
						</div>
						<div class="network_type_but">
							<input id="project_repair_button" type="button" value="保存" />
						</div>
					</div>
					<%-- 维护组织划分 end --%>

				</div>
				<%-- 内容 end --%>
				 
			--%></div>
			<%--工作区 end --%>
		</div>
		<%-- 主体开始 end --%>
	</body>
</html>