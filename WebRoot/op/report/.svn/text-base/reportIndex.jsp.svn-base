<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>统计报表</title>
<link rel="stylesheet" href="../../css/base.css" type="text/css" />
<link rel="stylesheet" href="../../css/public.css" type="text/css" />
<link rel="stylesheet" href="../../css/input.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/dialog/dialog.css" type="text/css" />
<link rel="stylesheet" href="css/statements.css" type="text/css" />
<link rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="../css/leftMenu.css"></script>

<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js" ></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="js/statements.js"></script>
<script type="text/javascript" src="js/reportIndex.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/highcharts.src.js"></script>
<script type="text/javascript" src="../../jslib/highcharts/exporting.src.js"></script>
<script type="text/javascript" src="js/report.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="../jslib/networkResourceViewDivPage.js"></script>
<script type="text/javascript" src="js/reportnetworkResourece.js"></script>
<script type="text/javascript" src="../js/leftMenu.js"></script>
<script>
	
</script>
<Style>
#tree2 span{ cursor:pointer;}
#tree2 span:hover{ background:#eee;}
#tree2 {
height:262px;
overflow:auto;
}
#tree3 span{ cursor:pointer;}
#tree3 span:hover{ background:#eee;}
#tree3 {
height:262px;
overflow:auto;
}
</Style>
</head>

