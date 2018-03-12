<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>NCS结构分析</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%--
	<link rel="stylesheet" type="text/css" href="styles.css">
	--%>
<%@include file="commonheader.jsp"%>
<script type="text/javascript" src="js/rno_structure_ncs_analysis.js?v=<%=(String)session.getAttribute("version")%>"></script>
<%--baidu--%>  
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>
<%-- base 转换器 --%>  
<script type="text/javascript"
	src="http://developer.baidu.com/map/jsdemo/demo/convertor.js"></script>
<script type="text/javascript"  src="jslib/base64/base64.js"></script>
<script type="text/javascript" src="jslib/base64/convertor.js"></script>
<script type="text/javascript">
	var $ = jQuery.noConflict();
	$(function() {
		//tab选项卡
		tab("div_tab", "li", "onclick");//项目服务范围类别切换
		tab("cluster_tab", "li", "onclick");
	})
</script>

<style type="text/css">
.dict_left {
	width: 130px;
	float: left;
	left: 0;
	z-index: 1;
	border: 1px;
	margin: 5px
}

.dict_right {
	width: 100%;
	left: 200px;
	float: left;
	border: 1px;
	margin: 5px
}
.oldTaskTable {
	width: 100%;
}
.oldTaskTable th{ 
	padding:0px 10px; 
	color:#444; 
	white-space:nowrap; 
	border:#CCC 1px solid; 
	line-height:30px; 
	font-weight:bold
}
.oldTaskTable tr td{ 
	border:1px solid #CCC; 
	padding:4px; 
	padding-left:6px; 
	color:#000; 
	line-height:21px; 
	vertical-align:middle; 
	background:WhiteSmoke;
	text-align:center;
	word-break:break-all;
}
</style>
</head>

