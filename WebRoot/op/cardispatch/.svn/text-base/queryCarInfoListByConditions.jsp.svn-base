<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*" errorPage=""%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base target="_self" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车辆综合查询</title>
<link rel="stylesheet" type="text/css" href="../../css/base.css" />
<link rel="stylesheet" type="text/css" href="../../css/public.css" />
<link rel="stylesheet" type="text/css" href="../../css/public-table.css" />
<link rel="stylesheet" type="text/css" href="css/car.css" />
<link type="text/css" rel="stylesheet" href="../../jslib/gantt/gantt_small.css" />
<link type="text/css" rel="stylesheet" href="css/alert_div.css" />
<link rel="stylesheet" type="text/css" href="../../jslib/jquery/css/jquery-ui-1.8.23.custom.css" />
<link rel="stylesheet" href="../../jslib/jquery/css/jquery.treeview.css" type="text/css"></link>
<link type="text/css" rel="stylesheet" href="../../jslib/paging/iscreate-paging.css" />
<%-- js --%>
<script type="text/javascript" src="../../jslib/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="../../jslib/common.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.treeview.js"></script>
<script type="text/javascript" src="../jslib/generateTree.js"></script>
<script type="text/javascript" src="../../js/public.js"></script>
<script type="text/javascript" src="../../jslib/date/date.js"></script>
<script type="text/javascript" src="../../jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="js/util/dateutil.js"></script>
<script type="text/javascript" src="js/queryCarInfoListByConditions.js"></script>
<%-- 加载地图模块需要引入的文件 --%>
<script type="text/javascript" src="js/util/objutil.js"></script>
<script type="text/javascript"
	src="../../jslib/gis/jslib/api/iscreate_map.js"></script>
<script type="text/javascript" src="../../jslib/gantt/gantt.js"></script>
<script type="text/javascript" src="../../jslib/paging/paging.js"></script>
<script type="text/javascript" src="js/util/input.js"></script>

<style type="text/css">
#treeDiv,#treeDiv2 {
	z-index: 50;
	display: none;
	position: absolute;
	overflow: auto;
	background: #fff;
	border: 1px solid #ccc;
	width: 190px;
	min-height: 186px;
	text-align: left;
	margin-top: 1px;
	top: 23px;
	left: 0;
	line-height: 18px;
}
.gis_right_information {
	position: absolute;
	right: 0px;
	top: 78px;
	background: #fff;
	height: 734px;
	width: 300px;
	border-left: 1px solid #ccc;
	display: none;
}

.gis_right_information_top {
	line-height: 28px;
	border: 1px solid #ccc;
	border-left: none;
	border-right: none;
	text-align: center;
	background: #eee;
}

.close_gis_right {
	height: 20px;
	width: 20px;
	position: absolute;
	right: 5px;
	top: 5px;
	background: url(images/shut_down.png) no-repeat;
	border: 1px solid #bbb;
	border-radius: 3px;
}
/*mapView*/
#map_canvas {
	top: 0px;
	width: 100%;
	height:580px;
}

/*播放器*/
#replayCtrl .progress {
	border-radius: 3px;
	background: none repeat scroll 0 0 #FFFFFF;
	border: 1px solid #01aaef;
	display: inline-block;
	top: -1px;
	width: 145px;
	margin: 5px 0;
}

#replayCtrl .progress div {
	background: none repeat scroll 0 0 #7bc2eb;
	margin-top: 0;
	padding: 1px 0;
}

#replayCtrl .progress div div {
	border-bottom: 3px solid #5dabd8;
	border-top: 3px solid #92d1f5;
	height: 0;
	overflow: hidden;
}

#progress_control {
	border: 1px solid #ccc;
	width: 196px;
	padding: 2px;
	height: 25px;
}

#progress_control img {
	margin-left: 7px;
	cursor: pointer;
}

/*自动补全*/
#resultDiv ul {
	cursor: default;
	margin: 0px;
	padding: 0px;
	list-style: none;
}

.esultDivLiHover {
	background: #cfcfcf;
}

#resultDiv {
	background: Wheat;
	border-bottom: 1px solid #CCCCCC;
	border-left: 1px solid #CCCCCC;
	border-right: 1px solid #CCCCCC;
	position: absolute;
	top: 90px;
	width: 144px;
	z-index: 100;
	display: none;
}