<body style="overflow: hidden;">
<%--主体开始--%>
<div id="statements_content" style="margin-top:5px;">

    <%--
	<div class="statements_left">
	    <div class="statements_menu_title">
		    <span>导航菜单</span>
		</div>
		<div class="statements_menu_info">
			<div class="tree_menu">
				<h3>关键考核指标统计分析</h3>
				<ul id="" class="tree_menu_info tree" style="display:none;">
					<li><a title="关键考核指标统计分析" href="javascript:void(0);" class="tree-parent">关键考核指标统计分析</a>
						<ul class="">
							<li><a title="故障抢修" href="#"  onclick="getUrgentRepairworkorder('reportUrgentRepairworkorderStatistics');">故障抢修</a>
								
							</li>
						</ul>
					</li>
				</ul>
				<p class="module_up_down"><img src="images/down.gif" alt="" /></p>
			</div>
			<div class="tree_menu">
				<h3>运维生产统计分析</h3>
				<ul id="" class="tree_menu_info tree" style="display:none;">
				    <li><a title="故障抢修" href="#" onclick="getUrgentRepairworkorder('reportUrgentRepairworkorderStatistics');">故障抢修</a>
						<%-- <ul class="">
							<li><a title="故障工单数量" href="#" onclick="getUrgentRepairworkorder('workorderCount');">工单数量</a></li>
							<li><a title="故障处理及时率" href="#" onclick="getUrgentRepairworkorder('workorderProcessTimeRate');">故障处理及时率</a></li>
							<li><a title="工单处理历时" href="#" onclick="getUrgentRepairworkorder('troudleshootingTime');">工单处理历时</a></li>
						</ul> --%><%--
					</li>
					<li><a title="巡检作业" href="#" class="tree_menu_no">巡检作业</a></li>
					<li><a title="业主维系" href="#" class="tree_menu_no">业主维系</a></li>
				</ul>
				<p class="module_up_down"><img src="images/down.gif" alt="" /></p>
			</div>
			<div class="tree_menu">
				<h3>网络资源统计分析</h3>
				<ul id="" class="tree_menu_info tree" style="display:none;">
				    <li><a title="11" href="#">网络资源</a>
						<ul class="">
									<li><a title="室外点资源统计" href="#" onclick="clickshowReportNET('Outdoor');">室外点资源统计</a></li>
									<li><a title="基站统计" href="#" onclick="clickshowReportNET('BaseStation');">基站统计</a></li>
									<li><a title="小区统计" href="#" onclick="clickshowReportNET('Cell');">小区统计</a></li>
									<li><a title="关键动力资源统计" href="#" onclick="clickshowReportNET('Power');">关键动力资源统计</a></li>
									<li><a title="资源盘点统计" href="#" onclick="clickshowReportNET('');">资源盘点统计</a></li>
						</ul>
					</li>
				</ul>
				<p class="module_up_down"><img src="images/down.gif" alt="" /></p>
			</div>
			<div class="tree_menu">
				<h3 class="tree_menu_no">生产资源统计分析</h3>
				<ul id="" class="tree_menu_info tree" style="display:none;">
				    <li><a title="人员" href="javascript:void(0);">人员</a>
						<ul class="">
							<li><a title="人员数量" href="#" onclick="forwardTOReportStaffPage('staffBySkillCount')">人员数量</a></li>
							<li><a title="人员报销" href="#">人员报销</a></li>
						</ul>
					</li>
					<li><a title="车辆" href="javascript:void(0);">车辆</a>
						<ul class="">
							<li><a title="车辆数量" href="#" onclick="forwardTOReportCarPage('carByProjectCount')">车辆数量</a></li>
							<li><a title="车辆报销" href="#" onclick="forwardTOReportCarPage('carFeeByProjectCount')">车辆报销</a></li>
						</ul>
					</li>
					<li><a title="物资" href="javascript:void(0);">物资</a>
						<ul class="">
							<li><a title="物资数量" href="#">物资数量</a></li>
							<li><a title="物资报销" href="#">物资报销</a></li>
						</ul>
					</li>
				</ul>
				<p class="module_up_down_no"><img src="images/down.gif" alt="" /></p>
			</div>
			<div class="tree_menu">
				<h3 class="tree_menu_no">日常报表</h3>
				<ul id="" class="tree_menu_info tree" style="display:none;">
				    <li><a title="日报" href="#">日报</a></li>
					<li><a title="周报" href="#">周报</a></li>
					<li><a title="月报" href="#">月报</a>
						<ul class="">
							<li><a title="运维生产统计分析" href="javascript:void(0);" >运维生产统计分析</a>
								<ul class="">
									<li><a title="故障抢修" href="#">故障抢修</a></li>
									<li><a title="巡检作业" href="#">巡检作业</a></li>
									<li><a title="修缮" href="#">修缮</a></li>
								</ul>
							</li>
							<li><a title="网络资源统计分析" href="#">网络资源统计分析</a>
								
							</li>
							<li><a title="生产资源统计分析" href="#">生产资源统计分析</a></li>
						</ul>
					</li>
					<li><a title="季报" href="#">季报</a></li>
				</ul>
				<p class="module_up_down_no"><img src="images/down.gif" alt="" /></p>
			</div>
			<div class="tree_menu">
				<h3 class="tree_menu_no">报表配置管理</h3>
				<ul id="" class="tree_menu_info tree" style="display:none;">
				    <li><a title="指标类型管理" href="#">指标类型管理</a></li>
					<li><a title="指标管理" href="#">指标管理</a></li>
					<li><a title="指标考核管理" href="#">指标考核管理</a></li>
					<li><a title="报表发布管理" href="#">报表发布管理</a></li>
					<li><a title="报表数据筛选条件管理" href="#">报表数据筛选条件管理</a></li>
					<li><a title="报表定制管理" href="#">报表定制管理</a></li>
				</ul>
				<p class="module_up_down_no"><img src="images/down.gif" alt="" /></p>
			</div>
        </div>
	</div>
	--%>
	<%--左边导航菜单开始--%>
	<div class="leftMenu">
	    <div class="leftMenu_top">
		    <span onclick="openReport();">统计分析</span>
		</div>
		<%-- <div class="leftMultilevelMenu_info">
			<h3>关键考核指标统计分析</h3>
			<ul>
				<li onclick="getUrgentRepairworkorder('reportUrgentRepairworkorderStatistics');clickThis(this);" title="故障抢修"><span></span>故障抢修</li>
			</ul>
			<p class="module_up_down"><img src="../../images/up.gif" alt="" /></p>
		</div> --%>
		<div class="leftMultilevelMenu_info">
			<h3>网络资源统计</h3>
			<ul>
				<li  title="站址/基站资源数统计" onclick="getUrgentRepairworkorder('stationOrBaseStaion');clickThis(this);"><span></span>站址/基站资源数统计</li>
				<li  title="无线分类统计" onclick="getUrgentRepairworkorder('WAN');clickThis(this);"><span></span>无线分类统计</li>
			</ul>
			<p class="module_up_down"><img src="../../images/up.gif" alt="" /></p>
		</div>
		<div class="leftMultilevelMenu_info">
			<h3>运维工作统计</h3>
			<ul>
				<li onclick="getUrgentRepairworkorder('reportUrgentRepairworkorderStatistics');clickThis(this);" class="menu_selected" title="抢修工单数/历时/及时率统计"><span></span>抢修工单数/历时/及时率统计</li>
				<li onclick="getUrgentRepairworkorder('reportAnalyseFaultCountAndResourceBalance');clickThis(this);" title="每百基站人/车/任务量统计分析"><span></span>每百基站人/车/任务量统计分析</li>
				<li onclick="getUrgentRepairworkorder('reoportBaseStationOut');clickThis(this);" title="每千基站退服率统计"><span></span>每千基站退服率统计</li>
				<li onclick="getUrgentRepairworkorder('reportFaultBaseStationTopTen');clickThis(this);" title="故障数Top-10最差基站排名"><span></span>故障数Top-10最差基站排名</li>
				<li  title="故障数Top-N最差基站分布" onclick="getUrgentRepairworkorder('reportCountFaultBaseStationTopFiveTenByOrg');clickThis(this);"><span></span>故障数Top-N最差基站分布</li>
				<li  title="代维公司考核排名" onclick="getUrgentRepairworkorder('cmsReportProjectAppraisal');clickThis(this);"><span></span>代维公司考核排名</li>
			</ul>
			<p class="module_up_down"><img src="../../images/up.gif" alt="" /></p>
		</div>
		<div class="leftMultilevelMenu_info">
			<h3>生产资源统计</h3>
			<ul>
				<li  title="人员数统计" onclick="getUrgentRepairworkorder('reportOrgStaffCount');clickThis(this);"><span></span>人员数统计</li>
				<li  title="人员任务量统计" onclick="getUrgentRepairworkorder('reportOrgWorkOrderCount');clickThis(this);"><span></span>人员任务量统计</li>
				<li  title="人员及任务数综合统计" onclick="getUrgentRepairworkorder('reportOrgCompareCount');clickThis(this);"><span></span>人员及任务数综合统计</li>
				<li  title="车辆数统计" onclick="getUrgentRepairworkorder('reportCarCensus');clickThis(this);"><span></span>车辆数统计</li>
				<li  title="车辆里程统计" onclick="getUrgentRepairworkorder('reportCarMileage');clickThis(this);"><span></span>车辆里程统计</li>
				<li  title="车辆及里程综合统计" onclick="getUrgentRepairworkorder('reportCarMileageCensus');clickThis(this);"><span></span>车辆及里程综合统计</li>
				<li  title="车辆油费统计" onclick="getUrgentRepairworkorder('reportCarOil');clickThis(this);"><span></span>车辆油费统计</li>
				
				<%-- <li  title="车辆路桥费统计" onclick="clickThis(this);"><span></span>车辆路桥费统计</li> --%>
			</ul>
			<p class="module_up_down"><img src="../../images/up.gif" alt="" /></p>
		</div>
		<%-- <div class="leftMultilevelMenu_info">
			<h3 class="tree_menu_no">生产资源统计分析</h3>
			<ul style="display:none;>
				<li onclick="getUrgentRepairworkorder('reportUrgentRepairworkorderStatistics');clickThis(this);" title="故障抢修"><span></span>人员数量</li>
			</ul>
			<p class="module_up_down_no"><img src="../../images/down.gif" alt="" /></p>
		</div> --%>
	</div>
	<%--显示/隐藏左边栏--%>
	<div class="menu_title_tool">
	    <div class="tool"></div>
	</div>
    <%--左边导航菜单结束--%>
	
	<%--右边报表开始--%>
	<div class="statements_right" id="statements_right" style="overflow-x: hidden; overflow-y: auto; height: 750px;position: relative;">
	</div>
	<%--右边报表结束--%>
</div>
<%--主体结束--%>
</body>
</html>
