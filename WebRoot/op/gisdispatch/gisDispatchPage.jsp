<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集中调度 </title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery.treeview.css" />
<link type="text/css" rel="stylesheet" href="../../jslib/gantt/gantt_small.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery-ui-1.8.23.custom.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/paging/iscreate-paging.css" />
<link rel="stylesheet" type="text/css" href="css/gis_Concentration.css" />
<link rel="stylesheet" type="text/css" href="css/alert_div.css" />
<link rel="stylesheet" type="text/css" href="css/statistics.css" />
<link rel="stylesheet" href="css/city.css" />
<%-- 加载地图模块需要引入的文件 --%>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/gis/jslib/api/iscreate_map.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../../jslib/gantt/gantt.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/highcharts.src.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/exporting.src.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="js/function.js"></script>
<script type="text/javascript" src="js/gisdispatch.js"></script>
<script type="text/javascript" src="../report/js/gisdispatchReport.js"></script>
<script type="text/javascript" src="../jslib/networkResourceViewDivPage.js"></script>
<script type="text/javascript" src="../jslib/common.js"></script>
<script type="text/javascript">
$(function(){
    //弹出层
	$(".statistics-layer-title").click(function(){
	    $(".statistics-layer-info").slideToggle("fast");
		$(this).toggleClass("show-statistics-layer-title")
    });
     $("#resourceCount").click(function(){
	    $(".statistics-table").show("fast");
		//$(".statistics-layer-info").hide();
    });
    $(".close-dialog").click(function(){
	    $(".statistics-table").hide();
	    $(".statistics-layer-info").hide();
    });
});
function loadRepoert(me){
  	$('#statistics-layer-div a').removeAttr("style");
  	$(me).css("font-weight","800");
  	$(me).css("color","black");
  	$("#monitor_div").text($(me).text());
  	loadReportChar('container');
}
function getPeakStatisticsByStation(me){
  	$('#statistics-layer-div a').removeAttr("style");
  	$(me).css("font-weight","800");
  	$(me).css("color","black");
  	$("#monitor_div").text($(me).text());
  	loadBaseStationReport('container');
}
function getPeakStatisticsByStaff(me){
	$('#statistics-layer-div a').removeAttr("style");
   	$(me).css("font-weight","800");
   	$(me).css("color","black");
   	$("#monitor_div").text($(me).text());
   	loadStaffReport('container');
}
function getPeakStatisticsByCar(me){
	$('#statistics-layer-div a').removeAttr("style");
 	$(me).css("font-weight","800");
 	$(me).css("color","black");
 	$("#monitor_div").text($(me).text());
	loadCarReport('container');
}
</script>