#moreMenuDiv {
	display: none;
	background: none repeat scroll 0 0 #FFFFFF;
	border: 1px solid #CCCCCC;
	box-shadow: 3px 3px 5px #999999;
	left: 55px;
	padding: 5px 10px;
	position: absolute;
	top: 105px;
	z-index: 100;
}
</style>
</head>
<body>
<%-- 隐藏域  --%>
<input type="hidden" value="${carLocusInfoMap.carNumber}" id="hiddenCarNumber"/>
<input type="hidden" value="${carLocusInfoMap.beginTime}" id="hiddenBeginTime"/>
<input type="hidden" value="${carLocusInfoMap.endTime}" id="hiddenEndTime"/>
<input type="hidden" value="${carLocusInfoMap.woId}" id="hiddenWoId"/>
<input type="hidden" value="${param.curFrame}" id="hiddenFrame"/>
<input type="hidden" value="${carLocusInfoMap.curPosition}" id="curPosition"/>
<div id="container">
	<%-- 搜索区tab --%>
    <div class="top_tab_container">
        
        <div class="tab_content_container">
        	<div class="search_tab clearfix">
                <ul>
                    <li class="ontab">普通查询</li>
                    <li>值班查询</li>
                </ul>
            </div>
            <div class="search_tab_content" style="display:block;">
                <table class="search_table">
                    <tr>
                        <td class="tl">
	                        <div style="position:relative;">
	                        	<input type="hidden" value="${param.curFrame}" id="hiddenFrame"/>
								<input type="hidden" id="bizunitIdText" />
				               	<input type="text" id="bizunitNameText" placehoder = "请选择组织" readonly="readonly" /><a class="areaButton" title="选择组织" href="javascript:void(0);" id="orgSelectButton"></a>
			                    <div id="treeDiv">
			                    	<%-- 放置组织架构树  --%>
			                    </div>
	                            <select id="carType">
	                            	<option value="">请选择车辆类型</option>
	                                <option value="面包车">面包车</option>
	                                <option vlaue="小轿车">小轿车</option>
	                                <option value="小货车">小货车</option>
	                            </select>
	                            <select id="carState">
	                                <option value="">请选择车辆状态</option>
	                                <option value="3">离线</option>
	                                <option value="2">静止</option>
	                                <option value="1">行驶中</option>
	                                <option value="0">待初始化</option>
	                            </select>
	                            <input type="text" placeholder="请输入车辆牌照" id="carNumber"/>
		              			<input id="simpleQueryButton" type="button" value="查询" />
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="search_tab_content">
                <table class="search_table">
                    <tr>
                        <td>
                        
                        	<div style="position:relative;">
	                             <input type="hidden" id="carBizId" />
				               	<input type="text" id="carBizName" placehoder = "请选择组织" readonly="readonly" /><a  id="orgSelectButton2" class="areaButton" title="选择组织" href="javascript:void(0);"></a>
			                    <div id="treeDiv2">
			                    	<%-- 放置组织架构树  --%>
			                    </div>
	                            <input type="text" name="_dutyDate" id="dutyDate" readonly="readonly"/><a href="javascript:void(0)" class="dateButton" onclick="fPopCalendar(event,document.getElementById('dutyDate'),document.getElementById('dutyDate'),false)"></a>
	                            <input type="checkbox" name="_freId" class="shiftType" checked="checked" value="1"/> 白班
	                            <input type="checkbox" name="_freId" class="shiftType" value="2"/> 晚班
	                            <input type="button" style="width:50px;" value="查询" id="multiSearchButton"/>
	                        </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    
    <%-- 展示区tab --%>
    <div class="main_tab_container">
    
        <ul class="present_tab clearfix">
            <li id="listViewButton">
                <a href="javascript:void(0);" class="list select"><em></em>列表呈现</a>
            </li>
            <li id="mapViewButton">
                <a href="javascript:void(0);" class="gis" style="border-left:none;"><em></em>GIS呈现</a>
            </li>
        </ul>
        
        <%-- 工具
        <div class="main_tab_tool">
        	<input type="checkbox" id="autoRefreshButton" checked="checked"/>自动刷新
			<select id="autoRefreshTime">
				<option value="10000">10秒</option>
				<option value="20000">20秒</option>
				<option value="30000">30秒</option>
				<option value="60000">1分钟</option>
				<option value="300000" selected="selected">5分钟</option>
			</select>
        </div>
         --%>
        <%-- 搜索结果、地图展示区 --%>
        <div class="search_result">
            <div class="present_tab_content" id="listView">	 	 	
                <table class="search_result_table" id="resultListTable">
                    <tr>
                        <th>车辆牌照</th>
                        <th>任务总数</th>
                        <th>车辆归属地</th>
                        <th>车辆类型</th>
                        <th>司机姓名</th>
                        <th>司机电话</th>
                        <th>定位状态</th>
                        <th>是否排班</th>
                        <th>操作</th>
                    </tr>
                </table>
                <%-- 默认每页10条或20条记录 --%>
                <input type="button" value="分配出车任务" id="assignButton" />
                <div id="pageContent "></div>
            </div>
            
            <div class="present_tab_content" style="display:none; position:relative;" id="MapView">
                <%-- 放置地图  --%>
         		<div id="map_canvas">  </div>
         		<%-- 车辆轨迹播放控件  --%>
	         	<div id="replayDiv">
	         		<div id="replayCtrl" >
				    	回放进度：<span id="replayIndexDiv" style="margin-left: 70px;">倍数：<span id="beishu"></span></span><br />
						<span class="progress" style="padding-left:0px;">
						    <div id="progressDiv" style="width: 0%">
							    <div></div>
							</div>
						</span>
						<span id="progressTime" style="font-weight:normal;">0:00:00</span>
					</div>
				    <div id="progress_control">
				    	<img id="back" src="icon-for-map/last.png" />
				        <img id="start" src="icon-for-map/play.png" />
				        <img id="pause" style="display:none" src="icon-for-map/pause.png" />
				        <img id="forward" src="icon-for-map/next.png" />
				        <img id="finish" src="icon-for-map/stop.png" />
				        <img id="toExcel" src="icon-for-map/excel.png" />
				    </div>
			   </div>
			   <%-- 右边轨迹回放查询区 BEGIN --%>
			    <div class="gis_right_information" id="rightInformation">
			    	<div class="gis_content_right_show">
						<img id="gis_content_right_show_img" src="images/show_right.png">
					</div>
	           		<div class="gis_right_information_top">
	               	   <h4>查看车辆轨迹信息
	               	   </h4>
	               	   <div class="gis_content_right_main">
	               	   		<table class="main-table1" style="width:96%;">
	                    		<tr>
	                    			<th colspan="2" class="main-table1-title tc">车辆轨迹查询</td>
	                    		</tr>
	                    		<tr>
	                    			<td>车辆牌照：</td>
	                    			<td align="left">
	                    				<input type="text" id="queryCarNumber" value=""/>
	                    				<div id="resultDiv"></div>
	                    			</td>
	                    		</tr>
	                    		<tr>
	                    			<td>轨迹回放日期：</td>
	                    			<td align="left">
	                    				<input type="text" id="locusDate" readonly="readonly" value="" onclick="fPopCalendar(event,document.getElementById('locusDate'),document.getElementById('locusDate'),false)"/>
	                    			</td>
	                    		</tr>
	                    		<tr style="display:none;">
	                    			<td>轨迹结束日期：</td>
	                    			<td align="left">
	                    				<input type="text" id="locusEndTime" readonly="readonly" value="" onclick="fPopCalendar(event,document.getElementById('locusEndTime'),document.getElementById('locusEndTime'),false)"/>
	                    			</td>
	                    		</tr>
	                    		<tr>
	                    			<td colspan="2" class="tc">
	                    				<input type="button" value="确定" id="search"/>
	                    			</td>
	                    		</tr>
	                   		</table>
	                   		<div class="map_table" style="height:300px;" id="taskDiv">
	                   			<div class="top_tab">
	                   				<ul class="gis_content_right_main_tab">
	                   					<li class="active">按出车任务</li>
	                   					<li>按时段</li>
	                   				</ul>
	                   			</div>
	                    		<div class="top_tab_content">
		                    		<table class="main-table1 tc" id="taskTable" style="width:96%;">
				                   		<tr>
				                   			<th>操作</th>
				                   			<th>工单号</th>
				                   			<th>仪表里程数</th>
				                   			<th>GPS里程数</th>
				                   		</tr>
		                    		</table>
		                    	</div>
	                    		<div class="top_tab_content" style="display:none;">
	                    			<table class="main-table1 tc" id="hoursTable" style="width:96%;">
				                   		<tr>
				                   			<th>操作</th>
				                   			<th>小时数</th>
				                   			<th>GPS里程数</th>
				                   		</tr>
				                   		<tr>
				                   			<td><input type="checkbox" bTime="00:00:00" eTime="08:00:00" onclick="showRwCarLocus('',this)"/></td>
				                   			<td>0-8(时)</td>
				                   			<td><em id="zehEm"></em></td>
				                   		</tr>
				                   		<tr>
				                   			<td><input type="checkbox" bTime="09:00:00" eTime="11:00:00" onclick="showRwCarLocus('',this)"/></td>
				                   			<td>9-11(时)</td>
				                   			<td><em id="nehEm"></em></em></td>
				                   		</tr>
				                   		<tr>
				                   			<td><input type="checkbox" bTime="12:00:00" eTime="13:00:00" onclick="showRwCarLocus('',this)"/></td>
				                   			<td>12-13(时)</td>
				                   			<td><em id="tthEm"></em></td>
				                   		</tr>
				                   		<tr>
				                   			<td><input type="checkbox" bTime="14:00:00" eTime="18:00:00" onclick="showRwCarLocus('',this)"/></td>
				                   			<td>14-18(时)</td>
				                   			<td><em id="fehEm"></em></td>
				                   		</tr>
				                   		<tr>
				                   			<td><input type="checkbox" bTime="19:00:00" eTime="23:59:59" onclick="showRwCarLocus('',this)"/></td>
				                   			<td>19-24(时)</td>
				                   			<td><em id="nthEm"></em></td>
				                   		</tr>
		                    		</table>
	                    		</div>
	                    	</div>
	               	   </div>
	               </div>
	           </div>
			   <%-- 右边轨迹回放查询区 END --%>
            </div>
        </div>
    </div>
</div>
<%-- 放置甘特图插件  --%>
<div id="ctGantt" style="display: none;  position: absolute; z-index: 250;background:#F9F9F9;border: 1px solid #CCCCCC;"></div>
<%-- 甘特图 END --%>

<%-- 更多菜单  --%>
<div id="moreMenuDiv"></div>
</body>
</html>