<body>

	<div class="loading_cover" id="loadingDiv" style="display: none">
		<div class="cover"></div>
		<h4 class="loading">
			正在加载 <em class="loading_fb" id="tipcontentId"></em>,请稍侯...
		</h4>
	</div>
	<div class="div_left_main" style="width: 100%">
		<div class="div_left_content">
			<div style="padding: 10px">
				<div id="frame" class="loadframe"
					style="border: 1px solid #ddd;width:100%;float:left">
					<div id="div_tab" class="div_tab divtab_menu">
						<ul style="width:100%">
							<li class="selected" style="width:11%" id="analysisTaskListLi">分析任务列表</li>
							<li id='structDataLi' style="width:14%">结构指标分析结果</li>
							<li id='clusterDataLi' style="width:13%">簇指标分析结果</li>
							<li id='optSuggestionLi' style="width:13%">自动优化建议</li>
							<li id='rendererDataLi' style="width:7%">专题图</li>
						</ul>
					</div>

					<div class="divtab_content">
						<div id="div_tab_0">
						<form id="ncsTaskDataForm" method="post">
							<input type="hidden" id="hiddenPageSize" name="page.pageSize"
								value="25" /> <input type="hidden" id="hiddenCurrentPage"
								name="page.currentPage" value="1" /> <input type="hidden"
								id="hiddenTotalPageCnt" /> <input type="hidden"
								id="hiddenTotalCnt" />
							<div style="margin-bottom: 2px;margin-left: 10px"><font style="font-weight: bold;">结构分析任务列表</font>		<a href="#" id="searchTask" style="text-decoration: underline;margin-left: 6px;margin-right: 6px">搜索任务</a>		<input type="button" id="newTask" name="newTask" value="新增任务"/></div>
							<div id="searchTaskPlane" style="display: none">
								<table class="main-table1 half-width"
									style="padding-top: 10px;margin:10px;width:60%">
									<tr>
										<td class="menuTd" style="text-align: center"><span
											style="padding-top: 0px">任务状态</span></td>
										<td class="menuTd" style="text-align: center">任务提交时间</td>
										<td class="menuTd" style="text-align: center">任务名称</td>
										<td class="menuTd" style="text-align: center">任务描述</td>
									</tr>
									<tr>
										<td><select name="cond['taskGoingStatus']">
												<option value="">全部</option>
												<option value="运行中">运行中</option>
												<option value="失败">失败</option>
												<option value="成功完成">成功完成</option>


										</select></td>
										<td style="text-align: left">从 <s:set name="begtime"
												value="new java.util.Date()" /> <input
											name="cond['begTime']"
											value="<s:date name="BEGTIME" format="yyyy-MM-dd HH:mm:ss" />"
											type="text"
											onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											readonly class="Wdate input-text" style="width: 132px;" /> <br />至
											<s:set name="endtime" value="new java.util.Date()" /> <input
											name="cond['endTime']"
											value="<s:date name="ENDTIME" format="yyyy-MM-dd HH:mm:ss" />"
											type="text"
											onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
											readonly class="Wdate input-text" style="width: 132px;" />

										</td>
										<td>
											<input id="taskNameSearch"  type="text" style="width:100%" name="cond['taskName']" />
										</td>
										<td>
											<input id="taskDescSearch"  type="text" style="width:100%" name="cond['taskDesc']" />
										</td>
									</tr>
									<tr>
										<td style=" text-align: right" colspan="4"><input
											type="button" onclick="" value="查  询" style="width: 90px;"
											id="searchTaskBtn" name="search" /></td>
									</tr>
								</table>
							</div>
						</form>
						<div>
							<table id="queryTaskResultTab" class="greystyle-standard"
								width="100%">
								<tr>
									<th>任务名称</th>
								    <th>汇总范围</th>
								    <th>汇总区域名称</th>
								    <th>分析的NCS文件数量</th>
								    <th>分析的MRR文件数量</th>
									<th>启动时间</th>
									<th>完成时间</th>
									<th>任务状态</th>
									<th>操作</th>
									<th>任务描述</th>
								</tr>
							</table>
							<form id='getReportForm' action='exportNcsAnalysisReportAction' method='post' style="display:none">
							     <input type='input' id='taskId' name='taskId' value='' />
							</form>
						</div>
						<div class="paging_div" id='ncsTaskResultPageDiv'
							style="border: 1px solid #ddd">
							<span class="mr10">共 <em id="emTotalCnt" class="blue">0</em>
								条记录
							</span> <a class="paging_link page-first" title="首页"
								onclick="showListViewByPage('first',doQueryNcsTask,'ncsTaskDataForm')"></a>
							<a class="paging_link page-prev" title="上一页"
								onclick="showListViewByPage('back',doQueryNcsTask,'ncsTaskDataForm')"></a>
							第 <input type="text" id="showCurrentPage"
								class="paging_input_text" value="1" /> 页/<em
								id="emTotalPageCnt">0</em>页 <a class="paging_link page-go"
								title="GO"
								onclick="showListViewByPage('num',doQueryNcsTask,'ncsTaskDataForm')">GO</a>
							<a class="paging_link page-next" title="下一页"
								onclick="showListViewByPage('next',doQueryNcsTask,'ncsTaskDataForm')"></a>
							<a class="paging_link page-last" title="末页"
								onclick="showListViewByPage('last',doQueryNcsTask,'ncsTaskDataForm')"></a>
						</div>
					</div>
					</div>
					
					<div id="div_tab_1" style="display:none;overflow:auto">
						结构指标数据
						<table id="ncsStructDataTab" class="greystyle-standard"
							width="100%">
							<tr>
								<th>编号</th>
								<th>小区</th>
								<th>小区频点数</th>
								<th>被干扰系数</th>
								<th>网络结构指数</th>
								<th>冗余覆盖指数</th>
								<th>重叠覆盖度</th>
								<TH>干扰源系数</TH>
								<TH>过覆盖系数</TH>
								<TH>小区检测次数</TH>
								<TH>小区检测次数（不含室分）</TH>
								<TH>理想覆盖距离</TH>
								<TH>关联邻小区数</TH>
								<TH>关联邻小区数（不含室分）</TH>
								<TH>小区覆盖分量</TH>
								<TH>小区容量分量</TH>
							</tr>
						</table>
						
					</div>
					<div id="div_tab_2" style="display:none">
						<div id="cluster_tab" class="div_tab divtab_menu">
							<ul style="width:100%">
								<li class="selected" id='allClusterLi' style="width:15%">所有连通簇列表</li>
								<li id="maxClusterLi" style="width:15%">最大连通簇列表</li>
								<li id="clusterCellLi" style="width:15%">簇相关小区列表</li>
							</ul>
						</div>
						<div class="divtab_content">
							<div id="cluster_tab_0">
								<table id="ncsClusterDataTab1" class="greystyle-standard"
									width="100%">
									<tr>
										<th>编号</th>
										<th>簇编号</th>
										<th>Count（小区数）</th>
										<th>Trxs（频点数/载波数）</th>
										<th>簇约束因子</th>
										<th>簇权重</th>
										<th>Sectors（小区列表）</th>
									</tr>
								</table>
							</div>
							<div id="cluster_tab_1" style="display:none">
								<table id="ncsClusterDataTab2" class="greystyle-standard"
									width="100%">
									<tr>
										<th>编号</th>
										<th>簇编号</th>
										<th>小区名</th>
										<th>小区中文名</th>
										<th>小区载频</th>
										<th>簇TCH载频数</th>
									</tr>
								</table>
							</div>
							<div id="cluster_tab_2" style="display:none">
								<table id="ncsClusterDataTab3" class="greystyle-standard"
									width="100%">
									<tr>
										<th>编号</th>
										<th>主小区</th>
										<th>簇编号</th>
										<th>簇内小区载波数</th>
										<th>干扰小区</th>
									</tr>
								</table>
							</div>
						</div>
					</div>
					<div id="div_tab_3" style="display:none;overflow:auto"">
					自动优化建议
						<table id="ncsOptSuggestionTab" class="greystyle-standard"
							width="100%">
							<tr>
								<th>编号</th>
								<th>小区名</th>
								<th>小区中文名</th>
								<th>理想覆盖距离</th>
								<th>过覆盖系数</th>
								<th>关联邻小区</th>
								<th>关联邻小区（不含室分）</th>
								<th>覆盖分量</th>
								<th>容量分量</th>
								<th>小区经度</th>
								<th>小区纬度</th>
								<th>天线挂高</th>
								<th>天线下倾</th>
								<th>理想下倾</th>
								<th>是否室分</th>
								<th>定义信道数</th>
								<th>可用信道数</th>
								<th>载波配置</th>
								<th>最忙时资源利用率</th>
								<th>话务量</th>
								<th>问题标识</th>
								<th>优化建议</th>
								<th>话统参考时间</th>
							</tr>
						</table>
					</div>

					 <div id="div_tab_4" style="display:none">
					 	<input type="hidden" id="reportNcsTaskId" value="" />
				     	<%@include file="rno_structure_ncs_render_img.jsp"%>
				     </div>

				</div>
			</div>
		</div>
	</div>

	<div id="operInfo"
		style="display:none; top:40px;left:600px;z-index:999;width:400px; height:40px; background-color:#7dff3f; filter:alpha(Opacity=80);-moz-opacity:0.5;opacity: 0.5;z-index:9999;position: fixed;">
		<table height="100%" width="100%" style="text-align:center">
			<tr>
				<td><span id="operTip"></span></td>
			</tr>
		</table>

	</div>

</body>
</html>
