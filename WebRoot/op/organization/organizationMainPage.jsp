<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<title>组织架构</title>
		<link rel="stylesheet" type="text/css" href="../../css/base.css" />
		<link rel="stylesheet" type="text/css" href="../../css/public.css" />
		<link rel="stylesheet" type="text/css" href="css/architecture.css" />
		<link rel="stylesheet" type="text/css" href="../css/selectStation.css" />
		<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
		<link rel="stylesheet" type="text/css" href="../../jslib/dialog/dialog.css"/>
		<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css"/>
		<style type="text/css">
		.areaservice_tree{display:none;top:66%;background-color:#FFFFFF; border: 1px solid #CCCCCC;height: 358px; margin-top: -227px; *+margin-top: -202px; *+margin-left: -160px;  position: absolute; width: 450px; z-index: 1000;}
		.areaservice_treeDiv{height: 329px; overflow: auto;  width: 450px; z-index: 1000;}
		
		</style>
		<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src="../../jslib/date/wdatePicker.js"></script>
		<script type="text/javascript" src="../../jslib/common.js"></script>
		<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
		<script type="text/javascript" src="js/tree_org.js"></script>
		<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
		<script type="text/javascript" src="../jslib/generateTree.js"></script>
		<script type="text/javascript" src="jslib/organizationMainPage.js"></script>
		<script type="text/javascript" src="../jslib/resourceMultiDivPage.js"></script>
		<script type="text/javascript" src="../js/tab.js" ></script>
		<%-- 
		<link rel="stylesheet" type="text/css"
			href="../informationmanage/css/projectInfoManage.css" />
		<link rel="stylesheet" type="text/css"
			href="../informationmanage/css/iscreate_treeview.css" />
			 --%>
		<%-- 帮助类 --%>
		<script type="text/javascript" src="../informationmanage/js/util/objutil.js"></script>
		<script type="text/javascript" src="../informationmanage/js/util/tablePage.js"></script>
		<script type="text/javascript" src="../informationmanage/js/util/new_formcheck.js"></script>
		<script type="text/javascript" src="../informationmanage/js/util/showedit.js"></script>
		<script type="text/javascript" src="../informationmanage/js/util/areaTreeView.js"></script>
		<%-- 类库 --%>
		<script type="text/javascript" src="../informationmanage/js/class/project.js"></script>
		<%-- 页面js --%>
		<script type="text/javascript" src="jslib/org_networkresource_info.js"></script>
		
		<script type="text/javascript">
			$(function() {
				//组织添加/修改基本信息弹出框
			    $(".serviceInfo_showBtn").live("click",function(){
				    $("#serviceInfo_Dialog").show();
					$(".black").show();
			    })
				//组织查看管辖人员弹出框
				$(".serviceStaff_showBtn").live("click",function(){
				    $("#serviceStaff_Dialog").show();
					$(".black").show();
			    })
				//组织添加/修改管辖人员弹出框
				$(".serviceAddStaff_showBtn").live("click",function(){
				    $("#serviceAddStaff_Dialog").show();
					$(".black").show();
			    })
				//关闭弹出框
			    $(".dialog_closeBtn").live("click",function(){
				    $("#serviceInfo_Dialog,#serviceStaff_Dialog,#serviceAddStaff_Dialog,#personnelDelete_Dialog").hide();
					$(".black").hide();
			    })
				//授予角色弹出框
				$(".serviceRole_showBtn").live("click",function(){
				    $("#serviceRole_Dialog").show();
					$(".role_black").show();
			    })
			    $(".serviceRole_closeBtn").live("click",function(){
				    $("#serviceRole_Dialog").hide();
					$(".role_black").hide();
			    })
				//删除提示框
				$(".personnelDelete_showBtn").live("click",function(){
				    $("#personnelDelete_Dialog").show();
					$(".black").show();
			    })
				
			    //显示隐藏左边框
				$(".show_hide_img").live("click",function(){
					$(".architecture_left").toggle();
					$(".architecture_structure_menu ul").toggle();
					if($(this).attr("src") == "images/hide_right.png"){
						$(this).attr("src","images/show_right.png");
					}else{
						$(this).attr("src","images/hide_right.png");
					}
				});
				//tab选项卡
				tab("architecture_structure","li","onclick");//组织类别切换
				tab("service_tab","li","onclick");//服务商组织tab切换
				tab("client_tab","li","onclick");//客户组织tab切换
				tab("materials_tab","li","onclick");//物资浏览tab切换
				tab("staff_tab","li","onclick");//管辖人员tab切换
				tab("staffadd_tab","li","onclick");//管辖人员tab切换
				tab("endow_role","li","onclick");//赋予角色tab切换
				tab("endowadd_role","li","onclick");//赋予角色tab切换
				tab("award_role","li","onclick");//授予角色tab切换
				//tab("business_role","li","onclick");//业务角色tab切换
				
				//添加人员的帐号信息
				$("#staffadd_tab_0 .staff_save").live("click",function(){
					/*
					$(".staff_li").show();
					$(".staffadd_info_account .staffInfo_value").show();
					$(".staffadd_account_but").hide();
					$(".staffadd_info_account .staffInfo_input").hide();
					*/
				});
				
				
				
				
				//查看/修改人员的帐号信息
				$("#staff_tab_0 .staffInfo_modify").live("click",function(){
					$(".staff_info_account .staffInfo_input,.staff_account_but .staffInfo_save").show();
					$(".staff_info_account .staffInfo_value,.staff_account_but .staffInfo_modify").hide();
				});
				$("#staff_tab_0 .staffInfo_save").live("click",function(){
					/*
					$(".staff_info_account .staffInfo_value,.staff_account_but .staffInfo_modify").show();
					$(".staff_info_account .staffInfo_input,.staff_account_but .staffInfo_save").hide();
					*/
				});
				//选择上级组织树
				$(".highOrg_but").click(function(){
					$(".highOrg_tree").slideToggle("fast");
				});
			})
		</script>
  	</head>
  
  	<body>
  		<input type="hidden" id="c_orgId" />
  		<input type="hidden" id="p_orgId" />
  		<input type="hidden" id="orgId" />
  		<input type="hidden" id="orgName" />
  		<input type="hidden" id="roleIdList" />
  		<input type="hidden" id="bizRoleIdList" />
  		<input type="hidden" id="roleNameList" />
  		<input type="hidden" id="orgRole_tableId" />
  		<input type="hidden" id="bizRole_tableId" />
  		<input type="hidden" id="account_downOrgId" />
  		<input type="hidden" id="enterpriseId" />
  		<input type="hidden" id="isCoo" />
  		<input type="hidden" id="org_c_enterpriseId" />
  		<input type="hidden" id="org_p_enterpriseId" />
  		<input type="hidden" id="org_c_enterpriseIds" />
  		<input type="hidden" id="org_p_enterpriseIds" />
  		<input type="hidden" id="add_top_type" />
  		
	    <div class="architecture clearfix">
			<%--头部--%>
			<div class="architecture_menu_title">
			    <p>组织架构</p>
			</div>
			<%--页面信息--%>
			<div class="architecture_structure_info">
				<%--组织切换--%>
				<div id="architecture_structure" class="architecture_structure_menu clearfix">
					<ul>
						<li class="selected">服务商组织</li>
						<li>运营商组织</li>
					</ul>
					<em class="add_root_architecture" onclick="showAddTopOrgDiv();"></em>
				</div>
			    <%--服务商组织--%>
			    <div id="architecture_structure_0">
					<%--左边树开始--%>
					<div class="architecture_left" id="treeDiv">
						
					</div>
					<div class="show_hide">
	                    <img class="show_hide_img" src="images/hide_right.png">
	                </div>
					<%--左边树结束--%>
					<%--右边tab开始--%>
					<div class="architecture_right" id="providerOrgDiv" style="display:none;">
						<div class="architecture_right_title" id="mainOrgName">组织:东一片区</div>
						<div id="service_tab" class="tab_menu">
							<ul>
								<li class="selected" style="border-left: 1px solid #99BBE8;">基本信息</li>
								<li>管辖人员</li>
								<li style="display:none;">管辖车辆</li>
								<li style="display:none;">管辖物资</li>
								<li>维护网络资源</li>
							</ul>
						</div>
						<%--tab title结束--%>
						
						<%--tab 信息切换开始--%>
						<div class="container_info">
							<%--基本信息开始--%>
							<div id="service_tab_0">
								
							</div>
							<%--管辖人员开始--%>
							<div id="service_tab_1" style="display:none;">
								<div class="container_info_pe_top clearfix">
									<div class="pe_left">
										<input type="button" value="添加" class="serviceAddStaff_showBtn authorityControlClass" onclick="showSaveProviderAccountInfo();" />
										<input type="button" value="选择人员" class="authorityControlClass" onclick="openStaffMultiDivByAccount('staffList')"/>
										<input type="button" value="删除" class="authorityControlClass" onclick="deleteProviderStaffInfo();" />
									</div>
									<div class="pe_right">
										<input type="text" id="org_p_staff_fuzzy" style="width:200px">
										<input type="button" value="过滤" onclick="orgShowStaffFuzzy('org_p_staff');">
									</div>
								</div>
								<div id="org_p_staff_table"></div>
								
								<div>
									<div id="org_p_staff_page"></div>
								</div>
								
								<%-- 弹出人员 结束 --%>
								<div id="black"></div>
							</div>
							<%--管辖车辆开始--%>
							<div id="service_tab_2" style="display: none;">
								<div class="car_title">车辆列表信息</div>
								<table class="main_table1 tc">
									<tr>
										<th><input type="checkbox">全选</th>
										<th>车辆号码</th>
										<th>车辆类型</th>
										<th>所属区域</th>
										<th>司机姓名</th>
										<th>司机号码</th>
										<th>车载终端</th>
									</tr>
									<tr>
										<td><input type="checkbox"/></td>
										<td>粤At123</td>
										<td>小轿车</td>
										<td>东二区维护二组</td>
										<td>黄达成</td>
										<td>01234567891</td>
										<td>19885321213215832</td>
									</tr>
									<tr>
										<td><input type="checkbox" /></td>
										<td>粤At123</td>
										<td>小轿车</td>
										<td>东二区维护二组</td>
										<td>黄达成</td>
										<td>01234567891</td>
										<td>19885321213215832</td>
									</tr>
								</table>
								<div class="main_table_foot clearfix">
									<input type="button" class="input_button" value="删除" />&nbsp;<input type="button" class="input_button" value="添加车辆" />
								</div>
							</div>
							<%--管辖物资开始--%>
							<div id="service_tab_3" class="clearfix" style="display: none;">
								<div class="materials_tree">
									<div class="materials_treeview">
										<ul id="materialsTree" class="filetree architecture_tree">
											<li><span class="folder">文件夹 1</span>
												<ul>
													<li><span class="file">文件→(class="file")</span></li>
													<li><span class="computer">计算机→(class="computer")</span></li>
													<li><span class="phone">手持终端→(class="phone")</span></li>
												</ul>
											</li>
											<li><span class="folder">文件夹 2</span>
												<ul>
													<li><span class="folder">ico 列表</span>
														<ul id="folder21">
															<li><span class="cangku"> 仓库→(class="cangku")</span></li>
															<li><span class="car"> 车辆→(class="car")</span></li>
															<li><span class="guanjin">管井→(class="guanjin")</span></li>
															<li><span class="jizhan">基站→(class="jizhan")</span></li>
															<li><span class="people">人员→(class="people")</span></li>
															<li><span class="peoples">维护队→(class="peoples")</span></li>
															<li><span class="shiyebu">事业部→(class="shiyebu")</span></li>
															<li><span class="tuceng">图层→(class="tuceng")</span></li>
															<li><span class="wentidian">问题点→(class="wentidian")</span></li>
															<li><span class="zhudi">驻地→(class="zhudi")</span></li>
															<li><span class="zhudian">驻点→(class="zhudian")</span></li>
															<li><span class="zongbu">总部→(class="zongbu")</span></li>
														</ul>
													</li>
												</ul>
											</li>
											<li class="closed"><span class="folder">文件夹 4 （class="closed" 默认折叠）</span>
												<ul>
													<li><span class="file">File 4.1</span></li>
													<li><span class="file">File 4.2</span></li>
												</ul>
											</li>
										</ul>
									</div>
								</div>
								<div class="materials_browse">
									<div class="materials_browse_info">
									    <h4>物资浏览</h4>
										<div id="materials_tab" class="materials_tab tab_menu">
											<ul>
												<li class="selected" style="border-left: 1px solid #99BBE8;">通用物资查询</li>
												<li>工具资产查询</li>
												<li>设备条件查询</li>
												<li>易耗品查询</li>
											</ul>
										</div>
										<%--通用物资查询开始--%>
										<div id="materials_tab_0">
											<div class="materials_browse_inquiry">
												<ul>
													<li><input type="checkbox"><em>过滤查询：</em></li>
													<li>
														<label>物资名：</label>
														<span><input type="text"/></span>
														<label>物资形态：</label>
														<span>
															<select name="11">  
																<option value="1">全部</option> 
																<option value="2">小部份</option>
																<option value="3">大部份</option>
															</select>
														</span>
														<label>物资分类：</label>
														<span>
															<select name="22">  
																<option value="1">A类</option> 
																<option value="2">B类</option>
																<option value="3">C类</option>
															</select>
														</span>
													</li>
													<li>
														<label>主计量单位：</label>
														<span>
															<select name="11">  
															   <option value="1">个</option> 
															   <option value="2">百</option>
															   <option value="3">千</option>
															</select>
														</span>
														<label>数量：</label>
														<span>
															大于<input type="text"/>
															<select name="22" style="width:50px;">  
															   <option value="1">且</option> 
															   <option value="2">或</option>
															</select>
															小于<input type="text"/>
														</span>
													</li>
												</ul>
											</div>
											<div class="materials_browse_list">
												<h5 class="clearfix"><span>查询结果：</span><span class="fr"><input type="button" value="创建出库单"></span></h5>
												<table class="main_table1 tc">
													<tr>
														<th><input type="checkbox">全选</th>
														<th>物资名</th>
														<th>物资形态</th>
														<th>物资用途</th>
														<th>物资分级</th>
														<th>主计量单位</th>
														<th>数量</th>
													</tr>
													<tr>
														<td><input type="checkbox" /></td>
														<td>PSU</td>
														<td>设备条件</td>
														<td>抢修备件</td>
														<td>A类</td>
														<td>个</td>
														<td><a href="#">1000</a></td>
													</tr>
													<tr>
														<td><input type="checkbox" /></td>
														<td>PSU</td>
														<td>设备条件</td>
														<td>抢修备件</td>
														<td>A类</td>
														<td>个</td>
														<td><a href="#">1000</a></td>
													</tr>
												</table>
											</div>
									    </div>
										<%--工具资产查询开始--%>
										<div id="materials_tab_1" style="display:none;">正在开发中。。。</div>
										<%--设备条件查询开始--%>
										<div id="materials_tab_2" style="display:none;">正在开发中。。。</div>
										<%--易耗品查询开始--%>
										<div id="materials_tab_3" style="display:none;">正在开发中。。。</div>
									</div>
								</div>
							</div>
							<%--维护网络资源开始--%>
							<div id="service_tab_4" style="display: none; ">
								<input type="hidden" id="p_projectId" />
							<%-- 
								<iframe id="stationLegacyProblem" src="../informationmanage/show_networkresource_info.jsp" frameborder='0' width='100%' height='100%'></iframe>
							 --%>
								<div class="network_top" id="p_network_top">
									<input type="checkbox" checked="checked"><em>广州海珠基站一体化维护项目<a href="#">查看详情</a></em>
									<input type="checkbox" checked="checked"><em>广州海珠无线网络测试项目<a href="#">查看详情</a></em>
									<input type="checkbox" checked="checked"><em>广州海珠网络政治工程项目<a href="#">查看详情</a></em>
								</div>
								<div class="network_main clearfix" id="p_network_main">
									
								</div>
								<%-- <div class="network_button"><button>保存</button></div> --%>
							</div>
							
						</div>
						<%--tab 信息切换结束--%>
					</div>
					<%--右边tab结束--%>
				</div>
				<%--客户组织--%>
				<div id="architecture_structure_1" style="display:none;">
					<%--左边树开始--%>
					<div class="architecture_left" id="customerTreeDiv">
						
					</div>
					<div class="show_hide">
	                    <img class="show_hide_img" src="images/hide_right.png">
	                </div>
					<%--左边树结束--%>
					<%--右边tab开始--%>
					<div class="architecture_right" id="customerOrgDiv" style="display:none;">
						<div class="architecture_right_title" id="c_orgMainName">无线网络优化室</div>
						<div id="client_tab" class="tab_menu">
							<ul>
								<li class="selected" style="border-left: 1px solid #99BBE8;">基本信息</li>
								<li>管辖人员</li>
								<li style="display:none;">管理网络资源</li>
							</ul>
						</div>
						<%--tab title结束--%>
						<%--tab 信息切换开始--%>
						<div class="container_info">
							<%--基本信息开始--%>
							<div id="client_tab_0">
								
							</div>
							<%--管辖人员开始--%>
							<div id="client_tab_1" style="display:none;">
								<div class="container_info_pe_top clearfix">
									<div class="pe_left">
										<input type="button" id="add_account_c_button" value="添加" onclick="showAddCustomerAccount();" />
										<input type="button" value="选择人员" onclick="openStaffMultiDivByAccount('staffList');" />
										<input type="button" value="删除" onclick="deleteCustomerStaffInfo();" />
									</div>
									<div class="pe_right">
										<input type="text" id="org_c_staff_fuzzy" style="width:200px">
										<input type="button" id="" value="过滤" onclick="orgShowStaffFuzzy('org_c_staff');">
									</div>
								</div>
								<div id="org_c_staff_table"></div>
								
								<div>
									<div id="org_c_staff_page"></div>
								</div>
							</div>
							<%--管理网络资源开始--%>
							<div id="client_tab_2" style="display: none;">
								<div class="network_top">
									<input type="checkbox" checked="checked"><em>广州海珠基站一体化维护项目<a href="#">查看详情</a></em>
									<input type="checkbox" checked="checked"><em>广州海珠无线网络测试项目<a href="#">查看详情</a></em>
									<input type="checkbox" checked="checked"><em>广州海珠网络政治工程项目<a href="#">查看详情</a></em>
								</div>
								<div class="network_main clearfix">
									
								</div>
							</div>
						</div>
						<%--tab 信息切换结束--%>
					</div>
					<%--右边tab结束--%>
				</div>
			</div>
			<%--弹出框--%>
			<div class="architecture_dialog">
				<%--基本信息弹出框--%>
				<div id="add_top_org" class="dialog architecture_dialog_basic" style="display:none;">
					<div class="dialog_header">
						<div class="dialog_title">添加<em id="add_top_org_title"></em>根组织</div>
						<div class="dialog_tool">
						   <div class="dialog_tool_close dialog_closeBtn" onclick="closeAddTopOrgDiv();"></div>
						</div>
					</div>
					<form action="" method="post">
						<div class="dialog_content" id="org_top_orgInfo_div">
							<%-- 组织编辑框 --%>
							<table class="dialog_table">
								<tbody>
									<tr>
										<td class="menu_td">区域选择：</td><%-- yuan.yw --%>
										<td colspan="3" style="width:240px">
											<input name="" type="text" class="add_top_org" id="add_top_org_areaname"  value="" />
											<input name="" type="hidden" class="add_top_org" id="add_top_org_areaId" value="" />
											<a class="orgButton" style="margin-left:-27px;" href="#" editButton="true" id="area_server_choice_btn"></a>
											<%--  <div id="area_server_treeDiv" class="areaservice_tree" name='add_top_org'>
												
											</div>--%>
											<div id="area_server_Div" class="areaservice_tree">
												<div id="area_server_treeDiv"  name='add_top_org' class="areaservice_treeDiv" >
													<%-- 组织树 --%>
												</div>
												<div class="dialog_but" style='border-top:1px solid #CCCCCC'>
													<input type="button" style="width:66px" class="aui_state_highlight" onclick="confirmChooseArea('area_server_treeDiv','add_top_org',this)" value="确定"></input>
													<input type="button" style="width:66px" class="aui_state_highlight" onclick="cancelChooseArea(this)" value="取消"></input>
												</div>
											</div>
										</td>
									</tr>
									<tr>
										<td class="menu_td">组织名称：</td>
										<td colspan="3">
											<input name="" type="text" class="add_top_org" id="add_top_org_name" value="" />
											<span class="redStar">*</span>
											<em class="redStar top_org_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">所属企业：</td>
										<td colspan="3">
											<em id="add_top_org_enterpriseId_em" class="add_top_org">
												<select id="add_top_org_enterpriseId" class="add_top_org">
													<option value="">请选择</option>
												</select>
											</em>
											<span class="redStar">*</span>
											<em class="redStar top_org_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">组织属性：</td>
										<td class="w_230">
											<em id="add_top_org_orgAttribute_em" class="add_top_org">
												<select id="add_top_org_orgAttribute" class="add_top_org">
													<option value="">请选择</option>
												</select>
											</em>
											<span class="redStar">*</span>
											<em class="redStar top_org_error"></em>
										</td>
										<td class="menu_td">组织类型：</td>
										<td class="w_230">
											<em id="add_top_org_orgType_em">
												<select id="add_top_org_orgType" class="add_top_org">
													<option value="">请选择</option>
												</select>
											</em>
											<span class="redStar">*</span>
											<em class="redStar top_org_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">组织职责：</td>
										<td colspan="3">
											<textarea class="add_top_org" rows="4" id="add_top_org_orgDuty"></textarea>
											<span class="redStar"></span>
											<em class="redStar top_org_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">驻点位置：</td>
										<td colspan="3">
											<input class="add_top_org" type="text" id="add_top_org_address" value="" />
											<span class="redStar"></span>
											<em class="redStar top_org_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">驻点经度：</td>
										<td class="w_230">
											<input class="add_top_org" type="text" id="add_top_org_longitude" value="" />
											<span class="redStar"></span>
											<em class="redStar top_org_error"></em>
										</td>
										<td class="menu_td">驻点纬度：</td>
										<td class="w_230">
											<input class="add_top_org" type="text" id="add_top_org_latitude" value="" />
											<span class="redStar"></span>
											<em class="redStar top_org_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">驻点电话：</td>
										<td colspan="3">
											<input class="add_top_org" type="text" id="add_top_org_contactPhone" value="" />
											<span class="redStar"></span>
											<em class="redStar top_org_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">负责人：</td>
										<td class="w_230">
											<em id="add_top_org_dutyPerson_em" class="add_top_org">
												<select id="add_top_org_dutyPerson">
												</select>
											</em>
										</td>
										<td class="menu_td">负责人电话：</td>
										<td class="w_230">
											<input class="add_top_org" type="text" id="add_top_org_dutyPersonPhone" value="" />
											<span class="redStar"></span>
											<em class="redStar top_org_error"></em>
										</td>
									</tr>
									<%-- <tr>
										<td class="menu_td">所属企业：</td>
										<td colspan="3">
											<em class="update_p_baseInfo org_p_orgInfo" id="update_org_p_enterpriseId"></em>
											<span class="add_p_baseInfo">
												<em id="select_enterpriseId" class="org_p_enterpriseId">
													<select id="org_p_enterpriseId">
													</select>
												</em>
												<span class="redStar">*</span>
												<em class="redStar org_p_error"></em>
											</span>
										</td>
									</tr>--%>
								</tbody>
							</table>
							<div class="dialog_but">
								<button type="button" class="aui_state_highlight add_top_baseInfo" onclick="addTopOrg();">保存</button>
							</div>
						</div>
					</form>
				</div>
				<%--基本信息弹出框--%>
				<div id="serviceInfo_Dialog" class="dialog architecture_dialog_basic" style="display:none;">
					<div class="dialog_header">
						<div class="dialog_title"><em class="add_p_baseInfo">组织添加</em><em class="update_p_baseInfo">组织修改</em></div>
						<div class="dialog_tool">
						   <div class="dialog_tool_close dialog_closeBtn"></div>
						</div>
					</div>
					<form action="" method="post">
						<div class="dialog_content" id="org_p_orgInfo_div">
							<%-- 组织编辑框 --%>
							<table class="dialog_table">
								<tbody>
									<tr>
										<td class="menu_td">区域选择：</td><%-- yuan.yw --%>
										<td colspan="3" style="width:240px">
											<input name="" type="text" class="org_p_orgInfo" id="org_p_areaname"  value="" />
											<input name="" type="hidden" class="org_p_orgInfo" id="org_p_areaId" value="" />
											<a class="orgButton" style="margin-left:-27px;" href="#" editButton="true" id="org_p_area_server_choice_btn"></a>
											<div id="org_p_area_server_Div" class="areaservice_tree">
												<div id="org_p_area_server_treeDiv"  name='org_p' class="areaservice_treeDiv" >
													<%-- 组织树 --%>
												</div>
												<div class="dialog_but" style='border-top:1px solid #CCCCCC'>
													<input type="button" style="width:66px" class="aui_state_highlight" onclick="confirmChooseArea('org_p_area_server_treeDiv','org_p',this)" value="确定"></input>
													<input type="button" style="width:66px" class="aui_state_highlight" onclick="cancelChooseArea(this)" value="取消"></input>
												</div>
											</div>
											
										</td>
									</tr>
									<tr>
										<td class="menu_td">组织名称：</td>
										<td colspan="3">
											<input name="" type="text" class="org_p_orgInfo" id="org_p_name" value="" />
											<span class="redStar">*</span>
											<em class="redStar org_p_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">上级组织：</td>
										<td colspan="3">
											<em class="add_p_baseInfo org_p_orgInfo" id="add_org_p_parentOrgName"></em>
											<span class="update_p_baseInfo">
												<input class="w_335" id="update_org_p_parentOrgName" type="text" />
												<input class="w_335" id="update_org_p_parentOrgId" type="hidden" />
												<a href="#" class="orgButton highOrg_but"></a>
												<em class="redStar">*</em>
												<em class="redStar org_p_error"></em>
											</span>
											<div id="highOrgTree" class="filetree highOrg_tree">
											
											</div>
										</td>
									</tr>
									<tr>
										<td class="menu_td">组织属性：</td>
										<td class="w_230">
											<em id="orgAttribute" class="org_p_orgInfo">
												<select>
													<option value="">请选择</option>
												</select>
											</em>
											<span class="redStar">*</span>
											<em class="redStar org_p_error"></em>
										</td>
										<td class="menu_td">组织类型：</td>
										<td class="w_230">
											<em id="orgType">
												<select class="org_p_orgInfo">
													<option value="">请选择</option>
												</select>
											</em>
											<span class="redStar">*</span>
											<em class="redStar org_p_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">组织职责：</td>
										<td colspan="3"><textarea class="org_p_orgInfo" rows="4" id="org_p_orgDuty"></textarea>
											<span class="redStar"></span>
											<em class="redStar org_p_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">驻点位置：</td>
										<td colspan="3">
											<input class="org_p_orgInfo" type="text" id="org_p_address" value="" />
											<span class="redStar"></span>
											<em class="redStar org_p_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">驻点经度：</td>
										<td class="w_230">
											<input class="org_p_orgInfo" type="text" id="org_p_longitude" value="" />
											<span class="redStar"></span>
											<em class="redStar org_p_error"></em>
										</td>
										<td class="menu_td">驻点纬度：</td>
										<td class="w_230">
											<input class="org_p_orgInfo" type="text" id="org_p_latitude" value="" />
											<span class="redStar"></span>
											<em class="redStar org_p_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">驻点电话：</td>
										<td colspan="3">
											<input class="org_p_orgInfo" type="text" id="org_p_contactPhone" value="" />
											<span class="redStar"></span>
											<em class="redStar org_p_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">负责人：</td>
										<td class="w_230">
											<em id="dutyPerson" class="org_p_orgInfo">
												<select id="org_p_dutyPerson">
												</select>
											</em>
										</td>
										<td class="menu_td">负责人电话：</td>
										<td class="w_230">
											<input class="org_p_orgInfo" type="text" id="org_p_dutyPersonPhone" value="" />
											<span class="redStar"></span>
											<em class="redStar org_p_error"></em>
										</td>
									</tr>
									<%-- <tr>
										<td class="menu_td">所属企业：</td>
										<td colspan="3">
											<em class="update_p_baseInfo org_p_orgInfo" id="update_org_p_enterpriseId"></em>
											<span class="add_p_baseInfo">
												<em id="select_enterpriseId" class="org_p_enterpriseId">
													<select id="org_p_enterpriseId">
													</select>
												</em>
												<span class="redStar">*</span>
												<em class="redStar org_p_error"></em>
											</span>
										</td>
									</tr>--%>
								</tbody>
							</table>
							<div class="dialog_but">
								<button type="button" class="aui_state_highlight add_p_baseInfo" onclick="confirmAddProviderOrgBaseInfo();">保存</button>
								<button type="button" class="aui_state_highlight update_p_baseInfo" onclick="confirmUpdateProviderOrgBaseInfo();">修改</button>
							</div>
						</div>
					</form>
				</div>
				<%--客户商基本信息弹出框--%>
				<div id="customerInfo_Dialog" class="dialog architecture_dialog_basic" style="display:none;">
					<div class="dialog_header">
						<div class="dialog_title"><em class="add_c_baseInfo">组织添加</em><em class="update_c_baseInfo">组织修改</em></div>
						<div class="dialog_tool">
						   <div class="dialog_tool_close dialog_closeBtn" onclick="closeCustomerOrgBaseInfoWin();"></div>
						</div>
					</div>
					<form action="" method="post">
						<div class="dialog_content" id="org_c_orgInfo_div">
							<%-- 组织编辑框 --%>
							<table class="dialog_table">
								<tbody>
									<tr>
									<td class="menu_td">区域选择：</td><%-- yuan.yw --%>
										<td colspan="3" style="width:240px">
											<input name="" type="text" class="org_c_orgInfo" id="org_c_areaname"  value="" />
											<input name="" type="hidden" class="org_c_orgInfo" id="org_c_areaId" value="" />
											<a class="orgButton" style="margin-left:-27px;" href="#" editButton="true" id="org_c_area_server_choice_btn"></a>
											<%--<div id="org_c_area_server_treeDiv" class="areaservice_tree" name='org_c'>
												
											</div>  --%>
											<div id="org_c_area_server_Div" class="areaservice_tree">
												<div id="org_c_area_server_treeDiv"  name='org_c' class="areaservice_treeDiv" >
													<%-- 组织树 --%>
												</div>
												<div class="dialog_but" style='border-top:1px solid #CCCCCC'>
													<input type="button" style="width:66px" class="aui_state_highlight" onclick="confirmChooseArea('org_c_area_server_treeDiv','org_c',this)" value="确定"></input>
													<input type="button" style="width:66px" class="aui_state_highlight" onclick="cancelChooseArea(this)" value="取消"></input>
												</div>
											</div>
									    </td>
									</tr>
									<tr>
										<td class="menu_td">组织名称：</td>
										<td colspan="3">
											<input name="" type="text" class="org_c_orgInfo" id="org_c_name" value="" />
											<span class="redStar">*</span>
											<em class="redStar org_c_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">上级组织：</td>
										<td colspan="3">
											<em class="add_c_baseInfo org_c_orgInfo" id="add_org_c_parentOrgName"></em>
											<span class="update_c_baseInfo">
												<input class="w_335" id="update_org_c_parentOrgName" type="text" />
												<input class="w_335" id="update_org_c_parentOrgId" type="hidden" />
												<a href="#" class="orgButton highOrg_but"></a>
												<em class="redStar">*</em>
												<em class="redStar org_c_error"></em>
											</span>
											<div id="org_c_highOrgTree" class="filetree highOrg_tree">
											
											</div>
										</td>
									</tr>
									<tr>
										<td class="menu_td">组织属性：</td>
										<td class="w_230">
											<em id="em_org_c_orgAttribute" class="org_c_orgInfo">
												<select>
													<option value="">请选择</option>
												</select>
											</em>
											<span class="redStar">*</span>
											<em class="redStar org_c_error"></em>
										</td>
										
										<td class="menu_td">组织类型：</td>
										<td class="w_230">
											<em id="em_org_c_orgType">
												<select class="org_c_orgInfo">
													<option value="">请选择</option>
												</select>
											</em>
											<span class="redStar">*</span>
											<em class="redStar org_c_error"></em>
										</td>
										 
									</tr>
									<tr>
										<td class="menu_td">组织职责：</td>
										<td colspan="3">
											<textarea class="org_c_orgInfo" rows="4" id="org_c_orgDuty"></textarea>
											<span class="redStar"></span>
											<em class="redStar org_c_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">驻点位置：</td>
										<td colspan="3">
											<input class="org_c_orgInfo" type="text" id="org_c_address" value="" />
											<span class="redStar"></span>
											<em class="redStar org_c_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">驻点经度：</td>
										<td class="w_230">
											<input class="org_c_orgInfo" type="text" id="org_c_longitude" value="" />
											<span class="redStar"></span>
											<em class="redStar org_c_error"></em>
										</td>
										<td class="menu_td">驻点纬度：</td>
										<td class="w_230">
											<input class="org_c_orgInfo" type="text" id="org_c_latitude" value="" />
											<span class="redStar"></span>
											<em class="redStar org_c_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">驻点电话：</td>
										<td colspan="3">
											<input class="org_c_orgInfo" type="text" id="org_c_contactPhone" value="" />
											<span class="redStar"></span>
											<em class="redStar org_c_error"></em>
										</td>
									</tr>
									<tr>
										<td class="menu_td">负责人：</td>
										<td class="w_230">
											<em id="em_org_c_dutyPerson" class="org_c_orgInfo">
												<select id="org_c_dutyPerson">
												</select>
											</em>
										</td>
										<td class="menu_td">负责人电话：</td>
										<td class="w_230">
											<input class="org_c_orgInfo" type="text" id="org_c_dutyPersonPhone" value="" />
											<span class="redStar"></span>
											<em class="redStar org_c_error"></em>
										</td>
									</tr>
									 <%-- <tr>
										<td class="menu_td">所属企业：</td>
										<td colspan="3">
											<em class="update_c_baseInfo org_c_orgInfo" id="update_org_c_enterpriseId"></em>
											<span class="add_c_baseInfo">
												<em id="org_c_select_enterpriseId" class="org_c_enterpriseId">
													<select id="org_c_enterpriseId">
													</select>
												</em>
												<span class="redStar">*</span>
												<em class="redStar org_c_error"></em>
											</span>
										</td>
									</tr> --%>
								</tbody>
							</table>
							<div class="dialog_but">
								<button type="button" class="aui_state_highlight add_c_baseInfo" onclick="confirmAddCustomerOrgBaseInfo();">保存</button>
								<button type="button" class="aui_state_highlight update_c_baseInfo" onclick="confirmUpdateCustomerOrgBaseInfo();">修改</button>
							</div>
						</div>
					</form>
				</div>
				<%--查看管辖人员弹出框--%>
				<div id="serviceStaff_Dialog" class="dialog architecture_dialog_pe" style="display:none;">
					<div class="dialog_header">
						<div class="dialog_title">人员信息</div>
						<div class="dialog_tool">
						   <div class="dialog_tool_close dialog_closeBtn"></div>
						</div>
					</div>
					<div class="dialog_content">
						<div id="staff_tab" class="staff_menu tab_menu">
							<ul>
								<li class="selected" style="border-left: 1px solid #99BBE8;">帐号信息</li>
								<li style="display:none;">人员信息</li>
								<li style="display:none;">人员技能</li>
							</ul>
						</div>
						<div class="staff_content">
							<div id="staff_tab_0">
								<ul class="staff_info staff_info_account">
									<li>
										<label>帐号：</label>
										<span class="staffInfo_value"><em id="show_update_account_p_account"></em></span>
										<span class="staffInfo_input"><input id="update_account_p_account" type="text"><em id="update_account_p_enterpriseUrl"></em><em class="redStar">*</em><em class="redStar update_account_p_error"></em></span>
										<span class="staffInfo_input"><button class="detect" onclick="checkUpdateProviderAccount('update_account_p');">检测账号</button><input id="update_orgUserId" type="hidden">
									</li>
									<li>
										<label>密码：</label>
										<span class="staffInfo_value"><em id="show_update_account_p_password"></em></span>
										<span class="staffInfo_input"><input id="update_account_p_password" type="password" /><em class="redStar">*</em><em class="redStar update_account_p_error"></em></span>
									</li>
									<li>
										<label>确认密码：</label>
										<span class="staffInfo_value"><em id="show_update_account_p_comfigPwd"></em></span>
										<span class="staffInfo_input"><input id="update_account_p_comfigPwd" type="password" /><em class="redStar">*</em><em class="redStar update_account_p_error"></em></span>
									</li>
									<li>
										<label>姓名：</label>
										<span class="staffInfo_value"><em id="show_update_account_p_name"></em></span>
										<span class="staffInfo_input"><input id="update_account_p_name" type="text" /><em class="redStar">*</em><em class="redStar update_account_p_error"></em></span>
									</li>
									<li>
										<label>性别：</label>
										<span class="staffInfo_value"><em id="show_update_account_p_sex"></em></span>
										<span class="staffInfo_input"><input type="radio" value="male" name="update_account_p_sex">男</input><input type="radio" value="female" name="update_account_p_sex" >女</input></span>
									</li>
									<li>
										<label>手机号码：</label>
										<span class="staffInfo_value"><em id="show_update_account_p_cellPhoneNumber"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="update_account_p_cellPhoneNumber" type="text"><em class="redStar">*</em><em class="redStar update_account_p_error"></em></span>
									</li>
									<li>
										<label>邮箱：</label>
										<span class="staffInfo_value"><em id="show_update_account_p_email"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="update_account_p_email" type="text"><em class="redStar">*</em><em class="redStar update_account_p_error"></em></span>
									</li>
									<li>
										<label>手机邮箱：</label>
										<span class="staffInfo_value"><em id="show_update_account_p_mobileEmailAddress"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="update_account_p_mobileEmailAddress" type="text"><%--  <em class="redStar">*</em>--%><em class="redStar update_account_p_error"></em></span>
									</li>
									<li>
										<label>备用邮箱：</label>
										<span class="staffInfo_value"><em id="show_update_account_p_backUpEmailAddress"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="update_account_p_backUpEmailAddress" type="text"></span>
									</li>
									<li>
										<label>状态：</label>
										<span class="staffInfo_value"><em id="show_update_account_p_status"></em><input type="checkbox" class="show_update_account_p_status" name="show_update_account_p_status" disabled="disabled" checked="checked" />有效 </span>
										<span class="staffInfo_input"><em id="update_account_p_status"><input type="checkbox" class="update_account_p_status" name="update_account_p_status" checked="checked" />有效 </em></span>
									</li>
									<%-- 
									<li>
										<label>是否属于本企业：</label>
										<span class="staffInfo_value"><em id="show_update_account_p_isEnterprise"></em>&nbsp;</span>
										<span class="staffInfo_input"><em id="update_account_p_isEnterprise"><input class="update_account_p_isEnterprise" name="update_account_p_isEnterprise" checked="checked" type="radio" value="是" />是 <input class="update_account_p_isEnterprise" name="update_account_p_isEnterprise" type="radio" value="否" />否</em><em class="redStar">*</em><em class="redStar update_account_p_error"></em></span>
									</li>
									 --%>
									<li class="clearfix">
										<label>赋予角色：</label>
										<div class="role">
											<div id="endow_role" class="tab_menu">
												<ul>
													<li class="selected" style="border-left: 1px solid #ccc;">组织角色</li>
												</ul>
												<p class="staffInfo_input"><a class="role_a serviceRole_showBtn">授予角色</a></p>
											</div>
											<div class="endow_role_info">
												<div id="endow_role_0">
													<div class="role_title">
														<span>角色</span>
													</div>
													<div class="role_table">
														<table id="show_update_org_p_org_role">
														</table>
													</div>
												</div>
												<div id="endow_role_1" style="display:none;">
													<div class="role_title">
														<span>业务模块</span>
														<span>角色</span>
													</div>
													<div class="role_table">
														<table id="show_update_org_p_biz_role">
															<tr>
																<td class="role_table_tr">抢修业务</td>
																<td>任务调度员</td>
															</tr>
															<tr>
																<td class="role_table_tr">抢修业务</td>
																<td>维护专员</td>
															</tr>
															<tr>
																<td class="role_table_tr">抢修业务</td>
																<td>技术人员</td>
															</tr>
															<tr>
																<td class="role_table_tr">抢修</td>
																<td>
																	<p>任务调度员</p>
																	<p>维护专员</p>
																	<p>技术人员</p>
																</td>
															</tr>
														</table>
													</div>
												</div>
											</div>
										</div>
									</li>
								</ul>
								<div class="dialog_but staff_account_but">
									<button class="aui_state_highlight staffInfo_modify authorityControlClass">修改</button>
									<button class="aui_state_highlight staffInfo_save authorityControlClass" onclick="updateProviderAccountInfo();" >保存</button>
								</div>
							</div>
							<div id="staff_tab_1" style="display:none;">
								<ul class="staff_info staff_info_other">
									<li>
										<label>性别：</label>
										<span class="staffInfo_value"><em id="show_update_staff_p_sex"></em></span>
										<span class="staffInfo_input">
											<select id="update_staff_p_sex">
												<option value="male">男</option>
												<option value="female">女</option>
											</select>
										</span>
									</li>
									<li>
										<label>出生年月：</label>
										<span class="staffInfo_value"><em id="show_update_staff_p_birthday"></em></span>
										<span class="staffInfo_input"><input type="text" id="update_staff_p_birthday" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text"/></span>
									</li>
									<li>
										<label>毕业时间：</label>
										<span class="staffInfo_value"><em id="show_update_staff_p_graduateDate"></em></span>
										<span class="staffInfo_input"><input type="text" id="update_staff_p_graduateDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text"/></span>
									</li>
									<li>
										<label>学历：</label>
										<span class="staffInfo_value"><em id="show_update_staff_p_degree"></em></span>
										<span class="staffInfo_input"><input type="text" id="update_staff_p_degree"></span>
									</li>
									<li>
										<label>身份证号码：</label>
										<span class="staffInfo_value"><em id="show_update_staff_p_identityCard"></em></span>
										<span class="staffInfo_input"><input type="text" id="update_staff_p_identityCard"></span>
									</li>
									<li>
										<label>人员档案：</label>
										<span><a href="#" style="line-height: 26px;">人员详细档案信息</a></span>
									</li>
								</ul>
								<div class="dialog_but staff_other_but">
									<button class="aui_state_highlight staffInfo_modify authorityControlClass" onclick="showUpdateProviderStaffInfo();">修改</button>
									<button class="aui_state_highlight staffInfo_save authorityControlClass" onclick="updateProviderStaffInfo();">保存</button>
								</div>
							</div>
							<div id="staff_tab_2" style="display:none;">
								<a href="#" target="_blank" class="skill_a">人员技能详细</a>
								<div class="skill">
									<div class="role_title">
										<span style="width: 100%;">现有技能</span>
									</div>
									<div class="role_table">
										<table >
											<tr>
												<td class="role_table_tr">TD网络测试</td>
												<td class="role_table_tr">初级</td>
												<td>2年</td>
											</tr>
											<tr>
												<td class="role_table_tr">动力电源维护</td>
												<td class="role_table_tr">初级</td>
												<td>2年</td>
											</tr>
											<tr>
												<td class="role_table_tr">传输设备维护</td>
												<td class="role_table_tr">初级</td>
												<td>2年</td>
											</tr>
											<tr>
												<td class="role_table_tr">TD网络测试</td>
												<td class="role_table_tr">初级</td>
												<td>2年</td>
											</tr>
										</table>
									</div>
								</div>
								<div class="dialog_but staff_skill_but">
									<button class="aui_state_highlight staffInfo_modify authorityControlClass">修改</button>
									<button class="aui_state_highlight staffInfo_save authorityControlClass">保存</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<%--添加管辖人员弹出框--%>
				<div id="serviceAddStaff_Dialog" class="dialog architecture_dialog_pe" style="display:none;">
					<div class="dialog_header">
						<div class="dialog_title">人员信息</div>
						<div class="dialog_tool">
						   <div class="dialog_tool_close dialog_closeBtn"></div>
						</div>
					</div>
					<div class="dialog_content">
						<div id="staffadd_tab" class="staff_menu tab_menu">
							<ul>
								<li id="account_title" class="selected" style="border-left: 1px solid #99BBE8;">帐号信息</li>
								<li id="staff_title" class="staff_li">人员信息</li>
								<li id="skill_title" style="display:none;" class="staff_li">人员技能</li>
							</ul>
						</div>
						<div class="staff_content">
							<div id="staffadd_tab_0">
								<ul class="staff_info staffadd_info_account">
									<li>
										<label>帐号：</label>
										<span class="staffInfo_value"><em id="show_add_account_p_account"></em>&nbsp;</span>
										<span class="staffInfo_input">
											<input id="add_account_p_account" type="text" />
											<em id="add_account_p_enterpriseUrl"></em>
											<%-- <select id="add_account_p_enterpriseUrl">
											</select> --%>
											<em class="redStar">*</em>
											<em class="redStar add_account_p_error"></em>
										</span>
										<span class="staffInfo_input"><button class="detect" onclick="checkAddProviderAccount('add_account_p');" >检测账号</button></span>
									</li>
									<li>
										<label>密码：</label>
										<span class="staffInfo_value"><em id="show_add_account_p_password"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_p_password" type="password"><em class="redStar">*</em><em class="redStar add_account_p_error"></em></span>
									</li>
									<li>
										<label>确认密码：</label>
										<span class="staffInfo_value"><em id="show_add_account_p_comfigPwd"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_p_comfigPwd" type="password"><em class="redStar">*</em><em class="redStar add_account_p_error"></em></span>
									</li>
									<li>
										<label>姓名：</label>
										<span class="staffInfo_value"><em id="show_add_account_p_name"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_p_name" type="text"><em class="redStar">*</em><em class="redStar add_account_p_error"></em></span>
									</li>
									<li>
										<label>性别：</label>
										<span class="staffInfo_value"><em id="show_add_account_p_sex"></em>&nbsp;</span>
										<span class="staffInfo_input"><input type="radio" value="male" name="add_account_p_sex" checked>男</input><input type="radio" value="female" name="add_account_p_sex">女</input></span>
									</li>
									<li>
										<label>手机号码：</label>
										<span class="staffInfo_value"><em id="show_add_account_p_cellPhoneNumber"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_p_cellPhoneNumber" type="text"><em class="redStar">*</em><em class="redStar add_account_p_error"></em></span>
									</li>
									<li>
										<label>邮箱：</label>
										<span class="staffInfo_value"><em id="show_add_account_p_email"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_p_email" type="text"><em class="redStar">*</em><em class="redStar add_account_p_error"></em></span>
									</li>
									<li>
										<label>手机邮箱：</label>
										<span class="staffInfo_value"><em id="show_add_account_p_mobileEmailAddress"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_p_mobileEmailAddress" type="text"><%--  <em class="redStar">*</em>--%><em class="redStar add_account_p_error"></em></span>
									</li>
									<li>
										<label>备用邮箱：</label>
										<span class="staffInfo_value"><em id="show_add_account_p_backUpEmailAddress"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_p_backUpEmailAddress" type="text"></span>
									</li>
									
									<li>
										<label>状态：</label>
										<span class="staffInfo_value"><em id="show_add_account_p_status"></em><input type="checkbox" class="show_add_account_p_status" name="show_add_account_p_status" disabled="disabled" checked="checked" />有效 </span>
										<span class="staffInfo_input"><em id="add_account_p_status"><input type="checkbox" class="add_account_p_status" name="add_account_p_status" checked="checked" />有效 </em></span>
									</li>
									<%-- 
									<li>
										<label>是否属于本企业：</label>
										<span class="staffInfo_value"><em id="show_add_account_p_isEnterprise"></em>&nbsp;</span>
										<span class="staffInfo_input"><em id="add_account_p_isEnterprise"><input class="add_account_p_isEnterprise" name="add_account_p_isEnterprise" type="radio" value="是" />是 <input class="add_account_p_isEnterprise" name="add_account_p_isEnterprise" type="radio" value="否" />否</em><em class="redStar">*</em><em class="redStar add_account_p_error"></em></span>
									</li>
									 --%>
									<li class="clearfix">
										<label>赋予角色：</label>
										<div class="role">
											<div id="endowadd_role" class="tab_menu">
												<ul>
													<li class="selected" style="border-left: 1px solid #ccc;">组织角色</li>
												</ul>
												<a class="role_a staffInfo_input" onclick="showProviderOrgRole(0);">授予角色</a>
											</div>
											<div class="endow_role_info">
												<div id="endowadd_role_0">
													<div class="role_title">
														<span>角色</span>
													</div>
													<div class="role_table">
														<table id="show_add_org_p_org_role">
															
														</table>
													</div>
												</div>
												<div id="endowadd_role_1" style="display:none;">
													<div class="role_title">
														<span>业务模块</span>
														<span>角色</span>
													</div>
													<div class="role_table">
														<table id="show_add_org_p_biz_role">
															<tr>
																<td class="role_table_tr">抢修业务</td>
																<td>任务调度员</td>
															</tr>
															<tr>
																<td class="role_table_tr">抢修业务</td>
																<td>维护专员</td>
															</tr>
															<tr>
																<td class="role_table_tr">抢修业务</td>
																<td>技术人员</td>
															</tr>
															<tr>
																<td class="role_table_tr">抢修</td>
																<td>
																	<p>任务调度员</p>
																	<p>维护专员</p>
																	<p>技术人员</p>
																</td>
															</tr>
														</table>
													</div>
												</div>
											</div>
										</div>
									</li>
								</ul>
								<div class="dialog_but staffadd_account_but">
									<button class="staff_save" onclick="saveProviderAccountInfo();">保存</button>
								</div>
							</div>
							<div id="staffadd_tab_1" style="display:none;">
								<ul class="staff_info staffadd_info_other">
									<li>
										<label>性别：</label>
										<span class="staffInfo_value"><em id="show_add_staff_p_sex"></em></span>
										<span class="staffInfo_input">
											<select id="add_staff_p_sex">
												<option value="male">男</option>
												<option value="female">女</option>
											</select>
											<span class="redStar">*</span>
										</span>
									</li>
									
									<li>
										<label>出生年月：</label>
										<span class="staffInfo_value"><em id="show_add_staff_p_birthday"></em></span>
										<span class="staffInfo_input"><input type="text" id="add_staff_p_birthday" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text"/></span>
									</li>
									<li>
										<label>毕业时间：</label>
										<span class="staffInfo_value"><em id="show_add_staff_p_graduateDate"></em></span>
										<span class="staffInfo_input"><input type="text" id="add_staff_p_graduateDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text"/></span>
									</li>
									<li>
										<label>学历：</label>
										<span class="staffInfo_value"><em id="show_add_staff_p_degree"></em></span>
										<span class="staffInfo_input"><input type="text" id="add_staff_p_degree"></span>
									</li>
									<li>
										<label>身份证号码：</label>
										<span class="staffInfo_value"><em id="show_add_staff_p_identityCard"></em></span>
										<span class="staffInfo_input"><input id="add_staff_p_identityCard" type="text"></span>
									</li>
									<li>
										<label>人员档案：</label>
										<span><a href="#" style="line-height: 26px;">人员详细档案信息</a></span>
									</li>
								</ul>
								<div class="dialog_but staffadd_other_but">
									<button class="staff_save staffInfo_input" onclick="saveProviderStaffInfo();">保存</button>
								</div>
							</div>
							<div id="staffadd_tab_2" style="display:none;">
								<span class="skill_a">
									<select id="">
										<option value="1">GSM基站维护</option>
									    <option value="10">光缆线路维护</option>
									    <option value="11">光缆线路工程</option>
									</select>
									<select id="">
										<option>初级</option>
										<option>中级</option>
										<option>高级</option>
									</select>
									<input type="text" style="width: 30px" value="1" id="">  年
									<input type="button" id="" value="添加技能">
								</span>
								<%--<a href="#" target="_blank" class="skill_a">人员技能详细</a>--%>
								<div class="skill">
									<div class="role_title">
										<span style="width: 100%;">现有技能</span>
									</div>
									<div class="role_table">
										<table >
											<tr>
												<td class="role_table_tr">TD网络测试</td>
												<td class="role_table_tr">初级</td>
												<td class="role_table_tr">2年</td>
												<td><a href="#">删除</a></td>
											</tr>
											<tr>
												<td class="role_table_tr">动力电源维护</td>
												<td class="role_table_tr">初级</td>
												<td class="role_table_tr">2年</td>
												<td><a href="#">删除</a></td>
											</tr>
											<tr>
												<td class="role_table_tr">传输设备维护</td>
												<td class="role_table_tr">初级</td>
												<td class="role_table_tr">2年</td>
												<td><a href="#">删除</a></td>
											</tr>
											<tr>
												<td class="role_table_tr">TD网络测试</td>
												<td class="role_table_tr">初级</td>
												<td class="role_table_tr">2年</td>
												<td><a href="#">删除</a></td>
											</tr>
										</table>
									</div>
								</div>
								<div class="dialog_but staffadd_skill_but">
									<button class="staff_save">保存</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<%-- 运营商组织管辖人员 --%>
				<%--添加管辖人员弹出框--%>
				<div id="serviceAddStaff_c_Dialog" class="dialog architecture_dialog_pe" style="display:none;">
					<div class="dialog_header">
						<div class="dialog_title">人员信息</div>
						<div class="dialog_tool">
						   <div class="dialog_tool_close dialog_closeBtn" onclick="closeAddCustomerAccount();"></div>
						</div>
					</div>
					<div class="dialog_content">
						<div id="staffadd_tab" class="staff_menu tab_menu">
							<ul>
								<li id="account_title" class="selected" style="border-left: 1px solid #99BBE8;">帐号信息</li>
								<li id="staff_title" class="staff_li">人员信息</li>
								<li id="skill_title" style="display:none;" class="staff_li">人员技能</li>
							</ul>
						</div>
						<div class="staff_content">
							<div id="staffadd_tab_0">
								<ul class="staff_info staffadd_info_account">
									<li>
										<label>帐号：</label>
										<span class="staffInfo_value"><em id="show_add_account_c_account"></em>&nbsp;</span>
										<span class="staffInfo_input">
											<input id="add_account_c_account" type="text" />
											<em id="add_account_c_enterpriseUrl"></em>
											<%-- <select id="add_account_p_enterpriseUrl">
											</select> --%>
											<em class="redStar">*</em>
											<em class="redStar add_account_c_error"></em>
										</span>
										<span class="staffInfo_input"><button class="detect" onclick="checkAddProviderAccount('add_account_c');" >检测账号</button></span>
									</li>
									<%-- <li>
										<label>用户群：</label>
										<span class="staffInfo_value"><em id="show_add_staff_c_type"></em>&nbsp;</span>
										<span class="staffInfo_input">
											<select id="add_staff_c_type">
												<option value="OperationCustomer">运营商客户</option>
											</select>
											<em class="redStar">*</em>
											<em class="redStar add_account_c_error"></em>
										</span>
									</li> --%>
									<li>
										<label>密码：</label>
										<span class="staffInfo_value"><em id="show_add_account_c_password"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_c_password" type="password"><em class="redStar">*</em><em class="redStar add_account_c_error"></em></span>
									</li>
									<li>
										<label>确认密码：</label>
										<span class="staffInfo_value"><em id="show_add_account_c_comfigPwd"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_c_comfigPwd" type="password"><em class="redStar">*</em><em class="redStar add_account_c_error"></em></span>
									</li>
									<li>
										<label>姓名：</label>
										<span class="staffInfo_value"><em id="show_add_account_c_name"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_c_name" type="text"><em class="redStar">*</em><em class="redStar add_account_c_error"></em></span>
									</li>
									<li>
										<label>性别：</label>
										<span class="staffInfo_value"><em id="show_add_account_c_sex"></em>&nbsp;</span>
										<span class="staffInfo_input"><input type="radio" value="male" name="add_account_c_sex" checked="checked">男</input><input type="radio" value="female" name="add_account_c_sex">女</input></span>
									</li>
									<li>
										<label>手机号码：</label>
										<span class="staffInfo_value"><em id="show_add_account_c_cellPhoneNumber"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_c_cellPhoneNumber" type="text"><em class="redStar">*</em><em class="redStar add_account_c_error"></em></span>
									</li>
									<li>
										<label>邮箱：</label>
										<span class="staffInfo_value"><em id="show_add_account_c_email"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_c_email" type="text"><em class="redStar">*</em><em class="redStar add_account_c_error"></em></span>
									</li>
									<li>
										<label>手机邮箱：</label>
										<span class="staffInfo_value"><em id="show_add_account_c_mobileEmailAddress"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_c_mobileEmailAddress" type="text"><%--  <em class="redStar">*</em>--%><em class="redStar add_account_c_error"></em></span>
									</li>
									<li>
										<label>备用邮箱：</label>
										<span class="staffInfo_value"><em id="show_add_account_c_backUpEmailAddress"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="add_account_c_backUpEmailAddress" type="text"><em class="redStar"></em><em class="redStar add_account_c_error"></em></span>
									</li>
									<li>
										<label>状态：</label>
										<span class="staffInfo_value"><em id="show_add_account_c_status"></em><input type="checkbox" class="show_add_account_c_status" name="show_add_account_c_status" disabled="disabled" checked="checked" />有效 </span>
										<span class="staffInfo_input"><em id="add_account_c_status"><input type="checkbox" class="add_account_c_status" name="add_account_c_status" checked="checked" />有效 </em></span>
									</li>
									<%-- 
									<li>
										<label>是否属于本企业：</label>
										<span class="staffInfo_value"><em id="show_add_account_p_isEnterprise"></em>&nbsp;</span>
										<span class="staffInfo_input"><em id="add_account_p_isEnterprise"><input class="add_account_p_isEnterprise" name="add_account_p_isEnterprise" type="radio" value="是" />是 <input class="add_account_p_isEnterprise" name="add_account_p_isEnterprise" type="radio" value="否" />否</em><em class="redStar">*</em><em class="redStar add_account_p_error"></em></span>
									</li>
									 --%>
									<li class="clearfix">
										<label>赋予角色：</label>
										<div class="role">
											<div id="endowadd_role" class="tab_menu">
												<ul>
													<li class="selected" style="border-left: 1px solid #ccc;">组织角色</li>
												</ul>
												<a class="role_a staffInfo_input" onclick="showProviderOrgRole(0);">授予角色</a>
											</div>
											<div class="endow_role_info">
												<div id="endowadd_role_0">
													<div class="role_title">

														<span>角色</span>
													</div>
													<div class="role_table">
														<table id="show_add_org_p_org_role_1">
															
														</table>
													</div>
												</div>
												<div id="endowadd_role_1" style="display:none;">
													<div class="role_title">
														<span>角色</span>
													</div>
													<div class="role_table">
														<table id="show_add_org_p_biz_role">
															<tr>
																<td class="role_table_tr">抢修业务</td>
																<td>任务调度员</td>
															</tr>
															<tr>
																<td class="role_table_tr">抢修业务</td>
																<td>维护专员</td>
															</tr>
															<tr>
																<td class="role_table_tr">抢修业务</td>
																<td>技术人员</td>
															</tr>
															<tr>
																<td class="role_table_tr">抢修</td>
																<td>
																	<p>任务调度员</p>
																	<p>维护专员</p>
																	<p>技术人员</p>
																</td>
															</tr>
														</table>
													</div>
												</div>
											</div>
										</div>
									</li>
								</ul>
								<div class="dialog_but staffadd_account_but">
									<button class="staff_save" onclick="saveCustomerAccountInfo();">保存</button>
								</div>
							</div>
							<div id="staffadd_tab_1" style="display:none;">
								<ul class="staff_info staffadd_info_other">
									<li>
										<label>性别：</label>
										<span class="staffInfo_value"><em id="show_add_staff_p_sex"></em></span>
										<span class="staffInfo_input">
											<select id="add_staff_p_sex">
												<option value="male">男</option>
												<option value="female">女</option>
											</select>
										</span>
										<span class="redStar">*</span>
									</li>
									
									<li>
										<label>出生年月：</label>
										<span class="staffInfo_value"><em id="show_add_staff_p_birthday"></em></span>
										<span class="staffInfo_input"><input type="text" id="add_staff_p_birthday" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text"/></span>
									</li>
									<li>
										<label>毕业时间：</label>
										<span class="staffInfo_value"><em id="show_add_staff_p_graduateDate"></em></span>
										<span class="staffInfo_input"><input type="text" id="add_staff_p_graduateDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text"/></span>
									</li>
									<li>
										<label>学历：</label>
										<span class="staffInfo_value"><em id="show_add_staff_p_degree"></em></span>
										<span class="staffInfo_input"><input type="text" id="add_staff_p_degree"></span>
									</li>
									<li>
										<label>身份证号码：</label>
										<span class="staffInfo_value"><em id="show_add_staff_p_identityCard"></em></span>
										<span class="staffInfo_input"><input id="add_staff_p_identityCard" type="text"></span>
									</li>
									<li>
										<label>人员档案：</label>
										<span><a href="#" style="line-height: 26px;">人员详细档案信息</a></span>
									</li>
								</ul>
								<div class="dialog_but staffadd_other_but">
									<button class="staff_save staffInfo_input" onclick="saveProviderStaffInfo();">保存</button>
								</div>
							</div>
							<div id="staffadd_tab_2" style="display:none;">
								<span class="skill_a">
									<select id="">
										<option value="1">GSM基站维护</option>
									    <option value="10">光缆线路维护</option>
									    <option value="11">光缆线路工程</option>
									</select>
									<select id="">
										<option>初级</option>
										<option>中级</option>
										<option>高级</option>
									</select>
									<input type="text" style="width: 30px" value="1" id="">  年
									<input type="button" id="" value="添加技能">
								</span>
								<%--<a href="#" target="_blank" class="skill_a">人员技能详细</a>--%>
								<div class="skill">
									<div class="role_title">
										<span style="width: 100%;">现有技能</span>
									</div>
									<div class="role_table">
										<table >
											<tr>
												<td class="role_table_tr">TD网络测试</td>
												<td class="role_table_tr">初级</td>
												<td class="role_table_tr">2年</td>
												<td><a href="#">删除</a></td>
											</tr>
											<tr>
												<td class="role_table_tr">动力电源维护</td>
												<td class="role_table_tr">初级</td>
												<td class="role_table_tr">2年</td>
												<td><a href="#">删除</a></td>
											</tr>
											<tr>
												<td class="role_table_tr">传输设备维护</td>
												<td class="role_table_tr">初级</td>
												<td class="role_table_tr">2年</td>
												<td><a href="#">删除</a></td>
											</tr>
											<tr>
												<td class="role_table_tr">TD网络测试</td>
												<td class="role_table_tr">初级</td>
												<td class="role_table_tr">2年</td>
												<td><a href="#">删除</a></td>
											</tr>
										</table>
									</div>
								</div>
								<div class="dialog_but staffadd_skill_but">
									<button class="staff_save">保存</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<%--查看管辖人员弹出框--%>
				<div id="serviceUpdateStaff_c_Dialog" class="dialog architecture_dialog_pe" style="display:none;">
					<div class="dialog_header">
						<div class="dialog_title">人员信息</div>
						<div class="dialog_tool">
						   <div class="dialog_tool_close dialog_closeBtn" onclick="closeUpdateCustomerAccount();"></div>
						</div>
					</div>
					<div class="dialog_content">
						<div id="staff_tab" class="staff_menu tab_menu">
							<ul>
								<li class="selected" style="border-left: 1px solid #99BBE8;">帐号信息</li>
								<li style="display:none;">人员信息</li>
								<li style="display:none;">人员技能</li>
							</ul>
						</div>
						<div class="staff_content">
							<div id="staff_tab_0">
								<ul class="staff_info staff_info_account">
									<li>
										<label>帐号：</label>
										<span class="staffInfo_value"><em id="show_update_account_c_account"></em></span>
										<span class="staffInfo_input"><input id="update_account_c_account" type="text"><em id="update_account_c_enterpriseUrl"></em><em class="redStar">*</em><em class="redStar update_account_c_error"></em></span>
										<span class="staffInfo_input"><button class="detect" onclick="checkUpdateProviderAccount('update_account_c');">检测账号</button></span>
									</li>
									<%-- <li>
										<label>用户群：</label>
										<span class="staffInfo_value"><em id="show_update_staff_c_type"></em></span>
										<span class="staffInfo_input">
											<select id="update_staff_c_type">
												<option value="OperationCustomer">运营商客户</option>
											</select>
											<em class="redStar">*</em><em class="redStar update_account_c_error"></em>
										</span>
									</li> --%>
									<li>
										<label>密码：</label>
										<span class="staffInfo_value"><em id="show_update_account_c_password"></em></span>
										<span class="staffInfo_input"><input id="update_account_c_password" type="password" /><em class="redStar">*</em><em class="redStar update_account_c_error"></em></span>
									</li>
									<li>
										<label>确认密码：</label>
										<span class="staffInfo_value"><em id="show_update_account_c_comfigPwd"></em></span>
										<span class="staffInfo_input"><input id="update_account_c_comfigPwd" type="password" /><em class="redStar">*</em><em class="redStar update_account_c_error"></em></span>
									</li>
									<li>
										<label>姓名：</label>
										<span class="staffInfo_value"><em id="show_update_account_c_name"></em></span>
										<span class="staffInfo_input"><input id="update_account_c_name" type="text" /><em class="redStar">*</em><em class="redStar update_account_c_error"></em></span>
									</li>
									<li>
										<label>性别：</label>
										<span class="staffInfo_value"><em id="show_update_account_c_sex"></em></span>
										<span class="staffInfo_input"><input type="radio" value="male" name="update_account_c_sex">男</input><input type="radio" value="female" name="update_account_c_sex" >女</input></span>
									</li>
									<li>
										<label>手机号码：</label>
										<span class="staffInfo_value"><em id="show_update_account_c_cellPhoneNumber"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="update_account_c_cellPhoneNumber" type="text"><em class="redStar">*</em><em class="redStar update_account_c_error"></em></span>
									</li>
									<li>
										<label>邮箱：</label>
										<span class="staffInfo_value"><em id="show_update_account_c_email"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="update_account_c_email" type="text"><em class="redStar">*</em><em class="redStar update_account_c_error"></em></span>
									</li>
									<li>
										<label>手机邮箱：</label>
										<span class="staffInfo_value"><em id="show_update_account_c_mobileEmailAddress"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="update_account_c_mobileEmailAddress" type="text"><%--  <em class="redStar">*</em>--%><em class="redStar update_account_c_error"></em></span>
									</li>
									<li>
										<label>备用邮箱：</label>
										<span class="staffInfo_value"><em id="show_update_account_c_backUpEmailAddress"></em>&nbsp;</span>
										<span class="staffInfo_input"><input id="update_account_c_backUpEmailAddress" type="text"><em class="redStar"></em><em class="redStar update_account_c_error"></em></span>
									</li>
									<li>
										<label>生效时间：</label>
										<span class="staffInfo_value"><em id="show_update_account_c_time_range_begin"></em>&nbsp;</span>
										<span class="staffInfo_input"><input type="text" id="update_account_c_time_range_begin" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text"/></span>
									</li>
									<li>
										<label>失效时间：</label>
										<span class="staffInfo_value"><em id="show_update_account_c_time_range_end"></em>&nbsp;</span>
										<span class="staffInfo_input"><input type="text" id="update_account_c_time_range_end" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text"/></span>
									</li>
									<li>
										<label>状态：</label>
										<span class="staffInfo_value"><em id="show_update_account_c_status"></em><input type="checkbox" class="show_update_account_c_status" name="show_update_account_c_status" disabled="disabled" checked="checked" />有效 </span>
										<span class="staffInfo_input"><em id="update_account_c_status"><input type="checkbox" class="update_account_c_status" name="update_account_c_status" checked="checked" />有效 </em></span>
									</li>
									<%-- 
									<li>
										<label>是否属于本企业：</label>
										<span class="staffInfo_value"><em id="show_update_account_p_isEnterprise"></em>&nbsp;</span>
										<span class="staffInfo_input"><em id="update_account_p_isEnterprise"><input class="update_account_p_isEnterprise" name="update_account_p_isEnterprise" checked="checked" type="radio" value="是" />是 <input class="update_account_p_isEnterprise" name="update_account_p_isEnterprise" type="radio" value="否" />否</em><em class="redStar">*</em><em class="redStar update_account_p_error"></em></span>
									</li>
									 --%>
									<li class="clearfix">
										<label>赋予角色：</label>
										<div class="role">
											<div id="endow_role" class="tab_menu">
												<ul>
													<li class="selected" style="border-left: 1px solid #ccc;">组织角色</li>
													<li>业务角色</li>
												</ul>
												<p class="staffInfo_input"><a class="role_a serviceRole_showBtn">授予角色</a></p>
											</div>
											<div class="endow_role_info">
												<div id="endow_role_0">
													<div class="role_title">
														<span>组织</span>
														<span>角色</span>
													</div>
													<div class="role_table">
														<table id="show_update_org_p_org_role">
															
														</table>
													</div>
												</div>
												<div id="endow_role_1" style="display:none;">
													<div class="role_title">
														<span>业务模块</span>
														<span>角色</span>
													</div>
													<div class="role_table">
														<table id="show_update_org_p_biz_role">
															<tr>
																<td class="role_table_tr">抢修业务</td>
																<td>任务调度员</td>
															</tr>
															<tr>
																<td class="role_table_tr">抢修业务</td>
																<td>维护专员</td>
															</tr>
															<tr>
																<td class="role_table_tr">抢修业务</td>
																<td>技术人员</td>
															</tr>
															<tr>
																<td class="role_table_tr">抢修</td>
																<td>
																	<p>任务调度员</p>
																	<p>维护专员</p>
																	<p>技术人员</p>
																</td>
															</tr>
														</table>
													</div>
												</div>
											</div>
										</div>
									</li>
								</ul>
								<div class="dialog_but staff_account_but">
									<button id="update_update_account_button" onclick="changeUpdateCustomerAccountInfo();">修改</button>
									<button id="update_save_account_button" onclick="updateCustomerAccountInfo();" >保存</button>
								</div>
							</div>
							<div id="staff_tab_1" style="display:none;">
								<ul class="staff_info staff_info_other">
									<li>
										<label>性别：</label>
										<span class="staffInfo_value"><em id="show_update_staff_p_sex"></em></span>
										<span class="staffInfo_input">
											<select id="update_staff_p_sex">
												<option value="male">男</option>
												<option value="female">女</option>
											</select>
										</span>
									</li>
									<li>
										<label>出生年月：</label>
										<span class="staffInfo_value"><em id="show_update_staff_p_birthday"></em></span>
										<span class="staffInfo_input"><input type="text" id="update_staff_p_birthday" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text"/></span>
									</li>
									<li>
										<label>毕业时间：</label>
										<span class="staffInfo_value"><em id="show_update_staff_p_graduateDate"></em></span>
										<span class="staffInfo_input"><input type="text" id="update_staff_p_graduateDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly class="Wdate required input-text"/></span>
									</li>
									<li>
										<label>学历：</label>
										<span class="staffInfo_value"><em id="show_update_staff_p_degree"></em></span>
										<span class="staffInfo_input"><input type="text" id="update_staff_p_degree"></span>
									</li>
									<li>
										<label>身份证号码：</label>
										<span class="staffInfo_value"><em id="show_update_staff_p_identityCard"></em></span>
										<span class="staffInfo_input"><input type="text" id="update_staff_p_identityCard"></span>
									</li>
									<li>
										<label>人员档案：</label>
										<span><a href="#" style="line-height: 26px;">人员详细档案信息</a></span>
									</li>
								</ul>
								<div class="dialog_but staff_other_but">
									<button class="aui_state_highlight staffInfo_modify authorityControlClass" onclick="showUpdateProviderStaffInfo();">修改</button>
									<button class="aui_state_highlight staffInfo_save authorityControlClass" onclick="updateProviderStaffInfo();">保存</button>
								</div>
							</div>
							<div id="staff_tab_2" style="display:none;">
								<a href="#" target="_blank" class="skill_a">人员技能详细</a>
								<div class="skill">
									<div class="role_title">
										<span style="width: 100%;">现有技能</span>
									</div>
									<div class="role_table">
										<table >
											<tr>
												<td class="role_table_tr">TD网络测试</td>
												<td class="role_table_tr">初级</td>
												<td>2年</td>
											</tr>
											<tr>
												<td class="role_table_tr">动力电源维护</td>
												<td class="role_table_tr">初级</td>
												<td>2年</td>
											</tr>
											<tr>
												<td class="role_table_tr">传输设备维护</td>
												<td class="role_table_tr">初级</td>
												<td>2年</td>
											</tr>
											<tr>
												<td class="role_table_tr">TD网络测试</td>
												<td class="role_table_tr">初级</td>
												<td>2年</td>
											</tr>
										</table>
									</div>
								</div>
								<div class="dialog_but staff_skill_but">
									<button class="aui_state_highlight staffInfo_modify authorityControlClass">修改</button>
									<button class="aui_state_highlight staffInfo_save authorityControlClass">保存</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<%--授予角色弹出框--%>
				<div class="role_black"></div>
				<div id="serviceRole_Dialog" class="dialog architecture_dialog_role" style="display:none;">
					<div class="dialog_header">
						<div class="dialog_title">授予角色</div>
						<div class="dialog_tool">
						   <div class="dialog_tool_close serviceRole_closeBtn"></div>
						</div>
					</div>
					<div class="dialog_content">
						<div id="award_role" class="roleSelect tab_menu">
							<ul>
								<li class="selected" style="border-left: 1px solid #99BBE8;">组织角色</li>
							</ul>
						</div>
						<div class="roleSelect_container">
							<div id="award_role_0" class="roleSelect_ar">
								<div class="roleSelect_ar_role">
									<h2>组织角色：</h2>
									<ul id="org_p_org_role">
									</ul>
								</div>
							</div>
							<div id="award_role_1" class="roleSelect_bu" style="display:none;">
								<div id="business_role" class="tab_menu clearfix">
									<ul>
										<li id="business_role_tag_0" class="selected" style="border-left: 1px solid #99BBE8;">抢修</li>
										<li id="business_role_tag_1">巡检</li>
										<li id="business_role_tag_2">人员调度</li>
										<li id="business_role_tag_3">车辆调度</li>
										<li id="business_role_tag_4">物资调度</li>
									</ul>
								</div>
								<div id="business_role_info" class="container_info">
									<ul name="org_p_biz_role" id="business_role_0">
									</ul>
									<ul name="org_p_biz_role" id="business_role_1" style="display:none;">
									</ul>
									<ul name="org_p_biz_role" id="business_role_2" style="display:none;">
									</ul>
									<ul name="org_p_biz_role" id="business_role_3" style="display:none;">
									</ul>
									<ul name="org_p_biz_role" id="business_role_4" style="display:none;">
									</ul>
								</div>
							</div>
						</div>
						<div class="dialog_but">
							<button class="aui_state_highlight" onclick="getProviderOrgRole();getProviderBizRole();closeProviderAwardRole();">确定</button>
							<button class="aui_state_highlight" onclick="closeProviderAwardRole();">取消</button>
						</div>
					</div>
				</div>
				<%--删除提示--%>
				<div id="personnelDelete_Dialog" class="dialog personnel_dialog" style="display:none;">
					<div class="dialog_header">
						<div class="dialog_title">删除组织</div>
						<div class="dialog_tool">
						   <div class="dialog_tool_close dialog_closeBtn personnelDelete_closeBtn"></div>
						</div>
					</div>
					<div class="dialog_content">
						<div class="personnel_info">
							请确认删除的组织：<span id="delete_p_baseInfo"></span>
						</div>
						<div class="dialog_but">
							<button class="aui_state_highlight dialog_closeBtn" onclick="confirmDeleteProviderOrgBaseInfo();">确定</button>
							<button class="aui_state_highlight dialog_closeBtn">取消</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<%-- 弹出人员 --%>
		<div id="staffList" style="position:absolute; left:50%; top:50px; z-index:300; width:770px; margin-left:-385px; border:2px solid #999; border-radius:5px;box-shadow:2px 2px 3px #999;background:#fff; display:none;">
		</div>
		<div class="black"></div>
	</body>
</html>