<script src="js/objutil.js"></script>
<script src="js/gisArea.js"></script>
</head>
<body>
	<div id="dialog-pop" class="dialog-box assignedtime-tips" style="display: none"></div>
	<input type="hidden" value="23.12651273063735" id="latText"/>
    <input type="hidden" value="113.41254847709276" id="lngText"/>
    <input type="hidden" value="17" id="zoomText"/>
    
    
	<div id="top">
	    <div id="main_container">
	    	<div id="container_left_gis">
	            <%--放地图区域 和 右边详细信息--%>
	            <div id="gis_content">
	            	<%-- 详细信息 --%>
	                <div id="gis_content_right">
	                    <%-- 详细信息主体信息 --%>
	                    <div id="gis_content_right_main" class="tc">
	                        <div class="top_tab">
							    <div class="tab_left" style="display: none;"></div>
								<ul id="tabmenu" class="gis_content_right_main_tab">
									<li id="monitorLi" class="active">
									    <div class="tab_title" title="监控资源">监控资源</div>
									</li>
									<li id="searchResultLi" style="display:none;">
									    <div class="tab_title" title="查询结果">查询结果</div>
										<div class="tab_close" onclick="cleanSearchResultEvent()"></div>
									</li>
								    <li id="assignResultLi" style="display:none;">
									    <div class="tab_title" title="任务调度分配">任务调度分配</div>
										<div class="tab_close" onclick="cleanAssignResultEvent()"></div>
									</li>
								</ul>
							</div>
	                        
	                        <div id="monitorResourceDiv" class="top_tab_content">
	                        	<div id="mrLoadingDiv">
	                        		<em><img src="image/loading.gif"/></em>
	                        		<em style="vertical-align:top; margin-left:10px;">数据加载中..</em>
	                        	</div>
	                        	<div class="sort_box clearfix">
									<a class="current" href="javascript:void(0);">车辆</a>
									<a href="javascript:void(0);">人员</a>
									<a href="javascript:void(0);">有任务站址</a>
									<em class="refresh_ico" id="refresh_ico"></em>
								</div>
								<div class="resource_list">
									<h4 class="resource_list_title"></h4>
									<ul id="monitorCarListUl"></ul>
									<div id="mcPageContent"></div>
								</div>
								
								<div class="resource_list" style="display:none;">
									<h4 class="resource_list_title"></h4>
									<ul id="monitorStaffListUl"></ul>
									<div id="mhPageContent"></div>
								</div>
								
								<div class="resource_list" style="display:none;">
									<h4 class="resource_list_title"></h4>
									<ul id="monitorStationListUl"></ul>
									<div id="msPageContent"></div>
								</div>
					                        	
	                        </div>
	                        
	                        <div id="searchResultDiv" class="top_tab_content" style="display: none;">
								<div class="sort_box clearfix">
									<a class="current" href="javascript:void(0);">车辆</a>
									<a href="javascript:void(0);">人员</a>
									<a href="javascript:void(0);">站址</a>
								</div>
								<div class="resource_list">
									<h4 class="resource_list_title"></h4>
									<ul id="searchCarListUl"></ul>
									<div id="scPageContent"></div>
								</div>
								<div class="resource_list" style="display:none;">
									<h4 class="resource_list_title"></h4>
									<ul id="searchStaffListUl"></ul>
									<div id="shPageContent"></div>
								</div>
								<div class="resource_list" style="display:none;">
									<h4 class="resource_list_title"></h4>
									<ul id="searchStationListUl"></ul>
									<div id="ssPageContent"></div>
								</div>
							</div>
							
	                        <div id="gis_content_right_data" class="top_tab_content" style="display: none;">
	                        	<input type="hidden" id="staffIdHiddenText" />
		                        <input type="hidden" id="stationIdHiddenText" />
		                        <input type="hidden" id="stationTypeHiddenText" />
		                        <input type="hidden" id="carIdHiddenText"/>
		                        <div id="loadingDiv">
		                        	<img src="image/loading.gif" width="20" height="20" style="vertical-align:middle" />
		                        	&emsp;加载数据中,请稍候...
		                        </div>
		                        <table id="netWorkTable"></table>
		                        <table id="staffInfoTable"></table>
		                        <table id="carInfoTable"> </table>
		                        <div style="margin-top: 10px;">
			                        <input class="dialog-but" type="button" value="下一步" id="nextStepButton" onclick="nextToAssignTaskEvent()"/>
		                        </div>
		                    </div>
	                    </div>
	                    
	                    <%-- 结果展开显示 --%>
	            		<div id="gis_content_right_show">
	                    	<img id="gis_content_right_show_img" src="image/hide_right.png" />
	                    </div>
	                </div>
	                
	            	<%--放地图上面工具--%>
	            	<div class="statistics-box" id="reportMonitorDiv">
						<div class="statistics-layer clearfix">
						    <div class="statistics-layer-title">
							    <em class="statistics-layer-arrow"></em><span>统计监控</span>
							</div>
							<div class="statistics-layer-info clearfix" style="display:none;" id="statistics-layer-div">
							    <ul>
									<li><a href="#" onclick="loadRepoert(this);" id="resourceCount">全资源统计</a></li>
									<li><a href="#" id="peakStatisticsByStaff" onclick="getPeakStatisticsByStaff(this);">人员任务监控</a></li>
									<li><a href="#" id="peakStatisticsByCar" onclick="getPeakStatisticsByCar(this);">车辆任务监控</a></li>
									<li><a href="#" onclick="getPeakStatisticsByStation(this);" id="peakStatisticsByStation">基站工单监控</a></li>
								</ul>
							</div>
						</div>
						<div class="statistics-table" style="display:none;filter:alpha(Opacity=80);-moz-opacity:0.9;opacity: 0.9;z-index:200;">
						    <div class="statistics-table-top">
							    <h2 id="monitor_div">资源统计</h2>
								<%--  <em class="statistics-table-arrow"></em>--%>
							    <a href="#" class="close-dialog" title="关闭窗口"></a>
							</div>
							<div id="container"  style="width: 600px;"></div>
						</div>
					</div>
					
	                <%-- 地图 --%>
	                <div id="gis_map_container">
	                	<div id="gis_toolBar" >
	                		<input type="hidden" id="select_area_id" />
			            	<span class="toolBar_area" style="white-space:nowrap;">选择区域：
			            		<span id="top_tool_bar" class="city_title">
			            			<a title="选择城市" class="city_expand city_show" id="city_select" href="javascript:void(0)">请选择</a>
									<a title="选择城区" class="city_expand district_show" style="display:none;" id="district_select" href="javascript:void(0)">请选择</a>
									<a title="选择街道" class="city_expand street_show" style="display:none;" id="street_select" href="javascript:void(0)">请选择</a>
			            		</span>
			                </span>
			                <div class="map_city">
								<div class="city_main">
									<div class="title">
										<em>城市列表</em>
										<a href="javascript:void(0)" title="关闭" class="close_icon city_close"></a>
									</div>
									<div class="sel_city clearfix">
										<div class="sel_city_top">
											<div class="sel_city_default">
												<p>
													<span>当前城市：<em id="now_choice_city"></em></span>
													<%-- 
													<a href="javascript:void(0);" class="def_city">设为默认城市</a>
													<span class="default_info">您默认的城市是：<em>广州市</em>[<a href="javascript:void(0);">删除</a>]</span>
													 --%>
												</p>
											</div>     
											<div class="sel_city_searchbar">
												<div class="btnbar">
													<span id = "" class="sel_city_btnl sel_city_btnl_sel" href="javascript:void(0)">按省份</span>
													<span class="sel_city_btnl" href="javascript:void(0)">按城市</span>
												</div>
												<form>
													<input type="text" id="search_city_input" name="" class="sel_city_searchinput">
													<span id="search_btn" class="sel_city_btn_submit">搜索</span>
													<div class="sel_city_tip">请输入正确的城市名</div>
												</form>
											</div>
											<div class="sel_city_letterbar" id="first_letter_choice_div">
												
											</div>
										</div>
										<div class="sel_city_result">
											<table id="province_table" cellspacing="0" cellpadding="0">
												<tbody>
													<%-- 城市列表 --%>
												</tbody>
											</table>
										</div>
									</div>
								</div>
							</div>
							<div class="map_district">
								<div class="city_main">
									<div class="title">
										<em>城区列表</em>
										<a href="javascript:void(0)" title="关闭" class="close_icon district_close"></a>
									</div>
									<div class="sel_city_default">
										<p>
											<span>当前城区：<em id="now_choice_district"></em></span>
										</p>
									</div> 
									<div class="sel_district_hotcity">
										<%-- 区 --%>
									</div>
								</div>
							</div>
							<div class="map_street">
								<div class="city_main">
									<div class="title">
										<em>街道列表</em>
										<a href="javascript:void(0)" title="关闭" class="close_icon street_close"></a>
									</div>
									<div class="sel_city_default">
										<p>
											<span>当前街道：<em id="now_choice_district"></em></span>
											<a href="javascript:void(0);" class="def_city">设为默认街道</a>
										</p>
										<p>
											<span class="default_info">您默认的街道是：<em>天河公园</em>[<a href="javascript:void(0);">删除</a>]</span>
										</p>
									</div> 
									<div class="sel_street_hotcity">
										<%-- 街 --%>
									</div>
								</div>
							</div>
			                
			                <span class="toolBar_btn clearfix">
			                	<div class="inline_btn" style="display: none">
			                    	<div class="l_buttom" id="markPoint">标记热点</div>
			                    	<span id="biaojiredian">
			                        	<ul>
			                            	<li onclick="markHotPointEvent('_resource_questionspot')">问题点</li>
			                                <li onclick="markHotPointEvent('_resource_falutspot')">故障点</li>
			                                <li onclick="markHotPointEvent('_resource_hiddentroublespot')">隐患点</li>
			                                <li onclick="markHotPointEvent('_resource_watchspot')">盯防区域</li>
			                            </ul>
			                        </span>
			                    </div>
			                    <%-- 搜索框 --%>
					            <div class="inline_btn">
					               		<div class="l_buttom"><h4 class="search_ico">搜索</h4></div>
						               	<div class="search_span_bg" style="margin-left:-260px;margin-top: 10px;">
						               		<em class="statistics-table-arrow">▲</em>
											<div class="search_bar">
												<div class="ml10">
													<input class="bt_search" type="checkbox" id="gjSearchCb"/> 高级搜索
												</div>
										        <div class="search_bar_tab_content bt_search_show" style="display:none;">
										            <ul class="search_bar_ul">
										                <li>
										                    <input type="checkbox" checked="checked" class="topSearchCb" value="_resource_station"/><em class="blue"> 基站</em>
										                    <select id="searchBsGrade">
										                        <option value="">基站等级</option>
										                        <option value="普通">普通</option>
										                        <option value="VIP">VIP</option>
										                        <option value="VVIP">VVIP</option>
										                    </select>
										                    <select id="searchBsWoCount">
										                        <option value="">工单数</option>
										                        <option value="0">0</option>
										                        <option value="1">1个以上</option>
										                    </select>
										                </li>
										                <li>
										                    <input type="checkbox" checked="checked" class="topSearchCb" value="_resource_car"/><em class="blue"> 车辆</em>
										                    <select id="searchCarStatu">
										                        <option value="">行驶状态</option>
										                        <option value="1">行驶中</option>
										                        <option value="2">静止</option>
										                        <option value="0">离线</option>
										                        <option value="3">待初始化</option>
										                    </select>
										                    <select id="searchCarDuty">
										                        <option value="">是否排班</option>
										                        <option value="1">是</option>
										                        <option value="0">否</option>
										                    </select>
										                    <select id="searchCarTaskCount">
										                        <option value="">任务数</option>
										                        <option value="0">0</option>
										                        <option value="5">5个以下</option>
										                        <option value="6">5个以上</option>
										                    </select>
										                </li>
										                <li>
										                    <input type="checkbox" checked="checked" class="topSearchCb" value="_resource_human"/><em class="blue"> 人员</em>
										                    <select id="searchHumanDuty">
										                        <option value="">是否值班</option>
										                        <option value="1">是</option>
										                        <option value="0">否</option>
										                    </select>
										                    <select id="searchHumanTaskCount">
										                        <option value="">任务数</option>
										                        <option value="0">0</option>
										                        <option value="5">5个以下</option>
										                        <option value="6">5个以上</option>
										                    </select>
										                </li>
										            </ul>
										        </div>
										        <div class="search_bar_tab_content">
													<input type="text" id="topSearchText" style="width:283px;" />
													<input type="button" value="" title="搜索" id="topSearchButton" class="search_button"/>
										        </div>
										    </div>
									        
						                </div>
					               </div>
			                    <div class="inline_btn">
									<div class="l_buttom" id="show_tuceng_div" class="show_button" ><h4 class="layer_ico">图层 ▼</h4></div>
							        <div id="tuceng_div" style="background:#fff;z-index:25;">
							        	<div class="tuceng_div_top">图元显示选择
							        	<input type="checkbox" id="isTitleShow" style="margin-left: 74px"/>显示图元名称</div>
										
										<%-- 动态生成左边树 --%>	
										<div id="treeDiv" style="overflow: auto; height: 400px;" ></div>		        	
							            
							            <div class="tuceng_div_bottom">
							            	<input type="button" value="设置显示图层" id="settingButton" />
							            </div>
							        </div>
							    </div>
			                    <div class="inline_btn mr10">
			                    	<div class="l_buttom" id="full_screen"><h4 class="fullScreen_ico">全屏</h4></div>
			                    </div>
			                </span>
			            </div>
	                	<div id="gis_map"></div>
	                </div>
	            </div>
	        </div>
	    </div>
	    
	    <div id="top_content">
	        
	        <div class="tab_1">
	        	<h4 class="ul_select">
	            	<em>工单列表</em>
	            	<em id="curBizType" style="display:none;">urgentrepair</em>
	            </h4>
	        	<ul>
	            	<li class="ontab" onclick="getUserWorkOrderListByPage('urgentrepair','pendingWorkOrder',0,10)" biztype="urgentrepair">我的工单</li>
	                <li onclick="getTaskOrderListByPage('urgentrepair','pendingTaskOrder',0,10)" biztype="taskorder">我的任务</li>
	                <li onclick="getUserWorkOrderListByPage('cardispatch','pendingWorkOrder',0,10)" biztype="cardispatch">车辆调度</li>
	                <li onclick="getUserWorkOrderListByPage('resourcedispatch','pendingWorkOrder',0,10)" style="display:none;" biztype="resourcedispatch">物资调度</li>
	            </ul>
	        </div>
	        
	        <%-- 我的工单tab大 --%>
	        <div class="tab_content">
	        	<div class="left_tree_tab_container radio_tool">
	            	<ul class="left_tree_tab" id="urUl">
	                    <li onclick="getUserWorkOrderListByPage('urgentrepair','pendingWorkOrder',0,10)" bizTypeCode="urgentrepair" workOrderType="pendingWorkOrder">
	                        <a class="selected">待办工单</a>
	                    </li>
	                    <li onclick="getUserWorkOrderListByPage('urgentrepair','trackWorkOrder',0,10)" bizTypeCode="urgentrepair" workOrderType="trackWorkOrder">
	                        <a>跟踪工单</a>
	                    </li>
	                    <li onclick="getUserWorkOrderListByPage('urgentrepair','superviseWorkOrder',0,10)" bizTypeCode="urgentrepair" workOrderType="superviseWorkOrder">
	                        <a>监督工单</a>
	                    </li>
	                </ul>
	                <div class="left_tree_tab_tool">
	                	<em>工单编号：</em><input type="text" value="" class="queryOrderNumber" />
	                	<em>工单主题：</em><input type="text" value="" class="queryOrderTitle"/>
	                	<em>工单状态：</em>
	                	<select style="width:120px;" class="queryOrderState">
	                		<option value="">请选择工单状态</option>
							<option value="2">待受理</option>
							<option value="3">处理中</option>
							<option value="6">已派发</option>
							<option value="7">已结束</option>
	                	</select>
	                	<input type="button" style="width:60px;" value="查询" onclick="getWorkOrderListByCondition(this)"/>
	                	<a href="javascript:void(0);" onclick="getUserWorkOrderListByPage('urgentrepair','pendingWorkOrder',0,10)">显示全部</a>
	                </div>
	            </div>
	            <%-- 创建工单tool --%>
	            <div class="cjgd_choose" >
	                <a class="cjgd_choose_a" href="#">创建工单▼</a>
	                <div class="cjgd_choose_button">
	                	<a class="not_disabled" href="../urgentrepair/createWorkOrder.jsp" target="_blank">故障抢修工单</a>
	                	<a class="is_disabled">网络优化工单</a>
	                	<a class="is_disabled">修缮工程工单</a>
	                	<a class="is_disabled">业主维系工单</a>
	                	<a class="is_disabled">项目管理工单</a>
	                	<a class="is_disabled">项目预算申请</a>
	                </div>
	            </div>
	            
	            <div class="tab_content_main left_tree_tab_content">
	                <table class="tab_content_table nowrap_td tc" id="urTable">
	                    <tr>
	                        <th style="width:30px;"></th>
	                        <th style="width:30px;"><input type="checkbox" /></th>
	                        <th>工单号</th>
	                        <th>工单类型</th>
	                        <th style="width:32%;">工单主题</th>
	                        <th>创建人</th>
	                        <th>创建时间</th>
	                        <th>要求完成时间</th>
	                        <th>当前责任人</th>
	                        <th>工单状态</th>
	                    </tr>
	                </table>
	            </div>
	        </div>
        
	        <%-- 我的任务tab大 --%>
	        <div class="tab_content" style="display:none;">
	        	<div class="left_tree_tab_container radio_tool">
	            	<ul class="left_tree_tab" id="toUl">
	                    <li onclick="getTaskOrderListByPage('urgentrepair','pendingTaskOrder',0,10)" workOrderType="pendingTaskOrder" bizTypeCode="urgentrepair">
	                        <a class="selected">待办任务</a>
	                    </li>
	                    <li onclick="getTaskOrderListByPage('urgentrepair','trackTaskOrder',0,10)" workOrderType="trackTaskOrder" bizTypeCode="urgentrepair">
	                        <a>跟踪任务</a>
	                    </li>
	                    <li onclick="getTaskOrderListByPage('urgentrepair','superviseTaskOrder',0,10)" workOrderType="superviseTaskOrder" bizTypeCode="urgentrepair">
	                        <a>监督任务</a>
	                    </li>
	                </ul>
	                <div class="left_tree_tab_tool">
	                	<em>任务单编号：</em><input type="text" value="" class="queryOrderNumber" />
	                	<em>任务单主题：</em><input type="text" value="" class="queryOrderTitle"/>
	                	<em>任务单状态：</em>
	                	<select style="width:120px;" class="queryOrderState">
	                		<option value="">请选择任务单状态</option>
							<option value="8">待受理</option>
							<option value="9">处理中</option>
							<option value="10">已派发</option>
							<option value="18">升级中</option>
							<option value="11">已结束</option>
	                	</select>
	                	<input type="button" style="width:60px;" value="查询" onclick="getWorkOrderListByCondition(this)"/>
	                	<a href="javascript:void(0);" onclick="getTaskOrderListByPage('urgentrepair','pendingTaskOrder',0,10)">显示全部</a>
	                </div>
	            </div>
	            <div class="tab_content_main left_tree_tab_content">
	            	<%-- 任务单Table  --%>
	                <table class="tab_content_table nowrap_td tc" id="toTable">
	                    <tr>
	                        <th style="width:30px;"></th>
	                        <th style="width:30px;"><input type="checkbox" /></th>
	                        <th>任务单号</th>
	                        <th>任务单类型</th>
	                        <th style="width:32%;">任务单主题</th>
	                        <th>创建人</th>
	                        <th>创建时间</th>
	                        <th>要求完成时间</th>
	                        <th>当前责任人</th>
	                        <th>任务单状态</th>
	                    </tr>
	                </table>
	            </div>
	        </div>
	        
	        <%-- 车辆调度tab大 --%>
	        <div class="tab_content" style="display:none;">
	        	<div class="left_tree_tab_container radio_tool">
	            	<ul class="left_tree_tab" id="cdUl">
	                    <li onclick="getUserWorkOrderListByPage('cardispatch','pendingWorkOrder',0,10)" bizTypeCode="cardispatch" workOrderType="pendingWorkOrder">
	                        <a class="selected">待办调度单</a>
	                    </li>
	                    <li onclick="getUserWorkOrderListByPage('cardispatch','trackWorkOrder',0,10)" bizTypeCode="cardispatch" workOrderType="trackWorkOrder">
	                        <a>跟踪调度单</a>
	                    </li>
	                    <li onclick="getUserWorkOrderListByPage('cardispatch','superviseWorkOrder',0,10)" bizTypeCode="cardispatch" workOrderType="superviseWorkOrder">
	                        <a>监督调度单</a>
	                    </li>
	                </ul>
	                <div class="left_tree_tab_tool">
	                	<em>工单编号：</em><input type="text" value="" class="queryOrderNumber" />
	                	<em>工单主题：</em><input type="text" value="" class="queryOrderTitle"/>
	                	<em>工单状态：</em>
	                	<select style="width:120px;" class="queryOrderState">
	                		<option value="">请选择工单状态</option>
							<option value="14">待派车</option>
							<option value="15">待用车</option>
							<option value="16">待还车</option>
							<option value="7">已结束</option>
	                	</select>
	                	<input type="button" style="width:60px;" value="查询" onclick="getWorkOrderListByCondition(this)"/>
	                	<a href="javascript:void(0);" onclick="getUserWorkOrderListByPage('cardispatch','pendingWorkOrder',0,10)">显示全部</a>
	                </div>
	            </div>
	           <div class="clgd_button">
	                <a class="not_disabled" href="../cardispatch/cardispatch_applyworkorder.jsp" target="_blank">车辆调度工单</a>
	            </div>
	        	<%-- 车辆调度单 Table --%>
	        	<div class="tab_content_main left_tree_tab_content">
				 <table class="tab_content_table nowrap_td tc" id="cdTable">
	               <tr>
	                   <th style="width:30px;"></th>
	                   <th style="width:30px;"><input type="checkbox" /></th>
	                   <th>工单号</th>
	                   <th style="width:32%;">工单主题</th>
	                   <th>创建人</th>
	                   <th>创建时间</th>
	                   <th>要求完成时间</th>
	                   <th>当前责任人</th>
	                   <th>工单状态</th>
	               </tr>
	            </table>
	           </div>
			</div>
	        
	        
	        <%-- 物资调度tab大 --%>
	        <div class="tab_content" style="display:none;">
	        	<div class="left_tree_tab_container radio_tool">
	            	<ul class="left_tree_tab" id="rdUl">
	                    <li onclick="getUserWorkOrderListByPage('resourcedispatch','pendingWorkOrder',0,10)" bizTypeCode="resourcedispatch" workOrderType="pendingWorkOrder">
	                        <a class="selected">待办入库单</a>
	                    </li>
	                    <li onclick="getUserWorkOrderListByPage('resourcedispatch','trackWorkOrder',0,10)" bizTypeCode="resourcedispatch" workOrderType="trackWorkOrder">
	                        <a>跟踪入库单</a>
	                    </li>
	                    <li onclick="getUserWorkOrderListByPage('resourcedispatch','superviseWorkOrder',0,10)" bizTypeCode="resourcedispatch" workOrderType="superviseWorkOrder">
	                        <a>监督入库单</a>
	                    </li>
	                </ul>
	            </div>
	        	<%-- 物资调度单 Table --%>
	        	<div class="tab_content_main left_tree_tab_content">
	        	 <table class="tab_content_table nowrap_td tc" id="rdTable">
	               <tr>
	                   <th style="width:30px;"></th>
	                   <th style="width:30px;"><input type="checkbox" /></th>
	                   <th>工单号</th>
	                   <th style="width:32%;">工单主题</th>
	                   <th>创建人</th>
	                   <th>创建时间</th>
	                   <th>要求完成时间</th>
	                   <th>当前责任人</th>
	                   <th>工单状态</th>
	               </tr>
	            </table>
	           </div>
	        </div>
	        <div class="tab_content_bottom">
	        	<span class="result_left" id="woResult" style="position:absolute; left:10px; bottom:5px;">
		        	共0条记录
		        </span>
		        
		        <%-- 分页组件  --%>
		        <div style="position:absolute; right:24px; bottom:0px;">
		        	 <input type='hidden' id='pageSize' value='10' >
			       	 <a title="首页" class="paging_link page-first" onclick="firstPage()"></a>
			         <a title="上一页" class="paging_link page-prev" onclick="backPage()"></a>
			         <i class="paging_text">&nbsp;第&nbsp;</i>
			         <input type="text" id='curPageIndex' value="1" class="paging_input_text" style="width:32px;">&nbsp;页/共&nbsp;
			         <i id="totalPage" class="paging_text">3</i>页&nbsp;
			         <a title="GO" class="paging_link page-go" onclick="skipToPage()">GO</a>
			         <a title="下一页" class="paging_link page-next" onclick="nextPage()"></a> 
			         <a title="末页" class="paging_link page-last" onclick="lastPage()"></a>
		        </div>
	        </div>
    	</div>
	    
		<div id="loading_div"><em>数据加载中，请稍后...</em></div>
		<%-- 放置甘特图插件  --%>
	  	<div class="gantt" id="report_gantt" style="display: none;  position: absolute; z-index: 250;background:#F9F9F9;border: 1px solid #CCCCCC;">
	      
	  	</div>
	  
	   <%-- 阻隔层 --%>
	   <div class="wait_white"></div>
	   
	   <div id="please_res_wait_cue_div" class="please_wait_cue_div">
			<img src="image/loading.gif" width="20" height="20" style="vertical-align:middle" />&emsp;加载数据中,请稍候...<span class="please_wait_second">1</span>
	   </div>
   
</body>
</html>

